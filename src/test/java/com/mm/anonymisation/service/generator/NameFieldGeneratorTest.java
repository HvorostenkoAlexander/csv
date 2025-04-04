package com.mm.anonymisation.service.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

class NameFieldGeneratorTest {

  @RepeatedTest(30)
  void shouldGenerateValidFieldValue() {
    var generator = new NameFieldGenerator();
    var generated = generator.generate();
    var firstLetter = generated.substring(0, 1);
    Assertions.assertEquals(firstLetter.toUpperCase(), firstLetter);
    var remainder = generated.substring(1);
    Assertions.assertEquals(remainder.toLowerCase(), remainder);
  }
}