/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.dao;

import java.util.List;

/**
 *
 * @author ericabenton
 */
public class OrderValidationException extends Exception {

    List<String> errors;

    public OrderValidationException(List<String> errors) {
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }

}
