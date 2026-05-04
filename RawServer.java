import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class RawServer {

  private static String body = "{name:Kevin}\r\n";
  private static String response = "HTTP/1.1 200 OK\r\n"
      + "Content-Length: " + body.getBytes().length + "\r\n"
      + "Content-Type: " + "text/plain" + "\r\n"
      + "\r\n"
      + body;

  public static void log(String str) {
    System.out.println(str);
  }

  public static void handleClientSocket(Socket socket) throws IOException {
    try {
      InputStream in = socket.getInputStream();
      OutputStream out = socket.getOutputStream();

      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

      String reqLine = reader.readLine();
      String[] methodPath = reqLine.split(" ");
      String method = methodPath[0];
      String path = methodPath[1];
      log("---method path: " + method + " " + path);

      /*
       * > POST / HTTP/1.1
       * > Host: localhost:4455
       * > User-Agent: curl/8.7.1
       * > Accept: all
       * > Content-Type: application/json
       * > Authorization: Bearer mytoken123
       * > Content-Length: 49
       * 
       */

      // parse headers
      Map<String, String> headers = new HashMap<String, String>();
      String line = reader.readLine();
      while (line != null && !line.isEmpty()) {
        log("line: " + line + "\n");
        int idx = line.indexOf(":");
        String key = "";
        String value = "";
        if (idx == -1) {
          key = line.toLowerCase().trim();
          headers.put(key, value);
        } else {
          key = line.substring(0, idx).toLowerCase().trim();
          value = line.substring(idx + 1).trim();
          headers.put(key, value);
        }
        line = reader.readLine();
      }

      log("---header: " + headers.toString() + "\n");

      // parse json body
      int contentLength = Integer.parseInt(headers.get("content-length"));
      char[] cbuf = new char[contentLength];
      reader.read(cbuf, 0, contentLength);
      log(String.valueOf(cbuf));

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
