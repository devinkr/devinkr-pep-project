package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService() {
        messageDAO = new MessageDAO();
    }

    /**
     * This constructor is used for testing MessageService independently of MessageDAO using a mock MessageDAO
     *
     * @param messageDAO a mock MessageDAO
     */
    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }


    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public List<Message> getAllMessagesByAccountId(int id) {
        return messageDAO.getAllMessagesByAccountId(id);
    }

    public Message getMessageById(int id) {
        return null;
    }

    public Message addMessage(Message message) {
        return null;
    }

    public Message deleteMessage(int id) {
        return null;
    }

    public Message updateMessage(int id, String messageText) {
        return null;
    }
}
