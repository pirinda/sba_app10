/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import java.util.Date;
import sba.lib.grid.DGridRow;

/**
 *
 * @author Sergio Flores
 */
public class DRowHistoryPrices implements DGridRow {

    protected int mnMove;
    protected String msDpsType;
    protected String msDpsNumber;
    protected Date mtDpsDate;
    protected String msDpsCompanyBranchCode;
    protected String msDpsBizPartner;
    protected String msDpsBizPartnerCode;
    protected double mdQuantity;
    protected String msUnitCode;
    protected double mdPriceUnitary;
    protected double mdDiscountUnitary;
    protected double mdDiscountRow;
    protected double mdSubtotalProv_r;
    protected boolean mbDiscountDocApplying;
    protected double mdDiscountDoc;
    protected double mdSubtotal_r;
    protected double mdTaxCharged_r;
    protected double mdTaxRetained_r;
    protected double mdTotal_r;
    protected String msCurrencyCode;

    public DRowHistoryPrices() {
        mnMove = 0;
        msDpsType = "";
        msDpsNumber = "";
        mtDpsDate = null;
        msDpsCompanyBranchCode = "";
        msDpsBizPartner = "";
        msDpsBizPartnerCode = "";
        mdQuantity = 0;
        msUnitCode = "";
        mdPriceUnitary = 0;
        mdDiscountUnitary = 0;
        mdDiscountRow = 0;
        mdSubtotalProv_r = 0;
        mbDiscountDocApplying = false;
        mdDiscountDoc = 0;
        mdSubtotal_r = 0;
        mdTaxCharged_r = 0;
        mdTaxRetained_r = 0;
        mdTotal_r = 0;
        msCurrencyCode = "";
    }

    public void setMove(int n) { mnMove = n; }
    public void setDpsType(String s) { msDpsType = s; }
    public void setDpsNumber(String s) { msDpsNumber = s; }
    public void setDpsDate(Date t) { mtDpsDate = t; }
    public void setDpsCompanyBranchCode(String s) { msDpsCompanyBranchCode = s; }
    public void setDpsBizPartner(String s) { msDpsBizPartner = s; }
    public void setDpsBizPartnerCode(String s) { msDpsBizPartnerCode = s; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setUnitCode(String s) { msUnitCode = s; }
    public void setPriceUnitary(double d) { mdPriceUnitary = d; }
    public void setDiscountUnitary(double d) { mdDiscountUnitary = d; }
    public void setDiscountRow(double d) { mdDiscountRow = d; }
    public void setSubtotalProv_r(double d) { mdSubtotalProv_r = d; }
    public void setDiscountDocApplying(boolean b) { mbDiscountDocApplying = b; }
    public void setDiscountDoc(double d) { mdDiscountDoc = d; }
    public void setSubtotal_r(double d) { mdSubtotal_r = d; }
    public void setTaxCharged_r(double d) { mdTaxCharged_r = d; }
    public void setTaxRetained_r(double d) { mdTaxRetained_r = d; }
    public void setTotal_r(double d) { mdTotal_r = d; }
    public void setCurrencyCode(String s) { msCurrencyCode = s; }

    public int getMove() { return mnMove; }
    public String getDpsType() { return msDpsType; }
    public String getDpsNumber() { return msDpsNumber; }
    public Date getDpsDate() { return mtDpsDate; }
    public String getDpsCompanyBranchCode() { return msDpsCompanyBranchCode; }
    public String getDpsBizPartner() { return msDpsBizPartner; }
    public String getDpsBizPartnerCode() { return msDpsBizPartnerCode; }
    public double getQuantity() { return mdQuantity; }
    public String getUnitCode() { return msUnitCode; }
    public double getPriceUnitary() { return mdPriceUnitary; }
    public double getDiscountUnitary() { return mdDiscountUnitary; }
    public double getDiscountRow() { return mdDiscountRow; }
    public double getSubtotalProv_r() { return mdSubtotalProv_r; }
    public boolean isDiscountDocApplying() { return mbDiscountDocApplying; }
    public double getDiscountDoc() { return mdDiscountDoc; }
    public double getSubtotal_r() { return mdSubtotal_r; }
    public double getTaxCharged_r() { return mdTaxCharged_r; }
    public double getTaxRetained_r() { return mdTaxRetained_r; }
    public double getTotal_r() { return mdTotal_r; }
    public String getCurrencyCode() { return msCurrencyCode; }

    @Override
    public int[] getRowPrimaryKey() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRowCode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRowName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowSystem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRowDeletable() {
        throw new UnsupportedOperationException("Not supported yet.");
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
                value = mnMove;
                break;
            case 1:
                value = msDpsType;
                break;
            case 2:
                value = msDpsNumber;
                break;
            case 3:
                value = mtDpsDate;
                break;
            case 4:
                value = msDpsCompanyBranchCode;
                break;
            case 5:
                value = msDpsBizPartner;
                break;
            case 6:
                value = msDpsBizPartnerCode;
                break;
            case 7:
                value = mdQuantity;
                break;
            case 8:
                value = msUnitCode;
                break;
            case 9:
                value = msCurrencyCode;
                break;
            case 10:
                value = mdPriceUnitary;
                break;
            case 11:
                value = mdDiscountUnitary;
                break;
            case 12:
                value = mdDiscountRow;
                break;
            case 13:
                value = mdSubtotalProv_r;
                break;
            case 14:
                value = mbDiscountDocApplying;
                break;
            case 15:
                value = mdDiscountDoc;
                break;
            case 16:
                value = mdSubtotal_r;
                break;
            case 17:
                value = mdTaxCharged_r;
                break;
            case 18:
                value = mdTaxRetained_r;
                break;
            case 19:
                value = mdTotal_r;
                break;
            case 20:
                value = msCurrencyCode;
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
