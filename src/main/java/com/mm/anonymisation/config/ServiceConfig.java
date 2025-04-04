package com.mm.anonymisation.config;

import com.mm.anonymisation.model.Table;
import com.mm.anonymisation.service.CsvProcessor;
import com.mm.anonymisation.service.DefaultCsvProcessor;
import com.mm.anonymisation.service.ExportScriptExecutor;
import com.mm.anonymisation.service.ExportScriptGenerator;
import com.mm.anonymisation.service.ExportTableStructureScriptExecutor;
import com.mm.anonymisation.service.ImportScriptExecutor;
import com.mm.anonymisation.service.ImportScriptGenerator;
import com.mm.anonymisation.service.ImportTableStructureScriptGenerator;
import com.mm.anonymisation.service.ResourceScriptGenerator;
import com.mm.anonymisation.service.ScriptExecutor;
import com.mm.anonymisation.service.ScriptGenerator;
import com.mm.anonymisation.service.YamlConfigReader;
import com.mm.anonymisation.service.csv.CsvPreprocessor;
import com.mm.anonymisation.service.csv.MySQLCSVParser;
import com.mm.anonymisation.service.csv.MySQLCsvPreprocessor;
import com.mm.base.service.crypt.CipherEngine;
import com.mm.base.service.crypt.CipherEngineAES;
import com.opencsv.CSVParser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.cfg4j.provider.ConfigurationProvider;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    RepositoryConfig.class
})
@RequiredArgsConstructor
public class ServiceConfig {

  private final DSLContext dslContext;
  private final RepositoryConfig repositoryConfig;
  private final ConfigurationProvider configurationProvider;

  @Bean
  public List<Table> tables() {
    var stream = this.getClass().getClassLoader().getResourceAsStream("config.yaml");
    var reader = new YamlConfigReader(repositoryConfig.schemaRepository());
    return reader.read(stream);
  }

  @Bean
  public ScriptGenerator exportScriptGenerator() {
    return new ExportScriptGenerator(repositoryConfig.schemaRepository());
  }

  @Bean
  public ScriptGenerator importScriptGenerator() {
    return new ImportScriptGenerator(repositoryConfig.schemaRepository());
  }

  @Bean
  public CipherEngine cipherEngine() {
    var secretKeys = configurationProvider.bind("secretKey", String[].class);
    return new CipherEngineAES(secretKeys);
  }

  @Bean
  public CsvPreprocessor csvCorrector() {
    return new MySQLCsvPreprocessor();
  }

  @Bean
  public CSVParser csvParser() {
    return new MySQLCSVParser(csvCorrector());
  }

  @Bean
  public CsvProcessor csvProcessor() {
    var settings = configurationProvider.bind("settings", CsvProcessingSettings.class);
    return new DefaultCsvProcessor(
        cipherEngine(),
        csvParser(),
        settings.linesProcessLimit().orElse(10000L),
        settings.gcInterval().orElse(10L)
    );
  }

  @Bean
  public ScriptExecutor exportScriptExecutor() {
    return new ExportScriptExecutor(
        dslContext, exportTableStructureScriptExecutor(), exportScriptGenerator()
    );
  }

  @Bean
  public ScriptGenerator resourceScriptGenerator() {
    return new ResourceScriptGenerator();
  }

  @Bean
  public ScriptExecutor importScriptExecutor() {
    return new ImportScriptExecutor(
        dslContext,
        importTableStructureScriptGenerator(),
        importScriptGenerator(),
        resourceScriptGenerator(),
        repositoryConfig.loggerRepository()
    );
  }

  @Bean
  public ScriptExecutor exportTableStructureScriptExecutor() {
    return new ExportTableStructureScriptExecutor(repositoryConfig.schemaRepository());
  }

  @Bean
  public ScriptGenerator importTableStructureScriptGenerator() {
    return new ImportTableStructureScriptGenerator(repositoryConfig.schemaRepository());
  }
}
