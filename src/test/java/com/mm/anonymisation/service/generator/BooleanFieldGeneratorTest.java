package com.mm.anonymisation.service.generator;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

class BooleanFieldGeneratorTest {

  private final FieldGenerator generator = new BooleanFieldGenerator();

  @RepeatedTest(value = 30)
  void shouldGenerateValidFieldValue() {
    var result = generator.generate();
    Assertions.assertTrue(List.of("0", "1").contains(result));
  }
}
