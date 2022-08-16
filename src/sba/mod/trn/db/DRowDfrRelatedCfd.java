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
public class DRowDfrRelatedCfd implements DGridRow {

    private int mnRow;
    private String msRelationCode;
    private String msRelationName;
    private String msUuids;

    public DRowDfrRelatedCfd(final int row, final String relationCode, final String relationName, final String uuids) {
        mnRow = row;
        msRelationCode = relationCode;
        msRelationName = relationName;
        msUuids = uuids;
    }

    public void setRow(final int row) {
        mnRow = row;
    }

    public void setRelation(final String code, final String name) {
        msRelationCode = code;
        msRelationName = name;
    }

    public void setUuids(final String uuids) {
        msUuids = uuids;
    }

    public String getRelationCode() {
        return msRelationCode;
    }

    public String getRelationCodeName() {
        return msRelationCode + " - " + msRelationName;
    }

    public String getRelationName() {
        return msRelationName;
    }

    public String getUuids() {
        return msUuids;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnRow };
    }

    @Override
    public String getRowCode() {
        return msRelationCode;
    }

    @Override
    public String getRowName() {
        return msRelationName;
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return true;
    }

    @Override
    public void setRowEdited(boolean edited) {

    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = mnRow;
                break;
            case 1:
                value = getRelationCodeName();
                break;
            case 2:
                value = msUuids;
                break;
            default:
            // go nothing
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
