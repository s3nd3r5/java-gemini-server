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
package io.senders.jgs.configs;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Test;

class MimeOverrideConfigTest {

  @Test
  void testMimeOverrideConfig() {
    var expectedConfig =
        new MimeOverrideConfig(Map.of("a.txt", "text/plain"), Map.of("txt", "text/plain"));
    var mimeOverrideConfig =
        new MimeOverrideConfig(Map.of("a.txt", "text/plain"), Map.of("txt", "text/plain"));

    assertEquals(expectedConfig, mimeOverrideConfig);
  }

  @Test
  void testMimeOverrideConfigBuilder() {
    var expectedConfig =
        new MimeOverrideConfig(Map.of("a.txt", "text/plain"), Map.of("txt", "text/plain"));
    var mimeOverrideConfig =
        MimeOverrideConfig.newBuilder()
            .withFiles(Map.of("a.txt", "text/plain"))
            .withExtensions(Map.of("txt", "text/plain"))
            .build();

    assertEquals(expectedConfig, mimeOverrideConfig);
  }

  @Test
  void testMimeOverrideConfigBuilderCopy() {
    var expectedConfig =
        new MimeOverrideConfig(Map.of("a.txt", "text/plain"), Map.of("txt", "text/plain"));
    var mimeOverrideConfig = MimeOverrideConfig.newBuilder(expectedConfig).build();

    assertEquals(expectedConfig, mimeOverrideConfig);
  }

  @Test
  void testMimeOverrideConfigBuilderCopyChange() {
    var expectedConfig =
        new MimeOverrideConfig(Map.of("a.txt", "text/plain"), Map.of("txt", "text/plain"));
    var oldConfig =
        MimeOverrideConfig.newBuilder()
            .withFiles(Map.of("a.txt", "text/plain"))
            .withExtensions(Map.of("gmi", "text/plain"))
            .build();
    var mimeOverrideConfig =
        MimeOverrideConfig.newBuilder(oldConfig)
            .withExtensions(Map.of("txt", "text/plain"))
            .build();

    assertEquals(expectedConfig, mimeOverrideConfig);
  }

  @Test
  void testWithFilesOverwrites() {
    var config =
        MimeOverrideConfig.newBuilder()
            .withFiles(Map.of("a.gmi", "text/gemini"))
            .withFiles(Map.of("b.txt", "text/plain"))
            .build();

    assertEquals(Map.of("b.txt", "text/plain"), config.files());
  }

  @Test
  void testWithExtensionsOverwrites() {
    var config =
        MimeOverrideConfig.newBuilder()
            .withExtensions(Map.of("gmi", "text/gemini"))
            .withExtensions(Map.of("txt", "text/plain"))
            .build();

    assertEquals(Map.of("txt", "text/plain"), config.extensions());
  }

  @Test
  void testAddFilesAddsAll() {
    var config =
        MimeOverrideConfig.newBuilder()
            .withFiles(Map.of("a.gmi", "text/gemini"))
            .addFiles(Map.of("b.txt", "text/plain"))
            .build();

    assertEquals(Map.of("a.gmi", "text/gemini", "b.txt", "text/plain"), config.files());
  }

  @Test
  void testAddExtensionsAddsAll() {
    var config =
        MimeOverrideConfig.newBuilder()
            .withExtensions(Map.of("gmi", "text/gemini"))
            .addExtensions(Map.of("txt", "text/plain"))
            .build();

    assertEquals(Map.of("gmi", "text/gemini", "txt", "text/plain"), config.extensions());
  }

  @Test
  void testAddFile() {
    var config =
        MimeOverrideConfig.newBuilder()
            .addFile("a.gmi", "text/gemini")
            .addFile("b.txt", "text/plain")
            .build();

    assertEquals(Map.of("a.gmi", "text/gemini", "b.txt", "text/plain"), config.files());
  }

  @Test
  void testAddExtension() {
    var config =
        MimeOverrideConfig.newBuilder()
            .addExtension("gmi", "text/gemini")
            .addExtension("txt", "text/plain")
            .build();

    assertEquals(Map.of("gmi", "text/gemini", "txt", "text/plain"), config.extensions());
  }

  @Test
  void testGetters() {
    var config =
        MimeOverrideConfig.newBuilder()
            .addExtension("txt", "text/plain")
            .addFile("a.txt", "text/plain")
            .build();

    assertEquals(Map.of("txt", "text/plain"), config.extensions());
    assertEquals(Map.of("a.txt", "text/plain"), config.files());
  }

  @Test
  void testEmpty() {
    var config = MimeOverrideConfig.newBuilder().build();

    assertEquals(Collections.emptyMap(), config.extensions());
    assertEquals(Collections.emptyMap(), config.files());
  }

  @Test
  void readableToString() {
    // basically lets make sure we can understand the output
    var config =
        MimeOverrideConfig.newBuilder()
            .addExtension("gmi", "text/gemini")
            .addFile("a.txt", "text/plain")
            .build();
    var toString = config.toString();

    assertTrue(toString.contains("MimeOverrideConfig"));
    assertTrue(toString.contains("gmi"));
    assertTrue(toString.contains("text/gemini"));
    assertTrue(toString.contains("a.txt"));
    assertTrue(toString.contains("text/plain"));
  }

  @Test
  void equalsHashDivergenceTest() {
    // lets make sure equals and hashcode don't diverge
    var conf1 =
        MimeOverrideConfig.newBuilder()
            .addFile("a.gmi", "text/gemini")
            .addExtension("b.txt", "text/plain")
            .build();
    var copy1 = MimeOverrideConfig.newBuilder(conf1).build();
    var conf2 =
        MimeOverrideConfig.newBuilder()
            .addFile("Makefile", "text/plain")
            .addExtension("c.txt", "text/plain")
            .build();

    assertEquals(conf1, conf1);
    assertEquals(conf1, copy1);
    assertEquals(conf1.hashCode(), copy1.hashCode());
    assertNotEquals(conf1, conf2);
    assertNotEquals(conf1.hashCode(), conf2.hashCode());
    assertNotEquals(conf1, null);
    assertNotEquals(new Object(), conf1);
    assertNotEquals(new Object().hashCode(), conf1.hashCode());
  }
}
