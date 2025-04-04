package com.mm.anonymisation.service.generator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class DateFieldGeneratorTest {

  @RepeatedTest(30)
  void shouldGenerateValidFieldValue() {
    var min = LocalDate.of(2020, 1, 1);
    var max = LocalDate.of(2020, 2, 1);
    var params = Map.<String, Object>of(
        "min", DateTimeFormatter.ISO_DATE.format(min),
        "max", DateTimeFormatter.ISO_DATE.format(max)
    );
    var generator = new DateFieldGenerator(params);
    var generatedDate = LocalDate.parse(generator.generate());

    Assertions.assertTrue(generatedDate.isAfter(min.minusDays(1)));
    Assertions.assertTrue(generatedDate.isBefore(max));
  }

  @Test
  void shouldExpectedThrowsWhenEmptyParams() {
    var params = Collections.<String, Object>emptyMap();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> new DateFieldGenerator(params)
    );
  }

  @Test
  void shouldExpectedThrowsWhenIncorrectParams() {
    var params = Map.<String, Object>of(
        "min", "2020-01-02",
        "max", "2020-01-01"
    );
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> new DateFieldGenerator(params)
    );
  }
}