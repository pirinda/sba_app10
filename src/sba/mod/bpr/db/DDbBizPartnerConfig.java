/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.bpr.db;

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
import sba.mod.cfg.db.DDbConfigCompany;

/**
 *
 * @author Sergio Flores
 */
public class DDbBizPartnerConfig extends DDbRegistryUser {

    protected int mnPkBizPartnerId;
    protected int mnPkBizPartnerClassId;
    protected String msCode;
    protected String msCodeOwn;
    protected double mdCreditLimit;
    protected int mnCreditDays;
    protected int mnCreditDaysGrace;
    protected Date mtDateStart;
    protected Date mtDateEnd_n;
    protected boolean mbCreditByUser;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkBizPartnerClassId;
    protected int mnFkBizPartnerTypeId;
    protected int mnFkCurrencyId_n;
    protected int mnFkCreditTypeId_n;
    protected int mnFkModeOfPaymentTypeId_n;
    protected int mnFkAbpBizPartnerId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected DDbBizPartnerType moRegBizPartnerType;

    public DDbBizPartnerConfig() {
        super(DModConsts.BU_BPR_CFG);
        initRegistry();
    }

    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setPkBizPartnerClassId(int n) { mnPkBizPartnerClassId = n; }
    public void setCode(String s) { msCode = s; }
    public void setCodeOwn(String s) { msCodeOwn = s; }
    public void setCreditLimit(double d) { mdCreditLimit = d; }
    public void setCreditDays(int n) { mnCreditDays = n; }
    public void setCreditDaysGrace(int n) { mnCreditDaysGrace = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setDateEnd_n(Date t) { mtDateEnd_n = t; }
    public void setCreditByUser(boolean b) { mbCreditByUser = b; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkBizPartnerClassId(int n) { mnFkBizPartnerClassId = n; }
    public void setFkBizPartnerTypeId(int n) { mnFkBizPartnerTypeId = n; }
    public void setFkCurrencyId_n(int n) { mnFkCurrencyId_n = n; }
    public void setFkCreditTypeId_n(int n) { mnFkCreditTypeId_n = n; }
    public void setFkModeOfPaymentTypeId_n(int n) { mnFkModeOfPaymentTypeId_n = n; }
    public void setFkAbpBizPartnerId_n(int n) { mnFkAbpBizPartnerId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public int getPkBizPartnerClassId() { return mnPkBizPartnerClassId; }
    public String getCode() { return msCode; }
    public String getCodeOwn() { return msCodeOwn; }
    public double getCreditLimit() { return mdCreditLimit; }
    public int getCreditDays() { return mnCreditDays; }
    public int getCreditDaysGrace() { return mnCreditDaysGrace; }
    public Date getDateStart() { return mtDateStart; }
    public Date getDateEnd_n() { return mtDateEnd_n; }
    public boolean isCreditByUser() { return mbCreditByUser; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkBizPartnerClassId() { return mnFkBizPartnerClassId; }
    public int getFkBizPartnerTypeId() { return mnFkBizPartnerTypeId; }
    public int getFkCurrencyId_n() { return mnFkCurrencyId_n; }
    public int getFkCreditTypeId_n() { return mnFkCreditTypeId_n; }
    public int getFkModeOfPaymentTypeId_n() { return mnFkModeOfPaymentTypeId_n; }
    public int getFkAbpBizPartnerId_n() { return mnFkAbpBizPartnerId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setRegBizPartnerType(DDbBizPartnerType o) { moRegBizPartnerType = o; }

    public DDbBizPartnerType getRegBizPartnerType() { return moRegBizPartnerType; }

    public double getActualCreditLimit() { return mbCreditByUser ? mdCreditLimit : moRegBizPartnerType.getCreditLimit(); }
    public int getActualCreditDays() { return mbCreditByUser ? mnCreditDays : moRegBizPartnerType.getCreditDays(); }
    public int getActualCreditDaysGrace() { return mbCreditByUser ? mnCreditDaysGrace : moRegBizPartnerType.getCreditDaysGrace(); }
    public int getActualFkCreditTypeId() { return mnFkCreditTypeId_n != DLibConsts.UNDEFINED ? mnFkCreditTypeId_n : moRegBizPartnerType.getFkCreditTypeId(); }
    public int getActualFkModeOfPaymentTypeId() { return mnFkModeOfPaymentTypeId_n != DLibConsts.UNDEFINED ? mnFkModeOfPaymentTypeId_n : moRegBizPartnerType.getFkModeOfPaymentTypeId(); }
    public int getActualFkAbpBizPartnerId() { return mnFkAbpBizPartnerId_n != DLibConsts.UNDEFINED ? mnFkAbpBizPartnerId_n : moRegBizPartnerType.getFkAbpBizPartnerId(); }
    public int getActualFkCurrencyId(DGuiSession session) { return mnFkCurrencyId_n != DLibConsts.UNDEFINED ? mnFkCurrencyId_n : ((DDbConfigCompany) session.getConfigCompany()).getFkCurrencyId(); }

    public int[] getBizPartnerTypeKey() { return new int[] { mnFkBizPartnerClassId, mnFkBizPartnerTypeId }; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBizPartnerId = pk[0];
        mnPkBizPartnerClassId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBizPartnerId, mnPkBizPartnerClassId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBizPartnerId = 0;
        mnPkBizPartnerClassId = 0;
        msCode = "";
        msCodeOwn = "";
        mdCreditLimit = 0;
        mnCreditDays = 0;
        mnCreditDaysGrace = 0;
        mtDateStart = null;
        mtDateEnd_n = null;
        mbCreditByUser = false;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkBizPartnerClassId = 0;
        mnFkBizPartnerTypeId = 0;
        mnFkCurrencyId_n = 0;
        mnFkCreditTypeId_n = 0;
        mnFkModeOfPaymentTypeId_n = 0;
        mnFkAbpBizPartnerId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        moRegBizPartnerType = null;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bpr = " + mnPkBizPartnerId + " AND " +
                "id_bpr_cl = " + mnPkBizPartnerClassId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bpr = " + pk[0] + " AND " +
                "id_bpr_cl = " + pk[1] + " ";
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
            mnPkBizPartnerId = resultSet.getInt("id_bpr");
            mnPkBizPartnerClassId = resultSet.getInt("id_bpr_cl");
            msCode = resultSet.getString("code");
            msCodeOwn = resultSet.getString("code_own");
            mdCreditLimit = resultSet.getDouble("cdt_lim");
            mnCreditDays = resultSet.getInt("cdt_day");
            mnCreditDaysGrace = resultSet.getInt("cdt_day_gra");
            mtDateStart = resultSet.getDate("dt_sta");
            mtDateEnd_n = resultSet.getDate("dt_end_n");
            mbCreditByUser = resultSet.getBoolean("b_cdt_usr");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkBizPartnerClassId = resultSet.getInt("fk_bpr_cl");
            mnFkBizPartnerTypeId = resultSet.getInt("fk_bpr_tp");
            mnFkCurrencyId_n = resultSet.getInt("fk_cur_n");
            mnFkCreditTypeId_n = resultSet.getInt("fk_cdt_tp_n");
            mnFkModeOfPaymentTypeId_n = resultSet.getInt("fk_mop_tp_n");
            mnFkAbpBizPartnerId_n = resultSet.getInt("fk_abp_bpr_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read aswell other registries:

            moRegBizPartnerType = new DDbBizPartnerType();
            moRegBizPartnerType.read(session, new int[] { mnFkBizPartnerClassId, mnFkBizPartnerTypeId });

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
            mbUpdatable = true;
            mbDisableable = true;
            mbDeletable = true;
            mbDisabled = false;
            mbDeleted = false;
            //mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkBizPartnerId + ", " +
                    mnPkBizPartnerClassId + ", " +
                    "'" + (msCode = msCode.length() > 0 ? msCode : "" + mnPkBizPartnerId) + "', " +
                    "'" + msCodeOwn + "', " +
                    mdCreditLimit + ", " +
                    mnCreditDays + ", " +
                    mnCreditDaysGrace + ", " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    (mtDateEnd_n == null ? "NULL" : "'" + DLibUtils.DbmsDateFormatDate.format(mtDateEnd_n) + "'") + ", " +
                    (mbCreditByUser ? 1 : 0) + ", " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkBizPartnerClassId + ", " +
                    mnFkBizPartnerTypeId + ", " +
                    (mnFkCurrencyId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCurrencyId_n) + ", " +
                    (mnFkCreditTypeId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCreditTypeId_n) + ", " +
                    (mnFkModeOfPaymentTypeId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkModeOfPaymentTypeId_n) + ", " +
                    (mnFkAbpBizPartnerId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAbpBizPartnerId_n) + ", " +
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
                    "id_bpr = " + mnPkBizPartnerId + ", " +
                    "id_bpr_cl = " + mnPkBizPartnerClassId + ", " +
                    */
                    "code = '" + msCode + "', " +
                    "code_own = '" + msCodeOwn + "', " +
                    "cdt_lim = " + mdCreditLimit + ", " +
                    "cdt_day = " + mnCreditDays + ", " +
                    "cdt_day_gra = " + mnCreditDaysGrace + ", " +
                    "dt_sta = '" + DLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "dt_end_n = " + (mtDateEnd_n == null ? "NULL" : "'" + DLibUtils.DbmsDateFormatDate.format(mtDateEnd_n) + "'") + ", " +
                    "b_cdt_usr = " + (mbCreditByUser ? 1 : 0) + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_bpr_cl = " + mnFkBizPartnerClassId + ", " +
                    "fk_bpr_tp = " + mnFkBizPartnerTypeId + ", " +
                    "fk_cur_n = " + (mnFkCurrencyId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCurrencyId_n) + ", " +
                    "fk_cdt_tp_n = " + (mnFkCreditTypeId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCreditTypeId_n) + ", " +
                    "fk_mop_tp_n = " + (mnFkModeOfPaymentTypeId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkModeOfPaymentTypeId_n) + ", " +
                    "fk_abp_bpr_n = " + (mnFkAbpBizPartnerId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAbpBizPartnerId_n) + ", " +
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
    public DDbBizPartnerConfig clone() throws CloneNotSupportedException {
        DDbBizPartnerConfig registry = new DDbBizPartnerConfig();

        registry.setPkBizPartnerId(this.getPkBizPartnerId());
        registry.setPkBizPartnerClassId(this.getPkBizPartnerClassId());
        registry.setCode(this.getCode());
        registry.setCodeOwn(this.getCodeOwn());
        registry.setCreditLimit(this.getCreditLimit());
        registry.setCreditDays(this.getCreditDays());
        registry.setCreditDaysGrace(this.getCreditDaysGrace());
        registry.setDateStart(this.getDateStart());
        registry.setDateEnd_n(this.getDateEnd_n());
        registry.setCreditByUser(this.isCreditByUser());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkBizPartnerClassId(this.getFkBizPartnerClassId());
        registry.setFkBizPartnerTypeId(this.getFkBizPartnerTypeId());
        registry.setFkCurrencyId_n(this.getFkCurrencyId_n());
        registry.setFkCreditTypeId_n(this.getFkCreditTypeId_n());
        registry.setFkModeOfPaymentTypeId_n(this.getFkModeOfPaymentTypeId_n());
        registry.setFkAbpBizPartnerId_n(this.getFkAbpBizPartnerId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegBizPartnerType(this.getRegBizPartnerType() == null ? null : this.getRegBizPartnerType().clone());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
