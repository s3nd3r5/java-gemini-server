/*-
 * -\-\-
 * Java Gemini Server
 * --
 * Copyright (C) 2021 - 2022 senders <steph (at) senders.io>
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
package io.senders.jgs.logging;

/** Custom logger for access logs. Used to separate server logs from access logs. */
public interface AccessLogger {

  /**
   * Log incoming request information
   *
   * @param arguments request info to log
   */
  void in(Object... arguments);

  /**
   * Log outgoing response information
   *
   * @param arguments response info to log
   */
  void out(Object... arguments);

  /**
   * Generic log method. You can specify a custom format and arguments
   *
   * @param fmt message format for log
   * @param arguments info to log
   */
  void log(String fmt, Object... arguments);

  /**
   * Generic log method to log any string
   *
   * @param msg message to log
   */
  void log(String msg);
}
