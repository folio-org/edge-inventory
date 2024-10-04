package org.folio.edge.inventory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.IOUtils;

@Log4j2
public class TestUtil {

  public static String readFileContentFromResources(String path) {
    try {
      ClassLoader classLoader = TestUtil.class.getClassLoader();
      URL url = classLoader.getResource(path);
      return IOUtils.toString(url, StandardCharsets.UTF_8);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
      throw new IllegalStateException(e);
    }
  }

  public static byte[] readFileBytesFromResources(String resourcePath) {
    try {
      return Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(resourcePath).toURI()));
    } catch (IOException | URISyntaxException e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException("Failed to read file from resources", e);
    }
  }
}
