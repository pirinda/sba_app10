/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.lad.form;

import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanForm;
import sba.mod.DModConsts;
import sba.mod.lad.db.DDbLocationDistance;

/**
 *
 * @author Sergio Flores
 */
public class DFormLocationDistance extends DBeanForm {
    
    protected DDbLocationDistance moLocationDistance;
    
    /**
     * Creates new form DFormLocationDistance
     * @param client GUI Client.
     * @param title Title.
     */
    public DFormLocationDistance(DGuiClient client, String title) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, DModConsts.LU_LOC_DIST, 0, title);
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

        bgBolIntlTransportDirections = new javax.swing.ButtonGroup();
        bgMerchDimensionUnits = new javax.swing.ButtonGroup();
        bgMerchHazardousMaterial = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jpHeader = new javax.swing.JPanel();
        jpLocInput1 = new javax.swing.JPanel();
        jpLocInput11 = new javax.swing.JPanel();
        jlLocSourceLocation = new javax.swing.JLabel();
        moKeyLocSourceLocation = new sba.lib.gui.bean.DBeanFieldKey();
        jpLocInput12 = new javax.swing.JPanel();
        jlLocDestinyLocation = new javax.swing.JLabel();
        moKeyLocDestinyLocation = new sba.lib.gui.bean.DBeanFieldKey();
        jpLocInput13 = new javax.swing.JPanel();
        jlLocDistanceKm = new javax.swing.JLabel();
        moDecLocDistanceKm = new sba.lib.gui.bean.DBeanFieldDecimal();
        jlLocDistanceKmUnit = new javax.swing.JLabel();

        jPanel1.setLayout(new java.awt.BorderLayout());

        jpHeader.setLayout(new java.awt.GridLayout(1, 2));

        jpLocInput1.setBorder(javax.swing.BorderFactory.createTitledBorder("Distancia entre ubicaciones:"));
        jpLocInput1.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jpLocInput11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLocSourceLocation.setText("Origen:*");
        jlLocSourceLocation.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput11.add(jlLocSourceLocation);

        moKeyLocSourceLocation.setPreferredSize(new java.awt.Dimension(230, 23));
        jpLocInput11.add(moKeyLocSourceLocation);

        jpLocInput1.add(jpLocInput11);

        jpLocInput12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLocDestinyLocation.setText("Destino:*");
        jlLocDestinyLocation.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput12.add(jlLocDestinyLocation);

        moKeyLocDestinyLocation.setPreferredSize(new java.awt.Dimension(230, 23));
        jpLocInput12.add(moKeyLocDestinyLocation);

        jpLocInput1.add(jpLocInput12);

        jpLocInput13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLocDistanceKm.setText("Distancia:*");
        jlLocDistanceKm.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput13.add(jlLocDistanceKm);

        moDecLocDistanceKm.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput13.add(moDecLocDistanceKm);

        jlLocDistanceKmUnit.setText("km");
        jlLocDistanceKmUnit.setPreferredSize(new java.awt.Dimension(30, 23));
        jpLocInput13.add(jlLocDistanceKmUnit);

        jpLocInput1.add(jpLocInput13);

        jpHeader.add(jpLocInput1);

        jPanel1.add(jpHeader, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgBolIntlTransportDirections;
    private javax.swing.ButtonGroup bgMerchDimensionUnits;
    private javax.swing.ButtonGroup bgMerchHazardousMaterial;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel jlLocDestinyLocation;
    private javax.swing.JLabel jlLocDistanceKm;
    private javax.swing.JLabel jlLocDistanceKmUnit;
    private javax.swing.JLabel jlLocSourceLocation;
    private javax.swing.JPanel jpHeader;
    private javax.swing.JPanel jpLocInput1;
    private javax.swing.JPanel jpLocInput11;
    private javax.swing.JPanel jpLocInput12;
    private javax.swing.JPanel jpLocInput13;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecLocDistanceKm;
    private sba.lib.gui.bean.DBeanFieldKey moKeyLocDestinyLocation;
    private sba.lib.gui.bean.DBeanFieldKey moKeyLocSourceLocation;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods
     */

    private void initComponentsCustom() {
        DGuiUtils.setWindowBounds(this, 400, 250);
        
        moKeyLocSourceLocation.setKeySettings(miClient, DGuiUtils.getLabelName(jlLocSourceLocation), true);
        moKeyLocDestinyLocation.setKeySettings(miClient, DGuiUtils.getLabelName(jlLocDestinyLocation), true);
        moDecLocDistanceKm.setDecimalSettings(DGuiUtils.getLabelName(jlLocDistanceKm), DGuiConsts.GUI_TYPE_DEC, true);
        moDecLocDistanceKm.setDecimalFormat(DLibUtils.DecimalFormatValue2D);
        
        moFields.addField(moKeyLocSourceLocation);
        moFields.addField(moKeyLocDestinyLocation);
        moFields.addField(moDecLocDistanceKm);
        
        moFields.setFormButton(jbSave);
    }

    private void renderLocationDistance(final DDbLocationDistance locationDistance) {
        moLocationDistance = locationDistance;
        
        if (moLocationDistance == null) {
            moFields.resetFields();
        }
        else {
            moKeyLocSourceLocation.setValue(new int[] { moLocationDistance.getPkLocationSourceId()});
            moKeyLocDestinyLocation.setValue(new int[] { moLocationDistance.getPkLocationDestinyId()});
            moDecLocDistanceKm.setValue(moLocationDistance.getDistanceKm());
        }
    }
    
    /*
     * DBeanForm methods
     */

    @Override
    public void addAllListeners() {

    }

    @Override
    public void removeAllListeners() {

    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyLocSourceLocation, DModConsts.LU_LOC, 0, null);
        miClient.getSession().populateCatalogue(moKeyLocDestinyLocation, DModConsts.LU_LOC, 0, null);
    }
    
    @Override
    public void setRegistry(DDbRegistry registry) throws Exception {
        moLocationDistance = (DDbLocationDistance) registry;

        mnFormResult = 0;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        if (moLocationDistance.isRegistryNew()) {
            moLocationDistance.initPrimaryKey();
            
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(DLibUtils.textKey(moLocationDistance.getPrimaryKey()));
        }

        setFormEditable(true);
        
        renderLocationDistance(moLocationDistance);
        
        if (moLocationDistance.isRegistryNew()) {
            
        }
        else {
            
        }
        
        addAllListeners();
    }

    @Override
    public DDbRegistry getRegistry() throws Exception {
        DDbLocationDistance locationDistance = moLocationDistance.clone();

        if (locationDistance.isRegistryNew()) {

        }

        locationDistance.setPkLocationSourceId(moKeyLocSourceLocation.getValue()[0]); // allow update of primary key!
        locationDistance.setPkLocationDestinyId(moKeyLocDestinyLocation.getValue()[0]); // allow update of primary key!
        locationDistance.setDistanceKm(moDecLocDistanceKm.getValue());
        //location.setUpdatable(...);
        //location.setDisableable(...);
        //location.setDeletable(...);
        //location.setDisabled(...);
        //location.setDeleted(...);
        //location.setSystem(...);
        //location.setFkUserInsertId(...);
        //location.setFkUserUpdateId(...);
        //location.setTsUserInsert(...);
        //location.setTsUserUpdate(...);

        locationDistance.setRegistryEdited(true);
        
        return locationDistance;
    }

    @Override
    public DGuiValidation validateForm() {
        DGuiValidation validation = moFields.validateFields();
        
        if (validation.isValid()) {
            if (moKeyLocSourceLocation.getValue()[0] == moKeyLocDestinyLocation.getValue()[0]) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + moKeyLocDestinyLocation.getFieldName() + "'.");
                validation.setComponent(moKeyLocDestinyLocation);
            }
        }
        
        return validation;
    }
}