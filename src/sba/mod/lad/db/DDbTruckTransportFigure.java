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
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbTruckTransportFigure extends DDbRegistryUser {

    protected int mnPkTruckId;
    protected int mnPkTransportFigureId;
    protected int mnFkTransportFigureId;
    
    protected ArrayList<DDbTruckTransportFigureTransportPart> maChildTransportParts;

    public DDbTruckTransportFigure() {
        super(DModConsts.LU_TRUCK_TPT_FIGURE);
        maChildTransportParts = new ArrayList<>();
        initRegistry();
    }

    public void setPkTruckId(int n) { mnPkTruckId = n; }
    public void setPkTransportFigureId(int n) { mnPkTransportFigureId = n; }
    public void setFkTransportFigureId(int n) { mnFkTransportFigureId = n; }

    public int getPkTruckId() { return mnPkTruckId; }
    public int getPkTransportFigureId() { return mnPkTransportFigureId; }
    public int getFkTransportFigureId() { return mnFkTransportFigureId; }
    
    public ArrayList<DDbTruckTransportFigureTransportPart> getChildTransportParts() { return maChildTransportParts; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkTruckId = pk[0];
        mnPkTransportFigureId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkTruckId, mnPkTransportFigureId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkTruckId = 0;
        mnPkTransportFigureId = 0;
        mnFkTransportFigureId = 0;
        
        maChildTransportParts.clear();
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_truck = " + mnPkTruckId + " "
                + "AND id_tpt_figure = " + mnPkTransportFigureId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_truck = " + pk[0] + " "
                + "AND id_tpt_figure = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkTransportFigureId = 0;

        msSql = "SELECT COALESCE(MAX(id_tpt_figure), 0) + 1 "
                + "FROM " + getSqlTable() + " "
                + "WHERE id_truck = " + mnPkTruckId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkTransportFigureId = resultSet.getInt(1);
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
            mnPkTransportFigureId = resultSet.getInt("id_tpt_figure");
            mnFkTransportFigureId = resultSet.getInt("fk_tpt_figure");
            
            // read as well child transport parts:
            
            msSql = "SELECT id_tpt_part "
                    + "FROM " + DModConsts.TablesMap.get(DModConsts.LU_TRUCK_TPT_FIGURE_TPT_PART) + " "
                    + getSqlWhere();
            
            try (Statement statement = session.getStatement().getConnection().createStatement()) {
                resultSet = statement.executeQuery(msSql);
                while (resultSet.next()) {
                    DDbTruckTransportFigureTransportPart transportPart = (DDbTruckTransportFigureTransportPart) session.readRegistry(DModConsts.LU_TRUCK_TPT_FIGURE_TPT_PART, new int[] { mnPkTruckId, mnPkTransportFigureId, resultSet.getInt("id_tpt_part") });
                    maChildTransportParts.add(transportPart);
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

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkTruckId + ", " + 
                    mnPkTransportFigureId + ", " + 
                    mnFkTransportFigureId + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_truck = " + mnPkTruckId + ", " +
                    //"id_tpt_figure = " + mnPkTransportFigureId + ", " +
                    "fk_tpt_figure = " + mnFkTransportFigureId + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        // save as well child transport parts:
        
        msSql = "DELETE "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.LU_TRUCK_TPT_FIGURE_TPT_PART) + " "
                + getSqlWhere();
        
        session.getStatement().execute(msSql);
        
        for (DDbTruckTransportFigureTransportPart transportPart : maChildTransportParts) {
            transportPart.setPkTruckId(mnPkTruckId);
            transportPart.setPkTransportFigureId(mnPkTransportFigureId);
            transportPart.setRegistryNew(true);
            transportPart.save(session);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbTruckTransportFigure clone() throws CloneNotSupportedException {
        DDbTruckTransportFigure registry = new DDbTruckTransportFigure();

        registry.setPkTruckId(this.getPkTruckId());
        registry.setPkTransportFigureId(this.getPkTransportFigureId());
        registry.setFkTransportFigureId(this.getFkTransportFigureId());
        
        // clone as well child transport parts:

        for (DDbTruckTransportFigureTransportPart transportPart : maChildTransportParts) {
            registry.getChildTransportParts().add(transportPart.clone());
        }
        
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
