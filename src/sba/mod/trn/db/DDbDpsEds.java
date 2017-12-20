/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import cfd.DCfdConsts;
import cfd.DCfdUtils;
import cfd.DElement;
import cfd.DElementExtAddenda;
import cfd.ext.continental.DElementAddendaContinentalTire;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiSession;
import sba.lib.xml.DXmlUtils;
import sba.mod.DModConsts;
import sba.mod.DModSysConsts;
import sba.mod.cfg.db.DDbConfigBranch;
import static sba.mod.trn.db.DTrnEdsUtils.extractExtAddenda;

/**
 *
 * @author Sergio Flores
 */
public class DDbDpsEds extends DDbRegistry {

    protected int mnPkDpsId;
    protected String msCertificateNumber;
    protected String msSignedText;
    protected String msSignature;
    protected String msUniqueId;
    protected Date mtDocTs;
    protected String msDocXml;
    protected String msDocXmlRaw;
    protected String msDocXmlAddenda;
    protected String msDocXmlName;
    protected String msCancelXml;
    protected java.sql.Blob moCancelPdf_n;
    protected int mnFkXmlTypeId;
    protected int mnFkXmlStatusId;
    protected int mnFkXmlAddendaTypeId;
    protected int mnFkXmlSignatureProviderId;
    protected int mnFkCertificateId;
    protected int mnFkUserIssueId;
    protected int mnFkUserAnnulId;
    protected Date mtTsUserIssue;
    protected Date mtTsUserAnnul;
    
    protected boolean mbAuxIssued;
    protected boolean mbAuxAnnulled;
    protected boolean mbAuxRewriteXmlOnSave;
    protected boolean mbAuxRegenerateXmlOnSave;
    protected int[] manAuxXmlSignatureRequestKey;

    public DDbDpsEds() {
        super(DModConsts.T_DPS_EDS);
        initRegistry();
    }
    
    /*
     * Private methods:
     */
    
    /*
     * Public methods:
     */

    public void setPkDpsId(int n) { mnPkDpsId = n; }
    public void setCertificateNumber(String s) { msCertificateNumber = s; }
    public void setSignedText(String s) { msSignedText = s; }
    public void setSignature(String s) { msSignature = s; }
    public void setUniqueId(String s) { msUniqueId = s; }
    public void setDocTs(Date t) { mtDocTs = t; }
    public void setDocXml(String s) { msDocXml = s; }
    public void setDocXmlRaw(String s) { msDocXmlRaw = s; }
    public void setDocXmlAddenda(String s) { msDocXmlAddenda = s; }
    public void setDocXmlName(String s) { msDocXmlName = s; }
    public void setCancelXml(String s) { msCancelXml = s; }
    public void setCancelPdf_n(java.sql.Blob o) { moCancelPdf_n = o; }
    public void setFkXmlTypeId(int n) { mnFkXmlTypeId = n; }
    public void setFkXmlStatusId(int n) { mnFkXmlStatusId = n; }
    public void setFkXmlAddendaTypeId(int n) { mnFkXmlAddendaTypeId = n; }
    public void setFkXmlSignatureProviderId(int n) { mnFkXmlSignatureProviderId = n; }
    public void setFkCertificateId(int n) { mnFkCertificateId = n; }
    public void setFkUserIssuedId(int n) { mnFkUserIssueId = n; }
    public void setFkUserAnnulId(int n) { mnFkUserAnnulId = n; }
    public void setTsUserIssue(Date t) { mtTsUserIssue = t; }
    public void setTsUserAnnul(Date t) { mtTsUserAnnul = t; }

    public int getPkDpsId() { return mnPkDpsId; }
    public String getCertificateNumber() { return msCertificateNumber; }
    public String getSignedText() { return msSignedText; }
    public String getSignature() { return msSignature; }
    public String getUniqueId() { return msUniqueId; }
    public Date getDocTs() { return mtDocTs; }
    public String getDocXml() { return msDocXml; }
    public String getDocXmlRaw() { return msDocXmlRaw; }
    public String getDocXmlAddenda() { return msDocXmlAddenda; }
    public String getDocXmlName() { return msDocXmlName; }
    public String getCancelXml() { return msCancelXml; }
    public java.sql.Blob getCancelPdf_n() { return moCancelPdf_n; }
    public int getFkXmlTypeId() { return mnFkXmlTypeId; }
    public int getFkXmlStatusId() { return mnFkXmlStatusId; }
    public int getFkXmlAddendaTypeId() { return mnFkXmlAddendaTypeId; }
    public int getFkXmlSignatureProviderId() { return mnFkXmlSignatureProviderId; }
    public int getFkCertificateId() { return mnFkCertificateId; }
    public int getFkUserIssuedId() { return mnFkUserIssueId; }
    public int getFkUserAnnulId() { return mnFkUserAnnulId; }
    public Date getTsUserIssue() { return mtTsUserIssue; }
    public Date getTsUserAnnul() { return mtTsUserAnnul; }

    public void setAuxIssued(boolean b) { mbAuxIssued = b; }
    public void setAuxAnnulled(boolean b) { mbAuxAnnulled = b; }
    public void setAuxRewriteXmlOnSave(boolean b) { mbAuxRewriteXmlOnSave = b; }
    public void setAuxRegenerateXmlOnSave(boolean b) { mbAuxRegenerateXmlOnSave = b; }
    public void setAuxXmlSignatureRequestKey(int[] key) { manAuxXmlSignatureRequestKey = key; }

    public boolean isAuxIssued() { return mbAuxIssued; }
    public boolean isAuxAnnulled() { return mbAuxAnnulled; }
    public boolean isAuxRewriteXmlOnSave() { return mbAuxRewriteXmlOnSave; }
    public boolean isAuxRegenerateXmlOnSave() { return mbAuxRegenerateXmlOnSave; }
    public int[] getAuxXmlSignatureRequestKey() { return manAuxXmlSignatureRequestKey; }
    
    /**
     * Get XML raw (just as fetched from web service) if available and its status is at least 'issued', otherwise own generated XML.
     * @return Suitable XML.
     */
    public String getSuitableDocXml() {
        return mnFkXmlStatusId >= DModSysConsts.TS_XML_ST_ISS && !msDocXmlRaw.isEmpty() ? msDocXmlRaw : msDocXml;
    }

    @Override
    public void setPrimaryKey(int[] pk) {
        mnPkDpsId = pk[0];
    }

    @Override
    public int[] getPrimaryKey() {
        return new int[] { mnPkDpsId };
    }

    @Override
    public void initRegistry() {
        initBaseRegistry();

        mnPkDpsId = 0;
        msCertificateNumber = "";
        msSignedText = null;
        msSignature = null;
        msUniqueId = "";
        mtDocTs = null;
        msDocXml = "";
        msDocXmlRaw = "";
        msDocXmlAddenda = "";
        msDocXmlName = "";
        msCancelXml = "";
        moCancelPdf_n = null;
        mnFkXmlTypeId = 0;
        mnFkXmlStatusId = 0;
        mnFkXmlAddendaTypeId = 0;
        mnFkXmlSignatureProviderId = 0;
        mnFkCertificateId = 0;
        mnFkUserIssueId = 0;
        mnFkUserAnnulId = 0;
        mtTsUserIssue = null;
        mtTsUserAnnul = null;
        
        mbAuxIssued = false;
        mbAuxAnnulled = false;
        mbAuxRewriteXmlOnSave = false;
        mbAuxRegenerateXmlOnSave = false;
        manAuxXmlSignatureRequestKey = null;
    }

    @Override
    public String getSqlTable() {
        return DModConsts.TablesMap.get(mnRegistryType);
    }

    @Override
    public String getSqlWhere() {
        return "WHERE id_dps = " + mnPkDpsId + " ";
    }

    @Override
    public String getSqlWhere(int[] pk) {
        return "WHERE id_dps = " + pk[0] + " ";
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
            mnPkDpsId = resultSet.getInt("id_dps");
            msCertificateNumber = resultSet.getString("cer_num");
            msSignedText = resultSet.getString("sig_txt");
            msSignature = resultSet.getString("sig");
            msUniqueId = resultSet.getString("uid");
            mtDocTs = resultSet.getTimestamp("doc_ts");
            msDocXml = resultSet.getString("doc_xml");
            msDocXmlRaw = resultSet.getString("doc_xml_raw");
            msDocXmlAddenda = resultSet.getString("doc_xml_add");
            msDocXmlName = resultSet.getString("doc_xml_name");
            msCancelXml = resultSet.getString("can_xml");
            moCancelPdf_n = resultSet.getBlob("can_pdf_n");
            mnFkXmlTypeId = resultSet.getInt("fk_xml_tp");
            mnFkXmlStatusId = resultSet.getInt("fk_xml_st");
            mnFkXmlAddendaTypeId = resultSet.getInt("fk_xml_add_tp");
            mnFkXmlSignatureProviderId = resultSet.getInt("fk_xsp");
            mnFkCertificateId = resultSet.getInt("fk_cer");
            mnFkUserIssueId = resultSet.getInt("fk_usr_iss");
            mnFkUserAnnulId = resultSet.getInt("fk_usr_ann");
            mtTsUserIssue = resultSet.getTimestamp("ts_usr_iss");
            mtTsUserAnnul = resultSet.getTimestamp("ts_usr_ann");

            mbRegistryNew = false;
        }

        mnQueryResultId = DDbConsts.READ_OK;
    }

    @Override
    public void save(DGuiSession session) throws SQLException, Exception {
        DDbDps dps = null;
        
        initQueryMembers();
        mnQueryResultId = DDbConsts.SAVE_ERROR;

        if (mbRegistryNew) {
            verifyRegistryNew(session);
        }

        if (mbAuxIssued) {
            mnFkUserIssueId = session.getUser().getPkUserId();
        }
        else if (mnFkUserIssueId == DLibConsts.UNDEFINED) {
            mnFkUserIssueId = DUtilConsts.USR_NA_ID;
        }

        if (mbAuxAnnulled) {
            mnFkUserAnnulId = session.getUser().getPkUserId();
        }
        else if (mnFkUserAnnulId == DLibConsts.UNDEFINED) {
            mnFkUserAnnulId = DUtilConsts.USR_NA_ID;
        }

        if (mbAuxRewriteXmlOnSave || mbAuxRegenerateXmlOnSave) {
            dps = (DDbDps) session.readRegistry(DModConsts.T_DPS, getPrimaryKey());
        }

        if (mbAuxRegenerateXmlOnSave) {
            // XML must be regenerated, so embeed addenda:
            
            DElementExtAddenda extAddenda = null;
            
            switch (mnFkXmlTypeId) {
                case DModSysConsts.TS_XML_TP_CFD:
                    throw new UnsupportedOperationException("Not supported yet.");  // no plans for supporting it later

                case DModSysConsts.TS_XML_TP_CFDI_32:
                    // Create EDS:
                    cfd.ver32.DElementComprobante comprobante32 = DCfdUtils.getCfdi32(msDocXml);
                    DTrnEdsUtils.configureCfdi32(session, comprobante32);

                    // Append to EDS the very addenda previously added to DPS if any:
                    if (!msDocXmlAddenda.isEmpty()) {
                        extAddenda = extractExtAddenda(msDocXmlAddenda, mnFkXmlAddendaTypeId);
                        if (extAddenda != null) {
                            cfd.ver32.DElementAddenda addenda = null;
                            
                            if (comprobante32.getEltOpcAddenda() != null) {
                                addenda = comprobante32.getEltOpcAddenda();
                            }
                            else {
                                addenda = new cfd.ver32.DElementAddenda();
                                comprobante32.setEltOpcAddenda(addenda);
                            }
                            
                            for (DElement element : addenda.getElements()) {
                                if (element instanceof DElementAddendaContinentalTire) {
                                    addenda.getElements().remove(element);
                                    break;
                                }
                            }
                            
                            addenda.getElements().add(extAddenda);
                        }
                    }
                    
                    msDocXml = DCfdConsts.XML_HEADER + comprobante32.getElementForXml();
                    break;

                case DModSysConsts.TS_XML_TP_CFDI_33:
                    // Create EDS:
                    cfd.ver33.DElementComprobante comprobante33 = DCfdUtils.getCfdi33(msDocXml);
                    DTrnEdsUtils.configureCfdi33(session, comprobante33);

                    // Append to EDS the very addenda previously added to DPS if any:
                    if (!msDocXmlAddenda.isEmpty()) {
                        extAddenda = extractExtAddenda(msDocXmlAddenda, mnFkXmlAddendaTypeId);
                        if (extAddenda != null) {
                            cfd.ver33.DElementAddenda addenda = null;
                            
                            if (comprobante33.getEltOpcAddenda() != null) {
                                addenda = comprobante33.getEltOpcAddenda();
                            }
                            else {
                                addenda = new cfd.ver33.DElementAddenda();
                                comprobante33.setEltOpcAddenda(addenda);
                            }
                            
                            for (DElement element : addenda.getElements()) {
                                if (element instanceof DElementAddendaContinentalTire) {
                                    addenda.getElements().remove(element);
                                    break;
                                }
                            }
                            
                            addenda.getElements().add(extAddenda);
                        }
                    }
                    
                    msDocXml = DCfdConsts.XML_HEADER + comprobante33.getElementForXml();
                    break;

                default:
                    throw new Exception(DLibConsts.ERR_MSG_OPTION_UNKNOWN);
            }
        }
        
        if (mbRegistryNew) {
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkDpsId + ", " +
                    "'" + msCertificateNumber + "', " +
                    "'" + msSignedText + "', " +
                    "'" + msSignature + "', " +
                    "'" + msUniqueId + "', " +
                    "'" + DLibUtils.DbmsDateFormatDatetime.format(mtDocTs) + "', " +
                    "'" + msDocXml.replaceAll("'", "''") + "', " +
                    "'" + msDocXmlRaw.replaceAll("'", "''") + "', " + 
                    "'" + msDocXmlAddenda.replaceAll("'", "''") + "', " + 
                    "'" + msDocXmlName + "', " +
                    "'" + msCancelXml.replaceAll("'", "''") + "', " + 
                    "NULL, " + 
                    mnFkXmlTypeId + ", " +
                    mnFkXmlStatusId + ", " +
                    mnFkXmlAddendaTypeId + ", " + 
                    mnFkXmlSignatureProviderId + ", " + 
                    mnFkCertificateId + ", " +
                    mnFkUserIssueId + ", " +
                    mnFkUserAnnulId + ", " +
                    "NOW()" + ", " +
                    "NOW()" + " " +
                    ")";
        }
        else {
            msSql = "UPDATE " + getSqlTable() + " SET " +
                    //"id_dps = " + mnPkDpsId + ", " +
                    "cer_num = '" + msCertificateNumber + "', " +
                    "sig_txt = '" + msSignedText + "', " +
                    "sig = '" + msSignature + "', " +
                    "uid = '" + msUniqueId + "', " +
                    "doc_ts = '" + DLibUtils.DbmsDateFormatDatetime.format(mtDocTs) + "', " +
                    "doc_xml = '" + msDocXml.replaceAll("'", "''") + "', " +
                    "doc_xml_raw = '" + msDocXmlRaw.replaceAll("'", "''") + "', " +
                    "doc_xml_add = '" + msDocXmlAddenda.replaceAll("'", "''") + "', " +
                    "doc_xml_name = '" + msDocXmlName + "', " +
                    "can_xml = '" + msCancelXml.replaceAll("'", "''") + "', " +
                    //"can_pdf_n = " + moCancelPdf_n + ", " +
                    "fk_xml_tp = " + mnFkXmlTypeId + ", " +
                    "fk_xml_st = " + mnFkXmlStatusId + ", " +
                    "fk_xml_add_tp = " + mnFkXmlAddendaTypeId + ", " +
                    "fk_xsp = " + mnFkXmlSignatureProviderId + ", " +
                    "fk_cer = " + mnFkCertificateId + " " +
                    (!mbAuxIssued ? "" : ", fk_usr_iss = " + mnFkUserIssueId + ", " +
                    "ts_usr_iss = " + "NOW()" + " ") +
                    (!mbAuxAnnulled ? "" : ", fk_usr_ann = " + mnFkUserAnnulId + ", " +
                    "ts_usr_ann = " + "NOW()" + " ") +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
        
        if (mbAuxRewriteXmlOnSave || mbAuxRegenerateXmlOnSave) {
            // XML must be regenerated, so save new XML file:
            
            DDbConfigBranch configBranch = (DDbConfigBranch) session.readRegistry(DModConsts.CU_CFG_BRA, dps.getCompanyBranchKey());
            
            DXmlUtils.writeXml(getSuitableDocXml(), configBranch.getEdsDirectory() + msDocXmlName);
        }
        
        mbRegistryNew = false;
        mnQueryResultId = DDbConsts.SAVE_OK;
    }

    @Override
    public DDbDpsEds clone() throws CloneNotSupportedException {
        DDbDpsEds registry = new DDbDpsEds();

        registry.setPkDpsId(this.getPkDpsId());
        registry.setCertificateNumber(this.getCertificateNumber());
        registry.setSignedText(this.getSignedText());
        registry.setSignature(this.getSignature());
        registry.setUniqueId(this.getUniqueId());
        registry.setDocTs(this.getDocTs());
        registry.setDocXml(this.getDocXml());
        registry.setDocXmlRaw(this.getDocXmlRaw());
        registry.setDocXmlAddenda(this.getDocXmlAddenda());
        registry.setDocXmlName(this.getDocXmlName());
        registry.setCancelXml(this.getCancelXml());
        registry.setCancelPdf_n(this.getCancelPdf_n());
        registry.setFkXmlTypeId(this.getFkXmlTypeId());
        registry.setFkXmlStatusId(this.getFkXmlStatusId());
        registry.setFkXmlAddendaTypeId(this.getFkXmlAddendaTypeId());
        registry.setFkXmlSignatureProviderId(this.getFkXmlSignatureProviderId());
        registry.setFkCertificateId(this.getFkCertificateId());
        registry.setFkUserIssuedId(this.getFkUserIssuedId());
        registry.setFkUserAnnulId(this.getFkUserAnnulId());
        registry.setTsUserIssue(this.getTsUserIssue());
        registry.setTsUserAnnul(this.getTsUserAnnul());

        registry.setAuxIssued(this.isAuxIssued());
        registry.setAuxAnnulled(this.isAuxAnnulled());
        registry.setAuxRewriteXmlOnSave(this.isAuxRewriteXmlOnSave());
        registry.setAuxRegenerateXmlOnSave(this.isAuxRegenerateXmlOnSave());
        registry.setAuxXmlSignatureRequestKey(this.getAuxXmlSignatureRequestKey() == null ? null : new int[] { this.getAuxXmlSignatureRequestKey()[0], this.getAuxXmlSignatureRequestKey()[1] });

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
