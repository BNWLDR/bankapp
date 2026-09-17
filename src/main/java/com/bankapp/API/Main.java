package com.bankapp.api;

import com.bankapp.persistence.BankDAO;
import com.bankapp.persistence.BankDAOImpl;
import com.bankapp.service.BankService;
import com.bankapp.service.BankServiceImpl;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        BankDAO dao = new BankDAOImpl();
        BankService service = new BankServiceImpl(dao);
        new BankRepl(service).run();
    }
}