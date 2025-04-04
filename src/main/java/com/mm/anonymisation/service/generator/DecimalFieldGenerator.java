package com.mm.anonymisation.service.generator;

import java.util.Map;
import java.util.Optional;

public class DecimalFieldGenerator implements FieldGenerator {

  private final FieldGenerator precisionDelegate;
  private final FieldGenerator scaleDelegate;

  public DecimalFieldGenerator(Map<String, Object> params) {
    int precision = Optional.ofNullable(params.get("precision"))
        .map(Number.class::cast)
        .map(Number::intValue)
        .orElse(10);
    int scale = Optional.ofNullable(params.get("scale"))
        .map(Number.class::cast)
        .map(Number::intValue)
        .orElse(2);
    if (precision <= scale) {
      throw new IllegalArgumentException("Precision must be greater than or equal to scale");
    }
    int diff = precision - scale;
    precisionDelegate = new NumberFieldGenerator(
        Map.of(
            "min", 0,
            "max", Math.pow(10, diff)
        )
    );
    scaleDelegate = new NumberFieldGenerator(
        Map.of(
            "min", 0,
            "max", Math.pow(10, scale)
        )
    );
  }

  @Override
  public String generate() {
    return "%s.%s".formatted(precisionDelegate.generate(), scaleDelegate.generate());
  }
}
