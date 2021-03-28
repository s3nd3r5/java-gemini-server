package io.senders.jgs.request.handlers;

import io.senders.jgs.request.Request;
import io.senders.jgs.response.ResponseMessage;

public interface RequestHandler {

  ResponseMessage handle(final Request request);
}
