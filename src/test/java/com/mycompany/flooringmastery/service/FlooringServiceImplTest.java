/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.service;

import com.mycompany.flooringmastery.dao.OrderValidationException;
import com.mycompany.flooringmastery.dao.ProductDAO;
import com.mycompany.flooringmastery.dto.Order;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author ericabenton
 */
public class FlooringServiceImplTest {

    private ApplicationContext ctx = new ClassPathXmlApplicationContext("testXML.xml");
    private FlooringService service;

    public FlooringServiceImplTest() throws ParseException {
        service = ctx.getBean("serviceLayer", FlooringService.class);
    }

    @Test
    public void testValidation() throws Exception {
        ProductDAO productDAO = ctx.getBean("productDAO", ProductDAO.class);
        productDAO.loadProducts();

        Date testDate = new SimpleDateFormat("MMddyyyy").parse("03271920");
        Order order = new Order();
        order.setOrderDate(testDate);
        order.setCustomerName("Sarah!");
        order.setState("UT");
        order.setProductType("TileX");
        order.setArea(new BigDecimal("45.00"));
        try {
            service.validateOrder(order);
            Assertions.fail("Expected new order to throw new order validation exception");
        } catch (OrderValidationException ex) {
            Assertions.assertEquals(5, ex.getErrors().size());
            Assertions.assertTrue(ex.getErrors().contains("The minimum order size is 100 square feet"));
            Assertions.assertTrue(ex.getErrors().contains("The date needs to be in the future."));
            Assertions.assertTrue(ex.getErrors().contains("Error with customer name. May not be left blank and can only contain "
                    + "a-z letters, 0-9, periods or commas."));
            Assertions.assertTrue(ex.getErrors().contains("Please enter a valid product."));
            Assertions.assertTrue(ex.getErrors().contains("We currently don't sell to that state."));

        }

    }

    @Test
    public void testCalcMaterialCost() {
        Order mcOrder = new Order();
        mcOrder.setArea(new BigDecimal("100.00"));
        mcOrder.setCostPerSqFt(new BigDecimal("2.00"));

        BigDecimal result = service.calcMaterialCost(mcOrder);

        Assertions.assertEquals(new BigDecimal("200.00").setScale(2), result);

    }

    @Test
    public void testCalcLaborlCost() {
        Order lcOrder = new Order();
        lcOrder.setArea(new BigDecimal("200.00"));
        lcOrder.setLaborCostPerSqFt(new BigDecimal("5.00"));

        BigDecimal result = service.calcLaborCost(lcOrder);

        Assertions.assertEquals(new BigDecimal("1000.00").setScale(2), result);

    }

    @Test
    public void testCalcTax() {
        Order taxOrder = new Order();
        taxOrder.setTaxRate(new BigDecimal("10.00"));
        taxOrder.setMaterialCost(new BigDecimal("200.00"));
        taxOrder.setLaborCost(new BigDecimal("300.00"));

        BigDecimal result = service.calcTax(taxOrder, taxOrder.getMaterialCost(), taxOrder.getLaborCost());

        Assertions.assertEquals(new BigDecimal("50.00").setScale(2), result);
    }

    @Test
    public void testCalcTotalCost() {
        BigDecimal one = new BigDecimal("100.00");
        BigDecimal two = new BigDecimal("200.00");
        BigDecimal three = new BigDecimal("300.00");

        BigDecimal total = service.calcTotal(one, two, three);

        Assertions.assertEquals(new BigDecimal("600.00").setScale(2), total);

    }

    @Test
    public void testCalculateAll() {
        Order calcAll = new Order();
        calcAll.setArea(new BigDecimal("100"));
        calcAll.setState("Ky");
        calcAll.setProductType("Wood");

        Order resultOrder = service.calculateAll(calcAll);

        Assertions.assertEquals(new BigDecimal("6.00").setScale(2), resultOrder.getTaxRate());
        Assertions.assertEquals(new BigDecimal("5.15").setScale(2), resultOrder.getCostPerSqFt());
        Assertions.assertEquals(new BigDecimal("4.75").setScale(2), resultOrder.getLaborCostPerSqFt());
        Assertions.assertEquals(new BigDecimal("515").setScale(2), resultOrder.getMaterialCost());
        Assertions.assertEquals(new BigDecimal("475").setScale(2), resultOrder.getLaborCost());
        Assertions.assertEquals(new BigDecimal("59.40").setScale(2), resultOrder.getTax());
        Assertions.assertEquals(new BigDecimal("1049.4").setScale(2), resultOrder.getTotal());

    }
}
