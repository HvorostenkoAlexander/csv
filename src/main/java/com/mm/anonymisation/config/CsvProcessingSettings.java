package com.mm.anonymisation.config;

import java.util.Optional;

public interface CsvProcessingSettings {

  Optional<Long> linesProcessLimit();

  Optional<Long> gcInterval();
}
