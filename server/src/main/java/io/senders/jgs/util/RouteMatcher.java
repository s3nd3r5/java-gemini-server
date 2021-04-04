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
package io.senders.jgs.util;

import java.net.URI;

public class RouteMatcher {

  private RouteMatcher() {}

  public static boolean match(final String pathPattern, final URI uri) {
    var regex = pathPattern.replaceAll(":[a-zA-Z0-9]+", "[^/]+").replaceAll("[*]", ".*");
    return uri.getPath().matches(regex);
  }

  public static boolean prefixMatch(final String pathPattern, final URI uri) {
    // if your prefix has the slash just append a match-all
    if (pathPattern.endsWith("/")) {
      return match(pathPattern + "*", uri);
    }
    // otherwise make it a path with a star
    return match(pathPattern + "/*", uri);
  }
}
