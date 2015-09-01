/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.fin.db;

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
public class DDbAbpBizPartner extends DDbRegistryUser {

    protected int mnPkAbpBizPartnerId;
    protected String msName;
    //protected boolean mbDeleted;
    protected int mnFkBizPartnerClassId;
    protected int mnFkAccountBizPartnerId;
    protected int mnFkAccountBizPartnerAdvanceId;
    protected int mnFkAccountDecDocExchangeRateId;
    protected int mnFkAccountDecDocAdjustmentId;
    protected int mnFkAccountDecMoneyExchangeRateId;
    protected int mnFkAccountDecMoneyAdjustmentId;
    protected int mnFkAccountDecMoneyAdvanceExchangeRateId;
    protected int mnFkAccountDecMoneyAdvanceAdjustmentId;
    protected int mnFkAccountIncDocExchangeRateId;
    protected int mnFkAccountIncDocAdjustmentId;
    protected int mnFkAccountIncMoneyExchangeRateId;
    protected int mnFkAccountIncMoneyAdjustmentId;
    protected int mnFkAccountIncMoneyAdvanceExchangeRateId;
    protected int mnFkAccountIncMoneyAdvanceAdjustmentId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public DDbAbpBizPartner() {
        super(DModConsts.F_ABP_BPR);
        initRegistry();
    }

    public void setPkAbpBizPartnerId(int n) { mnPkAbpBizPartnerId = n; }
    public void setName(String s) { msName = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkBizPartnerClassId(int n) { mnFkBizPartnerClassId = n; }
    public void setFkAccountBizPartnerId(int n) { mnFkAccountBizPartnerId = n; }
    public void setFkAccountBizPartnerAdvanceId(int n) { mnFkAccountBizPartnerAdvanceId = n; }
    public void setFkAccountDecDocExchangeRateId(int n) { mnFkAccountDecDocExchangeRateId = n; }
    public void setFkAccountDecDocAdjustmentId(int n) { mnFkAccountDecDocAdjustmentId = n; }
    public void setFkAccountDecMoneyExchangeRateId(int n) { mnFkAccountDecMoneyExchangeRateId = n; }
    public void setFkAccountDecMoneyAdjustmentId(int n) { mnFkAccountDecMoneyAdjustmentId = n; }
    public void setFkAccountDecMoneyAdvanceExchangeRateId(int n) { mnFkAccountDecMoneyAdvanceExchangeRateId = n; }
    public void setFkAccountDecMoneyAdvanceAdjustmentId(int n) { mnFkAccountDecMoneyAdvanceAdjustmentId = n; }
    public void setFkAccountIncDocExchangeRateId(int n) { mnFkAccountIncDocExchangeRateId = n; }
    public void setFkAccountIncDocAdjustmentId(int n) { mnFkAccountIncDocAdjustmentId = n; }
    public void setFkAccountIncMoneyExchangeRateId(int n) { mnFkAccountIncMoneyExchangeRateId = n; }
    public void setFkAccountIncMoneyAdjustmentId(int n) { mnFkAccountIncMoneyAdjustmentId = n; }
    public void setFkAccountIncMoneyAdvanceExchangeRateId(int n) { mnFkAccountIncMoneyAdvanceExchangeRateId = n; }
    public void setFkAccountIncMoneyAdvanceAdjustmentId(int n) { mnFkAccountIncMoneyAdvanceAdjustmentId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkAbpBizPartnerId() { return mnPkAbpBizPartnerId; }
    public String getName() { return msName; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkBizPartnerClassId() { return mnFkBizPartnerClassId; }
    public int getFkAccountBizPartnerId() { return mnFkAccountBizPartnerId; }
    public int getFkAccountBizPartnerAdvanceId() { return mnFkAccountBizPartnerAdvanceId; }
    public int getFkAccountDecDocExchangeRateId() { return mnFkAccountDecDocExchangeRateId; }
    public int getFkAccountDecDocAdjustmentId() { return mnFkAccountDecDocAdjustmentId; }
    public int getFkAccountDecMoneyExchangeRateId() { return mnFkAccountDecMoneyExchangeRateId; }
    public int getFkAccountDecMoneyAdjustmentId() { return mnFkAccountDecMoneyAdjustmentId; }
    public int getFkAccountDecMoneyAdvanceExchangeRateId() { return mnFkAccountDecMoneyAdvanceExchangeRateId; }
    public int getFkAccountDecMoneyAdvanceAdjustmentId() { return mnFkAccountDecMoneyAdvanceAdjustmentId; }
    public int getFkAccountIncDocExchangeRateId() { return mnFkAccountIncDocExchangeRateId; }
    public int getFkAccountIncDocAdjustmentId() { return mnFkAccountIncDocAdjustmentId; }
    public int getFkAccountIncMoneyExchangeRateId() { return mnFkAccountIncMoneyExchangeRateId; }
    public int getFkAccountIncMoneyAdjustmentId() { return mnFkAccountIncMoneyAdjustmentId; }
    public int getFkAccountIncMoneyAdvanceExchangeRateId() { return mnFkAccountIncMoneyAdvanceExchangeRateId; }
    public int getFkAccountIncMoneyAdvanceAdjustmentId() { return mnFkAccountIncMoneyAdvanceAdjustmentId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkAbpBizPartnerId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkAbpBizPartnerId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkAbpBizPartnerId = 0;
        msName = "";
        mbDeleted = false;
        mnFkBizPartnerClassId = 0;
        mnFkAccountBizPartnerId = 0;
        mnFkAccountBizPartnerAdvanceId = 0;
        mnFkAccountDecDocExchangeRateId = 0;
        mnFkAccountDecDocAdjustmentId = 0;
        mnFkAccountDecMoneyExchangeRateId = 0;
        mnFkAccountDecMoneyAdjustmentId = 0;
        mnFkAccountDecMoneyAdvanceExchangeRateId = 0;
        mnFkAccountDecMoneyAdvanceAdjustmentId = 0;
        mnFkAccountIncDocExchangeRateId = 0;
        mnFkAccountIncDocAdjustmentId = 0;
        mnFkAccountIncMoneyExchangeRateId = 0;
        mnFkAccountIncMoneyAdjustmentId = 0;
        mnFkAccountIncMoneyAdvanceExchangeRateId = 0;
        mnFkAccountIncMoneyAdvanceAdjustmentId = 0;
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
        return "WHERE id_abp_bpr = " + mnPkAbpBizPartnerId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_abp_bpr = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkAbpBizPartnerId = 0;

        msSql = "SELECT COALESCE(MAX(id_abp_bpr), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkAbpBizPartnerId = resultSet.getInt(1);
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
            mnPkAbpBizPartnerId = resultSet.getInt("id_abp_bpr");
            msName = resultSet.getString("name");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkBizPartnerClassId = resultSet.getInt("fk_bpr_cl");
            mnFkAccountBizPartnerId = resultSet.getInt("fk_acc_bpr");
            mnFkAccountBizPartnerAdvanceId = resultSet.getInt("fk_acc_bpr_adv");
            mnFkAccountDecDocExchangeRateId = resultSet.getInt("fk_acc_dec_doc_exr");
            mnFkAccountDecDocAdjustmentId = resultSet.getInt("fk_acc_dec_doc_adj");
            mnFkAccountDecMoneyExchangeRateId = resultSet.getInt("fk_acc_dec_mon_exr");
            mnFkAccountDecMoneyAdjustmentId = resultSet.getInt("fk_acc_dec_mon_adj");
            mnFkAccountDecMoneyAdvanceExchangeRateId = resultSet.getInt("fk_acc_dec_mon_adv_exr");
            mnFkAccountDecMoneyAdvanceAdjustmentId = resultSet.getInt("fk_acc_dec_mon_adv_adj");
            mnFkAccountIncDocExchangeRateId = resultSet.getInt("fk_acc_inc_doc_exr");
            mnFkAccountIncDocAdjustmentId = resultSet.getInt("fk_acc_inc_doc_adj");
            mnFkAccountIncMoneyExchangeRateId = resultSet.getInt("fk_acc_inc_mon_exr");
            mnFkAccountIncMoneyAdjustmentId = resultSet.getInt("fk_acc_inc_mon_adj");
            mnFkAccountIncMoneyAdvanceExchangeRateId = resultSet.getInt("fk_acc_inc_mon_adv_exr");
            mnFkAccountIncMoneyAdvanceAdjustmentId = resultSet.getInt("fk_acc_inc_mon_adv_adj");
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
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkAbpBizPartnerId + ", " +
                    "'" + msName + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkBizPartnerClassId + ", " +
                    mnFkAccountBizPartnerId + ", " +
                    mnFkAccountBizPartnerAdvanceId + ", " +
                    mnFkAccountDecDocExchangeRateId + ", " +
                    mnFkAccountDecDocAdjustmentId + ", " +
                    mnFkAccountDecMoneyExchangeRateId + ", " +
                    mnFkAccountDecMoneyAdjustmentId + ", " +
                    mnFkAccountDecMoneyAdvanceExchangeRateId + ", " +
                    mnFkAccountDecMoneyAdvanceAdjustmentId + ", " +
                    mnFkAccountIncDocExchangeRateId + ", " +
                    mnFkAccountIncDocAdjustmentId + ", " +
                    mnFkAccountIncMoneyExchangeRateId + ", " +
                    mnFkAccountIncMoneyAdjustmentId + ", " +
                    mnFkAccountIncMoneyAdvanceExchangeRateId + ", " +
                    mnFkAccountIncMoneyAdvanceAdjustmentId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_abp_bpr = " + mnPkAbpBizPartnerId + ", " +
                    "name = '" + msName + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_bpr_cl = " + mnFkBizPartnerClassId + ", " +
                    "fk_acc_bpr = " + mnFkAccountBizPartnerId + ", " +
                    "fk_acc_bpr_adv = " + mnFkAccountBizPartnerAdvanceId + ", " +
                    "fk_acc_dec_doc_exr = " + mnFkAccountDecDocExchangeRateId + ", " +
                    "fk_acc_dec_doc_adj = " + mnFkAccountDecDocAdjustmentId + ", " +
                    "fk_acc_dec_mon_exr = " + mnFkAccountDecMoneyExchangeRateId + ", " +
                    "fk_acc_dec_mon_adj = " + mnFkAccountDecMoneyAdjustmentId + ", " +
                    "fk_acc_dec_mon_adv_exr = " + mnFkAccountDecMoneyAdvanceExchangeRateId + ", " +
                    "fk_acc_dec_mon_adv_adj = " + mnFkAccountDecMoneyAdvanceAdjustmentId + ", " +
                    "fk_acc_inc_doc_exr = " + mnFkAccountIncDocExchangeRateId + ", " +
                    "fk_acc_inc_doc_adj = " + mnFkAccountIncDocAdjustmentId + ", " +
                    "fk_acc_inc_mon_exr = " + mnFkAccountIncMoneyExchangeRateId + ", " +
                    "fk_acc_inc_mon_adj = " + mnFkAccountIncMoneyAdjustmentId + ", " +
                    "fk_acc_inc_mon_adv_exr = " + mnFkAccountIncMoneyAdvanceExchangeRateId + ", " +
                    "fk_acc_inc_mon_adv_adj = " + mnFkAccountIncMoneyAdvanceAdjustmentId + ", " +
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
    public DDbAbpBizPartner clone() throws CloneNotSupportedException {
        DDbAbpBizPartner registry = new DDbAbpBizPartner();

        registry.setPkAbpBizPartnerId(this.getPkAbpBizPartnerId());
        registry.setName(this.getName());
        registry.setDeleted(this.isDeleted());
        registry.setFkBizPartnerClassId(this.getFkBizPartnerClassId());
        registry.setFkAccountBizPartnerId(this.getFkAccountBizPartnerId());
        registry.setFkAccountBizPartnerAdvanceId(this.getFkAccountBizPartnerAdvanceId());
        registry.setFkAccountDecDocExchangeRateId(this.getFkAccountDecDocExchangeRateId());
        registry.setFkAccountDecDocAdjustmentId(this.getFkAccountDecDocAdjustmentId());
        registry.setFkAccountDecMoneyExchangeRateId(this.getFkAccountDecMoneyExchangeRateId());
        registry.setFkAccountDecMoneyAdjustmentId(this.getFkAccountDecMoneyAdjustmentId());
        registry.setFkAccountDecMoneyAdvanceExchangeRateId(this.getFkAccountDecMoneyAdvanceExchangeRateId());
        registry.setFkAccountDecMoneyAdvanceAdjustmentId(this.getFkAccountDecMoneyAdvanceAdjustmentId());
        registry.setFkAccountIncDocExchangeRateId(this.getFkAccountIncDocExchangeRateId());
        registry.setFkAccountIncDocAdjustmentId(this.getFkAccountIncDocAdjustmentId());
        registry.setFkAccountIncMoneyExchangeRateId(this.getFkAccountIncMoneyExchangeRateId());
        registry.setFkAccountIncMoneyAdjustmentId(this.getFkAccountIncMoneyAdjustmentId());
        registry.setFkAccountIncMoneyAdvanceExchangeRateId(this.getFkAccountIncMoneyAdvanceExchangeRateId());
        registry.setFkAccountIncMoneyAdvanceAdjustmentId(this.getFkAccountIncMoneyAdvanceAdjustmentId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
