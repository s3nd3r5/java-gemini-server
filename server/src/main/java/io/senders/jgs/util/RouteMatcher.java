package io.senders.jgs.util;

import java.net.URI;

public class RouteMatcher {

  private RouteMatcher() {}

  /**
   * Allows routes to be defined as /path/to/:file
   *
   * @param pathPattern the uri pattern to match
   * @param uri the requested uri
   * @return true if the path pattern matches the uri given
   */
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
