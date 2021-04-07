package io.senders.jgs.response;

import io.senders.jgs.status.GeminiStatus;

/**
 * Helper class extending the {@link ResponseMessage} for Sensitive Input responses.
 *
 * @see GeminiStatus#SENSITIVE_INPUT
 */
public class SensitiveInputResponseMessage extends InputResponseMessage {

  /**
   * Create a response with the given meta with the status code {@link GeminiStatus#SENSITIVE_INPUT}
   *
   * @param meta meta of the response
   */
  public SensitiveInputResponseMessage(String meta) {
    super(meta, true);
  }
}
