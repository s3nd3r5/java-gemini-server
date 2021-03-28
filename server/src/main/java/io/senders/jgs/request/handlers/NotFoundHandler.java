package io.senders.jgs.request.handlers;

import io.senders.jgs.request.Request;
import io.senders.jgs.response.ResponseMessage;
import io.senders.jgs.status.GeminiStatus;

public class NotFoundHandler implements RequestHandler {

  @Override
  public ResponseMessage handle(final Request request) {
    return new ResponseMessage(GeminiStatus.NOT_FOUND, request.uri().getPath() + " is not found.");
  }
}
