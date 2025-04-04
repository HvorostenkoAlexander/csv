package com.mm.anonymisation.service;

import com.mm.anonymisation.model.Table;
import java.util.List;

public interface CsvProcessor {

  void process(String path, List<Table> tables);
}
