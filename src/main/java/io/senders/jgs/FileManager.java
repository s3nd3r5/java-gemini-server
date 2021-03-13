package io.senders.jgs;

import io.senders.jgs.exceptions.InvalidResourceException;
import io.senders.jgs.exceptions.ResourceNotFoundException;
import io.senders.jgs.mime.MimeTypes;
import io.senders.jgs.response.ResponseDoc;
import io.senders.jgs.util.FileUtil;
import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileManager {
  private static final Logger logger =
      LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

  private static final String BASE_PATH = "/tmp/docs";
  private final MimeTypes mimeTypes;

  public FileManager(final MimeTypes mimeTypes) {
    this.mimeTypes = mimeTypes;
  }

  public ResponseDoc load(URI uri) {
    try {
      // TODO figure out how to handle index directories best
      String path = uri.getPath();
      if (path.endsWith("/")) {
        path += "index.gmi";
      }
      Path docPath = Paths.get(BASE_PATH, path);
      File file = docPath.toFile();

      if (!file.exists() || !file.canRead()) {
        throw new ResourceNotFoundException(path + " not found");
      }

      String extension = FileUtil.getExtension(file.getName());
      String mimeType = mimeTypes.getMimeType(extension, Files.probeContentType(docPath));
      byte[] data = Files.readAllBytes(docPath);

      return new ResponseDoc(mimeType, null, "en", data);
    } catch (IOException e) {
      logger.error("Unable to read resource", e);
      throw new InvalidResourceException("Unexpected error reading requested resource");
    }
  }
}
