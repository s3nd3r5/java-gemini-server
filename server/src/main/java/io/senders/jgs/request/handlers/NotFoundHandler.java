package io.senders.jgs.request.handlers;

import io.senders.jgs.response.ResponseMessage;
import io.senders.jgs.status.GeminiStatus;
import java.net.URI;

public class NotFoundHandler implements RouteHandler {

  @Override
  public ResponseMessage handle(URI uri) {
    return new ResponseMessage(GeminiStatus.NOT_FOUND, uri.getPath() + " is not found.");
  }
}
