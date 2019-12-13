/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.ui;

import com.mycompany.flooringmastery.dto.Order;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ericabenton
 */
public class FlooringView {

    UserIO io = new UserIOConsoleImpl();

    public FlooringView(UserIO io) {
        this.io = io;
    }

    public int printMenuAndGetSelection() {
        io.print("<<Flooring Program>>");
        io.print("1. Display Orders");
        io.print("2. Add an Order");
        io.print("3. Edit an Order");
        io.print("4. Remove an Order");
        io.print("5. Save Current Work");
        io.print("6. Quit");

        return io.readInt("Please select from the above choices.", 1, 6);
    }

    public Date getOrderDateFromUser() {
        return io.readDateString("Please enter MMDDYYYY to see orders for that date.");
    }

    public void displaySingleOrder(Order orderToDisplay) {
        String output = "";
        if (orderToDisplay.getOrderNumber() > 0) {
            output = orderToDisplay.getOrderNumber() + " ";

        }
        io.print(output + "| Name: " + orderToDisplay.getCustomerName()
                + " | State: " + orderToDisplay.getState() + " | Tax rate: " + orderToDisplay.getTaxRate()
                + " | Product type: " + orderToDisplay.getProductType() + " | Area: " + orderToDisplay.getArea());
        io.print("Cost per sq ft: $" + orderToDisplay.getCostPerSqFt() + " | Labor cost per sq ft: $" + orderToDisplay.getLaborCostPerSqFt()
                + " | Material cost: $" + orderToDisplay.getMaterialCost() + " | Labor cost: $" + orderToDisplay.getLaborCost()
                + " | Tax: $" + orderToDisplay.getTax());
        io.print("Total: $" + orderToDisplay.getTotal());

    }

    public void displayOrders(List<Order> allOrdersByDate) {
        if (allOrdersByDate.isEmpty()) {
            io.print("No orders found for entered date.");
        }
        for (Order currentOrder : allOrdersByDate) {
            displaySingleOrder(currentOrder);
        }

    }

    public String getNameToEdit(Order orderNameToEdit) {
        io.print("Make the needed changes and hit enter. If a field doesn't need "
                + "to be changed just hit enter to move to the next field.");
        return io.readString("Enter customer name (" + orderNameToEdit.getCustomerName() + "):");
    }

    public String getStateToEdit(Order orderStateToEdit) {
        return io.readString("Enter state (" + orderStateToEdit.getState() + "):");
    }

    public String getProductToEdit(Order orderProductToEdit) {
        return io.readString("Enter product (" + orderProductToEdit.getProductType() + "):");
    }

    public String getAreaToEdit(Order orderAreaToEdit) {
        return io.readString("Enter area (" + orderAreaToEdit.getArea() + "):");
    }

    public Order getNewOrderInfo() {
        Date orderDate = io.readDateString("Please enter install date, MMDDYYYY");
        String customerName = io.readString("Please enter your name/company name");
        String state = io.readString("Please enter your state's abbreviation");
        String productType = io.readString("Please enter the product you'd like");
        String areaAsString = io.readString("Please enter the total area");
        BigDecimal area = new BigDecimal(areaAsString).setScale(2, RoundingMode.HALF_UP);
        Order currentOrder = new Order(orderDate, customerName, state, productType, area);
        return currentOrder;
    }

    public Integer getOrderNumFromUser() {
        Integer orderNumFromUser = io.readInt("Please enter your order number");
        return orderNumFromUser;
    }

    public Order editOrderInfo() {

        Order editedOrder = new Order();
        return editedOrder;
    }

    public boolean getSaveConfirmation() {
        return io.readYesNo("Would you like to save your changes? Y/N");
    }

    public boolean getRemoveConfirmation() {
        return io.readYesNo("Are you sure you want to remove order? Y/N");
    }

    public boolean getPlaceOrderConfirmation() {
        return io.readYesNo("Would you like to place this order? Y/N");
    }

    public void changesSavedConfirmation() {
        io.print("Your changes have been saved.");
    }

    public void displayOrderErrors(List<String> orderErrors) {
        io.print("Please correct these errors in your order:");
        for (String error : orderErrors) {
            io.print(error);
        }
    }

    public void displayDisplayOrdersBanner() {
        io.print("=== Display Orders ===");
    }

    public void displayAddOrderBanner() {
        io.print("=== Add an Order ===");
    }

    public void displayOrderCreatedBanner(Order order) {
        io.print("Order created successfully! Your order number is: " + order.getOrderNumber());
    }

    public void displaySaveCurrentWorkBanner() {
        io.print("=== Save Current Work ===");
    }

    public void displayExitBanner() {
        io.print("Goodbye!");
    }

    public void displayNoOrderInInventoryBanner() {
        io.print("=== No such order in inventory ===");
    }

    public void displayModeErrorMessage() {
        io.print("MODE ERROR. You cannot save while in training mode.");
    }

    public void displayUnknownCommand() {
        io.print("Unknown Command!");
    }

    public void printNewLine() {
        io.print("");
    }

}
