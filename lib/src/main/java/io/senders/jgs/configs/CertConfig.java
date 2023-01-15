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

public class CertConfig {
  private final String file;
  private final String key;

  public CertConfig(String file, String key) {
    this.file = Objects.requireNonNull(file, "file");
    this.key = Objects.requireNonNull(key, "key");
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(CertConfig copy) {
    return new Builder().withFile(copy.file).withKey(copy.key);
  }

  public String file() {
    return file;
  }

  public String key() {
    return key;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CertConfig that = (CertConfig) o;
    return Objects.equals(file, that.file) && Objects.equals(key, that.key);
  }

  @Override
  public int hashCode() {
    return Objects.hash(file, key);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("CertConfig{");
    sb.append("file='").append(file).append('\'');
    sb.append(", key='").append(key).append('\'');
    sb.append('}');
    return sb.toString();
  }

  public static final class Builder {

    private String file;
    private String key;

    private Builder() {}

    public Builder withFile(String file) {
      this.file = file;
      return this;
    }

    public Builder withKey(String key) {
      this.key = key;
      return this;
    }

    public CertConfig build() {
      return new CertConfig(file, key);
    }
  }
}
