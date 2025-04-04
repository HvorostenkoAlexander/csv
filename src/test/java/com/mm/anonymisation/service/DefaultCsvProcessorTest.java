package com.mm.anonymisation.service;

import com.mm.anonymisation.model.Column;
import com.mm.anonymisation.model.Field;
import com.mm.anonymisation.model.FileType;
import com.mm.anonymisation.model.Table;
import com.mm.anonymisation.service.csv.MySQLCSVParser;
import com.mm.anonymisation.service.csv.MySQLCsvPreprocessor;
import com.mm.anonymisation.service.generator.DateFieldGenerator;
import com.mm.anonymisation.service.generator.NameFieldGenerator;
import com.mm.anonymisation.service.generator.NumberFieldGenerator;
import com.mm.anonymisation.service.processor.FullNameRowPostProcessor;
import com.mm.anonymisation.service.processor.PassportIinRowPostProcessor;
import com.mm.base.service.crypt.DefaultCipherEngineAES;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DefaultCsvProcessorTest {

  @BeforeEach
  void ddd() {
    var file = new File(ScriptGenerator.exportFileName(path(), "personal_data_3", FileType.PROCESSED));
    if (file.exists()) {
      file.delete();
    }
  }

  @Test
  void allRecordsMustBeProcessed() throws IOException {
    var cipherEngine = new DefaultCipherEngineAES("vfybv'yntcnrhbgn rtq123lkzntcnjd");
    var nameFieldGenerator = new NameFieldGenerator(
        Map.of(
            "max", 10
        )
    );
    var dateFieldGenerator = new DateFieldGenerator(
        Map.of(
            "max", LocalDate.now().minusYears(20).toString()
        )
    );
    var numberFieldGenerator = new NumberFieldGenerator(
        Map.of(
            "min", 0,
            "max", 2
        )
    );
    var fields = List.of(
        new Field("birthday", 1, false, false, dateFieldGenerator),
        new Field("first_name", 4, true, false, nameFieldGenerator),
        new Field("last_name", 7, false, false, nameFieldGenerator),
        new Field("patronymic", 11, false, false, nameFieldGenerator),
        new Field("sex", 12, false, false, numberFieldGenerator)
    );
    var postProcessors = List.of(
        new FullNameRowPostProcessor(), new PassportIinRowPostProcessor()
    );
    var columns = List.of(
        new Column("birthday", 1),
        new Column("first_name", 4),
        new Column("full_name", 5),
        new Column("last_name", 7),
        new Column("patronymic", 11),
        new Column("sex", 12),
        new Column("passport_iin", 30)
    );
    var personalData = new Table("personal_data_3", false, postProcessors, fields, columns);

    var csvPreprocessor = new MySQLCsvPreprocessor();
    var csvParser = new MySQLCSVParser(csvPreprocessor);
    var csvProcessor = new DefaultCsvProcessor(cipherEngine, csvParser, 100, 1);

    var exportFile = new File(ScriptGenerator.exportFileName(path(), personalData.name(), FileType.CSV));
    long exportFileCount = linesCount(exportFile.toPath());

    csvProcessor.process(path(), List.of(personalData));

    var processedFile = new File(ScriptGenerator.exportFileName(path(), personalData.name(), FileType.PROCESSED));
    Assertions.assertTrue(processedFile.exists());
    Assertions.assertEquals(exportFileCount, linesCount(processedFile.toPath()));
  }

  private String path() {
    return Path.of(
        getClass().getClassLoader().getResource("tables-config.yaml").getPath()
    ).getParent().toString();
  }

  private long linesCount(Path path) throws IOException {
    try (var stream = Files.lines(path)) {
      return stream.count();
    }
  }
}
