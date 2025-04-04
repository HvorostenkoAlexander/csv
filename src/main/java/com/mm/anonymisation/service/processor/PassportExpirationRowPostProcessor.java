package com.mm.anonymisation.service.processor;

import com.mm.anonymisation.model.Table;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PassportExpirationRowPostProcessor implements RowPostProcessor {

  @Override
  public void processRow(String[] row, Table table) {
    var passportDate = row[findIndexByName("passport_date", table)];
    var expirationDate = LocalDate.parse(passportDate).plusYears(10);
    row[findIndexByName("passport_expiration_date", table)] = DateTimeFormatter.ISO_DATE.format(expirationDate);
  }
}
