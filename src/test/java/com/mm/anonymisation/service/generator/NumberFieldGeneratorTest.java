package com.mm.anonymisation.service.generator;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class NumberFieldGeneratorTest {

  @RepeatedTest(30)
  void shouldGenerateValidFieldValue() {
    var min = 1000_0000_0000_0000L;
    var max = 9999_9999_9999_9999L;
    var params = Map.<String, Object>of("min", min, "max", max);
    var generator = new NumberFieldGenerator(params);
    var generated = generator.generate();
    var i = Long.valueOf(generated);
    Assertions.assertTrue(i.compareTo(min) >= 0);
    Assertions.assertTrue(i.compareTo(max) <= 0);
  }

  @Test
  void shouldExpectedThrowsWhenIncorrectParams() {
    var params = Map.<String, Object>of(
        "min", 1, "max", 0
    );
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> new NumberFieldGenerator(params)
    );
  }
}
