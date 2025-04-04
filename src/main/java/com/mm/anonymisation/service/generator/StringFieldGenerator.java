package com.mm.anonymisation.service.generator;

import com.mm.anonymisation.model.CharSet;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StringFieldGenerator implements FieldGenerator {

  private final SecureRandom random = new SecureRandom();
  private int min = 5;
  private int max = 255;
  private final String chars;

  public StringFieldGenerator(Map<String, Object> params) {
    var minParam = (Number) params.get("min");
    if (null != minParam) {
      min = minParam.intValue();
    }
    var maxParam = (Number) params.get("max");
    if (null != maxParam) {
      max = maxParam.intValue();
    }
    if (min > max) {
      throw new IllegalArgumentException(this + ". Minimum value cannot be greater than maximum value!");
    }
    @SuppressWarnings("unchecked")
    var charSetParam = (List<String>) params.get("charSet");
    if (null != charSetParam && charSetParam.isEmpty()) {
      throw new IllegalArgumentException(this + ". CharSet cannot be empty!");
    }
    chars = Optional.ofNullable(charSetParam)
        .orElse(List.of(CharSet.LATIN.name()))
        .stream()
        .map(String::toUpperCase)
        .map(CharSet::valueOf)
        .map(CharSet::getChars)
        .collect(Collectors.joining());
  }

  @Override
  public String generate() {
    var streamSize = random.nextInt(min, max);
    return random.ints(streamSize, 0, chars.length())
        .mapToObj(chars::charAt)
        .map(String::valueOf)
        .collect(Collectors.joining());
  }
}
