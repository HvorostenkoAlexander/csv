package com.mm.anonymisation.service.generator;

import com.mm.anonymisation.model.CharSet;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class StringFieldGeneratorTest {

  @RepeatedTest(30)
  void shouldGenerateValidFieldValue() {
    var usedCharSet = CharSet.RUSSIAN;
    var min = 5;
    var max = 10;
    var params = Map.of(
        "min", min,
        "max", max,
        "charSet", List.of(usedCharSet.name())
    );
    var generator = new StringFieldGenerator(params);
    var generated = generator.generate().split("");
    Assertions.assertTrue(generated.length >= min && generated.length <= max);

    var expected = Arrays.asList(usedCharSet.getChars().split(""));
    long count = Arrays.stream(generated)
        .filter(s -> !expected.contains(s))
        .count();
    Assertions.assertEquals(0, count);
  }

  @Test
  void shouldExpectedThrowsWhenIncorrectParams() {
    var params = Map.<String, Object>of("min", 10, "max", 1);
    Assertions.assertThrows(
        IllegalArgumentException.class,
        () -> new StringFieldGenerator(params)
    );
  }
}