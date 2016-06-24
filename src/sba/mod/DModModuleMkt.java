/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import sba.lib.DLibConsts;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.db.DDbRegistrySysFly;
import sba.lib.grid.DGridPaneView;
import sba.lib.gui.DGuiCatalogueSettings;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiForm;
import sba.lib.gui.DGuiModule;
import sba.lib.gui.DGuiOptionPicker;
import sba.lib.gui.DGuiParams;
import sba.lib.gui.DGuiReport;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.itm.db.DDbItem;
import sba.mod.mkt.db.DDbAgentConfig;
import sba.mod.mkt.db.DDbAgentType;
import sba.mod.mkt.db.DDbChannel;
import sba.mod.mkt.db.DDbCommissionCalc;
import sba.mod.mkt.db.DDbCommissionCalcNote;
import sba.mod.mkt.db.DDbCommissionCalcRow;
import sba.mod.mkt.db.DDbCommissionConfigAgent;
import sba.mod.mkt.db.DDbCommissionConfigUser;
import sba.mod.mkt.db.DDbCustomerBranchConfig;
import sba.mod.mkt.db.DDbCustomerConfig;
import sba.mod.mkt.db.DDbCustomerItemFixedPrice;
import sba.mod.mkt.db.DDbCustomerItemPriceType;
import sba.mod.mkt.db.DDbCustomerType;
import sba.mod.mkt.db.DDbPromoPackage;
import sba.mod.mkt.db.DDbPromoPackageCustomer;
import sba.mod.mkt.db.DDbPromoPackagePromo;
import sba.mod.mkt.db.DDbRoute;
import sba.mod.mkt.db.DDbSegment;
import sba.mod.mkt.db.DDbSpecialPriceList;
import sba.mod.mkt.db.DDbSpecialPriceListCustomer;
import sba.mod.mkt.db.DDbSpecialPriceListPrice;
import sba.mod.mkt.form.DFormAgentType;
import sba.mod.mkt.form.DFormChannel;
import sba.mod.mkt.form.DFormCommissionCalc;
import sba.mod.mkt.form.DFormCustomerItemFixedPrice;
import sba.mod.mkt.form.DFormCustomerItemPriceType;
import sba.mod.mkt.form.DFormCustomerType;
import sba.mod.mkt.form.DFormItemNamePrices;
import sba.mod.mkt.form.DFormItemPrices;
import sba.mod.mkt.form.DFormPromoPackage;
import sba.mod.mkt.form.DFormPromoPackageCustomer;
import sba.mod.mkt.form.DFormRoute;
import sba.mod.mkt.form.DFormSegment;
import sba.mod.mkt.form.DFormSpecialPriceList;
import sba.mod.mkt.form.DFormSpecialPriceListCustomer;
import sba.mod.mkt.view.DViewAgentConfig;
import sba.mod.mkt.view.DViewAgentType;
import sba.mod.mkt.view.DViewChannel;
import sba.mod.mkt.view.DViewCommissionCalc;
import sba.mod.mkt.view.DViewCustomerItemFixedPrice;
import sba.mod.mkt.view.DViewCustomerItemPriceType;
import sba.mod.mkt.view.DViewCustomerType;
import sba.mod.mkt.view.DViewItemPrices;
import sba.mod.mkt.view.DViewPromoPackage;
import sba.mod.mkt.view.DViewPromoPackageCustomer;
import sba.mod.mkt.view.DViewPromoPackagePromo;
import sba.mod.mkt.view.DViewRoute;
import sba.mod.mkt.view.DViewSegment;
import sba.mod.mkt.view.DViewSpecialPrice;
import sba.mod.mkt.view.DViewSpecialPriceCustomer;
import sba.mod.mkt.view.DViewSpecialPricePrice;

/**
 *
 * @author Sergio Flores
 */
public class DModModuleMkt extends DGuiModule implements ActionListener {

    private JMenu mjCat;
    private JMenuItem mjCatSegment;
    private JMenuItem mjCatChannel;
    private JMenuItem mjCatRoute;
    private JMenuItem mjCatCustomerType;
    private JMenuItem mjCatAgentConfig;
    private JMenuItem mjCatAgentType;

    private JMenu mjPrc;
    private JMenuItem mjPrcItemPrices;
    private JMenuItem mjPrcItemPricesEdition;
    private JMenuItem mjPrcItemNamePricesEdition;
    private JMenuItem mjPrcCustomerItemPriceType;
    private JMenuItem mjPrcCustomerFixedPrice;
    private JMenuItem mjPrcCustomerSpecialPrice;
    private JMenuItem mjPrcCustomerSpecialPriceCus;
    private JMenuItem mjPrcCustomerSpecialPricePrc;
    private JMenuItem mjPrcCustomerPromotionalPackage;
    private JMenuItem mjPrcCustomerPromotionalPackageCus;
    private JMenuItem mjPrcCustomerPromotionalPackageDis;

    private JMenu mjCmm;
    private JMenuItem mjCmmCalc;

    private DFormAgentType moFormAgentType;
    private DFormCustomerType moFormCustomerType;
    private DFormSegment moFormSegment;
    private DFormChannel moFormChannel;
    private DFormRoute moFormRoute;
    private DFormCustomerItemFixedPrice moFormCustomerItemFixedPrice;
    private DFormCustomerItemPriceType moFormCustomerItemPriceType;
    private DFormCommissionCalc moFormCommissionCalc;
    private DFormItemPrices moFormItemPrices;
    private DFormItemNamePrices moFormItemNamePrices;
    private DFormSpecialPriceList moFormSpecialPriceList;
    private DFormSpecialPriceListCustomer moFormSpecialPriceListCustomer;
    private DFormPromoPackage moFormPromoPackage;
    private DFormPromoPackageCustomer moFormPromoPackageCustomer;

    public DModModuleMkt(DGuiClient client) {
        super(client, DModConsts.MOD_MKT, DLibConsts.UNDEFINED);
        initComponents();
    }

    private void initComponents() {
        mjCat = new JMenu("Catálogos");
        mjCatSegment = new JMenuItem("Segmentos");
        mjCatChannel = new JMenuItem("Canales");
        mjCatRoute = new JMenuItem("Rutas");
        mjCatCustomerType = new JMenuItem("Tipos de cliente marketing");
        mjCatAgentConfig = new JMenuItem("Configuración de agentes");
        mjCatAgentType = new JMenuItem("Tipos de agente");

        mjCat.add(mjCatSegment);
        mjCat.add(mjCatChannel);
        mjCat.add(mjCatRoute);
        mjCat.addSeparator();
        mjCat.add(mjCatCustomerType);
        mjCat.addSeparator();
        mjCat.add(mjCatAgentConfig);
        mjCat.add(mjCatAgentType);

        mjCatSegment.addActionListener(this);
        mjCatChannel.addActionListener(this);
        mjCatRoute.addActionListener(this);
        mjCatCustomerType.addActionListener(this);
        mjCatAgentConfig.addActionListener(this);
        mjCatAgentType.addActionListener(this);

        mjPrc = new JMenu("Precios");
        mjPrcItemPrices = new JMenuItem("Precios de ítems (Q)");
        mjPrcItemPricesEdition = new JMenuItem("Captura de precios de ítems...");
        mjPrcItemNamePricesEdition = new JMenuItem("Captura simplificada de precios de ítems...");
        mjPrcCustomerItemPriceType = new JMenuItem("Tipos de precios de ítems y clientes");
        mjPrcCustomerFixedPrice = new JMenuItem("Precios fijos de ítems y clientes");
        mjPrcCustomerSpecialPrice = new JMenuItem("Listas precios especiales de ítems");
        mjPrcCustomerSpecialPriceCus = new JMenuItem("Listas precios especiales de ítems y clientes");
        mjPrcCustomerSpecialPricePrc = new JMenuItem("Precios especiales de ítems (Q)");
        mjPrcCustomerPromotionalPackage = new JMenuItem("Paquetes promocionales");
        mjPrcCustomerPromotionalPackageCus = new JMenuItem("Paquetes promocionales y clientes");
        mjPrcCustomerPromotionalPackageDis = new JMenuItem("Descuentos paquetes promocionales (Q)");

        mjPrc.add(mjPrcItemPrices);
        mjPrc.add(mjPrcItemPricesEdition);
        mjPrc.add(mjPrcItemNamePricesEdition);
        mjPrc.addSeparator();
        mjPrc.add(mjPrcCustomerItemPriceType);
        mjPrc.addSeparator();
        mjPrc.add(mjPrcCustomerFixedPrice);
        mjPrc.addSeparator();
        mjPrc.add(mjPrcCustomerSpecialPrice);
        mjPrc.add(mjPrcCustomerSpecialPriceCus);
        mjPrc.add(mjPrcCustomerSpecialPricePrc);
        mjPrc.addSeparator();
        mjPrc.add(mjPrcCustomerPromotionalPackage);
        mjPrc.add(mjPrcCustomerPromotionalPackageCus);
        mjPrc.add(mjPrcCustomerPromotionalPackageDis);

        mjPrcItemPrices.addActionListener(this);
        mjPrcItemPricesEdition.addActionListener(this);
        mjPrcItemNamePricesEdition.addActionListener(this);
        mjPrcCustomerItemPriceType.addActionListener(this);
        mjPrcCustomerFixedPrice.addActionListener(this);
        mjPrcCustomerSpecialPrice.addActionListener(this);
        mjPrcCustomerSpecialPriceCus.addActionListener(this);
        mjPrcCustomerSpecialPricePrc.addActionListener(this);
        mjPrcCustomerPromotionalPackage.addActionListener(this);
        mjPrcCustomerPromotionalPackageDis.addActionListener(this);
        mjPrcCustomerPromotionalPackageCus.addActionListener(this);

        mjPrcCustomerFixedPrice.setEnabled(((DDbConfigCompany) miClient.getSession().getConfigCompany()).isCustomerFixedPrices());
        mjPrcCustomerSpecialPrice.setEnabled(((DDbConfigCompany) miClient.getSession().getConfigCompany()).isCustomerSpecialPrices());
        mjPrcCustomerSpecialPriceCus.setEnabled(mjPrcCustomerSpecialPrice.isEnabled());
        mjPrcCustomerSpecialPricePrc.setEnabled(mjPrcCustomerSpecialPrice.isEnabled());
        mjPrcCustomerPromotionalPackage.setEnabled(((DDbConfigCompany) miClient.getSession().getConfigCompany()).isCustomerPromotionalPackages());
        mjPrcCustomerPromotionalPackageCus.setEnabled(mjPrcCustomerPromotionalPackage.isEnabled());
        mjPrcCustomerPromotionalPackageDis.setEnabled(mjPrcCustomerPromotionalPackage.isEnabled());

        mjCmm = new JMenu("Comisiones");
        mjCmmCalc = new JMenuItem("Cálculo de comisiones");

        mjCmm.add(mjCmmCalc);

        mjCmmCalc.addActionListener(this);
    }

    private void showItemPricesEdition() {
        if (moFormItemPrices == null) {
            moFormItemPrices = new DFormItemPrices(miClient, "Captura de precios de ítems");
        }

        moFormItemPrices.resetForm();
        miClient.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));
        moFormItemPrices.populateItemPrices();
        miClient.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        moFormItemPrices.setVisible(true);

        if (moFormItemPrices.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
            miClient.getSession().notifySuscriptors(DModConsts.MX_ITM_PRC);
        }
    }

    private void showItemNamePricesEdition() {
        if (moFormItemNamePrices == null) {
            moFormItemNamePrices = new DFormItemNamePrices(miClient, "Captura simplificada de precios de ítems");
        }

        moFormItemNamePrices.resetForm();
        moFormItemNamePrices.setVisible(true);

        if (moFormItemNamePrices.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
            miClient.getSession().notifySuscriptors(DModConsts.MX_ITM_PRC);
        }
    }

    @Override
    public JMenu[] getMenus() {
        return new JMenu[] { mjCat, mjPrc, mjCmm };
    }

    @Override
    public DDbRegistry getRegistry(final int type, final DGuiParams params) {
        DDbRegistry registry = null;

        switch (type) {
            case DModConsts.MS_PRC_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_prc_tp = " + pk[0] + " "; }
                };
                break;
            case DModConsts.MS_ITM_PRC_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_itm_prc_tp = " + pk[0] + " "; }
                };
                break;
            case DModConsts.MS_LST_GRP_TP:
                break;
            case DModConsts.MS_LST_TP:
                break;
            case DModConsts.MS_DIS_TP:
                break;
            case DModConsts.MS_CMM_TP:
                break;
            case DModConsts.MS_LNK_AGT_TP:
                break;
            case DModConsts.MS_LNK_CUS_TP:
                break;
            case DModConsts.MU_AGT_TP:
                registry = new DDbAgentType();
                break;
            case DModConsts.MU_CUS_TP:
                registry = new DDbCustomerType();
                break;
            case DModConsts.MU_SEG:
                registry = new DDbSegment();
                break;
            case DModConsts.MU_CHA:
                registry = new DDbChannel();
                break;
            case DModConsts.MU_ROU:
                registry = new DDbRoute();
                break;
            case DModConsts.M_AGT_CFG:
                registry = new DDbAgentConfig();
                break;
            case DModConsts.M_CUS_CFG:
                registry = new DDbCustomerConfig();
                break;
            case DModConsts.M_CUS_BRA_CFG:
                registry = new DDbCustomerBranchConfig();
                break;
            case DModConsts.M_CUS_FIX:
                registry = new DDbCustomerItemFixedPrice();
                break;
            case DModConsts.M_CUS_ITM_PRC_TP:
                registry = new DDbCustomerItemPriceType();
                break;
            case DModConsts.M_SPE:
                registry = new DDbSpecialPriceList();
                break;
            case DModConsts.M_SPE_PRC:
                registry = new DDbSpecialPriceListPrice();
                break;
            case DModConsts.M_SPE_CUS:
                registry = new DDbSpecialPriceListCustomer();
                break;
            case DModConsts.M_PRM:
                registry = new DDbPromoPackage();
                break;
            case DModConsts.M_PRM_PRM:
                registry = new DDbPromoPackagePromo();
                break;
            case DModConsts.M_PRM_CUS:
                registry = new DDbPromoPackageCustomer();
                break;
            case DModConsts.M_CMM_CFG_AGT:
                registry = new DDbCommissionConfigAgent();
                break;
            case DModConsts.M_CMM_CFG_USR:
                registry = new DDbCommissionConfigUser();
                break;
            case DModConsts.M_CMM_CAL:
                registry = new DDbCommissionCalc();
                break;
            case DModConsts.M_CMM_CAL_NOT:
                registry = new DDbCommissionCalcNote();
                break;
            case DModConsts.M_CMM_CAL_ROW:
                registry = new DDbCommissionCalcRow();
                break;
            case DModConsts.MX_ITM_PRC:
                registry = new DDbItem();
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
            case DModConsts.MS_PRC_TP:
                settings = new DGuiCatalogueSettings("Tipo precio", 1);
                sql = "SELECT id_prc_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.MS_ITM_PRC_TP:
                settings = new DGuiCatalogueSettings("Tipo precio ítem", 1);
                sql = "SELECT id_itm_prc_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.MS_LST_GRP_TP:
                settings = new DGuiCatalogueSettings("Tipo grupo lista precios", 1);
                sql = "SELECT id_lst_grp_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.MS_LST_TP:
                settings = new DGuiCatalogueSettings("Tipo lista precios", 1);
                sql = "SELECT id_lst_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.MS_DIS_TP:
                settings = new DGuiCatalogueSettings("Tipo descuento", 1);
                sql = "SELECT id_dis_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.MS_CMM_TP:
                settings = new DGuiCatalogueSettings("Tipo comisión", 1);
                sql = "SELECT id_cmm_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.MS_CMM_CAL_TP:
                settings = new DGuiCatalogueSettings("Tipo cálculo comisión", 1);
                sql = "SELECT id_cmm_cal_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.MS_CMM_VAL_TP:
                settings = new DGuiCatalogueSettings("Tipo valor comisión", 1);
                sql = "SELECT id_cmm_val_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.MS_LNK_AGT_TP:
                settings = new DGuiCatalogueSettings("Tipo referencia agentes", 1);
                sql = "SELECT id_lnk_agt_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.MS_LNK_USR_TP:
                settings = new DGuiCatalogueSettings("Tipo referencia usuarios", 1);
                sql = "SELECT id_lnk_usr_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.MS_LNK_CUS_TP:
                settings = new DGuiCatalogueSettings("Tipo referencia clientes marketing", 1);
                sql = "SELECT id_lnk_cus_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.MU_AGT_TP:
                settings = new DGuiCatalogueSettings("Tipo agente", 1);
                sql = "SELECT id_agt_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_dis = 0 AND b_del = 0 ORDER BY name, id_agt_tp ";
                break;
            case DModConsts.MU_CUS_TP:
                settings = new DGuiCatalogueSettings("Tipo cliente mkt", 1);
                sql = "SELECT id_cus_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_dis = 0 AND b_del = 0 ORDER BY name, id_cus_tp ";
                break;
            case DModConsts.MU_SEG:
                settings = new DGuiCatalogueSettings("Tipo segmento", 1);
                sql = "SELECT id_seg AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_dis = 0 AND b_del = 0 ORDER BY name, id_seg ";
                break;
            case DModConsts.MU_CHA:
                settings = new DGuiCatalogueSettings("Tipo canal", 1);
                sql = "SELECT id_cha AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_dis = 0 AND b_del = 0 ORDER BY name, id_cha ";
                break;
            case DModConsts.MU_ROU:
                settings = new DGuiCatalogueSettings("Tipo ruta", 1);
                sql = "SELECT id_rou AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_dis = 0 AND b_del = 0 ORDER BY name, id_rou ";
                break;
            case DModConsts.M_SPE:
                settings = new DGuiCatalogueSettings("Lista precios especiales", 1);
                sql = "SELECT id_spe AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, id_spe ";
                break;
            case DModConsts.M_PRM:
                settings = new DGuiCatalogueSettings("Paquete promocional", 1);
                sql = "SELECT id_prm AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, id_prm ";
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
            case DModConsts.MU_AGT_TP:
                view = new DViewAgentType(miClient, "Tipos agentes");
                break;
            case DModConsts.MU_CUS_TP:
                view = new DViewCustomerType(miClient, "Tipos clientes mkt");
                break;
            case DModConsts.MU_SEG:
                view = new DViewSegment(miClient, "Segmentos");
                break;
            case DModConsts.MU_CHA:
                view = new DViewChannel(miClient, "Canales");
                break;
            case DModConsts.MU_ROU:
                view = new DViewRoute(miClient, "Rutas");
                break;
            case DModConsts.M_AGT_CFG:
                view = new DViewAgentConfig(miClient, "Config. agentes");
                break;
            case DModConsts.M_CUS_CFG:
                break;
            case DModConsts.M_CUS_BRA_CFG:
                break;
            case DModConsts.M_CUS_FIX:
                view = new DViewCustomerItemFixedPrice(miClient, "Precios fijos y clientes");
                break;
            case DModConsts.M_CUS_ITM_PRC_TP:
                view = new DViewCustomerItemPriceType(miClient, "Tipos precios ítems y clientes");
                break;
            case DModConsts.M_SPE:
                view = new DViewSpecialPrice(miClient, "Listas precios esp.");
                break;
            case DModConsts.M_SPE_PRC:
                view = new DViewSpecialPricePrice(miClient, "Precios esp. Q");
                break;
            case DModConsts.M_SPE_CUS:
                view = new DViewSpecialPriceCustomer(miClient, "Listas precios esp. y clientes");
                break;
            case DModConsts.M_PRM:
                view = new DViewPromoPackage(miClient, "Paqs. promocionales");
                break;
            case DModConsts.M_PRM_PRM:
                view = new DViewPromoPackagePromo(miClient, "Desctos. paqs. promocionales Q");
                break;
            case DModConsts.M_PRM_CUS:
                view = new DViewPromoPackageCustomer(miClient, "Paqs. promocionales y clientes");
                break;
            case DModConsts.M_CMM_CAL:
                view = new DViewCommissionCalc(miClient, "Cálculo comisiones");
                break;
            case DModConsts.MX_ITM_PRC:
                view = new DViewItemPrices(miClient, "Precios Q");
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
            case DModConsts.MU_AGT_TP:
                if (moFormAgentType == null) moFormAgentType = new DFormAgentType(miClient, "Tipo de agente");
                form = moFormAgentType;
                break;
            case DModConsts.MU_CUS_TP:
                if (moFormCustomerType == null) moFormCustomerType = new DFormCustomerType(miClient, "Tipo de cliente marketing");
                form = moFormCustomerType;
                break;
            case DModConsts.MU_SEG:
                if (moFormSegment == null) moFormSegment = new DFormSegment(miClient, "Segmento");
                form = moFormSegment;
                break;
            case DModConsts.MU_CHA:
                if (moFormChannel == null) moFormChannel = new DFormChannel(miClient, "Canal");
                form = moFormChannel;
                break;
            case DModConsts.MU_ROU:
                if (moFormRoute == null) moFormRoute = new DFormRoute(miClient, "Ruta");
                form = moFormRoute;
                break;
            case DModConsts.M_CUS_FIX:
                if (moFormCustomerItemFixedPrice == null) moFormCustomerItemFixedPrice = new DFormCustomerItemFixedPrice(miClient, "Precio unitario fijo y cliente");
                form = moFormCustomerItemFixedPrice;
                break;
            case DModConsts.M_CUS_ITM_PRC_TP:
                if (moFormCustomerItemPriceType == null) moFormCustomerItemPriceType = new DFormCustomerItemPriceType(miClient, "Tipo de precio de ítem y cliente");
                form = moFormCustomerItemPriceType;
                break;
            case DModConsts.M_SPE:
                if (moFormSpecialPriceList == null) moFormSpecialPriceList = new DFormSpecialPriceList(miClient, "Lista de precios especiales de ítems");
                form = moFormSpecialPriceList;
                break;
            case DModConsts.M_SPE_CUS:
                if (moFormSpecialPriceListCustomer == null) moFormSpecialPriceListCustomer = new DFormSpecialPriceListCustomer(miClient, "Lista de precios especiales de ítems y cliente");
                form = moFormSpecialPriceListCustomer;
                break;
            case DModConsts.M_PRM:
                if (moFormPromoPackage == null) moFormPromoPackage = new DFormPromoPackage(miClient, "Paquete promocional");
                form = moFormPromoPackage;
                break;
            case DModConsts.M_PRM_CUS:
                if (moFormPromoPackageCustomer == null) moFormPromoPackageCustomer = new DFormPromoPackageCustomer(miClient, "Paquete promocional y cliente");
                form = moFormPromoPackageCustomer;
                break;
            case DModConsts.M_CMM_CAL:
                if (moFormCommissionCalc == null) moFormCommissionCalc = new DFormCommissionCalc(miClient, "Cálculo de comisiones");
                form = moFormCommissionCalc;
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

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem) e.getSource();

            if (menuItem == mjCatSegment) {
                showView(DModConsts.MU_SEG, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatChannel) {
                showView(DModConsts.MU_CHA, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatRoute) {
                showView(DModConsts.MU_ROU, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatCustomerType) {
                showView(DModConsts.MU_CUS_TP, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatAgentConfig) {
                showView(DModConsts.M_AGT_CFG, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatAgentType) {
                showView(DModConsts.MU_AGT_TP, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjPrcItemPrices) {
                showView(DModConsts.MX_ITM_PRC, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjPrcItemPricesEdition) {
                showItemPricesEdition();
            }
            else if (menuItem == mjPrcItemNamePricesEdition) {
                showItemNamePricesEdition();
            }
            else if (menuItem == mjPrcItemPrices) {
                showView(DModConsts.MX_ITM_PRC, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjPrcCustomerItemPriceType) {
                showView(DModConsts.M_CUS_ITM_PRC_TP, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjPrcCustomerFixedPrice) {
                showView(DModConsts.M_CUS_FIX, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjPrcCustomerSpecialPrice) {
                showView(DModConsts.M_SPE, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjPrcCustomerSpecialPricePrc) {
                showView(DModConsts.M_SPE_PRC, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjPrcCustomerSpecialPriceCus) {
                showView(DModConsts.M_SPE_CUS, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjPrcCustomerPromotionalPackage) {
                showView(DModConsts.M_PRM, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjPrcCustomerPromotionalPackageDis) {
                showView(DModConsts.M_PRM_PRM, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjPrcCustomerPromotionalPackageCus) {
                showView(DModConsts.M_PRM_CUS, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCmmCalc) {
                showView(DModConsts.M_CMM_CAL, DLibConsts.UNDEFINED, null);
            }
        }
    }
}
