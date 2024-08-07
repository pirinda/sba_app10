/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import cfd.DAttributeOptionCondicionesPago;
import cfd.DAttributeOptionImpuestoRetencion;
import cfd.DAttributeOptionImpuestoTraslado;
import cfd.DCfdConsts;
import cfd.DCfdUtils;
import cfd.DElement;
import cfd.ver32.DCfdiVer32Consts;
import cfd.ver40.DCfdi40Catalogs;
import cfd.ver40.DCfdi40Consts;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import javax.xml.bind.JAXBElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.bouncycastle.util.encoders.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibTimeConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiEdsSignature;
import sba.lib.gui.DGuiSession;
import sba.lib.xml.DXmlUtils;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.bpr.db.DDbBizPartner;
import sba.mod.bpr.db.DDbBranch;
import sba.mod.bpr.db.DDbBranchAddress;
import sba.mod.cfg.db.DDbCertificate;
import sba.mod.cfg.db.DDbConfigBranch;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.fin.db.DFinConsts;
import sba.mod.itm.db.DDbUnit;
import sba.mod.lad.db.DDbBol;
import sba.mod.lad.db.DDbBolLocation;
import sba.mod.lad.db.DDbBolMerchandise;
import sba.mod.lad.db.DDbBolMerchandiseMove;
import sba.mod.lad.db.DDbBolTransportFigure;
import sba.mod.lad.db.DDbBolTransportFigureTransportPart;
import sba.mod.lad.db.DDbBolTruck;
import sba.mod.lad.db.DDbBolTruckTrailer;
import sba.mod.lad.form.DFormBolUtils;

/**
 * Utilities related to CFD generation and PAC transaction control for CFD signing and cancelling.
 * 
 * @author Sergio Flores
 */
public abstract class DTrnDfrUtils {
    
    /**
     * Get text for request type.
     * @param xmsType Options supported: DModSysConsts.TX_XMS_REQ_TP_...
     * @return 
     */
    public static String getXmsRequestType(final int xmsType) {
        String type = "";
        
        switch (xmsType) {
            case DModSysConsts.TX_XMS_REQ_TP_SIG:
                type = "timbrado";
                break;
            case DModSysConsts.TX_XMS_REQ_TP_CAN:
                type = "cancelación";
                break;
            default:
                type = "?";
        }
        
        return type;
    }
    
    /**
     * Get text for request subtype.
     * @param xmsSubtype Options supported: DModSysConsts.TX_XMS_REQ_STP_...
     * @return 
     */
    public static String getXmsRequestSubype(final int xmsSubtype) {
        String subtype = "";
        
        switch (xmsSubtype) {
            case DModSysConsts.TX_XMS_REQ_STP_REQ:
                subtype = "solicitud";
                break;
            case DModSysConsts.TX_XMS_REQ_STP_VER:
                subtype = "verificación";
                break;
            default:
                subtype = "?";
        }
        
        return subtype;
    }
    
    /**
     * Extracts existing digital signature attributes from CFD's XML.
     * @param dps
     * @return
     * @throws Exception 
     */
    @Deprecated
    private static cfd.ver32.DSelloDigital extractSello32(final DDbDps dps) throws Exception {
        Document document = null;
        Node node = null;
        NamedNodeMap namedNodeMapChild = null;
        cfd.ver32.DSelloDigital selloDigital = null;
        
        if (dps.getChildDfr() != null) {
            selloDigital = new cfd.ver32.DSelloDigital();

            document = DXmlUtils.parseDocument(dps.getChildDfr().getDocXml());

            node = DXmlUtils.extractElements(document, "cfdi:Comprobante").item(0);
            namedNodeMapChild = node.getAttributes();

            selloDigital.setSello(DXmlUtils.extractAttributeValue(namedNodeMapChild, "sello", true));
            selloDigital.setNoCertificado(DXmlUtils.extractAttributeValue(namedNodeMapChild, "noCertificado", true));
            selloDigital.setCertificado(DXmlUtils.extractAttributeValue(namedNodeMapChild, "certificado", true));
        }
        
        return selloDigital;
    }

    /**
     * Extracts existing digital signature attributes from CFD's XML.
     * @param dps
     * @return
     * @throws Exception 
     */
    @Deprecated
    private static cfd.ver33.DSelloDigital extractSello33(final DDbDps dps) throws Exception {
        Document document = null;
        Node node = null;
        NamedNodeMap namedNodeMapChild = null;
        cfd.ver33.DSelloDigital selloDigital = null;
        
        if (dps.getChildDfr() != null) {
            selloDigital = new cfd.ver33.DSelloDigital();

            document = DXmlUtils.parseDocument(dps.getChildDfr().getDocXml());

            node = DXmlUtils.extractElements(document, "cfdi:Comprobante").item(0);
            namedNodeMapChild = node.getAttributes();

            selloDigital.setSello(DXmlUtils.extractAttributeValue(namedNodeMapChild, "Sello", true));
            selloDigital.setNoCertificado(DXmlUtils.extractAttributeValue(namedNodeMapChild, "NoCertificado", true));
            selloDigital.setCertificado(DXmlUtils.extractAttributeValue(namedNodeMapChild, "Certificado", true));
        }
        
        return selloDigital;
    }

    @Deprecated
    public static void configureCfdi32(final DGuiSession session, final cfd.ver32.DElementComprobante comprobante) {
        DDbConfigCompany configCompany = (DDbConfigCompany) session.getConfigCompany();
        int decimalsQuantity = configCompany.getDecimalsQuantity();
        int decimalsPriceUnitary = configCompany.getDecimalsPriceUnitary();
        
        for (cfd.ver32.DElementConcepto concepto : comprobante.getEltConceptos().getEltHijosConcepto()) {
            concepto.getAttCantidad().setDecimals(decimalsQuantity);
            concepto.getAttValorUnitario().setDecimals(decimalsPriceUnitary);
        }
    }
    
    @Deprecated
    public static void configureCfdi33(final DGuiSession session, final cfd.ver33.DElementComprobante comprobante) {
        DDbConfigCompany configCompany = (DDbConfigCompany) session.getConfigCompany();
        int decimalsQuantity = configCompany.getDecimalsQuantity();
        int decimals = configCompany.getDecimalsPriceUnitary();
        
        for (cfd.ver33.DElementConcepto concepto : comprobante.getEltConceptos().getEltConceptos()) {
            concepto.getAttCantidad().setDecimals(decimalsQuantity);
            concepto.getAttValorUnitario().setDecimals(decimals);
        }
    }
    
    public static void configureCfdi40(final DGuiSession session, final cfd.ver40.DElementComprobante comprobante) {
        DDbConfigCompany configCompany = (DDbConfigCompany) session.getConfigCompany();
        int decimalsQuantity = configCompany.getDecimalsQuantity();
        int decimals = configCompany.getDecimalsPriceUnitary();
        
        for (cfd.ver40.DElementConcepto concepto : comprobante.getEltConceptos().getEltConceptos()) {
            concepto.getAttCantidad().setDecimals(decimalsQuantity);
            concepto.getAttValorUnitario().setDecimals(decimals);
        }
        
        if (comprobante.getEltOpcComplemento() != null) {
            cfd.ver40.DElementComplemento complemento = comprobante.getEltOpcComplemento();

            for (cfd.DElement element : complemento.getElements()) {
                if (element instanceof cfd.ver40.crp20.DElementPagos) {
                    cfd.ver40.crp20.DElementPagos pagos = (cfd.ver40.crp20.DElementPagos) element;
                    for (cfd.ver40.crp20.DElementPagosPago pago : pagos.getEltPagos()) {
                        if (pago.getAttMonedaP().getString().equals(DCfdi40Catalogs.ClaveMonedaMxn)) {
                            pago.getAttTipoCambioP().setDouble(1);
                            pago.getAttTipoCambioP().setDecimals(0);
                        }
                        
                        for (cfd.ver40.crp20.DElementDoctoRelacionado doctoRelacionado : pago.getEltDoctoRelacionados()) {
                            if (doctoRelacionado.getAttMonedaDR().getString().equals(pago.getAttMonedaP().getString())) {
                                doctoRelacionado.getAttEquivalenciaDR().setDouble(1);
                                doctoRelacionado.getAttEquivalenciaDR().setDecimals(0);
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Deprecated
    public static cfd.ver2.DElementComprobante createCfd(final DGuiSession session, final DDbDps dps) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Deprecated
    public static cfd.ver32.DElementComprobante createCfdi32(final DGuiSession session, final DDbDps dps) throws Exception {
        int i = 0;
        int nPagos = 1;
        int nImptoTipo = 0;
        //int nMoneda = 0;              // Addenda1 not needed anymore
        double dImpto = 0;
        double dImptoAux = 0;
        double dImptoTasa = 0;
        double dTotalImptoRetenido = 0;
        double dTotalImptoTrasladado = 0;
        //double dTotalPesoBruto = 0;   // Addenda1 not needed anymore
        //double dTotalPesoNeto = 0;    // Addenda1 not needed anymore
        boolean subrogatedIssue = false;
        Double oValue = null;
        Date tDate = null;
        Date tUpdateTs = dps.getTsUserUpdate() != null ? dps.getTsUserUpdate() : new Date();
        int[] anDateDps = DLibTimeUtils.digestDate(dps.getDate());
        int[] anDateEdit = DLibTimeUtils.digestDate(tUpdateTs);
        String[] asRegimenes = null;
        GregorianCalendar oGregorianCalendar = null;
        ArrayList<DTrnImportDeclaration> aImportDeclarations = new ArrayList<>();
        ArrayList<cfd.ext.addenda1.DElementAdicionalConcepto> aAdicionalConceptos = new ArrayList<>();
        ArrayList<cfd.ext.addenda1.DElementNota> aNotas = new ArrayList<>();
        Set<Double> setKeyImptos = null;
        HashMap<Double, Double> hmImpto = null;
        HashMap<Double, Double> hmRetenidoIva = new HashMap<>();
        HashMap<Double, Double> hmRetenidoIsr = new HashMap<>();
        HashMap<Double, Double> hmTrasladadoIva = new HashMap<>();
        DDbConfigCompany configCompany = (DDbConfigCompany) session.getConfigCompany();
        DDbConfigBranch configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, dps.getCompanyBranchKey());
        DDbBizPartner bprEmisor = null;
        DDbBranch braEmisor = null;
        DDbBranchAddress braAddressEmisor = null;
        DDbBizPartner bprReceptor = null;
        DDbBizPartner bprReceptorName = null;
        DDbBranch braReceptor = null;
        DDbBranchAddress braAddressReceptor = null;
        String sNombreReceptor = "";
        
        // Emisor:

        if (configBranch.getFkBizPartnerDpsSignatureId_n() == DLibConsts.UNDEFINED) {
            // Document's emisor:
            
            subrogatedIssue = false;
            bprEmisor = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, dps.getCompanyKey());
            braEmisor = (DDbBranch) session.readRegistry(DModConsts.BU_BRA, dps.getCompanyBranchKey());
        }
        else {
            // Company branch's emisor:
            
            subrogatedIssue = true;
            bprEmisor = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, new int[] { configBranch.getFkBizPartnerDpsSignatureId_n() });
            braEmisor = (DDbBranch) session.readRegistry(DModConsts.BU_BRA, new int[] { configBranch.getFkBizPartnerDpsSignatureId_n(), DUtilConsts.BPR_BRA_ID });
        }

        // Receptor:

        switch (dps.getFkEmissionTypeId()) {
            case DModSysConsts.TS_EMI_TP_BPR:
                bprReceptor = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, dps.getBizPartnerKey());
                braReceptor = (DDbBranch) session.readRegistry(DModConsts.BU_BRA, dps.getBizPartnerBranchKey());
                break;
            case DModSysConsts.TS_EMI_TP_PUB_NAM:
            case DModSysConsts.TS_EMI_TP_PUB:
                bprReceptor = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, new int[] { configBranch.getFkPublicBizPartnerSaleId() });
                braReceptor = (DDbBranch) session.readRegistry(DModConsts.BU_BRA, new int[] { configBranch.getFkPublicBizPartnerSaleId(), DUtilConsts.BPR_BRA_ID });
                break;
            default:
        }

        if (dps.getFkEmissionTypeId() == DModSysConsts.TS_EMI_TP_PUB_NAM) {
            bprReceptorName = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, dps.getBizPartnerKey());
        }
        else {
            bprReceptorName = bprReceptor;
        }

        sNombreReceptor = bprReceptorName.getPrintableName();

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

        cfd.ver32.DElementComprobante comprobante = new cfd.ver32.DElementComprobante();

        comprobante.getAttSerie().setString(dps.getSeries());
        comprobante.getAttFolio().setString("" + dps.getNumber());
        comprobante.getAttFecha().setDatetime(tDate);
        
        if (dps.getChildDfr() != null) {
            // If DPS's XML already exists, retrieve too the digital signature attributes:
            
            cfd.ver32.DSelloDigital selloDigital = extractSello32(dps);
            
            comprobante.getAttSello().setString(selloDigital.getSello());
            comprobante.getAttNoCertificado().setString(selloDigital.getNoCertificado());
            comprobante.getAttCertificado().setString(selloDigital.getCertificado());
        }

        if (nPagos <= 1) {
            comprobante.getAttFormaDePago().setOption(DCfdiVer32Consts.CFD_PAGO_UNA_EXHIBICION);
        }
        else {
            comprobante.getAttFormaDePago().setOption(DCfdiVer32Consts.CFD_PAGO_PARCIALIDADES + " " + nPagos);
        }

        comprobante.getAttCondicionesDePago().setOption(dps.getFkPaymentTypeId() == DModSysConsts.FS_PAY_TP_CSH ? DAttributeOptionCondicionesPago.CFD_CONTADO : DAttributeOptionCondicionesPago.CFD_CREDITO);
        comprobante.getAttSubTotal().setDouble(dps.getSubtotalProvCy_r());

        if (dps.getDiscountDocCy_r() > 0) {
            comprobante.getAttDescuento().setDouble(dps.getDiscountDocCy_r());
            comprobante.getAttMotivoDescuento().setString("DESCUENTO");
        }

        comprobante.getAttTipoCambio().setDouble(dps.getExchangeRate());
        comprobante.getAttMoneda().setString(session.getSessionCustom().getCurrencyCode(dps.getCurrencyKey()));
        comprobante.getAttTotal().setDouble(dps.getTotalCy_r());

        comprobante.getAttTipoDeComprobante().setOption(dps.isDpsDocument() ? DCfdiVer32Consts.CFD_INGRESO : DCfdiVer32Consts.CFD_EGRESO);
        comprobante.getAttMetodoDePago().setString(DLibUtils.textRight("" + dps.getFkModeOfPaymentTypeId(), 2));    // ID of type of mode of payment matches with required value

        braAddressEmisor = bprEmisor.getChildBranchHeadquarters().getChildAddressOfficial();

        comprobante.getEltEmisor().getAttRfc().setString(bprEmisor.getFiscalId());
        comprobante.getEltEmisor().getAttNombre().setString(bprEmisor.getName());

        comprobante.getEltEmisor().getEltDomicilioFiscal().getAttCalle().setString(braAddressEmisor.getAddress1());
        comprobante.getEltEmisor().getEltDomicilioFiscal().getAttNoExterior().setString(braAddressEmisor.getNumberExterior());
        comprobante.getEltEmisor().getEltDomicilioFiscal().getAttNoInterior().setString(braAddressEmisor.getNumberInterior());
        comprobante.getEltEmisor().getEltDomicilioFiscal().getAttColonia().setString(braAddressEmisor.getAddress2());
        comprobante.getEltEmisor().getEltDomicilioFiscal().getAttLocalidad().setString(braAddressEmisor.getLocality());
        comprobante.getEltEmisor().getEltDomicilioFiscal().getAttReferencia().setString(braAddressEmisor.getAddress3());
        comprobante.getEltEmisor().getEltDomicilioFiscal().getAttMunicipio().setString(braAddressEmisor.getCounty());
        comprobante.getEltEmisor().getEltDomicilioFiscal().getAttEstado().setString(braAddressEmisor.getState());
        comprobante.getEltEmisor().getEltDomicilioFiscal().getAttCodigoPostal().setString(braAddressEmisor.getZipCode());
        comprobante.getEltEmisor().getEltDomicilioFiscal().getAttPais().setString(braAddressEmisor.getFkCountryId_n() == DLibConsts.UNDEFINED ? session.getSessionCustom().getLocalCountry() : session.getSessionCustom().getCountry(new int[] { braAddressEmisor.getFkCountryId_n() }));

        if (!DLibUtils.compareKeys(braEmisor.getPrimaryKey(), dps.getCompanyBranchKey()) && !subrogatedIssue) {
            braAddressEmisor = bprEmisor.getChildBranch(dps.getCompanyBranchKey()).getChildAddressOfficial();

            comprobante.getEltEmisor().setEltOpcExpedidoEn(new cfd.ver32.DElementTipoUbicacion("cfdi:ExpedidoEn"));

            comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttCalle().setString(braAddressEmisor.getAddress1());
            comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttNoExterior().setString(braAddressEmisor.getNumberExterior());
            comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttNoInterior().setString(braAddressEmisor.getNumberInterior());
            comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttColonia().setString(braAddressEmisor.getAddress2());
            comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttLocalidad().setString(braAddressEmisor.getLocality());
            comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttReferencia().setString(braAddressEmisor.getAddress3());
            comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttMunicipio().setString(braAddressEmisor.getCounty());
            comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttEstado().setString(braAddressEmisor.getState());
            comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttCodigoPostal().setString(braAddressEmisor.getZipCode());
            comprobante.getEltEmisor().getEltOpcExpedidoEn().getAttPais().setString(braAddressEmisor.getFkCountryId_n() == DLibConsts.UNDEFINED ? session.getSessionCustom().getLocalCountry() : session.getSessionCustom().getCountry(new int[] { braAddressEmisor.getFkCountryId_n() }));
        }

        comprobante.getAttLugarExpedicion().setString(braAddressEmisor.getLocality() + ", " + braAddressEmisor.getState());
        comprobante.getAttNumCtaPago().setString(dps.getPaymentAccount());

        asRegimenes = DLibUtils.textExplode(configCompany.getFiscalIdentity(), ";");

        for (String regimen : asRegimenes) {
            cfd.ver32.DElementRegimenFiscal regimenFiscal = new cfd.ver32.DElementRegimenFiscal();
            regimenFiscal.getAttRegimen().setString(regimen);
            comprobante.getEltEmisor().getEltHijosRegimenFiscal().add(regimenFiscal);
        }

        comprobante.getEltReceptor().getAttRfc().setString(bprReceptor.getFiscalId());
        comprobante.getEltReceptor().getAttNombre().setString(sNombreReceptor);

        if(braReceptor.isAddressPrintable()) {
            braAddressReceptor = braReceptor.getChildAddressOfficial();
        }
        else {
            braAddressReceptor = bprReceptor.getChildBranchHeadquarters().getChildAddressOfficial();
        }

        comprobante.getEltReceptor().getEltDomicilio().getAttCalle().setString(braAddressReceptor.getAddress1());
        comprobante.getEltReceptor().getEltDomicilio().getAttNoExterior().setString(braAddressReceptor.getNumberExterior());
        comprobante.getEltReceptor().getEltDomicilio().getAttNoInterior().setString(braAddressReceptor.getNumberInterior());
        comprobante.getEltReceptor().getEltDomicilio().getAttColonia().setString(braAddressReceptor.getAddress2());
        comprobante.getEltReceptor().getEltDomicilio().getAttLocalidad().setString(braAddressReceptor.getLocality());
        comprobante.getEltReceptor().getEltDomicilio().getAttReferencia().setString(braAddressReceptor.getAddress3());
        comprobante.getEltReceptor().getEltDomicilio().getAttMunicipio().setString(braAddressReceptor.getCounty());
        comprobante.getEltReceptor().getEltDomicilio().getAttEstado().setString(braAddressReceptor.getState());
        comprobante.getEltReceptor().getEltDomicilio().getAttCodigoPostal().setString(braAddressReceptor.getZipCode());
        comprobante.getEltReceptor().getEltDomicilio().getAttPais().setString(braAddressReceptor.getFkCountryId_n() == DLibConsts.UNDEFINED ? session.getSessionCustom().getLocalCountry() : session.getSessionCustom().getCountry(new int[] { braAddressReceptor.getFkCountryId_n() }));

        for (DDbDpsRow dpsRow : dps.getChildRows()) {
            if (!dpsRow.isDeleted()) {
                // "Concepto" node:

                cfd.ver32.DElementConcepto concepto = new cfd.ver32.DElementConcepto();
                cfd.ext.addenda1.DElementAdicionalConcepto adicionalConcepto = new cfd.ext.addenda1.DElementAdicionalConcepto();

                concepto.getAttNoIdentificacion().setString(dpsRow.getCode());
                concepto.getAttUnidad().setString(dpsRow.getDbUnitCode());
                concepto.getAttCantidad().setDouble(dpsRow.getQuantity() == 0 ? 1 : dpsRow.getQuantity());
                concepto.getAttCantidad().setDecimals(configCompany.getDecimalsQuantity());
                concepto.getAttDescripcion().setString(dpsRow.getName());
                concepto.getAttValorUnitario().setDecimals(configCompany.getDecimalsPriceUnitary());
                concepto.getAttValorUnitario().setDouble(dpsRow.getPriceUnitaryCy());
                concepto.getAttImporte().setDouble(dpsRow.getSubtotalProvCy_r());

                // "InformacionAduanera" child nodes:

                aImportDeclarations = DTrnDocRowUtils.getDpsRowImportDeclarations(session, dpsRow.getPrimaryKey());
                for (DTrnImportDeclaration importDeclaration : aImportDeclarations) {
                    cfd.ver32.DElementInformacionAduanera informacionAduanera = new cfd.ver32.DElementInformacionAduanera();

                    informacionAduanera.getAttNumero().setString(importDeclaration.getNumber());
                    informacionAduanera.getAttFecha().setDate(importDeclaration.getDate());
                    informacionAduanera.getAttAduana().setString("");

                    concepto.getEltHijosInformacionAduanera().add(informacionAduanera);
                }

                comprobante.getEltConceptos().getEltHijosConcepto().add(concepto);

                // "AdicionalConcepto" node:

                adicionalConcepto.getAttClaveConcepto().setString(dpsRow.getCode());
                adicionalConcepto.getAttPresentacion().setString("");
                adicionalConcepto.getAttDescuentoUnitario().setDouble(dpsRow.getDiscountUnitaryCy());
                adicionalConcepto.getAttDescuento().setDouble(dpsRow.getDiscountRowCy());
                adicionalConcepto.getAttPesoBruto().setDouble(dpsRow.getWeightGross());
                adicionalConcepto.getAttPesoNeto().setDouble(dpsRow.getMeasurementMass());

                /* Addenda1 not needed anymore:
                dTotalPesoBruto += dpsRow.getWeightGross();
                dTotalPesoNeto += dpsRow.getMeasurementMass();
                */
                aNotas.clear();

                for (DDbDpsRowNote note : dpsRow.getChildRowNotes()) {
                    if (note.isPrintable()) {
                        cfd.ext.addenda1.DElementNota nota = new cfd.ext.addenda1.DElementNota();
                        nota.getAttTexto().setString(note.getText());
                        aNotas.add(nota);
                    }
                }

                if (aNotas.size() > 0) {
                    cfd.ext.addenda1.DElementNotas notas = new cfd.ext.addenda1.DElementNotas();
                    notas.getEltHijosNota().addAll(aNotas);
                    adicionalConcepto.setEltOpcNotas(notas);
                }

                aAdicionalConceptos.add(adicionalConcepto);

                // Document row retained taxes:

                dImptoTasa = 1; // CFDI retained tax has no rate

                for (i = 1; i <= 3; i++) {
                    switch (i) {
                        case 1:
                            nImptoTipo = dpsRow.getFkTaxRetained1Id();
                            dImptoAux = dpsRow.getTaxRetained1Cy();
                            break;
                        case 2:
                            nImptoTipo = dpsRow.getFkTaxRetained2Id();
                            dImptoAux = dpsRow.getTaxRetained2Cy();
                            break;
                        case 3:
                            nImptoTipo = dpsRow.getFkTaxRetained3Id();
                            dImptoAux = dpsRow.getTaxRetained3Cy();
                            break;
                    }

                    switch (nImptoTipo) {
                        case 1:
                            continue;
                        case 2:
                            hmImpto = hmRetenidoIva;
                            break;
                        case 3:
                            hmImpto = hmRetenidoIsr;
                            break;
                        default:
                            throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n El impuesto retenido no existe (" + nImptoTipo + ").");
                    }

                    oValue = hmImpto.get(dImptoTasa);
                    dImpto = oValue == null ? 0 : oValue.doubleValue();
                    hmImpto.put(dImptoTasa, dImpto + dImptoAux);
                }

                // Document row charged taxes:

                for (i = 1; i <= 3; i++) {
                    switch (i) {
                        case 1:
                            nImptoTipo = dpsRow.getFkTaxCharged1Id();
                            dImptoTasa = dpsRow.getTaxChargedRate1();
                            dImptoAux = dpsRow.getTaxCharged1Cy();
                            break;
                        case 2:
                            nImptoTipo = dpsRow.getFkTaxCharged2Id();
                            dImptoTasa = dpsRow.getTaxChargedRate2();
                            dImptoAux = dpsRow.getTaxCharged2Cy();
                            break;
                        case 3:
                            nImptoTipo = dpsRow.getFkTaxCharged3Id();
                            dImptoTasa = dpsRow.getTaxChargedRate3();
                            dImptoAux = dpsRow.getTaxCharged3Cy();
                            break;
                    }

                    switch (nImptoTipo) {
                        case 1:
                            continue;
                        case 2:
                            hmImpto = hmTrasladadoIva;
                            break;
                        default:
                            throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n El impuesto trasladado no existe (" + nImptoTipo + ").");
                    }

                    oValue = hmImpto.get(dImptoTasa);
                    dImpto = oValue == null ? 0 : oValue.doubleValue();
                    hmImpto.put(dImptoTasa, dImpto + dImptoAux);
                }
            }
        }

        // Retained taxes:

        cfd.ver32.DElementImpuestosRetenidos impuestosRetenidos = new cfd.ver32.DElementImpuestosRetenidos();

        hmImpto = hmRetenidoIva;
        if (!hmImpto.isEmpty()) {
            setKeyImptos = hmImpto.keySet();
            for (Double key : setKeyImptos) {
                cfd.ver32.DElementImpuestoRetencion impuestoRetencion = new cfd.ver32.DElementImpuestoRetencion();

                dImpto = hmImpto.get(key);
                if (dImpto != 0) {
                    dTotalImptoRetenido += dImpto;
                    impuestoRetencion.getAttImpuesto().setOption(DAttributeOptionImpuestoRetencion.CFD_IVA);
                    impuestoRetencion.getAttImporte().setDouble(dImpto);

                    impuestosRetenidos.getEltHijosImpuestoRetenido().add(impuestoRetencion);
                }
            }
        }

        hmImpto = hmRetenidoIsr;
        if (!hmImpto.isEmpty()) {
            setKeyImptos = hmImpto.keySet();
            for (Double key : setKeyImptos) {
                cfd.ver32.DElementImpuestoRetencion impuestoRetencion = new cfd.ver32.DElementImpuestoRetencion();

                dImpto = hmImpto.get(key);
                if (dImpto != 0) {
                    dTotalImptoRetenido += dImpto;
                    impuestoRetencion.getAttImpuesto().setOption(DAttributeOptionImpuestoRetencion.CFD_ISR);
                    impuestoRetencion.getAttImporte().setDouble(dImpto);

                    impuestosRetenidos.getEltHijosImpuestoRetenido().add(impuestoRetencion);
                }
            }
        }

        if (impuestosRetenidos.getEltHijosImpuestoRetenido().size() > 0) {
            comprobante.getEltImpuestos().getAttTotalImpuestosRetenidos().setDouble(dTotalImptoRetenido);
            comprobante.getEltImpuestos().setEltOpcImpuestosRetenidos(impuestosRetenidos);
        }

        // Charged taxes:

        cfd.ver32.DElementImpuestosTrasladados impuestosTrasladados = new cfd.ver32.DElementImpuestosTrasladados();

        hmImpto = hmTrasladadoIva;
        if (!hmImpto.isEmpty()) {
            setKeyImptos = hmImpto.keySet();
            for (Double key : setKeyImptos) {
                cfd.ver32.DElementImpuestoTraslado impuestoTraslado = new cfd.ver32.DElementImpuestoTraslado();

                dImpto = hmImpto.get(key);
                if (dImpto != 0) {
                    dTotalImptoTrasladado += dImpto;
                    impuestoTraslado.getAttImpuesto().setOption(DAttributeOptionImpuestoTraslado.CFD_IVA);
                    impuestoTraslado.getAttTasa().setDouble(key * 100); // tax rate as "integer" value
                    impuestoTraslado.getAttImporte().setDouble(dImpto);

                    impuestosTrasladados.getEltHijosImpuestoTrasladado().add(impuestoTraslado);
                }
            }
        }

        if (impuestosTrasladados.getEltHijosImpuestoTrasladado().size() > 0) {
            comprobante.getEltImpuestos().getAttTotalImpuestosTrasladados().setDouble(dTotalImptoTrasladado);
            comprobante.getEltImpuestos().setEltOpcImpuestosTrasladados(impuestosTrasladados);
        }

        /* Addenda1 not needed anymore:
        // Addenda:

        switch (dps.getFkCurrencyId()) {
            case 104: // MXN
                nMoneda = DAttributeOptionMoneda.CFD_MXN;
                break;
            case 151: // USD
                nMoneda = DAttributeOptionMoneda.CFD_USD;
                break;
            case 51: // EUR
                nMoneda = DAttributeOptionMoneda.CFD_EUR;
                break;
            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN + "\n La moneda no existe (" + dps.getFkCurrencyId() + ").");
        }
        */
        
        /*
        (Signed CFDI will be only gotten fetching it from certification provider, 2017-12-18, sflores)
        
        if (timbreFiscal_n != null) {
            // Create element Complemento:

            cfd.ver32.DElementComplemento complemento = new cfd.ver32.DElementComplemento();
            cfd.ver32.DElementTimbreFiscalDigital timbreFiscalDigital = new cfd.ver32.DElementTimbreFiscalDigital();

            timbreFiscalDigital.getAttVersion().setString(timbreFiscal_n.getVersion());
            timbreFiscalDigital.getAttUUID().setString(timbreFiscal_n.getUuid());
            timbreFiscalDigital.getAttFechaTimbrado().setString(timbreFiscal_n.getFechaTimbrado());
            timbreFiscalDigital.getAttSelloCFD().setString(timbreFiscal_n.getSelloCfd());
            timbreFiscalDigital.getAttNoCertificadoSAT().setString(timbreFiscal_n.getNoCertificadoSat());
            timbreFiscalDigital.getAttSelloSAT().setString(timbreFiscal_n.getSelloSat());

            complemento.getElements().add(timbreFiscalDigital);

            comprobante.setEltOpcComplemento(complemento);

            // Addenda1 not needed anymore:
            // Create element Addenda1:

            DElementAddenda1 addenda1 = new DElementAddenda1();

            addenda1.getEltMoneda().getAttClaveMoneda().setOption(nMoneda);
            addenda1.getEltMoneda().getAttTipoDeCambio().setDouble(dps.getExchangeRate());
            addenda1.getEltAdicional().getAttDiasDeCredito().setInteger(dps.getCreditDays());
            addenda1.getEltAdicional().getAttEmbarque().setString("");
            addenda1.getEltAdicional().getAttOrdenDeEmbarque().setString("");
            addenda1.getEltAdicional().getAttOrdenDeCompra().setString(dps.getOrder());
            addenda1.getEltAdicional().getAttContrato().setString("");
            addenda1.getEltAdicional().getAttPedido().setString("");
            addenda1.getEltAdicional().getAttCliente().setString(bprReceptor.getChildConfig(DModSysConsts.BS_BPR_CL_CUS).getCode());
            addenda1.getEltAdicional().getAttSucursal().setString(braEmisor.getCode());
            addenda1.getEltAdicional().getAttAgente().setString("" + dps.getFkAgentId_n());
            addenda1.getEltAdicional().getAttRuta().setString("");
            addenda1.getEltAdicional().getAttChofer().setString("");
            addenda1.getEltAdicional().getAttPlacas().setString("");
            addenda1.getEltAdicional().getAttBoleto().setString("");
            addenda1.getEltAdicional().getAttPesoBruto().setDouble(dTotalPesoBruto);
            addenda1.getEltAdicional().getAttPesoNeto().setDouble(dTotalPesoNeto);
            addenda1.getEltAdicional().getAttUnidadPesoBruto().setString("kg");
            addenda1.getEltAdicional().getAttUnidadPesoNeto().setString("kg");
            addenda1.getEltAdicional().getEltAdicionalConceptos().getEltHijosAdicionalConcepto().addAll(aAdicionalConceptos);

            aNotas.clear();

            for (DDbDpsNote note : dps.getChildNotes()) {
                if (note.isPrintable()) {
                    DElementNota nota = new DElementNota();
                    nota.getAttTexto().setString(note.getText());
                    aNotas.add(nota);
                }
            }

            if (aNotas.size() > 0) {
                DElementNotas notas = new DElementNotas();
                notas.getEltHijosNota().addAll(aNotas);
                addenda1.getEltAdicional().setEltOpcNotas(notas);
            }

            DElementPagare pagare = new DElementPagare();

            pagare.getAttFecha().setDate(dps.getDate());
            pagare.getAttFechaDeVencimiento().setDate(DLibTimeUtils.addDate(dps.getDateCredit(), 0, 0, dps.getCreditDays()));
            pagare.getAttImporte().setDouble(dps.getTotalCy_r());
            pagare.getAttClaveMoneda().setString(session.getSessionCustom().getCurrencyCode(dps.getCurrencyKey()));
            pagare.getAttInteresMoratorio().setDouble(((DDbConfigCompany) session.getConfigCompany()).getDelayInterestRate());

            addenda1.setEltOpcPagare(pagare);

            cfd.ver3.DElementAddenda addenda = new cfd.ver3.DElementAddenda();

            addenda.getElements().add(addenda1);

            comprobante.setEltOpcAddenda(addenda);
        }
        */

        return comprobante;
    }
    
    @Deprecated
    public static cfd.ver33.DElementComprobante createCfdi33(final DGuiSession session, final DDbDps dps, final DGuiEdsSignature signature) throws Exception {
        int decs = DLibUtils.getDecimalFormatAmount().getMaximumFractionDigits();
        int decsTax = 6;
        Date tDate = null;
        Date tUpdateTs = dps.getTsUserUpdate() != null ? dps.getTsUserUpdate() : new Date();
        int[] anDateDps = DLibTimeUtils.digestDate(dps.getDate());
        int[] anDateEdit = DLibTimeUtils.digestDate(tUpdateTs);
        GregorianCalendar oGregorianCalendar = null;
        DDbConfigCompany configCompany = (DDbConfigCompany) session.getConfigCompany();
        DDbConfigBranch configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, dps.getCompanyBranchKey());
        DDbBizPartner bprEmisor = null;
        DDbBranch braEmisor = null;
        DDbBranchAddress braAddressEmisor = null;
        DDbBizPartner bprReceptor = null;
        DDbBizPartner bprReceptorName = null;
        DDbBranch braReceptor = null;
        
        // Check company branch emission configuration:

        // Emisor:

        if (configBranch.getFkBizPartnerDpsSignatureId_n() == DLibConsts.UNDEFINED) {
            // Document's emisor:
            
            bprEmisor = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, dps.getCompanyKey());
            braEmisor = (DDbBranch) session.readRegistry(DModConsts.BU_BRA, dps.getCompanyBranchKey());
        }
        else {
            // Company branch's emisor:
            
            bprEmisor = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, new int[] { configBranch.getFkBizPartnerDpsSignatureId_n() });
            braEmisor = (DDbBranch) session.readRegistry(DModConsts.BU_BRA, new int[] { configBranch.getFkBizPartnerDpsSignatureId_n(), DUtilConsts.BPR_BRA_ID });
        }

        // Receptor:

        switch (dps.getFkEmissionTypeId()) {
            case DModSysConsts.TS_EMI_TP_BPR:
                bprReceptor = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, dps.getBizPartnerKey());
                braReceptor = (DDbBranch) session.readRegistry(DModConsts.BU_BRA, dps.getBizPartnerBranchKey());
                break;
            case DModSysConsts.TS_EMI_TP_PUB_NAM:
            case DModSysConsts.TS_EMI_TP_PUB:
                bprReceptor = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, new int[] { configBranch.getFkPublicBizPartnerSaleId() });
                braReceptor = (DDbBranch) session.readRegistry(DModConsts.BU_BRA, new int[] { configBranch.getFkPublicBizPartnerSaleId(), DUtilConsts.BPR_BRA_ID });
                break;
            default:
        }

        if (dps.getFkEmissionTypeId() == DModSysConsts.TS_EMI_TP_PUB_NAM) {
            bprReceptorName = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, dps.getBizPartnerKey());
        }
        else {
            bprReceptorName = bprReceptor;
        }

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

        comprobante.getAttSerie().setString(dps.getSeries());
        comprobante.getAttFolio().setString("" + dps.getNumber());
        comprobante.getAttFecha().setDatetime(tDate);
        
        comprobante.getAttSello().setString("");
        comprobante.getAttNoCertificado().setString(signature.getCertificateNumber());
        comprobante.getAttCertificado().setString(signature.getCertificateBase64());

        comprobante.getAttFormaPago().setString((String) session.readField(DModConsts.FS_MOP_TP, new int[] { dps.getFkModeOfPaymentTypeId() }, DDbRegistry.FIELD_CODE));
        comprobante.getAttCondicionesDePago().setString(dps.getXtaDfrMate().getPaymentTerms());
        
        comprobante.getAttSubTotal().setDouble(dps.getSubtotalProvCy_r());

        if (dps.getDiscountDocCy_r() > 0) {
            comprobante.getAttDescuento().setDouble(dps.getDiscountDocCy_r());
        }

        comprobante.getAttMoneda().setString(session.getSessionCustom().getCurrencyCode(dps.getCurrencyKey()));
        if (!session.getSessionCustom().isLocalCurrency(dps.getCurrencyKey())) {
            comprobante.getAttTipoCambio().setDouble(dps.getExchangeRate());
        }
        
        comprobante.getAttTotal().setDouble(dps.getTotalCy_r());

        comprobante.getAttTipoDeComprobante().setString(dps.isDpsDocument() ? DCfdi40Catalogs.CFD_TP_I : DCfdi40Catalogs.CFD_TP_E);
        comprobante.getAttMetodoPago().setString(dps.getXtaDfrMate().getMethodOfPayment());
        
        braAddressEmisor = braEmisor.getChildAddressOfficial();
        comprobante.getAttLugarExpedicion().setString(braAddressEmisor.getZipCode());
        
        comprobante.getAttConfirmacion().setString(dps.getXtaDfrMate().getConfirmation());
        
        // element 'CfdiRelacionados':
        if (dps.getXtaDfrMate().getRelations() != null && !dps.getXtaDfrMate().getRelations().getRelatedCfds().isEmpty()) {
            DDfrMateRelations.RelatedCfd relatedCfd = dps.getXtaDfrMate().getRelations().getRelatedCfds().get(0);
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
        comprobante.getEltEmisor().getAttRegimenFiscal().setString(dps.getXtaDfrMate().getIssuerTaxRegime());

        // element 'Receptor':
        comprobante.getEltReceptor().getAttRfc().setString(bprReceptor.getFiscalId());
        comprobante.getEltReceptor().getAttNombre().setString(bprReceptorName.getPrintableName());
        //comprobante.getEltReceptor().getAttResidenciaFiscal().setString(""); // not supported yet!
        //comprobante.getEltReceptor().getAttNumRegIdTrib().setString(""); // not supported yet!
        comprobante.getEltReceptor().getAttUsoCFDI().setString(dps.getXtaDfrMate().getCfdUsage());

        // element 'Conceptos':
        
        HashMap<Double, Double> mapTaxIva = new HashMap<>();   //tax rate, tax amount
        HashMap<String, Double> mapRetIva = new HashMap<>();   //retention code, retained amount
        HashMap<String, Double> mapRetIsr = new HashMap<>();   //retention code, retained amount
        
        for (DDbDpsRow dpsRow : dps.getChildRows()) {
            if (!dpsRow.isDeleted()) {
                // "Concepto" node:

                cfd.ver33.DElementConcepto concepto = new cfd.ver33.DElementConcepto();
                
                String notes = "";
                for (DDbDpsRowNote note : dpsRow.getChildRowNotes()) {
                    if (note.isDfr()) {
                        notes += (notes.isEmpty() ? "" : " ") + note.getText();
                    }
                }

                concepto.getAttClaveProdServ().setString(dpsRow.getDfrItemKey());
                concepto.getAttNoIdentificacion().setString(dpsRow.getCode());
                concepto.getAttCantidad().setDouble(dpsRow.getQuantity() == 0 ? 1 : dpsRow.getQuantity());
                concepto.getAttCantidad().setDecimals(configCompany.getDecimalsQuantity());
                concepto.getAttClaveUnidad().setString(dpsRow.getDfrUnitKey());
                concepto.getAttUnidad().setString(dpsRow.getDbUnitCode());
                concepto.getAttDescripcion().setString(dpsRow.getName() + (notes.isEmpty() ? "" : " " + notes));
                concepto.getAttValorUnitario().setDecimals(configCompany.getDecimalsPriceUnitary());
                concepto.getAttValorUnitario().setDouble(dpsRow.getPriceUnitaryCy());
                concepto.getAttImporte().setDouble(dpsRow.getSubtotalProvCy_r());
                concepto.getAttDescuento().setDouble(dpsRow.getDiscountDocCy());

                // "Impuestos" node:
                
                // "ImpuestosTrasladados" node:
                
                cfd.ver33.DElementConceptoImpuestosTraslados impuestosTraslados = null;
                
                //go through the 3 possible taxes on each document row:
                
                for (int index = 1; index <= 3; index++) {
                    int taxId = DLibConsts.UNDEFINED;
                    double taxRate = 0;
                    double taxAmount = 0;
                    String taxCode = "";
                    HashMap<Double, Double> mapTax = null; //tax rate, tax amount
                    
                    switch (index) {
                        case 1:
                            taxId = dpsRow.getFkTaxCharged1Id();
                            taxRate = dpsRow.getTaxChargedRate1();
                            taxAmount = dpsRow.getTaxCharged1Cy();
                            break;
                        case 2:
                            taxId = dpsRow.getFkTaxCharged2Id();
                            taxRate = dpsRow.getTaxChargedRate2();
                            taxAmount = dpsRow.getTaxCharged2Cy();
                            break;
                        case 3:
                            taxId = dpsRow.getFkTaxCharged3Id();
                            taxRate = dpsRow.getTaxChargedRate3();
                            taxAmount = dpsRow.getTaxCharged3Cy();
                            break;
                        default:
                    }
                    
                    if (taxId != DLibConsts.UNDEFINED) {
                        switch (taxId) {
                            case 1: // (N/A)
                                break;
                            case 2: // IVA
                                taxCode = DCfdi40Catalogs.IMP_IVA;
                                mapTax = mapTaxIva;
                                break;
                            case 3: // ISR
                                break;
                            default:
                                throw new Exception (DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                        }
                        
                        if (mapTax != null) {
                            Double taxAcum = mapTax.get(taxRate);
                            
                            if (taxAcum == null) {
                                mapTax.put(taxRate, taxAmount);
                            }
                            else {
                                mapTax.put(taxRate, DLibUtils.round(taxAcum + taxAmount, decs));
                            }
                            
                            cfd.ver33.DElementConceptoImpuestoTraslado impuestoTraslado = new cfd.ver33.DElementConceptoImpuestoTraslado();
                            impuestoTraslado.getAttBase().setDouble(dpsRow.getSubtotalCy_r());
                            impuestoTraslado.getAttImpuesto().setString(taxCode);
                            impuestoTraslado.getAttTipoFactor().setString(DCfdi40Catalogs.FAC_TP_TASA);
                            impuestoTraslado.getAttTasaOCuota().setDouble(DLibUtils.round(taxRate, decsTax));
                            impuestoTraslado.getAttImporte().setDouble(DLibUtils.round(taxAmount, decs));
                            
                            if (impuestosTraslados == null) {
                                impuestosTraslados = new cfd.ver33.DElementConceptoImpuestosTraslados();
                            }
                            impuestosTraslados.getEltImpuestoTrasladados().add(impuestoTraslado);
                        }
                    }
                }
                
                // "ImpuestosRetenidos" node:
                
                cfd.ver33.DElementConceptoImpuestosRetenciones impuestosRetenciones = null;
                
                //go through the 3 possible taxes on each document row:
                
                for (int index = 1; index <= 3; index++) {
                    int retId = DLibConsts.UNDEFINED;
                    double retRate = 0;
                    double retAmount = 0;
                    String retCode = "";
                    HashMap<String, Double> mapRet = null; //retention code, retained amount
                    
                    switch (index) {
                        case 1:
                            retId = dpsRow.getFkTaxRetained1Id();
                            retRate = dpsRow.getTaxRetainedRate1();
                            retAmount = dpsRow.getTaxRetained1Cy();
                            break;
                        case 2:
                            retId = dpsRow.getFkTaxRetained2Id();
                            retRate = dpsRow.getTaxRetainedRate2();
                            retAmount = dpsRow.getTaxRetained2Cy();
                            break;
                        case 3:
                            retId = dpsRow.getFkTaxRetained3Id();
                            retRate = dpsRow.getTaxRetainedRate3();
                            retAmount = dpsRow.getTaxRetained3Cy();
                            break;
                        default:
                    }
                    
                    if (retId != DLibConsts.UNDEFINED) {
                        switch (retId) {
                            case 1: // (N/A)
                                break;
                            case 2: // IVA
                                retCode = DCfdi40Catalogs.IMP_IVA;
                                mapRet = mapRetIva;
                                break;
                            case 3: // ISR
                                retCode = DCfdi40Catalogs.IMP_ISR;
                                mapRet = mapRetIsr;
                                break;
                            default:
                                throw new Exception (DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                        }
                        
                        if (mapRet != null) {
                            Double retAcum = mapRet.get(retCode);
                            
                            if (retAcum == null) {
                                mapRet.put(retCode, retAmount);
                            }
                            else {
                                mapRet.put(retCode, DLibUtils.round(retAcum + retAmount, decs));
                            }
                            
                            cfd.ver33.DElementConceptoImpuestoRetencion impuestoRetencion = new cfd.ver33.DElementConceptoImpuestoRetencion();
                            impuestoRetencion.getAttBase().setDouble(dpsRow.getSubtotalCy_r());
                            impuestoRetencion.getAttImpuesto().setString(retCode);
                            impuestoRetencion.getAttTipoFactor().setString(DCfdi40Catalogs.FAC_TP_TASA);
                            impuestoRetencion.getAttTasaOCuota().setDouble(DLibUtils.round(retRate, decsTax));
                            impuestoRetencion.getAttImporte().setDouble(DLibUtils.round(retAmount, decs));
                            
                            if (impuestosRetenciones == null) {
                                impuestosRetenciones = new cfd.ver33.DElementConceptoImpuestosRetenciones();
                            }
                            impuestosRetenciones.getEltImpuestoRetenciones().add(impuestoRetencion);
                        }
                    }
                }
                
                if (impuestosTraslados != null || impuestosRetenciones != null) {
                    cfd.ver33.DElementConceptoImpuestos impuestos = new cfd.ver33.DElementConceptoImpuestos();
                    
                    if (impuestosTraslados != null) {
                        impuestos.setEltOpcImpuestosTrasladados(impuestosTraslados);
                    }
                    
                    if (impuestosRetenciones != null) {
                        impuestos.setEltOpcImpuestosRetenciones(impuestosRetenciones);
                    }
                    
                    concepto.setEltOpcConceptoImpuestos(impuestos);
                }

                // "InformacionAduanera" child nodes:

                ArrayList<DTrnImportDeclaration> aImportDeclarations = DTrnDocRowUtils.getDpsRowImportDeclarations(session, dpsRow.getPrimaryKey());
                if (!aImportDeclarations.isEmpty()) {
                    
                    for (DTrnImportDeclaration importDeclaration : aImportDeclarations) {
                        cfd.ver33.DElementConceptoInformacionAduanera informacionAduanera = new cfd.ver33.DElementConceptoInformacionAduanera();

                        informacionAduanera.getAttNumeroPedimento().setString(importDeclaration.getNumberFormatted());

                        concepto.getEltOpcConceptoInformacionAduaneras().add(informacionAduanera);
                    }
                }

                // "CuentaPredial" child node:
                
                if (!dpsRow.getPredial().isEmpty()) {
                    cfd.ver33.DElementConceptoCuentaPredial cuentaPredial = new cfd.ver33.DElementConceptoCuentaPredial();
                    
                    cuentaPredial.getAttNumero().setString(dpsRow.getPredial());
                    
                    concepto.setEltOpcConceptoCuentaPredial(cuentaPredial);
                }

                comprobante.getEltConceptos().getEltConceptos().add(concepto);
            }
        }
        
        double totalTrasladados = 0;
        double totalRetenciones = 0;
        cfd.ver33.DElementImpuestosTraslados impuestosTraslados = null;
        cfd.ver33.DElementImpuestosRetenciones impuestosRetenciones = null;
        
        if (!mapTaxIva.isEmpty()) {
            impuestosTraslados = new cfd.ver33.DElementImpuestosTraslados();
            
            for (Double taxRate : mapTaxIva.keySet()) {
                cfd.ver33.DElementImpuestoTraslado impuestoTraslado = new cfd.ver33.DElementImpuestoTraslado();
                impuestoTraslado.getAttImpuesto().setString(DCfdi40Catalogs.IMP_IVA);
                impuestoTraslado.getAttTipoFactor().setString(DCfdi40Catalogs.FAC_TP_TASA);
                impuestoTraslado.getAttTasaOCuota().setDouble(DLibUtils.round(taxRate, decsTax));
                impuestoTraslado.getAttImporte().setDouble(mapTaxIva.get(taxRate));
                impuestosTraslados.getEltImpuestoTrasladados().add(impuestoTraslado);
                
                totalTrasladados = DLibUtils.round(totalTrasladados + mapTaxIva.get(taxRate), decs);
            }
        }

        if (!mapRetIva.isEmpty() || !mapRetIsr.isEmpty()) {
            impuestosRetenciones = new cfd.ver33.DElementImpuestosRetenciones();
            
            for (String retCode : mapRetIva.keySet()) {
                cfd.ver33.DElementImpuestoRetencion impuestoRetencion = new cfd.ver33.DElementImpuestoRetencion();
                impuestoRetencion.getAttImpuesto().setString(DCfdi40Catalogs.IMP_IVA);
                impuestoRetencion.getAttImporte().setDouble(mapRetIva.get(retCode));
                impuestosRetenciones.getEltImpuestoRetenciones().add(impuestoRetencion);
                
                totalRetenciones = DLibUtils.round(totalRetenciones + mapRetIva.get(retCode), decs);
            }
            
            for (String retCode : mapRetIsr.keySet()) {
                cfd.ver33.DElementImpuestoRetencion impuestoRetencion = new cfd.ver33.DElementImpuestoRetencion();
                impuestoRetencion.getAttImpuesto().setString(DCfdi40Catalogs.IMP_ISR);
                impuestoRetencion.getAttImporte().setDouble(mapRetIsr.get(retCode));
                impuestosRetenciones.getEltImpuestoRetenciones().add(impuestoRetencion);
                
                totalRetenciones = DLibUtils.round(totalRetenciones + mapRetIsr.get(retCode), decs);
            }
        }
        
        if (impuestosTraslados != null || impuestosRetenciones != null) {
            cfd.ver33.DElementImpuestos impuestos = new cfd.ver33.DElementImpuestos(comprobante);

            if (impuestosTraslados != null) {
                impuestos.getAttTotalImpuestosTraslados().setDouble(totalTrasladados);
                impuestos.setEltOpcImpuestosTrasladados(impuestosTraslados);
            }
            
            if (impuestosRetenciones != null) {
                impuestos.getAttTotalImpuestosRetenidos().setDouble(totalRetenciones);
                impuestos.setEltOpcImpuestosRetenciones(impuestosRetenciones);
            }

            comprobante.setEltOpcImpuestos(impuestos);
        }

        /*
        (Signed CFDI will be only gotten fetching it from certification provider, 2017-12-18, sflores)
        
        if (timbreFiscal_n != null) {
            // Create element Complemento:

            cfd.ver33.DElementComplemento complemento = new cfd.ver33.DElementComplemento();
            cfd.ver33.DElementTimbreFiscalDigital timbreFiscalDigital = new cfd.ver33.DElementTimbreFiscalDigital();

            timbreFiscalDigital.getAttVersion().setString(timbreFiscal_n.getVersion());
            timbreFiscalDigital.getAttUUID().setString(timbreFiscal_n.getUuid());
            timbreFiscalDigital.getAttFechaTimbrado().setString(timbreFiscal_n.getFechaTimbrado());
            timbreFiscalDigital.getAttRfcProvCertif().setString(timbreFiscal_n.getRfcProvCertif());
            timbreFiscalDigital.getAttLeyenda().setString(timbreFiscal_n.getLeyenda());
            timbreFiscalDigital.getAttSelloCFD().setString(timbreFiscal_n.getSelloCfd());
            timbreFiscalDigital.getAttNoCertificadoSAT().setString(timbreFiscal_n.getNoCertificadoSat());
            timbreFiscalDigital.getAttSelloSAT().setString(timbreFiscal_n.getSelloSat());

            complemento.getElements().add(timbreFiscalDigital);

            comprobante.setEltOpcComplemento(complemento);
        }
        */

        return comprobante;
    }
    
    public static cfd.ver40.DElementComprobante createCfdi40FromDps(final DGuiSession session, final DDbDps dps, final DGuiEdsSignature signature) throws Exception {
        int decs = DLibUtils.getDecimalFormatAmount().getMaximumFractionDigits();
        int decsTax = 6;
        Date tDate = null;
        Date tUpdateTs = dps.getTsUserUpdate() != null ? dps.getTsUserUpdate() : new Date();
        int[] anDateDps = DLibTimeUtils.digestDate(dps.getDate());
        int[] anDateEdit = DLibTimeUtils.digestDate(tUpdateTs);
        GregorianCalendar oGregorianCalendar = null;
        DDbConfigCompany configCompany = (DDbConfigCompany) session.getConfigCompany();
        DDbConfigBranch configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, dps.getCompanyBranchKey());
        DDbBizPartner bprEmisor = null;
        DDbBranch braEmisor = null;
        DDbBizPartner bprReceptor = null;
        DDbBizPartner bprReceptorName = null;
        String receptorName = null;
        
        // Check company branch emission configuration:

        // Emisor:

        if (configBranch.getFkBizPartnerDpsSignatureId_n() == DLibConsts.UNDEFINED) {
            // Document's emisor:
            
            bprEmisor = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, dps.getCompanyKey());
            braEmisor = (DDbBranch) session.readRegistry(DModConsts.BU_BRA, dps.getCompanyBranchKey());
        }
        else {
            // Company branch's emisor:
            
            bprEmisor = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, new int[] { configBranch.getFkBizPartnerDpsSignatureId_n() });
            braEmisor = (DDbBranch) session.readRegistry(DModConsts.BU_BRA, new int[] { configBranch.getFkBizPartnerDpsSignatureId_n(), DUtilConsts.BPR_BRA_ID });
        }

        // Receptor:
        
        boolean isEmmissionTypeAsPublic = false;

        switch (dps.getFkEmissionTypeId()) {
            case DModSysConsts.TS_EMI_TP_BPR:
                bprReceptor = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, dps.getBizPartnerKey());
                receptorName = bprReceptor.getNameFiscal();
                break;
                
            case DModSysConsts.TS_EMI_TP_PUB_NAM:
            case DModSysConsts.TS_EMI_TP_PUB:
                bprReceptor = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, new int[] { configBranch.getFkPublicBizPartnerSaleId() });
                isEmmissionTypeAsPublic = true;
                
                if (dps.getFkEmissionTypeId() == DModSysConsts.TS_EMI_TP_PUB_NAM) {
                    bprReceptorName = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, dps.getBizPartnerKey());
                    receptorName = bprReceptorName.getNameFiscal();
                }
                else {
                    receptorName = DTrnEmissionConsts.PUBLIC_FAKE;
                }
                break;
                
            default:
                // nothing
        }

        if (DLibUtils.compareKeys(anDateDps, anDateEdit)) {
            tDate = tUpdateTs;
        }
        else {
            oGregorianCalendar = new GregorianCalendar();
            oGregorianCalendar.setTime(tUpdateTs);
            oGregorianCalendar.set(GregorianCalendar.YEAR, anDateDps[0]);
            oGregorianCalendar.set(GregorianCalendar.MONTH, anDateDps[1] - 1); // January is month 0
            oGregorianCalendar.set(GregorianCalendar.DATE, anDateDps[2]);
            tDate = oGregorianCalendar.getTime();
        }
        
        // Create XML's main element 'Comprobante':

        cfd.ver40.DElementComprobante comprobante = new cfd.ver40.DElementComprobante(); // include Addenda1 only if explicitly defined and CFD is already signed

        comprobante.getAttSerie().setString(dps.getSeries());
        comprobante.getAttFolio().setString("" + dps.getNumber());
        comprobante.getAttFecha().setDatetime(tDate);
        
        comprobante.getAttSello().setString("");
        comprobante.getAttNoCertificado().setString(signature.getCertificateNumber());
        comprobante.getAttCertificado().setString(signature.getCertificateBase64());

        comprobante.getAttFormaPago().setString((String) session.readField(DModConsts.FS_MOP_TP, new int[] { dps.getFkModeOfPaymentTypeId() }, DDbRegistry.FIELD_CODE));
        if (!dps.getXtaDfrMate().isGlobal()) {
            comprobante.getAttCondicionesDePago().setString(dps.getXtaDfrMate().getPaymentTerms());
        }
        
        comprobante.getAttSubTotal().setDouble(dps.getSubtotalProvCy_r());

        if (dps.getDiscountDocCy_r() > 0) {
            comprobante.getAttDescuento().setDouble(dps.getDiscountDocCy_r());
        }

        comprobante.getAttMoneda().setString(session.getSessionCustom().getCurrencyCode(dps.getCurrencyKey()));
        if (!session.getSessionCustom().isLocalCurrency(dps.getCurrencyKey())) {
            comprobante.getAttTipoCambio().setDouble(dps.getExchangeRate());
        }
        
        comprobante.getAttTotal().setDouble(dps.getTotalCy_r());

        comprobante.getAttTipoDeComprobante().setString(dps.isDpsDocument() ? DCfdi40Catalogs.CFD_TP_I : DCfdi40Catalogs.CFD_TP_E);
        comprobante.getAttMetodoPago().setString(dps.getXtaDfrMate().getMethodOfPayment());
        
        comprobante.getAttLugarExpedicion().setString(bprEmisor.getActualAddressFiscal());
        
        comprobante.getAttConfirmacion().setString(dps.getXtaDfrMate().getConfirmation());
        comprobante.getAttExportacion().setString(DCfdi40Catalogs.ClaveExportacionNoAplica); // fixed value, exportations not supported yet!
        
        if (dps.getXtaDfrMate().isGlobal()) {
            cfd.ver40.DElementInformacionGlobal informacionGlobal = new cfd.ver40.DElementInformacionGlobal();
            informacionGlobal.getAttPeriodicidad().setString(dps.getXtaDfrMate().getGlobalPeriodicity());
            informacionGlobal.getAttMeses().setString(dps.getXtaDfrMate().getGlobalMonths());
            informacionGlobal.getAttAño().setInteger(dps.getXtaDfrMate().getGlobalYear());
            comprobante.setEltOpcInformacionGlobal(informacionGlobal);
        }
        
        // element 'CfdiRelacionados':
        if (dps.getXtaDfrMate().getRelations() != null && !dps.getXtaDfrMate().getRelations().getRelatedCfds().isEmpty()) {
            ArrayList<cfd.ver40.DElementCfdiRelacionados> cfdiRelacionadoses = new ArrayList<>();
            for (DDfrMateRelations.RelatedCfd relatedCfd : dps.getXtaDfrMate().getRelations().getRelatedCfds()) {
                cfd.ver40.DElementCfdiRelacionados cfdiRelacionados = new cfd.ver40.DElementCfdiRelacionados();
                cfdiRelacionados.getAttTipoRelacion().setString(relatedCfd.RelationCode);

                for (String uuid : relatedCfd.Uuids) {
                    cfd.ver40.DElementCfdiRelacionado cfdiRelacionado = new cfd.ver40.DElementCfdiRelacionado();
                    cfdiRelacionado.getAttUuid().setString(uuid);
                    cfdiRelacionados.getEltCfdiRelacionados().add(cfdiRelacionado);
                }
                
                cfdiRelacionadoses.add(cfdiRelacionados);
            }
            
            comprobante.setEltOpcCfdiRelacionados(cfdiRelacionadoses);
        }

        // element 'Emisor':
        comprobante.getEltEmisor().getAttRfc().setString(bprEmisor.getFiscalId());
        comprobante.getEltEmisor().getAttNombre().setString(bprEmisor.getNameFiscal());
        comprobante.getEltEmisor().getAttRegimenFiscal().setString(dps.getXtaDfrMate().getIssuerTaxRegime());

        // element 'Receptor':
        String receptorRfc = bprReceptor.getFiscalId();
        boolean isReceptorPublic = DLibUtils.belongsTo(receptorRfc, new String[] { DCfdConsts.RFC_GEN_NAC, DCfdConsts.RFC_GEN_INT });
        
        if (isEmmissionTypeAsPublic && !isReceptorPublic) {
            receptorRfc = DCfdConsts.RFC_GEN_NAC;
            isReceptorPublic = true;
        }
        
        comprobante.getEltReceptor().getAttRfc().setString(receptorRfc);
        comprobante.getEltReceptor().getAttNombre().setString(receptorName);
        comprobante.getEltReceptor().getAttRegimenFiscalReceptor().setString(isReceptorPublic ? DCfdi40Catalogs.ClaveRégimenFiscalSinObligacionesFiscales : dps.getXtaDfrMate().getReceiverTaxRegime());
        comprobante.getEltReceptor().getAttDomicilioFiscalReceptor().setString(isReceptorPublic ? bprEmisor.getActualAddressFiscal(): dps.getXtaDfrMate().getReceiverFiscalAddress());
        //comprobante.getEltReceptor().getAttResidenciaFiscal().setString(""); // not supported yet!
        //comprobante.getEltReceptor().getAttNumRegIdTrib().setString(""); // not supported yet!
        comprobante.getEltReceptor().getAttUsoCFDI().setString(isReceptorPublic ? DCfdi40Catalogs.ClaveUsoCfdiSinEfectosFiscales : dps.getXtaDfrMate().getCfdUsage());

        // element 'Conceptos':
        
        HashMap<Double, Double> mapTaxIva = new HashMap<>(); //tax rate, tax amount
        HashMap<Double, Double> mapBaseIva = new HashMap<>(); //tax rate, taxable amount
        HashMap<String, Double> mapRetIva = new HashMap<>(); //retention code, retained amount
        HashMap<String, Double> mapRetIsr = new HashMap<>(); //retention code, retained amount
        
        for (DDbDpsRow dpsRow : dps.getChildRows()) {
            if (!dpsRow.isDeleted()) {
                // "Concepto" node:

                cfd.ver40.DElementConcepto concepto = new cfd.ver40.DElementConcepto();
                
                String notes = "";
                for (DDbDpsRowNote note : dpsRow.getChildRowNotes()) {
                    if (note.isDfr()) {
                        notes += (notes.isEmpty() ? "" : " ") + note.getText();
                    }
                }

                concepto.getAttClaveProdServ().setString(dpsRow.getDfrItemKey());
                concepto.getAttNoIdentificacion().setString(dpsRow.getCode());
                concepto.getAttCantidad().setDouble(dpsRow.getQuantity() == 0 ? 1 : dpsRow.getQuantity());
                concepto.getAttCantidad().setDecimals(configCompany.getDecimalsQuantity());
                concepto.getAttClaveUnidad().setString(dpsRow.getDfrUnitKey());
                if (!dps.getXtaDfrMate().isGlobal()) {
                    concepto.getAttUnidad().setString(dpsRow.getDbUnitCode());
                    concepto.getAttDescripcion().setString(dpsRow.getName() + (notes.isEmpty() ? "" : " " + notes));
                }
                else {
                    concepto.getAttDescripcion().setString(DCfdi40Catalogs.ConceptoFacturaGlobal);
                }
                concepto.getAttValorUnitario().setDecimals(configCompany.getDecimalsPriceUnitary());
                concepto.getAttValorUnitario().setDouble(dpsRow.getPriceUnitaryCy());
                concepto.getAttImporte().setDouble(dpsRow.getSubtotalProvCy_r());
                concepto.getAttDescuento().setDouble(dpsRow.getDiscountDocCy());
                concepto.getAttObjetoImp().setString(DCfdi40Catalogs.ClaveObjetoImpNo);

                // "Impuestos" node:
                
                // "ImpuestosTrasladados" node:
                
                cfd.ver40.DElementConceptoImpuestosTraslados impuestosTraslados = null;
                
                // go through the 3 possible taxes on each document row:
                
                for (int index = 1; index <= DDbDpsRow.TAXES; index++) {
                    int taxId = DLibConsts.UNDEFINED;
                    double taxRate = 0;
                    double taxAmount = 0;
                    double baseAmount = dpsRow.getSubtotalCy_r();
                    String taxCode = "";
                    HashMap<Double, Double> mapTax = null; //tax rate, tax amount
                    HashMap<Double, Double> mapBase = null; //tax rate, taxable amount
                    
                    switch (index) {
                        case 1:
                            taxId = dpsRow.getFkTaxCharged1Id();
                            taxRate = dpsRow.getTaxChargedRate1();
                            taxAmount = dpsRow.getTaxCharged1Cy();
                            break;
                        case 2:
                            taxId = dpsRow.getFkTaxCharged2Id();
                            taxRate = dpsRow.getTaxChargedRate2();
                            taxAmount = dpsRow.getTaxCharged2Cy();
                            break;
                        case 3:
                            taxId = dpsRow.getFkTaxCharged3Id();
                            taxRate = dpsRow.getTaxChargedRate3();
                            taxAmount = dpsRow.getTaxCharged3Cy();
                            break;
                        default:
                            // nothing
                    }
                    
                    if (taxId != DLibConsts.UNDEFINED) {
                        switch (taxId) {
                            case DFinConsts.TAX_NA:
                                break;
                            case DFinConsts.TAX_IVA:
                                taxCode = DCfdi40Catalogs.IMP_IVA;
                                mapTax = mapTaxIva;
                                mapBase = mapBaseIva;
                                break;
                            case DFinConsts.TAX_ISR:
                                break;
                            default:
                                throw new Exception (DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                        }
                        
                        if (mapTax != null) {
                            Double taxAcum = mapTax.get(taxRate);

                            if (taxAcum == null) {
                                mapTax.put(taxRate, taxAmount);
                            }
                            else {
                                mapTax.put(taxRate, DLibUtils.round(taxAcum + taxAmount, decs));
                            }
                            
                            Double baseAcum = mapBase.get(taxRate);

                            if (baseAcum == null) {
                                mapBase.put(taxRate, baseAmount);
                            }
                            else {
                                mapBase.put(taxRate, DLibUtils.round(baseAcum + baseAmount, decs));
                            }
                            
                            cfd.ver40.DElementConceptoImpuestoTraslado impuestoTraslado = new cfd.ver40.DElementConceptoImpuestoTraslado();
                            impuestoTraslado.getAttBase().setDouble(baseAmount);
                            impuestoTraslado.getAttImpuesto().setString(taxCode);
                            impuestoTraslado.getAttTipoFactor().setString(DCfdi40Catalogs.FAC_TP_TASA);
                            impuestoTraslado.getAttTasaOCuota().setDouble(DLibUtils.round(taxRate, decsTax));
                            impuestoTraslado.getAttImporte().setDouble(DLibUtils.round(taxAmount, decs));
                            
                            if (impuestosTraslados == null) {
                                impuestosTraslados = new cfd.ver40.DElementConceptoImpuestosTraslados();
                            }
                            impuestosTraslados.getEltImpuestoTrasladados().add(impuestoTraslado);
                        }
                    }
                }
                
                // "ImpuestosRetenidos" node:
                
                cfd.ver40.DElementConceptoImpuestosRetenciones impuestosRetenciones = null;
                
                //go through the 3 possible taxes on each document row:
                
                for (int index = 1; index <= DDbDpsRow.TAXES; index++) {
                    int retId = DLibConsts.UNDEFINED;
                    double retRate = 0;
                    double retAmount = 0;
                    String retCode = "";
                    HashMap<String, Double> mapRet = null; //retention code, retained amount
                    
                    switch (index) {
                        case 1:
                            retId = dpsRow.getFkTaxRetained1Id();
                            retRate = dpsRow.getTaxRetainedRate1();
                            retAmount = dpsRow.getTaxRetained1Cy();
                            break;
                        case 2:
                            retId = dpsRow.getFkTaxRetained2Id();
                            retRate = dpsRow.getTaxRetainedRate2();
                            retAmount = dpsRow.getTaxRetained2Cy();
                            break;
                        case 3:
                            retId = dpsRow.getFkTaxRetained3Id();
                            retRate = dpsRow.getTaxRetainedRate3();
                            retAmount = dpsRow.getTaxRetained3Cy();
                            break;
                        default:
                            // nothing
                    }
                    
                    if (retId != DLibConsts.UNDEFINED) {
                        switch (retId) {
                            case DFinConsts.TAX_NA:
                                break;
                            case DFinConsts.TAX_IVA:
                                retCode = DCfdi40Catalogs.IMP_IVA;
                                mapRet = mapRetIva;
                                break;
                            case DFinConsts.TAX_ISR:
                                retCode = DCfdi40Catalogs.IMP_ISR;
                                mapRet = mapRetIsr;
                                break;
                            default:
                                throw new Exception (DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                        }
                        
                        if (mapRet != null) {
                            Double retAcum = mapRet.get(retCode);
                            
                            if (retAcum == null) {
                                mapRet.put(retCode, retAmount);
                            }
                            else {
                                mapRet.put(retCode, DLibUtils.round(retAcum + retAmount, decs));
                            }
                            
                            cfd.ver40.DElementConceptoImpuestoRetencion impuestoRetencion = new cfd.ver40.DElementConceptoImpuestoRetencion();
                            impuestoRetencion.getAttBase().setDouble(dpsRow.getSubtotalCy_r());
                            impuestoRetencion.getAttImpuesto().setString(retCode);
                            impuestoRetencion.getAttTipoFactor().setString(DCfdi40Catalogs.FAC_TP_TASA);
                            impuestoRetencion.getAttTasaOCuota().setDouble(DLibUtils.round(retRate, decsTax));
                            impuestoRetencion.getAttImporte().setDouble(DLibUtils.round(retAmount, decs));
                            
                            if (impuestosRetenciones == null) {
                                impuestosRetenciones = new cfd.ver40.DElementConceptoImpuestosRetenciones();
                            }
                            impuestosRetenciones.getEltImpuestoRetenciones().add(impuestoRetencion);
                        }
                    }
                }
                
                if (impuestosTraslados != null || impuestosRetenciones != null) {
                    cfd.ver40.DElementConceptoImpuestos impuestos = new cfd.ver40.DElementConceptoImpuestos();
                    
                    if (impuestosTraslados != null) {
                        impuestos.setEltOpcImpuestosTrasladados(impuestosTraslados);
                    }
                    
                    if (impuestosRetenciones != null) {
                        impuestos.setEltOpcImpuestosRetenciones(impuestosRetenciones);
                    }
                    
                    concepto.setEltOpcConceptoImpuestos(impuestos);
                    concepto.getAttObjetoImp().setString(DCfdi40Catalogs.ClaveObjetoImpSí);
                }

                // "InformacionAduanera" child nodes:

                ArrayList<DTrnImportDeclaration> aImportDeclarations = DTrnDocRowUtils.getDpsRowImportDeclarations(session, dpsRow.getPrimaryKey());
                if (!aImportDeclarations.isEmpty()) {
                    
                    for (DTrnImportDeclaration importDeclaration : aImportDeclarations) {
                        cfd.ver40.DElementConceptoInformacionAduanera informacionAduanera = new cfd.ver40.DElementConceptoInformacionAduanera();

                        informacionAduanera.getAttNumeroPedimento().setString(importDeclaration.getNumberFormatted());

                        concepto.getEltOpcConceptoInformacionAduaneras().add(informacionAduanera);
                    }
                }

                // "CuentaPredial" child nodes:
                
                if (!dpsRow.getPredial().isEmpty()) {
                    String[] prediales = dpsRow.getPredial().replace(" ", "").split(",");
                    ArrayList<cfd.ver40.DElementConceptoCuentaPredial> cuentaPredials = new ArrayList<>();
                    
                    for (String predial : prediales) {
                        if (!predial.isEmpty()) {
                            cfd.ver40.DElementConceptoCuentaPredial cuentaPredial = new cfd.ver40.DElementConceptoCuentaPredial();
                            cuentaPredial.getAttNumero().setString(predial);
                            cuentaPredials.add(cuentaPredial);
                        }
                    }
                    
                    concepto.setEltOpcConceptoCuentaPredial(cuentaPredials);
                }

                comprobante.getEltConceptos().getEltConceptos().add(concepto);
            }
        }
        
        double totalTrasladados = 0;
        double totalRetenciones = 0;
        cfd.ver40.DElementImpuestosTraslados impuestosTraslados = null;
        cfd.ver40.DElementImpuestosRetenciones impuestosRetenciones = null;
        
        if (!mapTaxIva.isEmpty()) {
            impuestosTraslados = new cfd.ver40.DElementImpuestosTraslados();
            
            for (Double taxRate : mapTaxIva.keySet()) {
                cfd.ver40.DElementImpuestoTraslado impuestoTraslado = new cfd.ver40.DElementImpuestoTraslado();
                impuestoTraslado.getAttBase().setDouble(mapBaseIva.get(taxRate));
                impuestoTraslado.getAttImpuesto().setString(DCfdi40Catalogs.IMP_IVA);
                impuestoTraslado.getAttTipoFactor().setString(DCfdi40Catalogs.FAC_TP_TASA);
                impuestoTraslado.getAttTasaOCuota().setDouble(DLibUtils.round(taxRate, decsTax));
                impuestoTraslado.getAttImporte().setDouble(mapTaxIva.get(taxRate));
                impuestosTraslados.getEltImpuestoTrasladados().add(impuestoTraslado);
                
                totalTrasladados = DLibUtils.round(totalTrasladados + mapTaxIva.get(taxRate), decs);
            }
        }

        if (!mapRetIva.isEmpty() || !mapRetIsr.isEmpty()) {
            impuestosRetenciones = new cfd.ver40.DElementImpuestosRetenciones();
            
            for (String retCode : mapRetIva.keySet()) {
                cfd.ver40.DElementImpuestoRetencion impuestoRetencion = new cfd.ver40.DElementImpuestoRetencion();
                impuestoRetencion.getAttImpuesto().setString(DCfdi40Catalogs.IMP_IVA);
                impuestoRetencion.getAttImporte().setDouble(mapRetIva.get(retCode));
                impuestosRetenciones.getEltImpuestoRetenciones().add(impuestoRetencion);
                
                totalRetenciones = DLibUtils.round(totalRetenciones + mapRetIva.get(retCode), decs);
            }
            
            for (String retCode : mapRetIsr.keySet()) {
                cfd.ver40.DElementImpuestoRetencion impuestoRetencion = new cfd.ver40.DElementImpuestoRetencion();
                impuestoRetencion.getAttImpuesto().setString(DCfdi40Catalogs.IMP_ISR);
                impuestoRetencion.getAttImporte().setDouble(mapRetIsr.get(retCode));
                impuestosRetenciones.getEltImpuestoRetenciones().add(impuestoRetencion);
                
                totalRetenciones = DLibUtils.round(totalRetenciones + mapRetIsr.get(retCode), decs);
            }
        }
        
        if (impuestosTraslados != null || impuestosRetenciones != null) {
            cfd.ver40.DElementImpuestos impuestos = new cfd.ver40.DElementImpuestos(comprobante);

            if (impuestosTraslados != null) {
                impuestos.getAttTotalImpuestosTraslados().setDouble(totalTrasladados);
                impuestos.setEltOpcImpuestosTrasladados(impuestosTraslados);
            }
            
            if (impuestosRetenciones != null) {
                impuestos.getAttTotalImpuestosRetenidos().setDouble(totalRetenciones);
                impuestos.setEltOpcImpuestosRetenciones(impuestosRetenciones);
            }

            comprobante.setEltOpcImpuestos(impuestos);
        }

        /*
        (Signed CFDI will be only gotten fetching it from certification provider, 2017-12-18, sflores)
        
        if (timbreFiscal_n != null) {
            // Create element Complemento:

            cfd.ver40.DElementComplemento complemento = new cfd.ver40.DElementComplemento();
            cfd.ver40.DElementTimbreFiscalDigital timbreFiscalDigital = new cfd.ver40.DElementTimbreFiscalDigital();

            timbreFiscalDigital.getAttVersion().setString(timbreFiscal_n.getVersion());
            timbreFiscalDigital.getAttUUID().setString(timbreFiscal_n.getUuid());
            timbreFiscalDigital.getAttFechaTimbrado().setString(timbreFiscal_n.getFechaTimbrado());
            timbreFiscalDigital.getAttRfcProvCertif().setString(timbreFiscal_n.getRfcProvCertif());
            timbreFiscalDigital.getAttLeyenda().setString(timbreFiscal_n.getLeyenda());
            timbreFiscalDigital.getAttSelloCFD().setString(timbreFiscal_n.getSelloCfd());
            timbreFiscalDigital.getAttNoCertificadoSAT().setString(timbreFiscal_n.getNoCertificadoSat());
            timbreFiscalDigital.getAttSelloSAT().setString(timbreFiscal_n.getSelloSat());

            complemento.getElements().add(timbreFiscalDigital);

            comprobante.setEltOpcComplemento(complemento);
        }
        */

        return comprobante;
    }
    
    @Deprecated
    private static cfd.ver3.ccp20.DElementCartaPorte createCcp20FromBol(final DGuiSession session, final DDbBol bol, final DDbBizPartner bprEmisor) {
        cfd.ver3.ccp20.DElementCartaPorte cartaPorte = new cfd.ver3.ccp20.DElementCartaPorte();
        
        //cartaPorte.getAttVersion().setString(...); // attribute has default value
        
        if (bol.getIntlTransport().equals(DCfdi40Catalogs.TextoNo)) {
            cartaPorte.getAttTransInternac().setString(DCfdi40Catalogs.TextoNo);
        }
        else {
            cartaPorte.getAttTransInternac().setString(DCfdi40Catalogs.TextoSí);
            cartaPorte.getAttEntradaSalidaMerc().setString(bol.getIntlTransportDirection());
            cartaPorte.getAttViaEntradaSalida().setString((String) session.readField(DModConsts.LS_TPT_TP, new int[] { bol.getFkIntlWayTransportTypeId() }, DDbRegistry.FIELD_CODE));
            cartaPorte.getAttPaisOrigenDestino().setString((String) session.readField(DModConsts.CS_CTY, new int[] { bol.getFkIntlTransportCountryId()}, DDbRegistry.FIELD_CODE));
        }
        
        cartaPorte.getAttTotalDistRec().setDouble(bol.getDistanceKm());
        
        // ubicaciones:
        
        for (DDbBolLocation bolLocation : bol.getChildLocations()) {
            cfd.ver3.ccp20.DElementUbicacion ubicacion = new cfd.ver3.ccp20.DElementUbicacion();
            
            ubicacion.getAttTipoUbicacion().setString((String) session.readField(DModConsts.LS_LOC_TP, new int[] { bolLocation.getFkLocationTypeId() }, DDbRegistry.FIELD_NAME));
            ubicacion.getAttIDUbicacion().setString(bolLocation.getLocationId());
            ubicacion.getAttRFCRemitenteDestinatario().setString(bprEmisor.getFiscalId());
            ubicacion.getAttNombreRemitenteDestinatario().setString(bprEmisor.getName());
            
            ubicacion.getAttNumRegIdTrib().setString("");
            ubicacion.getAttResidenciaFiscal().setString("");
            
            ubicacion.getAttFechaHoraSalidaLlegada().setDatetime(bolLocation.getArrivalDepartureDatetime());
            
            ubicacion.getAttDistanciaRecorrida().setDouble(bolLocation.getDistanceKm());
            
            ubicacion.getEltDomicilio().getAttCalle().setString(bolLocation.getAddressStreet());
            ubicacion.getEltDomicilio().getAttNumeroExterior().setString(bolLocation.getAddressNumberExt());
            ubicacion.getEltDomicilio().getAttNumeroInterior().setString(bolLocation.getAddressNumberInt());
            ubicacion.getEltDomicilio().getAttReferencia().setString(bolLocation.getAddressReference());
            ubicacion.getEltDomicilio().getAttCodigoPostal().setString(bolLocation.getAddressZipCode());
            
            String countryCode = session.getSessionCustom().getCountryCode(new int[] { bolLocation.getFkAddressCountryId() });
            ubicacion.getEltDomicilio().getAttPais().setString(countryCode);
            
            if (DFormBolUtils.applyStateCatalog(countryCode)) {
                ubicacion.getEltDomicilio().getAttEstado().setString(bolLocation.getAddressStateCode());
            }
            else {
                ubicacion.getEltDomicilio().getAttEstado().setString(bolLocation.getAddressStateName());
            }
            
            if (DFormBolUtils.applyAddressCatalogs(countryCode)) {
                ubicacion.getEltDomicilio().getAttColonia().setString(bolLocation.getAddressDistrictCode());
                ubicacion.getEltDomicilio().getAttLocalidad().setString(bolLocation.getAddressLocalityCode());
                ubicacion.getEltDomicilio().getAttMunicipio().setString(bolLocation.getAddressCountyCode());
            }
            else {
                ubicacion.getEltDomicilio().getAttColonia().setString(bolLocation.getAddressDistrictName());
                ubicacion.getEltDomicilio().getAttLocalidad().setString(bolLocation.getAddressLocalityName());
                ubicacion.getEltDomicilio().getAttMunicipio().setString(bolLocation.getAddressCountyName());
            }
            
            cartaPorte.getEltUbicaciones().getEltUbicaciones().add(ubicacion);
        }
        
        // mercancías:
        
        DDbUnit unitWeight = (DDbUnit) session.readRegistry(DModConsts.IU_UNT, new int[] { bol.getFkMerchandiseWeightUnitId() });
        
        cartaPorte.getEltMercancias().getAttPesoBrutoTotal().setDouble(bol.getMerchandiseWeight());
        cartaPorte.getEltMercancias().getAttUnidadPeso().setString(unitWeight.getCfdUnitKey());
        cartaPorte.getEltMercancias().getAttNumTotalMercancias().setInteger(bol.getMerchandiseNumber());
        
        for (DDbBolMerchandise bolMerchandise : bol.getChildMerchandises()) {
            cfd.ver3.ccp20.DElementMercancia mercancia = new cfd.ver3.ccp20.DElementMercancia();
            
            mercancia.getAttBienesTransp().setString(bolMerchandise.getOwnItem().getActualCfdItemKey());
            mercancia.getAttClaveSTCC().setString(""); // XXX ???
            mercancia.getAttDescripcion().setString(bolMerchandise.getDescriptionItem());
            mercancia.getAttCantidad().setDouble(bolMerchandise.getQuantity());
            mercancia.getAttClaveUnidad().setString(bolMerchandise.getOwnUnit().getCfdUnitKey());
            mercancia.getAttUnidad().setString(bolMerchandise.getDescriptionUnit());
            mercancia.getAttDimensiones().setString(bolMerchandise.getDimensions());
            if (bolMerchandise.isHazardousMaterial()) {
                if (bolMerchandise.isHazardousMaterialNo()) {
                    mercancia.getAttMaterialPeligroso().setString(DCfdi40Catalogs.TextoNo);
                }
                else if (bolMerchandise.isHazardousMaterialYes()) {
                    mercancia.getAttMaterialPeligroso().setString(DCfdi40Catalogs.TextoSí);
                    mercancia.getAttCveMaterialPeligroso().setString(bolMerchandise.getHazardousMaterialCode());
                    mercancia.getAttEmbalaje().setString(bolMerchandise.getPackagingCode());
                    mercancia.getAttDescripEmbalaje().setString(bolMerchandise.getPackagingName());
                }
            }
            mercancia.getAttPesoEnKg().setDouble(bolMerchandise.getWeightKg());
            
            if (bolMerchandise.getFkCurrencyId() != DModSysConsts.CS_CUR_NA) {
                mercancia.getAttValorMercancia().setDouble(bolMerchandise.getValue());
                mercancia.getAttMoneda().setString(session.getSessionCustom().getCurrencyCode(new int[] { bolMerchandise.getFkCurrencyId() }));
            }
            
            mercancia.getAttFraccionArancelaria().setString(bolMerchandise.getTariff());
            mercancia.getAttUUIDComercioExt().setString("");
            
            for (DDbBolMerchandiseMove bolMerchandiseMove : bolMerchandise.getChildMoves()) {
                cfd.ver3.ccp20.DElementCantidadTransporta cantidadTransporta = new cfd.ver3.ccp20.DElementCantidadTransporta();
                
                cantidadTransporta.getAttCantidad().setDouble(bolMerchandiseMove.getQuantity());
                
                DDbBolLocation source = bol.getChildLocation(bolMerchandiseMove.getSourceLocationKey());
                cantidadTransporta.getAttIDOrigen().setString(source != null ? source.getLocationId() : "");
                
                DDbBolLocation destiny = bol.getChildLocation(bolMerchandiseMove.getDestinyLocationKey());
                cantidadTransporta.getAttIDDestino().setString(destiny != null ? destiny.getLocationId() : "");
                
                cantidadTransporta.getAttCvesTransporte().setString("");
                
                mercancia.getEltCantidadTransporta().add(cantidadTransporta);
            }
            
            if (!bolMerchandise.getImportRequest().isEmpty()) {
                cfd.ver3.ccp20.DElementPedimentos elementPedimentos = new cfd.ver3.ccp20.DElementPedimentos();
                elementPedimentos.getAttPedimento().setString(bolMerchandise.getImportRequest());
                
                ArrayList<cfd.ver3.ccp20.DElementPedimentos> elementPedimentoses = new ArrayList<>();
                elementPedimentoses.add(elementPedimentos);
                
                mercancia.setEltPedimentos(elementPedimentoses);
            }

            cartaPorte.getEltMercancias().getEltMercancias().add(mercancia);
        }
        
        DDbBolTruck bolTruck = bol.getChildTrucks().get(0);
        
        cartaPorte.getEltMercancias().getEltAutotransporte().getAttPermSCT().setString(bolTruck.getPermissionTypeCode());
        cartaPorte.getEltMercancias().getEltAutotransporte().getAttNumPermisoSCT().setString(bolTruck.getPermissionNumber());
        
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltIdentificacionVehicular().getAttConfigVehicular().setString(bolTruck.getTransportConfigCode());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltIdentificacionVehicular().getAttPlacaVM().setString(bolTruck.getPlate());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltIdentificacionVehicular().getAttAnioModeloVM().setInteger(bolTruck.getModel());
        
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttAseguraRespCivil().setString(bolTruck.getCivilInsurance());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttPolizaRespCivil().setString(bolTruck.getCivilPolicy());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttAseguraMedAmbiente().setString(bolTruck.getEnvironmentInsurance());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttPolizaMedAmbiente().setString(bolTruck.getEnvironmentPolicy());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttAseguraCarga().setString(bolTruck.getCargoInsurance());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttPolizaCarga().setString(bolTruck.getCargoPolicy());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttPrimaSeguro().setDouble(bolTruck.getPrime());

        if (!bolTruck.getChildTrailers().isEmpty()) {
            cfd.ver3.ccp20.DElementRemolques remolques = new cfd.ver3.ccp20.DElementRemolques();
            
            for (DDbBolTruckTrailer bolTruckTrailer : bolTruck.getChildTrailers()) {
                cfd.ver3.ccp20.DElementRemolque remolque = new cfd.ver3.ccp20.DElementRemolque();
                
                remolque.getAttSubTipoRem().setString(bolTruckTrailer.getTrailerSubtypeCode());
                remolque.getAttPlaca().setString(bolTruckTrailer.getPlate());
                
                remolques.getEltRemolques().add(remolque);
            }
            
            cartaPorte.getEltMercancias().getEltAutotransporte().setEltRemolques(remolques);
        }

        // figuras de transporte:
        
        for (DDbBolTransportFigure bolTptFigure : bol.getChildTransportFigures()) {
            cfd.ver3.ccp20.DElementTiposFigura tiposFigura = new cfd.ver3.ccp20.DElementTiposFigura();
            
            tiposFigura.getAttTipoFigura().setString((String) session.readField(DModConsts.LS_TPT_FIGURE_TP, new int[] { bolTptFigure.getFkTransportFigureTypeId() }, DDbRegistry.FIELD_CODE));
            tiposFigura.getAttRFCFigura().setString(bolTptFigure.getFiscalId());
            tiposFigura.getAttNumLicencia().setString(bolTptFigure.getDriverLicense());
            tiposFigura.getAttNombreFigura().setString(bolTptFigure.getName());
            
            if (bolTptFigure.getFkFigureCountryId() != DModSysConsts.CS_CTY_NA) {
                String countryCode = session.getSessionCustom().getCountryCode(new int[] { bolTptFigure.getFkFigureCountryId() });
                tiposFigura.getAttNumRegIdTribFigura().setString(bolTptFigure.getForeignId());
                tiposFigura.getAttResidenciaFiscalFigura().setString(countryCode);
            }
            
            cfd.ver3.ccp20.DElementDomicilio domicilio = new cfd.ver3.ccp20.DElementDomicilio();

            domicilio.getAttCalle().setString(bolTptFigure.getAddressStreet());
            domicilio.getAttNumeroExterior().setString(bolTptFigure.getAddressNumberExt());
            domicilio.getAttNumeroInterior().setString(bolTptFigure.getAddressNumberInt());
            domicilio.getAttReferencia().setString(bolTptFigure.getAddressReference());
            domicilio.getAttCodigoPostal().setString(bolTptFigure.getAddressZipCode());
            
            String countryCode = session.getSessionCustom().getCountryCode(new int[] { bolTptFigure.getFkAddressCountryId() });
            domicilio.getAttPais().setString(countryCode);
            
            if (DFormBolUtils.applyStateCatalog(countryCode)) {
                domicilio.getAttEstado().setString(bolTptFigure.getAddressStateCode());
            }
            else {
                domicilio.getAttEstado().setString(bolTptFigure.getAddressStateName());
            }
            
            if (DFormBolUtils.applyAddressCatalogs(countryCode)) {
                domicilio.getAttColonia().setString(bolTptFigure.getAddressDistrictCode());
                domicilio.getAttLocalidad().setString(bolTptFigure.getAddressLocalityCode());
                domicilio.getAttMunicipio().setString(bolTptFigure.getAddressCountyCode());
            }
            else {
                domicilio.getAttColonia().setString(bolTptFigure.getAddressDistrictName());
                domicilio.getAttLocalidad().setString(bolTptFigure.getAddressLocalityName());
                domicilio.getAttMunicipio().setString(bolTptFigure.getAddressCountyName());
            }
            
            tiposFigura.setEltDomicilio(domicilio);
            
            for (DDbBolTransportFigureTransportPart bolTptFigureTptPart : bolTptFigure.getChildTransportParts()) {
                cfd.ver3.ccp20.DElementPartesTransporte partesTransporte = new cfd.ver3.ccp20.DElementPartesTransporte();
                
                partesTransporte.getAttParteTransporte().setString((String) session.readField(DModConsts.LS_TPT_PART_TP, new int[] { bolTptFigureTptPart.getFkTransportPartTypeId() }, DDbRegistry.FIELD_CODE));
                
                tiposFigura.getEltPartesTransporte().add(partesTransporte);
            }
            
            cartaPorte.getEltFiguraTransporte().getEltTiposFigura().add(tiposFigura);
        }
        
        return cartaPorte;
    }
    
    @Deprecated
    private static cfd.ver4.ccp30.DElementCartaPorte createCcp30FromBol(final DGuiSession session, final DDbBol bol, final DDbBizPartner bprEmisor) {
        cfd.ver4.ccp30.DElementCartaPorte cartaPorte = new cfd.ver4.ccp30.DElementCartaPorte();
        
        //cartaPorte.getAttVersion().setString(...); // attribute has default value
        cartaPorte.getAttIdCCP().setString(bol.getBolUuid());
        
        if (bol.getIntlTransport().equals(DCfdi40Catalogs.TextoNo)) {
            cartaPorte.getAttTransInternac().setString(DCfdi40Catalogs.TextoNo);
        }
        else {
            cartaPorte.getAttTransInternac().setString(DCfdi40Catalogs.TextoSí);
            cartaPorte.getAttEntradaSalidaMerc().setString(bol.getIntlTransportDirection());
            cartaPorte.getAttViaEntradaSalida().setString((String) session.readField(DModConsts.LS_TPT_TP, new int[] { bol.getFkIntlWayTransportTypeId() }, DDbRegistry.FIELD_CODE));
            cartaPorte.getAttPaisOrigenDestino().setString((String) session.readField(DModConsts.CS_CTY, new int[] { bol.getFkIntlTransportCountryId()}, DDbRegistry.FIELD_CODE));
        }
        
        if (bol.isIsthmus()) {
            cartaPorte.getAttRegistroISTMO().setString(DCfdi40Catalogs.TextoSí);
            cartaPorte.getAttUbicacionPoloOrigen().setString(bol.getIsthmusOrigin());
            cartaPorte.getAttUbicacionPoloDestino().setString(bol.getIsthmusDestiny());
        }
        
        cartaPorte.getAttTotalDistRec().setDouble(bol.getDistanceKm());
        
        // ubicaciones:
        
        for (DDbBolLocation bolLocation : bol.getChildLocations()) {
            cfd.ver4.ccp30.DElementUbicacion ubicacion = new cfd.ver4.ccp30.DElementUbicacion();
            
            ubicacion.getAttTipoUbicacion().setString((String) session.readField(DModConsts.LS_LOC_TP, new int[] { bolLocation.getFkLocationTypeId() }, DDbRegistry.FIELD_NAME));
            ubicacion.getAttIDUbicacion().setString(bolLocation.getLocationId());
            ubicacion.getAttRFCRemitenteDestinatario().setString(bprEmisor.getFiscalId());
            ubicacion.getAttNombreRemitenteDestinatario().setString(bprEmisor.getName());
            
            ubicacion.getAttNumRegIdTrib().setString("");
            ubicacion.getAttResidenciaFiscal().setString("");
            
            ubicacion.getAttFechaHoraSalidaLlegada().setDatetime(bolLocation.getArrivalDepartureDatetime());
            
            ubicacion.getAttDistanciaRecorrida().setDouble(bolLocation.getDistanceKm());
            
            ubicacion.getEltDomicilio().getAttCalle().setString(bolLocation.getAddressStreet());
            ubicacion.getEltDomicilio().getAttNumeroExterior().setString(bolLocation.getAddressNumberExt());
            ubicacion.getEltDomicilio().getAttNumeroInterior().setString(bolLocation.getAddressNumberInt());
            ubicacion.getEltDomicilio().getAttReferencia().setString(bolLocation.getAddressReference());
            ubicacion.getEltDomicilio().getAttCodigoPostal().setString(bolLocation.getAddressZipCode());
            
            String countryCode = session.getSessionCustom().getCountryCode(new int[] { bolLocation.getFkAddressCountryId() });
            ubicacion.getEltDomicilio().getAttPais().setString(countryCode);
            
            if (DFormBolUtils.applyStateCatalog(countryCode)) {
                ubicacion.getEltDomicilio().getAttEstado().setString(bolLocation.getAddressStateCode());
            }
            else {
                ubicacion.getEltDomicilio().getAttEstado().setString(bolLocation.getAddressStateName());
            }
            
            if (DFormBolUtils.applyAddressCatalogs(countryCode)) {
                ubicacion.getEltDomicilio().getAttColonia().setString(bolLocation.getAddressDistrictCode());
                ubicacion.getEltDomicilio().getAttLocalidad().setString(bolLocation.getAddressLocalityCode());
                ubicacion.getEltDomicilio().getAttMunicipio().setString(bolLocation.getAddressCountyCode());
            }
            else {
                ubicacion.getEltDomicilio().getAttColonia().setString(bolLocation.getAddressDistrictName());
                ubicacion.getEltDomicilio().getAttLocalidad().setString(bolLocation.getAddressLocalityName());
                ubicacion.getEltDomicilio().getAttMunicipio().setString(bolLocation.getAddressCountyName());
            }
            
            cartaPorte.getEltUbicaciones().getEltUbicaciones().add(ubicacion);
        }
        
        // mercancías:
        
        DDbUnit unitWeight = (DDbUnit) session.readRegistry(DModConsts.IU_UNT, new int[] { bol.getFkMerchandiseWeightUnitId() });
        
        cartaPorte.getEltMercancias().getAttPesoBrutoTotal().setDouble(bol.getMerchandiseWeight());
        cartaPorte.getEltMercancias().getAttUnidadPeso().setString(unitWeight.getCfdUnitKey());
        cartaPorte.getEltMercancias().getAttNumTotalMercancias().setInteger(bol.getMerchandiseNumber());
        
        if (bol.isMerchandiseInverseLogistics()) {
            cartaPorte.getEltMercancias().getAttLogisticaInversaRecoleccionDevolucion().setString(DCfdi40Catalogs.TextoSí);
        }
        
        for (DDbBolMerchandise bolMerchandise : bol.getChildMerchandises()) {
            cfd.ver4.ccp30.DElementMercancia mercancia = new cfd.ver4.ccp30.DElementMercancia();
            
            mercancia.getAttBienesTransp().setString(bolMerchandise.getOwnItem().getActualCfdItemKey());
            mercancia.getAttClaveSTCC().setString(""); // XXX ???
            mercancia.getAttDescripcion().setString(bolMerchandise.getDescriptionItem());
            mercancia.getAttCantidad().setDouble(bolMerchandise.getQuantity());
            mercancia.getAttClaveUnidad().setString(bolMerchandise.getOwnUnit().getCfdUnitKey());
            mercancia.getAttUnidad().setString(bolMerchandise.getDescriptionUnit());
            mercancia.getAttDimensiones().setString(bolMerchandise.getDimensions());
            if (bolMerchandise.isHazardousMaterial()) {
                if (bolMerchandise.isHazardousMaterialNo()) {
                    mercancia.getAttMaterialPeligroso().setString(DCfdi40Catalogs.TextoNo);
                }
                else if (bolMerchandise.isHazardousMaterialYes()) {
                    mercancia.getAttMaterialPeligroso().setString(DCfdi40Catalogs.TextoSí);
                    mercancia.getAttCveMaterialPeligroso().setString(bolMerchandise.getHazardousMaterialCode());
                    mercancia.getAttEmbalaje().setString(bolMerchandise.getPackagingCode());
                    mercancia.getAttDescripEmbalaje().setString(bolMerchandise.getPackagingName());
                }
            }
            mercancia.getAttPesoEnKg().setDouble(bolMerchandise.getWeightKg());
            
            if (bolMerchandise.getFkCurrencyId() != DModSysConsts.CS_CUR_NA) {
                mercancia.getAttValorMercancia().setDouble(bolMerchandise.getValue());
                mercancia.getAttMoneda().setString(session.getSessionCustom().getCurrencyCode(new int[] { bolMerchandise.getFkCurrencyId() }));
            }
            
            mercancia.getAttFraccionArancelaria().setString(bolMerchandise.getTariff());
            mercancia.getAttUUIDComercioExt().setString("");
            
            for (DDbBolMerchandiseMove bolMerchandiseMove : bolMerchandise.getChildMoves()) {
                cfd.ver4.ccp30.DElementCantidadTransporta cantidadTransporta = new cfd.ver4.ccp30.DElementCantidadTransporta();
                
                cantidadTransporta.getAttCantidad().setDouble(bolMerchandiseMove.getQuantity());
                
                DDbBolLocation source = bol.getChildLocation(bolMerchandiseMove.getSourceLocationKey());
                cantidadTransporta.getAttIDOrigen().setString(source != null ? source.getLocationId() : "");
                
                DDbBolLocation destiny = bol.getChildLocation(bolMerchandiseMove.getDestinyLocationKey());
                cantidadTransporta.getAttIDDestino().setString(destiny != null ? destiny.getLocationId() : "");
                
                cantidadTransporta.getAttCvesTransporte().setString("");
                
                mercancia.getEltCantidadTransportas().add(cantidadTransporta);
            }
            
            if (!bolMerchandise.getImportRequest().isEmpty()) {
                cfd.ver4.ccp30.DElementDocumentacionAduanera documentacionAduanera = new cfd.ver4.ccp30.DElementDocumentacionAduanera();
                documentacionAduanera.getAttTipoDocumento().setString(""); // XXX not implemented yet!
                documentacionAduanera.getAttNumPedimento().setString(bolMerchandise.getImportRequest());
                documentacionAduanera.getAttIdentDocAduanero().setString("");
                documentacionAduanera.getAttRFCImpo().setString("");
                
                ArrayList<cfd.ver4.ccp30.DElementDocumentacionAduanera> documentacionAduaneras = new ArrayList<>();
                documentacionAduaneras.add(documentacionAduanera);
                
                mercancia.setEltDocumentacionAduaneras(documentacionAduaneras);
            }

            cartaPorte.getEltMercancias().getEltMercancias().add(mercancia);
        }
        
        DDbBolTruck bolTruck = bol.getChildTrucks().get(0);
        
        cartaPorte.getEltMercancias().getEltAutotransporte().getAttPermSCT().setString(bolTruck.getPermissionTypeCode());
        cartaPorte.getEltMercancias().getEltAutotransporte().getAttNumPermisoSCT().setString(bolTruck.getPermissionNumber());
        
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltIdentificacionVehicular().getAttConfigVehicular().setString(bolTruck.getTransportConfigCode());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltIdentificacionVehicular().getAttPesoBrutoVehicular().setDouble(bolTruck.getWeightGrossTon());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltIdentificacionVehicular().getAttPlacaVM().setString(bolTruck.getPlate());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltIdentificacionVehicular().getAttAnioModeloVM().setInteger(bolTruck.getModel());
        
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttAseguraRespCivil().setString(bolTruck.getCivilInsurance());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttPolizaRespCivil().setString(bolTruck.getCivilPolicy());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttAseguraMedAmbiente().setString(bolTruck.getEnvironmentInsurance());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttPolizaMedAmbiente().setString(bolTruck.getEnvironmentPolicy());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttAseguraCarga().setString(bolTruck.getCargoInsurance());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttPolizaCarga().setString(bolTruck.getCargoPolicy());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttPrimaSeguro().setDouble(bolTruck.getPrime());

        if (!bolTruck.getChildTrailers().isEmpty()) {
            cfd.ver4.ccp30.DElementRemolques remolques = new cfd.ver4.ccp30.DElementRemolques();
            
            for (DDbBolTruckTrailer bolTruckTrailer : bolTruck.getChildTrailers()) {
                cfd.ver4.ccp30.DElementRemolque remolque = new cfd.ver4.ccp30.DElementRemolque();
                
                remolque.getAttSubTipoRem().setString(bolTruckTrailer.getTrailerSubtypeCode());
                remolque.getAttPlaca().setString(bolTruckTrailer.getPlate());
                
                remolques.getEltRemolques().add(remolque);
            }
            
            cartaPorte.getEltMercancias().getEltAutotransporte().setEltRemolques(remolques);
        }

        // figuras de transporte:
        
        for (DDbBolTransportFigure bolTptFigure : bol.getChildTransportFigures()) {
            cfd.ver4.ccp30.DElementTiposFigura tiposFigura = new cfd.ver4.ccp30.DElementTiposFigura();
            
            tiposFigura.getAttTipoFigura().setString((String) session.readField(DModConsts.LS_TPT_FIGURE_TP, new int[] { bolTptFigure.getFkTransportFigureTypeId() }, DDbRegistry.FIELD_CODE));
            tiposFigura.getAttRFCFigura().setString(bolTptFigure.getFiscalId());
            tiposFigura.getAttNumLicencia().setString(bolTptFigure.getDriverLicense());
            tiposFigura.getAttNombreFigura().setString(bolTptFigure.getName());
            
            if (bolTptFigure.getFkFigureCountryId() != DModSysConsts.CS_CTY_NA) {
                String countryCode = session.getSessionCustom().getCountryCode(new int[] { bolTptFigure.getFkFigureCountryId() });
                tiposFigura.getAttNumRegIdTribFigura().setString(bolTptFigure.getForeignId());
                tiposFigura.getAttResidenciaFiscalFigura().setString(countryCode);
            }
            
            cfd.ver4.ccp30.DElementDomicilio domicilio = new cfd.ver4.ccp30.DElementDomicilio();

            domicilio.getAttCalle().setString(bolTptFigure.getAddressStreet());
            domicilio.getAttNumeroExterior().setString(bolTptFigure.getAddressNumberExt());
            domicilio.getAttNumeroInterior().setString(bolTptFigure.getAddressNumberInt());
            domicilio.getAttReferencia().setString(bolTptFigure.getAddressReference());
            domicilio.getAttCodigoPostal().setString(bolTptFigure.getAddressZipCode());
            
            String countryCode = session.getSessionCustom().getCountryCode(new int[] { bolTptFigure.getFkAddressCountryId() });
            domicilio.getAttPais().setString(countryCode);
            
            if (DFormBolUtils.applyStateCatalog(countryCode)) {
                domicilio.getAttEstado().setString(bolTptFigure.getAddressStateCode());
            }
            else {
                domicilio.getAttEstado().setString(bolTptFigure.getAddressStateName());
            }
            
            if (DFormBolUtils.applyAddressCatalogs(countryCode)) {
                domicilio.getAttColonia().setString(bolTptFigure.getAddressDistrictCode());
                domicilio.getAttLocalidad().setString(bolTptFigure.getAddressLocalityCode());
                domicilio.getAttMunicipio().setString(bolTptFigure.getAddressCountyCode());
            }
            else {
                domicilio.getAttColonia().setString(bolTptFigure.getAddressDistrictName());
                domicilio.getAttLocalidad().setString(bolTptFigure.getAddressLocalityName());
                domicilio.getAttMunicipio().setString(bolTptFigure.getAddressCountyName());
            }
            
            tiposFigura.setEltDomicilio(domicilio);
            
            for (DDbBolTransportFigureTransportPart bolTptFigureTptPart : bolTptFigure.getChildTransportParts()) {
                cfd.ver4.ccp30.DElementPartesTransporte partesTransporte = new cfd.ver4.ccp30.DElementPartesTransporte();
                
                partesTransporte.getAttParteTransporte().setString((String) session.readField(DModConsts.LS_TPT_PART_TP, new int[] { bolTptFigureTptPart.getFkTransportPartTypeId() }, DDbRegistry.FIELD_CODE));
                
                tiposFigura.getEltPartesTransporte().add(partesTransporte);
            }
            
            cartaPorte.getEltFiguraTransporte().getEltTiposFigura().add(tiposFigura);
        }
        
        return cartaPorte;
    }
    
    private static cfd.ver4.ccp31.DElementCartaPorte createCcp31FromBol(final DGuiSession session, final DDbBol bol, final DDbBizPartner bprEmisor) {
        cfd.ver4.ccp31.DElementCartaPorte cartaPorte = new cfd.ver4.ccp31.DElementCartaPorte();
        
        //cartaPorte.getAttVersion().setString(...); // attribute has default value
        cartaPorte.getAttIdCCP().setString(bol.getBolUuid());
        
        if (bol.getIntlTransport().equals(DCfdi40Catalogs.TextoNo)) {
            cartaPorte.getAttTransInternac().setString(DCfdi40Catalogs.TextoNo);
        }
        else {
            cartaPorte.getAttTransInternac().setString(DCfdi40Catalogs.TextoSí);
            cartaPorte.getAttEntradaSalidaMerc().setString(bol.getIntlTransportDirection());
            cartaPorte.getAttViaEntradaSalida().setString((String) session.readField(DModConsts.LS_TPT_TP, new int[] { bol.getFkIntlWayTransportTypeId() }, DDbRegistry.FIELD_CODE));
            cartaPorte.getAttPaisOrigenDestino().setString((String) session.readField(DModConsts.CS_CTY, new int[] { bol.getFkIntlTransportCountryId()}, DDbRegistry.FIELD_CODE));
        }
        
        if (bol.isIsthmus()) {
            cartaPorte.getAttRegistroISTMO().setString(DCfdi40Catalogs.TextoSí);
            cartaPorte.getAttUbicacionPoloOrigen().setString(bol.getIsthmusOrigin());
            cartaPorte.getAttUbicacionPoloDestino().setString(bol.getIsthmusDestiny());
        }
        
        cartaPorte.getAttTotalDistRec().setDouble(bol.getDistanceKm());
        
        // ubicaciones:
        
        for (DDbBolLocation bolLocation : bol.getChildLocations()) {
            cfd.ver4.ccp31.DElementUbicacion ubicacion = new cfd.ver4.ccp31.DElementUbicacion();
            
            ubicacion.getAttTipoUbicacion().setString((String) session.readField(DModConsts.LS_LOC_TP, new int[] { bolLocation.getFkLocationTypeId() }, DDbRegistry.FIELD_NAME));
            ubicacion.getAttIDUbicacion().setString(bolLocation.getLocationId());
            ubicacion.getAttRFCRemitenteDestinatario().setString(bprEmisor.getFiscalId());
            ubicacion.getAttNombreRemitenteDestinatario().setString(bprEmisor.getName());
            
            ubicacion.getAttNumRegIdTrib().setString("");
            ubicacion.getAttResidenciaFiscal().setString("");
            
            ubicacion.getAttFechaHoraSalidaLlegada().setDatetime(bolLocation.getArrivalDepartureDatetime());
            
            ubicacion.getAttDistanciaRecorrida().setDouble(bolLocation.getDistanceKm());
            
            ubicacion.getEltDomicilio().getAttCalle().setString(bolLocation.getAddressStreet());
            ubicacion.getEltDomicilio().getAttNumeroExterior().setString(bolLocation.getAddressNumberExt());
            ubicacion.getEltDomicilio().getAttNumeroInterior().setString(bolLocation.getAddressNumberInt());
            ubicacion.getEltDomicilio().getAttReferencia().setString(bolLocation.getAddressReference());
            ubicacion.getEltDomicilio().getAttCodigoPostal().setString(bolLocation.getAddressZipCode());
            
            String countryCode = session.getSessionCustom().getCountryCode(new int[] { bolLocation.getFkAddressCountryId() });
            ubicacion.getEltDomicilio().getAttPais().setString(countryCode);
            
            if (DFormBolUtils.applyStateCatalog(countryCode)) {
                ubicacion.getEltDomicilio().getAttEstado().setString(bolLocation.getAddressStateCode());
            }
            else {
                ubicacion.getEltDomicilio().getAttEstado().setString(bolLocation.getAddressStateName());
            }
            
            if (DFormBolUtils.applyAddressCatalogs(countryCode)) {
                ubicacion.getEltDomicilio().getAttColonia().setString(bolLocation.getAddressDistrictCode());
                ubicacion.getEltDomicilio().getAttLocalidad().setString(bolLocation.getAddressLocalityCode());
                ubicacion.getEltDomicilio().getAttMunicipio().setString(bolLocation.getAddressCountyCode());
            }
            else {
                ubicacion.getEltDomicilio().getAttColonia().setString(bolLocation.getAddressDistrictName());
                ubicacion.getEltDomicilio().getAttLocalidad().setString(bolLocation.getAddressLocalityName());
                ubicacion.getEltDomicilio().getAttMunicipio().setString(bolLocation.getAddressCountyName());
            }
            
            cartaPorte.getEltUbicaciones().getEltUbicaciones().add(ubicacion);
        }
        
        // mercancías:
        
        DDbUnit unitWeight = (DDbUnit) session.readRegistry(DModConsts.IU_UNT, new int[] { bol.getFkMerchandiseWeightUnitId() });
        
        cartaPorte.getEltMercancias().getAttPesoBrutoTotal().setDouble(bol.getMerchandiseWeight());
        cartaPorte.getEltMercancias().getAttUnidadPeso().setString(unitWeight.getCfdUnitKey());
        cartaPorte.getEltMercancias().getAttNumTotalMercancias().setInteger(bol.getMerchandiseNumber());
        
        if (bol.isMerchandiseInverseLogistics()) {
            cartaPorte.getEltMercancias().getAttLogisticaInversaRecoleccionDevolucion().setString(DCfdi40Catalogs.TextoSí);
        }
        
        for (DDbBolMerchandise bolMerchandise : bol.getChildMerchandises()) {
            cfd.ver4.ccp31.DElementMercancia mercancia = new cfd.ver4.ccp31.DElementMercancia();
            
            mercancia.getAttBienesTransp().setString(bolMerchandise.getOwnItem().getActualCfdItemKey());
            mercancia.getAttClaveSTCC().setString(""); // XXX ???
            mercancia.getAttDescripcion().setString(bolMerchandise.getDescriptionItem());
            mercancia.getAttCantidad().setDouble(bolMerchandise.getQuantity());
            mercancia.getAttClaveUnidad().setString(bolMerchandise.getOwnUnit().getCfdUnitKey());
            mercancia.getAttUnidad().setString(bolMerchandise.getDescriptionUnit());
            mercancia.getAttDimensiones().setString(bolMerchandise.getDimensions());
            if (bolMerchandise.isHazardousMaterial()) {
                if (bolMerchandise.isHazardousMaterialNo()) {
                    mercancia.getAttMaterialPeligroso().setString(DCfdi40Catalogs.TextoNo);
                }
                else if (bolMerchandise.isHazardousMaterialYes()) {
                    mercancia.getAttMaterialPeligroso().setString(DCfdi40Catalogs.TextoSí);
                    mercancia.getAttCveMaterialPeligroso().setString(bolMerchandise.getHazardousMaterialCode());
                    mercancia.getAttEmbalaje().setString(bolMerchandise.getPackagingCode());
                    mercancia.getAttDescripEmbalaje().setString(bolMerchandise.getPackagingName());
                }
            }
            mercancia.getAttPesoEnKg().setDouble(bolMerchandise.getWeightKg());
            
            if (bolMerchandise.getFkCurrencyId() != DModSysConsts.CS_CUR_NA) {
                mercancia.getAttValorMercancia().setDouble(bolMerchandise.getValue());
                mercancia.getAttMoneda().setString(session.getSessionCustom().getCurrencyCode(new int[] { bolMerchandise.getFkCurrencyId() }));
            }
            
            mercancia.getAttFraccionArancelaria().setString(bolMerchandise.getTariff());
            mercancia.getAttUUIDComercioExt().setString("");
            
            for (DDbBolMerchandiseMove bolMerchandiseMove : bolMerchandise.getChildMoves()) {
                cfd.ver4.ccp31.DElementCantidadTransporta cantidadTransporta = new cfd.ver4.ccp31.DElementCantidadTransporta();
                
                cantidadTransporta.getAttCantidad().setDouble(bolMerchandiseMove.getQuantity());
                
                DDbBolLocation source = bol.getChildLocation(bolMerchandiseMove.getSourceLocationKey());
                cantidadTransporta.getAttIDOrigen().setString(source != null ? source.getLocationId() : "");
                
                DDbBolLocation destiny = bol.getChildLocation(bolMerchandiseMove.getDestinyLocationKey());
                cantidadTransporta.getAttIDDestino().setString(destiny != null ? destiny.getLocationId() : "");
                
                cantidadTransporta.getAttCvesTransporte().setString("");
                
                mercancia.getEltCantidadTransportas().add(cantidadTransporta);
            }
            
            if (!bolMerchandise.getImportRequest().isEmpty()) {
                cfd.ver4.ccp31.DElementDocumentacionAduanera documentacionAduanera = new cfd.ver4.ccp31.DElementDocumentacionAduanera();
                documentacionAduanera.getAttTipoDocumento().setString(""); // XXX not implemented yet!
                documentacionAduanera.getAttNumPedimento().setString(bolMerchandise.getImportRequest());
                documentacionAduanera.getAttIdentDocAduanero().setString("");
                documentacionAduanera.getAttRFCImpo().setString("");
                
                ArrayList<cfd.ver4.ccp31.DElementDocumentacionAduanera> documentacionAduaneras = new ArrayList<>();
                documentacionAduaneras.add(documentacionAduanera);
                
                mercancia.setEltDocumentacionAduaneras(documentacionAduaneras);
            }

            cartaPorte.getEltMercancias().getEltMercancias().add(mercancia);
        }
        
        DDbBolTruck bolTruck = bol.getChildTrucks().get(0);
        
        cartaPorte.getEltMercancias().getEltAutotransporte().getAttPermSCT().setString(bolTruck.getPermissionTypeCode());
        cartaPorte.getEltMercancias().getEltAutotransporte().getAttNumPermisoSCT().setString(bolTruck.getPermissionNumber());
        
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltIdentificacionVehicular().getAttConfigVehicular().setString(bolTruck.getTransportConfigCode());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltIdentificacionVehicular().getAttPesoBrutoVehicular().setDouble(bolTruck.getWeightGrossTon());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltIdentificacionVehicular().getAttPlacaVM().setString(bolTruck.getPlate());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltIdentificacionVehicular().getAttAnioModeloVM().setInteger(bolTruck.getModel());
        
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttAseguraRespCivil().setString(bolTruck.getCivilInsurance());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttPolizaRespCivil().setString(bolTruck.getCivilPolicy());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttAseguraMedAmbiente().setString(bolTruck.getEnvironmentInsurance());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttPolizaMedAmbiente().setString(bolTruck.getEnvironmentPolicy());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttAseguraCarga().setString(bolTruck.getCargoInsurance());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttPolizaCarga().setString(bolTruck.getCargoPolicy());
        cartaPorte.getEltMercancias().getEltAutotransporte().getEltSeguros().getAttPrimaSeguro().setDouble(bolTruck.getPrime());

        if (!bolTruck.getChildTrailers().isEmpty()) {
            cfd.ver4.ccp31.DElementRemolques remolques = new cfd.ver4.ccp31.DElementRemolques();
            
            for (DDbBolTruckTrailer bolTruckTrailer : bolTruck.getChildTrailers()) {
                cfd.ver4.ccp31.DElementRemolque remolque = new cfd.ver4.ccp31.DElementRemolque();
                
                remolque.getAttSubTipoRem().setString(bolTruckTrailer.getTrailerSubtypeCode());
                remolque.getAttPlaca().setString(bolTruckTrailer.getPlate());
                
                remolques.getEltRemolques().add(remolque);
            }
            
            cartaPorte.getEltMercancias().getEltAutotransporte().setEltRemolques(remolques);
        }

        // figuras de transporte:
        
        for (DDbBolTransportFigure bolTptFigure : bol.getChildTransportFigures()) {
            cfd.ver4.ccp31.DElementTiposFigura tiposFigura = new cfd.ver4.ccp31.DElementTiposFigura();
            
            tiposFigura.getAttTipoFigura().setString((String) session.readField(DModConsts.LS_TPT_FIGURE_TP, new int[] { bolTptFigure.getFkTransportFigureTypeId() }, DDbRegistry.FIELD_CODE));
            tiposFigura.getAttRFCFigura().setString(bolTptFigure.getFiscalId());
            tiposFigura.getAttNumLicencia().setString(bolTptFigure.getDriverLicense());
            tiposFigura.getAttNombreFigura().setString(bolTptFigure.getName());
            
            if (bolTptFigure.getFkFigureCountryId() != DModSysConsts.CS_CTY_NA) {
                String countryCode = session.getSessionCustom().getCountryCode(new int[] { bolTptFigure.getFkFigureCountryId() });
                tiposFigura.getAttNumRegIdTribFigura().setString(bolTptFigure.getForeignId());
                tiposFigura.getAttResidenciaFiscalFigura().setString(countryCode);
            }
            
            cfd.ver4.ccp31.DElementDomicilio domicilio = new cfd.ver4.ccp31.DElementDomicilio();

            domicilio.getAttCalle().setString(bolTptFigure.getAddressStreet());
            domicilio.getAttNumeroExterior().setString(bolTptFigure.getAddressNumberExt());
            domicilio.getAttNumeroInterior().setString(bolTptFigure.getAddressNumberInt());
            domicilio.getAttReferencia().setString(bolTptFigure.getAddressReference());
            domicilio.getAttCodigoPostal().setString(bolTptFigure.getAddressZipCode());
            
            String countryCode = session.getSessionCustom().getCountryCode(new int[] { bolTptFigure.getFkAddressCountryId() });
            domicilio.getAttPais().setString(countryCode);
            
            if (DFormBolUtils.applyStateCatalog(countryCode)) {
                domicilio.getAttEstado().setString(bolTptFigure.getAddressStateCode());
            }
            else {
                domicilio.getAttEstado().setString(bolTptFigure.getAddressStateName());
            }
            
            if (DFormBolUtils.applyAddressCatalogs(countryCode)) {
                domicilio.getAttColonia().setString(bolTptFigure.getAddressDistrictCode());
                domicilio.getAttLocalidad().setString(bolTptFigure.getAddressLocalityCode());
                domicilio.getAttMunicipio().setString(bolTptFigure.getAddressCountyCode());
            }
            else {
                domicilio.getAttColonia().setString(bolTptFigure.getAddressDistrictName());
                domicilio.getAttLocalidad().setString(bolTptFigure.getAddressLocalityName());
                domicilio.getAttMunicipio().setString(bolTptFigure.getAddressCountyName());
            }
            
            tiposFigura.setEltDomicilio(domicilio);
            
            for (DDbBolTransportFigureTransportPart bolTptFigureTptPart : bolTptFigure.getChildTransportParts()) {
                cfd.ver4.ccp31.DElementPartesTransporte partesTransporte = new cfd.ver4.ccp31.DElementPartesTransporte();
                
                partesTransporte.getAttParteTransporte().setString((String) session.readField(DModConsts.LS_TPT_PART_TP, new int[] { bolTptFigureTptPart.getFkTransportPartTypeId() }, DDbRegistry.FIELD_CODE));
                
                tiposFigura.getEltPartesTransporte().add(partesTransporte);
            }
            
            cartaPorte.getEltFiguraTransporte().getEltTiposFigura().add(tiposFigura);
        }
        
        return cartaPorte;
    }
    
    public static cfd.ver40.DElementComprobante createCfdi40FromBol(final DGuiSession session, final DDbBol bol, final DGuiEdsSignature signature) throws Exception {
        Date tDate = null;
        Date tUpdateTs = bol.getTsUserUpdate() != null ? bol.getTsUserUpdate() : new Date();
        int[] anDateDps = DLibTimeUtils.digestDate(bol.getDate());
        int[] anDateEdit = DLibTimeUtils.digestDate(tUpdateTs);
        GregorianCalendar oGregorianCalendar = null;
        DDbConfigCompany configCompany = (DDbConfigCompany) session.getConfigCompany();
        DDbConfigBranch configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, bol.getCompanyBranchKey());
        DDbBizPartner bprEmisor = null;
        DDbBranch braEmisor = null;
        DDbBizPartner bprReceptor = null;
        
        // Check company branch emission configuration:

        // Emisor:

        if (configBranch.getFkBizPartnerDpsSignatureId_n() == DLibConsts.UNDEFINED) {
            // Document's emisor:
            
            bprEmisor = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, bol.getCompanyKey());
            braEmisor = (DDbBranch) session.readRegistry(DModConsts.BU_BRA, bol.getCompanyBranchKey());
        }
        else {
            // Company branch's emisor:
            
            bprEmisor = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, new int[] { configBranch.getFkBizPartnerDpsSignatureId_n() });
            braEmisor = (DDbBranch) session.readRegistry(DModConsts.BU_BRA, new int[] { configBranch.getFkBizPartnerDpsSignatureId_n(), DUtilConsts.BPR_BRA_ID });
        }

        // Receptor:

        bprReceptor = bprEmisor;

        if (DLibUtils.compareKeys(anDateDps, anDateEdit)) {
            tDate = tUpdateTs;
        }
        else {
            oGregorianCalendar = new GregorianCalendar();
            oGregorianCalendar.setTime(tUpdateTs);
            oGregorianCalendar.set(GregorianCalendar.YEAR, anDateDps[0]);
            oGregorianCalendar.set(GregorianCalendar.MONTH, anDateDps[1] - 1); // January is month 0
            oGregorianCalendar.set(GregorianCalendar.DATE, anDateDps[2]);
            tDate = oGregorianCalendar.getTime();
        }
        
        // Create XML's main element 'Comprobante':

        cfd.ver40.DElementComprobante comprobante = new cfd.ver40.DElementComprobante(); // include Addenda1 only if explicitly defined and CFD is already signed

        comprobante.getAttSerie().setString(bol.getSeries());
        comprobante.getAttFolio().setString("" + bol.getNumber());
        comprobante.getAttFecha().setDatetime(tDate);
        
        comprobante.getAttSello().setString("");
        comprobante.getAttNoCertificado().setString(signature.getCertificateNumber());
        comprobante.getAttCertificado().setString(signature.getCertificateBase64());

        comprobante.getAttFormaPago().setString("");
        comprobante.getAttCondicionesDePago().setString("");
        
        comprobante.getAttSubTotal().setDouble(0);
        comprobante.getAttSubTotal().setDecimals(0);

        comprobante.getAttMoneda().setString(DCfdi40Catalogs.ClaveMonedaXxx);
        
        comprobante.getAttTotal().setDouble(0);
        comprobante.getAttTotal().setDecimals(0);

        comprobante.getAttTipoDeComprobante().setString(DCfdi40Catalogs.CFD_TP_T);
        comprobante.getAttMetodoPago().setString("");
        
        comprobante.getAttLugarExpedicion().setString(bprEmisor.getActualAddressFiscal());
        
        comprobante.getAttConfirmacion().setString("");
        comprobante.getAttExportacion().setString(DCfdi40Catalogs.ClaveExportacionNoAplica); // fixed value, exportations not supported yet!
        
        // element 'CfdiRelacionados':
        /* Not supported yet!
        if (bol.getXtaDfrMate().getRelations() != null && !bol.getXtaDfrMate().getRelations().getRelatedCfds().isEmpty()) {
            ArrayList<cfd.ver40.DElementCfdiRelacionados> cfdiRelacionadoses = new ArrayList<>();
            for (DDfrMateRelations.RelatedCfd relatedCfd : bol.getXtaDfrMate().getRelations().getRelatedCfds()) {
                cfd.ver40.DElementCfdiRelacionados cfdiRelacionados = new cfd.ver40.DElementCfdiRelacionados();
                cfdiRelacionados.getAttTipoRelacion().setString(relatedCfd.RelationCode);

                for (String uuid : relatedCfd.Uuids) {
                    cfd.ver40.DElementCfdiRelacionado cfdiRelacionado = new cfd.ver40.DElementCfdiRelacionado();
                    cfdiRelacionado.getAttUuid().setString(uuid);
                    cfdiRelacionados.getEltCfdiRelacionados().add(cfdiRelacionado);
                }
                
                cfdiRelacionadoses.add(cfdiRelacionados);
            }
            
            comprobante.setEltOpcCfdiRelacionados(cfdiRelacionadoses);
        }
        */

        // element 'Emisor':
        comprobante.getEltEmisor().getAttRfc().setString(bprEmisor.getFiscalId());
        comprobante.getEltEmisor().getAttNombre().setString(bprEmisor.getNameFiscal());
        comprobante.getEltEmisor().getAttRegimenFiscal().setString((String) session.readField(DModConsts.CS_TAX_REG, new int[] { bprEmisor.getFkTaxRegimeId() }, DDbRegistry.FIELD_CODE));

        // element 'Receptor':
        comprobante.getEltReceptor().getAttRfc().setString(bprReceptor.getFiscalId());
        comprobante.getEltReceptor().getAttNombre().setString(bprReceptor.getNameFiscal());
        comprobante.getEltReceptor().getAttRegimenFiscalReceptor().setString((String) session.readField(DModConsts.CS_TAX_REG, new int[] { bprEmisor.getFkTaxRegimeId() }, DDbRegistry.FIELD_CODE));
        comprobante.getEltReceptor().getAttDomicilioFiscalReceptor().setString(bprEmisor.getActualAddressFiscal());
        //comprobante.getEltReceptor().getAttResidenciaFiscal().setString(""); // not supported yet!
        //comprobante.getEltReceptor().getAttNumRegIdTrib().setString(""); // not supported yet!
        comprobante.getEltReceptor().getAttUsoCFDI().setString(DCfdi40Catalogs.ClaveUsoCfdiSinEfectosFiscales);

        // element 'Conceptos':

        for (DDbBolMerchandise bolMerchandise : bol.getChildMerchandises()) {
            // "Concepto" node:

            cfd.ver40.DElementConcepto concepto = new cfd.ver40.DElementConcepto();

            concepto.getAttClaveProdServ().setString(bolMerchandise.getOwnItem().getActualCfdItemKey());
            concepto.getAttNoIdentificacion().setString(bolMerchandise.getOwnItem().getCode());
            concepto.getAttCantidad().setDouble(bolMerchandise.getQuantity());
            concepto.getAttCantidad().setDecimals(configCompany.getDecimalsQuantity());
            concepto.getAttClaveUnidad().setString(bolMerchandise.getOwnUnit().getCfdUnitKey());
            concepto.getAttUnidad().setString(bolMerchandise.getOwnUnit().getCode());
            concepto.getAttDescripcion().setString(bolMerchandise.getDescriptionItem());
            concepto.getAttValorUnitario().setDecimals(configCompany.getDecimalsPriceUnitary());
            concepto.getAttValorUnitario().setDouble(0);
            concepto.getAttImporte().setDouble(0);
            concepto.getAttDescuento().setDouble(0);
            concepto.getAttObjetoImp().setString(DCfdi40Catalogs.ClaveObjetoImpNo);

            comprobante.getEltConceptos().getEltConceptos().add(concepto);
        }
        
        // complemento carta porte:
        
        DElement cartaPorte = null;
        
        switch (bol.getVersion()) {
            case cfd.ver3.ccp20.DElementCartaPorte.VERSION:
                cartaPorte = createCcp20FromBol(session, bol, bprEmisor);
                break;
            case cfd.ver4.ccp30.DElementCartaPorte.VERSION:
                cartaPorte = createCcp30FromBol(session, bol, bprEmisor);
                break;
            case cfd.ver4.ccp31.DElementCartaPorte.VERSION:
                cartaPorte = createCcp31FromBol(session, bol, bprEmisor);
                break;
            default:
                throw new UnsupportedOperationException("Version '" + bol.getVersion() + "' not supported yet.");  // invalid complement version
        }
        
        cfd.ver40.DElementComplemento complemento = new cfd.ver40.DElementComplemento();
        complemento.getElements().add(cartaPorte);
        comprobante.setEltOpcComplemento(complemento);

        return comprobante;
    }
    
    /**
     * Extracts DFR addenda.
     * @param xmlAddenda
     * @param typeXmlAddenda
     * @return
     * @throws Exception 
     */
    public static cfd.DSubelementAddenda extractExtAddenda(final String xmlAddenda, final int typeXmlAddenda) throws Exception {
        Document document = null;
        Node node = null;
        Node nodeChild = null;
        NamedNodeMap namedNodeMapChild = null;
        Vector<Node> nodeChildren = null;
        cfd.ext.continental.DElementAddendaContinentalTire addendaContinentalTire = null;
        cfd.DSubelementAddenda extAddenda = null;
        
        switch (typeXmlAddenda) {
            case DModSysConsts.TS_XML_ADD_TP_CON:
                cfd.ext.continental.DElementPosicion eltPosicion = null;

                addendaContinentalTire = new cfd.ext.continental.DElementAddendaContinentalTire();

                if (!xmlAddenda.isEmpty()) {
                    document = DXmlUtils.parseDocument(DCfdConsts.XML_HEADER + xmlAddenda);

                    node = DXmlUtils.extractElements(document, cfd.ext.continental.DElementAddendaContinentalTire.NAME).item(0);

                    nodeChild = DXmlUtils.extractChildElements(node, cfd.ext.continental.DElementPo.NAME).get(0);
                    addendaContinentalTire.getEltPo().setValue(nodeChild.getTextContent());

                    nodeChild = DXmlUtils.extractChildElements(node, cfd.ext.continental.DElementPedido.NAME).get(0);
                    addendaContinentalTire.getEltPedido().setValue(nodeChild.getTextContent());

                    nodeChild = DXmlUtils.extractChildElements(node, cfd.ext.continental.DElementTipoProv.NAME).get(0);
                    addendaContinentalTire.getEltTipoProv().setValue(nodeChild.getTextContent());

                    nodeChild = DXmlUtils.extractChildElements(node, cfd.ext.continental.DElementPosicionesPo.NAME).get(0);
                    nodeChildren = DXmlUtils.extractChildElements(nodeChild, cfd.ext.continental.DElementPosicion.NAME);

                    for (Node child : nodeChildren) {
                        namedNodeMapChild = child.getAttributes();
                        eltPosicion = new cfd.ext.continental.DElementPosicion();
                        eltPosicion.getAttNumPosicionPo().setInteger(DLibUtils.parseInt(DXmlUtils.extractAttributeValue(namedNodeMapChild, eltPosicion.getAttNumPosicionPo().getName(), true)));
                        eltPosicion.getAttDescripcion().setString(DXmlUtils.extractAttributeValue(namedNodeMapChild, eltPosicion.getAttDescripcion().getName(), true));
                        eltPosicion.getAttTasaRetencionIva().setDouble(DLibUtils.parseDouble(DXmlUtils.extractAttributeValue(namedNodeMapChild, eltPosicion.getAttTasaRetencionIva().getName(), false)));
                        eltPosicion.getAttTasaRetencionIsr().setDouble(DLibUtils.parseDouble(DXmlUtils.extractAttributeValue(namedNodeMapChild, eltPosicion.getAttTasaRetencionIsr().getName(), false)));
                        addendaContinentalTire.getEltPosicionesPo().getElements().add(eltPosicion);
                    }
                }

                extAddenda = addendaContinentalTire;
                break;

            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        return extAddenda;
    }
    
    /**
     * Extracts existing DFR addenda on DPS only if its addenda's type corresponds to that one defined on business partner.
     * @param dps
     * @param typeXmlAddenda
     * @return
     * @throws Exception 
     */
    public static cfd.DSubelementAddenda extractExtAddenda(final DDbDps dps, final int typeXmlAddenda) throws Exception {
        cfd.DSubelementAddenda extAddenda = null;
        
        if (dps.getChildDfr() != null && dps.getChildDfr().getFkXmlAddendaTypeId() == typeXmlAddenda) {
            switch (typeXmlAddenda) {
                case DModSysConsts.TS_XML_ADD_TP_CON:
                    extAddenda = (cfd.ext.continental.DElementAddendaContinentalTire) extractExtAddenda(dps.getChildDfr().getDocXmlAddenda(), typeXmlAddenda);
                    break;
                    
                default:
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
        
        return extAddenda;
    }

    /**
     * Creates new Digital Fiscal Receipt for provided DPS and XML type (CFD, CFDI 3.2, 3.3 or 4.0).
     * @param session Current GUI user session.
     * @param dps Document for purchases and sales.
     * @param xmlType CFD type.
     * @return
     * @throws Exception 
     */
    public static DDbDfr createDfrFromDps(final DGuiSession session, final DDbDps dps, final int xmlType) throws Exception {
        int xmlStatus = DLibConsts.UNDEFINED;
        String textToSign;
        String textSigned;
        DGuiEdsSignature signature;
        DDbBizPartner bizPartner = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, dps.getBizPartnerKey());
        DDbConfigBranch configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, dps.getCompanyBranchKey());
        cfd.DCfd cfd = new cfd.DCfd(configBranch.getDfrDirectory());
        cfd.DSubelementAddenda extAddenda = null;

        switch (xmlType) {
            case DModSysConsts.TS_XML_TP_CFD:
                throw new UnsupportedOperationException("Not supported yet.");  // no plans for supporting it later

            case DModSysConsts.TS_XML_TP_CFDI_32:
                // Create DFR:
                signature = session.getEdsSignature(dps.getCompanyBranchKey());
                cfd.ver32.DElementComprobante comprobante32 = createCfdi32(session, dps);
                
                // Append to DFR the very addenda previously added to DPS if any:
                if (dps.getChildDfr() != null && !dps.getChildDfr().getDocXmlAddenda().isEmpty()) {
                    extAddenda = extractExtAddenda(dps, bizPartner.getFkXmlAddendaTypeId());
                    if (extAddenda != null) {
                        cfd.ver3.DElementAddenda addenda = new cfd.ver3.DElementAddenda();
                        addenda.getElements().add(extAddenda);
                        comprobante32.setEltOpcAddenda(addenda);
                    }
                }
                
                // Sign DFR:
                textToSign = DCfdUtils.generateOriginalString(comprobante32);
                textSigned = signature.signText(textToSign, DLibTimeUtils.digestYear(dps.getDate())[0]);
                cfd.write(comprobante32, textToSign, textSigned, signature.getCertificateNumber(), signature.getCertificateBase64());

                // Set DFR status:
                xmlStatus = DModSysConsts.TS_XML_ST_PEN;
                break;

            case DModSysConsts.TS_XML_TP_CFDI_33:
                // Create DFR:
                signature = session.getEdsSignature(dps.getCompanyBranchKey());
                cfd.ver33.DElementComprobante comprobante33 = createCfdi33(session, dps, signature);
                
                // Append to DFR the very addenda previously added to DPS if any:
                if (dps.getChildDfr() != null && !dps.getChildDfr().getDocXmlAddenda().isEmpty()) {
                    extAddenda = extractExtAddenda(dps, bizPartner.getFkXmlAddendaTypeId());
                    if (extAddenda != null) {
                        cfd.ver3.DElementAddenda addenda = new cfd.ver3.DElementAddenda();
                        addenda.getElements().add(extAddenda);
                        comprobante33.setEltOpcAddenda(addenda);
                    }
                }
                
                // Sign DFR:
                textToSign = comprobante33.getElementForOriginalString();
                textSigned = signature.signText(textToSign, DLibTimeUtils.digestYear(dps.getDate())[0]);
                cfd.write(comprobante33, textToSign, textSigned, signature.getCertificateNumber(), signature.getCertificateBase64());

                // Set DFR status:
                xmlStatus = DModSysConsts.TS_XML_ST_PEN;
                break;

            case DModSysConsts.TS_XML_TP_CFDI_40:
                // Create DFR:
                signature = session.getEdsSignature(dps.getCompanyBranchKey());
                cfd.ver40.DElementComprobante comprobante40 = createCfdi40FromDps(session, dps, signature);
                
                // Append to DFR the very addenda previously added to DPS if any:
                if (dps.getChildDfr() != null && !dps.getChildDfr().getDocXmlAddenda().isEmpty()) {
                    extAddenda = extractExtAddenda(dps, bizPartner.getFkXmlAddendaTypeId());
                    if (extAddenda != null) {
                        cfd.ver4.DElementAddenda addenda = new cfd.ver4.DElementAddenda();
                        addenda.getElements().add(extAddenda);
                        comprobante40.setEltOpcAddenda(addenda);
                    }
                }
                
                // Sign DFR:
                textToSign = comprobante40.getElementForOriginalString();
                textSigned = signature.signText(textToSign, DLibTimeUtils.digestYear(dps.getDate())[0]);
                cfd.write(comprobante40, textToSign, textSigned, signature.getCertificateNumber(), signature.getCertificateBase64());

                // Set DFR status:
                xmlStatus = DModSysConsts.TS_XML_ST_PEN;
                break;

            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        // Create or update DFR:
        DDbDfr dfr;
        
        if (dps.getChildDfr() == null) {
            dfr = new DDbDfr();
        }
        else {
            dfr = dps.getChildDfr();
        }
        
        //dfr.setPkDfrId(...);
        dfr.setCertificateNumber(session.getEdsSignature(dps.getCompanyBranchKey()).getCertificateNumber());
        dfr.setSignedText(cfd.getLastStringSigned());
        dfr.setSignature(cfd.getLastSignature());
        dfr.setUuid("");
        dfr.setDocTs(cfd.getLastTimestamp());
        dfr.setDocXml(cfd.getLastXml());
        dfr.setDocXmlRaw("");
        dfr.setDocXmlAddenda(extAddenda == null ? "" : extAddenda.getElementForXml());
        dfr.setDocXmlName(cfd.getLastXmlFileName());
        dfr.setCancelStatus("");
        dfr.setCancelXml("");
        dfr.setCancelPdf_n(null);
        /*
        dfr.setBookeept();
        registry.setDeleted();
        registry.setSystem();
        */
        dfr.setFkXmlTypeId(xmlType);
        dfr.setFkXmlSubtypeId(DModSysConsts.TS_XML_STP_VER_FAC[0]); // invoice
        dfr.setFkXmlSubtypeVersionId(DModSysConsts.TS_XML_STP_VER_FAC[1]); // invoice
        dfr.setFkXmlStatusId(xmlStatus);
        dfr.setFkXmlAddendaTypeId(bizPartner.getFkXmlAddendaTypeId());
        dfr.setFkXmlSignatureProviderId(DModSysConsts.CS_XSP_NA);
        dfr.setFkCertificateId(session.getEdsSignature(dps.getCompanyBranchKey()).getCertificateId());
        dfr.setFkOwnerBizPartnerId(dps.getFkOwnerBizPartnerId());
        dfr.setFkOwnerBranchId(dps.getFkOwnerBranchId());
        dfr.setFkBizPartnerId(dps.getFkBizPartnerBizPartnerId());
        dfr.setFkDpsId_n(dps.getPkDpsId());
        dfr.setFkBolId_n(0);
        /*
        dfr.setFkBookkeepingYearId_n();
        dfr.setFkBookkeepingNumberId_n();
        dfr.setFkUserIssuedId();    // will be set as needed when saved!
        dfr.setFkUserAnnulledId();  // will be set as needed when saved!
        dfr.setFkUserInsertId();    // will be set as needed when saved!
        dfr.setFkUserUpdateId();    // will be set as needed when saved!
        */

        return dfr;
    }

    /**
     * Creates new Digital Fiscal Receipt for provided BOL and XML type (CFDI 3.3 or 4.0).
     * @param session Current GUI user session.
     * @param bol Bill of lading.
     * @param xmlType CFD type.
     * @return
     * @throws Exception 
     */
    public static DDbDfr createDfrFromBol(final DGuiSession session, final DDbBol bol, final int xmlType) throws Exception {
        int xmlStatus = DLibConsts.UNDEFINED;
        String textToSign;
        String textSigned;
        DGuiEdsSignature signature;
        DDbBizPartner bizPartner = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, bol.getCompanyBranchKey());
        DDbConfigBranch configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, bol.getCompanyBranchKey());
        cfd.DCfd cfd = new cfd.DCfd(configBranch.getDfrDirectory());
        cfd.DSubelementAddenda extAddenda = null;

        switch (xmlType) {
            case DModSysConsts.TS_XML_TP_CFD:
            case DModSysConsts.TS_XML_TP_CFDI_32:
            case DModSysConsts.TS_XML_TP_CFDI_33:
                throw new UnsupportedOperationException("Not supported yet.");  // no plans for supporting it later

            case DModSysConsts.TS_XML_TP_CFDI_40:
                // Create DFR:
                signature = session.getEdsSignature(bol.getCompanyBranchKey());
                cfd.ver40.DElementComprobante comprobante40 = createCfdi40FromBol(session, bol, signature);
                
                // Sign DFR:
                textToSign = comprobante40.getElementForOriginalString();
                textSigned = signature.signText(textToSign, DLibTimeUtils.digestYear(bol.getDate())[0]);
                cfd.write(comprobante40, textToSign, textSigned, signature.getCertificateNumber(), signature.getCertificateBase64());

                // Set DFR status:
                xmlStatus = DModSysConsts.TS_XML_ST_PEN;
                break;

            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        // Create or update DFR:
        DDbDfr dfr;
        
        if (bol.getChildDfr() == null) {
            dfr = new DDbDfr();
        }
        else {
            dfr = bol.getChildDfr();
        }
        
        //dfr.setPkDfrId(...);
        dfr.setCertificateNumber(session.getEdsSignature(bol.getCompanyBranchKey()).getCertificateNumber());
        dfr.setSignedText(cfd.getLastStringSigned());
        dfr.setSignature(cfd.getLastSignature());
        dfr.setUuid("");
        dfr.setDocTs(cfd.getLastTimestamp());
        dfr.setDocXml(cfd.getLastXml());
        dfr.setDocXmlRaw("");
        dfr.setDocXmlAddenda(extAddenda == null ? "" : extAddenda.getElementForXml());
        dfr.setDocXmlName(cfd.getLastXmlFileName());
        dfr.setCancelStatus("");
        dfr.setCancelXml("");
        dfr.setCancelPdf_n(null);
        /*
        dfr.setBookeept();
        registry.setDeleted();
        registry.setSystem();
        */
        dfr.setFkXmlTypeId(xmlType);
        dfr.setFkXmlSubtypeId(DModSysConsts.TS_XML_STP_VER_CCP_31[0]); // bill of lading
        dfr.setFkXmlSubtypeVersionId(DModSysConsts.TS_XML_STP_VER_CCP_31[1]); // bill of lading
        dfr.setFkXmlStatusId(xmlStatus);
        dfr.setFkXmlAddendaTypeId(bizPartner.getFkXmlAddendaTypeId());
        dfr.setFkXmlSignatureProviderId(DModSysConsts.CS_XSP_NA);
        dfr.setFkCertificateId(session.getEdsSignature(bol.getCompanyBranchKey()).getCertificateId());
        dfr.setFkOwnerBizPartnerId(bol.getFkOwnerBizPartnerId());
        dfr.setFkOwnerBranchId(bol.getFkOwnerBranchId());
        dfr.setFkBizPartnerId(bol.getFkOwnerBizPartnerId());
        dfr.setFkDpsId_n(0);
        dfr.setFkBolId_n(bol.getPkBolId());
        /*
        dfr.setFkBookkeepingYearId_n();
        dfr.setFkBookkeepingNumberId_n();
        dfr.setFkUserIssuedId();    // will be set as needed when saved!
        dfr.setFkUserAnnulledId();  // will be set as needed when saved!
        dfr.setFkUserInsertId();    // will be set as needed when saved!
        dfr.setFkUserUpdateId();    // will be set as needed when saved!
        */

        return dfr;
    }

    /**
     * Validates that CFDI's XML corresponds to DFR registry.
     * @param xml CFDI's XML.
     * @param dfr DFR registry.
     * @return true if is correct.
     * @throws java.lang.Exception
     */
    @SuppressWarnings("deprecation")
    public static boolean belongsXmlToDfr(final String xml, final DDbDfr dfr) throws Exception {
        cfd.ver32.DElementComprobante comprobanteXml = DCfdUtils.getCfdi32(xml);
        cfd.ver32.DElementComprobante comprobanteDfr = DCfdUtils.getCfdi32(dfr.getDocXml());

        boolean valid = comprobanteXml.getEltEmisor().getAttRfc().getString().compareTo(comprobanteDfr.getEltEmisor().getAttRfc().getString()) == 0 &&
                comprobanteXml.getEltReceptor().getAttRfc().getString().compareTo(comprobanteDfr.getEltReceptor().getAttRfc().getString()) == 0 &&
                comprobanteXml.getAttSerie().getString().compareTo(comprobanteDfr.getAttSerie().getString()) == 0 &&
                comprobanteXml.getAttFolio().getString().compareTo(comprobanteDfr.getAttFolio().getString()) == 0 &&
                comprobanteXml.getAttFecha().getDatetime().compareTo(comprobanteDfr.getAttFecha().getDatetime()) == 0 &&
                comprobanteXml.getAttTotal().getDouble() == comprobanteDfr.getAttTotal().getDouble();

        return valid;
    }

    /**
     * Sign DFR.
     * @param session GUI user session.
     * @param doc Document of DFR.
     * @param xmlSignatureProviderId ID of signature provider.
     * @param signatureCompanyBranchKey Company branch responsible for the signature event.
     * @param requestSubtype Constants defined in DModSysConsts.TX_XMS_REQ_STP_...
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws Exception 
     */
    public static void signDfr(final DGuiSession session, final DTrnDoc doc, final int xmlSignatureProviderId, final int[] signatureCompanyBranchKey, final int requestSubtype) throws TransformerConfigurationException, TransformerException, Exception {
        DDbDfr dfr = doc.getDfr();
        
        // ensure exclusive access is granted:
        doc.assureLock(session); // lock should already be set
        
        // Sign DFR:
        
        String cfdi = "";
        String edsName = "";
        String edsPassword = "";
        DocumentBuilder documentBuilder = null;
        Document document = null;
        NodeList nodeList = null;
        Node node = null;
        Node nodeChildTimbreFiscal = null;
        NamedNodeMap namedNodeMapTimbreFiscal = null;
        cfd.ver3.DTimbreFiscal timbreFiscal = null;
        DDbConfigBranch configBranch = null;
        DDbXmlSignatureRequest xsr = null;
        
        // Create or obtain XML Signature Request for signature:
        
        /*
        Status of XML Signature Request:
        DModSysConsts.TX_XMS_REQ_ST_STA: Request started.
        DModSysConsts.TX_XMS_REQ_ST_PRC: Request processed (CFDI signed/cancelled).
        DModSysConsts.TX_XMS_REQ_ST_CMP: Request computed (stamp consumed).
        DModSysConsts.TX_XMS_REQ_ST_FIN: Request finished (PDF generated).
        */
        
        switch (requestSubtype) {
            case DModSysConsts.TX_XMS_REQ_STP_REQ:  // log a sign request
                xsr = new DDbXmlSignatureRequest();
                xsr.setPkDfrId(dfr.getPkDfrId());
                //xsr.setPkRequestId(0);
                xsr.setRequestType(DModSysConsts.TX_XMS_REQ_TP_SIG);
                xsr.setRequestStatus(DModSysConsts.TX_XMS_REQ_ST_STA);
                xsr.setAnnulReasonCode("");
                xsr.setAnnulRelatedUuid("");
                xsr.setDeleted(false);
                xsr.setSystem(true);
                xsr.setFkXmlSignatureProviderId(xmlSignatureProviderId);
                xsr.save(session);
                break;
                
            case DModSysConsts.TX_XMS_REQ_STP_VER:  // log a sign verification
                xsr = DTrnEmissionUtils.getLastXmlSignatureRequest(session, dfr.getPkDfrId());
                if (xsr == null) {
                    throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND + " (" + DUtilConsts.TXT_LOG + ")");
                }
                else if (xsr.getRequestType() != DModSysConsts.TX_XMS_REQ_TP_SIG) {
                    throw new Exception(DLibConsts.ERR_MSG_WRONG_TYPE + " (" + DUtilConsts.TXT_TP + ")");
                }
                else if (xsr.getRequestStatus() == DModSysConsts.TX_XMS_REQ_ST_FIN) {
                    throw new Exception(DLibConsts.ERR_MSG_WRONG_TYPE + " (" + DUtilConsts.TXT_ST + ")");
                }
                break;
            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        // Request signature:
        
        if (xsr.getRequestStatus() < DModSysConsts.TX_XMS_REQ_ST_PRC) {
            /* Code used in fiscal stamp testing: (DO NOT DELETE! KEEPED JUST FOR REFERENCE!)
            XmlProcess xmlProcess = null;
            oInputFile = new BufferedInputStream(new FileInputStream(sXmlBaseDir + oDpsXml.getDocXmlName()));
            xmlProcess = new XmlProcess(oInputFile);
            sCfdi = xmlProcess.generaTextoXML();
            */

            configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, dfr.getCompanyBranchKey());
            
            edsName = configBranch.getDfrName();
            edsPassword = configBranch.getDfrPassword();
            
            switch (xmlSignatureProviderId) {
                case DModSysConsts.CS_XSP_FCG:  // FORCOGSA

                    if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                        // Sign request:
                        
                        if (((DDbConfigCompany) session.getConfigCompany()).isDevelopment()) {
                            // Development environment:
                            /* Simulate stamping:
                            
                            // WARNING: Works only with CFDI 3.2!

                            com.wscliente.WSForcogsaService fcgService = null;
                            com.wscliente.WSForcogsa fcgPort = null;
                            com.wscliente.WsAutenticarResponse autenticarResponse = null;
                            com.wscliente.WsTimbradoResponse timbradoResponse = null;

                            fcgService = new com.wscliente.WSForcogsaService();
                            fcgPort = fcgService.getWSForcogsaPort();

                            // Web service autentication:

                            autenticarResponse = fcgPort.autenticar(edsName, edsPassword);

                            // Web service request:

                            timbradoResponse = fcgPort.timbrar(dfr.getDocXml(), autenticarResponse.getToken());
                            
                            // Process response:

                            if (timbradoResponse.getMensaje() != null) {
                                xsr.delete(session); // delete request log
                                throw new Exception("WsTimbradoResponse msg: [" + timbradoResponse.getMensaje() + "]");
                            }
                            
                            cfdi = timbradoResponse.getCfdi();
                            */
                            cfdi = new String(dfr.getDocXml().getBytes("UTF-8")); // simulate stamping, returning the same XML, please add in a break point XML stamping complement!
                        }
                        else {
                            // Production environment:
                            
                            /*
                            // This commented source-code block works only with CFDI 3.2!
                            forsedi.timbrado.WSForcogsaService fcgService = null;
                            forsedi.timbrado.WSForcogsa fcgPort = null;
                            forsedi.timbrado.WsAutenticarResponse autenticarResponse = null;
                            forsedi.timbrado.WsTimbradoResponse timbradoResponse = null;

                            fcgService = new forsedi.timbrado.WSForcogsaService();
                            fcgPort = fcgService.getWSForcogsaPort();

                            // Web Service Autentication:

                            autenticarResponse = fcgPort.autenticar(edsName, edsPassword);

                            // Web service request:

                            timbradoResponse = fcgPort.timbrar(dfr.getDocXml(), autenticarResponse.getToken());

                            // Process response:

                            if (timbradoResponse.getMensaje() != null) {
                                xsr.delete(session); // delete request log
                                throw new Exception("WsTimbradoResponse msg: [" + timbradoResponse.getMensaje() + "]");
                            }

                            cfdi = timbradoResponse.getCfdi();
                            */
                            
                            // NOTE: Works only with CFDI 3.3!

                            wservicios.WSForcogsaService fcgService = null;
                            wservicios.WSForcogsa fcgPort = null;
                            wservicios.WsAutenticarResponse autenticarResponse = null;
                            wservicios.WsTimbradoResponse timbradoResponse = null;

                            fcgService = new wservicios.WSForcogsaService();
                            fcgPort = fcgService.getWSForcogsaPort();

                            // Web Service Autentication:

                            autenticarResponse = fcgPort.autenticar(edsName, edsPassword);

                            // Web service request:

                            timbradoResponse = fcgPort.timbrar(dfr.getDocXml(), autenticarResponse.getToken());

                            // Process response:

                            if (timbradoResponse.getMensaje() != null) {
                                xsr.delete(session); // delete request log
                                throw new Exception("WsTimbradoResponse msg: [" + timbradoResponse.getMensaje() + "]");
                            }

                            cfdi = timbradoResponse.getCfdi();
                        }
                    }
                    else {
                        // Sign verification request:
                        
                        cfdi = DTrnEmissionUtils.getFileXml(session.getClient());
                        
                        if (cfdi.isEmpty()) {
                            throw new Exception(DUtilConsts.FILE_EXT_XML.toUpperCase() + " " + DLibConsts.ERR_MSG_UNKNOWN.toLowerCase());
                        }
                        else if (!belongsXmlToDfr(cfdi, dfr)) {
                            throw new Exception(DLibConsts.ERR_MSG_WRONG_TYPE + " (" + DUtilConsts.FILE_EXT_XML.toUpperCase() + ")");
                        }
                    }

                    break;

                case DModSysConsts.CS_XSP_FNK:  // Finkok
                    if (((DDbConfigCompany) session.getConfigCompany()).isDevelopment()) {
                        // Development environment:
                        /* Simulate stamping:
                        stamp_demo.AcuseRecepcionCFDI acuseRecepcionCfdi = null;
                        JAXBElement<stamp_demo.IncidenciaArray> incidencias = null;
                        stamp_demo.Application port = new stamp_demo.StampSOAP().getApplication();

                        // Web service request:

                        if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                            // Sign request:
                            
                            acuseRecepcionCfdi = port.stamp(dfr.getDocXml().getBytes("UTF-8"), edsName, edsPassword);
                        }
                        else {
                            // Sign verification request:
                            
                            acuseRecepcionCfdi = port.stamped(dfr.getDocXml().getBytes("UTF-8"), edsName, edsPassword);
                        }
                        
                        // Process response:

                        incidencias = acuseRecepcionCfdi.getIncidencias();

                        if (!incidencias.getValue().getIncidencia().isEmpty()) {
                            xsr.delete(session); // delete request log
                            
                            String message = "";
                            for (stamp_demo.Incidencia incidencia : incidencias.getValue().getIncidencia()) {
                                message += (message.isEmpty() ? "" : "\n") + "WsAcuseRecepcionCFDI code: [" + incidencia.getCodigoError().getValue() + "]" + " msg: [" + incidencia.getMensajeIncidencia().getValue() + "]";
                            }

                            throw new Exception(message);
                        }
                        cfdi = acuseRecepcionCfdi.getXml().getValue();
                        */
                        
                        cfdi = new String(dfr.getDocXml().getBytes("UTF-8")); // simulate stamping, returning the same XML, please add in a break point XML stamping complement!
                    }
                    else {
                        // Production environment:
                        
                        com.finkok.stamp.AcuseRecepcionCFDI acuseRecepcionCfdi = null;
                        JAXBElement<com.finkok.stamp.IncidenciaArray> incidencias = null;
                        com.finkok.stamp.Application port = new com.finkok.stamp.StampSOAP().getApplication();

                        // Web service request:

                        if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                            // Request:
                            
                            acuseRecepcionCfdi = port.stamp(dfr.getDocXml().getBytes("UTF-8"), edsName, edsPassword);
                        }
                        else {
                            // Verification:
                            
                            acuseRecepcionCfdi = port.stamped(dfr.getDocXml().getBytes("UTF-8"), edsName, edsPassword);
                        }

                        // Process response:

                        incidencias = acuseRecepcionCfdi.getIncidencias();

                        if (!incidencias.getValue().getIncidencia().isEmpty()) {
                            xsr.delete(session); // delete request log

                            String message = "";
                            for (com.finkok.stamp.Incidencia incidencia : incidencias.getValue().getIncidencia()) {
                                message += (message.isEmpty() ? "" : "\n") + "WsAcuseRecepcionCFDI code: [" + incidencia.getCodigoError().getValue() + "]" + " msg: [" + incidencia.getMensajeIncidencia().getValue() + "]";
                            }

                            throw new Exception(message);
                        }

                        cfdi = acuseRecepcionCfdi.getXml().getValue();
                    }
                    break;

                case DModSysConsts.CS_XSP_TST:  // testing
                    cfdi = dfr.getDocXml();   // insert by hand for testint purposes 'Complemento' node into this local variable 'cfdi'!
                    break;
                    
                default:
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN + " " + DTrnEmissionConsts.PAC);
            }
            
            // Extract signed XML:

            documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = documentBuilder.parse(new ByteArrayInputStream(cfdi.getBytes("UTF-8")));
            nodeList = document.getElementsByTagName("cfdi:Complemento");

            if (nodeList == null) {
                xsr.delete(session); // delete request log
                throw new Exception("XML element 'cfdi:Complemento' not found!");
            }
            else {
                node = nodeList.item(0);
                
                if (node == null) {
                    xsr.delete(session); // delete request log
                    throw new Exception("XML element 'cfdi:Complemento' not found!");
                }
                else {
                    nodeList = node.getChildNodes();

                    if (nodeList == null) {
                        xsr.delete(session); // delete request log
                        throw new Exception("XML element 'cfdi:Complemento' does not have child elements!");
                    }
                    else {
                        for (int i = 0; i < nodeList.getLength(); i++) {
                            if (nodeList.item(i).getNodeName().compareTo("tfd:TimbreFiscalDigital") == 0) {
                                nodeChildTimbreFiscal = nodeList.item(i);
                                break;
                            }
                        }
                    }
                }
            }
            
            if (nodeChildTimbreFiscal == null) {
                xsr.delete(session); // delete request log
                throw new Exception("XML element 'tfd:TimbreFiscalDigital' not found!");
            }
            // Preserve signed XML into DFR:

            timbreFiscal = new cfd.ver3.DTimbreFiscal(); // compatible with version 4.0
            namedNodeMapTimbreFiscal = nodeChildTimbreFiscal.getAttributes();
            
            switch (dfr.getFkXmlTypeId()) {
                case DModSysConsts.TS_XML_TP_CFDI_32:
                    node = namedNodeMapTimbreFiscal.getNamedItem("Version");
                    timbreFiscal.setVersion(node.getNodeValue());

                    node = namedNodeMapTimbreFiscal.getNamedItem("UUID");
                    timbreFiscal.setUuid(node.getNodeValue());

                    node = namedNodeMapTimbreFiscal.getNamedItem("FechaTimbrado");
                    timbreFiscal.setFechaTimbrado(node.getNodeValue());

                    node = namedNodeMapTimbreFiscal.getNamedItem("selloCFD");
                    timbreFiscal.setSelloCfd(node.getNodeValue());

                    node = namedNodeMapTimbreFiscal.getNamedItem("noCertificadoSAT");
                    timbreFiscal.setNoCertificadoSat(node.getNodeValue());

                    node = namedNodeMapTimbreFiscal.getNamedItem("selloSAT");
                    timbreFiscal.setSelloSat(node.getNodeValue());

                    timbreFiscal.setPacId(xmlSignatureProviderId);
                    break;
                    
                case DModSysConsts.TS_XML_TP_CFDI_33:
                case DModSysConsts.TS_XML_TP_CFDI_40:
                    node = namedNodeMapTimbreFiscal.getNamedItem("Version");
                    timbreFiscal.setVersion(node.getNodeValue());

                    node = namedNodeMapTimbreFiscal.getNamedItem("UUID");
                    timbreFiscal.setUuid(node.getNodeValue());

                    node = namedNodeMapTimbreFiscal.getNamedItem("FechaTimbrado");
                    timbreFiscal.setFechaTimbrado(node.getNodeValue());

                    node = namedNodeMapTimbreFiscal.getNamedItem("RfcProvCertif");
                    timbreFiscal.setRfcProvCertif(node.getNodeValue());

                    node = namedNodeMapTimbreFiscal.getNamedItem("Leyenda");
                    if (node != null) {
                        timbreFiscal.setLeyenda(node.getNodeValue());
                    }

                    node = namedNodeMapTimbreFiscal.getNamedItem("SelloCFD");
                    timbreFiscal.setSelloCfd(node.getNodeValue());

                    node = namedNodeMapTimbreFiscal.getNamedItem("NoCertificadoSAT");
                    timbreFiscal.setNoCertificadoSat(node.getNodeValue());

                    node = namedNodeMapTimbreFiscal.getNamedItem("SelloSAT");
                    timbreFiscal.setSelloSat(node.getNodeValue());

                    timbreFiscal.setPacId(xmlSignatureProviderId);
                    break;

                default:
            }

            dfr.setUuid(timbreFiscal.getUuid());
            dfr.setDocXml(cfdi);
            dfr.setDocXmlRaw(cfdi);
            dfr.setFkXmlStatusId(DModSysConsts.TS_XML_ST_ISS);
            dfr.setFkXmlSignatureProviderId(timbreFiscal.getPacId());
            dfr.setAuxDfrJustIssued(true);
            dfr.save(session);

            xsr.setRequestStatus(DModSysConsts.TX_XMS_REQ_ST_PRC);
            xsr.save(session);
        }
        
        if (xsr.getRequestStatus() < DModSysConsts.TX_XMS_REQ_ST_CMP) {
            // Consume stamp:

            DTrnEmissionUtils.consumeStamp(session, xsr.getFkXmlSignatureProviderId(), signatureCompanyBranchKey, DModSysConsts.TS_XSM_TP_OUT_SIG, dfr.getPkDfrId());
            xsr.setRequestStatus(DModSysConsts.TX_XMS_REQ_ST_CMP);
            xsr.save(session);
        }
        
        if (xsr.getRequestStatus() < DModSysConsts.TX_XMS_REQ_ST_FIN) {
            // Print DFR:

            doc.printDfr(session);
            xsr.setRequestStatus(DModSysConsts.TX_XMS_REQ_ST_FIN);
            xsr.save(session);
        }
    }

    /**
     * Cancel DFR.
     * @param session GUI user session.
     * @param doc Document of DFR.
     * @param xmlSignatureProviderId ID of signature provider.
     * @param signatureCompanyBranchKey Company branch responsible for the signature event.
     * @param requestSubtype Constants defined in DModSysConsts.TX_XMS_REQ_STP_...
     * @param cancelParams Cancel paramenters.
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws Exception 
     */
    public static void cancelDfr(final DGuiSession session, final DTrnDoc doc, final int xmlSignatureProviderId, final int[] signatureCompanyBranchKey, final int requestSubtype, final DTrnAnnulParams cancelParams) throws TransformerConfigurationException, TransformerException, Exception {
        DDbDfr dfr = doc.getDfr();
        
        // ensure exclusive access is granted:
        doc.assureLock(session); // lock should already be set
        
        // Cancel DFR:
        
        String xmlAcuse = "";
        DDbXmlSignatureRequest xsr = null;
        
        // Create or obtain XML Signature Request for cancellation:

        /*
        Status of XML Signature Request:
        DModSysConsts.TX_XMS_REQ_ST_STA: Request started.
        DModSysConsts.TX_XMS_REQ_ST_PRC: Request processed (CFDI signed/cancelled).
        DModSysConsts.TX_XMS_REQ_ST_CMP: Request computed (stamp consumed).
        DModSysConsts.TX_XMS_REQ_ST_FIN: Request finished (PDF generated).
        */
        
        if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_REQ || (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_VER && cancelParams.ConfirmCancel)) {
            xsr = new DDbXmlSignatureRequest();
            
            xsr.setPkDfrId(dfr.getPkDfrId());
            //xsr.setPkRequestId(0);
            xsr.setRequestType(DModSysConsts.TX_XMS_REQ_TP_CAN);
            xsr.setRequestStatus(DModSysConsts.TX_XMS_REQ_ST_STA);
            xsr.setAnnulReasonCode(cancelParams.AnnulReasonCode);
            xsr.setAnnulRelatedUuid(cancelParams.AnnulRelatedUuid);
            xsr.setDeleted(false);
            xsr.setSystem(true);
            xsr.setFkXmlSignatureProviderId(xmlSignatureProviderId);
            xsr.save(session);
        }
        else if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_VER) {
            xsr = DTrnEmissionUtils.getLastXmlSignatureRequest(session, dfr.getPkDfrId());
            
            if (xsr == null) {
                throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND + " (" + DUtilConsts.TXT_LOG + ")");
            }
            else if (xsr.getRequestType() != DModSysConsts.TX_XMS_REQ_TP_CAN) {
                throw new Exception(DLibConsts.ERR_MSG_WRONG_TYPE + " (" + DUtilConsts.TXT_TP + ")");
            }
            else if (xsr.getRequestStatus() == DModSysConsts.TX_XMS_REQ_ST_FIN) {
                throw new Exception(DLibConsts.ERR_MSG_WRONG_TYPE + " (" + DUtilConsts.TXT_ST + ")");
            }
        }
        else {
            throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        // Request cancellation:
        
        if (xsr.getRequestStatus() < DModSysConsts.TX_XMS_REQ_ST_PRC) {
            if (cancelParams.AnnulAction == DTrnEmissionConsts.ACTION_ANNUL_CANCEL) {
                DDbBizPartner company = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, dfr.getCompanyKey());
                DDbConfigBranch configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, dfr.getCompanyBranchKey());
                DDbCertificate certificate = (DDbCertificate) session.readRegistry(DModConsts.CU_CER, new int[] { configBranch.getFkCertificateId_n() });
                
                String fiscalIdIssuer = company.getFiscalId();
                String edsName = configBranch.getDfrName();
                String edsPswd = configBranch.getDfrPassword();
                String cancelStatusCode = "";
                
                switch (xmlSignatureProviderId) {
                    case DModSysConsts.CS_XSP_FCG:  // FORCOGSA
                        /*
                        if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                            // Cancel request:

                            if (((DDbConfigCompany) session.getConfigCompany()).isDevelopment()) {
                                // Development environment:

                                com.wscliente.WSForcogsaService fcgService = null;
                                com.wscliente.WSForcogsa fcgPort = null;
                                com.wscliente.WsAutenticarResponse autenticarResponse = null;
                                com.wscliente.WsTimbradoResponse timbradoResponse = null;

                                fcgService = new com.wscliente.WSForcogsaService();
                                fcgPort = fcgService.getWSForcogsaPort();

                                // Web service autentication:

                                autenticarResponse = fcgPort.autenticar(edsName, edsPassword);

                                // Web service request:

                                timbradoResponse = fcgPort.timbrar(dfr.getDocXml(), autenticarResponse.getToken());

                                if (timbradoResponse.getMensaje() != null) {
                                    xsr.delete(session); // delete request log
                                    throw new Exception("WsTimbradoResponse msg: [" + timbradoResponse.getMensaje() + "]");
                                }

                                cfdi = timbradoResponse.getCfdi();
                            }
                            else {
                                // Production environment:

                                forsedi.timbrado.WSForcogsaService fcgService = null;
                                forsedi.timbrado.WSForcogsa fcgPort = null;
                                forsedi.timbrado.WsAutenticarResponse autenticarResponse = null;
                                forsedi.timbrado.WsTimbradoResponse timbradoResponse = null;

                                fcgService = new forsedi.timbrado.WSForcogsaService();
                                fcgPort = fcgService.getWSForcogsaPort();

                                // Web Service Autentication:

                                autenticarResponse = fcgPort.autenticar(edsName, edsPassword);

                                // Document stamp:

                                timbradoResponse = fcgPort.timbrar(dfr.getDocXml(), autenticarResponse.getToken());

                                if (timbradoResponse.getMensaje() != null) {
                                    xsr.delete(session); // delete request log
                                    throw new Exception("WsTimbradoResponse msg: [" + timbradoResponse.getMensaje() + "]");
                                }

                                cfdi = timbradoResponse.getCfdi();
                            }
                        }
                        else {
                            // Cancel verification request:

                            cfdi = DTrnEmissionUtils.getFileXml(session.getClient());

                            if (cfdi.isEmpty()) {
                                throw new Exception(DUtilConsts.FILE_EXT_XML.toUpperCase() + " " + DLibConsts.ERR_MSG_UNKNOWN.toLowerCase());
                            }
                            else if (!belongsXmlToDfr(cfdi, dfr)) {
                                throw new Exception(DLibConsts.ERR_MSG_WRONG_TYPE + " (" + DUtilConsts.FILE_EXT_XML.toUpperCase() + ")");
                            }
                        }

                        break;
                        */
                    case DModSysConsts.CS_XSP_FNK:  // Finkok
                        /*
                        2018-11-29, Sergio Flores:
                        Please note that Finkok cancellation and cancellation-demo web services cannot be used at the same time:
                        Java classes automatically generated are the same for both web services,
                        and cannot be customized because of conflicts in class canonical names!
                        */
                        
                        if (((DDbConfigCompany) session.getConfigCompany()).isDevelopment()) {
                            // Development environment:
                            
                            /*
                            TODO: By now, cancellation in development environment with Finkok is not going to be implemented yet.
                            */
                            
                            /* Testing code is obsolete for new cancel methods of 2022!
                            QName uuidQName = null;
                            JAXBElement<com.finkok.facturacion.cancellation.StringArray> uuidValue = null;
                            JAXBElement<views.core.soap.services.apps.FolioArray> folios = null;
                            com.finkok.facturacion.cancellation.StringArray aUuids = null;
                            views.core.soap.services.apps.UUIDS uuids = null;
                            views.core.soap.services.apps.CancelaCFDResult cancelaCFDResult = null;
                            views.core.soap.services.apps.ReceiptResult receiptResult = null;
                            com.finkok.facturacion.cancel.CancelSOAP service = null;
                            com.finkok.facturacion.cancel.Application port = null;

                            service = new com.finkok.facturacion.cancel.CancelSOAP();
                            port = service.getApplication();

                            // Web service request:

                            if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                                // Cancel request:
                                
                                alUuids = new ArrayList<>();
                                alUuids.add(dfr.getUuid());
                                
                                aUuids = new com.finkok.facturacion.cancellation.StringArray();
                                aUuids.getString().addAll(alUuids);
                                
                                uuidQName = new QName("uuids");
                                uuidValue = new JAXBElement<>(uuidQName, com.finkok.facturacion.cancellation.StringArray.class, aUuids);
                                
                                uuids = new views.core.soap.services.apps.UUIDS();
                                uuids.setUuids(uuidValue);
                                
                                if (dfr.getFkXmlSignatureProviderId() == xmlSignatureProviderId) {
                                    // Cancel own signed document:
                                    
                                    cancelaCFDResult = port.cancel(uuids, edsName, edsPassword, fiscalId, certificate.getXtaCertificatePemKeyPublic_n(), certificate.getXtaCertificatePemKeyPrivate_n(), true);
                                }
                                else {
                                    // Cancel third party signed document:
                                    
                                    cancelaCFDResult = port.outCancel(Base64.encode(dfr.getDocXml().getBytes()), edsName, edsPassword, fiscalId, certificate.getXtaCertificatePemKeyPublic_n(), certificate.getXtaCertificatePemKeyPrivate_n(), true);
                                }
                            }
                            else {
                                // Cancel verification request:
                                
                                receiptResult = port.getReceipt(edsName, edsPassword, fiscalId, dfr.getUuid(), "C");
                            }
                            
                            // Process response:

                            if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                                // Cancel request response:
                                
                                folios = cancelaCFDResult.getFolios();

                                if (folios == null) {
                                    xsr.delete(session); // delete request log
                                    throw new Exception("CancelaCFDResult msg: folios = null: [" + cancelaCFDResult.getCodEstatus().getValue() + "]");
                                }
                                else if (folios.getValue().getFolio().isEmpty()) {
                                    xsr.delete(session); // delete request log
                                    throw new Exception("CancelaCFDResult msg: folios.folio is empty: [" + cancelaCFDResult.getCodEstatus().getValue() + "]");
                                }
                                else if (folios.getValue().getFolio().get(0).getEstatusUUID().getValue().compareTo(DTrnEmissionConsts.UUID_ANNUL) != 0) {
                                    value = folios.getValue().getFolio().get(0).getEstatusUUID().getValue();

                                    xsr.delete(session); // delete request log
                                    throw new Exception("CancelaCFDResult msg: folios.folio.estatus != 201: [" + value + (value.compareTo(DTrnEmissionConsts.UUID_ANNUL_UUID_UNEXIST) != 0 ? "" : " (UUID does not exist!)") + "]");
                                }
                                else {
                                    xml = cancelaCFDResult.getAcuse().getValue();
                                }
                            }
                            else {
                                // Cancel verification response:
                                
                                if (receiptResult != null) {
                                    if (receiptResult.getSuccess() == null) {
                                        xsr.delete(session); // delete request log
                                        throw new Exception("ReceiptResult msg: success = null: [" + (receiptResult.getError() == null ? "error null" : receiptResult.getError().getValue()) + "]");
                                    }
                                    else if (!receiptResult.getSuccess().getValue()) {
                                        xsr.delete(session); // delete request log
                                        throw new Exception("ReceiptResult msg: success.value = false [" + (receiptResult.getError() == null ? "error null" : receiptResult.getError().getValue()) + "]");
                                    }
                                    else {
                                        xml = receiptResult.getReceipt().getValue();
                                        /*
                                        Cancellation acknowledgment comes wraped in another XML (SOAP response),
                                        so '<' and '>' must be represented with its correspondign character entity references.
                                        * /
                                        xml = xml.replace("&lt;", "<");
                                        xml = xml.replace("&gt;", ">");
                                    }
                                }
                            }
                            */
                        
                            // scamp code snippet to emulate an annulled CFDI:
                            cancelStatusCode = DCfdi40Consts.ESTATUS_CANCEL_SIN_ACEPT_CODE;
                            xmlAcuse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><tfd:TimbreFiscalDigital xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" FechaTimbrado=\"2018-08-16T16:00:02\" NoCertificadoSAT=\"00001000000405332712\" RfcProvCertif=\"FIN1203015JA\" SelloCFD=\"ONDe5/YJVqRfuohNNJA/UjxlR5SIwPlASe2/cKZaDHGQ7XKCOUefidDP3Szk9hHIE8hpJGoXjGqETQ/WKotYMcyOqzR+g0F5SGfhz34NZGVuffBLl4Co073g5ZeWGKiM6WXlim2njxxkhIqBTnf5BMcc7WqtyVfOGnwEZlXhx8kIbYKKWrSqEo72hldAZc8xrGkRikUUzp3aS6z5kDjfRcfIqSyBX1z7fOSjgYT9MXVezgEwKjwrhFUydrtz0Jqd5+KycPcHzedKJo6kbtnDmgKeLiZejGKobJ6VSlbXSYdiL+2Mt58WmUkG3JGCEzGXiBSO6ayz1Hmwjrr3rX84BA==\" SelloSAT=\"byIDPVs6qpW+D76RYX9RbZB4+inyp0QjYqzvX5Q0TObgWn9kcNKKsQ94C1OZrGon5qQx65WMlVjQsjSju6pf0Od6042c9S6emU1ANR3dSrcgtn0FjoNukj6lpgEt992hmf74D3wryVfrsc+NlCTuxFxpN0pO5Z2VADHie3GZRBzH9bH3ul8zO8hkihSqZNd1qtNQX3pW2KYnjaG6nQV0Obq441V1W483IUYxscsCrtDLrRyKvPBJQHNUuKAVyTKqzbJpD4u0tRudLIXtpSk9bj5f6ctYbl5ebZuMzOA3p7Nly/qkRoH2onLcZnx45dxJxid8hukCEojbVtW6jBps4w==\" UUID=\"5E43EBB7-D01A-4D44-A372-4A979A4778D4\" Version=\"1.1\" xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd\"/>";  // 2018-08-17, Sergio Flores: scamp code snippet to emulate a cancelled CFDI
                        }
                        else {
                            // Production environment:
                            
                            // Check CFDI cancellable status:

                            boolean getAckCancellation = false;
                            boolean isDirectlyCancellable = false;
                            DTrnDfrUtilsHandler dfrUtilsHandler = new DTrnDfrUtilsHandler(session);
                            DTrnDfrUtilsHandler.CfdiAckQuery cfdiAckQuery = dfrUtilsHandler.getCfdiSatStatus(dfr);

                            switch (cfdiAckQuery.CfdiStatus) {
                                case DCfdi40Consts.CFDI_ESTATUS_CAN:
                                    // CFDI is 'cancelled' before fiscal authority, but is still active in system:

                                    getAckCancellation = true;
                                    break;

                                case DCfdi40Consts.CFDI_ESTATUS_VIG:
                                    // CFDI is 'active' before fiscal authority:

                                    if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_VER) {
                                        // cancel verification request:
                                        
                                        if (cfdiAckQuery.CancelStatus.isEmpty()) {
                                            cancelStatusCode = "";
                                        }
                                        else {
                                            switch (cfdiAckQuery.CancelStatus) {
                                                case DCfdi40Consts.ESTATUS_CANCEL_PROC: // cancellation in process
                                                    cancelStatusCode = DCfdi40Consts.ESTATUS_CANCEL_PROC_CODE;
                                                    break;
                                                case DCfdi40Consts.ESTATUS_CANCEL_RECH: // cancellation was rejected
                                                    cancelStatusCode = DCfdi40Consts.ESTATUS_CANCEL_RECH_CODE;
                                                    break;
                                                case DCfdi40Consts.ESTATUS_CANCEL_NINGUNO: // cancellation in pending buffer
                                                    cancelStatusCode = DCfdi40Consts.ESTATUS_CANCEL_PEND_BUFF_CODE;
                                                    break;
                                                default:
                                                    cancelStatusCode = DTrnEmissionConsts.UNKNOWN;
                                            }
                                        }
                                        
                                        dfr.saveField(session.getStatement(), dfr.getPrimaryKey(), DDbDfr.FIELD_CAN_ST, cancelStatusCode);
                                        
                                        xsr.delete(session); // delete request log
                                        throw new Exception("El CFDI está vigente." + (cancelStatusCode.isEmpty() ? "" : " Estatus de cancelación: [" + cancelStatusCode + "]"));
                                    }

                                    // check cancellable status:
                                    switch (cfdiAckQuery.CancellableInfo) {
                                        case DCfdi40Consts.CANCELABLE_SIN_ACEPT:
                                            isDirectlyCancellable = true;
                                            // CFDI is cancellable, go through...
                                            break;

                                        case DCfdi40Consts.CANCELABLE_CON_ACEPT:
                                            // CFDI is cancellable, go through...
                                            break;

                                        case DCfdi40Consts.CANCELABLE_NO:
                                            // CFDI is not cancellable, but
                                            // evaluate if CFDI is directly cancellable for having only one unique relation to its replacement,
                                            isDirectlyCancellable = cfdiAckQuery.CfdiRelatedList.size() == 1 && cfdiAckQuery.CfdiRelatedList.get(0).Uuid.equals(cancelParams.AnnulRelatedUuid);
                                            // anyway, go through, the authority will resolve it...
                                            break;
                                            
                                        default:
                                            xsr.delete(session); // delete request log
                                            throw new Exception("Estatus de cancelación desconocido: [" + cfdiAckQuery.CancellableInfo + "]");
                                    }

                                    // check cancellation status:
                                    if (!cfdiAckQuery.CancelStatus.isEmpty()) { // since december 2020, cancel status is empty for all cancellable CFDI types!, awkward!
                                        switch (cfdiAckQuery.CancelStatus) {
                                            case DCfdi40Consts.ESTATUS_CANCEL_PROC: // CFDI cancellation in process
                                                dfr.saveField(session.getStatement(), dfr.getPrimaryKey(), DDbDfr.FIELD_CAN_ST, DCfdi40Consts.ESTATUS_CANCEL_PROC_CODE);

                                                //xsr.delete(session); preserve request log, DO NOT delete it!
                                                throw new Exception("La solicitud de cancelación del CFDI está pendiente de ser aceptada o rechazada por el receptor.");

                                            case DCfdi40Consts.ESTATUS_CANCEL_RECH: // CFDI cancellation was rejected by receptor
                                                if (!cancelParams.RetryCancel) {
                                                    dfr.saveField(session.getStatement(), dfr.getPrimaryKey(), DDbDfr.FIELD_CAN_ST, DCfdi40Consts.ESTATUS_CANCEL_RECH_CODE);

                                                    xsr.delete(session); // delete request log
                                                    throw new Exception("La solicitud de cancelación del CFDI fue rechazada por el receptor.");
                                                }
                                                // previous cancellation request was rejected, so try again...
                                                break;

                                            case DCfdi40Consts.ESTATUS_CANCEL_NINGUNO:
                                                // CFD about to be cancelled for the first time or maybe a cancellation is still in process (in pending buffer), go throuth...
                                                break;

                                            case DCfdi40Consts.ESTATUS_CANCEL_SIN_ACEPT:
                                            case DCfdi40Consts.ESTATUS_CANCEL_CON_ACEPT:
                                            case DCfdi40Consts.ESTATUS_CANCEL_PLAZO_VENC:
                                            case DCfdi40Consts.ESTATUS_CANCEL_PLAZO_VENC_ALT:
                                                xsr.delete(session); // delete request log
                                                throw new Exception("El estatus de cancelación del CFDI es inconsistente: [" + cfdiAckQuery.CancelStatus + "]");

                                            default:
                                                xsr.delete(session); // delete request log
                                                throw new Exception("El estatus de cancelación del CFDI es desconocido: [" + cfdiAckQuery.CancelStatus + "]");
                                        }
                                    }
                                    break;

                                case DCfdi40Consts.CFDI_ESTATUS_NO_ENC:
                                    // CFDI was 'not found' before fiscal authority:
                                    dfr.saveField(session.getStatement(), dfr.getPrimaryKey(), DDbDfr.FIELD_CAN_ST, "!!!");

                                    xsr.delete(session); // delete request log
                                    throw new Exception("El CFDI no fue encontrado ante el SAT.");
                                    
                                default:
                                    xsr.delete(session); // delete request log
                                    throw new Exception("Estatus de CFDI desconocido: '" + cfdiAckQuery.CfdiStatus + "'");
                            }

                            // Prepare web-service cancel request:
                            
                            com.finkok.facturacion.cancel.CancelSOAP cancelSoap = new com.finkok.facturacion.cancel.CancelSOAP();
                            com.finkok.facturacion.cancel.Application cancelApp = cancelSoap.getApplication();
                            
                            if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_VER || getAckCancellation) {
                                // verifying cancellation:
                                
                                views.core.soap.services.apps.ReceiptResult receiptResult = cancelApp.getReceipt(edsName, edsPswd, fiscalIdIssuer, dfr.getUuid(), "C");
                                
                                if (receiptResult == null || receiptResult.getSuccess() == null) {
                                    xsr.delete(session); // delete request log
                                    throw new Exception("Error al cancelar el CFDI con UUID '" + dfr.getUuid() + "'."
                                            + "\nError: [" + (receiptResult == null ? "" : receiptResult.getError().getValue()) + "]");
                                }
                                else {
                                    if (!receiptResult.getSuccess().getValue()) {
                                        xsr.delete(session); // delete request log
                                        throw new Exception("La cancelación del CFDI con UUID '" + dfr.getUuid() + "' no fue exitosa."
                                                + "\nRespuesta: [" + receiptResult.getError().getValue() + "]");
                                    }
                                    else {
                                        switch (cfdiAckQuery.CancelStatus) {
                                            case DCfdi40Consts.ESTATUS_CANCEL_SIN_ACEPT:
                                                cancelStatusCode = DCfdi40Consts.ESTATUS_CANCEL_SIN_ACEPT_CODE;
                                                break;
                                            case DCfdi40Consts.ESTATUS_CANCEL_CON_ACEPT:
                                                cancelStatusCode = DCfdi40Consts.ESTATUS_CANCEL_CON_ACEPT_CODE;
                                                break;
                                            case DCfdi40Consts.ESTATUS_CANCEL_PLAZO_VENC:
                                            case DCfdi40Consts.ESTATUS_CANCEL_PLAZO_VENC_ALT:
                                                cancelStatusCode = DCfdi40Consts.ESTATUS_CANCEL_PLAZO_VENC_CODE;
                                                break;
                                            default:
                                                cancelStatusCode = DTrnEmissionConsts.UNKNOWN;
                                        }
                                        
                                        /*
                                        Cancellation aknowledgment comes wraped in another XML (SOAP response),
                                        so '<' and '>' must be represented with its correspondign character entity references.
                                        */
                                        xmlAcuse = receiptResult.getReceipt().getValue();
                                        xmlAcuse = xmlAcuse.replace("&lt;", "<");
                                        xmlAcuse = xmlAcuse.replace("&gt;", ">");
                                    }
                                }
                            }
                            else {
                                // cancellation request:
                                
                                views.core.soap.services.apps.ObjectFactory objectFactory = new views.core.soap.services.apps.ObjectFactory();
                                views.core.soap.services.apps.UUID uuid = objectFactory.createUUID();
                                uuid.setUUID(dfr.getUuid());
                                uuid.setMotivo(cancelParams.AnnulReasonCode);
                                uuid.setFolioSustitucion(cancelParams.AnnulRelatedUuid);

                                views.core.soap.services.apps.UUIDArray uuidArray = new views.core.soap.services.apps.UUIDArray();
                                uuidArray.getUUID().add(uuid);

                                views.core.soap.services.apps.CancelaCFDResult cancelaCFDResult = null;

                                if (dfr.getFkXmlSignatureProviderId() == xmlSignatureProviderId) {
                                    // Cancel own signed document:
                                    cancelaCFDResult = cancelApp.cancel(uuidArray, edsName, edsPswd, fiscalIdIssuer, certificate.getXtaCertificatePemKeyPublic_n(), certificate.getXtaCertificatePemKeyPrivate_n(), true);
                                }
                                else {
                                    // Cancel third party signed document:
                                    cancelaCFDResult = cancelApp.outCancel(Base64.encode(dfr.getDocXml().getBytes()), edsName, edsPswd, fiscalIdIssuer, certificate.getXtaCertificatePemKeyPublic_n(), certificate.getXtaCertificatePemKeyPrivate_n(), true);
                                }
                                
                                JAXBElement<views.core.soap.services.apps.FolioArray> elementFolios = cancelaCFDResult.getFolios();
                                
                                if (elementFolios == null) {
                                    xsr.delete(session); // delete request log
                                    throw new Exception("Error al cancelar el CFDI: No se recibieron folios cancelados."
                                            + "\nRespuesta PAC: [" + cancelaCFDResult.getCodEstatus().getValue() + "]");
                                }
                                else if (elementFolios.getValue().getFolio().isEmpty()) {
                                    xsr.delete(session); // delete request log
                                    throw new Exception("Error al cancelar el CFDI: La lista de folios cancelados está vacía."
                                            + "\nRespuesta PAC: [" + cancelaCFDResult.getCodEstatus().getValue() + "]");
                                }
                                else {
                                    String estatusUuid = elementFolios.getValue().getFolio().get(0).getEstatusUUID().getValue();
                                    
                                    switch (estatusUuid) {
                                        case DTrnEmissionConsts.UUID_ANNUL:
                                        case DTrnEmissionConsts.UUID_ANNUL_PREV:
                                            // CFDI is already cancelled, or a cancellation request is in process (in pending buffer):

                                            String estatusCancelacion = elementFolios.getValue().getFolio().get(0).getEstatusCancelacion().getValue();
                                            
                                            if (estatusCancelacion.equalsIgnoreCase(DCfdi40Consts.ESTATUS_CANCEL_RECH)) {
                                                // CFDI cancellation rejected by receptor:
                                                
                                                dfr.saveField(session.getStatement(), dfr.getPrimaryKey(), DDbDfr.FIELD_CAN_ST, DCfdi40Consts.ESTATUS_CANCEL_RECH_CODE);

                                                xsr.delete(session); // delete request log
                                                throw new Exception("La solicitud de cancelación del CFDI fue rechazada por el receptor.");
                                            }
                                            else if (estatusCancelacion.equalsIgnoreCase(DCfdi40Consts.ESTATUS_CANCEL_PROC) ||
                                                    (estatusCancelacion.equalsIgnoreCase(DCfdi40Consts.RESPONSE_CANCEL) && !isDirectlyCancellable)) { // unexpected message in a succesful cancellation, treated as if cancellation is in process
                                                // CFDI cancellation in process:

                                                dfr.saveField(session.getStatement(), dfr.getPrimaryKey(), DDbDfr.FIELD_CAN_ST, DCfdi40Consts.ESTATUS_CANCEL_PROC_CODE);

                                                // do not delete XSR, preserve it!
                                                throw new Exception("La solicitud de cancelación del CFDI fue enviada al receptor.");
                                            }
                                            else if (estatusCancelacion.equalsIgnoreCase(DCfdi40Consts.ESTATUS_CANCEL_SIN_ACEPT) ||
                                                estatusCancelacion.equalsIgnoreCase(DCfdi40Consts.ESTATUS_CANCEL_CON_ACEPT) ||
                                                estatusCancelacion.equalsIgnoreCase(DCfdi40Consts.ESTATUS_CANCEL_PLAZO_VENC) ||
                                                estatusCancelacion.equalsIgnoreCase(DCfdi40Consts.ESTATUS_CANCEL_PLAZO_VENC_ALT) ||
                                                (estatusCancelacion.equalsIgnoreCase(DCfdi40Consts.RESPONSE_CANCEL) && isDirectlyCancellable)) { // unexpected message in a succesful cancellation, treated as if cancellation is done
                                                // CFDI canceled!:

                                                if (estatusCancelacion.equalsIgnoreCase(DCfdi40Consts.RESPONSE_CANCEL) && isDirectlyCancellable) {
                                                    cancelStatusCode = DCfdi40Consts.ESTATUS_CANCEL_SIN_ACEPT_CODE;
                                                }
                                                else {
                                                    switch (estatusCancelacion) {
                                                        case DCfdi40Consts.ESTATUS_CANCEL_SIN_ACEPT:
                                                            cancelStatusCode = DCfdi40Consts.ESTATUS_CANCEL_SIN_ACEPT_CODE;
                                                            break;
                                                        case DCfdi40Consts.ESTATUS_CANCEL_CON_ACEPT:
                                                            cancelStatusCode = DCfdi40Consts.ESTATUS_CANCEL_CON_ACEPT_CODE;
                                                            break;
                                                        case DCfdi40Consts.ESTATUS_CANCEL_PLAZO_VENC:
                                                        case DCfdi40Consts.ESTATUS_CANCEL_PLAZO_VENC_ALT:
                                                            cancelStatusCode = DCfdi40Consts.ESTATUS_CANCEL_PLAZO_VENC_CODE;
                                                            break;
                                                        default:
                                                            cancelStatusCode = DTrnEmissionConsts.UNKNOWN;
                                                    }
                                                }
                                                
                                                /*
                                                Cancellation cknowledgment comes wraped in another XML (SOAP response),
                                                so '<' and '>' must be represented with its correspondign character entity references.
                                                */
                                                xmlAcuse = cancelaCFDResult.getAcuse().getValue();
                                                xmlAcuse = xmlAcuse.replace("&lt;", "<");
                                                xmlAcuse = xmlAcuse.replace("&gt;", ">");
                                                break;
                                            }
                                            else if (estatusCancelacion.equalsIgnoreCase(DCfdi40Consts.ESTATUS_CANCEL_NINGUNO)) {
                                                // CFDI cancellation in pending buffer:
                                            
                                                dfr.saveField(session.getStatement(), dfr.getPrimaryKey(), DDbDfr.FIELD_CAN_ST, DCfdi40Consts.ESTATUS_CANCEL_PEND_BUFF_CODE);

                                                // do not delete XSR, preserve it!
                                                throw new Exception("El CFDI ya está en el controlador de espera (pending buffer), en proceso de ser cancelado.");
                                            }
                                            else {
                                                // unexpected cancellation code status:
                                                
                                                dfr.saveField(session.getStatement(), dfr.getPrimaryKey(), DDbDfr.FIELD_CAN_ST, "!!!");

                                                xsr.delete(session); // delete request log
                                                throw new Exception("El estatus de cancelación del CFDI es desconocido: [" + estatusCancelacion + "].");
                                            }

                                        default:
                                            // unexpected response code:

                                            xsr.delete(session); // delete request log
                                            throw new Exception("Error al cancelar el CFDI: El código de respuesta es desconocido."
                                                    + "\nRespuesta PAC: [" + estatusUuid + "]");
                                    }
                                }
                            }
                        }
                        break;

                    case DModSysConsts.CS_XSP_TST:  // testing
                        // scamp code snippet to emulate an annulled CFDI:
                        cancelStatusCode = DCfdi40Consts.ESTATUS_CANCEL_SIN_ACEPT_CODE;
                        xmlAcuse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><tfd:TimbreFiscalDigital xmlns:tfd=\"http://www.sat.gob.mx/TimbreFiscalDigital\" FechaTimbrado=\"2018-08-16T16:00:02\" NoCertificadoSAT=\"00001000000405332712\" RfcProvCertif=\"FIN1203015JA\" SelloCFD=\"ONDe5/YJVqRfuohNNJA/UjxlR5SIwPlASe2/cKZaDHGQ7XKCOUefidDP3Szk9hHIE8hpJGoXjGqETQ/WKotYMcyOqzR+g0F5SGfhz34NZGVuffBLl4Co073g5ZeWGKiM6WXlim2njxxkhIqBTnf5BMcc7WqtyVfOGnwEZlXhx8kIbYKKWrSqEo72hldAZc8xrGkRikUUzp3aS6z5kDjfRcfIqSyBX1z7fOSjgYT9MXVezgEwKjwrhFUydrtz0Jqd5+KycPcHzedKJo6kbtnDmgKeLiZejGKobJ6VSlbXSYdiL+2Mt58WmUkG3JGCEzGXiBSO6ayz1Hmwjrr3rX84BA==\" SelloSAT=\"byIDPVs6qpW+D76RYX9RbZB4+inyp0QjYqzvX5Q0TObgWn9kcNKKsQ94C1OZrGon5qQx65WMlVjQsjSju6pf0Od6042c9S6emU1ANR3dSrcgtn0FjoNukj6lpgEt992hmf74D3wryVfrsc+NlCTuxFxpN0pO5Z2VADHie3GZRBzH9bH3ul8zO8hkihSqZNd1qtNQX3pW2KYnjaG6nQV0Obq441V1W483IUYxscsCrtDLrRyKvPBJQHNUuKAVyTKqzbJpD4u0tRudLIXtpSk9bj5f6ctYbl5ebZuMzOA3p7Nly/qkRoH2onLcZnx45dxJxid8hukCEojbVtW6jBps4w==\" UUID=\"5E43EBB7-D01A-4D44-A372-4A979A4778D4\" Version=\"1.1\" xsi:schemaLocation=\"http://www.sat.gob.mx/TimbreFiscalDigital http://www.sat.gob.mx/sitio_internet/cfd/TimbreFiscalDigital/TimbreFiscalDigitalv11.xsd\"/>";  // 2018-08-17, Sergio Flores: scamp code snippet to emulate a cancelled CFDI
                        break;

                    default:
                        throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN + " " + DTrnEmissionConsts.PAC);
                }
                
                /*
                If this point is reached, it means that CFDI needs to be cancelled!
                */

                // Preserve aknowledgment of cancellation on XML format into DFR:

                dfr.setCancelStatus(cancelStatusCode);
                dfr.setCancelXml(xmlAcuse);
                dfr.setFkXmlStatusId(DModSysConsts.TS_XML_ST_ANN);
                dfr.setAuxDfrJustAnnulled(true);

                switch (dfr.getFkXmlSubtypeId()) {
                    case DModSysConsts.TS_XML_STP_FAC:
                        ((DDbDps) doc).updateDfr(session, dfr);
                        break;
                    case DModSysConsts.TS_XML_STP_CRP:
                        dfr.save(session);
                        break;
                    case DModSysConsts.TS_XML_STP_CCP:
                        ((DDbBol) doc).updateDfr(session, dfr);
                        break;
                    default:
                        // nothing
                }
            }
            
            switch (dfr.getFkXmlSubtypeId()) {
                case DModSysConsts.TS_XML_STP_FAC:
                    if (((DDbDps) doc).getFkDpsStatusId() != DModSysConsts.TS_DPS_ST_ANN) {
                        ((DDbDps) doc).disable(session);
                    }
                    break;
                case DModSysConsts.TS_XML_STP_CRP:
                    if (((DDbDfr) doc).getFkXmlStatusId() != DModSysConsts.TS_XML_ST_ANN) {
                        ((DDbDfr) doc).disable(session);
                    }
                    break;
                case DModSysConsts.TS_XML_STP_CCP:
                    if (((DDbBol) doc).getFkBolStatusId() != DModSysConsts.TS_DPS_ST_ANN) {
                        ((DDbBol) doc).disable(session);
                    }
                    break;
                default:
                    // nothing
            }
            
            xsr.setRequestStatus(DModSysConsts.TX_XMS_REQ_ST_PRC);
            xsr.save(session);
        }
        
        if (xsr.getRequestStatus() < DModSysConsts.TX_XMS_REQ_ST_CMP) {
            // Consume stamp:

            if (cancelParams.AnnulAction == DTrnEmissionConsts.ACTION_ANNUL_CANCEL) {
                DTrnEmissionUtils.consumeStamp(session, xsr.getFkXmlSignatureProviderId(), signatureCompanyBranchKey, DModSysConsts.TS_XSM_TP_OUT_ANN, dfr.getPkDfrId());
            }
            xsr.setRequestStatus(DModSysConsts.TX_XMS_REQ_ST_CMP);
            xsr.save(session);
        }

        if (xsr.getRequestStatus() < DModSysConsts.TX_XMS_REQ_ST_FIN) {
            // Print DFR:

            doc.printDfr(session);
            xsr.setRequestStatus(DModSysConsts.TX_XMS_REQ_ST_FIN);
            xsr.save(session);
        }
    }
    
    /**
     * Composes text of payment terms for CFDI.
     * @param paymentType Constants defined in DModSysConsts.FS_PAY_TP_...
     * @param creditDays Credit days.
     * @return 
     */
    public static String composeCfdiPaymentTerms(final int paymentType, final int creditDays) {
        String paymentTerms = "";
        
        switch (paymentType) {
            case DModSysConsts.FS_PAY_TP_CSH:
                paymentTerms = DFinConsts.TXT_PAY_TP_CSH;
                break;
            case DModSysConsts.FS_PAY_TP_CDT:
                paymentTerms = DFinConsts.TXT_PAY_TP_CDT + " " + creditDays + " " + (creditDays == 1 ? DLibTimeConsts.TXT_DAY : DLibTimeConsts.TXT_DAYS).toUpperCase();
                break;
            default:
        }
        
        return paymentTerms;
    }
}
