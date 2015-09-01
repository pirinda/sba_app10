/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import cfd.DAttributeOptionCondicionesPago;
import cfd.DAttributeOptionFormaPago;
import cfd.DAttributeOptionImpuestoRetencion;
import cfd.DAttributeOptionImpuestoTraslado;
import cfd.DAttributeOptionMetodoPago;
import cfd.DAttributeOptionMoneda;
import cfd.DAttributeOptionTipoComprobante;
import cfd.DCfd;
import cfd.DCfdUtils;
import cfd.ext.addenda1.DElementAddenda1;
import cfd.ext.addenda1.DElementAdicionalConcepto;
import cfd.ext.addenda1.DElementNota;
import cfd.ext.addenda1.DElementNotas;
import cfd.ext.addenda1.DElementPagare;
import cfd.util.DUtilities;
import cfd.ver3.DTimbreFiscal;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Set;
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
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.bpr.db.DDbBizPartner;
import sba.mod.bpr.db.DDbBranch;
import sba.mod.bpr.db.DDbBranchAddress;
import sba.mod.cfg.db.DDbCertificate;
import sba.mod.cfg.db.DDbConfigBranch;
import sba.mod.cfg.db.DDbConfigCompany;

/**
 *
 * @author Sergio Flores
 */
public abstract class DTrnEdsUtils {

    private static cfd.ver2.DElementComprobante createCfd(final DGuiSession session, final DDbDps dps) throws Exception {
        cfd.ver2.DElementComprobante comprobante = null;

        return comprobante;
    }

    private static cfd.ver3.DElementComprobante createCfdi(final DGuiSession session, final DDbDps dps, final DTimbreFiscal timbreFiscal) throws Exception {
        int i = 0;
        int nMoneda = 0;
        int nPagos = 1;
        int nImptoTipo = 0;
        double dImpto = 0;
        double dImptoAux = 0;
        double dImptoTasa = 0;
        double dTotalImptoRetenido = 0;
        double dTotalImptoTrasladado = 0;
        double dTotalPesoBruto = 0;
        double dTotalPesoNeto = 0;
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
        DAttributeOptionMetodoPago attOptionMetodoPago = new DAttributeOptionMetodoPago("MetodoPago", false);
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

        cfd.ver3.DElementComprobante comprobante = null;

        if (timbreFiscal != null) {
            comprobante = new cfd.ver3.DElementComprobante(true);
        }
        else {
            comprobante = new cfd.ver3.DElementComprobante();
        }

        comprobante.getAttSerie().setString(dps.getSeries());
        comprobante.getAttFolio().setString("" + dps.getNumber());
        comprobante.getAttFecha().setDatetime(tDate);

        if (nPagos <= 1) {
            comprobante.getAttFormaDePago().setOption(DAttributeOptionFormaPago.CFD_UNA_EXHIBICION);
        }
        else {
            comprobante.getAttFormaDePago().setOption("" + nPagos, DAttributeOptionFormaPago.CFD_PARCIALIDADES);
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

        comprobante.getAttTipoDeComprobante().setOption(dps.isDpsDocument() ? DAttributeOptionTipoComprobante.CFD_INGRESO : DAttributeOptionTipoComprobante.CFD_EGRESO);

        switch (dps.getFkModeOfPaymentTypeId()) {
            case DModSysConsts.FS_MOP_TP_CSH:
            case DModSysConsts.FS_MOP_TP_DEP:
                comprobante.getAttMetodoDePago().setString(attOptionMetodoPago.getOptions().get(DAttributeOptionMetodoPago.CFD_EFECTIVO));
                break;
            case DModSysConsts.FS_MOP_TP_TRA:
                comprobante.getAttMetodoDePago().setString(attOptionMetodoPago.getOptions().get(DAttributeOptionMetodoPago.CFD_TRANSFERENCIA));
                break;
            case DModSysConsts.FS_MOP_TP_CHK:
            case DModSysConsts.FS_MOP_TP_CHK_RET:
                comprobante.getAttMetodoDePago().setString(attOptionMetodoPago.getOptions().get(DAttributeOptionMetodoPago.CFD_CHEQUE));
                break;
            case DModSysConsts.FS_MOP_TP_DBT:
                comprobante.getAttMetodoDePago().setString(attOptionMetodoPago.getOptions().get(DAttributeOptionMetodoPago.CFD_TARJETA_DEBITO));
                break;
            case DModSysConsts.FS_MOP_TP_CDT:
                comprobante.getAttMetodoDePago().setString(attOptionMetodoPago.getOptions().get(DAttributeOptionMetodoPago.CFD_TARJETA_CREDITO));
                break;
            case DModSysConsts.FS_MOP_TP_NON_DEF:
                comprobante.getAttMetodoDePago().setString(attOptionMetodoPago.getOptions().get(DAttributeOptionMetodoPago.CFD_NO_IDENTIFICADO));
                break;
            default:
                comprobante.getAttMetodoDePago().setString(attOptionMetodoPago.getOptions().get(DAttributeOptionMetodoPago.CFD_NO_IDENTIFICADO));
        }

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

            comprobante.getEltEmisor().setEltOpcExpedidoEn(new cfd.ver3.DElementTipoUbicacion("cfdi:ExpedidoEn"));

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
            cfd.ver3.DElementRegimenFiscal regimenFiscal = new cfd.ver3.DElementRegimenFiscal();
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

                cfd.ver3.DElementConcepto concepto = new cfd.ver3.DElementConcepto();
                DElementAdicionalConcepto adicionalConcepto = new DElementAdicionalConcepto();

                concepto.getAttNoIdentificacion().setString(dpsRow.getCode());
                concepto.getAttUnidad().setString(dpsRow.getDbUnitCode());
                concepto.getAttCantidad().setDouble(dpsRow.getQuantity() == 0 ? 1 : dpsRow.getQuantity());
                concepto.getAttDescripcion().setString(dpsRow.getName());
                concepto.getAttValorUnitario().setDouble(dpsRow.getPriceUnitaryCy());
                concepto.getAttImporte().setDouble(dpsRow.getSubtotalProvCy_r());

                // "InformacionAduanera" child nodes:

                aImportDeclarations = DTrnDocRowUtils.getDpsRowImportDeclarations(session, dpsRow.getPrimaryKey());
                for (DTrnImportDeclaration importDeclaration : aImportDeclarations) {
                    cfd.ver3.DElementInformacionAduanera informacionAduanera = new cfd.ver3.DElementInformacionAduanera();

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

                dTotalPesoBruto += dpsRow.getWeightGross();
                dTotalPesoNeto += dpsRow.getMeasurementMass();

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

        cfd.ver3.DElementImpuestosRetenidos impuestosRetenidos = new cfd.ver3.DElementImpuestosRetenidos();

        hmImpto = hmRetenidoIva;
        if (!hmImpto.isEmpty()) {
            setKeyImptos = hmImpto.keySet();
            for (Double key : setKeyImptos) {
                cfd.ver3.DElementImpuestoRetencion impuestoRetencion = new cfd.ver3.DElementImpuestoRetencion();

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
                cfd.ver3.DElementImpuestoRetencion impuestoRetencion = new cfd.ver3.DElementImpuestoRetencion();

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

        cfd.ver3.DElementImpuestosTrasladados impuestosTrasladados = new cfd.ver3.DElementImpuestosTrasladados();

        hmImpto = hmTrasladadoIva;
        if (!hmImpto.isEmpty()) {
            setKeyImptos = hmImpto.keySet();
            for (Double key : setKeyImptos) {
                cfd.ver3.DElementImpuestoTraslado impuestoTraslado = new cfd.ver3.DElementImpuestoTraslado();

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

        if (timbreFiscal != null) {
            // Create element Complemento:

            cfd.ver3.DElementComplemento complemento = new cfd.ver3.DElementComplemento();
            cfd.ver3.DElementTimbreFiscalDigital timbreFiscalDigital = new cfd.ver3.DElementTimbreFiscalDigital();

            timbreFiscalDigital.getAttUuid().setString(timbreFiscal.getUuid());
            timbreFiscalDigital.getAttFechaTimbrado().setString(timbreFiscal.getFechaTimbrado());
            timbreFiscalDigital.getAttSelloCfd().setString(timbreFiscal.getSelloCfd());
            timbreFiscalDigital.getAttNoCertificadoSAT().setString(timbreFiscal.getNoCertificadoSat());
            timbreFiscalDigital.getAttSelloSAT().setString(timbreFiscal.getSelloSat());

            complemento.getElements().add(timbreFiscalDigital);

            comprobante.setEltOpcComplemento(complemento);

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

        return comprobante;
    }

    public static DDbDpsEds createDpsEds(final DGuiSession session, final DDbDps dps, final int xmlType, final String xmlRaw, final DTimbreFiscal timbreFiscal_n) throws Exception {
        int xmlStatus = DLibConsts.UNDEFINED;
        int userIssuedId = DLibConsts.UNDEFINED;
        String textToSign = "";
        String textSigned = "";
        DDbDpsEds eds = null;
        DDbConfigBranch configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, dps.getCompanyBranchKey());
        DCfd cfd = new DCfd(configBranch.getEdsDirectory());
        cfd.ver2.DElementComprobante comprobante2 = null;
        cfd.ver3.DElementComprobante comprobante3 = null;

        switch (xmlType) {
            case DModSysConsts.TS_XML_TP_CFD:
                comprobante2 = createCfd(session, dps);
                textToSign = DUtilities.generateOriginalString(comprobante2);
                textSigned = session.getEdsSignature(dps.getCompanyBranchKey()).signText(textToSign, DLibTimeUtils.digestYear(dps.getDate())[0]);
                cfd.write(comprobante2, textToSign, textSigned, session.getEdsSignature(dps.getCompanyBranchKey()).getCertificateNumber(), session.getEdsSignature(dps.getCompanyBranchKey()).getCertificateBase64());

                xmlStatus = DModSysConsts.TS_XML_ST_ISS;
                userIssuedId = session.getUser().getPkUserId();
                break;

            case DModSysConsts.TS_XML_TP_CFDI:
                comprobante3 = createCfdi(session, dps, timbreFiscal_n);
                textToSign = DUtilities.generateOriginalString(comprobante3);
                textSigned = session.getEdsSignature(dps.getCompanyBranchKey()).signText(textToSign, DLibTimeUtils.digestYear(dps.getDate())[0]);
                cfd.write(comprobante3, textToSign, textSigned, session.getEdsSignature(dps.getCompanyBranchKey()).getCertificateNumber(), session.getEdsSignature(dps.getCompanyBranchKey()).getCertificateBase64());

                if (timbreFiscal_n == null) {
                    xmlStatus = DModSysConsts.TS_XML_ST_PEN;
                    userIssuedId = DUtilConsts.USR_NA_ID;
                }
                else {
                    xmlStatus = DModSysConsts.TS_XML_ST_ISS;
                    userIssuedId = session.getUser().getPkUserId();
                }
                break;

            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        eds = new DDbDpsEds();
        eds.setPkDpsId(dps.getPkDpsId());
        eds.setCertificateNumber(session.getEdsSignature(dps.getCompanyBranchKey()).getCertificateNumber());
        eds.setSignedText(cfd.getLastStringSigned());
        eds.setSignature(cfd.getLastSignature());
        eds.setUniqueId(timbreFiscal_n == null ? "" : timbreFiscal_n.getUuid());
        eds.setDocTs(cfd.getLastTimestamp());
        eds.setDocXml(cfd.getLastXml().replaceAll("'", "''"));
        eds.setDocXmlRaw(xmlRaw.replaceAll("'", "''"));
        eds.setDocXmlName(cfd.getLastXmlFileName());
        eds.setCancelXml("");
        eds.setCancelPdf_n(null);
        eds.setFkXmlTypeId(xmlType);
        eds.setFkXmlStatusId(xmlStatus);
        eds.setFkXmlSignatureProviderId(timbreFiscal_n == null ? DModSysConsts.CS_XSP_NA : timbreFiscal_n.getPacId());
        eds.setFkCertificateId(session.getEdsSignature(dps.getCompanyBranchKey()).getCertificateId());
        eds.setFkUserIssuedId(userIssuedId);
        eds.setFkUserAnnulId(DUtilConsts.USR_NA_ID);

        return eds;
    }

    /**
     * Validate that CFDI's XML corresponds to CFD registry.
     * @param xml CFDI's XML.
     * @param eds EDS registry.
     * @return true if is correct.
     */
    private static boolean belongsXmlToEds(final String xml, final DDbDpsEds eds) throws Exception {
        cfd.ver3.DElementComprobante comprobanteXml = null;
        cfd.ver3.DElementComprobante comprobanteEds = null;
        boolean valid = false;

        comprobanteXml = DCfdUtils.getCfdi(xml);
        comprobanteEds = DCfdUtils.getCfdi(eds.getDocXml());

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
        DTimbreFiscal timbreFiscal = null;
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

            timbreFiscal = new DTimbreFiscal();

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

                // Preserve cancellation XML into EDS:

                eds = dps.getChildEds();
                eds.setCancelXml(xml.replaceAll("'", "''"));
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
