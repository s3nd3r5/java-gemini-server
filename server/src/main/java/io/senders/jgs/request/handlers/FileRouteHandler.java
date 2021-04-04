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
package io.senders.jgs.request.handlers;

import io.senders.jgs.configs.HostConfig;
import io.senders.jgs.exceptions.InvalidResourceException;
import io.senders.jgs.exceptions.ResourceNotFoundException;
import io.senders.jgs.mime.MimeTypes;
import io.senders.jgs.request.Request;
import io.senders.jgs.response.ResponseDoc;
import io.senders.jgs.response.ResponseMessage;
import io.senders.jgs.status.GeminiStatus;
import io.senders.jgs.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileRouteHandler implements RequestHandler {
  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
  private static final String LINK_FMT = "=> %s %s";

  private final MimeTypes mimeTypes;
  private final String defaultLang;
  private final String docRoot;

  private FileRouteHandler(
      final MimeTypes mimeTypes, final String docRoot, final String defaultLang) {
    this.mimeTypes = mimeTypes;
    this.docRoot = docRoot;
    this.defaultLang = defaultLang;
  }

  public static FileRouteHandler fromConfig(final HostConfig config) {
    final MimeTypes mimeTypes = new MimeTypes(config.docs().mimeOverrides().extensions());
    final String docRoot = config.docs().root();
    final String defaultLang = config.docs().defaultLang();

    return new FileRouteHandler(mimeTypes, docRoot, defaultLang);
  }

  @Override
  public ResponseMessage handle(Request request) {
    var uri = request.uri();
    try {
      String path = uri.getPath();
      if (path.endsWith("/")) {
        path += "index.gmi";
      }
      Path docPath = Paths.get(docRoot, path);
      File file = docPath.toFile();

      if (file.isDirectory()) {
        return new ResponseMessage(GeminiStatus.REDIRECT_PERMANENT, path + "/");
      }

      if (!file.exists() || !file.canRead()) {
        if (path.endsWith("index.gmi")) {
          return generateDirectory(uri.getPath(), docPath);
        }
        throw new ResourceNotFoundException(path + " not found");
      }

      String extension = FileUtil.getExtension(file.getName());
      String mimeType = mimeTypes.getMimeType(extension, Files.probeContentType(docPath));
      byte[] data = Files.readAllBytes(docPath);

      return new ResponseDoc(mimeType, null, defaultLang, data);
    } catch (IOException e) {
      logger.error("Unable to read resource", e);
      throw new InvalidResourceException("Unexpected error reading requested resource");
    }
  }

  private ResponseDoc generateDirectory(String uriPath, Path docPath) {
    StringBuilder directoryResponse = new StringBuilder();
    directoryResponse.append("# Directory Index").append("\r\n").append("\r\n");
    var parent = docPath.getParent();
    try {
      String fileList =
          Files.walk(parent, 1)
              .map(Path::toFile)
              .filter(File::isFile)
              .filter(File::canRead)
              .sorted(Comparator.comparing(File::getName))
              .map(
                  f ->
                      String.format(
                          LINK_FMT,
                          Path.of(uriPath, f.getName()),
                          FileUtil.getTitle(f).orElse(f.getName())))
              .collect(Collectors.joining("\r\n"));
      directoryResponse.append(fileList).append("\r\n").append("\r\n");
      directoryResponse.append("This directory was auto-generated").append("\r\n");
      return new ResponseDoc(
          mimeTypes.getMimeType("gmi", "text/gemini"),
          StandardCharsets.UTF_8.displayName(),
          defaultLang,
          directoryResponse.toString().getBytes());
    } catch (IOException | UncheckedIOException e) {
      logger.error("Unable to generate directory index", e);
      throw new ResourceNotFoundException(docPath.toString() + " not found");
    }
  }
}
