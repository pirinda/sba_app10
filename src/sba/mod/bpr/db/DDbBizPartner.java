/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.bpr.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.mkt.db.DDbAgentConfig;

/**
 *
 * @author Sergio Flores
 */
public class DDbBizPartner extends DDbRegistryUser {

    protected int mnPkBizPartnerId;
    protected String msName;
    protected String msLastname;
    protected String msFirstname;
    protected String msNickname;
    protected String msFiscalId;
    protected String msAlternativeId;
    protected String msForeignId;
    protected String msWeb;
    protected String msNote;
    protected boolean mbVendor;
    protected boolean mbCustomer;
    protected boolean mbCreditor;
    protected boolean mbDebtor;
    protected boolean mbFiscalHomologous;
    protected boolean mbBank;
    protected boolean mbCarrier;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkIdentityTypeId;
    protected int mnFkEmissionTypeId;
    protected int mnFkXmlAddendaTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected DDbAgentConfig moChildAgentConfig;
    protected Vector<DDbBizPartnerConfig> mvChildConfigs;
    protected Vector<DDbBranch> mvChildBranches;

    public DDbBizPartner() {
        super(DModConsts.BU_BPR);
        mvChildConfigs = new Vector<DDbBizPartnerConfig>();
        mvChildBranches = new Vector<DDbBranch>();
        initRegistry();
    }

    /*
     * Private methods
     */

    private String computeName() {
        return mnFkIdentityTypeId == DModSysConsts.BS_IDY_TP_PER ? msLastname + ", " + msFirstname : msName;
    }

    /*
     * Public methods
     */

    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setName(String s) { msName = s; }
    public void setLastname(String s) { msLastname = s; }
    public void setFirstname(String s) { msFirstname = s; }
    public void setNickname(String s) { msNickname = s; }
    public void setFiscalId(String s) { msFiscalId = s; }
    public void setAlternativeId(String s) { msAlternativeId = s; }
    public void setForeignId(String s) { msForeignId = s; }
    public void setWeb(String s) { msWeb = s; }
    public void setNote(String s) { msNote = s; }
    public void setVendor(boolean b) { mbVendor = b; }
    public void setCustomer(boolean b) { mbCustomer = b; }
    public void setCreditor(boolean b) { mbCreditor = b; }
    public void setDebtor(boolean b) { mbDebtor = b; }
    public void setFiscalHomologous(boolean b) { mbFiscalHomologous = b; }
    public void setBank(boolean b) { mbBank = b; }
    public void setCarrier(boolean b) { mbCarrier = b; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkIdentityTypeId(int n) { mnFkIdentityTypeId = n; }
    public void setFkEmissionTypeId(int n) { mnFkEmissionTypeId = n; }
    public void setFkXmlAddendaTypeId(int n) { mnFkXmlAddendaTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public String getName() { return msName; }
    public String getLastname() { return msLastname; }
    public String getFirstname() { return msFirstname; }
    public String getNickname() { return msNickname; }
    public String getFiscalId() { return msFiscalId; }
    public String getAlternativeId() { return msAlternativeId; }
    public String getForeignId() { return msForeignId; }
    public String getWeb() { return msWeb; }
    public String getNote() { return msNote; }
    public boolean isVendor() { return mbVendor; }
    public boolean isCustomer() { return mbCustomer; }
    public boolean isCreditor() { return mbCreditor; }
    public boolean isDebtor() { return mbDebtor; }
    public boolean isFiscalHomologous() { return mbFiscalHomologous; }
    public boolean isBank() { return mbBank; }
    public boolean isCarrier() { return mbCarrier; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkIdentityTypeId() { return mnFkIdentityTypeId; }
    public int getFkEmissionTypeId() { return mnFkEmissionTypeId; }
    public int getFkXmlAddendaTypeId() { return mnFkXmlAddendaTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setChildAgentConfig(DDbAgentConfig o) { moChildAgentConfig = o; }

    public DDbAgentConfig getChildAgentConfig() { return moChildAgentConfig; }
    public Vector<DDbBizPartnerConfig> getChildConfigs() { return mvChildConfigs; }
    public Vector<DDbBranch> getChildBranches() { return mvChildBranches; }

    public DDbBizPartnerConfig getChildConfig(int bprClassId) {
        DDbBizPartnerConfig config = null;

        for (DDbBizPartnerConfig child : mvChildConfigs) {
            if (child.getPkBizPartnerClassId() == bprClassId) {
                config = child;
                break;
            }
        }

        return config;
    }

    public DDbBranch getChildBranch(int[] branchKey) {
        DDbBranch desiredChild = null;

        for (DDbBranch child : mvChildBranches) {
            if (DLibUtils.compareKeys(branchKey, child.getPrimaryKey())) {
                desiredChild = child;
                break;
            }
        }
        return desiredChild;
    }

    public String getProperName() {
        return DLibUtils.textTrim(mnFkIdentityTypeId == DModSysConsts.BS_IDY_TP_ORG ? msName : msFirstname + " " + msLastname);
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBizPartnerId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBizPartnerId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBizPartnerId = 0;
        msName = "";
        msLastname = "";
        msFirstname = "";
        msNickname = "";
        msFiscalId = "";
        msAlternativeId = "";
        msForeignId = "";
        msWeb = "";
        msNote = "";
        mbVendor = false;
        mbCustomer = false;
        mbCreditor = false;
        mbDebtor = false;
        mbFiscalHomologous = false;
        mbBank = false;
        mbCarrier = false;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkIdentityTypeId = 0;
        mnFkEmissionTypeId = 0;
        mnFkXmlAddendaTypeId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        moChildAgentConfig = null;
        mvChildConfigs.clear();
        mvChildBranches.clear();
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bpr = " + mnPkBizPartnerId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bpr = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkBizPartnerId = 0;

        msSql = "SELECT COALESCE(MAX(id_bpr), 0) + 1 FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkBizPartnerId = resultSet.getInt(1);
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
            mnPkBizPartnerId = resultSet.getInt("id_bpr");
            msName = resultSet.getString("name");
            msLastname = resultSet.getString("name_lst");
            msFirstname = resultSet.getString("name_fst");
            msNickname = resultSet.getString("nick");
            msFiscalId = resultSet.getString("fis_id");
            msAlternativeId = resultSet.getString("alt_id");
            msForeignId = resultSet.getString("frg_id");
            msWeb = resultSet.getString("web");
            msNote = resultSet.getString("note");
            mbVendor = resultSet.getBoolean("b_ven");
            mbCustomer = resultSet.getBoolean("b_cus");
            mbCreditor = resultSet.getBoolean("b_cdr");
            mbDebtor = resultSet.getBoolean("b_dbr");
            mbFiscalHomologous = resultSet.getBoolean("b_fis_hom");
            mbBank = resultSet.getBoolean("b_bnk");
            mbCarrier = resultSet.getBoolean("b_car");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkIdentityTypeId = resultSet.getInt("fk_idy_tp");
            mnFkEmissionTypeId = resultSet.getInt("fk_emi_tp");
            mnFkXmlAddendaTypeId = resultSet.getInt("fk_xml_add_tp");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();

            msSql = "SELECT id_agt FROM " + DModConsts.TablesMap.get(DModConsts.M_AGT_CFG) + " " +
                    "WHERE id_agt = " + mnPkBizPartnerId + " ";
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {
                moChildAgentConfig = new DDbAgentConfig();
                moChildAgentConfig.read(session, new int[] { resultSet.getInt(1) });
            }

            msSql = "SELECT id_bpr_cl FROM " + DModConsts.TablesMap.get(DModConsts.BU_BPR_CFG) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbBizPartnerConfig child = new DDbBizPartnerConfig();
                child.read(session, new int[] { mnPkBizPartnerId, resultSet.getInt(1) });
                mvChildConfigs.add(child);
            }

            msSql = "SELECT id_bra FROM " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbBranch child = new DDbBranch();
                child.read(session, new int[] { mnPkBizPartnerId, resultSet.getInt(1) });
                mvChildBranches.add(child);
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

        msName = computeName();

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
                    mnPkBizPartnerId + ", " +
                    "'" + msName + "', " +
                    "'" + msLastname + "', " +
                    "'" + msFirstname + "', " +
                    "'" + msNickname + "', " +
                    "'" + msFiscalId + "', " +
                    "'" + msAlternativeId + "', " +
                    "'" + msForeignId + "', " +
                    "'" + msWeb + "', " +
                    "'" + msNote + "', " +
                    (mbVendor ? 1 : 0) + ", " +
                    (mbCustomer ? 1 : 0) + ", " +
                    (mbCreditor ? 1 : 0) + ", " +
                    (mbDebtor ? 1 : 0) + ", " +
                    (mbFiscalHomologous ? 1 : 0) + ", " +
                    (mbBank ? 1 : 0) + ", " +
                    (mbCarrier ? 1 : 0) + ", " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkIdentityTypeId + ", " +
                    mnFkEmissionTypeId + ", " +
                    mnFkXmlAddendaTypeId + ", " + 
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
                    */
                    "name = '" + msName + "', " +
                    "name_lst = '" + msLastname + "', " +
                    "name_fst = '" + msFirstname + "', " +
                    "nick = '" + msNickname + "', " +
                    "fis_id = '" + msFiscalId + "', " +
                    "alt_id = '" + msAlternativeId + "', " +
                    "frg_id = '" + msForeignId + "', " +
                    "web = '" + msWeb + "', " +
                    "note = '" + msNote + "', " +
                    "b_ven = " + (mbVendor ? 1 : 0) + ", " +
                    "b_cus = " + (mbCustomer ? 1 : 0) + ", " +
                    "b_cdr = " + (mbCreditor ? 1 : 0) + ", " +
                    "b_dbr = " + (mbDebtor ? 1 : 0) + ", " +
                    "b_fis_hom = " + (mbFiscalHomologous ? 1 : 0) + ", " +
                    "b_bnk = " + (mbBank ? 1 : 0) + ", " +
                    "b_car = " + (mbCarrier ? 1 : 0) + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_idy_tp = " + mnFkIdentityTypeId + ", " +
                    "fk_emi_tp = " + mnFkEmissionTypeId + ", " +
                    "fk_xml_add_tp = " + mnFkXmlAddendaTypeId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        if (moChildAgentConfig != null) {
            moChildAgentConfig.setPkAgentId(mnPkBizPartnerId);
            moChildAgentConfig.save(session);
        }

        for (DDbBizPartnerConfig child : mvChildConfigs) {
            child.setPkBizPartnerId(mnPkBizPartnerId);
            child.save(session);
        }

        for (DDbBranch child : mvChildBranches) {
            child.setPkBizPartnerId(mnPkBizPartnerId);
            child.save(session);
        }

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbBizPartner clone() throws CloneNotSupportedException {
        DDbBizPartner registry = new DDbBizPartner();

        registry.setPkBizPartnerId(this.getPkBizPartnerId());
        registry.setName(this.getName());
        registry.setLastname(this.getLastname());
        registry.setFirstname(this.getFirstname());
        registry.setNickname(this.getNickname());
        registry.setFiscalId(this.getFiscalId());
        registry.setAlternativeId(this.getAlternativeId());
        registry.setForeignId(this.getForeignId());
        registry.setWeb(this.getWeb());
        registry.setNote(this.getNote());
        registry.setVendor(this.isVendor());
        registry.setCustomer(this.isCustomer());
        registry.setCreditor(this.isCreditor());
        registry.setDebtor(this.isDebtor());
        registry.setFiscalHomologous(this.isFiscalHomologous());
        registry.setBank(this.isBank());
        registry.setCarrier(this.isCarrier());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkIdentityTypeId(this.getFkIdentityTypeId());
        registry.setFkEmissionTypeId(this.getFkEmissionTypeId());
        registry.setFkXmlAddendaTypeId(this.getFkXmlAddendaTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setChildAgentConfig(moChildAgentConfig == null ? null : moChildAgentConfig.clone());

        for (DDbBizPartnerConfig child : mvChildConfigs) {
            registry.getChildConfigs().add(child.clone());
        }

        for (DDbBranch child : mvChildBranches) {
            registry.getChildBranches().add(child.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public void setRegistryNew(boolean registryNew) {
        super.setRegistryNew(registryNew);

        if (moChildAgentConfig != null) {
            moChildAgentConfig.setRegistryNew(registryNew);
        }

        for (DDbBizPartnerConfig child : mvChildConfigs) {
            child.setRegistryNew(registryNew);
        }

        for (DDbBranch child : mvChildBranches) {
            child.setRegistryNew(registryNew);
        }
    }

    @Override
    public boolean canSave(final DGuiSession session) throws SQLException, Exception {
        boolean can = super.canSave(session);
        String name = computeName();
        ResultSet resultSet = null;

        if (can) {
            msSql = "SELECT COUNT(*) " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " " +
                    "WHERE name = '" + name + "' AND " +
                    "id_bpr <> " + mnPkBizPartnerId + " AND " +
                    "b_ven = " + mbVendor + " AND " +
                    "b_cus = " + mbCustomer + " AND " +
                    "b_cdr = " + mbCreditor + " AND " +
                    "b_dbr = " + mbDebtor + " ";
            resultSet = session.getStatement().executeQuery(msSql);
            if (resultSet.next()) {
                if (resultSet.getInt(1) > 0) {
                    can = false;
                    msQueryResult = DDbConsts.ERR_MSG_UNIQUE_NAME + " (" + name + ")";
                }
                else if (!mbFiscalHomologous) {
                    msSql = "SELECT COUNT(*) " +
                            "FROM " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " " +
                            "WHERE fis_id = '" + msFiscalId + "' AND fis_id NOT IN (" +
                            "'" + ((DDbConfigCompany) session.getConfigCompany()).getFiscalIdCountry() + "', " +
                            "'" + ((DDbConfigCompany) session.getConfigCompany()).getFiscalIdForeign() + "') AND " +
                            "id_bpr <> " + mnPkBizPartnerId + " AND " +
                            "b_ven = " + mbVendor + " AND " +
                            "b_cus = " + mbCustomer + " AND " +
                            "b_cdr = " + mbCreditor + " AND " +
                            "b_dbr = " + mbDebtor + " ";
                    resultSet = session.getStatement().executeQuery(msSql);
                    if (resultSet.next()) {
                        if (resultSet.getInt(1) > 0) {
                            can = false;
                            msQueryResult = DDbConsts.ERR_MSG_UNIQUE_FIELD + " " + DUtilConsts.TXT_FISCAL_ID + " (" + msFiscalId + ").";
                        }
                    }
                }
            }
        }

        return can;
    }
}
