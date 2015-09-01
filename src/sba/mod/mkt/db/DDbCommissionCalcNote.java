/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.mkt.db;

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
public class DDbCommissionCalcNote extends DDbRegistry {

    protected int mnPkCommissionCalcId;
    protected int mnPkNoteId;
    protected String msText;
    protected boolean mbPrintable;

    public DDbCommissionCalcNote() {
        super(DModConsts.M_CMM_CAL_NOT);
        initRegistry();
    }

    public void setPkCommissionCalcId(int n) { mnPkCommissionCalcId = n; }
    public void setPkNoteId(int n) { mnPkNoteId = n; }
    public void setText(String s) { msText = s; }
    public void setPrintable(boolean b) { mbPrintable = b; }

    public int getPkCommissionCalcId() { return mnPkCommissionCalcId; }
    public int getPkNoteId() { return mnPkNoteId; }
    public String getText() { return msText; }
    public boolean isPrintable() { return mbPrintable; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkCommissionCalcId = pk[0];
        mnPkNoteId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkCommissionCalcId, mnPkNoteId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkCommissionCalcId = 0;
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
        return "WHERE id_cmm = " + mnPkCommissionCalcId + " AND " +
                "id_not = " + mnPkNoteId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_cmm = " + pk[0] + " AND " +
                "id_not = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkNoteId = 0;

        msSql = "SELECT COALESCE(MAX(id_not), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_cmm = " + mnPkCommissionCalcId + " ";
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
            mnPkCommissionCalcId = resultSet.getInt("id_cmm");
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
                    mnPkCommissionCalcId + ", " +
                    mnPkNoteId + ", " +
                    "'" + msText + "', " +
                    (mbPrintable ? 1 : 0) + " " +
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_cmm = " + mnPkCommissionCalcId + ", " +
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
    public DDbCommissionCalcNote clone() throws CloneNotSupportedException {
        DDbCommissionCalcNote registry = new DDbCommissionCalcNote();

        registry.setPkCommissionCalcId(this.getPkCommissionCalcId());
        registry.setPkNoteId(this.getPkNoteId());
        registry.setText(this.getText());
        registry.setPrintable(this.isPrintable());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
