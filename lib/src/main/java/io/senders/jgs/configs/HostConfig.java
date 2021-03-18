package io.senders.jgs.configs;

import java.util.Objects;

public class HostConfig {
  private final DocsConfig docs;
  private final CertConfig cert;

  public HostConfig(DocsConfig docs, CertConfig cert) {
    this.docs = docs;
    this.cert = cert;
  }

  private HostConfig(Builder builder) {
    docs = builder.docs;
    cert = builder.cert;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(HostConfig copy) {
    return new Builder().withCert(copy.cert).withDocs(copy.docs);
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
    return Objects.equals(docs, that.docs) && Objects.equals(cert, that.cert);
  }

  @Override
  public int hashCode() {
    return Objects.hash(docs, cert);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("HostConfig{");
    sb.append("docs=").append(docs);
    sb.append(", cert=").append(cert);
    sb.append('}');
    return sb.toString();
  }

  public static final class Builder {

    private DocsConfig docs;
    private CertConfig cert;

    private Builder() {}

    public Builder withDocs(DocsConfig docs) {
      this.docs = docs;
      return this;
    }

    public Builder withCert(CertConfig cert) {
      this.cert = cert;
      return this;
    }

    public HostConfig build() {
      return new HostConfig(this);
    }
  }
}
