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
    
    protected DDbTransportPartType moOwnTransportPartType;
    
    protected int mnAuxSortingPos;

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
    
    public void setOwnTransportPartType(DDbTransportPartType o) { moOwnTransportPartType = o; updateFromOwnTransportPartType(); }
    
    public DDbTransportPartType getOwnTransportPartType() { return moOwnTransportPartType; }
    
    public void setAuxSortingPos(int n) { mnAuxSortingPos = n; }
    
    public int getAuxSortingPos() { return mnAuxSortingPos; }

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
        
        moOwnTransportPartType = null;
        
        mnAuxSortingPos = 0;
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
            
            moOwnTransportPartType = (DDbTransportPartType) session.readRegistry(DModConsts.LS_TPT_PART_TP, new int[] { mnFkTransportPartTypeId });
            
            mnAuxSortingPos = mnPkTransportPartId;

            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        // prepare dependencies:
        
        if (moOwnTransportPartType == null) {
            throw new Exception(DGuiConsts.ERR_MSG_UNDEF_REG + " (Own " + DDbTransportPartType.class.getName() + ")");
        }
        else if (moOwnTransportPartType.isRegistryNew() || moOwnTransportPartType.isRegistryEdited()) {
            throw new Exception(DDbConsts.MSG_REG_DENIED_UPDATE + " (Own " + DDbTransportPartType.class.getName() + ")");
        }
        
        mnFkTransportPartTypeId = moOwnTransportPartType.getPkTypeId();
        
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
        
        registry.setOwnTransportPartType(this.getOwnTransportPartType()); // clone shares the same "read-only" object
        
        registry.setAuxSortingPos(this.getAuxSortingPos());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
    
    public void updateFromOwnTransportPartType() {
        if (moOwnTransportPartType != null) {
            setFkTransportPartTypeId(moOwnTransportPartType.getPkTypeId());
        }
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return moOwnTransportPartType != null ? moOwnTransportPartType.getCode() : "";
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
                value = mnAuxSortingPos;
                break;
            case 1:
                value = moOwnTransportPartType != null ? moOwnTransportPartType.getCode() + " - " + moOwnTransportPartType.getName() : "";
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
