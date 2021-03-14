package io.senders.jgs.request;

import io.senders.jgs.response.ResponseMessage;
import java.net.URI;

public interface RouteHandler {

  ResponseMessage handle(final URI uri);
}
