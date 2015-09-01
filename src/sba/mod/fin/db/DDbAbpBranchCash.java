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
public class DDbAbpBranchCash extends DDbRegistryUser {

    protected int mnPkAbpCashId;
    protected String msName;
    //protected boolean mbDeleted;
    protected int mnFkAccountCashId;
    protected int mnFkAccountInOwnersEquityId;
    protected int mnFkAccountInExchangeRateId;
    protected int mnFkAccountInAdjustmentId;
    protected int mnFkAccountOutOwnersEquityId;
    protected int mnFkAccountOutExchangeRateId;
    protected int mnFkAccountOutAdjustmentId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public DDbAbpBranchCash() {
        super(DModConsts.F_ABP_CSH);
        initRegistry();
    }

    public void setPkAbpCashId(int n) { mnPkAbpCashId = n; }
    public void setName(String s) { msName = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkAccountCashId(int n) { mnFkAccountCashId = n; }
    public void setFkAccountInOwnersEquityId(int n) { mnFkAccountInOwnersEquityId = n; }
    public void setFkAccountInExchangeRateId(int n) { mnFkAccountInExchangeRateId = n; }
    public void setFkAccountInAdjustmentId(int n) { mnFkAccountInAdjustmentId = n; }
    public void setFkAccountOutOwnersEquityId(int n) { mnFkAccountOutOwnersEquityId = n; }
    public void setFkAccountOutExchangeRateId(int n) { mnFkAccountOutExchangeRateId = n; }
    public void setFkAccountOutAdjustmentId(int n) { mnFkAccountOutAdjustmentId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkAbpCashId() { return mnPkAbpCashId; }
    public String getName() { return msName; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkAccountCashId() { return mnFkAccountCashId; }
    public int getFkAccountInOwnersEquityId() { return mnFkAccountInOwnersEquityId; }
    public int getFkAccountInExchangeRateId() { return mnFkAccountInExchangeRateId; }
    public int getFkAccountInAdjustmentId() { return mnFkAccountInAdjustmentId; }
    public int getFkAccountOutOwnersEquityId() { return mnFkAccountOutOwnersEquityId; }
    public int getFkAccountOutExchangeRateId() { return mnFkAccountOutExchangeRateId; }
    public int getFkAccountOutAdjustmentId() { return mnFkAccountOutAdjustmentId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkAbpCashId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkAbpCashId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkAbpCashId = 0;
        msName = "";
        mbDeleted = false;
        mnFkAccountCashId = 0;
        mnFkAccountInOwnersEquityId = 0;
        mnFkAccountInExchangeRateId = 0;
        mnFkAccountInAdjustmentId = 0;
        mnFkAccountOutOwnersEquityId = 0;
        mnFkAccountOutExchangeRateId = 0;
        mnFkAccountOutAdjustmentId = 0;
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
        return "WHERE id_abp_csh = " + mnPkAbpCashId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_abp_csh = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkAbpCashId = 0;

        msSql = "SELECT COALESCE(MAX(id_abp_csh), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkAbpCashId = resultSet.getInt(1);
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
            mnPkAbpCashId = resultSet.getInt("id_abp_csh");
            msName = resultSet.getString("name");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkAccountCashId = resultSet.getInt("fk_acc_csh");
            mnFkAccountInOwnersEquityId = resultSet.getInt("fk_acc_in_eqy");
            mnFkAccountInExchangeRateId = resultSet.getInt("fk_acc_in_exr");
            mnFkAccountInAdjustmentId = resultSet.getInt("fk_acc_in_adj");
            mnFkAccountOutOwnersEquityId = resultSet.getInt("fk_acc_out_eqy");
            mnFkAccountOutExchangeRateId = resultSet.getInt("fk_acc_out_exr");
            mnFkAccountOutAdjustmentId = resultSet.getInt("fk_acc_out_adj");
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
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkAbpCashId + ", " +
                    "'" + msName + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkAccountCashId + ", " +
                    mnFkAccountInOwnersEquityId + ", " +
                    mnFkAccountInExchangeRateId + ", " +
                    mnFkAccountInAdjustmentId + ", " +
                    mnFkAccountOutOwnersEquityId + ", " +
                    mnFkAccountOutExchangeRateId + ", " +
                    mnFkAccountOutAdjustmentId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_abp_csh = " + mnPkAbpCashId + ", " +
                    "name = '" + msName + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_acc_csh = " + mnFkAccountCashId + ", " +
                    "fk_acc_in_eqy = " + mnFkAccountInOwnersEquityId + ", " +
                    "fk_acc_in_exr = " + mnFkAccountInExchangeRateId + ", " +
                    "fk_acc_in_adj = " + mnFkAccountInAdjustmentId + ", " +
                    "fk_acc_out_eqy = " + mnFkAccountOutOwnersEquityId + ", " +
                    "fk_acc_out_exr = " + mnFkAccountOutExchangeRateId + ", " +
                    "fk_acc_out_adj = " + mnFkAccountOutAdjustmentId + ", " +
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
    public DDbAbpBranchCash clone() throws CloneNotSupportedException {
        DDbAbpBranchCash registry = new DDbAbpBranchCash();

        registry.setPkAbpCashId(this.getPkAbpCashId());
        registry.setName(this.getName());
        registry.setDeleted(this.isDeleted());
        registry.setFkAccountCashId(this.getFkAccountCashId());
        registry.setFkAccountInOwnersEquityId(this.getFkAccountInOwnersEquityId());
        registry.setFkAccountInExchangeRateId(this.getFkAccountInExchangeRateId());
        registry.setFkAccountInAdjustmentId(this.getFkAccountInAdjustmentId());
        registry.setFkAccountOutOwnersEquityId(this.getFkAccountOutOwnersEquityId());
        registry.setFkAccountOutExchangeRateId(this.getFkAccountOutExchangeRateId());
        registry.setFkAccountOutAdjustmentId(this.getFkAccountOutAdjustmentId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
