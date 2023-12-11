package app.services.interfaces;

import app.account.Account;

public interface AccountService {
    Account getAccountByEmail(String email);
}