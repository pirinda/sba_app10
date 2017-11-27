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
import net.sf.jasperreports.engine.JRException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import sba.gui.prt.DPrtUtils;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiSession;
import sba.lib.xml.DXmlUtils;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.cfg.db.DDbConfigBranch;
import sba.mod.cfg.db.DDbUser;
import sba.mod.fin.db.DDbAbpBizPartner;
import sba.mod.fin.db.DDbAbpItem;
import sba.mod.fin.db.DDbBookkeepingMove;
import sba.mod.fin.db.DDbBookkeepingNumber;
import sba.mod.fin.db.DFinUtils;

/**
 *
 * @author Sergio Flores
 */
public class DDbDps extends DDbRegistryUser {

    public static final int FIELD_CLOSED_DPS = 1;
    public static final int FIELD_CLOSED_IOG = 2;
    public static final int FIELD_AUDITED = 3;

    protected int mnPkDpsId;
    protected String msSeries;
    protected int mnNumber;
    protected String msOrder;
    protected Date mtDate;
    protected Date mtDateCredit;
    protected Date mtDateDelivery_n;
    protected Date mtDateDocOriginal;
    protected Date mtDateDocSending;
    protected Date mtDateDocReception;
    protected int mnTerminal;
    protected int mnApproveYear;
    protected int mnApproveNumber;
    protected int mnCreditDays;
    protected String msPaymentAccount;
    protected String msImportDeclaration;
    protected Date mtImportDeclarationDate_n;
    protected boolean mbDiscountDocApplying;
    protected boolean mbDiscountDocPercentageApplying;
    protected double mdDiscountDocPercentage;
    protected double mdSubtotalProv_r;
    protected double mdDiscountDoc_r;
    protected double mdSubtotal_r;
    protected double mdTaxCharged_r;
    protected double mdTaxRetained_r;
    protected double mdTotal_r;
    protected double mdExchangeRate;
    protected double mdSubtotalProvCy_r;
    protected double mdDiscountDocCy_r;
    protected double mdSubtotalCy_r;
    protected double mdTaxChargedCy_r;
    protected double mdTaxRetainedCy_r;
    protected double mdTotalCy_r;
    protected boolean mbDocCopy;
    protected boolean mbClosedDps;
    protected boolean mbClosedIog;
    protected boolean mbAudited;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkDpsCategoryId;
    protected int mnFkDpsClassId;
    protected int mnFkDpsTypeId;
    protected int mnFkDpsStatusId;
    protected int mnFkCurrencyId;
    protected int mnFkPaymentTypeId;
    protected int mnFkModeOfPaymentTypeId;
    protected int mnFkEmissionTypeId;
    protected int mnFkOwnerBizPartnerId;
    protected int mnFkOwnerBranchId;
    protected int mnFkWarehouseBizPartnerId_n;
    protected int mnFkWarehouseBranchId_n;
    protected int mnFkWarehouseWarehouseId_n;
    protected int mnFkBizPartnerBizPartnerId;
    protected int mnFkBizPartnerBranchId;
    protected int mnFkBizPartnerAddressId;
    protected int mnFkAgentId_n;
    protected int mnFkBookkeepingYearId_n;
    protected int mnFkBookkeepingNumberId_n;
    protected int mnFkSourceDpsId_n;
    protected int mnFkUserClosedDpsId;
    protected int mnFkUserClosedIogId;
    protected int mnFkUserAuditedId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUserClosedDps;
    protected Date mtTsUserClosedIog;
    protected Date mtTsUserAudited;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected DDbDpsEds moChildEds;
    protected Vector<DDbDpsNote> mvChildNotes;
    protected Vector<DDbDpsRow> mvChildRows;

    protected String msEdsUuid;
    protected String msEdsMethodOfPayment;
    protected String msEdsPaymentConditions;
    protected String msEdsConfirmation;
    protected String msEdsTaxRegime;
    protected String msEdsUsage;

    protected int mnXtaIogId;

    protected int mnAuxNewDpsSeriesId;
    protected int mnAuxXmlTypeId;
    protected boolean mbAuxEdsRequired;
    protected double mdAuxTotalQuantity;

    public DDbDps() {
        super(DModConsts.T_DPS);
        mvChildNotes = new Vector<DDbDpsNote>();
        mvChildRows = new Vector<DDbDpsRow>();
        initRegistry();
    }

    /*
     * Private methods
     */

    private boolean isBookkeepingRequired() {
        return !mbDeleted && mnFkDpsStatusId == DModSysConsts.TS_DPS_ST_ISS &&
                (DTrnUtils.isDpsDocument(this) || DTrnUtils.isDpsAdjustment(this));
    }

    private boolean isStockRequired() {
        return !mbDeleted && mnFkDpsStatusId == DModSysConsts.TS_DPS_ST_ISS &&
                (DTrnUtils.isDpsDocument(this) || DTrnUtils.isDpsAdjustment(this)) &&
                getBranchWarehouseKey_n() != null;
    }

    private int[] getSysMoveTypeKey(final int[] adjustmentClassKey) {
        int[] typeKey = null;

        if (isDpsForPurchase()) {
            if (isDpsDocument()) {
                typeKey = DModSysConsts.FS_SYS_MOV_TP_PUR;
            }
            else if (isDpsAdjustmentInc()) {
                typeKey = DLibUtils.compareKeys(adjustmentClassKey, DModSysConsts.TS_ADJ_CL_INC_INC) ? DModSysConsts.FS_SYS_MOV_TP_PUR_INC_INC : DModSysConsts.FS_SYS_MOV_TP_PUR_INC_ADD;
            }
            else if (isDpsAdjustmentDec()) {
                typeKey = DLibUtils.compareKeys(adjustmentClassKey, DModSysConsts.TS_ADJ_CL_DEC_DIS) ? DModSysConsts.FS_SYS_MOV_TP_PUR_DEC_DIS : DModSysConsts.FS_SYS_MOV_TP_PUR_DEC_RET;
            }
        }
        else {
            if (isDpsDocument()) {
                typeKey = DModSysConsts.FS_SYS_MOV_TP_SAL;
            }
            else if (isDpsAdjustmentInc()) {
                typeKey = DLibUtils.compareKeys(adjustmentClassKey, DModSysConsts.TS_ADJ_CL_INC_INC) ? DModSysConsts.FS_SYS_MOV_TP_SAL_INC_INC : DModSysConsts.FS_SYS_MOV_TP_SAL_INC_ADD;
            }
            else if (isDpsAdjustmentDec()) {
                typeKey = DLibUtils.compareKeys(adjustmentClassKey, DModSysConsts.TS_ADJ_CL_DEC_DIS) ? DModSysConsts.FS_SYS_MOV_TP_SAL_DEC_DIS : DModSysConsts.FS_SYS_MOV_TP_SAL_DEC_RET;
            }
        }

        return typeKey;
    }

    private int getAccountIdForItem(final DDbAbpItem abpItem, final int[] adjustmentClassKey) {
        int accountId = DLibConsts.UNDEFINED;

        if (isDpsForPurchase()) {
            if (isDpsDocument()) {
                accountId = abpItem.getFkAccountPurchaseId();
            }
            else if (isDpsAdjustmentInc()) {
                accountId = DLibUtils.compareKeys(adjustmentClassKey, DModSysConsts.TS_ADJ_CL_INC_INC) ? abpItem.getFkAccountPurchaseIncIncrementId() : abpItem.getFkAccountPurchaseIncAditionId();
            }
            else if (isDpsAdjustmentDec()) {
                accountId = DLibUtils.compareKeys(adjustmentClassKey, DModSysConsts.TS_ADJ_CL_DEC_DIS) ? abpItem.getFkAccountPurchaseDecDiscountId() : abpItem.getFkAccountPurchaseDecReturnId();
            }
        }
        else {
            if (isDpsDocument()) {
                accountId = abpItem.getFkAccountSaleId();
            }
            else if (isDpsAdjustmentInc()) {
                accountId = DLibUtils.compareKeys(adjustmentClassKey, DModSysConsts.TS_ADJ_CL_INC_INC) ? abpItem.getFkAccountSaleIncIncrementId() : abpItem.getFkAccountSaleIncAdditionId();
            }
            else if (isDpsAdjustmentDec()) {
                accountId = DLibUtils.compareKeys(adjustmentClassKey, DModSysConsts.TS_ADJ_CL_DEC_DIS) ? abpItem.getFkAccountSaleDecDiscountId() : abpItem.getFkAccountSaleDecReturnId();
            }
        }

        return accountId;
    }

    /**
     * This method must be called only for new registries on method save().
     */
    private void generateDpsNumberAutomatic(final DGuiSession session) throws Exception {
        Vector<DDbDpsSeriesNumber> dpsSeriesNumbers = null;

        if (!((DDbConfigBranch) session.getConfigBranch()).isDpsNumberAutomaticByUser()) {
            /*
             * ALG#001. Define and validate new document automatic number:
             */

            dpsSeriesNumbers = ((DDbUser) session.getUser()).getAuxBranchDpsSeriesNumbers(mnAuxNewDpsSeriesId);

            if (dpsSeriesNumbers.isEmpty()) {
                throw new Exception(DUtilConsts.ERR_MSG_DPS_SER_NUM_NON_AVA);
            }
            else if (dpsSeriesNumbers.size() > 1) {
                throw new Exception(DUtilConsts.ERR_MSG_DPS_SER_NUM_MUL_AVA);
            }
            else {
                mnNumber = DTrnUtils.getNextNumberForDps(session, getDpsTypeKey(), msSeries);

                if (mnNumber < dpsSeriesNumbers.get(0).getNumberStart()) {
                    mnNumber = dpsSeriesNumbers.get(0).getNumberStart();
                }
                else if (dpsSeriesNumbers.get(0).getNumberEnd_n() != 0 && mnNumber > dpsSeriesNumbers.get(0).getNumberEnd_n()) {
                    throw new Exception(DUtilConsts.ERR_MSG_DPS_SER_NUM_MAX);
                }
            }

            /*
             * End of algorithm.
             */
        }
    }

    private DDbBookkeepingMove createBookkeepingMove(final String reference, final String text, final int accountId, final int sysAccountTypeId, final int[] sysMoveTypeKey) {
        DDbBookkeepingMove bkkMove = new DDbBookkeepingMove();

        bkkMove.setPkYearId(DLibTimeUtils.digestYear(mtDate)[0]);
        bkkMove.setPkMoveId(0);
        bkkMove.setDate(mtDate);
        bkkMove.setSupporting("");
        bkkMove.setReference(reference);
        bkkMove.setText(text);
        bkkMove.setDebit(0);
        bkkMove.setCredit(0);
        bkkMove.setExchangeRate(mdExchangeRate);
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
        bkkMove.setFkCurrencyId(mnFkCurrencyId);
        bkkMove.setFkPaymentTypeId(mnFkPaymentTypeId);
        bkkMove.setFkModeOfPaymentTypeId(mnFkModeOfPaymentTypeId);
        bkkMove.setFkValueTypeId(DModSysConsts.FS_VAL_TP_NA);
        bkkMove.setFkOwnerBizPartnerId(mnFkOwnerBizPartnerId);
        bkkMove.setFkOwnerBranchId(mnFkOwnerBranchId);
        bkkMove.setFkCashBizPartnerId_n(DLibConsts.UNDEFINED);
        bkkMove.setFkCashBranchId_n(DLibConsts.UNDEFINED);
        bkkMove.setFkCashCashId_n(DLibConsts.UNDEFINED);
        bkkMove.setFkWarehouseBizPartnerId_n(mnFkWarehouseBizPartnerId_n);
        bkkMove.setFkWarehouseBranchId_n(mnFkWarehouseBranchId_n);
        bkkMove.setFkWarehouseWarehouseId_n(mnFkWarehouseWarehouseId_n);
        bkkMove.setFkBizPartnerBizPartnerId_n(mnFkBizPartnerBizPartnerId);
        bkkMove.setFkBizPartnerBranchId_n(mnFkBizPartnerBranchId);
        bkkMove.setFkDpsInvId_n(isDpsDocument() ? mnPkDpsId : DLibConsts.UNDEFINED);
        bkkMove.setFkDpsAdjId_n(isDpsAdjustment() ? mnPkDpsId : DLibConsts.UNDEFINED);
        bkkMove.setFkIogId_n(DLibConsts.UNDEFINED);
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
        int bizPartnerClass = DLibConsts.UNDEFINED;
        String sql = "";
        String text = "";
        String reference = "";
        ResultSet resultSet = null;
        DDbAbpBizPartner abpBizPartner = null;
        DDbAbpItem abpItem = null;
        DDbBookkeepingMove bkkMove = null;
        DDbBookkeepingNumber bkkNumber = null;
        Vector<DDbBookkeepingMove> bkkMoves = new Vector<DDbBookkeepingMove>();

        // Delete previous bookkeeping moves, if any:

        if (mnFkBookkeepingYearId_n != DLibConsts.UNDEFINED && mnFkBookkeepingNumberId_n != DLibConsts.UNDEFINED) {
            bkkNumber = new DDbBookkeepingNumber();
            bkkNumber.read(session, new int[] { mnFkBookkeepingYearId_n, mnFkBookkeepingNumberId_n });
            bkkNumber.setDeleted(true);
            bkkNumber.save(session);
        }

        if (isBookkeepingRequired()) {
            // Save bookkeeping moves:

            bkkNumber = new DDbBookkeepingNumber();
            bkkNumber.setPkYearId(DLibTimeUtils.digestYear(mtDate)[0]);
            bkkNumber.save(session);

            mnFkBookkeepingYearId_n = bkkNumber.getPkYearId();
            mnFkBookkeepingNumberId_n = bkkNumber.getPkNumberId();

            msSql = "UPDATE " + getSqlTable() + " SET fk_bkk_yer_n = " + mnFkBookkeepingYearId_n + ", fk_bkk_num_n = " + mnFkBookkeepingNumberId_n + " " + getSqlWhere();
            session.getStatement().execute(msSql);

            // Businnes partner account:

            text = getBookkeepingText(session);
            bizPartnerClass = DTrnUtils.getBizPartnerClassByDpsCategory(mnFkDpsCategoryId);
            abpBizPartner = DFinUtils.readAbpBizPartner(session, new int[] { mnFkBizPartnerBizPartnerId }, bizPartnerClass);

            if (isDpsDocument()) {
                // Invoice:

                reference = getDpsReference(session);

                bkkMove = createBookkeepingMove(reference, text, abpBizPartner.getFkAccountBizPartnerId(),
                        DFinUtils.getSysAccountTypeForBizPartnerClass(bizPartnerClass),
                        getSysMoveTypeKey(DModSysConsts.TS_ADJ_CL_NA_NA));

                if (isDpsForPurchase()) {
                    bkkMove.setDebit(0);
                    bkkMove.setCredit(mdTotal_r);
                    bkkMove.setDebitCy(0);
                    bkkMove.setCreditCy(mdTotalCy_r);
                }
                else {
                    bkkMove.setDebit(mdTotal_r);
                    bkkMove.setCredit(0);
                    bkkMove.setDebitCy(mdTotalCy_r);
                    bkkMove.setCreditCy(0);
                }

                bkkMoves.add(bkkMove);

                // Update DPS reference on all bookkeeping moves, due it may be different than original:

                msSql = "UPDATE " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " SET ref = '" + reference + "' " +
                        "WHERE fk_dps_inv_n = " + mnPkDpsId + " AND " +
                        "fk_sys_acc_tp = " + DFinUtils.getSysAccountTypeForBizPartnerClass(bizPartnerClass) + " ";
                session.getStatement().execute(msSql);
            }
            else {
                // Debit or credit note:

                for (DDbDpsRow row : mvChildRows) {
                    if (!row.isDeleted()) {

                        if (row.getFkSourceDpsId_n() == DLibConsts.UNDEFINED) {
                            reference = "";
                        }
                        else {
                            sql = "SELECT ser, num FROM " + getSqlTable() + " " +
                                    "WHERE id_dps = " + row.getFkSourceDpsId_n() + " ";
                            resultSet = session.getStatement().executeQuery(sql);
                            if (!resultSet.next()) {
                                throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
                            }
                            else {
                                reference = DTrnUtils.composeDpsReference(resultSet.getString("ser"), resultSet.getInt("num"));
                            }
                        }

                        bkkMove = createBookkeepingMove(reference, text, abpBizPartner.getFkAccountBizPartnerId(),
                                DFinUtils.getSysAccountTypeForBizPartnerClass(bizPartnerClass),
                                getSysMoveTypeKey(row.getAdjustmentClassKey()));
                        bkkMove.setFkDpsInvId_n(row.getFkSourceDpsId_n());  // the keystone for DPS correct balance, when specified

                        if (isDpsForPurchase() && isDpsAdjustmentInc() || isDpsForSale() && isDpsAdjustmentDec()) {
                            bkkMove.setDebit(0);
                            bkkMove.setCredit(row.getTotal_r());
                            bkkMove.setDebitCy(0);
                            bkkMove.setCreditCy(row.getTotalCy_r());
                        }
                        else {
                            bkkMove.setDebit(row.getTotal_r());
                            bkkMove.setCredit(0);
                            bkkMove.setDebitCy(row.getTotalCy_r());
                            bkkMove.setCreditCy(0);
                        }

                        bkkMoves.add(bkkMove);
                    }
                }
            }

            // Results accouts:

            for (DDbDpsRow row : mvChildRows) {
                if (!row.isDeleted()) {
                    abpItem = DFinUtils.readAbpItem(session, new int[] { row.getFkRowItemId() });
                    bkkMove = createBookkeepingMove("", text, getAccountIdForItem(abpItem, row.getAdjustmentClassKey()), DModSysConsts.FS_SYS_ACC_TP_NA, getSysMoveTypeKey(row.getAdjustmentClassKey()));

                    if (isDpsDocument()) {
                        // Invoice:

                        if (isDpsForPurchase()) {
                            bkkMove.setDebit(row.mdTotal_r);
                            bkkMove.setCredit(0);
                            bkkMove.setDebitCy(row.mdTotalCy_r);
                            bkkMove.setCreditCy(0);
                        }
                        else {
                            bkkMove.setDebit(0);
                            bkkMove.setCredit(row.getTotal_r());
                            bkkMove.setDebitCy(0);
                            bkkMove.setCreditCy(row.mdTotalCy_r);
                        }
                    }
                    else {
                        // Debit or credit note:

                        bkkMove.setFkDpsInvId_n(row.getFkSourceDpsId_n());

                        if (isDpsForPurchase() && isDpsAdjustmentInc() || isDpsForSale() && isDpsAdjustmentDec()) {
                            bkkMove.setDebit(row.mdTotal_r);
                            bkkMove.setCredit(0);
                            bkkMove.setDebitCy(row.mdTotalCy_r);
                            bkkMove.setCreditCy(0);
                        }
                        else {
                            bkkMove.setDebit(0);
                            bkkMove.setCredit(row.getTotal_r());
                            bkkMove.setDebitCy(0);
                            bkkMove.setCreditCy(row.mdTotalCy_r);
                        }
                    }

                    bkkMoves.add(bkkMove);
                }
            }

            for (DDbBookkeepingMove move : bkkMoves) {
                move.save(session);
            }
        }
    }

    private void computeStock(final DGuiSession session) throws SQLException, Exception {
        String sql = "";
        Statement statement = null;
        ResultSet resultSet = null;
        DDbIog iog = null;

        // Delete previous stock moves, if any:

        statement = session.getStatement().getConnection().createStatement();

        sql = "SELECT id_iog FROM " + DModConsts.TablesMap.get(DModConsts.T_IOG) + " " +
                "WHERE b_del = 0 AND fk_src_dps_n = " + mnPkDpsId + " ";
        resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            iog = (DDbIog) session.readRegistry(DModConsts.T_IOG, new int[] { resultSet.getInt(1) }, DDbConsts.MODE_STEALTH);
            if (iog.getQueryResultId() == DDbConsts.READ_OK) {
                iog.setDeleted(true);
                iog.save(session);
            }
        }

        if (isStockRequired()) {
            // Save stock moves:

            iog = DTrnUtils.createIogForSupply(session, this);
            iog.save(session);
        }
    }

    private void computeEds(final DGuiSession session) throws Exception {
        boolean save = false;

        /*
         * Auxiliar members "mbAuxEdsRequired" and "mnAuxXmlTypeId" are only set in DFormDps.
         */

        if ((mbAuxEdsRequired && mnAuxXmlTypeId != DModSysConsts.TS_XML_TP_NA) || (!mbDeleted && mnFkDpsStatusId == DModSysConsts.TS_DPS_ST_ISS && moChildEds != null)) {
            moChildEds = DTrnEdsUtils.createDpsEds(session, this, mbAuxEdsRequired ? mnAuxXmlTypeId : moChildEds.getFkXmlTypeId(), "", null);
            save = true;
        }
        else if (!mbAuxEdsRequired && mnAuxXmlTypeId == DModSysConsts.TS_XML_TP_NA) {
            moChildEds = null;  // clear e-document supporting only if it is requested from DFormDps
            save = true;
        }

        if (save) {
            saveEds(session, true);
        }
    }

    private void saveEds(final DGuiSession session, boolean issueEds) throws SQLException, Exception {
        msSql = "DELETE FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS_EDS) + " " + getSqlWhere();
        session.getStatement().execute(msSql);

        if (moChildEds != null) {
            moChildEds.setPkDpsId(mnPkDpsId);
            moChildEds.setRegistryNew(true);
            moChildEds.save(session);

            if (issueEds) {
                issueEds(session);
            }
        }
    }

    private void readEds() throws Exception {
        if (moChildEds != null) {
            Document doc = DXmlUtils.parseDocument(moChildEds.getDocXml());
            Node node = null;
            NamedNodeMap namedNodeMap = null;
            
            switch (moChildEds.getFkXmlTypeId()) {
                case DModSysConsts.TS_XML_TP_CFD:
                case DModSysConsts.TS_XML_TP_CFDI_32:
                    break;
                    
                case DModSysConsts.TS_XML_TP_CFDI_33:
                    // comprobante:
                    node = DXmlUtils.extractElements(doc, "cfdi:Comprobante").item(0);
                    namedNodeMap = node.getAttributes();
                    msEdsMethodOfPayment = DXmlUtils.extractAttributeValue(namedNodeMap, "MetodoPago", true);
                    msEdsPaymentConditions = DXmlUtils.extractAttributeValue(namedNodeMap, "CondicionesDePago", false);
                    msEdsConfirmation = DXmlUtils.extractAttributeValue(namedNodeMap, "Confirmacion", false);

                    // emisor:
                    node = DXmlUtils.extractElements(doc, "cfdi:Emisor").item(0);
                    namedNodeMap = node.getAttributes();
                    msEdsTaxRegime = DXmlUtils.extractAttributeValue(namedNodeMap, "RegimenFiscal", true);

                    // receptor:
                    node = DXmlUtils.extractElements(doc, "cfdi:Receptor").item(0);
                    namedNodeMap = node.getAttributes();
                    msEdsUsage = DXmlUtils.extractAttributeValue(namedNodeMap, "UsoCFDI", true);
                    
                    // conceptos:
                    int row = 0;
                    node = DXmlUtils.extractElements(doc, "cfdi:Conceptos").item(0);
                    Vector<Node> conceptos = DXmlUtils.extractChildElements(node, "cfdi:Concepto");
                    for (Node concepto : conceptos) {
                        DDbDpsRow dpsRow = null;
                        
                        do {
                            dpsRow = mvChildRows.get(row++);
                        } while (dpsRow.isDeleted());
                        
                        namedNodeMap = concepto.getAttributes();
                        dpsRow.setEdsItemKey(DXmlUtils.extractAttributeValue(namedNodeMap, "ClaveProdServ", true));
                        dpsRow.setEdsUnitKey(DXmlUtils.extractAttributeValue(namedNodeMap, "ClaveUnidad", true));
                        
                        if (DXmlUtils.hasChildElement(concepto, "cfdi:CuentaPredial")) {
                            Vector<Node> cuentaPredial = DXmlUtils.extractChildElements(node, "cfdi:CuentaPredial");
                            namedNodeMap = cuentaPredial.get(0).getAttributes();
                            dpsRow.setEdsPredial(DXmlUtils.extractAttributeValue(namedNodeMap, "Numero", true));
                        }
                    }
                    
                    // complemento:
                    if (DXmlUtils.hasChildElement(doc, "cfdi:Complemento")) {
                        node = DXmlUtils.extractElements(doc, "cfdi:Receptor").item(0);
                        if (DXmlUtils.hasChildElement(node, "tfd:TimbreFiscalDigital")) {
                            Vector<Node> timbreFiscalDigital = DXmlUtils.extractChildElements(node, "tfd:TimbreFiscalDigital");
                            namedNodeMap = timbreFiscalDigital.get(0).getAttributes();
                            msEdsUuid = DXmlUtils.extractAttributeValue(namedNodeMap, "UUID", true);
                        }
                    }
                    break;
                    
                default:
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
    }

    private int countBookkeepingMoves(final DGuiSession session) throws SQLException, Exception {
        int count = 0;
        String sql = "";
        ResultSet resultSet = null;

        sql = "SELECT COUNT(*) FROM " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " " +
                "WHERE b_del = 0 AND fk_sys_mov_cl <> " + DModSysConsts.FS_SYS_MOV_CL_YO + " "; // exclude opening year moves

        if (isDpsDocument()) {
            sql += "AND fk_dps_inv_n = " + mnPkDpsId + " ";
            if (isDpsForPurchase()) {
                sql += "AND NOT (fk_sys_mov_cl = " + DModSysConsts.FS_SYS_MOV_TP_PUR[0] + " AND fk_sys_mov_TP = " + DModSysConsts.FS_SYS_MOV_TP_PUR[1] + ") ";
            }
            else {
                sql += "AND NOT (fk_sys_mov_cl = " + DModSysConsts.FS_SYS_MOV_TP_SAL[0] + " AND fk_sys_mov_TP = " + DModSysConsts.FS_SYS_MOV_TP_SAL[1] + ") ";
            }
        }
        else if (isDpsAdjustment()) {
            sql += "AND fk_dps_adj_n = " + mnPkDpsId + " ";
            if (isDpsForPurchase()) {
                sql += "AND fk_sys_mov_cl <> " + DModSysConsts.FS_SYS_MOV_CL_PUR + " ";
            }
            else {
                sql += "AND fk_sys_mov_cl <> " + DModSysConsts.FS_SYS_MOV_CL_SAL + " ";
            }
        }

        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            count = resultSet.getInt(1);
        }

        return count;
    }

    private boolean canChangeStatus(final DGuiSession session, final boolean activate) throws SQLException, Exception {
        int year = 0;
        boolean canChange = true;

        if (mbSystem) {
            canChange = false;
            msQueryResult = DDbConsts.MSG_REG_ + getDpsNumber() + DDbConsts.MSG_REG_IS_SYSTEM;
        }
        else {
            if (!activate) {
                if (countBookkeepingMoves(session) > 0) {
                    canChange = false;
                    msQueryResult = DTrnConsts.ERR_MSG_BKK_MOVES;
                }
            }

            if (canChange && getBranchWarehouseKey_n() != null) {
                year = DLibTimeUtils.digestYear(mtDate)[0];

                for (DDbDpsRow row : mvChildRows) {
                    if (!row.isDeleted() && row.isInventoriable()) {
                        if (activate) {
                            // Activate an inactive document:

                            if (DTrnUtils.isDpsForStockIn(this, true)) {
                                if (!DTrnDocRowUtils.validateRowIn(session, year, row)) {
                                    canChange = false;
                                    break;
                                }
                            }
                            else if (DTrnUtils.isDpsForStockOut(this, true)) {
                                if (!DTrnDocRowUtils.validateRowOut(session, year, row, new Vector<DTrnDocRow>(mvChildRows))) {
                                    canChange = false;
                                    break;
                                }
                            }
                        }
                        else {
                            // Inactivate an active document:

                            if (DTrnUtils.isDpsForStockIn(this, false)) {
                                if (!DTrnDocRowUtils.validateRowIn(session, year, row)) {
                                    canChange = false;
                                    break;
                                }
                            }
                            else if (DTrnUtils.isDpsForStockOut(this, false)) {
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

    private String composeDpsReference(DGuiSession session) {
        String sql = "";
        String reference = "";
        ResultSet resultSet = null;

        try {
            sql = "SELECT ref " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " " +
                    "WHERE b_del = 0 AND " +
                    "fk_dps_inv_n = " + mnPkDpsId + " AND " +
                    "fk_bpr_bpr_n = " + mnFkBizPartnerBizPartnerId + " AND " +
                    "fk_bpr_bra_n = " + mnFkBizPartnerBranchId + " ";
            resultSet = session.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                reference = resultSet.getString(1);
            }
        }
        catch (Exception e) {
            DLibUtils.printException(this, e);
        }

        return reference;
    }

    /*
     * Public methods
     */

    public boolean isDpsForPurchase() {
        return DTrnUtils.isDpsForPurchase(this);
    }

    public boolean isDpsForSale() {
        return DTrnUtils.isDpsForSale(this);
    }

    public boolean isDpsOrder() {
        return DTrnUtils.isDpsOrder(this);
    }

    public boolean isDpsDocument() {
        return DTrnUtils.isDpsDocument(this);
    }

    public boolean isDpsAdjustment() {
        return DTrnUtils.isDpsAdjustment(this);
    }

    public boolean isDpsAdjustmentInc() {
        return DTrnUtils.isDpsAdjustmentInc(this);
    }

    public boolean isDpsAdjustmentDec() {
        return DTrnUtils.isDpsAdjustmentDec(this);
    }

    public String getBookkeepingText(final DGuiSession session) {
        String text = "";

        if (isDpsDocument()) {
            text = DTrnConsts.TXT_DPS_DOC + " ";
        }
        else if (isDpsAdjustmentInc()) {
            text = DTrnConsts.TXT_DPS_ADJ_INC + " ";
        }
        else if (isDpsAdjustmentDec()) {
            text = DTrnConsts.TXT_DPS_ADJ_DEC + " ";
        }

        text += getDpsNumber() + "; " + session.readField(DModConsts.BU_BPR, new int[] { mnFkBizPartnerBizPartnerId }, DDbRegistry.FIELD_NAME);

        if (text.length() > DDbBookkeepingMove.LEN_TEXT) {
            text = text.substring(0, DDbBookkeepingMove.LEN_TEXT);
        }

        return text;
    }

    public void setPkDpsId(int n) { mnPkDpsId = n; }
    public void setSeries(String s) { msSeries = s; }
    public void setNumber(int n) { mnNumber = n; }
    public void setOrder(String s) { msOrder = s; }
    public void setDate(Date t) { mtDate = t; }
    public void setDateCredit(Date t) { mtDateCredit = t; }
    public void setDateDelivery_n(Date t) { mtDateDelivery_n = t; }
    public void setDateDocOriginal(Date t) { mtDateDocOriginal = t; }
    public void setDateDocSending(Date t) { mtDateDocSending = t; }
    public void setDateDocReception(Date t) { mtDateDocReception = t; }
    public void setTerminal(int n) { mnTerminal = n; }
    public void setApproveYear(int n) { mnApproveYear = n; }
    public void setApproveNumber(int n) { mnApproveNumber = n; }
    public void setCreditDays(int n) { mnCreditDays = n; }
    public void setPaymentAccount(String s) { msPaymentAccount = s; }
    public void setImportDeclaration(String s) { msImportDeclaration = s; }
    public void setImportDeclarationDate_n(Date t) { mtImportDeclarationDate_n = t; }
    public void setDiscountDocApplying(boolean b) { mbDiscountDocApplying = b; }
    public void setDiscountDocPercentageApplying(boolean b) { mbDiscountDocPercentageApplying = b; }
    public void setDiscountDocPercentage(double d) { mdDiscountDocPercentage = d; }
    public void setSubtotalProv_r(double d) { mdSubtotalProv_r = d; }
    public void setDiscountDoc_r(double d) { mdDiscountDoc_r = d; }
    public void setSubtotal_r(double d) { mdSubtotal_r = d; }
    public void setTaxCharged_r(double d) { mdTaxCharged_r = d; }
    public void setTaxRetained_r(double d) { mdTaxRetained_r = d; }
    public void setTotal_r(double d) { mdTotal_r = d; }
    public void setExchangeRate(double d) { mdExchangeRate = d; }
    public void setSubtotalProvCy_r(double d) { mdSubtotalProvCy_r = d; }
    public void setDiscountDocCy_r(double d) { mdDiscountDocCy_r = d; }
    public void setSubtotalCy_r(double d) { mdSubtotalCy_r = d; }
    public void setTaxChargedCy_r(double d) { mdTaxChargedCy_r = d; }
    public void setTaxRetainedCy_r(double d) { mdTaxRetainedCy_r = d; }
    public void setTotalCy_r(double d) { mdTotalCy_r = d; }
    public void setDocCopy(boolean b) { mbDocCopy = b; }
    public void setClosedDps(boolean b) { mbClosedDps = b; }
    public void setClosedIog(boolean b) { mbClosedIog = b; }
    public void setAudited(boolean b) { mbAudited = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkDpsCategoryId(int n) { mnFkDpsCategoryId = n; }
    public void setFkDpsClassId(int n) { mnFkDpsClassId = n; }
    public void setFkDpsTypeId(int n) { mnFkDpsTypeId = n; }
    public void setFkDpsStatusId(int n) { mnFkDpsStatusId = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setFkPaymentTypeId(int n) { mnFkPaymentTypeId = n; }
    public void setFkModeOfPaymentTypeId(int n) { mnFkModeOfPaymentTypeId = n; }
    public void setFkEmissionTypeId(int n) { mnFkEmissionTypeId = n; }
    public void setFkOwnerBizPartnerId(int n) { mnFkOwnerBizPartnerId = n; }
    public void setFkOwnerBranchId(int n) { mnFkOwnerBranchId = n; }
    public void setFkWarehouseBizPartnerId_n(int n) { mnFkWarehouseBizPartnerId_n = n; }
    public void setFkWarehouseBranchId_n(int n) { mnFkWarehouseBranchId_n = n; }
    public void setFkWarehouseWarehouseId_n(int n) { mnFkWarehouseWarehouseId_n = n; }
    public void setFkBizPartnerBizPartnerId(int n) { mnFkBizPartnerBizPartnerId = n; }
    public void setFkBizPartnerBranchId(int n) { mnFkBizPartnerBranchId = n; }
    public void setFkBizPartnerAddressId(int n) { mnFkBizPartnerAddressId = n; }
    public void setFkAgentId_n(int n) { mnFkAgentId_n = n; }
    public void setFkBookkeepingYearId_n(int n) { mnFkBookkeepingYearId_n = n; }
    public void setFkBookkeepingNumberId_n(int n) { mnFkBookkeepingNumberId_n = n; }
    public void setFkSourceDpsId_n(int n) { mnFkSourceDpsId_n = n; }
    public void setFkUserClosedDpsId(int n) { mnFkUserClosedDpsId = n; }
    public void setFkUserClosedIogId(int n) { mnFkUserClosedIogId = n; }
    public void setFkUserAuditedId(int n) { mnFkUserAuditedId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserClosedDps(Date t) { mtTsUserClosedDps = t; }
    public void setTsUserClosedIog(Date t) { mtTsUserClosedIog = t; }
    public void setTsUserAudited(Date t) { mtTsUserAudited = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkDpsId() { return mnPkDpsId; }
    public String getSeries() { return msSeries; }
    public int getNumber() { return mnNumber; }
    public String getOrder() { return msOrder; }
    public Date getDate() { return mtDate; }
    public Date getDateCredit() { return mtDateCredit; }
    public Date getDateDelivery_n() { return mtDateDelivery_n; }
    public Date getDateDocOriginal() { return mtDateDocOriginal; }
    public Date getDateDocSending() { return mtDateDocSending; }
    public Date getDateDocReception() { return mtDateDocReception; }
    public int getTerminal() { return mnTerminal; }
    public int getApproveYear() { return mnApproveYear; }
    public int getApproveNumber() { return mnApproveNumber; }
    public int getCreditDays() { return mnCreditDays; }
    public String getPaymentAccount() { return msPaymentAccount; }
    public String getImportDeclaration() { return msImportDeclaration; }
    public Date getImportDeclarationDate_n() { return mtImportDeclarationDate_n; }
    public boolean isDiscountDocApplying() { return mbDiscountDocApplying; }
    public boolean isDiscountDocPercentageApplying() { return mbDiscountDocPercentageApplying; }
    public double getDiscountDocPercentage() { return mdDiscountDocPercentage; }
    public double getSubtotalProv_r() { return mdSubtotalProv_r; }
    public double getDiscountDoc_r() { return mdDiscountDoc_r; }
    public double getSubtotal_r() { return mdSubtotal_r; }
    public double getTaxCharged_r() { return mdTaxCharged_r; }
    public double getTaxRetained_r() { return mdTaxRetained_r; }
    public double getTotal_r() { return mdTotal_r; }
    public double getExchangeRate() { return mdExchangeRate; }
    public double getSubtotalProvCy_r() { return mdSubtotalProvCy_r; }
    public double getDiscountDocCy_r() { return mdDiscountDocCy_r; }
    public double getSubtotalCy_r() { return mdSubtotalCy_r; }
    public double getTaxChargedCy_r() { return mdTaxChargedCy_r; }
    public double getTaxRetainedCy_r() { return mdTaxRetainedCy_r; }
    public double getTotalCy_r() { return mdTotalCy_r; }
    public boolean isDocCopy() { return mbDocCopy; }
    public boolean isClosedDps() { return mbClosedDps; }
    public boolean isClosedIog() { return mbClosedIog; }
    public boolean isAudited() { return mbAudited; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkDpsCategoryId() { return mnFkDpsCategoryId; }
    public int getFkDpsClassId() { return mnFkDpsClassId; }
    public int getFkDpsTypeId() { return mnFkDpsTypeId; }
    public int getFkDpsStatusId() { return mnFkDpsStatusId; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public int getFkPaymentTypeId() { return mnFkPaymentTypeId; }
    public int getFkModeOfPaymentTypeId() { return mnFkModeOfPaymentTypeId; }
    public int getFkEmissionTypeId() { return mnFkEmissionTypeId; }
    public int getFkOwnerBizPartnerId() { return mnFkOwnerBizPartnerId; }
    public int getFkOwnerBranchId() { return mnFkOwnerBranchId; }
    public int getFkWarehouseBizPartnerId_n() { return mnFkWarehouseBizPartnerId_n; }
    public int getFkWarehouseBranchId_n() { return mnFkWarehouseBranchId_n; }
    public int getFkWarehouseWarehouseId_n() { return mnFkWarehouseWarehouseId_n; }
    public int getFkBizPartnerBizPartnerId() { return mnFkBizPartnerBizPartnerId; }
    public int getFkBizPartnerBranchId() { return mnFkBizPartnerBranchId; }
    public int getFkBizPartnerAddressId() { return mnFkBizPartnerAddressId; }
    public int getFkAgentId_n() { return mnFkAgentId_n; }
    public int getFkBookkeepingYearId_n() { return mnFkBookkeepingYearId_n; }
    public int getFkBookkeepingNumberId_n() { return mnFkBookkeepingNumberId_n; }
    public int getFkSourceDpsId_n() { return mnFkSourceDpsId_n; }
    public int getFkUserClosedDpsId() { return mnFkUserClosedDpsId; }
    public int getFkUserClosedIogId() { return mnFkUserClosedIogId; }
    public int getFkUserAuditedId() { return mnFkUserAuditedId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserClosedDps() { return mtTsUserClosedDps; }
    public Date getTsUserClosedIog() { return mtTsUserClosedIog; }
    public Date getTsUserAudited() { return mtTsUserAudited; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setChildEds(DDbDpsEds o) { moChildEds = o; }

    public DDbDpsEds getChildEds() { return moChildEds; }
    public Vector<DDbDpsNote> getChildNotes() { return mvChildNotes; }
    public Vector<DDbDpsRow> getChildRows() { return mvChildRows; }

    public DDbDpsNote getChildNote(int[] key) {
        DDbDpsNote note = null;

        for (DDbDpsNote child : mvChildNotes) {
            if (DLibUtils.compareKeys(key, child.getPrimaryKey())) {
                note = child;
                break;
            }
        }

        return note;
    }

    public DDbDpsRow getChildRow(int[] key) {
        DDbDpsRow row = null;

        for (DDbDpsRow child : mvChildRows) {
            if (DLibUtils.compareKeys(key, child.getPrimaryKey())) {
                row = child;
                break;
            }
        }

        return row;
    }

    //public void setEdsUuid(String s) { msEdsUuid = s; } // read-only member!
    public void setEdsMethodOfPayment(String s) { msEdsMethodOfPayment = s; }
    public void setEdsPaymentConditions(String s) { msEdsPaymentConditions = s; }
    public void setEdsConfirmation(String s) { msEdsConfirmation = s; }
    public void setEdsTaxRegime(String s) { msEdsTaxRegime = s; }
    public void setEdsUsage(String s) { msEdsUsage = s; }
    
    public void setXtaIogId(int n) { mnXtaIogId = n; }

    public void setAuxNewDpsSeriesId(int n) { mnAuxNewDpsSeriesId = n; }
    public void setAuxXmlTypeId(int n) { mnAuxXmlTypeId = n; }
    public void setAuxEdsRequired(boolean b) { mbAuxEdsRequired = b; }
    public void setAuxTotalQuantity(double d) { mdAuxTotalQuantity = d; }

    public String getEdsUuid() { return msEdsUuid; }
    public String getEdsMethodOfPayment() { return msEdsMethodOfPayment; }
    public String getEdsPaymentConditions() { return msEdsPaymentConditions; }
    public String getEdsConfirmation() { return msEdsConfirmation; }
    public String getEdsTaxRegime() { return msEdsTaxRegime; }
    public String getEdsUsage() { return msEdsUsage; }
    
    public int getXtaIogId() { return mnXtaIogId; }

    public int getAuxNewDpsSeriesId() { return mnAuxNewDpsSeriesId; }
    public int getAuxXmlTypeId() { return mnAuxXmlTypeId; }
    public boolean isAuxEdsRequired() { return mbAuxEdsRequired; }
    public double getAuxTotalQuantity() { return mdAuxTotalQuantity; }

    public int getActiveRowsCount() {
        int count = 0;

        for (DDbDpsRow child : mvChildRows) {
            if (!child.isDeleted()) {
                count++;
            }
        }

        return count;
    }

    public int[] getDpsCategoryKey() { return new int[] { mnFkDpsCategoryId }; }
    public int[] getDpsClassKey() { return new int[] { mnFkDpsCategoryId, mnFkDpsClassId }; }
    public int[] getDpsTypeKey() { return new int[] { mnFkDpsCategoryId, mnFkDpsClassId, mnFkDpsTypeId }; }
    public int[] getDpsStatusKey() { return new int[] { mnFkDpsStatusId }; }
    public int[] getCurrencyKey() { return new int[] { mnFkCurrencyId }; }
    public int[] getPaymentTypeKey() { return new int[] { mnFkPaymentTypeId }; }
    public int[] getModeOfPaymentTypeKey() { return new int[] { mnFkModeOfPaymentTypeId }; }
    public int[] getCompanyKey() { return new int[] { mnFkOwnerBizPartnerId }; }
    public int[] getCompanyBranchKey() { return new int[] { mnFkOwnerBizPartnerId, mnFkOwnerBranchId }; }
    public int[] getBranchWarehouseKey_n() { return !(mnFkWarehouseBizPartnerId_n != DLibConsts.UNDEFINED && mnFkWarehouseBranchId_n != DLibConsts.UNDEFINED && mnFkWarehouseWarehouseId_n != DLibConsts.UNDEFINED) ? null : new int[] { mnFkWarehouseBizPartnerId_n, mnFkWarehouseBranchId_n, mnFkWarehouseWarehouseId_n }; }
    public int[] getBizPartnerKey() { return new int[] { mnFkBizPartnerBizPartnerId }; }
    public int[] getBizPartnerBranchKey() { return new int[] { mnFkBizPartnerBizPartnerId, mnFkBizPartnerBranchId }; }
    public int[] getBizPartnerBranchAddressKey() { return new int[] { mnFkBizPartnerBizPartnerId, mnFkBizPartnerBranchId, mnFkBizPartnerAddressId }; }
    public int[] getBookkeepingNumberKey_n() { return !(mnFkBookkeepingYearId_n != DLibConsts.UNDEFINED && mnFkBookkeepingNumberId_n != DLibConsts.UNDEFINED) ? null : new int[] { mnFkBookkeepingYearId_n, mnFkBookkeepingNumberId_n }; }
    public String getDpsNumber() { return DTrnUtils.composeDpsNumber(msSeries, mnNumber); }
    public String getDpsReference(DGuiSession session) { return !mbDeleted ? DTrnUtils.composeDpsReference(msSeries, mnNumber) : composeDpsReference(session); }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDpsId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDpsId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkDpsId = 0;
        msSeries = "";
        mnNumber = 0;
        msOrder = "";
        mtDate = null;
        mtDateCredit = null;
        mtDateDelivery_n = null;
        mtDateDocOriginal = null;
        mtDateDocSending = null;
        mtDateDocReception = null;
        mnTerminal = 0;
        mnApproveYear = 0;
        mnApproveNumber = 0;
        mnCreditDays = 0;
        msPaymentAccount = "";
        msImportDeclaration = "";
        mtImportDeclarationDate_n = null;
        mbDiscountDocApplying = false;
        mbDiscountDocPercentageApplying = false;
        mdDiscountDocPercentage = 0;
        mdSubtotalProv_r = 0;
        mdDiscountDoc_r = 0;
        mdSubtotal_r = 0;
        mdTaxCharged_r = 0;
        mdTaxRetained_r = 0;
        mdTotal_r = 0;
        mdExchangeRate = 0;
        mdSubtotalProvCy_r = 0;
        mdDiscountDocCy_r = 0;
        mdSubtotalCy_r = 0;
        mdTaxChargedCy_r = 0;
        mdTaxRetainedCy_r = 0;
        mdTotalCy_r = 0;
        mbDocCopy = false;
        mbClosedDps = false;
        mbClosedIog = false;
        mbAudited = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkDpsCategoryId = 0;
        mnFkDpsClassId = 0;
        mnFkDpsTypeId = 0;
        mnFkDpsStatusId = 0;
        mnFkCurrencyId = 0;
        mnFkPaymentTypeId = 0;
        mnFkModeOfPaymentTypeId = 0;
        mnFkEmissionTypeId = 0;
        mnFkOwnerBizPartnerId = 0;
        mnFkOwnerBranchId = 0;
        mnFkWarehouseBizPartnerId_n = 0;
        mnFkWarehouseBranchId_n = 0;
        mnFkWarehouseWarehouseId_n = 0;
        mnFkBizPartnerBizPartnerId = 0;
        mnFkBizPartnerBranchId = 0;
        mnFkBizPartnerAddressId = 0;
        mnFkAgentId_n = 0;
        mnFkBookkeepingYearId_n = 0;
        mnFkBookkeepingNumberId_n = 0;
        mnFkSourceDpsId_n = 0;
        mnFkUserClosedDpsId = 0;
        mnFkUserClosedIogId = 0;
        mnFkUserAuditedId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserClosedDps = null;
        mtTsUserClosedIog = null;
        mtTsUserAudited = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        moChildEds = null;
        mvChildNotes.clear();
        mvChildRows.clear();

        msEdsUuid = "";
        msEdsMethodOfPayment = "";
        msEdsPaymentConditions = "";
        msEdsConfirmation = "";
        msEdsTaxRegime = "";
        msEdsUsage = "";

        mnXtaIogId = 0;

        mnAuxNewDpsSeriesId = 0;
        mnAuxXmlTypeId = 0;
        mbAuxEdsRequired = false;
        mdAuxTotalQuantity = 0;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_dps = " + mnPkDpsId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_dps = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkDpsId = 0;

        msSql = "SELECT COALESCE(MAX(id_dps), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkDpsId = resultSet.getInt(1);
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
            mnPkDpsId = resultSet.getInt("id_dps");
            msSeries = resultSet.getString("ser");
            mnNumber = resultSet.getInt("num");
            msOrder = resultSet.getString("ord");
            mtDate = resultSet.getDate("dt");
            mtDateCredit = resultSet.getDate("dt_cdt");
            mtDateDelivery_n = resultSet.getDate("dt_dvy_n");
            mtDateDocOriginal = resultSet.getDate("dt_doc_ori");
            mtDateDocSending = resultSet.getDate("dt_doc_snd");
            mtDateDocReception = resultSet.getDate("dt_doc_rcp");
            mnTerminal = resultSet.getInt("ter");
            mnApproveYear = resultSet.getInt("apr_yer");
            mnApproveNumber = resultSet.getInt("apr_num");
            mnCreditDays = resultSet.getInt("cdt_day");
            msPaymentAccount = resultSet.getString("pay_acc");
            msImportDeclaration = resultSet.getString("imp_dec");
            mtImportDeclarationDate_n = resultSet.getDate("imp_dec_dt_n");
            mbDiscountDocApplying = resultSet.getBoolean("b_dsc_doc");
            mbDiscountDocPercentageApplying = resultSet.getBoolean("b_dsc_doc_per");
            mdDiscountDocPercentage = resultSet.getDouble("dsc_doc_per");
            mdSubtotalProv_r = resultSet.getDouble("sbt_prv_r");
            mdDiscountDoc_r = resultSet.getDouble("dsc_doc_r");
            mdSubtotal_r = resultSet.getDouble("sbt_r");
            mdTaxCharged_r = resultSet.getDouble("tax_cha_r");
            mdTaxRetained_r = resultSet.getDouble("tax_ret_r");
            mdTotal_r = resultSet.getDouble("tot_r");
            mdExchangeRate = resultSet.getDouble("exr");
            mdSubtotalProvCy_r = resultSet.getDouble("sbt_prv_cy_r");
            mdDiscountDocCy_r = resultSet.getDouble("dsc_doc_cy_r");
            mdSubtotalCy_r = resultSet.getDouble("sbt_cy_r");
            mdTaxChargedCy_r = resultSet.getDouble("tax_cha_cy_r");
            mdTaxRetainedCy_r = resultSet.getDouble("tax_ret_cy_r");
            mdTotalCy_r = resultSet.getDouble("tot_cy_r");
            mbDocCopy = resultSet.getBoolean("b_doc_cpy");
            mbClosedDps = resultSet.getBoolean("b_clo_dps");
            mbClosedIog = resultSet.getBoolean("b_clo_iog");
            mbAudited = resultSet.getBoolean("b_aud");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkDpsCategoryId = resultSet.getInt("fk_dps_ct");
            mnFkDpsClassId = resultSet.getInt("fk_dps_cl");
            mnFkDpsTypeId = resultSet.getInt("fk_dps_tp");
            mnFkDpsStatusId = resultSet.getInt("fk_dps_st");
            mnFkCurrencyId = resultSet.getInt("fk_cur");
            mnFkPaymentTypeId = resultSet.getInt("fk_pay_tp");
            mnFkModeOfPaymentTypeId = resultSet.getInt("fk_mop_tp");
            mnFkEmissionTypeId = resultSet.getInt("fk_emi_tp");
            mnFkOwnerBizPartnerId = resultSet.getInt("fk_own_bpr");
            mnFkOwnerBranchId = resultSet.getInt("fk_own_bra");
            mnFkWarehouseBizPartnerId_n = resultSet.getInt("fk_wah_bpr_n");
            mnFkWarehouseBranchId_n = resultSet.getInt("fk_wah_bra_n");
            mnFkWarehouseWarehouseId_n = resultSet.getInt("fk_wah_wah_n");
            mnFkBizPartnerBizPartnerId = resultSet.getInt("fk_bpr_bpr");
            mnFkBizPartnerBranchId = resultSet.getInt("fk_bpr_bra");
            mnFkBizPartnerAddressId = resultSet.getInt("fk_bpr_add");
            mnFkAgentId_n = resultSet.getInt("fk_agt_n");
            mnFkBookkeepingYearId_n = resultSet.getInt("fk_bkk_yer_n");
            mnFkBookkeepingNumberId_n = resultSet.getInt("fk_bkk_num_n");
            mnFkSourceDpsId_n = resultSet.getInt("fk_src_dps_n");
            mnFkUserClosedDpsId = resultSet.getInt("fk_usr_clo_dps");
            mnFkUserClosedIogId = resultSet.getInt("fk_usr_clo_iog");
            mnFkUserAuditedId = resultSet.getInt("fk_usr_aud");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserClosedDps = resultSet.getTimestamp("ts_usr_clo_dps");
            mtTsUserClosedIog = resultSet.getTimestamp("ts_usr_clo_iog");
            mtTsUserAudited = resultSet.getTimestamp("ts_usr_aud");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read aswell child registries:

            statement = session.getStatement().getConnection().createStatement();
            statementAux = session.getStatement().getConnection().createStatement();

            msSql = "SELECT id_dps FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS_EDS) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {
                moChildEds = new DDbDpsEds();
                moChildEds.read(session, new int[] { resultSet.getInt(1) });
            }

            msSql = "SELECT id_not FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS_NOT) + " " + getSqlWhere() +
                    "ORDER BY id_not ";
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbDpsNote child = new DDbDpsNote();
                child.read(session, new int[] { mnPkDpsId, resultSet.getInt(1) });
                mvChildNotes.add(child);
            }

            msSql = "SELECT id_row FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS_ROW) + " " + getSqlWhere() +
                    "ORDER BY id_row ";
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbDpsRow child = new DDbDpsRow();
                child.read(session, new int[] { mnPkDpsId, resultSet.getInt(1) });

                // Read aswell row serial numbers and information of import declarations:

                msSql = "SELECT s.id_itm, s.id_unt, s.id_lot, s.id_bpr, s.id_bra, s.id_wah, s.id_mov, s.mov_in, s.mov_out, s.snr, s.imp_dec, s.imp_dec_dt_n, l.lot, l.dt_exp_n " +
                        "FROM " + DModConsts.TablesMap.get(DModConsts.T_STK) + " AS s " +
                        "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.T_LOT) + " AS l ON " +
                        "s.id_itm = l.id_itm AND s.id_unt = l.id_unt AND s.id_lot = l.id_lot " +
                        "WHERE " +
                        (!child.isDpsRowAdjustment() ?
                            "s.fk_dps_inv_dps_n = " + child.getPkDpsId() + " AND s.fk_dps_inv_row_n = " + child.getPkRowId() + " AND " :
                            "s.fk_dps_adj_dps_n = " + child.getPkDpsId() + " AND s.fk_dps_adj_row_n = " + child.getPkRowId() + " AND ") +
                        "s.fk_bkk_yer_n = " + mnFkBookkeepingYearId_n + " AND s.fk_bkk_num_n = " + mnFkBookkeepingNumberId_n + " " +
                        "ORDER BY s.id_itm, s.id_unt, s.id_lot, s.id_bpr, s.id_bra, s.id_wah, s.id_mov ";
                resultSetAux = statementAux.executeQuery(msSql);
                while (resultSetAux.next()) {
                    DTrnStockMove move = new DTrnStockMove(
                            new int[] { resultSetAux.getInt(1), resultSetAux.getInt(2), resultSetAux.getInt(3), resultSetAux.getInt(4), resultSetAux.getInt(5), resultSetAux.getInt(6) },
                            DTrnUtils.isDpsForStockIn(this, true) ? resultSetAux.getDouble("s.mov_in") : resultSetAux.getDouble("s.mov_out"),
                            resultSetAux.getString("s.snr"),
                            resultSetAux.getString("s.imp_dec"),
                            resultSetAux.getDate("s.imp_dec_dt_n"),
                            resultSetAux.getString("l.lot"),
                            resultSetAux.getDate("l.dt_exp_n"));
                    child.getAuxStockMoves().add(move);
                }

                mvChildRows.add(child);
            }

            msSql = "SELECT id_iog FROM " + DModConsts.TablesMap.get(DModConsts.T_IOG) + " " +
                    "WHERE b_del = 0 AND fk_src_dps_n = " + mnPkDpsId + " ";

            resultSet = statement.executeQuery(msSql);
            if (resultSet.next()) {
                mnXtaIogId = resultSet.getInt(1);
            }
            
            if (moChildEds != null) {
                readEds();
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
            if (DTrnUtils.isDpsNumberAutomatic(getDpsClassKey())) {
                generateDpsNumberAutomatic(session);
            }

            computePrimaryKey(session);
            mbDeleted = false;
            //mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkDpsId + ", " +
                    "'" + msSeries + "', " +
                    mnNumber + ", " +
                    "'" + msOrder + "', " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtDateCredit) + "', " +
                    (mtDateDelivery_n == null ? "NULL" : "'" + DLibUtils.DbmsDateFormatDate.format(mtDateDelivery_n) + "'") + ", " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtDateDocOriginal) + "', " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtDateDocSending) + "', " +
                    "'" + DLibUtils.DbmsDateFormatDate.format(mtDateDocReception) + "', " +
                    mnTerminal + ", " +
                    mnApproveYear + ", " +
                    mnApproveNumber + ", " +
                    mnCreditDays + ", " +
                    "'" + msPaymentAccount + "', " +
                    "'" + msImportDeclaration + "', " +
                    (mtImportDeclarationDate_n == null ? "NULL" : "'" + DLibUtils.DbmsDateFormatDate.format(mtImportDeclarationDate_n) + "'") + ", " +
                    (mbDiscountDocApplying ? 1 : 0) + ", " +
                    (mbDiscountDocPercentageApplying ? 1 : 0) + ", " +
                    mdDiscountDocPercentage + ", " +
                    mdSubtotalProv_r + ", " +
                    mdDiscountDoc_r + ", " +
                    mdSubtotal_r + ", " +
                    mdTaxCharged_r + ", " +
                    mdTaxRetained_r + ", " +
                    mdTotal_r + ", " +
                    mdExchangeRate + ", " +
                    mdSubtotalProvCy_r + ", " +
                    mdDiscountDocCy_r + ", " +
                    mdSubtotalCy_r + ", " +
                    mdTaxChargedCy_r + ", " +
                    mdTaxRetainedCy_r + ", " +
                    mdTotalCy_r + ", " +
                    (mbDocCopy ? 1 : 0) + ", " +
                    (mbClosedDps ? 1 : 0) + ", " +
                    (mbClosedIog ? 1 : 0) + ", " +
                    (mbAudited ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkDpsCategoryId + ", " +
                    mnFkDpsClassId + ", " +
                    mnFkDpsTypeId + ", " +
                    mnFkDpsStatusId + ", " +
                    mnFkCurrencyId + ", " +
                    mnFkPaymentTypeId + ", " +
                    mnFkModeOfPaymentTypeId + ", " +
                    mnFkEmissionTypeId + ", " +
                    mnFkOwnerBizPartnerId + ", " +
                    mnFkOwnerBranchId + ", " +
                    (mnFkWarehouseBizPartnerId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkWarehouseBizPartnerId_n) + ", " +
                    (mnFkWarehouseBranchId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkWarehouseBranchId_n) + ", " +
                    (mnFkWarehouseWarehouseId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkWarehouseWarehouseId_n) + ", " +
                    mnFkBizPartnerBizPartnerId + ", " +
                    mnFkBizPartnerBranchId + ", " +
                    mnFkBizPartnerAddressId + ", " +
                    (mnFkAgentId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAgentId_n) + ", " +
                    (mnFkBookkeepingYearId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBookkeepingYearId_n) + ", " +
                    (mnFkBookkeepingNumberId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBookkeepingNumberId_n) + ", " +
                    (mnFkSourceDpsId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkSourceDpsId_n) + ", " +
                    mnFkUserClosedDpsId + ", " +
                    mnFkUserClosedIogId + ", " +
                    mnFkUserAuditedId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + ", " +
                    "NOW()" + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_dps = " + mnPkDpsId + ", " +
                    "ser = '" + msSeries + "', " +
                    "num = " + mnNumber + ", " +
                    "ord = '" + msOrder + "', " +
                    "dt = '" + DLibUtils.DbmsDateFormatDate.format(mtDate) + "', " +
                    "dt_cdt = '" + DLibUtils.DbmsDateFormatDate.format(mtDateCredit) + "', " +
                    "dt_dvy_n = " + (mtDateDelivery_n == null ? "NULL" : "'" + DLibUtils.DbmsDateFormatDate.format(mtDateDelivery_n) + "'") + ", " +
                    "dt_doc_ori = '" + DLibUtils.DbmsDateFormatDate.format(mtDateDocOriginal) + "', " +
                    "dt_doc_snd = '" + DLibUtils.DbmsDateFormatDate.format(mtDateDocSending) + "', " +
                    "dt_doc_rcp = '" + DLibUtils.DbmsDateFormatDate.format(mtDateDocReception) + "', " +
                    "ter = " + mnTerminal + ", " +
                    "apr_yer = " + mnApproveYear + ", " +
                    "apr_num = " + mnApproveNumber + ", " +
                    "cdt_day = " + mnCreditDays + ", " +
                    "pay_acc = '" + msPaymentAccount + "', " +
                    "imp_dec = '" + msImportDeclaration + "', " +
                    "imp_dec_dt_n = " + (mtImportDeclarationDate_n == null ? "NULL" : "'" + DLibUtils.DbmsDateFormatDate.format(mtImportDeclarationDate_n) + "'") + ", " +
                    "b_dsc_doc = " + (mbDiscountDocApplying ? 1 : 0) + ", " +
                    "b_dsc_doc_per = " + (mbDiscountDocPercentageApplying ? 1 : 0) + ", " +
                    "dsc_doc_per = " + mdDiscountDocPercentage + ", " +
                    "sbt_prv_r = " + mdSubtotalProv_r + ", " +
                    "dsc_doc_r = " + mdDiscountDoc_r + ", " +
                    "sbt_r = " + mdSubtotal_r + ", " +
                    "tax_cha_r = " + mdTaxCharged_r + ", " +
                    "tax_ret_r = " + mdTaxRetained_r + ", " +
                    "tot_r = " + mdTotal_r + ", " +
                    "exr = " + mdExchangeRate + ", " +
                    "sbt_prv_cy_r = " + mdSubtotalProvCy_r + ", " +
                    "dsc_doc_cy_r = " + mdDiscountDocCy_r + ", " +
                    "sbt_cy_r = " + mdSubtotalCy_r + ", " +
                    "tax_cha_cy_r = " + mdTaxChargedCy_r + ", " +
                    "tax_ret_cy_r = " + mdTaxRetainedCy_r + ", " +
                    "tot_cy_r = " + mdTotalCy_r + ", " +
                    "b_doc_cpy = " + (mbDocCopy ? 1 : 0) + ", " +
                    /*
                    "b_clo_dps = " + (mbClosedDps ? 1 : 0) + ", " +
                    "b_clo_iog = " + (mbClosedIog ? 1 : 0) + ", " +
                    "b_aud = " + (mbAudited ? 1 : 0) + ", " +
                    */
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_dps_ct = " + mnFkDpsCategoryId + ", " +
                    "fk_dps_cl = " + mnFkDpsClassId + ", " +
                    "fk_dps_tp = " + mnFkDpsTypeId + ", " +
                    "fk_dps_st = " + mnFkDpsStatusId + ", " +
                    "fk_cur = " + mnFkCurrencyId + ", " +
                    "fk_pay_tp = " + mnFkPaymentTypeId + ", " +
                    "fk_mop_tp = " + mnFkModeOfPaymentTypeId + ", " +
                    "fk_emi_tp = " + mnFkEmissionTypeId + ", " +
                    "fk_own_bpr = " + mnFkOwnerBizPartnerId + ", " +
                    "fk_own_bra = " + mnFkOwnerBranchId + ", " +
                    "fk_wah_bpr_n = " + (mnFkWarehouseBizPartnerId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkWarehouseBizPartnerId_n) + ", " +
                    "fk_wah_bra_n = " + (mnFkWarehouseBranchId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkWarehouseBranchId_n) + ", " +
                    "fk_wah_wah_n = " + (mnFkWarehouseWarehouseId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkWarehouseWarehouseId_n) + ", " +
                    "fk_bpr_bpr = " + mnFkBizPartnerBizPartnerId + ", " +
                    "fk_bpr_bra = " + mnFkBizPartnerBranchId + ", " +
                    "fk_bpr_add = " + mnFkBizPartnerAddressId + ", " +
                    "fk_agt_n = " + (mnFkAgentId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAgentId_n) + ", " +
                    "fk_bkk_yer_n = " + (mnFkBookkeepingYearId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBookkeepingYearId_n) + ", " +
                    "fk_bkk_num_n = " + (mnFkBookkeepingNumberId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkBookkeepingNumberId_n) + ", " +
                    "fk_src_dps_n = " + (mnFkSourceDpsId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkSourceDpsId_n) + ", " +
                    /*
                    "fk_usr_clo_dps = " + mnFkUserClosedDpsId + ", " +
                    "fk_usr_clo_iog = " + mnFkUserClosedIogId + ", " +
                    "fk_usr_aud = " + mnFkUserAuditedId + ", " +
                    "fk_usr_ins = " + mnFkUserInsertId + ", " +
                    */
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    /*
                    "ts_usr_clo_dps = " + "NOW()" + ", " +
                    "ts_usr_clo_iog = " + "NOW()" + ", " +
                    "ts_usr_aud = " + "NOW()" + ", " +
                    "ts_usr_ins = " + "NOW()" + ", " +
                    */
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        msSql = "DELETE FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS_NOT) + " " + getSqlWhere();
        session.getStatement().execute(msSql);

        for (DDbDpsNote child : mvChildNotes) {
            child.setPkDpsId(mnPkDpsId);
            child.setRegistryNew(true);
            child.save(session);
        }

        for (DDbDpsRow child : mvChildRows) {
            if (child.isRegistryNew() || child.isRegistryEdited()) {
                child.setPkDpsId(mnPkDpsId);
                child.save(session);
            }
        }

        // Aditional processing:

        computeBookkeeping(session);
        computeStock(session);
        computeEds(session);

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbDps clone() throws CloneNotSupportedException {
        DDbDps registry = new DDbDps();

        registry.setPkDpsId(this.getPkDpsId());
        registry.setSeries(this.getSeries());
        registry.setNumber(this.getNumber());
        registry.setOrder(this.getOrder());
        registry.setDate(this.getDate());
        registry.setDateCredit(this.getDateCredit());
        registry.setDateDelivery_n(this.getDateDelivery_n());
        registry.setDateDocOriginal(this.getDateDocOriginal());
        registry.setDateDocSending(this.getDateDocSending());
        registry.setDateDocReception(this.getDateDocReception());
        registry.setTerminal(this.getTerminal());
        registry.setApproveYear(this.getApproveYear());
        registry.setApproveNumber(this.getApproveNumber());
        registry.setCreditDays(this.getCreditDays());
        registry.setPaymentAccount(this.getPaymentAccount());
        registry.setImportDeclaration(this.getImportDeclaration());
        registry.setImportDeclarationDate_n(this.getImportDeclarationDate_n());
        registry.setDiscountDocApplying(this.isDiscountDocApplying());
        registry.setDiscountDocPercentageApplying(this.isDiscountDocPercentageApplying());
        registry.setDiscountDocPercentage(this.getDiscountDocPercentage());
        registry.setSubtotalProv_r(this.getSubtotalProv_r());
        registry.setDiscountDoc_r(this.getDiscountDoc_r());
        registry.setSubtotal_r(this.getSubtotal_r());
        registry.setTaxCharged_r(this.getTaxCharged_r());
        registry.setTaxRetained_r(this.getTaxRetained_r());
        registry.setTotal_r(this.getTotal_r());
        registry.setExchangeRate(this.getExchangeRate());
        registry.setSubtotalProvCy_r(this.getSubtotalProvCy_r());
        registry.setDiscountDocCy_r(this.getDiscountDocCy_r());
        registry.setSubtotalCy_r(this.getSubtotalCy_r());
        registry.setTaxChargedCy_r(this.getTaxChargedCy_r());
        registry.setTaxRetainedCy_r(this.getTaxRetainedCy_r());
        registry.setTotalCy_r(this.getTotalCy_r());
        registry.setDocCopy(this.isDocCopy());
        registry.setClosedDps(this.isClosedDps());
        registry.setClosedIog(this.isClosedIog());
        registry.setAudited(this.isAudited());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkDpsCategoryId(this.getFkDpsCategoryId());
        registry.setFkDpsClassId(this.getFkDpsClassId());
        registry.setFkDpsTypeId(this.getFkDpsTypeId());
        registry.setFkDpsStatusId(this.getFkDpsStatusId());
        registry.setFkCurrencyId(this.getFkCurrencyId());
        registry.setFkPaymentTypeId(this.getFkPaymentTypeId());
        registry.setFkModeOfPaymentTypeId(this.getFkModeOfPaymentTypeId());
        registry.setFkEmissionTypeId(this.getFkEmissionTypeId());
        registry.setFkOwnerBizPartnerId(this.getFkOwnerBizPartnerId());
        registry.setFkOwnerBranchId(this.getFkOwnerBranchId());
        registry.setFkWarehouseBizPartnerId_n(this.getFkWarehouseBizPartnerId_n());
        registry.setFkWarehouseBranchId_n(this.getFkWarehouseBranchId_n());
        registry.setFkWarehouseWarehouseId_n(this.getFkWarehouseWarehouseId_n());
        registry.setFkBizPartnerBizPartnerId(this.getFkBizPartnerBizPartnerId());
        registry.setFkBizPartnerBranchId(this.getFkBizPartnerBranchId());
        registry.setFkBizPartnerAddressId(this.getFkBizPartnerAddressId());
        registry.setFkAgentId_n(this.getFkAgentId_n());
        registry.setFkBookkeepingYearId_n(this.getFkBookkeepingYearId_n());
        registry.setFkBookkeepingNumberId_n(this.getFkBookkeepingNumberId_n());
        registry.setFkSourceDpsId_n(this.getFkSourceDpsId_n());
        registry.setFkUserClosedDpsId(this.getFkUserClosedDpsId());
        registry.setFkUserClosedIogId(this.getFkUserClosedIogId());
        registry.setFkUserAuditedId(this.getFkUserAuditedId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserClosedDps(this.getTsUserClosedDps());
        registry.setTsUserClosedIog(this.getTsUserClosedIog());
        registry.setTsUserAudited(this.getTsUserAudited());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setChildEds(moChildEds == null ? null : moChildEds.clone());

        for (DDbDpsNote child : mvChildNotes) {
            registry.getChildNotes().add(child.clone());
        }

        for (DDbDpsRow child : mvChildRows) {
            registry.getChildRows().add(child.clone());
        }

        //registry.setEdsUuid(this.getEdsUuid()); // read-only member!
        registry.setEdsMethodOfPayment(this.getEdsMethodOfPayment());
        registry.setEdsPaymentConditions(this.getEdsPaymentConditions());
        registry.setEdsConfirmation(this.getEdsConfirmation());
        registry.setEdsTaxRegime(this.getEdsTaxRegime());
        registry.setEdsUsage(this.getEdsUsage());

        registry.setXtaIogId(this.getXtaIogId());

        registry.setAuxNewDpsSeriesId(this.getAuxNewDpsSeriesId());
        registry.setAuxXmlTypeId(this.getAuxXmlTypeId());
        registry.setAuxEdsRequired(this.isAuxEdsRequired());
        registry.setAuxTotalQuantity(this.getAuxTotalQuantity());

        registry.setRegistryNew(this.isRegistryNew());

        // XXX Improve this:
        registry.setQueryResultId(this.getQueryResultId());
        registry.setQueryResult(this.getQueryResult());
        registry.setSql(this.getSql());

        return registry;
    }

    @Override
    public void setRegistryNew(boolean registryNew) {
        super.setRegistryNew(registryNew);

        if (moChildEds != null) {
            moChildEds.setRegistryNew(registryNew);
        }

        for (DDbDpsNote child : mvChildNotes) {
            child.setRegistryNew(registryNew);
        }

        for (DDbDpsRow child : mvChildRows) {
            child.setRegistryNew(registryNew);
        }
    }

    @Override
    public void saveField(final Statement statement, final int[] pk, final int field, final Object value) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        msSql = "UPDATE " + getSqlTable() + " SET ";

        switch (field) {
            case FIELD_CLOSED_DPS:
                msSql += "b_clo_dps = NOT b_clo_dps, fk_usr_clo_dps = " + (Integer) value + ", ts_usr_clo_dps = NOW() ";
                break;
            case FIELD_CLOSED_IOG:
                msSql += "b_clo_iog = NOT b_clo_iog, fk_usr_clo_iog = " + (Integer) value + ", ts_usr_clo_iog = NOW() ";
                break;
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
            // Validate document number:

            msQueryResult = DTrnUtils.validateNumberForDps(session, this);

            if (!msQueryResult.isEmpty()) {
                canSave = false;
            }
        }

        if (canSave) {
            // Validate stock:

            if (isStockRequired()) {
                year = DLibTimeUtils.digestYear(mtDate)[0];

                for (DDbDpsRow row : mvChildRows) {
                    if (row.isInventoriable()) {
                        if (mbRegistryNew || row.isRegistryNew()) {
                            // Validate as active rows:

                            if (DTrnUtils.isDpsForStockIn(this, true)) {
                                if (!DTrnDocRowUtils.validateRowIn(session, year, row)) {
                                    canSave = false;
                                    break;
                                }
                            }
                            else if (DTrnUtils.isDpsForStockOut(this, true)) {
                                if (!DTrnDocRowUtils.validateRowOut(session, year, row, new Vector<DTrnDocRow>(mvChildRows))) {
                                    canSave = false;
                                    break;
                                }
                            }
                        }
                        else if (row.isDeleted() && row.isRegistryEdited()) {
                            // Validate as inactive rows:

                            if (DTrnUtils.isDpsForStockIn(this, false)) {
                                if (!DTrnDocRowUtils.validateRowIn(session, year, row)) {
                                    canSave = false;
                                    break;
                                }
                            }
                            else if (DTrnUtils.isDpsForStockOut(this, false)) {
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
        boolean can = super.canDisable(session);

        if (can) {
            if (mbDeleted) {
                // Document is deleted already, so it will still remain as 'inactive', so no more validation is needed:
                
                can = true;
            }
            else {
                // If document is annuled already, i.e., 'inactive', then it will be activated by invoking method with parameter 'activate' = true, and viceversa:
                
                can = canChangeStatus(session, mnFkDpsStatusId == DModSysConsts.TS_DPS_ST_ANN);
            }
        }

        return can;
    }

    @Override
    public boolean canDelete(final DGuiSession session) throws SQLException, Exception {
        boolean can = super.canDelete(session);

        if (can) {
            if (mnFkDpsStatusId == DModSysConsts.TS_DPS_ST_ANN) {
                // Document is annuled already, so it will still remain as 'inactive', so no more validation is needed:
                
                can = true;
            }
            else {
                // If document is deleted already, i.e., 'inactive', then it will be activated by invoking method with parameter 'activate' = true, and viceversa:
                
                can = canChangeStatus(session, mbDeleted);
            }
        }

        return can;
    }

    @Override
    public void disable(final DGuiSession session) throws SQLException, Exception {
        if (mnFkDpsStatusId != DModSysConsts.TS_DPS_ST_ANN) {
            mnFkDpsStatusId = DModSysConsts.TS_DPS_ST_ANN;

            mdSubtotalProvCy_r = 0;
            mdDiscountDocCy_r = 0;
            mdSubtotalCy_r = 0;
            mdTaxChargedCy_r = 0;
            mdTaxRetainedCy_r = 0;
            mdTotalCy_r = 0;

            mdSubtotalProv_r = 0;
            mdDiscountDoc_r = 0;
            mdSubtotal_r = 0;
            mdTaxCharged_r = 0;
            mdTaxRetained_r = 0;
            mdTotal_r = 0;
        }
        else {
            mnFkDpsStatusId = DModSysConsts.TS_DPS_ST_ISS;
            computeTotal();
        }

        save(session);
        session.notifySuscriptors(mnRegistryType);
    }

    @Override
    public void delete(final DGuiSession session) throws SQLException, Exception {
        mbDeleted = !mbDeleted;
        save(session);
        session.notifySuscriptors(mnRegistryType);
    }

    public void computeTotal() {
        int position = 0;
        double subtotal = 0;
        double taxCharged = 0;
        double taxRetained = 0;
        double discountDoc = 0;
        double difference = 0;
        int decs = DLibUtils.getDecimalFormatAmount().getMaximumFractionDigits();
        DDbDpsRow rowGreater = null;

        mdSubtotalProvCy_r = 0;
        mdDiscountDocCy_r = 0;
        mdSubtotalCy_r = 0;
        mdTaxChargedCy_r = 0;
        mdTaxRetainedCy_r = 0;
        mdTotalCy_r = 0;

        mdAuxTotalQuantity = 0;

        for (DDbDpsRow row : mvChildRows) {
            row.computeTotal(mbDiscountDocApplying, mbDiscountDocPercentageApplying, mdDiscountDocPercentage, mdExchangeRate);

            if (row.isDeleted()) {
                row.setSortingPos(0);
            }
            else {
                row.setSortingPos(++position);

                mdSubtotalProvCy_r += row.getSubtotalProvCy_r();
                mdDiscountDocCy_r += row.getDiscountDocCy();
                mdSubtotalCy_r += row.getSubtotalCy_r();
                mdTaxChargedCy_r += row.getTaxChargedCy_r();
                mdTaxRetainedCy_r += row.getTaxRetainedCy_r();
                mdTotalCy_r += row.getTotalCy_r();

                mdAuxTotalQuantity += row.getQuantity();

                subtotal += row.getSubtotal_r();
                taxCharged += row.getTaxCharged_r();
                taxRetained += row.getTaxRetained_r();
                discountDoc += row.getDiscountDoc();
            }
        }

        mdTotal_r = DLibUtils.round(mdTotalCy_r * mdExchangeRate, decs);
        mdTaxCharged_r = taxCharged;
        mdTaxRetained_r = taxRetained;
        mdSubtotal_r = DLibUtils.round(mdTotal_r - mdTaxCharged_r + mdTaxRetained_r, decs);
        mdDiscountDoc_r = discountDoc;
        mdSubtotalProv_r = DLibUtils.round(mdSubtotal_r + mdDiscountDoc_r, decs);

        difference = DLibUtils.round(mdSubtotal_r - subtotal, decs);

        if (difference != 0) {
            for (DDbDpsRow row : mvChildRows) {
                if (!row.isDeleted()) {
                    if (rowGreater == null) {
                        rowGreater = row;
                    }
                    else {
                        if (row.getSubtotal_r() > rowGreater.getSubtotal_r()) {
                            rowGreater = row;
                        }
                    }
                }
            }

            if (rowGreater != null) {
                rowGreater.setSubtotal_r(DLibUtils.round(rowGreater.getSubtotal_r() + difference, decs));
                rowGreater.setSubtotalProv_r(DLibUtils.round(rowGreater.getSubtotalProv_r() + difference, decs));
            }
        }
    }

    public boolean hasDpsChanged(final String series, final int number, final String order, final Date date, final int emissionTypeId, final int[] dpsTypeKey) {
        return msSeries.compareTo(series) != 0 || mnNumber != number || msOrder.compareTo(order) != 0 ||
                !DLibTimeUtils.isSameDate(mtDate, date) || mnFkEmissionTypeId != emissionTypeId || !DLibUtils.compareKeys(getDpsTypeKey(), dpsTypeKey);
    }

    /**
     * Updates Electronic Document Supporting (EDS).
     */
    public void updateEds(final DGuiSession session, final DDbDpsEds eds) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        moChildEds = eds;
        saveEds(session, false);

        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    /**
     * Issues Electronic Document Supporting (EDS) as file of type Portable Document Format (PDF).
     */
    public void issueEds(final DGuiSession session) throws JRException, Exception {
        String fileName = "";
        DDbConfigBranch configBranch = null;
        
        if (moChildEds != null && moChildEds.getFkXmlStatusId() == DModSysConsts.TS_XML_ST_ISS) {
            configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, getCompanyBranchKey());
            fileName += configBranch.getEdsDirectory();
            fileName += moChildEds.getDocXmlName().replaceAll(".xml", ".pdf");

            switch (moChildEds.getFkXmlTypeId()) {
                case DModSysConsts.TS_XML_TP_CFD:
                    throw new UnsupportedOperationException("Not supported yet.");  // no plans for supporting it later

                case DModSysConsts.TS_XML_TP_CFDI_32:
                    DPrtUtils.exportReportToPdfFile(session, DModConsts.TR_DPS_CFDI_32, new DPrtDps(session, this).cratePrintMapCfdi32(), fileName);
                    break;

                case DModSysConsts.TS_XML_TP_CFDI_33:
                    DPrtUtils.exportReportToPdfFile(session, DModConsts.TR_DPS_CFDI_33, new DPrtDps(session, this).cratePrintMapCfdi33(), fileName);
                    break;

                default:
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
    }

    /**
     * Creates new entry for document printing log.
     */
    public DDbDpsPrinting createDpsPrinting() {
        DDbDpsPrinting dpsPrinting = new DDbDpsPrinting();

        dpsPrinting.setPkDpsId(this.getPkDpsId());
        dpsPrinting.setPkPrintingId(0);
        dpsPrinting.setSeries(this.getSeries());
        dpsPrinting.setNumber(this.getNumber());
        dpsPrinting.setOrder(this.getOrder());
        dpsPrinting.setDate(this.getDate());
        dpsPrinting.setDeleted(false);
        dpsPrinting.setFkEmissionTypeId(this.getFkEmissionTypeId());
        dpsPrinting.setFkDpsCategoryId(this.getFkDpsCategoryId());
        dpsPrinting.setFkDpsClassId(this.getFkDpsClassId());
        dpsPrinting.setFkDpsTypeId(this.getFkDpsTypeId());
        dpsPrinting.setFkUserInsertId(this.getFkUserInsertId());
        dpsPrinting.setFkUserUpdateId(this.getFkUserUpdateId());
        dpsPrinting.setTsUserInsert(this.getTsUserInsert());
        dpsPrinting.setTsUserUpdate(this.getTsUserUpdate());

        return dpsPrinting;
    }

    /**
     * Creates new entry for document signing log.
     */
    public DDbDpsSigning createDpsSigning() {
        DDbDpsSigning dpsSigning = new DDbDpsSigning();

        dpsSigning.setPkDpsId(this.getPkDpsId());
        dpsSigning.setPkSigningId(0);
        dpsSigning.setSeries(this.getSeries());
        dpsSigning.setNumber(this.getNumber());
        dpsSigning.setOrder(this.getOrder());
        dpsSigning.setDate(this.getDate());
        dpsSigning.setDeleted(false);
        dpsSigning.setFkEmissionTypeId(this.getFkEmissionTypeId());
        dpsSigning.setFkDpsCategoryId(this.getFkDpsCategoryId());
        dpsSigning.setFkDpsClassId(this.getFkDpsClassId());
        dpsSigning.setFkDpsTypeId(this.getFkDpsTypeId());
        dpsSigning.setFkUserInsertId(this.getFkUserInsertId());
        dpsSigning.setFkUserUpdateId(this.getFkUserUpdateId());
        dpsSigning.setTsUserInsert(this.getTsUserInsert());
        dpsSigning.setTsUserUpdate(this.getTsUserUpdate());

        return dpsSigning;
    }

    /**
     * Creates new entry for document sending log.
     */
    public DDbDpsSending createDpsSending() {
        DDbDpsSending dpsSending = new DDbDpsSending();

        dpsSending.setPkDpsId(this.getPkDpsId());
        dpsSending.setPkSendingId(0);
        dpsSending.setContact1("");
        dpsSending.setContact2("");
        dpsSending.setEmail1("");
        dpsSending.setEmail2("");
        dpsSending.setDeleted(false);
        dpsSending.setFkUserInsertId(this.getFkUserInsertId());
        dpsSending.setFkUserUpdateId(this.getFkUserUpdateId());
        dpsSending.setTsUserInsert(this.getTsUserInsert());
        dpsSending.setTsUserUpdate(this.getTsUserUpdate());

        return dpsSending;
    }
}
