/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import java.util.Vector;
import sba.lib.grid.DGridRow;

/**
 *
 * @author Sergio Flores
 */
public class DRowDpsRowAdjusted implements DGridRow {

    protected int mnPkDpsId;
    protected int mnPkRowId;
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnSortingPos;
    protected String msCode;
    protected String msName;
    protected double mdQuantityAdjusted;
    protected String msUnitCode;
    protected double mdTotalAdjustedCy;

    protected boolean mbInventoriable;
    protected double mdQuantityOriginal;
    protected double mdQuantityInc;
    protected double mdQuantityIncCurrent;
    protected double mdQuantityDec;
    protected double mdQuantityDecCurrent;
    protected double mdTotalOriginalCy;
    protected double mdTotalIncCy;
    protected double mdTotalIncCurrentCy;
    protected double mdTotalDecCy;
    protected double mdTotalDecCurrentCy;

    protected Vector<DTrnStockMove> mvStockMovesAvailable;
    protected Vector<DTrnStockMove> mvStockMovesAdjusted;

    public DRowDpsRowAdjusted(int[] rowKey, int itemId, int unitId, int sortingPos, String code, String name, String unitCode) {
        mnPkDpsId = rowKey[0];
        mnPkRowId = rowKey[1];
        mnFkItemId = itemId;
        mnFkUnitId = unitId;
        mnSortingPos = sortingPos;
        msCode = code;
        msName = name;
        mdQuantityAdjusted = 0;
        msUnitCode = unitCode;
        mdTotalAdjustedCy = 0;

        mbInventoriable = false;
        mdQuantityOriginal = 0;
        mdQuantityInc = 0;
        mdQuantityIncCurrent = 0;
        mdQuantityDec = 0;
        mdQuantityDecCurrent = 0;
        mdTotalOriginalCy = 0;
        mdTotalIncCy = 0;
        mdTotalIncCurrentCy = 0;
        mdTotalDecCy = 0;
        mdTotalDecCurrentCy = 0;

        mvStockMovesAvailable = new Vector<DTrnStockMove>();
        mvStockMovesAdjusted = new Vector<DTrnStockMove>();
    }

    public void setPkDpsId(int n) { mnPkDpsId = n; }
    public void setPkRowId(int n) { mnPkRowId = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setSortingPos(int n) { mnSortingPos = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setQuantityAdjusted(double d) { mdQuantityAdjusted = d; }
    public void setUnitCode(String s) { msUnitCode = s; }
    public void setTotalAdjustedCy(double d) { mdTotalAdjustedCy = d; }

    public void setInventoriable(boolean b) { mbInventoriable = b; }
    public void setQuantityOriginal(double d) { mdQuantityOriginal = d; }
    public void setQuantityInc(double d) { mdQuantityInc = d; }
    public void setQuantityIncCurrent(double d) { mdQuantityIncCurrent = d; }
    public void setQuantityDec(double d) { mdQuantityDec = d; }
    public void setQuantityDecCurrent(double d) { mdQuantityDecCurrent = d; }
    public void setTotalOriginalCy(double d) { mdTotalOriginalCy = d; }
    public void setTotalIncCy(double d) { mdTotalIncCy = d; }
    public void setTotalIncCurrentCy(double d) { mdTotalIncCurrentCy = d; }
    public void setTotalDecCy(double d) { mdTotalDecCy = d; }
    public void setTotalDecCurrentCy(double d) { mdTotalDecCurrentCy = d; }

    public int getPkDpsId() { return mnPkDpsId; }
    public int getPkRowId() { return mnPkRowId; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getSortingPos() { return mnSortingPos; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public double getQuantityAdjusted() { return mdQuantityAdjusted; }
    public String getUnitCode() { return msUnitCode; }
    public double getTotalAdjustedCy() { return mdTotalAdjustedCy; }

    public boolean isInventoriable() { return mbInventoriable; }
    public double getQuantityOriginal() { return mdQuantityOriginal; }
    public double getQuantityInc() { return mdQuantityInc; }
    public double getQuantityIncCurrent() { return mdQuantityIncCurrent; }
    public double getQuantityDec() { return mdQuantityDec; }
    public double getQuantityDecCurrent() { return mdQuantityDecCurrent; }
    public double getTotalOriginalCy() { return mdTotalOriginalCy; }
    public double getTotalIncCy() { return mdTotalIncCy; }
    public double getTotalIncCurrentCy() { return mdTotalIncCurrentCy; }
    public double getTotalDecCy() { return mdTotalDecCy; }
    public double getTotalDecCurrentCy() { return mdTotalDecCurrentCy; }

    public Vector<DTrnStockMove> getStockMovesAvailable() { return mvStockMovesAvailable; }
    public Vector<DTrnStockMove> getStockMovesAdjusted() { return mvStockMovesAdjusted; }

    public double getQuantityAvailable() { return mdQuantityOriginal + mdQuantityInc + mdQuantityIncCurrent - mdQuantityDec - mdQuantityDecCurrent; }
    public double getTotalAvailableCy() { return mdTotalOriginalCy + mdTotalIncCy + mdTotalIncCurrentCy - mdTotalDecCy - mdTotalDecCurrentCy; }

    public boolean areLotsAvailable() {
        return mbInventoriable && mvStockMovesAvailable.size() > 0 && !mvStockMovesAvailable.get(0).getLot().isEmpty();
    }

    public boolean areSerialNumbersAvailable() {
        return mbInventoriable && mvStockMovesAvailable.size() > 0 && !mvStockMovesAvailable.get(0).getSerialNumber().isEmpty();
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { mnPkDpsId, mnPkRowId };
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
                value = mnSortingPos;
                break;
            case 1:
                value = msCode;
                break;
            case 2:
                value = msName;
                break;
            case 3:
                value = getQuantityAvailable();
                break;
            case 4:
                value = mdQuantityAdjusted;
                break;
            case 5:
                value = msUnitCode;
                break;
            case 6:
                value = getTotalAvailableCy();
                break;
            case 7:
                value = mdTotalAdjustedCy;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                mdQuantityAdjusted = (Double) value;
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                mdTotalAdjustedCy = (Double) value;
                break;
            default:
        }
    }
}
