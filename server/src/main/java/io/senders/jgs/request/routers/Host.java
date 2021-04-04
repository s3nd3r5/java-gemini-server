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

public class Host {

  private final String host;

  private final Collection<Router> routers;

  public Host(String host, Collection<Router> routers) {
    this.host = host;
    this.routers = routers;
  }

  public String hosts() {
    return host;
  }

  public Collection<Router> routers() {
    return routers;
  }

  public static Builder newBuilder(final String host) {
    return new Builder(host);
  }

  public static class Builder {

    private final String host;
    private final Collection<Router> routers;

    public Builder(final String host) {
      this.host = host;
      this.routers = new ArrayList<>();
    }

    public Builder withRouter(final Router router) {
      this.routers.add(router);
      return this;
    }

    public Builder withRouters(final Router... routers) {
      this.routers.addAll(Arrays.asList(routers));
      return this;
    }

    public Builder withRouters(final Collection<Router> routers) {
      this.routers.addAll(routers);
      return this;
    }

    public Host build() {
      return new Host(host, routers);
    }
  }
}
