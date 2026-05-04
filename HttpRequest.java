import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
  private String path;
  private String method;
  private Map<String, String> headers;
  private String body;

  public HttpRequest(BufferedReader reader) throws IOException {
    this.headers = new HashMap<String, String>();
    extractMethodAndPath(reader.readLine());
    extractHeaders(reader);
    extractBody(reader);
  }

  public Map<String, String> headers() {
    return this.headers;
  }

  public String body() {
    return body;
  }

  public String path() {
    return path;
  }

  public String method() {
    return method;
  }

  public String toString() {
    return "Path: " + path + "\n" +
        "Method: " + method + "\n" +
        "Headers: " + headers.values() + "\n" +
        "Body: " + body + "\n";

  }

  private void extractMethodAndPath(String httpRequestLine) throws IOException {
    String[] methodPath = httpRequestLine.split(" ");
    this.method = methodPath[0];
    this.path = methodPath[1];

  }

  private void extractBody(BufferedReader reader) throws IOException {
    int contentLength = Integer.parseInt(headers.get("content-length"));
    char[] cbuf = new char[contentLength];
    reader.read(cbuf, 0, contentLength);
    body = String.valueOf(cbuf);
  }

  private void extractHeaders(BufferedReader reader) throws IOException {
    String line = reader.readLine();
    while (line != null && !line.isEmpty()) {
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
  }
}
