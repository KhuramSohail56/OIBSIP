import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

class Transaction {
    private String type;
    private double amount;
    private String timestamp;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    @Override
    public String toString() {
        return String.format("[%s] %-12s: $%.2f", timestamp, type, amount);
    }
}

class Account {
    private double balance;
    private List<Transaction> transactionHistory;

    public Account(double initialBalance) {
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>();
        transactionHistory.add(new Transaction("Initial Deposit", initialBalance));
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            transactionHistory.add(new Transaction("Deposit", amount));
            System.out.println("✅ Successfully deposited: $" + String.format("%.2f", amount));
        } else {
            System.out.println("❌ Invalid deposit amount!");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            transactionHistory.add(new Transaction("Withdrawal", amount));
            System.out.println("✅ Successfully withdrawn: $" + String.format("%.2f", amount));
        } else if (amount > balance) {
            System.out.println("❌ Insufficient Funds! Current Balance: $" + String.format("%.2f", balance));
        } else {
            System.out.println("❌ Invalid withdrawal amount!");
        }
    }

    public void transfer(Account targetAccount, double amount) {
        if (amount > 0 && amount <= balance) {
            this.balance -= amount;
            this.transactionHistory.add(new Transaction("Transfer Out", amount));
            
            targetAccount.balance += amount;
            targetAccount.transactionHistory.add(new Transaction("Transfer In", amount));
            
            System.out.println("✅ Successfully transferred $" + String.format("%.2f", amount) + " to Target Account.");
        } else if (amount > balance) {
            System.out.println("❌ Transfer failed! Insufficient balance.");
        } else {
            System.out.println("❌ Invalid transfer amount!");
        }
    }

    public void printTransactionHistory() {
        System.out.println("\n--- 📜 TRANSACTION HISTORY ---");
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            for (Transaction t : transactionHistory) {
                System.out.println(t);
            }
        }
        System.out.println("Current Balance: $" + String.format("%.2f", balance));
        System.out.println("---------------------------------");
    }
}

class User {
    private String userId;
    private String userPin;
    private Account account;

    public User(String userId, String userPin, double initialBalance) {
        this.userId = userId;
        this.userPin = userPin;
        this.account = new Account(initialBalance);
    }

    public String getUserId() {
        return userId;
    }

    public boolean validatePin(String pin) {
        return this.userPin.equals(pin);
    }

    public Account getAccount() {
        return account;
    }
}

class ATM {
    private User currentUser;
    private User dummyRecipient;
    private Scanner scanner;

    public ATM(User user, User recipient) {
        this.currentUser = user;
        this.dummyRecipient = recipient;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("==========================================");
        System.out.println("    🏦 WELCOME TO ATM SYSTEM INTERFACE    ");
        System.out.println("==========================================");
        
        System.out.print("Enter User ID: ");
        String enteredId = scanner.nextLine();
        
        System.out.print("Enter User PIN: ");
        String enteredPin = scanner.nextLine();

        if (currentUser.getUserId().equals(enteredId) && currentUser.validatePin(enteredPin)) {
            System.out.println("\n✅ Authentication Successful! Welcome, " + currentUser.getUserId());
            showMenu();
        } else {
            System.out.println("\n❌ Invalid User ID or PIN! Access Denied.");
        }
    }

    private void showMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n========== 🏧 ATM MAIN MENU ==========");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.println("=======================================");
            System.out.print("Choose an option (1-5): ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    currentUser.getAccount().printTransactionHistory();
                    break;
                case "2":
                    System.out.print("Enter amount to withdraw: $");
                    double withdrawAmt = parseDoubleInput();
                    currentUser.getAccount().withdraw(withdrawAmt);
                    break;
                case "3":
                    System.out.print("Enter amount to deposit: $");
                    double depositAmt = parseDoubleInput();
                    currentUser.getAccount().deposit(depositAmt);
                    break;
                case "4":
                    System.out.print("Enter Recipient User ID: ");
                    String recipientId = scanner.nextLine();
                    if (recipientId.equals(dummyRecipient.getUserId())) {
                        System.out.print("Enter amount to transfer: $");
                        double transferAmt = parseDoubleInput();
                        currentUser.getAccount().transfer(dummyRecipient.getAccount(), transferAmt);
                    } else {
                        System.out.println("❌ Recipient Account ID not found!");
                    }
                    break;
                case "5":
                    System.out.println("\n🙏 Thank you for using our ATM service. Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("❌ Invalid choice! Please select between 1 and 5.");
            }
        }
    }

    private double parseDoubleInput() {
        try {
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}

public class Main {
    public static void main(String[] args) {
        User primaryUser = new User("khuram123", "2026", 1000.00);
        User recipientUser = new User("user_target", "0000", 200.00);

        ATM atmSystem = new ATM(primaryUser, recipientUser);
        atmSystem.start();
    }
}
