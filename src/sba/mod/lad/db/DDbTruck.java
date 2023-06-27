/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.lad.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import sba.gui.util.DUtilConsts;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbTruck extends DDbRegistryUser {

    protected int mnPkTruckId;
    protected String msCode;
    protected String msName;
    protected String msPlate;
    protected int mnModel;
    protected String msTransportConfigCode;
    protected String msTransportConfigName;
    protected String msPermissionTypeCode;
    protected String msPermissionTypeName;
    protected String msPermissionNumber;
    protected String msCivilInsurance;
    protected String msCivilPolicy;
    protected String msEnvironmentInsurance;
    protected String msEnvironmentPolicy;
    protected String msCargoInsurance;
    protected String msCargoPolicy;
    protected double mdPrime;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    ArrayList<DDbTruckTrailer> maChildTrailers;
    ArrayList<DDbTruckTransportFigure> maChildTransportFigures;

    public DDbTruck() {
        super(DModConsts.LU_TRUCK);
        maChildTrailers = new ArrayList<>();
        maChildTransportFigures = new ArrayList<>();
        initRegistry();
    }

    public void setPkTruckId(int n) { mnPkTruckId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setPlate(String s) { msPlate = s; }
    public void setModel(int n) { mnModel = n; }
    public void setTransportConfigCode(String s) { msTransportConfigCode = s; }
    public void setTransportConfigName(String s) { msTransportConfigName = s; }
    public void setPermissionTypeCode(String s) { msPermissionTypeCode = s; }
    public void setPermissionTypeName(String s) { msPermissionTypeName = s; }
    public void setPermissionNumber(String s) { msPermissionNumber = s; }
    public void setCivilInsurance(String s) { msCivilInsurance = s; }
    public void setCivilPolicy(String s) { msCivilPolicy = s; }
    public void setEnvironmentInsurance(String s) { msEnvironmentInsurance = s; }
    public void setEnvironmentPolicy(String s) { msEnvironmentPolicy = s; }
    public void setCargoInsurance(String s) { msCargoInsurance = s; }
    public void setCargoPolicy(String s) { msCargoPolicy = s; }
    public void setPrime(double d) { mdPrime = d; }

    public int getPkTruckId() { return mnPkTruckId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getPlate() { return msPlate; }
    public int getModel() { return mnModel; }
    public String getTransportConfigCode() { return msTransportConfigCode; }
    public String getTransportConfigName() { return msTransportConfigName; }
    public String getPermissionTypeCode() { return msPermissionTypeCode; }
    public String getPermissionTypeName() { return msPermissionTypeName; }
    public String getPermissionNumber() { return msPermissionNumber; }
    public String getCivilInsurance() { return msCivilInsurance; }
    public String getCivilPolicy() { return msCivilPolicy; }
    public String getEnvironmentInsurance() { return msEnvironmentInsurance; }
    public String getEnvironmentPolicy() { return msEnvironmentPolicy; }
    public String getCargoInsurance() { return msCargoInsurance; }
    public String getCargoPolicy() { return msCargoPolicy; }
    public double getPrime() { return mdPrime; }

    public ArrayList<DDbTruckTrailer> getChildTrailers() { return maChildTrailers; }
    public ArrayList<DDbTruckTransportFigure> getChildTransportFigures() { return maChildTransportFigures; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkTruckId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkTruckId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkTruckId = 0;
        msCode = "";
        msName = "";
        msPlate = "";
        mnModel = 0;
        msTransportConfigCode = "";
        msTransportConfigName = "";
        msPermissionTypeCode = "";
        msPermissionTypeName = "";
        msPermissionNumber = "";
        msCivilInsurance = "";
        msCivilPolicy = "";
        msEnvironmentInsurance = "";
        msEnvironmentPolicy = "";
        msCargoInsurance = "";
        msCargoPolicy = "";
        mdPrime = 0;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        maChildTrailers.clear();
        maChildTransportFigures.clear();
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_truck = " + mnPkTruckId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_truck = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkTruckId = 0;

        msSql = "SELECT COALESCE(MAX(id_truck), 0) + 1 "
                + "FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkTruckId = resultSet.getInt(1);
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
            mnPkTruckId = resultSet.getInt("id_truck");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msPlate = resultSet.getString("plate");
            mnModel = resultSet.getInt("model");
            msTransportConfigCode = resultSet.getString("tpt_config_code");
            msTransportConfigName = resultSet.getString("tpt_config_name");
            msPermissionTypeCode = resultSet.getString("perm_tp_code");
            msPermissionTypeName = resultSet.getString("perm_tp_name");
            msPermissionNumber = resultSet.getString("perm_num");
            msCivilInsurance = resultSet.getString("civil_insurance");
            msCivilPolicy = resultSet.getString("civil_policy");
            msEnvironmentInsurance = resultSet.getString("envir_insurance");
            msEnvironmentPolicy = resultSet.getString("envir_policy");
            msCargoInsurance = resultSet.getString("cargo_insurance");
            msCargoPolicy = resultSet.getString("cargo_policy");
            mdPrime = resultSet.getDouble("prime");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            // read as well child trailers:
            
            msSql = "SELECT id_trail "
                    + "FROM " + DModConsts.TablesMap.get(DModConsts.LU_TRUCK_TRAIL) + " "
                    + getSqlWhere();
            
            try (Statement statement = session.getStatement().getConnection().createStatement()) {
                resultSet = statement.executeQuery(msSql);
                while (resultSet.next()) {
                    DDbTruckTrailer trailer = (DDbTruckTrailer) session.readRegistry(DModConsts.LU_TRUCK_TRAIL, new int[] { mnPkTruckId, resultSet.getInt("id_trail") });
                    maChildTrailers.add(trailer);
                }
            }

            // read as well child transport figures:
            
            msSql = "SELECT id_tpt_figure "
                    + "FROM " + DModConsts.TablesMap.get(DModConsts.LU_TRUCK_TPT_FIGURE) + " "
                    + getSqlWhere();
            
            try (Statement statement = session.getStatement().getConnection().createStatement()) {
                resultSet = statement.executeQuery(msSql);
                while (resultSet.next()) {
                    DDbTruckTransportFigure transportFigure = (DDbTruckTransportFigure) session.readRegistry(DModConsts.LU_TRUCK_TPT_FIGURE, new int[] { mnPkTruckId, resultSet.getInt("id_tpt_figure") });
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
                    mnPkTruckId + ", " + 
                    "'" + msCode + "', " + 
                    "'" + msName + "', " + 
                    "'" + msPlate + "', " + 
                    mnModel + ", " + 
                    "'" + msTransportConfigCode + "', " + 
                    "'" + msTransportConfigName + "', " + 
                    "'" + msPermissionTypeCode + "', " + 
                    "'" + msPermissionTypeName + "', " + 
                    "'" + msPermissionNumber + "', " + 
                    "'" + msCivilInsurance + "', " + 
                    "'" + msCivilPolicy + "', " + 
                    "'" + msEnvironmentInsurance + "', " + 
                    "'" + msEnvironmentPolicy + "', " + 
                    "'" + msCargoInsurance + "', " + 
                    "'" + msCargoPolicy + "', " + 
                    mdPrime + ", " + 
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_truck = " + mnPkTruckId + ", " +
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "plate = '" + msPlate + "', " +
                    "model = " + mnModel + ", " +
                    "tpt_config_code = '" + msTransportConfigCode + "', " +
                    "tpt_config_name = '" + msTransportConfigName + "', " +
                    "perm_tp_code = '" + msPermissionTypeCode + "', " +
                    "perm_tp_name = '" + msPermissionTypeName + "', " +
                    "perm_num = '" + msPermissionNumber + "', " +
                    "civil_insurance = '" + msCivilInsurance + "', " +
                    "civil_policy = '" + msCivilPolicy + "', " +
                    "envir_insurance = '" + msEnvironmentInsurance + "', " +
                    "envir_policy = '" + msEnvironmentPolicy + "', " +
                    "cargo_insurance = '" + msCargoInsurance + "', " +
                    "cargo_policy = '" + msCargoPolicy + "', " +
                    "prime = " + mdPrime + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        // save as well child trailers:
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.LU_TRUCK_TRAIL) + " "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        for (DDbTruckTrailer trailer : maChildTrailers) {
            trailer.setPkTruckId(mnPkTruckId);
            trailer.setRegistryNew(true);
            trailer.save(session);
        }
        
        // save as well child transport figures:
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.LU_TRUCK_TPT_FIGURE_TPT_PART) + " "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.LU_TRUCK_TPT_FIGURE) + " "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        for (DDbTruckTransportFigure transportFigure : maChildTransportFigures) {
            transportFigure.setPkTruckId(mnPkTruckId);
            transportFigure.setRegistryNew(true);
            transportFigure.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbTruck clone() throws CloneNotSupportedException {
        DDbTruck registry = new DDbTruck();

        registry.setPkTruckId(this.getPkTruckId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setPlate(this.getPlate());
        registry.setModel(this.getModel());
        registry.setTransportConfigCode(this.getTransportConfigCode());
        registry.setTransportConfigName(this.getTransportConfigName());
        registry.setPermissionTypeCode(this.getPermissionTypeCode());
        registry.setPermissionTypeName(this.getPermissionTypeName());
        registry.setPermissionNumber(this.getPermissionNumber());
        registry.setCivilInsurance(this.getCivilInsurance());
        registry.setCivilPolicy(this.getCivilPolicy());
        registry.setEnvironmentInsurance(this.getEnvironmentInsurance());
        registry.setEnvironmentPolicy(this.getEnvironmentPolicy());
        registry.setCargoInsurance(this.getCargoInsurance());
        registry.setCargoPolicy(this.getCargoPolicy());
        registry.setPrime(this.getPrime());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        // clone as well child trailers:
        
        for (DDbTruckTrailer trailer : maChildTrailers) {
            registry.getChildTrailers().add(trailer.clone());
        }
        
        // save as well child transport figures:
        
        for (DDbTruckTransportFigure transportFigure : maChildTransportFigures) {
            registry.getChildTransportFigures().add(transportFigure.clone());
        }
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
