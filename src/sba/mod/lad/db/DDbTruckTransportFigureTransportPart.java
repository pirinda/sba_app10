/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.lad.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbTruckTransportFigureTransportPart extends DDbRegistryUser {

    protected int mnPkTruckId;
    protected int mnPkTransportFigureId;
    protected int mnPkTransportPartId;
    protected int mnFkTransportPartTypeId;

    public DDbTruckTransportFigureTransportPart() {
        super(DModConsts.LU_TRUCK_TPT_FIGURE_TPT_PART);
        initRegistry();
    }

    public void setPkTruckId(int n) { mnPkTruckId = n; }
    public void setPkTransportFigureId(int n) { mnPkTransportFigureId = n; }
    public void setPkTransportPartId(int n) { mnPkTransportPartId = n; }
    public void setFkTransportPartTypeId(int n) { mnFkTransportPartTypeId = n; }

    public int getPkTruckId() { return mnPkTruckId; }
    public int getPkTransportFigureId() { return mnPkTransportFigureId; }
    public int getPkTransportPartId() { return mnPkTransportPartId; }
    public int getFkTransportPartTypeId() { return mnFkTransportPartTypeId; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkTruckId = pk[0];
        mnPkTransportFigureId = pk[1];
        mnPkTransportPartId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkTruckId, mnPkTransportFigureId, mnPkTransportPartId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkTruckId = 0;
        mnPkTransportFigureId = 0;
        mnPkTransportPartId = 0;
        mnFkTransportPartTypeId = 0;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_truck = " + mnPkTruckId + " "
                + "AND id_tpt_figure = " + mnPkTransportFigureId + " "
                + "AND id_tpt_part = " + mnPkTransportPartId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_truck = " + pk[0] + " "
                + "AND id_tpt_figure = " + pk[1] + " "
                + "AND id_tpt_part = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkTransportPartId = 0;

        msSql = "SELECT COALESCE(MAX(id_tpt_part), 0) + 1 "
                + "FROM " + getSqlTable() + " "
                + "WHERE id_truck = " + mnPkTruckId + " "
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
            mnPkTruckId = resultSet.getInt("id_truck");
            mnPkTransportFigureId = resultSet.getInt("id_tpt_figure");
            mnPkTransportPartId = resultSet.getInt("id_tpt_part");
            mnFkTransportPartTypeId = resultSet.getInt("fk_tpt_part_tp");

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
                    mnPkTransportPartId + ", " + 
                    mnFkTransportPartTypeId + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_truck = " + mnPkTruckId + ", " +
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
    public DDbTruckTransportFigureTransportPart clone() throws CloneNotSupportedException {
        DDbTruckTransportFigureTransportPart registry = new DDbTruckTransportFigureTransportPart();

        registry.setPkTruckId(this.getPkTruckId());
        registry.setPkTransportFigureId(this.getPkTransportFigureId());
        registry.setPkTransportPartId(this.getPkTransportPartId());
        registry.setFkTransportPartTypeId(this.getFkTransportPartTypeId());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
