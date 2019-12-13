/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.controller;

import com.mycompany.flooringmastery.dao.FlooringPersistenceException;
import com.mycompany.flooringmastery.dao.OrderValidationException;
import com.mycompany.flooringmastery.dto.Order;
import com.mycompany.flooringmastery.service.FlooringService;
import com.mycompany.flooringmastery.ui.FlooringView;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ericabenton
 */
public class FlooringController {

    FlooringView view;
    FlooringService service;

    public FlooringController(FlooringService service, FlooringView view) {
        this.service = service;
        this.view = view;
    }

    public void run() throws FlooringPersistenceException, OrderValidationException {
        boolean keepGoing = true;
        int menuSelection;

        service.loadAllData();

        while (keepGoing) {

            menuSelection = getMenuSelection();

            switch (menuSelection) {
                case 1:
                    //DISPLAY ORDERS 
                    Date userDate = view.getOrderDateFromUser();
                    List allOrdersByDate = service.getAllOrders(userDate);
                    view.displayOrders(allOrdersByDate);
                    break;
                case 2:
                    //ADD ORDER
                    while (true) {
                        Order orderToPlace = view.getNewOrderInfo();
                        try {
                            service.validateOrder(orderToPlace);
                            service.calculateAll(orderToPlace);
                            view.displaySingleOrder(orderToPlace);
                            if (view.getPlaceOrderConfirmation()) {
                                service.storeOrder(orderToPlace);
                                view.displayOrderCreatedBanner(orderToPlace);
                            }
                            break;
                        } catch (OrderValidationException ex) {
                            view.displayOrderErrors(ex.getErrors());
                        }
                    }
                    break;
                case 3:
                    //EDIT ORDER 
                    while (true) {
                        Date userDateToEdit = view.getOrderDateFromUser();
                        Integer userOrderNumToEdit = view.getOrderNumFromUser();
                        Order orderByDateAndNumEdit = service.getOrdersByDateAndNum(userDateToEdit, userOrderNumToEdit);

                        if (orderByDateAndNumEdit == null) {
                            view.displayNoOrderInInventoryBanner();
                            continue;
                        }

                        view.displaySingleOrder(orderByDateAndNumEdit);

                        String nameToEdit = view.getNameToEdit(orderByDateAndNumEdit);
                        if (!nameToEdit.equals("")) {
                            orderByDateAndNumEdit.setCustomerName(nameToEdit);
                        }

                        String stateToEdit = view.getStateToEdit(orderByDateAndNumEdit);
                        if (!stateToEdit.equals("")) {
                            orderByDateAndNumEdit.setState(stateToEdit);
                        }

                        String productToEdit = view.getProductToEdit(orderByDateAndNumEdit);
                        if (!productToEdit.equals("")) {
                            orderByDateAndNumEdit.setProductType(productToEdit);
                        }

                        String currentAreaAsString = view.getAreaToEdit(orderByDateAndNumEdit);
                        if (!currentAreaAsString.equals("")) {
                            BigDecimal areaToEdit = new BigDecimal(currentAreaAsString).setScale(2, RoundingMode.HALF_UP);
                            orderByDateAndNumEdit.setArea(areaToEdit);
                        }

                        try {
                            service.validateOrder(orderByDateAndNumEdit);
                            service.calculateAll(orderByDateAndNumEdit);
                            view.displaySingleOrder(orderByDateAndNumEdit);
                            if (view.getSaveConfirmation()) {
                                try {
                                    service.storeOrder(orderByDateAndNumEdit);
                                    view.changesSavedConfirmation();
                                } catch (OrderValidationException ex) {
                                    view.displayOrderErrors(ex.getErrors());
                                }
                            }
                            break;
                        } catch (OrderValidationException ex) {
                            view.displayOrderErrors(ex.getErrors());
                        }
                    }
                    break;
                case 4:
                    //REMOVE ORDER
                    //view -- get order date and number from user
                    Date userDateToRemove = view.getOrderDateFromUser();
                    Integer userOrderNumToRemove = view.getOrderNumFromUser();
                    //service -- get list of orders that matches date & order
                    Order orderByDateAndNumRemove = service.getOrdersByDateAndNum(userDateToRemove, userOrderNumToRemove);
                    if (orderByDateAndNumRemove == null) {
                        view.displayNoOrderInInventoryBanner();
                        continue;
                    }
                    //view -- display order for date/order number
                    view.displaySingleOrder(orderByDateAndNumRemove);
                    //view -- ask if user sure want to remove (service remove() from dao)
                    if (view.getRemoveConfirmation()) {
                        service.removeAnOrder(orderByDateAndNumRemove);
                    }
                    break;
                case 5:
                    if (service.isInProductionMode()) {
                        service.writeOrderToFile();
                        changesSavedMessage();
                    } else {
                        modeErrorMessage();
                    }
                    break;
                case 6:
                    if (view.getSaveConfirmation()) {
                        if (service.isInProductionMode()) {
                            service.writeOrderToFile();
                            changesSavedMessage();
                        } else {
                            modeErrorMessage();
                        }
                    }
                    keepGoing = false;
                    break;
                default:
                    unknownCommand();
            }
            view.printNewLine();
        }
        exitMessage();
    }

    public int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }

    private void unknownCommand() {
        view.displayUnknownCommand();
    }

    private void modeErrorMessage() {
        view.displayModeErrorMessage();
    }

    private void changesSavedMessage() {
        view.changesSavedConfirmation();
    }
}
