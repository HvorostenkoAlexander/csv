package com.mm.anonymisation.service;

import com.mm.anonymisation.model.Table;
import com.mm.anonymisation.repository.DefaultLoggerRepository;
import com.mm.anonymisation.repository.LoggerRepository;
import com.mm.testing.mysql.EmbeddedMySQLExtension;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

@ExtendWith(EmbeddedMySQLExtension.class)
class ImportScriptExecutorTest {

  @ParameterizedTest
  @MethodSource("importTableStructureScriptGenerator")
  void shouldSuccessfullyExecuteScriptsAndSaveErrors(String sql, Boolean isErrorExpected, DSLContext dslContext) {
    var tables = List.<Table>of();
    var path = "path";

    var importTableStructureScriptGenerator = Mockito.mock(ScriptGenerator.class);
    Mockito.when(importTableStructureScriptGenerator.generate(tables, path)).thenReturn(List.of());
    var importScriptGenerator = Mockito.mock(ScriptGenerator.class);
    Mockito.when(importScriptGenerator.generate(tables, path)).thenReturn(List.of(sql));
    var resourceScriptGenerator = Mockito.mock(ScriptGenerator.class);
    Mockito.when(resourceScriptGenerator.generate(tables, path)).thenReturn(List.of());
    LoggerRepository loggerRepository = new DefaultLoggerRepository(dslContext);

    ImportScriptExecutor importScriptExecutor = new ImportScriptExecutor(dslContext,
        importTableStructureScriptGenerator, importScriptGenerator, resourceScriptGenerator, loggerRepository);
    importScriptExecutor.execute(tables, path);

    Mockito.verify(importTableStructureScriptGenerator, Mockito.times(1)).generate(tables, path);
    Mockito.verify(importScriptGenerator, Mockito.times(1)).generate(tables, path);
    Mockito.verify(resourceScriptGenerator, Mockito.times(1)).generate(tables, path);

    Assertions.assertEquals(isErrorExpected, isRecordByQueryExist(dslContext, sql));
  }

  private boolean isRecordByQueryExist(DSLContext dslContext, String query) {
    return dslContext.selectCount().from(DSL.table("import_csv_log"))
        .where(DSL.field("executed_sql", SQLDataType.VARCHAR).eq(query))
        .fetchOne(0, Integer.class) == 1;
  }

  private static Stream<Arguments> importTableStructureScriptGenerator() {
    return Stream.of(
        Arguments.of("select %s from dual".formatted(LocalDateTime.now().getNano()), false),
        Arguments.of("select %s from unknown_table".formatted(LocalDateTime.now().getNano()), true)
    );
  }
}
