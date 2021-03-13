package io.senders.jgs.exceptions;

import io.senders.jgs.status.GeminiStatus;
import java.nio.charset.StandardCharsets;

public class ResourceNotFoundException extends ServerBaseException {

  public ResourceNotFoundException(String message) {
    super(GeminiStatus.NOT_FOUND, message);
  }
}
