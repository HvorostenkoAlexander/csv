package com.mm.anonymisation.service;

import com.mm.anonymisation.model.Table;
import com.mm.anonymisation.service.processor.RowProcessor;
import com.mm.base.service.crypt.CipherEngine;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class CsvRowProcessor implements RowProcessor {

  private final CipherEngine cipherEngine;

  @Override
  public void processRow(String[] row, Table table) {
    for (var field : table.fields()) {
      if (null != field.generator()) {
        row[field.position()] = field.generator().generate();
      }
    }
    for (var processor : table.rowPostProcessor()) {
      processor.processRow(row, table);
    }
    for (var field : table.fields()) {
      if (field.crypt()) {
        row[field.position()] = Base64.getEncoder()
            .encodeToString(cipherEngine.encrypt(row[field.position()]).getBytes());
      }
    }
    for (int pos = 0; pos < row.length; pos++) {
      if (null != row[pos]) {
        row[pos] = row[pos].replace("\n", "/\n");
      }
    }
  }
}
