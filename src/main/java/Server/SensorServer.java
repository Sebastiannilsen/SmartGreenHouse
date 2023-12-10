package Server;

import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensorServer {

  private int port;
  private List<Socket> controlPanelSockets = new ArrayList<>();
  private Map<Long, Socket> sensorNodeSockets = new HashMap<>();

  public SensorServer(int port) {
    this.port = port;
  }

  public void startServer() {
    try (ServerSocket serverSocket = new ServerSocket(port)) {
      System.out.println("Server started. Listening on port " + port);

      while (true) {
        try {
          Socket clientSocket = serverSocket.accept();
          new ClientHandler(clientSocket).start();
        } catch (IOException e) {
          System.err.println("Error handling client connection: " + e.getMessage());
        }
      }
    } catch (IOException e) {
      System.err.println("Could not listen on port " + port + ": " + e.getMessage());
    }
  }

  private class ClientHandler extends Thread {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
      this.clientSocket = socket;
    }

    public void run() {
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
        String line;
        String clientType = reader.readLine();
        if (clientType.equals("SENSOR_NODE")) {
          System.out.println("Sensor node connected");
          long sensorNodeId = Long.parseLong(reader.readLine());
          synchronized (sensorNodeSockets) {
            sensorNodeSockets.put(sensorNodeId, clientSocket);
          }
          while ((line = reader.readLine()) != null) {
            handleSensorData(line);
          }
        } else if (clientType.equals("CONTROL_PANEL")) {
          System.out.println("Control panel connected");
          synchronized (controlPanelSockets) {
            controlPanelSockets.add(clientSocket);
          }
          while ((line = reader.readLine()) != null) {
            // handle any incoming data from control panels, if necessary
            handleControlPanelActuatorChange(line);
          }
        } else {
          System.err.println("Unknown client type");
        }
      } catch (IOException e) {
        System.err.println("Error reading data from client: " + e.getMessage());
      } finally {
        try {
          synchronized (controlPanelSockets) {
            controlPanelSockets.remove(clientSocket);
          }
          clientSocket.close();
          System.out.println("Closed client socket");
        } catch (IOException e) {
          System.err.println("Error closing client socket: " + e.getMessage());
        }
      }
    }

    private void handleSensorData(String jsonData) {
      try {
        JSONObject data = new JSONObject(jsonData);
        forwardToControlPanel(data);
      } catch (Exception e) {
        System.err.println("Error processing sensor data: " + e.getMessage());
      }
    }

    private void forwardToControlPanel(JSONObject data) {
      synchronized (controlPanelSockets) {
        for (Socket socket : controlPanelSockets) {
          try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(data.toString());
          } catch (IOException e) {
            System.err.println("Error forwarding data to control panel: " + e.getMessage());
          }
        }
      }
    }
  }

  private void handleControlPanelActuatorChange(String line) {
    try{
      JSONObject data = new JSONObject(line);
      long sensorNodeId = data.getLong("id");
      Socket sensorNodeSocket = sensorNodeSockets.get(sensorNodeId);
      PrintWriter out = new PrintWriter(sensorNodeSocket.getOutputStream(), true);
      out.println(data.toString());
      System.out.println("Sent actuator change to sensor node");
    } catch (Exception e) {
      System.err.println("Error processing sensor data: " + e.getMessage());
    }
  }

  public static void main(String[] args) {
    SensorServer server = new SensorServer(12345); // Use the same port as in your client nodes
    server.startServer();
  }
}
