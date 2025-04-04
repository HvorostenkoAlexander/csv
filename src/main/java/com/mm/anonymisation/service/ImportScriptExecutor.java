package com.mm.anonymisation.service;

import com.mm.anonymisation.model.Table;
import com.mm.anonymisation.repository.LoggerRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jooq.DSLContext;

@Log4j2
@RequiredArgsConstructor
public class ImportScriptExecutor implements ScriptExecutor {

  private final DSLContext dslContext;
  private final ScriptGenerator importTableStructureScriptGenerator;
  private final ScriptGenerator importScriptGenerator;
  private final ScriptGenerator resourceScriptGenerator;
  private final LoggerRepository loggerRepository;

  @Override
  public void execute(List<Table> tables, String path) {
    loggerRepository.init();
    Stream.of(
            importTableStructureScriptGenerator.generate(tables, path),
            importScriptGenerator.generate(tables, path),
            resourceScriptGenerator.generate(tables, path)
        )
        .flatMap(List::stream)
        .forEach(this::execute);
    log.info("finished import");
  }

  private List<String> sqlBefore() {
    return List.of(
        "SET sql_mode='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION,ALLOW_INVALID_DATES'",
        "SET unique_checks=0",
        "SET foreign_key_checks=0"
    );
  }

  private List<String> sqlAfter() {
    return List.of(
        "SET foreign_key_checks=1",
        "SET unique_checks=1",
        "SET sql_mode='TRADITIONAL'"
    );
  }

  private void execute(String sql) {
    var now = LocalDateTime.now();
    try {
      log.info(sql);
      dslContext.transaction(conf -> {
        var dsl = conf.dsl();
        sqlBefore().forEach(dsl::execute);
        dslContext.execute(sql);
        sqlAfter().forEach(dsl::execute);
      });
    } catch (Exception e) {
      log.error(e);
      loggerRepository.store(now, sql, e.toString());
    }
  }
}
