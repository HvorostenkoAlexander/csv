package com.mm.anonymisation.model;

import com.mm.anonymisation.service.generator.FieldGenerator;

public record Field(
    String name,
    Integer position,
    boolean crypt,
    boolean base64,
    FieldGenerator generator
) {

}
