/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod;

import java.util.HashMap;

/**
 *
 * @author Sergio Flores
 */
public abstract class DModConsts {

    public static final int MOD_SYS = 1;
    public static final int MOD_CFG = 2;
    public static final int MOD_BPR = 3;
    public static final int MOD_ITM = 4;
    public static final int MOD_FIN = 5;
    public static final int MOD_TRN = 6;
    public static final int MOD_MKT = 7;
    public static final int MOD_POS = 11;
    public static final int MOD_SRV = 12;
    public static final int MOD_LAD = 13;

    public static final int SU_SYS = 110001;
    public static final int SU_CO = 110002;

    public static final int CS_CSH_TP = 210001;
    public static final int CS_WAH_TP = 210002;
    public static final int CS_POS_ASA_TP = 210003;
    public static final int CS_POS_ASP_TP = 210004;
    public static final int CS_USR_TP = 210005;
    public static final int CS_ACS_TP = 210006;
    public static final int CS_MOD = 210007;
    public static final int CS_PRV = 210008;
    public static final int CS_LEV = 210009;
    public static final int CS_CTY = 210010;
    public static final int CS_CUR = 210011;
    public static final int CS_CUR_DEN = 210012;
    public static final int CS_EMS_TP = 210013;
    public static final int CS_XSP = 210101;
    public static final int CS_TAX_REG = 210201;

    public static final int CU_CFG_CO = 220001;
    public static final int CU_CFG_BRA = 220002;
    public static final int CU_CSH = 220003;
    public static final int CU_WAH = 220004;
    public static final int CU_USR_CTM_TP = 220005;
    public static final int CU_USR = 220006;
    public static final int CU_USR_PRV = 220007;
    public static final int CU_USR_CO = 220008;
    public static final int CU_USR_BRA = 220009;
    public static final int CU_USR_CSH = 220010;
    public static final int CU_USR_WAH = 220011;
    public static final int CU_USR_SER_BRA = 220012;
    public static final int CU_CER = 220013;

    public static final int C_USR_GUI = 230001;
    public static final int C_LOCK = 230101;

    public static final int BS_BPR_CL = 310001;
    public static final int BS_IDY_TP = 310002;
    public static final int BS_BAF_TP = 310003;
    public static final int BS_CDT_TP = 310004;
    public static final int BS_TCD_TP = 310005;
    public static final int BS_TCE_TP = 310006;
    public static final int BS_BNK_TP = 310007;
    public static final int BS_LNK_BPR_TP = 310008;

    public static final int BU_BPR_TP = 320001;
    public static final int BU_BPR = 320002;
    public static final int BU_BPR_CFG = 320003;
    public static final int BU_BRA = 320004;
    public static final int BU_ADD = 320005;
    public static final int BU_BNK = 320006;

    public static final int BX_BPR = 340001;
    public static final int BX_BPR_BNK = 340002;
    public static final int BX_BPR_CAR = 340003;
    public static final int BX_BPR_AGT = 340004;

    public static final int IS_ITM_CT = 410001;
    public static final int IS_ITM_CL = 410002;
    public static final int IS_ITM_TP = 410003;
    public static final int IS_SNR_TP = 410004;
    public static final int IS_LNK_ITM_TP = 410005;

    public static final int IU_UNT = 420001;
    public static final int IU_BRD = 420002;
    public static final int IU_MFR = 420003;
    public static final int IU_CMP = 420004;
    public static final int IU_DEP = 420005;
    public static final int IU_FAM = 420006;
    public static final int IU_GEN = 420007;
    public static final int IU_LIN = 420008;
    public static final int IU_ITM = 420009;
    public static final int IU_ITM_BAR = 420010;
    public static final int IU_GEN_DEP = 420011;
    public static final int IU_LIN_DEP = 420012;
    public static final int IU_ITM_DEP = 420013;

    public static final int I_VEN_PRY = 430001;
    public static final int I_VEN_ITM = 430002;

    public static final int IX_ITM = 440001;
    public static final int IX_ITM_CL_BY_IDX = 440101;
    public static final int IX_ITM_TP_BY_IDX = 440102;

    public static final int FS_ACC_CT = 510001;
    public static final int FS_ACC_CL = 510002;
    public static final int FS_ACC_TP = 510003;
    public static final int FS_ACC_SPE_TP = 510004;
    public static final int FS_SYS_ACC_TP = 510005;
    public static final int FS_SYS_MOV_CL = 510006;
    public static final int FS_SYS_MOV_TP = 510007;
    public static final int FS_DIV_MOV_TP = 510008;
    public static final int FS_PAY_TP = 510009;
    public static final int FS_MOP_TP = 510010;
    public static final int FS_VAL_TP = 510011;
    public static final int FS_TAX_TP = 510012;
    public static final int FS_EXR_TP = 510013;
    public static final int FS_EXR_APP_TP = 510014;

    public static final int FU_REC_TP = 520001;
    public static final int FU_ACC = 520002;
    public static final int FU_TAX = 520003;
    public static final int FU_TAX_REG = 520004;
    public static final int FU_TAX_GRP = 520005;
    public static final int FU_TAX_GRP_CFG = 520006;
    public static final int FU_TAX_GRP_CFG_ROW = 520007;

    public static final int F_YER = 530001;
    public static final int F_YER_PER = 530002;
    public static final int F_ABP_CSH = 530003;
    public static final int F_ABP_WAH = 530004;
    public static final int F_ABP_BPR = 530005;
    public static final int F_ABP_ITM = 530006;
    public static final int F_EXR = 530007;
    public static final int F_BKK_REC = 530008;
    public static final int F_BKK_NUM = 530009;
    public static final int F_BKK = 530010;

    public static final int FX_BAL_BPR = 540001;
    public static final int FX_BAL_CSH = 540002;
    public static final int FX_BAL_WAH = 540003;
    public static final int FX_CSH_BPR = 540011;
    public static final int FX_CSH_BPR_MON_ADV = 540012;
    public static final int FX_CSH_BPR_MON = 540013;
    public static final int FX_CSH_ACC = 540021;
    public static final int FX_CSH_TRA = 540022;
    public static final int FX_BKK_CTM = 540031;
    public static final int FX_BKK_CTM_STC = 540032;
    public static final int FX_BKK_MOP = 540033;        // modes of payment
    public static final int FX_BKK_MOP_SMT = 540034;    // modes of payment and system move types
    public static final int FX_MOP_TP_ALL = 540041;     // all modes of payment
    public static final int FX_MOP_TP_COL = 540042;     // collectable modes of payment
    public static final int FX_CTM_DPS_CLR = 540051;
    public static final int FX_PRC_BKK_OPE = 540091;    // process bookkeeping opening

    public static final int TS_FIS_TP = 610001;
    public static final int TS_DPS_CT = 610002;
    public static final int TS_DPS_CL = 610003;
    public static final int TS_DPS_TP = 610004;
    public static final int TS_DPS_ST = 610005;
    public static final int TS_ADJ_CT = 610006;
    public static final int TS_ADJ_CL = 610007;
    public static final int TS_ADJ_TP = 610008;
    public static final int TS_EMI_TP = 610009;
    public static final int TS_BLK_TP = 610010;
    public static final int TS_XML_TP = 610011;
    public static final int TS_XML_STP = 610022;
    public static final int TS_XML_STP_VER = 610023;
    public static final int TS_XML_ST = 610012;
    public static final int TS_XML_ADD_TP = 610021;
    public static final int TS_XSM_CL = 610013;
    public static final int TS_XSM_TP = 610014;
    public static final int TS_DNP_TP = 610015;
    public static final int TS_IOG_CT = 610016;
    public static final int TS_IOG_CL = 610017;
    public static final int TS_IOG_TP = 610018;
    public static final int TS_IOM_TP = 610019;
    public static final int TS_CUT_TP = 610020;

    public static final int TU_SER = 620001;
    public static final int TU_SER_BRA = 620002;
    public static final int TU_SER_NUM = 620003;
    public static final int TU_SCH = 620004;

    public static final int T_DPS = 630001;
    public static final int T_DPS_PRT = 630002;
    public static final int T_DPS_SIG = 630003;
    public static final int T_DPS_SND = 630004;
    public static final int T_DPS_NOT = 630006;
    public static final int T_DPS_ROW = 630007;
    public static final int T_DPS_ROW_NOT = 630008;
    public static final int T_DPS_CHG = 630009;
    public static final int T_DFR = 630041;
    public static final int T_IOG = 630010;
    public static final int T_IOG_NOT = 630011;
    public static final int T_IOG_ROW = 630012;
    public static final int T_IOG_ROW_NOT = 630013;
    public static final int T_LOT = 630014;
    public static final int T_STK_CFG = 630015;
    public static final int T_STK = 630016;
    public static final int T_SNR_FIX = 630017;
    public static final int T_XSR = 630031;
    public static final int T_XSM = 630032;
    public static final int T_IOM = 630019;
    public static final int T_IOM_NOT = 630020;
    public static final int T_PUS = 630021;
    public static final int T_PUS_NOT = 630022;
    public static final int T_PUS_CUT = 630023;
    public static final int T_PUS_CUT_ROW = 630024;

    public static final int TX_DPS_ORD = 640101;
    public static final int TX_DPS_DOC_INV = 640111;
    public static final int TX_DPS_DOC_NOT = 640112;
    public static final int TX_DPS_DOC_TIC = 640113;
    public static final int TX_DPS_ADJ_INC = 640114;
    public static final int TX_DPS_ADJ_DEC = 640115;
    public static final int TX_ADJ_INC_INC = 640116;
    public static final int TX_ADJ_INC_ADD = 640117;
    public static final int TX_ADJ_DEC_DIS = 640118;
    public static final int TX_ADJ_DEC_RET = 640119;
    public static final int TX_IOG_PUR = 640121;
    public static final int TX_IOG_SAL = 640122;
    public static final int TX_IOG_EXT = 640123;
    public static final int TX_IOG_INT = 640124;
    public static final int TX_IOG_MFG = 640125;
    public static final int TX_STK = 640131;
    public static final int TX_STK_WAH = 640139;
    public static final int TX_STK_MOV = 640132;
    public static final int TX_STK_INV = 640133;
    public static final int TX_STK_BAL = 640134;
    public static final int TX_STK_LOT = 640135;
    public static final int TX_LOT_SHW = 640136;
    public static final int TX_XSM_AVA = 640137;

    public static final int TX_TRN_CO = 640141;
    public static final int TX_TRN_CO_BRA = 640142;
    public static final int TX_TRN_ITM = 640143;
    public static final int TX_TRN_ITM_LIN = 640144;
    public static final int TX_TRN_ITM_GEN = 640145;
    public static final int TX_TRN_ITM_BPR = 640146;
    public static final int TX_TRN_BPR = 640147;
    public static final int TX_TRN_BPR_TP = 640148;
    public static final int TX_TRN_BPR_ITM = 640149;
    public static final int TX_TRN_AGT = 640150;
    public static final int TX_TRN_AGT_TP = 640151;
    public static final int TX_TRN_AGT_ITM = 640152;
    public static final int TX_TRN_USR = 640153;
    public static final int TX_TRN_USR_TP = 640154;
    public static final int TX_TRN_USR_ITM = 640155;
    public static final int TX_TRN_SNR = 640156;
    public static final int TX_PMO_CO = 640161;
    public static final int TX_PMO_CO_BRA = 640162;
    public static final int TX_PMO_ITM = 640163;
    public static final int TX_PMO_ITM_LIN = 640164;
    public static final int TX_PMO_ITM_GEN = 640165;
    public static final int TX_PMO_ITM_BPR = 640166;
    public static final int TX_PMO_BPR = 640167;
    public static final int TX_PMO_BPR_TP = 640168;
    public static final int TX_PMO_BPR_ITM = 640169;
    public static final int TX_PMO_AGT = 640170;
    public static final int TX_PMO_AGT_TP = 640171;
    public static final int TX_PMO_AGT_ITM = 640172;
    public static final int TX_PMO_USR = 640173;
    public static final int TX_PMO_USR_TP = 640174;
    public static final int TX_PMO_USR_ITM = 640175;
    public static final int TX_ACC_PAY = 640181;
    public static final int TX_ACC_PAY_COL = 640182;
    public static final int TX_ACC_PAY_ALL = 640183;
    public static final int TX_DET_ITM = 640191;
    public static final int TX_DET_ITM_SNR = 640192;
    public static final int TX_MY_DPS_ORD = 640201;
    public static final int TX_MY_DPS_DOC = 640202;
    public static final int TX_MY_DPS_ADJ_INC = 640203;
    public static final int TX_MY_DPS_ADJ_DEC = 640204;
    public static final int TX_DAY_DPS = 640211;
    public static final int TX_DAY_MY_DPS = 640212;
    public static final int TX_TRC_SNR = 640221;
    public static final int TX_PRC_STK_OPE = 640301;
    public static final int TX_PRC_HST = 640302;        // prices history
    public static final int TX_DFR_PAY = 640310;
    public static final int TX_DFR_ADD = 640311;
    public static final int TX_DFR_INV = 640316;
    public static final int TX_DFR_ANNUL_PARAMS = 640321;
    public static final int TX_DFR_RELATIONS = 640326;

    public static final int TR_DPS = 650001;
    public static final int TR_DPS_CFD = 650002;
    public static final int TR_DPS_CFDI_32 = 650032;
    public static final int TR_DPS_CFDI_33 = 650033;
    public static final int TR_DPS_CFDI_33_CRP_10 = 650034;
    public static final int TR_DPS_CFDI_40 = 650040;
    public static final int TR_DPS_CFDI_40_CRP_20 = 650041;
    public static final int TR_DPS_CFDI_40_CCP_20 = 650042;

    public static final int MS_PRC_TP = 710001;
    public static final int MS_ITM_PRC_TP = 710002;
    public static final int MS_LST_GRP_TP = 710003;
    public static final int MS_LST_TP = 710004;
    public static final int MS_DIS_TP = 710005;
    public static final int MS_CMM_TP = 710006;
    public static final int MS_CMM_CAL_TP = 710007;
    public static final int MS_CMM_VAL_TP = 710008;
    public static final int MS_LNK_AGT_TP = 710009;
    public static final int MS_LNK_USR_TP = 710010;
    public static final int MS_LNK_CUS_TP = 710011;

    public static final int MU_AGT_TP = 720001;
    public static final int MU_CUS_TP = 720002;
    public static final int MU_SEG = 720003;
    public static final int MU_CHA = 720004;
    public static final int MU_ROU = 720005;

    public static final int M_AGT_CFG = 730001;
    public static final int M_CUS_CFG = 730002;
    public static final int M_CUS_BRA_CFG = 730003;
    public static final int M_LST_GRP = 730004;
    public static final int M_LST_GRP_ITM = 730005;
    public static final int M_LST = 730006;
    public static final int M_LST_PRC = 730007;
    public static final int M_CUS_FIX = 730008;
    public static final int M_CUS_ITM_PRC_TP = 730009;
    public static final int M_CUS_LST = 730010;
    public static final int M_SPE = 730011;
    public static final int M_SPE_PRC = 730012;
    public static final int M_SPE_CUS = 730013;
    public static final int M_PRM = 730014;
    public static final int M_PRM_PRM = 730015;
    public static final int M_PRM_CUS = 730016;
    public static final int M_CMM_CFG_AGT = 730021;
    public static final int M_CMM_CFG_USR = 730022;
    public static final int M_CMM_CAL = 730023;
    public static final int M_CMM_CAL_NOT = 730024;
    public static final int M_CMM_CAL_ROW = 730025;

    public static final int MX_ITM_PRC = 740001;
    public static final int MX_ITM_NAM_PRC = 740002;
    public static final int MX_CMM_CAL_ROW = 740011;
    public static final int MX_CMM_CAL_ROW_DET = 740012;

    public static final int SS_SRV_TP = 810001;
    public static final int SS_SRV_ST = 810002;

    public static final int SU_EQU = 820001;

    public static final int S_REP = 830001;
    public static final int S_REP_LOG = 830002;

    public static final int SR_REP_NEW = 850001;
    public static final int SR_REP_DVY = 850002;

    public static final int LS_TPT_TP = 910001;
    public static final int LS_LOC_TP = 910002;
    public static final int LS_TPT_FIGURE_TP = 910011;
    public static final int LS_TPT_PART_TP = 910012;

    public static final int LU_LOC = 920002;
    public static final int LU_LOC_DIST = 920003;
    public static final int LU_TPT_FIGURE = 920011;
    public static final int LU_TRAIL = 920021;
    public static final int LU_TRUCK = 920022;
    public static final int LU_TRUCK_TRAIL = 920026;
    public static final int LU_TRUCK_TPT_FIGURE = 920027;
    public static final int LU_TRUCK_TPT_FIGURE_TPT_PART = 920028;

    public static final int L_BOL = 930001;
    public static final int L_BOL_LOC = 930002;
    public static final int L_BOL_MERCH = 930006;
    public static final int L_BOL_MERCH_MOVE = 930007;
    public static final int L_BOL_TRUCK = 930022;
    public static final int L_BOL_TRUCK_TRAIL = 930026;
    public static final int L_BOL_TPT_FIGURE = 930027;
    public static final int L_BOL_TPT_FIGURE_TPT_PART = 930028;
    
    public static final int LR_BOL = 950001;

    public static final HashMap<Integer, String> TablesMap = new HashMap<>();

    static {
        TablesMap.put(SU_SYS, "su_sys");
        TablesMap.put(SU_CO, "su_co");

        TablesMap.put(CS_CSH_TP, "cs_csh_tp");
        TablesMap.put(CS_WAH_TP, "cs_wah_tp");
        TablesMap.put(CS_POS_ASA_TP, "cs_pos_asa_tp");
        TablesMap.put(CS_POS_ASP_TP, "cs_pos_asp_tp");
        TablesMap.put(CS_USR_TP, "cs_usr_tp");
        TablesMap.put(CS_ACS_TP, "cs_acs_tp");
        TablesMap.put(CS_MOD, "cs_mod");
        TablesMap.put(CS_PRV, "cs_prv");
        TablesMap.put(CS_LEV, "cs_lev");
        TablesMap.put(CS_CTY, "cs_cty");
        TablesMap.put(CS_CUR, "cs_cur");
        TablesMap.put(CS_CUR_DEN, "cs_cur_den");
        TablesMap.put(CS_EMS_TP, "cs_ems_tp");
        TablesMap.put(CS_XSP, "cs_xsp");
        TablesMap.put(CS_TAX_REG, "cs_tax_reg");

        TablesMap.put(CU_CFG_CO, "cu_cfg_co");
        TablesMap.put(CU_CFG_BRA, "cu_cfg_bra");
        TablesMap.put(CU_CSH, "cu_csh");
        TablesMap.put(CU_WAH, "cu_wah");
        TablesMap.put(CU_USR_CTM_TP, "cu_usr_ctm_tp");
        TablesMap.put(CU_USR, "cu_usr");
        TablesMap.put(CU_USR_PRV, "cu_usr_prv");
        TablesMap.put(CU_USR_CO, "cu_usr_co");
        TablesMap.put(CU_USR_BRA, "cu_usr_bra");
        TablesMap.put(CU_USR_CSH, "cu_usr_csh");
        TablesMap.put(CU_USR_WAH, "cu_usr_wah");
        TablesMap.put(CU_USR_SER_BRA, "cu_usr_ser_bra");
        TablesMap.put(CU_CER, "cu_cer");

        TablesMap.put(C_USR_GUI, "c_usr_gui");
        TablesMap.put(C_LOCK, "c_lock");

        TablesMap.put(BS_BPR_CL, "bs_bpr_cl");
        TablesMap.put(BS_IDY_TP, "bs_idy_tp");
        TablesMap.put(BS_BAF_TP, "bs_baf_tp");
        TablesMap.put(BS_CDT_TP, "bs_cdt_tp");
        TablesMap.put(BS_TCD_TP, "bs_tcd_tp");
        TablesMap.put(BS_TCE_TP, "bs_tce_tp");
        TablesMap.put(BS_BNK_TP, "bs_bnk_tp");
        TablesMap.put(BS_LNK_BPR_TP, "bs_lnk_bpr_tp");

        TablesMap.put(BU_BPR_TP, "bu_bpr_tp");
        TablesMap.put(BU_BPR, "bu_bpr");
        TablesMap.put(BU_BPR_CFG, "bu_bpr_cfg");
        TablesMap.put(BU_BRA, "bu_bra");
        TablesMap.put(BU_ADD, "bu_add");
        TablesMap.put(BU_BNK, "bu_bnk");

        TablesMap.put(IS_ITM_CT, "is_itm_ct");
        TablesMap.put(IS_ITM_CL, "is_itm_cl");
        TablesMap.put(IS_ITM_TP, "is_itm_tp");
        TablesMap.put(IS_SNR_TP, "is_snr_tp");
        TablesMap.put(IS_LNK_ITM_TP, "is_lnk_itm_tp");

        TablesMap.put(IU_UNT, "iu_unt");
        TablesMap.put(IU_BRD, "iu_brd");
        TablesMap.put(IU_MFR, "iu_mfr");
        TablesMap.put(IU_CMP, "iu_cmp");
        TablesMap.put(IU_DEP, "iu_dep");
        TablesMap.put(IU_FAM, "iu_fam");
        TablesMap.put(IU_GEN, "iu_gen");
        TablesMap.put(IU_LIN, "iu_lin");
        TablesMap.put(IU_ITM, "iu_itm");
        TablesMap.put(IU_ITM_BAR, "iu_itm_bar");
        TablesMap.put(IU_GEN_DEP, "iu_gen_dep");
        TablesMap.put(IU_LIN_DEP, "iu_lin_dep");
        TablesMap.put(IU_ITM_DEP, "iu_itm_dep");

        TablesMap.put(I_VEN_PRY, "i_ven_pry");
        TablesMap.put(I_VEN_ITM, "i_ven_itm");

        TablesMap.put(FS_ACC_CT, "fs_acc_ct");
        TablesMap.put(FS_ACC_CL, "fs_acc_cl");
        TablesMap.put(FS_ACC_TP, "fs_acc_tp");
        TablesMap.put(FS_ACC_SPE_TP, "fs_acc_spe_tp");
        TablesMap.put(FS_SYS_ACC_TP, "fs_sys_acc_tp");
        TablesMap.put(FS_SYS_MOV_CL, "fs_sys_mov_cl");
        TablesMap.put(FS_SYS_MOV_TP, "fs_sys_mov_tp");
        TablesMap.put(FS_DIV_MOV_TP, "fs_div_mov_tp");
        TablesMap.put(FS_PAY_TP, "fs_pay_tp");
        TablesMap.put(FS_MOP_TP, "fs_mop_tp");
        TablesMap.put(FS_VAL_TP, "fs_val_tp");
        TablesMap.put(FS_TAX_TP, "fs_tax_tp");
        TablesMap.put(FS_EXR_TP, "fs_exr_tp");
        TablesMap.put(FS_EXR_APP_TP, "fs_exr_app_tp");

        TablesMap.put(FU_REC_TP, "fu_rec_tp");
        TablesMap.put(FU_ACC, "fu_acc");
        TablesMap.put(FU_TAX, "fu_tax");
        TablesMap.put(FU_TAX_REG, "fu_tax_reg");
        TablesMap.put(FU_TAX_GRP, "fu_tax_grp");
        TablesMap.put(FU_TAX_GRP_CFG, "fu_tax_grp_cfg");
        TablesMap.put(FU_TAX_GRP_CFG_ROW, "fu_tax_grp_cfg_row");

        TablesMap.put(F_YER, "f_yer");
        TablesMap.put(F_YER_PER, "f_yer_per");
        TablesMap.put(F_ABP_CSH, "f_abp_csh");
        TablesMap.put(F_ABP_WAH, "f_abp_wah");
        TablesMap.put(F_ABP_BPR, "f_abp_bpr");
        TablesMap.put(F_ABP_ITM, "f_abp_itm");
        TablesMap.put(F_EXR, "f_exr");
        TablesMap.put(F_BKK_REC, "f_bkk_rec");
        TablesMap.put(F_BKK_NUM, "f_bkk_num");
        TablesMap.put(F_BKK, "f_bkk");

        TablesMap.put(TS_FIS_TP, "ts_fis_tp");
        TablesMap.put(TS_DPS_CT, "ts_dps_ct");
        TablesMap.put(TS_DPS_CL, "ts_dps_cl");
        TablesMap.put(TS_DPS_TP, "ts_dps_tp");
        TablesMap.put(TS_DPS_ST, "ts_dps_st");
        TablesMap.put(TS_ADJ_CT, "ts_adj_ct");
        TablesMap.put(TS_ADJ_CL, "ts_adj_cl");
        TablesMap.put(TS_ADJ_TP, "ts_adj_tp");
        TablesMap.put(TS_EMI_TP, "ts_emi_tp");
        TablesMap.put(TS_BLK_TP, "ts_blk_tp");
        TablesMap.put(TS_XML_TP, "ts_xml_tp");
        TablesMap.put(TS_XML_STP, "ts_xml_stp");
        TablesMap.put(TS_XML_STP_VER, "ts_xml_stp_ver");
        TablesMap.put(TS_XML_ST, "ts_xml_st");
        TablesMap.put(TS_XML_ADD_TP, "ts_xml_add_tp");
        TablesMap.put(TS_XSM_CL, "ts_xsm_cl");
        TablesMap.put(TS_XSM_TP, "ts_xsm_tp");
        TablesMap.put(TS_DNP_TP, "ts_dnp_tp");
        TablesMap.put(TS_IOG_CT, "ts_iog_ct");
        TablesMap.put(TS_IOG_CL, "ts_iog_cl");
        TablesMap.put(TS_IOG_TP, "ts_iog_tp");
        TablesMap.put(TS_IOM_TP, "ts_iom_tp");
        TablesMap.put(TS_CUT_TP, "ts_cut_tp");

        TablesMap.put(TU_SER, "tu_ser");
        TablesMap.put(TU_SER_BRA, "tu_ser_bra");
        TablesMap.put(TU_SER_NUM, "tu_ser_num");
        TablesMap.put(TU_SCH, "tu_sch");

        TablesMap.put(T_DPS, "t_dps");
        TablesMap.put(T_DPS_PRT, "t_dps_prt");
        TablesMap.put(T_DPS_SIG, "t_dps_sig");
        TablesMap.put(T_DPS_SND, "t_dps_snd");
        TablesMap.put(T_DPS_NOT, "t_dps_not");
        TablesMap.put(T_DPS_ROW, "t_dps_row");
        TablesMap.put(T_DPS_ROW_NOT, "t_dps_row_not");
        TablesMap.put(T_DPS_CHG, "t_dps_chg");
        TablesMap.put(T_DFR, "t_dfr");
        TablesMap.put(T_IOG, "t_iog");
        TablesMap.put(T_IOG_NOT, "t_iog_not");
        TablesMap.put(T_IOG_ROW, "t_iog_row");
        TablesMap.put(T_IOG_ROW_NOT, "t_iog_row_not");
        TablesMap.put(T_LOT, "t_lot");
        TablesMap.put(T_STK_CFG, "t_stk_cfg");
        TablesMap.put(T_STK, "t_stk");
        TablesMap.put(T_SNR_FIX, "t_snr_fix");
        TablesMap.put(T_XSR, "t_xsr");
        TablesMap.put(T_XSM, "t_xsm");
        TablesMap.put(T_IOM, "t_iom");
        TablesMap.put(T_IOM_NOT, "t_iom_not");
        TablesMap.put(T_PUS, "t_pus");
        TablesMap.put(T_PUS_NOT, "t_pus_not");
        TablesMap.put(T_PUS_CUT, "t_pus_cut");
        TablesMap.put(T_PUS_CUT_ROW, "t_pus_cut_row");

        TablesMap.put(MS_PRC_TP, "ms_prc_tp");
        TablesMap.put(MS_ITM_PRC_TP, "ms_itm_prc_tp");
        TablesMap.put(MS_LST_GRP_TP, "ms_lst_grp_tp");
        TablesMap.put(MS_LST_TP, "ms_lst_tp");
        TablesMap.put(MS_DIS_TP, "ms_dis_tp");
        TablesMap.put(MS_CMM_TP, "ms_cmm_tp");
        TablesMap.put(MS_CMM_CAL_TP, "ms_cmm_cal_tp");
        TablesMap.put(MS_CMM_VAL_TP, "ms_cmm_val_tp");
        TablesMap.put(MS_LNK_AGT_TP, "ms_lnk_agt_tp");
        TablesMap.put(MS_LNK_USR_TP, "ms_lnk_usr_tp");
        TablesMap.put(MS_LNK_CUS_TP, "ms_lnk_cus_tp");

        TablesMap.put(MU_AGT_TP, "mu_agt_tp");
        TablesMap.put(MU_CUS_TP, "mu_cus_tp");
        TablesMap.put(MU_SEG, "mu_seg");
        TablesMap.put(MU_CHA, "mu_cha");
        TablesMap.put(MU_ROU, "mu_rou");

        TablesMap.put(M_AGT_CFG, "m_agt_cfg");
        TablesMap.put(M_CUS_CFG, "m_cus_cfg");
        TablesMap.put(M_CUS_BRA_CFG, "m_cus_bra_cfg");
        TablesMap.put(M_LST_GRP, "m_lst_grp");
        TablesMap.put(M_LST_GRP_ITM, "m_lst_grp_itm");
        TablesMap.put(M_LST, "m_lst");
        TablesMap.put(M_LST_PRC, "m_lst_prc");
        TablesMap.put(M_CUS_FIX, "m_cus_fix");
        TablesMap.put(M_CUS_ITM_PRC_TP, "m_cus_itm_prc_tp");
        TablesMap.put(M_CUS_LST, "m_cus_lst");
        TablesMap.put(M_SPE, "m_spe");
        TablesMap.put(M_SPE_PRC, "m_spe_prc");
        TablesMap.put(M_SPE_CUS, "m_spe_cus");
        TablesMap.put(M_PRM, "m_prm");
        TablesMap.put(M_PRM_PRM, "m_prm_prm");
        TablesMap.put(M_PRM_CUS, "m_prm_cus");
        TablesMap.put(M_CMM_CFG_AGT, "m_cmm_cfg_agt");
        TablesMap.put(M_CMM_CFG_USR, "m_cmm_cfg_usr");
        TablesMap.put(M_CMM_CAL, "m_cmm_cal");
        TablesMap.put(M_CMM_CAL_NOT, "m_cmm_cal_not");
        TablesMap.put(M_CMM_CAL_ROW, "m_cmm_cal_row");

        TablesMap.put(SS_SRV_TP, "ss_srv_tp");
        TablesMap.put(SS_SRV_ST, "ss_srv_st");

        TablesMap.put(SU_EQU, "su_equ");

        TablesMap.put(S_REP, "s_rep");
        TablesMap.put(S_REP_LOG, "s_rep_log");
        
        TablesMap.put(LS_TPT_TP, "ls_tpt_tp");
        TablesMap.put(LS_LOC_TP, "ls_loc_tp");
        TablesMap.put(LS_TPT_FIGURE_TP, "ls_tpt_figure_tp");
        TablesMap.put(LS_TPT_PART_TP, "ls_tpt_part_tp");

        TablesMap.put(LU_LOC, "lu_loc");
        TablesMap.put(LU_LOC_DIST, "lu_loc_dist");
        TablesMap.put(LU_TPT_FIGURE, "lu_tpt_figure");
        TablesMap.put(LU_TRAIL, "lu_trail");
        TablesMap.put(LU_TRUCK, "lu_truck");
        TablesMap.put(LU_TRUCK_TRAIL, "lu_truck_trail");
        TablesMap.put(LU_TRUCK_TPT_FIGURE, "lu_truck_tpt_figure");
        TablesMap.put(LU_TRUCK_TPT_FIGURE_TPT_PART, "lu_truck_tpt_figure_tpt_part");

        TablesMap.put(L_BOL, "l_bol");
        TablesMap.put(L_BOL_LOC, "l_bol_loc");
        TablesMap.put(L_BOL_MERCH, "l_bol_merch");
        TablesMap.put(L_BOL_MERCH_MOVE, "l_bol_merch_move");
        TablesMap.put(L_BOL_TRUCK, "l_bol_truck");
        TablesMap.put(L_BOL_TRUCK_TRAIL, "l_bol_truck_trail");
        TablesMap.put(L_BOL_TPT_FIGURE, "l_bol_tpt_figure");
        TablesMap.put(L_BOL_TPT_FIGURE_TPT_PART, "l_bol_tpt_figure_tpt_part");
    }
}
