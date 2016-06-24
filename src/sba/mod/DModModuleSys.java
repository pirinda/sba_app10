/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.mod;

import javax.swing.JMenu;
import sba.lib.DLibConsts;
import sba.lib.db.DDbRegistry;
import sba.lib.grid.DGridPaneView;
import sba.lib.gui.DGuiCatalogueSettings;
import sba.lib.gui.DGuiClient;
import sba.lib.gui.DGuiForm;
import sba.lib.gui.DGuiModule;
import sba.lib.gui.DGuiOptionPicker;
import sba.lib.gui.DGuiParams;
import sba.lib.gui.DGuiReport;

/**
 *
 * @author Sergio Flores
 */
public class DModModuleSys extends DGuiModule {

    public DModModuleSys(DGuiClient client) {
        super(client, DModConsts.MOD_SYS, DLibConsts.UNDEFINED);
    }

    @Override
    public JMenu[] getMenus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DDbRegistry getRegistry(final int type, final DGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DGuiCatalogueSettings getCatalogueSettings(final int type, final int subtype, final DGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DGridPaneView getView(final int type, final int subtype, final DGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DGuiOptionPicker getOptionPicker(final int type, final int subtype, final DGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DGuiForm getForm(final int type, final int subtype, final DGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DGuiReport getReport(final int type, final int subtype, final DGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
