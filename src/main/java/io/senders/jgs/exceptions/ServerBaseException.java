package io.senders.jgs.exceptions;

import io.senders.jgs.status.GeminiStatus;

public class ServerBaseException extends RuntimeException {
  private final GeminiStatus status;

  public ServerBaseException(GeminiStatus status, final String message) {
    super(message);
    this.status = status;
  }

  public GeminiStatus getStatus() {
    return this.status;
  }
}
