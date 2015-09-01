/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

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
public class DDbIogNote extends DDbRegistry implements DGridRow {

    protected int mnPkIogId;
    protected int mnPkNoteId;
    protected String msText;
    protected boolean mbPrintable;

    public DDbIogNote() {
        super(DModConsts.T_IOG_NOT);
        initRegistry();
    }

    public void setPkIogId(int n) { mnPkIogId = n; }
    public void setPkNoteId(int n) { mnPkNoteId = n; }
    public void setText(String s) { msText = s; }
    public void setPrintable(boolean b) { mbPrintable = b; }

    public int getPkIogId() { return mnPkIogId; }
    public int getPkNoteId() { return mnPkNoteId; }
    public String getText() { return msText; }
    public boolean isPrintable() { return mbPrintable; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkIogId = pk[0];
        mnPkNoteId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkIogId, mnPkNoteId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkIogId = 0;
        mnPkNoteId = 0;
        msText = "";
        mbPrintable = false;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_iog = " + mnPkIogId + " AND " +
                "id_not = " + mnPkNoteId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_iog = " + pk[0] + " AND " +
                "id_not = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkNoteId = 0;

        msSql = "SELECT COALESCE(MAX(id_not), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_iog = " + mnPkIogId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkNoteId = resultSet.getInt(1);
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
            mnPkIogId = resultSet.getInt("id_iog");
            mnPkNoteId = resultSet.getInt("id_not");
            msText = resultSet.getString("txt");
            mbPrintable = resultSet.getBoolean("b_prt");

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
                    mnPkIogId + ", " +
                    mnPkNoteId + ", " +
                    "'" + msText + "', " +
                    (mbPrintable ? 1 : 0) + " " +
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_iog = " + mnPkIogId + ", " +
                    "id_not = " + mnPkNoteId + ", " +
                    */
                    "txt = '" + msText + "', " +
                    "b_prt = " + (mbPrintable ? 1 : 0) + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbIogNote clone() throws CloneNotSupportedException {
        DDbIogNote registry = new DDbIogNote();

        registry.setPkIogId(this.getPkIogId());
        registry.setPkNoteId(this.getPkNoteId());
        registry.setText(this.getText());
        registry.setPrintable(this.isPrintable());

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
                value = msText;
                break;
            case 1:
                value = mbPrintable;
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
