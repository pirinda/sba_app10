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
import sba.gui.util.DUtilConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.trn.db.DDbDfr;

/**
 *
 * @author Sergio Flores
 */
public class DDbBol extends DDbRegistryUser {
    
    public static final int BOL_REAL = 1; // real BOL
    public static final int BOL_TEMP = 2; // template BOL

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
    protected boolean mbTemplate;
    protected String msTemplateCode;
    protected String msTemplateName;
    /*
    protected boolean mbDeleted;
    */
    protected int mnFkTransportTypeId;
    protected int mnFkBolStatusId;
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

    protected DDbDfr moOwnDfr;
    
    public DDbBol() {
        super(DModConsts.L_BOL);
        maChildLocations = new ArrayList<>();
        maChildMerchandises = new ArrayList<>();
        maChildTrucks = new ArrayList<>();
        maChildTransportFigures = new ArrayList<>();
        initRegistry();
    }
    
    public void computeNextNumber(final DGuiSession session) throws Exception {
        msSql = "SELECT MAX(num) "
                + "FROM " + getSqlTable() + " "
                + "WHERE ser = '" + msSeries + "' AND NOT b_del;";
        try (ResultSet resultSet = session.getStatement().executeQuery(msSql)) {
            if (resultSet.next()) {
                mnNumber = resultSet.getInt(1) + 1;
            }
        }
    }

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
    public void setTemplate(boolean b) { mbTemplate = b; }
    public void setTemplateCode(String s) { msTemplateCode = s; }
    public void setTemplateName(String s) { msTemplateName = s; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setFkTransportTypeId(int n) { mnFkTransportTypeId = n; }
    public void setFkBolStatusId(int n) { mnFkBolStatusId = n; }
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
    public boolean isTemplate() { return mbTemplate; }
    public String getTemplateCode() { return msTemplateCode; }
    public String getTemplateName() { return msTemplateName; }
    public boolean isDeleted() { return mbDeleted; }
    public int getFkTransportTypeId() { return mnFkTransportTypeId; }
    public int getFkBolStatusId() { return mnFkBolStatusId; }
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

    public void setOwnDfr(DDbDfr o) { moOwnDfr = o; }
    
    public DDbDfr getOwnDfr() { return moOwnDfr; }
    
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
        mbTemplate = false;
        msTemplateCode = "";
        msTemplateName = "";
        mbDeleted = false;
        mnFkTransportTypeId = 0;
        mnFkBolStatusId = 0;
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
        
        moOwnDfr = null;
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
            mbTemplate = resultSet.getBoolean("b_temp");
            msTemplateCode = resultSet.getString("temp_code");
            msTemplateName = resultSet.getString("temp_name");
            mbDeleted = resultSet.getBoolean("b_del");
            mnFkTransportTypeId = resultSet.getInt("fk_tpt_tp");
            mnFkBolStatusId = resultSet.getInt("fk_bol_st");
            mnFkIntlTransportCountryId = resultSet.getInt("fk_intl_tpt_cty");
            mnFkIntlWayTransportTypeId = resultSet.getInt("fk_intl_way_tpt_tp");
            mnFkMerchandiseWeightUnitId = resultSet.getInt("fk_merch_weigh_unt");
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
                    (mbTemplate ? 1 : 0) + ", " + 
                    "'" + msTemplateCode + "', " + 
                    "'" + msTemplateName + "', " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    mnFkTransportTypeId + ", " + 
                    mnFkBolStatusId + ", " + 
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
                    "b_temp = " + (mbTemplate ? 1 : 0) + ", " +
                    "temp_code = '" + msTemplateCode + "', " +
                    "temp_name = '" + msTemplateName + "', " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "fk_tpt_tp = " + mnFkTransportTypeId + ", " +
                    "fk_bol_st = " + mnFkBolStatusId + ", " +
                    "fk_intl_tpt_cty = " + mnFkIntlTransportCountryId + ", " +
                    "fk_intl_way_tpt_tp = " + mnFkIntlWayTransportTypeId + ", " +
                    "fk_merch_weigh_unt = " + mnFkMerchandiseWeightUnitId + ", " +
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

        // delete all child locations:
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_LOC) + " "
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
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_MERCH_MOVE) + " "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_MERCH) + " "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        for (DDbBolMerchandise bolMerchandise : maChildMerchandises) {
            bolMerchandise.setPkBolId(mnPkBolId);
            bolMerchandise.setRegistryNew(true);
            bolMerchandise.save(session);
        }
        
        // save as well child trucks:
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_TRUCK_TRAIL) + " "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_TRUCK) + " "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        for (DDbBolTruck bolTruck : maChildTrucks) {
            bolTruck.setPkBolId(mnPkBolId);
            bolTruck.setRegistryNew(true);
            bolTruck.save(session);
        }
        
        // save as well child transport figures:
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_TPT_FIGURE_TPT_PART) + " "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_TPT_FIGURE) + " "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        for (DDbBolTransportFigure bolTransportFigure : maChildTransportFigures) {
            bolTransportFigure.setPkBolId(mnPkBolId);
            bolTransportFigure.setRegistryNew(true);
            bolTransportFigure.save(session);
        }
        
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
        registry.setTemplate(this.isTemplate());
        registry.setTemplateCode(this.getTemplateCode());
        registry.setTemplateName(this.getTemplateName());
        registry.setDeleted(this.isDeleted());
        registry.setFkTransportTypeId(this.getFkTransportTypeId());
        registry.setFkBolStatusId(this.getFkBolStatusId());
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
        
        registry.setOwnDfr(this.getOwnDfr() == null ? null : this.getOwnDfr().clone());
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
