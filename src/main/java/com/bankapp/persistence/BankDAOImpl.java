package com.bankapp.persistence;

public class BankDAOImpl implements BankDAO {

        public boolean logout(int id){

            return false;
        }

        public boolean addAccount(int id, int pin) {
            return false;

        }

        public void getBalance(int id){

        }

        public void updateBalance(String id, String type, String amount){

        }

        public void transfer(String id, String type, String amount, String relId){

        }

        public void history(int id){

        }

        public boolean login(int id, int pin){

            return false;
        }

        public boolean register(int id, int pin){

            return false;
        }

        public boolean verifyId(int id){

            return false;
        }

        public boolean verifyBalance(int amount){

            return false;
        }

        public boolean verifyPIN(int pin){

            return false;
        }
    
}
