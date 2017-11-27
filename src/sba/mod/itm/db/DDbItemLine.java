/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.itm.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbItemLine extends DDbRegistryUser {

    protected int mnPkItemLineId;
    protected String msCode;
    protected String msName;
    protected String msCfdItemKey;
    protected boolean mbBulk;
    protected boolean mbInventoriable;
    protected boolean mbLotApplying;
    protected boolean mbSerialNumberApplying;
    protected boolean mbFreeOfPrice;
    protected boolean mbFreeOfDiscount;
    protected boolean mbFreeOfCommission;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkItemGenusId;
    protected int mnFkBrandId;
    protected int mnFkManufacturerId;
    protected int mnFkComponentId;
    protected int mnFkDepartmentId;
    protected int mnFkUnitId;
    protected int mnFkTaxGroupId_n;
    protected int mnFkAbpItemId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public DDbItemLine() {
        super(DModConsts.IU_LIN);
        initRegistry();
    }

    public void setPkItemLineId(int n) { mnPkItemLineId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setCfdItemKey(String s) { msCfdItemKey = s; }
    public void setBulk(boolean b) { mbBulk = b; }
    public void setInventoriable(boolean b) { mbInventoriable = b; }
    public void setLotApplying(boolean b) { mbLotApplying = b; }
    public void setSerialNumberApplying(boolean b) { mbSerialNumberApplying = b; }
    public void setFreeOfPrice(boolean b) { mbFreeOfPrice = b; }
    public void setFreeOfDiscount(boolean b) { mbFreeOfDiscount = b; }
    public void setFreeOfCommission(boolean b) { mbFreeOfCommission = b; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkItemGenusId(int n) { mnFkItemGenusId = n; }
    public void setFkBrandId(int n) { mnFkBrandId = n; }
    public void setFkManufacturerId(int n) { mnFkManufacturerId = n; }
    public void setFkComponentId(int n) { mnFkComponentId = n; }
    public void setFkDepartmentId(int n) { mnFkDepartmentId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkTaxGroupId_n(int n) { mnFkTaxGroupId_n = n; }
    public void setFkAbpItemId_n(int n) { mnFkAbpItemId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkItemLineId() { return mnPkItemLineId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getCfdItemKey() { return msCfdItemKey; }
    public boolean isBulk() { return mbBulk; }
    public boolean isInventoriable() { return mbInventoriable; }
    public boolean isLotApplying() { return mbLotApplying; }
    public boolean isSerialNumberApplying() { return mbSerialNumberApplying; }
    public boolean isFreeOfPrice() { return mbFreeOfPrice; }
    public boolean isFreeOfDiscount() { return mbFreeOfDiscount; }
    public boolean isFreeOfCommission() { return mbFreeOfCommission; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkItemGenusId() { return mnFkItemGenusId; }
    public int getFkBrandId() { return mnFkBrandId; }
    public int getFkManufacturerId() { return mnFkManufacturerId; }
    public int getFkComponentId() { return mnFkComponentId; }
    public int getFkDepartmentId() { return mnFkDepartmentId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkTaxGroupId_n() { return mnFkTaxGroupId_n; }
    public int getFkAbpItemId_n() { return mnFkAbpItemId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkItemLineId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkItemLineId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkItemLineId = 0;
        msCode = "";
        msName = "";
        msCfdItemKey = "";
        mbBulk = false;
        mbInventoriable = false;
        mbLotApplying = false;
        mbSerialNumberApplying = false;
        mbFreeOfPrice = false;
        mbFreeOfDiscount = false;
        mbFreeOfCommission = false;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkItemGenusId = 0;
        mnFkBrandId = 0;
        mnFkManufacturerId = 0;
        mnFkComponentId = 0;
        mnFkDepartmentId = 0;
        mnFkUnitId = 0;
        mnFkTaxGroupId_n = 0;
        mnFkAbpItemId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_lin = " + mnPkItemLineId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_lin = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkItemLineId = 0;

        msSql = "SELECT COALESCE(MAX(id_lin), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkItemLineId = resultSet.getInt(1);
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
            mnPkItemLineId = resultSet.getInt("id_lin");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msCfdItemKey = resultSet.getString("cfd_itm_key");
            mbBulk = resultSet.getBoolean("b_buk");
            mbInventoriable = resultSet.getBoolean("b_inv");
            mbLotApplying = resultSet.getBoolean("b_lot");
            mbSerialNumberApplying = resultSet.getBoolean("b_snr");
            mbFreeOfPrice = resultSet.getBoolean("b_fre_prc");
            mbFreeOfDiscount = resultSet.getBoolean("b_fre_dsc");
            mbFreeOfCommission = resultSet.getBoolean("b_fre_cmm");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkItemGenusId = resultSet.getInt("fk_gen");
            mnFkBrandId = resultSet.getInt("fk_brd");
            mnFkManufacturerId = resultSet.getInt("fk_mfr");
            mnFkComponentId = resultSet.getInt("fk_cmp");
            mnFkDepartmentId = resultSet.getInt("fk_dep");
            mnFkUnitId = resultSet.getInt("fk_unt");
            mnFkTaxGroupId_n = resultSet.getInt("fk_tax_grp_n");
            mnFkAbpItemId_n = resultSet.getInt("fk_abp_itm_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

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
            mbUpdatable = true;
            mbDisableable = true;
            mbDeletable = true;
            mbDisabled = false;
            mbDeleted = false;
            //mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkItemLineId + ", " +
                    "'" + (msCode.length() > 0 ? msCode : "" + mnPkItemLineId) + "', " +
                    "'" + msName + "', " +
                    "'" + msCfdItemKey + "', " + 
                    (mbBulk ? 1 : 0) + ", " +
                    (mbInventoriable ? 1 : 0) + ", " +
                    (mbLotApplying ? 1 : 0) + ", " +
                    (mbSerialNumberApplying ? 1 : 0) + ", " +
                    (mbFreeOfPrice ? 1 : 0) + ", " +
                    (mbFreeOfDiscount ? 1 : 0) + ", " +
                    (mbFreeOfCommission ? 1 : 0) + ", " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkItemGenusId + ", " +
                    mnFkBrandId + ", " +
                    mnFkManufacturerId + ", " +
                    mnFkComponentId + ", " +
                    mnFkDepartmentId + ", " +
                    mnFkUnitId + ", " +
                    (mnFkTaxGroupId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkTaxGroupId_n) + ", " +
                    (mnFkAbpItemId_n  == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAbpItemId_n) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_lin = " + mnPkItemLineId + ", " +
                    "code = '" + (msCode.length() > 0 ? msCode : "" + mnPkItemLineId) + "', " +
                    "name = '" + msName + "', " +
                    "cfd_itm_key = '" + msCfdItemKey + "', " +
                    "b_buk = " + (mbBulk ? 1 : 0) + ", " +
                    "b_inv = " + (mbInventoriable ? 1 : 0) + ", " +
                    "b_lot = " + (mbLotApplying ? 1 : 0) + ", " +
                    "b_snr = " + (mbSerialNumberApplying ? 1 : 0) + ", " +
                    "b_fre_prc = " + (mbFreeOfPrice ? 1 : 0) + ", " +
                    "b_fre_dsc = " + (mbFreeOfDiscount ? 1 : 0) + ", " +
                    "b_fre_cmm = " + (mbFreeOfCommission ? 1 : 0) + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_gen = " + mnFkItemGenusId + ", " +
                    "fk_brd = " + mnFkBrandId + ", " +
                    "fk_mfr = " + mnFkManufacturerId + ", " +
                    "fk_cmp = " + mnFkComponentId + ", " +
                    "fk_dep = " + mnFkDepartmentId + ", " +
                    "fk_unt = " + mnFkUnitId + ", " +
                    "fk_tax_grp_n = " + (mnFkTaxGroupId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkTaxGroupId_n) + ", " +
                    "fk_abp_itm_n = " + (mnFkAbpItemId_n  == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAbpItemId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbItemLine clone() throws CloneNotSupportedException {
        DDbItemLine registry = new DDbItemLine();

        registry.setPkItemLineId(this.getPkItemLineId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setCfdItemKey(this.getCfdItemKey());
        registry.setBulk(this.isBulk());
        registry.setInventoriable(this.isInventoriable());
        registry.setLotApplying(this.isLotApplying());
        registry.setSerialNumberApplying(this.isSerialNumberApplying());
        registry.setFreeOfPrice(this.isFreeOfPrice());
        registry.setFreeOfDiscount(this.isFreeOfDiscount());
        registry.setFreeOfCommission(this.isFreeOfCommission());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkItemGenusId(this.getFkItemGenusId());
        registry.setFkBrandId(this.getFkBrandId());
        registry.setFkManufacturerId(this.getFkManufacturerId());
        registry.setFkComponentId(this.getFkComponentId());
        registry.setFkDepartmentId(this.getFkDepartmentId());
        registry.setFkUnitId(this.getFkUnitId());
        registry.setFkTaxGroupId_n(this.getFkTaxGroupId_n());
        registry.setFkAbpItemId_n(this.getFkAbpItemId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
