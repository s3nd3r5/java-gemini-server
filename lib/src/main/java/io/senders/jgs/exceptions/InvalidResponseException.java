package io.senders.jgs.exceptions;

import io.senders.jgs.status.GeminiStatus;

public class InvalidResponseException extends ServerBaseException{

  public InvalidResponseException(String message) {
    super(GeminiStatus.TEMPORARY_FAILURE, message);
  }
}
