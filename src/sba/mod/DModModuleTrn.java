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
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.db.DDbRegistrySysFly;
import sba.lib.grid.DGridPaneView;
import sba.lib.gui.DGuiCatalogueSettings;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiForm;
import sba.lib.gui.DGuiModule;
import sba.lib.gui.DGuiOptionPicker;
import sba.lib.gui.DGuiParams;
import sba.lib.gui.DGuiReport;
import sba.lib.gui.DGuiUserForm;
import sba.mod.bpr.db.DBprUtils;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.fin.db.DFinUtils;
import sba.mod.trn.db.DDbDps;
import sba.mod.trn.db.DDbDpsEds;
import sba.mod.trn.db.DDbDpsNote;
import sba.mod.trn.db.DDbDpsPrinting;
import sba.mod.trn.db.DDbDpsRow;
import sba.mod.trn.db.DDbDpsRowNote;
import sba.mod.trn.db.DDbDpsSending;
import sba.mod.trn.db.DDbDpsSeries;
import sba.mod.trn.db.DDbDpsSeriesBranch;
import sba.mod.trn.db.DDbDpsSeriesNumber;
import sba.mod.trn.db.DDbDpsSigning;
import sba.mod.trn.db.DDbDpsTypeChange;
import sba.mod.trn.db.DDbIog;
import sba.mod.trn.db.DDbIogNote;
import sba.mod.trn.db.DDbIogRow;
import sba.mod.trn.db.DDbIogRowNote;
import sba.mod.trn.db.DDbLot;
import sba.mod.trn.db.DDbSchedule;
import sba.mod.trn.db.DDbSerialNumberFix;
import sba.mod.trn.db.DDbStockConfig;
import sba.mod.trn.db.DDbStockMove;
import sba.mod.trn.db.DDbXmlSignatureMove;
import sba.mod.trn.db.DDbXmlSignatureRequest;
import sba.mod.trn.db.DTrnUtils;
import sba.mod.trn.form.DDialogTraceSerialNumber;
import sba.mod.trn.form.DFormDps;
import sba.mod.trn.form.DFormDpsCancelling;
import sba.mod.trn.form.DFormDpsPrinting;
import sba.mod.trn.form.DFormDpsSeries;
import sba.mod.trn.form.DFormDpsSeriesNumber;
import sba.mod.trn.form.DFormDpsSigning;
import sba.mod.trn.form.DFormDpsTypeChange;
import sba.mod.trn.form.DFormIog;
import sba.mod.trn.form.DFormSchedule;
import sba.mod.trn.form.DFormSerialNumberFix;
import sba.mod.trn.form.DFormXmlSignatureMove;
import sba.mod.trn.view.DViewDetailedTransact;
import sba.mod.trn.view.DViewDps;
import sba.mod.trn.view.DViewDpsAccounts;
import sba.mod.trn.view.DViewDpsAccountsCollected;
import sba.mod.trn.view.DViewDpsDay;
import sba.mod.trn.view.DViewDpsPrinting;
import sba.mod.trn.view.DViewDpsSending;
import sba.mod.trn.view.DViewDpsSeries;
import sba.mod.trn.view.DViewDpsSeriesBranch;
import sba.mod.trn.view.DViewDpsSeriesNumber;
import sba.mod.trn.view.DViewDpsSigning;
import sba.mod.trn.view.DViewDpsTypeChange;
import sba.mod.trn.view.DViewPerMonthTransact;
import sba.mod.trn.view.DViewPerMonthTransactAgent;
import sba.mod.trn.view.DViewPerMonthTransactBizPartner;
import sba.mod.trn.view.DViewPerMonthTransactItem;
import sba.mod.trn.view.DViewPerMonthTransactUser;
import sba.mod.trn.view.DViewSchedule;
import sba.mod.trn.view.DViewSerialNumberFix;
import sba.mod.trn.view.DViewStock;
import sba.mod.trn.view.DViewStockInventory;
import sba.mod.trn.view.DViewStockMoves;
import sba.mod.trn.view.DViewTransact;
import sba.mod.trn.view.DViewTransactAgent;
import sba.mod.trn.view.DViewTransactBizPartner;
import sba.mod.trn.view.DViewTransactItem;
import sba.mod.trn.view.DViewTransactUser;
import sba.mod.trn.view.DViewXmlSignatureMove;
import sba.mod.trn.view.DViewXmlStampsAvailable;

/**
 *
 * @author Sergio Flores
 */
public class DModModuleTrn extends DGuiModule implements ActionListener {

    private JMenu mjCat;
    private JMenuItem mjCatBizPartner;
    private JMenuItem mjCatBizPartnerConfig;
    private JMenuItem mjCatBranch;
    private JMenuItem mjCatBranchAddress;
    private JMenuItem mjCatBranchBankAccount;
    private JMenuItem mjCatBizPartnerType;
    private JMenuItem mjCatItem;
    private JMenuItem mjCatItemLine;
    private JMenuItem mjCatItemGenus;
    private JMenuItem mjCatItemFamily;
    private JMenuItem mjCatItemBrand;
    private JMenuItem mjCatItemManufacturer;
    private JMenuItem mjCatItemComponent;
    private JMenuItem mjCatItemDepartment;
    private JMenuItem mjCatItemUnit;
    private JMenuItem mjCatItemBarcode;

    private JMenu mjPrice;
    private JMenuItem mjPriceList;
    private JMenuItem mjPriceListBizPartnerType;
    private JMenuItem mjPriceListBizPartner;
    private JMenuItem mjPriceFixed;
    private JMenuItem mjPriceDiscountAditional;
    private JMenuItem mjPricePriceItem;
    private JMenuItem mjPricePriceList;

    private boolean mbOrdersApplying;

    private JMenu mjOrd;
    private JMenuItem mjOrdDoc;
    private JMenuItem mjOrdPendProc;
    private JMenuItem mjOrdPendProcDetail;
    private JMenuItem mjOrdProc;
    private JMenuItem mjOrdProcDetail;
    private JMenuItem mjOrdBackorderItem;
    private JMenuItem mjOrdBackorderBizPartner;
    private JMenuItem mjOrdBackorderDoc;

    private JMenu mjDoc;
    private JMenuItem mjDocDocInvoice;
    private JMenuItem mjDocDocNote;
    private JMenuItem mjDocDocTicket;
    private JMenuItem mjDocAdjustmentInc;
    private JMenuItem mjDocAdjustmentDec;
    private JMenuItem mjDocDay;
    private JMenuItem mjDocTypeChange;
    private JMenuItem mjDocPrintingPend;
    private JMenuItem mjDocPrinting;
    private JMenuItem mjDocSigningPend;
    private JMenuItem mjDocSigning;
    private JMenuItem mjDocSendingPend;
    private JMenuItem mjDocSending;

    private JMenu mjAcc;
    private JMenuItem mjAccBizPartner;
    private JMenuItem mjAccBizPartnerCur;
    private JMenuItem mjAccBizPartnerRef;
    private JMenuItem mjAccBranch;
    private JMenuItem mjAccBranchCur;
    private JMenuItem mjAccBranchRef;
    private JMenuItem mjAccDocPayable;
    private JMenuItem mjAccDocPayableCollected;

    private JMenu mjSup;
    private JMenuItem mjSupOrderPending;
    private JMenuItem mjSupOrderPendingDetail;
    private JMenuItem mjSupOrder;
    private JMenuItem mjSupOrderDetail;
    private JMenuItem mjSupDocPending;
    private JMenuItem mjSupDocPendingDetail;
    private JMenuItem mjSupDoc;
    private JMenuItem mjSupDocDetail;

    private JMenu mjRet;
    private JMenuItem mjRetDocPending;
    private JMenuItem mjRetDocPendingDetail;
    private JMenuItem mjRetDoc;
    private JMenuItem mjRetDocDetail;

    private JMenu mjStk;
    private JMenuItem mjStkStock;
    private JMenuItem mjStkStockLot;
    private JMenuItem mjStkStockSnr;
    private JMenuItem mjStkStockImpDec;
    private JMenuItem mjStkMoves;
    private JMenuItem mjStkMovesIn;
    private JMenuItem mjStkMovesOut;
    private JMenuItem mjStkStockInventory;
    private JMenuItem mjStkStockInventoryLot;
    private JMenuItem mjStkStockInventorySnr;
    private JMenuItem mjStkStockInventoryImpDec;
    private JMenuItem mjStkSnrFix;
    private JMenuItem mjStkSnrTrace;

    private JMenu mjRep;
    private JMenu mjRepTransac;
    private JMenu mjRepPerMonth;
    private JMenu mjRepDetailed;
    private JMenuItem mjRepTransacCompany;
    private JMenuItem mjRepTransacCompanyBranch;
    private JMenuItem mjRepTransacItemGenus;
    private JMenuItem mjRepTransacItemLine;
    private JMenuItem mjRepTransacItem;
    private JMenuItem mjRepTransacItemBizPartner;
    private JMenuItem mjRepTransacBizPartnerType;
    private JMenuItem mjRepTransacBizPartner;
    private JMenuItem mjRepTransacBizPartnerItem;
    private JMenuItem mjRepTransacAgentType;
    private JMenuItem mjRepTransacAgent;
    private JMenuItem mjRepTransacAgentItem;
    private JMenuItem mjRepTransacUserType;
    private JMenuItem mjRepTransacUser;
    private JMenuItem mjRepTransacUserItem;
    private JMenuItem mjRepPerMonthTransacCompany;
    private JMenuItem mjRepPerMonthTransacCompanyBranch;
    private JMenuItem mjRepPerMonthTransacItemGenus;
    private JMenuItem mjRepPerMonthTransacItemLine;
    private JMenuItem mjRepPerMonthTransacItem;
    private JMenuItem mjRepPerMonthTransacItemBizPartner;
    private JMenuItem mjRepPerMonthTransacBizPartnerType;
    private JMenuItem mjRepPerMonthTransacBizPartner;
    private JMenuItem mjRepPerMonthTransacBizPartnerItem;
    private JMenuItem mjRepPerMonthTransacAgentType;
    private JMenuItem mjRepPerMonthTransacAgent;
    private JMenuItem mjRepPerMonthTransacAgentItem;
    private JMenuItem mjRepPerMonthTransacUserType;
    private JMenuItem mjRepPerMonthTransacUser;
    private JMenuItem mjRepPerMonthTransacUserItem;
    private JMenuItem mjRepDetailedItem;
    private JMenuItem mjRepDetailedItemSnr;
    private JMenuItem mjRepListOrder;
    private JMenuItem mjRepListDoc;
    private JMenuItem mjRepListDocAdjInc;
    private JMenuItem mjRepListDocAdjDec;
    private JMenuItem mjRepDiary;
    private JMenuItem mjRepPriceUnitary;
    private JMenuItem mjRepPriceList;
    private JMenuItem mjRepBalance;
    private JMenuItem mjRepBalanceRef;
    private JMenuItem mjRepBalanceAging;
    private JMenuItem mjRepStateOfAccount;
    private JMenuItem mjRepStateOfAccountRef;

    private DFormDpsSeries moFormDpsSeries;
    private DFormDpsSeriesNumber moFormDpsSeriesNumber;
    private DFormSchedule moFormSchedule;
    private DFormDps moFormDpsOrder;
    private DFormDps moFormDpsDocumentInv;
    private DFormDps moFormDpsDocumentNot;
    private DFormDps moFormDpsDocumentTic;
    private DFormDps moFormDpsAdjustmentInc;
    private DFormDps moFormDpsAdjustmentDec;
    private DFormDpsPrinting moFormDpsPrinting;
    private DFormDpsSigning moFormDpsSigning;
    private DFormDpsCancelling moFormDpsCancelling;
    private DFormDpsTypeChange moFormDpsTypeChange;
    private DFormIog moFormIogExternalIn;
    private DFormIog moFormIogExternalOut;
    private DFormSerialNumberFix moFormSerialNumberFix;
    private DFormXmlSignatureMove moFormXmlSignatureMove;

    public DModModuleTrn(DGuiClient client, int subtype) {
        super(client, DModConsts.MOD_TRN, subtype);
        initComponents();
    }

    private void initComponents() {
        DDbConfigCompany configCompany = (DDbConfigCompany) miClient.getSession().getConfigCompany();

        mjPrice = new JMenu("Listas de precios");
        mjPriceList = new JMenuItem("Listas de precios");
        mjPriceListBizPartnerType = new JMenuItem("Listas de precios y tipos de proveedor");
        mjPriceListBizPartner = new JMenuItem("Listas de precios y proveedores");
        mjPriceFixed = new JMenuItem("Precios fijos");
        mjPriceDiscountAditional = new JMenuItem("Descuentos adicionales");
        mjPricePriceItem = new JMenuItem("Precios de ítems");
        mjPricePriceList = new JMenuItem("Precios de listas de precios");

        mjSup = new JMenu("Surtidos");
        mjSupOrderPending = new JMenuItem("Pedidos por surtir");
        mjSupOrderPendingDetail = new JMenuItem("Pedidos por surtir a detalle");
        mjSupOrder = new JMenuItem("Pedidos surtidos");
        mjSupOrderDetail = new JMenuItem("Pedidos surtidos a detalle");
        mjSupDocPending = new JMenuItem("Documentos por surtir");
        mjSupDocPendingDetail = new JMenuItem("Documentos por surtir a detalle");
        mjSupDoc = new JMenuItem("Documentos surtidos");
        mjSupDocDetail = new JMenuItem("Documentos surtidos a detalle");

        mjRet = new JMenu("Devoluciones");
        mjRetDocPending = new JMenuItem("Documentos por devolver");
        mjRetDocPendingDetail = new JMenuItem("Documentos por devolver a detalle");
        mjRetDoc = new JMenuItem("Documentos devueltos");
        mjRetDocDetail = new JMenuItem("Documentos devueltos a detalle");

        mbOrdersApplying = false;

        mjCat = new JMenu("Catálogos");

        switch (mnModuleSubtype) {
            case DModSysConsts.CS_MOD_PUR:


                mjCatBizPartner = new JMenuItem("Proveedores");
                mjCatBizPartnerConfig = new JMenuItem("Configuración de proveedores (Q)");
                mjCatBranch = new JMenuItem("Sucursales de proveedores (Q)");
                mjCatBranchAddress = new JMenuItem("Domicilios de proveedores (Q)");
                mjCatBranchBankAccount = new JMenuItem("Cuentas bancarias de proveedores (Q)");
                mjCatBranchBankAccount.setEnabled(false);
                mjCatBizPartnerType = new JMenuItem("Tipos de proveedor");

                mjCat.add(mjCatBizPartner);
                mjCat.addSeparator();
                mjCat.add(mjCatBizPartnerConfig);
                mjCat.add(mjCatBranch);
                mjCat.add(mjCatBranchAddress);
                mjCat.add(mjCatBranchBankAccount);
                mjCat.addSeparator();
                mjCat.add(mjCatBizPartnerType);

                mjCatBizPartner.addActionListener(this);
                mjCatBizPartnerConfig.addActionListener(this);
                mjCatBranch.addActionListener(this);
                mjCatBranchAddress.addActionListener(this);
                mjCatBranchBankAccount.addActionListener(this);
                mjCatBizPartnerType.addActionListener(this);

                mbOrdersApplying = configCompany.isOrdersPurchaseApplying();

                mjOrd = new JMenu("Pedidos compras");
                mjOrdDoc = new JMenuItem("Pedidos compras");
                /*
                mjOrdPendProc = new JMenuItem("Pedidos compras por procesar");
                mjOrdPendProcDetail = new JMenuItem("Pedidos compras por procesar a detalle");
                mjOrdProc = new JMenuItem("Pedidos compras procesados");
                mjOrdProcDetail = new JMenuItem("Pedidos compras procesados a detalle");
                mjOrdBackorderItem = new JMenuItem("Backorder compras por ítem");
                mjOrdBackorderBizPartner = new JMenuItem("Backorder compras por proveeedor");
                mjOrdBackorderDoc = new JMenuItem("Backorder compras por pedido");
                */

                mjOrd.add(mjOrdDoc);
                /*
                mjOrd.addSeparator();
                mjOrd.add(mjOrdPendProc);
                mjOrd.add(mjOrdPendProcDetail);
                mjOrd.addSeparator();
                mjOrd.add(mjOrdProc);
                mjOrd.add(mjOrdProcDetail);
                mjOrd.addSeparator();
                mjOrd.add(mjOrdBackorderItem);
                mjOrd.add(mjOrdBackorderBizPartner);
                mjOrd.add(mjOrdBackorderDoc);
                */

                mjOrdDoc.addActionListener(this);
                /*
                mjOrdPendProc.addActionListener(this);
                mjOrdPendProcDetail.addActionListener(this);
                mjOrdProc.addActionListener(this);
                mjOrdProcDetail.addActionListener(this);
                mjOrdBackorderItem.addActionListener(this);
                mjOrdBackorderBizPartner.addActionListener(this);
                mjOrdBackorderDoc.addActionListener(this);
                */

                mjDoc = new JMenu("Documentos compras");
                mjDocDocInvoice = new JMenuItem("Facturas");
                mjDocDocNote = new JMenuItem("Notas de venta");
                mjDocDocTicket = new JMenuItem("Tickets");
                mjDocAdjustmentInc = new JMenuItem("Notas de débito");
                mjDocAdjustmentDec = new JMenuItem("Notas de crédito");
                mjDocDay = new JMenuItem("Movimientos del día");
                mjDocTypeChange = new JMenuItem("Cambios de tipo de documento");
                /*
                mjDocPrintingPend = new JMenuItem("Documentos por imprimir");
                mjDocPrinting = new JMenuItem("Documentos imprimidos");
                mjDocSigningPend = new JMenuItem("Documentos por timbrar");
                mjDocSigning = new JMenuItem("Documentos timbrados");
                mjDocSendingPend = new JMenuItem("Documentos por enviar");
                mjDocSending = new JMenuItem("Documentos enviados");
                */

                mjDoc.add(mjDocDocInvoice);
                mjDoc.add(mjDocDocNote);
                mjDoc.add(mjDocDocTicket);
                mjDoc.addSeparator();
                mjDoc.add(mjDocAdjustmentInc);
                mjDoc.add(mjDocAdjustmentDec);
                mjDoc.addSeparator();
                mjDoc.add(mjDocDay);
                mjDoc.addSeparator();
                mjDoc.add(mjDocTypeChange);
                /*
                mjDoc.addSeparator();
                mjDoc.add(mjDocPrintingPend);
                mjDoc.add(mjDocPrinting);
                mjDoc.addSeparator();
                mjDoc.add(mjDocSigningPend);
                mjDoc.add(mjDocSigning);
                mjDoc.addSeparator();
                mjDoc.add(mjDocSendingPend);
                mjDoc.add(mjDocSending);
                */

                mjDocDocInvoice.addActionListener(this);
                mjDocDocNote.addActionListener(this);
                mjDocDocTicket.addActionListener(this);
                mjDocAdjustmentInc.addActionListener(this);
                mjDocAdjustmentDec.addActionListener(this);
                mjDocDay.addActionListener(this);
                mjDocTypeChange.addActionListener(this);
                /*
                mjDocPrintingPend.addActionListener(this);
                mjDocPrinting.addActionListener(this);
                mjDocSigningPend.addActionListener(this);
                mjDocSigning.addActionListener(this);
                mjDocSendingPend.addActionListener(this);
                mjDocSending.addActionListener(this);
                */

                mjAcc = new JMenu("Cuentas pendientes compras");
                mjAccBizPartner = new JMenuItem("Saldos proveedores");
                mjAccBizPartnerCur = new JMenuItem("Saldos proveedores por moneda");
                mjAccBizPartnerRef = new JMenuItem("Saldos proveedores por referencia");
                mjAccBranch = new JMenuItem("Saldos sucursales proveedores");
                mjAccBranchCur = new JMenuItem("Saldos sucursales proveedores por moneda");
                mjAccBranchRef = new JMenuItem("Saldos sucursales proveedores por referencia");
                mjAccDocPayable = new JMenuItem("Documentos por liquidar");
                mjAccDocPayableCollected = new JMenuItem("Documentos liquidados");

                mjAcc.add(mjAccBizPartner);
                mjAcc.add(mjAccBizPartnerCur);
                mjAcc.add(mjAccBizPartnerRef);
                mjAcc.addSeparator();
                mjAcc.add(mjAccBranch);
                mjAcc.add(mjAccBranchCur);
                mjAcc.add(mjAccBranchRef);
                mjAcc.addSeparator();
                mjAcc.add(mjAccDocPayable);
                mjAcc.add(mjAccDocPayableCollected);

                mjAccBizPartner.addActionListener(this);
                mjAccBizPartnerCur.addActionListener(this);
                mjAccBizPartnerRef.addActionListener(this);
                mjAccBranch.addActionListener(this);
                mjAccBranchCur.addActionListener(this);
                mjAccBranchRef.addActionListener(this);
                mjAccDocPayable.addActionListener(this);
                mjAccDocPayableCollected.addActionListener(this);

                mjRep = new JMenu("Reportes");

                mjRepTransac = new JMenu("Compras");
                mjRepTransacCompany = new JMenuItem("Compras (Q)");
                mjRepTransacCompanyBranch = new JMenuItem("Compras por sucursal (Q)");
                mjRepTransacItemGenus = new JMenuItem("Compras por género de ítems (Q)");
                mjRepTransacItemLine = new JMenuItem("Compras por línea de ítems (Q)");
                mjRepTransacItem = new JMenuItem("Compras por ítem (Q)");
                mjRepTransacItemBizPartner = new JMenuItem("Compras por ítem vs. proveedor (Q)");
                mjRepTransacBizPartnerType = new JMenuItem("Compras por tipo de proveedor (Q)");
                mjRepTransacBizPartner = new JMenuItem("Compras por proveedor (Q)");
                mjRepTransacBizPartnerItem = new JMenuItem("Compras por proveedor vs. ítem (Q)");
                mjRepTransacAgentType = new JMenuItem("Compras por tipo de agente (Q)");
                mjRepTransacAgent = new JMenuItem("Compras por agente (Q)");
                mjRepTransacAgentItem = new JMenuItem("Compras por agente vs. ítem (Q)");
                mjRepTransacUserType = new JMenuItem("Compras por tipo de usuario (Q)");
                mjRepTransacUser = new JMenuItem("Compras por usuario (Q)");
                mjRepTransacUserItem = new JMenuItem("Compras por usuario vs. ítem (Q)");

                mjRepTransac.add(mjRepTransacCompany);
                mjRepTransac.add(mjRepTransacCompanyBranch);
                mjRepTransac.addSeparator();
                mjRepTransac.add(mjRepTransacItemGenus);
                mjRepTransac.add(mjRepTransacItemLine);
                mjRepTransac.add(mjRepTransacItem);
                mjRepTransac.add(mjRepTransacItemBizPartner);
                mjRepTransac.addSeparator();
                mjRepTransac.add(mjRepTransacBizPartnerType);
                mjRepTransac.add(mjRepTransacBizPartner);
                mjRepTransac.add(mjRepTransacBizPartnerItem);
                mjRepTransac.addSeparator();
                mjRepTransac.add(mjRepTransacAgentType);
                mjRepTransac.add(mjRepTransacAgent);
                mjRepTransac.add(mjRepTransacAgentItem);
                mjRepTransac.addSeparator();
                mjRepTransac.add(mjRepTransacUserType);
                mjRepTransac.add(mjRepTransacUser);
                mjRepTransac.add(mjRepTransacUserItem);

                mjRepPerMonth = new JMenu("Compras netas por mes");
                mjRepPerMonthTransacCompany = new JMenuItem("Compras por mes (Q)");
                mjRepPerMonthTransacCompanyBranch = new JMenuItem("Compras por mes por sucursal (Q)");
                mjRepPerMonthTransacItemGenus = new JMenuItem("Compras por mes por género de ítems (Q)");
                mjRepPerMonthTransacItemLine = new JMenuItem("Compras por mes por línea de ítems (Q)");
                mjRepPerMonthTransacItem = new JMenuItem("Compras por mes por ítem (Q)");
                mjRepPerMonthTransacItemBizPartner = new JMenuItem("Compras por mes por ítem vs. proveedor (Q)");
                mjRepPerMonthTransacBizPartnerType = new JMenuItem("Compras por mes por tipo de proveedor (Q)");
                mjRepPerMonthTransacBizPartner = new JMenuItem("Compras por mes por proveedor (Q)");
                mjRepPerMonthTransacBizPartnerItem = new JMenuItem("Compras por mes por proveedor vs. ítem (Q)");
                mjRepPerMonthTransacAgentType = new JMenuItem("Compras por mes por tipo de agente (Q)");
                mjRepPerMonthTransacAgent = new JMenuItem("Compras por mes por agente (Q)");
                mjRepPerMonthTransacAgentItem = new JMenuItem("Compras por mes por agente vs. ítem (Q)");
                mjRepPerMonthTransacUserType = new JMenuItem("Compras por mes por tipo de usuario (Q)");
                mjRepPerMonthTransacUser = new JMenuItem("Compras por mes por usuario (Q)");
                mjRepPerMonthTransacUserItem = new JMenuItem("Compras por mes por usuario vs. ítem (Q)");

                mjRepPerMonth.add(mjRepPerMonthTransacCompany);
                mjRepPerMonth.add(mjRepPerMonthTransacCompanyBranch);
                mjRepPerMonth.addSeparator();
                mjRepPerMonth.add(mjRepPerMonthTransacItemGenus);
                mjRepPerMonth.add(mjRepPerMonthTransacItemLine);
                mjRepPerMonth.add(mjRepPerMonthTransacItem);
                mjRepPerMonth.add(mjRepPerMonthTransacItemBizPartner);
                mjRepPerMonth.addSeparator();
                mjRepPerMonth.add(mjRepPerMonthTransacBizPartnerType);
                mjRepPerMonth.add(mjRepPerMonthTransacBizPartner);
                mjRepPerMonth.add(mjRepPerMonthTransacBizPartnerItem);
                mjRepPerMonth.addSeparator();
                mjRepPerMonth.add(mjRepPerMonthTransacAgentType);
                mjRepPerMonth.add(mjRepPerMonthTransacAgent);
                mjRepPerMonth.add(mjRepPerMonthTransacAgentItem);
                mjRepPerMonth.addSeparator();
                mjRepPerMonth.add(mjRepPerMonthTransacUserType);
                mjRepPerMonth.add(mjRepPerMonthTransacUser);
                mjRepPerMonth.add(mjRepPerMonthTransacUserItem);

                mjRepDetailed = new JMenu("Compras detalladas");
                mjRepDetailedItem = new JMenuItem("Compras detalladas por ítem (Q)");
                mjRepDetailedItemSnr = new JMenuItem("Compras detalladas por número de serie (Q)");

                mjRepDetailed.add(mjRepDetailedItem);
                mjRepDetailed.add(mjRepDetailedItemSnr);

                mjRep.add(mjRepTransac);
                mjRep.add(mjRepPerMonth);
                mjRep.add(mjRepDetailed);

                mjRepTransacCompany.addActionListener(this);
                mjRepTransacCompanyBranch.addActionListener(this);
                mjRepTransacItemGenus.addActionListener(this);
                mjRepTransacItemLine.addActionListener(this);
                mjRepTransacItem.addActionListener(this);
                mjRepTransacItemBizPartner.addActionListener(this);
                mjRepTransacBizPartnerType.addActionListener(this);
                mjRepTransacBizPartner.addActionListener(this);
                mjRepTransacBizPartnerItem.addActionListener(this);
                mjRepTransacAgentType.addActionListener(this);
                mjRepTransacAgent.addActionListener(this);
                mjRepTransacAgentItem.addActionListener(this);
                mjRepTransacUserType.addActionListener(this);
                mjRepTransacUser.addActionListener(this);
                mjRepTransacUserItem.addActionListener(this);
                mjRepPerMonthTransacCompany.addActionListener(this);
                mjRepPerMonthTransacCompanyBranch.addActionListener(this);
                mjRepPerMonthTransacItemGenus.addActionListener(this);
                mjRepPerMonthTransacItemLine.addActionListener(this);
                mjRepPerMonthTransacItem.addActionListener(this);
                mjRepPerMonthTransacItemBizPartner.addActionListener(this);
                mjRepPerMonthTransacBizPartnerType.addActionListener(this);
                mjRepPerMonthTransacBizPartner.addActionListener(this);
                mjRepPerMonthTransacBizPartnerItem.addActionListener(this);
                mjRepPerMonthTransacAgentType.addActionListener(this);
                mjRepPerMonthTransacAgent.addActionListener(this);
                mjRepPerMonthTransacAgentItem.addActionListener(this);
                mjRepPerMonthTransacUserType.addActionListener(this);
                mjRepPerMonthTransacUser.addActionListener(this);
                mjRepPerMonthTransacUserItem.addActionListener(this);
                mjRepDetailedItem.addActionListener(this);
                mjRepDetailedItemSnr.addActionListener(this);

                mjRep.setEnabled(miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_PUR_REP));
                break;

            case DModSysConsts.CS_MOD_SAL:
                mjCatBizPartner = new JMenuItem("Clientes");
                mjCatBizPartnerConfig = new JMenuItem("Configuración de clientes (Q)");
                mjCatBranch = new JMenuItem("Sucursales de clientes (Q)");
                mjCatBranchAddress = new JMenuItem("Domicilios de clientes (Q)");
                mjCatBranchBankAccount = new JMenuItem("Cuentas bancarias de clientes (Q)");
                mjCatBranchBankAccount.setEnabled(false);
                mjCatBizPartnerType = new JMenuItem("Tipos de cliente");

                mjCat.add(mjCatBizPartner);
                mjCat.addSeparator();
                mjCat.add(mjCatBizPartnerConfig);
                mjCat.add(mjCatBranch);
                mjCat.add(mjCatBranchAddress);
                mjCat.add(mjCatBranchBankAccount);
                mjCat.addSeparator();
                mjCat.add(mjCatBizPartnerType);

                mjCatBizPartner.addActionListener(this);
                mjCatBizPartnerConfig.addActionListener(this);
                mjCatBranch.addActionListener(this);
                mjCatBranchAddress.addActionListener(this);
                mjCatBranchBankAccount.addActionListener(this);
                mjCatBizPartnerType.addActionListener(this);

                mbOrdersApplying = configCompany.isOrdersSaleApplying();

                mjOrd = new JMenu("Pedidos ventas");
                mjOrdDoc = new JMenuItem("Pedidos ventas");
                /*
                mjOrdPendProc = new JMenuItem("Pedidos ventas por procesar");
                mjOrdPendProcDetail = new JMenuItem("Pedidos ventas por procesar a detalle");
                mjOrdProc = new JMenuItem("Pedidos ventas procesados");
                mjOrdProcDetail = new JMenuItem("Pedidos ventas procesados a detalle");
                mjOrdBackorderItem = new JMenuItem("Backorder ventas por ítem");
                mjOrdBackorderBizPartner = new JMenuItem("Backorder ventas por proveeedor");
                mjOrdBackorderDoc = new JMenuItem("Backorder ventas por pedido");
                */

                mjOrd.add(mjOrdDoc);
                /*
                mjOrd.addSeparator();
                mjOrd.add(mjOrdPendProc);
                mjOrd.add(mjOrdPendProcDetail);
                mjOrd.addSeparator();
                mjOrd.add(mjOrdProc);
                mjOrd.add(mjOrdProcDetail);
                mjOrd.addSeparator();
                mjOrd.add(mjOrdBackorderItem);
                mjOrd.add(mjOrdBackorderBizPartner);
                mjOrd.add(mjOrdBackorderDoc);
                */

                mjOrdDoc.addActionListener(this);
                /*
                mjOrdPendProc.addActionListener(this);
                mjOrdPendProcDetail.addActionListener(this);
                mjOrdProc.addActionListener(this);
                mjOrdProcDetail.addActionListener(this);
                mjOrdBackorderItem.addActionListener(this);
                mjOrdBackorderBizPartner.addActionListener(this);
                mjOrdBackorderDoc.addActionListener(this);
                */

                mjDoc = new JMenu("Documentos ventas");
                mjDocDocInvoice = new JMenuItem("Facturas");
                mjDocDocNote = new JMenuItem("Notas de venta");
                mjDocDocTicket = new JMenuItem("Tickets");
                mjDocAdjustmentInc = new JMenuItem("Notas de débito");
                mjDocAdjustmentDec = new JMenuItem("Notas de crédito");
                mjDocDay = new JMenuItem("Movimientos del día");
                mjDocTypeChange = new JMenuItem("Cambios de tipo de documento");
                mjDocPrintingPend = new JMenuItem("Documentos por imprimir");
                mjDocPrinting = new JMenuItem("Documentos imprimidos");
                mjDocSigningPend = new JMenuItem("Documentos por timbrar");
                mjDocSigning = new JMenuItem("Documentos timbrados");
                mjDocSendingPend = new JMenuItem("Documentos por enviar");
                mjDocSending = new JMenuItem("Documentos enviados");

                mjDoc.add(mjDocDocInvoice);
                mjDoc.add(mjDocDocNote);
                mjDoc.add(mjDocDocTicket);
                mjDoc.addSeparator();
                mjDoc.add(mjDocAdjustmentInc);
                mjDoc.add(mjDocAdjustmentDec);
                mjDoc.addSeparator();
                mjDoc.add(mjDocDay);
                mjDoc.addSeparator();
                mjDoc.add(mjDocTypeChange);
                mjDoc.addSeparator();
                mjDoc.add(mjDocPrintingPend);
                mjDoc.add(mjDocPrinting);
                mjDoc.addSeparator();
                mjDoc.add(mjDocSigningPend);
                mjDoc.add(mjDocSigning);
                mjDoc.addSeparator();
                mjDoc.add(mjDocSendingPend);
                mjDoc.add(mjDocSending);

                mjDocDocInvoice.addActionListener(this);
                mjDocDocNote.addActionListener(this);
                mjDocDocTicket.addActionListener(this);
                mjDocAdjustmentInc.addActionListener(this);
                mjDocAdjustmentDec.addActionListener(this);
                mjDocDay.addActionListener(this);
                mjDocTypeChange.addActionListener(this);
                mjDocPrintingPend.addActionListener(this);
                mjDocPrinting.addActionListener(this);
                mjDocSigningPend.addActionListener(this);
                mjDocSigning.addActionListener(this);
                mjDocSendingPend.addActionListener(this);
                mjDocSending.addActionListener(this);

                mjAcc = new JMenu("Cuentas pendientes ventas");
                mjAccBizPartner = new JMenuItem("Saldos clientes");
                mjAccBizPartnerCur = new JMenuItem("Saldos clientes por moneda");
                mjAccBizPartnerRef = new JMenuItem("Saldos clientes por referencia");
                mjAccBranch = new JMenuItem("Saldos sucursales clientes");
                mjAccBranchCur = new JMenuItem("Saldos sucursales clientes por moneda");
                mjAccBranchRef = new JMenuItem("Saldos sucursales clientes por referencia");
                mjAccDocPayable = new JMenuItem("Documentos por liquidar");
                mjAccDocPayableCollected = new JMenuItem("Documentos liquidados");

                mjAcc.add(mjAccBizPartner);
                mjAcc.add(mjAccBizPartnerCur);
                mjAcc.add(mjAccBizPartnerRef);
                mjAcc.addSeparator();
                mjAcc.add(mjAccBranch);
                mjAcc.add(mjAccBranchCur);
                mjAcc.add(mjAccBranchRef);
                mjAcc.addSeparator();
                mjAcc.add(mjAccDocPayable);
                mjAcc.add(mjAccDocPayableCollected);

                mjAccBizPartner.addActionListener(this);
                mjAccBizPartnerCur.addActionListener(this);
                mjAccBizPartnerRef.addActionListener(this);
                mjAccBranch.addActionListener(this);
                mjAccBranchCur.addActionListener(this);
                mjAccBranchRef.addActionListener(this);
                mjAccDocPayable.addActionListener(this);
                mjAccDocPayableCollected.addActionListener(this);

                mjRep = new JMenu("Reportes");

                mjRepTransac = new JMenu("Ventas");
                mjRepTransacCompany = new JMenuItem("Ventas (Q)");
                mjRepTransacCompanyBranch = new JMenuItem("Ventas por sucursal (Q)");
                mjRepTransacItemGenus = new JMenuItem("Ventas por género de ítems (Q)");
                mjRepTransacItemLine = new JMenuItem("Ventas por línea de ítems (Q)");
                mjRepTransacItem = new JMenuItem("Ventas por ítem (Q)");
                mjRepTransacItemBizPartner = new JMenuItem("Ventas por ítem vs. cliente (Q)");
                mjRepTransacBizPartnerType = new JMenuItem("Ventas por tipo de cliente (Q)");
                mjRepTransacBizPartner = new JMenuItem("Ventas por cliente (Q)");
                mjRepTransacBizPartnerItem = new JMenuItem("Ventas por cliente vs. ítem (Q)");
                mjRepTransacAgentType = new JMenuItem("Ventas por tipo de agente (Q)");
                mjRepTransacAgent = new JMenuItem("Ventas por agente (Q)");
                mjRepTransacAgentItem = new JMenuItem("Ventas por agente vs. ítem (Q)");
                mjRepTransacUserType = new JMenuItem("Ventas por tipo de usuario (Q)");
                mjRepTransacUser = new JMenuItem("Ventas por usuario (Q)");
                mjRepTransacUserItem = new JMenuItem("Ventas por usuario vs. ítem (Q)");

                mjRepTransac.add(mjRepTransacCompany);
                mjRepTransac.add(mjRepTransacCompanyBranch);
                mjRepTransac.addSeparator();
                mjRepTransac.add(mjRepTransacItemGenus);
                mjRepTransac.add(mjRepTransacItemLine);
                mjRepTransac.add(mjRepTransacItem);
                mjRepTransac.add(mjRepTransacItemBizPartner);
                mjRepTransac.addSeparator();
                mjRepTransac.add(mjRepTransacBizPartnerType);
                mjRepTransac.add(mjRepTransacBizPartner);
                mjRepTransac.add(mjRepTransacBizPartnerItem);
                mjRepTransac.addSeparator();
                mjRepTransac.add(mjRepTransacAgentType);
                mjRepTransac.add(mjRepTransacAgent);
                mjRepTransac.add(mjRepTransacAgentItem);
                mjRepTransac.addSeparator();
                mjRepTransac.add(mjRepTransacUserType);
                mjRepTransac.add(mjRepTransacUser);
                mjRepTransac.add(mjRepTransacUserItem);

                mjRepPerMonth = new JMenu("Ventas netas por mes");
                mjRepPerMonthTransacCompany = new JMenuItem("Ventas por mes (Q)");
                mjRepPerMonthTransacCompanyBranch = new JMenuItem("Ventas por mes por sucursal (Q)");
                mjRepPerMonthTransacItemGenus = new JMenuItem("Ventas por mes por género de ítems (Q)");
                mjRepPerMonthTransacItemLine = new JMenuItem("Ventas por mes por línea de ítems (Q)");
                mjRepPerMonthTransacItem = new JMenuItem("Ventas por mes por ítem (Q)");
                mjRepPerMonthTransacItemBizPartner = new JMenuItem("Ventas por mes por ítem vs. cliente (Q)");
                mjRepPerMonthTransacBizPartnerType = new JMenuItem("Ventas por mes por tipo de cliente (Q)");
                mjRepPerMonthTransacBizPartner = new JMenuItem("Ventas por mes por cliente (Q)");
                mjRepPerMonthTransacBizPartnerItem = new JMenuItem("Ventas por mes por cliente vs. ítem (Q)");
                mjRepPerMonthTransacAgentType = new JMenuItem("Ventas por mes por tipo de agente (Q)");
                mjRepPerMonthTransacAgent = new JMenuItem("Ventas por mes por agente (Q)");
                mjRepPerMonthTransacAgentItem = new JMenuItem("Ventas por mes por agente vs. ítem (Q)");
                mjRepPerMonthTransacUserType = new JMenuItem("Ventas por mes por tipo de usuario (Q)");
                mjRepPerMonthTransacUser = new JMenuItem("Ventas por mes por usuario (Q)");
                mjRepPerMonthTransacUserItem = new JMenuItem("Ventas por mes por usuario vs. ítem (Q)");

                mjRepPerMonth.add(mjRepPerMonthTransacCompany);
                mjRepPerMonth.add(mjRepPerMonthTransacCompanyBranch);
                mjRepPerMonth.addSeparator();
                mjRepPerMonth.add(mjRepPerMonthTransacItemGenus);
                mjRepPerMonth.add(mjRepPerMonthTransacItemLine);
                mjRepPerMonth.add(mjRepPerMonthTransacItem);
                mjRepPerMonth.add(mjRepPerMonthTransacItemBizPartner);
                mjRepPerMonth.addSeparator();
                mjRepPerMonth.add(mjRepPerMonthTransacBizPartnerType);
                mjRepPerMonth.add(mjRepPerMonthTransacBizPartner);
                mjRepPerMonth.add(mjRepPerMonthTransacBizPartnerItem);
                mjRepPerMonth.addSeparator();
                mjRepPerMonth.add(mjRepPerMonthTransacAgentType);
                mjRepPerMonth.add(mjRepPerMonthTransacAgent);
                mjRepPerMonth.add(mjRepPerMonthTransacAgentItem);
                mjRepPerMonth.addSeparator();
                mjRepPerMonth.add(mjRepPerMonthTransacUserType);
                mjRepPerMonth.add(mjRepPerMonthTransacUser);
                mjRepPerMonth.add(mjRepPerMonthTransacUserItem);

                mjRepDetailed = new JMenu("Ventas detalladas");
                mjRepDetailedItem = new JMenuItem("Ventas detalladas por ítem (Q)");
                mjRepDetailedItemSnr = new JMenuItem("Ventas detalladas por número de serie (Q)");

                mjRepDetailed.add(mjRepDetailedItem);
                mjRepDetailed.add(mjRepDetailedItemSnr);

                mjRep.add(mjRepTransac);
                mjRep.add(mjRepPerMonth);
                mjRep.add(mjRepDetailed);

                mjRepTransacCompany.addActionListener(this);
                mjRepTransacCompanyBranch.addActionListener(this);
                mjRepTransacItemGenus.addActionListener(this);
                mjRepTransacItemLine.addActionListener(this);
                mjRepTransacItem.addActionListener(this);
                mjRepTransacItemBizPartner.addActionListener(this);
                mjRepTransacBizPartnerType.addActionListener(this);
                mjRepTransacBizPartner.addActionListener(this);
                mjRepTransacBizPartnerItem.addActionListener(this);
                mjRepTransacAgentType.addActionListener(this);
                mjRepTransacAgent.addActionListener(this);
                mjRepTransacAgentItem.addActionListener(this);
                mjRepTransacUserType.addActionListener(this);
                mjRepTransacUser.addActionListener(this);
                mjRepTransacUserItem.addActionListener(this);
                mjRepPerMonthTransacCompany.addActionListener(this);
                mjRepPerMonthTransacCompanyBranch.addActionListener(this);
                mjRepPerMonthTransacItemGenus.addActionListener(this);
                mjRepPerMonthTransacItemLine.addActionListener(this);
                mjRepPerMonthTransacItem.addActionListener(this);
                mjRepPerMonthTransacItemBizPartner.addActionListener(this);
                mjRepPerMonthTransacBizPartnerType.addActionListener(this);
                mjRepPerMonthTransacBizPartner.addActionListener(this);
                mjRepPerMonthTransacBizPartnerItem.addActionListener(this);
                mjRepPerMonthTransacAgentType.addActionListener(this);
                mjRepPerMonthTransacAgent.addActionListener(this);
                mjRepPerMonthTransacAgentItem.addActionListener(this);
                mjRepPerMonthTransacUserType.addActionListener(this);
                mjRepPerMonthTransacUser.addActionListener(this);
                mjRepPerMonthTransacUserItem.addActionListener(this);
                mjRepDetailedItem.addActionListener(this);
                mjRepDetailedItemSnr.addActionListener(this);

                mjRep.setEnabled(miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_SAL_REP));
                break;

            case DModSysConsts.CS_MOD_INV:
                break;

            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        mjCatItem = new JMenuItem("Ítems");
        mjCatItemLine = new JMenuItem("Líneas de ítems");
        mjCatItemGenus = new JMenuItem("Géneros de ítems");
        mjCatItemFamily = new JMenuItem("Familias de ítems");
        mjCatItemBrand = new JMenuItem("Marcas");
        mjCatItemManufacturer = new JMenuItem("Fabricantes");
        mjCatItemComponent = new JMenuItem("Componentes");
        mjCatItemDepartment = new JMenuItem("Departamentos");
        mjCatItemUnit = new JMenuItem("Unidades");
        mjCatItemBarcode = new JMenuItem("Códigos de barras");

        mjCat.addSeparator();
        mjCat.add(mjCatItem);
        mjCat.add(mjCatItemLine);
        mjCat.add(mjCatItemGenus);
        mjCat.add(mjCatItemFamily);
        mjCat.addSeparator();
        mjCat.add(mjCatItemBrand);
        mjCat.add(mjCatItemManufacturer);
        mjCat.add(mjCatItemComponent);
        mjCat.add(mjCatItemDepartment);
        mjCat.addSeparator();
        mjCat.add(mjCatItemUnit);
        mjCat.addSeparator();
        mjCat.add(mjCatItemBarcode);

        mjCatItem.addActionListener(this);
        mjCatItemLine.addActionListener(this);
        mjCatItemGenus.addActionListener(this);
        mjCatItemFamily.addActionListener(this);
        mjCatItemBrand.addActionListener(this);
        mjCatItemManufacturer.addActionListener(this);
        mjCatItemComponent.addActionListener(this);
        mjCatItemDepartment.addActionListener(this);
        mjCatItemUnit.addActionListener(this);
        mjCatItemBarcode.addActionListener(this);

        mjCatItemLine.setEnabled(configCompany.isItemLineApplying());
        mjCatItemBrand.setEnabled(configCompany.isBrandApplying());
        mjCatItemManufacturer.setEnabled(configCompany.isManufacturerApplying());
        mjCatItemComponent.setEnabled(configCompany.isComponentApplying());
        mjCatItemDepartment.setEnabled(configCompany.isDepartmentApplying());

        mjStk = new JMenu("Almacenes");
        mjStkStock = new JMenuItem("Existencias");
        mjStkStockLot = new JMenuItem("Existencias por lote");
        mjStkStockSnr = new JMenuItem("Existencias con número de serie");
        mjStkStockImpDec = new JMenuItem("Existencias con pedimento de importación");
        mjStkMoves = new JMenuItem("Movimientos de bienes");
        mjStkMovesIn = new JMenuItem("Entradas de bienes");
        mjStkMovesOut = new JMenuItem("Salidas de bienes");
        mjStkStockInventory = new JMenuItem("Inventarios de bienes");
        mjStkStockInventoryLot = new JMenuItem("Inventarios de bienes por lote");
        mjStkStockInventorySnr = new JMenuItem("Inventarios de bienes con número de serie");
        mjStkStockInventoryImpDec = new JMenuItem("Inventarios de bienes con pedimento de importación");
        mjStkSnrFix = new JMenuItem("Cambios de número de serie");
        mjStkSnrTrace = new JMenuItem("Rastreo de número de serie...");

        mjStk.add(mjStkStock);
        mjStk.add(mjStkStockLot);
        mjStk.add(mjStkStockSnr);
        mjStk.add(mjStkStockImpDec);
        mjStk.addSeparator();
        mjStk.add(mjStkMoves);
        mjStk.add(mjStkMovesIn);
        mjStk.add(mjStkMovesOut);
        mjStk.addSeparator();
        mjStk.add(mjStkStockInventory);
        mjStk.add(mjStkStockInventoryLot);
        mjStk.add(mjStkStockInventorySnr);
        mjStk.add(mjStkStockInventoryImpDec);
        mjStk.addSeparator();
        mjStk.add(mjStkSnrFix);
        mjStk.add(mjStkSnrTrace);

        mjStkStock.addActionListener(this);
        mjStkStockLot.addActionListener(this);
        mjStkStockSnr.addActionListener(this);
        mjStkStockImpDec.addActionListener(this);
        mjStkMoves.addActionListener(this);
        mjStkMovesIn.addActionListener(this);
        mjStkMovesOut.addActionListener(this);
        mjStkStockInventory.addActionListener(this);
        mjStkStockInventoryLot.addActionListener(this);
        mjStkStockInventorySnr.addActionListener(this);
        mjStkStockInventoryImpDec.addActionListener(this);
        mjStkSnrFix.addActionListener(this);
        mjStkSnrTrace.addActionListener(this);

        mjStkMoves.setEnabled(miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_INV_ADM));
        mjStkMovesIn.setEnabled(false);
        mjStkMovesOut.setEnabled(false);
        mjStkStockInventory.setEnabled(miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_INV_ADM));
        mjStkStockInventoryLot.setEnabled(miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_INV_ADM));
        mjStkStockInventorySnr.setEnabled(miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_INV_ADM));
        mjStkStockInventoryImpDec.setEnabled(miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_INV_ADM));
        mjStkSnrFix.setEnabled(miClient.getSession().getUser().hasPrivilege(DModSysConsts.CS_PRV_INV_ADM));

        mjRepListOrder = new JMenuItem("Relación de pedidos");
        mjRepListDoc = new JMenuItem("Relación de documentos");
        mjRepListDocAdjInc = new JMenuItem("Relaciónn de documentos de ajuste inc.");
        mjRepListDocAdjDec = new JMenuItem("Relaciónn de documentos de ajuste dec.");
        mjRepDiary = new JMenuItem("Reporte diario...");
        mjRepPriceUnitary = new JMenuItem("Reporte de precios unitarios...");
        mjRepPriceList = new JMenuItem("Listas de precios...");
        mjRepBalance = new JMenuItem("Saldos...");
        mjRepBalanceRef = new JMenuItem("Saldos por referencia...");
        mjRepBalanceAging = new JMenuItem("Antigüedad de saldos...");
        mjRepStateOfAccount = new JMenuItem("Estado de cuenta...");
        mjRepStateOfAccountRef = new JMenuItem("Estado de cuenta por referencia...");
    }

    @Override
    public JMenu[] getMenus() {
        return mbOrdersApplying ? new JMenu[] { mjCat, mjOrd, mjDoc, mjAcc, mjStk, mjRep } : new JMenu[] { mjCat, mjDoc, mjAcc, mjStk, mjRep };
    }

    @Override
    public DDbRegistry getRegistry(final int type) {
        DDbRegistry registry = null;

        switch (type) {
            case DModConsts.TS_FIS_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_fis_tp = " + pk[0] + " "; }
                };
                break;
            case DModConsts.TS_DPS_CT:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_dps_ct = " + pk[0] + " "; }
                };
                break;
            case DModConsts.TS_DPS_CL:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_dps_ct = " + pk[0] + " AND id_dps_cl = " + pk[1] + " "; }
                };
                break;
            case DModConsts.TS_DPS_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_dps_ct = " + pk[0] + " AND id_dps_cl = " + pk[1] + " AND id_dps_tp = " + pk[2] + " "; }
                };
                break;
            case DModConsts.TS_DPS_ST:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_dps_st = " + pk[0] + " "; }
                };
                break;
            case DModConsts.TS_ADJ_CT:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_adj_ct = " + pk[0] + " "; }
                };
                break;
            case DModConsts.TS_ADJ_CL:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_adj_ct = " + pk[0] + " AND id_adj_cl = " + pk[1] + " "; }
                };
                break;
            case DModConsts.TS_ADJ_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_adj_ct = " + pk[0] + " AND id_adj_cl = " + pk[1] + " AND id_adj_tp = " + pk[2] + " "; }
                };
                break;
            case DModConsts.TS_EMI_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_emi_tp = " + pk[0] + " "; }
                };
                break;
            case DModConsts.TS_BLK_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_blk_tp = " + pk[0] + " "; }
                };
                break;
            case DModConsts.TS_XML_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_xml_tp = " + pk[0] + " "; }
                };
                break;
            case DModConsts.TS_XML_ST:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_xml_st = " + pk[0] + " "; }
                };
                break;
            case DModConsts.TS_XSM_CL:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_xsm_cl = " + pk[0] + " "; }
                };
                break;
            case DModConsts.TS_XSM_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_xsm_cl = " + pk[0] + " AND id_xsm_tp = " + pk[1] + " "; }
                };
                break;
            case DModConsts.TS_DNP_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_dnp_tp = " + pk[0] + " "; }
                };
                break;
            case DModConsts.TS_IOG_CT:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_iog_ct = " + pk[0] + " "; }
                };
                break;
            case DModConsts.TS_IOG_CL:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_iog_ct = " + pk[0] + " AND id_iog_cl = " + pk[1] + " "; }
                };
                break;
            case DModConsts.TS_IOG_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_iog_ct = " + pk[0] + " AND id_iog_cl = " + pk[1] + " AND id_iog_tp = " + pk[2] + " "; }
                };
                break;
            case DModConsts.TS_IOM_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_iom_tp = " + pk[0] + " "; }
                };
                break;
            case DModConsts.TS_CUT_TP:
                registry = new DDbRegistrySysFly(type) {
                    public String getSqlTable() { return DModConsts.TablesMap.get(mnRegistryType); }
                    public String getSqlWhere(int[] pk) { return "WHERE id_cut_tp = " + pk[0] + " "; }
                };
                break;
            case DModConsts.TU_SER:
                registry = new DDbDpsSeries();
                break;
            case DModConsts.TU_SER_BRA:
                registry = new DDbDpsSeriesBranch();
                break;
            case DModConsts.TU_SER_NUM:
                registry = new DDbDpsSeriesNumber();
                break;
            case DModConsts.TU_SCH:
                registry = new DDbSchedule();
                break;
            case DModConsts.T_DPS:
            case DModConsts.TX_DPS_ORD:
            case DModConsts.TX_DPS_DOC_INV:
            case DModConsts.TX_DPS_DOC_NOT:
            case DModConsts.TX_DPS_DOC_TIC:
            case DModConsts.TX_DPS_ADJ_INC:
            case DModConsts.TX_DPS_ADJ_DEC:
            case DModConsts.TX_MY_DPS_ORD:
            case DModConsts.TX_MY_DPS_DOC:
            case DModConsts.TX_MY_DPS_ADJ_INC:
            case DModConsts.TX_MY_DPS_ADJ_DEC:
                registry = new DDbDps();
                break;
            case DModConsts.T_DPS_PRT:
                registry = new DDbDpsPrinting();
                break;
            case DModConsts.T_DPS_SIG:
                registry = new DDbDpsSigning();
                break;
            case DModConsts.T_DPS_SND:
                registry = new DDbDpsSending();
                break;
            case DModConsts.T_DPS_EDS:
                registry = new DDbDpsEds();
                break;
            case DModConsts.T_DPS_NOT:
                registry = new DDbDpsNote();
                break;
            case DModConsts.T_DPS_ROW:
                registry = new DDbDpsRow();
                break;
            case DModConsts.T_DPS_ROW_NOT:
                registry = new DDbDpsRowNote();
                break;
            case DModConsts.T_DPS_CHG:
                registry = new DDbDpsTypeChange();
                break;
            case DModConsts.T_IOG:
            case DModConsts.TX_IOG_PUR:
            case DModConsts.TX_IOG_SAL:
            case DModConsts.TX_IOG_EXT:
            case DModConsts.TX_IOG_INT:
            case DModConsts.TX_IOG_MFG:
            case DModConsts.TX_STK_MOV:
                registry = new DDbIog();
                break;
            case DModConsts.T_IOG_NOT:
                registry = new DDbIogNote();
                break;
            case DModConsts.T_IOG_ROW:
                registry = new DDbIogRow();
                break;
            case DModConsts.T_IOG_ROW_NOT:
                registry = new DDbIogRowNote();
                break;
            case DModConsts.T_LOT:
                registry = new DDbLot();
                break;
            case DModConsts.T_STK_CFG:
                registry = new DDbStockConfig();
                break;
            case DModConsts.T_STK:
                registry = new DDbStockMove();
                break;
            case DModConsts.T_SNR_FIX:
                registry = new DDbSerialNumberFix();
                break;
            case DModConsts.T_XSR:
                registry = new DDbXmlSignatureRequest();
                break;
            case DModConsts.T_XSM:
                registry = new DDbXmlSignatureMove();
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return registry;
    }

    @Override
    public DGuiCatalogueSettings getCatalogueSettings(final int type, final int subtype, final DGuiParams params) {
        int currency = DLibConsts.UNDEFINED;
        int[] bkkNumberKey = null;
        boolean balanceCy = false;
        String sql = "";
        String sum = "";
        String sumCy = "";
        String having = "";
        String columns = "";
        String columnsAll = "";
        DGuiCatalogueSettings settings = null;

        switch (type) {
            case DModConsts.TS_FIS_TP:
                settings = new DGuiCatalogueSettings("Tipo docto fiscal", 1);
                sql = "SELECT id_fis_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.TS_DPS_CT:
                settings = new DGuiCatalogueSettings("Categoría docto", 1);
                sql = "SELECT id_dps_ct AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.TS_DPS_CL:
                settings = new DGuiCatalogueSettings("Clase docto", 2, 1);
                sql = "SELECT id_dps_ct AS " + DDbConsts.FIELD_ID + "1, id_dps_cl AS " + DDbConsts.FIELD_ID + "2, " +
                        "name AS " + DDbConsts.FIELD_ITEM + ", id_dps_ct AS " + DDbConsts.FIELD_FK + "1 " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 " +
                        (params == null ? "" : "AND id_dps_ct = " + params.getKey()[0] + " ") +
                        "ORDER BY id_dps_ct, sort ";
                break;
            case DModConsts.TS_DPS_TP:
                settings = new DGuiCatalogueSettings("Tipo docto", 3, 2);
                sql = "SELECT id_dps_ct AS " + DDbConsts.FIELD_ID + "1, id_dps_cl AS " + DDbConsts.FIELD_ID + "2, id_dps_tp AS " + DDbConsts.FIELD_ID + "3, " +
                        "name AS " + DDbConsts.FIELD_ITEM + ", id_dps_ct AS " + DDbConsts.FIELD_FK + "1, id_dps_cl AS " + DDbConsts.FIELD_FK + "2 " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 " +
                        (params == null ? "" : "AND id_dps_ct = " + params.getKey()[0] + " AND id_dps_cl = " + params.getKey()[1] + " ") +
                        "ORDER BY id_dps_ct, id_dps_cl, sort ";
                break;
            case DModConsts.TS_DPS_ST:
                settings = new DGuiCatalogueSettings("Estatus docto", 1);
                sql = "SELECT id_dps_st AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.TS_ADJ_CT:
                settings = new DGuiCatalogueSettings("Categoría ajuste", 1);
                sql = "SELECT id_adj_ct AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.TS_ADJ_CL:
                settings = new DGuiCatalogueSettings("Clase ajuste", 2, 1);
                sql = "SELECT id_adj_ct AS " + DDbConsts.FIELD_ID + "1, id_adj_cl AS " + DDbConsts.FIELD_ID + "2, " +
                        "name AS " + DDbConsts.FIELD_ITEM + ", id_adj_ct AS " + DDbConsts.FIELD_FK + "1 " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 " +
                        (params == null ? "" : "AND id_adj_ct = " + params.getKey()[0] + " ") +
                        "ORDER BY id_adj_ct, sort ";
                break;
            case DModConsts.TS_ADJ_TP:
                settings = new DGuiCatalogueSettings("Tipo ajuste", 3, 2);
                sql = "SELECT id_adj_ct AS " + DDbConsts.FIELD_ID + "1, id_adj_cl AS " + DDbConsts.FIELD_ID + "2, id_adj_tp AS " + DDbConsts.FIELD_ID + "3, " +
                        "name AS " + DDbConsts.FIELD_ITEM + ", id_adj_ct AS " + DDbConsts.FIELD_FK + "1, id_adj_cl AS " + DDbConsts.FIELD_FK + "2 " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 " +
                        (params == null ? "" : "AND id_adj_ct = " + params.getKey()[0] + " AND id_adj_cl = " + params.getKey()[1] + " ") +
                        "ORDER BY id_adj_ct, id_adj_cl, sort ";
                break;
            case DModConsts.TS_EMI_TP:
                settings = new DGuiCatalogueSettings("Tipo emisión", 1);
                sql = "SELECT id_emi_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.TS_BLK_TP:
                settings = new DGuiCatalogueSettings("Tipo bloqueo doctos", 1);
                sql = "SELECT id_blk_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.TS_XML_TP:
                settings = new DGuiCatalogueSettings("Tipo XML", 1);
                sql = "SELECT id_xml_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.TS_XML_ST:
                settings = new DGuiCatalogueSettings("Status XML", 1);
                sql = "SELECT id_xml_st AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.TS_XSM_CL:
                settings = new DGuiCatalogueSettings("Clase mov. timbrado XML", 1);
                sql = "SELECT id_xsm_cl AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.TS_XSM_TP:
                settings = new DGuiCatalogueSettings("Tipo mov. timbrado XML", 2, 1);
                sql = "SELECT id_xsm_cl AS " + DDbConsts.FIELD_ID + "1, id_xsm_tp AS " + DDbConsts.FIELD_ID + "2, " +
                        "name AS " + DDbConsts.FIELD_ITEM + ", id_xsm_cl AS " + DDbConsts.FIELD_FK + "1 " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY id_xsm_cl, sort ";
                break;
            case DModConsts.TS_DNP_TP:
                settings = new DGuiCatalogueSettings("Tipo política foliado", 1);
                sql = "SELECT id_dnp_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.TS_IOG_CT:
                settings = new DGuiCatalogueSettings("Categoría docto E/S", 1);
                sql = "SELECT id_iog_ct AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.TS_IOG_CL:
                settings = new DGuiCatalogueSettings("Clase docto E/S", 2, 1);
                sql = "SELECT id_iog_ct AS " + DDbConsts.FIELD_ID + "1, id_iog_cl AS " + DDbConsts.FIELD_ID + "2, " +
                        "name AS " + DDbConsts.FIELD_ITEM + ", id_iog_ct AS " + DDbConsts.FIELD_FK + "1 " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY id_iog_ct, sort ";
                break;
            case DModConsts.TS_IOG_TP:
                settings = new DGuiCatalogueSettings("Tipo docto E/S", 3, 2);
                sql = "SELECT id_iog_ct AS " + DDbConsts.FIELD_ID + "1, id_iog_cl AS " + DDbConsts.FIELD_ID + "2, id_iog_tp AS " + DDbConsts.FIELD_ID + "3, " +
                        "name AS " + DDbConsts.FIELD_ITEM + ", id_iog_ct AS " + DDbConsts.FIELD_FK + "1, id_iog_cl AS " + DDbConsts.FIELD_FK + "2 " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY id_iog_ct, id_iog_cl, sort ";
                break;
            case DModConsts.TS_IOM_TP:
                settings = new DGuiCatalogueSettings("Tipo docto I/E", 1);
                sql = "SELECT id_iom_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.TS_CUT_TP:
                settings = new DGuiCatalogueSettings("Tipo corte caja", 1);
                sql = "SELECT id_cut_tp AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_del = 0 ORDER BY sort ";
                break;
            case DModConsts.TU_SER:
                settings = new DGuiCatalogueSettings("Serie docto", 1, 3);
                sql = "SELECT id_ser AS " + DDbConsts.FIELD_ID + "1, ser AS " + DDbConsts.FIELD_ITEM + ", " +
                        "fk_dps_ct AS " + DDbConsts.FIELD_FK + "1, fk_dps_cl AS " + DDbConsts.FIELD_FK + "2, fk_dps_tp AS " + DDbConsts.FIELD_FK + "3 " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_dis = 0 AND b_del = 0 ORDER BY fk_dps_ct, fk_dps_cl, fk_dps_tp, ser, id_ser ";
                break;
            case DModConsts.TU_SER_BRA:
                break;
            case DModConsts.TU_SER_NUM:
                break;
            case DModConsts.TU_SCH:
                settings = new DGuiCatalogueSettings("Horario", 1);
                sql = "SELECT id_sch AS " + DDbConsts.FIELD_ID + "1, name AS " + DDbConsts.FIELD_ITEM + " " +
                        "FROM " + DModConsts.TablesMap.get(type) + " WHERE b_dis = 0 AND b_del = 0 ORDER BY name, id_sch ";
                break;
            case DModConsts.TX_ACC_PAY:
            case DModConsts.TX_ACC_PAY_COL:
            case DModConsts.TX_ACC_PAY_ALL:
                switch (type) {
                    case DModConsts.TX_ACC_PAY:
                        settings = new DGuiCatalogueSettings("Documento x liquidar", 1);
                        break;
                    case DModConsts.TX_ACC_PAY_COL:
                        settings = new DGuiCatalogueSettings("Documento liquidado", 1);
                        break;
                    case DModConsts.TX_ACC_PAY_ALL:
                        settings = new DGuiCatalogueSettings("Documento", 1);
                        break;
                    default:
                        miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }

                if (params.getParamsMap().get(DModSysConsts.PARAM_CUR) != null) {
                    currency = (Integer) params.getParamsMap().get(DModSysConsts.PARAM_CUR);
                }

                if (params.getParamsMap().get(DModSysConsts.PARAM_BKK_NUM_KEY) != null) {
                    bkkNumberKey = (int[]) params.getParamsMap().get(DModSysConsts.PARAM_BKK_NUM_KEY);
                }

                if (params.getParamsMap().get(DModSysConsts.PARAM_BAL) != null) {
                    balanceCy = (Integer) params.getParamsMap().get(DModSysConsts.PARAM_BAL) == DUtilConsts.BAL_CY;
                }

                columns = columnsAll = "d.id_dps AS " + DDbConsts.FIELD_ID + "1, " +
                        "CONCAT(d.ser, IF(LENGTH(d.ser) = 0, '', '-'), d.num, '; ', d.dt, '; ', ";

                switch ((Integer) params.getParamsMap().get(DModSysConsts.PARAM_BPR_CL)) {
                    case DModSysConsts.BS_BPR_CL_VEN:
                    case DModSysConsts.BS_BPR_CL_CDR:
                        sum = "SUM(bkk.cdt - bkk.dbt) ";
                        sumCy = "SUM(bkk.cdt_cy - bkk.dbt_cy) ";
                        break;
                    case DModSysConsts.BS_BPR_CL_CUS:
                    case DModSysConsts.BS_BPR_CL_DBR:
                        sum = "SUM(bkk.dbt - bkk.cdt) ";
                        sumCy = "SUM(bkk.dbt_cy - bkk.cdt_cy) ";
                        break;
                    default:
                        miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }

                columns += "d.tot_cy_r, '; ', " + sumCy + ", " +
                        "' ', c.code) AS " + DDbConsts.FIELD_ITEM + ", " + sum + " ";

                columnsAll += "d.tot_cy_r, '; ', " + 0.0 + ", " +
                        "' ', c.code) AS " + DDbConsts.FIELD_ITEM + ", " + 0.0 + " ";

                switch (type) {
                    case DModConsts.TX_ACC_PAY:
                        having = "HAVING " + sumCy + " <> 0 " + (balanceCy ? "" : "OR " + sum + " <> 0 ");
                        break;
                    case DModConsts.TX_ACC_PAY_COL:
                        having = "HAVING " + sumCy + " = 0 " + (balanceCy ? "" : "AND " + sum + " = 0 ");
                        break;
                    case DModConsts.TX_ACC_PAY_ALL:
                        having = "";
                        break;
                    default:
                }

                sql = "SELECT " + columns +
                        "FROM " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " AS bkk " +
                        "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " AS d ON " +
                        "bkk.b_del = 0 AND bkk.id_yer = " + (Integer) params.getParamsMap().get(DModSysConsts.PARAM_YEAR) + " AND " +
                        "bkk.fk_sys_acc_tp = " + DFinUtils.getSysAccountTypeForBizPartnerClass((Integer) params.getParamsMap().get(DModSysConsts.PARAM_BPR_CL)) + " AND " +
                        "bkk.fk_bpr_bpr_n = " + (Integer) params.getParamsMap().get(DModSysConsts.PARAM_BPR) + " AND " +
                        "bkk.fk_dps_inv_n = d.id_dps " + (currency == DLibConsts.UNDEFINED ? "" : "AND d.fk_cur = " + currency + " ") +
                        (bkkNumberKey == null || (bkkNumberKey[0] == 0 && bkkNumberKey[1] == 0) ? "" : "AND NOT (bkk.fk_bkk_yer_n = " + bkkNumberKey[0] + " AND bkk.fk_bkk_num_n = " + bkkNumberKey[1] + ") ") +
                        "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_CUR) + " AS c ON " +
                        "d.fk_cur = c.id_cur " +
                        "WHERE bkk.b_del = 0 " +
                        "GROUP BY d.id_dps, d.ser, d.num, d.dt " +
                        having;

                if (type == DModConsts.TX_ACC_PAY_ALL) {
                    sql += "UNION " +
                            "SELECT " + columnsAll +
                            "FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS) + " AS d " +
                            "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.CS_CUR) + " AS c ON " +
                            "d.fk_cur = c.id_cur AND " +
                            "d.fk_dps_ct = " + DTrnUtils.getDpsCategoryByBizPartnerClass((Integer) params.getParamsMap().get(DModSysConsts.PARAM_BPR_CL)) + " AND " +
                            "d.fk_bpr_bpr = " + (Integer) params.getParamsMap().get(DModSysConsts.PARAM_BPR) + " " +
                            "WHERE " +
                            "d.id_dps NOT IN (SELECT bkk.fk_dps_inv_n " +
                            "FROM " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " AS bkk " +
                            "WHERE bkk.b_del = 0 AND bkk.id_yer = " + (Integer) params.getParamsMap().get(DModSysConsts.PARAM_YEAR) + " AND " +
                            "bkk.fk_bpr_bpr_n = " + (Integer) params.getParamsMap().get(DModSysConsts.PARAM_BPR) + ") AND " +
                            "d.id_dps IN (SELECT bkk.fk_dps_inv_n " +
                            "FROM " + DModConsts.TablesMap.get(DModConsts.F_BKK) + " AS bkk " +
                            "WHERE bkk.b_del = 0 AND bkk.id_yer = " + ((Integer) params.getParamsMap().get(DModSysConsts.PARAM_YEAR) - 1) + " AND " +
                            "bkk.fk_bpr_bpr_n = " + (Integer) params.getParamsMap().get(DModSysConsts.PARAM_BPR) + ") ";
                }

                sql += "ORDER BY " + DDbConsts.FIELD_ITEM + ", " + DDbConsts.FIELD_ID + "1 ";
                break;
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
            case DModConsts.TU_SER:
                view = new DViewDpsSeries(miClient, "Series doctos");
                break;
            case DModConsts.TU_SER_BRA:
                view = new DViewDpsSeriesBranch(miClient, "Series doctos y sucursales Q");
                break;
            case DModConsts.TU_SER_NUM:
                view = new DViewDpsSeriesNumber(miClient, "Folios doctos");
                break;
            case DModConsts.TU_SCH:
                view = new DViewSchedule(miClient, "Horarios");
                break;
            case DModConsts.T_DPS_PRT:
                view = new DViewDpsPrinting(miClient, subtype, params.getType(), DTrnUtils.getModuleAcronym(mnModuleSubtype) + " - Doctos " + (params.getType() == DUtilConsts.EMT_PEND ? "x imprimir" : "imprimidos"));
                break;
            case DModConsts.T_DPS_SIG:
                view = new DViewDpsSigning(miClient, subtype, params.getType(), DTrnUtils.getModuleAcronym(mnModuleSubtype) + " - Doctos " + (params.getType() == DUtilConsts.EMT_PEND ? "x timbrar" : "timbrados"));
                break;
            case DModConsts.T_DPS_SND:
                view = new DViewDpsSending(miClient, subtype, params.getType(), DTrnUtils.getModuleAcronym(mnModuleSubtype) + " - Doctos " + (params.getType() == DUtilConsts.EMT_PEND ? "x enviar" : "enviados"));
                break;
            case DModConsts.T_DPS_CHG:
                view = new DViewDpsTypeChange(miClient, subtype, DTrnUtils.getModuleAcronym(mnModuleSubtype) + " - Cambios tipo docto");
                break;
            case DModConsts.TX_DPS_ORD:
            case DModConsts.TX_DPS_DOC_INV:
            case DModConsts.TX_DPS_DOC_NOT:
            case DModConsts.TX_DPS_DOC_TIC:
            case DModConsts.TX_DPS_ADJ_INC:
            case DModConsts.TX_DPS_ADJ_DEC:
                view = new DViewDps(miClient, type, subtype, DTrnUtils.getModuleAcronym(mnModuleSubtype) + " - " + DTrnUtils.getDpsXTypeNamePlr(type));
                break;
            case DModConsts.TX_MY_DPS_ORD:
            case DModConsts.TX_MY_DPS_DOC:
            case DModConsts.TX_MY_DPS_ADJ_INC:
            case DModConsts.TX_MY_DPS_ADJ_DEC:
                view = new DViewDps(miClient, type, subtype, DTrnUtils.getModuleAcronym(mnModuleSubtype) + " - " + DTrnUtils.getMyDpsXTypeNamePlr(type));
                break;
            case DModConsts.TX_STK:
                switch (subtype) {
                    case DUtilConsts.PER_ITM:
                        view = new DViewStock(miClient, type, subtype, "Existencias");
                        break;
                    case DUtilConsts.PER_LOT:
                        view = new DViewStock(miClient, type, subtype, "Existencias lote");
                        break;
                    case DUtilConsts.PER_SNR:
                        view = new DViewStock(miClient, type, subtype, "Existencias ns");
                        break;
                    case DUtilConsts.PER_IMP_DEC:
                        view = new DViewStock(miClient, type, subtype, "Existencias ped. imp.");
                        break;
                    default:
                        miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case DModConsts.T_XSM:
                view = new DViewXmlSignatureMove(miClient, "Movimientos timbrado");
                break;
            case DModConsts.TX_XSM_AVA:
                view = new DViewXmlStampsAvailable(miClient, "Timbres disponibles");
                break;
            case DModConsts.TX_STK_MOV:
                view = new DViewStockMoves(miClient, "Movs bienes");
                break;
            case DModConsts.TX_STK_INV:
                switch (subtype) {
                    case DUtilConsts.PER_ITM:
                        view = new DViewStockInventory(miClient, type, subtype, "Inventarios");
                        break;
                    case DUtilConsts.PER_LOT:
                        view = new DViewStockInventory(miClient, type, subtype, "Inventarios lote");
                        break;
                    case DUtilConsts.PER_SNR:
                        view = new DViewStockInventory(miClient, type, subtype, "Inventarios ns");
                        break;
                    case DUtilConsts.PER_IMP_DEC:
                        view = new DViewStockInventory(miClient, type, subtype, "Inventarios ped. imp.");
                        break;
                    default:
                        miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case DModConsts.T_SNR_FIX:
                view = new DViewSerialNumberFix(miClient, "Cambios número serie");
                break;
            case DModConsts.TX_ACC_PAY:
                view = new DViewDpsAccounts(miClient, type, subtype, DTrnUtils.getModuleAcronym(mnModuleSubtype) + " - " + "Doctos x liquidar");
                break;
            case DModConsts.TX_ACC_PAY_COL:
                view = new DViewDpsAccountsCollected(miClient, type, subtype, DTrnUtils.getModuleAcronym(mnModuleSubtype) + " - " + "Doctos liquidados");
                break;
            case DModConsts.TX_DAY_DPS:
            case DModConsts.TX_DAY_MY_DPS:
                view = new DViewDpsDay(miClient, type, subtype, DTrnUtils.getModuleAcronym(mnModuleSubtype) + " - " + "Movs del día");
                break;
            case DModConsts.TX_TRN_CO:
                view = new DViewTransact(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " Q");
                break;
            case DModConsts.TX_TRN_CO_BRA:
                view = new DViewTransact(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " suc Q");
                break;
            case DModConsts.TX_TRN_ITM:
                view = new DViewTransactItem(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " ítem Q", null);
                break;
            case DModConsts.TX_TRN_ITM_LIN:
                view = new DViewTransactItem(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " línea ítems Q", null);
                break;
            case DModConsts.TX_TRN_ITM_GEN:
                view = new DViewTransactItem(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " género ítems Q", null);
                break;
            case DModConsts.TX_TRN_ITM_BPR:
                view = new DViewTransactItem(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " ítem vs. " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(subtype)).toLowerCase() + " Q", null);
                break;
            case DModConsts.TX_TRN_BPR:
                view = new DViewTransactBizPartner(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(subtype)).toLowerCase() + " Q");
                break;
            case DModConsts.TX_TRN_BPR_TP:
                view = new DViewTransactBizPartner(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " tipo " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(subtype)).toLowerCase() + " Q");
                break;
            case DModConsts.TX_TRN_BPR_ITM:
                view = new DViewTransactBizPartner(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(subtype)).toLowerCase() + " vs. ítem Q");
                break;
            case DModConsts.TX_TRN_AGT:
                view = new DViewTransactAgent(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " agente Q");
                break;
            case DModConsts.TX_TRN_AGT_TP:
                view = new DViewTransactAgent(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " tipo agente Q");
                break;
            case DModConsts.TX_TRN_AGT_ITM:
                view = new DViewTransactAgent(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " agente vs. ítem Q");
                break;
            case DModConsts.TX_TRN_USR:
                view = new DViewTransactUser(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " usuario Q");
                break;
            case DModConsts.TX_TRN_USR_TP:
                view = new DViewTransactUser(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " tipo usuario Q");
                break;
            case DModConsts.TX_TRN_USR_ITM:
                view = new DViewTransactUser(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " usuario vs. ítem Q");
                break;
            case DModConsts.TX_PMO_CO:
                view = new DViewPerMonthTransact(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " mes Q");
                break;
            case DModConsts.TX_PMO_CO_BRA:
                view = new DViewPerMonthTransact(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " mes suc Q");
                break;
            case DModConsts.TX_PMO_ITM:
                view = new DViewPerMonthTransactItem(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " mes ítem Q", null);
                break;
            case DModConsts.TX_PMO_ITM_LIN:
                view = new DViewPerMonthTransactItem(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " mes línea ítems Q", null);
                break;
            case DModConsts.TX_PMO_ITM_GEN:
                view = new DViewPerMonthTransactItem(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " mes género ítems Q", null);
                break;
            case DModConsts.TX_PMO_ITM_BPR:
                view = new DViewPerMonthTransactItem(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " mes ítem vs. " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(subtype)).toLowerCase() + " Q", null);
                break;
            case DModConsts.TX_PMO_BPR:
                view = new DViewPerMonthTransactBizPartner(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " mes " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(subtype)).toLowerCase() + " Q");
                break;
            case DModConsts.TX_PMO_BPR_TP:
                view = new DViewPerMonthTransactBizPartner(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " mes tipo " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(subtype)).toLowerCase() + " Q");
                break;
            case DModConsts.TX_PMO_BPR_ITM:
                view = new DViewPerMonthTransactBizPartner(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " mes " + DBprUtils.getBizPartnerClassNameSng(DTrnUtils.getBizPartnerClassByDpsCategory(subtype)).toLowerCase() + " vs. ítem Q");
                break;
            case DModConsts.TX_PMO_AGT:
                view = new DViewPerMonthTransactAgent(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " mes agente Q");
                break;
            case DModConsts.TX_PMO_AGT_TP:
                view = new DViewPerMonthTransactAgent(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " mes tipo agente Q");
                break;
            case DModConsts.TX_PMO_AGT_ITM:
                view = new DViewPerMonthTransactAgent(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " mes agente vs. ítem Q");
                break;
            case DModConsts.TX_PMO_USR:
                view = new DViewPerMonthTransactUser(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " mes usuario Q");
                break;
            case DModConsts.TX_PMO_USR_TP:
                view = new DViewPerMonthTransactUser(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " mes tipo usuario Q");
                break;
            case DModConsts.TX_PMO_USR_ITM:
                view = new DViewPerMonthTransactUser(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " mes usuario vs. ítem Q");
                break;
            case DModConsts.TX_DET_ITM:
                view = new DViewDetailedTransact(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " det. ítem Q");
                break;
            case DModConsts.TX_DET_ITM_SNR:
                view = new DViewDetailedTransact(miClient, type, subtype, DTrnUtils.getDpsCategoryName(subtype) + " det. numeros serie Q");
                break;
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
        DGuiForm form = null;
        DGuiUserForm userForm = new DGuiUserForm(miClient.getSession().getUser().getPkUserId(), type, subtype);

        switch (type) {
            case DModConsts.TU_SER:
                if (moFormDpsSeries == null) moFormDpsSeries = new DFormDpsSeries(miClient, "Serie de documento");
                form = moFormDpsSeries;
                break;
            case DModConsts.TU_SER_BRA:
                break;
            case DModConsts.TU_SER_NUM:
                if (moFormDpsSeriesNumber == null) moFormDpsSeriesNumber = new DFormDpsSeriesNumber(miClient, "Folio de documento");
                form = moFormDpsSeriesNumber;
                break;
            case DModConsts.TU_SCH:
                if (moFormSchedule == null) moFormSchedule = new DFormSchedule(miClient, "Horario");
                form = moFormSchedule;
                break;
            case DModConsts.T_DPS_PRT:
                if (moFormDpsPrinting == null) moFormDpsPrinting = new DFormDpsPrinting(miClient, "Impresión de documento");
                form = moFormDpsPrinting;
                break;
            case DModConsts.T_DPS_SIG:
                switch (subtype) {
                    case DModSysConsts.TX_XMS_REQ_TP_SIG:
                        if (moFormDpsSigning == null) moFormDpsSigning = new DFormDpsSigning(miClient, "Timbrado de documento");
                        form = moFormDpsSigning;
                        break;
                    case DModSysConsts.TX_XMS_REQ_TP_CAN:
                        if (moFormDpsCancelling == null) moFormDpsCancelling = new DFormDpsCancelling(miClient, "Cancelación de documento");
                        form = moFormDpsCancelling;
                        break;
                    default:
                        miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                break;
            case DModConsts.T_DPS_CHG:
                if (moFormDpsTypeChange == null) moFormDpsTypeChange = new DFormDpsTypeChange(miClient, "Cambio tipo de documento");
                form = moFormDpsTypeChange;
                break;
            case DModConsts.TX_DPS_ORD:
            case DModConsts.TX_MY_DPS_ORD:
                if (moFormDpsOrder == null) {
                    moFormDpsOrder = (DFormDps) moUserFormsMap.get(userForm.hashCode());
                    if (moFormDpsOrder == null) {
                        moFormDpsOrder = new DFormDps(miClient, DModConsts.TX_DPS_ORD, subtype, DTrnUtils.getModuleAcronym(mnModuleSubtype) + " - " + DTrnUtils.getDpsXTypeNameSng(DModConsts.TX_DPS_ORD));
                        moUserFormsMap.put(userForm.hashCode(), moFormDpsOrder);
                    }
                }
                form = moFormDpsOrder;
                break;
            case DModConsts.TX_DPS_DOC_INV:
                if (moFormDpsDocumentInv == null) {
                    moFormDpsDocumentInv = (DFormDps) moUserFormsMap.get(userForm.hashCode());
                    if (moFormDpsDocumentInv == null) {
                        moFormDpsDocumentInv = new DFormDps(miClient, type, subtype, DTrnUtils.getModuleAcronym(mnModuleSubtype) + " - " + DTrnUtils.getDpsXTypeNameSng(type));
                        moUserFormsMap.put(userForm.hashCode(), moFormDpsDocumentInv);
                    }
                }
                form = moFormDpsDocumentInv;
                break;
            case DModConsts.TX_DPS_DOC_NOT:
                if (moFormDpsDocumentNot == null) {
                    moFormDpsDocumentNot = (DFormDps) moUserFormsMap.get(userForm.hashCode());
                    if (moFormDpsDocumentNot == null) {
                        moFormDpsDocumentNot = new DFormDps(miClient, type, subtype, DTrnUtils.getModuleAcronym(mnModuleSubtype) + " - " + DTrnUtils.getDpsXTypeNameSng(type));
                        moUserFormsMap.put(userForm.hashCode(), moFormDpsDocumentNot);
                    }
                }
                form = moFormDpsDocumentNot;
                break;
            case DModConsts.TX_DPS_DOC_TIC:
                if (moFormDpsDocumentTic == null) {
                    moFormDpsDocumentTic = (DFormDps) moUserFormsMap.get(userForm.hashCode());
                    if (moFormDpsDocumentTic == null) {
                        moFormDpsDocumentTic = new DFormDps(miClient, type, subtype, DTrnUtils.getModuleAcronym(mnModuleSubtype) + " - " + DTrnUtils.getDpsXTypeNameSng(type));
                        moUserFormsMap.put(userForm.hashCode(), moFormDpsDocumentTic);
                    }
                }
                form = moFormDpsDocumentTic;
                break;
            case DModConsts.TX_DPS_ADJ_INC:
            case DModConsts.TX_MY_DPS_ADJ_INC:
                if (moFormDpsAdjustmentInc == null) {
                    moFormDpsAdjustmentInc = (DFormDps) moUserFormsMap.get(userForm.hashCode());
                    if (moFormDpsAdjustmentInc == null) {
                        moFormDpsAdjustmentInc = new DFormDps(miClient, DModConsts.TX_DPS_ADJ_INC, subtype, DTrnUtils.getModuleAcronym(mnModuleSubtype) + " - " + DTrnUtils.getDpsXTypeNameSng(DModConsts.TX_DPS_ADJ_INC));
                        moUserFormsMap.put(userForm.hashCode(), moFormDpsAdjustmentInc);
                    }
                }
                form = moFormDpsAdjustmentInc;
                break;
            case DModConsts.TX_DPS_ADJ_DEC:
            case DModConsts.TX_MY_DPS_ADJ_DEC:
                if (moFormDpsAdjustmentDec == null) {
                    moFormDpsAdjustmentDec = (DFormDps) moUserFormsMap.get(userForm.hashCode());
                    if (moFormDpsAdjustmentDec == null) {
                        moFormDpsAdjustmentDec = new DFormDps(miClient, DModConsts.TX_DPS_ADJ_DEC, subtype, DTrnUtils.getModuleAcronym(mnModuleSubtype) + " - " + DTrnUtils.getDpsXTypeNameSng(DModConsts.TX_DPS_ADJ_DEC));
                        moUserFormsMap.put(userForm.hashCode(), moFormDpsAdjustmentDec);
                    }
                }
                form = moFormDpsAdjustmentDec;
                break;
            case DModConsts.TX_IOG_PUR:
                break;
            case DModConsts.TX_IOG_SAL:
                break;
            case DModConsts.TX_IOG_EXT:
                switch (subtype) {
                    case DModSysConsts.TS_IOG_CT_IN:
                        if (moFormIogExternalIn == null) moFormIogExternalIn = new DFormIog(miClient, type, subtype, DTrnUtils.getIogCategoryName(subtype));
                        form = moFormIogExternalIn;
                        break;
                    case DModSysConsts.TS_IOG_CT_OUT:
                        if (moFormIogExternalOut == null) moFormIogExternalOut = new DFormIog(miClient, type, subtype, DTrnUtils.getIogCategoryName(subtype));
                        form = moFormIogExternalOut;
                        break;
                    default:
                        miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
                }
                form.setValue(DModSysConsts.PARAM_IOG_TP, params.getTypeKey());
                break;
            case DModConsts.TX_IOG_INT:
                break;
            case DModConsts.TX_IOG_MFG:
                break;
            case DModConsts.T_SNR_FIX:
                if (moFormSerialNumberFix == null) moFormSerialNumberFix = new DFormSerialNumberFix(miClient, "Cambio de número de serie");
                form = moFormSerialNumberFix;
                break;
            case DModConsts.T_XSM:
                if (moFormXmlSignatureMove == null) moFormXmlSignatureMove = new DFormXmlSignatureMove(miClient, "Movimiento timbrado");
                form = moFormXmlSignatureMove;
                break;
            default:
                miClient.showMsgBoxError(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        return form;
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
                miClient.getSession().showView(DModConsts.BU_BPR, DTrnUtils.getBizPartnerClassByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjCatBizPartnerConfig) {
                miClient.getSession().showView(DModConsts.BU_BPR_CFG, DTrnUtils.getBizPartnerClassByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjCatBranch) {
                miClient.getSession().showView(DModConsts.BU_BRA, DTrnUtils.getBizPartnerClassByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjCatBranchAddress) {
                miClient.getSession().showView(DModConsts.BU_ADD, DTrnUtils.getBizPartnerClassByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjCatBranchBankAccount) {
                miClient.getSession().showView(DModConsts.BU_BNK, DTrnUtils.getBizPartnerClassByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjCatBizPartnerType) {
                miClient.getSession().showView(DModConsts.BU_BPR_TP, DTrnUtils.getBizPartnerClassByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjCatItem) {
                miClient.getSession().showView(DModConsts.IU_ITM, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatItemLine) {
                miClient.getSession().showView(DModConsts.IU_LIN, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatItemGenus) {
                miClient.getSession().showView(DModConsts.IU_GEN, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatItemFamily) {
                miClient.getSession().showView(DModConsts.IU_FAM, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatItemBrand) {
                miClient.getSession().showView(DModConsts.IU_BRD, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatItemManufacturer) {
                miClient.getSession().showView(DModConsts.IU_MFR, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatItemComponent) {
                miClient.getSession().showView(DModConsts.IU_CMP, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatItemDepartment) {
                miClient.getSession().showView(DModConsts.IU_DEP, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatItemUnit) {
                miClient.getSession().showView(DModConsts.IU_UNT, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjCatItemBarcode) {
                miClient.getSession().showView(DModConsts.IU_ITM_BAR, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjOrdDoc) {
                showView(DModConsts.TX_DPS_ORD, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjDocDocInvoice) {
                showView(DModConsts.TX_DPS_DOC_INV, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjDocDocNote) {
                showView(DModConsts.TX_DPS_DOC_NOT, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjDocDocTicket) {
                showView(DModConsts.TX_DPS_DOC_TIC, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjDocAdjustmentInc) {
                showView(DModConsts.TX_DPS_ADJ_INC, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjDocAdjustmentDec) {
                showView(DModConsts.TX_DPS_ADJ_DEC, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjDocDay) {
                showView(DModConsts.TX_DAY_DPS, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjDocTypeChange) {
                showView(DModConsts.T_DPS_CHG, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjDocPrintingPend) {
                showView(DModConsts.T_DPS_PRT, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), new DGuiParams(DUtilConsts.EMT_PEND));
            }
            else if (menuItem == mjDocPrinting) {
                showView(DModConsts.T_DPS_PRT, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), new DGuiParams(DUtilConsts.EMT));
            }
            else if (menuItem == mjDocSigningPend) {
                showView(DModConsts.T_DPS_SIG, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), new DGuiParams(DUtilConsts.EMT_PEND));
            }
            else if (menuItem == mjDocSigning) {
                showView(DModConsts.T_DPS_SIG, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), new DGuiParams(DUtilConsts.EMT));
            }
            else if (menuItem == mjDocSendingPend) {
                showView(DModConsts.T_DPS_SND, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), new DGuiParams(DUtilConsts.EMT_PEND));
            }
            else if (menuItem == mjDocSending) {
                showView(DModConsts.T_DPS_SND, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), new DGuiParams(DUtilConsts.EMT));
            }
            else if (menuItem == mjAccBizPartner) {
                miClient.getSession().showView(DModConsts.FX_BAL_BPR, DTrnUtils.getBizPartnerClassByModuleSubtype(mnModuleSubtype), new DGuiParams(DUtilConsts.PER_BPR, DUtilConsts.PER_BPR));
            }
            else if (menuItem == mjAccBizPartnerCur) {
                miClient.getSession().showView(DModConsts.FX_BAL_BPR, DTrnUtils.getBizPartnerClassByModuleSubtype(mnModuleSubtype), new DGuiParams(DUtilConsts.PER_BPR, DUtilConsts.PER_CUR));
            }
            else if (menuItem == mjAccBizPartnerRef) {
                miClient.getSession().showView(DModConsts.FX_BAL_BPR, DTrnUtils.getBizPartnerClassByModuleSubtype(mnModuleSubtype), new DGuiParams(DUtilConsts.PER_BPR, DUtilConsts.PER_REF));
            }
            else if (menuItem == mjAccBranch) {
                miClient.getSession().showView(DModConsts.FX_BAL_BPR, DTrnUtils.getBizPartnerClassByModuleSubtype(mnModuleSubtype), new DGuiParams(DUtilConsts.PER_BPR_BRA, DUtilConsts.PER_BPR_BRA));
            }
            else if (menuItem == mjAccBranchCur) {
                miClient.getSession().showView(DModConsts.FX_BAL_BPR, DTrnUtils.getBizPartnerClassByModuleSubtype(mnModuleSubtype), new DGuiParams(DUtilConsts.PER_BPR_BRA, DUtilConsts.PER_CUR));
            }
            else if (menuItem == mjAccBranchRef) {
                miClient.getSession().showView(DModConsts.FX_BAL_BPR, DTrnUtils.getBizPartnerClassByModuleSubtype(mnModuleSubtype), new DGuiParams(DUtilConsts.PER_BPR_BRA, DUtilConsts.PER_REF));
            }
            else if (menuItem == mjAccDocPayable) {
                showView(DModConsts.TX_ACC_PAY, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjAccDocPayableCollected) {
                showView(DModConsts.TX_ACC_PAY_COL, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjStkStock) {
                showView(DModConsts.TX_STK, DUtilConsts.PER_ITM, null);
            }
            else if (menuItem == mjStkStockLot) {
                showView(DModConsts.TX_STK, DUtilConsts.PER_LOT, null);
            }
            else if (menuItem == mjStkStockSnr) {
                showView(DModConsts.TX_STK, DUtilConsts.PER_SNR, null);
            }
            else if (menuItem == mjStkStockImpDec) {
                showView(DModConsts.TX_STK, DUtilConsts.PER_IMP_DEC, null);
            }
            else if (menuItem == mjStkMoves) {
                showView(DModConsts.TX_STK_MOV, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjStkStockInventory) {
                showView(DModConsts.TX_STK_INV, DUtilConsts.PER_ITM, null);
            }
            else if (menuItem == mjStkStockInventoryLot) {
                showView(DModConsts.TX_STK_INV, DUtilConsts.PER_LOT, null);
            }
            else if (menuItem == mjStkStockInventorySnr) {
                showView(DModConsts.TX_STK_INV, DUtilConsts.PER_SNR, null);
            }
            else if (menuItem == mjStkStockInventoryImpDec) {
                showView(DModConsts.TX_STK_INV, DUtilConsts.PER_IMP_DEC, null);
            }
            else if (menuItem == mjStkSnrFix) {
                showView(DModConsts.T_SNR_FIX, DLibConsts.UNDEFINED, null);
            }
            else if (menuItem == mjStkSnrTrace) {
                DDialogTraceSerialNumber dlg = new DDialogTraceSerialNumber(miClient);
                dlg.resetForm();
                dlg.setVisible(true);
            }
            else if (menuItem == mjRepTransacCompany) {
                showView(DModConsts.TX_TRN_CO, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepTransacCompanyBranch) {
                showView(DModConsts.TX_TRN_CO_BRA, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepTransacItemGenus) {
                showView(DModConsts.TX_TRN_ITM_GEN, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepTransacItemLine) {
                showView(DModConsts.TX_TRN_ITM_LIN, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepTransacItem) {
                showView(DModConsts.TX_TRN_ITM, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepTransacItemBizPartner) {
                showView(DModConsts.TX_TRN_ITM_BPR, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepTransacBizPartnerType) {
                showView(DModConsts.TX_TRN_BPR_TP, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepTransacBizPartner) {
                showView(DModConsts.TX_TRN_BPR, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepTransacBizPartnerItem) {
                showView(DModConsts.TX_TRN_BPR_ITM, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepTransacAgentType) {
                showView(DModConsts.TX_TRN_AGT_TP, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepTransacAgent) {
                showView(DModConsts.TX_TRN_AGT, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepTransacAgentItem) {
                showView(DModConsts.TX_TRN_AGT_ITM, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepTransacUserType) {
                showView(DModConsts.TX_TRN_USR_TP, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepTransacUser) {
                showView(DModConsts.TX_TRN_USR, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepTransacUserItem) {
                showView(DModConsts.TX_TRN_USR_ITM, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepPerMonthTransacCompany) {
                showView(DModConsts.TX_PMO_CO, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepPerMonthTransacCompanyBranch) {
                showView(DModConsts.TX_PMO_CO_BRA, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepPerMonthTransacItemGenus) {
                showView(DModConsts.TX_PMO_ITM_GEN, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepPerMonthTransacItemLine) {
                showView(DModConsts.TX_PMO_ITM_LIN, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepPerMonthTransacItem) {
                showView(DModConsts.TX_PMO_ITM, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepPerMonthTransacItemBizPartner) {
                showView(DModConsts.TX_PMO_ITM_BPR, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepPerMonthTransacBizPartnerType) {
                showView(DModConsts.TX_PMO_BPR_TP, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepPerMonthTransacBizPartner) {
                showView(DModConsts.TX_PMO_BPR, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepPerMonthTransacBizPartnerItem) {
                showView(DModConsts.TX_PMO_BPR_ITM, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepPerMonthTransacAgentType) {
                showView(DModConsts.TX_PMO_AGT_TP, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepPerMonthTransacAgent) {
                showView(DModConsts.TX_PMO_AGT, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepPerMonthTransacAgentItem) {
                showView(DModConsts.TX_PMO_AGT_ITM, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepPerMonthTransacUserType) {
                showView(DModConsts.TX_PMO_USR_TP, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepPerMonthTransacUser) {
                showView(DModConsts.TX_PMO_USR, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepPerMonthTransacUserItem) {
                showView(DModConsts.TX_PMO_USR_ITM, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepDetailedItem) {
                showView(DModConsts.TX_DET_ITM, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
            else if (menuItem == mjRepDetailedItemSnr) {
                showView(DModConsts.TX_DET_ITM_SNR, DTrnUtils.getDpsCategoryByModuleSubtype(mnModuleSubtype), null);
            }
        }
    }
}
