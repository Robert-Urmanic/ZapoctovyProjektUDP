package cz.vsb.ekf.urm0009.accounts;

import cz.vsb.ekf.urm0009.clients.Client;
import cz.vsb.ekf.urm0009.clients.UnderageClient;
import cz.vsb.ekf.urm0009.exceptions.UnderageClientNoPartner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class Account {
    private int accountID;
    private Client client;
    private List<Client> partner = new ArrayList<>();
    private BigDecimal balance;
    private BigDecimal interestRate;

    public Account(int accountID, Client client, BigDecimal balance, BigDecimal interestRate) {
        try {
            if (client instanceof UnderageClient) {
                throw new UnderageClientNoPartner("Underage clients have to have at least one partner that is adult");
            }
        } catch (UnderageClientNoPartner ex) {
            ex.getMessage();
        }
        this.client = client;
        this.balance = balance;
        this.interestRate = interestRate;
        this.accountID = accountID;
        for (Client tempC : partner) {
            this.partner.add(tempC);
        }
    }

    public Account(int accountID, Client client, BigDecimal balance, BigDecimal interestRate, Client... partner) {
        for (Client tempC : partner) {
            this.partner.add(tempC);
        }

        try {
            boolean tempCondition = false;

            for (Client tempC : partner) {
                if (tempC.getAge() >= 18 || client.getAge() >= 18) {
                    tempCondition = true;
                }

            }

            if (tempCondition == false) {
                throw new UnderageClientNoPartner("Underage client has to have at least one partner of age");
            }


        } catch (UnderageClientNoPartner ex) {
            System.err.println(ex);
        }
        this.accountID = accountID;
        this.client = client;
        this.balance = balance;
        this.interestRate = interestRate;
    }

    //public static final Comparator<Account> BY_MONEY =

    public List<Client> getPartner() {
        return partner;
    }

    public Client getClient() {
        return client;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }


    public int getAccountID() {
        return accountID;
    }

    public void deposit(BigDecimal money) {
        balance = balance.add(money);
    }

    public abstract void withdraw(BigDecimal money);

    @Override
    public String toString() {
        return "Account ID: " + accountID + " with balance: " + balance + " is owned by " + getClient();
    }
}

