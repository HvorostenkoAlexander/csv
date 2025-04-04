package com.mm.anonymisation.service.csv;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MySQLCsvPreprocessor implements CsvPreprocessor {

  private static final byte[] MYSQL_NULL_BYTES = MYSQL_NULL.getBytes();
  private static final String ESCAPED_NULL = "\"%s\"".formatted(NULL_VALUE);
  private static final byte[] ESCAPED_NULL_BYTES = ESCAPED_NULL.getBytes();
  private static final char COMMA = ',';

  @Override
  public String process(String source) throws IOException {
    if (null == source || source.isEmpty()) {
      return source;
    }
    if (MYSQL_NULL.equals(source)) {
      return ESCAPED_NULL;
    }
    var bytes = source.getBytes();

    var offset = 0;
    OutputStream outputStream = null;
    int j = 0;
    do {
      if (bytes[j] == COMMA && bytes[j + 1] == MYSQL_NULL_BYTES[0] && bytes[j + 2] == MYSQL_NULL_BYTES[1]
          && (j + 3 == bytes.length || bytes[j + 3] == COMMA)
      ) {
        if (null == outputStream) {
          outputStream = new ByteArrayOutputStream();
        }
        outputStream.write(bytes, offset, j - offset);
        outputStream.write(COMMA);
        outputStream.write(ESCAPED_NULL_BYTES);
        offset = j + 3;
      } else if (0 == j && bytes[j] == MYSQL_NULL_BYTES[0] && bytes[j + 1] == MYSQL_NULL_BYTES[1]) {
        outputStream = new ByteArrayOutputStream();
        outputStream.write(ESCAPED_NULL_BYTES);
        j++;
        offset = 2;
      }
      j++;
    } while (j <= bytes.length - MYSQL_NULL_BYTES.length);
    if (null == outputStream) {
      return source;
    }
    outputStream.write(bytes, offset, bytes.length - offset);
    return outputStream.toString();
  }
}
