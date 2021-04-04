package io.senders.jgs.configs;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ServerConfig {
  private final boolean sni;
  private final int port;
  private final String hostname;
  private final Map<String, HostConfig> hosts;

  public ServerConfig(boolean sni, int port, String hostname, Map<String, HostConfig> hosts) {
    this.sni = sni;
    this.port = port;
    this.hostname = hostname;
    this.hosts = Map.copyOf(Objects.requireNonNull(hosts, "hosts"));
  }

  /**
   * Is SNI (Server Name Indication) enabled for the server. This allows using separate certs for
   * each host.
   *
   * @return sni configuration status (true if enabled)
   */
  public boolean sni() {
    return sni;
  }

  /**
   * The port the server will listen on.
   *
   * @return port number
   */
  public int port() {
    return port;
  }

  /**
   * The default hostname. Used to fetch the host from the hosts map config and as the default host
   * to provide from SNI. To disable default host cert providing in SNI omit this configuration.
   *
   * @return the default hostname
   */
  public String hostname() {
    return hostname;
  }

  /**
   * The host configuration mapping to be used by the server. Requires at least one host
   * configuration to be set. Each {@link HostConfig} is mapped by hostname (FQDN).
   *
   * <p>// TODO Make note about how to configure wildcard support - link to whichever server class
   * implements the SNI configuration
   *
   * @return host mapping
   */
  public Map<String, HostConfig> hosts() {
    return hosts;
  }

  // TODO document and guard method against nulls
  public HostConfig defaultHostConfig() {
    if (hosts.containsKey(hostname)) {
      return hosts.get(hostname);
    }
    throw new IllegalStateException("Default hostname " + hostname + " has no config.");
  }

  /** @see ServerConfig for defaults and required fields. */
  public static Builder newBuilder() {
    return new Builder();
  }

  /**
   * Construct a builder from an existing ServerConfig.
   *
   * @param fromConfig configuration to set the initial builder values to
   * @see #newBuilder()
   * @implNote hosts will by copied into a new mutable
   * @see Builder#withHosts(Map) for overwritting the fromConfig host mapping
   */
  public static Builder newBuilder(final ServerConfig fromConfig) {
    return new Builder()
        .withSni(fromConfig.sni)
        .withHostname(fromConfig.hostname)
        .withHosts(fromConfig.hosts)
        .withPort(fromConfig.port);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServerConfig config = (ServerConfig) o;
    return sni == config.sni
        && port == config.port
        && Objects.equals(hostname, config.hostname)
        && Objects.equals(hosts, config.hosts);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("ServerConfig{");
    sb.append("sni=").append(sni);
    sb.append(", port=").append(port);
    sb.append(", hostname='").append(hostname).append('\'');
    sb.append(", hosts=").append(hosts);
    sb.append('}');
    return sb.toString();
  }

  @Override
  public int hashCode() {
    return Objects.hash(sni, port, hostname, hosts);
  }

  public static class Builder {

    private boolean sni;
    private int port;
    private String hostname;
    private Map<String, HostConfig> hosts = new HashMap<>();

    private Builder() {}

    /**
     * Enables SNI (Server Name Indication) for the server.
     *
     * @param sni true/false if enabled/disabled.
     * @return builder for chaining
     * @see ServerConfig#sni()
     */
    public Builder withSni(boolean sni) {
      this.sni = sni;
      return this;
    }

    /**
     * Sets the port the server will listen on.
     *
     * @param port the serer will listen on
     * @return builder for chaining
     * @see ServerConfig#port()
     */
    public Builder withPort(int port) {
      this.port = port;
      return this;
    }

    /**
     * Sets the default hostname for the server.
     *
     * @param hostname the default hostname
     * @return builder for chaining
     * @see ServerConfig#hostname()
     */
    public Builder withHostname(String hostname) {
      this.hostname = hostname;
      return this;
    }

    /**
     * Sets the host configuration map to the argument map.
     *
     * @param hosts non-null host mapping to use
     * @return builder for chaining
     * @implNote Requires non-null to unset any existing configurations pass in an empty map
     * @implNote This method will overwrite any existing host mappings
     * @see #addHosts(Map) for adding multiple hosts at once without overwritting
     * @see ServerConfig#hosts()
     * @see HostConfig
     */
    public Builder withHosts(final Map<String, HostConfig> hosts) {
      this.hosts = new HashMap<>(Objects.requireNonNull(hosts, "hosts"));
      return this;
    }

    /**
     * Adds the host configuration mappings to the existing hosts map.
     *
     * @param hosts host mappings to put with previous configs
     * @return builder for chaining
     * @see #withHosts(Map) to overwrite all previous mappings
     * @see ServerConfig#hosts()
     * @see HostConfig
     */
    public Builder addHosts(Map<String, HostConfig> hosts) {
      this.hosts.putAll(hosts);
      return this;
    }

    /**
     * Add a host and configuration to the hosts mapping.
     *
     * @param hostname used as key for the config
     * @param config HostConfig for the given hostname
     * @return builder for chianing
     * @see #addHosts(Map) for adding multiple mappings
     * @see ServerConfig#hosts()
     * @see HostConfig
     */
    public Builder addHost(String hostname, HostConfig config) {
      this.hosts.put(hostname, config);
      return this;
    }

    /**
     * Creates a ServerConfig implementation.
     *
     * @return new ServerConfig from builder values
     * @see ServerConfig for required fields and validation
     */
    public ServerConfig build() {
      return new ServerConfig(sni, port, hostname, hosts);
    }
  }
}
