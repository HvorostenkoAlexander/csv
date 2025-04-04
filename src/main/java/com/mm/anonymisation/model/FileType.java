package com.mm.anonymisation.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {

  CSV(".csv"), SQL(".sql"), PROCESSED(".processed");

  private final String extension;
}
