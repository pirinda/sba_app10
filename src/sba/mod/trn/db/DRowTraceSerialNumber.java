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
public class DRowTraceSerialNumber implements DGridRow {

    protected int mnMove;
    protected Date mtDate;
    protected String msItemName;
    protected String msItemCode;
    protected String msIogMoveType;
    protected double mdMoveIn;
    protected double mdMoveOut;
    protected String msUnitCode;
    protected String msDpsNumber;
    protected String msDpsType;
    protected Date mtDpsDate;
    protected String msDpsCompanyBranchCode;
    protected String msDpsBizPartner;
    protected String msDpsBizPartnerCode;

    public DRowTraceSerialNumber() {
        mnMove = 0;
        mtDate = null;
        msItemName = "";
        msItemCode = "";
        msIogMoveType = "";
        mdMoveIn = 0;
        mdMoveOut = 0;
        msUnitCode = "";
        msDpsNumber = "";
        msDpsType = "";
        mtDpsDate = null;
        msDpsCompanyBranchCode = "";
        msDpsBizPartner = "";
        msDpsBizPartnerCode = "";
    }

    public void setMove(int n) { mnMove = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setItemName(String s) { msItemName = s; }
    public void setItemCode(String s) { msItemCode = s; }
    public void setIogMoveType(String s) { msIogMoveType = s; }
    public void setMoveIn(double d) { mdMoveIn = d; }
    public void setMoveOut(double d) { mdMoveOut = d; }
    public void setUnitCode(String s) { msUnitCode = s; }
    public void setDpsNumber(String s) { msDpsNumber = s; }
    public void setDpsType(String s) { msDpsType = s; }
    public void setDpsDate(Date t) { mtDpsDate = t; }
    public void setDpsCompanyBranchCode(String s) { msDpsCompanyBranchCode = s; }
    public void setDpsBizPartner(String s) { msDpsBizPartner = s; }
    public void setDpsBizPartnerCode(String s) { msDpsBizPartnerCode = s; }

    public int getMove() { return mnMove; }
    public Date getDate() { return mtDate; }
    public String getItemName() { return msItemName; }
    public String getItemCode() { return msItemCode; }
    public String getIogMoveType() { return msIogMoveType; }
    public double getMoveIn() { return mdMoveIn; }
    public double getMoveOut() { return mdMoveOut; }
    public String getUnitCode() { return msUnitCode; }
    public String getDpsNumber() { return msDpsNumber; }
    public String getDpsType() { return msDpsType; }
    public Date getDpsDate() { return mtDpsDate; }
    public String getDpsCompanyBranchCode() { return msDpsCompanyBranchCode; }
    public String getDpsBizPartner() { return msDpsBizPartner; }
    public String getDpsBizPartnerCode() { return msDpsBizPartnerCode; }

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
                value = mtDate;
                break;
            case 2:
                value = msItemName;
                break;
            case 3:
                value = msItemCode;
                break;
            case 4:
                value = msIogMoveType;
                break;
            case 5:
                value = mdMoveIn;
                break;
            case 6:
                value = mdMoveOut;
                break;
            case 7:
                value = msUnitCode;
                break;
            case 8:
                value = msDpsNumber;
                break;
            case 9:
                value = msDpsType;
                break;
            case 10:
                value = mtDpsDate;
                break;
            case 11:
                value = msDpsCompanyBranchCode;
                break;
            case 12:
                value = msDpsBizPartner;
                break;
            case 13:
                value = msDpsBizPartnerCode;
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
