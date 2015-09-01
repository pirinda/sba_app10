/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.fin.db;

import java.util.Date;
import sba.lib.grid.DGridRow;

/**
 *
 * @author Sergio Flores
 */
public class DRowCardexBookkeeping implements DGridRow {

    protected int mnMove;
    protected Date mtDate;
    protected String msReference;
    protected String msText;
    protected String msSystemMoveType;
    protected boolean mbAvailable;
    protected double mdDebit;
    protected double mdCredit;
    protected double mdBalanceStc;
    protected double mdBalance;

    public DRowCardexBookkeeping() {
        mnMove = 0;
        mtDate = null;
        msReference = "";
        msText = "";
        msSystemMoveType = "";
        mbAvailable = false;
        mdDebit = 0;
        mdCredit = 0;
        mdBalanceStc = 0;
        mdBalance = 0;
    }

    public void setMove(int n) { mnMove = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setReference(String s) { msReference = s; }
    public void setText(String s) { msText = s; }
    public void setSystemMoveType(String s) { msSystemMoveType = s; }
    public void setAvailable(boolean b) { mbAvailable = b; }
    public void setDebit(double d) { mdDebit = d; }
    public void setCredit(double d) { mdCredit = d; }
    public void setBalanceStc(double d) { mdBalanceStc = d; }
    public void setBalance(double d) { mdBalance = d; }

    public int getMove() { return mnMove; }
    public Date getDate() { return mtDate; }
    public String getReference() { return msReference; }
    public String getText() { return msText; }
    public String getSystemMoveType() { return msSystemMoveType; }
    public boolean getAvailable() { return mbAvailable; }
    public double getDebit() { return mdDebit; }
    public double getCredit() { return mdCredit; }
    public double getBalanceStc() { return mdBalanceStc; }
    public double getBalance() { return mdBalance; }

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
                value = msReference;
                break;
            case 3:
                value = msText;
                break;
            case 4:
                value = msSystemMoveType;
                break;
            case 5:
                value = mbAvailable;
                break;
            case 6:
                value = mdDebit;
                break;
            case 7:
                value = mdCredit;
                break;
            case 8:
                value = mdBalanceStc;
                break;
            case 9:
                value = mdBalance;
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
