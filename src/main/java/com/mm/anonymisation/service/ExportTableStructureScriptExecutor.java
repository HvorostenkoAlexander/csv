package com.mm.anonymisation.service;

import com.mm.anonymisation.model.FileType;
import com.mm.anonymisation.model.Table;
import com.mm.anonymisation.repository.SchemaRepository;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class ExportTableStructureScriptExecutor implements ScriptExecutor {

  private final SchemaRepository schemaRepository;

  @Override
  public void execute(List<Table> tables, String path) {
    for (var tableName : schemaRepository.tables()) {
      generateStructure(tableName, path);
    }
  }

  @SneakyThrows
  private void generateStructure(String tableName, String path) {
    var file = new File(ScriptGenerator.exportFileName(path, tableName, FileType.SQL));
    try (var fw = new FileWriter(file)) {
      fw.write(schemaRepository.structure(tableName));
    }
  }
}
