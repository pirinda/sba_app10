/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.mkt.db;

import sba.gui.mygrid.cell.DMyItemPrices;
import sba.lib.grid.DGridRow;

/**
 *
 * @author Sergio Flores
 */
public class DRowItemPrices implements DGridRow, DMyItemPrices {

    protected int mnPkItemId;
    protected String msCode;
    protected String msName;
    protected String msUnitCode;
    protected double mdStock;
    protected double mdPriceLpp;    // last purchase price
    protected double[] madPrices;
    protected double mdTaxRate;
    protected boolean mbTaxIncluded;

    protected boolean mbRowJustEdited;

    public DRowItemPrices(int itemId, String code, String name) {
        mnPkItemId = itemId;
        msCode = code;
        msName = name;
        msUnitCode = "";
        mdStock = 0d;
        mdPriceLpp = 0d;
        madPrices = null;
        mdTaxRate = 1d;
        mbTaxIncluded = false;

        mbRowJustEdited = false;
    }

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setUnitCode(String s) { msUnitCode = s; }
    public void setStock(double d) { mdStock = d; }
    public void setPriceLpp(double d) { mdPriceLpp = d; }
    public void setPrices(double[] ad) { madPrices = ad; }
    public void setTaxRate(double d) { mdTaxRate = d; }
    public void setTaxIncluded(boolean b) { mbTaxIncluded = b; }

    public int getPkItemId() { return mnPkItemId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getUnitCode() { return msUnitCode; }
    public double getStock() { return mdStock; }
    public double getPriceLpp() { return mdPriceLpp; }
    public double[] getPrices() { return madPrices; }
    public double getTaxRate() { return mdTaxRate; }
    public boolean isTaxIncluded() { return mbTaxIncluded; }

    public boolean isRowEdited() { return mbRowJustEdited; }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkItemId };
    }

    @Override
    public String getRowCode() {
        return msCode;
    }

    @Override
    public String getRowName() {
        return msName;
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
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = msName;
                break;
            case 1:
                value = msCode;
                break;
            case 2:
                value = mdStock;
                break;
            case 3:
                value = msUnitCode;
                break;
            case 4:
                value = mdPriceLpp * (!mbTaxIncluded ? 1d : mdTaxRate);
                break;
            default:
                if (col - 5 >= 0 && col - 5 < madPrices.length) {
                    value = madPrices[col - 5] * (!mbTaxIncluded ? 1d : mdTaxRate);
                }
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        if (col - 5 >= 0 && col - 5 < madPrices.length) {
            mbRowJustEdited = true;
            madPrices[col - 5] = (Double) value / (!mbTaxIncluded ? 1d : mdTaxRate);
        }
    }

    @Override
    public void setRowEdited(boolean edited) {
        mbRowJustEdited = edited;
    }
}
