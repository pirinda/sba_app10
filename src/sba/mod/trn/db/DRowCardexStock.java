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
public class DRowCardexStock implements DGridRow {

    protected int mnMove;
    protected Date mtDate;
    protected String msIogMoveType;
    protected double mdMoveIn;
    protected double mdMoveOut;
    protected double mdStock;
    protected String msDpsType;
    protected String msDpsNumber;
    protected Date mtDpsDate;
    protected String msDpsCompanyBranchCode;
    protected String msDpsBizPartner;
    protected String msDpsBizPartnerCode;

    public DRowCardexStock() {
        mnMove = 0;
        mtDate = null;
        msIogMoveType = "";
        mdMoveIn = 0;
        mdMoveOut = 0;
        mdStock = 0;
        msDpsType = "";
        msDpsNumber = "";
        mtDpsDate = null;
        msDpsCompanyBranchCode = "";
        msDpsBizPartner = "";
        msDpsBizPartnerCode = "";
    }

    public void setMove(int n) { mnMove = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setIogMoveType(String s) { msIogMoveType = s; }
    public void setMoveIn(double d) { mdMoveIn = d; }
    public void setMoveOut(double d) { mdMoveOut = d; }
    public void setStock(double d) { mdStock = d; }
    public void setDpsType(String s) { msDpsType = s; }
    public void setDpsNumber(String s) { msDpsNumber = s; }
    public void setDpsDate(Date t) { mtDpsDate = t; }
    public void setDpsCompanyBranchCode(String s) { msDpsCompanyBranchCode = s; }
    public void setDpsBizPartner(String s) { msDpsBizPartner = s; }
    public void setDpsBizPartnerCode(String s) { msDpsBizPartnerCode = s; }

    public int getMove() { return mnMove; }
    public Date getDate() { return mtDate; }
    public String getIogMoveType() { return msIogMoveType; }
    public double getMoveIn() { return mdMoveIn; }
    public double getMoveOut() { return mdMoveOut; }
    public double getStock() { return mdStock; }
    public String getDpsType() { return msDpsType; }
    public String getDpsNumber() { return msDpsNumber; }
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
                value = msIogMoveType;
                break;
            case 3:
                value = mdMoveIn;
                break;
            case 4:
                value = mdMoveOut;
                break;
            case 5:
                value = mdStock;
                break;
            case 6:
                value = msDpsType;
                break;
            case 7:
                value = msDpsNumber;
                break;
            case 8:
                value = mtDpsDate;
                break;
            case 9:
                value = msDpsCompanyBranchCode;
                break;
            case 10:
                value = msDpsBizPartner;
                break;
            case 11:
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
