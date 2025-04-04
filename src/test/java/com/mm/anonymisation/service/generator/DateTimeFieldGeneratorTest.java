package com.mm.anonymisation.service.generator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class DateTimeFieldGeneratorTest {

  @RepeatedTest(30)
  void shouldGenerateValidFieldValue() {
    var min = LocalDateTime.of(LocalDate.of(2020, 1, 1), LocalTime.MIN);
    var max = LocalDateTime.of(LocalDate.of(2020, 2, 1), LocalTime.MAX);
    var params = Map.<String, Object>of(
        "min", DateTimeFormatter.ISO_DATE_TIME.format(min),
        "max", DateTimeFormatter.ISO_DATE_TIME.format(max)
    );
    var generator = new DateTimeFieldGenerator(params);
    var generatedDate = LocalDateTime.parse(generator.generate());

    Assertions.assertTrue(generatedDate.isAfter(min.minusDays(1)));
    Assertions.assertTrue(generatedDate.isBefore(max));
  }

  @Test
  void shouldExpectedThrowsWhenEmptyParams() {
    var params = Collections.<String, Object>emptyMap();
    Assertions.assertThrows(
        NullPointerException.class,
        () -> new DateTimeFieldGenerator(params)
    );
  }

  @Test
  void shouldExpectedThrowsWhenIncorrectParams() {
    var params = Map.<String, Object>of(
        "min", "2020-01-02T01:01:01",
        "max", "2020-01-01T01:01:01"
    );
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> new DateTimeFieldGenerator(params)
    );
  }
}