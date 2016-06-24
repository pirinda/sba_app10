/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod;

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
import sba.lib.gui.DGuiForm;
import sba.lib.gui.DGuiModule;
import sba.lib.gui.DGuiOptionPicker;
import sba.lib.gui.DGuiParams;
import sba.lib.gui.DGuiReport;
import sba.mod.srv.db.DDbEquipment;
import sba.mod.srv.db.DDbReparation;
import sba.mod.srv.db.DDbReparationLog;
import sba.mod.srv.form.DFormEquipment;
import sba.mod.srv.form.DFormReparation;
import sba.mod.srv.view.DViewEquipment;
import sba.mod.srv.view.DViewReparation;

/**
 *
 * @author Sergio Flores
 */
public class DModModuleSrv extends DGuiModule implements ActionListener {

    private JMenu mjCat;
    private JMenuItem mjCatCustomer;
    private JMenuItem mjCatEquipment;
    private JMenuItem mjCatBrand;

    private JMenu mjRep;
    private JMenuItem mjRepNew;
    private JMenuItem mjRepApr;
    private JMenuItem mjRepRej;
    private JMenuItem mjRepFin;
    private JMenuItem mjRepDvyRep;
    private JMenuItem mjRepDvyDvy;
    private JMenuItem mjRepAll;

    private DFormEquipment moFormEquipment;
    private DFormReparation moFormReparation;

    public DModModuleSrv(DGuiClient client) {
        super(client, DModConsts.MOD_SRV, DLibConsts.UNDEFINED);
        initComponents();
    }

    /*
     * Private methods
     */

    private void initComponents() {
        mjCat = new JMenu("Catálogos");
        mjCatCustomer = new JMenuItem("Clientes");
        mjCatEquipment = new JMenuItem("Equipos");
        mjCatBrand = new JMenuItem("Marcas");

        mjCat.add(mjCatCustomer);
        mjCat.add(mjCatEquipment);
        mjCat.add(mjCatBrand);

        mjCatCustomer.addActionListener(this);
        mjCatEquipment.addActionListener(this);
        mjCatBrand.addActionListener(this);

        mjRep = new JMenu("Reparaciones");
        mjRep.setVisible(false);
        mjRepNew = new JMenuItem("Reparaciones nuevas");
        mjRepApr = new JMenuItem("Reparaciones aprobadas");
        mjRepRej = new JMenuItem("Reparaciones rechazadas");
        mjRepFin = new JMenuItem("Reparaciones terminadas");
        mjRepDvyRep = new JMenuItem("Reparaciones entregadas");
        mjRepDvyDvy = new JMenuItem("Reparaciones entregadas por fecha de entrega");
        mjRepAll = new JMenuItem("Todas las reparaciones");

        mjRep.add(mjRepNew);
        mjRep.add(mjRepApr);
        mjRep.add(mjRepRej);
        mjRep.add(mjRepFin);
        mjRep.add(mjRepDvyRep);
        mjRep.add(mjRepDvyDvy);
        mjRep.addSeparator();
        mjRep.add(mjRepAll);

        mjRepNew.addActionListener(this);
        mjRepApr.addActionListener(this);
        mjRepRej.addActionListener(this);
        mjRepFin.addActionListener(this);
        mjRepDvyRep.addActionListener(this);
        mjRepDvyDvy.addActionListener(this);
        mjRepAll.addActionListener(this);

        mjCat.setEnabled(miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_SRV_CAT));
        mjRep.setEnabled(miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_SRV_SRV));
    }

    /*
     * Public methods
     */

    public boolean isVisibleMenuRep() {
        return mjRep.isVisible();
    }

    public void setVisibleMenuRep(final boolean visible) {
        mjRep.setVisible(visible);
    }

    /*
     * Overriden methods
     */

    @Override
    public JMenu[] getMenus() {
        return new JMenu[] { mjCat, mjRep };
    }

    @Override
    public DDbRegistry getRegistry(final int type, final DGuiParams params) {
        DDbRegistry registry = null;

        switch (type) {
            case DModConsts.SS_SRV_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_srv_tp = " + pk[0] + " "; }
                };
                break;
            case DModConsts.SS_SRV_ST:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_srv_tp = " + pk[0] + " AND id_srv_st = " + pk[1] + " "; }
                };
                break;
            case DModConsts.SU_EQU:
                registry = new DDbEquipment();
                break;
            case DModConsts.S_REP:
                registry = new DDbReparation();
                break;
            case DModConsts.S_REP_LOG:
                registry = new DDbReparationLog();
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
            case DModConsts.SS_SRV_TP:
                settings = new DGuiCatalogueSettings("Tipo servicio", 1);
                sql = "SELECT id_srv_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.SS_SRV_ST:
                settings = new DGuiCatalogueSettings("Estatus servicio", 2, 1);
                sql = "SELECT id_srv_tp AS " + DDbConsts.FIELD_ID + "1, id_srv_st AS " + DDbConsts.FIELD_ID + "2, name AS " + DDbConsts.FIELD_ITEM + ", " +
                        "id_srv_tp AS " + DDbConsts.FIELD_FK + "1 " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY id_srv_tp, sort ";
                break;
            case DModConsts.SU_EQU:
                settings = new DGuiCatalogueSettings("Equipo", 1);
                sql = "SELECT id_equ AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY name, id_equ ";
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
            case DModConsts.SU_EQU:
                view = new DViewEquipment(miClient, "Equipos");
                break;
            case DModConsts.S_REP:
                switch (subtype) {
                    case DLibConsts.UNDEFINED:
                        view = new DViewReparation(miClient, subtype, "Reparaciones (todas)");
                        break;
                    case DModSysConsts.SX_SRV_ST_REP_NEW:
                        view = new DViewReparation(miClient, subtype, "Reparaciones nuevas");
                        break;
                    case DModSysConsts.SX_SRV_ST_REP_APR:
                        view = new DViewReparation(miClient, subtype, "Reparaciones aprobadas");
                        break;
                    case DModSysConsts.SX_SRV_ST_REP_REJ:
                        view = new DViewReparation(miClient, subtype, "Reparaciones rechazadas");
                        break;
                    case DModSysConsts.SX_SRV_ST_REP_FIN:
                        view = new DViewReparation(miClient, subtype, "Reparaciones terminadas");
                        break;
                    case DModSysConsts.SX_SRV_ST_REP_DVY:
                        switch (params.getType()) {
                            case DModSysConsts.SX_SRV_DVY_BY_REP:
                                view = new DViewReparation(miClient, subtype, params.getType(), "Reparaciones entregadas");
                                break;
                            case DModSysConsts.SX_SRV_DVY_BY_DVY:
                                view = new DViewReparation(miClient, subtype, params.getType(), "Reparaciones entregadas x fecha entrega");
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
            case DModConsts.SU_EQU:
                if (moFormEquipment == null) moFormEquipment = new DFormEquipment(miClient, "Equipo");
                form = moFormEquipment;
                break;
            case DModConsts.S_REP:
                if (moFormReparation == null) moFormReparation = new DFormReparation(miClient, "Reparación");
                form = moFormReparation;
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
            else if (menuItem == mjCatEquipment) {
                showView(DModConsts.SU_EQU, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatBrand) {
                miClient.getSession().showView(DModConsts.IU_BRD, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjRepNew) {
                showView(DModConsts.S_REP, DModSysConsts.SX_SRV_ST_REP_NEW, null);
            }
            else if (menuItem == mjRepApr) {
                showView(DModConsts.S_REP, DModSysConsts.SX_SRV_ST_REP_APR, null);
            }
            else if (menuItem == mjRepRej) {
                showView(DModConsts.S_REP, DModSysConsts.SX_SRV_ST_REP_REJ, null);
            }
            else if (menuItem == mjRepFin) {
                showView(DModConsts.S_REP, DModSysConsts.SX_SRV_ST_REP_FIN, null);
            }
            else if (menuItem == mjRepDvyRep) {
                showView(DModConsts.S_REP, DModSysConsts.SX_SRV_ST_REP_DVY, new DGuiParams(DModSysConsts.SX_SRV_DVY_BY_REP));
            }
            else if (menuItem == mjRepDvyDvy) {
                showView(DModConsts.S_REP, DModSysConsts.SX_SRV_ST_REP_DVY, new DGuiParams(DModSysConsts.SX_SRV_DVY_BY_DVY));
            }
            else if (menuItem == mjRepAll) {
                showView(DModConsts.S_REP, DLibConsts.UNDEFINED, null);
            }
        }
    }
}
