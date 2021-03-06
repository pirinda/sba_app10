/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DFormSpecialPriceListCustomer.java
 *
 * Created on 29/08/2011, 08:02:13 PM
 */

package sba.mod.mkt.form;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanFieldKey;
import sba.lib.gui.bean.DBeanForm;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.mkt.db.DDbSpecialPriceListCustomer;

/**
 *
 * @author Sergio Flores
 */
public class DFormSpecialPriceListCustomer extends DBeanForm implements ItemListener {

    private DDbSpecialPriceListCustomer moRegistry;

    /** Creates new form DFormSpecialPriceListCustomer */
    public DFormSpecialPriceListCustomer(DGuiClient client, String title) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, DModConsts.M_SPE_CUS, DLibConsts.UNDEFINED, title);
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

        jpContainer = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jlLinkCustomerType = new javax.swing.JLabel();
        moKeyLinkCustomerType = new sba.lib.gui.bean.DBeanFieldKey();
        jPanel5 = new javax.swing.JPanel();
        jlReferenceCustomer = new javax.swing.JLabel();
        moKeyReferenceCustomer = new sba.lib.gui.bean.DBeanFieldKey();
        jPanel6 = new javax.swing.JPanel();
        jlSpecialPriceList = new javax.swing.JLabel();
        moKeySpecialPriceList = new sba.lib.gui.bean.DBeanFieldKey();

        jpContainer.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del registro:"));
        jpContainer.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jPanel4.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlLinkCustomerType.setForeground(new java.awt.Color(0, 0, 255));
        jlLinkCustomerType.setText("Tipo referencia:*");
        jlLinkCustomerType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlLinkCustomerType);

        moKeyLinkCustomerType.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel4.add(moKeyLinkCustomerType);

        jPanel1.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlReferenceCustomer.setForeground(new java.awt.Color(0, 0, 255));
        jlReferenceCustomer.setText("Referencia:*");
        jlReferenceCustomer.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jlReferenceCustomer);

        moKeyReferenceCustomer.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel5.add(moKeyReferenceCustomer);

        jPanel1.add(jPanel5);

        jPanel6.setLayout(new java.awt.FlowLayout(0, 5, 0));

        jlSpecialPriceList.setText("Precios especiales:*");
        jlSpecialPriceList.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel6.add(jlSpecialPriceList);

        moKeySpecialPriceList.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel6.add(moKeySpecialPriceList);

        jPanel1.add(jPanel6);

        jpContainer.add(jPanel1, java.awt.BorderLayout.NORTH);

        getContentPane().add(jpContainer, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsCustom() {
        DGuiUtils.setWindowBounds(this, 480, 300);

        moKeyLinkCustomerType.setKeySettings(miClient, DGuiUtils.getLabelName(jlLinkCustomerType), true);
        moKeyReferenceCustomer.setKeySettings(miClient, DGuiUtils.getLabelName(jlReferenceCustomer), true);
        moKeySpecialPriceList.setKeySettings(miClient, DGuiUtils.getLabelName(jlSpecialPriceList), true);

        moFields.addField(moKeyLinkCustomerType);
        moFields.addField(moKeyReferenceCustomer);
        moFields.addField(moKeySpecialPriceList);

        moFields.setFormButton(jbSave);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel jlLinkCustomerType;
    private javax.swing.JLabel jlReferenceCustomer;
    private javax.swing.JLabel jlSpecialPriceList;
    private javax.swing.JPanel jpContainer;
    private sba.lib.gui.bean.DBeanFieldKey moKeyLinkCustomerType;
    private sba.lib.gui.bean.DBeanFieldKey moKeyReferenceCustomer;
    private sba.lib.gui.bean.DBeanFieldKey moKeySpecialPriceList;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods
     */

    private void itemStateLinkCustomerType() {
        if (moKeyLinkCustomerType.getSelectedIndex() <= 0) {
            moKeyReferenceCustomer.setEnabled(false);
            moKeyReferenceCustomer.removeAllItems();
        }
        else {
            moKeyReferenceCustomer.setEnabled(true);

            switch (moKeyLinkCustomerType.getValue()[0]) {
                case DModSysConsts.MS_LNK_CUS_TP_CUS:
                    miClient.getSession().populateCatalogue(moKeyReferenceCustomer, DModConsts.BU_BPR, DModSysConsts.BS_BPR_CL_CUS, null);
                    break;
                case DModSysConsts.MS_LNK_CUS_TP_CUS_TP:
                    miClient.getSession().populateCatalogue(moKeyReferenceCustomer, DModConsts.MU_CUS_TP, DLibConsts.UNDEFINED, null);
                    break;
                default:
                    moKeyReferenceCustomer.setEnabled(false);
                    moKeyReferenceCustomer.removeAllItems();
            }
        }
    }

    /*
     * Public methods
     */

    /*
     * Overriden methods
     */

    @Override
    public void addAllListeners() {
        moKeyLinkCustomerType.addItemListener(this);
    }

    @Override
    public void removeAllListeners() {
        moKeyLinkCustomerType.removeItemListener(this);
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyLinkCustomerType, DModConsts.MS_LNK_CUS_TP, DLibConsts.UNDEFINED, null);
        miClient.getSession().populateCatalogue(moKeySpecialPriceList, DModConsts.M_SPE, DLibConsts.UNDEFINED, null);
    }

    @Override
    public void setRegistry(DDbRegistry registry) throws Exception {
        int idLinkCustomerType = DLibConsts.UNDEFINED;
        int idReferenceCustomer = DLibConsts.UNDEFINED;

        moRegistry = (DDbSpecialPriceListCustomer) registry;

        mnFormResult = DLibConsts.UNDEFINED;
        mbFirstActivation = true;

        removeAllListeners();
        reloadCatalogues();

        idLinkCustomerType = moRegistry.getPkLinkCustomerTypeId();
        idReferenceCustomer = moRegistry.getPkReferenceCustomerId();

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();
            jtfRegistryKey.setText("");
        }
        else {
            jtfRegistryKey.setText(DLibUtils.textKey(moRegistry.getPrimaryKey()));
        }

        moKeyLinkCustomerType.setValue(new int[] { idLinkCustomerType });
        itemStateLinkCustomerType();
        moKeyReferenceCustomer.setValue(new int[] { idReferenceCustomer });

        moKeySpecialPriceList.setValue(new int[] { moRegistry.getFkSpecialPriceListId() });

        setFormEditable(true);

        if (moRegistry.isRegistryNew()) {
            moKeyLinkCustomerType.setEnabled(true);
            moKeyReferenceCustomer.setEnabled(idReferenceCustomer != DLibConsts.UNDEFINED);
        }
        else {
            moKeyLinkCustomerType.setEnabled(false);
            moKeyReferenceCustomer.setEnabled(false);
        }

        addAllListeners();
    }

    @Override
    public DDbSpecialPriceListCustomer getRegistry() throws Exception {
        DDbSpecialPriceListCustomer registry = moRegistry.clone();

        if (registry.isRegistryNew()) {
            registry.setPkLinkCustomerTypeId(moKeyLinkCustomerType.getValue()[0]);
            registry.setPkReferenceCustomerId(moKeyReferenceCustomer.getValue()[0]);
        }

        registry.setFkSpecialPriceListId(moKeySpecialPriceList.getValue()[0]);

        return registry;
    }

    @Override
    public DGuiValidation validateForm() {
        DGuiValidation validation = moFields.validateFields();

        return validation;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof DBeanFieldKey) {
            DBeanFieldKey field = (DBeanFieldKey) e.getSource();

            if (field == moKeyLinkCustomerType) {
                itemStateLinkCustomerType();
            }
        }
    }
}
