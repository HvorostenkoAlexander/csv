package com.mm.anonymisation.service.generator;

import com.mm.anonymisation.model.CharSet;
import java.util.List;
import java.util.Map;

public class NameFieldGenerator implements FieldGenerator {

  private final FieldGenerator delegate;

  public NameFieldGenerator() {
    delegate = new StringFieldGenerator(Map.of("charSet", List.of(CharSet.RUSSIAN.name())));
  }

  public NameFieldGenerator(Map<String, Object> params) {
    delegate = new StringFieldGenerator(params);
  }

  @Override
  public String generate() {
    var generated = delegate.generate();
    return generated.substring(0, 1).toUpperCase() + generated.substring(1).toLowerCase();
  }
}
