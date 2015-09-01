/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.cfg.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sba.gui.util.DUtilBranchEntity;
import sba.gui.util.DUtilConsts;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.bpr.db.DDbBranch;

/**
 *
 * @author Sergio Flores
 */
public class DDbBranchCash extends DDbRegistryUser implements DUtilBranchEntity {

    protected int mnPkBizPartnerId;
    protected int mnPkBranchId;
    protected int mnPkCashId;
    protected String msCode;
    protected String msName;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkCashTypeId;
    protected int mnFkCurrencyId;
    protected int mnFkAbpCashId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected String msDbBranchCode;
    protected String msDbBranchName;

    public DDbBranchCash() {
        super(DModConsts.CU_CSH);
        initRegistry();
    }

    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setPkBranchId(int n) { mnPkBranchId = n; }
    public void setPkCashId(int n) { mnPkCashId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkCashTypeId(int n) { mnFkCashTypeId = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setFkAbpCashId(int n) { mnFkAbpCashId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public int getPkBranchId() { return mnPkBranchId; }
    public int getPkCashId() { return mnPkCashId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkCashTypeId() { return mnFkCashTypeId; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public int getFkAbpCashId() { return mnFkAbpCashId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setDbBranchCode(String s) { msDbBranchCode = s; }
    public void setDbBranchName(String s) { msDbBranchName = s; }

    public String getDbBranchCode() { return msDbBranchCode; }
    public String getDbBranchName() { return msDbBranchName; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBizPartnerId = pk[0];
        mnPkBranchId = pk[1];
        mnPkCashId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBizPartnerId, mnPkBranchId, mnPkCashId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBizPartnerId = 0;
        mnPkBranchId = 0;
        mnPkCashId = 0;
        msCode = "";
        msName = "";
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkCashTypeId = 0;
        mnFkCurrencyId = 0;
        mnFkAbpCashId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        msDbBranchCode = "";
        msDbBranchName = "";
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bpr = " + mnPkBizPartnerId + " AND " +
                "id_bra = " + mnPkBranchId + " AND " +
                "id_csh = " + mnPkCashId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bpr = " + pk[0] + " AND " +
                "id_bra = " + pk[1] + " AND " +
                "id_csh = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkCashId = 0;

        msSql = "SELECT COALESCE(MAX(id_csh), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_bpr = " + mnPkBizPartnerId + " AND " +
                "id_bra = " + mnPkBranchId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkCashId = resultSet.getInt(1);
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
            mnPkCashId = resultSet.getInt("id_csh");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkCashTypeId = resultSet.getInt("fk_csh_tp");
            mnFkCurrencyId = resultSet.getInt("fk_cur");
            mnFkAbpCashId = resultSet.getInt("fk_abp_csh");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read additional fields:

            msDbBranchCode = (String) new DDbBranch().readField(session.getStatement(), new int[] { mnPkBizPartnerId, mnPkBranchId }, DDbRegistry.FIELD_CODE);
            msDbBranchName = (String) new DDbBranch().readField(session.getStatement(), new int[] { mnPkBizPartnerId, mnPkBranchId }, DDbRegistry.FIELD_NAME);

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
                    mnPkCashId + ", " +
                    "'" + msCode + "', " +
                    "'" + msName + "', " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkCashTypeId + ", " +
                    mnFkCurrencyId + ", " +
                    mnFkAbpCashId + ", " +
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
                    "id_csh = " + mnPkCashId + ", " +
                    */
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_csh_tp = " + mnFkCashTypeId + ", " +
                    "fk_cur = " + mnFkCurrencyId + ", " +
                    "fk_abp_csh = " + mnFkAbpCashId + ", " +
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
    public DDbBranchCash clone() throws CloneNotSupportedException {
        DDbBranchCash registry = new DDbBranchCash();

        registry.setPkBizPartnerId(this.getPkBizPartnerId());
        registry.setPkBranchId(this.getPkBranchId());
        registry.setPkCashId(this.getPkCashId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkCashTypeId(this.getFkCashTypeId());
        registry.setFkCurrencyId(this.getFkCurrencyId());
        registry.setFkAbpCashId(this.getFkAbpCashId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setDbBranchCode(this.getDbBranchCode());
        registry.setDbBranchName(this.getDbBranchName());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public int[] getCompanyKey() {
        return new int[] { mnPkBizPartnerId };
    }

    @Override
    public int[] getBranchKey() {
        return new int[] { mnPkBizPartnerId, mnPkBranchId };
    }

    @Override
    public int[] getBranchEntityKey() {
        return new int[] { mnPkBizPartnerId, mnPkBranchId, mnPkCashId };
    }
}
