/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.dao;

import com.mycompany.flooringmastery.dto.Order;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ericabenton
 */
public interface OrderDAO {
    
    public void loadAllOrders() throws FlooringPersistenceException;

    public Order storeOrder(Order newOrder);

    public List<Order> getAllOrders(Date userDate);

    public Order getOrderByDateAndNum(Date userDate, Integer userOrderNum);

    public void removeAnOrder(Order orderToRemove);
    
    public void writeOrderToFile() throws FlooringPersistenceException;

}
