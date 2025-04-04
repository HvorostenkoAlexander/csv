package com.mm.anonymisation.service.generator;

import java.security.SecureRandom;

public class BooleanFieldGenerator implements FieldGenerator {

  private final SecureRandom random = new SecureRandom();

  @Override
  public String generate() {
    return random.nextBoolean() ? "1" : "0";
  }
}
