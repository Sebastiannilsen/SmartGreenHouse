package ControlPanel;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ControlPanel {

  private Map<String, DefaultListModel<String>> sensorListModelMap;
  private Map<String, DefaultListModel<JToggleButton>> actuatorListModelMap;
  private Map<String, JFrame> sensorFrames;

  public ControlPanel() {
    sensorListModelMap = new HashMap<>();
    actuatorListModelMap = new HashMap<>();
    sensorFrames = new HashMap<>();
  }

  private void createSensorWindow(String sensorId) {
    JFrame frame = new JFrame("Sensor " + sensorId);
    frame.setSize(400, 500);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    DefaultListModel<String> sensorListModel = new DefaultListModel<>();
    JList<String> sensorList = new JList<>(sensorListModel);
    JScrollPane sensorScrollPane = new JScrollPane(sensorList);

    DefaultListModel<JToggleButton> actuatorListModel = new DefaultListModel<>();
    JList<JToggleButton> actuatorList = new JList<>(actuatorListModel);
    actuatorList.setCellRenderer(new ActuatorListCellRenderer());
    actuatorList.setSelectionModel(new NoSelectionModel()); // Disable selection
    JScrollPane actuatorScrollPane = new JScrollPane(actuatorList);

    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sensorScrollPane, actuatorScrollPane);
    frame.add(splitPane, BorderLayout.CENTER);
    frame.setVisible(true);

    sensorListModelMap.put(sensorId, sensorListModel);
    actuatorListModelMap.put(sensorId, actuatorListModel);
    sensorFrames.put(sensorId, frame);
  }

  public void connectToServer(String host, int port) {
    try {
      Socket socket = new Socket(host, port);
      PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
      out.println("CONTROL_PANEL");

      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
        JSONObject json = new JSONObject(line);
        String nodeId = String.valueOf(json.getDouble("id"));
        JSONArray sensors = json.getJSONArray("sensors");
        JSONArray actuators = json.getJSONArray("actuators");

        SwingUtilities.invokeLater(() -> {
          if (!sensorListModelMap.containsKey(nodeId)) {
            createSensorWindow(nodeId);
          }

          DefaultListModel<String> sensorListModel = sensorListModelMap.get(nodeId);
          sensorListModel.removeAllElements();
          for (int i = 0; i < sensors.length(); i++) {
            JSONObject sensor = sensors.getJSONObject(i);
            String sensorType = sensor.getString("type");
            double sensorValue = sensor.getDouble("value");
            long sensorId = sensor.getLong("id");
            String displayText = "Type: " + sensorType + ", Value: " + sensorValue + ", ID: " + sensorId;
            sensorListModel.addElement(displayText);
          }

          DefaultListModel<JToggleButton> actuatorListModel = actuatorListModelMap.get(nodeId);
          actuatorListModel.removeAllElements();
          for (int i = 0; i < actuators.length(); i++) {
            JSONObject actuator = actuators.getJSONObject(i);
            String actuatorType = actuator.getString("type");
            boolean actuatorValue = actuator.getBoolean("isOn");
            JToggleButton toggleButton = new JToggleButton(actuatorType);
            toggleButton.setSelected(actuatorValue);
            actuatorListModel.addElement(toggleButton);
          }
        });
      }
    } catch (IOException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, "Error connecting to server: " + e.getMessage(), "Connection Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private static class ActuatorListCellRenderer implements ListCellRenderer<JToggleButton> {
    @Override
    public Component getListCellRendererComponent(JList<? extends JToggleButton> list, JToggleButton value, int index, boolean isSelected, boolean cellHasFocus) {
      return value;
    }
  }

  private static class NoSelectionModel extends DefaultListSelectionModel {
    @Override
    public void setSelectionInterval(int index0, int index1) {
      super.setSelectionInterval(-1, -1);
    }
  }

  public static void main(String[] args) {
    ControlPanel controlPanel = new ControlPanel();
    controlPanel.connectToServer("localhost", 12345); // Replace with your server's IP address and port
  }
}
