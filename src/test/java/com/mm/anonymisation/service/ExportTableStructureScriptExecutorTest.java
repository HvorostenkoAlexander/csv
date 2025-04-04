package com.mm.anonymisation.service;

import com.mm.anonymisation.repository.SchemaRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ExportTableStructureScriptExecutorTest {

  @Test
  void shouldGenerateCorrectTableStructureScripts() throws IOException {
    var schemaRepository = Mockito.mock(SchemaRepository.class);
    var executor = new ExportTableStructureScriptExecutor(schemaRepository);
    Mockito.when(schemaRepository.tables()).thenReturn(List.of("table1", "table2"));
    Mockito.when(schemaRepository.structure("table1")).thenReturn("create table table1");
    Mockito.when(schemaRepository.structure("table2")).thenReturn("create table table2");

    var tempDir = Files.createTempDirectory("export-table-structure");
    executor.execute(List.of(), tempDir.toAbsolutePath().toString());

    var path1 = tempDir.resolve("export__table1.sql").toAbsolutePath();
    var table1Structure = Files.readString(path1);
    Assertions.assertEquals("create table table1", table1Structure);
    Files.delete(path1);

    var path2 = tempDir.resolve("export__table2.sql").toAbsolutePath();
    var table2Structure = Files.readString(path2);
    Assertions.assertEquals("create table table2", table2Structure);
    Files.delete(path2);

    Files.delete(tempDir);
  }
}
