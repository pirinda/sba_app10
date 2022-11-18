/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.lad.db;

import cfd.ver40.DCfdi40Catalogs;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.lad.form.DBolUtils;

/**
 *
 * @author Sergio Flores
 */
public class DDbLocation extends DDbRegistryUser {
    
    protected int mnPkLocationId;
    protected String msCode;
    protected String msCodeSource;
    protected String msCodeDestiny;
    protected String msName;
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
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkLocationTypeId;
    protected int mnFkAddressCountryId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected String msDbmsLocationTypeCode;
    protected String msDbmsLocationTypeName;

    public DDbLocation() {
        super(DModConsts.LU_LOC);
        initRegistry();
    }

    public void setPkLocationId(int n) { mnPkLocationId = n; }
    public void setCode(String s) { msCode = s; sanitize(); }
    //public void setCodeSource(String s) { msCodeSource = s; }
    //public void setCodeDestiny(String s) { msCodeDestiny = s; }
    public void setName(String s) { msName = s; }
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
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkLocationTypeId(int n) { mnFkLocationTypeId = n; }
    public void setFkAddressCountryId(int n) { mnFkAddressCountryId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkLocationId() { return mnPkLocationId; }
    public String getCode() { return msCode; }
    public String getCodeSource() { return msCodeSource; }
    public String getCodeDestiny() { return msCodeDestiny; }
    public String getName() { return msName; }
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
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkLocationTypeId() { return mnFkLocationTypeId; }
    public int getFkAddressCountryId() { return mnFkAddressCountryId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setDbmsLocationTypeCode(String s) { msDbmsLocationTypeCode = s; }
    public void setDbmsLocationTypeName(String s) { msDbmsLocationTypeName = s; }
    
    public String getDbmsLocationTypeCode() { return msDbmsLocationTypeCode; }
    public String getDbmsLocationTypeName() { return msDbmsLocationTypeName; }
    
    public String getDbmsLocationType() { return msDbmsLocationTypeCode + " - " + msDbmsLocationTypeName; }
    
    public String getLocationCode() {
        String properCode = "";
        
        switch (mnFkLocationTypeId) {
            case DModSysConsts.LS_LOC_TP_SRC:
                properCode = getCodeSource();
                break;
                
            case DModSysConsts.LS_LOC_TP_DES:
                properCode = getCodeDestiny();
                break;
                
            default:
                // nothing
        }
        
        return properCode;
    }
    
    public boolean isLocationSource() {
        return mnFkLocationTypeId == DModSysConsts.LS_LOC_TP_SRC;
    }
    
    public boolean isLocationDestiny() {
        return mnFkLocationTypeId == DModSysConsts.LS_LOC_TP_DES;
    }
    
    public boolean isLocationBidirectional() {
        return mnFkLocationTypeId == DModSysConsts.LS_LOC_TP_NA;
    }
    
    public void sanitize() {
        msCode = DBolUtils.FormatCodeLocation.format(DLibUtils.parseInt(msCode));
        msCodeSource = "";
        msCodeDestiny = "";
        
        if (isLocationSource() || isLocationBidirectional()) {
            msCodeSource = DCfdi40Catalogs.PrefijoClaveOrigen + msCode;
        }
        
        if (isLocationDestiny()|| isLocationBidirectional()) {
            msCodeDestiny = DCfdi40Catalogs.PrefijoClaveDestino + msCode;
        }
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkLocationId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkLocationId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkLocationId = 0;
        msCode = "";
        msCodeSource = "";
        msCodeDestiny = "";
        msName = "";
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
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkLocationTypeId = 0;
        mnFkAddressCountryId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        msDbmsLocationTypeCode = "";
        msDbmsLocationTypeName = "";
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_loc = " + mnPkLocationId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_loc = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkLocationId = 0;

        msSql = "SELECT COALESCE(MAX(id_loc), 0) + 1 "
                + "FROM " + getSqlTable();
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
            mnPkLocationId = resultSet.getInt("id_loc");
            msCode = resultSet.getString("code");
            msCodeSource = resultSet.getString("code_src");
            msCodeDestiny = resultSet.getString("code_des");
            msName = resultSet.getString("name");
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
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkLocationTypeId = resultSet.getInt("fk_loc_tp");
            mnFkAddressCountryId = resultSet.getInt("fk_add_cty");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            msDbmsLocationTypeCode = (String) session.readField(DModConsts.LU_LOC, new int[] { mnFkLocationTypeId }, DDbRegistry.FIELD_CODE);
            msDbmsLocationTypeName = (String) session.readField(DModConsts.LU_LOC, new int[] { mnFkLocationTypeId }, DDbRegistry.FIELD_NAME);

            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }
    
    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;
        
        sanitize();

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
                    mnPkLocationId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msCodeSource + "', " + 
                    "'" + msCodeDestiny + "', " + 
                    "'" + msName + "', " + 
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
                    (mbUpdatable ? 1 : 0) + ", " + 
                    (mbDisableable ? 1 : 0) + ", " + 
                    (mbDeletable ? 1 : 0) + ", " + 
                    (mbDisabled ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkLocationTypeId + ", " + 
                    mnFkAddressCountryId + ", " + 
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_loc = " + mnPkLocationId + ", " +
                    "code = '" + msCode + "', " +
                    "code_src = '" + msCodeSource + "', " +
                    "code_des = '" + msCodeDestiny + "', " +
                    "name = '" + msName + "', " +
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
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_loc_tp = " + mnFkLocationTypeId + ", " +
                    "fk_add_cty = " + mnFkAddressCountryId + ", " +
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
    public DDbLocation clone() throws CloneNotSupportedException {
        DDbLocation registry = new DDbLocation();

        registry.setPkLocationId(this.getPkLocationId());
        registry.setCode(this.getCode());
        //registry.setCodeSource(this.getCodeSource());
        //registry.setCodeDestiny(this.getCodeDestiny());
        registry.setName(this.getName());
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
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkLocationTypeId(this.getFkLocationTypeId());
        registry.setFkAddressCountryId(this.getFkAddressCountryId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setDbmsLocationTypeCode(this.getDbmsLocationTypeCode());
        registry.setDbmsLocationTypeName(this.getDbmsLocationTypeName());
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
