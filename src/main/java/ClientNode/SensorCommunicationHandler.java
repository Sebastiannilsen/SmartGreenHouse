package ClientNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The type Sensor communication handler.
 */
public class SensorCommunicationHandler {

  private String serverAddress;
  private int serverPort;
  private List<SensorNode> sensorNodes;

  /**
   * Instantiates a new Sensor communication handler.
   *
   * @param serverAddress the server address
   * @param serverPort    the server port
   */
  public SensorCommunicationHandler(String serverAddress, int serverPort) {
    this.serverAddress = serverAddress;
    this.serverPort = serverPort;
    this.sensorNodes = new ArrayList<>();
    initializeSensorNodes();
  }

  /**
   * Creates dummy sensor nodes and adds them to the list of sensor nodes.
   */
  private void initializeSensorNodes() {
    long id = 1;

    // First sensor node
    Sensor temperature = new Sensor(20, "TEMP", ++id);
    Sensor humidity = new Sensor(50, "HUM", ++id);
    Sensor light = new Sensor(100, "LIGHT", ++id);
    HashMap<Long, Sensor> sensors = new HashMap<>();
    sensors.put(temperature.getId(), temperature);
    sensors.put(humidity.getId(), humidity);


    Actuator windows = new Actuator(true, "WINDOWS", ++id);
    Actuator doors = new Actuator(true, "DOORS", ++id);

    HashMap<Long, Actuator> actuators = new HashMap<>();
    actuators.put(windows.getId(), windows);


    SensorNode sensorNode = new SensorNode(1, sensors  , actuators);
    sensorNodes.add(sensorNode);

    // second sensor node
    sensors.put(light.getId(), light);
    actuators.put(doors.getId(), doors);

    SensorNode sensorNode2 = new SensorNode(2, sensors  , actuators);

    sensorNodes.add(sensorNode2);
  }

  /**
   * Start sending data.
   */
  public void startSendingData() {
    for (SensorNode node : sensorNodes) {
      try {
        node.connectToServer(serverAddress, serverPort);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(node::randomizeAllSensors, 0, 10, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(node::sendSensorData, 0, 10, TimeUnit.SECONDS);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    SensorCommunicationHandler handler = new SensorCommunicationHandler("localhost", 12345);
    handler.startSendingData();
  }
}
