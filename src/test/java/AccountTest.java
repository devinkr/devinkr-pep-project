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
     * set up a flightDAO and recreate the database tables and mock data.
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
}
