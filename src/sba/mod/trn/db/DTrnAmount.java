/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import sba.lib.DLibConsts;

/**
 *
 * @author Sergio Flores
 */
public class DTrnAmount {

    protected double mdAmount;
    protected double mdAmountCy;
    protected int mnCurrencyId;
    protected String msCurrencyCode;

    public DTrnAmount() {
        this(0, 0, DLibConsts.UNDEFINED, "");
    }

    public DTrnAmount(double amount, double amountCy, int currencyId, String currencyCode) {
        mdAmount = amount;
        mdAmountCy = amountCy;
        mnCurrencyId = currencyId;
        msCurrencyCode = currencyCode;
    }

    public void setAmount(double d) { mdAmount = d; }
    public void setAmountCy(double d) { mdAmountCy = d; }
    public void setCurrencyId(int n) { mnCurrencyId = n; }
    public void setCurrencyCode(String s) { msCurrencyCode = s; }

    public double getAmount() { return mdAmount; }
    public double getAmountCy() { return mdAmountCy; }
    public int getCurrencyId() { return mnCurrencyId; }
    public String getCurrencyCode() { return msCurrencyCode; }

    public double getExchangeRate() { return mdAmountCy == 0d ? 0d : mdAmount / mdAmountCy; }
}
