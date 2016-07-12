/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import cfd.DCfd;
import cfd.DCfdUtils;
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
import sba.mod.bpr.db.DDbBranch;
import sba.mod.bpr.db.DDbBranchAddress;
import sba.mod.cfg.db.DDbConfigBranch;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.cfg.db.DDbSysCurrency;

/**
 *
 * @author Sergio Flores
 */
public class DPrtDps {

    protected DGuiSession moSession;
    protected DDbDps moDps;

    public DPrtDps(DGuiSession session, int[] dpsKey) {
        moSession = session;
        moDps = (DDbDps) moSession.readRegistry(DModConsts.T_DPS, dpsKey);
    }

    public DPrtDps(DGuiSession session, DDbDps dps) {
        moSession = session;
        moDps = dps;
    }

    public HashMap<String, Object> cratePrintCfdMap() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public HashMap<String, Object> cratePrintCfdiMap() throws Exception {
        Document doc = null;
        Node node = null;
        Node nodeChild = null;
        NamedNodeMap namedNodeMap = null;
        NamedNodeMap namedNodeMapChild = null;
        Vector<Node> nodes = null;
        HashMap<String, Object> hashMap = new HashMap<String, Object>();

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

        doc = DXmlUtils.parseDocument(moDps.getChildEds().getDocXml());

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
        hashMap.put("sXmlMetodoDePago", DCfdUtils.composeMetodoPago(DXmlUtils.extractAttributeValue(namedNodeMap, "metodoDePago", true)));
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

            bufferedImage = DCfd.createQrCodeBufferedImage(sRfcEmisor, sRfcReceptor, dCfdTotal, sCfdUuid);
            hashMap.put("oXmlTimQrCode", bufferedImage.getScaledInstance(bufferedImage.getWidth(), bufferedImage.getHeight(), Image.SCALE_DEFAULT));
        }

        // Otros campos:

        sTipoDoc = (String) moSession.readField(DModConsts.TS_DPS_TP, moDps.getDpsTypeKey(), DDbRegistry.FIELD_NAME);
        dbSysCurrency = (DDbSysCurrency) moSession.readRegistry(DModConsts.CS_CUR, moSession.getSessionCustom().getLocalCurrencyKey());

        hashMap.put("sDocTipoDoc", sTipoDoc);
        hashMap.put("sDocCadenaOriginal", moDps.getChildEds().getSignedText());
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
            hashMap.put("sEmiDomTels", dbCompanyBranchDom.getChildAddresses().get(0).getTelecommDevices());
            hashMap.put("sEmiDomEmails", dbCompanyBranchDom.getChildAddresses().get(0).getTelecommElectronics());
        }

        if (dbCompanyBranchExp != null) {
            hashMap.put("sEmiExpTels", dbCompanyBranchExp.getChildAddresses().get(0).getTelecommDevices());
            hashMap.put("sEmiExpEmails", dbCompanyBranchExp.getChildAddresses().get(0).getTelecommElectronics());
        }

        if (dbBranchAddress != null) {
            hashMap.put("sRecDomTels", dbBranchAddress.getTelecommDevices());
            hashMap.put("sRecDomEmails", dbBranchAddress.getTelecommElectronics());
        }

        hashMap.put("sEdsDir", dbConfigBranch.getEdsDirectory());

        return hashMap;
    }
}
