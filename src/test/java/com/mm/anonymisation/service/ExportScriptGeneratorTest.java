package com.mm.anonymisation.service;

import com.mm.anonymisation.model.Column;
import com.mm.anonymisation.model.Field;
import com.mm.anonymisation.model.Table;
import com.mm.anonymisation.repository.SchemaRepository;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ExportScriptGeneratorTest {

  @Test
  void shouldGenerateValidExportScript() {
    var repository = Mockito.mock(SchemaRepository.class);
    Mockito.when(repository.tables()).thenReturn(List.of("credit_calculations", "personal_data"));
    Mockito.when(repository.columns("personal_data")).thenReturn(
        List.of(
            new Column("id", 0),
            new Column("birthday", 1),
            new Column("first_name", 2),
            new Column("last_name", 3),
            new Column("patronymic", 4),
            new Column("other", 5)
        )
    );
    var generator = new ExportScriptGenerator(repository);
    var creditCalculations = new Table(
        "credit_calculations",
        true,
        Collections.emptyList(),
        Collections.emptyList(),
        Collections.emptyList()
    );
    var fields = List.of(
        new Field("first_name", 2, false, false, null),
        new Field("last_name", 3, true, false, null),
        new Field("patronymic", 4, true, true, null)
    );
    var personalData = new Table("personal_data", false, Collections.emptyList(), fields, Collections.emptyList());

    var generated = generator.generate(List.of(creditCalculations, personalData), "/path/");
    Assertions.assertEquals(1, generated.size());
    var personalDataTable = """
        SELECT `id`,`birthday`,null,null,REPLACE(TO_BASE64(`patronymic`), '\\n', ''),`other`
        INTO OUTFILE '/path/export__personal_data.csv'
          FIELDS TERMINATED BY ',' ESCAPED BY '\\\\' ENCLOSED BY '"'
          LINES TERMINATED BY '\\n'
        FROM `personal_data`;
        """;
    Assertions.assertTrue(generated.get(0).contains(personalDataTable));
  }
}
