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
package io.senders.jgs.request;

import java.net.URI;

/**
 * Request container. Passed to your routes containing all the necessary, relevant request
 * information.
 */
public class Request {
  private final URI uri;

  /**
   * Creates a new request for the given URI
   *
   * @param uri uri of the request
   */
  public Request(URI uri) {
    this.uri = uri;
  }

  /**
   * Get the uri of the request
   *
   * @return uri of the request
   */
  public URI uri() {
    return this.uri;
  }
}
