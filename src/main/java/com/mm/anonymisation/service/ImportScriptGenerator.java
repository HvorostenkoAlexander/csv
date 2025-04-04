package com.mm.anonymisation.service;

import com.mm.anonymisation.model.Column;
import com.mm.anonymisation.model.Field;
import com.mm.anonymisation.model.FileType;
import com.mm.anonymisation.model.Table;
import com.mm.anonymisation.repository.SchemaRepository;
import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ImportScriptGenerator implements ScriptGenerator {

  private final SchemaRepository schemaRepository;

  @Override
  public List<String> generate(List<Table> tables, String path) {
    var excludeTables = tables.stream()
        .filter(Table::exclude)
        .map(Table::name)
        .toList();
    var dbTables = schemaRepository.tables();
    return dbTables
        .stream()
        .filter(tableName -> !excludeTables.contains(tableName))
        .map(tableName -> build(tables, path, tableName))
        .flatMap(List::stream)
        .toList();
  }

  @SuppressWarnings({"java:S5665"})
  private List<String> build(List<Table> tables, String path, String tableName) {
    var count = tables.stream()
        .filter(table -> table.name().equals(tableName))
        .map(Table::fields)
        .flatMap(List::stream)
        .filter(field -> !field.base64())
        .count();
    var fileName = (count > 0) ? ScriptGenerator.importFileName(path, tableName, FileType.CSV)
        : ScriptGenerator.exportFileName(path, tableName, FileType.CSV);
    if (!new File(fileName).exists()) {
      return List.of();
    }
    var base64Columns = tables.stream()
        .filter(table -> table.name().equals(tableName))
        .map(Table::fields)
        .flatMap(Collection::stream)
        .filter(f -> f.base64() || f.crypt())
        .map(Field::name)
        .map(name -> "`%s`=FROM_BASE64(`%s`)".formatted(name, name))
        .collect(Collectors.joining(", "));
    var columns = schemaRepository.columns(tableName).stream()
        .map(Column::name)
        .collect(Collectors.joining("`,`"));
    return List.of(
        "TRUNCATE TABLE `%s`".formatted(tableName),
        """
            LOAD DATA CONCURRENT INFILE '%s'
            INTO TABLE `%s`
            FIELDS TERMINATED BY ',' ESCAPED BY '\\\\' ENCLOSED BY '"'
            LINES TERMINATED BY '\\n'
            (`%s`)%s
            """.formatted(
            fileName,
            tableName,
            columns,
            base64Columns.isBlank() ? "" : " SET %s".formatted(base64Columns)
        )
    );
  }
}
