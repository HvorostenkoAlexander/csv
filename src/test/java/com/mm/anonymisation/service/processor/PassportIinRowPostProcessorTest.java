package com.mm.anonymisation.service.processor;

import com.mm.anonymisation.model.Column;
import com.mm.anonymisation.model.Table;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PassportIinRowPostProcessorTest {

  @ParameterizedTest
  @MethodSource("rows")
  void shouldGenerateValidFieldValue(String rowValue, String expected) {
    var processor = new PassportIinRowPostProcessor();
    var row = rowValue.split(",");
    var columns = List.of(
        new Column("birthday", 1),
        new Column("sex", 12),
        new Column("passport_iin", 30)
    );
    var table = new Table("", false, Collections.emptyList(), Collections.emptyList(), columns);
    processor.processRow(row, table);
    Assertions.assertTrue(row[30].startsWith(expected));
  }

  static Stream<Arguments> rows() {
    return Stream.of(
        Arguments.arguments(
            "2657493,2003-07-24,,0,fname,fullname,false,lname,1,passportdata,2019-09-12,patronymic,0,,,0,KZ,,MVD,,0,2,0,2,,,,,300000,6,2029-09-11,,-",
            "0307245"
        ),
        Arguments.arguments(
            "2657470,1987-03-02,,0,fname,fullname,false,lname,0,passportdata,2022-04-14,patronymic,1,,,0,KZ,,MVD,,0,2,0,2,,,,,600000,6,2032-04-13,,-",
            "8703024"
        )
    );
  }
}