/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.lad.form;

import cfd.ver40.DCfdi40Catalogs;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import sa.lib.SLibUtils;
import sba.gui.DGuiClientSessionCustom;
import sba.gui.cat.DXmlCatalog;
import sba.gui.cat.DXmlCatalogEntry;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibTimeConsts;
import sba.lib.DLibTimeUtils;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridPaneForm;
import sba.lib.grid.DGridRow;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiFields;
import sba.lib.gui.DGuiItem;
import sba.lib.gui.DGuiParams;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiValidation;
import sba.lib.gui.bean.DBeanFieldBoolean;
import sba.lib.gui.bean.DBeanFieldDecimal;
import sba.lib.gui.bean.DBeanFieldInteger;
import sba.lib.gui.bean.DBeanFieldKey;
import sba.lib.gui.bean.DBeanFieldRadio;
import sba.lib.gui.bean.DBeanFieldText;
import sba.lib.gui.bean.DBeanForm;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.itm.db.DDbItem;
import sba.mod.itm.db.DDbUnit;
import sba.mod.lad.db.DDbBol;
import sba.mod.lad.db.DDbBolLocation;
import sba.mod.lad.db.DDbBolMerchandise;
import sba.mod.lad.db.DDbBolMerchandiseMove;
import sba.mod.lad.db.DDbBolTransportFigure;
import sba.mod.lad.db.DDbBolTransportFigureTransportPart;
import sba.mod.lad.db.DDbBolTruck;
import sba.mod.lad.db.DDbBolTruckTrailer;
import sba.mod.lad.db.DDbLocation;
import sba.mod.lad.db.DDbSysTransportPartType;
import sba.mod.lad.db.DDbTrailer;
import sba.mod.lad.db.DDbTransportFigure;
import sba.mod.lad.db.DDbTruck;
import sba.mod.lad.db.DDbTruckTrailer;
import sba.mod.lad.db.DDbTruckTransportFigure;
import sba.mod.lad.db.DDbTruckTransportFigureTransportPart;
import sba.mod.lad.db.DLadBolIntlTransport;
import sba.mod.lad.db.DLadCatalogAddressCountry;
import sba.mod.lad.db.DLadCatalogAddressCounty;
import sba.mod.lad.db.DLadCatalogAddressDistrict;
import sba.mod.lad.db.DLadCatalogAddressLocality;
import sba.mod.lad.db.DLadCatalogAddressState;
import sba.mod.lad.db.DLadCatalogConsts;
import sba.mod.lad.db.DLadCatalogHazardousMaterial;
import sba.mod.lad.db.DLadCatalogPackaging;

/**
 *
 * @author Sergio Flores
 */
public class DFormBol extends DBeanForm implements ActionListener, ItemListener, FocusListener, KeyListener, ListSelectionListener {
    
    protected final static int ACTION_ADD = 1;
    protected final static int ACTION_CREATE = 2;
    protected final static int ACTION_COPY = 3;
    protected final static int ACTION_MODIFY = 4;
    protected final static int ACTION_CANCEL = 9;
    
    protected final static String TXT_ACTION_ADD = "Adición";
    protected final static String TXT_ACTION_CREATE = "Creación";
    protected final static String TXT_ACTION_COPY = "Copia";
    protected final static String TXT_ACTION_MODIFY = "Modificación";
    protected final static String TXT_CFD_ITEM_KEY = "ProdServ";
    protected final static String TXT_UNKNOWN = "?";
    
    protected final static int TAB_IDX_LOCATION = 0;
    protected final static int TAB_IDX_MERCHANDISE = 1;
    protected final static int TAB_IDX_TRUCK = 2;
    protected final static int TAB_IDX_TPT_FIGURE = 3;
    
    protected final static int NAV_ACTION_START = 1;
    protected final static int NAV_ACTION_RESTART = 2;
    protected final static int NAV_ACTION_PREV = 3;
    protected final static int NAV_ACTION_NEXT = 4;
    
    private DGuiClientSessionCustom moSessionCustom;
    
    protected DDbBol moBol;
    protected DDbBolLocation moBolLocation;
    protected DDbBolMerchandise moBolMerchandise;
    protected DDbBolMerchandiseMove moBolMerchandiseMove;
    protected DDbBolTruck moBolTruck;
    protected DDbBolTruckTrailer moBolTruckTrailer;
    protected DDbBolTransportFigure moBolTptFigure;
    protected DDbBolTransportFigureTransportPart moBolTptFigureTptPart;
    
    protected DDialogBol moDialogBol;

    protected String msTransportTypeTruckCode;
    protected DXmlCatalog moXmlBolIsthmusRegistry;
    protected DXmlCatalog moXmlTruckTransportConfig;
    protected DXmlCatalog moXmlTruckPermissionType;
    protected DXmlCatalog moXmlTruckTrailerSubtype;

    protected int[] manTemplateKey;
    protected boolean mbForceCancel;
    protected DLadBolIntlTransport moBolIntlTransport;
    
    protected int mnBolWeightUnitId;
    protected double mdBolDistanceKm;
    protected double mdBolMerchandiseWeightKg;
    protected int mnBolMerchandiseNumber;

    protected int mnTempBolLocationId;
    protected DDbItem moMerchItem;
    protected DDbUnit moMerchUnit;
    protected int mnTruckIsTrailerRequired;
    protected boolean mbTptFigureTptPartIsRequired;
    protected boolean mbAdjustingGrids;
    
    protected DGridPaneForm moGridLocations;
    protected DGridPaneForm moGridMerchandises;
    protected DGridPaneForm moGridMerchandisesMoves;
    protected DGridPaneForm moGridTrucks;
    protected DGridPaneForm moGridTrucksTrailers;
    protected DGridPaneForm moGridTptFigures;
    protected DGridPaneForm moGridTptFiguresTptParts;
    
    protected DGuiFields moFieldsLocation;
    protected DGuiFields moFieldsMerchandise;
    protected DGuiFields moFieldsMerchandiseMove;
    protected DGuiFields moFieldsTruck;
    protected DGuiFields moFieldsTruckTrailer;
    protected DGuiFields moFieldsTptFigure;
    protected DGuiFields moFieldsTptFigureTptPart;
    
    protected int mnActionLocation;
    protected int mnActionMerchandise;
    protected int mnActionMerchandiseMove;
    protected int mnActionTruck;
    protected int mnActionTruckTrailer;
    protected int mnActionTptFigure;
    protected int mnActionTptFigureTptPart;
    
    protected boolean mbEditionStarted;
    protected boolean mbEditingLocation;
    protected boolean mbEditingMerchandise;
    protected boolean mbEditingMerchandiseMove;
    protected boolean mbEditingTruck;
    protected boolean mbEditingTruckTrailer;
    protected boolean mbEditingTptFigure;
    protected boolean mbEditingTptFigureTptPart;
    
    protected DPickerElement moPickerLocation;
    protected DPickerElement moPickerTruck;
    protected DPickerElement moPickerTrail;
    protected DPickerElement moPickerTptFigure;
    protected DPickerElement moPickerTptPartType;
    protected DPickerElement moPickerItem;
    
    protected DPickerCatalogAddressState moPickerCatalogAddressState;
    protected DPickerCatalogAddressCounty moPickerCatalogAddressCounty;
    protected DPickerCatalogAddressLocality moPickerCatalogAddressLocality;
    protected DPickerCatalogAddressDistrict moPickerCatalogAddressDistrict;
    protected DPickerCatalogHazardousMaterial moPickerCatalogHazardousMaterial;
    protected DPickerCatalogPackaging moPickerCatalogPackaging;

    /**
     * Creates new form DFormBol
     * @param client GUI Client.
     * @param mode Supported options: DDbBol.MODE_REAL, DDbBol.MODE_TEMP.
     * @param title Title.
     */
    public DFormBol(DGuiClient client, int mode, String title) {
        setFormSettings(client, DGuiConsts.BEAN_FORM_EDIT, DModConsts.L_BOL, mode, title);
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

        bgMerchDimensionUnits = new javax.swing.ButtonGroup();
        bgMerchHazardousMaterial = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jpHeader = new javax.swing.JPanel();
        jpHeaderBol = new javax.swing.JPanel();
        jpHeaderBol1 = new javax.swing.JPanel();
        jlBolTransportType = new javax.swing.JLabel();
        jtfBolTransportType = new javax.swing.JTextField();
        jtfBolVersion = new javax.swing.JTextField();
        jbBolUpdateVersion = new javax.swing.JButton();
        jpHeaderBol2 = new javax.swing.JPanel();
        jlBolNumber = new javax.swing.JLabel();
        moKeyBolSeries = new sba.lib.gui.bean.DBeanFieldKey();
        jtfBolNumber = new javax.swing.JTextField();
        jpHeaderBol3 = new javax.swing.JPanel();
        jlBolDate = new javax.swing.JLabel();
        moDateBolDate = new sba.lib.gui.bean.DBeanFieldDate();
        jtfBolStatus = new javax.swing.JTextField();
        jpHeaderBol4 = new javax.swing.JPanel();
        jtfBolDfrUuid = new javax.swing.JTextField();
        jpHeaderBol5 = new javax.swing.JPanel();
        jtfBolBolUuid = new javax.swing.JTextField();
        jpHeaderCfg = new javax.swing.JPanel();
        moBoolBolIntlTransport = new sba.lib.gui.bean.DBeanFieldBoolean();
        jpHeaderCfg2 = new javax.swing.JPanel();
        jtfBolIntlTransport = new javax.swing.JTextField();
        jbBolSetIntlTransport = new javax.swing.JButton();
        moBoolBolIsthmus = new sba.lib.gui.bean.DBeanFieldBoolean();
        jpHeaderIntl4 = new javax.swing.JPanel();
        jlBolIsthmusOrigin = new javax.swing.JLabel();
        moKeyBolIsthmusOrigin = new sba.lib.gui.bean.DBeanFieldKey();
        jpHeaderIntl45 = new javax.swing.JPanel();
        jlBolIsthmusDestiny = new javax.swing.JLabel();
        moKeyBolIsthmusDestiny = new sba.lib.gui.bean.DBeanFieldKey();
        jpHeaderLocMerch = new javax.swing.JPanel();
        jpHeaderLocMerch1 = new javax.swing.JPanel();
        jlBolDeparture = new javax.swing.JLabel();
        jtfBolDeparturelDatetime = new javax.swing.JTextField();
        jtfBolDepartureLocation = new javax.swing.JTextField();
        jpHeaderLocMerch2 = new javax.swing.JPanel();
        jlBolArrival = new javax.swing.JLabel();
        jtfBolArrivalDatetime = new javax.swing.JTextField();
        jtfBolArrivalLocation = new javax.swing.JTextField();
        jpHeaderLocMerch3 = new javax.swing.JPanel();
        jlBolDistanceKm = new javax.swing.JLabel();
        jtfBolDistanceKm = new javax.swing.JTextField();
        jlBolDistanceKmUnit = new javax.swing.JLabel();
        jlBolMerchandiseNumber = new javax.swing.JLabel();
        jpHeaderLocMerch4 = new javax.swing.JPanel();
        jlBolMerchandiseWeightKg = new javax.swing.JLabel();
        jtfBolMerchandiseWeightKg = new javax.swing.JTextField();
        jlBolMerchandiseWeightKgUnit = new javax.swing.JLabel();
        jtfBolMerchandiseNumber = new javax.swing.JTextField();
        moBoolBolMerchandiseInverseLogistics = new sba.lib.gui.bean.DBeanFieldBoolean();
        jpHeaderTemp = new javax.swing.JPanel();
        moBoolBolTemplate = new sba.lib.gui.bean.DBeanFieldBoolean();
        jpHeaderTemp2 = new javax.swing.JPanel();
        jlBolTemplateName = new javax.swing.JLabel();
        moTextBolTemplateName = new sba.lib.gui.bean.DBeanFieldText();
        jpHeaderTemp3 = new javax.swing.JPanel();
        jlBolTemplateCode = new javax.swing.JLabel();
        moTextBolTemplateCode = new sba.lib.gui.bean.DBeanFieldText();
        jpHeaderTemp4 = new javax.swing.JPanel();
        jlBolBolTemplate = new javax.swing.JLabel();
        jtfBolBolTemplate = new javax.swing.JTextField();
        jpHeaderTemp5 = new javax.swing.JPanel();
        jtpWizard = new javax.swing.JTabbedPane();
        jpWizardLoc = new javax.swing.JPanel();
        jpLoc = new javax.swing.JPanel();
        jpLocInput = new javax.swing.JPanel();
        jpLocInput1 = new javax.swing.JPanel();
        jpLocInput11 = new javax.swing.JPanel();
        jlLocLocationType = new javax.swing.JLabel();
        moKeyLocLocationType = new sba.lib.gui.bean.DBeanFieldKey();
        jbLocEditType = new javax.swing.JButton();
        jpLocInput12 = new javax.swing.JPanel();
        jlLocName = new javax.swing.JLabel();
        moTextLocName = new sba.lib.gui.bean.DBeanFieldText();
        jpLocInput13 = new javax.swing.JPanel();
        jlLocCode = new javax.swing.JLabel();
        moTextLocCode = new sba.lib.gui.bean.DBeanFieldText();
        jbLocGetNextCode = new javax.swing.JButton();
        jpLocInput14 = new javax.swing.JPanel();
        jlLocLocationId = new javax.swing.JLabel();
        jtfLocLocationId = new javax.swing.JTextField();
        jpLocInput15 = new javax.swing.JPanel();
        jlLocArrivalDepartureDatetime = new javax.swing.JLabel();
        moDatetimeLocArrivalDepartureDatetime = new sba.lib.gui.bean.DBeanFieldDatetime();
        jlLocArrivalDepartureDatetimeHelp = new javax.swing.JLabel();
        jpLocInput16 = new javax.swing.JPanel();
        jlLocSourceLocation = new javax.swing.JLabel();
        moKeyLocSourceLocation = new sba.lib.gui.bean.DBeanFieldKey();
        jpLocInput17 = new javax.swing.JPanel();
        jlLocDistanceKm = new javax.swing.JLabel();
        moDecLocDistanceKm = new sba.lib.gui.bean.DBeanFieldDecimal();
        jlLocDistanceKmUnit = new javax.swing.JLabel();
        jpLocInput18 = new javax.swing.JPanel();
        jpLocInput181 = new javax.swing.JPanel();
        jtfLocPk = new javax.swing.JTextField();
        jpLocInput182 = new javax.swing.JPanel();
        moBoolLocUpdate = new sba.lib.gui.bean.DBeanFieldBoolean();
        jpLocInput2 = new javax.swing.JPanel();
        jpLocInput21 = new javax.swing.JPanel();
        jlLocAddressCountry = new javax.swing.JLabel();
        moKeyLocAddressCountry = new sba.lib.gui.bean.DBeanFieldKey();
        jpLocInput22 = new javax.swing.JPanel();
        jlLocAddressState = new javax.swing.JLabel();
        moTextLocAddressStateCode = new sba.lib.gui.bean.DBeanFieldText();
        moTextLocAddressStateName = new sba.lib.gui.bean.DBeanFieldText();
        jpLocInput23 = new javax.swing.JPanel();
        jlLocAddressCounty = new javax.swing.JLabel();
        moTextLocAddressCountyCode = new sba.lib.gui.bean.DBeanFieldText();
        moTextLocAddressCountyName = new sba.lib.gui.bean.DBeanFieldText();
        jpLocInput24 = new javax.swing.JPanel();
        jlLocAddressLocality = new javax.swing.JLabel();
        moTextLocAddressLocalityCode = new sba.lib.gui.bean.DBeanFieldText();
        moTextLocAddressLocalityName = new sba.lib.gui.bean.DBeanFieldText();
        jpLocInput25 = new javax.swing.JPanel();
        jlLocAddressZipCode = new javax.swing.JLabel();
        moTextLocAddressZipCode = new sba.lib.gui.bean.DBeanFieldText();
        jpLocInput26 = new javax.swing.JPanel();
        jlLocAddressDistrict = new javax.swing.JLabel();
        moTextLocAddressDistrictCode = new sba.lib.gui.bean.DBeanFieldText();
        moTextLocAddressDistrictName = new sba.lib.gui.bean.DBeanFieldText();
        jpLocInput27 = new javax.swing.JPanel();
        jlLocAddressStreet = new javax.swing.JLabel();
        moTextLocAddressStreet = new sba.lib.gui.bean.DBeanFieldText();
        moTextLocAddressNumberExt = new sba.lib.gui.bean.DBeanFieldText();
        moTextLocAddressNumberInt = new sba.lib.gui.bean.DBeanFieldText();
        jpLocInput28 = new javax.swing.JPanel();
        jlLocAddressReference = new javax.swing.JLabel();
        moTextLocAddressReference = new sba.lib.gui.bean.DBeanFieldText();
        jpLocInput3 = new javax.swing.JPanel();
        jpLocInput31 = new javax.swing.JPanel();
        jlLocNotes = new javax.swing.JLabel();
        moTextLocNotes = new sba.lib.gui.bean.DBeanFieldText();
        jpLocInput32 = new javax.swing.JPanel();
        jpLocInput33 = new javax.swing.JPanel();
        jpLocInput34 = new javax.swing.JPanel();
        jpLocInput35 = new javax.swing.JPanel();
        jpLocInput36 = new javax.swing.JPanel();
        jpLocInput37 = new javax.swing.JPanel();
        jpLocInput38 = new javax.swing.JPanel();
        jpLocCrud1 = new javax.swing.JPanel();
        jbLocAdd = new javax.swing.JButton();
        jbLocCreate = new javax.swing.JButton();
        jbLocCopy = new javax.swing.JButton();
        jbLocModify = new javax.swing.JButton();
        jbLocRemove = new javax.swing.JButton();
        jbLocMoveUp = new javax.swing.JButton();
        jbLocMoveDown = new javax.swing.JButton();
        jpLocCrud2 = new javax.swing.JPanel();
        jtfLocCrud = new javax.swing.JTextField();
        jbLocOk = new javax.swing.JButton();
        jbLocCancel = new javax.swing.JButton();
        jpWizardMerch = new javax.swing.JPanel();
        jpMerch = new javax.swing.JPanel();
        jpMerchInput = new javax.swing.JPanel();
        jpMerchInput1 = new javax.swing.JPanel();
        jpMerchInput11 = new javax.swing.JPanel();
        jlMerchItem = new javax.swing.JLabel();
        moKeyMerchItem = new sba.lib.gui.bean.DBeanFieldKey();
        jbMerchPickItem = new javax.swing.JButton();
        jpMerchInput12 = new javax.swing.JPanel();
        jlMerchItemDescription = new javax.swing.JLabel();
        moTextMerchItemDescription = new sba.lib.gui.bean.DBeanFieldText();
        jpMerchInput14 = new javax.swing.JPanel();
        jlMerchUnit = new javax.swing.JLabel();
        moKeyMerchUnit = new sba.lib.gui.bean.DBeanFieldKey();
        jpMerchInput15 = new javax.swing.JPanel();
        jlMerchUnitDescription = new javax.swing.JLabel();
        moTextMerchUnitDescription = new sba.lib.gui.bean.DBeanFieldText();
        jpMerchInput13 = new javax.swing.JPanel();
        jlMerchQuantity = new javax.swing.JLabel();
        moDecMerchQuantity = new sba.lib.gui.bean.DBeanFieldDecimal();
        jtfMerchQuantityMoved = new javax.swing.JTextField();
        jpMerchInput16 = new javax.swing.JPanel();
        jlMerchWeightKg = new javax.swing.JLabel();
        moDecMerchWeightKg = new sba.lib.gui.bean.DBeanFieldDecimal();
        jlMerchWeightKgUnit = new javax.swing.JLabel();
        jbMerchSetWeightKg = new javax.swing.JButton();
        jlMerchRatioKgHint = new javax.swing.JLabel();
        jpMerchInput17 = new javax.swing.JPanel();
        jlMerchDimensions = new javax.swing.JLabel();
        moIntMerchDimensionsLength = new sba.lib.gui.bean.DBeanFieldInteger();
        jlMerchDimensions1 = new javax.swing.JLabel();
        moIntMerchDimensionsHeight = new sba.lib.gui.bean.DBeanFieldInteger();
        jlMerchDimensions2 = new javax.swing.JLabel();
        moIntMerchDimensionsWidth = new sba.lib.gui.bean.DBeanFieldInteger();
        moRadMerchDimensionsCm = new sba.lib.gui.bean.DBeanFieldRadio();
        moRadMerchDimensionsPlg = new sba.lib.gui.bean.DBeanFieldRadio();
        jpMerchInput18 = new javax.swing.JPanel();
        jlMerchDimensionsHint = new javax.swing.JLabel();
        jtfMerchDimensionsHint = new javax.swing.JTextField();
        jlMerchCfdItemKeySeparator = new javax.swing.JLabel();
        jlMerchCfdItemKey = new javax.swing.JLabel();
        jpMerchInput2 = new javax.swing.JPanel();
        jpMerchInput21 = new javax.swing.JPanel();
        jlMerchCurrency = new javax.swing.JLabel();
        moKeyMerchCurrency = new sba.lib.gui.bean.DBeanFieldKey();
        jpMerchInput22 = new javax.swing.JPanel();
        jlMerchValue = new javax.swing.JLabel();
        moCurMerchValue = new sba.lib.gui.bean.DBeanCompoundFieldCurrency();
        jpMerchInput23 = new javax.swing.JPanel();
        moBoolMerchHazardousMaterial = new sba.lib.gui.bean.DBeanFieldBoolean();
        moRadMerchHazardousMaterialYes = new sba.lib.gui.bean.DBeanFieldRadio();
        moRadMerchHazardousMaterialNo = new sba.lib.gui.bean.DBeanFieldRadio();
        jpMerchInput24 = new javax.swing.JPanel();
        jlMerchHazardousMaterial = new javax.swing.JLabel();
        moTextMerchHazardousMaterialCode = new sba.lib.gui.bean.DBeanFieldText();
        moTextMerchHazardousMaterialName = new sba.lib.gui.bean.DBeanFieldText();
        jpMerchInput25 = new javax.swing.JPanel();
        jlMerchPackaging = new javax.swing.JLabel();
        moTextMerchPackagingCode = new sba.lib.gui.bean.DBeanFieldText();
        moTextMerchPackagingName = new sba.lib.gui.bean.DBeanFieldText();
        jpMerchInput26 = new javax.swing.JPanel();
        jlMerchTariff = new javax.swing.JLabel();
        moTextMerchTariff = new sba.lib.gui.bean.DBeanFieldText();
        jpMerchInput27 = new javax.swing.JPanel();
        jlMerchImportRequest = new javax.swing.JLabel();
        moIntMerchImportRequest1 = new sba.lib.gui.bean.DBeanFieldInteger();
        moIntMerchImportRequest2 = new sba.lib.gui.bean.DBeanFieldInteger();
        moIntMerchImportRequest3 = new sba.lib.gui.bean.DBeanFieldInteger();
        moIntMerchImportRequest4 = new sba.lib.gui.bean.DBeanFieldInteger();
        jpMerchInput28 = new javax.swing.JPanel();
        jlMerchImportRequestHint = new javax.swing.JLabel();
        jtfMerchImportRequestHint = new javax.swing.JTextField();
        jpMerchInput3 = new javax.swing.JPanel();
        jpMerchInput3N = new javax.swing.JPanel();
        jpMerchInput3N1 = new javax.swing.JPanel();
        jlMerchMoveSource = new javax.swing.JLabel();
        moKeyMerchMoveSource = new sba.lib.gui.bean.DBeanFieldKey();
        jpMerchInput3N2 = new javax.swing.JPanel();
        jlMerchMoveDestiny = new javax.swing.JLabel();
        moKeyMerchMoveDestiny = new sba.lib.gui.bean.DBeanFieldKey();
        jpMerchInput3N3 = new javax.swing.JPanel();
        jlMerchMoveQuantity = new javax.swing.JLabel();
        moDecMerchMoveQuantity = new sba.lib.gui.bean.DBeanFieldDecimal();
        jbMerchMoveSetQuantity = new javax.swing.JButton();
        jpMerchInput3N4 = new javax.swing.JPanel();
        jpMerchInput3N41 = new javax.swing.JPanel();
        jbMerchMoveAdd = new javax.swing.JButton();
        jbMerchMoveCreate = new javax.swing.JButton();
        jbMerchMoveModify = new javax.swing.JButton();
        jbMerchMoveRemove = new javax.swing.JButton();
        jpMerchInput3N42 = new javax.swing.JPanel();
        jtfMerchMoveCrud = new javax.swing.JTextField();
        jbMerchMoveOk = new javax.swing.JButton();
        jbMerchMoveCancel = new javax.swing.JButton();
        jpMerchMoveGrid = new javax.swing.JPanel();
        jpMerchCrud1 = new javax.swing.JPanel();
        jbMerchAdd = new javax.swing.JButton();
        jbMerchCreate = new javax.swing.JButton();
        jbMerchCopy = new javax.swing.JButton();
        jbMerchModify = new javax.swing.JButton();
        jbMerchRemove = new javax.swing.JButton();
        jbMerchMoveUp = new javax.swing.JButton();
        jbMerchMoveDown = new javax.swing.JButton();
        jpMerchCrud2 = new javax.swing.JPanel();
        jtfMerchCrud = new javax.swing.JTextField();
        jbMerchOk = new javax.swing.JButton();
        jbMerchCancel = new javax.swing.JButton();
        jpWizardTruck = new javax.swing.JPanel();
        jpTruck = new javax.swing.JPanel();
        jpTruckInput = new javax.swing.JPanel();
        jpTruckInput1 = new javax.swing.JPanel();
        jpTruckInput11 = new javax.swing.JPanel();
        jlTruckTransportConfig = new javax.swing.JLabel();
        moKeyTruckTransportConfig = new sba.lib.gui.bean.DBeanFieldKey();
        jbTruckEditConfig = new javax.swing.JButton();
        jpTruckInput12 = new javax.swing.JPanel();
        jlTruckName = new javax.swing.JLabel();
        moTextTruckName = new sba.lib.gui.bean.DBeanFieldText();
        jpTruckInput13 = new javax.swing.JPanel();
        jlTruckCode = new javax.swing.JLabel();
        moTextTruckCode = new sba.lib.gui.bean.DBeanFieldText();
        jbTruckGetNextCode = new javax.swing.JButton();
        jlTruckPlate = new javax.swing.JLabel();
        moTextTruckPlate = new sba.lib.gui.bean.DBeanFieldText();
        jpTruckInput14 = new javax.swing.JPanel();
        jlTruckWeightTon = new javax.swing.JLabel();
        moDecTruckWeightTon = new sba.lib.gui.bean.DBeanFieldDecimal();
        jlTruckWeightTonUnit = new javax.swing.JLabel();
        jlTruckModel = new javax.swing.JLabel();
        moYearTruckModel = new sba.lib.gui.bean.DBeanFieldCalendarYear();
        jpTruckInput15 = new javax.swing.JPanel();
        jlTruckWeightGrossTon = new javax.swing.JLabel();
        moDecTruckWeightGrossTon = new sba.lib.gui.bean.DBeanFieldDecimal();
        jlTruckWeightGrossTonUnit = new javax.swing.JLabel();
        jbTruckSetWeightGrossTon = new javax.swing.JButton();
        jpTruckInput16 = new javax.swing.JPanel();
        jlTruckPermissionType = new javax.swing.JLabel();
        moKeyTruckPermissionType = new sba.lib.gui.bean.DBeanFieldKey();
        jpTruckInput17 = new javax.swing.JPanel();
        jlTruckPermissionNumber = new javax.swing.JLabel();
        moTextTruckPermissionNumber = new sba.lib.gui.bean.DBeanFieldText();
        jpTruckInput18 = new javax.swing.JPanel();
        jpTruckInput181 = new javax.swing.JPanel();
        jtfTruckPk = new javax.swing.JTextField();
        jpTruckInput182 = new javax.swing.JPanel();
        moBoolTruckUpdate = new sba.lib.gui.bean.DBeanFieldBoolean();
        jpTruckInput2 = new javax.swing.JPanel();
        jpTruckInput21 = new javax.swing.JPanel();
        jlTruckCivilInsurance = new javax.swing.JLabel();
        moTextTruckCivilInsurance = new sba.lib.gui.bean.DBeanFieldText();
        jpTruckInput22 = new javax.swing.JPanel();
        jlTruckCivilPolicy = new javax.swing.JLabel();
        moTextTruckCivilPolicy = new sba.lib.gui.bean.DBeanFieldText();
        jpTruckInput23 = new javax.swing.JPanel();
        jlTruckEnvironmentInsurance = new javax.swing.JLabel();
        moTextTruckEnvironmentInsurance = new sba.lib.gui.bean.DBeanFieldText();
        jpTruckInput24 = new javax.swing.JPanel();
        jlTruckEnvironmentPolicy = new javax.swing.JLabel();
        moTextTruckEnvironmentPolicy = new sba.lib.gui.bean.DBeanFieldText();
        jpTruckInput25 = new javax.swing.JPanel();
        jlTruckCargoInsurance = new javax.swing.JLabel();
        moTextTruckCargoInsurance = new sba.lib.gui.bean.DBeanFieldText();
        jpTruckInput26 = new javax.swing.JPanel();
        jlTruckCargoPolicy = new javax.swing.JLabel();
        moTextTruckCargoPolicy = new sba.lib.gui.bean.DBeanFieldText();
        jpTruckInput27 = new javax.swing.JPanel();
        jlTruckPrime = new javax.swing.JLabel();
        moCurTruckPrime = new sba.lib.gui.bean.DBeanCompoundFieldCurrency();
        jpTruckInput28 = new javax.swing.JPanel();
        jpTruckInput3 = new javax.swing.JPanel();
        jpTruckInput3N = new javax.swing.JPanel();
        jpTruckInput3N1 = new javax.swing.JPanel();
        jlTruckTrailSubtype = new javax.swing.JLabel();
        moKeyTruckTrailSubtype = new sba.lib.gui.bean.DBeanFieldKey();
        jbTruckTrailEditSubtype = new javax.swing.JButton();
        jpTruckInput3N2 = new javax.swing.JPanel();
        jlTruckTrailPlate = new javax.swing.JLabel();
        moTextTruckTrailPlate = new sba.lib.gui.bean.DBeanFieldText();
        jpTruckInput3N3 = new javax.swing.JPanel();
        jpTruckInput3N31 = new javax.swing.JPanel();
        jtfTruckTrailPk = new javax.swing.JTextField();
        jtfTruckTrailIsNeeded = new javax.swing.JTextField();
        jpTruckInput3N32 = new javax.swing.JPanel();
        moBoolTruckTrailUpdate = new sba.lib.gui.bean.DBeanFieldBoolean();
        jpTruckInput3N4 = new javax.swing.JPanel();
        jpTruckInput3N41 = new javax.swing.JPanel();
        jbTruckTrailAdd = new javax.swing.JButton();
        jbTruckTrailCreate = new javax.swing.JButton();
        jbTruckTrailModify = new javax.swing.JButton();
        jbTruckTrailRemove = new javax.swing.JButton();
        jpTptInput3N42 = new javax.swing.JPanel();
        jtfTruckTrailCrud = new javax.swing.JTextField();
        jbTruckTrailOk = new javax.swing.JButton();
        jbTruckTrailCancel = new javax.swing.JButton();
        jpTruckTrailGrid = new javax.swing.JPanel();
        jpTruckCrud1 = new javax.swing.JPanel();
        jbTruckAdd = new javax.swing.JButton();
        jbTruckCreate = new javax.swing.JButton();
        jbTruckCopy = new javax.swing.JButton();
        jbTruckModify = new javax.swing.JButton();
        jbTruckRemove = new javax.swing.JButton();
        jbTruckMoveUp = new javax.swing.JButton();
        jbTruckMoveDown = new javax.swing.JButton();
        jpTruckCrud2 = new javax.swing.JPanel();
        jtfTruckCrud = new javax.swing.JTextField();
        jbTruckOk = new javax.swing.JButton();
        jbTruckCancel = new javax.swing.JButton();
        jpWizardTptFigure = new javax.swing.JPanel();
        jpTptFig = new javax.swing.JPanel();
        jpTptFigInput = new javax.swing.JPanel();
        jpTptFigInput1 = new javax.swing.JPanel();
        jpTptFigInput11 = new javax.swing.JPanel();
        jlTptFigTransportFigureType = new javax.swing.JLabel();
        moKeyTptFigTransportFigureType = new sba.lib.gui.bean.DBeanFieldKey();
        jbTptFigEditType = new javax.swing.JButton();
        jpTptFigInput12 = new javax.swing.JPanel();
        jlTptFigName = new javax.swing.JLabel();
        moTextTptFigName = new sba.lib.gui.bean.DBeanFieldText();
        jpTptFigInput13 = new javax.swing.JPanel();
        jlTptFigCode = new javax.swing.JLabel();
        moTextTptFigCode = new sba.lib.gui.bean.DBeanFieldText();
        jbTptFigGetNextCode = new javax.swing.JButton();
        jlTptFigMail = new javax.swing.JLabel();
        moTextTptFigMail = new sba.lib.gui.bean.DBeanFieldText();
        jpTptFigInput14 = new javax.swing.JPanel();
        jlTptFigFiscalId = new javax.swing.JLabel();
        moTextTptFigFiscalId = new sba.lib.gui.bean.DBeanFieldText();
        jpTptFigInput15 = new javax.swing.JPanel();
        jlTptFigDriverLicense = new javax.swing.JLabel();
        moTextTptFigDriverLicense = new sba.lib.gui.bean.DBeanFieldText();
        jpTptFigInput16 = new javax.swing.JPanel();
        jlTptFigFigureCountry = new javax.swing.JLabel();
        moKeyTptFigFigureCountry = new sba.lib.gui.bean.DBeanFieldKey();
        jpTptFigInput17 = new javax.swing.JPanel();
        jlTptFigForeignId = new javax.swing.JLabel();
        moTextTptFigForeignId = new sba.lib.gui.bean.DBeanFieldText();
        jpTptFigInput18 = new javax.swing.JPanel();
        jpTptFigInput181 = new javax.swing.JPanel();
        jtfTptFigPk = new javax.swing.JTextField();
        jpTptFigInput182 = new javax.swing.JPanel();
        moBoolTptFigUpdate = new sba.lib.gui.bean.DBeanFieldBoolean();
        jpTptFigInput2 = new javax.swing.JPanel();
        jpTptFigInput21 = new javax.swing.JPanel();
        jlTptFigAddressCountry = new javax.swing.JLabel();
        moKeyTptFigAddressCountry = new sba.lib.gui.bean.DBeanFieldKey();
        jpTptFigInput22 = new javax.swing.JPanel();
        jlTptFigAddressState = new javax.swing.JLabel();
        moTextTptFigAddressStateCode = new sba.lib.gui.bean.DBeanFieldText();
        moTextTptFigAddressStateName = new sba.lib.gui.bean.DBeanFieldText();
        jpTptFigInput23 = new javax.swing.JPanel();
        jlTptFigAddressCounty = new javax.swing.JLabel();
        moTextTptFigAddressCountyCode = new sba.lib.gui.bean.DBeanFieldText();
        moTextTptFigAddressCountyName = new sba.lib.gui.bean.DBeanFieldText();
        jpTptFigInput24 = new javax.swing.JPanel();
        jlTptFigAddressLocality = new javax.swing.JLabel();
        moTextTptFigAddressLocalityCode = new sba.lib.gui.bean.DBeanFieldText();
        moTextTptFigAddressLocalityName = new sba.lib.gui.bean.DBeanFieldText();
        jpTptFigInput25 = new javax.swing.JPanel();
        jlTptFigAddressZipCode = new javax.swing.JLabel();
        moTextTptFigAddressZipCode = new sba.lib.gui.bean.DBeanFieldText();
        jpTptFigInput26 = new javax.swing.JPanel();
        jlTptFigAddressDistrict = new javax.swing.JLabel();
        moTextTptFigAddressDistrictCode = new sba.lib.gui.bean.DBeanFieldText();
        moTextTptFigAddressDistrictName = new sba.lib.gui.bean.DBeanFieldText();
        jpTptFigInput27 = new javax.swing.JPanel();
        jlTptFigAddressStreet = new javax.swing.JLabel();
        moTextTptFigAddressStreet = new sba.lib.gui.bean.DBeanFieldText();
        moTextTptFigAddressNumberExt = new sba.lib.gui.bean.DBeanFieldText();
        moTextTptFigAddressNumberInt = new sba.lib.gui.bean.DBeanFieldText();
        jpTptFigInput28 = new javax.swing.JPanel();
        jlTptFigAddressReference = new javax.swing.JLabel();
        moTextTptFigAddressReference = new sba.lib.gui.bean.DBeanFieldText();
        jpTptFigInput3 = new javax.swing.JPanel();
        jpTptFigInput3N = new javax.swing.JPanel();
        jpTptFigInput3N1 = new javax.swing.JPanel();
        jlTptFigTptPartTransportPartType = new javax.swing.JLabel();
        moKeyTptFigTptPartTransportPartType = new sba.lib.gui.bean.DBeanFieldKey();
        jbTptFigTptPartEditType = new javax.swing.JButton();
        jpTptFigInput3N2 = new javax.swing.JPanel();
        jpTptFigInput3N3 = new javax.swing.JPanel();
        jpTptFigInput3N31 = new javax.swing.JPanel();
        jtfTptFigTptPartPk = new javax.swing.JTextField();
        jtfTptFigTptPartIsRequired = new javax.swing.JTextField();
        jpTptFigInput3N32 = new javax.swing.JPanel();
        moBoolTptFigTptPartUpdate = new sba.lib.gui.bean.DBeanFieldBoolean();
        jpTptFigInput3N4 = new javax.swing.JPanel();
        jpTptFigInput3N41 = new javax.swing.JPanel();
        jbTptFigTptPartAdd = new javax.swing.JButton();
        jbTptFigTptPartCreate = new javax.swing.JButton();
        jbTptFigTptPartModify = new javax.swing.JButton();
        jbTptFigTptPartRemove = new javax.swing.JButton();
        jpTptFigInput3N42 = new javax.swing.JPanel();
        jtfTptFigTptPartCrud = new javax.swing.JTextField();
        jbTptFigTptPartOk = new javax.swing.JButton();
        jbTptFigTptPartCancel = new javax.swing.JButton();
        jpTptFigTptPartGrid = new javax.swing.JPanel();
        jpTptFigCrud1 = new javax.swing.JPanel();
        jbTptFigAdd = new javax.swing.JButton();
        jbTptFigCreate = new javax.swing.JButton();
        jbTptFigCopy = new javax.swing.JButton();
        jbTptFigModify = new javax.swing.JButton();
        jbTptFigRemove = new javax.swing.JButton();
        jbTptFigMoveUp = new javax.swing.JButton();
        jbTptFigMoveDown = new javax.swing.JButton();
        jpTptFigCrud2 = new javax.swing.JPanel();
        jtfTptFigCrud = new javax.swing.JTextField();
        jbTptFigOk = new javax.swing.JButton();
        jbTptFigCancel = new javax.swing.JButton();
        jpNav = new javax.swing.JPanel();
        jpNavW = new javax.swing.JPanel();
        jtfOwnBranch = new javax.swing.JTextField();
        jpNavC = new javax.swing.JPanel();
        jbBolNavStart = new javax.swing.JButton();
        jbBolNavRestart = new javax.swing.JButton();
        jbBolNavPrev = new javax.swing.JButton();
        jbBolNavNext = new javax.swing.JButton();
        jpNavE = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jpHeader.setLayout(new java.awt.GridLayout(1, 4));

        jpHeaderBol.setBorder(javax.swing.BorderFactory.createTitledBorder("Carta porte:"));
        jpHeaderBol.setLayout(new java.awt.GridLayout(5, 1, 0, 3));

        jpHeaderBol1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBolTransportType.setText("Tipo:");
        jlBolTransportType.setPreferredSize(new java.awt.Dimension(50, 23));
        jpHeaderBol1.add(jlBolTransportType);

        jtfBolTransportType.setEditable(false);
        jtfBolTransportType.setText("TEXT");
        jtfBolTransportType.setFocusable(false);
        jtfBolTransportType.setPreferredSize(new java.awt.Dimension(112, 23));
        jpHeaderBol1.add(jtfBolTransportType);

        jtfBolVersion.setEditable(false);
        jtfBolVersion.setText("1.0");
        jtfBolVersion.setToolTipText("Versión");
        jtfBolVersion.setFocusable(false);
        jtfBolVersion.setPreferredSize(new java.awt.Dimension(30, 23));
        jpHeaderBol1.add(jtfBolVersion);

        jbBolUpdateVersion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_mov_lft.gif"))); // NOI18N
        jbBolUpdateVersion.setToolTipText("Actualizar versión");
        jbBolUpdateVersion.setPreferredSize(new java.awt.Dimension(23, 23));
        jpHeaderBol1.add(jbBolUpdateVersion);

        jpHeaderBol.add(jpHeaderBol1);

        jpHeaderBol2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBolNumber.setText("Folio:");
        jlBolNumber.setPreferredSize(new java.awt.Dimension(50, 23));
        jpHeaderBol2.add(jlBolNumber);

        moKeyBolSeries.setPreferredSize(new java.awt.Dimension(95, 23));
        jpHeaderBol2.add(moKeyBolSeries);

        jtfBolNumber.setEditable(false);
        jtfBolNumber.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfBolNumber.setText("999999");
        jtfBolNumber.setToolTipText("Folio");
        jtfBolNumber.setFocusable(false);
        jtfBolNumber.setPreferredSize(new java.awt.Dimension(75, 23));
        jpHeaderBol2.add(jtfBolNumber);

        jpHeaderBol.add(jpHeaderBol2);

        jpHeaderBol3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBolDate.setText("Fecha:*");
        jlBolDate.setPreferredSize(new java.awt.Dimension(50, 23));
        jpHeaderBol3.add(jlBolDate);
        jpHeaderBol3.add(moDateBolDate);

        jtfBolStatus.setEditable(false);
        jtfBolStatus.setText("TEXT");
        jtfBolStatus.setToolTipText("Estatus");
        jtfBolStatus.setFocusable(false);
        jtfBolStatus.setPreferredSize(new java.awt.Dimension(66, 23));
        jpHeaderBol3.add(jtfBolStatus);

        jpHeaderBol.add(jpHeaderBol3);

        jpHeaderBol4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfBolDfrUuid.setEditable(false);
        jtfBolDfrUuid.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        jtfBolDfrUuid.setText("F5343B75-9A23-4E25-979A-03CA560FEE62");
        jtfBolDfrUuid.setToolTipText("UUID del CFDI");
        jtfBolDfrUuid.setFocusable(false);
        jtfBolDfrUuid.setPreferredSize(new java.awt.Dimension(230, 23));
        jpHeaderBol4.add(jtfBolDfrUuid);

        jpHeaderBol.add(jpHeaderBol4);

        jpHeaderBol5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfBolBolUuid.setEditable(false);
        jtfBolBolUuid.setFont(new java.awt.Font("Courier New", 0, 10)); // NOI18N
        jtfBolBolUuid.setText("CCC43B75-9A23-4E25-979A-03CA560FEE62");
        jtfBolBolUuid.setToolTipText("UUID del complemento");
        jtfBolBolUuid.setFocusable(false);
        jtfBolBolUuid.setPreferredSize(new java.awt.Dimension(230, 23));
        jpHeaderBol5.add(jtfBolBolUuid);

        jpHeaderBol.add(jpHeaderBol5);

        jpHeader.add(jpHeaderBol);

        jpHeaderCfg.setBorder(javax.swing.BorderFactory.createTitledBorder("Configuración del transporte:"));
        jpHeaderCfg.setLayout(new java.awt.GridLayout(5, 1, 0, 3));

        moBoolBolIntlTransport.setText("Es transporte internacional");
        moBoolBolIntlTransport.setPreferredSize(new java.awt.Dimension(230, 23));
        jpHeaderCfg.add(moBoolBolIntlTransport);

        jpHeaderCfg2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfBolIntlTransport.setEditable(false);
        jtfBolIntlTransport.setText("TEXT");
        jtfBolIntlTransport.setToolTipText("Transporte internacional");
        jtfBolIntlTransport.setFocusable(false);
        jtfBolIntlTransport.setPreferredSize(new java.awt.Dimension(202, 23));
        jpHeaderCfg2.add(jtfBolIntlTransport);

        jbBolSetIntlTransport.setText("...");
        jbBolSetIntlTransport.setToolTipText("Configurar transporte internacional...");
        jbBolSetIntlTransport.setPreferredSize(new java.awt.Dimension(23, 23));
        jpHeaderCfg2.add(jbBolSetIntlTransport);

        jpHeaderCfg.add(jpHeaderCfg2);

        moBoolBolIsthmus.setText("Es registro Istmo");
        moBoolBolIsthmus.setPreferredSize(new java.awt.Dimension(230, 23));
        jpHeaderCfg.add(moBoolBolIsthmus);

        jpHeaderIntl4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBolIsthmusOrigin.setText("P. origen:*");
        jlBolIsthmusOrigin.setPreferredSize(new java.awt.Dimension(60, 23));
        jpHeaderIntl4.add(jlBolIsthmusOrigin);

        moKeyBolIsthmusOrigin.setPreferredSize(new java.awt.Dimension(165, 23));
        jpHeaderIntl4.add(moKeyBolIsthmusOrigin);

        jpHeaderCfg.add(jpHeaderIntl4);

        jpHeaderIntl45.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBolIsthmusDestiny.setText("P. destino:*");
        jlBolIsthmusDestiny.setPreferredSize(new java.awt.Dimension(60, 23));
        jpHeaderIntl45.add(jlBolIsthmusDestiny);

        moKeyBolIsthmusDestiny.setPreferredSize(new java.awt.Dimension(165, 23));
        jpHeaderIntl45.add(moKeyBolIsthmusDestiny);

        jpHeaderCfg.add(jpHeaderIntl45);

        jpHeader.add(jpHeaderCfg);

        jpHeaderLocMerch.setBorder(javax.swing.BorderFactory.createTitledBorder("Ubicaciones y mercancías:"));
        jpHeaderLocMerch.setLayout(new java.awt.GridLayout(5, 1, 0, 3));

        jpHeaderLocMerch1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBolDeparture.setText("Salida:");
        jlBolDeparture.setPreferredSize(new java.awt.Dimension(60, 23));
        jpHeaderLocMerch1.add(jlBolDeparture);

        jtfBolDeparturelDatetime.setEditable(false);
        jtfBolDeparturelDatetime.setText("01/01/2001 00:00:00");
        jtfBolDeparturelDatetime.setFocusable(false);
        jtfBolDeparturelDatetime.setPreferredSize(new java.awt.Dimension(110, 23));
        jpHeaderLocMerch1.add(jtfBolDeparturelDatetime);

        jtfBolDepartureLocation.setEditable(false);
        jtfBolDepartureLocation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfBolDepartureLocation.setText("000000");
        jtfBolDepartureLocation.setToolTipText("Ubicación salida");
        jtfBolDepartureLocation.setFocusable(false);
        jtfBolDepartureLocation.setPreferredSize(new java.awt.Dimension(50, 23));
        jpHeaderLocMerch1.add(jtfBolDepartureLocation);

        jpHeaderLocMerch.add(jpHeaderLocMerch1);

        jpHeaderLocMerch2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBolArrival.setText("Llegada:");
        jlBolArrival.setPreferredSize(new java.awt.Dimension(60, 23));
        jpHeaderLocMerch2.add(jlBolArrival);

        jtfBolArrivalDatetime.setEditable(false);
        jtfBolArrivalDatetime.setText("01/01/2001 00:00:00");
        jtfBolArrivalDatetime.setFocusable(false);
        jtfBolArrivalDatetime.setPreferredSize(new java.awt.Dimension(110, 23));
        jpHeaderLocMerch2.add(jtfBolArrivalDatetime);

        jtfBolArrivalLocation.setEditable(false);
        jtfBolArrivalLocation.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfBolArrivalLocation.setText("000000");
        jtfBolArrivalLocation.setToolTipText("Ubicación llegada");
        jtfBolArrivalLocation.setFocusable(false);
        jtfBolArrivalLocation.setPreferredSize(new java.awt.Dimension(50, 23));
        jpHeaderLocMerch2.add(jtfBolArrivalLocation);

        jpHeaderLocMerch.add(jpHeaderLocMerch2);

        jpHeaderLocMerch3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBolDistanceKm.setText("Distancia:");
        jlBolDistanceKm.setPreferredSize(new java.awt.Dimension(60, 23));
        jpHeaderLocMerch3.add(jlBolDistanceKm);

        jtfBolDistanceKm.setEditable(false);
        jtfBolDistanceKm.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfBolDistanceKm.setText("9,999.999");
        jtfBolDistanceKm.setToolTipText("Distancia recorrida total en km");
        jtfBolDistanceKm.setFocusable(false);
        jtfBolDistanceKm.setPreferredSize(new java.awt.Dimension(75, 23));
        jpHeaderLocMerch3.add(jtfBolDistanceKm);

        jlBolDistanceKmUnit.setText("km");
        jlBolDistanceKmUnit.setPreferredSize(new java.awt.Dimension(30, 23));
        jpHeaderLocMerch3.add(jlBolDistanceKmUnit);

        jlBolMerchandiseNumber.setText("Mercs.:");
        jlBolMerchandiseNumber.setPreferredSize(new java.awt.Dimension(50, 23));
        jpHeaderLocMerch3.add(jlBolMerchandiseNumber);

        jpHeaderLocMerch.add(jpHeaderLocMerch3);

        jpHeaderLocMerch4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBolMerchandiseWeightKg.setText("Peso total:");
        jlBolMerchandiseWeightKg.setPreferredSize(new java.awt.Dimension(60, 23));
        jpHeaderLocMerch4.add(jlBolMerchandiseWeightKg);

        jtfBolMerchandiseWeightKg.setEditable(false);
        jtfBolMerchandiseWeightKg.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfBolMerchandiseWeightKg.setText("9,999.999");
        jtfBolMerchandiseWeightKg.setToolTipText("Peso bruto total en kg");
        jtfBolMerchandiseWeightKg.setFocusable(false);
        jtfBolMerchandiseWeightKg.setPreferredSize(new java.awt.Dimension(75, 23));
        jpHeaderLocMerch4.add(jtfBolMerchandiseWeightKg);

        jlBolMerchandiseWeightKgUnit.setText("kg");
        jlBolMerchandiseWeightKgUnit.setPreferredSize(new java.awt.Dimension(30, 23));
        jpHeaderLocMerch4.add(jlBolMerchandiseWeightKgUnit);

        jtfBolMerchandiseNumber.setEditable(false);
        jtfBolMerchandiseNumber.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfBolMerchandiseNumber.setText("999");
        jtfBolMerchandiseNumber.setToolTipText("Número total de mercancías");
        jtfBolMerchandiseNumber.setFocusable(false);
        jtfBolMerchandiseNumber.setPreferredSize(new java.awt.Dimension(50, 23));
        jpHeaderLocMerch4.add(jtfBolMerchandiseNumber);

        jpHeaderLocMerch.add(jpHeaderLocMerch4);

        moBoolBolMerchandiseInverseLogistics.setText("Es logística inversa, recolección, devolución");
        jpHeaderLocMerch.add(moBoolBolMerchandiseInverseLogistics);

        jpHeader.add(jpHeaderLocMerch);

        jpHeaderTemp.setBorder(javax.swing.BorderFactory.createTitledBorder("Plantilla:"));
        jpHeaderTemp.setLayout(new java.awt.GridLayout(5, 1, 0, 3));

        moBoolBolTemplate.setText("Es plantilla");
        moBoolBolTemplate.setPreferredSize(new java.awt.Dimension(230, 23));
        jpHeaderTemp.add(moBoolBolTemplate);

        jpHeaderTemp2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBolTemplateName.setText("Nombre:*");
        jlBolTemplateName.setPreferredSize(new java.awt.Dimension(60, 23));
        jpHeaderTemp2.add(jlBolTemplateName);

        moTextBolTemplateName.setText("TEXT");
        moTextBolTemplateName.setPreferredSize(new java.awt.Dimension(165, 23));
        jpHeaderTemp2.add(moTextBolTemplateName);

        jpHeaderTemp.add(jpHeaderTemp2);

        jpHeaderTemp3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBolTemplateCode.setText("Código:*");
        jlBolTemplateCode.setPreferredSize(new java.awt.Dimension(60, 23));
        jpHeaderTemp3.add(jlBolTemplateCode);

        moTextBolTemplateCode.setText("TEXT");
        moTextBolTemplateCode.setPreferredSize(new java.awt.Dimension(75, 23));
        jpHeaderTemp3.add(moTextBolTemplateCode);

        jpHeaderTemp.add(jpHeaderTemp3);

        jpHeaderTemp4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBolBolTemplate.setText("Plantilla:");
        jlBolBolTemplate.setPreferredSize(new java.awt.Dimension(60, 23));
        jpHeaderTemp4.add(jlBolBolTemplate);

        jtfBolBolTemplate.setEditable(false);
        jtfBolBolTemplate.setText("TEXT");
        jtfBolBolTemplate.setFocusable(false);
        jtfBolBolTemplate.setPreferredSize(new java.awt.Dimension(165, 23));
        jpHeaderTemp4.add(jtfBolBolTemplate);

        jpHeaderTemp.add(jpHeaderTemp4);

        jpHeaderTemp5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpHeaderTemp.add(jpHeaderTemp5);

        jpHeader.add(jpHeaderTemp);

        jPanel1.add(jpHeader, java.awt.BorderLayout.NORTH);

        jpWizardLoc.setLayout(new java.awt.BorderLayout());

        jpLoc.setLayout(new java.awt.BorderLayout());

        jpLocInput.setLayout(new java.awt.GridLayout(1, 3));

        jpLocInput1.setBorder(javax.swing.BorderFactory.createTitledBorder("Ubicación:"));
        jpLocInput1.setLayout(new java.awt.GridLayout(8, 1, 0, 3));

        jpLocInput11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLocLocationType.setText("Tipo:*");
        jlLocLocationType.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput11.add(jlLocLocationType);

        moKeyLocLocationType.setPreferredSize(new java.awt.Dimension(205, 23));
        jpLocInput11.add(moKeyLocLocationType);

        jbLocEditType.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_edit.gif"))); // NOI18N
        jbLocEditType.setToolTipText("Modificar");
        jbLocEditType.setPreferredSize(new java.awt.Dimension(23, 23));
        jpLocInput11.add(jbLocEditType);

        jpLocInput1.add(jpLocInput11);

        jpLocInput12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLocName.setText("Nombre:*");
        jlLocName.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput12.add(jlLocName);

        moTextLocName.setText("TEXT");
        moTextLocName.setPreferredSize(new java.awt.Dimension(230, 23));
        jpLocInput12.add(moTextLocName);

        jpLocInput1.add(jpLocInput12);

        jpLocInput13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLocCode.setText("Código:*");
        jlLocCode.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput13.add(jlLocCode);

        moTextLocCode.setText("000000");
        moTextLocCode.setPreferredSize(new java.awt.Dimension(60, 23));
        jpLocInput13.add(moTextLocCode);

        jbLocGetNextCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_mov_lft.gif"))); // NOI18N
        jbLocGetNextCode.setToolTipText("Obtener siguiente código");
        jbLocGetNextCode.setPreferredSize(new java.awt.Dimension(23, 23));
        jpLocInput13.add(jbLocGetNextCode);

        jpLocInput1.add(jpLocInput13);

        jpLocInput14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLocLocationId.setText("ID ubicación:");
        jlLocLocationId.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput14.add(jlLocLocationId);

        jtfLocLocationId.setEditable(false);
        jtfLocLocationId.setText("TEXT");
        jtfLocLocationId.setFocusable(false);
        jtfLocLocationId.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput14.add(jtfLocLocationId);

        jpLocInput1.add(jpLocInput14);

        jpLocInput15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLocArrivalDepartureDatetime.setForeground(new java.awt.Color(0, 102, 102));
        jlLocArrivalDepartureDatetime.setText("Fecha hr.:*");
        jlLocArrivalDepartureDatetime.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput15.add(jlLocArrivalDepartureDatetime);
        jpLocInput15.add(moDatetimeLocArrivalDepartureDatetime);

        jlLocArrivalDepartureDatetimeHelp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/icon_help.png"))); // NOI18N
        jlLocArrivalDepartureDatetimeHelp.setToolTipText("Formato: dd/mm/aaaa hh:mm:ss (24 hr)");
        jlLocArrivalDepartureDatetimeHelp.setPreferredSize(new java.awt.Dimension(15, 23));
        jpLocInput15.add(jlLocArrivalDepartureDatetimeHelp);

        jpLocInput1.add(jpLocInput15);

        jpLocInput16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLocSourceLocation.setText("Origen:*");
        jlLocSourceLocation.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput16.add(jlLocSourceLocation);

        moKeyLocSourceLocation.setPreferredSize(new java.awt.Dimension(230, 23));
        jpLocInput16.add(moKeyLocSourceLocation);

        jpLocInput1.add(jpLocInput16);

        jpLocInput17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLocDistanceKm.setText("Distancia:*");
        jlLocDistanceKm.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput17.add(jlLocDistanceKm);

        moDecLocDistanceKm.setText("0.00");
        moDecLocDistanceKm.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput17.add(moDecLocDistanceKm);

        jlLocDistanceKmUnit.setText("km");
        jlLocDistanceKmUnit.setPreferredSize(new java.awt.Dimension(30, 23));
        jpLocInput17.add(jlLocDistanceKmUnit);

        jpLocInput1.add(jpLocInput17);

        jpLocInput18.setLayout(new java.awt.BorderLayout());

        jpLocInput181.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfLocPk.setEditable(false);
        jtfLocPk.setText("TEXT");
        jtfLocPk.setToolTipText("PK");
        jtfLocPk.setFocusable(false);
        jtfLocPk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput181.add(jtfLocPk);

        jpLocInput18.add(jpLocInput181, java.awt.BorderLayout.CENTER);

        jpLocInput182.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolLocUpdate.setText("¡Actualizar catálogo!");
        moBoolLocUpdate.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        moBoolLocUpdate.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        moBoolLocUpdate.setPreferredSize(new java.awt.Dimension(150, 23));
        jpLocInput182.add(moBoolLocUpdate);

        jpLocInput18.add(jpLocInput182, java.awt.BorderLayout.EAST);

        jpLocInput1.add(jpLocInput18);

        jpLocInput.add(jpLocInput1);

        jpLocInput2.setBorder(javax.swing.BorderFactory.createTitledBorder("Domicilio:"));
        jpLocInput2.setLayout(new java.awt.GridLayout(8, 1, 0, 3));

        jpLocInput21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLocAddressCountry.setText("País:*");
        jlLocAddressCountry.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput21.add(jlLocAddressCountry);

        moKeyLocAddressCountry.setPreferredSize(new java.awt.Dimension(230, 23));
        jpLocInput21.add(moKeyLocAddressCountry);

        jpLocInput2.add(jpLocInput21);

        jpLocInput22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLocAddressState.setText("Estado:*");
        jlLocAddressState.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput22.add(jlLocAddressState);

        moTextLocAddressStateCode.setForeground(java.awt.Color.blue);
        moTextLocAddressStateCode.setText("XXX");
        moTextLocAddressStateCode.setToolTipText("Clave (F5 para buscar)");
        moTextLocAddressStateCode.setPreferredSize(new java.awt.Dimension(35, 23));
        jpLocInput22.add(moTextLocAddressStateCode);

        moTextLocAddressStateName.setText("TEXT");
        moTextLocAddressStateName.setToolTipText("Descripción");
        moTextLocAddressStateName.setPreferredSize(new java.awt.Dimension(190, 23));
        jpLocInput22.add(moTextLocAddressStateName);

        jpLocInput2.add(jpLocInput22);

        jpLocInput23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLocAddressCounty.setText("Municipio:");
        jlLocAddressCounty.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput23.add(jlLocAddressCounty);

        moTextLocAddressCountyCode.setForeground(java.awt.Color.blue);
        moTextLocAddressCountyCode.setText("000");
        moTextLocAddressCountyCode.setToolTipText("Clave (F5 para buscar)");
        moTextLocAddressCountyCode.setPreferredSize(new java.awt.Dimension(35, 23));
        jpLocInput23.add(moTextLocAddressCountyCode);

        moTextLocAddressCountyName.setText("TEXT");
        moTextLocAddressCountyName.setToolTipText("Descripción");
        moTextLocAddressCountyName.setPreferredSize(new java.awt.Dimension(190, 23));
        jpLocInput23.add(moTextLocAddressCountyName);

        jpLocInput2.add(jpLocInput23);

        jpLocInput24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLocAddressLocality.setText("Localidad:");
        jlLocAddressLocality.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput24.add(jlLocAddressLocality);

        moTextLocAddressLocalityCode.setForeground(java.awt.Color.blue);
        moTextLocAddressLocalityCode.setText("00");
        moTextLocAddressLocalityCode.setToolTipText("Clave (F5 para buscar)");
        moTextLocAddressLocalityCode.setPreferredSize(new java.awt.Dimension(35, 23));
        jpLocInput24.add(moTextLocAddressLocalityCode);

        moTextLocAddressLocalityName.setText("TEXT");
        moTextLocAddressLocalityName.setToolTipText("Descripción");
        moTextLocAddressLocalityName.setPreferredSize(new java.awt.Dimension(190, 23));
        jpLocInput24.add(moTextLocAddressLocalityName);

        jpLocInput2.add(jpLocInput24);

        jpLocInput25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLocAddressZipCode.setText("CP:");
        jlLocAddressZipCode.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput25.add(jlLocAddressZipCode);

        moTextLocAddressZipCode.setText("000000000000");
        jpLocInput25.add(moTextLocAddressZipCode);

        jpLocInput2.add(jpLocInput25);

        jpLocInput26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLocAddressDistrict.setText("Colonia:");
        jlLocAddressDistrict.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput26.add(jlLocAddressDistrict);

        moTextLocAddressDistrictCode.setForeground(java.awt.Color.blue);
        moTextLocAddressDistrictCode.setText("0000");
        moTextLocAddressDistrictCode.setToolTipText("Clave (F5 para buscar)");
        moTextLocAddressDistrictCode.setPreferredSize(new java.awt.Dimension(35, 23));
        jpLocInput26.add(moTextLocAddressDistrictCode);

        moTextLocAddressDistrictName.setText("TEXT");
        moTextLocAddressDistrictName.setToolTipText("Descripción");
        moTextLocAddressDistrictName.setPreferredSize(new java.awt.Dimension(190, 23));
        jpLocInput26.add(moTextLocAddressDistrictName);

        jpLocInput2.add(jpLocInput26);

        jpLocInput27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLocAddressStreet.setText("Calle:");
        jlLocAddressStreet.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput27.add(jlLocAddressStreet);

        moTextLocAddressStreet.setText("TEXT");
        moTextLocAddressStreet.setToolTipText("Calle");
        moTextLocAddressStreet.setPreferredSize(new java.awt.Dimension(135, 23));
        jpLocInput27.add(moTextLocAddressStreet);

        moTextLocAddressNumberExt.setText("TEXT");
        moTextLocAddressNumberExt.setToolTipText("No. exterior");
        moTextLocAddressNumberExt.setPreferredSize(new java.awt.Dimension(50, 23));
        jpLocInput27.add(moTextLocAddressNumberExt);

        moTextLocAddressNumberInt.setText("TEXT");
        moTextLocAddressNumberInt.setToolTipText("No. interior");
        moTextLocAddressNumberInt.setPreferredSize(new java.awt.Dimension(35, 23));
        jpLocInput27.add(moTextLocAddressNumberInt);

        jpLocInput2.add(jpLocInput27);

        jpLocInput28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLocAddressReference.setText("Referencia:");
        jlLocAddressReference.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput28.add(jlLocAddressReference);

        moTextLocAddressReference.setText("TEXT");
        moTextLocAddressReference.setPreferredSize(new java.awt.Dimension(230, 23));
        jpLocInput28.add(moTextLocAddressReference);

        jpLocInput2.add(jpLocInput28);

        jpLocInput.add(jpLocInput2);

        jpLocInput3.setBorder(javax.swing.BorderFactory.createTitledBorder("Adicionales:"));
        jpLocInput3.setLayout(new java.awt.GridLayout(8, 1, 0, 3));

        jpLocInput31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlLocNotes.setForeground(new java.awt.Color(0, 102, 102));
        jlLocNotes.setText("Notas:");
        jlLocNotes.setPreferredSize(new java.awt.Dimension(75, 23));
        jpLocInput31.add(jlLocNotes);

        moTextLocNotes.setText("TEXT");
        moTextLocNotes.setPreferredSize(new java.awt.Dimension(230, 23));
        jpLocInput31.add(moTextLocNotes);

        jpLocInput3.add(jpLocInput31);

        jpLocInput32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpLocInput3.add(jpLocInput32);

        jpLocInput33.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpLocInput3.add(jpLocInput33);

        jpLocInput34.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpLocInput3.add(jpLocInput34);

        jpLocInput35.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpLocInput3.add(jpLocInput35);

        jpLocInput36.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpLocInput3.add(jpLocInput36);

        jpLocInput37.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpLocInput3.add(jpLocInput37);

        jpLocInput38.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpLocInput3.add(jpLocInput38);

        jpLocInput.add(jpLocInput3);

        jpLoc.add(jpLocInput, java.awt.BorderLayout.PAGE_START);

        jpLocCrud1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 3));

        jbLocAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_add.gif"))); // NOI18N
        jbLocAdd.setToolTipText("Agregar");
        jbLocAdd.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbLocAdd.setPreferredSize(new java.awt.Dimension(40, 23));
        jpLocCrud1.add(jbLocAdd);

        jbLocCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_new.gif"))); // NOI18N
        jbLocCreate.setToolTipText("Crear");
        jbLocCreate.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbLocCreate.setPreferredSize(new java.awt.Dimension(40, 23));
        jpLocCrud1.add(jbLocCreate);

        jbLocCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_copy.gif"))); // NOI18N
        jbLocCopy.setToolTipText("Copiar");
        jbLocCopy.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbLocCopy.setPreferredSize(new java.awt.Dimension(40, 23));
        jpLocCrud1.add(jbLocCopy);

        jbLocModify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_edit.gif"))); // NOI18N
        jbLocModify.setToolTipText("Modificar");
        jbLocModify.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbLocModify.setPreferredSize(new java.awt.Dimension(40, 23));
        jpLocCrud1.add(jbLocModify);

        jbLocRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_delete.gif"))); // NOI18N
        jbLocRemove.setToolTipText("Quitar");
        jbLocRemove.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbLocRemove.setPreferredSize(new java.awt.Dimension(40, 23));
        jpLocCrud1.add(jbLocRemove);

        jbLocMoveUp.setText("▲");
        jbLocMoveUp.setToolTipText("Mover arriba");
        jbLocMoveUp.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbLocMoveUp.setPreferredSize(new java.awt.Dimension(40, 23));
        jpLocCrud1.add(jbLocMoveUp);

        jbLocMoveDown.setText("▼");
        jbLocMoveDown.setToolTipText("Mover abajo");
        jbLocMoveDown.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbLocMoveDown.setPreferredSize(new java.awt.Dimension(40, 23));
        jpLocCrud1.add(jbLocMoveDown);

        jpLoc.add(jpLocCrud1, java.awt.BorderLayout.CENTER);

        jpLocCrud2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 3));

        jtfLocCrud.setEditable(false);
        jtfLocCrud.setText("TEXT");
        jtfLocCrud.setToolTipText("Acción");
        jtfLocCrud.setFocusable(false);
        jtfLocCrud.setPreferredSize(new java.awt.Dimension(100, 23));
        jpLocCrud2.add(jtfLocCrud);

        jbLocOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_ok.gif"))); // NOI18N
        jbLocOk.setToolTipText("Aceptar");
        jbLocOk.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbLocOk.setPreferredSize(new java.awt.Dimension(40, 23));
        jpLocCrud2.add(jbLocOk);

        jbLocCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_cancel.gif"))); // NOI18N
        jbLocCancel.setToolTipText("Cancelar");
        jbLocCancel.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbLocCancel.setPreferredSize(new java.awt.Dimension(40, 23));
        jpLocCrud2.add(jbLocCancel);

        jpLoc.add(jpLocCrud2, java.awt.BorderLayout.EAST);

        jpWizardLoc.add(jpLoc, java.awt.BorderLayout.NORTH);

        jtpWizard.addTab("1. Ubicaciones", jpWizardLoc);

        jpWizardMerch.setLayout(new java.awt.BorderLayout());

        jpMerch.setLayout(new java.awt.BorderLayout());

        jpMerchInput.setLayout(new java.awt.GridLayout(1, 3));

        jpMerchInput1.setBorder(javax.swing.BorderFactory.createTitledBorder("Mercancía:"));
        jpMerchInput1.setLayout(new java.awt.GridLayout(8, 1, 0, 3));

        jpMerchInput11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMerchItem.setText("Cve. bienes:*");
        jlMerchItem.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMerchInput11.add(jlMerchItem);

        moKeyMerchItem.setPreferredSize(new java.awt.Dimension(205, 23));
        jpMerchInput11.add(moKeyMerchItem);

        jbMerchPickItem.setText("...");
        jbMerchPickItem.setToolTipText("Buscar ítem");
        jbMerchPickItem.setPreferredSize(new java.awt.Dimension(23, 23));
        jpMerchInput11.add(jbMerchPickItem);

        jpMerchInput1.add(jpMerchInput11);

        jpMerchInput12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMerchItemDescription.setText("Descripción:*");
        jlMerchItemDescription.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMerchInput12.add(jlMerchItemDescription);

        moTextMerchItemDescription.setText("TEXT");
        moTextMerchItemDescription.setPreferredSize(new java.awt.Dimension(230, 23));
        jpMerchInput12.add(moTextMerchItemDescription);

        jpMerchInput1.add(jpMerchInput12);

        jpMerchInput14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMerchUnit.setText("Cve. unidad:*");
        jlMerchUnit.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMerchInput14.add(jlMerchUnit);

        moKeyMerchUnit.setPreferredSize(new java.awt.Dimension(205, 23));
        jpMerchInput14.add(moKeyMerchUnit);

        jpMerchInput1.add(jpMerchInput14);

        jpMerchInput15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMerchUnitDescription.setText("Unidad:*");
        jlMerchUnitDescription.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMerchInput15.add(jlMerchUnitDescription);

        moTextMerchUnitDescription.setText("TEXT");
        moTextMerchUnitDescription.setPreferredSize(new java.awt.Dimension(230, 23));
        jpMerchInput15.add(moTextMerchUnitDescription);

        jpMerchInput1.add(jpMerchInput15);

        jpMerchInput13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMerchQuantity.setText("Cantidad:*");
        jlMerchQuantity.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMerchInput13.add(jlMerchQuantity);

        moDecMerchQuantity.setText("0.000000");
        moDecMerchQuantity.setPreferredSize(new java.awt.Dimension(90, 23));
        jpMerchInput13.add(moDecMerchQuantity);

        jtfMerchQuantityMoved.setEditable(false);
        jtfMerchQuantityMoved.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfMerchQuantityMoved.setText("0.000000");
        jtfMerchQuantityMoved.setToolTipText("Cantidad transportada");
        jtfMerchQuantityMoved.setFocusable(false);
        jtfMerchQuantityMoved.setPreferredSize(new java.awt.Dimension(90, 23));
        jtfMerchQuantityMoved.setRequestFocusEnabled(false);
        jpMerchInput13.add(jtfMerchQuantityMoved);

        jpMerchInput1.add(jpMerchInput13);

        jpMerchInput16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMerchWeightKg.setText("Peso:*");
        jlMerchWeightKg.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMerchInput16.add(jlMerchWeightKg);

        moDecMerchWeightKg.setText("0.000");
        moDecMerchWeightKg.setPreferredSize(new java.awt.Dimension(90, 23));
        jpMerchInput16.add(moDecMerchWeightKg);

        jlMerchWeightKgUnit.setText("kg");
        jlMerchWeightKgUnit.setPreferredSize(new java.awt.Dimension(15, 23));
        jpMerchInput16.add(jlMerchWeightKgUnit);

        jbMerchSetWeightKg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_mov_lft.gif"))); // NOI18N
        jbMerchSetWeightKg.setToolTipText("Asignar peso");
        jbMerchSetWeightKg.setPreferredSize(new java.awt.Dimension(23, 23));
        jpMerchInput16.add(jbMerchSetWeightKg);

        jlMerchRatioKgHint.setForeground(java.awt.Color.gray);
        jlMerchRatioKgHint.setText("1.000 x unidad");
        jlMerchRatioKgHint.setPreferredSize(new java.awt.Dimension(90, 23));
        jpMerchInput16.add(jlMerchRatioKgHint);

        jpMerchInput1.add(jpMerchInput16);

        jpMerchInput17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMerchDimensions.setText("Dimensiones:");
        jlMerchDimensions.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMerchInput17.add(jlMerchDimensions);

        moIntMerchDimensionsLength.setToolTipText("Longitud");
        moIntMerchDimensionsLength.setPreferredSize(new java.awt.Dimension(30, 23));
        jpMerchInput17.add(moIntMerchDimensionsLength);

        jlMerchDimensions1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlMerchDimensions1.setText("/");
        jlMerchDimensions1.setPreferredSize(new java.awt.Dimension(10, 23));
        jpMerchInput17.add(jlMerchDimensions1);

        moIntMerchDimensionsHeight.setToolTipText("Altura");
        moIntMerchDimensionsHeight.setPreferredSize(new java.awt.Dimension(30, 23));
        jpMerchInput17.add(moIntMerchDimensionsHeight);

        jlMerchDimensions2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlMerchDimensions2.setText("/");
        jlMerchDimensions2.setPreferredSize(new java.awt.Dimension(10, 23));
        jpMerchInput17.add(jlMerchDimensions2);

        moIntMerchDimensionsWidth.setToolTipText("Anchura");
        moIntMerchDimensionsWidth.setPreferredSize(new java.awt.Dimension(30, 23));
        jpMerchInput17.add(moIntMerchDimensionsWidth);

        bgMerchDimensionUnits.add(moRadMerchDimensionsCm);
        moRadMerchDimensionsCm.setText("cm");
        moRadMerchDimensionsCm.setPreferredSize(new java.awt.Dimension(45, 23));
        jpMerchInput17.add(moRadMerchDimensionsCm);

        bgMerchDimensionUnits.add(moRadMerchDimensionsPlg);
        moRadMerchDimensionsPlg.setText("plg");
        moRadMerchDimensionsPlg.setPreferredSize(new java.awt.Dimension(45, 23));
        jpMerchInput17.add(moRadMerchDimensionsPlg);

        jpMerchInput1.add(jpMerchInput17);

        jpMerchInput18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMerchDimensionsHint.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMerchInput18.add(jlMerchDimensionsHint);

        jtfMerchDimensionsHint.setEditable(false);
        jtfMerchDimensionsHint.setText("999/999/999cm");
        jtfMerchDimensionsHint.setToolTipText("Dimensiones");
        jtfMerchDimensionsHint.setFocusable(false);
        jtfMerchDimensionsHint.setPreferredSize(new java.awt.Dimension(100, 23));
        jpMerchInput18.add(jtfMerchDimensionsHint);

        jlMerchCfdItemKeySeparator.setPreferredSize(new java.awt.Dimension(5, 23));
        jpMerchInput18.add(jlMerchCfdItemKeySeparator);

        jlMerchCfdItemKey.setForeground(java.awt.Color.gray);
        jlMerchCfdItemKey.setText("ProdServ: 00000000");
        jlMerchCfdItemKey.setToolTipText("Clave de producto o servicio");
        jlMerchCfdItemKey.setPreferredSize(new java.awt.Dimension(110, 23));
        jpMerchInput18.add(jlMerchCfdItemKey);

        jpMerchInput1.add(jpMerchInput18);

        jpMerchInput.add(jpMerchInput1);

        jpMerchInput2.setBorder(javax.swing.BorderFactory.createTitledBorder("Adicionales:"));
        jpMerchInput2.setLayout(new java.awt.GridLayout(8, 1, 0, 3));

        jpMerchInput21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMerchCurrency.setText("Moneda:");
        jlMerchCurrency.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMerchInput21.add(jlMerchCurrency);

        moKeyMerchCurrency.setPreferredSize(new java.awt.Dimension(205, 23));
        jpMerchInput21.add(moKeyMerchCurrency);

        jpMerchInput2.add(jpMerchInput21);

        jpMerchInput22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMerchValue.setText("Valor:");
        jlMerchValue.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMerchInput22.add(jlMerchValue);
        jpMerchInput22.add(moCurMerchValue);

        jpMerchInput2.add(jpMerchInput22);

        jpMerchInput23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolMerchHazardousMaterial.setText("Material peligroso:");
        moBoolMerchHazardousMaterial.setPreferredSize(new java.awt.Dimension(125, 23));
        jpMerchInput23.add(moBoolMerchHazardousMaterial);

        bgMerchHazardousMaterial.add(moRadMerchHazardousMaterialYes);
        moRadMerchHazardousMaterialYes.setText("Sí");
        moRadMerchHazardousMaterialYes.setPreferredSize(new java.awt.Dimension(50, 23));
        jpMerchInput23.add(moRadMerchHazardousMaterialYes);

        bgMerchHazardousMaterial.add(moRadMerchHazardousMaterialNo);
        moRadMerchHazardousMaterialNo.setText("No");
        moRadMerchHazardousMaterialNo.setPreferredSize(new java.awt.Dimension(50, 23));
        jpMerchInput23.add(moRadMerchHazardousMaterialNo);

        jpMerchInput2.add(jpMerchInput23);

        jpMerchInput24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMerchHazardousMaterial.setText("Material:*");
        jlMerchHazardousMaterial.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMerchInput24.add(jlMerchHazardousMaterial);

        moTextMerchHazardousMaterialCode.setForeground(java.awt.Color.blue);
        moTextMerchHazardousMaterialCode.setText("9999");
        moTextMerchHazardousMaterialCode.setToolTipText("Clave (F5 para buscar)");
        moTextMerchHazardousMaterialCode.setPreferredSize(new java.awt.Dimension(35, 23));
        jpMerchInput24.add(moTextMerchHazardousMaterialCode);

        moTextMerchHazardousMaterialName.setText("TEXT");
        moTextMerchHazardousMaterialName.setToolTipText("Descripción");
        moTextMerchHazardousMaterialName.setPreferredSize(new java.awt.Dimension(190, 23));
        jpMerchInput24.add(moTextMerchHazardousMaterialName);

        jpMerchInput2.add(jpMerchInput24);

        jpMerchInput25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMerchPackaging.setText("Embalaje:*");
        jlMerchPackaging.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMerchInput25.add(jlMerchPackaging);

        moTextMerchPackagingCode.setForeground(java.awt.Color.blue);
        moTextMerchPackagingCode.setText("XXXX");
        moTextMerchPackagingCode.setToolTipText("Clave (F5 para buscar)");
        moTextMerchPackagingCode.setPreferredSize(new java.awt.Dimension(35, 23));
        jpMerchInput25.add(moTextMerchPackagingCode);

        moTextMerchPackagingName.setText("TEXT");
        moTextMerchPackagingName.setToolTipText("Descripción");
        moTextMerchPackagingName.setPreferredSize(new java.awt.Dimension(190, 23));
        jpMerchInput25.add(moTextMerchPackagingName);

        jpMerchInput2.add(jpMerchInput25);

        jpMerchInput26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMerchTariff.setText("Fracción a.:*");
        jlMerchTariff.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMerchInput26.add(jlMerchTariff);

        moTextMerchTariff.setText("9999999999");
        jpMerchInput26.add(moTextMerchTariff);

        jpMerchInput2.add(jpMerchInput26);

        jpMerchInput27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMerchImportRequest.setText("Pedimento i.:*");
        jlMerchImportRequest.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMerchInput27.add(jlMerchImportRequest);

        moIntMerchImportRequest1.setToolTipText("2 últimos dígitos año validación");
        moIntMerchImportRequest1.setPreferredSize(new java.awt.Dimension(25, 23));
        jpMerchInput27.add(moIntMerchImportRequest1);

        moIntMerchImportRequest2.setToolTipText("2 dígitos aduana despacho");
        moIntMerchImportRequest2.setPreferredSize(new java.awt.Dimension(25, 23));
        jpMerchInput27.add(moIntMerchImportRequest2);

        moIntMerchImportRequest3.setToolTipText("4 dígitos número patente");
        moIntMerchImportRequest3.setPreferredSize(new java.awt.Dimension(40, 23));
        jpMerchInput27.add(moIntMerchImportRequest3);

        moIntMerchImportRequest4.setToolTipText("1 dígito + 6 dígitos numeración progresiva aduana");
        moIntMerchImportRequest4.setPreferredSize(new java.awt.Dimension(60, 23));
        jpMerchInput27.add(moIntMerchImportRequest4);

        jpMerchInput2.add(jpMerchInput27);

        jpMerchInput28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMerchImportRequestHint.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMerchInput28.add(jlMerchImportRequestHint);

        jtfMerchImportRequestHint.setEditable(false);
        jtfMerchImportRequestHint.setText("99  99  9999  9999999");
        jtfMerchImportRequestHint.setToolTipText("Pedimento");
        jtfMerchImportRequestHint.setFocusable(false);
        jtfMerchImportRequestHint.setPreferredSize(new java.awt.Dimension(165, 23));
        jpMerchInput28.add(jtfMerchImportRequestHint);

        jpMerchInput2.add(jpMerchInput28);

        jpMerchInput.add(jpMerchInput2);

        jpMerchInput3.setBorder(javax.swing.BorderFactory.createTitledBorder("Cantidad transportada:"));
        jpMerchInput3.setLayout(new java.awt.BorderLayout());

        jpMerchInput3N.setLayout(new java.awt.GridLayout(4, 1, 0, 3));

        jpMerchInput3N1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMerchMoveSource.setText("Origen:*");
        jlMerchMoveSource.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMerchInput3N1.add(jlMerchMoveSource);

        moKeyMerchMoveSource.setPreferredSize(new java.awt.Dimension(230, 23));
        jpMerchInput3N1.add(moKeyMerchMoveSource);

        jpMerchInput3N.add(jpMerchInput3N1);

        jpMerchInput3N2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMerchMoveDestiny.setText("Destino:*");
        jlMerchMoveDestiny.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMerchInput3N2.add(jlMerchMoveDestiny);

        moKeyMerchMoveDestiny.setPreferredSize(new java.awt.Dimension(230, 23));
        jpMerchInput3N2.add(moKeyMerchMoveDestiny);

        jpMerchInput3N.add(jpMerchInput3N2);

        jpMerchInput3N3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlMerchMoveQuantity.setText("Cantidad:*");
        jlMerchMoveQuantity.setPreferredSize(new java.awt.Dimension(75, 23));
        jpMerchInput3N3.add(jlMerchMoveQuantity);

        moDecMerchMoveQuantity.setText("0.000000");
        jpMerchInput3N3.add(moDecMerchMoveQuantity);

        jbMerchMoveSetQuantity.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_mov_lft.gif"))); // NOI18N
        jbMerchMoveSetQuantity.setToolTipText("Asignar cantidad");
        jbMerchMoveSetQuantity.setPreferredSize(new java.awt.Dimension(23, 23));
        jpMerchInput3N3.add(jbMerchMoveSetQuantity);

        jpMerchInput3N.add(jpMerchInput3N3);

        jpMerchInput3N4.setLayout(new java.awt.BorderLayout());

        jpMerchInput3N41.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbMerchMoveAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_add.gif"))); // NOI18N
        jbMerchMoveAdd.setToolTipText("Agregar");
        jbMerchMoveAdd.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbMerchMoveAdd.setPreferredSize(new java.awt.Dimension(40, 23));
        jpMerchInput3N41.add(jbMerchMoveAdd);

        jbMerchMoveCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_new.gif"))); // NOI18N
        jbMerchMoveCreate.setToolTipText("Crear");
        jbMerchMoveCreate.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbMerchMoveCreate.setPreferredSize(new java.awt.Dimension(40, 23));
        jpMerchInput3N41.add(jbMerchMoveCreate);

        jbMerchMoveModify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_edit.gif"))); // NOI18N
        jbMerchMoveModify.setToolTipText("Modificar");
        jbMerchMoveModify.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbMerchMoveModify.setPreferredSize(new java.awt.Dimension(40, 23));
        jpMerchInput3N41.add(jbMerchMoveModify);

        jbMerchMoveRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_delete.gif"))); // NOI18N
        jbMerchMoveRemove.setToolTipText("Quitar");
        jbMerchMoveRemove.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbMerchMoveRemove.setPreferredSize(new java.awt.Dimension(40, 23));
        jpMerchInput3N41.add(jbMerchMoveRemove);

        jpMerchInput3N4.add(jpMerchInput3N41, java.awt.BorderLayout.CENTER);

        jpMerchInput3N42.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jtfMerchMoveCrud.setEditable(false);
        jtfMerchMoveCrud.setText("TEXT");
        jtfMerchMoveCrud.setToolTipText("Acción");
        jtfMerchMoveCrud.setFocusable(false);
        jtfMerchMoveCrud.setPreferredSize(new java.awt.Dimension(40, 23));
        jpMerchInput3N42.add(jtfMerchMoveCrud);

        jbMerchMoveOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_ok.gif"))); // NOI18N
        jbMerchMoveOk.setToolTipText("Aceptar");
        jbMerchMoveOk.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbMerchMoveOk.setPreferredSize(new java.awt.Dimension(40, 23));
        jpMerchInput3N42.add(jbMerchMoveOk);

        jbMerchMoveCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_cancel.gif"))); // NOI18N
        jbMerchMoveCancel.setToolTipText("Cancelar");
        jbMerchMoveCancel.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbMerchMoveCancel.setPreferredSize(new java.awt.Dimension(40, 23));
        jpMerchInput3N42.add(jbMerchMoveCancel);

        jpMerchInput3N4.add(jpMerchInput3N42, java.awt.BorderLayout.EAST);

        jpMerchInput3N.add(jpMerchInput3N4);

        jpMerchInput3.add(jpMerchInput3N, java.awt.BorderLayout.NORTH);

        jpMerchMoveGrid.setPreferredSize(new java.awt.Dimension(100, 105));
        jpMerchMoveGrid.setLayout(new java.awt.BorderLayout());
        jpMerchInput3.add(jpMerchMoveGrid, java.awt.BorderLayout.SOUTH);

        jpMerchInput.add(jpMerchInput3);

        jpMerch.add(jpMerchInput, java.awt.BorderLayout.PAGE_START);

        jpMerchCrud1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 3));

        jbMerchAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_add.gif"))); // NOI18N
        jbMerchAdd.setToolTipText("Agregar");
        jbMerchAdd.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbMerchAdd.setPreferredSize(new java.awt.Dimension(40, 23));
        jpMerchCrud1.add(jbMerchAdd);

        jbMerchCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_new.gif"))); // NOI18N
        jbMerchCreate.setToolTipText("Crear");
        jbMerchCreate.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbMerchCreate.setPreferredSize(new java.awt.Dimension(40, 23));
        jpMerchCrud1.add(jbMerchCreate);

        jbMerchCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_copy.gif"))); // NOI18N
        jbMerchCopy.setToolTipText("Copiar");
        jbMerchCopy.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbMerchCopy.setPreferredSize(new java.awt.Dimension(40, 23));
        jpMerchCrud1.add(jbMerchCopy);

        jbMerchModify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_edit.gif"))); // NOI18N
        jbMerchModify.setToolTipText("Modificar");
        jbMerchModify.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbMerchModify.setPreferredSize(new java.awt.Dimension(40, 23));
        jpMerchCrud1.add(jbMerchModify);

        jbMerchRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_delete.gif"))); // NOI18N
        jbMerchRemove.setToolTipText("Quitar");
        jbMerchRemove.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbMerchRemove.setPreferredSize(new java.awt.Dimension(40, 23));
        jpMerchCrud1.add(jbMerchRemove);

        jbMerchMoveUp.setText("▲");
        jbMerchMoveUp.setToolTipText("Mover arriba");
        jbMerchMoveUp.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbMerchMoveUp.setPreferredSize(new java.awt.Dimension(40, 23));
        jpMerchCrud1.add(jbMerchMoveUp);

        jbMerchMoveDown.setText("▼");
        jbMerchMoveDown.setToolTipText("Mover abajo");
        jbMerchMoveDown.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbMerchMoveDown.setPreferredSize(new java.awt.Dimension(40, 23));
        jpMerchCrud1.add(jbMerchMoveDown);

        jpMerch.add(jpMerchCrud1, java.awt.BorderLayout.CENTER);

        jpMerchCrud2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 3));

        jtfMerchCrud.setEditable(false);
        jtfMerchCrud.setText("TEXT");
        jtfMerchCrud.setToolTipText("Acción");
        jtfMerchCrud.setFocusable(false);
        jtfMerchCrud.setPreferredSize(new java.awt.Dimension(100, 23));
        jpMerchCrud2.add(jtfMerchCrud);

        jbMerchOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_ok.gif"))); // NOI18N
        jbMerchOk.setToolTipText("Aceptar");
        jbMerchOk.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbMerchOk.setPreferredSize(new java.awt.Dimension(40, 23));
        jpMerchCrud2.add(jbMerchOk);

        jbMerchCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_cancel.gif"))); // NOI18N
        jbMerchCancel.setToolTipText("Cancelar");
        jbMerchCancel.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbMerchCancel.setPreferredSize(new java.awt.Dimension(40, 23));
        jpMerchCrud2.add(jbMerchCancel);

        jpMerch.add(jpMerchCrud2, java.awt.BorderLayout.EAST);

        jpWizardMerch.add(jpMerch, java.awt.BorderLayout.NORTH);

        jtpWizard.addTab("2. Mercancías", jpWizardMerch);

        jpWizardTruck.setLayout(new java.awt.BorderLayout());

        jpTruck.setLayout(new java.awt.BorderLayout());

        jpTruckInput.setLayout(new java.awt.GridLayout(1, 3));

        jpTruckInput1.setBorder(javax.swing.BorderFactory.createTitledBorder("Autotransporte:"));
        jpTruckInput1.setLayout(new java.awt.GridLayout(8, 1, 0, 3));

        jpTruckInput11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTruckTransportConfig.setText("Config.:*");
        jlTruckTransportConfig.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput11.add(jlTruckTransportConfig);

        moKeyTruckTransportConfig.setPreferredSize(new java.awt.Dimension(205, 23));
        jpTruckInput11.add(moKeyTruckTransportConfig);

        jbTruckEditConfig.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_edit.gif"))); // NOI18N
        jbTruckEditConfig.setToolTipText("Modificar");
        jbTruckEditConfig.setPreferredSize(new java.awt.Dimension(23, 23));
        jpTruckInput11.add(jbTruckEditConfig);

        jpTruckInput1.add(jpTruckInput11);

        jpTruckInput12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTruckName.setText("Nombre:*");
        jlTruckName.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput12.add(jlTruckName);

        moTextTruckName.setText("TEXT");
        moTextTruckName.setPreferredSize(new java.awt.Dimension(230, 23));
        jpTruckInput12.add(moTextTruckName);

        jpTruckInput1.add(jpTruckInput12);

        jpTruckInput13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTruckCode.setText("Código:*");
        jlTruckCode.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput13.add(jlTruckCode);

        moTextTruckCode.setText("TEXT");
        moTextTruckCode.setPreferredSize(new java.awt.Dimension(60, 23));
        jpTruckInput13.add(moTextTruckCode);

        jbTruckGetNextCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_mov_lft.gif"))); // NOI18N
        jbTruckGetNextCode.setToolTipText("Obtener siguiente código");
        jbTruckGetNextCode.setPreferredSize(new java.awt.Dimension(23, 23));
        jpTruckInput13.add(jbTruckGetNextCode);

        jlTruckPlate.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlTruckPlate.setText("Placa:*");
        jlTruckPlate.setPreferredSize(new java.awt.Dimension(57, 23));
        jpTruckInput13.add(jlTruckPlate);

        moTextTruckPlate.setText("TEXT");
        moTextTruckPlate.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput13.add(moTextTruckPlate);

        jpTruckInput1.add(jpTruckInput13);

        jpTruckInput14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTruckWeightTon.setText("Peso:*");
        jlTruckWeightTon.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput14.add(jlTruckWeightTon);

        moDecTruckWeightTon.setText("0.00");
        moDecTruckWeightTon.setPreferredSize(new java.awt.Dimension(60, 23));
        jpTruckInput14.add(moDecTruckWeightTon);

        jlTruckWeightTonUnit.setText("Tn");
        jlTruckWeightTonUnit.setPreferredSize(new java.awt.Dimension(23, 23));
        jpTruckInput14.add(jlTruckWeightTonUnit);

        jlTruckModel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlTruckModel.setText("Modelo:*");
        jlTruckModel.setPreferredSize(new java.awt.Dimension(57, 23));
        jpTruckInput14.add(jlTruckModel);
        jpTruckInput14.add(moYearTruckModel);

        jpTruckInput1.add(jpTruckInput14);

        jpTruckInput15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTruckWeightGrossTon.setText("Peso bruto:*");
        jlTruckWeightGrossTon.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput15.add(jlTruckWeightGrossTon);

        moDecTruckWeightGrossTon.setText("0.00");
        moDecTruckWeightGrossTon.setPreferredSize(new java.awt.Dimension(60, 23));
        jpTruckInput15.add(moDecTruckWeightGrossTon);

        jlTruckWeightGrossTonUnit.setText("Tn");
        jlTruckWeightGrossTonUnit.setPreferredSize(new java.awt.Dimension(25, 23));
        jpTruckInput15.add(jlTruckWeightGrossTonUnit);

        jbTruckSetWeightGrossTon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_mov_lft.gif"))); // NOI18N
        jbTruckSetWeightGrossTon.setToolTipText("Asignar peso bruto");
        jbTruckSetWeightGrossTon.setPreferredSize(new java.awt.Dimension(23, 23));
        jpTruckInput15.add(jbTruckSetWeightGrossTon);

        jpTruckInput1.add(jpTruckInput15);

        jpTruckInput16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTruckPermissionType.setText("Tipo permiso:*");
        jlTruckPermissionType.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput16.add(jlTruckPermissionType);

        moKeyTruckPermissionType.setPreferredSize(new java.awt.Dimension(230, 23));
        jpTruckInput16.add(moKeyTruckPermissionType);

        jpTruckInput1.add(jpTruckInput16);

        jpTruckInput17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTruckPermissionNumber.setText("No. permiso:*");
        jlTruckPermissionNumber.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput17.add(jlTruckPermissionNumber);

        moTextTruckPermissionNumber.setText("TEXT");
        moTextTruckPermissionNumber.setPreferredSize(new java.awt.Dimension(230, 23));
        jpTruckInput17.add(moTextTruckPermissionNumber);

        jpTruckInput1.add(jpTruckInput17);

        jpTruckInput18.setLayout(new java.awt.BorderLayout());

        jpTruckInput181.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfTruckPk.setEditable(false);
        jtfTruckPk.setText("TEXT");
        jtfTruckPk.setToolTipText("PK");
        jtfTruckPk.setFocusable(false);
        jtfTruckPk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput181.add(jtfTruckPk);

        jpTruckInput18.add(jpTruckInput181, java.awt.BorderLayout.CENTER);

        jpTruckInput182.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolTruckUpdate.setText("¡Actualizar catálogo!");
        moBoolTruckUpdate.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        moBoolTruckUpdate.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        moBoolTruckUpdate.setPreferredSize(new java.awt.Dimension(150, 23));
        jpTruckInput182.add(moBoolTruckUpdate);

        jpTruckInput18.add(jpTruckInput182, java.awt.BorderLayout.EAST);

        jpTruckInput1.add(jpTruckInput18);

        jpTruckInput.add(jpTruckInput1);

        jpTruckInput2.setBorder(javax.swing.BorderFactory.createTitledBorder("Seguros:"));
        jpTruckInput2.setLayout(new java.awt.GridLayout(8, 1, 0, 3));

        jpTruckInput21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTruckCivilInsurance.setText("Aseg. resp.:*");
        jlTruckCivilInsurance.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput21.add(jlTruckCivilInsurance);

        moTextTruckCivilInsurance.setText("TEXT");
        moTextTruckCivilInsurance.setPreferredSize(new java.awt.Dimension(230, 23));
        jpTruckInput21.add(moTextTruckCivilInsurance);

        jpTruckInput2.add(jpTruckInput21);

        jpTruckInput22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTruckCivilPolicy.setText("Póliza resp.:*");
        jlTruckCivilPolicy.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput22.add(jlTruckCivilPolicy);

        moTextTruckCivilPolicy.setText("TEXT");
        moTextTruckCivilPolicy.setPreferredSize(new java.awt.Dimension(230, 23));
        jpTruckInput22.add(moTextTruckCivilPolicy);

        jpTruckInput2.add(jpTruckInput22);

        jpTruckInput23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTruckEnvironmentInsurance.setText("Aseg. amb.:");
        jlTruckEnvironmentInsurance.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput23.add(jlTruckEnvironmentInsurance);

        moTextTruckEnvironmentInsurance.setText("TEXT");
        moTextTruckEnvironmentInsurance.setPreferredSize(new java.awt.Dimension(230, 23));
        jpTruckInput23.add(moTextTruckEnvironmentInsurance);

        jpTruckInput2.add(jpTruckInput23);

        jpTruckInput24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTruckEnvironmentPolicy.setText("Póliza amb.:");
        jlTruckEnvironmentPolicy.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput24.add(jlTruckEnvironmentPolicy);

        moTextTruckEnvironmentPolicy.setText("TEXT");
        moTextTruckEnvironmentPolicy.setPreferredSize(new java.awt.Dimension(230, 23));
        jpTruckInput24.add(moTextTruckEnvironmentPolicy);

        jpTruckInput2.add(jpTruckInput24);

        jpTruckInput25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTruckCargoInsurance.setText("Aseg. carga:");
        jlTruckCargoInsurance.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput25.add(jlTruckCargoInsurance);

        moTextTruckCargoInsurance.setText("TEXT");
        moTextTruckCargoInsurance.setPreferredSize(new java.awt.Dimension(230, 23));
        jpTruckInput25.add(moTextTruckCargoInsurance);

        jpTruckInput2.add(jpTruckInput25);

        jpTruckInput26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTruckCargoPolicy.setText("Póliza carga:");
        jlTruckCargoPolicy.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput26.add(jlTruckCargoPolicy);

        moTextTruckCargoPolicy.setText("TEXT");
        moTextTruckCargoPolicy.setPreferredSize(new java.awt.Dimension(230, 23));
        jpTruckInput26.add(moTextTruckCargoPolicy);

        jpTruckInput2.add(jpTruckInput26);

        jpTruckInput27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTruckPrime.setText("Prima:");
        jlTruckPrime.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput27.add(jlTruckPrime);
        jpTruckInput27.add(moCurTruckPrime);

        jpTruckInput2.add(jpTruckInput27);

        jpTruckInput28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpTruckInput2.add(jpTruckInput28);

        jpTruckInput.add(jpTruckInput2);

        jpTruckInput3.setBorder(javax.swing.BorderFactory.createTitledBorder("Remolques:"));
        jpTruckInput3.setLayout(new java.awt.BorderLayout());

        jpTruckInput3N.setLayout(new java.awt.GridLayout(4, 1, 0, 3));

        jpTruckInput3N1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTruckTrailSubtype.setText("Subtipo:*");
        jlTruckTrailSubtype.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput3N1.add(jlTruckTrailSubtype);

        moKeyTruckTrailSubtype.setPreferredSize(new java.awt.Dimension(205, 23));
        jpTruckInput3N1.add(moKeyTruckTrailSubtype);

        jbTruckTrailEditSubtype.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_edit.gif"))); // NOI18N
        jbTruckTrailEditSubtype.setToolTipText("Obtener siguiente código");
        jbTruckTrailEditSubtype.setPreferredSize(new java.awt.Dimension(23, 23));
        jpTruckInput3N1.add(jbTruckTrailEditSubtype);

        jpTruckInput3N.add(jpTruckInput3N1);

        jpTruckInput3N2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTruckTrailPlate.setText("Placa:*");
        jlTruckTrailPlate.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput3N2.add(jlTruckTrailPlate);

        moTextTruckTrailPlate.setText("TEXT");
        moTextTruckTrailPlate.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput3N2.add(moTextTruckTrailPlate);

        jpTruckInput3N.add(jpTruckInput3N2);

        jpTruckInput3N3.setLayout(new java.awt.BorderLayout());

        jpTruckInput3N31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfTruckTrailPk.setEditable(false);
        jtfTruckTrailPk.setText("TEXT");
        jtfTruckTrailPk.setToolTipText("PK");
        jtfTruckTrailPk.setFocusable(false);
        jtfTruckTrailPk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput3N31.add(jtfTruckTrailPk);

        jtfTruckTrailIsNeeded.setEditable(false);
        jtfTruckTrailIsNeeded.setText("TEXT");
        jtfTruckTrailIsNeeded.setToolTipText("¿Remolque requerido?");
        jtfTruckTrailIsNeeded.setFocusable(false);
        jtfTruckTrailIsNeeded.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTruckInput3N31.add(jtfTruckTrailIsNeeded);

        jpTruckInput3N3.add(jpTruckInput3N31, java.awt.BorderLayout.CENTER);

        jpTruckInput3N32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolTruckTrailUpdate.setText("¡Actualizar catálogo!");
        moBoolTruckTrailUpdate.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        moBoolTruckTrailUpdate.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        moBoolTruckTrailUpdate.setPreferredSize(new java.awt.Dimension(150, 23));
        jpTruckInput3N32.add(moBoolTruckTrailUpdate);

        jpTruckInput3N3.add(jpTruckInput3N32, java.awt.BorderLayout.EAST);

        jpTruckInput3N.add(jpTruckInput3N3);

        jpTruckInput3N4.setLayout(new java.awt.BorderLayout());

        jpTruckInput3N41.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbTruckTrailAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_add.gif"))); // NOI18N
        jbTruckTrailAdd.setToolTipText("Agregar");
        jbTruckTrailAdd.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTruckTrailAdd.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTruckInput3N41.add(jbTruckTrailAdd);

        jbTruckTrailCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_new.gif"))); // NOI18N
        jbTruckTrailCreate.setToolTipText("Crear");
        jbTruckTrailCreate.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTruckTrailCreate.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTruckInput3N41.add(jbTruckTrailCreate);

        jbTruckTrailModify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_edit.gif"))); // NOI18N
        jbTruckTrailModify.setToolTipText("Modificar");
        jbTruckTrailModify.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTruckTrailModify.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTruckInput3N41.add(jbTruckTrailModify);

        jbTruckTrailRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_delete.gif"))); // NOI18N
        jbTruckTrailRemove.setToolTipText("Remover");
        jbTruckTrailRemove.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTruckTrailRemove.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTruckInput3N41.add(jbTruckTrailRemove);

        jpTruckInput3N4.add(jpTruckInput3N41, java.awt.BorderLayout.CENTER);

        jpTptInput3N42.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jtfTruckTrailCrud.setEditable(false);
        jtfTruckTrailCrud.setText("TEXT");
        jtfTruckTrailCrud.setToolTipText("Acción");
        jtfTruckTrailCrud.setFocusable(false);
        jtfTruckTrailCrud.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTptInput3N42.add(jtfTruckTrailCrud);

        jbTruckTrailOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_ok.gif"))); // NOI18N
        jbTruckTrailOk.setToolTipText("Aceptar");
        jbTruckTrailOk.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTruckTrailOk.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTptInput3N42.add(jbTruckTrailOk);

        jbTruckTrailCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_cancel.gif"))); // NOI18N
        jbTruckTrailCancel.setToolTipText("Cancelar");
        jbTruckTrailCancel.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTruckTrailCancel.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTptInput3N42.add(jbTruckTrailCancel);

        jpTruckInput3N4.add(jpTptInput3N42, java.awt.BorderLayout.EAST);

        jpTruckInput3N.add(jpTruckInput3N4);

        jpTruckInput3.add(jpTruckInput3N, java.awt.BorderLayout.NORTH);

        jpTruckTrailGrid.setPreferredSize(new java.awt.Dimension(100, 105));
        jpTruckTrailGrid.setLayout(new java.awt.BorderLayout());
        jpTruckInput3.add(jpTruckTrailGrid, java.awt.BorderLayout.SOUTH);

        jpTruckInput.add(jpTruckInput3);

        jpTruck.add(jpTruckInput, java.awt.BorderLayout.PAGE_START);

        jpTruckCrud1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 3));

        jbTruckAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_add.gif"))); // NOI18N
        jbTruckAdd.setToolTipText("Agregar");
        jbTruckAdd.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTruckAdd.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTruckCrud1.add(jbTruckAdd);

        jbTruckCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_new.gif"))); // NOI18N
        jbTruckCreate.setToolTipText("Crear");
        jbTruckCreate.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTruckCreate.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTruckCrud1.add(jbTruckCreate);

        jbTruckCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_copy.gif"))); // NOI18N
        jbTruckCopy.setToolTipText("Copiar");
        jbTruckCopy.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTruckCopy.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTruckCrud1.add(jbTruckCopy);

        jbTruckModify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_edit.gif"))); // NOI18N
        jbTruckModify.setToolTipText("Modificar");
        jbTruckModify.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTruckModify.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTruckCrud1.add(jbTruckModify);

        jbTruckRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_delete.gif"))); // NOI18N
        jbTruckRemove.setToolTipText("Remover");
        jbTruckRemove.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTruckRemove.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTruckCrud1.add(jbTruckRemove);

        jbTruckMoveUp.setText("▲");
        jbTruckMoveUp.setToolTipText("Mover arriba");
        jbTruckMoveUp.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTruckMoveUp.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTruckCrud1.add(jbTruckMoveUp);

        jbTruckMoveDown.setText("▼");
        jbTruckMoveDown.setToolTipText("Mover abajo");
        jbTruckMoveDown.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTruckMoveDown.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTruckCrud1.add(jbTruckMoveDown);

        jpTruck.add(jpTruckCrud1, java.awt.BorderLayout.CENTER);

        jpTruckCrud2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 3));

        jtfTruckCrud.setEditable(false);
        jtfTruckCrud.setText("TEXT");
        jtfTruckCrud.setToolTipText("Acción");
        jtfTruckCrud.setFocusable(false);
        jtfTruckCrud.setPreferredSize(new java.awt.Dimension(100, 23));
        jpTruckCrud2.add(jtfTruckCrud);

        jbTruckOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_ok.gif"))); // NOI18N
        jbTruckOk.setToolTipText("Aceptar");
        jbTruckOk.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTruckOk.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTruckCrud2.add(jbTruckOk);

        jbTruckCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_cancel.gif"))); // NOI18N
        jbTruckCancel.setToolTipText("Cancelar");
        jbTruckCancel.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTruckCancel.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTruckCrud2.add(jbTruckCancel);

        jpTruck.add(jpTruckCrud2, java.awt.BorderLayout.EAST);

        jpWizardTruck.add(jpTruck, java.awt.BorderLayout.NORTH);

        jtpWizard.addTab("3. Autotransporte", jpWizardTruck);

        jpWizardTptFigure.setLayout(new java.awt.BorderLayout());

        jpTptFig.setLayout(new java.awt.BorderLayout());

        jpTptFigInput.setLayout(new java.awt.GridLayout(1, 3));

        jpTptFigInput1.setBorder(javax.swing.BorderFactory.createTitledBorder("Figura del transporte:"));
        jpTptFigInput1.setLayout(new java.awt.GridLayout(8, 1, 0, 3));

        jpTptFigInput11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTptFigTransportFigureType.setText("Tipo:*");
        jlTptFigTransportFigureType.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTptFigInput11.add(jlTptFigTransportFigureType);

        moKeyTptFigTransportFigureType.setPreferredSize(new java.awt.Dimension(205, 23));
        jpTptFigInput11.add(moKeyTptFigTransportFigureType);

        jbTptFigEditType.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_edit.gif"))); // NOI18N
        jbTptFigEditType.setToolTipText("Modificar");
        jbTptFigEditType.setPreferredSize(new java.awt.Dimension(23, 23));
        jpTptFigInput11.add(jbTptFigEditType);

        jpTptFigInput1.add(jpTptFigInput11);

        jpTptFigInput12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTptFigName.setText("Nombre:*");
        jlTptFigName.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTptFigInput12.add(jlTptFigName);

        moTextTptFigName.setText("TEXT");
        moTextTptFigName.setPreferredSize(new java.awt.Dimension(230, 23));
        jpTptFigInput12.add(moTextTptFigName);

        jpTptFigInput1.add(jpTptFigInput12);

        jpTptFigInput13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTptFigCode.setText("Código:*");
        jlTptFigCode.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTptFigInput13.add(jlTptFigCode);

        moTextTptFigCode.setText("TEXT");
        moTextTptFigCode.setPreferredSize(new java.awt.Dimension(60, 23));
        jpTptFigInput13.add(moTextTptFigCode);

        jbTptFigGetNextCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_mov_lft.gif"))); // NOI18N
        jbTptFigGetNextCode.setToolTipText("Obtener siguiente código");
        jbTptFigGetNextCode.setPreferredSize(new java.awt.Dimension(23, 23));
        jpTptFigInput13.add(jbTptFigGetNextCode);

        jlTptFigMail.setForeground(new java.awt.Color(0, 102, 102));
        jlTptFigMail.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlTptFigMail.setText("Mail:");
        jlTptFigMail.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTptFigInput13.add(jlTptFigMail);

        moTextTptFigMail.setText("TEXT");
        moTextTptFigMail.setPreferredSize(new java.awt.Dimension(92, 23));
        jpTptFigInput13.add(moTextTptFigMail);

        jpTptFigInput1.add(jpTptFigInput13);

        jpTptFigInput14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTptFigFiscalId.setText("RFC:*");
        jlTptFigFiscalId.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTptFigInput14.add(jlTptFigFiscalId);

        moTextTptFigFiscalId.setText("TEXT");
        moTextTptFigFiscalId.setPreferredSize(new java.awt.Dimension(150, 23));
        jpTptFigInput14.add(moTextTptFigFiscalId);

        jpTptFigInput1.add(jpTptFigInput14);

        jpTptFigInput15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTptFigDriverLicense.setText("Licencia:*");
        jlTptFigDriverLicense.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTptFigInput15.add(jlTptFigDriverLicense);

        moTextTptFigDriverLicense.setText("TEXT");
        moTextTptFigDriverLicense.setPreferredSize(new java.awt.Dimension(150, 23));
        jpTptFigInput15.add(moTextTptFigDriverLicense);

        jpTptFigInput1.add(jpTptFigInput15);

        jpTptFigInput16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTptFigFigureCountry.setText("Residencia:");
        jlTptFigFigureCountry.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTptFigInput16.add(jlTptFigFigureCountry);

        moKeyTptFigFigureCountry.setPreferredSize(new java.awt.Dimension(230, 23));
        jpTptFigInput16.add(moKeyTptFigFigureCountry);

        jpTptFigInput1.add(jpTptFigInput16);

        jpTptFigInput17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTptFigForeignId.setText("ID fiscal:*");
        jlTptFigForeignId.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTptFigInput17.add(jlTptFigForeignId);

        moTextTptFigForeignId.setText("TEXT");
        moTextTptFigForeignId.setPreferredSize(new java.awt.Dimension(150, 23));
        jpTptFigInput17.add(moTextTptFigForeignId);

        jpTptFigInput1.add(jpTptFigInput17);

        jpTptFigInput18.setLayout(new java.awt.BorderLayout());

        jpTptFigInput181.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfTptFigPk.setEditable(false);
        jtfTptFigPk.setText("TEXT");
        jtfTptFigPk.setToolTipText("PK");
        jtfTptFigPk.setFocusable(false);
        jtfTptFigPk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTptFigInput181.add(jtfTptFigPk);

        jpTptFigInput18.add(jpTptFigInput181, java.awt.BorderLayout.CENTER);

        jpTptFigInput182.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolTptFigUpdate.setText("¡Actualizar catálogo!");
        moBoolTptFigUpdate.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        moBoolTptFigUpdate.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        moBoolTptFigUpdate.setPreferredSize(new java.awt.Dimension(150, 23));
        jpTptFigInput182.add(moBoolTptFigUpdate);

        jpTptFigInput18.add(jpTptFigInput182, java.awt.BorderLayout.EAST);

        jpTptFigInput1.add(jpTptFigInput18);

        jpTptFigInput.add(jpTptFigInput1);

        jpTptFigInput2.setBorder(javax.swing.BorderFactory.createTitledBorder("Domicilio:"));
        jpTptFigInput2.setLayout(new java.awt.GridLayout(8, 1, 0, 3));

        jpTptFigInput21.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTptFigAddressCountry.setText("País:*");
        jlTptFigAddressCountry.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTptFigInput21.add(jlTptFigAddressCountry);

        moKeyTptFigAddressCountry.setPreferredSize(new java.awt.Dimension(230, 23));
        jpTptFigInput21.add(moKeyTptFigAddressCountry);

        jpTptFigInput2.add(jpTptFigInput21);

        jpTptFigInput22.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTptFigAddressState.setText("Estado:*");
        jlTptFigAddressState.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTptFigInput22.add(jlTptFigAddressState);

        moTextTptFigAddressStateCode.setForeground(java.awt.Color.blue);
        moTextTptFigAddressStateCode.setText("XXX");
        moTextTptFigAddressStateCode.setToolTipText("Clave (F5 para buscar)");
        moTextTptFigAddressStateCode.setPreferredSize(new java.awt.Dimension(35, 23));
        jpTptFigInput22.add(moTextTptFigAddressStateCode);

        moTextTptFigAddressStateName.setText("TEXT");
        moTextTptFigAddressStateName.setToolTipText("Descripción");
        moTextTptFigAddressStateName.setPreferredSize(new java.awt.Dimension(190, 23));
        jpTptFigInput22.add(moTextTptFigAddressStateName);

        jpTptFigInput2.add(jpTptFigInput22);

        jpTptFigInput23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTptFigAddressCounty.setText("Municipio:");
        jlTptFigAddressCounty.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTptFigInput23.add(jlTptFigAddressCounty);

        moTextTptFigAddressCountyCode.setForeground(java.awt.Color.blue);
        moTextTptFigAddressCountyCode.setText("000");
        moTextTptFigAddressCountyCode.setToolTipText("Clave (F5 para buscar)");
        moTextTptFigAddressCountyCode.setPreferredSize(new java.awt.Dimension(35, 23));
        jpTptFigInput23.add(moTextTptFigAddressCountyCode);

        moTextTptFigAddressCountyName.setText("TEXT");
        moTextTptFigAddressCountyName.setToolTipText("Descripción");
        moTextTptFigAddressCountyName.setPreferredSize(new java.awt.Dimension(190, 23));
        jpTptFigInput23.add(moTextTptFigAddressCountyName);

        jpTptFigInput2.add(jpTptFigInput23);

        jpTptFigInput24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTptFigAddressLocality.setText("Localidad:");
        jlTptFigAddressLocality.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTptFigInput24.add(jlTptFigAddressLocality);

        moTextTptFigAddressLocalityCode.setForeground(java.awt.Color.blue);
        moTextTptFigAddressLocalityCode.setText("00");
        moTextTptFigAddressLocalityCode.setToolTipText("Clave (F5 para buscar)");
        moTextTptFigAddressLocalityCode.setPreferredSize(new java.awt.Dimension(35, 23));
        jpTptFigInput24.add(moTextTptFigAddressLocalityCode);

        moTextTptFigAddressLocalityName.setText("TEXT");
        moTextTptFigAddressLocalityName.setToolTipText("Descripción");
        moTextTptFigAddressLocalityName.setPreferredSize(new java.awt.Dimension(190, 23));
        jpTptFigInput24.add(moTextTptFigAddressLocalityName);

        jpTptFigInput2.add(jpTptFigInput24);

        jpTptFigInput25.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTptFigAddressZipCode.setText("CP:");
        jlTptFigAddressZipCode.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTptFigInput25.add(jlTptFigAddressZipCode);

        moTextTptFigAddressZipCode.setText("000000000000");
        jpTptFigInput25.add(moTextTptFigAddressZipCode);

        jpTptFigInput2.add(jpTptFigInput25);

        jpTptFigInput26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTptFigAddressDistrict.setText("Colonia:");
        jlTptFigAddressDistrict.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTptFigInput26.add(jlTptFigAddressDistrict);

        moTextTptFigAddressDistrictCode.setForeground(java.awt.Color.blue);
        moTextTptFigAddressDistrictCode.setText("0000");
        moTextTptFigAddressDistrictCode.setToolTipText("Clave (F5 para buscar)");
        moTextTptFigAddressDistrictCode.setPreferredSize(new java.awt.Dimension(35, 23));
        jpTptFigInput26.add(moTextTptFigAddressDistrictCode);

        moTextTptFigAddressDistrictName.setText("TEXT");
        moTextTptFigAddressDistrictName.setToolTipText("Descripción");
        moTextTptFigAddressDistrictName.setPreferredSize(new java.awt.Dimension(190, 23));
        jpTptFigInput26.add(moTextTptFigAddressDistrictName);

        jpTptFigInput2.add(jpTptFigInput26);

        jpTptFigInput27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTptFigAddressStreet.setText("Calle:");
        jlTptFigAddressStreet.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTptFigInput27.add(jlTptFigAddressStreet);

        moTextTptFigAddressStreet.setText("TEXT");
        moTextTptFigAddressStreet.setToolTipText("Calle");
        moTextTptFigAddressStreet.setPreferredSize(new java.awt.Dimension(135, 23));
        jpTptFigInput27.add(moTextTptFigAddressStreet);

        moTextTptFigAddressNumberExt.setText("TEXT");
        moTextTptFigAddressNumberExt.setToolTipText("No. exterior");
        moTextTptFigAddressNumberExt.setPreferredSize(new java.awt.Dimension(50, 23));
        jpTptFigInput27.add(moTextTptFigAddressNumberExt);

        moTextTptFigAddressNumberInt.setText("TEXT");
        moTextTptFigAddressNumberInt.setToolTipText("No. interior");
        moTextTptFigAddressNumberInt.setPreferredSize(new java.awt.Dimension(35, 23));
        jpTptFigInput27.add(moTextTptFigAddressNumberInt);

        jpTptFigInput2.add(jpTptFigInput27);

        jpTptFigInput28.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTptFigAddressReference.setText("Referencia:");
        jlTptFigAddressReference.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTptFigInput28.add(jlTptFigAddressReference);

        moTextTptFigAddressReference.setText("TEXT");
        moTextTptFigAddressReference.setPreferredSize(new java.awt.Dimension(230, 23));
        jpTptFigInput28.add(moTextTptFigAddressReference);

        jpTptFigInput2.add(jpTptFigInput28);

        jpTptFigInput.add(jpTptFigInput2);

        jpTptFigInput3.setBorder(javax.swing.BorderFactory.createTitledBorder("Partes del transporte:"));
        jpTptFigInput3.setLayout(new java.awt.BorderLayout());

        jpTptFigInput3N.setLayout(new java.awt.GridLayout(4, 1, 0, 3));

        jpTptFigInput3N1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlTptFigTptPartTransportPartType.setText("Tipo:*");
        jlTptFigTptPartTransportPartType.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTptFigInput3N1.add(jlTptFigTptPartTransportPartType);

        moKeyTptFigTptPartTransportPartType.setPreferredSize(new java.awt.Dimension(205, 23));
        jpTptFigInput3N1.add(moKeyTptFigTptPartTransportPartType);

        jbTptFigTptPartEditType.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_edit.gif"))); // NOI18N
        jbTptFigTptPartEditType.setToolTipText("Obtener siguiente código");
        jbTptFigTptPartEditType.setPreferredSize(new java.awt.Dimension(23, 23));
        jpTptFigInput3N1.add(jbTptFigTptPartEditType);

        jpTptFigInput3N.add(jpTptFigInput3N1);

        jpTptFigInput3N2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jpTptFigInput3N.add(jpTptFigInput3N2);

        jpTptFigInput3N3.setLayout(new java.awt.BorderLayout());

        jpTptFigInput3N31.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jtfTptFigTptPartPk.setEditable(false);
        jtfTptFigTptPartPk.setText("TEXT");
        jtfTptFigTptPartPk.setToolTipText("PK");
        jtfTptFigTptPartPk.setFocusable(false);
        jtfTptFigTptPartPk.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTptFigInput3N31.add(jtfTptFigTptPartPk);

        jtfTptFigTptPartIsRequired.setEditable(false);
        jtfTptFigTptPartIsRequired.setText("TEXT");
        jtfTptFigTptPartIsRequired.setToolTipText("¿Parte de transporte requerido?");
        jtfTptFigTptPartIsRequired.setFocusable(false);
        jtfTptFigTptPartIsRequired.setPreferredSize(new java.awt.Dimension(75, 23));
        jpTptFigInput3N31.add(jtfTptFigTptPartIsRequired);

        jpTptFigInput3N3.add(jpTptFigInput3N31, java.awt.BorderLayout.CENTER);

        jpTptFigInput3N32.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        moBoolTptFigTptPartUpdate.setText("¡Actualizar catálogo!");
        moBoolTptFigTptPartUpdate.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        moBoolTptFigTptPartUpdate.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        moBoolTptFigTptPartUpdate.setPreferredSize(new java.awt.Dimension(150, 23));
        jpTptFigInput3N32.add(moBoolTptFigTptPartUpdate);

        jpTptFigInput3N3.add(jpTptFigInput3N32, java.awt.BorderLayout.EAST);

        jpTptFigInput3N.add(jpTptFigInput3N3);

        jpTptFigInput3N4.setLayout(new java.awt.BorderLayout());

        jpTptFigInput3N41.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jbTptFigTptPartAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_add.gif"))); // NOI18N
        jbTptFigTptPartAdd.setToolTipText("Agregar");
        jbTptFigTptPartAdd.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTptFigTptPartAdd.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTptFigInput3N41.add(jbTptFigTptPartAdd);

        jbTptFigTptPartCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_new.gif"))); // NOI18N
        jbTptFigTptPartCreate.setToolTipText("Crear");
        jbTptFigTptPartCreate.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTptFigTptPartCreate.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTptFigInput3N41.add(jbTptFigTptPartCreate);

        jbTptFigTptPartModify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_edit.gif"))); // NOI18N
        jbTptFigTptPartModify.setToolTipText("Modificar");
        jbTptFigTptPartModify.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTptFigTptPartModify.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTptFigInput3N41.add(jbTptFigTptPartModify);

        jbTptFigTptPartRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_delete.gif"))); // NOI18N
        jbTptFigTptPartRemove.setToolTipText("Remover");
        jbTptFigTptPartRemove.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTptFigTptPartRemove.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTptFigInput3N41.add(jbTptFigTptPartRemove);

        jpTptFigInput3N4.add(jpTptFigInput3N41, java.awt.BorderLayout.CENTER);

        jpTptFigInput3N42.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 0));

        jtfTptFigTptPartCrud.setEditable(false);
        jtfTptFigTptPartCrud.setText("TEXT");
        jtfTptFigTptPartCrud.setToolTipText("Acción");
        jtfTptFigTptPartCrud.setFocusable(false);
        jtfTptFigTptPartCrud.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTptFigInput3N42.add(jtfTptFigTptPartCrud);

        jbTptFigTptPartOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_ok.gif"))); // NOI18N
        jbTptFigTptPartOk.setToolTipText("Aceptar");
        jbTptFigTptPartOk.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTptFigTptPartOk.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTptFigInput3N42.add(jbTptFigTptPartOk);

        jbTptFigTptPartCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_cancel.gif"))); // NOI18N
        jbTptFigTptPartCancel.setToolTipText("Cancelar");
        jbTptFigTptPartCancel.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTptFigTptPartCancel.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTptFigInput3N42.add(jbTptFigTptPartCancel);

        jpTptFigInput3N4.add(jpTptFigInput3N42, java.awt.BorderLayout.EAST);

        jpTptFigInput3N.add(jpTptFigInput3N4);

        jpTptFigInput3.add(jpTptFigInput3N, java.awt.BorderLayout.NORTH);

        jpTptFigTptPartGrid.setPreferredSize(new java.awt.Dimension(100, 105));
        jpTptFigTptPartGrid.setLayout(new java.awt.BorderLayout());
        jpTptFigInput3.add(jpTptFigTptPartGrid, java.awt.BorderLayout.SOUTH);

        jpTptFigInput.add(jpTptFigInput3);

        jpTptFig.add(jpTptFigInput, java.awt.BorderLayout.PAGE_START);

        jpTptFigCrud1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 3));

        jbTptFigAdd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_add.gif"))); // NOI18N
        jbTptFigAdd.setToolTipText("Agregar");
        jbTptFigAdd.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTptFigAdd.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTptFigCrud1.add(jbTptFigAdd);

        jbTptFigCreate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_new.gif"))); // NOI18N
        jbTptFigCreate.setToolTipText("Crear");
        jbTptFigCreate.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTptFigCreate.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTptFigCrud1.add(jbTptFigCreate);

        jbTptFigCopy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_copy.gif"))); // NOI18N
        jbTptFigCopy.setToolTipText("Copiar");
        jbTptFigCopy.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTptFigCopy.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTptFigCrud1.add(jbTptFigCopy);

        jbTptFigModify.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_edit.gif"))); // NOI18N
        jbTptFigModify.setToolTipText("Modificar");
        jbTptFigModify.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTptFigModify.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTptFigCrud1.add(jbTptFigModify);

        jbTptFigRemove.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_delete.gif"))); // NOI18N
        jbTptFigRemove.setToolTipText("Remover");
        jbTptFigRemove.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTptFigRemove.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTptFigCrud1.add(jbTptFigRemove);

        jbTptFigMoveUp.setText("▲");
        jbTptFigMoveUp.setToolTipText("Mover arriba");
        jbTptFigMoveUp.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTptFigMoveUp.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTptFigCrud1.add(jbTptFigMoveUp);

        jbTptFigMoveDown.setText("▼");
        jbTptFigMoveDown.setToolTipText("Mover abajo");
        jbTptFigMoveDown.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTptFigMoveDown.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTptFigCrud1.add(jbTptFigMoveDown);

        jpTptFig.add(jpTptFigCrud1, java.awt.BorderLayout.CENTER);

        jpTptFigCrud2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 5, 3));

        jtfTptFigCrud.setEditable(false);
        jtfTptFigCrud.setText("TEXT");
        jtfTptFigCrud.setToolTipText("Acción");
        jtfTptFigCrud.setFocusable(false);
        jtfTptFigCrud.setPreferredSize(new java.awt.Dimension(100, 23));
        jpTptFigCrud2.add(jtfTptFigCrud);

        jbTptFigOk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_ok.gif"))); // NOI18N
        jbTptFigOk.setToolTipText("Aceptar");
        jbTptFigOk.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTptFigOk.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTptFigCrud2.add(jbTptFigOk);

        jbTptFigCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/cmd_std_cancel.gif"))); // NOI18N
        jbTptFigCancel.setToolTipText("Cancelar");
        jbTptFigCancel.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbTptFigCancel.setPreferredSize(new java.awt.Dimension(40, 23));
        jpTptFigCrud2.add(jbTptFigCancel);

        jpTptFig.add(jpTptFigCrud2, java.awt.BorderLayout.EAST);

        jpWizardTptFigure.add(jpTptFig, java.awt.BorderLayout.NORTH);

        jtpWizard.addTab("4. Figuras del transporte", jpWizardTptFigure);

        jPanel1.add(jtpWizard, java.awt.BorderLayout.CENTER);

        jpNav.setLayout(new java.awt.BorderLayout());

        jpNavW.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jtfOwnBranch.setEditable(false);
        jtfOwnBranch.setText("TEXT");
        jtfOwnBranch.setToolTipText("Sucursal");
        jtfOwnBranch.setFocusable(false);
        jtfOwnBranch.setPreferredSize(new java.awt.Dimension(50, 23));
        jpNavW.add(jtfOwnBranch);

        jpNav.add(jpNavW, java.awt.BorderLayout.WEST);

        jbBolNavStart.setBackground(java.awt.Color.green);
        jbBolNavStart.setText("Iniciar");
        jbBolNavStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jpNavC.add(jbBolNavStart);

        jbBolNavRestart.setBackground(java.awt.Color.red);
        jbBolNavRestart.setText("Reiniciar");
        jbBolNavRestart.setPreferredSize(new java.awt.Dimension(100, 23));
        jpNavC.add(jbBolNavRestart);

        jbBolNavPrev.setBackground(java.awt.Color.yellow);
        jbBolNavPrev.setText("◄");
        jbBolNavPrev.setToolTipText("Ir atrás");
        jbBolNavPrev.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbBolNavPrev.setPreferredSize(new java.awt.Dimension(100, 23));
        jpNavC.add(jbBolNavPrev);

        jbBolNavNext.setBackground(java.awt.Color.yellow);
        jbBolNavNext.setText("►");
        jbBolNavNext.setToolTipText("Ir adelante");
        jbBolNavNext.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jbBolNavNext.setPreferredSize(new java.awt.Dimension(100, 23));
        jpNavC.add(jbBolNavNext);

        jpNav.add(jpNavC, java.awt.BorderLayout.CENTER);

        jpNavE.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setPreferredSize(new java.awt.Dimension(50, 23));
        jpNavE.add(jLabel1);

        jpNav.add(jpNavE, java.awt.BorderLayout.EAST);

        jPanel1.add(jpNav, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bgMerchDimensionUnits;
    private javax.swing.ButtonGroup bgMerchHazardousMaterial;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jbBolNavNext;
    private javax.swing.JButton jbBolNavPrev;
    private javax.swing.JButton jbBolNavRestart;
    private javax.swing.JButton jbBolNavStart;
    private javax.swing.JButton jbBolSetIntlTransport;
    private javax.swing.JButton jbBolUpdateVersion;
    private javax.swing.JButton jbLocAdd;
    private javax.swing.JButton jbLocCancel;
    private javax.swing.JButton jbLocCopy;
    private javax.swing.JButton jbLocCreate;
    private javax.swing.JButton jbLocEditType;
    private javax.swing.JButton jbLocGetNextCode;
    private javax.swing.JButton jbLocModify;
    private javax.swing.JButton jbLocMoveDown;
    private javax.swing.JButton jbLocMoveUp;
    private javax.swing.JButton jbLocOk;
    private javax.swing.JButton jbLocRemove;
    private javax.swing.JButton jbMerchAdd;
    private javax.swing.JButton jbMerchCancel;
    private javax.swing.JButton jbMerchCopy;
    private javax.swing.JButton jbMerchCreate;
    private javax.swing.JButton jbMerchModify;
    private javax.swing.JButton jbMerchMoveAdd;
    private javax.swing.JButton jbMerchMoveCancel;
    private javax.swing.JButton jbMerchMoveCreate;
    private javax.swing.JButton jbMerchMoveDown;
    private javax.swing.JButton jbMerchMoveModify;
    private javax.swing.JButton jbMerchMoveOk;
    private javax.swing.JButton jbMerchMoveRemove;
    private javax.swing.JButton jbMerchMoveSetQuantity;
    private javax.swing.JButton jbMerchMoveUp;
    private javax.swing.JButton jbMerchOk;
    private javax.swing.JButton jbMerchPickItem;
    private javax.swing.JButton jbMerchRemove;
    private javax.swing.JButton jbMerchSetWeightKg;
    private javax.swing.JButton jbTptFigAdd;
    private javax.swing.JButton jbTptFigCancel;
    private javax.swing.JButton jbTptFigCopy;
    private javax.swing.JButton jbTptFigCreate;
    private javax.swing.JButton jbTptFigEditType;
    private javax.swing.JButton jbTptFigGetNextCode;
    private javax.swing.JButton jbTptFigModify;
    private javax.swing.JButton jbTptFigMoveDown;
    private javax.swing.JButton jbTptFigMoveUp;
    private javax.swing.JButton jbTptFigOk;
    private javax.swing.JButton jbTptFigRemove;
    private javax.swing.JButton jbTptFigTptPartAdd;
    private javax.swing.JButton jbTptFigTptPartCancel;
    private javax.swing.JButton jbTptFigTptPartCreate;
    private javax.swing.JButton jbTptFigTptPartEditType;
    private javax.swing.JButton jbTptFigTptPartModify;
    private javax.swing.JButton jbTptFigTptPartOk;
    private javax.swing.JButton jbTptFigTptPartRemove;
    private javax.swing.JButton jbTruckAdd;
    private javax.swing.JButton jbTruckCancel;
    private javax.swing.JButton jbTruckCopy;
    private javax.swing.JButton jbTruckCreate;
    private javax.swing.JButton jbTruckEditConfig;
    private javax.swing.JButton jbTruckGetNextCode;
    private javax.swing.JButton jbTruckModify;
    private javax.swing.JButton jbTruckMoveDown;
    private javax.swing.JButton jbTruckMoveUp;
    private javax.swing.JButton jbTruckOk;
    private javax.swing.JButton jbTruckRemove;
    private javax.swing.JButton jbTruckSetWeightGrossTon;
    private javax.swing.JButton jbTruckTrailAdd;
    private javax.swing.JButton jbTruckTrailCancel;
    private javax.swing.JButton jbTruckTrailCreate;
    private javax.swing.JButton jbTruckTrailEditSubtype;
    private javax.swing.JButton jbTruckTrailModify;
    private javax.swing.JButton jbTruckTrailOk;
    private javax.swing.JButton jbTruckTrailRemove;
    private javax.swing.JLabel jlBolArrival;
    private javax.swing.JLabel jlBolBolTemplate;
    private javax.swing.JLabel jlBolDate;
    private javax.swing.JLabel jlBolDeparture;
    private javax.swing.JLabel jlBolDistanceKm;
    private javax.swing.JLabel jlBolDistanceKmUnit;
    private javax.swing.JLabel jlBolIsthmusDestiny;
    private javax.swing.JLabel jlBolIsthmusOrigin;
    private javax.swing.JLabel jlBolMerchandiseNumber;
    private javax.swing.JLabel jlBolMerchandiseWeightKg;
    private javax.swing.JLabel jlBolMerchandiseWeightKgUnit;
    private javax.swing.JLabel jlBolNumber;
    private javax.swing.JLabel jlBolTemplateCode;
    private javax.swing.JLabel jlBolTemplateName;
    private javax.swing.JLabel jlBolTransportType;
    private javax.swing.JLabel jlLocAddressCountry;
    private javax.swing.JLabel jlLocAddressCounty;
    private javax.swing.JLabel jlLocAddressDistrict;
    private javax.swing.JLabel jlLocAddressLocality;
    private javax.swing.JLabel jlLocAddressReference;
    private javax.swing.JLabel jlLocAddressState;
    private javax.swing.JLabel jlLocAddressStreet;
    private javax.swing.JLabel jlLocAddressZipCode;
    private javax.swing.JLabel jlLocArrivalDepartureDatetime;
    private javax.swing.JLabel jlLocArrivalDepartureDatetimeHelp;
    private javax.swing.JLabel jlLocCode;
    private javax.swing.JLabel jlLocDistanceKm;
    private javax.swing.JLabel jlLocDistanceKmUnit;
    private javax.swing.JLabel jlLocLocationId;
    private javax.swing.JLabel jlLocLocationType;
    private javax.swing.JLabel jlLocName;
    private javax.swing.JLabel jlLocNotes;
    private javax.swing.JLabel jlLocSourceLocation;
    private javax.swing.JLabel jlMerchCfdItemKey;
    private javax.swing.JLabel jlMerchCfdItemKeySeparator;
    private javax.swing.JLabel jlMerchCurrency;
    private javax.swing.JLabel jlMerchDimensions;
    private javax.swing.JLabel jlMerchDimensions1;
    private javax.swing.JLabel jlMerchDimensions2;
    private javax.swing.JLabel jlMerchDimensionsHint;
    private javax.swing.JLabel jlMerchHazardousMaterial;
    private javax.swing.JLabel jlMerchImportRequest;
    private javax.swing.JLabel jlMerchImportRequestHint;
    private javax.swing.JLabel jlMerchItem;
    private javax.swing.JLabel jlMerchItemDescription;
    private javax.swing.JLabel jlMerchMoveDestiny;
    private javax.swing.JLabel jlMerchMoveQuantity;
    private javax.swing.JLabel jlMerchMoveSource;
    private javax.swing.JLabel jlMerchPackaging;
    private javax.swing.JLabel jlMerchQuantity;
    private javax.swing.JLabel jlMerchRatioKgHint;
    private javax.swing.JLabel jlMerchTariff;
    private javax.swing.JLabel jlMerchUnit;
    private javax.swing.JLabel jlMerchUnitDescription;
    private javax.swing.JLabel jlMerchValue;
    private javax.swing.JLabel jlMerchWeightKg;
    private javax.swing.JLabel jlMerchWeightKgUnit;
    private javax.swing.JLabel jlTptFigAddressCountry;
    private javax.swing.JLabel jlTptFigAddressCounty;
    private javax.swing.JLabel jlTptFigAddressDistrict;
    private javax.swing.JLabel jlTptFigAddressLocality;
    private javax.swing.JLabel jlTptFigAddressReference;
    private javax.swing.JLabel jlTptFigAddressState;
    private javax.swing.JLabel jlTptFigAddressStreet;
    private javax.swing.JLabel jlTptFigAddressZipCode;
    private javax.swing.JLabel jlTptFigCode;
    private javax.swing.JLabel jlTptFigDriverLicense;
    private javax.swing.JLabel jlTptFigFigureCountry;
    private javax.swing.JLabel jlTptFigFiscalId;
    private javax.swing.JLabel jlTptFigForeignId;
    private javax.swing.JLabel jlTptFigMail;
    private javax.swing.JLabel jlTptFigName;
    private javax.swing.JLabel jlTptFigTptPartTransportPartType;
    private javax.swing.JLabel jlTptFigTransportFigureType;
    private javax.swing.JLabel jlTruckCargoInsurance;
    private javax.swing.JLabel jlTruckCargoPolicy;
    private javax.swing.JLabel jlTruckCivilInsurance;
    private javax.swing.JLabel jlTruckCivilPolicy;
    private javax.swing.JLabel jlTruckCode;
    private javax.swing.JLabel jlTruckEnvironmentInsurance;
    private javax.swing.JLabel jlTruckEnvironmentPolicy;
    private javax.swing.JLabel jlTruckModel;
    private javax.swing.JLabel jlTruckName;
    private javax.swing.JLabel jlTruckPermissionNumber;
    private javax.swing.JLabel jlTruckPermissionType;
    private javax.swing.JLabel jlTruckPlate;
    private javax.swing.JLabel jlTruckPrime;
    private javax.swing.JLabel jlTruckTrailPlate;
    private javax.swing.JLabel jlTruckTrailSubtype;
    private javax.swing.JLabel jlTruckTransportConfig;
    private javax.swing.JLabel jlTruckWeightGrossTon;
    private javax.swing.JLabel jlTruckWeightGrossTonUnit;
    private javax.swing.JLabel jlTruckWeightTon;
    private javax.swing.JLabel jlTruckWeightTonUnit;
    private javax.swing.JPanel jpHeader;
    private javax.swing.JPanel jpHeaderBol;
    private javax.swing.JPanel jpHeaderBol1;
    private javax.swing.JPanel jpHeaderBol2;
    private javax.swing.JPanel jpHeaderBol3;
    private javax.swing.JPanel jpHeaderBol4;
    private javax.swing.JPanel jpHeaderBol5;
    private javax.swing.JPanel jpHeaderCfg;
    private javax.swing.JPanel jpHeaderCfg2;
    private javax.swing.JPanel jpHeaderIntl4;
    private javax.swing.JPanel jpHeaderIntl45;
    private javax.swing.JPanel jpHeaderLocMerch;
    private javax.swing.JPanel jpHeaderLocMerch1;
    private javax.swing.JPanel jpHeaderLocMerch2;
    private javax.swing.JPanel jpHeaderLocMerch3;
    private javax.swing.JPanel jpHeaderLocMerch4;
    private javax.swing.JPanel jpHeaderTemp;
    private javax.swing.JPanel jpHeaderTemp2;
    private javax.swing.JPanel jpHeaderTemp3;
    private javax.swing.JPanel jpHeaderTemp4;
    private javax.swing.JPanel jpHeaderTemp5;
    private javax.swing.JPanel jpLoc;
    private javax.swing.JPanel jpLocCrud1;
    private javax.swing.JPanel jpLocCrud2;
    private javax.swing.JPanel jpLocInput;
    private javax.swing.JPanel jpLocInput1;
    private javax.swing.JPanel jpLocInput11;
    private javax.swing.JPanel jpLocInput12;
    private javax.swing.JPanel jpLocInput13;
    private javax.swing.JPanel jpLocInput14;
    private javax.swing.JPanel jpLocInput15;
    private javax.swing.JPanel jpLocInput16;
    private javax.swing.JPanel jpLocInput17;
    private javax.swing.JPanel jpLocInput18;
    private javax.swing.JPanel jpLocInput181;
    private javax.swing.JPanel jpLocInput182;
    private javax.swing.JPanel jpLocInput2;
    private javax.swing.JPanel jpLocInput21;
    private javax.swing.JPanel jpLocInput22;
    private javax.swing.JPanel jpLocInput23;
    private javax.swing.JPanel jpLocInput24;
    private javax.swing.JPanel jpLocInput25;
    private javax.swing.JPanel jpLocInput26;
    private javax.swing.JPanel jpLocInput27;
    private javax.swing.JPanel jpLocInput28;
    private javax.swing.JPanel jpLocInput3;
    private javax.swing.JPanel jpLocInput31;
    private javax.swing.JPanel jpLocInput32;
    private javax.swing.JPanel jpLocInput33;
    private javax.swing.JPanel jpLocInput34;
    private javax.swing.JPanel jpLocInput35;
    private javax.swing.JPanel jpLocInput36;
    private javax.swing.JPanel jpLocInput37;
    private javax.swing.JPanel jpLocInput38;
    private javax.swing.JPanel jpMerch;
    private javax.swing.JPanel jpMerchCrud1;
    private javax.swing.JPanel jpMerchCrud2;
    private javax.swing.JPanel jpMerchInput;
    private javax.swing.JPanel jpMerchInput1;
    private javax.swing.JPanel jpMerchInput11;
    private javax.swing.JPanel jpMerchInput12;
    private javax.swing.JPanel jpMerchInput13;
    private javax.swing.JPanel jpMerchInput14;
    private javax.swing.JPanel jpMerchInput15;
    private javax.swing.JPanel jpMerchInput16;
    private javax.swing.JPanel jpMerchInput17;
    private javax.swing.JPanel jpMerchInput18;
    private javax.swing.JPanel jpMerchInput2;
    private javax.swing.JPanel jpMerchInput21;
    private javax.swing.JPanel jpMerchInput22;
    private javax.swing.JPanel jpMerchInput23;
    private javax.swing.JPanel jpMerchInput24;
    private javax.swing.JPanel jpMerchInput25;
    private javax.swing.JPanel jpMerchInput26;
    private javax.swing.JPanel jpMerchInput27;
    private javax.swing.JPanel jpMerchInput28;
    private javax.swing.JPanel jpMerchInput3;
    private javax.swing.JPanel jpMerchInput3N;
    private javax.swing.JPanel jpMerchInput3N1;
    private javax.swing.JPanel jpMerchInput3N2;
    private javax.swing.JPanel jpMerchInput3N3;
    private javax.swing.JPanel jpMerchInput3N4;
    private javax.swing.JPanel jpMerchInput3N41;
    private javax.swing.JPanel jpMerchInput3N42;
    private javax.swing.JPanel jpMerchMoveGrid;
    private javax.swing.JPanel jpNav;
    private javax.swing.JPanel jpNavC;
    private javax.swing.JPanel jpNavE;
    private javax.swing.JPanel jpNavW;
    private javax.swing.JPanel jpTptFig;
    private javax.swing.JPanel jpTptFigCrud1;
    private javax.swing.JPanel jpTptFigCrud2;
    private javax.swing.JPanel jpTptFigInput;
    private javax.swing.JPanel jpTptFigInput1;
    private javax.swing.JPanel jpTptFigInput11;
    private javax.swing.JPanel jpTptFigInput12;
    private javax.swing.JPanel jpTptFigInput13;
    private javax.swing.JPanel jpTptFigInput14;
    private javax.swing.JPanel jpTptFigInput15;
    private javax.swing.JPanel jpTptFigInput16;
    private javax.swing.JPanel jpTptFigInput17;
    private javax.swing.JPanel jpTptFigInput18;
    private javax.swing.JPanel jpTptFigInput181;
    private javax.swing.JPanel jpTptFigInput182;
    private javax.swing.JPanel jpTptFigInput2;
    private javax.swing.JPanel jpTptFigInput21;
    private javax.swing.JPanel jpTptFigInput22;
    private javax.swing.JPanel jpTptFigInput23;
    private javax.swing.JPanel jpTptFigInput24;
    private javax.swing.JPanel jpTptFigInput25;
    private javax.swing.JPanel jpTptFigInput26;
    private javax.swing.JPanel jpTptFigInput27;
    private javax.swing.JPanel jpTptFigInput28;
    private javax.swing.JPanel jpTptFigInput3;
    private javax.swing.JPanel jpTptFigInput3N;
    private javax.swing.JPanel jpTptFigInput3N1;
    private javax.swing.JPanel jpTptFigInput3N2;
    private javax.swing.JPanel jpTptFigInput3N3;
    private javax.swing.JPanel jpTptFigInput3N31;
    private javax.swing.JPanel jpTptFigInput3N32;
    private javax.swing.JPanel jpTptFigInput3N4;
    private javax.swing.JPanel jpTptFigInput3N41;
    private javax.swing.JPanel jpTptFigInput3N42;
    private javax.swing.JPanel jpTptFigTptPartGrid;
    private javax.swing.JPanel jpTptInput3N42;
    private javax.swing.JPanel jpTruck;
    private javax.swing.JPanel jpTruckCrud1;
    private javax.swing.JPanel jpTruckCrud2;
    private javax.swing.JPanel jpTruckInput;
    private javax.swing.JPanel jpTruckInput1;
    private javax.swing.JPanel jpTruckInput11;
    private javax.swing.JPanel jpTruckInput12;
    private javax.swing.JPanel jpTruckInput13;
    private javax.swing.JPanel jpTruckInput14;
    private javax.swing.JPanel jpTruckInput15;
    private javax.swing.JPanel jpTruckInput16;
    private javax.swing.JPanel jpTruckInput17;
    private javax.swing.JPanel jpTruckInput18;
    private javax.swing.JPanel jpTruckInput181;
    private javax.swing.JPanel jpTruckInput182;
    private javax.swing.JPanel jpTruckInput2;
    private javax.swing.JPanel jpTruckInput21;
    private javax.swing.JPanel jpTruckInput22;
    private javax.swing.JPanel jpTruckInput23;
    private javax.swing.JPanel jpTruckInput24;
    private javax.swing.JPanel jpTruckInput25;
    private javax.swing.JPanel jpTruckInput26;
    private javax.swing.JPanel jpTruckInput27;
    private javax.swing.JPanel jpTruckInput28;
    private javax.swing.JPanel jpTruckInput3;
    private javax.swing.JPanel jpTruckInput3N;
    private javax.swing.JPanel jpTruckInput3N1;
    private javax.swing.JPanel jpTruckInput3N2;
    private javax.swing.JPanel jpTruckInput3N3;
    private javax.swing.JPanel jpTruckInput3N31;
    private javax.swing.JPanel jpTruckInput3N32;
    private javax.swing.JPanel jpTruckInput3N4;
    private javax.swing.JPanel jpTruckInput3N41;
    private javax.swing.JPanel jpTruckTrailGrid;
    private javax.swing.JPanel jpWizardLoc;
    private javax.swing.JPanel jpWizardMerch;
    private javax.swing.JPanel jpWizardTptFigure;
    private javax.swing.JPanel jpWizardTruck;
    private javax.swing.JTextField jtfBolArrivalDatetime;
    private javax.swing.JTextField jtfBolArrivalLocation;
    private javax.swing.JTextField jtfBolBolTemplate;
    private javax.swing.JTextField jtfBolBolUuid;
    private javax.swing.JTextField jtfBolDepartureLocation;
    private javax.swing.JTextField jtfBolDeparturelDatetime;
    private javax.swing.JTextField jtfBolDfrUuid;
    private javax.swing.JTextField jtfBolDistanceKm;
    private javax.swing.JTextField jtfBolIntlTransport;
    private javax.swing.JTextField jtfBolMerchandiseNumber;
    private javax.swing.JTextField jtfBolMerchandiseWeightKg;
    private javax.swing.JTextField jtfBolNumber;
    private javax.swing.JTextField jtfBolStatus;
    private javax.swing.JTextField jtfBolTransportType;
    private javax.swing.JTextField jtfBolVersion;
    private javax.swing.JTextField jtfLocCrud;
    private javax.swing.JTextField jtfLocLocationId;
    private javax.swing.JTextField jtfLocPk;
    private javax.swing.JTextField jtfMerchCrud;
    private javax.swing.JTextField jtfMerchDimensionsHint;
    private javax.swing.JTextField jtfMerchImportRequestHint;
    private javax.swing.JTextField jtfMerchMoveCrud;
    private javax.swing.JTextField jtfMerchQuantityMoved;
    private javax.swing.JTextField jtfOwnBranch;
    private javax.swing.JTextField jtfTptFigCrud;
    private javax.swing.JTextField jtfTptFigPk;
    private javax.swing.JTextField jtfTptFigTptPartCrud;
    private javax.swing.JTextField jtfTptFigTptPartIsRequired;
    private javax.swing.JTextField jtfTptFigTptPartPk;
    private javax.swing.JTextField jtfTruckCrud;
    private javax.swing.JTextField jtfTruckPk;
    private javax.swing.JTextField jtfTruckTrailCrud;
    private javax.swing.JTextField jtfTruckTrailIsNeeded;
    private javax.swing.JTextField jtfTruckTrailPk;
    private javax.swing.JTabbedPane jtpWizard;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolBolIntlTransport;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolBolIsthmus;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolBolMerchandiseInverseLogistics;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolBolTemplate;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolLocUpdate;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolMerchHazardousMaterial;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolTptFigTptPartUpdate;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolTptFigUpdate;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolTruckTrailUpdate;
    private sba.lib.gui.bean.DBeanFieldBoolean moBoolTruckUpdate;
    private sba.lib.gui.bean.DBeanCompoundFieldCurrency moCurMerchValue;
    private sba.lib.gui.bean.DBeanCompoundFieldCurrency moCurTruckPrime;
    private sba.lib.gui.bean.DBeanFieldDate moDateBolDate;
    private sba.lib.gui.bean.DBeanFieldDatetime moDatetimeLocArrivalDepartureDatetime;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecLocDistanceKm;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecMerchMoveQuantity;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecMerchQuantity;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecMerchWeightKg;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecTruckWeightGrossTon;
    private sba.lib.gui.bean.DBeanFieldDecimal moDecTruckWeightTon;
    private sba.lib.gui.bean.DBeanFieldInteger moIntMerchDimensionsHeight;
    private sba.lib.gui.bean.DBeanFieldInteger moIntMerchDimensionsLength;
    private sba.lib.gui.bean.DBeanFieldInteger moIntMerchDimensionsWidth;
    private sba.lib.gui.bean.DBeanFieldInteger moIntMerchImportRequest1;
    private sba.lib.gui.bean.DBeanFieldInteger moIntMerchImportRequest2;
    private sba.lib.gui.bean.DBeanFieldInteger moIntMerchImportRequest3;
    private sba.lib.gui.bean.DBeanFieldInteger moIntMerchImportRequest4;
    private sba.lib.gui.bean.DBeanFieldKey moKeyBolIsthmusDestiny;
    private sba.lib.gui.bean.DBeanFieldKey moKeyBolIsthmusOrigin;
    private sba.lib.gui.bean.DBeanFieldKey moKeyBolSeries;
    private sba.lib.gui.bean.DBeanFieldKey moKeyLocAddressCountry;
    private sba.lib.gui.bean.DBeanFieldKey moKeyLocLocationType;
    private sba.lib.gui.bean.DBeanFieldKey moKeyLocSourceLocation;
    private sba.lib.gui.bean.DBeanFieldKey moKeyMerchCurrency;
    private sba.lib.gui.bean.DBeanFieldKey moKeyMerchItem;
    private sba.lib.gui.bean.DBeanFieldKey moKeyMerchMoveDestiny;
    private sba.lib.gui.bean.DBeanFieldKey moKeyMerchMoveSource;
    private sba.lib.gui.bean.DBeanFieldKey moKeyMerchUnit;
    private sba.lib.gui.bean.DBeanFieldKey moKeyTptFigAddressCountry;
    private sba.lib.gui.bean.DBeanFieldKey moKeyTptFigFigureCountry;
    private sba.lib.gui.bean.DBeanFieldKey moKeyTptFigTptPartTransportPartType;
    private sba.lib.gui.bean.DBeanFieldKey moKeyTptFigTransportFigureType;
    private sba.lib.gui.bean.DBeanFieldKey moKeyTruckPermissionType;
    private sba.lib.gui.bean.DBeanFieldKey moKeyTruckTrailSubtype;
    private sba.lib.gui.bean.DBeanFieldKey moKeyTruckTransportConfig;
    private sba.lib.gui.bean.DBeanFieldRadio moRadMerchDimensionsCm;
    private sba.lib.gui.bean.DBeanFieldRadio moRadMerchDimensionsPlg;
    private sba.lib.gui.bean.DBeanFieldRadio moRadMerchHazardousMaterialNo;
    private sba.lib.gui.bean.DBeanFieldRadio moRadMerchHazardousMaterialYes;
    private sba.lib.gui.bean.DBeanFieldText moTextBolTemplateCode;
    private sba.lib.gui.bean.DBeanFieldText moTextBolTemplateName;
    private sba.lib.gui.bean.DBeanFieldText moTextLocAddressCountyCode;
    private sba.lib.gui.bean.DBeanFieldText moTextLocAddressCountyName;
    private sba.lib.gui.bean.DBeanFieldText moTextLocAddressDistrictCode;
    private sba.lib.gui.bean.DBeanFieldText moTextLocAddressDistrictName;
    private sba.lib.gui.bean.DBeanFieldText moTextLocAddressLocalityCode;
    private sba.lib.gui.bean.DBeanFieldText moTextLocAddressLocalityName;
    private sba.lib.gui.bean.DBeanFieldText moTextLocAddressNumberExt;
    private sba.lib.gui.bean.DBeanFieldText moTextLocAddressNumberInt;
    private sba.lib.gui.bean.DBeanFieldText moTextLocAddressReference;
    private sba.lib.gui.bean.DBeanFieldText moTextLocAddressStateCode;
    private sba.lib.gui.bean.DBeanFieldText moTextLocAddressStateName;
    private sba.lib.gui.bean.DBeanFieldText moTextLocAddressStreet;
    private sba.lib.gui.bean.DBeanFieldText moTextLocAddressZipCode;
    private sba.lib.gui.bean.DBeanFieldText moTextLocCode;
    private sba.lib.gui.bean.DBeanFieldText moTextLocName;
    private sba.lib.gui.bean.DBeanFieldText moTextLocNotes;
    private sba.lib.gui.bean.DBeanFieldText moTextMerchHazardousMaterialCode;
    private sba.lib.gui.bean.DBeanFieldText moTextMerchHazardousMaterialName;
    private sba.lib.gui.bean.DBeanFieldText moTextMerchItemDescription;
    private sba.lib.gui.bean.DBeanFieldText moTextMerchPackagingCode;
    private sba.lib.gui.bean.DBeanFieldText moTextMerchPackagingName;
    private sba.lib.gui.bean.DBeanFieldText moTextMerchTariff;
    private sba.lib.gui.bean.DBeanFieldText moTextMerchUnitDescription;
    private sba.lib.gui.bean.DBeanFieldText moTextTptFigAddressCountyCode;
    private sba.lib.gui.bean.DBeanFieldText moTextTptFigAddressCountyName;
    private sba.lib.gui.bean.DBeanFieldText moTextTptFigAddressDistrictCode;
    private sba.lib.gui.bean.DBeanFieldText moTextTptFigAddressDistrictName;
    private sba.lib.gui.bean.DBeanFieldText moTextTptFigAddressLocalityCode;
    private sba.lib.gui.bean.DBeanFieldText moTextTptFigAddressLocalityName;
    private sba.lib.gui.bean.DBeanFieldText moTextTptFigAddressNumberExt;
    private sba.lib.gui.bean.DBeanFieldText moTextTptFigAddressNumberInt;
    private sba.lib.gui.bean.DBeanFieldText moTextTptFigAddressReference;
    private sba.lib.gui.bean.DBeanFieldText moTextTptFigAddressStateCode;
    private sba.lib.gui.bean.DBeanFieldText moTextTptFigAddressStateName;
    private sba.lib.gui.bean.DBeanFieldText moTextTptFigAddressStreet;
    private sba.lib.gui.bean.DBeanFieldText moTextTptFigAddressZipCode;
    private sba.lib.gui.bean.DBeanFieldText moTextTptFigCode;
    private sba.lib.gui.bean.DBeanFieldText moTextTptFigDriverLicense;
    private sba.lib.gui.bean.DBeanFieldText moTextTptFigFiscalId;
    private sba.lib.gui.bean.DBeanFieldText moTextTptFigForeignId;
    private sba.lib.gui.bean.DBeanFieldText moTextTptFigMail;
    private sba.lib.gui.bean.DBeanFieldText moTextTptFigName;
    private sba.lib.gui.bean.DBeanFieldText moTextTruckCargoInsurance;
    private sba.lib.gui.bean.DBeanFieldText moTextTruckCargoPolicy;
    private sba.lib.gui.bean.DBeanFieldText moTextTruckCivilInsurance;
    private sba.lib.gui.bean.DBeanFieldText moTextTruckCivilPolicy;
    private sba.lib.gui.bean.DBeanFieldText moTextTruckCode;
    private sba.lib.gui.bean.DBeanFieldText moTextTruckEnvironmentInsurance;
    private sba.lib.gui.bean.DBeanFieldText moTextTruckEnvironmentPolicy;
    private sba.lib.gui.bean.DBeanFieldText moTextTruckName;
    private sba.lib.gui.bean.DBeanFieldText moTextTruckPermissionNumber;
    private sba.lib.gui.bean.DBeanFieldText moTextTruckPlate;
    private sba.lib.gui.bean.DBeanFieldText moTextTruckTrailPlate;
    private sba.lib.gui.bean.DBeanFieldCalendarYear moYearTruckModel;
    // End of variables declaration//GEN-END:variables

    /*
     * General private methods
     */

    private void initComponentsCustom() {
        DGuiUtils.setWindowBounds(this, 1024, 640);
        
        // BOL:
        
        moKeyBolSeries.setKeySettings(miClient, DGuiUtils.getLabelName(jlBolNumber), false); // this combo box lacks of selection-indicative item
        moDateBolDate.setDateSettings(miClient, DGuiUtils.getLabelName(jlBolDate), true);
        moBoolBolIntlTransport.setBooleanSettings(moBoolBolIntlTransport.getName(), false);
        moBoolBolIsthmus.setBooleanSettings(moBoolBolIsthmus.getName(), false);
        moKeyBolIsthmusOrigin.setKeySettings(miClient, DGuiUtils.getLabelName(jlBolIsthmusOrigin), true);
        moKeyBolIsthmusDestiny.setKeySettings(miClient, DGuiUtils.getLabelName(jlBolIsthmusDestiny), true);
        moBoolBolTemplate.setBooleanSettings(moBoolBolTemplate.getText(), false);
        moTextBolTemplateName.setTextSettings(DGuiUtils.getLabelName(jlBolTemplateName), 50);
        moTextBolTemplateCode.setTextSettings(DGuiUtils.getLabelName(jlBolTemplateCode), 5);
        
        moFields.addField(moKeyBolSeries);
        moFields.addField(moDateBolDate);
        moFields.addField(moBoolBolIntlTransport);
        moFields.addField(moBoolBolIsthmus);
        moFields.addField(moKeyBolIsthmusOrigin);
        moFields.addField(moKeyBolIsthmusDestiny);
        moFields.addField(moBoolBolTemplate);
        moFields.addField(moTextBolTemplateName);
        moFields.addField(moTextBolTemplateCode);
        moFields.setFormButton(jbBolNavStart);
        
        // locations:
        
        moKeyLocLocationType.setKeySettings(miClient, DGuiUtils.getLabelName(jlLocLocationType), true);
        moTextLocName.setTextSettings(DGuiUtils.getLabelName(jlLocName), 50);
        moTextLocCode.setTextSettings(DGuiUtils.getLabelName(jlLocCode), 6, 6);
        moDatetimeLocArrivalDepartureDatetime.setDateSettings(miClient, DGuiUtils.getLabelName(jlLocArrivalDepartureDatetime), true);
        moKeyLocSourceLocation.setKeySettings(miClient, DGuiUtils.getLabelName(jlLocSourceLocation), true);
        moDecLocDistanceKm.setDecimalSettings(DGuiUtils.getLabelName(jlLocDistanceKm), DGuiConsts.GUI_TYPE_DEC, true);
        moDecLocDistanceKm.setDecimalFormat(DLibUtils.DecimalFormatValue2D);
        moBoolLocUpdate.setBooleanSettings(moBoolLocUpdate.getText(), false);
        
        moKeyLocAddressCountry.setKeySettings(miClient, DGuiUtils.getLabelName(jlLocAddressCountry), true);
        moTextLocAddressStateCode.setTextSettings(DGuiUtils.getLabelName(jlLocAddressState) + ": clave", 3);
        moTextLocAddressStateName.setTextSettings(DGuiUtils.getLabelName(jlLocAddressState) + ": " + moTextLocAddressStateName.getToolTipText(), 30);
        moTextLocAddressCountyCode.setTextSettings(DGuiUtils.getLabelName(jlLocAddressCounty) + ": clave", 3, 0); 
        moTextLocAddressCountyName.setTextSettings(DGuiUtils.getLabelName(jlLocAddressCounty) + ": " + moTextLocAddressCountyName.getToolTipText(), 120, 0);
        moTextLocAddressLocalityCode.setTextSettings(DGuiUtils.getLabelName(jlLocAddressLocality) + ": clave", 3, 0);
        moTextLocAddressLocalityName.setTextSettings(DGuiUtils.getLabelName(jlLocAddressLocality) + ": " + moTextLocAddressLocalityName.getToolTipText(), 120, 0);
        moTextLocAddressZipCode.setTextSettings(DGuiUtils.getLabelName(jlLocAddressZipCode), 12, 5);
        moTextLocAddressDistrictCode.setTextSettings(DGuiUtils.getLabelName(jlLocAddressDistrict) + ": clave", 4, 0);
        moTextLocAddressDistrictName.setTextSettings(DGuiUtils.getLabelName(jlLocAddressDistrict) + ": " + moTextLocAddressDistrictName.getToolTipText(), 30, 0);
        moTextLocAddressStreet.setTextSettings(DGuiUtils.getLabelName(jlLocAddressStreet), 100); // street is mandatory for location
        moTextLocAddressNumberExt.setTextSettings(DGuiUtils.getLabelName(jlLocAddressStreet) + ": " + moTextLocAddressNumberExt.getToolTipText(), 25, 0);
        moTextLocAddressNumberInt.setTextSettings(DGuiUtils.getLabelName(jlLocAddressStreet) + ": " + moTextLocAddressNumberInt.getToolTipText(), 25, 0);
        moTextLocAddressReference.setTextSettings(DGuiUtils.getLabelName(jlLocAddressReference), 120, 0);
        
        moTextLocNotes.setTextSettings(DGuiUtils.getLabelName(jlLocNotes), 100, 0);
        
        moFieldsLocation = new DGuiFields(jtpWizard);
        
        moFieldsLocation.addField(moKeyLocLocationType, TAB_IDX_LOCATION);
        moFieldsLocation.addField(moTextLocName, TAB_IDX_LOCATION);
        moFieldsLocation.addField(moTextLocCode, TAB_IDX_LOCATION);
        moFieldsLocation.addField(moDatetimeLocArrivalDepartureDatetime, TAB_IDX_LOCATION);
        moFieldsLocation.addField(moKeyLocSourceLocation, TAB_IDX_LOCATION);
        moFieldsLocation.addField(moDecLocDistanceKm, TAB_IDX_LOCATION);
        moFieldsLocation.addField(moBoolLocUpdate, TAB_IDX_LOCATION);
        
        moFieldsLocation.addField(moKeyLocAddressCountry, TAB_IDX_LOCATION);
        moFieldsLocation.addField(moTextLocAddressStateCode, TAB_IDX_LOCATION);
        moFieldsLocation.addField(moTextLocAddressStateName, TAB_IDX_LOCATION);
        moFieldsLocation.addField(moTextLocAddressCountyCode, TAB_IDX_LOCATION);
        moFieldsLocation.addField(moTextLocAddressCountyName, TAB_IDX_LOCATION);
        moFieldsLocation.addField(moTextLocAddressLocalityCode, TAB_IDX_LOCATION);
        moFieldsLocation.addField(moTextLocAddressLocalityName, TAB_IDX_LOCATION);
        moFieldsLocation.addField(moTextLocAddressZipCode, TAB_IDX_LOCATION);
        moFieldsLocation.addField(moTextLocAddressDistrictCode, TAB_IDX_LOCATION);
        moFieldsLocation.addField(moTextLocAddressDistrictName, TAB_IDX_LOCATION);
        moFieldsLocation.addField(moTextLocAddressStreet, TAB_IDX_LOCATION);
        moFieldsLocation.addField(moTextLocAddressNumberExt, TAB_IDX_LOCATION);
        moFieldsLocation.addField(moTextLocAddressNumberInt, TAB_IDX_LOCATION);
        moFieldsLocation.addField(moTextLocAddressReference, TAB_IDX_LOCATION);
        
        moFieldsLocation.addField(moTextLocNotes, TAB_IDX_LOCATION);
        
        moFieldsLocation.setFormButton(jbLocOk);
        
        // merchandises:
        
        moKeyMerchItem.setKeySettings(miClient, DGuiUtils.getLabelName(jlMerchItem), true);
        moTextMerchItemDescription.setTextSettings(DGuiUtils.getLabelName(jlMerchItemDescription), 512);
        moKeyMerchUnit.setKeySettings(miClient, DGuiUtils.getLabelName(jlMerchUnit), true);
        moTextMerchUnitDescription.setTextSettings(DGuiUtils.getLabelName(jlMerchUnitDescription), 20);
        moDecMerchQuantity.setDecimalSettings(DGuiUtils.getLabelName(jlMerchQuantity), DGuiConsts.GUI_TYPE_DEC, true);
        moDecMerchQuantity.setDecimalFormat(DLibUtils.DecimalFormatValue6D);
        moDecMerchWeightKg.setDecimalSettings(DGuiUtils.getLabelName(jlMerchWeightKg), DGuiConsts.GUI_TYPE_DEC_QTY, true);
        moDecMerchWeightKg.setDecimalFormat(DLibUtils.DecimalFormatValue3D);
        moIntMerchDimensionsLength.setIntegerSettings(DGuiUtils.getLabelName(jlMerchDimensions) + ": " + moIntMerchDimensionsLength.getToolTipText(), DGuiConsts.GUI_TYPE_INT, false);
        moIntMerchDimensionsLength.setMaxInteger(999);
        moIntMerchDimensionsHeight.setIntegerSettings(DGuiUtils.getLabelName(jlMerchDimensions) + ": " + moIntMerchDimensionsHeight.getToolTipText(), DGuiConsts.GUI_TYPE_INT, false);
        moIntMerchDimensionsHeight.setMaxInteger(999);
        moIntMerchDimensionsWidth.setIntegerSettings(DGuiUtils.getLabelName(jlMerchDimensions) + ": " + moIntMerchDimensionsWidth.getToolTipText(), DGuiConsts.GUI_TYPE_INT, false);
        moIntMerchDimensionsWidth.setMaxInteger(999);
        moRadMerchDimensionsCm.setBooleanSettings(moRadMerchDimensionsCm.getText(), false);
        moRadMerchDimensionsPlg.setBooleanSettings(moRadMerchDimensionsPlg.getText(), false);
        
        moKeyMerchCurrency.setKeySettings(miClient, DGuiUtils.getLabelName(jlMerchCurrency), false);
        moCurMerchValue.setCompoundFieldSettings(miClient);
        moCurMerchValue.getField().setDecimalSettings(DGuiUtils.getLabelName(jlMerchValue), DGuiConsts.GUI_TYPE_DEC_AMT, false);
        moBoolMerchHazardousMaterial.setBooleanSettings(moBoolMerchHazardousMaterial.getText(), false);
        moRadMerchHazardousMaterialYes.setBooleanSettings(moRadMerchHazardousMaterialYes.getText(), true);
        moRadMerchHazardousMaterialNo.setBooleanSettings(moRadMerchHazardousMaterialNo.getText(), false);
        moTextMerchHazardousMaterialCode.setTextSettings(DGuiUtils.getLabelName(jlMerchHazardousMaterial) + ": clave", 4, 0);
        moTextMerchHazardousMaterialName.setTextSettings(DGuiUtils.getLabelName(jlMerchHazardousMaterial) + ": " + moTextMerchHazardousMaterialName.getToolTipText(), 100, 0);
        moTextMerchPackagingCode.setTextSettings(DGuiUtils.getLabelName(jlMerchPackaging) + ": clave", 4, 0);
        moTextMerchPackagingName.setTextSettings(DGuiUtils.getLabelName(jlMerchPackaging) + ": " + moTextMerchPackagingName.getToolTipText(), 100, 0);
        moTextMerchTariff.setTextSettings(DGuiUtils.getLabelName(jlMerchTariff), 10);
        moIntMerchImportRequest1.setIntegerSettings(DGuiUtils.getLabelName(jlMerchImportRequest) + ": " + moIntMerchImportRequest1.getToolTipText(), DGuiConsts.GUI_TYPE_INT_RAW, true);
        moIntMerchImportRequest1.setIntegerFormat(DFormBolUtils.FormatSegmentImportRequest1);
        moIntMerchImportRequest1.setMaxInteger(99); // 2 digits!
        moIntMerchImportRequest2.setIntegerSettings(DGuiUtils.getLabelName(jlMerchImportRequest) + ": " + moIntMerchImportRequest2.getToolTipText(), DGuiConsts.GUI_TYPE_INT_RAW, true);
        moIntMerchImportRequest2.setIntegerFormat(DFormBolUtils.FormatSegmentImportRequest2);
        moIntMerchImportRequest2.setMaxInteger(99); // 2 digits!
        moIntMerchImportRequest3.setIntegerSettings(DGuiUtils.getLabelName(jlMerchImportRequest) + ": " + moIntMerchImportRequest3.getToolTipText(), DGuiConsts.GUI_TYPE_INT_RAW, true);
        moIntMerchImportRequest3.setIntegerFormat(DFormBolUtils.FormatSegmentImportRequest3);
        moIntMerchImportRequest3.setMaxInteger(9999); // 4 digits!
        moIntMerchImportRequest4.setIntegerSettings(DGuiUtils.getLabelName(jlMerchImportRequest) + ": " + moIntMerchImportRequest4.getToolTipText(), DGuiConsts.GUI_TYPE_INT_RAW, true);
        moIntMerchImportRequest4.setIntegerFormat(DFormBolUtils.FormatSegmentImportRequest4);
        moIntMerchImportRequest4.setMaxInteger(9999999); // 7 digits!
        
        moKeyMerchMoveSource.setKeySettings(miClient, DGuiUtils.getLabelName(jlMerchMoveSource), true);
        moKeyMerchMoveDestiny.setKeySettings(miClient, DGuiUtils.getLabelName(jlMerchMoveDestiny), true);
        moDecMerchMoveQuantity.setDecimalSettings(DGuiUtils.getLabelName(jlMerchMoveQuantity), DGuiConsts.GUI_TYPE_DEC_QTY, true);
        moDecMerchMoveQuantity.setDecimalFormat(DLibUtils.DecimalFormatValue6D);
        
        moFieldsMerchandise = new DGuiFields(jtpWizard);
        
        moFieldsMerchandise.addField(moKeyMerchItem, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moTextMerchItemDescription, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moKeyMerchUnit, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moTextMerchUnitDescription, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moDecMerchQuantity, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moDecMerchWeightKg, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moIntMerchDimensionsLength, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moIntMerchDimensionsHeight, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moIntMerchDimensionsWidth, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moRadMerchDimensionsCm, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moRadMerchDimensionsPlg, TAB_IDX_MERCHANDISE);
        
        moFieldsMerchandise.addField(moKeyMerchCurrency, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moCurMerchValue.getField(), TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moBoolMerchHazardousMaterial, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moRadMerchHazardousMaterialYes, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moRadMerchHazardousMaterialNo, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moTextMerchHazardousMaterialCode, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moTextMerchHazardousMaterialName, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moTextMerchPackagingCode, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moTextMerchPackagingName, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moTextMerchTariff, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moIntMerchImportRequest1, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moIntMerchImportRequest2, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moIntMerchImportRequest3, TAB_IDX_MERCHANDISE);
        moFieldsMerchandise.addField(moIntMerchImportRequest4, TAB_IDX_MERCHANDISE);
        
        moFieldsMerchandise.setFormButton(jbMerchOk);
        
        moFieldsMerchandiseMove = new DGuiFields(jtpWizard);
        
        moFieldsMerchandiseMove.addField(moKeyMerchMoveSource, TAB_IDX_MERCHANDISE);
        moFieldsMerchandiseMove.addField(moKeyMerchMoveDestiny, TAB_IDX_MERCHANDISE);
        moFieldsMerchandiseMove.addField(moDecMerchMoveQuantity, TAB_IDX_MERCHANDISE);
        
        moFieldsMerchandiseMove.setFormButton(jbMerchMoveOk);
        
        // truck:
        
        moKeyTruckTransportConfig.setKeySettings(miClient, DGuiUtils.getLabelName(jlTruckTransportConfig), true);
        moTextTruckName.setTextSettings(DGuiUtils.getLabelName(jlTruckName), 100);
        moTextTruckCode.setTextSettings(DGuiUtils.getLabelName(jlTruckCode), 10);
        moDecTruckWeightTon.setDecimalSettings(DGuiUtils.getLabelName(jlTruckWeightTon), DGuiConsts.GUI_TYPE_DEC_AMT, true);
        moDecTruckWeightGrossTon.setDecimalSettings(DGuiUtils.getLabelName(jlTruckWeightGrossTon), DGuiConsts.GUI_TYPE_DEC_AMT, true);
        moTextTruckPlate.setTextSettings(DGuiUtils.getLabelName(jlTruckPlate), 7);
        moYearTruckModel.setCalendarSettings(DGuiUtils.getLabelName(jlTruckModel));
        moKeyTruckPermissionType.setKeySettings(miClient, DGuiUtils.getLabelName(jlTruckPermissionType), true);
        moTextTruckPermissionNumber.setTextSettings(DGuiUtils.getLabelName(jlTruckPermissionNumber), 50);
        moBoolTruckUpdate.setBooleanSettings(moBoolTruckUpdate.getText(), false);
        
        moTextTruckCivilInsurance.setTextSettings(DGuiUtils.getLabelName(jlTruckCivilInsurance), 50);
        moTextTruckCivilPolicy.setTextSettings(DGuiUtils.getLabelName(jlTruckCivilPolicy), 30);
        moTextTruckEnvironmentInsurance.setTextSettings(DGuiUtils.getLabelName(jlTruckEnvironmentInsurance), 50, 0);
        moTextTruckEnvironmentPolicy.setTextSettings(DGuiUtils.getLabelName(jlTruckEnvironmentPolicy), 30, 0);
        moTextTruckCargoInsurance.setTextSettings(DGuiUtils.getLabelName(jlTruckCargoInsurance), 50, 0);
        moTextTruckCargoPolicy.setTextSettings(DGuiUtils.getLabelName(jlTruckCargoPolicy), 30, 0);
        moCurTruckPrime.setCompoundFieldSettings(miClient);
        moCurTruckPrime.getField().setDecimalSettings(DGuiUtils.getLabelName(jlTruckPrime), DGuiConsts.GUI_TYPE_DEC_AMT, false);
        
        moFieldsTruck = new DGuiFields(jtpWizard);
        
        moFieldsTruck.addField(moKeyTruckTransportConfig, TAB_IDX_TRUCK);
        moFieldsTruck.addField(moTextTruckName, TAB_IDX_TRUCK);
        moFieldsTruck.addField(moTextTruckCode, TAB_IDX_TRUCK);
        moFieldsTruck.addField(moDecTruckWeightTon, TAB_IDX_TRUCK);
        moFieldsTruck.addField(moDecTruckWeightGrossTon, TAB_IDX_TRUCK);
        moFieldsTruck.addField(moTextTruckPlate, TAB_IDX_TRUCK);
        moFieldsTruck.addField(moYearTruckModel, TAB_IDX_TRUCK);
        moFieldsTruck.addField(moKeyTruckPermissionType, TAB_IDX_TRUCK);
        moFieldsTruck.addField(moTextTruckPermissionNumber, TAB_IDX_TRUCK);
        moFieldsTruck.addField(moBoolTruckUpdate, TAB_IDX_TRUCK);
        
        moFieldsTruck.addField(moTextTruckCivilInsurance, TAB_IDX_TRUCK);
        moFieldsTruck.addField(moTextTruckCivilPolicy, TAB_IDX_TRUCK);
        moFieldsTruck.addField(moTextTruckEnvironmentInsurance, TAB_IDX_TRUCK);
        moFieldsTruck.addField(moTextTruckEnvironmentPolicy, TAB_IDX_TRUCK);
        moFieldsTruck.addField(moTextTruckCargoInsurance, TAB_IDX_TRUCK);
        moFieldsTruck.addField(moTextTruckCargoPolicy, TAB_IDX_TRUCK);
        moFieldsTruck.addField(moCurTruckPrime.getField(), TAB_IDX_TRUCK);
        
        moFieldsTruck.setFormButton(jbTruckOk);
        
        moKeyTruckTrailSubtype.setKeySettings(miClient, DGuiUtils.getLabelName(jlTruckTrailSubtype), true);
        moTextTruckTrailPlate.setTextSettings(DGuiUtils.getLabelName(jlTruckTrailPlate), 7);
        moBoolTruckTrailUpdate.setBooleanSettings(moBoolTruckTrailUpdate.getText(), false);
        
        moFieldsTruckTrailer = new DGuiFields(jtpWizard);
        
        moFieldsTruckTrailer.addField(moKeyTruckTrailSubtype, TAB_IDX_TRUCK);
        moFieldsTruckTrailer.addField(moTextTruckTrailPlate, TAB_IDX_TRUCK);
        moFieldsTruckTrailer.addField(moBoolTruckTrailUpdate, TAB_IDX_TRUCK);
        
        moFieldsTruckTrailer.setFormButton(jbTruckTrailOk);
        
        // transport figures:
        
        moKeyTptFigTransportFigureType.setKeySettings(miClient, DGuiUtils.getLabelName(jlTptFigTransportFigureType), true);
        moTextTptFigName.setTextSettings(DGuiUtils.getLabelName(jlTptFigName), 200);
        moTextTptFigCode.setTextSettings(DGuiUtils.getLabelName(jlTptFigCode), 10);
        moTextTptFigMail.setTextSettings(DGuiUtils.getLabelName(jlTptFigMail), 100, 0);
        moTextTptFigMail.setTextCaseType(0);
        moTextTptFigFiscalId.setTextSettings(DGuiUtils.getLabelName(jlTptFigFiscalId), 14, 13);
        moTextTptFigDriverLicense.setTextSettings(DGuiUtils.getLabelName(jlTptFigDriverLicense), 16);
        moKeyTptFigFigureCountry.setKeySettings(miClient, DGuiUtils.getLabelName(jlTptFigFigureCountry), false);
        moTextTptFigForeignId.setTextSettings(DGuiUtils.getLabelName(jlTptFigForeignId), 16);
        moBoolTptFigUpdate.setBooleanSettings(moBoolTptFigUpdate.getText(), false);

        moKeyTptFigAddressCountry.setKeySettings(miClient, DGuiUtils.getLabelName(jlTptFigAddressCountry), true);
        moTextTptFigAddressStateCode.setTextSettings(DGuiUtils.getLabelName(jlTptFigAddressState) + ": clave", 3);
        moTextTptFigAddressStateName.setTextSettings(DGuiUtils.getLabelName(jlTptFigAddressState) + ": " + moTextTptFigAddressStateName.getToolTipText(), 30);
        moTextTptFigAddressCountyCode.setTextSettings(DGuiUtils.getLabelName(jlTptFigAddressCounty) + ": clave", 3, 0); 
        moTextTptFigAddressCountyName.setTextSettings(DGuiUtils.getLabelName(jlTptFigAddressCounty) + ": " + moTextTptFigAddressCountyName.getToolTipText(), 120, 0);
        moTextTptFigAddressLocalityCode.setTextSettings(DGuiUtils.getLabelName(jlTptFigAddressLocality) + ": clave", 3, 0);
        moTextTptFigAddressLocalityName.setTextSettings(DGuiUtils.getLabelName(jlTptFigAddressLocality) + ": " + moTextTptFigAddressLocalityName.getToolTipText(), 120, 0);
        moTextTptFigAddressZipCode.setTextSettings(DGuiUtils.getLabelName(jlTptFigAddressZipCode), 12, 5);
        moTextTptFigAddressDistrictCode.setTextSettings(DGuiUtils.getLabelName(jlTptFigAddressDistrict) + ": clave", 4, 0);
        moTextTptFigAddressDistrictName.setTextSettings(DGuiUtils.getLabelName(jlTptFigAddressDistrict) + ": " + moTextTptFigAddressDistrictName.getToolTipText(), 30, 0);
        moTextTptFigAddressStreet.setTextSettings(DGuiUtils.getLabelName(jlTptFigAddressStreet), 100, 0); // street is not mandatory for transport figure
        moTextTptFigAddressNumberExt.setTextSettings(DGuiUtils.getLabelName(jlTptFigAddressStreet) + ": " + moTextTptFigAddressNumberExt.getToolTipText(), 25, 0);
        moTextTptFigAddressNumberInt.setTextSettings(DGuiUtils.getLabelName(jlTptFigAddressStreet) + ": " + moTextTptFigAddressNumberInt.getToolTipText(), 25, 0);
        moTextTptFigAddressReference.setTextSettings(DGuiUtils.getLabelName(jlTptFigAddressReference), 120, 0);
        moTextTptFigAddressReference.setTextSettings(DGuiUtils.getLabelName(jlTptFigAddressReference), 100, 0);

        moFieldsTptFigure = new DGuiFields();
        
        moFieldsTptFigure.addField(moKeyTptFigTransportFigureType, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moTextTptFigName, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moTextTptFigCode, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moTextTptFigMail, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moTextTptFigFiscalId, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moTextTptFigDriverLicense, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moKeyTptFigFigureCountry, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moTextTptFigForeignId, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moBoolTptFigUpdate, TAB_IDX_TPT_FIGURE);
        
        moFieldsTptFigure.addField(moKeyTptFigAddressCountry, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moTextTptFigAddressStateCode, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moTextTptFigAddressStateName, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moTextTptFigAddressCountyCode, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moTextTptFigAddressCountyName, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moTextTptFigAddressLocalityCode, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moTextTptFigAddressLocalityName, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moTextTptFigAddressZipCode, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moTextTptFigAddressDistrictCode, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moTextTptFigAddressDistrictName, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moTextTptFigAddressStreet, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moTextTptFigAddressNumberExt, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moTextTptFigAddressNumberInt, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigure.addField(moTextTptFigAddressReference, TAB_IDX_TPT_FIGURE);
        
        moFieldsTptFigure.setFormButton(jbTptFigOk);
        
        moKeyTptFigTptPartTransportPartType.setKeySettings(miClient, DGuiUtils.getLabelName(jlTptFigTptPartTransportPartType), true);
        moBoolTptFigTptPartUpdate.setBooleanSettings(moBoolTptFigTptPartUpdate.getText(), false);
        
        moFieldsTptFigureTptPart = new DGuiFields();
        
        moFieldsTptFigureTptPart.addField(moKeyTptFigTptPartTransportPartType, TAB_IDX_TPT_FIGURE);
        moFieldsTptFigureTptPart.addField(moBoolTptFigTptPartUpdate, TAB_IDX_TPT_FIGURE);
        
        moFieldsTptFigureTptPart.setFormButton(jbTptFigTptPartOk);
        
        try {
            msTransportTypeTruckCode = (String) miClient.getSession().readField(DModConsts.LS_TPT_TP, new int[] { DModSysConsts.LS_TPT_TP_TRUCK }, DDbRegistry.FIELD_CODE);
            
            moXmlBolIsthmusRegistry = new DXmlCatalog(DCfdi40Catalogs.XML_CCP_REG_IST, "xml/" + DCfdi40Catalogs.XML_CCP_REG_IST + DXmlCatalog.XmlFileExt, false);
            moXmlBolIsthmusRegistry.populateCatalog(moKeyBolIsthmusOrigin);
            moXmlBolIsthmusRegistry.populateCatalog(moKeyBolIsthmusDestiny);
            
            moXmlTruckTransportConfig = new DXmlCatalog(DCfdi40Catalogs.XML_CCP_CFG_AUTO, "xml/" + DCfdi40Catalogs.XML_CCP_CFG_AUTO + DXmlCatalog.XmlFileExt, false, "", "", new String[] { "trailer" });
            moXmlTruckTransportConfig.populateCatalog(moKeyTruckTransportConfig);
            
            moXmlTruckPermissionType = new DXmlCatalog(DCfdi40Catalogs.XML_CCP_PERM_TP, "xml/" + DCfdi40Catalogs.XML_CCP_PERM_TP + DXmlCatalog.XmlFileExt, false);
            moXmlTruckPermissionType.populateCatalog(moKeyTruckPermissionType);
            
            moXmlTruckTrailerSubtype = new DXmlCatalog(DCfdi40Catalogs.XML_CCP_REM_STP, "xml/" + DCfdi40Catalogs.XML_CCP_REM_STP + DXmlCatalog.XmlFileExt, false);
            moXmlTruckTrailerSubtype.populateCatalog((moKeyTruckTrailSubtype));
            
            mnBolWeightUnitId = DFormBolUtils.getWeightUnitId(miClient.getSession());
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
        
        moGridLocations = DFormBolUtils.createGridLocations(miClient, this);
        jpWizardLoc.add(moGridLocations, BorderLayout.CENTER);
        
        moGridMerchandises = DFormBolUtils.createGridMerchandises(miClient, this);
        jpWizardMerch.add(moGridMerchandises, BorderLayout.CENTER);
        
        moGridMerchandisesMoves = DFormBolUtils.createGridMerchandisesMoves(miClient, this);
        jpMerchMoveGrid.add(moGridMerchandisesMoves, BorderLayout.CENTER);
        
        moGridTrucks = DFormBolUtils.createGridTrucks(miClient, this);
        jpWizardTruck.add(moGridTrucks, BorderLayout.CENTER);
        
        moGridTrucksTrailers = DFormBolUtils.createGridTrucksTrailers(miClient, this);
        jpTruckTrailGrid.add(moGridTrucksTrailers, BorderLayout.CENTER);
        
        moGridTptFigures = DFormBolUtils.createGridTptFigures(miClient, this);
        jpWizardTptFigure.add(moGridTptFigures, BorderLayout.CENTER);
        
        moGridTptFiguresTptParts = DFormBolUtils.createGridTptFiguresTptParts(miClient, this);
        jpTptFigTptPartGrid.add(moGridTptFiguresTptParts, BorderLayout.CENTER);
    }

    private void actionGridRemove(final DGridPaneForm grid, final Method renderGridRows) throws Exception {
        if (grid.getSelectedGridRow() == null) {
            miClient.showMsgBoxWarning(DGridConsts.MSG_SELECT_ROW);
        }
        else {
            if (miClient.showMsgBoxConfirm(DGridConsts.MSG_CONFIRM_REG_DEL) == JOptionPane.YES_OPTION) {
                int index = grid.getTable().getSelectedRow();
                grid.getModel().getGridRows().remove(grid.getTable().convertRowIndexToModel(index));

                renderGridRows.invoke(this);

                if (index < grid.getTable().getRowCount()) {
                    grid.setSelectedGridRow(index);
                }
                else {
                    grid.setSelectedGridRow(grid.getTable().getRowCount() - 1);
                }
            }
        }
    }

    private void actionGridMoveUp(final DGridPaneForm grid, final Method renderGridRows) throws Exception {
        if (grid.getSelectedGridRow() == null) {
            miClient.showMsgBoxWarning(DGridConsts.MSG_SELECT_ROW);
        }
        else {
            int index = grid.getTable().getSelectedRow();
            if (index > 0) {
                DGridRow row = grid.removeGridRow(index);
                grid.insertGridRow(row, --index);
                
                renderGridRows.invoke(this);
                grid.setSelectedGridRow(index);
            }
        }
    }

    private void actionGridMoveDown(final DGridPaneForm grid, final Method renderGridRows) throws Exception {
        if (grid.getSelectedGridRow() == null) {
            miClient.showMsgBoxWarning(DGridConsts.MSG_SELECT_ROW);
        }
        else {
            int index = grid.getTable().getSelectedRow();
            if (index + 1 < grid.getTable().getRowCount()) {
                DGridRow row = grid.removeGridRow(index);
                grid.insertGridRow(row, ++index);
                
                renderGridRows.invoke(this);
                grid.setSelectedGridRow(index);
            }
        }
    }
    
    private String getCrudText(final int action) {
        String crudText = "";
        
        switch (action) {
            case ACTION_ADD:
                crudText = TXT_ACTION_ADD;
                break;
            case ACTION_CREATE:
                crudText = TXT_ACTION_CREATE;
                break;
            case ACTION_COPY:
                crudText = TXT_ACTION_COPY;
                break;
            case ACTION_MODIFY:
                crudText = TXT_ACTION_MODIFY;
                break;
            default:
                crudText = TXT_UNKNOWN;
        }
        
        return crudText;
    }
    
    private int countLocations(final int locationType) {
        int count = 0;
        
        for (DGridRow row : moGridLocations.getModel().getGridRows()) {
            DDbBolLocation bolLocation = (DDbBolLocation) row;
            if (bolLocation.getFkLocationTypeId() == locationType) {
                count++;
            }
        }
        
        return count;
    }
    
    /*
     * Protected methods
     */

    @Override
    protected void windowActivated() {
        if (mbForceCancel) {
            actionCancel();
        }
        else {
            super.windowActivated();
        }
    }
    
    /*
     * BOL
     */
    
    private boolean isBolTemplate() {
        return mnFormSubtype == DDbBol.BOL_TEMPLATE;
    }

    private void computeBol() {
        jtfBolDeparturelDatetime.setText("");
        jtfBolDepartureLocation.setText("");

        jtfBolArrivalDatetime.setText("");
        jtfBolArrivalLocation.setText("");
        
        if (!moGridLocations.getModel().getGridRows().isEmpty()) {
            DDbBolLocation bolLocationFirst = (DDbBolLocation) moGridLocations.getModel().getGridRows().firstElement();
            if (bolLocationFirst.isLocationSource()) {
                jtfBolDeparturelDatetime.setText(DLibUtils.DateFormatDatetime.format(bolLocationFirst.getArrivalDepartureDatetime()));
                jtfBolDeparturelDatetime.setCaretPosition(0);
                jtfBolDepartureLocation.setText(bolLocationFirst.getOwnLocation().getCode());
                jtfBolDepartureLocation.setCaretPosition(0);
            }
            else {
                jtfBolDeparturelDatetime.setText("¡Primer ubicación no es origen!");
                jtfBolDeparturelDatetime.setCaretPosition(0);
            }

            if (moGridLocations.getModel().getGridRows().size() > 1) {
                DDbBolLocation bolLocationLast = (DDbBolLocation) moGridLocations.getModel().getGridRows().lastElement();
                if (bolLocationLast.isLocationDestiny()) {
                    jtfBolArrivalDatetime.setText(DLibUtils.DateFormatDatetime.format(bolLocationLast.getArrivalDepartureDatetime()));
                    jtfBolArrivalDatetime.setCaretPosition(0);
                    jtfBolArrivalLocation.setText(bolLocationLast.getOwnLocation().getCode());
                    jtfBolArrivalLocation.setCaretPosition(0);
                }
                else {
                    jtfBolArrivalDatetime.setText("¡Última ubicación no es destino!");
                    jtfBolArrivalDatetime.setCaretPosition(0);
                }
            }
        }
        
        mdBolDistanceKm = 0;
        for (DGridRow row : moGridLocations.getModel().getGridRows()) {
            DDbBolLocation bolLocation = (DDbBolLocation) row;
            if (bolLocation.isLocationDestiny()) {
                mdBolDistanceKm += bolLocation.getDistanceKm();
            }
        }
        jtfBolDistanceKm.setText(DLibUtils.DecimalFormatValue2D.format(mdBolDistanceKm));
        jtfBolDistanceKm.setCaretPosition(0);
        
        mdBolMerchandiseWeightKg = 0;
        for (DGridRow row : moGridMerchandises.getModel().getGridRows()) {
            DDbBolMerchandise bolMerchandise = (DDbBolMerchandise) row;
            mdBolMerchandiseWeightKg += bolMerchandise.getWeightKg();
        }
        jtfBolMerchandiseWeightKg.setText(DLibUtils.DecimalFormatValue3D.format(mdBolMerchandiseWeightKg));
        jtfBolMerchandiseWeightKg.setCaretPosition(0);
        
        mnBolMerchandiseNumber = moGridMerchandises.getModel().getRowCount();
        jtfBolMerchandiseNumber.setText(DLibUtils.DecimalFormatInteger.format(mnBolMerchandiseNumber));
    }

    private void clearBolContent() {
        mnTempBolLocationId = 0;
        
        if (moGridLocations != null) {
            moGridLocations.clearGridRows();
        }
        if (moGridMerchandises != null) {
            moGridMerchandises.clearGridRows();
        }
        if (moGridMerchandisesMoves != null) {
            moGridMerchandisesMoves.clearGridRows();
        }
        if (moGridTrucks != null) {
            moGridTrucks.clearGridRows();
        }
        if (moGridTrucksTrailers != null) {
            moGridTrucksTrailers.clearGridRows();
        }
        if (moGridTptFigures != null) {
            moGridTptFigures.clearGridRows();
        }
        if (moGridTptFiguresTptParts != null) {
            moGridTptFiguresTptParts.clearGridRows();
        }
    }
    
    private void showBolIntlTransport() {
        String bolIntlTransport = "";
        
        if (moBolIntlTransport != null) {
            if (moBolIntlTransport.IntlTransportDirection.isEmpty()) {
                bolIntlTransport += "ND";
            }
            else {
                bolIntlTransport += moBolIntlTransport.IntlTransportDirection;
                bolIntlTransport += "/ " + miClient.getSession().readField(DModConsts.CS_CTY, new int[] { moBolIntlTransport.IntlTransportCountryId }, DDbRegistry.FIELD_NAME);
                bolIntlTransport += "/ " + miClient.getSession().readField(DModConsts.LS_TPT_TP, new int[] { moBolIntlTransport.IntlWayTransportTypeId }, DDbRegistry.FIELD_NAME);
            }
        }
        
        jtfBolIntlTransport.setText(bolIntlTransport);
        jtfBolIntlTransport.setCaretPosition(0);
    }

    private void itemStateChangedBolIntlTransport() {
        if (moBoolBolIntlTransport.isSelected()) {
            jbBolSetIntlTransport.setEnabled(true);
        }
        else {
            jbBolSetIntlTransport.setEnabled(false);
            moBolIntlTransport = null;
        }
        
        showBolIntlTransport();
    }
    
    private void itemStateChangedBolIsthmus() {
        if (moBoolBolIsthmus.isSelected()) {
            moKeyBolIsthmusOrigin.setEnabled(true);
            moKeyBolIsthmusDestiny.setEnabled(true);
        }
        else {
            moKeyBolIsthmusOrigin.setEnabled(false);
            moKeyBolIsthmusDestiny.setEnabled(false);
            
            moKeyBolIsthmusOrigin.resetField();
            moKeyBolIsthmusDestiny.resetField();
        }
    }

    private void itemStateChangedBolTemplate() {
        if (moBoolBolTemplate.isSelected()) {
            moTextBolTemplateName.setEnabled(true);
            moTextBolTemplateCode.setEnabled(true);
        }
        else {
            moTextBolTemplateName.setEnabled(false);
            moTextBolTemplateCode.setEnabled(false);
            
            moTextBolTemplateName.resetField();
            moTextBolTemplateCode.resetField();
        }
    }

    /*
     * Navigation
     */

    private boolean canNavStart() {
        DGuiValidation validation = moFields.validateFields();
        
        if (validation.isValid()) {
            if (moBoolBolIntlTransport.getValue() && moBolIntlTransport == null) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + jtfBolIntlTransport.getToolTipText() + "'.");
                validation.setComponent(jbBolSetIntlTransport);
            }
        }
        
        return DGuiUtils.computeValidation(miClient, validation);
    }

    private boolean canNavRestart() {
        return true;
    }

    private boolean canNavPrev() {
        return jtpWizard.getSelectedIndex() > 0;
    }

    private boolean canNavNext() {
        boolean can = jtpWizard.getSelectedIndex() + 1 < jtpWizard.getTabCount();
        
        if (can) {
            DGuiValidation validation = new DGuiValidation();
            
            if (isBolTemplate()) {
                switch (jtpWizard.getSelectedIndex()) {
                    case TAB_IDX_LOCATION:
                        // not required for templates
                        break;
                    case TAB_IDX_MERCHANDISE:
                        // not required for templates
                        break;
                    case TAB_IDX_TRUCK:
                        if (moGridTrucks.getTable().getRowCount() > 1) {
                            validation.setMessage("Sólo puede haber un autotransporte.");
                            validation.setComponent(jbTruckAdd);
                        }
                        break;
                    case TAB_IDX_TPT_FIGURE:
                        // not required for templates
                        break;
                    default:
                        // nothing
                }
            }
            else {
                switch (jtpWizard.getSelectedIndex()) {
                    case TAB_IDX_LOCATION:
                        if (countLocations(DModSysConsts.LS_LOC_TP_SRC) == 0) {
                            validation.setMessage("Debe haber al menos una ubicación origen.");
                            validation.setComponent(jbLocAdd);
                        }
                        else if (countLocations(DModSysConsts.LS_LOC_TP_DES) == 0) {
                            validation.setMessage("Debe haber al menos una ubicación destino.");
                            validation.setComponent(jbLocAdd);
                        }
                        break;
                    case TAB_IDX_MERCHANDISE:
                        if (moGridMerchandises.getTable().getRowCount() < 1) {
                            validation.setMessage("Debe haber al menos una mercancía.");
                            validation.setComponent(jbMerchCreate); // adding not supported
                        }
                        break;
                    case TAB_IDX_TRUCK:
                        if (moGridTrucks.getTable().getRowCount() != 1) {
                            validation.setMessage("Sólo puede haber un autotransporte.");
                            validation.setComponent(jbTruckAdd);
                        }
                        break;
                    case TAB_IDX_TPT_FIGURE:
                        if (moGridTptFigures.getTable().getRowCount() < 1) {
                            validation.setMessage("Debe haber al menos una figura de transporte.");
                            validation.setComponent(jbTptFigAdd);
                        }
                        break;
                    default:
                        // nothing
                }
            }
            
            can = DGuiUtils.computeValidation(miClient, validation);
        }
        
        return can;
    }
    
    private boolean isSaveAllowed() {
        return moBol.isRegistryNew() || (!moBol.isSystem() && (moBol.getChildDfr() == null || moBol.getChildDfr().getFkXmlStatusId() <= DModSysConsts.TS_XML_ST_PEN));
    }

    private void enableBolControls(final boolean start) {
        // BOL:
        
        jbBolUpdateVersion.setEnabled(!start && isSaveAllowed() && !jtfBolVersion.getText().equals(cfd.ver4.ccp31.DElementCartaPorte.VERSION));
        moKeyBolSeries.setEnabled(!start);
        moDateBolDate.setEnabled(!isBolTemplate());
        
        moBoolBolIntlTransport.setEnabled(!start);
        jbBolSetIntlTransport.setEnabled(!start && moBoolBolIntlTransport.getValue());
        moBoolBolIsthmus.setEnabled(!start);
        moKeyBolIsthmusOrigin.setEnabled(!start && moBoolBolIsthmus.getValue());
        moKeyBolIsthmusDestiny.setEnabled(!start && moBoolBolIsthmus.getValue());
        
        moBoolBolMerchandiseInverseLogistics.setEnabled(!start);
        
        moBoolBolTemplate.setEnabled(false); // selection defined by form subtype
        moTextBolTemplateName.setEnabled(!start && moBoolBolTemplate.getValue());
        moTextBolTemplateCode.setEnabled(!start && moBoolBolTemplate.getValue());
        
        // locations:
        
        if (!start) {
            editBolLocation(false, 0);
        }
        
        jbLocAdd.setEnabled(start);
        jbLocCreate.setEnabled(start);
        jbLocCopy.setEnabled(start);
        jbLocModify.setEnabled(start);
        jbLocRemove.setEnabled(start);
        jbLocMoveUp.setEnabled(start);
        jbLocMoveDown.setEnabled(start);
        jbLocOk.setEnabled(false);
        jbLocCancel.setEnabled(false);
        
        // merchandises:
        
        if (!start) {
            editBolMerchandise(false, 0);
        }
        
        jbMerchAdd.setEnabled(false); // adding not required
        jbMerchCreate.setEnabled(start);
        jbMerchCopy.setEnabled(start);
        jbMerchModify.setEnabled(start);
        jbMerchRemove.setEnabled(start);
        jbMerchMoveUp.setEnabled(start);
        jbMerchMoveDown.setEnabled(start);
        jbMerchOk.setEnabled(false);
        jbMerchCancel.setEnabled(false);
        
        // truck:
        
        if (!start) {
            editBolTruck(false, 0);
        }
        
        jbTruckAdd.setEnabled(start);
        jbTruckCreate.setEnabled(start);
        jbTruckCopy.setEnabled(start);
        jbTruckModify.setEnabled(start);
        jbTruckRemove.setEnabled(start);
        jbTruckMoveUp.setEnabled(start);
        jbTruckMoveDown.setEnabled(start);
        jbTruckOk.setEnabled(false);
        jbTruckCancel.setEnabled(false);
        
        // transport figures:
        
        if (!start) {
            editBolTptFigure(false, 0);
        }
        
        jbTptFigAdd.setEnabled(start);
        jbTptFigCreate.setEnabled(start);
        jbTptFigCopy.setEnabled(start);
        jbTptFigModify.setEnabled(start);
        jbTptFigRemove.setEnabled(start);
        jbTptFigMoveUp.setEnabled(start);
        jbTptFigMoveDown.setEnabled(start);
        jbTptFigOk.setEnabled(false);
        jbTptFigCancel.setEnabled(false);
    }

    private void enableBolNavButtons(final int navAction) {
        boolean enableNavButtons = false;
        
        switch (navAction) {
            case NAV_ACTION_START:
            case NAV_ACTION_PREV:
            case NAV_ACTION_NEXT:
                // start and/or ready to restart:
                jbBolNavStart.setEnabled(false);
                jbBolNavRestart.setEnabled(true);
                enableNavButtons = true;
                break;
            case NAV_ACTION_RESTART:
                // restart and ready to start:
                jbBolNavStart.setEnabled(true);
                jbBolNavRestart.setEnabled(false);
                break;
            default:
                // unknown action:
                jbBolNavStart.setEnabled(false);
                jbBolNavRestart.setEnabled(false);
        }
        
        jbBolNavPrev.setEnabled(enableNavButtons && jtpWizard.getSelectedIndex() > 0);
        jbBolNavNext.setEnabled(enableNavButtons && jtpWizard.getSelectedIndex() + 1 < jtpWizard.getTabCount());
    }

    private void enableWizardTab(final int index, final int navAction) {
        if (index >= 0 && index < jtpWizard.getTabCount()) {
            // prepare tab:
            
            switch (index) {
                case TAB_IDX_LOCATION:
                    break;
                case TAB_IDX_MERCHANDISE:
                    valueChangedMerchMove(); // populates comboboxes of source and destiny locations
                    break;
                case TAB_IDX_TRUCK:
                    break;
                case TAB_IDX_TPT_FIGURE:
                    break;
                default:
                    // nothing
            }
            
            // enable tab:
            
            jtpWizard.setSelectedIndex(index);
            
            for (int tab = 0; tab < jtpWizard.getTabCount(); tab++) {
                jtpWizard.setEnabledAt(tab, tab == index && navAction != NAV_ACTION_RESTART);
            }
            
            enableBolNavButtons(navAction);
            
            // enable save button:
            
            jbSave.setEnabled((isBolTemplate() || (index + 1 == jtpWizard.getTabCount())) && isSaveAllowed());
        }
    }
    
    private void actionPerformedBolUpdateVersion() {
        jtfBolVersion.setText(cfd.ver4.ccp31.DElementCartaPorte.VERSION);
        jtfBolVersion.setCaretPosition(0);
    }

    private void actionPerformedBolNavStart() {
        if (canNavStart()) {
            enableWizardTab(0, NAV_ACTION_START);
            enableBolControls(true);
            
            mbEditionStarted = true;
            
            jbLocAdd.requestFocusInWindow();
        }
    }

    private void actionPerformedBolNavRestart(final boolean clearBolContent) {
        if (canNavRestart() && (!clearBolContent || miClient.showMsgBoxConfirm("Si reinicia, toda la información capturada será eliminada.\n" + DGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION)) {
            enableWizardTab(0, NAV_ACTION_RESTART);
            enableBolControls(false);
            
            mbEditionStarted = false;
            
            if (clearBolContent) {
                clearBolContent();
            }
            
            moDateBolDate.requestFocusInWindow();
        }
    }

    private void actionPerformedBolNavPrev() {
        if (canNavPrev()) {
            enableWizardTab(jtpWizard.getSelectedIndex() - 1, NAV_ACTION_PREV);
        }
    }

    private void actionPerformedBolNavNext() {
        if (canNavNext()) {
            enableWizardTab(jtpWizard.getSelectedIndex() + 1, NAV_ACTION_NEXT);
        }
    }
    
    private void actionPerformedBolSetIntlTransport() {
        DDialogBolIntlTransport dialog = new DDialogBolIntlTransport(miClient, "Transporte internacional");
        
        if (moBolIntlTransport == null) {
            moBolIntlTransport = new DLadBolIntlTransport();
            moBolIntlTransport.IntlTransportDirection = DCfdi40Catalogs.CcpMercancíasEntrada;
        }
        
        dialog.setValue(DModConsts.LX_BOL_INTL_TPT, moBolIntlTransport);
        dialog.setVisible(true);
        
        if (dialog.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
            moBolIntlTransport = (DLadBolIntlTransport) dialog.getValue(DModConsts.LX_BOL_INTL_TPT);
            showBolIntlTransport();
        }
    }

    /*
     * Locations
     */
    
    private DDbBolLocation createBolLocationPickingOne() {
        DDbBolLocation bolLocation = null;
        
        if (moPickerLocation == null) {
            moPickerLocation = new DPickerElement(miClient, DModConsts.LU_LOC);
        }
        
        moPickerLocation.resetForm();
        moPickerLocation.setVisible(true);

        if (moPickerLocation.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
            int[] key = (int[]) moPickerLocation.getValue(DPickerElement.ELEMENT);
            
            if (key != null) {
                bolLocation = new DDbBolLocation();
                bolLocation.setOwnLocation((DDbLocation) miClient.getSession().readRegistry(DModConsts.LU_LOC, key));
                bolLocation.setArrivalDepartureDatetime(new Date());
            }
        }
        
        return bolLocation;
    }

    private DGuiItem createGuiItemForBolLocation(final DDbBolLocation bolLocation) {
        DGuiItem guiItem = new DGuiItem(bolLocation.getPrimaryKey(), bolLocation.getOwnLocation().getName());
        guiItem.setCode(bolLocation.getOwnLocation().getCodeSource());
        guiItem.setComplement(bolLocation.getOwnLocation());
        return guiItem;
    }
    
    private boolean canBolLocationRemove() {
        boolean can = true;
        DDbBolLocation bolLocationSelected = (DDbBolLocation) moGridLocations.getSelectedGridRow();
        
        if (bolLocationSelected != null) {
            if (bolLocationSelected.isLocationSource()) {
                for (DGridRow row : moGridLocations.getModel().getGridRows()) {
                    DDbBolLocation bolLocation = (DDbBolLocation) row;
                    
                    if (bolLocation.isLocationDestiny() && DLibUtils.compareKeys(bolLocation.getSourceBolLocationKey(), bolLocationSelected.getPrimaryKey())) {
                        miClient.showMsgBoxWarning("La ubicación seleccionada '" + bolLocationSelected.getBolSortingPos() + ". " + bolLocationSelected.getRowName() + "' "
                                + "se usa en la ubicación '" + bolLocation.getBolSortingPos() + ". " + bolLocation.getRowName() + "'.");
                        can = false;
                        break;
                    }
                }
            }
            
            if (can) {
                merchandises:
                for (DGridRow row : moGridMerchandises.getModel().getGridRows()) {
                    DDbBolMerchandise bolMerchandise = (DDbBolMerchandise) row;
                    
                    for (DDbBolMerchandiseMove bolMerchandiseMove : bolMerchandise.getChildMoves()) {
                        if (bolLocationSelected.isLocationSource() && DLibUtils.compareKeys(bolMerchandiseMove.getSourceLocationKey(), bolLocationSelected.getPrimaryKey())) {
                            miClient.showMsgBoxWarning("La ubicación seleccionada '" + bolLocationSelected.getBolSortingPos() + ". " + bolLocationSelected.getRowName() + "' "
                                    + "se usa como origen al transportar la mercancía '" + bolMerchandise.getBolSortingPos() + ". " + bolMerchandise.getDescriptionItem() + "'.");
                            can = false;
                            break merchandises;
                        }
                        else if (bolLocationSelected.isLocationDestiny()&& DLibUtils.compareKeys(bolMerchandiseMove.getDestinyLocationKey(), bolLocationSelected.getPrimaryKey())) {
                            miClient.showMsgBoxWarning("La ubicación seleccionada '" + bolLocationSelected.getBolSortingPos() + ". " + bolLocationSelected.getRowName() + "' "
                                    + "se usa como destino al transportar la mercancía '" + bolMerchandise.getBolSortingPos() + ". " + bolMerchandise.getDescriptionItem() + "'.");
                            can = false;
                            break merchandises;
                        }
                    }
                }
            }
        }
        
        return can;
    }
    
    private void renderBolLocation(final DDbBolLocation bolLocation) {
        moBolLocation = bolLocation;
        
        if (moBolLocation == null) {
            moFieldsLocation.resetFields();
            
            moTextLocCode.setValue(DFormBolUtils.DEF_CODE_LOCATION);
            
            jtfLocLocationId.setText("");
            
            jtfLocPk.setText("");
            
            moBoolLocUpdate.setValue(true);
        }
        else {
            moKeyLocLocationType.setValue(new int[] { moBolLocation.getFkLocationTypeId() });
            itemStateChangedLocLocationType();
            moTextLocName.setValue(moBolLocation.getOwnLocation().getName());
            moTextLocCode.setValue(moBolLocation.getOwnLocation().getCode());
            moDatetimeLocArrivalDepartureDatetime.setValue(moBolLocation.getArrivalDepartureDatetime());
            moKeyLocSourceLocation.setValue(moBolLocation.getSourceBolLocationKey());
            itemStateChangedLocSourceLocation();
            moDecLocDistanceKm.setValue(moBolLocation.getDistanceKm());
            
            jtfLocLocationId.setText(moBolLocation.getLocationId());
            jtfLocLocationId.setCaretPosition(0);
            
            moKeyLocAddressCountry.setValue(new int[] { moBolLocation.getFkAddressCountryId() });
            itemStateChangedLocAddressCountry();
            moTextLocAddressStateCode.setValue(moBolLocation.getAddressStateCode());
            moTextLocAddressStateName.setValue(moBolLocation.getAddressStateName());
            moTextLocAddressCountyCode.setValue(moBolLocation.getAddressCountyCode());
            moTextLocAddressCountyName.setValue(moBolLocation.getAddressCountyName());
            moTextLocAddressLocalityCode.setValue(moBolLocation.getAddressLocalityCode());
            moTextLocAddressLocalityName.setValue(moBolLocation.getAddressLocalityName());
            moTextLocAddressZipCode.setValue(moBolLocation.getAddressZipCode());
            moTextLocAddressDistrictCode.setValue(moBolLocation.getAddressDistrictCode());
            moTextLocAddressDistrictName.setValue(moBolLocation.getAddressDistrictName());
            moTextLocAddressStreet.setValue(moBolLocation.getAddressStreet());
            moTextLocAddressNumberExt.setValue(moBolLocation.getAddressNumberExt());
            moTextLocAddressNumberInt.setValue(moBolLocation.getAddressNumberInt());
            moTextLocAddressReference.setValue(moBolLocation.getAddressReference());
            
            moTextLocNotes.setValue(moBolLocation.getNotes());
            
            jtfLocPk.setText(DLibUtils.textKey(moBolLocation.getPrimaryKey()));
            jtfLocPk.setCaretPosition(0);
            
            moBoolLocUpdate.setValue(moBolLocation.getOwnLocation() == null || moBolLocation.getOwnLocation().isRegistryNew()); // update only when requested
        }
    }
    
    private void editBolLocation(final boolean editing, final int action) {
        mbEditingLocation = editing;
        
        // CRUD action:
        
        if (!mbEditingLocation) {
            mnActionLocation = 0;
            jtfLocCrud.setText("");
        }
        else {
            mnActionLocation = action;
            jtfLocCrud.setText(getCrudText(action));
            jtfLocCrud.setCaretPosition(0);
        }
        
        // status of fields and input controls:
        
        moKeyLocLocationType.setEnabled(mbEditingLocation);
        jbLocEditType.setEnabled(false);
        itemStateChangedLocLocationType();
        moTextLocName.setEnabled(mbEditingLocation);
        moTextLocCode.setEnabled(mbEditingLocation);
        jbLocGetNextCode.setEnabled(mbEditingLocation);
        moDatetimeLocArrivalDepartureDatetime.setEnabled(mbEditingLocation);
        //moKeyLocSourceLocation.setEnabled(...); // depends on itemStateChangedLocLocationType()
        itemStateChangedLocSourceLocation();
        //moDecLocDistanceKm.setEnabled(...); // depends on itemStateChangedLocSourceLocation()
        
        moKeyLocAddressCountry.setEnabled(mbEditingLocation);
        itemStateChangedLocAddressCountry();
        //moTextLocAddressStateCode.setEnabled(...); // depends on itemStateChangedLocAddressCountry()
        //moTextLocAddressStateName.setEnabled(...); // depends on itemStateChangedLocAddressCountry()
        //moTextLocAddressCountyCode.setEnabled(...); // depends on itemStateChangedLocAddressCountry()
        //moTextLocAddressCountyName.setEnabled(...); // depends on itemStateChangedLocAddressCountry()
        //moTextLocAddressLocalityCode.setEnabled(...); // depends on itemStateChangedLocAddressCountry()
        //moTextLocAddressLocalityName.setEnabled(...); // depends on itemStateChangedLocAddressCountry()
        //moTextLocAddressZipCode.setEnabled(mbEditingLocation); // depends on itemStateChangedLocAddressCountry()
        //moTextLocAddressDistrictCode.setEnabled(...); // depends on itemStateChangedLocAddressCountry()
        //moTextLocAddressDistrictName.setEnabled(...); // depends on itemStateChangedLocAddressCountry()
        //moTextLocAddressStreet.setEnabled(mbEditingLocation); // depends on itemStateChangedLocAddressCountry()
        //moTextLocAddressNumberExt.setEnabled(mbEditingLocation); // depends on itemStateChangedLocAddressCountry()
        //moTextLocAddressNumberInt.setEnabled(mbEditingLocation); // depends on itemStateChangedLocAddressCountry()
        //moTextLocAddressReference.setEnabled(mbEditingLocation); // depends on itemStateChangedLocAddressCountry()
        
        moTextLocNotes.setEnabled(mbEditingLocation);
        
        // status of CRUD controls:
        
        jbLocAdd.setEnabled(!mbEditingLocation);
        jbLocCreate.setEnabled(!mbEditingLocation);
        jbLocCopy.setEnabled(!mbEditingLocation);
        jbLocModify.setEnabled(!mbEditingLocation);
        jbLocRemove.setEnabled(!mbEditingLocation);
        jbLocMoveUp.setEnabled(!mbEditingLocation);
        jbLocMoveDown.setEnabled(!mbEditingLocation);

        jbLocOk.setEnabled(mbEditingLocation);
        jbLocCancel.setEnabled(mbEditingLocation);
        
        moGridLocations.getTable().setEnabled(!mbEditingLocation);
        moGridLocations.getTable().getTableHeader().setEnabled(!mbEditingLocation);
        
        // clear or refresh data:
        
        switch (action) { // act on original parameter
            case ACTION_ADD:
                renderBolLocation(moBolLocation); // local registry already set
                // default values:
                if (moKeyLocSourceLocation.isEnabled() && moKeyLocSourceLocation.getSelectedIndex() <= 0 && moKeyLocSourceLocation.getItemCount() == 2) {
                    moKeyLocSourceLocation.setSelectedIndex(1);
                }
                break;
            case ACTION_CREATE:
                renderBolLocation(null);
                break;
            case ACTION_COPY:
            case ACTION_MODIFY:
            case ACTION_CANCEL:
                valueChangedLoc(); // renders current grid row
                break;
            default:
                // nothing
        }
        
        // set edition status appropriately into form:
        
        boolean updatable = mbEditingLocation && moBolLocation != null && moBolLocation.getOwnLocation() != null && !moBolLocation.getOwnLocation().isRegistryNew();
        
        moBoolLocUpdate.setEnabled(updatable);
        
        if (updatable) {
            moKeyLocLocationType.setEnabled(false);
            jbLocEditType.setEnabled(true);
        }
        
        if (action != 0) { // check original parameter
            enableBolNavButtons(mbEditingLocation ? 0 : NAV_ACTION_START);
            
            if (mbEditingLocation) {
                if (moKeyLocLocationType.isEnabled()) {
                    moKeyLocLocationType.requestFocusInWindow();
                }
                else {
                    moTextLocName.requestFocusInWindow();
                }
            }
            else {
                jbLocAdd.requestFocusInWindow();
            }
        }
    }
    
    /**
     * Render grid rows. It is needed public in order to be invoked by reflection.
     */
    public void refreshBolLocations() {
        int sortingPos = 0;
        
        for (DGridRow row : moGridLocations.getModel().getGridRows()) {
            ((DDbBolLocation) row).setBolSortingPos(++sortingPos);
        }
        
        moGridLocations.renderGridRows();
    }
    
    private void computeLocCode() {
        // format location code:
        moTextLocCode.setValue(DFormBolUtils.FormatCodeLocation.format(DLibUtils.parseInt(moTextLocCode.getValue())));
        
        // compose location ID:
        if (moKeyLocLocationType.getSelectedIndex() <= 0) {
            jtfLocLocationId.setText("");
        }
        else {
            jtfLocLocationId.setText(moKeyLocLocationType.getSelectedItem().getCode() + moTextLocCode.getValue());
            jtfLocLocationId.setCaretPosition(0);
        }
    }
    
    private void computeLocAddressStateCode() {
        DFormBolUtils.computeCatalogCode(moTextLocAddressStateCode, moTextLocAddressStateName, DFormBolUtils.DEF_CODE_ADDRESS_STATE, 
                null, DCfdi40Catalogs.XML_CCP_EDO_40, false, moKeyLocAddressCountry, DFormBolUtils.ATT_COUNTRY);
    }
    
    private void computeLocAddressCountyCode() {
        DFormBolUtils.computeCatalogCode(moTextLocAddressCountyCode, moTextLocAddressCountyName, DFormBolUtils.DEF_CODE_ADDRESS_COUNTY, 
                DFormBolUtils.FormatCodeAddressCounty, DCfdi40Catalogs.XML_CCP_MUN, false, moTextLocAddressStateCode, DFormBolUtils.ATT_STATE);
    }

    private void computeLocAddressLocalityCode() {
        DFormBolUtils.computeCatalogCode(moTextLocAddressLocalityCode, moTextLocAddressLocalityName, DFormBolUtils.DEF_CODE_ADDRESS_LOCALITY, 
                DFormBolUtils.FormatCodeAddressLocality, DCfdi40Catalogs.XML_CCP_LOC, false, moTextLocAddressStateCode, DFormBolUtils.ATT_STATE);
    }

    private void computeLocAddressDistrictCode() {
        DFormBolUtils.computeCatalogCode(moTextLocAddressDistrictCode, moTextLocAddressDistrictName, DFormBolUtils.DEF_CODE_ADDRESS_DISTRICT, 
                DFormBolUtils.FormatCodeAddressDistrict, DCfdi40Catalogs.XML_CCP_COL, true, moTextLocAddressZipCode, DFormBolUtils.ATT_ZIP);
    }

    @SuppressWarnings("unchecked")
    private void populateLocSourceLocation() {
        moKeyLocSourceLocation.removeAllItems();
        
        if (moKeyLocLocationType.getValue()[0] == DModSysConsts.LS_LOC_TP_DES) {
            moKeyLocSourceLocation.addItem(new DGuiItem("- " + DLibUtils.textProperCase(DGuiUtils.getLabelName(jlLocSourceLocation)) + " -"));
            
            for (DGridRow row : moGridLocations.getModel().getGridRows()) {
                DDbBolLocation bolLocation = (DDbBolLocation) row;
                
                if (bolLocation.isLocationSource()) {
                    moKeyLocSourceLocation.addItem(createGuiItemForBolLocation(bolLocation));
                }
            }
        }
    }
    
    private void actionPerformedLocEditType() {
        if (mbEditingLocation && miClient.showMsgBoxConfirm("¿Está seguro que desea cambiar el tipo de la ubicación?") == JOptionPane.YES_OPTION) {
            jbLocEditType.setEnabled(false);
            moKeyLocLocationType.setEnabled(true);
            moKeyLocLocationType.requestFocusInWindow();
        }
    }
    
    private void actionPerformedLocGetNextCode() {
        try {
            if (moTextLocCode.getValue().equals(DFormBolUtils.DEF_CODE_LOCATION) || 
                    miClient.showMsgBoxConfirm("¿Está seguro que desea obtener el siguiente código para el campo '" + DGuiUtils.getLabelName(jlLocCode) + "'?") == JOptionPane.YES_OPTION) {
                int nextCode = DFormBolUtils.getNextCode(miClient.getSession(), DModConsts.LU_LOC, moKeyLocLocationType.getValue()[0]);
                moTextLocCode.setValue("" + nextCode);
                moTextLocCode.requestFocusInWindow();
                computeLocCode();
            }
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedLocAdd() {
        DDbBolLocation bolLocation = createBolLocationPickingOne();
        
        if (bolLocation != null) {
            moBolLocation = bolLocation;
            editBolLocation(true, ACTION_ADD);
        }
    }
    
    private void actionPerformedLocCreate() {
        editBolLocation(true, ACTION_CREATE);
        
        moDatetimeLocArrivalDepartureDatetime.setValue(new Date());
    }

    private void actionPerformedLocCopy() {
        if (moGridLocations.getSelectedGridRow() == null) {
            miClient.showMsgBoxWarning(DGridConsts.MSG_SELECT_ROW);
        }
        else {
            editBolLocation(true, ACTION_COPY);
        }
    }

    private void actionPerformedLocModify() {
        if (moGridLocations.getSelectedGridRow() == null) {
            miClient.showMsgBoxWarning(DGridConsts.MSG_SELECT_ROW);
        }
        else {
            editBolLocation(true, ACTION_MODIFY);
        }
    }

    private void actionPerformedLocRemove() {
        if (canBolLocationRemove()) {
            try {
                actionGridRemove(moGridLocations, this.getClass().getMethod("refreshBolLocations"));
                computeBol();
            }
            catch (Exception e) {
                DLibUtils.showException(this, e);
            }
        }
    }

    private void actionPerformedLocMoveUp() {
        try {
            actionGridMoveUp(moGridLocations, this.getClass().getMethod("refreshBolLocations"));
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }

    private void actionPerformedLocMoveDown() {
        try {
            actionGridMoveDown(moGridLocations, this.getClass().getMethod("refreshBolLocations"));
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }

    private void actionPerformedLocOk() {
        DGuiValidation validation = moFieldsLocation.validateFields();
        
        if (validation.isValid()) {
            if (moTextLocCode.getValue().equals(DFormBolUtils.DEF_CODE_LOCATION)) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moTextLocCode.getFieldName() + "'.");
                validation.setComponent(moTextLocCode);
            }
            else if (moTextLocAddressStateCode.isEnabled() && moTextLocAddressStateCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_STATE)) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moTextLocAddressStateCode.getFieldName() + "'.");
                validation.setComponent(moTextLocAddressStateCode);
            }
        }
        
        if (DGuiUtils.computeValidation(miClient, validation)) {
            boolean adding = mnActionLocation == ACTION_ADD; // convenience variable
            boolean creating = mnActionLocation == ACTION_CREATE || mnActionLocation == ACTION_COPY; // convenience variable
            DDbLocation location = null;
            
            if (creating) {
                moBolLocation = new DDbBolLocation();
                location = new DDbLocation();
                moBolLocation.setOwnLocation(location);
            }
            
            location = moBolLocation.getOwnLocation(); // convenience variable
            
            //location.setPkLocationId(...);
            location.setCode(moTextLocCode.getValue());
            //location.setCodeSource(...);
            //location.setCodeDestiny(...);
            location.setName(moTextLocName.getValue());
            location.setAddressStreet(moTextLocAddressStreet.getValue());
            location.setAddressNumberExt(moTextLocAddressNumberExt.getValue());
            location.setAddressNumberInt(moTextLocAddressNumberInt.getValue());
            location.setAddressDistrictCode(moTextLocAddressDistrictCode.getValue());
            location.setAddressDistrictName(moTextLocAddressDistrictName.getValue());
            location.setAddressLocalityCode(moTextLocAddressLocalityCode.getValue());
            location.setAddressLocalityName(moTextLocAddressLocalityName.getValue());
            location.setAddressReference(moTextLocAddressReference.getValue());
            location.setAddressCountyCode(moTextLocAddressCountyCode.getValue());
            location.setAddressCountyName(moTextLocAddressCountyName.getValue());
            location.setAddressStateCode(moTextLocAddressStateCode.getValue());
            location.setAddressStateName(moTextLocAddressStateName.getValue());
            location.setAddressZipCode(moTextLocAddressZipCode.getValue());
            //location.setUpdatable(...);
            //location.setDisableable(...);
            //location.setDeletable(...);
            //location.setDisabled(...);
            //location.setDeleted(...);
            //location.setSystem(...);
            location.setFkLocationTypeId(moKeyLocLocationType.getValue()[0]);
            location.setFkAddressCountryId(moKeyLocAddressCountry.getValue()[0]);
            //location.setFkUserInsertId(...);
            //location.setFkUserUpdateId(...);
            //location.setTsUserInsert(...);
            //location.setTsUserUpdate(...);

            location.setDbmsLocationTypeName(moKeyLocLocationType.getSelectedItem().getItem());
            location.setDbmsLocationTypeCode(moKeyLocLocationType.getSelectedItem().getCode());
            
            location.sanitize();
            
            location.setRegistryEdited(true);
            
            moBolLocation.updateFromOwnLocation();
            
            //moBolLocation.setPkBolId(...);
            if (creating || adding) {
                moBolLocation.setTempBolLocationId(++mnTempBolLocationId); // preserve temporal BOL location ID
                moBolLocation.setPkLocationId(mnTempBolLocationId);
            }
            //registry.setLocationId(...); // updated already by DDbBolLocation.updateFromOwnLocation()
            moBolLocation.setArrivalDepartureDatetime(moDatetimeLocArrivalDepartureDatetime.getValue());
            moBolLocation.setDistanceKm(moDecLocDistanceKm.getValue());
            //moBolLocation.setAddressStreet(...); // updated already by DDbBolLocation.updateFromOwnLocation()
            //moBolLocation.setAddressNumberExt(...); // updated already by DDbBolLocation.updateFromOwnLocation()
            //moBolLocation.setAddressNumberInt(...); // updated already by DDbBolLocation.updateFromOwnLocation()
            //moBolLocation.setAddressDistrictCode(...); // updated already by DDbBolLocation.updateFromOwnLocation()
            //moBolLocation.setAddressDistrictName(...); // updated already by DDbBolLocation.updateFromOwnLocation()
            //moBolLocation.setAddressLocalityCode(...); // updated already by DDbBolLocation.updateFromOwnLocation()
            //moBolLocation.setAddressLocalityName(...); // updated already by DDbBolLocation.updateFromOwnLocation()
            //moBolLocation.setAddressReference(...); // updated already by DDbBolLocation.updateFromOwnLocation()
            //moBolLocation.setAddressCountyCode(...); // updated already by DDbBolLocation.updateFromOwnLocation()
            //moBolLocation.setAddressCountyName(...); // updated already by DDbBolLocation.updateFromOwnLocation()
            //moBolLocation.setAddressStateCode(...); // updated already by DDbBolLocation.updateFromOwnLocation()
            //moBolLocation.setAddressStateName(...); // updated already by DDbBolLocation.updateFromOwnLocation()
            //moBolLocation.setAddressZipCode(...); // updated already by DDbBolLocation.updateFromOwnLocation()
            moBolLocation.setNotes(moTextLocNotes.getValue());
            //moBolLocation.setFkLocationTypeId(...); // updated already by DDbBolLocation.updateFromOwnLocation()
            //moBolLocation.setFkLocationId(...); // updated already by DDbBolLocation.updateFromOwnLocation()
            //moBolLocation.setFkAddressCountryId(...); // updated already by DDbBolLocation.updateFromOwnLocation()
            DGuiItem guiItem = moKeyLocSourceLocation.getSelectedItem();
            moBolLocation.setFkSourceBolId_n(guiItem == null ? 0 : guiItem.getPrimaryKey()[0]);
            moBolLocation.setFkSourceLocationId_n(guiItem == null ? 0 : guiItem.getPrimaryKey()[1]);

            //moBolLocation.setOwnLocation(...); // already set above
            moBolLocation.setOwnSourceLocation(guiItem == null ? null : (DDbLocation) guiItem.getComplement());

            //registry.setTempBolLocationId(...); // already set above
            moBolLocation.setTempSourceBolLocationId(guiItem == null ? 0 : guiItem.getPrimaryKey()[1]); // preserve temporary ID of just created BOL locations
            
            // update own registry:
            
            moBolLocation.setBolUpdateOwnRegistry(moBoolLocUpdate.getValue());
            
            // update grid:

            if (creating || adding) {
                moGridLocations.addGridRow(moBolLocation);
            }
            
            int row = moGridLocations.getModel().getGridRows().indexOf(moBolLocation); // get row before refreshing grid!
            refreshBolLocations();
            moGridLocations.setSelectedGridRow(moGridLocations.getTable().convertRowIndexToView(row));
            
            actionPerformedLocCancel();
            
            computeBol();
        }
    }

    private void actionPerformedLocCancel() {
        editBolLocation(false, ACTION_CANCEL);
    }
    
    private void itemStateChangedLocLocationType() {
        moKeyLocSourceLocation.resetField();
        
        if (moKeyLocLocationType.getSelectedIndex() <= 0) {
            moKeyLocSourceLocation.setEnabled(false);
            
            moKeyLocSourceLocation.removeAllItems();
        }
        else {
            boolean enable = mbEditingLocation && moKeyLocLocationType.getValue()[0] == DModSysConsts.LS_LOC_TP_DES;
            
            moKeyLocSourceLocation.setEnabled(enable);
            
            populateLocSourceLocation();
        }
        
        computeLocCode();
        itemStateChangedLocSourceLocation();
    }
    
    private void itemStateChangedLocSourceLocation() {
        moDecLocDistanceKm.resetField();
        
        if (moKeyLocSourceLocation.getSelectedIndex() <= 0) {
            moDecLocDistanceKm.setEnabled(false);
        }
        else {
            boolean enable = mbEditingLocation && moKeyLocSourceLocation.isEnabled();
            
            moDecLocDistanceKm.setEnabled(enable);
            
            if (moBolLocation != null && moBolLocation.getOwnLocation() != null && !moBolLocation.getOwnLocation().isRegistryNew() && moDecLocDistanceKm.isEnabled()) {
                try {
                    moDecLocDistanceKm.setValue(DFormBolUtils.getDistanceKm(miClient.getSession(), (DDbLocation) moKeyLocSourceLocation.getSelectedItem().getComplement(), moBolLocation.getOwnLocation()));
                }
                catch (Exception e) {
                    DLibUtils.showException(this, e);
                }
            }
        }
    }
    
    private void itemStateChangedLocAddressCountry() {
        moTextLocAddressStateCode.resetField();
        moTextLocAddressStateName.resetField();
        moTextLocAddressCountyCode.resetField();
        moTextLocAddressCountyName.resetField();
        moTextLocAddressLocalityCode.resetField();
        moTextLocAddressLocalityName.resetField();
        moTextLocAddressZipCode.resetField();
        moTextLocAddressDistrictCode.resetField();
        moTextLocAddressDistrictName.resetField();
        moTextLocAddressStreet.resetField();
        moTextLocAddressNumberExt.resetField();
        moTextLocAddressNumberInt.resetField();
        moTextLocAddressReference.resetField();
        
        if (moKeyLocAddressCountry.getSelectedIndex() <= 0) {
            moTextLocAddressStateCode.setEnabled(false);
            moTextLocAddressStateName.setEnabled(false);
            moTextLocAddressCountyCode.setEnabled(false);
            moTextLocAddressCountyName.setEnabled(false);
            moTextLocAddressLocalityCode.setEnabled(false);
            moTextLocAddressLocalityName.setEnabled(false);
            moTextLocAddressZipCode.setEnabled(false);
            moTextLocAddressDistrictCode.setEnabled(false);
            moTextLocAddressDistrictName.setEnabled(false);
            moTextLocAddressStreet.setEnabled(false);
            moTextLocAddressNumberExt.setEnabled(false);
            moTextLocAddressNumberInt.setEnabled(false);
            moTextLocAddressReference.setEnabled(false);
        }
        else {
            String countryCode = moKeyLocAddressCountry.getSelectedItem().getCode(); // convenience variable
            boolean applyStateCatalog = DFormBolUtils.applyStateCatalog(countryCode);
            boolean applyAddressCatalogs = DFormBolUtils.applyAddressCatalogs(countryCode);
            
            moTextLocAddressStateCode.setEnabled(mbEditingLocation && applyStateCatalog);
            moTextLocAddressStateName.setEnabled(mbEditingLocation && !applyStateCatalog);
            moTextLocAddressCountyCode.setEnabled(mbEditingLocation && applyAddressCatalogs);
            moTextLocAddressCountyName.setEnabled(mbEditingLocation && !applyAddressCatalogs);
            moTextLocAddressLocalityCode.setEnabled(mbEditingLocation && applyAddressCatalogs);
            moTextLocAddressLocalityName.setEnabled(mbEditingLocation && !applyAddressCatalogs);
            moTextLocAddressZipCode.setEnabled(mbEditingLocation);
            moTextLocAddressDistrictCode.setEnabled(mbEditingLocation && applyAddressCatalogs);
            moTextLocAddressDistrictName.setEnabled(mbEditingLocation && !applyAddressCatalogs);
            moTextLocAddressStreet.setEnabled(mbEditingLocation);
            moTextLocAddressNumberExt.setEnabled(mbEditingLocation);
            moTextLocAddressNumberInt.setEnabled(mbEditingLocation);
            moTextLocAddressReference.setEnabled(mbEditingLocation);
        }
        
        if (moTextLocAddressStateCode.isEnabled()) {
            moTextLocAddressStateCode.setValue(DFormBolUtils.DEF_CODE_ADDRESS_STATE);
        }
        
        if (moTextLocAddressCountyCode.isEnabled()) {
            moTextLocAddressCountyCode.setValue(DFormBolUtils.DEF_CODE_ADDRESS_COUNTY);
        }
        
        if (moTextLocAddressLocalityCode.isEnabled()) {
            moTextLocAddressLocalityCode.setValue(DFormBolUtils.DEF_CODE_ADDRESS_LOCALITY);
        }
        
        if (moTextLocAddressDistrictCode.isEnabled()) {
            moTextLocAddressDistrictCode.setValue(DFormBolUtils.DEF_CODE_ADDRESS_DISTRICT);
        }
    }
    
    private void focusGainedLocCode() {
        if (moTextLocCode.getValue().equals(DFormBolUtils.DEF_CODE_LOCATION)) {
            moTextLocCode.resetField();
        }
    }
    
    private void focusGainedLocAddressStateCode() {
        if (moTextLocAddressStateCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_STATE)) {
            moTextLocAddressStateCode.resetField();
        }
    }
    
    private void focusGainedLocAddressCountyCode() {
        if (moTextLocAddressCountyCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_COUNTY)) {
            moTextLocAddressCountyCode.resetField();
        }
    }
    
    private void focusGainedLocAddressLocalityCode() {
        if (moTextLocAddressLocalityCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_LOCALITY)) {
            moTextLocAddressLocalityCode.resetField();
        }
    }
    
    private void focusGainedLocAddressDistrictCode() {
        if (moTextLocAddressDistrictCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_DISTRICT)) {
            moTextLocAddressDistrictCode.resetField();
        }
    }

    private void focusLostLocCode() {
        computeLocCode();
    }
    
    private void focusLostLocAddressStateCode() {
        computeLocAddressStateCode();
    }

    private void focusLostLocAddressCountyCode() {
        computeLocAddressCountyCode();
    }

    private void focusLostLocAddressLocalityCode() {
        computeLocAddressLocalityCode();
    }

    private void focusLostLocAddressDistrictCode() {
        computeLocAddressDistrictCode();
    }
    
    private void keyReleasedLocAddressStateCode(final KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_F5) {
            DGuiValidation validation = moKeyLocAddressCountry.validateField();
            
            if (DGuiUtils.computeValidation(miClient, validation)) {
                if (moPickerCatalogAddressState == null) {
                    moPickerCatalogAddressState = new DPickerCatalogAddressState(miClient);
                }

                DGuiItem guiItem = moKeyLocAddressCountry.getSelectedItem();
                DLadCatalogAddressCountry country = new DLadCatalogAddressCountry(guiItem.getCode(), guiItem.getItem());
                
                moPickerCatalogAddressState.setValue(DLadCatalogConsts.ADDRESS_COUNTRY, country);
                moPickerCatalogAddressState.resetForm();
                moPickerCatalogAddressState.setVisible(true);
                
                if (moPickerCatalogAddressState.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                    DLadCatalogAddressState state = (DLadCatalogAddressState) moPickerCatalogAddressState.getValue(DLadCatalogConsts.ADDRESS_STATE);
                    if (state != null) {
                        moTextLocAddressStateCode.setValue(state.Code);
                        moTextLocAddressStateName.setValue(state.Name);
                    }
                }
            }
        }
    }

    private void keyReleasedLocAddressCountyCode(final KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_F5) {
            DGuiValidation validation = moKeyLocAddressCountry.validateField();
            
            if (validation.isValid()) {
                if (moTextLocAddressStateCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_STATE)) {
                    validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + DGuiUtils.getLabelName(jlLocAddressState) + "'.");
                    validation.setComponent(moTextLocAddressStateCode);
                }
            }
            
            if (DGuiUtils.computeValidation(miClient, validation)) {
                if (moPickerCatalogAddressCounty == null) {
                    moPickerCatalogAddressCounty = new DPickerCatalogAddressCounty(miClient);
                }

                DGuiItem guiItem = moKeyLocAddressCountry.getSelectedItem();
                DLadCatalogAddressCountry country = new DLadCatalogAddressCountry(guiItem.getCode(), guiItem.getItem());
                DLadCatalogAddressState state = new DLadCatalogAddressState(moTextLocAddressStateCode.getValue(), moTextLocAddressStateName.getValue());
                
                moPickerCatalogAddressCounty.setValue(DLadCatalogConsts.ADDRESS_COUNTRY, country);
                moPickerCatalogAddressCounty.setValue(DLadCatalogConsts.ADDRESS_STATE, state);
                moPickerCatalogAddressCounty.resetForm();
                moPickerCatalogAddressCounty.setVisible(true);
                
                if (moPickerCatalogAddressCounty.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                    DLadCatalogAddressCounty county = (DLadCatalogAddressCounty) moPickerCatalogAddressCounty.getValue(DLadCatalogConsts.ADDRESS_COUNTY);
                    if (county != null) {
                        moTextLocAddressCountyCode.setValue(county.Code);
                        moTextLocAddressCountyName.setValue(county.Name);
                    }
                }
            }
        }
    }

    private void keyReleasedLocAddressLocalityCode(final KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_F5) {
            DGuiValidation validation = moKeyLocAddressCountry.validateField();
            
            if (validation.isValid()) {
                validation = moTextLocAddressStateCode.validateField();
                
                if (validation.isValid()) {
                    if (moTextLocAddressStateCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_STATE)) {
                        validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + DGuiUtils.getLabelName(jlLocAddressState) + "'.");
                        validation.setComponent(moTextLocAddressStateCode);
                    }
                    else if (moTextLocAddressCountyCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_COUNTY)) {
                        validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + DGuiUtils.getLabelName(jlLocAddressCounty) + "'.");
                        validation.setComponent(moTextLocAddressCountyCode);
                    }
                }
            }
            
            if (DGuiUtils.computeValidation(miClient, validation)) {
                if (moPickerCatalogAddressLocality == null) {
                    moPickerCatalogAddressLocality = new DPickerCatalogAddressLocality(miClient);
                }

                DGuiItem guiItem = moKeyLocAddressCountry.getSelectedItem();
                DLadCatalogAddressCountry country = new DLadCatalogAddressCountry(guiItem.getCode(), guiItem.getItem());
                DLadCatalogAddressState state = new DLadCatalogAddressState(moTextLocAddressStateCode.getValue(), moTextLocAddressStateName.getValue());
                DLadCatalogAddressCounty county = new DLadCatalogAddressCounty(moTextLocAddressCountyCode.getValue(), moTextLocAddressCountyName.getValue());
                
                moPickerCatalogAddressLocality.setValue(DLadCatalogConsts.ADDRESS_COUNTRY, country);
                moPickerCatalogAddressLocality.setValue(DLadCatalogConsts.ADDRESS_STATE, state);
                moPickerCatalogAddressLocality.setValue(DLadCatalogConsts.ADDRESS_COUNTY, county);
                moPickerCatalogAddressLocality.resetForm();
                moPickerCatalogAddressLocality.setVisible(true);
                
                if (moPickerCatalogAddressLocality.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                    DLadCatalogAddressLocality locality = (DLadCatalogAddressLocality) moPickerCatalogAddressLocality.getValue(DLadCatalogConsts.ADDRESS_LOCALITY);
                    if (locality != null) {
                        moTextLocAddressLocalityCode.setValue(locality.Code);
                        moTextLocAddressLocalityName.setValue(locality.Name);
                    }
                }
            }
        }
    }

    private void keyReleasedLocAddressDistrictCode(final KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_F5) {
            DGuiValidation validation = moKeyLocAddressCountry.validateField();
            
            if (validation.isValid()) {
                validation = moTextLocAddressStateCode.validateField();
                
                if (validation.isValid()) {
                    if (moTextLocAddressStateCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_STATE)) {
                        validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + DGuiUtils.getLabelName(jlLocAddressState) + "'.");
                        validation.setComponent(moTextLocAddressStateCode);
                    }
                    else if (moTextLocAddressCountyCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_COUNTY)) {
                        validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + DGuiUtils.getLabelName(jlLocAddressCounty) + "'.");
                        validation.setComponent(moTextLocAddressCountyCode);
                    }
                    else if (moTextLocAddressLocalityCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_LOCALITY)) {
                        validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + DGuiUtils.getLabelName(jlLocAddressLocality) + "'.");
                        validation.setComponent(moTextLocAddressLocalityCode);
                    }
                    else {
                        validation = moTextLocAddressZipCode.validateField();
                    }
                }
            }
            
            if (DGuiUtils.computeValidation(miClient, validation)) {
                if (moPickerCatalogAddressDistrict == null) {
                    moPickerCatalogAddressDistrict = new DPickerCatalogAddressDistrict(miClient);
                }

                DGuiItem guiItem = moKeyLocAddressCountry.getSelectedItem();
                DLadCatalogAddressCountry country = new DLadCatalogAddressCountry(guiItem.getCode(), guiItem.getItem());
                DLadCatalogAddressState state = new DLadCatalogAddressState(moTextLocAddressStateCode.getValue(), moTextLocAddressStateName.getValue());
                DLadCatalogAddressCounty county = new DLadCatalogAddressCounty(moTextLocAddressCountyCode.getValue(), moTextLocAddressCountyName.getValue());
                DLadCatalogAddressLocality locality = new DLadCatalogAddressLocality(moTextLocAddressLocalityCode.getValue(), moTextLocAddressLocalityName.getValue());
                
                moPickerCatalogAddressDistrict.setValue(DLadCatalogConsts.ADDRESS_COUNTRY, country);
                moPickerCatalogAddressDistrict.setValue(DLadCatalogConsts.ADDRESS_STATE, state);
                moPickerCatalogAddressDistrict.setValue(DLadCatalogConsts.ADDRESS_COUNTY, county);
                moPickerCatalogAddressDistrict.setValue(DLadCatalogConsts.ADDRESS_LOCALITY, locality);
                moPickerCatalogAddressDistrict.setValue(DLadCatalogConsts.ADDRESS_ZIP_CODE, moTextLocAddressZipCode.getValue());
                moPickerCatalogAddressDistrict.resetForm();
                moPickerCatalogAddressDistrict.setVisible(true);
                
                if (moPickerCatalogAddressDistrict.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                    DLadCatalogAddressDistrict district = (DLadCatalogAddressDistrict) moPickerCatalogAddressDistrict.getValue(DLadCatalogConsts.ADDRESS_DISTRICT);
                    if (district != null) {
                        moTextLocAddressDistrictCode.setValue(district.Code);
                        moTextLocAddressDistrictName.setValue(district.Name);
                    }
                }
            }
        }
    }
    
    private void valueChangedLoc() {
        renderBolLocation((DDbBolLocation) moGridLocations.getSelectedGridRow());
    }

    /*
     * Merchandises
     */
    
    private void renderBolMerchandise(final DDbBolMerchandise bolMerchandise) {
        moBolMerchandise = bolMerchandise;
        moGridMerchandisesMoves.clearGridRows();
        
        if (moBolMerchandise == null) {
            moFieldsMerchandise.resetFields();
            moMerchItem = null;
            moMerchUnit = null;
            
            jtfMerchQuantityMoved.setText(DLibUtils.DecimalFormatValue6D.format(0.0));
        }
        else {
            moKeyMerchItem.setValue(new int[] { moBolMerchandise.getFkItemId()});
            itemStateChangedMerchItem();
            moTextMerchItemDescription.setValue(moBolMerchandise.getDescriptionItem());
            moKeyMerchUnit.setValue(new int[] { moBolMerchandise.getFkUnitId() });
            itemStateChangedMerchUnit();
            moTextMerchUnitDescription.setValue(moBolMerchandise.getDescriptionUnit());
            moDecMerchQuantity.setValue(moBolMerchandise.getQuantity());
            moDecMerchWeightKg.setValue(moBolMerchandise.getWeightKg());
            
            jtfMerchQuantityMoved.setText(DLibUtils.DecimalFormatValue6D.format(moBolMerchandise.getAuxQuantityMoved()));
            jtfMerchQuantityMoved.setCaretPosition(0);
            
            DDbBolMerchandise.Dimensions dimensions = null;
            
            try {
                dimensions = moBolMerchandise.createDimensions();
            }
            catch (Exception e) {
                DLibUtils.printException(this, e);
            }
            
            if (dimensions != null) {
                moIntMerchDimensionsLength.setValue(dimensions.Length);
                moIntMerchDimensionsHeight.setValue(dimensions.Height);
                moIntMerchDimensionsWidth.setValue(dimensions.Width);
                moRadMerchDimensionsCm.setSelected(dimensions.Unit.equals(DCfdi40Catalogs.CcpDimensiónCm));
                moRadMerchDimensionsPlg.setSelected(dimensions.Unit.equals(DCfdi40Catalogs.CcpDimensiónPlg));
            }
            else {
                moIntMerchDimensionsLength.resetField();
                moIntMerchDimensionsHeight.resetField();
                moIntMerchDimensionsWidth.resetField();
                bgMerchDimensionUnits.clearSelection();
            }
            
            moKeyMerchCurrency.setValue(new int[] { moBolMerchandise.getFkCurrencyId() });
            itemStateChangedMerchCurrency();
            moCurMerchValue.getField().setValue(moBolMerchandise.getValue());
            moBoolMerchHazardousMaterial.setValue(moBolMerchandise.isHazardousMaterial());
            itemStateChangedMerchHazardousMaterial();
            moRadMerchHazardousMaterialYes.setValue(moBolMerchandise.isHazardousMaterialYes());
            moRadMerchHazardousMaterialNo.setValue(moBolMerchandise.isHazardousMaterialNo());
            itemStateChangedMerchHazardousMaterialYesNo();
            moTextMerchHazardousMaterialCode.setValue(moBolMerchandise.getHazardousMaterialCode());
            moTextMerchHazardousMaterialName.setValue(moBolMerchandise.getHazardousMaterialName());
            moTextMerchPackagingCode.setValue(moBolMerchandise.getPackagingCode());
            moTextMerchPackagingName.setValue(moBolMerchandise.getPackagingName());
            moTextMerchTariff.setValue(moBolMerchandise.getTariff());
            
            DDbBolMerchandise.ImporRequest imporRequest = null;
            
            try {
                imporRequest = moBolMerchandise.createImporRequest();
                
            }
            catch (Exception e) {
                DLibUtils.printException(this, e);
            }
            
            if (imporRequest != null) {
                moIntMerchImportRequest1.setValue(DLibUtils.parseInt(imporRequest.Section1));
                moIntMerchImportRequest2.setValue(DLibUtils.parseInt(imporRequest.Section2));
                moIntMerchImportRequest3.setValue(DLibUtils.parseInt(imporRequest.Section3));
                moIntMerchImportRequest4.setValue(DLibUtils.parseInt(imporRequest.Section4));
            }
            else {
                moIntMerchImportRequest1.resetField();
                moIntMerchImportRequest2.resetField();
                moIntMerchImportRequest3.resetField();
                moIntMerchImportRequest4.resetField();
            }
            
            // dependents:
            
            for (DDbBolMerchandiseMove move : moBolMerchandise.getChildMoves()) {
                moGridMerchandisesMoves.addGridRow(move);
            }
            moGridMerchandisesMoves.setSelectedGridRow(0);
        }
    
        computeMerchWeightKg();
        computeMerchDimensions();
        computeMerchImportRequest();
    }
    
    private void renderBolMerchandiseMove(final DDbBolMerchandiseMove bolMerchandiseMove) {
        moBolMerchandiseMove = bolMerchandiseMove;
        
        populateMerchMoveSourceDestinyLocations(); // call before setting fields values!
        
        if (moBolMerchandiseMove == null) {
            moFieldsMerchandiseMove.resetFields();
        }
        else {
            moKeyMerchMoveSource.setValue(moBolMerchandiseMove.getSourceLocationKey());
            moKeyMerchMoveDestiny.setValue(moBolMerchandiseMove.getDestinyLocationKey());
            moDecMerchMoveQuantity.setValue(moBolMerchandiseMove.getQuantity());
        }
    }
    
    private void editBolMerchandise(final boolean editing, final int action) {
        mbEditingMerchandise = editing;
        
        // CRUD action:
        
        if (!mbEditingMerchandise) {
            mnActionMerchandise = 0;
            jtfMerchCrud.setText("");
        }
        else {
            mnActionMerchandise = action;
            jtfMerchCrud.setText(getCrudText(action));
            jtfMerchCrud.setCaretPosition(0);
        }
        
        // status of fields and input controls:
        
        moKeyMerchItem.setEnabled(mbEditingMerchandise);
        jbMerchPickItem.setEnabled(mbEditingMerchandise);
        itemStateChangedMerchItem();
        //jbMerchPickItem.setEnabled(mbEditingMerchandise); // depends on itemStateChangedMerchItem()
        //moTextMerchItemDescription.setEnabled(mbEditingMerchandise); // depends on itemStateChangedMerchItem()
        //moKeyMerchUnit.setEnabled(mbEditingMerchandise); // depends on itemStateChangedMerchItem()
        //itemStateChangedMerchUnit(); // depends on itemStateChangedMerchItem() & itemStateChangedMerchUnit()
        //moTextMerchUnitDescription.setEnabled(mbEditingMerchandise); // depends on itemStateChangedMerchItem() & itemStateChangedMerchUnit()
        moDecMerchQuantity.setEnabled(mbEditingMerchandise);
        moDecMerchWeightKg.setEnabled(mbEditingMerchandise);
        jbMerchSetWeightKg.setEnabled(mbEditingMerchandise);
        moIntMerchDimensionsLength.setEnabled(mbEditingMerchandise);
        moIntMerchDimensionsHeight.setEnabled(mbEditingMerchandise);
        moIntMerchDimensionsWidth.setEnabled(mbEditingMerchandise);
        moRadMerchDimensionsCm.setEnabled(mbEditingMerchandise);
        moRadMerchDimensionsPlg.setEnabled(mbEditingMerchandise);
        
        moKeyMerchCurrency.setEnabled(mbEditingMerchandise);
        itemStateChangedMerchCurrency();
        //moCurMerchValue.getField().setEnabled(mbEditingMerchandise); // depends on itemStateChangedMerchCurrency()
        moBoolMerchHazardousMaterial.setEnabled(mbEditingMerchandise && moKeyMerchItem.getSelectedIndex() > 0);
        itemStateChangedMerchHazardousMaterial();
        //moRadMerchHazardousMaterialYes.setEnabled(...); // depends on itemStateChangedMerchHazardousMaterial()
        //moRadMerchHazardousMaterialNo.setEnabled(...); // depends on itemStateChangedMerchHazardousMaterial()
        //moTextMerchHazardousMaterialCode.setEnabled(...); // depends on itemStateChangedMerchHazardousMaterialYesNo()
        //moTextMerchHazardousMaterialName.setEnabled(...); // depends on itemStateChangedMerchHazardousMaterialYesNo()
        //moTextMerchPackagingCode.setEnabled(...); // depends on itemStateChangedMerchHazardousMaterialYesNo()
        //moTextMerchPackagingName.setEnabled(...); // depends on itemStateChangedMerchHazardousMaterialYesNo()
        moTextMerchTariff.setEnabled(mbEditingMerchandise && moBoolBolIntlTransport.getValue());
        moIntMerchImportRequest1.setEnabled(mbEditingMerchandise && moBoolBolIntlTransport.getValue());
        moIntMerchImportRequest2.setEnabled(mbEditingMerchandise && moBoolBolIntlTransport.getValue());
        moIntMerchImportRequest3.setEnabled(mbEditingMerchandise && moBoolBolIntlTransport.getValue());
        moIntMerchImportRequest4.setEnabled(mbEditingMerchandise && moBoolBolIntlTransport.getValue());
        
        // status of CRUD controls:
        
        jbMerchAdd.setEnabled(false); // adding not required
        jbMerchCreate.setEnabled(!mbEditingMerchandise);
        jbMerchCopy.setEnabled(!mbEditingMerchandise);
        jbMerchModify.setEnabled(!mbEditingMerchandise);
        jbMerchRemove.setEnabled(!mbEditingMerchandise);
        jbMerchMoveUp.setEnabled(!mbEditingMerchandise);
        jbMerchMoveDown.setEnabled(!mbEditingMerchandise);

        jbMerchOk.setEnabled(mbEditingMerchandise);
        jbMerchCancel.setEnabled(mbEditingMerchandise);
        
        moGridMerchandises.getTable().setEnabled(!mbEditingMerchandise);
        moGridMerchandises.getTable().getTableHeader().setEnabled(!mbEditingMerchandise);
        
        // clear or refresh data:
        
        switch (action) { // act on original parameter
            case ACTION_ADD:
                break;
            case ACTION_CREATE:
                renderBolMerchandise(null);
                break;
            case ACTION_COPY:
            case ACTION_MODIFY:
            case ACTION_CANCEL:
                valueChangedMerch(); // renders current grid row
                break;
            default:
                // nothing
        }
        
        // propagate edition state to dependents:
        
        editBolMerchandiseMove(false, mbEditingMerchandise ? ACTION_CANCEL : 0);
        if (action == 0) { // check original parameter
            jbMerchMoveAdd.setEnabled(false);
            jbMerchMoveCreate.setEnabled(false);
            jbMerchMoveModify.setEnabled(false);
            jbMerchMoveRemove.setEnabled(false);
            jbMerchMoveOk.setEnabled(false);
            jbMerchMoveCancel.setEnabled(false);
        }
        
        // set edition status appropriately into form:
        
        if (action != 0) { // check original parameter
            enableBolNavButtons(mbEditingMerchandise ? 0 : NAV_ACTION_START);
            
            if (mbEditingMerchandise) {
                moKeyMerchItem.requestFocusInWindow();
            }
            else {
                jbMerchCreate.requestFocusInWindow();
            }
        }
    }

    private void editBolMerchandiseMove(final boolean editing, final int action) {
        mbEditingMerchandiseMove = editing;
        
        // CRUD action:
        
        if (!mbEditingMerchandiseMove) {
            mnActionMerchandiseMove = 0;
            jtfMerchMoveCrud.setText("");
        }
        else {
            mnActionMerchandiseMove = action;
            jtfMerchMoveCrud.setText(getCrudText(action));
            jtfMerchMoveCrud.setCaretPosition(0);
        }
        
        // status of fields and input controls:
        
        moKeyMerchMoveSource.setEnabled(mbEditingMerchandiseMove);
        moKeyMerchMoveDestiny.setEnabled(mbEditingMerchandiseMove);
        moDecMerchMoveQuantity.setEnabled(mbEditingMerchandiseMove);
        jbMerchMoveSetQuantity.setEnabled(mbEditingMerchandiseMove);
        
        // status of CRUD controls:
        
        jbMerchMoveAdd.setEnabled(false); // adding not required
        jbMerchMoveCreate.setEnabled(mbEditingMerchandise && !mbEditingMerchandiseMove);
        jbMerchMoveModify.setEnabled(mbEditingMerchandise && !mbEditingMerchandiseMove);
        jbMerchMoveRemove.setEnabled(mbEditingMerchandise && !mbEditingMerchandiseMove);

        jbMerchMoveOk.setEnabled(mbEditingMerchandise && mbEditingMerchandiseMove);
        jbMerchMoveCancel.setEnabled(mbEditingMerchandise && mbEditingMerchandiseMove);
        
        moGridMerchandisesMoves.getTable().setEnabled(mbEditingMerchandise && !mbEditingMerchandiseMove);
        moGridMerchandisesMoves.getTable().getTableHeader().setEnabled(mbEditingMerchandise && !mbEditingMerchandiseMove);

        // clear or refresh data:
        
        switch (action) { // act on original parameter
            case ACTION_ADD:
                break;
            case ACTION_CREATE:
                renderBolMerchandiseMove(null);
                // default values:
                boolean setSource = false;
                if (moKeyMerchMoveSource.isEnabled() && moKeyMerchMoveSource.getSelectedIndex() <= 0 && moKeyMerchMoveSource.getItemCount() == 2) {
                    moKeyMerchMoveSource.setSelectedIndex(1);
                    setSource = true;
                }
                boolean setDestiny = false;
                if (moKeyMerchMoveDestiny.isEnabled() && moKeyMerchMoveDestiny.getSelectedIndex() <= 0 && moKeyMerchMoveDestiny.getItemCount() == 2) {
                    moKeyMerchMoveDestiny.setSelectedIndex(1);
                    setDestiny = true;
                }
                if (setSource && setDestiny && moDecMerchMoveQuantity.isEnabled()) {
                    actionPerformedMerchMoveSetQuantity();
                }
                break;
            case ACTION_COPY:
            case ACTION_MODIFY:
            case ACTION_CANCEL:
                valueChangedMerchMove(); // renders current grid row
                break;
            default:
                // nothing
        }
        
        // propagate edition state to precedents
        
        if (mbEditingMerchandiseMove) {
            jbMerchOk.setEnabled(false);
            jbMerchCancel.setEnabled(false);
        }
        else {
            jbMerchOk.setEnabled(mbEditingMerchandise);
            jbMerchCancel.setEnabled(mbEditingMerchandise);
        }
        
        // set edition status appropriately into form:
        
        if (action != 0) { // check original parameter
            //enableBolNavButtons(mbEditingMerchandiseMove ? 0 : NAV_ACTION_START); // does not interfere with BOL navigation
            
            if (mbEditingMerchandiseMove) {
                moKeyMerchMoveSource.requestFocusInWindow();
            }
            else {
                jbMerchMoveCreate.requestFocusInWindow();
            }
        }
    }

    /**
     * Render grid rows. It is needed public in order to be invoked by reflection.
     */
    public void refreshBolMerchandises() {
        int sortingPos = 0;
        
        for (DGridRow row : moGridMerchandises.getModel().getGridRows()) {
            ((DDbBolMerchandise) row).setBolSortingPos(++sortingPos);
        }
        
        moGridMerchandises.renderGridRows();
    }

    /**
     * Render grid rows. It is needed public in order to be invoked by reflection.
     */
    public void refreshBolMerchandisesMoves() {
        int sortingPos = 0;
        
        for (DGridRow row : moGridMerchandisesMoves.getModel().getGridRows()) {
            ((DDbBolMerchandiseMove) row).setBolSortingPos(++sortingPos);
        }
        
        moGridMerchandisesMoves.renderGridRows();
    }
    
    private void computeMerchWeightKg() {
        if (moMerchUnit == null || moMerchUnit.getRatioKg() == 0) {
            jlMerchRatioKgHint.setText("");
            jlMerchRatioKgHint.setToolTipText(null);
        }
        else {
            String ratio = DLibUtils.getDecimalFormatQuantity().format(moMerchUnit.getRatioKg()) + " x " + moMerchUnit.getCode();
            jlMerchRatioKgHint.setText(ratio);
            jlMerchRatioKgHint.setToolTipText("Equivalencia kg: " + ratio);

            if (moDecMerchWeightKg.getValue() == 0) {
                moDecMerchWeightKg.setValue(moDecMerchQuantity.getValue() * moMerchUnit.getRatioKg());
            }
        }
    }
    
    private void computeMerchDimensions() {
        if (moIntMerchDimensionsLength.getValue() != 0 && moIntMerchDimensionsHeight.getValue() != 0 && moIntMerchDimensionsWidth.getValue() != 0 && bgMerchDimensionUnits.getSelection() != null) {
            jtfMerchDimensionsHint.setText(DDbBolMerchandise.composeDimensions(
                    moIntMerchDimensionsLength.getValue(), 
                    moIntMerchDimensionsHeight.getValue(), 
                    moIntMerchDimensionsWidth.getValue(), 
                    moRadMerchDimensionsCm.isSelected() ? DCfdi40Catalogs.CcpDimensiónCm : DCfdi40Catalogs.CcpDimensiónPlg));
        }
        else {
            jtfMerchDimensionsHint.setText(DFormBolUtils.NA);
        }
        
        jtfMerchDimensionsHint.setCaretPosition(0);
    }
    
    private void computeMerchImportRequest() {
        if (moIntMerchImportRequest1.getValue() != 0 && moIntMerchImportRequest2.getValue() != 0 && moIntMerchImportRequest3.getValue() != 0 && moIntMerchImportRequest4.getValue() != 0) {
            jtfMerchImportRequestHint.setText(DDbBolMerchandise.composeImportRequest(
                    "" + moIntMerchImportRequest1.getValue(), 
                    "" + moIntMerchImportRequest2.getValue(), 
                    "" + moIntMerchImportRequest3.getValue(), 
                    "" + moIntMerchImportRequest4.getValue()));
        }
        else {
            jtfMerchImportRequestHint.setText(DFormBolUtils.NA);
        }
        
        jtfMerchImportRequestHint.setCaretPosition(0);
    }
    
    private void computeMerchHazardousMaterialCode() {
        DFormBolUtils.computeCatalogCode(moTextMerchHazardousMaterialCode, moTextMerchHazardousMaterialName, DFormBolUtils.DEF_CODE_HAZARDOUS_MATERIAL, 
                DFormBolUtils.FormatCodeHazardousMaterial, DCfdi40Catalogs.XML_CCP_MAT_PEL, false, null, "");
    }
    
    private void computeMerchPackagingCode() {
        DFormBolUtils.computeCatalogCode(moTextMerchPackagingCode, moTextMerchPackagingName, DFormBolUtils.DEF_CODE_PACKAGING, 
                null, DCfdi40Catalogs.XML_CCP_EMB_TP, false, null, "");
    }
    
    private void computeMerchQuantityMoves() {
        double quantityMoves = 0;
        
        for (DGridRow row : moGridMerchandisesMoves.getModel().getGridRows()) {
            quantityMoves += ((DDbBolMerchandiseMove) row).getQuantity();
        }
        
        jtfMerchQuantityMoved.setText(DLibUtils.DecimalFormatValue6D.format(quantityMoves));
        jtfMerchQuantityMoved.setCaretPosition(0);
    }
    
    @SuppressWarnings("unchecked")
    private void populateMerchMoveSourceDestinyLocations() {
        moKeyMerchMoveSource.removeAllItems();
        moKeyMerchMoveDestiny.removeAllItems();
        
        moKeyMerchMoveSource.addItem(new DGuiItem("- " + DLibUtils.textProperCase(DGuiUtils.getLabelName(jlMerchMoveSource)) + " -"));
        moKeyMerchMoveDestiny.addItem(new DGuiItem("- " + DLibUtils.textProperCase(DGuiUtils.getLabelName(jlMerchMoveDestiny)) + " -"));

        for (DGridRow row : moGridLocations.getModel().getGridRows()) {
            DDbBolLocation bolLocation = (DDbBolLocation) row;
            
            switch (bolLocation.getFkLocationTypeId()) {
                case DModSysConsts.LS_LOC_TP_SRC:
                    moKeyMerchMoveSource.addItem(createGuiItemForBolLocation(bolLocation));
                    break;
                case DModSysConsts.LS_LOC_TP_DES:
                    moKeyMerchMoveDestiny.addItem(createGuiItemForBolLocation(bolLocation));
                    break;
                default:
                    // nothing
            }
        }
    }
    
    private String getMerchDimensionsUnit() {
        String unit = "";
        
        if (moRadMerchDimensionsCm.isSelected()) {
            unit = DCfdi40Catalogs.CcpDimensiónCm;
        }
        else if (moRadMerchDimensionsPlg.isSelected()) {
            unit = DCfdi40Catalogs.CcpDimensiónPlg;
        }
        
        return unit;
    }
    
    private void actionPerformedMerchPickItem() {
        if (moPickerItem == null) {
            moPickerItem = new DPickerElement(miClient, DModConsts.IU_ITM);
        }
        
        moPickerItem.resetForm();
        moPickerItem.setVisible(true);

        if (moPickerItem.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
            moKeyMerchItem.setValue((int[]) moPickerItem.getValue(DPickerElement.ELEMENT));
            moKeyMerchItem.requestFocusInWindow();
        }
    }
    
    private void actionPerformedMerchSetWeightKg() {
        moDecMerchWeightKg.setValue(0d); // to force computing of merchandise weight
        computeMerchWeightKg();
        moDecMerchWeightKg.requestFocusInWindow();
    }
    
    private void actionPerformedMerchAdd() {
        throw new UnsupportedOperationException("Not supported yet."); // add not applicable
    }
    
    private void actionPerformedMerchCreate() {
        editBolMerchandise(true, ACTION_CREATE);
    }

    private void actionPerformedMerchCopy() {
        if (moGridMerchandises.getSelectedGridRow() == null) {
            miClient.showMsgBoxWarning(DGridConsts.MSG_SELECT_ROW);
        }
        else {
            editBolMerchandise(true, ACTION_COPY);
        }
    }

    private void actionPerformedMerchModify() {
        if (moGridMerchandises.getSelectedGridRow() == null) {
            miClient.showMsgBoxWarning(DGridConsts.MSG_SELECT_ROW);
        }
        else {
            editBolMerchandise(true, ACTION_MODIFY);
        }
    }

    private void actionPerformedMerchRemove() {
        try {
            actionGridRemove(moGridMerchandises, this.getClass().getMethod("refreshBolMerchandises"));
            computeBol();
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }

    private void actionPerformedMerchMoveUp() {
        try {
            actionGridMoveUp(moGridMerchandises, this.getClass().getMethod("refreshBolMerchandises"));
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }

    private void actionPerformedMerchMoveDown() {
        try {
            actionGridMoveDown(moGridMerchandises, this.getClass().getMethod("refreshBolMerchandises"));
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }

    private void actionPerformedMerchOk() {
        DGuiValidation validation = moFieldsMerchandise.validateFields();
        
        if (validation.isValid()) {
            if (moBoolMerchHazardousMaterial.isSelected() && moRadMerchHazardousMaterialYes.isSelected()) {
                try {
                    DXmlCatalog xmlCatalogHazardousMaterial = DFormBolUtils.getXmlCatalog(DCfdi40Catalogs.XML_CCP_MAT_PEL);
                    DXmlCatalog xmlCatalogPackaging = DFormBolUtils.getXmlCatalog(DCfdi40Catalogs.XML_CCP_EMB_TP);

                    if (moTextMerchHazardousMaterialCode.getValue().equals(DFormBolUtils.DEF_CODE_HAZARDOUS_MATERIAL)) {
                        validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moTextMerchHazardousMaterialCode.getFieldName() + "'.");
                        validation.setComponent(moTextMerchHazardousMaterialCode);
                    }
                    else if (xmlCatalogHazardousMaterial.getId(moTextMerchHazardousMaterialCode.getValue()) == 0) {
                        validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + moTextMerchHazardousMaterialCode.getFieldName() + "'.");
                        validation.setComponent(moTextMerchHazardousMaterialCode);
                    }
                    else if (moTextMerchPackagingCode.getValue().equals(DFormBolUtils.DEF_CODE_PACKAGING)) {
                        validation.setMessage(DGuiConsts.ERR_MSG_FIELD_REQ + "'" + moTextMerchPackagingCode.getFieldName() + "'.");
                        validation.setComponent(moTextMerchPackagingCode);
                    }
                    else if (xmlCatalogPackaging.getId(moTextMerchPackagingCode.getValue()) == 0) {
                        validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + moTextMerchPackagingCode.getFieldName() + "'.");
                        validation.setComponent(moTextMerchPackagingCode);
                    }
                }
                catch (Exception e) {
                    validation.setMessage(e.getMessage());
                }
            }
            
            if (validation.isValid()) {
                if (moGridMerchandisesMoves.getTable().getRowCount() == 0 && miClient.showMsgBoxConfirm("No hay cantidades transportadas.\n"
                        + DGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                    validation.setMessage("Agregar cantidades transportadas.");
                    validation.setComponent(jbMerchMoveCreate);
                }
                else {
                    double quantityMoved = DLibUtils.parseDouble(jtfMerchQuantityMoved.getText());

                    if (quantityMoved <= 0) {
                        if (moGridMerchandisesMoves.getTable().getRowCount() > 0 && miClient.showMsgBoxConfirm(DGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + DGuiUtils.getLabelName(jtfMerchQuantityMoved.getToolTipText()) + "'"
                                + DGuiConsts.ERR_MSG_FIELD_VAL_GREAT + DLibUtils.DecimalFormatValue6D.format(0) + ".\n"
                                + DGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                            validation.setMessage(DGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + DGuiUtils.getLabelName(jtfMerchQuantityMoved.getToolTipText()) + "'"
                                    + DGuiConsts.ERR_MSG_FIELD_VAL_GREAT + DLibUtils.DecimalFormatValue6D.format(0) + ".");
                            validation.setComponent(jbMerchMoveCreate);
                        }
                    }
                    else {
                        if (quantityMoved > moDecMerchQuantity.getValue()) {
                            validation.setMessage(DGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + DGuiUtils.getLabelName(jtfMerchQuantityMoved.getToolTipText()) + "', " + DLibUtils.DecimalFormatValue6D.format(quantityMoved) + ","
                                    + DGuiConsts.ERR_MSG_FIELD_VAL_LESS_EQUAL + DLibUtils.DecimalFormatValue6D.format(moDecMerchQuantity.getValue()) + ".");
                            validation.setComponent(jbMerchMoveModify);
                        }
                        else if (quantityMoved < moDecMerchQuantity.getValue() && miClient.showMsgBoxConfirm(DGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + DGuiUtils.getLabelName(jtfMerchQuantityMoved.getToolTipText()) + "', " + DLibUtils.DecimalFormatValue6D.format(quantityMoved) + ","
                                + DGuiConsts.ERR_MSG_FIELD_VAL_EQUAL + DLibUtils.DecimalFormatValue6D.format(moDecMerchQuantity.getValue()) + ".\n"
                                + DGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                            validation.setMessage(DGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + DGuiUtils.getLabelName(jtfMerchQuantityMoved.getToolTipText()) + "', " + DLibUtils.DecimalFormatValue6D.format(quantityMoved) + ","
                                    + DGuiConsts.ERR_MSG_FIELD_VAL_EQUAL + DLibUtils.DecimalFormatValue6D.format(moDecMerchQuantity.getValue()) + ".");
                            validation.setComponent(jbMerchMoveModify);
                        }
                    }
                }
            }
        }
        
        if (DGuiUtils.computeValidation(miClient, validation)) {
            boolean creating = mnActionMerchandise == ACTION_CREATE || mnActionMerchandise == ACTION_COPY; // convenience variable
            
            if (creating) {
                moBolMerchandise = new DDbBolMerchandise();
            }
            
            //moBolMerchandise.setPkBolId(...);
            //moBolMerchandise.setPkMerchandiseId(...);
            moBolMerchandise.setDescriptionItem(moTextMerchItemDescription.getValue());
            moBolMerchandise.setDescriptionUnit(moTextMerchUnitDescription.getValue());
            moBolMerchandise.setQuantity(moDecMerchQuantity.getValue());
            moBolMerchandise.setDimensions(DDbBolMerchandise.composeDimensions(
                    moIntMerchDimensionsLength.getValue(), 
                    moIntMerchDimensionsHeight.getValue(), 
                    moIntMerchDimensionsWidth.getValue(), 
                    getMerchDimensionsUnit()));
            moBolMerchandise.setHazardousMaterial(moBoolMerchHazardousMaterial.getValue());
            if (!moBolMerchandise.isHazardousMaterial()) {
                moBolMerchandise.setHazardousMaterial("");
                moBolMerchandise.setHazardousMaterialCode("");
                moBolMerchandise.setHazardousMaterialName("");
                moBolMerchandise.setPackagingCode("");
                moBolMerchandise.setPackagingName("");
            }
            else {
                if (moRadMerchHazardousMaterialYes.getValue()) {
                    moBolMerchandise.setHazardousMaterial(DDbItem.HAZARDOUS_MATERIAL_Y);
                    moBolMerchandise.setHazardousMaterialCode(moTextMerchHazardousMaterialCode.getValue());
                    moBolMerchandise.setHazardousMaterialName(moTextMerchHazardousMaterialName.getValue());
                    moBolMerchandise.setPackagingCode(moTextMerchPackagingCode.getValue());
                    moBolMerchandise.setPackagingName(moTextMerchPackagingName.getValue());
                }
                else {
                    moBolMerchandise.setHazardousMaterial(DDbItem.HAZARDOUS_MATERIAL_N);
                    moBolMerchandise.setHazardousMaterialCode("");
                    moBolMerchandise.setHazardousMaterialName("");
                    moBolMerchandise.setPackagingCode("");
                    moBolMerchandise.setPackagingName("");
                }
            }
            moBolMerchandise.setWeightKg(moDecMerchWeightKg.getValue());
            moBolMerchandise.setValue(moKeyMerchCurrency.getSelectedIndex() <= 0 ? 0.0 : moCurMerchValue.getField().getValue());
            moBolMerchandise.setTariff(moTextMerchTariff.getValue());
            moBolMerchandise.setImportRequest(DDbBolMerchandise.composeImportRequest(
                    "" + moIntMerchImportRequest1.getValue(), 
                    "" + moIntMerchImportRequest2.getValue(), 
                    "" + moIntMerchImportRequest3.getValue(), 
                    "" + moIntMerchImportRequest4.getValue()));
            moBolMerchandise.setFkItemId(moKeyMerchItem.getValue()[0]);
            moBolMerchandise.setFkUnitId(moKeyMerchUnit.getValue()[0]);
            moBolMerchandise.setFkCurrencyId(moKeyMerchCurrency.getSelectedIndex() <= 0 ? DModSysConsts.CS_CUR_NA : moKeyMerchCurrency.getValue()[0]);

            moBolMerchandise.getChildMoves().clear();
            for (DGridRow row : moGridMerchandisesMoves.getModel().getGridRows()) {
                moBolMerchandise.getChildMoves().add((DDbBolMerchandiseMove) row);
            }

            moBolMerchandise.setOwnItem((DDbItem) miClient.getSession().readRegistry(DModConsts.IU_ITM, moKeyMerchItem.getValue()));
            moBolMerchandise.setOwnUnit((DDbUnit) miClient.getSession().readRegistry(DModConsts.IU_UNT, moKeyMerchUnit.getValue()));
            
            moBolMerchandise.computeQuantityMoved();

            // update grid:

            if (creating) {
                moGridMerchandises.addGridRow(moBolMerchandise);
            }
            
            int row = moGridMerchandises.getModel().getGridRows().indexOf(moBolMerchandise); // get row before refreshing grid!
            refreshBolMerchandises();
            moGridMerchandises.setSelectedGridRow(moGridMerchandises.getTable().convertRowIndexToView(row));
            
            actionPerformedMerchCancel();
            
            computeBol();
        }
    }

    private void actionPerformedMerchCancel() {
        editBolMerchandise(false, ACTION_CANCEL);
    }
    
    private void actionPerformedMerchMoveSetQuantity() {
        double quantityMoved = DLibUtils.parseDouble(jtfMerchQuantityMoved.getText());
        double quantityPending = moDecMerchQuantity.getValue() > quantityMoved ? moDecMerchQuantity.getValue() - quantityMoved : 0.0;
        
        if (quantityPending == 0.0) {
            miClient.showMsgBoxWarning("No hay cantidad por transportar.");
        }
        else {
            moDecMerchMoveQuantity.setValue(quantityPending);
        }
        
        moDecMerchMoveQuantity.requestFocusInWindow();
    }
    
    private void actionPerformedMerchMoveAdd() {
        throw new UnsupportedOperationException("Not supported yet."); // add not applicable
    }
    
    private void actionPerformedMerchMoveCreate() {
        editBolMerchandiseMove(true, ACTION_CREATE);
    }

    private void actionPerformedMerchMoveModify() {
        if (moGridMerchandisesMoves.getSelectedGridRow() == null) {
            miClient.showMsgBoxWarning(DGridConsts.MSG_SELECT_ROW);
        }
        else {
            editBolMerchandiseMove(true, ACTION_MODIFY);
        }
    }

    private void actionPerformedMerchMoveRemove() {
        try {
            actionGridRemove(moGridMerchandisesMoves, this.getClass().getMethod("refreshBolMerchandisesMoves"));
            computeMerchQuantityMoves();
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }

    private void actionPerformedMerchMoveOk() {
        DGuiValidation validation = moFieldsMerchandiseMove.validateFields();
        
        if (validation.isValid()) {
            double quantityMoved = DLibUtils.parseDouble(jtfMerchQuantityMoved.getText());
            double quantityMovedNew = quantityMoved + moDecMerchMoveQuantity.getValue();

            if (quantityMovedNew > moDecMerchQuantity.getValue()) {
                validation.setMessage("La cantidad transportada, " + DLibUtils.DecimalFormatValue6D.format(quantityMoved) + ", "
                        + "más la cantidad por transportar, " + DLibUtils.DecimalFormatValue6D.format(moDecMerchMoveQuantity.getValue()) + ", "
                        + "es mayor a la cantidad de la mercancía, " + DLibUtils.DecimalFormatValue6D.format(moDecMerchQuantity.getValue()) + ".");
                validation.setComponent(moDecMerchMoveQuantity);
            }
        }
        
        if (DGuiUtils.computeValidation(miClient, validation)) {
            boolean creating = mnActionMerchandiseMove == ACTION_CREATE || mnActionMerchandiseMove == ACTION_COPY; // convenience variable
            
            if (creating) {
                moBolMerchandiseMove = new DDbBolMerchandiseMove();
            }
            
            //moBolMerchandiseMove.setPkBolId(...);
            //moBolMerchandiseMove.setPkMerchandiseId(...);
            //moBolMerchandiseMove.setPkMoveId(...);
            moBolMerchandiseMove.setQuantity(moDecMerchMoveQuantity.getValue());
            moBolMerchandiseMove.setFkSourceBolId(moKeyMerchMoveSource.getValue()[0]);
            moBolMerchandiseMove.setFkSourceLocationId(moKeyMerchMoveSource.getValue()[1]);
            moBolMerchandiseMove.setFkDestinyBolId(moKeyMerchMoveDestiny.getValue()[0]);
            moBolMerchandiseMove.setFkDestinyLocationId(moKeyMerchMoveDestiny.getValue()[1]);
            moBolMerchandiseMove.setFkTransportTypeId(DModSysConsts.LS_TPT_TP_TRUCK);

            moBolMerchandiseMove.setOwnSourceLocation((DDbLocation) moKeyMerchMoveSource.getSelectedItem().getComplement());
            moBolMerchandiseMove.setOwnDestinyLocation((DDbLocation) moKeyMerchMoveDestiny.getSelectedItem().getComplement());
            
            moBolMerchandiseMove.setTempSourceBolLocationId(moKeyMerchMoveSource.getValue()[1]); // preserve temporary ID of just created BOL locations
            moBolMerchandiseMove.setTempDestinyBolLocationId(moKeyMerchMoveDestiny.getValue()[1]); // preserve temporary ID of just created BOL locations
            
            // update grid:

            if (creating) {
                moGridMerchandisesMoves.addGridRow(moBolMerchandiseMove);
            }
            
            int row = moGridMerchandisesMoves.getModel().getGridRows().indexOf(moBolMerchandiseMove); // get row before refreshing grid!
            refreshBolMerchandisesMoves();
            moGridMerchandisesMoves.setSelectedGridRow(moGridMerchandisesMoves.getTable().convertRowIndexToView(row));
            
            computeMerchQuantityMoves();
            
            actionPerformedMerchMoveCancel();
        }
    }

    private void actionPerformedMerchMoveCancel() {
        editBolMerchandiseMove(false, ACTION_CANCEL);
    }
    
    private void itemStateChangedMerchItem() {
        String cfdItemKey = "";
        
        moTextMerchItemDescription.resetField();
        moKeyMerchUnit.resetField();
        moBoolMerchHazardousMaterial.resetField();
        itemStateChangedMerchHazardousMaterial();
        
        if (moKeyMerchItem.getSelectedIndex() <= 0) {
            moTextMerchItemDescription.setEnabled(false);
            moKeyMerchUnit.setEnabled(false);
            moBoolMerchHazardousMaterial.setEnabled(false);
            
            moMerchItem = null;
        }
        else {
            moTextMerchItemDescription.setEnabled(mbEditingMerchandise);
            moKeyMerchUnit.setEnabled(mbEditingMerchandise);
            moBoolMerchHazardousMaterial.setEnabled(mbEditingMerchandise);
            
            moMerchItem = (DDbItem) miClient.getSession().readRegistry(DModConsts.IU_ITM, moKeyMerchItem.getValue());
            cfdItemKey = moMerchItem.getActualCfdItemKey();
            
            moTextMerchItemDescription.setValue(moMerchItem.getName());
            moKeyMerchUnit.setValue(new int[] { moMerchItem.getFkUnitId() });
            moBoolMerchHazardousMaterial.setValue(moMerchItem.isHazardousMaterial());
            itemStateChangedMerchHazardousMaterial();
            moRadMerchHazardousMaterialYes.setValue(moMerchItem.isHazardousMaterialYes());
            moRadMerchHazardousMaterialNo.setValue(moMerchItem.isHazardousMaterialNo());
            itemStateChangedMerchHazardousMaterialYesNo();
            
            if (moRadMerchHazardousMaterialYes.isSelected()) {
                moTextMerchHazardousMaterialCode.setValue(moMerchItem.getHazardousMaterialCode());
                moTextMerchPackagingCode.setValue(moMerchItem.getPackagingCode());
                computeMerchHazardousMaterialCode();
                computeMerchPackagingCode();
            }
        }
        
        jlMerchCfdItemKey.setText(TXT_CFD_ITEM_KEY + ": " + (cfdItemKey.isEmpty() ? "?" : cfdItemKey));
        
        itemStateChangedMerchUnit();
    }
    
    private void itemStateChangedMerchUnit() {
        moTextMerchUnitDescription.resetField();
        
        if (moKeyMerchUnit.getSelectedIndex() <= 0) {
            moTextMerchUnitDescription.setEnabled(false);
            
            moMerchUnit = null;
        }
        else {
            moTextMerchUnitDescription.setEnabled(mbEditingMerchandise);
            
            moMerchUnit = (DDbUnit) miClient.getSession().readRegistry(DModConsts.IU_UNT, moKeyMerchUnit.getValue());
            
            moTextMerchUnitDescription.setValue(moMerchUnit.getName());
        }
        
        computeMerchWeightKg();
    }

    private void itemStateChangedMerchCurrency() {
        moCurMerchValue.getField().resetField();
        
        if (moKeyMerchCurrency.getSelectedIndex() <= 0) {
            moCurMerchValue.getField().setEnabled(false);
            
            moCurMerchValue.setCurrencyKey(null);
        }
        else {
            moCurMerchValue.getField().setEnabled(mbEditingMerchandise);
            
            moCurMerchValue.setCurrencyKey(moKeyMerchCurrency.getValue());
        }
    }

    private void itemStateChangedMerchDimensions() {
        computeMerchDimensions();
    }
    
    private void itemStateChangedMerchHazardousMaterial() {
        if (moBoolMerchHazardousMaterial.isSelected()) {
            bgMerchHazardousMaterial.setSelected(moRadMerchHazardousMaterialYes.getModel(), true);
            
            moRadMerchHazardousMaterialYes.setEnabled(mbEditingMerchandise);
            moRadMerchHazardousMaterialNo.setEnabled(mbEditingMerchandise);
        }
        else {
            bgMerchHazardousMaterial.clearSelection();
            
            moRadMerchHazardousMaterialYes.setEnabled(false);
            moRadMerchHazardousMaterialNo.setEnabled(false);
        }
        
        itemStateChangedMerchHazardousMaterialYesNo();
    }
    
    private void itemStateChangedMerchHazardousMaterialYesNo() {
        moTextMerchHazardousMaterialCode.resetField();
        moTextMerchHazardousMaterialName.resetField();
        moTextMerchPackagingCode.resetField();
        moTextMerchPackagingName.resetField();
        
        if (moRadMerchHazardousMaterialYes.isSelected()) {
            moTextMerchHazardousMaterialCode.setEnabled(mbEditingMerchandise);
            moTextMerchPackagingCode.setEnabled(mbEditingMerchandise);
            
            computeMerchHazardousMaterialCode();
            computeMerchPackagingCode();
        }
        else {
            moTextMerchHazardousMaterialCode.setEnabled(false);
            moTextMerchPackagingCode.setEnabled(false);
        }
        
        moTextMerchHazardousMaterialName.setEnabled(false);
        moTextMerchPackagingName.setEnabled(false);
    }
    
    private void focusGainedMerchHazardousMaterialCode() {
        if (moTextMerchHazardousMaterialCode.getValue().equals(DFormBolUtils.DEF_CODE_HAZARDOUS_MATERIAL)) {
            moTextMerchHazardousMaterialCode.resetField();
        }
    }
    
    private void focusGainedMerchPackagingCode() {
        if (moTextMerchPackagingCode.getValue().equals(DFormBolUtils.DEF_CODE_PACKAGING)) {
            moTextMerchPackagingCode.resetField();
        }
    }
    
    private void focusLostMerchQuantity() {
        computeMerchWeightKg();
    }
                
    private void focusLostMerchDimensions() {
        computeMerchDimensions();
    }
    
    private void focusLostMerchImportRequest() {
        computeMerchImportRequest();
    }
    
    private void focusLostMerchHazardousMaterialCode() {
        computeMerchHazardousMaterialCode();
    }
    
    private void focusLostMerchPackagingCode() {
        computeMerchPackagingCode();
    }
    
    private void keyReleasedMerchHazardousMaterialCode(final KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_F5) {
            DGuiValidation validation = moKeyMerchItem.validateField();
            
            if (DGuiUtils.computeValidation(miClient, validation)) {
                if (moPickerCatalogHazardousMaterial == null) {
                    moPickerCatalogHazardousMaterial = new DPickerCatalogHazardousMaterial(miClient);
                }

                moPickerCatalogHazardousMaterial.resetForm();
                moPickerCatalogHazardousMaterial.setVisible(true);
                
                if (moPickerCatalogHazardousMaterial.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                    DLadCatalogHazardousMaterial hazardousMaterial = (DLadCatalogHazardousMaterial) moPickerCatalogHazardousMaterial.getValue(DLadCatalogConsts.MERCH_HAZARDOUS_MATERIAL);
                    if (hazardousMaterial != null) {
                        moTextMerchHazardousMaterialCode.setValue(hazardousMaterial.Code);
                        moTextMerchHazardousMaterialName.setValue(hazardousMaterial.Name);
                    }
                }
            }
        }
    }
    
    private void keyReleasedMerchPackagingCode(final KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_F5) {
            DGuiValidation validation = moKeyMerchItem.validateField();
            
            if (DGuiUtils.computeValidation(miClient, validation)) {
                if (moPickerCatalogPackaging == null) {
                    moPickerCatalogPackaging = new DPickerCatalogPackaging(miClient);
                }

                moPickerCatalogPackaging.resetForm();
                moPickerCatalogPackaging.setVisible(true);
                
                if (moPickerCatalogPackaging.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                    DLadCatalogPackaging hazardousMaterial = (DLadCatalogPackaging) moPickerCatalogPackaging.getValue(DLadCatalogConsts.MERCH_PACKAGING);
                    if (hazardousMaterial != null) {
                        moTextMerchPackagingCode.setValue(hazardousMaterial.Code);
                        moTextMerchPackagingName.setValue(hazardousMaterial.Name);
                    }
                }
            }
        }
    }
            
    private void valueChangedMerch() {
        renderBolMerchandise((DDbBolMerchandise) moGridMerchandises.getSelectedGridRow());
    }

    private void valueChangedMerchMove() {
        renderBolMerchandiseMove((DDbBolMerchandiseMove) moGridMerchandisesMoves.getSelectedGridRow());
    }
    
    /*
     * Truck
     */
    
    private DDbBolTruck createBolTruckPickingOne() {
        DDbBolTruck bolTruck = null;
        
        if (moPickerTruck == null) {
            moPickerTruck = new DPickerElement(miClient, DModConsts.LU_TRUCK);
        }
        
        moPickerTruck.resetForm();
        moPickerTruck.setVisible(true);

        if (moPickerTruck.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
            int[] key = (int[]) moPickerTruck.getValue(DPickerElement.ELEMENT);
            
            if (key != null) {
                bolTruck = new DDbBolTruck();
                bolTruck.setOwnTruck((DDbTruck) miClient.getSession().readRegistry(DModConsts.LU_TRUCK, key), mdBolMerchandiseWeightKg);
                
                int sortingPos = 0;
                for (DDbTruckTrailer truckTrailer : bolTruck.getOwnTruck().getChildTrailers()) {
                    DDbBolTruckTrailer bolTruckTrailer = new DDbBolTruckTrailer();
                    bolTruckTrailer.setOwnTrailer((DDbTrailer) miClient.getSession().readRegistry(DModConsts.LU_TRAIL, new int[] { truckTrailer.getFkTrailerId() }));
                    bolTruckTrailer.setBolSortingPos(++sortingPos);
                    
                    bolTruck.getChildTrailers().add(bolTruckTrailer);
                }
            }
        }
        
        return bolTruck;
    }

    private DDbBolTruckTrailer createBolTruckTrailerPickingOne() {
        DDbBolTruckTrailer bolTruckTrailer = null;
        
        if (moPickerTrail == null) {
            moPickerTrail = new DPickerElement(miClient, DModConsts.LU_TRAIL);
        }
        
        moPickerTrail.resetForm();
        moPickerTrail.setVisible(true);

        if (moPickerTrail.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
            int[] key = (int[]) moPickerTrail.getValue(DPickerElement.ELEMENT);
            
            if (key != null) {
                bolTruckTrailer = new DDbBolTruckTrailer();
                bolTruckTrailer.setOwnTrailer((DDbTrailer) miClient.getSession().readRegistry(DModConsts.LU_TRAIL, key));
            }
        }
        
        return bolTruckTrailer;
    }

    private boolean canBolTruckAppend() {
        boolean can = true;
        
        if (moGridTrucks.getTable().getRowCount() >= 1) {
            miClient.showMsgBoxWarning("Sólo puede haber un autotransporte.");
            can = false;
        }
        
        return can;
    }

    private boolean canBolTruckAdd() {
        boolean can = canBolTruckAppend();
        
        if (can) {
            int count = moGridTptFigures.getTable().getRowCount();
            if (count > 0) {
                can = miClient.showMsgBoxConfirm("Ya hay " + DLibUtils.DecimalFormatInteger.format(count) + " " + (count == 1 ? "figura" : "figuras") + " de transporte.\n"
                        + "Si decide agregar un transporte, serán sustituídas por sus propias figuras de transporte.\n"
                        + DGuiConsts.MSG_CNF_CONT) == JOptionPane.YES_OPTION;
            }
        }
        
        return can;
    }
    
    private boolean canBolTruckTrailerAppend() {
        boolean can = DLibUtils.belongsTo(mnTruckIsTrailerRequired, new int[] { DXmlCatalogEntry.REQUIRED_YES, DXmlCatalogEntry.REQUIRED_OPT });
        
        if (!can) {
            miClient.showMsgBoxWarning("No se requieren remolques.");
        }
        
        return can;
    }

    private void renderBolTruck(final DDbBolTruck bolTruck) {
        moBolTruck = bolTruck;
        moGridTrucksTrailers.clearGridRows();
        
        if (moBolTruck == null) {
            moFieldsTruck.resetFields();
            
            jtfTruckPk.setText("");
            
            moBoolTruckUpdate.setValue(true);
        }
        else {
            moKeyTruckTransportConfig.setValue(new int[] { moXmlTruckTransportConfig.getId(moBolTruck.getTransportConfigCode()) });
            itemStateChangedTruckTransportConfig();
            moTextTruckName.setValue(moBolTruck.getOwnTruck().getName());
            moTextTruckCode.setValue(moBolTruck.getOwnTruck().getCode());
            moDecTruckWeightTon.setValue(moBolTruck.getWeightTon());
            moDecTruckWeightGrossTon.setValue(moBolTruck.getWeightGrossTon());
            moTextTruckPlate.setValue(moBolTruck.getPlate());
            moYearTruckModel.setValue(moBolTruck.getModel());
            moKeyTruckPermissionType.setValue(new int[] { moXmlTruckPermissionType.getId(moBolTruck.getPermissionTypeCode()) });
            itemStateChangedTruckPermissionType();
            moTextTruckPermissionNumber.setValue(moBolTruck.getPermissionNumber());

            moTextTruckCivilInsurance.setValue(moBolTruck.getCivilInsurance());
            moTextTruckCivilPolicy.setValue(moBolTruck.getCivilPolicy());
            moTextTruckEnvironmentInsurance.setValue(moBolTruck.getEnvironmentInsurance());
            moTextTruckEnvironmentPolicy.setValue(moBolTruck.getEnvironmentPolicy());
            moTextTruckCargoInsurance.setValue(moBolTruck.getCargoInsurance());
            moTextTruckCargoPolicy.setValue(moBolTruck.getCargoPolicy());
            moCurTruckPrime.getField().setValue(moBolTruck.getPrime());
            
            jtfTruckPk.setText(DLibUtils.textKey(moBolTruck.getPrimaryKey()));
            jtfTruckPk.setCaretPosition(0);
            
            moBoolTruckUpdate.setValue(moBolTruck.getOwnTruck() == null || moBolTruck.getOwnTruck().isRegistryNew()); // update only when requested
            
            // dependents:
            
            for (DDbBolTruckTrailer trailer : moBolTruck.getChildTrailers()) {
                moGridTrucksTrailers.addGridRow(trailer);
            }
            moGridTrucksTrailers.setSelectedGridRow(0);
        }
    }
    
    private void renderBolTruckTrailer(final DDbBolTruckTrailer bolTruckTrailer) {
        moBolTruckTrailer = bolTruckTrailer;
        
        if (moBolTruckTrailer == null) {
            moFieldsTruckTrailer.resetFields();
            
            jtfTruckTrailPk.setText("");
            
            moBoolTruckTrailUpdate.setValue(true);
        }
        else {
            moKeyTruckTrailSubtype.setValue(new int[] { moXmlTruckTrailerSubtype.getId(moBolTruckTrailer.getTrailerSubtypeCode()) });
            itemStateChangedTruckTrailSubtype();
            moTextTruckTrailPlate.setValue(moBolTruckTrailer.getPlate());
            
            jtfTruckTrailPk.setText(DLibUtils.textKey(moBolTruckTrailer.getPrimaryKey()));
            jtfTruckTrailPk.setCaretPosition(0);
            
            moBoolTruckTrailUpdate.setValue(moBolTruckTrailer.getOwnTrailer() == null || moBolTruckTrailer.getOwnTrailer().isRegistryNew()); // update only when requested
        }
    }
    
    private void editBolTruck(final boolean editing, final int action) {
        mbEditingTruck = editing;
        
        // CRUD action:
        
        if (!mbEditingTruck) {
            mnActionTruck = 0;
            jtfTruckCrud.setText("");
        }
        else {
            mnActionTruck = action;
            jtfTruckCrud.setText(getCrudText(action));
            jtfTruckCrud.setCaretPosition(0);
        }
        
        // status of fields and input controls:
        
        moKeyTruckTransportConfig.setEnabled(mbEditingTruck);
        jbTruckEditConfig.setEnabled(false);
        itemStateChangedTruckTransportConfig();
        moTextTruckName.setEnabled(mbEditingTruck);
        moTextTruckCode.setEnabled(mbEditingTruck);
        jbTruckGetNextCode.setEnabled(mbEditingTruck);
        moDecTruckWeightTon.setEnabled(mbEditingTruck);
        moDecTruckWeightGrossTon.setEnabled(mbEditingTruck);
        jbTruckSetWeightGrossTon.setEnabled(mbEditingTruck);
        moTextTruckPlate.setEnabled(mbEditingTruck);
        moYearTruckModel.setEnabled(mbEditingTruck);
        moKeyTruckPermissionType.setEnabled(mbEditingTruck);
        itemStateChangedTruckPermissionType();
        moTextTruckPermissionNumber.setEnabled(mbEditingTruck);

        moTextTruckCivilInsurance.setEnabled(mbEditingTruck);
        moTextTruckCivilPolicy.setEnabled(mbEditingTruck);
        moTextTruckEnvironmentInsurance.setEnabled(mbEditingTruck);
        moTextTruckEnvironmentPolicy.setEnabled(mbEditingTruck);
        moTextTruckCargoInsurance.setEnabled(mbEditingTruck);
        moTextTruckCargoPolicy.setEnabled(mbEditingTruck);
        moCurTruckPrime.getField().setEnabled(mbEditingTruck);
        
        // status of CRUD controls:
        
        jbTruckAdd.setEnabled(!mbEditingTruck);
        jbTruckCreate.setEnabled(!mbEditingTruck);
        jbTruckCopy.setEnabled(!mbEditingTruck);
        jbTruckModify.setEnabled(!mbEditingTruck);
        jbTruckRemove.setEnabled(!mbEditingTruck);
        jbTruckMoveUp.setEnabled(!mbEditingTruck);
        jbTruckMoveDown.setEnabled(!mbEditingTruck);

        jbTruckOk.setEnabled(mbEditingTruck);
        jbTruckCancel.setEnabled(mbEditingTruck);
        
        moGridTrucks.getTable().setEnabled(!mbEditingTruck);
        moGridTrucks.getTable().getTableHeader().setEnabled(!mbEditingTruck);
        
        // clear or refresh data:
        
        switch (action) { // act on original parameter
            case ACTION_ADD:
                renderBolTruck(moBolTruck); // local registry already set
                break;
            case ACTION_CREATE:
                renderBolTruck(null);
                // default values:
                moYearTruckModel.setValue(DLibTimeUtils.digestYear(new Date())[0]);
                break;
            case ACTION_COPY:
            case ACTION_MODIFY:
            case ACTION_CANCEL:
                valueChangedTruck(); // renders current grid row
                break;
            default:
                // nothing
        }
        
        // propagate edition state to dependents:
        
        editBolTruckTrailer(false, mbEditingTruck ? ACTION_CANCEL : 0);
        if (action == 0) { // check original parameter
            jbTruckTrailAdd.setEnabled(false);
            jbTruckTrailCreate.setEnabled(false);
            jbTruckTrailModify.setEnabled(false);
            jbTruckTrailRemove.setEnabled(false);
            jbTruckTrailOk.setEnabled(false);
            jbTruckTrailCancel.setEnabled(false);
        }
        
        // set edition status appropriately into form:
        
        boolean updatable = mbEditingTruck && moBolTruck != null && moBolTruck.getOwnTruck() != null && !moBolTruck.getOwnTruck().isRegistryNew();
        
        moBoolTruckUpdate.setEnabled(updatable);
        
        if (updatable) {
            moKeyTruckTransportConfig.setEnabled(false);
            jbTruckEditConfig.setEnabled(true);
        }
        
        if (action != 0) { // check original parameter
            enableBolNavButtons(mbEditingTruck ? 0 : NAV_ACTION_START);
            
            if (mbEditingTruck) {
                if (moKeyTruckTransportConfig.isEnabled()) {
                    moKeyTruckTransportConfig.requestFocusInWindow();
                }
                else {
                    moTextTruckName.requestFocusInWindow();
                }
            }
            else {
                jbTruckAdd.requestFocusInWindow();
            }
        }
    }

    private void editBolTruckTrailer(final boolean editing, final int action) {
        mbEditingTruckTrailer = editing;
        
        // CRUD action:
        
        if (!mbEditingTruckTrailer) {
            mnActionTruckTrailer = 0;
            jtfTruckTrailCrud.setText("");
        }
        else {
            mnActionTruckTrailer = action;
            jtfTruckTrailCrud.setText(getCrudText(action));
            jtfTruckTrailCrud.setCaretPosition(0);
        }
        
        // status of fields and input controls:
        
        moKeyTruckTrailSubtype.setEnabled(mbEditingTruckTrailer);
        jbTruckTrailEditSubtype.setEnabled(false);
        itemStateChangedTruckTrailSubtype();
        moTextTruckTrailPlate.setEnabled(mbEditingTruckTrailer);
        
        // status of CRUD controls:
        
        jbTruckTrailAdd.setEnabled(mbEditingTruck && !mbEditingTruckTrailer);
        jbTruckTrailCreate.setEnabled(mbEditingTruck && !mbEditingTruckTrailer);
        jbTruckTrailModify.setEnabled(mbEditingTruck && !mbEditingTruckTrailer);
        jbTruckTrailRemove.setEnabled(mbEditingTruck && !mbEditingTruckTrailer);

        jbTruckTrailOk.setEnabled(mbEditingTruck && mbEditingTruckTrailer);
        jbTruckTrailCancel.setEnabled(mbEditingTruck && mbEditingTruckTrailer);
        
        moGridTrucksTrailers.getTable().setEnabled(mbEditingTruck && !mbEditingTruckTrailer);
        moGridTrucksTrailers.getTable().getTableHeader().setEnabled(mbEditingTruck && !mbEditingTruckTrailer);

        // clear or refresh data:
        
        switch (action) { // act on original parameter
            case ACTION_ADD:
                renderBolTruckTrailer(moBolTruckTrailer); // local registry already set
                break;
            case ACTION_CREATE:
                renderBolTruckTrailer(null);
                break;
            case ACTION_COPY:
            case ACTION_MODIFY:
            case ACTION_CANCEL:
                valueChangedTruckTrail(); // renders current grid row
                break;
            default:
                // nothing
        }
        
        // propagate edition state to precedents:
        
        if (mbEditingTruckTrailer) {
            jbTruckOk.setEnabled(false);
            jbTruckCancel.setEnabled(false);
        }
        else {
            jbTruckOk.setEnabled(mbEditingTruck);
            jbTruckCancel.setEnabled(mbEditingTruck);
        }
        
        // set edition status appropriately into form:
        
        boolean updatable = mbEditingTruckTrailer && moBolTruckTrailer != null && moBolTruckTrailer.getOwnTrailer() != null && !moBolTruckTrailer.getOwnTrailer().isRegistryNew();
        
        moBoolTruckTrailUpdate.setEnabled(updatable);
        
        if (updatable) {
            moKeyTruckTrailSubtype.setEnabled(false);
            jbTruckTrailEditSubtype.setEnabled(true);
        }
        
        if (action != 0) { // check original parameter
            //enableBolNavButtons(mbEditingTruckTrailer ? 0 : NAV_ACTION_START); // does not interfere with BOL navigation
            
            if (mbEditingTruckTrailer) {
                if (moKeyTruckTrailSubtype.isEnabled()) {
                    moKeyTruckTrailSubtype.requestFocusInWindow();
                }
                else {
                    moTextTruckTrailPlate.requestFocus();
                }
            }
            else {
                jbTruckTrailAdd.requestFocusInWindow();
            }
        }
    }

    /**
     * Render grid rows. It is needed public in order to be invoked by reflection.
     */
    public void refreshBolTrucks() {
        int sortingPos = 0;
        
        for (DGridRow row : moGridTrucks.getModel().getGridRows()) {
            ((DDbBolTruck) row).setBolSortingPos(++sortingPos);
        }
        
        moGridTrucks.renderGridRows();
    }

    /**
     * Render grid rows. It is needed public in order to be invoked by reflection.
     */
    public void refreshBolTrucksTrailers() {
        int sortingPos = 0;
        
        for (DGridRow row : moGridTrucksTrailers.getModel().getGridRows()) {
            ((DDbBolTruckTrailer) row).setBolSortingPos(++sortingPos);
        }
        
        moGridTrucksTrailers.renderGridRows();
    }
    
    private void actionPerformedTruckEditConfig() {
        if (mbEditingTruck && miClient.showMsgBoxConfirm("¿Está seguro que desea cambiar la configuración del autotransporte?") == JOptionPane.YES_OPTION) {
            jbTruckEditConfig.setEnabled(false);
            moKeyTruckTransportConfig.setEnabled(true);
            moKeyTruckTransportConfig.requestFocusInWindow();
        }
    }
    
    private void actionPerformedTruckGetNextCode() {
        try {
            if (moTextTruckCode.getValue().isEmpty() || 
                    miClient.showMsgBoxConfirm("¿Está seguro que desea obtener el siguiente código para el campo '" + DGuiUtils.getLabelName(jlTruckCode) + "'?") == JOptionPane.YES_OPTION) {
                int nextCode = DFormBolUtils.getNextCode(miClient.getSession(), DModConsts.LU_TRUCK, 0);
                moTextTruckCode.setValue("" + nextCode);
                moTextTruckCode.requestFocusInWindow();
            }
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedTruckSetWeightGrossTon() {
        moDecTruckWeightGrossTon.setValue(DLibUtils.roundAmount(moDecTruckWeightTon.getValue() + DLibUtils.roundAmount((mdBolMerchandiseWeightKg / 1000d)))); // 2 decimals attributes
        moDecTruckWeightGrossTon.requestFocusInWindow();
    }
    
    private void actionPerformedTruckAdd() {
        if (canBolTruckAdd()) {
            DDbBolTruck bolTruck = createBolTruckPickingOne();

            if (bolTruck != null) {
                moBolTruck = bolTruck;
                editBolTruck(true, ACTION_ADD);
            }
        }
    }
    
    private void actionPerformedTruckCreate() {
        if (canBolTruckAppend()) {
            editBolTruck(true, ACTION_CREATE);
        }
    }

    private void actionPerformedTruckCopy() {
        if (canBolTruckAppend()) {
            if (moGridTrucks.getSelectedGridRow() == null) {
                miClient.showMsgBoxWarning(DGridConsts.MSG_SELECT_ROW);
            }
            else {
                editBolTruck(true, ACTION_COPY);
            }
        }
    }

    private void actionPerformedTruckModify() {
        if (moGridTrucks.getSelectedGridRow() == null) {
            miClient.showMsgBoxWarning(DGridConsts.MSG_SELECT_ROW);
        }
        else {
            editBolTruck(true, ACTION_MODIFY);
        }
    }

    private void actionPerformedTruckRemove() {
        try {
            actionGridRemove(moGridTrucks, this.getClass().getMethod("refreshBolTrucks"));
            computeBol();
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }

    private void actionPerformedTruckMoveUp() {
        try {
            actionGridMoveUp(moGridTrucks, this.getClass().getMethod("refreshBolTrucks"));
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }

    private void actionPerformedTruckMoveDown() {
        try {
            actionGridMoveDown(moGridTrucks, this.getClass().getMethod("refreshBolTrucks"));
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }

    private void actionPerformedTruckOk() {
        DGuiValidation validation = moFieldsTruck.validateFields();
        
        if (validation.isValid()) {
            double truckWeightGrossTon = DDbBolTruck.calculateWeigthGrossTon(moDecTruckWeightTon.getValue(), mdBolMerchandiseWeightKg);
                    
            if (DLibUtils.compareAmount(moDecTruckWeightGrossTon.getValue(), moDecTruckWeightTon.getValue()) && mdBolMerchandiseWeightKg != 0) { // 2 decimals attributes
                if (miClient.showMsgBoxConfirm("¿Está seguro que los valores de los campos '" + moDecTruckWeightGrossTon.getFieldName() + "' y '" + moDecTruckWeightTon.getFieldName() + "' "
                        + "sean iguales a pesar de que el valor de '" + DGuiUtils.getLabelName(jlBolMerchandiseWeightKg) + "' es " + DLibUtils.getDecimalFormatAmount().format(mdBolMerchandiseWeightKg) + " " + jlBolMerchandiseWeightKgUnit.getText() + "?") != JOptionPane.YES_OPTION) {
                    validation.setMessage(DGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + moDecTruckWeightGrossTon.getFieldName() + "'" + DGuiConsts.ERR_MSG_FIELD_VAL_GREAT + DLibUtils.getDecimalFormatAmount().format(moDecTruckWeightTon.getValue()) + " " + jlTruckWeightTonUnit.getText() + ", "
                            + "idealmente igual a " + DLibUtils.getDecimalFormatAmount().format(truckWeightGrossTon) + " " + jlTruckWeightGrossTonUnit.getText() + ".");
                    validation.setComponent(moDecTruckWeightGrossTon);
                }
            }
            else if (!DLibUtils.compareAmount(moDecTruckWeightGrossTon.getValue(), truckWeightGrossTon)) { // 2 decimals attributes
                if (miClient.showMsgBoxConfirm("¿Está seguro que el valor del campo '" + moDecTruckWeightGrossTon.getFieldName() + "' sea " + DLibUtils.getDecimalFormatAmount().format(moDecTruckWeightGrossTon.getValue()) + " " + jlTruckWeightGrossTonUnit.getText() + "?, "
                        + "porque debería ser igual a " + DLibUtils.getDecimalFormatAmount().format(truckWeightGrossTon) + " " + jlTruckWeightGrossTonUnit.getText() + ".") != JOptionPane.YES_OPTION) {
                    validation.setMessage(DGuiConsts.ERR_MSG_FIELD_VAL_ + "'" + moDecTruckWeightGrossTon.getFieldName() + "'" + DGuiConsts.ERR_MSG_FIELD_VAL_EQUAL + DLibUtils.getDecimalFormatAmount().format(truckWeightGrossTon) + " " + jlTruckWeightGrossTonUnit.getText() + ".");
                    validation.setComponent(moDecTruckWeightGrossTon);
                }
            }
            
            if (validation.isValid()) {
                String configCode = moKeyTruckTransportConfig.getSelectedItem().getCode();

                switch (mnTruckIsTrailerRequired) {
                    case DXmlCatalogEntry.REQUIRED_NO:
                        if (moGridTrucksTrailers.getTable().getRowCount() > 0) {
                            validation.setMessage("La configuración del autotransporte '" + configCode + "' no requiere de remolques.");
                            validation.setComponent(jbTruckTrailCreate);
                        }
                        break;
                    case DXmlCatalogEntry.REQUIRED_YES:
                        if (moGridTrucksTrailers.getTable().getRowCount() == 0) {
                            validation.setMessage("La configuración del autotransporte '" + configCode + "' requiere de remolques.");
                            validation.setComponent(jbTruckTrailCreate);
                        }
                        break;
                    case DXmlCatalogEntry.REQUIRED_OPT:
                        if (moGridTrucksTrailers.getTable().getRowCount() == 0) {
                            if (!configCode.equals(DCfdi40Catalogs.CcpConfigAutotransporteVl) && miClient.showMsgBoxConfirm(
                                    "La configuración del autotransporte '" + configCode + "' permite tener de remolques, pero no los hay.\n" + DGuiConsts.MSG_CNF_CONT) != JOptionPane.YES_OPTION) {
                                validation.setMessage("Agregar los remolques que se requieran.");
                                validation.setComponent(jbTruckTrailCreate);
                            }
                        }
                        break;
                    default:
                        // nothing
                }
            }
        }
        
        if (DGuiUtils.computeValidation(miClient, validation)) {
            boolean adding = mnActionTruck == ACTION_ADD; // convenience variable
            boolean creating = mnActionTruck == ACTION_CREATE || mnActionTruck == ACTION_COPY; // convenience variable
            DDbTruck truck = null;
            
            if (creating) {
                moBolTruck = new DDbBolTruck();
                truck = new DDbTruck();
                moBolTruck.setOwnTruck(truck, mdBolMerchandiseWeightKg);
            }
            
            truck = moBolTruck.getOwnTruck();
            
            //truck.setPkTruckId(...);
            truck.setCode(moTextTruckCode.getValue());
            truck.setName(moTextTruckName.getValue());
            truck.setWeightTon(moDecTruckWeightTon.getValue());
            truck.setPlate(moTextTruckPlate.getValue());
            truck.setModel(moYearTruckModel.getValue());
            DGuiItem guiItem1 = moKeyTruckTransportConfig.getSelectedItem();
            String[] codeAndName1 = guiItem1.getItem().split(" - ");
            truck.setTransportConfigCode(guiItem1.getCode());
            truck.setTransportConfigName(codeAndName1[1]);
            DGuiItem guiItem2 = moKeyTruckPermissionType.getSelectedItem();
            String[] codeAndName2 = guiItem2.getItem().split(" - ");
            truck.setPermissionTypeCode(guiItem2.getCode());
            truck.setPermissionTypeName(codeAndName2[1]);
            truck.setPermissionNumber(moTextTruckPermissionNumber.getValue());
            truck.setCivilInsurance(moTextTruckCivilInsurance.getValue());
            truck.setCivilPolicy(moTextTruckCivilPolicy.getValue());
            truck.setEnvironmentInsurance(moTextTruckEnvironmentInsurance.getValue());
            truck.setEnvironmentPolicy(moTextTruckEnvironmentPolicy.getValue());
            truck.setCargoInsurance(moTextTruckCargoInsurance.getValue());
            truck.setCargoPolicy(moTextTruckCargoPolicy.getValue());
            truck.setPrime(moCurTruckPrime.getField().getValue());
            
            truck.setRegistryEdited(true);
            
            moBolTruck.updateFromOwnTruck(mdBolMerchandiseWeightKg);
            
            //moBolTruck.setPkBolId(...);
            //moBolTruck.setPkTruckId(...); // updated already by DDbBolTruck.updateFromOwnTruck()
            //moBolTruck.setWeightTon(...); // updated already by DDbBolTruck.updateFromOwnTruck()
            //moBolTruck.setPlate(...); // updated already by DDbBolTruck.updateFromOwnTruck()
            //moBolTruck.setModel(...); // updated already by DDbBolTruck.updateFromOwnTruck()
            //moBolTruck.setTransportConfigCode(...); // updated already by DDbBolTruck.updateFromOwnTruck()
            //moBolTruck.setTransportConfigName(...); // updated already by DDbBolTruck.updateFromOwnTruck()
            //moBolTruck.setPermissionTypeCode(...); // updated already by DDbBolTruck.updateFromOwnTruck()
            //moBolTruck.setPermissionTypeName(...); // updated already by DDbBolTruck.updateFromOwnTruck()
            //moBolTruck.setPermissionNumber(...); // updated already by DDbBolTruck.updateFromOwnTruck()
            //moBolTruck.setCivilInsurance(...); // updated already by DDbBolTruck.updateFromOwnTruck()
            //moBolTruck.setCivilPolicy(...); // updated already by DDbBolTruck.updateFromOwnTruck()
            //moBolTruck.setEnvironmentInsurance(...); // updated already by DDbBolTruck.updateFromOwnTruck()
            //moBolTruck.setEnvironmentPolicy(...); // updated already by DDbBolTruck.updateFromOwnTruck()
            //moBolTruck.setCargoInsurance(...); // updated already by DDbBolTruck.updateFromOwnTruck()
            //moBolTruck.setCargoPolicy(...); // updated already by DDbBolTruck.updateFromOwnTruck()
            //moBolTruck.setPrime(...); // updated already by DDbBolTruck.updateFromOwnTruck()
            //moBolTruck.setFkTruckId(...); // updated already by DDbBolTruck.updateFromOwnTruck()

            moBolTruck.getChildTrailers().clear();
            for (DGridRow row : moGridTrucksTrailers.getModel().getGridRows()) {
                moBolTruck.getChildTrailers().add((DDbBolTruckTrailer) row);
            }
            
            //moBolTruck.setOwnTruck(...); // already set above
            
            // update own registry:
            
            moBolTruck.setBolUpdateOwnRegistry(moBoolTruckUpdate.getValue());
            
            // update grid:

            if (creating || adding) {
                moGridTrucks.addGridRow(moBolTruck);
            }
            
            int row = moGridTrucks.getModel().getGridRows().indexOf(moBolTruck); // get row before refreshing grid!
            refreshBolTrucks();
            moGridTrucks.setSelectedGridRow(moGridTrucks.getTable().convertRowIndexToView(row));
            
            if (adding) {
                // add as well all truck's transport figures:
                
                moGridTptFigures.clearGridRows();

                int sortingPos1 = 0;
                for (DDbTruckTransportFigure truckTptFigure : truck.getChildTransportFigures()) {
                    DDbTransportFigure tptFigure = (DDbTransportFigure) miClient.getSession().readRegistry(DModConsts.LU_TPT_FIGURE, new int[] { truckTptFigure.getFkTransportFigureId() });
                    DDbBolTransportFigure bolTptFigure = new DDbBolTransportFigure();
                    bolTptFigure.setDbmsTransportFigureTypeCode((String) miClient.getSession().readField(DModConsts.LS_TPT_FIGURE_TP, new int[] { tptFigure.getFkTransportFigureTypeId() }, DDbRegistry.FIELD_CODE));
                    bolTptFigure.setDbmsTransportFigureTypeName((String) miClient.getSession().readField(DModConsts.LS_TPT_FIGURE_TP, new int[] { tptFigure.getFkTransportFigureTypeId() }, DDbRegistry.FIELD_NAME));
                    bolTptFigure.setOwnTransportFigure(tptFigure);
                    bolTptFigure.setBolSortingPos(++sortingPos1);
                    
                    int sortingPos2 = 0;
                    for (DDbTruckTransportFigureTransportPart truckTptFigureTptPart : truckTptFigure.getChildTransportParts()) {
                        DDbSysTransportPartType sysTptPartType = (DDbSysTransportPartType) miClient.getSession().readRegistry(DModConsts.LS_TPT_PART_TP, new int[] { truckTptFigureTptPart.getFkTransportPartTypeId() });
                        DDbBolTransportFigureTransportPart tptFigureTptPart = new DDbBolTransportFigureTransportPart();
                        tptFigureTptPart.setOwnSysTransportPartType(sysTptPartType);
                        tptFigureTptPart.setBolSortingPos(++sortingPos2);
                        
                        bolTptFigure.getChildTransportParts().add(tptFigureTptPart);
                    }
                    
                    moGridTptFigures.addGridRow(bolTptFigure);
                }
                
                moGridTptFigures.setSelectedGridRow(0);
            }
            
            actionPerformedTruckCancel();
        }
    }

    private void actionPerformedTruckCancel() {
        editBolTruck(false, ACTION_CANCEL);
    }
    
    private void actionPerformedTruckTrailEditSubtype() {
        if (mbEditingTruckTrailer && miClient.showMsgBoxConfirm("¿Está seguro que desea cambiar el subtipo del remolque?") == JOptionPane.YES_OPTION) {
            jbTruckTrailEditSubtype.setEnabled(false);
            moKeyTruckTrailSubtype.setEnabled(true);
            moKeyTruckTrailSubtype.requestFocusInWindow();
        }
    }
    
    private void actionPerformedTruckTrailAdd() {
        if (canBolTruckTrailerAppend()) {
            DDbBolTruckTrailer bolTruckTrailer = createBolTruckTrailerPickingOne();

            if (bolTruckTrailer != null) {
                moBolTruckTrailer = bolTruckTrailer;
                editBolTruckTrailer(true, ACTION_ADD);
            }
        }
    }
    
    private void actionPerformedTruckTrailCreate() {
        if (canBolTruckTrailerAppend()) {
            editBolTruckTrailer(true, ACTION_CREATE);
        }
    }

    private void actionPerformedTruckTrailModify() {
        if (moGridTrucksTrailers.getSelectedGridRow() == null) {
            miClient.showMsgBoxWarning(DGridConsts.MSG_SELECT_ROW);
        }
        else {
            editBolTruckTrailer(true, ACTION_MODIFY);
        }
    }

    private void actionPerformedTruckTrailRemove() {
        try {
            actionGridRemove(moGridTrucksTrailers, this.getClass().getMethod("refreshBolTrucksTrailers"));
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }

    private void actionPerformedTruckTrailOk() {
        DGuiValidation validation = moFieldsTruckTrailer.validateFields();
        
        if (DGuiUtils.computeValidation(miClient, validation)) {
            boolean adding = mnActionTruckTrailer == ACTION_ADD; // convenience variable
            boolean creating = mnActionTruckTrailer == ACTION_CREATE || mnActionTruckTrailer == ACTION_COPY; // convenience variable
            DDbTrailer trailer = null;
            
            if (creating) {
                moBolTruckTrailer = new DDbBolTruckTrailer();
                trailer = new DDbTrailer();
                moBolTruckTrailer.setOwnTrailer(trailer);
            }
            
            trailer = moBolTruckTrailer.getOwnTrailer();
            
            //trailer.setPkTrailerId(...);
            trailer.setCode(moTextTruckTrailPlate.getValue()); // by now, plate serves as name
            trailer.setName(moTextTruckTrailPlate.getValue()); // by now, plate serves as code
            trailer.setPlate(moTextTruckTrailPlate.getValue());
            DGuiItem guiItem = moKeyTruckTrailSubtype.getSelectedItem();
            String[] codeAndName = guiItem.getItem().split(" - ");
            trailer.setTrailerSubtypeCode(guiItem.getCode());
            trailer.setTrailerSubtypeName(codeAndName[1]);
            
            trailer.setRegistryEdited(true);
            
            moBolTruckTrailer.updateFromOwnTrailer();
        
            //moBolTruckTrailer.setPkBolId(...);
            //moBolTruckTrailer.setPkTruckId(...);
            //moBolTruckTrailer.setPkTrailerId(...);
            //moBolTruckTrailer.setPlate(...); // updated already by DDbBolTruckTrailer.updateFromOwnTrailer()
            //moBolTruckTrailer.setTrailerSubtypeCode(...); // updated already by DDbBolTruckTrailer.updateFromOwnTrailer()
            //moBolTruckTrailer.setTrailerSubtypeName(...); // updated already by DDbBolTruckTrailer.updateFromOwnTrailer()
            //moBolTruckTrailer.setFkTrailerId(...); // updated already by DDbBolTruckTrailer.updateFromOwnTrailer()

            //moBolTruckTrailer.setOwnTrailer(...); // already set above
            
            // update own registry:
            
            moBolTruckTrailer.setBolUpdateOwnRegistry(moBoolTruckTrailUpdate.getValue());

            // update grid:

            if (creating || adding) {
                moGridTrucksTrailers.addGridRow(moBolTruckTrailer);
            }
            
            int row = moGridTrucksTrailers.getModel().getGridRows().indexOf(moBolTruckTrailer); // get row before refreshing grid!
            refreshBolTrucksTrailers();
            moGridTrucksTrailers.setSelectedGridRow(moGridTrucksTrailers.getTable().convertRowIndexToView(row));
            
            actionPerformedTruckTrailCancel();
        }
    }

    private void actionPerformedTruckTrailCancel() {
        editBolTruckTrailer(false, ACTION_CANCEL);
    }
    
    @SuppressWarnings("unchecked")
    private void itemStateChangedTruckTransportConfig() {
        if (moKeyTruckTransportConfig.getSelectedIndex() <= 0) {
            moKeyTruckTransportConfig.setToolTipText(null);
            
            mnTruckIsTrailerRequired = 0;
            jtfTruckTrailIsNeeded.setText("");
        }
        else {
            moKeyTruckTransportConfig.setToolTipText(moKeyTruckTransportConfig.getSelectedItem().getItem());
            
            switch (((HashMap<String, String>) moKeyTruckTransportConfig.getSelectedItem().getComplement()).get(DFormBolUtils.ATT_TRAILER)) {
                case DXmlCatalogEntry.VAL_REQUIRED_NO:
                    mnTruckIsTrailerRequired = DXmlCatalogEntry.REQUIRED_NO;
                    jtfTruckTrailIsNeeded.setText(DXmlCatalogEntry.TXT_REQUIRED_NO);
                    break;
                case DXmlCatalogEntry.VAL_REQUIRED_YES:
                    mnTruckIsTrailerRequired = DXmlCatalogEntry.REQUIRED_YES;
                    jtfTruckTrailIsNeeded.setText(DXmlCatalogEntry.TXT_REQUIRED_YES);
                    break;
                case DXmlCatalogEntry.VAL_REQUIRED_OPT:
                    mnTruckIsTrailerRequired = DXmlCatalogEntry.REQUIRED_OPT;
                    jtfTruckTrailIsNeeded.setText(DXmlCatalogEntry.TXT_REQUIRED_OPT);
                    break;
                default:
                    mnTruckIsTrailerRequired = 0;
                    jtfTruckTrailIsNeeded.setText(TXT_UNKNOWN);
            }
            
            jtfTruckTrailIsNeeded.setCaretPosition(0);
        }
    }
    
    private void itemStateChangedTruckPermissionType() {
        if (moKeyTruckPermissionType.getSelectedIndex() <= 0) {
            moKeyTruckPermissionType.setToolTipText(null);
        }
        else {
            moKeyTruckPermissionType.setToolTipText(moKeyTruckPermissionType.getSelectedItem().getItem());
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

    private void valueChangedTruck() {
        renderBolTruck((DDbBolTruck) moGridTrucks.getSelectedGridRow());
    }

    private void valueChangedTruckTrail() {
        renderBolTruckTrailer((DDbBolTruckTrailer) moGridTrucksTrailers.getSelectedGridRow());
    }
    
    /*
     * Transport figures
     */

    private DDbBolTransportFigure createBolTptFigurePickingOne() {
        DDbBolTransportFigure bolTptFigure = null;
        
        if (moPickerTptFigure == null) {
            moPickerTptFigure = new DPickerElement(miClient, DModConsts.LU_TPT_FIGURE);
        }
        
        moPickerTptFigure.resetForm();
        moPickerTptFigure.setVisible(true);

        if (moPickerTptFigure.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
            int[] key = (int[]) moPickerTptFigure.getValue(DPickerElement.ELEMENT);
            
            if (key != null) {
                bolTptFigure = new DDbBolTransportFigure();
                bolTptFigure.setOwnTransportFigure((DDbTransportFigure) miClient.getSession().readRegistry(DModConsts.LU_TPT_FIGURE, key));
            }
        }
        
        return bolTptFigure;
    }

    private DDbBolTransportFigureTransportPart createBolTptFigureTptPartPickingOne() {
        DDbBolTransportFigureTransportPart bolTptFigureTptPart = null;
        
        if (moPickerTptPartType == null) {
            moPickerTptPartType = new DPickerElement(miClient, DModConsts.LS_TPT_PART_TP);
        }
        
        moPickerTptPartType.resetForm();
        moPickerTptPartType.setVisible(true);

        if (moPickerTptPartType.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
            int[] key = (int[]) moPickerTptPartType.getValue(DPickerElement.ELEMENT);
            
            if (key != null) {
                bolTptFigureTptPart = new DDbBolTransportFigureTransportPart();
                bolTptFigureTptPart.setOwnSysTransportPartType((DDbSysTransportPartType) miClient.getSession().readRegistry(DModConsts.LS_TPT_PART_TP, key));
            }
        }
        
        return bolTptFigureTptPart;
    }

    private boolean canBolTptFigureTptPartAppend() {
        boolean can = mbTptFigureTptPartIsRequired;
        
        if (!can) {
            miClient.showMsgBoxWarning("No se requieren partes de transporte.");
        }
        
        return can;
    }

    private void renderBolTptFigure(final DDbBolTransportFigure bolTptFigure) {
        moBolTptFigure = bolTptFigure;
        moGridTptFiguresTptParts.clearGridRows();
        
        if (moBolTptFigure == null) {
            moFieldsTptFigure.resetFields();
            
            jtfTptFigPk.setText("");
            
            moBoolTptFigUpdate.setValue(true);
        }
        else {
            moKeyTptFigTransportFigureType.setValue(new int[] { moBolTptFigure.getFkTransportFigureTypeId() });
            itemStateChangedTptFigTransportFigureType();
            moTextTptFigName.setValue(moBolTptFigure.getOwnTransportFigure().getName());
            moTextTptFigCode.setValue(moBolTptFigure.getOwnTransportFigure().getCode());
            moTextTptFigMail.setValue(moBolTptFigure.getOwnTransportFigure().getMail());
            moTextTptFigFiscalId.setValue(moBolTptFigure.getFiscalId());
            moTextTptFigDriverLicense.setValue(moBolTptFigure.getDriverLicense());
            moKeyTptFigFigureCountry.setValue(new int[] { moBolTptFigure.getFkFigureCountryId() });
            itemStateChangedTptFigFigureCountry();
            moTextTptFigForeignId.setValue(moBolTptFigure.getForeignId());

            moKeyTptFigAddressCountry.setValue(new int[] { moBolTptFigure.getFkAddressCountryId() });
            itemStateChangedTptFigAddressCountry();
            moTextTptFigAddressStateCode.setValue(moBolTptFigure.getAddressStateCode());
            moTextTptFigAddressStateName.setValue(moBolTptFigure.getAddressStateName());
            moTextTptFigAddressCountyCode.setValue(moBolTptFigure.getAddressCountyCode());
            moTextTptFigAddressCountyName.setValue(moBolTptFigure.getAddressCountyName());
            moTextTptFigAddressLocalityCode.setValue(moBolTptFigure.getAddressLocalityCode());
            moTextTptFigAddressLocalityName.setValue(moBolTptFigure.getAddressLocalityName());
            moTextTptFigAddressZipCode.setValue(moBolTptFigure.getAddressZipCode());
            moTextTptFigAddressDistrictCode.setValue(moBolTptFigure.getAddressDistrictCode());
            moTextTptFigAddressDistrictName.setValue(moBolTptFigure.getAddressDistrictName());
            moTextTptFigAddressStreet.setValue(moBolTptFigure.getAddressStreet());
            moTextTptFigAddressNumberExt.setValue(moBolTptFigure.getAddressNumberExt());
            moTextTptFigAddressNumberInt.setValue(moBolTptFigure.getAddressNumberInt());
            moTextTptFigAddressReference.setValue(moBolTptFigure.getAddressReference());
            
            jtfTptFigPk.setText(DLibUtils.textKey(moBolTptFigure.getPrimaryKey()));
            jtfTptFigPk.setCaretPosition(0);
            
            moBoolTptFigUpdate.setValue(moBolTptFigure.getOwnTransportFigure() == null || moBolTptFigure.getOwnTransportFigure().isRegistryNew()); // update only when requested
            
            // dependents:
            
            for (DDbBolTransportFigureTransportPart tptPart : moBolTptFigure.getChildTransportParts()) {
                moGridTptFiguresTptParts.addGridRow(tptPart);
            }
            moGridTptFiguresTptParts.setSelectedGridRow(0);
        }
    }
    
    private void renderBolTptFigureTptPart(final DDbBolTransportFigureTransportPart bolTptFigureTptPart) {
        moBolTptFigureTptPart = bolTptFigureTptPart;
        
        if (moBolTptFigureTptPart == null) {
            moFieldsTptFigureTptPart.resetFields();
            
            jtfTptFigTptPartPk.setText("");
            
            moBoolTptFigTptPartUpdate.setValue(false/*true*/);
        }
        else {
            moKeyTptFigTptPartTransportPartType.setValue(new int[] { moBolTptFigureTptPart.getFkTransportPartTypeId() });
            
            jtfTptFigTptPartPk.setText(DLibUtils.textKey(moBolTptFigureTptPart.getPrimaryKey()));
            jtfTptFigTptPartPk.setCaretPosition(0);
            
            moBoolTptFigTptPartUpdate.setValue(false/*moBolTptFigureTptPart.getOwnSysTransportPartType() == null || moBolTptFigureTptPart.getOwnSysTransportPartType().isRegistryNew()*/); // update only when requested
        }
    }
    
    private void editBolTptFigure(final boolean editing, final int action) {
        mbEditingTptFigure = editing;
        
        // CRUD action:
        
        if (!mbEditingTptFigure) {
            mnActionTptFigure = 0;
            jtfTptFigCrud.setText("");
        }
        else {
            mnActionTptFigure = action;
            jtfTptFigCrud.setText(getCrudText(action));
            jtfTptFigCrud.setCaretPosition(0);
        }
        
        // status of fields and input controls:
        
        moKeyTptFigTransportFigureType.setEnabled(mbEditingTptFigure);
        jbTptFigEditType.setEnabled(false);
        itemStateChangedTptFigTransportFigureType();
        moTextTptFigName.setEnabled(mbEditingTptFigure);
        moTextTptFigCode.setEnabled(mbEditingTptFigure);
        moTextTptFigMail.setEnabled(mbEditingTptFigure);
        jbTptFigGetNextCode.setEnabled(mbEditingTptFigure);
        moTextTptFigFiscalId.setEnabled(mbEditingTptFigure);
        //moTextTptFigDriverLicense.setEnabled(...); // depends on itemStateChangedTptFigTransportFigureType()
        moKeyTptFigFigureCountry.setEnabled(mbEditingTptFigure);
        itemStateChangedTptFigFigureCountry();
        //moTextTptFigForeignId.setEnabled(...); // depends on itemStateChangedTptFigFigureCountry()

        moKeyTptFigAddressCountry.setEnabled(mbEditingTptFigure);
        itemStateChangedTptFigAddressCountry();
        //moTextTptFigAddressStateCode.setEnabled(...); // depends on itemStateChangedTptFigAddressCountry()
        //moTextTptFigAddressStateName.setEnabled(...); // depends on itemStateChangedTptFigAddressCountry()
        //moTextTptFigAddressCountyCode.setEnabled(...); // depends on itemStateChangedTptFigAddressCountry()
        //moTextTptFigAddressCountyName.setEnabled(...); // depends on itemStateChangedTptFigAddressCountry()
        //moTextTptFigAddressLocalityCode.setEnabled(...); // depends on itemStateChangedTptFigAddressCountry()
        //moTextTptFigAddressLocalityName.setEnabled(...); // depends on itemStateChangedTptFigAddressCountry()
        //moTextTptFigAddressZipCode.setEnabled(mbEditingLocation); // depends on itemStateChangedTptFigAddressCountry()
        //moTextTptFigAddressDistrictCode.setEnabled(...); // depends on itemStateChangedTptFigAddressCountry()
        //moTextTptFigAddressDistrictName.setEnabled(...); // depends on itemStateChangedTptFigAddressCountry()
        //moTextTptFigAddressStreet.setEnabled(mbEditingLocation); // depends on itemStateChangedTptFigAddressCountry()
        //moTextTptFigAddressNumberExt.setEnabled(mbEditingLocation); // depends on itemStateChangedTptFigAddressCountry()
        //moTextTptFigAddressNumberInt.setEnabled(mbEditingLocation); // depends on itemStateChangedTptFigAddressCountry()
        //moTextTptFigAddressReference.setEnabled(mbEditingLocation); // depends on itemStateChangedTptFigAddressCountry()
        
        // status of CRUD controls:
        
        jbTptFigAdd.setEnabled(!mbEditingTptFigure);
        jbTptFigCreate.setEnabled(!mbEditingTptFigure);
        jbTptFigCopy.setEnabled(!mbEditingTptFigure);
        jbTptFigModify.setEnabled(!mbEditingTptFigure);
        jbTptFigRemove.setEnabled(!mbEditingTptFigure);
        jbTptFigMoveUp.setEnabled(!mbEditingTptFigure);
        jbTptFigMoveDown.setEnabled(!mbEditingTptFigure);

        jbTptFigOk.setEnabled(mbEditingTptFigure);
        jbTptFigCancel.setEnabled(mbEditingTptFigure);
        
        moGridTptFigures.getTable().setEnabled(!mbEditingTptFigure);
        moGridTptFigures.getTable().getTableHeader().setEnabled(!mbEditingTptFigure);
        
        // clear or refresh data:
        
        switch (action) { // act on original parameter
            case ACTION_ADD:
                renderBolTptFigure(moBolTptFigure); // local registry already set
                break;
            case ACTION_CREATE:
                renderBolTptFigure(null);
                break;
            case ACTION_COPY:
            case ACTION_MODIFY:
            case ACTION_CANCEL:
                valueChangedTptFig(); // renders current grid row
                break;
            default:
                // nothing
        }
        
        // propagate edition state to dependents:
        
        editBolTptFigureTptPart(false, mbEditingTptFigure ? ACTION_CANCEL : 0);
        if (action == 0) { // check original parameter
            jbTptFigTptPartAdd.setEnabled(false);
            jbTptFigTptPartCreate.setEnabled(false);
            jbTptFigTptPartModify.setEnabled(false);
            jbTptFigTptPartRemove.setEnabled(false);
            jbTptFigTptPartOk.setEnabled(false);
            jbTptFigTptPartCancel.setEnabled(false);
        }
        
        // set edition status appropriately into form:
        
        boolean updatable = mbEditingTptFigure && moBolTptFigure != null && moBolTptFigure.getOwnTransportFigure() != null && !moBolTptFigure.getOwnTransportFigure().isRegistryNew();
        
        moBoolTptFigUpdate.setEnabled(updatable);
        
        if (updatable) {
            moKeyTptFigTransportFigureType.setEnabled(false);
            jbTptFigEditType.setEnabled(true);
        }
        
        if (action != 0) { // check original parameter
            enableBolNavButtons(mbEditingTptFigure ? 0 : NAV_ACTION_START);
            
            if (mbEditingTptFigure) {
                if (moKeyTptFigTransportFigureType.isEnabled()) {
                    moKeyTptFigTransportFigureType.requestFocusInWindow();
                }
                else {
                    moTextTptFigName.requestFocusInWindow();
                }
            }
            else {
                jbTptFigAdd.requestFocusInWindow();
            }
        }
    }

    private void editBolTptFigureTptPart(final boolean editing, final int action) {
        mbEditingTptFigureTptPart = editing;
        
        // CRUD action:
        
        if (!mbEditingTptFigureTptPart) {
            mnActionTptFigureTptPart = 0;
            jtfTptFigTptPartCrud.setText("");
        }
        else {
            mnActionTptFigureTptPart = action;
            jtfTptFigTptPartCrud.setText(getCrudText(action));
            jtfTptFigTptPartCrud.setCaretPosition(0);
        }
        
        // status of fields and input controls:
        
        moKeyTptFigTptPartTransportPartType.setEnabled(mbEditingTptFigureTptPart);
        jbTptFigTptPartEditType.setEnabled(false);
        
        // status of CRUD controls:
        
        jbTptFigTptPartAdd.setEnabled(false); // adding not required
        jbTptFigTptPartCreate.setEnabled(mbEditingTptFigure && !mbEditingTptFigureTptPart);
        jbTptFigTptPartModify.setEnabled(mbEditingTptFigure && !mbEditingTptFigureTptPart);
        jbTptFigTptPartRemove.setEnabled(mbEditingTptFigure && !mbEditingTptFigureTptPart);

        jbTptFigTptPartOk.setEnabled(mbEditingTptFigure && mbEditingTptFigureTptPart);
        jbTptFigTptPartCancel.setEnabled(mbEditingTptFigure && mbEditingTptFigureTptPart);
        
        moGridTptFiguresTptParts.getTable().setEnabled(mbEditingTptFigure && !mbEditingTptFigureTptPart);
        moGridTptFiguresTptParts.getTable().getTableHeader().setEnabled(mbEditingTptFigure && !mbEditingTptFigureTptPart);

        // clear or refresh data:
        
        switch (action) { // act on original parameter
            case ACTION_ADD:
                renderBolTptFigureTptPart(moBolTptFigureTptPart); // local registry already set
                break;
            case ACTION_CREATE:
                renderBolTptFigureTptPart(null);
                break;
            case ACTION_COPY:
            case ACTION_MODIFY:
            case ACTION_CANCEL:
                valueChangedTptFigTptPart(); // renders current grid row
                break;
            default:
                // nothing
        }
        
        // propagate edition state to precedents:
        
        if (mbEditingTptFigureTptPart) {
            jbTptFigOk.setEnabled(false);
            jbTptFigCancel.setEnabled(false);
        }
        else {
            jbTptFigOk.setEnabled(mbEditingTptFigure);
            jbTptFigCancel.setEnabled(mbEditingTptFigure);
        }
        
        // set edition status appropriately into form:
        
        boolean updatable = mbEditingTptFigureTptPart && moBolTptFigureTptPart != null && moBolTptFigureTptPart.getOwnSysTransportPartType() != null && !moBolTptFigureTptPart.getOwnSysTransportPartType().isRegistryNew();
        
        moBoolTptFigTptPartUpdate.setEnabled(false/*updatable*/);
        
        if (updatable) {
            moKeyTptFigTptPartTransportPartType.setEnabled(false);
            jbTptFigTptPartEditType.setEnabled(true);
        }
        
        if (action != 0) { // check original parameter
            //enableBolNavButtons(mbEditingTptFigureTptPart ? 0 : NAV_ACTION_START); // does not interfere with BOL navigation
            
            if (mbEditingTptFigureTptPart) {
                if (moKeyTptFigTptPartTransportPartType.isEnabled()) {
                    moKeyTptFigTptPartTransportPartType.requestFocusInWindow();
                }
                else {
                    jbTptFigTptPartEditType.requestFocusInWindow();
                }
            }
            else {
                jbTptFigTptPartAdd.requestFocusInWindow();
            }
        }
    }
    
    /**
     * Render grid rows. It is needed public in order to be invoked by reflection.
     */
    public void refreshBolTptFigures() {
        int sortingPos = 0;
        
        for (DGridRow row : moGridTptFigures.getModel().getGridRows()) {
            ((DDbBolTransportFigure) row).setBolSortingPos(++sortingPos);
        }
        
        moGridTptFigures.renderGridRows();
    }

    /**
     * Render grid rows. It is needed public in order to be invoked by reflection.
     */
    public void refreshBolTptFiguresTptParts() {
        int sortingPos = 0;
        
        for (DGridRow row : moGridTptFiguresTptParts.getModel().getGridRows()) {
            ((DDbBolTransportFigureTransportPart) row).setBolSortingPos(++sortingPos);
        }
        
        moGridTptFiguresTptParts.renderGridRows();
    }
    
    private void computeTptFigAddressStateCode() {
        DFormBolUtils.computeCatalogCode(moTextTptFigAddressStateCode, moTextTptFigAddressStateName, DFormBolUtils.DEF_CODE_ADDRESS_STATE, 
                null, DCfdi40Catalogs.XML_CCP_EDO_40, false, moKeyTptFigAddressCountry, DFormBolUtils.ATT_COUNTRY);
    }
    
    private void computeTptFigAddressCountyCode() {
        DFormBolUtils.computeCatalogCode(moTextTptFigAddressCountyCode, moTextTptFigAddressCountyName, DFormBolUtils.DEF_CODE_ADDRESS_COUNTY, 
                DFormBolUtils.FormatCodeAddressCounty, DCfdi40Catalogs.XML_CCP_MUN, false, moTextTptFigAddressStateCode, DFormBolUtils.ATT_STATE);
    }

    private void computeTptFigAddressLocalityCode() {
        DFormBolUtils.computeCatalogCode(moTextTptFigAddressLocalityCode, moTextTptFigAddressLocalityName, DFormBolUtils.DEF_CODE_ADDRESS_LOCALITY, 
                DFormBolUtils.FormatCodeAddressLocality, DCfdi40Catalogs.XML_CCP_LOC, false, moTextTptFigAddressStateCode, DFormBolUtils.ATT_STATE);
    }

    private void computeTptFigAddressDistrictCode() {
        DFormBolUtils.computeCatalogCode(moTextTptFigAddressDistrictCode, moTextTptFigAddressDistrictName, DFormBolUtils.DEF_CODE_ADDRESS_DISTRICT, 
                DFormBolUtils.FormatCodeAddressDistrict, DCfdi40Catalogs.XML_CCP_COL, true, moTextTptFigAddressZipCode, DFormBolUtils.ATT_ZIP);
    }
    
    private void actionPerformedTptFigEditType() {
        if (mbEditingTptFigure && miClient.showMsgBoxConfirm("¿Está seguro que desea cambiar el tipo de la figura de transporte?") == JOptionPane.YES_OPTION) {
            jbTptFigEditType.setEnabled(false);
            moKeyTptFigTransportFigureType.setEnabled(true);
            moKeyTptFigTransportFigureType.requestFocusInWindow();
        }
    }

    private void actionPerformedTptFigGetNextCode() {
        try {
            if (moTextTptFigCode.getValue().isEmpty() || 
                    miClient.showMsgBoxConfirm("¿Está seguro que desea obtener el siguiente código para el campo '" + DGuiUtils.getLabelName(jlTptFigCode) + "'?") == JOptionPane.YES_OPTION) {
                int nextCode = DFormBolUtils.getNextCode(miClient.getSession(), DModConsts.LU_TPT_FIGURE, 0);
                moTextTptFigCode.setValue("" + nextCode);
                moTextTptFigCode.requestFocusInWindow();
            }
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }
    
    private void actionPerformedTptFigAdd() {
        DDbBolTransportFigure bolTptFigure = createBolTptFigurePickingOne();

        if (bolTptFigure != null) {
            moBolTptFigure = bolTptFigure;
            editBolTptFigure(true, ACTION_ADD);
        }
    }
    
    private void actionPerformedTptFigCreate() {
        editBolTptFigure(true, ACTION_CREATE);
    }

    private void actionPerformedTptFigCopy() {
        if (moGridTptFigures.getSelectedGridRow() == null) {
            miClient.showMsgBoxWarning(DGridConsts.MSG_SELECT_ROW);
        }
        else {
            editBolTptFigure(true, ACTION_COPY);
        }
    }

    private void actionPerformedTptFigModify() {
        if (moGridTptFigures.getSelectedGridRow() == null) {
            miClient.showMsgBoxWarning(DGridConsts.MSG_SELECT_ROW);
        }
        else {
            editBolTptFigure(true, ACTION_MODIFY);
        }
    }

    private void actionPerformedTptFigRemove() {
        try {
            actionGridRemove(moGridTptFigures, this.getClass().getMethod("refreshBolTptFigures"));
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }

    private void actionPerformedTptFigMoveUp() {
        try {
            actionGridMoveUp(moGridTptFigures, this.getClass().getMethod("refreshBolTptFigures"));
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }

    private void actionPerformedTptFigMoveDown() {
        try {
            actionGridMoveDown(moGridTptFigures, this.getClass().getMethod("refreshBolTptFigures"));
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }

    private void actionPerformedTptFigOk() {
        DGuiValidation validation = moFieldsTptFigure.validateFields();
        
        if (validation.isValid()) {
            if (moKeyTptFigFigureCountry.getSelectedIndex() > 0 && moKeyTptFigFigureCountry.getValue()[0] == DModSysConsts.CS_CTY_MEX) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + moKeyTptFigFigureCountry.getFieldName() + "'.");
                validation.setComponent(moKeyTptFigFigureCountry);
            }
            else if (moTextTptFigAddressStateCode.isEnabled() && moTextTptFigAddressStateCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_STATE)) {
                validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + moTextTptFigAddressStateCode.getFieldName() + "'.");
                validation.setComponent(moTextTptFigAddressStateCode);
            }
            else if (mbTptFigureTptPartIsRequired && moGridTptFiguresTptParts.getTable().getRowCount() == 0) {
                validation.setMessage("El tipo de figura de transporte requiere de partes de transporte.");
                validation.setComponent(jbTptFigTptPartCreate);
            }
            else if (!mbTptFigureTptPartIsRequired && moGridTptFiguresTptParts.getTable().getRowCount() > 0) {
                validation.setMessage("El tipo de figura de transporte no requiere de partes de transporte.");
                validation.setComponent(jbTptFigTptPartCreate);
            }
        }
        
        if (DGuiUtils.computeValidation(miClient, validation)) {
            boolean adding = mnActionTptFigure == ACTION_ADD; // convenience variable
            boolean creating = mnActionTptFigure == ACTION_CREATE || mnActionTptFigure == ACTION_COPY; // convenience variable
            DDbTransportFigure tptFigure = null;
            
            if (creating) {
                moBolTptFigure = new DDbBolTransportFigure();
                tptFigure = new DDbTransportFigure();
                moBolTptFigure.setOwnTransportFigure(tptFigure);
            }
            
            tptFigure = moBolTptFigure.getOwnTransportFigure(); // convenience variable
            
            //tptFigure.setPkTransportFigureId(...);
            tptFigure.setCode(moTextTptFigCode.getValue());
            tptFigure.setName(moTextTptFigName.getValue());
            tptFigure.setMail(moTextTptFigMail.getValue());
            tptFigure.setFiscalId(moTextTptFigFiscalId.getValue());
            tptFigure.setForeignId(moTextTptFigForeignId.getValue());
            tptFigure.setDriverLicense(moTextTptFigDriverLicense.getValue());
            tptFigure.setAddressStreet(moTextTptFigAddressStreet.getValue());
            tptFigure.setAddressNumberExt(moTextTptFigAddressNumberExt.getValue());
            tptFigure.setAddressNumberInt(moTextTptFigAddressNumberInt.getValue());
            tptFigure.setAddressDistrictCode(moTextTptFigAddressDistrictCode.getValue());
            tptFigure.setAddressDistrictName(moTextTptFigAddressDistrictName.getValue());
            tptFigure.setAddressLocalityCode(moTextTptFigAddressLocalityCode.getValue());
            tptFigure.setAddressLocalityName(moTextTptFigAddressLocalityName.getValue());
            tptFigure.setAddressReference(moTextTptFigAddressReference.getValue());
            tptFigure.setAddressCountyCode(moTextTptFigAddressCountyCode.getValue());
            tptFigure.setAddressCountyName(moTextTptFigAddressCountyName.getValue());
            tptFigure.setAddressStateCode(moTextTptFigAddressStateCode.getValue());
            tptFigure.setAddressStateName(moTextTptFigAddressStateName.getValue());
            tptFigure.setAddressZipCode(moTextTptFigAddressZipCode.getValue());
            //tptFigure.setUpdatable(...);
            //tptFigure.setDisableable(...);
            //tptFigure.setDeletable(...);
            //tptFigure.setDisabled(...);
            //tptFigure.setDeleted(...);
            //tptFigure.setSystem(...);
            tptFigure.setFkTransportFigureTypeId(moKeyTptFigTransportFigureType.getValue()[0]);
            tptFigure.setFkFigureCountryId(moKeyTptFigFigureCountry.getSelectedIndex() <= 0 ? DModSysConsts.CS_CTY_NA : moKeyTptFigFigureCountry.getValue()[0]);
            tptFigure.setFkAddressCountryId(moKeyTptFigAddressCountry.getValue()[0]);
            //tptFigure.setFkUserInsertId(...);
            //tptFigure.setFkUserUpdateId(...);
            //tptFigure.setTsUserInsert(...);
            //tptFigure.setTsUserUpdate(...);
            
            tptFigure.setRegistryEdited(true);

            moBolTptFigure.updateFromOwnTransportFigure();
            
            //moBolTptFigure.setPkBolId(...);
            //moBolTptFigure.setPkTransportFigureId(...);
            //moBolTptFigure.setName(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            //moBolTptFigure.setFiscalId(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            //moBolTptFigure.setForeignId(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            //moBolTptFigure.setDriverLicense(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            //moBolTptFigure.setAddressStreet(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            //moBolTptFigure.setAddressNumberExt(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            //moBolTptFigure.setAddressNumberInt(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            //moBolTptFigure.setAddressDistrictCode(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            //moBolTptFigure.setAddressDistrictName(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            //moBolTptFigure.setAddressLocalityCode(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            //moBolTptFigure.setAddressLocalityName(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            //moBolTptFigure.setAddressReference(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            //moBolTptFigure.setAddressCountyCode(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            //moBolTptFigure.setAddressCountyName(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            //moBolTptFigure.setAddressStateCode(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            //moBolTptFigure.setAddressStateName(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            //moBolTptFigure.setAddressZipCode(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            //moBolTptFigure.setFkTransportFigureTypeId(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            //moBolTptFigure.setFkTransportFigureId(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            //moBolTptFigure.setFkFigureCountryId(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            //moBolTptFigure.setFkAddressCountryId(...); // updated already by DDbBolTransportFigure.updateFromOwnTransportFigure()
            
            moBolTptFigure.getChildTransportParts().clear();
            for (DGridRow row : moGridTptFiguresTptParts.getModel().getGridRows()) {
                moBolTptFigure.getChildTransportParts().add((DDbBolTransportFigureTransportPart) row);
            }

            //moBolTptFigure.setOwnTransportFigute(...); // already set above
            
            moBolTptFigure.setDbmsTransportFigureTypeCode((String) miClient.getSession().readField(DModConsts.LS_TPT_FIGURE_TP, new int[] { moBolTptFigure.getFkTransportFigureTypeId() }, DDbRegistry.FIELD_CODE));
            moBolTptFigure.setDbmsTransportFigureTypeName((String) miClient.getSession().readField(DModConsts.LS_TPT_FIGURE_TP, new int[] { moBolTptFigure.getFkTransportFigureTypeId() }, DDbRegistry.FIELD_NAME));
            
            // update own registry:
            
            moBolTptFigure.setBolUpdateOwnRegistry(moBoolTptFigUpdate.getValue());

            // update grid:

            if (creating || adding) {
                moGridTptFigures.addGridRow(moBolTptFigure);
            }
            
            int row = moGridTptFigures.getModel().getGridRows().indexOf(moBolTptFigure); // get row before refreshing grid!
            refreshBolTptFigures();
            moGridTptFigures.setSelectedGridRow(moGridTptFigures.getTable().convertRowIndexToView(row));
            
            actionPerformedTptFigCancel();
        }
    }

    private void actionPerformedTptFigCancel() {
        editBolTptFigure(false, ACTION_CANCEL);
    }
    
    private void actionPerformedTptFigTptPartEditType() {
        if (mbEditingTptFigureTptPart && miClient.showMsgBoxConfirm("¿Está seguro que desea cambiar el tipo de la parte de transporte?") == JOptionPane.YES_OPTION) {
            jbTptFigTptPartEditType.setEnabled(false);
            moKeyTptFigTptPartTransportPartType.setEnabled(true);
            moKeyTptFigTptPartTransportPartType.requestFocusInWindow();
        }
    }
    
    private void actionPerformedTptFigTptPartAdd() {
        if (canBolTptFigureTptPartAppend()) {
            DDbBolTransportFigureTransportPart bolTptFigureTptPart = createBolTptFigureTptPartPickingOne();

            if (bolTptFigureTptPart != null) {
                moBolTptFigureTptPart = bolTptFigureTptPart;
                editBolTptFigureTptPart(true, ACTION_ADD);
            }
        }
    }
    
    private void actionPerformedTptFigTptPartCreate() {
        if (canBolTptFigureTptPartAppend()) {
            editBolTptFigureTptPart(true, ACTION_CREATE);
        }
    }

    private void actionPerformedTptFigTptPartModify() {
        if (moGridTptFiguresTptParts.getSelectedGridRow() == null) {
            miClient.showMsgBoxWarning(DGridConsts.MSG_SELECT_ROW);
        }
        else {
            editBolTptFigureTptPart(true, ACTION_MODIFY);
        }
    }

    private void actionPerformedTptFigTptPartRemove() {
        try {
            actionGridRemove(moGridTptFiguresTptParts, this.getClass().getMethod("refreshBolTptFiguresTptParts"));
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }

    private void actionPerformedTptFigTptPartOk() {
        DGuiValidation validation = moFieldsTptFigureTptPart.validateFields();
        
        if (DGuiUtils.computeValidation(miClient, validation)) {
            boolean adding = mnActionTptFigureTptPart == ACTION_ADD; // convenience variable
            boolean creating = mnActionTptFigureTptPart == ACTION_CREATE || mnActionTptFigureTptPart == ACTION_COPY; // convenience variable
            DDbSysTransportPartType sysTptPartType = null;
            
            if (creating) {
                moBolTptFigureTptPart = new DDbBolTransportFigureTransportPart();
                sysTptPartType = (DDbSysTransportPartType) miClient.getSession().readRegistry(DModConsts.LS_TPT_PART_TP, moKeyTptFigTptPartTransportPartType.getValue());
                moBolTptFigureTptPart.setOwnSysTransportPartType(sysTptPartType);
            }
            
            sysTptPartType = moBolTptFigureTptPart.getOwnSysTransportPartType();
            
            sysTptPartType.setPkTypeId(moKeyTptFigTptPartTransportPartType.getValue()[0]);
            
            sysTptPartType.setRegistryEdited(true);
            
            moBolTptFigureTptPart.updateFromOwnSysTransportPartType();
        
            //moBolTptFigureTptPart.setPkBolId(...);
            //moBolTptFigureTptPart.setPkTransportFigureId(...);
            //moBolTptFigureTptPart.setPkTransportPartIdId(...);
            //moBolTptFigureTptPart.setFkTransportPartTypeId(...); // updated already by DDbBolTransportFigureTransportPart.updateFromOwnTransportPartType()

            //moBolTptFigureTptPart.setOwnTransportPartType(...); // already set above
            
            // update own registry:
            
            moBolTptFigureTptPart.setBolUpdateOwnRegistry(moBoolTptFigTptPartUpdate.getValue());

            // update grid:

            if (creating || adding) {
                moGridTptFiguresTptParts.addGridRow(moBolTptFigureTptPart);
            }
            
            int row = moGridTptFiguresTptParts.getModel().getGridRows().indexOf(moBolTptFigureTptPart); // get row before refreshing grid!
            refreshBolTptFiguresTptParts();
            moGridTptFiguresTptParts.setSelectedGridRow(moGridTptFiguresTptParts.getTable().convertRowIndexToView(row));
            
            actionPerformedTptFigTptPartCancel();
        }
    }

    private void actionPerformedTptFigTptPartCancel() {
        editBolTptFigureTptPart(false, ACTION_CANCEL);
    }
    
    private void itemStateChangedTptFigTransportFigureType() {
        moTextTptFigDriverLicense.resetField();
        
        if (moKeyTptFigTransportFigureType.getSelectedIndex() <= 0) {
            moTextTptFigDriverLicense.setEnabled(false);
            
            mbTptFigureTptPartIsRequired = false;
            jtfTptFigTptPartIsRequired.setText("");
        }
        else {
            boolean enableDriverLicense = mbEditingTptFigure && moKeyTptFigTransportFigureType.getValue()[0] == DModSysConsts.LS_TPT_FIGURE_TP_DRIVER;
            
            moTextTptFigDriverLicense.setEnabled(enableDriverLicense);
            
            mbTptFigureTptPartIsRequired = DLibUtils.belongsTo(moKeyTptFigTransportFigureType.getValue()[0], new int[] { DModSysConsts.LS_TPT_FIGURE_TP_OWNER, DModSysConsts.LS_TPT_FIGURE_TP_LEASER });
            jtfTptFigTptPartIsRequired.setText(mbTptFigureTptPartIsRequired ? DXmlCatalogEntry.TXT_REQUIRED_YES : DXmlCatalogEntry.TXT_REQUIRED_NO);
            jtfTptFigTptPartIsRequired.setCaretPosition(0);
        }
    }
    
    private void itemStateChangedTptFigFigureCountry() {
        moTextTptFigForeignId.resetField();
        
        if (moKeyTptFigFigureCountry.getSelectedIndex() <= 0) {
            moTextTptFigForeignId.setEnabled(false);
        }
        else {
            boolean enable = mbEditingTptFigure && moKeyTptFigFigureCountry.getValue()[0] != DModSysConsts.CS_CTY_MEX;
            
            moTextTptFigForeignId.setEnabled(enable);
        }
    }
    
    private void itemStateChangedTptFigAddressCountry() {
        moTextTptFigAddressStateCode.resetField();
        moTextTptFigAddressStateName.resetField();
        moTextTptFigAddressCountyCode.resetField();
        moTextTptFigAddressCountyName.resetField();
        moTextTptFigAddressLocalityCode.resetField();
        moTextTptFigAddressLocalityName.resetField();
        moTextTptFigAddressZipCode.resetField();
        moTextTptFigAddressDistrictCode.resetField();
        moTextTptFigAddressDistrictName.resetField();
        moTextTptFigAddressStreet.resetField();
        moTextTptFigAddressNumberExt.resetField();
        moTextTptFigAddressNumberInt.resetField();
        moTextTptFigAddressReference.resetField();
        
        if (moKeyTptFigAddressCountry.getSelectedIndex() <= 0) {
            moTextTptFigAddressStateCode.setEnabled(false);
            moTextTptFigAddressStateName.setEnabled(false);
            moTextTptFigAddressCountyCode.setEnabled(false);
            moTextTptFigAddressCountyName.setEnabled(false);
            moTextTptFigAddressLocalityCode.setEnabled(false);
            moTextTptFigAddressLocalityName.setEnabled(false);
            moTextTptFigAddressZipCode.setEnabled(false);
            moTextTptFigAddressDistrictCode.setEnabled(false);
            moTextTptFigAddressDistrictName.setEnabled(false);
            moTextTptFigAddressStreet.setEnabled(false);
            moTextTptFigAddressNumberExt.setEnabled(false);
            moTextTptFigAddressNumberInt.setEnabled(false);
            moTextTptFigAddressReference.setEnabled(false);
        }
        else {
            String countryCode = moKeyTptFigAddressCountry.getSelectedItem().getCode(); // convenience variable
            boolean applyStateCatalog = DFormBolUtils.applyStateCatalog(countryCode);
            boolean applyAddressCatalogs = DFormBolUtils.applyAddressCatalogs(countryCode);
            
            moTextTptFigAddressStateCode.setEnabled(mbEditingTptFigure && applyStateCatalog);
            moTextTptFigAddressStateName.setEnabled(mbEditingTptFigure && !applyStateCatalog);
            moTextTptFigAddressCountyCode.setEnabled(mbEditingTptFigure && applyAddressCatalogs);
            moTextTptFigAddressCountyName.setEnabled(mbEditingTptFigure && !applyAddressCatalogs);
            moTextTptFigAddressLocalityCode.setEnabled(mbEditingTptFigure && applyAddressCatalogs);
            moTextTptFigAddressLocalityName.setEnabled(mbEditingTptFigure && !applyAddressCatalogs);
            moTextTptFigAddressZipCode.setEnabled(mbEditingTptFigure);
            moTextTptFigAddressDistrictCode.setEnabled(mbEditingTptFigure && applyAddressCatalogs);
            moTextTptFigAddressDistrictName.setEnabled(mbEditingTptFigure && !applyAddressCatalogs);
            moTextTptFigAddressStreet.setEnabled(mbEditingTptFigure);
            moTextTptFigAddressNumberExt.setEnabled(mbEditingTptFigure);
            moTextTptFigAddressNumberInt.setEnabled(mbEditingTptFigure);
            moTextTptFigAddressReference.setEnabled(mbEditingTptFigure);
        }
        
        if (moTextTptFigAddressStateCode.isEnabled()) {
            moTextTptFigAddressStateCode.setValue(DFormBolUtils.DEF_CODE_ADDRESS_STATE);
        }
        
        if (moTextTptFigAddressCountyCode.isEnabled()) {
            moTextTptFigAddressCountyCode.setValue(DFormBolUtils.DEF_CODE_ADDRESS_COUNTY);
        }
        
        if (moTextTptFigAddressLocalityCode.isEnabled()) {
            moTextTptFigAddressLocalityCode.setValue(DFormBolUtils.DEF_CODE_ADDRESS_LOCALITY);
        }
        
        if (moTextTptFigAddressDistrictCode.isEnabled()) {
            moTextTptFigAddressDistrictCode.setValue(DFormBolUtils.DEF_CODE_ADDRESS_DISTRICT);
        }
    }
    
    private void focusGainedTptFigAddressStateCode() {
        if (moTextTptFigAddressStateCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_STATE)) {
            moTextTptFigAddressStateCode.resetField();
        }
    }
    
    private void focusGainedTptFigAddressCountyCode() {
        if (moTextTptFigAddressCountyCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_COUNTY)) {
            moTextTptFigAddressCountyCode.resetField();
        }
    }
    
    private void focusGainedTptFigAddressLocalityCode() {
        if (moTextTptFigAddressLocalityCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_LOCALITY)) {
            moTextTptFigAddressLocalityCode.resetField();
        }
    }
    
    private void focusGainedTptFigAddressDistrictCode() {
        if (moTextTptFigAddressDistrictCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_DISTRICT)) {
            moTextTptFigAddressDistrictCode.resetField();
        }
    }

    private void focusLostTptFigAddressStateCode() {
        computeTptFigAddressStateCode();
    }

    private void focusLostTptFigAddressCountyCode() {
        computeTptFigAddressCountyCode();
    }

    private void focusLostTptFigAddressLocalityCode() {
        computeTptFigAddressLocalityCode();
    }

    private void focusLostTptFigAddressDistrictCode() {
        computeTptFigAddressDistrictCode();
    }
    
    private void keyReleasedTptFigAddressStateCode(final KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_F5) {
            DGuiValidation validation = moKeyTptFigAddressCountry.validateField();
            
            if (DGuiUtils.computeValidation(miClient, validation)) {
                if (moPickerCatalogAddressState == null) {
                    moPickerCatalogAddressState = new DPickerCatalogAddressState(miClient);
                }

                DGuiItem guiItem = moKeyTptFigAddressCountry.getSelectedItem();
                DLadCatalogAddressCountry country = new DLadCatalogAddressCountry(guiItem.getCode(), guiItem.getItem());
                
                moPickerCatalogAddressState.setValue(DLadCatalogConsts.ADDRESS_COUNTRY, country);
                moPickerCatalogAddressState.resetForm();
                moPickerCatalogAddressState.setVisible(true);
                
                if (moPickerCatalogAddressState.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                    DLadCatalogAddressState state = (DLadCatalogAddressState) moPickerCatalogAddressState.getValue(DLadCatalogConsts.ADDRESS_STATE);
                    if (state != null) {
                        moTextTptFigAddressStateCode.setValue(state.Code);
                        moTextTptFigAddressStateName.setValue(state.Name);
                    }
                }
            }
        }
    }

    private void keyReleasedTptFigAddressCountyCode(final KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_F5) {
            DGuiValidation validation = moKeyTptFigAddressCountry.validateField();
            
            if (validation.isValid()) {
                if (moTextTptFigAddressStateCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_STATE)) {
                    validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + DGuiUtils.getLabelName(jlTptFigAddressState) + "'.");
                    validation.setComponent(moTextTptFigAddressStateCode);
                }
            }
            
            if (DGuiUtils.computeValidation(miClient, validation)) {
                if (moPickerCatalogAddressCounty == null) {
                    moPickerCatalogAddressCounty = new DPickerCatalogAddressCounty(miClient);
                }

                DGuiItem guiItem = moKeyTptFigAddressCountry.getSelectedItem();
                DLadCatalogAddressCountry country = new DLadCatalogAddressCountry(guiItem.getCode(), guiItem.getItem());
                DLadCatalogAddressState state = new DLadCatalogAddressState(moTextTptFigAddressStateCode.getValue(), moTextTptFigAddressStateName.getValue());
                
                moPickerCatalogAddressCounty.setValue(DLadCatalogConsts.ADDRESS_COUNTRY, country);
                moPickerCatalogAddressCounty.setValue(DLadCatalogConsts.ADDRESS_STATE, state);
                moPickerCatalogAddressCounty.resetForm();
                moPickerCatalogAddressCounty.setVisible(true);
                
                if (moPickerCatalogAddressCounty.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                    DLadCatalogAddressCounty county = (DLadCatalogAddressCounty) moPickerCatalogAddressCounty.getValue(DLadCatalogConsts.ADDRESS_COUNTY);
                    if (county != null) {
                        moTextTptFigAddressCountyCode.setValue(county.Code);
                        moTextTptFigAddressCountyName.setValue(county.Name);
                    }
                }
            }
        }
    }

    private void keyReleasedTptFigAddressLocalityCode(final KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_F5) {
            DGuiValidation validation = moKeyTptFigAddressCountry.validateField();
            
            if (validation.isValid()) {
                validation = moTextTptFigAddressStateCode.validateField();
                
                if (validation.isValid()) {
                    if (moTextTptFigAddressStateCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_STATE)) {
                        validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + DGuiUtils.getLabelName(jlTptFigAddressState) + "'.");
                        validation.setComponent(moTextTptFigAddressStateCode);
                    }
                    else if (moTextTptFigAddressCountyCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_COUNTY)) {
                        validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + DGuiUtils.getLabelName(jlTptFigAddressCounty) + "'.");
                        validation.setComponent(moTextTptFigAddressCountyCode);
                    }
                }
            }
            
            if (DGuiUtils.computeValidation(miClient, validation)) {
                if (moPickerCatalogAddressLocality == null) {
                    moPickerCatalogAddressLocality = new DPickerCatalogAddressLocality(miClient);
                }

                DGuiItem guiItem = moKeyTptFigAddressCountry.getSelectedItem();
                DLadCatalogAddressCountry country = new DLadCatalogAddressCountry(guiItem.getCode(), guiItem.getItem());
                DLadCatalogAddressState state = new DLadCatalogAddressState(moTextTptFigAddressStateCode.getValue(), moTextTptFigAddressStateName.getValue());
                DLadCatalogAddressCounty county = new DLadCatalogAddressCounty(moTextTptFigAddressCountyCode.getValue(), moTextTptFigAddressCountyName.getValue());
                
                moPickerCatalogAddressLocality.setValue(DLadCatalogConsts.ADDRESS_COUNTRY, country);
                moPickerCatalogAddressLocality.setValue(DLadCatalogConsts.ADDRESS_STATE, state);
                moPickerCatalogAddressLocality.setValue(DLadCatalogConsts.ADDRESS_COUNTY, county);
                moPickerCatalogAddressLocality.resetForm();
                moPickerCatalogAddressLocality.setVisible(true);
                
                if (moPickerCatalogAddressLocality.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                    DLadCatalogAddressLocality locality = (DLadCatalogAddressLocality) moPickerCatalogAddressLocality.getValue(DLadCatalogConsts.ADDRESS_LOCALITY);
                    if (locality != null) {
                        moTextTptFigAddressLocalityCode.setValue(locality.Code);
                        moTextTptFigAddressLocalityName.setValue(locality.Name);
                    }
                }
            }
        }
    }

    private void keyReleasedTptFigAddressDistrictCode(final KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_F5) {
            DGuiValidation validation = moKeyTptFigAddressCountry.validateField();
            
            if (validation.isValid()) {
                validation = moTextTptFigAddressStateCode.validateField();
                
                if (validation.isValid()) {
                    if (moTextTptFigAddressStateCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_STATE)) {
                        validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + DGuiUtils.getLabelName(jlTptFigAddressState) + "'.");
                        validation.setComponent(moTextTptFigAddressStateCode);
                    }
                    else if (moTextTptFigAddressCountyCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_COUNTY)) {
                        validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + DGuiUtils.getLabelName(jlTptFigAddressCounty) + "'.");
                        validation.setComponent(moTextTptFigAddressCountyCode);
                    }
                    else if (moTextTptFigAddressLocalityCode.getValue().equals(DFormBolUtils.DEF_CODE_ADDRESS_LOCALITY)) {
                        validation.setMessage(DGuiConsts.ERR_MSG_FIELD_DIF + "'" + DGuiUtils.getLabelName(jlTptFigAddressLocality) + "'.");
                        validation.setComponent(moTextTptFigAddressLocalityCode);
                    }
                    else {
                        validation = moTextTptFigAddressZipCode.validateField();
                    }
                }
            }
            
            if (DGuiUtils.computeValidation(miClient, validation)) {
                if (moPickerCatalogAddressDistrict == null) {
                    moPickerCatalogAddressDistrict = new DPickerCatalogAddressDistrict(miClient);
                }

                DGuiItem guiItem = moKeyTptFigAddressCountry.getSelectedItem();
                DLadCatalogAddressCountry country = new DLadCatalogAddressCountry(guiItem.getCode(), guiItem.getItem());
                DLadCatalogAddressState state = new DLadCatalogAddressState(moTextTptFigAddressStateCode.getValue(), moTextTptFigAddressStateName.getValue());
                DLadCatalogAddressCounty county = new DLadCatalogAddressCounty(moTextTptFigAddressCountyCode.getValue(), moTextTptFigAddressCountyName.getValue());
                DLadCatalogAddressLocality locality = new DLadCatalogAddressLocality(moTextTptFigAddressLocalityCode.getValue(), moTextTptFigAddressLocalityName.getValue());
                
                moPickerCatalogAddressDistrict.setValue(DLadCatalogConsts.ADDRESS_COUNTRY, country);
                moPickerCatalogAddressDistrict.setValue(DLadCatalogConsts.ADDRESS_STATE, state);
                moPickerCatalogAddressDistrict.setValue(DLadCatalogConsts.ADDRESS_COUNTY, county);
                moPickerCatalogAddressDistrict.setValue(DLadCatalogConsts.ADDRESS_LOCALITY, locality);
                moPickerCatalogAddressDistrict.setValue(DLadCatalogConsts.ADDRESS_ZIP_CODE, moTextTptFigAddressZipCode.getValue());
                moPickerCatalogAddressDistrict.resetForm();
                moPickerCatalogAddressDistrict.setVisible(true);
                
                if (moPickerCatalogAddressDistrict.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                    DLadCatalogAddressDistrict district = (DLadCatalogAddressDistrict) moPickerCatalogAddressDistrict.getValue(DLadCatalogConsts.ADDRESS_DISTRICT);
                    if (district != null) {
                        moTextTptFigAddressDistrictCode.setValue(district.Code);
                        moTextTptFigAddressDistrictName.setValue(district.Name);
                    }
                }
            }
        }
    }
    
    private void valueChangedTptFig() {
        renderBolTptFigure((DDbBolTransportFigure) moGridTptFigures.getSelectedGridRow());
    }

    private void valueChangedTptFigTptPart() {
        renderBolTptFigureTptPart((DDbBolTransportFigureTransportPart) moGridTptFiguresTptParts.getSelectedGridRow());
    }

    /*
     * General public methods
     */

    public void setTemplateKey(final int[] key) {
        manTemplateKey = key;
    }
    
    /*
     * DBeanForm methods
     */

    @Override
    public void addAllListeners() {
        // BOL
        
        jbBolUpdateVersion.addActionListener(this);
        jbBolNavStart.addActionListener(this);
        jbBolNavRestart.addActionListener(this);
        jbBolNavPrev.addActionListener(this);
        jbBolNavNext.addActionListener(this);
        jbBolSetIntlTransport.addActionListener(this);
        
        moKeyBolSeries.addItemListener(this);
        moBoolBolIntlTransport.addItemListener(this);
        moBoolBolIsthmus.addItemListener(this);
        moBoolBolTemplate.addItemListener(this);
        
        // locations

        jbLocEditType.addActionListener(this);
        jbLocGetNextCode.addActionListener(this);
        jbLocAdd.addActionListener(this);
        jbLocCreate.addActionListener(this);
        jbLocCopy.addActionListener(this);
        jbLocModify.addActionListener(this);
        jbLocRemove.addActionListener(this);
        jbLocMoveUp.addActionListener(this);
        jbLocMoveDown.addActionListener(this);
        jbLocOk.addActionListener(this);
        jbLocCancel.addActionListener(this);
        
        moKeyLocLocationType.addItemListener(this);
        moKeyLocSourceLocation.addItemListener(this);
        moKeyLocAddressCountry.addItemListener(this);
        
        moTextLocCode.addFocusListener(this);
        moTextLocAddressStateCode.addFocusListener(this);
        moTextLocAddressCountyCode.addFocusListener(this);
        moTextLocAddressLocalityCode.addFocusListener(this);
        moTextLocAddressDistrictCode.addFocusListener(this);
        
        moTextLocAddressStateCode.addKeyListener(this);
        moTextLocAddressCountyCode.addKeyListener(this);
        moTextLocAddressLocalityCode.addKeyListener(this);
        moTextLocAddressDistrictCode.addKeyListener(this);
        
        // merchandises
        
        jbMerchPickItem.addActionListener(this);
        jbMerchSetWeightKg.addActionListener(this);
        jbMerchAdd.addActionListener(this);
        jbMerchCreate.addActionListener(this);
        jbMerchCopy.addActionListener(this);
        jbMerchModify.addActionListener(this);
        jbMerchRemove.addActionListener(this);
        jbMerchMoveUp.addActionListener(this);
        jbMerchMoveDown.addActionListener(this);
        jbMerchOk.addActionListener(this);
        jbMerchCancel.addActionListener(this);
        jbMerchMoveSetQuantity.addActionListener(this);
        jbMerchMoveAdd.addActionListener(this);
        jbMerchMoveCreate.addActionListener(this);
        jbMerchMoveModify.addActionListener(this);
        jbMerchMoveRemove.addActionListener(this);
        jbMerchMoveOk.addActionListener(this);
        jbMerchMoveCancel.addActionListener(this);
        
        moKeyMerchItem.addItemListener(this);
        moKeyMerchUnit.addItemListener(this);
        moKeyMerchCurrency.addItemListener(this);
        moRadMerchDimensionsCm.addItemListener(this);
        moRadMerchDimensionsPlg.addItemListener(this);
        moBoolMerchHazardousMaterial.addItemListener(this);
        moRadMerchHazardousMaterialYes.addItemListener(this);
        moRadMerchHazardousMaterialNo.addItemListener(this);
        
        moDecMerchQuantity.addFocusListener(this);
        moIntMerchDimensionsLength.addFocusListener(this);
        moIntMerchDimensionsHeight.addFocusListener(this);
        moIntMerchDimensionsWidth.addFocusListener(this);
        moTextMerchHazardousMaterialCode.addFocusListener(this);
        moTextMerchPackagingCode.addFocusListener(this);
        moIntMerchImportRequest1.addFocusListener(this);
        moIntMerchImportRequest2.addFocusListener(this);
        moIntMerchImportRequest3.addFocusListener(this);
        moIntMerchImportRequest4.addFocusListener(this);
        
        moTextMerchHazardousMaterialCode.addKeyListener(this);
        moTextMerchPackagingCode.addKeyListener(this);
        
        // truck
        
        jbTruckEditConfig.addActionListener(this);
        jbTruckGetNextCode.addActionListener(this);
        jbTruckSetWeightGrossTon.addActionListener(this);
        jbTruckAdd.addActionListener(this);
        jbTruckCreate.addActionListener(this);
        jbTruckCopy.addActionListener(this);
        jbTruckModify.addActionListener(this);
        jbTruckRemove.addActionListener(this);
        jbTruckMoveUp.addActionListener(this);
        jbTruckMoveDown.addActionListener(this);
        jbTruckOk.addActionListener(this);
        jbTruckCancel.addActionListener(this);
        jbTruckTrailEditSubtype.addActionListener(this);
        jbTruckTrailAdd.addActionListener(this);
        jbTruckTrailCreate.addActionListener(this);
        jbTruckTrailModify.addActionListener(this);
        jbTruckTrailRemove.addActionListener(this);
        jbTruckTrailOk.addActionListener(this);
        jbTruckTrailCancel.addActionListener(this);
        
        moKeyTruckTransportConfig.addItemListener(this);
        moKeyTruckPermissionType.addItemListener(this);
        moKeyTruckTrailSubtype.addItemListener(this);
        
        // transport figures
        
        jbTptFigEditType.addActionListener(this);
        jbTptFigGetNextCode.addActionListener(this);
        jbTptFigAdd.addActionListener(this);
        jbTptFigCreate.addActionListener(this);
        jbTptFigCopy.addActionListener(this);
        jbTptFigModify.addActionListener(this);
        jbTptFigRemove.addActionListener(this);
        jbTptFigMoveUp.addActionListener(this);
        jbTptFigMoveDown.addActionListener(this);
        jbTptFigOk.addActionListener(this);
        jbTptFigCancel.addActionListener(this);
        jbTptFigTptPartEditType.addActionListener(this);
        jbTptFigTptPartAdd.addActionListener(this);
        jbTptFigTptPartCreate.addActionListener(this);
        jbTptFigTptPartModify.addActionListener(this);
        jbTptFigTptPartRemove.addActionListener(this);
        jbTptFigTptPartOk.addActionListener(this);
        jbTptFigTptPartCancel.addActionListener(this);
        
        moKeyTptFigTransportFigureType.addItemListener(this);
        moKeyTptFigFigureCountry.addItemListener(this);
        moKeyTptFigAddressCountry.addItemListener(this);
        
        moTextTptFigAddressStateCode.addFocusListener(this);
        moTextTptFigAddressCountyCode.addFocusListener(this);
        moTextTptFigAddressLocalityCode.addFocusListener(this);
        moTextTptFigAddressDistrictCode.addFocusListener(this);
        
        moTextTptFigAddressStateCode.addKeyListener(this);
        moTextTptFigAddressCountyCode.addKeyListener(this);
        moTextTptFigAddressLocalityCode.addKeyListener(this);
        moTextTptFigAddressDistrictCode.addKeyListener(this);
    }

    @Override
    public void removeAllListeners() {
        // BOL
        
        jbBolUpdateVersion.removeActionListener(this);
        jbBolNavStart.removeActionListener(this);
        jbBolNavRestart.removeActionListener(this);
        jbBolNavPrev.removeActionListener(this);
        jbBolNavNext.removeActionListener(this);
        jbBolSetIntlTransport.removeActionListener(this);
        
        moKeyBolSeries.removeItemListener(this);
        moBoolBolIntlTransport.removeItemListener(this);
        moBoolBolIsthmus.removeItemListener(this);
        moBoolBolTemplate.removeItemListener(this);
        
        // locations
        
        jbLocEditType.removeActionListener(this);
        jbLocGetNextCode.removeActionListener(this);
        jbLocAdd.removeActionListener(this);
        jbLocCreate.removeActionListener(this);
        jbLocCopy.removeActionListener(this);
        jbLocModify.removeActionListener(this);
        jbLocRemove.removeActionListener(this);
        jbLocMoveUp.removeActionListener(this);
        jbLocMoveDown.removeActionListener(this);
        jbLocOk.removeActionListener(this);
        jbLocCancel.removeActionListener(this);
        
        moKeyLocLocationType.removeItemListener(this);
        moKeyLocSourceLocation.removeItemListener(this);
        moKeyLocAddressCountry.removeItemListener(this);
        
        moTextLocCode.removeFocusListener(this);
        moTextLocAddressStateCode.removeFocusListener(this);
        moTextLocAddressCountyCode.removeFocusListener(this);
        moTextLocAddressLocalityCode.removeFocusListener(this);
        moTextLocAddressDistrictCode.removeFocusListener(this);
        
        moTextLocAddressStateCode.removeKeyListener(this);
        moTextLocAddressCountyCode.removeKeyListener(this);
        moTextLocAddressLocalityCode.removeKeyListener(this);
        moTextLocAddressDistrictCode.removeKeyListener(this);
        
        // merchandises
        
        jbMerchPickItem.removeActionListener(this);
        jbMerchSetWeightKg.removeActionListener(this);
        jbMerchAdd.removeActionListener(this);
        jbMerchCreate.removeActionListener(this);
        jbMerchCopy.removeActionListener(this);
        jbMerchModify.removeActionListener(this);
        jbMerchRemove.removeActionListener(this);
        jbMerchMoveUp.removeActionListener(this);
        jbMerchMoveDown.removeActionListener(this);
        jbMerchOk.removeActionListener(this);
        jbMerchCancel.removeActionListener(this);
        jbMerchMoveSetQuantity.removeActionListener(this);
        jbMerchMoveAdd.removeActionListener(this);
        jbMerchMoveCreate.removeActionListener(this);
        jbMerchMoveModify.removeActionListener(this);
        jbMerchMoveRemove.removeActionListener(this);
        jbMerchMoveOk.removeActionListener(this);
        jbMerchMoveCancel.removeActionListener(this);
        
        moKeyMerchItem.removeItemListener(this);
        moKeyMerchUnit.removeItemListener(this);
        moKeyMerchCurrency.removeItemListener(this);
        moRadMerchDimensionsCm.removeItemListener(this);
        moRadMerchDimensionsPlg.removeItemListener(this);
        moBoolMerchHazardousMaterial.removeItemListener(this);
        moRadMerchHazardousMaterialYes.removeItemListener(this);
        moRadMerchHazardousMaterialNo.removeItemListener(this);
        
        moDecMerchQuantity.removeFocusListener(this);
        moIntMerchDimensionsLength.removeFocusListener(this);
        moIntMerchDimensionsHeight.removeFocusListener(this);
        moIntMerchDimensionsWidth.removeFocusListener(this);
        moTextMerchHazardousMaterialCode.removeFocusListener(this);
        moTextMerchPackagingCode.removeFocusListener(this);
        moIntMerchImportRequest1.removeFocusListener(this);
        moIntMerchImportRequest2.removeFocusListener(this);
        moIntMerchImportRequest3.removeFocusListener(this);
        moIntMerchImportRequest4.removeFocusListener(this);
        
        moTextMerchHazardousMaterialCode.removeKeyListener(this);
        moTextMerchPackagingCode.removeKeyListener(this);
        
        // truck
        
        jbTruckEditConfig.removeActionListener(this);
        jbTruckGetNextCode.removeActionListener(this);
        jbTruckSetWeightGrossTon.removeActionListener(this);
        jbTruckAdd.removeActionListener(this);
        jbTruckCreate.removeActionListener(this);
        jbTruckCopy.removeActionListener(this);
        jbTruckModify.removeActionListener(this);
        jbTruckRemove.removeActionListener(this);
        jbTruckMoveUp.removeActionListener(this);
        jbTruckMoveDown.removeActionListener(this);
        jbTruckOk.removeActionListener(this);
        jbTruckCancel.removeActionListener(this);
        jbTruckTrailEditSubtype.removeActionListener(this);
        jbTruckTrailAdd.removeActionListener(this);
        jbTruckTrailCreate.removeActionListener(this);
        jbTruckTrailModify.removeActionListener(this);
        jbTruckTrailRemove.removeActionListener(this);
        jbTruckTrailOk.removeActionListener(this);
        jbTruckTrailCancel.removeActionListener(this);
        
        moKeyTruckTransportConfig.removeItemListener(this);
        moKeyTruckPermissionType.removeItemListener(this);
        moKeyTruckTrailSubtype.removeItemListener(this);
        
        // transport figures
        
        jbTptFigEditType.removeActionListener(this);
        jbTptFigGetNextCode.removeActionListener(this);
        jbTptFigAdd.removeActionListener(this);
        jbTptFigCreate.removeActionListener(this);
        jbTptFigCopy.removeActionListener(this);
        jbTptFigModify.removeActionListener(this);
        jbTptFigRemove.removeActionListener(this);
        jbTptFigMoveUp.removeActionListener(this);
        jbTptFigMoveDown.removeActionListener(this);
        jbTptFigOk.removeActionListener(this);
        jbTptFigCancel.removeActionListener(this);
        jbTptFigTptPartEditType.removeActionListener(this);
        jbTptFigTptPartAdd.removeActionListener(this);
        jbTptFigTptPartCreate.removeActionListener(this);
        jbTptFigTptPartModify.removeActionListener(this);
        jbTptFigTptPartRemove.removeActionListener(this);
        jbTptFigTptPartOk.removeActionListener(this);
        jbTptFigTptPartCancel.removeActionListener(this);
        
        moKeyTptFigTransportFigureType.removeItemListener(this);
        moKeyTptFigFigureCountry.removeItemListener(this);
        moKeyTptFigAddressCountry.removeItemListener(this);
        
        moTextTptFigAddressStateCode.removeFocusListener(this);
        moTextTptFigAddressCountyCode.removeFocusListener(this);
        moTextTptFigAddressLocalityCode.removeFocusListener(this);
        moTextTptFigAddressDistrictCode.removeFocusListener(this);
        
        moTextTptFigAddressStateCode.removeKeyListener(this);
        moTextTptFigAddressCountyCode.removeKeyListener(this);
        moTextTptFigAddressLocalityCode.removeKeyListener(this);
        moTextTptFigAddressDistrictCode.removeKeyListener(this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void reloadCatalogues() {
        int[] branchKey = !moBol.isRegistryNew() ? moBol.getCompanyBranchKey() : moSessionCustom.getBranchKey();
        ArrayList<String> seriesArray = new ArrayList<>();
        
        try {
            String[] bolSeries = DFormBolUtils.getBolSeries(miClient.getSession(), branchKey, msTransportTypeTruckCode);
            
            for (String series : bolSeries) {
                seriesArray.add(series);
            }
            
            if (!moBol.isRegistryNew() && !seriesArray.contains(moBol.getSeries())) {
                seriesArray.add(0, moBol.getSeries()); // add original series at first position
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        
        moKeyBolSeries.removeAllItems();
        for (String series : seriesArray) {
            moKeyBolSeries.addItem(new DGuiItem(series));
        }
        
        miClient.getSession().populateCatalogue(moKeyLocLocationType, DModConsts.LS_LOC_TP, DModConsts.L_BOL, null);
        miClient.getSession().populateCatalogue(moKeyLocAddressCountry, DModConsts.CS_CTY, 0, null);
        
        miClient.getSession().populateCatalogue(moKeyMerchItem, DModConsts.IU_ITM, 0, new DGuiParams(DModConsts.IS_ITM_CL, DModSysConsts.IS_ITM_CL_SAL_PRO));
        miClient.getSession().populateCatalogue(moKeyMerchUnit, DModConsts.IU_UNT, 0, null);
        miClient.getSession().populateCatalogue(moKeyMerchCurrency, DModConsts.CS_CUR, 0, null);
        
        miClient.getSession().populateCatalogue(moKeyTptFigTransportFigureType, DModConsts.LS_TPT_FIGURE_TP, 0, null);
        miClient.getSession().populateCatalogue(moKeyTptFigFigureCountry, DModConsts.CS_CTY, 0, null);
        miClient.getSession().populateCatalogue(moKeyTptFigAddressCountry, DModConsts.CS_CTY, 0, null);
        miClient.getSession().populateCatalogue(moKeyTptFigTptPartTransportPartType, DModConsts.LS_TPT_PART_TP, 0, null);
    }
    
    private void setBolSeries(final String series) {
        moKeyBolSeries.resetField();
        
        for (int index = 0; index < moKeyBolSeries.getItemCount(); index++) {
            if (((DGuiItem) moKeyBolSeries.getItemAt(index)).getItem().equals(series)) {
                moKeyBolSeries.setSelectedIndex(index);
                break;
            }
        }
    }

    @Override
    public void setRegistry(DDbRegistry registry) throws Exception {
        moBol = (DDbBol) registry;

        mnFormResult = 0;
        mbFirstActivation = true;
        mbForceCancel = false;

        moSessionCustom = (DGuiClientSessionCustom) miClient.getSession().getSessionCustom(); // refresh each time this form is used
        
        if (moBol.isRegistryNew()) {
            // Validate if new registry can be created:

            if (moSessionCustom.getBranchKey() == null) {
                mbCanShowForm = false;
                msCanShowFormMessage = DUtilConsts.ERR_MSG_USR_SES_BRA;
                return;
            }
        }

        removeAllListeners();
        reloadCatalogues();

        if (moBol.isRegistryNew() || manTemplateKey != null) {
            moBol.initPrimaryKey();
            
            moBol.setVersion(cfd.ver4.ccp31.DElementCartaPorte.VERSION);
            
            if (isBolTemplate()) {
                moBol.setTemplate(true);
                moBol.setDate(DLibTimeUtils.createDate(DLibTimeConsts.YEAR_MIN, 1, 1));
            }
            else {
                moBol.setTemplate(false);
                moBol.setDate(miClient.getSession().getWorkingDate());
            }
            
            moBol.setFkTransportTypeId(DModSysConsts.LS_TPT_TP_TRUCK);
            moBol.setFkBolStatusId(DModSysConsts.TS_DPS_ST_NEW);
            moBol.setFkOwnerBizPartnerId(moSessionCustom.getBranchKey()[0]);
            moBol.setFkOwnerBranchId(moSessionCustom.getBranchKey()[1]);
            moBol.setFkMerchandiseWeightUnitId(mnBolWeightUnitId);
            
            if (manTemplateKey != null) {
                moBol.setRegistryNew(true);
                moBol.initBolTemplate(manTemplateKey[0]);
                moBol.initBolLocations(new Date());
                
                manTemplateKey = null;
                
                if (moDialogBol == null) {
                    moDialogBol = new DDialogBol(miClient, "Captura rápida de carta porte");
                }
                
                moDialogBol.setRegistry(moBol.clone()); // cloning prevents from editing actual registry when unnecessary
                
                if (moDialogBol.isDialogNeeded()) {
                    moDialogBol.setVisible(true);
                    
                    if (moDialogBol.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
                        moBol = (DDbBol) moDialogBol.getRegistry();
                        moBol.computeMerchandisesAndTruck();
                    }
                    else if (miClient.showMsgBoxConfirm("¿Desea continuar con la captura de la carta porte?") != JOptionPane.YES_OPTION) {
                        mbForceCancel = true;
                    }
                }
            }
            
            jtfRegistryKey.setText("");
        }
        else {
            if (moBol.isTemplate() && moBol.getFkBolStatusId() == DModSysConsts.TS_DPS_ST_ANN) {
                moBol.setFkBolStatusId(DModSysConsts.TS_DPS_ST_NEW);
            }
            
            jtfRegistryKey.setText(DLibUtils.textKey(moBol.getPrimaryKey()));
        }

        setFormEditable(true);
        
        jtfBolTransportType.setText((String) miClient.getSession().readField(DModConsts.LS_TPT_TP, new int[] { moBol.getFkTransportTypeId() }, DDbRegistry.FIELD_NAME));
        jtfBolTransportType.setCaretPosition(0);
        jtfBolVersion.setText(moBol.getVersion());
        jtfBolVersion.setCaretPosition(0);
        setBolSeries(moBol.getSeries());
        jtfBolNumber.setText("" + moBol.getNumber());
        jtfBolNumber.setCaretPosition(0);
        moDateBolDate.setValue(moBol.getDate());
        jtfBolStatus.setText((String) miClient.getSession().readField(DModConsts.TS_DPS_ST, new int[] { moBol.getFkBolStatusId() }, DDbRegistry.FIELD_NAME));
        jtfBolStatus.setCaretPosition(0);
        jtfBolDfrUuid.setText(moBol.getChildDfr() == null ? "" : moBol.getChildDfr().getUuid());
        jtfBolDfrUuid.setCaretPosition(0);
        jtfBolBolUuid.setText(moBol.getBolUuid());
        jtfBolBolUuid.setCaretPosition(0);
        
        boolean isBolIntlTransport = moBol.getIntlTransport().equals(DCfdi40Catalogs.TextoSí);
        moBoolBolIntlTransport.setValue(isBolIntlTransport);
        
        if (!isBolIntlTransport) {
            moBolIntlTransport = null;
        }
        else {
            moBolIntlTransport = new DLadBolIntlTransport();
            moBolIntlTransport.IntlTransportDirection = moBol.getIntlTransportDirection();
            moBolIntlTransport.IntlTransportCountryId = moBol.getFkIntlTransportCountryId();
            moBolIntlTransport.IntlWayTransportTypeId = moBol.getFkIntlWayTransportTypeId();
        }
        
        itemStateChangedBolIntlTransport();
        
        moBoolBolIsthmus.setValue(moBol.isIsthmus());
        itemStateChangedBolIsthmus();
        moKeyBolIsthmusOrigin.setValue(new int[] { moXmlBolIsthmusRegistry.getId(moBol.getIsthmusOrigin()) });
        moKeyBolIsthmusDestiny.setValue(new int[] { moXmlBolIsthmusRegistry.getId(moBol.getIsthmusDestiny()) });
        
        moBoolBolTemplate.setValue(moBol.isTemplate());
        itemStateChangedBolTemplate();
        moTextBolTemplateName.setValue(moBol.getTemplateName());
        moTextBolTemplateCode.setValue(moBol.getTemplateCode());
        
        if (moBol.getFkBolTemplateId_n() != 0) {
            jtfBolBolTemplate.setText((String) miClient.getSession().readField(DModConsts.L_BOL, new int[] { moBol.getFkBolTemplateId_n() }, DDbBol.FIELD_TEMP_NAME));
            jtfBolBolTemplate.setCaretPosition(0);
        }
        else {
            jtfBolBolTemplate.setText("");
        }
        
        jtfOwnBranch.setText((String) miClient.getSession().readField(DModConsts.BU_BRA, moBol.getCompanyBranchKey(), DDbRegistry.FIELD_CODE));
        jtfOwnBranch.setCaretPosition(0);
        
        renderBolLocation(null);
        renderBolMerchandise(null);
        renderBolMerchandiseMove(null);
        renderBolTruck(null);
        renderBolTruckTrailer(null);
        renderBolTptFigure(null);
        renderBolTptFigureTptPart(null);
        
        ///// BEGIN OF TESTING CODE (REMOVE FRO PRODUCTION) ////////////////////
        /*
        moBol.getChildLocations().addAll(DBolUtils.createTestingBolLocations(miClient.getSession()));
        moBol.getChildMerchandises().addAll(DBolUtils.createTestingBolMerchandises(miClient.getSession(), moBol.getChildLocations()));
        computeBol();
        moBol.getChildTrucks().addAll(DBolUtils.createTestingBolTrucks(miClient.getSession(), mdBolMerchandiseWeightKg));
        moBol.getChildTransportFigures().addAll(DBolUtils.createTestingBolTptFigures(miClient.getSession()));
        */
        ///// END OF TESTING CODE (REMOVE FRO PRODUCTION) //////////////////////
        
        mbAdjustingGrids = true;
        
        clearBolContent();
        
        // initialize temporal BOL location ID:
        
        if (!moBol.getChildLocations().isEmpty()) {
            mnTempBolLocationId = moBol.getChildLocations().get(moBol.getChildLocations().size() - 1).getPkLocationId();
        }
        
        // populate grids:
        
        moGridLocations.populateGrid(new Vector<>(moBol.getChildLocations()), this);
        moGridLocations.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moGridLocations.getTable().setRowSorter(null);
        
        moGridMerchandises.populateGrid(new Vector<>(moBol.getChildMerchandises()), this);
        moGridMerchandises.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moGridMerchandises.getTable().setRowSorter(null);
        
        moGridMerchandisesMoves.populateGrid(moBol.getChildMerchandises().isEmpty() ? new Vector<>() : new Vector<>(moBol.getChildMerchandises().get(0).getChildMoves()), this);
        moGridMerchandisesMoves.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moGridMerchandisesMoves.getTable().setRowSorter(null);
        
        moGridTrucks.populateGrid(new Vector<>(moBol.getChildTrucks()), this);
        moGridTrucks.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moGridTrucks.getTable().setRowSorter(null);
        
        moGridTrucksTrailers.populateGrid(moBol.getChildTrucks().isEmpty() ? new Vector<>() : new Vector<>(moBol.getChildTrucks().get(0).getChildTrailers()), this);
        moGridTrucksTrailers.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moGridTrucksTrailers.getTable().setRowSorter(null);
        
        moGridTptFigures.populateGrid(new Vector<>(moBol.getChildTransportFigures()), this);
        moGridTptFigures.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moGridTptFigures.getTable().setRowSorter(null);
        
        moGridTptFiguresTptParts.populateGrid(moBol.getChildTransportFigures().isEmpty() ? new Vector<>() : new Vector<>(moBol.getChildTransportFigures().get(0).getChildTransportParts()), this);
        moGridTptFiguresTptParts.getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        moGridTptFiguresTptParts.getTable().setRowSorter(null);
        
        mbAdjustingGrids = false;
        
        computeBol();
        
        actionPerformedBolNavRestart(false); // sets selected tab to 0!

        moGridLocations.setSelectedGridRow(0);
        moGridMerchandises.setSelectedGridRow(0);
        moGridTrucks.setSelectedGridRow(0);
        moGridTptFigures.setSelectedGridRow(0);
        
        addAllListeners();
    }

    @Override
    public DDbRegistry getRegistry() throws Exception {
        DDbBol bol = moBol.clone();

        if (bol.isRegistryNew()) {
            //bol.setPkBolId(...);
        }
        
        computeBol();

        bol.setVersion(jtfBolVersion.getText());
        bol.setSeries(moKeyBolSeries.getSelectedItem().getItem());
        bol.setNumber(DLibUtils.parseInt(jtfBolNumber.getText()));
        bol.setDate(moDateBolDate.getValue());
        bol.setIntlTransport(!moBoolBolIntlTransport.getValue() ? DCfdi40Catalogs.TextoNo : DCfdi40Catalogs.TextoSí);
        bol.setIntlTransportDirection(!moBoolBolIntlTransport.getValue() || moBolIntlTransport == null ? "" : moBolIntlTransport.IntlTransportDirection);
        bol.setDistanceKm(mdBolDistanceKm);
        bol.setMerchandiseWeight(mdBolMerchandiseWeightKg);
        bol.setMerchandiseNumber(mnBolMerchandiseNumber);
        bol.setMerchandiseInverseLogistics(moBoolBolMerchandiseInverseLogistics.isSelected());
        bol.setIsthmus(moBoolBolIsthmus.isSelected());
        bol.setIsthmusOrigin(moKeyBolIsthmusOrigin.getSelectedIndex() <= 0 ? "" : moKeyBolIsthmusOrigin.getSelectedItem().getCode());
        bol.setIsthmusDestiny(moKeyBolIsthmusDestiny.getSelectedIndex() <= 0 ? "" : moKeyBolIsthmusDestiny.getSelectedItem().getCode());
        //bol.setBolUuid(...); // set on save()
        bol.setTemplate(moBoolBolTemplate.getValue());
        bol.setTemplateCode(!moBoolBolTemplate.getValue() ? "" : moTextBolTemplateCode.getValue());
        bol.setTemplateName(!moBoolBolTemplate.getValue() ? "" : moTextBolTemplateName.getValue());
        //bol.setDeleted(...);
        //bol.setFkTransportTypeId(...); // set from beginning
        //bol.setFkBolStatusId(...); // set from beginning
        bol.setFkIntlTransportCountryId(!moBoolBolIntlTransport.getValue() || moBolIntlTransport == null ? DModSysConsts.CS_CTY_NA : moBolIntlTransport.IntlTransportCountryId);
        bol.setFkIntlWayTransportTypeId(!moBoolBolIntlTransport.getValue() || moBolIntlTransport == null ? DModSysConsts.LS_TPT_TP_NA : moBolIntlTransport.IntlWayTransportTypeId);
        //bol.setFkMerchandiseWeightUnitId(...); // set from beginning
        //bol.setFkBolTemplateId_n(...); // set from beginning
        //bol.setFkUserInsertId(...);
        //bol.setFkUserUpdateId(...);
        //bol.setTsUserInsert(...);
        //bol.setTsUserUpdate(...);
        
        // locations:
        
        bol.getChildLocations().clear();
        
        for (DGridRow row : moGridLocations.getModel().getGridRows()) {
            DDbBolLocation bolLocation = (DDbBolLocation) row;
            
            bol.getChildLocations().add(bolLocation);
        }
        
        // merchandises:
        
        bol.getChildMerchandises().clear();
        
        for (DGridRow row : moGridMerchandises.getModel().getGridRows()) {
            DDbBolMerchandise bolMerchandise = (DDbBolMerchandise) row;
            
            bol.getChildMerchandises().add(bolMerchandise);
        }
        
        // trucks:
        
        bol.getChildTrucks().clear();
        
        for (DGridRow row : moGridTrucks.getModel().getGridRows()) {
            DDbBolTruck bolTruck = (DDbBolTruck) row;
            
            bol.getChildTrucks().add(bolTruck);
            
            if (bolTruck.isBolUpdateOwnRegistry()) {
                // preserve truck trailers, transport figures and parts of transport figures:

                DDbTruck truck = bolTruck.getOwnTruck();
                
                truck.getChildTrailers().clear();
                
                for (DDbBolTruckTrailer btt : bolTruck.getChildTrailers()) {
                    DDbTruckTrailer truckTrailer = new DDbTruckTrailer();
                    
                    truckTrailer.setPkTruckId(bolTruck.getFkTruckId());
                    truckTrailer.setPkTrailerId(btt.getPkTrailerId());
                    truckTrailer.setFkTrailerId(btt.getFkTrailerId());
                    
                    truck.getChildTrailers().add(truckTrailer);
                }
                
                truck.getChildTransportFigures().clear();
                
                for (DGridRow row1 : moGridTptFigures.getModel().getGridRows()) {
                    DDbBolTransportFigure btf = (DDbBolTransportFigure) row1;
                    DDbTruckTransportFigure truckTransportFigure = new DDbTruckTransportFigure();
                    
                    truckTransportFigure.setPkTruckId(bolTruck.getFkTruckId());
                    truckTransportFigure.setPkTransportFigureId(btf.getPkTransportFigureId());
                    truckTransportFigure.setFkTransportFigureId(btf.getFkTransportFigureId());
                    
                    for (DDbBolTransportFigureTransportPart tftp : btf.getChildTransportParts()) {
                        DDbTruckTransportFigureTransportPart truckTransportFigureTransportPart = new DDbTruckTransportFigureTransportPart();
                        
                        truckTransportFigureTransportPart.setPkTruckId(bolTruck.getFkTruckId());
                        truckTransportFigureTransportPart.setPkTransportFigureId(btf.getPkTransportFigureId());
                        truckTransportFigureTransportPart.setPkTransportPartId(tftp.getPkTransportPartId());
                        truckTransportFigureTransportPart.setFkTransportPartTypeId(tftp.getFkTransportPartTypeId());
                        
                        truckTransportFigure.getChildTransportParts().add(truckTransportFigureTransportPart);
                    }
                    
                    truck.getChildTransportFigures().add(truckTransportFigure);
                }
            }
        }
        
        // save as well child transport figures:
        
        bol.getChildTransportFigures().clear();
        
        for (DGridRow row : moGridTptFigures.getModel().getGridRows()) {
            DDbBolTransportFigure bolTransportFigure = (DDbBolTransportFigure) row;
            
            bol.getChildTransportFigures().add(bolTransportFigure);
        }
        
        bol.setAuxXmlTypeId(DModSysConsts.TS_XML_TP_CFDI_40);
        
        return bol;
    }

    @Override
    public DGuiValidation validateForm() {
        DGuiValidation validation = moFields.validateFields();
        
        if (validation.isValid()) {
            if (mbEditingLocation) {
                validation.setMessage("Se debe terminar la captura de ubicaciones.");
            }
            else if (mbEditingMerchandise) {
                validation.setMessage("Se debe terminar la captura de mercancías.");
            }
            else if (mbEditingMerchandiseMove) {
                validation.setMessage("Se debe terminar la captura de cantidades transportadas.");
            }
            else if (mbEditingTruck) {
                validation.setMessage("Se debe terminar la captura de autotransporte.");
            }
            else if (mbEditingTruckTrailer) {
                validation.setMessage("Se debe terminar la captura de remolques.");
            }
            else if (mbEditingTptFigure) {
                validation.setMessage("Se debe terminar la captura de figuras de transporte.");
            }
            else if (mbEditingTptFigureTptPart) {
                validation.setMessage("Se debe terminar la captura de partes de transporte.");
            }
            else if (!mbEditionStarted) {
                validation.setMessage("Debe haberse iniciado la captura de la carta porte.");
                validation.setComponent(jbBolNavStart);
            }
            else {
                if (isBolTemplate()) {
                    if (moGridTrucks.getTable().getRowCount() > 1) {
                        validation.setMessage("Debe haber sólo un autotransporte.");
                        enableWizardTab(TAB_IDX_TRUCK, NAV_ACTION_NEXT);
                    }
                }
                else {
                    if (countLocations(DModSysConsts.LS_LOC_TP_SRC) == 0) {
                        validation.setMessage("Debe haber al menos una ubicación origen.");
                        validation.setComponent(jbLocAdd);
                    }
                    else if (countLocations(DModSysConsts.LS_LOC_TP_DES) == 0) {
                        validation.setMessage("Debe haber al menos una ubicación destino.");
                        validation.setComponent(jbLocAdd);
                    }
                    else if (moGridMerchandises.getTable().getRowCount() < 1) {
                        validation.setMessage("Debe haber al menos una mercancía.");
                        enableWizardTab(TAB_IDX_MERCHANDISE, NAV_ACTION_NEXT);
                    }
                    else if (moGridTrucks.getTable().getRowCount() != 1) {
                        validation.setMessage("Sólo puede haber un autotransporte.");
                        enableWizardTab(TAB_IDX_TRUCK, NAV_ACTION_NEXT);
                    }
                    else if (moGridTptFigures.getTable().getRowCount() < 1) {
                        validation.setMessage("Debe haber al menos una figura de transporte.");
                        enableWizardTab(TAB_IDX_TPT_FIGURE, NAV_ACTION_NEXT);
                    }
                }
            }
        }
        
        return validation;
    }
    
    /*
     * Listeners methods
     */

    @Override
    public void actionCancel() {
        boolean cancel = true;

        if (mnFormStatus == DGuiConsts.FORM_STATUS_EDIT && !mbForceCancel && isSaveAllowed()) {
            cancel = miClient.showMsgBoxConfirm(DGuiConsts.MSG_CNF_FORM_CLS) == JOptionPane.YES_OPTION;
        }

        if (cancel) {
            super.actionCancel();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();
            
            if (button == jbBolUpdateVersion) {
                actionPerformedBolUpdateVersion();
            }
            else if (button == jbBolNavStart) {
                actionPerformedBolNavStart();
            }
            else if (button == jbBolNavRestart) {
                actionPerformedBolNavRestart(true);
            }
            else if (button == jbBolNavPrev) {
                actionPerformedBolNavPrev();
            }
            else if (button == jbBolNavNext) {
                actionPerformedBolNavNext();
            }
            else if (button == jbBolSetIntlTransport) {
                actionPerformedBolSetIntlTransport();
            }
            else if (button == jbLocEditType) {
                actionPerformedLocEditType();
            }
            else if (button == jbLocGetNextCode) {
                actionPerformedLocGetNextCode();
            }
            else if (button == jbLocAdd) {
                actionPerformedLocAdd();
            }
            else if (button == jbLocCreate) {
                actionPerformedLocCreate();
            }
            else if (button == jbLocCopy) {
                actionPerformedLocCopy();
            }
            else if (button == jbLocModify) {
                actionPerformedLocModify();
            }
            else if (button == jbLocRemove) {
                actionPerformedLocRemove();
            }
            else if (button == jbLocMoveUp) {
                actionPerformedLocMoveUp();
            }
            else if (button == jbLocMoveDown) {
                actionPerformedLocMoveDown();
            }
            else if (button == jbLocOk) {
                actionPerformedLocOk();
            }
            else if (button == jbLocCancel) {
                actionPerformedLocCancel();
            }
            else if (button == jbMerchPickItem) {
                actionPerformedMerchPickItem();
            }
            else if (button == jbMerchSetWeightKg) {
                actionPerformedMerchSetWeightKg();
            }
            else if (button == jbMerchAdd) {
                actionPerformedMerchAdd();
            }
            else if (button == jbMerchCreate) {
                actionPerformedMerchCreate();
            }
            else if (button == jbMerchCopy) {
                actionPerformedMerchCopy();
            }
            else if (button == jbMerchModify) {
                actionPerformedMerchModify();
            }
            else if (button == jbMerchRemove) {
                actionPerformedMerchRemove();
            }
            else if (button == jbMerchMoveUp) {
                actionPerformedMerchMoveUp();
            }
            else if (button == jbMerchMoveDown) {
                actionPerformedMerchMoveDown();
            }
            else if (button == jbMerchOk) {
                actionPerformedMerchOk();
            }
            else if (button == jbMerchCancel) {
                actionPerformedMerchCancel();
            }
            else if (button == jbMerchMoveSetQuantity) {
                actionPerformedMerchMoveSetQuantity();
            }
            else if (button == jbMerchMoveAdd) {
                actionPerformedMerchMoveAdd();
            }
            else if (button == jbMerchMoveCreate) {
                actionPerformedMerchMoveCreate();
            }
            else if (button == jbMerchMoveModify) {
                actionPerformedMerchMoveModify();
            }
            else if (button == jbMerchMoveRemove) {
                actionPerformedMerchMoveRemove();
            }
            else if (button == jbMerchMoveOk) {
                actionPerformedMerchMoveOk();
            }
            else if (button == jbMerchMoveCancel) {
                actionPerformedMerchMoveCancel();
            }
            else if (button == jbTruckEditConfig) {
                actionPerformedTruckEditConfig();
            }
            else if (button == jbTruckGetNextCode) {
                actionPerformedTruckGetNextCode();
            }
            else if (button == jbTruckSetWeightGrossTon) {
                actionPerformedTruckSetWeightGrossTon();
            }
            else if (button == jbTruckAdd) {
                actionPerformedTruckAdd();
            }
            else if (button == jbTruckCreate) {
                actionPerformedTruckCreate();
            }
            else if (button == jbTruckCopy) {
                actionPerformedTruckCopy();
            }
            else if (button == jbTruckModify) {
                actionPerformedTruckModify();
            }
            else if (button == jbTruckRemove) {
                actionPerformedTruckRemove();
            }
            else if (button == jbTruckMoveUp) {
                actionPerformedTruckMoveUp();
            }
            else if (button == jbTruckMoveDown) {
                actionPerformedTruckMoveDown();
            }
            else if (button == jbTruckOk) {
                actionPerformedTruckOk();
            }
            else if (button == jbTruckCancel) {
                actionPerformedTruckCancel();
            }
            else if (button == jbTruckTrailEditSubtype) {
                actionPerformedTruckTrailEditSubtype();
            }
            else if (button == jbTruckTrailAdd) {
                actionPerformedTruckTrailAdd();
            }
            else if (button == jbTruckTrailCreate) {
                actionPerformedTruckTrailCreate();
            }
            else if (button == jbTruckTrailModify) {
                actionPerformedTruckTrailModify();
            }
            else if (button == jbTruckTrailRemove) {
                actionPerformedTruckTrailRemove();
            }
            else if (button == jbTruckTrailOk) {
                actionPerformedTruckTrailOk();
            }
            else if (button == jbTruckTrailCancel) {
                actionPerformedTruckTrailCancel();
            }
            else if (button == jbTptFigEditType) {
                actionPerformedTptFigEditType();
            }
            else if (button == jbTptFigGetNextCode) {
                actionPerformedTptFigGetNextCode();
            }
            else if (button == jbTptFigAdd) {
                actionPerformedTptFigAdd();
            }
            else if (button == jbTptFigCreate) {
                actionPerformedTptFigCreate();
            }
            else if (button == jbTptFigCopy) {
                actionPerformedTptFigCopy();
            }
            else if (button == jbTptFigModify) {
                actionPerformedTptFigModify();
            }
            else if (button == jbTptFigRemove) {
                actionPerformedTptFigRemove();
            }
            else if (button == jbTptFigMoveUp) {
                actionPerformedTptFigMoveUp();
            }
            else if (button == jbTptFigMoveDown) {
                actionPerformedTptFigMoveDown();
            }
            else if (button == jbTptFigOk) {
                actionPerformedTptFigOk();
            }
            else if (button == jbTptFigCancel) {
                actionPerformedTptFigCancel();
            }
            else if (button == jbTptFigTptPartEditType) {
                actionPerformedTptFigTptPartEditType();
            }
            else if (button == jbTptFigTptPartAdd) {
                actionPerformedTptFigTptPartAdd();
            }
            else if (button == jbTptFigTptPartCreate) {
                actionPerformedTptFigTptPartCreate();
            }
            else if (button == jbTptFigTptPartModify) {
                actionPerformedTptFigTptPartModify();
            }
            else if (button == jbTptFigTptPartRemove) {
                actionPerformedTptFigTptPartRemove();
            }
            else if (button == jbTptFigTptPartOk) {
                actionPerformedTptFigTptPartOk();
            }
            else if (button == jbTptFigTptPartCancel) {
                actionPerformedTptFigTptPartCancel();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof DBeanFieldBoolean) {
            DBeanFieldBoolean field = (DBeanFieldBoolean) e.getSource();
            
            if (field == moBoolBolIntlTransport) {
                itemStateChangedBolIntlTransport();
            }
            else if (field == moBoolBolIsthmus) {
                itemStateChangedBolIsthmus();
            }
            else if (field == moBoolBolTemplate) {
                itemStateChangedBolTemplate();
            }
            else if (field == moBoolMerchHazardousMaterial) {
                itemStateChangedMerchHazardousMaterial();
            }
        }
        else if (e.getSource() instanceof DBeanFieldKey) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                DBeanFieldKey field = (DBeanFieldKey) e.getSource();

                if (field == moKeyLocLocationType) {
                    itemStateChangedLocLocationType();
                }
                else if (field == moKeyLocSourceLocation) {
                    itemStateChangedLocSourceLocation();
                }
                else if (field == moKeyLocAddressCountry) {
                    itemStateChangedLocAddressCountry();
                }
                else if (field == moKeyMerchItem) {
                    itemStateChangedMerchItem();
                }
                else if (field == moKeyMerchUnit) {
                    itemStateChangedMerchUnit();
                }
                else if (field == moKeyMerchCurrency) {
                    itemStateChangedMerchCurrency();
                }
                else if (field == moKeyTruckTransportConfig) {
                    itemStateChangedTruckTransportConfig();
                }
                else if (field == moKeyTruckPermissionType) {
                    itemStateChangedTruckPermissionType();
                }
                else if (field == moKeyTruckTrailSubtype) {
                    itemStateChangedTruckTrailSubtype();
                }
                else if (field == moKeyTptFigTransportFigureType) {
                    itemStateChangedTptFigTransportFigureType();
                }
                else if (field == moKeyTptFigFigureCountry) {
                    itemStateChangedTptFigFigureCountry();
                }
                else if (field == moKeyTptFigAddressCountry) {
                    itemStateChangedTptFigAddressCountry();
                }
            }
        }
        else if (e.getSource() instanceof DBeanFieldRadio) {
            DBeanFieldRadio field = (DBeanFieldRadio) e.getSource();
            
            if (field == moRadMerchDimensionsCm || field == moRadMerchDimensionsPlg) {
                itemStateChangedMerchDimensions();
            }
            else if (field == moRadMerchHazardousMaterialYes|| field == moRadMerchHazardousMaterialNo) {
                itemStateChangedMerchHazardousMaterialYesNo();
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getSource() instanceof DBeanFieldText) {
            DBeanFieldText field = (DBeanFieldText) e.getSource();
            
            if (field == moTextLocCode) {
                focusGainedLocCode();
            }
            else if (field == moTextLocAddressStateCode) {
                focusGainedLocAddressStateCode();
            }
            else if (field == moTextLocAddressCountyCode) {
                focusGainedLocAddressCountyCode();
            }
            else if (field == moTextLocAddressLocalityCode) {
                focusGainedLocAddressLocalityCode();
            }
            else if (field == moTextLocAddressDistrictCode) {
                focusGainedLocAddressDistrictCode();
            }
            else if (field == moTextMerchHazardousMaterialCode) {
                focusGainedMerchHazardousMaterialCode();
            }
            else if (field == moTextMerchPackagingCode) {
                focusGainedMerchPackagingCode();
            }
            else if (field == moTextTptFigAddressStateCode) {
                focusGainedTptFigAddressStateCode();
            }
            else if (field == moTextTptFigAddressCountyCode) {
                focusGainedTptFigAddressCountyCode();
            }
            else if (field == moTextTptFigAddressLocalityCode) {
                focusGainedTptFigAddressLocalityCode();
            }
            else if (field == moTextTptFigAddressDistrictCode) {
                focusGainedTptFigAddressDistrictCode();
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() instanceof DBeanFieldText) {
            DBeanFieldText field = (DBeanFieldText) e.getSource();
            
            if (field == moTextLocCode) {
                focusLostLocCode();
            }
            else if (field == moTextLocAddressStateCode) {
                focusLostLocAddressStateCode();
            }
            else if (field == moTextLocAddressCountyCode) {
                focusLostLocAddressCountyCode();
            }
            else if (field == moTextLocAddressLocalityCode) {
                focusLostLocAddressLocalityCode();
            }
            else if (field == moTextLocAddressDistrictCode) {
                focusLostLocAddressDistrictCode();
            }
            else if (field == moTextMerchHazardousMaterialCode) {
                focusLostMerchHazardousMaterialCode();
            }
            else if (field == moTextMerchPackagingCode) {
                focusLostMerchPackagingCode();
            }
            else if (field == moTextTptFigAddressStateCode) {
                focusLostTptFigAddressStateCode();
            }
            else if (field == moTextTptFigAddressCountyCode) {
                focusLostTptFigAddressCountyCode();
            }
            else if (field == moTextTptFigAddressLocalityCode) {
                focusLostTptFigAddressLocalityCode();
            }
            else if (field == moTextTptFigAddressDistrictCode) {
                focusLostTptFigAddressDistrictCode();
            }
        }
        else if (e.getSource() instanceof DBeanFieldInteger) {
            DBeanFieldInteger field = (DBeanFieldInteger) e.getSource();
            
            if (field == moIntMerchDimensionsLength || field == moIntMerchDimensionsHeight || field == moIntMerchDimensionsWidth) {
                focusLostMerchDimensions();
            }
            else if (field == moIntMerchImportRequest1 || field == moIntMerchImportRequest2 || field == moIntMerchImportRequest3 || field == moIntMerchImportRequest4) {
                focusLostMerchImportRequest();
            }
        }
        else if (e.getSource() instanceof DBeanFieldDecimal) {
            DBeanFieldDecimal field = (DBeanFieldDecimal) e.getSource();
            
            if (field == moDecMerchQuantity) {
                focusLostMerchQuantity();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() instanceof DBeanFieldText) {
            DBeanFieldText field = (DBeanFieldText) e.getSource();
            
            if (field == moTextLocAddressStateCode) {
                keyReleasedLocAddressStateCode(e);
            }
            else if (field == moTextLocAddressCountyCode) {
                keyReleasedLocAddressCountyCode(e);
            }
            else if (field == moTextLocAddressLocalityCode) {
                keyReleasedLocAddressLocalityCode(e);
            }
            else if (field == moTextLocAddressDistrictCode) {
                keyReleasedLocAddressDistrictCode(e);
            }
            else if (field == moTextMerchHazardousMaterialCode) {
                keyReleasedMerchHazardousMaterialCode(e);
            }
            else if (field == moTextMerchPackagingCode) {
                keyReleasedMerchPackagingCode(e);
            }
            else if (field == moTextTptFigAddressStateCode) {
                keyReleasedTptFigAddressStateCode(e);
            }
            else if (field == moTextTptFigAddressCountyCode) {
                keyReleasedTptFigAddressCountyCode(e);
            }
            else if (field == moTextTptFigAddressLocalityCode) {
                keyReleasedTptFigAddressLocalityCode(e);
            }
            else if (field == moTextTptFigAddressDistrictCode) {
                keyReleasedTptFigAddressDistrictCode(e);
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (e.getSource() instanceof DefaultListSelectionModel && !mbAdjustingGrids) {
            DefaultListSelectionModel model = (DefaultListSelectionModel) e.getSource();
            
            if (model == moGridLocations.getTable().getSelectionModel()) {
                valueChangedLoc();
            }
            else if (model == moGridMerchandises.getTable().getSelectionModel()) {
                valueChangedMerch();
            }
            else if (model == moGridMerchandisesMoves.getTable().getSelectionModel()) {
                valueChangedMerchMove();
            }
            else if (model == moGridTrucks.getTable().getSelectionModel()) {
                valueChangedTruck();
            }
            else if (model == moGridTrucksTrailers.getTable().getSelectionModel()) {
                valueChangedTruckTrail();
            }
            else if (model == moGridTptFigures.getTable().getSelectionModel()) {
                valueChangedTptFig();
            }
            else if (model == moGridTptFiguresTptParts.getTable().getSelectionModel()) {
                valueChangedTptFigTptPart();
            }
        }
    }
}
