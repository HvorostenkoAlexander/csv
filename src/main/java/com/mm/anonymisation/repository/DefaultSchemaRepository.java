package com.mm.anonymisation.repository;

import static com.mm.anonymisation.db.information_schema.Tables.COLUMNS;
import static com.mm.anonymisation.db.information_schema.Tables.TABLES;

import com.mm.anonymisation.model.Column;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.ResultQuery;
import org.jooq.impl.DSL;

@RequiredArgsConstructor
public class DefaultSchemaRepository implements SchemaRepository {

  private final DSLContext dslContext;

  @Override
  public List<String> tables() {
    return dslContext.select(TABLES.TABLE_NAME)
        .from(TABLES)
        .where(
            TABLES.TABLE_SCHEMA.eq(DSL.currentSchema())
                .and(TABLES.TABLE_TYPE.eq("BASE TABLE"))
        )
        .orderBy(TABLES.TABLE_NAME)
        .fetch(TABLES.TABLE_NAME);
  }

  @Override
  public List<Column> columns(String tableName) {
    return dslContext.select(COLUMNS.COLUMN_NAME, COLUMNS.ORDINAL_POSITION)
        .from(COLUMNS)
        .where(
            COLUMNS.TABLE_NAME.eq(tableName)
                .and(COLUMNS.TABLE_SCHEMA.eq(DSL.currentSchema()))
        )
        .orderBy(COLUMNS.ORDINAL_POSITION)
        .fetch(rec -> new Column(rec.value1(), rec.value2().intValue() - 1));
  }

  @Override
  public String structure(String tableName) {
    ResultQuery<Record> records = dslContext.resultQuery("show create table %s".formatted(tableName));
    return records.fetchOne().getValue(1, String.class);
  }
}
