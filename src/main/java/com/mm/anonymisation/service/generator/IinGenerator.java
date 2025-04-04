package com.mm.anonymisation.service.generator;

import java.time.LocalDate;

public interface IinGenerator {

  String generate(LocalDate birthDay, String sex);
}
