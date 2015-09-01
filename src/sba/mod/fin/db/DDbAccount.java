/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.fin.db;

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
public class DDbAccount extends DDbRegistryUser {

    protected int mnPkAccountId;
    protected String msCode;
    protected String msName;
    protected int mnDeep;
    protected int mnLevel;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkAccountClassId;
    protected int mnFkAccountCategoryId;
    protected int mnFkAccountTypeId;
    protected int mnFkSystemAccountCategoryId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public DDbAccount() {
        super(DModConsts.FU_ACC);
        initRegistry();
    }

    public void setPkAccountId(int n) { mnPkAccountId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setDeep(int n) { mnDeep = n; }
    public void setLevel(int n) { mnLevel = n; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkAccountClassId(int n) { mnFkAccountClassId = n; }
    public void setFkAccountCategoryId(int n) { mnFkAccountCategoryId = n; }
    public void setFkAccountTypeId(int n) { mnFkAccountTypeId = n; }
    public void setFkSystemAccountCategoryId(int n) { mnFkSystemAccountCategoryId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkAccountId() { return mnPkAccountId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public int getDeep() { return mnDeep; }
    public int getLevel() { return mnLevel; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkAccountClassId() { return mnFkAccountClassId; }
    public int getFkAccountCategoryId() { return mnFkAccountCategoryId; }
    public int getFkAccountTypeId() { return mnFkAccountTypeId; }
    public int getFkSystemAccountCategoryId() { return mnFkSystemAccountCategoryId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkAccountId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkAccountId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkAccountId = 0;
        msCode = "";
        msName = "";
        mnDeep = 0;
        mnLevel = 0;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkAccountClassId = 0;
        mnFkAccountCategoryId = 0;
        mnFkAccountTypeId = 0;
        mnFkSystemAccountCategoryId = 0;
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
        return "WHERE id_acc = " + mnPkAccountId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_acc = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkAccountId = 0;

        msSql = "SELECT COALESCE(MAX(id_acc), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkAccountId = resultSet.getInt(1);
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
            mnPkAccountId = resultSet.getInt("id_acc");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mnDeep = resultSet.getInt("dee");
            mnLevel = resultSet.getInt("lev");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkAccountClassId = resultSet.getInt("fk_acc_ct");
            mnFkAccountCategoryId = resultSet.getInt("fk_acc_cl");
            mnFkAccountTypeId = resultSet.getInt("fk_acc_tp");
            mnFkSystemAccountCategoryId = resultSet.getInt("fk_acc_spe_tp");
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
                    mnPkAccountId + ", " +
                    "'" + (msCode.length() > 0 ? msCode : "" + mnPkAccountId) + "', " +
                    "'" + msName + "', " +
                    mnDeep + ", " +
                    mnLevel + ", " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkAccountClassId + ", " +
                    mnFkAccountCategoryId + ", " +
                    mnFkAccountTypeId + ", " +
                    mnFkSystemAccountCategoryId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_acc = " + mnPkAccountId + ", " +
                    "code = '" + (msCode.length() > 0 ? msCode : "" + mnPkAccountId) + "', " +
                    "name = '" + msName + "', " +
                    "dee = " + mnDeep + ", " +
                    "lev = " + mnLevel + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_acc_ct = " + mnFkAccountClassId + ", " +
                    "fk_acc_cl = " + mnFkAccountCategoryId + ", " +
                    "fk_acc_tp = " + mnFkAccountTypeId + ", " +
                    "fk_acc_spe_tp = " + mnFkSystemAccountCategoryId + ", " +
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
    public DDbAccount clone() throws CloneNotSupportedException {
        DDbAccount registry = new DDbAccount();

        registry.setPkAccountId(this.getPkAccountId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setDeep(this.getDeep());
        registry.setLevel(this.getLevel());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkAccountClassId(this.getFkAccountClassId());
        registry.setFkAccountCategoryId(this.getFkAccountCategoryId());
        registry.setFkAccountTypeId(this.getFkAccountTypeId());
        registry.setFkSystemAccountCategoryId(this.getFkSystemAccountCategoryId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
