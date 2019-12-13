/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.service;

import com.mycompany.flooringmastery.dao.FlooringPersistenceException;
import com.mycompany.flooringmastery.dao.OrderValidationException;
import com.mycompany.flooringmastery.dto.Order;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ericabenton
 */
public interface FlooringService {

    public void loadAllData() throws FlooringPersistenceException;
    
    public Order storeOrder(Order partialOrder) throws FlooringPersistenceException, OrderValidationException;
    
    public void validateOrder(Order order) throws OrderValidationException;

    public List<Order> getAllOrders(Date userDate);

    public void removeAnOrder(Order orderToRemove);

    public Order getOrdersByDateAndNum(Date userDate, Integer userOrderNum);

    public BigDecimal calcMaterialCost(Order orderToCalc);

    public BigDecimal calcLaborCost(Order orderToCalc);

    public BigDecimal calcTax(Order orderToCalc, BigDecimal materialCost, BigDecimal laborCost);

    public BigDecimal calcTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax);
    
    public Order calculateAll(Order orderToCalc);
    
    public void writeOrderToFile() throws FlooringPersistenceException;
    
    public boolean isInProductionMode();

}
