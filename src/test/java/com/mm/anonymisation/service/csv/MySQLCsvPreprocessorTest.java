package com.mm.anonymisation.service.csv;

import java.io.IOException;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MySQLCsvPreprocessorTest {

  private final MySQLCsvPreprocessor csvCorrector = new MySQLCsvPreprocessor();

  static Stream<Arguments> arguments() {
    return Stream.of(
        Arguments.of(
            "\"1\",\"^A\",\"2018-03-01 00:00:00\",\"2028-03-31 00:00:00\",\"cpa_test1\",\"test1\",\"BASE\",\"1440\",\"1\",\"^@\",\\N,\\N,\"LEAD_GENERATOR\",\"0\",\\N,\\N",
            "\"1\",\"^A\",\"2018-03-01 00:00:00\",\"2028-03-31 00:00:00\",\"cpa_test1\",\"test1\",\"BASE\",\"1440\",\"1\",\"^@\",\"\u0004\",\"\u0004\",\"LEAD_GENERATOR\",\"0\",\"\u0004\",\"\u0004\""
        ),
        Arguments.of("\"1\",\\N,\\N,\"abc\",\\N", "\"1\",\"\u0004\",\"\u0004\",\"abc\",\"\u0004\""),
        Arguments.of("\"1\",\\N,\\N,\"abc\"", "\"1\",\"\u0004\",\"\u0004\",\"abc\""),
        Arguments.of("\\N,\\N,\"abc\"", "\"\u0004\",\"\u0004\",\"abc\""),
        Arguments.of("\\N", "\"\u0004\""),
        Arguments.of(null, null),
        Arguments.of("", "")
    );
  }

  @ParameterizedTest
  @MethodSource("arguments")
  void shouldSucceedReplaceMySQLNullValues(String source, String expected) throws IOException {
    var correct = csvCorrector.process(source);
    Assertions.assertEquals(expected, correct);
  }
}