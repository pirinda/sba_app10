/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import java.awt.Cursor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import sba.gui.prt.DPrtUtils;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridRowView;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiSession;
import sba.lib.xml.DXmlUtils;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.bpr.db.DBprEmail;
import sba.mod.bpr.db.DDbBizPartner;
import sba.mod.bpr.db.DDbBranchAddress;
import sba.mod.cfg.db.DDbConfigBranch;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.cfg.db.DDbSysCurrency;
import sba.mod.cfg.db.DDbSysXmlSignatureProvider;
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
                throw new Exception("El " + DTrnEmissionConsts.PAC + " no permite la operación solicitada.\nSerá necesario realizar la acción de forma manual por otros medios.");
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
                        settings.SignatureCompanyBranchKey = null;    // value already set, just for consistence
                    }
                    else {
                        throw new Exception("El " + DTrnEmissionConsts.PAC + " no tiene timbres disponibles para completar la solicitud.");
                    }
                }
            }
        }
        
        return settings;
    }
    
    public static DDbXmlSignatureRequest getLastXmlSignatureRequest(final DGuiSession session, final int dfrId) throws SQLException, Exception {
        DDbXmlSignatureRequest xsr = null;
        String sql = "";
        ResultSet resultSet = null;
        
        sql = "SELECT COALESCE(MAX(id_req), 0) "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.T_XSR) + " "
                + "WHERE b_del = 0 AND id_dfr = " + dfrId + " ";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next() && resultSet.getInt(1) != DLibConsts.UNDEFINED) {
            xsr = (DDbXmlSignatureRequest) session.readRegistry(DModConsts.T_XSR, new int[] { dfrId, resultSet.getInt(1) });
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

    public static void printDps(final DGuiClient client, final DGridRowView gridRow, final int printMode) {
        boolean print = true;
        boolean printed = false;
        DDbDps dps = null;
        DDbDfr dfr = null;
        DDbDpsPrinting dpsPrinting = null;
        DDbDpsSeries series = null;
        DDbDpsSeriesNumber seriesNumber = null;
        DDbSysCurrency currency = null;
        DDbBizPartner bizPartner = null;
        DDbConfigBranch configBranch = null;
        DFormDpsPrinting formDpsPrinting = null;
        HashMap<String, Object> map = null;

        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            try {
                dps = (DDbDps) client.getSession().readRegistry(DModConsts.T_DPS, gridRow.getRowPrimaryKey());
                dfr = dps.getChildDfr();

                // Check if document number can be still changed:

                if (DTrnUtils.isDpsNumberAutomatic(dps.getDpsClassKey()) && ((DDbConfigBranch) client.getSession().getConfigBranch()).isDpsPrintingDialog() &&
                    (dfr == null || (dfr.isDfrCfdi() && dfr.getFkXmlStatusId() == DModSysConsts.TS_XML_ST_PEN))) {
                    formDpsPrinting = (DFormDpsPrinting) client.getSession().getModule(DModConsts.MOD_TRN).getForm(DModConsts.T_DPS_PRT, DLibConsts.UNDEFINED, null);
                    formDpsPrinting.setRegistry(dps);
                    formDpsPrinting.setVisible(true);

                    if (formDpsPrinting.getFormResult() != DGuiConsts.FORM_RESULT_OK) {
                        print = false;
                    }
                    else {
                        dpsPrinting = formDpsPrinting.getRegistry();
                        dpsPrinting.save(client.getSession());

                        if (dpsPrinting.hasDpsChanged()) {
                            client.getSession().notifySuscriptors(DModConsts.T_DPS);

                            seriesNumber = DTrnUtils.getDpsSeriesNumber(client.getSession(), dps.getDpsTypeKey(), dps.getSeries(), dps.getNumber());
                            if (seriesNumber != null && seriesNumber.getFkXmlTypeId() != DModSysConsts.TS_XML_TP_NA) {
                                dps = (DDbDps) client.getSession().readRegistry(DModConsts.T_DPS, gridRow.getRowPrimaryKey());
                                dfr = DTrnDfrUtils.createDfr(client.getSession(), dps, seriesNumber.getFkXmlTypeId());
                                dps.updateDfr(client.getSession(), dfr);
                            }
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
                                    bizPartner.getFiscalId().compareTo(((DDbConfigCompany) client.getSession().getConfigCompany()).getFiscalIdCountry()) == 0) {
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
                        dpsPrinting = dps.createDpsPrinting();
                        dpsPrinting.save(client.getSession());
                    }
                    catch (Exception e) {
                        DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
                    }
                    client.getSession().notifySuscriptors(DModConsts.T_DPS_PRT);
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
                        DPrtUtils.printReport(client.getSession(), DModConsts.TR_DPS_CFDI_33_CRP_10, DTrnDfrPrinting.createPrintingMap(client.getSession(), dfr));
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

    public static void signDps(final DGuiClient client, final DGridRowView gridRow, final int requestType) {
        boolean sign = true;
        boolean signed = false;
        DTrnEmissionSettings settings = null;
        DDbSysXmlSignatureProvider xsp = null;
        DDbXmlSignatureRequest xsr = null;
        DDbDps dps = null;
        DDbDfr dfr = null;
        DDbDpsSigning dpsSigning = null;
        DDbDpsSeriesNumber seriesNumber = null;
        DFormDpsSigning formDpsSigning = null;

        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            try {
                dps = (DDbDps) client.getSession().readRegistry(DModConsts.T_DPS, gridRow.getRowPrimaryKey(), DDbConsts.MODE_VERBOSE);
                
                // Check if XML signature is allowed:
                
                settings = checkXmlSignatureRequestAllowed(client.getSession(), DModSysConsts.TX_XMS_REQ_TP_SIG, requestType, dps.getChildDfr());
            }
            catch (Exception e) {
                e.printStackTrace();
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
            }
            
            if (settings != null && settings.RequestAllowed) {
                try {
                    // Check that there are not pending transactions:

                    xsp = (DDbSysXmlSignatureProvider) client.getSession().readRegistry(DModConsts.CS_XSP, new int[] { settings.SignatureProviderId });
                    xsr = getLastXmlSignatureRequest(client.getSession(), dps.getChildDfr().getPkDfrId());

                    if (requestType == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                        // Request signature:

                        if (xsr != null && xsr.getRequestStatus() != DModSysConsts.TX_XMS_REQ_ST_FIN) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN +
                                    "\nHay una transacción pendiente para el documento.");
                        }
                        else {
                            xsr = null; // new request about to be created
                        }
                    }
                    else {
                        // Verify signature:

                        if (xsr == null || xsr.getRequestStatus() == DModSysConsts.TX_XMS_REQ_ST_FIN) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN +
                                    "\nNo hay una transacción pendiente para el documento.");
                        }
                    }

                    // Check if document can be signed:

                    if (xsr == null) {
                        // Check and confirm document details before first signature request:

                        dfr = dps.getChildDfr();

                        if (dfr == null || dfr.isRegistryNew()) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN + DDbConsts.ERR_MSG_REG_NOT_FOUND +
                                    "\nEl registro XML del documento no existe.");
                        }
                        else if (!dfr.isDfrCfdi()) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN + "El tipo del registro XML del documento debe ser CFDI: " +
                                    "'" + client.getSession().readField(DModConsts.TS_XML_TP, new int[] { DModSysConsts.TS_XML_TP_CFDI_32 }, DDbRegistry.FIELD_NAME) + "' o " +
                                    "'" + client.getSession().readField(DModConsts.TS_XML_TP, new int[] { DModSysConsts.TS_XML_TP_CFDI_33 }, DDbRegistry.FIELD_NAME) + "'.");
                        }
                        else if (!DLibUtils.belongsTo(dfr.getFkXmlStatusId(), new int[] { DModSysConsts.TS_XML_ST_PEN })) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN + "El estatus del registro XML del documento debe ser '" +
                                    client.getSession().readField(DModConsts.TS_XML_ST, new int[] { DModSysConsts.TS_XML_ST_PEN }, DDbRegistry.FIELD_NAME) + "'.");
                        }
                        else if (dps.getFkDpsStatusId() != DModSysConsts.TS_DPS_ST_ISS) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN + "El estatus del documento debe ser '" +
                                    client.getSession().readField(DModConsts.TS_DPS_ST, new int[] { DModSysConsts.TS_DPS_ST_ISS }, DDbRegistry.FIELD_NAME) + "'.");
                        }
                        else if (DLibTimeUtils.convertToDateOnly(dps.getDate()).before(DLibTimeUtils.convertToDateOnly(client.getSession().getSystemDate()))) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN + "La fecha del documento '" + DLibUtils.DateFormatDate.format(dps.getDate()) +
                                    "' no puede ser anterior al día de hoy '" + DLibUtils.DateFormatDate.format(client.getSession().getSystemDate()) + "'.");
                        }
                        else {
                            if (!DLibTimeUtils.isSameDate(dps.getDate(), client.getSession().getSystemDate())) {
                                sign = client.showMsgBoxConfirm("La fecha del documento '" + DLibUtils.DateFormatDate.format(dps.getDate()) +
                                        "' es posterior al día de hoy '" + DLibUtils.DateFormatDate.format(client.getSession().getSystemDate()) + "'.\n" +
                                        "¿Está seguro que desea timbrar el documento '" + dps.getDpsNumber() + "'?") == JOptionPane.YES_OPTION;
                            }

                            if (sign) {
                                // Check if document number can be changed:

                                if (DTrnUtils.isDpsNumberAutomatic(dps.getDpsClassKey()) && ((DDbConfigBranch) client.getSession().getConfigBranch()).isDpsPrintingDialog()) {
                                    formDpsSigning = (DFormDpsSigning) client.getSession().getModule(DModConsts.MOD_TRN).getForm(DModConsts.T_DPS_SIG, DModSysConsts.TX_XMS_REQ_TP_SIG, null);
                                    formDpsSigning.setRegistry(dps);
                                    formDpsSigning.setVisible(true);

                                    if (formDpsSigning.getFormResult() != DGuiConsts.FORM_RESULT_OK) {
                                        sign = false;
                                    }
                                    else {
                                        dpsSigning = formDpsSigning.getRegistry();
                                        dpsSigning.save(client.getSession());

                                        if (dpsSigning.hasDpsChanged()) {
                                            client.getSession().notifySuscriptors(DModConsts.T_DPS);

                                            seriesNumber = DTrnUtils.getDpsSeriesNumber(client.getSession(), dps.getDpsTypeKey(), dps.getSeries(), dps.getNumber());
                                            if (seriesNumber != null && seriesNumber.getFkXmlTypeId() != DModSysConsts.TS_XML_TP_NA) {
                                                dps = (DDbDps) client.getSession().readRegistry(DModConsts.T_DPS, gridRow.getRowPrimaryKey());
                                                dfr = DTrnDfrUtils.createDfr(client.getSession(), dps, seriesNumber.getFkXmlTypeId());
                                                dps.updateDfr(client.getSession(), dfr);
                                            }
                                        }
                                    }
                                }
                                else {
                                    sign = client.showMsgBoxConfirm("¿Está seguro que desea timbrar el documento '" + dps.getDpsNumber() + "'?") == JOptionPane.YES_OPTION;
                                }
                            }
                        }
                    }

                    if (sign) {
                        try {
                            client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));    // XXX improve this!!!

                            DTrnDfrUtils.signDfr(client.getSession(), dps.getChildDfr(), dps, xsp.getPkXmlSignatureProviderId(), settings.SignatureCompanyBranchKey, requestType);
                            signed = true;
                        }
                        catch (Exception e) {
                            DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
                        }
                        finally {
                            client.getSession().notifySuscriptors(DModConsts.T_DPS_SIG);

                            if (!signed) {
                                client.showMsgBoxWarning("El documento '" + dps.getDpsNumber() + "' no ha sido timbrado.");
                            }
                            else {
                                client.showMsgBoxInformation("El documento '" + dps.getDpsNumber() + "' ha sido timbrado.\n" +
                                        "Quedan " + DLibUtils.DecimalFormatInteger.format(getStampsAvailable(client.getSession(), xsp.getPkXmlSignatureProviderId(), settings.SignatureCompanyBranchKey)) + " timbres disponibles.");
                                
                                sendDps(client, gridRow);
                            }

                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // XXX improve this!!!
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
                }
            }
        }
    }

    public static void signDfr(final DGuiClient client, final DGridRowView gridRow, final int requestType) {
        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            DDbDfr dfr = null;
            DTrnEmissionSettings settings = null;
            
            try {
                dfr = (DDbDfr) client.getSession().readRegistry(DModConsts.T_DFR, gridRow.getRowPrimaryKey(), DDbConsts.MODE_VERBOSE);
                
                // Check if XML signature is allowed:
                
                settings = checkXmlSignatureRequestAllowed(client.getSession(), DModSysConsts.TX_XMS_REQ_TP_SIG, requestType, dfr);
            }
            catch (Exception e) {
                e.printStackTrace();
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
            }
            
            if (settings != null && settings.RequestAllowed) {
                boolean sign = true;
                
                try {
                    // Check that there are not pending transactions:

                    DDbSysXmlSignatureProvider xsp = (DDbSysXmlSignatureProvider) client.getSession().readRegistry(DModConsts.CS_XSP, new int[] { settings.SignatureProviderId });
                    DDbXmlSignatureRequest xsr = getLastXmlSignatureRequest(client.getSession(), dfr.getPkDfrId());

                    if (requestType == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                        // Request signature:

                        if (xsr != null && xsr.getRequestStatus() != DModSysConsts.TX_XMS_REQ_ST_FIN) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN +
                                    "\nHay una transacción pendiente para el comprobante.");
                        }
                        else {
                            xsr = null; // new request about to be created
                        }
                    }
                    else {
                        // Verify signature:

                        if (xsr == null || xsr.getRequestStatus() == DModSysConsts.TX_XMS_REQ_ST_FIN) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN +
                                    "\nNo hay una transacción pendiente para el comprobante.");
                        }
                    }

                    // Check if document can be signed:

                    if (xsr == null) {
                        // Check and confirm document details before first signature request:

                        if (dfr.isRegistryNew()) { // really needed?
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN + DDbConsts.ERR_MSG_REG_NOT_FOUND +
                                    "\nEl registro XML del comprobante no existe.");
                        }
                        else if (dfr.getFkXmlTypeId() != DModSysConsts.TS_XML_TP_CFDI_33) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN + "El tipo del registro XML del comprobante debe ser " +
                                    "'" + client.getSession().readField(DModConsts.TS_XML_TP, new int[] { DModSysConsts.TS_XML_TP_CFDI_33 }, DDbRegistry.FIELD_NAME) + "'.");
                        }
                        else if (!DLibUtils.belongsTo(dfr.getFkXmlStatusId(), new int[] { DModSysConsts.TS_XML_ST_PEN })) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN + "El estatus del registro XML del comprobante debe ser '" +
                                    client.getSession().readField(DModConsts.TS_XML_ST, new int[] { DModSysConsts.TS_XML_ST_PEN }, DDbRegistry.FIELD_NAME) + "'.");
                        }
                        else if (DLibTimeUtils.convertToDateOnly(dfr.getDocTs()).before(DLibTimeUtils.convertToDateOnly(client.getSession().getSystemDate()))) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN + "La fecha del comprobante '" + DLibUtils.DateFormatDate.format(dfr.getDocTs()) +
                                    "' no puede ser anterior al día de hoy '" + DLibUtils.DateFormatDate.format(client.getSession().getSystemDate()) + "'.");
                        }
                        else {
                            if (!DLibTimeUtils.isSameDate(dfr.getDocTs(), client.getSession().getSystemDate())) {
                                sign = client.showMsgBoxConfirm("La fecha del comprobante '" + DLibUtils.DateFormatDate.format(dfr.getDocTs()) +
                                        "' es posterior al día de hoy '" + DLibUtils.DateFormatDate.format(client.getSession().getSystemDate()) + "'.\n" +
                                        "¿Está seguro que desea timbrar el comprobante '" + dfr.getDfrNumber() + "'?") == JOptionPane.YES_OPTION;
                            }

                            if (sign) {
                                sign = client.showMsgBoxConfirm("¿Está seguro que desea timbrar el comprobante '" + dfr.getDfrNumber() + "'?") == JOptionPane.YES_OPTION;
                            }
                        }
                    }

                    if (sign) {
                        boolean signed = false;
                        
                        try {
                            client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));    // XXX improve this!!!

                            DTrnDfrUtils.signDfr(client.getSession(), dfr, dfr, xsp.getPkXmlSignatureProviderId(), settings.SignatureCompanyBranchKey, requestType);
                            signed = true;
                        }
                        catch (Exception e) {
                            DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
                        }
                        finally {
                            client.getSession().notifySuscriptors(DModConsts.T_DFR);

                            if (!signed) {
                                client.showMsgBoxWarning("El comprobante '" + dfr.getDfrNumber() + "' no ha sido timbrado.");
                            }
                            else {
                                client.showMsgBoxInformation("El comprobante '" + dfr.getDfrNumber() + "' ha sido timbrado.\n" +
                                        "Quedan " + DLibUtils.DecimalFormatInteger.format(getStampsAvailable(client.getSession(), xsp.getPkXmlSignatureProviderId(), settings.SignatureCompanyBranchKey)) + " timbres disponibles.");
                                
                                sendDfr(client, gridRow);
                            }

                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // XXX improve this!!!
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
                }
            }
        }
    }

    public static void cancelDps(final DGuiClient client, final DGridRowView gridRow, final int requestType) {
        boolean cancel = true;
        boolean cancelled = false;
        DTrnEmissionSettings settings = null;
        DDbSysXmlSignatureProvider xsp = null;
        DDbXmlSignatureRequest xsr = null;
        DDbDps dps = null;
        DDbDfr dfr = null;
        DFormDpsCancelling formDpsCancelling = null;
        int action = DTrnEmissionConsts.ANNUL_CANCEL;
        int lastRequestStatus = DLibConsts.UNDEFINED;

        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            try {
                dps = (DDbDps) client.getSession().readRegistry(DModConsts.T_DPS, gridRow.getRowPrimaryKey(), DDbConsts.MODE_VERBOSE);
                
                // Check if XML cancellation is allowed:
                
                settings = checkXmlSignatureRequestAllowed(client.getSession(), DModSysConsts.TX_XMS_REQ_TP_CAN, requestType, dps.getChildDfr());
            }
            catch (Exception e) {
                e.printStackTrace();
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
            }
            
            if (settings != null && settings.RequestAllowed) {
                try {
                    // Check that there are not pending transactions:

                    xsp = (DDbSysXmlSignatureProvider) client.getSession().readRegistry(DModConsts.CS_XSP, new int[] { settings.SignatureProviderId });
                    xsr = getLastXmlSignatureRequest(client.getSession(), dps.getChildDfr().getPkDfrId());

                    if (requestType == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                        // Request signature:

                        if (xsr != null && xsr.getRequestStatus() != DModSysConsts.TX_XMS_REQ_ST_FIN) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL +
                                    "\nHay una transacción pendiente para el documento.");
                        }
                        else {
                            if (xsr != null) {
                                lastRequestStatus = xsr.getRequestStatus();
                            }
                            xsr = null; // new request about to be created
                        }
                    }
                    else {
                        // Verify signature:

                        if (xsr == null || xsr.getRequestStatus() == DModSysConsts.TX_XMS_REQ_ST_FIN) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL +
                                    "\nNo hay una transacción pendiente para el documento.");
                        }
                    }

                    // Check if document can be cancelled:

                    if (xsr == null) {
                        // Check and confirm document details before first signature request:

                        dfr = dps.getChildDfr();

                        if (dfr == null || dfr.isRegistryNew()) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL + DDbConsts.ERR_MSG_REG_NOT_FOUND +
                                    "\nEl registro XML del documento no existe.");
                        }
                        else if (!dfr.isDfrCfdi()) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN + "El tipo del registro XML del documento debe ser CFDI: " +
                                    "'" + client.getSession().readField(DModConsts.TS_XML_TP, new int[] { DModSysConsts.TS_XML_TP_CFDI_32 }, DDbRegistry.FIELD_NAME) + "' o " +
                                    "'" + client.getSession().readField(DModConsts.TS_XML_TP, new int[] { DModSysConsts.TS_XML_TP_CFDI_33 }, DDbRegistry.FIELD_NAME) + "'.");
                        }
                        else if (!DLibUtils.belongsTo(dfr.getFkXmlStatusId(), new int[] { DModSysConsts.TS_XML_ST_PEN, DModSysConsts.TS_XML_ST_ISS })) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL + "El estatus del registro XML del documento debe ser '" +
                                    client.getSession().readField(DModConsts.TS_XML_ST, new int[] { DModSysConsts.TS_XML_ST_PEN }, DDbRegistry.FIELD_NAME) + "' o '" +
                                    client.getSession().readField(DModConsts.TS_XML_ST, new int[] { DModSysConsts.TS_XML_ST_ISS }, DDbRegistry.FIELD_NAME) + "'.");
                        }
                        else if (dps.getFkDpsStatusId() != DModSysConsts.TS_DPS_ST_ISS) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL + "El estatus del documento debe ser '" +
                                    client.getSession().readField(DModConsts.TS_DPS_ST, new int[] { DModSysConsts.TS_DPS_ST_ISS }, DDbRegistry.FIELD_NAME) + "'.");
                        }
                        else if (DLibTimeUtils.convertToDateOnly(dps.getDate()).after(DLibTimeUtils.convertToDateOnly(client.getSession().getSystemDate()))) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL + "La fecha del documento '" + DLibUtils.DateFormatDate.format(dps.getDate()) +
                                    "' no puede ser posterior al día de hoy '" + DLibUtils.DateFormatDate.format(client.getSession().getSystemDate()) + "'.");
                        }
                        else {
                            // Check if document can be disabled (in this case, annuled):

                            cancel = dps.canAnnul(client.getSession());
                        }
                    }

                    if (cancel) {
                        boolean retryCancel = false;
                        
                        if (requestType == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                            cancel = client.showMsgBoxConfirm("¿Está seguro que desea cancelar el documento '" + dps.getDpsNumber() + "'?") == JOptionPane.YES_OPTION;

                            if (cancel) {
                                if (xsr == null || lastRequestStatus == DModSysConsts.TX_XMS_REQ_ST_STA) {
                                    formDpsCancelling = (DFormDpsCancelling) client.getSession().getModule(DModConsts.MOD_TRN).getForm(DModConsts.T_DPS_SIG, DModSysConsts.TX_XMS_REQ_TP_CAN, null);
                                    formDpsCancelling.setRegistry(dps);
                                    formDpsCancelling.setValue(DModConsts.CS_XSP, xsp);
                                    formDpsCancelling.setVisible(true);

                                    if (formDpsCancelling.getFormResult() != DGuiConsts.FORM_RESULT_OK) {
                                        cancel = false;
                                    }
                                    else {
                                        action = (Integer) formDpsCancelling.getValue(DModSysConsts.TX_XMS_REQ_TP_CAN); // "annul" or "anull and cancel"
                                        retryCancel = (Boolean) formDpsCancelling.getValue(DFormDpsCancelling.RETRY_CANCEL); // yes or no
                                    }
                                }
                            }
                        }

                        if (cancel) {
                            try {
                                client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));    // XXX improve this!!!

                                DTrnDfrUtils.cancelDfr(client.getSession(), dps.getChildDfr(), dps, xsp.getPkXmlSignatureProviderId(), settings.SignatureCompanyBranchKey, requestType, action, retryCancel);
                                cancelled = true;
                            }
                            catch (Exception e) {
                                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
                            }
                            finally {
                                client.getSession().notifySuscriptors(DModConsts.T_DPS_SIG);

                                if (!cancelled) {
                                    client.showMsgBoxWarning("El documento '" + dps.getDpsNumber() + "' no ha sido cancelado.");
                                }
                                else {
                                    client.showMsgBoxInformation("El documento '" + dps.getDpsNumber() + "' ha sido cancelado.\n" +
                                            "Quedan " + DLibUtils.DecimalFormatInteger.format(getStampsAvailable(client.getSession(), xsp.getPkXmlSignatureProviderId(), settings.SignatureCompanyBranchKey)) + " timbres disponibles.");
                                }

                                client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // XXX improve this!!!
                            }
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
                }
            }
        }
    }

    public static void cancelDfr(final DGuiClient client, final DGridRowView gridRow, final int requestType) {
        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            DDbDfr dfr = null;
            DTrnEmissionSettings settings = null;
            
            try {
                dfr = (DDbDfr) client.getSession().readRegistry(DModConsts.T_DFR, gridRow.getRowPrimaryKey(), DDbConsts.MODE_VERBOSE);
                
                // Check if XML cancellation is allowed:
                
                settings = checkXmlSignatureRequestAllowed(client.getSession(), DModSysConsts.TX_XMS_REQ_TP_CAN, requestType, dfr);
            }
            catch (Exception e) {
                e.printStackTrace();
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
            }
            
            if (settings != null && settings.RequestAllowed) {
                boolean cancel = true;
                
                try {
                    // Check that there are not pending transactions:

                    DDbSysXmlSignatureProvider xsp = (DDbSysXmlSignatureProvider) client.getSession().readRegistry(DModConsts.CS_XSP, new int[] { settings.SignatureProviderId });
                    DDbXmlSignatureRequest xsr = getLastXmlSignatureRequest(client.getSession(), dfr.getPkDfrId());

                    if (requestType == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                        // Request signature:

                        if (xsr != null && xsr.getRequestStatus() != DModSysConsts.TX_XMS_REQ_ST_FIN) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL +
                                    "\nHay una transacción pendiente para el comprobante.");
                        }
                        else {
                            xsr = null; // new request about to be created
                        }
                    }
                    else {
                        // Verify signature:

                        if (xsr == null || xsr.getRequestStatus() == DModSysConsts.TX_XMS_REQ_ST_FIN) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL +
                                    "\nNo hay una transacción pendiente para el comprobante.");
                        }
                    }

                    // Check if document can be cancelled:

                    if (xsr == null) {
                        // Check and confirm document details before first signature request:

                        if (dfr.isRegistryNew()) { // really needed?
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL + DDbConsts.ERR_MSG_REG_NOT_FOUND +
                                    "\nEl registro XML del comprobante no existe.");
                        }
                        else if (dfr.getFkXmlTypeId() != DModSysConsts.TS_XML_TP_CFDI_33) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN + "El tipo del registro XML del comprobante debe ser " +
                                    "'" + client.getSession().readField(DModConsts.TS_XML_TP, new int[] { DModSysConsts.TS_XML_TP_CFDI_33 }, DDbRegistry.FIELD_NAME) + "'.");
                        }
                        else if (!DLibUtils.belongsTo(dfr.getFkXmlStatusId(), new int[] { DModSysConsts.TS_XML_ST_PEN, DModSysConsts.TS_XML_ST_ISS })) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL + "El estatus del registro XML del comprobante debe ser '" +
                                    client.getSession().readField(DModConsts.TS_XML_ST, new int[] { DModSysConsts.TS_XML_ST_PEN }, DDbRegistry.FIELD_NAME) + "' o '" +
                                    client.getSession().readField(DModConsts.TS_XML_ST, new int[] { DModSysConsts.TS_XML_ST_ISS }, DDbRegistry.FIELD_NAME) + "'.");
                        }
                        else if (DLibTimeUtils.convertToDateOnly(dfr.getDocTs()).after(DLibTimeUtils.convertToDateOnly(client.getSession().getSystemDate()))) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL + "La fecha del comprobante '" + DLibUtils.DateFormatDate.format(dfr.getDocTs()) +
                                    "' no puede ser posterior al día de hoy '" + DLibUtils.DateFormatDate.format(client.getSession().getSystemDate()) + "'.");
                        }
                        else {
                            // Check if document can be disabled:

                            cancel = dfr.canDisable(client.getSession());
                        }
                    }

                    if (cancel) {
                        int action = DTrnEmissionConsts.ANNUL_CANCEL;
                        cancel = client.showMsgBoxConfirm("¿Está seguro que desea cancelar el comprobante '" + dfr.getDfrNumber() + "'?") == JOptionPane.YES_OPTION;

                        if (cancel) {
                            boolean cancelled = false;
                            
                            try {
                                client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));    // XXX improve this!!!

                                DTrnDfrUtils.cancelDfr(client.getSession(), dfr, dfr, xsp.getPkXmlSignatureProviderId(), settings.SignatureCompanyBranchKey, requestType, action, true); // "retry cancel" is implicit!
                                cancelled = true;
                            }
                            catch (Exception e) {
                                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
                            }
                            finally {
                                client.getSession().notifySuscriptors(DModConsts.T_DFR);

                                if (!cancelled) {
                                    client.showMsgBoxWarning("El comprobante '" + dfr.getDfrNumber() + "' no ha sido cancelado.");
                                }
                                else {
                                    client.showMsgBoxInformation("El comprobante '" + dfr.getDfrNumber() + "' ha sido cancelado.\n" +
                                            "Quedan " + DLibUtils.DecimalFormatInteger.format(getStampsAvailable(client.getSession(), xsp.getPkXmlSignatureProviderId(), settings.SignatureCompanyBranchKey)) + " timbres disponibles.");
                                }

                                client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // XXX improve this!!!
                            }
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
                }
            }
        }
    }

    public static void sendDps(final DGuiClient client, final DGridRowView gridRow) {
        DDbDps dps = null;
        DDbDfr dfr = null;
        DDbBranchAddress branchAddress = null;
        DDbConfigCompany configCompany = (DDbConfigCompany) client.getSession().getConfigCompany();
        final String name = configCompany.getDfrEmsName();
        final String password = configCompany.getDfrEmsPassword();

        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else if (configCompany.getFkDfrEmsTypeId() == DModSysConsts.CS_EMS_TP_NA) {
            client.showMsgBoxWarning("No está configurado el envío de documentos electrónicos vía correo-e.");
        }
        else {
            try {
                dps = (DDbDps) client.getSession().readRegistry(DModConsts.T_DPS, gridRow.getRowPrimaryKey());
                dfr = dps.getChildDfr();

                if (dfr == null || dfr.isRegistryNew()) {
                    throw new Exception(DTrnEmissionConsts.MSG_DENIED_SEND + DDbConsts.ERR_MSG_REG_NOT_FOUND + "\nEl registro XML del documento no existe.");
                }
                else if (dfr.getFkXmlStatusId() != DModSysConsts.TS_XML_ST_ISS) {
                    throw new Exception(DTrnEmissionConsts.MSG_DENIED_SEND + "El estatus del registro XML del documento debe ser '" + client.getSession().readField(DModConsts.TS_XML_ST, new int[] { DModSysConsts.TS_XML_ST_ISS }, DDbRegistry.FIELD_NAME) + "'.");
                }
                else if (dfr.getFkXmlAddendaTypeId() != DModSysConsts.TS_XML_ADD_TP_NA && dfr.getDocXmlAddenda().isEmpty()) {
                    throw new Exception(DTrnEmissionConsts.MSG_DENIED_SEND + "La addenda del registro XML del documento no ha sido incorporada.");
                }
                else {
                    branchAddress = (DDbBranchAddress) client.getSession().readRegistry(DModConsts.BU_ADD, dps.getBizPartnerBranchAddressKey(), DDbConsts.MODE_STEALTH);
                    
                    if (branchAddress.countEmails() == 0) {
                        client.showMsgBoxWarning("No se han definido correos-e para "
                                + "'" + client.getSession().readField(DModConsts.BU_BPR, dps.getBizPartnerKey(), DDbRegistry.FIELD_NAME).toString() + "',\n"
                                + "receptor del documento '" + dps.getDpsNumber() + "'.");
                    }
                    
                    DDialogEmailSending dialog = new DDialogEmailSending(client, DTrnUtils.getBizPartnerClassByDpsCategory(dps.getFkDpsCategoryId()));
                    dialog.setBizPartner(client.getSession().readField(DModConsts.BU_BPR, dps.getBizPartnerKey(), DDbRegistry.FIELD_NAME).toString());
                    dialog.setDocument("Documento", dps.getDpsNumber());
                    dialog.setEmails(branchAddress.createEmails());
                    dialog.setVisible(true);

                    if (dialog.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                        try {
                            client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));    // XXX improve this!!!

                            Properties properties = System.getProperties();

                            String from = configCompany.getDfrEmsFrom();

                            switch (configCompany.getFkDfrEmsTypeId()) {
                                case DModSysConsts.CS_EMS_TP_OWN:
                                    properties.setProperty("mail.smtp.host", configCompany.getDfrEmsSmtpHost());
                                    properties.setProperty("mail.smtp.port", "" + configCompany.getDfrEmsSmtpPort());
                                    if (configCompany.isDfrEmsSmtpSslEnabled()) {
                                        properties.setProperty("mail.smtp.ssl.enable", "true");
                                    }
                                    break;
                                case DModSysConsts.CS_EMS_TP_LIVE:
                                    properties.setProperty("mail.smtp.host", "smtp.live.com");
                                    properties.setProperty("mail.smtp.port", "587");
                                    properties.setProperty("mail.smtp.auth", "true");
                                    properties.setProperty("mail.smtp.starttls.enable", "true");
                                    properties.setProperty("mail.smtp.ssl.trust", "smtp.live.com");
                                    break;
                                case DModSysConsts.CS_EMS_TP_GMAIL:
                                    properties.setProperty("mail.smtp.host", "smtp.gmail.com");
                                    properties.setProperty("mail.smtp.port", "587");
                                    properties.setProperty("mail.smtp.auth", "true");
                                    properties.setProperty("mail.smtp.starttls.enable", "true");
                                    properties.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");
                                    break;
                                default:
                                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                            }

                            Session session = Session.getInstance(properties,
                                    new Authenticator() {
                                        protected PasswordAuthentication getPasswordAuthentication() {
                                            return new javax.mail.PasswordAuthentication(name, password);
                                        }
                                    });

                            MimeMessage mimeMessage = new MimeMessage(session);
                            mimeMessage.setFrom(new InternetAddress(from));

                            ArrayList<DBprEmail> emails = dialog.getEmails();
                            for (DBprEmail email : emails) {
                                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email.composeEmail()));
                            }

                            mimeMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(from));

                            mimeMessage.setSubject(configCompany.getDfrEmsSubject() + " " + dps.getDpsNumber());

                            Multipart multipart = new MimeMultipart();
                            BodyPart bodyPart = null;

                            bodyPart = new MimeBodyPart();
                            bodyPart.setText(configCompany.getDfrEmsBody());
                            multipart.addBodyPart(bodyPart);

                            String filePath = "";
                            String fileName = "";
                            DataSource dataSource = null;
                            DDbConfigBranch configBranch = (DDbConfigBranch) client.getSession().readRegistry(DModConsts.CU_CFG_BRA, dps.getCompanyBranchKey());

                            // PDF file:

                            fileName = dfr.getDocXmlName().replaceAll(".xml", ".pdf");
                            filePath = configBranch.getDfrDirectory() + fileName;

                            dataSource = new FileDataSource(filePath);
                            bodyPart = new MimeBodyPart();
                            bodyPart.setDataHandler(new DataHandler(dataSource));
                            bodyPart.setFileName(fileName);
                            multipart.addBodyPart(bodyPart);

                            // XML file:

                            fileName = dfr.getDocXmlName();
                            filePath = configBranch.getDfrDirectory() + fileName;

                            dataSource = new FileDataSource(filePath);
                            bodyPart = new MimeBodyPart();
                            bodyPart.setDataHandler(new DataHandler(dataSource));
                            bodyPart.setFileName(fileName);
                            multipart.addBodyPart(bodyPart);

                            mimeMessage.setContent(multipart);

                            Transport.send(mimeMessage);

                            // register sending:
                            DDbDpsSending sending = dps.createDpsSending();
                            DTrnUtils.populateEmails(sending, emails);
                            sending.save(client.getSession());

                            // notify suscriptors and user:
                            client.getSession().notifySuscriptors(DModConsts.T_DPS_SND);
                            client.showMsgBoxInformation("El documento '" + dps.getDpsNumber() + "' ha sido enviado.");

                            // preserve mails if requested:
                            if (dialog.isPreserveEmailsSelected()) {
                                dialog.preserveEmails(branchAddress);
                            }
                        }
                        catch (MessagingException e) {
                            e.printStackTrace();
                            DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
                        }
                        finally {
                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // XXX improve this!!!
                        }
                    }
                }
            }
            catch (Exception e) {
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
            }
        }
    }

    public static void sendDfr(final DGuiClient client, final DGridRowView gridRow) {
        DDbDfr dfr = null;
        DDbBranchAddress branchAddress = null;
        DDbConfigCompany configCompany = (DDbConfigCompany) client.getSession().getConfigCompany();
        final String name = configCompany.getDfrEmsName();
        final String password = configCompany.getDfrEmsPassword();

        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else if (configCompany.getFkDfrEmsTypeId() == DModSysConsts.CS_EMS_TP_NA) {
            client.showMsgBoxWarning("No está configurado el envío de comprobantes vía correo-e.");
        }
        else {
            try {
                dfr = (DDbDfr) client.getSession().readRegistry(DModConsts.T_DFR, gridRow.getRowPrimaryKey());

                if (dfr == null || dfr.isRegistryNew()) {
                    throw new Exception(DTrnEmissionConsts.MSG_DENIED_SEND + DDbConsts.ERR_MSG_REG_NOT_FOUND + "\nEl registro XML del comprobante no existe.");
                }
                else if (dfr.getFkXmlStatusId() != DModSysConsts.TS_XML_ST_ISS) {
                    throw new Exception(DTrnEmissionConsts.MSG_DENIED_SEND + "El estatus del registro XML del comprobante debe ser '" + client.getSession().readField(DModConsts.TS_XML_ST, new int[] { DModSysConsts.TS_XML_ST_ISS }, DDbRegistry.FIELD_NAME) + "'.");
                }
                else if (dfr.getFkXmlAddendaTypeId() != DModSysConsts.TS_XML_ADD_TP_NA && dfr.getDocXmlAddenda().isEmpty()) {
                    throw new Exception(DTrnEmissionConsts.MSG_DENIED_SEND + "La addenda del registro XML del comprobante no ha sido incorporada.");
                }
                else {
                    branchAddress = (DDbBranchAddress) client.getSession().readRegistry(DModConsts.BU_ADD, new int[] { dfr.getFkBizPartnerId(), 1, 1 }, DDbConsts.MODE_STEALTH);

                    if (branchAddress.countEmails() == 0) {
                        client.showMsgBoxWarning("No se han definido correos-e para "
                                + "'" + client.getSession().readField(DModConsts.BU_BPR, dfr.getBizPartnerKey(), DDbRegistry.FIELD_NAME).toString() + "',\n"
                                + "receptor del comprobante '" + dfr.getDfrNumber() + "'.");
                    }

                    DDialogEmailSending dialog = new DDialogEmailSending(client, DModSysConsts.BS_BPR_CL_CUS);
                    dialog.setBizPartner(client.getSession().readField(DModConsts.BU_BPR, dfr.getBizPartnerKey(), DDbRegistry.FIELD_NAME).toString());
                    dialog.setDocument("Comprobante", dfr.getDfrNumber());
                    dialog.setEmails(branchAddress.createEmails());
                    dialog.setVisible(true);

                    if (dialog.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                        try {
                            client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));    // XXX improve this!!!

                            Properties properties = System.getProperties();

                            String from = configCompany.getDfrEmsFrom();

                            switch (configCompany.getFkDfrEmsTypeId()) {
                                case DModSysConsts.CS_EMS_TP_OWN:
                                    properties.setProperty("mail.smtp.host", configCompany.getDfrEmsSmtpHost());
                                    properties.setProperty("mail.smtp.port", "" + configCompany.getDfrEmsSmtpPort());
                                    if (configCompany.isDfrEmsSmtpSslEnabled()) {
                                        properties.setProperty("mail.smtp.ssl.enable", "true");
                                    }
                                    break;
                                case DModSysConsts.CS_EMS_TP_LIVE:
                                    properties.setProperty("mail.smtp.host", "smtp.live.com");
                                    properties.setProperty("mail.smtp.port", "587");
                                    properties.setProperty("mail.smtp.auth", "true");
                                    properties.setProperty("mail.smtp.starttls.enable", "true");
                                    properties.setProperty("mail.smtp.ssl.trust", "smtp.live.com");
                                    break;
                                case DModSysConsts.CS_EMS_TP_GMAIL:
                                    properties.setProperty("mail.smtp.host", "smtp.gmail.com");
                                    properties.setProperty("mail.smtp.port", "587");
                                    properties.setProperty("mail.smtp.auth", "true");
                                    properties.setProperty("mail.smtp.starttls.enable", "true");
                                    properties.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com");
                                    break;
                                default:
                                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                            }

                            Session session = Session.getInstance(properties,
                                    new Authenticator() {
                                        protected PasswordAuthentication getPasswordAuthentication() {
                                            return new javax.mail.PasswordAuthentication(name, password);
                                        }
                                    });

                            MimeMessage mimeMessage = new MimeMessage(session);
                            mimeMessage.setFrom(new InternetAddress(from));

                            ArrayList<DBprEmail> emails = dialog.getEmails();
                            for (DBprEmail email : emails) {
                                mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email.composeEmail()));
                            }

                            mimeMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(from));

                            mimeMessage.setSubject(configCompany.getDfrEmsSubject() + " " + dfr.getDfrNumber());

                            Multipart multipart = new MimeMultipart();
                            BodyPart bodyPart = null;

                            bodyPart = new MimeBodyPart();
                            bodyPart.setText(configCompany.getDfrEmsBody());
                            multipart.addBodyPart(bodyPart);

                            String filePath = "";
                            String fileName = "";
                            DataSource dataSource = null;
                            DDbConfigBranch configBranch = (DDbConfigBranch) client.getSession().readRegistry(DModConsts.CU_CFG_BRA, dfr.getCompanyBranchKey());

                            // PDF file:

                            fileName = dfr.getDocXmlName().replaceAll(".xml", ".pdf");
                            filePath = configBranch.getDfrDirectory() + fileName;

                            dataSource = new FileDataSource(filePath);
                            bodyPart = new MimeBodyPart();
                            bodyPart.setDataHandler(new DataHandler(dataSource));
                            bodyPart.setFileName(fileName);
                            multipart.addBodyPart(bodyPart);

                            // XML file:

                            fileName = dfr.getDocXmlName();
                            filePath = configBranch.getDfrDirectory() + fileName;

                            dataSource = new FileDataSource(filePath);
                            bodyPart = new MimeBodyPart();
                            bodyPart.setDataHandler(new DataHandler(dataSource));
                            bodyPart.setFileName(fileName);
                            multipart.addBodyPart(bodyPart);

                            mimeMessage.setContent(multipart);

                            Transport.send(mimeMessage);

                            // notify suscriptors and user:
                            client.getSession().notifySuscriptors(DModConsts.T_DFR);
                            client.showMsgBoxInformation("El comprobante '" + dfr.getDfrNumber() + "' ha sido enviado.");

                            // preserve mails if requested:
                            if (dialog.isPreserveEmailsSelected()) {
                                dialog.preserveEmails(branchAddress);
                            }
                        }
                        catch (MessagingException e) {
                            e.printStackTrace();
                            DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
                        }
                        finally {
                            client.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR)); // XXX improve this!!!
                        }
                    }
                }
            }
            catch (Exception e) {
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
            }
        }
    }
    
    public static void checkDps(final DGuiClient client, final DGridRowView gridRow) {
        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            try {
                DDbDps dps = (DDbDps) client.getSession().readRegistry(DModConsts.T_DPS, gridRow.getRowPrimaryKey(), DDbConsts.MODE_VERBOSE);
                DDbDfr dfr = dps.getChildDfr();
                
                DTrnDfrUtilsHandler dfrUtilsHandler = new DTrnDfrUtilsHandler(client.getSession());
                DTrnDfrUtilsHandler.CfdiAckQuery ackQuery = dfrUtilsHandler.getCfdiSatStatus(dfr);
                
                client.showMsgBoxInformation(ackQuery.composeMessage());
            }
            catch (Exception e) {
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
            }
        }
    }
    
    public static void checkDfr(final DGuiClient client, final DGridRowView gridRow) {
        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            try {
                DDbDfr dfr = (DDbDfr) client.getSession().readRegistry(DModConsts.T_DFR, gridRow.getRowPrimaryKey(), DDbConsts.MODE_VERBOSE);
                
                DTrnDfrUtilsHandler dfrUtilsHandler = new DTrnDfrUtilsHandler(client.getSession());
                DTrnDfrUtilsHandler.CfdiAckQuery ackQuery = dfrUtilsHandler.getCfdiSatStatus(dfr);
                
                client.showMsgBoxInformation(ackQuery.composeMessage());
            }
            catch (Exception e) {
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
            }
        }
    }
}
