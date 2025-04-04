package com.mm.anonymisation.service.generator;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class DateFieldGenerator implements FieldGenerator {

  private final SecureRandom random = new SecureRandom();
  private final LocalDate min;
  private final LocalDate max;

  public DateFieldGenerator(Map<String, Object> params) {
    min = Optional.ofNullable(params.get("min"))
        .map(String.class::cast)
        .map(LocalDate::parse)
        .orElse(LocalDate.of(1980, Month.JANUARY, 1));
    var maxParam = (String) params.get("max");
    Objects.requireNonNull(maxParam, "max parameter is null!");
    max = LocalDate.parse(maxParam);
    if (min.isAfter(max)) {
      throw new IllegalArgumentException(this + ". Minimum value cannot be greater than maximum value!");
    }
  }

  @Override
  public String generate() {
    long randomDay = random.nextLong(min.toEpochDay(), max.toEpochDay());
    return DateTimeFormatter.ISO_DATE.format(LocalDate.ofEpochDay(randomDay));
  }
}
