/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.itm.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.grid.DGridRow;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbItemBarcode extends DDbRegistry implements DGridRow {

    protected int mnPkItemId;
    protected int mnPkBarcodeId;
    protected String msBarcode;

    public DDbItemBarcode() {
        super(DModConsts.IU_ITM_BAR);
        initRegistry();
    }

    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPkBarcodeId(int n) { mnPkBarcodeId = n; }
    public void setBarcode(String s) { msBarcode = s; }

    public int getPkItemId() { return mnPkItemId; }
    public int getPkBarcodeId() { return mnPkBarcodeId; }
    public String getBarcode() { return msBarcode; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkItemId = pk[0];
        mnPkBarcodeId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkItemId, mnPkBarcodeId };
    }

    @Override
    public void initRegistry() {
        super.initBaseRegistry();

        mnPkItemId = 0;
        mnPkBarcodeId = 0;
        msBarcode = "";
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_itm = " + mnPkItemId + " AND " +
                "id_bar = " + mnPkBarcodeId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_itm = " + pk[0] + " AND " +
                "id_bar = " + pk[1] + " ";
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
            mnPkItemId = resultSet.getInt("id_itm");
            mnPkBarcodeId = resultSet.getInt("id_bar");
            msBarcode = resultSet.getString("bar");

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
                    mnPkItemId + ", " +
                    mnPkBarcodeId + ", " +
                    "'" + msBarcode + "' " +
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
    public DDbItemBarcode clone() throws CloneNotSupportedException {
        DDbItemBarcode registry = new DDbItemBarcode();

        registry.setPkItemId(this.getPkItemId());
        registry.setPkBarcodeId(this.getPkBarcodeId());
        registry.setBarcode(this.getBarcode());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
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
                value = msBarcode;
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
