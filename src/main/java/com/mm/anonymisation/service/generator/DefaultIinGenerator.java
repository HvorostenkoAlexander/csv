package com.mm.anonymisation.service.generator;

import com.mm.anonymisation.model.CharSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class DefaultIinGenerator implements IinGenerator {

  private static final List<List<Integer>> WEIGHT = List.of(
      List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11),
      List.of(3, 4, 5, 6, 7, 8, 9, 10, 11, 1, 2)
  );
  private static final Map<String, Map<Integer, String>> CENTURY_MAP = Map.of(
      "1", Map.of(20, "6", 19, "4", 18, "2"),
      "0", Map.of(20, "5", 19, "3", 18, "1"),
      "MAN", Map.of(20, "6", 19, "4", 18, "2"),
      "WOMAN", Map.of(20, "5", 19, "3", 18, "1")
  );
  private final FieldGenerator delegate;

  public DefaultIinGenerator() {
    delegate = new StringFieldGenerator(
        Map.of(
            "min", 4,
            "max", 5,
            "charSet", List.of(CharSet.NUMERIC.name())
        )
    );
  }

  @Override
  public String generate(LocalDate birthDay, String sex) {
    var dateWithSex = buildBirthDay(birthDay) + buildSex(sex, birthDay.getYear());
    return buildCheckDigit(dateWithSex);
  }

  private String buildBirthDay(LocalDate birthDay) {
    return DateTimeFormatter.ofPattern("yyMMdd").format(birthDay);
  }

  private String buildSex(String sex, int year) {
    var floored = Math.floorDiv(year, 100);
    return CENTURY_MAP.get(sex).get(floored);
  }

  private String buildCheckDigit(String iin) {
    while (true) {
      var tmp = iin + delegate.generate();
      for (int j = 0; j < 2; j++) {
        var sum = 0;
        for (int i = 1; i < 12; i++) {
          sum += WEIGHT.get(j).get(i - 1) * Integer.parseInt(tmp.substring(i - 1, i));
        }
        var result = sum % 11;
        if (result < 10) {
          return tmp + result;
        }
      }
    }
  }
}
