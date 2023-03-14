package DAO;

import Model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    /**
     * Retrieve all messages.
     *
     * @return all messages.
     */
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        return messages;
    }

    /**
     * Retrieve all messages written by a particular user.
     *
     * @param account_id a user account ID.
     * @return all the messages created by the user.
     */
    public List<Message> getAllMessagesByAccountId(int account_id) {
        List<Message> messages = new ArrayList<>();
        return messages;
    }

    /**
     * Retrieve a message by its ID
     *
     * @param id a message ID.
     * @return the matching message.
     */
    public Message getMessageById(int id) {
        return null;
    }

    /**
     * Insert a new message into the database.
     *
     * @param message an object modeling a message.
     * @return the newly created message if successful.
     */
    public Message insertMessage(Message message) {
        return null;
    }

    /**
     * Deletes specified message from the database
     *
     * @param id a message ID.
     * @return the deleted message.
     */
    public Message deleteMessage(int id) {
        return null;
    }

    /**
     * Updates the text of the specified message.
     *
     * @param id a message id
     * @param message the message object with updated text.
     * @return the updated message if successful.
     */
    public Message updateMessage(int id, Message message) {
        return null;
    }


}
