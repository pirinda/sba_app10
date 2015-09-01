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

/**
 *
 * @author Sergio Flores
 */
public class DDbSpecialPriceListPrice extends DDbRegistryUser implements DGridRow {

    protected int mnPkSpecialPriceListId;
    protected int mnPkPaymentTypeId;
    protected int mnPkItemId;
    protected double mdPrice;

    protected String msDbItemCode;
    protected String msDbItemName;
    protected String msDbPaymentTypeName;
    protected String msDbCurrencyCode;

    public DDbSpecialPriceListPrice() {
        super(DModConsts.M_SPE_PRC);
        initRegistry();
    }

    public void setPkSpecialPriceListId(int n) { mnPkSpecialPriceListId = n; }
    public void setPkPaymentTypeId(int n) { mnPkPaymentTypeId = n; }
    public void setPkItemId(int n) { mnPkItemId = n; }
    public void setPrice(double d) { mdPrice = d; }

    public int getPkSpecialPriceListId() { return mnPkSpecialPriceListId; }
    public int getPkPaymentTypeId() { return mnPkPaymentTypeId; }
    public int getPkItemId() { return mnPkItemId; }
    public double getPrice() { return mdPrice; }

    public void setDbItemCode(String s) { msDbItemCode = s; }
    public void setDbItemName(String s) { msDbItemName = s; }
    public void setDbPaymentTypeName(String s) { msDbPaymentTypeName = s; }
    public void setDbCurrencyCode(String s) { msDbCurrencyCode = s; }

    public String getDbItemCode() { return msDbItemCode; }
    public String getDbItemName() { return msDbItemName; }
    public String getDbPaymentTypeName() { return msDbPaymentTypeName; }
    public String getDbCurrencyCode() { return msDbCurrencyCode; }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkSpecialPriceListId = pk[0];
        mnPkPaymentTypeId = pk[1];
        mnPkItemId = pk[2];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkSpecialPriceListId, mnPkPaymentTypeId, mnPkItemId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkSpecialPriceListId = 0;
        mnPkPaymentTypeId = 0;
        mnPkItemId = 0;
        mdPrice = 0;

        msDbItemCode = "";
        msDbItemName = "";
        msDbPaymentTypeName = "";
        msDbCurrencyCode = "";
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_spe = " + mnPkSpecialPriceListId + " AND " +
                "id_pay_tp = " + mnPkPaymentTypeId + " AND " +
                "id_itm = " + mnPkItemId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_spe = " + pk[0] + " AND " +
                "id_pay_tp = " + pk[1] + " AND " +
                "id_itm = " + pk[2] + " ";
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
            mnPkSpecialPriceListId = resultSet.getInt("id_spe");
            mnPkPaymentTypeId = resultSet.getInt("id_pay_tp");
            mnPkItemId = resultSet.getInt("id_itm");
            mdPrice = resultSet.getDouble("prc");

            // Read aswell complementary database values:

            msDbItemCode = (String) session.readField(DModConsts.IU_ITM, new int[] { mnPkItemId }, DDbRegistry.FIELD_CODE);
            msDbItemName = (String) session.readField(DModConsts.IU_ITM, new int[] { mnPkItemId }, DDbRegistry.FIELD_NAME);
            msDbPaymentTypeName = (String) session.readField(DModConsts.FS_PAY_TP, new int[] { mnPkPaymentTypeId }, DDbRegistry.FIELD_NAME);
            msDbCurrencyCode = session.getSessionCustom().getLocalCurrencyCode();

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
                    mnPkSpecialPriceListId + ", " +
                    mnPkPaymentTypeId + ", " +
                    mnPkItemId + ", " +
                    mdPrice + " " +
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
    public DDbSpecialPriceListPrice clone() throws CloneNotSupportedException {
        DDbSpecialPriceListPrice registry = new DDbSpecialPriceListPrice();

        registry.setPkSpecialPriceListId(this.getPkSpecialPriceListId());
        registry.setPkPaymentTypeId(this.getPkPaymentTypeId());
        registry.setPkItemId(this.getPkItemId());
        registry.setPrice(this.getPrice());

        registry.setDbItemCode(this.getDbItemCode());
        registry.setDbItemName(this.getDbItemName());
        registry.setDbPaymentTypeName(this.getDbPaymentTypeName());
        registry.setDbCurrencyCode(this.getDbCurrencyCode());

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
                value = msDbItemCode;
                break;
            case 1:
                value = msDbItemName;
                break;
            case 2:
                value = msDbPaymentTypeName;
                break;
            case 3:
                value = mdPrice;
                break;
            case 4:
                value = msDbCurrencyCode;
                break;
            default:
        }

        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        switch (col) {
            case 3:
                mdPrice = (Double) value;
                break;
            default:
        }
    }
}
