/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.service;

import com.mycompany.flooringmastery.dao.FlooringPersistenceException;
import com.mycompany.flooringmastery.dao.ModeDAO;
import com.mycompany.flooringmastery.dao.OrderDAO;
import com.mycompany.flooringmastery.dao.OrderValidationException;
import com.mycompany.flooringmastery.dao.ProductDAO;
import com.mycompany.flooringmastery.dao.TaxDAO;
import com.mycompany.flooringmastery.dto.Order;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ericabenton
 */
public class FlooringServiceImpl implements FlooringService {

    OrderDAO orderDAO;
    ProductDAO productDAO;
    TaxDAO taxDAO;
    ModeDAO modeDAO;

    public FlooringServiceImpl() {
    }

    public FlooringServiceImpl(OrderDAO orderDAO, ProductDAO productDAO, TaxDAO taxDAO, ModeDAO modeDAO) {
        this.orderDAO = orderDAO;
        this.productDAO = productDAO;
        this.taxDAO = taxDAO;
        this.modeDAO = modeDAO;
    }

    @Override
    public void loadAllData() throws FlooringPersistenceException {
        this.orderDAO.loadAllOrders();
        this.productDAO.loadProducts();
        this.taxDAO.loadTaxRates();
        this.modeDAO.loadMode();
    }

    @Override
    public Order storeOrder(Order order) throws FlooringPersistenceException {
        return this.orderDAO.storeOrder(order);
    }

    @Override
    public void validateOrder(Order order) throws OrderValidationException {
        List<String> errors = new ArrayList<>();
        Date todaysDate = new Date();
        if (order.getOrderDate().before(todaysDate)) {
            errors.add("The date needs to be in the future.");
        }
        if (!order.getCustomerName().matches("^[\\w.,\\s]+$")) {
            errors.add("Error with customer name. May not be left blank and can only contain "
                    + "a-z letters, 0-9, periods or commas.");
        }
        if (!this.taxDAO.doesStateExist(order.getState())) {
            errors.add("We currently don't sell to that state.");
        }
        if (!this.productDAO.doesProductExist(order.getProductType())) {
            errors.add("Please enter a valid product.");
        }
        if (order.getArea().compareTo(new BigDecimal(100.00)) == -1) {
            errors.add("The minimum order size is 100 square feet");
        }
        if (errors.size() >= 1) {
            throw new OrderValidationException(errors);
        }

    }

    @Override
    public List<Order> getAllOrders(Date userDate) {
        return this.orderDAO.getAllOrders(userDate);
    }

    @Override
    public void removeAnOrder(Order orderToRemove) {
        this.orderDAO.removeAnOrder(orderToRemove);
    }

    @Override
    public Order getOrdersByDateAndNum(Date userDate, Integer userOrderNum) {
        return this.orderDAO.getOrderByDateAndNum(userDate, userOrderNum);
    }

    @Override
    public BigDecimal calcMaterialCost(Order orderToCalc) {
        BigDecimal area = orderToCalc.getArea();
        BigDecimal costPerSqFt = orderToCalc.getCostPerSqFt();

        BigDecimal materialCost = area.multiply(costPerSqFt).setScale(2, RoundingMode.HALF_UP);

        return materialCost;
    }

    @Override
    public BigDecimal calcLaborCost(Order orderToCalc) {
        BigDecimal area = orderToCalc.getArea();
        BigDecimal laborCostPerSqFt = orderToCalc.getLaborCostPerSqFt();

        BigDecimal laborCost = area.multiply(laborCostPerSqFt).setScale(2, RoundingMode.HALF_UP);

        return laborCost;
    }

    @Override
    public BigDecimal calcTax(Order orderToCalc, BigDecimal materialCost, BigDecimal laborCost) {
        BigDecimal taxRate = orderToCalc.getTaxRate();

        BigDecimal tax = materialCost.add(laborCost).multiply(taxRate).divide(new BigDecimal("100.00")).setScale(2, RoundingMode.HALF_UP);
        return tax;
    }

    @Override
    public BigDecimal calcTotal(BigDecimal materialCost, BigDecimal laborCost, BigDecimal tax) {
        BigDecimal total = materialCost.add(laborCost).add(tax).setScale(2, RoundingMode.HALF_UP);

        return total;
    }

    @Override
    public Order calculateAll(Order orderToCalc) {
        orderToCalc.setTaxRate(this.taxDAO.getTaxRateByState(orderToCalc.getState()).getTaxRate());
        orderToCalc.setCostPerSqFt(this.productDAO.getProduct(orderToCalc.getProductType()).getCostPerSqFt());
        orderToCalc.setLaborCostPerSqFt(this.productDAO.getProduct(orderToCalc.getProductType()).getLaborCostPerSqFt());
        BigDecimal materialCost = this.calcMaterialCost(orderToCalc);
        BigDecimal laborCost = this.calcLaborCost(orderToCalc);
        BigDecimal tax = this.calcTax(orderToCalc, materialCost, laborCost);
        BigDecimal total = this.calcTotal(materialCost, laborCost, tax);

        orderToCalc.setMaterialCost(materialCost);
        orderToCalc.setLaborCost(laborCost);
        orderToCalc.setTax(tax);
        orderToCalc.setTotal(total);

        return orderToCalc;
    }

    @Override
    public void writeOrderToFile() throws FlooringPersistenceException {
        this.orderDAO.writeOrderToFile();
    }

    @Override
    public boolean isInProductionMode() {
        return this.modeDAO.isInProductionMode();
    }

}
