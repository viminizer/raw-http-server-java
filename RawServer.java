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

    ServerSocket serverSocket = new ServerSocket(4455);

    System.out.println("server is listening... on port 4455\n");
    ThreadPool pool = new ThreadPool();
    while (true) {
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
