package com.bankapp.api;

import java.math.BigDecimal;
import java.util.Scanner;
import com.bankapp.service.*;
import com.bankapp.domain.Transaction;
import com.bankapp.exception.DatabaseException;


public class BankRepl {

    private int id = 0;

    private final BankService service;
    private final Scanner scanner = new Scanner(System.in);
    
    public BankRepl(BankService service) {
        this.service = service;
    }

    public void run() {
        while (true) {
            System.out.print("> ");
            String command = scanner.nextLine().trim();

            if (command.equals("exit")) {
                if (id != 0) {
                    id = 0; //doesn't update session id as this ends the program
                }
                return;
            }

            try {
                handle(command);
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (DatabaseException e) {
                System.out.println("Service unavailable. Please try again later.");
            }
        }
    }

    private void handle(String command) {
        if (id != 0) { //check if user is logged in
            switch (command) {
                case "logout" -> id = 0; //Updates id -> 0 when logging out, but doesn't exit
                case "balance" -> System.out.printf("Account ID: %d | Current Balance: %d%n", id, service.getBalance(id));
                case "withdraw" -> withdraw(); 
                case "deposit" -> deposit();
                case "transfer" -> transfer();
                case "transactions" -> printHistory();
                case "list" -> {
                    System.out.println("Available commands:");
                    System.out.println("  balance");
                    System.out.println("  deposit");
                    System.out.println("  withdraw");
                    System.out.println("  transfer");
                    System.out.println("  transactions");
                    System.out.println("  list"); 
                    System.out.println("  exit");
                }
                default -> System.out.println("Sorry, that is not a valid command. Type 'list' to see the available commands.");
            }
        } else { //if user is not logged in
            switch (command) {
                //calls reader method and passes it to service layer to check 
                
                case "login" -> id = login(); //updates session information to show user is logged in under id
                case "register" -> register();
                case "list" -> {
                    System.out.println("Available commands:");
                    System.out.println("  login");
                    System.out.println("  register");
                    System.out.println("  list");
                    System.out.println("  exit");
                }                
                default -> System.out.println("Sorry, that is not a valid command. Type 'list' to see the available commands.");

            }
        }
    }

   private int login() {
    int accountId = readInt("ID: ");
    String pin = readString("PIN: ");
    int res = service.login(accountId, pin);   // throws if invalid, so id is not updated
    System.out.println("Login successful.");
    return res;
    }

    private void register() {
        String pin = readString("Enter a PIN for your new account: ");
        System.out.println("Account created successfully! Your account ID is: " + service.createAccount(pin));
    }

    private void deposit() {
        System.out.printf("Current Balance: %d%n", service.getBalance(id));
        BigDecimal amount = readAmount("Amount to deposit: ");
        BigDecimal balance = service.deposit(id, amount);
        System.out.printf("Deposit succeeded! Account ID: %d | Current Balance: %d%n", id, balance);
    }

    private void withdraw() {
        System.out.printf("Current Balance: %d%n", service.getBalance(id));
        BigDecimal amount = readAmount("Amount to withdraw: ");
        BigDecimal balance = service.withdraw(id, amount);
        System.out.printf("Withdraw succeeded! Account ID: %d | Current Balance: %d%n", id, balance);
    }

    private void transfer() {
        System.out.printf("Current Balance: %d%n", service.getBalance(id));
        BigDecimal amount = readAmount("Amount to transfer: ");
        int relId = readInt("Receiving Account: ");
        service.transfer(id, relId, amount);
        System.out.printf("Transfer succeeded! Account ID: %d | Current Balance: %d%n", id, service.getBalance(id));
    }

    private void printHistory() {
        Transaction[] transactions = service.history(id);
        if (transactions.length == 0) {
            System.out.println("No transactions found.");
        }
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }


    private int readInt(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Please enter a whole number.");
        }
    }

    private BigDecimal readAmount(String prompt) {
    System.out.print(prompt);
    try {
        return new BigDecimal(scanner.nextLine().trim());
    } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Please enter an amount like 25 or 25.50.");
    }
}

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
