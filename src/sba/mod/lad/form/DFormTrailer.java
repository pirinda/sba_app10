/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.lad.form;

import cfd.ver40.DCfdi40Catalogs;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import sba.gui.cat.DXmlCatalog;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiItem;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanFieldKey;
import sba.lib.gui.bean.DBeanForm;
import sba.mod.DModConsts;
import sba.mod.lad.db.DDbTrailer;

/**
 *
 * @author Sergio Flores
 */
public class DFormTrailer extends DBeanForm implements ActionListener, ItemListener {
    
    protected DDbTrailer moTrailer;
    
    protected DXmlCatalog moXmlTruckTrailerSubtype;

    /**
     * Creates new form DFormTrailer
     * @param client GUI Client.
     * @param title Title.
     */
    public DFormTrailer(DGuiClient client, String title) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, DModConsts.LU_TRAIL, 0, title);
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
        jpTruckInput1 = new javax.swing.JPanel();
        jpTruckInput11 = new javax.swing.JPanel();
        jlTruckTrailSubtype = new javax.swing.JLabel();
        moKeyTruckTrailSubtype = new sba.lib.gui.bean.DBeanFieldKey();
        jbTruckTrailEditSubtype = new javax.swing.JButton();
        jpTruckInput12 = new javax.swing.JPanel();
        jlTruckTrailPlate = new javax.swing.JLabel();
        moTextTruckTrailPlate = new sba.lib.gui.bean.DBeanFieldText();

        jPanel1.setLayout(new java.awt.BorderLayout());

        jpHeader.setLayout(new java.awt.GridLayout(1, 2));

        jpTruckInput1.setBorder(javax.swing.BorderFactory.createTitledBorder("Autotransporte:"));
        jpTruckInput1.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jpTruckInput11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTruckTrailSubtype.setText("Subtipo:*");
        jlTruckTrailSubtype.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput11.add(jlTruckTrailSubtype);

        moKeyTruckTrailSubtype.setPreferredSize(new java.awt.Dimension(265, 23));
        jpTruckInput11.add(moKeyTruckTrailSubtype);

        jbTruckTrailEditSubtype.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_edit.gif"))); // NOI18N
        jbTruckTrailEditSubtype.setToolTipText("Obtener siguiente código");
        jbTruckTrailEditSubtype.setPreferredSize(new java.awt.Dimension(23, 23));
        jpTruckInput11.add(jbTruckTrailEditSubtype);

        jpTruckInput1.add(jpTruckInput11);

        jpTruckInput12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTruckTrailPlate.setText("Placa:*");
        jlTruckTrailPlate.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput12.add(jlTruckTrailPlate);

        moTextTruckTrailPlate.setText("TEXT");
        moTextTruckTrailPlate.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput12.add(moTextTruckTrailPlate);

        jpTruckInput1.add(jpTruckInput12);

        jpHeader.add(jpTruckInput1);

        jPanel1.add(jpHeader, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgBolIntlTransportDirections;
    private javax.swing.ButtonGroup bgMerchDimensionUnits;
    private javax.swing.ButtonGroup bgMerchHazardousMaterial;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbTruckTrailEditSubtype;
    private javax.swing.JLabel jlTruckTrailPlate;
    private javax.swing.JLabel jlTruckTrailSubtype;
    private javax.swing.JPanel jpHeader;
    private javax.swing.JPanel jpTruckInput1;
    private javax.swing.JPanel jpTruckInput11;
    private javax.swing.JPanel jpTruckInput12;
    private sba.lib.gui.bean.DBeanFieldKey moKeyTruckTrailSubtype;
    private sba.lib.gui.bean.DBeanFieldText moTextTruckTrailPlate;
    // End of variables declaration//GEN-END:variables

    /*
     * General private methods
     */

    private void initComponentsCustom() {
        DGuiUtils.setWindowBounds(this, 400, 250);
        
        moKeyTruckTrailSubtype.setKeySettings(miClient, DGuiUtils.getLabelName(jlTruckTrailSubtype), true);
        moTextTruckTrailPlate.setTextSettings(DGuiUtils.getLabelName(jlTruckTrailPlate), 7);
        
        moFields.addField(moKeyTruckTrailSubtype);
        moFields.addField(moTextTruckTrailPlate);
        
        moFields.setFormButton(jbSave);
        
        try {
            moXmlTruckTrailerSubtype = new DXmlCatalog(DCfdi40Catalogs.XML_CCP_REM_STP, "xml/" + DCfdi40Catalogs.XML_CCP_REM_STP + DXmlCatalog.XmlFileExt, false);
            
            moXmlTruckTrailerSubtype.populateCatalog((moKeyTruckTrailSubtype));
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }
    
    private void renderTrailer(final DDbTrailer trailer) {
        moTrailer = trailer;
        
        if (moTrailer == null) {
            moFields.resetFields();
        }
        else {
            moKeyTruckTrailSubtype.setValue(new int[] { moXmlTruckTrailerSubtype.getId(moTrailer.getTrailerSubtypeCode()) });
            itemStateChangedTruckTrailSubtype();
            moTextTruckTrailPlate.setValue(moTrailer.getPlate());
        }
    }

    private void actionPerformedTruckTrailEditSubtype() {
        if (miClient.showMsgBoxConfirm("¿Está seguro que desea cambiar el subtipo del remolque?") == JOptionPane.YES_OPTION) {
            jbTruckTrailEditSubtype.setEnabled(false);
            moKeyTruckTrailSubtype.setEnabled(true);
            moKeyTruckTrailSubtype.requestFocusInWindow();
        }
    }
    
    private void itemStateChangedTruckTrailSubtype() {
        if (moKeyTruckTrailSubtype.getSelectedIndex() <= 0) {
            moKeyTruckTrailSubtype.setToolTipText(null);
        }
        else {
            moKeyTruckTrailSubtype.setToolTipText(moKeyTruckTrailSubtype.getSelectedItem().getItem());
        }
    }

    /*
     * DBeanForm methods
     */

    @Override
    public void addAllListeners() {
        jbTruckTrailEditSubtype.addActionListener(this);
        
        moKeyTruckTrailSubtype.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbTruckTrailEditSubtype.removeActionListener(this);
        
        moKeyTruckTrailSubtype.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {

    }
    
    @Override
    public void setRegistry(DDbRegistry registry) throws Exception {
        moTrailer = (DDbTrailer) registry;

        mnFormResult = 0;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        if (moTrailer.isRegistryNew()) {
            moTrailer.initPrimaryKey();
            
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(DLibUtils.textKey(moTrailer.getPrimaryKey()));
        }

        setFormEditable(true);
        
        renderTrailer(moTrailer);
        
        if (moTrailer.isRegistryNew()) {
            moKeyTruckTrailSubtype.setEnabled(true);
            jbTruckTrailEditSubtype.setEnabled(false);
        }
        else {
            moKeyTruckTrailSubtype.setEnabled(false);
            jbTruckTrailEditSubtype.setEnabled(true);
        }
        
        addAllListeners();
    }

    @Override
    public DDbRegistry getRegistry() throws Exception {
        DDbTrailer trailer = moTrailer.clone();

        if (trailer.isRegistryNew()) {
            //trailer.setPkTrailerId(...);
        }
        
        trailer.setCode(moTextTruckTrailPlate.getValue()); // by now, plate serves as name
        trailer.setName(moTextTruckTrailPlate.getValue()); // by now, plate serves as code
        trailer.setPlate(moTextTruckTrailPlate.getValue());
        DGuiItem guiItem = moKeyTruckTrailSubtype.getSelectedItem();
        String[] codeAndName = guiItem.getItem().split(" - ");
        trailer.setTrailerSubtypeCode(guiItem.getCode());
        trailer.setTrailerSubtypeName(codeAndName[1]);
        //trailer.setUpdatable(...);
        //trailer.setDisableable(...);
        //trailer.setDeletable(...);
        //trailer.setDisabled(...);
        //trailer.setDeleted(...);
        //trailer.setSystem(...);
        //trailer.setFkUserInsertId(...);
        //trailer.setFkUserUpdateId(...);
        //trailer.setTsUserInsert(...);
        //trailer.setTsUserUpdate(...);

        trailer.setRegistryEdited(true);
        
        return trailer;
    }

    @Override
    public DGuiValidation validateForm() {
        DGuiValidation validation = moFields.validateFields();
        
        return validation;
    }
    
    /*
     * Listeners methods
     */

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbTruckTrailEditSubtype) {
                actionPerformedTruckTrailEditSubtype();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof DBeanFieldKey) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                DBeanFieldKey field = (DBeanFieldKey) e.getSource();

                if (field == moKeyTruckTrailSubtype) {
                    itemStateChangedTruckTrailSubtype();
                }
            }
        }
    }
}
