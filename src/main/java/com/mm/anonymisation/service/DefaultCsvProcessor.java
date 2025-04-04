package com.mm.anonymisation.service;

import com.mm.anonymisation.model.Field;
import com.mm.anonymisation.model.FileType;
import com.mm.anonymisation.model.Table;
import com.mm.base.service.crypt.CipherEngine;
import com.opencsv.CSVParser;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class DefaultCsvProcessor implements CsvProcessor {

  private final CipherEngine cipherEngine;
  private final CSVParser parser;
  private final long linesProcessLimit;
  private final long gcInterval;

  @Override
  @SuppressWarnings("java:S1215")
  public void process(String path, List<Table> tables) {
    tables.stream()
        .filter(table -> !table.exclude())
        .filter(table -> !table.fields().stream().allMatch(Field::base64))
        .forEach(table -> {
          var exportFile = new File(ScriptGenerator.exportFileName(path, table.name(), FileType.CSV));
          if (exportFile.exists()) {
            log.info("processing file {}", exportFile);
            var importFile = ScriptGenerator.importFileName(path, table.name(), FileType.CSV);
            try (var reader = new BufferedReader(new FileReader(exportFile));
                var writer = new CSVWriterBuilder(new BufferedWriter(new FileWriter(importFile)))
                    .withParser(parser)
                    .build()
            ) {
              var cnt = 0;
              while (true) {
                var data = readCSV(reader, linesProcessLimit);
                if (data.isEmpty()) {
                  break;
                }
                processValues(data, table);
                writeCSV(writer, data);
                cnt++;
                if (cnt == gcInterval) {
                  System.gc();
                  cnt = 0;
                }
              }
              var processedName = ScriptGenerator.exportFileName(path, table.name(), FileType.PROCESSED);
              Files.move(exportFile.toPath(), Path.of(processedName));
              log.info("file {} processing completed", exportFile);
            } catch (Exception e) {
              log.error("error processing file {}", exportFile, e);
              throw new IllegalStateException(e);
            }
          } else {
            log.warn("file {} not found", exportFile);
          }
        });
    log.info("finished processing files");
  }

  private List<String[]> readCSV(BufferedReader reader, long limit) throws IOException {
    String line;
    var data = new LinkedList<String[]>();
    int cnt = 0;
    while ((line = readLine(reader)) != null) {
      data.add(parser.parseLine(line));
      if (++cnt == limit) {
        break;
      }
    }
    log.info("read {} entries", data.size());
    return data;
  }

  private String readLine(BufferedReader reader) throws IOException {
    String line;
    List<String> builder = null;
    while ((line = reader.readLine()) != null) {
      if (line.endsWith("\\")) {
        if (null == builder) {
          builder = new ArrayList<>();
        }
        builder.add(line.substring(0, line.length() - 1));
      } else {
        if (null != builder) {
          builder.add(line);
        }
        break;
      }
    }
    return null != builder ? String.join("\n", builder) : line;
  }

  private void processValues(List<String[]> data, Table table) {
    var processor = new CsvRowProcessor(cipherEngine);
    data.parallelStream()
        .forEach(row -> processor.processRow(row, table));
  }

  private void writeCSV(ICSVWriter writer, List<String[]> data) {
    data.forEach(row -> writer.writeNext(row, true));
    log.info("write {} entries", data.size());
  }
}
