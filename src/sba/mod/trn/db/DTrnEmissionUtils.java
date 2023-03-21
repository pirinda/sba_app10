/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import cfd.DCfdConsts;
import cfd.ver40.DCfdi40Consts;
import cfd.ver40.DCfdi40Utils;
import java.awt.Cursor;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.commons.text.WordUtils;
import sba.gui.DGuiClientApp;
import sba.gui.prt.DPrtUtils;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridRowView;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiSession;
import sba.lib.mail.DMailConsts;
import sba.lib.mail.gmail.DGmail;
import sba.lib.mail.gmail.DMessage;
import sba.lib.xml.DXmlUtils;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.bpr.db.DBprEmail;
import sba.mod.bpr.db.DDbBizPartner;
import sba.mod.bpr.db.DDbBranchAddress;
import sba.mod.cfg.db.DDbConfigBranch;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.cfg.db.DDbLock;
import sba.mod.cfg.db.DDbSysCurrency;
import sba.mod.cfg.db.DDbSysXmlSignatureProvider;
import sba.mod.cfg.db.DLockUtils;
import sba.mod.lad.db.DDbBol;
import sba.mod.trn.form.DDialogEmailSending;
import sba.mod.trn.form.DFormDpsCancelling;
import sba.mod.trn.form.DFormDpsPrinting;
import sba.mod.trn.form.DFormDpsSigning;
import sba.mod.trn.form.DFormDpsTypeChange;

/**
 * Utilities related to printing, signing, cancelling and sending documents.
 * 
 * @author Sergio Flores
 */
public abstract class DTrnEmissionUtils {
    
    public static int[] getDpsOwnDpsTypeKey(final DGuiSession session, final int[] dpsKey) throws Exception {
        int[] key = null;
        String sql = "";
        ResultSet resultSet = null;
        
        sql = "SELECT fk_dps_ct, fk_dps_cl, fk_dps_tp "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " "
                + "WHERE id_dps = " + dpsKey[0] + " ";
        resultSet = session.getStatement().executeQuery(sql);
        if (!resultSet.next()) {
            throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            key = new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) };
        }
        
        return key;
    }
    
    public static int[] getDpsOwnCompanyBranchKey(final DGuiSession session, final int[] dpsKey) throws Exception {
        int[] key = null;
        String sql = "";
        ResultSet resultSet = null;
        
        sql = "SELECT fk_own_bpr, fk_own_bra "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " "
                + "WHERE id_dps = " + dpsKey[0] + " ";
        resultSet = session.getStatement().executeQuery(sql);
        if (!resultSet.next()) {
            throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            key = new int[] { resultSet.getInt(1), resultSet.getInt(2) };
        }
        
        return key;
    }
    
    /**
     * Checks if XML signature request is allowed.
     * @param session GUI session.
     * @param requestType Signature request type: sign or cancel; constants defined in DModSysConsts.TX_XMS_REQ_TP_.
     * @param requestSubtype Signature request subtype: stamp request or stamp verification; constants defined in DModSysConsts.TX_XMS_REQ_STP_.
     * @param dfr DFR.
     * @return Emission settings.
     */
    private static DTrnEmissionSettings checkXmlSignatureRequestAllowed(final DGuiSession session, final int requestType, final int requestSubtype, final DDbDfr dfr) throws Exception {
        DTrnEmissionSettings settings = new DTrnEmissionSettings();
        DDbConfigCompany configCompany = (DDbConfigCompany) session.getConfigCompany();
        DDbConfigBranch configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, dfr.getCompanyBranchKey());
        DDbSysXmlSignatureProvider xsp = null;

        if (!configCompany.isEdsApplying()) {
            throw new Exception("No se ha habilitado la emisión de documentos electrónicos en la configuración de la " + DUtilConsts.TXT_COMPANY.toLowerCase() + ".");
        }
        else if (configBranch.getFkXmlSignatureProviderId() == DModSysConsts.CS_XSP_NA) {
            throw new Exception("No se ha definido el " + DTrnEmissionConsts.PAC + " en la configuración de la " + DUtilConsts.TXT_BRANCH.toLowerCase() + ".");
        }
        else {
            settings.SignatureProviderId = configBranch.getFkXmlSignatureProviderId();
            
            xsp = (DDbSysXmlSignatureProvider) session.readRegistry(DModConsts.CS_XSP, new int[] { settings.SignatureProviderId });

            switch (requestType) {
                case DModSysConsts.TX_XMS_REQ_TP_SIG:
                    switch (requestSubtype) {
                        case DModSysConsts.TX_XMS_REQ_STP_REQ:
                            settings.RequestAllowed = xsp.isSignature();
                            break;
                        case DModSysConsts.TX_XMS_REQ_STP_VER:
                            settings.RequestAllowed = xsp.isSignatureVerification();
                            break;
                        default:
                            throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                    }
                    break;
                case DModSysConsts.TX_XMS_REQ_TP_CAN:
                    switch (requestSubtype) {
                        case DModSysConsts.TX_XMS_REQ_STP_REQ:
                            settings.RequestAllowed = xsp.isCancellation();
                            break;
                        case DModSysConsts.TX_XMS_REQ_STP_VER:
                            settings.RequestAllowed = xsp.isCancellationVerification();
                            break;
                        default:
                            throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                    }
                    break;
                default:
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
            
            if (!settings.RequestAllowed) {
                throw new Exception("El " + DTrnEmissionConsts.PAC + " no permite la operación solicitada.\n"
                        + "Será necesario realizar la acción manualmente por otros medios.");
            }
            else {
                // Check stamps available with DPS's company branch ones:
                
                settings.StampsAvailable = getStampsAvailable(session, settings.SignatureProviderId, dfr.getCompanyBranchKey());
                if (settings.StampsAvailable > 0) {
                    settings.SignatureCompanyBranchKey = dfr.getCompanyBranchKey();
                }
                else {
                    // Check stamps available with company ones:
                
                    settings.StampsAvailable = getStampsAvailable(session, settings.SignatureProviderId, null);
                    if (settings.StampsAvailable > 0) {
                        settings.SignatureCompanyBranchKey = null; // null value already set, just for consistence
                    }
                    else {
                        throw new Exception("Se agotaron los timbres del " + DTrnEmissionConsts.PAC + " procesar la solicitud.");
                    }
                }
            }
        }
        
        return settings;
    }
    
    public static DDbXmlSignatureRequest getLastXmlSignatureRequest(final DGuiSession session, final int dfrId) throws SQLException, Exception {
        DDbXmlSignatureRequest xsr = null;
        
        String sql = "SELECT COALESCE(MAX(id_req), 0) "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.T_XSR) + " "
                + "WHERE NOT b_del AND id_dfr = " + dfrId + ";";
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next() && resultSet.getInt(1) > 0) {
                xsr = (DDbXmlSignatureRequest) session.readRegistry(DModConsts.T_XSR, new int[] { dfrId, resultSet.getInt(1) });
            }
        }
        
        return xsr;
    }

    public static int getStampsAvailable(final DGuiSession session, final int xmlSignatureProviderId, final int[] signatureCompanyBranchKey) throws SQLException, Exception {
        int stamps = 0;
        String sql = "";
        ResultSet resultSet = null;
        
        sql = "SELECT SUM(mov_in) - SUM(mov_out) "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.T_XSM) + " "
                + "WHERE b_del = 0 AND id_xsp = " + xmlSignatureProviderId + " "
                + (signatureCompanyBranchKey == null ? "" : "AND fk_sig_bpr_n = " + signatureCompanyBranchKey[0] + " AND fk_sig_bra_n = " + signatureCompanyBranchKey[1] + " ");
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next()) {
            stamps = resultSet.getInt(1);
        }
        
        return stamps;
    }
    
    public static String getFileXml(DGuiClient client) throws Exception {
        String xml = "";
        FileFilter filter = new FileNameExtensionFilter(DUtilConsts.FILE_EXT_XML.toUpperCase(), DUtilConsts.FILE_EXT_XML);
        client.getFileChooser().repaint();
        client.getFileChooser().addChoosableFileFilter(filter);
        client.getFileChooser().setAcceptAllFileFilterUsed(false);
        client.getFileChooser().setFileFilter(filter);

        if (client.getFileChooser().showOpenDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
            xml = DXmlUtils.readXml(client.getFileChooser().getSelectedFile().getAbsolutePath());
        }
        
        client.getFileChooser().resetChoosableFileFilters();
        client.getFileChooser().setAcceptAllFileFilterUsed(true);
        
        return xml;
    }
    
    public static FileInputStream getFilePdf(DGuiClient client) throws FileNotFoundException, Exception {
        FileInputStream fis = null;
        FileFilter filter = new FileNameExtensionFilter(DUtilConsts.FILE_EXT_XML.toUpperCase(), DUtilConsts.FILE_EXT_XML);
        client.getFileChooser().repaint();
        client.getFileChooser().addChoosableFileFilter(filter);
        client.getFileChooser().setAcceptAllFileFilterUsed(false);
        client.getFileChooser().setFileFilter(filter);

        if (client.getFileChooser().showOpenDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
            fis = new FileInputStream(client.getFileChooser().getSelectedFile());
        }
        
        client.getFileChooser().resetChoosableFileFilters();
        client.getFileChooser().setAcceptAllFileFilterUsed(true);
        
        return fis;
    }
    
    public static void consumeStamp(final DGuiSession session, final int xmlSignatureProviderId, final int[] signatureCompanyBranchKey, final int[] xmlSignatureMoveKey, final int dfrId) throws SQLException, Exception {
        DDbXmlSignatureMove xsm = null;
        
        xsm = new DDbXmlSignatureMove();
        xsm.setPkXmlSignatureProviderId(xmlSignatureProviderId);
        //xsm.setPkMoveId(...);
        xsm.setDate(session.getWorkingDate());
        xsm.setMoveIn(0);
        xsm.setMoveOut(1);
        xsm.setDeleted(false);
        xsm.setSystem(true);
        xsm.setFkXsmClassId(xmlSignatureMoveKey[0]);
        xsm.setFkXsmTypeId(xmlSignatureMoveKey[1]);
        xsm.setFkSignatureBizPartnerId_n(signatureCompanyBranchKey == null ? DLibConsts.UNDEFINED : signatureCompanyBranchKey[0]);
        xsm.setFkSignatureBranchId_n(signatureCompanyBranchKey == null ? DLibConsts.UNDEFINED : signatureCompanyBranchKey[1]);
        xsm.setFkDfrId_n(dfrId);
        xsm.save(session);
    }
    
    public static void changeDpsType(final DGuiClient client, final DGridRowView gridRow) {
        DDbDps dps = null;
        DDbDfr dfr = null;
        DDbDpsTypeChange dpsTypeChange = null;
        DFormDpsTypeChange formDpsTypeChange = null;

        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            try {
                dps = (DDbDps) client.getSession().readRegistry(DModConsts.T_DPS, gridRow.getRowPrimaryKey());
                dfr = dps.getChildDfr();

                if (dfr != null) {
                    throw new Exception(DTrnEmissionConsts.MSG_DENIED_CHANGE + "El documento es electrónico.");
                }
                else {
                    formDpsTypeChange = (DFormDpsTypeChange) client.getSession().getModule(DModConsts.MOD_TRN).getForm(DModConsts.T_DPS_CHG, DLibConsts.UNDEFINED, null);
                    formDpsTypeChange.setRegistry(dps);
                    formDpsTypeChange.setVisible(true);

                    if (formDpsTypeChange.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                        dpsTypeChange = formDpsTypeChange.getRegistry();

                        if (client.getSession().saveRegistry(dpsTypeChange) == DDbConsts.SAVE_OK) {
                            client.getSession().notifySuscriptors(DModConsts.T_DPS);
                        }
                    }
                }
            }
            catch (Exception e) {
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
            }
        }
    }

    @SuppressWarnings("deprecation")
    public static void printDps(final DGuiClient client, final DGridRowView gridRow, final int printMode) {
        boolean print = true;
        boolean printed = false;
        DDbDps dps = null;
        DDbDfr dfr = null;
        DDbLock lock = null;
        DDbDpsPrinting dpsPrinting = null;
        DDbDpsSeries series = null;
        DDbSysCurrency currency = null;
        DDbBizPartner bizPartner = null;
        DDbConfigBranch configBranch = null;
        HashMap<String, Object> map = null;

        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            try {
                dps = (DDbDps) client.getSession().readRegistry(DModConsts.T_DPS, gridRow.getRowPrimaryKey());
                dfr = dps.getChildDfr();

                // ensure exclusive access is granted:
                lock = dps.assureLock(client.getSession()); // lock just about to be set
                
                // Check if document number can be still changed:

                if (DTrnUtils.isDpsNumberAutomatic(dps.getDpsClassKey()) && ((DDbConfigBranch) client.getSession().getConfigBranch()).isDpsPrintingDialog() &&
                    (dfr == null || (dfr.isDfrCfdi() && dfr.getFkXmlStatusId() == DModSysConsts.TS_XML_ST_PEN))) {
                    DFormDpsPrinting formDpsPrinting = (DFormDpsPrinting) client.getSession().getModule(DModConsts.MOD_TRN).getForm(DModConsts.T_DPS_PRT, DLibConsts.UNDEFINED, null);
                    formDpsPrinting.setRegistry(dps);
                    formDpsPrinting.setVisible(true);

                    if (formDpsPrinting.getFormResult() != DGuiConsts.FORM_RESULT_OK) {
                        print = false;
                    }
                    else {
                        dpsPrinting = formDpsPrinting.getRegistry();
                        dpsPrinting.save(client.getSession());

                        DLockUtils.resumeLock(client.getSession(), lock); // a long journey to go, so resume lock to prevent losing it

                        if (dpsPrinting.hasDpsChanged()) {
                            DDbDpsSeriesNumber seriesNumber = DTrnUtils.getDpsSeriesNumber(client.getSession(), dps.getDpsTypeKey(), dps.getSeries(), dps.getNumber());
                            
                            if (seriesNumber != null && seriesNumber.getFkXmlTypeId() != DModSysConsts.TS_XML_TP_NA) {
                                // reissue document:
                                dps = (DDbDps) client.getSession().readRegistry(DModConsts.T_DPS, gridRow.getRowPrimaryKey()); // retrieve document again, it has been just updated
                                dps.setAuxLock(lock); // set transaction lock
                                dfr = DTrnDfrUtils.createDfrFromDps(client.getSession(), dps, seriesNumber.getFkXmlTypeId());
                                dps.updateDfr(client.getSession(), dfr);
                            }
                            
                            client.getSession().notifySuscriptors(DModConsts.T_DPS);
                        }
                    }
                }

                // Print document:

                if (print) {
                    if (dfr == null || dfr.getFkXmlStatusId() == DModSysConsts.TS_XML_ST_PEN) {
                        // Print document as "document":

                        series = DTrnUtils.getDpsSeries(client.getSession(), dps.getDpsTypeKey(), dps.getSeries());
                        currency = (DDbSysCurrency) client.getSession().readRegistry(DModConsts.CS_CUR, dps.getCurrencyKey());

                        map = DPrtUtils.createReportHashMap(client.getSession());
                        map.put("nPkDps", (long) dps.getPkDpsId());
                        map.put("sDocTotalConLetra", DLibUtils.translateValueToText(dps.getTotalCy_r(), DLibUtils.getDecimalFormatAmount().getMaximumFractionDigits(),
                                client.getSession().getSessionCustom().getLocalLanguage(),
                                currency.getCurrencySingular(), currency.getCurrencyPlural(), currency.getCurrencyPrefix(), currency.getCurrencySuffix()));

                        if (series != null && series.isTaxImprovement()) {
                            bizPartner = (DDbBizPartner) client.getSession().readRegistry(DModConsts.BU_BPR, dps.getBizPartnerKey());
                            if (dps.getFkEmissionTypeId() == DModSysConsts.TS_EMI_TP_PUB || dps.getFkEmissionTypeId() == DModSysConsts.TS_EMI_TP_PUB_NAM ||
                                    bizPartner.getFiscalId().equals(DCfdConsts.RFC_GEN_NAC)) {
                                map.put("bTaxImprovement", true);
                            }
                        }
                        
                        configBranch = (DDbConfigBranch) client.getSession().readRegistry(DModConsts.CU_CFG_BRA, dps.getCompanyBranchKey());
                        map.put("sEdsDir", configBranch.getDfrDirectory());

                        DPrtUtils.printReport(client.getSession(), DModConsts.TR_DPS, printMode, map);
                        printed = true;
                    }
                    else {
                        // Print document as "electronic document":

                        switch (dfr.getFkXmlTypeId()) {
                            case DModSysConsts.TS_XML_TP_CFD:
                                throw new UnsupportedOperationException("Not supported yet.");  // no plans for supporting it later
                                
                            case DModSysConsts.TS_XML_TP_CFDI_32:
                                DPrtUtils.printReport(client.getSession(), DModConsts.TR_DPS_CFDI_32, printMode, new DTrnDpsPrinting(client.getSession(), gridRow.getRowPrimaryKey()).cratePrintMapCfdi32());
                                printed = true;
                                break;
                                
                            case DModSysConsts.TS_XML_TP_CFDI_33:
                                DPrtUtils.printReport(client.getSession(), DModConsts.TR_DPS_CFDI_33, printMode, new DTrnDpsPrinting(client.getSession(), gridRow.getRowPrimaryKey()).cratePrintMapCfdi33());
                                printed = true;
                                break;
                                
                            case DModSysConsts.TS_XML_TP_CFDI_40:
                                DPrtUtils.printReport(client.getSession(), DModConsts.TR_DPS_CFDI_40, printMode, new DTrnDpsPrinting(client.getSession(), gridRow.getRowPrimaryKey()).cratePrintMapCfdi40());
                                printed = true;
                                break;
                                
                            default:
                                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                        }
                    }
                }
            }
            catch (Exception e) {
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
            }
            finally {
                if (printed && dpsPrinting == null) {
                    try {
                        dpsPrinting = dps.createDpsPrinting(true);
                        dpsPrinting.save(client.getSession());
                    }
                    catch (Exception e) {
                        DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
                    }
                    
                    client.getSession().notifySuscriptors(DModConsts.T_DPS_PRT);
                }
                
                try {
                    if (lock != null && lock.getLockStatus() == DDbLock.LOCK_ST_ACTIVE) {
                        DLockUtils.freeLock(client.getSession(), lock, printed ? DDbLock.LOCK_ST_FREED_UPDATE : DDbLock.LOCK_ST_FREED_EXCEPTION);
                    }
                }
                catch (Exception e) {
                    DLibUtils.printException(DTrnEmissionUtils.class.getName(), e);
                }
            }
        }
    }

    public static void printDfr(final DGuiClient client, final DGridRowView gridRow) {
        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            try {
                DDbDfr dfr = (DDbDfr) client.getSession().readRegistry(DModConsts.T_DFR, gridRow.getRowPrimaryKey());

                switch (dfr.getFkXmlTypeId()) {
                    case DModSysConsts.TS_XML_TP_CFD:
                    case DModSysConsts.TS_XML_TP_CFDI_32:
                        throw new UnsupportedOperationException("Not supported yet."); // no plans for supporting it later

                    case DModSysConsts.TS_XML_TP_CFDI_33:
                        DPrtUtils.printReport(client.getSession(), DModConsts.TR_DPS_CFDI_33_CRP_10, DTrnDfrPrinting.createPrintingMap33(client.getSession(), dfr));
                        break;

                    case DModSysConsts.TS_XML_TP_CFDI_40:
                        DPrtUtils.printReport(client.getSession(), DModConsts.TR_DPS_CFDI_40_CRP_20, DTrnDfrPrinting.createPrintingMap40(client.getSession(), dfr));
                        break;

                    default:
                        throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
            }
            catch (Exception e) {
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
            }
        }
    }

    public static void printBol(final DGuiClient client, final DGridRowView gridRow) {
        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            try {
                DDbBol bol = (DDbBol) client.getSession().readRegistry(DModConsts.L_BOL, gridRow.getRowPrimaryKey());

                switch (bol.getDfr().getFkXmlTypeId()) {
                    case DModSysConsts.TS_XML_TP_CFD:
                    case DModSysConsts.TS_XML_TP_CFDI_32:
                    case DModSysConsts.TS_XML_TP_CFDI_33:
                        throw new UnsupportedOperationException("Not supported yet."); // no plans for supporting it later

                    case DModSysConsts.TS_XML_TP_CFDI_40:
                        //DPrtUtils.printReport(client.getSession(), DModConsts.TR_DPS_CFDI_40_CRP_20, DTrnDfrPrinting.createPrintingMap40(client.getSession(), bol));
                        break;

                    default:
                        throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
            }
            catch (Exception e) {
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
            }
        }
    }

    /**
     * Signs a document.
     * <b>WARNING:</b> Current lock for Client will be set, manage it propperly.
     * @param client GUI Client.
     * @param gridRow Document.
     * @param requestSubtype Constants defined in DModSysConsts.TX_XMS_REQ_STP_...
     * @throws Exception 
     */
    private static void signDoc(final DGuiClient client, final DTrnDoc doc, final int requestSubtype) throws Exception {
        DDbDfr dfr = doc.getDfr();

        if (dfr == null || dfr.isRegistryNew()) {
            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN + DDbConsts.ERR_MSG_REG_NOT_FOUND + "\n"
                    + "El registro XML del " + doc.getDocName() + " '" + doc.getDocNumber() + "' no existe.");
        }
        else if (!dfr.isDfrCfdi()) {
            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN
                    + "El registro XML del " + doc.getDocName() + " '" + doc.getDocNumber() + "' no es un CFDI.");
        }
        else if (dfr.getFkXmlStatusId() == DModSysConsts.TS_XML_ST_ISS) {
            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN
                    + "El registro XML del " + doc.getDocName() + " '" + doc.getDocNumber() + "' ya está "
                    + "'" + client.getSession().readField(DModConsts.TS_XML_ST, new int[] { DModSysConsts.TS_XML_ST_ISS }, DDbRegistry.FIELD_NAME) + "'.");
        }
        else if (dfr.getFkXmlStatusId() != DModSysConsts.TS_XML_ST_PEN) {
            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN
                    + "El registro XML del " + doc.getDocName() + " '" + doc.getDocNumber() + "' debe estar "
                    + "'" + client.getSession().readField(DModConsts.TS_XML_ST, new int[] { DModSysConsts.TS_XML_ST_PEN }, DDbRegistry.FIELD_NAME) + "'.");
        }
        
        // Check if signature is allowed:
        DTrnEmissionSettings settings = checkXmlSignatureRequestAllowed(client.getSession(), DModSysConsts.TX_XMS_REQ_TP_SIG, requestSubtype, dfr);

        if (settings != null && settings.RequestAllowed) {
            boolean sign = true;
            boolean confirmSign = false;
            boolean signed = false;

            // ensure exclusive access is granted:
            DDbLock lock = doc.assureLock(client.getSession()); // lock just about to be set
            ((DGuiClientApp) client).setCurrentLock(lock); // to guarantee that lock is freed if an exception occurs

            // Check for pending transactions:

            DDbSysXmlSignatureProvider xsp = (DDbSysXmlSignatureProvider) client.getSession().readRegistry(DModConsts.CS_XSP, new int[] { settings.SignatureProviderId });
            DDbXmlSignatureRequest xsr = getLastXmlSignatureRequest(client.getSession(), dfr.getPkDfrId());

            switch (requestSubtype) {
                case DModSysConsts.TX_XMS_REQ_STP_REQ: // request signature
                    if (xsr != null) {
                        if (xsr.getRequestStatus() < DModSysConsts.TX_XMS_REQ_ST_FIN) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN
                                    + "El " + doc.getDocName() + " '" + doc.getDocNumber() + "' tiene una transacción pendiente de " + DTrnDfrUtils.getXmsRequestType(xsr.getRequestType()) + " en estatus '" + xsr.getRequestStatus() + "'.\n"
                                    + "SUGERENCIA: Realizar una " + DTrnDfrUtils.getXmsRequestSubype(DModSysConsts.TX_XMS_REQ_STP_VER) + " de " + DTrnDfrUtils.getXmsRequestType(xsr.getRequestType()) + ".");
                        }
                        
                        xsr = null; // new request will be created
                    }
                    break;

                case DModSysConsts.TX_XMS_REQ_STP_VER: // verify signature
                    if (xsr == null || xsr.getRequestType() != DModSysConsts.TX_XMS_REQ_TP_SIG) {
                        throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN_VER
                                + "El " + doc.getDocName() + " '" + doc.getDocNumber() + "' no tiene transacciones pendientes de " + DTrnDfrUtils.getXmsRequestType(DModSysConsts.TX_XMS_REQ_TP_SIG) + ".\n"
                                + "SUGERENCIA: Realizar una " + DTrnDfrUtils.getXmsRequestSubype(DModSysConsts.TX_XMS_REQ_STP_REQ) + " de " + DTrnDfrUtils.getXmsRequestType(DModSysConsts.TX_XMS_REQ_TP_SIG) + ".");
                    }
                    else if (xsr.getRequestStatus() == DModSysConsts.TX_XMS_REQ_ST_FIN) {
                        confirmSign = true; // the last transaction is finished, only a new transaction for confirmation can be requested
                    }
                    break;

                default:
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }

            // Check if registry can be signed:

            if (xsr == null) {
                // Check and confirm registry eligibility to be signed on a new request:

                if (doc instanceof DDbDps && doc.getDocStatus() != DModSysConsts.TS_DPS_ST_ISS) {
                    throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN
                            + "El " + doc.getDocName() + " '" + doc.getDocNumber() + "' debe estar "
                            + "'" + client.getSession().readField(DModConsts.TS_DPS_ST, new int[] { DModSysConsts.TS_DPS_ST_ISS }, DDbRegistry.FIELD_NAME) + "'.");
                }
                else if (doc instanceof DDbDfr && doc.getDocStatus() != DModSysConsts.TS_XML_ST_PEN) {
                    throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN
                            + "El " + doc.getDocName() + " '" + doc.getDocNumber() + "' debe estar "
                            + "'" + client.getSession().readField(DModConsts.TS_XML_ST, new int[] { DModSysConsts.TS_XML_ST_PEN }, DDbRegistry.FIELD_NAME) + "'.");
                }
                else {
                    Date now = new Date();
                    long gap = now.getTime() - doc.getDocDate().getTime(); // time gap in milliseconds
                    double delay = (gap / (double) (1000 * 60 * 60));

                    if ((gap < 0) && 
                            client.showMsgBoxConfirm("La fecha-hora del " + doc.getDocName() + " '" + doc.getDocNumber() + "', " + DLibUtils.DateFormatDatetime.format(doc.getDocDate().getTime()) + ",\n"
                                    + "no debe ser posterior a la fecha-hora actual, " + DLibUtils.DateFormatDatetime.format(now) + ".\n"
                                    + "A pesar de lo anterior, ¿desea intentar timbrarlo?") != JOptionPane.YES_OPTION) {
                        throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN
                                + "Cambiar la fecha del " + doc.getDocName() + " '" + doc.getDocNumber() + "' "
                                + "para que no sea posterior a la fecha-hora actual.");
                    }
                    else if (delay > DCfdi40Consts.HOURS_TO_SIGN && 
                            client.showMsgBoxConfirm("La fecha-hora del " + doc.getDocName() + " '" + doc.getDocNumber() + "', " + DLibUtils.DateFormatDatetime.format(doc.getDocDate().getTime()) + ",\n"
                                    + "no debe ser anterior a la fecha-hora actual, " + DLibUtils.DateFormatDatetime.format(now) + ", por más de " + DCfdi40Consts.HOURS_TO_SIGN + " hrs.\n"
                                    + "Hay un exceso de " + DLibUtils.DecimalFormatValue4D.format(delay - DCfdi40Consts.HOURS_TO_SIGN) + " hrs. más de lo permitido.\n"
                                    + "A pesar de lo anterior, ¿desea intentar timbrarlo?") != JOptionPane.YES_OPTION) {
                        throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN
                                + "Cambiar la fecha del " + doc.getDocName() + " '" + doc.getDocNumber() + "' "
                                + "para que no sea anterior a la fecha-hora actual por más de " + DCfdi40Consts.HOURS_TO_SIGN + " hrs.");
                    }
                }
            }

            if (!sign) {
                throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN
                        + "El " + doc.getDocName() + " '" + doc.getDocNumber() + "' no se puede timbrar.");
            }
            else {
                // Proceed with signature:

                DDbDps dps = null;

                switch (requestSubtype) {
                    case DModSysConsts.TX_XMS_REQ_STP_REQ:
                        if (((DDbConfigBranch) client.getSession().getConfigBranch()).isDpsPrintingDialog() && doc instanceof DDbDps && DTrnUtils.isDpsNumberAutomatic(((DDbDps) doc).getDpsClassKey())) {
                            dps = (DDbDps) doc;

                            DFormDpsSigning formDpsSigning = (DFormDpsSigning) client.getSession().getModule(DModConsts.MOD_TRN).getForm(DModConsts.T_DPS_SIG, DModSysConsts.TX_XMS_REQ_TP_SIG, null);
                            formDpsSigning.setRegistry(dps);
                            formDpsSigning.setVisible(true);

                            if (formDpsSigning.getFormResult() != DGuiConsts.FORM_RESULT_OK) {
                                sign = false;
                            }
                            else {
                                DDbDpsSigning dpsSigning = formDpsSigning.getRegistry();
                                dpsSigning.save(client.getSession());

                                DLockUtils.resumeLock(client.getSession(), lock); // a long journey to go, so resume lock to prevent losing it

                                if (dpsSigning.hasDpsChanged()) {
                                    DDbDpsSeriesNumber seriesNumber = DTrnUtils.getDpsSeriesNumber(client.getSession(), dpsSigning.getDpsTypeKey(), dpsSigning.getSeries(), dpsSigning.getNumber());

                                    if (seriesNumber != null && seriesNumber.getFkXmlTypeId() != DModSysConsts.TS_XML_TP_NA) {
                                        // reissue document:
                                        dps = (DDbDps) client.getSession().readRegistry(DModConsts.T_DPS, dps.getPrimaryKey()); // retrieve document again, it has been just updated
                                        dps.setAuxLock(lock); // set transaction lock
                                        dfr = DTrnDfrUtils.createDfrFromDps(client.getSession(), dps, seriesNumber.getFkXmlTypeId());
                                        dps.updateDfr(client.getSession(), dfr);
                                    }
                                    
                                    client.getSession().notifySuscriptors(DModConsts.T_DPS);
                                }
                            }
                        }
                        else {
                            sign = client.showMsgBoxConfirm("¿Desea timbrar el " + doc.getDocName() + " '" + doc.getDocNumber() + "'?") == JOptionPane.YES_OPTION;
                        }
                        break;

                    case DModSysConsts.TX_XMS_REQ_STP_VER:
                        if (confirmSign) {
                            sign = client.showMsgBoxConfirm("¿Desea confirmar la última solicitud de "
                                    + DTrnDfrUtils.getXmsRequestType(DModSysConsts.TX_XMS_REQ_TP_SIG) + " del " + doc.getDocName() + " '" + doc.getDocNumber() + "'?") == JOptionPane.YES_OPTION;
                        }
                        break;

                    default:
                        // nothing
                }

                if (sign) {
                    DTrnDoc docTbs = dps != null ? dps : doc; // doc to be signed

                    try {
                        client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR)); // XXX improve this!!!

                        DTrnDfrUtils.signDfr(client.getSession(), docTbs, xsp.getPkXmlSignatureProviderId(), settings.SignatureCompanyBranchKey, requestSubtype);
                        signed = true;
                    }
                    catch (Exception e) {
                        DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
                    }
                    finally {
                        client.getSession().notifySuscriptors(DModConsts.T_DFR);

                        if (!signed) {
                            client.showMsgBoxWarning("El " + docTbs.getDocName() + " '" + docTbs.getDocNumber() + "' "
                                    + "no fue timbrado.");
                        }
                        else {
                            client.showMsgBoxInformation("El " + docTbs.getDocName() + " '" + docTbs.getDocNumber() + "' "
                                    + "ha sido timbrado.\n"
                                    + "Quedan " + DLibUtils.DecimalFormatInteger.format(getStampsAvailable(client.getSession(), xsp.getPkXmlSignatureProviderId(), settings.SignatureCompanyBranchKey)) + " timbres disponibles.");

                            sendDoc(client, docTbs);
                        }

                        client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // XXX improve this!!!
                    }
                }
            }
            
            // free exclusive access:
            doc.freeLock(client.getSession(), signed ? DDbLock.LOCK_ST_FREED_UPDATE : DDbLock.LOCK_ST_FREED_EXCEPTION);
        }
    }

    /**
     * Signs a DPS.
     * <b>WARNING:</b> Assure to manage properly current lock in Client if an exception occurs during processing.
     * Requests subtypes:
     * a) <b>Request</b> attempts to sign the provided registry.
     * b) <b>Verification</b> resumes an unfinished previous request or confirms a pending finished cancel request before the authority.
     * @param client GUI Client.
     * @param gridRow Grid row containing DPS.
     * @param requestSubtype Constants defined in DModSysConsts.TX_XMS_REQ_STP_...
     * @throws java.lang.Exception
     */
    public static void signDps(final DGuiClient client, final DGridRowView gridRow, final int requestSubtype) throws Exception {
        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            DDbDps dps = (DDbDps) client.getSession().readRegistry(DModConsts.T_DPS, gridRow.getRowPrimaryKey(), DDbConsts.MODE_VERBOSE);
            signDoc(client, dps, requestSubtype);
        }
    }

    /**
     * Signs a DFR.
     * <b>WARNING:</b> Assure to manage properly current lock in Client if an exception occurs during processing.
     * Requests subtypes:
     * a) <b>Request</b> attempts to sign the provided registry.
     * b) <b>Verification</b> resumes an unfinished previous request or confirms a pending finished cancel request before the authority.
     * @param client GUI Client.
     * @param gridRow Grid row containing DFR.
     * @param requestSubtype Constants defined in DModSysConsts.TX_XMS_REQ_STP_..
     * @throws java.lang.Exception
     */
    public static void signDfr(final DGuiClient client, final DGridRowView gridRow, final int requestSubtype) throws Exception {
        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            DDbDfr dfr = (DDbDfr) client.getSession().readRegistry(DModConsts.T_DFR, gridRow.getRowPrimaryKey(), DDbConsts.MODE_VERBOSE);
            signDoc(client, dfr, requestSubtype);
        }
    }
    
    /**
     * Cancels a document.
     * <b>WARNING:</b> Current lock for Client will be set, manage it propperly.
     * @param client GUI Client.
     * @param gridRow Document.
     * @param requestSubtype Constants defined in DModSysConsts.TX_XMS_REQ_STP_...
     * @throws Exception 
     */
    private static void cancelDoc(final DGuiClient client, final DTrnDoc doc, final int requestSubtype) throws Exception {
        DDbDfr dfr = doc.getDfr();

        if (dfr == null || dfr.isRegistryNew()) {
            throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL + DDbConsts.ERR_MSG_REG_NOT_FOUND + "\n"
                    + "El registro XML del " + doc.getDocName() + " '" + doc.getDocNumber() + "' no existe.");
        }
        else if (!dfr.isDfrCfdi()) {
            throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL
                    + "El registro XML del " + doc.getDocName() + " '" + doc.getDocNumber() + "' no es un CFDI.");
        }
        else if (dfr.getFkXmlStatusId() == DModSysConsts.TS_XML_ST_ANN) {
            throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL
                    + "El registro XML del " + doc.getDocName() + " '" + doc.getDocNumber() + "' ya está "
                    + "'" + client.getSession().readField(DModConsts.TS_XML_ST, new int[] { DModSysConsts.TS_XML_ST_ANN }, DDbRegistry.FIELD_NAME) + "'.");
        }
        else if (dfr.getFkXmlStatusId() == DModSysConsts.TS_XML_ST_PEN) {
            String message = "El registro XML del " + doc.getDocName() + " '" + doc.getDocNumber() + "' está "
                    + "'" + client.getSession().readField(DModConsts.TS_XML_ST, new int[] { DModSysConsts.TS_XML_ST_PEN }, DDbRegistry.FIELD_NAME) + "',\n"
                    + "no es posible realizar una " + DTrnDfrUtils.getXmsRequestSubype(requestSubtype) + " de " + DTrnDfrUtils.getXmsRequestType(DModSysConsts.TX_XMS_REQ_TP_CAN) + ".";
            
            switch (requestSubtype) {
                case DModSysConsts.TX_XMS_REQ_STP_REQ:
                    if (client.showMsgBoxConfirm(message + "\n"
                            + "Se puede optar por continuar esta " + DTrnDfrUtils.getXmsRequestSubype(requestSubtype) + " sólo para anular el " + doc.getDocName() + " '" + doc.getDocNumber() + "'\n"
                            + "o interrumpirla para solicitar manulamnete la acción inhabilitar (anular) de forma directa.\n"
                            + DGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                        throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL
                                + "Inhabilitar (anular) de forma directa el " + doc.getDocName() + " '" + doc.getDocNumber() + "'.");
                    }
                    break;
                    
                case DModSysConsts.TX_XMS_REQ_STP_VER:
                    throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL
                            + message);
                    
                default:
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
        else if (dfr.getFkXmlStatusId() != DModSysConsts.TS_XML_ST_ISS) {
            throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL
                    + "El registro XML del " + doc.getDocName() + " '" + doc.getDocNumber() + "' debe estar "
                    + "'" + client.getSession().readField(DModConsts.TS_XML_ST, new int[] { DModSysConsts.TS_XML_ST_ISS }, DDbRegistry.FIELD_NAME) + "'.");
        }

        // Check if cancellation is allowed:
        DTrnEmissionSettings settings = checkXmlSignatureRequestAllowed(client.getSession(), DModSysConsts.TX_XMS_REQ_TP_CAN, requestSubtype, dfr);

        if (settings != null && settings.RequestAllowed) {
            boolean cancel = true;
            boolean confirmCancel = false;
            boolean cancelled = false;

            // ensure exclusive access is granted:
            DDbLock lock = doc.assureLock(client.getSession()); // lock just about to be set
            ((DGuiClientApp) client).setCurrentLock(lock); // to guarantee that lock is freed if an exception occurs

            // Check for pending transactions:

            DDbXmlSignatureRequest xsr = getLastXmlSignatureRequest(client.getSession(), dfr.getPkDfrId());

            switch (requestSubtype) {
                case DModSysConsts.TX_XMS_REQ_STP_REQ: // request cancellation
                    if (!dfr.getCancelStatus().isEmpty()) {
                        String status = DCfdi40Utils.getEstatusCancelación(dfr.getCancelStatus());
                        throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL
                                + "El " + doc.getDocName() + " '" + doc.getDocNumber() + "' parece estar en proceso de " + DTrnDfrUtils.getXmsRequestType(DModSysConsts.TX_XMS_REQ_TP_CAN) + " en estatus '" + (status.isEmpty() ? DTrnEmissionConsts.UNKNOWN : status) + "'.\n"
                                + "SUGERENCIA: Realizar una " + DTrnDfrUtils.getXmsRequestSubype(DModSysConsts.TX_XMS_REQ_STP_VER) + " de " + DTrnDfrUtils.getXmsRequestType(DModSysConsts.TX_XMS_REQ_TP_CAN) + ".");
                    }
                    else if (xsr != null) {
                        if (xsr.getRequestStatus() < DModSysConsts.TX_XMS_REQ_ST_FIN) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL
                                    + "El " + doc.getDocName() + " '" + doc.getDocNumber() + "' tiene una transacción pendiente de " + DTrnDfrUtils.getXmsRequestType(xsr.getRequestType()) + " en estatus '" + xsr.getRequestStatus() + "'.\n"
                                    + "SUGERENCIA: Realizar una " + DTrnDfrUtils.getXmsRequestSubype(DModSysConsts.TX_XMS_REQ_STP_VER) + " de " + DTrnDfrUtils.getXmsRequestType(xsr.getRequestType()) + ".");
                        }

                        xsr = null; // a new request will be created
                    }
                    break;

                case DModSysConsts.TX_XMS_REQ_STP_VER: // verify cancellation
                    if ((xsr == null || xsr.getRequestType() != DModSysConsts.TX_XMS_REQ_TP_CAN) && dfr.getCancelStatus().isEmpty()) {
                        throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL_VER
                                + "El " + doc.getDocName() + " '" + doc.getDocNumber() + "' no tiene transacciones pendientes de " + DTrnDfrUtils.getXmsRequestType(DModSysConsts.TX_XMS_REQ_TP_CAN) + ".\n"
                                + "SUGERENCIA: Realizar una " + DTrnDfrUtils.getXmsRequestSubype(DModSysConsts.TX_XMS_REQ_STP_REQ) + " de " + DTrnDfrUtils.getXmsRequestType(DModSysConsts.TX_XMS_REQ_TP_CAN) + ".");
                    }
                    else if (xsr.getRequestStatus() == DModSysConsts.TX_XMS_REQ_ST_FIN || !dfr.getCancelStatus().isEmpty()) {
                        xsr = null; // a new request will be created
                        confirmCancel = true; // the last transaction is finished, only a new transaction for confirmation can be requested
                    }
                    break;

                default:
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }

            // Check if registry can be cancelled:

            if (xsr == null) {
                // Check and validate registry eligibility to be cancelled on a new request:

                if (doc instanceof DDbDps && doc.getDocStatus() != DModSysConsts.TS_DPS_ST_ISS) {
                    throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL
                            + "El " + doc.getDocName() + " '" + doc.getDocNumber() + "' debe estar "
                            + "'" + client.getSession().readField(DModConsts.TS_DPS_ST, new int[] { DModSysConsts.TS_DPS_ST_ISS }, DDbRegistry.FIELD_NAME) + "'.");
                }
                else if (doc instanceof DDbDfr && doc.getDocStatus() != DModSysConsts.TS_XML_ST_ISS && doc.getDocStatus() != DModSysConsts.TS_XML_ST_PEN) {
                    throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL
                            + "El " + doc.getDocName() + " '" + doc.getDocNumber() + "' debe estar "
                            + "'" + client.getSession().readField(DModConsts.TS_XML_ST, new int[] { DModSysConsts.TS_XML_ST_ISS }, DDbRegistry.FIELD_NAME) + "' o "
                            + "'" + client.getSession().readField(DModConsts.TS_XML_ST, new int[] { DModSysConsts.TS_XML_ST_PEN }, DDbRegistry.FIELD_NAME) + "'.");
                }
                else {
                    cancel = doc.canAnnul(client.getSession());
                }
            }

            if (!cancel) {
                String message = doc instanceof DDbRegistry ? ((DDbRegistry) doc).getQueryResult() : "";
                throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL + "El " + doc.getDocName() + " '" + doc.getDocNumber() + "' no se puede cancelar."
                        + (message.isEmpty() ? "" : "\n" + message));
            }
            else {
                // Proceed with cancellation:

                DDbSysXmlSignatureProvider xsp = (DDbSysXmlSignatureProvider) client.getSession().readRegistry(DModConsts.CS_XSP, new int[] { settings.SignatureProviderId });
                DTrnAnnulParams annulParams = new DTrnAnnulParams(DTrnEmissionConsts.ACTION_ANNUL_CANCEL, true, confirmCancel);

                switch (requestSubtype) {
                    case DModSysConsts.TX_XMS_REQ_STP_REQ:
                        cancel = client.showMsgBoxConfirm("¿Desea cancelar el " + doc.getDocName() + " '" + doc.getDocNumber() + "'?\n"
                                + "ADVERTENCIA: ¡Esta acción no se puede revertir!") == JOptionPane.YES_OPTION;

                        if (cancel) {
                            if (xsr == null) {
                                DFormDpsCancelling formDpsCancelling = (DFormDpsCancelling) client.getSession().getModule(DModConsts.MOD_TRN).getForm(DModConsts.T_DPS_SIG, DModSysConsts.TX_XMS_REQ_TP_CAN, null);
                                formDpsCancelling.setRegistry((DDbRegistry) doc);
                                formDpsCancelling.setValue(DModConsts.CS_XSP, xsp);
                                formDpsCancelling.setVisible(true);

                                if (formDpsCancelling.getFormResult() != DGuiConsts.FORM_RESULT_OK) {
                                    cancel = false;
                                }
                                else {
                                    annulParams = (DTrnAnnulParams) formDpsCancelling.getValue(DModConsts.TX_DFR_ANNUL_PARAMS);
                                }
                            }
                        }
                        break;

                    case DModSysConsts.TX_XMS_REQ_STP_VER:
                        if (annulParams.ConfirmCancel) {
                            cancel = client.showMsgBoxConfirm("¿Desea confirmar la última solicitud de "
                                    + DTrnDfrUtils.getXmsRequestType(DModSysConsts.TX_XMS_REQ_TP_CAN) + " del " + doc.getDocName() + " '" + doc.getDocNumber() + "'?") == JOptionPane.YES_OPTION;
                        }
                        break;

                    default:
                        // do nothing
                }

                if (cancel) {
                    try {
                        client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR)); // XXX improve this!!!
                        
                        DTrnDfrUtils.cancelDfr(client.getSession(), doc, xsp.getPkXmlSignatureProviderId(), settings.SignatureCompanyBranchKey, requestSubtype, annulParams);
                        cancelled = true;
                    }
                    catch (Exception e) {
                        DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
                    }
                    finally {
                        client.getSession().notifySuscriptors(DModConsts.T_DFR);

                        if (!cancelled) {
                            client.showMsgBoxWarning("El " + doc.getDocName() + " '" + doc.getDocNumber() + "' "
                                    + "no fue anulado" + (annulParams.AnnulAction == DTrnEmissionConsts.ACTION_ANNUL_CANCEL ? " ni cancelado" : "") + ".");
                        }
                        else {
                            client.showMsgBoxWarning("El " + doc.getDocName() + " '" + doc.getDocNumber() + "' "
                                    + "ha sido anulado" + (annulParams.AnnulAction == DTrnEmissionConsts.ACTION_ANNUL_CANCEL ? " y cancelado" : "") + "."
                                    + (annulParams.AnnulAction == DTrnEmissionConsts.ACTION_ANNUL_CANCEL ? "\nQuedan " + DLibUtils.DecimalFormatInteger.format(getStampsAvailable(client.getSession(), xsp.getPkXmlSignatureProviderId(), settings.SignatureCompanyBranchKey)) + " timbres disponibles." : ""));
                        }

                        client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // XXX improve this!!!
                    }
                }
            }
            
            // free exclusive access:
            doc.freeLock(client.getSession(), cancelled ? DDbLock.LOCK_ST_FREED_UPDATE : DDbLock.LOCK_ST_FREED_EXCEPTION);
        }
    }

    /**
     * Cancels a DPS.
     * <b>WARNING:</b> Assure to manage properly current lock in Client if an exception occurs during processing.
     * Requests subtypes:
     * a) <b>Request</b> attempts to annul and cancel the provided registry.
     * b) <b>Verification</b> resumes an unfinished previous request or confirms a pending finished cancel request before the authority.
     * @param client GUI Client.
     * @param gridRow Grid row containing DPS.
     * @param requestSubtype Constants defined in DModSysConsts.TX_XMS_REQ_STP_...
     */
    public static void cancelDps(final DGuiClient client, final DGridRowView gridRow, final int requestSubtype) throws Exception {
        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            DDbDps dps = (DDbDps) client.getSession().readRegistry(DModConsts.T_DPS, gridRow.getRowPrimaryKey(), DDbConsts.MODE_VERBOSE);
            cancelDoc(client, dps, requestSubtype);
        }
    }

    /**
     * Cancels a DFR.
     * <b>WARNING:</b> Assure to manage properly current lock in Client if an exception occurs during processing.
     * Requests subtypes:
     * a) <b>Request</b> attempts to execute or confirm an annul and cancel request for provided registry.
     * b) <b>Verification</b> only resumes an unfinished previous request.
     * @param client GUI Client.
     * @param gridRow Grid row containing DFR.
     * @param requestSubtype Constants defined in DModSysConsts.TX_XMS_REQ_STP_...
     */
    public static void cancelDfr(final DGuiClient client, final DGridRowView gridRow, final int requestSubtype) throws Exception {
        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            DDbDfr dfr = (DDbDfr) client.getSession().readRegistry(DModConsts.T_DFR, gridRow.getRowPrimaryKey(), DDbConsts.MODE_VERBOSE);
            cancelDoc(client, dfr, requestSubtype);
        }
    }

    /**
     * Sends a document by mail.
     * @param client GUI Client.
     * @param gridRow Document.
     * @throws Exception 
     */
    private static void sendDoc(final DGuiClient client, final DTrnDoc doc) throws Exception {
        DDbConfigCompany configCompany = (DDbConfigCompany) client.getSession().getConfigCompany();

        if (configCompany.getFkDfrEmsTypeId() == DModSysConsts.CS_EMS_TP_NA) {
            client.showMsgBoxWarning("No está configurado en el sistema el envío vía correo-e.");
        }
        else {
            DDbDfr dfr = doc.getDfr();

            if (dfr == null || dfr.isRegistryNew()) {
                throw new Exception(DTrnEmissionConsts.MSG_DENIED_SEND + DDbConsts.ERR_MSG_REG_NOT_FOUND + "\n"
                        + "El registro XML del " + doc.getDocName() + " '" + doc.getDocNumber() + "' no existe.");
            }
            else if (dfr.getFkXmlStatusId() != DModSysConsts.TS_XML_ST_ISS) {
                throw new Exception(DTrnEmissionConsts.MSG_DENIED_SEND
                        + "El registro XML del " + doc.getDocName() + " '" + doc.getDocNumber() + "' debe estar "
                        + "'" + client.getSession().readField(DModConsts.TS_XML_ST, new int[] { DModSysConsts.TS_XML_ST_ISS }, DDbRegistry.FIELD_NAME) + "'.");
            }
            else if (dfr.getFkXmlAddendaTypeId() != DModSysConsts.TS_XML_ADD_TP_NA && dfr.getDocXmlAddenda().isEmpty()) {
                throw new Exception(DTrnEmissionConsts.MSG_DENIED_SEND
                        + "La addenda del registro XML del " + doc.getDocName() + " '" + doc.getDocNumber() + "' no ha sido incorporada.");
            }
            else {
                DDbBranchAddress branchAddress = (DDbBranchAddress) client.getSession().readRegistry(DModConsts.BU_ADD, doc.getBizPartnerBranchAddressKey(), DDbConsts.MODE_STEALTH);

                if (branchAddress.countEmails() == 0) {
                    client.showMsgBoxWarning("No se han definido correos-e para "
                            + "'" + client.getSession().readField(DModConsts.BU_BPR, doc.getBizPartnerKey(), DDbRegistry.FIELD_NAME).toString() + "',\n"
                            + "que es el receptor del " + doc.getDocName() + " '" + doc.getDocNumber() + "'.");
                }

                DDialogEmailSending dialog = new DDialogEmailSending(client, doc.getBizPartnerCategory());
                dialog.setBizPartner(client.getSession().readField(DModConsts.BU_BPR, doc.getBizPartnerKey(), DDbRegistry.FIELD_NAME).toString());
                dialog.setDocument(WordUtils.capitalizeFully(doc.getDocName()), doc.getDocNumber());
                dialog.setEmails(branchAddress.createEmails());
                dialog.setVisible(true);

                if (dialog.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                    try {
                        client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));    // XXX improve this!!!

                        // mail properties:

                        String from = configCompany.getDfrEmsFrom();
                        String subject = configCompany.getDfrEmsSubject() + " " + doc.getDocNumber();
                        String bodyText = configCompany.getDfrEmsBody();

                        ArrayList<DBprEmail> emails = dialog.getEmails();
                        ArrayList<String> recipients = new ArrayList<>();
                        for (DBprEmail email : emails) {
                            recipients.add(email.composeEmail());
                        }

                        DDbConfigBranch configBranch = (DDbConfigBranch) client.getSession().readRegistry(DModConsts.CU_CFG_BRA, doc.getCompanyBranchKey());
                        String fileNamePdf = dfr.getDocXmlName().replaceAll(".xml", ".pdf");
                        String filePathPdf = configBranch.getDfrDirectory() + fileNamePdf;
                        String fileNameXml = dfr.getDocXmlName();
                        String filePathXml = configBranch.getDfrDirectory() + fileNameXml;

                        if (configCompany.getFkDfrEmsTypeId() == DModSysConsts.CS_EMS_TP_GMAIL_API) {
                            File pdf = new File(filePathPdf);
                            File xml = new File(filePathXml);
                            DMessage message = new DMessage(from, subject, bodyText, DMailConsts.CONT_TP_TEXT_PLAIN, recipients);
                            message.getAttachments().add(pdf);
                            message.getAttachments().add(xml);
                            DGmail gmail = new DGmail();
                            gmail.sendMessage(message.createMimeMessage());
                        }
                        else {
                            String smtpHost = configCompany.getDfrEmsSmtpHost();
                            String smtpPort = "" + configCompany.getDfrEmsSmtpPort();
                            boolean isSmtpSsl = configCompany.isDfrEmsSmtpSslEnabled();

                            //Properties properties = System.getProperties();
                            Properties properties = new Properties();

                            properties.setProperty("mail.smtp.host", smtpHost);
                            properties.setProperty("mail.smtp.port", smtpPort);

                            switch (configCompany.getFkDfrEmsTypeId()) {
                                case DModSysConsts.CS_EMS_TP_OWN:
                                    if (isSmtpSsl) {
                                        properties.setProperty("mail.smtp.ssl.enable", "true");
                                    }
                                    break;

                                case DModSysConsts.CS_EMS_TP_LIVE:
                                case DModSysConsts.CS_EMS_TP_GMAIL_SMTP:
                                    /*
                                    Microsoft:
                                    - SMTP host: smtp.live.com, apparently deprecated by beginning of 2022
                                    - SMTP host: smtp.office365.com, since beginning of 2022
                                    - SMTP port: 587
                                    - SMTP SSL: 1 (true)
                                    Gmail:
                                    - SMTP host: smtp.gmail.com
                                    - SMTP port: 587
                                    - SMTP SSL: 1 (true)
                                    - NOTE:
                                        This sign-in technology is obsolete by May 30, 2022.
                                        Gmail access is now superseded by own implementation of Gmail API.
                                        Another option is to request an external app code for a Gmail account (go to Google account settings).
                                    */
                                    properties.setProperty("mail.smtp.auth", "true");
                                    properties.setProperty("mail.smtp.starttls.enable", "true");
                                    properties.setProperty("mail.smtp.ssl.trust", smtpHost);
                                    break;

                                default:
                                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                            }

                            final String emsName = configCompany.getDfrEmsName();
                            final String emsPswd = configCompany.getDfrEmsPassword();

                            Session session = Session.getInstance(properties,
                                    new Authenticator() {
                                        @Override
                                        protected PasswordAuthentication getPasswordAuthentication() {
                                            return new javax.mail.PasswordAuthentication(emsName, emsPswd);
                                        }
                                    });

                            // MIME message:

                            MimeMessage mimeMessage = new MimeMessage(session);
                            mimeMessage.setFrom(new InternetAddress(from));
                            mimeMessage.setSubject(subject);

                            for (String recipient : recipients) {
                                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                            }

                            mimeMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(from));

                            Multipart multipart = new MimeMultipart();

                            BodyPart bodyPart = new MimeBodyPart();
                            bodyPart.setText(bodyText);
                            multipart.addBodyPart(bodyPart);

                            // PDF file:

                            DataSource dataSourcePdf = new FileDataSource(filePathPdf);
                            bodyPart = new MimeBodyPart();
                            bodyPart.setDataHandler(new DataHandler(dataSourcePdf));
                            bodyPart.setFileName(fileNamePdf);
                            multipart.addBodyPart(bodyPart);

                            // XML file:

                            DataSource dataSourceXml = new FileDataSource(filePathXml);
                            bodyPart = new MimeBodyPart();
                            bodyPart.setDataHandler(new DataHandler(dataSourceXml));
                            bodyPart.setFileName(fileNameXml);
                            multipart.addBodyPart(bodyPart);

                            // compose and send mail:

                            mimeMessage.setContent(multipart);

                            Transport.send(mimeMessage);
                        }

                        // register sending:
                        DDbRegistry docSending = doc.createDocSending();
                        if (docSending != null && docSending instanceof DDbDpsSending) {
                            DDbDpsSending dpsSending = (DDbDpsSending) docSending;
                            DTrnUtils.populateEmails(dpsSending, emails);
                            dpsSending.save(client.getSession());
                        }

                        // notify suscriptors and user:
                        client.getSession().notifySuscriptors(DModConsts.T_DPS_SND);
                        client.showMsgBoxInformation("El " + doc.getDocName() + " '" + doc.getDocNumber() + "' ha sido enviado.");

                        // preserve mails if requested:
                        if (dialog.isPreserveEmailsSelected()) {
                            dialog.preserveEmails(branchAddress);
                        }
                    }
                    catch (MessagingException e) {
                        DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
                    }
                    catch (Exception e) {
                        DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
                    }
                    finally {
                        client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // XXX improve this!!!
                    }
                }
            }
        }
    }
    
    /**
     * Sends a DPS by mail.
     * @param client GUI Client.
     * @param gridRow Grid row containing DPS.
     */
    public static void sendDps(final DGuiClient client, final DGridRowView gridRow) {
        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            try {
                DDbDps dps = (DDbDps) client.getSession().readRegistry(DModConsts.T_DPS, gridRow.getRowPrimaryKey());
                sendDoc(client, dps);
            }
            catch (Exception e) {
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
            }
        }
    }

    /**
     * Sends a DFR by mail.
     * @param client GUI Client.
     * @param gridRow Grid row containing DFR.
     */
    public static void sendDfr(final DGuiClient client, final DGridRowView gridRow) {
        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            try {
                DDbDfr dfr = (DDbDfr) client.getSession().readRegistry(DModConsts.T_DFR, gridRow.getRowPrimaryKey());
                sendDoc(client, dfr);
            }
            catch (Exception e) {
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
            }
        }
    }
    
    /**
     * Checks the status of a document before the authority.
     * @param client GUI Client.
     * @param gridRow Document.
     * @throws Exception 
     */
    private static void checkDoc(final DGuiClient client, DTrnDoc doc) throws Exception {
        DDbDfr dfr = doc.getDfr();
        DTrnDfrUtilsHandler dfrUtilsHandler = new DTrnDfrUtilsHandler(client.getSession());
        DTrnDfrUtilsHandler.CfdiAckQuery ackQuery = dfrUtilsHandler.getCfdiSatStatus(dfr);

        client.showMsgBoxInformation(ackQuery.composeMessage());
    }
    
    /**
     * Checks the status of a DPS before the authority.
     * @param client GUI Client.
     * @param gridRow Grid row containing DPS.
     */
    public static void checkDps(final DGuiClient client, final DGridRowView gridRow) {
        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            try {
                DDbDps dps = (DDbDps) client.getSession().readRegistry(DModConsts.T_DPS, gridRow.getRowPrimaryKey(), DDbConsts.MODE_VERBOSE);
                checkDoc(client, dps);
            }
            catch (Exception e) {
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
            }
        }
    }
    
    /**
     * Checks the status of a DFR before the authority.
     * @param client GUI Client.
     * @param gridRow Grid row containing DFR.
     */
    public static void checkDfr(final DGuiClient client, final DGridRowView gridRow) {
        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            try {
                DDbDfr dfr = (DDbDfr) client.getSession().readRegistry(DModConsts.T_DFR, gridRow.getRowPrimaryKey(), DDbConsts.MODE_VERBOSE);
                checkDoc(client, dfr);
            }
            catch (Exception e) {
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
            }
        }
    }
    
    private static void writeXmlToDisk(final File file, final DDbDfr dfr, final int typeXml) throws Exception {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),"UTF8"));
        bw.write(typeXml == DModSysConsts.TS_DPS_ST_ISS ? dfr.getDocXml() : dfr.getCancelXml());
        bw.close();
    }
    
    private static void downloadDoc(final DGuiClient client, final DTrnDoc doc) throws Exception {
        DDbDfr dfr = doc.getDfr();

        if (dfr == null || dfr.isRegistryNew()) {
            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SEND + DDbConsts.ERR_MSG_REG_NOT_FOUND + "\n"
                    + "El registro XML del " + doc.getDocName() + " '" + doc.getDocNumber() + "' no existe.");
        }
        else if (dfr.getFkXmlStatusId() != DModSysConsts.TS_XML_ST_ISS) {
            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SEND
                    + "El registro XML del " + doc.getDocName() + " '" + doc.getDocNumber() + "' debe estar "
                    + "'" + client.getSession().readField(DModConsts.TS_XML_ST, new int[] { DModSysConsts.TS_XML_ST_ISS }, DDbRegistry.FIELD_NAME) + "'.");
        }
        else {
            client.getFileChooser().setSelectedFile(new File(dfr.getDocXmlName()));
            if (client.getFileChooser().showSaveDialog(client.getFrame()) == JFileChooser.APPROVE_OPTION) {
                File file = new File(client.getFileChooser().getSelectedFile().getAbsolutePath());
                writeXmlToDisk(file, dfr, DModSysConsts.TS_DPS_ST_ISS);
                client.showMsgBoxInformation(DGuiConsts.MSG_FILE_SAVED + file.getAbsolutePath());
            }
        }
    }
    
    public static void downloadDps(final DGuiClient client, final DGridRowView gridRow) {
        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            try {
                DDbDps dps = (DDbDps) client.getSession().readRegistry(DModConsts.T_DPS, gridRow.getRowPrimaryKey());
                downloadDoc(client, dps);
            }
            catch (Exception e) {
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
            }
        }
    }
    
    public static void downloadDfr(final DGuiClient client, final DGridRowView gridRow) {
        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            try {
                DDbDfr dfr = (DDbDfr) client.getSession().readRegistry(DModConsts.T_DFR, gridRow.getRowPrimaryKey());
                downloadDoc(client, dfr);
            }
            catch (Exception e) {
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
            }
        }
    }
}
