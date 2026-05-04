import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpResponse {
  private String responseLine;
  private Map<String, String> headers;
  private String body = null;

  public HttpResponse(int status, String body) {
    this.responseLine = "HTTP/1.1 " + status + " OK\r\n";
    this.headers = new HashMap<>();
    this.headers.put("Content-Type", "text/plain");
    this.headers.put("Content-Length", String.valueOf(body.getBytes().length));
    this.body = body;
  }

  public String toString() {
    return responseLine + stringifyHeaders() + body;
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
