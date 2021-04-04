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
package io.senders.jgs.request.handlers;

import io.senders.jgs.request.Request;
import io.senders.jgs.response.ResponseMessage;
import io.senders.jgs.status.GeminiStatus;

/**
 * Server a 51 Not Found for any request passed to this handler. This can be used as a catch-all in
 * your server configuration.
 *
 * @see GeminiStatus#NOT_FOUND
 */
public class NotFoundHandler implements RequestHandler {

  /**
   * @param request request of the not found resource
   * @return a not found construction of a {@link ResponseMessage}
   */
  @Override
  public ResponseMessage handle(final Request request) {
    return new ResponseMessage(GeminiStatus.NOT_FOUND, request.uri().getPath() + " is not found.");
  }
}
