package com.mm.anonymisation.service.generator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

class UrlFieldGeneratorTest {

  @RepeatedTest(30)
  void shouldGenerateValidFieldValue() {
    var generator = new UrlFieldGenerator();
    var generated = generator.generate();
    Assertions.assertTrue(generated.startsWith("http://"));
    Assertions.assertTrue(generated.endsWith(".idftest.kz"));
  }
}
