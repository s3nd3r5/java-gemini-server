package io.senders.jgs.request.handlers;

import io.senders.jgs.response.ResponseMessage;
import java.net.URI;

public interface RouteHandler {

  ResponseMessage handle(final URI uri);
}
