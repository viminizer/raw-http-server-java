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

  public static void handleClientSocket(Socket socket) throws IOException {
    try {
      InputStream in = socket.getInputStream();
      OutputStream out = socket.getOutputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
      HttpRequest request = new HttpRequest(reader);
      Handler handler = new Handler(request);
      writer.write(handler.getResponse());
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    } finally {
      socket.close();
    }

  }

  public static void main(String[] args) throws IOException {
    final int PORT = 4455;

    ServerSocket serverSocket = new ServerSocket(PORT);

    System.out.println("Server is listening on port " + PORT + "\n");
    ThreadPool pool = new ThreadPool(10);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      try {
        serverSocket.close();
      } catch (IOException e) {
      }
      pool.shutdown();
    }));

    while (!serverSocket.isClosed()) {
      Socket socket = serverSocket.accept();
      pool.execute(() -> {
        try {
          handleClientSocket(socket);
        } catch (IOException e) {
          e.printStackTrace();
        }
      });

    }
  }
}
