package com.mm.anonymisation.service;

import com.mm.anonymisation.model.FileType;
import com.mm.anonymisation.model.Table;
import java.nio.file.Path;
import java.util.List;

public interface ScriptGenerator {

  static String exportFileName(String path, String tableName, FileType fileType) {
    return Path.of(path, "export__%s%s".formatted(tableName, fileType.getExtension())).toString();
  }

  static String importFileName(String path, String tableName, FileType fileType) {
    return Path.of(path, "import__%s%s".formatted(tableName, fileType.getExtension())).toString();
  }

  List<String> generate(List<Table> tables, String path);
}
