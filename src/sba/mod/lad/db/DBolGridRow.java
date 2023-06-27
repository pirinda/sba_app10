/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.lad.db;

/**
 *
 * @author Sergio Flores
 */
public interface DBolGridRow {
    
    public void setBolUpdateOwnRegistry(boolean update);
    public void setBolSortingPos(int n);
    
    public boolean isBolUpdateOwnRegistry();
    public int getBolSortingPos();
}
