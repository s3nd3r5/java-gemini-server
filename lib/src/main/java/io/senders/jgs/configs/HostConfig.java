package io.senders.jgs.configs;

import java.util.Objects;

public class HostConfig {
  private final String hostname;
  private final DocsConfig docs;
  private final CertConfig cert;

  public HostConfig(final String hostname, final DocsConfig docs, final CertConfig cert) {
    this.hostname = Objects.requireNonNull(hostname, "hostname");
    this.docs = Objects.requireNonNull(docs, "docs");
    this.cert = Objects.requireNonNull(cert, "cert");
  }

  public static Builder newBuilder(final String hostname) {
    return new Builder(hostname);
  }

  public static Builder newBuilder(final HostConfig copy) {
    return new Builder(copy.hostname).withCert(copy.cert).withDocs(copy.docs);
  }

  public String hostname() {
    return hostname;
  }

  public DocsConfig docs() {
    return docs;
  }

  public CertConfig cert() {
    return cert;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    HostConfig that = (HostConfig) o;
    return Objects.equals(hostname, that.hostname)
        && Objects.equals(docs, that.docs)
        && Objects.equals(cert, that.cert);
  }

  @Override
  public int hashCode() {
    return Objects.hash(hostname, docs, cert);
  }

  public static final class Builder {

    private String hostname;
    private DocsConfig docs;
    private CertConfig cert;

    private Builder(String hostname) {
      this.hostname = hostname;
    }

    public Builder withHostname(String hostname) {
      this.hostname = hostname;
      return this;
    }

    public Builder withDocs(DocsConfig docs) {
      this.docs = docs;
      return this;
    }

    public Builder withCert(CertConfig cert) {
      this.cert = cert;
      return this;
    }

    public HostConfig build() {
      return new HostConfig(this.hostname, this.docs, this.cert);
    }
  }
}
