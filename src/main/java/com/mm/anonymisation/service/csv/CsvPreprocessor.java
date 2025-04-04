package com.mm.anonymisation.service.csv;

import java.io.IOException;

public interface CsvPreprocessor {

  String MYSQL_NULL = "\\N";
  char NULL_VALUE = '\u0004';

  String process(String source) throws IOException;
}
