/*-
 * -\-\-
 * Java Gemini Library
 * --
 * Copyright (C) 2021 - 2023 senders <steph (at) senders.io>
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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ServerConfig {
  private final boolean sni;
  private final int port;
  private final int numMainThreads;
  private final int numWorkerThreads;
  private final String hostname;
  private final Map<String, HostConfig> hosts;

  public ServerConfig(boolean sni, int port, String hostname, Map<String, HostConfig> hosts) {
    this.sni = sni;
    this.port = port;
    this.numMainThreads = 0;
    this.numWorkerThreads = 0;
    this.hostname = hostname;
    this.hosts = Map.copyOf(Objects.requireNonNull(hosts, "hosts"));
  }

  public ServerConfig(
      boolean sni,
      int port,
      int numMainThreads,
      int numWorkerThreads,
      String hostname,
      Map<String, HostConfig> hosts) {
    this.sni = sni;
    this.port = port;
    this.numMainThreads = numMainThreads;
    this.numWorkerThreads = numWorkerThreads;
    this.hostname = hostname;
    this.hosts = Map.copyOf(Objects.requireNonNull(hosts, "hosts"));
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(final ServerConfig fromConfig) {
    return new Builder()
        .withSni(fromConfig.sni)
        .withHostname(fromConfig.hostname)
        .withHosts(fromConfig.hosts)
        .withPort(fromConfig.port);
  }

  public boolean sni() {
    return sni;
  }

  public int port() {
    return port;
  }

  public int numMainThreads() {
    return numMainThreads;
  }

  public int numWorkerThreads() {
    return numWorkerThreads;
  }

  public String hostname() {
    return hostname;
  }

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ServerConfig that = (ServerConfig) o;
    return sni == that.sni
        && port == that.port
        && numMainThreads == that.numMainThreads
        && numWorkerThreads == that.numWorkerThreads
        && Objects.equals(hostname, that.hostname)
        && Objects.equals(hosts, that.hosts);
  }

  @Override
  public String toString() {
    String sb =
        "ServerConfig{"
            + "sni="
            + sni
            + ", port="
            + port
            + ", numMainThreads="
            + numMainThreads
            + ", numWorkerThreads="
            + numWorkerThreads
            + ", hostname='"
            + hostname
            + '\''
            + ", hosts="
            + hosts
            + '}';
    return sb;
  }

  @Override
  public int hashCode() {
    return Objects.hash(sni, port, numMainThreads, numWorkerThreads, hostname, hosts);
  }

  public static class Builder {

    private boolean sni;
    private int port;
    private int numMainThreads;
    private int numWorkerThreads;
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
     * Sets the number of threads for the main NIO group.
     *
     * @param nThreads number of threads - 0 means unbound
     * @return self
     * @see ServerConfig#numMainThreads()
     * @see <a
     *     href="https://netty.io/4.1/api/io/netty/bootstrap/ServerBootstrap.html#group-io.netty.channel.EventLoopGroup-io.netty.channel.EventLoopGroup-"
     *     >Netty 4.1 Javadoc for ServerBootstrap#group(EventLoopGroup parent, EventLoopGroup child)
     *     </a>
     */
    public Builder withNumMainThreads(int nThreads) {
      this.numMainThreads = nThreads;
      return this;
    }

    /**
     * Sets the number of threads for the worker NIO group.
     *
     * @param nThreads number of threads - 0 means unbound
     * @return self
     * @see ServerConfig#numWorkerThreads()
     * @see <a
     *     href="https://netty.io/4.1/api/io/netty/bootstrap/ServerBootstrap.html#group-io.netty.channel.EventLoopGroup-io.netty.channel.EventLoopGroup-"
     *     >Netty 4.1 Javadoc for ServerBootstrap#group(EventLoopGroup parent, EventLoopGroup child)
     *     </a>
     */
    public Builder withNumWorkerThreads(int nThreads) {
      this.numWorkerThreads = nThreads;
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
      return new ServerConfig(sni, port, numMainThreads, numWorkerThreads, hostname, hosts);
    }
  }
}
