/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.mkt.db;

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
public class DDbCustomerConfig extends DDbRegistryUser {

    protected int mnPkCustomerId;
    protected String msName;
    /*
    protected boolean mbDeleted;
    */
    protected int mnFkSegmentId;
    protected int mnFkChannelId;
    protected int mnFkRouteId;
    protected int mnFkAgentId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public DDbCustomerConfig() {
        super(DModConsts.M_CUS_CFG);
        initRegistry();
    }

    public void setPkCustomerId(int n) { mnPkCustomerId = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkSegmentId(int n) { mnFkSegmentId = n; }
    public void setFkChannelId(int n) { mnFkChannelId = n; }
    public void setFkRouteId(int n) { mnFkRouteId = n; }
    public void setFkAgentId_n(int n) { mnFkAgentId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkCustomerId() { return mnPkCustomerId; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkSegmentId() { return mnFkSegmentId; }
    public int getFkChannelId() { return mnFkChannelId; }
    public int getFkRouteId() { return mnFkRouteId; }
    public int getFkAgentId_n() { return mnFkAgentId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkCustomerId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCustomerId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkCustomerId = 0;
        mbDeleted = false;
        mnFkSegmentId = 0;
        mnFkChannelId = 0;
        mnFkRouteId = 0;
        mnFkAgentId_n = 0;
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
        return "WHERE id_cus = " + mnPkCustomerId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_cus = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
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
            mnPkCustomerId = resultSet.getInt("id_cus");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkSegmentId = resultSet.getInt("fk_seg");
            mnFkChannelId = resultSet.getInt("fk_cha");
            mnFkRouteId = resultSet.getInt("fk_rou");
            mnFkAgentId_n = resultSet.getInt("fk_agt_n");
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
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkCustomerId + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkSegmentId + ", " +
                    mnFkChannelId + ", " +
                    mnFkRouteId + ", " +
                    (mnFkAgentId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAgentId_n) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_cus = " + mnPkCustomerId + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_seg = " + mnFkSegmentId + ", " +
                    "fk_cha = " + mnFkChannelId + ", " +
                    "fk_rou = " + mnFkRouteId + ", " +
                    "fk_agt_n = " + (mnFkAgentId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAgentId_n) + ", " +
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
    public DDbCustomerConfig clone() throws CloneNotSupportedException {
        DDbCustomerConfig registry = new DDbCustomerConfig();

        registry.setPkCustomerId(this.getPkCustomerId());
        registry.setDeleted(this.isDeleted());
        registry.setFkSegmentId(this.getFkSegmentId());
        registry.setFkChannelId(this.getFkChannelId());
        registry.setFkRouteId(this.getFkRouteId());
        registry.setFkAgentId_n(this.getFkAgentId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
