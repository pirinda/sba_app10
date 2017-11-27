/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.gui.cat;

import java.util.ArrayList;
import java.util.Date;
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
    private boolean mbApplyEntity;
    private ArrayList<DXmlCatalogEntry> maEntries;
    
    public DXmlCatalog(String name, String file) throws Exception {
        this(name, file, false);
    }
    
    public DXmlCatalog(String name, String filePath, boolean isApplyEntity) throws Exception {
        msName = name;
        msFilePath = filePath;
        mbApplyEntity = isApplyEntity;
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
            
            if (mbApplyEntity) {
                applyEntityPer = DXmlUtils.extractAttributeValue(node.getAttributes(), DXmlCatalogEntry.ApplyEntityPer, true).compareTo("1") == 0;
                applyEntityOrg = DXmlUtils.extractAttributeValue(node.getAttributes(), DXmlCatalogEntry.ApplyEntityOrg, true).compareTo("1") == 0;
            }
            
            maEntries.add(new DXmlCatalogEntry(id, code, name, termStartDate, termEndDate, applyEntityPer, applyEntityOrg));
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
            items.add(item);
        }
        
        return items;
    }
    
    public void populateCatalog(JComboBox comboBox) {
        comboBox.removeAllItems();
        for (DGuiItem item : createGuiItems()) {
            comboBox.addItem(item);
        }
    }
    
    public int getId(final String code) {
        int id = 0;
        
        for (DXmlCatalogEntry entry : maEntries) {
            if (entry.getCode().compareTo(code) == 0) {
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
