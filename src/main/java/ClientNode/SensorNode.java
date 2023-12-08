package ClientNode;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONArray;
import org.json.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class SensorNode {
  private long id;
  private Map<Long, Sensor> sensors;
  private Map<Long, Actuator> actuators;
  private Socket socket;
  private PrintWriter out;

  public SensorNode(long id, Map<Long, Sensor> sensors, Map<Long, Actuator> actuators) {
    this.id = id;
    this.sensors = sensors;
    this.actuators = actuators;
  }

  public void connectToServer(String serverAddress, int serverPort) throws IOException {
    this.socket = new Socket(serverAddress, serverPort);
    this.out = new PrintWriter(socket.getOutputStream(), true);
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


  // Getters and setters


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
