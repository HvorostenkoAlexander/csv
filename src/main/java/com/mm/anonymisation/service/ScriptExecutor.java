package com.mm.anonymisation.service;

import com.mm.anonymisation.model.Table;
import java.util.List;

public interface ScriptExecutor {

  void execute(List<Table> tables, String path);
}
