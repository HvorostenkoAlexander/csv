package com.mm.anonymisation.service;

import com.mm.anonymisation.model.Column;
import com.mm.anonymisation.model.Field;
import com.mm.anonymisation.model.Table;
import com.mm.anonymisation.service.generator.FieldGenerator;
import com.mm.anonymisation.service.processor.FullNameRowPostProcessor;
import com.mm.anonymisation.service.processor.RowPostProcessor;
import com.mm.base.service.crypt.CipherEngine;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

class CsvRowProcessorTest {

  @ParameterizedTest
  @MethodSource("data")
  void allOperationsMustExecutedInCorrectOrder(String data) throws Exception {
    var generated = "generated";
    var encrypted = "encrypted";
    var cipherEngine = Mockito.mock(CipherEngine.class);
    Mockito.when(cipherEngine.encrypt(Mockito.anyString())).thenReturn(encrypted);
    var fieldGenerator = Mockito.mock(FieldGenerator.class);
    Mockito.when(fieldGenerator.generate()).thenReturn(generated);
    var fields = List.of(
        new Field("first_name", 4, true, false, fieldGenerator),
        new Field("last_name", 7, false, false, fieldGenerator),
        new Field("passport_data", 9, true, false, null),
        new Field("patronymic", 11, false, false, fieldGenerator)
    );
    var postProcessors = List.<RowPostProcessor>of(
        new FullNameRowPostProcessor()
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
    var personalData = new Table("personal_data", false, postProcessors, fields, columns);
    var row = data.split(",");
    new CsvRowProcessor(cipherEngine).processRow(row, personalData);

    Assertions.assertEquals(encrypted, new String(Base64.getDecoder().decode(row[4])));
    Assertions.assertEquals(generated, row[7]);
    Assertions.assertEquals(generated, row[11]);
    Assertions.assertEquals("%s %s %s".formatted(generated, generated, generated), row[5]);
  }

  static Stream<Arguments> data() {
    return Stream.of(
        Arguments.arguments(
            "1001163,,,1,,,,,0,,2013-04-10,,0,,,0,KZ,,MVD,,0,2,0,2,,,,,438635,6,2028-04-10,,,,IDENTITY_CARD,KZ,5874"),
        Arguments.arguments(
            "1001164,,,1,,,,,0,,2013-04-10,,0,,,0,KZ,,MVD,,0,2,0,2,,,,,716058,6,2028-04-10,,,,IDENTITY_CARD,KZ,8183"),
        Arguments.arguments(
            "1001165,,,1,,,,,0,,2013-04-10,,0,,,0,KZ,,MVD,,0,2,0,2,,,,,840181,6,2028-04-10,,,,IDENTITY_CARD,KZ,5943")
    );
  }
}