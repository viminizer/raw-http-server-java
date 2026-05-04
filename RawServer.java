import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class RawServer {

  private static String body = "{name:Kevin}\r\n";
  private static String response = "HTTP/1.1 200 OK\r\n"
      + "Content-Length: " + body.getBytes().length + "\r\n"
      + "Content-Type: " + "text/plain" + "\r\n"
      + "\r\n"
      + body;

  public static void handleClientSocket(Socket socket) throws IOException {
    try {
      InputStream in = socket.getInputStream();
      OutputStream out = socket.getOutputStream();

      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

      System.out.println("printed: " + reader.readLine());
      writer.write(response);
      writer.flush();
      System.out.println("flushed");
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    } finally {
      socket.close();
    }

  }

  public static void main(String[] args) throws IOException {

    ServerSocket serverSocket = new ServerSocket(4455);

    System.out.println("server is listening... on port 4455\n");
    while (true) {
      Socket socket = serverSocket.accept();
      Thread thread = new Thread(() -> {
        try {
          handleClientSocket(socket);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
      thread.start();
    }
  }
}
