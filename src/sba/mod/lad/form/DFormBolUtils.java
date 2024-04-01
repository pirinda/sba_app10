/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.lad.form;

import cfd.ver40.DCfdi40Catalogs;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import sba.gui.cat.DXmlCatalog;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.grid.DGridColumnForm;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridPaneForm;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiField;
import sba.lib.gui.DGuiSession;
import sba.lib.gui.bean.DBeanFieldKey;
import sba.lib.gui.bean.DBeanFieldText;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.itm.db.DDbItem;
import sba.mod.itm.db.DDbUnit;
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

/**
 *
 * @author Sergio Flores
 */
public abstract class DFormBolUtils {
    
    public static final String NA = "ND"; // not available
    
    public static final String DEF_CODE_LOCATION = "000000";
    public static final String DEF_CODE_ADDRESS_STATE = "XXX";
    public static final String DEF_CODE_ADDRESS_COUNTY = "000";
    public static final String DEF_CODE_ADDRESS_LOCALITY = "00";
    public static final String DEF_CODE_ADDRESS_DISTRICT = "0000";
    public static final String DEF_CODE_HAZARDOUS_MATERIAL = "0000";
    public static final String DEF_CODE_PACKAGING = "XXXX";
    public static final String DEF_SEGMENT_IMPORT_REQUEST_1 = "00";
    public static final String DEF_SEGMENT_IMPORT_REQUEST_2 = "00";
    public static final String DEF_SEGMENT_IMPORT_REQUEST_3 = "0000";
    public static final String DEF_SEGMENT_IMPORT_REQUEST_4 = "0000000";
    
    public static final String ATT_COUNTRY = "country";
    public static final String ATT_STATE = "state";
    public static final String ATT_ZIP = "zip";
    public static final String ATT_TRAILER = "trailer";
    
    public static final int LEN_ZIP = 5;
    
    public static HashMap<String, DXmlCatalog> XmlCatalogsMap = new HashMap<>();
    
    public static DecimalFormat FormatCodeLocation = new DecimalFormat(DEF_CODE_LOCATION);
    public static DecimalFormat FormatCodeAddressCounty = new DecimalFormat(DEF_CODE_ADDRESS_COUNTY);
    public static DecimalFormat FormatCodeAddressLocality = new DecimalFormat(DEF_CODE_ADDRESS_LOCALITY);
    public static DecimalFormat FormatCodeAddressDistrict = new DecimalFormat(DEF_CODE_ADDRESS_DISTRICT);
    public static DecimalFormat FormatCodeHazardousMaterial = new DecimalFormat(DEF_CODE_HAZARDOUS_MATERIAL);
    public static DecimalFormat FormatSegmentImportRequest1 = new DecimalFormat(DEF_SEGMENT_IMPORT_REQUEST_1);
    public static DecimalFormat FormatSegmentImportRequest2 = new DecimalFormat(DEF_SEGMENT_IMPORT_REQUEST_2);
    public static DecimalFormat FormatSegmentImportRequest3 = new DecimalFormat(DEF_SEGMENT_IMPORT_REQUEST_3);
    public static DecimalFormat FormatSegmentImportRequest4 = new DecimalFormat(DEF_SEGMENT_IMPORT_REQUEST_4);
    
    public static DXmlCatalog getXmlCatalog(final String name) throws Exception {
        return getXmlCatalog(name, "", "", null);
    }
    
    public static DXmlCatalog getXmlCatalog(final String name, final String belongAttribute, String searchAttribute, final String[] extraAttriburtes) throws Exception {
        DXmlCatalog xmlCatalog = XmlCatalogsMap.get(name);
        
        if (xmlCatalog == null) {
            xmlCatalog = new DXmlCatalog(name, "xml/" + name + DXmlCatalog.XmlFileExt, false, belongAttribute, searchAttribute, extraAttriburtes);
            XmlCatalogsMap.put(name, xmlCatalog);
        }
        
        return xmlCatalog;
    }

    public static void computeCatalogCode(final DBeanFieldText textCode, final DBeanFieldText textName, final String defaultCode, final DecimalFormat formatCode, 
            final String catalog, final boolean appendFirstCharCodeToCatalog, final DGuiField fieldFilter, final String attributeFilter) {
        if (textCode.getValue().isEmpty()) {
            // clear code & name:
            textCode.setValue(defaultCode);
            textName.resetField();
        }
        else {
            if (formatCode != null) {
                textCode.setValue(formatCode.format(DLibUtils.parseInt(textCode.getValue())));
            }
            else {
                textCode.setValue(textCode.getValue().toUpperCase());
            }
            
            String filter = "";
            boolean missingFilter = false;
            
            if (fieldFilter != null) {
                if (fieldFilter instanceof DBeanFieldText) {
                    if (!((DBeanFieldText) fieldFilter).getValue().isEmpty()) {
                        filter = ((DBeanFieldText) fieldFilter).getValue();
                    }
                    else {
                        missingFilter = true;
                    }
                }
                else if (fieldFilter instanceof DBeanFieldKey) {
                    if (((DBeanFieldKey) fieldFilter).getSelectedIndex() > 0) {
                        filter = ((DBeanFieldKey) fieldFilter).getSelectedItem().getCode();
                    }
                    else {
                        missingFilter = true;
                    }
                }
            }
            
            if (missingFilter) {
                textName.setValue("(" + DUtilConsts.TXT_SELECT + " " + fieldFilter.getFieldName() + ")");
            }
            else {
                try {
                    String catalogName = catalog;
                    if (appendFirstCharCodeToCatalog) {
                        catalogName += "_" + DLibUtils.textLeft(filter, 1);
                    }
                    
                    DXmlCatalog xmlCatalog = DFormBolUtils.getXmlCatalog(catalogName, attributeFilter, "", null);
                    int id = xmlCatalog.getId(textCode.getValue(), filter);
                    if (id != 0) {
                        textName.setValue(xmlCatalog.getName(id));
                    }
                    else {
                        textName.setValue("(" + DLibUtils.textProperCase(textName.getFieldName()) + " " + DUtilConsts.TXT_UNKNOWN.toLowerCase() + ")");
                    }
                }
                catch (Exception e) {
                    DLibUtils.showException(DFormBolUtils.class.getName(), e);
                }
            }
        }
    }
    
    /**
     * Check if contry code equals MEX, USA or CAN.
     * @param countryCode Country code.
     * @return <code>true</code> if contry code equals MEX, USA or CAN, otherwise <code>false</code>.
     */
    public static boolean applyStateCatalog(final String countryCode) {
        return countryCode.equals(DCfdi40Catalogs.ClavePaísMex) || countryCode.equals(DCfdi40Catalogs.ClavePaísUsa) || countryCode.equals(DCfdi40Catalogs.ClavePaísCan);
    }
    
    /**
     * Check if contry code equals MEX.
     * @param countryCode Country code.
     * @return <code>true</code> if contry code equals MEX, otherwise <code>false</code>.
     */
    public static boolean applyAddressCatalogs(final String countryCode) {
        return countryCode.equals(DCfdi40Catalogs.ClavePaísMex);
    }

    /**
     * Get unit ID associated to kilogram.
     * @param session GUI session.
     * @return If found, unit ID associated to kilogram, otherwise <code>0</code>.
     * @throws Exception
     */
    public static int getWeightUnitId(final DGuiSession session) throws Exception {
        int unitId = 0;
        String sql = "SELECT id_unt " + "FROM " + DModConsts.TablesMap.get(DModConsts.IU_UNT) + " " + "WHERE code = 'kg' AND NOT b_del " + "ORDER BY id_unt " + "LIMIT 1;";
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                unitId = resultSet.getInt(1);
            }
        }
        return unitId;
    }
    
    /**
     * Parse BOL series.
     * @param bolSeries BOL series as set in configuration.
     * @param transportTypeCode SAT code for required transport type.
     * @return 
     */
    private static String[] parseBolSeries(final String bolSeries, final String transportTypeCode) {
        String[] series = null;
        
        if (!bolSeries.isEmpty()) {
            String[] configs = DLibUtils.textExplode(bolSeries, ";");
            
            if (configs.length > 0) {
                for (String config : configs) {
                    String[] configByType = config.split("=");
                    if (configByType.length > 0 && configByType[0].equals(transportTypeCode)) {
                        switch (configByType.length) {
                            case 1:
                                series = new String[] { "" };
                                break;
                            case 2:
                                series = DLibUtils.textExplode(configByType[1], ",");
                                break;
                            default:
                                // nothing
                        }
                        break;
                    }
                }
            }
        }
        
        return series;
    }

    /**
     * Get BOL series for required transport type. First in company's branch configuration, then in company's configuration.
     * BOL number series, e.g., 01=;02=A;03=B,C (code of transport type and number series, separated by semicolon).
     * @param session GUI session.
     * @param branchKey Current company's branch key.
     * @param transportTypeCode SAT code for required transport type.
     * @return If found, BOL series, otherwise <code>null</code>.
     * @throws Exception
     */
    public static String[] getBolSeries(final DGuiSession session, final int[] branchKey, final String transportTypeCode) throws Exception {
        String[] series = null;
        String bolSeries = "";
        String sql;
        
        sql = "SELECT bol_ser " + "FROM " + DModConsts.TablesMap.get(DModConsts.CU_CFG_BRA) + " " + "WHERE id_bpr = " + branchKey[0] + " AND id_bra = " + branchKey[1] + ";";
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                bolSeries = resultSet.getString("bol_ser");
            }
        }
        
        series = parseBolSeries(bolSeries, transportTypeCode);
        
        if (series == null) {
            sql = "SELECT bol_ser " + "FROM " + DModConsts.TablesMap.get(DModConsts.CU_CFG_CO) + " " + "WHERE id_bpr = " + branchKey[0] + ";";
            try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
                if (resultSet.next()) {
                    bolSeries = resultSet.getString("bol_ser");
                }
            }
            
            series = parseBolSeries(bolSeries, transportTypeCode);
        }
        
        return series;
    }
    
    public static DGridPaneForm createGridLocations(final DGuiClient client, final DFormBol formBol) {
        DGridPaneForm grid = new DGridPaneForm(client, DModConsts.L_BOL_LOC, 0, "Ubicaciones") {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }
            
            @Override
            public void createGridColumns() {
                int col = 0;
                DGridColumnForm[] columns = new DGridColumnForm[8];

                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_INT_1B, "#");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "Tipo", 50);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, DGridConsts.COL_TITLE_NAME, 200);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "ID ubicación", 75);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DATE_DATETIME, "Fecha hr.");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "Origen", 200);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_2D, "Distancia km");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "Notas", 250);

                for (DGridColumnForm column : columns) {
                    moModel.getGridColumns().add(column);
                }
            }
        };
        
        return grid;
    }
    
    public static DGridPaneForm createGridMerchandises(final DGuiClient client, final DFormBol formBol) {
        DGridPaneForm grid = new DGridPaneForm(client, DModConsts.L_BOL_MERCH, 0, "Mercancías") {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }
            
            @Override
            public void createGridColumns() {
                int col = 0;
                DGridColumnForm[] columns = new DGridColumnForm[7];

                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_INT_1B, "#");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "Descripción", 200);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "ClaveProdServ", 75);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_6D, "Cantidad");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "Unidad", 200);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "ClaveUnidad", 75);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_3D, "Peso bruto kg");

                for (DGridColumnForm column : columns) {
                    moModel.getGridColumns().add(column);
                }
            }
        };
        
        return grid;
    }

    public static DGridPaneForm createGridMerchandisesMoves(final DGuiClient client, final DFormBol formBol) {
        DGridPaneForm grid = new DGridPaneForm(client, DModConsts.L_BOL_MERCH, 0, "Cantidad transporta") {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }
            
            @Override
            public void createGridColumns() {
                int col = 0;
                DGridColumnForm[] columns = new DGridColumnForm[6];

                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_INT_1B, "#");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, DGridConsts.COL_TITLE_NAME + " origen", 100);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "ID ubicación origen", 75);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, DGridConsts.COL_TITLE_NAME + " destino", 100);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "ID ubicación destino", 75);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_6D, "Cantidad");

                for (DGridColumnForm column : columns) {
                    moModel.getGridColumns().add(column);
                }
            }
        };
        
        return grid;
    }

    public static DGridPaneForm createGridTrucks(final DGuiClient client, final DFormBol formBol) {
        DGridPaneForm grid = new DGridPaneForm(client, DModConsts.L_BOL_MERCH, 0, "Transporte") {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }
            
            @Override
            public void createGridColumns() {
                int col = 0;
                DGridColumnForm[] columns = new DGridColumnForm[7];

                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_INT_1B, "#");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "Nombre", 200);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "Código", 100);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_DEC_AMT, "Peso bruto (ton)", 100);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "Placa", 100);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_INT_CAL_YEAR, "Modelo");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "Configuración", 500);

                for (DGridColumnForm column : columns) {
                    moModel.getGridColumns().add(column);
                }
            }
        };
        
        return grid;
    }

    public static DGridPaneForm createGridTrucksTrailers(final DGuiClient client, final DFormBol formBol) {
        DGridPaneForm grid = new DGridPaneForm(client, DModConsts.L_BOL_MERCH, 0, "Remolques") {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }
            
            @Override
            public void createGridColumns() {
                int col = 0;
                DGridColumnForm[] columns = new DGridColumnForm[3];

                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_INT_1B, "#");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "Placa", 100);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "Subipo", 200);

                for (DGridColumnForm column : columns) {
                    moModel.getGridColumns().add(column);
                }
            }
        };
        
        return grid;
    }

    public static DGridPaneForm createGridTptFigures(final DGuiClient client, final DFormBol formBol) {
        DGridPaneForm grid = new DGridPaneForm(client, DModConsts.L_BOL_MERCH, 0, "Figuras transporte") {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }
            
            @Override
            public void createGridColumns() {
                int col = 0;
                DGridColumnForm[] columns = new DGridColumnForm[6];

                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_INT_1B, "#");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "Nombre", 200);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "RFC", 100);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "ID fiscal", 100);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "Licencia", 100);
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "Tipo", 200);

                for (DGridColumnForm column : columns) {
                    moModel.getGridColumns().add(column);
                }
            }
        };
        
        return grid;
    }

    public static DGridPaneForm createGridTptFiguresTptParts(final DGuiClient client, final DFormBol formBol) {
        DGridPaneForm grid = new DGridPaneForm(client, DModConsts.L_BOL_MERCH, 0, "Partes transporte") {
            
            @Override
            public void initGrid() {
                setRowButtonsEnabled(false);
            }
            
            @Override
            public void createGridColumns() {
                int col = 0;
                DGridColumnForm[] columns = new DGridColumnForm[2];

                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_INT_1B, "#");
                columns[col++] = new DGridColumnForm(DGridConsts.COL_TYPE_TEXT, "Tipo", 200);

                for (DGridColumnForm column : columns) {
                    moModel.getGridColumns().add(column);
                }
            }
        };
        
        return grid;
    }
    
    /**
     * Get next code for desired table.
     * @param session GUI session.
     * @param table Desired table.
     * @param type Desired type. If not required, can be zero.
     * @return Next code.
     * @throws Exception 
     */
    public static int getNextCode(final DGuiSession session, final int table, final int type) throws Exception {
        int nextCode = 0;
        String sqlType = "";
        
        switch (table) {
            case DModConsts.LU_LOC:
                sqlType = " AND fk_loc_tp = " + type + " ";
                break;
            case DModConsts.LU_TRUCK:
            case DModConsts.LU_TPT_FIGURE:
                break;
            default:
                // nothing
        }
        
        String sql = "SELECT code "
                + "FROM " + DModConsts.TablesMap.get(table) + " "
                + "WHERE NOT b_del " + (!sqlType.isEmpty() ? sqlType : "")
                + "ORDER BY code DESC "
                + "LIMIT 1;";
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                nextCode = DLibUtils.parseInt(resultSet.getString(1));
            }
        }
        
        return ++nextCode;
    }
    
    /**
     * Validate that code does not duplicate for desired table.
     * @param session GUI session.
     * @param table Desired table.
     * @param code Code to validate.
     * @param registryPk Registry PK of code to validate.
     * @throws Exception 
     */
    public static void validateCode(final DGuiSession session, final int table, final String code, final int[] registryPk) throws Exception {
        DDbRegistry registry = session.getRegistry(table, null);
        
        String sql = "SELECT COUNT(*) "
                + registry.getSqlFromWhere(registryPk).replace("=", "<>")
                + "AND NOT b_del AND code = '" + code + "';";
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                if (resultSet.getInt(1) > 0) {
                    throw new Exception("El código '" + code + "' ya existe.");
                }
            }
        }
    }
    
    public static double getDistanceKm(final DGuiSession session, final DDbLocation source, final DDbLocation destiny) throws Exception {
        double distanceKm = 0;
        
        String sql = "SELECT dist_km "
                + "FROM " + DModConsts.TablesMap.get(DModConsts.LU_LOC_DIST) + " "
                + "WHERE id_loc_src = " + source.getPkLocationId() + " AND id_loc_des = " + destiny.getPkLocationId() + " AND NOT b_del;";
        try (ResultSet resultSet = session.getStatement().executeQuery(sql)) {
            if (resultSet.next()) {
                distanceKm = resultSet.getDouble("dist_km");
            }
        }
        
        return distanceKm;
    }
    
    public static ArrayList<DDbBolLocation> createTestingBolLocations(final DGuiSession session) {
        ArrayList<DDbBolLocation> bolLocations = new ArrayList<>();
        
        // location 1
        
        DDbLocation location1 = new DDbLocation();

        location1.setPkLocationId(0);
        location1.setCode("000001");
        //registry.setCodeSource(this.getCodeSource());
        //registry.setCodeDestiny(this.getCodeDestiny());
        location1.setName("CHAPULTEPEC");
        location1.setAddressStreet("BATALLA DE CASAMATA");
        location1.setAddressNumberExt("1189");
        location1.setAddressNumberInt("A");
        location1.setAddressDistrictCode("0324");
        location1.setAddressDistrictName("Chapultepec Sur");
        location1.setAddressLocalityCode("06");
        location1.setAddressLocalityName("Morelia");
        location1.setAddressReference("FRENTE A LAS CHANCAS DE TENIS");
        location1.setAddressCountyCode("053");
        location1.setAddressCountyName("Morelia");
        location1.setAddressStateCode("MIC");
        location1.setAddressStateName("Michoacán");
        location1.setAddressZipCode("58260");
        //location1.setUpdatable(...);
        //location1.setDisableable(...);
        //location1.setDeletable(...);
        //location1.setDisabled(...);
        //location1.setDeleted(...);
        //location1.setSystem(...);
        location1.setFkLocationTypeId(DModSysConsts.LS_LOC_TP_SRC);
        location1.setFkAddressCountryId(DModSysConsts.CS_CTY_MEX);
        //location1.setFkUserInsertId(...);
        //location1.setFkUserUpdateId(...);
        //location1.setTsUserInsert(...);
        //location1.setTsUserUpdate(...);
        
        location1.setDbmsLocationTypeCode((String) session.readField(DModConsts.LS_LOC_TP, new int[] { location1.getFkLocationTypeId() }, DDbRegistry.FIELD_CODE));
        location1.setDbmsLocationTypeName((String) session.readField(DModConsts.LS_LOC_TP, new int[] { location1.getFkLocationTypeId() }, DDbRegistry.FIELD_NAME));
        
        DDbBolLocation bolLocation1 = new DDbBolLocation();
        
        bolLocation1.setPkBolId(0);
        bolLocation1.setPkLocationId(1); // as temporary location ID
        //bolLocation1.setLocationId(...); // set in setOwnLocation()
        bolLocation1.setArrivalDepartureDatetime(new Date());
        bolLocation1.setDistanceKm(0.0);
        //bolLocation1.setAddressStreet(...); // set in setOwnLocation()
        //bolLocation1.setAddressNumberExt(...); // set in setOwnLocation()
        //bolLocation1.setAddressNumberInt(...); // set in setOwnLocation()
        //bolLocation1.setAddressDistrictCode(...); // set in setOwnLocation()
        //bolLocation1.setAddressDistrictName(...); // set in setOwnLocation()
        //bolLocation1.setAddressLocalityCode(...); // set in setOwnLocation()
        //bolLocation1.setAddressLocalityName(...); // set in setOwnLocation()
        //bolLocation1.setAddressReference(...); // set in setOwnLocation()
        //bolLocation1.setAddressCountyCode(...); // set in setOwnLocation()
        //bolLocation1.setAddressCountyName(...); // set in setOwnLocation()
        //bolLocation1.setAddressStateCode(...); // set in setOwnLocation()
        //bolLocation1.setAddressStateName(...); // set in setOwnLocation()
        //bolLocation1.setAddressZipCode(...); // set in setOwnLocation()
        bolLocation1.setNotes("SIN NOTAS, SIN COMENTARIOS");
        //bolLocation1.setFkLocationTypeId(...); // set in setOwnLocation()
        //bolLocation1.setFkLocationId(...); // set in setOwnLocation()
        //bolLocation1.setFkAddressCountryId(...); // set in setOwnLocation()
        bolLocation1.setFkSourceBolId_n(0);
        bolLocation1.setFkSourceLocationId_n(0); // sources have no sources
        
        bolLocation1.setOwnLocation(location1);
        bolLocation1.setOwnSourceLocation(null);
        
        bolLocation1.setBolSortingPos(1);
        
        bolLocation1.setTempBolLocationId(1);
        bolLocation1.setTempSourceBolLocationId(0);

        bolLocations.add(bolLocation1);
        
        // location 2
        
        DDbLocation location2 = new DDbLocation();

        location2.setPkLocationId(0);
        location2.setCode("000001");
        //registry.setCodeSource(this.getCodeSource());
        //registry.setCodeDestiny(this.getCodeDestiny());
        location2.setName("CD. INDUSTRIAL");
        location2.setAddressStreet("ORIENTE CUATRO");
        location2.setAddressNumberExt("1000");
        location2.setAddressNumberInt("");
        location2.setAddressDistrictCode("0249");
        location2.setAddressDistrictName("Ciudad Industrial");
        location2.setAddressLocalityCode("06");
        location2.setAddressLocalityName("Morelia");
        location2.setAddressReference("JUNTO A LA COCA-COLA");
        location2.setAddressCountyCode("053");
        location2.setAddressCountyName("Morelia");
        location2.setAddressStateCode("MIC");
        location2.setAddressStateName("Michoacán");
        location2.setAddressZipCode("58200");
        //location2.setUpdatable(...);
        //location2.setDisableable(...);
        //location2.setDeletable(...);
        //location2.setDisabled(...);
        //location2.setDeleted(...);
        //location2.setSystem(...);
        location2.setFkLocationTypeId(DModSysConsts.LS_LOC_TP_DES);
        location2.setFkAddressCountryId(DModSysConsts.CS_CTY_MEX);
        //location2.setFkUserInsertId(...);
        //location2.setFkUserUpdateId(...);
        //location2.setTsUserInsert(...);
        //location2.setTsUserUpdate(...);
        
        location2.setDbmsLocationTypeCode((String) session.readField(DModConsts.LS_LOC_TP, new int[] { location2.getFkLocationTypeId() }, DDbRegistry.FIELD_CODE));
        location2.setDbmsLocationTypeName((String) session.readField(DModConsts.LS_LOC_TP, new int[] { location2.getFkLocationTypeId() }, DDbRegistry.FIELD_NAME));
        
        DDbBolLocation bolLocation2 = new DDbBolLocation();
        
        bolLocation2.setPkBolId(0);
        bolLocation2.setPkLocationId(2); // as temporary location ID
        //bolLocation2.setLocationId(...); // set in setOwnLocation()
        bolLocation2.setArrivalDepartureDatetime(new Date());
        bolLocation2.setDistanceKm(7.5);
        //bolLocation2.setAddressStreet(...); // set in setOwnLocation()
        //bolLocation2.setAddressNumberExt(...); // set in setOwnLocation()
        //bolLocation2.setAddressNumberInt(...); // set in setOwnLocation()
        //bolLocation2.setAddressDistrictCode(...); // set in setOwnLocation()
        //bolLocation2.setAddressDistrictName(...); // set in setOwnLocation()
        //bolLocation2.setAddressLocalityCode(...); // set in setOwnLocation()
        //bolLocation2.setAddressLocalityName(...); // set in setOwnLocation()
        //bolLocation2.setAddressReference(...); // set in setOwnLocation()
        //bolLocation2.setAddressCountyCode(...); // set in setOwnLocation()
        //bolLocation2.setAddressCountyName(...); // set in setOwnLocation()
        //bolLocation2.setAddressStateCode(...); // set in setOwnLocation()
        //bolLocation2.setAddressStateName(...); // set in setOwnLocation()
        //bolLocation2.setAddressZipCode(...); // set in setOwnLocation()
        bolLocation2.setNotes("ESTA ES LA CENTRAL DE OPERACIONES");
        //bolLocation2.setFkLocationTypeId(...); // set in setOwnLocation()
        //bolLocation2.setFkLocationId(...); // set in setOwnLocation()
        //bolLocation2.setFkAddressCountryId(...); // set in setOwnLocation()
        bolLocation2.setFkSourceBolId_n(0);
        bolLocation2.setFkSourceLocationId_n(1); // destinies have sources
        
        bolLocation2.setOwnLocation(location2);
        bolLocation2.setOwnSourceLocation(location1);
        
        bolLocation2.setBolSortingPos(2);

        bolLocation2.setTempBolLocationId(2);
        bolLocation2.setTempSourceBolLocationId(1);
        
        bolLocations.add(bolLocation2);
        
        return bolLocations;
    }
    
    public static ArrayList<DDbBolMerchandise> createTestingBolMerchandises(final DGuiSession session, ArrayList<DDbBolLocation> bolLocations) {
        ArrayList<DDbBolMerchandise> bolMerchandises = new ArrayList<>();
        
        // merchandise 1
        
        DDbBolMerchandise bolMerchandise1 = new DDbBolMerchandise();

        bolMerchandise1.setPkBolId(0);
        bolMerchandise1.setPkMerchandiseId(0);
        bolMerchandise1.setDescriptionItem("ALUMNIO BOTE");
        bolMerchandise1.setDescriptionUnit("TONELADA MÉTRICA");
        bolMerchandise1.setQuantity(10.0);
        bolMerchandise1.setDimensions("1/20/300plg");
        bolMerchandise1.setHazardousMaterial(true);
        bolMerchandise1.setHazardousMaterialCode("1309");
        bolMerchandise1.setHazardousMaterialName("ALUMINIO EN POLVO, RECUBIERTO");
        bolMerchandise1.setPackagingCode("7L1");
        bolMerchandise1.setPackagingName("Bultos de Tela");
        bolMerchandise1.setWeightKg(10000.0);
        bolMerchandise1.setValue(25000.0);
        bolMerchandise1.setTariff("01019099");
        bolMerchandise1.setImportRequest("01  22  0333  000444");
        bolMerchandise1.setFkItemId(1);
        bolMerchandise1.setFkUnitId(5);
        bolMerchandise1.setFkCurrencyId(DModSysConsts.CS_CUR_MXN);
        
        DDbBolMerchandiseMove bolMerchandiseMove1 = new DDbBolMerchandiseMove();

        bolMerchandiseMove1.setPkBolId(0);
        bolMerchandiseMove1.setPkMerchandiseId(0);
        bolMerchandiseMove1.setPkMoveId(0);
        bolMerchandiseMove1.setQuantity(10.0);
        bolMerchandiseMove1.setFkSourceBolId(0);
        bolMerchandiseMove1.setFkSourceLocationId(1);
        bolMerchandiseMove1.setFkDestinyBolId(0);
        bolMerchandiseMove1.setFkDestinyLocationId(2);
        bolMerchandiseMove1.setFkTransportTypeId(DModSysConsts.LS_TPT_TP_TRUCK);

        bolMerchandiseMove1.setOwnSourceLocation(bolLocations.get(0).getOwnLocation());
        bolMerchandiseMove1.setOwnDestinyLocation(bolLocations.get(1).getOwnLocation());
        
        bolMerchandiseMove1.setBolSortingPos(1);
        
        bolMerchandiseMove1.setTempSourceBolLocationId(1);
        bolMerchandiseMove1.setTempDestinyBolLocationId(2);
        
        bolMerchandise1.getChildMoves().add(bolMerchandiseMove1);
        
        DDbItem item1 = (DDbItem) session.readRegistry(DModConsts.IU_ITM, new int[] { bolMerchandise1.getFkItemId() });
        bolMerchandise1.setOwnItem(item1);
        
        DDbUnit unit1 = (DDbUnit) session.readRegistry(DModConsts.IU_UNT, new int[] { bolMerchandise1.getFkUnitId()});
        bolMerchandise1.setOwnUnit(unit1);
        
        bolMerchandise1.setBolSortingPos(1);
        bolMerchandise1.computeQuantityMoved();
        
        bolMerchandises.add(bolMerchandise1);
        
        // merchandise 2
        
        DDbBolMerchandise bolMerchandise2 = new DDbBolMerchandise();

        bolMerchandise2.setPkBolId(0);
        bolMerchandise2.setPkMerchandiseId(0);
        bolMerchandise2.setDescriptionItem("COBRE DE PRIMERA");
        bolMerchandise2.setDescriptionUnit("TONELADA MÉTRICA");
        bolMerchandise2.setQuantity(1.0);
        bolMerchandise2.setDimensions("");
        bolMerchandise2.setHazardousMaterial(false);
        bolMerchandise2.setHazardousMaterialCode("");
        bolMerchandise2.setHazardousMaterialName("");
        bolMerchandise2.setPackagingCode("");
        bolMerchandise2.setPackagingName("");
        bolMerchandise2.setWeightKg(1000.0);
        bolMerchandise2.setValue(35000.0);
        bolMerchandise2.setTariff("");
        bolMerchandise2.setImportRequest("");
        bolMerchandise2.setFkItemId(5);
        bolMerchandise2.setFkUnitId(5);
        bolMerchandise2.setFkCurrencyId(DModSysConsts.CS_CUR_MXN);
        
        DDbBolMerchandiseMove bolMerchandiseMove2 = new DDbBolMerchandiseMove();

        bolMerchandiseMove2.setPkBolId(0);
        bolMerchandiseMove2.setPkMerchandiseId(0);
        bolMerchandiseMove2.setPkMoveId(0);
        bolMerchandiseMove2.setQuantity(1.0);
        bolMerchandiseMove2.setFkSourceBolId(0);
        bolMerchandiseMove2.setFkSourceLocationId(1);
        bolMerchandiseMove2.setFkDestinyBolId(0);
        bolMerchandiseMove2.setFkDestinyLocationId(2);
        bolMerchandiseMove2.setFkTransportTypeId(DModSysConsts.LS_TPT_TP_TRUCK);

        bolMerchandiseMove2.setOwnSourceLocation(bolLocations.get(0).getOwnLocation());
        bolMerchandiseMove2.setOwnDestinyLocation(bolLocations.get(1).getOwnLocation());
        
        bolMerchandiseMove2.setBolSortingPos(1);
        
        bolMerchandiseMove2.setTempSourceBolLocationId(1);
        bolMerchandiseMove2.setTempDestinyBolLocationId(2);
        
        bolMerchandise2.getChildMoves().add(bolMerchandiseMove2);
        
        DDbItem item2 = (DDbItem) session.readRegistry(DModConsts.IU_ITM, new int[] { bolMerchandise2.getFkItemId() });
        bolMerchandise2.setOwnItem(item2);
        
        DDbUnit unit2 = (DDbUnit) session.readRegistry(DModConsts.IU_UNT, new int[] { bolMerchandise2.getFkUnitId()});
        bolMerchandise2.setOwnUnit(unit2);
        
        bolMerchandise2.setBolSortingPos(2);
        bolMerchandise2.computeQuantityMoved();
        
        bolMerchandises.add(bolMerchandise2);

        return bolMerchandises;
    }
    
    public static ArrayList<DDbBolTruck> createTestingBolTrucks(final DGuiSession session) {
        ArrayList<DDbBolTruck> bolTrucks = new ArrayList<>();
        
        // truck 1
        
        DDbTruck truck1 = new DDbTruck();

        truck1.setPkTruckId(0);
        truck1.setCode("P1");
        truck1.setName("PALOMO");
        truck1.setPlate("PHB610B");
        truck1.setModel(2019);
        truck1.setTransportConfigCode("VL");
        truck1.setTransportConfigName("Vehículo ligero de carga (2 llantas en el eje delantero y 2 llantas en el eje trasero)");
        truck1.setPermissionTypeCode("TPAF02");
        truck1.setPermissionTypeName("Transporte privado de carga.");
        truck1.setPermissionNumber("PER001");
        truck1.setCivilInsurance("QUÁLITAS");
        truck1.setCivilPolicy("POL-Q-001");
        truck1.setEnvironmentInsurance("AXXA");
        truck1.setEnvironmentPolicy("POL-A-001");
        truck1.setCargoInsurance("SEGUROS MONTERREY");
        truck1.setCargoPolicy("POL-M-001");
        truck1.setPrime(25000.0);
        //truck1.setUpdatable(...);
        //truck1.setDisableable(...);
        //truck1.setDeletable(...);
        //truck1.setDisabled(...);
        //truck1.setDeleted(...);
        //truck1.setSystem(...);
        //truck1.setFkUserInsertId(...);
        //truck1.setFkUserUpdateId(...);
        //truck1.setTsUserInsert(...);
        //truck1.setTsUserUpdate(...);
        
        DDbBolTruck bolTruck1 = new DDbBolTruck();

        bolTruck1.setPkBolId(0);
        bolTruck1.setPkTruckId(0);
        //bolTruck1.setPlate(); // set in setOwnTruck()
        //bolTruck1.setModel(); // set in setOwnTruck()
        //bolTruck1.setTransportConfigCode(); // set in setOwnTruck()
        //bolTruck1.setTransportConfigName(); // set in setOwnTruck()
        //bolTruck1.setPermissionTypeCode(); // set in setOwnTruck()
        //bolTruck1.setPermissionTypeName(); // set in setOwnTruck()
        //bolTruck1.setPermissionNumber(); // set in setOwnTruck()
        //bolTruck1.setCivilInsurance(); // set in setOwnTruck()
        //bolTruck1.setCivilPolicy(); // set in setOwnTruck()
        //bolTruck1.setEnvironmentInsurance(); // set in setOwnTruck()
        //bolTruck1.setEnvironmentPolicy(); // set in setOwnTruck()
        //bolTruck1.setCargoInsurance(); // set in setOwnTruck()
        //bolTruck1.setCargoPolicy(); // set in setOwnTruck()
        //bolTruck1.setPrime(); // set in setOwnTruck()
        //bolTruck1.setFkTruckId(); // set in setOwnTruck()

        bolTruck1.setOwnTruck(truck1);
        
        bolTruck1.setBolSortingPos(1);
        
        // trailer 1
        
        DDbTrailer trailer1 = new DDbTrailer();

        trailer1.setPkTrailerId(0);
        trailer1.setCode("PHB620A");
        trailer1.setName("PHB620A");
        trailer1.setPlate("PHB620A");
        trailer1.setTrailerSubtypeCode("CTR002");
        trailer1.setTrailerSubtypeName("Caja");
        //trailer1.setUpdatable();
        //trailer1.setDisableable();
        //trailer1.setDeletable();
        //trailer1.setDisabled();
        //trailer1.setDeleted();
        //trailer1.setSystem();
        //trailer1.setFkUserInsertId();
        //trailer1.setFkUserUpdateId();
        //trailer1.setTsUserInsert();
        //trailer1.setTsUserUpdate();
        
        DDbBolTruckTrailer bolTruckTrailer1 = new DDbBolTruckTrailer();

        bolTruckTrailer1.setPkBolId(0);
        bolTruckTrailer1.setPkTruckId(0);
        bolTruckTrailer1.setPkTrailerId(0);
        //bolTruckTrailer1.setPlate(); // set in setOwnTrailer()
        //bolTruckTrailer1.setTrailerSubtypeCode(); // set in setOwnTrailer()
        //bolTruckTrailer1.setTrailerSubtypeName(); // set in setOwnTrailer()
        //bolTruckTrailer1.setFkTrailerId(); // set in setOwnTrailer()
        
        bolTruckTrailer1.setOwnTrailer(trailer1);
        
        bolTruckTrailer1.setBolSortingPos(1);
        
        bolTruck1.getChildTrailers().add(bolTruckTrailer1);
        
        bolTrucks.add(bolTruck1);
        
        return bolTrucks;
    }
    
    public static ArrayList<DDbBolTransportFigure> createTestingBolTptFigures(final DGuiSession session) {
        ArrayList<DDbBolTransportFigure> bolTptFigures = new ArrayList<>();
        
        // transport figure 1
        
        DDbTransportFigure tptFigure1 = new DDbTransportFigure();

        tptFigure1.setPkTransportFigureId(0);
        tptFigure1.setCode("JCP");
        tptFigure1.setName("JUAN COLORADO PÉREZ");
        tptFigure1.setFiscalId("XEXX010101000");
        tptFigure1.setForeignId("FISCALID-JCP");
        tptFigure1.setDriverLicense("DRVLIC-JCP");
        tptFigure1.setAddressStreet("STREET-AUT");
        tptFigure1.setAddressNumberExt("EXT.-AUT");
        tptFigure1.setAddressNumberInt("INT.-AUT");
        tptFigure1.setAddressDistrictCode("");
        tptFigure1.setAddressDistrictName("DISTRICT-AUT");
        tptFigure1.setAddressLocalityCode("");
        tptFigure1.setAddressLocalityName("LOCALITY-AUT");
        tptFigure1.setAddressReference("REFERENCE-AUT");
        tptFigure1.setAddressCountyCode("");
        tptFigure1.setAddressCountyName("COUNTY-AUT");
        tptFigure1.setAddressStateCode("");
        tptFigure1.setAddressStateName("STATE-AUT");
        tptFigure1.setAddressZipCode("01000");
        //tptFigure1.setUpdatable(...);
        //tptFigure1.setDisableable(...);
        //tptFigure1.setDeletable(...);
        //tptFigure1.setDisabled(...);
        //tptFigure1.setDeleted(...);
        //tptFigure1.setSystem(...);
        tptFigure1.setFkTransportFigureTypeId(DModSysConsts.LS_TPT_FIGURE_TP_DRIVER);
        tptFigure1.setFkFigureCountryId(16); // AUT - Austria
        tptFigure1.setFkAddressCountryId(16); // AUT - Austria
        //tptFigure1.setFkUserInsertId(...);
        //tptFigure1.setFkUserUpdateId(...);
        //tptFigure1.setTsUserInsert(...);
        //tptFigure1.setTsUserUpdate(...);
        
        DDbBolTransportFigure bolTptFigure1 = new DDbBolTransportFigure();

        bolTptFigure1.setPkBolId(0);
        bolTptFigure1.setPkTransportFigureId(0);
        //bolTptFigure1.setName(...); // set in setOwnTransportFigure()
        //bolTptFigure1.setFiscalId(...); // set in setOwnTransportFigure()
        //bolTptFigure1.setForeignId(...); // set in setOwnTransportFigure()
        //bolTptFigure1.setDriverLicense(...); // set in setOwnTransportFigure()
        //bolTptFigure1.setAddressStreet(...); // set in setOwnTransportFigure()
        //bolTptFigure1.setAddressNumberExt(...); // set in setOwnTransportFigure()
        //bolTptFigure1.setAddressNumberInt(...); // set in setOwnTransportFigure()
        //bolTptFigure1.setAddressDistrictCode(...); // set in setOwnTransportFigure()
        //bolTptFigure1.setAddressDistrictName(...); // set in setOwnTransportFigure()
        //bolTptFigure1.setAddressLocalityCode(...); // set in setOwnTransportFigure()
        //bolTptFigure1.setAddressLocalityName(...); // set in setOwnTransportFigure()
        //bolTptFigure1.setAddressReference(...); // set in setOwnTransportFigure()
        //bolTptFigure1.setAddressCountyCode(...); // set in setOwnTransportFigure()
        //bolTptFigure1.setAddressCountyName(...); // set in setOwnTransportFigure()
        //bolTptFigure1.setAddressStateCode(...); // set in setOwnTransportFigure()
        //bolTptFigure1.setAddressStateName(...); // set in setOwnTransportFigure()
        //bolTptFigure1.setAddressZipCode(...); // set in setOwnTransportFigure()
        //bolTptFigure1.setFkTransportFigureTypeId(...); // set in setOwnTransportFigure()
        //bolTptFigure1.setFkTransportFigureId(...); // set in setOwnTransportFigure()
        //bolTptFigure1.setFkFigureCountryId(...); // set in setOwnTransportFigure()
        //bolTptFigure1.setFkAddressCountryId(...); // set in setOwnTransportFigure()
        
        bolTptFigure1.setOwnTransportFigure(tptFigure1);
        
        bolTptFigure1.setBolSortingPos(1);
        
        bolTptFigure1.setDbmsTransportFigureTypeCode((String) session.readField(DModConsts.LS_TPT_FIGURE_TP, new int[] { bolTptFigure1.getFkTransportFigureTypeId() }, DDbRegistry.FIELD_CODE));
        bolTptFigure1.setDbmsTransportFigureTypeName((String) session.readField(DModConsts.LS_TPT_FIGURE_TP, new int[] { bolTptFigure1.getFkTransportFigureTypeId() }, DDbRegistry.FIELD_NAME));
        
        bolTptFigures.add(bolTptFigure1);
        
        // transport figure 2
        
        DDbTransportFigure tptFigure2 = new DDbTransportFigure();

        tptFigure2.setPkTransportFigureId(0);
        tptFigure2.setCode("JCL");
        tptFigure2.setName("JUAN CARRASQUEADO LÓPEZ");
        tptFigure2.setFiscalId("XEXX010101000");
        tptFigure2.setForeignId("FISCALID-JCL");
        tptFigure2.setDriverLicense("DRVLIC-JCL");
        tptFigure2.setAddressStreet("STREET-BGR");
        tptFigure2.setAddressNumberExt("EXT.-BGR");
        tptFigure2.setAddressNumberInt("INT.-BGR");
        tptFigure2.setAddressDistrictCode("");
        tptFigure2.setAddressDistrictName("DISTRICT-BGR");
        tptFigure2.setAddressLocalityCode("");
        tptFigure2.setAddressLocalityName("LOCALITY-BGR");
        tptFigure2.setAddressReference("REFERENCE-BGR");
        tptFigure2.setAddressCountyCode("");
        tptFigure2.setAddressCountyName("COUNTY-BGR");
        tptFigure2.setAddressStateCode("");
        tptFigure2.setAddressStateName("STATE-BGR");
        tptFigure2.setAddressZipCode("02000");
        //tptFigure2.setUpdatable(...);
        //tptFigure2.setDisableable(...);
        //tptFigure2.setDeletable(...);
        //tptFigure2.setDisabled(...);
        //tptFigure2.setDeleted(...);
        //tptFigure2.setSystem(...);
        tptFigure2.setFkTransportFigureTypeId(DModSysConsts.LS_TPT_FIGURE_TP_OWNER);
        tptFigure2.setFkFigureCountryId(35); // BGR - Bulgaria
        tptFigure2.setFkAddressCountryId(35); // BGR - Bulgaria
        //tptFigure2.setFkUserInsertId(...);
        //tptFigure2.setFkUserUpdateId(...);
        //tptFigure2.setTsUserInsert(...);
        //tptFigure2.setTsUserUpdate(...);
        
        DDbBolTransportFigure bolTptFigure2 = new DDbBolTransportFigure();

        bolTptFigure2.setPkBolId(0);
        bolTptFigure2.setPkTransportFigureId(0);
        //bolTptFigure2.setName(...); // set in setOwnTransportFigure()
        //bolTptFigure2.setFiscalId(...); // set in setOwnTransportFigure()
        //bolTptFigure2.setForeignId(...); // set in setOwnTransportFigure()
        //bolTptFigure2.setDriverLicense(...); // set in setOwnTransportFigure()
        //bolTptFigure2.setAddressStreet(...); // set in setOwnTransportFigure()
        //bolTptFigure2.setAddressNumberExt(...); // set in setOwnTransportFigure()
        //bolTptFigure2.setAddressNumberInt(...); // set in setOwnTransportFigure()
        //bolTptFigure2.setAddressDistrictCode(...); // set in setOwnTransportFigure()
        //bolTptFigure2.setAddressDistrictName(...); // set in setOwnTransportFigure()
        //bolTptFigure2.setAddressLocalityCode(...); // set in setOwnTransportFigure()
        //bolTptFigure2.setAddressLocalityName(...); // set in setOwnTransportFigure()
        //bolTptFigure2.setAddressReference(...); // set in setOwnTransportFigure()
        //bolTptFigure2.setAddressCountyCode(...); // set in setOwnTransportFigure()
        //bolTptFigure2.setAddressCountyName(...); // set in setOwnTransportFigure()
        //bolTptFigure2.setAddressStateCode(...); // set in setOwnTransportFigure()
        //bolTptFigure2.setAddressStateName(...); // set in setOwnTransportFigure()
        //bolTptFigure2.setAddressZipCode(...); // set in setOwnTransportFigure()
        //bolTptFigure2.setFkTransportFigureTypeId(...); // set in setOwnTransportFigure()
        //bolTptFigure2.setFkTransportFigureId(...); // set in setOwnTransportFigure()
        //bolTptFigure2.setFkFigureCountryId(...); // set in setOwnTransportFigure()
        //bolTptFigure2.setFkAddressCountryId(...); // set in setOwnTransportFigure()
        
        DDbSysTransportPartType sysTptPartType2 = (DDbSysTransportPartType) session.readRegistry(DModConsts.LS_TPT_PART_TP, new int[] { 4 }); // remolque
        
        DDbBolTransportFigureTransportPart tptFigureTptPart2 = new DDbBolTransportFigureTransportPart();

        tptFigureTptPart2.setPkBolId(0);
        tptFigureTptPart2.setPkTransportFigureId(0);
        tptFigureTptPart2.setPkTransportPartId(0);
        //tptFigureTptPart1.setFkTransportPartTypeId(...); set in setOwnTransportPartType()
        
        tptFigureTptPart2.setOwnSysTransportPartType(sysTptPartType2);
        
        tptFigureTptPart2.setBolSortingPos(1);
        
        bolTptFigure2.getChildTransportParts().add(tptFigureTptPart2);
        
        bolTptFigure2.setOwnTransportFigure(tptFigure2);
        
        bolTptFigure2.setBolSortingPos(1);
        
        bolTptFigure2.setDbmsTransportFigureTypeCode((String) session.readField(DModConsts.LS_TPT_FIGURE_TP, new int[] { bolTptFigure2.getFkTransportFigureTypeId() }, DDbRegistry.FIELD_CODE));
        bolTptFigure2.setDbmsTransportFigureTypeName((String) session.readField(DModConsts.LS_TPT_FIGURE_TP, new int[] { bolTptFigure2.getFkTransportFigureTypeId() }, DDbRegistry.FIELD_NAME));
        
        bolTptFigures.add(bolTptFigure2);
        
        return bolTptFigures;
    }
}
