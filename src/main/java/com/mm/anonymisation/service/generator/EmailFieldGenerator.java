package com.mm.anonymisation.service.generator;

import com.mm.anonymisation.model.CharSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EmailFieldGenerator implements FieldGenerator {

  private final String domain;
  private final FieldGenerator delegate;

  public EmailFieldGenerator(Map<String, Object> params) {
    domain = Optional.ofNullable(params.get("domain"))
        .map(String.class::cast)
        .orElse("prod-test.com");
    var paramsWithCharSet = new HashMap<>(params);
    paramsWithCharSet.putIfAbsent(
        "charSet",
        List.of(
            CharSet.LATIN.name()
        )
    );
    delegate = new StringFieldGenerator(paramsWithCharSet);
  }

  @Override
  public String generate() {
    return "%s@%s".formatted(delegate.generate(), domain);
  }
}
