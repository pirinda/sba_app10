/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.lad.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.grid.DGridRow;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbBolLocation extends DDbRegistryUser implements DGridRow, DBolGridRow {

    protected int mnPkBolId;
    protected int mnPkLocationId;
    protected String msLocationId;
    protected Date mtArrivalDepartureDatetime;
    protected double mdDistanceKm;
    protected String msAddressStreet;
    protected String msAddressNumberExt;
    protected String msAddressNumberInt;
    protected String msAddressDistrictCode;
    protected String msAddressDistrictName;
    protected String msAddressLocalityCode;
    protected String msAddressLocalityName;
    protected String msAddressReference;
    protected String msAddressCountyCode;
    protected String msAddressCountyName;
    protected String msAddressStateCode;
    protected String msAddressStateName;
    protected String msAddressZipCode;
    protected String msNotes;
    protected int mnFkLocationTypeId;
    protected int mnFkLocationId;
    protected int mnFkAddressCountryId;
    protected int mnFkSourceBolId_n;
    protected int mnFkSourceLocationId_n;

    /** Location directly corresponding to member mnFkLocationId. */
    protected DDbLocation moOwnLocation;
    /** Location indirectly corresponding to members mnFkSourceBolId_n and mnFkSourceLocationId_n, when this is destiny BOL location. */
    protected DDbLocation moOwnSourceLocation;
    
    protected boolean mbBolUpdateOwnRegistry;
    protected int mnBolSortingPos;

    /** Helps maintaining relations between other BOL elements when creating in GUI and saving new BOL registries. */
    protected int mnTempBolLocationId;
    protected int mnTempSourceBolLocationId;
    
    public DDbBolLocation() {
        super(DModConsts.L_BOL_LOC);
        initRegistry();
    }

    public void setPkBolId(int n) { mnPkBolId = n; }
    public void setPkLocationId(int n) { mnPkLocationId = n; }
    public void setLocationId(String s) { msLocationId = s; }
    public void setArrivalDepartureDatetime(Date t) { mtArrivalDepartureDatetime = t; }
    public void setDistanceKm(double d) { mdDistanceKm = d; }
    public void setAddressStreet(String s) { msAddressStreet = s; }
    public void setAddressNumberExt(String s) { msAddressNumberExt = s; }
    public void setAddressNumberInt(String s) { msAddressNumberInt = s; }
    public void setAddressDistrictCode(String s) { msAddressDistrictCode = s; }
    public void setAddressDistrictName(String s) { msAddressDistrictName = s; }
    public void setAddressLocalityCode(String s) { msAddressLocalityCode = s; }
    public void setAddressLocalityName(String s) { msAddressLocalityName = s; }
    public void setAddressReference(String s) { msAddressReference = s; }
    public void setAddressCountyCode(String s) { msAddressCountyCode = s; }
    public void setAddressCountyName(String s) { msAddressCountyName = s; }
    public void setAddressStateCode(String s) { msAddressStateCode = s; }
    public void setAddressStateName(String s) { msAddressStateName = s; }
    public void setAddressZipCode(String s) { msAddressZipCode = s; }
    public void setNotes(String s) { msNotes = s; }
    public void setFkLocationTypeId(int n) { mnFkLocationTypeId = n; }
    public void setFkLocationId(int n) { mnFkLocationId = n; }
    public void setFkAddressCountryId(int n) { mnFkAddressCountryId = n; }
    public void setFkSourceBolId_n(int n) { mnFkSourceBolId_n = n; }
    public void setFkSourceLocationId_n(int n) { mnFkSourceLocationId_n = n; }

    public int getPkBolId() { return mnPkBolId; }
    public int getPkLocationId() { return mnPkLocationId; }
    public String getLocationId() { return msLocationId; }
    public Date getArrivalDepartureDatetime() { return mtArrivalDepartureDatetime; }
    public double getDistanceKm() { return mdDistanceKm; }
    public String getAddressStreet() { return msAddressStreet; }
    public String getAddressNumberExt() { return msAddressNumberExt; }
    public String getAddressNumberInt() { return msAddressNumberInt; }
    public String getAddressDistrictCode() { return msAddressDistrictCode; }
    public String getAddressDistrictName() { return msAddressDistrictName; }
    public String getAddressLocalityCode() { return msAddressLocalityCode; }
    public String getAddressLocalityName() { return msAddressLocalityName; }
    public String getAddressReference() { return msAddressReference; }
    public String getAddressCountyCode() { return msAddressCountyCode; }
    public String getAddressCountyName() { return msAddressCountyName; }
    public String getAddressStateCode() { return msAddressStateCode; }
    public String getAddressStateName() { return msAddressStateName; }
    public String getAddressZipCode() { return msAddressZipCode; }
    public String getNotes() { return msNotes; }
    public int getFkLocationTypeId() { return mnFkLocationTypeId; }
    public int getFkLocationId() { return mnFkLocationId; }
    public int getFkAddressCountryId() { return mnFkAddressCountryId; }
    public int getFkSourceBolId_n() { return mnFkSourceBolId_n; }
    public int getFkSourceLocationId_n() { return mnFkSourceLocationId_n; }
    
    public void setOwnLocation(DDbLocation o) { moOwnLocation = o; updateFromOwnLocation(); }
    public void setOwnSourceLocation(DDbLocation o) { moOwnSourceLocation = o; updateFromOwnSourceLocation(); }
    
    public DDbLocation getOwnLocation() { return moOwnLocation; }
    public DDbLocation getOwnSourceLocation() { return moOwnSourceLocation; }

    @Override
    public void setBolUpdateOwnRegistry(boolean update) { mbBolUpdateOwnRegistry = update; }
    @Override
    public void setBolSortingPos(int pos) { mnBolSortingPos = pos; }

    @Override
    public boolean isBolUpdateOwnRegistry() { return mbBolUpdateOwnRegistry; }
    @Override
    public int getBolSortingPos() { return mnBolSortingPos; }
    
    public void setTempBolLocationId(int n) { mnTempBolLocationId = n; }
    public void setTempSourceBolLocationId(int n) { mnTempSourceBolLocationId = n; }
    
    public int getTempBolLocationId() { return mnTempBolLocationId; }
    public int getTempSourceBolLocationId() { return mnTempSourceBolLocationId; }
    
    public int[] getSourceBolLocationKey() {
        return new int[] { mnFkSourceBolId_n, mnFkSourceLocationId_n };
    }
    
    public boolean isLocationSource() {
        return mnFkLocationTypeId == DModSysConsts.LS_LOC_TP_SRC;
    }
    
    public boolean isLocationDestiny() {
        return mnFkLocationTypeId == DModSysConsts.LS_LOC_TP_DES;
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBolId = pk[0];
        mnPkLocationId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBolId, mnPkLocationId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBolId = 0;
        mnPkLocationId = 0;
        msLocationId = "";
        mtArrivalDepartureDatetime = null;
        mdDistanceKm = 0;
        msAddressStreet = "";
        msAddressNumberExt = "";
        msAddressNumberInt = "";
        msAddressDistrictCode = "";
        msAddressDistrictName = "";
        msAddressLocalityCode = "";
        msAddressLocalityName = "";
        msAddressReference = "";
        msAddressCountyCode = "";
        msAddressCountyName = "";
        msAddressStateCode = "";
        msAddressStateName = "";
        msAddressZipCode = "";
        msNotes = "";
        mnFkLocationTypeId = 0;
        mnFkLocationId = 0;
        mnFkAddressCountryId = 0;
        mnFkSourceBolId_n = 0;
        mnFkSourceLocationId_n = 0;
        
        moOwnLocation = null;
        moOwnSourceLocation = null;
        
        mbBolUpdateOwnRegistry = false;
        mnBolSortingPos = 0;
        
        mnTempBolLocationId = 0;
        mnTempSourceBolLocationId = 0;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bol = " + mnPkBolId + " "
                + "AND id_loc = " + mnPkLocationId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bol = " + pk[0] + " "
                + "AND id_loc = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkLocationId = 0;

        msSql = "SELECT COALESCE(MAX(id_loc), 0) + 1 "
                + "FROM " + getSqlTable() + " "
                + "WHERE id_bol = " + mnPkBolId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkLocationId = resultSet.getInt(1);
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
            mnPkLocationId = resultSet.getInt("id_loc");
            msLocationId = resultSet.getString("loc_id");
            mtArrivalDepartureDatetime = resultSet.getTimestamp("arr_dep_dt");
            mdDistanceKm = resultSet.getDouble("dist_km");
            msAddressStreet = resultSet.getString("add_str");
            msAddressNumberExt = resultSet.getString("add_num_ext");
            msAddressNumberInt = resultSet.getString("add_num_int");
            msAddressDistrictCode = resultSet.getString("add_dist_code");
            msAddressDistrictName = resultSet.getString("add_dist_name");
            msAddressLocalityCode = resultSet.getString("add_loc_code");
            msAddressLocalityName = resultSet.getString("add_loc_name");
            msAddressReference = resultSet.getString("add_ref");
            msAddressCountyCode = resultSet.getString("add_cou_code");
            msAddressCountyName = resultSet.getString("add_cou_name");
            msAddressStateCode = resultSet.getString("add_ste_code");
            msAddressStateName = resultSet.getString("add_ste_name");
            msAddressZipCode = resultSet.getString("add_zip");
            msNotes = resultSet.getString("notes");
            mnFkLocationTypeId = resultSet.getInt("fk_loc_tp");
            mnFkLocationId = resultSet.getInt("fk_loc");
            mnFkAddressCountryId = resultSet.getInt("fk_add_cty");
            mnFkSourceBolId_n = resultSet.getInt("fk_src_bol_n");
            mnFkSourceLocationId_n = resultSet.getInt("fk_src_loc_n");
            
            moOwnLocation = (DDbLocation) session.readRegistry(DModConsts.LU_LOC, new int[] { mnFkLocationId });
            
            if (isLocationDestiny()) {
                msSql = "SELECT fk_loc "
                        + "FROM " + getSqlTable() + " "
                        + "WHERE fk_src_bol_n = " + mnFkSourceBolId_n + " AND fk_src_loc_n = " + mnFkSourceLocationId_n + ";";
                
                resultSet = session.getStatement().executeQuery(msSql);
                if (!resultSet.next()) {
                    throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
                }
                else {
                    moOwnSourceLocation = (DDbLocation) session.readRegistry(DModConsts.LU_LOC, new int[] { resultSet.getInt("fk_loc") });
                }
            }
            
            mnBolSortingPos = mnPkLocationId;
            
            mnTempBolLocationId = mnPkLocationId;
            mnTempSourceBolLocationId = mnFkSourceLocationId_n;
            
            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;
        
        // prepare dependencies:
        
        if (moOwnLocation == null) {
            throw new Exception(DGuiConsts.ERR_MSG_UNDEF_REG + " (" + DDbLocation.class.getName() + ")");
        }
        else if (moOwnLocation.isRegistryNew() || (moOwnLocation.isRegistryEdited() && mbBolUpdateOwnRegistry)) {
            moOwnLocation.save(session);
        }
        
        mnFkLocationId = moOwnLocation.getPkLocationId();
        
        if (isLocationDestiny() && moOwnSourceLocation == null) {
            throw new Exception(DGuiConsts.ERR_MSG_UNDEF_REG + " (" + DDbLocation.class.getName() + ")");
        }
        
        // save registry:

        if (mbRegistryNew) {
            if (mnPkLocationId == 0) {
                // location ID shold be already set, if not, compute a new one:
                computePrimaryKey(session);
            }

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkBolId + ", " + 
                    mnPkLocationId + ", " + 
                    "'" + msLocationId + "', " + 
                    "NOW()" + ", " + 
                    mdDistanceKm + ", " + 
                    "'" + msAddressStreet + "', " + 
                    "'" + msAddressNumberExt + "', " + 
                    "'" + msAddressNumberInt + "', " + 
                    "'" + msAddressDistrictCode + "', " + 
                    "'" + msAddressDistrictName + "', " + 
                    "'" + msAddressLocalityCode + "', " + 
                    "'" + msAddressLocalityName + "', " + 
                    "'" + msAddressReference + "', " + 
                    "'" + msAddressCountyCode + "', " + 
                    "'" + msAddressCountyName + "', " + 
                    "'" + msAddressStateCode + "', " + 
                    "'" + msAddressStateName + "', " + 
                    "'" + msAddressZipCode + "', " + 
                    "'" + msNotes + "', " + 
                    mnFkLocationTypeId + ", " + 
                    mnFkLocationId + ", " + 
                    mnFkAddressCountryId + ", " + 
                    (mnFkSourceBolId_n == 0 ? "NULL" : mnFkSourceBolId_n) + ", " + 
                    (mnFkSourceLocationId_n == 0 ? "NULL" : mnFkSourceLocationId_n) + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_bol = " + mnPkBolId + ", " +
                    //"id_loc = " + mnPkLocationId + ", " +
                    "loc_id = '" + msLocationId + "', " +
                    "arr_dep_dt = " + "NOW()" + ", " +
                    "dist_km = " + mdDistanceKm + ", " +
                    "add_str = '" + msAddressStreet + "', " +
                    "add_num_ext = '" + msAddressNumberExt + "', " +
                    "add_num_int = '" + msAddressNumberInt + "', " +
                    "add_dist_code = '" + msAddressDistrictCode + "', " +
                    "add_dist_name = '" + msAddressDistrictName + "', " +
                    "add_loc_code = '" + msAddressLocalityCode + "', " +
                    "add_loc_name = '" + msAddressLocalityName + "', " +
                    "add_ref = '" + msAddressReference + "', " +
                    "add_cou_code = '" + msAddressCountyCode + "', " +
                    "add_cou_name = '" + msAddressCountyName + "', " +
                    "add_ste_code = '" + msAddressStateCode + "', " +
                    "add_ste_name = '" + msAddressStateName + "', " +
                    "add_zip = '" + msAddressZipCode + "', " +
                    "notes = '" + msNotes + "', " +
                    "fk_loc_tp = " + mnFkLocationTypeId + ", " +
                    "fk_loc = " + mnFkLocationId + ", " +
                    "fk_add_cty = " + mnFkAddressCountryId + ", " +
                    "fk_src_bol_n = " + (mnFkSourceBolId_n == 0 ? "NULL" : mnFkSourceBolId_n) + ", " +
                    "fk_src_loc_n = " + (mnFkSourceLocationId_n == 0 ? "NULL" : mnFkSourceLocationId_n) + " " +
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        if (isLocationDestiny()) {
            // update distance in km to source:
            
            boolean update = true;
            DDbLocationDistance locationDistance = null;
            
            msSql = "SELECT COUNT(*) "
                    + "FROM " + DModConsts.TablesMap.get(DModConsts.LU_LOC_DIST) + " "
                    + "WHERE id_loc_src = " + moOwnSourceLocation.getPkLocationId() + " AND id_loc_des = " + mnFkLocationId + ";";
            
            try (Statement statement = session.getStatement().getConnection().createStatement()) {
                ResultSet resultSet = statement.executeQuery(msSql);
                if (resultSet.next() && resultSet.getInt(1) > 0) {
                    locationDistance = (DDbLocationDistance) session.readRegistry(DModConsts.LU_LOC_DIST, new int[] { moOwnSourceLocation.getPkLocationId(), mnFkLocationId });
                    if (locationDistance.getDistanceKm() == mdDistanceKm) {
                        update = false;
                    }
                }
            }
            
            if (update) {
                if (locationDistance == null) {
                    locationDistance = new DDbLocationDistance();
                    locationDistance.setPkLocationSourceId(moOwnSourceLocation.getPkLocationId());
                    locationDistance.setPkLocationDestinyId(mnFkLocationId);
                }
                
                locationDistance.setDistanceKm(mdDistanceKm);
                
                locationDistance.save(session);
            }
        }

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbBolLocation clone() throws CloneNotSupportedException {
        DDbBolLocation registry = new DDbBolLocation();

        registry.setPkBolId(this.getPkBolId());
        registry.setPkLocationId(this.getPkLocationId());
        registry.setLocationId(this.getLocationId());
        registry.setArrivalDepartureDatetime(this.getArrivalDepartureDatetime());
        registry.setDistanceKm(this.getDistanceKm());
        registry.setAddressStreet(this.getAddressStreet());
        registry.setAddressNumberExt(this.getAddressNumberExt());
        registry.setAddressNumberInt(this.getAddressNumberInt());
        registry.setAddressDistrictCode(this.getAddressDistrictCode());
        registry.setAddressDistrictName(this.getAddressDistrictName());
        registry.setAddressLocalityCode(this.getAddressLocalityCode());
        registry.setAddressLocalityName(this.getAddressLocalityName());
        registry.setAddressReference(this.getAddressReference());
        registry.setAddressCountyCode(this.getAddressCountyCode());
        registry.setAddressCountyName(this.getAddressCountyName());
        registry.setAddressStateCode(this.getAddressStateCode());
        registry.setAddressStateName(this.getAddressStateName());
        registry.setAddressZipCode(this.getAddressZipCode());
        registry.setNotes(this.getNotes());
        registry.setFkLocationTypeId(this.getFkLocationTypeId());
        registry.setFkLocationId(this.getFkLocationId());
        registry.setFkAddressCountryId(this.getFkAddressCountryId());
        registry.setFkSourceBolId_n(this.getFkSourceBolId_n());
        registry.setFkSourceLocationId_n(this.getFkSourceLocationId_n());
        
        registry.setOwnLocation(this.getOwnLocation()); // clone shares the same "read-only" object
        registry.setOwnSourceLocation(this.getOwnSourceLocation()); // clone shares the same "read-only" object
        
        registry.setBolUpdateOwnRegistry(this.isBolUpdateOwnRegistry());
        registry.setBolSortingPos(this.getBolSortingPos());
        
        registry.setTempBolLocationId(this.getTempBolLocationId());
        registry.setTempSourceBolLocationId(this.getTempSourceBolLocationId());
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    public void updateFromOwnLocation() {
        if (moOwnLocation != null) {
            setAddressStreet(moOwnLocation.getAddressStreet());
            setAddressNumberExt(moOwnLocation.getAddressNumberExt());
            setAddressNumberInt(moOwnLocation.getAddressNumberInt());
            setAddressDistrictCode(moOwnLocation.getAddressDistrictCode());
            setAddressDistrictName(moOwnLocation.getAddressDistrictName());
            setAddressLocalityCode(moOwnLocation.getAddressLocalityCode());
            setAddressLocalityName(moOwnLocation.getAddressLocalityName());
            setAddressReference(moOwnLocation.getAddressReference());
            setAddressCountyCode(moOwnLocation.getAddressCountyCode());
            setAddressCountyName(moOwnLocation.getAddressCountyName());
            setAddressStateCode(moOwnLocation.getAddressStateCode());
            setAddressStateName(moOwnLocation.getAddressStateName());
            setAddressZipCode(moOwnLocation.getAddressZipCode());
            
            setFkLocationTypeId(moOwnLocation.getFkLocationTypeId());
            setFkLocationId(moOwnLocation.getPkLocationId());
            setFkAddressCountryId(moOwnLocation.getFkAddressCountryId());
            
            switch (mnFkLocationTypeId) {
                case DModSysConsts.LS_LOC_TP_SRC:
                    setLocationId(moOwnLocation.getCodeSource());
                    break;
                case DModSysConsts.LS_LOC_TP_DES:
                    setLocationId(moOwnLocation.getCodeDestiny());
                    break;
                default:
                    setLocationId("");
            }
        }
    }
    
    public void updateFromOwnSourceLocation() {
        if (moOwnSourceLocation != null) {
            // nothing
        }
    }
    
    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return moOwnLocation != null ? moOwnLocation.getCode() : "";
    }

    @Override
    public String getRowName() {
        return moOwnLocation != null ? moOwnLocation.getName() : "";
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return true;
    }

    @Override
    public boolean isRowEdited() {
        return mbRegistryEdited;
    }

    @Override
    public void setRowEdited(boolean edited) {
        mbRegistryEdited = edited;
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = mnBolSortingPos;
                break;
            case 1:
                value = moOwnLocation.getDbmsLocationTypeName();
                break;
            case 2:
                value = moOwnLocation.getName();
                break;
            case 3:
                value = msLocationId;
                break;
            case 4:
                value = mtArrivalDepartureDatetime;
                break;
            case 5:
                value = isLocationDestiny() && moOwnSourceLocation != null ? moOwnSourceLocation.getName() : null;
                break;
            case 6:
                value = isLocationDestiny() ? mdDistanceKm : null;
                break;
            case 7:
                value = msNotes;
                break;
            default:
                // nothing
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
