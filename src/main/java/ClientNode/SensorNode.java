package ClientNode;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class SensorNode {
  private long id;
  private Map<Long, Sensor> sensors;
  private Map<Long, Actuator> actuators;
  private Socket socket;
  private PrintWriter out;
  private BufferedReader in;

  public SensorNode(long id, Map<Long, Sensor> sensors, Map<Long, Actuator> actuators) {
    this.id = id;
    this.sensors = sensors;
    this.actuators = actuators;
  }

  public void connectToServer(String serverAddress, int serverPort) throws IOException {
    this.socket = new Socket(serverAddress, serverPort);
    this.out = new PrintWriter(socket.getOutputStream(), true);
    this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    out.println("SENSOR_NODE");
    out.println(this.id);

    // Start listening for messages from the server
    new Thread(this::listenForServerMessages).start();
  }

  private void listenForServerMessages() {
    String line;
    try {
      while ((line = in.readLine()) != null) {
        JSONObject message = new JSONObject(line);
        processActuatorMessage(message);
      }
    } catch (IOException e) {
      System.err.println("Error reading messages from server: " + e.getMessage());
    }
  }

  private void processActuatorMessage(JSONObject message) {
    // Assuming the message contains actuator ID and its new state
    long actuatorId = message.getLong("actuatorId");
    boolean newState = message.getBoolean("value");

    Actuator actuator = actuators.get(actuatorId);
    if (actuator != null) {
      actuator.setOn(newState); // Update the actuator's state
      System.out.println("Updated actuator " + actuatorId + " to state " + newState);
    } else {
      System.err.println("Actuator " + actuatorId + " not found");
    }
  }

  public void sendSensorData() {
    JSONObject json = new JSONObject();
    json.put("id", this.id);

    JSONArray sensorsJsonArray = new JSONArray();
    for (Map.Entry<Long, Sensor> entry : sensors.entrySet()) {
      sensorsJsonArray.put(entry.getValue().toJSON());
    }
    json.put("sensors", sensorsJsonArray);

    JSONArray actuatorsJsonArray = new JSONArray();
    for (Map.Entry<Long, Actuator> entry : actuators.entrySet()) {
      actuatorsJsonArray.put(entry.getValue().toJSON());
    }
    json.put("actuators", actuatorsJsonArray);

    out.println(json.toString());
    System.out.println("Sent data to server: " + json.toString());
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Map<Long, Sensor> getSensors() {
    return sensors;
  }

  public void setSensors(Map<Long, Sensor> sensors) {
    this.sensors = sensors;
  }

  public Map<Long, Actuator> getActuators() {
    return actuators;
  }

  public void setActuators(Map<Long, Actuator> actuators) {
    this.actuators = actuators;
  }

  public void randomizeAllSensors() {
    for (Map.Entry<Long, Sensor> entry : sensors.entrySet()) {
      entry.getValue().setValue(Math.random() * 100);
    }
  }
}
