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
import sba.lib.DLibTimeUtils;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.fin.db.DDbYear;

/**
 *
 * @author Sergio Flores
 */
public class DDbProcessStockOpening extends DDbRegistryUser {

    protected int mnPkOpeningYearId;
    protected Date mtDate;

    public DDbProcessStockOpening() {
        super(DModConsts.TX_PRC_STK_OPE);
        initRegistry();
    }

    private void processStock(DGuiSession session) throws SQLException, Exception {
        int sort = 0;
        Statement statementIog = null;
        Statement statementIogRow = null;
        ResultSet resultSet = null;
        ResultSet resultSetIog = null;
        ResultSet resultSetIogRow = null;
        String sql1 = "";
        String sql2 = "";
        String sql3 = "";
        DDbYear year = null;
        DDbIog iog = null;
        DDbIogRow iogRow = null;
        DDbIogNote iogNote = null;
        Vector<DDbIog> iogs = new Vector<DDbIog>();

        mtDate = DLibTimeUtils.createDate(mnPkOpeningYearId, 1, 1);
        mnFkUserInsertId = session.getUser().getPkUserId();
        mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

        statementIog = session.getStatement().getConnection().createStatement();
        statementIogRow = session.getStatement().getConnection().createStatement();

        // Delete previous stock documents:

        msSql = "SELECT id_iog FROM " + DModConsts.TablesMap.get(DModConsts.T_IOG) + " "
                + "WHERE dt = '" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "' AND b_del = 0 AND b_sys = 1 AND "
                + "fk_iog_ct = " + DModSysConsts.TS_IOG_TP_IN_EXT_INV[0] + " AND "
                + "fk_iog_cl = " + DModSysConsts.TS_IOG_TP_IN_EXT_INV[1] + " AND "
                + "fk_iog_tp = " + DModSysConsts.TS_IOG_TP_IN_EXT_INV[2] + " "
                + "ORDER BY id_iog ";
        resultSetIog = statementIog.executeQuery(msSql);
        while (resultSetIog.next()) {
            iog = new DDbIog();
            iog.read(session, new int[] { resultSetIog.getInt(1) });
            iog.setSystem(false);           // system registries cannot be deleted
            if (iog.canDelete(session)) {
                iog.setSystem(true);        // opening stock documents are system ones
                iogs.add(iog);
            }
            else {
                throw new Exception(iog.getQueryResult());
            }
        }

        for (DDbIog o : iogs) {
            o.delete(session);
        }

        // Define required stock documents, one per warehouse:

        iogs.clear();

        sql1 = "SELECT id_yer, id_itm, id_unt, id_lot, id_bpr, id_bra, id_wah, snr, imp_dec, imp_dec_dt_n, "
                + "SUM(mov_in) AS f_mov_in, SUM(mov_out) AS f_mov_out, SUM(mov_in - mov_out) AS f_stk, "
                + "SUM(dbt) AS f_dbt, SUM(cdt) AS f_cdt, SUM(dbt - cdt) AS f_bal "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.T_STK) + " ";
        sql2 = "WHERE id_yer = " + (mnPkOpeningYearId - 1) + " AND b_del = 0 ";
        sql3 = "GROUP BY id_yer, id_itm, id_unt, id_lot, id_bpr, id_bra, id_wah, snr, imp_dec, imp_dec_dt_n "
                + "HAVING SUM(mov_in - mov_out) <> 0 "
                + "ORDER BY id_yer, id_itm, id_unt, id_lot, id_bpr, id_bra, id_wah, snr, imp_dec, imp_dec_dt_n ";

        msSql = "SELECT DISTINCT id_bpr, id_bra, id_wah "
                + "FROM ("
                + sql1 + sql2 + sql3
                + ") AS t "
                + "ORDER BY id_bpr, id_bra, id_wah ";
        resultSetIog = statementIog.executeQuery(msSql);
        while (resultSetIog.next()) {
            iog = new DDbIog();
            iog.setPkIogId(0);
            iog.setDate(mtDate);
            iog.setNumber(1);
            iog.setValue_r(0.0);
            iog.setAudited(false);
            iog.setDeleted(false);
            iog.setSystem(true);
            iog.setFkIogCategoryId(DModSysConsts.TS_IOG_TP_IN_EXT_INV[0]);
            iog.setFkIogClassId(DModSysConsts.TS_IOG_TP_IN_EXT_INV[1]);
            iog.setFkIogTypeId(DModSysConsts.TS_IOG_TP_IN_EXT_INV[2]);
            iog.setFkWarehouseBizPartnerId(resultSetIog.getInt("id_bpr"));
            iog.setFkWarehouseBranchId(resultSetIog.getInt("id_bra"));
            iog.setFkWarehouseWarehouseId(resultSetIog.getInt("id_wah"));
            iog.setFkBookkeepingYearId_n(DLibConsts.UNDEFINED);
            iog.setFkBookkeepingNumberId_n(DLibConsts.UNDEFINED);
            iog.setFkSourceDpsId_n(DLibConsts.UNDEFINED);
            iog.setFkSourceIogId_n(DLibConsts.UNDEFINED);
            iog.setFkUserAuditedId(DUtilConsts.USR_NA_ID);
            iog.setFkUserInsertId(mnFkUserInsertId);
            iog.setFkUserUpdateId(mnFkUserUpdateId);
            iog.setTsUserAudited(null);
            iog.setTsUserInsert(null);
            iog.setTsUserUpdate(null);

            iogNote = new DDbIogNote();
            iogNote.setText("" + mnPkOpeningYearId);

            iog.getChildNotes().add(iogNote);

            sort = 0;
            msSql = sql1 + sql2
                    + "AND id_bpr = " + resultSetIog.getInt("id_bpr") + " "
                    + "AND id_bra = " + resultSetIog.getInt("id_bra") + " "
                    + "AND id_wah = " + resultSetIog.getInt("id_wah") + " "
                    + sql3;
            resultSetIogRow = statementIogRow.executeQuery(msSql);
            while (resultSetIogRow.next()) {
                iogRow = new DDbIogRow();
                iogRow.setPkIogId(0);
                iogRow.setPkRowId(0);
                iogRow.setSortingPos(++sort);
                iogRow.setQuantity(resultSetIogRow.getDouble("f_stk"));
                iogRow.setValueUnitary(0.0);
                iogRow.setValue_r(0.0);
                iogRow.setInventoriable(true);
                iogRow.setDeleted(false);
                iogRow.setSystem(true);
                iogRow.setFkItemId(resultSetIogRow.getInt("id_itm"));
                iogRow.setFkUnitId(resultSetIogRow.getInt("id_unt"));
                iogRow.setFkDpsInvDpsId_n(DLibConsts.UNDEFINED);
                iogRow.setFkDpsInvRowId_n(DLibConsts.UNDEFINED);
                iogRow.setFkDpsAdjDpsId_n(DLibConsts.UNDEFINED);
                iogRow.setFkDpsAdjRowId_n(DLibConsts.UNDEFINED);
                iogRow.setFkUserInsertId(mnFkUserInsertId);
                iogRow.setFkUserUpdateId(mnFkUserUpdateId);
                iogRow.setTsUserInsert(null);
                iogRow.setTsUserUpdate(null);
                iogRow.getAuxStockMoves().add(new DTrnStockMove(
                        new int[] {
                            resultSetIogRow.getInt("id_itm"), resultSetIogRow.getInt("id_unt"), resultSetIogRow.getInt("id_lot"),
                            resultSetIogRow.getInt("id_bpr"), resultSetIogRow.getInt("id_bra"), resultSetIogRow.getInt("id_wah") },
                        resultSetIogRow.getDouble("f_stk"),
                        resultSetIogRow.getString("snr"),
                        resultSetIogRow.getString("imp_dec"),
                        resultSetIogRow.getDate("imp_dec_dt_n")));

                iog.getChildRows().add(iogRow);
            }

            iogs.add(iog);
        }

        // Create year registries if required:

        msSql = "SELECT count(*) FROM " + DModConsts.TablesMap.get(DModConsts.F_YER) + " " +
                "WHERE id_yer = " + mnPkOpeningYearId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            if (resultSet.getInt(1) == 0) {
                year = new DDbYear();
                year.setPkYearId(mnPkOpeningYearId);
                year.createPeriods();
                year.save(session);
            }
        }

        for (DDbIog o : iogs) {
            o.save(session);
        }
    }

    public void setPkOpeningYearId(int n) { mnPkOpeningYearId = n; }
    public void setDate(Date t) { mtDate = t; }

    public int getPkOpeningYearId() { return mnPkOpeningYearId; }
    public Date getDate() { return mtDate; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkOpeningYearId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkOpeningYearId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkOpeningYearId = 0;
        mtDate = null;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(DModConsts.F_BKK);
    }

    @Override
    public String getSqlWhere() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getSqlWhere(int[] pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(DGuiSession session, int[] pk) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        processStock(session);

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbProcessStockOpening clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
