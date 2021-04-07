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
