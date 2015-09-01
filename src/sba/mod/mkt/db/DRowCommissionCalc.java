/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.mkt.db;

import sba.lib.grid.DGridRow;

/**
 *
 * @author Sergio Flores
 */
public class DRowCommissionCalc implements DGridRow {

    protected int mnOwnerId;
    protected String msOwnerName;
    protected double mdQuantity;
    protected double mdQuantityInc;
    protected double mdQuantityDec;
    protected double mdValue;
    protected double mdValueInc;
    protected double mdValueDec;
    protected double mdCommissionBase;
    protected double mdCommissionBaseFree;
    protected double mdCommission;

    public DRowCommissionCalc() {
        initRow();
    }

    protected void initRow() {
        mnOwnerId = 0;
        msOwnerName = "";
        mdQuantity = 0;
        mdQuantityInc = 0;
        mdQuantityDec = 0;
        mdValue = 0;
        mdValueInc = 0;
        mdValueDec = 0;
        mdCommissionBase = 0;
        mdCommissionBaseFree = 0;
        mdCommission = 0;
    }

    public void setOwnerId(int n) { mnOwnerId = n; }
    public void setOwnerName(String s) { msOwnerName = s; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setQuantityInc(double d) { mdQuantityInc = d; }
    public void setQuantityDec(double d) { mdQuantityDec = d; }
    public void setValue(double d) { mdValue = d; }
    public void setValueInc(double d) { mdValueInc = d; }
    public void setValueDec(double d) { mdValueDec = d; }
    public void setCommissionBase(double d) { mdCommissionBase = d; }
    public void setCommissionBaseFree(double d) { mdCommissionBaseFree = d; }
    public void setCommission(double d) { mdCommission = d; }

    public int getOwnerId() { return mnOwnerId; }
    public String getOwnerName() { return msOwnerName; }
    public double getQuantity() { return mdQuantity; }
    public double getQuantityInc() { return mdQuantityInc; }
    public double getQuantityDec() { return mdQuantityDec; }
    public double getValue() { return mdValue; }
    public double getValueInc() { return mdValueInc; }
    public double getValueDec() { return mdValueDec; }
    public double getCommissionBase() { return mdCommissionBase; }
    public double getCommissionBaseFree() { return mdCommissionBaseFree; }
    public double getCommission() { return mdCommission; }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnOwnerId };
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return msOwnerName;
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return false;
    }

    @Override
    public boolean isRowEdited() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRowEdited(final boolean edited) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = msOwnerName;
                break;
            case 1:
                value = mdQuantity + mdQuantityInc;
                break;
            case 2:
                value = mdQuantityDec;
                break;
            case 3:
                value = mdQuantity + mdQuantityInc - mdQuantityDec;
                break;
            case 4:
                value = mdValue + mdValueInc;
                break;
            case 5:
                value = mdValueDec;
                break;
            case 6:
                value = mdValue + mdValueInc - mdValueDec;
                break;
            case 7:
                value = mdCommissionBase;
                break;
            case 8:
                value = mdCommissionBaseFree;
                break;
            case 9:
                value = mdCommission;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
