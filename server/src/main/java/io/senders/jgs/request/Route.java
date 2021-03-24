package io.senders.jgs.request;

import io.senders.jgs.response.ResponseMessage;
import java.util.function.Function;

public class Route {
  private final String route;
  private final Function<Request, ResponseMessage> handleFn;

  public Route(String route, Function<Request, ResponseMessage> handleFn) {
    this.route = route;
    this.handleFn = handleFn;
  }

  public String route() {
    return this.route;
  }

  public ResponseMessage handle(final Request request) {
    return this.handleFn.apply(request);
  }
}
