package io.senders.jgs.configs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class HostConfigTest {

  public static final CertConfig DEFAULT_CERTS =
      CertConfig.newBuilder().withKey("key").withFile("file").build();
  public static final DocsConfig DEFAULT_DOCS = DocsConfig.newBuilder().withRoot("/root").build();

  @Test
  void testHostConfig() {
    var expectedConfig = new HostConfig("localhost", DEFAULT_DOCS, DEFAULT_CERTS);
    var config = new HostConfig("localhost", DEFAULT_DOCS, DEFAULT_CERTS);

    assertEquals(expectedConfig, config);
  }

  @Test
  void testHostConfigMinimal() {
    var expectedConfig = new HostConfig("localhost", DEFAULT_DOCS, DEFAULT_CERTS);
    var config =
        HostConfig.newBuilder("localhost").withDocs(DEFAULT_DOCS).withCert(DEFAULT_CERTS).build();

    assertEquals(expectedConfig, config);
  }

  @Test
  void testHostConfigBuilderCopy() {
    var expectedConfig = new HostConfig("localhost", DEFAULT_DOCS, DEFAULT_CERTS);
    var config = HostConfig.newBuilder(expectedConfig).build();

    assertEquals(expectedConfig, config);
  }

  @Test
  void testHostConfigBuilderCopyChange() {
    var expectedConfig = new HostConfig("localhost", DEFAULT_DOCS, DEFAULT_CERTS);
    var old = new HostConfig("otherhost", DEFAULT_DOCS, DEFAULT_CERTS);
    var config = HostConfig.newBuilder(old).withHostname("localhost").build();

    assertEquals(expectedConfig, config);
  }

  @Test
  void testGetters() {
    String hostname = "localhost";
    var config = new HostConfig(hostname, DEFAULT_DOCS, DEFAULT_CERTS);

    assertEquals(hostname, config.hostname());
    assertEquals(DEFAULT_DOCS, config.docs());
    assertEquals(DEFAULT_CERTS, config.cert());
  }

  @Test
  void testRequiresHostname() {
    assertThrows(
        NullPointerException.class,
        () ->
            HostConfig.newBuilder((String) null)
                .withDocs(DEFAULT_DOCS)
                .withCert(DEFAULT_CERTS)
                .build());
  }

  @Test
  void testRequiresDocs() {
    assertThrows(
        NullPointerException.class,
        () -> HostConfig.newBuilder("hostname").withCert(DEFAULT_CERTS).build());
  }

  @Test
  void testRequiresCerts() {
    assertThrows(
        NullPointerException.class,
        () -> HostConfig.newBuilder("hostname").withDocs(DEFAULT_DOCS).build());
  }

  @Test
  void testFailsOnEmpty() {
    assertThrows(NullPointerException.class, () -> HostConfig.newBuilder((String) null).build());
  }

  @Test
  void readableToString() {
    // basically lets make sure we can understand the output
    var toString =
        HostConfig.newBuilder("localhost")
            .withDocs(DEFAULT_DOCS)
            .withCert(DEFAULT_CERTS)
            .build()
            .toString();

    assertTrue(toString.contains("HostConfig"));
    assertTrue(toString.contains("localhost"));
    assertTrue(toString.contains("DocsConfig")); // the rest can be asserted by the class
    assertTrue(toString.contains("CertConfig")); // the rest can be asserted by the class
  }

  @Test
  void equalsHashDivergenceTest() {
    // lets make sure equals and hashcode don't diverge
    var conf1 =
        HostConfig.newBuilder("localhost").withDocs(DEFAULT_DOCS).withCert(DEFAULT_CERTS).build();
    var copy1 = HostConfig.newBuilder(conf1).build();
    var conf2 =
        HostConfig.newBuilder("example.com").withDocs(DEFAULT_DOCS).withCert(DEFAULT_CERTS).build();

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
