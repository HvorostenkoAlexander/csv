package com.mm.anonymisation.service;

import com.mm.anonymisation.model.Table;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import lombok.SneakyThrows;

public class ResourceScriptGenerator implements ScriptGenerator {

  private static final String SQL_PATH = "sql/";
  private static final String SQL_EXT = ".sql";

  @SneakyThrows
  @Override
  public List<String> generate(List<Table> tables, String path) {
    URL resource = this.getClass().getClassLoader().getResource("sql");
    if (null == resource) {
      return Collections.emptyList();
    }
    return listFiles(resource).stream()
        .sorted()
        .map(this::readSqlResource)
        .toList();
  }

  private List<String> listFiles(URL resource) throws IOException, URISyntaxException {
    if (resource.getProtocol().equals("jar")) {
      String substring = resource.getPath().substring(5, resource.getPath().indexOf("!"));
      try (var jarFile = new JarFile(URLDecoder.decode(substring, StandardCharsets.UTF_8))) {
        return jarFile.stream().map(JarEntry::getName)
            .filter(name -> name.startsWith(SQL_PATH) && name.endsWith(SQL_EXT))
            .map(name -> name.replace(SQL_PATH, ""))
            .toList();
      }
    } else {
      try (var files = Files.list(Paths.get(resource.toURI()))) {
        return files.filter(file -> file.toString().endsWith(SQL_EXT))
            .map(Path::toFile)
            .map(File::getName)
            .toList();
      }
    }
  }

  @SneakyThrows
  private String readSqlResource(String name) {
    var stream = this.getClass().getClassLoader().getResourceAsStream(SQL_PATH + name);
    Objects.requireNonNull(stream, "Resource not found: " + name);
    try (var reader = new BufferedReader(new InputStreamReader(stream))) {
      return reader.lines().collect(Collectors.joining("\n"));
    }
  }
}
