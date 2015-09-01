/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DDialogSerialNumberShow.java
 *
 * Created on 7/11/2011, 07:51:51 PM
 */

package sba.mod.trn.form;

import java.util.Vector;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanFormDialog;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.itm.db.DDbItem;
import sba.mod.itm.db.DDbUnit;
import sba.mod.trn.db.DDbDpsRow;
import sba.mod.trn.db.DDbIogRow;
import sba.mod.trn.db.DRowDpsRowAdjusted;
import sba.mod.trn.db.DTrnStockMove;

/**
 *
 * @author Sergio Flores
 */
public class DDialogSerialNumberShow extends DBeanFormDialog {

    public static final int TYPE_DPS = 1;
    public static final int TYPE_ADJ_AVL = 2;
    public static final int TYPE_ADJ_ADJ = 3;

    private DDbItem moItem;
    private DDbUnit moUnit;

    /** Creates new form DDialogSerialNumberShow
     * @param client GUI client.
     * @param type Type of serial numbers to show. Constants defined in this class (TYPE_...).
     */
    public DDialogSerialNumberShow(DGuiClient client, int type) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, type, DLibConsts.UNDEFINED, "");
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
        jlSerialNumberName = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jtfCode = new javax.swing.JTextField();
        jtfName = new javax.swing.JTextField();
        jtfQuantity = new javax.swing.JTextField();
        jtfUnitCode = new javax.swing.JTextField();
        jtfSerialNumberName = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jspSerialNumbers = new javax.swing.JScrollPane();
        jltSerialNumber = new javax.swing.JList();

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 5, 0));
        jPanel2.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

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

        jlSerialNumberName.setText("Número serie:");
        jlSerialNumberName.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel1.add(jlSerialNumberName);

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

        jtfSerialNumberName.setEditable(false);
        jtfSerialNumberName.setText("NAME");
        jtfSerialNumberName.setToolTipText("Número de serie");
        jtfSerialNumberName.setFocusable(false);
        jtfSerialNumberName.setPreferredSize(new java.awt.Dimension(135, 23));
        jPanel3.add(jtfSerialNumberName);

        jPanel2.add(jPanel3);

        getContentPane().add(jPanel2, java.awt.BorderLayout.NORTH);

        jPanel5.setPreferredSize(new java.awt.Dimension(100, 100));
        jPanel5.setLayout(new java.awt.BorderLayout());

        jspSerialNumbers.setPreferredSize(new java.awt.Dimension(100, 100));

        jltSerialNumber.setBackground(java.awt.SystemColor.control);
        jltSerialNumber.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jspSerialNumbers.setViewportView(jltSerialNumber);

        jPanel5.add(jspSerialNumbers, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel5, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsCustom() {
        DGuiUtils.setWindowBounds(this, 640, 400);

        switch (mnFormType) {
            case TYPE_DPS:
                setTitle("Números de serie");
                break;
            case TYPE_ADJ_AVL:
                setTitle("Números de serie disponibles");
                break;
            case TYPE_ADJ_ADJ:
                setTitle("Números de serie ajustados");
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        jbSave.setEnabled(false);
        jbSave.setText(DGuiConsts.TXT_BTN_OK);
        jbCancel.setText(DGuiConsts.TXT_BTN_CLOSE);
    }

    @Override
    protected void windowActivated() {
        if (mbFirstActivation) {
            mbFirstActivation = false;
            jbCancel.requestFocus();
        }
    }

    private void renderRow(Object source) {
        int item = DLibConsts.UNDEFINED;
        int unit = DLibConsts.UNDEFINED;
        double quantity = 0;
        Vector<DTrnStockMove> moves = null;

        if (source == null) {
            moItem = null;
            moUnit = null;
            jtfQuantity.setText("");
            jltSerialNumber.setListData(new Object[0]);
        }
        else {
            if (source instanceof DDbIogRow) {
                item = ((DDbIogRow) source).getFkItemId();
                unit = ((DDbIogRow) source).getFkUnitId();
                quantity = ((DDbIogRow) source).getQuantity();
                moves = ((DDbIogRow) source).getAuxStockMoves();
            }
            else if (source instanceof DDbDpsRow) {
                item = ((DDbDpsRow) source).getFkRowItemId();
                unit = ((DDbDpsRow) source).getFkRowUnitId();
                quantity = ((DDbDpsRow) source).getQuantity();
                moves = ((DDbDpsRow) source).getAuxStockMoves();
            }
            else if (source instanceof DRowDpsRowAdjusted) {
                item = ((DRowDpsRowAdjusted) source).getFkItemId();
                unit = ((DRowDpsRowAdjusted) source).getFkUnitId();

                if (mnFormType == TYPE_ADJ_AVL) {
                    quantity = ((DRowDpsRowAdjusted) source).getQuantityAvailable();
                    moves = ((DRowDpsRowAdjusted) source).getStockMovesAvailable();
                }
                else {
                    quantity = ((DRowDpsRowAdjusted) source).getQuantityAdjusted();
                    moves = ((DRowDpsRowAdjusted) source).getStockMovesAdjusted();
                }
            }

            moItem = (DDbItem) miClient.getSession().readRegistry(DModConsts.IU_ITM, new int[] { item });
            moUnit = (DDbUnit) miClient.getSession().readRegistry(DModConsts.IU_UNT, new int[] { unit });
            jtfQuantity.setText(DLibUtils.DecimalFormatInteger.format(quantity));
            jltSerialNumber.setListData(moves);
        }

        renderItem();
        renderUnit();
    }

    private void renderItem() {
        if (moItem == null) {
            jtfCode.setText("");
            jtfName.setText("");
            jtfSerialNumberName.setText("");

            jtfCode.setToolTipText(DGuiConsts.TXT_LBL_CODE);
            jtfName.setToolTipText(DGuiConsts.TXT_LBL_NAME);
        }
        else {
            jtfCode.setText(moItem.getCode());
            jtfName.setText(moItem.getName());
            jtfSerialNumberName.setText(moItem.getParentGenus().getSerialNumberName());

            jtfCode.setToolTipText(DGuiConsts.TXT_LBL_CODE + ": " + moItem.getCode());
            jtfName.setToolTipText(DGuiConsts.TXT_LBL_NAME + ": " + moItem.getName());
        }

        jtfCode.setCaretPosition(0);
        jtfName.setCaretPosition(0);
        jtfSerialNumberName.setCaretPosition(0);
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup jButtonGroup;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLabel jlCode;
    private javax.swing.JLabel jlName;
    private javax.swing.JLabel jlQuantity;
    private javax.swing.JLabel jlSerialNumberName;
    private javax.swing.JLabel jlUnitCode;
    private javax.swing.JList jltSerialNumber;
    private javax.swing.JScrollPane jspSerialNumbers;
    private javax.swing.JTextField jtfCode;
    private javax.swing.JTextField jtfName;
    private javax.swing.JTextField jtfQuantity;
    private javax.swing.JTextField jtfSerialNumberName;
    private javax.swing.JTextField jtfUnitCode;
    // End of variables declaration//GEN-END:variables

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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DDbRegistry getRegistry() throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DGuiValidation validateForm() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void resetForm() {
        removeAllListeners();
        reloadCatalogues();

        mnFormResult = DLibConsts.UNDEFINED;
        mbFirstActivation = true;

        renderRow(null);

        addAllListeners();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(final int type, final Object value) {
        switch (type) {
            case DModSysConsts.PARAM_OBJ_IOG_ROW:
                renderRow((DDbIogRow) value);
                break;
            case DModSysConsts.PARAM_OBJ_DPS_ROW:
                renderRow((DDbDpsRow) value);
                break;
            case DModSysConsts.PARAM_OBJ_DPS_ROW_ADJ:
                renderRow((DRowDpsRowAdjusted) value);
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
    }

    @Override
    public Object getValue(final int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
