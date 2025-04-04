package com.mm.anonymisation.service.processor;

import com.mm.anonymisation.model.Table;

public interface RowProcessor {

  void processRow(String[] row, Table table);
}
