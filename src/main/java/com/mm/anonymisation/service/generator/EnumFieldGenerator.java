package com.mm.anonymisation.service.generator;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

public class EnumFieldGenerator implements FieldGenerator {

  private final SecureRandom random = new SecureRandom();
  private final List<String> values;

  public EnumFieldGenerator(Map<String, Object> params) {
    @SuppressWarnings("unchecked") var valuesParam = (List<String>) params.get("values");
    if (null == valuesParam || valuesParam.isEmpty()) {
      throw new IllegalArgumentException(this + ". Values cannot be empty!");
    }
    values = valuesParam;
  }

  @Override
  public String generate() {
    int index = random.nextInt(values.size() - 1);
    return values.get(index);
  }
}
