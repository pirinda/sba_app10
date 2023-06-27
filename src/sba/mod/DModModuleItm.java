/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod;

import javax.swing.JMenu;
import sba.gui.util.DUtilConsts;
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
import sba.mod.itm.db.DDbBrand;
import sba.mod.itm.db.DDbComponent;
import sba.mod.itm.db.DDbDepartment;
import sba.mod.itm.db.DDbItem;
import sba.mod.itm.db.DDbItemBarcode;
import sba.mod.itm.db.DDbItemFamily;
import sba.mod.itm.db.DDbItemGenus;
import sba.mod.itm.db.DDbItemLine;
import sba.mod.itm.db.DDbManufacturer;
import sba.mod.itm.db.DDbUnit;
import sba.mod.itm.form.DFormBrand;
import sba.mod.itm.form.DFormComponent;
import sba.mod.itm.form.DFormDepartment;
import sba.mod.itm.form.DFormItem;
import sba.mod.itm.form.DFormItemFamily;
import sba.mod.itm.form.DFormItemGenus;
import sba.mod.itm.form.DFormItemLine;
import sba.mod.itm.form.DFormManufacturer;
import sba.mod.itm.form.DFormUnit;
import sba.mod.itm.view.DViewBrand;
import sba.mod.itm.view.DViewComplement;
import sba.mod.itm.view.DViewDepartment;
import sba.mod.itm.view.DViewItem;
import sba.mod.itm.view.DViewItemBarcode;
import sba.mod.itm.view.DViewItemFamily;
import sba.mod.itm.view.DViewItemGenus;
import sba.mod.itm.view.DViewItemLine;
import sba.mod.itm.view.DViewManufacturer;
import sba.mod.itm.view.DViewUnit;

/**
 *
 * @author Sergio Flores
 */
public class DModModuleItm extends DGuiModule {

    private DFormUnit moFormUnit;
    private DFormBrand moFormBrand;
    private DFormManufacturer moFormManufacturer;
    private DFormComponent moFormComponent;
    private DFormDepartment moFormDepartment;
    private DFormItemFamily moFormItemFamily;
    private DFormItemGenus moFormItemGenus;
    private DFormItemLine moFormItemLine;
    private DFormItem moFormItem;

    public DModModuleItm(DGuiClient client) {
        super(client, DModConsts.MOD_ITM, DLibConsts.UNDEFINED);
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
            case DModConsts.IS_ITM_CT:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_itm_ct = " + pk[0] + " "; }
                };
                break;
            case DModConsts.IS_ITM_CL:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_itm_ct = " + pk[0] + " AND id_itm_cl = " + pk[1] + " "; }
                };
                break;
            case DModConsts.IS_ITM_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_itm_ct = " + pk[0] + " AND id_itm_cl = " + pk[1] + " AND id_itm_tp = " + pk[2] + " "; }
                };
                break;
            case DModConsts.IX_ITM_CL_BY_IDX:
                registry = new DDbRegistrySysFly(DModConsts.IS_ITM_CL) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE idx = " + pk[0] + " "; }
                };
                break;
            case DModConsts.IX_ITM_TP_BY_IDX:
                registry = new DDbRegistrySysFly(DModConsts.IS_ITM_TP) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE idx = " + pk[0] + " "; }
                };
                break;
            case DModConsts.IS_LNK_ITM_TP:
                registry = new DDbRegistrySysFly(DModConsts.IS_LNK_ITM_TP) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_lnk_tp = " + pk[0] + " "; }
                };
                break;
            case DModConsts.IU_UNT:
                registry = new DDbUnit();
                break;
            case DModConsts.IU_BRD:
                registry = new DDbBrand();
                break;
            case DModConsts.IU_MFR:
                registry = new DDbManufacturer();
                break;
            case DModConsts.IU_CMP:
                registry = new DDbComponent();
                break;
            case DModConsts.IU_DEP:
                registry = new DDbDepartment();
                break;
            case DModConsts.IU_FAM:
                registry = new DDbItemFamily();
                break;
            case DModConsts.IU_GEN:
                registry = new DDbItemGenus();
                break;
            case DModConsts.IU_LIN:
                registry = new DDbItemLine();
                break;
            case DModConsts.IU_ITM:
                registry = new DDbItem();
                break;
            case DModConsts.IU_ITM_BAR:
                registry = new DDbItemBarcode();
                break;
            case DModConsts.IU_GEN_DEP:
                break;
            case DModConsts.IU_LIN_DEP:
                break;
            case DModConsts.IU_ITM_DEP:
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return registry;
    }

    @Override
    public DGuiCatalogueSettings getCatalogueSettings(final int type, final int subtype, final DGuiParams params) {
        String sql = "";
        String innerJoin = "";
        DGuiCatalogueSettings settings = null;

        switch (type) {
            case DModConsts.IS_ITM_CT:
                settings = new DGuiCatalogueSettings("Categoría ítems", 1);
                sql = "SELECT id_itm_ct AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case DModConsts.IS_ITM_CL:
                settings = new DGuiCatalogueSettings("Clase ítems", 2, 1);
                sql = "SELECT id_itm_ct AS " + DDbConsts.FIELD_ID + "1, id_itm_cl AS " + DDbConsts.FIELD_ID + "2, name AS " + DDbConsts.FIELD_ITEM + ", " +
                        "id_itm_ct AS " + DDbConsts.FIELD_FK + "1 " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY id_itm_ct, sort ";
                break;
            case DModConsts.IS_ITM_TP:
                settings = new DGuiCatalogueSettings("Tipo ítems", 3, 2);
                sql = "SELECT id_itm_ct AS " + DDbConsts.FIELD_ID + "1, id_itm_cl AS " + DDbConsts.FIELD_ID + "2, id_itm_tp AS " + DDbConsts.FIELD_ID + "3, name AS " + DDbConsts.FIELD_ITEM + ", " +
                        "id_itm_ct AS " + DDbConsts.FIELD_FK + "1, id_itm_cl AS " + DDbConsts.FIELD_FK + "2 " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY id_itm_ct, id_itm_cl, sort ";
                break;
            case DModConsts.IX_ITM_CL_BY_IDX:
                settings = new DGuiCatalogueSettings("Clase ítems", 1);
                sql = "SELECT cl.idx AS " + DDbConsts.FIELD_ID + "1, CONCAT(ct.name, ', ', cl.name) AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(DModConsts.IS_ITM_CT) + " AS ct " +
                        "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IS_ITM_CL) + " AS cl ON ct.id_itm_ct = cl.id_itm_ct " +
                        "WHERE NOT cl.b_del ORDER BY cl.id_itm_ct, cl.sort ";
                break;
            case DModConsts.IX_ITM_TP_BY_IDX:
                settings = new DGuiCatalogueSettings("Tipo ítems", 1);
                sql = "SELECT tp.idx AS " + DDbConsts.FIELD_ID + "1, CONCAT(ct.name, ', ', cl.name, ', ', tp.name) AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(DModConsts.IS_ITM_CT) + " AS ct " +
                        "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IS_ITM_CL) + " AS cl ON ct.id_itm_ct = cl.id_itm_ct " +
                        "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IS_ITM_TP) + " AS tp ON cl.id_itm_ct = tp.id_itm_ct AND cl.id_itm_cl = tp.id_itm_cl " +
                        "WHERE NOT tp.b_del ORDER BY tp.id_itm_ct, tp.id_itm_cl, tp.sort ";
                break;
            case DModConsts.IS_SNR_TP:
                settings = new DGuiCatalogueSettings("Tipo número serie", 1);
                sql = "SELECT id_snr_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case DModConsts.IS_LNK_ITM_TP:
                settings = new DGuiCatalogueSettings("Tipo referencia ítems", 1);
                sql = "SELECT id_lnk_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY sort ";
                break;
            case DModConsts.IU_UNT:
                settings = new DGuiCatalogueSettings("Unidad", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_unt AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + ", code AS " + DDbConsts.FIELD_CODE + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY name, id_unt ";
                break;
            case DModConsts.IU_BRD:
                settings = new DGuiCatalogueSettings("Marca", 1);
                sql = "SELECT id_brd AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY name, id_brd ";
                break;
            case DModConsts.IU_MFR:
                settings = new DGuiCatalogueSettings("Fabricante", 1);
                sql = "SELECT id_mfr AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY name, id_mfr ";
                break;
            case DModConsts.IU_CMP:
                settings = new DGuiCatalogueSettings("Componente", 1);
                sql = "SELECT id_cmp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY name, id_cmp ";
                break;
            case DModConsts.IU_DEP:
                settings = new DGuiCatalogueSettings("Departamento", 1);
                sql = "SELECT id_dep AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY name, id_dep ";
                break;
            case DModConsts.IU_FAM:
                settings = new DGuiCatalogueSettings("Familia ítems", 1);
                sql = "SELECT id_fam AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE NOT b_del ORDER BY name, id_fam ";
                break;
            case DModConsts.IU_GEN:
                settings = new DGuiCatalogueSettings("Género ítems", 1);
                sql = "SELECT id_gen AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE NOT b_del " +
                        (params == null ? "" : (params.getParamsMap().get(DModConsts.IU_LIN) == null ? "" : "AND b_lin = " + (Boolean) params.getParamsMap().get(DModConsts.IU_LIN) + " ")) +
                        "ORDER BY name, id_gen ";
                break;
            case DModConsts.IU_LIN:
                settings = new DGuiCatalogueSettings("Línea ítems", 1, 1);
                sql = "SELECT id_lin AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + ", " +
                        "fk_gen AS " + DDbConsts.FIELD_FK + "1 " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE NOT b_del " +
                        (params == null ? "" : "AND fk_gen = " + params.getKey()[0] + " ") +
                        "ORDER BY fk_gen, name, id_lin ";
                break;
            case DModConsts.IU_ITM:
                if (params != null && params.getType() == DModConsts.IS_ITM_CL) {
                    innerJoin = "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_GEN) + " AS gen ON i.fk_gen = gen.id_gen "
                            + "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IS_ITM_CL) + " AS icl ON gen.fk_itm_ct = icl.id_itm_ct AND gen.fk_itm_cl = icl.id_itm_cl "
                            + "AND icl.id_itm_ct = " + params.getKey()[0] + " AND icl.id_itm_cl = " + params.getKey()[1] + " ";
                }
                
                settings = new DGuiCatalogueSettings("Ítem", 1, 0, DLibConsts.DATA_TYPE_TEXT);
                settings.setCodeApplying(true);
                sql = "SELECT i.id_itm AS " + DDbConsts.FIELD_ID + "1, i.name AS " + DDbConsts.FIELD_ITEM + ", i.code AS " + DDbConsts.FIELD_CODE + ", u.code AS " + DDbConsts.FIELD_COMP + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " AS i " +
                        "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_UNT) + " AS u ON i.fk_unt = u.id_unt " +
                        innerJoin +
                        "WHERE NOT i.b_del " + (subtype == DUtilConsts.PER_SNR ? "AND b_snr " : "") +
                        "ORDER BY i.name, i.id_itm ";
                break;
            case DModConsts.IU_ITM_BAR:
                break;
            case DModConsts.IU_GEN_DEP:
                break;
            case DModConsts.IU_LIN_DEP:
                break;
            case DModConsts.IU_ITM_DEP:
                break;
            case DModConsts.I_VEN_PRY:
                break;
            case DModConsts.I_VEN_ITM:
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
            case DModConsts.IU_UNT:
                view = new DViewUnit(miClient, "Unidades");
                break;
            case DModConsts.IU_BRD:
                view = new DViewBrand(miClient, "Marcas");
                break;
            case DModConsts.IU_MFR:
                view = new DViewManufacturer(miClient, "Fabricantes");
                break;
            case DModConsts.IU_CMP:
                view = new DViewComplement(miClient, "Componentes");
                break;
            case DModConsts.IU_DEP:
                view = new DViewDepartment(miClient, "Departamentos");
                break;
            case DModConsts.IU_FAM:
                view = new DViewItemFamily(miClient, "Familias ítems");
                break;
            case DModConsts.IU_GEN:
                view = new DViewItemGenus(miClient, "Géneros ítems");
                break;
            case DModConsts.IU_LIN:
                view = new DViewItemLine(miClient, "Líneas ítems");
                break;
            case DModConsts.IU_ITM:
                view = new DViewItem(miClient, "Ítems");
                break;
            case DModConsts.IU_ITM_BAR:
                view = new DViewItemBarcode(miClient, "Códigos barras");
                break;
            case DModConsts.IU_GEN_DEP:
                break;
            case DModConsts.IU_LIN_DEP:
                break;
            case DModConsts.IU_ITM_DEP:
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
            case DModConsts.IU_UNT:
                if (moFormUnit == null) moFormUnit = new DFormUnit(miClient, "Unidad");
                form = moFormUnit;
                break;
            case DModConsts.IU_BRD:
                if (moFormBrand == null) moFormBrand = new DFormBrand(miClient, "Marca");
                form = moFormBrand;
                break;
            case DModConsts.IU_MFR:
                if (moFormManufacturer == null) moFormManufacturer = new DFormManufacturer(miClient, "Fabricante");
                form = moFormManufacturer;
                break;
            case DModConsts.IU_CMP:
                if (moFormComponent == null) moFormComponent = new DFormComponent(miClient, "Componente");
                form = moFormComponent;
                break;
            case DModConsts.IU_DEP:
                if (moFormDepartment == null) moFormDepartment = new DFormDepartment(miClient, "Departamento");
                form = moFormDepartment;
                break;
            case DModConsts.IU_FAM:
                if (moFormItemFamily == null) moFormItemFamily = new DFormItemFamily(miClient, "Familia ítems");
                form = moFormItemFamily;
                break;
            case DModConsts.IU_GEN:
                if (moFormItemGenus == null) moFormItemGenus = new DFormItemGenus(miClient, "Género ítems");
                form = moFormItemGenus;
                break;
            case DModConsts.IU_LIN:
                if (moFormItemLine == null) moFormItemLine = new DFormItemLine(miClient, "Línea ítems");
                if (params != null) moFormItemLine.setValue(DModConsts.IU_GEN, params.getParamsMap().get(DModConsts.IU_GEN));
                form = moFormItemLine;
                break;
            case DModConsts.IU_ITM:
                if (moFormItem == null) moFormItem = new DFormItem(miClient, "Ítem");
                form = moFormItem;
                break;
            case DModConsts.IU_ITM_BAR:
                break;
            case DModConsts.IU_GEN_DEP:
                break;
            case DModConsts.IU_LIN_DEP:
                break;
            case DModConsts.IU_ITM_DEP:
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
