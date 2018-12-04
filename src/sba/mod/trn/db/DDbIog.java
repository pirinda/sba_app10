/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.fin.db.DDbAbpBranchWarehouse;
import sba.mod.fin.db.DDbAbpItem;
import sba.mod.fin.db.DDbBookkeepingMove;
import sba.mod.fin.db.DDbBookkeepingNumber;
import sba.mod.fin.db.DFinUtils;
import sba.mod.itm.db.DDbItem;

/**
 *
 * @author Sergio Flores
 */
public class DDbIog extends DDbRegistryUser {

    /*
     * DDbIog created integrally by the following methods and classes:
     * method DTrnUtils.createIogForSupply()
     * class DDbProcessStockOpening
     */

    public static final int FIELD_BKK_NUMBER = 1;
    public static final int FIELD_AUDITED = 2;

    protected int mnPkIogId;
    protected Date mtDate;
    protected int mnNumber;
    protected double mdValue_r;
    protected boolean mbAudited;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkIogCategoryId;
    protected int mnFkIogClassId;
    protected int mnFkIogTypeId;
    protected int mnFkWarehouseBizPartnerId;
    protected int mnFkWarehouseBranchId;
    protected int mnFkWarehouseWarehouseId;
    protected int mnFkBookkeepingYearId_n;
    protected int mnFkBookkeepingNumberId_n;
    protected int mnFkSourceDpsId_n;
    protected int mnFkSourceIogId_n;
    protected int mnFkUserAuditedId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUserAudited;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected Vector<DDbIogNote> mvChildNotes;
    protected Vector<DDbIogRow> mvChildRows;

    protected boolean mbAuxComputeSiblingIog;
    protected int[] manAuxSiblingIogWarehouseKey;

    protected DDbIog moSiblingIog;

    public DDbIog() {
        super(DModConsts.T_IOG);
        mvChildNotes = new Vector<>();
        mvChildRows = new Vector<>();
        initRegistry();
    }

    /*
     * Private methods
     */

    private boolean isBookkeepingRequired() {
        return !mbDeleted || mbRegistryEdited;
    }

    private boolean isStockRequired() {
        return !mbDeleted || mbRegistryEdited;
    }

    private int[] getSysMoveTypeKey() {
        int[] moveTypeKey = null;
        int[] iogTypeKey = getIogTypeKey();

        if (isIogForIn()) {
            if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_PUR_PUR)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GI_PUR_PUR;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_PUR_CHG)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GI_PUR_CHG;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_PUR_WAR)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GI_PUR_WAR;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_PUR_CSG)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GI_PUR_CSG;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_SAL_SAL)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GI_SAL_SAL;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_SAL_CHG)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GI_SAL_CHG;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_SAL_WAR)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GI_SAL_WAR;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_SAL_CSG)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GI_SAL_CSG;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_EXT_ADJ)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GI_EXT_ADJ;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_EXT_INV)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GI_EXT_INV;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_INT_TRA)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GI_INT_TRA;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_INT_CNV)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GI_INT_CNV;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_MFG_RM_ASD)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GI_MFG_RM_ASD;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_MFG_RM_RET)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GI_MFG_RM_RET;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_MFG_WP_ASD)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GI_MFG_WP_ASD;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_MFG_WP_RET)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GI_MFG_WP_RET;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_MFG_FG_ASD)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GI_MFG_FG_ASD;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_MFG_FG_RET)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GI_MFG_FG_RET;
            }
        }
        else {
            if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_PUR_PUR)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GO_PUR_PUR;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_PUR_CHG)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GO_PUR_CHG;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_PUR_WAR)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GO_PUR_WAR;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_PUR_CSG)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GO_PUR_CSG;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_SAL_SAL)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GO_SAL_SAL;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_SAL_CHG)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GO_SAL_CHG;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_SAL_WAR)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GO_SAL_WAR;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_SAL_CSG)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GO_SAL_CSG;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_EXT_ADJ)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GO_EXT_ADJ;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_EXT_INV)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GO_EXT_INV;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_INT_TRA)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GO_INT_TRA;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_INT_CNV)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GO_INT_CNV;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_MFG_RM_ASD)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GO_MFG_RM_ASD;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_MFG_RM_RET)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GO_MFG_RM_RET;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_MFG_WP_ASD)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GO_MFG_WP_ASD;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_MFG_WP_RET)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GO_MFG_WP_RET;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_MFG_FG_ASD)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GO_MFG_FG_ASD;
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_MFG_FG_RET)) {
                moveTypeKey = DModSysConsts.FS_SYS_MOV_TP_GO_MFG_FG_RET;
            }
        }

        return moveTypeKey;
    }

    private int getAccountIdForItem(final DDbAbpItem abpItem) {
        int accountId = DLibConsts.UNDEFINED;
        int[] iogTypeKey = getIogTypeKey();

        if (isIogForIn()) {
            if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_PUR_PUR)) {
                accountId = abpItem.getFkAccountInPurchaseId();
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_PUR_CHG)) {
                accountId = abpItem.getFkAccountInPurchaseChangeId();
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_PUR_WAR)) {
                accountId = abpItem.getFkAccountInPurchaseWarrantyId();
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_PUR_CSG)) {
                accountId = abpItem.getFkAccountInPurchaseConsignationId();
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_SAL_SAL)) {
                accountId = abpItem.getFkAccountInSaleId();
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_SAL_CHG)) {
                accountId = abpItem.getFkAccountInSaleChangeId();
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_SAL_WAR)) {
                accountId = abpItem.getFkAccountInSaleWarrantyId();
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_SAL_CSG)) {
                accountId = abpItem.getFkAccountInSaleConsignationId();
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_IN_EXT_ADJ)) {
                accountId = abpItem.getFkAccountInAdjustmentId();
            }
            else {
                accountId = abpItem.getFkAccountInInventoryId();
            }
        }
        else {
            if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_PUR_PUR)) {
                accountId = abpItem.getFkAccountOutPurchaseId();
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_PUR_CHG)) {
                accountId = abpItem.getFkAccountOutPurchaseChangeId();
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_PUR_WAR)) {
                accountId = abpItem.getFkAccountOutPurchaseWarrantyId();
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_PUR_CSG)) {
                accountId = abpItem.getFkAccountOutPurchaseConsignationId();
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_SAL_SAL)) {
                accountId = abpItem.getFkAccountOutSaleId();
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_SAL_CHG)) {
                accountId = abpItem.getFkAccountOutSaleChangeId();
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_SAL_WAR)) {
                accountId = abpItem.getFkAccountOutSaleWarrantyId();
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_SAL_CSG)) {
                accountId = abpItem.getFkAccountOutSaleConsignationId();
            }
            else if (DLibUtils.compareKeys(iogTypeKey, DModSysConsts.TS_IOG_TP_OUT_EXT_ADJ)) {
                accountId = abpItem.getFkAccountOutAdjustmentId();
            }
            else {
                accountId = abpItem.getFkAccountOutInventoryId();
            }
        }

        return accountId;
    }

    private DDbBookkeepingMove createBookkeepingMove(final String text, final int accountId, final int sysAccountTypeId, final int[] sysMoveTypeKey, final int currencyId) {
        DDbBookkeepingMove bkkMove = new DDbBookkeepingMove();

        bkkMove.setPkYearId(DLibTimeUtils.digestYear(mtDate)[0]);
        bkkMove.setPkMoveId(0);
        bkkMove.setDate(mtDate);
        bkkMove.setSupporting("");
        bkkMove.setReference("");
        bkkMove.setText(text);
        bkkMove.setDebit(0);
        bkkMove.setCredit(0);
        bkkMove.setExchangeRate(1);
        bkkMove.setDebitCy(0);
        bkkMove.setCreditCy(0);
        bkkMove.setUnits(0);
        bkkMove.setSortingPos(0);
        bkkMove.setExchangeRateDifference(false);
        bkkMove.setAvailable(true);
        bkkMove.setDeleted(false);
        bkkMove.setSystem(true);
        bkkMove.setFkAccountId(accountId);
        bkkMove.setFkSystemAccountTypeId(sysAccountTypeId);
        bkkMove.setFkSystemMoveClassId(sysMoveTypeKey[0]);
        bkkMove.setFkSystemMoveTypeId(sysMoveTypeKey[1]);
        bkkMove.setFkDiverseMoveTypeId(DModSysConsts.FS_DIV_MOV_TP_NA);
        bkkMove.setFkCurrencyId(currencyId);
        bkkMove.setFkPaymentTypeId(DModSysConsts.FS_PAY_TP_NA);
        bkkMove.setFkModeOfPaymentTypeId(DModSysConsts.FS_MOP_TP_NA);
        bkkMove.setFkValueTypeId(DModSysConsts.FS_VAL_TP_NA);
        bkkMove.setFkOwnerBizPartnerId(mnFkWarehouseBizPartnerId);
        bkkMove.setFkOwnerBranchId(mnFkWarehouseBranchId);
        bkkMove.setFkCashBizPartnerId_n(DLibConsts.UNDEFINED);
        bkkMove.setFkCashBranchId_n(DLibConsts.UNDEFINED);
        bkkMove.setFkCashCashId_n(DLibConsts.UNDEFINED);
        bkkMove.setFkWarehouseBizPartnerId_n(mnFkWarehouseBizPartnerId);
        bkkMove.setFkWarehouseBranchId_n(mnFkWarehouseBranchId);
        bkkMove.setFkWarehouseWarehouseId_n(mnFkWarehouseWarehouseId);
        bkkMove.setFkBizPartnerBizPartnerId_n(DLibConsts.UNDEFINED);
        bkkMove.setFkBizPartnerBranchId_n(DLibConsts.UNDEFINED);
        bkkMove.setFkDpsInvId_n(DLibConsts.UNDEFINED);
        bkkMove.setFkDpsAdjId_n(DLibConsts.UNDEFINED);
        bkkMove.setFkDfrId_n(DLibConsts.UNDEFINED);
        bkkMove.setFkIogId_n(mnPkIogId);
        bkkMove.setFkIomId_n(DLibConsts.UNDEFINED);
        bkkMove.setFkPusId_n(DLibConsts.UNDEFINED);
        bkkMove.setFkItemId_n(DLibConsts.UNDEFINED);
        bkkMove.setFkItemAuxId_n(DLibConsts.UNDEFINED);
        bkkMove.setFkUnitId_n(DLibConsts.UNDEFINED);
        bkkMove.setFkRecordYearId_n(DLibConsts.UNDEFINED);
        bkkMove.setFkRecordRecordId_n(DLibConsts.UNDEFINED);
        bkkMove.setFkBookkeepingYearId_n(mnFkBookkeepingYearId_n);
        bkkMove.setFkBookkeepingNumberId_n(mnFkBookkeepingNumberId_n);
        bkkMove.setFkUserAvailableId(DUtilConsts.USR_NA_ID);
        bkkMove.setFkUserInsertId(DUtilConsts.USR_NA_ID);
        bkkMove.setFkUserUpdateId(DUtilConsts.USR_NA_ID);
        bkkMove.setTsUserAvailable(null);
        bkkMove.setTsUserInsert(null);
        bkkMove.setTsUserUpdate(null);

        return bkkMove;
    }

    private void computeBookkeeping(final DGuiSession session) throws SQLException, Exception {
        int currency = session.getSessionCustom().getLocalCurrencyKey()[0];
        int[] sysMoveTypeKey = null;
        String text = "";
        DDbAbpBranchWarehouse abpWarehouse = null;
        DDbAbpItem abpItem = null;
        DDbBookkeepingMove bkkMove = null;
        DDbBookkeepingNumber bkkNumber = null;
        Vector<DDbBookkeepingMove> bkkMoves = new Vector<>();

        // Delete previous bookkeeping moves, if any:

        if (mnFkSourceDpsId_n == DLibConsts.UNDEFINED && mnFkSourceIogId_n == DLibConsts.UNDEFINED) {
            if (mnFkBookkeepingYearId_n != DLibConsts.UNDEFINED && mnFkBookkeepingNumberId_n != DLibConsts.UNDEFINED) {
                bkkNumber = new DDbBookkeepingNumber();
                bkkNumber.read(session, new int[] { mnFkBookkeepingYearId_n, mnFkBookkeepingNumberId_n });
                bkkNumber.setDeleted(true);
                bkkNumber.save(session);
            }
        }

        if (isBookkeepingRequired()) {
            // Save bookkeeping moves:

            if (mnFkSourceDpsId_n == DLibConsts.UNDEFINED && mnFkSourceIogId_n == DLibConsts.UNDEFINED) {
                bkkNumber = new DDbBookkeepingNumber();
                bkkNumber.setPkYearId(DLibTimeUtils.digestYear(mtDate)[0]);
                bkkNumber.save(session);

                mnFkBookkeepingYearId_n = bkkNumber.getPkYearId();
                mnFkBookkeepingNumberId_n = bkkNumber.getPkNumberId();

                msSql = "UPDATE " + getSqlTable() + " SET fk_bkk_yer_n = " + mnFkBookkeepingYearId_n + ", fk_bkk_num_n = " + mnFkBookkeepingNumberId_n + " " + getSqlWhere();
                session.getStatement().execute(msSql);
            }

            // Warehouse account:

            text = getBookkeepingText(session);
            abpWarehouse = DFinUtils.readAbpBranchWarehouse(session, getBranchWarehouseKey());
            sysMoveTypeKey = getSysMoveTypeKey();

            if (isIogForIn()) {
                // Goods in:

                for (DDbIogRow row : mvChildRows) {
                    if (!row.isDeleted() && row.isInventoriable()) {
                        abpItem = DFinUtils.readAbpItem(session, new int[] { row.getFkItemId() });

                        bkkMove = createBookkeepingMove(text, abpWarehouse.getFkAccountWarehouseId(), DModSysConsts.FS_SYS_ACC_TP_ENT_WAH, sysMoveTypeKey, currency);
                        bkkMove.setDebit(row.getValue_r());
                        bkkMove.setCredit(0);
                        bkkMove.setDebitCy(row.getValue_r());
                        bkkMove.setCreditCy(0);
                        bkkMoves.add(bkkMove);

                        bkkMove = createBookkeepingMove(text, getAccountIdForItem(abpItem), DModSysConsts.FS_SYS_ACC_TP_NA, sysMoveTypeKey, currency);
                        bkkMove.setDebit(0);
                        bkkMove.setCredit(row.getValue_r());
                        bkkMove.setDebitCy(0);
                        bkkMove.setCreditCy(row.getValue_r());
                        bkkMoves.add(bkkMove);
                    }
                }
            }
            else {
                // Goods out:

                for (DDbIogRow row : mvChildRows) {
                    if (!row.isDeleted() && row.isInventoriable()) {
                        abpItem = DFinUtils.readAbpItem(session, new int[] { row.getFkItemId() });

                        bkkMove = createBookkeepingMove(text, abpWarehouse.getFkAccountWarehouseId(), DModSysConsts.FS_SYS_ACC_TP_ENT_WAH, sysMoveTypeKey, currency);
                        bkkMove.setDebit(0);
                        bkkMove.setCredit(row.getValue_r());
                        bkkMove.setDebitCy(0);
                        bkkMove.setCreditCy(row.getValue_r());
                        bkkMoves.add(bkkMove);

                        bkkMove = createBookkeepingMove(text, getAccountIdForItem(abpItem), DModSysConsts.FS_SYS_ACC_TP_NA, sysMoveTypeKey, currency);
                        bkkMove.setDebit(row.getValue_r());
                        bkkMove.setCredit(0);
                        bkkMove.setDebitCy(row.getValue_r());
                        bkkMove.setCreditCy(0);
                        bkkMoves.add(bkkMove);
                    }
                }
            }

            for (DDbBookkeepingMove move : bkkMoves) {
                move.save(session);
            }
        }
    }

    private DDbStockMove createStockMove(final DDbIogRow iogRow, final int[] lotKey, final double quantity, final String serialNumber, final String importDeclaration, final Date importDeclarationDate_n, final String lot, final Date dateExpiration_n) {
        DDbStockMove stockMove = new DDbStockMove();

        stockMove.setPkYearId(DLibTimeUtils.digestYear(mtDate)[0]);
        stockMove.setPkItemId(lotKey[0]);
        stockMove.setPkUnitId(lotKey[1]);
        stockMove.setPkLotId(lotKey[2]);
        stockMove.setPkBizPartnerId(mnFkWarehouseBizPartnerId);
        stockMove.setPkBranchId(mnFkWarehouseBranchId);
        stockMove.setPkWarehouseId(mnFkWarehouseWarehouseId);
        stockMove.setPkMoveId(0);
        stockMove.setDate(mtDate);

        if (DTrnUtils.isIogForStockIn(this)) {
            stockMove.setMoveIn(quantity);
            stockMove.setMoveOut(0);
        }
        else {
            stockMove.setMoveIn(0);
            stockMove.setMoveOut(quantity);
        }

        stockMove.setCostUnitary(iogRow.getValueUnitary());
        stockMove.setCost(0);
        stockMove.setDebit(0);      // set by DDbStockMove.computeValue()
        stockMove.setCredit(0);     // set by DDbStockMove.computeValue()
        stockMove.setSerialNumber(serialNumber);
        stockMove.setImportDeclaration(importDeclaration);
        stockMove.setImportDeclarationDate_n(importDeclarationDate_n);
        stockMove.setDeleted(false);
        stockMove.setFkIogCategoryId(mnFkIogCategoryId);
        stockMove.setFkIogClassId(mnFkIogClassId);
        stockMove.setFkIogTypeId(mnFkIogTypeId);
        stockMove.setFkIogIogId(iogRow.getPkIogId());
        stockMove.setFkIogRowId(iogRow.getPkRowId());
        stockMove.setFkDpsInvDpsId_n(iogRow.getFkDpsInvDpsId_n());
        stockMove.setFkDpsInvRowId_n(iogRow.getFkDpsInvRowId_n());
        stockMove.setFkDpsAdjDpsId_n(iogRow.getFkDpsAdjDpsId_n());
        stockMove.setFkDpsAdjRowId_n(iogRow.getFkDpsAdjRowId_n());
        stockMove.setFkBookkeepingYearId_n(mnFkBookkeepingYearId_n);
        stockMove.setFkBookkeepingNumberId_n(mnFkBookkeepingNumberId_n);

        stockMove.setAuxLot(lot);
        stockMove.setAuxDateExpiration_n(dateExpiration_n);

        stockMove.computeValue();

        return stockMove;
    }

    private void computeStock(final DGuiSession session) throws SQLException, Exception {
        int year = 0;
        boolean isProcessed = false;
        boolean isImportDeclaration = false;
        double quantityRow = 0;
        double quantityIogRow = 0;
        String sql = "";
        ResultSet resultSet = null;
        DTrnStockMove auxMoveNew = null;
        DTrnStockMove auxMoveUsed = null;
        ArrayList<DTrnStockMove> tsmMovesRow = new ArrayList<DTrnStockMove>();
        ArrayList<DTrnStockMove> tsmMovesNew = new ArrayList<DTrnStockMove>();
        ArrayList<DTrnStockMove> tsmMovesUsed = new ArrayList<DTrnStockMove>();
        ArrayList<DDbStockMove> dbStockMoves = new ArrayList<DDbStockMove>();

        // Delete previous stock moves, if any:

        sql = "UPDATE " + DModConsts.TablesMap.get(DModConsts.T_STK) + " " +
                "SET b_del = 1 " +
                "WHERE b_del = 0 AND fk_iog_iog = " + mnPkIogId + " ";
        session.getStatement().execute(sql);

        if (isStockRequired()) {
            // Save stock moves:

            if (isIogForOut()) {
                year = DLibTimeUtils.digestYear(mtDate)[0];
                isImportDeclaration = ((DDbConfigCompany) session.getConfigCompany()).isImportDeclaration();
            }

            for (DDbIogRow iogRow : mvChildRows) {
                if (!iogRow.isDeleted() && iogRow.isInventoriable()) {
                    /*
                     * POSIBLE SCENARIOS:
                     * 1. Incoming items:
                     *      When import declaration information is required, it is already provided.
                     * 2. Outgoing items:
                     *      When import declaration information is required, it needs to be defined by system.
                     *      If this document is a transfer move, then the information of import declaration will be preserved when the former is cloned.
                     *
                     * WARNING!:
                     * If document is being reactivated, it is pretty posible that picked import declarations will be different to the original ones.
                     */

                    if (isIogForOut() && isImportDeclaration) {
                        quantityIogRow = 0;
                        tsmMovesRow.clear();
                        tsmMovesNew.clear();

                        // Process IOG row auxiliar stock moves to consolidate them (note that former information of import declarations will be lost):

                        for (DTrnStockMove tsmIogRow : iogRow.getAuxStockMoves()) {
                            isProcessed = false;

                            for (DTrnStockMove tsmRow : tsmMovesRow) {
                                if (DLibUtils.compareKeys(tsmRow.getStockMoveKey(), tsmIogRow.getStockMoveKey()) &&
                                        tsmRow.getSerialNumber().compareTo(tsmIogRow.getSerialNumber()) == 0) {
                                    tsmRow.setQuantity(tsmRow.getQuantity() + tsmIogRow.getQuantity());
                                    isProcessed = true;
                                    break;
                                }
                            }

                            if (!isProcessed) {
                                tsmMovesRow.add(tsmIogRow.clone());
                            }
                        }

                        // Process consolidated IOG row auxiliar stock moves:

                        for (DTrnStockMove tsmRow : tsmMovesRow) {
                            quantityRow = 0;

                            // Consume available imported stocks:

                            sql = "SELECT imp_dec_dt_n IS NULL AS f_dt_n, imp_dec_dt_n, imp_dec, SUM(mov_in - mov_out) AS f_stk " +
                                    "FROM " + DModConsts.TablesMap.get(DModConsts.T_STK) + " " +
                                    "WHERE b_del = 0 AND id_yer = " + year + " AND " +
                                    "id_itm = " + tsmRow.getPkItemId() + " AND id_unt = " + tsmRow.getPkUnitId() + " AND id_lot = " + tsmRow.getPkLotId() + " AND " +
                                    "id_bpr = " + tsmRow.getPkBizPartnerId() + " AND id_bra = " + tsmRow.getPkBranchId() + " AND id_wah = " + tsmRow.getPkWarehouseId() + " AND " +
                                    "snr = '" + tsmRow.getSerialNumber() + "' " +
                                    "GROUP BY imp_dec_dt_n, imp_dec " +
                                    "HAVING f_stk > 0 " +
                                    "ORDER BY f_dt_n, imp_dec_dt_n, imp_dec ";
                            resultSet = session.getStatement().executeQuery(sql);
                            while (resultSet.next() && quantityRow < tsmRow.getQuantity()) {
                                auxMoveUsed = null;

                                for (DTrnStockMove tsmUsed : tsmMovesUsed) {
                                    if (DLibUtils.compareKeys(tsmUsed.getStockMoveKey(), tsmRow.getStockMoveKey()) &&
                                            tsmUsed.getSerialNumber().compareTo(tsmRow.getSerialNumber()) == 0 &&
                                            tsmUsed.getImportDeclaration().compareTo(resultSet.getString("imp_dec")) == 0 &&
                                            ((tsmUsed.getImportDeclarationDate() == null && resultSet.getDate("imp_dec_dt_n") == null) ||
                                            (tsmUsed.getImportDeclarationDate() != null && resultSet.getDate("imp_dec_dt_n") != null &&
                                            tsmUsed.getImportDeclarationDate().compareTo(resultSet.getDate("imp_dec_dt_n")) == 0))) {
                                        auxMoveUsed = tsmUsed;
                                        break;
                                    }
                                }

                                if (auxMoveUsed == null) {
                                    auxMoveUsed = tsmRow.clone();
                                    auxMoveUsed.setQuantity(0);
                                    auxMoveUsed.setImportDeclaration(resultSet.getString("imp_dec"));
                                    auxMoveUsed.setImportDeclarationDate(resultSet.getDate("imp_dec_dt_n"));
                                    tsmMovesUsed.add(auxMoveUsed);
                                }

                                auxMoveNew = tsmRow.clone();
                                auxMoveNew.setQuantity(0);
                                auxMoveNew.setImportDeclaration(resultSet.getString("imp_dec"));
                                auxMoveNew.setImportDeclarationDate(resultSet.getDate("imp_dec_dt_n"));

                                if (resultSet.getDouble("f_stk") - auxMoveUsed.getQuantity() > 0) {
                                    if ((resultSet.getDouble("f_stk") - auxMoveUsed.getQuantity()) <= (tsmRow.getQuantity() - quantityRow)) {
                                        auxMoveNew.setQuantity(resultSet.getDouble("f_stk") - auxMoveUsed.getQuantity());
                                    }
                                    else {
                                        auxMoveNew.setQuantity(tsmRow.getQuantity() - quantityRow);
                                    }

                                    quantityRow += auxMoveNew.getQuantity();
                                    quantityIogRow += auxMoveNew.getQuantity();
                                    auxMoveUsed.setQuantity(auxMoveUsed.getQuantity() + auxMoveNew.getQuantity());
                                    tsmMovesNew.add(auxMoveNew);
                                }
                            }
                        }

                        if (quantityIogRow != iogRow.getQuantity()) {
                            throw new Exception("Error al procesar las partida # " + iogRow.getRowNumber() + " " +
                            "el ítem '" + iogRow.getItemName() + "'\n" +
                            "requiere de " + DLibUtils.getDecimalFormatQuantity().format(iogRow.getQuantity()) + " " + iogRow.getUnitCode() + ", " +
                            "pero se obtuvieron " + DLibUtils.getDecimalFormatQuantity().format(quantityRow) + " " + iogRow.getUnitCode() + " del almacén " +
                            session.readField(DModConsts.CU_WAH, getBranchWarehouseKey(), DDbRegistry.FIELD_NAME) + ".");
                        }

                        iogRow.getAuxStockMoves().clear();
                        iogRow.getAuxStockMoves().addAll(tsmMovesNew);
                    }

                    for (DTrnStockMove tsmIogRow : iogRow.getAuxStockMoves()) {
                        dbStockMoves.add(createStockMove(iogRow, tsmIogRow.getLotKey(), tsmIogRow.getQuantity(), tsmIogRow.getSerialNumber(), tsmIogRow.getImportDeclaration(), tsmIogRow.getImportDeclarationDate(), tsmIogRow.getLot(), tsmIogRow.getDateExpiration()));
                    }
                }
            }

            for (DDbStockMove move : dbStockMoves) {
                move.save(session);
            }
        }
    }

    private DDbIog createSiblingIog(final DGuiSession session, final int[] siblingIogWarehouseKey) throws Exception {
        int[] key = null;
        double quantity = 0;
        long serialNumber = 0;
        DDbItem itemOriginal = null;
        DDbItem itemConverted = null;
        DTrnStockMove tsmMoveConverted = null;
        Vector<DTrnStockMove> tsmMovesConverted = new Vector<>();
        DDbIog iogSibling = this.clone();

        iogSibling.setAuxComputeSiblingIog(false);  // a sibling document cannot have aswell a sibling

        key = DTrnUtils.getIogTypeForSibling(this.getIogTypeKey());
        iogSibling.setFkIogCategoryId(key[0]);
        iogSibling.setFkIogClassId(key[1]);
        iogSibling.setFkIogTypeId(key[2]);

        iogSibling.setSystem(true);
        iogSibling.setFkWarehouseBizPartnerId(siblingIogWarehouseKey[0]);
        iogSibling.setFkWarehouseBranchId(siblingIogWarehouseKey[1]);
        iogSibling.setFkWarehouseWarehouseId(siblingIogWarehouseKey[2]);

        if (this.isRegistryNew()) {
            iogSibling.setNumber(DTrnUtils.getNextNumberForIog(session, iogSibling.getFkIogCategoryId(), iogSibling.getBranchWarehouseKey()));
        }
        else {
            iogSibling.setNumber(moSiblingIog.getNumber());
        }

        if (DTrnUtils.isIogTypeForTransfer(this.getIogTypeKey())) {
            /*
             * TRANSFER MOVE
             * Note: information of import declaration is preserved.
             */

            throw new UnsupportedOperationException("Not supported yet.");
        }
        else if (DTrnUtils.isIogTypeForConversion(this.getIogTypeKey())) {
            /*
             * CONVERSION MOVE
             * Note: information of import declaration is extingued.
             */

            for (DDbIogRow iogRowSibling : iogSibling.getChildRows()) {
                itemOriginal = (DDbItem) session.readRegistry(DModConsts.IU_ITM, new int[] { iogRowSibling.getFkItemId() });
                itemConverted = (DDbItem) session.readRegistry(DModConsts.IU_ITM, new int[] { itemOriginal.getFkItemPackageId_n() });

                if (itemConverted == null) {
                    throw new Exception("El ítem '" + itemOriginal.getName() + "' no está configurado para conversión.");
                }
                else {
                    tsmMovesConverted.clear();

                    for (DTrnStockMove moveSibling : iogRowSibling.getAuxStockMoves()) {
                        serialNumber = DLibUtils.parseLong(moveSibling.getSerialNumber());

                        if (itemOriginal.getUnitsPackage() == 0 && serialNumber != 0) {
                            for (int i = 0; i < moveSibling.getQuantity(); i++) {
                                tsmMoveConverted = moveSibling.clone();
                                tsmMoveConverted.setPkItemId(itemConverted.getPkItemId());
                                tsmMoveConverted.setPkUnitId(itemConverted.getFkUnitId());
                                tsmMoveConverted.setPkLotId(moveSibling.getPkLotId());
                                tsmMoveConverted.setQuantity(1);
                                tsmMoveConverted.setSerialNumber("" + (serialNumber + i));
                                tsmMovesConverted.add(tsmMoveConverted);

                                quantity += tsmMoveConverted.getQuantity();
                            }
                        }
                        else {
                            tsmMoveConverted = moveSibling.clone();
                            tsmMoveConverted.setPkItemId(itemConverted.getPkItemId());
                            tsmMoveConverted.setPkUnitId(itemConverted.getFkUnitId());
                            tsmMoveConverted.setPkLotId(moveSibling.getPkLotId());
                            tsmMoveConverted.setQuantity(moveSibling.getQuantity() * itemConverted.getUnitsPackage());
                            tsmMovesConverted.add(tsmMoveConverted);

                            quantity += tsmMoveConverted.getQuantity();
                        }
                    }

                    // Redefine quantity:

                    iogRowSibling.setQuantity(quantity);
                    iogRowSibling.setValueUnitary(quantity == 0 ? 0 : iogRowSibling.getValue_r() / quantity);

                    // Incorporate stock moves:

                    iogRowSibling.getAuxStockMoves().clear();
                    iogRowSibling.getAuxStockMoves().addAll(tsmMovesConverted);
                }
            }
        }

        return iogSibling;
    }

    private boolean canChangeStatus(final DGuiSession session, final boolean activate, final boolean isSibling) throws SQLException, Exception {
        int year = 0;
        boolean canChange = true;

        if (mbSystem && !isSibling) {
            canChange = false;
            msQueryResult = DDbConsts.MSG_REG_ + mnNumber + DDbConsts.MSG_REG_IS_SYSTEM;
        }
        else {
            if (canChange && getBranchWarehouseKey() != null) {
                year = DLibTimeUtils.digestYear(mtDate)[0];

                for (DDbIogRow row : mvChildRows) {
                    if (!row.isDeleted() && row.isInventoriable()) {
                        if (activate) {
                            // Activate an inactive document:

                            if (DTrnUtils.isIogForStockIn(this)) {
                                if (!DTrnDocRowUtils.validateRowIn(session, year, row)) {
                                    canChange = false;
                                    break;
                                }
                            }
                            else if (DTrnUtils.isIogForStockOut(this)) {
                                if (!DTrnDocRowUtils.validateRowOut(session, year, row, new Vector<DTrnDocRow>(mvChildRows))) {
                                    canChange = false;
                                    break;
                                }
                            }
                        }
                        else {
                            // Inactivate an active document:

                            if (DTrnUtils.isIogForStockOut(this)) {
                                if (!DTrnDocRowUtils.validateRowIn(session, year, row)) {
                                    canChange = false;
                                    break;
                                }
                            }
                            else if (DTrnUtils.isIogForStockIn(this)) {
                                if (!DTrnDocRowUtils.validateRowOut(session, year, row, new Vector<DTrnDocRow>(mvChildRows))) {
                                    canChange = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        return canChange;
    }

    /*
     * Public methods
     */

    public boolean isIogForIn() {
        return DTrnUtils.isIogForStockIn(this);
    }

    public boolean isIogForOut() {
        return DTrnUtils.isIogForStockOut(this);
    }

    public String getBookkeepingText(final DGuiSession session) {
        String text = "";

        if (isIogForIn()) {
            text = "E ";
        }
        else {
            text = "S ";
        }

        text += mnNumber;

        if (text.length() > DDbBookkeepingMove.LEN_TEXT) {
            text = text.substring(0, DDbBookkeepingMove.LEN_TEXT);
        }

        return text;
    }

    public void setPkIogId(int n) { mnPkIogId = n; }
    public void setDate(Date t) { mtDate = t; }
    public void setNumber(int n) { mnNumber = n; }
    public void setValue_r(double d) { mdValue_r = d; }
    public void setAudited(boolean b) { mbAudited = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkIogCategoryId(int n) { mnFkIogCategoryId = n; }
    public void setFkIogClassId(int n) { mnFkIogClassId = n; }
    public void setFkIogTypeId(int n) { mnFkIogTypeId = n; }
    public void setFkWarehouseBizPartnerId(int n) { mnFkWarehouseBizPartnerId = n; }
    public void setFkWarehouseBranchId(int n) { mnFkWarehouseBranchId = n; }
    public void setFkWarehouseWarehouseId(int n) { mnFkWarehouseWarehouseId = n; }
    public void setFkBookkeepingYearId_n(int n) { mnFkBookkeepingYearId_n = n; }
    public void setFkBookkeepingNumberId_n(int n) { mnFkBookkeepingNumberId_n = n; }
    public void setFkSourceDpsId_n(int n) { mnFkSourceDpsId_n = n; }
    public void setFkSourceIogId_n(int n) { mnFkSourceIogId_n = n; }
    public void setFkUserAuditedId(int n) { mnFkUserAuditedId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserAudited(Date t) { mtTsUserAudited = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkIogId() { return mnPkIogId; }
    public Date getDate() { return mtDate; }
    public int getNumber() { return mnNumber; }
    public double getValue_r() { return mdValue_r; }
    public boolean isAudited() { return mbAudited; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkIogCategoryId() { return mnFkIogCategoryId; }
    public int getFkIogClassId() { return mnFkIogClassId; }
    public int getFkIogTypeId() { return mnFkIogTypeId; }
    public int getFkWarehouseBizPartnerId() { return mnFkWarehouseBizPartnerId; }
    public int getFkWarehouseBranchId() { return mnFkWarehouseBranchId; }
    public int getFkWarehouseWarehouseId() { return mnFkWarehouseWarehouseId; }
    public int getFkBookkeepingYearId_n() { return mnFkBookkeepingYearId_n; }
    public int getFkBookkeepingNumberId_n() { return mnFkBookkeepingNumberId_n; }
    public int getFkSourceDpsId_n() { return mnFkSourceDpsId_n; }
    public int getFkSourceIogId_n() { return mnFkSourceIogId_n; }
    public int getFkUserAuditedId() { return mnFkUserAuditedId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserAudited() { return mtTsUserAudited; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public Vector<DDbIogNote> getChildNotes() { return mvChildNotes; }
    public Vector<DDbIogRow> getChildRows() { return mvChildRows; }

    public void setAuxComputeSiblingIog(boolean b) { mbAuxComputeSiblingIog = b; }
    public void setAuxSiblingIogWarehouseKey(int[] key) { manAuxSiblingIogWarehouseKey = key; }

    public boolean isAuxComputeSiblingIog() { return mbAuxComputeSiblingIog; }
    public int[] getAuxSiblingIogWarehouseKey() { return manAuxSiblingIogWarehouseKey; }

    public void setSiblingIog(DDbIog o) { moSiblingIog = o; }

    public DDbIog getSiblingIog() { return moSiblingIog; }

    public int[] getIogCategoryKey() { return new int[] { mnFkIogCategoryId }; }
    public int[] getIogClassKey() { return new int[] { mnFkIogCategoryId, mnFkIogClassId }; }
    public int[] getIogTypeKey() { return new int[] { mnFkIogCategoryId, mnFkIogClassId, mnFkIogTypeId }; }
    public int[] getCompanyKey() { return new int[] { mnFkWarehouseBizPartnerId }; }
    public int[] getCompanyBranchKey() { return new int[] { mnFkWarehouseBizPartnerId, mnFkWarehouseBranchId }; }
    public int[] getBranchWarehouseKey() { return new int[] { mnFkWarehouseBizPartnerId, mnFkWarehouseBranchId, mnFkWarehouseWarehouseId }; }
    public int[] getBookkeepingNumberKey_n() { return mnFkBookkeepingYearId_n == DLibConsts.UNDEFINED || mnFkBookkeepingNumberId_n == DLibConsts.UNDEFINED ? null : new int[] { mnFkBookkeepingYearId_n, mnFkBookkeepingNumberId_n }; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkIogId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkIogId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkIogId = 0;
        mtDate = null;
        mnNumber = 0;
        mdValue_r = 0;
        mbAudited = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkIogCategoryId = 0;
        mnFkIogClassId = 0;
        mnFkIogTypeId = 0;
        mnFkWarehouseBizPartnerId = 0;
        mnFkWarehouseBranchId = 0;
        mnFkWarehouseWarehouseId = 0;
        mnFkBookkeepingYearId_n = 0;
        mnFkBookkeepingNumberId_n = 0;
        mnFkSourceDpsId_n = 0;
        mnFkSourceIogId_n = 0;
        mnFkUserAuditedId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserAudited = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        mvChildNotes.clear();
        mvChildRows.clear();

        mbAuxComputeSiblingIog = false;
        manAuxSiblingIogWarehouseKey = null;

        moSiblingIog = null;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_iog = " + mnPkIogId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_iog = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkIogId = 0;

        msSql = "SELECT COALESCE(MAX(id_iog), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkIogId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(DGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
        Statement statementAux = null;
        ResultSet resultSet = null;
        ResultSet resultSetAux = null;

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
            mtDate = resultSet.getDate("dt");
            mnNumber = resultSet.getInt("num");
            mdValue_r = resultSet.getDouble("val_r");
            mbAudited = resultSet.getBoolean("b_aud");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkIogCategoryId = resultSet.getInt("fk_iog_ct");
            mnFkIogClassId = resultSet.getInt("fk_iog_cl");
            mnFkIogTypeId = resultSet.getInt("fk_iog_tp");
            mnFkWarehouseBizPartnerId = resultSet.getInt("fk_wah_bpr");
            mnFkWarehouseBranchId = resultSet.getInt("fk_wah_bra");
            mnFkWarehouseWarehouseId = resultSet.getInt("fk_wah_wah");
            mnFkBookkeepingYearId_n = resultSet.getInt("fk_bkk_yer_n");
            mnFkBookkeepingNumberId_n = resultSet.getInt("fk_bkk_num_n");
            mnFkSourceDpsId_n = resultSet.getInt("fk_src_dps_n");
            mnFkSourceIogId_n = resultSet.getInt("fk_src_iog_n");
            mnFkUserAuditedId = resultSet.getInt("fk_usr_aud");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserAudited = resultSet.getTimestamp("ts_usr_aud");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();
            statementAux = session.getStatement().getConnection().createStatement();

            msSql = "SELECT id_not FROM " + DModConsts.TablesMap.get(DModConsts.T_IOG_NOT) + " " + getSqlWhere() +
                    "ORDER BY id_not ";
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbIogNote child = new DDbIogNote();
                child.read(session, new int[] { mnPkIogId, resultSet.getInt(1) });
                mvChildNotes.add(child);
            }

            msSql = "SELECT id_row FROM " + DModConsts.TablesMap.get(DModConsts.T_IOG_ROW) + " " + getSqlWhere() +
                    "ORDER BY id_row ";
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbIogRow child = new DDbIogRow();
                child.read(session, new int[] { mnPkIogId, resultSet.getInt(1) });

                // Read aswell row serial numbers and information of import declarations:

                msSql = "SELECT s.id_itm, s.id_unt, s.id_lot, s.id_bpr, s.id_bra, s.id_wah, s.id_mov, s.mov_in, s.mov_out, s.snr, s.imp_dec, s.imp_dec_dt_n, l.lot, l.dt_exp_n " +
                        "FROM " + DModConsts.TablesMap.get(DModConsts.T_STK) + " AS s " +
                        "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.T_LOT) + " AS l ON " +
                        "s.id_itm = l.id_itm AND s.id_unt = l.id_unt AND s.id_lot = l.id_lot " +
                        "WHERE s.fk_iog_iog = " + child.getPkIogId() + " AND s.fk_iog_row = " + child.getPkRowId() + " AND " +
                        "s.fk_bkk_yer_n = " + mnFkBookkeepingYearId_n + " AND s.fk_bkk_num_n = " + mnFkBookkeepingNumberId_n + " " +
                        "ORDER BY s.id_itm, s.id_unt, s.id_lot, s.id_bpr, s.id_bra, s.id_wah, s.id_mov ";
                resultSetAux = statementAux.executeQuery(msSql);
                while (resultSetAux.next()) {
                    DTrnStockMove move = new DTrnStockMove(
                            new int[] { resultSetAux.getInt(1), resultSetAux.getInt(2), resultSetAux.getInt(3), resultSetAux.getInt(4), resultSetAux.getInt(5), resultSetAux.getInt(6) },
                            DTrnUtils.isIogForStockIn(this) ? resultSetAux.getDouble("s.mov_in") : resultSetAux.getDouble("s.mov_out"),
                            resultSetAux.getString("s.snr"),
                            resultSetAux.getString("s.imp_dec"),
                            resultSetAux.getDate("s.imp_dec_dt_n"),
                            resultSetAux.getString("l.lot"),
                            resultSetAux.getDate("l.dt_exp_n"));
                    child.getAuxStockMoves().add(move);
                }

                mvChildRows.add(child);
            }

            // Check if there is a sibling document:

            msSql = "SELECT id_iog FROM " + DModConsts.TablesMap.get(DModConsts.T_IOG) + " " +
                    "WHERE fk_src_iog_n = " + mnPkIogId + " AND fk_bkk_yer_n = " + mnFkBookkeepingYearId_n + " AND fk_bkk_num_n = " + mnFkBookkeepingNumberId_n + " ";
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {
                moSiblingIog = new DDbIog();
                moSiblingIog.read(session, new int[] { resultSet.getInt(1) });
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
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    mnNumber + ", " +
                    mdValue_r + ", " +
                    (mbAudited ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkIogCategoryId + ", " +
                    mnFkIogClassId + ", " +
                    mnFkIogTypeId + ", " +
                    mnFkWarehouseBizPartnerId + ", " +
                    mnFkWarehouseBranchId + ", " +
                    mnFkWarehouseWarehouseId + ", " +
                    (mnFkBookkeepingYearId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBookkeepingYearId_n) + ", " +
                    (mnFkBookkeepingNumberId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBookkeepingNumberId_n) + ", " +
                    (mnFkSourceDpsId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkSourceDpsId_n) + ", " +
                    (mnFkSourceIogId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkSourceIogId_n) + ", " +
                    mnFkUserAuditedId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_iog = " + mnPkIogId + ", " +
                    "dt = '" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "num = " + mnNumber + ", " +
                    "val_r = " + mdValue_r + ", " +
                    //"b_aud = " + (mbAudited ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_iog_ct = " + mnFkIogCategoryId + ", " +
                    "fk_iog_cl = " + mnFkIogClassId + ", " +
                    "fk_iog_tp = " + mnFkIogTypeId + ", " +
                    "fk_wah_bpr = " + mnFkWarehouseBizPartnerId + ", " +
                    "fk_wah_bra = " + mnFkWarehouseBranchId + ", " +
                    "fk_wah_wah = " + mnFkWarehouseWarehouseId + ", " +
                    "fk_bkk_yer_n = " + (mnFkBookkeepingYearId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBookkeepingYearId_n) + ", " +
                    "fk_bkk_num_n = " + (mnFkBookkeepingNumberId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBookkeepingNumberId_n) + ", " +
                    "fk_src_dps_n = " + (mnFkSourceDpsId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkSourceDpsId_n) + ", " +
                    "fk_src_iog_n = " + (mnFkSourceIogId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkSourceIogId_n) + ", " +
                    /*
                    "fk_usr_aud = " + mnFkUserAuditedId + ", " +
                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    */
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    /*
                    "ts_usr_aud = " + "NOW()" + ", " +
                    "ts_usr_ins = " + "NOW()" + ", " +
                    */
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        msSql = "DELETE FROM " + DModConsts.TablesMap.get(DModConsts.T_IOG_NOT) + " " + getSqlWhere();
        session.getStatement().execute(msSql);

        for (DDbIogNote child : mvChildNotes) {
            child.setPkIogId(mnPkIogId);
            child.setRegistryNew(true);
            child.save(session);
        }

        for (DDbIogRow child : mvChildRows) {
            if (child.isRegistryNew() || child.isRegistryEdited()) {
                child.setPkIogId(mnPkIogId);
                child.save(session);
            }
        }

        // Aditional processing:

        computeBookkeeping(session);
        computeStock(session);

        // Save siblign document if required:

        if (mbAuxComputeSiblingIog) {
            moSiblingIog = createSiblingIog(session, manAuxSiblingIogWarehouseKey);
            mbAuxComputeSiblingIog = false;
        }

        if (moSiblingIog != null) {
            moSiblingIog.setFkSourceIogId_n(mnPkIogId);
            moSiblingIog.setFkBookkeepingYearId_n(mnFkBookkeepingYearId_n);
            moSiblingIog.setFkBookkeepingNumberId_n(mnFkBookkeepingNumberId_n);
            moSiblingIog.setDeleted(mbDeleted);
            moSiblingIog.save(session);
        }

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbIog clone() throws CloneNotSupportedException {
        DDbIog registry = new DDbIog();

        registry.setPkIogId(this.getPkIogId());
        registry.setDate(this.getDate());
        registry.setNumber(this.getNumber());
        registry.setValue_r(this.getValue_r());
        registry.setAudited(this.isAudited());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkIogCategoryId(this.getFkIogCategoryId());
        registry.setFkIogClassId(this.getFkIogClassId());
        registry.setFkIogTypeId(this.getFkIogTypeId());
        registry.setFkWarehouseBizPartnerId(this.getFkWarehouseBizPartnerId());
        registry.setFkWarehouseBranchId(this.getFkWarehouseBranchId());
        registry.setFkWarehouseWarehouseId(this.getFkWarehouseWarehouseId());
        registry.setFkBookkeepingYearId_n(this.getFkBookkeepingYearId_n());
        registry.setFkBookkeepingNumberId_n(this.getFkBookkeepingNumberId_n());
        registry.setFkSourceDpsId_n(this.getFkSourceDpsId_n());
        registry.setFkSourceIogId_n(this.getFkSourceIogId_n());
        registry.setFkUserAuditedId(this.getFkUserAuditedId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserAudited(this.getTsUserAudited());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        for (DDbIogNote child : mvChildNotes) {
            registry.getChildNotes().add(child.clone());
        }

        for (DDbIogRow child : mvChildRows) {
            registry.getChildRows().add(child.clone());
        }

        registry.setAuxComputeSiblingIog(this.isAuxComputeSiblingIog());
        registry.setAuxSiblingIogWarehouseKey(this.getAuxSiblingIogWarehouseKey());

        if (moSiblingIog != null) {
            registry.setSiblingIog(moSiblingIog.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public void setRegistryNew(boolean registryNew) {
        super.setRegistryNew(registryNew);

        for (DDbIogNote child : mvChildNotes) {
            child.setRegistryNew(registryNew);
        }

        for (DDbIogRow child : mvChildRows) {
            child.setRegistryNew(registryNew);
        }
    }

    @Override
    public void saveField(final Statement statement, final int[] pk, final int field, final Object value) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        msSql = "UPDATE " + getSqlTable() + " SET ";

        switch (field) {
            case FIELD_AUDITED:
                msSql += "b_aud = NOT b_aud, fk_usr_aud = " + (Integer) value + ", ts_usr_aud = NOW() ";
                break;
            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        statement.execute(msSql);
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public boolean canSave(final DGuiSession session) throws SQLException, Exception {
        int year = 0;
        boolean canSave = super.canSave(session);

        if (canSave) {
            // Validate stock:

            if (isStockRequired()) {
                year = DLibTimeUtils.digestYear(mtDate)[0];

                for (DDbIogRow row : mvChildRows) {
                    if (row.isInventoriable()) {
                        if (mbRegistryNew || row.isRegistryNew()) {
                            // Validate as active rows:

                            if (DTrnUtils.isIogForStockIn(this)) {
                                if (!DTrnDocRowUtils.validateRowIn(session, year, row)) {
                                    canSave = false;
                                    break;
                                }
                            }
                            else if (DTrnUtils.isIogForStockOut(this)) {
                                if (!DTrnDocRowUtils.validateRowOut(session, year, row, new Vector<DTrnDocRow>(mvChildRows))) {
                                    canSave = false;
                                    break;
                                }
                            }
                        }
                        else if (row.isDeleted() && row.isRegistryEdited()) {
                            // Validate as inactive rows:

                            if (DTrnUtils.isIogForStockIn(this)) {
                                if (!DTrnDocRowUtils.validateRowIn(session, year, row)) {
                                    canSave = false;
                                    break;
                                }
                            }
                            else if (DTrnUtils.isIogForStockOut(this)) {
                                if (!DTrnDocRowUtils.validateRowOut(session, year, row, new Vector<DTrnDocRow>(mvChildRows))) {
                                    canSave = false;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }

        return canSave;
    }

    @Override
    public boolean canDisable(final DGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean canDelete(final DGuiSession session) throws SQLException, Exception {
        boolean can = super.canDelete(session);

        if (can) {
            can = canChangeStatus(session, mbDeleted, false);

            if (can && moSiblingIog != null) {
                can = moSiblingIog.canChangeStatus(session, mbDeleted, true);

                if (!can) {
                    mnQueryResultId = moSiblingIog.getQueryResultId();
                    msQueryResult = moSiblingIog.getQueryResult();
                }
            }
        }

        return can;
    }

    @Override
    public void disable(final DGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete(final DGuiSession session) throws SQLException, Exception {
        mbDeleted = !mbDeleted;
        save(session);
        session.notifySuscriptors(mnRegistryType);
    }

    public void computeTotal() {
        int position = 0;

        mdValue_r = 0;

        for (DDbIogRow row : mvChildRows) {
            row.computeTotal();

            if (row.isDeleted()) {
                row.setSortingPos(0);
            }
            else {
                row.setSortingPos(++position);
                mdValue_r += row.getValue_r();
            }
        }
    }
}
