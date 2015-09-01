/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.cfg.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import sba.gui.util.DUtilBranch;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.grid.DGridRow;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbUserBranch extends DDbRegistry implements DUtilBranch, DGridRow {

    protected int mnPkUserId;
    protected int mnPkBizPartnerId;
    protected int mnPkBranchId;
    protected boolean mbUniversalCash;
    protected boolean mbUniversalWarehouse;
    protected boolean mbUniversalDpsSeries;

    protected Vector<DDbUserBranchCash> mvChildBranchCashes;
    protected Vector<DDbUserBranchWarehouse> mvChildBranchWarehouses;
    protected Vector<DDbUserBranchDpsSeries> mvChildBranchDpsSeries;

    protected String msXtaBranchName;
    protected boolean mbXtaDisabled;

    public DDbUserBranch() {
        super(DModConsts.CU_USR_BRA);
        mvChildBranchCashes = new Vector<DDbUserBranchCash>();
        mvChildBranchWarehouses = new Vector<DDbUserBranchWarehouse>();
        mvChildBranchDpsSeries = new Vector<DDbUserBranchDpsSeries>();
        initRegistry();
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setPkBranchId(int n) { mnPkBranchId = n; }
    public void setUniversalCash(boolean b) { mbUniversalCash = b; }
    public void setUniversalWarehouse(boolean b) { mbUniversalWarehouse = b; }
    public void setUniversalDpsSeries(boolean b) { mbUniversalDpsSeries = b; }

    public int getPkUserId() { return mnPkUserId; }
    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public int getPkBranchId() { return mnPkBranchId; }
    public boolean isUniversalCash() { return mbUniversalCash; }
    public boolean isUniversalWarehouse() { return mbUniversalWarehouse; }
    public boolean isUniversalDpsSeries() { return mbUniversalDpsSeries; }

    public Vector<DDbUserBranchCash> getChildBranchCashes() { return mvChildBranchCashes; }
    public Vector<DDbUserBranchWarehouse> getChildBranchWarehouses() { return mvChildBranchWarehouses; }
    public Vector<DDbUserBranchDpsSeries> getChildBranchDpsSeries() { return mvChildBranchDpsSeries; }

    public void setXtaBranchName(String s) { msXtaBranchName = s; }
    public void setXtaDisabled(boolean b) { mbXtaDisabled = b; }

    public String getXtaBranchName() { return msXtaBranchName; }
    public boolean getXtaDisabled() { return mbXtaDisabled; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkUserId = pk[0];
        mnPkBizPartnerId = pk[1];
        mnPkBranchId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkUserId, mnPkBizPartnerId, mnPkBranchId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkUserId = 0;
        mnPkBizPartnerId = 0;
        mnPkBranchId = 0;
        mbUniversalCash = false;
        mbUniversalWarehouse = false;
        mbUniversalDpsSeries = false;

        mvChildBranchCashes.clear();
        mvChildBranchWarehouses.clear();
        mvChildBranchDpsSeries.clear();

        msXtaBranchName = "";
        mbXtaDisabled = false;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_usr = " + mnPkUserId + " AND " +
                "id_bpr = " + mnPkBizPartnerId + " AND " +
                "id_bra = " + mnPkBranchId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_usr = " + pk[0] + " AND " +
                "id_bpr = " + pk[1] + " AND " +
                "id_bra = " + pk[2] + " ";
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
            mnPkUserId = resultSet.getInt("id_usr");
            mnPkBizPartnerId = resultSet.getInt("id_bpr");
            mnPkBranchId = resultSet.getInt("id_bra");
            mbUniversalCash = resultSet.getBoolean("b_unv_csh");
            mbUniversalWarehouse = resultSet.getBoolean("b_unv_wah");
            mbUniversalDpsSeries = resultSet.getBoolean("b_unv_ser_bra");

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();

            msSql = "SELECT id_csh FROM " + DModConsts.TablesMap.get(DModConsts.CU_USR_CSH) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbUserBranchCash child = new DDbUserBranchCash();
                child.read(session, new int[] { mnPkUserId, mnPkBizPartnerId, mnPkBranchId, resultSet.getInt(1) });
                mvChildBranchCashes.add(child);
            }

            msSql = "SELECT id_wah FROM " + DModConsts.TablesMap.get(DModConsts.CU_USR_WAH) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbUserBranchWarehouse child = new DDbUserBranchWarehouse();
                child.read(session, new int[] { mnPkUserId, mnPkBizPartnerId, mnPkBranchId, resultSet.getInt(1) });
                mvChildBranchWarehouses.add(child);
            }

            msSql = "SELECT id_ser FROM " + DModConsts.TablesMap.get(DModConsts.CU_USR_SER_BRA) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbUserBranchDpsSeries child = new DDbUserBranchDpsSeries();
                child.read(session, new int[] { mnPkUserId, resultSet.getInt(1), mnPkBizPartnerId, mnPkBranchId });
                mvChildBranchDpsSeries.add(child);
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
                    mnPkUserId + ", " +
                    mnPkBizPartnerId + ", " +
                    mnPkBranchId + ", " +
                    (mbUniversalCash ? 1 : 0) + ", " +
                    (mbUniversalWarehouse ? 1 : 0) + ", " +
                    (mbUniversalDpsSeries ? 1 : 0) + " " +
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_usr = " + mnPkUserId + ", " +
                    "id_bpr = " + mnPkBizPartnerId + ", " +
                    "id_bra = " + mnPkBranchId + ", " +
                    */
                    "b_unv_csh = " + (mbUniversalCash ? 1 : 0) + ", " +
                    "b_unv_wah = " + (mbUniversalWarehouse ? 1 : 0) + ", " +
                    "b_unv_ser_bra = " + (mbUniversalDpsSeries ? 1 : 0) + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        for (DDbUserBranchCash child : mvChildBranchCashes) {
            child.setPkUserId(mnPkUserId);
            child.setPkBizPartnerId(mnPkBizPartnerId);
            child.setPkBranchId(mnPkBranchId);
            child.setRegistryNew(true);
            child.save(session);
        }

        for (DDbUserBranchWarehouse child : mvChildBranchWarehouses) {
            child.setPkUserId(mnPkUserId);
            child.setPkBizPartnerId(mnPkBizPartnerId);
            child.setPkBranchId(mnPkBranchId);
            child.setRegistryNew(true);
            child.save(session);
        }

        for (DDbUserBranchDpsSeries child : mvChildBranchDpsSeries) {
            child.setPkUserId(mnPkUserId);
            child.setPkBizPartnerId(mnPkBizPartnerId);
            child.setPkBranchId(mnPkBranchId);
            child.setRegistryNew(true);
            child.save(session);
        }

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbUserBranch clone() throws CloneNotSupportedException {
        DDbUserBranch registry = new DDbUserBranch();

        registry.setPkUserId(this.getPkUserId());
        registry.setPkBizPartnerId(this.getPkBizPartnerId());
        registry.setPkBranchId(this.getPkBranchId());
        registry.setUniversalCash(this.isUniversalCash());
        registry.setUniversalWarehouse(this.isUniversalWarehouse());
        registry.setUniversalDpsSeries(this.isUniversalDpsSeries());

        for (DDbUserBranchCash child : mvChildBranchCashes) {
            registry.getChildBranchCashes().add(child.clone());
        }

        for (DDbUserBranchWarehouse child : mvChildBranchWarehouses) {
            registry.getChildBranchWarehouses().add(child.clone());
        }

        for (DDbUserBranchDpsSeries child : mvChildBranchDpsSeries) {
            registry.getChildBranchDpsSeries().add(child.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public int[] getCompanyKey() {
        return new int[] { mnPkBizPartnerId };
    }


    @Override
    public int[] getBranchKey() {
        return new int[] { mnPkBizPartnerId, mnPkBranchId };
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return getCode();
    }

    @Override
    public String getRowName() {
        return getName();
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
        return isRegistryEdited();
    }

    @Override
    public void setRowEdited(final boolean edited) {
        setRegistryEdited(edited);
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = msXtaBranchName;
                break;
            case 1:
                value = mbXtaDisabled;
                break;
            case 2:
                value = mbUniversalCash;
                break;
            case 3:
                value = mbUniversalWarehouse;
                break;
            case 4:
                value = mbUniversalDpsSeries;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
