package com.mm.anonymisation.service;

import com.mm.anonymisation.model.Table;
import java.io.InputStream;
import java.util.List;

public interface ConfigReader {

  List<Table> read(InputStream inputStream);
}
