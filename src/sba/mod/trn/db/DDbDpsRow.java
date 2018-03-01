/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Vector;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.db.DDbRegistryUser;
import sba.lib.grid.DGridRow;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.itm.db.DDbItem;

/**
 *
 * @author Sergio Flores
 */
public class DDbDpsRow extends DDbRegistryUser implements DGridRow, DTrnDocRow {

    protected int mnPkDpsId;
    protected int mnPkRowId;
    protected String msCode;
    protected String msName;
    protected String msPredial;
    protected int mnSortingPos;
    protected boolean mbDiscountDocApplying;
    protected boolean mbDiscountUnitaryPercentageApplying;
    protected boolean mbDiscountRowPercentageApplying;
    protected double mdDiscountUnitaryPercentage;
    protected double mdDiscountRowPercentage;
    protected double mdQuantity;
    protected double mdPriceUnitary;
    protected double mdDiscountUnitary;
    protected double mdDiscountRow;
    protected double mdSubtotalProv_r;
    protected double mdDiscountDoc;
    protected double mdSubtotal_r;
    protected double mdTaxCharged_r;
    protected double mdTaxRetained_r;
    protected double mdTotal_r;
    protected double mdPriceUnitaryCalculated_r;
    protected double mdPriceUnitaryCy;
    protected double mdDiscountUnitaryCy;
    protected double mdDiscountRowCy;
    protected double mdSubtotalProvCy_r;
    protected double mdDiscountDocCy;
    protected double mdSubtotalCy_r;
    protected double mdTaxChargedCy_r;
    protected double mdTaxRetainedCy_r;
    protected double mdTotalCy_r;
    protected double mdPriceUnitaryCalculatedCy_r;
    protected double mdTaxChargedRate1;
    protected double mdTaxChargedRate2;
    protected double mdTaxChargedRate3;
    protected double mdTaxRetainedRate1;
    protected double mdTaxRetainedRate2;
    protected double mdTaxRetainedRate3;
    protected double mdTaxCharged1;
    protected double mdTaxCharged2;
    protected double mdTaxCharged3;
    protected double mdTaxRetained1;
    protected double mdTaxRetained2;
    protected double mdTaxRetained3;
    protected double mdTaxCharged1Cy;
    protected double mdTaxCharged2Cy;
    protected double mdTaxCharged3Cy;
    protected double mdTaxRetained1Cy;
    protected double mdTaxRetained2Cy;
    protected double mdTaxRetained3Cy;
    protected double mdMeasurementLength;
    protected double mdMeasurementSurface;
    protected double mdMeasurementVolume;
    protected double mdMeasurementMass;
    protected double mdMeasurementTime;
    protected double mdWeightGross;
    protected double mdWeightDelivery;
    protected double mdSurplusPercentage;
    protected boolean mbTaxManual;
    protected boolean mbInventoriable;
    /*
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkAdjustmentCategoryId;
    protected int mnFkAdjustmentClassId;
    protected int mnFkAdjustmentTypeId;
    protected int mnFkRowItemId;
    protected int mnFkRowUnitId;
    protected int mnFkReferenceItemId_n;
    protected int mnFkReferenceUnitId_n;
    protected int mnFkTaxRegionId;
    protected int mnFkTaxCharged1Id;
    protected int mnFkTaxCharged2Id;
    protected int mnFkTaxCharged3Id;
    protected int mnFkTaxRetained1Id;
    protected int mnFkTaxRetained2Id;
    protected int mnFkTaxRetained3Id;
    protected int mnFkSourceDpsId_n;
    protected int mnFkSourceRowId_n;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected String msEdsItemKey;
    protected String msEdsUnitKey;
    protected String msEdsSourceUuid;
    
    protected String msDbUnitCode;
    protected int mnDbTaxRegimeId;
    protected Vector<int[]> mvDbDependentDpsRowKeys;

    protected Vector<DDbDpsRowNote> mvChildRowNotes;

    protected int mnAuxPkAdjustedDpsId;
    protected double mdAuxTotalAdjustedCy;

    protected Vector<DTrnStockMove> mvAuxStockMoves;

    public DDbDpsRow() {
        super(DModConsts.T_DPS_ROW);
        mvDbDependentDpsRowKeys = new Vector<>();
        mvChildRowNotes = new Vector<>();
        mvAuxStockMoves = new Vector<>();
        initRegistry();
    }

    /*
     * Private methods:
     */

    private boolean canChangeStatus() {
        boolean canChange = true;

        initQueryMembers();

        if (mvDbDependentDpsRowKeys.size() > 0) {
            canChange = false;
            msQueryResult = "La partida del documento tiene registros dependientes.";
        }

        return canChange;
    }

    /*
     * Public methods:
     */

    public void setPkDpsId(int n) { mnPkDpsId = n; }
    public void setPkRowId(int n) { mnPkRowId = n; }
    public void setCode(String s) { msCode = s; }
    public void setName(String s) { msName = s; }
    public void setPredial(String s) { msPredial = s; }
    public void setSortingPos(int n) { mnSortingPos = n; }
    public void setDiscountDocApplying(boolean b) { mbDiscountDocApplying = b; }
    public void setDiscountUnitaryPercentageApplying(boolean b) { mbDiscountUnitaryPercentageApplying = b; }
    public void setDiscountRowPercentageApplying(boolean b) { mbDiscountRowPercentageApplying = b; }
    public void setDiscountUnitaryPercentage(double d) { mdDiscountUnitaryPercentage = d; }
    public void setDiscountRowPercentage(double d) { mdDiscountRowPercentage = d; }
    public void setQuantity(double d) { mdQuantity = d; }
    public void setPriceUnitary(double d) { mdPriceUnitary = d; }
    public void setDiscountUnitary(double d) { mdDiscountUnitary = d; }
    public void setDiscountRow(double d) { mdDiscountRow = d; }
    public void setSubtotalProv_r(double d) { mdSubtotalProv_r = d; }
    public void setDiscountDoc(double d) { mdDiscountDoc = d; }
    public void setSubtotal_r(double d) { mdSubtotal_r = d; }
    public void setTaxCharged_r(double d) { mdTaxCharged_r = d; }
    public void setTaxRetained_r(double d) { mdTaxRetained_r = d; }
    public void setTotal_r(double d) { mdTotal_r = d; }
    public void setPriceUnitaryCalculated_r(double d) { mdPriceUnitaryCalculated_r = d; }
    public void setPriceUnitaryCy(double d) { mdPriceUnitaryCy = d; }
    public void setDiscountUnitaryCy(double d) { mdDiscountUnitaryCy = d; }
    public void setDiscountRowCy(double d) { mdDiscountRowCy = d; }
    public void setSubtotalProvCy_r(double d) { mdSubtotalProvCy_r = d; }
    public void setDiscountDocCy(double d) { mdDiscountDocCy = d; }
    public void setSubtotalCy_r(double d) { mdSubtotalCy_r = d; }
    public void setTaxChargedCy_r(double d) { mdTaxChargedCy_r = d; }
    public void setTaxRetainedCy_r(double d) { mdTaxRetainedCy_r = d; }
    public void setTotalCy_r(double d) { mdTotalCy_r = d; }
    public void setPriceUnitaryCalculatedCy_r(double d) { mdPriceUnitaryCalculatedCy_r = d; }
    public void setTaxChargedRate1(double d) { mdTaxChargedRate1 = d; }
    public void setTaxChargedRate2(double d) { mdTaxChargedRate2 = d; }
    public void setTaxChargedRate3(double d) { mdTaxChargedRate3 = d; }
    public void setTaxRetainedRate1(double d) { mdTaxRetainedRate1 = d; }
    public void setTaxRetainedRate2(double d) { mdTaxRetainedRate2 = d; }
    public void setTaxRetainedRate3(double d) { mdTaxRetainedRate3 = d; }
    public void setTaxCharged1(double d) { mdTaxCharged1 = d; }
    public void setTaxCharged2(double d) { mdTaxCharged2 = d; }
    public void setTaxCharged3(double d) { mdTaxCharged3 = d; }
    public void setTaxRetained1(double d) { mdTaxRetained1 = d; }
    public void setTaxRetained2(double d) { mdTaxRetained2 = d; }
    public void setTaxRetained3(double d) { mdTaxRetained3 = d; }
    public void setTaxCharged1Cy(double d) { mdTaxCharged1Cy = d; }
    public void setTaxCharged2Cy(double d) { mdTaxCharged2Cy = d; }
    public void setTaxCharged3Cy(double d) { mdTaxCharged3Cy = d; }
    public void setTaxRetained1Cy(double d) { mdTaxRetained1Cy = d; }
    public void setTaxRetained2Cy(double d) { mdTaxRetained2Cy = d; }
    public void setTaxRetained3Cy(double d) { mdTaxRetained3Cy = d; }
    public void setMeasurementLength(double d) { mdMeasurementLength = d; }
    public void setMeasurementSurface(double d) { mdMeasurementSurface = d; }
    public void setMeasurementVolume(double d) { mdMeasurementVolume = d; }
    public void setMeasurementMass(double d) { mdMeasurementMass = d; }
    public void setMeasurementTime(double d) { mdMeasurementTime = d; }
    public void setWeightGross(double d) { mdWeightGross = d; }
    public void setWeightDelivery(double d) { mdWeightDelivery = d; }
    public void setSurplusPercentage(double d) { mdSurplusPercentage = d; }
    public void setTaxManual(boolean b) { mbTaxManual = b; }
    public void setInventoriable(boolean b) { mbInventoriable = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkAdjustmentCategoryId(int n) { mnFkAdjustmentCategoryId = n; }
    public void setFkAdjustmentClassId(int n) { mnFkAdjustmentClassId = n; }
    public void setFkAdjustmentTypeId(int n) { mnFkAdjustmentTypeId = n; }
    public void setFkRowItemId(int n) { mnFkRowItemId = n; }
    public void setFkRowUnitId(int n) { mnFkRowUnitId = n; }
    public void setFkReferenceItemId_n(int n) { mnFkReferenceItemId_n = n; }
    public void setFkReferenceUnitId_n(int n) { mnFkReferenceUnitId_n = n; }
    public void setFkTaxRegionId(int n) { mnFkTaxRegionId = n; }
    public void setFkTaxCharged1Id(int n) { mnFkTaxCharged1Id = n; }
    public void setFkTaxCharged2Id(int n) { mnFkTaxCharged2Id = n; }
    public void setFkTaxCharged3Id(int n) { mnFkTaxCharged3Id = n; }
    public void setFkTaxRetained1Id(int n) { mnFkTaxRetained1Id = n; }
    public void setFkTaxRetained2Id(int n) { mnFkTaxRetained2Id = n; }
    public void setFkTaxRetained3Id(int n) { mnFkTaxRetained3Id = n; }
    public void setFkSourceDpsId_n(int n) { mnFkSourceDpsId_n = n; }
    public void setFkSourceRowId_n(int n) { mnFkSourceRowId_n = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkDpsId() { return mnPkDpsId; }
    public int getPkRowId() { return mnPkRowId; }
    public String getCode() { return msCode; }
    public String getName() { return msName; }
    public String getPredial() { return msPredial; }
    public int getSortingPos() { return mnSortingPos; }
    public boolean isDiscountDocApplying() { return mbDiscountDocApplying; }
    public boolean isDiscountUnitaryPercentageApplying() { return mbDiscountUnitaryPercentageApplying; }
    public boolean isDiscountRowPercentageApplying() { return mbDiscountRowPercentageApplying; }
    public double getDiscountUnitaryPercentage() { return mdDiscountUnitaryPercentage; }
    public double getDiscountRowPercentage() { return mdDiscountRowPercentage; }
    public double getQuantity() { return mdQuantity; }
    public double getPriceUnitary() { return mdPriceUnitary; }
    public double getDiscountUnitary() { return mdDiscountUnitary; }
    public double getDiscountRow() { return mdDiscountRow; }
    public double getSubtotalProv_r() { return mdSubtotalProv_r; }
    public double getDiscountDoc() { return mdDiscountDoc; }
    public double getSubtotal_r() { return mdSubtotal_r; }
    public double getTaxCharged_r() { return mdTaxCharged_r; }
    public double getTaxRetained_r() { return mdTaxRetained_r; }
    public double getTotal_r() { return mdTotal_r; }
    public double getPriceUnitaryCalculated_r() { return mdPriceUnitaryCalculated_r; }
    public double getPriceUnitaryCy() { return mdPriceUnitaryCy; }
    public double getDiscountUnitaryCy() { return mdDiscountUnitaryCy; }
    public double getDiscountRowCy() { return mdDiscountRowCy; }
    public double getSubtotalProvCy_r() { return mdSubtotalProvCy_r; }
    public double getDiscountDocCy() { return mdDiscountDocCy; }
    public double getSubtotalCy_r() { return mdSubtotalCy_r; }
    public double getTaxChargedCy_r() { return mdTaxChargedCy_r; }
    public double getTaxRetainedCy_r() { return mdTaxRetainedCy_r; }
    public double getTotalCy_r() { return mdTotalCy_r; }
    public double getPriceUnitaryCalculatedCy_r() { return mdPriceUnitaryCalculatedCy_r; }
    public double getTaxChargedRate1() { return mdTaxChargedRate1; }
    public double getTaxChargedRate2() { return mdTaxChargedRate2; }
    public double getTaxChargedRate3() { return mdTaxChargedRate3; }
    public double getTaxRetainedRate1() { return mdTaxRetainedRate1; }
    public double getTaxRetainedRate2() { return mdTaxRetainedRate2; }
    public double getTaxRetainedRate3() { return mdTaxRetainedRate3; }
    public double getTaxCharged1() { return mdTaxCharged1; }
    public double getTaxCharged2() { return mdTaxCharged2; }
    public double getTaxCharged3() { return mdTaxCharged3; }
    public double getTaxRetained1() { return mdTaxRetained1; }
    public double getTaxRetained2() { return mdTaxRetained2; }
    public double getTaxRetained3() { return mdTaxRetained3; }
    public double getTaxCharged1Cy() { return mdTaxCharged1Cy; }
    public double getTaxCharged2Cy() { return mdTaxCharged2Cy; }
    public double getTaxCharged3Cy() { return mdTaxCharged3Cy; }
    public double getTaxRetained1Cy() { return mdTaxRetained1Cy; }
    public double getTaxRetained2Cy() { return mdTaxRetained2Cy; }
    public double getTaxRetained3Cy() { return mdTaxRetained3Cy; }
    public double getMeasurementLength() { return mdMeasurementLength; }
    public double getMeasurementSurface() { return mdMeasurementSurface; }
    public double getMeasurementVolume() { return mdMeasurementVolume; }
    public double getMeasurementMass() { return mdMeasurementMass; }
    public double getMeasurementTime() { return mdMeasurementTime; }
    public double getWeightGross() { return mdWeightGross; }
    public double getWeightDelivery() { return mdWeightDelivery; }
    public double getSurplusPercentage() { return mdSurplusPercentage; }
    public boolean isTaxManual() { return mbTaxManual; }
    public boolean isInventoriable() { return mbInventoriable; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkAdjustmentCategoryId() { return mnFkAdjustmentCategoryId; }
    public int getFkAdjustmentClassId() { return mnFkAdjustmentClassId; }
    public int getFkAdjustmentTypeId() { return mnFkAdjustmentTypeId; }
    public int getFkRowItemId() { return mnFkRowItemId; }
    public int getFkRowUnitId() { return mnFkRowUnitId; }
    public int getFkReferenceItemId_n() { return mnFkReferenceItemId_n; }
    public int getFkReferenceUnitId_n() { return mnFkReferenceUnitId_n; }
    public int getFkTaxRegionId() { return mnFkTaxRegionId; }
    public int getFkTaxCharged1Id() { return mnFkTaxCharged1Id; }
    public int getFkTaxCharged2Id() { return mnFkTaxCharged2Id; }
    public int getFkTaxCharged3Id() { return mnFkTaxCharged3Id; }
    public int getFkTaxRetained1Id() { return mnFkTaxRetained1Id; }
    public int getFkTaxRetained2Id() { return mnFkTaxRetained2Id; }
    public int getFkTaxRetained3Id() { return mnFkTaxRetained3Id; }
    public int getFkSourceDpsId_n() { return mnFkSourceDpsId_n; }
    public int getFkSourceRowId_n() { return mnFkSourceRowId_n; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setEdsItemKey(String s) { msEdsItemKey = s; }
    public void setEdsUnitKey(String s) { msEdsUnitKey = s; }
    public void setEdsSourceUuid(String s) { msEdsSourceUuid = s; }
    
    public String getEdsItemKey() { return msEdsItemKey; }
    public String getEdsUnitKey() { return msEdsUnitKey; }
    public String getEdsSourceUuid() { return msEdsSourceUuid; }
    
    public void setDbUnitCode(String s) { msDbUnitCode = s; }
    public void setDbTaxRegimeId(int n) { mnDbTaxRegimeId = n; }

    public String getDbUnitCode() { return msDbUnitCode; }
    public int getDbTaxRegimeId() { return mnDbTaxRegimeId; }
    public Vector<int[]> getDbDependentDpsRowKeys() { return mvDbDependentDpsRowKeys; }

    public Vector<DDbDpsRowNote> getChildRowNotes() { return mvChildRowNotes; }

    public void setAuxPkAdjustedDpsId(int n) { mnAuxPkAdjustedDpsId = n; }
    public void setAuxTotalAdjustedCy(double d) { mdAuxTotalAdjustedCy = d; }

    public int getAuxPkAdjustedDpsId() { return mnAuxPkAdjustedDpsId; }
    public double getAuxTotalAdjustedCy() { return mdAuxTotalAdjustedCy; }

    public Vector<DTrnStockMove> getAuxStockMoves() { return mvAuxStockMoves; }

    public int[] getAdjustmentCategoryKey() { return new int[] { mnFkAdjustmentCategoryId }; }
    public int[] getAdjustmentClassKey() { return new int[] { mnFkAdjustmentCategoryId, mnFkAdjustmentClassId }; }
    public int[] getAdjustmentTypeKey() { return new int[] { mnFkAdjustmentCategoryId, mnFkAdjustmentClassId, mnFkAdjustmentTypeId }; }
    public int[] getSourceDpsRowKey_n() { return mnFkSourceDpsId_n == DLibConsts.UNDEFINED || mnFkSourceRowId_n == DLibConsts.UNDEFINED ? null : new int[] { mnFkSourceDpsId_n, mnFkSourceRowId_n  }; }
    public boolean isDpsRowAdjustment() { return mnFkAdjustmentCategoryId != DModSysConsts.TS_ADJ_CT_NA; }

    /*
     * Overriden methods
     */

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDpsId = pk[0];
        mnPkRowId = pk[1];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDpsId, mnPkRowId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkDpsId = 0;
        mnPkRowId = 0;
        msCode = "";
        msName = "";
        msPredial = "";
        mnSortingPos = 0;
        mbDiscountDocApplying = false;
        mbDiscountUnitaryPercentageApplying = false;
        mbDiscountRowPercentageApplying = false;
        mdDiscountUnitaryPercentage = 0;
        mdDiscountRowPercentage = 0;
        mdQuantity = 0;
        mdPriceUnitary = 0;
        mdDiscountUnitary = 0;
        mdDiscountRow = 0;
        mdSubtotalProv_r = 0;
        mdDiscountDoc = 0;
        mdSubtotal_r = 0;
        mdTaxCharged_r = 0;
        mdTaxRetained_r = 0;
        mdTotal_r = 0;
        mdPriceUnitaryCalculated_r = 0;
        mdPriceUnitaryCy = 0;
        mdDiscountUnitaryCy = 0;
        mdDiscountRowCy = 0;
        mdSubtotalProvCy_r = 0;
        mdDiscountDocCy = 0;
        mdSubtotalCy_r = 0;
        mdTaxChargedCy_r = 0;
        mdTaxRetainedCy_r = 0;
        mdTotalCy_r = 0;
        mdPriceUnitaryCalculatedCy_r = 0;
        mdTaxChargedRate1 = 0;
        mdTaxChargedRate2 = 0;
        mdTaxChargedRate3 = 0;
        mdTaxRetainedRate1 = 0;
        mdTaxRetainedRate2 = 0;
        mdTaxRetainedRate3 = 0;
        mdTaxCharged1 = 0;
        mdTaxCharged2 = 0;
        mdTaxCharged3 = 0;
        mdTaxRetained1 = 0;
        mdTaxRetained2 = 0;
        mdTaxRetained3 = 0;
        mdTaxCharged1Cy = 0;
        mdTaxCharged2Cy = 0;
        mdTaxCharged3Cy = 0;
        mdTaxRetained1Cy = 0;
        mdTaxRetained2Cy = 0;
        mdTaxRetained3Cy = 0;
        mdMeasurementLength = 0;
        mdMeasurementSurface = 0;
        mdMeasurementVolume = 0;
        mdMeasurementMass = 0;
        mdMeasurementTime = 0;
        mdWeightGross = 0;
        mdWeightDelivery = 0;
        mdSurplusPercentage = 0;
        mbTaxManual = false;
        mbInventoriable = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkAdjustmentCategoryId = 0;
        mnFkAdjustmentClassId = 0;
        mnFkAdjustmentTypeId = 0;
        mnFkRowItemId = 0;
        mnFkRowUnitId = 0;
        mnFkReferenceItemId_n = 0;
        mnFkReferenceUnitId_n = 0;
        mnFkTaxRegionId = 0;
        mnFkTaxCharged1Id = 0;
        mnFkTaxCharged2Id = 0;
        mnFkTaxCharged3Id = 0;
        mnFkTaxRetained1Id = 0;
        mnFkTaxRetained2Id = 0;
        mnFkTaxRetained3Id = 0;
        mnFkSourceDpsId_n = 0;
        mnFkSourceRowId_n = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;

        msEdsItemKey = "";
        msEdsUnitKey = "";
        msEdsSourceUuid = "";
        
        msDbUnitCode = "";
        mnDbTaxRegimeId = 0;
        mvDbDependentDpsRowKeys.clear();

        mvChildRowNotes.clear();

        mnAuxPkAdjustedDpsId = 0;
        mdAuxTotalAdjustedCy = 0;

        mvAuxStockMoves.clear();
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_dps = " + mnPkDpsId + " AND " +
                "id_row = " + mnPkRowId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_dps = " + pk[0] + " AND " +
                "id_row = " + pk[1] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        ResultSet resultSet = null;

        mnPkRowId = 0;

        msSql = "SELECT COALESCE(MAX(id_row), 0) + 1 FROM " + getSqlTable() + " " +
                "WHERE id_dps = " + mnPkDpsId + " ";
        resultSet = session.getStatement().executeQuery(msSql);
        if (resultSet.next()) {
            mnPkRowId = resultSet.getInt(1);
        }
    }

    @Override
    public void read(DGuiSession session, int[] pk) throws SQLException, Exception {
        Statement statement = null;
        ResultSet resultSet = null;

        initRegistry();
        initQueryMembers();
        mnQueryResultId = DDbConsts.READ_ERROR;

        msSql = "SELECT * " + getSqlFromWhere(pk);
        resultSet = session.getStatement().executeQuery(msSql);
        if (!resultSet.next()) {
            throw new Exception(DDbConsts.ERR_MSG_REG_NOT_FOUND);
        }
        else {
            mnPkDpsId = resultSet.getInt("id_dps");
            mnPkRowId = resultSet.getInt("id_row");
            msCode = resultSet.getString("code");
            msName = resultSet.getString("name");
            msPredial = resultSet.getString("pred");
            mnSortingPos = resultSet.getInt("sort");
            mbDiscountDocApplying = resultSet.getBoolean("b_dsc_doc");
            mbDiscountUnitaryPercentageApplying = resultSet.getBoolean("b_dsc_unt_per");
            mbDiscountRowPercentageApplying = resultSet.getBoolean("b_dsc_row_per");
            mdDiscountUnitaryPercentage = resultSet.getDouble("dsc_unt_per");
            mdDiscountRowPercentage = resultSet.getDouble("dsc_row_per");
            mdQuantity = resultSet.getDouble("qty");
            mdPriceUnitary = resultSet.getDouble("prc_unt");
            mdDiscountUnitary = resultSet.getDouble("dsc_unt");
            mdDiscountRow = resultSet.getDouble("dsc_row");
            mdSubtotalProv_r = resultSet.getDouble("sbt_prv_r");
            mdDiscountDoc = resultSet.getDouble("dsc_doc");
            mdSubtotal_r = resultSet.getDouble("sbt_r");
            mdTaxCharged_r = resultSet.getDouble("tax_cha_r");
            mdTaxRetained_r = resultSet.getDouble("tax_ret_r");
            mdTotal_r = resultSet.getDouble("tot_r");
            mdPriceUnitaryCalculated_r = resultSet.getDouble("prc_unt_cal_r");
            mdPriceUnitaryCy = resultSet.getDouble("prc_unt_cy");
            mdDiscountUnitaryCy = resultSet.getDouble("dsc_unt_cy");
            mdDiscountRowCy = resultSet.getDouble("dsc_row_cy");
            mdSubtotalProvCy_r = resultSet.getDouble("sbt_prv_cy_r");
            mdDiscountDocCy = resultSet.getDouble("dsc_doc_cy");
            mdSubtotalCy_r = resultSet.getDouble("sbt_cy_r");
            mdTaxChargedCy_r = resultSet.getDouble("tax_cha_cy_r");
            mdTaxRetainedCy_r = resultSet.getDouble("tax_ret_cy_r");
            mdTotalCy_r = resultSet.getDouble("tot_cy_r");
            mdPriceUnitaryCalculatedCy_r = resultSet.getDouble("prc_unt_cal_cy_r");
            mdTaxChargedRate1 = resultSet.getDouble("tax_cha_rat_1");
            mdTaxChargedRate2 = resultSet.getDouble("tax_cha_rat_2");
            mdTaxChargedRate3 = resultSet.getDouble("tax_cha_rat_3");
            mdTaxRetainedRate1 = resultSet.getDouble("tax_ret_rat_1");
            mdTaxRetainedRate2 = resultSet.getDouble("tax_ret_rat_2");
            mdTaxRetainedRate3 = resultSet.getDouble("tax_ret_rat_3");
            mdTaxCharged1 = resultSet.getDouble("tax_cha_1");
            mdTaxCharged2 = resultSet.getDouble("tax_cha_2");
            mdTaxCharged3 = resultSet.getDouble("tax_cha_3");
            mdTaxRetained1 = resultSet.getDouble("tax_ret_1");
            mdTaxRetained2 = resultSet.getDouble("tax_ret_2");
            mdTaxRetained3 = resultSet.getDouble("tax_ret_3");
            mdTaxCharged1Cy = resultSet.getDouble("tax_cha_1_cy");
            mdTaxCharged2Cy = resultSet.getDouble("tax_cha_2_cy");
            mdTaxCharged3Cy = resultSet.getDouble("tax_cha_3_cy");
            mdTaxRetained1Cy = resultSet.getDouble("tax_ret_1_cy");
            mdTaxRetained2Cy = resultSet.getDouble("tax_ret_2_cy");
            mdTaxRetained3Cy = resultSet.getDouble("tax_ret_3_cy");
            mdMeasurementLength = resultSet.getDouble("mst_len");
            mdMeasurementSurface = resultSet.getDouble("mst_sur");
            mdMeasurementVolume = resultSet.getDouble("mst_vol");
            mdMeasurementMass = resultSet.getDouble("mst_mas");
            mdMeasurementTime = resultSet.getDouble("mst_tme");
            mdWeightGross = resultSet.getDouble("wgt_grs");
            mdWeightDelivery = resultSet.getDouble("wgt_del");
            mdSurplusPercentage = resultSet.getDouble("sur_per");
            mbTaxManual = resultSet.getBoolean("b_tax_man");
            mbInventoriable = resultSet.getBoolean("b_inv");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkAdjustmentCategoryId = resultSet.getInt("fk_adj_ct");
            mnFkAdjustmentClassId = resultSet.getInt("fk_adj_cl");
            mnFkAdjustmentTypeId = resultSet.getInt("fk_adj_tp");
            mnFkRowItemId = resultSet.getInt("fk_row_itm");
            mnFkRowUnitId = resultSet.getInt("fk_row_unt");
            mnFkReferenceItemId_n = resultSet.getInt("fk_ref_itm_n");
            mnFkReferenceUnitId_n = resultSet.getInt("fk_ref_unt_n");
            mnFkTaxRegionId = resultSet.getInt("fk_tax_reg");
            mnFkTaxCharged1Id = resultSet.getInt("fk_tax_cha_1");
            mnFkTaxCharged2Id = resultSet.getInt("fk_tax_cha_2");
            mnFkTaxCharged3Id = resultSet.getInt("fk_tax_cha_3");
            mnFkTaxRetained1Id = resultSet.getInt("fk_tax_ret_1");
            mnFkTaxRetained2Id = resultSet.getInt("fk_tax_ret_2");
            mnFkTaxRetained3Id = resultSet.getInt("fk_tax_ret_3");
            mnFkSourceDpsId_n = resultSet.getInt("fk_src_dps_n");
            mnFkSourceRowId_n = resultSet.getInt("fk_src_row_n");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            // Read database members:

            statement = session.getStatement().getConnection().createStatement();

            msDbUnitCode = (String) session.readField(DModConsts.IU_UNT, new int[] { mnFkRowUnitId }, DDbRegistry.FIELD_CODE);
            mnDbTaxRegimeId = ((DDbItem) session.readRegistry(DModConsts.IU_ITM, new int[] { mnFkRowItemId })).getFkTaxRegimeId();
            
            msSql = "SELECT id_dps, id_row FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS_ROW) + " " +
                    "WHERE b_del = 0 AND fk_src_dps_n = " + mnPkDpsId + " AND fk_src_row_n = " + mnPkRowId + " ";
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                mvDbDependentDpsRowKeys.add(new int[] { resultSet.getInt(1), resultSet.getInt(2) });
            }

            // Read aswell child registries:

            msSql = "SELECT id_not FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS_ROW_NOT) + " " + getSqlWhere();
            resultSet = statement.executeQuery(msSql);
            while (resultSet.next()) {
                DDbDpsRowNote child = new DDbDpsRowNote();
                child.read(session, new int[] { mnPkDpsId, mnPkRowId, resultSet.getInt(1) });
                mvChildRowNotes.add(child);
            }

            // Finish registry reading:

            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        // Check if dummy row in DPS adjusted needs to be created:

        if (mnAuxPkAdjustedDpsId != DLibConsts.UNDEFINED && !DLibUtils.compareKeys(getAdjustmentTypeKey(), DModSysConsts.TS_ADJ_TP_NA_NA)) {
            DDbDpsRow row = new DDbDpsRow();

            row.setPkDpsId(mnAuxPkAdjustedDpsId);
            row.setFkAdjustmentCategoryId(DModSysConsts.TS_ADJ_TP_NA_NA[0]);
            row.setFkAdjustmentClassId(DModSysConsts.TS_ADJ_TP_NA_NA[1]);
            row.setFkAdjustmentTypeId(DModSysConsts.TS_ADJ_TP_NA_NA[2]);
            row.setFkRowItemId(mnFkRowItemId);
            row.setFkRowUnitId(mnFkRowUnitId);
            row.setFkTaxRegionId(mnFkTaxRegionId);
            row.setFkTaxCharged1Id(DModSysConsts.FU_TAX_NA);
            row.setFkTaxCharged2Id(DModSysConsts.FU_TAX_NA);
            row.setFkTaxCharged3Id(DModSysConsts.FU_TAX_NA);
            row.setFkTaxRetained1Id(DModSysConsts.FU_TAX_NA);
            row.setFkTaxRetained2Id(DModSysConsts.FU_TAX_NA);
            row.setFkTaxRetained3Id(DModSysConsts.FU_TAX_NA);
            row.save(session);

            row.setDeleted(true);
            row.save(session);

            mnFkSourceDpsId_n = row.getPkDpsId();
            mnFkSourceRowId_n = row.getPkRowId();
        }

        if (mbRegistryNew) {
            computePrimaryKey(session);
            mbDeleted = false;
            //mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkDpsId + ", " +
                    mnPkRowId + ", " +
                    "'" + msCode + "', " +
                    "'" + msName + "', " +
                    "'" + msPredial + "', " + 
                    mnSortingPos + ", " +
                    (mbDiscountDocApplying ? 1 : 0) + ", " +
                    (mbDiscountUnitaryPercentageApplying ? 1 : 0) + ", " +
                    (mbDiscountRowPercentageApplying ? 1 : 0) + ", " +
                    mdDiscountUnitaryPercentage + ", " +
                    mdDiscountRowPercentage + ", " +
                    mdQuantity + ", " +
                    mdPriceUnitary + ", " +
                    mdDiscountUnitary + ", " +
                    mdDiscountRow + ", " +
                    mdSubtotalProv_r + ", " +
                    mdDiscountDoc + ", " +
                    mdSubtotal_r + ", " +
                    mdTaxCharged_r + ", " +
                    mdTaxRetained_r + ", " +
                    mdTotal_r + ", " +
                    mdPriceUnitaryCalculated_r + ", " +
                    mdPriceUnitaryCy + ", " +
                    mdDiscountUnitaryCy + ", " +
                    mdDiscountRowCy + ", " +
                    mdSubtotalProvCy_r + ", " +
                    mdDiscountDocCy + ", " +
                    mdSubtotalCy_r + ", " +
                    mdTaxChargedCy_r + ", " +
                    mdTaxRetainedCy_r + ", " +
                    mdTotalCy_r + ", " +
                    mdPriceUnitaryCalculatedCy_r + ", " +
                    mdTaxChargedRate1 + ", " +
                    mdTaxChargedRate2 + ", " +
                    mdTaxChargedRate3 + ", " +
                    mdTaxRetainedRate1 + ", " +
                    mdTaxRetainedRate2 + ", " +
                    mdTaxRetainedRate3 + ", " +
                    mdTaxCharged1 + ", " +
                    mdTaxCharged2 + ", " +
                    mdTaxCharged3 + ", " +
                    mdTaxRetained1 + ", " +
                    mdTaxRetained2 + ", " +
                    mdTaxRetained3 + ", " +
                    mdTaxCharged1Cy + ", " +
                    mdTaxCharged2Cy + ", " +
                    mdTaxCharged3Cy + ", " +
                    mdTaxRetained1Cy + ", " +
                    mdTaxRetained2Cy + ", " +
                    mdTaxRetained3Cy + ", " +
                    mdMeasurementLength + ", " +
                    mdMeasurementSurface + ", " +
                    mdMeasurementVolume + ", " +
                    mdMeasurementMass + ", " +
                    mdMeasurementTime + ", " +
                    mdWeightGross + ", " +
                    mdWeightDelivery + ", " +
                    mdSurplusPercentage + ", " +
                    (mbTaxManual ? 1 : 0) + ", " +
                    (mbInventoriable ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkAdjustmentCategoryId + ", " +
                    mnFkAdjustmentClassId + ", " +
                    mnFkAdjustmentTypeId + ", " +
                    mnFkRowItemId + ", " +
                    mnFkRowUnitId + ", " +
                    (mnFkReferenceItemId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkReferenceItemId_n) + ", " +
                    (mnFkReferenceUnitId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkReferenceUnitId_n) + ", " +
                    mnFkTaxRegionId + ", " +
                    mnFkTaxCharged1Id + ", " +
                    mnFkTaxCharged2Id + ", " +
                    mnFkTaxCharged3Id + ", " +
                    mnFkTaxRetained1Id + ", " +
                    mnFkTaxRetained2Id + ", " +
                    mnFkTaxRetained3Id + ", " +
                    (mnFkSourceDpsId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkSourceDpsId_n) + ", " +
                    (mnFkSourceRowId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkSourceRowId_n) + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    /*
                    "id_dps = " + mnPkDpsId + ", " +
                    "id_row = " + mnPkRowId + ", " +
                    */
                    "code = '" + msCode + "', " +
                    "name = '" + msName + "', " +
                    "pred = '" + msPredial + "', " +
                    "sort = " + mnSortingPos + ", " +
                    "b_dsc_doc = " + (mbDiscountDocApplying ? 1 : 0) + ", " +
                    "b_dsc_unt_per = " + (mbDiscountUnitaryPercentageApplying ? 1 : 0) + ", " +
                    "b_dsc_row_per = " + (mbDiscountRowPercentageApplying ? 1 : 0) + ", " +
                    "dsc_unt_per = " + mdDiscountUnitaryPercentage + ", " +
                    "dsc_row_per = " + mdDiscountRowPercentage + ", " +
                    "qty = " + mdQuantity + ", " +
                    "prc_unt = " + mdPriceUnitary + ", " +
                    "dsc_unt = " + mdDiscountUnitary + ", " +
                    "dsc_row = " + mdDiscountRow + ", " +
                    "sbt_prv_r = " + mdSubtotalProv_r + ", " +
                    "dsc_doc = " + mdDiscountDoc + ", " +
                    "sbt_r = " + mdSubtotal_r + ", " +
                    "tax_cha_r = " + mdTaxCharged_r + ", " +
                    "tax_ret_r = " + mdTaxRetained_r + ", " +
                    "tot_r = " + mdTotal_r + ", " +
                    "prc_unt_cal_r = " + mdPriceUnitaryCalculated_r + ", " +
                    "prc_unt_cy = " + mdPriceUnitaryCy + ", " +
                    "dsc_unt_cy = " + mdDiscountUnitaryCy + ", " +
                    "dsc_row_cy = " + mdDiscountRowCy + ", " +
                    "sbt_prv_cy_r = " + mdSubtotalProvCy_r + ", " +
                    "dsc_doc_cy = " + mdDiscountDocCy + ", " +
                    "sbt_cy_r = " + mdSubtotalCy_r + ", " +
                    "tax_cha_cy_r = " + mdTaxChargedCy_r + ", " +
                    "tax_ret_cy_r = " + mdTaxRetainedCy_r + ", " +
                    "tot_cy_r = " + mdTotalCy_r + ", " +
                    "prc_unt_cal_cy_r = " + mdPriceUnitaryCalculatedCy_r + ", " +
                    "tax_cha_rat_1 = " + mdTaxChargedRate1 + ", " +
                    "tax_cha_rat_2 = " + mdTaxChargedRate2 + ", " +
                    "tax_cha_rat_3 = " + mdTaxChargedRate3 + ", " +
                    "tax_ret_rat_1 = " + mdTaxRetainedRate1 + ", " +
                    "tax_ret_rat_2 = " + mdTaxRetainedRate2 + ", " +
                    "tax_ret_rat_3 = " + mdTaxRetainedRate3 + ", " +
                    "tax_cha_1 = " + mdTaxCharged1 + ", " +
                    "tax_cha_2 = " + mdTaxCharged2 + ", " +
                    "tax_cha_3 = " + mdTaxCharged3 + ", " +
                    "tax_ret_1 = " + mdTaxRetained1 + ", " +
                    "tax_ret_2 = " + mdTaxRetained2 + ", " +
                    "tax_ret_3 = " + mdTaxRetained3 + ", " +
                    "tax_cha_1_cy = " + mdTaxCharged1Cy + ", " +
                    "tax_cha_2_cy = " + mdTaxCharged2Cy + ", " +
                    "tax_cha_3_cy = " + mdTaxCharged3Cy + ", " +
                    "tax_ret_1_cy = " + mdTaxRetained1Cy + ", " +
                    "tax_ret_2_cy = " + mdTaxRetained2Cy + ", " +
                    "tax_ret_3_cy = " + mdTaxRetained3Cy + ", " +
                    "mst_len = " + mdMeasurementLength + ", " +
                    "mst_sur = " + mdMeasurementSurface + ", " +
                    "mst_vol = " + mdMeasurementVolume + ", " +
                    "mst_mas = " + mdMeasurementMass + ", " +
                    "mst_tme = " + mdMeasurementTime + ", " +
                    "wgt_grs = " + mdWeightGross + ", " +
                    "wgt_del = " + mdWeightDelivery + ", " +
                    "sur_per = " + mdSurplusPercentage + ", " +
                    "b_tax_man = " + (mbTaxManual ? 1 : 0) + ", " +
                    "b_inv = " + (mbInventoriable ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_adj_ct = " + mnFkAdjustmentCategoryId + ", " +
                    "fk_adj_cl = " + mnFkAdjustmentClassId + ", " +
                    "fk_adj_tp = " + mnFkAdjustmentTypeId + ", " +
                    "fk_row_itm = " + mnFkRowItemId + ", " +
                    "fk_row_unt = " + mnFkRowUnitId + ", " +
                    "fk_ref_itm_n = " + (mnFkReferenceItemId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkReferenceItemId_n) + ", " +
                    "fk_ref_unt_n = " + (mnFkReferenceUnitId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkReferenceUnitId_n) + ", " +
                    "fk_tax_reg = " + mnFkTaxRegionId + ", " +
                    "fk_tax_cha_1 = " + mnFkTaxCharged1Id + ", " +
                    "fk_tax_cha_2 = " + mnFkTaxCharged2Id + ", " +
                    "fk_tax_cha_3 = " + mnFkTaxCharged3Id + ", " +
                    "fk_tax_ret_1 = " + mnFkTaxRetained1Id + ", " +
                    "fk_tax_ret_2 = " + mnFkTaxRetained2Id + ", " +
                    "fk_tax_ret_3 = " + mnFkTaxRetained3Id + ", " +
                    "fk_src_dps_n = " + (mnFkSourceDpsId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkSourceDpsId_n) + ", " +
                    "fk_src_row_n = " + (mnFkSourceRowId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkSourceRowId_n) + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);

        // Save aswell child registries:

        msSql = "DELETE FROM " + DModConsts.TablesMap.get(DModConsts.T_DPS_ROW_NOT) + " " + getSqlWhere();
        session.getStatement().execute(msSql);

        for (DDbDpsRowNote child : mvChildRowNotes) {
            child.setPkDpsId(mnPkDpsId);
            child.setPkRowId(mnPkRowId);
            child.setRegistryNew(true);
            child.save(session);
        }

        // Finish registry updating:

        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbDpsRow clone() throws CloneNotSupportedException {
        DDbDpsRow registry = new DDbDpsRow();

        registry.setPkDpsId(this.getPkDpsId());
        registry.setPkRowId(this.getPkRowId());
        registry.setCode(this.getCode());
        registry.setName(this.getName());
        registry.setPredial(this.getPredial());
        registry.setSortingPos(this.getSortingPos());
        registry.setDiscountDocApplying(this.isDiscountDocApplying());
        registry.setDiscountUnitaryPercentageApplying(this.isDiscountUnitaryPercentageApplying());
        registry.setDiscountRowPercentageApplying(this.isDiscountRowPercentageApplying());
        registry.setDiscountUnitaryPercentage(this.getDiscountUnitaryPercentage());
        registry.setDiscountRowPercentage(this.getDiscountRowPercentage());
        registry.setQuantity(this.getQuantity());
        registry.setPriceUnitary(this.getPriceUnitary());
        registry.setDiscountUnitary(this.getDiscountUnitary());
        registry.setDiscountRow(this.getDiscountRow());
        registry.setSubtotalProv_r(this.getSubtotalProv_r());
        registry.setDiscountDoc(this.getDiscountDoc());
        registry.setSubtotal_r(this.getSubtotal_r());
        registry.setTaxCharged_r(this.getTaxCharged_r());
        registry.setTaxRetained_r(this.getTaxRetained_r());
        registry.setTotal_r(this.getTotal_r());
        registry.setPriceUnitaryCalculated_r(this.getPriceUnitaryCalculated_r());
        registry.setPriceUnitaryCy(this.getPriceUnitaryCy());
        registry.setDiscountUnitaryCy(this.getDiscountUnitaryCy());
        registry.setDiscountRowCy(this.getDiscountRowCy());
        registry.setSubtotalProvCy_r(this.getSubtotalProvCy_r());
        registry.setDiscountDocCy(this.getDiscountDocCy());
        registry.setSubtotalCy_r(this.getSubtotalCy_r());
        registry.setTaxChargedCy_r(this.getTaxChargedCy_r());
        registry.setTaxRetainedCy_r(this.getTaxRetainedCy_r());
        registry.setTotalCy_r(this.getTotalCy_r());
        registry.setPriceUnitaryCalculatedCy_r(this.getPriceUnitaryCalculatedCy_r());
        registry.setTaxChargedRate1(this.getTaxChargedRate1());
        registry.setTaxChargedRate2(this.getTaxChargedRate2());
        registry.setTaxChargedRate3(this.getTaxChargedRate3());
        registry.setTaxRetainedRate1(this.getTaxRetainedRate1());
        registry.setTaxRetainedRate2(this.getTaxRetainedRate2());
        registry.setTaxRetainedRate3(this.getTaxRetainedRate3());
        registry.setTaxCharged1(this.getTaxCharged1());
        registry.setTaxCharged2(this.getTaxCharged2());
        registry.setTaxCharged3(this.getTaxCharged3());
        registry.setTaxRetained1(this.getTaxRetained1());
        registry.setTaxRetained2(this.getTaxRetained2());
        registry.setTaxRetained3(this.getTaxRetained3());
        registry.setTaxCharged1Cy(this.getTaxCharged1Cy());
        registry.setTaxCharged2Cy(this.getTaxCharged2Cy());
        registry.setTaxCharged3Cy(this.getTaxCharged3Cy());
        registry.setTaxRetained1Cy(this.getTaxRetained1Cy());
        registry.setTaxRetained2Cy(this.getTaxRetained2Cy());
        registry.setTaxRetained3Cy(this.getTaxRetained3Cy());
        registry.setMeasurementLength(this.getMeasurementLength());
        registry.setMeasurementSurface(this.getMeasurementSurface());
        registry.setMeasurementVolume(this.getMeasurementVolume());
        registry.setMeasurementMass(this.getMeasurementMass());
        registry.setMeasurementTime(this.getMeasurementTime());
        registry.setWeightGross(this.getWeightGross());
        registry.setWeightDelivery(this.getWeightDelivery());
        registry.setSurplusPercentage(this.getSurplusPercentage());
        registry.setTaxManual(this.isTaxManual());
        registry.setInventoriable(this.isInventoriable());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkAdjustmentCategoryId(this.getFkAdjustmentCategoryId());
        registry.setFkAdjustmentClassId(this.getFkAdjustmentClassId());
        registry.setFkAdjustmentTypeId(this.getFkAdjustmentTypeId());
        registry.setFkRowItemId(this.getFkRowItemId());
        registry.setFkRowUnitId(this.getFkRowUnitId());
        registry.setFkReferenceItemId_n(this.getFkReferenceItemId_n());
        registry.setFkReferenceUnitId_n(this.getFkReferenceUnitId_n());
        registry.setFkTaxRegionId(this.getFkTaxRegionId());
        registry.setFkTaxCharged1Id(this.getFkTaxCharged1Id());
        registry.setFkTaxCharged2Id(this.getFkTaxCharged2Id());
        registry.setFkTaxCharged3Id(this.getFkTaxCharged3Id());
        registry.setFkTaxRetained1Id(this.getFkTaxRetained1Id());
        registry.setFkTaxRetained2Id(this.getFkTaxRetained2Id());
        registry.setFkTaxRetained3Id(this.getFkTaxRetained3Id());
        registry.setFkSourceDpsId_n(this.getFkSourceDpsId_n());
        registry.setFkSourceRowId_n(this.getFkSourceRowId_n());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setEdsItemKey(this.getEdsItemKey());
        registry.setEdsUnitKey(this.getEdsUnitKey());
        registry.setEdsSourceUuid(this.getEdsSourceUuid());
        
        registry.setDbUnitCode(this.getDbUnitCode());
        registry.setDbTaxRegimeId(this.getDbTaxRegimeId());

        for (int[] key : mvDbDependentDpsRowKeys) {
            registry.getDbDependentDpsRowKeys().add(key);
        }

        for (DDbDpsRowNote child : mvChildRowNotes) {
            registry.getChildRowNotes().add(child.clone());
        }

        registry.setAuxPkAdjustedDpsId(this.getAuxPkAdjustedDpsId());
        registry.setAuxTotalAdjustedCy(this.getAuxTotalAdjustedCy());

        for (DTrnStockMove aux : mvAuxStockMoves) {
            registry.getAuxStockMoves().add(aux.clone());
        }

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public void setRegistryNew(boolean registryNew) {
        super.setRegistryNew(registryNew);

        for (DDbDpsRowNote child : mvChildRowNotes) {
            child.setRegistryNew(registryNew);
        }
    }

    @Override
    public int[] getRowPrimaryKey() {
        return getPrimaryKey();
    }

    @Override
    public String getRowCode() {
        return getCode();
    }

    @Override
    public String getRowName() {
        return getName();
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return canChangeStatus();
    }

    @Override
    public boolean isRowEdited() {
        return isRegistryEdited();
    }

    @Override
    public void setRowEdited(final boolean edited) {
        setRegistryEdited(edited);
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = mnSortingPos;
                break;
            case 1:
                value = msCode;
                break;
            case 2:
                value = msName;
                break;
            case 3:
                value = msEdsItemKey;
                break;
            case 4:
                value = mdQuantity;
                break;
            case 5:
                value = msDbUnitCode;
                break;
            case 6:
                value = msEdsUnitKey;
                break;
            case 7:
                value = mdPriceUnitaryCy;
                break;
            case 8:
                value = mdSubtotalProvCy_r;
                break;
            case 9:
                value = mbDiscountDocApplying;
                break;
            case 10:
                value = mdDiscountDocCy;
                break;
            case 11:
                value = mdSubtotalCy_r;
                break;
            case 12:
                value = mdTaxChargedCy_r;
                break;
            case 13:
                value = mdTaxRetainedCy_r;
                break;
            case 14:
                value = mdTotalCy_r;
                break;
            case 15:
                value = msPredial;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getRowNumber() {
        return getSortingPos();
    }

    @Override
    public String getItemCode() {
        return getRowCode();
    }

    @Override
    public String getItemName() {
        return getRowName();
    }

    @Override
    public String getUnitCode() {
        return getDbUnitCode();
    }

    @Override
    public Vector<DTrnStockMove> getStockMoves() {
        return getAuxStockMoves();
    }

    /*
     * Other public methods
     */

    public void computeTotal(final boolean isDiscDocApplying, final boolean isDiscDocPercentageApplying, final double discDocPercentage, final double exchangeRate) {
        double subtotal = 0;
        double correction = 0;
        int decsAmount = DLibUtils.getDecimalFormatAmount().getMaximumFractionDigits();
        int decsAmountUnit = DLibUtils.getDecimalFormatAmountUnitary().getMaximumFractionDigits();

        /*
         * 1. Document currency
         */

        // Subtotal:

        if (mbDiscountUnitaryPercentageApplying) {
            mdDiscountUnitaryCy = DLibUtils.round(mdPriceUnitaryCy * mdDiscountUnitaryPercentage, decsAmountUnit);
        }

        subtotal = DLibUtils.round((mdQuantity == 0 ? 1 : mdQuantity) * DLibUtils.round(mdPriceUnitaryCy - mdDiscountUnitaryCy, decsAmountUnit), decsAmount);

        if (mbDiscountRowPercentageApplying) {
            mdDiscountRowCy = DLibUtils.round(subtotal * mdDiscountRowPercentage, decsAmount);
        }

        mdSubtotalProvCy_r = DLibUtils.round(subtotal - mdDiscountRowCy, decsAmount);

        if (!mbDiscountDocApplying || !isDiscDocApplying) {
            mdDiscountDocCy = 0;
        }
        else if (isDiscDocPercentageApplying) {
            mdDiscountDocCy = DLibUtils.round(mdSubtotalProvCy_r * discDocPercentage, decsAmount);
        }

        mdSubtotalCy_r = DLibUtils.round(mdSubtotalProvCy_r - mdDiscountDocCy, decsAmount);

        // Taxes:

        mdTaxCharged1Cy = DLibUtils.round(mdSubtotalCy_r * mdTaxChargedRate1, decsAmount);
        mdTaxCharged2Cy = DLibUtils.round(mdSubtotalCy_r * mdTaxChargedRate2, decsAmount);
        mdTaxCharged3Cy = DLibUtils.round(mdSubtotalCy_r * mdTaxChargedRate3, decsAmount);
        mdTaxChargedCy_r = DLibUtils.round(mdTaxCharged1Cy + mdTaxCharged2Cy + mdTaxCharged3Cy, decsAmount);

        mdTaxRetained1Cy = DLibUtils.round(mdSubtotalCy_r * mdTaxRetainedRate1, decsAmount);
        mdTaxRetained2Cy = DLibUtils.round(mdSubtotalCy_r * mdTaxRetainedRate2, decsAmount);
        mdTaxRetained3Cy = DLibUtils.round(mdSubtotalCy_r * mdTaxRetainedRate3, decsAmount);
        mdTaxRetainedCy_r = DLibUtils.round(mdTaxRetained1Cy + mdTaxRetained2Cy + mdTaxRetained3Cy, decsAmount);

        // Total:

        mdPriceUnitaryCy = DLibUtils.round(mdPriceUnitaryCy, decsAmountUnit);
        mdDiscountUnitaryCy = DLibUtils.round(mdDiscountUnitaryCy, decsAmountUnit);
        mdDiscountRowCy = DLibUtils.round(mdDiscountRowCy, decsAmount);
        mdSubtotalProvCy_r = DLibUtils.round(mdSubtotalProvCy_r, decsAmount);
        mdDiscountDocCy = DLibUtils.round(mdDiscountDocCy, decsAmount);
        mdSubtotalCy_r = DLibUtils.round(mdSubtotalCy_r, decsAmount);
        mdTotalCy_r = DLibUtils.round(mdSubtotalCy_r + mdTaxChargedCy_r - mdTaxRetainedCy_r, decsAmount);

        // Correct total for DPS adjustments if needed:

        if (isDpsRowAdjustment() && mdAuxTotalAdjustedCy != 0) {
            correction = DLibUtils.round(mdAuxTotalAdjustedCy, decsAmount) - DLibUtils.round(mdTotalCy_r, decsAmount);

            if (correction != 0) {
                mdPriceUnitaryCy = DLibUtils.round(mdPriceUnitaryCy + (correction / (mdQuantity == 0 ? 1 : mdQuantity)), decsAmountUnit);
                mdSubtotalProvCy_r = DLibUtils.round(mdSubtotalProvCy_r + correction, decsAmount);
                mdSubtotalCy_r = DLibUtils.round(mdSubtotalCy_r + correction, decsAmount);
                mdTotalCy_r = DLibUtils.round(mdAuxTotalAdjustedCy, decsAmount);
            }
        }

        // Calculated unitary price:

        mdPriceUnitaryCalculatedCy_r = DLibUtils.round(mdSubtotalCy_r / (mdQuantity == 0 ? 1 : mdQuantity), decsAmountUnit);

        /*
         * 2. Domestic currency
         */

        if (exchangeRate == 1) {
            // Total:

            mdTotal_r = mdTotalCy_r;

            // Taxes:

            mdTaxCharged1 = mdTaxCharged1Cy;
            mdTaxCharged2 = mdTaxCharged2Cy;
            mdTaxCharged3 = mdTaxCharged3Cy;
            mdTaxCharged_r = mdTaxChargedCy_r;

            mdTaxRetained1 = mdTaxRetained1Cy;
            mdTaxRetained2 = mdTaxRetained2Cy;
            mdTaxRetained3 = mdTaxRetained3Cy;
            mdTaxRetained_r = mdTaxRetainedCy_r;

            // Subtotal:

            mdSubtotal_r = mdSubtotalCy_r;

            mdDiscountDoc = mdDiscountDocCy;
            mdSubtotalProv_r = mdSubtotalProvCy_r;

            mdPriceUnitary = mdPriceUnitaryCy;
            mdDiscountUnitary = mdDiscountUnitaryCy;
            mdDiscountRow = mdDiscountRowCy;

            // Calculated unitary price:

            mdPriceUnitaryCalculated_r = mdPriceUnitaryCalculatedCy_r;
        }
        else {
            // Total:

            mdTotal_r = DLibUtils.round(mdTotalCy_r * exchangeRate, decsAmount);

            // Taxes:

            mdTaxCharged1 = DLibUtils.round(mdTaxCharged1Cy * exchangeRate, decsAmount);
            mdTaxCharged2 = DLibUtils.round(mdTaxCharged2Cy * exchangeRate, decsAmount);
            mdTaxCharged3 = DLibUtils.round(mdTaxCharged3Cy * exchangeRate, decsAmount);
            mdTaxCharged_r = DLibUtils.round(mdTaxCharged1 + mdTaxCharged2 + mdTaxCharged3, decsAmount);

            mdTaxRetained1 = DLibUtils.round(mdTaxRetained1Cy * exchangeRate, decsAmount);
            mdTaxRetained2 = DLibUtils.round(mdTaxRetained2Cy * exchangeRate, decsAmount);
            mdTaxRetained3 = DLibUtils.round(mdTaxRetained3Cy * exchangeRate, decsAmount);
            mdTaxRetained_r = DLibUtils.round(mdTaxRetained1 + mdTaxRetained2 + mdTaxRetained3, decsAmount);

            // Subtotal:

            mdSubtotal_r = DLibUtils.round(mdTotal_r - mdTaxCharged_r + mdTaxRetained_r, decsAmount);

            mdDiscountDoc = DLibUtils.round(mdDiscountDocCy * exchangeRate, decsAmount);
            mdSubtotalProv_r = DLibUtils.round(mdSubtotal_r + mdDiscountDoc, decsAmount);

            mdDiscountRow = DLibUtils.round(mdDiscountRowCy * exchangeRate, decsAmount);
            mdDiscountUnitary = DLibUtils.round(mdDiscountUnitaryCy * exchangeRate, decsAmountUnit);
            mdPriceUnitary = DLibUtils.round(mdPriceUnitaryCy * exchangeRate, decsAmountUnit);

            // Calculated unitary price:

            mdPriceUnitaryCalculated_r = DLibUtils.round(mdSubtotal_r / (mdQuantity == 0 ? 1 : mdQuantity), decsAmountUnit);
        }
    }
}
