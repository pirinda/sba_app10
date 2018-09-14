/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
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
public class DDbDpsSeries extends DDbRegistryUser {

    public static final int FIELD_SERIES = 1;
    public static final int FIELD_DPS_TP_KEY = 2;

    protected int mnPkSeriesId;
    protected String msSeries;
    protected int mnRowMaximum;
    protected boolean mbTaxImprovement;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkDpsCategoryId;
    protected int mnFkDpsClassId;
    protected int mnFkDpsTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected Vector<DDbDpsSeriesBranch> mvChildBranches;

    public DDbDpsSeries() {
        super(DModConsts.TU_SER);
        mvChildBranches = new Vector<>();
        initRegistry();
    }

    public void setPkSeriesId(int n) { mnPkSeriesId = n; }
    public void setSeries(String s) { msSeries = s; }
    public void setRowMaximum(int n) { mnRowMaximum = n; }
    public void setTaxImprovement(boolean b) { mbTaxImprovement = b; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkDpsCategoryId(int n) { mnFkDpsCategoryId = n; }
    public void setFkDpsClassId(int n) { mnFkDpsClassId = n; }
    public void setFkDpsTypeId(int n) { mnFkDpsTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkSeriesId() { return mnPkSeriesId; }
    public String getSeries() { return msSeries; }
    public int getRowMaximum() { return mnRowMaximum; }
    public boolean isTaxImprovement() { return mbTaxImprovement; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkDpsCategoryId() { return mnFkDpsCategoryId; }
    public int getFkDpsClassId() { return mnFkDpsClassId; }
    public int getFkDpsTypeId() { return mnFkDpsTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public Vector<DDbDpsSeriesBranch> getChildBranches() { return mvChildBranches; }

    public int[] getDpsCategoryKey() { return new int[] { mnFkDpsCategoryId }; }
    public int[] getDpsClassKey() { return new int[] { mnFkDpsCategoryId, mnFkDpsClassId }; }
    public int[] getDpsTypeKey() { return new int[] { mnFkDpsCategoryId, mnFkDpsClassId, mnFkDpsTypeId }; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkSeriesId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkSeriesId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkSeriesId = 0;
        msSeries = "";
        mnRowMaximum = 0;
        mbTaxImprovement = false;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkDpsCategoryId = 0;
        mnFkDpsClassId = 0;
        mnFkDpsTypeId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mvChildBranches.clear();
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_ser = " + mnPkSeriesId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_ser = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkSeriesId = 0;

        msSql = "SELECT COALESCE(MAX(id_ser), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkSeriesId = resultSet.getInt(1);
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
            mnPkSeriesId = resultSet.getInt("id_ser");
            msSeries = resultSet.getString("ser");
            mnRowMaximum = resultSet.getInt("row_max");
            mbTaxImprovement = resultSet.getBoolean("b_tax_imp");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkDpsCategoryId = resultSet.getInt("fk_dps_ct");
            mnFkDpsClassId = resultSet.getInt("fk_dps_cl");
            mnFkDpsTypeId = resultSet.getInt("fk_dps_tp");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();

            msSql = "SELECT id_bpr, id_bra FROM " + DModConsts.TablesMap.get(DModConsts.TU_SER_BRA) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbDpsSeriesBranch child = new DDbDpsSeriesBranch();
                child.read(session, new int[] { mnPkSeriesId, resultSet.getInt(1), resultSet.getInt(2) });
                mvChildBranches.add(child);
            }

            // Finish registry reading:

            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        String sql = "";
        ResultSet resultSet = null;

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
                    "'" + msSeries + "', " +
                    mnRowMaximum + ", " +
                    (mbTaxImprovement ? 1 : 0) + ", " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkDpsCategoryId + ", " +
                    mnFkDpsClassId + ", " +
                    mnFkDpsTypeId + ", " +
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
                    */
                    "ser = '" + msSeries + "', " +
                    "row_max = " + mnRowMaximum + ", " +
                    "b_tax_imp = " + (mbTaxImprovement ? 1 : 0) + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_dps_ct = " + mnFkDpsCategoryId + ", " +
                    "fk_dps_cl = " + mnFkDpsClassId + ", " +
                    "fk_dps_tp = " + mnFkDpsTypeId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        for (DDbDpsSeriesBranch child : mvChildBranches) {
            child.setPkSeriesId(mnPkSeriesId);
            sql += (sql.length() == 0 ? "": " OR ") + "(" +
                    "id_ser = " + child.getPkSeriesId() + " AND id_bpr = " + child.getPkBizPartnerId() + " AND id_bra = " + child.getPkBranchId() + ")";
        }

        if (sql.length() > 0) {
            msSql = "DELETE FROM " + DModConsts.TablesMap.get(DModConsts.CU_USR_SER_BRA) + " " +
                    "WHERE id_ser = " + mnPkSeriesId + " AND NOT (" + sql + ")";
            session.getStatement().execute(msSql);

            msSql = "DELETE FROM " + DModConsts.TablesMap.get(DModConsts.TU_SER_BRA) + " " +
                    "WHERE id_ser = " + mnPkSeriesId + " AND NOT (" + sql + ")";
            session.getStatement().execute(msSql);
        }

        for (DDbDpsSeriesBranch child : mvChildBranches) {
            msSql = "SELECT COUNT(*) " + child.getSqlFromWhere();
            resultSet = session.getStatement().executeQuery(msSql);
            if (resultSet.next()) {
                if (resultSet.getInt(1) == 0) {
                    child.save(session);
                }
            }
        }

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbDpsSeries clone() throws CloneNotSupportedException {
        DDbDpsSeries registry = new DDbDpsSeries();

        registry.setPkSeriesId(this.getPkSeriesId());
        registry.setSeries(this.getSeries());
        registry.setRowMaximum(this.getRowMaximum());
        registry.setTaxImprovement(this.isTaxImprovement());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkDpsCategoryId(this.getFkDpsCategoryId());
        registry.setFkDpsClassId(this.getFkDpsClassId());
        registry.setFkDpsTypeId(this.getFkDpsTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        for (DDbDpsSeriesBranch child : mvChildBranches) {
            registry.getChildBranches().add(child.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public void setRegistryNew(boolean registryNew) {
        super.setRegistryNew(registryNew);

        for (DDbDpsSeriesBranch child : mvChildBranches) {
            child.setRegistryNew(registryNew);
        }
    }

    @Override
    public Object readField(final Statement statement, final int[] pk, final int field) throws SQLException, Exception {
        Object value = null;
        ResultSet resultSet = null;

        if (isBaseRegistryField(field)) {
            value = super.readField(statement, pk, field);
        }
        else {
            initQueryMembers();
            mnQueryResultId = DDbConsts.READ_ERROR;

            switch (field) {
                case FIELD_SERIES:
                    msSql = "SELECT ser ";
                    break;
                case FIELD_DPS_TP_KEY:
                    msSql = "SELECT fk_dps_ct, fk_dps_cl, fk_dps_tp ";
                    break;
                default:
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }

            msSql += getSqlFromWhere(pk);
            resultSet = statement.executeQuery(msSql);
            if (!resultSet.next()) {
                throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                switch (field) {
                    case FIELD_SERIES:
                        value = resultSet.getString(1);
                        break;
                    case FIELD_DPS_TP_KEY:
                        value = new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) };
                        break;
                    default:
                }
            }

            mnQueryResultId = DDbConsts.READ_OK;
        }

        return value;
    }
}
