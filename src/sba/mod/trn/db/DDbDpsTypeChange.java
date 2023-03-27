/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import cfd.ver40.DCfdi40Catalogs;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sba.gui.DGuiClientApp;
import sba.gui.cat.DXmlCatalog;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.bpr.db.DDbBizPartner;
import sba.mod.bpr.db.DDbBizPartnerConfig;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.cfg.db.DDbLock;
import sba.mod.itm.db.DDbItem;
import sba.mod.itm.db.DDbUnit;

/**
 *
 * @author Sergio Flores
 */
public class DDbDpsTypeChange extends DDbRegistryUser {

    protected int mnPkDpsId;
    protected int mnPkChangeId;
    protected String msNewSeries;
    protected int mnNewNumber;
    protected String msNewOrder;
    protected Date mtNewDate;
    protected String msOldSeries;
    protected int mnOldNumber;
    protected String msOldOrder;
    protected Date mtOldDate;
    /*
    protected boolean mbDeleted;
    */
    protected int mnFkNewDpsCategoryId;
    protected int mnFkNewDpsClassId;
    protected int mnFkNewDpsTypeId;
    protected int mnFkOldDpsCategoryId;
    protected int mnFkOldDpsClassId;
    protected int mnFkOldDpsTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    private int mnAuxXmlTypeId;
    protected DDbLock moAuxDpsLock;

    public DDbDpsTypeChange() {
        super(DModConsts.T_DPS_CHG);
        initRegistry();
    }

    /*
     * Private methods
     */

    /*
     * Public methods
     */

    public void setPkDpsId(int n) { mnPkDpsId = n; }
    public void setPkChangeId(int n) { mnPkChangeId = n; }
    public void setNewSeries(String s) { msNewSeries = s; }
    public void setNewNumber(int n) { mnNewNumber = n; }
    public void setNewOrder(String s) { msNewOrder = s; }
    public void setNewDate(Date t) { mtNewDate = t; }
    public void setOldSeries(String s) { msOldSeries = s; }
    public void setOldNumber(int n) { mnOldNumber = n; }
    public void setOldOrder(String s) { msOldOrder = s; }
    public void setOldDate(Date t) { mtOldDate = t; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkNewDpsCategoryId(int n) { mnFkNewDpsCategoryId = n; }
    public void setFkNewDpsClassId(int n) { mnFkNewDpsClassId = n; }
    public void setFkNewDpsTypeId(int n) { mnFkNewDpsTypeId = n; }
    public void setFkOldDpsCategoryId(int n) { mnFkOldDpsCategoryId = n; }
    public void setFkOldDpsClassId(int n) { mnFkOldDpsClassId = n; }
    public void setFkOldDpsTypeId(int n) { mnFkOldDpsTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkDpsId() { return mnPkDpsId; }
    public int getPkChangeId() { return mnPkChangeId; }
    public String getNewSeries() { return msNewSeries; }
    public int getNewNumber() { return mnNewNumber; }
    public String getNewOrder() { return msNewOrder; }
    public Date getNewDate() { return mtNewDate; }
    public String getOldSeries() { return msOldSeries; }
    public int getOldNumber() { return mnOldNumber; }
    public String getOldOrder() { return msOldOrder; }
    public Date getOldDate() { return mtOldDate; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkNewDpsCategoryId() { return mnFkNewDpsCategoryId; }
    public int getFkNewDpsClassId() { return mnFkNewDpsClassId; }
    public int getFkNewDpsTypeId() { return mnFkNewDpsTypeId; }
    public int getFkOldDpsCategoryId() { return mnFkOldDpsCategoryId; }
    public int getFkOldDpsClassId() { return mnFkOldDpsClassId; }
    public int getFkOldDpsTypeId() { return mnFkOldDpsTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setAuxXmlTypeId(int n) { mnAuxXmlTypeId = n; }
    public void setAuxDpsLock(DDbLock o) { moAuxDpsLock = o; }

    public int getAuxXmlTypeId() { return mnAuxXmlTypeId; }
    public DDbLock getAuxDpsLock() { return moAuxDpsLock; }

    public int[] getNewDpsCategoryKey() { return new int[] { mnFkNewDpsCategoryId }; }
    public int[] getNewDpsClassKey() { return new int[] { mnFkNewDpsCategoryId, mnFkNewDpsClassId }; }
    public int[] getNewDpsTypeKey() { return new int[] { mnFkNewDpsCategoryId, mnFkNewDpsClassId, mnFkNewDpsTypeId }; }
    public String getNewDpsNumber() { return DTrnUtils.composeDpsNumber(msNewSeries, mnNewNumber); }
    public String getNewDpsReference() { return DTrnUtils.composeDpsReference(msNewSeries, mnNewNumber); }
    public int[] getOldDpsCategoryKey() { return new int[] { mnFkOldDpsCategoryId }; }
    public int[] getOldDpsClassKey() { return new int[] { mnFkOldDpsCategoryId, mnFkOldDpsClassId }; }
    public int[] getOldDpsTypeKey() { return new int[] { mnFkOldDpsCategoryId, mnFkOldDpsClassId, mnFkOldDpsTypeId }; }
    public String getOldDpsNumber() { return DTrnUtils.composeDpsNumber(msOldSeries, mnOldNumber); }
    public String getOldDpsReference() { return DTrnUtils.composeDpsReference(msOldSeries, mnOldNumber); }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDpsId = pk[0];
        mnPkChangeId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDpsId, mnPkChangeId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkDpsId = 0;
        mnPkChangeId = 0;
        msNewSeries = "";
        mnNewNumber = 0;
        msNewOrder = "";
        mtNewDate = null;
        msOldSeries = "";
        mnOldNumber = 0;
        msOldOrder = "";
        mtOldDate = null;
        mbDeleted = false;
        mnFkNewDpsCategoryId = 0;
        mnFkNewDpsClassId = 0;
        mnFkNewDpsTypeId = 0;
        mnFkOldDpsCategoryId = 0;
        mnFkOldDpsClassId = 0;
        mnFkOldDpsTypeId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mnAuxXmlTypeId = 0;
        moAuxDpsLock = null;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_dps = " + mnPkDpsId + " AND " +
                "id_chg = " + mnPkChangeId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_dps = " + pk[0] + " AND " +
                "id_chg = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkChangeId = 0;

        msSql = "SELECT COALESCE(MAX(id_chg), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_dps = " + mnPkDpsId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkChangeId = resultSet.getInt(1);
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
            mnPkChangeId = resultSet.getInt("id_chg");
            msNewSeries = resultSet.getString("new_ser");
            mnNewNumber = resultSet.getInt("new_num");
            msNewOrder = resultSet.getString("new_ord");
            mtNewDate = resultSet.getDate("new_dt");
            msOldSeries = resultSet.getString("old_ser");
            mnOldNumber = resultSet.getInt("old_num");
            msOldOrder = resultSet.getString("old_ord");
            mtOldDate = resultSet.getDate("old_dt");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkNewDpsCategoryId = resultSet.getInt("fk_new_dps_ct");
            mnFkNewDpsClassId = resultSet.getInt("fk_new_dps_cl");
            mnFkNewDpsTypeId = resultSet.getInt("fk_new_dps_tp");
            mnFkOldDpsCategoryId = resultSet.getInt("fk_old_dps_ct");
            mnFkOldDpsClassId = resultSet.getInt("fk_old_dps_cl");
            mnFkOldDpsTypeId = resultSet.getInt("fk_old_dps_tp");
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

        msSql = "UPDATE " + DModConsts.TablesMap.get(DModConsts.T_DPS_CHG) + " SET " +
                "b_del = 1, " +
                "fk_usr_upd = " + session.getUser().getPkUserId() + ", " +
                "ts_usr_upd = " + "NOW()" + " " +
                "WHERE id_dps = " + mnPkDpsId + " AND b_del = 0 ";
        session.getStatement().execute(msSql);

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkDpsId + ", " +
                    mnPkChangeId + ", " +
                    "'" + msNewSeries + "', " +
                    mnNewNumber + ", " +
                    "'" + msNewOrder + "', " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtNewDate) + "', " +
                    "'" + msOldSeries + "', " +
                    mnOldNumber + ", " +
                    "'" + msOldOrder + "', " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtOldDate) + "', " +
                    (mbDeleted ? 1 : 0) + ", " +
                    mnFkNewDpsCategoryId + ", " +
                    mnFkNewDpsClassId + ", " +
                    mnFkNewDpsTypeId + ", " +
                    mnFkOldDpsCategoryId + ", " +
                    mnFkOldDpsClassId + ", " +
                    mnFkOldDpsTypeId + ", " +
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
                    "id_chg = " + mnPkChangeId + ", " +
                    */
                    "new_ser = '" + msNewSeries + "', " +
                    "new_num = " + mnNewNumber + ", " +
                    "new_ord = '" + msNewOrder + "', " +
                    "new_dt = '" + DLibUtils.DbmsDateFormatDate.format(mtNewDate) + "', " +
                    "old_ser = '" + msOldSeries + "', " +
                    "old_num = " + mnOldNumber + ", " +
                    "old_ord = '" + msOldOrder + "', " +
                    "old_dt = '" + DLibUtils.DbmsDateFormatDate.format(mtOldDate) + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_new_dps_ct = " + mnFkNewDpsCategoryId + ", " +
                    "fk_new_dps_cl = " + mnFkNewDpsClassId + ", " +
                    "fk_new_dps_tp = " + mnFkNewDpsTypeId + ", " +
                    "fk_old_dps_ct = " + mnFkOldDpsCategoryId + ", " +
                    "fk_old_dps_cl = " + mnFkOldDpsClassId + ", " +
                    "fk_old_dps_tp = " + mnFkOldDpsTypeId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell document:

        dps = (DDbDps) session.readRegistry(DModConsts.T_DPS, new int[] { mnPkDpsId });
        dps.setSeries(msNewSeries);
        dps.setNumber(mnNewNumber);
        dps.setOrder(msNewOrder);
        dps.setDate(mtNewDate);
        dps.setFkDpsCategoryId(mnFkNewDpsCategoryId);
        dps.setFkDpsClassId(mnFkNewDpsClassId);
        dps.setFkDpsTypeId(mnFkNewDpsTypeId);
        dps.setAuxLock(moAuxDpsLock);

        if (mnAuxXmlTypeId != DModSysConsts.TS_XML_TP_NA) {
            // prepare CFDI:
            
            DXmlCatalog xmlCatalogMethodOfPayment = ((DGuiClientApp) session.getClient()).getXmlCatalogsMap().get(DCfdi40Catalogs.CAT_MDP);
            DDbBizPartner bizPartner = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, new int[] { dps.getFkBizPartnerBizPartnerId() });
            DDbBizPartnerConfig bizPartnerConfig = bizPartner.getChildConfig(DTrnUtils.getBizPartnerClassByDpsCategory(dps.getFkDpsCategoryId()));
            
            if (dps.getXtaDfrMate() == null) {
                dps.setXtaDfrMate(new DDfrMate());
            }
            
            dps.getXtaDfrMate().setMethodOfPayment(xmlCatalogMethodOfPayment.getCode(dps.getCreditDays() == 0 ? DModSysConsts.TS_XML_TP_PAY_PUE : DModSysConsts.TS_XML_TP_PAY_PPD));
            dps.setFkModeOfPaymentTypeId(dps.getCreditDays() == 0 ? DModSysConsts.FS_MOP_TP_NA : DModSysConsts.FS_MOP_TP_TO_DEF);
            dps.getXtaDfrMate().setPaymentTerms(DTrnDfrUtils.composeCfdiPaymentTerms(dps.getFkPaymentTypeId(), dps.getCreditDays()));
            dps.getXtaDfrMate().setConfirmation("");
            dps.getXtaDfrMate().setIssuerTaxRegime((String) session.readField(DModConsts.CS_TAX_REG, new int[] { ((DDbConfigCompany) session.getConfigCompany()).getChildBizPartner().getFkTaxRegimeId() }, DDbRegistry.FIELD_CODE));
            dps.getXtaDfrMate().setReceiverTaxRegime((String) session.readField(DModConsts.CS_TAX_REG, new int[] { bizPartner.getFkTaxRegimeId() }, DDbRegistry.FIELD_CODE));
            dps.getXtaDfrMate().setReceiverFiscalAddress(bizPartner.getActualAddressFiscal());
            dps.getXtaDfrMate().setCfdUsage(bizPartnerConfig.getActualCfdUsage());
            dps.getXtaDfrMate().setGlobalPeriodicity("");
            dps.getXtaDfrMate().setGlobalMonths("");
            dps.getXtaDfrMate().setGlobalYear(0);
            dps.getXtaDfrMate().setRelations(null); // it is assumed that no UUID's are available
            
            for (DDbDpsRow row : dps.getChildRows()) {
                if (!row.isDeleted()) {
                    DDbItem item = (DDbItem) session.readRegistry(DModConsts.IU_ITM, new int[] { row.getFkRowItemId() });
                    DDbUnit unit = (DDbUnit) session.readRegistry(DModConsts.IU_UNT, new int[] { row.getFkRowUnitId() });
                    
                    row.setDfrItemKey(item.getActualCfdItemKey());
                    row.setDfrUnitKey(unit.getCfdUnitKey());
                }
            }
            
            dps.setChildDfr(DTrnDfrUtils.createDfrFromDps(session, dps, mnAuxXmlTypeId));
        }

        dps.save(session);

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbDpsTypeChange clone() throws CloneNotSupportedException {
        DDbDpsTypeChange registry = new DDbDpsTypeChange();

        registry.setPkDpsId(this.getPkDpsId());
        registry.setPkChangeId(this.getPkChangeId());
        registry.setNewSeries(this.getNewSeries());
        registry.setNewNumber(this.getNewNumber());
        registry.setNewOrder(this.getNewOrder());
        registry.setNewDate(this.getNewDate());
        registry.setOldSeries(this.getOldSeries());
        registry.setOldNumber(this.getOldNumber());
        registry.setOldOrder(this.getOldOrder());
        registry.setOldDate(this.getOldDate());
        registry.setDeleted(this.isDeleted());
        registry.setFkNewDpsCategoryId(this.getFkNewDpsCategoryId());
        registry.setFkNewDpsClassId(this.getFkNewDpsClassId());
        registry.setFkNewDpsTypeId(this.getFkNewDpsTypeId());
        registry.setFkOldDpsCategoryId(this.getFkOldDpsCategoryId());
        registry.setFkOldDpsClassId(this.getFkOldDpsClassId());
        registry.setFkOldDpsTypeId(this.getFkOldDpsTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setAuxXmlTypeId(this.getAuxXmlTypeId());
        registry.setAuxDpsLock(this.getAuxDpsLock()); // locks cannot be clonned!

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
