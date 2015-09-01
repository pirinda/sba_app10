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
public class DRowItemNamePrices implements DGridRow, DMyItemPrices {

    protected String msGenusName;
    protected String msItemName;
    protected double[] madPrices;
    protected double[] madFormerPrices;
    protected double mdTaxRate;
    protected boolean mbTaxIncluded;

    protected boolean mbRowJustEdited;

    public DRowItemNamePrices(String genusName, String itemName) {
        msGenusName = genusName;
        msItemName = itemName;
        madPrices = null;
        madFormerPrices = null;
        mdTaxRate = 1d;
        mbTaxIncluded = false;

        mbRowJustEdited = false;
    }

    public void setGenusName(String s) { msGenusName = s; }
    public void setItemName(String s) { msItemName = s; }
    public void setPrices(double[] ad) { madPrices = ad; }
    public void setFormerPrices(double[] ad) { madFormerPrices = ad; }
    public void setTaxRate(double d) { mdTaxRate = d; }
    public void setTaxIncluded(boolean b) { mbTaxIncluded = b; }

    public String getGenusName() { return msGenusName; }
    public String getItemName() { return msItemName; }
    public double[] getPrices() { return madPrices; }
    public double[] getFormerPrices() { return madFormerPrices; }
    public double getTaxRate() { return mdTaxRate; }
    public boolean isTaxIncluded() { return mbTaxIncluded; }

    public boolean isRowEdited() { return mbRowJustEdited; }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { };
    }

    @Override
    public String getRowCode() {
        return "";
    }

    @Override
    public String getRowName() {
        return msGenusName + " " + msItemName;
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
                value = msGenusName;
                break;
            case 1:
                value = msItemName;
                break;
            default:
                if (col - 2 >= 0 && col - 2 < madPrices.length) {
                    value = madPrices[col - 2] * (!mbTaxIncluded ? 1d : mdTaxRate);
                }
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        if (col - 2 >= 0 && col - 2 < madPrices.length) {
            mbRowJustEdited = true;
            madPrices[col - 2] = (Double) value / (!mbTaxIncluded ? 1d : mdTaxRate);
        }
    }

    @Override
    public void setRowEdited(boolean edited) {
        mbRowJustEdited = edited;
    }

    @Override
    public double getPriceLpp() {
        return 0;
    }
}
