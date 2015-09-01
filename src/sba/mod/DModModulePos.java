/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.grid.DGridPaneView;
import sba.lib.gui.DGuiCatalogueSettings;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiForm;
import sba.lib.gui.DGuiModule;
import sba.lib.gui.DGuiOptionPicker;
import sba.lib.gui.DGuiParams;
import sba.lib.gui.DGuiReport;
import sba.mod.cfg.db.DDbConfigCompany;

/**
 *
 * @author Sergio Flores
 */
public class DModModulePos extends DGuiModule implements ActionListener {

    private JMenu mjCat;
    private JMenuItem mjCatBizPartner;
    private JMenuItem mjCatBizPartnerConfig;
    private JMenuItem mjCatBranch;
    private JMenuItem mjCatBranchAddress;
    private JMenuItem mjCatBranchBankAccount;

    private JMenu mjDoc;
    private JMenuItem mjDocDocument;
    private JMenuItem mjDocAdjustmentInc;
    private JMenuItem mjDocAdjustmentDec;
    private JMenuItem mjDocDay;

    private JMenu mjAcc;
    private JMenuItem mjAccBizPartner;
    private JMenuItem mjAccBizPartnerCur;
    private JMenuItem mjAccBizPartnerRef;
    private JMenuItem mjAccBranch;
    private JMenuItem mjAccBranchCur;
    private JMenuItem mjAccBranchRef;

    private JMenu mjPrc;
    private JMenuItem mjPrcItemPrice;

    private JMenu mjStk;
    private JMenuItem mjStkStock;
    private JMenuItem mjStkStockLot;
    private JMenuItem mjStkStockSnr;

    private JMenu mjRep;

    public DModModulePos(DGuiClient client) {
        super(client, DModConsts.MOD_POS, DLibConsts.UNDEFINED);
        initComponents();
    }

    private void initComponents() {
        DDbConfigCompany configCompany = (DDbConfigCompany) miClient.getSession().getConfigCompany();

        mjCat = new JMenu("Catálogos");
        mjCatBizPartner = new JMenuItem("Clientes");
        mjCatBizPartnerConfig = new JMenuItem("Configuración de clientes (Q)");
        mjCatBranch = new JMenuItem("Sucursales de clientes (Q)");
        mjCatBranchAddress = new JMenuItem("Domicilios de clientes (Q)");
        mjCatBranchBankAccount = new JMenuItem("Cuentas bancarias de clientes (Q)");
        mjCatBranchBankAccount.setEnabled(false);

        mjCat.add(mjCatBizPartner);
        mjCat.addSeparator();
        mjCat.add(mjCatBizPartnerConfig);
        mjCat.add(mjCatBranch);
        mjCat.add(mjCatBranchAddress);
        mjCat.add(mjCatBranchBankAccount);

        mjCatBizPartner.addActionListener(this);
        mjCatBizPartnerConfig.addActionListener(this);
        mjCatBranch.addActionListener(this);
        mjCatBranchAddress.addActionListener(this);
        mjCatBranchBankAccount.addActionListener(this);

        mjDoc = new JMenu("Documentos ventas");
        mjDocDocument = new JMenuItem("Mis documentos");
        mjDocAdjustmentInc = new JMenuItem("Mis notas de débito");
        mjDocAdjustmentDec = new JMenuItem("Mis notas de crédito");
        mjDocDay = new JMenuItem("Mis ventas del día");

        mjDoc.add(mjDocDocument);
        mjDoc.addSeparator();
        mjDoc.add(mjDocAdjustmentInc);
        mjDoc.add(mjDocAdjustmentDec);
        mjDoc.addSeparator();
        mjDoc.add(mjDocDay);

        mjDocDocument.addActionListener(this);
        mjDocAdjustmentInc.addActionListener(this);
        mjDocAdjustmentDec.addActionListener(this);
        mjDocDay.addActionListener(this);

        mjAcc = new JMenu("Cuentas pendientes ventas");
        mjAccBizPartner = new JMenuItem("Saldos clientes");
        mjAccBizPartnerCur = new JMenuItem("Saldos clientes por moneda");
        mjAccBizPartnerRef = new JMenuItem("Saldos clientes por referencia");
        mjAccBranch = new JMenuItem("Saldos sucursales clientes");
        mjAccBranchCur = new JMenuItem("Saldos sucursales clientes por moneda");
        mjAccBranchRef = new JMenuItem("Saldos sucursales clientes por referencia");

        mjAcc.add(mjAccBizPartner);
        mjAcc.add(mjAccBizPartnerCur);
        mjAcc.add(mjAccBizPartnerRef);
        mjAcc.addSeparator();
        mjAcc.add(mjAccBranch);
        mjAcc.add(mjAccBranchCur);
        mjAcc.add(mjAccBranchRef);

        mjAccBizPartner.addActionListener(this);
        mjAccBizPartnerCur.addActionListener(this);
        mjAccBizPartnerRef.addActionListener(this);
        mjAccBranch.addActionListener(this);
        mjAccBranchCur.addActionListener(this);
        mjAccBranchRef.addActionListener(this);

        mjAcc.setEnabled(miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_POS_ACC_PEN));

        mjPrc = new JMenu("Precios");
        mjPrcItemPrice = new JMenuItem("Precios de ítems");

        mjPrc.add(mjPrcItemPrice);

        mjPrcItemPrice.addActionListener(this);

        mjPrc.setEnabled(miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_POS_PRC));

        mjStk = new JMenu("Almacenes");
        mjStkStock = new JMenuItem("Existencias");
        mjStkStockLot = new JMenuItem("Existencias por lote");
        mjStkStockSnr = new JMenuItem("Existencias por número de serie");

        mjStk.add(mjStkStock);
        mjStk.add(mjStkStockLot);
        mjStk.add(mjStkStockSnr);

        mjStkStock.addActionListener(this);
        mjStkStockLot.addActionListener(this);
        mjStkStockSnr.addActionListener(this);

        mjStk.setEnabled(miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_POS_STK));

        mjRep = new JMenu("Reportes");
        mjRep.setEnabled(false);
    }

    @Override
    public JMenu[] getMenus() {
        return new JMenu[] { mjCat, mjDoc, mjAcc, mjPrc, mjStk/*, mjRep*/ };
    }

    @Override
    public DDbRegistry getRegistry(final int type) {
        DDbRegistry registry = null;

        switch (type) {
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return registry;
    }

    @Override
    public DGuiCatalogueSettings getCatalogueSettings(final int type, final int subtype, final DGuiParams params) {
        String sql = "";
        DGuiCatalogueSettings settings = null;

        switch (type) {
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        if (settings != null) {
            settings.setSql(sql);
        }

        return settings;
    }

    @Override
    public DGridPaneView getView(final int type, final int subtype, final DGuiParams params) {
        DGridPaneView view = null;

        switch (type) {
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return view;
    }

    @Override
    public DGuiOptionPicker getOptionPicker(final int type, final int subtype, final DGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DGuiForm getForm(final int type, final int subtype, final DGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DGuiReport getReport(final int type, final int subtype, final DGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JMenuItem) {
            JMenuItem menuItem = (JMenuItem) e.getSource();

            if (menuItem == mjCatBizPartner) {
                miClient.getSession().showView(DModConsts.BU_BPR, DModSysConsts.BS_BPR_CL_CUS, null);
            }
            if (menuItem == mjCatBizPartnerConfig) {
                miClient.getSession().showView(DModConsts.BU_BPR_CFG, DModSysConsts.BS_BPR_CL_CUS, null);
            }
            else if (menuItem == mjCatBranch) {
                miClient.getSession().showView(DModConsts.BU_BRA, DModSysConsts.BS_BPR_CL_CUS, null);
            }
            else if (menuItem == mjCatBranchAddress) {
                miClient.getSession().showView(DModConsts.BU_ADD, DModSysConsts.BS_BPR_CL_CUS, null);
            }
            else if (menuItem == mjCatBranchBankAccount) {
                miClient.getSession().showView(DModConsts.BU_BNK, DModSysConsts.BS_BPR_CL_CUS, null);
            }
            else if (menuItem == mjDocDocument) {
                miClient.getSession().showView(DModConsts.TX_MY_DPS_DOC, DModSysConsts.TS_DPS_CT_SAL, null);
            }
            else if (menuItem == mjDocAdjustmentInc) {
                miClient.getSession().showView(DModConsts.TX_MY_DPS_ADJ_INC, DModSysConsts.TS_DPS_CT_SAL, null);
            }
            else if (menuItem == mjDocAdjustmentDec) {
                miClient.getSession().showView(DModConsts.TX_MY_DPS_ADJ_DEC, DModSysConsts.TS_DPS_CT_SAL, null);
            }
            else if (menuItem == mjDocDay) {
                miClient.getSession().showView(DModConsts.TX_DAY_MY_DPS, DModSysConsts.TS_DPS_CT_SAL, null);
            }
            else if (menuItem == mjAccBizPartner) {
                miClient.getSession().showView(DModConsts.FX_BAL_BPR, DModSysConsts.BS_BPR_CL_CUS, new DGuiParams(DUtilConsts.PER_BPR, DUtilConsts.PER_BPR));
            }
            else if (menuItem == mjAccBizPartnerCur) {
                miClient.getSession().showView(DModConsts.FX_BAL_BPR, DModSysConsts.BS_BPR_CL_CUS, new DGuiParams(DUtilConsts.PER_BPR, DUtilConsts.PER_CUR));
            }
            else if (menuItem == mjAccBizPartnerRef) {
                miClient.getSession().showView(DModConsts.FX_BAL_BPR, DModSysConsts.BS_BPR_CL_CUS, new DGuiParams(DUtilConsts.PER_BPR, DUtilConsts.PER_REF));
            }
            else if (menuItem == mjAccBranch) {
                miClient.getSession().showView(DModConsts.FX_BAL_BPR, DModSysConsts.BS_BPR_CL_CUS, new DGuiParams(DUtilConsts.PER_BPR_BRA, DUtilConsts.PER_BPR_BRA));
            }
            else if (menuItem == mjAccBranchCur) {
                miClient.getSession().showView(DModConsts.FX_BAL_BPR, DModSysConsts.BS_BPR_CL_CUS, new DGuiParams(DUtilConsts.PER_BPR_BRA, DUtilConsts.PER_CUR));
            }
            else if (menuItem == mjAccBranchRef) {
                miClient.getSession().showView(DModConsts.FX_BAL_BPR, DModSysConsts.BS_BPR_CL_CUS, new DGuiParams(DUtilConsts.PER_BPR_BRA, DUtilConsts.PER_REF));
            }
            else if (menuItem == mjPrcItemPrice) {
                miClient.getSession().showView(DModConsts.MX_ITM_PRC, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjStkStock) {
                miClient.getSession().showView(DModConsts.TX_STK, DUtilConsts.PER_ITM, null);
            }
            else if (menuItem == mjStkStockLot) {
                miClient.getSession().showView(DModConsts.TX_STK, DUtilConsts.PER_LOT, null);
            }
            else if (menuItem == mjStkStockSnr) {
                miClient.getSession().showView(DModConsts.TX_STK, DUtilConsts.PER_SNR, null);
            }
        }
    }
}
