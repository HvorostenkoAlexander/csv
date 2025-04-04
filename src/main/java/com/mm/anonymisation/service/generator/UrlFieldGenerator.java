package com.mm.anonymisation.service.generator;

import com.mm.anonymisation.model.CharSet;
import java.util.List;
import java.util.Map;

public class UrlFieldGenerator implements FieldGenerator {

  private final FieldGenerator delegate;

  public UrlFieldGenerator() {
    delegate = new StringFieldGenerator(
        Map.of(
            "charSet", List.of(CharSet.LATIN.name()),
            "min", 5,
            "max", 20
        )
    );
  }

  public UrlFieldGenerator(Map<String, Object> params) {
    delegate = new StringFieldGenerator(params);
  }

  @Override
  public String generate() {
    return "http://%s.idftest.kz".formatted(delegate.generate());
  }
}
