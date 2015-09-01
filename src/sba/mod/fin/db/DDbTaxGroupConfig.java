/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.fin.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbTaxGroupConfig extends DDbRegistry {

    protected int mnPkTaxGroupId;
    protected int mnPkConfigId;
    protected Date mtDateStart;
    protected int mnFkTaxRegionId;

    protected Vector<DDbTaxGroupConfigRow> mvChildRows;

    public DDbTaxGroupConfig() {
        super(DModConsts.FU_TAX_GRP_CFG);
        mvChildRows = new Vector<DDbTaxGroupConfigRow>();
        initRegistry();
    }

    public void setPkTaxGroupId(int n) { mnPkTaxGroupId = n; }
    public void setPkConfigId(int n) { mnPkConfigId = n; }
    public void setDateStart(Date t) { mtDateStart = t; }
    public void setFkTaxRegionId(int n) { mnFkTaxRegionId = n; }

    public int getPkTaxGroupId() { return mnPkTaxGroupId; }
    public int getPkConfigId() { return mnPkConfigId; }
    public Date getDateStart() { return mtDateStart; }
    public int getFkTaxRegionId() { return mnFkTaxRegionId; }

    public Vector<DDbTaxGroupConfigRow> getChildRows() { return mvChildRows; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkTaxGroupId = pk[0];
        mnPkConfigId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkTaxGroupId, mnPkConfigId };
    }

    @Override
    public void initRegistry() {
        super.initBaseRegistry();

        mnPkTaxGroupId = 0;
        mnPkConfigId = 0;
        mtDateStart = null;
        mnFkTaxRegionId = 0;

        mvChildRows.clear();
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_tax_grp = " + mnPkTaxGroupId + " AND " +
                "id_cfg = " + mnPkConfigId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_tax_grp = " + pk[0] + " AND " +
                "id_cfg = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(DGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
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
            mnPkTaxGroupId = resultSet.getInt("id_tax_grp");
            mnPkConfigId = resultSet.getInt("id_cfg");
            mtDateStart = resultSet.getDate("dt_sta");
            mnFkTaxRegionId = resultSet.getInt("fk_tax_reg");

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();

            msSql = "SELECT id_row FROM " + DModConsts.TablesMap.get(DModConsts.FU_TAX_GRP_CFG_ROW) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbTaxGroupConfigRow child = new DDbTaxGroupConfigRow();
                child.read(session, new int[] { mnPkTaxGroupId, mnPkConfigId, resultSet.getInt(1) });
                mvChildRows.add(child);
            }

            // Finish registry reading:

            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkTaxGroupId + ", " +
                    mnPkConfigId + ", " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    mnFkTaxRegionId + " " +
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_tax_grp = " + mnPkTaxGroupId + ", " +
                    "id_cfg = " + mnPkConfigId + ", " +
                    */
                    "dt_sta = '" + DLibUtils.DbmsDateFormatDate.format(mtDateStart) + "', " +
                    "fk_tax_reg = " + mnFkTaxRegionId + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        for (DDbTaxGroupConfigRow child : mvChildRows) {
            child.setPkTaxGroupId(mnPkTaxGroupId);
            child.setPkConfigId(mnPkConfigId);
            child.setRegistryNew(true);
            child.save(session);
        }

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbTaxGroupConfig clone() throws CloneNotSupportedException {
        DDbTaxGroupConfig registry = new DDbTaxGroupConfig();

        registry.setPkTaxGroupId(this.getPkTaxGroupId());
        registry.setPkConfigId(this.getPkConfigId());
        registry.setDateStart(this.getDateStart());
        registry.setFkTaxRegionId(this.getFkTaxRegionId());

        for (DDbTaxGroupConfigRow child : mvChildRows) {
            registry.getChildRows().add(child.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public void setRegistryNew(boolean registryNew) {
        super.setRegistryNew(registryNew);

        for (DDbTaxGroupConfigRow child : mvChildRows) {
            child.setRegistryNew(registryNew);
        }
    }
}
