/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.gui.cat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JComboBox;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sba.lib.DLibUtils;
import sba.lib.gui.DGuiItem;
import sba.lib.xml.DXmlUtils;

/**
 *
 * @author Sergio Flores
 */
public class DXmlCatalog {
    
    static final String Catalog = "Catalog";
    public static final String XmlFileExt = ".xml";
    
    private String msName;
    private String msFilePath;
    private boolean mbApplyEntityAttribute;
    private String msBelongingAttribute;
    private String msSearchingAttributes;
    private String[] maExtraAttributes;
    private ArrayList<DXmlCatalogEntry> maEntries;
    
    /**
     * Create and load XML catalog.
     * @param name Catalog name.
     * @param filePath Catalog filepath, including file extension.
     * @throws Exception 
     */
    public DXmlCatalog(String name, String filePath) throws Exception {
        this(name, filePath, false, "", "", null);
    }
    
    /**
     * Create and load XML catalog.
     * @param name Catalog name.
     * @param filePath Catalog filepath, including file extension.
     * @param applyEntityAttributes Flag to indicate if catalog has apply-entity (for persons and organizations) attributes.
     * @throws Exception 
     */
    public DXmlCatalog(String name, String filePath, boolean applyEntityAttributes) throws Exception {
        this(name, filePath, applyEntityAttributes, "", "", null);
    }
    
    /**
     * Create and load XML catalog.
     * @param name Catalog name.
     * @param filePath Catalog filepath, including file extension.
     * @param applyEntityAttributes Flag to indicate if catalog has apply-entity (for persons and organizations) attributes.
     * @param belongingAttribute If applies, name of belonging attribute, used for grouping catalog options when needed; otherwise an empty string must be given.
     * @param searchingAttribute If applies, name of searching attribute, used for looking up catalog options when needed; otherwise an empty string must be given.
     * @param extraAttributes List of other extra attributes that the catalog may have; othersise <code>null</code> must be given.
     * @throws Exception 
     */
    public DXmlCatalog(String name, String filePath, boolean applyEntityAttributes, String belongingAttribute, String searchingAttribute, String[] extraAttributes) throws Exception {
        msName = name;
        msFilePath = filePath;
        mbApplyEntityAttribute = applyEntityAttributes;
        msBelongingAttribute = belongingAttribute;
        msSearchingAttributes = searchingAttribute;
        maExtraAttributes = extraAttributes;
        maEntries = new ArrayList<>();
        populate();
    }
    
    private void populate() throws Exception {
        String xml = DXmlUtils.readXml(msFilePath);
        Document doc = DXmlUtils.parseDocument(xml);
        NodeList nodeList = DXmlUtils.extractElements(doc, Catalog);
        Vector<Node> nodes = DXmlUtils.extractChildElements(nodeList.item(0), DXmlCatalogEntry.Entry);
        maEntries.clear();
        
        for (Node node : nodes) {
            int id = DLibUtils.parseInt(DXmlUtils.extractAttributeValue(node.getAttributes(), DXmlCatalogEntry.Id, true));
            String code = DXmlUtils.extractAttributeValue(node.getAttributes(), DXmlCatalogEntry.Code, true);
            String name = DXmlUtils.extractAttributeValue(node.getAttributes(), DXmlCatalogEntry.Name, true);
            String termStart = DXmlUtils.extractAttributeValue(node.getAttributes(), DXmlCatalogEntry.TermStart, true);
            String termEnd = DXmlUtils.extractAttributeValue(node.getAttributes(), DXmlCatalogEntry.TermEnd, false);
            Date termStartDate = DLibUtils.DbmsDateFormatDate.parse(termStart);
            Date termEndDate = termEnd.isEmpty() ? null : DLibUtils.DbmsDateFormatDate.parse(termEnd);
            boolean applyEntityPer = false;
            boolean applyEntityOrg = false;
            String belongingCode = msBelongingAttribute.isEmpty() ? "" : DXmlUtils.extractAttributeValue(node.getAttributes(), msBelongingAttribute, true);
            String searchingCode = msSearchingAttributes.isEmpty() ? "" : DXmlUtils.extractAttributeValue(node.getAttributes(), msSearchingAttributes, true);
            
            if (mbApplyEntityAttribute) {
                applyEntityPer = DXmlUtils.extractAttributeValue(node.getAttributes(), DXmlCatalogEntry.ApplyEntityPer, true).compareTo("1") == 0;
                applyEntityOrg = DXmlUtils.extractAttributeValue(node.getAttributes(), DXmlCatalogEntry.ApplyEntityOrg, true).compareTo("1") == 0;
            }
            
            DXmlCatalogEntry entry = new DXmlCatalogEntry(id, code, name, termStartDate, termEndDate, applyEntityPer, applyEntityOrg, belongingCode, searchingCode);
            
            if (maExtraAttributes != null) {
                for (String extraAttribute : maExtraAttributes) {
                    entry.getExtraAttributesMap().put(extraAttribute, DXmlUtils.extractAttributeValue(node.getAttributes(), extraAttribute, true));
                }
            }
            
            maEntries.add(entry);
        }
    }

    public String getName() {
        return msName;
    }

    public ArrayList<DXmlCatalogEntry> getEntries() {
        return maEntries;
    }
    
    public ArrayList<DGuiItem> createGuiItems() {
        ArrayList<DGuiItem> items = new ArrayList<>();
        
        items.add(new DGuiItem("- " + msName + " -"));
        
        for (DXmlCatalogEntry entry : maEntries) {
            DGuiItem item = new DGuiItem(new int[] { entry.getId() }, entry.getCode() + " - " + entry.getName());
            
            item.setCode(entry.getCode());
            item.setCodeVisible(false);
            
            if (!entry.getExtraAttributesMap().isEmpty()) {
                HashMap<String, String> attributes = new HashMap<>(entry.getExtraAttributesMap());
                item.setComplement(attributes);
            }
            
            items.add(item);
        }
        
        return items;
    }
    
    @SuppressWarnings("unchecked")
    public void populateCatalog(JComboBox comboBox) {
        comboBox.removeAllItems();
        for (DGuiItem item : createGuiItems()) {
            comboBox.addItem(item);
        }
    }
    
    public int getId(final String code) {
        return getId(code, "");
    }
    
    public int getId(final String code, final String belongCode) {
        int id = 0;
        
        for (DXmlCatalogEntry entry : maEntries) {
            if (entry.getCode().equals(code) && entry.getBelongingCode().equals(belongCode)) {
                id = entry.getId();
                break;
            }
        }
        
        return id;
    }
    
    public int getIdBySearchCode(final String searchCode) {
        return getIdBySearchCode(searchCode, "");
    }
    
    public int getIdBySearchCode(final String searchCode, final String belongCode) {
        int id = 0;
        
        for (DXmlCatalogEntry entry : maEntries) {
            if (entry.getSearchingCode().equals(searchCode) && entry.getBelongingCode().equals(belongCode)) {
                id = entry.getId();
                break;
            }
        }
        
        return id;
    }
    
    public String getCode(final int id) {
        String code = "";
        
        for (DXmlCatalogEntry entry : maEntries) {
            if (entry.getId() == id) {
                code = entry.getCode();
                break;
            }
        }
        
        return code;
    }
    
    public String getName(final int id) {
        String name = "";
        
        for (DXmlCatalogEntry entry : maEntries) {
            if (entry.getId() == id) {
                name = entry.getName();
                break;
            }
        }
        
        return name;
    }
    
    public String composeCodeName(final int id) {
        String codeName = "";
        
        for (DXmlCatalogEntry entry : maEntries) {
            if (entry.getId() == id) {
                codeName = entry.getCode() + " - " + entry.getName();
                break;
            }
        }
        
        return codeName;
    }
}
