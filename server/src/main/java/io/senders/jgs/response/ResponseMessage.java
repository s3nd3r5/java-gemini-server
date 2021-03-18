package io.senders.jgs.response;

import io.senders.jgs.status.GeminiStatus;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ResponseMessage {
  private static final byte SPACE = 0x20;
  private static final byte[] CR_LF = {0x0d, 0x0a};
  // status (2) + space (1) + meta (1024) + <cr><lf> (2)
  private static final int MESSAGE_BUFFER_SIZE = 1029;

  private final String status;
  private final String meta;

  public ResponseMessage(final GeminiStatus status, final String meta) {
    this.status = status.code();
    this.meta = meta;
  }

  public String status() {
    return this.status;
  }

  public String meta() {
    return this.meta;
  }

  public byte[] toResponseMessage() {
    try {
      var bao = new ByteArrayOutputStream(MESSAGE_BUFFER_SIZE);
      bao.write(status.getBytes(StandardCharsets.UTF_8));
      bao.write(SPACE);
      bao.write(meta.getBytes(StandardCharsets.UTF_8));
      bao.write(CR_LF);
      return bao.toByteArray();
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
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
