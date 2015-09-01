/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbStockMove extends DDbRegistry {

    protected int mnPkYearId;
    protected int mnPkItemId;
    protected int mnPkUnitId;
    protected int mnPkLotId;
    protected int mnPkBizPartnerId;
    protected int mnPkBranchId;
    protected int mnPkWarehouseId;
    protected int mnPkMoveId;
    protected Date mtDate;
    protected double mdMoveIn;
    protected double mdMoveOut;
    protected double mdCostUnitary;
    protected double mdCost;
    protected double mdDebit;
    protected double mdCredit;
    protected String msSerialNumber;
    protected String msImportDeclaration;
    protected Date mtImportDeclarationDate_n;
    protected boolean mbDeleted;
    protected int mnFkIogCategoryId;
    protected int mnFkIogClassId;
    protected int mnFkIogTypeId;
    protected int mnFkIogIogId;
    protected int mnFkIogRowId;
    protected int mnFkDpsInvDpsId_n;
    protected int mnFkDpsInvRowId_n;
    protected int mnFkDpsAdjDpsId_n;
    protected int mnFkDpsAdjRowId_n;
    protected int mnFkBookkeepingYearId_n;
    protected int mnFkBookkeepingNumberId_n;

    protected String msAuxLot;
    protected Date mtAuxDateExpiration_n;

    public DDbStockMove() {
        super(DModConsts.T_STK);
        initRegistry();
    }

    /*
     * Private methods
     */

    /*
     * Public methods
     */

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkUnitId(int n) { mnPkUnitId = n; }
    public void setPkLotId(int n) { mnPkLotId = n; }
    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setPkBranchId(int n) { mnPkBranchId = n; }
    public void setPkWarehouseId(int n) { mnPkWarehouseId = n; }
    public void setPkMoveId(int n) { mnPkMoveId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setMoveIn(double d) { mdMoveIn = d; }
    public void setMoveOut(double d) { mdMoveOut = d; }
    public void setCostUnitary(double d) { mdCostUnitary = d; }
    public void setCost(double d) { mdCost = d; }
    public void setDebit(double d) { mdDebit = d; }
    public void setCredit(double d) { mdCredit = d; }
    public void setSerialNumber(String s) { msSerialNumber = s; }
    public void setImportDeclaration(String s) { msImportDeclaration = s; }
    public void setImportDeclarationDate_n(Date t) { mtImportDeclarationDate_n = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkIogCategoryId(int n) { mnFkIogCategoryId = n; }
    public void setFkIogClassId(int n) { mnFkIogClassId = n; }
    public void setFkIogTypeId(int n) { mnFkIogTypeId = n; }
    public void setFkIogIogId(int n) { mnFkIogIogId = n; }
    public void setFkIogRowId(int n) { mnFkIogRowId = n; }
    public void setFkDpsInvDpsId_n(int n) { mnFkDpsInvDpsId_n = n; }
    public void setFkDpsInvRowId_n(int n) { mnFkDpsInvRowId_n = n; }
    public void setFkDpsAdjDpsId_n(int n) { mnFkDpsAdjDpsId_n = n; }
    public void setFkDpsAdjRowId_n(int n) { mnFkDpsAdjRowId_n = n; }
    public void setFkBookkeepingYearId_n(int n) { mnFkBookkeepingYearId_n = n; }
    public void setFkBookkeepingNumberId_n(int n) { mnFkBookkeepingNumberId_n = n; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkItemId() { return mnPkItemId; }
    public int getPkUnitId() { return mnPkUnitId; }
    public int getPkLotId() { return mnPkLotId; }
    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public int getPkBranchId() { return mnPkBranchId; }
    public int getPkWarehouseId() { return mnPkWarehouseId; }
    public int getPkMoveId() { return mnPkMoveId; }
    public Date getDate() { return mtDate; }
    public double getMoveIn() { return mdMoveIn; }
    public double getMoveOut() { return mdMoveOut; }
    public double getCostUnitary() { return mdCostUnitary; }
    public double getCost() { return mdCost; }
    public double getDebit() { return mdDebit; }
    public double getCredit() { return mdCredit; }
    public String getSerialNumber() { return msSerialNumber; }
    public String getImportDeclaration() { return msImportDeclaration; }
    public Date getImportDeclarationDate_n() { return mtImportDeclarationDate_n; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkIogCategoryId() { return mnFkIogCategoryId; }
    public int getFkIogClassId() { return mnFkIogClassId; }
    public int getFkIogTypeId() { return mnFkIogTypeId; }
    public int getFkIogIogId() { return mnFkIogIogId; }
    public int getFkIogRowId() { return mnFkIogRowId; }
    public int getFkDpsInvDpsId_n() { return mnFkDpsInvDpsId_n; }
    public int getFkDpsInvRowId_n() { return mnFkDpsInvRowId_n; }
    public int getFkDpsAdjDpsId_n() { return mnFkDpsAdjDpsId_n; }
    public int getFkDpsAdjRowId_n() { return mnFkDpsAdjRowId_n; }
    public int getFkBookkeepingYearId_n() { return mnFkBookkeepingYearId_n; }
    public int getFkBookkeepingNumberId_n() { return mnFkBookkeepingNumberId_n; }

    public void setAuxLot(String s) { msAuxLot = s; }
    public void setAuxDateExpiration_n(Date t) { mtAuxDateExpiration_n = t; }

    public String getAuxLot() { return msAuxLot; }
    public Date getAuxDateExpiration_n() { return mtAuxDateExpiration_n; }

    public int[] getDpsInvRowKey_n() { return mnFkDpsInvDpsId_n == DLibConsts.UNDEFINED || mnFkDpsInvRowId_n == DLibConsts.UNDEFINED ? null : new int[] { mnFkDpsInvDpsId_n, mnFkDpsInvRowId_n }; }
    public int[] getDpsAdjRowKey_n() { return mnFkDpsAdjDpsId_n == DLibConsts.UNDEFINED || mnFkDpsAdjRowId_n == DLibConsts.UNDEFINED ? null : new int[] { mnFkDpsAdjDpsId_n, mnFkDpsAdjRowId_n }; }
    public int[] getBookkeepingNumberKey_n() { return mnFkBookkeepingYearId_n == DLibConsts.UNDEFINED || mnFkBookkeepingNumberId_n == DLibConsts.UNDEFINED ? null : new int[] { mnFkBookkeepingYearId_n, mnFkBookkeepingNumberId_n }; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkYearId = pk[0];
        mnPkItemId = pk[1];
        mnPkUnitId = pk[2];
        mnPkLotId = pk[3];
        mnPkBizPartnerId = pk[4];
        mnPkBranchId = pk[5];
        mnPkWarehouseId = pk[6];
        mnPkMoveId = pk[7];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkItemId, mnPkUnitId, mnPkLotId, mnPkBizPartnerId, mnPkBranchId, mnPkWarehouseId, mnPkMoveId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkYearId = 0;
        mnPkItemId = 0;
        mnPkUnitId = 0;
        mnPkLotId = 0;
        mnPkBizPartnerId = 0;
        mnPkBranchId = 0;
        mnPkWarehouseId = 0;
        mnPkMoveId = 0;
        mtDate = null;
        mdMoveIn = 0;
        mdMoveOut = 0;
        mdCostUnitary = 0;
        mdCost = 0;
        mdDebit = 0;
        mdCredit = 0;
        msSerialNumber = "";
        msImportDeclaration = "";
        mtImportDeclarationDate_n = null;
        mbDeleted = false;
        mnFkIogCategoryId = 0;
        mnFkIogClassId = 0;
        mnFkIogTypeId = 0;
        mnFkIogIogId = 0;
        mnFkIogRowId = 0;
        mnFkDpsInvDpsId_n = 0;
        mnFkDpsInvRowId_n = 0;
        mnFkDpsAdjDpsId_n = 0;
        mnFkDpsAdjRowId_n = 0;
        mnFkBookkeepingYearId_n = 0;
        mnFkBookkeepingNumberId_n = 0;

        msAuxLot = "";
        mtAuxDateExpiration_n = null;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_yer = " + mnPkYearId + " AND " +
                "id_itm = " + mnPkItemId + " AND " +
                "id_unt = " + mnPkUnitId + " AND " +
                "id_lot = " + mnPkLotId + " AND " +
                "id_bpr = " + mnPkBizPartnerId + " AND " +
                "id_bra = " + mnPkBranchId + " AND " +
                "id_wah = " + mnPkWarehouseId + " AND " +
                "id_mov = " + mnPkMoveId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_yer = " + pk[0] + " " +
                "id_itm = " + pk[1] + " AND " +
                "id_unt = " + pk[2] + " AND " +
                "id_lot = " + pk[3] + " AND " +
                "id_bpr = " + pk[4] + " AND " +
                "id_bra = " + pk[5] + " AND " +
                "id_wah = " + pk[6] + " AND " +
                "id_mov = " + pk[7] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkMoveId = 0;

        msSql = "SELECT COALESCE(MAX(id_mov), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_yer = " + mnPkYearId + " AND " +
                "id_itm = " + mnPkItemId + " AND " +
                "id_unt = " + mnPkUnitId + " AND " +
                "id_lot = " + mnPkLotId + " AND " +
                "id_bpr = " + mnPkBizPartnerId + " AND " +
                "id_bra = " + mnPkBranchId + " AND " +
                "id_wah = " + mnPkWarehouseId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkMoveId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(DGuiSession session, int[] pk) throws SQLException, Exception {
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = DDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkYearId = resultSet.getInt("id_yer");
            mnPkItemId = resultSet.getInt("id_itm");
            mnPkUnitId = resultSet.getInt("id_unt");
            mnPkLotId = resultSet.getInt("id_lot");
            mnPkBizPartnerId = resultSet.getInt("id_bpr");
            mnPkBranchId = resultSet.getInt("id_bra");
            mnPkWarehouseId = resultSet.getInt("id_wah");
            mnPkMoveId = resultSet.getInt("id_mov");
            mtDate = resultSet.getDate("dt");
            mdMoveIn = resultSet.getDouble("mov_in");
            mdMoveOut = resultSet.getDouble("mov_out");
            mdCostUnitary = resultSet.getDouble("cst_unt");
            mdCost = resultSet.getDouble("cst");
            mdDebit = resultSet.getDouble("dbt");
            mdCredit = resultSet.getDouble("cdt");
            msSerialNumber = resultSet.getString("snr");
            msImportDeclaration = resultSet.getString("imp_dec");
            mtImportDeclarationDate_n = resultSet.getDate("imp_dec_dt_n");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkIogCategoryId = resultSet.getInt("fk_iog_ct");
            mnFkIogClassId = resultSet.getInt("fk_iog_cl");
            mnFkIogTypeId = resultSet.getInt("fk_iog_tp");
            mnFkIogIogId = resultSet.getInt("fk_iog_iog");
            mnFkIogRowId = resultSet.getInt("fk_iog_row");
            mnFkDpsInvDpsId_n = resultSet.getInt("fk_dps_inv_dps_n");
            mnFkDpsInvRowId_n = resultSet.getInt("fk_dps_inv_row_n");
            mnFkDpsAdjDpsId_n = resultSet.getInt("fk_dps_adj_dps_n");
            mnFkDpsAdjRowId_n = resultSet.getInt("fk_dps_adj_row_n");
            mnFkBookkeepingYearId_n = resultSet.getInt("fk_bkk_yer_n");
            mnFkBookkeepingNumberId_n = resultSet.getInt("fk_bkk_num_n");

            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        int[] key = null;
        DDbLot lot = null;
        DDbStockConfig stockConfig = null;

        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        // Create lot if needed:

        key = new int[] { mnPkItemId, mnPkUnitId, mnPkLotId };

        if (mnPkLotId == DUtilConsts.LOT_ID) {
            // Create default stock lot if necessary:

            lot = (DDbLot) session.readRegistry(DModConsts.T_LOT, key, DDbConsts.MODE_STEALTH);
            if (lot.getQueryResultId() != DDbConsts.READ_OK) {
                lot = new DDbLot();
                lot.setPrimaryKey(key);
                lot.save(session);
            }
        }

        if (!msAuxLot.isEmpty()) {
            if (mnPkLotId == DUtilConsts.LOT_ID) {
                // Create new stock lot:

                lot = new DDbLot();
                lot.setPrimaryKey(key);
                lot.setLot(msAuxLot);
                lot.setDateExpiration_n(mtAuxDateExpiration_n);
                lot.save(session);

                mnPkLotId = lot.getPkLotId();
            }
            else if (mnPkLotId >= DUtilConsts.LOT_ID) {
                // Check existing stock lot:

                lot = (DDbLot) session.readRegistry(DModConsts.T_LOT, key, DDbConsts.MODE_STEALTH);
                if (lot.getQueryResultId() != DDbConsts.READ_OK) {
                    throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
                }
                else if (lot.isDeleted()) {
                    lot.delete(session);
                }
            }
        }

        // Create stock configuration if needed:

        key = new int[] { mnPkItemId, mnPkUnitId, mnPkBizPartnerId, mnPkBranchId, mnPkWarehouseId };
        stockConfig = (DDbStockConfig) session.readRegistry(DModConsts.T_STK_CFG, key, DDbConsts.MODE_STEALTH);
        if (stockConfig.getQueryResultId() != DDbConsts.READ_OK) {
            stockConfig = new DDbStockConfig();
            stockConfig.setPrimaryKey(key);
            stockConfig.save(session);
        }

        // Save registry:

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkYearId + ", " +
                    mnPkItemId + ", " +
                    mnPkUnitId + ", " +
                    mnPkLotId + ", " +
                    mnPkBizPartnerId + ", " +
                    mnPkBranchId + ", " +
                    mnPkWarehouseId + ", " +
                    mnPkMoveId + ", " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    mdMoveIn + ", " +
                    mdMoveOut + ", " +
                    mdCostUnitary + ", " +
                    mdCost + ", " +
                    mdDebit + ", " +
                    mdCredit + ", " +
                    "'" + msSerialNumber + "', " +
                    "'" + msImportDeclaration + "', " +
                    (mtImportDeclarationDate_n == null ? "NULL" : "'" + DLibUtils.DbmsDateFormatDate.format(mtImportDeclarationDate_n) + "'") + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkIogCategoryId + ", " +
                    mnFkIogClassId + ", " +
                    mnFkIogTypeId + ", " +
                    mnFkIogIogId + ", " +
                    mnFkIogRowId + ", " +
                    (mnFkDpsInvDpsId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsInvDpsId_n) + ", " +
                    (mnFkDpsInvRowId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsInvRowId_n) + ", " +
                    (mnFkDpsAdjDpsId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsAdjDpsId_n) + ", " +
                    (mnFkDpsAdjRowId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsAdjRowId_n) + ", " +
                    (mnFkBookkeepingYearId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBookkeepingYearId_n) + ", " +
                    (mnFkBookkeepingNumberId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBookkeepingNumberId_n) + " " +
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_yer = " + mnPkYearId + ", " +
                    "id_itm = " + mnPkItemId + ", " +
                    "id_unt = " + mnPkUnitId + ", " +
                    "id_lot = " + mnPkLotId + ", " +
                    "id_bpr = " + mnPkBizPartnerId + ", " +
                    "id_bra = " + mnPkBranchId + ", " +
                    "id_wah = " + mnPkWarehouseId + ", " +
                    "id_mov = " + mnPkMoveId + ", " +
                    */
                    "dt = '" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "mov_in = " + mdMoveIn + ", " +
                    "mov_out = " + mdMoveOut + ", " +
                    "cst_unt = " + mdCostUnitary + ", " +
                    "cst = " + mdCost + ", " +
                    "dbt = " + mdDebit + ", " +
                    "cdt = " + mdCredit + ", " +
                    "snr = '" + msSerialNumber + "', " +
                    "imp_dec = '" + msImportDeclaration + "', " +
                    "imp_dec_dt_n = " + (mtImportDeclarationDate_n == null ? "NULL" : "'" + DLibUtils.DbmsDateFormatDate.format(mtImportDeclarationDate_n) + "'") + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_iog_ct = " + mnFkIogCategoryId + ", " +
                    "fk_iog_cl = " + mnFkIogClassId + ", " +
                    "fk_iog_tp = " + mnFkIogTypeId + ", " +
                    "fk_iog_iog = " + mnFkIogIogId + ", " +
                    "fk_iog_row = " + mnFkIogRowId + ", " +
                    "fk_dps_inv_dps_n = " + (mnFkDpsInvDpsId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsInvDpsId_n) + ", " +
                    "fk_dps_inv_row_n = " + (mnFkDpsInvRowId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsInvRowId_n) + ", " +
                    "fk_dps_adj_dps_n = " + (mnFkDpsAdjDpsId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsAdjDpsId_n) + ", " +
                    "fk_dps_adj_row_n = " + (mnFkDpsAdjRowId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsAdjRowId_n) + ", " +
                    "fk_bkk_yer_n = " + (mnFkBookkeepingYearId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBookkeepingYearId_n) + ", " +
                    "fk_bkk_num_n = " + (mnFkBookkeepingNumberId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBookkeepingNumberId_n) + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbStockMove clone() throws CloneNotSupportedException {
        DDbStockMove registry = new DDbStockMove();

        registry.setPkYearId(this.getPkYearId());
        registry.setPkItemId(this.getPkItemId());
        registry.setPkUnitId(this.getPkUnitId());
        registry.setPkLotId(this.getPkLotId());
        registry.setPkBizPartnerId(this.getPkBizPartnerId());
        registry.setPkBranchId(this.getPkBranchId());
        registry.setPkWarehouseId(this.getPkWarehouseId());
        registry.setPkMoveId(this.getPkMoveId());
        registry.setDate(this.getDate());
        registry.setMoveIn(this.getMoveIn());
        registry.setMoveOut(this.getMoveOut());
        registry.setCostUnitary(this.getCostUnitary());
        registry.setCost(this.getCost());
        registry.setDebit(this.getDebit());
        registry.setCredit(this.getCredit());
        registry.setSerialNumber(this.getSerialNumber());
        registry.setImportDeclaration(this.getImportDeclaration());
        registry.setImportDeclarationDate_n(this.getImportDeclarationDate_n());
        registry.setDeleted(this.isDeleted());
        registry.setFkIogCategoryId(this.getFkIogCategoryId());
        registry.setFkIogClassId(this.getFkIogClassId());
        registry.setFkIogTypeId(this.getFkIogTypeId());
        registry.setFkIogIogId(this.getFkIogIogId());
        registry.setFkIogRowId(this.getFkIogRowId());
        registry.setFkDpsInvDpsId_n(this.getFkDpsInvDpsId_n());
        registry.setFkDpsInvRowId_n(this.getFkDpsInvRowId_n());
        registry.setFkDpsAdjDpsId_n(this.getFkDpsAdjDpsId_n());
        registry.setFkDpsAdjRowId_n(this.getFkDpsAdjRowId_n());
        registry.setFkBookkeepingYearId_n(this.getFkBookkeepingYearId_n());
        registry.setFkBookkeepingNumberId_n(this.getFkBookkeepingNumberId_n());

        registry.setAuxLot(this.getAuxLot());
        registry.setAuxDateExpiration_n(this.getAuxDateExpiration_n());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public boolean canSave(final DGuiSession session) throws SQLException, Exception {
        boolean canSave = super.canSave(session);

        if (canSave) {
            if (mnPkItemId == DLibConsts.UNDEFINED) {
                canSave = false;
                msQueryResult = DGuiConsts.ERR_MSG_FIELD_REQ + "'" + DUtilConsts.TXT_ITEM + "'";
            }
            else if (mnPkUnitId == DLibConsts.UNDEFINED) {
                canSave = false;
                msQueryResult = DGuiConsts.ERR_MSG_FIELD_REQ + "'" + DUtilConsts.TXT_UNIT + "'";
            }
            else if (mnPkLotId == DLibConsts.UNDEFINED) {
                canSave = false;
                msQueryResult = DGuiConsts.ERR_MSG_FIELD_REQ + "'" + DUtilConsts.TXT_LOT + "'";
            }
        }

        return canSave;
    }

    public void computeValue() {
        int decs = DLibUtils.getDecimalFormatAmount().getMaximumFractionDigits();

        if (mnFkIogCategoryId == DModSysConsts.TS_IOG_CT_IN) {
            mdDebit = DLibUtils.round(mdMoveIn * mdCostUnitary, decs);
            mdCredit = 0;
        }
        else {
            mdDebit = 0;
            mdCredit = DLibUtils.round(mdMoveOut * mdCostUnitary, decs);
        }
    }
}
