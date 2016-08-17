/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DGuiClientApp.java
 *
 * Created on 4/06/2011, 05:12:09 PM
 */

package sba.gui;

import cfd.DCfdSignature;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import sba.gui.util.DUtilConfigXml;
import sba.gui.util.DUtilConsts;
import sba.gui.util.DUtilLoginDlg;
import sba.gui.util.DUtilPasswordDlg;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbDatabase;
import sba.lib.db.DDbDatabaseMonitor;
import sba.lib.db.DDbRegistry;
import sba.lib.grid.DGridPaneView;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiConsts;
import sba.lib.gui.DGuiDatePicker;
import sba.lib.gui.DGuiDateRangePicker;
import sba.lib.gui.DGuiEdsSignature;
import sba.lib.gui.DGuiForm;
import sba.lib.gui.DGuiSession;
import sba.lib.gui.DGuiUserGui;
import sba.lib.gui.DGuiUtils;
import sba.lib.gui.DGuiYearMonthPicker;
import sba.lib.gui.DGuiYearPicker;
import sba.lib.gui.bean.DBeanDialogReport;
import sba.lib.gui.bean.DBeanForm;
import sba.lib.gui.bean.DBeanFormDialog;
import sba.lib.gui.bean.DBeanFormProcess;
import sba.lib.gui.bean.DBeanOptionPicker;
import sba.lib.img.DImgConsts;
import sba.lib.xml.DXmlUtils;
import sba.mod.DModConsts;
import sba.mod.DModModuleBpr;
import sba.mod.DModModuleCfg;
import sba.mod.DModModuleFin;
import sba.mod.DModModuleItm;
import sba.mod.DModModuleMkt;
import sba.mod.DModModulePos;
import sba.mod.DModModuleSrv;
import sba.mod.DModModuleSys;
import sba.mod.DModModuleTrn;
import sba.mod.DModSysConsts;
import sba.mod.DModUtils;
import sba.mod.cfg.db.DDbCertificate;
import sba.mod.cfg.db.DDbConfigBranch;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.cfg.db.DDbUser;
import sba.mod.cfg.db.DDbUserGui;
import sba.mod.cfg.form.DDialogUserSession;
import sba.mod.trn.db.DDbDpsSeries;

/**
 *
 * @author Sergio Flores
 */
public class DGuiClientApp extends JFrame implements DGuiClient, ActionListener {

    public static final String APP_NAME = "SBA 1.0";
    public static final String APP_RELEASE = "SBA 1.0 017.01";
    public static final String APP_COPYRIGHT = "Copyright © 2011-2016 Sergio Abraham Flores Gutiérrez";
    public static final String APP_PROVIDER = "https://sites.google.com/site/iscsergioflores";

    private int mnTerminal;
    private boolean mbFirstActivation;
    private boolean mbLoggedIn;
    private DGuiSession moSession;
    private DUtilConfigXml moConfigXml;
    private DDbDatabase moSysDatabase;
    private DDbDatabaseMonitor moSysDatabaseMonitor;
    private Statement miSysStatement;
    private String msCompany;

    private DGuiDatePicker moDatePicker;
    private DGuiDateRangePicker moDateRangePicker;
    private DGuiYearPicker moYearPicker;
    private DGuiYearMonthPicker moYearMonthPicker;
    private DDialogUserSession moDialogUserSession;
    private JFileChooser moFileChooser;
    private HashMap<Integer, DGuiForm> moUserFormsMap;
    private ImageIcon moIcon;
    private ImageIcon moIconGuiClose;
    private ImageIcon moIconGuiCloseIna;
    private ImageIcon moIconGuiCloseBri;
    private ImageIcon moIconGuiCloseDar;
    private ImageIcon moIconCmdStdOk;
    private ImageIcon moIconCmdStdDelete;
    private ImageIcon moIconCmdStdDisable;
    private ImageIcon moIconCmdStdPrint;
    private ImageIcon moIconCmdStdCardex;
    private ImageIcon moIconCmdStdView;
    private ImageIcon moIconCmdStdDate;
    private ImageIcon moIconCmdStdClear;
    private ImageIcon moIconCmdStdAdd;
    private ImageIcon moIconCmdStdSubtract;
    private ImageIcon moIconCmdStdLot;
    private ImageIcon moIconCmdStdSerialNumber;
    private ImageIcon moIconCmdStdAdjustmentDiscount;
    private ImageIcon moIconCmdStdAdjustmentReturn;
    private ImageIcon moIconCmdStdAdjustmentDocument;
    private ImageIcon moIconCmdStdAdjustmentLot;
    private ImageIcon moIconCmdStdAdjustmentSerialNumber;
    private ImageIcon moIconCmdStdNote;
    private ImageIcon moIconCmdStdSign;
    private ImageIcon moIconCmdStdSignVer;
    private ImageIcon moIconCmdStdCancel;
    private ImageIcon moIconCmdStdCancelVer;
    private ImageIcon moIconCmdStdSend;
    private ImageIcon moIconCmdStdExport;
    private ImageIcon moIconCmdStdImport;
    /* XXX Code for future versions.
    private ImageIcon moIconModCfg;
    private ImageIcon moIconModFin;
    private ImageIcon moIconModPur;
    private ImageIcon moIconModSal;
    private ImageIcon moIconModInv;
    private ImageIcon moIconModMkt;
    private ImageIcon moIconModPos;
    private ImageIcon moIconModSrv;
    */

    /** Creates new form DGuiClientApp */
    public DGuiClientApp() {
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

        jbgModules = new javax.swing.ButtonGroup();
        jtbToolBar = new javax.swing.JToolBar();
        jtbModuleCfg = new javax.swing.JToggleButton();
        jtbModuleFin = new javax.swing.JToggleButton();
        jtbModulePur = new javax.swing.JToggleButton();
        jtbModuleSal = new javax.swing.JToggleButton();
        jtbModuleInv = new javax.swing.JToggleButton();
        jtbModuleMkt = new javax.swing.JToggleButton();
        jtbModulePos = new javax.swing.JToggleButton();
        jtbModuleSrv = new javax.swing.JToggleButton();
        jtpTabbedPane = new javax.swing.JTabbedPane();
        jpStatusBar = new javax.swing.JPanel();
        jtfSystemDate = new javax.swing.JTextField();
        jtfWorkingDate = new javax.swing.JTextField();
        jbWorkingDate = new javax.swing.JButton();
        jtfSessionBranch = new javax.swing.JTextField();
        jtfSessionBranchCash = new javax.swing.JTextField();
        jtfSessionBranchWarehouse = new javax.swing.JTextField();
        jtfSessionBranchDpsSeries = new javax.swing.JTextField();
        jbSessionSettings = new javax.swing.JButton();
        jtfUser = new javax.swing.JTextField();
        jtfUserTs = new javax.swing.JTextField();
        jtfTerminal = new javax.swing.JTextField();
        jlAppRelease = new javax.swing.JLabel();
        jmbMenu = new javax.swing.JMenuBar();
        jmFile = new javax.swing.JMenu();
        jmiFileWorkingDate = new javax.swing.JMenuItem();
        jmiFileSessionSettings = new javax.swing.JMenuItem();
        jmiFileUserPassword = new javax.swing.JMenuItem();
        jsFile1 = new javax.swing.JSeparator();
        jmiFileCloseViewsAll = new javax.swing.JMenuItem();
        jmiFileCloseViewsOther = new javax.swing.JMenuItem();
        jsFile2 = new javax.swing.JSeparator();
        jmiFileCloseSession = new javax.swing.JMenuItem();
        jsFile3 = new javax.swing.JSeparator();
        jmiFileExit = new javax.swing.JMenuItem();
        jmView = new javax.swing.JMenu();
        jmiViewModuleCfg = new javax.swing.JMenuItem();
        jmiViewModuleFin = new javax.swing.JMenuItem();
        jmiViewModulePur = new javax.swing.JMenuItem();
        jmiViewModuleSal = new javax.swing.JMenuItem();
        jmiViewModuleInv = new javax.swing.JMenuItem();
        jmiViewModuleMkt = new javax.swing.JMenuItem();
        jmiViewModulePos = new javax.swing.JMenuItem();
        jmiViewModuleSrv = new javax.swing.JMenuItem();
        jmHelp = new javax.swing.JMenu();
        jmiHelpHelp = new javax.swing.JMenuItem();
        jsHelp1 = new javax.swing.JSeparator();
        jmiHelpAbout = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jtbToolBar.setFloatable(false);
        jtbToolBar.setRollover(true);

        jbgModules.add(jtbModuleCfg);
        jtbModuleCfg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_cfg_bw.png"))); // NOI18N
        jtbModuleCfg.setToolTipText("Configuración");
        jtbModuleCfg.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jtbModuleCfg.setFocusable(false);
        jtbModuleCfg.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbModuleCfg.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jtbModuleCfg.setPreferredSize(new java.awt.Dimension(64, 64));
        jtbModuleCfg.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_cfg.png"))); // NOI18N
        jtbModuleCfg.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_cfg.png"))); // NOI18N
        jtbModuleCfg.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbToolBar.add(jtbModuleCfg);

        jbgModules.add(jtbModuleFin);
        jtbModuleFin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_fin_bw.png"))); // NOI18N
        jtbModuleFin.setToolTipText("Finanzas");
        jtbModuleFin.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jtbModuleFin.setFocusable(false);
        jtbModuleFin.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbModuleFin.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jtbModuleFin.setPreferredSize(new java.awt.Dimension(64, 64));
        jtbModuleFin.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_fin.png"))); // NOI18N
        jtbModuleFin.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_fin.png"))); // NOI18N
        jtbModuleFin.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbToolBar.add(jtbModuleFin);

        jbgModules.add(jtbModulePur);
        jtbModulePur.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_pur_bw.png"))); // NOI18N
        jtbModulePur.setToolTipText("Compras");
        jtbModulePur.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jtbModulePur.setFocusable(false);
        jtbModulePur.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbModulePur.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jtbModulePur.setPreferredSize(new java.awt.Dimension(64, 64));
        jtbModulePur.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_pur.png"))); // NOI18N
        jtbModulePur.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_pur.png"))); // NOI18N
        jtbModulePur.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbToolBar.add(jtbModulePur);

        jbgModules.add(jtbModuleSal);
        jtbModuleSal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_sal_bw.png"))); // NOI18N
        jtbModuleSal.setToolTipText("Ventas");
        jtbModuleSal.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jtbModuleSal.setFocusable(false);
        jtbModuleSal.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbModuleSal.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jtbModuleSal.setPreferredSize(new java.awt.Dimension(64, 64));
        jtbModuleSal.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_sal.png"))); // NOI18N
        jtbModuleSal.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_sal.png"))); // NOI18N
        jtbModuleSal.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbToolBar.add(jtbModuleSal);

        jbgModules.add(jtbModuleInv);
        jtbModuleInv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_inv_bw.png"))); // NOI18N
        jtbModuleInv.setToolTipText("Inventarios");
        jtbModuleInv.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jtbModuleInv.setFocusable(false);
        jtbModuleInv.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbModuleInv.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jtbModuleInv.setPreferredSize(new java.awt.Dimension(64, 64));
        jtbModuleInv.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_inv.png"))); // NOI18N
        jtbModuleInv.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_inv.png"))); // NOI18N
        jtbModuleInv.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbToolBar.add(jtbModuleInv);

        jbgModules.add(jtbModuleMkt);
        jtbModuleMkt.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_mkt_bw.png"))); // NOI18N
        jtbModuleMkt.setToolTipText("Marketing");
        jtbModuleMkt.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jtbModuleMkt.setFocusable(false);
        jtbModuleMkt.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbModuleMkt.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jtbModuleMkt.setPreferredSize(new java.awt.Dimension(64, 64));
        jtbModuleMkt.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_mkt.png"))); // NOI18N
        jtbModuleMkt.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_mkt.png"))); // NOI18N
        jtbModuleMkt.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbToolBar.add(jtbModuleMkt);

        jbgModules.add(jtbModulePos);
        jtbModulePos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_pos_bw.png"))); // NOI18N
        jtbModulePos.setToolTipText("Punto de venta");
        jtbModulePos.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jtbModulePos.setFocusable(false);
        jtbModulePos.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbModulePos.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jtbModulePos.setPreferredSize(new java.awt.Dimension(64, 64));
        jtbModulePos.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_pos.png"))); // NOI18N
        jtbModulePos.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_pos.png"))); // NOI18N
        jtbModulePos.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbToolBar.add(jtbModulePos);

        jbgModules.add(jtbModuleSrv);
        jtbModuleSrv.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_srv_bw.png"))); // NOI18N
        jtbModuleSrv.setToolTipText("Servicios");
        jtbModuleSrv.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jtbModuleSrv.setFocusable(false);
        jtbModuleSrv.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jtbModuleSrv.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jtbModuleSrv.setPreferredSize(new java.awt.Dimension(64, 64));
        jtbModuleSrv.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_srv.png"))); // NOI18N
        jtbModuleSrv.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/sba/gui/img/mod_srv.png"))); // NOI18N
        jtbModuleSrv.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jtbToolBar.add(jtbModuleSrv);

        getContentPane().add(jtbToolBar, java.awt.BorderLayout.NORTH);
        getContentPane().add(jtpTabbedPane, java.awt.BorderLayout.CENTER);

        jpStatusBar.setLayout(new java.awt.FlowLayout(0));

        jtfSystemDate.setEditable(false);
        jtfSystemDate.setText("01/01/2000");
        jtfSystemDate.setToolTipText("Fecha de sistema");
        jtfSystemDate.setFocusable(false);
        jtfSystemDate.setPreferredSize(new java.awt.Dimension(65, 20));
        jpStatusBar.add(jtfSystemDate);

        jtfWorkingDate.setEditable(false);
        jtfWorkingDate.setText("01/01/2000");
        jtfWorkingDate.setToolTipText("Fecha de trabajo");
        jtfWorkingDate.setFocusable(false);
        jtfWorkingDate.setPreferredSize(new java.awt.Dimension(65, 20));
        jpStatusBar.add(jtfWorkingDate);

        jbWorkingDate.setText("...");
        jbWorkingDate.setToolTipText("Cambiar fecha de trabajo");
        jbWorkingDate.setPreferredSize(new java.awt.Dimension(23, 23));
        jpStatusBar.add(jbWorkingDate);

        jtfSessionBranch.setEditable(false);
        jtfSessionBranch.setText("TEXT");
        jtfSessionBranch.setToolTipText("Sucursal actual");
        jtfSessionBranch.setFocusable(false);
        jtfSessionBranch.setPreferredSize(new java.awt.Dimension(50, 20));
        jpStatusBar.add(jtfSessionBranch);

        jtfSessionBranchCash.setEditable(false);
        jtfSessionBranchCash.setText("TEXT");
        jtfSessionBranchCash.setToolTipText("Cuenta de dinero actual");
        jtfSessionBranchCash.setFocusable(false);
        jtfSessionBranchCash.setPreferredSize(new java.awt.Dimension(50, 20));
        jpStatusBar.add(jtfSessionBranchCash);

        jtfSessionBranchWarehouse.setEditable(false);
        jtfSessionBranchWarehouse.setText("TEXT");
        jtfSessionBranchWarehouse.setToolTipText("Almacén de bienes actual");
        jtfSessionBranchWarehouse.setFocusable(false);
        jtfSessionBranchWarehouse.setPreferredSize(new java.awt.Dimension(50, 20));
        jpStatusBar.add(jtfSessionBranchWarehouse);

        jtfSessionBranchDpsSeries.setEditable(false);
        jtfSessionBranchDpsSeries.setText("TEXT");
        jtfSessionBranchDpsSeries.setToolTipText("Serie de documentos actual");
        jtfSessionBranchDpsSeries.setFocusable(false);
        jtfSessionBranchDpsSeries.setPreferredSize(new java.awt.Dimension(75, 20));
        jpStatusBar.add(jtfSessionBranchDpsSeries);

        jbSessionSettings.setText("...");
        jbSessionSettings.setToolTipText("Cambiar sesión de usuario");
        jbSessionSettings.setPreferredSize(new java.awt.Dimension(23, 23));
        jpStatusBar.add(jbSessionSettings);

        jtfUser.setEditable(false);
        jtfUser.setText("TEXT");
        jtfUser.setToolTipText("Usuario");
        jtfUser.setFocusable(false);
        jtfUser.setPreferredSize(new java.awt.Dimension(100, 20));
        jtfUser.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtfUserMouseClicked(evt);
            }
        });
        jpStatusBar.add(jtfUser);

        jtfUserTs.setEditable(false);
        jtfUserTs.setText("01/01/2000 00:00:00 +0000");
        jtfUserTs.setToolTipText("Marca de tiempo de acceso");
        jtfUserTs.setFocusable(false);
        jtfUserTs.setPreferredSize(new java.awt.Dimension(150, 20));
        jpStatusBar.add(jtfUserTs);

        jtfTerminal.setEditable(false);
        jtfTerminal.setText("TEXT");
        jtfTerminal.setToolTipText("Terminal");
        jtfTerminal.setFocusable(false);
        jtfTerminal.setPreferredSize(new java.awt.Dimension(50, 20));
        jpStatusBar.add(jtfTerminal);

        jlAppRelease.setText("RELEASE");
        jlAppRelease.setPreferredSize(new java.awt.Dimension(100, 20));
        jpStatusBar.add(jlAppRelease);

        getContentPane().add(jpStatusBar, java.awt.BorderLayout.SOUTH);

        jmFile.setText("Archivo");

        jmiFileWorkingDate.setText("Cambiar fecha de trabajo...");
        jmFile.add(jmiFileWorkingDate);

        jmiFileSessionSettings.setText("Cambiar sesión de usuario...");
        jmFile.add(jmiFileSessionSettings);

        jmiFileUserPassword.setText("Cambiar contraseña...");
        jmFile.add(jmiFileUserPassword);
        jmFile.add(jsFile1);

        jmiFileCloseViewsAll.setText("Cerrar todas las vistas");
        jmFile.add(jmiFileCloseViewsAll);

        jmiFileCloseViewsOther.setText("Cerrar las otras vistas");
        jmFile.add(jmiFileCloseViewsOther);
        jmFile.add(jsFile2);

        jmiFileCloseSession.setText("Cerrar sesión de usuario");
        jmFile.add(jmiFileCloseSession);
        jmFile.add(jsFile3);

        jmiFileExit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        jmiFileExit.setText("Salir");
        jmFile.add(jmiFileExit);

        jmbMenu.add(jmFile);

        jmView.setText("Ver");

        jmiViewModuleCfg.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_1, java.awt.event.InputEvent.CTRL_MASK));
        jmiViewModuleCfg.setText("Módulo configuración");
        jmView.add(jmiViewModuleCfg);

        jmiViewModuleFin.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_2, java.awt.event.InputEvent.CTRL_MASK));
        jmiViewModuleFin.setText("Módulo finanzas");
        jmView.add(jmiViewModuleFin);

        jmiViewModulePur.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_3, java.awt.event.InputEvent.CTRL_MASK));
        jmiViewModulePur.setText("Módulo compras");
        jmView.add(jmiViewModulePur);

        jmiViewModuleSal.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_4, java.awt.event.InputEvent.CTRL_MASK));
        jmiViewModuleSal.setText("Módulo ventas");
        jmView.add(jmiViewModuleSal);

        jmiViewModuleInv.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_5, java.awt.event.InputEvent.CTRL_MASK));
        jmiViewModuleInv.setText("Módulo inventarios");
        jmView.add(jmiViewModuleInv);

        jmiViewModuleMkt.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_6, java.awt.event.InputEvent.CTRL_MASK));
        jmiViewModuleMkt.setText("Módulo marketing");
        jmView.add(jmiViewModuleMkt);

        jmiViewModulePos.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_7, java.awt.event.InputEvent.CTRL_MASK));
        jmiViewModulePos.setText("Módulo punto de venta");
        jmView.add(jmiViewModulePos);

        jmiViewModuleSrv.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_8, java.awt.event.InputEvent.CTRL_MASK));
        jmiViewModuleSrv.setText("Módulo servicios");
        jmView.add(jmiViewModuleSrv);

        jmbMenu.add(jmView);

        jmHelp.setText("Ayuda");

        jmiHelpHelp.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jmiHelpHelp.setText("Contenido de ayuda...");
        jmHelp.add(jmiHelpHelp);
        jmHelp.add(jsHelp1);

        jmiHelpAbout.setText("Acerca de...");
        jmHelp.add(jmiHelpAbout);

        jmbMenu.add(jmHelp);

        setJMenuBar(jmbMenu);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-1024)/2, (screenSize.height-640)/2, 1024, 640);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        windowClosing();
    }//GEN-LAST:event_formWindowClosing

    private void jtfUserMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jtfUserMouseClicked
        mouseClickedUser(evt);
    }//GEN-LAST:event_jtfUserMouseClicked

    /*
     * Private methods
     */

    private void initComponentsCustom() {
        int result = DLibConsts.UNDEFINED;
        String xml = "";
        TimeZone zone = null;

        mbFirstActivation = true;

        setExtendedState(Frame.MAXIMIZED_BOTH);

        logout();

        try {
            xml = DXmlUtils.readXml(DUtilConsts.FILE_NAME_CFG);
            moConfigXml = new DUtilConfigXml();
            moConfigXml.processXml(xml);

            zone = DLibUtils.createTimeZone(TimeZone.getDefault(), TimeZone.getTimeZone((String) moConfigXml.getAttribute(DUtilConfigXml.ATT_TIME_ZONE).getValue()));
            DLibUtils.restoreDateFormats(zone);
            TimeZone.setDefault(zone);

            moSysDatabase = new DDbDatabase(DDbConsts.DBMS_MYSQL);
            result = moSysDatabase.connect(
                    (String) moConfigXml.getAttribute(DUtilConfigXml.ATT_DB_HOST).getValue(),
                    (String) moConfigXml.getAttribute(DUtilConfigXml.ATT_DB_PORT).getValue(),
                    (String) moConfigXml.getAttribute(DUtilConfigXml.ATT_DB_NAME).getValue(),
                    (String) moConfigXml.getAttribute(DUtilConfigXml.ATT_USR_NAME).getValue(),
                    (String) moConfigXml.getAttribute(DUtilConfigXml.ATT_USR_PSWD).getValue());
            if (result != DDbConsts.CONNECTION_OK) {
                throw new Exception(DDbConsts.ERR_MSG_DB_CONNECTION);
            }
            else {
                moSysDatabaseMonitor = new DDbDatabaseMonitor(moSysDatabase);
                moSysDatabaseMonitor.startThread();

                miSysStatement = moSysDatabase.getConnection().createStatement();
            }

            mnTerminal = DLibUtils.parseInt((String) moConfigXml.getAttribute(DUtilConfigXml.ATT_TERMINAL).getValue());

            moDatePicker = new DGuiDatePicker(this, DGuiConsts.DATE_PICKER_DATE);
            moDateRangePicker = new DGuiDateRangePicker(this);
            moYearPicker = new DGuiYearPicker(this);
            moYearMonthPicker = new DGuiYearMonthPicker(this);
            moDialogUserSession = new DDialogUserSession(this, "Sesión del usuario actual");
            moFileChooser = new JFileChooser();
            moUserFormsMap = new HashMap<Integer, DGuiForm>();
            moIcon = new ImageIcon(getClass().getResource("/sba/gui/img/sba.gif"));
            moIconGuiClose = new ImageIcon(getClass().getResource("/sba/lib/img/gui_close.png"));
            moIconGuiCloseIna = new ImageIcon(getClass().getResource("/sba/lib/img/gui_close_ina.png"));
            moIconGuiCloseBri = new ImageIcon(getClass().getResource("/sba/lib/img/gui_close_bri.png"));
            moIconGuiCloseDar = new ImageIcon(getClass().getResource("/sba/lib/img/gui_close_dar.png"));
            moIconCmdStdOk = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_ok.gif"));
            moIconCmdStdDelete = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_delete.gif"));
            moIconCmdStdDisable = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_disable.gif"));
            moIconCmdStdPrint = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_print.gif"));
            moIconCmdStdCardex = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_cardex.gif"));
            moIconCmdStdView = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_view.gif"));
            moIconCmdStdDate = new ImageIcon(getClass().getResource("/sba/lib/img/cal_cal.gif"));
            moIconCmdStdClear = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_clear.gif"));
            moIconCmdStdAdd = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_add.gif"));
            moIconCmdStdSubtract = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_subtract.gif"));
            moIconCmdStdLot = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_lot.gif"));
            moIconCmdStdSerialNumber = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_sn.gif"));
            moIconCmdStdAdjustmentDiscount = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_adj_disc.gif"));
            moIconCmdStdAdjustmentReturn = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_adj_ret.gif"));
            moIconCmdStdAdjustmentDocument = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_adj_doc.gif"));
            moIconCmdStdAdjustmentLot = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_adj_lot.gif"));
            moIconCmdStdAdjustmentSerialNumber = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_adj_sn.gif"));
            moIconCmdStdNote = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_note.gif"));
            moIconCmdStdSign = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_sign.gif"));
            moIconCmdStdSignVer = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_sign_ver.gif"));
            moIconCmdStdCancel = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_cancel.gif"));
            moIconCmdStdCancelVer = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_cancel_ver.gif"));
            moIconCmdStdSend = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_send.gif"));
            moIconCmdStdExport = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_exp.gif"));
            moIconCmdStdImport = new ImageIcon(getClass().getResource("/sba/lib/img/cmd_std_imp.gif"));
            /* XXX Code for future versions.
            moIconModCfg = new ImageIcon(getClass().getResource("/sba/lib/img/icon_mod_cfg.gif"));
            moIconModFin = new ImageIcon(getClass().getResource("/sba/lib/img/icon_mod_fin.gif"));
            moIconModPur = new ImageIcon(getClass().getResource("/sba/lib/img/icon_mod_pur.gif"));
            moIconModSal = new ImageIcon(getClass().getResource("/sba/lib/img/icon_mod_sal.gif"));
            moIconModInv = new ImageIcon(getClass().getResource("/sba/lib/img/icon_mod_inv.gif"));
            moIconModMkt = new ImageIcon(getClass().getResource("/sba/lib/img/icon_mod_mkt.gif"));
            moIconModPos = new ImageIcon(getClass().getResource("/sba/lib/img/icon_mod_pos.gif"));
            moIconModSrv = new ImageIcon(getClass().getResource("/sba/lib/img/icon_mod_srv.gif"));
            */

            setIconImage(moIcon.getImage());
            jtfTerminal.setText("" + mnTerminal);

            // Set owner frame for bean forms (bean have no-arguments in constructors):

            DBeanForm.OwnerFrame = this;
            DBeanFormDialog.OwnerFrame = this;
            DBeanFormProcess.OwnerFrame = this;
            DBeanOptionPicker.OwnerFrame = this;
            DBeanDialogReport.OwnerFrame = this;
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
            actionFileExit();
        }

        jbWorkingDate.addActionListener(this);
        jbSessionSettings.addActionListener(this);

        jtbModuleCfg.addActionListener(this);
        jtbModuleFin.addActionListener(this);
        jtbModulePur.addActionListener(this);
        jtbModuleSal.addActionListener(this);
        jtbModuleInv.addActionListener(this);
        jtbModuleMkt.addActionListener(this);
        jtbModulePos.addActionListener(this);
        jtbModuleSrv.addActionListener(this);

        jmiFileWorkingDate.addActionListener(this);
        jmiFileSessionSettings.addActionListener(this);
        jmiFileUserPassword.addActionListener(this);
        jmiFileCloseViewsAll.addActionListener(this);
        jmiFileCloseViewsOther.addActionListener(this);
        jmiFileCloseSession.addActionListener(this);
        jmiFileExit.addActionListener(this);
        jmiViewModuleCfg.addActionListener(this);
        jmiViewModuleFin.addActionListener(this);
        jmiViewModulePur.addActionListener(this);
        jmiViewModuleSal.addActionListener(this);
        jmiViewModuleInv.addActionListener(this);
        jmiViewModuleMkt.addActionListener(this);
        jmiViewModulePos.addActionListener(this);
        jmiViewModuleSrv.addActionListener(this);
        jmiHelpHelp.addActionListener(this);
        jmiHelpAbout.addActionListener(this);

        jlAppRelease.setText(APP_RELEASE);
    }

    private void windowActivated() {
        if (mbFirstActivation) {
            mbFirstActivation = false;
            login();
        }
    }

    private void windowClosing() {
        if (mbLoggedIn) {
            logout();
        }
    }

    private void mouseClickedUser(final MouseEvent evt) {
        DModModuleSrv moduleSrv = null;

        if (evt.getClickCount() == 2) {
            moduleSrv = (DModModuleSrv) moSession.getModule(DModConsts.MOD_SRV);
            if (moduleSrv != null) {
                moduleSrv.setVisibleMenuRep(!moduleSrv.isVisibleMenuRep());
            }
        }
    }

    private void renderMenues(JMenu[] menues) {
        jmbMenu.removeAll();
        validate();

        jmbMenu.add(jmFile);
        jmbMenu.add(jmView);

        if (menues != null) {
            for (JMenu menu : menues) {
                jmbMenu.add(menu);
            }
        }

        jmbMenu.add(jmHelp);
        validate();
    }

    private void renderClientSession(DGuiClientSessionCustom clientSession) {
        DDbDpsSeries series = null;

        if (clientSession == null) {
            jtfSessionBranch.setText("");
            jtfSessionBranchCash.setText("");
            jtfSessionBranchWarehouse.setText("");
            jtfSessionBranchDpsSeries.setText("");
        }
        else {
            jtfSessionBranch.setText(clientSession.getBranchKey() == null ? "" : (String) moSession.readField(DModConsts.BU_BRA, clientSession.getBranchKey(), DDbRegistry.FIELD_CODE));
            jtfSessionBranchCash.setText(clientSession.getBranchCashKey() == null ? "" : (String) moSession.readField(DModConsts.CU_CSH, clientSession.getBranchCashKey(), DDbRegistry.FIELD_CODE));
            jtfSessionBranchWarehouse.setText(clientSession.getBranchWarehouseKey() == null ? "" : (String) moSession.readField(DModConsts.CU_WAH, clientSession.getBranchWarehouseKey(), DDbRegistry.FIELD_CODE));

            if (clientSession.getBranchDpsSeriesKey() == null) {
                jtfSessionBranchDpsSeries.setText("");
            }
            else {
                series = (DDbDpsSeries) moSession.readRegistry(DModConsts.TU_SER, clientSession.getBranchDpsSeriesKey());
                jtfSessionBranchDpsSeries.setText((String) moSession.readField(DModConsts.TS_DPS_TP, series.getDpsTypeKey(), DDbRegistry.FIELD_CODE) +
                        ": [" + (series.getSeries().length() == 0 ? " " : series.getSeries()) + "]");
            }
        }
    }

    private ArrayList<DGuiEdsSignature> createEdsSignatures() throws NoSuchAlgorithmException, Exception {
        String sql = "";
        DGuiEdsSignature signature = null;
        ArrayList<DGuiEdsSignature> signatures = new ArrayList<>();
        
        sql = "SELECT id_bpr, id_bra, fk_cer_n "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.CU_CFG_BRA) + " "
                + "WHERE b_del = 0 "
                + "ORDER BY id_bpr, id_bra ";
        Statement statement = moSession.getStatement().getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(sql);  // local variable declaration for be considered as efectively final
        while (resultSet.next()) {
            if (resultSet.getInt("fk_cer_n") != DLibConsts.UNDEFINED) {
                signature = new DGuiEdsSignature() {

                    private final int mnCompanyId = resultSet.getInt(1);
                    private final int mnBranchId = resultSet.getInt(2);
                    private final DDbCertificate moCertificate = (DDbCertificate) moSession.readRegistry(DModConsts.CU_CER, new int[] { resultSet.getInt("fk_cer_n") });
                    private final DCfdSignature moCfdSignature = new DCfdSignature(
                            DLibUtils.convertBlobToBytes(moCertificate.getCertificateKeyPrivate_n()),
                            DLibUtils.convertBlobToBytes(moCertificate.getCertificateKeyPublic_n()), moCertificate.getCertificateNumber());

                    @Override
                    public int[] getKey() {
                        return new int[] { mnCompanyId, mnBranchId };
                    }
                    
                    @Override
                    public String signText(String textToSign, int year) {
                        String sign = "";

                        try {
                            sign = moCfdSignature.sign(textToSign, year);
                        }
                        catch (Exception e) {
                            DLibUtils.showException(this, e);
                        }

                        return sign;
                    }

                    @Override
                    public String getCertificateNumber() {
                        return moCfdSignature.getCertNumber();
                    }

                    @Override
                    public String getCertificateBase64() {
                        return moCfdSignature.getCertBase64();
                    }

                    @Override
                    public int getCertificateId() {
                        return moCertificate.getPkCertificateId();
                    }
                };
                
                signatures.add(signature);
            }
        }

        return signatures;
    }

    private void logout() {
        renderMenues(null);
        actionFileCloseViewAll();

        mbLoggedIn = false;
        moSession = null;

        msCompany = "";
        setTitle(APP_NAME);

        jtfSystemDate.setText("");
        jtfWorkingDate.setText("");
        jtfUser.setText("");
        jtfUserTs.setText("");
        renderClientSession(null);

        jmFile.setEnabled(false);
        jmView.setEnabled(false);
        jmHelp.setEnabled(false);
        jmiFileWorkingDate.setEnabled(false);
        jmiFileSessionSettings.setEnabled(false);
        jmiViewModuleCfg.setEnabled(false);
        jmiViewModuleFin.setEnabled(false);
        jmiViewModulePur.setEnabled(false);
        jmiViewModuleSal.setEnabled(false);
        jmiViewModuleInv.setEnabled(false);
        jmiViewModuleMkt.setEnabled(false);
        jmiViewModulePos.setEnabled(false);
        jmiViewModuleSrv.setEnabled(false);

        //jmiViewModuleCfg.setVisible(false);
        jmiViewModuleFin.setVisible(false);
        jmiViewModulePur.setVisible(false);
        jmiViewModuleSal.setVisible(false);
        jmiViewModuleInv.setVisible(false);
        jmiViewModuleMkt.setVisible(false);
        jmiViewModulePos.setVisible(false);
        jmiViewModuleSrv.setVisible(false);

        jbWorkingDate.setEnabled(false);
        jbSessionSettings.setEnabled(false);
        jtbModuleCfg.setEnabled(false);
        jtbModuleFin.setEnabled(false);
        jtbModulePur.setEnabled(false);
        jtbModuleSal.setEnabled(false);
        jtbModuleInv.setEnabled(false);
        jtbModuleMkt.setEnabled(false);
        jtbModulePos.setEnabled(false);
        jtbModuleSrv.setEnabled(false);

        //jtbModuleCfg.setVisible(false);
        jtbModuleFin.setVisible(false);
        jtbModulePur.setVisible(false);
        jtbModuleSal.setVisible(false);
        jtbModuleInv.setVisible(false);
        jtbModuleMkt.setVisible(false);
        jtbModulePos.setVisible(false);
        jtbModuleSrv.setVisible(false);

        jbgModules.clearSelection();
    }

    private void login() {
        int modulesAccessed = 0;
        String sql = "";
        ResultSet resultSet = null;
        Date date = null;
        DDbUser user = null;
        DDbConfigCompany configCompany = null;
        DDbConfigBranch configBranch = null;
        DModModuleTrn moduleTrn = null;
        DUtilLoginDlg loginDlg = new DUtilLoginDlg(this);
        JToggleButton defaultToggleButton = null;

        loginDlg.setVisible(true);

        if (loginDlg.getFormResult() != DGuiConsts.FORM_RESULT_OK) {
            actionFileExit();
        }
        else {
            try {
                // Get system date:

                sql = "SELECT NOW() ";
                resultSet = miSysStatement.executeQuery(sql);
                if (!resultSet.next()) {
                    throw new Exception(DUtilConsts.ERR_MSG_SYS_DATE);
                }
                else {
                    date = resultSet.getTimestamp(1);
                }

                // Process login:

                mbLoggedIn = true;
                moSession = new DGuiSession(this);
                moSession.setSystemDate(date);
                moSession.setWorkingDate(date);
                moSession.setUserTs(date);
                moSession.setDatabase(loginDlg.getDatabase());

                configCompany = new DDbConfigCompany();
                configCompany.read(moSession, new int[] { DUtilConsts.BPR_CO_ID });

                configBranch = new DDbConfigBranch();
                configBranch.read(moSession, new int[] { DUtilConsts.BPR_CO_ID, DUtilConsts.BPR_BRA_ID });

                user = new DDbUser();
                user.read(moSession, loginDlg.getUserKey());

                moSession.setConfigSystem(null);
                moSession.setConfigCompany(configCompany);
                moSession.setConfigBranch(configBranch);
                moSession.setConfigBranchHq(configBranch.clone());
                moSession.setUser(user);
                moSession.setModuleUtils(new DModUtils());
                moSession.getModules().add(new DModModuleSys(this));
                moSession.getModules().add(new DModModuleCfg(this));
                moSession.getModules().add(new DModModuleBpr(this));
                moSession.getModules().add(new DModModuleItm(this));
                moSession.getModules().add(new DModModuleFin(this));
                moduleTrn = new DModModuleTrn(this, DModSysConsts.CS_MOD_PUR);
                moduleTrn.setUserFormsMap(moUserFormsMap);
                moSession.getModules().add(moduleTrn);
                moduleTrn = new DModModuleTrn(this, DModSysConsts.CS_MOD_SAL);
                moduleTrn.setUserFormsMap(moUserFormsMap);
                moSession.getModules().add(moduleTrn);
                moduleTrn = new DModModuleTrn(this, DModSysConsts.CS_MOD_INV);
                moduleTrn.setUserFormsMap(moUserFormsMap);
                moSession.getModules().add(moduleTrn);
                moSession.getModules().add(new DModModuleMkt(this));
                moSession.getModules().add(new DModModulePos(this));
                moSession.getModules().add(new DModModuleSrv(this));

                moSession.getUser().computeAccess(moSession);
                moSession.setSessionCustom(moSession.getUser().createDefaultUserSession(this, mnTerminal));
                moSession.getEdsSignatures().addAll(createEdsSignatures());

                msCompany = loginDlg.getCompany();
                setTitle(APP_NAME + " - " + msCompany);

                jtfSystemDate.setText(DLibUtils.DateFormatDate.format(moSession.getSystemDate()));
                jtfWorkingDate.setText(DLibUtils.DateFormatDate.format(moSession.getWorkingDate()));
                jtfUser.setText(user.getName());
                jtfUserTs.setText(DLibUtils.DateFormatDatetimeTimeZone.format(date));

                jmFile.setEnabled(true);
                jmView.setEnabled(true);
                jmHelp.setEnabled(true);

                jbWorkingDate.setEnabled(user.hasPrivilege(DModSysConsts.CS_PRV_SYS_DAT_WKG));
                jbSessionSettings.setEnabled(true);

                //jtbModuleCfg.setVisible(configCompany.isModuleConfiguration());
                jtbModuleFin.setVisible(configCompany.isModuleFinance());
                jtbModulePur.setVisible(configCompany.isModulePurchases());
                jtbModuleSal.setVisible(configCompany.isModuleSales());
                jtbModuleInv.setVisible(configCompany.isModuleInventory());
                jtbModuleMkt.setVisible(configCompany.isModuleMarketing());
                jtbModulePos.setVisible(configCompany.isModulePointOfSale());
                jtbModuleSrv.setVisible(configCompany.isModuleServices());

                //jmiViewModuleCfg.setVisible(jtbModuleCfg.isVisible());
                jmiViewModuleFin.setVisible(jtbModuleFin.isVisible());
                jmiViewModulePur.setVisible(jtbModulePur.isVisible());
                jmiViewModuleSal.setVisible(jtbModuleSal.isVisible());
                jmiViewModuleInv.setVisible(jtbModuleInv.isVisible());
                jmiViewModuleMkt.setVisible(jtbModuleMkt.isVisible());
                jmiViewModulePos.setVisible(jtbModulePos.isVisible());
                jmiViewModuleSrv.setVisible(jtbModuleSrv.isVisible());

                if (user.hasModuleAccess(DModSysConsts.CS_MOD_CFG)) {
                    modulesAccessed++;
                    jtbModuleCfg.setEnabled(true);
                    if (defaultToggleButton == null) {
                        defaultToggleButton = jtbModuleCfg;
                    }
                }

                if (user.hasModuleAccess(DModSysConsts.CS_MOD_FIN)) {
                    modulesAccessed++;
                    jtbModuleFin.setEnabled(true);
                    if (defaultToggleButton == null) {
                        defaultToggleButton = jtbModuleFin;
                    }
                }

                if (user.hasModuleAccess(DModSysConsts.CS_MOD_PUR)) {
                    modulesAccessed++;
                    jtbModulePur.setEnabled(true);
                    if (defaultToggleButton == null) {
                        defaultToggleButton = jtbModulePur;
                    }
                }

                if (user.hasModuleAccess(DModSysConsts.CS_MOD_SAL)) {
                    modulesAccessed++;
                    jtbModuleSal.setEnabled(true);
                    if (defaultToggleButton == null) {
                        defaultToggleButton = jtbModuleSal;
                    }
                }

                if (user.hasModuleAccess(DModSysConsts.CS_MOD_INV)) {
                    /*
                    modulesAccessed++;
                    jtbModuleInv.setEnabled(true);
                    if (defaultToggleButton == null) {
                        defaultToggleButton = jtbModuleInv;
                    }
                    */
                }

                if (user.hasModuleAccess(DModSysConsts.CS_MOD_MKT)) {
                    modulesAccessed++;
                    jtbModuleMkt.setEnabled(true);
                    if (defaultToggleButton == null) {
                        defaultToggleButton = jtbModuleMkt;
                    }
                }

                if (user.hasModuleAccess(DModSysConsts.CS_MOD_POS)) {
                    modulesAccessed++;
                    jtbModulePos.setEnabled(true);
                    if (defaultToggleButton == null) {
                        defaultToggleButton = jtbModulePos;
                    }
                }

                if (user.hasModuleAccess(DModSysConsts.CS_MOD_SRV)) {
                    modulesAccessed++;
                    jtbModuleSrv.setEnabled(true);
                    if (defaultToggleButton == null) {
                        defaultToggleButton = jtbModuleSrv;
                    }
                }

                jmiFileWorkingDate.setEnabled(jbWorkingDate.isEnabled());
                jmiFileSessionSettings.setEnabled(jbSessionSettings.isEnabled());
                jmiViewModuleCfg.setEnabled(jtbModuleCfg.isEnabled());
                jmiViewModuleFin.setEnabled(jtbModuleFin.isEnabled());
                jmiViewModulePur.setEnabled(jtbModulePur.isEnabled());
                jmiViewModuleSal.setEnabled(jtbModuleSal.isEnabled());
                jmiViewModuleInv.setEnabled(jtbModuleInv.isEnabled());
                jmiViewModuleMkt.setEnabled(jtbModuleMkt.isEnabled());
                jmiViewModulePos.setEnabled(jtbModulePos.isEnabled());
                jmiViewModuleSrv.setEnabled(jtbModuleSrv.isEnabled());

                moDialogUserSession.setRegistry((DDbUser) moSession.getUser());

                if (!moSession.getUser().showUserSessionConfigOnLogin()) {
                    renderClientSession((DGuiClientSessionCustom) moSession.getSessionCustom());
                }
                else {
                    moDialogUserSession.setVisible(true);

                    if (moDialogUserSession.getFormResult() != DGuiConsts.FORM_RESULT_OK) {
                        actionFileCloseSession();
                    }
                    else {
                        renderClientSession(moDialogUserSession.getClientSession());
                    }
                }

                if (defaultToggleButton != null) {
                    defaultToggleButton.requestFocus();
                }

                if (modulesAccessed == 1) {
                    defaultToggleButton.doClick();
                }
            }
            catch (SQLException e) {
                DLibUtils.showException(this, e);
                actionFileExit();
            }
            catch (Exception e) {
                DLibUtils.showException(this, e);
                actionFileExit();
            }
        }
    }

    private void actionToggleViewModule(int type, int subtype) {
        renderMenues(moSession.getModule(type, subtype).getMenus());

        if (jtpTabbedPane.getTabCount() == 0) {
            if (type == DModConsts.MOD_POS) {
                moSession.showView(DModConsts.TX_MY_DPS_DOC, DModSysConsts.TS_DPS_CT_SAL, null);
            }
        }
    }

    public void actionFileWorkingDate() {
        moDatePicker.resetPicker();
        moDatePicker.setOption(moSession.getWorkingDate());
        moDatePicker.setVisible(true);

        if (moDatePicker.getPickerResult() == DGuiConsts.FORM_RESULT_OK) {
            moSession.setWorkingDate(moDatePicker.getOption());
            jtfWorkingDate.setText(DLibUtils.DateFormatDate.format(moSession.getWorkingDate()));
        }
    }

    public void actionFileSessionSettings() {
        moDialogUserSession.resetForm();
        moDialogUserSession.setVisible(true);

        if (moDialogUserSession.getFormResult() == DGuiConsts.FORM_RESULT_OK) {
            renderClientSession(moDialogUserSession.getClientSession());
        }
    }

    public void actionFileUserPassword() {
        new DUtilPasswordDlg(this).setVisible(true);
    }

    public void actionFileCloseViewAll() {
        try {
            DGuiUtils.setCursorWait(this);

            for (int i = 0; i < jtpTabbedPane.getTabCount(); i++) {
                ((DGridPaneView) jtpTabbedPane.getComponentAt(i)).paneViewClosed(); // this preserves view user settings
            }

            jtpTabbedPane.removeAll();
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
        finally {
            DGuiUtils.setCursorDefault(this);
        }
    }

    public void actionFileCloseViewOther() {
        int i = 0;
        int index = jtpTabbedPane.getSelectedIndex();

        try {
            DGuiUtils.setCursorWait(this);

            for (i = jtpTabbedPane.getTabCount() - 1; i > index; i--) {
                ((DGridPaneView) jtpTabbedPane.getComponentAt(i)).paneViewClosed(); // this preserves view user settings
                jtpTabbedPane.removeTabAt(i);
            }

            for (i = 0; i < index; i++) {
                ((DGridPaneView) jtpTabbedPane.getComponentAt(0)).paneViewClosed(); // this preserves view user settings
                jtpTabbedPane.removeTabAt(0);
            }
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
        finally {
            DGuiUtils.setCursorDefault(this);
        }
    }

    public void actionFileCloseSession() {
        logout();
        login();
    }

    public void actionFileExit() {
        logout();
        System.exit(0);
    }

    public void actionViewModule(JToggleButton toggleButton) {
        toggleButton.doClick();
        toggleButton.requestFocus();
    }

    public void actionHelpHelp() {

    }

    public void actionHelpAbout() {

    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (java.lang.Exception e) {
            DLibUtils.showException(DGuiClientApp.class.getName(), e);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DGuiClientApp().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbSessionSettings;
    private javax.swing.JButton jbWorkingDate;
    private javax.swing.ButtonGroup jbgModules;
    private javax.swing.JLabel jlAppRelease;
    private javax.swing.JMenu jmFile;
    private javax.swing.JMenu jmHelp;
    private javax.swing.JMenu jmView;
    private javax.swing.JMenuBar jmbMenu;
    private javax.swing.JMenuItem jmiFileCloseSession;
    private javax.swing.JMenuItem jmiFileCloseViewsAll;
    private javax.swing.JMenuItem jmiFileCloseViewsOther;
    private javax.swing.JMenuItem jmiFileExit;
    private javax.swing.JMenuItem jmiFileSessionSettings;
    private javax.swing.JMenuItem jmiFileUserPassword;
    private javax.swing.JMenuItem jmiFileWorkingDate;
    private javax.swing.JMenuItem jmiHelpAbout;
    private javax.swing.JMenuItem jmiHelpHelp;
    private javax.swing.JMenuItem jmiViewModuleCfg;
    private javax.swing.JMenuItem jmiViewModuleFin;
    private javax.swing.JMenuItem jmiViewModuleInv;
    private javax.swing.JMenuItem jmiViewModuleMkt;
    private javax.swing.JMenuItem jmiViewModulePos;
    private javax.swing.JMenuItem jmiViewModulePur;
    private javax.swing.JMenuItem jmiViewModuleSal;
    private javax.swing.JMenuItem jmiViewModuleSrv;
    private javax.swing.JPanel jpStatusBar;
    private javax.swing.JSeparator jsFile1;
    private javax.swing.JSeparator jsFile2;
    private javax.swing.JSeparator jsFile3;
    private javax.swing.JSeparator jsHelp1;
    private javax.swing.JToggleButton jtbModuleCfg;
    private javax.swing.JToggleButton jtbModuleFin;
    private javax.swing.JToggleButton jtbModuleInv;
    private javax.swing.JToggleButton jtbModuleMkt;
    private javax.swing.JToggleButton jtbModulePos;
    private javax.swing.JToggleButton jtbModulePur;
    private javax.swing.JToggleButton jtbModuleSal;
    private javax.swing.JToggleButton jtbModuleSrv;
    private javax.swing.JToolBar jtbToolBar;
    private javax.swing.JTextField jtfSessionBranch;
    private javax.swing.JTextField jtfSessionBranchCash;
    private javax.swing.JTextField jtfSessionBranchDpsSeries;
    private javax.swing.JTextField jtfSessionBranchWarehouse;
    private javax.swing.JTextField jtfSystemDate;
    private javax.swing.JTextField jtfTerminal;
    private javax.swing.JTextField jtfUser;
    private javax.swing.JTextField jtfUserTs;
    private javax.swing.JTextField jtfWorkingDate;
    private javax.swing.JTabbedPane jtpTabbedPane;
    // End of variables declaration//GEN-END:variables

    /*
     * Overriden methods
     */

    @Override
    public JFrame getFrame() {
        return this;
    }

    @Override
    public JTabbedPane getTabbedPane() {
        return jtpTabbedPane;
    }

    @Override
    public DDbDatabase getSysDatabase() {
        return moSysDatabase;
    }

    @Override
    public Statement getSysStatement() {
        return miSysStatement;
    }

    @Override
    public DGuiSession getSession() {
        return moSession;
    }

    @Override
    public DGuiDatePicker getDatePicker() {
        return moDatePicker;
    }

    @Override
    public DGuiDateRangePicker getDateRangePicker() {
        return moDateRangePicker;
    }

    @Override
    public DGuiYearPicker getYearPicker() {
        return moYearPicker;
    }

    @Override
    public DGuiYearMonthPicker getYearMonthPicker() {
        return moYearMonthPicker;
    }

    @Override
    public JFileChooser getFileChooser() {
        return moFileChooser;
    }

    @Override
    public ImageIcon getImageIcon(final int icon) {
        ImageIcon imageIcon = null;

        switch (icon) {
            case DImgConsts.ICO_GUI_CLOSE:
                imageIcon = moIconGuiClose;
                break;
            case DImgConsts.ICO_GUI_CLOSE_INA:
                imageIcon = moIconGuiCloseIna;
                break;
            case DImgConsts.ICO_GUI_CLOSE_BRI:
                imageIcon = moIconGuiCloseBri;
                break;
            case DImgConsts.ICO_GUI_CLOSE_DAR:
                imageIcon = moIconGuiCloseDar;
                break;
            case DImgConsts.CMD_STD_OK:
                imageIcon = moIconCmdStdOk;
                break;
            case DImgConsts.CMD_STD_DELETE:
                imageIcon = moIconCmdStdDelete;
                break;
            case DImgConsts.CMD_STD_DISABLE:
                imageIcon = moIconCmdStdDisable;
                break;
            case DImgConsts.CMD_STD_PRINT:
                imageIcon = moIconCmdStdPrint;
                break;
            case DImgConsts.CMD_STD_CARDEX:
                imageIcon = moIconCmdStdCardex;
                break;
            case DImgConsts.CMD_STD_VIEW:
                imageIcon = moIconCmdStdView;
                break;
            case DImgConsts.CMD_STD_DATE:
                imageIcon = moIconCmdStdDate;
                break;
            case DImgConsts.CMD_STD_CLEAR:
                imageIcon = moIconCmdStdClear;
                break;
            case DImgConsts.CMD_STD_ADD:
                imageIcon = moIconCmdStdAdd;
                break;
            case DImgConsts.CMD_STD_SUBTRACT:
                imageIcon = moIconCmdStdSubtract;
                break;
            case DImgConsts.CMD_STD_LOT:
                imageIcon = moIconCmdStdLot;
                break;
            case DImgConsts.CMD_STD_SER_NUM:
                imageIcon = moIconCmdStdSerialNumber;
                break;
            case DImgConsts.CMD_STD_ADJ_DISC:
                imageIcon = moIconCmdStdAdjustmentDiscount;
                break;
            case DImgConsts.CMD_STD_ADJ_RET:
                imageIcon = moIconCmdStdAdjustmentReturn;
                break;
            case DImgConsts.CMD_STD_ADJ_DOC:
                imageIcon = moIconCmdStdAdjustmentDocument;
                break;
            case DImgConsts.CMD_STD_ADJ_LOT:
                imageIcon = moIconCmdStdAdjustmentLot;
                break;
            case DImgConsts.CMD_STD_ADJ_SER_NUM:
                imageIcon = moIconCmdStdAdjustmentSerialNumber;
                break;
            case DImgConsts.CMD_STD_NOTE:
                imageIcon = moIconCmdStdNote;
                break;
            case DImgConsts.CMD_STD_SIGN:
                imageIcon = moIconCmdStdSign;
                break;
            case DImgConsts.CMD_STD_SIGN_VER:
                imageIcon = moIconCmdStdSignVer;
                break;
            case DImgConsts.CMD_STD_CANCEL:
                imageIcon = moIconCmdStdCancel;
                break;
            case DImgConsts.CMD_STD_CANCEL_VER:
                imageIcon = moIconCmdStdCancelVer;
                break;
            case DImgConsts.CMD_STD_SEND:
                imageIcon = moIconCmdStdSend;
                break;
            case DImgConsts.CMD_STD_EXPORT:
                imageIcon = moIconCmdStdExport;
                break;
            case DImgConsts.CMD_STD_IMPORT:
                imageIcon = moIconCmdStdImport;
                break;
            default:
                showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return imageIcon;
    }

    @Override
    public DGuiUserGui readUserGui(final int[] key) {
        DDbUserGui userGui = new DDbUserGui();

        try {
            userGui.read(moSession, key);
        }
        catch (SQLException e) {
            userGui = null;
            DLibUtils.printException(this, e);
        }
        catch (Exception e) {
            userGui = null;
            DLibUtils.printException(this, e);
        }

        return userGui;
    }

    @Override
    public DGuiUserGui saveUserGui(final int[] key, final String gui) {
        DDbUserGui userGui = (DDbUserGui) readUserGui(key);

        if (userGui == null) {
            userGui = new DDbUserGui();
            userGui.setPrimaryKey(key);
        }

        try {
            userGui.setGui(gui);
            userGui.save(moSession);
        }
        catch (SQLException e) {
            DLibUtils.printException(this, e);
        }
        catch (Exception e) {
            DLibUtils.printException(this, e);
        }

        return userGui;
    }

    @Override
    public HashMap<String, Object> createReportParams() {
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("sAppName", APP_NAME);
        params.put("sAppCopyright", APP_COPYRIGHT);
        params.put("sAppProvider", APP_PROVIDER);
        params.put("sCompany", msCompany);
        params.put("sUser", moSession.getUser().getName());
        params.put("oFormatDate", DLibUtils.DateFormatDate);

        return params;
    }

    @Override
    public String getTableCompany() {
        return DModConsts.TablesMap.get(DModConsts.SU_CO);
    }

    @Override
    public String getTableUser() {
        return DModConsts.TablesMap.get(DModConsts.CU_USR);
    }

    @Override
    public String getAppName() {
        return APP_NAME;
    }

    @Override
    public String getAppRelease() {
        return APP_RELEASE;
    }

    @Override
    public String getAppCopyright() {
        return APP_COPYRIGHT;
    }

    @Override
    public String getAppProvider() {
        return APP_PROVIDER;
    }

    @Override
    public void showMsgBoxError(final String msg) {
        JOptionPane.showMessageDialog(this, msg, DGuiConsts.MSG_BOX_ERROR, JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void showMsgBoxWarning(final String msg) {
        JOptionPane.showMessageDialog(this, msg, DGuiConsts.MSG_BOX_WARNING, JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void showMsgBoxInformation(final String msg) {
        JOptionPane.showMessageDialog(this, msg, DGuiConsts.MSG_BOX_INFORMATION, JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public int showMsgBoxConfirm(final String msg) {
        return JOptionPane.showConfirmDialog(this, msg, DGuiConsts.MSG_BOX_CONFIRM, JOptionPane.YES_NO_OPTION);
    }

    @Override
    public void finalize() throws Throwable {
        super.finalize();

        if (moSysDatabaseMonitor != null && moSysDatabaseMonitor.isAlive()) {
            moSysDatabaseMonitor.stopThread();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbWorkingDate) {
                actionFileWorkingDate();
            }
            else if (button == jbSessionSettings) {
                actionFileSessionSettings();
            }
        }
        else if (e.getSource() instanceof JToggleButton) {
            JToggleButton toggleButton = (JToggleButton) e.getSource();

            if (toggleButton == jtbModuleCfg) {
                actionToggleViewModule(DModConsts.MOD_CFG, DLibConsts.UNDEFINED);
            }
            else if (toggleButton == jtbModuleFin) {
                actionToggleViewModule(DModConsts.MOD_FIN, DLibConsts.UNDEFINED);
            }
            else if (toggleButton == jtbModulePur) {
                actionToggleViewModule(DModConsts.MOD_TRN, DModSysConsts.CS_MOD_PUR);
            }
            else if (toggleButton == jtbModuleSal) {
                actionToggleViewModule(DModConsts.MOD_TRN, DModSysConsts.CS_MOD_SAL);
            }
            else if (toggleButton == jtbModuleInv) {
                actionToggleViewModule(DModConsts.MOD_TRN, DModSysConsts.CS_MOD_INV);
            }
            else if (toggleButton == jtbModuleMkt) {
                actionToggleViewModule(DModConsts.MOD_MKT, DLibConsts.UNDEFINED);
            }
            else if (toggleButton == jtbModulePos) {
                actionToggleViewModule(DModConsts.MOD_POS, DLibConsts.UNDEFINED);
            }
            else if (toggleButton == jtbModuleSrv) {
                actionToggleViewModule(DModConsts.MOD_SRV, DLibConsts.UNDEFINED);
            }
        }
        else if (e.getSource() instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem) e.getSource();

            if (menuItem == jmiFileWorkingDate) {
                actionFileWorkingDate();
            }
            else if (menuItem == jmiFileSessionSettings) {
                actionFileSessionSettings();
            }
            else if (menuItem == jmiFileUserPassword) {
                actionFileUserPassword();
            }
            else if (menuItem == jmiFileCloseViewsAll) {
                actionFileCloseViewAll();
            }
            else if (menuItem == jmiFileCloseViewsOther) {
                actionFileCloseViewOther();
            }
            else if (menuItem == jmiFileCloseSession) {
                actionFileCloseSession();
            }
            else if (menuItem == jmiFileExit) {
                actionFileExit();
            }
            else if (menuItem == jmiViewModuleCfg) {
                actionViewModule(jtbModuleCfg);
            }
            else if (menuItem == jmiViewModuleFin) {
                actionViewModule(jtbModuleFin);
            }
            else if (menuItem == jmiViewModulePur) {
                actionViewModule(jtbModulePur);
            }
            else if (menuItem == jmiViewModuleSal) {
                actionViewModule(jtbModuleSal);
            }
            else if (menuItem == jmiViewModuleInv) {
                actionViewModule(jtbModuleInv);
            }
            else if (menuItem == jmiViewModuleMkt) {
                actionViewModule(jtbModuleMkt);
            }
            else if (menuItem == jmiViewModulePos) {
                actionViewModule(jtbModulePos);
            }
            else if (menuItem == jmiViewModuleSrv) {
                actionViewModule(jtbModuleSrv);
            }
            else if (menuItem == jmiHelpHelp) {
                actionHelpHelp();
            }
            else if (menuItem == jmiHelpAbout) {
                actionHelpAbout();
            }
        }
    }
}
