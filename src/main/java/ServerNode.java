import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ServerNode {
  private ServerSocket serverSocket;
  private ExecutorService pool; // For handling multiple client threads

  public ServerNode(int port) throws IOException {
    pool = Executors.newFixedThreadPool(10); // Example: up to 10 clients
    serverSocket = new ServerSocket(port);
  }

  public void startServer() {
    while (true) {
      try {
        Socket clientSocket = serverSocket.accept();
        ClientHandler clientHandler = new ClientHandler(clientSocket);
        pool.execute(clientHandler);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args){
    int port = 12345; // Choose your port
    ServerNode server = null;
    try {
      server = new ServerNode(port);
    } catch (IOException e) {
      e.printStackTrace();
    }
    server.startServer();
  }
}

class ClientHandler implements Runnable {
  private Socket clientSocket;

  public ClientHandler(Socket socket) {
    this.clientSocket = socket;
  }

  @Override
  public void run() {
    // Handle client communication here
  }
}
