package com.mm.anonymisation.service.generator;

import com.mm.anonymisation.model.CharSet;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

public class PhoneFieldGenerator implements FieldGenerator {

  private final SecureRandom random = new SecureRandom();
  static final List<String> CODES = List.of("700", "701", "751", "760", "771", "775", "776", "777", "778");
  private final FieldGenerator delegate;

  public PhoneFieldGenerator() {
    var params = Map.of(
        "min", 7,
        "max", 8,
        "charSet", List.of(CharSet.NUMERIC.name())
    );
    delegate = new StringFieldGenerator(params);
  }

  @Override
  public String generate() {
    return "+7%s%s".formatted(
        CODES.get(random.nextInt(CODES.size())),
        delegate.generate()
    );
  }
}
