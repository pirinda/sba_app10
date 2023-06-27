/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DFormDps.java
 *
 * Created on 29/08/2011, 08:02:13 PM
 */

package sba.mod.trn.form;

import cfd.DCfdConsts;
import cfd.ver40.DCfdi40Catalogs;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import sba.gui.DGuiClientApp;
import sba.gui.DGuiClientSessionCustom;
import sba.gui.cat.DXmlCatalog;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.grid.DGridColumnForm;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridPaneForm;
import sba.lib.grid.DGridPaneFormOwner;
import sba.lib.grid.DGridRow;
import sba.lib.grid.DGridUtils;
import sba.lib.grid.cell.DGridCellRendererNumber;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiItem;
import sba.lib.gui.DGuiParams;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanFieldBoolean;
import sba.lib.gui.bean.DBeanFieldDecimal;
import sba.lib.gui.bean.DBeanFieldInteger;
import sba.lib.gui.bean.DBeanFieldKey;
import sba.lib.gui.bean.DBeanForm;
import sba.lib.img.DImgConsts;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.DModUtils;
import sba.mod.bpr.db.DBprUtils;
import sba.mod.bpr.db.DDbBizPartner;
import sba.mod.bpr.db.DDbBizPartnerConfig;
import sba.mod.bpr.db.DDbBranch;
import sba.mod.bpr.db.DDbBranchAddress;
import sba.mod.cfg.db.DDbConfigBranch;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.cfg.db.DDbLock;
import sba.mod.cfg.db.DDbUser;
import sba.mod.cfg.db.DLockUtils;
import sba.mod.fin.db.DDbTaxGroupConfigRow;
import sba.mod.itm.db.DDbItem;
import sba.mod.itm.db.DDbUnit;
import sba.mod.mkt.db.DDbCustomerItemPriceType;
import sba.mod.trn.db.DDbDfr;
import sba.mod.trn.db.DDbDps;
import sba.mod.trn.db.DDbDpsNote;
import sba.mod.trn.db.DDbDpsRow;
import sba.mod.trn.db.DDbDpsRowNote;
import sba.mod.trn.db.DDbDpsSeries;
import sba.mod.trn.db.DDbDpsSeriesBranch;
import sba.mod.trn.db.DDbDpsSeriesNumber;
import sba.mod.trn.db.DDfrMate;
import sba.mod.trn.db.DDfrMateRelations;
import sba.mod.trn.db.DTrnConsts;
import sba.mod.trn.db.DTrnDfrUtils;
import sba.mod.trn.db.DTrnItemsFound;
import sba.mod.trn.db.DTrnStockMove;
import sba.mod.trn.db.DTrnUtils;

/**
 *
 * @author Sergio Flores
 */
public class DFormDps extends DBeanForm implements DGridPaneFormOwner, ActionListener, FocusListener, ItemListener {
    
    private static final int DOC_TAB_BP = 0;
    private static final int DOC_TAB_DFR = 1;
    private static final int DOC_TAB_GBL = 2;

    private DDbDps moRegistry;
    private DDbLock moRegistryLock;
    private DDbBizPartner moBizPartner;
    private DDbBizPartnerConfig moBizPartnerConfig;
    private DDbBranch moBizPartnerBranchHeadquarters;
    private DDbBranchAddress moBizPartnerBranchAddressOfficial;
    private DGuiClientSessionCustom moSessionCustom;
    private DDbConfigCompany moConfigCompany;
    private DDialogFindBizPartner moDialogFindBizPartner;
    private DDialogFindItem moDialogFindItem;
    private DDialogSelectItemFound moDialogSelectItemFound;
    private DDialogSelectDpsSeries moDialogSelectDpsSeries;
    private DDialogNoteShow moDialogNoteShow;
    private DDialogDpsDiscount moDialogDpsDiscount;
    private DDialogDpsDependentDocsShow moDialogDpsDependentDocsShow;
    private DDialogDpsAdjusted moDialogDpsAdjustedForMoney;
    private DDialogDpsAdjusted moDialogDpsAdjustedForStock;
    private DDialogDpsAdjustedDoc moDialogDpsAdjustedDocument;
    private DDialogDpsAdjustedDocShow moDialogDpsAdjustedDocumentShow;
    private DDialogLot moDialogLot;
    private DDialogLotShow moDialogLotShow;
    private DDialogSerialNumber moDialogSerialNumber;
    private DDialogSerialNumberCompound moDialogSerialNumberCompound;
    private DDialogSerialNumberInStock moDialogSerialNumberInStock;
    private DDialogSerialNumberShow moDialogSerialNumberShow;
    private DDialogCfdRelations moDialogCfdRelations;
    private DGridPaneForm moGridDpsNotes;
    private DGridPaneForm moGridDpsRows;
    private DDbItem moItem;
    private DDbUnit moUnit;
    private int mnCompanyIdentityType;
    private int mnBizPartnerIdentityType;
    private int mnIogCategory;
    private int mnBizPartnerItemPriceType;
    private int[] manAdjustmentClassMoneyKey;
    private int[] manAdjustmentClassStockKey;
    private int[] manCurrentAdjustmentTypeKey;
    private int[] manCurrentAdjustmentClassKey;
    private int[] manCurrentAdjustedDpsKey;
    private String msCurrentAdjustedDpsUuid;
    private double mdBizPartnerDiscountPercentage;
    private boolean mbIsDpsSource; // indicates if this document is source of other documents
    private boolean mbIsDocument; // indicates if this document is just a document
    private boolean mbIsAdjustment; // indicates if this document is for adjustment
    private boolean mbIsPosModule; // indicates if this document is for POS module
    private boolean mbIsImportDeclaration; // indicates if import declaration is enabled
    private boolean mbCanChangePrice;
    private boolean mbCanChangePricePos;
    private boolean mbCanChangeDiscount;
    private boolean mbCanChangeDiscountPos;
    private boolean mbQuantityAlreadySet;
    private boolean mbShowPricesOnFind;
    private boolean mbReloadItemsOnFind;
    private boolean mbCheckDfrOnSaveOnlyOnce;
    private int mnNewDpsNumber;
    private int mnNewDpsSeriesId;
    private String msNewDpsSeries;
    private int mnOriginalYear;
    private Date mtOriginalDate;
    private Vector<DTrnStockMove> mvRowStockMoves;
    private JButton mjButtonLaunchCalc;
    private JButton mjButtonEditItem;
    private JButton mjButtonShowRowNote;
    private JButton mjButtonShowRowLot;
    private JButton mjButtonShowRowSerialNumber;
    private JButton mjButtonShowDependentDocs;
    private JButton mjButtonShowAdjustedDoc;
    private JButton mjButtonAdjustmentForMoney;
    private JButton mjButtonAdjustmentForStock;
    private JButton mjButtonAdjustmentDoc;
    private JTextField mjTextCurrentAdjustmentType;
    private DDbDpsSeries moDpsSeries;
    private DDbDpsSeriesNumber moDpsSeriesNumber;

    private DXmlCatalog moXmlCatalogMethodOfPayment;
    private DXmlCatalog moXmlCatalogCfdUsage;
    private DXmlCatalog moXmlCatalogGlobalPeriodicity;
    private DXmlCatalog moXmlCatalogGlobalMonths;
    
    /** Creates new form DFormDps
     * @param client GUI client.
     * @param type XType of DPS. Constants defined in DModConsts (TX_DPS_...).
     * @param subtype Category of DPS. Constants defined in DModSysConsts (TS_DPS_CT_...).
     * @param title
     */
    public DFormDps(DGuiClient client, int type, int subtype, String title) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, type, subtype, title);
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
        jpHeader = new javax.swing.JPanel();
        jpMain = new javax.swing.JPanel();
        jpMain1 = new javax.swing.JPanel();
        moKeyBizPartner = new sba.lib.gui.bean.DBeanFieldKey();
        jtfDfrReceiverFiscalId = new javax.swing.JTextField();
        jtfDfrReceiverFiscalAddress = new javax.swing.JTextField();
        jlBizPartnerName = new javax.swing.JLabel();
        jbBizPartnerPick = new javax.swing.JButton();
        jbBizPartnerEdit = new javax.swing.JButton();
        jlNumber = new javax.swing.JLabel();
        moTextSeries = new sba.lib.gui.bean.DBeanFieldText();
        moIntNumber = new sba.lib.gui.bean.DBeanFieldInteger();
        jlDateDate = new javax.swing.JLabel();
        moDateDate = new sba.lib.gui.bean.DBeanFieldDate();
        jtfDocType = new javax.swing.JTextField();
        jpMain2 = new javax.swing.JPanel();
        moKeyDfrReceiverTaxRegime = new sba.lib.gui.bean.DBeanFieldKey();
        moKeyDfrCfdUsage = new sba.lib.gui.bean.DBeanFieldKey();
        jlCurrency = new javax.swing.JLabel();
        moKeyCurrency = new sba.lib.gui.bean.DBeanFieldKey();
        jlExchangeRate = new javax.swing.JLabel();
        moDecExchangeRate = new sba.lib.gui.bean.DBeanFieldDecimal();
        jbExchangeRatePick = new javax.swing.JButton();
        jtfDocStatus = new javax.swing.JTextField();
        jtpDocument = new javax.swing.JTabbedPane();
        jpDocument1 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jpDocument11 = new javax.swing.JPanel();
        jlOrder1 = new javax.swing.JLabel();
        moKeyBranchAddress = new sba.lib.gui.bean.DBeanFieldKey();
        jbBranchAddressOfficialView = new javax.swing.JButton();
        jpDocument12 = new javax.swing.JPanel();
        jspBranchAddress = new javax.swing.JScrollPane();
        jtaBranchAddress = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jpDocument13 = new javax.swing.JPanel();
        jlOrder = new javax.swing.JLabel();
        moTextOrder = new sba.lib.gui.bean.DBeanFieldText();
        jlDateDelivery = new javax.swing.JLabel();
        moDateDelivery = new sba.lib.gui.bean.DBeanFieldDate();
        jpDocument214 = new javax.swing.JPanel();
        jlImportDeclaration = new javax.swing.JLabel();
        moTextImportDeclaration = new sba.lib.gui.bean.DBeanFieldText();
        jlImportDeclarationDate = new javax.swing.JLabel();
        moDateImportDeclarationDate = new sba.lib.gui.bean.DBeanFieldDate();
        jpDocument2 = new javax.swing.JPanel();
        jpDocument21 = new javax.swing.JPanel();
        jpDocument211 = new javax.swing.JPanel();
        jlCfdType = new javax.swing.JLabel();
        jtfCfdType = new javax.swing.JTextField();
        jtfCfdStatus = new javax.swing.JTextField();
        jtfDfrPlaceOfIssue = new javax.swing.JTextField();
        jlDfrConfirmation = new javax.swing.JLabel();
        moTextDfrConfirmation = new sba.lib.gui.bean.DBeanFieldText();
        jpDocument212 = new javax.swing.JPanel();
        jlDfrIssuerTaxRegime = new javax.swing.JLabel();
        moKeyDfrIssuerTaxRegime = new sba.lib.gui.bean.DBeanFieldKey();
        jpDocument213 = new javax.swing.JPanel();
        jlDfrCfdRelations = new javax.swing.JLabel();
        jtfDfrCfdRelations = new javax.swing.JTextField();
        jbDfrCfdRelationsEdit = new javax.swing.JButton();
        jpDocument3 = new javax.swing.JPanel();
        jpDocument31 = new javax.swing.JPanel();
        moBoolDfrGlobal = new sba.lib.gui.bean.DBeanFieldBoolean();
        jpDocument311 = new javax.swing.JPanel();
        jlDfrGlobalPeriodicity = new javax.swing.JLabel();
        moKeyDfrGlobalPeriodicity = new sba.lib.gui.bean.DBeanFieldKey();
        jpDocument312 = new javax.swing.JPanel();
        jlDfrGlobalMonths = new javax.swing.JLabel();
        moKeyDfrGlobalMonths = new sba.lib.gui.bean.DBeanFieldKey();
        jpDocument313 = new javax.swing.JPanel();
        jlDfrGlobalYear = new javax.swing.JLabel();
        moCalDfrGlobalYear = new sba.lib.gui.bean.DBeanFieldCalendarYear();
        jpCredit = new javax.swing.JPanel();
        jpCredit1 = new javax.swing.JPanel();
        jpCredit11 = new javax.swing.JPanel();
        jlPaymentType = new javax.swing.JLabel();
        moKeyPaymentType = new sba.lib.gui.bean.DBeanFieldKey();
        jlDfrMethodOfPayment = new javax.swing.JLabel();
        moKeyDfrMethodOfPayment = new sba.lib.gui.bean.DBeanFieldKey();
        jpCredit12 = new javax.swing.JPanel();
        jlCreditDays = new javax.swing.JLabel();
        moIntCreditDays = new sba.lib.gui.bean.DBeanFieldInteger();
        jLabel1 = new javax.swing.JLabel();
        jlModeOfPaymentType = new javax.swing.JLabel();
        moKeyModeOfPaymentType = new sba.lib.gui.bean.DBeanFieldKey();
        jpCredit13 = new javax.swing.JPanel();
        jlDateCredit = new javax.swing.JLabel();
        moDateCredit = new sba.lib.gui.bean.DBeanFieldDate();
        jlDateMaturity = new javax.swing.JLabel();
        jtfDateMaturity = new javax.swing.JTextField();
        jtfDfrPaymentTerms = new javax.swing.JTextField();
        jpCredit14 = new javax.swing.JPanel();
        jlDfrInfo = new javax.swing.JLabel();
        jtfDfrCfdType = new javax.swing.JTextField();
        jtfDfrVersion = new javax.swing.JTextField();
        jtfDfrDatetime = new javax.swing.JTextField();
        jtfDfrUuid = new javax.swing.JTextField();
        jpRows = new javax.swing.JPanel();
        jpRows1 = new javax.swing.JPanel();
        jpRows11 = new javax.swing.JPanel();
        jPanel48 = new javax.swing.JPanel();
        jPanel50 = new javax.swing.JPanel();
        moTextFind = new sba.lib.gui.bean.DBeanFieldText();
        jbFind = new javax.swing.JButton();
        jlRowLabel1 = new javax.swing.JLabel();
        jlRowQuantity = new javax.swing.JLabel();
        jlRowUnitCode = new javax.swing.JLabel();
        jlRowPriceUnitary = new javax.swing.JLabel();
        jlRowSubtotalProv = new javax.swing.JLabel();
        jlRowDiscountDoc = new javax.swing.JLabel();
        jlRowSubtotal = new javax.swing.JLabel();
        jbRowClear = new javax.swing.JButton();
        jPanel51 = new javax.swing.JPanel();
        moTextRowCode = new sba.lib.gui.bean.DBeanFieldText();
        moTextRowName = new sba.lib.gui.bean.DBeanFieldText();
        jtfRowCfdItemKey = new javax.swing.JTextField();
        moDecRowQuantity = new sba.lib.gui.bean.DBeanFieldDecimal();
        jtfRowUnitCode = new javax.swing.JTextField();
        jtfRowCfdUnitKey = new javax.swing.JTextField();
        moDecRowPriceUnitary = new sba.lib.gui.bean.DBeanFieldDecimal();
        moDecRowSubtotalProv = new sba.lib.gui.bean.DBeanFieldDecimal();
        moDecRowDiscountDoc = new sba.lib.gui.bean.DBeanFieldDecimal();
        moDecRowSubtotal = new sba.lib.gui.bean.DBeanFieldDecimal();
        jbRowAdd = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        moBoolRowNote = new sba.lib.gui.bean.DBeanFieldBoolean();
        moTextRowNote = new sba.lib.gui.bean.DBeanFieldText();
        moBoolRowNotePrintable = new sba.lib.gui.bean.DBeanFieldBoolean();
        moBoolRowNoteDfr = new sba.lib.gui.bean.DBeanFieldBoolean();
        moBoolRowCfdPredial = new sba.lib.gui.bean.DBeanFieldBoolean();
        moTextRowCfdPredial = new sba.lib.gui.bean.DBeanFieldText();
        jtfTaxRegion = new javax.swing.JTextField();
        jtfIdentityType = new javax.swing.JTextField();
        moBoolRowTaxInput = new sba.lib.gui.bean.DBeanFieldBoolean();
        jpRows2 = new javax.swing.JPanel();
        jpDocInfo = new javax.swing.JPanel();
        jpDocInfo1 = new javax.swing.JPanel();
        jpDocInfo2 = new javax.swing.JPanel();
        jPanel41 = new javax.swing.JPanel();
        jlEmissionType = new javax.swing.JLabel();
        moKeyEmissionType = new sba.lib.gui.bean.DBeanFieldKey();
        jPanel14 = new javax.swing.JPanel();
        jlAgent = new javax.swing.JLabel();
        moKeyAgent = new sba.lib.gui.bean.DBeanFieldKey();
        jPanel53 = new javax.swing.JPanel();
        jlOwnBranch = new javax.swing.JLabel();
        jtfOwnBranch = new javax.swing.JTextField();
        jtfBranchWarehose = new javax.swing.JTextField();
        jtfTerminal = new javax.swing.JTextField();
        jPanel52 = new javax.swing.JPanel();
        jlTotalQuantity = new javax.swing.JLabel();
        jtfTotalQuantity = new javax.swing.JTextField();
        jpDocTotal = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        moBoolDiscountDocApplying = new sba.lib.gui.bean.DBeanFieldBoolean();
        moBoolDiscountDocPercentageApplying = new sba.lib.gui.bean.DBeanFieldBoolean();
        jbDiscountDocSet = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jlDiscountDocPercentage = new javax.swing.JLabel();
        moDecDiscountDocPercentage = new sba.lib.gui.bean.DBeanFieldDecimal();
        jPanel9 = new javax.swing.JPanel();
        jlSubtotalProv = new javax.swing.JLabel();
        moCurSubtotalProv = new sba.lib.gui.bean.DBeanCompoundFieldCurrency();
        jPanel10 = new javax.swing.JPanel();
        jlDiscountDoc = new javax.swing.JLabel();
        moCurDiscountDoc = new sba.lib.gui.bean.DBeanCompoundFieldCurrency();
        jPanel43 = new javax.swing.JPanel();
        jPanel44 = new javax.swing.JPanel();
        jlSubtotal = new javax.swing.JLabel();
        moCurSubtotal = new sba.lib.gui.bean.DBeanCompoundFieldCurrency();
        jPanel45 = new javax.swing.JPanel();
        jlTaxCharged = new javax.swing.JLabel();
        moCurTaxCharged = new sba.lib.gui.bean.DBeanCompoundFieldCurrency();
        jPanel46 = new javax.swing.JPanel();
        jlTaxRetainded = new javax.swing.JLabel();
        moCurTaxRetained = new sba.lib.gui.bean.DBeanCompoundFieldCurrency();
        jPanel47 = new javax.swing.JPanel();
        jlTotal = new javax.swing.JLabel();
        moCurTotal = new sba.lib.gui.bean.DBeanCompoundFieldCurrency();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jpContainer.setLayout(new java.awt.BorderLayout());

        jpHeader.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del documento:"));
        jpHeader.setLayout(new java.awt.BorderLayout(0, 10));

        jpMain.setLayout(new java.awt.GridLayout(2, 1, 0, 5));

        jpMain1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 0));

        moKeyBizPartner.setToolTipText("Asociado de negocios");
        moKeyBizPartner.setPreferredSize(new java.awt.Dimension(350, 23));
        jpMain1.add(moKeyBizPartner);

        jtfDfrReceiverFiscalId.setEditable(false);
        jtfDfrReceiverFiscalId.setText("XAXX010101000");
        jtfDfrReceiverFiscalId.setToolTipText("RFC del asociado de negocios");
        jtfDfrReceiverFiscalId.setFocusable(false);
        jtfDfrReceiverFiscalId.setPreferredSize(new java.awt.Dimension(95, 23));
        jpMain1.add(jtfDfrReceiverFiscalId);

        jtfDfrReceiverFiscalAddress.setEditable(false);
        jtfDfrReceiverFiscalAddress.setText("00000");
        jtfDfrReceiverFiscalAddress.setToolTipText("Domicilio fiscal (CP) del asociado de negocios");
        jtfDfrReceiverFiscalAddress.setFocusable(false);
        jtfDfrReceiverFiscalAddress.setPreferredSize(new java.awt.Dimension(45, 23));
        jpMain1.add(jtfDfrReceiverFiscalAddress);

        jlBizPartnerName.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/icon_help.png"))); // NOI18N
        jlBizPartnerName.setToolTipText("Razón social: ?");
        jlBizPartnerName.setPreferredSize(new java.awt.Dimension(14, 23));
        jpMain1.add(jlBizPartnerName);

        jbBizPartnerPick.setText("...");
        jbBizPartnerPick.setToolTipText("Buscar... [F9]");
        jbBizPartnerPick.setPreferredSize(new java.awt.Dimension(23, 23));
        jpMain1.add(jbBizPartnerPick);

        jbBizPartnerEdit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_edit.gif"))); // NOI18N
        jbBizPartnerEdit.setToolTipText("Modificar...");
        jbBizPartnerEdit.setPreferredSize(new java.awt.Dimension(23, 23));
        jpMain1.add(jbBizPartnerEdit);

        jlNumber.setText("Folio:*");
        jlNumber.setPreferredSize(new java.awt.Dimension(55, 23));
        jlNumber.setRequestFocusEnabled(false);
        jpMain1.add(jlNumber);

        moTextSeries.setText("TEXT");
        moTextSeries.setToolTipText("Serie");
        moTextSeries.setPreferredSize(new java.awt.Dimension(50, 23));
        jpMain1.add(moTextSeries);

        moIntNumber.setText("999999999");
        moIntNumber.setToolTipText("Folio");
        moIntNumber.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMain1.add(moIntNumber);

        jlDateDate.setText("Fecha:*");
        jlDateDate.setPreferredSize(new java.awt.Dimension(65, 23));
        jpMain1.add(jlDateDate);

        moDateDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jpMain1.add(moDateDate);

        jtfDocType.setEditable(false);
        jtfDocType.setText("TEXT");
        jtfDocType.setToolTipText("Tipo de documento");
        jtfDocType.setFocusable(false);
        jtfDocType.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMain1.add(jtfDocType);

        jpMain.add(jpMain1);

        jpMain2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 3, 0));

        moKeyDfrReceiverTaxRegime.setToolTipText("Régimen fiscal del receptor");
        moKeyDfrReceiverTaxRegime.setPreferredSize(new java.awt.Dimension(350, 23));
        jpMain2.add(moKeyDfrReceiverTaxRegime);

        moKeyDfrCfdUsage.setToolTipText("Uso del CFDI");
        moKeyDfrCfdUsage.setPreferredSize(new java.awt.Dimension(212, 23));
        jpMain2.add(moKeyDfrCfdUsage);

        jlCurrency.setText("Moneda:*");
        jlCurrency.setPreferredSize(new java.awt.Dimension(55, 23));
        jlCurrency.setRequestFocusEnabled(false);
        jpMain2.add(jlCurrency);

        moKeyCurrency.setPreferredSize(new java.awt.Dimension(128, 23));
        jpMain2.add(moKeyCurrency);

        jlExchangeRate.setText("T. cambio:*");
        jlExchangeRate.setPreferredSize(new java.awt.Dimension(65, 23));
        jpMain2.add(jlExchangeRate);

        moDecExchangeRate.setPreferredSize(new java.awt.Dimension(74, 23));
        jpMain2.add(moDecExchangeRate);

        jbExchangeRatePick.setText("...");
        jbExchangeRatePick.setToolTipText("Buscar tipo de cambio");
        jbExchangeRatePick.setPreferredSize(new java.awt.Dimension(23, 23));
        jpMain2.add(jbExchangeRatePick);

        jtfDocStatus.setEditable(false);
        jtfDocStatus.setText("TEXT");
        jtfDocStatus.setToolTipText("Estatus del documento");
        jtfDocStatus.setFocusable(false);
        jtfDocStatus.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMain2.add(jtfDocStatus);

        jpMain.add(jpMain2);

        jpHeader.add(jpMain, java.awt.BorderLayout.NORTH);

        jtpDocument.setTabPlacement(javax.swing.JTabbedPane.LEFT);

        jpDocument1.setLayout(new java.awt.BorderLayout(0, 3));

        jPanel1.setLayout(new java.awt.BorderLayout(0, 3));

        jpDocument11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlOrder1.setText("Domicilio entrega:*");
        jlOrder1.setPreferredSize(new java.awt.Dimension(100, 23));
        jpDocument11.add(jlOrder1);

        moKeyBranchAddress.setToolTipText("Domicilio de entrega");
        moKeyBranchAddress.setPreferredSize(new java.awt.Dimension(275, 23));
        jpDocument11.add(moKeyBranchAddress);

        jbBranchAddressOfficialView.setText("...");
        jbBranchAddressOfficialView.setToolTipText("Ver domicilio principal");
        jbBranchAddressOfficialView.setPreferredSize(new java.awt.Dimension(23, 23));
        jpDocument11.add(jbBranchAddressOfficialView);

        jPanel1.add(jpDocument11, java.awt.BorderLayout.PAGE_START);

        jpDocument12.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 5));
        jpDocument12.setLayout(new java.awt.BorderLayout(0, 5));

        jspBranchAddress.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jspBranchAddress.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        jspBranchAddress.setPreferredSize(new java.awt.Dimension(100, 35));

        jtaBranchAddress.setEditable(false);
        jtaBranchAddress.setBackground(java.awt.SystemColor.control);
        jtaBranchAddress.setColumns(20);
        jtaBranchAddress.setFont(new java.awt.Font("Monospaced", 0, 11)); // NOI18N
        jtaBranchAddress.setText("a\nb\nc");
        jtaBranchAddress.setToolTipText("Domicilio de entrega");
        jtaBranchAddress.setFocusable(false);
        jspBranchAddress.setViewportView(jtaBranchAddress);

        jpDocument12.add(jspBranchAddress, java.awt.BorderLayout.CENTER);

        jPanel1.add(jpDocument12, java.awt.BorderLayout.CENTER);

        jpDocument1.add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.GridLayout(2, 1, 0, 3));

        jpDocument13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlOrder.setText("Pedido/referencia:");
        jlOrder.setPreferredSize(new java.awt.Dimension(100, 23));
        jpDocument13.add(jlOrder);

        moTextOrder.setText("TEXT");
        jpDocument13.add(moTextOrder);

        jlDateDelivery.setText("Entrega:");
        jlDateDelivery.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDocument13.add(jlDateDelivery);

        moDateDelivery.setPreferredSize(new java.awt.Dimension(100, 23));
        jpDocument13.add(moDateDelivery);

        jPanel3.add(jpDocument13);

        jpDocument214.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlImportDeclaration.setText("Número pedimento:");
        jlImportDeclaration.setPreferredSize(new java.awt.Dimension(100, 23));
        jpDocument214.add(jlImportDeclaration);

        moTextImportDeclaration.setText("TEXT");
        jpDocument214.add(moTextImportDeclaration);

        jlImportDeclarationDate.setText("Importación:");
        jlImportDeclarationDate.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDocument214.add(jlImportDeclarationDate);

        moDateImportDeclarationDate.setPreferredSize(new java.awt.Dimension(100, 23));
        jpDocument214.add(moDateImportDeclarationDate);

        jPanel3.add(jpDocument214);

        jpDocument1.add(jPanel3, java.awt.BorderLayout.SOUTH);

        jtpDocument.addTab("Entrega", jpDocument1);

        jpDocument2.setLayout(new java.awt.BorderLayout());

        jpDocument21.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jpDocument211.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCfdType.setText("Datos CFDI:");
        jlCfdType.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDocument211.add(jlCfdType);

        jtfCfdType.setEditable(false);
        jtfCfdType.setText("TEXT");
        jtfCfdType.setToolTipText("Tipo de comprobante");
        jtfCfdType.setFocusable(false);
        jtfCfdType.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDocument211.add(jtfCfdType);

        jtfCfdStatus.setEditable(false);
        jtfCfdStatus.setText("TEXT");
        jtfCfdStatus.setToolTipText("Estatus del comprobante");
        jtfCfdStatus.setFocusable(false);
        jtfCfdStatus.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDocument211.add(jtfCfdStatus);

        jtfDfrPlaceOfIssue.setEditable(false);
        jtfDfrPlaceOfIssue.setText("00000");
        jtfDfrPlaceOfIssue.setToolTipText("Lugar de expedición");
        jtfDfrPlaceOfIssue.setFocusable(false);
        jtfDfrPlaceOfIssue.setPreferredSize(new java.awt.Dimension(50, 23));
        jpDocument211.add(jtfDfrPlaceOfIssue);

        jlDfrConfirmation.setForeground(new java.awt.Color(0, 102, 102));
        jlDfrConfirmation.setText("Confirmación:");
        jlDfrConfirmation.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDocument211.add(jlDfrConfirmation);

        moTextDfrConfirmation.setText("TEXT");
        moTextDfrConfirmation.setToolTipText("Clave de confirmación");
        moTextDfrConfirmation.setPreferredSize(new java.awt.Dimension(50, 23));
        jpDocument211.add(moTextDfrConfirmation);

        jpDocument21.add(jpDocument211);

        jpDocument212.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDfrIssuerTaxRegime.setText("Rég. emisor:*");
        jlDfrIssuerTaxRegime.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDocument212.add(jlDfrIssuerTaxRegime);

        moKeyDfrIssuerTaxRegime.setToolTipText("Régimen fiscal del emisor");
        moKeyDfrIssuerTaxRegime.setPreferredSize(new java.awt.Dimension(345, 23));
        jpDocument212.add(moKeyDfrIssuerTaxRegime);

        jpDocument21.add(jpDocument212);

        jpDocument213.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDfrCfdRelations.setText("Relaciones:");
        jlDfrCfdRelations.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDocument213.add(jlDfrCfdRelations);

        jtfDfrCfdRelations.setEditable(false);
        jtfDfrCfdRelations.setText("TEXT");
        jtfDfrCfdRelations.setToolTipText("CFDI relacionados");
        jtfDfrCfdRelations.setFocusable(false);
        jtfDfrCfdRelations.setPreferredSize(new java.awt.Dimension(317, 23));
        jpDocument213.add(jtfDfrCfdRelations);

        jbDfrCfdRelationsEdit.setText("...");
        jbDfrCfdRelationsEdit.setToolTipText("Gestionar CFDI relacionados");
        jbDfrCfdRelationsEdit.setPreferredSize(new java.awt.Dimension(23, 23));
        jpDocument213.add(jbDfrCfdRelationsEdit);

        jpDocument21.add(jpDocument213);

        jpDocument2.add(jpDocument21, java.awt.BorderLayout.NORTH);

        jtpDocument.addTab("CFDI", jpDocument2);

        jpDocument3.setLayout(new java.awt.BorderLayout());

        jpDocument31.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        moBoolDfrGlobal.setText("Comprobante global de operaciones con el público en general");
        jpDocument31.add(moBoolDfrGlobal);

        jpDocument311.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDfrGlobalPeriodicity.setText("Periodicidad:*");
        jlDfrGlobalPeriodicity.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDocument311.add(jlDfrGlobalPeriodicity);

        moKeyDfrGlobalPeriodicity.setPreferredSize(new java.awt.Dimension(300, 23));
        jpDocument311.add(moKeyDfrGlobalPeriodicity);

        jpDocument31.add(jpDocument311);

        jpDocument312.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDfrGlobalMonths.setText("Meses:*");
        jlDfrGlobalMonths.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDocument312.add(jlDfrGlobalMonths);

        moKeyDfrGlobalMonths.setPreferredSize(new java.awt.Dimension(300, 23));
        jpDocument312.add(moKeyDfrGlobalMonths);

        jpDocument31.add(jpDocument312);

        jpDocument313.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDfrGlobalYear.setText("Año:*");
        jlDfrGlobalYear.setPreferredSize(new java.awt.Dimension(75, 23));
        jpDocument313.add(jlDfrGlobalYear);
        jpDocument313.add(moCalDfrGlobalYear);

        jpDocument31.add(jpDocument313);

        jpDocument3.add(jpDocument31, java.awt.BorderLayout.NORTH);

        jtpDocument.addTab("Global", jpDocument3);

        jpHeader.add(jtpDocument, java.awt.BorderLayout.CENTER);

        jpCredit.setLayout(new java.awt.BorderLayout());

        jpCredit1.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jpCredit11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlPaymentType.setText("Tipo pago:*");
        jlPaymentType.setPreferredSize(new java.awt.Dimension(75, 23));
        jpCredit11.add(jlPaymentType);
        jpCredit11.add(moKeyPaymentType);

        jlDfrMethodOfPayment.setText("Método pago:*");
        jlDfrMethodOfPayment.setPreferredSize(new java.awt.Dimension(85, 23));
        jpCredit11.add(jlDfrMethodOfPayment);

        moKeyDfrMethodOfPayment.setPreferredSize(new java.awt.Dimension(225, 23));
        jpCredit11.add(moKeyDfrMethodOfPayment);

        jpCredit1.add(jpCredit11);

        jpCredit12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCreditDays.setText("Días crédito:*");
        jlCreditDays.setPreferredSize(new java.awt.Dimension(75, 23));
        jpCredit12.add(jlCreditDays);

        moIntCreditDays.setPreferredSize(new java.awt.Dimension(50, 23));
        jpCredit12.add(moIntCreditDays);

        jLabel1.setPreferredSize(new java.awt.Dimension(45, 23));
        jpCredit12.add(jLabel1);

        jlModeOfPaymentType.setText("Forma pago:*");
        jlModeOfPaymentType.setPreferredSize(new java.awt.Dimension(85, 23));
        jpCredit12.add(jlModeOfPaymentType);

        moKeyModeOfPaymentType.setPreferredSize(new java.awt.Dimension(225, 23));
        jpCredit12.add(moKeyModeOfPaymentType);

        jpCredit1.add(jpCredit12);

        jpCredit13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateCredit.setText("Base crédito:*");
        jlDateCredit.setPreferredSize(new java.awt.Dimension(75, 23));
        jpCredit13.add(jlDateCredit);

        moDateCredit.setPreferredSize(new java.awt.Dimension(100, 23));
        jpCredit13.add(moDateCredit);

        jlDateMaturity.setText("Vencimiento:");
        jlDateMaturity.setPreferredSize(new java.awt.Dimension(85, 23));
        jpCredit13.add(jlDateMaturity);

        jtfDateMaturity.setEditable(false);
        jtfDateMaturity.setText("00/00/0000");
        jtfDateMaturity.setToolTipText("Vencimiento");
        jtfDateMaturity.setFocusable(false);
        jtfDateMaturity.setPreferredSize(new java.awt.Dimension(70, 23));
        jpCredit13.add(jtfDateMaturity);

        jtfDfrPaymentTerms.setEditable(false);
        jtfDfrPaymentTerms.setText("TEXT");
        jtfDfrPaymentTerms.setToolTipText("Condiciones de pago");
        jtfDfrPaymentTerms.setFocusable(false);
        jtfDfrPaymentTerms.setPreferredSize(new java.awt.Dimension(150, 23));
        jpCredit13.add(jtfDfrPaymentTerms);

        jpCredit1.add(jpCredit13);

        jpCredit14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDfrInfo.setText("Info. CFDI:");
        jlDfrInfo.setPreferredSize(new java.awt.Dimension(75, 23));
        jpCredit14.add(jlDfrInfo);

        jtfDfrCfdType.setEditable(false);
        jtfDfrCfdType.setText("T");
        jtfDfrCfdType.setToolTipText("Tipo de comprobante");
        jtfDfrCfdType.setFocusable(false);
        jtfDfrCfdType.setPreferredSize(new java.awt.Dimension(30, 23));
        jpCredit14.add(jtfDfrCfdType);

        jtfDfrVersion.setEditable(false);
        jtfDfrVersion.setText("1.0");
        jtfDfrVersion.setToolTipText("Versión del comprobante");
        jtfDfrVersion.setFocusable(false);
        jtfDfrVersion.setPreferredSize(new java.awt.Dimension(30, 23));
        jpCredit14.add(jtfDfrVersion);

        jtfDfrDatetime.setEditable(false);
        jtfDfrDatetime.setText("01/01/2001 00:00:00");
        jtfDfrDatetime.setToolTipText("Fecha-hora del comprobante");
        jtfDfrDatetime.setFocusable(false);
        jtfDfrDatetime.setPreferredSize(new java.awt.Dimension(120, 23));
        jpCredit14.add(jtfDfrDatetime);

        jtfDfrUuid.setEditable(false);
        jtfDfrUuid.setText("00000000-0000-0000-0000-000000000000");
        jtfDfrUuid.setToolTipText("UUID del comprobante");
        jtfDfrUuid.setFocusable(false);
        jtfDfrUuid.setPreferredSize(new java.awt.Dimension(225, 23));
        jpCredit14.add(jtfDfrUuid);

        jpCredit1.add(jpCredit14);

        jpCredit.add(jpCredit1, java.awt.BorderLayout.NORTH);

        jpHeader.add(jpCredit, java.awt.BorderLayout.EAST);

        jpContainer.add(jpHeader, java.awt.BorderLayout.NORTH);

        jpRows.setBorder(javax.swing.BorderFactory.createTitledBorder("Partidas del documento:"));
        jpRows.setLayout(new java.awt.BorderLayout(0, 5));

        jpRows1.setLayout(new java.awt.BorderLayout(0, 5));

        jpRows11.setLayout(new java.awt.BorderLayout());

        jPanel48.setLayout(new java.awt.GridLayout(3, 1, 0, 5));

        jPanel50.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moTextFind.setText("TEXT");
        moTextFind.setToolTipText("Buscar ítem");
        moTextFind.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel50.add(moTextFind);

        jbFind.setText("...");
        jbFind.setToolTipText("Buscar ítem");
        jbFind.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel50.add(jbFind);

        jlRowLabel1.setPreferredSize(new java.awt.Dimension(227, 23));
        jPanel50.add(jlRowLabel1);

        jlRowQuantity.setText("Cantidad:");
        jlRowQuantity.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel50.add(jlRowQuantity);

        jlRowUnitCode.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel50.add(jlRowUnitCode);

        jlRowPriceUnitary.setText("Precio unitario:");
        jlRowPriceUnitary.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel50.add(jlRowPriceUnitary);

        jlRowSubtotalProv.setText("Importe:");
        jlRowSubtotalProv.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel50.add(jlRowSubtotalProv);

        jlRowDiscountDoc.setText("Descuento:");
        jlRowDiscountDoc.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel50.add(jlRowDiscountDoc);

        jlRowSubtotal.setText("Subtotal:");
        jlRowSubtotal.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel50.add(jlRowSubtotal);

        jbRowClear.setIcon(miClient.getImageIcon(DImgConsts.CMD_STD_CLEAR));
        jbRowClear.setToolTipText("Limpiar partida");
        jbRowClear.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel50.add(jbRowClear);

        jPanel48.add(jPanel50);

        jPanel51.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moTextRowCode.setText("TEXT");
        moTextRowCode.setToolTipText("Código ...");
        moTextRowCode.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel51.add(moTextRowCode);

        moTextRowName.setText("TEXT");
        moTextRowName.setToolTipText("Nombre ...");
        moTextRowName.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel51.add(moTextRowName);

        jtfRowCfdItemKey.setEditable(false);
        jtfRowCfdItemKey.setText("12345678");
        jtfRowCfdItemKey.setToolTipText("Clave ProdServ");
        jtfRowCfdItemKey.setFocusable(false);
        jtfRowCfdItemKey.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel51.add(jtfRowCfdItemKey);

        moDecRowQuantity.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel51.add(moDecRowQuantity);

        jtfRowUnitCode.setEditable(false);
        jtfRowUnitCode.setText("UN");
        jtfRowUnitCode.setToolTipText("Unidad ...");
        jtfRowUnitCode.setFocusable(false);
        jtfRowUnitCode.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel51.add(jtfRowUnitCode);

        jtfRowCfdUnitKey.setEditable(false);
        jtfRowCfdUnitKey.setText("XUN");
        jtfRowCfdUnitKey.setToolTipText("Clave Unidad");
        jtfRowCfdUnitKey.setFocusable(false);
        jtfRowCfdUnitKey.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel51.add(jtfRowCfdUnitKey);
        jPanel51.add(moDecRowPriceUnitary);

        moDecRowSubtotalProv.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel51.add(moDecRowSubtotalProv);

        moDecRowDiscountDoc.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel51.add(moDecRowDiscountDoc);
        jPanel51.add(moDecRowSubtotal);

        jbRowAdd.setIcon(miClient.getImageIcon(DImgConsts.CMD_STD_ADD));
        jbRowAdd.setToolTipText("Agregar partida");
        jbRowAdd.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel51.add(jbRowAdd);

        jPanel48.add(jPanel51);

        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolRowNote.setForeground(new java.awt.Color(0, 102, 102));
        moBoolRowNote.setText("Notas:");
        moBoolRowNote.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel5.add(moBoolRowNote);

        moTextRowNote.setText("TEXT");
        moTextRowNote.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel5.add(moTextRowNote);

        moBoolRowNotePrintable.setText("Imprimir");
        moBoolRowNotePrintable.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel5.add(moBoolRowNotePrintable);

        moBoolRowNoteDfr.setText("CFDI");
        moBoolRowNoteDfr.setPreferredSize(new java.awt.Dimension(55, 23));
        jPanel5.add(moBoolRowNoteDfr);

        moBoolRowCfdPredial.setText("Prediales:");
        moBoolRowCfdPredial.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel5.add(moBoolRowCfdPredial);

        moTextRowCfdPredial.setText("TEXT");
        moTextRowCfdPredial.setToolTipText("Separar prediales con coma");
        moTextRowCfdPredial.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel5.add(moTextRowCfdPredial);

        jtfTaxRegion.setEditable(false);
        jtfTaxRegion.setText("TEXT");
        jtfTaxRegion.setToolTipText("Región de impuestos");
        jtfTaxRegion.setFocusable(false);
        jtfTaxRegion.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jtfTaxRegion);

        jtfIdentityType.setEditable(false);
        jtfIdentityType.setText("TEXT");
        jtfIdentityType.setToolTipText("Tipo de persona");
        jtfIdentityType.setFocusable(false);
        jtfIdentityType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel5.add(jtfIdentityType);

        moBoolRowTaxInput.setText("Capturar con impuestos");
        moBoolRowTaxInput.setPreferredSize(new java.awt.Dimension(145, 23));
        jPanel5.add(moBoolRowTaxInput);

        jPanel2.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel48.add(jPanel2);

        jpRows11.add(jPanel48, java.awt.BorderLayout.CENTER);

        jpRows1.add(jpRows11, java.awt.BorderLayout.PAGE_START);

        jpRows.add(jpRows1, java.awt.BorderLayout.CENTER);

        jpRows2.setPreferredSize(new java.awt.Dimension(660, 108));
        jpRows2.setLayout(new java.awt.BorderLayout(5, 5));

        jpDocInfo.setLayout(new java.awt.BorderLayout());

        jpDocInfo1.setLayout(new java.awt.BorderLayout());
        jpDocInfo.add(jpDocInfo1, java.awt.BorderLayout.CENTER);

        jpDocInfo2.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel41.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlEmissionType.setText("Tipo emisión:*");
        jlEmissionType.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel41.add(jlEmissionType);

        moKeyEmissionType.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel41.add(moKeyEmissionType);

        jpDocInfo2.add(jPanel41);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlAgent.setText("Agente:");
        jlAgent.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel14.add(jlAgent);

        moKeyAgent.setPreferredSize(new java.awt.Dimension(150, 23));
        jPanel14.add(moKeyAgent);

        jpDocInfo2.add(jPanel14);

        jPanel53.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlOwnBranch.setText("Sucursal doc.:");
        jlOwnBranch.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel53.add(jlOwnBranch);

        jtfOwnBranch.setEditable(false);
        jtfOwnBranch.setText("TEXT");
        jtfOwnBranch.setToolTipText(DUtilConsts.TXT_BRANCH);
        jtfOwnBranch.setFocusable(false);
        jtfOwnBranch.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel53.add(jtfOwnBranch);

        jtfBranchWarehose.setEditable(false);
        jtfBranchWarehose.setText("TEXT");
        jtfBranchWarehose.setToolTipText(DUtilConsts.TXT_BRANCH_WAH);
        jtfBranchWarehose.setFocusable(false);
        jtfBranchWarehose.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel53.add(jtfBranchWarehose);

        jtfTerminal.setEditable(false);
        jtfTerminal.setText("TEXT");
        jtfTerminal.setToolTipText("Terminal de captura del documento");
        jtfTerminal.setFocusable(false);
        jtfTerminal.setPreferredSize(new java.awt.Dimension(40, 23));
        jPanel53.add(jtfTerminal);

        jpDocInfo2.add(jPanel53);

        jPanel52.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTotalQuantity.setText("Cantidad total:");
        jlTotalQuantity.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel52.add(jlTotalQuantity);

        jtfTotalQuantity.setEditable(false);
        jtfTotalQuantity.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfTotalQuantity.setText("0");
        jtfTotalQuantity.setFocusable(false);
        jtfTotalQuantity.setPreferredSize(new java.awt.Dimension(105, 23));
        jPanel52.add(jtfTotalQuantity);

        jpDocInfo2.add(jPanel52);

        jpDocInfo.add(jpDocInfo2, java.awt.BorderLayout.EAST);

        jpRows2.add(jpDocInfo, java.awt.BorderLayout.CENTER);

        jpDocTotal.setLayout(new java.awt.GridLayout(1, 2, 5, 5));

        jPanel8.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolDiscountDocApplying.setText("Descto. doc.");
        moBoolDiscountDocApplying.setPreferredSize(new java.awt.Dimension(90, 23));
        jPanel11.add(moBoolDiscountDocApplying);

        moBoolDiscountDocPercentageApplying.setText("Descto. %");
        moBoolDiscountDocPercentageApplying.setPreferredSize(new java.awt.Dimension(85, 23));
        jPanel11.add(moBoolDiscountDocPercentageApplying);

        jbDiscountDocSet.setText("...");
        jbDiscountDocSet.setToolTipText("Establecer descuento del documento");
        jbDiscountDocSet.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel11.add(jbDiscountDocSet);

        jPanel8.add(jPanel11);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDiscountDocPercentage.setText("Descto. %:*");
        jlDiscountDocPercentage.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel12.add(jlDiscountDocPercentage);
        jPanel12.add(moDecDiscountDocPercentage);

        jPanel8.add(jPanel12);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSubtotalProv.setText("Importe:");
        jlSubtotalProv.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel9.add(jlSubtotalProv);
        jPanel9.add(moCurSubtotalProv);

        jPanel8.add(jPanel9);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDiscountDoc.setText("Descto. doc.:");
        jlDiscountDoc.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel10.add(jlDiscountDoc);
        jPanel10.add(moCurDiscountDoc);

        jPanel8.add(jPanel10);

        jpDocTotal.add(jPanel8);

        jPanel43.setLayout(new java.awt.GridLayout(4, 1, 0, 5));

        jPanel44.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlSubtotal.setText("Subtotal:");
        jlSubtotal.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel44.add(jlSubtotal);
        jPanel44.add(moCurSubtotal);

        jPanel43.add(jPanel44);

        jPanel45.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTaxCharged.setText("Imptos. tras.:");
        jlTaxCharged.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel45.add(jlTaxCharged);
        jPanel45.add(moCurTaxCharged);

        jPanel43.add(jPanel45);

        jPanel46.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTaxRetainded.setText("Imptos. rets.:");
        jlTaxRetainded.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel46.add(jlTaxRetainded);
        jPanel46.add(moCurTaxRetained);

        jPanel43.add(jPanel46);

        jPanel47.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTotal.setText("Total:");
        jlTotal.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel47.add(jlTotal);
        jPanel47.add(moCurTotal);

        jPanel43.add(jPanel47);

        jpDocTotal.add(jPanel43);

        jpRows2.add(jpDocTotal, java.awt.BorderLayout.EAST);

        jpRows.add(jpRows2, java.awt.BorderLayout.SOUTH);

        jpContainer.add(jpRows, java.awt.BorderLayout.CENTER);

        getContentPane().add(jpContainer, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        freeLockByCancel();
    }//GEN-LAST:event_formWindowClosing

    private void initComponentsCustom() {
        DGuiUtils.setWindowBounds(this, 1024, 640);

        String bizPartnerClassName = DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnFormSubtype));
        
        moConfigCompany = (DDbConfigCompany) miClient.getSession().getConfigCompany(); // refresh it again each time this form is used in method setRegistry()
        
        moKeyBizPartner.setKeySettings(miClient, DGuiUtils.getLabelName(bizPartnerClassName), true);
        moKeyBizPartner.setFieldButton(jbBizPartnerPick);
        moTextSeries.setTextSettings(DGuiUtils.getLabelName(jlNumber), 10, 0);
        moIntNumber.setIntegerSettings(DGuiUtils.getLabelName(jlNumber), DGuiConsts.GUI_TYPE_INT_RAW, true);
        moDateDate.setDateSettings(miClient, DGuiUtils.getLabelName(jlDateDate), true);
        moKeyDfrReceiverTaxRegime.setKeySettings(miClient, moKeyDfrReceiverTaxRegime.getToolTipText(), true);
        moKeyDfrCfdUsage.setKeySettings(miClient, moKeyDfrCfdUsage.getToolTipText(), true);
        moKeyCurrency.setKeySettings(miClient, DGuiUtils.getLabelName(jlCurrency), true);
        moDecExchangeRate.setDecimalSettings(DGuiUtils.getLabelName(jlExchangeRate), DGuiConsts.GUI_TYPE_DEC_EXC_RATE, true);
        moDecExchangeRate.setFieldButton(jbExchangeRatePick);
        moKeyPaymentType.setKeySettings(miClient, DGuiUtils.getLabelName(jlPaymentType), true);
        moIntCreditDays.setIntegerSettings(DGuiUtils.getLabelName(jlCreditDays), DGuiConsts.GUI_TYPE_INT, true);
        moDateCredit.setDateSettings(miClient, DGuiUtils.getLabelName(jlDateCredit), true);
        moKeyDfrMethodOfPayment.setKeySettings(miClient, DGuiUtils.getLabelName(jlDfrMethodOfPayment), true);
        moKeyModeOfPaymentType.setKeySettings(miClient, DGuiUtils.getLabelName(jlModeOfPaymentType), true);
        
        moKeyBranchAddress.setKeySettings(miClient, moKeyBranchAddress.getToolTipText(), true);
        moTextOrder.setTextSettings(DGuiUtils.getLabelName(jlOrder), 15, 0);
        moDateDelivery.setDateSettings(miClient, DGuiUtils.getLabelName(jlDateDelivery), false);
        moTextImportDeclaration.setTextSettings(DGuiUtils.getLabelName(jlImportDeclaration), DDbDps.LEN_IMP_DEC, 0);
        moDateImportDeclarationDate.setDateSettings(miClient, DGuiUtils.getLabelName(jlImportDeclarationDate), false);
        
        moTextDfrConfirmation.setTextSettings(DGuiUtils.getLabelName(jlDfrConfirmation), 5, 0);
        moTextDfrConfirmation.setTextCaseType(0);
        moKeyDfrIssuerTaxRegime.setKeySettings(miClient, DGuiUtils.getLabelName(jlDfrIssuerTaxRegime), true);
        
        moBoolDfrGlobal.setBooleanSettings(moBoolDfrGlobal.getText(), false);
        moKeyDfrGlobalPeriodicity.setKeySettings(miClient, DGuiUtils.getLabelName(jlDfrGlobalPeriodicity), false);
        moKeyDfrGlobalMonths.setKeySettings(miClient, DGuiUtils.getLabelName(jlDfrGlobalMonths), false);
        moCalDfrGlobalYear.setCalendarSettings(DGuiUtils.getLabelName(jlDfrGlobalYear));
        
        moTextFind.setTextSettings(DGuiUtils.getLabelName(moTextFind.getToolTipText()), 100, 0);
        moTextFind.setFieldButton(jbFind);
        moTextRowCode.setTextSettings(moTextRowCode.getToolTipText(), 45, 1);
        moTextRowName.setTextSettings(moTextRowName.getToolTipText(), 150, 1);
        moDecRowQuantity.setDecimalSettings(DGuiUtils.getLabelName(jlRowQuantity), DGuiConsts.GUI_TYPE_DEC_QTY, false);
        moDecRowQuantity.setDecimalFormat(moConfigCompany.getDecimalFormatQuantity());
        moDecRowPriceUnitary.setDecimalSettings(DGuiUtils.getLabelName(jlRowPriceUnitary), DGuiConsts.GUI_TYPE_DEC_AMT_UNIT, false);
        moDecRowPriceUnitary.setDecimalFormat(moConfigCompany.getDecimalFormatPriceUnitary());
        moDecRowSubtotalProv.setDecimalSettings(DGuiUtils.getLabelName(jlRowSubtotalProv), DGuiConsts.GUI_TYPE_DEC_AMT, false);
        moDecRowDiscountDoc.setDecimalSettings(DGuiUtils.getLabelName(jlRowDiscountDoc), DGuiConsts.GUI_TYPE_DEC_AMT, false);
        moDecRowSubtotal.setDecimalSettings(DGuiUtils.getLabelName(jlRowSubtotal), DGuiConsts.GUI_TYPE_DEC_AMT, false);
        moBoolRowNote.setBooleanSettings(DGuiUtils.getLabelName(moBoolRowNote.getText()), false);
        moTextRowNote.setTextSettings(DGuiUtils.getLabelName(moBoolRowNote.getText()), 512, 0);
        moTextRowNote.setTextCaseType(0);
        moBoolRowNotePrintable.setBooleanSettings(moBoolRowNotePrintable.getText(), true);
        moBoolRowNoteDfr.setBooleanSettings(moBoolRowNoteDfr.getText(), false);
        moBoolRowCfdPredial.setBooleanSettings(DGuiUtils.getLabelName(moBoolRowCfdPredial.getText()), false);
        moTextRowCfdPredial.setTextSettings(DGuiUtils.getLabelName(moBoolRowCfdPredial.getText()), 50, 0);
        moBoolRowTaxInput.setBooleanSettings(moBoolRowTaxInput.getText(), false);
        
        moKeyEmissionType.setKeySettings(miClient, DGuiUtils.getLabelName(jlEmissionType), true);
        moKeyAgent.setKeySettings(miClient, DGuiUtils.getLabelName(jlAgent), true);
        moBoolDiscountDocApplying.setBooleanSettings(moBoolDiscountDocApplying.getText(), false);
        moBoolDiscountDocPercentageApplying.setBooleanSettings(moBoolDiscountDocPercentageApplying.getText(), false);
        moDecDiscountDocPercentage.setDecimalSettings(DGuiUtils.getLabelName(jlDiscountDocPercentage), DGuiConsts.GUI_TYPE_DEC_PER_DISC, true);
        moCurSubtotalProv.setCompoundFieldSettings(miClient);
        moCurSubtotalProv.getField().setDecimalSettings(DGuiUtils.getLabelName(jlSubtotal), DGuiConsts.GUI_TYPE_DEC_AMT, false);
        moCurDiscountDoc.setCompoundFieldSettings(miClient);
        moCurDiscountDoc.getField().setDecimalSettings(DGuiUtils.getLabelName(jlDiscountDoc), DGuiConsts.GUI_TYPE_DEC_AMT, false);
        moCurSubtotal.setCompoundFieldSettings(miClient);
        moCurSubtotal.getField().setDecimalSettings(DGuiUtils.getLabelName(jlSubtotal), DGuiConsts.GUI_TYPE_DEC_AMT, false);
        moCurTaxCharged.setCompoundFieldSettings(miClient);
        moCurTaxCharged.getField().setDecimalSettings(DGuiUtils.getLabelName(jlTaxCharged), DGuiConsts.GUI_TYPE_DEC_AMT, false);
        moCurTaxRetained.setCompoundFieldSettings(miClient);
        moCurTaxRetained.getField().setDecimalSettings(DGuiUtils.getLabelName(jlTaxRetainded), DGuiConsts.GUI_TYPE_DEC_AMT, false);
        moCurTotal.setCompoundFieldSettings(miClient);
        moCurTotal.getField().setDecimalSettings(DGuiUtils.getLabelName(jlTotal), DGuiConsts.GUI_TYPE_DEC_AMT, false);

        moFields.addField(moKeyBizPartner);
        moFields.addField(moTextSeries);
        moFields.addField(moIntNumber);
        moFields.addField(moDateDate);
        moFields.addField(moKeyDfrReceiverTaxRegime);
        moFields.addField(moKeyDfrCfdUsage);
        moFields.addField(moKeyCurrency);
        moFields.addField(moDecExchangeRate);
        moFields.addField(moKeyPaymentType);
        moFields.addField(moIntCreditDays);
        moFields.addField(moDateCredit);
        moFields.addField(moKeyDfrMethodOfPayment);
        moFields.addField(moKeyModeOfPaymentType);
        
        moFields.addField(moKeyBranchAddress);
        moFields.addField(moTextOrder);
        moFields.addField(moDateDelivery);
        moFields.addField(moTextImportDeclaration);
        moFields.addField(moDateImportDeclarationDate);
        
        moFields.addField(moTextDfrConfirmation);
        moFields.addField(moKeyDfrIssuerTaxRegime);
        
        moFields.addField(moBoolDfrGlobal);
        moFields.addField(moKeyDfrGlobalPeriodicity);
        moFields.addField(moKeyDfrGlobalMonths);
        moFields.addField(moCalDfrGlobalYear);
        
        moFields.addField(moTextFind);
        moFields.addField(moTextRowCode);
        moFields.addField(moTextRowName);
        moFields.addField(moDecRowQuantity);
        moFields.addField(moDecRowPriceUnitary);
        moFields.addField(moDecRowSubtotalProv);
        moFields.addField(moDecRowDiscountDoc);
        moFields.addField(moDecRowSubtotal);
        moFields.addField(moBoolRowNote);
        moFields.addField(moTextRowNote);
        moFields.addField(moBoolRowNotePrintable);
        moFields.addField(moBoolRowNoteDfr);
        moFields.addField(moBoolRowCfdPredial);
        moFields.addField(moTextRowCfdPredial);
        moFields.addField(moBoolRowTaxInput);
        
        moFields.addField(moKeyEmissionType);
        moFields.addField(moKeyAgent);
        moFields.addField(moBoolDiscountDocApplying);
        moFields.addField(moBoolDiscountDocPercentageApplying);
        moFields.addField(moDecDiscountDocPercentage);
        moFields.addField(moCurSubtotalProv.getField());
        moFields.addField(moCurDiscountDoc.getField());
        moFields.addField(moCurSubtotal.getField());
        moFields.addField(moCurTaxCharged.getField());
        moFields.addField(moCurTaxRetained.getField());
        moFields.addField(moCurTotal.getField());

        moKeyModeOfPaymentType.setNextField(moTextFind);
        moDateDelivery.setNextField(moTextFind);
        moDateImportDeclarationDate.setNextField(moTextFind);
        moCalDfrGlobalYear.setNextField(moTextFind);
        
        moDecRowSubtotal.setNextButton(jbRowAdd);
        moBoolRowTaxInput.setNextButton(jbRowAdd);

        moFields.setFormButton(jbSave);
        
        mnCompanyIdentityType = ((DDbBizPartner) miClient.getSession().readRegistry(DModConsts.BU_BPR, new int[] { DUtilConsts.BPR_CO_ID })).getFkIdentityTypeId();

        jbBizPartnerPick.setToolTipText(DGuiConsts.TXT_BTN_FIND + " " + bizPartnerClassName.toLowerCase() + " " + DUtilConsts.ACTION_KEY);
        jbBizPartnerEdit.setToolTipText(DGuiConsts.TXT_BTN_EDIT + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(mnFormSubtype)).toLowerCase());
        
        moXmlCatalogMethodOfPayment = ((DGuiClientApp) miClient).getXmlCatalogsMap().get(DCfdi40Catalogs.CAT_MDP);
        moXmlCatalogMethodOfPayment.populateCatalog(moKeyDfrMethodOfPayment);
        
        moXmlCatalogCfdUsage = ((DGuiClientApp) miClient).getXmlCatalogsMap().get(DCfdi40Catalogs.CAT_CFDI_USO);
        moXmlCatalogCfdUsage.populateCatalog(moKeyDfrCfdUsage);
        
        moXmlCatalogGlobalPeriodicity = ((DGuiClientApp) miClient).getXmlCatalogsMap().get(DCfdi40Catalogs.CAT_GBL_PER);
        moXmlCatalogGlobalPeriodicity.populateCatalog(moKeyDfrGlobalPeriodicity);
        
        moXmlCatalogGlobalMonths = ((DGuiClientApp) miClient).getXmlCatalogsMap().get(DCfdi40Catalogs.CAT_GBL_MES);
        moXmlCatalogGlobalMonths.populateCatalog(moKeyDfrGlobalMonths);
        
        mnIogCategory = DTrnUtils.isDpsClassForStockIn(DTrnUtils.getDpsClassByDpsXType(mnFormType, mnFormSubtype)) ? DModSysConsts.TS_IOG_CT_IN : DModSysConsts.TS_IOG_CT_OUT;

        switch (mnFormType) {
            case DModConsts.TX_DPS_ORD:
            case DModConsts.TX_DPS_DOC_INV:
            case DModConsts.TX_DPS_DOC_NOT:
            case DModConsts.TX_DPS_DOC_TIC:
                mbIsDocument = mnFormType != DModConsts.TX_DPS_ORD;
                mbIsAdjustment = false;
                manAdjustmentClassMoneyKey = null;
                manAdjustmentClassStockKey = null;
                moDialogDpsAdjustedForMoney = null;
                moDialogDpsAdjustedForStock = null;
                moDialogDpsAdjustedDocument = null;
                break;
            case DModConsts.TX_DPS_ADJ_INC:
                mbIsDocument = false;
                mbIsAdjustment = true;
                manAdjustmentClassMoneyKey = DModSysConsts.TS_ADJ_CL_INC_INC;
                manAdjustmentClassStockKey = DModSysConsts.TS_ADJ_CL_INC_ADD;
                moDialogDpsAdjustedForMoney = new DDialogDpsAdjusted(miClient, DModConsts.TX_ADJ_INC_INC, mnFormSubtype);
                moDialogDpsAdjustedForStock = new DDialogDpsAdjusted(miClient, DModConsts.TX_ADJ_INC_ADD, mnFormSubtype);
                moDialogDpsAdjustedDocument = new DDialogDpsAdjustedDoc(miClient, DModSysConsts.TS_ADJ_CT_INC, mnFormSubtype);
                break;
            case DModConsts.TX_DPS_ADJ_DEC:
                mbIsDocument = false;
                mbIsAdjustment = true;
                manAdjustmentClassMoneyKey = DModSysConsts.TS_ADJ_CL_DEC_DIS;
                manAdjustmentClassStockKey = DModSysConsts.TS_ADJ_CL_DEC_RET;
                moDialogDpsAdjustedForMoney = new DDialogDpsAdjusted(miClient, DModConsts.TX_ADJ_DEC_DIS, mnFormSubtype);
                moDialogDpsAdjustedForStock = new DDialogDpsAdjusted(miClient, DModConsts.TX_ADJ_DEC_RET, mnFormSubtype);
                moDialogDpsAdjustedDocument = new DDialogDpsAdjustedDoc(miClient, DModSysConsts.TS_ADJ_CT_DEC, mnFormSubtype);
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        moDialogFindBizPartner = new DDialogFindBizPartner(miClient, DTrnUtils.getBizPartnerClassByDpsCategory(mnFormSubtype));
        moDialogFindItem = new DDialogFindItem(miClient, mnIogCategory);
        moDialogSelectItemFound = new DDialogSelectItemFound(miClient);
        moDialogSelectDpsSeries = new DDialogSelectDpsSeries(miClient, mnFormType, mnFormSubtype);
        moDialogNoteShow = new DDialogNoteShow(miClient);
        moDialogDpsDiscount = new DDialogDpsDiscount(miClient);

        if (mbIsDocument || mbIsAdjustment) {
            moDialogLot = new DDialogLot(miClient, mnFormType, mnFormSubtype);
            moDialogLotShow = new DDialogLotShow(miClient, DDialogLotShow.TYPE_DPS);
            moDialogSerialNumber = new DDialogSerialNumber(miClient, mnFormType, mnFormSubtype);
            moDialogSerialNumberCompound = new DDialogSerialNumberCompound(miClient);
            moDialogSerialNumberInStock = new DDialogSerialNumberInStock(miClient);
            moDialogSerialNumberShow = new DDialogSerialNumberShow(miClient, DDialogSerialNumberShow.TYPE_DPS);
            moDialogCfdRelations = new DDialogCfdRelations(miClient);
        }

        mvRowStockMoves = new Vector<>();
        mjButtonLaunchCalc = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_cal.gif")), "Mostrar calculadora", this);
        mjButtonEditItem = DGridUtils.createButton(new ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_mon.gif")), "Editar ítem", this);
        mjButtonShowRowNote = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_NOTE), "Ver notas", this);
        mjButtonShowRowLot = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_LOT), "Ver lotes", this);
        mjButtonShowRowSerialNumber = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_SER_NUM), "Ver números de serie", this);

        if (!mbIsAdjustment) {
            mjButtonShowDependentDocs = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_VIEW), "Ver documentos dependientes", this);
            moDialogDpsDependentDocsShow = new DDialogDpsDependentDocsShow(miClient);

            moDialogDpsAdjustedDocumentShow = null;
            mjButtonShowAdjustedDoc = null;
            mjButtonAdjustmentForMoney = null;
            mjButtonAdjustmentForStock = null;
            mjButtonAdjustmentDoc = null;
            mjTextCurrentAdjustmentType = null;
        }
        else {
            mjButtonShowDependentDocs = null;
            moDialogDpsDependentDocsShow = null;

            moDialogDpsAdjustedDocumentShow = new DDialogDpsAdjustedDocShow(miClient);
            mjButtonShowAdjustedDoc = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_VIEW), "Ver documento ajustado", this);
            mjButtonAdjustmentForMoney = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_ADJ_DISC), DUtilConsts.TXT_CREATE + " " + DTrnUtils.getAdjustmentClassName(manAdjustmentClassMoneyKey).toLowerCase(), this);
            mjButtonAdjustmentForStock = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_ADJ_RET), DUtilConsts.TXT_CREATE + " " + DTrnUtils.getAdjustmentClassName(manAdjustmentClassStockKey).toLowerCase(), this);
            mjButtonAdjustmentDoc = DGridUtils.createButton(miClient.getImageIcon(DImgConsts.CMD_STD_ADJ_DOC), DUtilConsts.TXT_CREATE + " ajuste", this);
            mjTextCurrentAdjustmentType = DGridUtils.createTextField("Tipo de ajuste", new Dimension(200, 23));
        }

        switch (mnFormSubtype) {
            case DModSysConsts.TS_DPS_CT_PUR:
                mbShowPricesOnFind = false;
                mjButtonEditItem.setEnabled(miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_PUR_PRC));
                break;
            case DModSysConsts.TS_DPS_CT_SAL:
                mbShowPricesOnFind = true;
                mjButtonEditItem.setEnabled(miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_SAL_PRC));
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        moDialogFindItem.setValue(DModSysConsts.FLAG_SHOW_PRICES, mbShowPricesOnFind);
        moDialogFindItem.setValue(DModSysConsts.FLAG_ONLY_IN_STOCK, (mbIsDocument || mbIsAdjustment) && DTrnUtils.isDpsClassForStockOut(DTrnUtils.getDpsClassByDpsXType(mnFormType, mnFormSubtype)));
        moDialogFindItem.setValue(DModSysConsts.FLAG_VALIDATE_STOCK, (mbIsDocument || mbIsAdjustment) && DTrnUtils.isDpsClassForStockOut(DTrnUtils.getDpsClassByDpsXType(mnFormType, mnFormSubtype)));

        moTextFind.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    actionTextFind();
                }
            }
        });

        moGridDpsNotes = new DGridPaneForm(miClient, DModConsts.T_DPS_NOT, mnFormType, "Notas") {

            @Override
            public void initGrid() {

            }

            @Override
            public void createGridColumns() {
                int col = 0;
                DGridColumnForm[] columns = new DGridColumnForm[2];

                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "Nota", 200);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_BOOL_M, "Impresión");

                for (col = 0; col < columns.length; col++) {
                    moModel.getGridColumns().add(columns[col]);
                }
            }
        };

        moGridDpsRows = new DGridPaneForm(miClient, DModConsts.T_DPS_ROW, mnFormType, "Partidas") {

            @Override
            public void initGrid() {
                setRowButtonsEnabled(false, false, true);
            }

            @Override
            public void createGridColumns() {
                int col = 0;
                DGridColumnForm[] columns = new DGridColumnForm[16];

                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_INT_1B, "#");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_CODE_ITM, DGridConsts.COL_TITLE_CODE + " concepto");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_ITM_S, DGridConsts.COL_TITLE_NAME + " concepto");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_CODE_CAT, "Clave ProdServ");
                columns[col] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_QTY, "Cantidad");
                columns[col++].setCellRenderer(new DGridCellRendererNumber(moConfigCompany.getDecimalFormatQuantity()));
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_CODE_UNT, "Unidad");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_CODE_UNT, "Clave Unidad");
                columns[col] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT_UNIT, "Precio unitario $ M");
                columns[col++].setCellRenderer(new DGridCellRendererNumber(moConfigCompany.getDecimalFormatPriceUnitary()));
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Importe $ M");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_BOOL_S, "Descuento");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Descuento $ M");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Subtotal $ M");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Imptos. tras. $ M");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Imptos. rets. $ M");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Total $ M");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT_NAME_CAT_S, "Predial");

                for (col = 0; col < columns.length; col++) {
                    moModel.getGridColumns().add(columns[col]);
                }
            }
        };

        moGridDpsNotes.setForm(new DFormDpsNote(miClient, "Nota"));
        moGridDpsNotes.setPaneFormOwner(this);
        jpDocInfo1.add(moGridDpsNotes, BorderLayout.CENTER);

        jpCommandLeft.add(mjButtonLaunchCalc);

        moGridDpsRows.setForm(null);
        moGridDpsRows.setPaneFormOwner(this);
        moGridDpsRows.getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonEditItem);
        moGridDpsRows.getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonShowRowNote);
        moGridDpsRows.getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonShowRowLot);
        moGridDpsRows.getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonShowRowSerialNumber);

        mjButtonShowRowLot.setEnabled(mbIsDocument || mbIsAdjustment);
        mjButtonShowRowSerialNumber.setEnabled(mbIsDocument || mbIsAdjustment);

        if (!mbIsAdjustment) {
            moGridDpsRows.getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonShowDependentDocs);
        }
        else {
            moGridDpsRows.getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonShowAdjustedDoc);
            moGridDpsRows.getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonAdjustmentForMoney);
            moGridDpsRows.getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonAdjustmentForStock);
            moGridDpsRows.getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjButtonAdjustmentDoc);
            moGridDpsRows.getPanelCommandsSys(DGuiConsts.PANEL_CENTER).add(mjTextCurrentAdjustmentType);
        }

        jpRows1.add(moGridDpsRows, BorderLayout.CENTER);

        mvFormGrids.add(moGridDpsNotes);
        mvFormGrids.add(moGridDpsRows);
    }
    
    private void freeLockByCancel() {
        if (moRegistryLock != null) {
            try {
                DLockUtils.freeLock(miClient.getSession(), moRegistryLock, DDbLock.LOCK_ST_FREED_CANCEL);
            }
            catch (Exception e) {
                DLibUtils.showException(this, e);
            }
            finally {
                moRegistryLock = null;
            }
        }
    }

    private void evaluateIsDpsSource() {
        mbIsDpsSource = false;

        if (!moRegistry.isRegistryNew()) {
            for (DDbDpsRow row : moRegistry.getChildRows()) {
                if (!row.isDeleted()) {
                    if (row.getDbDependentDpsRowKeys().size() > 0) {
                        mbIsDpsSource = true;
                        break;
                    }
                }
            }
        }
    }

    private void computeIsPos() {
        if (mbIsPosModule) {
            moDateDate.setEditable(false);
        }
    }

    private void computeCreditInfo() {
        if (moDateCredit.getValue() == null) {
            moDateCredit.setValue(moDateDate.getValue() == null ? mtOriginalDate : moDateDate.getValue());
        }

        jtfDateMaturity.setText(DLibUtils.DateFormatDate.format(DLibTimeUtils.addDate(moDateCredit.getValue(), 0, 0, moIntCreditDays.getValue())));
        jtfDateMaturity.setCaretPosition(0);

        String paymentTerms = "";
        
        if (moKeyPaymentType.getSelectedIndex() > 0) {
            paymentTerms = DTrnDfrUtils.composeCfdiPaymentTerms(moKeyPaymentType.getValue()[0], moIntCreditDays.getValue());
        }
        
        jtfDfrPaymentTerms.setText(paymentTerms);
        jtfDfrPaymentTerms.setCaretPosition(0);
    }
    
    /**
     * This method must be called only for new registries on method setRegistry().
     */
    private boolean computeNewDpsNumber(final int[] dpsTypeKey) {
        Vector<DDbDpsSeriesBranch> dpsSeriesBranches = null;
        Vector<DDbDpsSeriesNumber> dpsSeriesNumbers = null;

        if (DTrnUtils.isDpsNumberAutomatic(new int[] { dpsTypeKey[0], dpsTypeKey[1] })) {
            try {
                // Document number must be defined by system:

                dpsSeriesBranches = ((DDbUser) miClient.getSession().getUser()).getAuxBranchDpsSeries(moSessionCustom.getBranchKey(), dpsTypeKey);

                // If document is being copied, check if original document series is valid:

                if (moRegistry.getNumber() != 0) {
                    for (DDbDpsSeriesBranch dpsSeriesBranch : dpsSeriesBranches) {
                        if (moRegistry.getSeries().compareTo(dpsSeriesBranch.getDbSeries()) == 0) {
                            mnNewDpsSeriesId = dpsSeriesBranch.getPkSeriesId();
                            msNewDpsSeries = dpsSeriesBranch.getDbSeries();
                            break;
                        }
                    }

                    if (mnNewDpsSeriesId == 0) {
                        // Original document series is not allowed:

                        mbCanShowForm = false;
                        msCanShowFormMessage = DUtilConsts.ERR_MSG_DPS_SER_NOT_ASIGNED;
                        return false;
                    }
                }

                if (dpsSeriesBranches.isEmpty()) {
                    // No document series found:

                    mbCanShowForm = false;
                    msCanShowFormMessage = DUtilConsts.ERR_MSG_DPS_SER_NON_AVA;
                    return false;
                }
                else if (dpsSeriesBranches.size() == 1) {
                    // One document series found:

                    mnNewDpsSeriesId = dpsSeriesBranches.get(0).getPkSeriesId();
                    msNewDpsSeries = dpsSeriesBranches.get(0).getDbSeries();
                }
                else {
                    // Check if default document series is set in user session:

                    if (moSessionCustom.getBranchDpsSeriesKey() != null) {
                        for (DDbDpsSeriesBranch dpsSeriesBranch : dpsSeriesBranches) {
                            if (DLibUtils.compareKeys(moSessionCustom.getBranchDpsSeriesKey(), dpsSeriesBranch.getPrimaryKey())) {
                                mnNewDpsSeriesId = dpsSeriesBranch.getPkSeriesId();
                                msNewDpsSeries = dpsSeriesBranch.getDbSeries();
                                break;
                            }
                        }
                    }

                    if (mnNewDpsSeriesId == 0) {
                        // Pick document series:

                        moDialogSelectDpsSeries.setRegistry((DDbUser) miClient.getSession().getUser());
                        moDialogSelectDpsSeries.setVisible(true);

                        if (moDialogSelectDpsSeries.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                            mnNewDpsSeriesId = (Integer) moDialogSelectDpsSeries.getValue(DModSysConsts.PARAM_SER_ID);
                            msNewDpsSeries = (String) moDialogSelectDpsSeries.getValue(DModSysConsts.PARAM_SER_SER);
                        }
                        else {
                            mbCanShowForm = false;
                            msCanShowFormMessage = DUtilConsts.ERR_MSG_DPS_SER_NON_DEF;
                            return false;
                        }
                    }
                }

                /*
                 * ALG#001. Define and validate new document automatic number:
                 */

                dpsSeriesNumbers = ((DDbUser) miClient.getSession().getUser()).getAuxBranchDpsSeriesNumbers(mnNewDpsSeriesId);

                if (dpsSeriesNumbers.isEmpty()) {
                    mbCanShowForm = false;
                    msCanShowFormMessage = DUtilConsts.ERR_MSG_DPS_SER_NUM_NON_AVA;
                    return false;
                }
                else if (dpsSeriesNumbers.size() > 1) {
                    mbCanShowForm = false;
                    msCanShowFormMessage = DUtilConsts.ERR_MSG_DPS_SER_NUM_MUL_AVA;
                    return false;
                }
                else {
                    mnNewDpsNumber = DTrnUtils.getNextNumberForDps(miClient.getSession(), dpsTypeKey, msNewDpsSeries);

                    if (mnNewDpsNumber < dpsSeriesNumbers.get(0).getNumberStart()) {
                        mnNewDpsNumber = dpsSeriesNumbers.get(0).getNumberStart();
                    }
                    else if (dpsSeriesNumbers.get(0).getNumberEnd_n() > 0 && mnNewDpsNumber > dpsSeriesNumbers.get(0).getNumberEnd_n()) {
                        mbCanShowForm = false;
                        msCanShowFormMessage = DUtilConsts.ERR_MSG_DPS_SER_NUM_MAX;
                        return false;
                    }
                }

                /*
                 * End of algorithm.
                 */
            }
            catch (Exception e) {
                DLibUtils.showException(this, e);
                return false;
            }
        }

        return true;
    }
    
    private int getDocumentTabbedPaneIndex(final JComponent component) {
        int index = -1;
        
        if (component == moKeyBranchAddress || component == moTextOrder || component == moDateDelivery || component == moTextImportDeclaration || component == moDateImportDeclarationDate) {
            index = DOC_TAB_BP;
        }
        else if (component == moTextDfrConfirmation || component == moKeyDfrIssuerTaxRegime) {
            index = DOC_TAB_DFR;
        }
        else if (component == moKeyDfrGlobalPeriodicity || component == moKeyDfrGlobalMonths || component == moCalDfrGlobalYear) {
            index = DOC_TAB_GBL;
        }
        
        return index;
    }
    
    private void clearRow() {
        renderItem(null, 0);

        moTextFind.resetField();
        moBoolRowNote.resetField();
        itemStateChangedRowNote();
        moBoolRowCfdPredial.resetField();
        itemStateChangedRowPredial();

        mvRowStockMoves.clear();

        if (mbIsAdjustment) {
            setRowFieldsEditable(false);

            manCurrentAdjustmentClassKey = null;
            manCurrentAdjustmentTypeKey = null;
            manCurrentAdjustedDpsKey = null;
            msCurrentAdjustedDpsUuid = "";
            mjTextCurrentAdjustmentType.setText("");
        }
    }

    private void computeRowFocus() {
        if (moTextRowCode.isEditable()) {
            moTextRowCode.requestFocusInWindow();
        }
        else if (moTextRowName.isEditable()) {
            moTextRowName.requestFocusInWindow();
        }
        else if (moDecRowQuantity.isEnabled()) {
            moDecRowQuantity.requestFocusInWindow();
        }
        else if (moDecRowPriceUnitary.isEnabled()) {
            moDecRowPriceUnitary.requestFocusInWindow();
        }
        else if (moDecRowSubtotalProv.isEnabled()) {
            moDecRowSubtotalProv.requestFocusInWindow();
        }
        else if (moDecRowDiscountDoc.isEnabled()) {
            moDecRowDiscountDoc.requestFocusInWindow();
        }
        else if (moDecRowSubtotal.isEnabled()) {
            moDecRowSubtotal.requestFocusInWindow();
        }
        else {
            jbRowAdd.requestFocusInWindow();
        }
    }

    private void computeRowPriceUnitary() {
        double qty = !moDecRowQuantity.isEnabled() ? 1 : moDecRowQuantity.getValue(); // when adjustments for money, quantity is disabled
        
        // backwards calculation:
        double stot = moDecRowSubtotal.getValue();
        double discDoc = moDecRowDiscountDoc.getValue();
        double stotProv = DLibUtils.round(stot + discDoc, DLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
        double prcUnt = qty == 0 ? 0 : DLibUtils.round(stotProv / qty, DLibUtils.getDecimalFormatAmountUnitary().getMaximumFractionDigits());

        moDecRowSubtotalProv.setValue(stotProv);
        moDecRowPriceUnitary.setValue(prcUnt);
    }

    private void computeRowTotal() {
        double qty = !moDecRowQuantity.isEnabled() ? 1 : moDecRowQuantity.getValue(); // when adjustments for money, quantity is disabled
        
        // forwards calculation:
        double prcUnt = moDecRowPriceUnitary.getValue();
        double stotProv = DLibUtils.round(qty * prcUnt, DLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
        double discDoc = DLibUtils.round(moBoolDiscountDocPercentageApplying.getValue() ? stotProv * moDecDiscountDocPercentage.getValue() : moDecRowDiscountDoc.getValue(), DLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());
        double stot = DLibUtils.round(stotProv - discDoc, DLibUtils.getDecimalFormatAmount().getMaximumFractionDigits());

        moDecRowSubtotalProv.setValue(stotProv);
        moDecRowSubtotal.setValue(stot);
    }

    private void computeTotal() {
        moRegistry.setDiscountDocApplying(moBoolDiscountDocApplying.getValue());
        moRegistry.setDiscountDocPercentageApplying(moBoolDiscountDocPercentageApplying.getValue());
        moRegistry.setDiscountDocPercentage(moDecDiscountDocPercentage.getValue());
        moRegistry.setExchangeRate(moDecExchangeRate.getValue());

        moRegistry.computeTotal();

        moCurSubtotalProv.getField().setValue(moRegistry.getSubtotalProvCy_r());
        moCurDiscountDoc.getField().setValue(moRegistry.getDiscountDocCy_r());
        moCurSubtotal.getField().setValue(moRegistry.getSubtotalCy_r());
        moCurTaxCharged.getField().setValue(moRegistry.getTaxChargedCy_r());
        moCurTaxRetained.getField().setValue(moRegistry.getTaxRetainedCy_r());
        moCurTotal.getField().setValue(moRegistry.getTotalCy_r());

        jtfTotalQuantity.setText(moConfigCompany.getDecimalFormatQuantity().format(moRegistry.getAuxTotalQuantity()));

        evaluateIsDpsSource();

        if (!mbIsAdjustment) {
            moKeyCurrency.setEnabled(!mbIsDpsSource && moKeyBizPartner.getSelectedIndex() > 0);
        }

        if (moRegistry.getActiveRowsCount() == 0) {
            moKeyBizPartner.setEnabled(true);
            moKeyPaymentType.setEnabled(enablePaymentTypeField());
            jbBizPartnerPick.setEnabled(true);
            jbBizPartnerEdit.setEnabled(true);

            if (mbIsAdjustment) {
                moKeyCurrency.setEnabled(true);
            }
        }
        else {
            moKeyBizPartner.setEnabled(false);
            moKeyPaymentType.setEnabled(false);
            jbBizPartnerPick.setEnabled(false);
            jbBizPartnerEdit.setEnabled(false);

            if (mbIsAdjustment) {
                moKeyCurrency.setEnabled(false);
            }
        }
    }
    
    private void populateBranchAddresses() {
        if (moKeyBizPartner.getSelectedIndex() <= 0) {
            moKeyBranchAddress.removeAllItems();
        }
        else {
            miClient.getSession().populateCatalogue(moKeyBranchAddress, DModConsts.BU_ADD, 0, new DGuiParams(moBizPartnerBranchHeadquarters.getPrimaryKey()));

            // select default branch address:
            for (int i = 0; i < moKeyBranchAddress.getItemCount(); i++) {
                Object complement = ((DGuiItem) moKeyBranchAddress.getItemAt(i)).getComplement();
                if (complement != null && complement instanceof Boolean && (Boolean) complement) {
                    moKeyBranchAddress.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private boolean isCurrentAdjustmentForMoney() {
        return mbIsAdjustment && DTrnUtils.isAdjustmentForMoney(manCurrentAdjustmentClassKey);
    }

    private boolean isStockMovesInputNeeded() {
        return moItem.isInventoriable() && (mbIsDocument || mbIsAdjustment) && !isCurrentAdjustmentForMoney();
    }

    private boolean enablePaymentTypeField() {
        return !mbIsPosModule && !mbIsAdjustment && moBizPartnerConfig != null && moBizPartnerConfig.getActualFkCreditTypeId() != DModSysConsts.BS_CDT_TP_CDT_NON;
    }

    /**
     * Check if DFR fields must be enabled. Should be invoked after initializing member moDpsSeriesNumber.
     * @return 
     */
    private boolean enableDfrFields() {
        return mnFormSubtype == DModSysConsts.TS_DPS_CT_SAL && (mbIsDocument || mbIsAdjustment) && 
                DLibUtils.belongsTo(moDpsSeriesNumber.getFkXmlTypeId(), new int[] { DModSysConsts.TS_XML_TP_CFDI_33, DModSysConsts.TS_XML_TP_CFDI_40 });
    }

    private boolean enableDfrGlobalFields() {
        return enableDfrFields() && (moBizPartner != null && moBizPartner.isPublicForDfr());
    }

    private boolean isSalesAdjustment() {
        return mnFormSubtype == DModSysConsts.TS_DPS_CT_SAL && mbIsAdjustment;
    }

    private boolean validateCoreData() {
        boolean validate = true;

        if (moKeyBizPartner.getSelectedIndex() <= 0) {
            validate = false;
            miClient.showMsgBoxWarning(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moKeyBizPartner.getFieldName() + "'.");
            moKeyBizPartner.requestFocusInWindow();
        }
        else if (moKeyBranchAddress.getSelectedIndex() <= 0) {
            validate = false;
            miClient.showMsgBoxWarning(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moKeyBranchAddress.getFieldName() + "'.");
            moKeyBranchAddress.requestFocusInWindow();
        }
        else if (moDateDate.getValue() == null) {
            validate = false;
            miClient.showMsgBoxWarning(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + DGuiUtils.getLabelName(jlDateDate.getText()) + "'.");
            moDateDate.getComponent().requestFocusInWindow();
        }
        else if (moKeyCurrency.getSelectedIndex() <= 0) {
            validate = false;
            miClient.showMsgBoxWarning(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + DGuiUtils.getLabelName(jlCurrency.getText()) + "'.");
            moKeyCurrency.requestFocusInWindow();
        }
        else if (moDecExchangeRate.getValue() == 0) {
            validate = false;
            miClient.showMsgBoxWarning(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + DGuiUtils.getLabelName(jlExchangeRate.getText()) + "'.");
            moDecExchangeRate.requestFocusInWindow();
        }
        else if (moKeyPaymentType.getSelectedIndex() <= 0) {
            validate = false;
            miClient.showMsgBoxWarning(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + DGuiUtils.getLabelName(jlPaymentType.getText()) + "'.");
            moKeyPaymentType.requestFocusInWindow();
        }
        else {
            moRegistry.setDate(moDateDate.getValue());
            moRegistry.setExchangeRate(moDecExchangeRate.getValue());
            moRegistry.setFkCurrencyId(moKeyCurrency.getValue()[0]);
            moRegistry.setFkPaymentTypeId(moKeyPaymentType.getValue()[0]);
            moRegistry.setFkBizPartnerBizPartnerId(moKeyBranchAddress.getValue()[0]);
            moRegistry.setFkBizPartnerBranchId(moKeyBranchAddress.getValue()[1]);
            moRegistry.setFkBizPartnerAddressId(moKeyBranchAddress.getValue()[2]);
        }

        return validate;
    }

    private boolean canAddRow() {
        boolean can = true;

        if (moDpsSeries != null && moDpsSeries.getRowMaximum() > 0 && moGridDpsRows.getModel().getRowCount() >= moDpsSeries.getRowMaximum()) {
            can = false;
            miClient.showMsgBoxWarning("El número máximo de renglones es " + DLibUtils.DecimalFormatInteger.format(moDpsSeries.getRowMaximum()) + ".");
        }

        return can;
    }

    @SuppressWarnings("unchecked")
    private boolean defineRowStockMoves(final double quantity, final double[] prices) {
        boolean valid = true;
        boolean defined = true;
        double stock = 0;
        int[] key = null;
        DTrnStockMove stockMove = null;
        ArrayList<DTrnStockMove> currentStockMoves = null;

        mbQuantityAlreadySet = false;

        if (moItem.isLotApplying()) {
            key = new int[] { moItem.getPkItemId(), moUnit.getPkUnitId() };

            if (DTrnUtils.isDpsForStockOut(moRegistry, true) && mvRowStockMoves.isEmpty()) {
                // Propouse older stock lots:

                currentStockMoves = new ArrayList<>();

                for (DDbDpsRow row : moRegistry.getChildRows()) {
                    if (!row.isDeleted()) {
                        currentStockMoves.addAll(row.getAuxStockMoves());
                    }
                }

                try {
                    mvRowStockMoves.addAll(DTrnUtils.getAvailableLots(miClient.getSession(), DLibTimeUtils.digestYear(moDateDate.getValue())[0],
                            moItem.getPkItemId(), moUnit.getPkUnitId(), moRegistry.getBranchWarehouseKey_n(), moRegistry.getXtaIogId(), currentStockMoves, quantity));
                }
                catch (Exception e) {
                    DLibUtils.showException(this, e);
                }
            }

            moDialogLot.resetForm();
            moDialogLot.setValue(DModSysConsts.PARAM_DATE, moDateDate.getValue());
            moDialogLot.setValue(DModSysConsts.PARAM_ITM_UNT, key);
            moDialogLot.setValue(DModSysConsts.PARAM_BRA_WAH, moRegistry.getBranchWarehouseKey_n());
            moDialogLot.setValue(DModSysConsts.PARAM_BKK_NUM_KEY, moRegistry.getBookkeepingNumberKey_n());
            moDialogLot.setValue(DModSysConsts.PARAM_QTY, quantity);
            moDialogLot.setValue(DModSysConsts.PARAM_PRC, prices);
            moDialogLot.setValue(DModSysConsts.PARAM_CUR, moKeyCurrency.getSelectedItem().getCode());
            moDialogLot.setValue(DModSysConsts.PARAM_VEC_STK_MOV, mvRowStockMoves);
            moDialogLot.setVisible(true);

            if (moDialogLot.getFormResult() != DGuiConsts.FORM_RESULT_OK) {
                defined = false;
            }
            else {
                mvRowStockMoves.clear();
                mvRowStockMoves.addAll((Vector<DTrnStockMove>) moDialogLot.getValue(DModSysConsts.PARAM_VEC_STK_MOV));
            }
        }
        else if (moItem.isSerialNumberApplying()) {
            key = new int[] { moItem.getPkItemId(), moUnit.getPkUnitId(), DUtilConsts.LOT_ID,
                moRegistry.getFkWarehouseBizPartnerId_n(), moRegistry.getFkWarehouseBranchId_n(), moRegistry.getFkWarehouseWarehouseId_n() };

            switch (moItem.getParentGenus().getFkSerialNumberTypeId()) {
                case DModSysConsts.IS_SNR_TP_SMP:
                    // Simple serial number:

                    for (DTrnStockMove move : mvRowStockMoves) {
                        move.setPkItemId(key[0]);
                        move.setPkUnitId(key[1]);
                        move.setPkLotId(key[2]);
                        move.setPkBizPartnerId(key[3]);
                        move.setPkBranchId(key[4]);
                        move.setPkWarehouseId(key[5]);
                    }

                    while (defined) {
                        moDialogSerialNumber.resetForm();
                        moDialogSerialNumber.setValue(DModSysConsts.PARAM_DATE, moDateDate.getValue());
                        moDialogSerialNumber.setValue(DModSysConsts.PARAM_LOT_KEY, key);
                        moDialogSerialNumber.setValue(DModSysConsts.PARAM_BRA_WAH, moRegistry.getBranchWarehouseKey_n());
                        moDialogSerialNumber.setValue(DModSysConsts.PARAM_BKK_NUM_KEY, moRegistry.getBookkeepingNumberKey_n());
                        moDialogSerialNumber.setValue(DModSysConsts.PARAM_QTY, new Double(quantity).intValue());
                        moDialogSerialNumber.setValue(DModSysConsts.PARAM_PRC, prices);
                        moDialogSerialNumber.setValue(DModSysConsts.PARAM_CUR, moKeyCurrency.getSelectedItem().getCode());
                        moDialogSerialNumber.setValue(DModSysConsts.PARAM_VEC_STK_MOV, mvRowStockMoves);
                        moDialogSerialNumber.setVisible(true);

                        if (moDialogSerialNumber.getFormResult() != DGuiConsts.FORM_RESULT_OK) {
                            defined = false;
                        }
                        else {
                            // Validate serial numbers:

                            valid = true;
                            mvRowStockMoves.clear();
                            mvRowStockMoves.addAll((Vector<DTrnStockMove>) moDialogSerialNumber.getValue(DModSysConsts.PARAM_VEC_STK_MOV));

                            DPS:
                            for (DDbDpsRow row : moRegistry.getChildRows()) {
                                if (!row.isDeleted() && row.getFkRowItemId() == moItem.getPkItemId()) {
                                    for (DTrnStockMove moveRow : row.getAuxStockMoves()) {
                                        for (DTrnStockMove moveNew : mvRowStockMoves) {
                                            if (moveRow.getSerialNumber().compareTo(moveNew.getSerialNumber()) == 0) {
                                                valid = false;
                                                miClient.showMsgBoxWarning("El número de serie '" + moveNew.getSerialNumber() + "' ya está en el documento.");
                                                break DPS;
                                            }
                                        }
                                    }
                                }
                            }

                            if (valid) {
                                break;
                            }
                        }
                    }
                    break;

                case DModSysConsts.IS_SNR_TP_CMP:
                    // Compound serial number:

                    if (DTrnUtils.isDpsClassForStockIn(moRegistry.getDpsClassKey())) {
                        // Incoming items:

                        while (defined) {
                            moDialogSerialNumberCompound.resetForm();
                            moDialogSerialNumberCompound.setValue(DModSysConsts.PARAM_LOT_KEY, key);
                            moDialogSerialNumberCompound.setValue(DModSysConsts.PARAM_QTY, new Double(quantity).intValue());
                            moDialogSerialNumberCompound.setValue(DModSysConsts.PARAM_PRC, prices);
                            moDialogSerialNumberCompound.setValue(DModSysConsts.PARAM_CUR, moKeyCurrency.getSelectedItem().getCode());
                            moDialogSerialNumberCompound.setVisible(true);

                            if (moDialogSerialNumberCompound.getFormResult() != DGuiConsts.FORM_RESULT_OK) {
                                defined = false;
                            }
                            else {
                                // Validate serial numbers:

                                valid = true;
                                key = new int[] { moItem.getPkItemId(), moUnit.getPkUnitId(), DUtilConsts.LOT_ID,
                                    moRegistry.getFkWarehouseBizPartnerId_n(), moRegistry.getFkWarehouseBranchId_n(), moRegistry.getFkWarehouseWarehouseId_n() };

                                mvRowStockMoves.clear();
                                mvRowStockMoves.add(new DTrnStockMove(key, ((Number) moDialogSerialNumberCompound.getValue(DModSysConsts.PARAM_QTY)).doubleValue(), (String) moDialogSerialNumberCompound.getValue(DModSysConsts.PARAM_SNR)));

                                DPS:
                                for (DDbDpsRow row : moRegistry.getChildRows()) {
                                    if (!row.isDeleted() && row.getFkRowItemId() == moItem.getPkItemId()) {
                                        for (DTrnStockMove moveRow : row.getAuxStockMoves()) {
                                            for (DTrnStockMove moveNew : mvRowStockMoves) {
                                                if (moveRow.getSerialNumber().compareTo(moveNew.getSerialNumber()) == 0) {
                                                    valid = false;
                                                    miClient.showMsgBoxWarning("El número de serie '" + moveNew.getSerialNumber() + "' ya está en el documento.");
                                                    break DPS;
                                                }
                                            }
                                        }
                                    }
                                }

                                if (valid) {
                                    moDecRowQuantity.setValue(mvRowStockMoves.get(0).getQuantity());
                                    mbQuantityAlreadySet = true;
                                    break;
                                }
                            }
                        }
                    }
                    else {
                        // Outgoing items:

                        while (defined) {
                            moDialogSerialNumberInStock.resetForm();
                            moDialogSerialNumberInStock.setValue(DModSysConsts.PARAM_DATE, moDateDate.getValue());
                            moDialogSerialNumberInStock.setValue(DModSysConsts.PARAM_LOT_KEY, key);
                            moDialogSerialNumberInStock.setValue(DModSysConsts.PARAM_BRA_WAH, moRegistry.getBranchWarehouseKey_n());
                            moDialogSerialNumberInStock.setValue(DModSysConsts.PARAM_BKK_NUM_KEY, moRegistry.getBookkeepingNumberKey_n());
                            //moDialogSerialNumberInStock.setValue(DModSysConsts.PARAM_VEC_STK_MOV_ADJ, ?); // serial numbers available to adjust
                            moDialogSerialNumberInStock.setValue(DModSysConsts.PARAM_VEC_STK_MOV, mvRowStockMoves); // serial numbers in document
                            moDialogSerialNumberInStock.setVisible(true);

                            if (moDialogSerialNumberInStock.getFormResult() != DGuiConsts.FORM_RESULT_OK) {
                                defined = false;
                            }
                            else {
                                // Validate serial numbers:

                                valid = true;
                                key = new int[] { moItem.getPkItemId(), moUnit.getPkUnitId(), DUtilConsts.LOT_ID,
                                    moRegistry.getFkWarehouseBizPartnerId_n(), moRegistry.getFkWarehouseBranchId_n(), moRegistry.getFkWarehouseWarehouseId_n() };

                                stockMove = (DTrnStockMove) moDialogSerialNumberInStock.getValue(DModSysConsts.PARAM_OBJ_STK_MOV);
                                mvRowStockMoves.clear();
                                mvRowStockMoves.add(new DTrnStockMove(key, stockMove.getQuantity(), stockMove.getSerialNumber()));

                                DPS:
                                for (DDbDpsRow row : moRegistry.getChildRows()) {
                                    if (!row.isDeleted() && row.getFkRowItemId() == moItem.getPkItemId()) {
                                        for (DTrnStockMove moveRow : row.getAuxStockMoves()) {
                                            for (DTrnStockMove moveNew : mvRowStockMoves) {
                                                if (moveRow.getSerialNumber().compareTo(moveNew.getSerialNumber()) == 0) {
                                                    valid = false;
                                                    miClient.showMsgBoxWarning("El número de serie '" + moveNew.getSerialNumber() + "' ya está en el documento.");
                                                    break DPS;
                                                }
                                            }
                                        }
                                    }
                                }

                                if (valid) {
                                    moDecRowQuantity.setValue(mvRowStockMoves.get(0).getQuantity());
                                    mbQuantityAlreadySet = true;
                                    break;
                                }
                            }
                        }
                    }
                    break;

                default:
                    miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
        else {
            key = new int[] { moItem.getPkItemId(), moUnit.getPkUnitId(), DUtilConsts.LOT_ID };

            if (mnIogCategory == DModSysConsts.TS_IOG_CT_OUT) {
                // Validate stock for outgoing items:

                stock = DTrnUtils.getStockForLot(miClient.getSession(), mnOriginalYear, key, moRegistry.getBranchWarehouseKey_n());

                if (quantity > stock) {
                    defined = false;
                    miClient.showMsgBoxWarning("No se puede procesar el ítem '" + moItem.getName() + "'.\n" +
                                    "No hay existencias suficientes en el almacén del documento:\n" +
                                    "- unidades requeridas: " + DLibUtils.getDecimalFormatQuantity().format(quantity) + " " + moUnit.getCode() + "\n" +
                                    "- unidades en existencia: " + DLibUtils.getDecimalFormatQuantity().format(stock) + " " + moUnit.getCode() + "");
                    moDecRowQuantity.requestFocusInWindow();
                }
            }

            if (defined) {
                key = new int[] { moItem.getPkItemId(), moUnit.getPkUnitId(), DUtilConsts.LOT_ID,
                    moRegistry.getFkWarehouseBizPartnerId_n(), moRegistry.getFkWarehouseBranchId_n(), moRegistry.getFkWarehouseWarehouseId_n() };

                mvRowStockMoves.clear();
                mvRowStockMoves.add(new DTrnStockMove(key, quantity, ""));
            }
        }

        return defined;
    }

    private boolean renderItem(final int[] itemKey_n, final double quantity) {
        double taxRate = 0;
        double price = 0;
        double[] prices = null;
        boolean render = true;
        boolean canChangePrice = false;
        boolean canChangeDiscount = false;
        DDbTaxGroupConfigRow taxGroupConfigRow = null;

        if (itemKey_n == null) {
            moItem = null;
            moUnit = null;

            moTextRowCode.setToolTipText(DGuiConsts.TXT_LBL_CODE);
            moTextRowName.setToolTipText(DGuiConsts.TXT_LBL_NAME);
            jtfRowUnitCode.setToolTipText(DGuiConsts.TXT_LBL_UNIT);

            jtfRowCfdItemKey.setText("");
            jtfRowUnitCode.setText("");
            jtfRowCfdUnitKey.setText("");

            moTextRowCode.setEditable(false);
            moTextRowName.setEditable(false);
            moDecRowQuantity.setEnabled(false);
            moDecRowPriceUnitary.setEnabled(false);
            moDecRowSubtotalProv.setEnabled(false);
            moDecRowDiscountDoc.setEnabled(false);
            moDecRowSubtotal.setEnabled(false);

            moTextRowCode.resetField();
            moTextRowName.resetField();
            moDecRowQuantity.resetField();
            moDecRowPriceUnitary.resetField();
            moDecRowSubtotalProv.resetField();
            moDecRowDiscountDoc.resetField();
            moDecRowSubtotal.resetField();
        }
        else {
            moItem = (DDbItem) miClient.getSession().readRegistry(DModConsts.IU_ITM, itemKey_n);
            moUnit = (DDbUnit) miClient.getSession().readRegistry(DModConsts.IU_UNT, new int[] { moItem.getFkUnitId() });

            // Define item prices:

            switch (mnFormSubtype) {
                case DModSysConsts.TS_DPS_CT_PUR:
                    prices = DTrnUtils.getItemLastPrices(miClient.getSession(), mnFormSubtype, moKeyCurrency.getValue(), moItem.getPrimaryKey(), moBizPartner.getPrimaryKey());
                    break;
                case DModSysConsts.TS_DPS_CT_SAL:
                    price = moDecExchangeRate.getValue() == 0 ? 0 : DTrnUtils.getItemSalesPrice(miClient.getSession(), moItem, moBizPartner.getPrimaryKey(), moKeyPaymentType.getValue()[0], mnBizPartnerItemPriceType, mdBizPartnerDiscountPercentage) / moDecExchangeRate.getValue();
                    prices = new double[] { price, 0, 0 };
                    break;
                default:
            }

            if (moBoolRowTaxInput.isSelected()) {
                // Item prices must include taxes:

                taxGroupConfigRow = DTrnUtils.getTaxGroupConfigRow(
                        miClient.getSession(),
                        DTrnUtils.getTaxGroupForItem(miClient.getSession(), moItem.getPrimaryKey()),
                        new int[] { moBizPartnerBranchHeadquarters.getActualFkTaxRegionId(miClient.getSession()) },
                        moBizPartner.getFkIdentityTypeId(),
                        moDateDate.getValue());

                taxRate = DTrnUtils.computeTaxRate(taxGroupConfigRow, mnFormSubtype);
                prices[0] *= taxRate;
                prices[1] *= taxRate;
                prices[2] *= taxRate;
            }

            if (!isStockMovesInputNeeded()) {
                mvRowStockMoves.clear();
            }
            else {
                render = defineRowStockMoves(quantity, prices);
            }

            if (!render) {
                clearRow();
            }
            else {
                if (mbIsPosModule) {
                    canChangePrice = mbCanChangePricePos;
                    canChangeDiscount = mbCanChangeDiscountPos;
                }
                else {
                    canChangePrice = mbCanChangePrice;
                    canChangeDiscount = mbCanChangeDiscount;
                }

                moTextRowCode.setEditable(moItem.getParentGenus().isCodeEditable());
                moTextRowName.setEditable(moItem.getParentGenus().isNameEditable());
                moDecRowQuantity.setEnabled(!isCurrentAdjustmentForMoney());
                moDecRowPriceUnitary.setEnabled(canChangePrice || prices[0] == 0d);
                moDecRowSubtotalProv.setEnabled(canChangePrice);
                moDecRowDiscountDoc.setEnabled(canChangeDiscount && !moItem.isFreeOfDiscount() && moBoolDiscountDocApplying.getValue() && !moBoolDiscountDocPercentageApplying.getValue());
                moDecRowSubtotal.setEnabled(canChangePrice);

                moTextRowCode.setValue(moItem.getCode());
                moTextRowName.setValue(moItem.getName());
                jtfRowCfdItemKey.setText(isSalesAdjustment() ? DCfdi40Catalogs.ClaveProdServServsFacturacion : moItem.getActualCfdItemKey());
                jtfRowCfdItemKey.setCaretPosition(0);
                jtfRowUnitCode.setText(moUnit.getCode());
                jtfRowUnitCode.setCaretPosition(0);
                jtfRowCfdUnitKey.setText(isSalesAdjustment() ? DCfdi40Catalogs.ClaveUnidadAct : moUnit.getCfdUnitKey());
                jtfRowCfdUnitKey.setCaretPosition(0);

                moTextRowCode.setToolTipText(DGuiConsts.TXT_LBL_CODE + ": " + moItem.getCode());
                moTextRowName.setToolTipText(DGuiConsts.TXT_LBL_NAME + ": " + moItem.getName());
                jtfRowUnitCode.setToolTipText(DGuiConsts.TXT_LBL_UNIT + ": " + moUnit.getCode());

                if (!mbQuantityAlreadySet) {
                    moDecRowQuantity.setValue(!isCurrentAdjustmentForMoney() ? quantity : 0);
                }

                moDecRowPriceUnitary.setValue(prices[0]);
                //moDecRowDiscountUnitary.setValue(prices[1]);
                //moDecRowDiscountRow.setValue(prices[2]);
            }
        }

        return render;
    }
    
    private DDbDpsRow createDpsRow() {
        double taxRate = 0;
        double qty = moDecRowQuantity.getValue();
        double prcUnt = moDecRowPriceUnitary.getValue();
        double discDoc = moDecRowDiscountDoc.getValue();
        DDbDpsRow dpsRow = new DDbDpsRow();
        DDbTaxGroupConfigRow taxGroupConfigRow = null;

        taxGroupConfigRow = DTrnUtils.getTaxGroupConfigRow(
                miClient.getSession(),
                DTrnUtils.getTaxGroupForItem(miClient.getSession(), moItem.getPrimaryKey()),
                new int[] { moBizPartnerBranchHeadquarters.getActualFkTaxRegionId(miClient.getSession()) },
                moBizPartner.getFkIdentityTypeId(),
                moDateDate.getValue());

        if (moBoolRowTaxInput.getValue()) {
            // Subtract taxes:

            taxRate = DTrnUtils.computeTaxRate(taxGroupConfigRow, mnFormSubtype);
            prcUnt = DTrnUtils.computePrice(prcUnt, taxRate);
            discDoc = DTrnUtils.computePrice(discDoc, taxRate);
        }

        dpsRow.setPkDpsId(0);
        dpsRow.setPkRowId(0);
        dpsRow.setCode(moTextRowCode.getText());
        dpsRow.setName(moTextRowName.getText());
        dpsRow.setPredial(moTextRowCfdPredial.getText());
        dpsRow.setSortingPos(0);
        dpsRow.setDiscountDocApplying(true); // XXX improve this!, set discount only if item settings allow it
        //dpsRow.setDiscountDocApplying(!moItem.isFreeOfDiscount());
        dpsRow.setDiscountUnitaryPercentageApplying(false);
        dpsRow.setDiscountRowPercentageApplying(false);
        dpsRow.setDiscountUnitaryPercentage(0);
        dpsRow.setDiscountRowPercentage(0);

        dpsRow.setQuantity(qty);

        dpsRow.setPriceUnitary(0);
        dpsRow.setDiscountUnitary(0);
        dpsRow.setDiscountRow(0);
        dpsRow.setSubtotalProv_r(0);
        dpsRow.setDiscountDoc(0);
        dpsRow.setSubtotal_r(0);
        dpsRow.setTaxCharged_r(0);
        dpsRow.setTaxRetained_r(0);
        dpsRow.setTotal_r(0);
        dpsRow.setPriceUnitaryCalculated_r(0);

        dpsRow.setPriceUnitaryCy(prcUnt);
        dpsRow.setDiscountUnitaryCy(0);
        dpsRow.setDiscountRowCy(0);
        dpsRow.setSubtotalProvCy_r(0);
        dpsRow.setDiscountDocCy(discDoc);
        dpsRow.setSubtotalCy_r(0);
        dpsRow.setTaxChargedCy_r(0);
        dpsRow.setTaxRetainedCy_r(0);
        dpsRow.setTotalCy_r(0);
        dpsRow.setPriceUnitaryCalculatedCy_r(0);

        switch (mnFormSubtype) {
            case DModSysConsts.TS_DPS_CT_PUR:
                dpsRow.setTaxChargedRate1(taxGroupConfigRow.getPurchaseTaxChargedRate1());
                dpsRow.setTaxChargedRate2(taxGroupConfigRow.getPurchaseTaxChargedRate2());
                dpsRow.setTaxChargedRate3(taxGroupConfigRow.getPurchaseTaxChargedRate3());
                dpsRow.setTaxRetainedRate1(taxGroupConfigRow.getPurchaseTaxRetainedRate1());
                dpsRow.setTaxRetainedRate2(taxGroupConfigRow.getPurchaseTaxRetainedRate2());
                dpsRow.setTaxRetainedRate3(taxGroupConfigRow.getPurchaseTaxRetainedRate3());

                dpsRow.setFkTaxCharged1Id(taxGroupConfigRow.getFkPurchaseTaxCharged1Id());
                dpsRow.setFkTaxCharged2Id(taxGroupConfigRow.getFkPurchaseTaxCharged2Id());
                dpsRow.setFkTaxCharged3Id(taxGroupConfigRow.getFkPurchaseTaxCharged3Id());
                dpsRow.setFkTaxRetained1Id(taxGroupConfigRow.getFkPurchaseTaxRetained1Id());
                dpsRow.setFkTaxRetained2Id(taxGroupConfigRow.getFkPurchaseTaxRetained2Id());
                dpsRow.setFkTaxRetained3Id(taxGroupConfigRow.getFkPurchaseTaxRetained3Id());
                break;

            case DModSysConsts.TS_DPS_CT_SAL:
                dpsRow.setTaxChargedRate1(taxGroupConfigRow.getSaleTaxChargedRate1());
                dpsRow.setTaxChargedRate2(taxGroupConfigRow.getSaleTaxChargedRate2());
                dpsRow.setTaxChargedRate3(taxGroupConfigRow.getSaleTaxChargedRate3());
                dpsRow.setTaxRetainedRate1(taxGroupConfigRow.getSaleTaxRetainedRate1());
                dpsRow.setTaxRetainedRate2(taxGroupConfigRow.getSaleTaxRetainedRate2());
                dpsRow.setTaxRetainedRate3(taxGroupConfigRow.getSaleTaxRetainedRate3());

                dpsRow.setFkTaxCharged1Id(taxGroupConfigRow.getFkSaleTaxCharged1Id());
                dpsRow.setFkTaxCharged2Id(taxGroupConfigRow.getFkSaleTaxCharged2Id());
                dpsRow.setFkTaxCharged3Id(taxGroupConfigRow.getFkSaleTaxCharged3Id());
                dpsRow.setFkTaxRetained1Id(taxGroupConfigRow.getFkSaleTaxRetained1Id());
                dpsRow.setFkTaxRetained2Id(taxGroupConfigRow.getFkSaleTaxRetained2Id());
                dpsRow.setFkTaxRetained3Id(taxGroupConfigRow.getFkSaleTaxRetained3Id());
                break;

            default:
        }

        dpsRow.setTaxCharged1(0);
        dpsRow.setTaxCharged2(0);
        dpsRow.setTaxCharged3(0);
        dpsRow.setTaxRetained1(0);
        dpsRow.setTaxRetained2(0);
        dpsRow.setTaxRetained3(0);

        dpsRow.setTaxCharged1Cy(0);
        dpsRow.setTaxCharged2Cy(0);
        dpsRow.setTaxCharged3Cy(0);
        dpsRow.setTaxRetained1Cy(0);
        dpsRow.setTaxRetained2Cy(0);
        dpsRow.setTaxRetained3Cy(0);

        dpsRow.setMeasurementLength(qty * moItem.getMeasurementLength());
        dpsRow.setMeasurementSurface(qty * moItem.getMeasurementSurface());
        dpsRow.setMeasurementVolume(qty * moItem.getMeasurementVolume());
        dpsRow.setMeasurementMass(qty * moItem.getMeasurementMass());
        dpsRow.setMeasurementTime(qty * moItem.getMeasurementTime());
        dpsRow.setWeightGross(qty * moItem.getWeightGross());
        dpsRow.setWeightDelivery(qty * moItem.getWeightDelivery());
        dpsRow.setSurplusPercentage(0);
        dpsRow.setTaxManual(false);
        dpsRow.setInventoriable(moItem.isInventoriable());
        dpsRow.setDeleted(false);
        dpsRow.setSystem(false);

        if (!mbIsAdjustment) {
            dpsRow.setFkAdjustmentCategoryId(DModSysConsts.TS_ADJ_TP_NA_NA[0]);
            dpsRow.setFkAdjustmentClassId(DModSysConsts.TS_ADJ_TP_NA_NA[1]);
            dpsRow.setFkAdjustmentTypeId(DModSysConsts.TS_ADJ_TP_NA_NA[2]);

            dpsRow.setAuxPkAdjustedDpsId(0);
        }
        else {
            dpsRow.setFkAdjustmentCategoryId(manCurrentAdjustmentTypeKey[0]);
            dpsRow.setFkAdjustmentClassId(manCurrentAdjustmentTypeKey[1]);
            dpsRow.setFkAdjustmentTypeId(manCurrentAdjustmentTypeKey[2]);

            dpsRow.setAuxPkAdjustedDpsId(manCurrentAdjustedDpsKey == null ? 0 : manCurrentAdjustedDpsKey[0]);
        }

        dpsRow.setFkRowItemId(moItem.getPkItemId());
        dpsRow.setFkRowUnitId(moUnit.getPkUnitId());
        dpsRow.setFkReferenceItemId_n(0);
        dpsRow.setFkReferenceUnitId_n(0);
        dpsRow.setFkTaxRegionId(moBizPartnerBranchHeadquarters.getActualFkTaxRegionId(miClient.getSession()));

        if (!mbIsAdjustment) {
            dpsRow.setFkSourceDpsId_n(0);
            dpsRow.setFkSourceRowId_n(0);
        }
        else {
            dpsRow.setFkSourceDpsId_n(manCurrentAdjustedDpsKey == null ? 0 : manCurrentAdjustedDpsKey[0]);
            dpsRow.setFkSourceRowId_n(0);
        }

        dpsRow.setFkUserInsertId(0);
        dpsRow.setFkUserUpdateId(0);
        dpsRow.setTsUserInsert(null);
        dpsRow.setTsUserUpdate(null);

        dpsRow.setDfrItemKey(isSalesAdjustment() ? DCfdi40Catalogs.ClaveProdServServsFacturacion : moItem.getActualCfdItemKey());
        dpsRow.setDfrUnitKey(isSalesAdjustment() ? DCfdi40Catalogs.ClaveUnidadAct : moUnit.getCfdUnitKey());
        dpsRow.setDfrSourceUuid(manCurrentAdjustedDpsKey == null ? "" : msCurrentAdjustedDpsUuid);
        
        dpsRow.setDbUnitCode(moUnit.getCode());
        dpsRow.setDbTaxRegimeId(moItem.getFkTaxRegimeId());
        
        if (enableDfrFields() && manCurrentAdjustedDpsKey != null && !msCurrentAdjustedDpsUuid.isEmpty()) {
            addRelatedCfd(DCfdi40Catalogs.ClaveTipoRelaciónNotaCrédito, msCurrentAdjustedDpsUuid);
        }

        return dpsRow;
    }
    
    private void setRowFieldsEditable(final boolean editable) {
        moTextFind.setEnabled(editable);
        jbFind.setEnabled(editable);

        moBoolRowNote.setEnabled(editable);
        moTextRowNote.setEnabled(editable && moBoolRowNote.isSelected());
        moBoolRowNotePrintable.setEnabled(editable && moBoolRowNote.isSelected());
        moBoolRowNoteDfr.setEnabled(editable && moBoolRowNote.isSelected() && (mnFormSubtype == DModSysConsts.TS_DPS_CT_SAL && (mbIsDocument || mbIsAdjustment)));
        moBoolRowCfdPredial.setEnabled(editable);
        moTextRowCfdPredial.setEnabled(editable && moBoolRowCfdPredial.isSelected());
        moBoolRowTaxInput.setEnabled(editable);

        jbRowClear.setEnabled(editable);
        jbRowAdd.setEnabled(editable);
    }

    private void addRelatedCfd(final String relationCode, final String uuid) {
        try {
            if (moRegistry.getXtaDfrMate().getRelations() == null) {
                moRegistry.getXtaDfrMate().setRelations(new DDfrMateRelations());
            }
            
            moRegistry.getXtaDfrMate().getRelations().addRelatedCfd(relationCode, uuid);
            
            jtfDfrCfdRelations.setText(moRegistry.getXtaDfrMate().getRelations().toString());
            jtfDfrCfdRelations.setCaretPosition(0);
    }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }
    
    private void removeRelatedCfd(final String relationCode, final String uuid) {
        try {
            if (moRegistry.getXtaDfrMate().getRelations() != null) {
                moRegistry.getXtaDfrMate().getRelations().removeRelatedCfd(relationCode, uuid);

                jtfDfrCfdRelations.setText(moRegistry.getXtaDfrMate().getRelations().toString());
                jtfDfrCfdRelations.setCaretPosition(0);
            }
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }

    private void computeRowDeleted(DDbDpsRow dpsRow) {
        moRegistry.getChildRows().remove(dpsRow);
        
        boolean isUuidStillUsed = false;
        
        for (DGridRow row : moGridDpsRows.getModel().getGridRows()) {
            if (((DDbDpsRow) row).getDfrSourceUuid().equals(dpsRow.getDfrSourceUuid())) {
                isUuidStillUsed = true;
                break;
            }
        }

        if (!isUuidStillUsed) {
            if (enableDfrFields() && !dpsRow.getDfrSourceUuid().isEmpty()) {
                removeRelatedCfd(DCfdi40Catalogs.ClaveTipoRelaciónNotaCrédito, dpsRow.getDfrSourceUuid());
            }
        }
        
        computeTotal();
    }

    private void actionPerformedBizPartnerPick() {
        moDialogFindBizPartner.resetForm();
        moDialogFindBizPartner.initForm();
        moDialogFindBizPartner.setVisible(true);

        if (moDialogFindBizPartner.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
            if (moDialogFindBizPartner.getNewRegistries()) {
                miClient.getSession().populateCatalogue(moKeyBizPartner, DModConsts.BU_BPR, DTrnUtils.getBizPartnerClassByDpsCategory(mnFormSubtype), null);
            }
            moKeyBizPartner.setValue(moDialogFindBizPartner.getValue(DModConsts.BU_BPR));
            moKeyBizPartner.requestFocusInWindow();
        }
    }

    private void actionPerformedBizPartnerEdit() {
        if (jbBizPartnerEdit.isEnabled()) {
            if (moKeyBizPartner.getSelectedIndex() <= 0) {
                miClient.showMsgBoxInformation(DGuiConsts.MSG_GUI_SELECT_OPTION);
            }
            else {
                int[] key = moKeyBizPartner.getValue();
                miClient.getSession().showForm(DModConsts.BU_BPR, DTrnUtils.getBizPartnerClassByDpsCategory(mnFormSubtype), new DGuiParams(key));
                
                if (miClient.getSession().getModule((new DModUtils()).getModuleTypeByType(DModConsts.BU_BPR)).getLastFormResult() == DGuiConsts.FORM_RESULT_OK) {
                    miClient.getSession().populateCatalogue(moKeyBizPartner, DModConsts.BU_BPR, DTrnUtils.getBizPartnerClassByDpsCategory(mnFormSubtype), null);
                    moKeyBizPartner.setValue(key);
                    moKeyBizPartner.requestFocusInWindow();
                }
            }
        }
    }

    private void actionPerformedBranchAddressOfficialView() {
        if (moBizPartnerBranchHeadquarters != null) {
            String address = moBizPartnerBranchHeadquarters.getChildAddressOfficial().composeAddress(miClient.getSession(), moBizPartnerBranchHeadquarters.getActualFkAddressFormatTypeId(miClient.getSession()));
            miClient.showMsgBoxInformation(address);
        }
    }
    
    private void actionPerformedExchangeRatePick() {
        miClient.showMsgBoxInformation("Favor de captuar manualmente el tipo de cambio deseado.");
        moDecExchangeRate.requestFocusInWindow();
    }
    
    private void actionPerformedCfdRelationsEdit() {
        try {
            moDialogCfdRelations.resetForm();
            moDialogCfdRelations.setValue(DModConsts.TX_DFR_RELATIONS, moRegistry.getXtaDfrMate().getRelations());
            moDialogCfdRelations.setValue(DGuiConsts.PARAM_STATUS, jbSave.isEnabled() ? DGuiConsts.FORM_STATUS_EDIT : DGuiConsts.FORM_STATUS_READ);
            moDialogCfdRelations.setVisible(true);
            
            if (moDialogCfdRelations.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                moRegistry.getXtaDfrMate().setRelations((DDfrMateRelations) moDialogCfdRelations.getValue(DModConsts.TX_DFR_RELATIONS));
                
                jtfDfrCfdRelations.setText(moRegistry.getXtaDfrMate().getRelations() == null ? "" : moRegistry.getXtaDfrMate().getRelations().toString());
                jtfDfrCfdRelations.setCaretPosition(0);
            }
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }

    private void actionPerformedFind(final DTrnItemsFound itemsFound) {
        DTrnItemsFound itemsFoundActual = itemsFound;

        if (itemsFoundActual == null) {
            if (validateCoreData()) {
                itemsFoundActual = DTrnUtils.digestItemsFound(miClient.getSession(), moTextFind.getValue());

                if (isCurrentAdjustmentForMoney()) {
                    itemsFoundActual.setQuantity(0);
                }
            }
        }

        if (itemsFoundActual != null) {
            moDialogFindItem.resetForm();
            moDialogFindItem.setValue(DModSysConsts.PARAM_DATE, moDateDate.getValue());
            moDialogFindItem.setValue(DModSysConsts.PARAM_TAX_REG, new int[] { moBizPartnerBranchHeadquarters.getActualFkTaxRegionId(miClient.getSession()) });
            moDialogFindItem.setValue(DModSysConsts.PARAM_QTY, itemsFoundActual.getQuantity());

            if (mbReloadItemsOnFind) {
                mbReloadItemsOnFind = false;
                moDialogFindItem.reloadItems();
            }

            moDialogFindItem.setVisible(true);

            if (moDialogFindItem.getFormResult() != DGuiConsts.FORM_RESULT_OK) {
                actionPerformedRowClear();
            }
            else {
                if (!renderItem((int[]) moDialogFindItem.getValue(DModConsts.IU_ITM), itemsFoundActual.getQuantity())) {
                    actionPerformedRowClear();
                }
                else {
                    computeRowTotal();
                    computeRowFocus();
                }
            }
        }
    }

    private void actionPerformedRowClear() {
        clearRow();

        if (!mbIsAdjustment) {
            moTextFind.requestFocusInWindow();
        }
        else {
            mjButtonAdjustmentDoc.requestFocusInWindow();
        }
    }

    private void actionPerformedRowAdd() {
        double quantity = 0;
        boolean add = true;
        DDbDpsRow dpsRow = null;
        DDbDpsRowNote dpsRowNote = null;

        if (canAddRow()) {
            if (moItem == null) {
                miClient.showMsgBoxWarning(DGuiConsts.ERR_MSG_UNDEF_REG);
                moTextFind.requestFocusInWindow();
            }
            else if (moTextRowCode.isEditable() && moTextRowCode.getValue().isEmpty()) {
                miClient.showMsgBoxWarning(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moTextRowCode.getFieldName() + "'.");
                moTextRowCode.requestFocusInWindow();
            }
            else if (moTextRowName.isEditable() && moTextRowName.getValue().isEmpty()) {
                miClient.showMsgBoxWarning(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moTextRowName.getFieldName() + "'.");
                moTextRowName.requestFocusInWindow();
            }
            else if (moDecRowQuantity.getValue() == 0 && !isCurrentAdjustmentForMoney()) {
                miClient.showMsgBoxWarning(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moDecRowQuantity.getFieldName()+ "'.");
                moDecRowQuantity.requestFocusInWindow();
            }
            else if (moBoolRowNote.getValue() && moTextRowNote.getValue().isEmpty()) {
                miClient.showMsgBoxWarning(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moTextRowNote.getFieldName()+ "'.");
                moTextRowNote.requestFocusInWindow();
            }
            else if (moBoolRowNoteDfr.getValue() && !moBoolRowNotePrintable.getValue()) {
                miClient.showMsgBoxWarning(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moBoolRowNotePrintable.getText() + "'.");
                moBoolRowNotePrintable.requestFocusInWindow();
            }
            else if (moItem.isPredial() && !moBoolRowCfdPredial.getValue()) {
                miClient.showMsgBoxWarning(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moBoolRowCfdPredial.getText() + "'.");
                moBoolRowCfdPredial.requestFocusInWindow();
            }
            else if (moItem.isPredial() && moTextRowCfdPredial.isEnabled() && moTextRowCfdPredial.getValue().isEmpty()) {
                miClient.showMsgBoxWarning(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moBoolRowCfdPredial.getText() + "'.");
                moTextRowCfdPredial.requestFocusInWindow();
            }
            else {
                if (moDecRowSubtotal.getValue() == 0) {
                    if (miClient.showMsgBoxConfirm(DTrnConsts.MSG_CNF_ROW_VALUELESS) != JOptionPane.YES_OPTION) {
                        add = false;
                        moDecRowPriceUnitary.requestFocusInWindow();
                    }
                }

                if (add) {
                    if (!isStockMovesInputNeeded()) {
                        mvRowStockMoves.clear();
                    }
                    else {
                        // Validate quantity in row stock moves (lots, serial numbers, etc.):

                        for (DTrnStockMove move : mvRowStockMoves) {
                            quantity += move.getQuantity();
                        }

                        if (quantity != moDecRowQuantity.getValue()) {
                            // Redefine row stock moves (lots, serial numbers, etc.):

                            add = defineRowStockMoves(moDecRowQuantity.getValue(), new double[] { moDecRowPriceUnitary.getValue(), 0, 0 });
                            //add = defineRowStockMoves(moDecRowQuantity.getValue(), new double[] { moDecRowPriceUnitary.getValue(), moDecRowDiscountUnitary.getValue(), moDecRowDiscountRow.getValue() });
                        }
                    }

                    if (add) {
                        dpsRow = createDpsRow();
                        dpsRow.getAuxStockMoves().addAll(mvRowStockMoves);

                        if (!moTextRowNote.getValue().isEmpty()) {
                            dpsRowNote = new DDbDpsRowNote();
                            dpsRowNote.setText(moTextRowNote.getValue());
                            dpsRowNote.setPrintable(moBoolRowNotePrintable.getValue());
                            dpsRowNote.setDfr(moBoolRowNoteDfr.getValue());
                            dpsRow.getChildRowNotes().add(dpsRowNote);
                        }

                        moRegistry.getChildRows().add(dpsRow);
                        computeTotal();

                        moGridDpsRows.addGridRow(dpsRow);
                        moGridDpsRows.renderGridRows();
                        moGridDpsRows.setSelectedGridRow(moGridDpsRows.getTable().getRowCount() - 1);
                    }

                    actionPerformedRowClear();
                }
            }
        }
    }

    private void actionPerformedDiscountDocSet() {
        int decs = DLibUtils.getDecimalFormatAmount().getMaximumFractionDigits();
        boolean proceed = false;
        boolean allreadySet = false;
        double discountCy = 0;
        double prorateCy = 0;
        double prorateSumCy = 0;
        double prorateBaseCy = 0;
        DDbDpsRow rowMax = null;

        try {
            for (DDbDpsRow row : moRegistry.getChildRows()) {
                if (!row.isDeleted()) {
                    if (row.isDiscountDocApplying()) {
                        proceed = true;
                    }
                    if (row.getDiscountRowCy() != 0) {
                        allreadySet = true;
                    }
                }
            }

            if (!proceed) {
                miClient.showMsgBoxWarning("No hay partidas a las que se pueda aplicar descuento del documento.");
            }
            else if (allreadySet) {
                proceed = miClient.showMsgBoxConfirm("Hay partidas que ya tienen descuento del documento. Si desea continuar, dichos descuentos serán descartados.\n" + DGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION;
            }

            if (proceed) {
                moDialogDpsDiscount.setRegistry(moRegistry);
                moDialogDpsDiscount.setVisible(true);

                if (moDialogDpsDiscount.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                    discountCy = (Double) moDialogDpsDiscount.getValue(DModSysConsts.PARAM_DPS_DIS);

                    // Estimate base to prorate:

                    for (DDbDpsRow row : moRegistry.getChildRows()) {
                        if (!row.isDeleted()) {
                            if (row.isDiscountDocApplying()) {
                                prorateBaseCy += row.getSubtotalProvCy_r();
                            }
                        }
                    }

                    if (prorateBaseCy == 0) {
                        miClient.showMsgBoxWarning("No hay base para prorratear el descuento del documento.");
                    }
                    else if (discountCy > prorateBaseCy) {
                        miClient.showMsgBoxWarning("El descuento del documento deseado (" + DLibUtils.getDecimalFormatAmount().format(discountCy) + ") " +
                                "es mayor a la base para prorratear el descuento del documento (" + DLibUtils.getDecimalFormatAmount().format(prorateBaseCy) + ").");
                    }
                    else {
                        // Prorate document discount and identify row with maximum total:

                        for (DDbDpsRow row : moRegistry.getChildRows()) {
                            if (!row.isDeleted()) {
                                if (row.isDiscountDocApplying()) {
                                    prorateCy = DLibUtils.round(discountCy * (row.getSubtotalProvCy_r() / prorateBaseCy), decs);
                                    prorateSumCy += prorateCy;
                                    row.setDiscountDocCy(prorateCy);
                                }

                                if (rowMax == null) {
                                    rowMax = row;
                                }
                                else if (row.getSubtotalProvCy_r() > rowMax.getSubtotalProvCy_r()) {
                                    rowMax = row;
                                }
                            }
                        }

                        if (DLibUtils.round(discountCy, decs) != DLibUtils.round(prorateSumCy, decs)) {
                            rowMax.setDiscountDocCy(rowMax.getDiscountDocCy() + (DLibUtils.round(discountCy, decs) - DLibUtils.round(prorateSumCy, decs)));
                        }

                        computeTotal();

                        moGridDpsRows.renderGridRows();
                        moGridDpsRows.setSelectedGridRow(0);

                        actionPerformedRowClear();
                    }
                }
            }
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }

    private void actionPerformedLaunchCalc() {
        DLibUtils.launchCalculator();
    }

    private void actionPerformedEditItem() {
        DDbDpsRow row = (DDbDpsRow) moGridDpsRows.getSelectedGridRow();

        if (row == null) {
            miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
        }
        else {
            miClient.getSession().showForm(DModConsts.IU_ITM, 0, new DGuiParams(new int[] { row.getFkRowItemId() }));
        }
    }

    private void actionPerformedShowNote() {
        DDbDpsRow row = (DDbDpsRow) moGridDpsRows.getSelectedGridRow();

        if (row == null) {
            miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
        }
        else {
            if (row.getChildRowNotes().isEmpty()) {
                miClient.showMsgBoxInformation("La partida del documento no tiene notas.");
            }
            else {
                moDialogNoteShow.resetForm();
                moDialogNoteShow.setValue(DModSysConsts.PARAM_OBJ_DPS_ROW, row);
                moDialogNoteShow.setVisible(true);
            }
        }
    }

    private void actionPerformedShowLot() {
        DDbDpsRow row = (DDbDpsRow) moGridDpsRows.getSelectedGridRow();

        if (row == null) {
            miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
        }
        else {
            if (!row.isInventoriable()) {
                miClient.showMsgBoxInformation("La partida del documento no es inventariable.");
            }
            else if (row.getAuxStockMoves().isEmpty() || row.getAuxStockMoves().get(0).getLot().isEmpty()) {
                miClient.showMsgBoxInformation("La partida del documento no tiene lotes.");
            }
            else {
                moDialogLotShow.resetForm();
                moDialogLotShow.setValue(DModSysConsts.PARAM_OBJ_DPS_ROW, row);
                moDialogLotShow.setVisible(true);
            }
        }
    }

    private void actionPerformedShowSerialNumber() {
        DDbDpsRow row = (DDbDpsRow) moGridDpsRows.getSelectedGridRow();

        if (row == null) {
            miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
        }
        else {
            if (!row.isInventoriable()) {
                miClient.showMsgBoxInformation("La partida del documento no es inventariable.");
            }
            else if (row.getAuxStockMoves().isEmpty() || row.getAuxStockMoves().get(0).getSerialNumber().isEmpty()) {
                miClient.showMsgBoxInformation("La partida del documento no tiene números de serie.");
            }
            else {
                moDialogSerialNumberShow.resetForm();
                moDialogSerialNumberShow.setValue(DModSysConsts.PARAM_OBJ_DPS_ROW, row);
                moDialogSerialNumberShow.setVisible(true);
            }
        }
    }

    private void actionPerformedShowDependentDocs() {
        DDbDpsRow row = (DDbDpsRow) moGridDpsRows.getSelectedGridRow();

        if (row == null) {
            miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
        }
        else {
            if (row.getDbDependentDpsRowKeys().isEmpty()) {
                miClient.showMsgBoxInformation("La partida del documento no tiene documentos dependientes.");
            }
            else {
                moDialogDpsDependentDocsShow.resetForm();
                moDialogDpsDependentDocsShow.setValue(DModSysConsts.PARAM_VEC_DPS_ROW, row.getDbDependentDpsRowKeys());
                moDialogDpsDependentDocsShow.setVisible(true);
            }
        }
    }

    private void actionPerformedShowAdjustedDoc() {
        DDbDpsRow row = (DDbDpsRow) moGridDpsRows.getSelectedGridRow();

        if (row == null) {
            miClient.showMsgBoxInformation(DGridConsts.MSG_SELECT_ROW);
        }
        else if (row.getFkSourceDpsId_n() == 0) {
            miClient.showMsgBoxInformation("La partida del documento no tiene un documento ajustado en específico.");
        }
        else {
            moDialogDpsAdjustedDocumentShow.resetForm();
            moDialogDpsAdjustedDocumentShow.setValue(DModSysConsts.PARAM_BKK_NUM_KEY, moRegistry.getBookkeepingNumberKey_n());
            moDialogDpsAdjustedDocumentShow.setValue(DModSysConsts.PARAM_DPS, new int[] { row.getFkSourceDpsId_n() });
            moDialogDpsAdjustedDocumentShow.setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    private void actionPerformedAdjustmentForMoney() {
        if (validateCoreData()) {
            clearRow();

            moDialogDpsAdjustedForMoney.resetForm();
            moDialogDpsAdjustedForMoney.setValue(DModSysConsts.PARAM_OBJ_DPS_ADJ, moRegistry);
            moDialogDpsAdjustedForMoney.setVisible(true);

            if (moDialogDpsAdjustedForMoney.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                boolean added = false;
                Vector<DDbDpsRow> dpsRows = (Vector<DDbDpsRow>) moDialogDpsAdjustedForMoney.getValue(DModSysConsts.PARAM_VEC_DPS_ROW);

                for (DDbDpsRow dpsRow : dpsRows) {
                    if (!canAddRow()) {
                        break;
                    }
                    else {
                        added = true;
                        moRegistry.getChildRows().add(dpsRow);
                        moGridDpsRows.addGridRow(dpsRow);
                        
                        if (enableDfrFields() && !dpsRow.getDfrSourceUuid().isEmpty()) {
                            addRelatedCfd(DCfdi40Catalogs.ClaveTipoRelaciónNotaCrédito, dpsRow.getDfrSourceUuid());
                        }
                    }
                }

                if (added) {
                    computeTotal();

                    moGridDpsRows.renderGridRows();
                    moGridDpsRows.setSelectedGridRow(moGridDpsRows.getTable().getRowCount() - 1);

                    actionPerformedRowClear();
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void actionPerformedAdjustmentForStock() {
        if (validateCoreData()) {
            clearRow();

            moDialogDpsAdjustedForStock.resetForm();
            moDialogDpsAdjustedForStock.setValue(DModSysConsts.PARAM_OBJ_DPS_ADJ, moRegistry);
            moDialogDpsAdjustedForStock.setVisible(true);

            if (moDialogDpsAdjustedForStock.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                boolean added = false;
                Vector<DDbDpsRow> dpsRows = (Vector<DDbDpsRow>) moDialogDpsAdjustedForStock.getValue(DModSysConsts.PARAM_VEC_DPS_ROW);

                for (DDbDpsRow dpsRow : dpsRows) {
                    if (!canAddRow()) {
                        break;
                    }
                    else {
                        added = true;
                        moRegistry.getChildRows().add(dpsRow);
                        moGridDpsRows.addGridRow(dpsRow);

                        if (enableDfrFields() && !dpsRow.getDfrSourceUuid().isEmpty()) {
                            addRelatedCfd(DCfdi40Catalogs.ClaveTipoRelaciónNotaCrédito, dpsRow.getDfrSourceUuid());
                        }
                    }
                }

                if (added) {
                    computeTotal();

                    moGridDpsRows.renderGridRows();
                    moGridDpsRows.setSelectedGridRow(moGridDpsRows.getTable().getRowCount() - 1);

                    actionPerformedRowClear();
                }
            }
        }
    }

    private void actionPerformedAdjustmentDoc() {
        if (validateCoreData()) {
            clearRow();

            moDialogDpsAdjustedDocument.resetForm();
            moDialogDpsAdjustedDocument.setValue(DModSysConsts.PARAM_OBJ_DPS_ADJ, moRegistry);
            moDialogDpsAdjustedDocument.setVisible(true);

            if (moDialogDpsAdjustedDocument.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                manCurrentAdjustmentTypeKey = (int[]) moDialogDpsAdjustedDocument.getValue(DModSysConsts.PARAM_DPS_ADJ_TP);
                manCurrentAdjustmentClassKey = new int[] { manCurrentAdjustmentTypeKey[0], manCurrentAdjustmentTypeKey[1] };
                manCurrentAdjustedDpsKey = (int[]) moDialogDpsAdjustedDocument.getValue(DModSysConsts.PARAM_DPS);
                
                DDbDps adjustedDps = (DDbDps) miClient.getSession().readRegistry(DModConsts.T_DPS, manCurrentAdjustedDpsKey);
                
                if (adjustedDps.getChildDfr() == null) {
                    miClient.showMsgBoxWarning("El documento seleccionado '" + adjustedDps.getDpsNumber() + "' no tiene CFDI.");
                }
                else if (adjustedDps.getChildDfr().getUuid().isEmpty()) {
                    miClient.showMsgBoxWarning("El CFDI del documento seleccionado '" + adjustedDps.getDpsNumber() + "' no tiene UUID.");
                }
                else {
                    msCurrentAdjustedDpsUuid = adjustedDps.getChildDfr().getUuid();
                }

                setRowFieldsEditable(true);
                mjTextCurrentAdjustmentType.setText((String) miClient.getSession().readField(DModConsts.TS_ADJ_TP, manCurrentAdjustmentTypeKey, DDbRegistry.FIELD_NAME));
                mjTextCurrentAdjustmentType.setCaretPosition(0);
                moTextFind.requestFocusInWindow();
            }
        }
    }

    private void actionTextFind() {
        int[] itemFoundKey = null;
        DTrnItemsFound itemsFound = null;

        if (validateCoreData()) {
            itemsFound = DTrnUtils.digestItemsFound(miClient.getSession(), moTextFind.getValue());

            if (isCurrentAdjustmentForMoney()) {
                itemsFound.setQuantity(0);
            }

            if (itemsFound.getItemsFoundKeys().isEmpty()) {
                actionPerformedFind(itemsFound); // no items found, so find item (itemsFoundObject will be used only for quantity)
            }
            else if (itemsFound.getItemsFoundKeys().size() == 1) {
                itemFoundKey = itemsFound.getItemsFoundKeys().get(0);
            }
            else if (itemsFound.getItemsFoundKeys().size() > 1) {
                miClient.showMsgBoxWarning("Se encontraron " + itemsFound.getItemsFoundKeys().size() + " ítems con el texto '" + itemsFound.getTextToFind() + "'.\n" +
                        "Seleccionar el ítem deseado.");
                moDialogSelectItemFound.resetForm();
                moDialogSelectItemFound.setValue(DModSysConsts.PARAM_OBJ_ITM_FOUND, itemsFound);
                moDialogSelectItemFound.setVisible(true);
                if (moDialogSelectItemFound.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                    itemFoundKey = (int[]) moDialogSelectItemFound.getValue(DModSysConsts.PARAM_ITM);
                }
            }

            if (itemFoundKey != null) {
                if (itemsFound.getFoundBy() == DTrnConsts.FOUND_BY_SNR) {
                    mvRowStockMoves.clear();
                    mvRowStockMoves.add(new DTrnStockMove(new int[6], 1, itemsFound.getSerialNumber())); // stock key will be set in method renderItem()
                }

                if (!renderItem(itemFoundKey, itemsFound.getQuantity())) {
                    actionPerformedRowClear();
                }
                else {
                    computeRowTotal();
                    computeRowFocus();
                }
            }
        }
    }
    
    private void itemStateChangedDfrGlobal() {
        if (moBoolDfrGlobal.isSelected()) {
            moKeyDfrGlobalPeriodicity.setEnabled(true);
            moKeyDfrGlobalMonths.setEnabled(true);
            moCalDfrGlobalYear.setEnabled(true);
            
            moCalDfrGlobalYear.setValue(DLibTimeUtils.digestYear(moDateDate.getValue())[0]);
            
            moKeyDfrGlobalPeriodicity.requestFocusInWindow();
        }
        else {
            moKeyDfrGlobalPeriodicity.setEnabled(false);
            moKeyDfrGlobalMonths.setEnabled(false);
            moCalDfrGlobalYear.setEnabled(false);
            
            moKeyDfrGlobalPeriodicity.resetField();
            moKeyDfrGlobalMonths.resetField();
            moCalDfrGlobalYear.resetField();
        }
    }

    private void itemStateChangedRowNote() {
        if (moBoolRowNote.isSelected()) {
            moTextRowNote.setEnabled(true);
            moBoolRowNotePrintable.setEnabled(true);
            moBoolRowNotePrintable.setSelected(true);
            moBoolRowNoteDfr.setEnabled(mnFormSubtype == DModSysConsts.TS_DPS_CT_SAL && (mbIsDocument || mbIsAdjustment));
            moBoolRowNoteDfr.setSelected(false);
            
            moTextRowNote.requestFocusInWindow();
        }
        else {
            moTextRowNote.setEnabled(false);
            moTextRowNote.resetField();
            moBoolRowNotePrintable.setEnabled(false);
            moBoolRowNotePrintable.setSelected(false);
            moBoolRowNoteDfr.setEnabled(false);
            moBoolRowNoteDfr.setSelected(false);
        }
    }
    
    private void itemStateChangedRowPredial() {
        if (moBoolRowCfdPredial.isSelected()) {
            moTextRowCfdPredial.setEnabled(true);
            
            moTextRowCfdPredial.requestFocusInWindow();
        }
        else {
            moTextRowCfdPredial.setEnabled(false);
            
            moTextRowCfdPredial.resetField();
        }
    }

    private void itemStateChangedDiscountDocApplying(boolean computeTotal) {
        if (moBoolDiscountDocApplying.getValue()) {
            moBoolDiscountDocPercentageApplying.setEnabled(true);
        }
        else {
            moBoolDiscountDocPercentageApplying.setEnabled(false);

            moBoolDiscountDocPercentageApplying.setSelected(false);
        }

        itemStateChangedDiscountDocPercentageApplying(computeTotal);
    }

    private void itemStateChangedDiscountDocPercentageApplying(boolean computeTotal) {
        if (moBoolDiscountDocPercentageApplying.getValue()) {
            moDecDiscountDocPercentage.setEnabled(true);
            jbDiscountDocSet.setEnabled(false);

            moDecRowDiscountDoc.setEnabled(false);
        }
        else {
            moDecDiscountDocPercentage.setEnabled(false);
            jbDiscountDocSet.setEnabled(moBoolDiscountDocApplying.getValue());

            moDecRowDiscountDoc.setEnabled(moItem == null ? false : !moItem.isFreeOfDiscount() && moBoolDiscountDocApplying.getValue());

            moDecDiscountDocPercentage.setValue(0d);
        }

        if (computeTotal) {
            computeTotal();

            moGridDpsRows.renderGridRows();
            moGridDpsRows.setSelectedGridRow(0);
        }
    }

    private void itemStateChangedBizPartner() {
        jtpDocument.setSelectedIndex(DOC_TAB_BP);
        
        if (moKeyBizPartner.getSelectedIndex() <= 0) {
            moBizPartner = null;
            moBizPartnerConfig = null;
            moBizPartnerBranchHeadquarters = null;
            moBizPartnerBranchAddressOfficial = null;
            mbIsImportDeclaration = false;
            mnBizPartnerIdentityType = ((DGuiClientSessionCustom) miClient.getSession().getSessionCustom()).getIdentityTypeDefault();
            moKeyDfrReceiverTaxRegime.removeAllItems();
            
            jtpDocument.setEnabledAt(DOC_TAB_GBL, false);

            jbBizPartnerEdit.setEnabled(false);
            moKeyDfrReceiverTaxRegime.setEnabled(false);
            moKeyDfrCfdUsage.setEnabled(false);
            
            moKeyBranchAddress.setEnabled(false);
            jbBranchAddressOfficialView.setEnabled(false);
            moTextOrder.setEditable(false);
            moDateDelivery.setEditable(false);
            
            moTextDfrConfirmation.setEditable(false);
            //moKeyDfrIssuerTaxRegime.setEnabled(false); // non-dependant on business partner
            jtfDfrCfdRelations.setEnabled(false);
            jbDfrCfdRelationsEdit.setEnabled(false);
            moTextImportDeclaration.setEditable(false);
            moDateImportDeclarationDate.setEditable(false);
            
            moBoolDfrGlobal.setEnabled(false);
            
            moKeyCurrency.setEnabled(false);
            moKeyPaymentType.setEnabled(false);
            
            jtfTaxRegion.setEnabled(false);
            jtfIdentityType.setEnabled(false);
            
            moKeyEmissionType.setEnabled(false);
            moKeyAgent.setEnabled(false);
            
            setRowFieldsEditable(false);

            jtfDfrReceiverFiscalId.setText("");
            jtfDfrReceiverFiscalAddress.setText("");
            jlBizPartnerName.setToolTipText("Razón social: ?");
            moKeyDfrReceiverTaxRegime.resetField();
            moKeyDfrCfdUsage.resetField();
            
            moKeyBranchAddress.resetField();
            moTextOrder.resetField();
            moDateDelivery.resetField();
            
            moTextDfrConfirmation.resetField();
            //moKeyDfrIssuerTaxRegime.resetField(); // non-dependant on business partner
            jtfDfrCfdRelations.setText("");
            moTextImportDeclaration.resetField();
            moDateImportDeclarationDate.resetField();
            
            moBoolDfrGlobal.resetField();
            
            moKeyCurrency.resetField();
            moKeyPaymentType.resetField();
            
            jtfTaxRegion.setText("");
            jtfIdentityType.setText("");
            
            moKeyEmissionType.resetField();
            moKeyAgent.resetField();

            mbReloadItemsOnFind = false;

            mnBizPartnerItemPriceType = 0;
            mdBizPartnerDiscountPercentage = 0;
        }
        else {
            moBizPartner = (DDbBizPartner) miClient.getSession().readRegistry(DModConsts.BU_BPR, moKeyBizPartner.getValue());
            moBizPartnerConfig = moBizPartner.getChildConfig(DTrnUtils.getBizPartnerClassByDpsCategory(mnFormSubtype));
            moBizPartnerBranchHeadquarters = moBizPartner.getChildBranchHeadquarters();
            moBizPartnerBranchAddressOfficial = moBizPartnerBranchHeadquarters.getChildAddressOfficial();
            mbIsImportDeclaration = moConfigCompany.isImportDeclaration() && mnFormSubtype == DModSysConsts.TS_DPS_CT_PUR && moBizPartnerBranchAddressOfficial.getFkCountryId_n() != 0 && !moSessionCustom.isLocalCountry(new int[] { moBizPartnerBranchAddressOfficial.getFkCountryId_n() });
            mnBizPartnerIdentityType = moBizPartner.getFkIdentityTypeId();
            miClient.getSession().populateCatalogue(moKeyDfrReceiverTaxRegime, DModConsts.CS_TAX_REG, mnBizPartnerIdentityType, null);
            
            boolean enableDfrFields = enableDfrFields();
            boolean enableDfrGlobalFields = enableDfrGlobalFields();

            jtpDocument.setEnabledAt(DOC_TAB_GBL, enableDfrGlobalFields);

            jbBizPartnerEdit.setEnabled(true);
            moKeyDfrReceiverTaxRegime.setEnabled(enableDfrFields);
            moKeyDfrCfdUsage.setEnabled(enableDfrFields);
            
            moKeyBranchAddress.setEnabled(true);
            jbBranchAddressOfficialView.setEnabled(true);
            moTextOrder.setEditable(!mbIsAdjustment);
            moDateDelivery.setEditable(!mbIsAdjustment);
            
            moTextDfrConfirmation.setEditable(enableDfrFields);
            //moKeyDfrIssuerTaxRegime.setEnabled(enableDfrFields); // non-dependant on business partner
            jtfDfrCfdRelations.setEnabled(enableDfrFields);
            jbDfrCfdRelationsEdit.setEnabled(enableDfrFields);
            moTextImportDeclaration.setEditable(mbIsImportDeclaration);
            moDateImportDeclarationDate.setEditable(mbIsImportDeclaration);
            
            moBoolDfrGlobal.setEnabled(enableDfrGlobalFields);
            
            moKeyCurrency.setEnabled(!mbIsPosModule);
            moKeyPaymentType.setEnabled(enablePaymentTypeField());
            
            jtfTaxRegion.setEnabled(true);
            jtfIdentityType.setEnabled(true);
            
            moKeyEmissionType.setEnabled(moRegistry.isDpsForSale());
            moKeyAgent.setEnabled(false); // not implemented yet!
            
            setRowFieldsEditable(!mbIsAdjustment);

            jtfDfrReceiverFiscalId.setText(moBizPartner.getFiscalId());
            jtfDfrReceiverFiscalId.setCaretPosition(0);
            jtfDfrReceiverFiscalAddress.setText(moBizPartner.getActualAddressFiscal());
            jtfDfrReceiverFiscalAddress.setCaretPosition(0);
            jlBizPartnerName.setToolTipText("Razón social: " + moBizPartner.getNameFiscal());
            moKeyDfrReceiverTaxRegime.setValue(new int[] { !moKeyDfrReceiverTaxRegime.isEnabled() ? 0 : moBizPartner.getFkTaxRegimeId() });
            moKeyDfrCfdUsage.setValue(new int[] { !moKeyDfrCfdUsage.isEnabled() ? 0 : moXmlCatalogCfdUsage.getId(moBizPartner.isPublic() ? DCfdi40Catalogs.ClaveUsoCfdiSinEfectosFiscales : moBizPartnerConfig.getActualCfdUsage()) });
            
            moKeyBranchAddress.setValue(moBizPartnerBranchHeadquarters.getChildAddressDefault().getPrimaryKey());
            moTextOrder.setValue("");
            moDateDelivery.setValue(null);
            
            moTextDfrConfirmation.setValue("");
            //moKeyDfrIssuerTaxRegime.setValue(...); // non-dependant on business partner
            jtfDfrCfdRelations.setText("");
            moTextImportDeclaration.setValue("");
            moDateImportDeclarationDate.setValue(null);
            
            moBoolDfrGlobal.setValue(false);
            
            moKeyCurrency.setValue(new int[] { moBizPartnerConfig.getActualFkCurrencyId(miClient.getSession()) });
            moKeyPaymentType.setValue(new int[] { mbIsAdjustment ? DModSysConsts.FS_PAY_TP_CSH : (moBizPartnerConfig.getActualFkCreditTypeId() == DModSysConsts.BS_CDT_TP_CDT_NON ? DModSysConsts.FS_PAY_TP_CSH : DModSysConsts.FS_PAY_TP_CDT) });
            
            jtfTaxRegion.setText((String) miClient.getSession().readField(DModConsts.FU_TAX_REG, new int[] { moBizPartnerBranchHeadquarters.getActualFkTaxRegionId(miClient.getSession()) }, DDbRegistry.FIELD_NAME));
            jtfTaxRegion.setCaretPosition(0);
            jtfIdentityType.setText((String) miClient.getSession().readField(DModConsts.BS_IDY_TP, new int[] { moBizPartner.getFkIdentityTypeId() }, DDbRegistry.FIELD_NAME));
            jtfIdentityType.setCaretPosition(0);
            
            moKeyEmissionType.setValue(new int[] { moRegistry.isDpsForSale() ? moBizPartner.getFkEmissionTypeId() : DModSysConsts.TS_EMI_TP_BPR });
            moKeyAgent.setValue(null); // not implemented yet!

            mbReloadItemsOnFind = true;

            if (mnFormSubtype == DModSysConsts.TS_DPS_CT_PUR) {
                mnBizPartnerItemPriceType = 0;
                mdBizPartnerDiscountPercentage = 0;
            }
            else {
                DDbCustomerItemPriceType itemPriceType = DTrnUtils.getItemPriceType(miClient.getSession(), moBizPartner.getPrimaryKey());
                mnBizPartnerItemPriceType = itemPriceType.getFkItemPriceTypeId();
                mdBizPartnerDiscountPercentage = itemPriceType.getDiscountPercentage();
            }
        }

        // Update item finding settings:

        moDialogFindItem.setValue(DModSysConsts.PARAM_BPR_IDY_TP, mnBizPartnerIdentityType);
        if (mbShowPricesOnFind) {
            moDialogFindItem.setValue(DModSysConsts.PARAM_ITM_PRC_TP, mnBizPartnerItemPriceType);
            moDialogFindItem.setValue(DModSysConsts.PARAM_DSC_PER, mdBizPartnerDiscountPercentage);
        }

        // Update GUI:

        populateBranchAddresses();

        itemStateChangedBranchAddress();
        itemStateChangedCurrency();
        itemStateChangedPaymentType();
        itemStateChangedDfrGlobal();
        
        focusLostDate();

        clearRow();
    }

    private void itemStateChangedBranchAddress() {
        if (moKeyBranchAddress.getSelectedIndex() <= 0) {
            jtaBranchAddress.setText("");
        }
        else {
            jtaBranchAddress.setText(moBizPartnerBranchHeadquarters.getChildAddress(moKeyBranchAddress.getValue()).composeAddress(miClient.getSession(), moBizPartnerBranchHeadquarters.getActualFkAddressFormatTypeId(miClient.getSession())));
            jtaBranchAddress.setCaretPosition(0);
            
            jspBranchAddress.validate();
            
            jspBranchAddress.getVerticalScrollBar().setValue(0);
        }
    }

    private void itemStateChangedCurrency() {
        String code = "";

        if (moKeyCurrency.getSelectedIndex() <= 0) {
            moDecExchangeRate.setEnabled(false);
            moDecExchangeRate.setValue(0d);

            code = "?";
        }
        else {
            moDecExchangeRate.setEnabled(!miClient.getSession().getSessionCustom().isLocalCurrency(moKeyCurrency.getValue()));
            moDecExchangeRate.setValue(!miClient.getSession().getSessionCustom().isLocalCurrency(moKeyCurrency.getValue()) ? 0d : 1d); // XXX get today's exchange rate

            code = moKeyCurrency.getSelectedItem().getCode();
        }

        jbExchangeRatePick.setEnabled(moDecExchangeRate.isEnabled());

        moCurSubtotalProv.setCompoundText(code);
        moCurDiscountDoc.setCompoundText(code);
        moCurSubtotal.setCompoundText(code);
        moCurTaxCharged.setCompoundText(code);
        moCurTaxRetained.setCompoundText(code);
        moCurTotal.setCompoundText(code);
    }

    private void itemStateChangedPaymentType() {
        if (moKeyPaymentType.getSelectedIndex() <= 0 || moKeyPaymentType.getValue()[0] == DModSysConsts.FS_PAY_TP_NA) {
            moIntCreditDays.setEnabled(false);
            moDateCredit.setEnabled(false);
            moKeyDfrMethodOfPayment.setEnabled(false);
            moKeyModeOfPaymentType.setEnabled(false);

            moIntCreditDays.setValue(0);
            moDateCredit.setValue(null);
            moKeyDfrMethodOfPayment.resetField();
            moKeyModeOfPaymentType.resetField();
            
            moKeyPaymentType.setNextField(moTextFind);
        }
        else {
            int paymentType = moKeyPaymentType.getValue()[0];
            boolean isCash = paymentType == DModSysConsts.FS_PAY_TP_CSH;
            boolean isCredit = paymentType == DModSysConsts.FS_PAY_TP_CDT;
            boolean enableCfdiFields = enableDfrFields();
            
            moIntCreditDays.setEnabled(!mbIsPosModule && isCredit);
            moDateCredit.setEnabled(!mbIsPosModule && isCredit);
            moKeyDfrMethodOfPayment.setEnabled(false);
            moKeyModeOfPaymentType.setEnabled(!mbIsPosModule && enableCfdiFields && isCash);

            moIntCreditDays.setValue(isCash ? 0 : moBizPartnerConfig.getActualCreditDays());
            moDateCredit.setValue(moDateDate.getValue());
            moKeyDfrMethodOfPayment.setValue(new int [] { !enableCfdiFields ? 0 : (isCash || mbIsAdjustment) ? DModSysConsts.TS_XML_TP_PAY_PUE : DModSysConsts.TS_XML_TP_PAY_PPD });
            moKeyModeOfPaymentType.setValue(new int[] { !enableCfdiFields ? 0 : (isCash || mbIsAdjustment) ? moBizPartnerConfig.getActualFkModeOfPaymentTypeId() : DModSysConsts.FS_MOP_TP_TO_DEF });
            
            if (moIntCreditDays.isEnabled()) {
                moKeyPaymentType.setNextField(moIntCreditDays);
            }
            else if (moKeyDfrMethodOfPayment.isEnabled()) {
                moKeyPaymentType.setNextField(moKeyDfrMethodOfPayment);
            }
            else {
                moKeyPaymentType.setNextField(moTextFind);
            }
        }

        computeCreditInfo();
    }

    private void focusLostDate() {
        if (moDateDate.getValue() == null) {
            moDateDate.setValue(mtOriginalDate);
        }

        moDateCredit.setValue(moDateDate.getValue());
        
        computeCreditInfo();
    }

    private void focusLostCredit() {
        computeCreditInfo();
    }

    private void focusLostCreditDays() {
        computeCreditInfo();
    }

    private void focusLostDiscountDocPercentage() {
        computeTotal();

        moGridDpsRows.renderGridRows();
        moGridDpsRows.setSelectedGridRow(0);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbBizPartnerEdit;
    private javax.swing.JButton jbBizPartnerPick;
    private javax.swing.JButton jbBranchAddressOfficialView;
    private javax.swing.JButton jbDfrCfdRelationsEdit;
    private javax.swing.JButton jbDiscountDocSet;
    private javax.swing.JButton jbExchangeRatePick;
    private javax.swing.JButton jbFind;
    private javax.swing.JButton jbRowAdd;
    private javax.swing.JButton jbRowClear;
    private javax.swing.JLabel jlAgent;
    private javax.swing.JLabel jlBizPartnerName;
    private javax.swing.JLabel jlCfdType;
    private javax.swing.JLabel jlCreditDays;
    private javax.swing.JLabel jlCurrency;
    private javax.swing.JLabel jlDateCredit;
    private javax.swing.JLabel jlDateDate;
    private javax.swing.JLabel jlDateDelivery;
    private javax.swing.JLabel jlDateMaturity;
    private javax.swing.JLabel jlDfrCfdRelations;
    private javax.swing.JLabel jlDfrConfirmation;
    private javax.swing.JLabel jlDfrGlobalMonths;
    private javax.swing.JLabel jlDfrGlobalPeriodicity;
    private javax.swing.JLabel jlDfrGlobalYear;
    private javax.swing.JLabel jlDfrInfo;
    private javax.swing.JLabel jlDfrIssuerTaxRegime;
    private javax.swing.JLabel jlDfrMethodOfPayment;
    private javax.swing.JLabel jlDiscountDoc;
    private javax.swing.JLabel jlDiscountDocPercentage;
    private javax.swing.JLabel jlEmissionType;
    private javax.swing.JLabel jlExchangeRate;
    private javax.swing.JLabel jlImportDeclaration;
    private javax.swing.JLabel jlImportDeclarationDate;
    private javax.swing.JLabel jlModeOfPaymentType;
    private javax.swing.JLabel jlNumber;
    private javax.swing.JLabel jlOrder;
    private javax.swing.JLabel jlOrder1;
    private javax.swing.JLabel jlOwnBranch;
    private javax.swing.JLabel jlPaymentType;
    private javax.swing.JLabel jlRowDiscountDoc;
    private javax.swing.JLabel jlRowLabel1;
    private javax.swing.JLabel jlRowPriceUnitary;
    private javax.swing.JLabel jlRowQuantity;
    private javax.swing.JLabel jlRowSubtotal;
    private javax.swing.JLabel jlRowSubtotalProv;
    private javax.swing.JLabel jlRowUnitCode;
    private javax.swing.JLabel jlSubtotal;
    private javax.swing.JLabel jlSubtotalProv;
    private javax.swing.JLabel jlTaxCharged;
    private javax.swing.JLabel jlTaxRetainded;
    private javax.swing.JLabel jlTotal;
    private javax.swing.JLabel jlTotalQuantity;
    private javax.swing.JPanel jpContainer;
    private javax.swing.JPanel jpCredit;
    private javax.swing.JPanel jpCredit1;
    private javax.swing.JPanel jpCredit11;
    private javax.swing.JPanel jpCredit12;
    private javax.swing.JPanel jpCredit13;
    private javax.swing.JPanel jpCredit14;
    private javax.swing.JPanel jpDocInfo;
    private javax.swing.JPanel jpDocInfo1;
    private javax.swing.JPanel jpDocInfo2;
    private javax.swing.JPanel jpDocTotal;
    private javax.swing.JPanel jpDocument1;
    private javax.swing.JPanel jpDocument11;
    private javax.swing.JPanel jpDocument12;
    private javax.swing.JPanel jpDocument13;
    private javax.swing.JPanel jpDocument2;
    private javax.swing.JPanel jpDocument21;
    private javax.swing.JPanel jpDocument211;
    private javax.swing.JPanel jpDocument212;
    private javax.swing.JPanel jpDocument213;
    private javax.swing.JPanel jpDocument214;
    private javax.swing.JPanel jpDocument3;
    private javax.swing.JPanel jpDocument31;
    private javax.swing.JPanel jpDocument311;
    private javax.swing.JPanel jpDocument312;
    private javax.swing.JPanel jpDocument313;
    private javax.swing.JPanel jpHeader;
    private javax.swing.JPanel jpMain;
    private javax.swing.JPanel jpMain1;
    private javax.swing.JPanel jpMain2;
    private javax.swing.JPanel jpRows;
    private javax.swing.JPanel jpRows1;
    private javax.swing.JPanel jpRows11;
    private javax.swing.JPanel jpRows2;
    private javax.swing.JScrollPane jspBranchAddress;
    private javax.swing.JTextArea jtaBranchAddress;
    private javax.swing.JTextField jtfBranchWarehose;
    private javax.swing.JTextField jtfCfdStatus;
    private javax.swing.JTextField jtfCfdType;
    private javax.swing.JTextField jtfDateMaturity;
    private javax.swing.JTextField jtfDfrCfdRelations;
    private javax.swing.JTextField jtfDfrCfdType;
    private javax.swing.JTextField jtfDfrDatetime;
    private javax.swing.JTextField jtfDfrPaymentTerms;
    private javax.swing.JTextField jtfDfrPlaceOfIssue;
    private javax.swing.JTextField jtfDfrReceiverFiscalAddress;
    private javax.swing.JTextField jtfDfrReceiverFiscalId;
    private javax.swing.JTextField jtfDfrUuid;
    private javax.swing.JTextField jtfDfrVersion;
    private javax.swing.JTextField jtfDocStatus;
    private javax.swing.JTextField jtfDocType;
    private javax.swing.JTextField jtfIdentityType;
    private javax.swing.JTextField jtfOwnBranch;
    private javax.swing.JTextField jtfRowCfdItemKey;
    private javax.swing.JTextField jtfRowCfdUnitKey;
    private javax.swing.JTextField jtfRowUnitCode;
    private javax.swing.JTextField jtfTaxRegion;
    private javax.swing.JTextField jtfTerminal;
    private javax.swing.JTextField jtfTotalQuantity;
    private javax.swing.JTabbedPane jtpDocument;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolDfrGlobal;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolDiscountDocApplying;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolDiscountDocPercentageApplying;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolRowCfdPredial;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolRowNote;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolRowNoteDfr;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolRowNotePrintable;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolRowTaxInput;
    private sba.lib.gui.bean.DBeanFieldCalendarYear moCalDfrGlobalYear;
    private sba.lib.gui.bean.DBeanCompoundFieldCurrency moCurDiscountDoc;
    private sba.lib.gui.bean.DBeanCompoundFieldCurrency moCurSubtotal;
    private sba.lib.gui.bean.DBeanCompoundFieldCurrency moCurSubtotalProv;
    private sba.lib.gui.bean.DBeanCompoundFieldCurrency moCurTaxCharged;
    private sba.lib.gui.bean.DBeanCompoundFieldCurrency moCurTaxRetained;
    private sba.lib.gui.bean.DBeanCompoundFieldCurrency moCurTotal;
    private sba.lib.gui.bean.DBeanFieldDate moDateCredit;
    private sba.lib.gui.bean.DBeanFieldDate moDateDate;
    private sba.lib.gui.bean.DBeanFieldDate moDateDelivery;
    private sba.lib.gui.bean.DBeanFieldDate moDateImportDeclarationDate;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecDiscountDocPercentage;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecExchangeRate;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecRowDiscountDoc;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecRowPriceUnitary;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecRowQuantity;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecRowSubtotal;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecRowSubtotalProv;
    private sba.lib.gui.bean.DBeanFieldInteger moIntCreditDays;
    private sba.lib.gui.bean.DBeanFieldInteger moIntNumber;
    private sba.lib.gui.bean.DBeanFieldKey moKeyAgent;
    private sba.lib.gui.bean.DBeanFieldKey moKeyBizPartner;
    private sba.lib.gui.bean.DBeanFieldKey moKeyBranchAddress;
    private sba.lib.gui.bean.DBeanFieldKey moKeyCurrency;
    private sba.lib.gui.bean.DBeanFieldKey moKeyDfrCfdUsage;
    private sba.lib.gui.bean.DBeanFieldKey moKeyDfrGlobalMonths;
    private sba.lib.gui.bean.DBeanFieldKey moKeyDfrGlobalPeriodicity;
    private sba.lib.gui.bean.DBeanFieldKey moKeyDfrIssuerTaxRegime;
    private sba.lib.gui.bean.DBeanFieldKey moKeyDfrMethodOfPayment;
    private sba.lib.gui.bean.DBeanFieldKey moKeyDfrReceiverTaxRegime;
    private sba.lib.gui.bean.DBeanFieldKey moKeyEmissionType;
    private sba.lib.gui.bean.DBeanFieldKey moKeyModeOfPaymentType;
    private sba.lib.gui.bean.DBeanFieldKey moKeyPaymentType;
    private sba.lib.gui.bean.DBeanFieldText moTextDfrConfirmation;
    private sba.lib.gui.bean.DBeanFieldText moTextFind;
    private sba.lib.gui.bean.DBeanFieldText moTextImportDeclaration;
    private sba.lib.gui.bean.DBeanFieldText moTextOrder;
    private sba.lib.gui.bean.DBeanFieldText moTextRowCfdPredial;
    private sba.lib.gui.bean.DBeanFieldText moTextRowCode;
    private sba.lib.gui.bean.DBeanFieldText moTextRowName;
    private sba.lib.gui.bean.DBeanFieldText moTextRowNote;
    private sba.lib.gui.bean.DBeanFieldText moTextSeries;
    // End of variables declaration//GEN-END:variables

    @Override
    public void addAllListeners() {
        jbBizPartnerPick.addActionListener(this);
        jbBizPartnerEdit.addActionListener(this);
        jbExchangeRatePick.addActionListener(this);
        jbBranchAddressOfficialView.addActionListener(this);
        jbDfrCfdRelationsEdit.addActionListener(this);
        jbFind.addActionListener(this);
        jbRowClear.addActionListener(this);
        jbRowAdd.addActionListener(this);
        jbDiscountDocSet.addActionListener(this);
        mjButtonLaunchCalc.addActionListener(this);
        mjButtonEditItem.addActionListener(this);
        mjButtonShowRowNote.addActionListener(this);
        mjButtonShowRowLot.addActionListener(this);
        mjButtonShowRowSerialNumber.addActionListener(this);
        
        moIntCreditDays.addFocusListener(this);
        moDateDate.getComponent().addFocusListener(this);
        moDateCredit.getComponent().addFocusListener(this);
        moDecRowQuantity.addFocusListener(this);
        moDecRowPriceUnitary.addFocusListener(this);
        moDecRowSubtotalProv.addFocusListener(this);
        moDecRowDiscountDoc.addFocusListener(this);
        moDecRowSubtotal.addFocusListener(this);
        moDecDiscountDocPercentage.addFocusListener(this);
        
        moKeyBizPartner.addItemListener(this);
        moKeyBranchAddress.addItemListener(this);
        moKeyCurrency.addItemListener(this);
        moKeyPaymentType.addItemListener(this);
        moBoolDfrGlobal.addItemListener(this);
        moBoolRowNote.addItemListener(this);
        moBoolRowCfdPredial.addItemListener(this);
        moBoolDiscountDocApplying.addItemListener(this);
        moBoolDiscountDocPercentageApplying.addItemListener(this);

        if (!mbIsAdjustment) {
            mjButtonShowDependentDocs.addActionListener(this);
        }
        else {
            mjButtonAdjustmentForMoney.addActionListener(this);
            mjButtonAdjustmentForStock.addActionListener(this);
            mjButtonAdjustmentDoc.addActionListener(this);
            mjButtonShowAdjustedDoc.addActionListener(this);
        }
    }

    @Override
    public void removeAllListeners() {
        jbBizPartnerPick.removeActionListener(this);
        jbBizPartnerEdit.removeActionListener(this);
        jbExchangeRatePick.removeActionListener(this);
        jbBranchAddressOfficialView.removeActionListener(this);
        jbDfrCfdRelationsEdit.removeActionListener(this);
        jbFind.removeActionListener(this);
        jbRowClear.removeActionListener(this);
        jbRowAdd.removeActionListener(this);
        jbDiscountDocSet.removeActionListener(this);
        mjButtonLaunchCalc.removeActionListener(this);
        mjButtonEditItem.removeActionListener(this);
        mjButtonShowRowNote.removeActionListener(this);
        mjButtonShowRowLot.removeActionListener(this);
        mjButtonShowRowSerialNumber.removeActionListener(this);
        
        moIntCreditDays.removeFocusListener(this);
        moDateDate.getComponent().removeFocusListener(this);
        moDateCredit.getComponent().removeFocusListener(this);
        moDecRowQuantity.removeFocusListener(this);
        moDecRowPriceUnitary.removeFocusListener(this);
        moDecRowSubtotalProv.removeFocusListener(this);
        moDecRowDiscountDoc.removeFocusListener(this);
        moDecRowSubtotal.removeFocusListener(this);
        moDecDiscountDocPercentage.removeFocusListener(this);
        
        moKeyBizPartner.removeItemListener(this);
        moKeyBranchAddress.removeItemListener(this);
        moKeyCurrency.removeItemListener(this);
        moKeyPaymentType.removeItemListener(this);
        moBoolDfrGlobal.removeItemListener(this);
        moBoolRowNote.removeItemListener(this);
        moBoolRowCfdPredial.removeItemListener(this);
        moBoolDiscountDocApplying.removeItemListener(this);
        moBoolDiscountDocPercentageApplying.removeItemListener(this);

        if (!mbIsAdjustment) {
            mjButtonShowDependentDocs.removeActionListener(this);
        }
        else {
            mjButtonAdjustmentForMoney.removeActionListener(this);
            mjButtonAdjustmentForStock.removeActionListener(this);
            mjButtonAdjustmentDoc.removeActionListener(this);
            mjButtonShowAdjustedDoc.removeActionListener(this);
        }
    }

    @Override
    public void reloadCatalogues() {
        miClient.getSession().populateCatalogue(moKeyBizPartner, DModConsts.BU_BPR, DTrnUtils.getBizPartnerClassByDpsCategory(mnFormSubtype), null);
        miClient.getSession().populateCatalogue(moKeyCurrency, DModConsts.CS_CUR, 0, null);
        miClient.getSession().populateCatalogue(moKeyPaymentType, DModConsts.FS_PAY_TP, 0, null);
        miClient.getSession().populateCatalogue(moKeyModeOfPaymentType, DModConsts.FS_MOP_TP, 0, null);
        miClient.getSession().populateCatalogue(moKeyDfrIssuerTaxRegime, DModConsts.CS_TAX_REG, mnCompanyIdentityType, null);
        miClient.getSession().populateCatalogue(moKeyEmissionType, DModConsts.TS_EMI_TP, 0, null);
    }

    @Override
    public void setRegistry(DDbRegistry registry) throws Exception {
        int[] dpsClassKey = DTrnUtils.getDpsClassByDpsXType(mnFormType, mnFormSubtype);
        int[] dpsTypeKey = DTrnUtils.getDpsTypeByDpsXType(mnFormType, mnFormSubtype);
        boolean isDpsNumberAutomatic = DTrnUtils.isDpsNumberAutomatic(dpsClassKey);
        Vector<DGridRow> rows = new Vector<>();
        Vector<DGridRow> notes = new Vector<>();

        moRegistry = (DDbDps) registry;

        mnFormResult = 0;
        mbFirstActivation = true;

        moSessionCustom = (DGuiClientSessionCustom) miClient.getSession().getSessionCustom(); // refresh each time this form is used
        moConfigCompany = (DDbConfigCompany) miClient.getSession().getConfigCompany(); // refresh each time this form is used

        mnNewDpsNumber = 0;
        mnNewDpsSeriesId = 0;
        msNewDpsSeries = "";

        jbSave.setEnabled(false);
        
        if (moRegistry.isRegistryNew()) {
            // Validate if new registry can be created:

            if (moSessionCustom.getBranchKey() == null) {
                mbCanShowForm = false;
                msCanShowFormMessage = DUtilConsts.ERR_MSG_USR_SES_BRA;
                return;
            }
            else if (moSessionCustom.getBranchWarehouseKey() == null) {
                mbCanShowForm = false;
                msCanShowFormMessage = DUtilConsts.ERR_MSG_USR_SES_BRA_WAH;
                return;
            }
            else if (isDpsNumberAutomatic) {
                if (!computeNewDpsNumber(dpsTypeKey)) {
                    return;
                }
            }
        }

        // Set registry:

        removeAllListeners();
        reloadCatalogues();

        mbIsPosModule = false;
        mbCheckDfrOnSaveOnlyOnce = true;

        if (moRegistry.isRegistryNew()) {
            moRegistry.initPrimaryKey();

            moRegistry.setTerminal(moSessionCustom.getTerminal());

            moRegistry.setFkDpsCategoryId(dpsTypeKey[0]);
            moRegistry.setFkDpsClassId(dpsTypeKey[1]);
            moRegistry.setFkDpsTypeId(dpsTypeKey[2]);
            moRegistry.setFkDpsStatusId(DModSysConsts.TS_DPS_ST_ISS);

            moRegistry.setFkOwnerBizPartnerId(moSessionCustom.getBranchKey()[0]);
            moRegistry.setFkOwnerBranchId(moSessionCustom.getBranchKey()[1]);

            moRegistry.setFkWarehouseBizPartnerId_n(moSessionCustom.getBranchWarehouseKey()[0]);
            moRegistry.setFkWarehouseBranchId_n(moSessionCustom.getBranchWarehouseKey()[1]);
            moRegistry.setFkWarehouseWarehouseId_n(moSessionCustom.getBranchWarehouseKey()[2]);

            moTextSeries.setValue(msNewDpsSeries); // new series defined above in computeNewDpsNumber() if required
            moIntNumber.setValue(mnNewDpsNumber); // new number defined above in computeNewDpsNumber() if required

            mtOriginalDate = miClient.getSession().getWorkingDate();
            
            moRegistryLock = null;
            jtfRegistryKey.setText("");
        }
        else {
            moTextSeries.setValue(moRegistry.getSeries());
            moIntNumber.setValue(moRegistry.getNumber());

            mtOriginalDate = moRegistry.getDate();
            
            moRegistryLock = moRegistry.assureLock(miClient.getSession());
            jtfRegistryKey.setText(DLibUtils.textKey(moRegistry.getPrimaryKey()));
        }
        
        // Set series ad number:

        if (!isDpsNumberAutomatic) {
            moDpsSeries = null;
            moDpsSeriesNumber = null;
        }
        else {
            if (moRegistry.isRegistryNew()) {
                moDpsSeries = (DDbDpsSeries) miClient.getSession().readRegistry(DModConsts.TU_SER, new int[] { mnNewDpsSeriesId });
                moDpsSeriesNumber = ((DDbUser) miClient.getSession().getUser()).getAuxBranchDpsSeriesNumbers(mnNewDpsSeriesId).get(0);
            }
            else {
                moDpsSeries = DTrnUtils.getDpsSeries(miClient.getSession(), dpsTypeKey, moTextSeries.getValue());
                moDpsSeriesNumber = DTrnUtils.getDpsSeriesNumber(miClient.getSession(), dpsTypeKey, moTextSeries.getValue(), moIntNumber.getValue());
            }
        }
        
        // Once series and number set, set DFR settings:
        
        boolean enableDfrFields = enableDfrFields(); // moDpsSeriesNumber already must be set
        
        if (moRegistry.isRegistryNew() && enableDfrFields && moRegistry.getXtaDfrMate() == null) {
            DDfrMate dfrMate = new DDfrMate();
            dfrMate.setPlaceOfIssue(moConfigCompany.getChildBizPartner().getActualAddressFiscal());
            dfrMate.setIssuerTaxRegime("" + moConfigCompany.getChildBizPartner().getFkTaxRegimeId()); // id = code
            moRegistry.setXtaDfrMate(dfrMate);
        }
        
        // Render registry:

        setFormEditable(true); // enable all controls before setting form values
        
        jtpDocument.setEnabledAt(DOC_TAB_DFR, enableDfrFields);
        moTextDfrConfirmation.setEditable(enableDfrFields);
        moKeyDfrIssuerTaxRegime.setEnabled(enableDfrFields);
        jbDfrCfdRelationsEdit.setEnabled(enableDfrFields);

        moKeyBizPartner.setValue(moRegistry.getBizPartnerKey());
        itemStateChangedBizPartner();
        
        if (enableDfrFields) {
            moKeyDfrReceiverTaxRegime.setValue(new int[] { DLibUtils.parseInt(moRegistry.getXtaDfrMate().getReceiverTaxRegime()) }); // id = code
            moKeyDfrCfdUsage.setValue(new int[] { moXmlCatalogCfdUsage.getId(moRegistry.getXtaDfrMate().getCfdUsage()) });
            
            if (moKeyDfrReceiverTaxRegime.getSelectedIndex() <= 0 && moBizPartner != null) {
                moKeyDfrReceiverTaxRegime.setValue(new int[] { moBizPartner.getFkTaxRegimeId() });
            }
        }

        moKeyBranchAddress.setValue(moRegistry.getBizPartnerBranchAddressKey());
        itemStateChangedBranchAddress();

        mnOriginalYear = DLibTimeUtils.digestYear(mtOriginalDate)[0];
        moDateDate.setValue(mtOriginalDate);

        moKeyCurrency.setValue(moRegistry.getCurrencyKey());
        itemStateChangedCurrency();
        moDecExchangeRate.setValue(moRegistry.getExchangeRate());
        
        moKeyPaymentType.setValue(moRegistry.getPaymentTypeKey());
        itemStateChangedPaymentType();
        
        moIntCreditDays.setValue(moRegistry.getCreditDays());
        moDateCredit.setValue(moRegistry.getDateCredit());
        computeCreditInfo();
        
        moKeyDfrMethodOfPayment.setValue(new int[] { !enableDfrFields ? 0 : moXmlCatalogMethodOfPayment.getId(moRegistry.getXtaDfrMate().getMethodOfPayment()) });
        moKeyModeOfPaymentType.setValue(new int[] { moRegistry.getFkModeOfPaymentTypeId() });
        
        moTextOrder.setValue(moRegistry.getOrder());
        moDateDelivery.setValue(moRegistry.getDateDelivery_n());
        
        jtfDfrPlaceOfIssue.setText(!enableDfrFields ? "" : moRegistry.getXtaDfrMate().getPlaceOfIssue());
        jtfDfrPlaceOfIssue.setCaretPosition(0);
        moTextDfrConfirmation.setValue(!enableDfrFields ? "" : moRegistry.getXtaDfrMate().getConfirmation());
        moKeyDfrIssuerTaxRegime.setValue(new int[] { !enableDfrFields ? 0 : DLibUtils.parseInt(moRegistry.getXtaDfrMate().getIssuerTaxRegime()) }); // id = code
        moTextImportDeclaration.setValue(moRegistry.getImportDeclaration()); // depends on business partner settings (i.e. country)
        moDateImportDeclarationDate.setValue(moRegistry.getImportDeclarationDate_n()); // depends on business partner settings (i.e. country)
        jtfDfrCfdRelations.setText(!enableDfrFields ? "" : (moRegistry.getXtaDfrMate().getRelations() == null ? "" : moRegistry.getXtaDfrMate().getRelations().toString()));
        jtfDfrCfdRelations.setCaretPosition(0);
        
        moBoolDfrGlobal.setValue(enableDfrFields && !moRegistry.getXtaDfrMate().getGlobalPeriodicity().isEmpty());
        itemStateChangedDfrGlobal();
        if (moBoolDfrGlobal.isSelected()) {
            moKeyDfrGlobalPeriodicity.setValue(new int[] { moXmlCatalogGlobalPeriodicity.getId(moRegistry.getXtaDfrMate().getGlobalPeriodicity()) });
            moKeyDfrGlobalMonths.setValue(new int[] { moXmlCatalogGlobalMonths.getId(moRegistry.getXtaDfrMate().getGlobalMonths()) });
            moCalDfrGlobalYear.setValue(moRegistry.getXtaDfrMate().getGlobalYear());
        }

        moKeyEmissionType.setValue(new int[] { moRegistry.getFkEmissionTypeId() });

        moBoolDiscountDocApplying.setValue(moRegistry.isDiscountDocApplying());
        itemStateChangedDiscountDocApplying(false);

        moBoolDiscountDocPercentageApplying.setValue(moRegistry.isDiscountDocPercentageApplying());
        itemStateChangedDiscountDocPercentageApplying(false);
        moDecDiscountDocPercentage.setValue(moRegistry.getDiscountDocPercentage());

        jtfDocType.setText((String) miClient.getSession().readField(DModConsts.TS_DPS_TP, moRegistry.getDpsTypeKey(), DDbRegistry.FIELD_NAME));
        jtfDocType.setCaretPosition(0);
        
        jtfDocStatus.setText((String) miClient.getSession().readField(DModConsts.TS_DPS_ST, moRegistry.getDpsStatusKey(), DDbRegistry.FIELD_NAME));
        jtfDocStatus.setCaretPosition(0);
        
        jtfDfrCfdType.setText(!enableDfrFields ? "" : moRegistry.getXtaDfrMate().getCfdType());
        jtfDfrCfdType.setCaretPosition(0);
        
        jtfDfrVersion.setText(!enableDfrFields ? "" : moRegistry.getXtaDfrMate().getVersion());
        jtfDfrVersion.setCaretPosition(0);
        
        jtfDfrDatetime.setText(moRegistry.getChildDfr() == null ? "" : DLibUtils.DateFormatDatetime.format(moRegistry.getChildDfr().getDocTs()));
        jtfDfrDatetime.setCaretPosition(0);
        
        jtfDfrUuid.setText(moRegistry.getChildDfr() == null ? "" : moRegistry.getChildDfr().getUuid());
        jtfDfrUuid.setCaretPosition(0);
        
        jtfCfdType.setText(moRegistry.getChildDfr() == null ? "" : (String) miClient.getSession().readField(DModConsts.TS_XML_TP, new int[] { moRegistry.getChildDfr().getFkXmlTypeId() }, DDbRegistry.FIELD_NAME));
        jtfCfdType.setCaretPosition(0);
        
        jtfCfdStatus.setText(moRegistry.getChildDfr() == null ? "" : (String) miClient.getSession().readField(DModConsts.TS_XML_ST, new int[] { moRegistry.getChildDfr().getFkXmlStatusId() }, DDbRegistry.FIELD_NAME));
        jtfCfdStatus.setCaretPosition(0);
        
        jtfOwnBranch.setText((String) miClient.getSession().readField(DModConsts.BU_BRA, moRegistry.getCompanyBranchKey(), DDbRegistry.FIELD_CODE));
        jtfOwnBranch.setCaretPosition(0);
        
        jtfBranchWarehose.setText(moRegistry.getBranchWarehouseKey_n() == null ? "" : (String) miClient.getSession().readField(DModConsts.CU_WAH, moRegistry.getBranchWarehouseKey_n(), DDbRegistry.FIELD_CODE));
        jtfBranchWarehose.setCaretPosition(0);
        
        jtfTerminal.setText("" + moRegistry.getTerminal());
        jtfTerminal.setCaretPosition(0);

        for (DDbDpsNote child : moRegistry.getChildNotes()) {
            notes.add(child);
        }

        moGridDpsNotes.populateGrid(notes);

        for (DDbDpsRow child : moRegistry.getChildRows()) {
            if (!child.isDeleted()) {
                rows.add(child);
            }
        }

        moGridDpsRows.populateGrid(rows);

        moKeyAgent.setEnabled(false); // not implemented yet!
        
        moDecRowSubtotalProv.setEditable(false);
        moCurSubtotalProv.getField().setEditable(false);
        moCurDiscountDoc.getField().setEditable(false);
        moCurSubtotal.getField().setEditable(false);
        moCurTaxCharged.getField().setEditable(false);
        moCurTaxRetained.getField().setEditable(false);
        moCurTotal.getField().setEditable(false);

        moDialogFindBizPartner.initForm();

        switch (mnFormSubtype) {
            case DModSysConsts.TS_DPS_CT_PUR:
                moDialogFindItem.setValue(DModSysConsts.PARAM_ITM_CT_KEY, new int[] { DModSysConsts.IS_ITM_CT_PUR });

                mbCanChangePrice = miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_PUR_PRC_INV);
                mbCanChangePricePos = false;
                mbCanChangeDiscount = miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_PUR_DIS_INV);
                mbCanChangeDiscountPos = false;

                jlImportDeclaration.setEnabled(true);
                jlImportDeclarationDate.setEnabled(true);
                break;

            case DModSysConsts.TS_DPS_CT_SAL:
                moDialogFindItem.setValue(DModSysConsts.PARAM_ITM_CT_KEY, new int[] { DModSysConsts.IS_ITM_CT_SAL });

                mbCanChangePrice = miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_SAL_PRC_INV);
                mbCanChangePricePos = miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_POS_PRC_INV);
                mbCanChangeDiscount = miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_SAL_DIS_INV);
                mbCanChangeDiscountPos = miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_POS_DIS_INV);

                jlImportDeclaration.setEnabled(false);
                jlImportDeclarationDate.setEnabled(false);
                break;

            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        mbReloadItemsOnFind = false;
        moDialogFindItem.setValue(DModSysConsts.PARAM_BRA_WAH, moRegistry.getBranchWarehouseKey_n());
        moDialogFindItem.setValue(DModSysConsts.FLAG_ONLY_INVENTORIABLE, false);
        moDialogFindItem.setValue(DModSysConsts.FLAG_ONLY_CONVERTIBLE, false);
        moDialogFindItem.initForm();

        if (isDpsNumberAutomatic) {
            moTextSeries.setEditable(false);
            moIntNumber.setEditable(((DDbConfigBranch) miClient.getSession().getConfigBranch()).isDpsNumberAutomaticByUser());
        }

        moBoolRowTaxInput.setValue(false);

        // XXX This needs to be improved!!!

        if (moRegistry.isRegistryNew()) {
            jbSave.setEnabled(true);
        }
        else {
            jbSave.setEnabled(!moRegistry.isSystem() && (moRegistry.getChildDfr() == null || moRegistry.getChildDfr().getFkXmlStatusId() != DModSysConsts.TS_XML_ST_ISS));
        }

        computeTotal();
        clearRow();

        addAllListeners();
    }

    @Override
    public DDbDps getRegistry() throws Exception {
        DDbDps registry = moRegistry.clone();

        if (registry.isRegistryNew()) {
            if (DTrnUtils.isDpsNumberAutomatic(registry.getDpsClassKey())) {
                registry.setAuxNewDpsSeriesId(mnNewDpsSeriesId);
            }
        }
        else {
            registry.setAuxLock(moRegistryLock);
        }

        registry.setSeries(moTextSeries.getValue());
        registry.setNumber(moIntNumber.getValue());
        registry.setOrder(moTextOrder.getValue());
        registry.setDate(moDateDate.getValue());
        registry.setDateCredit(moDateCredit.getValue());
        registry.setDateDelivery_n(moDateDelivery.getValue());
        registry.setDateDocOriginal(moDateDate.getValue());
        registry.setDateDocSending(moDateDate.getValue());
        registry.setDateDocReception(moDateDate.getValue());
        registry.setApproveYear(0); // obsolete, preserved for backwards compatibility
        registry.setApproveNumber(0); // obsolete, preserved for backwards compatibility
        registry.setCreditDays(moIntCreditDays.getValue());
        registry.setPaymentAccount("");
        registry.setImportDeclaration(moTextImportDeclaration.getValue());
        registry.setImportDeclarationDate_n(moDateImportDeclarationDate.getValue());
        registry.setDiscountDocApplying(moBoolDiscountDocApplying.getValue());
        registry.setDiscountDocPercentageApplying(moBoolDiscountDocPercentageApplying.getValue());
        registry.setDiscountDocPercentage(moDecDiscountDocPercentage.getValue());
        registry.setExchangeRate(moDecExchangeRate.getValue());
        registry.setDocCopy(false);
        registry.setClosedDps(false);
        registry.setClosedIog(false);
        registry.setAudited(false);
        registry.setDeleted(false);
        registry.setSystem(false);
        //registry.setFkDpsCategoryId(?);
        //registry.setFkDpsClassId(?);
        //registry.setFkDpsTypeId(?);
        registry.setFkDpsStatusId(DModSysConsts.TS_DPS_ST_ISS);
        registry.setFkCurrencyId(moKeyCurrency.getValue()[0]);
        registry.setFkPaymentTypeId(moKeyPaymentType.getValue()[0]);
        registry.setFkModeOfPaymentTypeId(moKeyModeOfPaymentType.getSelectedIndex() <= 0 ? DModSysConsts.FS_MOP_TP_NA : moKeyModeOfPaymentType.getValue()[0]);
        registry.setFkEmissionTypeId(!moKeyEmissionType.isEnabled() ? DModSysConsts.TS_EMI_TP_BPR : moKeyEmissionType.getValue()[0]);
        //registry.setFkOwnerBizPartnerId(?);
        //registry.setFkOwnerBranchId(?);
        //registry.setFkWarehouseBizPartnerId_n(?);
        //registry.setFkWarehouseBranchId_n(?);
        //registry.setFkWarehouseWarehouseId_n(?);
        registry.setFkBizPartnerBizPartnerId(moKeyBranchAddress.getValue()[0]);
        registry.setFkBizPartnerBranchId(moKeyBranchAddress.getValue()[1]);
        registry.setFkBizPartnerAddressId(moKeyBranchAddress.getValue()[2]);
        registry.setFkAgentId_n(!moKeyAgent.isEnabled() ? 0 : moKeyAgent.getValue()[0]);
        //registry.setFkBookkeepingYearId_n(?);
        //registry.setFkBookkeepingNumberId_n(?);
        registry.setFkSourceDpsId_n(0);
        registry.setFkUserClosedDpsId(DUtilConsts.USR_NA_ID);
        registry.setFkUserClosedIogId(DUtilConsts.USR_NA_ID);
        registry.setFkUserAuditedId(DUtilConsts.USR_NA_ID);

        // Document alive and deleted notes:

        registry.getChildNotes().clear();
        for (DGridRow row : moGridDpsNotes.getModel().getGridRows()) {
            registry.getChildNotes().add((DDbDpsNote) row);
        }

        // Document alive and deleted rows:

        registry.getChildRows().clear();
        for (DGridRow row : moGridDpsRows.getModel().getGridRows()) {
            registry.getChildRows().add((DDbDpsRow) row);
        }

        for (DGridRow row : moGridDpsRows.getDeletedRows()) {
            DDbDpsRow dpsRow = (DDbDpsRow) row;
            if (!dpsRow.isRegistryNew()) {
                dpsRow.setDeleted(true);
                dpsRow.setRegistryEdited(true);
                registry.getChildRows().add(dpsRow);
            }
        }

        // Set information of import declaration if applies:

        if (mbIsImportDeclaration && !moTextImportDeclaration.getValue().isEmpty()) {
            for (DDbDpsRow dpsRow : registry.getChildRows()) {
                if (!dpsRow.isDeleted()) {
                    dpsRow.setRegistryEdited(true);
                    for (DTrnStockMove move : dpsRow.getAuxStockMoves()) {
                        move.setImportDeclaration(moTextImportDeclaration.getValue());
                        move.setImportDeclarationDate(moDateImportDeclarationDate.getValue());
                    }
                }
            }
        }

        registry.computeTotal();

        if (moDpsSeriesNumber == null) {
            registry.setAuxXmlTypeId(0);
            registry.setAuxDfrRequired(false);
        }
        else {
            if (enableDfrFields()) {
                float version = 0;
                
                switch (moDpsSeriesNumber.getFkXmlTypeId()) {
                    case DModSysConsts.TS_XML_TP_CFDI_33:
                        version = DCfdConsts.CFDI_VER_33;
                        break;
                    case DModSysConsts.TS_XML_TP_CFDI_40:
                        version = DCfdConsts.CFDI_VER_40;
                        break;
                    default:
                        throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN + "\nTipo XML.");
                }
                
                DDfrMate dfrMate = new DDfrMate();

                dfrMate.setCfdType(mbIsDocument ? DCfdi40Catalogs.CFD_TP_I : DCfdi40Catalogs.CFD_TP_E);
                dfrMate.setVersion("" + version);
                dfrMate.setPlaceOfIssue(jtfDfrPlaceOfIssue.getText());
                dfrMate.setMethodOfPayment(moKeyDfrMethodOfPayment.getSelectedIndex() <= 0 ? "" : moKeyDfrMethodOfPayment.getSelectedItem().getCode());
                dfrMate.setPaymentTerms(jtfDfrPaymentTerms.getText());
                dfrMate.setConfirmation(moTextDfrConfirmation.getValue());
                dfrMate.setIssuerTaxRegime(moKeyDfrIssuerTaxRegime.getSelectedIndex() <= 0 ? "" : "" + moKeyDfrIssuerTaxRegime.getValue()[0]); // id = code
                dfrMate.setReceiverTaxRegime(moKeyDfrReceiverTaxRegime.getSelectedIndex() <= 0 ? "" : "" + moKeyDfrReceiverTaxRegime.getValue()[0]); // id = code
                dfrMate.setReceiverFiscalAddress(moBizPartner.getActualAddressFiscal());
                dfrMate.setCfdUsage(moKeyDfrCfdUsage.getSelectedIndex() <= 0 ? "" : moKeyDfrCfdUsage.getSelectedItem().getCode());
                
                if (!moBoolDfrGlobal.isSelected()) {
                    dfrMate.setGlobalPeriodicity("");
                    dfrMate.setGlobalMonths("");
                    dfrMate.setGlobalYear(0);
                }
                else {
                    dfrMate.setGlobalPeriodicity(moKeyDfrGlobalPeriodicity.getSelectedItem().getCode());
                    dfrMate.setGlobalMonths(moKeyDfrGlobalMonths.getSelectedItem().getCode());
                    dfrMate.setGlobalYear(moCalDfrGlobalYear.getValue());
                }
                
                dfrMate.setRelations(moRegistry.getXtaDfrMate().getRelations()); // current relations already in registry

                registry.setXtaDfrMate(dfrMate);
                
                // create dummy DFR only to verify if it can be processed:
                if (mbCheckDfrOnSaveOnlyOnce) {
                    DDbDfr dummy = DTrnDfrUtils.createDfrFromDps(miClient.getSession(), registry, moDpsSeriesNumber.getFkXmlTypeId());
                    mbCheckDfrOnSaveOnlyOnce = false;
                }
            }

            // Real DFR will be created in method DDbDps.save():

           registry.setAuxXmlTypeId(moDpsSeriesNumber.getFkXmlTypeId());
           registry.setAuxDfrRequired(moDpsSeriesNumber.getFkXmlTypeId() != DModSysConsts.TS_XML_TP_NA);
        }

        return registry;
    }

    @Override
    public DGuiValidation validateForm() {
        String message = "";
        Vector<DDbDpsSeriesNumber> dpsSeriesNumbers = null;
        DGuiValidation validation = moFields.validateFields();

        if (!validation.isValid()) {
            int tab = getDocumentTabbedPaneIndex(validation.getComponent());
            if (tab != -1) {
                validation.setTabbedPane(jtpDocument);
                validation.setTab(tab);
            }
        }
        else {
            if (DLibTimeUtils.digestYear(moDateDate.getValue())[0] != mnOriginalYear) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DATE_ + "'" + DGuiUtils.getLabelName(jlDateDate.getText()) + "'" +
                        DGuiConsts.ERR_MSG_FIELD_DATE_YEAR + DLibUtils.DecimalFormatCalendarYear.format(mnOriginalYear) + ".");
                validation.setComponent(moDateDate);
            }
            else if (moDpsSeriesNumber != null && moDpsSeriesNumber.getApprobationDate_n() != null && moDpsSeriesNumber.getApprobationDate_n().after(moDateDate.getValue())) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DATE_ + "'" + DGuiUtils.getLabelName(jlDateDate.getText()) + "'" +
                        DGuiConsts.ERR_MSG_FIELD_DATE_GREAT_EQUAL + DLibUtils.DateFormatDate.format(moDpsSeriesNumber.getApprobationDate_n()) + ", fecha de aprobación de los folios del documento.");
                validation.setComponent(moDateDate);
            }
            else if (moKeyDfrReceiverTaxRegime.isEnabled() && moKeyDfrReceiverTaxRegime.getValue()[0] == DModSysConsts.CS_TAX_REG_NA) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + moKeyDfrReceiverTaxRegime.getFieldName() + "'.");
                validation.setComponent(moKeyDfrReceiverTaxRegime);
            }
            else if (mbIsImportDeclaration && moTextImportDeclaration.getValue().isEmpty() && moDateImportDeclarationDate.getValue() != null) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + DGuiUtils.getLabelName(jlImportDeclaration.getText()) + "'.");
                validation.setComponent(moTextImportDeclaration);
                validation.setTabbedPane(jtpDocument);
                validation.setTab(DOC_TAB_BP);
            }
            else if (mbIsImportDeclaration && !moTextImportDeclaration.getValue().isEmpty() && moDateImportDeclarationDate.getValue() == null) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + DGuiUtils.getLabelName(jlImportDeclarationDate.getText()) + "'.");
                validation.setComponent(moDateImportDeclarationDate);
                validation.setTabbedPane(jtpDocument);
                validation.setTab(DOC_TAB_BP);
            }
            else if (mbIsImportDeclaration && !moTextImportDeclaration.getValue().isEmpty() && moTextImportDeclaration.getValue().length() != DDbDps.LEN_IMP_DEC) {
                validation.setMessage("La longitud del campo '" + DGuiUtils.getLabelName(jlImportDeclaration.getText()) + "' " + DGuiConsts.ERR_MSG_FIELD_VAL_EQUAL + " " + DDbDps.LEN_IMP_DEC + ".");
                validation.setComponent(moTextImportDeclaration);
                validation.setTabbedPane(jtpDocument);
                validation.setTab(DOC_TAB_BP);
            }
            else if (moKeyDfrIssuerTaxRegime.isEnabled() && moKeyDfrIssuerTaxRegime.getValue()[0] == DModSysConsts.CS_TAX_REG_NA) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + moKeyDfrIssuerTaxRegime.getFieldName() + "'.");
                validation.setComponent(moKeyDfrIssuerTaxRegime);
                validation.setTabbedPane(jtpDocument);
                validation.setTab(DOC_TAB_DFR);
            }
            else if (moDateCredit.getValue().before(moDateDate.getValue())) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DATE_ + "'" + DGuiUtils.getLabelName(jlDateCredit.getText()) + "'" +
                        DGuiConsts.ERR_MSG_FIELD_DATE_GREAT_EQUAL + DLibUtils.DateFormatDate.format(moDateDate.getValue()) + ".");
                validation.setComponent(moDateCredit);
            }
            else if (moKeyDfrMethodOfPayment.getSelectedIndex() > 0 && moKeyDfrMethodOfPayment.getSelectedItem().getCode().equals(DCfdi40Catalogs.MDP_PUE) && 
                    (moKeyModeOfPaymentType.getValue()[0] == DModSysConsts.FS_MOP_TP_ADV_APL || moKeyModeOfPaymentType.getValue()[0] == DModSysConsts.FS_MOP_TP_TO_DEF)) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + moKeyDfrMethodOfPayment.getFieldName() + "' es '" + moKeyDfrMethodOfPayment.getSelectedItem().getCode() + "'."
                        + "\n" + DGuiConsts.ERR_MSG_FIELD_DIF + "'" + moKeyModeOfPaymentType.getFieldName() + "'.");
                validation.setComponent(moKeyModeOfPaymentType);
            }
            else if (moKeyEmissionType.getValue()[0] != DModSysConsts.TS_EMI_TP_BPR && moKeyPaymentType.getValue()[0] != DModSysConsts.FS_PAY_TP_CSH &&
                    miClient.showMsgBoxConfirm("¿Está seguro que desea emitir la operación a crédito, dado que el tipo de emisíón es '" + moKeyEmissionType.getSelectedItem().getItem().toLowerCase() + "'?") != JOptionPane.YES_OPTION) {
                if (moKeyPaymentType.isEnabled()) {
                    validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + moKeyPaymentType.getFieldName() + "', si '" + moKeyEmissionType.getFieldName() + "' es '" + moKeyEmissionType.getSelectedItem().getItem() + "'.");
                    validation.setComponent(moKeyPaymentType);
                }
                else {
                    validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + moKeyEmissionType.getFieldName() + "', si '" + moKeyPaymentType.getFieldName() + "' es '" + moKeyPaymentType.getSelectedItem().getItem() + "'.");
                    validation.setComponent(moKeyEmissionType);
                }
            }
            else if (enableDfrGlobalFields() && !moBoolDfrGlobal.isSelected() && 
                    miClient.showMsgBoxConfirm("¿Está seguro que no desea seleccionar el campo '" + moBoolDfrGlobal.getText() + "'?") != JOptionPane.YES_OPTION) {
                validation.setMessage("Seleccionar el campo '" + moBoolDfrGlobal.getText() + "'.");
                validation.setComponent(moBoolDfrGlobal);
                validation.setTabbedPane(jtpDocument);
                validation.setTab(DOC_TAB_GBL);
            }
            else if (moBoolDfrGlobal.isSelected() && (moKeyDfrGlobalPeriodicity.getSelectedIndex() <= 0 || moKeyDfrGlobalMonths.getSelectedIndex() <= 0 || moCalDfrGlobalYear.getValue() == 0)) {
                validation.setMessage("Se debe completar la información de la sección '" + DGuiUtils.getLabelName(moBoolDfrGlobal.getText()) + "'.");
                validation.setComponent(moBoolDfrGlobal);
                validation.setTabbedPane(jtpDocument);
                validation.setTab(DOC_TAB_GBL);
            }
            else if (moGridDpsRows.getTable().getRowCount() == 0) {
                validation.setMessage(DUtilConsts.ERR_MSG_DOC_NO_ROWS);
                validation.setComponent(moTextFind);
            }
            else {
                // Confirm document number of new documents:

                if (moRegistry.isRegistryNew() && DTrnUtils.isDpsNumberAutomatic(moRegistry.getDpsClassKey()) && mnNewDpsNumber == moIntNumber.getValue()) {
                    /*
                     * ALG#001. Define and validate new document automatic number:
                     */

                    dpsSeriesNumbers = ((DDbUser) miClient.getSession().getUser()).getAuxBranchDpsSeriesNumbers(mnNewDpsSeriesId);

                    if (dpsSeriesNumbers.isEmpty()) {
                        validation.setMessage(DUtilConsts.ERR_MSG_DPS_SER_NUM_NON_AVA);
                        validation.setComponent(moIntNumber);
                    }
                    else if (dpsSeriesNumbers.size() > 1) {
                        validation.setMessage(DUtilConsts.ERR_MSG_DPS_SER_NUM_MUL_AVA);
                        validation.setComponent(moIntNumber);
                    }
                    else {
                        mnNewDpsNumber = DTrnUtils.getNextNumberForDps(miClient.getSession(), moRegistry.getDpsTypeKey(), msNewDpsSeries);

                        if (mnNewDpsNumber < dpsSeriesNumbers.get(0).getNumberStart()) {
                            mnNewDpsNumber = dpsSeriesNumbers.get(0).getNumberStart();
                        }
                        else if (dpsSeriesNumbers.get(0).getNumberEnd_n() != 0 && mnNewDpsNumber > dpsSeriesNumbers.get(0).getNumberEnd_n()) {
                            validation.setMessage(DUtilConsts.ERR_MSG_DPS_SER_NUM_MAX);
                            validation.setComponent(moIntNumber);
                        }
                    }

                    /*
                     * End of algorithm.
                     */

                    if (validation.isValid()) {
                        if (mnNewDpsNumber != moIntNumber.getValue()) {
                            moIntNumber.setValue(mnNewDpsNumber);
                        }
                    }
                }
                
                // Validate tax regime in sales documents:

                if (validation.isValid() && mbIsDocument && mnFormSubtype == DModSysConsts.TS_DPS_CT_SAL) {
                    for (DGridRow row : moGridDpsRows.getModel().getGridRows()) {
                        DDbDpsRow dpsRow = (DDbDpsRow) row;
                        if (dpsRow.getDbTaxRegimeId() != DModSysConsts.CS_TAX_REG_NA && dpsRow.getDbTaxRegimeId() != moKeyDfrIssuerTaxRegime.getValue()[0]) {
                            validation.setMessage(DGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + moKeyDfrIssuerTaxRegime.getFieldName() + "'" + 
                                    DGuiConsts.ERR_MSG_FIELD_VAL_EQUAL + "'" + ((String) miClient.getSession().readField(DModConsts.CS_TAX_REG, new int[] { dpsRow.getDbTaxRegimeId() }, DDbRegistry.FIELD_CODE))  + "'.");
                            validation.setComponent(moKeyDfrIssuerTaxRegime);
                            validation.setTabbedPane(jtpDocument);
                            validation.setTab(DOC_TAB_DFR);
                            break;
                        }
                    }
                }

                // Validate document number:

                if (validation.isValid()) {
                    moRegistry.setSeries(moTextSeries.getValue());
                    moRegistry.setNumber(moIntNumber.getValue());
                    moRegistry.setFkBizPartnerBizPartnerId(moKeyBranchAddress.getValue()[0]);

                    message = DTrnUtils.validateNumberForDps(miClient.getSession(), moRegistry);
                    if (!message.isEmpty()) {
                        validation.setMessage(message);
                        validation.setComponent(moIntNumber);
                    }
                    else if (moRegistry.isRegistryNew() && DTrnUtils.isDpsNumberAutomatic(moRegistry.getDpsClassKey())) {
                        moIntNumber.setValue(moRegistry.getNumber()); // new document number could be updated
                    }
                }
            }
        }

        return validation;
    }

    @Override
    public void setValue(final int type, final Object value) {
        switch (type) {
            case DModSysConsts.FLAG_IS_POS:
                mbIsPosModule = (Boolean) value;
                computeIsPos();
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
    public void notifyRowNew(int gridType, int gridSubtype, int row, DGridRow gridRow) {

    }

    @Override
    public void notifyRowEdit(int gridType, int gridSubtype, int row, DGridRow gridRow) {

    }

    @Override
    public void notifyRowDelete(int gridType, int gridSubtype, int row, DGridRow gridRow) {
        switch (gridType) {
            case DModConsts.T_DPS_NOT:
                break;
            case DModConsts.T_DPS_ROW:
                computeRowDeleted((DDbDpsRow) gridRow);
                break;
            default:
        }
    }

    @Override
    public void actionCancel() {
        boolean cancel = true;

        if (mnFormStatus == DGuiConsts.FORM_STATUS_EDIT && jbSave.isEnabled()) {
            cancel = miClient.showMsgBoxConfirm(DGuiConsts.MSG_CNF_FORM_CLS) == JOptionPane.YES_OPTION;
        }

        if (cancel) {
            freeLockByCancel();
            super.actionCancel();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbBizPartnerPick) {
                actionPerformedBizPartnerPick();
            }
            else if (button == jbBizPartnerEdit) {
                actionPerformedBizPartnerEdit();
            }
            else if (button == jbBranchAddressOfficialView) {
                actionPerformedBranchAddressOfficialView();
            }
            else if (button == jbExchangeRatePick) {
                actionPerformedExchangeRatePick();
            }
            else if (button == jbDfrCfdRelationsEdit) {
                actionPerformedCfdRelationsEdit();
            }
            else if (button == jbFind) {
                actionPerformedFind(null);
            }
            else if (button == jbRowClear) {
                actionPerformedRowClear();
            }
            else if (button == jbRowAdd) {
                actionPerformedRowAdd();
            }
            else if (button == jbDiscountDocSet) {
                actionPerformedDiscountDocSet();
            }
            else if (button == mjButtonLaunchCalc) {
                actionPerformedLaunchCalc();
            }
            else if (button == mjButtonEditItem) {
                actionPerformedEditItem();
            }
            else if (button == mjButtonShowRowNote) {
                actionPerformedShowNote();
            }
            else if (button == mjButtonShowRowLot) {
                actionPerformedShowLot();
            }
            else if (button == mjButtonShowRowSerialNumber) {
                actionPerformedShowSerialNumber();
            }
            else if (button == mjButtonShowDependentDocs) {
                actionPerformedShowDependentDocs();
            }
            else if (button == mjButtonShowAdjustedDoc) {
                actionPerformedShowAdjustedDoc();
            }
            else if (button == mjButtonAdjustmentForMoney) {
                actionPerformedAdjustmentForMoney();
            }
            else if (button == mjButtonAdjustmentForStock) {
                actionPerformedAdjustmentForStock();
            }
            else if (button == mjButtonAdjustmentDoc) {
                actionPerformedAdjustmentDoc();
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() instanceof JFormattedTextField) {
            JFormattedTextField field = (JFormattedTextField) e.getSource();

            if (field == moDateDate.getComponent()) {
                focusLostDate();
            }
            else if (field == moDateCredit.getComponent()) {
                focusLostCredit();
            }
        }
        else if (e.getSource() instanceof DBeanFieldInteger) {
            DBeanFieldInteger field = (DBeanFieldInteger) e.getSource();

            if (field == moIntCreditDays) {
                focusLostCreditDays();
            }
        }
        else if (e.getSource() instanceof DBeanFieldDecimal) {
            DBeanFieldDecimal field = (DBeanFieldDecimal) e.getSource();

            if (field == moDecRowQuantity) {
                computeRowTotal();
            }
            else if (field == moDecRowPriceUnitary) {
                computeRowTotal();
            }
            else if (field == moDecRowSubtotalProv) {
                if (moDecRowPriceUnitary.getValue() == 0) {
                    computeRowPriceUnitary();
                }
            }
            else if (field == moDecRowDiscountDoc) {
                computeRowTotal();
            }
            else if (field == moDecRowSubtotal) {
                if (moDecRowPriceUnitary.getValue() == 0) {
                    computeRowPriceUnitary();
                }
            }
            else if (field == moDecDiscountDocPercentage) {
                focusLostDiscountDocPercentage();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof DBeanFieldKey) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                DBeanFieldKey field = (DBeanFieldKey) e.getSource();

                if (field == moKeyBizPartner) {
                    itemStateChangedBizPartner();
                }
                else if (field == moKeyBranchAddress) {
                    itemStateChangedBranchAddress();
                }
                else if (field == moKeyCurrency) {
                    itemStateChangedCurrency();
                }
                else if (field == moKeyPaymentType) {
                    itemStateChangedPaymentType();
                }
            }
        }
        else if (e.getSource() instanceof DBeanFieldBoolean) {
            DBeanFieldBoolean field = (DBeanFieldBoolean) e.getSource();

            if (field == moBoolDfrGlobal) {
                itemStateChangedDfrGlobal();
            }
            else if (field == moBoolRowNote) {
                itemStateChangedRowNote();
            }
            else if (field == moBoolRowCfdPredial) {
                itemStateChangedRowPredial();
            }
            else if (field == moBoolDiscountDocApplying) {
                itemStateChangedDiscountDocApplying(true);
            }
            else if (field == moBoolDiscountDocPercentageApplying) {
                itemStateChangedDiscountDocPercentageApplying(true);
            }
        }
    }
}
