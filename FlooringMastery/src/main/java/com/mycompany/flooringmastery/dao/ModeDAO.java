/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.dao;

/**
 *
 * @author ericabenton
 */
public interface ModeDAO {
    
    public void loadMode() throws FlooringPersistenceException;
    
    public boolean isInProductionMode();
    
}
