package io.senders.jgs.configs;

public class DocsConfig {
  private final String root;
  private final String defaultLang;
  private final MimeOverrideConfig mimeOverrides;

  public DocsConfig(String root, String defaultLang, MimeOverrideConfig mimeOverrides) {
    this.root = root;
    this.defaultLang = defaultLang;
    this.mimeOverrides = mimeOverrides;
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
