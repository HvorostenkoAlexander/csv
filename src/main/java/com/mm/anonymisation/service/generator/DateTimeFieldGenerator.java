package com.mm.anonymisation.service.generator;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class DateTimeFieldGenerator implements FieldGenerator {

  private final SecureRandom random = new SecureRandom();
  private final LocalDateTime min;
  private final LocalDateTime max;

  public DateTimeFieldGenerator(Map<String, Object> params) {
    min = Optional.ofNullable(params.get("min"))
        .map(String.class::cast)
        .map(LocalDateTime::parse)
        .orElse(LocalDateTime.of(LocalDate.of(1980, Month.JANUARY, 1), LocalTime.MIN));
    var maxParam = (String) params.get("max");
    Objects.requireNonNull(maxParam, "max parameter is null!");
    max = LocalDateTime.parse(maxParam);
    if (min.isAfter(max)) {
      throw new IllegalArgumentException(this + ". Minimum value cannot be greater than maximum value!");
    }
  }

  @Override
  public String generate() {
    long randomSecond = random.nextLong(min.toEpochSecond(ZoneOffset.UTC), max.toEpochSecond(ZoneOffset.UTC));
    return DateTimeFormatter.ISO_DATE_TIME.format(LocalDateTime.ofEpochSecond(randomSecond, 0, ZoneOffset.UTC));
  }
}
