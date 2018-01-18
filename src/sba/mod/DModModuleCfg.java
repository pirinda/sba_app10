/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.grid.DGridPaneView;
import sba.lib.gui.DGuiCatalogueSettings;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiForm;
import sba.lib.gui.DGuiModule;
import sba.lib.gui.DGuiOptionPicker;
import sba.lib.gui.DGuiParams;
import sba.lib.gui.DGuiReport;
import sba.mod.bpr.db.DDbBizPartner;
import sba.mod.cfg.db.DDbBranchCash;
import sba.mod.cfg.db.DDbBranchWarehouse;
import sba.mod.cfg.db.DDbCertificate;
import sba.mod.cfg.db.DDbConfigBranch;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.cfg.db.DDbLock;
import sba.mod.cfg.db.DDbSysCountry;
import sba.mod.cfg.db.DDbSysCurrency;
import sba.mod.cfg.db.DDbSysCurrencyDenomination;
import sba.mod.cfg.db.DDbSysTaxRegime;
import sba.mod.cfg.db.DDbSysXmlSignatureProvider;
import sba.mod.cfg.db.DDbUser;
import sba.mod.cfg.db.DDbUserBranch;
import sba.mod.cfg.db.DDbUserBranchCash;
import sba.mod.cfg.db.DDbUserBranchDpsSeries;
import sba.mod.cfg.db.DDbUserBranchWarehouse;
import sba.mod.cfg.db.DDbUserCompany;
import sba.mod.cfg.db.DDbUserCustomType;
import sba.mod.cfg.db.DDbUserGui;
import sba.mod.cfg.db.DDbUserPrivilege;
import sba.mod.cfg.db.DLockUtils;
import sba.mod.cfg.form.DDialogUserBranch;
import sba.mod.cfg.form.DFormBranchCash;
import sba.mod.cfg.form.DFormBranchWarehouse;
import sba.mod.cfg.form.DFormUser;
import sba.mod.cfg.form.DFormUserCustomType;
import sba.mod.cfg.view.DViewBranchCash;
import sba.mod.cfg.view.DViewBranchWarehouse;
import sba.mod.cfg.view.DViewCountry;
import sba.mod.cfg.view.DViewCurrency;
import sba.mod.cfg.view.DViewCurrencyDenomination;
import sba.mod.cfg.view.DViewUser;
import sba.mod.cfg.view.DViewUserBranch;
import sba.mod.cfg.view.DViewUserBranchCash;
import sba.mod.cfg.view.DViewUserBranchDpsSeries;
import sba.mod.cfg.view.DViewUserBranchWarehouse;
import sba.mod.cfg.view.DViewUserCompany;
import sba.mod.cfg.view.DViewUserCustomType;
import sba.mod.cfg.view.DViewUserPrivilege;

/**
 *
 * @author Sergio Flores
 */
public class DModModuleCfg extends DGuiModule implements ActionListener {

    private JMenu mjCat;
    private JMenuItem mjCatCompany;
    private JMenuItem mjCatBranch;
    private JMenuItem mjCatBranchCash;
    private JMenuItem mjCatBranchWarehouse;
    private JMenuItem mjCatDpsSeries;
    private JMenuItem mjCatDpsSeriesBranch;
    private JMenuItem mjCatDpsSeriesNumber;
    private JMenuItem mjCatXmlStampsAvailable;
    private JMenuItem mjCatXmlSignatureMove;
    private JMenuItem mjCatCountry;
    private JMenuItem mjCatCurrency;
    private JMenuItem mjCatCurrencyDenomination;
    private JMenu mjUsr;
    private JMenuItem mjUsrUser;
    private JMenuItem mjUsrUserCustomType;
    private JMenuItem mjUsrUserPrivilege;
    private JMenuItem mjUsrUserCompany;
    private JMenuItem mjUsrUserBranch;
    private JMenuItem mjUsrUserBranchCash;
    private JMenuItem mjUsrUserBranchWarehouse;
    private JMenuItem mjUsrUserBranchDpsSeries;
    private JMenuItem mjUsrDeleteAllActiveLocks;
    private JMenu mjSys;
    private JMenuItem mjSysConfigCompany;
    private JMenuItem mjSysConfigBranch;
    private JMenu mjRep;

    private DFormBranchCash moFormBranchCash;
    private DFormBranchWarehouse moFormBranchWarehouse;
    private DFormUser moFormUser;
    private DFormUserCustomType moFormUserCustomType;
    private DDialogUserBranch moFormUserBranch;

    public DModModuleCfg(DGuiClient client) {
        super(client, DModConsts.MOD_CFG, DLibConsts.UNDEFINED);
        initComponents();
    }

    private void initComponents() {
        mjCat = new JMenu("Catálogos");
        mjCatCompany = new JMenuItem("Empresa");
        mjCatBranch = new JMenuItem("Sucursales");
        mjCatBranchCash = new JMenuItem("Cuentas de dinero");
        mjCatBranchWarehouse = new JMenuItem("Almacenes de bienes");
        mjCatDpsSeries = new JMenuItem("Series de documentos");
        mjCatDpsSeriesBranch = new JMenuItem("Series de documentos y sucursales (Q)");
        mjCatDpsSeriesNumber = new JMenuItem("Folios de documentos");
        mjCatXmlStampsAvailable = new JMenuItem("Timbres disponibles");
        mjCatXmlSignatureMove = new JMenuItem("Movimientos de timbrado");
        mjCatCountry = new JMenuItem("Países");
        mjCatCurrency = new JMenuItem("Monedas");
        mjCatCurrencyDenomination = new JMenuItem("Denominaciones de monedas");
        mjCat.add(mjCatCompany);
        mjCat.add(mjCatBranch);
        mjCat.add(mjCatBranchCash);
        mjCat.add(mjCatBranchWarehouse);
        mjCat.addSeparator();
        mjCat.add(mjCatDpsSeries);
        mjCat.add(mjCatDpsSeriesBranch);
        mjCat.add(mjCatDpsSeriesNumber);
        mjCat.addSeparator();
        mjCat.add(mjCatXmlStampsAvailable);
        mjCat.add(mjCatXmlSignatureMove);
        mjCat.addSeparator();
        mjCat.add(mjCatCountry);
        mjCat.add(mjCatCurrency);
        mjCat.add(mjCatCurrencyDenomination);

        // XXX
        mjCatCompany.setEnabled(false);
        mjCatBranch.setEnabled(false);
        // XXX

        mjUsr = new JMenu("Usuarios");
        mjUsrUser = new JMenuItem("Usuarios");
        mjUsrUserCustomType = new JMenuItem("Tipos propios de usuarios");
        mjUsrUserPrivilege = new JMenuItem("Permisos de usuarios (Q)");
        mjUsrUserCompany = new JMenuItem("Usuarios y empresa (Q)");
        mjUsrUserBranch = new JMenuItem("Usuarios y sucursales (Q)");
        mjUsrUserBranchCash = new JMenuItem("Usuarios y cuentas de dinero (Q)");
        mjUsrUserBranchWarehouse = new JMenuItem("Usuarios y almacenes de bienes (Q)");
        mjUsrUserBranchDpsSeries = new JMenuItem("Usuarios y series de documentos (Q)");
        mjUsrDeleteAllActiveLocks = new JMenuItem("Eliminar todos los bloqueos a registros...");
        mjUsr.add(mjUsrUser);
        mjUsr.add(mjUsrUserCustomType);
        mjUsr.addSeparator();
        mjUsr.add(mjUsrUserPrivilege);
        mjUsr.addSeparator();
        mjUsr.add(mjUsrUserCompany);
        mjUsr.add(mjUsrUserBranch);
        mjUsr.add(mjUsrUserBranchCash);
        mjUsr.add(mjUsrUserBranchWarehouse);
        mjUsr.add(mjUsrUserBranchDpsSeries);
        mjUsr.addSeparator();
        mjUsr.add(mjUsrDeleteAllActiveLocks);

        mjSys = new JMenu("Sistema");
        mjSysConfigCompany = new JMenuItem("Configuración de empresa");
        mjSysConfigBranch = new JMenuItem("Configuración de sucursales");
        mjSys.add(mjSysConfigCompany);
        mjSys.add(mjSysConfigBranch);

        // XXX
        mjSysConfigCompany.setEnabled(false);
        mjSysConfigBranch.setEnabled(false);
        // XXX

        mjRep = new JMenu("Reportes");

        mjCat.setEnabled(miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_CFG_CAT) || miClient.getSession().getUser().isAdministrator());
        mjUsr.setEnabled(miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_CFG_USR) || miClient.getSession().getUser().isSupervisor());
        mjSys.setEnabled(miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_CFG_SYS) || miClient.getSession().getUser().isAdministrator());
        mjRep.setEnabled(miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_CFG_REP));

        mjCatCompany.addActionListener(this);
        mjCatBranch.addActionListener(this);
        mjCatBranchCash.addActionListener(this);
        mjCatBranchWarehouse.addActionListener(this);
        mjCatDpsSeries.addActionListener(this);
        mjCatDpsSeriesBranch.addActionListener(this);
        mjCatDpsSeriesNumber.addActionListener(this);
        mjCatXmlStampsAvailable.addActionListener(this);
        mjCatXmlSignatureMove.addActionListener(this);
        mjCatCountry.addActionListener(this);
        mjCatCurrency.addActionListener(this);
        mjCatCurrencyDenomination.addActionListener(this);
        mjUsrUser.addActionListener(this);
        mjUsrUserCustomType.addActionListener(this);
        mjUsrUserPrivilege.addActionListener(this);
        mjUsrUserCompany.addActionListener(this);
        mjUsrUserBranch.addActionListener(this);
        mjUsrUserBranchCash.addActionListener(this);
        mjUsrUserBranchWarehouse.addActionListener(this);
        mjUsrUserBranchDpsSeries.addActionListener(this);
        mjUsrDeleteAllActiveLocks.addActionListener(this);
        mjSysConfigCompany.addActionListener(this);
        mjSysConfigBranch.addActionListener(this);
    }
    
    private void actionPerformedDeleteAllActiveLocks() {
        if (miClient.showMsgBoxConfirm("¿Está seguro que desea eliminar todos los bloqueos a registros?") == JOptionPane.YES_OPTION) {
            try {
                DLockUtils.deleteActiveLocks(miClient.getSession());
                miClient.showMsgBoxInformation(DLibConsts.MSG_PROCESS_FINISHED);
            }
            catch (Exception e) {
                DLibUtils.showException(this, e);
            }
        }
    }

    @Override
    public JMenu[] getMenus() {
        return new JMenu[] { mjCat, mjUsr, mjSys/*, mjRep*/ };
    }

    @Override
    public DDbRegistry getRegistry(final int type, final DGuiParams params) {
        DDbRegistry registry = null;

        switch (type) {
            case DModConsts.CS_MOD:
                break;
            case DModConsts.CS_PRV:
                break;
            case DModConsts.CS_LEV:
                break;
            case DModConsts.CS_CTY:
                registry = new DDbSysCountry();
                break;
            case DModConsts.CS_CUR:
                registry = new DDbSysCurrency();
                break;
            case DModConsts.CS_CUR_DEN:
                registry = new DDbSysCurrencyDenomination();
                break;
            case DModConsts.CS_XSP:
                registry = new DDbSysXmlSignatureProvider();
                break;
            case DModConsts.CS_TAX_REG:
                registry = new DDbSysTaxRegime();
                break;
            case DModConsts.CU_CFG_CO:
                registry = new DDbConfigCompany();
                break;
            case DModConsts.CU_CFG_BRA:
                registry = new DDbConfigBranch();
                break;
            case DModConsts.CU_CSH:
                registry = new DDbBranchCash();
                break;
            case DModConsts.CU_WAH:
                registry = new DDbBranchWarehouse();
                break;
            case DModConsts.CU_USR_CTM_TP:
                registry = new DDbUserCustomType();
                break;
            case DModConsts.CU_USR:
                registry = new DDbUser();
                break;
            case DModConsts.CU_USR_PRV:
                registry = new DDbUserPrivilege();
                break;
            case DModConsts.CU_USR_CO:
                registry = new DDbUserCompany();
                break;
            case DModConsts.CU_USR_BRA:
                registry = new DDbUserBranch();
                break;
            case DModConsts.CU_USR_CSH:
                registry = new DDbUserBranchCash();
                break;
            case DModConsts.CU_USR_WAH:
                registry = new DDbUserBranchWarehouse();
                break;
            case DModConsts.CU_USR_SER_BRA:
                registry = new DDbUserBranchDpsSeries();
                break;
            case DModConsts.CU_CER:
                registry = new DDbCertificate();
                break;
            case DModConsts.C_USR_GUI:
                registry = new DDbUserGui();
                break;
            case DModConsts.C_LOCK:
                registry = new DDbLock();
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
            case DModConsts.CS_CSH_TP:
                settings = new DGuiCatalogueSettings("Tipo cuenta dinero", 1);
                sql = "SELECT id_csh_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.CS_WAH_TP:
                settings = new DGuiCatalogueSettings("Tipo almacén", 1);
                sql = "SELECT id_wah_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.CS_POS_ASA_TP:
                settings = new DGuiCatalogueSettings("Tipo acción post-venta", 1);
                sql = "SELECT id_pos_asa_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.CS_POS_ASP_TP:
                settings = new DGuiCatalogueSettings("Tipo impresión post-venta", 1);
                sql = "SELECT id_pos_asp_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.CS_USR_TP:
                settings = new DGuiCatalogueSettings("Tipo usuario", 1);
                sql = "SELECT id_usr_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.CS_ACS_TP:
                settings = new DGuiCatalogueSettings("Tipo acceso", 1);
                sql = "SELECT id_acs_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.CS_MOD:
                settings = new DGuiCatalogueSettings("Módulo", 1);
                sql = "SELECT id_mod AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.CS_PRV:
                settings = new DGuiCatalogueSettings("Permiso", 1, 1, DLibConsts.DATA_TYPE_BOOL);
                sql = "SELECT id_prv AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + ", " +
                        "fk_mod AS " + DDbConsts.FIELD_FK + "1, b_lev AS " + DDbConsts.FIELD_COMP + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY fk_mod, sort ";
                break;
            case DModConsts.CS_LEV:
                settings = new DGuiCatalogueSettings("Nivel", 1);
                sql = "SELECT id_lev AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.CS_CTY:
                settings = new DGuiCatalogueSettings("País", 1);
                settings.setCodeApplying(true);
                settings.setCodeVisible(false);
                sql = "SELECT id_cty AS " + DDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name) AS " + DDbConsts.FIELD_ITEM + ", code AS " + DDbConsts.FIELD_CODE + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.CS_CUR:
                settings = new DGuiCatalogueSettings("Moneda", 1);
                settings.setCodeApplying(true);
                settings.setCodeVisible(false);
                sql = "SELECT id_cur AS " + DDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name) AS " + DDbConsts.FIELD_ITEM + ", code AS " + DDbConsts.FIELD_CODE + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.CS_CUR_DEN:
                settings = new DGuiCatalogueSettings("Denominación moneda", 1);
                sql = "SELECT id_cur AS " + DDbConsts.FIELD_ID + "1, id_den AS " + DDbConsts.FIELD_ID + "2, " +
                        "name AS " + DDbConsts.FIELD_ITEM + ", id_den AS " + DDbConsts.FIELD_FK + "1 " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY den ";
                break;
            case DModConsts.CS_EMS_TP:
                settings = new DGuiCatalogueSettings("Tipo configuración e-mail", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_ems_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + ", code AS " + DDbConsts.FIELD_CODE + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.CS_XSP:
                settings = new DGuiCatalogueSettings("Proveedor certificación", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_xsp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + ", code AS " + DDbConsts.FIELD_CODE + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.CS_TAX_REG:
                settings = new DGuiCatalogueSettings("Régimen fiscal", 1);
                sql = "SELECT id_tax_reg AS " + DDbConsts.FIELD_ID + "1, CONCAT(code, ' - ', name) AS " + DDbConsts.FIELD_ITEM + ", code AS " + DDbConsts.FIELD_CODE + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND " +
                        "(id_tax_reg = " + DModSysConsts.CS_TAX_REG_NA + " OR " + (((DDbBizPartner) miClient.getSession().readRegistry(DModConsts.BU_BPR, new int[] { DUtilConsts.BPR_CO_ID })).getFkIdentityTypeId() == DModSysConsts.BS_IDY_TP_PER ? "b_per" : "b_org") + ") " +
                        "ORDER BY sort ";
                break;
            case DModConsts.CU_CFG_CO:
                break;
            case DModConsts.CU_CFG_BRA:
                break;
            case DModConsts.CU_CSH:
                break;
            case DModConsts.CU_WAH:
                break;
            case DModConsts.CU_USR_CTM_TP:
                settings = new DGuiCatalogueSettings("Tipo propio usuario", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_usr_ctm_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + ", code AS " + DDbConsts.FIELD_CODE + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, id_usr_ctm_tp ";
                break;
            case DModConsts.CU_USR:
                settings = new DGuiCatalogueSettings("Usuario", 1);
                sql = "SELECT id_usr AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 AND fk_usr_tp = " + DModSysConsts.CS_USR_TP_USR + " ORDER BY name, id_usr ";
                break;
            case DModConsts.CU_USR_PRV:
                break;
            case DModConsts.CU_USR_CO:
                break;
            case DModConsts.CU_USR_BRA:
                break;
            case DModConsts.CU_USR_CSH:
                break;
            case DModConsts.CU_USR_WAH:
                break;
            case DModConsts.CU_USR_SER_BRA:
                break;
            case DModConsts.CU_CER:
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
            case DModConsts.CS_CTY:
                view = new DViewCountry(miClient, "Países");
                break;
            case DModConsts.CS_CUR:
                view = new DViewCurrency(miClient, "Monedas");
                break;
            case DModConsts.CS_CUR_DEN:
                view = new DViewCurrencyDenomination(miClient, "Denoms monedas");
                break;
            case DModConsts.CU_CFG_CO:
                break;
            case DModConsts.CU_CFG_BRA:
                break;
            case DModConsts.CU_CSH:
                view = new DViewBranchCash(miClient, "Cuentas dinero");
                break;
            case DModConsts.CU_WAH:
                view = new DViewBranchWarehouse(miClient, "Almacenes");
                break;
            case DModConsts.CU_USR_CTM_TP:
                view = new DViewUserCustomType(miClient, "Tipos propios usuarios");
                break;
            case DModConsts.CU_USR:
                view = new DViewUser(miClient, "Usuarios");
                break;
            case DModConsts.CU_USR_PRV:
                view = new DViewUserPrivilege(miClient, "Permisos usuarios Q");
                break;
            case DModConsts.CU_USR_CO:
                view = new DViewUserCompany(miClient, "Usuarios y empresa Q");
                break;
            case DModConsts.CU_USR_BRA:
                view = new DViewUserBranch(miClient, "Usuarios y sucursales Q");
                break;
            case DModConsts.CU_USR_CSH:
                view = new DViewUserBranchCash(miClient, "Usuarios y cuentas dinero Q");
                break;
            case DModConsts.CU_USR_WAH:
                view = new DViewUserBranchWarehouse(miClient, "Usuarios y almacenes Q");
                break;
            case DModConsts.CU_USR_SER_BRA:
                view = new DViewUserBranchDpsSeries(miClient, "Usuarios y folios doctos Q");
                break;
            case DModConsts.CU_CER:
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
            case DModConsts.CU_CFG_CO:
                break;
            case DModConsts.CU_CFG_BRA:
                break;
            case DModConsts.CU_CSH:
                if (moFormBranchCash == null) moFormBranchCash = new DFormBranchCash(miClient, "Cuenta de dinero");
                form = moFormBranchCash;
                break;
            case DModConsts.CU_WAH:
                if (moFormBranchWarehouse == null) moFormBranchWarehouse = new DFormBranchWarehouse(miClient, "Almacén de bienes");
                form = moFormBranchWarehouse;
                break;
            case DModConsts.CU_USR_CTM_TP:
                if (moFormUserCustomType == null) moFormUserCustomType = new DFormUserCustomType(miClient, "Tipo propio usuario");
                form = moFormUserCustomType;
                break;
            case DModConsts.CU_USR:
                if (moFormUser == null) moFormUser = new DFormUser(miClient, "Usuario");
                form = moFormUser;
                break;
            case DModConsts.CU_USR_PRV:
                break;
            case DModConsts.CU_USR_CO:
                break;
            case DModConsts.CU_USR_BRA:
                if (moFormUserBranch == null) moFormUserBranch = new DDialogUserBranch(miClient, "Usuario y sucursal");
                form = moFormUserBranch;
                break;
            case DModConsts.CU_USR_CSH:
                break;
            case DModConsts.CU_USR_WAH:
                break;
            case DModConsts.CU_USR_SER_BRA:
                break;
            case DModConsts.CU_CER:
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

            if (menuItem == mjCatBranchCash) {
                showView(DModConsts.CU_CSH, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatBranchWarehouse) {
                showView(DModConsts.CU_WAH, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatDpsSeries) {
                miClient.getSession().showView(DModConsts.TU_SER, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatDpsSeriesBranch) {
                miClient.getSession().showView(DModConsts.TU_SER_BRA, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatDpsSeriesNumber) {
                miClient.getSession().showView(DModConsts.TU_SER_NUM, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatXmlStampsAvailable) {
                miClient.getSession().showView(DModConsts.TX_XSM_AVA, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatXmlSignatureMove) {
                miClient.getSession().showView(DModConsts.T_XSM, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatCountry) {
                showView(DModConsts.CS_CTY, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatCurrency) {
                showView(DModConsts.CS_CUR, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatCurrencyDenomination) {
                showView(DModConsts.CS_CUR_DEN, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjUsrUser) {
                showView(DModConsts.CU_USR, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjUsrUserCustomType) {
                showView(DModConsts.CU_USR_CTM_TP, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjUsrUserPrivilege) {
                showView(DModConsts.CU_USR_PRV, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjUsrUserCompany) {
                showView(DModConsts.CU_USR_CO, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjUsrUserBranch) {
                showView(DModConsts.CU_USR_BRA, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjUsrUserBranchCash) {
                showView(DModConsts.CU_USR_CSH, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjUsrUserBranchWarehouse) {
                showView(DModConsts.CU_USR_WAH, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjUsrUserBranchDpsSeries) {
                showView(DModConsts.CU_USR_SER_BRA, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjUsrDeleteAllActiveLocks) {
                actionPerformedDeleteAllActiveLocks();
            }
        }
    }
}
