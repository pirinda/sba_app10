/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.cfg.db;

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
public class DDbUserBranchDpsSeries extends DDbRegistry implements DUtilBranchEntity, DGridRow {

    protected int mnPkUserId;
    protected int mnPkSeriesId;
    protected int mnPkBizPartnerId;
    protected int mnPkBranchId;

    protected String msXtaSeries;
    protected String msXtaBranchName;
    protected String msXtaDpsTypeName;
    protected boolean mbXtaDisabled;
    protected boolean mbXtaSelected;

    public DDbUserBranchDpsSeries() {
        super(DModConsts.CU_USR_SER_BRA);
        initRegistry();
    }

    public void setPkUserId(int n) { mnPkUserId = n; }
    public void setPkSeriesId(int n) { mnPkSeriesId = n; }
    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setPkBranchId(int n) { mnPkBranchId = n; }

    public int getPkUserId() { return mnPkUserId; }
    public int getPkSeriesId() { return mnPkSeriesId; }
    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public int getPkBranchId() { return mnPkBranchId; }

    public void setXtaSeries(String s) { msXtaSeries = s; }
    public void setXtaBranchName(String s) { msXtaBranchName = s; }
    public void setXtaDpsTypeName(String s) { msXtaDpsTypeName = s; }
    public void setXtaDisabled(boolean b) { mbXtaDisabled = b; }
    public void setXtaSelected(boolean b) { mbXtaSelected = b; }

    public String getXtaSeries() { return msXtaSeries; }
    public String getXtaBranchName() { return msXtaBranchName; }
    public String getXtaDpsTypeName() { return msXtaDpsTypeName; }
    public boolean getXtaDisabled() { return mbXtaDisabled; }
    public boolean getXtaSelected() { return mbXtaSelected; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkUserId = pk[0];
        mnPkSeriesId = pk[1];
        mnPkBizPartnerId = pk[2];
        mnPkBranchId = pk[3];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkUserId, mnPkSeriesId, mnPkBizPartnerId, mnPkBranchId};
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkUserId = 0;
        mnPkSeriesId = 0;
        mnPkBizPartnerId = 0;
        mnPkBranchId = 0;

        msXtaSeries = "";
        msXtaBranchName = "";
        msXtaDpsTypeName = "";
        mbXtaDisabled = false;
        mbXtaSelected = false;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_usr = " + mnPkUserId + " AND " +
                "id_ser = " + mnPkSeriesId + " AND " +
                "id_bpr = " + mnPkBizPartnerId + " AND " +
                "id_bra = " + mnPkBranchId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_usr = " + pk[0] + " AND " +
                "id_ser = " + pk[1] + " AND " +
                "id_bpr = " + pk[2] + " AND " +
                "id_bra = " + pk[3] + " ";
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
            mnPkUserId = resultSet.getInt("id_usr");
            mnPkSeriesId = resultSet.getInt("id_ser");
            mnPkBizPartnerId = resultSet.getInt("id_bpr");
            mnPkBranchId = resultSet.getInt("id_bra");

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
    public DDbUserBranchDpsSeries clone() throws CloneNotSupportedException {
        DDbUserBranchDpsSeries registry = new DDbUserBranchDpsSeries();

        registry.setPkUserId(this.getPkUserId());
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
        return new int[] { mnPkSeriesId, mnPkBizPartnerId, mnPkBranchId };
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
                value = msXtaDpsTypeName;
                break;
            case 2:
                value = msXtaSeries;
                break;
            case 3:
                value = mbXtaDisabled;
                break;
            case 4:
                value = mbXtaSelected;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 0:
            case 1:
            case 2:
            case 3:
                break;
            case 4:
                mbXtaSelected = (Boolean) value;
                break;
            default:
        }
    }
}
