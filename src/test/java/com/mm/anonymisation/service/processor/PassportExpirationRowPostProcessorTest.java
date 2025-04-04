package com.mm.anonymisation.service.processor;

import com.mm.anonymisation.model.Column;
import com.mm.anonymisation.model.Table;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PassportExpirationRowPostProcessorTest {

  @Test
  void shouldGenerateValidFieldValue() {
    var columns = List.of(
        new Column("passport_date", 0),
        new Column("passport_expiration_date", 1)
    );
    var table = new Table("", false, Collections.emptyList(), Collections.emptyList(), columns);
    var processor = new PassportExpirationRowPostProcessor();
    var row = new String[2];
    var now = LocalDate.now();
    row[0] = DateTimeFormatter.ISO_DATE.format(now);
    processor.processRow(row, table);
    Assertions.assertEquals(now.plusYears(10), LocalDate.parse(row[1]));
  }
}