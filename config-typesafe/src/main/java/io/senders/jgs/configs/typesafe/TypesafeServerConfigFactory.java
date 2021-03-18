package io.senders.jgs.configs.typesafe;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.senders.jgs.configs.CertConfig;
import io.senders.jgs.configs.DocsConfig;
import io.senders.jgs.configs.HostConfig;
import io.senders.jgs.configs.MimeOverrideConfig;
import io.senders.jgs.configs.ServerConfig;
import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TypesafeServerConfigFactory {

  private TypesafeServerConfigFactory() {}

  /**
   * Create a server configuration from the default typesafe locations.
   *
   * @see ConfigFactory#load() for how it searches for files
   * @return ServerConfig loaded from the server.conf in the classpath
   */
  public static ServerConfig create() {
    final Config config = ConfigFactory.load();
    return createServerConfig(config);
  }

  public static ServerConfig create(final String configPath) {
    File file = Paths.get(configPath).toFile();
    if (!file.exists() || !file.canRead()) {
      throw new IllegalStateException(
          "config file " + configPath + " cannot be read or doesn't exist");
    }

    return createServerConfig(ConfigFactory.parseFile(file));
  }

  private static ServerConfig createServerConfig(Config config) {

    Config hosts = config.getConfig("hosts");
    Map<String, HostConfig> hostConfigMap = new HashMap<>();

    for (String hostKey : hosts.root().keySet()) {
      String hostPath = "hosts.\"" + hostKey + "\"";
      String docsPath = hostPath + ".docs";
      DocsConfig.Builder docsConfig =
          DocsConfig.newBuilder().withRoot(config.getString(docsPath + ".root"));

      if (config.hasPath(docsPath + ".defaultLang")) {
        docsConfig.withDefaultLang(config.getString(docsPath + ".defaultLang"));
      }

      if (config.hasPath(docsPath + ".mimeOverrides")) {
        String mimesPath = docsPath + ".mimeOverrides";
        MimeOverrideConfig.Builder mimeOverrideConfig = MimeOverrideConfig.newBuilder();

        if (config.hasPath(mimesPath + ".files")) {
          String filesPath = mimesPath + ".files";
          Config mimeFiles = config.getConfig(filesPath);
          Map<String, String> mimeFilesMap =
              mimeFiles.entrySet().stream()
                  .map(
                      e ->
                          Map.entry(
                              stripQuotes(e.getKey()), String.valueOf(e.getValue().unwrapped())))
                  .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
          mimeOverrideConfig.withFiles(mimeFilesMap);
        }

        if (config.hasPath(mimesPath + ".extensions")) {
          Config mimeExtensions = config.getConfig(mimesPath + ".extensions");
          Map<String, String> mimeExtensionsMap =
              mimeExtensions.entrySet().stream()
                  .map(
                      e ->
                          Map.entry(
                              stripQuotes(e.getKey()), String.valueOf(e.getValue().unwrapped())))
                  .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
          mimeOverrideConfig.withExtensions(mimeExtensionsMap);
        }

        docsConfig.withMimeOverrides(mimeOverrideConfig.build());
      }

      String certPath = hostPath + ".cert";
      CertConfig certConfig =
          CertConfig.newBuilder()
              .withFile(config.getString(certPath + ".file"))
              .withKey(config.getString(certPath + ".key"))
              .build();

      HostConfig hostConfig =
          HostConfig.newBuilder().withDocs(docsConfig.build()).withCert(certConfig).build();
      hostConfigMap.put(hostKey, hostConfig);
    }

    return ServerConfig.newBuilder()
        .withSni(getBoolean(config, "sni", false))
        .withPort(config.getInt("port"))
        .withHostname(config.getString("hostname"))
        .withHosts(hostConfigMap)
        .build();
  }

  private static String stripQuotes(String key) {
    return key.replace("\"", "");
  }

  private static boolean getBoolean(Config config, String path, boolean defaultVal) {
    if (config.hasPath(path)) {
      return config.getBoolean(path);
    } else {
      return defaultVal;
    }
  }
}
