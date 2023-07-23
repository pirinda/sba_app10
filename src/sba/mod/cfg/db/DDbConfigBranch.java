/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.cfg.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiConfigBranch;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbConfigBranch extends DDbRegistryUser implements DGuiConfigBranch {

    protected int mnPkBizPartnerId;
    protected int mnPkBranchId;
    protected String msDfrDirectory;
    protected String msDfrName;
    protected String msDfrPassword;
    protected String msDfrCrpSeries;
    protected boolean mbDpsNumberAutomaticByUser;
    protected boolean mbDpsPrintingDialog;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkPublicBizPartnerPurchaseId;
    protected int mnFkPublicBizPartnerSaleId;
    protected int mnFkBizPartnerDpsSignatureId_n;
    protected int mnFkTaxRegionId;
    protected int mnFkPosAfterSaleActionTypeId;
    protected int mnFkPosAfterSalePrintingTypeId;
    protected int mnFkXmlSignatureProviderId;
    protected int mnFkCertificateId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    public DDbConfigBranch() {
        super(DModConsts.CU_CFG_BRA);
        initRegistry();
    }

    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setPkBranchId(int n) { mnPkBranchId = n; }
    public void setDfrDirectory(String s) { msDfrDirectory = s; }
    public void setDfrName(String s) { msDfrName = s; }
    public void setDfrPassword(String s) { msDfrPassword = s; }
    public void setDfrCrpSeries(String s) { msDfrCrpSeries = s; }
    public void setDpsNumberAutomaticByUser(boolean b) { mbDpsNumberAutomaticByUser = b; }
    public void setDpsPrintingDialog(boolean b) { mbDpsPrintingDialog = b; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkPublicBizPartnerPurchaseId(int n) { mnFkPublicBizPartnerPurchaseId = n; }
    public void setFkPublicBizPartnerSaleId(int n) { mnFkPublicBizPartnerSaleId = n; }
    public void setFkBizPartnerDpsSignatureId_n(int n) { mnFkBizPartnerDpsSignatureId_n = n; }
    public void setFkTaxRegionId(int n) { mnFkTaxRegionId = n; }
    public void setFkPosAfterSaleActionTypeId(int n) { mnFkPosAfterSaleActionTypeId = n; }
    public void setFkPosAfterSalePrintingTypeId(int n) { mnFkPosAfterSalePrintingTypeId = n; }
    public void setFkXmlSignatureProviderId(int n) { mnFkXmlSignatureProviderId = n; }
    public void setFkCertificateId_n(int n) { mnFkCertificateId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public int getPkBranchId() { return mnPkBranchId; }
    public String getDfrDirectory() { return msDfrDirectory; }
    public String getDfrName() { return msDfrName; }
    public String getDfrPassword() { return msDfrPassword; }
    public String getDfrCrpSeries() { return msDfrCrpSeries; }
    public boolean isDpsNumberAutomaticByUser() { return mbDpsNumberAutomaticByUser; }
    public boolean isDpsPrintingDialog() { return mbDpsPrintingDialog; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkPublicBizPartnerPurchaseId() { return mnFkPublicBizPartnerPurchaseId; }
    public int getFkPublicBizPartnerSaleId() { return mnFkPublicBizPartnerSaleId; }
    public int getFkBizPartnerDpsSignatureId_n() { return mnFkBizPartnerDpsSignatureId_n; }
    public int getFkTaxRegionId() { return mnFkTaxRegionId; }
    public int getFkPosAfterSaleActionTypeId() { return mnFkPosAfterSaleActionTypeId; }
    public int getFkPosAfterSalePrintingTypeId() { return mnFkPosAfterSalePrintingTypeId; }
    public int getFkXmlSignatureProviderId() { return mnFkXmlSignatureProviderId; }
    public int getFkCertificateId_n() { return mnFkCertificateId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBizPartnerId = pk[0];
        mnPkBranchId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBizPartnerId, mnPkBranchId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBizPartnerId = 0;
        mnPkBranchId = 0;
        msDfrDirectory = "";
        msDfrName = "";
        msDfrPassword = "";
        msDfrCrpSeries = "";
        mbDpsNumberAutomaticByUser = false;
        mbDpsPrintingDialog = false;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkPublicBizPartnerPurchaseId = 0;
        mnFkPublicBizPartnerSaleId = 0;
        mnFkBizPartnerDpsSignatureId_n = 0;
        mnFkTaxRegionId = 0;
        mnFkPosAfterSaleActionTypeId = 0;
        mnFkPosAfterSalePrintingTypeId = 0;
        mnFkXmlSignatureProviderId = 0;
        mnFkCertificateId_n = 0;
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
        return "WHERE id_bpr = " + mnPkBizPartnerId + " AND id_bra = " + mnPkBranchId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bpr = " + pk[0] + " AND id_bra = " + pk[1] + " ";
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
            mnPkBranchId = resultSet.getInt("id_bra");
            msDfrDirectory = resultSet.getString("dfr_dir");
            msDfrName = resultSet.getString("dfr_name");
            msDfrPassword = resultSet.getString("dfr_pswd");
            msDfrCrpSeries = resultSet.getString("dfr_crp_ser");
            mbDpsNumberAutomaticByUser = resultSet.getBoolean("b_dps_num_aut_usr");
            mbDpsPrintingDialog = resultSet.getBoolean("b_dps_prt_dlg");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkPublicBizPartnerPurchaseId = resultSet.getInt("fk_pub_bpr_pur");
            mnFkPublicBizPartnerSaleId = resultSet.getInt("fk_pub_bpr_sal");
            mnFkBizPartnerDpsSignatureId_n = resultSet.getInt("fk_bpr_dps_sig_n");
            mnFkTaxRegionId = resultSet.getInt("fk_tax_reg");
            mnFkPosAfterSaleActionTypeId = resultSet.getInt("fk_pos_asa_tp");
            mnFkPosAfterSalePrintingTypeId = resultSet.getInt("fk_pos_asp_tp");
            mnFkXmlSignatureProviderId = resultSet.getInt("fk_xsp");
            mnFkCertificateId_n = resultSet.getInt("fk_cer_n");
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
            mbUpdatable = true;
            mbDisableable = false;
            mbDeletable = false;
            
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            mbDisabled = false;
            mbDeleted = false;
            //mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkBizPartnerId + ", " +
                    mnPkBranchId + ", " +
                    "'" + msDfrDirectory + "', " +
                    "'" + msDfrName + "', " +
                    "'" + msDfrPassword + "', " +
                    "'" + msDfrCrpSeries + "', " + 
                    (mbDpsNumberAutomaticByUser ? 1 : 0) + ", " +
                    (mbDpsPrintingDialog ? 1 : 0) + ", " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkPublicBizPartnerPurchaseId + ", " +
                    mnFkPublicBizPartnerSaleId + ", " +
                    (mnFkBizPartnerDpsSignatureId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBizPartnerDpsSignatureId_n) + ", " + 
                    mnFkTaxRegionId + ", " +
                    mnFkPosAfterSaleActionTypeId + ", " +
                    mnFkPosAfterSalePrintingTypeId + ", " +
                    mnFkXmlSignatureProviderId + ", " + 
                    (mnFkCertificateId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCertificateId_n) + ", " +
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
                    "id_bra = " + mnPkBranchId + ", " +
                    */
                    "dfr_dir = '" + msDfrDirectory + "', " +
                    "dfr_name = '" + msDfrName + "', " +
                    "dfr_pswd = '" + msDfrPassword + "', " +
                    "dfr_crp_ser = '" + msDfrCrpSeries + "', " +
                    "b_dps_num_aut_usr = " + (mbDpsNumberAutomaticByUser ? 1 : 0) + ", " +
                    "b_dps_prt_dlg = " + (mbDpsPrintingDialog ? 1 : 0) + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_pub_bpr_pur = " + mnFkPublicBizPartnerPurchaseId + ", " +
                    "fk_pub_bpr_sal = " + mnFkPublicBizPartnerSaleId + ", " +
                    "fk_bpr_dps_sig_n = " + (mnFkBizPartnerDpsSignatureId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBizPartnerDpsSignatureId_n) + ", " +
                    "fk_tax_reg = " + mnFkTaxRegionId + ", " +
                    "fk_pos_asa_tp = " + mnFkPosAfterSaleActionTypeId + ", " +
                    "fk_pos_asp_tp = " + mnFkPosAfterSalePrintingTypeId + ", " +
                    "fk_xsp = " + mnFkXmlSignatureProviderId + ", " +
                    "fk_cer_n = " + (mnFkCertificateId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkCertificateId_n) + ", " +
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
    public DDbConfigBranch clone() throws CloneNotSupportedException {
        DDbConfigBranch registry = new DDbConfigBranch();

        registry.setPkBizPartnerId(this.getPkBizPartnerId());
        registry.setPkBranchId(this.getPkBranchId());
        registry.setDfrDirectory(this.getDfrDirectory());
        registry.setDfrName(this.getDfrName());
        registry.setDfrPassword(this.getDfrPassword());
        registry.setDfrCrpSeries(this.getDfrCrpSeries());
        registry.setDpsNumberAutomaticByUser(this.isDpsNumberAutomaticByUser());
        registry.setDpsPrintingDialog(this.isDpsPrintingDialog());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkPublicBizPartnerPurchaseId(this.getFkPublicBizPartnerPurchaseId());
        registry.setFkPublicBizPartnerSaleId(this.getFkPublicBizPartnerSaleId());
        registry.setFkBizPartnerDpsSignatureId_n(this.getFkBizPartnerDpsSignatureId_n());
        registry.setFkTaxRegionId(this.getFkTaxRegionId());
        registry.setFkPosAfterSaleActionTypeId(this.getFkPosAfterSaleActionTypeId());
        registry.setFkPosAfterSalePrintingTypeId(this.getFkPosAfterSalePrintingTypeId());
        registry.setFkXmlSignatureProviderId(this.getFkXmlSignatureProviderId());
        registry.setFkCertificateId_n(this.getFkCertificateId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public int getCompanyId() {
        return getPkBizPartnerId();
    }

    @Override
    public int getBranchId() {
        return getPkBranchId();
    }
}
