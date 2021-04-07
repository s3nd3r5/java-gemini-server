/*-
 * -\-\-
 * Java Gemini Server
 * --
 * Copyright (C) 2021 senders <stephen (at) senders.io>
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

import io.senders.jgs.exceptions.InvalidResponseException;
import io.senders.jgs.status.GeminiStatus;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ResponseMessage {

  private static final String MESSAGE_FORMAT = "%s %s\r\n";
  private static final int META_MAX_LEN = 1024;
  private final String status;
  private final String meta;

  /**
   * Create a new response. Consult the gemini specification for what the meta needs to be for the
   * given status. If additional data needs to be sent along with the response extend this class and
   * override the {@link #toBytes()} method.
   *
   * @param status status of the response
   * @param meta meta of the response
   */
  public ResponseMessage(final GeminiStatus status, final String meta) {
    this.status = Objects.requireNonNull(status, "status").code();
    if (meta != null && meta.length() > META_MAX_LEN) {
      throw new InvalidResponseException("Meta too long " + meta.length() + ": " + meta);
    }
    this.meta = meta;
  }

  /**
   * Get status of the response
   *
   * @return status number as string
   */
  public String status() {
    return this.status;
  }

  /**
   * Get meta of the response
   *
   * @return meta string
   */
  public String meta() {
    return this.meta;
  }

  /**
   * Convert the response to bytes. If overriding this class and extra data is being contained
   * please make sure to override this method if you need to send additional data in the response.
   *
   * @return byte[] of the response
   * @see FileResponseMessage
   */
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
