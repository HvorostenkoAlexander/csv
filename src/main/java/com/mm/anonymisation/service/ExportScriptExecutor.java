package com.mm.anonymisation.service;

import com.mm.anonymisation.model.Table;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;

@Log4j2
@RequiredArgsConstructor
public class ExportScriptExecutor implements ScriptExecutor {

  private final DSLContext dslContext;
  private final ScriptExecutor exportTableStructureScriptExecutor;
  private final ScriptGenerator scriptGenerator;

  @SneakyThrows
  @Override
  public void execute(List<Table> tables, String path) {
    try (var files = Files.list(Path.of(path))) {
      for (var file : files.filter(this::isExportImport).toList()) {
        Files.delete(file);
      }
    }
    exportTableStructureScriptExecutor.execute(tables, path);
    Stream.of(
            sqlBefore(),
            scriptGenerator.generate(tables, path),
            sqlAfter()
        )
        .flatMap(List::stream)
        .forEach(dslContext::execute);
    log.info("finished export");
  }

  private boolean isExportImport(Path path) {
    var fileName = path.getFileName().toString();
    return fileName.startsWith("export__") || fileName.startsWith("import__");
  }

  private List<String> sqlBefore() {
    return List.of(
        "STOP REPLICA"
    );
  }

  private List<String> sqlAfter() {
    return List.of(
        "START REPLICA"
    );
  }
}
