/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.trn.form;

import java.util.ArrayList;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanFieldText;
import sba.lib.gui.bean.DBeanFormDialog;
import sba.mod.DModSysConsts;
import sba.mod.bpr.db.DBprEmail;
import sba.mod.bpr.db.DBprUtils;
import sba.mod.bpr.db.DDbBranchAddress;

/**
 *
 * @author Sergio Flores
 */
public class DDialogEmailSending extends DBeanFormDialog {

    private final int mnBizPartnerClass;
    
    /**
     * Creates new form DDialogEmailSending
     * @param client GUI client.
     * @param bizPartnerClass
     */
    public DDialogEmailSending(DGuiClient client, int bizPartnerClass) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, DLibConsts.UNDEFINED, DLibConsts.UNDEFINED, "Envío vía correo-e");
        mnBizPartnerClass = bizPartnerClass;
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

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jlBizPartnerClass = new javax.swing.JLabel();
        jtfBizPartner = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jlDocument = new javax.swing.JLabel();
        jtfDocument = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jlDummy = new javax.swing.JLabel();
        jlContact = new javax.swing.JLabel();
        jlEmail = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jlContact1 = new javax.swing.JLabel();
        moTextContact1 = new sba.lib.gui.bean.DBeanFieldText();
        moTextEmail1 = new sba.lib.gui.bean.DBeanFieldText();
        jPanel4 = new javax.swing.JPanel();
        jlContact2 = new javax.swing.JLabel();
        moTextContact2 = new sba.lib.gui.bean.DBeanFieldText();
        moTextEmail2 = new sba.lib.gui.bean.DBeanFieldText();
        jPanel5 = new javax.swing.JPanel();
        jlContact3 = new javax.swing.JLabel();
        moTextContact3 = new sba.lib.gui.bean.DBeanFieldText();
        moTextEmail3 = new sba.lib.gui.bean.DBeanFieldText();
        moBoolPreserveEmails = new sba.lib.gui.bean.DBeanFieldBoolean();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Enviar el documento a:"));
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new java.awt.GridLayout(7, 1, 0, 5));

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerClass.setText("[Asoc. neg.:]");
        jlBizPartnerClass.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel7.add(jlBizPartnerClass);

        jtfBizPartner.setEditable(false);
        jtfBizPartner.setFocusable(false);
        jtfBizPartner.setPreferredSize(new java.awt.Dimension(400, 23));
        jPanel7.add(jtfBizPartner);

        jPanel2.add(jPanel7);

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDocument.setText("[Document:]");
        jlDocument.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel8.add(jlDocument);

        jtfDocument.setEditable(false);
        jtfDocument.setFocusable(false);
        jtfDocument.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel8.add(jtfDocument);

        jPanel2.add(jPanel8);

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDummy.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel6.add(jlDummy);

        jlContact.setText("Nombre contacto:");
        jlContact.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel6.add(jlContact);

        jlEmail.setForeground(new java.awt.Color(0, 153, 153));
        jlEmail.setText("Correo-e contacto:");
        jlEmail.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel6.add(jlEmail);

        jPanel2.add(jPanel6);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlContact1.setText("Contacto 1:");
        jlContact1.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel3.add(jlContact1);

        moTextContact1.setText("dBeanFieldText1");
        moTextContact1.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel3.add(moTextContact1);

        moTextEmail1.setText("dBeanFieldText2");
        moTextEmail1.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel3.add(moTextEmail1);

        jPanel2.add(jPanel3);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlContact2.setText("Contacto 2:");
        jlContact2.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel4.add(jlContact2);

        moTextContact2.setText("dBeanFieldText1");
        moTextContact2.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel4.add(moTextContact2);

        moTextEmail2.setText("dBeanFieldText2");
        moTextEmail2.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel4.add(moTextEmail2);

        jPanel2.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlContact3.setText("Contacto 3:");
        jlContact3.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel5.add(jlContact3);

        moTextContact3.setText("dBeanFieldText1");
        moTextContact3.setPreferredSize(new java.awt.Dimension(225, 23));
        jPanel5.add(moTextContact3);

        moTextEmail3.setText("dBeanFieldText2");
        moTextEmail3.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel5.add(moTextEmail3);

        jPanel2.add(jPanel5);

        moBoolPreserveEmails.setText("Preservar contactos y correos-e para futuros envíos");
        jPanel2.add(moBoolPreserveEmails);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JLabel jlBizPartnerClass;
    private javax.swing.JLabel jlContact;
    private javax.swing.JLabel jlContact1;
    private javax.swing.JLabel jlContact2;
    private javax.swing.JLabel jlContact3;
    private javax.swing.JLabel jlDocument;
    private javax.swing.JLabel jlDummy;
    private javax.swing.JLabel jlEmail;
    private javax.swing.JTextField jtfBizPartner;
    private javax.swing.JTextField jtfDocument;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolPreserveEmails;
    private sba.lib.gui.bean.DBeanFieldText moTextContact1;
    private sba.lib.gui.bean.DBeanFieldText moTextContact2;
    private sba.lib.gui.bean.DBeanFieldText moTextContact3;
    private sba.lib.gui.bean.DBeanFieldText moTextEmail1;
    private sba.lib.gui.bean.DBeanFieldText moTextEmail2;
    private sba.lib.gui.bean.DBeanFieldText moTextEmail3;
    // End of variables declaration//GEN-END:variables

    private void initComponentsCustom() {
        DGuiUtils.setWindowBounds(this, 600, 340);
        
        moTextContact1.setTextSettings(DGuiUtils.getLabelName(jlContact) + " 1", 50, 0);
        moTextEmail1.setTextSettings(DGuiUtils.getLabelName(jlEmail) + " 1", 50);
        moTextEmail1.setTextCaseType(0);
        moTextContact2.setTextSettings(DGuiUtils.getLabelName(jlContact) + " 2", 50, 0);
        moTextEmail2.setTextSettings(DGuiUtils.getLabelName(jlEmail) + " 2", 50, 0);
        moTextEmail2.setTextCaseType(0);
        moTextContact3.setTextSettings(DGuiUtils.getLabelName(jlContact) + " 3", 50, 0);
        moTextEmail3.setTextSettings(DGuiUtils.getLabelName(jlEmail) + " 3", 50, 0);
        moTextEmail3.setTextCaseType(0);
        
        moFields.addField(moTextContact1);
        moFields.addField(moTextEmail1);
        moFields.addField(moTextContact2);
        moFields.addField(moTextEmail2);
        moFields.addField(moTextContact3);
        moFields.addField(moTextEmail3);

        jbSave.setText(DUtilConsts.TXT_SEND);
        moFields.setFormButton(jbSave);
        
        jlBizPartnerClass.setText(DBprUtils.getBizPartnerClassNameSng(mnBizPartnerClass) + ":");
    }
    
    public void setBizPartner(final String bizPartner) {
        jtfBizPartner.setText(bizPartner);
        jtfBizPartner.setCaretPosition(0);
    }
    
    public void setDocument(final String label, final String document) {
        jlDocument.setText(label + ":");
        jtfDocument.setText(document);
        jtfDocument.setCaretPosition(0);
    }
    
    /**
     * Set contacts and e-mails as an array of individual arrays of strings of length 1 (e-mail) or length 2 (contact name & e-mail).
     * @param bprEmails 
     */
    public void setEmails(final ArrayList<DBprEmail> bprEmails) {
        moFields.resetFields();
        DBeanFieldText[] textContacts = new DBeanFieldText[] { moTextContact1, moTextContact2, moTextContact3 };
        DBeanFieldText[] textMails = new DBeanFieldText[] { moTextEmail1, moTextEmail2, moTextEmail3 };
        
        for (int index = 0; index < bprEmails.size() && index < 3; index++) {
            DBprEmail bprEmail = bprEmails.get(index);
            
            textContacts[index].setValue(bprEmail.Contact);
            textMails[index].setValue(bprEmail.Email);
        }
    }
    
    /**
     * Get array of e-mails in format 'box@mail.com' or '"User Name" <box@mail.com>'.
     * @return 
     */
    public ArrayList<DBprEmail> getEmails() {
        ArrayList<DBprEmail> bprEmails = new ArrayList<>();
        
        if (!moTextEmail1.getValue().isEmpty()) {
            bprEmails.add(new DBprEmail(moTextContact1.getValue(), moTextEmail1.getValue()));
        }
        
        if (!moTextEmail2.getValue().isEmpty()) {
            bprEmails.add(new DBprEmail(moTextContact2.getValue(), moTextEmail2.getValue()));
        }
        
        if (!moTextEmail3.getValue().isEmpty()) {
            bprEmails.add(new DBprEmail(moTextContact3.getValue(), moTextEmail3.getValue()));
        }
        
        return bprEmails;
    }
    
    public boolean isPreserveEmailsSelected() {
        return moBoolPreserveEmails.getValue();
    }
    
    public void preserveEmails(final DDbBranchAddress branchAddress) throws Exception {
        boolean save = false;
        if (!moTextEmail1.getValue().isEmpty()) {
            branchAddress.setContact1(moTextContact1.getValue());
            branchAddress.setTelecommElectronic1(moTextEmail1.getValue());
            branchAddress.setFkTelecommElectronic1TypeId(DModSysConsts.BS_TCE_TP_EMA);
            save = true;
        }
        
        if (!moTextEmail2.getValue().isEmpty()) {
            branchAddress.setContact2(moTextContact2.getValue());
            branchAddress.setTelecommElectronic2(moTextEmail2.getValue());
            branchAddress.setFkTelecommElectronic2TypeId(DModSysConsts.BS_TCE_TP_EMA);
            save = true;
        }
        
        if (!moTextEmail3.getValue().isEmpty()) {
            branchAddress.setContact3(moTextContact3.getValue());
            branchAddress.setTelecommElectronic3(moTextEmail3.getValue());
            branchAddress.setFkTelecommElectronic3TypeId(DModSysConsts.BS_TCE_TP_EMA);
            save = true;
        }
        
        if (save) {
            branchAddress.save(miClient.getSession());
        }
    }
    
    @Override
    public void addAllListeners() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeAllListeners() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reloadCatalogues() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRegistry(DDbRegistry registry) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DDbRegistry getRegistry() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DGuiValidation validateForm() {
        DGuiValidation validation = moFields.validateFields();
        
        if (validation.isValid()) {
            if (!moTextContact1.getValue().isEmpty() && moTextEmail1.getValue().isEmpty()) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moTextEmail1.getFieldName() + "'.");
                validation.setComponent(moTextEmail1);
            }
            else if (!moTextContact2.getValue().isEmpty() && moTextEmail2.getValue().isEmpty()) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moTextEmail2.getFieldName() + "'.");
                validation.setComponent(moTextEmail2);
            }
            else if (!moTextContact3.getValue().isEmpty() && moTextEmail3.getValue().isEmpty()) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moTextEmail3.getFieldName() + "'.");
                validation.setComponent(moTextEmail3);
            }
        }
        
        return validation;
    }
}
