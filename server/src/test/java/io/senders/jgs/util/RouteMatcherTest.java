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

import static io.senders.jgs.util.RouteMatcher.match;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import org.junit.jupiter.api.Test;

class RouteMatcherTest {

  @Test
  void testMatchIdentifier() {
    String pathPattern = "/:file";
    assertTrue(match(pathPattern, URI.create("/index.gmi")), "a file");
    assertTrue(match(pathPattern, URI.create("/profile")), "a path part");
    assertFalse(match(pathPattern, URI.create("/profile/")), "not with trailing slash");
    assertFalse(match(pathPattern, URI.create("/profile/index.gmi")), "not a nested path");
  }

  @Test
  void testMatchIdentifierMidPath() {
    assertTrue(match("/:user/profile", URI.create("/senders/profile")));
    assertTrue(match("/:user/index.gmi", URI.create("/senders/index.gmi")));
  }

  @Test
  void testMultipleIdentifiers() {
    String pathPattern = "/:user/gemlog/:file";
    assertTrue(match(pathPattern, URI.create("/senders/gemlog/index.gmi")));
    assertTrue(match(pathPattern, URI.create("/senders/gemlog/profile")));
    assertFalse(match(pathPattern, URI.create("/senders/gemlog/profile/")));
  }

  @Test
  void testStarCatchAll() {
    String pathPattern = "/*";
    assertTrue(match(pathPattern, URI.create("/")), "Root");
    assertTrue(match(pathPattern, URI.create("/index.gmi")), "Single level file");
    assertTrue(match(pathPattern, URI.create("/path/")), "Nested Root");
    assertTrue(match(pathPattern, URI.create("/path/page")), "Nested Root");
    assertTrue(match(pathPattern, URI.create("/path/file.txt")), "Nested page");
    assertTrue(match(pathPattern, URI.create("/path/file.txt")), "Nested file");
    assertTrue(match(pathPattern, URI.create("/path/to/file.txt")), "Multiple Nested file");
  }

  @Test
  void testStarWithIdentifier() {
    String pathPattern = "/:user/*";
    assertTrue(match(pathPattern, URI.create("/senders/")), "Root");
    assertTrue(match(pathPattern, URI.create("/senders/index.gmi")), "Single level file");
    assertTrue(match("/*", URI.create("/path/")), "Nested Root");
    assertTrue(match("/*", URI.create("/path/page")), "Nested Root");
    assertTrue(match("/*", URI.create("/path/file.txt")), "Nested page");
    assertTrue(match("/*", URI.create("/path/file.txt")), "Nested file");
    assertTrue(match("/*", URI.create("/path/to/file.txt")), "Multiple Nested file");
  }
}
