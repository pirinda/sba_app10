/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import java.util.Date;
import sba.lib.DLibUtils;

/**
 *
 * @author Sergio Flores
 */
public class DTrnStockMove {

    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected int mnPkLotId;
    protected int mnPkBizPartnerId;
    protected int mnPkBranchId;
    protected int mnPkWarehouseId;
    protected double mdQuantity;
    protected String msSerialNumber;
    protected String msImportDeclaration;
    protected Date mtImportDeclarationDate;
    protected String msLot;
    protected Date mtDateExpiration;

    /**
     * @param stockMoveKey Stock move primary key. Integer array of length of 6: item ID, unit ID, lot ID and warehouse ID (i.e. business partner ID, branch ID and warehouse ID)
     * @param quantity Stock move quantity.
     */
    public DTrnStockMove(int[] stockMoveKey, double quantity) {
        this(stockMoveKey, quantity, "", "", null, "", null);
    }

    /**
     * @param stockMoveKey Stock move primary key. Integer array of length of 6: item ID, unit ID, lot ID and warehouse ID (i.e. business partner ID, branch ID and warehouse ID)
     * @param quantity Stock move quantity.
     * @param serialNumber Stock move serial move.
     */
    public DTrnStockMove(int[] stockMoveKey, double quantity, String serialNumber) {
        this(stockMoveKey, quantity, serialNumber, "", null, "", null);
    }

    /**
     * @param stockMoveKey Stock move primary key. Integer array of length of 6: item ID, unit ID, lot ID and warehouse ID (i.e. business partner ID, branch ID and warehouse ID)
     * @param quantity Stock move quantity.
     * @param importDeclaration Stock move import (customs) declaration.
     * @param importDeclarationDate  Stock move import (customs) declaration date.
     */
    public DTrnStockMove(int[] stockMoveKey, double quantity, String serialNumber, String importDeclaration, Date importDeclarationDate) {
        this(stockMoveKey, quantity, serialNumber, importDeclaration, importDeclarationDate, "", null);
    }

    /**
     * @param stockMoveKey Stock move primary key. Integer array of length of 6: item ID, unit ID, lot ID and warehouse ID (i.e. business partner ID, branch ID and warehouse ID)
     * @param quantity Stock move quantity.
     * @param importDeclaration Stock move import (customs) declaration.
     * @param importDeclarationDate  Stock move import (customs) declaration date.
     * @param lot Stock move lot.
     * @param dateExpiration  Stock move date expiration (lot).
     */
    public DTrnStockMove(int[] stockMoveKey, double quantity, String serialNumber, String importDeclaration, Date importDeclarationDate, String lot, Date dateExpiration) {
        mnPkItemId = stockMoveKey[0];
        mnPkUnitId = stockMoveKey[1];
        mnPkLotId = stockMoveKey[2];
        mnPkBizPartnerId = stockMoveKey[3];
        mnPkBranchId = stockMoveKey[4];
        mnPkWarehouseId = stockMoveKey[5];
        mdQuantity = quantity;
        msSerialNumber = serialNumber;
        msImportDeclaration = importDeclaration;
        mtImportDeclarationDate = importDeclarationDate;
        msLot = lot;
        mtDateExpiration = dateExpiration;
    }

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setPkLotId(int n) { mnPkLotId = n; }
    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setPkBranchId(int n) { mnPkBranchId = n; }
    public void setPkWarehouseId(int n) { mnPkWarehouseId = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setSerialNumber(String s) { msSerialNumber = s; }
    public void setImportDeclaration(String s) { msImportDeclaration = s; }
    public void setImportDeclarationDate(Date t) { mtImportDeclarationDate = t; }
    public void setLot(String s) { msLot = s; }
    public void setDateExpiration(Date t) { mtDateExpiration = t; }

    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public int getPkLotId() { return mnPkLotId; }
    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public int getPkBranchId() { return mnPkBranchId; }
    public int getPkWarehouseId() { return mnPkWarehouseId; }
    public double getQuantity() { return mdQuantity; }
    public String getSerialNumber() { return msSerialNumber; }
    public String getImportDeclaration() { return msImportDeclaration; }
    public Date getImportDeclarationDate() { return mtImportDeclarationDate; }
    public String getLot() { return msLot; }
    public Date getDateExpiration() { return mtDateExpiration; }

    public int[] getLotKey() { return new int[] { mnPkItemId, mnPkUnitId, mnPkLotId }; }
    public int[] getWarehouseKey() { return new int[] { mnPkBizPartnerId, mnPkBranchId, mnPkWarehouseId }; }
    public int[] getStockMoveKey() { return new int[] { mnPkItemId, mnPkUnitId, mnPkLotId, mnPkBizPartnerId, mnPkBranchId, mnPkWarehouseId }; }

    @Override
    public String toString() {
        return msSerialNumber + (mdQuantity == 1 ? "" : " (" + DLibUtils.getDecimalFormatQuantity().format(mdQuantity) + ")");
    }

    @Override
    public DTrnStockMove clone() throws CloneNotSupportedException {
        return new DTrnStockMove(this.getStockMoveKey(), this.getQuantity(), this.getSerialNumber(), this.getImportDeclaration(), this.getImportDeclarationDate(), this.getLot(), this.getDateExpiration());
    }
}
