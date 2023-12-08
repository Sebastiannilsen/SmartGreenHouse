package Server;

import ClientNode.SensorNode; // Import your SensorNode class or the package it resides in
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class SensorServer {

  private int port;

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

  private static class ClientHandler extends Thread {
    private Socket clientSocket;

    public ClientHandler(Socket socket) {
      this.clientSocket = socket;
    }

    public void run() {
      System.out.println("Accepted connection from client! - " + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
        String line;
        while ((line = reader.readLine()) != null) {
          handleSensorData(line);
        }
      } catch (IOException e) {
        System.err.println("Error reading data from client: " + e.getMessage());
      } finally {
        try {
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
      System.out.println("Received data: " + data.toString());
    }
  }

  public static void main(String[] args) {
    SensorServer server = new SensorServer(12345); // Use the same port as in your client nodes
    server.startServer();
  }
}
