/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DDialogNoteShow.java
 *
 * Created on 7/11/2011, 07:51:51 PM
 */

package sba.mod.trn.form;

import sba.lib.DLibConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanFormDialog;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.trn.db.DDbDpsRow;
import sba.mod.trn.db.DDbDpsRowNote;
import sba.mod.trn.db.DDbIogRow;
import sba.mod.trn.db.DDbIogRowNote;

/**
 *
 * @author Sergio Flores
 */
public class DDialogNoteShow extends DBeanFormDialog {

    /** Creates new form DDialogNoteShow
     * @param client GUI client.
     */
    public DDialogNoteShow(DGuiClient client) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, DModConsts.T_DPS_ROW_NOT, DLibConsts.UNDEFINED, "Notas");
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
        jspNotes = new javax.swing.JScrollPane();
        jtaNotes = new javax.swing.JTextArea();

        jspNotes.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        jtaNotes.setEditable(false);
        jtaNotes.setBackground(java.awt.SystemColor.control);
        jtaNotes.setColumns(20);
        jtaNotes.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        jtaNotes.setLineWrap(true);
        jspNotes.setViewportView(jtaNotes);

        getContentPane().add(jspNotes, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsCustom() {
        DGuiUtils.setWindowBounds(this, 640, 400);

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
        String notes = "";

        if (source != null) {
            if (source instanceof DDbIogRow) {
                for (DDbIogRowNote note : ((DDbIogRow) source).getChildRowNotes()) {
                    notes += (notes.isEmpty() ? "" : "\n") + note.getText() + 
                            (!note.isPrintable() ? "" : " (Imprimir)");
                }
            }
            else if (source instanceof DDbDpsRow) {
                for (DDbDpsRowNote note : ((DDbDpsRow) source).getChildRowNotes()) {
                    notes += (notes.isEmpty() ? "" : "\n") + note.getText() +
                            (!note.isPrintable() ? "" : " (Imprimir)") +
                            (!note.isDfr()? "" : " (CFDI)");
                }
            }
        }

        jtaNotes.setText(notes);
        jtaNotes.setCaretPosition(0);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup jButtonGroup;
    private javax.swing.JScrollPane jspNotes;
    private javax.swing.JTextArea jtaNotes;
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
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }
    }

    @Override
    public Object getValue(final int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
