package io.senders.jgs.configs.typesafe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.typesafe.config.ConfigException;
import io.senders.jgs.configs.CertConfig;
import io.senders.jgs.configs.DocsConfig;
import io.senders.jgs.configs.HostConfig;
import io.senders.jgs.configs.MimeOverrideConfig;
import io.senders.jgs.configs.ServerConfig;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypesafeServerConfigFactoryTest {

  @BeforeEach
  void setUp() {}

  @AfterEach
  void tearDown() {}

  @Test
  void testCreateMinimumConfig() {
    ServerConfig expectedDefault =
        ServerConfig.newBuilder()
            .withPort(1965)
            .withSni(false)
            .withHostname("localhost")
            .withHosts(
                Collections.singletonMap(
                    "localhost",
                    HostConfig.newBuilder()
                        .withDocs(DocsConfig.newBuilder().withRoot("/var/gemini").build())
                        .withCert(
                            CertConfig.newBuilder().withKey("key.pem").withFile("cert.pem").build())
                        .build()))
            .build();
    ServerConfig config = TypesafeServerConfigFactory.create();

    assertEquals(expectedDefault, config);
  }

  @Test
  void testCreateSniOptional() {
    ServerConfig expectedDefault =
        ServerConfig.newBuilder()
            .withPort(1965)
            .withSni(false)
            .withHostname("localhost")
            .withHosts(
                Collections.singletonMap(
                    "localhost",
                    HostConfig.newBuilder()
                        .withDocs(DocsConfig.newBuilder().withRoot("/var/gemini").build())
                        .withCert(
                            CertConfig.newBuilder().withKey("key.pem").withFile("cert.pem").build())
                        .build()))
            .build();
    ServerConfig config =
        TypesafeServerConfigFactory.create(
            MethodHandles.lookup().lookupClass().getResource("/no_sni.conf").getPath());

    assertEquals(expectedDefault, config);
  }

  @Test
  void testCreateFromNotFoundPath() {
    assertThrows(
        IllegalStateException.class,
        () -> TypesafeServerConfigFactory.create(UUID.randomUUID() + "-not-found.conf"));
  }

  @Test
  void testFullConfig() {
    ServerConfig expectedConfig =
        ServerConfig.newBuilder()
            .withSni(true)
            .withPort(19650)
            .withHostname("senders.io")
            .withHosts(
                Map.of(
                    "senders.io",
                    HostConfig.newBuilder()
                        .withCert(
                            CertConfig.newBuilder()
                                .withFile("/etc/certs/senders.io/cert.pem")
                                .withKey("/etc/certs/senders.io/key.pem")
                                .build())
                        .withDocs(
                            DocsConfig.newBuilder()
                                .withRoot("/var/public_gemini/senders.io")
                                .withDefaultLang("en")
                                .withMimeOverrides(
                                    MimeOverrideConfig.newBuilder()
                                        .addExtension("xml", "application/xml")
                                        .addFile("gemini-server.conf", "application/hocon")
                                        .build())
                                .build())
                        .build(),
                    "example.com",
                    HostConfig.newBuilder()
                        .withCert(
                            CertConfig.newBuilder()
                                .withFile("/etc/certs/example.com/cert.pem")
                                .withKey("/etc/certs/example.com/key.pem")
                                .build())
                        .withDocs(
                            DocsConfig.newBuilder()
                                .withRoot("/var/public_gemini/example.com")
                                .withDefaultLang("en,es")
                                .withMimeOverrides(
                                    MimeOverrideConfig.newBuilder()
                                        .addExtension("estxt", "text/plain")
                                        .addFile("ExampleFile", "text/plain")
                                        .build())
                                .build())
                        .build()))
            .build();
    ServerConfig config =
        TypesafeServerConfigFactory.create(
            MethodHandles.lookup().lookupClass().getResource("/full.conf").getPath());
    assertEquals(expectedConfig, config);
  }

  @Test
  void testMissingHosts() {
    assertThrows(
        ConfigException.Missing.class,
        () ->
            TypesafeServerConfigFactory.create(
                MethodHandles.lookup().lookupClass().getResource("/no_hosts.conf").getPath()));
  }

  @Test
  void testEmptyConf() {
    assertThrows(
        ConfigException.Missing.class,
        () ->
            TypesafeServerConfigFactory.create(
                MethodHandles.lookup().lookupClass().getResource("/empty.conf").getPath()));
  }
}
