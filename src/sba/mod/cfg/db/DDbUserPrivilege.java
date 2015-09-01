/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.cfg.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbUserPrivilege extends DDbRegistry {

    protected int mnPkUserId;
    protected int mnPkPrivilegeId;
    protected int mnFkLevelId;

    public DDbUserPrivilege() {
        super(DModConsts.CU_USR_PRV);
        initRegistry();
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setPkPrivilegeId(int n) { mnPkPrivilegeId = n; }
    public void setFkLevelId(int n) { mnFkLevelId = n; }

    public int getPkUserId() { return mnPkUserId; }
    public int getPkPrivilegeId() { return mnPkPrivilegeId; }
    public int getFkLevelId() { return mnFkLevelId; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkUserId = pk[0];
        mnPkPrivilegeId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkUserId, mnPkPrivilegeId };
    }

    @Override
    public void initRegistry() {
        super.initBaseRegistry();

        mnPkUserId = 0;
        mnPkPrivilegeId = 0;
        mnFkLevelId = 0;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_usr = " + mnPkUserId + " AND " +
                "id_prv = " + mnPkPrivilegeId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_usr = " + pk[0] + " AND " +
                "id_prv = " + pk[1] + " ";
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
            mnPkUserId = resultSet.getInt("id_usr");
            mnPkPrivilegeId = resultSet.getInt("id_prv");
            mnFkLevelId = resultSet.getInt("fk_lev");

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
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkUserId + ", " +
                    mnPkPrivilegeId + ", " +
                    mnFkLevelId + " " +
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_usr = " + mnPkUserId + ", " +
                    "id_prv = " + mnPkPrivilegeId + ", " +
                    */
                    "fk_lev = " + mnFkLevelId + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbUserPrivilege clone() throws CloneNotSupportedException {
        DDbUserPrivilege registry = new DDbUserPrivilege();

        registry.setPkUserId(this.getPkUserId());
        registry.setPkPrivilegeId(this.getPkPrivilegeId());
        registry.setFkLevelId(this.getFkLevelId());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
