/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.cfg.db;

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
public class DDbLock extends DDbRegistryUser {

    protected int mnPkLockId;
    protected int mnRegistryTypeId;
    protected int mnRegistryId;
    protected int mnLockTimeout;
    protected Date mtLockTimestamp;
    protected int mnLockStatus;
    protected boolean mbDeleted;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    
    protected boolean mbAuxUpdateLockTimestamp;
            
    public DDbLock() {
        super(DModConsts.C_LOCK);
        initRegistry();
    }

    public void setPkLockId(int n) { mnPkLockId = n; }
    public void setRegistryTypeId(int n) { mnRegistryTypeId = n; }
    public void setRegistryId(int n) { mnRegistryId = n; }
    public void setLockTimeout(int n) { mnLockTimeout = n; }
    public void setLockTimestamp(Date t) { mtLockTimestamp = t; }
    public void setLockStatus(int n) { mnLockStatus = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkLockId() { return mnPkLockId; }
    public int getRegistryTypeId() { return mnRegistryTypeId; }
    public int getRegistryId() { return mnRegistryId; }
    public int getLockTimeout() { return mnLockTimeout; }
    public Date getLockTimestamp() { return mtLockTimestamp; }
    public int getLockStatus() { return mnLockStatus; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setAuxUpdateLockTimestamp(boolean b) { mbAuxUpdateLockTimestamp = b; }

    public boolean isAuxUpdateLockTimestamp() { return mbAuxUpdateLockTimestamp; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkLockId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkLockId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkLockId = 0;
        mnRegistryTypeId = 0;
        mnRegistryId = 0;
        mnLockTimeout = 0;
        mtLockTimestamp = null;
        mnLockStatus = 0;
        mbDeleted = false;
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
        return "WHERE id_lock = " + mnPkLockId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_lock = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkLockId = 0;

        msSql = "SELECT COALESCE(MAX(id_lock), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkLockId = resultSet.getInt(1);
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
            mnPkLockId = resultSet.getInt("id_lock");
            mnRegistryTypeId = resultSet.getInt("reg_tp_id");
            mnRegistryId = resultSet.getInt("reg_id");
            mnLockTimeout = resultSet.getInt("lock_tout");
            mtLockTimestamp = resultSet.getTimestamp("lock_ts");
            mnLockStatus = resultSet.getInt("lock_st");
            mbDeleted = resultSet.getBoolean("b_del");
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
            //mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkLockId + ", " + 
                    mnRegistryTypeId + ", " + 
                    mnRegistryId + ", " + 
                    mnLockTimeout + ", " + 
                    "NOW()" + ", " + 
                    mnLockStatus + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_lock = " + mnPkLockId + ", " +
                    "reg_tp_id = " + mnRegistryTypeId + ", " +
                    "reg_id = " + mnRegistryId + ", " +
                    "lock_tout = " + mnLockTimeout + ", " +
                    (!mbAuxUpdateLockTimestamp ? "" : "lock_ts = NOW(), ") +
                    "lock_st = " + mnLockStatus + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
            
            mbAuxUpdateLockTimestamp = false; //disable automatic further lock-timestamp updates
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;

        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbLock clone() throws CloneNotSupportedException {
        DDbLock registry = new DDbLock();

        registry.setPkLockId(this.getPkLockId());
        registry.setRegistryTypeId(this.getRegistryTypeId());
        registry.setRegistryId(this.getRegistryId());
        registry.setLockTimeout(this.getLockTimeout());
        registry.setLockTimestamp(this.getLockTimestamp());
        registry.setLockStatus(this.getLockStatus());
        registry.setDeleted(this.isDeleted());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setAuxUpdateLockTimestamp(this.isAuxUpdateLockTimestamp());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
