/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.bpr.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sba.gui.util.DUtilConsts;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbBranchBankAccount extends DDbRegistryUser {

    protected int mnPkBizPartnerId;
    protected int mnPkBranchId;
    protected int mnPkBankAccountId;
    protected String msName;
    protected String msBankNumber;
    protected String msNumber;
    protected String msNumberStandard;
    protected String msReference;
    protected String msAbba;
    protected String msSwift;
    protected boolean mbDefault;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkBankAccountTypeId;
    protected int mnFkBizPartnerBankId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public DDbBranchBankAccount() {
        super(DModConsts.BU_BNK);
        initRegistry();
    }

    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setPkBranchId(int n) { mnPkBranchId = n; }
    public void setPkBankAccountId(int n) { mnPkBankAccountId = n; }
    public void setName(String s) { msName = s; }
    public void setBankNumber(String s) { msBankNumber = s; }
    public void setNumber(String s) { msNumber = s; }
    public void setNumberStandard(String s) { msNumberStandard = s; }
    public void setReference(String s) { msReference = s; }
    public void setAbba(String s) { msAbba = s; }
    public void setSwift(String s) { msSwift = s; }
    public void setDefault(boolean b) { mbDefault = b; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkBankAccountTypeId(int n) { mnFkBankAccountTypeId = n; }
    public void setFkBizPartnerBankId(int n) { mnFkBizPartnerBankId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public int getPkBranchId() { return mnPkBranchId; }
    public int getPkBankAccountId() { return mnPkBankAccountId; }
    public String getName() { return msName; }
    public String getBankNumber() { return msBankNumber; }
    public String getNumber() { return msNumber; }
    public String getNumberStandard() { return msNumberStandard; }
    public String getReference() { return msReference; }
    public String getAbba() { return msAbba; }
    public String getSwift() { return msSwift; }
    public boolean isDefault() { return mbDefault; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkBankAccountTypeId() { return mnFkBankAccountTypeId; }
    public int getFkBizPartnerBankId() { return mnFkBizPartnerBankId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBizPartnerId = pk[0];
        mnPkBranchId = pk[1];
        mnPkBankAccountId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBizPartnerId, mnPkBranchId, mnPkBankAccountId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBizPartnerId = 0;
        mnPkBranchId = 0;
        mnPkBankAccountId = 0;
        msName = "";
        msBankNumber = "";
        msNumber = "";
        msNumberStandard = "";
        msReference = "";
        msAbba = "";
        msSwift = "";
        mbDefault = false;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkBankAccountTypeId = 0;
        mnFkBizPartnerBankId = 0;
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
        return "WHERE id_bpr = " + mnPkBizPartnerId + " AND " +
                "id_bra = " + mnPkBranchId + " AND " +
                "id_bnk = " + mnPkBankAccountId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bpr = " + pk[0] + " AND " +
                "id_bra = " + pk[1] + " AND " +
                "id_bnk = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkBankAccountId = 0;

        msSql = "SELECT COALESCE(MAX(id_bnk), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_bpr = " + mnPkBizPartnerId + " AND " +
                "id_bra = " + mnPkBranchId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkBankAccountId = resultSet.getInt(1);
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
            mnPkBizPartnerId = resultSet.getInt("id_bpr");
            mnPkBranchId = resultSet.getInt("id_bra");
            mnPkBankAccountId = resultSet.getInt("id_bnk");
            msName = resultSet.getString("name");
            msBankNumber = resultSet.getString("bnk_num");
            msNumber = resultSet.getString("num");
            msNumberStandard = resultSet.getString("num_std");
            msReference = resultSet.getString("ref");
            msAbba = resultSet.getString("aba");
            msSwift = resultSet.getString("swt");
            mbDefault = resultSet.getBoolean("b_def");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkBankAccountTypeId = resultSet.getInt("fk_bnk_tp");
            mnFkBizPartnerBankId = resultSet.getInt("fk_bnk");
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
                    mnPkBizPartnerId + ", " +
                    mnPkBranchId + ", " +
                    mnPkBankAccountId + ", " +
                    "'" + msName + "', " +
                    "'" + msBankNumber + "', " +
                    "'" + msNumber + "', " +
                    "'" + msNumberStandard + "', " +
                    "'" + msReference + "', " +
                    "'" + msAbba + "', " +
                    "'" + msSwift + "', " +
                    (mbDefault ? 1 : 0) + ", " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkBankAccountTypeId + ", " +
                    mnFkBizPartnerBankId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_bpr = " + mnPkBizPartnerId + ", " +
                    "id_bra = " + mnPkBranchId + ", " +
                    "id_bnk = " + mnPkBankAccountId + ", " +
                    */
                    "name = '" + msName + "', " +
                    "bnk_num = '" + msBankNumber + "', " +
                    "num = '" + msNumber + "', " +
                    "num_std = '" + msNumberStandard + "', " +
                    "ref = '" + msReference + "', " +
                    "aba = '" + msAbba + "', " +
                    "swt = '" + msSwift + "', " +
                    "b_def = " + (mbDefault ? 1 : 0) + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_bnk_tp = " + mnFkBankAccountTypeId + ", " +
                    "fk_bnk = " + mnFkBizPartnerBankId + ", " +
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
    public DDbBranchBankAccount clone() throws CloneNotSupportedException {
        DDbBranchBankAccount registry = new DDbBranchBankAccount();

        registry.setPkBizPartnerId(this.getPkBizPartnerId());
        registry.setPkBranchId(this.getPkBranchId());
        registry.setPkBankAccountId(this.getPkBankAccountId());
        registry.setName(this.getName());
        registry.setBankNumber(this.getBankNumber());
        registry.setNumber(this.getNumber());
        registry.setNumberStandard(this.getNumberStandard());
        registry.setReference(this.getReference());
        registry.setAbba(this.getAbba());
        registry.setSwift(this.getSwift());
        registry.setDefault(this.isDefault());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkBankAccountTypeId(this.getFkBankAccountTypeId());
        registry.setFkBizPartnerBankId(this.getFkBizPartnerBankId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
