package com.mm.anonymisation.config;

import com.mm.anonymisation.repository.DefaultLoggerRepository;
import com.mm.anonymisation.repository.DefaultSchemaRepository;
import com.mm.anonymisation.repository.LoggerRepository;
import com.mm.anonymisation.repository.SchemaRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RepositoryConfig {

  private final DSLContext dslContext;

  @Bean
  public SchemaRepository schemaRepository() {
    return new DefaultSchemaRepository(dslContext);
  }

  @Bean
  public LoggerRepository loggerRepository() {
    return new DefaultLoggerRepository(dslContext);
  }
}
