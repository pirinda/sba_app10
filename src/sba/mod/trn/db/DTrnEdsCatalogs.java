/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.trn.db;

import cfd.ver33.DCfdi33Catalogs;
import java.sql.ResultSet;
import sba.gui.DGuiClientApp;
import sba.gui.cat.DXmlCatalog;
import sba.lib.DLibConsts;
import sba.lib.db.DDbConsts;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public abstract class DTrnEdsCatalogs {
    
    private static String getCatalogEntry(final DGuiSession session, final int table, final String code) throws Exception {
        String entry = "";
        
        String sql = "SELECT CONCAT(code, ' - ', name) FROM " + DModConsts.TablesMap.get(table) + " WHERE NOT b_del AND code = '" + code + "';";
        ResultSet resultSet = session.getStatement().executeQuery(sql);
        if (!resultSet.next()) {
            throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            entry = resultSet.getString(1);
        }
        
        return entry;
    }
    
    /**
     * Composes catalog entry in format: code - name
     * @param client GUI client.
     * @param catalog Desired catalog.
     * @param code Code of desired catalog entry.
     * @return Catalog entry in format: code - name
     * @throws Exception 
     */
    public static String composeCatalogEntry(final DGuiClient client, final int catalog, final String code) throws Exception {
        String entry = "";
        DXmlCatalog xmlCatalog;
        
        switch (catalog) {
            case DCfdi33Catalogs.CAT_CFDI_TP:
                entry = code + " - " + DCfdi33Catalogs.TipoComprobante.get(code);
                break;
            case DCfdi33Catalogs.CAT_CFDI_USO:
                xmlCatalog = ((DGuiClientApp) client).getXmlCatalogsMap().get(catalog);
                entry = xmlCatalog.composeCodeName(xmlCatalog.getId(code));
                break;
            case DCfdi33Catalogs.CAT_REG_FISC:
                entry = getCatalogEntry(client.getSession(), DModConsts.CS_TAX_REG, code);
                break;
            case DCfdi33Catalogs.CAT_MDP:
                xmlCatalog = ((DGuiClientApp) client).getXmlCatalogsMap().get(catalog);
                entry = xmlCatalog.composeCodeName(xmlCatalog.getId(code));
                break;
            case DCfdi33Catalogs.CAT_FDP:
                entry = getCatalogEntry(client.getSession(), DModConsts.FS_MOP_TP, code);
                break;
            case DCfdi33Catalogs.CAT_MON:
                entry = getCatalogEntry(client.getSession(), DModConsts.CS_CUR, code);
                break;
            case DCfdi33Catalogs.CAT_PAIS:
                entry = getCatalogEntry(client.getSession(), DModConsts.CS_CTY, code);
                break;
            case DCfdi33Catalogs.CAT_IMP:
                entry = code + " - " + DCfdi33Catalogs.Impuesto.get(code);
                break;
            case DCfdi33Catalogs.CAT_FAC_TP:
                entry = code;
                break;
            case DCfdi33Catalogs.CAT_REL_TP:
                xmlCatalog = ((DGuiClientApp) client).getXmlCatalogsMap().get(catalog);
                entry = xmlCatalog.composeCodeName(xmlCatalog.getId(code));
                break;
            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        return entry;
    }
}
