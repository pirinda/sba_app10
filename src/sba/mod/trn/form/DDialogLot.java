/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DDialogLot.java
 *
 * Created on 7/11/2011, 07:51:51 PM
 */

package sba.mod.trn.form;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Vector;
import javax.swing.JButton;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.grid.DGridColumnForm;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridPaneForm;
import sba.lib.grid.DGridPaneFormOwner;
import sba.lib.grid.DGridRow;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanFieldText;
import sba.lib.gui.bean.DBeanFormDialog;
import sba.lib.img.DImgConsts;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.itm.db.DDbItem;
import sba.mod.itm.db.DDbUnit;
import sba.mod.trn.db.DRowLot;
import sba.mod.trn.db.DTrnStockMove;
import sba.mod.trn.db.DTrnUtils;

/**
 *
 * @author Sergio Flores
 */
public class DDialogLot extends DBeanFormDialog implements DGridPaneFormOwner, ActionListener {

    private int mnIogCategory;

    private Date mtDate;
    private int mnYear;
    private double mdQuantity;
    private int[] manItemUnitKey;
    private int[] manWarehouseKey;
    private int[] manBookkeepingNumberKey;
    private double[] madPrices;
    private String msCurrencyCode;
    private DDbItem moItem;
    private DDbUnit moUnit;
    private Vector<DTrnStockMove> mvLotsToAdjust;
    private DDialogLotInStock moDialogLotInStock;
    private DGridPaneForm moGridLots;

    /** Creates new form DDialogLot
     * @param client GUI client.
     * @param type XType of DPS. Constants defined in DModConsts (TX_DPS_... or TX_IOG_...).
     * @param subtype Category of DPS. Constants defined in DModSysConsts (TS_DPS_CT_... or TS_IOG_CT_...).
     */
    public DDialogLot(DGuiClient client, int type, int subtype) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, type, subtype, "Lotes");
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

        jButtonGroup = new javax.swing.ButtonGroup();
        jpPanelItem = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jlCode = new javax.swing.JLabel();
        jlName = new javax.swing.JLabel();
        jlQuantity = new javax.swing.JLabel();
        jlUnitCode = new javax.swing.JLabel();
        jlInput = new javax.swing.JLabel();
        jlRemaining = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jtfCode = new javax.swing.JTextField();
        jtfName = new javax.swing.JTextField();
        jtfQuantity = new javax.swing.JTextField();
        jtfUnitCode = new javax.swing.JTextField();
        jtfInput = new javax.swing.JTextField();
        jtfRemaining = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jlLotQuantity = new javax.swing.JLabel();
        jlLotDateExpiration_n = new javax.swing.JLabel();
        jlLotLot = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        moDecLotQuantity = new sba.lib.gui.bean.DBeanFieldDecimal();
        jtfLotUnitCode = new javax.swing.JTextField();
        moDateLotDateExpiration_n = new sba.lib.gui.bean.DBeanFieldDate();
        moTextLot = new sba.lib.gui.bean.DBeanFieldText();
        jbFind = new javax.swing.JButton();
        jbAdd = new javax.swing.JButton();
        jbClear = new javax.swing.JButton();
        jpPanelCentral = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlPriceUnitary = new javax.swing.JLabel();
        jlDiscountUnitary = new javax.swing.JLabel();
        jlDiscountRow = new javax.swing.JLabel();
        jlCurrencyCode = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        moDecPriceUnitary = new sba.lib.gui.bean.DBeanFieldDecimal();
        moDecDiscountUnitary = new sba.lib.gui.bean.DBeanFieldDecimal();
        moDecDiscountRow = new sba.lib.gui.bean.DBeanFieldDecimal();
        jtfCurrencyCode = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jtfBranchCode = new javax.swing.JTextField();
        jtfBranchWarehouseCode = new javax.swing.JTextField();
        jtfYear = new javax.swing.JTextField();

        jpPanelItem.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        jpPanelItem.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel1.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlCode.setText("Código ítem:");
        jlCode.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel1.add(jlCode);

        jlName.setText("Nombre ítem:");
        jlName.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel1.add(jlName);

        jlQuantity.setText("Cantidad:");
        jlQuantity.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel1.add(jlQuantity);

        jlUnitCode.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel1.add(jlUnitCode);

        jlInput.setText("Capturados:");
        jlInput.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel1.add(jlInput);

        jlRemaining.setText("Restantes:");
        jlRemaining.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel1.add(jlRemaining);

        jpPanelItem.add(jPanel1);

        jPanel3.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jtfCode.setEditable(false);
        jtfCode.setText("CODE");
        jtfCode.setToolTipText("Código");
        jtfCode.setFocusable(false);
        jtfCode.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel3.add(jtfCode);

        jtfName.setEditable(false);
        jtfName.setText("NAME");
        jtfName.setToolTipText("Nombre");
        jtfName.setFocusable(false);
        jtfName.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel3.add(jtfName);

        jtfQuantity.setEditable(false);
        jtfQuantity.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfQuantity.setText("0");
        jtfQuantity.setFocusable(false);
        jtfQuantity.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel3.add(jtfQuantity);

        jtfUnitCode.setEditable(false);
        jtfUnitCode.setText("UN");
        jtfUnitCode.setFocusable(false);
        jtfUnitCode.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel3.add(jtfUnitCode);

        jtfInput.setEditable(false);
        jtfInput.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfInput.setText("0");
        jtfInput.setFocusable(false);
        jtfInput.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel3.add(jtfInput);

        jtfRemaining.setEditable(false);
        jtfRemaining.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfRemaining.setText("0");
        jtfRemaining.setFocusable(false);
        jtfRemaining.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel3.add(jtfRemaining);

        jpPanelItem.add(jPanel3);

        jPanel2.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlLotQuantity.setText("Cantidad:");
        jlLotQuantity.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel2.add(jlLotQuantity);

        jlLotDateExpiration_n.setText("Caducidad:");
        jlLotDateExpiration_n.setPreferredSize(new java.awt.Dimension(103, 23));
        jPanel2.add(jlLotDateExpiration_n);

        jlLotLot.setText("Lote:");
        jlLotLot.setPreferredSize(new java.awt.Dimension(142, 23));
        jPanel2.add(jlLotLot);

        jpPanelItem.add(jPanel2);

        jPanel4.setLayout(new java.awt.FlowLayout(3, 5, 0));

        moDecLotQuantity.setToolTipText("Cantidad");
        moDecLotQuantity.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel4.add(moDecLotQuantity);

        jtfLotUnitCode.setEditable(false);
        jtfLotUnitCode.setText("UN");
        jtfLotUnitCode.setToolTipText("Unidad");
        jtfLotUnitCode.setFocusable(false);
        jtfLotUnitCode.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel4.add(jtfLotUnitCode);

        moDateLotDateExpiration_n.setToolTipText("Caducidad");
        jPanel4.add(moDateLotDateExpiration_n);

        moTextLot.setToolTipText("Lote");
        moTextLot.setPreferredSize(new java.awt.Dimension(142, 23));
        jPanel4.add(moTextLot);

        jbFind.setText("...");
        jbFind.setToolTipText("Buscar lote");
        jbFind.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbFind);

        jbAdd.setIcon(miClient.getImageIcon(DImgConsts.CMD_STD_ADD));
        jbAdd.setToolTipText("Agregar lote");
        jbAdd.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbAdd);

        jbClear.setIcon(miClient.getImageIcon(DImgConsts.CMD_STD_CLEAR));
        jbClear.setToolTipText("Limpiar lote");
        jbClear.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbClear);

        jpPanelItem.add(jPanel4);

        getContentPane().add(jpPanelItem, java.awt.BorderLayout.NORTH);

        jpPanelCentral.setPreferredSize(new java.awt.Dimension(100, 100));
        jpPanelCentral.setLayout(new java.awt.BorderLayout());

        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel7.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel6.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlPriceUnitary.setText("Precio u.:");
        jlPriceUnitary.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlPriceUnitary);

        jlDiscountUnitary.setText("Descto. u.:");
        jlDiscountUnitary.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlDiscountUnitary);

        jlDiscountRow.setText("Descto. par.:");
        jlDiscountRow.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel6.add(jlDiscountRow);

        jlCurrencyCode.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel6.add(jlCurrencyCode);

        jPanel7.add(jPanel6);

        jPanel9.setLayout(new java.awt.FlowLayout(0, 5, 0));

        moDecPriceUnitary.setEditable(false);
        jPanel9.add(moDecPriceUnitary);

        moDecDiscountUnitary.setEditable(false);
        jPanel9.add(moDecDiscountUnitary);

        moDecDiscountRow.setEditable(false);
        moDecDiscountRow.setPreferredSize(new java.awt.Dimension(80, 23));
        jPanel9.add(moDecDiscountRow);

        jtfCurrencyCode.setEditable(false);
        jtfCurrencyCode.setText("TEXT");
        jtfCurrencyCode.setFocusable(false);
        jtfCurrencyCode.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel9.add(jtfCurrencyCode);

        jPanel7.add(jPanel9);

        jPanel8.add(jPanel7, java.awt.BorderLayout.CENTER);

        jPanel10.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel11.setLayout(new java.awt.FlowLayout(2, 5, 0));
        jPanel10.add(jPanel11);

        jPanel12.setLayout(new java.awt.FlowLayout(2, 5, 0));

        jtfBranchCode.setEditable(false);
        jtfBranchCode.setToolTipText(DUtilConsts.TXT_BRANCH);
        jtfBranchCode.setFocusable(false);
        jtfBranchCode.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel12.add(jtfBranchCode);

        jtfBranchWarehouseCode.setEditable(false);
        jtfBranchWarehouseCode.setToolTipText(DUtilConsts.TXT_BRANCH_WAH);
        jtfBranchWarehouseCode.setFocusable(false);
        jtfBranchWarehouseCode.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel12.add(jtfBranchWarehouseCode);

        jtfYear.setEditable(false);
        jtfYear.setToolTipText(DUtilConsts.TXT_FISCAL_YEAR);
        jtfYear.setFocusable(false);
        jtfYear.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel12.add(jtfYear);

        jPanel10.add(jPanel12);

        jPanel8.add(jPanel10, java.awt.BorderLayout.EAST);

        jpPanelCentral.add(jPanel8, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jpPanelCentral, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsCustom() {
        int[] dpsClassKey = null;

        DGuiUtils.setWindowBounds(this, 640, 400);

        moDecLotQuantity.setDecimalSettings(moDecLotQuantity.getToolTipText(), DGuiConsts.GUI_TYPE_DEC_QTY, true);
        moDateLotDateExpiration_n.setDateSettings(miClient, moDateLotDateExpiration_n.getToolTipText(), true);
        moTextLot.setTextSettings(moTextLot.getToolTipText(), 25);

        moFields.addField(moDecLotQuantity);
        moFields.addField(moDateLotDateExpiration_n);
        moFields.addField(moTextLot);
        moFields.setFormButton(jbAdd);

        moDecPriceUnitary.setDecimalSettings(DGuiUtils.getLabelName(jlPriceUnitary.getText()), DGuiConsts.GUI_TYPE_DEC_AMT_UNIT, false);
        moDecDiscountUnitary.setDecimalSettings(DGuiUtils.getLabelName(jlDiscountUnitary.getText()), DGuiConsts.GUI_TYPE_DEC_AMT_UNIT, false);
        moDecDiscountRow.setDecimalSettings(DGuiUtils.getLabelName(jlDiscountRow.getText()), DGuiConsts.GUI_TYPE_DEC_AMT, false);

        mvLotsToAdjust = new Vector<>();
        moDialogLotInStock = new DDialogLotInStock(miClient);

        if (DTrnUtils.isXTypeForDps(mnFormType)) {
            dpsClassKey = DTrnUtils.getDpsClassByDpsXType(mnFormType, mnFormSubtype);

            if (DTrnUtils.isDpsClassForStockIn(dpsClassKey)) {
                mnIogCategory = DModSysConsts.TS_IOG_CT_IN;
                jbFind.setEnabled(false);
            }
            else if (DTrnUtils.isDpsClassForStockOut(dpsClassKey)) {
                mnIogCategory = DModSysConsts.TS_IOG_CT_OUT;
                jbFind.setEnabled(true);
            }
            else {
                mnIogCategory = DLibConsts.UNDEFINED;
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
        else if (DTrnUtils.isXTypeForIog(mnFormType)) {
            mnIogCategory = mnFormSubtype;

            if (mnIogCategory == DModSysConsts.TS_IOG_CT_IN) {
                jbFind.setEnabled(false);
            }
            else if (mnIogCategory == DModSysConsts.TS_IOG_CT_OUT) {
                jbFind.setEnabled(true);
            }
            else {
                mnIogCategory = DLibConsts.UNDEFINED;
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
        else {
            mnIogCategory = DLibConsts.UNDEFINED;
            miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        jbSave.setText(DGuiConsts.TXT_BTN_OK);

        moGridLots = new DGridPaneForm(miClient, mnFormType, DModConsts.T_LOT, msTitle) {

            @Override
            public void initGrid() {
                setRowButtonsEnabled(false, false, true);
            }

            @Override
            public void createGridColumns() {
                int col = 0;
                DGridColumnForm[] columns = new DGridColumnForm[3];

                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DATE, "Caducidad");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_CAT_M, "Lote");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_QTY, "Cantidad");

                for (col = 0; col < columns.length; col++) {
                    moModel.getGridColumns().add(columns[col]);
                }
            }
        };

        moGridLots.setForm(null);
        moGridLots.setPaneFormOwner(this);

        jpPanelCentral.add(moGridLots, BorderLayout.CENTER);

        mvFormGrids.add(moGridLots);
    }

    @Override
    protected void windowActivated() {
        if (mbFirstActivation) {
            mbFirstActivation = false;

            if (mdQuantity == computeQuantity()) {
                jbSave.requestFocus();
            }
            else {
                moDecLotQuantity.requestFocus();
            }
        }
    }

    private Vector<DTrnStockMove> getLots() {
        Vector<DTrnStockMove> lots = new Vector<>();

        for (DGridRow row : moGridLots.getModel().getGridRows()) {
            lots.add(((DRowLot) row).getStockMove());
        }

        return lots;
    }

    private double computeQuantity() {
        double quantity = 0;

        for (DGridRow row : moGridLots.getModel().getGridRows()) {
            quantity += ((DRowLot) row).getStockMove().getQuantity();
        }

        return quantity;
    }

    private boolean canAdd() {
        boolean can = false;
        double quantity = computeQuantity();

        if (quantity > mdQuantity) {
            miClient.showMsgBoxWarning("La cantidad capturada (" + DLibUtils.getDecimalFormatQuantity().format(quantity) + " " + moUnit.getCode() + ") "
                    + "es mayor a la requerida (" + DLibUtils.getDecimalFormatQuantity().format(mdQuantity) + " " + moUnit.getCode() + ").");
        }
        else if (quantity == mdQuantity) {
            miClient.showMsgBoxWarning("La cantidad capturada (" + DLibUtils.getDecimalFormatQuantity().format(quantity) + " " + moUnit.getCode() + ") "
                    + "es igual a la requerida (" + DLibUtils.getDecimalFormatQuantity().format(mdQuantity) + " " + moUnit.getCode() + ").");
        }
        else {
            can = true;
        }

        return can;
    }

    private void renderDate() {
        jtfYear.setText(DLibUtils.DecimalFormatCalendarYear.format(mnYear));
    }

    private void renderBranchWarehouse() {
        jtfBranchCode.setText((String) miClient.getSession().readField(DModConsts.BU_BRA, new int[] { manWarehouseKey[0], manWarehouseKey[1] }, DDbRegistry.FIELD_CODE));
        jtfBranchWarehouseCode.setText((String) miClient.getSession().readField(DModConsts.CU_WAH, manWarehouseKey, DDbRegistry.FIELD_CODE));
    }

    private void renderItem() {
        if (moItem == null) {
            jtfCode.setText("");
            jtfName.setText("");

            jtfCode.setToolTipText(DGuiConsts.TXT_LBL_CODE);
            jtfName.setToolTipText(DGuiConsts.TXT_LBL_NAME);
        }
        else {
            jtfCode.setText(moItem.getCode());
            jtfName.setText(moItem.getName());

            jtfCode.setToolTipText(DGuiConsts.TXT_LBL_CODE + ": " + moItem.getCode());
            jtfName.setToolTipText(DGuiConsts.TXT_LBL_NAME + ": " + moItem.getName());
        }

        jtfCode.setCaretPosition(0);
        jtfName.setCaretPosition(0);
    }

    private void renderUnit() {
        if (moUnit == null) {
            jtfUnitCode.setText("");
            jtfLotUnitCode.setText("");

            jtfUnitCode.setToolTipText(DGuiConsts.TXT_LBL_UNIT);
            jtfLotUnitCode.setToolTipText(DGuiConsts.TXT_LBL_UNIT);
        }
        else {
            jtfUnitCode.setText(moUnit.getCode());
            jtfLotUnitCode.setText(moUnit.getCode());

            jtfUnitCode.setToolTipText(DGuiConsts.TXT_LBL_UNIT + ": " + moUnit.getCode());
            jtfLotUnitCode.setToolTipText(DGuiConsts.TXT_LBL_UNIT + ": " + moUnit.getCode());
        }

        jtfUnitCode.setCaretPosition(0);
        jtfLotUnitCode.setCaretPosition(0);
    }

    private void renderQuantity() {
        jtfQuantity.setText(DLibUtils.getDecimalFormatQuantity().format(mdQuantity));
        jtfQuantity.setCaretPosition(0);
    }

    private void renderPrices() {
        if (madPrices == null) {
            moDecPriceUnitary.resetField();
            moDecDiscountUnitary.resetField();
            moDecDiscountRow.resetField();
        }
        else {
            moDecPriceUnitary.setValue(madPrices[0]);
            moDecDiscountUnitary.setValue(madPrices[1]);
            moDecDiscountRow.setValue(madPrices[2]);
        }
    }

    private void renderCurrencyCode() {
        jtfCurrencyCode.setText(msCurrencyCode.length() == 0 ? "?" : msCurrencyCode);
        jtfCurrencyCode.setCaretPosition(0);
    }

    private void renderRemainingLots() {
        double quantity = computeQuantity();

        jtfInput.setText(DLibUtils.getDecimalFormatQuantity().format(quantity));
        jtfRemaining.setText(DLibUtils.getDecimalFormatQuantity().format(mdQuantity - quantity));
        jtfInput.setCaretPosition(0);
        jtfRemaining.setCaretPosition(0);
    }

    private void actionFind() {
        double quantity = 0;
        DTrnStockMove stockMove = null;

        if (canAdd()) {
            moDialogLotInStock.resetForm();
            moDialogLotInStock.setValue(DModSysConsts.PARAM_DATE, mtDate);
            moDialogLotInStock.setValue(DModSysConsts.PARAM_ITM_UNT, manItemUnitKey);
            moDialogLotInStock.setValue(DModSysConsts.PARAM_BRA_WAH, manWarehouseKey);
            moDialogLotInStock.setValue(DModSysConsts.PARAM_BKK_NUM_KEY, manBookkeepingNumberKey);
            moDialogLotInStock.setValue(DModSysConsts.PARAM_VEC_STK_MOV_ADJ, mvLotsToAdjust);     // stock lots available to adjust
            moDialogLotInStock.setValue(DModSysConsts.PARAM_VEC_STK_MOV, getLots());              // stock lots currently in document
            moDialogLotInStock.setVisible(true);

            if (moDialogLotInStock.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                quantity = computeQuantity();
                stockMove = (DTrnStockMove) moDialogLotInStock.getValue(DModSysConsts.PARAM_OBJ_STK_MOV);
                moDecLotQuantity.setValue(stockMove.getQuantity() <= (mdQuantity - quantity) ? stockMove.getQuantity() : (mdQuantity - quantity));
                moDateLotDateExpiration_n.setValue(stockMove.getDateExpiration());
                moTextLot.setValue(stockMove.getLot());
                moDecLotQuantity.requestFocus();
            }
        }
    }

    private void actionAdd() {
        boolean add = true;
        double quantity = 0;
        DTrnStockMove stockMove = null;
        DRowLot rowLot = null;

        if (canAdd()) {
            quantity = computeQuantity();

            if (moDecLotQuantity.getValue() <= 0) {
                miClient.showMsgBoxWarning(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + DGuiUtils.getLabelName(jlLotQuantity) + "'");
                moDecLotQuantity.requestFocus();
            }
            else if (moDecLotQuantity.getValue() > (mdQuantity - quantity)) {
                miClient.showMsgBoxWarning(DGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + DGuiUtils.getLabelName(jlLotQuantity) + "' " + DGuiConsts.ERR_MSG_FIELD_VAL_LESS_EQUAL + " " + DLibUtils.getDecimalFormatQuantity().format(mdQuantity - quantity) + " " + moUnit.getCode() + ".");
                moDecLotQuantity.requestFocus();
            }
            else if (moDateLotDateExpiration_n.getValue() == null) {
                miClient.showMsgBoxWarning(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + DGuiUtils.getLabelName(jlLotDateExpiration_n) + "'");
                moDateLotDateExpiration_n.getComponent().requestFocus();
            }
            else if (moTextLot.getValue().isEmpty()) {
                miClient.showMsgBoxWarning(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + DGuiUtils.getLabelName(jlLotLot) + "'");
                moTextLot.requestFocus();
            }
            else {
                for (DGridRow row : moGridLots.getModel().getGridRows()) {
                    stockMove = ((DRowLot) row).getStockMove();
                    if (moTextLot.getValue().compareToIgnoreCase(stockMove.getLot()) == 0) {
                        miClient.showMsgBoxWarning("El lote '" + stockMove.getLot() + "' ya está capturado.");
                        add = false;
                        break;
                    }
                }

                if (add) {
                    rowLot = new DRowLot(new DTrnStockMove(new int[] { manItemUnitKey[0], manItemUnitKey[1], DLibConsts.UNDEFINED, manWarehouseKey[0], manWarehouseKey[1], manWarehouseKey[2] },
                            moDecLotQuantity.getValue(), "", "", null, moTextLot.getValue(), moDateLotDateExpiration_n.getValue()));
                    moGridLots.addGridRow(rowLot);
                    moGridLots.renderGridRows();
                    moGridLots.setSelectedGridRow(moGridLots.getTable().getRowCount() - 1);

                    moFields.resetFields();
                    renderRemainingLots();

                    quantity = computeQuantity();

                    if (quantity < mdQuantity) {
                        moDecLotQuantity.requestFocus();
                    }
                    else {
                        jbSave.requestFocus();
                    }
                }
            }
        }
    }

    private void actionClear() {
        moFields.resetFields();
        moDecLotQuantity.requestFocus();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup jButtonGroup;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbAdd;
    private javax.swing.JButton jbClear;
    private javax.swing.JButton jbFind;
    private javax.swing.JLabel jlCode;
    private javax.swing.JLabel jlCurrencyCode;
    private javax.swing.JLabel jlDiscountRow;
    private javax.swing.JLabel jlDiscountUnitary;
    private javax.swing.JLabel jlInput;
    private javax.swing.JLabel jlLotDateExpiration_n;
    private javax.swing.JLabel jlLotLot;
    private javax.swing.JLabel jlLotQuantity;
    private javax.swing.JLabel jlName;
    private javax.swing.JLabel jlPriceUnitary;
    private javax.swing.JLabel jlQuantity;
    private javax.swing.JLabel jlRemaining;
    private javax.swing.JLabel jlUnitCode;
    private javax.swing.JPanel jpPanelCentral;
    private javax.swing.JPanel jpPanelItem;
    private javax.swing.JTextField jtfBranchCode;
    private javax.swing.JTextField jtfBranchWarehouseCode;
    private javax.swing.JTextField jtfCode;
    private javax.swing.JTextField jtfCurrencyCode;
    private javax.swing.JTextField jtfInput;
    private javax.swing.JTextField jtfLotUnitCode;
    private javax.swing.JTextField jtfName;
    private javax.swing.JTextField jtfQuantity;
    private javax.swing.JTextField jtfRemaining;
    private javax.swing.JTextField jtfUnitCode;
    private javax.swing.JTextField jtfYear;
    private sba.lib.gui.bean.DBeanFieldDate moDateLotDateExpiration_n;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecDiscountRow;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecDiscountUnitary;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecLotQuantity;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecPriceUnitary;
    private sba.lib.gui.bean.DBeanFieldText moTextLot;
    // End of variables declaration//GEN-END:variables

    @Override
    public void addAllListeners() {
        jbFind.addActionListener(this);
        jbAdd.addActionListener(this);
        jbClear.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbFind.removeActionListener(this);
        jbAdd.removeActionListener(this);
        jbClear.removeActionListener(this);
    }

    @Override
    public void reloadCatalogues() {

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
        int idLot = DLibConsts.UNDEFINED;
        double stock = 0;
        double quantity = computeQuantity();
        boolean exists = false;
        DTrnStockMove move = null;
        DGuiValidation validation = new DGuiValidation();

        if (quantity != mdQuantity) {
            validation.setMessage("Se especificó que se desean capturar " + DLibUtils.getDecimalFormatQuantity().format(mdQuantity) + " " + moUnit.getCode() + ", "
                    + "pero se han capturado " + DLibUtils.getDecimalFormatQuantity().format(quantity) + " " + moUnit.getCode() + ".");
            validation.setComponent(moDecLotQuantity);
        }
        else {
            if (mvLotsToAdjust.size() > 0) {
                // Validate serial numbers against serial numbers available to adjust:

                for (DGridRow row : moGridLots.getModel().getGridRows()) {
                    exists = false;
                    move = ((DRowLot) row).getStockMove();

                    for (DTrnStockMove moveToAdjust : mvLotsToAdjust) {
                        if (move.getLot().compareTo(moveToAdjust.getLot()) == 0) {
                            exists = true;
                            break;
                        }
                    }

                    if (!exists) {
                        validation.setMessage("No se puede procesar el lote '" + move.getLot() + "' " +
                                "porque no pertenece al conjunto de lotes para ajustar.");
                        validation.setComponent(moDecLotQuantity);
                        break;
                    }
                }
            }

            if (validation.isValid()) {
                // Validate stock lots:

                switch (mnIogCategory) {
                    case DModSysConsts.TS_IOG_CT_IN:
                        for (DGridRow row : moGridLots.getModel().getGridRows()) {
                            move = ((DRowLot) row).getStockMove();
                            idLot = DTrnUtils.getLotId(miClient.getSession(), manItemUnitKey[0], manItemUnitKey[1], move.getLot(), move.getDateExpiration());

                            if (idLot == DLibConsts.UNDEFINED) {
                                move.setPkLotId(DUtilConsts.LOT_ID);
                            }
                            else {
                                move.setPkLotId(idLot);
                            }
                        }
                        break;

                    case DModSysConsts.TS_IOG_CT_OUT:
                        for (DGridRow row : moGridLots.getModel().getGridRows()) {
                            move = ((DRowLot) row).getStockMove();
                            idLot = DTrnUtils.getLotId(miClient.getSession(), manItemUnitKey[0], manItemUnitKey[1], move.getLot(), move.getDateExpiration());

                            if (idLot == DLibConsts.UNDEFINED) {
                                validation.setMessage("No se puede procesar el lote '" + move.getLot() + "' porque no existe.");
                                validation.setComponent(moDecLotQuantity);
                                break;
                            }
                            else {
                                move.setPkLotId(idLot);
                                stock = DTrnUtils.getStockForLot(miClient.getSession(), mnYear, new int[] { manItemUnitKey[0], manItemUnitKey[1], idLot }, manWarehouseKey);

                                if (stock < move.getQuantity()) {
                                    validation.setMessage("No se puede procesar el lote '" + move.getLot() + "' "
                                            + "porque que se requieren " + DLibUtils.getDecimalFormatQuantity().format(move.getQuantity()) + " " + moUnit.getCode() + ", "
                                            + "pero sólo hay " + DLibUtils.getDecimalFormatQuantity().format(stock) + " " + moUnit.getCode() + " en el almacén del documento.");
                                    validation.setComponent(moDecLotQuantity);
                                    break;
                                }
                            }
                        }
                        break;
                    default:
                }
            }
        }

        return validation;
    }

    @Override
    public void resetForm() {
        removeAllListeners();
        reloadCatalogues();

        mnFormResult = DLibConsts.UNDEFINED;
        mbFirstActivation = true;

        mtDate = null;
        mnYear = 0;
        mdQuantity = 0;
        manItemUnitKey = null;
        manWarehouseKey = null;
        manBookkeepingNumberKey = null;
        madPrices = null;
        msCurrencyCode = "";
        moItem = null;
        moUnit = null;
        mvLotsToAdjust.clear();

        jbFind.setEnabled(mnIogCategory == DModSysConsts.TS_IOG_CT_OUT);

        actionClear();
        renderItem();
        renderUnit();
        renderQuantity();
        renderPrices();
        renderCurrencyCode();
        moGridLots.clearGridRows();
        renderRemainingLots();

        addAllListeners();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(final int type, final Object value) {
        Vector<DGridRow> rows = null;

        switch (type) {
            case DModSysConsts.PARAM_DATE:
                mtDate = (Date) value;
                mnYear = DLibTimeUtils.digestYear(mtDate)[0];
                renderDate();
                break;
            case DModSysConsts.PARAM_ITM_UNT:
                manItemUnitKey = (int[]) value;
                moItem = (DDbItem) miClient.getSession().readRegistry(DModConsts.IU_ITM, new int[] { manItemUnitKey[0] });
                moUnit = (DDbUnit) miClient.getSession().readRegistry(DModConsts.IU_UNT, new int[] { manItemUnitKey[1] });
                renderItem();
                renderUnit();
                break;
            case DModSysConsts.PARAM_BRA_WAH:
                manWarehouseKey = (int[]) value;
                renderBranchWarehouse();
                break;
            case DModSysConsts.PARAM_BKK_NUM_KEY:
                manBookkeepingNumberKey = (int[]) value;
                break;
            case DModSysConsts.PARAM_QTY:
                mdQuantity = (Double) value;
                renderQuantity();
                break;
            case DModSysConsts.PARAM_PRC:
                madPrices = (double[]) value;
                renderPrices();
                break;
            case DModSysConsts.PARAM_CUR:
                msCurrencyCode = (String) value;
                renderCurrencyCode();
                break;
            case DModSysConsts.PARAM_VEC_STK_MOV_ADJ:
                mvLotsToAdjust.addAll((Vector<DTrnStockMove>) value);
                jbFind.setEnabled(mvLotsToAdjust.size() > 0);
                break;
            case DModSysConsts.PARAM_VEC_STK_MOV:
                rows = new Vector<>();
                for (DTrnStockMove move : (Vector<DTrnStockMove>) value) {
                    rows.add(new DRowLot(move));
                }
                moGridLots.populateGrid(rows);
                renderRemainingLots();
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
    }

    @Override
    public Object getValue(final int type) {
        Object value = null;

        switch (type) {
            case DModSysConsts.PARAM_VEC_STK_MOV:
                value = getLots();
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return value;
    }

    @Override
    public void notifyRowNew(int gridType, int gridSubtype, int row, DGridRow gridRow) {

    }

    @Override
    public void notifyRowEdit(int gridType, int gridSubtype, int row, DGridRow gridRow) {

    }

    @Override
    public void notifyRowDelete(int gridType, int gridSubtype, int row, DGridRow gridRow) {
        renderRemainingLots();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof DBeanFieldText) {
            DBeanFieldText field = (DBeanFieldText) e.getSource();

            if (field == moTextLot) {
                actionAdd();
            }
        }
        else if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbFind) {
                actionFind();
            }
            else if (button == jbAdd) {
                actionAdd();
            }
            else if (button == jbClear) {
                actionClear();
            }
        }
    }
}
