/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.dao;

import com.mycompany.flooringmastery.dto.Product;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ericabenton
 */
public class ProductDAOStubImpl implements ProductDAO {

    private Product wood;
    private Product tile;
    private Product bamboo;
    private Product spanishTile;
    private Map<String, Product> products = new HashMap<>();

    public ProductDAOStubImpl() {
        this.wood = new Product("Wood", new BigDecimal("5.15"), new BigDecimal("4.75"));
        this.tile = new Product("Tile", new BigDecimal("2.00"), new BigDecimal("4.00"));
        this.bamboo = new Product("Bamboo", new BigDecimal("1.00"), new BigDecimal("3.50"));
        this.spanishTile = new Product("Spanish Tile", new BigDecimal("3.50"), new BigDecimal("2.50"));
        products.put("Wood".toLowerCase(), wood);
        products.put("Tile".toLowerCase(), tile);
        products.put("Bamboo".toLowerCase(), bamboo);
        products.put("Spanish Tile".toLowerCase(), spanishTile);
    }

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
    }

}
