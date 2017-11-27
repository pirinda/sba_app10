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
import cfd.DElementExtAddenda;
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

/**
 * Utilities related to CFD generation and PAC transaction control for CFD signing and cancelling.
 * 
 * @author Sergio Flores
 */
public abstract class DTrnEdsUtils {
    
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
    
    public static cfd.ver2.DElementComprobante createCfd(final DGuiSession session, final DDbDps dps) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static cfd.ver32.DElementComprobante createCfdi32(final DGuiSession session, final DDbDps dps, final cfd.ver32.DTimbreFiscal timbreFiscal_n) throws Exception {
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
        boolean includeAddenda1 = false;
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

        cfd.ver32.DElementComprobante comprobante = new cfd.ver32.DElementComprobante(timbreFiscal_n != null && includeAddenda1); // include Addenda1 only if explicitly defined and CFD is already signed

        comprobante.getAttSerie().setString(dps.getSeries());
        comprobante.getAttFolio().setString("" + dps.getNumber());
        comprobante.getAttFecha().setDatetime(tDate);
        
        if (dps.getChildEds() != null) {
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
        
        if (timbreFiscal_n != null) {
            // Create element Complemento:

            cfd.ver32.DElementComplemento complemento = new cfd.ver32.DElementComplemento();
            cfd.ver32.DElementTimbreFiscalDigital timbreFiscalDigital = new cfd.ver32.DElementTimbreFiscalDigital();

            //timbreFiscalDigital.getAttVersion().setString(timbreFiscal_n.getVersion());   // by default version is "1.0"
            timbreFiscalDigital.getAttUuid().setString(timbreFiscal_n.getUuid());
            timbreFiscalDigital.getAttFechaTimbrado().setString(timbreFiscal_n.getFechaTimbrado());
            timbreFiscalDigital.getAttSelloCfd().setString(timbreFiscal_n.getSelloCfd());
            timbreFiscalDigital.getAttNoCertificadoSAT().setString(timbreFiscal_n.getNoCertificadoSat());
            timbreFiscalDigital.getAttSelloSAT().setString(timbreFiscal_n.getSelloSat());

            complemento.getElements().add(timbreFiscalDigital);

            comprobante.setEltOpcComplemento(complemento);

            /* Addenda1 not needed anymore:
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
            */
        }

        return comprobante;
    }
    
    public static cfd.ver33.DElementComprobante createCfdi33(final DGuiSession session, final DDbDps dps, final cfd.ver32.DTimbreFiscal timbreFiscal_n, final DGuiEdsSignature signature) throws Exception {
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
        
        if (timbreFiscal_n == null) {
            // CFDI about to be signed:
            comprobante.getAttSello().setString("");
            comprobante.getAttNoCertificado().setString(signature.getCertificateNumber());
            comprobante.getAttCertificado().setString(signature.getCertificateBase64());
        }
        else {
            // CFDI already signed:
            cfd.ver33.DSelloDigital selloDigital = extractSello33(dps);
            comprobante.getAttSello().setString(selloDigital.getSello());
            comprobante.getAttNoCertificado().setString(selloDigital.getNoCertificado());
            comprobante.getAttCertificado().setString(selloDigital.getCertificado());
        }

        comprobante.getAttFormaPago().setString((String) session.readField(DModConsts.FS_MOP_TP, new int[] { dps.getFkModeOfPaymentTypeId() }, DDbRegistry.FIELD_CODE));
        comprobante.getAttCondicionesDePago().setString(dps.getEdsPaymentConditions());
        
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
        comprobante.getAttMetodoPago().setString(dps.getEdsMethodOfPayment());
        
        braAddressEmisor = bprEmisor.getChildBranches().get(0).getChildAddresses().get(0);
        comprobante.getAttLugarExpedicion().setString(braAddressEmisor.getZipCode());
        
        comprobante.getAttConfirmacion().setString(dps.getEdsConfirmation());

        // element 'Emisor':
        comprobante.getEltEmisor().getAttRfc().setString(bprEmisor.getFiscalId());
        comprobante.getEltEmisor().getAttNombre().setString(bprEmisor.getProperName());
        comprobante.getEltEmisor().getAttRegimenFiscal().setString(dps.getEdsTaxRegime());

        // element 'Receptor':
        comprobante.getEltReceptor().getAttRfc().setString(bprReceptor.getFiscalId());
        comprobante.getEltReceptor().getAttNombre().setString(bprReceptorName.getProperName());
        //comprobante.getEltReceptor().getAttResidenciaFiscal().setString(""); // not supported yet!
        //comprobante.getEltReceptor().getAttNumRegIdTrib().setString(""); // not supported yet!
        comprobante.getEltReceptor().getAttUsoCFDI().setString(dps.getEdsUsage());

        // element 'Conceptos':
        
        HashMap<Double, Double> mapTaxIva = new HashMap<>();   // rate, tax
        HashMap<Double, Double> mapRetIva = new HashMap<>();   // rate, retained
        HashMap<Double, Double> mapRetIsr = new HashMap<>();   // rate, retained
        
        for (DDbDpsRow dpsRow : dps.getChildRows()) {
            if (!dpsRow.isDeleted()) {
                // "Concepto" node:

                cfd.ver33.DElementConcepto concepto = new cfd.ver33.DElementConcepto();

                concepto.getAttClaveProdServ().setString(dpsRow.getEdsItemKey());
                concepto.getAttNoIdentificacion().setString(dpsRow.getCode());
                concepto.getAttCantidad().setDouble(dpsRow.getQuantity() == 0 ? 1 : dpsRow.getQuantity());
                concepto.getAttCantidad().setDecimals(((DDbConfigCompany) session.getConfigCompany()).getDecimalsQuantity());
                concepto.getAttClaveUnidad().setString(dpsRow.getEdsUnitKey());
                concepto.getAttUnidad().setString(dpsRow.getDbUnitCode());
                concepto.getAttDescripcion().setString(dpsRow.getName());
                concepto.getAttValorUnitario().setDecimals(((DDbConfigCompany) session.getConfigCompany()).getDecimalsPriceUnitary());
                concepto.getAttValorUnitario().setDouble(dpsRow.getPriceUnitaryCy());
                concepto.getAttImporte().setDouble(dpsRow.getSubtotalProvCy_r());
                concepto.getAttDescuento().setDouble(dpsRow.getDiscountDocCy());

                // "Impuestos" node:
                
                // "ImpuestosTrasladados" node:
                
                cfd.ver33.DElementConceptoImpuestosTraslados impuestosTraslados = null;
                
                for (int index = 1; index <= 3; index++) {
                    int taxId = DLibConsts.UNDEFINED;
                    double taxRate = 0;
                    double taxAmount = 0;
                    String tax = "";
                    HashMap<Double, Double> mapTax = null;  // rate, tax
                    
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
                                tax = DCfdi33Catalogs.IMP_IVA;
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
                            impuestoTraslado.getAttImpuesto().setString(tax);
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
                
                for (int index = 1; index <= 3; index++) {
                    int retId = DLibConsts.UNDEFINED;
                    double retRate = 0;
                    double retAmount = 0;
                    String ret = "";
                    HashMap<Double, Double> mapRet = null;  // rate, retention
                    
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
                                ret = DCfdi33Catalogs.IMP_IVA;
                                mapRet = mapRetIva;
                                break;
                            case 3: // ISR
                                ret = DCfdi33Catalogs.IMP_ISR;
                                mapRet = mapRetIsr;
                                break;
                            default:
                                throw new Exception (DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                        }
                        
                        if (mapRet != null) {
                            Double retAcum = mapRet.get(retRate);
                            if (retAcum == null) {
                                mapRet.put(retRate, retAmount);
                            }
                            else {
                                mapRet.put(retRate, DLibUtils.round(retAcum + retAmount, decs));
                            }
                            
                            cfd.ver33.DElementConceptoImpuestoRetencion impuestoRetencion = new cfd.ver33.DElementConceptoImpuestoRetencion();
                            impuestoRetencion.getAttBase().setDouble(dpsRow.getSubtotalCy_r());
                            impuestoRetencion.getAttImpuesto().setString(ret);
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

                comprobante.getEltConceptos().getEltConceptos().add(concepto);
            }
        }
        
        double totalTrasladados = 0;
        double totalRetenciones = 0;
        cfd.ver33.DElementImpuestosTraslados impuestosTraslados = null;
        cfd.ver33.DElementImpuestosRetenciones impuestosRetenciones = null;
        
        if (!mapTaxIva.isEmpty()) {
            impuestosTraslados = new cfd.ver33.DElementImpuestosTraslados();
            
            for (Double rate : mapTaxIva.keySet()) {
                cfd.ver33.DElementImpuestoTraslado impuestoTraslado = new cfd.ver33.DElementImpuestoTraslado();
                impuestoTraslado.getAttImpuesto().setString(DCfdi33Catalogs.IMP_IVA);
                impuestoTraslado.getAttTipoFactor().setString(DCfdi33Catalogs.FAC_TP_TASA);
                impuestoTraslado.getAttTasaOCuota().setDouble(DLibUtils.round(rate, decsTax));
                impuestoTraslado.getAttImporte().setDouble(mapTaxIva.get(rate));
                impuestosTraslados.getEltImpuestoTrasladados().add(impuestoTraslado);
                
                totalTrasladados = DLibUtils.round(totalTrasladados + mapTaxIva.get(rate), decs);
            }
        }

        if (!mapRetIva.isEmpty() || !mapRetIsr.isEmpty()) {
            impuestosRetenciones = new cfd.ver33.DElementImpuestosRetenciones();
            
            for (Double rate : mapRetIva.keySet()) {
                cfd.ver33.DElementImpuestoRetencion impuestoRetencion = new cfd.ver33.DElementImpuestoRetencion();
                impuestoRetencion.getAttImpuesto().setString(DCfdi33Catalogs.IMP_IVA);
                impuestoRetencion.getAttImporte().setDouble(mapRetIva.get(rate));
                impuestosRetenciones.getEltImpuestoRetenciones().add(impuestoRetencion);
                
                totalRetenciones = DLibUtils.round(totalRetenciones+ mapRetIva.get(rate), decs);
            }
            
            for (Double rate : mapRetIsr.keySet()) {
                cfd.ver33.DElementImpuestoRetencion impuestoRetencion = new cfd.ver33.DElementImpuestoRetencion();
                impuestoRetencion.getAttImpuesto().setString(DCfdi33Catalogs.IMP_ISR);
                impuestoRetencion.getAttImporte().setDouble(mapRetIsr.get(rate));
                impuestosRetenciones.getEltImpuestoRetenciones().add(impuestoRetencion);
                
                totalRetenciones = DLibUtils.round(totalRetenciones+ mapRetIsr.get(rate), decs);
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

        if (timbreFiscal_n != null) {
            // Create element Complemento:

            cfd.ver33.DElementComplemento complemento = new cfd.ver33.DElementComplemento();
            cfd.ver33.DElementTimbreFiscalDigital timbreFiscalDigital = new cfd.ver33.DElementTimbreFiscalDigital();

            //timbreFiscalDigital.getAttVersion().setString(timbreFiscal_n.getVersion());   // by default version is "1.0"
            timbreFiscalDigital.getAttUuid().setString(timbreFiscal_n.getUuid());
            timbreFiscalDigital.getAttFechaTimbrado().setString(timbreFiscal_n.getFechaTimbrado());
            timbreFiscalDigital.getAttSelloCfd().setString(timbreFiscal_n.getSelloCfd());
            timbreFiscalDigital.getAttNoCertificadoSAT().setString(timbreFiscal_n.getNoCertificadoSat());
            timbreFiscalDigital.getAttSelloSAT().setString(timbreFiscal_n.getSelloSat());
            //timbreFiscalDigital.getAttRfcProvCertif().setString(timbreFiscal_n.get());

            complemento.getElements().add(timbreFiscalDigital);

            comprobante.setEltOpcComplemento(complemento);
        }

        return comprobante;
    }
    
    /**
     * Extracts EDS addenda.
     * @param xmlAddenda
     * @param typeXmlAddenda
     * @return
     * @throws Exception 
     */
    public static DElementExtAddenda extractExtAddenda(final String xmlAddenda, final int typeXmlAddenda) throws Exception {
        Document doc = null;
        Node node = null;
        Node nodeChild = null;
        NamedNodeMap namedNodeMapChild = null;
        Vector<Node> nodeChildren = null;
        DElementAddendaContinentalTire addendaContinentalTire = null;
        DElementExtAddenda extAddenda = null;
        
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
     * Extracts existing EDS addenda on DPS only if its addenda's type corresponds to that one defined on business partner.
     * @param dps
     * @param typeXmlAddenda
     * @return
     * @throws Exception 
     */
    public static DElementExtAddenda extractExtAddenda(final DDbDps dps, final int typeXmlAddenda) throws Exception {
        DElementExtAddenda extAddenda = null;
        
        if (dps.getChildEds() != null && dps.getChildEds().getFkXmlAddendaTypeId() == typeXmlAddenda) {
            switch (typeXmlAddenda) {
                case DModSysConsts.TS_XML_ADD_TP_CON:
                    extAddenda = (DElementAddendaContinentalTire) DTrnEdsUtils.extractExtAddenda(dps.getChildEds().getDocXmlAddenda(), typeXmlAddenda);
                    break;
                    
                default:
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
        
        return extAddenda;
    }

    /**
     * Extracts existing digital signature attributes from CFD's XML.
     * @param dps
     * @return
     * @throws Exception 
     */
    public static cfd.ver32.DSelloDigital extractSello32(final DDbDps dps) throws Exception {
        Document doc = null;
        Node node = null;
        NamedNodeMap namedNodeMapChild = null;
        cfd.ver32.DSelloDigital selloDigital = null;
        
        if (dps.getChildEds() != null) {
            selloDigital = new cfd.ver32.DSelloDigital();

            doc = DXmlUtils.parseDocument(dps.getChildEds().getDocXml());

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
    public static cfd.ver33.DSelloDigital extractSello33(final DDbDps dps) throws Exception {
        Document doc = null;
        Node node = null;
        NamedNodeMap namedNodeMapChild = null;
        cfd.ver33.DSelloDigital selloDigital = null;
        
        if (dps.getChildEds() != null) {
            selloDigital = new cfd.ver33.DSelloDigital();

            doc = DXmlUtils.parseDocument(dps.getChildEds().getDocXml());

            node = DXmlUtils.extractElements(doc, "cfdi:Comprobante").item(0);
            namedNodeMapChild = node.getAttributes();

            selloDigital.setSello(DXmlUtils.extractAttributeValue(namedNodeMapChild, "Sello", true));
            selloDigital.setNoCertificado(DXmlUtils.extractAttributeValue(namedNodeMapChild, "NoCertificado", true));
            selloDigital.setCertificado(DXmlUtils.extractAttributeValue(namedNodeMapChild, "Certificado", true));
        }
        
        return selloDigital;
    }

    /**
     * Creates new Electronic Document Supporting for provided document and type.
     * @param session Current GUI user session.
     * @param dps Document.
     * @param xmlType CFD type.
     * @param xmlRaw CFD XML file just as provided by XML Signature Provider. Required only when CFD is signed, otherwise empty string.
     * @param timbreFiscal_n CFD stamp. Required only when CFD is signed, otherwise null.
     * @return
     * @throws Exception 
     */
    public static DDbDpsEds createDpsEds(final DGuiSession session, final DDbDps dps, final int xmlType, final String xmlRaw, final cfd.ver32.DTimbreFiscal timbreFiscal_n) throws Exception {
        int xmlStatus = DLibConsts.UNDEFINED;
        int userIssuedId = DLibConsts.UNDEFINED;
        String textToSign;
        String textSigned;
        DDbDpsEds eds;
        DGuiEdsSignature signature;
        DDbBizPartner bizPartner = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, dps.getBizPartnerKey());
        DDbConfigBranch configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, dps.getCompanyBranchKey());
        cfd.DCfd cfd = new cfd.DCfd(configBranch.getEdsDirectory());
        cfd.DElementExtAddenda extAddenda = null;

        switch (xmlType) {
            case DModSysConsts.TS_XML_TP_CFD:
                throw new UnsupportedOperationException("Not supported yet.");  // no plans for supporting it later

            case DModSysConsts.TS_XML_TP_CFDI_32:
                // Create EDS:
                signature = session.getEdsSignature(dps.getCompanyBranchKey());
                cfd.ver32.DElementComprobante comprobante32 = createCfdi32(session, dps, timbreFiscal_n);
                
                // Append to EDS the very addenda previously added to DPS if any:
                if (dps.getChildEds() != null && !dps.getChildEds().getDocXmlAddenda().isEmpty()) {
                    extAddenda = extractExtAddenda(dps, bizPartner.getFkXmlAddendaTypeId());
                    if (extAddenda != null) {
                        cfd.ver32.DElementAddenda addenda = new cfd.ver32.DElementAddenda();
                        addenda.getElements().add(extAddenda);
                        comprobante32.setEltOpcAddenda(addenda);
                    }
                }
                
                // Sign EDS:
                textToSign = DCfdUtils.generateOriginalString(comprobante32);
                textSigned = signature.signText(textToSign, DLibTimeUtils.digestYear(dps.getDate())[0]);
                cfd.write(comprobante32, textToSign, textSigned, signature.getCertificateNumber(), signature.getCertificateBase64());

                // Set EDS status:
                if (timbreFiscal_n == null) {
                    // emit only CFDI:
                    xmlStatus = DModSysConsts.TS_XML_ST_PEN;
                    userIssuedId = DUtilConsts.USR_NA_ID;
                }
                else {
                    // CFDI just signed!:
                    xmlStatus = DModSysConsts.TS_XML_ST_ISS;
                    userIssuedId = session.getUser().getPkUserId();
                }
                break;

            case DModSysConsts.TS_XML_TP_CFDI_33:
                // Create EDS:
                signature = session.getEdsSignature(dps.getCompanyBranchKey());
                cfd.ver33.DElementComprobante comprobante33 = createCfdi33(session, dps, timbreFiscal_n, signature);
                
                // Append to EDS the very addenda previously added to DPS if any:
                if (dps.getChildEds() != null && !dps.getChildEds().getDocXmlAddenda().isEmpty()) {
                    extAddenda = extractExtAddenda(dps, bizPartner.getFkXmlAddendaTypeId());
                    if (extAddenda != null) {
                        cfd.ver33.DElementAddenda addenda = new cfd.ver33.DElementAddenda();
                        addenda.getElements().add(extAddenda);
                        comprobante33.setEltOpcAddenda(addenda);
                    }
                }
                
                // Sign EDS:
                textToSign = comprobante33.getElementForOriginalString();
                textSigned = signature.signText(textToSign, DLibTimeUtils.digestYear(dps.getDate())[0]);
                cfd.write(comprobante33, textToSign, textSigned, signature.getCertificateNumber(), signature.getCertificateBase64());

                // Set EDS status:
                if (timbreFiscal_n == null) {
                    // emit only CFDI:
                    xmlStatus = DModSysConsts.TS_XML_ST_PEN;
                    userIssuedId = DUtilConsts.USR_NA_ID;
                }
                else {
                    // CFDI just signed!:
                    xmlStatus = DModSysConsts.TS_XML_ST_ISS;
                    userIssuedId = session.getUser().getPkUserId();
                }
                break;

            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        // Create EDS:
        eds = new DDbDpsEds();
        eds.setPkDpsId(dps.getPkDpsId());
        eds.setCertificateNumber(session.getEdsSignature(dps.getCompanyBranchKey()).getCertificateNumber());
        eds.setSignedText(cfd.getLastStringSigned());
        eds.setSignature(cfd.getLastSignature());
        eds.setUniqueId(timbreFiscal_n == null ? "" : timbreFiscal_n.getUuid());
        eds.setDocTs(cfd.getLastTimestamp());
        eds.setDocXml(cfd.getLastXml());
        eds.setDocXmlRaw(xmlRaw);
        eds.setDocXmlAddenda(extAddenda == null ? "" : extAddenda.getElementForXml());
        eds.setDocXmlName(cfd.getLastXmlFileName());
        eds.setCancelXml("");
        eds.setCancelPdf_n(null);
        eds.setFkXmlTypeId(xmlType);
        eds.setFkXmlStatusId(xmlStatus);
        eds.setFkXmlAddendaTypeId(bizPartner.getFkXmlAddendaTypeId());
        eds.setFkXmlSignatureProviderId(timbreFiscal_n == null ? DModSysConsts.CS_XSP_NA : timbreFiscal_n.getPacId());
        eds.setFkCertificateId(session.getEdsSignature(dps.getCompanyBranchKey()).getCertificateId());
        eds.setFkUserIssuedId(userIssuedId);
        eds.setFkUserAnnulId(DUtilConsts.USR_NA_ID);

        return eds;
    }

    /**
     * Validates that CFDI's XML corresponds to CFD registry.
     * @param xml CFDI's XML.
     * @param eds EDS registry.
     * @return true if is correct.
     */
    public static boolean belongsXmlToEds(final String xml, final DDbDpsEds eds) throws Exception {
        cfd.ver32.DElementComprobante comprobanteXml = null;
        cfd.ver32.DElementComprobante comprobanteEds = null;
        boolean valid = false;

        comprobanteXml = DCfdUtils.getCfdi32(xml);
        comprobanteEds = DCfdUtils.getCfdi32(eds.getDocXml());

        valid = comprobanteXml.getEltEmisor().getAttRfc().getString().compareTo(comprobanteEds.getEltEmisor().getAttRfc().getString()) == 0 &&
                comprobanteXml.getEltReceptor().getAttRfc().getString().compareTo(comprobanteEds.getEltReceptor().getAttRfc().getString()) == 0 &&
                comprobanteXml.getAttSerie().getString().compareTo(comprobanteEds.getAttSerie().getString()) == 0 &&
                comprobanteXml.getAttFolio().getString().compareTo(comprobanteEds.getAttFolio().getString()) == 0 &&
                comprobanteXml.getAttFecha().getDatetime().compareTo(comprobanteEds.getAttFecha().getDatetime()) == 0 &&
                comprobanteXml.getAttTotal().getDouble() == comprobanteEds.getAttTotal().getDouble();

        return valid;
    }

    public static void signDps(final DGuiSession session, final DDbDps dps, final int xmlSignatureProviderId, final int[] signatureCompanyBranchKey, final int requestSubtype) throws TransformerConfigurationException, TransformerException, Exception {
        String cfdi = "";
        String message = "";
        String edsName = "";
        String edsPassword = "";
        DocumentBuilder docBuilder = null;
        Document doc = null;
        NodeList nodeList = null;
        Node node = null;
        Node nodeChildTimbreFiscal = null;
        NamedNodeMap namedNodeMapTimbreFiscal = null;
        cfd.ver32.DTimbreFiscal timbreFiscal = null;
        DDbConfigBranch configBranch = null;
        DDbXmlSignatureRequest xsr = null;
        DDbDpsEds eds = null;

        // Create or obtain XML Signature Request for signature:

        switch (requestSubtype) {
            case DModSysConsts.TX_XMS_REQ_STP_REQ:  // log sign request
                xsr = new DDbXmlSignatureRequest();
                xsr.setPkDpsId(dps.getPkDpsId());
                //xsr.setPkRequestId(0);
                xsr.setRequestType(DModSysConsts.TX_XMS_REQ_TP_SIG);
                xsr.setRequestStatus(DModSysConsts.TX_XMS_REQ_ST_STA);
                xsr.setDeleted(false);
                xsr.setSystem(true);
                xsr.setFkXmlSignatureProviderId(xmlSignatureProviderId);
                xsr.save(session);
                break;
                
            case DModSysConsts.TX_XMS_REQ_STP_VER:  // log sign verification
                xsr = DTrnEmissionUtils.getLastXmlSignatureRequest(session, dps.getPrimaryKey());
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

            configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, dps.getCompanyBranchKey());
            
            edsName = configBranch.getEdsName();
            edsPassword = configBranch.getEdsPassword();
            
            switch (xmlSignatureProviderId) {
                case DModSysConsts.CS_XSP_FCG:  // FORCOGSA

                    if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                        // Sign request:
                        
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

                            timbradoResponse = fcgPort.timbrar(dps.getChildEds().getDocXml(), autenticarResponse.getToken());
                            
                            // Process response:

                            if (timbradoResponse.getMensaje() != null) {
                                xsr.delete(session);    // delete request log

                                message = "WsTimbradoResponse msg: [" + timbradoResponse.getMensaje() + "]";
                                System.err.println(message);
                                throw new Exception(message);
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

                            // Web service request:

                            timbradoResponse = fcgPort.timbrar(dps.getChildEds().getDocXml(), autenticarResponse.getToken());

                            // Process response:

                            if (timbradoResponse.getMensaje() != null) {
                                xsr.delete(session);    // delete request log

                                message = "WsTimbradoResponse msg: [" + timbradoResponse.getMensaje() + "]";

                                System.err.println(message);
                                throw new Exception(message);
                            }

                            cfdi = timbradoResponse.getCfdi();
                        }
                    }
                    else {
                        // Sign verification:
                        
                        cfdi = DTrnEmissionUtils.getFileXml(session.getClient());
                        
                        if (cfdi.isEmpty()) {
                            throw new Exception(DUtilConsts.FILE_EXT_XML.toUpperCase() + " " + DLibConsts.ERR_MSG_UNKNOWN.toLowerCase());
                        }
                        else if (!belongsXmlToEds(cfdi, dps.getChildEds())) {
                            throw new Exception(DLibConsts.ERR_MSG_WRONG_TYPE + " (" + DUtilConsts.FILE_EXT_XML.toUpperCase() + ")");
                        }
                    }

                    break;

                case DModSysConsts.CS_XSP_FNK:  // Finkok
                    if (((DDbConfigCompany) session.getConfigCompany()).isDevelopment()) {
                        // Development environment:

                        stamp_demo.AcuseRecepcionCFDI acuseRecepcionCfdi = null;
                        JAXBElement<stamp_demo.IncidenciaArray> incidencias = null;
                        stamp_demo.Application port = new stamp_demo.StampSOAP().getApplication();

                        // Web service request:

                        if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                            // Sign request:
                            
                            acuseRecepcionCfdi = port.stamp(dps.getChildEds().getDocXml().getBytes("UTF-8"), edsName, edsPassword);
                        }
                        else {
                            // Sign verification:
                            
                            acuseRecepcionCfdi = port.stamped(dps.getChildEds().getDocXml().getBytes("UTF-8"), edsName, edsPassword);
                        }
                        
                        // Process response:

                        incidencias = acuseRecepcionCfdi.getIncidencias();

                        if (!incidencias.getValue().getIncidencia().isEmpty()) {
                            xsr.delete(session);    // delete request log

                            for (stamp_demo.Incidencia incidencia : incidencias.getValue().getIncidencia()) {
                                message += (message.isEmpty() ? "" : "\n") + "WsAcuseRecepcionCFDI code: [" + incidencia.getCodigoError().getValue() + "]" + " msg: [" + incidencia.getMensajeIncidencia().getValue() + "]";
                            }

                            System.err.println(message);
                            throw new Exception(message);
                        }

                        cfdi = acuseRecepcionCfdi.getXml().getValue();
                    }
                    else {
                        // Production environment:
                        
                        stamp.AcuseRecepcionCFDI acuseRecepcionCfdi = null;
                        JAXBElement<stamp.IncidenciaArray> incidencias = null;
                        stamp.Application port = new stamp.StampSOAP().getApplication();

                        // Web service request:

                        if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                            // Request:
                            
                            acuseRecepcionCfdi = port.stamp(dps.getChildEds().getDocXml().getBytes("UTF-8"), edsName, edsPassword);
                        }
                        else {
                            // Verification:
                            
                            acuseRecepcionCfdi = port.stamped(dps.getChildEds().getDocXml().getBytes("UTF-8"), edsName, edsPassword);
                        }

                        // Process response:

                        incidencias = acuseRecepcionCfdi.getIncidencias();

                        if (!incidencias.getValue().getIncidencia().isEmpty()) {
                            xsr.delete(session);    // delete request log

                            for (stamp.Incidencia incidencia : incidencias.getValue().getIncidencia()) {
                                message += (message.isEmpty() ? "" : "\n") + "WsAcuseRecepcionCFDI code: [" + incidencia.getCodigoError().getValue() + "]" + " msg: [" + incidencia.getMensajeIncidencia().getValue() + "]";
                            }

                            System.err.println(message);
                            throw new Exception(message);
                        }

                        cfdi = acuseRecepcionCfdi.getXml().getValue();
                    }
                    break;

                default:
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN + " " + DTrnEmissionConsts.PAC);
            }
            
            // Extract signed XML:

            docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            doc = docBuilder.parse(new ByteArrayInputStream(cfdi.getBytes("UTF-8")));
            nodeList = doc.getElementsByTagName("cfdi:Complemento");

            if (nodeList == null) {
                throw new Exception("XML element '" + "cfdi:Complemento" + "' not found!");
            }
            else {
                node = nodeList.item(0);
            }

            nodeList = node.getChildNodes();

            if (nodeList == null) {
                throw new Exception("XML element '" + "tfd:TimbreFiscalDigital" + "' does not have child elements!");
            }
            else {
                for (int i = 0; i < nodeList.getLength(); i++) {
                    if (nodeList.item(i).getNodeName().compareTo("tfd:TimbreFiscalDigital") == 0) {
                        nodeChildTimbreFiscal = nodeList.item(i);
                        break;
                    }
                }
            }

            // Preserve signed XML into EDS:

            namedNodeMapTimbreFiscal = nodeChildTimbreFiscal.getAttributes();

            timbreFiscal = new cfd.ver32.DTimbreFiscal();

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

            eds = createDpsEds(session, dps, dps.getChildEds().getFkXmlTypeId(), cfdi, timbreFiscal);

            dps.updateEds(session, eds);
            xsr.setRequestStatus(DModSysConsts.TX_XMS_REQ_ST_PRC);
            xsr.save(session);
        }
        
        if (xsr.getRequestStatus() < DModSysConsts.TX_XMS_REQ_ST_CMP) {
            // Consume stamp:

            DTrnEmissionUtils.consumeStamp(session, xsr.getFkXmlSignatureProviderId(), signatureCompanyBranchKey, DModSysConsts.TS_XSM_TP_OUT_SIG, dps.getPrimaryKey());
            xsr.setRequestStatus(DModSysConsts.TX_XMS_REQ_ST_CMP);
            xsr.save(session);
        }
        
        if (xsr.getRequestStatus() < DModSysConsts.TX_XMS_REQ_ST_FIN) {
            // Issue EDS:

            dps.issueEds(session);
            xsr.setRequestStatus(DModSysConsts.TX_XMS_REQ_ST_FIN);
            xsr.save(session);
        }
    }

    public static void cancelDps(final DGuiSession session, final DDbDps dps, final int xmlSignatureProviderId, final int[] signatureCompanyBranchKey, final int requestSubtype, final int action) throws TransformerConfigurationException, TransformerException, Exception {
        String xml = "";
        String value = "";
        String message = "";
        String fiscalId = "";
        String edsName = "";
        String edsPassword = "";
        ArrayList<String> alUuids = null;
        DDbBizPartner company = null;
        DDbConfigBranch configBranch = null;
        DDbCertificate certificate = null;
        DDbXmlSignatureRequest xsr = null;
        DDbDpsEds eds = null;
        
        // Create or obtain XML Signature Request for cancellation:

        switch (requestSubtype) {
            case DModSysConsts.TX_XMS_REQ_STP_REQ:  // log sign request
                xsr = new DDbXmlSignatureRequest();
                xsr.setPkDpsId(dps.getPkDpsId());
                //xsr.setPkRequestId(0);
                xsr.setRequestType(DModSysConsts.TX_XMS_REQ_TP_CAN);
                xsr.setRequestStatus(DModSysConsts.TX_XMS_REQ_ST_STA);
                xsr.setDeleted(false);
                xsr.setSystem(true);
                xsr.setFkXmlSignatureProviderId(xmlSignatureProviderId);
                xsr.save(session);
                break;

            case DModSysConsts.TX_XMS_REQ_STP_VER:  // log sign verification
                xsr = DTrnEmissionUtils.getLastXmlSignatureRequest(session, dps.getPrimaryKey());
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
                company = (DDbBizPartner) session.readRegistry(DModConsts.BU_BPR, dps.getCompanyKey());
                configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, dps.getCompanyBranchKey());
                certificate = (DDbCertificate) session.readRegistry(DModConsts.CU_CER, new int[] { configBranch.getFkCertificateId_n() });
                
                fiscalId = company.getFiscalId();
                edsName = configBranch.getEdsName();
                edsPassword = configBranch.getEdsPassword();
                
                switch (xmlSignatureProviderId) {
                    case DModSysConsts.CS_XSP_FCG:  // FORCOGSA
                        /*
                        if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                            // Sign request:

                            if (((DDbConfigCompany) session.getConfigCompany()).isDevelopment()) {
                                // Development environment:

                                com.wscliente.WSForcogsaService fcgService = null;
                                com.wscliente.WSForcogsa fcgPort = null;
                                com.wscliente.WsAutenticarResponse autenticarResponse = null;
                                com.wscliente.WsTimbradoResponse timbradoResponse = null;

                                fcgService = new com.wscliente.WSForcogsaService();
                                fcgPort = fcgService.getWSForcogsaPort();

                                // Web service autentication:

                                autenticarResponse = fcgPort.autenticar(sEdsName, sEdsPassword);

                                // Web service request:

                                timbradoResponse = fcgPort.timbrar(dps.getChildEds().getDocXml(), autenticarResponse.getToken());

                                if (timbradoResponse.getMensaje() != null) {
                                    xsr.delete(session);    // delete request log

                                    message = "WsTimbradoResponse msg: [" + timbradoResponse.getMensaje() + "]";
                                    System.err.println(message);
                                    throw new Exception(message);
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

                                autenticarResponse = fcgPort.autenticar(sEdsName, sEdsPassword);

                                // Document stamp:

                                timbradoResponse = fcgPort.timbrar(dps.getChildEds().getDocXml(), autenticarResponse.getToken());

                                if (timbradoResponse.getMensaje() != null) {
                                    xsr.delete(session);    // delete request log

                                    message = "WsTimbradoResponse msg: [" + timbradoResponse.getMensaje() + "]";

                                    System.err.println(message);
                                    throw new Exception(message);
                                }

                                cfdi = timbradoResponse.getCfdi();
                            }
                        }
                        else {
                            // Sign verification:

                            cfdi = DTrnEmissionUtils.getFileXml(session.getClient());

                            if (cfdi.isEmpty()) {
                                throw new Exception(DUtilConsts.FILE_EXT_XML.toUpperCase() + " " + DLibConsts.ERR_MSG_UNKNOWN.toLowerCase());
                            }
                            else if (!belongsXmlToEds(cfdi, dps.getChildEds())) {
                                throw new Exception(DLibConsts.ERR_MSG_WRONG_TYPE + " (" + DUtilConsts.FILE_EXT_XML.toUpperCase() + ")");
                            }
                        }

                        break;
                        */
                    case DModSysConsts.CS_XSP_FNK:  // Finkok
                        if (((DDbConfigCompany) session.getConfigCompany()).isDevelopment()) {
                            // Development environment:
                            
                            QName uuidQName = null;
                            JAXBElement<cancel_demo.StringArray> uuidValue = null;
                            JAXBElement<cancel_demo.FolioArray> folios = null;
                            cancel_demo.StringArray aUuids = null;
                            cancel_demo.UUIDS uuids = null;
                            cancel_demo.CancelaCFDResult cancelaCFDResult = null;
                            cancel_demo.ReceiptResult receiptResult = null;
                            cancel_demo.CancelSOAP service = null;
                            cancel_demo.Application port = null;

                            service = new cancel_demo.CancelSOAP();
                            port = service.getApplication();

                            // Web service request:

                            if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                                // Cancel request:
                                
                                alUuids = new ArrayList<String>();
                                alUuids.add(dps.getChildEds().getUniqueId());
                                
                                aUuids = new cancel_demo.StringArray();
                                aUuids.getString().addAll(alUuids);
                                
                                uuidQName = new QName("uuids");
                                uuidValue = new JAXBElement<cancel_demo.StringArray>(uuidQName, cancel_demo.StringArray.class, aUuids);
                                
                                uuids = new cancel_demo.UUIDS();
                                uuids.setUuids(uuidValue);
                                
                                if (dps.getChildEds().getFkXmlSignatureProviderId() == xmlSignatureProviderId) {
                                    // Cancel own signed document:
                                    
                                    cancelaCFDResult = port.cancel(uuids, edsName, edsPassword, fiscalId, certificate.getXtaCertificatePemKeyPublic_n(), certificate.getXtaCertificatePemKeyPrivate_n(), true);
                                }
                                else {
                                    // Cancel third party signed document:
                                    
                                    cancelaCFDResult = port.outCancel(Base64.encode(dps.getChildEds().getDocXml().getBytes()), edsName, edsPassword, fiscalId, certificate.getXtaCertificatePemKeyPublic_n(), certificate.getXtaCertificatePemKeyPrivate_n(), true);
                                }
                            }
                            else {
                                // Cancel verification:
                                
                                receiptResult = port.getReceipt(edsName, edsPassword, fiscalId, dps.getChildEds().getUniqueId(), "C");
                            }
                            
                            // Process response:

                            if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                                // Cancel request response:
                                
                                folios = cancelaCFDResult.getFolios();

                                if (folios == null) {
                                    xsr.delete(session);    // delete request log

                                    message = (message.isEmpty() ? "" : "\n") + "CancelaCFDResult msg: folios = null: [" + cancelaCFDResult.getCodEstatus().getValue() + "]";

                                    System.err.println(message);
                                    throw new Exception(message);
                                }
                                else if (folios.getValue().getFolio().isEmpty()) {
                                    xsr.delete(session);    // delete request log

                                    message = (message.isEmpty() ? "" : "\n") + "CancelaCFDResult msg: folios.folio is empty: [" + cancelaCFDResult.getCodEstatus().getValue() + "]";

                                    System.err.println(message);
                                    throw new Exception(message);
                                }
                                else if (folios.getValue().getFolio().get(0).getEstatusUUID().getValue().compareTo(DTrnEmissionConsts.UUID_ANNUL) != 0) {
                                    xsr.delete(session);    // delete request log
                                    
                                    value = folios.getValue().getFolio().get(0).getEstatusUUID().getValue();

                                    message = (message.isEmpty() ? "" : "\n") + "CancelaCFDResult msg: folios.folio.estatus != 201: [" + value + (value.compareTo(DTrnEmissionConsts.UUID_ANNUL_UUID_UNEXIST) != 0 ? "" : " (UUID does not exist!)") + "]";

                                    System.err.println(message);
                                    throw new Exception(message);
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

                                        message = (message.isEmpty() ? "" : "\n") + "ReceiptResult msg: success = null: [" + (receiptResult.getError() == null ? "error null" : receiptResult.getError().getValue()) + "]";

                                        System.err.println(message);
                                        throw new Exception(message);
                                    }
                                    else if (!receiptResult.getSuccess().getValue()) {
                                        xsr.delete(session);    // delete request log

                                        message = (message.isEmpty() ? "" : "\n") + "ReceiptResult msg: success.value = false [" + (receiptResult.getError() == null ? "error null" : receiptResult.getError().getValue()) + "]";

                                        System.err.println(message);
                                        throw new Exception(message);
                                    }
                                    else {
                                        xml = receiptResult.getReceipt().getValue();
                                    }
                                }
                            }
                        }
                        else {
                            // Production environment:
                            
                            QName uuidQName = null;
                            JAXBElement<cancel.StringArray> uuidValue = null;
                            JAXBElement<cancel.FolioArray> folios = null;
                            cancel.StringArray aUuids = null;
                            cancel.UUIDS uuids = null;
                            cancel.CancelaCFDResult cancelaCFDResult = null;
                            cancel.ReceiptResult receiptResult = null;
                            cancel.CancelSOAP service = null;
                            cancel.Application port = null;

                            service = new cancel.CancelSOAP();
                            port = service.getApplication();

                            // Web service request:

                            if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                                // Cancel request:
                                
                                alUuids = new ArrayList<String>();
                                alUuids.add(dps.getChildEds().getUniqueId());
                                
                                aUuids = new cancel.StringArray();
                                aUuids.getString().addAll(alUuids);
                                
                                uuidQName = new QName("uuids");
                                uuidValue = new JAXBElement<cancel.StringArray>(uuidQName, cancel.StringArray.class, aUuids);
                                
                                uuids = new cancel.UUIDS();
                                uuids.setUuids(uuidValue);
                                
                                if (dps.getChildEds().getFkXmlSignatureProviderId() == xmlSignatureProviderId) {
                                    // Cancel own signed document:
                                    
                                    cancelaCFDResult = port.cancel(uuids, edsName, edsPassword, fiscalId, certificate.getXtaCertificatePemKeyPublic_n(), certificate.getXtaCertificatePemKeyPrivate_n(), true);
                                }
                                else {
                                    // Cancel third party signed document:
                                    
                                    cancelaCFDResult = port.outCancel(Base64.encode(dps.getChildEds().getDocXml().getBytes()), edsName, edsPassword, fiscalId, certificate.getXtaCertificatePemKeyPublic_n(), certificate.getXtaCertificatePemKeyPrivate_n(), true);
                                }
                            }
                            else {
                                // Cancel verification:
                                
                                receiptResult = port.getReceipt(edsName, edsPassword, fiscalId, dps.getChildEds().getUniqueId(), "C");
                            }
                            
                            // Process response:

                            if (requestSubtype == DModSysConsts.TX_XMS_REQ_STP_REQ) {
                                // Cancel request response:
                                
                                folios = cancelaCFDResult.getFolios();

                                if (folios == null) {
                                    xsr.delete(session);    // delete request log

                                    message = (message.isEmpty() ? "" : "\n") + "CancelaCFDResult msg: folios = null: [" + cancelaCFDResult.getCodEstatus().getValue() + "]";

                                    System.err.println(message);
                                    throw new Exception(message);
                                }
                                else if (folios.getValue().getFolio().isEmpty()) {
                                    xsr.delete(session);    // delete request log

                                    message = (message.isEmpty() ? "" : "\n") + "CancelaCFDResult msg: folios.folio is empty: [" + cancelaCFDResult.getCodEstatus().getValue() + "]";

                                    System.err.println(message);
                                    throw new Exception(message);
                                }
                                else if (folios.getValue().getFolio().get(0).getEstatusUUID().getValue().compareTo(DTrnEmissionConsts.UUID_ANNUL) != 0) {
                                    xsr.delete(session);    // delete request log

                                    message = (message.isEmpty() ? "" : "\n") + "CancelaCFDResult msg: folios.folio.estatus != 201: [" + folios.getValue().getFolio().get(0).getEstatusUUID().getValue() + "]";

                                    System.err.println(message);
                                    throw new Exception(message);
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

                                        message = (message.isEmpty() ? "" : "\n") + "ReceiptResult msg: success = null: [" + (receiptResult.getError() == null ? "error null" : receiptResult.getError().getValue()) + "]";

                                        System.err.println(message);
                                        throw new Exception(message);
                                    }
                                    else if (!receiptResult.getSuccess().getValue()) {
                                        xsr.delete(session);    // delete request log

                                        message = (message.isEmpty() ? "" : "\n") + "ReceiptResult msg: success.value = false [" + (receiptResult.getError() == null ? "error null" : receiptResult.getError().getValue()) + "]";

                                        System.err.println(message);
                                        throw new Exception(message);
                                    }
                                    else {
                                        xml = receiptResult.getReceipt().getValue();
                                    }
                                }
                            }
                        }
                        break;

                    default:
                        throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN + " " + DTrnEmissionConsts.PAC);
                }

                // Preserve aknowledgment of cancellation on XML format into EDS:

                eds = dps.getChildEds();
                eds.setCancelXml(xml);
                eds.setFkXmlStatusId(DModSysConsts.TS_XML_ST_ANN);
                eds.setFkUserAnnulId(session.getUser().getPkUserId());

                dps.updateEds(session, eds);
            }
            
            if (dps.getFkDpsStatusId() != DModSysConsts.TS_DPS_ST_ANN) {
                dps.disable(session);
            }
            
            xsr.setRequestStatus(DModSysConsts.TX_XMS_REQ_ST_PRC);
            xsr.save(session);
        }

        if (xsr.getRequestStatus() < DModSysConsts.TX_XMS_REQ_ST_CMP) {
            // Consume stamp:

            if (action == DTrnEmissionConsts.ANNUL_CANCEL) {
                DTrnEmissionUtils.consumeStamp(session, xsr.getFkXmlSignatureProviderId(), signatureCompanyBranchKey, DModSysConsts.TS_XSM_TP_OUT_ANN, dps.getPrimaryKey());
            }
            xsr.setRequestStatus(DModSysConsts.TX_XMS_REQ_ST_CMP);
            xsr.save(session);
        }

        if (xsr.getRequestStatus() < DModSysConsts.TX_XMS_REQ_ST_FIN) {
            // Issue EDS:

            dps.issueEds(session);
            xsr.setRequestStatus(DModSysConsts.TX_XMS_REQ_ST_FIN);
            xsr.save(session);
        }
    }
}
