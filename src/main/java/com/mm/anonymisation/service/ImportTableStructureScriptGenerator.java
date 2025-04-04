package com.mm.anonymisation.service;

import com.mm.anonymisation.model.FileType;
import com.mm.anonymisation.model.Table;
import com.mm.anonymisation.repository.SchemaRepository;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class ImportTableStructureScriptGenerator implements ScriptGenerator {

  private final SchemaRepository schemaRepository;

  @Override
  public List<String> generate(List<Table> tables, String path) {
    return schemaRepository.tables()
        .stream()
        .map(tableName -> generateScripts(tableName, path))
        .flatMap(List::stream)
        .toList();
  }

  @SneakyThrows
  private List<String> generateScripts(String tableName, String path) {
    var file = new File(ScriptGenerator.exportFileName(path, tableName, FileType.SQL));
    if (!file.exists()) {
      return List.of();
    }
    return List.of(
        "DROP TABLE IF EXISTS `%s`".formatted(tableName),
        Files.readString(file.toPath())
    );
  }
}
