package com.mm.anonymisation.service.generator;

import java.util.Map;
import java.util.Objects;

public class SkipValueFieldGenerator implements FieldGenerator {

  private final String value;

  public SkipValueFieldGenerator(Map<String, Object> params) {
    Objects.requireNonNull(params.get("value"), "value is required!");
    value = String.valueOf(params.get("value"));
  }

  @Override
  public String generate() {
    return value;
  }
}
