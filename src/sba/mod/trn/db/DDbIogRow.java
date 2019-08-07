/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.db.DDbRegistryUser;
import sba.lib.grid.DGridRow;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbIogRow extends DDbRegistryUser implements DGridRow, DTrnDocRow {

    protected int mnPkIogId;
    protected int mnPkRowId;
    protected int mnSortingPos;
    protected double mdQuantity;
    protected double mdValueUnitary;
    protected double mdValue_r;
    protected boolean mbInventoriable;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkItemId;
    protected int mnFkUnitId;
    protected int mnFkDpsInvDpsId_n;
    protected int mnFkDpsInvRowId_n;
    protected int mnFkDpsAdjDpsId_n;
    protected int mnFkDpsAdjRowId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected String msDbItemCode;
    protected String msDbItemName;
    protected String msDbUnitCode;

    protected Vector<DDbIogRowNote> mvChildRowNotes;

    protected Vector<DTrnStockMove> mvAuxStockMoves;

    public DDbIogRow() {
        super(DModConsts.T_IOG_ROW);
        mvChildRowNotes = new Vector<>();
        mvAuxStockMoves = new Vector<>();
        initRegistry();
    }

    /*
     * Public methods
     */

    public void setPkIogId(int n) { mnPkIogId = n; }
    public void setPkRowId(int n) { mnPkRowId = n; }
    public void setSortingPos(int n) { mnSortingPos = n; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setValueUnitary(double d) { mdValueUnitary = d; }
    public void setValue_r(double d) { mdValue_r = d; }
    public void setInventoriable(boolean b) { mbInventoriable = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkItemId(int n) { mnFkItemId = n; }
    public void setFkUnitId(int n) { mnFkUnitId = n; }
    public void setFkDpsInvDpsId_n(int n) { mnFkDpsInvDpsId_n = n; }
    public void setFkDpsInvRowId_n(int n) { mnFkDpsInvRowId_n = n; }
    public void setFkDpsAdjDpsId_n(int n) { mnFkDpsAdjDpsId_n = n; }
    public void setFkDpsAdjRowId_n(int n) { mnFkDpsAdjRowId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkIogId() { return mnPkIogId; }
    public int getPkRowId() { return mnPkRowId; }
    public int getSortingPos() { return mnSortingPos; }
    public double getQuantity() { return mdQuantity; }
    public double getValueUnitary() { return mdValueUnitary; }
    public double getValue_r() { return mdValue_r; }
    public boolean isInventoriable() { return mbInventoriable; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkItemId() { return mnFkItemId; }
    public int getFkUnitId() { return mnFkUnitId; }
    public int getFkDpsInvDpsId_n() { return mnFkDpsInvDpsId_n; }
    public int getFkDpsInvRowId_n() { return mnFkDpsInvRowId_n; }
    public int getFkDpsAdjDpsId_n() { return mnFkDpsAdjDpsId_n; }
    public int getFkDpsAdjRowId_n() { return mnFkDpsAdjRowId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setDbItemCode(String s) { msDbItemCode = s; }
    public void setDbItemName(String s) { msDbItemName = s; }
    public void setDbUnitCode(String s) { msDbUnitCode = s; }

    public String getDbItemCode() { return msDbItemCode; }
    public String getDbItemName() { return msDbItemName; }
    public String getDbUnitCode() { return msDbUnitCode; }

    public Vector<DDbIogRowNote> getChildRowNotes() { return mvChildRowNotes; }

    public Vector<DTrnStockMove> getAuxStockMoves() { return mvAuxStockMoves; }

    public int[] getDpsInvRowKey_n() { return mnFkDpsInvDpsId_n == DLibConsts.UNDEFINED || mnFkDpsInvRowId_n == DLibConsts.UNDEFINED ? null : new int[] { mnFkDpsInvDpsId_n, mnFkDpsInvRowId_n }; }
    public int[] getDpsAdjRowKey_n() { return mnFkDpsAdjDpsId_n == DLibConsts.UNDEFINED || mnFkDpsAdjRowId_n == DLibConsts.UNDEFINED ? null : new int[] { mnFkDpsAdjDpsId_n, mnFkDpsAdjRowId_n }; }

    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkIogId = pk[0];
        mnPkRowId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkIogId, mnPkRowId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkIogId = 0;
        mnPkRowId = 0;
        mnSortingPos = 0;
        mdQuantity = 0;
        mdValueUnitary = 0;
        mdValue_r = 0;
        mbInventoriable = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkItemId = 0;
        mnFkUnitId = 0;
        mnFkDpsInvDpsId_n = 0;
        mnFkDpsInvRowId_n = 0;
        mnFkDpsAdjDpsId_n = 0;
        mnFkDpsAdjRowId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        msDbItemCode = "";
        msDbItemName = "";
        msDbUnitCode = "";

        mvChildRowNotes.clear();

        mvAuxStockMoves.clear();
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_iog = " + mnPkIogId + " AND " +
                "id_row = " + mnPkRowId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_iog = " + pk[0] + " AND " +
                "id_row = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkRowId = 0;

        msSql = "SELECT COALESCE(MAX(id_row), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_iog = " + mnPkIogId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkRowId = resultSet.getInt(1);
        }
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
            mnPkIogId = resultSet.getInt("id_iog");
            mnPkRowId = resultSet.getInt("id_row");
            mnSortingPos = resultSet.getInt("sort");
            mdQuantity = resultSet.getDouble("qty");
            mdValueUnitary = resultSet.getDouble("val_unt");
            mdValue_r = resultSet.getDouble("val_r");
            mbInventoriable = resultSet.getBoolean("b_inv");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkItemId = resultSet.getInt("fk_itm");
            mnFkUnitId = resultSet.getInt("fk_unt");
            mnFkDpsInvDpsId_n = resultSet.getInt("fk_dps_inv_dps_n");
            mnFkDpsInvRowId_n = resultSet.getInt("fk_dps_inv_row_n");
            mnFkDpsAdjDpsId_n = resultSet.getInt("fk_dps_adj_dps_n");
            mnFkDpsAdjRowId_n = resultSet.getInt("fk_dps_adj_row_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read database members:

            msDbItemCode = (String) session.readField(DModConsts.IU_ITM, new int[] { mnFkItemId }, DDbRegistry.FIELD_CODE);
            msDbItemName = (String) session.readField(DModConsts.IU_ITM, new int[] { mnFkItemId }, DDbRegistry.FIELD_NAME);
            msDbUnitCode = (String) session.readField(DModConsts.IU_UNT, new int[] { mnFkUnitId }, DDbRegistry.FIELD_CODE);

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();

            msSql = "SELECT id_not FROM " + DModConsts.TablesMap.get(DModConsts.T_IOG_ROW_NOT) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbIogRowNote child = new DDbIogRowNote();
                child.read(session, new int[] { mnPkIogId, mnPkRowId, resultSet.getInt(1) });
                mvChildRowNotes.add(child);
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
            computePrimaryKey(session);
            mbDeleted = false;
            //mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkIogId + ", " +
                    mnPkRowId + ", " +
                    mnSortingPos + ", " +
                    mdQuantity + ", " +
                    mdValueUnitary + ", " +
                    mdValue_r + ", " +
                    (mbInventoriable ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkItemId + ", " +
                    mnFkUnitId + ", " +
                    (mnFkDpsInvDpsId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsInvDpsId_n) + ", " +
                    (mnFkDpsInvRowId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsInvRowId_n) + ", " +
                    (mnFkDpsAdjDpsId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsAdjDpsId_n) + ", " +
                    (mnFkDpsAdjRowId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsAdjRowId_n) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_iog = " + mnPkIogId + ", " +
                    "id_row = " + mnPkRowId + ", " +
                    */
                    "sort = " + mnSortingPos + ", " +
                    "qty = " + mdQuantity + ", " +
                    "val_unt = " + mdValueUnitary + ", " +
                    "val_r = " + mdValue_r + ", " +
                    "b_inv = " + (mbInventoriable ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_itm = " + mnFkItemId + ", " +
                    "fk_unt = " + mnFkUnitId + ", " +
                    "fk_dps_inv_dps_n = " + (mnFkDpsInvDpsId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsInvDpsId_n) + ", " +
                    "fk_dps_inv_row_n = " + (mnFkDpsInvRowId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsInvRowId_n) + ", " +
                    "fk_dps_adj_dps_n = " + (mnFkDpsAdjDpsId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsAdjDpsId_n) + ", " +
                    "fk_dps_adj_row_n = " + (mnFkDpsAdjRowId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkDpsAdjRowId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        msSql = "DELETE FROM " + DModConsts.TablesMap.get(DModConsts.T_IOG_ROW_NOT) + " " + getSqlWhere();
        session.getStatement().execute(msSql);

        for (DDbIogRowNote child : mvChildRowNotes) {
            child.setPkIogId(mnPkIogId);
            child.setPkRowId(mnPkRowId);
            child.setRegistryNew(true);
            child.save(session);
        }

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbIogRow clone() throws CloneNotSupportedException {
        DDbIogRow registry = new DDbIogRow();

        registry.setPkIogId(this.getPkIogId());
        registry.setPkRowId(this.getPkRowId());
        registry.setSortingPos(this.getSortingPos());
        registry.setQuantity(this.getQuantity());
        registry.setValueUnitary(this.getValueUnitary());
        registry.setValue_r(this.getValue_r());
        registry.setInventoriable(this.isInventoriable());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkItemId(this.getFkItemId());
        registry.setFkUnitId(this.getFkUnitId());
        registry.setFkDpsInvDpsId_n(this.getFkDpsInvDpsId_n());
        registry.setFkDpsInvRowId_n(this.getFkDpsInvRowId_n());
        registry.setFkDpsAdjDpsId_n(this.getFkDpsAdjDpsId_n());
        registry.setFkDpsAdjRowId_n(this.getFkDpsAdjRowId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setDbItemCode(this.getDbItemCode());
        registry.setDbItemName(this.getDbItemName());
        registry.setDbUnitCode(this.getDbUnitCode());

        for (DDbIogRowNote child : mvChildRowNotes) {
            registry.getChildRowNotes().add(child.clone());
        }

        for (DTrnStockMove aux : mvAuxStockMoves) {
            registry.getAuxStockMoves().add(aux.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public void setRegistryNew(boolean registryNew) {
        super.setRegistryNew(registryNew);

        for (DDbIogRowNote child : mvChildRowNotes) {
            child.setRegistryNew(registryNew);
        }
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return getDbItemCode();
    }

    @Override
    public String getRowName() {
        return getDbItemName();
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
                value = mnSortingPos;
                break;
            case 1:
                value = mdQuantity;
                break;
            case 2:
                value = msDbUnitCode;
                break;
            case 3:
                value = msDbItemCode;
                break;
            case 4:
                value = msDbItemName;
                break;
            case 5:
                value = mdValueUnitary;
                break;
            case 6:
                value = mdValue_r;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getRowNumber() {
        return getSortingPos();
    }

    @Override
    public String getItemCode() {
        return getRowCode();
    }

    @Override
    public String getItemName() {
        return getRowName();
    }

    @Override
    public String getUnitCode() {
        return getDbUnitCode();
    }

    @Override
    public Vector<DTrnStockMove> getStockMoves() {
        return getAuxStockMoves();
    }

    /*
     * Other public methods
     */

    public void computeTotal() {
        int decs = DLibUtils.getDecimalFormatAmount().getMaximumFractionDigits();

        mdValue_r = DLibUtils.round(mdQuantity * mdValueUnitary, decs);
    }
}
