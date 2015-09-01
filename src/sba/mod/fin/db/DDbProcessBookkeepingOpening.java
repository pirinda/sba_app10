/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.fin.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbProcessBookkeepingOpening extends DDbRegistryUser {

    protected int mnPkOpeningYearId;
    protected Date mtDate;

    public DDbProcessBookkeepingOpening() {
        super(DModConsts.FX_PRC_BKK_OPE);
        initRegistry();
    }

    private DDbBookkeepingMove createBookkeepingMove(ResultSet resultSet) throws java.sql.SQLException {
        DDbBookkeepingMove move = new DDbBookkeepingMove();

        move.setPkYearId(mnPkOpeningYearId);
        move.setPkMoveId(0);
        move.setDate(mtDate);
        move.setSupporting("");
        move.setReference(resultSet.getString("b.ref"));
        move.setText("" + mnPkOpeningYearId);

        if (resultSet.getDouble("f_bal") >= 0.0) {
            move.setDebit(resultSet.getDouble("f_bal"));
            move.setCredit(0.0);
            move.setDebitCy(resultSet.getDouble("f_bal_cy"));
            move.setCreditCy(0.0);

            move.setFkSystemMoveClassId(DModSysConsts.FS_SYS_MOV_TP_YO_DBT[0]);
            move.setFkSystemMoveTypeId(DModSysConsts.FS_SYS_MOV_TP_YO_DBT[1]);
        }
        else {
            move.setDebit(0.0);
            move.setCredit(resultSet.getDouble("f_bal") * -1.0);
            move.setDebitCy(0.0);
            move.setCreditCy(resultSet.getDouble("f_bal_cy") * -1.0);

            move.setFkSystemMoveClassId(DModSysConsts.FS_SYS_MOV_TP_YO_CDT[0]);
            move.setFkSystemMoveTypeId(DModSysConsts.FS_SYS_MOV_TP_YO_CDT[1]);
        }

        move.setExchangeRate(resultSet.getDouble("f_bal_cy") == 0.0 ? 0.0 : Math.abs(resultSet.getDouble("f_bal_cy") / resultSet.getDouble("f_bal_cy")));

        move.setUnits(0);
        move.setSortingPos(0);
        move.setExchangeRateDifference(false);
        move.setAvailable(resultSet.getBoolean("b.b_avl"));
        move.setDeleted(false);
        move.setSystem(true);
        move.setFkAccountId(resultSet.getInt("b.fk_acc"));
        move.setFkSystemAccountTypeId(resultSet.getInt("b.fk_sys_acc_tp"));

        move.setFkDiverseMoveTypeId(DModSysConsts.FS_DIV_MOV_TP_NA);
        move.setFkCurrencyId(resultSet.getInt("b.fk_cur"));
        move.setFkPaymentTypeId(DModSysConsts.FS_PAY_TP_NA);
        move.setFkModeOfPaymentTypeId(DModSysConsts.FS_MOP_TP_NA);
        move.setFkValueTypeId(DModSysConsts.FS_VAL_TP_NA);
        move.setFkOwnerBizPartnerId(resultSet.getInt("b.fk_own_bpr"));
        move.setFkOwnerBranchId(resultSet.getInt("b.fk_own_bra"));
        move.setFkCashBizPartnerId_n(DLibConsts.UNDEFINED);
        move.setFkCashBranchId_n(DLibConsts.UNDEFINED);
        move.setFkCashCashId_n(DLibConsts.UNDEFINED);
        move.setFkWarehouseBizPartnerId_n(DLibConsts.UNDEFINED);
        move.setFkWarehouseBranchId_n(DLibConsts.UNDEFINED);
        move.setFkWarehouseWarehouseId_n(DLibConsts.UNDEFINED);
        move.setFkBizPartnerBizPartnerId_n(DLibConsts.UNDEFINED);
        move.setFkBizPartnerBranchId_n(DLibConsts.UNDEFINED);
        move.setFkDpsInvId_n(DLibConsts.UNDEFINED);
        move.setFkDpsAdjId_n(DLibConsts.UNDEFINED);
        move.setFkIogId_n(DLibConsts.UNDEFINED);
        move.setFkIomId_n(DLibConsts.UNDEFINED);
        move.setFkPusId_n(DLibConsts.UNDEFINED);
        move.setFkItemId_n(DLibConsts.UNDEFINED);
        move.setFkItemAuxId_n(DLibConsts.UNDEFINED);
        move.setFkUnitId_n(DLibConsts.UNDEFINED);
        move.setFkRecordYearId_n(DLibConsts.UNDEFINED);
        move.setFkRecordRecordId_n(DLibConsts.UNDEFINED);
        move.setFkBookkeepingYearId_n(DLibConsts.UNDEFINED);
        move.setFkBookkeepingNumberId_n(DLibConsts.UNDEFINED);
        move.setFkUserAvailableId(DUtilConsts.USR_NA_ID);
        move.setFkUserInsertId(mnFkUserInsertId);
        move.setFkUserUpdateId(mnFkUserUpdateId);
        move.setTsUserAvailable(null);
        move.setTsUserInsert(null);
        move.setTsUserUpdate(null);

        return move;
    }

    private void processBookeeping(DGuiSession session) throws SQLException, Exception {
        int sort = 0;
        ResultSet resultSet = null;
        String sum = "SUM(b.dbt) AS f_dbt, SUM(b.cdt) AS f_cdt, SUM(b.dbt - b.cdt) AS f_bal, SUM(b.dbt_cy) AS f_dbt_cy, SUM(b.cdt_cy) AS f_cdt_cy, SUM(b.dbt_cy - b.cdt_cy) AS f_bal_cy ";
        String from = "FROM " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " AS b INNER JOIN fu_acc AS a ON b.fk_acc = a.id_acc AND a.fk_acc_ct = " + DModSysConsts.FS_ACC_CT_BAL + " ";
        String where = "WHERE b.id_yer = " + (mnPkOpeningYearId - 1) + " AND b.b_del = 0 ";
        String having = "HAVING SUM(b.dbt - b.cdt) <> 0 OR SUM(b.dbt_cy - b.cdt_cy) <> 0 ";
        String cols = "b.fk_acc, b.fk_sys_acc_tp, b.fk_cur, b.fk_own_bpr, b.fk_own_bra, b.ref, b.b_avl";
        String colsCsh = "b.fk_csh_bpr_n, b.fk_csh_bra_n, b.fk_csh_csh_n, b.fk_mop_tp";
        String colsWah = "b.fk_wah_bpr_n, b.fk_wah_bra_n, b.fk_wah_wah_n";
        String colsBpr = "b.fk_bpr_bpr_n, b.fk_bpr_bra_n, b.fk_dps_inv_n";
        String innerCsh = "AND b.fk_sys_acc_tp = " + DModSysConsts.FS_SYS_ACC_TP_ENT_CSH + " ";
        String innerWah = "AND b.fk_sys_acc_tp = " + DModSysConsts.FS_SYS_ACC_TP_ENT_WAH + " ";
        String innerBpr = "AND b.fk_sys_acc_tp IN (" + DModSysConsts.FS_SYS_ACC_TP_BPR_VEN + ", " + DModSysConsts.FS_SYS_ACC_TP_BPR_CUS + ", " + DModSysConsts.FS_SYS_ACC_TP_BPR_CDR + ", " + DModSysConsts.FS_SYS_ACC_TP_BPR_DBR + ") ";
        String innerOth = "AND b.fk_sys_acc_tp = " + DModSysConsts.FS_SYS_ACC_TP_NA + " ";
        DDbYear year = null;
        DDbBookkeepingMove bkkMove = null;
        Vector<DDbBookkeepingMove> bkkMoves = new Vector<DDbBookkeepingMove>();

        mtDate = DLibTimeUtils.createDate(mnPkOpeningYearId - 1, 12, 31);
        mnFkUserInsertId = session.getUser().getPkUserId();
        mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

        // Cash opening moves:

        msSql = "SELECT " + cols + ", " + colsCsh + ", " + sum + from + innerCsh + where +
                "GROUP BY " + cols + ", " + colsCsh + " " + having +
                "ORDER BY " + cols + ", " + colsCsh + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        while (resultSet.next()) {
            bkkMove = createBookkeepingMove(resultSet);

            bkkMove.setSortingPos(++sort);
            bkkMove.setFkCashBizPartnerId_n(resultSet.getInt("b.fk_csh_bpr_n"));
            bkkMove.setFkCashBranchId_n(resultSet.getInt("b.fk_csh_bra_n"));
            bkkMove.setFkCashCashId_n(resultSet.getInt("b.fk_csh_csh_n"));
            bkkMove.setFkModeOfPaymentTypeId(resultSet.getInt("b.fk_mop_tp"));

            bkkMoves.add(bkkMove);
        }

        // Warehouse opening moves:

        msSql = "SELECT " + cols + ", " + colsWah + ", " + sum + from + innerWah + where +
                "GROUP BY " + cols + ", " + colsWah + " " + having +
                "ORDER BY " + cols + ", " + colsWah + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        while (resultSet.next()) {
            bkkMove = createBookkeepingMove(resultSet);

            bkkMove.setSortingPos(++sort);
            bkkMove.setFkWarehouseBizPartnerId_n(resultSet.getInt("b.fk_wah_bpr_n"));
            bkkMove.setFkWarehouseBranchId_n(resultSet.getInt("b.fk_wah_bra_n"));
            bkkMove.setFkWarehouseWarehouseId_n(resultSet.getInt("b.fk_wah_wah_n"));

            bkkMoves.add(bkkMove);
        }

        // Business partner opening moves:

        msSql = "SELECT " + cols + ", " + colsBpr + ", " + sum + from + innerBpr + where +
                "GROUP BY " + cols + ", " + colsBpr + " " + having +
                "ORDER BY " + cols + ", " + colsBpr + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        while (resultSet.next()) {
            bkkMove = createBookkeepingMove(resultSet);

            bkkMove.setSortingPos(++sort);
            bkkMove.setFkBizPartnerBizPartnerId_n(resultSet.getInt("b.fk_bpr_bpr_n"));
            bkkMove.setFkBizPartnerBranchId_n(resultSet.getInt("b.fk_bpr_bra_n"));
            bkkMove.setFkDpsInvId_n(resultSet.getInt("b.fk_dps_inv_n"));

            bkkMoves.add(bkkMove);
        }

        // Other opening moves:

        msSql = "SELECT " + cols + ", " + sum + from + innerOth + where +
                "GROUP BY " + cols + " " + having +
                "ORDER BY " + cols + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        while (resultSet.next()) {
            bkkMove = createBookkeepingMove(resultSet);

            bkkMove.setSortingPos(++sort);

            bkkMoves.add(bkkMove);
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

        // Save bookkeeping opening moves:

        msSql = "UPDATE " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " SET b_del = 1 " +
                "WHERE id_yer = " + mnPkOpeningYearId + " AND b_del = 0 AND b_sys = 1 AND fk_sys_mov_cl = " + DModSysConsts.FS_SYS_MOV_CL_YO + " ";
        session.getStatement().execute(msSql);

        for (DDbBookkeepingMove move : bkkMoves) {
            move.save(session);
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

        processBookeeping(session);

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbProcessBookkeepingOpening clone() throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
