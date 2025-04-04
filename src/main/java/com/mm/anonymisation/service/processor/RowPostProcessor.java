package com.mm.anonymisation.service.processor;

import com.mm.anonymisation.model.Column;
import com.mm.anonymisation.model.Table;

public interface RowPostProcessor extends RowProcessor {

  default int findIndexByName(String name, Table table) {
    return table.columns().stream()
        .filter(column -> column.name().equals(name))
        .map(Column::position)
        .findFirst()
        .orElseThrow(() ->
            new IllegalArgumentException("Column `%s` not found in table `%s`!".formatted(name, table.name()))
        );
  }
}
