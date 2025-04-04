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

class FullNameRowPostProcessorTest {

  @ParameterizedTest
  @MethodSource("rows")
  void shouldGenerateValidFieldValue(String rowValue, String expectedValue) {
    var processor = new FullNameRowPostProcessor();
    var row = rowValue.split(",");
    var columns = List.of(
        new Column("first_name", 4),
        new Column("last_name", 7),
        new Column("patronymic", 11),
        new Column("full_name", 5)
    );
    var table = new Table("", false, Collections.emptyList(), Collections.emptyList(), columns);
    processor.processRow(row, table);
    Assertions.assertEquals(expectedValue, row[5]);
  }

  static Stream<Arguments> rows() {
    return Stream.of(
        Arguments.arguments(",,,,first_name,,,last_name,,,,patronymic", "first_name patronymic last_name"),
        Arguments.arguments(",,,,first_name,,,last_name,,,,,-", "first_name last_name")
    );
  }
}