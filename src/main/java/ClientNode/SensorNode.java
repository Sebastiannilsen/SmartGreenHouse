package ClientNode;

import java.util.List;
import java.util.Map;

public class SensorNode {
  private long id;
  private Map<Long,Sensor> sensors;
  private Map<Long,Actuator> actuators;

  public SensorNode(long id, Map<Long,Sensor> sensors, Map<Long,Actuator> actuators) {
    this.id = id;
    this.sensors = sensors;
    this.actuators = actuators;
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
    for (Map.Entry<Long,Sensor> entry : sensors.entrySet()) {
      entry.getValue().setValue(Math.random() * 100);
    }
  }
}
