/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import sba.lib.grid.DGridRow;

/**
 *
 * @author Sergio Flores
 */
public class DRowDpsRow implements DGridRow {
    
    private DDbDpsRow moDpsRow;
    private Object moComplement;
    
    public DRowDpsRow(DDbDpsRow dpsRow) {
        this(dpsRow, null);
    }

    public DRowDpsRow(DDbDpsRow dpsRow, Object complement) {
        moDpsRow = dpsRow;
        moComplement = complement;
    }

    //public DDbDpsRow getDpsRow() { return moDpsRow; }
    public void setComplement(Object o) { moComplement = o; }
    
    public DDbDpsRow getDpsRow() { return moDpsRow; }
    public Object getComplement() { return moComplement; }
    
    @Override
    public int[] getRowPrimaryKey() {
        return moDpsRow.getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return moDpsRow.getCode();
    }

    @Override
    public String getRowName() {
        return moDpsRow.getName();
    }

    @Override
    public boolean isRowSystem() {
        return moDpsRow.isRowSystem();
    }

    @Override
    public boolean isRowDeletable() {
        return moDpsRow.isDeletable();
    }

    @Override
    public boolean isRowEdited() {
        return moDpsRow.isRegistryEdited();
    }

    @Override
    public void setRowEdited(boolean edited) {
        moDpsRow.setRegistryEdited(edited);
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = moDpsRow.getSortingPos();
                break;
            case 1:
                value = moDpsRow.getQuantity();
                break;
            case 2:
                value = moDpsRow.getDbUnitCode();
                break;
            case 3:
                value = moDpsRow.getCode();
                break;
            case 4:
                value = moDpsRow.getName();
                break;
            case 5:
                value = moDpsRow.getSubtotalCy_r();
                break;
            case 6:
                value = moDpsRow.getTaxChargedCy_r();
                break;
            case 7:
                value = moDpsRow.getTaxRetainedCy_r();
                break;
            case 8:
                value = moDpsRow.getTotalCy_r();
                break;
            default:
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
