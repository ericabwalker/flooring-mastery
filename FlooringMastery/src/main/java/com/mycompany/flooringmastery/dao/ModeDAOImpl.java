/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 *
 * @author ericabenton
 */
public class ModeDAOImpl implements ModeDAO {

    public static final String MODE_FILE = "./Data/Mode.txt";
    public static final String DELIMITER = "::";

    String currentMode;

    @Override
    public void loadMode() throws FlooringPersistenceException {
        Scanner scanner;

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(MODE_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringPersistenceException(
                    "Could not load mode into memory.", e);
        }

        currentMode = unmarshallMode(scanner.nextLine());

        scanner.close();
    }

    @Override
    public boolean isInProductionMode() {
        return this.currentMode.equalsIgnoreCase("Production");
    }

    private String unmarshallMode(String modeAsText) throws FlooringPersistenceException {
        String[] modeTokens = modeAsText.split(DELIMITER);
        return modeTokens[1];
    }

}
