package io.senders.jgs.configs;

public class CertConfig {
  private final String file;
  private final String key;

  public CertConfig(String file, String key) {
    this.file = file;
    this.key = key;
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
