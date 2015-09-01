/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbIogRowNote extends DDbRegistry {

    protected int mnPkIogId;
    protected int mnPkRowId;
    protected int mnPkNoteId;
    protected String msText;
    protected boolean mbPrintable;

    public DDbIogRowNote() {
        super(DModConsts.T_IOG_ROW_NOT);
        initRegistry();
    }

    public void setPkIogId(int n) { mnPkIogId = n; }
    public void setPkRowId(int n) { mnPkRowId = n; }
    public void setPkNoteId(int n) { mnPkNoteId = n; }
    public void setText(String s) { msText = s; }
    public void setPrintable(boolean b) { mbPrintable = b; }

    public int getPkIogId() { return mnPkIogId; }
    public int getPkRowId() { return mnPkRowId; }
    public int getPkNoteId() { return mnPkNoteId; }
    public String getText() { return msText; }
    public boolean isPrintable() { return mbPrintable; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkIogId = pk[0];
        mnPkRowId = pk[1];
        mnPkNoteId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkIogId, mnPkRowId, mnPkNoteId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkIogId = 0;
        mnPkRowId = 0;
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
                "id_row = " + mnPkRowId + " AND " +
                "id_not = " + mnPkNoteId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_iog = " + pk[0] + " AND " +
                "id_row = " + pk[1] + " AND " +
                "id_not = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkNoteId = 0;

        msSql = "SELECT COALESCE(MAX(id_not), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_iog = " + mnPkIogId + " AND " +
                "id_row = " + mnPkRowId + " ";
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
            mnPkRowId = resultSet.getInt("id_row");
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
                    mnPkRowId + ", " +
                    mnPkNoteId + ", " +
                    "'" + msText + "', " +
                    (mbPrintable ? 1 : 0) + " " +
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_iog = " + mnPkIogId + ", " +
                    "id_row = " + mnPkRowId + ", " +
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
    public DDbIogRowNote clone() throws CloneNotSupportedException {
        DDbIogRowNote registry = new DDbIogRowNote();

        registry.setPkIogId(this.getPkIogId());
        registry.setPkRowId(this.getPkRowId());
        registry.setPkNoteId(this.getPkNoteId());
        registry.setText(this.getText());
        registry.setPrintable(this.isPrintable());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
