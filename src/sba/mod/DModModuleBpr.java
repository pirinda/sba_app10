/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod;

import javax.swing.JMenu;
import sba.lib.DLibConsts;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.db.DDbRegistrySysFly;
import sba.lib.grid.DGridPaneView;
import sba.lib.gui.DGuiCatalogueSettings;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiForm;
import sba.lib.gui.DGuiModule;
import sba.lib.gui.DGuiOptionPicker;
import sba.lib.gui.DGuiParams;
import sba.lib.gui.DGuiReport;
import sba.mod.bpr.db.DBprUtils;
import sba.mod.bpr.db.DDbBizPartner;
import sba.mod.bpr.db.DDbBizPartnerConfig;
import sba.mod.bpr.db.DDbBizPartnerType;
import sba.mod.bpr.db.DDbBranch;
import sba.mod.bpr.db.DDbBranchAddress;
import sba.mod.bpr.db.DDbBranchBankAccount;
import sba.mod.bpr.form.DFormBizPartner;
import sba.mod.bpr.form.DFormBizPartnerType;
import sba.mod.bpr.view.DViewBizPartner;
import sba.mod.bpr.view.DViewBizPartnerConfig;
import sba.mod.bpr.view.DViewBizPartnerType;
import sba.mod.bpr.view.DViewBranch;
import sba.mod.bpr.view.DViewBranchAddress;

/**
 *
 * @author Sergio Flores
 */
public class DModModuleBpr extends DGuiModule {

    private DFormBizPartnerType moFormBizPartnerTypeVen;
    private DFormBizPartnerType moFormBizPartnerTypeCus;
    private DFormBizPartnerType moFormBizPartnerTypeCdr;
    private DFormBizPartnerType moFormBizPartnerTypeDbr;
    private DFormBizPartner moFormBizPartnerVen;
    private DFormBizPartner moFormBizPartnerCus;
    private DFormBizPartner moFormBizPartnerCdr;
    private DFormBizPartner moFormBizPartnerDbr;

    public DModModuleBpr(DGuiClient client) {
        super(client, DModConsts.MOD_BPR, DLibConsts.UNDEFINED);
        initComponents();
    }

    private void initComponents() {

    }

    @Override
    public JMenu[] getMenus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DDbRegistry getRegistry(final int type, final DGuiParams params) {
        DDbRegistry registry = null;

        switch (type) {
            case DModConsts.BS_BPR_CL:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_bpr_cl = " + pk[0] + " "; }
                };
                break;
            case DModConsts.BS_IDY_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_idy_tp = " + pk[0] + " "; }
                };
                break;
            case DModConsts.BS_BAF_TP:
                break;
            case DModConsts.BS_CDT_TP:
                break;
            case DModConsts.BS_TCD_TP:
                break;
            case DModConsts.BS_TCE_TP:
                break;
            case DModConsts.BS_BNK_TP:
                break;
            case DModConsts.BS_LNK_BPR_TP:
                break;
            case DModConsts.BU_BPR_TP:
                registry = new DDbBizPartnerType();
                break;
            case DModConsts.BU_BPR:
                registry = new DDbBizPartner();
                break;
            case DModConsts.BU_BPR_CFG:
                registry = new DDbBizPartnerConfig();
                break;
            case DModConsts.BU_BRA:
                registry = new DDbBranch();
                break;
            case DModConsts.BU_ADD:
                registry = new DDbBranchAddress();
                break;
            case DModConsts.BU_BNK:
                registry = new DDbBranchBankAccount();
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return registry;
    }

    @Override
    public DGuiCatalogueSettings getCatalogueSettings(final int type, final int subtype, final DGuiParams params) {
        String sql = "";
        DGuiCatalogueSettings settings = null;

        switch (type) {
            case DModConsts.BS_BPR_CL:
                settings = new DGuiCatalogueSettings("Clase asociado negocios", 1);
                sql = "SELECT id_bpr_cl AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.BS_IDY_TP:
                settings = new DGuiCatalogueSettings("Tipo persona", 1);
                sql = "SELECT id_idy_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.BS_BAF_TP:
                settings = new DGuiCatalogueSettings("Tipo formato domicilio", 1);
                sql = "SELECT id_baf_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.BS_CDT_TP:
                settings = new DGuiCatalogueSettings("Tipo crédito", 1);
                sql = "SELECT id_cdt_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.BS_TCD_TP:
                settings = new DGuiCatalogueSettings("Tipo equipo", 1);
                sql = "SELECT id_tcd_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.BS_TCE_TP:
                settings = new DGuiCatalogueSettings("Tipo cuenta", 1);
                sql = "SELECT id_tce_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.BS_BNK_TP:
                settings = new DGuiCatalogueSettings("Tipo cuenta bancaria", 1);
                sql = "SELECT id_bnk_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.BS_LNK_BPR_TP:
                settings = new DGuiCatalogueSettings("Tipo referencia", 1);
                sql = "SELECT id_lnk_bpr_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.BU_BPR_TP:
                settings = new DGuiCatalogueSettings("Tipo " + DBprUtils.getBizPartnerClassNameSng(subtype).toLowerCase(), 2);
                sql = "SELECT id_bpr_cl AS " + DDbConsts.FIELD_ID + "1, id_bpr_tp AS " + DDbConsts.FIELD_ID + "2, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_dis = 0 AND b_del = 0 AND id_bpr_cl = " + subtype + " " +
                        "ORDER BY name, id_bpr_cl, id_bpr_tp ";
                break;
            case DModConsts.BU_BPR:
                settings = new DGuiCatalogueSettings(DBprUtils.getBizPartnerClassNameSng(subtype), 1);
                sql = "SELECT id_bpr AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_dis = 0 AND b_del = 0 ";
                switch (subtype) {
                    case DModSysConsts.BS_BPR_CL_VEN:
                        sql += "AND b_ven = 1 ";
                        break;
                    case DModSysConsts.BS_BPR_CL_CUS:
                        sql += "AND b_cus = 1 ";
                        break;
                    case DModSysConsts.BS_BPR_CL_CDR:
                        sql += "AND b_cdr = 1 ";
                        break;
                    case DModSysConsts.BS_BPR_CL_DBR:
                        sql += "AND b_dbr = 1 ";
                        break;
                    default:
                }
                sql += "ORDER BY name, id_bpr ";
                break;
            case DModConsts.BU_BPR_CFG:
                break;
            case DModConsts.BU_BRA:
                settings = new DGuiCatalogueSettings("Sucursal", 2, 1);
                sql = "SELECT id_bpr AS " + DDbConsts.FIELD_ID + "1, id_bra AS " + DDbConsts.FIELD_ID + "2, " +
                        "id_bpr AS " + DDbConsts.FIELD_FK + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_dis = 0 AND b_del = 0 AND id_bpr = " + params.getKey()[0] + " " +
                        "ORDER BY id_bra = 1 DESC, name, id_bpr, id_bra ";
                break;
            case DModConsts.BU_ADD:
                settings = new DGuiCatalogueSettings("Domicilio", 3, 2, DLibConsts.DATA_TYPE_BOOL);
                sql = "SELECT id_bpr AS " + DDbConsts.FIELD_ID + "1, id_bra AS " + DDbConsts.FIELD_ID + "2, id_add AS " + DDbConsts.FIELD_ID + "3, " +
                        "id_bpr AS " + DDbConsts.FIELD_FK + "1, id_bra AS " + DDbConsts.FIELD_FK + "2, " +
                        "CONCAT(name, IF(b_def, '*', '')) AS " + DDbConsts.FIELD_ITEM + ", b_def AS " + DDbConsts.FIELD_COMP + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_dis = 0 AND b_del = 0 AND id_bpr = " + params.getKey()[0] + " AND id_bra = " + params.getKey()[1] + " " +
                        "ORDER BY id_add = 1 DESC, name, id_bpr, id_bra, id_add ";
                break;
            case DModConsts.BU_BNK:
                settings = new DGuiCatalogueSettings("Cuenta bancaria", 3, 2, DLibConsts.DATA_TYPE_BOOL);
                sql = "SELECT id_bpr AS " + DDbConsts.FIELD_ID + "1, id_bra AS " + DDbConsts.FIELD_ID + "2, id_bnk AS " + DDbConsts.FIELD_ID + "3, " +
                        "id_bpr AS " + DDbConsts.FIELD_FK + "1, id_bra AS " + DDbConsts.FIELD_FK + "2, " +
                        "CONCAT(name, IF(b_def, '*', '')) AS " + DDbConsts.FIELD_ITEM + ", b_def AS " + DDbConsts.FIELD_COMP + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_dis = 0 AND b_del = 0 AND id_bpr = " + params.getKey()[0] + " AND id_bra = " + params.getKey()[1] + " " +
                        "ORDER BY name, id_bpr, id_bra, id_bnk ";
                break;
            case DModConsts.BX_BPR_BNK:
                settings = new DGuiCatalogueSettings("Banco", 1);
                sql = "SELECT id_bpr AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " WHERE b_dis = 0 AND b_del = 0 AND b_bnk = 1 " +
                        "ORDER BY name, id_bpr ";
                break;
            case DModConsts.BX_BPR_CAR:
                settings = new DGuiCatalogueSettings("Transportista", 1);
                sql = "SELECT id_bpr AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " WHERE b_dis = 0 AND b_del = 0 AND b_car = 1 " +
                        "ORDER BY name, id_bpr ";
                break;
            case DModConsts.BX_BPR_AGT:
                settings = new DGuiCatalogueSettings("Agente", 1);
                sql = "SELECT b.id_bpr AS " + DDbConsts.FIELD_ID + "1, b.name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(DModConsts.BU_BPR) + " AS b " +
                        "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.M_AGT_CFG) + " AS a ON " +
                        "b.id_bpr = a.id_agt " +
                        "WHERE b.b_dis = 0 AND b.b_del = 0 AND a.b_del = 0 " +
                        "ORDER BY b.name, b.id_bpr ";
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        if (settings != null) {
            settings.setSql(sql);
        }

        return settings;
    }

    @Override
    public DGridPaneView getView(final int type, final int subtype, final DGuiParams params) {
        DGridPaneView view = null;

        switch (type) {
            case DModConsts.BU_BPR_TP:
                view = new DViewBizPartnerType(miClient, subtype, "Tipos " + DBprUtils.getBizPartnerClassNameSng(subtype).toLowerCase());
                break;
            case DModConsts.BU_BPR:
                view = new DViewBizPartner(miClient, subtype, DBprUtils.getBizPartnerClassNamePlr(subtype));
                break;
            case DModConsts.BU_BPR_CFG:
                view = new DViewBizPartnerConfig(miClient, subtype, "Config " + DBprUtils.getBizPartnerClassNamePlr(subtype).toLowerCase() + " Q");
                break;
            case DModConsts.BU_BRA:
                view = new DViewBranch(miClient, subtype, "Sucursales " + DBprUtils.getBizPartnerClassNamePlr(subtype).toLowerCase() + " Q");
                break;
            case DModConsts.BU_ADD:
                view = new DViewBranchAddress(miClient, subtype, "Domicilios " + DBprUtils.getBizPartnerClassNamePlr(subtype).toLowerCase() + " Q");
                break;
            case DModConsts.BU_BNK:
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return view;
    }

    @Override
    public DGuiOptionPicker getOptionPicker(final int type, final int subtype, final DGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DGuiForm getForm(final int type, final int subtype, final DGuiParams params) {
        DGuiForm form = null;

        switch (type) {
            case DModConsts.BU_BPR_TP:
                switch(subtype) {
                    case DModSysConsts.BS_BPR_CL_VEN:
                        if (moFormBizPartnerTypeVen == null) moFormBizPartnerTypeVen = new DFormBizPartnerType(miClient, subtype, "Tipo " + DBprUtils.getBizPartnerClassNameSng(subtype).toLowerCase());
                        form = moFormBizPartnerTypeVen;
                        break;
                    case DModSysConsts.BS_BPR_CL_CUS:
                        if (moFormBizPartnerTypeCus == null) moFormBizPartnerTypeCus = new DFormBizPartnerType(miClient, subtype, "Tipo " + DBprUtils.getBizPartnerClassNameSng(subtype).toLowerCase());
                        form = moFormBizPartnerTypeCus;
                        break;
                    case DModSysConsts.BS_BPR_CL_CDR:
                        if (moFormBizPartnerTypeCdr == null) moFormBizPartnerTypeCdr = new DFormBizPartnerType(miClient, subtype, "Tipo " + DBprUtils.getBizPartnerClassNameSng(subtype).toLowerCase());
                        form = moFormBizPartnerTypeCdr;
                        break;
                    case DModSysConsts.BS_BPR_CL_DBR:
                        if (moFormBizPartnerTypeDbr == null) moFormBizPartnerTypeDbr = new DFormBizPartnerType(miClient, subtype, "Tipo " + DBprUtils.getBizPartnerClassNameSng(subtype).toLowerCase());
                        form = moFormBizPartnerTypeDbr;
                        break;
                    default:
                        miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }

                break;
            case DModConsts.BU_BPR:
                switch(subtype) {
                    case DModSysConsts.BS_BPR_CL_VEN:
                        if (moFormBizPartnerVen == null) moFormBizPartnerVen = new DFormBizPartner(miClient, subtype, DBprUtils.getBizPartnerClassNameSng(subtype));
                        form = moFormBizPartnerVen;
                        break;
                    case DModSysConsts.BS_BPR_CL_CUS:
                        if (moFormBizPartnerCus == null) moFormBizPartnerCus = new DFormBizPartner(miClient, subtype, DBprUtils.getBizPartnerClassNameSng(subtype));
                        form = moFormBizPartnerCus;
                        break;
                    case DModSysConsts.BS_BPR_CL_CDR:
                        if (moFormBizPartnerCdr == null) moFormBizPartnerCdr = new DFormBizPartner(miClient, subtype, DBprUtils.getBizPartnerClassNameSng(subtype));
                        form = moFormBizPartnerCdr;
                        break;
                    case DModSysConsts.BS_BPR_CL_DBR:
                        if (moFormBizPartnerDbr == null) moFormBizPartnerDbr = new DFormBizPartner(miClient, subtype, DBprUtils.getBizPartnerClassNameSng(subtype));
                        form = moFormBizPartnerDbr;
                        break;
                    default:
                        miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case DModConsts.BU_BPR_CFG:
                break;
            case DModConsts.BU_BRA:
                break;
            case DModConsts.BU_ADD:
                break;
            case DModConsts.BU_BNK:
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return form;
    }

    @Override
    public DGuiReport getReport(final int type, final int subtype, final DGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
