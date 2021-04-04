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

import org.junit.jupiter.api.Test;

class DocsConfigTest {

  @Test
  void testDocsConfig() {
    var expectedConfig = new DocsConfig("/root", null, null);
    var config = new DocsConfig("/root", null, null);

    assertEquals(expectedConfig, config);
  }

  @Test
  void testDocsConfigBuilderMinimal() {
    var expectedConfig = new DocsConfig("/root", null, null);
    var config = DocsConfig.newBuilder().withRoot("/root").build();

    assertEquals(expectedConfig, config);
  }

  @Test
  void testDocsConfigBuilderMinimalFull() {
    var expectedConfig = new DocsConfig("/root", "en", MimeOverrideConfig.newBuilder().build());
    var config =
        DocsConfig.newBuilder()
            .withRoot("/root")
            .withDefaultLang("en")
            .withMimeOverrides(MimeOverrideConfig.newBuilder().build())
            .build();

    assertEquals(expectedConfig, config);
  }

  @Test
  void testDocsConfigBuilderCopy() {
    var expectedConfig = new DocsConfig("/root", "en", MimeOverrideConfig.newBuilder().build());
    var config = DocsConfig.newBuilder(expectedConfig).build();

    assertEquals(expectedConfig, config);
  }

  @Test
  void testDocsConfigBuilderCopyChange() {
    var expectedConfig = new DocsConfig("/root", "en", null);
    var old = DocsConfig.newBuilder().withRoot("/root").build();
    var config = DocsConfig.newBuilder(old).withDefaultLang("en").build();

    assertEquals(expectedConfig, config);
  }

  @Test
  void testGetters() {
    MimeOverrideConfig mime = MimeOverrideConfig.newBuilder().build();
    String root = "/root";
    String lang = "en";
    var config =
        DocsConfig.newBuilder()
            .withRoot(root)
            .withDefaultLang(lang)
            .withMimeOverrides(mime)
            .build();

    assertEquals(root, config.root());
    assertEquals(lang, config.defaultLang());
    assertEquals(mime, config.mimeOverrides());
  }

  @Test
  void testRequiresRoot() {
    assertThrows(
        NullPointerException.class,
        () ->
            DocsConfig.newBuilder()
                .withDefaultLang("en")
                .withMimeOverrides(MimeOverrideConfig.newBuilder().build())
                .build());
  }

  @Test
  void testDefaultMimeOverrideConfig() {
    var expected = MimeOverrideConfig.newBuilder().build();
    var docConfig = DocsConfig.newBuilder().withRoot("/root").build();
    assertEquals(expected, docConfig.mimeOverrides());
  }

  @Test
  void testDefaultLangNull() {
    assertNull(DocsConfig.newBuilder().withRoot("/").build().defaultLang());
  }

  @Test
  void testFailsOnEmpty() {
    assertThrows(NullPointerException.class, () -> DocsConfig.newBuilder().build());
  }

  @Test
  void readableToString() {
    // basically lets make sure we can understand the output
    var toString =
        DocsConfig.newBuilder()
            .withRoot("/root")
            .withDefaultLang("en")
            .withMimeOverrides(MimeOverrideConfig.newBuilder().build())
            .build()
            .toString();

    assertTrue(toString.contains("DocsConfig"));
    assertTrue(toString.contains("/root"));
    assertTrue(toString.contains("en"));
    assertTrue(toString.contains("MimeOverrideConfig")); // the rest can be asserted by the class
  }

  @Test
  void equalsHashDivergenceTest() {
    // lets make sure equals and hashcode don't diverge
    var conf1 = DocsConfig.newBuilder().withRoot("/root1").build();
    var copy1 = DocsConfig.newBuilder(conf1).build();
    var conf2 = DocsConfig.newBuilder().withRoot("/root2").build();

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
