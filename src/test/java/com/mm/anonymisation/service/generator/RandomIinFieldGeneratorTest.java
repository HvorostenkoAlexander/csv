package com.mm.anonymisation.service.generator;

import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

class RandomIinFieldGeneratorTest {

  @RepeatedTest(30)
  void shouldGenerateValidFieldValue() {
    var params = Map.<String, Object>of("min", "1980-01-01", "max", "2000-01-01");
    var generator = new RandomIinFieldGenerator(params);
    var generated = generator.generate();
    Assertions.assertEquals(12, generated.length());
  }
}