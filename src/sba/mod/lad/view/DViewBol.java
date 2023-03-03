/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.lad.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import sba.gui.util.DUtilConsts;
import sba.lib.db.DDbConsts;
import sba.lib.grid.DGridColumnView;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridFilterDatePeriod;
import sba.lib.grid.DGridPaneSettings;
import sba.lib.grid.DGridPaneView;
import sba.lib.grid.DGridUtils;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiDate;
import sba.lib.gui.DGuiParams;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.lad.db.DDbBol;
import sba.mod.lad.form.DPickerTemplate;

/**
 *
 * @author Sergio Flores
 */
public class DViewBol extends DGridPaneView implements ActionListener {

    private DPickerTemplate moPickerTemplate;
    private JButton mjButtonCreateFromTemplate;
    private DGridFilterDatePeriod moFilterDatePeriod;

    /**
     * @param client GUI Client.
     * @param mode Supported options: DDbBol.MODE_REAL, DDbBol.MODE_TEMP.
     * @param title Title.
     */
    public DViewBol(DGuiClient client, int mode, String title) {
        super(client, DGridConsts.GRID_VIEW_TAB, DModConsts.L_BOL, mode, title, null);
        initComponentsCustom();
    }

    /*
     * Private methods
     */

    private void initComponentsCustom() {
        if (!isBolTemplate()) {
            mjButtonCreateFromTemplate = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/dps_inv_tmp.gif")), "Nuevo desde plantilla", this);
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonCreateFromTemplate);
            
            moFilterDatePeriod = new DGridFilterDatePeriod(miClient, this, DGuiConsts.DATE_PICKER_DATE_PERIOD);
            moFilterDatePeriod.initFilter(new DGuiDate(DGuiConsts.GUI_DATE_MONTH, miClient.getSession().getWorkingDate().getTime()));
            getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(moFilterDatePeriod);
        }
    }
    
    private boolean isBolTemplate() {
        return mnGridSubtype == DDbBol.BOL_TEMPLATE;
    }
    
    private void actionPerformedCreateFromTemplate() {
        if (mjButtonCreateFromTemplate != null && mjButtonCreateFromTemplate.isEnabled()) {
            if (moPickerTemplate == null) {
                moPickerTemplate = new DPickerTemplate(miClient);
            }
            
            moPickerTemplate.resetForm();
            moPickerTemplate.setVisible(true);

            if (moPickerTemplate.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                int[] key = (int[]) moPickerTemplate.getValue(DPickerTemplate.TEMPLATE);

                if (key != null) {
                    DGuiParams params = new DGuiParams(DModConsts.L_BOL, DDbBol.BOL_TEMPLATE); // subtype BOL_TEMPLATE triggers creating new BOL from a template
                    params.setKey(key);

                    miClient.getSession().getModule(DModConsts.MOD_LAD, 0).showForm(DModConsts.L_BOL, DDbBol.BOL_REAL, params);
                }
            }
            
        }
    }

    /*
     * Public methods
     */

    /*
     * Overriden methods
     */

    @Override
    public void prepareSqlQuery() {
        String sql = "";
        Object filter = null;

        moPaneSettings = new DGridPaneSettings(1);
        moPaneSettings.setDeletedApplying(true);
        moPaneSettings.setUserInsertApplying(true);
        moPaneSettings.setUserUpdateApplying(true);

        filter = (Boolean) moFiltersMap.get(DGridConsts.FILTER_DELETED);
        if ((Boolean) filter) {
            sql += (sql.isEmpty() ? "" : "AND ") + "b.b_del = 0 ";
        }

        if (!isBolTemplate()) {
            filter = (DGuiDate) moFiltersMap.get(DGridConsts.FILTER_DATE_PERIOD);
            if (filter != null) {
                sql += (sql.isEmpty() ? "" : "AND ") + DGridUtils.getSqlFilterDate("b.dt", (DGuiDate) filter);
            }
        }
        
        sql += (sql.isEmpty() ? "" : "AND ") + "b.b_temp = " + (mnGridSubtype == DDbBol.BOL_TEMPLATE ? 1 : 0) + " ";
        
        msSql = "SELECT " +
                "b.id_bol AS " + DDbConsts.FIELD_ID + "1, " +
                (isBolTemplate() ?
                "b.temp_code AS " + DDbConsts.FIELD_CODE + ", " +
                "b.temp_name AS " + DDbConsts.FIELD_NAME + ", " : 
                "CONCAT(IF(b.ser <> '', CONCAT(b.ser, '-'), ''), b.num) AS " + DDbConsts.FIELD_CODE + ", " +
                "CONCAT(IF(b.ser <> '', CONCAT(b.ser, '-'), ''), b.num) AS " + DDbConsts.FIELD_NAME + ", ") +
                "b.ser, " +
                "b.num, " +
                "IF(b.fk_bol_st = " + DModSysConsts.TS_XML_ST_ANN + ", " + DGridConsts.ICON_ANNUL + ", " + DGridConsts.ICON_NULL + ") AS _ico_bol_st, " +
                "IF(dfr.fk_xml_st = " + DModSysConsts.TS_XML_ST_PEN + ", " + DGridConsts.ICON_XML_PEND + ", " +
                "IF(dfr.fk_xml_st = " + DModSysConsts.TS_XML_ST_ISS + ", " + DGridConsts.ICON_XML_ISSU + ", " +
                "IF(dfr.fk_xml_st = " + DModSysConsts.TS_XML_ST_ANN + ", " + DGridConsts.ICON_XML_ANNUL + ", " + DGridConsts.ICON_NULL + "))) AS _ico_xml_st, " +
                "cb.code, " +
                "(SELECT l.name FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_LOC) + " AS bl " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.LU_LOC) + " AS l ON l.id_loc = bl.fk_loc " +
                "WHERE bl.id_bol = b.id_bol AND bl.fk_loc_tp = " + DModSysConsts.LS_LOC_TP_SRC + " ORDER BY bl.id_loc LIMIT 1) AS _src, " +
                "(SELECT l.name FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL_LOC) + " AS bl " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.LU_LOC) + " AS l ON l.id_loc = bl.fk_loc " +
                "WHERE bl.id_bol = b.id_bol AND bl.fk_loc_tp = " + DModSysConsts.LS_LOC_TP_DES + " ORDER BY bl.id_loc DESC LIMIT 1) AS _des, " +
                "b.dt AS " + DDbConsts.FIELD_DATE + ", " +
                "b.b_del AS " + DDbConsts.FIELD_IS_DEL + ", " +
                "b.fk_usr_ins AS " + DDbConsts.FIELD_USER_INS_ID + ", " +
                "b.fk_usr_upd AS " + DDbConsts.FIELD_USER_UPD_ID + ", " +
                "b.ts_usr_ins AS " + DDbConsts.FIELD_USER_INS_TS + ", " +
                "b.ts_usr_upd AS " + DDbConsts.FIELD_USER_UPD_TS + ", " +
                "ui.name AS " + DDbConsts.FIELD_USER_INS_NAME + ", " +
                "uu.name AS " + DDbConsts.FIELD_USER_UPD_NAME + " " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.L_BOL) + " AS b " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " AS cb ON " +
                "b.fk_own_bpr = cb.id_bpr AND b.fk_own_bra = cb.id_bra " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS ui ON " +
                "b.fk_usr_ins = ui.id_usr " +
                "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CU_USR) + " AS uu ON " +
                "b.fk_usr_upd = uu.id_usr " +
                "LEFT OUTER JOIN " + DModConsts.TablesMap.get(DModConsts.T_DFR) + " AS dfr ON " +
                "b.id_bol = dfr.fk_bol_n " +
                (sql.isEmpty() ? "" : "WHERE " + sql) +
                "ORDER BY b.ser, b.num, b.temp_name, b.temp_code, b.id_bol;";
    }

    @Override
    public void createGridColumns() {
        int col = 0;
        DGridColumnView[] columns = null;

        if (isBolTemplate()) {
            columns = new DGridColumnView[11];
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_L, DDbConsts.FIELD_NAME, "Nombre");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, DDbConsts.FIELD_CODE, "Código");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "b.ser", "Serie");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "cb.code", DUtilConsts.TXT_BRANCH + " empresa");
        }
        else {
            columns = new DGridColumnView[12];
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_REG_NUM, DDbConsts.FIELD_NAME, "Folio");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE, DDbConsts.FIELD_DATE, "Fecha");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_CODE_CO, "cb.code", DUtilConsts.TXT_BRANCH + " empresa");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_ICON, "_ico_bol_st", DGridConsts.COL_TITLE_STAT + " docto");
            columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_INT_ICON, "_ico_xml_st", "XML");
        }
        
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "_src", "Origen");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "_des", "Destino");
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_BOOL_S, DDbConsts.FIELD_IS_DEL, DGridConsts.COL_TITLE_IS_DEL);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_USR, DDbConsts.FIELD_USER_INS_NAME, DGridConsts.COL_TITLE_USER_INS_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE_DATETIME, DDbConsts.FIELD_USER_INS_TS, DGridConsts.COL_TITLE_USER_INS_TS);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_TEXT_NAME_USR, DDbConsts.FIELD_USER_UPD_NAME, DGridConsts.COL_TITLE_USER_UPD_NAME);
        columns[col++] = new DGridColumnView(DGridConsts.COL_TYPE_DATE_DATETIME, DDbConsts.FIELD_USER_UPD_TS, DGridConsts.COL_TITLE_USER_UPD_TS);

        for (col = 0; col < columns.length; col++) {
            moModel.getGridColumns().add(columns[col]);
        }
    }

    @Override
    public void defineSuscriptions() {
        moSuscriptionsSet.add(mnGridType);
        moSuscriptionsSet.add(DModConsts.CU_USR);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == mjButtonCreateFromTemplate) {
                actionPerformedCreateFromTemplate();
            }
        }
    }
}
