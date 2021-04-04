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
package io.senders.jgs.request.routers.impls;

import io.senders.jgs.configs.HostConfig;
import io.senders.jgs.request.handlers.FileRouteHandler;
import io.senders.jgs.request.handlers.RequestHandler;
import io.senders.jgs.request.routers.Route;
import io.senders.jgs.request.routers.Router;
import java.util.Collection;
import java.util.Collections;

public class BasicFileRouter implements Router {

  private final RequestHandler fileHandler;

  public BasicFileRouter(final HostConfig config) {
    this.fileHandler = FileRouteHandler.fromConfig(config);
  }

  @Override
  public Collection<Route> routes() {
    return Collections.singleton(new Route("/*", fileHandler::handle));
  }
}
