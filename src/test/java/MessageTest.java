import DAO.MessageDAO;
import Model.Message;
import Service.MessageService;
import Util.ConnectionUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageTest {

    public MessageDAO messageDAO;
    public MessageDAO mockMessageDAO;
    public MessageService messageService;

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
        mockMessageDAO = Mockito.mock(MessageDAO.class);
    }

    /*
     * These tests are for the MessageDAO class
     */

    /**
     * The messageDAO should retrieve all messages when getAllMessages is called.
     */
    @Test
    public void messageDAO_GetAllMessagesTest() {
        List<Message> allMessages = messageDAO.getAllMessages();
        Message m1 = new Message(1,1, "This is a test message", 1678823535691L);
        Message m2 = new Message(2,1, "This is another test message", 1678823535691L);
        Assert.assertTrue(allMessages.contains(m1));
        Assert.assertTrue(allMessages.contains(m2));
    }

    /**
     * The messageDAO should retrieve all messages by user when getAllMessagesByAccountId is called
     */
    @Test
    public void messageDAO_GetAllMessagesByAccountIdTest() {
        List<Message> allMessages = messageDAO.getAllMessagesByAccountId(1);
        Message m1 = new Message(1,1, "This is a test message", 1678823535691L);
        Message m2 = new Message(2,1, "This is another test message", 1678823535691L);
        Assert.assertTrue(allMessages.contains(m1));
        Assert.assertTrue(allMessages.contains(m2));
        Assert.assertEquals(2, allMessages.size());
    }

    /**
     * The messageDAO should return a message with the specific ID when getMessageById is called
     */
    @Test
    public void messageDAO_GetMessageById() {
        Message message = messageDAO.getMessageById(1);
        if (message == null) {
            Assert.fail();
        } else {
            Message m1 = new Message(1, 1, "This is a test message", 1678823535691L);
            Assert.assertEquals(m1, message);
        }
    }

    /**
     * When a message is added it should be retrievable by the message ID.
     */
    @Test
    public void messageDAO_InsertMessageCheckByIdTest() {
        Message newMessage = new Message(1, "This is the third message", 1678823535691L);
        messageDAO.insertMessage(newMessage);
        Message mExpected = new Message(3,1, "This is the third message", 1678823535691L);
        Message mActual = messageDAO.getMessageById(3);
        Assert.assertEquals(mExpected, mActual);
    }

    /**
     * When a message is added it should be listed in all messages.
     */
    @Test
    public void messageDAO_InsertMessageCheckAllMessagesTest() {
        Message newMessage = new Message(1, "This is the third message", 1678823535691L);
        messageDAO.insertMessage(newMessage);
        Message mExpected = new Message(3,1, "This is the third message", 1678823535691L);
        List<Message> allMessages = messageDAO.getAllMessages();
        Assert.assertTrue(allMessages.contains(mExpected));
    }

    /**
     * When a message is added it should return the new message.
     */
    @Test
    public void messageDAO_InsertMessageCheckReturnTest() {
        Message newMessage = new Message(1, "This is the third message", 1678823535691L);
        Message mExpected = new Message(3,1, "This is the third message", 1678823535691L);
        Message mActual = messageDAO.insertMessage(newMessage);
        Assert.assertEquals(mExpected, mActual);
    }

    /**
     * When a message is deleted it should not be retrievable by ID.
     */
    @Test
    public void messageDAO_DeleteMessageCheckByIdTest() {
        messageDAO.deleteMessage(1);
        Message mExpected = messageDAO.getMessageById(1);
        Assert.assertNull(mExpected);
    }

    /**
     * When a message is deleted it should not be in list of all messages.
     */
    @Test
    public void messageDAO_DeleteMessageCheckAllMessagesTest() {
        Message mExpected = messageDAO.getMessageById(1);
        messageDAO.deleteMessage(1);
        List<Message> allMessages = messageDAO.getAllMessages();
        Assert.assertFalse(allMessages.contains(mExpected));
    }

    /**
     * When a message is deleted it should return the deleted message
     */
    @Test
    public void messageDAO_DeleteMessageCheckReturnTest() {
        Message mExpected = messageDAO.getMessageById(1);
        Message mDeleted = messageDAO.deleteMessage(1);
        Assert.assertEquals(mExpected, mDeleted);
    }

    /**
     * If id doesn't exist for message to delete should return null.
     */
    @Test
    public void messageDAO_DeleteMessageMessageDoesntExistTest() {
        Message mDeleted = messageDAO.deleteMessage(5);
        Assert.assertNull(mDeleted);
    }

    /**
     * When a message is updated, the updated values should be retrieved when message is next retrieved.
     */
    @Test
    public void messageDAO_UpdateMessageTest(){
        Message mUpdated = new Message(1, "This is an updated test message", 1678823535691L);
        messageDAO.updateMessage(1, mUpdated.getMessage_text());
        Message mExpected = new Message(1,
                1,
                "This is an updated test message",
                1678823535691L);

        Message mActual = messageDAO.getMessageById(1);
        Assert.assertEquals(mExpected, mActual);
    }

    /**
     * Check return value of updateMessage is new updated message.
     */
    @Test
    public void messageDAO_UpdateMessageCheckReturnTest(){
        Message mUpdated = new Message(1, "This is an updated test message", 1678823535691L);
        Message mActual = messageDAO.updateMessage(1, mUpdated.getMessage_text());
        Message mExpected = new Message(1,
                1,
                "This is an updated test message",
                1678823535691L);
        Assert.assertEquals(mExpected, mActual);
    }

    /*
     * These tests are for the MessageService class
     */

    /**
     * when a messageDAO returns all messages, messageService.getAllMessages should return all messages.
     */
    @Test
    public void messageService_GetAllMessagesTest() {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(1, "First message", 1678823535691L));
        messages.add(new Message(2, "Second message", 1678823535691L));
        messages.add(new Message(3, "Third message", 1678823535691L));
        Mockito.when(mockMessageDAO.getAllMessages()).thenReturn(messages);
        Assert.assertEquals(messages, messageService.getAllMessages());
        Mockito.verify(mockMessageDAO).getAllMessages();
    }

    /**
     * when a messageDAO returns all messages by account ID, messageService.getAllMessagesByAccountID
     * should return all the same messages.
     */
    @Test
    public void messageService_GetAllMessagesByAccountIdTest() {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message(1, "First message", 1678823535691L));
        messages.add(new Message(1, "Second message", 1678823535691L));
        messages.add(new Message(1, "Third message", 1678823535691L));
        Mockito.when(mockMessageDAO.getAllMessagesByAccountId(1)).thenReturn(messages);
        Assert.assertEquals(messages, messageService.getAllMessagesByAccountId(1));
        Mockito.verify(mockMessageDAO).getAllMessagesByAccountId(1);
    }

    @Test
    public void messageService_GetMessageByIdTest() {
        Message message = new Message(1,1, "This is a test message", 1678823535691L);
        Mockito.when(mockMessageDAO.getMessageById(1)).thenReturn(message);
        Assert.assertEquals(message, messageService.getMessageById(1));
    }


    @Test
    public void messageService_AddMessageTest() {
        Message message = new Message(1, "This is a test message", 1678823535691L);
        Message mExpected = new Message(1,1, "This is a test message", 1678823535691L);
        Mockito.when(mockMessageDAO.insertMessage(message)).thenReturn(mExpected);
        Message mActual = messageService.addMessage(message);
        Assert.assertEquals(mExpected, mActual);
        Mockito.verify(mockMessageDAO).insertMessage(Mockito.any());
    }

    @Test
    public void messageService_DeleteMessageTest() {
        Message mExpected = new Message(1,1, "This is a test message", 1678823535691L);
        Mockito.when(mockMessageDAO.deleteMessage(1)).thenReturn(mExpected);
        Message mActual = messageService.deleteMessage(1);
        Assert.assertEquals(mExpected, mActual);
        Mockito.verify(mockMessageDAO).deleteMessage(1);
    }

    @Test
    public void messageService_UpdateMessageTest() {
        Message mExpected = new Message(1,1, "This is a test message", 1678823535691L);
        Mockito.when(mockMessageDAO.updateMessage(1, "This is updated Message")).thenReturn(mExpected);
        Message mActual = messageService.updateMessage(1, "This is updated Message");
        Assert.assertEquals(mExpected, mActual);
    }

}
