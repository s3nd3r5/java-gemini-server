package io.senders.jgs.response;

import io.senders.jgs.status.GeminiStatus;

public class InputResponseMessage extends ResponseMessage {

  public InputResponseMessage(String meta) {
    this(meta, false);
  }

  public InputResponseMessage(String meta, boolean secure) {
    super(secure ? GeminiStatus.SENSITIVE_INPUT : GeminiStatus.INPUT, meta);
  }
}
