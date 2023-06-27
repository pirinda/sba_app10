/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.trn.db;

import cfd.ver40.DCfdi40Catalogs;
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
public abstract class DTrnDfrCatalogs {
    
    private static String getDatabaseCatalogEntry(final DGuiSession session, final int table, final String code) throws Exception {
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
            case DCfdi40Catalogs.CAT_CFDI_TP:
                entry = code + " - " + DCfdi40Catalogs.TipoComprobante.get(code);
                break;
            case DCfdi40Catalogs.CAT_CFDI_USO:
                xmlCatalog = ((DGuiClientApp) client).getXmlCatalogsMap().get(catalog);
                entry = xmlCatalog.composeCodeName(xmlCatalog.getId(code));
                break;
            case DCfdi40Catalogs.CAT_REG_FISC:
                entry = getDatabaseCatalogEntry(client.getSession(), DModConsts.CS_TAX_REG, code);
                break;
            case DCfdi40Catalogs.CAT_MDP:
                xmlCatalog = ((DGuiClientApp) client).getXmlCatalogsMap().get(catalog);
                entry = xmlCatalog.composeCodeName(xmlCatalog.getId(code));
                break;
            case DCfdi40Catalogs.CAT_FDP:
                entry = getDatabaseCatalogEntry(client.getSession(), DModConsts.FS_MOP_TP, code);
                break;
            case DCfdi40Catalogs.CAT_MON:
                entry = getDatabaseCatalogEntry(client.getSession(), DModConsts.CS_CUR, code);
                break;
            case DCfdi40Catalogs.CAT_PAIS:
                entry = getDatabaseCatalogEntry(client.getSession(), DModConsts.CS_CTY, code);
                break;
            case DCfdi40Catalogs.CAT_IMP:
                entry = code + " - " + DCfdi40Catalogs.Impuesto.get(code);
                break;
            case DCfdi40Catalogs.CAT_FAC_TP:
                entry = code;
                break;
            case DCfdi40Catalogs.CAT_REL_TP:
                entry = code + " - " + DCfdi40Catalogs.TipoRelación.get(code);
                break;
            case DCfdi40Catalogs.CAT_EXP:
                entry = code + " - " + DCfdi40Catalogs.Exportación.get(code);
                break;
            case DCfdi40Catalogs.CAT_GBL_PER:
                xmlCatalog = ((DGuiClientApp) client).getXmlCatalogsMap().get(catalog);
                entry = xmlCatalog.composeCodeName(xmlCatalog.getId(code));
                break;
            case DCfdi40Catalogs.CAT_GBL_MES:
                xmlCatalog = ((DGuiClientApp) client).getXmlCatalogsMap().get(catalog);
                entry = xmlCatalog.composeCodeName(xmlCatalog.getId(code));
                break;
            case DCfdi40Catalogs.CAT_OBJ_IMP:
                entry = code + " - " + DCfdi40Catalogs.ObjetoImp.get(code);
            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        return entry;
    }
}
