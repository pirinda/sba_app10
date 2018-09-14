/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DDialogSelectBranchEntity.java
 *
 * Created on 30/12/2011, 11:58:52 AM
 */

package sba.mod.cfg.form;

import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.border.TitledBorder;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiItem;
import sba.lib.gui.DGuiUtils;
import sba.mod.DModConsts;
import sba.mod.bpr.db.DDbBranch;
import sba.mod.cfg.db.DDbBranchCash;
import sba.mod.cfg.db.DDbBranchWarehouse;
import sba.mod.cfg.db.DDbUser;

/**
 *
 * @author Sergio Flores
 */
public class DDialogSelectBranchEntity extends javax.swing.JDialog {

    private DGuiClient miClient;
    private int mnType;
    private int mnFormResult;
    private boolean mbFirstActivation;

    /** Creates new form DDialogSelectBranchEntity */
    public DDialogSelectBranchEntity(DGuiClient client, int type) {
        super(client.getFrame(), true);
        miClient = client;
        mnType = type;
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

        jpEntities = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jlCompany = new javax.swing.JLabel();
        jtfCompany = new javax.swing.JTextField();
        jckBranchAll = new javax.swing.JCheckBox();
        jPanel12 = new javax.swing.JPanel();
        jlBranch = new javax.swing.JLabel();
        jcbBranch = new javax.swing.JComboBox();
        jckBranchEntityAll = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jspBranchEntity = new javax.swing.JScrollPane();
        jltBranchEntity = new javax.swing.JList();
        jpCommands = new javax.swing.JPanel();
        jbOk = new javax.swing.JButton();
        jbCancel = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Seleccionar entidad");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jpEntities.setBorder(javax.swing.BorderFactory.createTitledBorder("Entidades disponibles:"));
        jpEntities.setLayout(new java.awt.BorderLayout(0, 5));

        jPanel1.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCompany.setText("Empresa:");
        jlCompany.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jlCompany);

        jtfCompany.setEditable(false);
        jtfCompany.setToolTipText("Nombre");
        jtfCompany.setFocusable(false);
        jtfCompany.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel11.add(jtfCompany);

        jckBranchAll.setText("Todas las sucursales");
        jckBranchAll.setPreferredSize(new java.awt.Dimension(125, 23));
        jckBranchAll.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jckBranchAllItemStateChanged(evt);
            }
        });
        jPanel11.add(jckBranchAll);

        jPanel1.add(jPanel11);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBranch.setText("Sucursal empresa:");
        jlBranch.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jlBranch);

        jcbBranch.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jcbBranch.setPreferredSize(new java.awt.Dimension(200, 23));
        jcbBranch.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jcbBranchItemStateChanged(evt);
            }
        });
        jcbBranch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jcbBranchKeyPressed(evt);
            }
        });
        jPanel12.add(jcbBranch);

        jckBranchEntityAll.setText("Todas las entidades");
        jckBranchEntityAll.setPreferredSize(new java.awt.Dimension(125, 23));
        jckBranchEntityAll.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jckBranchEntityAllItemStateChanged(evt);
            }
        });
        jPanel12.add(jckBranchEntityAll);

        jPanel1.add(jPanel12);

        jpEntities.add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jltBranchEntity.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jltBranchEntity.setToolTipText(DGuiConsts.MSG_OPTION_DBL_CLICK);
        jltBranchEntity.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jltBranchEntityMouseClicked(evt);
            }
        });
        jltBranchEntity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jltBranchEntityKeyPressed(evt);
            }
        });
        jspBranchEntity.setViewportView(jltBranchEntity);

        jPanel2.add(jspBranchEntity, java.awt.BorderLayout.CENTER);

        jpEntities.add(jPanel2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpEntities, java.awt.BorderLayout.CENTER);

        jpCommands.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbOk.setText("Aceptar");
        jbOk.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbOk.setPreferredSize(new java.awt.Dimension(75, 23));
        jbOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbOkActionPerformed(evt);
            }
        });
        jpCommands.add(jbOk);

        jbCancel.setText("Cancelar");
        jbCancel.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbCancel.setPreferredSize(new java.awt.Dimension(75, 23));
        jbCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelActionPerformed(evt);
            }
        });
        jpCommands.add(jbCancel);

        getContentPane().add(jpCommands, java.awt.BorderLayout.PAGE_END);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-488)/2, (screenSize.height-334)/2, 488, 334);
    }// </editor-fold>//GEN-END:initComponents

    private void jbCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCancelActionPerformed
        actionCancel();
    }//GEN-LAST:event_jbCancelActionPerformed

    private void jltBranchEntityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jltBranchEntityKeyPressed
        keyPressedBranchEntity(evt);
    }//GEN-LAST:event_jltBranchEntityKeyPressed

    private void jltBranchEntityMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jltBranchEntityMouseClicked
        mouseClickedEntity(evt);
    }//GEN-LAST:event_jltBranchEntityMouseClicked

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void jbOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbOkActionPerformed
        actionOk();
    }//GEN-LAST:event_jbOkActionPerformed

    private void jcbBranchItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jcbBranchItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            itemStateBranch();
        }
    }//GEN-LAST:event_jcbBranchItemStateChanged

    private void jcbBranchKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jcbBranchKeyPressed
        keyPressedBranch(evt);
    }//GEN-LAST:event_jcbBranchKeyPressed

    private void jckBranchAllItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jckBranchAllItemStateChanged
        itemStateBranchAll();
    }//GEN-LAST:event_jckBranchAllItemStateChanged

    private void jckBranchEntityAllItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jckBranchEntityAllItemStateChanged
        itemStateBranchEntityAll();
    }//GEN-LAST:event_jckBranchEntityAllItemStateChanged

    private void initComponentsCustom() {
        DDbUser user = (DDbUser) miClient.getSession().getUser();

        switch (mnType) {
            case DModConsts.CU_CSH:
                setTitle(DUtilConsts.TXT_SELECT + " " + DUtilConsts.TXT_BRANCH_CSH.toLowerCase());
                ((TitledBorder) jpEntities.getBorder()).setTitle(DUtilConsts.TXT_BRANCH_CSH_PLR);
                break;
            case DModConsts.CU_WAH:
                setTitle(DUtilConsts.TXT_SELECT + " " + DUtilConsts.TXT_BRANCH_WAH.toLowerCase());
                ((TitledBorder) jpEntities.getBorder()).setTitle(DUtilConsts.TXT_BRANCH_WAH_PLR);
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        jtfCompany.setText((String) miClient.getSession().readField(DModConsts.BU_BPR, new int[] { DUtilConsts.BPR_CO_ID }, DDbRegistry.FIELD_NAME));
        jtfCompany.setCaretPosition(0);

        jckBranchAll.setEnabled(user.hasUniversalAccess());

        jcbBranch.removeAllItems();
        jcbBranch.addItem(new DGuiItem("- " + DUtilConsts.TXT_BRANCH + " -"));

        for (DDbBranch branch : user.getAuxBranches()) {
            jcbBranch.addItem(new DGuiItem(branch.getPrimaryKey(), branch.getName()));
        }

        jcbBranch.setSelectedIndex(0);
    }

    private void windowActivated() {
        if (mbFirstActivation) {
            mbFirstActivation = false;
            jcbBranch.requestFocus();
        }
    }

    private void itemStateBranch() {
        int[] branchKey = null;
        Vector<DGuiItem> items = new Vector<>();
        DDbUser user = (DDbUser) miClient.getSession().getUser();

        if (jcbBranch.getSelectedIndex() <= 0) {
            jckBranchEntityAll.setEnabled(false);
            jckBranchEntityAll.setSelected(false);

            jltBranchEntity.setEnabled(false);
            jltBranchEntity.setListData(items);
        }
        else {
            branchKey = ((DGuiItem) jcbBranch.getSelectedItem()).getPrimaryKey();

            switch (mnType) {
                case DModConsts.CU_CSH:
                    jckBranchEntityAll.setEnabled(user.hasUniversalAccess() || user.hasUniversalAccessToBranchCashes(branchKey));
                    for (DDbBranchCash entity : user.getAuxBranchCashes(branchKey)) {
                        items.add(new DGuiItem(entity.getPrimaryKey(), entity.getName()));
                    }
                    break;
                case DModConsts.CU_WAH:
                    jckBranchEntityAll.setEnabled(user.hasUniversalAccess() || user.hasUniversalAccessToBranchWarehouses(branchKey));
                    for (DDbBranchWarehouse entity : user.getAuxBranchWarehouses(branchKey)) {
                        items.add(new DGuiItem(entity.getPrimaryKey(), entity.getName()));
                    }
                    break;
                default:
            }

            jltBranchEntity.setEnabled(true);
            jltBranchEntity.setListData(items);
        }
    }

    private void itemStateBranchAll() {
        if (!jckBranchAll.isSelected()) {
            jcbBranch.setEnabled(true);
        }
        else {
            if (jcbBranch.getSelectedIndex() > 0) {
                jcbBranch.setSelectedIndex(0);
            }

            jcbBranch.setEnabled(false);
        }
    }

    private void itemStateBranchEntityAll() {
        if (!jckBranchEntityAll.isSelected()) {
            jltBranchEntity.setEnabled(true);
        }
        else {
            jltBranchEntity.setEnabled(false);
            jltBranchEntity.clearSelection();
        }
    }

    private void actionOk() {
        if (jcbBranch.getSelectedIndex() <= 0 && !jckBranchAll.isSelected()) {
            miClient.showMsgBoxWarning(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + DGuiUtils.getLabelName(jlBranch.getText()) + "'.");
            jcbBranch.requestFocus();
        }
        else if (jltBranchEntity.getSelectedValue() == null && !jckBranchAll.isSelected() && !jckBranchEntityAll.isSelected()) {
            miClient.showMsgBoxWarning(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + DGuiUtils.getLabelName(((TitledBorder) jpEntities.getBorder()).getTitle()) + "'.");
            jltBranchEntity.requestFocus();
        }
        else {
            mnFormResult = DGuiConsts.FORM_RESULT_OK;
            dispose();
        }
    }

    private void actionCancel() {
        mnFormResult = DGuiConsts.FORM_RESULT_CANCEL;
        dispose();
    }

    private void keyPressedBranch(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jltBranchEntity.requestFocus();
        }
    }

    private void keyPressedBranchEntity(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jbOk.requestFocus();
        }
    }

    private void mouseClickedEntity(MouseEvent evt) {
        if (evt.getClickCount() == 2) {
            actionOk();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbCancel;
    private javax.swing.JButton jbOk;
    private javax.swing.JComboBox jcbBranch;
    private javax.swing.JCheckBox jckBranchAll;
    private javax.swing.JCheckBox jckBranchEntityAll;
    private javax.swing.JLabel jlBranch;
    private javax.swing.JLabel jlCompany;
    private javax.swing.JList jltBranchEntity;
    private javax.swing.JPanel jpCommands;
    private javax.swing.JPanel jpEntities;
    private javax.swing.JScrollPane jspBranchEntity;
    private javax.swing.JTextField jtfCompany;
    // End of variables declaration//GEN-END:variables

    public void resetDialog() {
        mnFormResult = DLibConsts.UNDEFINED;
        mbFirstActivation = true;

        setSelectionKey(null);
    }

    public void setSelectionKey(final int[] key) {
        if (jckBranchEntityAll.isSelected()) {
            jckBranchEntityAll.setSelected(false);
        }

        if (jckBranchAll.isSelected()) {
            jckBranchAll.setSelected(false);
        }

        if (jcbBranch.getSelectedIndex() > 0) {
            jcbBranch.setSelectedIndex(0);
        }

        if (key != null) {
            if (key[1] == DLibConsts.UNDEFINED) {
                jckBranchAll.setSelected(true);
            }
            else {
                DGuiUtils.locateItem(jcbBranch, new int[] { key[0], key[1] });

                if (key[2] == DLibConsts.UNDEFINED) {
                    jckBranchEntityAll.setSelected(true);
                }
                else {
                    DGuiUtils.locateItem(jltBranchEntity, key);
                }
            }
        }
    }

    public int[] getSelectionKey() {
        int[] key = null;

        if (jltBranchEntity.getSelectedValue() != null) {
            key = ((DGuiItem) jltBranchEntity.getSelectedValue()).getPrimaryKey();
        }
        else {
            key = new int[3];

            if (jcbBranch.getSelectedIndex() > 0) {
                key[0] = ((DGuiItem) jcbBranch.getSelectedItem()).getPrimaryKey()[0];
                key[1] = ((DGuiItem) jcbBranch.getSelectedItem()).getPrimaryKey()[1];
            }
        }

        return key;
    }

    public int getFormResult() {
        return mnFormResult;
    }
}
