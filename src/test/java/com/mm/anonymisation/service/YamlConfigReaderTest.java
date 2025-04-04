package com.mm.anonymisation.service;

import com.mm.anonymisation.model.Column;
import com.mm.anonymisation.repository.SchemaRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class YamlConfigReaderTest {

  @Test
  void shouldSuccessReadConfig() {
    var repository = Mockito.mock(SchemaRepository.class);
    Mockito.when(repository.columns("borrower")).thenReturn(
        List.of(new Column("secret_key", 0), new Column("profile_last_updated_date", 1))
    );
    Mockito.when(repository.columns("personal_data")).thenReturn(
        List.of(new Column("first_name", 0), new Column("sex", 1), new Column("passport_data", 2))
    );
    Mockito.when(repository.columns("borrower_tracking")).thenReturn(
        List.of(new Column("calculator_type", 0))
    );
    Mockito.when(repository.columns("credit")).thenReturn(
        List.of(new Column("sent_amount", 0), new Column("date_repaid", 1), new Column("date_requested", 2))
    );
    Mockito.when(repository.columns("credit_calculations_fin_context")).thenReturn(
        List.of(new Column("fin_context", 0))
    );
    var stream = this.getClass().getClassLoader().getResourceAsStream("tables-config.yaml");
    var tables = new YamlConfigReader(repository).read(stream);
    Assertions.assertNotNull(tables);
  }
}
