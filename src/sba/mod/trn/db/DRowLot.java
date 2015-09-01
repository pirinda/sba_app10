/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.trn.db;

import sba.lib.grid.DGridRowCustom;

/**
 *
 * @author Sergio Flores
 */
public class DRowLot extends DGridRowCustom {

    protected DTrnStockMove moStockMove;

    public DRowLot(DTrnStockMove stockMove) {
        super(stockMove.getStockMoveKey(), "", "");
        moStockMove = stockMove;
    }

    public void setStockMove(DTrnStockMove o) { moStockMove = o; }

    public DTrnStockMove getStockMove() { return moStockMove; }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = moStockMove.getDateExpiration();
                break;
            case 1:
                value = moStockMove.getLot();
                break;
            case 2:
                value = moStockMove.getQuantity();
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
