package com.mm.anonymisation.model;

import com.mm.anonymisation.service.processor.RowPostProcessor;
import java.util.List;

public record Table(
    String name,
    Boolean exclude,
    List<RowPostProcessor> rowPostProcessor,
    List<Field> fields,
    List<Column> columns
) {

}
