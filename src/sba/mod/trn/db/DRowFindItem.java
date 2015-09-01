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
public class DRowFindItem implements DGridRow {

    protected int mnFindMode;
    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected String msCode;
    protected String msName;
    protected String msExtra;
    protected int mnFkItemCategoryId;
    protected int mnFkItemClassId;
    protected int mnFkItemTypeId;
    protected String msItemCategoryCode;
    protected String msItemClassCode;
    protected String msItemTypeCode;
    protected boolean mbInventoriable;
    protected boolean mbConvertible;
    protected double mdStock;
    protected String msUnitCode;
    protected double mdPrice;
    protected double mdPriceNet;

    public DRowFindItem(int findMode, int itemId, int unitId, String code, String name, String extra) {
        mnFindMode = findMode;
        mnPkItemId = itemId;
        mnPkUnitId = unitId;
        msCode = code;
        msName = name;
        msExtra = extra;
        mnFkItemCategoryId = 0;
        mnFkItemClassId = 0;
        mnFkItemTypeId = 0;
        msItemCategoryCode = "";
        msItemClassCode = "";
        msItemTypeCode = "";
        mbInventoriable = false;
        mbConvertible = false;
        mdStock = 0;
        msUnitCode = "";
        mdPrice = 0;
        mdPriceNet = 0;
    }

    public void setFindMode(int n) { mnFindMode = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setExtra(String s) { msExtra = s; }
    public void setFkItemCategoryId(int n) { mnFkItemCategoryId = n; }
    public void setFkItemClassId(int n) { mnFkItemClassId = n; }
    public void setFkItemTypeId(int n) { mnFkItemTypeId = n; }
    public void setItemCategoryCode(String s) { msItemCategoryCode = s; }
    public void setItemClassCode(String s) { msItemClassCode = s; }
    public void setItemTypeCode(String s) { msItemTypeCode = s; }
    public void setInventoriable(boolean b) { mbInventoriable = b; }
    public void setConvertible(boolean b) { mbConvertible = b; }
    public void setStock(double d) { mdStock = d; }
    public void setUnitCode(String s) { msUnitCode = s; }
    public void setPrice(double d) { mdPrice = d; }
    public void setPriceNet(double d) { mdPriceNet = d; }

    public int getFindMode() { return mnFindMode; }
    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getExtra() { return msExtra; }
    public int getFkItemCategoryId() { return mnFkItemCategoryId; }
    public int getFkItemClassId() { return mnFkItemClassId; }
    public int getFkItemTypeId() { return mnFkItemTypeId; }
    public String getItemCategoryCode() { return msItemCategoryCode; }
    public String getItemClassCode() { return msItemClassCode; }
    public String getItemTypeCode() { return msItemTypeCode; }
    public double getStock() { return mdStock; }
    public boolean isInventoriable() { return mbInventoriable; }
    public boolean isConvertible() { return mbConvertible; }
    public String getUnitCode() { return msUnitCode; }
    public double getPrice() { return mdPrice; }
    public double getPriceNet() { return mdPriceNet; }

    public void setItemTypeKey(int[] key) {
        mnFkItemCategoryId = key[0];
        mnFkItemClassId = key[1];
        mnFkItemTypeId = key[2];
    }

    public int[] getItemCategoryKey() {
        return new int[] { mnFkItemCategoryId };
    }

    public int[] getItemClassKey() {
        return new int[] { mnFkItemCategoryId, mnFkItemClassId };
    }

    public int[] getItemTypeKey() {
        return new int[] { mnFkItemCategoryId, mnFkItemClassId, mnFkItemTypeId };
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkItemId, mnPkUnitId };
    }

    @Override
    public String getRowCode() {
        return getCode();
    }

    @Override
    public String getRowName() {
        return getName();
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
                switch (mnFindMode) {
                    case DTrnConsts.FIND_MODE_BY_CODE:
                        value = msCode;
                        break;
                    case DTrnConsts.FIND_MODE_BY_NAME:
                        value = msName;
                        break;
                    case DTrnConsts.FIND_MODE_BY_BRD:
                    case DTrnConsts.FIND_MODE_BY_MFR:
                    case DTrnConsts.FIND_MODE_BY_CMP:
                    case DTrnConsts.FIND_MODE_BY_DEP:
                        value = msExtra;
                        break;
                    default:
                }
                break;

            case 1:
                switch (mnFindMode) {
                    case DTrnConsts.FIND_MODE_BY_CODE:
                        value = msName;
                        break;
                    case DTrnConsts.FIND_MODE_BY_NAME:
                        value = msCode;
                        break;
                    case DTrnConsts.FIND_MODE_BY_BRD:
                    case DTrnConsts.FIND_MODE_BY_MFR:
                    case DTrnConsts.FIND_MODE_BY_CMP:
                    case DTrnConsts.FIND_MODE_BY_DEP:
                        value = msName;
                        break;
                    default:
                }
                break;

            case 2:
                switch (mnFindMode) {
                    case DTrnConsts.FIND_MODE_BY_CODE:
                    case DTrnConsts.FIND_MODE_BY_NAME:
                        value = msExtra;
                        break;
                    case DTrnConsts.FIND_MODE_BY_BRD:
                    case DTrnConsts.FIND_MODE_BY_MFR:
                    case DTrnConsts.FIND_MODE_BY_CMP:
                    case DTrnConsts.FIND_MODE_BY_DEP:
                        value = msCode;
                        break;
                    default:
                }
                break;

            case 3:
                value = msItemCategoryCode;
                break;

            case 4:
                value = msItemClassCode;
                break;

            case 5:
                value = msItemTypeCode;
                break;

            case 6:
                value = mdStock;
                break;

            case 7:
                value = msUnitCode;
                break;

            case 8:
                value = mdPrice;
                break;

            case 9:
                value = mdPriceNet;
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
