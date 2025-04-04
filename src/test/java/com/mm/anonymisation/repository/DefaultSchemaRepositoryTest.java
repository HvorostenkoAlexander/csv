package com.mm.anonymisation.repository;

import com.mm.testing.mysql.EmbeddedMySQLExtension;
import java.util.List;
import java.util.stream.Stream;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(EmbeddedMySQLExtension.class)
class DefaultSchemaRepositoryTest {

  @Test
  void shouldSuccessfullyGetListAllTables(DSLContext dslContext) {
    var repository = new DefaultSchemaRepository(dslContext);
    var tables = repository.tables();
    Assertions.assertTrue(tables.size() > 1000);
    Assertions.assertTrue(tables.contains("credit"));
    Assertions.assertTrue(tables.contains("borrower"));
  }

  @ParameterizedTest
  @MethodSource("arguments")
  void shouldSuccessfullyGetListAllColumnsInCorrectOrder(
      String tableName, List<String> tableColumns, DSLContext dslContext
  ) {
    var repository = new DefaultSchemaRepository(dslContext);
    var columns = repository.columns(tableName);
    Assertions.assertEquals(tableColumns.size(), columns.size());
    for (int i = 0; i < tableColumns.size(); i++) {
      Assertions.assertEquals(i, columns.get(i).position());
      Assertions.assertEquals(tableColumns.get(i), columns.get(i).name());
    }
  }

  static Stream<Arguments> arguments() {
    return Stream.of(
        Arguments.arguments(
            "credit_calculations_fin_context",
            List.of("credit_id", "fin_context", "calculation_date", "calculation_method")
        ),
        Arguments.arguments(
            "llp_credit",
            List.of("id", "llp_id", "credit_id", "external_offer_number", "llp_bank_account_id")
        )
    );
  }
}
