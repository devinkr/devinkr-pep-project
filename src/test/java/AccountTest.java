import DAO.AccountDAO;
import Model.Account;
import Service.AccountService;
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
}
