/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.trn.db;

import cfd.DCfdConsts;
import cfd.DCfdUtils;
import cfd.ver4.DCfdVer4Consts;
import java.text.DecimalFormat;
import java.util.ArrayList;
import org.tempuri.ConsultaCFDIService;
import org.tempuri.IConsultaCFDIService;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
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
    
    private static final String PADRE = "Padre";
    private static final String HIJO = "Hijo";
    
    private final DGuiSession moSession;
    private String msLastCfdiRelatedError;
    
    public DTrnDfrUtilsHandler(DGuiSession session) {
        moSession = session;
        msLastCfdiRelatedError = "";
    }

    /** Obtener la lista de los CFDI relacionados con otro CFDI.
     * 2019-08-14, Sergio Flores: Se deshabilita temporalmente la consulta de CFDI relacionados 
     * debido a cambio inesperado en los parámetros de la solicitud del web service,
     * ahora se discurrió que deben proporcionarse el RFC y CSD del receptor. ¡Sí, el CSD del receptor! WTF!
     * @param dfr
     * @return
     * @throws Exception
     * @deprecated
     */
    public ArrayList<CfdiRelated> getCfdiRelated(final DDbDfr dfr) throws Exception {
        msLastCfdiRelatedError = "";
        ArrayList<CfdiRelated> cfdiRelatedList = new ArrayList<>();
        
        DDbBizPartner company = (DDbBizPartner) moSession.readRegistry(DModConsts.BU_BPR, dfr.getCompanyKey());
        DDbConfigBranch configBranch = (DDbConfigBranch) moSession.readRegistry(DModConsts.CU_CFG_BRA, dfr.getCompanyBranchKey());
        DDbCertificate certificate = (DDbCertificate) moSession.readRegistry(DModConsts.CU_CER, new int[] { configBranch.getFkCertificateId_n() });
        
        String fiscalId = company.getFiscalId();
        String edsName = configBranch.getDfrName();
        String edsPswd = configBranch.getDfrPassword();

        if (!((DDbConfigCompany) moSession.getConfigCompany()).isDevelopment()) {
            // environment for production is set:

            switch (configBranch.getFkXmlSignatureProviderId()) {
                case DModSysConsts.CS_XSP_FCG:
                    break;

                case DModSysConsts.CS_XSP_FNK:
                    com.finkok.facturacion.cancel.CancelSOAP cancelSOAP = new com.finkok.facturacion.cancel.CancelSOAP();
                    com.finkok.facturacion.cancel.Application application = cancelSOAP.getApplication();
                    
                    /* 2019-08-14, Sergio Flores: Se deshabilita temporalmente la consulta de CFDI relacionados.
                     * debido a cambio inesperado en los parámetros de la solicitud del web service,
                     * ahora se discurrió que deben proporcionarse el RFC y CSD del receptor. ¡Sí, el CSD del receptor! WTF!
                    */
                    
                    views.core.soap.services.apps.RelatedResult relatedResult = application.getRelated(
                            edsName, edsPswd, fiscalId, "", dfr.getUuid(), certificate.getXtaCertificatePemKeyPublic_n(), certificate.getXtaCertificatePemKeyPrivate_n());
                    
                    if (relatedResult.getError() != null) {
                        msLastCfdiRelatedError = relatedResult.getError().getValue();
                    }
                    
                    if (relatedResult.getPadres() != null) {
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

                            cfdiRelatedList.add(new CfdiRelated(PADRE, valUuid, valEmisor, valReceptor));
                        }
                    }

                    if (relatedResult.getHijos() != null) {
                        views.core.soap.services.apps.HijoArray hijoArray = relatedResult.getHijos().getValue();
                        for (views.core.soap.services.apps.Hijo hijo : hijoArray.getHijo()) {
                            String valUuid = "";
                            String valEmisor = "";
                            String valReceptor = "";

                            if (hijo.getUuid().getValue() != null) {
                                valUuid = hijo.getUuid().getValue();
                            }
                            if (hijo.getEmisor().getValue() != null) {
                                valEmisor = hijo.getEmisor().getValue();
                            }
                            if (hijo.getReceptor().getValue() != null) {
                                valReceptor = hijo.getReceptor().getValue();
                            }

                            cfdiRelatedList.add(new CfdiRelated(HIJO, valUuid, valEmisor, valReceptor));
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
    
    @SuppressWarnings("deprecation")
    public CfdiAckQuery getCfdiSatStatus(final DDbDfr dfr) throws Exception {
        /*
        // Former code. Status query done through PAC web services
        
        CfdiAckQuery cfdiAckQuery = null;
        
        DDbBizPartner company = (DDbBizPartner) moSession.readRegistry(DModConsts.BU_BPR, dfr.getCompanyKey());
        DDbConfigBranch configBranch = (DDbConfigBranch) moSession.readRegistry(DModConsts.CU_CFG_BRA, dfr.getCompanyBranchKey());
        DDbBizPartner bizPartner = (DDbBizPartner) moSession.readRegistry(DModConsts.BU_BPR, dfr.getBizPartnerKey());
        
        String fiscalId = company.getFiscalId();
        String edsName = configBranch.getDfrName();
        String edsPswd = configBranch.getDfrPassword();
        
        if (((DDbConfigCompany) moSession.getConfigCompany()).isDevelopment()) {
            // environment for development is set:

            switch (configBranch.getFkXmlSignatureProviderId()) {
                case DModSysConsts.CS_XSP_FCG:
                case DModSysConsts.CS_XSP_FNK:
                    throw new UnsupportedOperationException("La consulta de estatus SAT de CFDI no está implementada en ambiente de desarrollo.");

                default:
                    // do nothing
            }
        }
        else {
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

                        cfdiAckQuery = new CfdiAckQuery(dfr.getUuid(), valEsCancelable, valCodigoEstatus, valEstado, valEstatusCancelacion, false);
                        // XXX 2019-08-27 Sergio Flores: El método de consula de CFDI relacionados dejó de funcionar con el PAC Quadrum asociado a Finkok:
                        //cfdiAckQuery.CfdiRelatedList.addAll(getCfdiRelated(dfr));
                    }
                    break;

                default:
                    // do nothing
            }
        }
        */
        
        String rfcEmisor = "";
        String rfcReceptor = "";
        double total = 0;
        String uuid = "";
        String sello = "";
        
        switch (dfr.getFkXmlTypeId()) {
            case DModSysConsts.TS_XML_TP_CFDI_32:
                cfd.ver32.DElementComprobante comprobante32 = DCfdUtils.getCfdi32(dfr.getSuitableDocXml());
                cfd.ver32.DElementTimbreFiscalDigital tfd32 = comprobante32.getEltOpcComplementoTimbreFiscalDigital();
        
                if (tfd32 == null) {
                    throw new Exception("¡El CFDI 3.2 no está timbrado!");
                }
                else {
                    rfcEmisor = comprobante32.getEltEmisor().getAttRfc().getString();
                    rfcReceptor = comprobante32.getEltReceptor().getAttRfc().getString();
                    total = comprobante32.getAttTotal().getDouble();
                    uuid = tfd32.getAttUUID().getString();
                }
                break;
                
            case DModSysConsts.TS_XML_TP_CFDI_33:
                cfd.ver33.DElementComprobante comprobante33 = DCfdUtils.getCfdi33(dfr.getSuitableDocXml());
                cfd.ver33.DElementTimbreFiscalDigital tfd33 = comprobante33.getEltOpcComplementoTimbreFiscalDigital();
        
                if (tfd33 == null) {
                    throw new Exception("¡El CFDI 3.3 no está timbrado!");
                }
                else {
                    rfcEmisor = comprobante33.getEltEmisor().getAttRfc().getString();
                    rfcReceptor = comprobante33.getEltReceptor().getAttRfc().getString();
                    total = comprobante33.getAttTotal().getDouble();
                    uuid = tfd33.getAttUUID().getString();
                    sello = comprobante33.getAttSello().getString();
                }
                break;
                
            case DModSysConsts.TS_XML_TP_CFDI_40:
                cfd.ver40.DElementComprobante comprobante40 = DCfdUtils.getCfdi40(dfr.getSuitableDocXml());
                cfd.ver40.DElementTimbreFiscalDigital tfd40 = comprobante40.getEltOpcComplementoTimbreFiscalDigital();
        
                if (tfd40 == null) {
                    throw new Exception("¡El CFDI 4.0 no está timbrado!");
                }
                else {
                    rfcEmisor = comprobante40.getEltEmisor().getAttRfc().getString();
                    rfcReceptor = comprobante40.getEltReceptor().getAttRfc().getString();
                    total = comprobante40.getAttTotal().getDouble();
                    uuid = tfd40.getAttUUID().getString();
                    sello = comprobante40.getAttSello().getString();
                }
                break;
                
            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nTipo de XML.");
        }
        
        String data = "";
        
        switch (dfr.getFkXmlTypeId()) {
            case DModSysConsts.TS_XML_TP_CFDI_32:
                DecimalFormat decimalFormat32 = new DecimalFormat("0000000000.000000");

                data += "?re=" + rfcEmisor;
                data += "&rr=" + rfcReceptor;
                data += "&tt=" + decimalFormat32.format(total);
                data += "&id=" + uuid;
                break;
                
            case DModSysConsts.TS_XML_TP_CFDI_33:
            case DModSysConsts.TS_XML_TP_CFDI_40:
                DecimalFormat decimalFormat33 = new DecimalFormat("#." + DLibUtils.textRepeat("#", 6)); // max decimals for total according to XSD of CFDI 3.3

                data += "?id=" + (uuid == null || uuid.isEmpty() ? DLibUtils.textRepeat("0", DCfdVer4Consts.LEN_UUID) : uuid); // UUID length hyphens included
                data += "&re=" + (rfcEmisor == null || rfcEmisor.isEmpty() ? DLibUtils.textRepeat("X", DCfdConsts.LEN_RFC_PER) : rfcEmisor.replaceAll("&", "&amp;")); // some fiscal ID contains character '&', and must be encoded
                data += "&rr=" + (rfcReceptor == null || rfcReceptor.isEmpty() ? DLibUtils.textRepeat("X", DCfdConsts.LEN_RFC_PER) : rfcReceptor.replaceAll("&", "&amp;")); // some fiscal ID contains character '&', and must be encoded
                data += "&tt=" + decimalFormat33.format(DLibUtils.roundAmount(total));
                if (sello != null && !sello.isEmpty()) {
                    data += "&fe=" + DLibUtils.textRight(sello, 8); // last 8 characters of electronic signature
                }
                break;
                
            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nTipo de XML.");
        }
        
        ConsultaCFDIService service = new ConsultaCFDIService(); 
        IConsultaCFDIService iService = service.getBasicHttpBindingIConsultaCFDIService();
        org.datacontract.schemas._2004._07.sat_cfdi_negocio_consultacfdi.Acuse acuse = iService.consulta(data);
        
        String valEsCancelable = "";
        String valCodigoEstatus = "";
        String valEstado = "";
        String valEstatusCancelacion = "";
        
        if (acuse != null) {
            if (acuse.getEsCancelable() != null) {
                valEsCancelable = acuse.getEsCancelable().getValue();
            }
            if (acuse.getCodigoEstatus() != null) {
                valCodigoEstatus = acuse.getCodigoEstatus().getValue();
            }
            if (acuse.getEstado() != null) {
                valEstado = acuse.getEstado().getValue();
            }
            if (acuse.getEstatusCancelacion() != null) {
                valEstatusCancelacion = acuse.getEstatusCancelacion().getValue();
            }
        }
        
        boolean areCfdiRelatedApplicable = ((DDbConfigCompany) moSession.getConfigCompany()).getChildBizPartner().getFiscalId().equals(rfcEmisor); // ¿el emisor del CFDI es la empresa de la sesión?
        CfdiAckQuery cfdiAckQuery = new CfdiAckQuery(uuid, valEsCancelable, valCodigoEstatus, valEstado, valEstatusCancelacion, areCfdiRelatedApplicable);
        
        if (areCfdiRelatedApplicable) {
            cfdiAckQuery.CfdiRelatedList.addAll(getCfdiRelated(dfr));
        }
        
        return cfdiAckQuery;
    }
    
    public class CfdiAckQuery {
        public String Uuid;
        public String CancellableInfo;
        public String RetrievalInfo;
        public String CfdiStatus;
        public String CancelStatus;
        public boolean AreCfdiRelatedApplicable;
        public ArrayList<CfdiRelated> CfdiRelatedList;
        
        /**
         * Construye nuevo resultado de la consulta de estatus de CFDI.
         * @param uuid UUID del CFDI.
         * @param cancellableInfo Estatus de cancelabilidad del CFDI.
         * @param retrievalInfo Estatus de obtención del CFDI.
         * @param cfdiStatus Estatus del CFDI.
         * @param cancelStatus Estatus de cancelación del CFDI.
         * @param areCfdiRelatedApplicable Aplicabilidad de obtención de CFDI relacionados del CFDI.
         */
        public CfdiAckQuery(String uuid, String cancellableInfo, String retrievalInfo, String cfdiStatus, String cancelStatus, boolean areCfdiRelatedApplicable) {
            Uuid = uuid;
            CancellableInfo = cancellableInfo;
            RetrievalInfo = retrievalInfo;
            CfdiStatus = cfdiStatus;
            CancelStatus = cancelStatus;
            AreCfdiRelatedApplicable = areCfdiRelatedApplicable;
            CfdiRelatedList = new ArrayList<>();
        }
        
        /**
         * Genera texto con CFDI relacionados del CFDI y mensaje de error asociado, si aplica.
         * @return 
         */
        public String composeCfdiRelated() {
            String message = "";
            
            if (AreCfdiRelatedApplicable) {
                if (CfdiRelatedList.isEmpty()) {
                    message = "Sin CFDI relacionados.";
                }
                else {
                    message = "Con " + CfdiRelatedList.size() + " CFDI " + (CfdiRelatedList.size() == 1 ? "relacionado" : "relacionados") + ":";
                    for (CfdiRelated cfdiRelated : CfdiRelatedList) {
                        message += "\n- " + cfdiRelated.composeMessage();
                    }
                }
                
                if (!msLastCfdiRelatedError.isEmpty()) {
                    message += (!message.isEmpty() ? "\n" : "") + msLastCfdiRelatedError;
                }
            }
            
            return message;
        }
        
        /**
         * Genera texto con CFDI relacionados del CFDI y mensaje de error asociado, si aplica.
         * @return 
         */
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
    }
    
    public class CfdiRelated {
        public String Tipo;
        public String Uuid;
        public String Issuer;
        public String Receiver;
        
        /**
         * Construye nuevo CFDI relacionado.
         * @param tipo Tipo de CFDI relacionado: PADRE o HIJO.
         * @param uuid UUID del CFDI relacionado.
         * @param issuer Emisor del CFDI relacionado.
         * @param receiver Receptor del CFDI relacionado.
         */
        public CfdiRelated(String tipo, String uuid, String issuer, String receiver) {
            Tipo = tipo;
            Uuid = uuid;
            Issuer = issuer;
            Receiver = receiver;
        }
        
        /**
         * Genera texto con la información del CFDI relacionado.
         * @return 
         */
        public String composeMessage() {
            return "Tipo: " + Tipo + "; UUID: " + Uuid + "; emisor: " + Issuer + "; receptor: " + Receiver + ".";
        }
    }
}
