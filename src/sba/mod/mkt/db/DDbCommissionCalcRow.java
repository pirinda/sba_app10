/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.mkt.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import sba.gui.util.DUtilConsts;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbCommissionCalcRow extends DDbRegistryUser {

    protected int mnPkCommissionCalcId;
    protected int mnPkReferenceId;
    protected int mnPkRowId;
    protected double mdQuantity;
    protected double mdQuantityInc;
    protected double mdQuantityDec;
    protected double mdQuantityNet_r;
    protected double mdValue;
    protected double mdValueInc;
    protected double mdValueDec;
    protected double mdValueNet_r;
    protected double mdCommissionPercentage;
    protected double mdCommissionUnitary;
    protected double mdCommission_r;
    protected boolean mbFreeOfCommission;
    protected int mnFkCommissionTypeId;
    protected int mnFkLinkOwnerTypeId;
    protected int mnFkReferenceOwnerId;
    protected int mnFkLinkItemTypeId;
    protected int mnFkReferenceItemId;
    protected int mnFkItemId;

    public DDbCommissionCalcRow() {
        super(DModConsts.M_CMM_CAL_ROW);
        initRegistry();
    }

    public void setPkCommissionCalcId(int n) { mnPkCommissionCalcId = n; }
    public void setPkReferenceId(int n) { mnPkReferenceId = n; }
    public void setPkRowId(int n) { mnPkRowId = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setQuantityInc(double d) { mdQuantityInc = d; }
    public void setQuantityDec(double d) { mdQuantityDec = d; }
    public void setQuantityNet_r(double d) { mdQuantityNet_r = d; }
    public void setValue(double d) { mdValue = d; }
    public void setValueInc(double d) { mdValueInc = d; }
    public void setValueDec(double d) { mdValueDec = d; }
    public void setValueNet_r(double d) { mdValueNet_r = d; }
    public void setCommissionPercentage(double d) { mdCommissionPercentage = d; }
    public void setCommissionUnitary(double d) { mdCommissionUnitary = d; }
    public void setCommission_r(double d) { mdCommission_r = d; }
    public void setFreeOfCommission(boolean b) { mbFreeOfCommission = b; }
    public void setFkCommissionTypeId(int n) { mnFkCommissionTypeId = n; }
    public void setFkLinkOwnerTypeId(int n) { mnFkLinkOwnerTypeId = n; }
    public void setFkReferenceOwnerId(int n) { mnFkReferenceOwnerId = n; }
    public void setFkLinkItemTypeId(int n) { mnFkLinkItemTypeId = n; }
    public void setFkReferenceItemId(int n) { mnFkReferenceItemId = n; }
    public void setFkItemId(int n) { mnFkItemId = n; }

    public int getPkCommissionCalcId() { return mnPkCommissionCalcId; }
    public int getPkReferenceId() { return mnPkReferenceId; }
    public int getPkRowId() { return mnPkRowId; }
    public double getQuantity() { return mdQuantity; }
    public double getQuantityInc() { return mdQuantityInc; }
    public double getQuantityDec() { return mdQuantityDec; }
    public double getQuantityNet_r() { return mdQuantityNet_r; }
    public double getValue() { return mdValue; }
    public double getValueInc() { return mdValueInc; }
    public double getValueDec() { return mdValueDec; }
    public double getValueNet_r() { return mdValueNet_r; }
    public double getCommissionPercentage() { return mdCommissionPercentage; }
    public double getCommissionUnitary() { return mdCommissionUnitary; }
    public double getCommission_r() { return mdCommission_r; }
    public boolean isFreeOfCommission() { return mbFreeOfCommission; }
    public int getFkCommissionTypeId() { return mnFkCommissionTypeId; }
    public int getFkLinkOwnerTypeId() { return mnFkLinkOwnerTypeId; }
    public int getFkReferenceOwnerId() { return mnFkReferenceOwnerId; }
    public int getFkLinkItemTypeId() { return mnFkLinkItemTypeId; }
    public int getFkReferenceItemId() { return mnFkReferenceItemId; }
    public int getFkItemId() { return mnFkItemId; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkCommissionCalcId = pk[0];
        mnPkReferenceId = pk[1];
        mnPkRowId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCommissionCalcId, mnPkReferenceId, mnPkRowId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkCommissionCalcId = 0;
        mnPkReferenceId = 0;
        mnPkRowId = 0;
        mdQuantity = 0;
        mdQuantityInc = 0;
        mdQuantityDec = 0;
        mdQuantityNet_r = 0;
        mdValue = 0;
        mdValueInc = 0;
        mdValueDec = 0;
        mdValueNet_r = 0;
        mdCommissionPercentage = 0;
        mdCommissionUnitary = 0;
        mdCommission_r = 0;
        mbFreeOfCommission = false;
        mnFkCommissionTypeId = 0;
        mnFkLinkOwnerTypeId = 0;
        mnFkReferenceOwnerId = 0;
        mnFkLinkItemTypeId = 0;
        mnFkReferenceItemId = 0;
        mnFkItemId = 0;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_cmm = " + mnPkCommissionCalcId + " AND " +
                "id_ref = " + mnPkReferenceId + " AND " +
                "id_row = " + mnPkRowId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_cmm = " + pk[0] + " AND " +
                "id_ref = " + pk[1] + " AND " +
                "id_row = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkRowId = 0;

        msSql = "SELECT COALESCE(MAX(id_row), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_cmm = " + mnPkCommissionCalcId + " AND " +
                "id_ref = " + mnPkReferenceId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkRowId = resultSet.getInt(1);
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
            mnPkCommissionCalcId = resultSet.getInt("id_cmm");
            mnPkReferenceId = resultSet.getInt("id_ref");
            mnPkRowId = resultSet.getInt("id_row");
            mdQuantity = resultSet.getDouble("qty");
            mdQuantityInc = resultSet.getDouble("qty_inc");
            mdQuantityDec = resultSet.getDouble("qty_dec");
            mdQuantityNet_r = resultSet.getDouble("qty_net_r");
            mdValue = resultSet.getDouble("val");
            mdValueInc = resultSet.getDouble("val_inc");
            mdValueDec = resultSet.getDouble("val_dec");
            mdValueNet_r = resultSet.getDouble("val_net_r");
            mdCommissionPercentage = resultSet.getDouble("cmm_per");
            mdCommissionUnitary = resultSet.getDouble("cmm_unt");
            mdCommission_r = resultSet.getDouble("cmm_r");
            mbFreeOfCommission = resultSet.getBoolean("b_fre_cmm");
            mnFkCommissionTypeId = resultSet.getInt("fk_cmm_tp");
            mnFkLinkOwnerTypeId = resultSet.getInt("fk_lnk_own_tp");
            mnFkReferenceOwnerId = resultSet.getInt("fk_ref_own");
            mnFkLinkItemTypeId = resultSet.getInt("fk_lnk_itm_tp");
            mnFkReferenceItemId = resultSet.getInt("fk_ref_itm");
            mnFkItemId = resultSet.getInt("fk_itm");

            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkCommissionCalcId + ", " +
                    mnPkReferenceId + ", " +
                    mnPkRowId + ", " +
                    mdQuantity + ", " +
                    mdQuantityInc + ", " +
                    mdQuantityDec + ", " +
                    mdQuantityNet_r + ", " +
                    mdValue + ", " +
                    mdValueInc + ", " +
                    mdValueDec + ", " +
                    mdValueNet_r + ", " +
                    mdCommissionPercentage + ", " +
                    mdCommissionUnitary + ", " +
                    mdCommission_r + ", " +
                    (mbFreeOfCommission ? 1 : 0) + ", " +
                    mnFkCommissionTypeId + ", " +
                    mnFkLinkOwnerTypeId + ", " +
                    mnFkReferenceOwnerId + ", " +
                    mnFkLinkItemTypeId + ", " +
                    mnFkReferenceItemId + ", " +
                    mnFkItemId + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_cmm = " + mnPkCommissionCalcId + ", " +
                    "id_ref = " + mnPkReferenceId + ", " +
                    "id_row = " + mnPkRowId + ", " +
                    */
                    "qty = " + mdQuantity + ", " +
                    "qty_inc = " + mdQuantityInc + ", " +
                    "qty_dec = " + mdQuantityDec + ", " +
                    "qty_net_r = " + mdQuantityNet_r + ", " +
                    "val = " + mdValue + ", " +
                    "val_inc = " + mdValueInc + ", " +
                    "val_dec = " + mdValueDec + ", " +
                    "val_net_r = " + mdValueNet_r + ", " +
                    "cmm_per = " + mdCommissionPercentage + ", " +
                    "cmm_unt = " + mdCommissionUnitary + ", " +
                    "cmm_r = " + mdCommission_r + ", " +
                    "b_fre_cmm = " + (mbFreeOfCommission ? 1 : 0) + ", " +
                    "fk_cmm_tp = " + mnFkCommissionTypeId + ", " +
                    "fk_lnk_own_tp = " + mnFkLinkOwnerTypeId + ", " +
                    "fk_ref_own = " + mnFkReferenceOwnerId + ", " +
                    "fk_lnk_itm_tp = " + mnFkLinkItemTypeId + ", " +
                    "fk_ref_itm = " + mnFkReferenceItemId + ", " +
                    "fk_itm = " + mnFkItemId + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbCommissionCalcRow clone() throws CloneNotSupportedException {
        DDbCommissionCalcRow registry = new DDbCommissionCalcRow();

        registry.setPkCommissionCalcId(this.getPkCommissionCalcId());
        registry.setPkReferenceId(this.getPkReferenceId());
        registry.setPkRowId(this.getPkRowId());
        registry.setQuantity(this.getQuantity());
        registry.setQuantityInc(this.getQuantityInc());
        registry.setQuantityDec(this.getQuantityDec());
        registry.setQuantityNet_r(this.getQuantityNet_r());
        registry.setValue(this.getValue());
        registry.setValueInc(this.getValueInc());
        registry.setValueDec(this.getValueDec());
        registry.setValueNet_r(this.getValueNet_r());
        registry.setCommissionPercentage(this.getCommissionPercentage());
        registry.setCommissionUnitary(this.getCommissionUnitary());
        registry.setCommission_r(this.getCommission_r());
        registry.setFreeOfCommission(this.isFreeOfCommission());
        registry.setFkCommissionTypeId(this.getFkCommissionTypeId());
        registry.setFkLinkOwnerTypeId(this.getFkLinkOwnerTypeId());
        registry.setFkReferenceOwnerId(this.getFkReferenceOwnerId());
        registry.setFkLinkItemTypeId(this.getFkLinkItemTypeId());
        registry.setFkReferenceItemId(this.getFkReferenceItemId());
        registry.setFkItemId(this.getFkItemId());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
