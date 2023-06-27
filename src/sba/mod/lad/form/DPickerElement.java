/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.lad.form;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JTable;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.grid.DGridColumnForm;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridPaneForm;
import sba.lib.grid.DGridRow;
import sba.lib.grid.DGridRowOptionPicker;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanFieldKey;
import sba.lib.gui.bean.DBeanFieldText;
import sba.lib.gui.bean.DBeanFormDialog;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DPickerElement extends DBeanFormDialog implements ActionListener, ItemListener, KeyListener {
    
    public static final int ELEMENT = 1;
    
    protected DGridPaneForm moGridOptions;
    protected ArrayList<DGridRow> maGridRows;

    /**
     * Creates new form DPickerElement.
     * @param client GUI client.
     * @param element Element. Constants defined in DModConsts. Optsions supported: LU_LOC, LU_TPT_FIGURE, LU_TRAIL, LU_TRUCK, LS_TPT_PART_TP, IU_ITM.
     */
    public DPickerElement(DGuiClient client, int element) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, element, 0, "");
        initComponents();
        initComponentsCustom();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpParams = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jlFilter = new javax.swing.JLabel();
        moKeyFilter = new sba.lib.gui.bean.DBeanFieldKey();
        jpParams2 = new javax.swing.JPanel();
        jlElement = new javax.swing.JLabel();
        moTextElementName = new sba.lib.gui.bean.DBeanFieldText();
        moTextElementCode = new sba.lib.gui.bean.DBeanFieldText();
        jbClear = new javax.swing.JButton();
        jpOptions = new javax.swing.JPanel();

        jpParams.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros de búsqueda:"));
        jpParams.setLayout(new java.awt.GridLayout(2, 0, 0, 5));

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlFilter.setText("[Filtro:*]");
        jlFilter.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel1.add(jlFilter);

        moKeyFilter.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel1.add(moKeyFilter);

        jpParams.add(jPanel1);

        jpParams2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlElement.setText("[Elemento:]");
        jlElement.setPreferredSize(new java.awt.Dimension(100, 23));
        jpParams2.add(jlElement);

        moTextElementName.setText("TEXT");
        moTextElementName.setPreferredSize(new java.awt.Dimension(250, 23));
        jpParams2.add(moTextElementName);

        moTextElementCode.setText("TEXT");
        moTextElementCode.setPreferredSize(new java.awt.Dimension(75, 23));
        jpParams2.add(moTextElementCode);

        jbClear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_delete.gif"))); // NOI18N
        jbClear.setToolTipText("Limpiar");
        jbClear.setPreferredSize(new java.awt.Dimension(23, 23));
        jpParams2.add(jbClear);

        jpParams.add(jpParams2);

        getContentPane().add(jpParams, java.awt.BorderLayout.NORTH);

        jpOptions.setBorder(javax.swing.BorderFactory.createTitledBorder("Opciones:"));
        jpOptions.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jpOptions, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbClear;
    private javax.swing.JLabel jlElement;
    private javax.swing.JLabel jlFilter;
    private javax.swing.JPanel jpOptions;
    private javax.swing.JPanel jpParams;
    private javax.swing.JPanel jpParams2;
    private sba.lib.gui.bean.DBeanFieldKey moKeyFilter;
    private sba.lib.gui.bean.DBeanFieldText moTextElementCode;
    private sba.lib.gui.bean.DBeanFieldText moTextElementName;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        DGuiUtils.setWindowBounds(this, 640, 400);
        
        msTitle = composeTitle();
        setTitle(msTitle);
        
        moKeyFilter.setKeySettings(miClient, DGuiUtils.getLabelName(jlFilter), true);
        moTextElementName.setTextSettings(DGuiUtils.getLabelName(jlElement), 100);
        moTextElementName.setTextCaseType(0);
        moTextElementCode.setTextSettings(DGuiUtils.getLabelName(jlElement), DBolUtils.DEF_CODE_ADDRESS_STATE.length());
        moTextElementCode.setTextCaseType(0);
        
        moFields.addField(moKeyFilter);
        moFields.addField(moTextElementName);
        moFields.addField(moTextElementCode);
        moFields.setFormButton(jbSave);
        
        jbSave.setText(DGuiConsts.TXT_BTN_OK);
        
        if (mnFormType == DModConsts.LU_LOC || mnFormType == DModConsts.LU_TPT_FIGURE) {
            jlFilter.setText("Tipo:*");
            jlFilter.setEnabled(true);
            moKeyFilter.setEnabled(true);
        }
        else {
            jlFilter.setText("Filtro:*");
            jlFilter.setEnabled(false);
            moKeyFilter.setEnabled(false);
        }
        
        switch (mnFormType) {
            case DModConsts.LU_LOC:
                jlElement.setText("Ubicación:");
                break;
            case DModConsts.LU_TPT_FIGURE:
                jlElement.setText("Figura transporte:");
                break;
            case DModConsts.LU_TRAIL:
                jlElement.setText("Remolque:");
                break;
            case DModConsts.LU_TRUCK:
                jlElement.setText("Autotransporte:");
                break;
            case DModConsts.LS_TPT_PART_TP:
                jlElement.setText("Parte transporte:");
                break;
            case DModConsts.IU_ITM:
                jlElement.setText("Ítem:");
                break;
            default:
                jlElement.setText(DUtilConsts.TXT_UNKNOWN + ":");
        }
        
        moGridOptions = new DGridPaneForm(miClient, mnFormType, mnFormSubtype, msTitle) {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }
            
            @Override
            public void createGridColumns() {
                int col = 0;
                DGridColumnForm[] columns = null;
                
                switch (mnFormType) {
                    case DModConsts.LU_LOC:
                        columns = new DGridColumnForm[5];
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Nombre");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "Código");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "CP");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "Domicilio");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "País");
                        break;
                        
                    case DModConsts.LU_TPT_FIGURE:
                        columns = new DGridColumnForm[7];
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Nombre");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "Código");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "RFC");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "Licencia");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "CP");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_CAT_L, "Domicilio");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "País");
                        break;
                        
                    case DModConsts.LU_TRAIL:
                        columns = new DGridColumnForm[5];
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Nombre");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "Código");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "Placa");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "Subtipo clave");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Subtipo descripción");
                        break;
                        
                    case DModConsts.LU_TRUCK:
                        columns = new DGridColumnForm[5];
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Nombre");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "Código");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "Placa");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "Config. vehicular clave");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Config. vehicular descripción");
                        break;
                        
                    case DModConsts.LS_TPT_PART_TP:
                        columns = new DGridColumnForm[2];
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Nombre");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "Código");
                        break;
                        
                    case DModConsts.IU_ITM:
                        columns = new DGridColumnForm[3];
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_ITM_L, "Nombre");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "Código");
                        columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Unidad");
                        break;
                        
                    default:
                        // nothing
                }

                for (DGridColumnForm column : columns) {
                    moModel.getGridColumns().add(column);
                }
            }
        };
        
        jpOptions.add(moGridOptions, BorderLayout.CENTER);
    }
    
    private String composeTitle() {
        String formTitle = DUtilConsts.TXT_FIND;
        
        switch (mnFormType) {
            case DModConsts.LU_LOC:
                formTitle += " ubicación";
                break;
            case DModConsts.LU_TPT_FIGURE:
                formTitle += " figura de transporte";
                break;
            case DModConsts.LU_TRAIL:
                formTitle += " remolque";
                break;
            case DModConsts.LU_TRUCK:
                formTitle += " autotransporte";
                break;
            case DModConsts.LS_TPT_PART_TP:
                formTitle += " parte de transporte";
                break;
            case DModConsts.IU_ITM:
                formTitle += " ítem";
                break;
            default:
                formTitle += " " + DUtilConsts.TXT_UNKNOWN.toLowerCase();
        }
        
        return formTitle;
    }
    
    private void populateGridOptions(final Vector<DGridRow> rows) {
        Vector<DGridRow> fittingRows = new Vector<>();
        
        if (moKeyFilter.isEnabled()) {
            // show only rows that match filter:
            if (moKeyFilter.getSelectedIndex() > 0) {
                for (DGridRow row : rows) {
                    if ((Integer) ((DGridRowOptionPicker) row).getMainOption() == moKeyFilter.getValue()[0]) {
                        fittingRows.add(row);
                    }
                }
            }
        }
        else {
            // show all rows:
            fittingRows.addAll(rows);
        }
        
        moGridOptions.populateGrid(fittingRows);
        moGridOptions.getTable().addKeyListener(this);
        moGridOptions.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    actionSave();
                }
            }
        });
    }
    
    private void actionPerformedClear() {
        moTextElementName.resetField();
        moTextElementCode.resetField();
        
        moTextElementName.requestFocus();
    }

    private void keyPressedElementCode(final KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            event.consume();
            moGridOptions.getTable().requestFocusInWindow();
        }
    }

    private void keyPressedTable(final KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_ENTER && moGridOptions.getSelectedGridRow() != null) {
            event.consume();
            actionSave();
        }
    }

    private void keyReleasedElementNameCode(final KeyEvent e) {
        String name = moTextElementName.getValue().toUpperCase();
        String code = moTextElementCode.getValue().toUpperCase();
        Vector<DGridRow> rows = new Vector<>();
        
        if (name.isEmpty() && code.isEmpty()) {
            rows.addAll(maGridRows);
        }
        else {
            for (DGridRow row : maGridRows) {
                if ((!name.isEmpty() && row.getRowName().toUpperCase().contains(name)) || (!code.isEmpty() && row.getRowCode().toUpperCase().contains(code))) {
                    rows.add(row);
                }
            }
        }
        
        populateGridOptions(rows);
    }
    
    private String composeAddress(final String street, final String numberExt, final String numberInt, 
            final String district, final String locality, final String county, final String state) {
        String address = "";
        
        if (!street.isEmpty()) {
            address += street;
            
            if (!numberExt.isEmpty()) {
                address += " " + numberExt;
                
                if (!numberInt.isEmpty()) {
                    address += " " + numberInt;
                }
            }
        }
        
        for (String part : new String[] { district, locality, county, state }) {
            if (!part.isEmpty()) {
                address += (address.isEmpty() ? "" : ", ") + part;
            }
        }
        
        return address;
    }
    
    private void retreiveGridOptions() throws Exception {
        String sql = "";

        switch (mnFormType) {
            case DModConsts.LU_LOC:
                sql = "SELECT l.id_loc AS _id, l.code, l.name, '' AS fis_id, '' AS drv_lic, "
                        + "l.add_zip, l.add_str, l.add_num_ext, l.add_num_int, "
                        + "l.add_dist_name, l.add_loc_name, l.add_cou_name, l.add_ste_name, "
                        + "l.fk_loc_tp AS _filter_id, ac.name AS _cty_name "
                        + "FROM " + DModConsts.TablesMap.get(DModConsts.LU_LOC) + " AS l "
                        + "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_CTY) + " AS ac ON ac.id_cty = l.fk_add_cty "
                        + "WHERE NOT l.b_del "
                        + "ORDER BY l.name, l.code, l.id_loc;";
                break;
                
            case DModConsts.LU_TPT_FIGURE:
                sql = "SELECT tf.id_tpt_figure AS _id, tf.code, tf.name, tf.fis_id, tf.drv_lic, "
                        + "tf.add_zip, tf.add_str, tf.add_num_ext, tf.add_num_int, "
                        + "tf.add_dist_name, tf.add_loc_name, tf.add_cou_name, tf.add_ste_name, "
                        + "tf.fk_tpt_figure_tp AS _filter_id, ac.name AS _cty_name "
                        + "FROM " + DModConsts.TablesMap.get(DModConsts.LU_TPT_FIGURE) + " AS tf "
                        + "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_CTY) + " AS ac ON ac.id_cty = tf.fk_add_cty "
                        + "WHERE NOT tf.b_del "
                        + "ORDER BY tf.name, tf.code, tf.id_tpt_figure;";
                break;
                
            case DModConsts.LU_TRAIL:
                sql = "SELECT id_trail AS _id, code, name, "
                        + "plate, trail_stp_code AS _type_code, trail_stp_name AS _type_name "
                        + "FROM " + DModConsts.TablesMap.get(DModConsts.LU_TRAIL) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY name, code, id_trail;";
                break;
                
            case DModConsts.LU_TRUCK:
                sql = "SELECT id_truck AS _id, code, name, "
                        + "plate, tpt_config_code AS _type_code, tpt_config_name AS _type_name "
                        + "FROM " + DModConsts.TablesMap.get(DModConsts.LU_TRUCK) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY name, code, id_truck;";
                break;
                
            case DModConsts.LS_TPT_PART_TP:
                sql = "SELECT id_tpt_part_tp AS _id, code, name "
                        + "FROM " + DModConsts.TablesMap.get(DModConsts.LS_TPT_PART_TP) + " "
                        + "WHERE NOT b_del "
                        + "ORDER BY name, code, id_tpt_part_tp;";
                break;
                
            case DModConsts.IU_ITM:
                sql = "SELECT i.id_itm AS _id, i.code, i.name, u.name AS _unit_name "
                        + "FROM " + DModConsts.TablesMap.get(DModConsts.IU_ITM) + " AS i "
                        + "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.IU_UNT) + " AS u ON u.id_unt = i.fk_unt "
                        + "WHERE NOT i.b_del "
                        + "ORDER BY i.name, i.code, i.id_itm;";
                break;
                
            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        maGridRows = new ArrayList<>();

        try (Statement statement = miClient.getSession().getStatement().getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            
            switch (mnFormType) {
                case DModConsts.LU_LOC:
                case DModConsts.LU_TPT_FIGURE:
                    while (resultSet.next()) {
                        DGridRowOptionPicker row = new DGridRowOptionPicker(new int[] { resultSet.getInt("_id") });
                        
                        row.setRowName(resultSet.getString("name"));
                        row.setRowCode(resultSet.getString("code"));
                        
                        row.setMainOption(resultSet.getInt("_filter_id")); // filter
                        
                        row.getValues().add(resultSet.getString("name")); // 0
                        row.getValues().add(resultSet.getString("code")); // 1
                        
                        if (mnFormType == DModConsts.LU_TPT_FIGURE) {
                            row.getValues().add(resultSet.getString("fis_id")); // 2
                            row.getValues().add(resultSet.getString("drv_lic")); // 3
                        }
                        
                        row.getValues().add(resultSet.getString("add_zip")); // 2 or 4
                        row.getValues().add(composeAddress(resultSet.getString("add_str"), resultSet.getString("add_num_ext"), resultSet.getString("add_num_int"), 
                                resultSet.getString("add_dist_name"), resultSet.getString("add_loc_name"), resultSet.getString("add_cou_name"), resultSet.getString("add_ste_name"))); // 3 or 5
                        row.getValues().add(resultSet.getString("_cty_name")); // 4 or 6
                        
                        maGridRows.add(row);
                    }
                    break;
                    
                case DModConsts.LU_TRAIL:
                case DModConsts.LU_TRUCK:
                    while (resultSet.next()) {
                        DGridRowOptionPicker row = new DGridRowOptionPicker(new int[] { resultSet.getInt("_id") });
                        
                        row.setRowName(resultSet.getString("name"));
                        row.setRowCode(resultSet.getString("code"));
                        
                        row.getValues().add(resultSet.getString("name")); // 0
                        row.getValues().add(resultSet.getString("code")); // 1
                        row.getValues().add(resultSet.getString("plate")); // 2
                        row.getValues().add(resultSet.getString("_type_code")); // 3
                        row.getValues().add(resultSet.getString("_type_name")); // 4
                        
                        maGridRows.add(row);
                    }
                    break;
                    
                case DModConsts.LS_TPT_PART_TP:
                case DModConsts.IU_ITM:
                    while (resultSet.next()) {
                        DGridRowOptionPicker row = new DGridRowOptionPicker(new int[] { resultSet.getInt("_id") });
                        
                        row.setRowName(resultSet.getString("name"));
                        row.setRowCode(resultSet.getString("code"));
                        
                        row.getValues().add(resultSet.getString("name")); // 0
                        row.getValues().add(resultSet.getString("code")); // 1
                        
                        if (mnFormType == DModConsts.IU_ITM) {
                            row.getValues().add(resultSet.getString("_unit_name")); // 2
                        }
                        
                        maGridRows.add(row);
                    }
                    break;
                    
                default:
                    // nothing
            }
        }
    }
    
    private void itemStateChangedFilter() {
        moTextElementName.resetField();
        moTextElementCode.resetField();
        
        if (moKeyFilter.getSelectedIndex() <= 0) {
            moTextElementName.setEnabled(false);
            moTextElementCode.setEnabled(false);
            
            moGridOptions.clearGridRows();
        }
        else {
            moTextElementName.setEnabled(true);
            moTextElementCode.setEnabled(true);
            
            keyReleasedElementNameCode(null); // triggers population of gridpane
        }
    }
    
    @Override
    public void resetForm() {
        mnFormResult = 0;
        mbFirstActivation = true;
        
        removeAllListeners();
        
        moFields.resetFields();
        
        try {
            reloadCatalogues();
            
            if (moKeyFilter.isEnabled()) {
                itemStateChangedFilter();
            }
            
            retreiveGridOptions();
            populateGridOptions(new Vector<>(maGridRows));
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
        
        addAllListeners();
    }

    @Override
    public void addAllListeners() {
        jbClear.addActionListener(this);
        moTextElementName.addKeyListener(this);
        moTextElementCode.addKeyListener(this);
        moKeyFilter.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbClear.removeActionListener(this);
        moTextElementName.removeKeyListener(this);
        moTextElementCode.removeKeyListener(this);
        moKeyFilter.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        switch (mnFormType) {
            case DModConsts.LU_LOC:
                miClient.getSession().populateCatalogue(moKeyFilter, DModConsts.LS_LOC_TP, 0, null);
                break;
            case DModConsts.LU_TPT_FIGURE:
                miClient.getSession().populateCatalogue(moKeyFilter, DModConsts.LS_TPT_FIGURE_TP, 0, null);
                break;
            default:
                // nothing
        }
    }

    @Override
    public void setRegistry(DDbRegistry registry) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DDbRegistry getRegistry() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DGuiValidation validateForm() {
        DGuiValidation validation = new DGuiValidation();
        
        if (moGridOptions.getSelectedGridRow() == null) {
            validation.setMessage(DGridConsts.MSG_SELECT_ROW);
            validation.setComponent(moGridOptions.getTable());
        }
        
        return validation;
    }

    @Override
    public void setValue(final int type, final Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getValue(final int type) {
        Object value = null;
        
        switch (type) {
            case ELEMENT:
                value = moGridOptions.getSelectedGridRow().getRowPrimaryKey();
                break;
            default:
                // nothing
        }
        
        return value;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbClear) {
                actionPerformedClear();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof DBeanFieldKey && e.getStateChange() == ItemEvent.SELECTED) {
            DBeanFieldKey field = (DBeanFieldKey) e.getSource();
            
            if (field == moKeyFilter) {
                itemStateChangedFilter();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() instanceof DBeanFieldText) {
            DBeanFieldText field = (DBeanFieldText) e.getSource();
            
            if (field == moTextElementCode) {
                keyPressedElementCode(e);
            }
        }
        else if (e.getSource() instanceof JTable) {
            JTable table = (JTable) e.getSource();
            
            if (table == moGridOptions.getTable()) {
                keyPressedTable(e);
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() instanceof DBeanFieldText) {
            DBeanFieldText field = (DBeanFieldText) e.getSource();
            
            if (field == moTextElementName || field == moTextElementCode) {
                keyReleasedElementNameCode(e);
            }
        }
    }
}
