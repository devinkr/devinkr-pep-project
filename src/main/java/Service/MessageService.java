package Service;

import DAO.MessageDAO;

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

}
