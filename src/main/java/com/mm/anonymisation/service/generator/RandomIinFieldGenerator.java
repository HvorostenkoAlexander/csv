package com.mm.anonymisation.service.generator;

import java.time.LocalDate;
import java.util.Map;

public class RandomIinFieldGenerator implements FieldGenerator {

  private final IinGenerator iinGenerator;
  private final FieldGenerator dateFieldGenerator;
  private final FieldGenerator numberFieldGenerator;

  public RandomIinFieldGenerator(Map<String, Object> params) {
    iinGenerator = new DefaultIinGenerator();
    dateFieldGenerator = new DateFieldGenerator(params);
    numberFieldGenerator = new NumberFieldGenerator(Map.of("min", 0, "max", 2));
  }

  @Override
  public String generate() {
    var birthDay = LocalDate.parse(dateFieldGenerator.generate());
    var sex = numberFieldGenerator.generate();
    return iinGenerator.generate(birthDay, sex);
  }
}
