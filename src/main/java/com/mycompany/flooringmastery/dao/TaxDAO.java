/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.dao;

import com.mycompany.flooringmastery.dto.TaxRate;

/**
 *
 * @author ericabenton
 */
public interface TaxDAO {
    
    public TaxRate getTaxRateByState(String state);
    
    public boolean doesStateExist(String state); 
            
    public void loadTaxRates() throws FlooringPersistenceException;
    
}
