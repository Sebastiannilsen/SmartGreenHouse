import ClientNode.Actuator;
import ClientNode.Sensor;
import ClientNode.SensorNode;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SensorCommunicationHandler {

  private final String serverAddress;
  private final int serverPort;
  private Socket socket;
  private PrintWriter out;

  public SensorCommunicationHandler(String address, int port) {
    this.serverAddress = address;
    this.serverPort = port;
  }

  public void connectToServer() throws IOException {
    socket = new Socket(serverAddress, serverPort);
    out = new PrintWriter(socket.getOutputStream(), true);
  }

  public void startSendingData() {
    ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    executor.scheduleAtFixedRate(this::generateAndSendData, 0, 10, TimeUnit.SECONDS);
  }

  private void generateAndSendData() {
    long id = 1;

    // Simulate sensor data
    Sensor temperature = new Sensor(20, "TEMP", ++id);
    Sensor humidity = new Sensor(50, "HUM", ++id);
    Sensor light = new Sensor(100, "LIGHT", ++id);
    HashMap<Long, Sensor> sensors = new HashMap<>();
    sensors.put(temperature.getId(), temperature);
    sensors.put(humidity.getId(), humidity);
    sensors.put(light.getId(), light);

    Actuator windows = new Actuator(true, "WINDOWS", ++id);
    Actuator doors = new Actuator(true, "DOORS", ++id);

    HashMap<Long, Actuator> actuators = new HashMap<>();
    actuators.put(windows.getId(), windows);
    actuators.put(doors.getId(), doors);


    SensorNode sensorNode = new SensorNode(1, sensors  , actuators);
    sensorNode.randomizeAllSensors();


    // Prepare data in a JSON format
    JSONObject data = new JSONObject();
    data.put("id", sensorNode.getId());
    data.put("sensors", sensorNode.getSensors());
    data.put("actuators", sensorNode.getActuators());

    // Send data
    out.println(data.toString());
    System.out.println("Sent data: " + data.toString());
  }

  public static void main(String[] args) {
    try {
      SensorCommunicationHandler node = new SensorCommunicationHandler("localhost", 12345); // Server address and port
      node.connectToServer();
      node.startSendingData();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
