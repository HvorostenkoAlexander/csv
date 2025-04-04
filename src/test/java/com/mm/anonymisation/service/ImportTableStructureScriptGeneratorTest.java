package com.mm.anonymisation.service;

import com.mm.anonymisation.repository.SchemaRepository;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ImportTableStructureScriptGeneratorTest {

  @Test
  void shouldGenerateStructureScriptsWhenTableExists() {
    var schemaRepository = Mockito.mock(SchemaRepository.class);
    Mockito.when(schemaRepository.tables()).thenReturn(List.of("credit"));
    var generator = new ImportTableStructureScriptGenerator(schemaRepository);
    var path = getClass().getClassLoader().getResource("tables-config.yaml").getPath();
    var scripts = generator.generate(
        List.of(), Path.of(path).getParent().toString()
    );
    Assertions.assertEquals(2, scripts.size());
    Assertions.assertEquals("DROP TABLE IF EXISTS `credit`", scripts.get(0));
  }
}
