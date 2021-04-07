/*-
 * -\-\-
 * Java Gemini Server
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
package io.senders.jgs.request.routers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/** Container class for your hosts routers. */
public class Host {

  private final String hostname;

  private final Collection<Router> routers;

  /**
   * Create a new host definition
   *
   * @param hostname hostname associated with this host
   * @param routers routers for this host
   */
  public Host(String hostname, Collection<Router> routers) {
    if (routers == null || routers.isEmpty()) {
      throw new IllegalArgumentException("routers cannot be empty");
    }
    if (hostname == null || hostname.isBlank()) {
      throw new IllegalArgumentException("hostname cannot be blank");
    }
    this.hostname = hostname;
    this.routers = List.copyOf(routers);
  }

  /**
   * Get hostname of host
   *
   * @return hostname
   */
  public String hostname() {
    return hostname;
  }

  /**
   * Get routers for host
   *
   * @return routers
   */
  public Collection<Router> routers() {
    return routers;
  }

  /**
   * Create new builder for hostname
   *
   * @param hostname required hostname for the host
   * @return a new builder instance
   */
  public static Builder newBuilder(final String hostname) {
    return new Builder(hostname);
  }

  /** Builder for host */
  public static class Builder {

    private String hostname;
    private Collection<Router> routers;

    /**
     * Constructs a new builder
     *
     * @param hostname hostname for the host
     * @see Host#newBuilder(String)
     */
    public Builder(final String hostname) {
      this.hostname = hostname;
      this.routers = new ArrayList<>();
    }

    /**
     * Override hostname used when instantiating the builder
     *
     * @param hostname hostname for host
     * @return builder for chaining
     */
    public Builder withHostname(final String hostname) {
      this.hostname = hostname;
      return this;
    }

    /**
     * Add a router to the host
     *
     * @param router a router for the host
     * @return builder for chaining
     */
    public Builder withRouter(final Router router) {
      this.routers.add(router);
      return this;
    }

    /**
     * Add multiple routers to host
     *
     * @param routers varargs of routes to add
     * @return builder for chaining
     */
    public Builder withRouters(final Router... routers) {
      this.routers.addAll(Arrays.asList(routers));
      return this;
    }

    /**
     * Add a collection of routers to host
     *
     * @param routers collection of routers to add
     * @return builder for chaining
     */
    public Builder withRouters(final Collection<Router> routers) {
      this.routers.addAll(routers);
      return this;
    }

    /**
     * Overwrite the routers with collection
     *
     * @param routers new collection of routers for host
     * @return builder for chaining
     */
    public Builder setRouters(final Collection<Router> routers) {
      this.routers = List.copyOf(routers);
      return this;
    }

    /**
     * Create new host instance
     *
     * @return created host
     */
    public Host build() {
      return new Host(hostname, routers);
    }
  }
}
