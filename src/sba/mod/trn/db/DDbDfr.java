/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import cfd.DCfdConsts;
import cfd.DCfdUtils;
import cfd.DElement;
import cfd.DSubelementAddenda;
import cfd.ext.continental.DElementAddendaContinentalTire;
import cfd.ver40.DCfdi40Catalogs;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import sba.gui.DGuiClientApp;
import sba.gui.cat.DXmlCatalog;
import sba.gui.prt.DPrtUtils;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiEdsSignature;
import sba.lib.gui.DGuiSession;
import sba.lib.xml.DXmlUtils;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.bpr.db.DDbBizPartner;
import sba.mod.bpr.db.DDbBranch;
import sba.mod.cfg.db.DDbConfigBranch;
import sba.mod.cfg.db.DDbLock;
import sba.mod.cfg.db.DLockUtils;
import sba.mod.fin.db.DDbAbpBranchCash;
import sba.mod.fin.db.DDbBookkeepingMove;
import sba.mod.fin.db.DDbBookkeepingMoveCustom;
import sba.mod.fin.db.DDbBookkeepingNumber;
import sba.mod.fin.db.DFinUtils;
import sba.mod.lad.db.DDbBol;

/**
 * Digital Fiscal Receipt (DFR)
 * @author Sergio Flores
 */
public class DDbDfr extends DDbRegistryUser implements DTrnDoc {
    
    public static final int FIELD_CAN_ST = 1;

    /** Timeout in minutes.  */
    public static final int TIMEOUT = 3; // 3 min.
    
    protected int mnPkDfrId;
    protected String msSeries;
    protected int mnNumber;
    protected String msCertificateNumber;
    protected String msSignedText;
    protected String msSignature;
    protected String msUuid;
    protected Date mtDocTs;
    protected String msDocXml;
    protected String msDocXmlRaw;
    protected String msDocXmlAddenda;
    protected String msDocXmlName;
    protected String msCancelStatus;
    protected String msCancelXml;
    protected java.sql.Blob moCancelPdf_n;
    protected boolean mbBookkept;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkXmlTypeId;
    protected int mnFkXmlSubtypeId;
    protected int mnFkXmlSubtypeVersionId;
    protected int mnFkXmlStatusId;
    protected int mnFkXmlAddendaTypeId;
    protected int mnFkXmlSignatureProviderId;
    protected int mnFkCertificateId;
    protected int mnFkOwnerBizPartnerId;
    protected int mnFkOwnerBranchId;
    protected int mnFkBizPartnerId;
    protected int mnFkDpsId_n;
    protected int mnFkBolId_n;
    protected int mnFkCashBizPartnerId_n;
    protected int mnFkCashBranchId_n;
    protected int mnFkCashCashId_n;
    protected int mnFkBookkeepingYearId_n;
    protected int mnFkBookkeepingNumberId_n;
    protected int mnFkUserIssuedId;
    protected int mnFkUserAnnulledId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    */
    protected Date mtTsUserIssued;
    protected Date mtTsUserAnnulled;
    /*
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */
    
    protected DDfrMate moXtaDfrMate;
    
    protected cfd.ver33.crp10.DElementPagos moDfrPagos10;
    protected cfd.ver40.crp20.DElementPagos moDfrPagos20;

    protected boolean mbAuxDfrJustIssued;
    protected boolean mbAuxDfrJustAnnulled;
    protected boolean mbAuxAddendaJustSet;
    protected int[] manAuxXmlSignatureRequestKey;
    protected DDbLock moAuxLock;
    protected DDbLock moAuxDpsLock;
    
    public DDbDfr() {
        super(DModConsts.T_DFR);
        initRegistry();
    }
    
    /*
     * Private methods:
     */
    
    /**
     * Is this DFR a Complemento de Recepción de Pagos?
     * @return 
     */
    private boolean isCrp() {
        return mnFkXmlSubtypeId == DModSysConsts.TS_XML_STP_CRP;
    }

    /**
     * Prepare this DFR for CRP. Works only if DFR was not just issued nor annulled!
     * Supported only creation of CFDI 3.3 with Complemento de Recepción de Pagos 1.0 and CFDI 4.0 with Complemento de Recepción de Pagos 2.0.
     * @param session GUI user session.
     * @throws Exception 
     */
    private void prepareDfrForCrp(final DGuiSession session) throws Exception {
        if (isCrp() && !mbAuxDfrJustIssued && !mbAuxDfrJustAnnulled) {
            if (mnFkXmlTypeId == 0) {
                throw new Exception(DGuiConsts.ERR_MSG_FIELD_REQ + "'FK tipo XML'.");
            }
            else if (mnFkOwnerBizPartnerId == 0) {
                throw new Exception(DGuiConsts.ERR_MSG_FIELD_REQ + "'FK empresa'.");
            }
            else if (mnFkOwnerBranchId == 0) {
                throw new Exception(DGuiConsts.ERR_MSG_FIELD_REQ + "'FK sucursal empresa'.");
            }
            if (mnFkBizPartnerId == 0) {
                throw new Exception(DGuiConsts.ERR_MSG_FIELD_REQ + "'FK asociado de negocios'.");
            }
            
            // generate CFDI 3.3 with CRP 1.0 of CFDI 4.0 with CRP 2.0:
            
            if (msSeries.isEmpty()) {
                msSeries = DCfdi40Catalogs.CFD_TP_P; // default series if no one provided
            }
            
            if (mnNumber == 0) {
                mnNumber = getNextNumber(session, msSeries);
            }

            int xmlStatus = DLibConsts.UNDEFINED;
            String textToSign;
            String textSigned;
            DGuiEdsSignature signature;
            DDbBizPartner bizPartner = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, getBizPartnerKey());
            DDbConfigBranch configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, getCompanyBranchKey());
            cfd.DCfd cfd = new cfd.DCfd(configBranch.getDfrDirectory());
            cfd.DSubelementAddenda extAddenda = null;

            switch (mnFkXmlTypeId) {
                case DModSysConsts.TS_XML_TP_CFD:
                case DModSysConsts.TS_XML_TP_CFDI_32:
                    throw new UnsupportedOperationException("Not supported yet.");  // no plans for supporting it later

                case DModSysConsts.TS_XML_TP_CFDI_33:
                    // Create DFR:
                    signature = session.getEdsSignature(getCompanyBranchKey());
                    cfd.ver33.DElementComprobante comprobante33 = createCfdi33(session, signature);

                    // Append to DFR the very addenda previously added to DPS if any:
                    if (!msDocXmlAddenda.isEmpty()) {
                        extAddenda = DTrnDfrUtils.extractExtAddenda(msDocXmlAddenda, bizPartner.getFkXmlAddendaTypeId());
                        if (extAddenda != null) {
                            cfd.ver3.DElementAddenda addenda = new cfd.ver3.DElementAddenda();
                            addenda.getElements().add(extAddenda);
                            comprobante33.setEltOpcAddenda(addenda);
                        }
                    }

                    // Sign DFR:
                    textToSign = comprobante33.getElementForOriginalString();
                    textSigned = signature.signText(textToSign, DLibTimeUtils.digestYear(mtDocTs)[0]);
                    cfd.write(comprobante33, textToSign, textSigned, signature.getCertificateNumber(), signature.getCertificateBase64());

                    // Set DFR status:
                    xmlStatus = DModSysConsts.TS_XML_ST_PEN;
                    break;

                case DModSysConsts.TS_XML_TP_CFDI_40:
                    // Create DFR:
                    signature = session.getEdsSignature(getCompanyBranchKey());
                    cfd.ver40.DElementComprobante comprobante40 = createCfdi40(session, signature);

                    // Append to DFR the very addenda previously added to DPS if any:
                    if (!msDocXmlAddenda.isEmpty()) {
                        extAddenda = DTrnDfrUtils.extractExtAddenda(msDocXmlAddenda, bizPartner.getFkXmlAddendaTypeId());
                        if (extAddenda != null) {
                            cfd.ver4.DElementAddenda addenda = new cfd.ver4.DElementAddenda();
                            addenda.getElements().add(extAddenda);
                            comprobante40.setEltOpcAddenda(addenda);
                        }
                    }

                    // Sign DFR:
                    textToSign = comprobante40.getElementForOriginalString();
                    textSigned = signature.signText(textToSign, DLibTimeUtils.digestYear(mtDocTs)[0]);
                    cfd.write(comprobante40, textToSign, textSigned, signature.getCertificateNumber(), signature.getCertificateBase64());

                    // Set DFR status:
                    xmlStatus = DModSysConsts.TS_XML_ST_PEN;
                    break;

                default:
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }

            // Create DFR:

            //mnPkDfrId;
            //msSeries; // should be already provided
            //mnNumber; // will be defined by system
            msCertificateNumber = session.getEdsSignature(getCompanyBranchKey()).getCertificateNumber();
            msSignedText = cfd.getLastStringSigned();
            msSignature = cfd.getLastSignature();
            msUuid = "";
            mtDocTs = cfd.getLastTimestamp();
            msDocXml = cfd.getLastXml();
            msDocXmlRaw = "";
            msDocXmlAddenda = extAddenda == null ? "" : extAddenda.getElementForXml();
            msDocXmlName = cfd.getLastXmlFileName();
            msCancelStatus = "";
            msCancelXml = "";
            moCancelPdf_n = null;
            //mbBookkept; // should be already provided
            /*
            mbDeleted;
            mbSystem;
            */
            //mnFkXmlTypeId; // should be already provided
            mnFkXmlSubtypeId = DModSysConsts.TS_XML_STP_VER_CRP_2[0];
            mnFkXmlSubtypeVersionId = DModSysConsts.TS_XML_STP_VER_CRP_2[1];
            mnFkXmlStatusId = xmlStatus;
            mnFkXmlAddendaTypeId = msDocXmlAddenda.isEmpty() ? DModSysConsts.TS_XML_ADD_TP_NA : bizPartner.getFkXmlAddendaTypeId();
            mnFkXmlSignatureProviderId = DModSysConsts.CS_XSP_NA;
            mnFkCertificateId = session.getEdsSignature(getCompanyBranchKey()).getCertificateId();
            //mnFkOwnerBizPartnerId;    // should be already provided
            //mnFkOwnerBranchId;        // should be already provided
            //mnFkBizPartnerId;         // should be already provided
            mnFkDpsId_n = 0;
            mnFkBolId_n = 0;
            //mnFkCashBizPartnerId_n;       // should be already provided
            //mnFkCashBranchId_n;           // should be already provided
            //mnFkCashCashId_n;             // should be already provided
            //mnFkBookkeepingYearId_n;      // should be already provided
            //mnFkBookkeepingNumberId_n;    // should be already provided
            /*
            mnFkUserIssuedId;
            mnFkUserAnnulledId;
            mnFkUserInsertId;
            mnFkUserUpdateId;
            mtTsUserIssued;
            mtTsUserAnnulled;
            mtTsUserInsert;
            mtTsUserUpdate;
            */
            
            /*
            mbAuxDfrJustIssued;
            mbAuxDfrJustAnnulled;
            mbAuxRewriteXmlOnSave;
            mbAuxAddendaJustSet;
            manAuxXmlSignatureRequestKey;
            */
        }
    }
    
    /**
     * Create payment moves for CRP.
     * @param session GUI user session.
     * @param pago10 XML-element Pago 1.0.
     * @param abpBranchCash Automatic bookkeeping package for branch cash.
     * @return
     * @throws Exception 
     */
    private ArrayList<DDbBookkeepingMove> createPaymentMoves10ForCrp(final DGuiSession session, final cfd.ver33.crp10.DElementPagosPago pago10, final DDbAbpBranchCash abpBranchCash) throws Exception {
        ArrayList<DDbBookkeepingMove> moves = new ArrayList<>();
        
        DDbBookkeepingMove bkkMoveCash = new DDbBookkeepingMove();

        bkkMoveCash.setPkYearId(mnFkBookkeepingYearId_n);
        bkkMoveCash.setPkMoveId(0);
        //moBookkeepingMove.setDate(?);     // value will be set by bookkeeping custom move object
        bkkMoveCash.setSupporting(pago10.getAttNumOperacion().getString());
        bkkMoveCash.setReference("");
        bkkMoveCash.setText("CFDI " + getDfrNumber());

        double xrt = pago10.getAttTipoCambioP().getDouble() == 0 ? 1d : pago10.getAttTipoCambioP().getDouble();
        bkkMoveCash.setDebit(DLibUtils.roundAmount(pago10.getAttMonto().getDouble() * xrt));
        bkkMoveCash.setCredit(0);
        bkkMoveCash.setDebitCy(pago10.getAttMonto().getDouble());
        bkkMoveCash.setCreditCy(0);
        
        DXmlCatalog xmlCatalogCurrency = ((DGuiClientApp) session.getClient()).getXmlCatalogsMap().get(DCfdi40Catalogs.CAT_MON);
        DXmlCatalog xmlCatalogModeOfPayment = ((DGuiClientApp) session.getClient()).getXmlCatalogsMap().get(DCfdi40Catalogs.CAT_FDP);

        bkkMoveCash.setExchangeRate(xrt);
        bkkMoveCash.setUnits(0);
        //moBkkMoveCash.setSortingPos(?);               // value will be set by bookkeeping custom move object
        bkkMoveCash.setExchangeRateDifference(false);   // XXX Code for future versions
        bkkMoveCash.setAvailable(true);
        bkkMoveCash.setDeleted(false);
        bkkMoveCash.setSystem(false);
        bkkMoveCash.setFkAccountId(abpBranchCash.getFkAccountCashId());
        bkkMoveCash.setFkSystemAccountTypeId(DModSysConsts.FS_SYS_ACC_TP_ENT_CSH);
        bkkMoveCash.setFkSystemMoveClassId(DModSysConsts.FS_SYS_MOV_TP_MI_CUS_PAY[0]);
        bkkMoveCash.setFkSystemMoveTypeId(DModSysConsts.FS_SYS_MOV_TP_MI_CUS_PAY[1]);
        bkkMoveCash.setFkDiverseMoveTypeId(DModSysConsts.FS_DIV_MOV_TP_NA);
        bkkMoveCash.setFkCurrencyId(xmlCatalogCurrency.getId(pago10.getAttMonedaP().getString()));
        bkkMoveCash.setFkPaymentTypeId(DModSysConsts.FS_PAY_TP_NA);
        bkkMoveCash.setFkModeOfPaymentTypeId(xmlCatalogModeOfPayment.getId(pago10.getAttFormaDePagoP().getString()));
        bkkMoveCash.setFkValueTypeId(DModSysConsts.FS_VAL_TP_CSH);
        //moBkkMoveCash.setFkOwnerBizPartnerId(?);      // value will be set by bookkeeping custom move object
        //moBkkMoveCash.setFkOwnerBranchId(?);          // value will be set by bookkeeping custom move object
        bkkMoveCash.setFkCashBizPartnerId_n(mnFkCashBizPartnerId_n);
        bkkMoveCash.setFkCashBranchId_n(mnFkCashBranchId_n);
        bkkMoveCash.setFkCashCashId_n(mnFkCashCashId_n);
        bkkMoveCash.setFkWarehouseBizPartnerId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkWarehouseBranchId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkWarehouseWarehouseId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkBizPartnerBizPartnerId_n(mnFkBizPartnerId);
        bkkMoveCash.setFkBizPartnerBranchId_n(DUtilConsts.BPR_BRA_ID);
        bkkMoveCash.setFkDpsInvId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkDpsAdjId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkDfrId_n(mnPkDfrId);
        bkkMoveCash.setFkIogId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkIomId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkPusId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkItemId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkItemAuxId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkUnitId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkRecordYearId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkRecordRecordId_n(DLibConsts.UNDEFINED);
        //moBookkeepingMove.setFkBookkeepingYearId_n(?);    // value will be set by bookkeeping custom move object
        //moBookkeepingMove.setFkBookkeepingNumberId_n(?);  // value will be set by bookkeeping custom move object
        bkkMoveCash.setFkUserAvailableId(session.getUser().getPkUserId());
        
        moves.add(bkkMoveCash);
        
        for (cfd.ver33.crp10.DElementDoctoRelacionado doctoRelacionado : pago10.getEltDoctoRelacionados()) {
            DDbBookkeepingMove bkkMovePayment = bkkMoveCash.createComplement(session);
            
            double xrtDr = doctoRelacionado.getAttTipoCambioDR().getDouble() == 0 ? 1d : doctoRelacionado.getAttTipoCambioDR().getDouble();
            bkkMovePayment.setDebit(0);
            bkkMovePayment.setCredit(DLibUtils.roundAmount(doctoRelacionado.getAttImpPagado().getDouble() / xrtDr));
            bkkMovePayment.setDebitCy(0);
            bkkMovePayment.setCreditCy(doctoRelacionado.getAttImpPagado().getDouble());
            bkkMovePayment.setExchangeRate(bkkMovePayment.getCreditCy() == 0 ? 0d : DLibUtils.round(bkkMovePayment.getCredit() / bkkMovePayment.getCreditCy(), DLibUtils.getDecimalFormatExchangeRate().getMaximumFractionDigits()));
            bkkMovePayment.setFkCurrencyId(xmlCatalogCurrency.getId(doctoRelacionado.getAttMonedaDR().getString()));
            
            DDbDps dps = DTrnUtils.getDpsByUuid(session, doctoRelacionado.getAttIdDocumento().getString());
            bkkMovePayment.setReference(dps.getDpsReference(session));
            bkkMovePayment.setFkDpsInvId_n(dps.getPkDpsId());
            
            moves.add(bkkMovePayment);
        }
        
        return moves;
    }
    
    /**
     * Create payment moves for CRP.
     * @param session GUI user session.
     * @param pago10 XML-element Pago 2.0.
     * @param abpBranchCash Automatic bookkeeping package for branch cash.
     * @return
     * @throws Exception 
     */
    private ArrayList<DDbBookkeepingMove> createPaymentMoves20ForCrp(final DGuiSession session, final cfd.ver40.crp20.DElementPagosPago pago20, final DDbAbpBranchCash abpBranchCash) throws Exception {
        ArrayList<DDbBookkeepingMove> moves = new ArrayList<>();
        
        DDbBookkeepingMove bkkMoveCash = new DDbBookkeepingMove();

        bkkMoveCash.setPkYearId(mnFkBookkeepingYearId_n);
        bkkMoveCash.setPkMoveId(0);
        //moBookkeepingMove.setDate(?);     // value will be set by bookkeeping custom move object
        bkkMoveCash.setSupporting(pago20.getAttNumOperacion().getString());
        bkkMoveCash.setReference("");
        bkkMoveCash.setText("CFDI " + getDfrNumber());

        double xrt = pago20.getAttTipoCambioP().getDouble() == 0 ? 1d : pago20.getAttTipoCambioP().getDouble();
        bkkMoveCash.setDebit(DLibUtils.roundAmount(pago20.getAttMonto().getDouble() * xrt));
        bkkMoveCash.setCredit(0);
        bkkMoveCash.setDebitCy(pago20.getAttMonto().getDouble());
        bkkMoveCash.setCreditCy(0);
        
        DXmlCatalog xmlCatalogCurrency = ((DGuiClientApp) session.getClient()).getXmlCatalogsMap().get(DCfdi40Catalogs.CAT_MON);
        DXmlCatalog xmlCatalogModeOfPayment = ((DGuiClientApp) session.getClient()).getXmlCatalogsMap().get(DCfdi40Catalogs.CAT_FDP);

        bkkMoveCash.setExchangeRate(xrt);
        bkkMoveCash.setUnits(0);
        //moBkkMoveCash.setSortingPos(?);               // value will be set by bookkeeping custom move object
        bkkMoveCash.setExchangeRateDifference(false);   // XXX Code for future versions
        bkkMoveCash.setAvailable(true);
        bkkMoveCash.setDeleted(false);
        bkkMoveCash.setSystem(false);
        bkkMoveCash.setFkAccountId(abpBranchCash.getFkAccountCashId());
        bkkMoveCash.setFkSystemAccountTypeId(DModSysConsts.FS_SYS_ACC_TP_ENT_CSH);
        bkkMoveCash.setFkSystemMoveClassId(DModSysConsts.FS_SYS_MOV_TP_MI_CUS_PAY[0]);
        bkkMoveCash.setFkSystemMoveTypeId(DModSysConsts.FS_SYS_MOV_TP_MI_CUS_PAY[1]);
        bkkMoveCash.setFkDiverseMoveTypeId(DModSysConsts.FS_DIV_MOV_TP_NA);
        bkkMoveCash.setFkCurrencyId(xmlCatalogCurrency.getId(pago20.getAttMonedaP().getString()));
        bkkMoveCash.setFkPaymentTypeId(DModSysConsts.FS_PAY_TP_NA);
        bkkMoveCash.setFkModeOfPaymentTypeId(xmlCatalogModeOfPayment.getId(pago20.getAttFormaDePagoP().getString()));
        bkkMoveCash.setFkValueTypeId(DModSysConsts.FS_VAL_TP_CSH);
        //moBkkMoveCash.setFkOwnerBizPartnerId(?);      // value will be set by bookkeeping custom move object
        //moBkkMoveCash.setFkOwnerBranchId(?);          // value will be set by bookkeeping custom move object
        bkkMoveCash.setFkCashBizPartnerId_n(mnFkCashBizPartnerId_n);
        bkkMoveCash.setFkCashBranchId_n(mnFkCashBranchId_n);
        bkkMoveCash.setFkCashCashId_n(mnFkCashCashId_n);
        bkkMoveCash.setFkWarehouseBizPartnerId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkWarehouseBranchId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkWarehouseWarehouseId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkBizPartnerBizPartnerId_n(mnFkBizPartnerId);
        bkkMoveCash.setFkBizPartnerBranchId_n(DUtilConsts.BPR_BRA_ID);
        bkkMoveCash.setFkDpsInvId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkDpsAdjId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkDfrId_n(mnPkDfrId);
        bkkMoveCash.setFkIogId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkIomId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkPusId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkItemId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkItemAuxId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkUnitId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkRecordYearId_n(DLibConsts.UNDEFINED);
        bkkMoveCash.setFkRecordRecordId_n(DLibConsts.UNDEFINED);
        //moBookkeepingMove.setFkBookkeepingYearId_n(?);    // value will be set by bookkeeping custom move object
        //moBookkeepingMove.setFkBookkeepingNumberId_n(?);  // value will be set by bookkeeping custom move object
        bkkMoveCash.setFkUserAvailableId(session.getUser().getPkUserId());
        
        moves.add(bkkMoveCash);
        
        for (cfd.ver40.crp20.DElementDoctoRelacionado doctoRelacionado : pago20.getEltDoctoRelacionados()) {
            DDbBookkeepingMove bkkMovePayment = bkkMoveCash.createComplement(session);
            
            double xrtDr = doctoRelacionado.getAttEquivalenciaDR().getDouble() == 0 ? 1d : doctoRelacionado.getAttEquivalenciaDR().getDouble();
            bkkMovePayment.setDebit(0);
            bkkMovePayment.setCredit(DLibUtils.roundAmount(doctoRelacionado.getAttImpPagado().getDouble() / xrtDr));
            bkkMovePayment.setDebitCy(0);
            bkkMovePayment.setCreditCy(doctoRelacionado.getAttImpPagado().getDouble());
            bkkMovePayment.setExchangeRate(bkkMovePayment.getCreditCy() == 0 ? 0d : DLibUtils.round(bkkMovePayment.getCredit() / bkkMovePayment.getCreditCy(), DLibUtils.getDecimalFormatExchangeRate().getMaximumFractionDigits()));
            bkkMovePayment.setFkCurrencyId(xmlCatalogCurrency.getId(doctoRelacionado.getAttMonedaDR().getString()));
            
            DDbDps dps = DTrnUtils.getDpsByUuid(session, doctoRelacionado.getAttIdDocumento().getString());
            bkkMovePayment.setReference(dps.getDpsReference(session));
            bkkMovePayment.setFkDpsInvId_n(dps.getPkDpsId());
            
            moves.add(bkkMovePayment);
        }
        
        return moves;
    }

    /**
     * Compute bookkeeping for CFR. Works only if DFR was not just issued but annulled!
     * Allways clear previous bookkeeping moves, if any, and create new ones.
     * @param session
     * @throws SQLException
     * @throws Exception 
     */
    private void computeBookkeepingForCrp(final DGuiSession session) throws SQLException, Exception {
        if (isCrp() && !mbAuxDfrJustIssued) {
            DDbBookkeepingNumber bkkNumber;
            
            // delete previous bookkeeping moves, if any:
            
            msSql = "UPDATE " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " "
                    + "SET b_del = 1, fk_usr_upd = " + session.getUser().getPkUserId() + ", ts_usr_upd = NOW() "
                    + "WHERE fk_dfr_n = " + mnPkDfrId + " AND NOT b_del;";
            session.getStatement().execute(msSql);
            
            // delete previous bookkeeping number (it will attempt to delete as well bookkeping moves), if any:
            
            if (mnFkBookkeepingYearId_n != DLibConsts.UNDEFINED && mnFkBookkeepingNumberId_n != DLibConsts.UNDEFINED) {
                bkkNumber = new DDbBookkeepingNumber();
                bkkNumber.read(session, new int[] { mnFkBookkeepingYearId_n, mnFkBookkeepingNumberId_n });
                bkkNumber.setDeleted(true);
                bkkNumber.save(session);

                if (!mbBookkept) {
                    // clear reference to bookkeeping number in this DFR:

                    mnFkBookkeepingYearId_n = 0;
                    mnFkBookkeepingNumberId_n = 0;

                    msSql = "UPDATE " + getSqlTable() + " "
                            + "SET fk_bkk_yer_n = NULL AND fk_bkk_num_n = NULL " + getSqlWhere();
                    session.getStatement().execute(msSql);
                }
            }
            
            if (mbBookkept && !mbDeleted && mnFkXmlStatusId < DModSysConsts.TS_XML_ST_ANN) {
                // save bookkeeping moves:

                bkkNumber = new DDbBookkeepingNumber();
                bkkNumber.setPkYearId(DLibTimeUtils.digestYear(mtDocTs)[0]);
                bkkNumber.save(session);

                // update reference to bookkeeping number in this DFR:

                mnFkBookkeepingYearId_n = bkkNumber.getPkYearId();
                mnFkBookkeepingNumberId_n = bkkNumber.getPkNumberId();

                msSql = "UPDATE " + getSqlTable() + " "
                        + "SET fk_bkk_yer_n = " + mnFkBookkeepingYearId_n + ", fk_bkk_num_n = " + mnFkBookkeepingNumberId_n + " " + getSqlWhere();
                session.getStatement().execute(msSql);

                DDbBookkeepingMoveCustom bkkMoveCustom = new DDbBookkeepingMoveCustom();
                
                bkkMoveCustom.setAuxRenewBkkNumber(false); // prevents creation of a new bookkeeping number!
                bkkMoveCustom.setPkBookkeepingYearId(mnFkBookkeepingYearId_n);
                bkkMoveCustom.setPkBookkeepingNumberId(mnFkBookkeepingNumberId_n);
                bkkMoveCustom.setDate(mtDocTs);
                bkkMoveCustom.setFkSystemMoveClassId(DModSysConsts.FS_SYS_MOV_TP_MI_CUS_PAY[0]);
                bkkMoveCustom.setFkSystemMoveTypeId(DModSysConsts.FS_SYS_MOV_TP_MI_CUS_PAY[1]);
                bkkMoveCustom.setFkOwnerBizPartnerId(mnFkOwnerBizPartnerId);
                bkkMoveCustom.setFkOwnerBranchId(mnFkOwnerBranchId);
                
                DDbAbpBranchCash abpBranchCash = DFinUtils.readAbpBranchCash(session, getBranchCashKey_n());
                
                switch (mnFkXmlTypeId) {
                    case DModSysConsts.TS_XML_TP_CFDI_33:
                        cfd.ver33.DElementComprobante comprobante33 = getElementComprobante33();

                        if (comprobante33.getEltOpcComplemento() != null) {
                            for (DElement element : comprobante33.getEltOpcComplemento().getElements()) {
                                if (element instanceof cfd.ver33.crp10.DElementPagos) {
                                    cfd.ver33.crp10.DElementPagos pagos10 = (cfd.ver33.crp10.DElementPagos) element;
                                    for (cfd.ver33.crp10.DElementPagosPago pago10 : pagos10.getEltPagos()) {
                                        bkkMoveCustom.getChildMoves().addAll(createPaymentMoves10ForCrp(session, pago10, abpBranchCash));
                                    }
                                    break;
                                }
                            }
                        }
                        break;
                        
                    case DModSysConsts.TS_XML_TP_CFDI_40:
                        cfd.ver40.DElementComprobante comprobante40 = getElementComprobante40();

                        if (comprobante40.getEltOpcComplemento() != null) {
                            for (DElement element : comprobante40.getEltOpcComplemento().getElements()) {
                                if (element instanceof cfd.ver40.crp20.DElementPagos) {
                                    cfd.ver40.crp20.DElementPagos pagos20 = (cfd.ver40.crp20.DElementPagos) element;
                                    for (cfd.ver40.crp20.DElementPagosPago pago20 : pagos20.getEltPagos()) {
                                        bkkMoveCustom.getChildMoves().addAll(createPaymentMoves20ForCrp(session, pago20, abpBranchCash));
                                    }
                                    break;
                                }
                            }
                        }
                        break;
                        
                    default:
                        throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                
                bkkMoveCustom.save(session);
            }
        }
    }
    
    /**
     * Embeed addenda to this DFR.
     * @param session GUI user session.
     * @throws Exception 
     */
    @SuppressWarnings("deprecation")
    private void embeedAddenda(final DGuiSession session) throws Exception {
        if (mbAuxAddendaJustSet) {
            DSubelementAddenda extAddenda = null;

            switch (mnFkXmlTypeId) {
                case DModSysConsts.TS_XML_TP_CFD:
                    throw new UnsupportedOperationException("Not supported yet.");  // no plans for supporting it later

                case DModSysConsts.TS_XML_TP_CFDI_32:
                    // Create DFR:
                    cfd.ver32.DElementComprobante comprobante32 = DCfdUtils.getCfdi32(getSuitableDocXml());
                    DTrnDfrUtils.configureCfdi32(session, comprobante32);

                    // Append to DFR the very addenda previously added to DPS if any:
                    if (!msDocXmlAddenda.isEmpty()) {
                        extAddenda = DTrnDfrUtils.extractExtAddenda(msDocXmlAddenda, mnFkXmlAddendaTypeId);
                        if (extAddenda != null) {
                            cfd.ver3.DElementAddenda addenda = null;

                            if (comprobante32.getEltOpcAddenda() != null) {
                                addenda = comprobante32.getEltOpcAddenda();
                            }
                            else {
                                addenda = new cfd.ver3.DElementAddenda();
                                comprobante32.setEltOpcAddenda(addenda);
                            }

                            for (DElement element : addenda.getElements()) {
                                if (element instanceof DElementAddendaContinentalTire) {
                                    addenda.getElements().remove(element);
                                    break;
                                }
                            }

                            addenda.getElements().add(extAddenda);
                        }
                    }

                    msDocXml = DCfdConsts.XML_HEADER + comprobante32.getElementForXml();
                    break;

                case DModSysConsts.TS_XML_TP_CFDI_33:
                    // Create DFR:
                    cfd.ver33.DElementComprobante comprobante33 = DCfdUtils.getCfdi33(getSuitableDocXml());
                    DTrnDfrUtils.configureCfdi33(session, comprobante33);

                    // Append to DFR the very addenda previously added to DPS if any:
                    if (!msDocXmlAddenda.isEmpty()) {
                        extAddenda = DTrnDfrUtils.extractExtAddenda(msDocXmlAddenda, mnFkXmlAddendaTypeId);
                        if (extAddenda != null) {
                            cfd.ver3.DElementAddenda addenda = null;

                            if (comprobante33.getEltOpcAddenda() != null) {
                                addenda = comprobante33.getEltOpcAddenda();
                            }
                            else {
                                addenda = new cfd.ver3.DElementAddenda();
                                comprobante33.setEltOpcAddenda(addenda);
                            }

                            for (DElement element : addenda.getElements()) {
                                if (element instanceof DElementAddendaContinentalTire) {
                                    addenda.getElements().remove(element);
                                    break;
                                }
                            }

                            addenda.getElements().add(extAddenda);
                        }
                    }

                    msDocXml = DCfdConsts.XML_HEADER + comprobante33.getElementForXml();
                    break;

                case DModSysConsts.TS_XML_TP_CFDI_40:
                    // Create DFR:
                    cfd.ver40.DElementComprobante comprobante40 = DCfdUtils.getCfdi40(getSuitableDocXml());
                    DTrnDfrUtils.configureCfdi40(session, comprobante40);

                    // Append to DFR the very addenda previously added to DPS if any:
                    if (!msDocXmlAddenda.isEmpty()) {
                        extAddenda = DTrnDfrUtils.extractExtAddenda(msDocXmlAddenda, mnFkXmlAddendaTypeId);
                        if (extAddenda != null) {
                            cfd.ver4.DElementAddenda addenda = null;

                            if (comprobante40.getEltOpcAddenda() != null) {
                                addenda = comprobante40.getEltOpcAddenda();
                            }
                            else {
                                addenda = new cfd.ver4.DElementAddenda();
                                comprobante40.setEltOpcAddenda(addenda);
                            }

                            for (DElement element : addenda.getElements()) {
                                if (element instanceof DElementAddendaContinentalTire) {
                                    addenda.getElements().remove(element);
                                    break;
                                }
                            }

                            addenda.getElements().add(extAddenda);
                        }
                    }

                    msDocXml = DCfdConsts.XML_HEADER + comprobante40.getElementForXml();
                    break;

                default:
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
    }
    
    /**
     * Rewrite XML file of DFR when it must be regenerated, so save new XML file.
     * @param session GUI user session.
     * @throws Exception 
     */
    private void rewriteXml(final DGuiSession session) throws Exception {
        if (mbAuxDfrJustIssued || mbAuxAddendaJustSet) {
            // XML must be regenerated, so save new XML file:
            
            DDbConfigBranch configBranch = null;
            
            if (mnFkDpsId_n != 0) {
                DDbDps dps = (DDbDps) session.readRegistry(DModConsts.T_DPS, new int[] { mnFkDpsId_n });
                configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, dps.getCompanyBranchKey());
            }
            else if (mnFkBolId_n != 0) {
                DDbBol bol = (DDbBol) session.readRegistry(DModConsts.L_BOL, new int[] { mnFkBolId_n });
                configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, bol.getCompanyBranchKey());
            }
            else {
                configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, getCompanyBranchKey());
            }
            
            DXmlUtils.writeXml(mbAuxAddendaJustSet ? msDocXml : getSuitableDocXml(), configBranch.getDfrDirectory() + msDocXmlName);
        }
    }
    
    /**
     * Load DFR Mate data.
     * @throws Exception 
     */
    private void loadXtaDfrMate() throws Exception {
        Node node;
        NamedNodeMap namedNodeMap;
        Document doc;

        switch (mnFkXmlTypeId) {
            case DModSysConsts.TS_XML_TP_CFD:
            case DModSysConsts.TS_XML_TP_CFDI_32:
                break;

            case DModSysConsts.TS_XML_TP_CFDI_33:
                moXtaDfrMate = new DDfrMate();

                doc = DXmlUtils.parseDocument(getSuitableDocXml());

                // comprobante:

                node = DXmlUtils.extractElements(doc, "cfdi:Comprobante").item(0);
                namedNodeMap = node.getAttributes();

                moXtaDfrMate.setCfdType(DXmlUtils.extractAttributeValue(namedNodeMap, "TipoDeComprobante", true));
                moXtaDfrMate.setVersion(DXmlUtils.extractAttributeValue(namedNodeMap, "Version", true));
                moXtaDfrMate.setPlaceOfIssue(DXmlUtils.extractAttributeValue(namedNodeMap, "LugarExpedicion", true));
                moXtaDfrMate.setMethodOfPayment(DXmlUtils.extractAttributeValue(namedNodeMap, "MetodoPago", false));
                moXtaDfrMate.setPaymentTerms(DXmlUtils.extractAttributeValue(namedNodeMap, "CondicionesDePago", false));
                moXtaDfrMate.setConfirmation(DXmlUtils.extractAttributeValue(namedNodeMap, "Confirmacion", false));

                // emisor:

                node = DXmlUtils.extractElements(doc, "cfdi:Emisor").item(0);
                namedNodeMap = node.getAttributes();

                moXtaDfrMate.setIssuerTaxRegime(DXmlUtils.extractAttributeValue(namedNodeMap, "RegimenFiscal", true));

                // receptor:

                node = DXmlUtils.extractElements(doc, "cfdi:Receptor").item(0);
                namedNodeMap = node.getAttributes();

                //moXtaDfrMate.setReceiverTaxRegime(..); // not supported in CFDI 3.3
                //moXtaDfrMate.setReceiverFiscalAddress(..); // not supported in CFDI 3.3
                moXtaDfrMate.setCfdUsage(DXmlUtils.extractAttributeValue(namedNodeMap, "UsoCFDI", true));
                
                // información global (not supported in CFDI 3.3):

                //dfr.setGlobalPeriodicity(...);
                //dfr.setGlobalMonths(...);
                //dfr.setGlobalYear(...);
                
                // CFDI relacionados:

                if (DXmlUtils.hasChildElement(node, "cfdi:CfdiRelacionados")) {
                    node = DXmlUtils.extractChildElements(node, "cfdi:CfdiRelacionados").get(0); // CFDI 3.3 has only one child, if any
                    namedNodeMap = node.getAttributes();

                    String relationCode = DXmlUtils.extractAttributeValue(namedNodeMap, "TipoRelacion", true);
                    String plainUuids = "";

                    for (Node child : DXmlUtils.extractChildElements(node, "cfdi:CfdiRelacionado")) {
                        namedNodeMap = child.getAttributes();
                        plainUuids += (plainUuids.isEmpty() ? "" : ", ") + DXmlUtils.extractAttributeValue(namedNodeMap, "UUID", false);
                    }

                    DDfrMateRelations relations = new DDfrMateRelations();
                    DDfrMateRelations.RelatedCfd relatedCfd = relations.new RelatedCfd(relationCode, plainUuids);
                    relations.getRelatedCfds().add(relatedCfd);
                    moXtaDfrMate.setRelations(relations);
                }
                break;

            case DModSysConsts.TS_XML_TP_CFDI_40:
                moXtaDfrMate = new DDfrMate();

                doc = DXmlUtils.parseDocument(getSuitableDocXml());

                // comprobante:

                node = DXmlUtils.extractElements(doc, "cfdi:Comprobante").item(0);
                namedNodeMap = node.getAttributes();

                moXtaDfrMate.setCfdType(DXmlUtils.extractAttributeValue(namedNodeMap, "TipoDeComprobante", true));
                moXtaDfrMate.setVersion(DXmlUtils.extractAttributeValue(namedNodeMap, "Version", true));
                moXtaDfrMate.setPlaceOfIssue(DXmlUtils.extractAttributeValue(namedNodeMap, "LugarExpedicion", true));
                moXtaDfrMate.setMethodOfPayment(DXmlUtils.extractAttributeValue(namedNodeMap, "MetodoPago", false));
                moXtaDfrMate.setPaymentTerms(DXmlUtils.extractAttributeValue(namedNodeMap, "CondicionesDePago", false));
                moXtaDfrMate.setConfirmation(DXmlUtils.extractAttributeValue(namedNodeMap, "Confirmacion", false));

                // emisor:

                node = DXmlUtils.extractElements(doc, "cfdi:Emisor").item(0);
                namedNodeMap = node.getAttributes();

                moXtaDfrMate.setIssuerTaxRegime(DXmlUtils.extractAttributeValue(namedNodeMap, "RegimenFiscal", true));

                // receptor:

                node = DXmlUtils.extractElements(doc, "cfdi:Receptor").item(0);
                namedNodeMap = node.getAttributes();

                moXtaDfrMate.setReceiverFiscalAddress(DXmlUtils.extractAttributeValue(namedNodeMap, "DomicilioFiscalReceptor", true));
                moXtaDfrMate.setReceiverTaxRegime(DXmlUtils.extractAttributeValue(namedNodeMap, "RegimenFiscalReceptor", true));
                moXtaDfrMate.setCfdUsage(DXmlUtils.extractAttributeValue(namedNodeMap, "UsoCFDI", true));

                // información global:

                if (DXmlUtils.hasChildElement(node, "cfdi:InformacionGlobal")) {
                    node = DXmlUtils.extractChildElements(node, "cfdi:InformacionGlobal").get(0);
                    namedNodeMap = node.getAttributes();

                    moXtaDfrMate.setGlobalPeriodicity(DXmlUtils.extractAttributeValue(namedNodeMap, "Periodicidad", true));
                    moXtaDfrMate.setGlobalMonths(DXmlUtils.extractAttributeValue(namedNodeMap, "Meses", true));
                    moXtaDfrMate.setGlobalYear(DLibUtils.parseInt(DXmlUtils.extractAttributeValue(namedNodeMap, "Año", true)));
                }

                // CFDI relacionados:

                if (DXmlUtils.hasChildElement(node, "cfdi:CfdiRelacionados")) {
                    Vector<Node> relaciones = DXmlUtils.extractChildElements(node, "cfdi:CfdiRelacionados");
                    DDfrMateRelations relations = new DDfrMateRelations();

                    for (Node relacion : relaciones) {
                        namedNodeMap = relacion.getAttributes();

                        String relationCode = DXmlUtils.extractAttributeValue(namedNodeMap, "TipoRelacion", true);
                        String plainUuids = "";

                        for (Node child : DXmlUtils.extractChildElements(relacion, "cfdi:CfdiRelacionado")) {
                            namedNodeMap = child.getAttributes();
                            plainUuids += (plainUuids.isEmpty() ? "" : ", ") + DXmlUtils.extractAttributeValue(namedNodeMap, "UUID", false);
                        }

                        DDfrMateRelations.RelatedCfd relatedCfd = relations.new RelatedCfd(relationCode, plainUuids);
                        relations.getRelatedCfds().add(relatedCfd);
                    }

                    moXtaDfrMate.setRelations(relations);
                }
                break;

            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nTipo de XML.");
        }
    }
    
    /*
     * Public methods:
     */

    /**
     * Get next number.
     * @param session User session.
     * @param series Series.
     * @return Next number.
     * @throws Exception 
     */
    public static int getNextNumber(final DGuiSession session, final String series) throws Exception {
        int number = 0;
        
        // do not exclude deleted registries because CFDI with series cannot be deleted, only annulled:
        String sql = "SELECT COALESCE(MAX(num), 0) + 1 "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.T_DFR) + " "
                + "WHERE ser = '" + series + "';";
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            number = resultSet.getInt(1);
        }
        
        return number;
    }

    private void freeDpsLock(final DGuiSession session, final int freedLockStatus) throws Exception {
        if (moAuxDpsLock != null) {
            DLockUtils.freeLock(session, moAuxDpsLock, freedLockStatus);
            moAuxDpsLock = null;
        }
    }

    public void setPkDfrId(int n) { mnPkDfrId = n; }
    public void setSeries(String s) { msSeries = s; }
    public void setNumber(int n) { mnNumber = n; }
    public void setCertificateNumber(String s) { msCertificateNumber = s; }
    public void setSignedText(String s) { msSignedText = s; }
    public void setSignature(String s) { msSignature = s; }
    public void setUuid(String s) { msUuid = s; }
    public void setDocTs(Date t) { mtDocTs = t; }
    public void setDocXml(String s) { msDocXml = s; }
    public void setDocXmlRaw(String s) { msDocXmlRaw = s; }
    public void setDocXmlAddenda(String s) { msDocXmlAddenda = s; }
    public void setDocXmlName(String s) { msDocXmlName = s; }
    public void setCancelStatus(String s) { msCancelStatus = s; }
    public void setCancelXml(String s) { msCancelXml = s; }
    public void setCancelPdf_n(java.sql.Blob o) { moCancelPdf_n = o; }
    public void setBookkept(boolean b) { mbBookkept = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkXmlTypeId(int n) { mnFkXmlTypeId = n; }
    public void setFkXmlSubtypeId(int n) { mnFkXmlSubtypeId = n; }
    public void setFkXmlSubtypeVersionId(int n) { mnFkXmlSubtypeVersionId = n; }
    public void setFkXmlStatusId(int n) { mnFkXmlStatusId = n; }
    public void setFkXmlAddendaTypeId(int n) { mnFkXmlAddendaTypeId = n; }
    public void setFkXmlSignatureProviderId(int n) { mnFkXmlSignatureProviderId = n; }
    public void setFkCertificateId(int n) { mnFkCertificateId = n; }
    public void setFkOwnerBizPartnerId(int n) { mnFkOwnerBizPartnerId = n; }
    public void setFkOwnerBranchId(int n) { mnFkOwnerBranchId = n; }
    public void setFkBizPartnerId(int n) { mnFkBizPartnerId = n; }
    public void setFkDpsId_n(int n) { mnFkDpsId_n = n; }
    public void setFkBolId_n(int n) { mnFkBolId_n = n; }
    public void setFkCashBizPartnerId_n(int n) { mnFkCashBizPartnerId_n = n; }
    public void setFkCashBranchId_n(int n) { mnFkCashBranchId_n = n; }
    public void setFkCashCashId_n(int n) { mnFkCashCashId_n = n; }
    public void setFkBookkeepingYearId_n(int n) { mnFkBookkeepingYearId_n = n; }
    public void setFkBookkeepingNumberId_n(int n) { mnFkBookkeepingNumberId_n = n; }
    public void setFkUserIssuedId(int n) { mnFkUserIssuedId = n; }
    public void setFkUserAnnulledId(int n) { mnFkUserAnnulledId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserIssued(Date t) { mtTsUserIssued = t; }
    public void setTsUserAnnulled(Date t) { mtTsUserAnnulled = t; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkDfrId() { return mnPkDfrId; }
    public String getSeries() { return msSeries; }
    public int getNumber() { return mnNumber; }
    public String getCertificateNumber() { return msCertificateNumber; }
    public String getSignedText() { return msSignedText; }
    public String getSignature() { return msSignature; }
    public String getUuid() { return msUuid; }
    public Date getDocTs() { return mtDocTs; }
    public String getDocXml() { return msDocXml; }
    public String getDocXmlRaw() { return msDocXmlRaw; }
    public String getDocXmlAddenda() { return msDocXmlAddenda; }
    public String getDocXmlName() { return msDocXmlName; }
    public String getCancelStatus() { return msCancelStatus; }
    public String getCancelXml() { return msCancelXml; }
    public java.sql.Blob getCancelPdf_n() { return moCancelPdf_n; }
    public boolean isBookkept() { return mbBookkept; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkXmlTypeId() { return mnFkXmlTypeId; }
    public int getFkXmlSubtypeId() { return mnFkXmlSubtypeId; }
    public int getFkXmlSubtypeVersionId() { return mnFkXmlSubtypeVersionId; }
    public int getFkXmlStatusId() { return mnFkXmlStatusId; }
    public int getFkXmlAddendaTypeId() { return mnFkXmlAddendaTypeId; }
    public int getFkXmlSignatureProviderId() { return mnFkXmlSignatureProviderId; }
    public int getFkCertificateId() { return mnFkCertificateId; }
    public int getFkOwnerBizPartnerId() { return mnFkOwnerBizPartnerId; }
    public int getFkOwnerBranchId() { return mnFkOwnerBranchId; }
    public int getFkBizPartnerId() { return mnFkBizPartnerId; }
    public int getFkDpsId_n() { return mnFkDpsId_n; }
    public int getFkBolId_n() { return mnFkBolId_n; }
    public int getFkCashBizPartnerId_n() { return mnFkCashBizPartnerId_n; }
    public int getFkCashBranchId_n() { return mnFkCashBranchId_n; }
    public int getFkCashCashId_n() { return mnFkCashCashId_n; }
    public int getFkBookkeepingYearId_n() { return mnFkBookkeepingYearId_n; }
    public int getFkBookkeepingNumberId_n() { return mnFkBookkeepingNumberId_n; }
    public int getFkUserIssuedId() { return mnFkUserIssuedId; }
    public int getFkUserAnnulledId() { return mnFkUserAnnulledId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserIssued() { return mtTsUserIssued; }
    public Date getTsUserAnnulled() { return mtTsUserAnnulled; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setXtaDfrMate(DDfrMate o) { moXtaDfrMate = o; }
    
    public void setDfrPagos10(cfd.ver33.crp10.DElementPagos o) { moDfrPagos10 = o; }
    public void setDfrPagos20(cfd.ver40.crp20.DElementPagos o) { moDfrPagos20 = o; if (moDfrPagos20 != null) moDfrPagos20.computePagos(); }
    
    public DDfrMate getXtaDfrMate() { return moXtaDfrMate; }
    
    public cfd.ver33.crp10.DElementPagos getDfrPagos10() { return moDfrPagos10; }
    public cfd.ver40.crp20.DElementPagos getDfrPagos20() { return moDfrPagos20; }
    
    public void setAuxDfrJustIssued(boolean b) { mbAuxDfrJustIssued = b; }
    public void setAuxDfrJustAnnulled(boolean b) { mbAuxDfrJustAnnulled = b; }
    public void setAuxAddendaJustSet(boolean b) { mbAuxAddendaJustSet = b; }
    public void setAuxXmlSignatureRequestKey(int[] key) { manAuxXmlSignatureRequestKey = key; }
    public void setAuxLock(DDbLock o) { moAuxLock = o; }
    public void setAuxDpsLock(DDbLock o) { moAuxDpsLock = o; }

    public boolean isAuxJustIssued() { return mbAuxDfrJustIssued; }
    public boolean isAuxJustAnnulled() { return mbAuxDfrJustAnnulled; }
    public boolean isAuxAddendaJustSet() { return mbAuxAddendaJustSet; }
    public int[] getAuxXmlSignatureRequestKey() { return manAuxXmlSignatureRequestKey; }
    public DDbLock getAuxLock() { return moAuxLock; }
    public DDbLock getAuxDpsLock() { return moAuxDpsLock; }
    
    public int[] getCompanyKey() { return new int[] { mnFkOwnerBizPartnerId }; }
    public int[] getCompanyBranchKey() { return new int[] { mnFkOwnerBizPartnerId, mnFkOwnerBranchId }; }
    public int[] getBizPartnerKey() { return new int[] { mnFkBizPartnerId }; }
    public int[] getBranchCashKey_n() { return !(mnFkCashBizPartnerId_n != DLibConsts.UNDEFINED && mnFkCashBranchId_n != DLibConsts.UNDEFINED && mnFkCashCashId_n != DLibConsts.UNDEFINED) ? null : new int[] { mnFkCashBizPartnerId_n, mnFkCashBranchId_n, mnFkCashCashId_n }; }
    public int[] getBookkeepingNumberKey_n() { return !(mnFkBookkeepingYearId_n != DLibConsts.UNDEFINED && mnFkBookkeepingNumberId_n != DLibConsts.UNDEFINED) ? null : new int[] { mnFkBookkeepingYearId_n, mnFkBookkeepingNumberId_n }; }
    public String getDfrNumber() { return DTrnUtils.composeDpsNumber(msSeries, mnNumber); }
    public boolean isDfrCfdi() { return DLibUtils.belongsTo(mnFkXmlTypeId, new int[] { DModSysConsts.TS_XML_TP_CFDI_32, DModSysConsts.TS_XML_TP_CFDI_33, DModSysConsts.TS_XML_TP_CFDI_40 }); }
    /**
     * Get XML raw (just as fetched from web service) if available and its status is at least 'issued', otherwise own generated XML.
     * @return Suitable XML.
     */
    public String getSuitableDocXml() {
        return mnFkXmlStatusId >= DModSysConsts.TS_XML_ST_ISS && !msDocXmlRaw.isEmpty() ? msDocXmlRaw : msDocXml;
    }
    
    @SuppressWarnings("deprecation")
    public cfd.ver33.DElementComprobante getElementComprobante33() throws Exception {
        String xml = getSuitableDocXml();
        return xml.isEmpty() ? null : DCfdUtils.getCfdi33(xml);
    }
    
    public cfd.ver40.DElementComprobante getElementComprobante40() throws Exception {
        String xml = getSuitableDocXml();
        return xml.isEmpty() ? null : DCfdUtils.getCfdi40(xml);
    }
    
    public double getDfrTotal(DGuiSession session) throws Exception {
        double total = 0;
        
        switch (mnFkXmlSubtypeId) {
            case DModSysConsts.TS_XML_STP_FAC:
                total = ((DDbDps) session.readRegistry(DModConsts.T_DPS, new int[] { mnFkDpsId_n })).getTotalCy_r();
                break;
            case DModSysConsts.TS_XML_STP_CRP:
            case DModSysConsts.TS_XML_STP_CCP:
                break;
            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        return total;
    }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDfrId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDfrId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkDfrId = 0;
        msSeries = "";
        mnNumber = 0;
        msCertificateNumber = "";
        msSignedText = "";
        msSignature = "";
        msUuid = "";
        mtDocTs = null;
        msDocXml = "";
        msDocXmlRaw = "";
        msDocXmlAddenda = "";
        msDocXmlName = "";
        msCancelStatus = "";
        msCancelXml = "";
        moCancelPdf_n = null;
        mbBookkept = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkXmlTypeId = 0;
        mnFkXmlSubtypeId = 0;
        mnFkXmlSubtypeVersionId = 0;
        mnFkXmlStatusId = 0;
        mnFkXmlAddendaTypeId = 0;
        mnFkXmlSignatureProviderId = 0;
        mnFkCertificateId = 0;
        mnFkOwnerBizPartnerId = 0;
        mnFkOwnerBranchId = 0;
        mnFkBizPartnerId = 0;
        mnFkDpsId_n = 0;
        mnFkBolId_n = 0;
        mnFkCashBizPartnerId_n = 0;
        mnFkCashBranchId_n = 0;
        mnFkCashCashId_n = 0;
        mnFkBookkeepingYearId_n = 0;
        mnFkBookkeepingNumberId_n = 0;
        mnFkUserIssuedId = 0;
        mnFkUserAnnulledId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserIssued = null;
        mtTsUserAnnulled = null;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        moXtaDfrMate = null;
        
        moDfrPagos10 = null;
        moDfrPagos20 = null;
        
        mbAuxDfrJustIssued = false;
        mbAuxDfrJustAnnulled = false;
        mbAuxAddendaJustSet = false;
        manAuxXmlSignatureRequestKey = null;
        moAuxLock = null;
        moAuxDpsLock = null;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_dfr = " + mnPkDfrId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_dfr = " + pk[0] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkDfrId = 0;

        msSql = "SELECT COALESCE(MAX(id_dfr), 0) + 1 FROM " + getSqlTable();
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkDfrId = resultSet.getInt(1);
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
            mnPkDfrId = resultSet.getInt("id_dfr");
            msSeries = resultSet.getString("ser");
            mnNumber = resultSet.getInt("num");
            msCertificateNumber = resultSet.getString("cer_num");
            msSignedText = resultSet.getString("sig_txt");
            msSignature = resultSet.getString("sig");
            msUuid = resultSet.getString("uid");
            mtDocTs = resultSet.getTimestamp("doc_ts");
            msDocXml = resultSet.getString("doc_xml");
            msDocXmlRaw = resultSet.getString("doc_xml_raw");
            msDocXmlAddenda = resultSet.getString("doc_xml_add");
            msDocXmlName = resultSet.getString("doc_xml_name");
            msCancelStatus = resultSet.getString("can_st");
            msCancelXml = resultSet.getString("can_xml");
            moCancelPdf_n = resultSet.getBlob("can_pdf_n");
            mbBookkept = resultSet.getBoolean("b_bkk");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkXmlTypeId = resultSet.getInt("fk_xml_tp");
            mnFkXmlSubtypeId = resultSet.getInt("fk_xml_stp");
            mnFkXmlSubtypeVersionId = resultSet.getInt("fk_xml_stp_ver");
            mnFkXmlStatusId = resultSet.getInt("fk_xml_st");
            mnFkXmlAddendaTypeId = resultSet.getInt("fk_xml_add_tp");
            mnFkXmlSignatureProviderId = resultSet.getInt("fk_xsp");
            mnFkCertificateId = resultSet.getInt("fk_cer");
            mnFkOwnerBizPartnerId = resultSet.getInt("fk_own_bpr");
            mnFkOwnerBranchId = resultSet.getInt("fk_own_bra");
            mnFkBizPartnerId = resultSet.getInt("fk_bpr");
            mnFkDpsId_n = resultSet.getInt("fk_dps_n");
            mnFkBolId_n = resultSet.getInt("fk_bol_n");
            mnFkCashBizPartnerId_n = resultSet.getInt("fk_csh_bpr_n");
            mnFkCashBranchId_n = resultSet.getInt("fk_csh_bra_n");
            mnFkCashCashId_n = resultSet.getInt("fk_csh_csh_n");
            mnFkBookkeepingYearId_n = resultSet.getInt("fk_bkk_yer_n");
            mnFkBookkeepingNumberId_n = resultSet.getInt("fk_bkk_num_n");
            mnFkUserIssuedId = resultSet.getInt("fk_usr_iss");
            mnFkUserAnnulledId = resultSet.getInt("fk_usr_ann");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserIssued = resultSet.getTimestamp("ts_usr_iss");
            mtTsUserAnnulled = resultSet.getTimestamp("ts_usr_ann");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");
            
            mbRegistryNew = false;
        }
        
        loadXtaDfrMate();

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        if (isCrp() && !mbAuxDfrJustIssued && !mbAuxDfrJustAnnulled) {
            prepareDfrForCrp(session);
        }
        
        if (mbAuxAddendaJustSet) {
            embeedAddenda(session);
        }
        
        // is issued?:
        if (mbAuxDfrJustIssued) {
            mnFkUserIssuedId = session.getUser().getPkUserId();
        }
        else if (mnFkUserIssuedId == DLibConsts.UNDEFINED) {
            mnFkUserIssuedId = DUtilConsts.USR_NA_ID;
        }

        // is annulled?:
        if (mbAuxDfrJustAnnulled) {
            mnFkUserAnnulledId = session.getUser().getPkUserId();
        }
        else if (mnFkUserAnnulledId == DLibConsts.UNDEFINED) {
            mnFkUserAnnulledId = DUtilConsts.USR_NA_ID;
        }
        
        if (mbRegistryNew) {
            computePrimaryKey(session);
            
            mbDeleted = false;
            //mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;
            
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkDfrId + ", " + 
                    "'" + msSeries + "', " + 
                    mnNumber + ", " + 
                    "'" + msCertificateNumber + "', " + 
                    "'" + msSignedText + "', " + 
                    "'" + msSignature + "', " + 
                    "'" + msUuid + "', " + 
                    "'" + DLibUtils.DbmsDateFormatDatetime.format(mtDocTs) + "', " +
                    "'" + msDocXml.replaceAll("'", "''") + "', " +
                    "'" + msDocXmlRaw.replaceAll("'", "''") + "', " + 
                    "'" + msDocXmlAddenda.replaceAll("'", "''") + "', " + 
                    "'" + msDocXmlName + "', " +
                    "'" + msCancelStatus + "', " + 
                    "'" + msCancelXml.replaceAll("'", "''") + "', " + 
                    "NULL, " + 
                    (mbBookkept ? 1 : 0) + ", " + 
                    (mbDeleted ? 1 : 0) + ", " + 
                    (mbSystem ? 1 : 0) + ", " + 
                    mnFkXmlTypeId + ", " + 
                    mnFkXmlSubtypeId + ", " + 
                    mnFkXmlSubtypeVersionId + ", " + 
                    mnFkXmlStatusId + ", " + 
                    mnFkXmlAddendaTypeId + ", " + 
                    mnFkXmlSignatureProviderId + ", " + 
                    mnFkCertificateId + ", " + 
                    mnFkOwnerBizPartnerId + ", " + 
                    mnFkOwnerBranchId + ", " + 
                    mnFkBizPartnerId + ", " + 
                    (mnFkDpsId_n == 0 ? "NULL" : "" + mnFkDpsId_n) + ", " +
                    (mnFkBolId_n == 0 ? "NULL" : "" + mnFkBolId_n) + ", " +
                    (mnFkCashBizPartnerId_n == 0 ? "NULL" : "" + mnFkCashBizPartnerId_n) + ", " + 
                    (mnFkCashBranchId_n == 0 ? "NULL" : "" + mnFkCashBranchId_n) + ", " + 
                    (mnFkCashCashId_n == 0 ? "NULL" : "" + mnFkCashCashId_n) + ", " + 
                    (mnFkBookkeepingYearId_n == 0 ? "NULL" : "" + mnFkBookkeepingYearId_n) + ", " + 
                    (mnFkBookkeepingNumberId_n == 0 ? "NULL" : "" + mnFkBookkeepingNumberId_n) + ", " + 
                    mnFkUserIssuedId + ", " + 
                    mnFkUserAnnulledId + ", " + 
                    mnFkUserInsertId + ", " + 
                    mnFkUserUpdateId + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + ", " + 
                    "NOW()" + " " + 
                    ")";
        }
        else {
            assureLock(session);
            
            mnFkUserUpdateId = session.getUser().getPkUserId();
            
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_dfr = " + mnPkDfrId + ", " +
                    "ser = '" + msSeries + "', " +
                    "num = " + mnNumber + ", " +
                    "cer_num = '" + msCertificateNumber + "', " +
                    "sig_txt = '" + msSignedText + "', " +
                    "sig = '" + msSignature + "', " +
                    "uid = '" + msUuid + "', " +
                    "doc_ts = '" + DLibUtils.DbmsDateFormatDatetime.format(mtDocTs) + "', " +
                    "doc_xml = '" + msDocXml.replaceAll("'", "''") + "', " +
                    "doc_xml_raw = '" + msDocXmlRaw.replaceAll("'", "''") + "', " +
                    "doc_xml_add = '" + msDocXmlAddenda.replaceAll("'", "''") + "', " +
                    "doc_xml_name = '" + msDocXmlName + "', " +
                    "can_st = '" + msCancelStatus + "', " +
                    "can_xml = '" + msCancelXml.replaceAll("'", "''") + "', " +
                    //"can_pdf_n = " + moCancelPdf_n + ", " +
                    "b_bkk = " + (mbBookkept ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_xml_tp = " + mnFkXmlTypeId + ", " +
                    "fk_xml_stp = " + mnFkXmlSubtypeId + ", " +
                    "fk_xml_stp_ver = " + mnFkXmlSubtypeVersionId + ", " +
                    "fk_xml_st = " + mnFkXmlStatusId + ", " +
                    "fk_xml_add_tp = " + mnFkXmlAddendaTypeId + ", " +
                    "fk_xsp = " + mnFkXmlSignatureProviderId + ", " +
                    "fk_cer = " + mnFkCertificateId + ", " +
                    "fk_own_bpr = " + mnFkOwnerBizPartnerId + ", " +
                    "fk_own_bra = " + mnFkOwnerBranchId + ", " +
                    "fk_bpr = " + mnFkBizPartnerId + ", " +
                    "fk_dps_n = " + (mnFkDpsId_n == 0 ? "NULL" : "" + mnFkDpsId_n) + ", " +
                    "fk_bol_n = " + (mnFkBolId_n == 0 ? "NULL" : "" + mnFkBolId_n) + ", " +
                    "fk_csh_bpr_n = " + (mnFkCashBizPartnerId_n == 0 ? "NULL" : "" + mnFkCashBizPartnerId_n) + ", " +
                    "fk_csh_bra_n = " + (mnFkCashBranchId_n == 0 ? "NULL" : "" + mnFkCashBranchId_n) + ", " +
                    "fk_csh_csh_n = " + (mnFkCashCashId_n == 0 ? "NULL" : "" + mnFkCashCashId_n) + ", " +
                    "fk_bkk_yer_n = " + (mnFkBookkeepingYearId_n == 0 ? "NULL" : "" + mnFkBookkeepingYearId_n) + ", " +
                    "fk_bkk_num_n = " + (mnFkBookkeepingNumberId_n == 0 ? "NULL" : "" + mnFkBookkeepingNumberId_n) + ", " +
                    (!mbAuxDfrJustIssued ? "" : "fk_usr_iss = " + mnFkUserIssuedId + ", ") +
                    (!mbAuxDfrJustAnnulled ? "" : "fk_usr_ann = " + mnFkUserAnnulledId + ", ") +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + "" +
                    (!mbAuxDfrJustIssued ? "" : ", ts_usr_iss = " + "NOW()") +
                    (!mbAuxDfrJustAnnulled ? "" : ", ts_usr_ann = " + "NOW()") +
                    //", ts_usr_ins = '" + DLibUtils.DbmsDateFormatDatetime.format(mtTsUserInsert) + "'" +
                    ", ts_usr_upd = NOW()" +
                    " " +   // NOTE: trailing space!
                    getSqlWhere();
        }
        
        session.getStatement().execute(msSql);
        
        // Additional processing:
        
        if (mbAuxDfrJustIssued || mbAuxAddendaJustSet) {
            rewriteXml(session);
        }
        
        if (isCrp() && !mbAuxDfrJustIssued) {
            computeBookkeepingForCrp(session);
        }
        
        // Finish registry updating:
        
        freeLock(session, DDbLock.LOCK_ST_FREED_UPDATE);
        freeDpsLock(session, DDbLock.LOCK_ST_FREED_UPDATE);
        
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbDfr clone() throws CloneNotSupportedException {
        DDbDfr registry = new DDbDfr();

        registry.setPkDfrId(this.getPkDfrId());
        registry.setSeries(this.getSeries());
        registry.setNumber(this.getNumber());
        registry.setCertificateNumber(this.getCertificateNumber());
        registry.setSignedText(this.getSignedText());
        registry.setSignature(this.getSignature());
        registry.setUuid(this.getUuid());
        registry.setDocTs(this.getDocTs());
        registry.setDocXml(this.getDocXml());
        registry.setDocXmlRaw(this.getDocXmlRaw());
        registry.setDocXmlAddenda(this.getDocXmlAddenda());
        registry.setDocXmlName(this.getDocXmlName());
        registry.setCancelStatus(this.getCancelStatus());
        registry.setCancelXml(this.getCancelXml());
        registry.setCancelPdf_n(this.getCancelPdf_n());
        registry.setBookkept(this.isBookkept());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkXmlTypeId(this.getFkXmlTypeId());
        registry.setFkXmlSubtypeId(this.getFkXmlSubtypeId());
        registry.setFkXmlSubtypeVersionId(this.getFkXmlSubtypeVersionId());
        registry.setFkXmlStatusId(this.getFkXmlStatusId());
        registry.setFkXmlAddendaTypeId(this.getFkXmlAddendaTypeId());
        registry.setFkXmlSignatureProviderId(this.getFkXmlSignatureProviderId());
        registry.setFkCertificateId(this.getFkCertificateId());
        registry.setFkOwnerBizPartnerId(this.getFkOwnerBizPartnerId());
        registry.setFkOwnerBranchId(this.getFkOwnerBranchId());
        registry.setFkBizPartnerId(this.getFkBizPartnerId());
        registry.setFkDpsId_n(this.getFkDpsId_n());
        registry.setFkBolId_n(this.getFkBolId_n());
        registry.setFkCashBizPartnerId_n(this.getFkCashBizPartnerId_n());
        registry.setFkCashBranchId_n(this.getFkCashBranchId_n());
        registry.setFkCashCashId_n(this.getFkCashCashId_n());
        registry.setFkBookkeepingYearId_n(this.getFkBookkeepingYearId_n());
        registry.setFkBookkeepingNumberId_n(this.getFkBookkeepingNumberId_n());
        registry.setFkUserIssuedId(this.getFkUserIssuedId());
        registry.setFkUserAnnulledId(this.getFkUserAnnulledId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserIssued(this.getTsUserIssued());
        registry.setTsUserAnnulled(this.getTsUserAnnulled());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());
        
        registry.setXtaDfrMate(this.getXtaDfrMate() == null ? null : this.getXtaDfrMate().clone());
        
        registry.setDfrPagos10(this.getDfrPagos10() == null ? null : this.getDfrPagos10()); // not cloned!, because clone is not supported for this class!
        registry.setDfrPagos20(this.getDfrPagos20() == null ? null : this.getDfrPagos20()); // not cloned!, because clone is not supported for this class!

        registry.setAuxDfrJustIssued(this.isAuxJustIssued());
        registry.setAuxDfrJustAnnulled(this.isAuxJustAnnulled());
        registry.setAuxAddendaJustSet(this.isAuxAddendaJustSet());
        registry.setAuxXmlSignatureRequestKey(this.getAuxXmlSignatureRequestKey() == null ? null : new int[] { this.getAuxXmlSignatureRequestKey()[0], this.getAuxXmlSignatureRequestKey()[1] });
        registry.setAuxLock(this.getAuxLock()); // locks cannot be clonned!
        registry.setAuxDpsLock(this.getAuxDpsLock()); // locks cannot be clonned!

        registry.setRegistryNew(this.isRegistryNew());
        
        // XXX Improve this:
        registry.setQueryResultId(this.getQueryResultId());
        registry.setQueryResult(this.getQueryResult());
        registry.setSql(this.getSql());

        return registry;
    }
    
    @Override
    public void saveField(final Statement statement, final int[] pk, final int field, final Object value) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        msSql = "UPDATE " + getSqlTable() + " SET ";

        switch (field) {
            case FIELD_CAN_ST:
                msSql += "can_st = '" + (String) value + "' ";
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
        boolean can = super.canSave(session);

        if (can) {
            can = checkAvailability(session);
        }

        return can;
    }

    @Override
    public boolean canDisable(final DGuiSession session) throws SQLException, Exception {
        boolean can = super.canDisable(session);
        
        if (can) {
            if (mnFkXmlStatusId == DModSysConsts.TS_XML_ST_ANN) {
                msQueryResult = DDbConsts.MSG_REG_ + getDfrNumber() + DDbConsts.MSG_REG_IS_DISABLED + "\n" + DTrnConsts.ERR_MSG_NOT_PROCEED;
                can = false;
            }
        }

        if (can) {
            can = checkAvailability(session);
        }

        return can;
    }

    @Override
    public boolean canDelete(final DGuiSession session) throws SQLException, Exception {
        msQueryResult = DDbConsts.ERR_MSG_REG_NON_DELETABLE + "\n" + DTrnConsts.ERR_MSG_NOT_PROCEED;
        
        return false;
    }

    @Override
    public void disable(final DGuiSession session) throws SQLException, Exception {
        if (mnFkXmlStatusId == DModSysConsts.TS_XML_ST_ANN) {
            msQueryResult = DDbConsts.MSG_REG_ + getDfrNumber() + DDbConsts.MSG_REG_IS_DISABLED;
        }
        else {
            mnFkXmlStatusId = DModSysConsts.TS_XML_ST_ANN;
            mbAuxDfrJustAnnulled = true;
        }
        
        save(session);
        session.notifySuscriptors(mnRegistryType);
    }

    @Override
    public void delete(final DGuiSession session) throws SQLException, Exception {
        throw new Exception(DDbConsts.ERR_MSG_REG_NON_DELETABLE);
    }
    
    /**
     * Create CFDI 3.3 with Complemento de Recepción de Pagos 1.0.
     * @param session
     * @param signature
     * @return
     * @throws Exception 
     */
    @Deprecated
    private cfd.ver33.DElementComprobante createCfdi33(final DGuiSession session, final DGuiEdsSignature signature) throws Exception {
        // validate supported subtype of CFDI:
        switch (mnFkXmlSubtypeId) {
            case DModSysConsts.TS_XML_STP_CRP:
                break;
            case DModSysConsts.TS_XML_STP_NA:
            case DModSysConsts.TS_XML_STP_FAC:
            case DModSysConsts.TS_XML_STP_CCP:
                // falls through
            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        Date tDate = null;
        Date tUpdateTs = mtTsUserUpdate != null ? mtTsUserUpdate : new Date();
        int[] anDateDps = DLibTimeUtils.digestDate(mtDocTs);
        int[] anDateEdit = DLibTimeUtils.digestDate(tUpdateTs);
        GregorianCalendar oGregorianCalendar = null;
        DDbConfigBranch configBranch = null;
        DDbBizPartner bprEmisor = null;
        DDbBizPartner bprReceptor = null;
                
        // Check company branch emission configuration:
        
        configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, getCompanyBranchKey());

        // Emisor:

        if (configBranch.getFkBizPartnerDpsSignatureId_n() == DLibConsts.UNDEFINED) {
            // Document's emisor:
            bprEmisor = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, getCompanyKey());
        }
        else {
            // Company branch's emisor:
            bprEmisor = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, new int[] { configBranch.getFkBizPartnerDpsSignatureId_n() });
        }

        // Receptor:

        bprReceptor = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, getBizPartnerKey());

        if (DLibUtils.compareKeys(anDateDps, anDateEdit)) {
            tDate = tUpdateTs;
        }
        else {
            oGregorianCalendar = new GregorianCalendar();
            oGregorianCalendar.setTime(tUpdateTs);
            oGregorianCalendar.set(GregorianCalendar.YEAR, anDateDps[0]);
            oGregorianCalendar.set(GregorianCalendar.MONTH, anDateDps[1] - 1);  // January is month 0
            oGregorianCalendar.set(GregorianCalendar.DATE, anDateDps[2]);
            tDate = oGregorianCalendar.getTime();
        }
        
        // Create XML's main element 'Comprobante':

        cfd.ver33.DElementComprobante comprobante = new cfd.ver33.DElementComprobante(); // include Addenda1 only if explicitly defined and CFD is already signed

        comprobante.getAttSerie().setString(msSeries);
        comprobante.getAttFolio().setString("" + mnNumber);
        comprobante.getAttFecha().setDatetime(tDate);
        
        comprobante.getAttSello().setString("");
        comprobante.getAttNoCertificado().setString(signature.getCertificateNumber());
        comprobante.getAttCertificado().setString(signature.getCertificateBase64());

        //comprobante.getAttFormaPago().setString();
        //comprobante.getAttCondicionesDePago().setString();
        
        comprobante.getAttSubTotal().setDouble(0);
        comprobante.getAttSubTotal().setDecimals(0);

        comprobante.getAttMoneda().setString(DCfdi40Catalogs.ClaveMonedaXxx);
        //comprobante.getAttTipoCambio().setDouble();
        
        comprobante.getAttTotal().setDouble(0);
        comprobante.getAttTotal().setDecimals(0);

        comprobante.getAttTipoDeComprobante().setString(DCfdi40Catalogs.CFD_TP_P);
        //comprobante.getAttMetodoPago().setString();
        
        comprobante.getAttLugarExpedicion().setString(bprEmisor.getActualAddressFiscal());
        
        comprobante.getAttConfirmacion().setString(moXtaDfrMate.getConfirmation());
        
        // element 'CfdiRelacionados':
        if (moXtaDfrMate.getRelations() != null && !moXtaDfrMate.getRelations().getRelatedCfds().isEmpty()) {
            DDfrMateRelations.RelatedCfd relatedCfd = moXtaDfrMate.getRelations().getRelatedCfds().get(0);
            cfd.ver33.DElementCfdiRelacionados cfdiRelacionados = new cfd.ver33.DElementCfdiRelacionados();
            cfdiRelacionados.getAttTipoRelacion().setString(relatedCfd.RelationCode);
            
            for (String uuid : relatedCfd.Uuids) {
                cfd.ver33.DElementCfdiRelacionado cfdiRelacionado = new cfd.ver33.DElementCfdiRelacionado();
                cfdiRelacionado.getAttUuid().setString(uuid);
                cfdiRelacionados.getEltCfdiRelacionados().add(cfdiRelacionado);
            }
            
            comprobante.setEltOpcCfdiRelacionados(cfdiRelacionados);
        }

        // element 'Emisor':
        comprobante.getEltEmisor().getAttRfc().setString(bprEmisor.getFiscalId());
        comprobante.getEltEmisor().getAttNombre().setString(bprEmisor.getPrintableName());
        comprobante.getEltEmisor().getAttRegimenFiscal().setString(moXtaDfrMate.getIssuerTaxRegime());

        // element 'Receptor':
        comprobante.getEltReceptor().getAttRfc().setString(bprReceptor.getFiscalId());
        comprobante.getEltReceptor().getAttNombre().setString(bprReceptor.getPrintableName());
        //comprobante.getEltReceptor().getAttResidenciaFiscal().setString(""); // not supported yet!
        //comprobante.getEltReceptor().getAttNumRegIdTrib().setString(""); // not supported yet!
        comprobante.getEltReceptor().getAttUsoCFDI().setString(moXtaDfrMate.getCfdUsage());

        // element 'Conceptos':
        
        cfd.ver33.DElementConcepto concepto = new cfd.ver33.DElementConcepto();

        concepto.getAttClaveProdServ().setString(DCfdi40Catalogs.ClaveProdServServsFacturacion);
        //concepto.getAttNoIdentificacion().setString();
        concepto.getAttCantidad().setDouble(1);
        concepto.getAttCantidad().setDecimals(0);
        concepto.getAttClaveUnidad().setString(DCfdi40Catalogs.ClaveUnidadAct);
        //concepto.getAttUnidad().setString();
        concepto.getAttDescripcion().setString(DCfdi40Catalogs.ConceptoPago);
        concepto.getAttValorUnitario().setDouble(0);
        concepto.getAttValorUnitario().setDecimals(0);
        concepto.getAttImporte().setDouble(0);
        concepto.getAttImporte().setDecimals(0);
        //concepto.getAttDescuento().setDouble();

        comprobante.getEltConceptos().getEltConceptos().add(concepto);
        
        if (moDfrPagos10 != null) {
            cfd.ver33.DElementComplemento complemento = new cfd.ver33.DElementComplemento();
            complemento.getElements().add(moDfrPagos10);
            comprobante.setEltOpcComplemento(complemento);
        }

        return comprobante;
    }
    
    /**
     * Create CFDI 4.0 with Complemento de Recepción de Pagos 2.0.
     * @param session
     * @param signature
     * @return
     * @throws Exception 
     */
    private cfd.ver40.DElementComprobante createCfdi40(final DGuiSession session, final DGuiEdsSignature signature) throws Exception {
        // validate supported CFDI:
        
        if (mnFkXmlSubtypeId != DModSysConsts.TS_XML_STP_CRP) {
            throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Subtipo de XML.)");
        }
        else if (moDfrPagos20 == null) {
            throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN + " (Complemento de recepción de pagos 2.0.)");
        }
        
        Date tDate = null;
        Date tUpdateTs = mtTsUserUpdate != null ? mtTsUserUpdate : new Date();
        int[] anDateDps = DLibTimeUtils.digestDate(mtDocTs);
        int[] anDateEdit = DLibTimeUtils.digestDate(tUpdateTs);
        GregorianCalendar oGregorianCalendar = null;
        DDbConfigBranch configBranch = null;
        DDbBizPartner bprEmisor = null;
        DDbBranch braEmisor = null;
        DDbBizPartner bprReceptor = null;
                
        // Check company branch emission configuration:
        
        configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, getCompanyBranchKey());

        // Emisor:

        if (configBranch.getFkBizPartnerDpsSignatureId_n() == DLibConsts.UNDEFINED) {
            // Document's emisor:
            
            bprEmisor = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, getCompanyKey());
            braEmisor = (DDbBranch) session.readRegistry(DModConsts.BU_BRA, getCompanyBranchKey());
        }
        else {
            // Company branch's emisor:
            
            bprEmisor = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, new int[] { configBranch.getFkBizPartnerDpsSignatureId_n() });
            braEmisor = (DDbBranch) session.readRegistry(DModConsts.BU_BRA, new int[] { configBranch.getFkBizPartnerDpsSignatureId_n(), DUtilConsts.BPR_BRA_ID });
        }

        // Receptor:

        bprReceptor = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, getBizPartnerKey());

        if (DLibUtils.compareKeys(anDateDps, anDateEdit)) {
            tDate = tUpdateTs;
        }
        else {
            oGregorianCalendar = new GregorianCalendar();
            oGregorianCalendar.setTime(tUpdateTs);
            oGregorianCalendar.set(GregorianCalendar.YEAR, anDateDps[0]);
            oGregorianCalendar.set(GregorianCalendar.MONTH, anDateDps[1] - 1);  // January is month 0
            oGregorianCalendar.set(GregorianCalendar.DATE, anDateDps[2]);
            tDate = oGregorianCalendar.getTime();
        }
        
        // Create XML's main element 'Comprobante':

        cfd.ver40.DElementComprobante comprobante = new cfd.ver40.DElementComprobante(); // include Addenda1 only if explicitly defined and CFD is already signed

        comprobante.getAttSerie().setString(msSeries);
        comprobante.getAttFolio().setString("" + mnNumber);
        comprobante.getAttFecha().setDatetime(tDate);
        
        comprobante.getAttSello().setString("");
        comprobante.getAttNoCertificado().setString(signature.getCertificateNumber());
        comprobante.getAttCertificado().setString(signature.getCertificateBase64());

        //comprobante.getAttFormaPago().setString();
        //comprobante.getAttCondicionesDePago().setString();
        
        comprobante.getAttSubTotal().setDouble(0);
        comprobante.getAttSubTotal().setDecimals(0);

        comprobante.getAttMoneda().setString(DCfdi40Catalogs.ClaveMonedaXxx);
        //comprobante.getAttTipoCambio().setDouble();
        
        comprobante.getAttTotal().setDouble(0);
        comprobante.getAttTotal().setDecimals(0);

        comprobante.getAttTipoDeComprobante().setString(DCfdi40Catalogs.CFD_TP_P);
        //comprobante.getAttMetodoPago().setString();
        
        comprobante.getAttLugarExpedicion().setString(bprEmisor.getActualAddressFiscal());
        
        comprobante.getAttConfirmacion().setString(moXtaDfrMate.getConfirmation());
        comprobante.getAttExportacion().setString(DCfdi40Catalogs.ClaveExportacionNoAplica);
        
        // element 'CfdiRelacionados':
        if (moXtaDfrMate.getRelations() != null && !moXtaDfrMate.getRelations().getRelatedCfds().isEmpty()) {
            DDfrMateRelations.RelatedCfd relatedCfd = moXtaDfrMate.getRelations().getRelatedCfds().get(0);
            cfd.ver40.DElementCfdiRelacionados cfdiRelacionados = new cfd.ver40.DElementCfdiRelacionados();
            cfdiRelacionados.getAttTipoRelacion().setString(relatedCfd.RelationCode);
            
            for (String uuid : relatedCfd.Uuids) {
                cfd.ver40.DElementCfdiRelacionado cfdiRelacionado = new cfd.ver40.DElementCfdiRelacionado();
                cfdiRelacionado.getAttUuid().setString(uuid);
                cfdiRelacionados.getEltCfdiRelacionados().add(cfdiRelacionado);
            }
            
            ArrayList<cfd.ver40.DElementCfdiRelacionados> arrayCfdiRelacionados = new ArrayList<>();
            arrayCfdiRelacionados.add(cfdiRelacionados);
            comprobante.setEltOpcCfdiRelacionados(arrayCfdiRelacionados);
        }

        // element 'Emisor':
        comprobante.getEltEmisor().getAttRfc().setString(bprEmisor.getFiscalId());
        comprobante.getEltEmisor().getAttNombre().setString(bprEmisor.getNameFiscal());
        comprobante.getEltEmisor().getAttRegimenFiscal().setString(moXtaDfrMate.getIssuerTaxRegime());

        // element 'Receptor':
        comprobante.getEltReceptor().getAttRfc().setString(bprReceptor.getFiscalId());
        comprobante.getEltReceptor().getAttNombre().setString(bprReceptor.getNameFiscal());
        comprobante.getEltReceptor().getAttDomicilioFiscalReceptor().setString(moXtaDfrMate.getReceiverFiscalAddress());
        comprobante.getEltReceptor().getAttRegimenFiscalReceptor().setString(moXtaDfrMate.getReceiverTaxRegime());
        //comprobante.getEltReceptor().getAttResidenciaFiscal().setString(""); // not supported yet!
        //comprobante.getEltReceptor().getAttNumRegIdTrib().setString(""); // not supported yet!
        comprobante.getEltReceptor().getAttUsoCFDI().setString(moXtaDfrMate.getCfdUsage());

        // element 'Conceptos':
        
        cfd.ver40.DElementConcepto concepto = new cfd.ver40.DElementConcepto();

        concepto.getAttClaveProdServ().setString(DCfdi40Catalogs.ClaveProdServServsFacturacion);
        //concepto.getAttNoIdentificacion().setString();
        concepto.getAttCantidad().setDouble(1);
        concepto.getAttCantidad().setDecimals(0);
        concepto.getAttClaveUnidad().setString(DCfdi40Catalogs.ClaveUnidadAct);
        //concepto.getAttUnidad().setString();
        concepto.getAttDescripcion().setString(DCfdi40Catalogs.ConceptoPago);
        concepto.getAttValorUnitario().setDouble(0);
        concepto.getAttValorUnitario().setDecimals(0);
        concepto.getAttImporte().setDouble(0);
        concepto.getAttImporte().setDecimals(0);
        //concepto.getAttDescuento().setDouble();
        concepto.getAttObjetoImp().setString(DCfdi40Catalogs.ClaveObjetoImpNo);

        comprobante.getEltConceptos().getEltConceptos().add(concepto);
        
        if (moDfrPagos20 != null) {
            cfd.ver40.DElementComplemento complemento = new cfd.ver40.DElementComplemento();
            complemento.getElements().add(moDfrPagos20);
            comprobante.setEltOpcComplemento(complemento);
        }

        return comprobante;
    }

    @Override
    public String getDocName() {
        return "comprobante";
    }

    @Override
    public String getDocNumber() {
        return getDfrNumber();
    }

    @Override
    public Date getDocDate() {
        return getDocTs();
    }
    
    /**
     * Get DPS status. Constants defined in DModSysConsts.TS_XML_ST_...
     */
    @Override
    public int getDocStatus() {
        return getFkXmlStatusId();
    }
    
    @Override
    public int getBizPartnerCategory() {
        return DModSysConsts.BS_BPR_CL_CUS;
    }

    @Override
    public int[] getBizPartnerBranchAddressKey() {
        return new int[] { mnFkBizPartnerId, DUtilConsts.BPR_BRA_ID, DUtilConsts.BRA_ADD_ID };
    }

    @Override
    public DDbDfr getDfr() {
        return this;
    }

    @Override
    public DDbRegistry createDocSending() {
        return null;
    }
    
    /**
     * Issues Digital Fiscal Receipt (DFR) as file of type Portable Document Format (PDF).
     * @param session
     * @throws net.sf.jasperreports.engine.JRException
     */
    @Override
    public void printDfr(final DGuiSession session) throws Exception {
        String fileName = "";
        DDbConfigBranch configBranch = null;
        
        if (mnFkXmlStatusId == DModSysConsts.TS_XML_ST_ISS) {
            configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, getCompanyBranchKey());
            fileName += configBranch.getDfrDirectory();
            fileName += msDocXmlName.replaceAll(".xml", ".pdf");

            switch (mnFkXmlTypeId) {
                case DModSysConsts.TS_XML_TP_CFD:
                case DModSysConsts.TS_XML_TP_CFDI_32:
                    throw new UnsupportedOperationException("Not supported yet."); // no plans for supporting it later

                case DModSysConsts.TS_XML_TP_CFDI_33:
                    DPrtUtils.exportReportToPdfFile(session, DModConsts.TR_DPS_CFDI_33_CRP_10, DTrnDfrPrinting.createPrintingMapCfdi33(session, this), fileName);
                    break;

                case DModSysConsts.TS_XML_TP_CFDI_40:
                    DPrtUtils.exportReportToPdfFile(session, DModConsts.TR_DPS_CFDI_40_CRP_20, DTrnDfrPrinting.createPrintingMapCfdi40(session, this), fileName);
                    break;

                default:
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
    }
    
    @Override
    public boolean canAnnul(final DGuiSession session) throws Exception {
        return canDisable(session);
    }
    
    /**
     * Check if registry is available, that is, not locked.
     * @param session User session.
     * @return
     * @throws Exception 
     */
    @Override
    public boolean checkAvailability(final DGuiSession session) throws Exception {
        boolean isAvailable = true;
        
        if (!mbRegistryNew) {
            if (moAuxLock == null) {
                // no lock set already, check only availabitity:
                isAvailable = DLockUtils.isRegistryAvailable(session, mnRegistryType, mnPkDfrId);
            }
            else {
                // lock already set, validate it:
                DLockUtils.validateLock(session, moAuxLock, true);
                isAvailable = true;
            }
        }
        
        return isAvailable;
    }

    /**
     * Assure lock. That is if it does not already exist, it is created, otherwise validated.
     * @param session GUI session.
     * @return 
     * @throws Exception 
     */
    @Override
    public DDbLock assureLock(final DGuiSession session) throws Exception {
        if (!mbRegistryNew) {
            if (moAuxLock == null) {
                // no lock set already, create it:
                moAuxLock = DLockUtils.createLock(session, mnRegistryType, mnPkDfrId, TIMEOUT);
            }
            else {
                // lock already set, validate it:
                DLockUtils.validateLock(session, moAuxLock, true);
            }
        }
        
        return moAuxLock;
    }

    /**
     * Free current lock, if any, with by-update status.
     * @param session GUI session.
     * @param freedLockStatus Options supported: DDbLock.LOCK_ST_FREED_...
     * @throws Exception 
     */
    @Override
    public void freeLock(final DGuiSession session, final int freedLockStatus) throws Exception {
        if (moAuxLock != null) {
            DLockUtils.freeLock(session, moAuxLock, freedLockStatus);
            moAuxLock = null;
        }
    }
}
