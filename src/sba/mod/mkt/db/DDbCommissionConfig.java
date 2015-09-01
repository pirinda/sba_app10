/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.mkt.db;

/**
 *
 * @author Sergio Flores
 */
public interface DDbCommissionConfig {

    public int getPkLinkOwnerTypeId();
    public int getPkReferenceOwnerId();
    public int getPkLinkItemTypeId();
    public int getPkReferenceItemId();
    public double getCommissionPercentage();
    public double getCommissionUnitary();
    public boolean isDeleted();
    public int getFkCommissionTypeId();
}
