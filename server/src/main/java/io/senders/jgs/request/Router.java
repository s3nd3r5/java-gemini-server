package io.senders.jgs.request;

import java.util.stream.Stream;

public interface Router {

  Stream<Route> routes();
}
