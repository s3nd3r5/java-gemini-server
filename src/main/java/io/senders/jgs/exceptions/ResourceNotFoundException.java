package io.senders.jgs.exceptions;

import io.senders.jgs.status.GeminiStatus;

public class ResourceNotFoundException extends ServerBaseException {

  public ResourceNotFoundException(String message) {
    super(GeminiStatus.NOT_FOUND, message);
  }
}
