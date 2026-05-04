import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {
  private Map<String, String> headers;
  private String body = null;
  private int status;

  public HttpResponse() {
    this.headers = new HashMap<>();
  }

  public HttpResponse(int status, String body) {
    this.status = status;
    this.headers = new HashMap<>();
    this.headers.put("Content-Type", "text/plain");
    this.headers.put("Content-Length", String.valueOf(body.getBytes().length));
    this.body = body;
  }

  private String buildResponseLine() {
    return "HTTP/1.1 " + status + " OK\r\n";

  }

  public void setStatus(int status) {
    this.status = status;
  }

  public void setBody(String body) {
    this.body = body;
    this.headers.put("Content-Type", "text/plain");
    this.headers.put("Content-Length", String.valueOf(body.getBytes().length));
  }

  public String toString() {
    return buildResponseLine() + stringifyHeaders() + body;
  }

  private String stringifyHeaders() {
    List<String> headerString = new ArrayList<>();
    headers.forEach((key, value) -> {
      if (value.isEmpty()) {
        headerString.add(key + "\r\n");
      } else {
        headerString.add(key + ": " + value + "\r\n");
      }
    });
    headerString.add("\r\n");
    return String.join("", headerString);
  }

  public HttpResponse setHeader(String key, String value) {
    headers.put(key, value);
    return this;
  }

  public HttpResponse setHeader(String key) {
    headers.put(key, "");
    return this;
  }

}
