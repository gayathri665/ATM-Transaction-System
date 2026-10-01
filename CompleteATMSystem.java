package GUI_Package;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

public class CompleteATMSystem extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private String currentUser;
    private double balance;
    private String currentTransactionType;
    private double transactionAmount;
    
    // Database simulation
    private HashMap<String, String> users = new HashMap<>();
    private HashMap<String, Double> balances = new HashMap<>();
    private HashMap<String, String> userNames = new HashMap<>();
    private HashMap<String, ArrayList<String>> transactionHistory = new HashMap<>();
    
    // GUI Components
    private JPasswordField pinField;
    private JTextField amountField;
    private JLabel balanceLabel;
    private JTextArea receiptArea;

    public CompleteATMSystem() {
        initializeDatabase();
        setupGUI();
    }
    
    private void initializeDatabase() {
        // User data: cardNumber -> PIN
        users.put("1234567890123456", "1234");
        users.put("6543210987654321", "4321");
        
        // Balances
        balances.put("1234567890123456", 1500.00);
        balances.put("6543210987654321", 3000.00);
        
        // User names
        userNames.put("1234567890123456", "John Doe");
        userNames.put("6543210987654321", "Jane Smith");
        
        // Transaction history
        initializeTransactionHistory();
    }
    
    private void initializeTransactionHistory() {
        transactionHistory.put("1234567890123456", new ArrayList<>());
        transactionHistory.put("6543210987654321", new ArrayList<>());
        
        // Add sample transactions
        addTransaction("1234567890123456", "2024-01-15 10:30", "DEPOSIT", 500.00, 1500.00);
        addTransaction("6543210987654321", "2024-01-14 14:22", "WITHDRAWAL", 200.00, 3000.00);
    }
    
    private void setupGUI() {
        setTitle("ATM Machine - Secure Banking");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setResizable(false);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create all screens
        mainPanel.add(createWelcomeScreen(), "WELCOME");
        mainPanel.add(createPinEntryScreen(), "PIN_ENTRY");
        mainPanel.add(createMainMenuScreen(), "MAIN_MENU");
        mainPanel.add(createBalanceScreen(), "BALANCE");
        mainPanel.add(createWithdrawalScreen(), "WITHDRAWAL");
        mainPanel.add(createAmountSelectionScreen(), "AMOUNT_SELECT");
        mainPanel.add(createProcessingScreen(), "PROCESSING");
        mainPanel.add(createDepositScreen(), "DEPOSIT");
        mainPanel.add(createTransferScreen(), "TRANSFER");
        mainPanel.add(createMiniStatementScreen(), "MINI_STATEMENT");
        mainPanel.add(createReceiptScreen(), "RECEIPT");
        mainPanel.add(createThankYouScreen(), "THANK_YOU");
        mainPanel.add(createErrorScreen(), "ERROR");
        
        add(mainPanel);
        showScreen("WELCOME");
    }
    
    private JPanel createWelcomeScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 102, 204)); // Bank blue
        
        JLabel titleLabel = new JLabel("WELCOME TO GLOBAL BANK", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        
        // FIX: use JLabel (not J.Label)
        JLabel instructionLabel = new JLabel("Please insert your card to begin", JLabel.CENTER);
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionLabel.setForeground(Color.WHITE);
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        JButton insertCardBtn = new JButton("INSERT CARD");
        insertCardBtn.setFont(new Font("Arial", Font.BOLD, 18));
        insertCardBtn.setBackground(Color.YELLOW);
        insertCardBtn.setFocusPainted(false);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(instructionLabel, BorderLayout.CENTER);
        panel.add(insertCardBtn, BorderLayout.SOUTH);
        
        insertCardBtn.addActionListener(e -> showScreen("PIN_ENTRY"));
        
        return panel;
    }
    
    private JPanel createPinEntryScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("ENTER YOUR PIN", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        JPanel centerPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        
        pinField = new JPasswordField(4);
        pinField.setFont(new Font("Arial", Font.BOLD, 24));
        pinField.setHorizontalAlignment(JTextField.CENTER);
        
        JPanel keypadPanel = createNumericKeypad(pinField);
        
        JButton submitBtn = new JButton("SUBMIT");
        JButton cancelBtn = new JButton("CANCEL");
        
        centerPanel.add(new JLabel("PIN:", JLabel.CENTER));
        centerPanel.add(pinField);
        centerPanel.add(keypadPanel);
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(cancelBtn);
        buttonPanel.add(submitBtn);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        submitBtn.addActionListener(e -> authenticateUser());
        cancelBtn.addActionListener(e -> showScreen("WELCOME"));
        
        return panel;
    }
    
    private JPanel createNumericKeypad(JTextField targetField) {
        JPanel panel = new JPanel(new GridLayout(4, 3, 5, 5));
        
        for (int i = 1; i <= 9; i++) {
            JButton btn = new JButton(String.valueOf(i));
            btn.setFont(new Font("Arial", Font.BOLD, 18));
            final int number = i;
            btn.addActionListener(e -> {
                targetField.setText(targetField.getText() + number);
            });
            panel.add(btn);
        }
        
        JButton clearBtn = new JButton("CLR");
        clearBtn.addActionListener(e -> targetField.setText(""));
        panel.add(clearBtn);
        
        JButton zeroBtn = new JButton("0");
        zeroBtn.addActionListener(e -> targetField.setText(targetField.getText() + "0"));
        panel.add(zeroBtn);
        
        JButton backBtn = new JButton("←");
        backBtn.addActionListener(e -> {
            String text = targetField.getText();
            if (text.length() > 0) {
                targetField.setText(text.substring(0, text.length() - 1));
            }
        });
        panel.add(backBtn);
        
        return panel;
    }
    
    private JPanel createMainMenuScreen() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // protect against null currentUser when building menu
        String name = (currentUser != null && userNames.get(currentUser) != null) 
                        ? userNames.get(currentUser) 
                        : "Customer";
        JLabel welcomeLabel = new JLabel("Welcome, " + name, JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        String[] menuItems = {
            "Balance Inquiry",
            "Cash Withdrawal", 
            "Cash Deposit",
            "Fund Transfer",
            "Mini Statement",
            "Change PIN",
            "Exit"
        };
        
        panel.add(welcomeLabel);
        
        for (String item : menuItems) {
            JButton btn = new JButton(item);
            btn.addActionListener(e -> handleMenuSelection(item));
            panel.add(btn);
        }
        
        return panel;
    }
    
    private void handleMenuSelection(String menuItem) {
        switch (menuItem) {
            case "Balance Inquiry":
                // update balance label before showing
                balanceLabel.setText("Current Balance: $" + String.format("%.2f", balance));
                showScreen("BALANCE");
                break;
            case "Cash Withdrawal":
                currentTransactionType = "WITHDRAWAL";
                showScreen("WITHDRAWAL");
                break;
            case "Cash Deposit":
                currentTransactionType = "DEPOSIT";
                showScreen("DEPOSIT");
                break;
            case "Fund Transfer":
                currentTransactionType = "TRANSFER";
                showScreen("TRANSFER");
                break;
            case "Mini Statement":
                showScreen("MINI_STATEMENT");
                break;
            case "Change PIN":
                // Implement PIN change
                JOptionPane.showMessageDialog(this, "PIN change feature coming soon!");
                break;
            case "Exit":
                showScreen("THANK_YOU");
                break;
        }
    }
    
    private JPanel createBalanceScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        balanceLabel = new JLabel("Current Balance: $" + String.format("%.2f", balance), JLabel.CENTER);
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 24));
        balanceLabel.setForeground(Color.BLUE);
        
        JButton receiptBtn = new JButton("Print Receipt");
        JButton anotherBtn = new JButton("Another Transaction");
        JButton exitBtn = new JButton("Exit");
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        buttonPanel.add(receiptBtn);
        buttonPanel.add(anotherBtn);
        buttonPanel.add(exitBtn);
        
        panel.add(balanceLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        receiptBtn.addActionListener(e -> generateReceipt("BALANCE_INQUIRY", 0));
        anotherBtn.addActionListener(e -> showScreen("MAIN_MENU"));
        exitBtn.addActionListener(e -> showScreen("THANK_YOU"));
        
        return panel;
    }
    
    private JPanel createWithdrawalScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("SELECT WITHDRAWAL AMOUNT", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JPanel amountPanel = new JPanel(new GridLayout(2, 3, 10, 10));
        double[] quickAmounts = {20, 40, 60, 100, 200, 500};
        
        for (double amount : quickAmounts) {
            JButton btn = new JButton("$" + (int)amount);
            btn.addActionListener(e -> processWithdrawal(amount));
            amountPanel.add(btn);
        }
        
        JButton otherAmountBtn = new JButton("OTHER AMOUNT");
        JButton backBtn = new JButton("BACK");
        
        JPanel controlPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        controlPanel.add(backBtn);
        controlPanel.add(otherAmountBtn);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(amountPanel, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        otherAmountBtn.addActionListener(e -> showScreen("AMOUNT_SELECT"));
        backBtn.addActionListener(e -> showScreen("MAIN_MENU"));
        
        return panel;
    }
    
    private JPanel createAmountSelectionScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("ENTER AMOUNT", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        amountField = new JTextField();
        amountField.setFont(new Font("Arial", Font.BOLD, 20));
        amountField.setHorizontalAlignment(JTextField.RIGHT);
        
        JPanel keypadPanel = createNumericKeypad(amountField);
        
        JButton enterBtn = new JButton("ENTER");
        JButton clearBtn = new JButton("CLEAR");
        JButton backBtn = new JButton("BACK");
        
        JPanel controlPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        controlPanel.add(backBtn);
        controlPanel.add(clearBtn);
        controlPanel.add(enterBtn);
        
        // FIX: place amountField at NORTH and keypad in CENTER
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(amountField, BorderLayout.NORTH);
        panel.add(keypadPanel, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.SOUTH);
        
        enterBtn.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if ("WITHDRAWAL".equals(currentTransactionType)) {
                    processWithdrawal(amount);
                } else if ("DEPOSIT".equals(currentTransactionType)) {
                    // deposit flow (simple)
                    transactionAmount = amount;
                    currentTransactionType = "DEPOSIT";
                    completeDeposit();
                }
            } catch (NumberFormatException ex) {
                showError("Invalid amount entered!");
            }
        });
        
        clearBtn.addActionListener(e -> amountField.setText(""));
        backBtn.addActionListener(e -> showScreen("WITHDRAWAL"));
        
        return panel;
    }
    
    private JPanel createProcessingScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(50, 20, 20, 20));

        JLabel processingLabel = new JLabel("PROCESSING TRANSACTION...", JLabel.CENTER);
        processingLabel.setFont(new Font("Arial", Font.BOLD, 20));

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        panel.add(processingLabel, BorderLayout.CENTER);
        panel.add(progressBar, BorderLayout.SOUTH);

        // Start timer only when panel becomes visible
        panel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentShown(java.awt.event.ComponentEvent e) {
                // create a new one-shot timer each time it's shown
                Timer timer = new Timer(2000, ev -> {
                    // do transaction completion on EDT (Timer runs on EDT already)
                    completeTransaction();
                });
                timer.setRepeats(false);
                timer.start();
            }
        });

        return panel;
    }
    private JPanel createDepositScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel title = new JLabel("DEPOSIT AMOUNT", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        
        JTextField depositField = new JTextField();
        depositField.setFont(new Font("Arial", Font.BOLD, 20));
        depositField.setHorizontalAlignment(JTextField.RIGHT);
        
        JPanel keypad = createNumericKeypad(depositField);
        
        JButton enter = new JButton("ENTER");
        JButton back = new JButton("BACK");
        
        enter.addActionListener(e -> {
            try {
                double amt = Double.parseDouble(depositField.getText());
                if (amt > 0) {
                    transactionAmount = amt;
                    currentTransactionType = "DEPOSIT";
                    showScreen("PROCESSING");
                } else {
                    showError("Enter a positive amount!");
                }
            } catch (NumberFormatException ex) {
                showError("Invalid amount!");
            }
        });
        back.addActionListener(e -> showScreen("MAIN_MENU"));
        
        panel.add(title, BorderLayout.NORTH);
        panel.add(keypad, BorderLayout.CENTER);
        
        JPanel bottom = new JPanel(new GridLayout(1,2,10,10));
        bottom.add(back);
        bottom.add(enter);
        panel.add(bottom, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createTransferScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        // Implementation for fund transfers (placeholder)
        JLabel label = new JLabel("Transfer feature coming soon", JLabel.CENTER);
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createMiniStatementScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("MINI STATEMENT", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        JTextArea statementArea = new JTextArea(15, 30);
        statementArea.setEditable(false);
        statementArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        // Display transaction history
        StringBuilder statement = new StringBuilder();
        statement.append("Date       Time     Type       Amount    Balance\n");
        statement.append("------------------------------------------------\n");
        
        ArrayList<String> transactions = transactionHistory.get(currentUser);
        if (transactions != null) {
            for (String transaction : transactions) {
                statement.append(transaction).append("\n");
            }
        }
        
        statementArea.setText(statement.toString());
        JScrollPane scrollPane = new JScrollPane(statementArea);
        
        JButton backBtn = new JButton("Back to Main Menu");
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(backBtn, BorderLayout.SOUTH);
        
        backBtn.addActionListener(e -> showScreen("MAIN_MENU"));
        
        return panel;
    }
    
    private JPanel createReceiptScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("TRANSACTION RECEIPT", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        receiptArea = new JTextArea(10, 30);
        receiptArea.setEditable(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(receiptArea);
        
        JButton printBtn = new JButton("Print Receipt");
        JButton doneBtn = new JButton("Done");
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(printBtn);
        buttonPanel.add(doneBtn);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        printBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Receipt printed!"));
        doneBtn.addActionListener(e -> showScreen("MAIN_MENU"));
        
        return panel;
    }
    
    private JPanel createThankYouScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(0, 102, 204));
        panel.setBorder(BorderFactory.createEmptyBorder(50, 20, 20, 20));
        
        JLabel thankYouLabel = new JLabel("THANK YOU FOR BANKING WITH US", JLabel.CENTER);
        thankYouLabel.setFont(new Font("Arial", Font.BOLD, 20));
        thankYouLabel.setForeground(Color.WHITE);
        
        // FIX: use JLabel (not J.Label) and place components in different regions
        JLabel securityLabel = new JLabel("Please take your card", JLabel.CENTER);
        securityLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        securityLabel.setForeground(Color.YELLOW);
        
        JButton newTransactionBtn = new JButton("New Transaction");
        newTransactionBtn.addActionListener(e -> {
            currentUser = null;
            showScreen("WELCOME");
        });
        
        panel.add(thankYouLabel, BorderLayout.NORTH);
        panel.add(securityLabel, BorderLayout.CENTER);
        panel.add(newTransactionBtn, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createErrorScreen() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(50, 20, 20, 20));
        
        JLabel errorLabel = new JLabel("TRANSACTION ERROR", JLabel.CENTER);
        errorLabel.setFont(new Font("Arial", Font.BOLD, 20));
        errorLabel.setForeground(Color.RED);
        
        JLabel messageLabel = new JLabel("Please contact customer service", JLabel.CENTER);
        
        JButton retryBtn = new JButton("Retry");
        JButton cancelBtn = new JButton("Cancel");
        
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        buttonPanel.add(retryBtn);
        buttonPanel.add(cancelBtn);
        
        panel.add(errorLabel, BorderLayout.NORTH);
        panel.add(messageLabel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        retryBtn.addActionListener(e -> showScreen("MAIN_MENU"));
        cancelBtn.addActionListener(e -> showScreen("THANK_YOU"));
        
        return panel;
    }
    
    // Business Logic Methods
    private void authenticateUser() {
        String enteredPin = new String(pinField.getPassword());
        String correctPin = users.get("1234567890123456"); // For demo, using first user
        
        if (enteredPin.equals(correctPin)) {
            currentUser = "1234567890123456";
            balance = balances.get(currentUser);
            showScreen("MAIN_MENU");
        } else {
            JOptionPane.showMessageDialog(this, "Invalid PIN! Please try again.");
            pinField.setText("");
        }
    }
    
    private void processWithdrawal(double amount) {
        if (amount > balance) {
            showError("Insufficient funds!");
            return;
        }
        
        if (amount <= 0) {
            showError("Invalid amount!");
            return;
        }
        
        transactionAmount = amount;
        currentTransactionType = "WITHDRAWAL";
        showScreen("PROCESSING");
    }
    
    private void completeTransaction() {
        if ("WITHDRAWAL".equals(currentTransactionType)) {
            balance -= transactionAmount;
            balances.put(currentUser, balance);
            addTransaction(currentUser, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), 
                          "WITHDRAWAL", transactionAmount, balance);
            generateReceipt("WITHDRAWAL", transactionAmount);
        } else if ("DEPOSIT".equals(currentTransactionType)) {
            balance += transactionAmount;
            balances.put(currentUser, balance);
            addTransaction(currentUser, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), 
                          "DEPOSIT", transactionAmount, balance);
            generateReceipt("DEPOSIT", transactionAmount);
        }
    }

    private void completeDeposit() {
        // simple immediate deposit without processing screen
        balance += transactionAmount;
        balances.put(currentUser, balance);
        addTransaction(currentUser, new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()), 
                      "DEPOSIT", transactionAmount, balance);
        generateReceipt("DEPOSIT", transactionAmount);
    }
    
    private void generateReceipt(String transactionType, double amount) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String receipt = "GLOBAL BANK ATM\n";
        receipt += "====================\n";
        receipt += "Date: " + sdf.format(new Date()) + "\n";
        receipt += "Card: " + (currentUser != null ? maskCardNumber(currentUser) : "N/A") + "\n";
        receipt += "Transaction: " + transactionType + "\n";
        
        if (amount > 0) {
            receipt += "Amount: $" + String.format("%.2f", amount) + "\n";
        }
        
        receipt += "Available Balance: $" + String.format("%.2f", balance) + "\n";
        receipt += "Transaction ID: " + generateTransactionId() + "\n";
        receipt += "====================\n";
        receipt += "Thank you for banking with us!";
        
        if (receiptArea != null) {
            receiptArea.setText(receipt);
        }
        showScreen("RECEIPT");
    }
    
    private void addTransaction(String user, String date, String type, double amount, double newBalance) {
        String transaction = String.format("%s %-10s $%-8.2f $%.2f", 
                                          date, type, amount, newBalance);
        ArrayList<String> list = transactionHistory.get(user);
        if (list == null) {
            list = new ArrayList<>();
            transactionHistory.put(user, list);
        }
        list.add(0, transaction); // Add to beginning
        
        // Keep only last 10 transactions
        if (list.size() > 10) {
            list.remove(10);
        }
    }
    
    private String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) return "";
        return "---" + cardNumber.substring(Math.max(0, cardNumber.length() - 4));
    }
    
    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis();
    }
    
    private void showScreen(String screenName) {
        cardLayout.show(mainPanel, screenName);
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CompleteATMSystem().setVisible(true);
        });
    }
}