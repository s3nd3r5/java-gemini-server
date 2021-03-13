package io.senders.jgs.exceptions;

import io.senders.jgs.status.GeminiStatus;

public class InvalidResourceException extends ServerBaseException {

  public InvalidResourceException(String message) {
    super(GeminiStatus.PERMANENT_FAILURE, message);
  }
}
