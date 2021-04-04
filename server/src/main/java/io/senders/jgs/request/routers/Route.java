/*-
 * -\-\-
 * Java Gemini Server
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
package io.senders.jgs.request.routers;

import io.senders.jgs.request.Request;
import io.senders.jgs.response.ResponseMessage;
import java.util.function.Function;

/**
 * Defines a route path and the handler to be used when a request matches this route.
 *
 * @see Router
 * @see io.senders.jgs.request.RequestMessageAdapter
 */
public class Route {
  private final String route;
  private final Function<Request, ResponseMessage> handleFn;

  /**
   * Create a new Route definition
   *
   * @param route route path
   * @param handleFn request handler
   */
  public Route(String route, Function<Request, ResponseMessage> handleFn) {
    this.route = route;
    this.handleFn = handleFn;
  }

  /**
   * Get the route path
   *
   * @return route path
   */
  public String route() {
    return this.route;
  }

  /**
   * handle the incoming request matched for this route
   *
   * @param request incoming request information
   * @return {@link ResponseMessage} based on the handler of this route
   * @implNote This method does not guarantee the request matches the route. That is to be handled
   *     externally.
   * @see io.senders.jgs.request.RequestMessageAdapter
   */
  public ResponseMessage handle(final Request request) {
    return this.handleFn.apply(request);
  }
}
