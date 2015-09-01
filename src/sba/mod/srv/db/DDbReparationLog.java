/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.srv.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbReparationLog extends DDbRegistryUser {

    protected int mnPkReparationId;
    protected int mnPkLogId;
    protected Date mtDate;
    //protected boolean mbDeleted;
    protected int mnFkServiceTypeId;
    protected int mnFkServiceStatusId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public DDbReparationLog() {
        super(DModConsts.S_REP_LOG);
        initRegistry();
    }

    public void setPkReparationId(int n) { mnPkReparationId = n; }
    public void setPkLogId(int n) { mnPkLogId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkServiceTypeId(int n) { mnFkServiceTypeId = n; }
    public void setFkServiceStatusId(int n) { mnFkServiceStatusId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkReparationId() { return mnPkReparationId; }
    public int getPkLogId() { return mnPkLogId; }
    public Date getDate() { return mtDate; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkServiceTypeId() { return mnFkServiceTypeId; }
    public int getFkServiceStatusId() { return mnFkServiceStatusId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkReparationId = pk[0];
        mnPkLogId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkReparationId, mnPkLogId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkReparationId = 0;
        mnPkLogId = 0;
        mtDate = null;
        mbDeleted = false;
        mnFkServiceTypeId = 0;
        mnFkServiceStatusId = 0;
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
        return "WHERE id_rep = " + mnPkReparationId + " AND " +
                "id_log = " + mnPkLogId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_rep = " + pk[0] + " AND " +
                "id_log = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkLogId = 0;

        msSql = "SELECT COALESCE(MAX(id_log), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_rep = " + mnPkReparationId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkLogId = resultSet.getInt(1);
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
            mnPkReparationId = resultSet.getInt("id_rep");
            mnPkLogId = resultSet.getInt("id_log");
            mtDate = resultSet.getDate("dt");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkServiceTypeId = resultSet.getInt("fk_srv_tp");
            mnFkServiceStatusId = resultSet.getInt("fk_srv_st");
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
                    mnPkReparationId + ", " +
                    mnPkLogId + ", " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkServiceTypeId + ", " +
                    mnFkServiceStatusId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_rep = " + mnPkReparationId + ", " +
                    "id_log = " + mnPkLogId + ", " +
                    "dt = '" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_srv_tp = " + mnFkServiceTypeId + ", " +
                    "fk_srv_st = " + mnFkServiceStatusId + ", " +
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
    public DDbReparationLog clone() throws CloneNotSupportedException {
        DDbReparationLog registry = new DDbReparationLog();

        registry.setPkReparationId(this.getPkReparationId());
        registry.setPkLogId(this.getPkLogId());
        registry.setDate(this.getDate());
        registry.setDeleted(this.isDeleted());
        registry.setFkServiceTypeId(this.getFkServiceTypeId());
        registry.setFkServiceStatusId(this.getFkServiceStatusId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
