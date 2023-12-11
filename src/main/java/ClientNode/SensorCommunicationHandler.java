package ClientNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
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
    Random random = new Random();
    String[] sensorTypes = {"HUMIDITY", "TEMPERATURE", "LIGHT", "WIND"};
    String[] actuatorTypes = {"WINDOW", "HEATER", "AC", "DOOR"};

    // Generate between 2 and 5 sensor nodes
    int numSensorNodes = 2 + random.nextInt(4);
    for (int i = 0; i < numSensorNodes; i++) {
      HashMap<Long, Sensor> sensors = new HashMap<>();
      HashMap<Long, Actuator> actuators = new HashMap<>();

      // Add 2 to 3 sensors
      int numSensors = 2 + random.nextInt(2);
      for (int j = 0; j < numSensors; j++) {
        String sensorType = sensorTypes[random.nextInt(sensorTypes.length)];
        Sensor sensor = new Sensor(random.nextInt(100), sensorType, ++id);
        sensors.put(sensor.getId(), sensor);
      }

      // Add 2 to 3 actuators
      int numActuators = 2 + random.nextInt(2);
      for (int k = 0; k < numActuators; k++) {
        String actuatorType = actuatorTypes[random.nextInt(actuatorTypes.length)];
        Actuator actuator = new Actuator(random.nextBoolean(), actuatorType, ++id);
        actuators.put(actuator.getId(), actuator);
      }

      SensorNode sensorNode = new SensorNode(i + 1, sensors, actuators);
      sensorNodes.add(sensorNode);
    }
  }

  /**
   * Start sending data.
   */
  public void startSendingData() {
    for (SensorNode node : sensorNodes) {
      try {
        node.connectToServer(serverAddress, serverPort);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(node::randomizeAllSensors, 0, 5, TimeUnit.SECONDS);
        executor.scheduleAtFixedRate(node::sendSensorData, 0, 5, TimeUnit.SECONDS);
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
