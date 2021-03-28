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
