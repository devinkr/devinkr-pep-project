package Service;

import DAO.AccountDAO;
import Model.Account;
import Util.BCrypt;

public class AccountService {

    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    /**
     * Constructor for an accountService when an accountDAO is provided.
     * This is used for when a mock accountDAO that exhibits mock behavior is used in the test cases.
     * This allows the testing of AccountService independently of AccountDAO.
     * @param accountDAO is mock accountDAO
     */
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    /**
     * This method will create a new user account in the database
     * and return the newly created account
     *
     * @param account an object representing a new Account.
     * @return the newly added account if the operation was successful
     */
    public Account addAccount(Account account){
        // Verify username is not null and password is at least 4 characters
        if (account.getUsername().equals("") || account.getPassword().length() < 4) {
            return null;
        }

        // Check if account already exists.
        Account accountExists = accountDAO.getAccountByUsername(account.getUsername());
        if (accountExists != null) {
            return null;
        }

        return accountDAO.insertAccount(account);
    }

    public Account verifyCredentials(Account credentials) {
        if (credentials.getUsername().equals("")) {
            return null;
        }

        Account account = accountDAO.getAccountByUsername(credentials.getUsername());
        if (account == null) {
            return null;
        }

        boolean matched = BCrypt.checkpw(credentials.getPassword(), account.getPassword());
        if (matched) {
            // Setting password to plaintext since test expects plaintext password and not hash.
            // Would normally never send a password in a response and especially not in plaintext.
            account.setPassword(credentials.getPassword());
            return account;
        }
        return null;
    }
}
