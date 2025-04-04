package com.mm.anonymisation.service;

import com.mm.anonymisation.model.Table;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import org.jooq.DSLContext;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ExportScriptExecutorTest {

  private static Path csvPath;
  private static List<Path> csvFiles;

  @BeforeAll
  static void generateTempFiles() throws IOException {
    csvPath = Files.createTempDirectory("csv");
    csvFiles = List.of(
        Files.createTempFile(csvPath, "export__t1", ".csv"),
        Files.createTempFile(csvPath, "import__t2", ".csv"),
        Files.createTempFile(csvPath, "other", ".csv")
    );
  }

  @AfterAll
  static void clearTempFiles() throws IOException {
    for (var file : csvFiles) {
      Files.deleteIfExists(file);
    }
    Files.delete(csvPath);
  }

  @Test
  void shouldSuccessRemoveOldFilesAndGenerateNewFiles() throws IOException {
    var dslContext = Mockito.mock(DSLContext.class);
    var scriptGenerator = Mockito.mock(ScriptGenerator.class);
    var scripts = List.of("script1", "script2");
    var tables = Collections.<Table>emptyList();
    Mockito.when(scriptGenerator.generate(tables, csvPath.toString())).thenReturn(scripts);
    var scriptExecutor = new ExportScriptExecutor(dslContext, Mockito.spy(ScriptExecutor.class), scriptGenerator);
    scriptExecutor.execute(tables, csvPath.toString());
    Mockito.verify(scriptGenerator, Mockito.times(1)).generate(tables, csvPath.toString());
    Mockito.verify(dslContext, Mockito.times(1)).execute(scripts.get(0));
    Mockito.verify(dslContext, Mockito.times(1)).execute(scripts.get(1));

    Assertions.assertEquals(1, Files.list(csvPath).count());
  }
}
