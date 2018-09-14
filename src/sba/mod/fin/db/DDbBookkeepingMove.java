/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.fin.db;

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
import sba.mod.DModSysConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbBookkeepingMove extends DDbRegistryUser {

    /*
     * DDbBookkeepingMove created integrally by the following methods and classes:
     * class sba.mod.trn.db.DDbDps
     * class sba.mod.trn.db.DDbIog
     * class sba.mod.fin.db.DDbProcessBookkeepingOpening
     * class sba.mod.fin.form.DPanelPayment
     */

    public static final int LEN_TEXT = 100;

    protected int mnPkYearId;
    protected int mnPkMoveId;
    protected Date mtDate;
    protected String msSupporting;
    protected String msReference;
    protected String msText;
    protected double mdDebit;
    protected double mdCredit;
    protected double mdExchangeRate;
    protected double mdDebitCy;
    protected double mdCreditCy;
    protected double mdUnits;
    protected int mnSortingPos;
    protected boolean mbExchangeRateDifference;
    protected boolean mbAvailable;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkAccountId;
    protected int mnFkSystemAccountTypeId;
    protected int mnFkSystemMoveClassId;
    protected int mnFkSystemMoveTypeId;
    protected int mnFkDiverseMoveTypeId;
    protected int mnFkCurrencyId;
    protected int mnFkPaymentTypeId;
    protected int mnFkModeOfPaymentTypeId;
    protected int mnFkValueTypeId;
    protected int mnFkOwnerBizPartnerId;
    protected int mnFkOwnerBranchId;
    protected int mnFkCashBizPartnerId_n;
    protected int mnFkCashBranchId_n;
    protected int mnFkCashCashId_n;
    protected int mnFkWarehouseBizPartnerId_n;
    protected int mnFkWarehouseBranchId_n;
    protected int mnFkWarehouseWarehouseId_n;
    protected int mnFkBizPartnerBizPartnerId_n;
    protected int mnFkBizPartnerBranchId_n;
    protected int mnFkDpsInvId_n;
    protected int mnFkDpsAdjId_n;
    protected int mnFkDfrId_n;
    protected int mnFkIogId_n;
    protected int mnFkIomId_n;
    protected int mnFkPusId_n;
    protected int mnFkItemId_n;
    protected int mnFkItemAuxId_n;
    protected int mnFkUnitId_n;
    protected int mnFkRecordYearId_n;
    protected int mnFkRecordRecordId_n;
    protected int mnFkBookkeepingYearId_n;
    protected int mnFkBookkeepingNumberId_n;
    protected int mnFkUserAvailableId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUserAvailable;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public DDbBookkeepingMove() {
        super(DModConsts.F_BKK);
        initRegistry();
    }

    public void setPkYearId(int n) { mnPkYearId = n; }
    public void setPkMoveId(int n) { mnPkMoveId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setSupporting(String s) { msSupporting = s; }
    public void setReference(String s) { msReference = s; }
    public void setText(String s) { msText = s; }
    public void setDebit(double d) { mdDebit = d; }
    public void setCredit(double d) { mdCredit = d; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setDebitCy(double d) { mdDebitCy = d; }
    public void setCreditCy(double d) { mdCreditCy = d; }
    public void setUnits(double d) { mdUnits = d; }
    public void setSortingPos(int n) { mnSortingPos = n; }
    public void setExchangeRateDifference(boolean b) { mbExchangeRateDifference = b; }
    public void setAvailable(boolean b) { mbAvailable = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkAccountId(int n) { mnFkAccountId = n; }
    public void setFkSystemAccountTypeId(int n) { mnFkSystemAccountTypeId = n; }
    public void setFkSystemMoveClassId(int n) { mnFkSystemMoveClassId = n; }
    public void setFkSystemMoveTypeId(int n) { mnFkSystemMoveTypeId = n; }
    public void setFkDiverseMoveTypeId(int n) { mnFkDiverseMoveTypeId = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setFkPaymentTypeId(int n) { mnFkPaymentTypeId = n; }
    public void setFkModeOfPaymentTypeId(int n) { mnFkModeOfPaymentTypeId = n; }
    public void setFkValueTypeId(int n) { mnFkValueTypeId = n; }
    public void setFkOwnerBizPartnerId(int n) { mnFkOwnerBizPartnerId = n; }
    public void setFkOwnerBranchId(int n) { mnFkOwnerBranchId = n; }
    public void setFkCashBizPartnerId_n(int n) { mnFkCashBizPartnerId_n = n; }
    public void setFkCashBranchId_n(int n) { mnFkCashBranchId_n = n; }
    public void setFkCashCashId_n(int n) { mnFkCashCashId_n = n; }
    public void setFkWarehouseBizPartnerId_n(int n) { mnFkWarehouseBizPartnerId_n = n; }
    public void setFkWarehouseBranchId_n(int n) { mnFkWarehouseBranchId_n = n; }
    public void setFkWarehouseWarehouseId_n(int n) { mnFkWarehouseWarehouseId_n = n; }
    public void setFkBizPartnerBizPartnerId_n(int n) { mnFkBizPartnerBizPartnerId_n = n; }
    public void setFkBizPartnerBranchId_n(int n) { mnFkBizPartnerBranchId_n = n; }
    public void setFkDpsInvId_n(int n) { mnFkDpsInvId_n = n; }
    public void setFkDpsAdjId_n(int n) { mnFkDpsAdjId_n = n; }
    public void setFkDfrId_n(int n) { mnFkDfrId_n = n; }
    public void setFkIogId_n(int n) { mnFkIogId_n = n; }
    public void setFkIomId_n(int n) { mnFkIomId_n = n; }
    public void setFkPusId_n(int n) { mnFkPusId_n = n; }
    public void setFkItemId_n(int n) { mnFkItemId_n = n; }
    public void setFkItemAuxId_n(int n) { mnFkItemAuxId_n = n; }
    public void setFkUnitId_n(int n) { mnFkUnitId_n = n; }
    public void setFkRecordYearId_n(int n) { mnFkRecordYearId_n = n; }
    public void setFkRecordRecordId_n(int n) { mnFkRecordRecordId_n = n; }
    public void setFkBookkeepingYearId_n(int n) { mnFkBookkeepingYearId_n = n; }
    public void setFkBookkeepingNumberId_n(int n) { mnFkBookkeepingNumberId_n = n; }
    public void setFkUserAvailableId(int n) { mnFkUserAvailableId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserAvailable(Date t) { mtTsUserAvailable = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkYearId() { return mnPkYearId; }
    public int getPkMoveId() { return mnPkMoveId; }
    public Date getDate() { return mtDate; }
    public String getSupporting() { return msSupporting; }
    public String getReference() { return msReference; }
    public String getText() { return msText; }
    public double getDebit() { return mdDebit; }
    public double getCredit() { return mdCredit; }
    public double getExchangeRate() { return mdExchangeRate; }
    public double getDebitCy() { return mdDebitCy; }
    public double getCreditCy() { return mdCreditCy; }
    public double getUnits() { return mdUnits; }
    public int getSortingPos() { return mnSortingPos; }
    public boolean isExchangeRateDifference() { return mbExchangeRateDifference; }
    public boolean isAvailable() { return mbAvailable; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkAccountId() { return mnFkAccountId; }
    public int getFkSystemAccountTypeId() { return mnFkSystemAccountTypeId; }
    public int getFkSystemMoveClassId() { return mnFkSystemMoveClassId; }
    public int getFkSystemMoveTypeId() { return mnFkSystemMoveTypeId; }
    public int getFkDiverseMoveTypeId() { return mnFkDiverseMoveTypeId; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public int getFkPaymentTypeId() { return mnFkPaymentTypeId; }
    public int getFkModeOfPaymentTypeId() { return mnFkModeOfPaymentTypeId; }
    public int getFkValueTypeId() { return mnFkValueTypeId; }
    public int getFkOwnerBizPartnerId() { return mnFkOwnerBizPartnerId; }
    public int getFkOwnerBranchId() { return mnFkOwnerBranchId; }
    public int getFkCashBizPartnerId_n() { return mnFkCashBizPartnerId_n; }
    public int getFkCashBranchId_n() { return mnFkCashBranchId_n; }
    public int getFkCashCashId_n() { return mnFkCashCashId_n; }
    public int getFkWarehouseBizPartnerId_n() { return mnFkWarehouseBizPartnerId_n; }
    public int getFkWarehouseBranchId_n() { return mnFkWarehouseBranchId_n; }
    public int getFkWarehouseWarehouseId_n() { return mnFkWarehouseWarehouseId_n; }
    public int getFkBizPartnerBizPartnerId_n() { return mnFkBizPartnerBizPartnerId_n; }
    public int getFkBizPartnerBranchId_n() { return mnFkBizPartnerBranchId_n; }
    public int getFkDpsInvId_n() { return mnFkDpsInvId_n; }
    public int getFkDpsAdjId_n() { return mnFkDpsAdjId_n; }
    public int getFkDfrId_n() { return mnFkDfrId_n; }
    public int getFkIogId_n() { return mnFkIogId_n; }
    public int getFkIomId_n() { return mnFkIomId_n; }
    public int getFkPusId_n() { return mnFkPusId_n; }
    public int getFkItemId_n() { return mnFkItemId_n; }
    public int getFkItemAuxId_n() { return mnFkItemAuxId_n; }
    public int getFkUnitId_n() { return mnFkUnitId_n; }
    public int getFkRecordYearId_n() { return mnFkRecordYearId_n; }
    public int getFkRecordRecordId_n() { return mnFkRecordRecordId_n; }
    public int getFkBookkeepingYearId_n() { return mnFkBookkeepingYearId_n; }
    public int getFkBookkeepingNumberId_n() { return mnFkBookkeepingNumberId_n; }
    public int getFkUserAvailableId() { return mnFkUserAvailableId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserAvailable() { return mtTsUserAvailable; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public int[] getSystemMoveTypeKey() { return new int[] { mnFkSystemMoveClassId, mnFkSystemMoveTypeId }; }
    public int[] getCompanyKey() { return new int[] { mnFkOwnerBizPartnerId }; }
    public int[] getCompanyBranchKey() { return new int[] { mnFkOwnerBizPartnerId, mnFkOwnerBranchId }; }
    public int[] getBranchCashKey_n() { return mnFkCashBizPartnerId_n == DLibConsts.UNDEFINED || mnFkCashBranchId_n == DLibConsts.UNDEFINED || mnFkCashCashId_n == DLibConsts.UNDEFINED ? null : new int[] { mnFkCashBizPartnerId_n, mnFkCashBranchId_n, mnFkCashCashId_n }; }
    public int[] getBranchWarehouseKey_n() { return mnFkWarehouseBizPartnerId_n == DLibConsts.UNDEFINED || mnFkWarehouseBranchId_n == DLibConsts.UNDEFINED || mnFkWarehouseWarehouseId_n == DLibConsts.UNDEFINED ? null : new int[] { mnFkWarehouseBizPartnerId_n, mnFkWarehouseBranchId_n, mnFkWarehouseWarehouseId_n }; }
    public int[] getBizPartnerKey_n() { return mnFkBizPartnerBizPartnerId_n == DLibConsts.UNDEFINED ? null : new int[] { mnFkBizPartnerBizPartnerId_n }; }
    public int[] getBizPartnerBranchKey_n() { return mnFkBizPartnerBizPartnerId_n == DLibConsts.UNDEFINED || mnFkBizPartnerBranchId_n == DLibConsts.UNDEFINED ? null : new int[] { mnFkBizPartnerBizPartnerId_n, mnFkBizPartnerBranchId_n }; }
    public int[] getBookkeepingNumberKey_n() { return mnFkBookkeepingYearId_n == DLibConsts.UNDEFINED || mnFkBookkeepingNumberId_n == DLibConsts.UNDEFINED ? null : new int[] { mnFkBookkeepingYearId_n, mnFkBookkeepingNumberId_n }; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkYearId = pk[0];
        mnPkMoveId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkYearId, mnPkMoveId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkYearId = 0;
        mnPkMoveId = 0;
        mtDate = null;
        msSupporting = "";
        msReference = "";
        msText = "";
        mdDebit = 0;
        mdCredit = 0;
        mdExchangeRate = 0;
        mdDebitCy = 0;
        mdCreditCy = 0;
        mdUnits = 0;
        mnSortingPos = 0;
        mbExchangeRateDifference = false;
        mbAvailable = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkAccountId = 0;
        mnFkSystemAccountTypeId = 0;
        mnFkSystemMoveClassId = 0;
        mnFkSystemMoveTypeId = 0;
        mnFkDiverseMoveTypeId = 0;
        mnFkCurrencyId = 0;
        mnFkPaymentTypeId = 0;
        mnFkModeOfPaymentTypeId = 0;
        mnFkValueTypeId = 0;
        mnFkOwnerBizPartnerId = 0;
        mnFkOwnerBranchId = 0;
        mnFkCashBizPartnerId_n = 0;
        mnFkCashBranchId_n = 0;
        mnFkCashCashId_n = 0;
        mnFkWarehouseBizPartnerId_n = 0;
        mnFkWarehouseBranchId_n = 0;
        mnFkWarehouseWarehouseId_n = 0;
        mnFkBizPartnerBizPartnerId_n = 0;
        mnFkBizPartnerBranchId_n = 0;
        mnFkDpsInvId_n = 0;
        mnFkDpsAdjId_n = 0;
        mnFkDfrId_n = 0;
        mnFkIogId_n = 0;
        mnFkIomId_n = 0;
        mnFkPusId_n = 0;
        mnFkItemId_n = 0;
        mnFkItemAuxId_n = 0;
        mnFkUnitId_n = 0;
        mnFkRecordYearId_n = 0;
        mnFkRecordRecordId_n = 0;
        mnFkBookkeepingYearId_n = 0;
        mnFkBookkeepingNumberId_n = 0;
        mnFkUserAvailableId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserAvailable = null;
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
                "id_mov = " + mnPkMoveId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_yer = " + pk[0] + " AND " +
                "id_mov = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkMoveId = 0;

        msSql = "SELECT COALESCE(MAX(id_mov), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_yer = " + mnPkYearId + " ";
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
            mnPkYearId = resultSet.getInt("id_yer");
            mnPkMoveId = resultSet.getInt("id_mov");
            mtDate = resultSet.getDate("dt");
            msSupporting = resultSet.getString("sup");
            msReference = resultSet.getString("ref");
            msText = resultSet.getString("txt");
            mdDebit = resultSet.getDouble("dbt");
            mdCredit = resultSet.getDouble("cdt");
            mdExchangeRate = resultSet.getDouble("exr");
            mdDebitCy = resultSet.getDouble("dbt_cy");
            mdCreditCy = resultSet.getDouble("cdt_cy");
            mdUnits = resultSet.getDouble("unt");
            mnSortingPos = resultSet.getInt("sort");
            mbExchangeRateDifference = resultSet.getBoolean("b_exr");
            mbAvailable = resultSet.getBoolean("b_avl");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkAccountId = resultSet.getInt("fk_acc");
            mnFkSystemAccountTypeId = resultSet.getInt("fk_sys_acc_tp");
            mnFkSystemMoveClassId = resultSet.getInt("fk_sys_mov_cl");
            mnFkSystemMoveTypeId = resultSet.getInt("fk_sys_mov_tp");
            mnFkDiverseMoveTypeId = resultSet.getInt("fk_div_mov_tp");
            mnFkCurrencyId = resultSet.getInt("fk_cur");
            mnFkPaymentTypeId = resultSet.getInt("fk_pay_tp");
            mnFkModeOfPaymentTypeId = resultSet.getInt("fk_mop_tp");
            mnFkValueTypeId = resultSet.getInt("fk_val_tp");
            mnFkOwnerBizPartnerId = resultSet.getInt("fk_own_bpr");
            mnFkOwnerBranchId = resultSet.getInt("fk_own_bra");
            mnFkCashBizPartnerId_n = resultSet.getInt("fk_csh_bpr_n");
            mnFkCashBranchId_n = resultSet.getInt("fk_csh_bra_n");
            mnFkCashCashId_n = resultSet.getInt("fk_csh_csh_n");
            mnFkWarehouseBizPartnerId_n = resultSet.getInt("fk_wah_bpr_n");
            mnFkWarehouseBranchId_n = resultSet.getInt("fk_wah_bra_n");
            mnFkWarehouseWarehouseId_n = resultSet.getInt("fk_wah_wah_n");
            mnFkBizPartnerBizPartnerId_n = resultSet.getInt("fk_bpr_bpr_n");
            mnFkBizPartnerBranchId_n = resultSet.getInt("fk_bpr_bra_n");
            mnFkDpsInvId_n = resultSet.getInt("fk_dps_inv_n");
            mnFkDpsAdjId_n = resultSet.getInt("fk_dps_adj_n");
            mnFkDfrId_n = resultSet.getInt("fk_dfr_n");
            mnFkIogId_n = resultSet.getInt("fk_iog_n");
            mnFkIomId_n = resultSet.getInt("fk_iom_n");
            mnFkPusId_n = resultSet.getInt("fk_pus_n");
            mnFkItemId_n = resultSet.getInt("fk_itm_n");
            mnFkItemAuxId_n = resultSet.getInt("fk_itm_aux_n");
            mnFkUnitId_n = resultSet.getInt("fk_unt_n");
            mnFkRecordYearId_n = resultSet.getInt("fk_rec_yer_n");
            mnFkRecordRecordId_n = resultSet.getInt("fk_rec_rec_n");
            mnFkBookkeepingYearId_n = resultSet.getInt("fk_bkk_yer_n");
            mnFkBookkeepingNumberId_n = resultSet.getInt("fk_bkk_num_n");
            mnFkUserAvailableId = resultSet.getInt("fk_usr_avl");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserAvailable = resultSet.getTimestamp("ts_usr_avl");
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
                    mnPkMoveId + ", " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "'" + msSupporting + "', " +
                    "'" + msReference + "', " +
                    "'" + msText + "', " +
                    mdDebit + ", " +
                    mdCredit + ", " +
                    mdExchangeRate + ", " +
                    mdDebitCy + ", " +
                    mdCreditCy + ", " +
                    mdUnits + ", " +
                    mnSortingPos + ", " +
                    (mbExchangeRateDifference ? 1 : 0) + ", " +
                    (mbAvailable ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkAccountId + ", " +
                    mnFkSystemAccountTypeId + ", " +
                    mnFkSystemMoveClassId + ", " +
                    mnFkSystemMoveTypeId + ", " +
                    mnFkDiverseMoveTypeId + ", " +
                    mnFkCurrencyId + ", " +
                    mnFkPaymentTypeId + ", " +
                    mnFkModeOfPaymentTypeId + ", " +
                    mnFkValueTypeId + ", " +
                    mnFkOwnerBizPartnerId + ", " +
                    mnFkOwnerBranchId + ", " +
                    (mnFkCashBizPartnerId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashBizPartnerId_n) + ", " +
                    (mnFkCashBranchId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashBranchId_n) + ", " +
                    (mnFkCashCashId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashCashId_n) + ", " +
                    (mnFkWarehouseBizPartnerId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkWarehouseBizPartnerId_n) + ", " +
                    (mnFkWarehouseBranchId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkWarehouseBranchId_n) + ", " +
                    (mnFkWarehouseWarehouseId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkWarehouseWarehouseId_n) + ", " +
                    (mnFkBizPartnerBizPartnerId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBizPartnerBizPartnerId_n) + ", " +
                    (mnFkBizPartnerBranchId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBizPartnerBranchId_n) + ", " +
                    (mnFkDpsInvId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsInvId_n) + ", " +
                    (mnFkDpsAdjId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsAdjId_n) + ", " +
                    (mnFkDfrId_n == DLibConsts.UNDEFINED ? "NULL" : mnFkDfrId_n) + ", " +
                    (mnFkIogId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkIogId_n) + ", " +
                    (mnFkIomId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkIomId_n) + ", " +
                    (mnFkPusId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkPusId_n) + ", " +
                    (mnFkItemId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemId_n) + ", " +
                    (mnFkItemAuxId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemAuxId_n) + ", " +
                    (mnFkUnitId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkUnitId_n) + ", " +
                    (mnFkRecordYearId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkRecordYearId_n) + ", " +
                    (mnFkRecordRecordId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkRecordRecordId_n) + ", " +
                    (mnFkBookkeepingYearId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBookkeepingYearId_n) + ", " +
                    (mnFkBookkeepingNumberId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBookkeepingNumberId_n) + ", " +
                    mnFkUserAvailableId + ", " +
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
                    "id_mov = " + mnPkMoveId + ", " +
                    */
                    "dt = '" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "sup = '" + msSupporting + "', " +
                    "ref = '" + msReference + "', " +
                    "txt = '" + msText + "', " +
                    "dbt = " + mdDebit + ", " +
                    "cdt = " + mdCredit + ", " +
                    "exr = " + mdExchangeRate + ", " +
                    "dbt_cy = " + mdDebitCy + ", " +
                    "cdt_cy = " + mdCreditCy + ", " +
                    "unt = " + mdUnits + ", " +
                    "sort = " + mnSortingPos + ", " +
                    "b_exr = " + (mbExchangeRateDifference ? 1 : 0) + ", " +
                    "b_avl = " + (mbAvailable ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_acc = " + mnFkAccountId + ", " +
                    "fk_sys_acc_tp = " + mnFkSystemAccountTypeId + ", " +
                    "fk_sys_mov_cl = " + mnFkSystemMoveClassId + ", " +
                    "fk_sys_mov_tp = " + mnFkSystemMoveTypeId + ", " +
                    "fk_div_mov_tp = " + mnFkDiverseMoveTypeId + ", " +
                    "fk_cur = " + mnFkCurrencyId + ", " +
                    "fk_pay_tp = " + mnFkPaymentTypeId + ", " +
                    "fk_mop_tp = " + mnFkModeOfPaymentTypeId + ", " +
                    "fk_val_tp = " + mnFkValueTypeId + ", " +
                    "fk_own_bpr = " + mnFkOwnerBizPartnerId + ", " +
                    "fk_own_bra = " + mnFkOwnerBranchId + ", " +
                    "fk_csh_bpr_n = " + (mnFkCashBizPartnerId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashBizPartnerId_n) + ", " +
                    "fk_csh_bra_n = " + (mnFkCashBranchId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashBranchId_n) + ", " +
                    "fk_csh_csh_n = " + (mnFkCashCashId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCashCashId_n) + ", " +
                    "fk_wah_bpr_n = " + (mnFkWarehouseBizPartnerId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkWarehouseBizPartnerId_n) + ", " +
                    "fk_wah_bra_n = " + (mnFkWarehouseBranchId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkWarehouseBranchId_n) + ", " +
                    "fk_wah_wah_n = " + (mnFkWarehouseWarehouseId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkWarehouseWarehouseId_n) + ", " +
                    "fk_bpr_bpr_n = " + (mnFkBizPartnerBizPartnerId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBizPartnerBizPartnerId_n) + ", " +
                    "fk_bpr_bra_n = " + (mnFkBizPartnerBranchId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBizPartnerBranchId_n) + ", " +
                    "fk_dps_inv_n = " + (mnFkDpsInvId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsInvId_n) + ", " +
                    "fk_dps_adj_n = " + (mnFkDpsAdjId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsAdjId_n) + ", " +
                    "fk_dfr_n = " + (mnFkDfrId_n == DLibConsts.UNDEFINED ? "NULL" : mnFkDfrId_n) + ", " +
                    "fk_iog_n = " + (mnFkIogId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkIogId_n) + ", " +
                    "fk_iom_n = " + (mnFkIomId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkIomId_n) + ", " +
                    "fk_pus_n = " + (mnFkPusId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkPusId_n) + ", " +
                    "fk_itm_n = " + (mnFkItemId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemId_n) + ", " +
                    "fk_itm_aux_n = " + (mnFkItemAuxId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkItemAuxId_n) + ", " +
                    "fk_unt_n = " + (mnFkUnitId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkUnitId_n) + ", " +
                    "fk_rec_yer_n = " + (mnFkRecordYearId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkRecordYearId_n) + ", " +
                    "fk_rec_rec_n = " + (mnFkRecordRecordId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkRecordRecordId_n) + ", " +
                    "fk_bkk_yer_n = " + (mnFkBookkeepingYearId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBookkeepingYearId_n) + ", " +
                    "fk_bkk_num_n = " + (mnFkBookkeepingNumberId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBookkeepingNumberId_n) + ", " +
                    "fk_usr_avl = " + mnFkUserAvailableId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    "ts_usr_avl = " + "NOW()" + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbBookkeepingMove clone() throws CloneNotSupportedException {
        DDbBookkeepingMove registry = new DDbBookkeepingMove();

        registry.setPkYearId(this.getPkYearId());
        registry.setPkMoveId(this.getPkMoveId());
        registry.setDate(this.getDate());
        registry.setSupporting(this.getSupporting());
        registry.setReference(this.getReference());
        registry.setText(this.getText());
        registry.setDebit(this.getDebit());
        registry.setCredit(this.getCredit());
        registry.setExchangeRate(this.getExchangeRate());
        registry.setDebitCy(this.getDebitCy());
        registry.setCreditCy(this.getCreditCy());
        registry.setUnits(this.getUnits());
        registry.setSortingPos(this.getSortingPos());
        registry.setExchangeRateDifference(this.isExchangeRateDifference());
        registry.setAvailable(this.isAvailable());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkAccountId(this.getFkAccountId());
        registry.setFkSystemAccountTypeId(this.getFkSystemAccountTypeId());
        registry.setFkSystemMoveClassId(this.getFkSystemMoveClassId());
        registry.setFkSystemMoveTypeId(this.getFkSystemMoveTypeId());
        registry.setFkDiverseMoveTypeId(this.getFkDiverseMoveTypeId());
        registry.setFkCurrencyId(this.getFkCurrencyId());
        registry.setFkPaymentTypeId(this.getFkPaymentTypeId());
        registry.setFkModeOfPaymentTypeId(this.getFkModeOfPaymentTypeId());
        registry.setFkValueTypeId(this.getFkValueTypeId());
        registry.setFkOwnerBizPartnerId(this.getFkOwnerBizPartnerId());
        registry.setFkOwnerBranchId(this.getFkOwnerBranchId());
        registry.setFkCashBizPartnerId_n(this.getFkCashBizPartnerId_n());
        registry.setFkCashBranchId_n(this.getFkCashBranchId_n());
        registry.setFkCashCashId_n(this.getFkCashCashId_n());
        registry.setFkWarehouseBizPartnerId_n(this.getFkWarehouseBizPartnerId_n());
        registry.setFkWarehouseBranchId_n(this.getFkWarehouseBranchId_n());
        registry.setFkWarehouseWarehouseId_n(this.getFkWarehouseWarehouseId_n());
        registry.setFkBizPartnerBizPartnerId_n(this.getFkBizPartnerBizPartnerId_n());
        registry.setFkBizPartnerBranchId_n(this.getFkBizPartnerBranchId_n());
        registry.setFkDpsInvId_n(this.getFkDpsInvId_n());
        registry.setFkDpsAdjId_n(this.getFkDpsAdjId_n());
        registry.setFkDfrId_n(this.getFkDfrId_n());
        registry.setFkIogId_n(this.getFkIogId_n());
        registry.setFkIomId_n(this.getFkIomId_n());
        registry.setFkPusId_n(this.getFkPusId_n());
        registry.setFkItemId_n(this.getFkItemId_n());
        registry.setFkItemAuxId_n(this.getFkItemAuxId_n());
        registry.setFkUnitId_n(this.getFkUnitId_n());
        registry.setFkRecordYearId_n(this.getFkRecordYearId_n());
        registry.setFkRecordRecordId_n(this.getFkRecordRecordId_n());
        registry.setFkBookkeepingYearId_n(this.getFkBookkeepingYearId_n());
        registry.setFkBookkeepingNumberId_n(this.getFkBookkeepingNumberId_n());
        registry.setFkUserAvailableId(this.getFkUserAvailableId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserAvailable(this.getTsUserAvailable());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    public DDbBookkeepingMove createComplement(DGuiSession session) throws Exception {
        int bizPartnerClass = DLibConsts.UNDEFINED;
        int[] sysMoveTypeKey = getSystemMoveTypeKey();
        DDbAbpBizPartner abpBizPartner = null;
        DDbAbpBranchCash abpBranchCash = null;
        DDbBookkeepingMove complement = null;

        complement = clone();
        complement.setDebit(mdCredit);
        complement.setCredit(mdDebit);
        complement.setDebitCy(mdCreditCy);
        complement.setCreditCy(mdDebitCy);
        complement.setFkSystemAccountTypeId(DModSysConsts.FS_SYS_ACC_TP_NA);

        if (DFinUtils.isSysMoveTypeForBranchCashMove(sysMoveTypeKey)) {
            if (DFinUtils.isSysMoveTypeForBranchCashBizPartnerMove(sysMoveTypeKey)) {
                bizPartnerClass = DFinUtils.getBizPartnerClassForSysMoveType(sysMoveTypeKey);
                abpBizPartner = DFinUtils.readAbpBizPartner(session, getBizPartnerKey_n(), bizPartnerClass);

                complement.setFkSystemAccountTypeId(DFinUtils.getSysAccountTypeForBizPartnerClass(bizPartnerClass));

                if (DFinUtils.isSysMoveTypeForBranchCashPayment(sysMoveTypeKey)) {
                    complement.setFkAccountId(abpBizPartner.getFkAccountBizPartnerId());
                }
                else {
                    complement.setFkAccountId(abpBizPartner.getFkAccountBizPartnerAdvanceId());
                }
            }
            else {
                abpBranchCash = DFinUtils.readAbpBranchCash(session, getBranchCashKey_n());

                if (DLibUtils.compareKeys(sysMoveTypeKey, DModSysConsts.FS_SYS_MOV_TP_MI_EQY)) {
                    complement.setFkAccountId(abpBranchCash.getFkAccountInOwnersEquityId());
                }
                else if (DLibUtils.compareKeys(sysMoveTypeKey, DModSysConsts.FS_SYS_MOV_TP_MI_EXR)) {
                    complement.setFkAccountId(abpBranchCash.getFkAccountInExchangeRateId());
                }
                else if (DLibUtils.compareKeys(sysMoveTypeKey, DModSysConsts.FS_SYS_MOV_TP_MI_ADJ)) {
                    complement.setFkAccountId(abpBranchCash.getFkAccountInAdjustmentId());
                }
                else if (DLibUtils.compareKeys(sysMoveTypeKey, DModSysConsts.FS_SYS_MOV_TP_MO_EQY)) {
                    complement.setFkAccountId(abpBranchCash.getFkAccountOutOwnersEquityId());
                }
                else if (DLibUtils.compareKeys(sysMoveTypeKey, DModSysConsts.FS_SYS_MOV_TP_MO_EXR)) {
                    complement.setFkAccountId(abpBranchCash.getFkAccountOutExchangeRateId());
                }
                else if (DLibUtils.compareKeys(sysMoveTypeKey, DModSysConsts.FS_SYS_MOV_TP_MO_ADJ)) {
                    complement.setFkAccountId(abpBranchCash.getFkAccountOutAdjustmentId());
                }
                else {
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
            }
        }
        else if (DFinUtils.isSysMoveTypeForBizPartnerDoc(sysMoveTypeKey)) {
            bizPartnerClass = DFinUtils.getBizPartnerClassForSysMoveType(sysMoveTypeKey);
            abpBizPartner = DFinUtils.readAbpBizPartner(session, getBizPartnerKey_n(), bizPartnerClass);

            if (DLibUtils.compareKeys(sysMoveTypeKey, DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_DEC_ADJ)) {
                complement.setFkAccountId(abpBizPartner.getFkAccountDecDocAdjustmentId());
            }
            else if (DLibUtils.compareKeys(sysMoveTypeKey, DModSysConsts.FS_SYS_MOV_TP_VEN_DOC_INC_ADJ)) {
                complement.setFkAccountId(abpBizPartner.getFkAccountIncDocAdjustmentId());
            }
            else if (DLibUtils.compareKeys(sysMoveTypeKey, DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_DEC_ADJ)) {
                complement.setFkAccountId(abpBizPartner.getFkAccountDecDocAdjustmentId());
            }
            else if (DLibUtils.compareKeys(sysMoveTypeKey, DModSysConsts.FS_SYS_MOV_TP_CUS_DOC_INC_ADJ)) {
                complement.setFkAccountId(abpBizPartner.getFkAccountIncDocAdjustmentId());
            }
            else {
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
        else {
            throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return complement;
    }
}
