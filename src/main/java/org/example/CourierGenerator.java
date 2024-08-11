package org.example;

import java.util.Random;

public class CourierGenerator {
    private static final String[] login = new String[]{"Kate12565221", "Alex633472", "Sasha26321", "Iv54665An44382"};
    private static final String[] password = new String[]{"1234", "1111", "6145102"};
    private static final String[] firstName = new String[]{"Екатерина", "Александр", "Александра", "Иван"};

    public static Courier createNewCourier() {
        int randomLogin  = new Random().nextInt(login.length);
        int randomPassword = new Random().nextInt(password.length);
        int randomFirstName = new Random().nextInt(firstName.length);

        return new Courier(login[randomLogin], password[randomPassword], firstName[randomFirstName]);
    }
}
