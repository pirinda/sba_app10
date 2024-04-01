/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.lad.db;

import cfd.DCfd;
import cfd.ver40.DCfdi40Catalogs;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiSession;
import sba.lib.xml.DXmlUtils;
import sba.mod.DModConsts;
import sba.mod.bpr.db.DDbBizPartner;
import sba.mod.cfg.db.DDbConfigBranch;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.cfg.db.DDbSysCountry;
import sba.mod.itm.db.DDbUnit;
import sba.mod.trn.db.DTrnDfrCatalogs;

/**
 *
 * @author Sergio Flores
 */
public class DLadBolPrinting {

    protected DGuiSession moSession;
    protected DDbBol moBol;

    public DLadBolPrinting(DGuiSession session, int[] bolKey) {
        this(session, (DDbBol) session.readRegistry(DModConsts.L_BOL, bolKey));
    }

    public DLadBolPrinting(DGuiSession session, DDbBol bol) {
        moSession = session;
        moBol = bol;
    }

    @Deprecated
    public HashMap<String, Object> createPrintingMapCfd() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Deprecated
    public HashMap<String, Object> createPrintingMapCfdi32() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Deprecated
    public HashMap<String, Object> createPrintingMapCfdi33() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public HashMap<String, Object> createPrintingMapCfdi40() throws Exception {
        HashMap<String, Object> hashMap = new HashMap<>();
        DDbConfigCompany configCompany = (DDbConfigCompany) moSession.getConfigCompany();
        DDbConfigBranch configBranch = (DDbConfigBranch) moSession.readRegistry(DModConsts.CU_CFG_BRA, moBol.getCompanyBranchKey());

        hashMap.put("oDecimalFormatPriceUnitary", configCompany.getDecimalFormatPriceUnitary());
        hashMap.put("oDecimalFormatQuantity", configCompany.getDecimalFormatQuantity());
        hashMap.put("oDecimalFormatCantidad", new DecimalFormat("#,##0.000000"));
        hashMap.put("separator", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"); // 5 HTML blank spaces
        hashMap.put("sUser", moSession.readField(DModConsts.CU_USR, new int[] { moBol.getFkUserInsertId() }, DDbRegistry.FIELD_NAME));
        hashMap.put("sEdsDir", configBranch.getDfrDirectory());
        hashMap.put("nPkBol", (long) moBol.getPkBolId());
        
        // Complemento carta porte:
        
        DDbBizPartner bizPartner;
        DDbSysCountry country = (DDbSysCountry) moSession.readRegistry(DModConsts.CS_CTY, new int[] { moBol.getFkIntlTransportCountryId() });
        DDbSysTransportType intlWayTransportType = (DDbSysTransportType) moSession.readRegistry(DModConsts.LS_TPT_TP, new int[] { moBol.getFkIntlWayTransportTypeId() });
        DDbUnit unit = (DDbUnit) moSession.readRegistry(DModConsts.IU_UNT, new int[] { moBol.getFkMerchandiseWeightUnitId() });
        
        if (configBranch.getFkBizPartnerDpsSignatureId_n() != 0) {
            bizPartner = (DDbBizPartner) moSession.readRegistry(DModConsts.BU_BPR, new int[] { configBranch.getFkBizPartnerDpsSignatureId_n() });
        }
        else {
            bizPartner = ((DDbConfigCompany) moSession.getConfigCompany()).getChildBizPartner();
        }
        
        hashMap.put("ccpVersion", moBol.getVersion());
        hashMap.put("ccpIdCCP", moBol.getBolUuid());
        hashMap.put("ccpTranspInternac", moBol.getIntlTransport());
        hashMap.put("ccpEntradaSalidaMerc", moBol.getIntlTransportDirection());
        hashMap.put("ccpPaisOrigenDestino", country.getCode() + " - " + country.getName());
        hashMap.put("ccpViaEntradaSalida", intlWayTransportType.getCode() + " - " + intlWayTransportType.getName());
        hashMap.put("ccpTotalDistRec", moBol.getDistanceKm());
        hashMap.put("ccpRegistroISTMO", moBol.isIsthmus() ? DCfdi40Catalogs.TextoSí : "");
        hashMap.put("ccpUbicacionPoloOrigen", moBol.isIsthmus() ? moBol.getIsthmusOrigin() : "");
        hashMap.put("ccpUbicacionPoloDestino", moBol.isIsthmus() ? moBol.getIsthmusDestiny() : "");
        hashMap.put("ccpRfcRemitDestin", bizPartner.getFiscalId());
        hashMap.put("ccpNombreRemitDestin", bizPartner.getPrintableName());
        hashMap.put("ccpPesoBrutoTotal", moBol.getMerchandiseWeight());
        hashMap.put("ccpUnidadPeso", unit.getCfdUnitKey() + " - " + unit.getName());
        hashMap.put("ccpNumTotalMercancias", moBol.getMerchandiseNumber());
        
        // XML parsing:

        NamedNodeMap namedNodeMap;
        Document doc = DXmlUtils.parseDocument(moBol.getChildDfr().getSuitableDocXml());

        // Comprobante:
        
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
        
        // Emisor:
        
        Node nodeEmisor = DXmlUtils.extractElements(doc, "cfdi:Emisor").item(0);
        namedNodeMap = nodeEmisor.getAttributes();
        
        DDbBizPartner bizPartnerEmisor = (DDbBizPartner) moSession.readRegistry(DModConsts.BU_BPR, moBol.getCompanyKey());
        String emisorRfc = DXmlUtils.extractAttributeValue(namedNodeMap, "Rfc", true);
        String emisorNombre = bizPartnerEmisor.isNamePrintingPolicyForFiscal() ? DXmlUtils.extractAttributeValue(namedNodeMap, "Nombre", true) : bizPartnerEmisor.getPrintableName();
        
        hashMap.put("sXmlEmiRfc", emisorRfc);
        hashMap.put("sXmlEmiNombre", emisorNombre);
        hashMap.put("sXmlEmiRegimenFiscal", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi40Catalogs.CAT_REG_FISC, DXmlUtils.extractAttributeValue(namedNodeMap, "RegimenFiscal", true)));
        
        // Receptor:
        
        Node nodeReceptor = DXmlUtils.extractElements(doc, "cfdi:Receptor").item(0);
        namedNodeMap = nodeReceptor.getAttributes();
        
        DDbBizPartner bizPartnerReceptor = (DDbBizPartner) moSession.readRegistry(DModConsts.BU_BPR, moBol.getBizPartnerKey());
        String sRecRfc = DXmlUtils.extractAttributeValue(namedNodeMap, "Rfc", true);
        String sRecNombre = bizPartnerReceptor.isNamePrintingPolicyForFiscal() ? DXmlUtils.extractAttributeValue(namedNodeMap, "Nombre", true) : bizPartnerReceptor.getPrintableName();
        
        hashMap.put("sXmlRecRfc", sRecRfc);
        hashMap.put("sXmlRecNombre", sRecNombre);
        hashMap.put("sXmlRecDomicilioFiscal", DXmlUtils.extractAttributeValue(namedNodeMap, "DomicilioFiscalReceptor", true));
        hashMap.put("sXmlRecRegimenFiscal", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi40Catalogs.CAT_REG_FISC, DXmlUtils.extractAttributeValue(namedNodeMap, "RegimenFiscalReceptor", true)));
        hashMap.put("sXmlRecUsoCFDI", DTrnDfrCatalogs.composeCatalogEntry(moSession.getClient(), DCfdi40Catalogs.CAT_CFDI_USO, DXmlUtils.extractAttributeValue(namedNodeMap, "UsoCFDI", true)));
        
        // Complemento:

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

                BufferedImage bufferedImage = DCfd.createQrCodeBufferedImageCfdi40(sUuid, emisorRfc, sRecRfc, 0, sSelloCfd.isEmpty() ? DLibUtils.textRepeat("0", 8) : sSelloCfd.substring(sSelloCfd.length() - 8, sSelloCfd.length()));
                hashMap.put("oXmlTimQrCode", bufferedImage.getScaledInstance(bufferedImage.getWidth(), bufferedImage.getHeight(), Image.SCALE_DEFAULT));
            }
        }

        // Otros campos:

        hashMap.put("sDocCadenaOriginal", moBol.getChildDfr().getSignedText());

        return hashMap;
    }
}
