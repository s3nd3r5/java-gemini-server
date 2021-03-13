package io.senders.jgs.response;

import io.senders.jgs.status.GeminiStatus;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ResponseMessage {
  private static final byte SPACE = 0x20;
  private static final byte[] CR_LF = {0x0d, 0x0a};
  // status (2) + space (1) + meta (1024) + <cr><lf> (2)
  private static final int MESSAGE_BUFFER_SIZE = 1029;

  private final byte[] status;
  private final byte[] meta;

  public ResponseMessage(final GeminiStatus status, final String meta) {
    this.status = status.getCode();
    this.meta = meta.getBytes(StandardCharsets.UTF_8);
  }

  public byte[] toResponseMessage() {
    try {
      var bao = new ByteArrayOutputStream(MESSAGE_BUFFER_SIZE);
      bao.write(status);
      bao.write(SPACE);
      bao.write(meta);
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
    return Arrays.equals(status, that.status) && Arrays.equals(meta, that.meta);
  }

  @Override
  public int hashCode() {
    int result = Arrays.hashCode(status);
    result = 31 * result + Arrays.hashCode(meta);
    return result;
  }
}
