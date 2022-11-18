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
public class DDbTruckTrailer extends DDbRegistryUser {

    protected int mnPkTruckId;
    protected int mnPkTrailerId;
    protected int mnFkTrailerId;

    public DDbTruckTrailer() {
        super(DModConsts.LU_TRUCK_TRAIL);
        initRegistry();
    }

    public void setPkTruckId(int n) { mnPkTruckId = n; }
    public void setPkTrailerId(int n) { mnPkTrailerId = n; }
    public void setFkTrailerId(int n) { mnFkTrailerId = n; }

    public int getPkTruckId() { return mnPkTruckId; }
    public int getPkTrailerId() { return mnPkTrailerId; }
    public int getFkTrailerId() { return mnFkTrailerId; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkTruckId = pk[0];
        mnPkTrailerId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkTruckId, mnPkTrailerId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkTruckId = 0;
        mnPkTrailerId = 0;
        mnFkTrailerId = 0;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_truck = " + mnPkTruckId + " "
                + "AND id_trail = " + mnPkTrailerId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_truck = " + pk[0] + " "
                + "AND id_trail = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkTrailerId = 0;

        msSql = "SELECT COALESCE(MAX(id_trail), 0) + 1 "
                + "FROM " + getSqlTable() + " "
                + "WHERE id_truck = " + mnPkTruckId + " ";
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
            mnPkTruckId = resultSet.getInt("id_truck");
            mnPkTrailerId = resultSet.getInt("id_trail");
            mnFkTrailerId = resultSet.getInt("fk_trail");

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
                    mnPkTrailerId + ", " + 
                    mnFkTrailerId + " " + 
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_truck = " + mnPkTruckId + ", " +
                    //"id_trail = " + mnPkTrailerId + ", " +
                    "fk_trail = " + mnFkTrailerId + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbTruckTrailer clone() throws CloneNotSupportedException {
        DDbTruckTrailer registry = new DDbTruckTrailer();

        registry.setPkTruckId(this.getPkTruckId());
        registry.setPkTrailerId(this.getPkTrailerId());
        registry.setFkTrailerId(this.getFkTrailerId());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
