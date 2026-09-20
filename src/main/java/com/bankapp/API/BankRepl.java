package com.bankapp.api;

import java.util.Scanner;
import com.bankapp.service.*;


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
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }



        }
    }

    private void handle(String command) {
        if (id != 0) { //check if user is logged in
            switch (command) {
                case "logout" -> id = 0; //Updates id -> 0 when logging out, but doesn't exit
                case "balance" -> service.getBalance(id);
                case "withdraw" -> service.updateBalance(readTransaction("withdraw"));
                case "deposit" -> service.updateBalance(readTransaction("deposit"));
                case "transfer" -> service.transfer(readTransaction("transfer"));
                case "transactions" -> service.history(id);
                case "list" -> System.out.print("");
                default -> System.out.println("Sorry, that is not a valid command.");

            }
        }
        else { //if user is not logged in
            switch (command) {
                //calls reader method and passes it to service layer to check 
                
                case "login" -> id = service.login(readLogin()); //updates session information to show user is logged in under id
                case "register" -> service.createAccount(readRegistration());
                case "list" -> System.out.print("");
                default -> System.out.println("Sorry, that is not a valid command.");

            }
        }
    }

    private int[] readLogin() {
        int[] res = new int[2];
        System.out.print("ID: ");
        res[0] = scanner.nextInt();
        System.out.print("PIN: ");
        res[1] = scanner.nextInt();
        return res;

    }

    private String[] readTransaction(String type) {
        service.getBalance(id);
        String[] res = new String[5];
        
        res[0] = Integer.toString(id); //id
        res[1] = type;
        System.out.print("Amount to " + type + ": ");
        res[2] = scanner.nextLine();       
        if (type == "transfer") {
            System.out.print("Receiving Account: ");
            res[3] = scanner.nextLine();
        }
        else {
            res[3] = "";
        }

        return res;
    }

    private int readRegistration() {
        int res = 0;
        System.out.print("Enter a PIN for your new account: ");
        res = scanner.nextInt();
        
        return res;

    }
}
