/*-
 * -\-\-
 * Java Gemini Library
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
  public String toString() {
    final StringBuilder sb = new StringBuilder("HostConfig{");
    sb.append("hostname='").append(hostname).append('\'');
    sb.append(", docs=").append(docs);
    sb.append(", cert=").append(cert);
    sb.append('}');
    return sb.toString();
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
