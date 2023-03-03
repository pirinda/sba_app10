/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.lad.db;

import java.sql.ResultSet;
import java.sql.SQLException;
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
public class DDbBolTruckTrailer extends DDbRegistryUser implements DGridRow, DBolGridRow {

    protected int mnPkBolId;
    protected int mnPkTruckId;
    protected int mnPkTrailerId;
    protected String msPlate;
    protected String msTrailerSubtypeCode;
    protected String msTrailerSubtypeName;
    protected int mnFkTrailerId;
    
    protected DDbTrailer moOwnTrailer;
    
    protected boolean mbBolUpdateOwnRegistry;
    protected int mnBolSortingPos;

    public DDbBolTruckTrailer() {
        super(DModConsts.L_BOL_TRUCK_TRAIL);
        initRegistry();
    }

    public void setPkBolId(int n) { mnPkBolId = n; }
    public void setPkTruckId(int n) { mnPkTruckId = n; }
    public void setPkTrailerId(int n) { mnPkTrailerId = n; }
    public void setPlate(String s) { msPlate = s; }
    public void setTrailerSubtypeCode(String s) { msTrailerSubtypeCode = s; }
    public void setTrailerSubtypeName(String s) { msTrailerSubtypeName = s; }
    public void setFkTrailerId(int n) { mnFkTrailerId = n; }

    public int getPkBolId() { return mnPkBolId; }
    public int getPkTruckId() { return mnPkTruckId; }
    public int getPkTrailerId() { return mnPkTrailerId; }
    public String getPlate() { return msPlate; }
    public String getTrailerSubtypeCode() { return msTrailerSubtypeCode; }
    public String getTrailerSubtypeName() { return msTrailerSubtypeName; }
    public int getFkTrailerId() { return mnFkTrailerId; }
    
    public void setOwnTrailer(DDbTrailer o) { moOwnTrailer = o; updateFromOwnTrailer(); }
    
    public DDbTrailer getOwnTrailer() { return moOwnTrailer; }
    
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
        mnPkTrailerId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBolId, mnPkTruckId, mnPkTrailerId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBolId = 0;
        mnPkTruckId = 0;
        mnPkTrailerId = 0;
        msPlate = "";
        msTrailerSubtypeCode = "";
        msTrailerSubtypeName = "";
        mnFkTrailerId = 0;
        
        moOwnTrailer = null;
        
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
                + "AND id_truck = " + mnPkTruckId + " "
                + "AND id_trail = " + mnPkTrailerId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bol = " + pk[0] + " "
                + "AND id_truck = " + pk[1] + " "
                + "AND id_trail = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkTrailerId = 0;

        msSql = "SELECT COALESCE(MAX(id_trail), 0) + 1 "
                + "FROM " + getSqlTable() + " "
                + "WHERE id_bol = " + mnPkBolId + " "
                + "AND id_truck = " + mnPkTruckId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkTrailerId = resultSet.getInt(1);
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
            mnPkTrailerId = resultSet.getInt("id_trail");
            msPlate = resultSet.getString("plate");
            msTrailerSubtypeCode = resultSet.getString("trail_stp_code");
            msTrailerSubtypeName = resultSet.getString("trail_stp_name");
            mnFkTrailerId = resultSet.getInt("fk_trail");
            
            moOwnTrailer = (DDbTrailer) session.readRegistry(DModConsts.LU_TRAIL, new int[] { mnFkTrailerId });
            
            mnBolSortingPos = mnPkTrailerId;

            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        // prepare dependencies:
        
        if (moOwnTrailer == null) {
            throw new Exception(DGuiConsts.ERR_MSG_UNDEF_REG + " (Own " + DDbTrailer.class.getName() + ")");
        }
        else if (moOwnTrailer.isRegistryNew() || (moOwnTrailer.isRegistryEdited() && mbBolUpdateOwnRegistry)) {
            moOwnTrailer.save(session);
        }
        
        mnFkTrailerId = moOwnTrailer.getPkTrailerId();
        
        // save registry:

        if (mbRegistryNew) {
            computePrimaryKey(session);

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkBolId + ", " + 
                    mnPkTruckId + ", " + 
                    mnPkTrailerId + ", " + 
                    "'" + msPlate + "', " + 
                    "'" + msTrailerSubtypeCode + "', " + 
                    "'" + msTrailerSubtypeName + "', " + 
                    mnFkTrailerId + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_bol = " + mnPkBolId + ", " +
                    //"id_truck = " + mnPkTruckId + ", " +
                    //"id_trail = " + mnPkTrailerId + ", " +
                    "plate = '" + msPlate + "', " +
                    "trail_stp_code = '" + msTrailerSubtypeCode + "', " +
                    "trail_stp_name = '" + msTrailerSubtypeName + "', " +
                    "fk_trail = " + mnFkTrailerId + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbBolTruckTrailer clone() throws CloneNotSupportedException {
        DDbBolTruckTrailer registry = new DDbBolTruckTrailer();

        registry.setPkBolId(this.getPkBolId());
        registry.setPkTruckId(this.getPkTruckId());
        registry.setPkTrailerId(this.getPkTrailerId());
        registry.setPlate(this.getPlate());
        registry.setTrailerSubtypeCode(this.getTrailerSubtypeCode());
        registry.setTrailerSubtypeName(this.getTrailerSubtypeName());
        registry.setFkTrailerId(this.getFkTrailerId());
        
        registry.setOwnTrailer(this.getOwnTrailer()); // clone shares the same "read-only" object
        
        registry.setBolUpdateOwnRegistry(this.isBolUpdateOwnRegistry());
        registry.setBolSortingPos(this.getBolSortingPos());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    public void updateFromOwnTrailer() {
        if (moOwnTrailer != null) {
            setPlate(moOwnTrailer.getPlate());
            setTrailerSubtypeCode(moOwnTrailer.getTrailerSubtypeCode());
            setTrailerSubtypeName(moOwnTrailer.getTrailerSubtypeName());
            
            setFkTrailerId(moOwnTrailer.getPkTrailerId());
        }
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return moOwnTrailer != null ? moOwnTrailer.getCode() : "";
    }

    @Override
    public String getRowName() {
        return moOwnTrailer != null ? moOwnTrailer.getName() : "";
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
                value = msPlate;
                break;
            case 2:
                value = msTrailerSubtypeCode + " - " + msTrailerSubtypeName;
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
