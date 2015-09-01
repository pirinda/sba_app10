/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DPanelBranchAddress.java
 *
 * Created on 1/10/2011, 01:54:31 PM
 */

package sba.mod.bpr.form;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanPanel;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.bpr.db.DDbBranchAddress;
import sba.mod.cfg.db.DDbConfigCompany;

/**
 *
 * @author Sergio Flores
 */
public class DPanelBranchAddress extends DBeanPanel implements ActionListener {

    private DDbBranchAddress moRegistry;
    private boolean mbHeadquarters;

    /** Creates new form DPanelBranchAddress */
    public DPanelBranchAddress() {
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

        jpRegistry = new javax.swing.JPanel();
        jpRegistry1 = new javax.swing.JPanel();
        jpRegistry11 = new javax.swing.JPanel();
        jlName = new javax.swing.JLabel();
        moTextName = new sba.lib.gui.bean.DBeanFieldText();
        moBoolDefault = new sba.lib.gui.bean.DBeanFieldBoolean();
        jpRegistry12 = new javax.swing.JPanel();
        jlStreet = new javax.swing.JLabel();
        moTextStreet = new sba.lib.gui.bean.DBeanFieldText();
        moTextNumberExterior = new sba.lib.gui.bean.DBeanFieldText();
        moTextNumberInterior = new sba.lib.gui.bean.DBeanFieldText();
        jpRegistry13 = new javax.swing.JPanel();
        jlNeighborhood = new javax.swing.JLabel();
        moTextNeighborhood = new sba.lib.gui.bean.DBeanFieldText();
        moTextReference = new sba.lib.gui.bean.DBeanFieldText();
        jpRegistry14 = new javax.swing.JPanel();
        jlLocality = new javax.swing.JLabel();
        moTextLocality = new sba.lib.gui.bean.DBeanFieldText();
        moTextCounty = new sba.lib.gui.bean.DBeanFieldText();
        jpRegistry15 = new javax.swing.JPanel();
        jlState = new javax.swing.JLabel();
        moTextState = new sba.lib.gui.bean.DBeanFieldText();
        jpRegistry16 = new javax.swing.JPanel();
        jlZipCode = new javax.swing.JLabel();
        moTextZipCode = new sba.lib.gui.bean.DBeanFieldText();
        moTextPostOfficeBox = new sba.lib.gui.bean.DBeanFieldText();
        moKeyCountry = new sba.lib.gui.bean.DBeanFieldKey();
        jbCountry = new javax.swing.JButton();
        jpRegistry17 = new javax.swing.JPanel();
        jlNote = new javax.swing.JLabel();
        moTextNote = new sba.lib.gui.bean.DBeanFieldText();
        jpRegistry2 = new javax.swing.JPanel();
        jpRegistry21 = new javax.swing.JPanel();
        jlTelecommDevice1 = new javax.swing.JLabel();
        moTextTelecommDevice1 = new sba.lib.gui.bean.DBeanFieldText();
        moKeyTelecommDevice1Type = new sba.lib.gui.bean.DBeanFieldKey();
        jpRegistry22 = new javax.swing.JPanel();
        jlTelecommDevice2 = new javax.swing.JLabel();
        moTextTelecommDevice2 = new sba.lib.gui.bean.DBeanFieldText();
        moKeyTelecommDevice2Type = new sba.lib.gui.bean.DBeanFieldKey();
        jpRegistry23 = new javax.swing.JPanel();
        jlTelecommDevice3 = new javax.swing.JLabel();
        moTextTelecommDevice3 = new sba.lib.gui.bean.DBeanFieldText();
        moKeyTelecommDevice3Type = new sba.lib.gui.bean.DBeanFieldKey();
        jpRegistry24 = new javax.swing.JPanel();
        jlContact1 = new javax.swing.JLabel();
        moTextContact1 = new sba.lib.gui.bean.DBeanFieldText();
        jpRegistry25 = new javax.swing.JPanel();
        jlContact2 = new javax.swing.JLabel();
        moTextContact2 = new sba.lib.gui.bean.DBeanFieldText();
        jpRegistry26 = new javax.swing.JPanel();
        jlTelecommElectronic1 = new javax.swing.JLabel();
        moTextTelecommElectronic1 = new sba.lib.gui.bean.DBeanFieldText();
        moKeyTelecommElectronic1Type = new sba.lib.gui.bean.DBeanFieldKey();
        jpRegistry27 = new javax.swing.JPanel();
        jlTelecommElectronic2 = new javax.swing.JLabel();
        moTextTelecommElectronic2 = new sba.lib.gui.bean.DBeanFieldText();
        moKeyTelecommElectronic2Type = new sba.lib.gui.bean.DBeanFieldKey();

        setLayout(new java.awt.BorderLayout());

        jpRegistry.setLayout(new java.awt.GridLayout(1, 2, 5, 5));

        jpRegistry1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del domicilio:"));
        jpRegistry1.setLayout(new java.awt.GridLayout(7, 1, 0, 5));

        jpRegistry11.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlName.setText("Nombre:*");
        jlName.setPreferredSize(new java.awt.Dimension(75, 23));
        jpRegistry11.add(jlName);

        moTextName.setPreferredSize(new java.awt.Dimension(175, 23));
        jpRegistry11.add(moTextName);

        moBoolDefault.setText("Default");
        jpRegistry11.add(moBoolDefault);

        jpRegistry1.add(jpRegistry11);

        jpRegistry12.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlStreet.setText("Calle:");
        jlStreet.setPreferredSize(new java.awt.Dimension(75, 23));
        jpRegistry12.add(jlStreet);

        moTextStreet.setToolTipText("Calle");
        moTextStreet.setPreferredSize(new java.awt.Dimension(175, 23));
        jpRegistry12.add(moTextStreet);

        moTextNumberExterior.setToolTipText("Número exterior");
        moTextNumberExterior.setPreferredSize(new java.awt.Dimension(50, 23));
        jpRegistry12.add(moTextNumberExterior);

        moTextNumberInterior.setToolTipText("Número interior");
        moTextNumberInterior.setPreferredSize(new java.awt.Dimension(50, 23));
        jpRegistry12.add(moTextNumberInterior);

        jpRegistry1.add(jpRegistry12);

        jpRegistry13.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlNeighborhood.setText("Colonia:");
        jlNeighborhood.setPreferredSize(new java.awt.Dimension(75, 23));
        jpRegistry13.add(jlNeighborhood);

        moTextNeighborhood.setToolTipText("Colonia");
        moTextNeighborhood.setPreferredSize(new java.awt.Dimension(175, 23));
        jpRegistry13.add(moTextNeighborhood);

        moTextReference.setToolTipText("Referencia");
        moTextReference.setPreferredSize(new java.awt.Dimension(105, 23));
        jpRegistry13.add(moTextReference);

        jpRegistry1.add(jpRegistry13);

        jpRegistry14.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlLocality.setText("Localidad:*");
        jlLocality.setPreferredSize(new java.awt.Dimension(75, 23));
        jpRegistry14.add(jlLocality);

        moTextLocality.setToolTipText("Localidad");
        moTextLocality.setPreferredSize(new java.awt.Dimension(175, 23));
        jpRegistry14.add(moTextLocality);

        moTextCounty.setToolTipText("Municipio");
        moTextCounty.setPreferredSize(new java.awt.Dimension(105, 23));
        jpRegistry14.add(moTextCounty);

        jpRegistry1.add(jpRegistry14);

        jpRegistry15.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlState.setText("Estado:*");
        jlState.setPreferredSize(new java.awt.Dimension(75, 23));
        jpRegistry15.add(jlState);

        moTextState.setToolTipText("Estado");
        moTextState.setPreferredSize(new java.awt.Dimension(175, 23));
        jpRegistry15.add(moTextState);

        jpRegistry1.add(jpRegistry15);

        jpRegistry16.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlZipCode.setText("CP:*");
        jlZipCode.setPreferredSize(new java.awt.Dimension(75, 23));
        jpRegistry16.add(jlZipCode);

        moTextZipCode.setToolTipText("Código postal");
        moTextZipCode.setPreferredSize(new java.awt.Dimension(75, 23));
        jpRegistry16.add(moTextZipCode);

        moTextPostOfficeBox.setToolTipText("Apartado postal");
        moTextPostOfficeBox.setPreferredSize(new java.awt.Dimension(50, 23));
        jpRegistry16.add(moTextPostOfficeBox);

        moKeyCountry.setToolTipText("País");
        moKeyCountry.setPreferredSize(new java.awt.Dimension(122, 23));
        jpRegistry16.add(moKeyCountry);

        jbCountry.setText("<");
        jbCountry.setToolTipText("Modificar país");
        jbCountry.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbCountry.setPreferredSize(new java.awt.Dimension(23, 23));
        jpRegistry16.add(jbCountry);

        jpRegistry1.add(jpRegistry16);

        jpRegistry17.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlNote.setText("Notas:");
        jlNote.setPreferredSize(new java.awt.Dimension(75, 23));
        jpRegistry17.add(jlNote);

        moTextNote.setPreferredSize(new java.awt.Dimension(285, 23));
        jpRegistry17.add(moTextNote);

        jpRegistry1.add(jpRegistry17);

        jpRegistry.add(jpRegistry1);

        jpRegistry2.setBorder(javax.swing.BorderFactory.createTitledBorder("Información de contacto:"));
        jpRegistry2.setLayout(new java.awt.GridLayout(7, 1, 0, 5));

        jpRegistry21.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlTelecommDevice1.setText("Teléfono 1:");
        jlTelecommDevice1.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry21.add(jlTelecommDevice1);

        moTextTelecommDevice1.setPreferredSize(new java.awt.Dimension(150, 23));
        jpRegistry21.add(moTextTelecommDevice1);

        moKeyTelecommDevice1Type.setToolTipText("Tipo de teléfono 1");
        jpRegistry21.add(moKeyTelecommDevice1Type);

        jpRegistry2.add(jpRegistry21);

        jpRegistry22.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlTelecommDevice2.setText("Teléfono 2:");
        jlTelecommDevice2.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry22.add(jlTelecommDevice2);

        moTextTelecommDevice2.setPreferredSize(new java.awt.Dimension(150, 23));
        jpRegistry22.add(moTextTelecommDevice2);

        moKeyTelecommDevice2Type.setToolTipText("Tipo de teléfono 2");
        jpRegistry22.add(moKeyTelecommDevice2Type);

        jpRegistry2.add(jpRegistry22);

        jpRegistry23.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlTelecommDevice3.setText("Teléfono 3:");
        jlTelecommDevice3.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry23.add(jlTelecommDevice3);

        moTextTelecommDevice3.setPreferredSize(new java.awt.Dimension(150, 23));
        jpRegistry23.add(moTextTelecommDevice3);

        moKeyTelecommDevice3Type.setToolTipText("Tipo de teléfono 3");
        jpRegistry23.add(moKeyTelecommDevice3Type);

        jpRegistry2.add(jpRegistry23);

        jpRegistry24.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlContact1.setText("Contacto 1:");
        jlContact1.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry24.add(jlContact1);

        moTextContact1.setPreferredSize(new java.awt.Dimension(255, 23));
        jpRegistry24.add(moTextContact1);

        jpRegistry2.add(jpRegistry24);

        jpRegistry25.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlContact2.setText("Contacto 2:");
        jlContact2.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry25.add(jlContact2);

        moTextContact2.setPreferredSize(new java.awt.Dimension(255, 23));
        jpRegistry25.add(moTextContact2);

        jpRegistry2.add(jpRegistry25);

        jpRegistry26.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlTelecommElectronic1.setForeground(new java.awt.Color(0, 102, 102));
        jlTelecommElectronic1.setText("Buzón o cuenta 1:");
        jlTelecommElectronic1.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry26.add(jlTelecommElectronic1);

        moTextTelecommElectronic1.setPreferredSize(new java.awt.Dimension(150, 23));
        jpRegistry26.add(moTextTelecommElectronic1);

        moKeyTelecommElectronic1Type.setToolTipText("Tipo de buzón o cuenta 1");
        jpRegistry26.add(moKeyTelecommElectronic1Type);

        jpRegistry2.add(jpRegistry26);

        jpRegistry27.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlTelecommElectronic2.setForeground(new java.awt.Color(0, 102, 102));
        jlTelecommElectronic2.setText("Buzón o cuenta 2:");
        jlTelecommElectronic2.setPreferredSize(new java.awt.Dimension(100, 23));
        jpRegistry27.add(jlTelecommElectronic2);

        moTextTelecommElectronic2.setPreferredSize(new java.awt.Dimension(150, 23));
        jpRegistry27.add(moTextTelecommElectronic2);

        moKeyTelecommElectronic2Type.setToolTipText("Tipo de buzón o cuenta 2");
        jpRegistry27.add(moKeyTelecommElectronic2Type);

        jpRegistry2.add(jpRegistry27);

        jpRegistry.add(jpRegistry2);

        add(jpRegistry, java.awt.BorderLayout.NORTH);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsCustom() {
        mbHeadquarters = false;

        moTextName.setTextSettings(DGuiUtils.getLabelName(jlName.getText()), 50);
        moBoolDefault.setBooleanSettings(moBoolDefault.getComponent().getText(), false);
        moTextStreet.setTextSettings(DGuiUtils.getLabelName(jlStreet.getText()), 100, 0);
        moTextNumberExterior.setTextSettings(DGuiUtils.getLabelName(moTextNumberExterior.getToolTipText()), 25, 0);
        moTextNumberInterior.setTextSettings(DGuiUtils.getLabelName(moTextNumberInterior.getToolTipText()), 25, 0);
        moTextNeighborhood.setTextSettings(DGuiUtils.getLabelName(jlNeighborhood.getText()), 100, 0);
        moTextReference.setTextSettings(DGuiUtils.getLabelName(moTextReference.getToolTipText()), 100, 0);
        moTextLocality.setTextSettings(DGuiUtils.getLabelName(jlLocality.getText()), 50, 0);
        moTextCounty.setTextSettings(DGuiUtils.getLabelName(moTextCounty.getToolTipText()), 50, 0);
        moTextState.setTextSettings(DGuiUtils.getLabelName(jlState.getText()), 50, 0);
        moTextZipCode.setTextSettings(DGuiUtils.getLabelName(jlZipCode.getText()), 10, 0);
        moTextPostOfficeBox.setTextSettings(DGuiUtils.getLabelName(moTextPostOfficeBox.getToolTipText()), 10, 0);
        moKeyCountry.setKeySettings(miClient, moKeyCountry.getToolTipText(), false);
        moTextNote.setTextSettings(DGuiUtils.getLabelName(jlNote.getText()), 100, 0);
        moTextTelecommDevice1.setTextSettings(DGuiUtils.getLabelName(jlTelecommDevice1.getText()), 50, 0);
        moKeyTelecommDevice1Type.setKeySettings(miClient, moKeyTelecommDevice1Type.getToolTipText(), true);
        moTextTelecommDevice2.setTextSettings(DGuiUtils.getLabelName(jlTelecommDevice2.getText()), 50, 0);
        moKeyTelecommDevice2Type.setKeySettings(miClient, moKeyTelecommDevice2Type.getToolTipText(), true);
        moTextTelecommDevice3.setTextSettings(DGuiUtils.getLabelName(jlTelecommDevice3.getText()), 50, 0);
        moKeyTelecommDevice3Type.setKeySettings(miClient, moKeyTelecommDevice3Type.getToolTipText(), true);
        moTextContact1.setTextSettings(DGuiUtils.getLabelName(jlContact1.getText()), 50, 0);
        moTextContact2.setTextSettings(DGuiUtils.getLabelName(jlContact2.getText()), 50, 0);
        moTextTelecommElectronic1.setTextSettings(DGuiUtils.getLabelName(jlTelecommElectronic1.getText()), 50, 0);
        moTextTelecommElectronic1.setTextCaseType(DLibConsts.UNDEFINED);
        moKeyTelecommElectronic1Type.setKeySettings(miClient, moKeyTelecommElectronic1Type.getToolTipText(), true);
        moTextTelecommElectronic2.setTextSettings(DGuiUtils.getLabelName(jlTelecommElectronic2.getText()), 50, 0);
        moTextTelecommElectronic2.setTextCaseType(DLibConsts.UNDEFINED);
        moKeyTelecommElectronic2Type.setKeySettings(miClient, moKeyTelecommElectronic2Type.getToolTipText(), true);

        moFields.addField(moTextName);
        moFields.addField(moBoolDefault);
        moFields.addField(moTextStreet);
        moFields.addField(moTextNumberExterior);
        moFields.addField(moTextNumberInterior);
        moFields.addField(moTextNeighborhood);
        moFields.addField(moTextReference);
        moFields.addField(moTextLocality);
        moFields.addField(moTextCounty);
        moFields.addField(moTextState);
        moFields.addField(moTextZipCode);
        moFields.addField(moTextPostOfficeBox);
        moFields.addField(moKeyCountry);
        moFields.addField(moTextNote);
        moFields.addField(moTextTelecommDevice1);
        moFields.addField(moKeyTelecommDevice1Type);
        moFields.addField(moTextTelecommDevice2);
        moFields.addField(moKeyTelecommDevice2Type);
        moFields.addField(moTextTelecommDevice3);
        moFields.addField(moKeyTelecommDevice3Type);
        moFields.addField(moTextContact1);
        moFields.addField(moTextContact2);
        moFields.addField(moTextTelecommElectronic1);
        moFields.addField(moKeyTelecommElectronic1Type);
        moFields.addField(moTextTelecommElectronic2);
        moFields.addField(moKeyTelecommElectronic2Type);
    }

    private void actionCountry() {
        jbCountry.setEnabled(false);
        moKeyCountry.setEnabled(true);
        moKeyCountry.requestFocus();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbCountry;
    private javax.swing.JLabel jlContact1;
    private javax.swing.JLabel jlContact2;
    private javax.swing.JLabel jlLocality;
    private javax.swing.JLabel jlName;
    private javax.swing.JLabel jlNeighborhood;
    private javax.swing.JLabel jlNote;
    private javax.swing.JLabel jlState;
    private javax.swing.JLabel jlStreet;
    private javax.swing.JLabel jlTelecommDevice1;
    private javax.swing.JLabel jlTelecommDevice2;
    private javax.swing.JLabel jlTelecommDevice3;
    private javax.swing.JLabel jlTelecommElectronic1;
    private javax.swing.JLabel jlTelecommElectronic2;
    private javax.swing.JLabel jlZipCode;
    private javax.swing.JPanel jpRegistry;
    private javax.swing.JPanel jpRegistry1;
    private javax.swing.JPanel jpRegistry11;
    private javax.swing.JPanel jpRegistry12;
    private javax.swing.JPanel jpRegistry13;
    private javax.swing.JPanel jpRegistry14;
    private javax.swing.JPanel jpRegistry15;
    private javax.swing.JPanel jpRegistry16;
    private javax.swing.JPanel jpRegistry17;
    private javax.swing.JPanel jpRegistry2;
    private javax.swing.JPanel jpRegistry21;
    private javax.swing.JPanel jpRegistry22;
    private javax.swing.JPanel jpRegistry23;
    private javax.swing.JPanel jpRegistry24;
    private javax.swing.JPanel jpRegistry25;
    private javax.swing.JPanel jpRegistry26;
    private javax.swing.JPanel jpRegistry27;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolDefault;
    private sba.lib.gui.bean.DBeanFieldKey moKeyCountry;
    private sba.lib.gui.bean.DBeanFieldKey moKeyTelecommDevice1Type;
    private sba.lib.gui.bean.DBeanFieldKey moKeyTelecommDevice2Type;
    private sba.lib.gui.bean.DBeanFieldKey moKeyTelecommDevice3Type;
    private sba.lib.gui.bean.DBeanFieldKey moKeyTelecommElectronic1Type;
    private sba.lib.gui.bean.DBeanFieldKey moKeyTelecommElectronic2Type;
    private sba.lib.gui.bean.DBeanFieldText moTextContact1;
    private sba.lib.gui.bean.DBeanFieldText moTextContact2;
    private sba.lib.gui.bean.DBeanFieldText moTextCounty;
    private sba.lib.gui.bean.DBeanFieldText moTextLocality;
    private sba.lib.gui.bean.DBeanFieldText moTextName;
    private sba.lib.gui.bean.DBeanFieldText moTextNeighborhood;
    private sba.lib.gui.bean.DBeanFieldText moTextNote;
    private sba.lib.gui.bean.DBeanFieldText moTextNumberExterior;
    private sba.lib.gui.bean.DBeanFieldText moTextNumberInterior;
    private sba.lib.gui.bean.DBeanFieldText moTextPostOfficeBox;
    private sba.lib.gui.bean.DBeanFieldText moTextReference;
    private sba.lib.gui.bean.DBeanFieldText moTextState;
    private sba.lib.gui.bean.DBeanFieldText moTextStreet;
    private sba.lib.gui.bean.DBeanFieldText moTextTelecommDevice1;
    private sba.lib.gui.bean.DBeanFieldText moTextTelecommDevice2;
    private sba.lib.gui.bean.DBeanFieldText moTextTelecommDevice3;
    private sba.lib.gui.bean.DBeanFieldText moTextTelecommElectronic1;
    private sba.lib.gui.bean.DBeanFieldText moTextTelecommElectronic2;
    private sba.lib.gui.bean.DBeanFieldText moTextZipCode;
    // End of variables declaration//GEN-END:variables

    /*
     * Public methods
     */

    public void setHeadquarters(boolean headquarters) {
        mbHeadquarters = headquarters;

        if (mbHeadquarters) {
            moTextName.setEnabled(false);
            moTextNote.setEnabled(false);
        }
        else {
            moTextName.setEnabled(true);
            moTextNote.setEnabled(true);
        }
    }

    /*
     * Overriden methods
     */

    @Override
    public void addAllListeners() {
        jbCountry.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbCountry.removeActionListener(this);
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyCountry, DModConsts.CS_CTY, DLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyTelecommDevice1Type, DModConsts.BS_TCD_TP, DLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyTelecommDevice2Type, DModConsts.BS_TCD_TP, DLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyTelecommDevice3Type, DModConsts.BS_TCD_TP, DLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyTelecommElectronic1Type, DModConsts.BS_TCE_TP, DLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeyTelecommElectronic2Type, DModConsts.BS_TCE_TP, DLibConsts.UNDEFINED, null);
    }

    @Override
    public void setRegistry(DDbRegistry registry) throws Exception {
        DDbConfigCompany configCompany = null;

        moRegistry = (DDbBranchAddress) registry;

        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();

            moRegistry.setName(mbHeadquarters ? DUtilConsts.HEADQUARTERS_ADD : moRegistry.getName());

            configCompany = (DDbConfigCompany) miClient.getSession().getConfigCompany();

            if (moRegistry.getLocality().isEmpty()) {
                moRegistry.setLocality(configCompany.getDefaultLocality());
            }
            if (moRegistry.getCounty().isEmpty()) {
                moRegistry.setCounty(configCompany.getDefaultCounty());
            }
            if (moRegistry.getState().isEmpty()) {
                moRegistry.setState(configCompany.getDefaultState());
            }
            if (moRegistry.getZipCode().isEmpty()) {
                moRegistry.setZipCode(configCompany.getDefaultZipCode());
            }

            if (moRegistry.getFkTelecommDevice1TypeId() == DLibConsts.UNDEFINED) {
                moRegistry.setFkTelecommDevice1TypeId(DModSysConsts.BS_TCD_TP_NA);
            }
            if (moRegistry.getFkTelecommDevice2TypeId() == DLibConsts.UNDEFINED) {
                moRegistry.setFkTelecommDevice2TypeId(DModSysConsts.BS_TCD_TP_NA);
            }
            if (moRegistry.getFkTelecommDevice3TypeId() == DLibConsts.UNDEFINED) {
                moRegistry.setFkTelecommDevice3TypeId(DModSysConsts.BS_TCD_TP_NA);
            }
            if (moRegistry.getFkTelecommElectronic1TypeId() == DLibConsts.UNDEFINED) {
                moRegistry.setFkTelecommElectronic1TypeId(DModSysConsts.BS_TCE_TP_NA);
            }
            if (moRegistry.getFkTelecommElectronic2TypeId() == DLibConsts.UNDEFINED) {
                moRegistry.setFkTelecommElectronic2TypeId(DModSysConsts.BS_TCE_TP_NA);
            }
        }
        else {

        }

        moTextName.setValue(moRegistry.getName());
        moTextStreet.setValue(moRegistry.getAddress1());
        moTextNeighborhood.setValue(moRegistry.getAddress2());
        moTextReference.setValue(moRegistry.getAddress3());
        moTextNumberExterior.setValue(moRegistry.getNumberExterior());
        moTextNumberInterior.setValue(moRegistry.getNumberInterior());
        moTextLocality.setValue(moRegistry.getLocality());
        moTextCounty.setValue(moRegistry.getCounty());
        moTextState.setValue(moRegistry.getState());
        moTextZipCode.setValue(moRegistry.getZipCode());
        moTextPostOfficeBox.setValue(moRegistry.getPostOfficeBox());
        moTextTelecommDevice1.setValue(moRegistry.getTelecommDevice1());
        moTextTelecommDevice2.setValue(moRegistry.getTelecommDevice2());
        moTextTelecommDevice3.setValue(moRegistry.getTelecommDevice3());
        moTextContact1.setValue(moRegistry.getContact1());
        moTextContact2.setValue(moRegistry.getContact2());
        moTextTelecommElectronic1.setValue(moRegistry.getTelecommElectronic1());
        moTextTelecommElectronic2.setValue(moRegistry.getTelecommElectronic2());
        moTextNote.setValue(moRegistry.getNote());
        moBoolDefault.setValue(moRegistry.isDefault());
        moKeyCountry.setValue(new int[] { moRegistry.getFkCountryId_n() });
        moKeyTelecommDevice1Type.setValue(new int[] { moRegistry.getFkTelecommDevice1TypeId() });
        moKeyTelecommDevice2Type.setValue(new int[] { moRegistry.getFkTelecommDevice2TypeId() });
        moKeyTelecommDevice3Type.setValue(new int[] { moRegistry.getFkTelecommDevice3TypeId() });
        moKeyTelecommElectronic1Type.setValue(new int[] { moRegistry.getFkTelecommElectronic1TypeId() });
        moKeyTelecommElectronic2Type.setValue(new int[] { moRegistry.getFkTelecommElectronic2TypeId() });

        setPanelEditable(true);

        jbCountry.setEnabled(true);
        moKeyCountry.setEnabled(false);

        addAllListeners();
    }

    @Override
    public DDbBranchAddress getRegistry() throws Exception {
        DDbBranchAddress registry = moRegistry.clone();

        if (registry.isRegistryNew()) { } else { }

        registry.setName(moTextName.getValue());
        registry.setAddress1(moTextStreet.getValue());
        registry.setAddress2(moTextNeighborhood.getValue());
        registry.setAddress3(moTextReference.getValue());
        registry.setNumberExterior(moTextNumberExterior.getValue());
        registry.setNumberInterior(moTextNumberInterior.getValue());
        registry.setLocality(moTextLocality.getValue());
        registry.setCounty(moTextCounty.getValue());
        registry.setState(moTextState.getValue());
        registry.setZipCode(moTextZipCode.getValue());
        registry.setPostOfficeBox(moTextPostOfficeBox.getValue());
        registry.setTelecommDevice1(moTextTelecommDevice1.getValue());
        registry.setTelecommDevice2(moTextTelecommDevice2.getValue());
        registry.setTelecommDevice3(moTextTelecommDevice3.getValue());
        registry.setContact1(moTextContact1.getValue());
        registry.setContact2(moTextContact2.getValue());
        registry.setTelecommElectronic1(moTextTelecommElectronic1.getValue());
        registry.setTelecommElectronic2(moTextTelecommElectronic2.getValue());
        registry.setNote(moTextNote.getValue());
        registry.setDefault(moBoolDefault.getValue());
        registry.setFkCountryId_n(moKeyCountry.getSelectedIndex() <= 0 ? DLibConsts.UNDEFINED : moKeyCountry.getValue()[0]);
        registry.setFkTelecommDevice1TypeId(moKeyTelecommDevice1Type.getValue()[0]);
        registry.setFkTelecommDevice2TypeId(moKeyTelecommDevice2Type.getValue()[0]);
        registry.setFkTelecommDevice3TypeId(moKeyTelecommDevice3Type.getValue()[0]);
        registry.setFkTelecommElectronic1TypeId(moKeyTelecommElectronic1Type.getValue()[0]);
        registry.setFkTelecommElectronic2TypeId(moKeyTelecommElectronic2Type.getValue()[0]);

        return registry;
    }

    @Override
    public DGuiValidation validatePanel() {
        DGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            if (((DDbConfigCompany) miClient.getSession().getConfigCompany()).isTelecommDeviceRequired() && moTextTelecommDevice1.getValue().isEmpty()) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moTextTelecommDevice1.getFieldName() + "'.");
                validation.setComponent(moTextTelecommDevice1);
            }
            else if (moTextTelecommDevice1.getValue().isEmpty() && moKeyTelecommDevice1Type.getSelectedIndex() > 1) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moTextTelecommDevice1.getFieldName() + "'.");
                validation.setComponent(moTextTelecommDevice1);
            }
            else if (moTextTelecommDevice2.getValue().isEmpty() && moKeyTelecommDevice2Type.getSelectedIndex() > 1) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moTextTelecommDevice2.getFieldName() + "'.");
                validation.setComponent(moTextTelecommDevice2);
            }
            else if (moTextTelecommDevice3.getValue().isEmpty() && moKeyTelecommDevice3Type.getSelectedIndex() > 1) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moTextTelecommDevice3.getFieldName() + "'.");
                validation.setComponent(moTextTelecommDevice3);
            }
            else if (!moTextTelecommDevice1.getValue().isEmpty() && moKeyTelecommDevice1Type.getSelectedIndex() <= 1) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moKeyTelecommDevice1Type.getFieldName() + "'.");
                validation.setComponent(moKeyTelecommDevice1Type);
            }
            else if (!moTextTelecommDevice2.getValue().isEmpty() && moKeyTelecommDevice2Type.getSelectedIndex() <= 1) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moKeyTelecommDevice2Type.getFieldName() + "'.");
                validation.setComponent(moKeyTelecommDevice2Type);
            }
            else if (!moTextTelecommDevice3.getValue().isEmpty() && moKeyTelecommDevice3Type.getSelectedIndex() <= 1) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moKeyTelecommDevice3Type.getFieldName() + "'.");
                validation.setComponent(moKeyTelecommDevice3Type);
            }
            else if (moTextTelecommElectronic1.getValue().isEmpty() && moKeyTelecommElectronic1Type.getSelectedIndex() > 1) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moTextTelecommElectronic1.getFieldName() + "'.");
                validation.setComponent(moTextTelecommElectronic1);
            }
            else if (moTextTelecommElectronic2.getValue().isEmpty() && moKeyTelecommElectronic2Type.getSelectedIndex() > 1) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moTextTelecommElectronic2.getFieldName() + "'.");
                validation.setComponent(moTextTelecommElectronic2);
            }
            else if (!moTextTelecommElectronic1.getValue().isEmpty() && moKeyTelecommElectronic1Type.getSelectedIndex() <= 1) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moKeyTelecommElectronic1Type.getFieldName() + "'.");
                validation.setComponent(moKeyTelecommElectronic1Type);
            }
            else if (!moTextTelecommElectronic2.getValue().isEmpty() && moKeyTelecommElectronic2Type.getSelectedIndex() <= 1) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moKeyTelecommElectronic2Type.getFieldName() + "'.");
                validation.setComponent(moKeyTelecommElectronic2Type);
            }
        }

        return validation;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbCountry) {
                actionCountry();
            }
        }
    }
}
