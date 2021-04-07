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

/** Helper class for matching configured route path patterns to the actual requested URI */
public class RouteMatcher {

  private RouteMatcher() {}

  /**
   * Match a URI to an exact route path pattern
   *
   * @param pathPattern route path pattern
   * @param uri request URI
   * @return true if the request URI matches
   */
  public static boolean match(final String pathPattern, final URI uri) {
    var regex = pathPattern.replaceAll(":[a-zA-Z0-9]+", "[^/]+").replaceAll("[*]", ".*");
    return uri.getPath().matches(regex);
  }

  /**
   * Match a URI to a route path pattern if the path prefixes the URI.
   *
   * <p>/path/:user will match to the URI /path/senders/file.gmi
   *
   * @param prefixPathPattern prefix path pattern
   * @param uri requested URI
   * @return true if the prefixPathPattern is a exact or a prefix of the requested URI
   */
  public static boolean prefixMatch(final String prefixPathPattern, final URI uri) {
    // if your prefix has the slash just append a match-all
    if (prefixPathPattern.endsWith("/")) {
      return match(prefixPathPattern + "*", uri);
    }
    // otherwise make it a path with a star
    return match(prefixPathPattern + "/*", uri);
  }
}
