/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bot;

import com.cyberbugs.model.Expense;
import com.cyberbugs.service.ExpenseService;
import com.cyberbugs.service.UserService;
import com.google.cloud.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 *
 * @author Cruis
 */
@Component
public class ExpenseBot extends TelegramLongPollingBot {

    private static final String START = "/start";
    private static final String HELP = "/help";
    private static final String ADD_EXPENSE = "/add";
    private static final String VIEW_EXPENSES = "/view";
    private static final String DELETE_EXPENSE = "/delete";
    private static final String UPDATE_EXPENSE = "/update";
    private static final String END = "/end";

    @Autowired
    private UserService userService;

    @Autowired
    private ExpenseService expenseService;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            Long userId = update.getMessage().getFrom().getId();
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String username = update.getMessage().getFrom().getUserName();

            switch (messageText.split(" ")[0]) {
                case START ->
                    handleStart(userId, username, chatId);
                case ADD_EXPENSE ->
                    handleAddExpense(chatId, messageText);
                case VIEW_EXPENSES ->
                    handleViewExpense(chatId);
                case DELETE_EXPENSE ->
                    handledDeleteExpense(chatId, messageText);
                case UPDATE_EXPENSE ->
                    handleUpdateExpense(chatId, messageText);
                case HELP ->
                    handleHelp(chatId);
                case END -> 
                    handleEnd(chatId);
                default ->
                    throw new AssertionError();
            }
        }

    }

    private void sendMessage(Long chatId, String response) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(response);
        try {
            execute(message);
        } catch (TelegramApiException e) {
        }

    }

    private boolean isAuthenticated(Long telegramUserId) {
        return userService.isExistId(telegramUserId);
    }

    private void handleStart(Long userId, String username, Long chatId) {
        if (!isAuthenticated(userId)) {
            userService.registerUser(userId, username);
        }
        sendMessage(chatId, "Welcome to CoinCraft, to start, type /help");
    }

    private void handleAddExpense(Long chatId, String messageText) {
        try {
            String[] parts = messageText.replace(ADD_EXPENSE, "").trim().split(" ", 2);
            if (parts.length < 2) {
                sendMessage(chatId, "Enter in this order: " + ADD_EXPENSE + " <amount> <description>");
                return;
            }

            Double amount = Double.valueOf(parts[0].trim());
            String description = parts[1].trim();
            Expense newExpense = new Expense();
            newExpense.setAmount(amount);
            newExpense.setDescription(description);
            newExpense.setCreatedAt(Timestamp.now());
            expenseService.addExpense(description, amount);

            sendMessage(chatId, "Expense added successfully!");

        } catch (Exception e) {
            sendMessage(chatId, "Error: Could not add expense. Please check your input.");

        }
    }

    private void handleViewExpense(Long chatId) {

        String expenses = expenseService.viewAllExpenses();
        sendMessage(chatId, expenses);

    }

    private void handledDeleteExpense(Long chatId, String messageText) {
        try {
            String expenseId = messageText.replace(DELETE_EXPENSE, "").trim();
            if (expenseId.isEmpty()) {
                sendMessage(chatId, "Enter in this order: " + DELETE_EXPENSE + " <expense ID>");
                return;
            }

            expenseService.deleteExpense(expenseId);
            sendMessage(chatId, "Expense deleted successfully");
        } catch (Exception e) {
            sendMessage(chatId, "Error: Could not delete expense. Please check the expense ID.");

        }
    }

    private void handleUpdateExpense(Long chatId, String messageText) {

        try {
            String[] parts = messageText.replace(UPDATE_EXPENSE, "").trim().split(" ", 4);

            if (parts.length < 2) {
                String blockString = """
                                        To update full expense info, type : %s <expesneId> all <new amount> <new description>
                                        To update amount: %s <expesneId> amount <new amount>
                                        To update description: %s <expesneId> description <new description>
                                      
                                     """;
                String instructions = String.format(blockString, UPDATE_EXPENSE);
                sendMessage(chatId, instructions);
                return;
            }
            
            Double amount = null;
            String description = null;
            String expenseId = parts[0];
            switch (parts[1]) {
                case "all":
                    amount = Double.valueOf(parts[2].trim());
                    description = parts[3].trim();
                    break;
                case "amount":
                    amount = Double.valueOf(parts[2].trim());
                    description = null;
                    break;
                case "description":
                    amount = null;
                    description = parts[3].trim();
                    break;
                default:
                    throw new AssertionError();
            }
            expenseService.updateExpense(expenseId, amount, description);
            sendMessage(chatId, "Expense updated successfully");
        } catch (Exception e) {
            sendMessage(chatId, "Error: Could not delete expense. Please check the expense ID.");

        }

    }

    private void handleHelp(Long chatId) {
        String blockString = """
                              Available commands:
                              %s - Add a new expense
                              %s - View all expenses
                              %s - Delete an expense by ID
                              %s - Update an expense by ID
                              /report - Get a summary report of your expenses
                              /exit - End the session
                          
                              """;
        
        String helpText = String.format(blockString, ADD_EXPENSE, VIEW_EXPENSES, DELETE_EXPENSE, UPDATE_EXPENSE);
        
        sendMessage(chatId, helpText);
    }

    private void handleEnd(Long chatId) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
