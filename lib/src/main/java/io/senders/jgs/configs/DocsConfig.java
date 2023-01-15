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

import java.util.Objects;

public class DocsConfig {
  private final String root;
  private final String defaultLang;
  private final MimeOverrideConfig mimeOverrides;

  public DocsConfig(String root, String defaultLang, MimeOverrideConfig mimeOverrides) {
    this.root = Objects.requireNonNull(root, "root");
    this.defaultLang = defaultLang;
    this.mimeOverrides =
        mimeOverrides != null ? mimeOverrides : MimeOverrideConfig.newBuilder().build();
  }

  public String root() {
    return root;
  }

  public String defaultLang() {
    return defaultLang;
  }

  public MimeOverrideConfig mimeOverrides() {
    return mimeOverrides;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(DocsConfig copy) {
    return new Builder()
        .withDefaultLang(copy.defaultLang)
        .withMimeOverrides(copy.mimeOverrides)
        .withRoot(copy.root);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DocsConfig that = (DocsConfig) o;
    return Objects.equals(root, that.root)
        && Objects.equals(defaultLang, that.defaultLang)
        && Objects.equals(mimeOverrides, that.mimeOverrides);
  }

  @Override
  public int hashCode() {
    return Objects.hash(root, defaultLang, mimeOverrides);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("DocsConfig{");
    sb.append("root='").append(root).append('\'');
    sb.append(", defaultLang='").append(defaultLang).append('\'');
    sb.append(", mimeOverrides=").append(mimeOverrides);
    sb.append('}');
    return sb.toString();
  }

  public static final class Builder {

    private String root;
    private String defaultLang;
    private MimeOverrideConfig mimeOverrides;

    private Builder() {}

    public Builder withRoot(String root) {
      this.root = root;
      return this;
    }

    public Builder withDefaultLang(String defaultLang) {
      this.defaultLang = defaultLang;
      return this;
    }

    public Builder withMimeOverrides(MimeOverrideConfig mimeOverrides) {
      this.mimeOverrides = mimeOverrides;
      return this;
    }

    public DocsConfig build() {
      return new DocsConfig(root, defaultLang, mimeOverrides);
    }
  }
}
