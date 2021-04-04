package io.senders.jgs.logging;

public interface AccessLogger {

  void in(Object... arguments);

  void out(Object... arguments);
}
