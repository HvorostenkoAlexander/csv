package com.mm.anonymisation.config;

import com.mm.anonymisation.controller.AnonymisationController;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    ServiceConfig.class
})
@RequiredArgsConstructor
public class ControllerConfig {

  private final ServiceConfig serviceConfig;

  @Bean
  public AnonymisationController anonymisationController() {
    return new AnonymisationController(
        serviceConfig.tables(),
        serviceConfig.csvProcessor(),
        serviceConfig.exportScriptExecutor(),
        serviceConfig.importScriptExecutor()
    );
  }
}
