/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import cfd.DCfd;
import cfd.ver33.DCfdi33Catalogs;
import cfd.ver40.DCfdi40Catalogs;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiSession;
import sba.lib.xml.DXmlUtils;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.bpr.db.DDbBizPartner;
import sba.mod.bpr.db.DDbBranch;
import sba.mod.bpr.db.DDbBranchAddress;
import sba.mod.cfg.db.DDbConfigBranch;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.cfg.db.DDbSysCurrency;

/**
 *
 * @author Sergio Flores
 */
public class DTrnDpsPrinting {

    protected DGuiSession moSession;
    protected DDbDps moDps;

    public DTrnDpsPrinting(DGuiSession session, int[] dpsKey) {
        this(session, (DDbDps) session.readRegistry(DModConsts.T_DPS, dpsKey));
    }

    public DTrnDpsPrinting(DGuiSession session, DDbDps dps) {
        moSession = session;
        moDps = dps;
    }

    @Deprecated
    public HashMap<String, Object> createPrintingMapCfd() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Deprecated
    public HashMap<String, Object> createPrintingMapCfdi32() throws Exception {
        Document doc = null;
        Node node = null;
        Node nodeChild = null;
        NamedNodeMap namedNodeMap = null;
        NamedNodeMap namedNodeMapChild = null;
        Vector<Node> nodes = null;
        HashMap<String, Object> hashMap = new HashMap<>();

        String sRegimenFiscal = "";
        String sTipoDoc = "";
        String sRfcEmisor = "";
        String sEmisor = "";
        String sRfcReceptor = "";
        String sCfdUuid = "";
        String sMoneda = "";
        String sLugarExpedicion = "";
        double dCfdTotal = 0;
        BufferedImage bufferedImage = null;
        DDbSysCurrency dbSysCurrency = null;
        DDbBranch dbCompanyBranchDom = null;
        DDbBranch dbCompanyBranchExp = null;
        DDbBranchAddress dbBranchAddress = null;
        DDbConfigBranch dbConfigBranch = (DDbConfigBranch) moSession.readRegistry(DModConsts.CU_CFG_BRA, moDps.getCompanyBranchKey());

        hashMap.put("nPkDps", (long) moDps.getPkDpsId());
        hashMap.put("nFkEmissionType", (long) moDps.getFkEmissionTypeId());

        // Comprobante:

        doc = DXmlUtils.parseDocument(moDps.getChildDfr().getDocXml());

        node = DXmlUtils.extractElements(doc, "cfdi:Comprobante").item(0);
        namedNodeMap = node.getAttributes();

        hashMap.put("sXmlVersion", DXmlUtils.extractAttributeValue(namedNodeMap, "version", false));
        hashMap.put("sXmlSerie", DXmlUtils.extractAttributeValue(namedNodeMap, "serie", false));
        hashMap.put("sXmlFolio", DXmlUtils.extractAttributeValue(namedNodeMap, "folio", true));
        hashMap.put("sXmlFecha", DXmlUtils.extractAttributeValue(namedNodeMap, "fecha", true));
        hashMap.put("sXmlSello", DXmlUtils.extractAttributeValue(namedNodeMap, "sello", true));
        hashMap.put("sXmlFormaDePago", DXmlUtils.extractAttributeValue(namedNodeMap, "formaDePago", true));
        hashMap.put("sXmlNoCertificado", DXmlUtils.extractAttributeValue(namedNodeMap, "noCertificado", true));
        hashMap.put("sXmlCondicionesDePago", DXmlUtils.extractAttributeValue(namedNodeMap, "condicionesDePago", false));
        hashMap.put("fXmlSubTotal", DLibUtils.parseDouble(DXmlUtils.extractAttributeValue(namedNodeMap, "subTotal", true)));
        hashMap.put("fXmlDescuento", DLibUtils.parseDouble(DXmlUtils.extractAttributeValue(namedNodeMap, "descuento", false)));
        hashMap.put("sXmlMotivoDescuento", DXmlUtils.extractAttributeValue(namedNodeMap, "motivoDescuento", false));
        hashMap.put("fXmlTotal", dCfdTotal = DLibUtils.parseDouble(DXmlUtils.extractAttributeValue(namedNodeMap, "total", true)));
        hashMap.put("sXmlTipoCambio", DXmlUtils.extractAttributeValue(namedNodeMap, "TipoCambio", true));   // actually "false" in CFDI 3.2 specification
        hashMap.put("sXmlMoneda", sMoneda = DXmlUtils.extractAttributeValue(namedNodeMap, "Moneda", true));           // actually "false" in CFDI 3.2 specification
        hashMap.put("sXmlTipoDeComprobante", DXmlUtils.extractAttributeValue(namedNodeMap, "tipoDeComprobante", true));
        hashMap.put("sXmlMetodoDePago", DXmlUtils.extractAttributeValue(namedNodeMap, "metodoDePago", true));
        hashMap.put("sXmlNumCtaPago", DXmlUtils.extractAttributeValue(namedNodeMap, "NumCtaPago", false));
        hashMap.put("sXmlLugarExpedicion", sLugarExpedicion = DXmlUtils.extractAttributeValue(namedNodeMap, "LugarExpedicion", true));

        // Emisor:

        node = DXmlUtils.extractElements(doc, "cfdi:Emisor").item(0);
        namedNodeMap = node.getAttributes();

        hashMap.put("sXmlEmiRfc", sRfcEmisor = DXmlUtils.extractAttributeValue(namedNodeMap, "rfc", true));
        hashMap.put("sXmlEmiNombre", sEmisor = DXmlUtils.extractAttributeValue(namedNodeMap, "nombre", false));

        if (DXmlUtils.hasChildElement(node, "cfdi:DomicilioFiscal")) {
            nodeChild = DXmlUtils.extractChildElements(node, "cfdi:DomicilioFiscal").get(0);
            namedNodeMapChild = nodeChild.getAttributes();

            hashMap.put("sXmlEmiDomCalle", DXmlUtils.extractAttributeValue(namedNodeMapChild, "calle", true));
            hashMap.put("sXmlEmiDomNoExterior", DXmlUtils.extractAttributeValue(namedNodeMapChild, "noExterior", false));
            hashMap.put("sXmlEmiDomNoInterior", DXmlUtils.extractAttributeValue(namedNodeMapChild, "noInterior", false));
            hashMap.put("sXmlEmiDomColonia", DXmlUtils.extractAttributeValue(namedNodeMapChild, "colonia", false));
            hashMap.put("sXmlEmiDomLocalidad", DXmlUtils.extractAttributeValue(namedNodeMapChild, "localidad", false));
            hashMap.put("sXmlEmiDomReferencia", DXmlUtils.extractAttributeValue(namedNodeMapChild, "referencia", false));
            hashMap.put("sXmlEmiDomMunicipio", DXmlUtils.extractAttributeValue(namedNodeMapChild, "municipio", true));
            hashMap.put("sXmlEmiDomEstado", DXmlUtils.extractAttributeValue(namedNodeMapChild, "estado", true));
            hashMap.put("sXmlEmiDomPais", DXmlUtils.extractAttributeValue(namedNodeMapChild, "pais", true));
            hashMap.put("sXmlEmiDomCodigoPostal", DXmlUtils.extractAttributeValue(namedNodeMapChild, "codigoPostal", true));

            dbCompanyBranchDom = (DDbBranch) moSession.readRegistry(DModConsts.BU_BRA, new int[] { moDps.getFkOwnerBizPartnerId(), DUtilConsts.BPR_BRA_ID });
        }

        if (DXmlUtils.hasChildElement(node, "cfdi:ExpedidoEn")) {
            nodeChild = DXmlUtils.extractChildElements(node, "cfdi:ExpedidoEn").get(0);
            namedNodeMapChild = nodeChild.getAttributes();

            hashMap.put("sXmlEmiExpCalle", DXmlUtils.extractAttributeValue(namedNodeMapChild, "calle", false));
            hashMap.put("sXmlEmiExpNoExterior", DXmlUtils.extractAttributeValue(namedNodeMapChild, "noExterior", false));
            hashMap.put("sXmlEmiExpNoInterior", DXmlUtils.extractAttributeValue(namedNodeMapChild, "noInterior", false));
            hashMap.put("sXmlEmiExpColonia", DXmlUtils.extractAttributeValue(namedNodeMapChild, "colonia", false));
            hashMap.put("sXmlEmiExpLocalidad", DXmlUtils.extractAttributeValue(namedNodeMapChild, "localidad", false));
            hashMap.put("sXmlEmiExpReferencia", DXmlUtils.extractAttributeValue(namedNodeMapChild, "referencia", false));
            hashMap.put("sXmlEmiExpMunicipio", DXmlUtils.extractAttributeValue(namedNodeMapChild, "municipio", false));
            hashMap.put("sXmlEmiExpEstado", DXmlUtils.extractAttributeValue(namedNodeMapChild, "estado", false));
            hashMap.put("sXmlEmiExpPais", DXmlUtils.extractAttributeValue(namedNodeMapChild, "pais", true));
            hashMap.put("sXmlEmiExpCodigoPostal", DXmlUtils.extractAttributeValue(namedNodeMapChild, "codigoPostal", false));

            dbCompanyBranchExp = (DDbBranch) moSession.readRegistry(DModConsts.BU_BRA, moDps.getCompanyBranchKey());
        }

        nodes = DXmlUtils.extractChildElements(node, "cfdi:RegimenFiscal");

        for (Node n : nodes) {
            namedNodeMapChild = n.getAttributes();
            sRegimenFiscal += (sRegimenFiscal.length() == 0 ? "" : "; ") + DXmlUtils.extractAttributeValue(namedNodeMapChild, "Regimen", true);
        }

        hashMap.put("sXmlEmiRegimenFiscal", sRegimenFiscal);

        // Receptor:

        node = DXmlUtils.extractElements(doc, "cfdi:Receptor").item(0);
        namedNodeMap = node.getAttributes();

        hashMap.put("sXmlRecRfc", sRfcReceptor = DXmlUtils.extractAttributeValue(namedNodeMap, "rfc", true));
        hashMap.put("sXmlRecNombre", DXmlUtils.extractAttributeValue(namedNodeMap, "nombre", false));

        if (DXmlUtils.hasChildElement(node, "cfdi:Domicilio")) {
            nodeChild = DXmlUtils.extractChildElements(node, "cfdi:Domicilio").get(0);
            namedNodeMapChild = nodeChild.getAttributes();

            hashMap.put("sXmlRecDomCalle", DXmlUtils.extractAttributeValue(namedNodeMapChild, "calle", false));
            hashMap.put("sXmlRecDomNoExterior", DXmlUtils.extractAttributeValue(namedNodeMapChild, "noExterior", false));
            hashMap.put("sXmlRecDomNoInterior", DXmlUtils.extractAttributeValue(namedNodeMapChild, "noInterior", false));
            hashMap.put("sXmlRecDomColonia", DXmlUtils.extractAttributeValue(namedNodeMapChild, "colonia", false));
            hashMap.put("sXmlRecDomLocalidad", DXmlUtils.extractAttributeValue(namedNodeMapChild, "localidad", false));
            hashMap.put("sXmlRecDomReferencia", DXmlUtils.extractAttributeValue(namedNodeMapChild, "referencia", false));
            hashMap.put("sXmlRecDomMunicipio", DXmlUtils.extractAttributeValue(namedNodeMapChild, "municipio", false));
            hashMap.put("sXmlRecDomEstado", DXmlUtils.extractAttributeValue(namedNodeMapChild, "estado", false));
            hashMap.put("sXmlRecDomPais", DXmlUtils.extractAttributeValue(namedNodeMapChild, "pais", true));
            hashMap.put("sXmlRecDomCodigoPostal", DXmlUtils.extractAttributeValue(namedNodeMapChild, "codigoPostal", false));

            dbBranchAddress = (DDbBranchAddress) moSession.readRegistry(DModConsts.BU_ADD, moDps.getBizPartnerBranchAddressKey());
        }

        // Impuestos:

        node = DXmlUtils.extractElements(doc, "cfdi:Impuestos").item(0);
        namedNodeMap = node.getAttributes();

        hashMap.put("fXmlImpTotalImptosRetenidos", DLibUtils.parseDouble(DXmlUtils.extractAttributeValue(namedNodeMap, "totalImpuestosRetenidos", false)));
        hashMap.put("fXmlImpTotalImptosTrasladados", DLibUtils.parseDouble(DXmlUtils.extractAttributeValue(namedNodeMap, "totalImpuestosTrasladados", false)));

        // Timbre fiscal:

        node = DXmlUtils.extractElements(doc, "cfdi:Complemento").item(0);

        if (node != null) {
            nodeChild = DXmlUtils.extractChildElements(node, "tfd:TimbreFiscalDigital").get(0);

            namedNodeMapChild = nodeChild.getAttributes();

            hashMap.put("sXmlTimVersion", DXmlUtils.extractAttributeValue(namedNodeMapChild, "version", true));
            hashMap.put("sXmlTimUuid", sCfdUuid = DXmlUtils.extractAttributeValue(namedNodeMapChild, "UUID", true));
            hashMap.put("sXmlTimFechaTimbrado", DXmlUtils.extractAttributeValue(namedNodeMapChild, "FechaTimbrado", true));
            hashMap.put("sXmlTimSelloCfd", DXmlUtils.extractAttributeValue(namedNodeMapChild, "selloCFD", true));

            String text = DXmlUtils.extractAttributeValue(namedNodeMapChild, "noCertificadoSAT", false);
            if (text == null || text.length() == 0) {
                text = DXmlUtils.extractAttributeValue(namedNodeMapChild, "nocertificadoSAT", true);
            }
            hashMap.put("sXmlTimNoCertificadoSat", text);

            hashMap.put("sXmlTimSelloSat", DXmlUtils.extractAttributeValue(namedNodeMapChild, "selloSAT", true));

            bufferedImage = DCfd.createQrCodeBufferedImageCfdi32(sRfcEmisor, sRfcReceptor, dCfdTotal, sCfdUuid);
            hashMap.put("oXmlTimQrCode", bufferedImage.getScaledInstance(bufferedImage.getWidth(), bufferedImage.getHeight(), Image.SCALE_DEFAULT));
        }

        // Otros campos:

        sTipoDoc = (String) moSession.readField(DModConsts.TS_DPS_TP, moDps.getDpsTypeKey(), DDbRegistry.FIELD_NAME);
        dbSysCurrency = (DDbSysCurrency) moSession.readRegistry(DModConsts.CS_CUR, moSession.getSessionCustom().getLocalCurrencyKey());

        hashMap.put("sDocTipoDoc", sTipoDoc);
        hashMap.put("sDocCadenaOriginal", moDps.getChildDfr().getSignedText());
        hashMap.put("sDocTotalConLetra", DLibUtils.translateValueToText(
                moDps.getTotalCy_r(), DLibUtils.DecimalFormatValue2D.getMinimumFractionDigits(), moSession.getSessionCustom().getLocalLanguage(),
                dbSysCurrency.getCurrencySingular(), dbSysCurrency.getCurrencyPlural(), dbSysCurrency.getCurrencyPrefix(), dbSysCurrency.getCurrencySuffix()));
        hashMap.put("sDocRef", moDps.getOrder());
        hashMap.put("sDocUser", moSession.readField(DModConsts.CU_USR, new int[] { moDps.getFkUserInsertId() }, DDbRegistry.FIELD_NAME));

        if (moDps.getFkPaymentTypeId() == DModSysConsts.FS_PAY_TP_CDT) {
            hashMap.put("sDocPagare", "LUGAR DE EXPEDICIÓN: " + sLugarExpedicion + ", A " + DLibUtils.DateFormatDateLong.format(moDps.getDate()).toUpperCase() + ".\n" +
                    "POR ESTE PAGARÉ DEBO(EMOS) Y PAGARÉ(EMOS) INCONDICIONALMENTE A LA ORDEN DE \"" +
                    sEmisor + "\", EL DÍA " + DLibUtils.DateFormatDateLong.format(DLibTimeUtils.addDate(moDps.getDateCredit(), 0, 0, moDps.getCreditDays())).toUpperCase() + ", " +
                    "LA CANTIDAD DE " + DLibUtils.getDecimalFormatAmount().format(moDps.getTotalCy_r()) + " " + sMoneda + ", " +
                    "EN ESTA CIUDAD DE " + sLugarExpedicion + " O DONDE EXIJA EL TENEDOR, IMPORTE DE LA MERCANCÍA ARRIBA DESCRITA " +
                    "A MI(NUESTRA) ENTERA CONFORMIDAD. EN CASO DE MORA SE CONVIENE EN PACTAR UN INTERÉS MORATORIO DEL " +
                    DLibUtils.getDecimalFormatPercentageDiscount().format(((DDbConfigCompany) moSession.getConfigCompany()).getDelayInterestRate()) + " " +
                    "MENSUAL DESDE SU VENCIMIENTO HASTA SU TOTAL LIQUIDACIÓN.");
        }

        if (dbCompanyBranchDom != null) {
            hashMap.put("sEmiDomTels", dbCompanyBranchDom.getChildAddressOfficial().getTelecommDevices());
            hashMap.put("sEmiDomEmails", dbCompanyBranchDom.getChildAddressOfficial().getTelecommElectronics());
        }

        if (dbCompanyBranchExp != null) {
            hashMap.put("sEmiExpTels", dbCompanyBranchExp.getChildAddressOfficial().getTelecommDevices());
            hashMap.put("sEmiExpEmails", dbCompanyBranchExp.getChildAddressOfficial().getTelecommElectronics());
        }

        if (dbBranchAddress != null) {
            hashMap.put("sRecDomTels", dbBranchAddress.getTelecommDevices());
            hashMap.put("sRecDomEmails", dbBranchAddress.getTelecommElectronics());
        }

        hashMap.put("sEdsDir", dbConfigBranch.getDfrDirectory());

        return hashMap;
    }
    
    @Deprecated
    public HashMap<String, Object> createPrintingMapCfdi33() throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>();
        DDbConfigCompany configCompany = (DDbConfigCompany) moSession.getConfigCompany();
        DDbConfigBranch configBranch = (DDbConfigBranch) moSession.readRegistry(DModConsts.CU_CFG_BRA, moDps.getCompanyBranchKey());
        NamedNodeMap namedNodeMap;

        hashMap.put("oDecimalFormatQuantity", configCompany.getDecimalFormatQuantity());
        hashMap.put("oDecimalFormatPriceUnitary", configCompany.getDecimalFormatPriceUnitary());
        hashMap.put("nPkDps", (long) moDps.getPkDpsId());
        hashMap.put("nFkEmissionType", (long) moDps.getFkEmissionTypeId());
        hashMap.put("sEdsDir", configBranch.getDfrDirectory());
        
        // XML parsing:

        Document doc = DXmlUtils.parseDocument(moDps.getChildDfr().getSuitableDocXml());

        // Comprobante:

        double dTotal;
        String sMoneda;
        Node nodeComprobante = DXmlUtils.extractElements(doc, "cfdi:Comprobante").item(0);
        
        namedNodeMap = nodeComprobante.getAttributes();
        hashMap.put("sXmlVersion", DXmlUtils.extractAttributeValue(namedNodeMap, "Version", true));
        hashMap.put("sXmlTipoDeComprobante", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi33Catalogs.CAT_CFDI_TP, DXmlUtils.extractAttributeValue(namedNodeMap, "TipoDeComprobante", true)));
        hashMap.put("sXmlSerie", DXmlUtils.extractAttributeValue(namedNodeMap, "Serie", false));
        hashMap.put("sXmlFolio", DXmlUtils.extractAttributeValue(namedNodeMap, "Folio", true));
        hashMap.put("sXmlFecha", DXmlUtils.extractAttributeValue(namedNodeMap, "Fecha", true));
        hashMap.put("sXmlLugarExpedicion", DXmlUtils.extractAttributeValue(namedNodeMap, "LugarExpedicion", true));
        hashMap.put("sXmlNoCertificado", DXmlUtils.extractAttributeValue(namedNodeMap, "NoCertificado", true));
        hashMap.put("sXmlSello", DXmlUtils.extractAttributeValue(namedNodeMap, "Sello", true));
        hashMap.put("sXmlMetodoPago", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi33Catalogs.CAT_MDP, DXmlUtils.extractAttributeValue(namedNodeMap, "MetodoPago", true)));
        hashMap.put("sXmlFormaPago", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi33Catalogs.CAT_FDP, DXmlUtils.extractAttributeValue(namedNodeMap, "FormaPago", true)));
        hashMap.put("sXmlCondicionesDePago", DXmlUtils.extractAttributeValue(namedNodeMap, "CondicionesDePago", false));
        hashMap.put("sXmlMoneda", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi33Catalogs.CAT_MON, sMoneda = DXmlUtils.extractAttributeValue(namedNodeMap, "Moneda", true)));
        hashMap.put("sXmlTipoCambio", DXmlUtils.extractAttributeValue(namedNodeMap, "TipoCambio", false));
        hashMap.put("dXmlSubTotal", DLibUtils.parseDouble(DXmlUtils.extractAttributeValue(namedNodeMap, "SubTotal", true)));
        hashMap.put("dXmlDescuento", DLibUtils.parseDouble(DXmlUtils.extractAttributeValue(namedNodeMap, "Descuento", false)));
        hashMap.put("dXmlTotal", dTotal = DLibUtils.parseDouble(DXmlUtils.extractAttributeValue(namedNodeMap, "Total", true)));
        hashMap.put("sXmlConfirmacion", DXmlUtils.extractAttributeValue(namedNodeMap, "Confirmacion", false));
        
        // CFDI Relacionados:

        if (DXmlUtils.hasChildElement(nodeComprobante, "cfdi:CfdiRelacionados")) {
            String cfdiRelacionados = "";
            Node nodeCfdiRelacionados = DXmlUtils.extractChildElements(nodeComprobante, "cfdi:CfdiRelacionados").get(0);

            namedNodeMap = nodeCfdiRelacionados.getAttributes();
            hashMap.put("sXmlTipoRelacion", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi33Catalogs.CAT_REL_TP, DXmlUtils.extractAttributeValue(namedNodeMap, "TipoRelacion", true)));
            
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
        hashMap.put("sXmlEmiNombre", sEmiNombre = DXmlUtils.extractAttributeValue(namedNodeMap, "Nombre", false));
        hashMap.put("sXmlEmiRegimenFiscal", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi33Catalogs.CAT_REG_FISC, DXmlUtils.extractAttributeValue(namedNodeMap, "RegimenFiscal", false)));
        
        DDbBranchAddress addressEmi = (DDbBranchAddress) moSession.readRegistry(DModConsts.BU_ADD, new int[] { moDps.getFkOwnerBizPartnerId(), moDps.getFkOwnerBranchId(), 1 });
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
        Node nodeReceptor = DXmlUtils.extractElements(doc, "cfdi:Receptor").item(0);
        
        namedNodeMap = nodeReceptor.getAttributes();
        hashMap.put("sXmlRecRfc", sRecRfc = DXmlUtils.extractAttributeValue(namedNodeMap, "Rfc", true));
        hashMap.put("sXmlRecNombre", DXmlUtils.extractAttributeValue(namedNodeMap, "Nombre", false));
        hashMap.put("sXmlRecUsoCFDI", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi33Catalogs.CAT_CFDI_USO, DXmlUtils.extractAttributeValue(namedNodeMap, "UsoCFDI", false)));

        DDbBranchAddress addressRec = (DDbBranchAddress) moSession.readRegistry(DModConsts.BU_ADD, new int[] { moDps.getFkBizPartnerBizPartnerId(), moDps.getFkBizPartnerBranchId(), moDps.getFkBizPartnerAddressId() });
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

        DDbSysCurrency dbCurrency = (DDbSysCurrency) moSession.readRegistry(DModConsts.CS_CUR, new int[] { moDps.getFkCurrencyId() });

        hashMap.put("sDocTipoDoc", (String) moSession.readField(DModConsts.TS_DPS_TP, moDps.getDpsTypeKey(), DDbRegistry.FIELD_NAME));
        hashMap.put("sDocRef", moDps.getOrder());
        hashMap.put("sDocCadenaOriginal", moDps.getChildDfr().getSignedText());
        hashMap.put("sDocTotalConLetra", DLibUtils.translateValueToText(
                moDps.getTotalCy_r(), DLibUtils.DecimalFormatValue2D.getMinimumFractionDigits(), moSession.getSessionCustom().getLocalLanguage(),
                dbCurrency.getCurrencySingular(), dbCurrency.getCurrencyPlural(), dbCurrency.getCurrencyPrefix(), dbCurrency.getCurrencySuffix()));
        hashMap.put("sDocUser", moSession.readField(DModConsts.CU_USR, new int[] { moDps.getFkUserInsertId() }, DDbRegistry.FIELD_NAME));

        if (moDps.getFkPaymentTypeId() == DModSysConsts.FS_PAY_TP_CDT) {
            hashMap.put("sDocPagare", "LUGAR DE EXPEDICIÓN: " + sLugarExpedicion + ", A " + DLibUtils.DateFormatDateLong.format(moDps.getDate()).toUpperCase() + ".\n" +
                    "POR ESTE PAGARÉ DEBO(EMOS) Y PAGARÉ(EMOS) INCONDICIONALMENTE A LA ORDEN DE \"" +
                    sEmiNombre + "\", EL DÍA " + DLibUtils.DateFormatDateLong.format(DLibTimeUtils.addDate(moDps.getDateCredit(), 0, 0, moDps.getCreditDays())).toUpperCase() + ", " +
                    "LA CANTIDAD DE $" + DLibUtils.getDecimalFormatAmount().format(moDps.getTotalCy_r()) + " " + sMoneda + ", " +
                    "EN ESTA CIUDAD DE " + sLugarExpedicion + " O DONDE EXIJA EL TENEDOR, IMPORTE DE LA MERCANCÍA ARRIBA DESCRITA " +
                    "A MI(NUESTRA) ENTERA CONFORMIDAD. EN CASO DE MORA SE CONVIENE EN PACTAR UN INTERÉS MORATORIO DEL " +
                    DLibUtils.getDecimalFormatPercentageDiscount().format(((DDbConfigCompany) moSession.getConfigCompany()).getDelayInterestRate()) + " " +
                    "MENSUAL DESDE SU VENCIMIENTO HASTA SU TOTAL LIQUIDACIÓN.");
        }

        return hashMap;
    }
    
    public HashMap<String, Object> createPrintingMapCfdi40() throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>();
        DDbConfigCompany configCompany = (DDbConfigCompany) moSession.getConfigCompany();
        DDbConfigBranch configBranch = (DDbConfigBranch) moSession.readRegistry(DModConsts.CU_CFG_BRA, moDps.getCompanyBranchKey());

        hashMap.put("oDecimalFormatQuantity", configCompany.getDecimalFormatQuantity());
        hashMap.put("oDecimalFormatPriceUnitary", configCompany.getDecimalFormatPriceUnitary());
        hashMap.put("nPkDps", (long) moDps.getPkDpsId());
        hashMap.put("nFkEmissionType", (long) moDps.getFkEmissionTypeId());
        hashMap.put("sEdsDir", configBranch.getDfrDirectory());
        
        // XML parsing:

        NamedNodeMap namedNodeMap;
        Document doc = DXmlUtils.parseDocument(moDps.getChildDfr().getSuitableDocXml());

        // Comprobante:
        
        Node nodeComprobante = DXmlUtils.extractElements(doc, "cfdi:Comprobante").item(0);
        namedNodeMap = nodeComprobante.getAttributes();
        
        double total = DLibUtils.parseDouble(DXmlUtils.extractAttributeValue(namedNodeMap, "Total", true));
        String currency = DXmlUtils.extractAttributeValue(namedNodeMap, "Moneda", true);
        
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
        hashMap.put("sXmlMoneda", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi40Catalogs.CAT_MON, currency));
        hashMap.put("sXmlTipoCambio", DXmlUtils.extractAttributeValue(namedNodeMap, "TipoCambio", false));
        hashMap.put("dXmlSubTotal", DLibUtils.parseDouble(DXmlUtils.extractAttributeValue(namedNodeMap, "SubTotal", true)));
        hashMap.put("dXmlDescuento", DLibUtils.parseDouble(DXmlUtils.extractAttributeValue(namedNodeMap, "Descuento", false)));
        hashMap.put("dXmlTotal", total);
        hashMap.put("sXmlExportacion", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi40Catalogs.CAT_EXP, DXmlUtils.extractAttributeValue(namedNodeMap, "Exportacion", true)));
        hashMap.put("sXmlConfirmacion", DXmlUtils.extractAttributeValue(namedNodeMap, "Confirmacion", false));
        
        // CFDI relacionados:

        if (DXmlUtils.hasChildElement(nodeComprobante, "cfdi:CfdiRelacionados")) {
            String cfdiRelacionados = "";
            
            Vector<Node> nodes = DXmlUtils.extractChildElements(nodeComprobante, "cfdi:CfdiRelacionados");
            for (Node node : nodes) {
                namedNodeMap = node.getAttributes();
                
                cfdiRelacionados += (cfdiRelacionados.isEmpty() ? "" : "/ ") + "Tipo relación: "
                        + DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi40Catalogs.CAT_REL_TP, DXmlUtils.extractAttributeValue(namedNodeMap, "TipoRelacion", true));

                String uuids = "";
                for (Node nodeCfdiRelacionado : DXmlUtils.extractChildElements(node, "cfdi:CfdiRelacionado")) {
                    namedNodeMap = nodeCfdiRelacionado.getAttributes();
                    uuids += (uuids.isEmpty() ? "" : ", ") + DXmlUtils.extractAttributeValue(namedNodeMap, "UUID", true);
                }
                
                cfdiRelacionados += "; UUID: " + uuids;
            }
            
            hashMap.put("sXmlCfdiRelacionados", cfdiRelacionados);
        }
        
        // Información global:
        
        if (DXmlUtils.hasChildElement(nodeComprobante, "cfdi:InformacionGlobal")) {
            String informacionGlobal = "";
            
            Node node = DXmlUtils.extractChildElements(nodeComprobante, "cfdi:InformacionGlobal").get(0);
            namedNodeMap = node.getAttributes();

            informacionGlobal = "Periodicidad: "
                    + DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi40Catalogs.CAT_GBL_PER, DXmlUtils.extractAttributeValue(namedNodeMap, "Periodicidad", true))
                    + "; Meses: "
                    + DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi40Catalogs.CAT_GBL_MES, DXmlUtils.extractAttributeValue(namedNodeMap, "Meses", true))
                    + "; Año: "
                    + DXmlUtils.extractAttributeValue(namedNodeMap, "Año", true);
            
            hashMap.put("sDocGlobal", informacionGlobal);
        }

        // Emisor:
        
        Node nodeEmisor = DXmlUtils.extractElements(doc, "cfdi:Emisor").item(0);
        namedNodeMap = nodeEmisor.getAttributes();
        
        DDbBizPartner bizPartnerEmisor = (DDbBizPartner) moSession.readRegistry(DModConsts.BU_BPR, moDps.getCompanyKey());
        DDbBranchAddress addressEmisor = (DDbBranchAddress) moSession.readRegistry(DModConsts.BU_ADD, new int[] { moDps.getFkOwnerBizPartnerId(), moDps.getFkOwnerBranchId(), DUtilConsts.BRA_ADD_ID });
        String emisorRfc = DXmlUtils.extractAttributeValue(namedNodeMap, "Rfc", true);
        String emisorNombre = bizPartnerEmisor.isNamePrintingPolicyForFiscal() ? DXmlUtils.extractAttributeValue(namedNodeMap, "Nombre", true) : bizPartnerEmisor.getPrintableName();
        String lugarExpedicion = addressEmisor.composeLocality(moSession).toUpperCase();
        
        hashMap.put("sXmlEmiRfc", emisorRfc);
        hashMap.put("sXmlEmiNombre", emisorNombre);
        hashMap.put("sXmlEmiRegimenFiscal", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi40Catalogs.CAT_REG_FISC, DXmlUtils.extractAttributeValue(namedNodeMap, "RegimenFiscal", true)));
        
        hashMap.put("sEmiDomCalle", addressEmisor.getAddress1());
        hashMap.put("sEmiDomNoExterior", addressEmisor.getNumberExterior());
        hashMap.put("sEmiDomNoInterior", addressEmisor.getNumberInterior());
        hashMap.put("sEmiDomColonia", addressEmisor.getAddress2());
        hashMap.put("sEmiDomLocalidad", addressEmisor.getLocality());
        hashMap.put("sEmiDomReferencia", addressEmisor.getAddress3());
        hashMap.put("sEmiDomMunicipio", addressEmisor.getCounty());
        hashMap.put("sEmiDomEstado", addressEmisor.getState());
        hashMap.put("sEmiDomPais", ((String) moSession.readField(DModConsts.CS_CTY, new int[] { addressEmisor.getActualFkCountryId_n(moSession) }, DDbRegistry.FIELD_NAME)).toUpperCase());
        hashMap.put("sEmiDomCodigoPostal", addressEmisor.getZipCode());
        hashMap.put("sEmiDomTels", addressEmisor.getTelecommDevices());
        hashMap.put("sEmiDomEmails", addressEmisor.getTelecommElectronics());
        
        // Receptor:
        
        Node nodeReceptor = DXmlUtils.extractElements(doc, "cfdi:Receptor").item(0);
        namedNodeMap = nodeReceptor.getAttributes();
        
        DDbBizPartner bizPartnerReceptor = (DDbBizPartner) moSession.readRegistry(DModConsts.BU_BPR, moDps.getBizPartnerKey());
        DDbBranchAddress addressReceptor = (DDbBranchAddress) moSession.readRegistry(DModConsts.BU_ADD, moDps.getBizPartnerBranchAddressKey());
        String sRecRfc = DXmlUtils.extractAttributeValue(namedNodeMap, "Rfc", true);
        String sRecNombre = bizPartnerReceptor.isNamePrintingPolicyForFiscal() ? DXmlUtils.extractAttributeValue(namedNodeMap, "Nombre", true) : bizPartnerReceptor.getPrintableName();
        
        hashMap.put("sXmlRecRfc", sRecRfc);
        hashMap.put("sXmlRecNombre", sRecNombre);
        hashMap.put("sXmlRecDomicilioFiscal", DXmlUtils.extractAttributeValue(namedNodeMap, "DomicilioFiscalReceptor", true));
        hashMap.put("sXmlRecRegimenFiscal", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi40Catalogs.CAT_REG_FISC, DXmlUtils.extractAttributeValue(namedNodeMap, "RegimenFiscalReceptor", true)));
        hashMap.put("sXmlRecUsoCFDI", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi40Catalogs.CAT_CFDI_USO, DXmlUtils.extractAttributeValue(namedNodeMap, "UsoCFDI", true)));
        
        hashMap.put("sRecDomCalle", addressReceptor.getAddress1());
        hashMap.put("sRecDomNoExterior", addressReceptor.getNumberExterior());
        hashMap.put("sRecDomNoInterior", addressReceptor.getNumberInterior());
        hashMap.put("sRecDomColonia", addressReceptor.getAddress2());
        hashMap.put("sRecDomLocalidad", addressReceptor.getLocality());
        hashMap.put("sRecDomReferencia", addressReceptor.getAddress3());
        hashMap.put("sRecDomMunicipio", addressReceptor.getCounty());
        hashMap.put("sRecDomEstado", addressReceptor.getState());
        hashMap.put("sRecDomPais", ((String) moSession.readField(DModConsts.CS_CTY, new int[] { addressReceptor.getActualFkCountryId_n(moSession) }, DDbRegistry.FIELD_NAME)).toUpperCase());
        hashMap.put("sRecDomCodigoPostal", addressReceptor.getZipCode());
        hashMap.put("sRecDomTels", addressReceptor.getTelecommDevices());
        hashMap.put("sRecDomEmails", addressReceptor.getTelecommElectronics());

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

                BufferedImage bufferedImage = DCfd.createQrCodeBufferedImageCfdi40(sUuid, emisorRfc, sRecRfc, total, sSelloCfd.isEmpty() ? DLibUtils.textRepeat("0", 8) : sSelloCfd.substring(sSelloCfd.length() - 8, sSelloCfd.length()));
                hashMap.put("oXmlTimQrCode", bufferedImage.getScaledInstance(bufferedImage.getWidth(), bufferedImage.getHeight(), Image.SCALE_DEFAULT));
            }
        }

        // Otros campos:

        DDbSysCurrency dbCurrency = (DDbSysCurrency) moSession.readRegistry(DModConsts.CS_CUR, new int[] { moDps.getFkCurrencyId() });

        hashMap.put("sDocTipoDoc", (String) moSession.readField(DModConsts.TS_DPS_TP, moDps.getDpsTypeKey(), DDbRegistry.FIELD_NAME));
        hashMap.put("sDocRef", moDps.getOrder());
        hashMap.put("sDocCadenaOriginal", moDps.getChildDfr().getSignedText());
        hashMap.put("sDocTotalConLetra", DLibUtils.translateValueToText(
                moDps.getTotalCy_r(), DLibUtils.DecimalFormatValue2D.getMinimumFractionDigits(), moSession.getSessionCustom().getLocalLanguage(),
                dbCurrency.getCurrencySingular(), dbCurrency.getCurrencyPlural(), dbCurrency.getCurrencyPrefix(), dbCurrency.getCurrencySuffix()));
        hashMap.put("sDocUser", moSession.readField(DModConsts.CU_USR, new int[] { moDps.getFkUserInsertId() }, DDbRegistry.FIELD_NAME));

        if (moDps.getFkPaymentTypeId() == DModSysConsts.FS_PAY_TP_CDT) {
            hashMap.put("sDocPagare", "LUGAR DE EXPEDICIÓN: " + lugarExpedicion + ", A " + DLibUtils.DateFormatDateLong.format(moDps.getDate()).toUpperCase() + ".\n" +
                    "POR ESTE PAGARÉ DEBO(EMOS) Y PAGARÉ(EMOS) INCONDICIONALMENTE A LA ORDEN DE \"" +
                    emisorNombre + "\", EL DÍA " + DLibUtils.DateFormatDateLong.format(DLibTimeUtils.addDate(moDps.getDateCredit(), 0, 0, moDps.getCreditDays())).toUpperCase() + ", " +
                    "LA CANTIDAD DE $" + DLibUtils.getDecimalFormatAmount().format(moDps.getTotalCy_r()) + " " + currency + ", " +
                    "EN ESTA CIUDAD DE " + lugarExpedicion + " O DONDE EXIJA EL TENEDOR, IMPORTE DE LA MERCANCÍA ARRIBA DESCRITA " +
                    "A MI(NUESTRA) ENTERA CONFORMIDAD. EN CASO DE MORA SE CONVIENE EN PACTAR UN INTERÉS MORATORIO DEL " +
                    DLibUtils.getDecimalFormatPercentageDiscount().format(((DDbConfigCompany) moSession.getConfigCompany()).getDelayInterestRate()) + " " +
                    "MENSUAL DESDE SU VENCIMIENTO HASTA SU TOTAL LIQUIDACIÓN.");
        }

        return hashMap;
    }
}
