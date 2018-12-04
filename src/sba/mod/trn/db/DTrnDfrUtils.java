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
import cfd.DSubelementAddenda;
import cfd.ext.addenda1.DElementAdicionalConcepto;
import cfd.ext.addenda1.DElementNota;
import cfd.ext.addenda1.DElementNotas;
import cfd.ext.continental.DElementAddendaContinentalTire;
import cfd.ext.continental.DElementPedido;
import cfd.ext.continental.DElementPo;
import cfd.ext.continental.DElementPosicion;
import cfd.ext.continental.DElementPosicionesPo;
import cfd.ext.continental.DElementTipoProv;
import cfd.ver32.DCfdiVer32Consts;
import cfd.ver33.DCfdi33Catalogs;
import cfd.ver33.DCfdi33Consts;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
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
import sba.mod.cfg.db.DDbLock;
import sba.mod.cfg.db.DLockConsts;
import sba.mod.cfg.db.DLockUtils;
import sba.mod.fin.db.DFinConsts;

/**
 * Utilities related to CFD generation and PAC transaction control for CFD signing and cancelling.
 * 
 * @author Sergio Flores
 */
public abstract class DTrnDfrUtils {
    
    /**
     * Extracts existing digital signature attributes from CFD's XML.
     * @param dps
     * @return
     * @throws Exception 
     */
    @Deprecated
    private static cfd.ver32.DSelloDigital extractSello32(final DDbDps dps) throws Exception {
        Document doc = null;
        Node node = null;
        NamedNodeMap namedNodeMapChild = null;
        cfd.ver32.DSelloDigital selloDigital = null;
        
        if (dps.getChildDfr() != null) {
            selloDigital = new cfd.ver32.DSelloDigital();

            doc = DXmlUtils.parseDocument(dps.getChildDfr().getDocXml());

            node = DXmlUtils.extractElements(doc, "cfdi:Comprobante").item(0);
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
        Document doc = null;
        Node node = null;
        NamedNodeMap namedNodeMapChild = null;
        cfd.ver33.DSelloDigital selloDigital = null;
        
        if (dps.getChildDfr() != null) {
            selloDigital = new cfd.ver33.DSelloDigital();

            doc = DXmlUtils.parseDocument(dps.getChildDfr().getDocXml());

            node = DXmlUtils.extractElements(doc, "cfdi:Comprobante").item(0);
            namedNodeMapChild = node.getAttributes();

            selloDigital.setSello(DXmlUtils.extractAttributeValue(namedNodeMapChild, "Sello", true));
            selloDigital.setNoCertificado(DXmlUtils.extractAttributeValue(namedNodeMapChild, "NoCertificado", true));
            selloDigital.setCertificado(DXmlUtils.extractAttributeValue(namedNodeMapChild, "Certificado", true));
        }
        
        return selloDigital;
    }

    @Deprecated
    public static void configureCfdi32(final DGuiSession session, final cfd.ver32.DElementComprobante comprobante) {
        int decimalsQuantity = ((DDbConfigCompany) session.getConfigCompany()).getDecimalsQuantity();
        int decimalsPriceUnitary = ((DDbConfigCompany) session.getConfigCompany()).getDecimalsPriceUnitary();
        
        for (cfd.ver32.DElementConcepto concepto : comprobante.getEltConceptos().getEltHijosConcepto()) {
            concepto.getAttCantidad().setDecimals(decimalsQuantity);
            concepto.getAttValorUnitario().setDecimals(decimalsPriceUnitary);
        }
    }
    
    public static void configureCfdi33(final DGuiSession session, final cfd.ver33.DElementComprobante comprobante) {
        int decimalsQuantity = ((DDbConfigCompany) session.getConfigCompany()).getDecimalsQuantity();
        int decimals = ((DDbConfigCompany) session.getConfigCompany()).getDecimalsPriceUnitary();
        
        for (cfd.ver33.DElementConcepto concepto : comprobante.getEltConceptos().getEltConceptos()) {
            concepto.getAttCantidad().setDecimals(decimalsQuantity);
            concepto.getAttValorUnitario().setDecimals(decimals);
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
        ArrayList<DTrnImportDeclaration> aImportDeclarations = new ArrayList<DTrnImportDeclaration>();
        ArrayList<DElementAdicionalConcepto> aAdicionalConceptos = new ArrayList<DElementAdicionalConcepto>();
        ArrayList<DElementNota> aNotas = new ArrayList<DElementNota>();
        Set<Double> setKeyImptos = null;
        HashMap<Double, Double> hmImpto = null;
        HashMap<Double, Double> hmRetenidoIva = new HashMap<Double, Double>();
        HashMap<Double, Double> hmRetenidoIsr = new HashMap<Double, Double>();
        HashMap<Double, Double> hmTrasladadoIva = new HashMap<Double, Double>();
        DDbConfigBranch configBranch = null;
        DDbBizPartner bprEmisor = null;
        DDbBranch braEmisor = null;
        DDbBranchAddress braAddressEmisor = null;
        DDbBizPartner bprReceptor = null;
        DDbBizPartner bprReceptorName = null;
        DDbBranch braReceptor = null;
        DDbBranchAddress braAddressReceptor = null;
        String sNombreReceptor = "";
        
        // Check company branch emission configuration:
        
        configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, dps.getCompanyBranchKey());

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

        sNombreReceptor = bprReceptorName.getProperName();

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

        braAddressEmisor = bprEmisor.getChildBranches().get(0).getChildAddresses().get(0);

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
            braAddressEmisor = bprEmisor.getChildBranch(dps.getCompanyBranchKey()).getChildAddresses().get(0);

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

        asRegimenes = DLibUtils.textExplode(((DDbConfigCompany) session.getConfigCompany()).getFiscalIdentity(), ";");

        for (String regimen : asRegimenes) {
            cfd.ver32.DElementRegimenFiscal regimenFiscal = new cfd.ver32.DElementRegimenFiscal();
            regimenFiscal.getAttRegimen().setString(regimen);
            comprobante.getEltEmisor().getEltHijosRegimenFiscal().add(regimenFiscal);
        }

        comprobante.getEltReceptor().getAttRfc().setString(bprReceptor.getFiscalId());
        comprobante.getEltReceptor().getAttNombre().setString(sNombreReceptor);

        if(braReceptor.isAddressPrintable()) {
            braAddressReceptor = braReceptor.getChildAddresses().get(0);
        }
        else {
            braAddressReceptor = bprReceptor.getChildBranches().get(0).getChildAddresses().get(0);
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
                DElementAdicionalConcepto adicionalConcepto = new DElementAdicionalConcepto();

                concepto.getAttNoIdentificacion().setString(dpsRow.getCode());
                concepto.getAttUnidad().setString(dpsRow.getDbUnitCode());
                concepto.getAttCantidad().setDouble(dpsRow.getQuantity() == 0 ? 1 : dpsRow.getQuantity());
                concepto.getAttCantidad().setDecimals(((DDbConfigCompany) session.getConfigCompany()).getDecimalsQuantity());
                concepto.getAttDescripcion().setString(dpsRow.getName());
                concepto.getAttValorUnitario().setDecimals(((DDbConfigCompany) session.getConfigCompany()).getDecimalsPriceUnitary());
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
                        DElementNota nota = new DElementNota();
                        nota.getAttTexto().setString(note.getText());
                        aNotas.add(nota);
                    }
                }

                if (aNotas.size() > 0) {
                    DElementNotas notas = new DElementNotas();
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
    
    public static cfd.ver33.DElementComprobante createCfdi33(final DGuiSession session, final DDbDps dps, final DGuiEdsSignature signature) throws Exception {
        int decs = DLibUtils.getDecimalFormatAmount().getMaximumFractionDigits();
        int decsTax = 6;
        Date tDate = null;
        Date tUpdateTs = dps.getTsUserUpdate() != null ? dps.getTsUserUpdate() : new Date();
        int[] anDateDps = DLibTimeUtils.digestDate(dps.getDate());
        int[] anDateEdit = DLibTimeUtils.digestDate(tUpdateTs);
        GregorianCalendar oGregorianCalendar = null;
        DDbConfigBranch configBranch = null;
        DDbBizPartner bprEmisor = null;
        DDbBranch braEmisor = null;
        DDbBranchAddress braAddressEmisor = null;
        DDbBizPartner bprReceptor = null;
        DDbBizPartner bprReceptorName = null;
        DDbBranch braReceptor = null;
        
        // Check company branch emission configuration:
        
        configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, dps.getCompanyBranchKey());

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
        comprobante.getAttCondicionesDePago().setString(dps.getDfrPaymentTerms());
        
        comprobante.getAttSubTotal().setDouble(dps.getSubtotalProvCy_r());

        if (dps.getDiscountDocCy_r() > 0) {
            comprobante.getAttDescuento().setDouble(dps.getDiscountDocCy_r());
        }

        comprobante.getAttMoneda().setString(session.getSessionCustom().getCurrencyCode(dps.getCurrencyKey()));
        if (!session.getSessionCustom().isLocalCurrency(dps.getCurrencyKey())) {
            comprobante.getAttTipoCambio().setDouble(dps.getExchangeRate());
        }
        
        comprobante.getAttTotal().setDouble(dps.getTotalCy_r());

        comprobante.getAttTipoDeComprobante().setString(dps.isDpsDocument() ? DCfdi33Catalogs.CFD_TP_I : DCfdi33Catalogs.CFD_TP_E);
        comprobante.getAttMetodoPago().setString(dps.getDfrMethodOfPayment());
        
        braAddressEmisor = braEmisor.getChildAddresses().get(0);
        comprobante.getAttLugarExpedicion().setString(braAddressEmisor.getZipCode());
        
        comprobante.getAttConfirmacion().setString(dps.getDfrConfirmation());
        
        // element 'CfdiRelacionados':
        if (!dps.getDfrRelationType().isEmpty() && !dps.getDfrCfdiRelated().isEmpty()) {
            cfd.ver33.DElementCfdiRelacionados cfdiRelacionados = new cfd.ver33.DElementCfdiRelacionados();
            cfdiRelacionados.getAttTipoRelacion().setString(dps.getDfrRelationType());
            
            for (String uuid : dps.getDfrCfdiRelated()) {
                cfd.ver33.DElementCfdiRelacionado cfdiRelacionado = new cfd.ver33.DElementCfdiRelacionado();
                cfdiRelacionado.getAttUuid().setString(uuid);
                cfdiRelacionados.getEltCfdiRelacionados().add(cfdiRelacionado);
            }
            
            comprobante.setEltOpcCfdiRelacionados(cfdiRelacionados);
        }

        // element 'Emisor':
        comprobante.getEltEmisor().getAttRfc().setString(bprEmisor.getFiscalId());
        comprobante.getEltEmisor().getAttNombre().setString(bprEmisor.getProperName());
        comprobante.getEltEmisor().getAttRegimenFiscal().setString(dps.getDfrTaxRegime());

        // element 'Receptor':
        comprobante.getEltReceptor().getAttRfc().setString(bprReceptor.getFiscalId());
        comprobante.getEltReceptor().getAttNombre().setString(bprReceptorName.getProperName());
        //comprobante.getEltReceptor().getAttResidenciaFiscal().setString(""); // not supported yet!
        //comprobante.getEltReceptor().getAttNumRegIdTrib().setString(""); // not supported yet!
        comprobante.getEltReceptor().getAttUsoCFDI().setString(dps.getDfrCfdUsage());

        // element 'Conceptos':
        
        HashMap<Double, Double> mapTaxIva = new HashMap<>();   //tax rate, tax amount
        HashMap<String, Double> mapRetIva = new HashMap<>();   //retention code, retained amount
        HashMap<String, Double> mapRetIsr = new HashMap<>();   //retention code, retained amount
        
        for (DDbDpsRow dpsRow : dps.getChildRows()) {
            if (!dpsRow.isDeleted()) {
                // "Concepto" node:

                cfd.ver33.DElementConcepto concepto = new cfd.ver33.DElementConcepto();

                concepto.getAttClaveProdServ().setString(dpsRow.getDfrItemKey());
                concepto.getAttNoIdentificacion().setString(dpsRow.getCode());
                concepto.getAttCantidad().setDouble(dpsRow.getQuantity() == 0 ? 1 : dpsRow.getQuantity());
                concepto.getAttCantidad().setDecimals(((DDbConfigCompany) session.getConfigCompany()).getDecimalsQuantity());
                concepto.getAttClaveUnidad().setString(dpsRow.getDfrUnitKey());
                concepto.getAttUnidad().setString(dpsRow.getDbUnitCode());
                concepto.getAttDescripcion().setString(dpsRow.getName());
                concepto.getAttValorUnitario().setDecimals(((DDbConfigCompany) session.getConfigCompany()).getDecimalsPriceUnitary());
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
                                taxCode = DCfdi33Catalogs.IMP_IVA;
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
                            impuestoTraslado.getAttTipoFactor().setString(DCfdi33Catalogs.FAC_TP_TASA);
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
                                retCode = DCfdi33Catalogs.IMP_IVA;
                                mapRet = mapRetIva;
                                break;
                            case 3: // ISR
                                retCode = DCfdi33Catalogs.IMP_ISR;
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
                            impuestoRetencion.getAttTipoFactor().setString(DCfdi33Catalogs.FAC_TP_TASA);
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
                impuestoTraslado.getAttImpuesto().setString(DCfdi33Catalogs.IMP_IVA);
                impuestoTraslado.getAttTipoFactor().setString(DCfdi33Catalogs.FAC_TP_TASA);
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
                impuestoRetencion.getAttImpuesto().setString(DCfdi33Catalogs.IMP_IVA);
                impuestoRetencion.getAttImporte().setDouble(mapRetIva.get(retCode));
                impuestosRetenciones.getEltImpuestoRetenciones().add(impuestoRetencion);
                
                totalRetenciones = DLibUtils.round(totalRetenciones + mapRetIva.get(retCode), decs);
            }
            
            for (String retCode : mapRetIsr.keySet()) {
                cfd.ver33.DElementImpuestoRetencion impuestoRetencion = new cfd.ver33.DElementImpuestoRetencion();
                impuestoRetencion.getAttImpuesto().setString(DCfdi33Catalogs.IMP_ISR);
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
            timbreFiscalDigital.getAttRfcProvCertif().setString(timbreFiscal_n.getRfcProdCertif());
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
    
    /**
     * Extracts DFR addenda.
     * @param xmlAddenda
     * @param typeXmlAddenda
     * @return
     * @throws Exception 
     */
    public static DSubelementAddenda extractExtAddenda(final String xmlAddenda, final int typeXmlAddenda) throws Exception {
        Document doc = null;
        Node node = null;
        Node nodeChild = null;
        NamedNodeMap namedNodeMapChild = null;
        Vector<Node> nodeChildren = null;
        DElementAddendaContinentalTire addendaContinentalTire = null;
        DSubelementAddenda extAddenda = null;
        
        switch (typeXmlAddenda) {
            case DModSysConsts.TS_XML_ADD_TP_CON:
                DElementPosicion eltPosicion = null;

                addendaContinentalTire = new DElementAddendaContinentalTire();

                if (!xmlAddenda.isEmpty()) {
                    doc = DXmlUtils.parseDocument(DCfdConsts.XML_HEADER + xmlAddenda);

                    node = DXmlUtils.extractElements(doc, DElementAddendaContinentalTire.NAME).item(0);

                    nodeChild = DXmlUtils.extractChildElements(node, DElementPo.NAME).get(0);
                    addendaContinentalTire.getEltPo().setValue(nodeChild.getTextContent());

                    nodeChild = DXmlUtils.extractChildElements(node, DElementPedido.NAME).get(0);
                    addendaContinentalTire.getEltPedido().setValue(nodeChild.getTextContent());

                    nodeChild = DXmlUtils.extractChildElements(node, DElementTipoProv.NAME).get(0);
                    addendaContinentalTire.getEltTipoProv().setValue(nodeChild.getTextContent());

                    nodeChild = DXmlUtils.extractChildElements(node, DElementPosicionesPo.NAME).get(0);
                    nodeChildren = DXmlUtils.extractChildElements(nodeChild, DElementPosicion.NAME);

                    for (Node child : nodeChildren) {
                        namedNodeMapChild = child.getAttributes();
                        eltPosicion = new DElementPosicion();
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
    public static DSubelementAddenda extractExtAddenda(final DDbDps dps, final int typeXmlAddenda) throws Exception {
        DSubelementAddenda extAddenda = null;
        
        if (dps.getChildDfr() != null && dps.getChildDfr().getFkXmlAddendaTypeId() == typeXmlAddenda) {
            switch (typeXmlAddenda) {
                case DModSysConsts.TS_XML_ADD_TP_CON:
                    extAddenda = (DElementAddendaContinentalTire) extractExtAddenda(dps.getChildDfr().getDocXmlAddenda(), typeXmlAddenda);
                    break;
                    
                default:
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
        
        return extAddenda;
    }

    /**
     * Creates new Digital Fiscal Receipt for provided document and type.
     * @param session Current GUI user session.
     * @param dps Document.
     * @param xmlType CFD type.
     * @return
     * @throws Exception 
     */
    public static DDbDfr createDfr(final DGuiSession session, final DDbDps dps, final int xmlType) throws Exception {
        int xmlStatus = DLibConsts.UNDEFINED;
        String textToSign;
        String textSigned;
        DGuiEdsSignature signature;
        DDbBizPartner bizPartner = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, dps.getBizPartnerKey());
        DDbConfigBranch configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, dps.getCompanyBranchKey());
        cfd.DCfd cfd = new cfd.DCfd(configBranch.getEdsDirectory());
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
        dfr.setFkXmlSubtypeId(DModSysConsts.TS_XML_STP_CFDI_FAC); // invoice
        dfr.setFkXmlStatusId(xmlStatus);
        dfr.setFkXmlAddendaTypeId(bizPartner.getFkXmlAddendaTypeId());
        dfr.setFkXmlSignatureProviderId(DModSysConsts.CS_XSP_NA);
        dfr.setFkCertificateId(session.getEdsSignature(dps.getCompanyBranchKey()).getCertificateId());
        dfr.setFkOwnerBizPartnerId(dps.getFkOwnerBizPartnerId());
        dfr.setFkOwnerBranchId(dps.getFkOwnerBranchId());
        dfr.setFkBizPartnerId(dps.getFkBizPartnerBizPartnerId());
        dfr.setFkDpsId_n(dps.getPkDpsId());
        /*
        dfr.setFkBookkeepingYearId_n();
        dfr.setFkBookkeepingNumberId_n();
        dfr.setFkUserIssuedId();    // will be set as needed when saved!
        dfr.setFkUserAnnulledId();  // will be set as needed when saved!
        dfr.setFkUserInsertId();    // will be set as needed when saved!
        dfr.setFkUserUpdateId();    // will be set as needed when saved!
        */
        dfr.setAuxJustIssued(true);

        return dfr;
    }

    /**
     * Validates that CFDI's XML corresponds to DFR registry.
     * @param xml CFDI's XML.
     * @param dfr DFR registry.
     * @return true if is correct.
     */
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

    public static void signDfr(final DGuiSession session, final DDbDfr dfr, final DTrnDfr trnDfr, final int xmlSignatureProviderId, final int[] signatureCompanyBranchKey, final int requestSubtype) throws TransformerConfigurationException, TransformerException, Exception {
        String cfdi = "";
        String edsName = "";
        String edsPassword = "";
        DocumentBuilder docBuilder = null;
        Document doc = null;
        NodeList nodeList = null;
        Node node = null;
        Node nodeChildTimbreFiscal = null;
        NamedNodeMap namedNodeMapTimbreFiscal = null;
        cfd.ver3.DTimbreFiscal timbreFiscal = null;
        DDbConfigBranch configBranch = null;
        DDbXmlSignatureRequest xsr = null;
        DDbLock lock;
        
        // Lock document:
        
        int registryType;
        int registryId;
        int timeout;
        
        switch (dfr.getFkXmlSubtypeId()) {
            case DModSysConsts.TS_XML_STP_CFDI_FAC:
                registryType = ((DDbDps) trnDfr).getRegistryType();
                registryId = ((DDbDps) trnDfr).getPkDpsId();
                timeout = DDbDps.TIMEOUT;
                break;
                
            case DModSysConsts.TS_XML_STP_CFDI_CRP:
                registryType = dfr.getRegistryType();
                registryId = dfr.getPkDfrId();
                timeout = DDbDfr.TIMEOUT;
                break;
                
            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        lock = DLockUtils.createLock(session, registryType, registryId, timeout);
        
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
            
            edsName = configBranch.getEdsName();
            edsPassword = configBranch.getEdsPassword();
            
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
                                xsr.delete(session);    // delete request log
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
                                xsr.delete(session);    // delete request log
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
                                xsr.delete(session);    // delete request log
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
                            xsr.delete(session);    // delete request log
                            
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
                            xsr.delete(session);    // delete request log

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

            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = docBuilder.parse(new ByteArrayInputStream(cfdi.getBytes("UTF-8")));
            nodeList = doc.getElementsByTagName("cfdi:Complemento");

            if (nodeList == null) {
                throw new Exception("XML element 'cfdi:Complemento' not found!");
            }
            else {
                node = nodeList.item(0);
            }

            nodeList = node.getChildNodes();

            if (nodeList == null) {
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
            
            // Preserve signed XML into DFR:

            timbreFiscal = new cfd.ver3.DTimbreFiscal();
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
                    node = namedNodeMapTimbreFiscal.getNamedItem("Version");
                    timbreFiscal.setVersion(node.getNodeValue());

                    node = namedNodeMapTimbreFiscal.getNamedItem("UUID");
                    timbreFiscal.setUuid(node.getNodeValue());

                    node = namedNodeMapTimbreFiscal.getNamedItem("FechaTimbrado");
                    timbreFiscal.setFechaTimbrado(node.getNodeValue());

                    node = namedNodeMapTimbreFiscal.getNamedItem("RfcProvCertif");
                    timbreFiscal.setRfcProdCertif(node.getNodeValue());

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
            dfr.setAuxJustIssued(true);
            dfr.setAuxRewriteXmlOnSave(true);
            
            if (dfr.getFkXmlSubtypeId() == DModSysConsts.TS_XML_STP_CFDI_CRP) {
                dfr.setAuxLock(lock);
            }
            
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
            // Issue DFR:

            trnDfr.issueDfr(session);
            xsr.setRequestStatus(DModSysConsts.TX_XMS_REQ_ST_FIN);
            xsr.save(session);
        }
        
        DLockUtils.freeLock(session, lock, DLockConsts.LOCK_ST_FREED_UPDATE);
    }

    public static void cancelDfr(final DGuiSession session, final DDbDfr dfr, final DTrnDfr trnDfr, final int xmlSignatureProviderId, final int[] signatureCompanyBranchKey, final int requestSubtype, final int action) throws TransformerConfigurationException, TransformerException, Exception {
        String xml = "";
        String value = "";
        String fiscalId = "";
        String edsName = "";
        String edsPassword = "";
        String cancelStatus = "?";
        ArrayList<String> alUuids = null;
        DDbBizPartner company = null;
        DDbConfigBranch configBranch = null;
        DDbCertificate certificate = null;
        DDbXmlSignatureRequest xsr = null;
        
        // Lock document, if necessary:
        
        switch (dfr.getFkXmlSubtypeId()) {
            case DModSysConsts.TS_XML_STP_CFDI_FAC:
                ((DDbDps) trnDfr).assureLock(session);
                break;
                
            case DModSysConsts.TS_XML_STP_CFDI_CRP:
                ((DDbDfr) trnDfr).assureLock(session);
                break;
                
            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        // Create or obtain XML Signature Request for cancellation:

        switch (requestSubtype) {
            case DModSysConsts.TX_XMS_REQ_STP_REQ:  // log a cancel request
                xsr = new DDbXmlSignatureRequest();
                xsr.setPkDfrId(dfr.getPkDfrId());
                //xsr.setPkRequestId(0);
                xsr.setRequestType(DModSysConsts.TX_XMS_REQ_TP_CAN);
                xsr.setRequestStatus(DModSysConsts.TX_XMS_REQ_ST_STA);
                xsr.setDeleted(false);
                xsr.setSystem(true);
                xsr.setFkXmlSignatureProviderId(xmlSignatureProviderId);
                xsr.save(session);
                break;

            case DModSysConsts.TX_XMS_REQ_STP_VER:  // log a cancel verification
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
                break;
                
            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        // Request cancellation:

        if (xsr.getRequestStatus() < DModSysConsts.TX_XMS_REQ_ST_PRC) {
            if (action == DTrnEmissionConsts.ANNUL_CANCEL) {
                company = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, dfr.getCompanyKey());
                configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, dfr.getCompanyBranchKey());
                certificate = (DDbCertificate) session.readRegistry(DModConsts.CU_CER, new int[] { configBranch.getFkCertificateId_n() });
                
                fiscalId = company.getFiscalId();
                edsName = configBranch.getEdsName();
                edsPassword = configBranch.getEdsPassword();
                
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
                                    xsr.delete(session);    // delete request log
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
                                    xsr.delete(session);    // delete request log
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
                                    xsr.delete(session);    // delete request log
                                    throw new Exception("CancelaCFDResult msg: folios = null: [" + cancelaCFDResult.getCodEstatus().getValue() + "]");
                                }
                                else if (folios.getValue().getFolio().isEmpty()) {
                                    xsr.delete(session);    // delete request log
                                    throw new Exception("CancelaCFDResult msg: folios.folio is empty: [" + cancelaCFDResult.getCodEstatus().getValue() + "]");
                                }
                                else if (folios.getValue().getFolio().get(0).getEstatusUUID().getValue().compareTo(DTrnEmissionConsts.UUID_ANNUL) != 0) {
                                    value = folios.getValue().getFolio().get(0).getEstatusUUID().getValue();

                                    xsr.delete(session);    // delete request log
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
                                        xsr.delete(session);    // delete request log
                                        throw new Exception("ReceiptResult msg: success = null: [" + (receiptResult.getError() == null ? "error null" : receiptResult.getError().getValue()) + "]");
                                    }
                                    else if (!receiptResult.getSuccess().getValue()) {
                                        xsr.delete(session);    // delete request log
                                        throw new Exception("ReceiptResult msg: success.value = false [" + (receiptResult.getError() == null ? "error null" : receiptResult.getError().getValue()) + "]");
                                    }
                                    else {
                                        xml = receiptResult.getReceipt().getValue();
                                        /*
                                        Cancellation cknowledgment comes wraped in another XML (SOAP response),
                                        so '<' and '>' must be represented with its correspondign character entity references.
                                        */
                                        xml = xml.replace("&lt;", "<");
                                        xml = xml.replace("&gt;", ">");
                                    }
                                }
                            }
                        }
                        else {
                            // Production environment:
                            
                            // Check CFDI cancellable status:

                            boolean getAckCancellation = false;
                            DTrnDfrUtilsHandler dfrUtilsHandler = new DTrnDfrUtilsHandler(session);
                            DTrnDfrUtilsHandler.CfdiAckQuery ackQuery = dfrUtilsHandler.getCfdiSatStatus(dfr);

                            switch (ackQuery.CfdiStatus) {
                                case DCfdi33Consts.CFDI_ESTATUS_CAN:
                                    // CFDI is 'cancelled' before fiscal authority, but is still active in system:

                                    getAckCancellation = true;
                                    break;

                                case DCfdi33Consts.CFDI_ESTATUS_VIG:
                                    // CFDI is 'active' before fiscal authority:

                                    if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_VER) {
                                        // cancel verification request:
                                        dfr.saveField(session.getStatement(), dfr.getPrimaryKey(), DDbDfr.FIELD_CAN_ST, "");
                                        session.notifySuscriptors(dfr.getRegistryType());
                                        
                                        xsr.delete(session);    // delete request log
                                        throw new Exception("El CFDI está vigente.");
                                    }

                                    // check cancellable status:
                                    switch (ackQuery.CancellableInfo) {
                                        case DCfdi33Consts.CANCELABLE_SIN_ACEPT:
                                        case DCfdi33Consts.CANCELABLE_CON_ACEPT:
                                            // CFDI is cancellable, go through...
                                            break;

                                        case DCfdi33Consts.CANCELABLE_NO:
                                            // CFDI is not cancellable:
                                            xsr.delete(session);    // delete request log
                                            throw new Exception("El CFDI es no cancelable.\n " + ackQuery.composeCfdiRelated());
                                    }

                                    // check cancellation status:
                                    switch (ackQuery.CancelStatus) {
                                        case DCfdi33Consts.ESTATUS_CANCEL_PROC: // CFDI cancellation is in process
                                            dfr.saveField(session.getStatement(), dfr.getPrimaryKey(), DDbDfr.FIELD_CAN_ST, DCfdi33Consts.ESTATUS_CANCEL_PROC_CODE);
                                            session.notifySuscriptors(dfr.getRegistryType());
                                            
                                            // do not delete XSR, preserve it!
                                            throw new Exception("La solicitud de cancelación del CFDI está pendiente de ser aceptada o rechazada por el receptor.");

                                        case DCfdi33Consts.ESTATUS_CANCEL_RECH: // CFDI cancellation was rejected by receptor
                                            dfr.saveField(session.getStatement(), dfr.getPrimaryKey(), DDbDfr.FIELD_CAN_ST, DCfdi33Consts.ESTATUS_CANCEL_RECH_CODE);
                                            session.notifySuscriptors(dfr.getRegistryType());
                                            
                                            xsr.delete(session);    // delete request log
                                            throw new Exception("La solicitud de cancelación del CFDI fue rechazada por el receptor.");

                                        case DCfdi33Consts.ESTATUS_CANCEL_SIN_ACEPT:
                                        case DCfdi33Consts.ESTATUS_CANCEL_CON_ACEPT:
                                        case DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC:       // it is not clear wich expression is the good one
                                        case DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC_EXT:   // it is not clear wich expression is the good one
                                            xsr.delete(session);    // delete request log
                                            throw new Exception("El estatus de cancelación del CFDI es inconsistente.");

                                        case DCfdi33Consts.ESTATUS_CANCEL_NINGUNO:
                                            // CFD about to be cancelled for the first time or maybe a cancellation is still in process (in pending buffer)!
                                            break;

                                        default:
                                            xsr.delete(session);    // delete request log
                                            throw new Exception("El estatus de cancelación del CFDI es desconocido.");
                                    }
                                    break;

                                case DCfdi33Consts.CFDI_ESTATUS_NO_ENC:
                                    // CFDI was 'not found' before fiscal authority:
                                    dfr.saveField(session.getStatement(), dfr.getPrimaryKey(), DDbDfr.FIELD_CAN_ST, "");
                                    session.notifySuscriptors(dfr.getRegistryType());

                                    xsr.delete(session);    // delete request log
                                    throw new Exception("El CFDI no fue encontrado ante el SAT.");
                                    
                                default:
                            }

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

                            if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_REQ && !getAckCancellation) {
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

                            if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_REQ && !getAckCancellation) {
                                // Cancel request response:
                                
                                folios = cancelaCFDResult.getFolios();

                                if (folios == null) {
                                    xsr.delete(session);    // delete request log
                                    throw new Exception("CancelaCFDResult msg: folios = null: [" + cancelaCFDResult.getCodEstatus().getValue() + "]");
                                }
                                else if (folios.getValue().getFolio().isEmpty()) {
                                    xsr.delete(session);    // delete request log
                                    throw new Exception("CancelaCFDResult msg: folios.folio is empty: [" + cancelaCFDResult.getCodEstatus().getValue() + "]");
                                }
                                else {
                                    String estatusUuid = folios.getValue().getFolio().get(0).getEstatusUUID().getValue();
                                    switch (estatusUuid) {
                                        case DTrnEmissionConsts.UUID_ANNUL:
                                        case DTrnEmissionConsts.UUID_ANNUL_PREV:
                                            // CFDI is cancelled, or a cancellation request is in process:

                                            String estatusCancelacion = folios.getValue().getFolio().get(0).getEstatusCancelacion().getValue();
                                            switch (estatusCancelacion) {
                                                case DCfdi33Consts.ESTATUS_CANCEL_PROC:
                                                    // CFDI cancellation is in process:
                                                    dfr.saveField(session.getStatement(), dfr.getPrimaryKey(), DDbDfr.FIELD_CAN_ST, DCfdi33Consts.ESTATUS_CANCEL_PROC_CODE);
                                                    session.notifySuscriptors(dfr.getRegistryType());
                                                    
                                                    // do not delete XSR, preserve it!
                                                    throw new Exception("La solicitud de cancelación del CFDI ha sido enviada al receptor.");

                                                case DCfdi33Consts.ESTATUS_CANCEL_RECH:
                                                    // CFDI cancellation was rejected by receptor:
                                                    dfr.saveField(session.getStatement(), dfr.getPrimaryKey(), DDbDfr.FIELD_CAN_ST, DCfdi33Consts.ESTATUS_CANCEL_RECH_CODE);
                                                    session.notifySuscriptors(dfr.getRegistryType());
                                                    
                                                    xsr.delete(session);    // delete request log
                                                    throw new Exception("La solicitud de cancelación del CFDI ha sido rechazada por el receptor.");

                                                case DCfdi33Consts.ESTATUS_CANCEL_SIN_ACEPT:
                                                case DCfdi33Consts.ESTATUS_CANCEL_CON_ACEPT:
                                                case DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC:       // it is not clear wich expression is the good one
                                                case DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC_EXT:   // it is not clear wich expression is the good one
                                                    // CFDI was canceled!:
                                                    switch (estatusCancelacion) {
                                                        case DCfdi33Consts.ESTATUS_CANCEL_SIN_ACEPT:
                                                            cancelStatus = DCfdi33Consts.ESTATUS_CANCEL_SIN_ACEPT_CODE;
                                                            break;
                                                        case DCfdi33Consts.ESTATUS_CANCEL_CON_ACEPT:
                                                            cancelStatus = DCfdi33Consts.ESTATUS_CANCEL_CON_ACEPT_CODE;
                                                            break;
                                                        case DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC:       // it is not clear wich expression is the good one
                                                        case DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC_EXT:   // it is not clear wich expression is the good one
                                                            cancelStatus = DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC_CODE;
                                                            break;
                                                        default:
                                                    }

                                                    xml = cancelaCFDResult.getAcuse().getValue();
                                                    /*
                                                    Cancellation cknowledgment comes wraped in another XML (SOAP response),
                                                    so '<' and '>' must be represented with its correspondign character entity references.
                                                    */
                                                    xml = xml.replace("&lt;", "<");
                                                    xml = xml.replace("&gt;", ">");
                                                    break;

                                                case DCfdi33Consts.ESTATUS_CANCEL_NINGUNO:
                                                    // CFDI in pending buffer:
                                                    dfr.saveField(session.getStatement(), dfr.getPrimaryKey(), DDbDfr.FIELD_CAN_ST, DCfdi33Consts.ESTATUS_CANCEL_PEND_BUFF_CODE);
                                                    session.notifySuscriptors(dfr.getRegistryType());

                                                    // do not delete XSR, preserve it!
                                                    throw new Exception("El CFDI ya está en el controlador de espera (pending buffer), en proceso de ser cancelado.");

                                                default:
                                                    // unexpected cancellation code status:
                                                    dfr.saveField(session.getStatement(), dfr.getPrimaryKey(), DDbDfr.FIELD_CAN_ST, "?");
                                                    session.notifySuscriptors(dfr.getRegistryType());

                                                    xsr.delete(session);    // delete request log
                                                    throw new Exception("El estatus de cancelación del CFDI es desconocido: [" + estatusCancelacion + "].");
                                            }
                                            break;

                                        default:
                                            // unexpected response code:

                                            xsr.delete(session);    // delete request log
                                            throw new Exception("CancelaCFDResult msg: folios.folio.estatus different to 201 and 202: [" + folios.getValue().getFolio().get(0).getEstatusUUID().getValue() + "]");
                                    }
                                }
                            }
                            else {
                                // Cancel verification response:
                                
                                if (receiptResult != null) {
                                    if (receiptResult.getSuccess() == null) {
                                        xsr.delete(session);    // delete request log
                                        throw new Exception("ReceiptResult msg: success = null: [" + (receiptResult.getError() == null ? "error null" : receiptResult.getError().getValue()) + "]");
                                    }
                                    else if (!receiptResult.getSuccess().getValue()) {
                                        xsr.delete(session);    // delete request log
                                        throw new Exception("ReceiptResult msg: success.value = false [" + (receiptResult.getError() == null ? "error null" : receiptResult.getError().getValue()) + "]");
                                    }
                                    else {
                                        // CFDI was canceled!:
                                        switch (ackQuery.CancelStatus) {
                                            case DCfdi33Consts.ESTATUS_CANCEL_SIN_ACEPT:
                                                cancelStatus = DCfdi33Consts.ESTATUS_CANCEL_SIN_ACEPT_CODE;
                                                break;
                                            case DCfdi33Consts.ESTATUS_CANCEL_CON_ACEPT:
                                                cancelStatus = DCfdi33Consts.ESTATUS_CANCEL_CON_ACEPT_CODE;
                                                break;
                                            case DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC:       // it is not clear wich expression is the good one
                                            case DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC_EXT:   // it is not clear wich expression is the good one
                                                cancelStatus = DCfdi33Consts.ESTATUS_CANCEL_PLAZO_VENC_CODE;
                                                break;
                                            default:
                                        }
                                        
                                        xml = receiptResult.getReceipt().getValue();
                                        /*
                                        Cancellation cknowledgment comes wraped in another XML (SOAP response),
                                        so '<' and '>' must be represented with its correspondign character entity references.
                                        */
                                        xml = xml.replace("&lt;", "<");
                                        xml = xml.replace("&gt;", ">");
                                    }
                                }
                            }
                        }
                        break;

                    case DModSysConsts.CS_XSP_TST:  // testing
                        xml = "";   // insert by hand for testint purposes 'Cancellation Aknowledge' into this local variable 'xml'!
                        break;

                    default:
                        throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN + " " + DTrnEmissionConsts.PAC);
                }
                
                /*
                If this point is reached, it means that CFDI needs to be cancelled!
                */

                // Preserve aknowledgment of cancellation on XML format into DFR:

                dfr.setCancelStatus(cancelStatus);
                dfr.setCancelXml(xml);
                dfr.setFkXmlStatusId(DModSysConsts.TS_XML_ST_ANN);
                dfr.setAuxJustAnnulled(true);

                switch (dfr.getFkXmlSubtypeId()) {
                    case DModSysConsts.TS_XML_STP_CFDI_FAC:
                        ((DDbDps) trnDfr).updateDfr(session, dfr);
                        break;
                    case DModSysConsts.TS_XML_STP_CFDI_CRP:
                        dfr.setAuxComputeBookkeeping(true);
                        dfr.save(session);
                        break;
                    default:
                }
            }
            
            if (dfr.getFkXmlSubtypeId() == DModSysConsts.TS_XML_STP_CFDI_FAC) {
                if (((DDbDps) trnDfr).getFkDpsStatusId() != DModSysConsts.TS_DPS_ST_ANN) {
                    ((DDbDps) trnDfr).disable(session);
                }
            }
            
            xsr.setRequestStatus(DModSysConsts.TX_XMS_REQ_ST_PRC);
            xsr.save(session);
        }

        if (xsr.getRequestStatus() < DModSysConsts.TX_XMS_REQ_ST_CMP) {
            // Consume stamp:

            if (action == DTrnEmissionConsts.ANNUL_CANCEL) {
                DTrnEmissionUtils.consumeStamp(session, xsr.getFkXmlSignatureProviderId(), signatureCompanyBranchKey, DModSysConsts.TS_XSM_TP_OUT_ANN, dfr.getPkDfrId());
            }
            xsr.setRequestStatus(DModSysConsts.TX_XMS_REQ_ST_CMP);
            xsr.save(session);
        }

        if (xsr.getRequestStatus() < DModSysConsts.TX_XMS_REQ_ST_FIN) {
            // Issue DFR:

            dfr.issueDfr(session);
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
