package cz.vsb.ekf.urm0009.system;

import cz.vsb.ekf.urm0009.accounts.Account;
import cz.vsb.ekf.urm0009.bank.Bank;
import cz.vsb.ekf.urm0009.bank.BankList;
import cz.vsb.ekf.urm0009.clients.Client;
import cz.vsb.ekf.urm0009.exceptions.NotEnoughMoney;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Application {
    private static final int numberOfRandomlyGeneratedAccounts = 10;
    private static final SecureRandom random = new SecureRandom();

    public static void run() {
        System.out.println("Welcome to your personal banking system");
        System.out.println("Please, choose your bank from the following: ");

        System.out.println();
        boolean correctBankName = true;
        String bankNameString = "";
        Bank bank = null;
        while (correctBankName) {
            System.out.println(Arrays.asList(BankList.values()));
            Scanner scanBankName = new Scanner(System.in);
            bankNameString = scanBankName.nextLine().toUpperCase(Locale.ROOT);
            try {
                bank = new Bank(BankList.valueOf(bankNameString));
                correctBankName = false;
            } catch (IllegalArgumentException ex) {
                System.err.println("The name you have typed in is incorrect, you have to type in correctly the name of your desired bank.\nPay attention to underscores, case sensitivity is not important, try again:");
            }
        }
        try {
            Thread.sleep(500);
            for (int i = 0; i < 3; i++) {
                System.out.println(".");
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Congratulations, you have successfully created your bank.");
        try {
            Thread.sleep(500);
            for (int i = 0; i < 3; i++) {
                System.out.println(".");
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("The creation of your bank made such fuss that you already have " + numberOfRandomlyGeneratedAccounts + " clients and accounts! You will be able to view them in the following menu:");
        try {
            Thread.sleep(500);
            for (int i = 0; i < 3; i++) {
                System.out.println(".");
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < numberOfRandomlyGeneratedAccounts; i++) {
            bank.createAdultClient(names[random.nextInt(names.length)], ThreadLocalRandom.current().nextInt(20, 70));
            bank.createAdultAccount(bank.getClient(i), new BigDecimal(ThreadLocalRandom.current().nextInt(1000, 100000)), new BigDecimal(ThreadLocalRandom.current().nextDouble(0.1, 0.9)));
        }
        //System.out.println(bank);
        System.out.println("Type out the following commands as you please:\n1. create client\n" +
                "2. create account\n" +
                "3. read clients\n" +
                "4. read accounts\n" +
                "5. read bank\n" +
                "6. read richest clients (reads top 5 richest clients)\n" +
                "7. deposit\n" +
                "8. withdraw\n" +
                "9. exit\n" +
                "10. show menu");
        // deleting accounts and clients if client is deleted type out which account partners have been changed
        // 6.  -> lambda, comparator
        boolean runApp = true;
        while (runApp) {
            System.out.println("\nType out the commands as you please, type \"show menu\" to show commands: ");
            Scanner option = new Scanner(System.in);
            String optionTemp = option.nextLine().toUpperCase(Locale.ROOT);
            String[] optionSplit = optionTemp.split("\\s+");
            if (optionSplit[0].equals("CREATE")) {
                try {
                    if (optionSplit[1].equals("CLIENT")) {
                        String clientName = "";
                        int clientAge;
                        boolean typeInfo = true;
                        int attemptsLeft = 3;
                        while (typeInfo) {
                            try {
                                if ((clientName.equals(""))) {
                                    System.out.println("Please enter name: ");
                                    Scanner scanName = new Scanner(System.in);
                                    clientName = scanName.nextLine();
                                }
                                System.out.println("Please enter age: ");
                                Scanner scanAge = new Scanner(System.in);
                                clientAge = scanAge.nextInt();
                                if (clientAge < 18) {
                                    bank.createUnderageClient(clientName, clientAge);
                                    System.out.println("Client " + clientName + " has been created");
                                    break;
                                } else {
                                    bank.createAdultClient(clientName, clientAge);
                                    System.out.println("Client " + clientName + " has been created");
                                    break;
                                }
                            } catch (InputMismatchException ex) {
                                attemptsLeft--;
                                if (attemptsLeft == 0) {
                                    System.out.println("Client creation request was terminated.");
                                    break;
                                }
                                System.err.println("You made a mistake trying to type the age of the client, you have " + attemptsLeft + " attempt/s left, try again:");

                            }
                        }

                    } else if (optionSplit[1].equals("ACCOUNT")) {
                        boolean accountCreated = false;
                        int attemptsLeft = 3;
                        boolean typeInfo = true;
                        while (typeInfo) {
                            try {
                                System.out.println(bank.getClient());
                                System.out.println("Enter the id of the client you want to create an account for: ");
                                Scanner scanAccount = new Scanner(System.in);
                                int clientID = scanAccount.nextInt();
                                if (((bank.getClient(clientID - 1).getAge()) < 18)) {
                                    System.out.println("This account is required to have at least one mandatory adult partner.");
                                    Client[] partners = partners = Application.getPartners(bank, bank.getClient(clientID - 1));
                                    System.out.println("Enter account's balance: ");
                                    Scanner scanDetails = new Scanner(System.in);
                                    BigDecimal balance = scanDetails.nextBigDecimal();
                                    System.out.println("Enter account's interest: ");
                                    BigDecimal interest = scanDetails.nextBigDecimal();
                                    bank.createUnderageAccount(bank.getClient(clientID - 1), balance, interest, partners);
                                    System.out.println("Your account has been successfully created!");
                                    accountCreated = true;
                                    break;
                                } else {
                                    Client[] partners = null;
                                    System.out.println("Will the client have any partners? Y/n");
                                    Scanner scanAnswer = new Scanner(System.in);
                                    String answer = scanAnswer.nextLine();
                                    if ("Y".equals(answer.toUpperCase(Locale.ROOT))) {
                                        partners = Application.getPartners(bank, bank.getClient(clientID - 1));
                                    }
                                    System.out.println("Enter account's balance: ");
                                    Scanner scanDetails = new Scanner(System.in);
                                    BigDecimal balance = scanDetails.nextBigDecimal();
                                    System.out.println("Enter account's interest: ");
                                    BigDecimal interest = scanDetails.nextBigDecimal();
                                    if (partners == null) {
                                        bank.createAdultAccount(bank.getClient(clientID - 1), balance, interest);
                                        System.out.println("Your account has been successfully created.");
                                        accountCreated = true;
                                        break;
                                    } else {
                                        bank.createAdultAccount(bank.getClient(clientID - 1), balance, interest, partners);
                                        System.out.println("Your account has been successfully created.");
                                        accountCreated = true;
                                        break;
                                    }


                                }
                            } catch (IllegalArgumentException ex) {
                                System.err.println("Client cannot be his/hers own partner");
                            } catch (InputMismatchException ex) {
                                System.err.println("You made a mistake trying to type the age of the client.");
                            } finally {
                                if (!accountCreated) {
                                    attemptsLeft -= 1;
                                    if (attemptsLeft == 0) {
                                        System.err.println("Your attempt to create an account was terminated.");
                                        break;
                                    }
                                    System.err.println("You have " + attemptsLeft + " attempts left to create your desired account.");
                                }
                            }
                        }

                    } else {
                        System.out.println("Please specify what do you want to create: \"create client\" or \"create account\"");
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.err.println("You have to specify what you want to create, for example: \"create client\" or \"create account\"");
                }
            } else if (optionSplit[0].equals("READ")) {
                try {
                    if (optionSplit[1].equals("CLIENT") || (optionSplit[1].equals("CLIENTS"))) {

                        System.out.println(bank.getClient());
                    } else if (optionSplit[1].equals("ACCOUNT") || (optionSplit[1].equals("ACCOUNTS"))) {
                        System.out.println(bank.getAccount());
                    } else if (optionSplit[1].equals("BANK")) {
                        System.out.println(bank);
                    } else if (optionSplit[1].equals("RICH") || (optionSplit[1].equals("RICHEST"))) {
                        Collections.sort(bank.getAccount(), ((a1, a2) -> (a2.getBalance()).intValue() - (a1.getBalance()).intValue()));
                        int clientNumber = 0;
                        try {
                            for (int i = 0; i < 5; i++) {
                                bank.getAccount(i);
                                clientNumber++;
                            }
                            for (int i = 0; i < 5; i++) {
                                System.out.println(bank.getAccount(i));
                            }
                            Collections.sort(bank.getAccount(), ((a1, a2) -> (a1.getAccountID()) - (a2.getAccountID())));
                        } catch (IndexOutOfBoundsException ex) {
                            System.err.println("Writing only " + clientNumber + " accounts, bank doesn't have enough accounts to print out 5...\n");
                            for (Account tempA : bank.getAccount()) {
                                System.out.println(tempA);
                            }
                            Collections.sort(bank.getAccount(), ((a1, a2) -> (a1.getAccountID()) - (a2.getAccountID())));
                        }

                    } else System.out.println("Please type correctly: \"read clients\" or \"read accounts\"");
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.err.println("You have to specify what you want to read, for example: \"read clients\" or \"read accounts\"");
                }
            } else if (optionSplit[0].equals("DEPOSIT")) {
                Scanner scannerForDeposit = new Scanner(System.in);
                try {
                    System.out.println(bank.getAccount());
                    System.out.println("Please type in the id of the account you wish to deposit to: ");
                    int accountID = scannerForDeposit.nextInt();
                    System.out.println("Please enter the amount you wish to deposit:");
                    scannerForDeposit = new Scanner(System.in);
                    BigDecimal depositValue = scannerForDeposit.nextBigDecimal();
                    if (depositValue.compareTo(new BigDecimal(0)) < 0) {
                        throw new InputMismatchException();
                    }
                    bank.getAccount(accountID - 1).deposit(depositValue);
                    System.out.println(depositValue + " has successfully been added to your balance. Total: " + bank.getAccount((accountID - 1)).getBalance());
                } catch (InputMismatchException ex) {
                    System.err.println("A positive number value was expected");
                } catch (IndexOutOfBoundsException ex) {
                    System.err.println("The ID you were trying to deposit to is not existent.");
                }
            } else if (optionSplit[0].equals("WITHDRAW")) {
                Scanner scannerForWithdrawal = new Scanner(System.in);
                try {
                    System.out.println(bank.getAccount());
                    System.out.println("Please type in the id of the account you wish to withdraw from: ");
                    int accountID = scannerForWithdrawal.nextInt();
                    System.out.println("Please enter the amount you wish to withdraw:");
                    scannerForWithdrawal = new Scanner(System.in);
                    BigDecimal withdrawValue = scannerForWithdrawal.nextBigDecimal();
                    if (withdrawValue.compareTo(new BigDecimal(0)) < 0) {
                        throw new InputMismatchException();
                    }
                    bank.getAccount(accountID - 1).withdraw(withdrawValue);
                    System.out.println(withdrawValue + " has successfully been added to your balance. Total: " + bank.getAccount((accountID - 1)).getBalance());
                } catch (IndexOutOfBoundsException ex) {
                    System.err.println("The ID you were trying to withdraw from is not existent");
                } catch (InputMismatchException ex) {
                    System.err.println("You cannot withdraw negative value!");
                }
            } else if (optionSplit[0].equals("SHOW")) {
                System.out.println("Type out the following commands as you please:\n1. create client\n" +
                        "2. create account\n" +
                        "3. read clients\n" +
                        "4. read accounts\n" +
                        "5. read bank\n" +
                        "6. read richest clients\n" +
                        "7. deposit\n" +
                        "8. withdraw\n" +
                        "9. exit\n" +
                        "10. show menu");
            } else if (optionSplit[0].equals("EXIT")) {
                runApp = false;
                System.exit(0);
            } else {
                System.err.println("You must have typed something wrong, type again: ");
            }


        }
    }

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    private static String[] names = {
            "Šarlota",
            "Dobromila",
            "Ljuba",
            "Stela",
            "Emílie",
            "Michaela",
            "Hanuš",
            "Svatopluk",
            "Oskar",
            "Herbert",
            "Vratislav",
            "Kryštof"
    };

    public static Client[] getPartners(Bank bank, Client notPartner) {
        {
            System.out.println("How many partners, which already are registered in this bank, will this account have?");
            Scanner scanAnswer = new Scanner(System.in);
            int numOfPartners = scanAnswer.nextInt();
            Client[] client = new Client[numOfPartners];
            System.out.println(bank.getClient());
            System.out.println("Select " + numOfPartners + " partner IDs from the previous list: ");
            int tempCounter = numOfPartners;
            for (int i = 0; i < numOfPartners; i++) {
                int partnerID = scanAnswer.nextInt();
                tempCounter -= 1;
                System.out.println(tempCounter + " IDs left: ");
                client[i] = (bank.getClient(partnerID - 1));
            }
            for (int i = 0; i < numOfPartners; i++) {
                if (client[i].equals(notPartner)) {
                    throw new IllegalArgumentException();
                }
            }
            return client;
        }
    }
}
