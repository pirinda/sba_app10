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
public class DDbBolTransportFigureTransportPart extends DDbRegistryUser implements DGridRow, DBolGridRow {

    protected int mnPkBolId;
    protected int mnPkTransportFigureId;
    protected int mnPkTransportPartId;
    protected int mnFkTransportPartTypeId;
    
    protected DDbSysTransportPartType moOwnSysTransportPartType;
    
    protected boolean mbBolUpdateOwnRegistry;
    protected int mnBolSortingPos;

    public DDbBolTransportFigureTransportPart() {
        super(DModConsts.L_BOL_TPT_FIGURE_TPT_PART);
        initRegistry();
    }

    public void setPkBolId(int n) { mnPkBolId = n; }
    public void setPkTransportFigureId(int n) { mnPkTransportFigureId = n; }
    public void setPkTransportPartId(int n) { mnPkTransportPartId = n; }
    public void setFkTransportPartTypeId(int n) { mnFkTransportPartTypeId = n; }

    public int getPkBolId() { return mnPkBolId; }
    public int getPkTransportFigureId() { return mnPkTransportFigureId; }
    public int getPkTransportPartId() { return mnPkTransportPartId; }
    public int getFkTransportPartTypeId() { return mnFkTransportPartTypeId; }
    
    public void setOwnSysTransportPartType(final DDbSysTransportPartType o) { moOwnSysTransportPartType = o; updateFromOwnSysTransportPartType(); }
    
    public DDbSysTransportPartType getOwnSysTransportPartType() { return moOwnSysTransportPartType; }
    
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
        mnPkTransportFigureId = pk[1];
        mnPkTransportPartId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBolId, mnPkTransportFigureId, mnPkTransportPartId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBolId = 0;
        mnPkTransportFigureId = 0;
        mnPkTransportPartId = 0;
        mnFkTransportPartTypeId = 0;
        
        moOwnSysTransportPartType = null;
        
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
                + "AND id_tpt_figure = " + mnPkTransportFigureId + " "
                + "AND id_tpt_part = " + mnPkTransportPartId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bol = " + pk[0] + " "
                + "AND id_tpt_figure = " + pk[1] + " "
                + "AND id_tpt_part = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkTransportPartId = 0;

        msSql = "SELECT COALESCE(MAX(id_tpt_part), 0) + 1 "
                + "FROM " + getSqlTable() + " "
                + "WHERE id_bol = " + mnPkBolId + " "
                + "AND id_tpt_figure = " + mnPkTransportFigureId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkTransportPartId = resultSet.getInt(1);
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
            mnPkTransportFigureId = resultSet.getInt("id_tpt_figure");
            mnPkTransportPartId = resultSet.getInt("id_tpt_part");
            mnFkTransportPartTypeId = resultSet.getInt("fk_tpt_part_tp");
            
            moOwnSysTransportPartType = (DDbSysTransportPartType) session.readRegistry(DModConsts.LS_TPT_PART_TP, new int[] { mnFkTransportPartTypeId });
            
            mnBolSortingPos = mnPkTransportPartId;

            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        // prepare dependencies:
        
        if (moOwnSysTransportPartType == null) {
            throw new Exception(DGuiConsts.ERR_MSG_UNDEF_REG + " (Own " + DDbSysTransportPartType.class.getName() + ")");
        }
        else if (moOwnSysTransportPartType.isRegistryNew() || (moOwnSysTransportPartType.isRegistryEdited() && mbBolUpdateOwnRegistry)) {
            throw new Exception(DDbConsts.MSG_REG_DENIED_UPDATE + " (Own " + DDbSysTransportPartType.class.getName() + ")");
        }
        
        mnFkTransportPartTypeId = moOwnSysTransportPartType.getPkTypeId();
        
        // save registry:

        if (mbRegistryNew) {
            computePrimaryKey(session);

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkBolId + ", " + 
                    mnPkTransportFigureId + ", " + 
                    mnPkTransportPartId + ", " + 
                    mnFkTransportPartTypeId + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_bol = " + mnPkBolId + ", " +
                    //"id_tpt_figure = " + mnPkTransportFigureId + ", " +
                    //"id_tpt_part = " + mnPkTransportPartId + ", " +
                    "fk_tpt_part_tp = " + mnFkTransportPartTypeId + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbBolTransportFigureTransportPart clone() throws CloneNotSupportedException {
        DDbBolTransportFigureTransportPart registry = new DDbBolTransportFigureTransportPart();

        registry.setPkBolId(this.getPkBolId());
        registry.setPkTransportFigureId(this.getPkTransportFigureId());
        registry.setPkTransportPartId(this.getPkTransportPartId());
        registry.setFkTransportPartTypeId(this.getFkTransportPartTypeId());
        
        registry.setOwnSysTransportPartType(this.getOwnSysTransportPartType()); // clone shares the same "read-only" object
        
        registry.setBolUpdateOwnRegistry(this.isBolUpdateOwnRegistry());
        registry.setBolSortingPos(this.getBolSortingPos());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    public void updateFromOwnSysTransportPartType() {
        if (moOwnSysTransportPartType != null) {
            setFkTransportPartTypeId(moOwnSysTransportPartType.getPkTypeId());
        }
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return moOwnSysTransportPartType != null ? moOwnSysTransportPartType.getCode() : "";
    }

    @Override
    public String getRowName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                value = moOwnSysTransportPartType != null ? moOwnSysTransportPartType.getCode() + " - " + moOwnSysTransportPartType.getName() : "";
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
