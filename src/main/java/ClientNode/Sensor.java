package ClientNode;

import org.json.JSONObject;

/**
 * Represents a sensor in a network of sensor/actuator nodes.
 * Each sensor has a unique identifier, a type, and a value.
 */
public class Sensor {
  private double value;
  private String type;
  private long id;

  /**
   * Constructs a new Sensor with specified value, type, and ID.
   *
   * @param value The initial value of the sensor.
   * @param type The type of the sensor (e.g., temperature, humidity).
   * @param id The unique identifier of the sensor.
   */
  public Sensor(double value, String type, long id) {
    this.value = value;
    this.type = type;
    this.id = id;
  }

  /**
   * Returns the current value of the sensor.
   *
   * @return The sensor's current value.
   */
  public double getValue() {
    return value;
  }

  /**
   * Sets the value of the sensor.
   *
   * @param value The new value to be set for the sensor.
   */
  public void setValue(double value) {
    this.value = value;
  }

  /**
   * Returns the type of the sensor.
   *
   * @return The sensor's type.
   */
  public String getType() {
    return type;
  }

  /**
   * Sets the type of the sensor.
   *
   * @param type The new type to be set for the sensor.
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Returns the unique identifier of the sensor.
   *
   * @return The sensor's ID.
   */
  public long getId() {
    return id;
  }

  /**
   * Sets the unique identifier of the sensor.
   *
   * @param id The new ID to be set for the sensor.
   */
  public void setId(long id) {
    this.id = id;
  }

  /**
   * Randomizes the sensor's value to simulate sensor data change.
   * The value is set to a random number between 0 and 100.
   */
  public void randomizeValue() {
    value = Math.random() * 100;
  }

  public JSONObject toJSON() {
    JSONObject json = new JSONObject();
    json.put("id", id);
    json.put("type", type);
    json.put("value", value);
    return json;
  }
}
