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
import sba.mod.lad.db.DDbBol;
import sba.mod.lad.db.DDbBolLocation;
import sba.mod.lad.db.DDbBolMerchandise;
import sba.mod.lad.db.DDbBolMerchandiseMove;
import sba.mod.lad.db.DDbBolTransportFigure;
import sba.mod.lad.db.DDbBolTransportFigureTransportPart;
import sba.mod.lad.db.DDbBolTruck;
import sba.mod.lad.db.DDbBolTruckTrailer;
import sba.mod.lad.db.DDbLocation;
import sba.mod.lad.db.DDbLocationDistance;
import sba.mod.lad.db.DDbSysTransportPartType;
import sba.mod.lad.db.DDbSysTransportType;
import sba.mod.lad.db.DDbTrailer;
import sba.mod.lad.db.DDbTransportFigure;
import sba.mod.lad.db.DDbTruck;
import sba.mod.lad.db.DDbTruckTrailer;
import sba.mod.lad.db.DDbTruckTransportFigure;
import sba.mod.lad.db.DDbTruckTransportFigureTransportPart;
import sba.mod.lad.form.DFormBol;
import sba.mod.lad.form.DFormLocation;
import sba.mod.lad.form.DFormLocationDistance;
import sba.mod.lad.form.DFormTrailer;
import sba.mod.lad.form.DFormTransportFigure;
import sba.mod.lad.form.DFormTruck;
import sba.mod.lad.view.DViewBol;
import sba.mod.lad.view.DViewBolMerchandise;
import sba.mod.lad.view.DViewLocation;
import sba.mod.lad.view.DViewLocationDistance;
import sba.mod.lad.view.DViewTrailer;
import sba.mod.lad.view.DViewTransportFigure;
import sba.mod.lad.view.DViewTruck;

/**
 *
 * @author Sergio Flores
 */
public class DModModuleLad extends DGuiModule implements ActionListener {

    private JMenu mjCat;
    private JMenuItem mjCatLocation;
    private JMenuItem mjCatLocationDistance;
    private JMenuItem mjCatTransportFigure;
    private JMenuItem mjCatTrailer;
    private JMenuItem mjCatTruck;
    
    private JMenu mjLad;
    private JMenuItem mjLadBolReal;
    private JMenuItem mjLadBolTemplate;

    private JMenu mjRep;
    private JMenuItem mjRepBolMerchandise;

    private DFormLocation moFormLocation;
    private DFormLocationDistance moFormLocationDistance;
    private DFormTransportFigure moformTptFigure;
    private DFormTrailer moformTrailer;
    private DFormTruck moformTruck;
    private DFormBol moFormBolReal;
    private DFormBol moFormBolTemplate;

    public DModModuleLad(DGuiClient client) {
        super(client, DModConsts.MOD_LAD, DLibConsts.UNDEFINED);
        initComponents();
    }

    /*
     * Private methods
     */

    private void initComponents() {
        mjCat = new JMenu("Catálogos");
        mjCatLocation = new JMenuItem("Ubicaciones");
        mjCatLocationDistance = new JMenuItem("Distancias entre ubicaciones");
        mjCatTransportFigure = new JMenuItem("Figuras del transporte");
        mjCatTruck = new JMenuItem("Autotransportes");
        mjCatTrailer = new JMenuItem("Remolques");

        mjCat.add(mjCatLocation);
        mjCat.add(mjCatLocationDistance);
        mjCat.addSeparator();
        mjCat.add(mjCatTransportFigure);
        mjCat.addSeparator();
        mjCat.add(mjCatTruck);
        mjCat.add(mjCatTrailer);

        mjCatLocation.addActionListener(this);
        mjCatLocationDistance.addActionListener(this);
        mjCatTransportFigure.addActionListener(this);
        mjCatTruck.addActionListener(this);
        mjCatTrailer.addActionListener(this);

        mjLad = new JMenu("Traslados");
        mjLadBolReal = new JMenuItem("Cartas porte");
        mjLadBolTemplate = new JMenuItem("Plantillas de cartas porte");

        mjLad.add(mjLadBolReal);
        mjLad.addSeparator();
        mjLad.add(mjLadBolTemplate);

        mjLadBolReal.addActionListener(this);
        mjLadBolTemplate.addActionListener(this);

        mjCat.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { DModSysConsts.CS_PRV_LAD_ADM, DModSysConsts.CS_PRV_LAD_CAT }));
        mjLad.setEnabled(miClient.getSession().getUser().hasPrivilege(new int[] { DModSysConsts.CS_PRV_LAD_ADM, DModSysConsts.CS_PRV_LAD_LAD }));

        mjRep = new JMenu("Reportes");
        mjRepBolMerchandise = new JMenuItem("Mercancías transportadas");

        mjRep.add(mjRepBolMerchandise);

        mjRepBolMerchandise.addActionListener(this);
    }

    /*
     * Public methods
     */

    /*
     * Overriden methods
     */

    @Override
    public JMenu[] getMenus() {
        return new JMenu[] { mjCat, mjLad, mjRep };
    }

    @Override
    public DDbRegistry getRegistry(final int type, final DGuiParams params) {
        DDbRegistry registry = null;

        switch (type) {
            case DModConsts.LS_TPT_TP:
                registry = new DDbSysTransportType();
                break;
            case DModConsts.LS_LOC_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_loc_tp = " + pk[0] + " "; }
                };
                break;
            case DModConsts.LS_TPT_FIGURE_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_tpt_figure_tp = " + pk[0] + " "; }
                };
                break;
            case DModConsts.LS_TPT_PART_TP:
                registry = new DDbSysTransportPartType();
                break;
            case DModConsts.LS_XCC_COFEPRIS_SECT:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_xcc_cofepris_sect = " + pk[0] + " "; }
                };
                break;
            case DModConsts.LS_XCC_PHARM_FORM:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_xcc_pharm_form = " + pk[0] + " "; }
                };
                break;
            case DModConsts.LS_XCC_SPEC_COND:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_xcc_spec_cond = " + pk[0] + " "; }
                };
                break;
            case DModConsts.LU_LOC:
                registry = new DDbLocation();
                break;
            case DModConsts.LU_LOC_DIST:
                registry = new DDbLocationDistance();
                break;
            case DModConsts.LU_TPT_FIGURE:
                registry = new DDbTransportFigure();
                break;
            case DModConsts.LU_TRAIL:
                registry = new DDbTrailer();
                break;
            case DModConsts.LU_TRUCK:
                registry = new DDbTruck();
                break;
            case DModConsts.LU_TRUCK_TRAIL:
                registry = new DDbTruckTrailer();
                break;
            case DModConsts.LU_TRUCK_TPT_FIGURE:
                registry = new DDbTruckTransportFigure();
                break;
            case DModConsts.LU_TRUCK_TPT_FIGURE_TPT_PART:
                registry = new DDbTruckTransportFigureTransportPart();
                break;
            case DModConsts.L_BOL:
                registry = new DDbBol();
                break;
            case DModConsts.L_BOL_LOC:
                registry = new DDbBolLocation();
                break;
            case DModConsts.L_BOL_MERCH:
                registry = new DDbBolMerchandise();
                break;
            case DModConsts.L_BOL_MERCH_MOVE:
                registry = new DDbBolMerchandiseMove();
                break;
            case DModConsts.L_BOL_TRUCK:
                registry = new DDbBolTruck();
                break;
            case DModConsts.L_BOL_TRUCK_TRAIL:
                registry = new DDbBolTruckTrailer();
                break;
            case DModConsts.L_BOL_TPT_FIGURE:
                registry = new DDbBolTransportFigure();
                break;
            case DModConsts.L_BOL_TPT_FIGURE_TPT_PART:
                registry = new DDbBolTransportFigureTransportPart();
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
            case DModConsts.LS_TPT_TP:
                settings = new DGuiCatalogueSettings("Tipo transporte", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_tpt_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + ", code AS " + DDbConsts.FIELD_CODE + " "
                        + "FROM " + DModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del " + (subtype == DModConsts.L_BOL ? "AND id_tpt_tp <> " + DModSysConsts.LS_TPT_TP_NA + " " : "") + "ORDER BY sort ";
                break;
            case DModConsts.LS_LOC_TP:
                settings = new DGuiCatalogueSettings("Tipo ubicación", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_loc_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + ", code AS " + DDbConsts.FIELD_CODE + " "
                        + "FROM " + DModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del " + (subtype == DModConsts.L_BOL ? "AND id_loc_tp <> " + DModSysConsts.LS_LOC_TP_NA + " " : "") + "ORDER BY sort ";
                break;
            case DModConsts.LS_TPT_FIGURE_TP:
                settings = new DGuiCatalogueSettings("Tipo figura transporte", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_tpt_figure_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + ", code AS " + DDbConsts.FIELD_CODE + " "
                        + "FROM " + DModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del ORDER BY sort ";
                break;
            case DModConsts.LS_TPT_PART_TP:
                settings = new DGuiCatalogueSettings("Tipo parte transporte", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_tpt_part_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + ", code AS " + DDbConsts.FIELD_CODE + " "
                        + "FROM " + DModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del ORDER BY sort ";
                break;
            case DModConsts.LS_XCC_COFEPRIS_SECT:
                settings = new DGuiCatalogueSettings("Sector COFEPRIS", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_xcc_cofepris_sect AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + ", code AS " + DDbConsts.FIELD_CODE + " "
                        + "FROM " + DModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del ORDER BY sort ";
                break;
            case DModConsts.LS_XCC_PHARM_FORM:
                settings = new DGuiCatalogueSettings("Forma farmacéutica", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_xcc_pharm_form AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + ", code AS " + DDbConsts.FIELD_CODE + " "
                        + "FROM " + DModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del ORDER BY sort ";
                break;
            case DModConsts.LS_XCC_SPEC_COND:
                settings = new DGuiCatalogueSettings("Condiciones especiales transporte", 1);
                settings.setCodeApplying(true);
                sql = "SELECT id_xcc_spec_cond AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + ", code AS " + DDbConsts.FIELD_CODE + " "
                        + "FROM " + DModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del ORDER BY sort ";
                break;
            case DModConsts.LU_LOC:
                settings = new DGuiCatalogueSettings("Ubicación", 1);
                sql = "SELECT id_loc AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " "
                        + "FROM " + DModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del ORDER BY name, id_loc ";
                break;
            case DModConsts.LU_LOC_DIST:
                break;
            case DModConsts.LU_TPT_FIGURE:
                settings = new DGuiCatalogueSettings("Figura transporte", 1);
                sql = "SELECT id_tpt_figure AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " "
                        + "FROM " + DModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del ORDER BY name, id_tpt_figure ";
                break;
            case DModConsts.LU_TRAIL:
                settings = new DGuiCatalogueSettings("Remolque", 1);
                sql = "SELECT id_trail AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " "
                        + "FROM " + DModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del ORDER BY name, id_trail ";
                break;
            case DModConsts.LU_TRUCK:
                settings = new DGuiCatalogueSettings("Transporte", 1);
                sql = "SELECT id_truck AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " "
                        + "FROM " + DModConsts.TablesMap.get(type) + " "
                        + "WHERE NOT b_del ORDER BY name, id_truck ";
                break;
            case DModConsts.LU_TRUCK_TRAIL:
                break;
            case DModConsts.LU_TRUCK_TPT_FIGURE:
                break;
            case DModConsts.LU_TRUCK_TPT_FIGURE_TPT_PART:
                break;
            case DModConsts.L_BOL:
                break;
            case DModConsts.L_BOL_LOC:
                break;
            case DModConsts.L_BOL_MERCH:
                break;
            case DModConsts.L_BOL_MERCH_MOVE:
                break;
            case DModConsts.L_BOL_TRUCK:
                break;
            case DModConsts.L_BOL_TRUCK_TRAIL:
                break;
            case DModConsts.L_BOL_TPT_FIGURE:
                break;
            case DModConsts.L_BOL_TPT_FIGURE_TPT_PART:
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
            case DModConsts.LS_TPT_TP:
                break;
            case DModConsts.LS_LOC_TP:
                break;
            case DModConsts.LS_TPT_FIGURE_TP:
                break;
            case DModConsts.LS_XCC_COFEPRIS_SECT:
                break;
            case DModConsts.LS_XCC_PHARM_FORM:
                break;
            case DModConsts.LS_XCC_SPEC_COND:
                break;
            case DModConsts.LS_TPT_PART_TP:
                break;
            case DModConsts.LU_LOC:
                view = new DViewLocation(miClient, "Ubicaciones");
                break;
            case DModConsts.LU_LOC_DIST:
                view = new DViewLocationDistance(miClient, "Distancias ubicaciones");
                break;
            case DModConsts.LU_TPT_FIGURE:
                view = new DViewTransportFigure(miClient, "Figuras transporte");
                break;
            case DModConsts.LU_TRAIL:
                view = new DViewTrailer(miClient, "Remolques");
                break;
            case DModConsts.LU_TRUCK:
                view = new DViewTruck(miClient, "Transportes");
                break;
            case DModConsts.LU_TRUCK_TRAIL:
                break;
            case DModConsts.LU_TRUCK_TPT_FIGURE:
                break;
            case DModConsts.LU_TRUCK_TPT_FIGURE_TPT_PART:
                break;
            case DModConsts.L_BOL:
                view = new DViewBol(miClient, subtype, subtype == DDbBol.BOL_TEMPLATE ? "Plantillas cartas porte" : "Cartas porte");
                break;
            case DModConsts.L_BOL_LOC:
                break;
            case DModConsts.L_BOL_MERCH:
                view = new DViewBolMerchandise(miClient, "Mercancías transportadas");
                break;
            case DModConsts.L_BOL_MERCH_MOVE:
                break;
            case DModConsts.L_BOL_TRUCK:
                break;
            case DModConsts.L_BOL_TRUCK_TRAIL:
                break;
            case DModConsts.L_BOL_TPT_FIGURE:
                break;
            case DModConsts.L_BOL_TPT_FIGURE_TPT_PART:
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
            case DModConsts.LS_TPT_TP:
                break;
            case DModConsts.LS_LOC_TP:
                break;
            case DModConsts.LS_TPT_FIGURE_TP:
                break;
            case DModConsts.LS_TPT_PART_TP:
                break;
            case DModConsts.LS_XCC_COFEPRIS_SECT:
                break;
            case DModConsts.LS_XCC_PHARM_FORM:
                break;
            case DModConsts.LS_XCC_SPEC_COND:
                break;
            case DModConsts.LU_LOC:
                if (moFormLocation == null) moFormLocation = new DFormLocation(miClient, "Ubicación");
                form = moFormLocation;
                break;
            case DModConsts.LU_LOC_DIST:
                if (moFormLocationDistance == null) moFormLocationDistance = new DFormLocationDistance(miClient, "Distancia entre ubicaciones");
                form = moFormLocationDistance;
                break;
            case DModConsts.LU_TPT_FIGURE:
                if (moformTptFigure == null) moformTptFigure = new DFormTransportFigure(miClient, "Figura del transporte");
                form = moformTptFigure;
                break;
            case DModConsts.LU_TRAIL:
                if (moformTrailer == null) moformTrailer = new DFormTrailer(miClient, "Remolque");
                form = moformTrailer;
                break;
            case DModConsts.LU_TRUCK:
                if (moformTruck == null) moformTruck = new DFormTruck(miClient, "Autotransporte");
                form = moformTruck;
                break;
            case DModConsts.LU_TRUCK_TRAIL:
                break;
            case DModConsts.LU_TRUCK_TPT_FIGURE:
                break;
            case DModConsts.LU_TRUCK_TPT_FIGURE_TPT_PART:
                break;
            case DModConsts.L_BOL:
                if (subtype == DDbBol.BOL_TEMPLATE) {
                    if (moFormBolTemplate == null) moFormBolTemplate = new DFormBol(miClient, subtype, "Plantilla carta porte");
                    form = moFormBolTemplate;
                }
                else {
                    if (moFormBolReal == null) moFormBolReal = new DFormBol(miClient, subtype, "Carta porte");
                    if (params != null && params.getSubtype() == DDbBol.BOL_TEMPLATE) moFormBolReal.setTemplateKey(params.getKey());
                    form = moFormBolReal;
                }
                break;
            case DModConsts.L_BOL_LOC:
                break;
            case DModConsts.L_BOL_MERCH:
                break;
            case DModConsts.L_BOL_MERCH_MOVE:
                break;
            case DModConsts.L_BOL_TRUCK:
                break;
            case DModConsts.L_BOL_TRUCK_TRAIL:
                break;
            case DModConsts.L_BOL_TPT_FIGURE:
                break;
            case DModConsts.L_BOL_TPT_FIGURE_TPT_PART:
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return form;
    }

    @Override
    public DGuiReport getReport(final int type, final int subtype, final DGuiParams params) {
        DGuiReport report = null;
        
        switch (type) {
            case DModConsts.L_BOL:
                report = new DGuiReport("reps/bol.jasper", "Carta porte");
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
        
        return report;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem) e.getSource();

            if (menuItem == mjCatLocation) {
                showView(DModConsts.LU_LOC, 0, null);
            }
            else if (menuItem == mjCatLocationDistance) {
                showView(DModConsts.LU_LOC_DIST, 0, null);
            }
            else if (menuItem == mjCatTransportFigure) {
                showView(DModConsts.LU_TPT_FIGURE, 0, null);
            }
            else if (menuItem == mjCatTruck) {
                showView(DModConsts.LU_TRUCK, 0, null);
            }
            else if (menuItem == mjCatTrailer) {
                showView(DModConsts.LU_TRAIL, 0, null);
            }
            else if (menuItem == mjLadBolReal) {
                showView(DModConsts.L_BOL, DDbBol.BOL_REAL, null);
            }
            else if (menuItem == mjLadBolTemplate) {
                showView(DModConsts.L_BOL, DDbBol.BOL_TEMPLATE, null);
            }
            else if (menuItem == mjRepBolMerchandise) {
                showView(DModConsts.L_BOL_MERCH, 0, null);
            }
        }
    }
}
