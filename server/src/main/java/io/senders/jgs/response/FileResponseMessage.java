/*-
 * -\-\-
 * Java Gemini Server
 * --
 * Copyright (C) 2021 - 2022 senders <steph (at) senders.io>
 * --
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * -/-/-
 */
package io.senders.jgs.response;

import io.senders.jgs.status.GeminiStatus;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * Helper class for returning a file response. Provides the constructor for setting the meta fields
 * that can be set when return some file.
 *
 * @implNote previous named DocResponse
 */
public class FileResponseMessage extends ResponseMessage {
  private final byte[] data;

  /**
   * Create a new response
   *
   * @param mimeType mimeType of the file
   * @param charset charset of the file
   * @param lang language of the file
   * @param data data of the file
   */
  public FileResponseMessage(
      final String mimeType, final String charset, final String lang, final byte[] data) {
    super(GeminiStatus.SUCCESS, toMeta(mimeType, charset, lang));
    this.data = data;
  }

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

  /**
   * Convert the response to bytes
   *
   * @return byte[] of the response
   */
  @Override
  public byte[] toBytes() {
    byte[] statusLine = super.toBytes();
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
    if (!super.equals(o)) {
      return false;
    }
    FileResponseMessage that = (FileResponseMessage) o;
    return Arrays.equals(data, that.data);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + Arrays.hashCode(data);
    return result;
  }
}
