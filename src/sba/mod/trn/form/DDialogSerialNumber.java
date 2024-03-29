/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DDialogSerialNumber.java
 *
 * Created on 7/11/2011, 07:51:51 PM
 */

package sba.mod.trn.form;

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
import sba.mod.itm.db.DDbItemGenus;
import sba.mod.itm.db.DDbUnit;
import sba.mod.trn.db.DTrnStockMove;
import sba.mod.trn.db.DTrnUtils;

/**
 *
 * @author Sergio Flores
 */
public class DDialogSerialNumber extends DBeanFormDialog implements ActionListener {

    private int mnIogCategory;

    private Date mtDate;
    private int mnYear;
    private int mnQuantity;
    private int mnSerialNumberLength;
    private int[] manLotKey;
    private int[] manWarehouseKey;
    private int[] manBookkeepingNumberKey;
    private double[] madPrices;
    private String msCurrencyCode;
    private DDbItem moItem;
    private DDbUnit moUnit;
    private Vector<DTrnStockMove> mvSerialNumbersToAdjust;  // serial numbers available to adjust a DPS
    private Vector<DTrnStockMove> mvSerialNumbers;          // serial numbers in DPS
    private DDialogSerialNumberInStock moDialogSerialNumberInStock;

    /** Creates new form DDialogSerialNumber
     * @param client GUI client.
     * @param type XType of DPS. Constants defined in DModConsts (TX_DPS_... or TX_IOG_...).
     * @param subtype Category of DPS. Constants defined in DModSysConsts (TS_DPS_CT_... or TS_IOG_CT_...).
     */
    public DDialogSerialNumber(DGuiClient client, int type, int subtype) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, type, subtype, "Números de serie");
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
        jPanel2 = new javax.swing.JPanel();
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
        jPanel4 = new javax.swing.JPanel();
        moTextSerialNumber = new sba.lib.gui.bean.DBeanFieldText();
        jbFind = new javax.swing.JButton();
        jbAdd = new javax.swing.JButton();
        jbRemove = new javax.swing.JButton();
        jtfSerialNumberName = new javax.swing.JTextField();
        jtfSerialNumberLength = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jspSerialNumbers = new javax.swing.JScrollPane();
        jltSerialNumber = new javax.swing.JList();
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

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        jPanel2.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCode.setText("Código ítem:");
        jlCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel1.add(jlCode);

        jlName.setText("Nombre ítem:");
        jlName.setPreferredSize(new java.awt.Dimension(260, 23));
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

        jPanel2.add(jPanel1);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfCode.setEditable(false);
        jtfCode.setText("CODE");
        jtfCode.setToolTipText("Código");
        jtfCode.setFocusable(false);
        jtfCode.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jtfCode);

        jtfName.setEditable(false);
        jtfName.setText("NAME");
        jtfName.setToolTipText("Nombre");
        jtfName.setFocusable(false);
        jtfName.setPreferredSize(new java.awt.Dimension(260, 23));
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

        jPanel2.add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moTextSerialNumber.setToolTipText("Número de serie");
        moTextSerialNumber.setPreferredSize(new java.awt.Dimension(365, 23));
        jPanel4.add(moTextSerialNumber);

        jbFind.setText("...");
        jbFind.setToolTipText("Buscar número de serie");
        jbFind.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbFind);

        jbAdd.setIcon(miClient.getImageIcon(DImgConsts.CMD_STD_ADD));
        jbAdd.setToolTipText("Agregar número de serie");
        jbAdd.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbAdd);

        jbRemove.setIcon(miClient.getImageIcon(DImgConsts.CMD_STD_SUBTRACT));
        jbRemove.setToolTipText("Remover número de serie");
        jbRemove.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbRemove);

        jtfSerialNumberName.setEditable(false);
        jtfSerialNumberName.setText("NAME");
        jtfSerialNumberName.setToolTipText("Número de serie");
        jtfSerialNumberName.setFocusable(false);
        jtfSerialNumberName.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel4.add(jtfSerialNumberName);

        jtfSerialNumberLength.setEditable(false);
        jtfSerialNumberLength.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfSerialNumberLength.setText("0");
        jtfSerialNumberLength.setToolTipText("Longitud");
        jtfSerialNumberLength.setFocusable(false);
        jtfSerialNumberLength.setPreferredSize(new java.awt.Dimension(30, 23));
        jPanel4.add(jtfSerialNumberLength);

        jPanel2.add(jPanel4);

        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel5.setPreferredSize(new java.awt.Dimension(100, 100));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jspSerialNumbers.setPreferredSize(new java.awt.Dimension(100, 100));

        jltSerialNumber.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jspSerialNumbers.setViewportView(jltSerialNumber);

        jPanel5.add(jspSerialNumbers, java.awt.BorderLayout.CENTER);

        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel7.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

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

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

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

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));
        jPanel10.add(jPanel11);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

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

        jPanel5.add(jPanel8, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsCustom() {
        int[] dpsClassKey = null;

        DGuiUtils.setWindowBounds(this, 640, 400);

        moTextSerialNumber.setTextSettings(DGuiUtils.getLabelName(moTextSerialNumber.getToolTipText()), DDbItemGenus.LEN_SNR);
        moTextSerialNumber.setFieldButton(jbFind);

        moFields.addField(moTextSerialNumber);

        moDecPriceUnitary.setDecimalSettings(DGuiUtils.getLabelName(jlPriceUnitary.getText()), DGuiConsts.GUI_TYPE_DEC_AMT_UNIT, false);
        moDecDiscountUnitary.setDecimalSettings(DGuiUtils.getLabelName(jlDiscountUnitary.getText()), DGuiConsts.GUI_TYPE_DEC_AMT_UNIT, false);
        moDecDiscountRow.setDecimalSettings(DGuiUtils.getLabelName(jlDiscountRow.getText()), DGuiConsts.GUI_TYPE_DEC_AMT, false);

        mvSerialNumbersToAdjust = new Vector<>();
        mvSerialNumbers = new Vector<>();
        moDialogSerialNumberInStock = new DDialogSerialNumberInStock(miClient);

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
    }

    @Override
    protected void windowActivated() {
        if (mbFirstActivation) {
            mbFirstActivation = false;
            if (mvSerialNumbers.size() == mnQuantity) {
                jbSave.requestFocus();
            }
            else {
                moTextSerialNumber.requestFocus();
            }
        }
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
            mnSerialNumberLength = 0;

            jtfCode.setText("");
            jtfName.setText("");
            jtfSerialNumberName.setText("");
            jtfSerialNumberLength.setText("");

            moTextSerialNumber.setMaxLength(DDbItemGenus.LEN_SNR);

            jtfCode.setToolTipText(DGuiConsts.TXT_LBL_CODE);
            jtfName.setToolTipText(DGuiConsts.TXT_LBL_NAME);
        }
        else {
            mnSerialNumberLength = moItem.getParentGenus().getSerialNumberLength();

            jtfCode.setText(moItem.getCode());
            jtfName.setText(moItem.getName());
            jtfSerialNumberName.setText(moItem.getParentGenus().getSerialNumberName());
            jtfSerialNumberLength.setText(mnSerialNumberLength == 0 ? "" : DLibUtils.DecimalFormatInteger.format(mnSerialNumberLength));

            moTextSerialNumber.setMaxLength(mnSerialNumberLength == 0 ? DDbItemGenus.LEN_SNR : mnSerialNumberLength);

            jtfCode.setToolTipText(DGuiConsts.TXT_LBL_CODE + ": " + moItem.getCode());
            jtfName.setToolTipText(DGuiConsts.TXT_LBL_NAME + ": " + moItem.getName());
        }

        jtfCode.setCaretPosition(0);
        jtfName.setCaretPosition(0);
        jtfSerialNumberName.setCaretPosition(0);
        jtfSerialNumberLength.setCaretPosition(0);
    }

    private void renderUnit() {
        if (moUnit == null) {
            jtfUnitCode.setText("");

            jtfUnitCode.setToolTipText(DGuiConsts.TXT_LBL_UNIT);
        }
        else {
            jtfUnitCode.setText(moUnit.getCode());

            jtfUnitCode.setToolTipText(DGuiConsts.TXT_LBL_UNIT + ": " + moUnit.getCode());
        }

        jtfUnitCode.setCaretPosition(0);
    }

    private void renderQuantity() {
        jtfQuantity.setText(DLibUtils.DecimalFormatInteger.format(mnQuantity));
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

    @SuppressWarnings("unchecked")
    private void renderStockMoves() {
        jltSerialNumber.setListData(mvSerialNumbers);
        jtfInput.setText(DLibUtils.DecimalFormatInteger.format(mvSerialNumbers.size()));
        jtfRemaining.setText(DLibUtils.DecimalFormatInteger.format(mnQuantity - mvSerialNumbers.size()));
        jtfInput.setCaretPosition(0);
        jtfRemaining.setCaretPosition(0);
    }

    private void actionFind() {
        DTrnStockMove stockMove = null;

        moDialogSerialNumberInStock.resetForm();
        moDialogSerialNumberInStock.setValue(DModSysConsts.PARAM_DATE, mtDate);
        moDialogSerialNumberInStock.setValue(DModSysConsts.PARAM_LOT_KEY, manLotKey);
        moDialogSerialNumberInStock.setValue(DModSysConsts.PARAM_BRA_WAH, manWarehouseKey);
        moDialogSerialNumberInStock.setValue(DModSysConsts.PARAM_BKK_NUM_KEY, manBookkeepingNumberKey);
        moDialogSerialNumberInStock.setValue(DModSysConsts.PARAM_VEC_STK_MOV_ADJ, mvSerialNumbersToAdjust);     // serial numbers available to adjust
        moDialogSerialNumberInStock.setValue(DModSysConsts.PARAM_VEC_STK_MOV, mvSerialNumbers);                 // serial numbers currently in document
        moDialogSerialNumberInStock.setVisible(true);

        if (moDialogSerialNumberInStock.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
            stockMove = (DTrnStockMove) moDialogSerialNumberInStock.getValue(DModSysConsts.PARAM_OBJ_STK_MOV);
            moTextSerialNumber.setValue(stockMove.getSerialNumber());
            moTextSerialNumber.requestFocus();
        }
    }

    private void actionAdd() {
        boolean add = true;

        if (moTextSerialNumber.getValue().length() == 0) {
            if (mvSerialNumbers.size() == mnQuantity) {
                jbSave.requestFocus();
            }
        }
        else if (mnSerialNumberLength > 0 && mnSerialNumberLength != moTextSerialNumber.getValue().length()) {
            miClient.showMsgBoxWarning("El valor '" + moTextSerialNumber.getValue() + "' debe tener una longitud de " + mnSerialNumberLength + " caracteres.");
        }
        else {
            for (DTrnStockMove move : mvSerialNumbers) {
                if (move.getSerialNumber().compareTo(moTextSerialNumber.getValue()) == 0) {
                    miClient.showMsgBoxWarning("El valor '" + moTextSerialNumber.getValue() + "' ya está capturado.");
                    add = false;
                    break;
                }
            }

            if (add) {
                mvSerialNumbers.add(new DTrnStockMove(new int[] { manLotKey[0], manLotKey[1], manLotKey[2], manWarehouseKey[0], manWarehouseKey[1], manWarehouseKey[2] },
                        1, moTextSerialNumber.getValue()));
                renderStockMoves();

                jltSerialNumber.setSelectedIndex(mvSerialNumbers.size() - 1);
                jltSerialNumber.ensureIndexIsVisible(mvSerialNumbers.size() - 1);

                moFields.resetFields();

                if (mvSerialNumbers.size() < mnQuantity) {
                    moTextSerialNumber.requestFocus();
                }
                else {
                    jbSave.requestFocus();
                }
            }
        }
    }

    private void actionRemove() {
        int index = jltSerialNumber.getSelectedIndex();

        if (index != -1) {
            mvSerialNumbers.remove(index);
            renderStockMoves();

            index = index < mvSerialNumbers.size() ? index : index - 1;
            if (index >= 0) {
                jltSerialNumber.setSelectedIndex(index);
                jltSerialNumber.ensureIndexIsVisible(index);
            }
        }

        moTextSerialNumber.requestFocus();
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
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbAdd;
    private javax.swing.JButton jbFind;
    private javax.swing.JButton jbRemove;
    private javax.swing.JLabel jlCode;
    private javax.swing.JLabel jlCurrencyCode;
    private javax.swing.JLabel jlDiscountRow;
    private javax.swing.JLabel jlDiscountUnitary;
    private javax.swing.JLabel jlInput;
    private javax.swing.JLabel jlName;
    private javax.swing.JLabel jlPriceUnitary;
    private javax.swing.JLabel jlQuantity;
    private javax.swing.JLabel jlRemaining;
    private javax.swing.JLabel jlUnitCode;
    private javax.swing.JList jltSerialNumber;
    private javax.swing.JScrollPane jspSerialNumbers;
    private javax.swing.JTextField jtfBranchCode;
    private javax.swing.JTextField jtfBranchWarehouseCode;
    private javax.swing.JTextField jtfCode;
    private javax.swing.JTextField jtfCurrencyCode;
    private javax.swing.JTextField jtfInput;
    private javax.swing.JTextField jtfName;
    private javax.swing.JTextField jtfQuantity;
    private javax.swing.JTextField jtfRemaining;
    private javax.swing.JTextField jtfSerialNumberLength;
    private javax.swing.JTextField jtfSerialNumberName;
    private javax.swing.JTextField jtfUnitCode;
    private javax.swing.JTextField jtfYear;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecDiscountRow;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecDiscountUnitary;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecPriceUnitary;
    private sba.lib.gui.bean.DBeanFieldText moTextSerialNumber;
    // End of variables declaration//GEN-END:variables

    @Override
    public void addAllListeners() {
        moTextSerialNumber.addActionListener(this);
        jbFind.addActionListener(this);
        jbAdd.addActionListener(this);
        jbRemove.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        moTextSerialNumber.removeActionListener(this);
        jbFind.removeActionListener(this);
        jbAdd.removeActionListener(this);
        jbRemove.removeActionListener(this);
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
        double stock = 0;
        boolean exists = false;
        DGuiValidation validation = new DGuiValidation();

        if (jltSerialNumber.getModel().getSize() != mnQuantity) {
            validation.setMessage("Se especificó que se desean capturar " + mnQuantity + " números de serie, "
                    + "pero sólo hay " + jltSerialNumber.getModel().getSize() + ".");
            validation.setComponent(jltSerialNumber.getModel().getSize() < mnQuantity ? moTextSerialNumber : jltSerialNumber);
        }
        else {
            if (mvSerialNumbersToAdjust.size() > 0) {
                // Validate serial numbers against serial numbers available to adjust:

                for (DTrnStockMove move : mvSerialNumbers) {
                    exists = false;

                    for (DTrnStockMove moveToAdjust : mvSerialNumbersToAdjust) {
                        if (move.getSerialNumber().compareTo(moveToAdjust.getSerialNumber()) == 0) {
                            exists = true;
                            break;
                        }
                    }

                    if (!exists) {
                        validation.setMessage("No se puede procesar el número de serie '" + move.getSerialNumber() + "' " +
                                "porque no pertenece al conjunto de números de serie para ajustar.");
                        validation.setComponent(jltSerialNumber);
                        break;
                    }
                }
            }

            if (validation.isValid()) {
                // Validate serial numbers against stock:

                for (DTrnStockMove move : mvSerialNumbers) {
                    stock = DTrnUtils.getStockForLotSerialNumber(miClient.getSession(), mnYear, manLotKey, manWarehouseKey, move.getSerialNumber());

                    if (mnIogCategory == DModSysConsts.TS_IOG_CT_IN && stock != 0) {
                        validation.setMessage("No se puede procesar el número de serie '" + move.getSerialNumber() + "' " +
                                "porque ya hay " + DLibUtils.DecimalFormatInteger.format(stock) + " " + moUnit.getCode() + " en existencia en el almacén del documento.");
                        validation.setComponent(jltSerialNumber);
                        break;
                    }
                    else if (mnIogCategory == DModSysConsts.TS_IOG_CT_OUT && stock <= 0) {
                        validation.setMessage("No se puede procesar el número de serie '" + move.getSerialNumber() + "' " +
                                "porque que no hay existencias en el almacén del documento.");
                        validation.setComponent(jltSerialNumber);
                        break;
                    }
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
        mnQuantity = 0;
        mnSerialNumberLength = 0;
        manLotKey = null;
        manWarehouseKey = null;
        manBookkeepingNumberKey = null;
        madPrices = null;
        msCurrencyCode = "";
        moItem = null;
        moUnit = null;
        mvSerialNumbersToAdjust.clear();
        mvSerialNumbers.clear();

        moTextSerialNumber.resetField();

        jbFind.setEnabled(mnIogCategory == DModSysConsts.TS_IOG_CT_OUT);

        renderItem();
        renderUnit();
        renderQuantity();
        renderPrices();
        renderCurrencyCode();
        renderStockMoves();

        addAllListeners();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(final int type, final Object value) {
        switch (type) {
            case DModSysConsts.PARAM_DATE:
                mtDate = (Date) value;
                mnYear = DLibTimeUtils.digestYear(mtDate)[0];
                renderDate();
                break;
            case DModSysConsts.PARAM_LOT_KEY:
                manLotKey = (int[]) value;
                moItem = (DDbItem) miClient.getSession().readRegistry(DModConsts.IU_ITM, new int[] { manLotKey[0] });
                moUnit = (DDbUnit) miClient.getSession().readRegistry(DModConsts.IU_UNT, new int[] { manLotKey[1] });
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
                mnQuantity = (Integer) value;
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
                mvSerialNumbersToAdjust.addAll((Vector<DTrnStockMove>) value);
                jbFind.setEnabled(mvSerialNumbersToAdjust.size() > 0);
                break;
            case DModSysConsts.PARAM_VEC_STK_MOV:
                mvSerialNumbers.addAll((Vector<DTrnStockMove>) value);
                renderStockMoves();
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
                value = mvSerialNumbers;
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return value;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof DBeanFieldText) {
            DBeanFieldText field = (DBeanFieldText) e.getSource();

            if (field == moTextSerialNumber) {
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
            else if (button == jbRemove) {
                actionRemove();
            }
        }
    }
}
