/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.dao;

import com.mycompany.flooringmastery.dto.TaxRate;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author ericabenton
 */
public class TaxDAOImpl implements TaxDAO {

    public static final String TAX_FILE = "./Data/Taxes.txt";
    public static final String DELIMITER = ",";

    private Map<String, TaxRate> taxes = new HashMap<>();

    @Override
    public TaxRate getTaxRateByState(String state) {
        return taxes.get(state.toLowerCase());
    }

    @Override
    public boolean doesStateExist(String state) {
        return taxes.containsKey(state.toLowerCase());
    }

    @Override
    public void loadTaxRates() throws FlooringPersistenceException {
        Scanner scanner;

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(TAX_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringPersistenceException(
                    "Could not load states & tax rates into memory.", e);
        }
        String currentLine;
        TaxRate currentState;

        //to skip header line in text file
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentState = unmarshallTaxRate(currentLine);

            taxes.put(currentState.getStateAbbreviation().toLowerCase(), currentState);
        }
        scanner.close();

    }

    private TaxRate unmarshallTaxRate(String taxRateAsText) throws FlooringPersistenceException {
        String[] taxTokens = taxRateAsText.split(DELIMITER);

        String stateAbbreviation = taxTokens[0];
        String stateName = taxTokens[1];
        BigDecimal taxRate = new BigDecimal(taxTokens[2]);

        TaxRate taxRateFromFile = new TaxRate(stateAbbreviation, stateName, taxRate);

        return taxRateFromFile;
    }

}
