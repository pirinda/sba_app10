/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbXmlSignatureMove extends DDbRegistryUser {

    protected int mnPkXmlSignatureProviderId;
    protected int mnPkMoveId;
    protected Date mtDate;
    protected int mnMoveIn;
    protected int mnMoveOut;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkXsmClassId;
    protected int mnFkXsmTypeId;
    protected int mnFkSignatureBizPartnerId_n;
    protected int mnFkSignatureBranchId_n;
    protected int mnFkDfrId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    public DDbXmlSignatureMove() {
        super(DModConsts.T_XSM);
        initRegistry();
    }

    /*
     * Private methods
     */

    /*
     * Public methods
     */

    public void setPkXmlSignatureProviderId(int n) { mnPkXmlSignatureProviderId = n; }
    public void setPkMoveId(int n) { mnPkMoveId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setMoveIn(int n) { mnMoveIn = n; }
    public void setMoveOut(int n) { mnMoveOut = n; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkXsmClassId(int n) { mnFkXsmClassId = n; }
    public void setFkXsmTypeId(int n) { mnFkXsmTypeId = n; }
    public void setFkSignatureBizPartnerId_n(int n) { mnFkSignatureBizPartnerId_n = n; }
    public void setFkSignatureBranchId_n(int n) { mnFkSignatureBranchId_n = n; }
    public void setFkDfrId_n(int n) { mnFkDfrId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkXmlSignatureProviderId() { return mnPkXmlSignatureProviderId; }
    public int getPkMoveId() { return mnPkMoveId; }
    public Date getDate() { return mtDate; }
    public int getMoveIn() { return mnMoveIn; }
    public int getMoveOut() { return mnMoveOut; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkXsmClassId() { return mnFkXsmClassId; }
    public int getFkXsmTypeId() { return mnFkXsmTypeId; }
    public int getFkSignatureBizPartnerId_n() { return mnFkSignatureBizPartnerId_n; }
    public int getFkSignatureBranchId_n() { return mnFkSignatureBranchId_n; }
    public int getFkDfrId_n() { return mnFkDfrId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public int[] getXsmTypeKey() { return new int[] { mnFkXsmClassId, mnFkXsmTypeId }; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkXmlSignatureProviderId = pk[0];
        mnPkMoveId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkXmlSignatureProviderId, mnPkMoveId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkXmlSignatureProviderId = 0;
        mnPkMoveId = 0;
        mtDate = null;
        mnMoveIn = 0;
        mnMoveOut = 0;
        mbDeleted = false;
        mbSystem = false;
        mnFkXsmClassId = 0;
        mnFkXsmTypeId = 0;
        mnFkSignatureBizPartnerId_n = 0;
        mnFkSignatureBranchId_n = 0;
        mnFkDfrId_n = 0;
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
        return "WHERE id_xsp = " + mnPkXmlSignatureProviderId + " AND " +
                "id_mov = " + mnPkMoveId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_xsp = " + pk[0] + " AND " +
                "id_mov = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkMoveId = 0;

        msSql = "SELECT COALESCE(MAX(id_mov), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_xsp = " + mnPkXmlSignatureProviderId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkMoveId = resultSet.getInt(1);
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
            mnPkXmlSignatureProviderId = resultSet.getInt("id_xsp");
            mnPkMoveId = resultSet.getInt("id_mov");
            mtDate = resultSet.getDate("dt");
            mnMoveIn = resultSet.getInt("mov_in");
            mnMoveOut = resultSet.getInt("mov_out");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkXsmClassId = resultSet.getInt("fk_xsm_cl");
            mnFkXsmTypeId = resultSet.getInt("fk_xsm_tp");
            mnFkSignatureBizPartnerId_n = resultSet.getInt("fk_sig_bpr_n");
            mnFkSignatureBranchId_n = resultSet.getInt("fk_sig_bra_n");
            mnFkDfrId_n = resultSet.getInt("fk_dfr_n");
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
                    mnPkXmlSignatureProviderId + ", " +
                    mnPkMoveId + ", " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    mnMoveIn + ", " +
                    mnMoveOut + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkXsmClassId + ", " +
                    mnFkXsmTypeId + ", " +
                    (mnFkSignatureBizPartnerId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkSignatureBizPartnerId_n) + ", " + 
                    (mnFkSignatureBranchId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkSignatureBranchId_n) + ", " + 
                    (mnFkDfrId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDfrId_n) + ", " +
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
                    "id_xsp = " + mnPkXmlSignatureProviderId + ", " +
                    "id_mov = " + mnPkMoveId + ", " +
                    */
                    "dt = '" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "mov_in = " + mnMoveIn + ", " +
                    "mov_out = " + mnMoveOut + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_xsm_cl = " + mnFkXsmClassId + ", " +
                    "fk_xsm_tp = " + mnFkXsmTypeId + ", " +
                    "fk_sig_bpr_n = " + (mnFkSignatureBizPartnerId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkSignatureBizPartnerId_n) + ", " +
                    "fk_sig_bra_n = " + (mnFkSignatureBranchId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkSignatureBranchId_n) + ", " +
                    "fk_dfr_n = " + (mnFkDfrId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDfrId_n) + ", " +
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
    public DDbXmlSignatureMove clone() throws CloneNotSupportedException {
        DDbXmlSignatureMove registry = new DDbXmlSignatureMove();

        registry.setPkXmlSignatureProviderId(this.getPkXmlSignatureProviderId());
        registry.setPkMoveId(this.getPkMoveId());
        registry.setDate(this.getDate());
        registry.setMoveIn(this.getMoveIn());
        registry.setMoveOut(this.getMoveOut());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkXsmClassId(this.getFkXsmClassId());
        registry.setFkXsmTypeId(this.getFkXsmTypeId());
        registry.setFkSignatureBizPartnerId_n(this.getFkSignatureBizPartnerId_n());
        registry.setFkSignatureBranchId_n(this.getFkSignatureBranchId_n());
        registry.setFkDfrId_n(this.getFkDfrId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    @Override
    public boolean canDelete(final DGuiSession session) throws SQLException, Exception {
        boolean can = super.canDelete(session);

        if (can) {
            if (mbDeleted) {
                can = false;    // cannot reactivate registries
            }
        }

        return can;
    }
}
