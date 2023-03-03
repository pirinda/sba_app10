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
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbBolMerchandiseMove extends DDbRegistryUser implements DGridRow, DBolGridRow {

    protected int mnPkBolId;
    protected int mnPkMerchandiseId;
    protected int mnPkMoveId;
    protected double mdQuantity;
    protected int mnFkSourceBolId;
    protected int mnFkSourceLocationId;
    protected int mnFkDestinyBolId;
    protected int mnFkDestinyLocationId;
    protected int mnFkTransportTypeId;

    /** Location indirectly corresponding to members mnFkSourceBolId and mnFkSourceLocationId. */
    protected DDbLocation moOwnSourceLocation;
    /** Location indirectly corresponding to members mnFkDestinyBolId and mnFkDestinyLocationId. */
    protected DDbLocation moOwnDestinyLocation;
    
    protected int mnBolSortingPos;
    
    /** Helps maintaining relations between other BOL elements when creating in GUI and saving new BOL registries. */
    protected int mnTempSourceBolLocationId;
    protected int mnTempDestinyBolLocationId;
    
    public DDbBolMerchandiseMove() {
        super(DModConsts.L_BOL_MERCH_MOVE);
        initRegistry();
    }

    public void setPkBolId(int n) { mnPkBolId = n; }
    public void setPkMerchandiseId(int n) { mnPkMerchandiseId = n; }
    public void setPkMoveId(int n) { mnPkMoveId = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setFkSourceBolId(int n) { mnFkSourceBolId = n; }
    public void setFkSourceLocationId(int n) { mnFkSourceLocationId = n; }
    public void setFkDestinyBolId(int n) { mnFkDestinyBolId = n; }
    public void setFkDestinyLocationId(int n) { mnFkDestinyLocationId = n; }
    public void setFkTransportTypeId(int n) { mnFkTransportTypeId = n; }

    public int getPkBolId() { return mnPkBolId; }
    public int getPkMerchandiseId() { return mnPkMerchandiseId; }
    public int getPkMoveId() { return mnPkMoveId; }
    public double getQuantity() { return mdQuantity; }
    public int getFkSourceBolId() { return mnFkSourceBolId; }
    public int getFkSourceLocationId() { return mnFkSourceLocationId; }
    public int getFkDestinyBolId() { return mnFkDestinyBolId; }
    public int getFkDestinyLocationId() { return mnFkDestinyLocationId; }
    public int getFkTransportTypeId() { return mnFkTransportTypeId; }
    
    public int[] getSourceLocationKey() { return new int[] { mnFkSourceBolId, mnFkSourceLocationId }; }
    public int[] getDestinyLocationKey() { return new int[] { mnFkDestinyBolId, mnFkDestinyLocationId }; }

    public void setOwnSourceLocation(DDbLocation o) { moOwnSourceLocation = o; }
    public void setOwnDestinyLocation(DDbLocation o) { moOwnDestinyLocation = o; }
    
    public DDbLocation getOwnSourceLocation() { return moOwnSourceLocation; }
    public DDbLocation getOwnDestinyLocation() { return moOwnDestinyLocation; }
    
    @Override
    public void setBolUpdateOwnRegistry(boolean update) { }
    @Override
    public void setBolSortingPos(int n) { mnBolSortingPos = n; }
    
    @Override
    public boolean isBolUpdateOwnRegistry() { return false; }
    @Override
    public int getBolSortingPos() { return mnBolSortingPos; }
    
    public void setTempSourceBolLocationId(int n) { mnTempSourceBolLocationId = n; }
    public void setTempDestinyBolLocationId(int n) { mnTempDestinyBolLocationId = n; }
    
    public int getTempSourceBolLocationId() { return mnTempSourceBolLocationId; }
    public int getTempDestinyBolLocationId() { return mnTempDestinyBolLocationId; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBolId = pk[0];
        mnPkMerchandiseId = pk[1];
        mnPkMoveId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBolId, mnPkMerchandiseId, mnPkMoveId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBolId = 0;
        mnPkMerchandiseId = 0;
        mnPkMoveId = 0;
        mdQuantity = 0;
        mnFkSourceBolId = 0;
        mnFkSourceLocationId = 0;
        mnFkDestinyBolId = 0;
        mnFkDestinyLocationId = 0;
        mnFkTransportTypeId = 0;
        
        moOwnSourceLocation = null;
        moOwnDestinyLocation = null;
        
        mnBolSortingPos = 0;
        
        mnTempSourceBolLocationId = 0;
        mnTempDestinyBolLocationId = 0;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bol = " + mnPkBolId + " "
                + "AND id_merch = " + mnPkMerchandiseId + " "
                + "AND id_move = " + mnPkMoveId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bol = " + pk[0] + " "
                + "AND id_merch = " + pk[1] + " "
                + "AND id_move = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkMoveId = 0;

        msSql = "SELECT COALESCE(MAX(id_move), 0) + 1 "
                + "FROM " + getSqlTable() + " "
                + "WHERE id_bol = " + mnPkBolId + " "
                + "AND id_merch = " + mnPkMerchandiseId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkMoveId = resultSet.getInt(1);
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
            mnPkMerchandiseId = resultSet.getInt("id_merch");
            mnPkMoveId = resultSet.getInt("id_move");
            mdQuantity = resultSet.getDouble("qty");
            mnFkSourceBolId = resultSet.getInt("fk_src_bol");
            mnFkSourceLocationId = resultSet.getInt("fk_src_loc");
            mnFkDestinyBolId = resultSet.getInt("fk_des_bol");
            mnFkDestinyLocationId = resultSet.getInt("fk_des_loc");
            mnFkTransportTypeId = resultSet.getInt("fk_tpt_tp");
            
            msSql = "SELECT fk_loc "
                    + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_LOC) + " "
                    + "WHERE id_bol = " + mnFkSourceBolId + " AND id_loc = " + mnFkSourceLocationId + ";";

            resultSet = session.getStatement().executeQuery(msSql);
            if (!resultSet.next()) {
                throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                moOwnSourceLocation = (DDbLocation) session.readRegistry(DModConsts.LU_LOC, new int[] { resultSet.getInt("fk_loc") });
            }

            msSql = "SELECT fk_loc "
                    + "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_LOC) + " "
                    + "WHERE id_bol = " + mnFkDestinyBolId + " AND id_loc = " + mnFkDestinyLocationId + ";";

            resultSet = session.getStatement().executeQuery(msSql);
            if (!resultSet.next()) {
                throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
            }
            else {
                moOwnDestinyLocation = (DDbLocation) session.readRegistry(DModConsts.LU_LOC, new int[] { resultSet.getInt("fk_loc") });
            }
            
            mnBolSortingPos = mnPkMoveId;
            
            mnTempSourceBolLocationId = mnFkSourceLocationId;
            mnTempDestinyBolLocationId = mnFkDestinyLocationId;

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

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkBolId + ", " + 
                    mnPkMerchandiseId + ", " + 
                    mnPkMoveId + ", " + 
                    mdQuantity + ", " + 
                    mnFkSourceBolId + ", " + 
                    mnFkSourceLocationId + ", " + 
                    mnFkDestinyBolId + ", " + 
                    mnFkDestinyLocationId + ", " + 
                    mnFkTransportTypeId + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_bol = " + mnPkBolId + ", " +
                    //"id_merch = " + mnPkMerchandiseId + ", " +
                    //"id_move = " + mnPkMoveId + ", " +
                    "qty = " + mdQuantity + ", " +
                    "fk_src_bol = " + mnFkSourceBolId + ", " +
                    "fk_src_loc = " + mnFkSourceLocationId + ", " +
                    "fk_des_bol = " + mnFkDestinyBolId + ", " +
                    "fk_des_loc = " + mnFkDestinyLocationId + ", " +
                    "fk_tpt_tp = " + mnFkTransportTypeId + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbBolMerchandiseMove clone() throws CloneNotSupportedException {
        DDbBolMerchandiseMove registry = new DDbBolMerchandiseMove();

        registry.setPkBolId(this.getPkBolId());
        registry.setPkMerchandiseId(this.getPkMerchandiseId());
        registry.setPkMoveId(this.getPkMoveId());
        registry.setQuantity(this.getQuantity());
        registry.setFkSourceBolId(this.getFkSourceBolId());
        registry.setFkSourceLocationId(this.getFkSourceLocationId());
        registry.setFkDestinyBolId(this.getFkDestinyBolId());
        registry.setFkDestinyLocationId(this.getFkDestinyLocationId());
        registry.setFkTransportTypeId(this.getFkTransportTypeId());

        registry.setOwnSourceLocation(this.getOwnSourceLocation()); // clone shares the same "read-only" object
        registry.setOwnDestinyLocation(this.getOwnDestinyLocation()); // clone shares the same "read-only" object
        
        registry.setBolUpdateOwnRegistry(this.isBolUpdateOwnRegistry());
        registry.setBolSortingPos(this.getBolSortingPos());
        
        registry.setTempSourceBolLocationId(this.getTempSourceBolLocationId());
        registry.setTempDestinyBolLocationId(this.getTempDestinyBolLocationId());
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return moOwnSourceLocation.getCodeSource() + " - " + moOwnDestinyLocation.getCodeDestiny();
    }

    @Override
    public String getRowName() {
        return moOwnSourceLocation.getName()+ " - " + moOwnDestinyLocation.getName();
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
                value = moOwnSourceLocation.getName();
                break;
            case 2:
                value = moOwnSourceLocation.getCodeSource();
                break;
            case 3:
                value = moOwnDestinyLocation.getName();
                break;
            case 4:
                value = moOwnDestinyLocation.getCodeDestiny();
                break;
            case 5:
                value = mdQuantity;
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
