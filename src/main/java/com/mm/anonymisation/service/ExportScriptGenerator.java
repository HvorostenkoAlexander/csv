package com.mm.anonymisation.service;

import com.mm.anonymisation.model.Column;
import com.mm.anonymisation.model.Field;
import com.mm.anonymisation.model.FileType;
import com.mm.anonymisation.model.Table;
import com.mm.anonymisation.repository.SchemaRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExportScriptGenerator implements ScriptGenerator {

  private final SchemaRepository schemaRepository;

  @Override
  public List<String> generate(List<Table> tables, String path) {
    var excludeTables = tables.stream()
        .filter(Table::exclude)
        .map(Table::name)
        .toList();
    List<String> dbTables = schemaRepository.tables();
    return dbTables
        .stream()
        .filter(tableName -> !excludeTables.contains(tableName))
        .map(tableName -> build(tables, tableName, path))
        .toList();
  }

  @SuppressWarnings({"java:S5665", "java:S3457"})
  private String build(List<Table> tables, String tableName, String path) {
    var fields = tables.stream()
        .filter(table -> table.name().equals(tableName))
        .findFirst()
        .map(Table::fields)
        .orElse(Collections.emptyList());
    var base64Columns = fields.stream()
        .filter(Field::base64)
        .map(Field::name)
        .toList();
    var skipColumns = fields.stream()
        .map(Field::name)
        .toList();
    var columns = new ArrayList<>(schemaRepository.columns(tableName).stream().map(Column::name).toList());
    for (int i = 0; i < columns.size(); i++) {
      if (base64Columns.contains(columns.get(i))) {
        columns.set(i, "REPLACE(TO_BASE64(`%s`), '\\n', '')".formatted(columns.get(i)));
      } else if (skipColumns.contains(columns.get(i))) {
        columns.set(i, "null");
      } else {
        columns.set(i, "`%s`".formatted(columns.get(i)));
      }
    }
    return """
        SELECT %s
        INTO OUTFILE '%s'
          FIELDS TERMINATED BY ',' ESCAPED BY '\\\\' ENCLOSED BY '"'
          LINES TERMINATED BY '\\n'
        FROM `%s`;
        """.formatted(String.join(",", columns), ScriptGenerator.exportFileName(path, tableName, FileType.CSV), tableName);
  }
}
