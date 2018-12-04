/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.trn.db;

import java.text.DecimalFormat;
import java.util.ArrayList;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.bpr.db.DDbBizPartner;
import sba.mod.cfg.db.DDbCertificate;
import sba.mod.cfg.db.DDbConfigBranch;
import sba.mod.cfg.db.DDbConfigCompany;

/**
 *
 * @author Sergio Flores
 */
public class DTrnDfrUtilsHandler {
    
    private final DGuiSession moSession;
    
    public DTrnDfrUtilsHandler(DGuiSession session) {
        moSession = session;
    }
    
    public ArrayList<CfdiRelated> getCfdiRelated(final DDbDfr dfr) throws Exception {
        ArrayList<CfdiRelated> cfdiRelatedList = new ArrayList<>();
        
        DDbBizPartner company = (DDbBizPartner) moSession.readRegistry(DModConsts.BU_BPR, dfr.getCompanyKey());
        DDbConfigBranch configBranch = (DDbConfigBranch) moSession.readRegistry(DModConsts.CU_CFG_BRA, dfr.getCompanyBranchKey());
        DDbCertificate certificate = (DDbCertificate) moSession.readRegistry(DModConsts.CU_CER, new int[] { configBranch.getFkCertificateId_n() });
        
        String fiscalId = company.getFiscalId();
        String edsName = configBranch.getEdsName();
        String edsPswd = configBranch.getEdsPassword();

        if (!((DDbConfigCompany) moSession.getConfigCompany()).isDevelopment()) {
            // environment for production is set:

            switch (configBranch.getFkXmlSignatureProviderId()) {
                case DModSysConsts.CS_XSP_FCG:
                    break;

                case DModSysConsts.CS_XSP_FNK:
                    com.finkok.facturacion.cancel.CancelSOAP cancelSOAP = new com.finkok.facturacion.cancel.CancelSOAP();
                    com.finkok.facturacion.cancel.Application application = cancelSOAP.getApplication();

                    views.core.soap.services.apps.RelatedResult relatedResult = application.getRelated(
                            edsName, edsPswd, fiscalId, dfr.getUuid(), certificate.getXtaCertificatePemKeyPublic_n(), certificate.getXtaCertificatePemKeyPrivate_n());

                    if (relatedResult.getError() != null) {
                        throw new Exception(relatedResult.getError().getValue());
                    }
                    else if (relatedResult.getPadres() != null) {
                        views.core.soap.services.apps.PadreArray padreArray = relatedResult.getPadres().getValue();
                        for (views.core.soap.services.apps.Padre padre : padreArray.getPadre()) {
                            String valUuid = "";
                            String valEmisor = "";
                            String valReceptor = "";

                            if (padre.getUuid().getValue() != null) {
                                valUuid = padre.getUuid().getValue();
                            }
                            if (padre.getEmisor().getValue() != null) {
                                valEmisor = padre.getEmisor().getValue();
                            }
                            if (padre.getReceptor().getValue() != null) {
                                valEmisor = padre.getReceptor().getValue();
                            }

                            cfdiRelatedList.add(new CfdiRelated(valUuid, valEmisor, valReceptor));
                        }
                    }
                    break;

                default:
            }
        }
        else {
            // environment for development is set:

            switch (configBranch.getFkXmlSignatureProviderId()) {
                case DModSysConsts.CS_XSP_FCG:
                    break;

                case DModSysConsts.CS_XSP_FNK:
                    break;

                default:
            }
        }
        
        return cfdiRelatedList;
    }
    
    public CfdiAckQuery getCfdiSatStatus(final DDbDfr dfr) throws Exception {
        CfdiAckQuery cfdiAckQuery = null;
        
        DDbBizPartner company = (DDbBizPartner) moSession.readRegistry(DModConsts.BU_BPR, dfr.getCompanyKey());
        DDbConfigBranch configBranch = (DDbConfigBranch) moSession.readRegistry(DModConsts.CU_CFG_BRA, dfr.getCompanyBranchKey());
        DDbBizPartner bizPartner = (DDbBizPartner) moSession.readRegistry(DModConsts.BU_BPR, dfr.getBizPartnerKey());
        
        String fiscalId = company.getFiscalId();
        String edsName = configBranch.getEdsName();
        String edsPswd = configBranch.getEdsPassword();

        if (!((DDbConfigCompany) moSession.getConfigCompany()).isDevelopment()) {
            // environment for production is set:

            switch (configBranch.getFkXmlSignatureProviderId()) {
                case DModSysConsts.CS_XSP_FCG:
                    break;

                case DModSysConsts.CS_XSP_FNK:
                    DecimalFormat totalFormat = new DecimalFormat("#.00");
                    com.finkok.facturacion.cancel.CancelSOAP cancelSOAP = new com.finkok.facturacion.cancel.CancelSOAP();
                    com.finkok.facturacion.cancel.Application application = cancelSOAP.getApplication();

                    views.core.soap.services.apps.AcuseSatEstatus acuseSatEstatus = application.getSatStatus(
                            edsName, edsPswd, fiscalId, bizPartner.getFiscalId(), dfr.getUuid(), totalFormat.format(dfr.getDfrTotal(moSession)));

                    if (acuseSatEstatus.getError() != null) {
                        throw new Exception(acuseSatEstatus.getError().getValue());
                    }
                    else {
                        String valEsCancelable = "";
                        String valCodigoEstatus = "";
                        String valEstado = "";
                        String valEstatusCancelacion = "";

                        if (acuseSatEstatus.getSat().getValue().getEsCancelable().getValue() != null) {
                            valEsCancelable = acuseSatEstatus.getSat().getValue().getEsCancelable().getValue();
                        }
                        if (acuseSatEstatus.getSat().getValue().getCodigoEstatus().getValue() != null) {
                            valCodigoEstatus = acuseSatEstatus.getSat().getValue().getCodigoEstatus().getValue();
                        }
                        if (acuseSatEstatus.getSat().getValue().getEstado().getValue() != null) {
                            valEstado = acuseSatEstatus.getSat().getValue().getEstado().getValue();
                        }
                        if (acuseSatEstatus.getSat().getValue().getEstatusCancelacion().getValue() != null) {
                            valEstatusCancelacion = acuseSatEstatus.getSat().getValue().getEstatusCancelacion().getValue();
                        }

                        cfdiAckQuery = new CfdiAckQuery(dfr.getUuid(), valEsCancelable, valCodigoEstatus, valEstado, valEstatusCancelacion);
                        cfdiAckQuery.CfdiRelatedList.addAll(getCfdiRelated(dfr));
                    }
                    break;

                default:
            }
        }
        else {
            // environment for development is set:

            switch (configBranch.getFkXmlSignatureProviderId()) {
                case DModSysConsts.CS_XSP_FCG:
                    break;

                case DModSysConsts.CS_XSP_FNK:
                    break;

                default:
            }
        }
        
        return cfdiAckQuery;
    }
    
    public class CfdiAckQuery {
        public String Uuid;
        public String CancellableInfo;
        public String RetrievalInfo;
        public String CfdiStatus;
        public String CancelStatus;
        public ArrayList<CfdiRelated> CfdiRelatedList;
        
        public CfdiAckQuery(String uuid, String cancellableInfo, String retrievalInfo, String cfdiStatus, String cancelStatus) {
            Uuid = uuid;
            CancellableInfo = cancellableInfo;
            RetrievalInfo = retrievalInfo;
            CfdiStatus = cfdiStatus;
            CancelStatus = cancelStatus;
            CfdiRelatedList = new ArrayList<>();
        }
        
        public String composeMessage() {
            String message;
            
            message = "ESTATUS SAT DEL CFDI '" + Uuid + "':\n";
            message += "Cancelable: [" + CancellableInfo + "]\n";
            message += "Recuperación: [" + RetrievalInfo + "]\n";
            message += "Estatus CFDI: [" + CfdiStatus + "]\n";
            message += "Estatus cancelación: [" + CancelStatus + "]\n";
            message += composeCfdiRelated();
            
            return message;
        }
        
        public String composeCfdiRelated() {
            String message;
            
            if (CfdiRelatedList.isEmpty()) {
                message = "Sin CFDI relacionados";
            }
            else {
                message = "Con " + CfdiRelatedList.size() + " CFDI " + (CfdiRelatedList.size() == 1 ? "relacionado" : "relacionados") + ":";
                for (CfdiRelated cfdiRelated : CfdiRelatedList) {
                    message += "\n- " + cfdiRelated.composeMessage();
                }
            }
            
            return message;
        }
    }
    
    public class CfdiRelated {
        public String Uuid;
        public String Issuer;
        public String Receiver;
        
        public CfdiRelated(String uuid, String issuer, String receiver) {
            Uuid = uuid;
            Issuer = issuer;
            Receiver = receiver;
        }
        
        public String composeMessage() {
            return "UUID: " + Uuid + "; emisor: " + Issuer + "; receptor: " + Receiver;
        }
    }
}
