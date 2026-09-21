# Bank of CLI

Welcome to the Bank of CLI project! In this project, I have built a functional banking application that runs entirely within the terminal complete with logs and testing. This project is to showcase what I've learned in Java, SQL, and Agile development. 

## Setup
1. Start a Postgres server and create the database
I have used a docker container to host the database. You can create a db.properties file in your resources folder like this:
'''
DB_URL=jdbc:postgresql://localhost:5432/bankapp
DB_USER=your-db-user
DB_PASSWORD=your-db-password
'''

2. The queries to set up the tables:
   ```sql
   CREATE TABLE account (
       account_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
       pin VARCHAR(255) NOT NULL,
       balance NUMERIC(12, 2) NOT NULL DEFAULT 0.00
   );

   CREATE TABLE transaction (
       transaction_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
       account_id INTEGER NOT NULL REFERENCES account(account_id),
       type VARCHAR(20) NOT NULL,
       amount NUMERIC(12, 2) NOT NULL,
       related_account_id INTEGER REFERENCES account(account_id),
       timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
   );
   ```

## Run
    ```
    mvn compile org.codehaus.mojo:exec-maven-plugin:3.5.0:java -Dexec.mainClass=com.bankapp.api.Main
    ```

Type `list` at the prompt to see the available commands. Activity is logged to `logs/bankapp.log`.


