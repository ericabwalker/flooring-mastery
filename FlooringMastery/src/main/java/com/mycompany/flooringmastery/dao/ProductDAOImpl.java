/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.dao;

import com.mycompany.flooringmastery.dto.Product;
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
public class ProductDAOImpl implements ProductDAO {

    public static final String PRODUCT_FILE = "./Data/Products.txt";
    public static final String DELIMITER = ",";

    private Map<String, Product> products = new HashMap<>();

    @Override
    public Product getProduct(String product) {
        return products.get(product.toLowerCase());
    }

    @Override
    public boolean doesProductExist(String product) {
        return products.containsKey(product.toLowerCase());
    }

    @Override
    public void loadProducts() throws FlooringPersistenceException {
        Scanner scanner;

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(PRODUCT_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringPersistenceException(
                    "Could not load products into memory.", e);
        }
        String currentLine;
        Product currentProduct;

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentProduct = unmarshallProduct(currentLine);

            products.put(currentProduct.getProductType().toLowerCase(), currentProduct);
        }
        scanner.close();

    }

    private Product unmarshallProduct(String productAsText) throws FlooringPersistenceException {
        String[] productTokens = productAsText.split(DELIMITER);

        String productType = productTokens[0];
        BigDecimal costPerSqFt = new BigDecimal(productTokens[1]);
        BigDecimal laborCostPerSqFt = new BigDecimal(productTokens[2]);

        Product productFromFile = new Product(productType, costPerSqFt, laborCostPerSqFt);

        return productFromFile;
    }

}
