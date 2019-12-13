/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.dao;

import com.mycompany.flooringmastery.dto.Order;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ericabenton
 */
public class OrderDAOStubImpl implements OrderDAO {

    private Order onlyOrder;
    private List<Order> orderList = new ArrayList<>();

    public OrderDAOStubImpl() throws ParseException {
        Date userDate = new SimpleDateFormat("MMddyyyy").parse("01272020");
        this.onlyOrder = new Order(userDate, "Bret Walker", "KY", "Wood", new BigDecimal("250.00"));
        this.orderList.add(onlyOrder);
    }

    @Override
    public Order storeOrder(Order newOrder) {
        if (newOrder.equals(onlyOrder.getOrderDate())) {
            return onlyOrder;
        } else {
            return null;
        }
    }

    @Override
    public List<Order> getAllOrders(Date userDate) {
        return orderList;
    }

    @Override
    public void removeAnOrder(Order orderToRemove) {
    }

    @Override
    public Order getOrderByDateAndNum(Date userDate, Integer userOrderNum) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void writeOrderToFile() throws FlooringPersistenceException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public void loadAllOrders() throws FlooringPersistenceException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
