/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.lad.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import sba.gui.prt.DPrtUtils;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.cfg.db.DDbConfigBranch;
import sba.mod.cfg.db.DDbLock;
import sba.mod.cfg.db.DLockUtils;
import sba.mod.trn.db.DDbDfr;
import sba.mod.trn.db.DTrnDfrUtils;
import sba.mod.trn.db.DTrnDoc;
import sba.mod.trn.db.DTrnUtils;

/**
 *
 * @author Sergio Flores
 */
public class DDbBol extends DDbRegistryUser implements DTrnDoc {
    
    public static final int BOL_REAL = 1; // real BOL
    public static final int BOL_TEMPLATE = 2; // template BOL
    
    public static final int FIELD_TEMP_CODE = FIELD_BASE + 1;
    public static final int FIELD_TEMP_NAME = FIELD_BASE + 2;

    /** Timeout in minutes.  */
    public static final int TIMEOUT = 3; // 3 min.

    protected int mnPkBolId;
    protected String msVersion;
    protected String msSeries;
    protected int mnNumber;
    protected Date mtDate;
    protected String msIntlTransport;
    protected String msIntlTransportDirection;
    protected double mdDistanceKm;
    protected double mdMerchandiseWeight;
    protected int mnMerchandiseNumber;
    protected boolean mbMerchandiseInverseLogistics;
    protected boolean mbIsthmus;
    protected String msIsthmusOrigin;
    protected String msIsthmusDestiny;
    protected String msBolUuid;
    protected boolean mbTemplate;
    protected String msTemplateCode;
    protected String msTemplateName;
    /*
    protected boolean mbDeleted;
    */
    protected int mnFkTransportTypeId;
    protected int mnFkBolStatusId;
    protected int mnFkOwnerBizPartnerId;
    protected int mnFkOwnerBranchId;
    protected int mnFkIntlTransportCountryId;
    protected int mnFkIntlWayTransportTypeId;
    protected int mnFkMerchandiseWeightUnitId;
    protected int mnFkBolTemplateId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected ArrayList<DDbBolLocation> maChildLocations;
    protected ArrayList<DDbBolMerchandise> maChildMerchandises;
    protected ArrayList<DDbBolTruck> maChildTrucks;
    protected ArrayList<DDbBolTransportFigure> maChildTransportFigures;

    protected DDbDfr moChildDfr;
    
    protected int mnAuxXmlTypeId;
    protected DDbLock moAuxLock;
    
    public DDbBol() {
        super(DModConsts.L_BOL);
        maChildLocations = new ArrayList<>();
        maChildMerchandises = new ArrayList<>();
        maChildTrucks = new ArrayList<>();
        maChildTransportFigures = new ArrayList<>();
        initRegistry();
    }
    
    /*
     * Private methods
     */
    
    private void computeNextNumber(final DGuiSession session) throws Exception {
        if (!mbTemplate) {
            msSql = "SELECT MAX(num) "
                    + "FROM " + getSqlTable() + " "
                    + "WHERE ser = '" + msSeries + "' AND NOT b_del;";
            try (ResultSet resultSet = session.getStatement().executeQuery(msSql)) {
                if (resultSet.next()) {
                    mnNumber = resultSet.getInt(1) + 1;
                }
            }
        }
    }
    
    private void computeChildDfr(final DGuiSession session) throws Exception {
        if (!mbTemplate) {
            boolean save = false;

            if (!mbDeleted && mnFkBolStatusId == DModSysConsts.TS_DPS_ST_NEW && mnAuxXmlTypeId != 0) {
                moChildDfr = DTrnDfrUtils.createDfrFromBol(session, this, mnAuxXmlTypeId);
                save = true;
            }

            if (save) {
                saveChildDfr(session, true);
            }
        }
    }

    private void saveChildDfr(final DGuiSession session, boolean printDfr) throws SQLException, Exception {
        if (moChildDfr == null) {
            msSql = "UPDATE " + DModConsts.TablesMap.get(DModConsts.T_DFR) + " "
                    + "SET b_del = 1, fk_usr_upd = " + session.getUser().getPkUserId() + ", ts_usr_upd = NOW() "
                    + "WHERE fk_bol_n = " + mnPkBolId + ";";
            session.getStatement().execute(msSql);
        }
        else {
            moChildDfr.setFkOwnerBizPartnerId(mnFkOwnerBizPartnerId);
            moChildDfr.setFkOwnerBranchId(mnFkOwnerBranchId);
            moChildDfr.setFkBizPartnerId(mnFkOwnerBizPartnerId);
            moChildDfr.setFkBolId_n(mnPkBolId);
            moChildDfr.setDeleted(mbDeleted);
            moChildDfr.save(session);

            if (printDfr) {
                printDfr(session);
            }
        }
    }

    /*
     * Public methods
     */

    public void setPkBolId(int n) { mnPkBolId = n; }
    public void setVersion(String s) { msVersion = s; }
    public void setSeries(String s) { msSeries = s; }
    public void setNumber(int n) { mnNumber = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setIntlTransport(String s) { msIntlTransport = s; }
    public void setIntlTransportDirection(String s) { msIntlTransportDirection = s; }
    public void setDistanceKm(double d) { mdDistanceKm = d; }
    public void setMerchandiseWeight(double d) { mdMerchandiseWeight = d; }
    public void setMerchandiseNumber(int n) { mnMerchandiseNumber = n; }
    public void setMerchandiseInverseLogistics(boolean b) { mbMerchandiseInverseLogistics = b; }
    public void setIsthmus(boolean b) { mbIsthmus = b; }
    public void setIsthmusOrigin(String s) { msIsthmusOrigin = s; }
    public void setIsthmusDestiny(String s) { msIsthmusDestiny = s; }
    public void setBolUuid(String s) { msBolUuid = s; }
    public void setTemplate(boolean b) { mbTemplate = b; }
    public void setTemplateCode(String s) { msTemplateCode = s; }
    public void setTemplateName(String s) { msTemplateName = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkTransportTypeId(int n) { mnFkTransportTypeId = n; }
    public void setFkBolStatusId(int n) { mnFkBolStatusId = n; }
    public void setFkOwnerBizPartnerId(int n) { mnFkOwnerBizPartnerId = n; }
    public void setFkOwnerBranchId(int n) { mnFkOwnerBranchId = n; }
    public void setFkIntlTransportCountryId(int n) { mnFkIntlTransportCountryId = n; }
    public void setFkIntlWayTransportTypeId(int n) { mnFkIntlWayTransportTypeId = n; }
    public void setFkMerchandiseWeightUnitId(int n) { mnFkMerchandiseWeightUnitId = n; }
    public void setFkBolTemplateId_n(int n) { mnFkBolTemplateId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkBolId() { return mnPkBolId; }
    public String getVersion() { return msVersion; }
    public String getSeries() { return msSeries; }
    public int getNumber() { return mnNumber; }
    public Date getDate() { return mtDate; }
    public String getIntlTransport() { return msIntlTransport; }
    public String getIntlTransportDirection() { return msIntlTransportDirection; }
    public double getDistanceKm() { return mdDistanceKm; }
    public double getMerchandiseWeight() { return mdMerchandiseWeight; }
    public int getMerchandiseNumber() { return mnMerchandiseNumber; }
    public boolean isMerchandiseInverseLogistics() { return mbMerchandiseInverseLogistics; }
    public boolean isIsthmus() { return mbIsthmus; }
    public String getIsthmusOrigin() { return msIsthmusOrigin; }
    public String getIsthmusDestiny() { return msIsthmusDestiny; }
    public String getBolUuid() { return msBolUuid; }
    public boolean isTemplate() { return mbTemplate; }
    public String getTemplateCode() { return msTemplateCode; }
    public String getTemplateName() { return msTemplateName; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkTransportTypeId() { return mnFkTransportTypeId; }
    public int getFkBolStatusId() { return mnFkBolStatusId; }
    public int getFkOwnerBizPartnerId() { return mnFkOwnerBizPartnerId; }
    public int getFkOwnerBranchId() { return mnFkOwnerBranchId; }
    public int getFkIntlTransportCountryId() { return mnFkIntlTransportCountryId; }
    public int getFkIntlWayTransportTypeId() { return mnFkIntlWayTransportTypeId; }
    public int getFkMerchandiseWeightUnitId() { return mnFkMerchandiseWeightUnitId; }
    public int getFkBolTemplateId_n() { return mnFkBolTemplateId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public ArrayList<DDbBolLocation> getChildLocations() { return maChildLocations; }
    public ArrayList<DDbBolMerchandise> getChildMerchandises() { return maChildMerchandises; }
    public ArrayList<DDbBolTruck> getChildTrucks() { return maChildTrucks; }
    public ArrayList<DDbBolTransportFigure> getChildTransportFigures() { return maChildTransportFigures; }

    public void setChildDfr(DDbDfr o) { moChildDfr = o; }
    
    public DDbDfr getChildDfr() { return moChildDfr; }
    
    public void setAuxXmlTypeId(int n) { mnAuxXmlTypeId = n; }
    public void setAuxLock(DDbLock o) { moAuxLock = o; }
    
    public int getAuxXmlTypeId() { return mnAuxXmlTypeId; }
    public DDbLock getAuxLock() { return moAuxLock; }
    
    public int[] getCompanyKey() { return new int[] { mnFkOwnerBizPartnerId }; }
    @Override
    public int[] getCompanyBranchKey() { return new int[] { mnFkOwnerBizPartnerId, mnFkOwnerBranchId }; }
    public String getBolNumber() { return DTrnUtils.composeDpsNumber(msSeries, mnNumber); }
    
    public DDbBolLocation getChildLocation(final int[] key) {
        DDbBolLocation bolLocation = null;
        
        for (DDbBolLocation bl : maChildLocations) {
            if (DLibUtils.compareKeys(bl.getPrimaryKey(), key)) {
                bolLocation = bl;
                break;
            }
        }
        
        return bolLocation;
    }
    
    public DDbBolMerchandise getChildMerchandise(final int[] key) {
        DDbBolMerchandise bolMerchandise = null;
        
        for (DDbBolMerchandise bm : maChildMerchandises) {
            if (DLibUtils.compareKeys(bm.getPrimaryKey(), key)) {
                bolMerchandise = bm;
                break;
            }
        }
        
        return bolMerchandise;
    }
    
    public DDbBolTransportFigure getFirstChildTransportFigure(final int transportFigureType) {
        DDbBolTransportFigure bolTransportFigure = null;
        
        for (DDbBolTransportFigure btf : maChildTransportFigures) {
            if (btf.getFkTransportFigureTypeId() == transportFigureType) {
                bolTransportFigure = btf;
                break;
            }
        }
        
        return bolTransportFigure;
    }
    
    public void initBolTemplate(final int bolTemplateId) {
        mbTemplate = false;
        msTemplateCode = "";
        msTemplateName = "";
        mnFkBolTemplateId_n = bolTemplateId;
        
        for (DDbBolLocation location : maChildLocations) {
            location.setPkBolId(0);
            location.setFkSourceBolId_n(0);
        }
        
        for (DDbBolMerchandise merchandise : maChildMerchandises) {
            merchandise.setPkBolId(0);
            
            for (DDbBolMerchandiseMove merchandiseMove : merchandise.getChildMoves()) {
                merchandiseMove.setPkBolId(0);
                merchandiseMove.setFkSourceBolId(0);
                merchandiseMove.setFkDestinyBolId(0);
            }
        }
        
        for (DDbBolTruck truck : maChildTrucks) {
            truck.setPkBolId(0);
            
            for (DDbBolTruckTrailer truckTrailer : truck.getChildTrailers()) {
                truckTrailer.setPkBolId(0);
            }
        }
        
        for (DDbBolTransportFigure transportFigure : maChildTransportFigures) {
            transportFigure.setPkBolId(0);
            
            for (DDbBolTransportFigureTransportPart transportFigureTransportPart : transportFigure.getChildTransportParts()) {
                transportFigureTransportPart.setPkBolId(0);
            }
        }
    }
    
    public void initBolLocations(final Date arrivalDepartureDatetime) {
        for (DDbBolLocation location : maChildLocations) {
            location.setArrivalDepartureDatetime(arrivalDepartureDatetime);
        }
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBolId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBolId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBolId = 0;
        msVersion = "";
        msSeries = "";
        mnNumber = 0;
        mtDate = null;
        msIntlTransport = "";
        msIntlTransportDirection = "";
        mdDistanceKm = 0;
        mdMerchandiseWeight = 0;
        mnMerchandiseNumber = 0;
        mbMerchandiseInverseLogistics = false;
        mbIsthmus = false;
        msIsthmusOrigin = "";
        msIsthmusDestiny = "";
        msBolUuid = "";
        mbTemplate = false;
        msTemplateCode = "";
        msTemplateName = "";
        mbDeleted = false;
        mnFkTransportTypeId = 0;
        mnFkBolStatusId = 0;
        mnFkOwnerBizPartnerId = 0;
        mnFkOwnerBranchId = 0;
        mnFkIntlTransportCountryId = 0;
        mnFkIntlWayTransportTypeId = 0;
        mnFkMerchandiseWeightUnitId = 0;
        mnFkBolTemplateId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maChildLocations.clear();
        maChildMerchandises.clear();
        maChildTrucks.clear();
        maChildTransportFigures.clear();
        
        moChildDfr = null;
        
        mnAuxXmlTypeId = 0;
        moAuxLock = null;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bol = " + mnPkBolId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bol = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkBolId = 0;

        msSql = "SELECT COALESCE(MAX(id_bol), 0) + 1 "
                + "FROM " + getSqlTable() + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkBolId = resultSet.getInt(1);
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
            mnPkBolId = resultSet.getInt("id_bol");
            msVersion = resultSet.getString("ver");
            msSeries = resultSet.getString("ser");
            mnNumber = resultSet.getInt("num");
            mtDate = resultSet.getDate("dt");
            msIntlTransport = resultSet.getString("intl_tpt");
            msIntlTransportDirection = resultSet.getString("intl_tpt_dir");
            mdDistanceKm = resultSet.getDouble("dist_km");
            mdMerchandiseWeight = resultSet.getDouble("merch_weight");
            mnMerchandiseNumber = resultSet.getInt("merch_num");
            mbMerchandiseInverseLogistics = resultSet.getBoolean("b_merch_inv_log");
            mbIsthmus = resultSet.getBoolean("b_isthmus");
            msIsthmusOrigin = resultSet.getString("isthmus_orig");
            msIsthmusDestiny = resultSet.getString("isthmus_dest");
            msBolUuid = resultSet.getString("bol_uuid");
            mbTemplate = resultSet.getBoolean("b_temp");
            msTemplateCode = resultSet.getString("temp_code");
            msTemplateName = resultSet.getString("temp_name");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkTransportTypeId = resultSet.getInt("fk_tpt_tp");
            mnFkBolStatusId = resultSet.getInt("fk_bol_st");
            mnFkOwnerBizPartnerId = resultSet.getInt("fk_own_bpr");
            mnFkOwnerBranchId = resultSet.getInt("fk_own_bra");
            mnFkIntlTransportCountryId = resultSet.getInt("fk_intl_tpt_cty");
            mnFkIntlWayTransportTypeId = resultSet.getInt("fk_intl_way_tpt_tp");
            mnFkMerchandiseWeightUnitId = resultSet.getInt("fk_merch_weight_unt");
            mnFkBolTemplateId_n = resultSet.getInt("fk_bol_temp_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // read as well child locations:
            
            msSql = "SELECT id_loc "
                    + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_LOC) + " "
                    + getSqlWhere();
            
            try (Statement statement = session.getStatement().getConnection().createStatement()) {
                resultSet = statement.executeQuery(msSql);
                while (resultSet.next()) {
                    DDbBolLocation location = (DDbBolLocation) session.readRegistry(DModConsts.L_BOL_LOC, new int[] { mnPkBolId, resultSet.getInt("id_loc") });
                    maChildLocations.add(location);
                }
            }

            // read as well child merchandises:
            
            msSql = "SELECT id_merch "
                    + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_MERCH) + " "
                    + getSqlWhere();
            
            try (Statement statement = session.getStatement().getConnection().createStatement()) {
                resultSet = statement.executeQuery(msSql);
                while (resultSet.next()) {
                    DDbBolMerchandise merchandise = (DDbBolMerchandise) session.readRegistry(DModConsts.L_BOL_MERCH, new int[] { mnPkBolId, resultSet.getInt("id_merch") });
                    maChildMerchandises.add(merchandise);
                }
            }

            // read as well child trucks:
            
            msSql = "SELECT id_truck "
                    + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_TRUCK) + " "
                    + getSqlWhere();
            
            try (Statement statement = session.getStatement().getConnection().createStatement()) {
                resultSet = statement.executeQuery(msSql);
                while (resultSet.next()) {
                    DDbBolTruck truck = (DDbBolTruck) session.readRegistry(DModConsts.L_BOL_TRUCK, new int[] { mnPkBolId, resultSet.getInt("id_truck") });
                    maChildTrucks.add(truck);
                }
            }

            // read as well child transport figures:
            
            msSql = "SELECT id_tpt_figure "
                    + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_TPT_FIGURE) + " "
                    + getSqlWhere();
            
            try (Statement statement = session.getStatement().getConnection().createStatement()) {
                resultSet = statement.executeQuery(msSql);
                while (resultSet.next()) {
                    DDbBolTransportFigure transportFigure = (DDbBolTransportFigure) session.readRegistry(DModConsts.L_BOL_TPT_FIGURE, new int[] { mnPkBolId, resultSet.getInt("id_tpt_figure") });
                    maChildTransportFigures.add(transportFigure);
                }
            }

            // read as well child DFR:
            
            msSql = "SELECT id_dfr "
                    + "FROM " + DModConsts.TablesMap.get(DModConsts.T_DFR) + " "
                    + "WHERE fk_bol_n = " + mnPkBolId + ";";
            
            try (Statement statement = session.getStatement().getConnection().createStatement()) {
                resultSet = statement.executeQuery(msSql);
                if (resultSet.next()) {
                    moChildDfr = (DDbDfr) session.readRegistry(DModConsts.T_DFR, new int[] { resultSet.getInt("id_dfr") });
                }
            }
            
            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;
        
        if (mbRegistryNew) {
            if (!mbTemplate) {
                computeNextNumber(session);
                msBolUuid = "CCC" + java.util.UUID.randomUUID().toString().substring(3).toUpperCase();
            }
            
            computePrimaryKey(session);
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkBolId + ", " + 
                    "'" + msVersion + "', " + 
                    "'" + msSeries + "', " + 
                    mnNumber + ", " + 
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " + 
                    "'" + msIntlTransport + "', " + 
                    "'" + msIntlTransportDirection + "', " + 
                    mdDistanceKm + ", " + 
                    mdMerchandiseWeight + ", " + 
                    mnMerchandiseNumber + ", " + 
                    (mbMerchandiseInverseLogistics ? 1 : 0) + ", " + 
                    (mbIsthmus ? 1 : 0) + ", " + 
                    "'" + msIsthmusOrigin + "', " + 
                    "'" + msIsthmusDestiny + "', " + 
                    "'" + msBolUuid + "', " + 
                    (mbTemplate ? 1 : 0) + ", " + 
                    "'" + msTemplateCode + "', " + 
                    "'" + msTemplateName + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkTransportTypeId + ", " + 
                    mnFkBolStatusId + ", " + 
                    mnFkOwnerBizPartnerId + ", " + 
                    mnFkOwnerBranchId + ", " + 
                    mnFkIntlTransportCountryId + ", " + 
                    mnFkIntlWayTransportTypeId + ", " + 
                    mnFkMerchandiseWeightUnitId + ", " + 
                    (mnFkBolTemplateId_n == 0 ? "NULL" : mnFkBolTemplateId_n) + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_bol = " + mnPkBolId + ", " +
                    "ver = '" + msVersion + "', " +
                    "ser = '" + msSeries + "', " +
                    "num = " + mnNumber + ", " +
                    "dt = '" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "intl_tpt = '" + msIntlTransport + "', " +
                    "intl_tpt_dir = '" + msIntlTransportDirection + "', " +
                    "dist_km = " + mdDistanceKm + ", " +
                    "merch_weight = " + mdMerchandiseWeight + ", " +
                    "merch_num = " + mnMerchandiseNumber + ", " +
                    "b_merch_inv_log = " + (mbMerchandiseInverseLogistics ? 1 : 0) + ", " +
                    "b_isthmus = " + (mbIsthmus ? 1 : 0) + ", " +
                    "isthmus_orig = '" + msIsthmusOrigin + "', " +
                    "isthmus_dest = '" + msIsthmusDestiny + "', " +
                    "bol_uuid = '" + msBolUuid + "', " +
                    "b_temp = " + (mbTemplate ? 1 : 0) + ", " +
                    "temp_code = '" + msTemplateCode + "', " +
                    "temp_name = '" + msTemplateName + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tpt_tp = " + mnFkTransportTypeId + ", " +
                    "fk_bol_st = " + mnFkBolStatusId + ", " +
                    "fk_own_bpr = " + mnFkOwnerBizPartnerId + ", " +
                    "fk_own_bra = " + mnFkOwnerBranchId + ", " +
                    "fk_intl_tpt_cty = " + mnFkIntlTransportCountryId + ", " +
                    "fk_intl_way_tpt_tp = " + mnFkIntlWayTransportTypeId + ", " +
                    "fk_merch_weight_unt = " + mnFkMerchandiseWeightUnitId + ", " +
                    "fk_bol_temp_n = " + (mnFkBolTemplateId_n == 0 ? "NULL" : mnFkBolTemplateId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        // set location ID (if it is already set on creation, a new one will not be computed):
        
        int locationId = 0;
        for (DDbBolLocation bolLocation : maChildLocations) {
            bolLocation.setPkLocationId(++locationId);
            
            if (bolLocation.getTempBolLocationId() != 0) {
                // propague new location ID:
                
                if (bolLocation.isLocationSource()) {
                    for (DDbBolLocation destiny : maChildLocations) {
                        if (destiny.isLocationDestiny()) {
                            if (destiny.getTempSourceBolLocationId() == bolLocation.getTempBolLocationId()) {
                                destiny.setFkSourceBolId_n(mnPkBolId);
                                destiny.setFkSourceLocationId_n(bolLocation.getPkLocationId());
                            }
                        }
                    }
                }
                
                for (DDbBolMerchandise bolMerchandise : maChildMerchandises) {
                    for (DDbBolMerchandiseMove move : bolMerchandise.getChildMoves()) {
                        switch (bolLocation.getFkLocationTypeId()) {
                            case DModSysConsts.LS_LOC_TP_SRC:
                                if (move.getTempSourceBolLocationId() == bolLocation.getTempBolLocationId()) {
                                    move.setFkSourceBolId(mnPkBolId);
                                    move.setFkSourceLocationId(bolLocation.getPkLocationId());
                                }
                                break;
                                
                            case DModSysConsts.LS_LOC_TP_DES:
                                if (move.getTempDestinyBolLocationId() == bolLocation.getTempBolLocationId()) {
                                    move.setFkDestinyBolId(mnPkBolId);
                                    move.setFkDestinyLocationId(bolLocation.getPkLocationId());
                                }
                                break;
                                
                            default:
                                // nothing
                        }
                    }
                }
            }
        }

        // delete all existing children:
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_MERCH_MOVE) + " "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_MERCH) + " "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        msSql = "UPDATE " + DModConsts.TablesMap.get(DModConsts.L_BOL_LOC) + " SET "
                + "fk_src_bol_n = NULL, "
                + "fk_src_loc_n = NULL "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_LOC) + " "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_TRUCK_TRAIL) + " "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_TRUCK) + " "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_TPT_FIGURE_TPT_PART) + " "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_TPT_FIGURE) + " "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        // save child source locations (precede destiny locations):
        
        for (DDbBolLocation bolLocation : maChildLocations) {
            if (bolLocation.isLocationSource()) {
                bolLocation.setPkBolId(mnPkBolId);
                bolLocation.setRegistryNew(true);
                bolLocation.save(session);
            }
        }
        
        // save child destiny locations (depend on source locations):
        
        for (DDbBolLocation bolLocation : maChildLocations) {
            if (bolLocation.isLocationDestiny()) {
                bolLocation.setPkBolId(mnPkBolId);
                bolLocation.setRegistryNew(true);
                bolLocation.save(session);
            }
        }
        
        // save as well child merchandises:
        
        for (DDbBolMerchandise bolMerchandise : maChildMerchandises) {
            bolMerchandise.setPkBolId(mnPkBolId);
            bolMerchandise.setRegistryNew(true);
            bolMerchandise.save(session);
        }
        
        // save as well child transport figures (before saving trucks!):
        
        for (DDbBolTransportFigure bolTransportFigure : maChildTransportFigures) {
            bolTransportFigure.setPkBolId(mnPkBolId);
            bolTransportFigure.setRegistryNew(true);
            bolTransportFigure.save(session);
        }
        
        // save as well child trucks (after saving transport figures!):
        
        for (DDbBolTruck bolTruck : maChildTrucks) {
            if (bolTruck.isBolUpdateOwnRegistry()) {
                // update foreign keys to transport figures:
                
                DDbTruck truck = (DDbTruck) bolTruck.getOwnTruck();

                int index = 0;
                for (DDbBolTransportFigure bolTransportFigure : maChildTransportFigures) {
                    truck.getChildTransportFigures().get(index).setFkTransportFigureId(bolTransportFigure.getFkTransportFigureId());
                    index++;
                }
            }
            
            bolTruck.setPkBolId(mnPkBolId);
            bolTruck.setRegistryNew(true);
            bolTruck.save(session);
        }
        
        // aditional processing:
        
        if (!mbTemplate) {
            computeChildDfr(session);
        }

        // finish registry updating:
        
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbBol clone() throws CloneNotSupportedException {
        DDbBol registry = new DDbBol();

        registry.setPkBolId(this.getPkBolId());
        registry.setVersion(this.getVersion());
        registry.setSeries(this.getSeries());
        registry.setNumber(this.getNumber());
        registry.setDate(this.getDate());
        registry.setIntlTransport(this.getIntlTransport());
        registry.setIntlTransportDirection(this.getIntlTransportDirection());
        registry.setDistanceKm(this.getDistanceKm());
        registry.setMerchandiseWeight(this.getMerchandiseWeight());
        registry.setMerchandiseNumber(this.getMerchandiseNumber());
        registry.setMerchandiseInverseLogistics(this.isMerchandiseInverseLogistics());
        registry.setIsthmus(this.isIsthmus());
        registry.setIsthmusOrigin(this.getIsthmusOrigin());
        registry.setIsthmusDestiny(this.getIsthmusDestiny());
        registry.setBolUuid(this.getBolUuid());
        registry.setTemplate(this.isTemplate());
        registry.setTemplateCode(this.getTemplateCode());
        registry.setTemplateName(this.getTemplateName());
        registry.setDeleted(this.isDeleted());
        registry.setFkTransportTypeId(this.getFkTransportTypeId());
        registry.setFkBolStatusId(this.getFkBolStatusId());
        registry.setFkOwnerBizPartnerId(this.getFkOwnerBizPartnerId());
        registry.setFkOwnerBranchId(this.getFkOwnerBranchId());
        registry.setFkIntlTransportCountryId(this.getFkIntlTransportCountryId());
        registry.setFkIntlWayTransportTypeId(this.getFkIntlWayTransportTypeId());
        registry.setFkMerchandiseWeightUnitId(this.getFkMerchandiseWeightUnitId());
        registry.setFkBolTemplateId_n(this.getFkBolTemplateId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        // clone as well child locations:
        
        for (DDbBolLocation location : maChildLocations) {
            registry.getChildLocations().add(location.clone());
        }
        
        // save as well child merchandises:
        
        for (DDbBolMerchandise merchandise : maChildMerchandises) {
            registry.getChildMerchandises().add(merchandise.clone());
        }
        
        // save as well child trucks:
        
        for (DDbBolTruck truck : maChildTrucks) {
            registry.getChildTrucks().add(truck.clone());
        }
        
        // save as well child transport figures:
        
        for (DDbBolTransportFigure transportFigure : maChildTransportFigures) {
            registry.getChildTransportFigures().add(transportFigure.clone());
        }
        
        registry.setChildDfr(this.getChildDfr() == null ? null : this.getChildDfr().clone());
        
        registry.setAuxXmlTypeId(this.getAuxXmlTypeId());
        registry.setAuxLock(this.getAuxLock()); // locks cannot be clonned!
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    @Override
    public Object readField(final Statement statement, final int[] pk, final int field) throws SQLException, Exception {
        if (DLibUtils.belongsTo(field, new int[] { FIELD_TEMP_CODE, FIELD_TEMP_NAME })) {
            Object value = null;
            ResultSet resultSet = null;

            initQueryMembers();
            mnQueryResultId = DDbConsts.READ_ERROR;

            msSql = "SELECT ";

            switch (field) {
                case FIELD_TEMP_CODE:
                    msSql += "temp_code ";
                    break;
                case FIELD_TEMP_NAME:
                    msSql += "temp_name ";
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
                    case FIELD_TEMP_CODE:
                    case FIELD_TEMP_NAME:
                        value = resultSet.getString(1);
                        break;
                    default:
                        // nothing
                }
            }

            mnQueryResultId = DDbConsts.READ_OK;
            return value;
        }
        else {
            return super.readField(statement, pk, field);
        }
    }

    /**
     * Updates Digital Fiscal Receipt (DFR).
     * VERY IMPORTANT NOTICE!: Check on each usage of this method that is covered by exclusive-access locks.
     * By now, all current usages (one in DTrnDfrUtils, two in DTrnEmissionUtils) are covered properly by these locks.
     * @param session
     * @param dfr
     * @throws java.sql.SQLException
     */
    public void updateDfr(final DGuiSession session, final DDbDfr dfr) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        moChildDfr = dfr;
        saveChildDfr(session, false);

        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public String getDocName() {
        return "traslado";
    }

    @Override
    public String getDocNumber() {
        return getBolNumber();
    }

    @Override
    public Date getDocDate() {
        return getChildDfr() == null ? null : getChildDfr().getDocTs();
    }

    /**
     * Get BOL status. Constants defined in DModSysConsts.TS_XML_ST_...
     */
    @Override
    public int getDocStatus() {
        return getFkBolStatusId();
    }

    @Override
    public int getBizPartnerCategory() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int[] getBizPartnerKey() {
        return getCompanyKey();
    }

    @Override
    public int[] getBizPartnerBranchAddressKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DDbDfr getDfr() {
        return getChildDfr();
    }

    @Override
    public DDbRegistry createDocSending() {
        return null;
    }

    @Override
    public void printDfr(DGuiSession session) throws Exception {
        String fileName = "";
        DDbConfigBranch configBranch = null;
        
        if (moChildDfr != null && moChildDfr.getFkXmlStatusId() == DModSysConsts.TS_XML_ST_ISS) {
            configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, getCompanyBranchKey());
            fileName += configBranch.getDfrDirectory();
            fileName += moChildDfr.getDocXmlName().replaceAll(".xml", ".pdf");

            switch (moChildDfr.getFkXmlTypeId()) {
                case DModSysConsts.TS_XML_TP_CFD:
                case DModSysConsts.TS_XML_TP_CFDI_32:
                case DModSysConsts.TS_XML_TP_CFDI_33:
                    throw new UnsupportedOperationException("Not supported yet.");  // no plans for supporting it later

                case DModSysConsts.TS_XML_TP_CFDI_40:
                    switch (msVersion) {
                        case cfd.ver3.ccp20.DElementCartaPorte.VERSION:
                            DPrtUtils.exportReportToPdfFile(session, DModConsts.TR_DPS_CFDI_40_CCP_20, new DLadBolPrinting(session, this).createPrintingMapCfdi40(), fileName);
                            break;
                        case cfd.ver4.ccp30.DElementCartaPorte.VERSION:
                            DPrtUtils.exportReportToPdfFile(session, DModConsts.TR_DPS_CFDI_40_CCP_30, new DLadBolPrinting(session, this).createPrintingMapCfdi40(), fileName);
                            break;
                        default:
                            throw new UnsupportedOperationException("Not supported yet.");  // invalid complement version
                    }
                    break;

                default:
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
    }

    @Override
    public boolean canAnnul(DGuiSession session) throws Exception {
        return true;
    }

    /**
     * Check if registry is available, that is, not locked.
     * @param session
     * @return
     * @throws Exception 
     */
    @Override
    public boolean checkAvailability(final DGuiSession session) throws Exception {
        boolean isAvailable = true;
        
        if (!mbRegistryNew) {
            if (moAuxLock == null) {
                // no lock set already, check only availabitity:
                isAvailable = DLockUtils.isRegistryAvailable(session, mnRegistryType, mnPkBolId);
            }
            else {
                // lock already set, validate it:
                DLockUtils.validateLock(session, moAuxLock, true);
                isAvailable = true;
            }
        }
        
        return isAvailable;
    }

    /**
     * Assure lock. That is if it does not already exist, it is created, otherwise validated.
     * @param session GUI session.
     * @return 
     * @throws Exception 
     */
    @Override
    public DDbLock assureLock(final DGuiSession session) throws Exception {
        if (!mbRegistryNew) {
            if (moAuxLock == null) {
                // no lock set already, create it:
                moAuxLock = DLockUtils.createLock(session, mnRegistryType, mnPkBolId, TIMEOUT);
            }
            else {
                // lock already set, validate it:
                DLockUtils.validateLock(session, moAuxLock, true);
            }
        }
        
        return moAuxLock;
    }

    /**
     * Free current lock, if any, with by-update status.
     * @param session GUI session.
     * @param freedLockStatus Options supported: DDbLock.LOCK_ST_FREED_...
     * @throws Exception 
     */
    @Override
    public void freeLock(final DGuiSession session, final int freedLockStatus) throws Exception {
        if (moAuxLock != null) {
            DLockUtils.freeLock(session, moAuxLock, freedLockStatus);
            moAuxLock = null;
        }
    }
}
