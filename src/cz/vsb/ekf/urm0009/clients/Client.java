package cz.vsb.ekf.urm0009.clients;


import cz.vsb.ekf.urm0009.exceptions.UnderageClientNoPartner;
import cz.vsb.ekf.urm0009.exceptions.UnderageClientWrongAge;

import java.util.Comparator;

public class Client {
    private String name;
    private int clientID;
    private int age;

    public Client() {
    }

    public Client(String name, int age, int clientID) {
        setName(name);
        setAge(age);
        setClientID(clientID);
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public int getClientID() {
        return clientID;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Client ID " + clientID +
                " named: " + name +
                ", " + age + " years old.\n";
    }
}
