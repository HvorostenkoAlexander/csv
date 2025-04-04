package com.mm.anonymisation.service.generator;

import com.mm.anonymisation.model.CharSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

class EmailFieldGeneratorTest {

  @RepeatedTest(30)
  void shouldGenerateValidFieldValue() {
    var usedCharSet = CharSet.LATIN;
    var min = 5;
    var max = 10;
    var domain = "mail.org";
    var params = Map.of(
        "min", min,
        "max", max,
        "domain", domain,
        "charSet", List.of(usedCharSet.name())
    );
    var generator = new EmailFieldGenerator(params);
    var generated = generator.generate();

    var formatted = "@%s".formatted(domain);
    Assertions.assertTrue(generated.endsWith(formatted));
    var strings = generated.replace(formatted, "").split("");
    Assertions.assertTrue(strings.length >= min && strings.length <= max);

    var expected = Arrays.asList(usedCharSet.getChars().split(""));
    long count = Arrays.stream(strings)
        .filter(s -> !expected.contains(s))
        .count();
    Assertions.assertEquals(0, count);
  }
}
