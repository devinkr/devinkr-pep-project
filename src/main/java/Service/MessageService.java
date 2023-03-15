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

    /**
     * Get all messages
     *
     * @return list of all messages
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * Get all messages made by a user
     *
     * @param id an account id
     * @return List of messages made by user with id
     */
    public List<Message> getAllMessagesByAccountId(int id) {
        return messageDAO.getAllMessagesByAccountId(id);
    }

    /**
     * Get a message by id
     *
     * @param id a message id.
     * @return message with given message id.
     */
    public Message getMessageById(int id) {
        return messageDAO.getMessageById(id);
    }

    /**
     * Add a new message
     *
     * @param message an object representing message to be added.
     * @return newly added message with a message_id.
     */
    public Message addMessage(Message message) {
        // Check if message text is blank or too long
        int messageLength = message.getMessage_text().length();
        if (messageLength == 0 || messageLength >= 255) {
            return null;
        }

        return messageDAO.insertMessage(message);
    }

    /**
     * Delete a message matching id.
     *
     * @param id a message id.
     * @return message that was deleted.
     */
    public Message deleteMessage(int id) {
        Message message = messageDAO.getMessageById(id);
        if (message == null) {
            return null;
        }
        boolean deleted = messageDAO.deleteMessage(id);
        if (deleted) {
            return message;
        }
        return null;
    }

    /**
     *
     * @param id a message id.
     * @param messageText message content to be updated.
     * @return updated message.
     */
    public Message updateMessage(int id, String messageText) {
        // Check if message text is blank or too long
        if (messageText.length() == 0 || messageText.length() >= 255) {
            return null;
        }

        // If message doesn't exist return null
        Message foundMessage = messageDAO.getMessageById(id);
        if (foundMessage == null) {
            return null;
        }
        return messageDAO.updateMessage(id, messageText);
    }
}
