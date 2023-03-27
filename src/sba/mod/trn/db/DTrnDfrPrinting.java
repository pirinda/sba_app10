/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.trn.db;

import cfd.DCfd;
import cfd.DCfdUtils;
import cfd.DElement;
import cfd.ver40.DCfdi40Catalogs;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import sba.lib.DLibUtils;
import sba.lib.gui.DGuiSession;
import sba.mod.DModSysConsts;
import sba.mod.cfg.db.DDbConfigBranch;

/**
 *
 * @author Sergio Flores
 */
public abstract class DTrnDfrPrinting {
    
    private static void createTempTablesCfdi33Crp10(final DGuiSession session, final int sessionId) throws Exception {
        String sql;

        sql = "CREATE TEMPORARY TABLE IF NOT EXISTS temp_pago ("
                + "id_pago SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT, "
                + "FechaPago CHAR(20), "
                + "FormaDePagoP CHAR(2), "
                + "MonedaP CHAR(3), "
                + "TipoCambioP DECIMAL(19, 4), "
                + "Monto DECIMAL(17, 2), "
                + "NumOperacion CHAR(100), "
                + "RfcEmisorCtaOrd CHAR(13), "
                + "NomBancoOrdExt VARCHAR(300), "
                + "CtaOrdenante VARCHAR(50), "
                + "RfcEmisorCtaBen VARCHAR(12), "
                + "CtaBeneficiario VARCHAR(50), "
                + "session_id INT UNSIGNED, "
                + "CONSTRAINT pk_temp_pago PRIMARY KEY (id_pago)) ENGINE=InnoDB;";
        session.getStatement().execute(sql);
        
        sql = "CREATE TEMPORARY TABLE IF NOT EXISTS temp_pago_docto ("
                + "id_docto SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT, "
                + "IdDocumento CHAR(36), "
                + "Serie CHAR(25), "
                + "Folio CHAR(40), "
                + "MonedaDR CHAR(3), "
                + "TipoCambioDR DECIMAL(19, 4), "
                + "NumParcialidad SMALLINT UNSIGNED, "
                + "ImpSaldoAnt DECIMAL(17, 2), "
                + "ImpPagado DECIMAL(17, 2), "
                + "ImpSaldoInsoluto DECIMAL(17, 2), "
                + "fk_pago SMALLINT UNSIGNED, "
                + "session_id INT UNSIGNED, "
                + "CONSTRAINT pk_temp_pago_docto PRIMARY KEY (id_docto)) ENGINE=InnoDB;";
        session.getStatement().execute(sql);

        sql = "DELETE FROM temp_pago WHERE session_id = " + sessionId + ";";
        session.getStatement().execute(sql);

        sql = "DELETE FROM temp_pago_docto WHERE session_id = " + sessionId + ";";
        session.getStatement().execute(sql);
    }
    
    /**
     * CFDI suported: CFDI 3.3 with CRP 1.0.
     * @param session
     * @param dfr
     * @return 
     * @throws java.lang.Exception 
     */
    @SuppressWarnings("deprecation")
    public static HashMap<String, Object> createPrintingMapCfdi33(final DGuiSession session, final DDbDfr dfr) throws Exception {
        cfd.ver33.DElementComprobante comprobante = DCfdUtils.getCfdi33(dfr.getSuitableDocXml());
        HashMap<String, Object> map = new HashMap<>();
        
        String sello;
        String rfcEmisor;
        String rfcReceptor;
        String uuid = "";
        int sessionId = session.getUser().getPkUserId();
        
        // CFDI data:
        
        map.put("sXmlBaseDir", ((DDbConfigBranch) session.getConfigBranch()).getDfrDirectory());
        map.put("nParamSessionId", sessionId);
        map.put("bIsAnnulled", dfr.getFkXmlStatusId() == DModSysConsts.TS_XML_ST_ANN);
        map.put("bIsDeleted", dfr.isDeleted());
        map.put("sCfdiVersion", "" + comprobante.getVersion());
        map.put("sCfdiSerie", comprobante.getAttSerie().getString());
        map.put("sCfdiFolio", comprobante.getAttFolio().getString());
        map.put("sCfdiFecha", DLibUtils.DateFormatDatetime.format(comprobante.getAttFecha().getDatetime()));
        map.put("sCfdiSello", sello = comprobante.getAttSello().getString());
        map.put("sCfdiNoCertificado", comprobante.getAttNoCertificado().getString());
        map.put("sCfdiCertificado", comprobante.getAttCertificado().getString());
        map.put("dCfdiSubTotal", comprobante.getAttSubTotal().getDouble());
        map.put("sCfdiMoneda", comprobante.getAttMoneda().getString());
        map.put("dCfdiTotal", comprobante.getAttTotal().getDouble());
        map.put("sCfdiTipoDeComprobante", comprobante.getAttTipoDeComprobante().getString());
        map.put("sCfdiLugarExpedicion", comprobante.getAttLugarExpedicion().getString());
        map.put("sCfdiConfirmacion", comprobante.getAttConfirmacion().getString());
        
        if (comprobante.getEltOpcCfdiRelacionados() != null) {
            cfd.ver33.DElementCfdiRelacionados cfdiRelacionados = comprobante.getEltOpcCfdiRelacionados();
            map.put("sCfdiRelTipoRelacion", cfdiRelacionados.getAttTipoRelacion().getString());
            map.put("sCfdiRelUUID", cfdiRelacionados.getEltCfdiRelacionados().get(0).getAttUuid().getString());
        }
        
        cfd.ver33.DElementEmisor emisor = comprobante.getEltEmisor();
        map.put("sEmiRfc", rfcEmisor = emisor.getAttRfc().getString());
        map.put("sEmiNombre", emisor.getAttNombre().getString());
        map.put("sEmiRegimenFiscal", emisor.getAttRegimenFiscal().getString());
        
        cfd.ver33.DElementReceptor receptor = comprobante.getEltReceptor();
        map.put("sRecRfc", rfcReceptor = receptor.getAttRfc().getString());
        map.put("sRecNombre", receptor.getAttNombre().getString());
        map.put("sRecResidenciaFiscal", receptor.getAttResidenciaFiscal().getString());
        map.put("sRecNumRegIdTrib", receptor.getAttNumRegIdTrib().getString());
        map.put("sRecUsoCFDI", receptor.getAttUsoCFDI().getString());
        
        if (comprobante.getEltOpcComplemento() != null) {
            cfd.ver33.DElementComplemento complemento = comprobante.getEltOpcComplemento();
            for (DElement element : complemento.getElements()) {
                if (element instanceof cfd.ver33.DElementTimbreFiscalDigital) {
                    cfd.ver33.DElementTimbreFiscalDigital tfd = (cfd.ver33.DElementTimbreFiscalDigital) element;
                    map.put("sTfdVersion", tfd.getAttVersion().getString());
                    map.put("sTfdUUID", uuid = tfd.getAttUUID().getString());
                    map.put("sTfdFechaTimbrado", tfd.getAttFechaTimbrado().getString());
                    map.put("sTfdRfcProvCertif", tfd.getAttRfcProvCertif().getString());
                    map.put("sTfdNoCertificadoSAT", tfd.getAttNoCertificadoSAT().getString());
                    map.put("sTfdSelloCFD", tfd.getAttSelloCFD().getString());
                    map.put("sTfdSelloSAT", tfd.getAttSelloSAT().getString());
                    map.put("sTfdLeyenda", tfd.getAttLeyenda().getString());
                    
                    break;
                }
            }
        }
        
        //map.put("sPagosVersion", );   // default-value of parameter will be used
        //map.put("oFormatInt", );      // default-value of parameter will be used
        //map.put("oFormatAmt", );      // default-value of parameter will be used
        //map.put("oFormatExr", );      // default-value of parameter will be used
        //map.put("sCfdi", );
        map.put("bHideVendor", true);
        
        // QR code:
        
        map.put("oCfdiQrCode", DCfd.createQrCodeBufferedImageCfdi33(uuid, rfcEmisor, rfcReceptor, 0, sello.isEmpty() ? DLibUtils.textRepeat("0", 8) : sello.substring(sello.length() - 8, sello.length())));
        
        // CRP 1.0 data:
        
        createTempTablesCfdi33Crp10(session, sessionId);
        
        if (comprobante.getEltOpcComplemento() != null) {
            for (DElement element : comprobante.getEltOpcComplemento().getElements()) {
                if (element instanceof cfd.ver33.crp10.DElementPagos) {
                    String sql;
                    ResultSet resultSet;
                    
                    cfd.ver33.crp10.DElementPagos pagos = (cfd.ver33.crp10.DElementPagos) element;
                    for (cfd.ver33.crp10.DElementPagosPago pago : pagos.getEltPagos()) {
                        sql = "INSERT INTO temp_pago VALUES ("
                                + "0, " // auto increment
                                + "'" + DLibUtils.DateFormatDatetime.format(pago.getAttFechaPago().getDatetime()) + "', "
                                + "'" + pago.getAttFormaDePagoP().getString()+ "', "
                                + "'" + pago.getAttMonedaP().getString()+ "', "
                                + pago.getAttTipoCambioP().getDouble() + ", "
                                + pago.getAttMonto().getDouble() + ", "
                                + "'" + pago.getAttNumOperacion().getString()+ "', "
                                + "'" + pago.getAttRfcEmisorCtaOrd().getString()+ "', "
                                + "'" + pago.getAttNomBancoOrdExt().getString()+ "', "
                                + "'" + pago.getAttCtaOrdenante().getString()+ "', "
                                + "'" + pago.getAttRfcEmisorCtaBen().getString()+ "', "
                                + "'" + pago.getAttCtaBeneficiario().getString()+ "', "
                                + sessionId + ");";
                        session.getStatement().execute(sql, Statement.RETURN_GENERATED_KEYS);
                        resultSet = session.getStatement().getGeneratedKeys();
                        if (resultSet.next()) {
                            int pagoId = resultSet.getInt(1);
                            
                            for (cfd.ver33.crp10.DElementDoctoRelacionado docto : pago.getEltDoctoRelacionados()) {
                                sql = "INSERT INTO temp_pago_docto VALUES ("
                                        + "0, " // auto increment
                                        + "'" + docto.getAttIdDocumento().getString() + "', "
                                        + "'" + docto.getAttSerie().getString() + "', "
                                        + "'" + docto.getAttFolio().getString() + "', "
                                        + "'" + docto.getAttMonedaDR().getString() + "', "
                                        + docto.getAttTipoCambioDR().getDouble() + ", "
                                        + docto.getAttNumParcialidad().getInteger()+ ", "
                                        + docto.getAttImpSaldoAnt().getDouble() + ", "
                                        + docto.getAttImpPagado().getDouble() + ", "
                                        + docto.getAttImpSaldoInsoluto().getDouble() + ", "
                                        + pagoId + ", "
                                        + sessionId + ");";
                                session.getStatement().execute(sql);
                            }
                        }
                    }
                    
                    break;
                }
            }
        }
        
        return map;
    }
    
    private static void createTempTablesCfdi40Crp20(final DGuiSession session, final int sessionId) throws Exception {
        String sql;

        sql = "CREATE TEMPORARY TABLE IF NOT EXISTS temp_pago_40_20 ("
                + "id_pago SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT, "
                + "FechaPago CHAR(20), "
                + "FormaDePagoP CHAR(2), "
                + "MonedaP CHAR(3), "
                + "TipoCambioP DECIMAL(19, 4), "
                + "Monto DECIMAL(17, 2), "
                + "NumOperacion CHAR(100), "
                + "RfcEmisorCtaOrd CHAR(13), "
                + "NomBancoOrdExt VARCHAR(300), "
                + "CtaOrdenante VARCHAR(50), "
                + "RfcEmisorCtaBen VARCHAR(12), "
                + "CtaBeneficiario VARCHAR(50), "
                + "RetencionesP TEXT, "
                + "TrasladosP TEXT, "
                + "session_id INT UNSIGNED, "
                + "CONSTRAINT pk_temp_pago_40_20 PRIMARY KEY (id_pago)) ENGINE=InnoDB;";
        session.getStatement().execute(sql);
        
        sql = "CREATE TEMPORARY TABLE IF NOT EXISTS temp_pago_40_20_docto ("
                + "id_docto SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT, "
                + "IdDocumento CHAR(36), "
                + "Serie CHAR(25), "
                + "Folio CHAR(40), "
                + "MonedaDR CHAR(3), "
                + "EquivalenciaDR DECIMAL(19, 4), "
                + "NumParcialidad SMALLINT UNSIGNED, "
                + "ImpSaldoAnt DECIMAL(17, 2), "
                + "ImpPagado DECIMAL(17, 2), "
                + "ImpSaldoInsoluto DECIMAL(17, 2), "
                + "ObjetoImpDR CHAR(2), "
                + "RetencionesDR TEXT, "
                + "TrasladosDR TEXT, "
                + "fk_pago SMALLINT UNSIGNED, "
                + "session_id INT UNSIGNED, "
                + "CONSTRAINT pk_temp_pago_40_20_docto PRIMARY KEY (id_docto)) ENGINE=InnoDB;";
        session.getStatement().execute(sql);

        sql = "DELETE FROM temp_pago_40_20 WHERE session_id = " + sessionId + ";";
        session.getStatement().execute(sql);

        sql = "DELETE FROM temp_pago_40_20_docto WHERE session_id = " + sessionId + ";";
        session.getStatement().execute(sql);
    }
    
    /**
     * CFDI suported: CFDI 3.3 with CRP 1.0.
     * @param session
     * @param dfr
     * @return 
     * @throws java.lang.Exception 
     */
    @SuppressWarnings("deprecation")
    public static HashMap<String, Object> createPrintingMapCfdi40(final DGuiSession session, final DDbDfr dfr) throws Exception {
        cfd.ver40.DElementComprobante comprobante = DCfdUtils.getCfdi40(dfr.getSuitableDocXml());
        HashMap<String, Object> map = new HashMap<>();
        
        String sello;
        String rfcEmisor;
        String rfcReceptor;
        String uuid = "";
        int sessionId = session.getUser().getPkUserId();
        
        // CFDI data:
        
        map.put("sXmlBaseDir", ((DDbConfigBranch) session.getConfigBranch()).getDfrDirectory());
        map.put("nParamSessionId", sessionId);
        map.put("bIsAnnulled", dfr.getFkXmlStatusId() == DModSysConsts.TS_XML_ST_ANN);
        map.put("bIsDeleted", dfr.isDeleted());
        map.put("sCfdiVersion", "" + comprobante.getVersion());
        map.put("sCfdiSerie", comprobante.getAttSerie().getString());
        map.put("sCfdiFolio", comprobante.getAttFolio().getString());
        map.put("sCfdiFecha", DLibUtils.DateFormatDatetime.format(comprobante.getAttFecha().getDatetime()));
        map.put("sCfdiSello", sello = comprobante.getAttSello().getString());
        map.put("sCfdiNoCertificado", comprobante.getAttNoCertificado().getString());
        map.put("sCfdiCertificado", comprobante.getAttCertificado().getString());
        map.put("dCfdiSubTotal", comprobante.getAttSubTotal().getDouble());
        map.put("sCfdiMoneda", comprobante.getAttMoneda().getString());
        map.put("dCfdiTotal", comprobante.getAttTotal().getDouble());
        map.put("sCfdiTipoDeComprobante", comprobante.getAttTipoDeComprobante().getString());
        map.put("sCfdiLugarExpedicion", comprobante.getAttLugarExpedicion().getString());
        map.put("sCfdiConfirmacion", comprobante.getAttConfirmacion().getString());
        
        if (comprobante.getEltOpcCfdiRelacionados() != null) {
            String xmlCfdiRelacionados = "";
            
            for (cfd.ver40.DElementCfdiRelacionados cfdiRelacionados : comprobante.getEltOpcCfdiRelacionados()) {
                xmlCfdiRelacionados += (xmlCfdiRelacionados.isEmpty() ? "" : "/ ") + "TipoRelacion: "
                        + DTrnDfrCatalogs.composeCatalogEntry(session.getClient(), DCfdi40Catalogs.CAT_REL_TP, cfdiRelacionados.getAttTipoRelacion().getString());
                
                String uuids = "";
                for (cfd.ver40.DElementCfdiRelacionado cfdiRelacionado : cfdiRelacionados.getEltCfdiRelacionados()) {
                    uuids += (uuids.isEmpty() ? "" : ", ") + cfdiRelacionado.getAttUuid().getString();
                }
                xmlCfdiRelacionados += "; UUID: " + uuids;
            }
            
            map.put("sCfdiRelacionados", xmlCfdiRelacionados);
        }
        
        cfd.ver40.DElementEmisor emisor = comprobante.getEltEmisor();
        map.put("sEmiRfc", rfcEmisor = emisor.getAttRfc().getString());
        map.put("sEmiNombre", emisor.getAttNombre().getString());
        map.put("sEmiRegimenFiscal", DTrnDfrCatalogs.composeCatalogEntry(session.getClient(), DCfdi40Catalogs.CAT_REG_FISC, emisor.getAttRegimenFiscal().getString()));
        
        cfd.ver40.DElementReceptor receptor = comprobante.getEltReceptor();
        map.put("sRecRfc", rfcReceptor = receptor.getAttRfc().getString());
        map.put("sRecNombre", receptor.getAttNombre().getString());
        map.put("sRecRegimenFiscal", DTrnDfrCatalogs.composeCatalogEntry(session.getClient(), DCfdi40Catalogs.CAT_REG_FISC, receptor.getAttRegimenFiscalReceptor().getString()));
        map.put("sRecDomicilioFiscal", receptor.getAttDomicilioFiscalReceptor().getString());
        map.put("sRecUsoCFDI", DTrnDfrCatalogs.composeCatalogEntry(session.getClient(), DCfdi40Catalogs.CAT_CFDI_USO, receptor.getAttUsoCFDI().getString()));
        map.put("sRecResidenciaFiscal", receptor.getAttResidenciaFiscal().getString());
        map.put("sRecNumRegIdTrib", receptor.getAttNumRegIdTrib().getString());
        
        if (comprobante.getEltOpcComplemento() != null) {
            cfd.ver40.DElementComplemento complemento = comprobante.getEltOpcComplemento();
            for (DElement element : complemento.getElements()) {
                if (element instanceof cfd.ver40.DElementTimbreFiscalDigital) {
                    cfd.ver40.DElementTimbreFiscalDigital tfd = (cfd.ver40.DElementTimbreFiscalDigital) element;
                    map.put("sTfdVersion", tfd.getAttVersion().getString());
                    map.put("sTfdUUID", uuid = tfd.getAttUUID().getString());
                    map.put("sTfdFechaTimbrado", tfd.getAttFechaTimbrado().getString());
                    map.put("sTfdRfcProvCertif", tfd.getAttRfcProvCertif().getString());
                    map.put("sTfdNoCertificadoSAT", tfd.getAttNoCertificadoSAT().getString());
                    map.put("sTfdSelloCFD", tfd.getAttSelloCFD().getString());
                    map.put("sTfdSelloSAT", tfd.getAttSelloSAT().getString());
                    map.put("sTfdLeyenda", tfd.getAttLeyenda().getString());
                    
                    break;
                }
            }
        }
        
        //map.put("sPagosVersion", );   // default-value of parameter will be used
        //map.put("oFormatInt", );      // default-value of parameter will be used
        //map.put("oFormatAmt", );      // default-value of parameter will be used
        //map.put("oFormatExr", );      // default-value of parameter will be used
        //map.put("sCfdi", );
        map.put("bHideVendor", true);
        
        // QR code:
        
        map.put("oCfdiQrCode", DCfd.createQrCodeBufferedImageCfdi40(uuid, rfcEmisor, rfcReceptor, 0, sello.isEmpty() ? DLibUtils.textRepeat("0", 8) : sello.substring(sello.length() - 8, sello.length())));
        
        // CRP 1.0 data:
        
        createTempTablesCfdi40Crp20(session, sessionId);
        
        if (comprobante.getEltOpcComplemento() != null) {
            for (DElement element : comprobante.getEltOpcComplemento().getElements()) {
                if (element instanceof cfd.ver40.crp20.DElementPagos) {
                    cfd.ver40.crp20.DElementPagos pagos = (cfd.ver40.crp20.DElementPagos) element;
                    
                    String htmlTotales = "";
                    String htmlSeparator = "&nbsp;&nbsp;&nbsp;";
                    cfd.ver40.crp20.DElementTotales totales = pagos.getEltTotales();
                    
                    if (totales.getAttTotalRetencionesIVA().getDouble() != 0) {
                        htmlTotales += (htmlTotales.isEmpty() ? "" : htmlSeparator) + "TotalRetencionesIVA: " + DLibUtils.getDecimalFormatAmount().format(totales.getAttTotalRetencionesIVA().getDouble());
                    }
                    
                    if (totales.getAttTotalRetencionesISR().getDouble() != 0) {
                        htmlTotales += (htmlTotales.isEmpty() ? "" : htmlSeparator) + "TotalRetencionesISR: " + DLibUtils.getDecimalFormatAmount().format(totales.getAttTotalRetencionesISR().getDouble());
                    }
                    
                    if (totales.getAttTotalRetencionesIEPS().getDouble() != 0) {
                        htmlTotales += (htmlTotales.isEmpty() ? "" : htmlSeparator) + "TotalRetencionesIEPS: " + DLibUtils.getDecimalFormatAmount().format(totales.getAttTotalRetencionesIEPS().getDouble());
                    }
                    
                    if (totales.getAttTotalTrasladosBaseIVA16().getDouble() != 0) {
                        htmlTotales += (htmlTotales.isEmpty() ? "" : htmlSeparator) + "TotalTrasladosBaseIVA16: " + DLibUtils.getDecimalFormatAmount().format(totales.getAttTotalTrasladosBaseIVA16().getDouble())
                                + " TotalTrasladosImpuestoIVA16: " + DLibUtils.getDecimalFormatAmount().format(totales.getAttTotalTrasladosImpuestoIVA16().getDouble());
                    }
                    
                    if (totales.getAttTotalTrasladosBaseIVA8().getDouble() != 0) {
                        htmlTotales += (htmlTotales.isEmpty() ? "" : htmlSeparator) + "TotalTrasladosBaseIVA8: " + DLibUtils.getDecimalFormatAmount().format(totales.getAttTotalTrasladosBaseIVA8().getDouble())
                                + " TotalTrasladosImpuestoIVA8: " + DLibUtils.getDecimalFormatAmount().format(totales.getAttTotalTrasladosImpuestoIVA8().getDouble());
                    }
                    
                    if (totales.getAttTotalTrasladosBaseIVA0().getDouble() != 0) {
                        htmlTotales += (htmlTotales.isEmpty() ? "" : htmlSeparator) + "TotalTrasladosBaseIVA0: " + DLibUtils.getDecimalFormatAmount().format(totales.getAttTotalTrasladosBaseIVA0().getDouble())
                                + " TotalTrasladosImpuestoIVA0: " + DLibUtils.getDecimalFormatAmount().format(totales.getAttTotalTrasladosImpuestoIVA0().getDouble());
                    }
                    
                    if (totales.getAttTotalTrasladosBaseIVAExento().getDouble() != 0) {
                        htmlTotales += (htmlTotales.isEmpty() ? "" : htmlSeparator) + "TotalTrasladosBaseIVAExento: " + DLibUtils.getDecimalFormatAmount().format(totales.getAttTotalTrasladosBaseIVAExento().getDouble());
                    }
                    
                    htmlTotales += (htmlTotales.isEmpty() ? "" : htmlSeparator) + "MontoTotalPagos: " + DLibUtils.getDecimalFormatAmount().format(totales.getAttMontoTotalPagos().getDouble());
                    htmlTotales = "<br>" + htmlTotales;
                    
                    map.put("sPagosTotales", htmlTotales);
                    
                    String sql;
                    ResultSet resultSet;
                    
                    for (cfd.ver40.crp20.DElementPagosPago pago : pagos.getEltPagos()) {
                        int retencionesP = 0;
                        int trasladosP = 0;
                        String htmlRetencionesP = "";
                        String htmlTrasladosP = "";

                        if (pago.getEltImpuestosP() != null) {
                            // retenciones documento relacionado:

                            if (pago.getEltImpuestosP().getEltRetencionesP()!= null) {
                                for (cfd.ver40.crp20.DElementRetencionP retencionP : pago.getEltImpuestosP().getEltRetencionesP().getEltRetencionPs()) {
                                    retencionesP++;
                                    htmlRetencionesP += "<br>"
                                            + "- <i>RetencionP:</i> "
                                            + "ImpuestoP: " + DTrnDfrCatalogs.composeCatalogEntry(session.getClient(), DCfdi40Catalogs.CAT_IMP, retencionP.getAttImpuestoP().getString())
                                            + ", ImporteP: " + DLibUtils.getDecimalFormatAmount().format(retencionP.getAttImporteP().getDouble());
                                }
                            }

                            if (retencionesP > 0) {
                                htmlRetencionesP = "<b>RetencionesP:</b>" + htmlRetencionesP;
                            }

                            // traslados documento relacionado:

                            if (pago.getEltImpuestosP().getEltTrasladosP()!= null) {
                                for (cfd.ver40.crp20.DElementTrasladoP trasladoP : pago.getEltImpuestosP().getEltTrasladosP().getEltTrasladoPs()) {
                                    trasladosP++;
                                    htmlTrasladosP += "<br>"
                                            + "- <i>TrasladoP:</i> "
                                            + "BaseP: " + DLibUtils.getDecimalFormatAmount().format(trasladoP.getAttBaseP().getDouble())
                                            + ", ImpuestoP: " + DTrnDfrCatalogs.composeCatalogEntry(session.getClient(), DCfdi40Catalogs.CAT_IMP, trasladoP.getAttImpuestoP().getString())
                                            + ", TipoFactorP: " + trasladoP.getAttTipoFactorP().getString()
                                            + ", TasaOCuotaP: " + DLibUtils.DecimalFormatValue6D.format(trasladoP.getAttTasaOCuotaP().getDouble())
                                            + ", ImporteP: " + DLibUtils.getDecimalFormatAmount().format(trasladoP.getAttImporteP().getDouble());
                                }
                            }

                            if (trasladosP > 0) {
                                htmlTrasladosP = "<b>TrasladosP:</b>" + htmlTrasladosP;
                            }
                        }
                        
                        sql = "INSERT INTO temp_pago_40_20 VALUES ("
                                + "0," // auto increment
                                + "'" + DLibUtils.DateFormatDatetime.format(pago.getAttFechaPago().getDatetime()) + "', "
                                + "'" + pago.getAttFormaDePagoP().getString()+ "', "
                                + "'" + pago.getAttMonedaP().getString()+ "', "
                                + pago.getAttTipoCambioP().getDouble() + ", "
                                + pago.getAttMonto().getDouble() + ", "
                                + "'" + pago.getAttNumOperacion().getString()+ "', "
                                + "'" + pago.getAttRfcEmisorCtaOrd().getString()+ "', "
                                + "'" + pago.getAttNomBancoOrdExt().getString()+ "', "
                                + "'" + pago.getAttCtaOrdenante().getString()+ "', "
                                + "'" + pago.getAttRfcEmisorCtaBen().getString()+ "', "
                                + "'" + pago.getAttCtaBeneficiario().getString()+ "', "
                                + "'" + htmlRetencionesP + "', "
                                + "'" + htmlTrasladosP + "', "
                                + sessionId + ");";
                        session.getStatement().execute(sql, Statement.RETURN_GENERATED_KEYS);
                        resultSet = session.getStatement().getGeneratedKeys();
                        if (resultSet.next()) {
                            int pagoId = resultSet.getInt(1);
                            
                            for (cfd.ver40.crp20.DElementDoctoRelacionado docto : pago.getEltDoctoRelacionados()) {
                                int retencionesDr = 0;
                                int trasladosDr = 0;
                                String htmlRetencionesDr = "";
                                String htmlTrasladosDr = "";
                                
                                if (docto.getEltImpuestosDR() != null) {
                                    // retenciones documento relacionado:
                                    
                                    if (docto.getEltImpuestosDR().getEltRetencionesDR() != null) {
                                        for (cfd.ver40.crp20.DElementRetencionDR retencionDR : docto.getEltImpuestosDR().getEltRetencionesDR().getEltRetencionDRs()) {
                                            retencionesDr++;
                                            htmlRetencionesDr += "<br>"
                                                + "- <i>RetencionDR:</i> "
                                                + "BaseDR: " + DLibUtils.getDecimalFormatAmount().format(retencionDR.getAttBaseDR().getDouble())
                                                + ", ImpuestoDR: " + DTrnDfrCatalogs.composeCatalogEntry(session.getClient(), DCfdi40Catalogs.CAT_IMP, retencionDR.getAttImpuestoDR().getString())
                                                + ", TipoFactorDR: " + retencionDR.getAttTipoFactorDR().getString()
                                                + ", TasaOCuotaDR: " + DLibUtils.DecimalFormatValue6D.format(retencionDR.getAttTasaOCuotaDR().getDouble())
                                                + ", ImporteDR: " + DLibUtils.getDecimalFormatAmount().format(retencionDR.getAttImporteDR().getDouble());
                                        }
                                    }
                                    
                                    if (retencionesDr > 0) {
                                        htmlRetencionesDr = "<b>RetencionesDR:</b>" + htmlRetencionesDr;
                                    }
                                    
                                    // traslados documento relacionado:

                                    if (docto.getEltImpuestosDR().getEltTrasladosDR()!= null) {
                                        for (cfd.ver40.crp20.DElementTrasladoDR trasladoDR : docto.getEltImpuestosDR().getEltTrasladosDR().getEltTrasladoDRs()) {
                                            trasladosDr++;
                                            htmlTrasladosDr += "<br>"
                                                + "- <i>TrasladoDR:</i> "
                                                + "BaseDR: " + DLibUtils.getDecimalFormatAmount().format(trasladoDR.getAttBaseDR().getDouble())
                                                + ", ImpuestoDR: " + DTrnDfrCatalogs.composeCatalogEntry(session.getClient(), DCfdi40Catalogs.CAT_IMP, trasladoDR.getAttImpuestoDR().getString())
                                                + ", TipoFactorDR: " + trasladoDR.getAttTipoFactorDR().getString()
                                                + ", TasaOCuotaDR: " + DLibUtils.DecimalFormatValue6D.format(trasladoDR.getAttTasaOCuotaDR().getDouble())
                                                + ", ImporteDR: " + DLibUtils.getDecimalFormatAmount().format(trasladoDR.getAttImporteDR().getDouble());
                                        }
                                    }

                                    if (trasladosDr > 0) {
                                        htmlTrasladosDr = "<b>TrasladosDR:</b>" + htmlTrasladosDr;
                                    }
                                }
                                
                                sql = "INSERT INTO temp_pago_40_20_docto VALUES ("
                                        + "0, " // auto increment
                                        + "'" + docto.getAttIdDocumento().getString() + "', "
                                        + "'" + docto.getAttSerie().getString() + "', "
                                        + "'" + docto.getAttFolio().getString() + "', "
                                        + "'" + docto.getAttMonedaDR().getString() + "', "
                                        + docto.getAttEquivalenciaDR().getDouble() + ", "
                                        + docto.getAttNumParcialidad().getInteger()+ ", "
                                        + docto.getAttImpSaldoAnt().getDouble() + ", "
                                        + docto.getAttImpPagado().getDouble() + ", "
                                        + docto.getAttImpSaldoInsoluto().getDouble() + ", "
                                        + "'" + (trasladosDr > 0 ? DCfdi40Catalogs.ClaveObjetoImpSí : DCfdi40Catalogs.ClaveObjetoImpNo) + "', "
                                        + "'" + htmlRetencionesDr + "', "
                                        + "'" + htmlTrasladosDr + "', "
                                        + pagoId + ", "
                                        + sessionId + ");";
                                session.getStatement().execute(sql);
                            }
                        }
                    }
                    
                    break;
                }
            }
        }
        
        return map;
    }
}
