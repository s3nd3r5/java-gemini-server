package io.senders.jgs.standalone;

import io.senders.jgs.request.Request;
import io.senders.jgs.request.Route;
import io.senders.jgs.request.Router;
import io.senders.jgs.response.ResponseMessage;
import io.senders.jgs.status.GeminiStatus;
import java.util.stream.Stream;

public class RouterTest {

  public class PingRouter implements Router {

    @Override
    public Stream<Route> routes() {
      return Stream.of(
         new Route("/ping", this::ping)
      );
    }

    ResponseMessage ping(final Request request) {
      return new ResponseMessage(GeminiStatus.SUCCESS, "text/plain");
    }
  }

  public static void main(String... args) {

  }

}
