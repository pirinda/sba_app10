/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import sba.gui.util.DUtilBranchEntity;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.grid.DGridRow;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbDpsSeriesBranch extends DDbRegistry implements DUtilBranchEntity, DGridRow {

    protected int mnPkSeriesId;
    protected int mnPkBizPartnerId;
    protected int mnPkBranchId;

    protected String msDbSeries;
    protected String msDbBranchName;
    protected String msDbDpsTypeName;
    protected int[] manDbDpsTypeKey;

    protected String msXtaBranchName;
    protected boolean mbXtaDisabled;
    protected boolean mbXtaSelected;

    public DDbDpsSeriesBranch() {
        super(DModConsts.TU_SER_BRA);
        initRegistry();
    }

    public void setPkSeriesId(int n) { mnPkSeriesId = n; }
    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setPkBranchId(int n) { mnPkBranchId = n; }

    public int getPkSeriesId() { return mnPkSeriesId; }
    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public int getPkBranchId() { return mnPkBranchId; }

    public void setDbSeries(String s) { msDbSeries = s; }
    public void setDbBranchName(String s) { msDbBranchName = s; }
    public void setDbDpsTypeName(String s) { msDbDpsTypeName = s; }
    public void setDbDpsTypeKey(int[] key) { manDbDpsTypeKey = key; }

    public String getDbSeries() { return msDbSeries; }
    public String getDbBranchName() { return msDbBranchName; }
    public String getDbDpsTypeName() { return msDbDpsTypeName; }
    public int[] getDbDpsTypeKey() { return manDbDpsTypeKey; }

    public void setXtaBranchName(String s) { msXtaBranchName = s; }
    public void setXtaDisabled(boolean b) { mbXtaDisabled = b; }
    public void setXtaSelected(boolean b) { mbXtaSelected = b; }

    public String getXtaBranchName() { return msXtaBranchName; }
    public boolean getXtaDisabled() { return mbXtaDisabled; }
    public boolean getXtaSelected() { return mbXtaSelected; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkSeriesId = pk[0];
        mnPkBizPartnerId = pk[1];
        mnPkBranchId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkSeriesId, mnPkBizPartnerId, mnPkBranchId };
    }

    @Override
    public void initRegistry() {
        super.initBaseRegistry();

        mnPkSeriesId = 0;
        mnPkBizPartnerId = 0;
        mnPkBranchId = 0;

        msDbSeries = "";
        msDbBranchName = "";
        msDbDpsTypeName = "";
        manDbDpsTypeKey = null;

        msXtaBranchName = "";
        mbXtaDisabled = false;
        mbXtaSelected = false;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_ser = " + mnPkSeriesId + " AND " +
                "id_bpr = " + mnPkBizPartnerId + " AND " +
                "id_bra = " + mnPkBranchId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_ser = " + pk[0] + " AND " +
                "id_bpr = " + pk[1] + " AND " +
                "id_bra = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
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
            mnPkSeriesId = resultSet.getInt("id_ser");
            mnPkBizPartnerId = resultSet.getInt("id_bpr");
            mnPkBranchId = resultSet.getInt("id_bra");

            // Read additional fields:

            msDbSeries = (String) session.readField(DModConsts.TU_SER, new int[] { mnPkSeriesId }, DDbDpsSeries.FIELD_SERIES);
            msDbBranchName = (String) session.readField(DModConsts.BU_BRA, new int[] { mnPkBizPartnerId, mnPkBranchId }, DDbRegistry.FIELD_NAME);

            manDbDpsTypeKey = (int[]) session.readField(DModConsts.TU_SER, new int[] { mnPkSeriesId }, DDbDpsSeries.FIELD_DPS_TP_KEY);
            msDbDpsTypeName = (String) session.readField(DModConsts.TS_DPS_TP, manDbDpsTypeKey, DDbRegistry.FIELD_NAME);

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
                    mnPkSeriesId + ", " +
                    mnPkBizPartnerId + ", " +
                    mnPkBranchId + " " +
                    ")";
        }
        else {
            throw new Exception(DDbConsts.ERR_MSG_REG_NON_UPDATABLE);
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbDpsSeriesBranch clone() throws CloneNotSupportedException {
        DDbDpsSeriesBranch registry = new DDbDpsSeriesBranch();

        registry.setPkSeriesId(this.getPkSeriesId());
        registry.setPkBizPartnerId(this.getPkBizPartnerId());
        registry.setPkBranchId(this.getPkBranchId());

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
    public int[] getBranchEntityKey() {
        return new int[] { mnPkBizPartnerId, mnPkBranchId, mnPkSeriesId };
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
    public Object getRowValueAt(final int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = msXtaBranchName;
                break;
            case 1:
                value = mbXtaDisabled;
                break;
            case 2:
                value = mbXtaSelected;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(final Object value, final int col) {
        switch (col) {
            case 0:
            case 1:
                break;
            case 2:
                mbXtaSelected = (Boolean) value;
                break;
            default:
        }
    }
}
