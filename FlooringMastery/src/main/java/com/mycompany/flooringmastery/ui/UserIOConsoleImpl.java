/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/**
 *
 * @author ericabenton
 */
public class UserIOConsoleImpl implements UserIO {

    Scanner scanner = new Scanner(System.in);

    @Override
    public void print(String message) {
        System.out.println(message);
    }

    @Override
    public double readDouble(String prompt) {
        double result;

        print(prompt);
        result = scanner.nextDouble();
        scanner.nextLine();

        return result;
    }

    @Override
    public double readDouble(String prompt, double min, double max) {
        double result = 0;
        boolean badInput = true;

        while (badInput) {
            result = readDouble(prompt);
            if (result >= min && result <= max) {
                badInput = false;
            } else {
                print("Input neeeds to be >= " + min + " and <= " + max);
            }
        }
        return result;
    }

    @Override
    public float readFloat(String prompt) {
        float result;

        print(prompt);
        result = scanner.nextFloat();
        scanner.nextLine();

        return result;
    }

    @Override
    public float readFloat(String prompt, float min, float max) {
        float result = 0;
        boolean badInput = true;

        while (badInput) {
            result = readInt(prompt);
            if (result >= min && result <= max) {
                badInput = false;
            } else {
                print("Input neeeds to be >= " + min + " and <= " + max);
            }
        }
        return result;
    }

    @Override
    public int readInt(String prompt) {
        int result;

        print(prompt);
        result = scanner.nextInt();
        scanner.nextLine();

        return result;
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        int result = 0;
        boolean badInput = true;

        while (badInput) {
            result = readInt(prompt);
            if (result >= min && result <= max) {
                badInput = false;
            } else {
                print("Input neeeds to be >= " + min + " and <= " + max);
            }
        }
        return result;
    }

    @Override
    public long readLong(String prompt) {
        long result;

        print(prompt);
        result = scanner.nextLong();
        scanner.nextLine();

        return result;
    }

    @Override
    public long readLong(String prompt, long min, long max) {
        long result = 0;
        boolean badInput = true;

        while (badInput) {
            result = readLong(prompt);
            if (result >= min && result <= max) {
                badInput = false;
            } else {
                print("Input neeeds to be >= " + min + " and <= " + max);
            }
        }
        return result;
    }

    @Override
    public String readString(String prompt) {
        String result = "";

        print(prompt);
        result = scanner.nextLine();

        return result;
    }

    @Override
    public Date readDateString(String prompt) {
        Date result = null;
        boolean badInput = true;

        while (badInput) {
            String input = readString(prompt);
            try {
                result = new SimpleDateFormat("MMddyyyy").parse(input);
                badInput = false;
            } catch (ParseException ex) {
                print("Please enter date as MMDDYYYY");
            }
        }
        return result;
    }

    @Override
    public boolean readYesNo(String prompt) {
        while (true) {
            String answer = readString(prompt);
            if (answer.equalsIgnoreCase("Y")) {
                return true;
            } else if (answer.equalsIgnoreCase("N")) {
                return false;
            }
            print("Please enter Y or N");
        }
    }
}
