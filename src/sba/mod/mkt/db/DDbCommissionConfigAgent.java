/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.mkt.db;

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
public class DDbCommissionConfigAgent extends DDbRegistryUser implements DDbCommissionConfig {

    protected int mnPkLinkAgentTypeId;
    protected int mnPkReferenceAgentId;
    protected int mnPkLinkItemTypeId;
    protected int mnPkReferenceItemId;
    protected double mdCommissionPercentage;
    protected double mdCommissionUnitary;
    /*
    protected boolean mbDeleted;
    */
    protected int mnFkCommissionTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public DDbCommissionConfigAgent() {
        super(DModConsts.M_CMM_CFG_AGT);
        initRegistry();
    }

    public void setPkLinkAgentTypeId(int n) { mnPkLinkAgentTypeId = n; }
    public void setPkReferenceAgentId(int n) { mnPkReferenceAgentId = n; }
    public void setPkLinkItemTypeId(int n) { mnPkLinkItemTypeId = n; }
    public void setPkReferenceItemId(int n) { mnPkReferenceItemId = n; }
    public void setCommissionPercentage(double d) { mdCommissionPercentage = d; }
    public void setCommissionUnitary(double d) { mdCommissionUnitary = d; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkCommissionTypeId(int n) { mnFkCommissionTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkLinkAgentTypeId() { return mnPkLinkAgentTypeId; }
    public int getPkReferenceAgentId() { return mnPkReferenceAgentId; }
    public int getPkLinkItemTypeId() { return mnPkLinkItemTypeId; }
    public int getPkReferenceItemId() { return mnPkReferenceItemId; }
    public double getCommissionPercentage() { return mdCommissionPercentage; }
    public double getCommissionUnitary() { return mdCommissionUnitary; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkCommissionTypeId() { return mnFkCommissionTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkLinkAgentTypeId = pk[0];
        mnPkReferenceAgentId = pk[1];
        mnPkLinkItemTypeId = pk[2];
        mnPkReferenceItemId = pk[3];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkLinkAgentTypeId, mnPkReferenceAgentId, mnPkLinkItemTypeId, mnPkReferenceItemId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkLinkAgentTypeId = 0;
        mnPkReferenceAgentId = 0;
        mnPkLinkItemTypeId = 0;
        mnPkReferenceItemId = 0;
        mdCommissionPercentage = 0;
        mdCommissionUnitary = 0;
        mbDeleted = false;
        mnFkCommissionTypeId = 0;
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
        return "WHERE id_lnk_agt_tp = " + mnPkLinkAgentTypeId + " AND " +
                "id_ref_agt = " + mnPkReferenceAgentId + " AND " +
                "id_lnk_itm_tp = " + mnPkLinkItemTypeId + " AND " +
                "id_ref_itm = " + mnPkReferenceItemId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_lnk_agt_tp = " + pk[0] + " AND " +
                "id_ref_agt = " + pk[1] + " AND " +
                "id_lnk_itm_tp = " + pk[2] + " AND " +
                "id_ref_itm = " + pk[3] + " ";
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
            mnPkLinkAgentTypeId = resultSet.getInt("id_lnk_agt_tp");
            mnPkReferenceAgentId = resultSet.getInt("id_ref_agt");
            mnPkLinkItemTypeId = resultSet.getInt("id_lnk_itm_tp");
            mnPkReferenceItemId = resultSet.getInt("id_ref_itm");
            mdCommissionPercentage = resultSet.getDouble("per");
            mdCommissionUnitary = resultSet.getDouble("val_unt");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkCommissionTypeId = resultSet.getInt("fk_cmm_tp");
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
                    mnPkLinkAgentTypeId + ", " +
                    mnPkReferenceAgentId + ", " +
                    mnPkLinkItemTypeId + ", " +
                    mnPkReferenceItemId + ", " +
                    mdCommissionPercentage + ", " +
                    mdCommissionUnitary + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkCommissionTypeId + ", " +
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
                    "id_lnk_agt_tp = " + mnPkLinkAgentTypeId + ", " +
                    "id_ref_agt = " + mnPkReferenceAgentId + ", " +
                    "id_lnk_itm_tp = " + mnPkLinkItemTypeId + ", " +
                    "id_ref_itm = " + mnPkReferenceItemId + ", " +
                     */
                    "per = " + mdCommissionPercentage + ", " +
                    "val_unt = " + mdCommissionUnitary + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_cmm_tp = " + mnFkCommissionTypeId + ", " +
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
    public DDbCommissionConfigAgent clone() throws CloneNotSupportedException {
        DDbCommissionConfigAgent registry = new DDbCommissionConfigAgent();

        registry.setPkLinkAgentTypeId(this.getPkLinkAgentTypeId());
        registry.setPkReferenceAgentId(this.getPkReferenceAgentId());
        registry.setPkLinkItemTypeId(this.getPkLinkItemTypeId());
        registry.setPkReferenceItemId(this.getPkReferenceItemId());
        registry.setCommissionPercentage(this.getCommissionPercentage());
        registry.setCommissionUnitary(this.getCommissionUnitary());
        registry.setDeleted(this.isDeleted());
        registry.setFkCommissionTypeId(this.getFkCommissionTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public int getPkLinkOwnerTypeId() {
        return getPkLinkAgentTypeId();
    }

    @Override
    public int getPkReferenceOwnerId() {
        return getPkReferenceAgentId();
    }
}
