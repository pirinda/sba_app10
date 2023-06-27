/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DFormDfrAddenda.java
 *
 * Created on 29/08/2011, 08:02:13 PM
 */

package sba.mod.trn.form;

import cfd.DSubelementAddenda;
import cfd.ext.continental.DElementAddendaContinentalTire;
import cfd.ext.continental.DElementPosicion;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanForm;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.cfg.db.DDbLock;
import sba.mod.cfg.db.DLockUtils;
import sba.mod.trn.db.DDbDfr;
import sba.mod.trn.db.DDbDps;
import sba.mod.trn.db.DDbDpsRow;
import sba.mod.trn.db.DRowDpsRow;
import sba.mod.trn.db.DTrnDfrUtils;

/**
 *
 * @author Sergio Flores
 */
public class DFormDfrAddenda extends DBeanForm implements ActionListener, ListSelectionListener {
    
    private static final String STATUS_PEND = "¡PENDIENTE!";
    private static final String STATUS_DONE = "¡LISTO!";
    private static final int DESCRIP_MAX_LEN = 50;

    private DDbDfr moRegistry;
    private DDbLock moRegistryLock;
    private DDbDps moDps;
    private DDbLock moDpsLock;
    private DGridPaneForm moGridDpsRows;

    /** Creates new form DFormDfrAddenda
     * @param client GUI client.
     */
    public DFormDfrAddenda(DGuiClient client, String title) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, DModConsts.TX_DFR_ADD, DLibConsts.UNDEFINED, title);
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
        jPanel6 = new javax.swing.JPanel();
        moPanelDps = new sba.mod.trn.form.DPanelDps();
        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jlXmlAddendaType = new javax.swing.JLabel();
        jtfXmlAddendaType = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jlContTipoProv = new javax.swing.JLabel();
        jtfContTipoProv = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jlContPo = new javax.swing.JLabel();
        jtfContPo = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jlContPedido = new javax.swing.JLabel();
        moTextContPedido = new sba.lib.gui.bean.DBeanFieldText();
        jpRows = new javax.swing.JPanel();
        jpRows1 = new javax.swing.JPanel();
        jpRows2 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jlContDescripcion = new javax.swing.JLabel();
        jtfContDescripcion = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jlContNumPosicionPo = new javax.swing.JLabel();
        moIntContNumPosicionPo = new sba.lib.gui.bean.DBeanFieldInteger();
        jPanel18 = new javax.swing.JPanel();
        jlContTasaRetencionIva = new javax.swing.JLabel();
        jtfContTasaRetencionIva = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jlContTasaRetencionIsr = new javax.swing.JLabel();
        jtfContTasaRetencionIsr = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jlContEntryStatus = new javax.swing.JLabel();
        jtfContEntryStatus = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        jbContEntryEdit = new javax.swing.JButton();
        jbContEntrySave = new javax.swing.JButton();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jpContainer.setLayout(new java.awt.BorderLayout());

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del documento:"));
        jPanel6.setLayout(new java.awt.BorderLayout(5, 5));
        jPanel6.add(moPanelDps, java.awt.BorderLayout.CENTER);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlXmlAddendaType.setText("Tipo addenda:");
        jlXmlAddendaType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlXmlAddendaType);

        jtfXmlAddendaType.setEditable(false);
        jtfXmlAddendaType.setFocusable(false);
        jtfXmlAddendaType.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel3.add(jtfXmlAddendaType);

        jPanel1.add(jPanel3);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlContTipoProv.setText("Tipo proveedor:");
        jlContTipoProv.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel7.add(jlContTipoProv);

        jtfContTipoProv.setEditable(false);
        jtfContTipoProv.setFocusable(false);
        jtfContTipoProv.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel7.add(jtfContTipoProv);

        jPanel1.add(jPanel7);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlContPo.setText("OP:");
        jlContPo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel11.add(jlContPo);

        jtfContPo.setEditable(false);
        jtfContPo.setFocusable(false);
        jtfContPo.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel11.add(jtfContPo);

        jPanel1.add(jPanel11);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlContPedido.setText("Folio OP:");
        jlContPedido.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel12.add(jlContPedido);
        jPanel12.add(moTextContPedido);

        jPanel1.add(jPanel12);

        jPanel4.add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel6.add(jPanel4, java.awt.BorderLayout.EAST);

        jpContainer.add(jPanel6, java.awt.BorderLayout.NORTH);

        jpRows.setBorder(javax.swing.BorderFactory.createTitledBorder("Partidas del documento:"));
        jpRows.setLayout(new java.awt.BorderLayout(5, 0));

        jpRows1.setLayout(new java.awt.BorderLayout());
        jpRows.add(jpRows1, java.awt.BorderLayout.CENTER);

        jpRows2.setLayout(new java.awt.BorderLayout());

        jPanel15.setLayout(new java.awt.GridLayout(6, 0, 0, 5));

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlContDescripcion.setText("Descripción:");
        jlContDescripcion.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel16.add(jlContDescripcion);

        jtfContDescripcion.setEditable(false);
        jtfContDescripcion.setFocusable(false);
        jtfContDescripcion.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel16.add(jtfContDescripcion);

        jPanel15.add(jPanel16);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlContNumPosicionPo.setText("Posición OP:");
        jlContNumPosicionPo.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel17.add(jlContNumPosicionPo);

        moIntContNumPosicionPo.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel17.add(moIntContNumPosicionPo);

        jPanel15.add(jPanel17);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlContTasaRetencionIva.setText("Tasa retención IVA:");
        jlContTasaRetencionIva.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel18.add(jlContTasaRetencionIva);

        jtfContTasaRetencionIva.setEditable(false);
        jtfContTasaRetencionIva.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfContTasaRetencionIva.setText("0.00");
        jtfContTasaRetencionIva.setFocusable(false);
        jtfContTasaRetencionIva.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel18.add(jtfContTasaRetencionIva);

        jPanel15.add(jPanel18);

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlContTasaRetencionIsr.setText("Tasa retención ISR:");
        jlContTasaRetencionIsr.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel19.add(jlContTasaRetencionIsr);

        jtfContTasaRetencionIsr.setEditable(false);
        jtfContTasaRetencionIsr.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfContTasaRetencionIsr.setText("0.00");
        jtfContTasaRetencionIsr.setFocusable(false);
        jtfContTasaRetencionIsr.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel19.add(jtfContTasaRetencionIsr);

        jPanel15.add(jPanel19);

        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlContEntryStatus.setText("Estatus:");
        jlContEntryStatus.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel20.add(jlContEntryStatus);

        jtfContEntryStatus.setEditable(false);
        jtfContEntryStatus.setFocusable(false);
        jtfContEntryStatus.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel20.add(jtfContEntryStatus);

        jPanel15.add(jPanel20);

        jPanel21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jbContEntryEdit.setText("Modificar");
        jPanel21.add(jbContEntryEdit);

        jbContEntrySave.setText("Guardar");
        jbContEntrySave.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel21.add(jbContEntrySave);

        jPanel15.add(jPanel21);

        jpRows2.add(jPanel15, java.awt.BorderLayout.NORTH);

        jpRows.add(jpRows2, java.awt.BorderLayout.EAST);

        jpContainer.add(jpRows, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpContainer, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        freeLocks(DDbLock.LOCK_ST_FREED_CANCEL);
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JButton jbContEntryEdit;
    private javax.swing.JButton jbContEntrySave;
    private javax.swing.JLabel jlContDescripcion;
    private javax.swing.JLabel jlContEntryStatus;
    private javax.swing.JLabel jlContNumPosicionPo;
    private javax.swing.JLabel jlContPedido;
    private javax.swing.JLabel jlContPo;
    private javax.swing.JLabel jlContTasaRetencionIsr;
    private javax.swing.JLabel jlContTasaRetencionIva;
    private javax.swing.JLabel jlContTipoProv;
    private javax.swing.JLabel jlXmlAddendaType;
    private javax.swing.JPanel jpContainer;
    private javax.swing.JPanel jpRows;
    private javax.swing.JPanel jpRows1;
    private javax.swing.JPanel jpRows2;
    private javax.swing.JTextField jtfContDescripcion;
    private javax.swing.JTextField jtfContEntryStatus;
    private javax.swing.JTextField jtfContPo;
    private javax.swing.JTextField jtfContTasaRetencionIsr;
    private javax.swing.JTextField jtfContTasaRetencionIva;
    private javax.swing.JTextField jtfContTipoProv;
    private javax.swing.JTextField jtfXmlAddendaType;
    private sba.lib.gui.bean.DBeanFieldInteger moIntContNumPosicionPo;
    private sba.mod.trn.form.DPanelDps moPanelDps;
    private sba.lib.gui.bean.DBeanFieldText moTextContPedido;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods
     */
    
    private void initComponentsCustom() {
        DGuiUtils.setWindowBounds(this, 960, 600);

        moTextContPedido.setTextSettings(DGuiUtils.getLabelName(jlContPedido.getText()), 10, 10);
        moIntContNumPosicionPo.setIntegerSettings(DGuiUtils.getLabelName(jlContNumPosicionPo.getText()), DGuiConsts.GUI_TYPE_INT_RAW, false);

        moFields.addField(moTextContPedido);
        moFields.addField(moIntContNumPosicionPo);

        moFields.setFormButton(jbContEntrySave);

        moPanelDps.setPanelSettings(miClient);
        moPanelDps.enableShowCardex();
        
        moGridDpsRows = new DGridPaneForm(miClient, DModConsts.TX_DFR_ADD, mnFormType, "Partidas") {

            @Override
            public void initGrid() {
                setRowButtonsEnabled(false, false, false);
            }

            @Override
            public void createGridColumns() {
                int col = 0;
                DGridColumnForm[] columns = new DGridColumnForm[9];

                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_INT_2B, "#");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_QTY, "Cantidad");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unidad");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_CODE_ITM, DGridConsts.COL_TITLE_CODE);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_ITM_S, DGridConsts.COL_TITLE_NAME);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Subtotal $ M");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Imptos. tras. $ M");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Imptos. rets. $ M");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Total $ M");

                for (col = 0; col < columns.length; col++) {
                    moModel.getGridColumns().add(columns[col]);
                }
            }
        };

        jpRows1.add(moGridDpsRows, BorderLayout.CENTER);

        moGridDpsRows.setForm(null);
        moGridDpsRows.setPaneFormOwner(null);
        
        mvFormGrids.add(moGridDpsRows);
    }
    
    private void freeLock(final DDbLock lock, final int freedLockStatus) {
        if (lock != null) {
            try {
                DLockUtils.freeLock(miClient.getSession(), lock, freedLockStatus);
            }
            catch (Exception e) {
                DLibUtils.printException(this, e);
            }
        }
    }
    
    private void freeLocks(final int freedLockStatus) {
        freeLock(moRegistryLock, freedLockStatus);
        moRegistryLock = null;
        
        freeLock(moDpsLock, freedLockStatus);
        moDpsLock = null;
    }
    
    private void actionPerformedContEntryEdit() {
        DRowDpsRow row = (DRowDpsRow) moGridDpsRows.getSelectedGridRow();
        
        if (row != null) {
            switch (moRegistry.getFkXmlAddendaTypeId()) {
                case DModSysConsts.TS_XML_ADD_TP_CON:
                    jbContEntryEdit.setEnabled(false);
                    jbContEntrySave.setEnabled(true);
                    moIntContNumPosicionPo.setEditable(true);
                    moIntContNumPosicionPo.requestFocus();
                    break;

                default:
                    // do nothing
            }
        }
    }
    
    private void actionPerformedContEntrySave() {
        DRowDpsRow row = (DRowDpsRow) moGridDpsRows.getSelectedGridRow();
        DElementPosicion posicion = null;
        DGuiValidation validation = null;
        
        if (row != null) {
            switch (moRegistry.getFkXmlAddendaTypeId()) {
                case DModSysConsts.TS_XML_ADD_TP_CON:
                    validation = moIntContNumPosicionPo.validateField();
                    
                    if (!validation.isValid()) {
                        DGuiUtils.computeValidation(miClient, validation);
                    }
                    else {
                        posicion = new DElementPosicion();
                        posicion.getAttNumPosicionPo().setInteger(moIntContNumPosicionPo.getValue());
                        posicion.getAttDescripcion().setString(jtfContDescripcion.getText());
                        posicion.getAttTasaRetencionIva().setDouble(DLibUtils.parseDouble(jtfContTasaRetencionIva.getText()));
                        posicion.getAttTasaRetencionIsr().setDouble(DLibUtils.parseDouble(jtfContTasaRetencionIsr.getText()));
                        row.setComplement(posicion);
                        valueChangedGridDpsRows();
                    }
                    break;

                default:
                    // do nothing
            }
        }
    }
    
    private void valueChangedGridDpsRows() {
        DRowDpsRow row = (DRowDpsRow) moGridDpsRows.getSelectedGridRow();
        DElementPosicion posicion = null;
        boolean done = false;
        
        // Clear addenda row fields:
        
        moIntContNumPosicionPo.setValue(0);
        jtfContDescripcion.setText("");
        jtfContTasaRetencionIva.setText("");
        jtfContTasaRetencionIsr.setText("");

        jtfContEntryStatus.setText("");
        
        moIntContNumPosicionPo.setEditable(false);
        
        jbContEntryEdit.setEnabled(false);
        jbContEntrySave.setEnabled(false);
        
        // Prepare addenda row fields and show current data if any:
        
        if (row != null) {
            switch (moRegistry.getFkXmlAddendaTypeId()) {
                case DModSysConsts.TS_XML_ADD_TP_CON:
                    posicion = (DElementPosicion) row.getComplement();
                    done = posicion != null;
                    
                    moIntContNumPosicionPo.setValue(posicion == null ? 0 : posicion.getAttNumPosicionPo().getInteger());
                    jtfContDescripcion.setText(row.getDpsRow().getName().length() <= DESCRIP_MAX_LEN ? row.getDpsRow().getName() : row.getDpsRow().getName().substring(0, DESCRIP_MAX_LEN));
                    jtfContDescripcion.setCaretPosition(0);
                    jtfContTasaRetencionIva.setText(DLibUtils.getDecimalFormatAmount().format(row.getDpsRow().getTaxRetainedRate1()));
                    jtfContTasaRetencionIva.setCaretPosition(0);
                    jtfContTasaRetencionIsr.setText(DLibUtils.getDecimalFormatAmount().format(row.getDpsRow().getTaxRetainedRate2()));
                    jtfContTasaRetencionIsr.setCaretPosition(0);
                    
                    if (done) {
                        // Restrict user input:
                        moIntContNumPosicionPo.setEditable(false);
                        jbContEntryEdit.setEnabled(true);
                        jbContEntrySave.setEnabled(false);
                        
                        jbContEntryEdit.requestFocus();         // request focus for an accesible control for user
                    }
                    else {
                        // Allow user input:
                        moIntContNumPosicionPo.setEditable(true);
                        jbContEntryEdit.setEnabled(false);
                        jbContEntrySave.setEnabled(true);
                        
                        moIntContNumPosicionPo.requestFocus();  // request focus for an accesible control for user
                    }
                    break;
                    
                default:
                    // do nothing
            }
            
            jtfContEntryStatus.setText(done ? STATUS_DONE : STATUS_PEND);
            jtfContEntryStatus.setCaretPosition(0);
        }
    }
    
    /*
     * Public methods
     */
    
    @Override
    public void addAllListeners() {
        jbContEntryEdit.addActionListener(this);
        jbContEntrySave.addActionListener(this);
    }

    @Override
    public void removeAllListeners() {
        jbContEntryEdit.removeActionListener(this);
        jbContEntrySave.removeActionListener(this);
    }

    @Override
    public void reloadCatalogues() {
        
    }

    @Override
    public void setRegistry(DDbRegistry registry) throws Exception {
        int row = 0;
        DSubelementAddenda extAddenda = null;
        DElementAddendaContinentalTire addendaContinentalTire = null;
        Vector<DGridRow> rows = new Vector<>();
        
        moRegistry = (DDbDfr) registry;
        moDps = (DDbDps) miClient.getSession().readRegistry(DModConsts.T_DPS, new int[] { moRegistry.getFkDpsId_n() });

        mnFormResult = DLibConsts.UNDEFINED;
        mbFirstActivation = true;

        // Set registry:

        removeAllListeners();
        reloadCatalogues();

        if (moRegistry.isRegistryNew()) {
            throw new Exception(DGuiConsts.ERR_MSG_FORM_EXIST_REG);
        }
        else {
            jtfRegistryKey.setText("");
            
            try {
                moRegistryLock = moRegistry.assureLock(miClient.getSession());
                moDpsLock = moDps.assureLock(miClient.getSession());
            }
            catch (Exception e) {
                freeLocks(DDbLock.LOCK_ST_FREED_EXCEPTION);
                throw e;
            }
        }
        
        jtfXmlAddendaType.setText((String) miClient.getSession().readField(DModConsts.TS_XML_ADD_TP, new int[] { moRegistry.getFkXmlAddendaTypeId() }, DDbRegistry.FIELD_NAME));
        jtfXmlAddendaType.setCaretPosition(0);
        
        extAddenda = DTrnDfrUtils.extractExtAddenda(moDps, moRegistry.getFkXmlAddendaTypeId());
        
        switch (moRegistry.getFkXmlAddendaTypeId()) {
            case DModSysConsts.TS_XML_ADD_TP_CON:
                addendaContinentalTire = (DElementAddendaContinentalTire) extAddenda;
                break;
                
            default:
                // do nothing
        }

        setFormEditable(true);  // enable all controls before setting form values

        moPanelDps.setValue(DModSysConsts.PARAM_YEAR, DLibTimeUtils.digestYear(moDps.getDate())[0]);
        moPanelDps.setRegistry(moDps);
        
        for (DDbDpsRow child : moDps.getChildRows()) {
            if (!child.isDeleted()) {
                switch (moRegistry.getFkXmlAddendaTypeId()) {
                    case DModSysConsts.TS_XML_ADD_TP_CON:
                        if (addendaContinentalTire.getEltPosicionesPo().getElements().isEmpty()) {
                            rows.add(new DRowDpsRow(child)); // new addenda
                        }
                        else {
                            rows.add(new DRowDpsRow(child, addendaContinentalTire.getEltPosicionesPo().getElements().get(row++))); // existing addenda
                        }
                        break;
                        
                    default:
                        // do nothing
                }
            }
        }

        moGridDpsRows.populateGrid(rows, this);
        
        switch (moRegistry.getFkXmlAddendaTypeId()) {
            case DModSysConsts.TS_XML_ADD_TP_CON:
                jtfContTipoProv.setText(addendaContinentalTire.getEltTipoProv().getValue());
                jtfContTipoProv.setCaretPosition(0);
                jtfContPo.setText(addendaContinentalTire.getEltPo().getValue());
                jtfContPo.setCaretPosition(0);
                
                moTextContPedido.setValue(addendaContinentalTire.getEltPedido().getValue());
                break;
                
            default:
                // do nothing
        }
        
        addAllListeners();
    }

    @Override
    public DDbDfr getRegistry() throws Exception {
        DDbDfr registry = moRegistry.clone();

        if (registry.isRegistryNew()) {
            throw new Exception(DGuiConsts.ERR_MSG_FORM_EXIST_REG);
        }
        else {
            registry.setAuxLock(moRegistryLock); // replace just cloned lock with original one
            registry.setAuxDpsLock(moDpsLock); // provide DPS lock
        }
        
        DSubelementAddenda extAddenda = null;
        
        switch (moRegistry.getFkXmlAddendaTypeId()) {
            case DModSysConsts.TS_XML_ADD_TP_CON:
                DElementAddendaContinentalTire addendaContinentalTire = new DElementAddendaContinentalTire();
                addendaContinentalTire.getEltPo().setValue(jtfContPo.getText());
                addendaContinentalTire.getEltPedido().setValue(moTextContPedido.getValue());
                addendaContinentalTire.getEltTipoProv().setValue(jtfContTipoProv.getText());
                
                for (DGridRow row : moGridDpsRows.getModel().getGridRows()) {
                    DRowDpsRow dpsRow = (DRowDpsRow) row;
                    addendaContinentalTire.getEltPosicionesPo().getElements().add((DElementPosicion) dpsRow.getComplement());
                }
                
                extAddenda = addendaContinentalTire;
                break;
                
            default:
                // do nothing
        }
        
        registry.setDocXmlAddenda(extAddenda.getElementForXml()); // the very only member of registry set on this form!
        registry.setAuxAddendaJustSet(true); // activate request for embedding new addenda and updating XML file when saving this registry

        return registry;
    }

    @Override
    public DGuiValidation validateForm() {
        DGuiValidation validation = moFields.validateFields();

        if (validation.isValid()) {
            if (DLibUtils.parseLong(moTextContPedido.getValue()) == 0L) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + DGuiUtils.getLabelName(jlContPedido) + "' debe ser un folio numérico válido.");
                validation.setComponent(moTextContPedido);
            }
            else {
                for (DGridRow row : moGridDpsRows.getModel().getGridRows()) {
                    DRowDpsRow dpsRow = (DRowDpsRow) row;
                    if (dpsRow.getComplement() == null) {
                        validation.setMessage("No se ha capturado la información de la partida " + dpsRow.getDpsRow().getSortingPos() + ".");
                        validation.setComponent(moGridDpsRows.getTable());
                        break;
                    }
                }
            }
        }

        return validation;
    }

    @Override
    public void setValue(final int type, final Object value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getValue(final int type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionCancel() {
        freeLocks(DDbLock.LOCK_ST_FREED_CANCEL);
        super.actionCancel();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbContEntryEdit) {
                actionPerformedContEntryEdit();
            }
            else if (button == jbContEntrySave) {
                actionPerformedContEntrySave();
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() instanceof ListSelectionModel) {
            if (!e.getValueIsAdjusting()) {
                valueChangedGridDpsRows();
            }
        }
    }
}
