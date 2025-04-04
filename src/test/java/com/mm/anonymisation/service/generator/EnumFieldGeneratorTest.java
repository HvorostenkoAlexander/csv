package com.mm.anonymisation.service.generator;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

class EnumFieldGeneratorTest {

  private final List<String> values = List.of("VAL1", "VAL2");
  private final FieldGenerator generator = new EnumFieldGenerator(
      Map.of(
          "values", values
      )
  );

  @RepeatedTest(30)
  void shouldGenerateValidFieldValue() {
    var result = generator.generate();
    Assertions.assertTrue(values.contains(result));
  }
}
