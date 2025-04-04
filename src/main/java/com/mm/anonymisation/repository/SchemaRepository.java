package com.mm.anonymisation.repository;

import com.mm.anonymisation.model.Column;
import java.util.List;

public interface SchemaRepository {

  List<String> tables();

  List<Column> columns(String tableName);

  String structure(String tableName);
}
