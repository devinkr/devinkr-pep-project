import DAO.AccountDAO;
import Model.Account;
import Service.AccountService;
import Util.BCrypt;
import Util.ConnectionUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AccountTest {

    public AccountDAO accountDAO;
    public AccountDAO mockAccountDao;
    public AccountService accountService;

    /**
     * set up an AccountDAO and reset the database tables and create mock data.
     */
    @Before
    public void setUp(){
        ConnectionUtil.resetTestDatabase();
        Connection connection = ConnectionUtil.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO account (username, password) VALUES ('bob123', 'password')");
            ps.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        accountDAO = new AccountDAO();
        mockAccountDao = Mockito.mock(AccountDAO.class);
        accountService = new AccountService(mockAccountDao);
    }

    /**
     * These tests are for the AccountDAO class
     */

    @Test
    public void accountDAO_GetAccountByIDTest() {
        Account account = accountDAO.getAccountById(1);
        if (account == null) {
            Assert.fail();
        } else {
            Account user1 = new Account(1, "bob123", "password");
            Assert.assertEquals(account, user1);
        }
    }

    @Test
    public void accountDAO_GetAccountByUsernameTest() {
        Account account = accountDAO.getAccountByUsername("bob123");
        if (account == null) {
            Assert.fail();
        } else {
            Account user1 = new Account(1, "bob123", "password");
            Assert.assertEquals(account, user1);
        }
    }

    @Test
    public void accountDAO_InsertAccountCheckByIdTest() {
        Account newUser = new Account("Steve", "pass12345");
        accountDAO.insertAccount(newUser);

        Account userExpected = new Account(2, "Steve", "pass12345");
        Account userActual = accountDAO.getAccountById(2);
        Assert.assertEquals(userExpected.getAccount_id(), userActual.getAccount_id());
        Assert.assertEquals(userExpected.getUsername(), userActual.getUsername());

        boolean matched = BCrypt.checkpw("pass12345", userActual.getPassword());
        Assert.assertTrue(matched);

    }

    /**
     * These tests are for the AccountService class
     */

    @Test
    public void accountService_AddAccountTest() {
        Account newAccount = new Account("Gary", "pass12345");
        Account persistedAccount = new Account(1, "Gary", "pass12345");
        Mockito.when(mockAccountDao.insertAccount(newAccount)).thenReturn(persistedAccount);
        Account actualAccount = accountService.addAccount(newAccount);
        Assert.assertEquals(persistedAccount, actualAccount);
        Mockito.verify(mockAccountDao).insertAccount(Mockito.any());
    }

    @Test
    public void accountService_AddAccountWithoutUsernameTest() {
        Account newAccount = new Account("", "password");
        Account actualAccount = accountService.addAccount(newAccount);
        Assert.assertNull(actualAccount);
    }

    @Test
    public void accountService_AddAccountWithShortPassword() {
        Account newAccount = new Account("Mary", "123");
        Account actualAccount = accountService.addAccount(newAccount);
        Assert.assertNull(actualAccount);
    }
}
