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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author ericabenton
 */
public class OrderDAOFileImplTest {

    OrderDAOFileImpl testDAO;

    public OrderDAOFileImplTest() {
        testDAO = new OrderDAOFileImpl();
        try {
            testDAO.loadAllOrders();
        } catch (FlooringPersistenceException e) {
        }
    }

    @Test
    public void testStoreOrder() throws ParseException {
        //ARRANGE
        Map<Date, Map<Integer, Order>> ordersByDate = new HashMap<>();

        Date userDate = new SimpleDateFormat("MMddyyyy").parse("03042023");
        Order orderToAdd = new Order(userDate, "Erica Walker", "KY", "Tile", new BigDecimal("150.00"));

        testDAO = new OrderDAOFileImpl(ordersByDate);

        //ACT
        Assertions.assertEquals(0, orderToAdd.getOrderNumber());
        Order testAddAnOrder = testDAO.storeOrder(orderToAdd);

        //ASSERT
        Assertions.assertTrue(testAddAnOrder.getOrderNumber() > 0);
        Assertions.assertEquals(1, testAddAnOrder.getOrderNumber());
        Assertions.assertEquals(orderToAdd, testAddAnOrder);
        Assertions.assertEquals(1, ordersByDate.size());
        Assertions.assertEquals(1, ordersByDate.get(userDate).size());
        Assertions.assertEquals(orderToAdd, ordersByDate.get(userDate).get(testAddAnOrder.getOrderNumber()));
    }

    @Test
    public void testGetAllOrders() throws FlooringPersistenceException, ParseException {
        //ARRANGE
        Map<Date, Map<Integer, Order>> testMapName = new HashMap<>();

        Date userDate = new SimpleDateFormat("MMddyyyy").parse("03042023");
        Order orderOne = new Order(userDate, "Erica Walker", "KY", "Tile", new BigDecimal("150.00"));
        Order orderTwo = new Order(userDate, "Bret Walker", "KY", "Tile", new BigDecimal("150.00"));

        testDAO = new OrderDAOFileImpl(testMapName);
        testDAO.storeOrder(orderOne);
        testDAO.storeOrder(orderTwo);

        //ACT
        List<Order> testGetAllList = testDAO.getAllOrders(userDate);

        //ASSERT
        Assertions.assertEquals(2, testGetAllList.size());
    }

    @Test
    public void testRemoveAnOrder() throws ParseException {
        //ARRANGE
        Map<Date, Map<Integer, Order>> testMapName = new HashMap<>();

        Date userDate = new SimpleDateFormat("MMddyyyy").parse("03042023");
        Order orderOne = new Order(userDate, "Erica Walker", "KY", "Tile", new BigDecimal("150.00"));
        Order orderTwo = new Order(userDate, "Bret Walker", "KY", "Tile", new BigDecimal("150.00"));

        testDAO = new OrderDAOFileImpl(testMapName);
        testDAO.storeOrder(orderOne);
        testDAO.storeOrder(orderTwo);

        //ACT
        testDAO.removeAnOrder(orderTwo);

        //ASSERT
        Map<Integer, Order> ordersForDate = testMapName.get(userDate);
        Assertions.assertEquals(1, ordersForDate.size());
        Assertions.assertTrue(ordersForDate.containsValue(orderOne));

    }

    @Test
    public void testGetOrderByDateAndNum() throws ParseException {
        //ARRANGE
        Map<Date, Map<Integer, Order>> testMapName = new HashMap<>();
        Map<Integer, Order> innerMap = new HashMap<>();

        Date userDate = new SimpleDateFormat("MMddyyyy").parse("03042023");
        Order orderOne = new Order(1, userDate, "Erica Walker", "KY", "Tile", new BigDecimal("150.00"));
        innerMap.put(orderOne.getOrderNumber(), orderOne);
        testMapName.put(userDate, innerMap);
        testDAO.storeOrder(orderOne);

        //ACT
        Order testOrderOne = testDAO.getOrderByDateAndNum(userDate, orderOne.getOrderNumber());

        //ASSERT
        Assertions.assertEquals(orderOne, testOrderOne);
    }

}
