package com.mm.anonymisation.service.generator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class DateShiftFieldGenerator implements FieldGenerator {

  private final FieldGenerator delegate;

  public DateShiftFieldGenerator(Map<String, Object> params) {
    delegate = new NumberFieldGenerator(params);
  }

  @Override
  public String generate() {
    var date = LocalDate.now().plusDays(Long.parseLong(delegate.generate()));
    return DateTimeFormatter.ISO_DATE.format(date);
  }
}
