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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MimeOverrideConfig {
  private final Map<String, String> files;
  private final Map<String, String> extensions;

  public MimeOverrideConfig(Map<String, String> files, Map<String, String> extensions) {

    this.files = files == null ? Collections.emptyMap() : Map.copyOf(files);
    this.extensions = extensions == null ? Collections.emptyMap() : Map.copyOf(extensions);
  }

  public Map<String, String> files() {
    return files;
  }

  public Map<String, String> extensions() {
    return extensions;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static Builder newBuilder(MimeOverrideConfig copy) {
    return new Builder().withFiles(copy.files()).withExtensions(copy.extensions());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MimeOverrideConfig that = (MimeOverrideConfig) o;
    return Objects.equals(files, that.files) && Objects.equals(extensions, that.extensions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(files, extensions);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("MimeOverrideConfig{");
    sb.append("files=").append(files);
    sb.append(", extensions=").append(extensions);
    sb.append('}');
    return sb.toString();
  }

  public static class Builder {
    private Map<String, String> files = new HashMap<>();
    private Map<String, String> extensions = new HashMap<>();

    private Builder() {}

    public Builder withFiles(Map<String, String> files) {
      this.files = new HashMap<>(Objects.requireNonNull(files, "files"));
      return this;
    }

    public Builder addFiles(Map<String, String> files) {
      this.files.putAll(Objects.requireNonNull(files, "files"));
      return this;
    }

    public Builder addFile(String filename, String mimeType) {
      this.files.put(filename, mimeType);
      return this;
    }

    public Builder withExtensions(Map<String, String> extensions) {
      this.extensions = new HashMap<>(Objects.requireNonNull(extensions, "extensions"));
      return this;
    }

    public Builder addExtensions(Map<String, String> extensions) {
      this.extensions.putAll(Objects.requireNonNull(extensions, "extensions"));
      return this;
    }

    public Builder addExtension(String extension, String mimeType) {
      this.extensions.put(extension, mimeType);
      return this;
    }

    public MimeOverrideConfig build() {
      return new MimeOverrideConfig(files, extensions);
    }
  }
}
