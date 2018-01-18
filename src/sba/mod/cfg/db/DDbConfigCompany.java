/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.cfg.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Date;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistryUser;
import sba.lib.gui.DGuiConfigCompany;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public class DDbConfigCompany extends DDbRegistryUser implements DGuiConfigCompany {

    public static final int FIELD_VERSION = 1;

    protected int mnPkBizPartnerId;
    protected int mnVersion;
    protected Date mtVersionTs;
    protected boolean mbModuleConfiguration;
    protected boolean mbModuleFinance;
    protected boolean mbModulePurchases;
    protected boolean mbModuleSales;
    protected boolean mbModuleInventory;
    protected boolean mbModuleMarketing;
    protected boolean mbModulePointOfSale;
    protected boolean mbModuleServices;
    protected int mnAccountLevel;
    protected String msAccountFormat;
    protected boolean mbDateVendorApplying;
    protected boolean mbDateCustomerApplying;
    protected boolean mbCodeBizPartnerApplying;
    protected int mnLengthCodeBizPartner;
    protected String msFiscalIdCountry;
    protected String msFiscalIdForeign;
    protected String msFiscalIdentity;
    protected String msDefaultLocality;
    protected String msDefaultCounty;
    protected String msDefaultState;
    protected String msDefaultZipCode;
    protected boolean mbTelecommDeviceRequired;
    protected boolean mbItemCodeApplying;
    protected boolean mbItemLineApplying;
    protected boolean mbBrandApplying;
    protected boolean mbManufacturerApplying;
    protected boolean mbComponentApplying;
    protected boolean mbDepartmentApplying;
    protected boolean mbCodeItemFamilyApplying;
    protected boolean mbCodeItemGenusApplying;
    protected boolean mbCodeItemLineApplying;
    protected boolean mbCodeBrandApplying;
    protected boolean mbCodeManufacturerApplying;
    protected boolean mbCodeComponentApplying;
    protected boolean mbCodeDepartmentApplying;
    protected boolean mbCodeItemApplying;
    protected int mnLengthCodeItemFamily;
    protected int mnLengthCodeItemGenus;
    protected int mnLengthCodeItemLine;
    protected int mnLengthCodeBrand;
    protected int mnLengthCodeManufacturer;
    protected int mnLengthCodeComponent;
    protected int mnLengthCodeDepartment;
    protected int mnLengthCodeItem;
    protected boolean mbMeasurementLengthApplying;
    protected boolean mbMeasurementSurfaceApplying;
    protected boolean mbMeasurementVolumeApplying;
    protected boolean mbMeasurementMassApplying;
    protected boolean mbMeasurementTimeApplying;
    protected boolean mbWeightGrossApplying;
    protected boolean mbWeightDeliveryApplying;
    protected boolean mbUnitsVirtualApplying;
    protected boolean mbUnitsContainedApplying;
    protected boolean mbUnitsPackageApplying;
    protected boolean mbPriceSrpApplying;
    protected boolean mbPrice1Applying;
    protected boolean mbPrice2Applying;
    protected boolean mbPrice3Applying;
    protected boolean mbPrice4Applying;
    protected boolean mbPrice5Applying;
    protected boolean mbEdsApplying;
    protected boolean mbTaxIncluded;
    protected boolean mbImportDeclaration;
    protected boolean mbOrdersPurchaseApplying;
    protected boolean mbOrdersSaleApplying;
    protected boolean mbBlockingCreditPurchaseApplying;
    protected boolean mbBlockingDueDatePurchaseApplying;
    protected boolean mbBlockingCreditSaleApplying;
    protected boolean mbBlockingDueDateSaleApplying;
    protected boolean mbDpsDeletable;
    protected boolean mbCustomerFixedPrices;
    protected boolean mbCustomerSpecialPrices;
    protected boolean mbCustomerPromotionalPackages;
    protected double mdDelayInterestRate;
    protected int mnDecimalsQuantity;
    protected int mnDecimalsPriceUnitary;
    protected String msEdsEmsSmtpHost;
    protected int mnEdsEmsSmtpPort;
    protected boolean mbEdsEmsSmtpSslEnabled;
    protected String msEdsEmsFrom;
    protected String msEdsEmsName;
    protected String msEdsEmsPassword;
    protected String msEdsEmsSubject;
    protected String msEdsEmsBody;
    protected java.sql.Blob moImage_n;
    protected boolean mbDevelopment;
    /*
    protected boolean mbUpdatable;
    protected boolean mbDisableable;
    protected boolean mbDeletable;
    protected boolean mbDisabled;
    protected boolean mbDeleted;
    protected boolean mbSystem;
    */
    protected int mnFkCountryId;
    protected int mnFkCurrencyId;
    protected int mnFkAddressFormatTypeId;
    protected int mnFkTaxRegimeId;
    protected int mnFkAccountCashId_n;
    protected int mnFkAccountWarehouseId_n;
    protected int mnFkAccountVendorId_n;
    protected int mnFkAccountVendorAdvanceId_n;
    protected int mnFkAccountCustomerId_n;
    protected int mnFkAccountCustomerAdvanceId_n;
    protected int mnFkAccountCreditorId_n;
    protected int mnFkAccountDebtorId_n;
    protected int mnFkAccountPurchaseId_n;
    protected int mnFkAccountPurchaseIncIncrementId_n;
    protected int mnFkAccountPurchaseIncAdditionId_n;
    protected int mnFkAccountPurchaseDecDiscountId_n;
    protected int mnFkAccountPurchaseDecReturnId_n;
    protected int mnFkAccountSaleId_n;
    protected int mnFkAccountSaleIncIncrementId_n;
    protected int mnFkAccountSaleIncAdditionId_n;
    protected int mnFkAccountSaleDecDiscountId_n;
    protected int mnFkAccountSaleDecReturnId_n;
    protected int mnFkRecordTypeMoneyInId_n;
    protected int mnFkRecordTypeMoneyOutId_n;
    protected int mnFkRecordTypeGoodsInId_n;
    protected int mnFkRecordTypeGoodsOutId_n;
    protected int mnFkRecordTypeTrnPurchaseId_n;
    protected int mnFkRecordTypeTrnSaleId_n;
    protected int mnFkTaxGroupId_n;
    protected int mnFkExchangeRateAppTypePurchaseId;
    protected int mnFkExchangeRateAppTypeSaleId;
    protected int mnFkBlockingTypePurchaseOrderId;
    protected int mnFkBlockingTypePurchaseInvoiceId;
    protected int mnFkBlockingTypeSaleOrderId;
    protected int mnFkBlockingTypeSaleInvoiceId;
    protected int mnFkDnpTypeIogId;
    protected int mnFkDnpTypeIomId;
    protected int mnFkEdsEmsTypeId;
    /*
    protected int mnFkUserInsertId;
    protected int mnFkUserUpdateId;
    protected Date mtTsUserInsert;
    protected Date mtTsUserUpdate;
    */

    protected DecimalFormat moDecimalFormatQuantity;
    protected DecimalFormat moDecimalFormatPriceUnitary;
            
    public DDbConfigCompany() {
        super(DModConsts.CU_CFG_CO);
        initRegistry();
    }

    public void setPkBizPartnerId(int n) { mnPkBizPartnerId = n; }
    public void setVersion(int n) { mnVersion = n; }
    public void setVersionTs(Date t) { mtVersionTs = t; }
    public void setModuleConfiguration(boolean b) { mbModuleConfiguration = b; }
    public void setModuleFinance(boolean b) { mbModuleFinance = b; }
    public void setModulePurchases(boolean b) { mbModulePurchases = b; }
    public void setModuleSales(boolean b) { mbModuleSales = b; }
    public void setModuleInventory(boolean b) { mbModuleInventory = b; }
    public void setModuleMarketing(boolean b) { mbModuleMarketing = b; }
    public void setModulePointOfSale(boolean b) { mbModulePointOfSale = b; }
    public void setModuleServices(boolean b) { mbModuleServices = b; }
    public void setAccountLevel(int n) { mnAccountLevel = n; }
    public void setAccountFormat(String s) { msAccountFormat = s; }
    public void setDateVendorApplying(boolean b) { mbDateVendorApplying = b; }
    public void setDateCustomerApplying(boolean b) { mbDateCustomerApplying = b; }
    public void setCodeBizPartnerApplying(boolean b) { mbCodeBizPartnerApplying = b; }
    public void setLengthCodeBizPartner(int n) { mnLengthCodeBizPartner = n; }
    public void setFiscalIdCountry(String s) { msFiscalIdCountry = s; }
    public void setFiscalIdForeign(String s) { msFiscalIdForeign = s; }
    public void setFiscalIdentity(String s) { msFiscalIdentity = s; }
    public void setDefaultLocality(String s) { msDefaultLocality = s; }
    public void setDefaultCounty(String s) { msDefaultCounty = s; }
    public void setDefaultState(String s) { msDefaultState = s; }
    public void setDefaultZipCode(String s) { msDefaultZipCode = s; }
    public void setTelecommDeviceRequired(boolean b) { mbTelecommDeviceRequired = b; }
    public void setItemCodeApplying(boolean b) { mbItemCodeApplying = b; }
    public void setItemLineApplying(boolean b) { mbItemLineApplying = b; }
    public void setBrandApplying(boolean b) { mbBrandApplying = b; }
    public void setManufacturerApplying(boolean b) { mbManufacturerApplying = b; }
    public void setComponentApplying(boolean b) { mbComponentApplying = b; }
    public void setDepartmentApplying(boolean b) { mbDepartmentApplying = b; }
    public void setCodeItemFamilyApplying(boolean b) { mbCodeItemFamilyApplying = b; }
    public void setCodeItemGenusApplying(boolean b) { mbCodeItemGenusApplying = b; }
    public void setCodeItemLineApplying(boolean b) { mbCodeItemLineApplying = b; }
    public void setCodeBrandApplying(boolean b) { mbCodeBrandApplying = b; }
    public void setCodeManufacturerApplying(boolean b) { mbCodeManufacturerApplying = b; }
    public void setCodeComponentApplying(boolean b) { mbCodeComponentApplying = b; }
    public void setCodeDepartmentApplying(boolean b) { mbCodeDepartmentApplying = b; }
    public void setCodeItemApplying(boolean b) { mbCodeItemApplying = b; }
    public void setLengthCodeItemFamily(int n) { mnLengthCodeItemFamily = n; }
    public void setLengthCodeItemGenus(int n) { mnLengthCodeItemGenus = n; }
    public void setLengthCodeItemLine(int n) { mnLengthCodeItemLine = n; }
    public void setLengthCodeBrand(int n) { mnLengthCodeBrand = n; }
    public void setLengthCodeManufacturer(int n) { mnLengthCodeManufacturer = n; }
    public void setLengthCodeComponent(int n) { mnLengthCodeComponent = n; }
    public void setLengthCodeDepartment(int n) { mnLengthCodeDepartment = n; }
    public void setLengthCodeItem(int n) { mnLengthCodeItem = n; }
    public void setMeasurementLengthApplying(boolean b) { mbMeasurementLengthApplying = b; }
    public void setMeasurementSurfaceApplying(boolean b) { mbMeasurementSurfaceApplying = b; }
    public void setMeasurementVolumeApplying(boolean b) { mbMeasurementVolumeApplying = b; }
    public void setMeasurementMassApplying(boolean b) { mbMeasurementMassApplying = b; }
    public void setMeasurementTimeApplying(boolean b) { mbMeasurementTimeApplying = b; }
    public void setWeightGrossApplying(boolean b) { mbWeightGrossApplying = b; }
    public void setWeightDeliveryApplying(boolean b) { mbWeightDeliveryApplying = b; }
    public void setUnitsVirtualApplying(boolean b) { mbUnitsVirtualApplying = b; }
    public void setUnitsContainedApplying(boolean b) { mbUnitsContainedApplying = b; }
    public void setUnitsPackageApplying(boolean b) { mbUnitsPackageApplying = b; }
    public void setPriceSrpApplying(boolean b) { mbPriceSrpApplying = b; }
    public void setPrice1Applying(boolean b) { mbPrice1Applying = b; }
    public void setPrice2Applying(boolean b) { mbPrice2Applying = b; }
    public void setPrice3Applying(boolean b) { mbPrice3Applying = b; }
    public void setPrice4Applying(boolean b) { mbPrice4Applying = b; }
    public void setPrice5Applying(boolean b) { mbPrice5Applying = b; }
    public void setEdsApplying(boolean b) { mbEdsApplying = b; }
    public void setTaxIncluded(boolean b) { mbTaxIncluded = b; }
    public void setImportDeclaration(boolean b) { mbImportDeclaration = b; }
    public void setOrdersPurchaseApplying(boolean b) { mbOrdersPurchaseApplying = b; }
    public void setOrdersSaleApplying(boolean b) { mbOrdersSaleApplying = b; }
    public void setBlockingCreditPurchaseApplying(boolean b) { mbBlockingCreditPurchaseApplying = b; }
    public void setBlockingDueDatePurchaseApplying(boolean b) { mbBlockingDueDatePurchaseApplying = b; }
    public void setBlockingCreditSaleApplying(boolean b) { mbBlockingCreditSaleApplying = b; }
    public void setBlockingDueDateSaleApplying(boolean b) { mbBlockingDueDateSaleApplying = b; }
    public void setDpsDeletable(boolean b) { mbDpsDeletable = b; }
    public void setCustomerFixedPrices(boolean b) { mbCustomerFixedPrices = b; }
    public void setCustomerSpecialPrices(boolean b) { mbCustomerSpecialPrices = b; }
    public void setCustomerPromotionalPackages(boolean b) { mbCustomerPromotionalPackages = b; }
    public void setDelayInterestRate(double d) { mdDelayInterestRate = d; }
    public void setDecimalsQuantity(int n) { mnDecimalsQuantity = n; }
    public void setDecimalsPriceUnitary(int n) { mnDecimalsPriceUnitary = n; }
    public void setEdsEmsSmtpHost(String s) { msEdsEmsSmtpHost = s; }
    public void setEdsEmsSmtpPort(int n) { mnEdsEmsSmtpPort = n; }
    public void setEdsEmsSmtpSslEnabled(boolean b) { mbEdsEmsSmtpSslEnabled = b; }
    public void setEdsEmsFrom(String s) { msEdsEmsFrom = s; }
    public void setEdsEmsName(String s) { msEdsEmsName = s; }
    public void setEdsEmsPassword(String s) { msEdsEmsPassword = s; }
    public void setEdsEmsSubject(String s) { msEdsEmsSubject = s; }
    public void setEdsEmsBody(String s) { msEdsEmsBody = s; }
    public void setImage_n(java.sql.Blob o) { moImage_n = o; }
    public void setDevelopment(boolean b) { mbDevelopment = b; }
    public void setUpdatable(boolean b) { mbUpdatable = b; }
    public void setDisableable(boolean b) { mbDisableable = b; }
    public void setDeletable(boolean b) { mbDeletable = b; }
    public void setDisabled(boolean b) { mbDisabled = b; }
    public void setDeleted(boolean b) { mbDeleted = b; }
    public void setSystem(boolean b) { mbSystem = b; }
    public void setFkCountryId(int n) { mnFkCountryId = n; }
    public void setFkCurrencyId(int n) { mnFkCurrencyId = n; }
    public void setFkAddressFormatTypeId(int n) { mnFkAddressFormatTypeId = n; }
    public void setFkTaxRegimeId(int n) { mnFkTaxRegimeId = n; }
    public void setFkAccountCashId_n(int n) { mnFkAccountCashId_n = n; }
    public void setFkAccountWarehouseId_n(int n) { mnFkAccountWarehouseId_n = n; }
    public void setFkAccountVendorId_n(int n) { mnFkAccountVendorId_n = n; }
    public void setFkAccountVendorAdvanceId_n(int n) { mnFkAccountVendorAdvanceId_n = n; }
    public void setFkAccountCustomerId_n(int n) { mnFkAccountCustomerId_n = n; }
    public void setFkAccountCustomerAdvanceId_n(int n) { mnFkAccountCustomerAdvanceId_n = n; }
    public void setFkAccountCreditorId_n(int n) { mnFkAccountCreditorId_n = n; }
    public void setFkAccountDebtorId_n(int n) { mnFkAccountDebtorId_n = n; }
    public void setFkAccountPurchaseId_n(int n) { mnFkAccountPurchaseId_n = n; }
    public void setFkAccountPurchaseIncIncrementId_n(int n) { mnFkAccountPurchaseIncIncrementId_n = n; }
    public void setFkAccountPurchaseIncAdditionId_n(int n) { mnFkAccountPurchaseIncAdditionId_n = n; }
    public void setFkAccountPurchaseDecDiscountId_n(int n) { mnFkAccountPurchaseDecDiscountId_n = n; }
    public void setFkAccountPurchaseDecReturnId_n(int n) { mnFkAccountPurchaseDecReturnId_n = n; }
    public void setFkAccountSaleId_n(int n) { mnFkAccountSaleId_n = n; }
    public void setFkAccountSaleIncIncrementId_n(int n) { mnFkAccountSaleIncIncrementId_n = n; }
    public void setFkAccountSaleIncAdditionId_n(int n) { mnFkAccountSaleIncAdditionId_n = n; }
    public void setFkAccountSaleDecDiscountId_n(int n) { mnFkAccountSaleDecDiscountId_n = n; }
    public void setFkAccountSaleDecReturnId_n(int n) { mnFkAccountSaleDecReturnId_n = n; }
    public void setFkRecordTypeMoneyInId_n(int n) { mnFkRecordTypeMoneyInId_n = n; }
    public void setFkRecordTypeMoneyOutId_n(int n) { mnFkRecordTypeMoneyOutId_n = n; }
    public void setFkRecordTypeGoodsInId_n(int n) { mnFkRecordTypeGoodsInId_n = n; }
    public void setFkRecordTypeGoodsOutId_n(int n) { mnFkRecordTypeGoodsOutId_n = n; }
    public void setFkRecordTypeTrnPurchaseId_n(int n) { mnFkRecordTypeTrnPurchaseId_n = n; }
    public void setFkRecordTypeTrnSaleId_n(int n) { mnFkRecordTypeTrnSaleId_n = n; }
    public void setFkTaxGroupId_n(int n) { mnFkTaxGroupId_n = n; }
    public void setFkExchangeRateAppTypePurchaseId(int n) { mnFkExchangeRateAppTypePurchaseId = n; }
    public void setFkExchangeRateAppTypeSaleId(int n) { mnFkExchangeRateAppTypeSaleId = n; }
    public void setFkBlockingTypePurchaseOrderId(int n) { mnFkBlockingTypePurchaseOrderId = n; }
    public void setFkBlockingTypePurchaseInvoiceId(int n) { mnFkBlockingTypePurchaseInvoiceId = n; }
    public void setFkBlockingTypeSaleOrderId(int n) { mnFkBlockingTypeSaleOrderId = n; }
    public void setFkBlockingTypeSaleInvoiceId(int n) { mnFkBlockingTypeSaleInvoiceId = n; }
    public void setFkDnpTypeIogId(int n) { mnFkDnpTypeIogId = n; }
    public void setFkDnpTypeIomId(int n) { mnFkDnpTypeIomId = n; }
    public void setFkEdsEmsTypeId(int n) { mnFkEdsEmsTypeId = n; }
    public void setFkUserInsertId(int n) { mnFkUserInsertId = n; }
    public void setFkUserUpdateId(int n) { mnFkUserUpdateId = n; }
    public void setTsUserInsert(Date t) { mtTsUserInsert = t; }
    public void setTsUserUpdate(Date t) { mtTsUserUpdate = t; }

    public int getPkBizPartnerId() { return mnPkBizPartnerId; }
    public int getVersion() { return mnVersion; }
    public Date getVersionTs() { return mtVersionTs; }
    public boolean isModuleConfiguration() { return mbModuleConfiguration; }
    public boolean isModuleFinance() { return mbModuleFinance; }
    public boolean isModulePurchases() { return mbModulePurchases; }
    public boolean isModuleSales() { return mbModuleSales; }
    public boolean isModuleInventory() { return mbModuleInventory; }
    public boolean isModuleMarketing() { return mbModuleMarketing; }
    public boolean isModulePointOfSale() { return mbModulePointOfSale; }
    public boolean isModuleServices() { return mbModuleServices; }
    public int getAccountLevel() { return mnAccountLevel; }
    public String getAccountFormat() { return msAccountFormat; }
    public boolean isDateVendorApplying() { return mbDateVendorApplying; }
    public boolean isDateCustomerApplying() { return mbDateCustomerApplying; }
    public boolean isCodeBizPartnerApplying() { return mbCodeBizPartnerApplying; }
    public int getLengthCodeBizPartner() { return mnLengthCodeBizPartner; }
    public String getFiscalIdCountry() { return msFiscalIdCountry; }
    public String getFiscalIdForeign() { return msFiscalIdForeign; }
    public String getFiscalIdentity() { return msFiscalIdentity; }
    public String getDefaultLocality() { return msDefaultLocality; }
    public String getDefaultCounty() { return msDefaultCounty; }
    public String getDefaultState() { return msDefaultState; }
    public String getDefaultZipCode() { return msDefaultZipCode; }
    public boolean isTelecommDeviceRequired() { return mbTelecommDeviceRequired; }
    public boolean isItemCodeApplying() { return mbItemCodeApplying; }
    public boolean isItemLineApplying() { return mbItemLineApplying; }
    public boolean isBrandApplying() { return mbBrandApplying; }
    public boolean isManufacturerApplying() { return mbManufacturerApplying; }
    public boolean isComponentApplying() { return mbComponentApplying; }
    public boolean isDepartmentApplying() { return mbDepartmentApplying; }
    public boolean isCodeItemFamilyApplying() { return mbCodeItemFamilyApplying; }
    public boolean isCodeItemGenusApplying() { return mbCodeItemGenusApplying; }
    public boolean isCodeItemLineApplying() { return mbCodeItemLineApplying; }
    public boolean isCodeBrandApplying() { return mbCodeBrandApplying; }
    public boolean isCodeManufacturerApplying() { return mbCodeManufacturerApplying; }
    public boolean isCodeComponentApplying() { return mbCodeComponentApplying; }
    public boolean isCodeDepartmentApplying() { return mbCodeDepartmentApplying; }
    public boolean isCodeItemApplying() { return mbCodeItemApplying; }
    public int getLengthCodeItemFamily() { return mnLengthCodeItemFamily; }
    public int getLengthCodeItemGenus() { return mnLengthCodeItemGenus; }
    public int getLengthCodeItemLine() { return mnLengthCodeItemLine; }
    public int getLengthCodeBrand() { return mnLengthCodeBrand; }
    public int getLengthCodeManufacturer() { return mnLengthCodeManufacturer; }
    public int getLengthCodeComponent() { return mnLengthCodeComponent; }
    public int getLengthCodeDepartment() { return mnLengthCodeDepartment; }
    public int getLengthCodeItem() { return mnLengthCodeItem; }
    public boolean isMeasurementLengthApplying() { return mbMeasurementLengthApplying; }
    public boolean isMeasurementSurfaceApplying() { return mbMeasurementSurfaceApplying; }
    public boolean isMeasurementVolumeApplying() { return mbMeasurementVolumeApplying; }
    public boolean isMeasurementMassApplying() { return mbMeasurementMassApplying; }
    public boolean isMeasurementTimeApplying() { return mbMeasurementTimeApplying; }
    public boolean isWeightGrossApplying() { return mbWeightGrossApplying; }
    public boolean isWeightDeliveryApplying() { return mbWeightDeliveryApplying; }
    public boolean isUnitsVirtualApplying() { return mbUnitsVirtualApplying; }
    public boolean isUnitsContainedApplying() { return mbUnitsContainedApplying; }
    public boolean isUnitsPackageApplying() { return mbUnitsPackageApplying; }
    public boolean isPriceSrpApplying() { return mbPriceSrpApplying; }
    public boolean isPrice1Applying() { return mbPrice1Applying; }
    public boolean isPrice2Applying() { return mbPrice2Applying; }
    public boolean isPrice3Applying() { return mbPrice3Applying; }
    public boolean isPrice4Applying() { return mbPrice4Applying; }
    public boolean isPrice5Applying() { return mbPrice5Applying; }
    public boolean isEdsApplying() { return mbEdsApplying; }
    public boolean isTaxIncluded() { return mbTaxIncluded; }
    public boolean isImportDeclaration() { return mbImportDeclaration; }
    public boolean isOrdersPurchaseApplying() { return mbOrdersPurchaseApplying; }
    public boolean isOrdersSaleApplying() { return mbOrdersSaleApplying; }
    public boolean isBlockingCreditPurchaseApplying() { return mbBlockingCreditPurchaseApplying; }
    public boolean isBlockingDueDatePurchaseApplying() { return mbBlockingDueDatePurchaseApplying; }
    public boolean isBlockingCreditSaleApplying() { return mbBlockingCreditSaleApplying; }
    public boolean isBlockingDueDateSaleApplying() { return mbBlockingDueDateSaleApplying; }
    public boolean isDpsDeletable() { return mbDpsDeletable; }
    public boolean isCustomerFixedPrices() { return mbCustomerFixedPrices; }
    public boolean isCustomerSpecialPrices() { return mbCustomerSpecialPrices; }
    public boolean isCustomerPromotionalPackages() { return mbCustomerPromotionalPackages; }
    public double getDelayInterestRate() { return mdDelayInterestRate; }
    public int getDecimalsQuantity() { return mnDecimalsQuantity; }
    public int getDecimalsPriceUnitary() { return mnDecimalsPriceUnitary; }
    public String getEdsEmsSmtpHost() { return msEdsEmsSmtpHost; }
    public int getEdsEmsSmtpPort() { return mnEdsEmsSmtpPort; }
    public boolean isEdsEmsSmtpSslEnabled() { return mbEdsEmsSmtpSslEnabled; }
    public String getEdsEmsFrom() { return msEdsEmsFrom; }
    public String getEdsEmsName() { return msEdsEmsName; }
    public String getEdsEmsPassword() { return msEdsEmsPassword; }
    public String getEdsEmsSubject() { return msEdsEmsSubject; }
    public String getEdsEmsBody() { return msEdsEmsBody; }
    public java.sql.Blob getImage_n() { return moImage_n; }
    public boolean isDevelopment() { return mbDevelopment; }
    public boolean isUpdatable() { return mbUpdatable; }
    public boolean isDisableable() { return mbDisableable; }
    public boolean isDeletable() { return mbDeletable; }
    public boolean isDisabled() { return mbDisabled; }
    public boolean isDeleted() { return mbDeleted; }
    public boolean isSystem() { return mbSystem; }
    public int getFkCountryId() { return mnFkCountryId; }
    public int getFkCurrencyId() { return mnFkCurrencyId; }
    public int getFkAddressFormatTypeId() { return mnFkAddressFormatTypeId; }
    public int getFkTaxRegimeId() { return mnFkTaxRegimeId; }
    public int getFkAccountCashId_n() { return mnFkAccountCashId_n; }
    public int getFkAccountWarehouseId_n() { return mnFkAccountWarehouseId_n; }
    public int getFkAccountVendorId_n() { return mnFkAccountVendorId_n; }
    public int getFkAccountVendorAdvanceId_n() { return mnFkAccountVendorAdvanceId_n; }
    public int getFkAccountCustomerId_n() { return mnFkAccountCustomerId_n; }
    public int getFkAccountCustomerAdvanceId_n() { return mnFkAccountCustomerAdvanceId_n; }
    public int getFkAccountCreditorId_n() { return mnFkAccountCreditorId_n; }
    public int getFkAccountDebtorId_n() { return mnFkAccountDebtorId_n; }
    public int getFkAccountPurchaseId_n() { return mnFkAccountPurchaseId_n; }
    public int getFkAccountPurchaseIncIncrementId_n() { return mnFkAccountPurchaseIncIncrementId_n; }
    public int getFkAccountPurchaseIncAdditionId_n() { return mnFkAccountPurchaseIncAdditionId_n; }
    public int getFkAccountPurchaseDecDiscountId_n() { return mnFkAccountPurchaseDecDiscountId_n; }
    public int getFkAccountPurchaseDecReturnId_n() { return mnFkAccountPurchaseDecReturnId_n; }
    public int getFkAccountSaleId_n() { return mnFkAccountSaleId_n; }
    public int getFkAccountSaleIncIncrementId_n() { return mnFkAccountSaleIncIncrementId_n; }
    public int getFkAccountSaleIncAdditionId_n() { return mnFkAccountSaleIncAdditionId_n; }
    public int getFkAccountSaleDecDiscountId_n() { return mnFkAccountSaleDecDiscountId_n; }
    public int getFkAccountSaleDecReturnId_n() { return mnFkAccountSaleDecReturnId_n; }
    public int getFkRecordTypeMoneyInId_n() { return mnFkRecordTypeMoneyInId_n; }
    public int getFkRecordTypeMoneyOutId_n() { return mnFkRecordTypeMoneyOutId_n; }
    public int getFkRecordTypeGoodsInId_n() { return mnFkRecordTypeGoodsInId_n; }
    public int getFkRecordTypeGoodsOutId_n() { return mnFkRecordTypeGoodsOutId_n; }
    public int getFkRecordTypeTrnPurchaseId_n() { return mnFkRecordTypeTrnPurchaseId_n; }
    public int getFkRecordTypeTrnSaleId_n() { return mnFkRecordTypeTrnSaleId_n; }
    public int getFkTaxGroupId_n() { return mnFkTaxGroupId_n; }
    public int getFkExchangeRateAppTypePurchaseId() { return mnFkExchangeRateAppTypePurchaseId; }
    public int getFkExchangeRateAppTypeSaleId() { return mnFkExchangeRateAppTypeSaleId; }
    public int getFkBlockingTypePurchaseOrderId() { return mnFkBlockingTypePurchaseOrderId; }
    public int getFkBlockingTypePurchaseInvoiceId() { return mnFkBlockingTypePurchaseInvoiceId; }
    public int getFkBlockingTypeSaleOrderId() { return mnFkBlockingTypeSaleOrderId; }
    public int getFkBlockingTypeSaleInvoiceId() { return mnFkBlockingTypeSaleInvoiceId; }
    public int getFkDnpTypeIogId() { return mnFkDnpTypeIogId; }
    public int getFkDnpTypeIomId() { return mnFkDnpTypeIomId; }
    public int getFkEdsEmsTypeId() { return mnFkEdsEmsTypeId; }
    public int getFkUserInsertId() { return mnFkUserInsertId; }
    public int getFkUserUpdateId() { return mnFkUserUpdateId; }
    public Date getTsUserInsert() { return mtTsUserInsert; }
    public Date getTsUserUpdate() { return mtTsUserUpdate; }

    public void setDecimalFormatQuantity(DecimalFormat o) { moDecimalFormatQuantity = o; }
    public void setDecimalFormatPriceUnitary(DecimalFormat o) { moDecimalFormatPriceUnitary = o; }

    public DecimalFormat getDecimalFormatQuantity() { return moDecimalFormatQuantity; }
    public DecimalFormat getDecimalFormatPriceUnitary() { return moDecimalFormatPriceUnitary; }
    
    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkBizPartnerId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkBizPartnerId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkBizPartnerId = 0;
        mnVersion = 0;
        mtVersionTs = null;
        mbModuleConfiguration = false;
        mbModuleFinance = false;
        mbModulePurchases = false;
        mbModuleSales = false;
        mbModuleInventory = false;
        mbModuleMarketing = false;
        mbModulePointOfSale = false;
        mbModuleServices = false;
        mnAccountLevel = 0;
        msAccountFormat = "";
        mbDateVendorApplying = false;
        mbDateCustomerApplying = false;
        mbCodeBizPartnerApplying = false;
        mnLengthCodeBizPartner = 0;
        msFiscalIdCountry = "";
        msFiscalIdForeign = "";
        msFiscalIdentity = "";
        msDefaultLocality = "";
        msDefaultCounty = "";
        msDefaultState = "";
        msDefaultZipCode = "";
        mbTelecommDeviceRequired = false;
        mbItemCodeApplying = false;
        mbItemLineApplying = false;
        mbBrandApplying = false;
        mbManufacturerApplying = false;
        mbComponentApplying = false;
        mbDepartmentApplying = false;
        mbCodeItemFamilyApplying = false;
        mbCodeItemGenusApplying = false;
        mbCodeItemLineApplying = false;
        mbCodeBrandApplying = false;
        mbCodeManufacturerApplying = false;
        mbCodeComponentApplying = false;
        mbCodeDepartmentApplying = false;
        mbCodeItemApplying = false;
        mnLengthCodeItemFamily = 0;
        mnLengthCodeItemGenus = 0;
        mnLengthCodeItemLine = 0;
        mnLengthCodeBrand = 0;
        mnLengthCodeManufacturer = 0;
        mnLengthCodeComponent = 0;
        mnLengthCodeDepartment = 0;
        mnLengthCodeItem = 0;
        mbMeasurementLengthApplying = false;
        mbMeasurementSurfaceApplying = false;
        mbMeasurementVolumeApplying = false;
        mbMeasurementMassApplying = false;
        mbMeasurementTimeApplying = false;
        mbWeightGrossApplying = false;
        mbWeightDeliveryApplying = false;
        mbUnitsVirtualApplying = false;
        mbUnitsContainedApplying = false;
        mbUnitsPackageApplying = false;
        mbPriceSrpApplying = false;
        mbPrice1Applying = false;
        mbPrice2Applying = false;
        mbPrice3Applying = false;
        mbPrice4Applying = false;
        mbPrice5Applying = false;
        mbEdsApplying = false;
        mbTaxIncluded = false;
        mbImportDeclaration = false;
        mbOrdersPurchaseApplying = false;
        mbOrdersSaleApplying = false;
        mbBlockingCreditPurchaseApplying = false;
        mbBlockingDueDatePurchaseApplying = false;
        mbBlockingCreditSaleApplying = false;
        mbBlockingDueDateSaleApplying = false;
        mbDpsDeletable = false;
        mbCustomerFixedPrices = false;
        mbCustomerSpecialPrices = false;
        mbCustomerPromotionalPackages = false;
        mdDelayInterestRate = 0;
        mnDecimalsQuantity = 0;
        mnDecimalsPriceUnitary = 0;
        msEdsEmsSmtpHost = "";
        mnEdsEmsSmtpPort = 0;
        mbEdsEmsSmtpSslEnabled = false;
        msEdsEmsFrom = "";
        msEdsEmsName = "";
        msEdsEmsPassword = "";
        msEdsEmsSubject = "";
        msEdsEmsBody = "";
        moImage_n = null;
        mbDevelopment = false;
        mbUpdatable = false;
        mbDisableable = false;
        mbDeletable = false;
        mbDisabled = false;
        mbDeleted = false;
        mbSystem = false;
        mnFkCountryId = 0;
        mnFkCurrencyId = 0;
        mnFkAddressFormatTypeId = 0;
        mnFkTaxRegimeId = 0;
        mnFkAccountCashId_n = 0;
        mnFkAccountWarehouseId_n = 0;
        mnFkAccountVendorId_n = 0;
        mnFkAccountVendorAdvanceId_n = 0;
        mnFkAccountCustomerId_n = 0;
        mnFkAccountCustomerAdvanceId_n = 0;
        mnFkAccountCreditorId_n = 0;
        mnFkAccountDebtorId_n = 0;
        mnFkAccountPurchaseId_n = 0;
        mnFkAccountPurchaseIncIncrementId_n = 0;
        mnFkAccountPurchaseIncAdditionId_n = 0;
        mnFkAccountPurchaseDecDiscountId_n = 0;
        mnFkAccountPurchaseDecReturnId_n = 0;
        mnFkAccountSaleId_n = 0;
        mnFkAccountSaleIncIncrementId_n = 0;
        mnFkAccountSaleIncAdditionId_n = 0;
        mnFkAccountSaleDecDiscountId_n = 0;
        mnFkAccountSaleDecReturnId_n = 0;
        mnFkRecordTypeMoneyInId_n = 0;
        mnFkRecordTypeMoneyOutId_n = 0;
        mnFkRecordTypeGoodsInId_n = 0;
        mnFkRecordTypeGoodsOutId_n = 0;
        mnFkRecordTypeTrnPurchaseId_n = 0;
        mnFkRecordTypeTrnSaleId_n = 0;
        mnFkTaxGroupId_n = 0;
        mnFkExchangeRateAppTypePurchaseId = 0;
        mnFkExchangeRateAppTypeSaleId = 0;
        mnFkBlockingTypePurchaseOrderId = 0;
        mnFkBlockingTypePurchaseInvoiceId = 0;
        mnFkBlockingTypeSaleOrderId = 0;
        mnFkBlockingTypeSaleInvoiceId = 0;
        mnFkDnpTypeIogId = 0;
        mnFkDnpTypeIomId = 0;
        mnFkEdsEmsTypeId = 0;
        mnFkUserInsertId = 0;
        mnFkUserUpdateId = 0;
        mtTsUserInsert = null;
        mtTsUserUpdate = null;
        
        moDecimalFormatQuantity = null;
        moDecimalFormatPriceUnitary = null;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_bpr = " + mnPkBizPartnerId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_bpr = " + pk[0] + " ";
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
            mnPkBizPartnerId = resultSet.getInt("id_bpr");
            mnVersion = resultSet.getInt("ver");
            mtVersionTs = resultSet.getTimestamp("ver_ts");
            mbModuleConfiguration = resultSet.getBoolean("b_mod_cfg");
            mbModuleFinance = resultSet.getBoolean("b_mod_fin");
            mbModulePurchases = resultSet.getBoolean("b_mod_pur");
            mbModuleSales = resultSet.getBoolean("b_mod_sal");
            mbModuleInventory = resultSet.getBoolean("b_mod_inv");
            mbModuleMarketing = resultSet.getBoolean("b_mod_mkt");
            mbModulePointOfSale = resultSet.getBoolean("b_mod_pos");
            mbModuleServices = resultSet.getBoolean("b_mod_srv");
            mnAccountLevel = resultSet.getInt("acc_lev");
            msAccountFormat = resultSet.getString("acc_fmt");
            mbDateVendorApplying = resultSet.getBoolean("b_dt_ven");
            mbDateCustomerApplying = resultSet.getBoolean("b_dt_cus");
            mbCodeBizPartnerApplying = resultSet.getBoolean("b_code_bpr");
            mnLengthCodeBizPartner = resultSet.getInt("len_code_bpr");
            msFiscalIdCountry = resultSet.getString("fis_id_cty");
            msFiscalIdForeign = resultSet.getString("fis_id_frg");
            msFiscalIdentity = resultSet.getString("fis_idy");
            msDefaultLocality = resultSet.getString("def_loc");
            msDefaultCounty = resultSet.getString("def_cou");
            msDefaultState = resultSet.getString("def_ste");
            msDefaultZipCode = resultSet.getString("def_zip");
            mbTelecommDeviceRequired = resultSet.getBoolean("b_tcd_req");
            mbItemCodeApplying = resultSet.getBoolean("b_itm_code");
            mbItemLineApplying = resultSet.getBoolean("b_itm_lin");
            mbBrandApplying = resultSet.getBoolean("b_itm_brd");
            mbManufacturerApplying = resultSet.getBoolean("b_itm_mfr");
            mbComponentApplying = resultSet.getBoolean("b_itm_cmp");
            mbDepartmentApplying = resultSet.getBoolean("b_itm_dep");
            mbCodeItemFamilyApplying = resultSet.getBoolean("b_code_fam");
            mbCodeItemGenusApplying = resultSet.getBoolean("b_code_gen");
            mbCodeItemLineApplying = resultSet.getBoolean("b_code_lin");
            mbCodeBrandApplying = resultSet.getBoolean("b_code_brd");
            mbCodeManufacturerApplying = resultSet.getBoolean("b_code_mfr");
            mbCodeComponentApplying = resultSet.getBoolean("b_code_cmp");
            mbCodeDepartmentApplying = resultSet.getBoolean("b_code_dep");
            mbCodeItemApplying = resultSet.getBoolean("b_code_itm");
            mnLengthCodeItemFamily = resultSet.getInt("len_code_fam");
            mnLengthCodeItemGenus = resultSet.getInt("len_code_gen");
            mnLengthCodeItemLine = resultSet.getInt("len_code_lin");
            mnLengthCodeBrand = resultSet.getInt("len_code_brd");
            mnLengthCodeManufacturer = resultSet.getInt("len_code_mfr");
            mnLengthCodeComponent = resultSet.getInt("len_code_cmp");
            mnLengthCodeDepartment = resultSet.getInt("len_code_dep");
            mnLengthCodeItem = resultSet.getInt("len_code_itm");
            mbMeasurementLengthApplying = resultSet.getBoolean("b_mst_len");
            mbMeasurementSurfaceApplying = resultSet.getBoolean("b_mst_sur");
            mbMeasurementVolumeApplying = resultSet.getBoolean("b_mst_vol");
            mbMeasurementMassApplying = resultSet.getBoolean("b_mst_mas");
            mbMeasurementTimeApplying = resultSet.getBoolean("b_mst_tme");
            mbWeightGrossApplying = resultSet.getBoolean("b_wgt_grs");
            mbWeightDeliveryApplying = resultSet.getBoolean("b_wgt_del");
            mbUnitsVirtualApplying = resultSet.getBoolean("b_unt_vir");
            mbUnitsContainedApplying = resultSet.getBoolean("b_unt_con");
            mbUnitsPackageApplying = resultSet.getBoolean("b_unt_pac");
            mbPriceSrpApplying = resultSet.getBoolean("b_prc_srp");
            mbPrice1Applying = resultSet.getBoolean("b_prc_1");
            mbPrice2Applying = resultSet.getBoolean("b_prc_2");
            mbPrice3Applying = resultSet.getBoolean("b_prc_3");
            mbPrice4Applying = resultSet.getBoolean("b_prc_4");
            mbPrice5Applying = resultSet.getBoolean("b_prc_5");
            mbEdsApplying = resultSet.getBoolean("b_eds");
            mbTaxIncluded = resultSet.getBoolean("b_tax_inc");
            mbImportDeclaration = resultSet.getBoolean("b_imp_dec");
            mbOrdersPurchaseApplying = resultSet.getBoolean("b_ord_pur");
            mbOrdersSaleApplying = resultSet.getBoolean("b_ord_sal");
            mbBlockingCreditPurchaseApplying = resultSet.getBoolean("b_blk_cdt_pur");
            mbBlockingDueDatePurchaseApplying = resultSet.getBoolean("b_blk_due_pur");
            mbBlockingCreditSaleApplying = resultSet.getBoolean("b_blk_cdt_sal");
            mbBlockingDueDateSaleApplying = resultSet.getBoolean("b_blk_due_sal");
            mbDpsDeletable = resultSet.getBoolean("b_dps_del");
            mbCustomerFixedPrices = resultSet.getBoolean("b_cus_fix");
            mbCustomerSpecialPrices = resultSet.getBoolean("b_cus_spe");
            mbCustomerPromotionalPackages = resultSet.getBoolean("b_cus_prm");
            mdDelayInterestRate = resultSet.getDouble("int_rat");
            mnDecimalsQuantity = resultSet.getInt("decs_qty");
            mnDecimalsPriceUnitary = resultSet.getInt("decs_prc_unt");
            msEdsEmsSmtpHost = resultSet.getString("eds_ems_smtp_host");
            mnEdsEmsSmtpPort = resultSet.getInt("eds_ems_smtp_port");
            mbEdsEmsSmtpSslEnabled = resultSet.getBoolean("eds_ems_smtp_ssl");
            msEdsEmsFrom = resultSet.getString("eds_ems_from");
            msEdsEmsName = resultSet.getString("eds_ems_name");
            msEdsEmsPassword = resultSet.getString("eds_ems_pswd");
            msEdsEmsSubject = resultSet.getString("eds_ems_subj");
            msEdsEmsBody = resultSet.getString("eds_ems_body");
            moImage_n = resultSet.getBlob("img_n");
            mbDevelopment = resultSet.getBoolean("b_dev");
            mbUpdatable = resultSet.getBoolean("b_can_upd");
            mbDisableable = resultSet.getBoolean("b_can_dis");
            mbDeletable = resultSet.getBoolean("b_can_del");
            mbDisabled = resultSet.getBoolean("b_dis");
            mbDeleted = resultSet.getBoolean("b_del");
            mbSystem = resultSet.getBoolean("b_sys");
            mnFkCountryId = resultSet.getInt("fk_cty");
            mnFkCurrencyId = resultSet.getInt("fk_cur");
            mnFkAddressFormatTypeId = resultSet.getInt("fk_baf_tp");
            mnFkTaxRegimeId = resultSet.getInt("fk_tax_reg");
            mnFkAccountCashId_n = resultSet.getInt("fk_acc_csh_n");
            mnFkAccountWarehouseId_n = resultSet.getInt("fk_acc_wah_n");
            mnFkAccountVendorId_n = resultSet.getInt("fk_acc_ven_n");
            mnFkAccountVendorAdvanceId_n = resultSet.getInt("fk_acc_ven_adv_n");
            mnFkAccountCustomerId_n = resultSet.getInt("fk_acc_cus_n");
            mnFkAccountCustomerAdvanceId_n = resultSet.getInt("fk_acc_cus_adv_n");
            mnFkAccountCreditorId_n = resultSet.getInt("fk_acc_cdr_n");
            mnFkAccountDebtorId_n = resultSet.getInt("fk_acc_dbr_n");
            mnFkAccountPurchaseId_n = resultSet.getInt("fk_acc_pur_n");
            mnFkAccountPurchaseIncIncrementId_n = resultSet.getInt("fk_acc_pur_inc_inc_n");
            mnFkAccountPurchaseIncAdditionId_n = resultSet.getInt("fk_acc_pur_inc_add_n");
            mnFkAccountPurchaseDecDiscountId_n = resultSet.getInt("fk_acc_pur_dec_dis_n");
            mnFkAccountPurchaseDecReturnId_n = resultSet.getInt("fk_acc_pur_dec_ret_n");
            mnFkAccountSaleId_n = resultSet.getInt("fk_acc_sal_n");
            mnFkAccountSaleIncIncrementId_n = resultSet.getInt("fk_acc_sal_inc_inc_n");
            mnFkAccountSaleIncAdditionId_n = resultSet.getInt("fk_acc_sal_inc_add_n");
            mnFkAccountSaleDecDiscountId_n = resultSet.getInt("fk_acc_sal_dec_dis_n");
            mnFkAccountSaleDecReturnId_n = resultSet.getInt("fk_acc_sal_dec_ret_n");
            mnFkRecordTypeMoneyInId_n = resultSet.getInt("fk_rec_tp_csh_in_n");
            mnFkRecordTypeMoneyOutId_n = resultSet.getInt("fk_rec_tp_csh_out_n");
            mnFkRecordTypeGoodsInId_n = resultSet.getInt("fk_rec_tp_gds_in_n");
            mnFkRecordTypeGoodsOutId_n = resultSet.getInt("fk_rec_tp_gds_out_n");
            mnFkRecordTypeTrnPurchaseId_n = resultSet.getInt("fk_rec_tp_trn_pur_n");
            mnFkRecordTypeTrnSaleId_n = resultSet.getInt("fk_rec_tp_trn_sal_n");
            mnFkTaxGroupId_n = resultSet.getInt("fk_tax_grp_n");
            mnFkExchangeRateAppTypePurchaseId = resultSet.getInt("fk_exr_app_pur");
            mnFkExchangeRateAppTypeSaleId = resultSet.getInt("fk_exr_app_sal");
            mnFkBlockingTypePurchaseOrderId = resultSet.getInt("fk_blk_tp_pur_ord");
            mnFkBlockingTypePurchaseInvoiceId = resultSet.getInt("fk_blk_tp_pur_inv");
            mnFkBlockingTypeSaleOrderId = resultSet.getInt("fk_blk_tp_sal_ord");
            mnFkBlockingTypeSaleInvoiceId = resultSet.getInt("fk_blk_tp_sal_inv");
            mnFkDnpTypeIogId = resultSet.getInt("fk_dnp_tp_iog");
            mnFkDnpTypeIomId = resultSet.getInt("fk_dnp_tp_iom");
            mnFkEdsEmsTypeId = resultSet.getInt("fk_eds_ems_tp");
            mnFkUserInsertId = resultSet.getInt("fk_usr_ins");
            mnFkUserUpdateId = resultSet.getInt("fk_usr_upd");
            mtTsUserInsert = resultSet.getTimestamp("ts_usr_ins");
            mtTsUserUpdate = resultSet.getTimestamp("ts_usr_upd");

            moDecimalFormatQuantity = new DecimalFormat("#,##0" + (mnDecimalsQuantity == 0 ? "" : "." + DLibUtils.textRepeat("0", mnDecimalsQuantity)));
            moDecimalFormatPriceUnitary = new DecimalFormat("#,##0" + (mnDecimalsPriceUnitary == 0 ? "" : "." + DLibUtils.textRepeat("0", mnDecimalsPriceUnitary)));
            
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
            mbUpdatable = true;
            mbDisableable = false;
            mbDeletable = false;
            mbDisabled = false;
            mbDeleted = false;
            //mbSystem = false;
            mnFkUserInsertId = session.getUser().getPkUserId();
            mnFkUserUpdateId = DUtilConsts.USR_NA_ID;

            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkBizPartnerId + ", " +
                    mnVersion + ", " +
                    "NOW()" + ", " +
                    (mbModuleConfiguration ? 1 : 0) + ", " +
                    (mbModuleFinance ? 1 : 0) + ", " +
                    (mbModulePurchases ? 1 : 0) + ", " +
                    (mbModuleSales ? 1 : 0) + ", " +
                    (mbModuleInventory ? 1 : 0) + ", " +
                    (mbModuleMarketing ? 1 : 0) + ", " +
                    (mbModulePointOfSale ? 1 : 0) + ", " +
                    (mbModuleServices ? 1 : 0) + ", " +
                    mnAccountLevel + ", " +
                    "'" + msAccountFormat + "', " +
                    (mbDateVendorApplying ? 1 : 0) + ", " +
                    (mbDateCustomerApplying ? 1 : 0) + ", " +
                    (mbCodeBizPartnerApplying ? 1 : 0) + ", " +
                    mnLengthCodeBizPartner + ", " +
                    "'" + msFiscalIdCountry + "', " +
                    "'" + msFiscalIdForeign + "', " +
                    "'" + msFiscalIdentity + "', " +
                    "'" + msDefaultLocality + "', " +
                    "'" + msDefaultCounty + "', " +
                    "'" + msDefaultState + "', " +
                    "'" + msDefaultZipCode + "', " +
                    (mbTelecommDeviceRequired ? 1 : 0) + ", " +
                    (mbItemCodeApplying ? 1 : 0) + ", " +
                    (mbItemLineApplying ? 1 : 0) + ", " +
                    (mbBrandApplying ? 1 : 0) + ", " +
                    (mbManufacturerApplying ? 1 : 0) + ", " +
                    (mbComponentApplying ? 1 : 0) + ", " +
                    (mbDepartmentApplying ? 1 : 0) + ", " +
                    (mbCodeItemFamilyApplying ? 1 : 0) + ", " +
                    (mbCodeItemGenusApplying ? 1 : 0) + ", " +
                    (mbCodeItemLineApplying ? 1 : 0) + ", " +
                    (mbCodeBrandApplying ? 1 : 0) + ", " +
                    (mbCodeManufacturerApplying ? 1 : 0) + ", " +
                    (mbCodeComponentApplying ? 1 : 0) + ", " +
                    (mbCodeDepartmentApplying ? 1 : 0) + ", " +
                    (mbCodeItemApplying ? 1 : 0) + ", " +
                    mnLengthCodeItemFamily + ", " +
                    mnLengthCodeItemGenus + ", " +
                    mnLengthCodeItemLine + ", " +
                    mnLengthCodeBrand + ", " +
                    mnLengthCodeManufacturer + ", " +
                    mnLengthCodeComponent + ", " +
                    mnLengthCodeDepartment + ", " +
                    mnLengthCodeItem + ", " +
                    (mbMeasurementLengthApplying ? 1 : 0) + ", " +
                    (mbMeasurementSurfaceApplying ? 1 : 0) + ", " +
                    (mbMeasurementVolumeApplying ? 1 : 0) + ", " +
                    (mbMeasurementMassApplying ? 1 : 0) + ", " +
                    (mbMeasurementTimeApplying ? 1 : 0) + ", " +
                    (mbWeightGrossApplying ? 1 : 0) + ", " +
                    (mbWeightDeliveryApplying ? 1 : 0) + ", " +
                    (mbUnitsVirtualApplying ? 1 : 0) + ", " +
                    (mbUnitsContainedApplying ? 1 : 0) + ", " +
                    (mbUnitsPackageApplying ? 1 : 0) + ", " +
                    (mbPriceSrpApplying ? 1 : 0) + ", " +
                    (mbPrice1Applying ? 1 : 0) + ", " +
                    (mbPrice2Applying ? 1 : 0) + ", " +
                    (mbPrice3Applying ? 1 : 0) + ", " +
                    (mbPrice4Applying ? 1 : 0) + ", " +
                    (mbPrice5Applying ? 1 : 0) + ", " +
                    (mbEdsApplying ? 1 : 0) + ", " +
                    (mbTaxIncluded ? 1 : 0) + ", " +
                    (mbImportDeclaration ? 1 : 0) + ", " +
                    (mbOrdersPurchaseApplying ? 1 : 0) + ", " +
                    (mbOrdersSaleApplying ? 1 : 0) + ", " +
                    (mbBlockingCreditPurchaseApplying ? 1 : 0) + ", " +
                    (mbBlockingDueDatePurchaseApplying ? 1 : 0) + ", " +
                    (mbBlockingCreditSaleApplying ? 1 : 0) + ", " +
                    (mbBlockingDueDateSaleApplying ? 1 : 0) + ", " +
                    (mbDpsDeletable ? 1 : 0) + ", " +
                    (mbCustomerFixedPrices ? 1 : 0) + ", " +
                    (mbCustomerSpecialPrices ? 1 : 0) + ", " +
                    (mbCustomerPromotionalPackages ? 1 : 0) + ", " +
                    mdDelayInterestRate + ", " +
                    mnDecimalsQuantity + ", " + 
                    mnDecimalsPriceUnitary + ", " + 
                    "'" + msEdsEmsSmtpHost + "', " +
                    mnEdsEmsSmtpPort + ", " +
                    (mbEdsEmsSmtpSslEnabled ? 1 : 0) + ", " +
                    "'" + msEdsEmsFrom + "', " +
                    "'" + msEdsEmsName + "', " +
                    "'" + msEdsEmsPassword + "', " +
                    "'" + msEdsEmsSubject + "', " +
                    "'" + msEdsEmsBody + "', " +
                    "NULL, " +
                    (mbDevelopment ? 1 : 0) + ", " +
                    (mbUpdatable ? 1 : 0) + ", " +
                    (mbDisableable ? 1 : 0) + ", " +
                    (mbDeletable ? 1 : 0) + ", " +
                    (mbDisabled ? 1 : 0) + ", " +
                    (mbDeleted ? 1 : 0) + ", " +
                    (mbSystem ? 1 : 0) + ", " +
                    mnFkCountryId + ", " +
                    mnFkCurrencyId + ", " +
                    mnFkAddressFormatTypeId + ", " +
                    mnFkTaxRegimeId + ", " + 
                    (mnFkAccountCashId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountCashId_n) + ", " +
                    (mnFkAccountWarehouseId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountWarehouseId_n) + ", " +
                    (mnFkAccountVendorId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountVendorId_n) + ", " +
                    (mnFkAccountVendorAdvanceId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountVendorAdvanceId_n) + ", " +
                    (mnFkAccountCustomerId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountCustomerId_n) + ", " +
                    (mnFkAccountCustomerAdvanceId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountCustomerAdvanceId_n) + ", " +
                    (mnFkAccountCreditorId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountCreditorId_n) + ", " +
                    (mnFkAccountDebtorId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountDebtorId_n) + ", " +
                    (mnFkAccountPurchaseId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountPurchaseId_n) + ", " +
                    (mnFkAccountPurchaseIncIncrementId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountPurchaseIncIncrementId_n) + ", " +
                    (mnFkAccountPurchaseIncAdditionId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountPurchaseIncAdditionId_n) + ", " +
                    (mnFkAccountPurchaseDecDiscountId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountPurchaseDecDiscountId_n) + ", " +
                    (mnFkAccountPurchaseDecReturnId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountPurchaseDecReturnId_n) + ", " +
                    (mnFkAccountSaleId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountSaleId_n) + ", " +
                    (mnFkAccountSaleIncIncrementId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountSaleIncIncrementId_n) + ", " +
                    (mnFkAccountSaleIncAdditionId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountSaleIncAdditionId_n) + ", " +
                    (mnFkAccountSaleDecDiscountId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountSaleDecDiscountId_n) + ", " +
                    (mnFkAccountSaleDecReturnId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountSaleDecReturnId_n) + ", " +
                    (mnFkRecordTypeMoneyInId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkRecordTypeMoneyInId_n) + ", " +
                    (mnFkRecordTypeMoneyOutId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkRecordTypeMoneyOutId_n) + ", " +
                    (mnFkRecordTypeGoodsInId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkRecordTypeGoodsInId_n) + ", " +
                    (mnFkRecordTypeGoodsOutId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkRecordTypeGoodsOutId_n) + ", " +
                    (mnFkRecordTypeTrnPurchaseId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkRecordTypeTrnPurchaseId_n) + ", " +
                    (mnFkRecordTypeTrnSaleId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkRecordTypeTrnSaleId_n) + ", " +
                    (mnFkTaxGroupId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkTaxGroupId_n) + ", " +
                    mnFkExchangeRateAppTypePurchaseId + ", " +
                    mnFkExchangeRateAppTypeSaleId + ", " +
                    mnFkBlockingTypePurchaseOrderId + ", " +
                    mnFkBlockingTypePurchaseInvoiceId + ", " +
                    mnFkBlockingTypeSaleOrderId + ", " +
                    mnFkBlockingTypeSaleInvoiceId + ", " +
                    mnFkDnpTypeIogId + ", " +
                    mnFkDnpTypeIomId + ", " +
                    mnFkEdsEmsTypeId + ", " +
                    mnFkUserInsertId + ", " +
                    mnFkUserUpdateId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            mnFkUserUpdateId = session.getUser().getPkUserId();

            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_bpr = " + mnPkBizPartnerId + ", " +
                    //"ver = " + mnVersion + ", " +
                    //"ver_ts = " + "NOW()" + ", " +
                    "b_mod_cfg = " + (mbModuleConfiguration ? 1 : 0) + ", " +
                    "b_mod_fin = " + (mbModuleFinance ? 1 : 0) + ", " +
                    "b_mod_pur = " + (mbModulePurchases ? 1 : 0) + ", " +
                    "b_mod_sal = " + (mbModuleSales ? 1 : 0) + ", " +
                    "b_mod_inv = " + (mbModuleInventory ? 1 : 0) + ", " +
                    "b_mod_mkt = " + (mbModuleMarketing ? 1 : 0) + ", " +
                    "b_mod_pos = " + (mbModulePointOfSale ? 1 : 0) + ", " +
                    "b_mod_srv = " + (mbModuleServices ? 1 : 0) + ", " +
                    "acc_lev = " + mnAccountLevel + ", " +
                    "acc_fmt = '" + msAccountFormat + "', " +
                    "b_dt_ven = " + (mbDateVendorApplying ? 1 : 0) + ", " +
                    "b_dt_cus = " + (mbDateCustomerApplying ? 1 : 0) + ", " +
                    "b_code_bpr = " + (mbCodeBizPartnerApplying ? 1 : 0) + ", " +
                    "len_code_bpr = " + mnLengthCodeBizPartner + ", " +
                    "fis_id_cty = '" + msFiscalIdCountry + "', " +
                    "fis_id_frg = '" + msFiscalIdForeign + "', " +
                    "fis_idy = '" + msFiscalIdentity + "', " +
                    "def_loc = '" + msDefaultLocality + "', " +
                    "def_cou = '" + msDefaultCounty + "', " +
                    "def_ste = '" + msDefaultState + "', " +
                    "def_zip = '" + msDefaultZipCode + "', " +
                    "b_tcd_req = " + (mbTelecommDeviceRequired ? 1 : 0) + ", " +
                    "b_itm_code = " + (mbItemCodeApplying ? 1 : 0) + ", " +
                    "b_itm_lin = " + (mbItemLineApplying ? 1 : 0) + ", " +
                    "b_itm_brd = " + (mbBrandApplying ? 1 : 0) + ", " +
                    "b_itm_mfr = " + (mbManufacturerApplying ? 1 : 0) + ", " +
                    "b_itm_cmp = " + (mbComponentApplying ? 1 : 0) + ", " +
                    "b_itm_dep = " + (mbDepartmentApplying ? 1 : 0) + ", " +
                    "b_code_fam = " + (mbCodeItemFamilyApplying ? 1 : 0) + ", " +
                    "b_code_gen = " + (mbCodeItemGenusApplying ? 1 : 0) + ", " +
                    "b_code_lin = " + (mbCodeItemLineApplying ? 1 : 0) + ", " +
                    "b_code_brd = " + (mbCodeBrandApplying ? 1 : 0) + ", " +
                    "b_code_mfr = " + (mbCodeManufacturerApplying ? 1 : 0) + ", " +
                    "b_code_cmp = " + (mbCodeComponentApplying ? 1 : 0) + ", " +
                    "b_code_dep = " + (mbCodeDepartmentApplying ? 1 : 0) + ", " +
                    "b_code_itm = " + (mbCodeItemApplying ? 1 : 0) + ", " +
                    "len_code_fam = " + mnLengthCodeItemFamily + ", " +
                    "len_code_gen = " + mnLengthCodeItemGenus + ", " +
                    "len_code_lin = " + mnLengthCodeItemLine + ", " +
                    "len_code_brd = " + mnLengthCodeBrand + ", " +
                    "len_code_mfr = " + mnLengthCodeManufacturer + ", " +
                    "len_code_cmp = " + mnLengthCodeComponent + ", " +
                    "len_code_dep = " + mnLengthCodeDepartment + ", " +
                    "len_code_itm = " + mnLengthCodeItem + ", " +
                    "b_mst_len = " + (mbMeasurementLengthApplying ? 1 : 0) + ", " +
                    "b_mst_sur = " + (mbMeasurementSurfaceApplying ? 1 : 0) + ", " +
                    "b_mst_vol = " + (mbMeasurementVolumeApplying ? 1 : 0) + ", " +
                    "b_mst_mas = " + (mbMeasurementMassApplying ? 1 : 0) + ", " +
                    "b_mst_tme = " + (mbMeasurementTimeApplying ? 1 : 0) + ", " +
                    "b_wgt_grs = " + (mbWeightGrossApplying ? 1 : 0) + ", " +
                    "b_wgt_del = " + (mbWeightDeliveryApplying ? 1 : 0) + ", " +
                    "b_unt_vir = " + (mbUnitsVirtualApplying ? 1 : 0) + ", " +
                    "b_unt_con = " + (mbUnitsContainedApplying ? 1 : 0) + ", " +
                    "b_unt_pac = " + (mbUnitsPackageApplying ? 1 : 0) + ", " +
                    "b_prc_srp = " + (mbPriceSrpApplying ? 1 : 0) + ", " +
                    "b_prc_1 = " + (mbPrice1Applying ? 1 : 0) + ", " +
                    "b_prc_2 = " + (mbPrice2Applying ? 1 : 0) + ", " +
                    "b_prc_3 = " + (mbPrice3Applying ? 1 : 0) + ", " +
                    "b_prc_4 = " + (mbPrice4Applying ? 1 : 0) + ", " +
                    "b_prc_5 = " + (mbPrice5Applying ? 1 : 0) + ", " +
                    "b_eds = " + (mbEdsApplying ? 1 : 0) + ", " +
                    "b_tax_inc = " + (mbTaxIncluded ? 1 : 0) + ", " +
                    "b_imp_dec = " + (mbImportDeclaration ? 1 : 0) + ", " +
                    "b_ord_pur = " + (mbOrdersPurchaseApplying ? 1 : 0) + ", " +
                    "b_ord_sal = " + (mbOrdersSaleApplying ? 1 : 0) + ", " +
                    "b_blk_cdt_pur = " + (mbBlockingCreditPurchaseApplying ? 1 : 0) + ", " +
                    "b_blk_due_pur = " + (mbBlockingDueDatePurchaseApplying ? 1 : 0) + ", " +
                    "b_blk_cdt_sal = " + (mbBlockingCreditSaleApplying ? 1 : 0) + ", " +
                    "b_blk_due_sal = " + (mbBlockingDueDateSaleApplying ? 1 : 0) + ", " +
                    "b_dps_del = " + (mbDpsDeletable ? 1 : 0) + ", " +
                    "b_cus_fix = " + (mbCustomerFixedPrices ? 1 : 0) + ", " +
                    "b_cus_spe = " + (mbCustomerSpecialPrices ? 1 : 0) + ", " +
                    "b_cus_prm = " + (mbCustomerPromotionalPackages ? 1 : 0) + ", " +
                    "int_rat = " + mdDelayInterestRate + ", " +
                    "decs_qty = " + mnDecimalsQuantity + ", " +
                    "decs_prc_unt = " + mnDecimalsPriceUnitary + ", " +
                    "eds_ems_smtp_host = '" + msEdsEmsSmtpHost + "', " +
                    "eds_ems_smtp_port = " + mnEdsEmsSmtpPort + ", " +
                    "eds_ems_smtp_ssl = " + (mbEdsEmsSmtpSslEnabled ? 1 : 0) + ", " +
                    "eds_ems_from = '" + msEdsEmsFrom + "', " +
                    "eds_ems_name = '" + msEdsEmsName + "', " +
                    "eds_ems_pswd = '" + msEdsEmsPassword + "', " +
                    "eds_ems_subj = '" + msEdsEmsSubject + "', " +
                    "eds_ems_body = '" + msEdsEmsBody + "', " +
                    //"img_n = " + moImage_n + ", " +
                    "b_dev = " + (mbDevelopment ? 1 : 0) + ", " +
                    "b_can_upd = " + (mbUpdatable ? 1 : 0) + ", " +
                    "b_can_dis = " + (mbDisableable ? 1 : 0) + ", " +
                    "b_can_del = " + (mbDeletable ? 1 : 0) + ", " +
                    "b_dis = " + (mbDisabled ? 1 : 0) + ", " +
                    "b_del = " + (mbDeleted ? 1 : 0) + ", " +
                    "b_sys = " + (mbSystem ? 1 : 0) + ", " +
                    "fk_cty = " + mnFkCountryId + ", " +
                    "fk_cur = " + mnFkCurrencyId + ", " +
                    "fk_baf_tp = " + mnFkAddressFormatTypeId + ", " +
                    "fk_tax_reg = " + mnFkTaxRegimeId + ", " +
                    "fk_acc_csh_n = " + (mnFkAccountCashId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountCashId_n) + ", " +
                    "fk_acc_wah_n = " + (mnFkAccountWarehouseId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountWarehouseId_n) + ", " +
                    "fk_acc_ven_n = " + (mnFkAccountVendorId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountVendorId_n) + ", " +
                    "fk_acc_ven_adv_n = " + (mnFkAccountVendorAdvanceId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountVendorAdvanceId_n) + ", " +
                    "fk_acc_cus_n = " + (mnFkAccountCustomerId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountCustomerId_n) + ", " +
                    "fk_acc_cus_adv_n = " + (mnFkAccountCustomerAdvanceId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountCustomerAdvanceId_n) + ", " +
                    "fk_acc_cdr_n = " + (mnFkAccountCreditorId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountCreditorId_n) + ", " +
                    "fk_acc_dbr_n = " + (mnFkAccountDebtorId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountDebtorId_n) + ", " +
                    "fk_acc_pur_n = " + (mnFkAccountPurchaseId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountPurchaseId_n) + ", " +
                    "fk_acc_pur_inc_inc_n = " + (mnFkAccountPurchaseIncIncrementId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountPurchaseIncIncrementId_n) + ", " +
                    "fk_acc_pur_inc_add_n = " + (mnFkAccountPurchaseIncAdditionId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountPurchaseIncAdditionId_n) + ", " +
                    "fk_acc_pur_dec_dis_n = " + (mnFkAccountPurchaseDecDiscountId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountPurchaseDecDiscountId_n) + ", " +
                    "fk_acc_pur_dec_ret_n = " + (mnFkAccountPurchaseDecReturnId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountPurchaseDecReturnId_n) + ", " +
                    "fk_acc_sal_n = " + (mnFkAccountSaleId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountSaleId_n) + ", " +
                    "fk_acc_sal_inc_inc_n = " + (mnFkAccountSaleIncIncrementId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountSaleIncIncrementId_n) + ", " +
                    "fk_acc_sal_inc_add_n = " + (mnFkAccountSaleIncAdditionId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountSaleIncAdditionId_n) + ", " +
                    "fk_acc_sal_dec_dis_n = " + (mnFkAccountSaleDecDiscountId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountSaleDecDiscountId_n) + ", " +
                    "fk_acc_sal_dec_ret_n = " + (mnFkAccountSaleDecReturnId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkAccountSaleDecReturnId_n) + ", " +
                    "fk_rec_tp_csh_in_n = " + (mnFkRecordTypeMoneyInId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkRecordTypeMoneyInId_n) + ", " +
                    "fk_rec_tp_csh_out_n = " + (mnFkRecordTypeMoneyOutId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkRecordTypeMoneyOutId_n) + ", " +
                    "fk_rec_tp_gds_in_n = " + (mnFkRecordTypeGoodsInId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkRecordTypeGoodsInId_n) + ", " +
                    "fk_rec_tp_gds_out_n = " + (mnFkRecordTypeGoodsOutId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkRecordTypeGoodsOutId_n) + ", " +
                    "fk_rec_tp_trn_pur_n = " + (mnFkRecordTypeTrnPurchaseId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkRecordTypeTrnPurchaseId_n) + ", " +
                    "fk_rec_tp_trn_sal_n = " + (mnFkRecordTypeTrnSaleId_n == DLibConsts.UNDEFINED ? "NULL" : "" + mnFkRecordTypeTrnSaleId_n) + ", " +
                    "fk_tax_grp_n = " + mnFkTaxGroupId_n + ", " +
                    "fk_exr_app_pur = " + mnFkExchangeRateAppTypePurchaseId + ", " +
                    "fk_exr_app_sal = " + mnFkExchangeRateAppTypeSaleId + ", " +
                    "fk_blk_tp_pur_ord = " + mnFkBlockingTypePurchaseOrderId + ", " +
                    "fk_blk_tp_pur_inv = " + mnFkBlockingTypePurchaseInvoiceId + ", " +
                    "fk_blk_tp_sal_ord = " + mnFkBlockingTypeSaleOrderId + ", " +
                    "fk_blk_tp_sal_inv = " + mnFkBlockingTypeSaleInvoiceId + ", " +
                    "fk_dnp_tp_iog = " + mnFkDnpTypeIogId + ", " +
                    "fk_dnp_tp_iom = " + mnFkDnpTypeIomId + ", " +
                    "fk_eds_ems_tp = " + mnFkEdsEmsTypeId + ", " +
                    //"fk_usr_ins = " + mnFkUserInsertId + ", " +
                    "fk_usr_upd = " + mnFkUserUpdateId + ", " +
                    //"ts_usr_ins = " + "NOW()" + ", " +
                    "ts_usr_upd = " + "NOW()" + " " +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        mbRegistryNew = false;

        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbConfigCompany clone() throws CloneNotSupportedException {
        DDbConfigCompany registry = new DDbConfigCompany();

        registry.setPkBizPartnerId(this.getPkBizPartnerId());
        registry.setVersion(this.getVersion());
        registry.setVersionTs(this.getVersionTs());
        registry.setModuleConfiguration(this.isModuleConfiguration());
        registry.setModuleFinance(this.isModuleFinance());
        registry.setModulePurchases(this.isModulePurchases());
        registry.setModuleSales(this.isModuleSales());
        registry.setModuleInventory(this.isModuleInventory());
        registry.setModuleMarketing(this.isModuleMarketing());
        registry.setModulePointOfSale(this.isModulePointOfSale());
        registry.setModuleServices(this.isModuleServices());
        registry.setAccountLevel(this.getAccountLevel());
        registry.setAccountFormat(this.getAccountFormat());
        registry.setDateVendorApplying(this.isDateVendorApplying());
        registry.setDateCustomerApplying(this.isDateCustomerApplying());
        registry.setCodeBizPartnerApplying(this.isCodeBizPartnerApplying());
        registry.setLengthCodeBizPartner(this.getLengthCodeBizPartner());
        registry.setFiscalIdCountry(this.getFiscalIdCountry());
        registry.setFiscalIdForeign(this.getFiscalIdForeign());
        registry.setFiscalIdentity(this.getFiscalIdentity());
        registry.setDefaultLocality(this.getDefaultLocality());
        registry.setDefaultCounty(this.getDefaultCounty());
        registry.setDefaultState(this.getDefaultState());
        registry.setDefaultZipCode(this.getDefaultZipCode());
        registry.setTelecommDeviceRequired(this.isTelecommDeviceRequired());
        registry.setItemCodeApplying(this.isItemCodeApplying());
        registry.setItemLineApplying(this.isItemLineApplying());
        registry.setBrandApplying(this.isBrandApplying());
        registry.setManufacturerApplying(this.isManufacturerApplying());
        registry.setComponentApplying(this.isComponentApplying());
        registry.setDepartmentApplying(this.isDepartmentApplying());
        registry.setCodeItemFamilyApplying(this.isCodeItemFamilyApplying());
        registry.setCodeItemGenusApplying(this.isCodeItemGenusApplying());
        registry.setCodeItemLineApplying(this.isCodeItemLineApplying());
        registry.setCodeBrandApplying(this.isCodeBrandApplying());
        registry.setCodeManufacturerApplying(this.isCodeManufacturerApplying());
        registry.setCodeComponentApplying(this.isCodeComponentApplying());
        registry.setCodeDepartmentApplying(this.isCodeDepartmentApplying());
        registry.setCodeItemApplying(this.isCodeItemApplying());
        registry.setLengthCodeItemFamily(this.getLengthCodeItemFamily());
        registry.setLengthCodeItemGenus(this.getLengthCodeItemGenus());
        registry.setLengthCodeItemLine(this.getLengthCodeItemLine());
        registry.setLengthCodeBrand(this.getLengthCodeBrand());
        registry.setLengthCodeManufacturer(this.getLengthCodeManufacturer());
        registry.setLengthCodeComponent(this.getLengthCodeComponent());
        registry.setLengthCodeDepartment(this.getLengthCodeDepartment());
        registry.setLengthCodeItem(this.getLengthCodeItem());
        registry.setMeasurementLengthApplying(this.isMeasurementLengthApplying());
        registry.setMeasurementSurfaceApplying(this.isMeasurementSurfaceApplying());
        registry.setMeasurementVolumeApplying(this.isMeasurementVolumeApplying());
        registry.setMeasurementMassApplying(this.isMeasurementMassApplying());
        registry.setMeasurementTimeApplying(this.isMeasurementTimeApplying());
        registry.setWeightGrossApplying(this.isWeightGrossApplying());
        registry.setWeightDeliveryApplying(this.isWeightDeliveryApplying());
        registry.setUnitsVirtualApplying(this.isUnitsVirtualApplying());
        registry.setUnitsContainedApplying(this.isUnitsContainedApplying());
        registry.setUnitsPackageApplying(this.isUnitsPackageApplying());
        registry.setPriceSrpApplying(this.isPriceSrpApplying());
        registry.setPrice1Applying(this.isPrice1Applying());
        registry.setPrice2Applying(this.isPrice2Applying());
        registry.setPrice3Applying(this.isPrice3Applying());
        registry.setPrice4Applying(this.isPrice4Applying());
        registry.setPrice5Applying(this.isPrice5Applying());
        registry.setEdsApplying(this.isEdsApplying());
        registry.setTaxIncluded(this.isTaxIncluded());
        registry.setImportDeclaration(this.isImportDeclaration());
        registry.setOrdersPurchaseApplying(this.isOrdersPurchaseApplying());
        registry.setOrdersSaleApplying(this.isOrdersSaleApplying());
        registry.setBlockingCreditPurchaseApplying(this.isBlockingCreditPurchaseApplying());
        registry.setBlockingDueDatePurchaseApplying(this.isBlockingDueDatePurchaseApplying());
        registry.setBlockingCreditSaleApplying(this.isBlockingCreditSaleApplying());
        registry.setBlockingDueDateSaleApplying(this.isBlockingDueDateSaleApplying());
        registry.setDpsDeletable(this.isDpsDeletable());
        registry.setCustomerFixedPrices(this.isCustomerFixedPrices());
        registry.setCustomerSpecialPrices(this.isCustomerSpecialPrices());
        registry.setCustomerPromotionalPackages(this.isCustomerPromotionalPackages());
        registry.setDelayInterestRate(this.getDelayInterestRate());
        registry.setDecimalsQuantity(this.getDecimalsQuantity());
        registry.setDecimalsPriceUnitary(this.getDecimalsPriceUnitary());
        registry.setEdsEmsSmtpHost(this.getEdsEmsSmtpHost());
        registry.setEdsEmsSmtpPort(this.getEdsEmsSmtpPort());
        registry.setEdsEmsSmtpSslEnabled(this.isEdsEmsSmtpSslEnabled());
        registry.setEdsEmsFrom(this.getEdsEmsFrom());
        registry.setEdsEmsName(this.getEdsEmsName());
        registry.setEdsEmsPassword(this.getEdsEmsPassword());
        registry.setEdsEmsSubject(this.getEdsEmsSubject());
        registry.setEdsEmsBody(this.getEdsEmsBody());
        registry.setImage_n(this.getImage_n());
        registry.setDevelopment(this.isDevelopment());
        registry.setUpdatable(this.isUpdatable());
        registry.setDisableable(this.isDisableable());
        registry.setDeletable(this.isDeletable());
        registry.setDisabled(this.isDisabled());
        registry.setDeleted(this.isDeleted());
        registry.setSystem(this.isSystem());
        registry.setFkCountryId(this.getFkCountryId());
        registry.setFkCurrencyId(this.getFkCurrencyId());
        registry.setFkAddressFormatTypeId(this.getFkAddressFormatTypeId());
        registry.setFkTaxRegimeId(this.getFkTaxRegimeId());
        registry.setFkAccountCashId_n(this.getFkAccountCashId_n());
        registry.setFkAccountWarehouseId_n(this.getFkAccountWarehouseId_n());
        registry.setFkAccountVendorId_n(this.getFkAccountVendorId_n());
        registry.setFkAccountVendorAdvanceId_n(this.getFkAccountVendorAdvanceId_n());
        registry.setFkAccountCustomerId_n(this.getFkAccountCustomerId_n());
        registry.setFkAccountCustomerAdvanceId_n(this.getFkAccountCustomerAdvanceId_n());
        registry.setFkAccountCreditorId_n(this.getFkAccountCreditorId_n());
        registry.setFkAccountDebtorId_n(this.getFkAccountDebtorId_n());
        registry.setFkAccountPurchaseId_n(this.getFkAccountPurchaseId_n());
        registry.setFkAccountPurchaseIncIncrementId_n(this.getFkAccountPurchaseIncIncrementId_n());
        registry.setFkAccountPurchaseIncAdditionId_n(this.getFkAccountPurchaseIncAdditionId_n());
        registry.setFkAccountPurchaseDecDiscountId_n(this.getFkAccountPurchaseDecDiscountId_n());
        registry.setFkAccountPurchaseDecReturnId_n(this.getFkAccountPurchaseDecReturnId_n());
        registry.setFkAccountSaleId_n(this.getFkAccountSaleId_n());
        registry.setFkAccountSaleIncIncrementId_n(this.getFkAccountSaleIncIncrementId_n());
        registry.setFkAccountSaleIncAdditionId_n(this.getFkAccountSaleIncAdditionId_n());
        registry.setFkAccountSaleDecDiscountId_n(this.getFkAccountSaleDecDiscountId_n());
        registry.setFkAccountSaleDecReturnId_n(this.getFkAccountSaleDecReturnId_n());
        registry.setFkRecordTypeMoneyInId_n(this.getFkRecordTypeMoneyInId_n());
        registry.setFkRecordTypeMoneyOutId_n(this.getFkRecordTypeMoneyOutId_n());
        registry.setFkRecordTypeGoodsInId_n(this.getFkRecordTypeGoodsInId_n());
        registry.setFkRecordTypeGoodsOutId_n(this.getFkRecordTypeGoodsOutId_n());
        registry.setFkRecordTypeTrnPurchaseId_n(this.getFkRecordTypeTrnPurchaseId_n());
        registry.setFkRecordTypeTrnSaleId_n(this.getFkRecordTypeTrnSaleId_n());
        registry.setFkTaxGroupId_n(this.getFkTaxGroupId_n());
        registry.setFkExchangeRateAppTypePurchaseId(this.getFkExchangeRateAppTypePurchaseId());
        registry.setFkExchangeRateAppTypeSaleId(this.getFkExchangeRateAppTypeSaleId());
        registry.setFkBlockingTypePurchaseOrderId(this.getFkBlockingTypePurchaseOrderId());
        registry.setFkBlockingTypePurchaseInvoiceId(this.getFkBlockingTypePurchaseInvoiceId());
        registry.setFkBlockingTypeSaleOrderId(this.getFkBlockingTypeSaleOrderId());
        registry.setFkBlockingTypeSaleInvoiceId(this.getFkBlockingTypeSaleInvoiceId());
        registry.setFkDnpTypeIogId(this.getFkDnpTypeIogId());
        registry.setFkDnpTypeIomId(this.getFkDnpTypeIomId());
        registry.setFkEdsEmsTypeId(this.getFkEdsEmsTypeId());
        registry.setFkUserInsertId(this.getFkUserInsertId());
        registry.setFkUserUpdateId(this.getFkUserUpdateId());
        registry.setTsUserInsert(this.getTsUserInsert());
        registry.setTsUserUpdate(this.getTsUserUpdate());

        registry.setDecimalFormatQuantity(this.getDecimalFormatQuantity());
        registry.setDecimalFormatPriceUnitary(this.getDecimalFormatPriceUnitary());
    
        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }

    @Override
    public void saveField(final Statement statement, final int[] pk, final int field, final Object value) throws SQLException, Exception {
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        msSql = "UPDATE " + getSqlTable() + " SET ";

        switch (field) {
            case FIELD_VERSION:
                msSql += "ver = " + value + ", ver_ts = NOW() ";
                break;
            default:
                throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
        }

        msSql += getSqlWhere(pk);
        statement.execute(msSql);
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public int getCompanyId() {
        return getPkBizPartnerId();
    }
}
