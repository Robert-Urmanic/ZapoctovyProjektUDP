package cz.vsb.ekf.urm0009.accounts;

import cz.vsb.ekf.urm0009.clients.Client;
import cz.vsb.ekf.urm0009.exceptions.NotEnoughMoney;

import java.math.BigDecimal;

public class AdultAccount extends Account {
    public static final int charge = 50;

    public AdultAccount(int accountID, Client client, BigDecimal balance, BigDecimal interestRate) {
        super(accountID, client, balance, interestRate);
    }

    public AdultAccount(int accountID, Client client, BigDecimal balance, BigDecimal interestRate, Client... partner) {
        super(accountID, client, balance, interestRate, partner);
    }

    @Override
    public void withdraw(BigDecimal money) {
        try {
            if ((getBalance()).compareTo(money.add(new BigDecimal(charge))) < 0) {
                throw new NotEnoughMoney("Not enough balance on account to withdraw");
            }
            else setBalance(getBalance().subtract(money));
        } catch (NotEnoughMoney ex) {
            System.err.println(ex + ", your balance stays at previous " + getBalance() + ". Don't forget to count in your fee of value: " + charge + ".");
        }
    }
}
