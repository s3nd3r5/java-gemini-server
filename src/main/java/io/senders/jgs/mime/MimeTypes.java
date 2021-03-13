package io.senders.jgs.mime;

import java.util.Collections;
import java.util.Map;

public class MimeTypes {

  private static final String DEFAULT_TYPE = "application/octect-stream";
  private static final Map<String, String> DEFAULT_TYPES =
      Map.of(
          "gmi", "text/gemini",
          "gemini", "text/gemini",
          "txt", "text/plain",
          "", DEFAULT_TYPE);

  private final Map<String, String> overrides;

  public MimeTypes(Map<String, String> overrides) {
    if (overrides == null || overrides.isEmpty()) {
      this.overrides = Collections.emptyMap();
    } else {
      this.overrides = Map.copyOf(overrides);
    }
  }

  public String getMimeType(final String extension, String foundMimeType) {
    if (foundMimeType == null || foundMimeType.isBlank()) {
      return overrides.getOrDefault(extension, DEFAULT_TYPES.getOrDefault(extension, DEFAULT_TYPE));
    } else {
      return overrides.getOrDefault(extension, foundMimeType);
    }
  }
}
