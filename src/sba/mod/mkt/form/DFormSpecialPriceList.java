/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DFormSpecialPriceList.java
 *
 * Created on 29/08/2011, 08:02:13 PM
 */

package sba.mod.mkt.form;

import java.awt.BorderLayout;
import java.util.Vector;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.grid.DGridColumnForm;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridPaneForm;
import sba.lib.grid.DGridRow;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiFormOwner;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanForm;
import sba.lib.gui.bean.DBeanFormDialog;
import sba.mod.DModConsts;
import sba.mod.mkt.db.DDbSpecialPriceList;
import sba.mod.mkt.db.DDbSpecialPriceListPrice;

/**
 *
 * @author Sergio Flores
 */
public class DFormSpecialPriceList extends DBeanForm implements DGuiFormOwner {

    private DDbSpecialPriceList moRegistry;
    private DGridPaneForm moGridPrices;

    /** Creates new form DFormRoute */
    public DFormSpecialPriceList(DGuiClient client, String title) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, DModConsts.M_SPE, DLibConsts.UNDEFINED, title);
        initComponents();
        initComponentsCustom();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlCode = new javax.swing.JLabel();
        moTextCode = new sba.lib.gui.bean.DBeanFieldText();
        jPanel1 = new javax.swing.JPanel();
        jlName = new javax.swing.JLabel();
        moTextName = new sba.lib.gui.bean.DBeanFieldText();
        jpPrices = new javax.swing.JPanel();

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jPanel2.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlCode.setText("Código:*");
        jlCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlCode);

        moTextCode.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel3.add(moTextCode);

        jPanel2.add(jPanel3);

        jPanel1.setLayout(new java.awt.FlowLayout(3, 5, 0));

        jlName.setText("Nombre:*");
        jlName.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel1.add(jlName);

        moTextName.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel1.add(moTextName);

        jPanel2.add(jPanel1);

        getContentPane().add(jPanel2, java.awt.BorderLayout.PAGE_START);

        jpPrices.setBorder(javax.swing.BorderFactory.createTitledBorder("Precios especiales:"));
        jpPrices.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jpPrices, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsCustom() {
        DBeanFormDialog form = null;

        DGuiUtils.setWindowBounds(this, 640, 400);

        moTextCode.setTextSettings(DGuiUtils.getLabelName(jlCode.getText()), 5);
        moTextName.setTextSettings(DGuiUtils.getLabelName(jlName.getText()), 50);

        moFields.addField(moTextCode);
        moFields.addField(moTextName);

        moFields.setFormButton(jbSave);

        moGridPrices = new DGridPaneForm(miClient, DModConsts.M_SPE_PRC, DLibConsts.UNDEFINED, "Precios especiales") {

            @Override
            public void initGrid() {

            }

            @Override
            public void createGridColumns() {
                int col = 0;
                DGridColumnForm[] columns = new DGridColumnForm[5];

                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_CODE_ITM, DGridConsts.COL_TITLE_CODE);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, DGridConsts.COL_TITLE_NAME);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "Tipo pago");
                columns[col] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT_UNIT, "Precio esp. $");
                columns[col++].setEditable(true);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_CODE_CUR, "Moneda");

                for (col = 0; col < columns.length; col++) {
                    moModel.getGridColumns().add(columns[col]);
                }
            }
        };

        form = new DDialogSpecialPriceListPrice(miClient, "Precio especial");
        form.setFormOwner(this);
        moGridPrices.setForm(form);
        moGridPrices.setPaneFormOwner(null);

        jpPrices.add(moGridPrices, BorderLayout.CENTER);

        mvFormGrids.add(moGridPrices);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel jlCode;
    private javax.swing.JLabel jlName;
    private javax.swing.JPanel jpPrices;
    private sba.lib.gui.bean.DBeanFieldText moTextCode;
    private sba.lib.gui.bean.DBeanFieldText moTextName;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods
     */

    /**
     * @param priceToEvaluate Special price to evaluate.
     * @param indexToExclude Index to exclude if necesary, -1 when it is not.
     */
    private boolean isRowUnique(final DDbSpecialPriceListPrice priceToEvaluate, final int indexToExclude) {
        boolean unique = true;
        DDbSpecialPriceListPrice price = null;

        for (int index = 0; index < moGridPrices.getModel().getRowCount(); index++) {
            if (index != indexToExclude) {
                price = (DDbSpecialPriceListPrice) moGridPrices.getGridRow(index);
                if (price.getPkPaymentTypeId() == priceToEvaluate.getPkPaymentTypeId() && price.getPkItemId() == priceToEvaluate.getPkItemId()) {
                    unique = false;
                    miClient.showMsgBoxWarning(DDbConsts.ERR_MSG_REG_ALLREADY_EXISTS);
                    break;
                }
            }
        }

        return unique;
    }

    /*
     * Public methods
     */

    /*
     * Overriden methods
     */

    @Override
    public void addAllListeners() {

    }

    @Override
    public void removeAllListeners() {

    }

    @Override
    public void reloadCatalogues() {

    }

    @Override
    public void setRegistry(DDbRegistry registry) throws Exception {
        Vector<DGridRow> prices = new Vector<>();

        moRegistry = (DDbSpecialPriceList) registry;

        mnFormResult = DLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(DLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        moTextCode.setValue(moRegistry.getCode());
        moTextName.setValue(moRegistry.getName());

        for (DDbSpecialPriceListPrice price :  moRegistry.getChildPrices()) {
            prices.add(price);
        }

        moGridPrices.populateGrid(prices);

        setFormEditable(true);

        addAllListeners();
    }

    @Override
    public DDbSpecialPriceList getRegistry() throws Exception {
        DDbSpecialPriceList registry = moRegistry.clone();

        if (registry.isRegistryNew()) { }

        registry.setCode(moTextCode.getValue());
        registry.setName(moTextName.getValue());

        registry.getChildPrices().clear();
        for (DGridRow row : moGridPrices.getModel().getGridRows()) {
            registry.getChildPrices().add((DDbSpecialPriceListPrice) row);
        }

        return registry;
    }

    @Override
    public DGuiValidation validateForm() {
        DGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            if (moGridPrices.getTable().getRowCount() == 0) {
                validation.setMessage(DUtilConsts.ERR_MSG_DOC_NO_ROWS);
                validation.setComponent(moTextName);
            }
        }

        return validation;
    }

    @Override
    public boolean validateRegistryNew(DDbRegistry registry) {
        return isRowUnique((DDbSpecialPriceListPrice) registry, -1);
    }

    @Override
    public boolean validateRegistryEdit(DDbRegistry registry) {
        return isRowUnique((DDbSpecialPriceListPrice) registry, moGridPrices.getTable().getSelectedRow());
    }
}
