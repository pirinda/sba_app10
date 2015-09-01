/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.fin.db.DFinUtils;

/**
 *
 * @author Sergio Flores
 */
public class DDbDpsPrinting extends DDbRegistryUser {

    protected int mnPkDpsId;
    protected int mnPkPrintingId;
    protected String msSeries;
    protected int mnNumber;
    protected String msOrder;
    protected Date mtDate;
    /*
    protected boolean mbDeleted;
    */
    protected int mnFkEmissionTypeId;
    protected int mnFkDpsCategoryId;
    protected int mnFkDpsClassId;
    protected int mnFkDpsTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected boolean mbHasDpsChanged;

    public DDbDpsPrinting() {
        super(DModConsts.T_DPS_PRT);
        initRegistry();
    }

    public void setPkDpsId(int n) { mnPkDpsId = n; }
    public void setPkPrintingId(int n) { mnPkPrintingId = n; }
    public void setSeries(String s) { msSeries = s; }
    public void setNumber(int n) { mnNumber = n; }
    public void setOrder(String s) { msOrder = s; }
    public void setDate(Date t) { mtDate = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkEmissionTypeId(int n) { mnFkEmissionTypeId = n; }
    public void setFkDpsCategoryId(int n) { mnFkDpsCategoryId = n; }
    public void setFkDpsClassId(int n) { mnFkDpsClassId = n; }
    public void setFkDpsTypeId(int n) { mnFkDpsTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkDpsId() { return mnPkDpsId; }
    public int getPkPrintingId() { return mnPkPrintingId; }
    public String getSeries() { return msSeries; }
    public int getNumber() { return mnNumber; }
    public String getOrder() { return msOrder; }
    public Date getDate() { return mtDate; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkEmissionTypeId() { return mnFkEmissionTypeId; }
    public int getFkDpsCategoryId() { return mnFkDpsCategoryId; }
    public int getFkDpsClassId() { return mnFkDpsClassId; }
    public int getFkDpsTypeId() { return mnFkDpsTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public boolean hasDpsChanged() { return mbHasDpsChanged; }

    public int[] getDpsCategoryKey() { return new int[] { mnFkDpsCategoryId }; }
    public int[] getDpsClassKey() { return new int[] { mnFkDpsCategoryId, mnFkDpsClassId }; }
    public int[] getDpsTypeKey() { return new int[] { mnFkDpsCategoryId, mnFkDpsClassId, mnFkDpsTypeId }; }
    public String getDpsNumber() { return DTrnUtils.composeDpsNumber(msSeries, mnNumber); }
    public String getDpsReference() { return DTrnUtils.composeDpsReference(msSeries, mnNumber); }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDpsId = pk[0];
        mnPkPrintingId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDpsId, mnPkPrintingId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkDpsId = 0;
        mnPkPrintingId = 0;
        msSeries = "";
        mnNumber = 0;
        msOrder = "";
        mtDate = null;
        mbDeleted = false;
        mnFkEmissionTypeId = 0;
        mnFkDpsCategoryId = 0;
        mnFkDpsClassId = 0;
        mnFkDpsTypeId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mbHasDpsChanged = false;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_dps = " + mnPkDpsId + " AND " +
                "id_prt = " + mnPkPrintingId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_dps = " + pk[0] + " AND " +
                "id_prt = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkPrintingId = 0;

        msSql = "SELECT COALESCE(MAX(id_prt), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_dps = " + mnPkDpsId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkPrintingId = resultSet.getInt(1);
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
            mnPkPrintingId = resultSet.getInt("id_prt");
            msSeries = resultSet.getString("ser");
            mnNumber = resultSet.getInt("num");
            msOrder = resultSet.getString("ord");
            mtDate = resultSet.getDate("dt");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkEmissionTypeId = resultSet.getInt("fk_emi_tp");
            mnFkDpsCategoryId = resultSet.getInt("fk_dps_ct");
            mnFkDpsClassId = resultSet.getInt("fk_dps_cl");
            mnFkDpsTypeId = resultSet.getInt("fk_dps_tp");
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
        DDbDps dps = null;

        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkDpsId + ", " +
                    mnPkPrintingId + ", " +
                    "'" + msSeries + "', " +
                    mnNumber + ", " +
                    "'" + msOrder + "', " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkEmissionTypeId + ", " +
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
                    "id_dps = " + mnPkDpsId + ", " +
                    "id_prt = " + mnPkPrintingId + ", " +
                    */
                    "ser = '" + msSeries + "', " +
                    "num = " + mnNumber + ", " +
                    "ord = '" + msOrder + "', " +
                    "dt = '" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_emi_tp = " + mnFkEmissionTypeId + ", " +
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

        // Update DPS:

        dps = (DDbDps) session.readRegistry(DModConsts.T_DPS, new int[] { mnPkDpsId });

        if (mbHasDpsChanged = dps.hasDpsChanged(msSeries, mnNumber, msOrder, mtDate, mnFkEmissionTypeId, getDpsTypeKey())) {
            msSql = "UPDATE " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " SET " +
                    "ser = '" + msSeries + "', " +
                    "num = " + mnNumber + ", " +
                    "ord = '" + msOrder + "', " +
                    "dt = '" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "fk_emi_tp = " + mnFkEmissionTypeId + ", " +
                    "fk_dps_ct = " + mnFkDpsCategoryId + ", " +
                    "fk_dps_cl = " + mnFkDpsClassId + ", " +
                    "fk_dps_tp = " + mnFkDpsTypeId + " " +
                    "WHERE id_dps = " + mnPkDpsId + " ";
            session.getStatement().execute(msSql);

            msSql = "UPDATE " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " SET ref = '" + getDpsReference() + "' " +
                    "WHERE fk_dps_inv_n = " + mnPkDpsId + " AND " +
                    "fk_sys_acc_tp = " + DFinUtils.getSysAccountTypeForBizPartnerClass(DTrnUtils.getBizPartnerClassByDpsCategory(mnFkDpsCategoryId)) + " ";
            session.getStatement().execute(msSql);
        }

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbDpsPrinting clone() throws CloneNotSupportedException {
        DDbDpsPrinting registry = new DDbDpsPrinting();

        registry.setPkDpsId(this.getPkDpsId());
        registry.setPkPrintingId(this.getPkPrintingId());
        registry.setSeries(this.getSeries());
        registry.setNumber(this.getNumber());
        registry.setOrder(this.getOrder());
        registry.setDate(this.getDate());
        registry.setDeleted(this.isDeleted());
        registry.setFkEmissionTypeId(this.getFkEmissionTypeId());
        registry.setFkDpsCategoryId(this.getFkDpsCategoryId());
        registry.setFkDpsClassId(this.getFkDpsClassId());
        registry.setFkDpsTypeId(this.getFkDpsTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
