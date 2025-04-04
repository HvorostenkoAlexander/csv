package com.mm.anonymisation.service.processor;

import com.mm.anonymisation.model.Table;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FullNameRowPostProcessor implements RowPostProcessor {

  @Override
  public void processRow(String[] row, Table table) {
    var fullName = Stream.of("first_name", "patronymic", "last_name")
        .map(col -> findIndexByName(col, table))
        .map(ind -> row[ind])
        .filter(v -> !v.isBlank())
        .collect(Collectors.joining(" "));
    row[findIndexByName("full_name", table)] = fullName;
  }
}
