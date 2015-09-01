/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.mkt.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
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
public class DDbCommissionCalc extends DDbRegistryUser {

    public static final int FIELD_AUDITED = 1;

    protected int mnPkCommissionCalcId;
    protected Date mtDateStart;
    protected Date mtDateEnd;
    protected boolean mbAudited;
    /*
    protected boolean mbDeleted;
    */
    protected int mnFkCommissionCalcTypeId;
    protected int mnFkCommissionValueTypeId;
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

    protected Vector<DDbCommissionCalcNote> mvChildNotes;
    protected Vector<DDbCommissionCalcRow> mvChildRows;

    public DDbCommissionCalc() {
        super(DModConsts.M_CMM_CAL);
        mvChildNotes = new Vector<DDbCommissionCalcNote>();
        mvChildRows = new Vector<DDbCommissionCalcRow>();
        initRegistry();
    }

    public void setPkCommissionCalcId(int n) { mnPkCommissionCalcId = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd(Date t) { mtDateEnd = t; }
    public void setAudited(boolean b) { mbAudited = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkCommissionCalcTypeId(int n) { mnFkCommissionCalcTypeId = n; }
    public void setFkCommissionValueTypeId(int n) { mnFkCommissionValueTypeId = n; }
    public void setFkUserAuditedId(int n) { mnFkUserAuditedId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserAudited(Date t) { mtTsUserAudited = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkCommissionCalcId() { return mnPkCommissionCalcId; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd() { return mtDateEnd; }
    public boolean isAudited() { return mbAudited; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkCommissionCalcTypeId() { return mnFkCommissionCalcTypeId; }
    public int getFkCommissionValueTypeId() { return mnFkCommissionValueTypeId; }
    public int getFkUserAuditedId() { return mnFkUserAuditedId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserAudited() { return mtTsUserAudited; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public Vector<DDbCommissionCalcNote> getChildNotes() { return mvChildNotes; }
    public Vector<DDbCommissionCalcRow> getChildRows() { return mvChildRows; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkCommissionCalcId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCommissionCalcId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkCommissionCalcId = 0;
        mtDateStart = null;
        mtDateEnd = null;
        mbAudited = false;
        mbDeleted = false;
        mnFkCommissionCalcTypeId = 0;
        mnFkCommissionValueTypeId = 0;
        mnFkUserAuditedId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserAudited = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mvChildNotes.clear();
        mvChildRows.clear();
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_cmm = " + mnPkCommissionCalcId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_cmm = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkCommissionCalcId = 0;

        msSql = "SELECT COALESCE(MAX(id_cmm), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkCommissionCalcId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(DGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
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
            mnPkCommissionCalcId = resultSet.getInt("id_cmm");
            mtDateStart = resultSet.getDate("dt_sta");
            mtDateEnd = resultSet.getDate("dt_end");
            mbAudited = resultSet.getBoolean("b_aud");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkCommissionCalcTypeId = resultSet.getInt("fk_cmm_cal_tp");
            mnFkCommissionValueTypeId = resultSet.getInt("fk_cmm_val_tp");
            mnFkUserAuditedId = resultSet.getInt("fk_usr_aud");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserAudited = resultSet.getTimestamp("ts_usr_aud");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            statement = session.getStatement().getConnection().createStatement();

            // Read aswell child registries:

            msSql = "SELECT id_not FROM " + DModConsts.TablesMap.get(DModConsts.M_CMM_CAL_NOT) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbCommissionCalcNote child = new DDbCommissionCalcNote();
                child.read(session, new int[] { mnPkCommissionCalcId, resultSet.getInt(1) });
                mvChildNotes.add(child);
            }

            msSql = "SELECT id_ref, id_row FROM " + DModConsts.TablesMap.get(DModConsts.M_CMM_CAL_ROW) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbCommissionCalcRow child = new DDbCommissionCalcRow();
                child.read(session, new int[] { mnPkCommissionCalcId, resultSet.getInt(1), resultSet.getInt(2) });
                mvChildRows.add(child);
            }

            // Finish registry reading:

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
                    mnPkCommissionCalcId + ", " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " +
                    (mbAudited ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkCommissionCalcTypeId + ", " +
                    mnFkCommissionValueTypeId + ", " +
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
                    //"id_cmm = " + mnPkCommissionCalcId + ", " +
                    "dt_sta = '" + DLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "dt_end = '" + DLibUtils.DbmsDateFormatDate.format(mtDateEnd) + "', " +
                    "b_aud = " + (mbAudited ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_cmm_cal_tp = " + mnFkCommissionCalcTypeId + ", " +
                    "fk_cmm_val_tp = " + mnFkCommissionValueTypeId + ", " +
                    //"fk_usr_aud = " + mnFkUserAuditedId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_aud = " + "NOW()" + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        msSql = "DELETE FROM " + DModConsts.TablesMap.get(DModConsts.M_CMM_CAL_NOT) + " " + getSqlWhere();
        session.getStatement().execute(msSql);

        for (DDbCommissionCalcNote child : mvChildNotes) {
            child.setPkCommissionCalcId(mnPkCommissionCalcId);
            child.setRegistryNew(true);
            child.save(session);
        }

        msSql = "DELETE FROM " + DModConsts.TablesMap.get(DModConsts.M_CMM_CAL_ROW) + " " + getSqlWhere();
        session.getStatement().execute(msSql);

        for (DDbCommissionCalcRow child : mvChildRows) {
            child.setPkCommissionCalcId(mnPkCommissionCalcId);
            child.setRegistryNew(true);
            child.save(session);
        }

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbCommissionCalc clone() throws CloneNotSupportedException {
        DDbCommissionCalc registry = new DDbCommissionCalc();

        registry.setPkCommissionCalcId(this.getPkCommissionCalcId());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd(this.getDateEnd());
        registry.setAudited(this.isAudited());
        registry.setDeleted(this.isDeleted());
        registry.setFkCommissionCalcTypeId(this.getFkCommissionCalcTypeId());
        registry.setFkCommissionValueTypeId(this.getFkCommissionValueTypeId());
        registry.setFkUserAuditedId(this.getFkUserAuditedId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserAudited(this.getTsUserAudited());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        for (DDbCommissionCalcNote child : mvChildNotes) {
            registry.getChildNotes().add(child.clone());
        }

        for (DDbCommissionCalcRow child : mvChildRows) {
            registry.getChildRows().add(child.clone());
        }

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
