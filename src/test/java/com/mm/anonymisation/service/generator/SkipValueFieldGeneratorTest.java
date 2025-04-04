package com.mm.anonymisation.service.generator;

import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SkipValueFieldGeneratorTest {

  @Test
  void shouldGenerateValidFieldValue() {
    var val = "val";
    var generated = new SkipValueFieldGenerator(Map.of("value", val)).generate();
    Assertions.assertEquals(val, generated);
  }

  @Test
  void shouldThrowExceptionWhenParamsIsNull() {
    Assertions.assertThrows(NullPointerException.class, () -> new SkipValueFieldGenerator(null));
  }

  @Test
  void shouldThrowExceptionWhenValueIsMissing() {
    Assertions.assertThrows(NullPointerException.class, () -> new SkipValueFieldGenerator(Collections.emptyMap()));
  }
}
