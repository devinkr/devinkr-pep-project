import DAO.MessageDAO;
import Model.Message;
import Util.ConnectionUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class MessageTest {

    public MessageDAO messageDAO;
    public MessageDAO mockMessageDao;

    /**
     * set up a messageDAO, reset the database tables, and create mock data.
     */

    @Before
    public void setUp() {
        ConnectionUtil.resetTestDatabase();
        Connection connection = ConnectionUtil.getConnection();
        try {
            PreparedStatement psAccount = connection.prepareStatement("INSERT INTO account " +
                    "(username, password) VALUES ('bob123', 'password');");
            psAccount.executeUpdate();
            PreparedStatement psMessage = connection.prepareStatement("INSERT INTO message " +
                    "(posted_by, message_text, time_posted_epoch) VALUES " +
                    "(1, 'This is a test message', 1678823535691L)," +
                    "(1, 'This is another test message', 1678823535691L);");
            psMessage.executeUpdate();
        } catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        messageDAO = new MessageDAO();
        mockMessageDao = Mockito.mock(MessageDAO.class);
    }

    /**
     * These tests are for the MessageDAO class
     */

    @Test
    public void messageDAO_GetAllMessagesTest() {
        List<Message> allMessages = messageDAO.getAllMessages();
        Message m1 = new Message(1, "This is a test message", 1678823535691L);
        Message m2 = new Message(1, "This is another test message", 1678823535691L);
        Assert.assertTrue(allMessages.contains(m1));
        Assert.assertTrue(allMessages.contains(m2));
    }

}
