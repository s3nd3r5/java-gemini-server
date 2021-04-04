/*-
 * -\-\-
 * Java Gemini Library
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
package io.senders.jgs.mime;

import java.util.Collections;
import java.util.Map;

public class MimeTypes {

  private static final String DEFAULT_TYPE = "application/octet-stream";
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
