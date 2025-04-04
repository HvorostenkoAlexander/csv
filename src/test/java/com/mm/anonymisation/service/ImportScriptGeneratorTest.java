package com.mm.anonymisation.service;

import com.mm.anonymisation.model.Column;
import com.mm.anonymisation.model.Field;
import com.mm.anonymisation.model.FileType;
import com.mm.anonymisation.model.Table;
import com.mm.anonymisation.repository.SchemaRepository;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ImportScriptGeneratorTest {

  @Test
  void shouldGenerateImportScript() {
    var repository = Mockito.mock(SchemaRepository.class);
    Mockito.when(repository.tables()).thenReturn(List.of("credit_calculations", "credit", "personal_data_2"));
    Mockito.when(repository.columns("credit"))
        .thenReturn(
            List.of(
                new Column("id", 1),
                new Column("date", 2)
            )
        );
    Mockito.when(repository.columns("personal_data_2"))
        .thenReturn(
            List.of(
                new Column("first_name", 1),
                new Column("last_name", 2),
                new Column("patronymic", 3)
            )
        );
    var generator = new ImportScriptGenerator(repository);
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
    var personalData = new Table(
        "personal_data_2",
        false,
        Collections.emptyList(),
        fields,
        Collections.emptyList()
    );
    var path = Path.of(
        getClass().getClassLoader().getResource("tables-config.yaml").getPath()
    ).getParent().toString();
    var generated = generator.generate(List.of(creditCalculations, personalData), path);
    Assertions.assertEquals(4, generated.size());

    Assertions.assertTrue(generated.contains("TRUNCATE TABLE `credit`"));
    Assertions.assertTrue(generated.contains(
        """
            LOAD DATA CONCURRENT INFILE '%s'
            INTO TABLE `credit`
            FIELDS TERMINATED BY ',' ESCAPED BY '\\\\' ENCLOSED BY '"'
            LINES TERMINATED BY '\\n'
            (`id`,`date`)
            """.formatted(ScriptGenerator.exportFileName(path, "credit", FileType.CSV))
    ));

    Assertions.assertTrue(generated.contains("TRUNCATE TABLE `personal_data_2`"));
    Assertions.assertTrue(generated.contains(
        """
            LOAD DATA CONCURRENT INFILE '%s'
            INTO TABLE `personal_data_2`
            FIELDS TERMINATED BY ',' ESCAPED BY '\\\\' ENCLOSED BY '"'
            LINES TERMINATED BY '\\n'
            (`first_name`,`last_name`,`patronymic`) SET `last_name`=FROM_BASE64(`last_name`), `patronymic`=FROM_BASE64(`patronymic`)
            """.formatted(ScriptGenerator.importFileName(path, "personal_data_2", FileType.CSV))
    ));
  }
}
