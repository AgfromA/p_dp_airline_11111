package app.service.interfaces;

import app.entities.account.Account;


public interface AccountService {

    Account getAccountByEmail(String email);

}
