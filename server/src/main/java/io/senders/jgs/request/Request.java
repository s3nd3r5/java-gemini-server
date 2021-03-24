package io.senders.jgs.request;

import java.net.URI;

public class Request {
  private final URI uri;

  public Request(URI uri) {
    this.uri = uri;
  }

  public URI uri() {
    return this.uri;
  }
}
