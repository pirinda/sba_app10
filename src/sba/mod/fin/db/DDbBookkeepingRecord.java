/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.fin.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class DDbBookkeepingRecord extends DDbRegistryUser {

    public static final int FIELD_AUDITED = 1;

    protected int mnPkYearId;
    protected int mnPkRecordId;
    protected int mnIxYear;
    protected int mnIxPeriod;
    protected int mnIxRecordType;
    protected int mnIxNumber;
    protected int mnSortingPos;
    protected Date mtDate;
    protected String msText;
    protected boolean mbAudited;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkOwnerBizPartnerId;
    protected int mnFkOwnerBranchId;
    protected int mnFkCashBizPartnerId_n;
    protected int mnFkCashBranchId_n;
    protected int mnFkCashCashId_n;
    protected int mnFkBookkeepingYearId_n;
    protected int mnFkBookkeepingNumberId_n;
    protected int mnFkUserAuditedId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUserAudited;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public DDbBookkeepingRecord() {
        super(DModConsts.F_BKK_REC);
        initRegistry();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkRecordId(int n) { mnPkRecordId = n; }
    public void setIxYear(int n) { mnIxYear = n; }
    public void setIxPeriod(int n) { mnIxPeriod = n; }
    public void setIxRecordType(int n) { mnIxRecordType = n; }
    public void setIxNumber(int n) { mnIxNumber = n; }
    public void setSortingPos(int n) { mnSortingPos = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setText(String s) { msText = s; }
    public void setAudited(boolean b) { mbAudited = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkOwnerBizPartnerId(int n) { mnFkOwnerBizPartnerId = n; }
    public void setFkOwnerBranchId(int n) { mnFkOwnerBranchId = n; }
    public void setFkCashBizPartnerId_n(int n) { mnFkCashBizPartnerId_n = n; }
    public void setFkCashBranchId_n(int n) { mnFkCashBranchId_n = n; }
    public void setFkCashCashId_n(int n) { mnFkCashCashId_n = n; }
    public void setFkBookkeepingYearId_n(int n) { mnFkBookkeepingYearId_n = n; }
    public void setFkBookkeepingNumberId_n(int n) { mnFkBookkeepingNumberId_n = n; }
    public void setFkUserAuditedId(int n) { mnFkUserAuditedId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserAudited(Date t) { mtTsUserAudited = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkRecordId() { return mnPkRecordId; }
    public int getIxYear() { return mnIxYear; }
    public int getIxPeriod() { return mnIxPeriod; }
    public int getIxRecordType() { return mnIxRecordType; }
    public int getIxNumber() { return mnIxNumber; }
    public int getSortingPos() { return mnSortingPos; }
    public Date getDate() { return mtDate; }
    public String getText() { return msText; }
    public boolean isAudited() { return mbAudited; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkOwnerBizPartnerId() { return mnFkOwnerBizPartnerId; }
    public int getFkOwnerBranchId() { return mnFkOwnerBranchId; }
    public int getFkCashBizPartnerId_n() { return mnFkCashBizPartnerId_n; }
    public int getFkCashBranchId_n() { return mnFkCashBranchId_n; }
    public int getFkCashCashId_n() { return mnFkCashCashId_n; }
    public int getFkBookkeepingYearId_n() { return mnFkBookkeepingYearId_n; }
    public int getFkBookkeepingNumberId_n() { return mnFkBookkeepingNumberId_n; }
    public int getFkUserAuditedId() { return mnFkUserAuditedId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserAudited() { return mtTsUserAudited; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkYearId = pk[0];
        mnPkRecordId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkRecordId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkYearId = 0;
        mnPkRecordId = 0;
        mnIxYear = 0;
        mnIxPeriod = 0;
        mnIxRecordType = 0;
        mnIxNumber = 0;
        mnSortingPos = 0;
        mtDate = null;
        msText = "";
        mbAudited = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkOwnerBizPartnerId = 0;
        mnFkOwnerBranchId = 0;
        mnFkCashBizPartnerId_n = 0;
        mnFkCashBranchId_n = 0;
        mnFkCashCashId_n = 0;
        mnFkBookkeepingYearId_n = 0;
        mnFkBookkeepingNumberId_n = 0;
        mnFkUserAuditedId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserAudited = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_yer = " + mnPkYearId + " AND " +
                "id_rec = " + mnPkRecordId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_yer = " + pk[0] + " AND " +
                "id_rec = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkRecordId = 0;

        msSql = "SELECT COALESCE(MAX(id_rec), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_yer = " + mnPkYearId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkRecordId = resultSet.getInt(1);
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
            mnPkYearId = resultSet.getInt("id_yer");
            mnPkRecordId = resultSet.getInt("id_rec");
            mnIxYear = resultSet.getInt("i_yer");
            mnIxPeriod = resultSet.getInt("i_per");
            mnIxRecordType = resultSet.getInt("i_rec_tp");
            mnIxNumber = resultSet.getInt("i_num");
            mnSortingPos = resultSet.getInt("sort");
            mtDate = resultSet.getDate("dt");
            msText = resultSet.getString("txt");
            mbAudited = resultSet.getBoolean("b_aud");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkOwnerBizPartnerId = resultSet.getInt("fk_own_bpr");
            mnFkOwnerBranchId = resultSet.getInt("fk_own_bra");
            mnFkCashBizPartnerId_n = resultSet.getInt("fk_csh_bpr_n");
            mnFkCashBranchId_n = resultSet.getInt("fk_csh_bra_n");
            mnFkCashCashId_n = resultSet.getInt("fk_csh_csh_n");
            mnFkBookkeepingYearId_n = resultSet.getInt("fk_bkk_yer_n");
            mnFkBookkeepingNumberId_n = resultSet.getInt("fk_bkk_num_n");
            mnFkUserAuditedId = resultSet.getInt("fk_usr_aud");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserAudited = resultSet.getTimestamp("ts_usr_aud");
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
                    mnPkYearId + ", " +
                    mnPkRecordId + ", " +
                    mnIxYear + ", " +
                    mnIxPeriod + ", " +
                    mnIxRecordType + ", " +
                    mnIxNumber + ", " +
                    mnSortingPos + ", " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "'" + msText + "', " +
                    (mbAudited ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkOwnerBizPartnerId + ", " +
                    mnFkOwnerBranchId + ", " +
                    (mnFkCashBizPartnerId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashBizPartnerId_n) + ", " +
                    (mnFkCashBranchId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashBranchId_n)  + ", " +
                    (mnFkCashCashId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashCashId_n)  + ", " +
                    (mnFkBookkeepingYearId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBookkeepingYearId_n) + ", " +
                    (mnFkBookkeepingNumberId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBookkeepingNumberId_n) + ", " +
                    mnFkUserAuditedId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_yer = " + mnPkYearId + ", " +
                    "id_rec = " + mnPkRecordId + ", " +
                    */
                    "i_yer = " + mnIxYear + ", " +
                    "i_per = " + mnIxPeriod + ", " +
                    "i_rec_tp = " + mnIxRecordType + ", " +
                    "i_num = " + mnIxNumber + ", " +
                    "sort = " + mnSortingPos + ", " +
                    "dt = '" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "txt = '" + msText + "', " +
                    //"b_aud = " + (mbAudited ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_own_bpr = " + mnFkOwnerBizPartnerId + ", " +
                    "fk_own_bra = " + mnFkOwnerBranchId + ", " +
                    "fk_csh_bpr_n = " + (mnFkCashBizPartnerId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashBizPartnerId_n) + ", " +
                    "fk_csh_bra_n = " + (mnFkCashBranchId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashBranchId_n) + ", " +
                    "fk_csh_csh_n = " + (mnFkCashCashId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashCashId_n) + ", " +
                    "fk_bkk_yer_n = " + (mnFkBookkeepingYearId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBookkeepingYearId_n) + ", " +
                    "fk_bkk_num_n = " + (mnFkBookkeepingNumberId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBookkeepingNumberId_n) + ", " +
                    /*
                    "fk_usr_aud = " + mnFkUserAuditedId + ", " +
                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                     */
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    /*
                    "ts_usr_aud = " + "NOW()" + ", " +
                    "ts_usr_ins = " + "NOW()" + ", " +
                     */
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbBookkeepingRecord clone() throws CloneNotSupportedException {
        DDbBookkeepingRecord registry = new DDbBookkeepingRecord();

        registry.setPkYearId(this.getPkYearId());
        registry.setPkRecordId(this.getPkRecordId());
        registry.setIxYear(this.getIxYear());
        registry.setIxPeriod(this.getIxPeriod());
        registry.setIxRecordType(this.getIxRecordType());
        registry.setIxNumber(this.getIxNumber());
        registry.setSortingPos(this.getSortingPos());
        registry.setDate(this.getDate());
        registry.setText(this.getText());
        registry.setAudited(this.isAudited());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkOwnerBizPartnerId(this.getFkOwnerBizPartnerId());
        registry.setFkOwnerBranchId(this.getFkOwnerBranchId());
        registry.setFkCashBizPartnerId_n(this.getFkCashBizPartnerId_n());
        registry.setFkCashBranchId_n(this.getFkCashBranchId_n());
        registry.setFkCashCashId_n(this.getFkCashCashId_n());
        registry.setFkBookkeepingYearId_n(this.getFkBookkeepingYearId_n());
        registry.setFkBookkeepingNumberId_n(this.getFkBookkeepingNumberId_n());
        registry.setFkUserAuditedId(this.getFkUserAuditedId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserAudited(this.getTsUserAudited());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public void saveField(final Statement statement, final int[] pk, final int field, final Object value) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        msSql = "UPDATE " + getSqlTable() + " SET ";

        switch (field) {
            case FIELD_AUDITED:
                msSql += "b_aud = NOT b_aud, fk_usr_aud = " + (Integer) value + ", ts_usr_aud = NOW() ";
                break;
            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        statement.execute(msSql);
        mnQueryResultId = DDbConsts.SAVE_OK;
    }
}
