/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.lad.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.grid.DGridRow;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbBolTruck extends DDbRegistryUser implements DGridRow, DBolGridRow {

    protected int mnPkBolId;
    protected int mnPkTruckId;
    protected double mdWeightTon;
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
    protected int mnFkTruckId;
    
    ArrayList<DDbBolTruckTrailer> maChildTrailers;
    
    protected DDbTruck moOwnTruck;
    
    protected boolean mbBolUpdateOwnRegistry;
    protected int mnBolSortingPos;

    public DDbBolTruck() {
        super(DModConsts.L_BOL_TRUCK);
        maChildTrailers = new ArrayList<>();
        initRegistry();
    }

    public void setPkBolId(int n) { mnPkBolId = n; }
    public void setPkTruckId(int n) { mnPkTruckId = n; }
    public void setWeightTon(double d) { mdWeightTon = d; }
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
    public void setFkTruckId(int n) { mnFkTruckId = n; }

    public int getPkBolId() { return mnPkBolId; }
    public int getPkTruckId() { return mnPkTruckId; }
    public double getWeightTon() { return mdWeightTon; }
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
    public int getFkTruckId() { return mnFkTruckId; }

    public ArrayList<DDbBolTruckTrailer> getChildTrailers() { return maChildTrailers; }
    
    public void setOwnTruck(DDbTruck o) { moOwnTruck = o; updateFromOwnTruck(); }
    
    public DDbTruck getOwnTruck() { return moOwnTruck; }
    
    @Override
    public void setBolUpdateOwnRegistry(boolean update) { mbBolUpdateOwnRegistry = update; }
    @Override
    public void setBolSortingPos(int n) { mnBolSortingPos = n; }
    
    @Override
    public boolean isBolUpdateOwnRegistry() { return mbBolUpdateOwnRegistry; }
    @Override
    public int getBolSortingPos() { return mnBolSortingPos; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBolId = pk[0];
        mnPkTruckId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBolId, mnPkTruckId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBolId = 0;
        mnPkTruckId = 0;
        mdWeightTon = 0;
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
        mnFkTruckId = 0;
        
        maChildTrailers.clear();
        
        moOwnTruck = null;
        
        mbBolUpdateOwnRegistry = false;
        mnBolSortingPos = 0;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bol = " + mnPkBolId + " "
                + "AND id_truck = " + mnPkTruckId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bol = " + pk[0] + " "
                + "AND id_truck = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkTruckId = 0;

        msSql = "SELECT COALESCE(MAX(id_truck), 0) + 1 "
                + "FROM " + getSqlTable() + " "
                + "WHERE id_bol = " + mnPkBolId + " ";
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
            mnPkBolId = resultSet.getInt("id_bol");
            mnPkTruckId = resultSet.getInt("id_truck");
            mdWeightTon = resultSet.getDouble("weight_ton");
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
            mnFkTruckId = resultSet.getInt("fk_truck");
            
            // read as well child trailers:
            
            msSql = "SELECT id_trail "
                    + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_TRUCK_TRAIL) + " "
                    + getSqlWhere();
            
            try (Statement statement = session.getStatement().getConnection().createStatement()) {
                resultSet = statement.executeQuery(msSql);
                while (resultSet.next()) {
                    DDbBolTruckTrailer trailer = (DDbBolTruckTrailer) session.readRegistry(DModConsts.L_BOL_TRUCK_TRAIL, new int[] { mnPkBolId, mnPkTruckId, resultSet.getInt("id_trail") });
                    maChildTrailers.add(trailer);
                }
            }
            
            moOwnTruck = (DDbTruck) session.readRegistry(DModConsts.LU_TRUCK, new int[] { mnFkTruckId });
            
            mnBolSortingPos = mnPkTruckId;

            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        // prepare dependencies:
        
        if (moOwnTruck == null) {
            throw new Exception(DGuiConsts.ERR_MSG_UNDEF_REG + " (Own " + DDbTruck.class.getName() + ")");
        }
        else if (moOwnTruck.isRegistryNew() || (moOwnTruck.isRegistryEdited() && mbBolUpdateOwnRegistry)) {
            // assure that all trailers are already saved:
            
            int index = 0;
            for (DDbBolTruckTrailer trailer : maChildTrailers) {
                if (trailer.getOwnTrailer() != null) {
                    if (trailer.getOwnTrailer().isRegistryNew()) {
                        trailer.getOwnTrailer().save(session);
                    }
                    moOwnTruck.getChildTrailers().get(index).setFkTrailerId(trailer.getOwnTrailer().getPkTrailerId());
                }
                index++;
            }
            
            moOwnTruck.save(session);
        }
        
        mnFkTruckId = moOwnTruck.getPkTruckId();
        
        // save registry:

        if (mbRegistryNew) {
            computePrimaryKey(session);

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkBolId + ", " + 
                    mnPkTruckId + ", " + 
                    mdWeightTon + ", " + 
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
                    mnFkTruckId + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_bol = " + mnPkBolId + ", " +
                    //"id_truck = " + mnPkTruckId + ", " +
                    "weight_ton = " + mdWeightTon + ", " +
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
                    "fk_truck = " + mnFkTruckId + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        // save as well child trailers:
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_TRUCK_TRAIL) + " "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        for (DDbBolTruckTrailer trailer : maChildTrailers) {
            trailer.setPkBolId(mnPkBolId);
            trailer.setPkTruckId(mnPkTruckId);
            trailer.setRegistryNew(true);
            trailer.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbBolTruck clone() throws CloneNotSupportedException {
        DDbBolTruck registry = new DDbBolTruck();

        registry.setPkBolId(this.getPkBolId());
        registry.setPkTruckId(this.getPkTruckId());
        registry.setWeightTon(this.getWeightTon());
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
        registry.setFkTruckId(this.getFkTruckId());

        // clone as well child trailers:
        
        for (DDbBolTruckTrailer trailer : maChildTrailers) {
            registry.getChildTrailers().add(trailer.clone());
        }
        
        registry.setOwnTruck(this.getOwnTruck()); // clone shares the same "read-only" object
        
        registry.setBolUpdateOwnRegistry(this.isBolUpdateOwnRegistry());
        registry.setBolSortingPos(this.getBolSortingPos());
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    public void updateFromOwnTruck() {
        if (moOwnTruck != null) {
            setWeightTon(moOwnTruck.getWeightTon());
            setPlate(moOwnTruck.getPlate());
            setModel(moOwnTruck.getModel());
            setTransportConfigCode(moOwnTruck.getTransportConfigCode());
            setTransportConfigName(moOwnTruck.getTransportConfigName());
            setPermissionTypeCode(moOwnTruck.getPermissionTypeCode());
            setPermissionTypeName(moOwnTruck.getPermissionTypeName());
            setPermissionNumber(moOwnTruck.getPermissionNumber());
            setCivilInsurance(moOwnTruck.getCivilInsurance());
            setCivilPolicy(moOwnTruck.getCivilPolicy());
            setEnvironmentInsurance(moOwnTruck.getEnvironmentInsurance());
            setEnvironmentPolicy(moOwnTruck.getEnvironmentPolicy());
            setCargoInsurance(moOwnTruck.getCargoInsurance());
            setCargoPolicy(moOwnTruck.getCargoPolicy());
            setPrime(moOwnTruck.getPrime());
            
            setFkTruckId(moOwnTruck.getPkTruckId());
        }
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return moOwnTruck != null ? moOwnTruck.getName() : "";
    }

    @Override
    public String getRowName() {
        return moOwnTruck != null ? moOwnTruck.getName() : "";
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
                value = moOwnTruck != null ? moOwnTruck.getName() : "";
                break;
            case 2:
                value = moOwnTruck != null ? moOwnTruck.getCode() : "";
                break;
            case 3:
                value = mdWeightTon;
                break;
            case 4:
                value = msPlate;
                break;
            case 5:
                value = mnModel;
                break;
            case 6:
                value = msTransportConfigCode + " - " + msTransportConfigName;
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
