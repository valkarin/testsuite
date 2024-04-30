package com.finalcoder.testsuite.Common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Constants {

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class ParaBank {
    public static final String CREDENTIALS_KEY = "parabank.credentials";
    public static final String HIGHEST_AMOUNT_ACCOUNT_KEY = "parabank.highest_amount_account";
    public static final String NEW_ACCOUNT_KEY = "parabank.new_account";
    public static final String NEW_ACCOUNT_BALANCE_KEY = "parabank.new_account_balance";
    public static final String TRANSFER_AMOUNT_KEY = "parabank.transfer_amount";
  }
}
