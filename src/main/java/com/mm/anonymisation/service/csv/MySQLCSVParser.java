package com.mm.anonymisation.service.csv;

import com.opencsv.CSVParser;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class MySQLCSVParser extends CSVParser {

  private final CsvPreprocessor csvPreprocessor;

  @Override
  public String[] parseLine(String nextLine) throws IOException {
    var correct = csvPreprocessor.process(nextLine);
    try {
      return super.parseLine(correct);
    } catch (IOException e) {
      log.error("`{}` / `{}`", nextLine, correct);
      throw e;
    }
  }

  @Override
  protected String convertToCsvValue(String value, boolean applyQuotestoAll) {
    if (null != value && value.length() == 1 && value.charAt(0) == CsvPreprocessor.NULL_VALUE) {
      return CsvPreprocessor.MYSQL_NULL;
    } else {
      return super.convertToCsvValue(value, applyQuotestoAll);
    }
  }
}
