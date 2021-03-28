package io.senders.jgs.configs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class CertConfigTest {

  @Test
  void testCertConfig() {
    var expectedConfig = new CertConfig("./file.pem", "./key.pem");
    var certConfig = new CertConfig("./file.pem", "./key.pem");

    assertEquals(expectedConfig, certConfig);
  }

  @Test
  void testCertConfigBuilder() {
    var expectedConfig = new CertConfig("./file.pem", "./key.pem");
    var certConfig = CertConfig.newBuilder().withKey("./key.pem").withFile("./file.pem").build();

    assertEquals(expectedConfig, certConfig);
  }

  @Test
  void testCertConfigBuilderCopy() {
    var expectedConfig = new CertConfig("./file2.pem", "./key.pem");
    var certConfig = CertConfig.newBuilder(expectedConfig).build();

    assertEquals(expectedConfig, certConfig);
  }

  @Test
  void testCertConfigBuilderCopyChange() {
    var expectedConfig = new CertConfig("./file2.pem", "./key.pem");
    var oldConfig = CertConfig.newBuilder().withKey("./key.pem").withFile("./file.pem").build();
    var certConfig = CertConfig.newBuilder(oldConfig).withFile("./file2.pem").build();

    assertEquals(expectedConfig, certConfig);
  }

  @Test
  void testGetters() {
    var key = "key.pem";
    var file = "file.pem";
    var config = CertConfig.newBuilder().withKey(key).withFile(file).build();

    assertEquals(key, config.key());
    assertEquals(file, config.file());
  }

  @Test
  void testRequiresKey() {
    assertThrows(
        NullPointerException.class, () -> CertConfig.newBuilder().withFile("file.pem").build());
  }

  @Test
  void testRequiresFile() {
    assertThrows(
        NullPointerException.class, () -> CertConfig.newBuilder().withKey("key.pem").build());
  }

  @Test
  void testFailsOnEmpty() {
    assertThrows(NullPointerException.class, () -> CertConfig.newBuilder().build());
  }

  @Test
  void readableToString() {
    // basically lets make sure we can understand the output
    var config = CertConfig.newBuilder().withFile("file.pem").withKey("key.pem").build();
    var toString = config.toString();

    assertTrue(toString.contains("CertConfig"));
    assertTrue(toString.contains("file.pem"));
    assertTrue(toString.contains("key.pem"));
  }

  @Test
  void equalsHashDivergenceTest() {
    // lets make sure equals and hashcode don't diverge
    var conf1 = CertConfig.newBuilder().withFile("file1.pem").withKey("key1.pem").build();
    var copy1 = CertConfig.newBuilder(conf1).build();
    var conf2 = CertConfig.newBuilder().withFile("file2.pem").withKey("key2.pem").build();

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
