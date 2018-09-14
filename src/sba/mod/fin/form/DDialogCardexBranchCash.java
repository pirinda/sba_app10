/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DDialogCardexBranchCash.java
 *
 * Created on 20/12/2011, 04:23:05 PM
 */

package sba.mod.fin.form;

import java.awt.BorderLayout;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.grid.DGridColumnForm;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridPaneForm;
import sba.lib.grid.DGridRow;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiParams;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanFormDialog;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.fin.db.DRowCardexBookkeeping;

/**
 *
 * @author Sergio Flores
 */
public class DDialogCardexBranchCash extends DBeanFormDialog {

    private int mnModality;
    private int mnYear;
    private Date mtDateCutOff;
    private int[] manBranchKey;
    private int[] manBranchCashKey;
    private int mnCurrency;
    private DGridPaneForm moGridMoves;

    /** Creates new form DDialogCardexBranchCash
     * @param client GUI client.
     * @param modality Balance modality. Constants defined in DUtilConsts (PER_...).
     */
    public DDialogCardexBranchCash(DGuiClient client, int modality) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, DModConsts.FX_BAL_CSH, DLibConsts.UNDEFINED, "Tarjeta auxiliar");
        mnModality = modality;
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

        jPanel7 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlBranch = new javax.swing.JLabel();
        jtfBranch = new javax.swing.JTextField();
        jtfBranchCode = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jlBranchCash = new javax.swing.JLabel();
        jtfBranchCash = new javax.swing.JTextField();
        jtfBranchCashCode = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jlCurrency = new javax.swing.JLabel();
        jtfCurrency = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jlDateCutOff = new javax.swing.JLabel();
        jtfDateCutOff = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jlBalanceStc = new javax.swing.JLabel();
        moCurBalanceStc = new sba.lib.gui.bean.DBeanCompoundFieldCurrency();
        jPanel10 = new javax.swing.JPanel();
        jlBalance = new javax.swing.JLabel();
        moCurBalance = new sba.lib.gui.bean.DBeanCompoundFieldCurrency();
        jpMoves = new javax.swing.JPanel();

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de la cuenta:"));
        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBranch.setText("Sucursal empresa:");
        jlBranch.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlBranch);

        jtfBranch.setEditable(false);
        jtfBranch.setText("TEXT");
        jtfBranch.setToolTipText("Nombre");
        jtfBranch.setFocusable(false);
        jtfBranch.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel3.add(jtfBranch);

        jtfBranchCode.setEditable(false);
        jtfBranchCode.setText("TEXT");
        jtfBranchCode.setToolTipText("Código");
        jtfBranchCode.setFocusable(false);
        jtfBranchCode.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel3.add(jtfBranchCode);

        jPanel1.add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBranchCash.setText("Cuenta dinero:");
        jlBranchCash.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlBranchCash);

        jtfBranchCash.setEditable(false);
        jtfBranchCash.setText("TEXT");
        jtfBranchCash.setFocusable(false);
        jtfBranchCash.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel4.add(jtfBranchCash);

        jtfBranchCashCode.setEditable(false);
        jtfBranchCashCode.setText("TEXT");
        jtfBranchCashCode.setToolTipText("Código");
        jtfBranchCashCode.setFocusable(false);
        jtfBranchCashCode.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel4.add(jtfBranchCashCode);

        jPanel1.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCurrency.setFont(new java.awt.Font("Tahoma", 1, 11));
        jlCurrency.setText("Moneda:");
        jlCurrency.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlCurrency);

        jtfCurrency.setEditable(false);
        jtfCurrency.setFont(new java.awt.Font("Tahoma", 1, 11));
        jtfCurrency.setText("TEXT");
        jtfCurrency.setFocusable(false);
        jtfCurrency.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel5.add(jtfCurrency);

        jPanel1.add(jPanel5);

        jPanel7.add(jPanel1, java.awt.BorderLayout.WEST);

        jPanel6.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateCutOff.setText("Fecha corte:");
        jlDateCutOff.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel8.add(jlDateCutOff);

        jtfDateCutOff.setEditable(false);
        jtfDateCutOff.setText("00/00/0000");
        jtfDateCutOff.setFocusable(false);
        jtfDateCutOff.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel8.add(jtfDateCutOff);

        jPanel6.add(jPanel8);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBalanceStc.setText("Saldo SBC:");
        jlBalanceStc.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel9.add(jlBalanceStc);
        jPanel9.add(moCurBalanceStc);

        jPanel6.add(jPanel9);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBalance.setText("Saldo:");
        jlBalance.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel10.add(jlBalance);
        jPanel10.add(moCurBalance);

        jPanel6.add(jPanel10);

        jPanel7.add(jPanel6, java.awt.BorderLayout.EAST);

        getContentPane().add(jPanel7, java.awt.BorderLayout.NORTH);

        jpMoves.setBorder(javax.swing.BorderFactory.createTitledBorder("Movimientos contables:"));
        jpMoves.setLayout(new java.awt.BorderLayout());
        getContentPane().add(jpMoves, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsCustom() {
        DGuiUtils.setWindowBounds(this, 960, 600);

        moCurBalanceStc.setCompoundFieldSettings(miClient);
        moCurBalance.setCompoundFieldSettings(miClient);
        moCurBalanceStc.getField().setDecimalSettings(DGuiUtils.getLabelName(jlBalanceStc.getText()), DGuiConsts.GUI_TYPE_DEC_AMT, false);
        moCurBalance.getField().setDecimalSettings(DGuiUtils.getLabelName(jlBalanceStc.getText()), DGuiConsts.GUI_TYPE_DEC_AMT, false);
        moCurBalanceStc.getField().setEditable(false);
        moCurBalance.getField().setEditable(false);

        jbSave.setEnabled(false);
        jbSave.setText(DGuiConsts.TXT_BTN_OK);
        jbCancel.setText(DGuiConsts.TXT_BTN_CLOSE);

        moGridMoves = new DGridPaneForm(miClient, mnFormType, mnFormSubtype, msTitle, new DGuiParams(mnModality)) {
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }

            @Override
            public void createGridColumns() {
                int col = 0;
                DGridColumnForm[] columns = new DGridColumnForm[10];

                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_INT_2B, "#");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DATE, DGridConsts.COL_TITLE_DATE);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "Referencia");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "Concepto");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "Movimiento", 125);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_BOOL_S, "Disponible");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Debe $");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Haber $");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Saldo SBC $");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Saldo $");

                for (col = 0; col < columns.length; col++) {
                    moModel.getGridColumns().add(columns[col]);
                }
            }
        };

        jpMoves.add(moGridMoves, BorderLayout.CENTER);

        mvFormGrids.add(moGridMoves);
    }

    @Override
    protected void windowActivated() {
        if (mbFirstActivation) {
            mbFirstActivation = false;
            jbCancel.requestFocus();
        }
    }

    private void renderDateCutOff() {
        if (mtDateCutOff == null) {
            mnYear = 0;
            jtfDateCutOff.setText("");
        }
        else {
            mnYear = DLibTimeUtils.digestYear(mtDateCutOff)[0];
            jtfDateCutOff.setText(DLibUtils.DateFormatDate.format(mtDateCutOff));
        }
    }

    private void renderBranch() {
        if (manBranchKey == null) {
            jtfBranch.setText(DUtilConsts.NON_APPLYING);
            jtfBranchCode.setText("");
        }
        else {
            jtfBranch.setText((String) miClient.getSession().readField(DModConsts.BU_BRA, manBranchKey, DDbRegistry.FIELD_NAME));
            jtfBranch.setCaretPosition(0);
            jtfBranchCode.setText((String) miClient.getSession().readField(DModConsts.BU_BRA, manBranchKey, DDbRegistry.FIELD_CODE));
            jtfBranchCode.setCaretPosition(0);
        }
    }

    private void renderBranchCash() {
        if (manBranchCashKey == null) {
            manBranchKey = null;

            jtfBranchCash.setText(DUtilConsts.NON_APPLYING);
            jtfBranchCashCode.setText("");
        }
        else {
            manBranchKey = new int[] { manBranchCashKey[0], manBranchCashKey[1] };

            jtfBranchCash.setText((String) miClient.getSession().readField(DModConsts.CU_CSH, manBranchCashKey, DDbRegistry.FIELD_NAME));
            jtfBranchCash.setCaretPosition(0);
            jtfBranchCashCode.setText((String) miClient.getSession().readField(DModConsts.CU_CSH, manBranchCashKey, DDbRegistry.FIELD_CODE));
            jtfBranchCashCode.setCaretPosition(0);
        }

        renderBranch();
    }

    private void renderCurrency() {
        if (mnCurrency == DLibConsts.UNDEFINED) {
            jtfCurrency.setText(DUtilConsts.NON_APPLYING);

            moCurBalanceStc.setCurrencyKey(miClient.getSession().getSessionCustom().getLocalCurrencyKey());
            moCurBalance.setCurrencyKey(miClient.getSession().getSessionCustom().getLocalCurrencyKey());
        }
        else {
            jtfCurrency.setText(miClient.getSession().getSessionCustom().getCurrency(new int[] { mnCurrency }));
            jtfCurrency.setCaretPosition(0);

            moCurBalanceStc.setCurrencyKey(new int[] { mnCurrency });
            moCurBalance.setCurrencyKey(new int[] { mnCurrency });
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel jlBalance;
    private javax.swing.JLabel jlBalanceStc;
    private javax.swing.JLabel jlBranch;
    private javax.swing.JLabel jlBranchCash;
    private javax.swing.JLabel jlCurrency;
    private javax.swing.JLabel jlDateCutOff;
    private javax.swing.JPanel jpMoves;
    private javax.swing.JTextField jtfBranch;
    private javax.swing.JTextField jtfBranchCash;
    private javax.swing.JTextField jtfBranchCashCode;
    private javax.swing.JTextField jtfBranchCode;
    private javax.swing.JTextField jtfCurrency;
    private javax.swing.JTextField jtfDateCutOff;
    private sba.lib.gui.bean.DBeanCompoundFieldCurrency moCurBalance;
    private sba.lib.gui.bean.DBeanCompoundFieldCurrency moCurBalanceStc;
    // End of variables declaration//GEN-END:variables

    /*
     * Public methods
     */

    public void populateCardex() {
        int move = 0;
        double balSbt = 0;
        double bal = 0;
        String sql = "";
        ResultSet resultSet = null;
        DRowCardexBookkeeping cardex = null;
        Vector<DGridRow> gridRows = new Vector<>();

        try {
            // Opening balance:

            sql = "SELECT " + (mnCurrency == DLibConsts.UNDEFINED ?
                    "SUM(bkk.dbt - bkk.cdt) AS f_bal_stc, SUM(IF(bkk.b_avl = 0, 0, bkk.dbt - bkk.cdt)) AS f_bal " :
                    "SUM(bkk.dbt_cy - bkk.cdt_cy) AS f_bal_stc, SUM(IF(bkk.b_avl = 0, 0, bkk.dbt_cy - bkk.cdt_cy)) AS f_bal ") +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " AS bkk " +
                    "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.FS_SYS_MOV_TP) + " AS mt ON " +
                    "bkk.fk_sys_mov_cl = mt.id_sys_mov_cl AND bkk.fk_sys_mov_tp = mt.id_sys_mov_tp AND " +
                    "bkk.b_del = 0 AND bkk.id_yer = " + mnYear + " AND " +
                    "bkk.dt < '" + DLibUtils.DbmsDateFormatDate.format(DLibTimeUtils.getBeginOfYear(mtDateCutOff)) + "' AND " +
                    "bkk.fk_sys_acc_tp = " + DModSysConsts.FS_SYS_ACC_TP_ENT_CSH + " AND " +
                    (manBranchCashKey == null ?
                        "bkk.fk_own_bpr = " + manBranchKey[0] + " AND bkk.fk_own_bra = " + manBranchKey[1] + " " :
                        "bkk.fk_csh_bpr_n = " + manBranchCashKey[0] + " AND bkk.fk_csh_bra_n = " + manBranchCashKey[1] + " AND bkk.fk_csh_csh_n = " + manBranchCashKey[2] + " ") +
                    (mnCurrency == DLibConsts.UNDEFINED ? "" : "AND bkk.fk_cur = " + mnCurrency + " ");
            resultSet = miClient.getSession().getStatement().executeQuery(sql);
            if (resultSet.next()) {
                cardex = new DRowCardexBookkeeping();

                cardex.setMove(move++);
                cardex.setDate(DLibTimeUtils.getBeginOfYear(mtDateCutOff));
                cardex.setReference("");
                cardex.setText("");
                cardex.setSystemMoveType(DUtilConsts.OPEN_BALANCE);
                cardex.setAvailable(false);
                cardex.setDebit(0);
                cardex.setCredit(0);

                balSbt += resultSet.getDouble("f_bal_stc");
                bal += resultSet.getDouble("f_bal");

                cardex.setBalanceStc(balSbt);
                cardex.setBalance(bal);

                gridRows.add(cardex);
            }

            // Accounting moves:

            sql = "SELECT bkk.id_yer, bkk.id_mov, " +
                    "bkk.dt, bkk.ref, bkk.txt, " +
                    (mnCurrency == DLibConsts.UNDEFINED ? "bkk.dbt AS f_dbt, bkk.cdt AS f_cdt, " : "bkk.dbt_cy AS f_dbt, bkk.cdt_cy AS f_cdt, ") +
                    "bkk.b_avl, mt.name " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " AS bkk " +
                    "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.FS_SYS_MOV_TP) + " AS mt ON " +
                    "bkk.fk_sys_mov_cl = mt.id_sys_mov_cl AND bkk.fk_sys_mov_tp = mt.id_sys_mov_tp AND " +
                    "bkk.b_del = 0 AND bkk.id_yer = " + mnYear + " AND " +
                    "bkk.dt BETWEEN '" + DLibUtils.DbmsDateFormatDate.format(DLibTimeUtils.getBeginOfYear(mtDateCutOff)) + "' AND '" + DLibUtils.DbmsDateFormatDate.format(mtDateCutOff) + "' AND " +
                    "bkk.fk_sys_acc_tp = " + DModSysConsts.FS_SYS_ACC_TP_ENT_CSH + " AND " +
                    (manBranchCashKey == null ?
                        "bkk.fk_own_bpr = " + manBranchKey[0] + " AND bkk.fk_own_bra = " + manBranchKey[1] + " " :
                        "bkk.fk_csh_bpr_n = " + manBranchCashKey[0] + " AND bkk.fk_csh_bra_n = " + manBranchCashKey[1] + " AND bkk.fk_csh_csh_n = " + manBranchCashKey[2] + " ") +
                    (mnCurrency == DLibConsts.UNDEFINED ? "" : "AND bkk.fk_cur = " + mnCurrency + " ") +
                    "ORDER BY bkk.dt, bkk.id_yer, bkk.id_mov ";
            resultSet = miClient.getSession().getStatement().executeQuery(sql);
            while (resultSet.next()) {
                cardex = new DRowCardexBookkeeping();

                cardex.setMove(move++);
                cardex.setDate(resultSet.getDate("bkk.dt"));
                cardex.setReference(resultSet.getString("bkk.ref"));
                cardex.setText(resultSet.getString("bkk.txt"));
                cardex.setSystemMoveType(resultSet.getString("mt.name"));
                cardex.setAvailable(resultSet.getBoolean("bkk.b_avl"));
                cardex.setDebit(resultSet.getDouble("f_dbt"));
                cardex.setCredit(resultSet.getDouble("f_cdt"));

                if (mnFormSubtype == DModSysConsts.BS_BPR_CL_VEN) {
                    balSbt += resultSet.getDouble("f_cdt") - resultSet.getDouble("f_dbt");
                    if (resultSet.getBoolean("bkk.b_avl")) {
                        bal += resultSet.getDouble("f_cdt") - resultSet.getDouble("f_dbt");
                    }
                }
                else {
                    balSbt += resultSet.getDouble("f_dbt") - resultSet.getDouble("f_cdt");
                    if (resultSet.getBoolean("bkk.b_avl")) {
                        bal += resultSet.getDouble("f_dbt") - resultSet.getDouble("f_cdt");
                    }
                }

                cardex.setBalanceStc(balSbt);
                cardex.setBalance(bal);

                gridRows.add(cardex);
            }

            moGridMoves.populateGrid(gridRows);

            moCurBalanceStc.getField().setValue(balSbt);
            moCurBalance.getField().setValue(bal);
        }
        catch (SQLException e) {
            DLibUtils.showException(this, e);
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }

    /*
     * Overrided methods
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
    public void setValue(final int type, final Object value) {
        switch (type) {
            case DModSysConsts.PARAM_DATE:
                mtDateCutOff = (Date) value;
                renderDateCutOff();
                break;
            case DModSysConsts.PARAM_BPR_BRA:
                manBranchKey = (int[]) value;
                renderBranch();
                break;
            case DModSysConsts.PARAM_BRA_CSH:
                manBranchCashKey = (int[]) value;
                renderBranchCash();
                break;
            case DModSysConsts.PARAM_CUR:
                mnCurrency = (Integer) value;
                renderCurrency();
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
    }

    @Override
    public Object getValue(final int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void resetForm() {
        mnFormResult = DLibConsts.UNDEFINED;
        mbFirstActivation = true;

        mnYear = 0;
        mtDateCutOff = null;
        manBranchKey = null;
        manBranchCashKey = null;
        mnCurrency = DLibConsts.UNDEFINED;

        moCurBalanceStc.getField().resetField();
        moCurBalance.getField().resetField();

        moGridMoves.clearGridRows();

        renderDateCutOff();
        renderBranch();
        renderBranchCash();
        renderCurrency();
    }
}
