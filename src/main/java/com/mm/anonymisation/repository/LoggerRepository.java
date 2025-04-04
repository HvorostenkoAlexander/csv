package com.mm.anonymisation.repository;

import java.time.LocalDateTime;

public interface LoggerRepository {

  void init();

  void store(LocalDateTime executedDt, String executedSql, String errorText);
}
