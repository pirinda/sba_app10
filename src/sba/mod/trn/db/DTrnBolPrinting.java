/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import cfd.DCfd;
import cfd.ver40.DCfdi40Catalogs;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiSession;
import sba.lib.xml.DXmlUtils;
import sba.mod.DModConsts;
import sba.mod.bpr.db.DDbBranchAddress;
import sba.mod.cfg.db.DDbConfigBranch;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.lad.db.DDbBol;

/**
 *
 * @author Sergio Flores
 */
public class DTrnBolPrinting {

    protected DGuiSession moSession;
    protected DDbBol moBol;

    public DTrnBolPrinting(DGuiSession session, int[] dpsKey) {
        moSession = session;
        moBol = (DDbBol) moSession.readRegistry(DModConsts.L_BOL, dpsKey);
    }

    public DTrnBolPrinting(DGuiSession session, DDbBol bol) {
        moSession = session;
        moBol = bol;
    }

    @Deprecated
    public HashMap<String, Object> cratePrintMapCfd() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Deprecated
    public HashMap<String, Object> cratePrintMapCfdi32() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Deprecated
    public HashMap<String, Object> cratePrintMapCfdi33() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public HashMap<String, Object> cratePrintMapCfdi40() throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>();
        DDbConfigCompany configCompany = (DDbConfigCompany) moSession.getConfigCompany();
        DDbConfigBranch configBranch = (DDbConfigBranch) moSession.readRegistry(DModConsts.CU_CFG_BRA, moBol.getCompanyBranchKey());
        NamedNodeMap namedNodeMap;

        hashMap.put("oDecimalFormatQuantity", configCompany.getDecimalFormatQuantity());
        hashMap.put("oDecimalFormatPriceUnitary", configCompany.getDecimalFormatPriceUnitary());
        hashMap.put("nPkBol", (long) moBol.getPkBolId());
//        hashMap.put("nFkEmissionType", (long) moBol.getFkEmissionTypeId());
        hashMap.put("sEdsDir", configBranch.getDfrDirectory());
        
        // XML parsing:

        Document doc = DXmlUtils.parseDocument(moBol.getChildDfr().getSuitableDocXml());

        // Comprobante:

        double dTotal;
        String sMoneda;
        Node nodeComprobante = DXmlUtils.extractElements(doc, "cfdi:Comprobante").item(0);
        
        namedNodeMap = nodeComprobante.getAttributes();
        hashMap.put("sXmlVersion", DXmlUtils.extractAttributeValue(namedNodeMap, "Version", true));
        hashMap.put("sXmlTipoDeComprobante", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi40Catalogs.CAT_CFDI_TP, DXmlUtils.extractAttributeValue(namedNodeMap, "TipoDeComprobante", true)));
        hashMap.put("sXmlSerie", DXmlUtils.extractAttributeValue(namedNodeMap, "Serie", false));
        hashMap.put("sXmlFolio", DXmlUtils.extractAttributeValue(namedNodeMap, "Folio", true));
        hashMap.put("sXmlFecha", DXmlUtils.extractAttributeValue(namedNodeMap, "Fecha", true));
        hashMap.put("sXmlLugarExpedicion", DXmlUtils.extractAttributeValue(namedNodeMap, "LugarExpedicion", true));
        hashMap.put("sXmlNoCertificado", DXmlUtils.extractAttributeValue(namedNodeMap, "NoCertificado", true));
        hashMap.put("sXmlSello", DXmlUtils.extractAttributeValue(namedNodeMap, "Sello", true));
        hashMap.put("sXmlMetodoPago", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi40Catalogs.CAT_MDP, DXmlUtils.extractAttributeValue(namedNodeMap, "MetodoPago", true)));
        hashMap.put("sXmlFormaPago", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi40Catalogs.CAT_FDP, DXmlUtils.extractAttributeValue(namedNodeMap, "FormaPago", true)));
        hashMap.put("sXmlCondicionesDePago", DXmlUtils.extractAttributeValue(namedNodeMap, "CondicionesDePago", false));
        hashMap.put("sXmlMoneda", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi40Catalogs.CAT_MON, sMoneda = DXmlUtils.extractAttributeValue(namedNodeMap, "Moneda", true)));
        hashMap.put("sXmlTipoCambio", DXmlUtils.extractAttributeValue(namedNodeMap, "TipoCambio", false));
        hashMap.put("dXmlSubTotal", DLibUtils.parseDouble(DXmlUtils.extractAttributeValue(namedNodeMap, "SubTotal", true)));
        hashMap.put("dXmlDescuento", DLibUtils.parseDouble(DXmlUtils.extractAttributeValue(namedNodeMap, "Descuento", false)));
        hashMap.put("dXmlTotal", dTotal = DLibUtils.parseDouble(DXmlUtils.extractAttributeValue(namedNodeMap, "Total", true)));
        hashMap.put("sXmlConfirmacion", DXmlUtils.extractAttributeValue(namedNodeMap, "Confirmacion", false));
        hashMap.put("sXmlConfirmacion", DXmlUtils.extractAttributeValue(namedNodeMap, "Confirmacion", true));
        
        // CFDI Relacionados:

        if (DXmlUtils.hasChildElement(nodeComprobante, "cfdi:CfdiRelacionados")) {
            String cfdiRelacionados = "";
            Node nodeCfdiRelacionados = DXmlUtils.extractChildElements(nodeComprobante, "cfdi:CfdiRelacionados").get(0);

            namedNodeMap = nodeCfdiRelacionados.getAttributes();
            hashMap.put("sXmlTipoRelacion", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi40Catalogs.CAT_REL_TP, DXmlUtils.extractAttributeValue(namedNodeMap, "TipoRelacion", true)));
            
            for (Node nodeCfdiRelacionado : DXmlUtils.extractChildElements(nodeCfdiRelacionados, "cfdi:CfdiRelacionado")) {
                namedNodeMap = nodeCfdiRelacionado.getAttributes();
                cfdiRelacionados += (cfdiRelacionados.isEmpty() ? "" : ", ") + DXmlUtils.extractAttributeValue(namedNodeMap, "UUID", true);
            }
            hashMap.put("sXmlCfdiRelacionados", cfdiRelacionados);
        }

        // Emisor:
                
        String sEmiRfc;
        String sEmiNombre;
        String sLugarExpedicion;
        Node nodeEmisor = DXmlUtils.extractElements(doc, "cfdi:Emisor").item(0);
        
        namedNodeMap = nodeEmisor.getAttributes();
        hashMap.put("sXmlEmiRfc", sEmiRfc = DXmlUtils.extractAttributeValue(namedNodeMap, "Rfc", true));
        hashMap.put("sXmlEmiNombre", sEmiNombre = DXmlUtils.extractAttributeValue(namedNodeMap, "Nombre", true));
        hashMap.put("sXmlEmiRegimenFiscal", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi40Catalogs.CAT_REG_FISC, DXmlUtils.extractAttributeValue(namedNodeMap, "RegimenFiscal", true)));
        
        DDbBranchAddress addressEmi = (DDbBranchAddress) moSession.readRegistry(DModConsts.BU_ADD, new int[] { moBol.getFkOwnerBizPartnerId(), moBol.getFkOwnerBranchId(), 1 });
        hashMap.put("sEmiDomCalle", addressEmi.getAddress1());
        hashMap.put("sEmiDomNoExterior", addressEmi.getNumberExterior());
        hashMap.put("sEmiDomNoInterior", addressEmi.getNumberInterior());
        hashMap.put("sEmiDomColonia", addressEmi.getAddress2());
        hashMap.put("sEmiDomLocalidad", addressEmi.getLocality());
        hashMap.put("sEmiDomReferencia", addressEmi.getAddress3());
        hashMap.put("sEmiDomMunicipio", addressEmi.getCounty());
        hashMap.put("sEmiDomEstado", addressEmi.getState());
        hashMap.put("sEmiDomPais", ((String) moSession.readField(DModConsts.CS_CTY, new int[] { addressEmi.getActualFkCountryId_n(moSession) }, DDbRegistry.FIELD_NAME)).toUpperCase());
        hashMap.put("sEmiDomCodigoPostal", addressEmi.getZipCode());
        hashMap.put("sEmiDomTels", addressEmi.getTelecommDevices());
        hashMap.put("sEmiDomEmails", addressEmi.getTelecommElectronics());
        sLugarExpedicion = addressEmi.composeLocality(moSession).toUpperCase();
        
        // Receptor:
        
        String sRecRfc;
        String sRecNombre;
        Node nodeReceptor = DXmlUtils.extractElements(doc, "cfdi:Receptor").item(0);
        
        namedNodeMap = nodeReceptor.getAttributes();
        hashMap.put("sXmlRecRfc", sRecRfc = DXmlUtils.extractAttributeValue(namedNodeMap, "Rfc", true));
        hashMap.put("sXmlRecNombre", sRecNombre = DXmlUtils.extractAttributeValue(namedNodeMap, "Nombre", true));
        hashMap.put("sXmlRecDomicilioFiscal", sRecNombre = DXmlUtils.extractAttributeValue(namedNodeMap, "DomicilioFiscalReceptor", true));
        hashMap.put("sXmlRecRegimenFiscal", sRecNombre = DXmlUtils.extractAttributeValue(namedNodeMap, "RegimenFiscalReceptor", true));
        hashMap.put("sXmlRecUsoCFDI", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi40Catalogs.CAT_CFDI_USO, DXmlUtils.extractAttributeValue(namedNodeMap, "UsoCFDI", true)));

        DDbBranchAddress addressRec = (DDbBranchAddress) moSession.readRegistry(DModConsts.BU_ADD, new int[] { /*moBol.getFkBizPartnerBizPartnerId(), moBol.getFkBizPartnerBranchId(), moBol.getFkBizPartnerAddressId()*/ });
        hashMap.put("sRecDomCalle", addressRec.getAddress1());
        hashMap.put("sRecDomNoExterior", addressRec.getNumberExterior());
        hashMap.put("sRecDomNoInterior", addressRec.getNumberInterior());
        hashMap.put("sRecDomColonia", addressRec.getAddress2());
        hashMap.put("sRecDomLocalidad", addressRec.getLocality());
        hashMap.put("sRecDomReferencia", addressRec.getAddress3());
        hashMap.put("sRecDomMunicipio", addressRec.getCounty());
        hashMap.put("sRecDomEstado", addressRec.getState());
        hashMap.put("sRecDomPais", ((String) moSession.readField(DModConsts.CS_CTY, new int[] { addressRec.getActualFkCountryId_n(moSession) }, DDbRegistry.FIELD_NAME)).toUpperCase());
        hashMap.put("sRecDomCodigoPostal", addressRec.getZipCode());
        hashMap.put("sRecDomTels", addressRec.getTelecommDevices());
        hashMap.put("sRecDomEmails", addressRec.getTelecommElectronics());

        // Impuestos:

        if (DXmlUtils.hasChildElement(nodeComprobante, "cfdi:Impuestos")) {
            Node nodeImpuestos = DXmlUtils.extractChildElements(nodeComprobante, "cfdi:Impuestos").get(0);

            namedNodeMap = nodeImpuestos.getAttributes();
            hashMap.put("dXmlImpTotalImptosRetenidos", DLibUtils.parseDouble(DXmlUtils.extractAttributeValue(namedNodeMap, "TotalImpuestosRetenidos", false)));
            hashMap.put("dXmlImpTotalImptosTrasladados", DLibUtils.parseDouble(DXmlUtils.extractAttributeValue(namedNodeMap, "TotalImpuestosTrasladados", false)));
        }

        // Timbre fiscal:

        if (DXmlUtils.hasChildElement(nodeComprobante, "cfdi:Complemento")) {
            Node nodeComplemento = DXmlUtils.extractChildElements(nodeComprobante, "cfdi:Complemento").get(0);
            if (DXmlUtils.hasChildElement(nodeComplemento, "tfd:TimbreFiscalDigital")) {
                String sUuid;
                String sSelloCfd;
                Node nodeTimbreFiscalDigital = DXmlUtils.extractChildElements(nodeComplemento, "tfd:TimbreFiscalDigital").get(0);
                
                namedNodeMap = nodeTimbreFiscalDigital.getAttributes();
                hashMap.put("sXmlTimVersion", DXmlUtils.extractAttributeValue(namedNodeMap, "Version", true));
                hashMap.put("sXmlTimUuid", sUuid = DXmlUtils.extractAttributeValue(namedNodeMap, "UUID", true));
                hashMap.put("sXmlTimFechaTimbrado", DXmlUtils.extractAttributeValue(namedNodeMap, "FechaTimbrado", true));
                hashMap.put("sXmlTimRfcProvCertif", DXmlUtils.extractAttributeValue(namedNodeMap, "RfcProvCertif", false));
                hashMap.put("sXmlTimNoCertificadoSat", DXmlUtils.extractAttributeValue(namedNodeMap, "NoCertificadoSAT", false));
                hashMap.put("sXmlTimSelloCfd", sSelloCfd = DXmlUtils.extractAttributeValue(namedNodeMap, "SelloCFD", true));
                hashMap.put("sXmlTimSelloSat", DXmlUtils.extractAttributeValue(namedNodeMap, "SelloSAT", true));

                BufferedImage bufferedImage = DCfd.createQrCodeBufferedImageCfdi33(sUuid, sEmiRfc, sRecRfc, dTotal, sSelloCfd.isEmpty() ? DLibUtils.textRepeat("0", 8) : sSelloCfd.substring(sSelloCfd.length() - 8, sSelloCfd.length()));
                hashMap.put("oXmlTimQrCode", bufferedImage.getScaledInstance(bufferedImage.getWidth(), bufferedImage.getHeight(), Image.SCALE_DEFAULT));
            }
        }

        // Otros campos:

//        DDbSysCurrency dbCurrency = (DDbSysCurrency) moSession.readRegistry(DModConsts.CS_CUR, new int[] { moBol.getFkCurrencyId() });
//
//        hashMap.put("sDocTipoDoc", (String) moSession.readField(DModConsts.TS_DPS_TP, moBol.getDpsTypeKey(), DDbRegistry.FIELD_NAME));
//        hashMap.put("sDocRef", moBol.getOrder());
//        hashMap.put("sDocCadenaOriginal", moBol.getChildDfr().getSignedText());
//        hashMap.put("sDocTotalConLetra", DLibUtils.translateValueToText(moBol.getTotalCy_r(), DLibUtils.DecimalFormatValue2D.getMinimumFractionDigits(), moSession.getSessionCustom().getLocalLanguage(),
//                dbCurrency.getCurrencySingular(), dbCurrency.getCurrencyPlural(), dbCurrency.getCurrencyPrefix(), dbCurrency.getCurrencySuffix()));
//        hashMap.put("sDocUser", moSession.readField(DModConsts.CU_USR, new int[] { moBol.getFkUserInsertId() }, DDbRegistry.FIELD_NAME));
//
//        if (moBol.getFkPaymentTypeId() == DModSysConsts.FS_PAY_TP_CDT) {
//            hashMap.put("sDocPagare", "LUGAR DE EXPEDICIÓN: " + sLugarExpedicion + ", A " + DLibUtils.DateFormatDateLong.format(moBol.getDate()).toUpperCase() + ".\n" +
//                    "POR ESTE PAGARÉ DEBO(EMOS) Y PAGARÉ(EMOS) INCONDICIONALMENTE A LA ORDEN DE \"" +
//                    sEmiNombre + "\", EL DÍA " + DLibUtils.DateFormatDateLong.format(DLibTimeUtils.addDate(moBol.getDateCredit(), 0, 0, moBol.getCreditDays())).toUpperCase() + ", " +
//                    "LA CANTIDAD DE $" + DLibUtils.getDecimalFormatAmount().format(moBol.getTotalCy_r()) + " " + sMoneda + ", " +
//                    "EN ESTA CIUDAD DE " + sLugarExpedicion + " O DONDE EXIJA EL TENEDOR, IMPORTE DE LA MERCANCÍA ARRIBA DESCRITA " +
//                    "A MI(NUESTRA) ENTERA CONFORMIDAD. EN CASO DE MORA SE CONVIENE EN PACTAR UN INTERÉS MORATORIO DEL " +
//                    DLibUtils.getDecimalFormatPercentageDiscount().format(((DDbConfigCompany) moSession.getConfigCompany()).getDelayInterestRate()) + " " +
//                    "MENSUAL DESDE SU VENCIMIENTO HASTA SU TOTAL LIQUIDACIÓN.");
//        }

        return hashMap;
    }
}
