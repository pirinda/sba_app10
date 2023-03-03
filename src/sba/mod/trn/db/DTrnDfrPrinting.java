/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.trn.db;

import cfd.DCfd;
import cfd.DCfdUtils;
import cfd.DElement;
import cfd.ver33.DElementCfdiRelacionados;
import cfd.ver33.DElementComplemento;
import cfd.ver33.DElementComprobante;
import cfd.ver33.DElementEmisor;
import cfd.ver33.DElementReceptor;
import cfd.ver33.DElementTimbreFiscalDigital;
import cfd.ver33.crp10.DElementPagos;
import cfd.ver33.crp10.DElementPagosPago;
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
                + "id_pago SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,"
                + "FechaPago CHAR(20),"
                + "FormaDePagoP CHAR(2),"
                + "MonedaP CHAR(3),"
                + "TipoCambioP DECIMAL(19, 4),"
                + "Monto DECIMAL(17, 2),"
                + "NumOperacion CHAR(100),"
                + "RfcEmisorCtaOrd CHAR(13),"
                + "NomBancoOrdExt VARCHAR(300),"
                + "CtaOrdenante VARCHAR(50),"
                + "RfcEmisorCtaBen VARCHAR(12),"
                + "CtaBeneficiario VARCHAR(50),"
                + "session_id INT UNSIGNED,"
                + "CONSTRAINT pk_temp_pago PRIMARY KEY (id_pago)) ENGINE=InnoDB;";
        session.getStatement().execute(sql);
        sql = "CREATE TEMPORARY TABLE IF NOT EXISTS temp_pago_docto ("
                + "id_docto SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,"
                + "IdDocumento CHAR(36),"
                + "Serie CHAR(25),"
                + "Folio CHAR(40),"
                + "MonedaDR CHAR(3),"
                + "TipoCambioDR DECIMAL(19, 4),"
                + "NumParcialidad SMALLINT UNSIGNED,"
                + "ImpSaldoAnt DECIMAL(17, 2),"
                + "ImpPagado DECIMAL(17, 2),"
                + "ImpSaldoInsoluto DECIMAL(17, 2),"
                + "fk_pago SMALLINT UNSIGNED,"
                + "session_id INT UNSIGNED,"
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
    public static HashMap<String, Object> createPrintingMap(final DGuiSession session, final DDbDfr dfr) throws Exception {
        DElementComprobante comprobante = DCfdUtils.getCfdi33(dfr.getSuitableDocXml());
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
            DElementCfdiRelacionados cfdiRelacionados = comprobante.getEltOpcCfdiRelacionados();
            map.put("sCfdiRelTipoRelacion", cfdiRelacionados.getAttTipoRelacion().getString());
            map.put("sCfdiRelUUID", cfdiRelacionados.getEltCfdiRelacionados().get(0).getAttUuid().getString());
        }
        
        DElementEmisor emisor = comprobante.getEltEmisor();
        map.put("sEmiRfc", rfcEmisor = emisor.getAttRfc().getString());
        map.put("sEmiNombre", emisor.getAttNombre().getString());
        map.put("sEmiRegimenFiscal", emisor.getAttRegimenFiscal().getString());
        
        DElementReceptor receptor = comprobante.getEltReceptor();
        map.put("sRecRfc", rfcReceptor = receptor.getAttRfc().getString());
        map.put("sRecNombre", receptor.getAttNombre().getString());
        map.put("sRecResidenciaFiscal", receptor.getAttResidenciaFiscal().getString());
        map.put("sRecNumRegIdTrib", receptor.getAttNumRegIdTrib().getString());
        map.put("sRecUsoCFDI", receptor.getAttUsoCFDI().getString());
        
        if (comprobante.getEltOpcComplemento() != null) {
            DElementComplemento complemento = comprobante.getEltOpcComplemento();
            for (DElement element : complemento.getElements()) {
                if (element instanceof DElementTimbreFiscalDigital) {
                    DElementTimbreFiscalDigital tfd = (DElementTimbreFiscalDigital) element;
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
                if (element instanceof DElementPagos) {
                    String sql;
                    ResultSet resultSet;
                    
                    DElementPagos pagos = (DElementPagos) element;
                    for (DElementPagosPago pago : pagos.getEltPagos()) {
                        int pagoId;
                        
                        sql = "INSERT INTO temp_pago VALUES ("
                                + "0," // auto increment
                                + "'" + DLibUtils.DateFormatDatetime.format(pago.getAttFechaPago().getDatetime()) + "',"
                                + "'" + pago.getAttFormaDePagoP().getString()+ "',"
                                + "'" + pago.getAttMonedaP().getString()+ "',"
                                + pago.getAttTipoCambioP().getDouble() + ","
                                + pago.getAttMonto().getDouble() + ","
                                + "'" + pago.getAttNumOperacion().getString()+ "',"
                                + "'" + pago.getAttRfcEmisorCtaOrd().getString()+ "',"
                                + "'" + pago.getAttNomBancoOrdExt().getString()+ "',"
                                + "'" + pago.getAttCtaOrdenante().getString()+ "',"
                                + "'" + pago.getAttRfcEmisorCtaBen().getString()+ "',"
                                + "'" + pago.getAttCtaBeneficiario().getString()+ "',"
                                + sessionId + ");";
                        session.getStatement().execute(sql, Statement.RETURN_GENERATED_KEYS);
                        resultSet = session.getStatement().getGeneratedKeys();
                        if (resultSet.next()) {
                            pagoId = resultSet.getInt(1);
                            
                            for (cfd.ver33.crp10.DElementDoctoRelacionado docto : pago.getEltDoctoRelacionados()) {
                                sql = "INSERT INTO temp_pago_docto VALUES ("
                                        + "0," // auto increment
                                        + "'" + docto.getAttIdDocumento().getString() + "',"
                                        + "'" + docto.getAttSerie().getString() + "',"
                                        + "'" + docto.getAttFolio().getString() + "',"
                                        + "'" + docto.getAttMonedaDR().getString() + "',"
                                        + docto.getAttTipoCambioDR().getDouble() + ","
                                        + docto.getAttNumParcialidad().getInteger()+ ","
                                        + docto.getAttImpSaldoAnt().getDouble() + ","
                                        + docto.getAttImpPagado().getDouble() + ","
                                        + docto.getAttImpSaldoInsoluto().getDouble() + ","
                                        + pagoId + ","
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
