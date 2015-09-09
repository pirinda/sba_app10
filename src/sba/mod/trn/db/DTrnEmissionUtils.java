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
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;
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
import sba.mod.bpr.db.DDbBizPartner;
import sba.mod.bpr.db.DDbBranchAddress;
import sba.mod.cfg.db.DDbConfigBranch;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.cfg.db.DDbSysCurrency;
import sba.mod.cfg.db.DDbSysXmlSignatureProvider;
import sba.mod.trn.form.DFormDpsCancelling;
import sba.mod.trn.form.DFormDpsPrinting;
import sba.mod.trn.form.DFormDpsSigning;
import sba.mod.trn.form.DFormDpsTypeChange;

/**
 *
 * @author Sergio Flores
 */
public abstract class DTrnEmissionUtils {
    
    private static int[] getCompanyBranchKeyFromDps(final DGuiSession session, final int[] dpsKey) throws Exception {
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
     * @return Emission parameters.
     */
    private static DTrnEmissionParams checkXmlSignatureRequestAllowed(final DGuiSession session, final int requestType, final int requestSubtype, final int[] dpsKey) throws Exception {
        int[] companyBranchKey = null;
        DTrnEmissionParams params = new DTrnEmissionParams(dpsKey[0]);
        DDbConfigCompany configCompany = (DDbConfigCompany) session.getConfigCompany();
        DDbConfigBranch configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, getCompanyBranchKeyFromDps(session, dpsKey));
        DDbSysXmlSignatureProvider xsp = null;

        if (!configCompany.isEdsApplying()) {
            throw new Exception("No se ha definido emisión de documentos electrónicos en la configuración de " + DUtilConsts.TXT_COMPANY.toLowerCase() + ".");
        }
        else if (configBranch.getFkXmlSignatureProviderId() == DModSysConsts.CS_XSP_NA) {
            throw new Exception("No se ha definido " + DTrnEmissionConsts.PAC + " en la configuración de " + DUtilConsts.TXT_BRANCH.toLowerCase() + ".");
        }
        else {
            params.SignatureProviderId = configBranch.getFkXmlSignatureProviderId();
            
            xsp = (DDbSysXmlSignatureProvider) session.readRegistry(DModConsts.CS_XSP, new int[] { params.SignatureProviderId });

            switch (requestType) {
                case DModSysConsts.TX_XMS_REQ_TP_SIG:
                    switch (requestSubtype) {
                        case DModSysConsts.TX_XMS_REQ_STP_REQ:
                            params.RequestAllowed = xsp.isSignature();
                            break;
                        case DModSysConsts.TX_XMS_REQ_STP_VER:
                            params.RequestAllowed = xsp.isSignatureVerification();
                            break;
                        default:
                            throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                    }
                    break;
                case DModSysConsts.TX_XMS_REQ_TP_CAN:
                    switch (requestSubtype) {
                        case DModSysConsts.TX_XMS_REQ_STP_REQ:
                            params.RequestAllowed = xsp.isCancellation();
                            break;
                        case DModSysConsts.TX_XMS_REQ_STP_VER:
                            params.RequestAllowed = xsp.isCancellationVerification();
                            break;
                        default:
                            throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                    }
                    break;
                default:
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
            
            if (!params.RequestAllowed) {
                throw new Exception("El " + DTrnEmissionConsts.PAC + " no permite la operación solicitada.\nQuizás será necesario realizar la acción de forma manual.");
            }
            else {
                // Check stamps available with DPS's company branch ones:
                
                companyBranchKey = getCompanyBranchKeyFromDps(session, dpsKey);
                params.StampsAvailable = getStampsAvailable(session, params.SignatureProviderId, companyBranchKey);
                if (params.StampsAvailable > 0) {
                    params.SignatureCompanyBranchKey = companyBranchKey;
                }
                else {
                    // Check stamps available with company ones:
                
                    params.StampsAvailable = getStampsAvailable(session, params.SignatureProviderId, null);
                    if (params.StampsAvailable > 0) {
                        params.SignatureCompanyBranchKey = null;    // value already set, just for consistence
                    }
                    else {
                        throw new Exception("El " + DTrnEmissionConsts.PAC + " no tiene timbres disponibles.");
                    }
                }
            }
        }
        
        return params;
    }
    
    public static DDbXmlSignatureRequest getLastXmlSignatureRequest(final DGuiSession session, final int[] dpsKey) throws SQLException, Exception {
        DDbXmlSignatureRequest xsr = null;
        String sql = "";
        ResultSet resultSet = null;
        
        sql = "SELECT COALESCE(MAX(id_req), 0) "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.T_XSR) + " "
                + "WHERE b_del = 0 AND id_dps = " + dpsKey[0] + " ";
        resultSet = session.getStatement().executeQuery(sql);
        if (resultSet.next() && resultSet.getInt(1) != DLibConsts.UNDEFINED) {
            xsr = (DDbXmlSignatureRequest) session.readRegistry(DModConsts.T_XSR, new int[] { dpsKey[0], resultSet.getInt(1) });
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
    
    public static void consumeStamp(final DGuiSession session, final int xmlSignatureProviderId, final int[] signatureCompanyBranchKey, final int[] xmlSignatureMoveKey, final int[] dpsKey) throws SQLException, Exception {
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
        xsm.setFkDpsId_n(dpsKey[0]);
        xsm.save(session);
    }
    
    public static void changeDpsType(final DGuiClient client, final DGridRowView gridRow) {
        DDbDps dps = null;
        DDbDpsEds eds = null;
        DDbDpsTypeChange dpsTypeChange = null;
        DFormDpsTypeChange formDpsTypeChange = null;

        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            try {
                dps = (DDbDps) client.getSession().readRegistry(DModConsts.T_DPS, gridRow.getRowPrimaryKey());
                eds = dps.getChildEds();

                if (eds != null) {
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

    public static void printDps(final DGuiClient client, final DGridRowView gridRow) {
        boolean print = true;
        boolean printed = false;
        DDbDps dps = null;
        DDbDpsEds eds = null;
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
                eds = dps.getChildEds();

                // Check if document number can be still changed:

                if (DTrnUtils.isDpsNumberAutomatic(dps.getDpsClassKey()) && ((DDbConfigBranch) client.getSession().getConfigBranch()).isDpsPrintingDialog() &&
                    (eds == null || (eds.getFkXmlTypeId() == DModSysConsts.TS_XML_TP_CFDI && eds.getFkXmlStatusId() == DModSysConsts.TS_XML_ST_PEN))) {
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
                                eds = DTrnEdsUtils.createDpsEds(client.getSession(), dps, seriesNumber.getFkXmlTypeId(), "", null);
                                dps.updateEds(client.getSession(), eds);
                            }
                        }
                    }
                }

                // Print document:

                if (print) {
                    if (eds == null || eds.getFkXmlStatusId() == DModSysConsts.TS_XML_ST_PEN) {
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
                        map.put("sEdsDir", configBranch.getEdsDirectory());

                        DPrtUtils.printReport(client.getSession(), DModConsts.TR_DPS, map);
                        printed = true;
                    }
                    else {
                        // Print document as "electronic document":

                        switch (eds.getFkXmlTypeId()) {
                            case DModSysConsts.TS_XML_TP_CFD:
                                DPrtUtils.printReport(client.getSession(), DModConsts.TR_DPS_CFD, new DPrtDps(client.getSession(), gridRow.getRowPrimaryKey()).cratePrintCfdMap());
                                printed = true;
                                break;
                            case DModSysConsts.TS_XML_TP_CFDI:
                                DPrtUtils.printReport(client.getSession(), DModConsts.TR_DPS_CFDI, new DPrtDps(client.getSession(), gridRow.getRowPrimaryKey()).cratePrintCfdiMap());
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

    public static void signDps(final DGuiClient client, final DGridRowView gridRow, final int requestType) {
        boolean sign = false;
        boolean signed = false;
        DTrnEmissionParams params = null;
        DDbSysXmlSignatureProvider xsp = null;
        DDbXmlSignatureRequest xsr = null;
        DDbDps dps = null;
        DDbDpsEds eds = null;
        DDbDpsSigning dpsSigning = null;
        DDbDpsSeriesNumber seriesNumber = null;
        DFormDpsSigning formDpsSigning = null;

        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            try {
                // Check if XML signature is allowed:
                
                params = checkXmlSignatureRequestAllowed(client.getSession(), DModSysConsts.TX_XMS_REQ_TP_SIG, requestType, gridRow.getRowPrimaryKey());
                sign = params.RequestAllowed;
            }
            catch (Exception e) {
                e.printStackTrace();
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
            }
            
            if (sign) {
                try {
                    // Check that there are not pending transactions:

                    xsp = (DDbSysXmlSignatureProvider) client.getSession().readRegistry(DModConsts.CS_XSP, new int[] { params.SignatureProviderId });
                    xsr = getLastXmlSignatureRequest(client.getSession(), gridRow.getRowPrimaryKey());

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

                    dps = (DDbDps) client.getSession().readRegistry(DModConsts.T_DPS, gridRow.getRowPrimaryKey(), DDbConsts.MODE_VERBOSE);

                    if (xsr == null) {
                        // Check and confirm document details before first signature request:

                        eds = dps.getChildEds();

                        if (eds == null || eds.isRegistryNew()) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN + DDbConsts.ERR_MSG_REG_NOT_FOUND +
                                    "\nEl registro XML del documento no existe.");
                        }
                        else if (eds.getFkXmlTypeId() != DModSysConsts.TS_XML_TP_CFDI) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_SIGN + "El tipo del registro XML del documento debe ser '" +
                                    client.getSession().readField(DModConsts.TS_XML_TP, new int[] { DModSysConsts.TS_XML_TP_CFDI }, DDbRegistry.FIELD_NAME) + "'.");
                        }
                        else if (!DLibUtils.belongsTo(eds.getFkXmlStatusId(), new int[] { DModSysConsts.TS_XML_ST_PEN })) {
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
                                                eds = DTrnEdsUtils.createDpsEds(client.getSession(), dps, seriesNumber.getFkXmlTypeId(), "", null);
                                                dps.updateEds(client.getSession(), eds);
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

                            DTrnEdsUtils.signDps(client.getSession(), dps, xsp.getPkXmlSignatureProviderId(), params.SignatureCompanyBranchKey, requestType);
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
                                        "Quedan " + DLibUtils.DecimalFormatInteger.format(getStampsAvailable(client.getSession(), xsp.getPkXmlSignatureProviderId(), params.SignatureCompanyBranchKey)) + " timbres disponibles.");
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
        int action = DTrnEmissionConsts.ANNUL_CANCEL;
        int lastRequestStatus = DLibConsts.UNDEFINED;
        boolean cancel = false;
        boolean cancelled = false;
        DTrnEmissionParams params = null;
        DDbSysXmlSignatureProvider xsp = null;
        DDbXmlSignatureRequest xsr = null;
        DDbDps dps = null;
        DDbDpsEds eds = null;
        DFormDpsCancelling formDpsCancelling = null;

        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else {
            try {
                // Check if XML cancellation is allowed:
                
                params = checkXmlSignatureRequestAllowed(client.getSession(), DModSysConsts.TX_XMS_REQ_TP_CAN, requestType, gridRow.getRowPrimaryKey());
                cancel = params.RequestAllowed;
            }
            catch (Exception e) {
                e.printStackTrace();
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
                
                if (requestType == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                    cancel = client.showMsgBoxConfirm(DGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION;
                }
            }
            
            if (cancel) {
                try {
                    // Check that there are not pending transactions:

                    xsp = (DDbSysXmlSignatureProvider) client.getSession().readRegistry(DModConsts.CS_XSP, new int[] { params.SignatureProviderId });
                    xsr = getLastXmlSignatureRequest(client.getSession(), gridRow.getRowPrimaryKey());

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

                    dps = (DDbDps) client.getSession().readRegistry(DModConsts.T_DPS, gridRow.getRowPrimaryKey(), DDbConsts.MODE_VERBOSE);

                    if (xsr == null) {
                        // Check and confirm document details before first signature request:

                        eds = dps.getChildEds();

                        if (eds == null || eds.isRegistryNew()) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL + DDbConsts.ERR_MSG_REG_NOT_FOUND +
                                    "\nEl registro XML del documento no existe.");
                        }
                        else if (eds.getFkXmlTypeId() != DModSysConsts.TS_XML_TP_CFDI) {
                            throw new Exception(DTrnEmissionConsts.MSG_DENIED_CANCEL + "El tipo del registro XML del documento debe ser '" +
                                    client.getSession().readField(DModConsts.TS_XML_TP, new int[] { DModSysConsts.TS_XML_TP_CFDI }, DDbRegistry.FIELD_NAME) + "'.");
                        }
                        else if (!DLibUtils.belongsTo(eds.getFkXmlStatusId(), new int[] { DModSysConsts.TS_XML_ST_PEN, DModSysConsts.TS_XML_ST_ISS })) {
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
                            // Check if document can be disabled:

                            cancel = dps.canDisable(client.getSession());
                        }
                    }

                    if (cancel) {
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
                                }
                            }
                        }

                        if (cancel) {
                            try {
                                client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));    // XXX improve this!!!

                                DTrnEdsUtils.cancelDps(client.getSession(), dps, xsp.getPkXmlSignatureProviderId(), params.SignatureCompanyBranchKey, requestType, action);
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
                                            "Quedan " + DLibUtils.DecimalFormatInteger.format(getStampsAvailable(client.getSession(), xsp.getPkXmlSignatureProviderId(), params.SignatureCompanyBranchKey)) + " timbres disponibles.");
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
        boolean send = false;
        DDbDps dps = null;
        DDbDpsEds eds = null;
        DDbDpsSending sending = null;
        DDbBizPartner bizPartner = null;
        DDbBranchAddress branchAddress = null;
        DDbConfigCompany configCompany = (DDbConfigCompany) client.getSession().getConfigCompany();
        final String name = configCompany.getEdsEmsName();
        final String password = configCompany.getEdsEmsPassword();
        Vector<String> emails = new Vector<String>();

        if (gridRow.getRowType() != DGridConsts.ROW_TYPE_DATA) {
            client.showMsgBoxWarning(DGridConsts.ERR_MSG_ROW_TYPE_DATA);
        }
        else if (configCompany.getFkEdsEmsTypeId() == DModSysConsts.CS_EMS_TP_NA) {
            client.showMsgBoxWarning("No está configurado el envío de documentos electrónicos vía e-mail.");
        }
        else {

            try {
                eds = (DDbDpsEds) client.getSession().readRegistry(DModConsts.T_DPS_EDS, gridRow.getRowPrimaryKey(), DDbConsts.MODE_STEALTH);

                if (eds == null || eds.isRegistryNew()) {
                    throw new Exception(DTrnEmissionConsts.MSG_DENIED_SEND + DDbConsts.ERR_MSG_REG_NOT_FOUND + "\nEl registro XML del documento no existe.");
                }
                else if (eds.getFkXmlStatusId() != DModSysConsts.TS_XML_ST_ISS) {
                    throw new Exception(DTrnEmissionConsts.MSG_DENIED_SEND + "El estatus del registro XML del documento debe ser '" + client.getSession().readField(DModConsts.TS_XML_ST, new int[] { DModSysConsts.TS_XML_ST_ISS }, DDbRegistry.FIELD_NAME) + "'.");
                }
                else {
                    dps = (DDbDps) client.getSession().readRegistry(DModConsts.T_DPS, gridRow.getRowPrimaryKey());
                    sending = dps.createDpsSending();

                    if (dps.getFkDpsStatusId() != DModSysConsts.TS_DPS_ST_ISS) {
                        throw new Exception(DTrnEmissionConsts.MSG_DENIED_SEND + "El estatus del documento debe ser '" + client.getSession().readField(DModConsts.TS_DPS_ST, new int[] { DModSysConsts.TS_DPS_ST_ISS }, DDbRegistry.FIELD_NAME) + "'.");
                    }
                    else {
                        branchAddress = (DDbBranchAddress) client.getSession().readRegistry(DModConsts.BU_ADD, dps.getBizPartnerBranchAddressKey(), DDbConsts.MODE_STEALTH);

                        if (branchAddress.getFkTelecommElectronic1TypeId() == DModSysConsts.BS_TCE_TP_EMA) {
                            if (branchAddress.getContact1().isEmpty()) {
                                emails.add(branchAddress.getTelecommElectronic1());
                            }
                            else {
                                emails.add("\"" + branchAddress.getContact1() + "\" <" + branchAddress.getTelecommElectronic1() + ">");
                                sending.setContact1(branchAddress.getContact1());
                            }
                            sending.setEmail1(branchAddress.getTelecommElectronic1());
                        }

                        if (branchAddress.getFkTelecommElectronic2TypeId() == DModSysConsts.BS_TCE_TP_EMA) {
                            if (branchAddress.getContact2().isEmpty()) {
                                emails.add(branchAddress.getTelecommElectronic2());
                            }
                            else {
                                emails.add("\"" + branchAddress.getContact2() + "\" <" + branchAddress.getTelecommElectronic2() + ">");
                                sending.setContact2(branchAddress.getContact2());
                            }
                            sending.setEmail2(branchAddress.getTelecommElectronic2());
                        }

                        if (emails.isEmpty()) {
                            bizPartner = (DDbBizPartner) client.getSession().readRegistry(DModConsts.BU_BPR, dps.getBizPartnerKey(), DDbConsts.MODE_STEALTH);
                            client.showMsgBoxWarning("No se han definido correos electrónicos para '" + bizPartner.getName() + "',\nreceptor del documento '" + dps.getDpsNumber() + "'.");
                        }
                        else {
                            send = client.showMsgBoxConfirm("El documento '" + dps.getDpsNumber() + "' será enviado a:" +
                                    (emails.size() <= 0 ? "" : "\n" + emails.get(0)) +
                                    (emails.size() <= 1 ? "" : "\n" + emails.get(1)) +
                                    "\n¿Está seguro que desea enviar el documento?") == JOptionPane.YES_OPTION;

                            if (send) {
                                try {
                                    client.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));    // XXX improve this!!!

                                    Properties properties = System.getProperties();

                                    String from = configCompany.getEdsEmsFrom();

                                    switch (configCompany.getFkEdsEmsTypeId()) {
                                        case DModSysConsts.CS_EMS_TP_OWN:
                                            properties.setProperty("mail.smtp.host", configCompany.getEdsEmsSmtpHost());
                                            properties.setProperty("mail.smtp.port", "" + configCompany.getEdsEmsSmtpPort());
                                            if (configCompany.isEdsEmsSmtpSslEnabled()) {
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

                                    if (emails.size() > 0) {
                                        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emails.get(0)));
                                    }
                                    if (emails.size() > 1) {
                                        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(emails.get(1)));
                                    }

                                    mimeMessage.addRecipient(Message.RecipientType.BCC, new InternetAddress(from));

                                    mimeMessage.setSubject(configCompany.getEdsEmsSubject() + " " + dps.getDpsNumber());

                                    Multipart multipart = new MimeMultipart();
                                    BodyPart bodyPart = null;

                                    bodyPart = new MimeBodyPart();
                                    bodyPart.setText(configCompany.getEdsEmsBody());
                                    multipart.addBodyPart(bodyPart);

                                    String filePath = "";
                                    String fileName = "";
                                    DataSource dataSource = null;
                                    DDbConfigBranch configBranch = (DDbConfigBranch) client.getSession().readRegistry(DModConsts.CU_CFG_BRA, dps.getCompanyBranchKey());

                                    // PDF file:

                                    fileName = eds.getDocXmlName().replaceAll(".xml", ".pdf");
                                    filePath = configBranch.getEdsDirectory() + fileName;

                                    dataSource = new FileDataSource(filePath);
                                    bodyPart = new MimeBodyPart();
                                    bodyPart.setDataHandler(new DataHandler(dataSource));
                                    bodyPart.setFileName(fileName);
                                    multipart.addBodyPart(bodyPart);

                                    // XML file:

                                    fileName = eds.getDocXmlName();
                                    filePath = configBranch.getEdsDirectory() + fileName;

                                    dataSource = new FileDataSource(filePath);
                                    bodyPart = new MimeBodyPart();
                                    bodyPart.setDataHandler(new DataHandler(dataSource));
                                    bodyPart.setFileName(fileName);
                                    multipart.addBodyPart(bodyPart);

                                    mimeMessage.setContent(multipart);

                                    Transport.send(mimeMessage);

                                    sending.save(client.getSession());
                                    client.getSession().notifySuscriptors(DModConsts.T_DPS_SND);

                                    client.showMsgBoxInformation("El documento '" + dps.getDpsNumber() + "' ha sido enviado.");
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
                }
            }
            catch (Exception e) {
                DLibUtils.showException(DTrnEmissionUtils.class.getName(), e);
            }
        }
    }
}
