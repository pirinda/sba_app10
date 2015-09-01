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
public class DDbDpsRowNote extends DDbRegistry {

    protected int mnPkDpsId;
    protected int mnPkRowId;
    protected int mnPkNoteId;
    protected String msText;
    protected boolean mbPrintable;

    public DDbDpsRowNote() {
        super(DModConsts.T_DPS_ROW_NOT);
        initRegistry();
    }

    public void setPkDpsId(int n) { mnPkDpsId = n; }
    public void setPkRowId(int n) { mnPkRowId = n; }
    public void setPkNoteId(int n) { mnPkNoteId = n; }
    public void setText(String s) { msText = s; }
    public void setPrintable(boolean b) { mbPrintable = b; }

    public int getPkDpsId() { return mnPkDpsId; }
    public int getPkRowId() { return mnPkRowId; }
    public int getPkNoteId() { return mnPkNoteId; }
    public String getText() { return msText; }
    public boolean isPrintable() { return mbPrintable; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDpsId = pk[0];
        mnPkRowId = pk[1];
        mnPkNoteId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDpsId, mnPkRowId, mnPkNoteId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkDpsId = 0;
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
        return "WHERE id_dps = " + mnPkDpsId + " AND " +
                "id_row = " + mnPkRowId + " AND " +
                "id_not = " + mnPkNoteId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_dps = " + pk[0] + " AND " +
                "id_row = " + pk[1] + " AND " +
                "id_not = " + pk[2] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkNoteId = 0;

        msSql = "SELECT COALESCE(MAX(id_not), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_dps = " + mnPkDpsId + " AND " +
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
            mnPkDpsId = resultSet.getInt("id_dps");
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
                    mnPkDpsId + ", " +
                    mnPkRowId + ", " +
                    mnPkNoteId + ", " +
                    "'" + msText + "', " +
                    (mbPrintable ? 1 : 0) + " " +
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_dps = " + mnPkDpsId + ", " +
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
    public DDbDpsRowNote clone() throws CloneNotSupportedException {
        DDbDpsRowNote registry = new DDbDpsRowNote();

        registry.setPkDpsId(this.getPkDpsId());
        registry.setPkRowId(this.getPkRowId());
        registry.setPkNoteId(this.getPkNoteId());
        registry.setText(this.getText());
        registry.setPrintable(this.isPrintable());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
