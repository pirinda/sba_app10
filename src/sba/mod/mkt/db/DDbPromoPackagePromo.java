/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.mkt.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.db.DDbRegistryUser;
import sba.lib.grid.DGridRow;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;
import sba.mod.itm.db.DItemUtils;

/**
 *
 * @author Sergio Flores
 */
public class DDbPromoPackagePromo extends DDbRegistryUser implements DGridRow {

    protected int mnPkPromoPackageId;
    protected int mnPkPaymentTypeId;
    protected int mnPkLinkItemTypeId;
    protected int mnPkReferenceItemId;
    protected double mdDiscountPercentage;

    protected String msDbLinkItemTypeName;
    protected String msDbReferenceItemName;
    protected String msDbPaymentTypeName;

    public DDbPromoPackagePromo() {
        super(DModConsts.M_PRM_PRM);
        initRegistry();
    }

    public void setPkPromoPackageId(int n) { mnPkPromoPackageId = n; }
    public void setPkPaymentTypeId(int n) { mnPkPaymentTypeId = n; }
    public void setPkLinkItemTypeId(int n) { mnPkLinkItemTypeId = n; }
    public void setPkReferenceItemId(int n) { mnPkReferenceItemId = n; }
    public void setDiscountPercentage(double d) { mdDiscountPercentage = d; }

    public int getPkPromoPackageId() { return mnPkPromoPackageId; }
    public int getPkPaymentTypeId() { return mnPkPaymentTypeId; }
    public int getPkLinkItemTypeId() { return mnPkLinkItemTypeId; }
    public int getPkReferenceItemId() { return mnPkReferenceItemId; }
    public double getDiscountPercentage() { return mdDiscountPercentage; }

    public void setDbLinkItemTypeName(String s) { msDbLinkItemTypeName = s; }
    public void setDbReferenceItemName(String s) { msDbReferenceItemName = s; }
    public void setDbPaymentTypeName(String s) { msDbPaymentTypeName = s; }

    public String getDbLinkItemTypeName() { return msDbLinkItemTypeName; }
    public String getDbReferenceItemName() { return msDbReferenceItemName; }
    public String getDbPaymentTypeName() { return msDbPaymentTypeName; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkPromoPackageId = pk[0];
        mnPkPaymentTypeId = pk[1];
        mnPkLinkItemTypeId = pk[2];
        mnPkReferenceItemId = pk[3];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkPromoPackageId, mnPkPaymentTypeId, mnPkLinkItemTypeId, mnPkReferenceItemId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkPromoPackageId = 0;
        mnPkPaymentTypeId = 0;
        mnPkLinkItemTypeId = 0;
        mnPkReferenceItemId = 0;
        mdDiscountPercentage = 0;

        msDbLinkItemTypeName = "";
        msDbReferenceItemName = "";
        msDbPaymentTypeName = "";
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_prm = " + mnPkPromoPackageId + " AND " +
                "id_pay_tp = " + mnPkPaymentTypeId + " AND " +
                "id_lnk_itm_tp = " + mnPkLinkItemTypeId + " AND " +
                "id_ref_itm = " + mnPkReferenceItemId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_prm = " + pk[0] + " AND " +
                "id_pay_tp = " + pk[1] + " AND " +
                "id_lnk_itm_tp = " + pk[2] + " AND " +
                "id_ref_itm = " + pk[3] + " ";
    }

    @Override
    public void computePrimaryKey(DGuiSession session) throws SQLException, Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void read(DGuiSession session, int[] pk) throws SQLException, Exception {
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
            mnPkPromoPackageId = resultSet.getInt("id_prm");
            mnPkPaymentTypeId = resultSet.getInt("id_pay_tp");
            mnPkLinkItemTypeId = resultSet.getInt("id_lnk_itm_tp");
            mnPkReferenceItemId = resultSet.getInt("id_ref_itm");
            mdDiscountPercentage = resultSet.getDouble("dsc_per");

            // Read aswell complementary database values:

            msDbLinkItemTypeName = (String) session.readField(DModConsts.IS_LNK_ITM_TP, new int[] { mnPkLinkItemTypeId }, DDbRegistry.FIELD_NAME);
            msDbReferenceItemName = DItemUtils.readItemLinkName(session, mnPkLinkItemTypeId, mnPkReferenceItemId);
            msDbPaymentTypeName = (String) session.readField(DModConsts.FS_PAY_TP, new int[] { mnPkPaymentTypeId }, DDbRegistry.FIELD_NAME);

            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }

        if (mbRegistryNew) {
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkPromoPackageId + ", " +
                    mnPkPaymentTypeId + ", " +
                    mnPkLinkItemTypeId + ", " +
                    mnPkReferenceItemId + ", " +
                    mdDiscountPercentage + " " +
                    ")";
        }
        else {
            throw new Exception(DDbConsts.ERR_MSG_REG_NON_UPDATABLE);
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbPromoPackagePromo clone() throws CloneNotSupportedException {
        DDbPromoPackagePromo registry = new DDbPromoPackagePromo();

        registry.setPkPromoPackageId(this.getPkPromoPackageId());
        registry.setPkPaymentTypeId(this.getPkPaymentTypeId());
        registry.setPkLinkItemTypeId(this.getPkLinkItemTypeId());
        registry.setPkReferenceItemId(this.getPkReferenceItemId());
        registry.setDiscountPercentage(this.getDiscountPercentage());

        registry.setDbLinkItemTypeName(this.getDbLinkItemTypeName());
        registry.setDbReferenceItemName(this.getDbReferenceItemName());
        registry.setDbPaymentTypeName(this.getDbPaymentTypeName());

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
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
        return isSystem();
    }

    @Override
    public boolean isRowDeletable() {
        return isDeletable();
    }

    @Override
    public boolean isRowEdited() {
        return isRegistryEdited();
    }

    @Override
    public void setRowEdited(boolean edited) {
        setRegistryEdited(edited);
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;

        switch (col) {
            case 0:
                value = msDbLinkItemTypeName;
                break;
            case 1:
                value = msDbReferenceItemName;
                break;
            case 2:
                value = msDbPaymentTypeName;
                break;
            case 3:
                value = mdDiscountPercentage;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 3:
                mdDiscountPercentage = (Double) value;
                break;
            default:
        }
    }
}
