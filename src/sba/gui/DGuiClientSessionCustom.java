/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.gui;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiSessionCustom;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.bpr.db.DDbBranch;
import sba.mod.cfg.db.DDbBranchCash;
import sba.mod.cfg.db.DDbBranchWarehouse;
import sba.mod.cfg.db.DDbConfigCompany;
import sba.mod.trn.db.DDbDpsSeriesBranch;

/**
 *
 * @author Sergio Flores
 */
public final class DGuiClientSessionCustom implements DGuiSessionCustom {

    private DGuiClient miClient;
    private Statement miStatement;

    private int[] manLocalCountryKey;
    private int[] manLocalCurrencyKey;
    private String msLocalCountry;
    private String msLocalCountryCode;
    private String msLocalCurrency;
    private String msLocalCurrencyCode;
    private String msLocalLanguage;

    private int mnTerminal;

    private int[] manBranchKey;
    private int[] manBranchCashKey;
    private int[] manBranchWarehouseKey;
    private int[] manBranchDpsSeriesKey;

    public DGuiClientSessionCustom(DGuiClient client) {
        DDbConfigCompany configCompany = null;

        try {
            miClient = client;
            miStatement = miClient.getSession().getStatement().getConnection().createStatement();

            configCompany = (DDbConfigCompany) miClient.getSession().getConfigCompany();

            manLocalCountryKey = new int[] { configCompany.getFkCountryId() };
            msLocalCountry = (String) miClient.getSession().readField(DModConsts.CS_CTY, manLocalCountryKey, DDbRegistry.FIELD_NAME);
            msLocalCountryCode = (String) miClient.getSession().readField(DModConsts.CS_CTY, manLocalCountryKey, DDbRegistry.FIELD_CODE);

            manLocalCurrencyKey = new int[] { configCompany.getFkCurrencyId() };
            msLocalCurrency = (String) miClient.getSession().readField(DModConsts.CS_CUR, manLocalCurrencyKey, DDbRegistry.FIELD_NAME);
            msLocalCurrencyCode = (String) miClient.getSession().readField(DModConsts.CS_CUR, manLocalCurrencyKey, DDbRegistry.FIELD_CODE);

            msLocalLanguage = DLibConsts.LAN_ISO639_ES;

            mnTerminal = 0;

            manBranchKey = null;
            manBranchCashKey = null;
            manBranchWarehouseKey = null;
            manBranchDpsSeriesKey = null;
        }
        catch (SQLException e) {
            DLibUtils.showException(this, e);
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }
    }

    /*
     * Private methods
     */

    private Object readField(final int registry, final int[] key, final int field) {
        Object value = "";

        try {
            value = miClient.getSession().getRegistry(registry, null).readField(miStatement, key, field);
        }
        catch (SQLException e) {
            DLibUtils.showException(this, e);
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }

        return value;
    }

    /*
     * Public methods
     */

    public void setTerminal(int n) { mnTerminal = n; }

    public void setBranchKey(int[] an) { manBranchKey = an; }
    public void setBranchCashKey(int[] an) { manBranchCashKey = an; }
    public void setBranchWarehouseKey(int[] an) { manBranchWarehouseKey = an; }
    public void setBranchDpsSeriesKey(int[] an) { manBranchDpsSeriesKey = an; }

    public int[] getLocalCountryKey() { return manLocalCountryKey; }
    public String getLocalCountry() { return msLocalCountry; }
    public String getLocalCountryCode() { return msLocalCountryCode; }
    public boolean isLocalCountry(final int[] key) { return DLibUtils.compareKeys(key, manLocalCountryKey); }
    public int[] getLocalCurrencyKey() { return manLocalCurrencyKey; }
    public String getLocalCurrency() { return msLocalCurrency; }
    public String getLocalCurrencyCode() { return msLocalCurrencyCode; }
    public boolean isLocalCurrency(final int[] key) { return DLibUtils.compareKeys(key, manLocalCurrencyKey); }
    public String getLocalLanguage() { return msLocalLanguage; }

    public String getCountry(final int[] key) { return (String) readField(DModConsts.CS_CTY, key, DDbRegistry.FIELD_NAME); }
    public String getCountryCode(final int[] key) { return (String) readField(DModConsts.CS_CTY, key, DDbRegistry.FIELD_CODE); }
    public String getCurrency(final int[] key) {return (String) readField(DModConsts.CS_CUR, key, DDbRegistry.FIELD_NAME); }
    public String getCurrencyCode(final int[] key) { return (String) readField(DModConsts.CS_CUR, key, DDbRegistry.FIELD_CODE); }
    public String getLanguage(final int[] key) { throw new UnsupportedOperationException("Not supported yet."); }

    public int getTerminal() { return mnTerminal; }

    public int getIdentityTypeDefault() {
        return DModSysConsts.BS_IDY_TP_ORG;
    }

    public int[] getBranchKey() { return manBranchKey; }
    public int[] getBranchCashKey() { return manBranchCashKey; }
    public int[] getBranchWarehouseKey() { return manBranchWarehouseKey; }
    public int[] getBranchDpsSeriesKey() { return manBranchDpsSeriesKey; }

    public Vector<DDbBranch> getAllBranches() {
        String sql = "";
        Statement statement = null;
        ResultSet resultSet = null;
        Vector<DDbBranch> registries = new Vector<>();

        try {
            statement = miClient.getSession().getStatement().getConnection().createStatement();

            sql = "SELECT id_bpr, id_bra FROM " + DModConsts.TablesMap.get(DModConsts.BU_BRA) + " " +
                    "WHERE b_del = 0 AND id_bpr = " + DUtilConsts.BPR_CO_ID + " " +
                    "ORDER BY name, id_bpr, id_bra ";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                DDbBranch registry = new DDbBranch();
                registry.read(miClient.getSession(), new int[] { resultSet.getInt(1), resultSet.getInt(2) });
                registries.add(registry);
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(this, e);
            DLibUtils.printSqlQuery(this, sql);
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }

        return registries;
    }

    public Vector<DDbBranchCash> getAllBranchCashes() {
        String sql = "";
        Statement statement = null;
        ResultSet resultSet = null;
        Vector<DDbBranchCash> registries = new Vector<>();

        try {
            statement = miClient.getSession().getStatement().getConnection().createStatement();

            sql = "SELECT e.id_bpr, e.id_bra, e.id_csh " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.CU_CSH) + " AS e " +
                    "WHERE e.b_del = 0 " +
                    "ORDER BY e.name, e.id_bpr, e.id_bra, e.id_csh ";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                DDbBranchCash registry = new DDbBranchCash();
                registry.read(miClient.getSession(), new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
                registries.add(registry);
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(this, e);
            DLibUtils.printSqlQuery(this, sql);
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }

        return registries;
    }

    public Vector<DDbBranchWarehouse> getAllBranchWarehouses() {
        String sql = "";
        Statement statement = null;
        ResultSet resultSet = null;
        Vector<DDbBranchWarehouse> registries = new Vector<>();

        try {
            statement = miClient.getSession().getStatement().getConnection().createStatement();

            sql = "SELECT e.id_bpr, e.id_bra, e.id_wah " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.CU_WAH) + " AS e " +
                    "WHERE e.b_del = 0 " +
                    "ORDER BY e.name, e.id_bpr, e.id_bra, e.id_wah ";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                DDbBranchWarehouse registry = new DDbBranchWarehouse();
                registry.read(miClient.getSession(), new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
                registries.add(registry);
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(this, e);
            DLibUtils.printSqlQuery(this, sql);
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }

        return registries;
    }

    public Vector<DDbDpsSeriesBranch> getAllBranchDpsSeries() {
        String sql = "";
        Statement statement = null;
        ResultSet resultSet = null;
        Vector<DDbDpsSeriesBranch> registries = new Vector<>();

        try {
            statement = miClient.getSession().getStatement().getConnection().createStatement();

            sql = "SELECT e.id_ser, e.id_bpr, e.id_bra " +
                    "FROM " + DModConsts.TablesMap.get(DModConsts.TU_SER_BRA) + " AS e " +
                    "INNER JOIN " + DModConsts.TablesMap.get(DModConsts.TU_SER) + " AS s ON " +
                    "e.id_ser = s.id_ser " +
                    "WHERE s.b_del = 0 " +
                    "ORDER BY s.fk_dps_ct, s.fk_dps_cl, s.fk_dps_tp, s.ser, e.id_ser, e.id_bpr, e.id_bra ";
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                DDbDpsSeriesBranch registry = new DDbDpsSeriesBranch();
                registry.read(miClient.getSession(), new int[] { resultSet.getInt(1), resultSet.getInt(2), resultSet.getInt(3) });
                registries.add(registry);
            }
        }
        catch (SQLException e) {
            DLibUtils.showException(this, e);
            DLibUtils.printSqlQuery(this, sql);
        }
        catch (Exception e) {
            DLibUtils.showException(this, e);
        }

        return registries;
    }

    public int[] createFilterBranchCashKey() {
        int[] key = null;

        if (manBranchCashKey != null) {
            key = manBranchCashKey;
        }
        else if (manBranchKey != null) {
            key = new int[] { manBranchKey[0], manBranchKey[1], DLibConsts.UNDEFINED };
        }
        else {
            key = new int[3];
        }

        return key;
    }

    public int[] createFilterBranchWarehouseKey() {
        int[] key = null;

        if (manBranchWarehouseKey != null) {
            key = manBranchWarehouseKey;
        }
        else if (manBranchKey != null) {
            key = new int[] { manBranchKey[0], manBranchKey[1], DLibConsts.UNDEFINED };
        }
        else {
            key = new int[3];
        }

        return key;
    }
}
