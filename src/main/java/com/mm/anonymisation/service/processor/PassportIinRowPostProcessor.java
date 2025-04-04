package com.mm.anonymisation.service.processor;

import com.mm.anonymisation.model.Table;
import com.mm.anonymisation.service.generator.DefaultIinGenerator;
import com.mm.anonymisation.service.generator.IinGenerator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PassportIinRowPostProcessor implements RowPostProcessor {

  static final String FIELD_NAME = "passport_iin";
  private final IinGenerator iinGenerator;

  public PassportIinRowPostProcessor() {
    iinGenerator = new DefaultIinGenerator();
  }

  @Override
  public void processRow(String[] row, Table table) {
    var birthDay = LocalDate.parse(row[findIndexByName("birthday", table)], DateTimeFormatter.ISO_DATE);
    row[findIndexByName(sourceField(), table)] = iinGenerator.generate(birthDay, row[findIndexByName("sex", table)]);
  }

  public String sourceField() {
    return FIELD_NAME;
  }
}
