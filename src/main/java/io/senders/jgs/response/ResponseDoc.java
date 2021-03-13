package io.senders.jgs.response;

import io.senders.jgs.status.GeminiStatus;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Objects;

public class ResponseDoc extends ResponseMessage {
  private final byte[] data;

  public ResponseDoc(String mimeType, String charset, String lang, byte[] data) {
    super(GeminiStatus.SUCCESS, toMeta(mimeType, charset, lang));
    this.data = data;
  }

  /** Builds the meta string out of the mimeType charset and lang. */
  private static String toMeta(String mimeType, String charset, String lang) {
    StringBuilder meta =
        new StringBuilder(Objects.requireNonNull(mimeType, "mimeType")).append(";");
    if (charset != null) {
      meta.append(" charset=").append(charset).append(";");
    }
    if (lang != null) {
      meta.append(" lang=").append(lang).append(";");
    }

    return meta.toString();
  }

  @Override
  public byte[] toResponseMessage() {
    byte[] statusLine = super.toResponseMessage();
    try {
      var bao = new ByteArrayOutputStream(statusLine.length + data.length);
      bao.write(statusLine);
      bao.write(data);
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
    ResponseDoc that = (ResponseDoc) o;
    return Arrays.equals(data, that.data);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(data);
  }
}
