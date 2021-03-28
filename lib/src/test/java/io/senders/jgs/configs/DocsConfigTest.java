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
