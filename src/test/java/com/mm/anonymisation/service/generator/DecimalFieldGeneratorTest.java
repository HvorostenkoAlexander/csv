package com.mm.anonymisation.service.generator;

import java.util.Map;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class DecimalFieldGeneratorTest {

  Pattern pattern = Pattern.compile("\\d{1,3}\\.\\d{1,2}");

  @RepeatedTest(30)
  void shouldGenerateValidFieldValue() {
    var precision = 5;
    var scale = 2;
    var params = Map.<String, Object>of(
        "precision", precision,
        "scale", scale
    );
    var generator = new DecimalFieldGenerator(params);
    var generated = generator.generate();
    Assertions.assertTrue(pattern.matcher(generated).matches());
  }

  @Test
  void shouldExpectedThrowsWhenIncorrectParams() {
    var params = Map.<String, Object>of(
        "precision", 2,
        "scale", 2
    );
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> new DecimalFieldGenerator(params)
    );
  }
}
