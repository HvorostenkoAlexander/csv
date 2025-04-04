package com.mm.anonymisation.service.generator;

import java.security.SecureRandom;
import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NumberFieldGenerator implements FieldGenerator {

  private final SecureRandom random = new SecureRandom();
  private long min = 0;
  private long max = 255L;

  public NumberFieldGenerator(Map<String, Object> params) {
    var minParam = (Number) params.get("min");
    if (null != minParam) {
      min = minParam.longValue();
    }
    var maxParam = (Number) params.get("max");
    if (null != maxParam) {
      max = maxParam.longValue();
    }
    if (min > max) {
      throw new IllegalArgumentException(this + ". Minimum value cannot be greater than maximum value!");
    }
  }

  @Override
  public String generate() {
    return String.valueOf(random.nextLong(min, max));
  }
}
