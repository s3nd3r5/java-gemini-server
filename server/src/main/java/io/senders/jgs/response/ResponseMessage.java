package io.senders.jgs.response;

import io.senders.jgs.status.GeminiStatus;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ResponseMessage {

  private static final String MESSAGE_FORMAT = "%s %s\r\n";
  private static final int META_MAX_LEN = 1024;
  private final String status;
  private final String meta;

  public ResponseMessage(final GeminiStatus status, final String meta) {
    this.status = Objects.requireNonNull(status, "status").code();
    if (meta != null && meta.length() > META_MAX_LEN) {
      // TODO pick a better exception
      throw new RuntimeException("Meta too long " + meta.length() + ": " + meta);
    }
    this.meta = meta;
  }

  public String status() {
    return this.status;
  }

  public String meta() {
    return this.meta;
  }

  public byte[] toBytes() {
    return String.format(MESSAGE_FORMAT, status, meta).getBytes(StandardCharsets.UTF_8);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ResponseMessage that = (ResponseMessage) o;
    return Objects.equals(status, that.status) && Objects.equals(meta, that.meta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, meta);
  }
}
