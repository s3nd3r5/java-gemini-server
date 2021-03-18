package io.senders.jgs.status;

public enum GeminiStatus {
  INPUT("10"),
  SENSITIVE_INPUT("11"),
  SUCCESS("20"),
  REDIRECT_TEMPORARY("30"),
  REDIRECT_PERMANENT("31"),
  TEMPORARY_FAILURE("40"),
  SERVER_UNAVAILABLE("41"),
  GCI_ERROR("42"),
  PROXY_ERROR("43"),
  SLOW_DOWN("44"),
  PERMANENT_FAILURE("50"),
  NOT_FOUND("51"),
  GONE("52"),
  PROXY_REQUEST_REFUSED("53"),
  BAD_REQUEST("59"),
  CLIENT_CERTIFICATE_REQUIRED("60"),
  CERTIFICATE_NOT_AUTHORISED("61"),
  CERTIFICATE_NOT_VALID("62");

  private final String code;

  GeminiStatus(String code) {
    this.code = code;
  }

  public String code() {
    return this.code;
  }
}
