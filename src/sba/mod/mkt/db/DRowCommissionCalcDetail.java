/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.mkt.db;

/**
 *
 * @author Sergio Flores
 */
public class DRowCommissionCalcDetail extends DRowCommissionCalc {

    protected int mnItemId;
    protected String msItemCode;
    protected String msItemName;
    protected String msUnitCode;

    public DRowCommissionCalcDetail() {
        initRow();
    }

    @Override
    protected void initRow() {
        super.initRow();

        mnItemId = 0;
        msItemCode = "";
        msItemName = "";
        msUnitCode = "";
    }

    public void setItemId(int n) { mnItemId = n; }
    public void setItemCode(String s) { msItemCode = s; }
    public void setItemName(String s) { msItemName = s; }
    public void setUnitCode(String s) { msUnitCode = s; }

    public int getItemId() { return mnItemId; }
    public String getItemCode() { return msItemCode; }
    public String getItemName() { return msItemName; }
    public String getUnitCode() { return msUnitCode; }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnOwnerId, mnItemId };
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
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = msOwnerName;
                break;
            case 1:
                value = msItemName;
                break;
            case 2:
                value = msItemCode;
                break;
            case 3:
                value = mdQuantity + mdQuantityInc;
                break;
            case 4:
                value = mdQuantityDec;
                break;
            case 5:
                value = mdQuantity + mdQuantityInc - mdQuantityDec;
                break;
            case 6:
                value = msUnitCode;
                break;
            case 7:
                value = mdValue + mdValueInc;
                break;
            case 8:
                value = mdValueDec;
                break;
            case 9:
                value = mdValue + mdValueInc - mdValueDec;
                break;
            case 10:
                value = mdCommissionBase;
                break;
            case 11:
                value = mdCommissionBaseFree;
                break;
            case 12:
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

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
