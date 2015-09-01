/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.bpr.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
public class DDbBizPartnerType extends DDbRegistryUser {

    protected int mnPkBizPartnerClassId;
    protected int mnPkBizPartnerTypeId;
    protected String msCode;
    protected String msName;
    protected double mdCreditLimit;
    protected int mnCreditDays;
    protected int mnCreditDaysGrace;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkCustomerTypeId_n;
    protected int mnFkCreditTypeId;
    protected int mnFkModeOfPaymentTypeId;
    protected int mnFkAbpBizPartnerId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public DDbBizPartnerType() {
        super(DModConsts.BU_BPR_TP);
        initRegistry();
    }

    public void setPkBizPartnerClassId(int n) { mnPkBizPartnerClassId = n; }
    public void setPkBizPartnerTypeId(int n) { mnPkBizPartnerTypeId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setCreditLimit(double d) { mdCreditLimit = d; }
    public void setCreditDays(int n) { mnCreditDays = n; }
    public void setCreditDaysGrace(int n) { mnCreditDaysGrace = n; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkCustomerTypeId_n(int n) { mnFkCustomerTypeId_n = n; }
    public void setFkCreditTypeId(int n) { mnFkCreditTypeId = n; }
    public void setFkModeOfPaymentTypeId(int n) { mnFkModeOfPaymentTypeId = n; }
    public void setFkAbpBizPartnerId(int n) { mnFkAbpBizPartnerId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkBizPartnerClassId() { return mnPkBizPartnerClassId; }
    public int getPkBizPartnerTypeId() { return mnPkBizPartnerTypeId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public double getCreditLimit() { return mdCreditLimit; }
    public int getCreditDays() { return mnCreditDays; }
    public int getCreditDaysGrace() { return mnCreditDaysGrace; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkCustomerTypeId_n() { return mnFkCustomerTypeId_n; }
    public int getFkCreditTypeId() { return mnFkCreditTypeId; }
    public int getFkModeOfPaymentTypeId() { return mnFkModeOfPaymentTypeId; }
    public int getFkAbpBizPartnerId() { return mnFkAbpBizPartnerId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBizPartnerClassId = pk[0];
        mnPkBizPartnerTypeId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBizPartnerClassId, mnPkBizPartnerTypeId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBizPartnerClassId = 0;
        mnPkBizPartnerTypeId = 0;
        msCode = "";
        msName = "";
        mdCreditLimit = 0;
        mnCreditDays = 0;
        mnCreditDaysGrace = 0;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkCustomerTypeId_n = 0;
        mnFkCreditTypeId = 0;
        mnFkModeOfPaymentTypeId = 0;
        mnFkAbpBizPartnerId = 0;
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
        return "WHERE id_bpr_cl = " + mnPkBizPartnerClassId + " AND " +
                "id_bpr_tp = " + mnPkBizPartnerTypeId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bpr_cl = " + pk[0] + " AND " +
                "id_bpr_tp = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkBizPartnerTypeId = 0;

        msSql = "SELECT COALESCE(MAX(id_bpr_tp), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkBizPartnerTypeId = resultSet.getInt(1);
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
            mnPkBizPartnerClassId = resultSet.getInt("id_bpr_cl");
            mnPkBizPartnerTypeId = resultSet.getInt("id_bpr_tp");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            mdCreditLimit = resultSet.getDouble("cdt_lim");
            mnCreditDays = resultSet.getInt("cdt_day");
            mnCreditDaysGrace = resultSet.getInt("cdt_day_gra");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkCustomerTypeId_n = resultSet.getInt("fk_cus_tp_n");
            mnFkCreditTypeId = resultSet.getInt("fk_cdt_tp");
            mnFkModeOfPaymentTypeId = resultSet.getInt("fk_mop_tp");
            mnFkAbpBizPartnerId = resultSet.getInt("fk_abp_bpr");
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
            mbUpdatable = true;
            mbDisableable = true;
            mbDeletable = true;
            mbDisabled = false;
            mbDeleted = false;
            //mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkBizPartnerClassId + ", " +
                    mnPkBizPartnerTypeId + ", " +
                    "'" + msCode + "', " +
                    "'" + msName + "', " +
                    mdCreditLimit + ", " +
                    mnCreditDays + ", " +
                    mnCreditDaysGrace + ", " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    (mnFkCustomerTypeId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCustomerTypeId_n) + ", " +
                    mnFkCreditTypeId + ", " +
                    mnFkModeOfPaymentTypeId + ", " +
                    mnFkAbpBizPartnerId + ", " +
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
                    "id_bpr_cl = " + mnPkBizPartnerClassId + ", " +
                    "id_bpr_tp = " + mnPkBizPartnerTypeId + ", " +
                    */
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "cdt_lim = " + mdCreditLimit + ", " +
                    "cdt_day = " + mnCreditDays + ", " +
                    "cdt_day_gra = " + mnCreditDaysGrace + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_cus_tp_n = " + (mnFkCustomerTypeId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCustomerTypeId_n) + ", " +
                    "fk_cdt_tp = " + mnFkCreditTypeId + ", " +
                    "fk_mop_tp = " + mnFkModeOfPaymentTypeId + ", " +
                    "fk_abp_bpr = " + mnFkAbpBizPartnerId + ", " +
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
    public DDbBizPartnerType clone() throws CloneNotSupportedException {
        DDbBizPartnerType registry = new DDbBizPartnerType();

        registry.setPkBizPartnerClassId(this.getPkBizPartnerClassId());
        registry.setPkBizPartnerTypeId(this.getPkBizPartnerTypeId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setCreditLimit(this.getCreditLimit());
        registry.setCreditDays(this.getCreditDays());
        registry.setCreditDaysGrace(this.getCreditDaysGrace());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkCustomerTypeId_n(this.getFkCustomerTypeId_n());
        registry.setFkCreditTypeId(this.getFkCreditTypeId());
        registry.setFkModeOfPaymentTypeId(this.getFkModeOfPaymentTypeId());
        registry.setFkAbpBizPartnerId(this.getFkAbpBizPartnerId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
