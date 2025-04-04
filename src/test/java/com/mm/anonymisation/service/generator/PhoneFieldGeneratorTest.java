package com.mm.anonymisation.service.generator;

import java.util.regex.Pattern;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

class PhoneFieldGeneratorTest {

  private final Pattern pattern = Pattern.compile("\\+\\d{11}");
  private final FieldGenerator generator = new PhoneFieldGenerator();

  @RepeatedTest(30)
  void shouldGenerateValidFieldValue() {
    var generated = generator.generate();
    Assertions.assertTrue(pattern.matcher(generated).matches());
  }
}