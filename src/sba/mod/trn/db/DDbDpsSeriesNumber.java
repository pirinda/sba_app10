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
public class DDbDpsSeriesNumber extends DDbRegistryUser {

    protected int mnPkSeriesId;
    protected int mnPkNumberId;
    protected int mnNumberStart;
    protected int mnNumberEnd_n;
    protected int mnApprobationYear;
    protected int mnApprobationNumber;
    protected Date mtApprobationDate_n;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkXmlTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected DDbDpsSeries moParentDpsSeries;

    public DDbDpsSeriesNumber() {
        super(DModConsts.TU_SER_NUM);
        initRegistry();
    }

    public void setPkSeriesId(int n) { mnPkSeriesId = n; }
    public void setPkNumberId(int n) { mnPkNumberId = n; }
    public void setNumberStart(int n) { mnNumberStart = n; }
    public void setNumberEnd_n(int n) { mnNumberEnd_n = n; }
    public void setApprobationYear(int n) { mnApprobationYear = n; }
    public void setApprobationNumber(int n) { mnApprobationNumber = n; }
    public void setApprobationDate_n(Date t) { mtApprobationDate_n = t; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkXmlTypeId(int n) { mnFkXmlTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkSeriesId() { return mnPkSeriesId; }
    public int getPkNumberId() { return mnPkNumberId; }
    public int getNumberStart() { return mnNumberStart; }
    public int getNumberEnd_n() { return mnNumberEnd_n; }
    public int getApprobationYear() { return mnApprobationYear; }
    public int getApprobationNumber() { return mnApprobationNumber; }
    public Date getApprobationDate_n() { return mtApprobationDate_n; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkXmlTypeId() { return mnFkXmlTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setParentDpsSeries(DDbDpsSeries o) { moParentDpsSeries = o; }

    public DDbDpsSeries getParentDpsSeries() { return moParentDpsSeries; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkSeriesId = pk[0];
        mnPkNumberId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkSeriesId, mnPkNumberId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkSeriesId = 0;
        mnPkNumberId = 0;
        mnNumberStart = 0;
        mnNumberEnd_n = 0;
        mnApprobationYear = 0;
        mnApprobationNumber = 0;
        mtApprobationDate_n = null;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkXmlTypeId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        moParentDpsSeries = null;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_ser = " + mnPkSeriesId + " AND " +
                "id_num = " + mnPkNumberId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_ser = " + pk[0] + " AND " +
                "id_num = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkNumberId = 0;

        msSql = "SELECT COALESCE(MAX(id_num), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_ser = " + mnPkSeriesId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkNumberId = resultSet.getInt(1);
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
            mnPkSeriesId = resultSet.getInt("id_ser");
            mnPkNumberId = resultSet.getInt("id_num");
            mnNumberStart = resultSet.getInt("num_sta");
            mnNumberEnd_n = resultSet.getInt("num_end_n");
            mnApprobationYear = resultSet.getInt("apb_yer");
            mnApprobationNumber = resultSet.getInt("apb_num");
            mtApprobationDate_n = resultSet.getDate("apb_dt_n");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkXmlTypeId = resultSet.getInt("fk_xml_tp");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read aswell parent registries:

            moParentDpsSeries = new DDbDpsSeries();
            moParentDpsSeries.read(session, new int[] { mnPkSeriesId });

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
            mbUpdatable = true;
            mbDisableable = true;
            mbDeletable = true;
            mbDisabled = false;
            mbDeleted = false;
            //mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkSeriesId + ", " +
                    mnPkNumberId + ", " +
                    mnNumberStart + ", " +
                    (mnNumberEnd_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnNumberEnd_n) + ", " +
                    mnApprobationYear + ", " +
                    mnApprobationNumber + ", " +
                    (mtApprobationDate_n == null ? "NULL" : "'" + DLibUtils.DbmsDateFormatDate.format(mtApprobationDate_n) + "'") + ", " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkXmlTypeId + ", " +
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
                    "id_ser = " + mnPkSeriesId + ", " +
                    "id_num = " + mnPkNumberId + ", " +
                    */
                    "num_sta = " + mnNumberStart + ", " +
                    "num_end_n = " + (mnNumberEnd_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnNumberEnd_n) + ", " +
                    "apb_yer = " + mnApprobationYear + ", " +
                    "apb_num = " + mnApprobationNumber + ", " +
                    "apb_dt_n = " + (mtApprobationDate_n == null ? "NULL" : "'" + DLibUtils.DbmsDateFormatDate.format(mtApprobationDate_n) + "'") + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_xml_tp = " + mnFkXmlTypeId + ", " +
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
    public DDbDpsSeriesNumber clone() throws CloneNotSupportedException {
        DDbDpsSeriesNumber registry = new DDbDpsSeriesNumber();

        registry.setPkSeriesId(this.getPkSeriesId());
        registry.setPkNumberId(this.getPkNumberId());
        registry.setNumberStart(this.getNumberStart());
        registry.setNumberEnd_n(this.getNumberEnd_n());
        registry.setApprobationYear(this.getApprobationYear());
        registry.setApprobationNumber(this.getApprobationNumber());
        registry.setApprobationDate_n(this.getApprobationDate_n());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkXmlTypeId(this.getFkXmlTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setParentDpsSeries(moParentDpsSeries == null ? null : moParentDpsSeries.clone());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
