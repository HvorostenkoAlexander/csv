package com.mm.anonymisation.service.generator;

import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

class DateShiftFieldGeneratorTest {

  @RepeatedTest(value = 30)
  void shouldGenerateValidFieldValue() {
    var params = Map.<String, Object>of("min", -1000, "max", -100);
    var generator = new DateShiftFieldGenerator(params);
    var generated = generator.generate();
    Assertions.assertTrue(LocalDate.now().isAfter(LocalDate.parse(generated)));
  }
}