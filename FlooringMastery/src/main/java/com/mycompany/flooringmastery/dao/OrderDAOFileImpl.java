/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.dao;

import com.mycompany.flooringmastery.dto.Order;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author ericabenton
 */
public class OrderDAOFileImpl implements OrderDAO {

    public static File[] files = new File("./Orders/").listFiles();
    public static final String DELIMITER = "::";

    private Map<Date, Map<Integer, Order>> orders = new HashMap<>();
    private Integer orderNumber = 1;

    public OrderDAOFileImpl() {
    }

    public OrderDAOFileImpl(Map<Date, Map<Integer, Order>> ordersIn) {
        this.orders = ordersIn;
    }

    @Override
    public void loadAllOrders() throws FlooringPersistenceException {
        for (File file : files) {

            Scanner scanner;

            try {
                scanner = new Scanner(
                        new BufferedReader(
                                new FileReader("./Orders/" + file.getName())));
            } catch (FileNotFoundException e) {
                throw new FlooringPersistenceException(
                        "Could not load orders into memory.", e);
            }
            String currentLine;
            Order currentOrder;

            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            while (scanner.hasNextLine()) {
                currentLine = scanner.nextLine();
                currentOrder = unmarshallOrder(currentLine);
                storeOrder(currentOrder);
                if (currentOrder.getOrderNumber() > orderNumber) {
                    orderNumber = currentOrder.getOrderNumber();
                }
            }
            scanner.close();
        }
        orderNumber++;
    }

    @Override
    public Order storeOrder(Order newOrder) {
        if (newOrder.getOrderNumber() == 0) {
            newOrder.setOrderNumber(orderNumber);
            orderNumber++;
        }
        Map<Integer, Order> ordersByDate = orders.get(newOrder.getOrderDate());
        if (ordersByDate == null) {
            ordersByDate = new HashMap<>();
        }
        ordersByDate.put(newOrder.getOrderNumber(), newOrder);
        orders.put(newOrder.getOrderDate(), ordersByDate);
        return newOrder;
    }

    @Override
    public List<Order> getAllOrders(Date userDate) {
        List<Order> allOrders = new ArrayList<>();

        if (orders.get(userDate) != null) {
            for (Order o : orders.get(userDate).values()) {
                allOrders.add(new Order(o));
            }
        }

        return allOrders;
    }

    @Override
    public Order getOrderByDateAndNum(Date userDate, Integer userOrderNum) {
        Order foundOrder = new Order();

        if (orders.containsKey(userDate)) {
            Map<Integer, Order> ordersByDate = orders.get(userDate);
            if (ordersByDate.containsKey(userOrderNum)) {
                foundOrder = ordersByDate.get(userOrderNum);
            } else {
                return null;
            }
        } else {
            return null;
        }

        return new Order(foundOrder);
    }

    @Override
    public void removeAnOrder(Order orderToRemove) {
        Map<Integer, Order> ordersByDate = orders.get(orderToRemove.getOrderDate());
        ordersByDate.remove(orderToRemove.getOrderNumber());
        orders.put(orderToRemove.getOrderDate(), ordersByDate);
    }

    private Order unmarshallOrder(String orderAsText) throws FlooringPersistenceException {
        String[] orderTokens = orderAsText.split(DELIMITER);

        String orderNumAsString = orderTokens[0];
        int orderNumber = Integer.parseInt(orderNumAsString);
        String customerName = orderTokens[1];
        String state = orderTokens[2];
        BigDecimal taxRate = new BigDecimal(orderTokens[3]);
        String productType = orderTokens[4];
        BigDecimal area = new BigDecimal(orderTokens[5]);
        BigDecimal costPerSqFt = new BigDecimal(orderTokens[6]);
        BigDecimal laborCostPerSqFt = new BigDecimal(orderTokens[7]);
        BigDecimal materialCost = new BigDecimal(orderTokens[8]);
        BigDecimal laborCost = new BigDecimal(orderTokens[9]);
        BigDecimal tax = new BigDecimal(orderTokens[10]);
        BigDecimal total = new BigDecimal(orderTokens[11]);
        Date orderDate;
        try {
            orderDate = new SimpleDateFormat("MMddyyyy").parse(orderTokens[12]);
        } catch (ParseException ex) {
            throw new FlooringPersistenceException("Couldn't unmarshall order date", ex);
        }

        Order orderFromFile = new Order(orderDate, orderNumber, customerName, state, taxRate,
                productType, area, costPerSqFt, laborCostPerSqFt, materialCost,
                laborCost, tax, total);

        return orderFromFile;
    }

    private String marshallOrder(Order toText) {
        String orderString = "";
        orderString = orderString + toText.getOrderNumber() + OrderDAOFileImpl.DELIMITER;
        orderString = orderString + toText.getCustomerName() + OrderDAOFileImpl.DELIMITER;
        orderString = orderString + toText.getState() + OrderDAOFileImpl.DELIMITER;
        orderString = orderString + toText.getTaxRate() + OrderDAOFileImpl.DELIMITER;
        orderString = orderString + toText.getProductType() + OrderDAOFileImpl.DELIMITER;
        orderString = orderString + toText.getArea() + OrderDAOFileImpl.DELIMITER;
        orderString = orderString + toText.getCostPerSqFt() + OrderDAOFileImpl.DELIMITER;
        orderString = orderString + toText.getLaborCostPerSqFt() + OrderDAOFileImpl.DELIMITER;
        orderString = orderString + toText.getMaterialCost() + OrderDAOFileImpl.DELIMITER;
        orderString = orderString + toText.getLaborCost() + OrderDAOFileImpl.DELIMITER;
        orderString = orderString + toText.getTax() + OrderDAOFileImpl.DELIMITER;
        orderString = orderString + toText.getTotal() + OrderDAOFileImpl.DELIMITER;
        orderString = orderString + new SimpleDateFormat("MMddyyyy").format(toText.getOrderDate());

        return orderString;
    }

    @Override
    public void writeOrderToFile() throws FlooringPersistenceException {
        for (File file : files) {
            file.delete();
        }

        for (Date date : orders.keySet()) {
            if (orders.get(date).isEmpty()) {
                continue;
            }

            PrintWriter out;

            try {
                out = new PrintWriter(new FileWriter("./Orders/Orders_"
                        + new SimpleDateFormat("MMddyyyy").format(date) + ".txt"));
                String[] columnHeaders = {"OrderNumber", "CustomerName", "State",
                    "TaxRate", "ProductType", "Area", "CostPerSquareFoot",
                    "LaborCostPerSquareFoot", "MaterialCost", "LaborCost", "Tax",
                    "Total", "OrderDate"};
                out.println(String.join(DELIMITER, columnHeaders));
            } catch (IOException e) {
                throw new FlooringPersistenceException(
                        "Could not save order.", e);
            }

            String orderAsText;
            for (Order currentOrder : orders.get(date).values()) {

                orderAsText = marshallOrder(currentOrder);

                out.println(orderAsText);

                out.flush();
            }
            out.close();
        }

    }

}
