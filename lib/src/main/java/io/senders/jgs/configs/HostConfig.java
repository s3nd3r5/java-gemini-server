package io.senders.jgs.configs;

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
