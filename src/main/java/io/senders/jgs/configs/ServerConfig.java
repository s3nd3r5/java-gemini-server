package io.senders.jgs.configs;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerConfig {
  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  // SERVER CONFIGS
  private static final String HOSTNAME = "server.hostname";
  private static final String PORT = "server.port";

  // DOC SERVING
  private static final String DOC_ROOT = "server.docs.root";
  private static final String DEFAULT_LANG = "server.docs.lang";
  private static final String MIMES = "server.docs.mimes";

  // CERTS
  private static final String CERT_KEY = "server.certs.key";
  private static final String CERT_FILE = "server.certs.cert";

  private static final String DEFAULT_FILE_LOCATION = "./server.properties";

  private final Properties config;

  // singleton initialized by getMimeOverrides
  private Map<String, String> mimeOverrides;

  private ServerConfig(final Properties configPropertiesFile) {
    this.config = configPropertiesFile;
  }

  public static ServerConfig create() {
    return create(DEFAULT_FILE_LOCATION);
  }

  public static ServerConfig create(final String filePath) {
    try {
      Properties properties = new Properties();
      var path = Path.of(filePath);
      var file = path.toFile();
      if (file.exists() && file.canRead()) {
        properties.load(Files.newInputStream(path, StandardOpenOption.READ));
      } else {
        logger.warn("Using default configuration: {} does not exist", path.toAbsolutePath());
      }
      return new ServerConfig(properties);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public int getPort() {
    return Integer.parseInt(config.getProperty(PORT, "1965"));
  }

  public String getHostname() {
    return config.getProperty(HOSTNAME, "localhost");
  }

  public String getDocRoot() {
    return config.getProperty(DOC_ROOT, "/var/gemini");
  }

  public String getCertKey() {
    return config.getProperty(CERT_KEY, "key.pem");
  }

  public String getCertFile() {
    return config.getProperty(CERT_FILE, "cert.pem");
  }

  public Optional<String> getDefaultLang() {
    return Optional.ofNullable(config.getProperty(DEFAULT_LANG));
  }

  public Map<String, String> getMimeOverrides() {
    if (this.mimeOverrides == null) {
      final Map<String, String> mimeOverrides = new HashMap<>();
      config.stringPropertyNames().stream()
          .filter(key -> key.startsWith(MIMES))
          .forEach(
              key -> {
                String mimeType = config.getProperty(key);
                String extension = key.substring(key.lastIndexOf(".") + 1);
                mimeOverrides.put(extension, mimeType);
              });
      this.mimeOverrides = mimeOverrides;
    }
    return Map.copyOf(this.mimeOverrides);
  }
}
