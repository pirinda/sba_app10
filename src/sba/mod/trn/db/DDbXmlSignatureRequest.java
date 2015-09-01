/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

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
public class DDbXmlSignatureRequest extends DDbRegistryUser {

    protected int mnPkDpsId;
    protected int mnPkRequestId;
    protected int mnRequestType;
    protected int mnRequestStatus;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkXmlSignatureProviderId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public DDbXmlSignatureRequest() {
        super(DModConsts.T_XSR);
        initRegistry();
    }

    /*
     * Private methods
     */

    /*
     * Public methods
     */

    public void setPkDpsId(int n) { mnPkDpsId = n; }
    public void setPkRequestId(int n) { mnPkRequestId = n; }
    public void setRequestType(int n) { mnRequestType = n; }
    public void setRequestStatus(int n) { mnRequestStatus = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkXmlSignatureProviderId(int n) { mnFkXmlSignatureProviderId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkDpsId() { return mnPkDpsId; }
    public int getPkRequestId() { return mnPkRequestId; }
    public int getRequestType() { return mnRequestType; }
    public int getRequestStatus() { return mnRequestStatus; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkXmlSignatureProviderId() { return mnFkXmlSignatureProviderId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDpsId = pk[0];
        mnPkRequestId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDpsId, mnPkRequestId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkDpsId = 0;
        mnPkRequestId = 0;
        mnRequestType = 0;
        mnRequestStatus = 0;
        mbDeleted = false;
        mbSystem = false;
        mnFkXmlSignatureProviderId = 0;
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
        return "WHERE id_dps = " + mnPkDpsId + " AND " +
                "id_req = " + mnPkRequestId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_dps = " + pk[0] + " AND " +
                "id_req = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkRequestId = 0;

        msSql = "SELECT COALESCE(MAX(id_req), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_dps = " + mnPkDpsId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkRequestId = resultSet.getInt(1);
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
            mnPkDpsId = resultSet.getInt("id_dps");
            mnPkRequestId = resultSet.getInt("id_req");
            mnRequestType = resultSet.getInt("req_tp");
            mnRequestStatus = resultSet.getInt("req_st");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkXmlSignatureProviderId = resultSet.getInt("fk_xsp");
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
                    mnPkDpsId + ", " + 
                    mnPkRequestId + ", " + 
                    mnRequestType + ", " + 
                    mnRequestStatus + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkXmlSignatureProviderId + ", " + 
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
                    "id_dps = " + mnPkDpsId + ", " +
                    "id_req = " + mnPkRequestId + ", " +
                    */
                    "req_tp = " + mnRequestType + ", " +
                    "req_st = " + mnRequestStatus + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_xsp = " + mnFkXmlSignatureProviderId + ", " +
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
    public DDbXmlSignatureRequest clone() throws CloneNotSupportedException {
        DDbXmlSignatureRequest registry = new DDbXmlSignatureRequest();

        registry.setPkDpsId(this.getPkDpsId());
        registry.setPkRequestId(this.getPkRequestId());
        registry.setRequestType(this.getRequestType());
        registry.setRequestStatus(this.getRequestStatus());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkXmlSignatureProviderId(this.getFkXmlSignatureProviderId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
