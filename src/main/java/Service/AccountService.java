package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {

    AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    /**
     * Constructor for an accountService when an accountDAO is provided.
     * This is used for when a mock accountDAO that exhibits mock behavior is used in the test cases.
     * This allows the testing of AccountService independently of AccountDAO.
     * @param accountDAO
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
        if (account.getUsername() == null || account.getPassword().length() < 4) {
            return null;
        }

        // Check if account already exists.
        Account accountExists = accountDAO.getAccountByUsername(account.getUsername());
        if (accountExists != null) {
            return null;
        }

        return accountDAO.insertAccount(account);
    }
}
