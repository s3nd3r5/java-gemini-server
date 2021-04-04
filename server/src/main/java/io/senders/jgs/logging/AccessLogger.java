package io.senders.jgs.logging;

public interface AccessLogger {

  void in(Object... arguments);

  void in(String fmt, Object... arguments);

  void in(String msg);

  void out(Object... arguments);

  void out(String fmt, Object... arguments);

  void out(String msg);
}
