package com.mm.anonymisation.service.processor;

public class BorrowerRegistrationIinRowPostProcessor extends PassportIinRowPostProcessor {

  static final String FIELD_NAME = "iin";

  @Override
  public String sourceField() {
    return FIELD_NAME;
  }
}
