package cz.vsb.ekf.urm0009.bank;

import cz.vsb.ekf.urm0009.accounts.Account;
import cz.vsb.ekf.urm0009.accounts.UnderageAccount;
import cz.vsb.ekf.urm0009.clients.AdultClient;
import cz.vsb.ekf.urm0009.clients.Client;
import cz.vsb.ekf.urm0009.clients.UnderageClient;
import cz.vsb.ekf.urm0009.accounts.AdultAccount;
import cz.vsb.ekf.urm0009.exceptions.UnderageClientNoPartner;
import cz.vsb.ekf.urm0009.exceptions.UnderageClientWrongAge;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Bank {
    private List<Client> client = new ArrayList<>();
    private List<Account> account = new ArrayList<>();
    private BankList bankName;

    public Bank(BankList bankName) {
        this.bankName = bankName;
    }

    public BankList getBankName() {
        return bankName;
    }

    public Account getAccount(int i) {
        return account.get(i);
    }

    public void addAccount(Account account) {
        this.account.add(account);
    }

    public Client getClient(int i) {
        return client.get(i);
    }

    public void addClient(Client client) {
        this.client.add(client);
    }

    public List<Client> getClient() {
        return client;
    }

    public List<Account> getAccount() {
        return account;
    }

    public void createAdultClient(String name, int age) {
        try {
            if (age < 18) {
                throw new UnderageClientWrongAge("Adult account cannot have underage owner.\n" + name + "'s account with id: " + (this.client.size() + 1) + " wasn't created");
            }
            this.client.add(new AdultClient(name, age, this.client.size() + 1));
        } catch (UnderageClientWrongAge ex) {
            System.err.println(ex);
        }

    }

    public void createUnderageClient(String name, int age) {
        try {
            if (age >= 18) {
                throw new UnderageClientWrongAge("You cannot create an underage client who is older than 18.\n" + name + "'s account with id: " + (this.client.size() + 1) + " wasn't created");
            }
            this.client.add(new UnderageClient(name, age, this.client.size() + 1));
        } catch (UnderageClientWrongAge ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void createUnderageAccount(Client client, BigDecimal balance, BigDecimal interestRate, Client... partner) {
        try {
            boolean hasAdultPartner = false;
            for (Client tempP : partner) {
                if (tempP.equals(client)) {
                    throw new UnsupportedOperationException("Client cannot have himself as partner.");
                }
                if (tempP.getAge() >= 18) {
                    hasAdultPartner = true;
                }
            }
            if (hasAdultPartner == false)
                throw new UnderageClientNoPartner("You are trying to create an account with underage client who has no adult supervisor/ partner. An underage client has to have an adult supervisor.");
            if (!(client instanceof UnderageClient)) {
                throw new IllegalArgumentException("You cannot create an underage account for adult client");
            }
            this.account.add(new UnderageAccount(this.account.size() + 1, client, balance, interestRate, partner));
        } catch (UnderageClientNoPartner ex) {
            System.err.println(ex.getMessage());
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage() + ": " + client.getName() + " with client id: " + client.getClientID());
        } catch (UnsupportedOperationException ex) {
            System.err.println(ex.getMessage() + "Client chosen as partner: " + client.getName() + " with client id: " + client.getClientID());
        }

    }

    public void createAdultAccount(Client client, BigDecimal balance, BigDecimal interestRate, Client... partner) {
        try {
            if (!(client instanceof AdultClient)) {
                throw new IllegalArgumentException("You cannot create an adult account for underage client");
            }
            this.account.add(new AdultAccount(this.account.size() + 1, client, balance, interestRate, partner));
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage() + ": " + client.getName() + " with client id: " + client.getClientID());
        }
    }

    public void createAdultAccount(Client client, BigDecimal balance, BigDecimal interestRate) {
        try {
            if (!(client instanceof AdultClient)) {
                throw new IllegalArgumentException("You cannot create an adult account for underage client");
            }
            this.account.add(new AdultAccount(this.account.size() + 1, client, balance, interestRate));
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage() + ": " + client.getName() + " with client id: " + client.getClientID());
        }
    }

    @Override
    public String toString() {
        String clients = "";
        String accounts = "";
        for (Client tempC : this.client) {
            clients += tempC;
        }
        for (Account tempA : this.account) {
            accounts += tempA;
        }
        return "Your bank " + bankName + " has " + client.size() + " clients and " + account.size() + " accounts.\nClients:\n" + clients + "\nAccounts:\n" + accounts;
    }
}
