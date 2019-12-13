/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.flooringmastery.dao;

import com.mycompany.flooringmastery.dto.TaxRate;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author ericabenton
 */
public class TaxDAOStubImpl implements TaxDAO {

    private TaxRate ky;
    private TaxRate sc;
    private TaxRate mo;
    private TaxRate ak;
    private Map<String, TaxRate> taxes = new HashMap<>();

    public TaxDAOStubImpl() {
        this.ky = new TaxRate("KY", "Alaska", new BigDecimal("6.00"));
        this.sc = new TaxRate("SC", "South Carolina", new BigDecimal("12.00"));
        this.mo = new TaxRate("MO", "Missouri", new BigDecimal("4.00"));
        this.ak = new TaxRate("AK", "Alaska", new BigDecimal("10.00"));
        taxes.put("ky", ky);
        taxes.put("sc", sc);
        taxes.put("mo", mo);
        taxes.put("ak", ak);
    }

    @Override
    public TaxRate getTaxRateByState(String state) {
        return taxes.get(state.toLowerCase());
    }

    @Override
    public boolean doesStateExist(String state) {
        return taxes.containsKey(state.toLowerCase());
    }

    @Override
    public void loadTaxRates() throws FlooringPersistenceException {
    }

}
