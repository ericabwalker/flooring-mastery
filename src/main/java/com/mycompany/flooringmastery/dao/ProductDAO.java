/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.dao;

import com.mycompany.flooringmastery.dto.Product;

/**
 *
 * @author ericabenton
 */
public interface ProductDAO {
    
    public Product getProduct(String product);
    
    public boolean doesProductExist(String product);
    
    public void loadProducts() throws FlooringPersistenceException;
    
}
