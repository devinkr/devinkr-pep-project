package Controller;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        // Routes
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerAccountHandler);
        app.post("/login", this::loginAccountHandler);

        app.get("/messages", this::getAllMessagesHandler);
        app.post("/messages", this::postMessageHandler);

        app.get("/messages/{message_id}", this::getMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);

        app.get("/accounts/{account_id}/messages", this::getMessagesByAccountHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /**
     * Handler to register new Accounts
     * Will return status code 400 on error such as if account already exists, username is blank,
     * or password is less than 4 characters.
     *
     * @param ctx Javalin context object
     * @throws JsonProcessingException on error converting JSON into object.
     */
    private void registerAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account newAccount = accountService.addAccount(account);
        if (newAccount == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(newAccount));
        }
    }

    /**
     * Login Handler
     * Checks that the given credentials match the username and password in database otherwise
     * returns a status code of 401 Unauthorized.
     * @param ctx Javalin context object.
     * @throws JsonProcessingException on error converting JSON into object.
     */
    private void loginAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account credentials = mapper.readValue(ctx.body(), Account.class);
        Account verifiedAccount = accountService.verifyCredentials(credentials);
        if (verifiedAccount == null) {
            ctx.status(401);
        } else {
            ctx.json(mapper.writeValueAsString(verifiedAccount));
        }
    }

    /**
     * Handler to get all messages.
     *
     * @param ctx Javalin context Object.
     */
    private void getAllMessagesHandler(Context ctx) {
        ctx.json(messageService.getAllMessages());
    }

    /**
     * Handler to post new message.
     * returns status code 400 if message text is blank or too long (>= 255 chars).
     *
     * @param ctx Javalin context object.
     * @throws JsonProcessingException on error converting JSON into object.
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if (addedMessage == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(addedMessage));
        }
    }

    /**
     * Handler to get a message by ID.
     * returns status 200 with matching message or blank if no message matches id.
     *
     * @param ctx Javalin context object.
     */
    public void getMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(id);
        if (message == null) {
            ctx.json("");
        } else {
            ctx.json(mapper.writeValueAsString(message));
        }
    }

    public void updateMessageHandler(Context ctx) throws JsonProcessingException {
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message updatedMessage = messageService.updateMessage(id, message.getMessage_text());
        if (updatedMessage == null) {
            ctx.status(400);
        } else {
            ctx.json(mapper.writeValueAsString(updatedMessage));
        }
    }

    /**
     * Handler to delete message by id.
     * returns status 200 and deleted message or blank if no message was deleted.
     *
     * @param ctx Javalin context object.
     */
    public void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessage(id);
        if (deletedMessage == null) {
            ctx.json("");
        } else {
            ctx.json(mapper.writeValueAsString(deletedMessage));
        }
    }

    /**
     * Handler to get all messages made by a given account.
     * returns status 200 with list of messages or empty list if no matching account.
     *
     * @param ctx Javalin context object.
     */
    public void getMessagesByAccountHandler(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(messageService.getAllMessagesByAccountId(id));
    }


}