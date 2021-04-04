package io.senders.jgs.logging;

/**
 * Custom Logging interface. Intended to allow for separate logging between the server and the
 * access logging.
 *
 * @see LogbackAccessLogger
 */
public interface AccessLogger {

  /**
   * Logs a varargs of arguments for 'incoming' access message. Intended to represent the log for
   * what the client is requesting.
   *
   * @param arguments incoming request details to be formatted and logged
   */
  void in(Object... arguments);

  /**
   * Logs a varargs of arguments for 'outgoing' access message. Intended to represent the log for
   * what the server is serving.
   *
   * @param arguments outgoing response details to be formatted and logged
   */
  void out(Object... arguments);

  /**
   * Logs a custom formatting message to the access log
   *
   * @param fmt format for the log message
   * @param arguments varargs to format in the log message
   */
  void log(String fmt, Object... arguments);

  /**
   * Logs a message to the access log
   *
   * @param msg message to log
   */
  void log(String msg);
}
