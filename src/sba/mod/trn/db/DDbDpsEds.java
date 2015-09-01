/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod.trn.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import sba.gui.util.DUtilConsts;
import sba.lib.DLibConsts;
import sba.lib.DLibUtils;
import sba.lib.db.DDbConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

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
    protected String msDocXmlName;
    protected String msCancelXml;
    protected java.sql.Blob moCancelPdf_n;
    protected int mnFkXmlTypeId;
    protected int mnFkXmlStatusId;
    protected int mnFkXmlSignatureProviderId;
    protected int mnFkCertificateId;
    protected int mnFkUserIssueId;
    protected int mnFkUserAnnulId;
    protected Date mtTsUserIssue;
    protected Date mtTsUserAnnul;

    protected boolean mbAuxIssued;
    protected boolean mbAuxAnnulled;
    protected int[] manAuxXmlSignatureRequestKey;

    public DDbDpsEds() {
        super(DModConsts.T_DPS_EDS);
        initRegistry();
    }

    public void setPkDpsId(int n) { mnPkDpsId = n; }
    public void setCertificateNumber(String s) { msCertificateNumber = s; }
    public void setSignedText(String s) { msSignedText = s; }
    public void setSignature(String s) { msSignature = s; }
    public void setUniqueId(String s) { msUniqueId = s; }
    public void setDocTs(Date t) { mtDocTs = t; }
    public void setDocXml(String s) { msDocXml = s; }
    public void setDocXmlRaw(String s) { msDocXmlRaw = s; }
    public void setDocXmlName(String s) { msDocXmlName = s; }
    public void setCancelXml(String s) { msCancelXml = s; }
    public void setCancelPdf_n(java.sql.Blob o) { moCancelPdf_n = o; }
    public void setFkXmlTypeId(int n) { mnFkXmlTypeId = n; }
    public void setFkXmlStatusId(int n) { mnFkXmlStatusId = n; }
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
    public String getDocXmlName() { return msDocXmlName; }
    public String getCancelXml() { return msCancelXml; }
    public java.sql.Blob getCancelPdf_n() { return moCancelPdf_n; }
    public int getFkXmlTypeId() { return mnFkXmlTypeId; }
    public int getFkXmlStatusId() { return mnFkXmlStatusId; }
    public int getFkXmlSignatureProviderId() { return mnFkXmlSignatureProviderId; }
    public int getFkCertificateId() { return mnFkCertificateId; }
    public int getFkUserIssuedId() { return mnFkUserIssueId; }
    public int getFkUserAnnulId() { return mnFkUserAnnulId; }
    public Date getTsUserIssue() { return mtTsUserIssue; }
    public Date getTsUserAnnul() { return mtTsUserAnnul; }

    public void setAuxIssued(boolean b) { mbAuxIssued = b; }
    public void setAuxAnnulled(boolean b) { mbAuxAnnulled = b; }
    public void setAuxXmlSignatureRequestKey(int[] key) { manAuxXmlSignatureRequestKey = key; }

    public boolean isAuxIssued() { return mbAuxIssued; }
    public boolean isAuxAnnulled() { return mbAuxAnnulled; }
    public int[] getAuxXmlSignatureRequestKey() { return manAuxXmlSignatureRequestKey; }

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
        msDocXmlName = "";
        msCancelXml = "";
        moCancelPdf_n = null;
        mnFkXmlTypeId = 0;
        mnFkXmlStatusId = 0;
        mnFkXmlSignatureProviderId = 0;
        mnFkCertificateId = 0;
        mnFkUserIssueId = 0;
        mnFkUserAnnulId = 0;
        mtTsUserIssue = null;
        mtTsUserAnnul = null;

        mbAuxIssued = false;
        mbAuxAnnulled = false;
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
            msDocXmlName = resultSet.getString("doc_xml_name");
            msCancelXml = resultSet.getString("can_xml");
            moCancelPdf_n = resultSet.getBlob("can_pdf_n");
            mnFkXmlTypeId = resultSet.getInt("fk_xml_tp");
            mnFkXmlStatusId = resultSet.getInt("fk_xml_st");
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

        if (mbRegistryNew) {
            msSql = "INSERT INTO " + getSqlTable() + " VALUES (" +
                    mnPkDpsId + ", " +
                    "'" + msCertificateNumber + "', " +
                    "'" + msSignedText + "', " +
                    "'" + msSignature + "', " +
                    "'" + msUniqueId + "', " +
                    "'" + DLibUtils.DbmsDateFormatDatetime.format(mtDocTs) + "', " +
                    "'" + msDocXml + "', " +
                    "'" + msDocXmlRaw + "', " + 
                    "'" + msDocXmlName + "', " +
                    "'" + msCancelXml + "', " + 
                    "NULL, " + 
                    mnFkXmlTypeId + ", " +
                    mnFkXmlStatusId + ", " +
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
                    "doc_xml = '" + msDocXml + "', " +
                    "doc_xml_raw = '" + msDocXmlRaw + "', " +
                    "doc_xml_name = '" + msDocXmlName + "', " +
                    "can_xml = '" + msCancelXml + "', " +
                    //"can_pdf_n = " + moCancelPdf_n + ", " +
                    "fk_xml_tp = " + mnFkXmlTypeId + ", " +
                    "fk_xml_st = " + mnFkXmlStatusId + ", " +
                    "fk_xsp = " + mnFkXmlSignatureProviderId + ", " +
                    "fk_cer = " + mnFkCertificateId + " " +
                    (!mbAuxIssued ? "" : ", fk_usr_iss = " + mnFkUserIssueId + ", " +
                    "ts_usr_iss = " + "NOW()" + " ") +
                    (!mbAuxAnnulled ? "" : ", fk_usr_ann = " + mnFkUserAnnulId + ", " +
                    "ts_usr_ann = " + "NOW()" + " ") +
                    getSqlWhere();
        }

        session.getStatement().execute(msSql);
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
        registry.setDocXmlName(this.getDocXmlName());
        registry.setCancelXml(this.getCancelXml());
        registry.setCancelPdf_n(this.getCancelPdf_n());
        registry.setFkXmlTypeId(this.getFkXmlTypeId());
        registry.setFkXmlStatusId(this.getFkXmlStatusId());
        registry.setFkXmlSignatureProviderId(this.getFkXmlSignatureProviderId());
        registry.setFkCertificateId(this.getFkCertificateId());
        registry.setFkUserIssuedId(this.getFkUserIssuedId());
        registry.setFkUserAnnulId(this.getFkUserAnnulId());
        registry.setTsUserIssue(this.getTsUserIssue());
        registry.setTsUserAnnul(this.getTsUserAnnul());

        registry.setAuxIssued(this.isAuxIssued());
        registry.setAuxAnnulled(this.isAuxAnnulled());
        registry.setAuxXmlSignatureRequestKey(this.getAuxXmlSignatureRequestKey() == null ? null : new int[] { this.getAuxXmlSignatureRequestKey()[0], this.getAuxXmlSignatureRequestKey()[1] });

        registry.setRegistryNew(this.isRegistryNew());
        return registry;
    }
}
