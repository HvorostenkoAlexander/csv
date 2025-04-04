package com.mm.anonymisation;

import com.mm.anonymisation.config.KzSolvaAnonymisationConfig;
import com.mm.base.endpoint.MMApp;

public class KzSolvaAnonymisationApp {

  public static void main(String[] args) {
    MMApp.run("kz-solva", "kz-solva-anonymisation", KzSolvaAnonymisationConfig.class);
  }
}
