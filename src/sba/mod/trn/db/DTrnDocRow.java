/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.trn.db;

import java.util.Vector;

/**
 *
 * @author Sergio Flores
 */
public interface DTrnDocRow {

    public int getRowNumber();
    public String getItemCode();
    public String getItemName();
    public String getUnitCode();
    public Vector<DTrnStockMove> getStockMoves();
}
