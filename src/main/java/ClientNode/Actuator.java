package ClientNode;

public class Actuator {
  private boolean isOn;
  private String type;
  private long id;


  public Actuator(boolean isOn,String type, long id) {
    this.isOn = isOn;
    this.type = type;
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public long getId() {
    return id;
  }

  public void setType(String type) {
    this.type = type;
  }

  public void setId(long id) {
    this.id = id;
  }

  public boolean isOn() {
    return isOn;
  }

  public void setOn(boolean isOn) {
    this.isOn = isOn;
  }
}
