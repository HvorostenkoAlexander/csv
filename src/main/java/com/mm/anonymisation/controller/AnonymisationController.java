package com.mm.anonymisation.controller;

import com.mm.anonymisation.model.Table;
import com.mm.anonymisation.service.CsvProcessor;
import com.mm.anonymisation.service.ScriptExecutor;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AnonymisationController {

  private final List<Table> tables;
  private final CsvProcessor csvProcessor;
  private final ScriptExecutor exportScriptExecutor;
  private final ScriptExecutor importScriptExecutor;

  @ApiOperation("export csv")
  @PostMapping("export-csv")
  public void exportCsv(
      @ApiParam(defaultValue = "/database/export/") @RequestParam String path,
      @RequestParam(required = false) List<String> filter
  ) {
    exportScriptExecutor.execute(tables, path);
  }

  @ApiOperation("process csv")
  @PostMapping("process-csv")
  public void processCsv(
      @ApiParam(defaultValue = "/database/export/") @RequestParam String path,
      @RequestParam(required = false) List<String> filter
  ) {
    csvProcessor.process(path, tables(filter));
  }

  @ApiOperation("import csv")
  @PostMapping("import-csv")
  public void importCsv(@ApiParam(defaultValue = "/database/export/") @RequestParam String path) {
    importScriptExecutor.execute(tables, path);
  }

  private List<Table> tables(List<String> filter) {
    if (null == filter || filter.isEmpty()) {
      return tables;
    }
    return tables.stream()
        .filter(table -> filter.contains(table.name()))
        .toList();
  }
}
