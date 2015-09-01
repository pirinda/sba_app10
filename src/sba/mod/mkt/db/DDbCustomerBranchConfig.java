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
public class DDbCustomerBranchConfig extends DDbRegistryUser {

    protected int mnPkCustomerId;
    protected int mnPkBranchId;
    protected String msName;
    /*
    protected boolean mbDeleted;
    */
    protected int mnFkRouteId;
    protected int mnFkAgentId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public DDbCustomerBranchConfig() {
        super(DModConsts.M_CUS_BRA_CFG);
        initRegistry();
    }

    public void setPkCustomerId(int n) { mnPkCustomerId = n; }
    public void setPkBranchId(int n) { mnPkBranchId = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkRouteId(int n) { mnFkRouteId = n; }
    public void setFkAgentId_n(int n) { mnFkAgentId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkCustomerId() { return mnPkCustomerId; }
    public int getPkBranchId() { return mnPkBranchId; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkRouteId() { return mnFkRouteId; }
    public int getFkAgentId_n() { return mnFkAgentId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkCustomerId = pk[0];
        mnPkBranchId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCustomerId, mnPkBranchId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkCustomerId = 0;
        mnPkBranchId = 0;
        mbDeleted = false;
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
        return "WHERE id_cus = " + mnPkCustomerId + " AND " +
                "id_bra = " + mnPkBranchId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_cus = " + pk[0] + " AND " +
                "id_bra = " + pk[1] + " ";
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
            mnPkBranchId = resultSet.getInt("id_bra");
            mbDeleted = resultSet.getBoolean("b_del");
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
                    mnPkBranchId + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
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
                    //"id_bra = " + mnPkBranchId + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
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
    public DDbCustomerBranchConfig clone() throws CloneNotSupportedException {
        DDbCustomerBranchConfig registry = new DDbCustomerBranchConfig();

        registry.setPkCustomerId(this.getPkCustomerId());
        registry.setPkBranchId(this.getPkBranchId());
        registry.setDeleted(this.isDeleted());
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
