package com.mm.anonymisation.repository;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

@RequiredArgsConstructor
public class DefaultLoggerRepository implements LoggerRepository {

  private final DSLContext dslContext;

  @Override
  public void init() {
    dslContext.createTableIfNotExists("import_csv_log")
        .column("id", SQLDataType.BIGINT.identity(true))
        .column("executed_dt", SQLDataType.LOCALDATETIME)
        .column("executed_sql", SQLDataType.VARCHAR)
        .column("error_text", SQLDataType.VARCHAR)
        .column("resolved", SQLDataType.BOOLEAN)
        .constraint(DSL.constraint("pk_import_log").primaryKey("id"))
        .execute();
  }

  @Override
  public void store(LocalDateTime executedDt, String executedSql, String errorText) {
    dslContext.insertInto(DSL.table("import_csv_log"))
        .set(DSL.field("executed_dt", SQLDataType.LOCALDATETIME), executedDt)
        .set(DSL.field("executed_sql", SQLDataType.VARCHAR), executedSql)
        .set(DSL.field("error_text", SQLDataType.VARCHAR), errorText)
        .set(DSL.field("resolved", SQLDataType.BOOLEAN), false)
        .execute();
  }
}
