
public class Handler {

  private HttpRequest request;
  private HttpResponse response;

  public Handler(HttpRequest req) {
    this.request = req;
    this.response = new HttpResponse();
    switchMethod();
  }

  public void get() {
    String body = "great body";
    response.setStatus(200);
    response.setHeader("Custom-Header", "great-value");
    response.setBody(body);
  }

  public void put() {
  }

  public void post() {
    String body = "post response body";
    response.setStatus(201);
    response.setHeader("Custom-Header", "post-response");
    response.setBody(body);
  }

  public void delete() {
  }

  public void optional() {
  }

  public String getResponse() {
    return response.toString();
  }

  private void switchMethod() {
    switch (request.method()) {
      case GET:
        get();
        break;
      case POST:
        post();
        break;
      case PUT:
        put();
        break;
      case DELETE:
        delete();
        break;
      case OPTIONAL:
        optional();
        break;
    }
  }

}
