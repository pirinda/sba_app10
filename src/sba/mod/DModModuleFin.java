/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import sba.gui.util.DUtilConsts;
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
import sba.mod.bpr.db.DBprUtils;
import sba.mod.fin.db.DDbAbpBizPartner;
import sba.mod.fin.db.DDbAbpBranchCash;
import sba.mod.fin.db.DDbAbpBranchWarehouse;
import sba.mod.fin.db.DDbAbpItem;
import sba.mod.fin.db.DDbAccount;
import sba.mod.fin.db.DDbBookkeepingMove;
import sba.mod.fin.db.DDbBookkeepingMoveCustom;
import sba.mod.fin.db.DDbBookkeepingNumber;
import sba.mod.fin.db.DDbBookkeepingRecord;
import sba.mod.fin.db.DDbExchangeRate;
import sba.mod.fin.db.DDbRecordType;
import sba.mod.fin.db.DDbTax;
import sba.mod.fin.db.DDbTaxGroup;
import sba.mod.fin.db.DDbTaxGroupConfig;
import sba.mod.fin.db.DDbTaxGroupConfigRow;
import sba.mod.fin.db.DDbTaxRegion;
import sba.mod.fin.db.DDbYear;
import sba.mod.fin.db.DDbYearPeriod;
import sba.mod.fin.db.DFinUtils;
import sba.mod.fin.form.DDialogBookkeepingOpening;
import sba.mod.fin.form.DFormBookkeepingMoveCustom;
import sba.mod.fin.form.DFormBookkeepingMoveCustomDpsClear;
import sba.mod.fin.form.DFormCashPayment;
import sba.mod.fin.view.DViewBalanceBizPartner;
import sba.mod.fin.view.DViewBalanceBranchCash;
import sba.mod.fin.view.DViewBranchCashMoves;
import sba.mod.fin.view.DViewBranchCashMovesByMode;
import sba.mod.trn.form.DDialogStockOpening;

/**
 *
 * @author Sergio Flores
 */
public class DModModuleFin extends DGuiModule implements ActionListener {

    private JMenu mjCat;
    private JMenuItem mjCatAccount;
    private JMenuItem mjCatCustomer;
    private JMenuItem mjCatCustomerConfig;
    private JMenuItem mjCatCustomerBranch;
    private JMenuItem mjCatCustomerType;
    private JMenuItem mjCatVendor;
    private JMenuItem mjCatVendorConfig;
    private JMenuItem mjCatVendorBranch;
    private JMenuItem mjCatVendorType;
    private JMenuItem mjCatDebtor;
    private JMenuItem mjCatDebtorConfig;
    private JMenuItem mjCatDebtorType;
    private JMenuItem mjCatCreditor;
    private JMenuItem mjCatCreditorConfig;
    private JMenuItem mjCatCreditorType;
    private JMenuItem mjCatAbpBranchCash;
    private JMenuItem mjCatAbpBranchWarehouse;
    private JMenuItem mjCatAbpBranchCustomer;
    private JMenuItem mjCatAbpBranchVendor;
    private JMenuItem mjCatAbpBranchItem;
    private JMenuItem mjCatCfgTax;

    private JMenu mjBkk;
    private JMenuItem mjBkkExchangeRate;
    private JMenuItem mjBkkPeriod;
    private JMenuItem mjBkkBookkeepingOpening;
    private JMenuItem mjBkkStockOpening;

    private JMenu mjCash;
    private JMenuItem mjCashCash;
    private JMenuItem mjCashCashCur;
    private JMenuItem mjCashBranch;
    private JMenuItem mjCashBranchCur;
    private JMenuItem mjCashMovesIn;
    private JMenuItem mjCashMovesOut;
    private JMenuItem mjCashMovesStcIn;
    private JMenuItem mjCashMovesStcOut;
    private JMenuItem mjCashMovesByMode;
    private JMenuItem mjCashMovesByModeType;

    private JMenu mjDbr;
    private JMenuItem mjDbrBizPartner;
    private JMenuItem mjDbrBizPartnerCur;
    private JMenuItem mjDbrBizPartnerRef;

    private JMenu mjCdr;
    private JMenuItem mjCdrBizPartner;
    private JMenuItem mjCdrBizPartnerCur;
    private JMenuItem mjCdrBizPartnerRef;

    private JMenu mjRep;

    private DFormBookkeepingMoveCustom moFormBookkeepingMoveCustom;
    private DFormBookkeepingMoveCustomDpsClear moFormBookkeepingMoveCustomDpsClear;
    private DFormCashPayment moFormCashPaymentInVen;
    private DFormCashPayment moFormCashPaymentInCus;
    private DFormCashPayment moFormCashPaymentInCdr;
    private DFormCashPayment moFormCashPaymentInDbr;
    private DFormCashPayment moFormCashPaymentOutVen;
    private DFormCashPayment moFormCashPaymentOutCus;
    private DFormCashPayment moFormCashPaymentOutCdr;
    private DFormCashPayment moFormCashPaymentOutDbr;

    public DModModuleFin(DGuiClient client) {
        super(client, DModConsts.MOD_FIN, DLibConsts.UNDEFINED);
        initComponents();
    }

    private void initComponents() {
        mjCat = new JMenu("Catálogos");
        mjCatAccount = new JMenuItem("Cuentas contables");
        mjCatCustomer = new JMenuItem("Clientes");
        mjCatCustomerConfig = new JMenuItem("Configuración de clientes (Q)");
        mjCatCustomerBranch = new JMenuItem("Sucursales de clientes (Q)");
        mjCatCustomerType = new JMenuItem("Tipos de cliente");
        mjCatVendor = new JMenuItem("Proveedores");
        mjCatVendorConfig = new JMenuItem("Configuración de proveedores (Q)");
        mjCatVendorBranch = new JMenuItem("Sucursales de proveedores (Q)");
        mjCatVendorType = new JMenuItem("Tipos de proveedor");
        mjCatDebtor = new JMenuItem("Deudores diversos");
        mjCatDebtorConfig = new JMenuItem("Configuración de deudores diversos (Q)");
        mjCatDebtorType = new JMenuItem("Tipos de deudor diverso");
        mjCatCreditor = new JMenuItem("Acreedores diversos");
        mjCatCreditorConfig = new JMenuItem("Configuración de acreedores diversos (Q)");
        mjCatCreditorType = new JMenuItem("Tipos de acreedor diverso");
        mjCatAbpBranchCash = new JMenuItem("Config. contable para cuentas de dinero");
        mjCatAbpBranchWarehouse = new JMenuItem("Config. contable para almacenes de bienes");
        mjCatAbpBranchCustomer = new JMenuItem("Config. contable para clientes y deudores");
        mjCatAbpBranchVendor = new JMenuItem("Config. contable para proveedores y acreedores");
        mjCatAbpBranchItem = new JMenuItem("Config. contable para ítems");
        mjCatCfgTax = new JMenuItem("Config. impuestos");

        mjCat.add(mjCatAccount);
        mjCat.addSeparator();
        mjCat.add(mjCatCustomer);
        mjCat.add(mjCatCustomerConfig);
        mjCat.add(mjCatCustomerBranch);
        mjCat.add(mjCatCustomerType);
        mjCat.addSeparator();
        mjCat.add(mjCatVendor);
        mjCat.add(mjCatVendorConfig);
        mjCat.add(mjCatVendorBranch);
        mjCat.add(mjCatVendorType);
        mjCat.addSeparator();
        mjCat.add(mjCatDebtor);
        mjCat.add(mjCatDebtorConfig);
        mjCat.add(mjCatDebtorType);
        mjCat.addSeparator();
        mjCat.add(mjCatCreditor);
        mjCat.add(mjCatCreditorConfig);
        mjCat.add(mjCatCreditorType);
        mjCat.addSeparator();
        mjCat.add(mjCatAbpBranchCash);
        mjCat.add(mjCatAbpBranchWarehouse);
        mjCat.add(mjCatAbpBranchCustomer);
        mjCat.add(mjCatAbpBranchVendor);
        mjCat.add(mjCatAbpBranchItem);
        mjCat.addSeparator();
        mjCat.add(mjCatCfgTax);

        // XXX
        mjCatAccount.setEnabled(false);
        mjCatAbpBranchCash.setEnabled(false);
        mjCatAbpBranchWarehouse.setEnabled(false);
        mjCatAbpBranchCustomer.setEnabled(false);
        mjCatAbpBranchVendor.setEnabled(false);
        mjCatAbpBranchItem.setEnabled(false);
        mjCatCfgTax.setEnabled(false);
        // XXX

        mjCatAccount.addActionListener(this);
        mjCatCustomer.addActionListener(this);
        mjCatCustomerConfig.addActionListener(this);
        mjCatCustomerBranch.addActionListener(this);
        mjCatCustomerType.addActionListener(this);
        mjCatVendor.addActionListener(this);
        mjCatVendorConfig.addActionListener(this);
        mjCatVendorBranch.addActionListener(this);
        mjCatVendorType.addActionListener(this);
        mjCatDebtor.addActionListener(this);
        mjCatDebtorConfig.addActionListener(this);
        mjCatDebtorType.addActionListener(this);
        mjCatCreditor.addActionListener(this);
        mjCatCreditorConfig.addActionListener(this);
        mjCatCreditorType.addActionListener(this);
        mjCatAbpBranchCash.addActionListener(this);
        mjCatAbpBranchWarehouse.addActionListener(this);
        mjCatAbpBranchCustomer.addActionListener(this);
        mjCatAbpBranchVendor.addActionListener(this);
        mjCatAbpBranchItem.addActionListener(this);
        mjCatCfgTax.addActionListener(this);

        mjBkk = new JMenu("Contabilidad");
        mjBkkExchangeRate = new JMenuItem("Tipos de cambio");
        mjBkkPeriod = new JMenuItem("Control de períodos contables");
        mjBkkBookkeepingOpening = new JMenuItem("Generación de saldos contables iniciales");
        mjBkkStockOpening = new JMenuItem("Generación de inventarios iniciales");

        mjBkk.add(mjBkkExchangeRate);
        mjBkk.addSeparator();
        mjBkk.add(mjBkkPeriod);
        mjBkk.addSeparator();
        mjBkk.add(mjBkkBookkeepingOpening);
        mjBkk.add(mjBkkStockOpening);

        // XXX
        mjBkkExchangeRate.setEnabled(false);
        mjBkkPeriod.setEnabled(false);
        // XXX

        mjBkkExchangeRate.addActionListener(this);
        mjBkkPeriod.addActionListener(this);
        mjBkkBookkeepingOpening.addActionListener(this);
        mjBkkStockOpening.addActionListener(this);

        mjCash = new JMenu("Cuentas de dinero");
        mjCashCash = new JMenuItem("Dinero disponible en cuentas de dinero");
        mjCashCashCur = new JMenuItem("Dinero disponible en cuentas de dinero por moneda");
        mjCashBranch = new JMenuItem("Dinero disponible en sucursales");
        mjCashBranchCur = new JMenuItem("Dinero disponible en sucursales por moneda");
        mjCashMovesIn = new JMenuItem("Ingresos de dinero");
        mjCashMovesOut = new JMenuItem("Egresos de dinero");
        mjCashMovesStcIn = new JMenuItem("Ingresos de dinero SBC");
        mjCashMovesStcOut = new JMenuItem("Engresos de dinero SBC");
        mjCashMovesByMode = new JMenuItem("Movimientos por forma de pago");
        mjCashMovesByModeType = new JMenuItem("Movimientos por tipo y forma de pago");

        mjCash.add(mjCashCash);
        mjCash.add(mjCashCashCur);
        mjCash.add(mjCashBranch);
        mjCash.add(mjCashBranchCur);
        mjCash.addSeparator();
        mjCash.add(mjCashMovesIn);
        mjCash.add(mjCashMovesOut);
        mjCash.addSeparator();
        mjCash.add(mjCashMovesStcIn);
        mjCash.add(mjCashMovesStcOut);
        mjCash.addSeparator();
        mjCash.add(mjCashMovesByMode);
        mjCash.add(mjCashMovesByModeType);

        mjCashCash.addActionListener(this);
        mjCashCashCur.addActionListener(this);
        mjCashBranch.addActionListener(this);
        mjCashBranchCur.addActionListener(this);
        mjCashMovesIn.addActionListener(this);
        mjCashMovesOut.addActionListener(this);
        mjCashMovesStcIn.addActionListener(this);
        mjCashMovesStcOut.addActionListener(this);
        mjCashMovesByMode.addActionListener(this);
        mjCashMovesByModeType.addActionListener(this);

        mjDbr = new JMenu("Deudores diversos");
        mjDbrBizPartner = new JMenuItem("Saldos deudores diversos");
        mjDbrBizPartnerCur = new JMenuItem("Saldos deudores diversos por moneda");
        mjDbrBizPartnerRef = new JMenuItem("Saldos deudores diversos por referencia");

        mjDbr.add(mjDbrBizPartner);
        mjDbr.add(mjDbrBizPartnerCur);
        mjDbr.add(mjDbrBizPartnerRef);

        mjDbrBizPartner.addActionListener(this);
        mjDbrBizPartnerCur.addActionListener(this);
        mjDbrBizPartnerRef.addActionListener(this);

        mjCdr = new JMenu("Acreedores diversos");
        mjCdrBizPartner = new JMenuItem("Saldos acreedores diversos");
        mjCdrBizPartnerCur = new JMenuItem("Saldos acreedores diversos por moneda");
        mjCdrBizPartnerRef = new JMenuItem("Saldos acreedores diversos por referencia");

        mjCdr.add(mjCdrBizPartner);
        mjCdr.add(mjCdrBizPartnerCur);
        mjCdr.add(mjCdrBizPartnerRef);

        mjCdrBizPartner.addActionListener(this);
        mjCdrBizPartnerCur.addActionListener(this);
        mjCdrBizPartnerRef.addActionListener(this);

        mjRep = new JMenu("Reportes");
    }

    @Override
    public JMenu[] getMenus() {
        return new JMenu[] { mjCat, mjBkk, mjCash, mjDbr, mjCdr/*, mjRep*/ };
    }

    @Override
    public DDbRegistry getRegistry(final int type) {
        DDbRegistry registry = null;

        switch (type) {
            case DModConsts.FS_ACC_CT:
                break;
            case DModConsts.FS_ACC_CL:
                break;
            case DModConsts.FS_ACC_TP:
                break;
            case DModConsts.FS_ACC_SPE_TP:
                break;
            case DModConsts.FS_SYS_ACC_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_sys_acc_tp = " + pk[0] + " "; }
                };
                break;
            case DModConsts.FS_SYS_MOV_CL:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_sys_mov_cl = " + pk[0] + " "; }
                };
                break;
            case DModConsts.FS_SYS_MOV_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_sys_mov_cl = " + pk[0] + " AND id_sys_mov_tp = " + pk[1] + " "; }
                };
                break;
            case DModConsts.FS_DIV_MOV_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_div_mov_tp = " + pk[0] + " "; }
                };
                break;
            case DModConsts.FS_PAY_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_pay_tp = " + pk[0] + " "; }
                };
                break;
            case DModConsts.FS_MOP_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_mop_tp = " + pk[0] + " "; }
                };
                break;
            case DModConsts.FS_VAL_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_val_tp = " + pk[0] + " "; }
                };
                break;
            case DModConsts.FS_TAX_TP:
                break;
            case DModConsts.FS_EXR_TP:
                break;
            case DModConsts.FS_EXR_APP_TP:
                break;
            case DModConsts.FU_REC_TP:
                registry = new DDbRecordType();
                break;
            case DModConsts.FU_ACC:
                registry = new DDbAccount();
                break;
            case DModConsts.FU_TAX:
                registry = new DDbTax();
                break;
            case DModConsts.FU_TAX_REG:
                registry = new DDbTaxRegion();
                break;
            case DModConsts.FU_TAX_GRP:
                registry = new DDbTaxGroup();
                break;
            case DModConsts.FU_TAX_GRP_CFG:
                registry = new DDbTaxGroupConfig();
                break;
            case DModConsts.FU_TAX_GRP_CFG_ROW:
                registry = new DDbTaxGroupConfigRow();
                break;
            case DModConsts.F_YER:
                registry = new DDbYear();
                break;
            case DModConsts.F_YER_PER:
                registry = new DDbYearPeriod();
                break;
            case DModConsts.F_ABP_CSH:
                registry = new DDbAbpBranchCash();
                break;
            case DModConsts.F_ABP_WAH:
                registry = new DDbAbpBranchWarehouse();
                break;
            case DModConsts.F_ABP_BPR:
                registry = new DDbAbpBizPartner();
                break;
            case DModConsts.F_ABP_ITM:
                registry = new DDbAbpItem();
                break;
            case DModConsts.F_EXR:
                registry = new DDbExchangeRate();
                break;
            case DModConsts.F_BKK_REC:
                registry = new DDbBookkeepingRecord();
                break;
            case DModConsts.F_BKK_NUM:
                registry = new DDbBookkeepingNumber();
                break;
            case DModConsts.F_BKK:
                registry = new DDbBookkeepingMove();
                break;
            case DModConsts.FX_BKK_CTM:
            case DModConsts.FX_CTM_DPS_CLR:
                registry = new DDbBookkeepingMoveCustom();
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return registry;
    }

    @Override
    public DGuiCatalogueSettings getCatalogueSettings(final int type, final int subtype, final DGuiParams params) {
        String sql = "";
        String where = "";
        DGuiCatalogueSettings settings = null;

        switch (type) {
            case DModConsts.FS_ACC_CT:
                break;
            case DModConsts.FS_ACC_CL:
                break;
            case DModConsts.FS_ACC_TP:
                break;
            case DModConsts.FS_ACC_SPE_TP:
                break;
            case DModConsts.FS_SYS_ACC_TP:
                settings = new DGuiCatalogueSettings("Tipo cuenta sistema", 1);
                sql = "SELECT id_sys_acc_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.FS_SYS_MOV_CL:
                settings = new DGuiCatalogueSettings("Clase mov. sistema", 1);
                sql = "SELECT id_sys_mov_cl AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.FS_SYS_MOV_TP:
                settings = new DGuiCatalogueSettings("Tipo mov. sistema", 2, 1);
                sql = "SELECT id_sys_mov_cl AS " + DDbConsts.FIELD_ID + "1, id_sys_mov_tp AS " + DDbConsts.FIELD_ID + "2, name AS " + DDbConsts.FIELD_ITEM + ", " +
                        "id_sys_mov_cl AS " + DDbConsts.FIELD_FK + "1 " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.FS_DIV_MOV_TP:
                settings = new DGuiCatalogueSettings("Tipo mov. deudores/acreedores", 1);
                sql = "SELECT id_div_mov_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.FS_PAY_TP:
                settings = new DGuiCatalogueSettings("Tipo pago", 1);
                sql = "SELECT id_pay_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.FS_MOP_TP:
                settings = new DGuiCatalogueSettings("Forma pago", 1, 0, DLibConsts.DATA_TYPE_BOOL);
                if (params != null) {
                    switch (params.getType()) {
                        case DModConsts.FX_MOP_TP_COL:
                            where = "AND id_mop_tp NOT IN (" + DModSysConsts.FS_MOP_TP_NON_DEF + ") ";
                            break;
                        default:
                    }
                }
                sql = "SELECT id_mop_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + ", " +
                        "b_avl AS " + DDbConsts.FIELD_COMP + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 " + where + "ORDER BY sort ";
                break;
            case DModConsts.FS_VAL_TP:
                settings = new DGuiCatalogueSettings("Tipo valor", 1);
                sql = "SELECT id_val_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.FS_TAX_TP:
                break;
            case DModConsts.FS_EXR_TP:
                break;
            case DModConsts.FS_EXR_APP_TP:
                break;
            case DModConsts.FU_REC_TP:
                break;
            case DModConsts.FU_ACC:
                break;
            case DModConsts.FU_TAX:
                break;
            case DModConsts.FU_TAX_REG:
                settings = new DGuiCatalogueSettings("Región impuestos", 1);
                sql = "SELECT id_tax_reg AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_dis = 0 AND b_del = 0 ORDER BY name, id_tax_reg ";
                break;
            case DModConsts.FU_TAX_GRP:
                settings = new DGuiCatalogueSettings("Grupo impuestos", 1);
                sql = "SELECT id_tax_grp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_dis = 0 AND b_del = 0 ORDER BY name, id_tax_grp ";
                break;
            case DModConsts.FU_TAX_GRP_CFG:
                break;
            case DModConsts.FU_TAX_GRP_CFG_ROW:
                break;
            case DModConsts.F_YER:
                break;
            case DModConsts.F_YER_PER:
                break;
            case DModConsts.F_ABP_CSH:
                settings = new DGuiCatalogueSettings("Paquete contable", 1);
                sql = "SELECT id_abp_csh AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, id_abp_csh ";
                break;
            case DModConsts.F_ABP_WAH:
                settings = new DGuiCatalogueSettings("Paquete contable", 1);
                sql = "SELECT id_abp_wah AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, id_abp_wah ";
                break;
            case DModConsts.F_ABP_BPR:
                settings = new DGuiCatalogueSettings("Paquete contable", 1);
                sql = "SELECT id_abp_bpr AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, id_abp_bpr ";
                break;
            case DModConsts.F_ABP_ITM:
                settings = new DGuiCatalogueSettings("Paquete contable", 1);
                sql = "SELECT id_abp_itm AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, id_abp_itm ";
                break;
            case DModConsts.F_EXR:
                break;
            case DModConsts.F_BKK_REC:
                break;
            case DModConsts.F_BKK:
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
        String title = "";
        DGridPaneView view = null;

        switch (type) {
            case DModConsts.FX_BAL_BPR:
                switch (params.getType()) {
                    case DUtilConsts.PER_BPR:
                        title = "Saldos " + DBprUtils.getBizPartnerClassNamePlr(subtype).toLowerCase();
                        break;
                    case DUtilConsts.PER_BPR_BRA:
                        title = "Saldos suc " + DBprUtils.getBizPartnerClassNamePlr(subtype).toLowerCase();
                        break;
                    default:
                        miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }

                switch (params.getSubtype()) {
                    case DUtilConsts.PER_BPR:
                    case DUtilConsts.PER_BPR_BRA:
                        break;
                    case DUtilConsts.PER_CUR:
                        title += " mon";
                        break;
                    case DUtilConsts.PER_REF:
                        title += " ref";
                        break;
                    default:
                }
                view = new DViewBalanceBizPartner(miClient, type, subtype, title, params);
                break;

            case DModConsts.FX_BAL_CSH:
                switch (params.getType()) {
                    case DUtilConsts.PER_BPR_BRA:
                        title = "Dinero sucursales";
                        break;
                    case DUtilConsts.PER_ENT_CSH:
                        title = "Dinero cuentas dinero";
                        break;
                    default:
                        miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }

                switch (params.getSubtype()) {
                    case DUtilConsts.PER_BPR_BRA:
                        break;
                    case DUtilConsts.PER_CUR:
                        title += " mon";
                        break;
                    default:
                }
                view = new DViewBalanceBranchCash(miClient, type, title, params);
                break;

            case DModConsts.FX_BKK_CTM:
                view = new DViewBranchCashMoves(miClient, type, subtype, DFinUtils.getCashMoveNamePlr(subtype) + " dinero");
                break;

            case DModConsts.FX_BKK_CTM_STC:
                view = new DViewBranchCashMoves(miClient, type, subtype, DFinUtils.getCashMoveNamePlr(subtype) + " dinero SBC");
                break;

            case DModConsts.FX_BKK_MOP:
                view = new DViewBranchCashMovesByMode(miClient, type, "Movs x forma pago");
                break;

            case DModConsts.FX_BKK_MOP_SMT:
                view = new DViewBranchCashMovesByMode(miClient, type, "Movs x tipo y forma pago");
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
        String title = "";
        DGuiForm form = null;

        switch (type) {
            case DModConsts.FX_BKK_CTM:
                if (moFormBookkeepingMoveCustom == null) moFormBookkeepingMoveCustom = new DFormBookkeepingMoveCustom(miClient, "Movimiento dinero");
                moFormBookkeepingMoveCustom.setValue(DGuiConsts.PARAM_SYS_MOV_TP, params.getTypeKey());
                form = moFormBookkeepingMoveCustom;
                break;

            case DModConsts.FX_CTM_DPS_CLR:
                if (moFormBookkeepingMoveCustomDpsClear == null) moFormBookkeepingMoveCustomDpsClear = new DFormBookkeepingMoveCustomDpsClear(miClient, "Limpieza saldo");
                form = moFormBookkeepingMoveCustomDpsClear;
                break;

            case DModConsts.FX_CSH_BPR:
                switch (subtype) {
                    case DModSysConsts.FS_SYS_MOV_CL_MI:
                        title = "Cobro " + DBprUtils.getBizPartnerClassNameSng(subtype).toLowerCase();
                        switch (params.getType()) {
                            case DModSysConsts.BS_BPR_CL_VEN:
                                if (moFormCashPaymentInVen == null) moFormCashPaymentInVen = new DFormCashPayment(miClient, type, subtype, title);
                                form = moFormCashPaymentInVen;
                                break;
                            case DModSysConsts.BS_BPR_CL_CUS:
                                if (moFormCashPaymentInCus == null) moFormCashPaymentInCus = new DFormCashPayment(miClient, type, subtype, title);
                                form = moFormCashPaymentInCus;
                                break;
                            case DModSysConsts.BS_BPR_CL_CDR:
                                if (moFormCashPaymentInCdr == null) moFormCashPaymentInCdr = new DFormCashPayment(miClient, type, subtype, title);
                                form = moFormCashPaymentInCdr;
                                break;
                            case DModSysConsts.BS_BPR_CL_DBR:
                                if (moFormCashPaymentInDbr == null) moFormCashPaymentInDbr = new DFormCashPayment(miClient, type, subtype, title);
                                form = moFormCashPaymentInDbr;
                                break;
                            default:
                                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                        }
                        break;

                    case DModSysConsts.FS_SYS_MOV_CL_MO:
                        title = "Pago " + DBprUtils.getBizPartnerClassNameSng(subtype).toLowerCase();
                        switch (params.getType()) {
                            case DModSysConsts.BS_BPR_CL_VEN:
                                if (moFormCashPaymentOutVen == null) moFormCashPaymentOutVen = new DFormCashPayment(miClient, type, subtype, title);
                                form = moFormCashPaymentOutVen;
                                break;
                            case DModSysConsts.BS_BPR_CL_CUS:
                                if (moFormCashPaymentOutCus == null) moFormCashPaymentOutCus = new DFormCashPayment(miClient, type, subtype, title);
                                form = moFormCashPaymentOutCus;
                                break;
                            case DModSysConsts.BS_BPR_CL_CDR:
                                if (moFormCashPaymentOutCdr == null) moFormCashPaymentOutCdr = new DFormCashPayment(miClient, type, subtype, title);
                                form = moFormCashPaymentOutCdr;
                                break;
                            case DModSysConsts.BS_BPR_CL_DBR:
                                if (moFormCashPaymentOutDbr == null) moFormCashPaymentOutDbr = new DFormCashPayment(miClient, type, subtype, title);
                                form = moFormCashPaymentOutDbr;
                                break;
                            default:
                                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                        }
                        break;

                    default:
                        miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
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

            if (menuItem == mjCatCustomer) {
                miClient.getSession().showView(DModConsts.BU_BPR, DModSysConsts.BS_BPR_CL_CUS, null);
            }
            else if (menuItem == mjCatCustomerConfig) {
                miClient.getSession().showView(DModConsts.BU_BPR_CFG, DModSysConsts.BS_BPR_CL_CUS, null);
            }
            else if (menuItem == mjCatCustomerBranch) {
                miClient.getSession().showView(DModConsts.BU_BRA, DModSysConsts.BS_BPR_CL_CUS, null);
            }
            else if (menuItem == mjCatCustomerType) {
                miClient.getSession().showView(DModConsts.BU_BPR_TP, DModSysConsts.BS_BPR_CL_CUS, null);
            }
            else if (menuItem == mjCatVendor) {
                miClient.getSession().showView(DModConsts.BU_BPR, DModSysConsts.BS_BPR_CL_VEN, null);
            }
            else if (menuItem == mjCatVendorConfig) {
                miClient.getSession().showView(DModConsts.BU_BPR_CFG, DModSysConsts.BS_BPR_CL_VEN, null);
            }
            else if (menuItem == mjCatVendorBranch) {
                miClient.getSession().showView(DModConsts.BU_BRA, DModSysConsts.BS_BPR_CL_VEN, null);
            }
            else if (menuItem == mjCatVendorType) {
                miClient.getSession().showView(DModConsts.BU_BPR_TP, DModSysConsts.BS_BPR_CL_VEN, null);
            }
            else if (menuItem == mjCatDebtor) {
                miClient.getSession().showView(DModConsts.BU_BPR, DModSysConsts.BS_BPR_CL_DBR, null);
            }
            else if (menuItem == mjCatDebtorConfig) {
                miClient.getSession().showView(DModConsts.BU_BPR_CFG, DModSysConsts.BS_BPR_CL_DBR, null);
            }
            else if (menuItem == mjCatDebtorType) {
                miClient.getSession().showView(DModConsts.BU_BPR_TP, DModSysConsts.BS_BPR_CL_DBR, null);
            }
            else if (menuItem == mjCatCreditor) {
                miClient.getSession().showView(DModConsts.BU_BPR, DModSysConsts.BS_BPR_CL_CDR, null);
            }
            else if (menuItem == mjCatCreditorConfig) {
                miClient.getSession().showView(DModConsts.BU_BPR_CFG, DModSysConsts.BS_BPR_CL_CDR, null);
            }
            else if (menuItem == mjCatCreditorType) {
                miClient.getSession().showView(DModConsts.BU_BPR_TP, DModSysConsts.BS_BPR_CL_CDR, null);
            }
            else if (menuItem == mjBkkExchangeRate) {

            }
            else if (menuItem == mjBkkPeriod) {

            }
            else if (menuItem == mjBkkBookkeepingOpening) {
                new DDialogBookkeepingOpening(miClient).setVisible(true);
            }
            else if (menuItem == mjBkkStockOpening) {
                new DDialogStockOpening(miClient).setVisible(true);
            }
            else if (menuItem == mjCashCash) {
                showView(DModConsts.FX_BAL_CSH, DLibConsts.UNDEFINED, new DGuiParams(DUtilConsts.PER_ENT_CSH, DUtilConsts.PER_ENT_CSH));
            }
            else if (menuItem == mjCashCashCur) {
                showView(DModConsts.FX_BAL_CSH, DLibConsts.UNDEFINED, new DGuiParams(DUtilConsts.PER_ENT_CSH, DUtilConsts.PER_CUR));
            }
            else if (menuItem == mjCashBranch) {
                showView(DModConsts.FX_BAL_CSH, DLibConsts.UNDEFINED, new DGuiParams(DUtilConsts.PER_BPR_BRA, DUtilConsts.PER_BPR_BRA));
            }
            else if (menuItem == mjCashBranchCur) {
                showView(DModConsts.FX_BAL_CSH, DLibConsts.UNDEFINED, new DGuiParams(DUtilConsts.PER_BPR_BRA, DUtilConsts.PER_CUR));
            }
            else if (menuItem == mjCashMovesIn) {
                showView(DModConsts.FX_BKK_CTM, DModSysConsts.FS_SYS_MOV_CL_MI, null);
            }
            else if (menuItem == mjCashMovesOut) {
                showView(DModConsts.FX_BKK_CTM, DModSysConsts.FS_SYS_MOV_CL_MO, null);
            }
            else if (menuItem == mjCashMovesStcIn) {
                showView(DModConsts.FX_BKK_CTM_STC, DModSysConsts.FS_SYS_MOV_CL_MI, null);
            }
            else if (menuItem == mjCashMovesStcOut) {
                showView(DModConsts.FX_BKK_CTM_STC, DModSysConsts.FS_SYS_MOV_CL_MO, null);
            }
            else if (menuItem == mjCashMovesByMode) {
                showView(DModConsts.FX_BKK_MOP, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCashMovesByModeType) {
                showView(DModConsts.FX_BKK_MOP_SMT, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjDbrBizPartner) {
                showView(DModConsts.FX_BAL_BPR, DModSysConsts.BS_BPR_CL_DBR, new DGuiParams(DUtilConsts.PER_BPR, DUtilConsts.PER_BPR));
            }
            else if (menuItem == mjDbrBizPartnerCur) {
                showView(DModConsts.FX_BAL_BPR, DModSysConsts.BS_BPR_CL_DBR, new DGuiParams(DUtilConsts.PER_BPR, DUtilConsts.PER_CUR));
            }
            else if (menuItem == mjDbrBizPartnerRef) {
                showView(DModConsts.FX_BAL_BPR, DModSysConsts.BS_BPR_CL_DBR, new DGuiParams(DUtilConsts.PER_BPR, DUtilConsts.PER_REF));
            }
            else if (menuItem == mjCdrBizPartner) {
                showView(DModConsts.FX_BAL_BPR, DModSysConsts.BS_BPR_CL_CDR, new DGuiParams(DUtilConsts.PER_BPR, DUtilConsts.PER_BPR));
            }
            else if (menuItem == mjCdrBizPartnerCur) {
                showView(DModConsts.FX_BAL_BPR, DModSysConsts.BS_BPR_CL_CDR, new DGuiParams(DUtilConsts.PER_BPR, DUtilConsts.PER_CUR));
            }
            else if (menuItem == mjCdrBizPartnerRef) {
                showView(DModConsts.FX_BAL_BPR, DModSysConsts.BS_BPR_CL_CDR, new DGuiParams(DUtilConsts.PER_BPR, DUtilConsts.PER_REF));
            }
        }
    }
}
