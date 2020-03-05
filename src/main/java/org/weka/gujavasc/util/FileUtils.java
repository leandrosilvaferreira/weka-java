package org.weka.gujavasc.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FileUtils {

  public static String getResoucePath(final String path) {

    final ClassLoader loader = Thread.currentThread().getContextClassLoader();
    final URL url = loader.getResource(path);

    return url.getPath();
  }

  public static List<File> getResourceFolderFiles(final String folder) {

    List<File> files = new ArrayList<>();

    try {
      final String folderPath = getResoucePath(folder);

      files =
          Files.list(Paths.get(folderPath))
              .map(filePath -> filePath.toFile())
              .collect(Collectors.toList());

    } catch (final IOException e) {
      e.printStackTrace();
    }

    return files;
  }

  public static File getResourceFile(final String path) {

    final String filePath = getResoucePath(path);
    return new File(filePath);
  }

  public static String salvarArquivo(final String nomeArquivo, final String conteudo)
      throws IOException {

    final File arquivo = new File(nomeArquivo);
    final FileOutputStream f = new FileOutputStream(arquivo);
    f.write(conteudo.getBytes());
    f.close();

    return String.format("Aquivo %s salvo em %s", arquivo.getName(), arquivo.getAbsolutePath());
  }

  public static String getResourcesAbsolutPath() {
    return Paths.get("").toAbsolutePath() + "/src/main/resources/";
  }
}
