/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery;

import com.mycompany.flooringmastery.controller.FlooringController;
import com.mycompany.flooringmastery.dao.FlooringPersistenceException;
import com.mycompany.flooringmastery.dao.OrderValidationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author ericabenton
 */
public class App {

    public static void main(String[] args) throws FlooringPersistenceException, OrderValidationException {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationXML.xml");
        FlooringController controller = ctx.getBean("controller", FlooringController.class);
        controller.run();
    }

}
