/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sba.gui.mygrid.cell;

import java.awt.Color;
import java.awt.Component;
import java.text.NumberFormat;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import sba.lib.DLibUtils;
import sba.lib.grid.DGridConsts;
import sba.lib.grid.DGridModel;

/**
 *
 * @author Sergio Flores
 */
public class DMyGridCellRendererItemPrices extends DefaultTableCellRenderer {

    public static final Color COLOR_LESS_ZERO = new Color(255, 255, 0);
    public static final Color COLOR_LESS_PRICE_LPP = new Color(255, 153, 0);

    private NumberFormat moNumberFormat;
    private JLabel moLabel;

    public DMyGridCellRendererItemPrices(NumberFormat numberFormat) {
        moNumberFormat = numberFormat;
        moLabel = new JLabel();
        moLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        moLabel.setOpaque(true);
    }

    public void setNumberFormat(NumberFormat o) { moNumberFormat = o; }
    public void setLabel(JLabel o) { moLabel = o; }

    public NumberFormat getNumberFormat() { return moNumberFormat; }
    public JLabel getLabel() { return moLabel; }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        String sNumber = "";
        Double dNumber = 0d;
        Color background = null;
        DMyItemPrices myItemPrices = null;

        if (value == null) {
            sNumber = table.isCellEditable(row, col) ? "0" : "";
        }
        else {
            try {
                dNumber = ((Number) value).doubleValue();
                sNumber = moNumberFormat.format(dNumber);
            }
            catch (Exception e) {
                DLibUtils.showException(this, e);
            }
        }

        moLabel.setText(sNumber);

        if (isSelected) {
            if (table.isCellEditable(row, col)) {
                myItemPrices = (DMyItemPrices) ((DGridModel) table.getModel()).getGridRows().get(table.convertRowIndexToModel(row));

                if (hasFocus) {
                    background = dNumber <= 0d ? COLOR_LESS_ZERO.brighter() : (myItemPrices != null && dNumber <= (myItemPrices.getPriceLpp() * (!myItemPrices.isTaxIncluded() ? 1d : myItemPrices.getTaxRate())) ? COLOR_LESS_PRICE_LPP.brighter() : DGridConsts.COLOR_BG_SELECT_EDIT_FOCUS);
                }
                else {
                    background = dNumber <= 0d ? COLOR_LESS_ZERO.darker() : (myItemPrices != null && dNumber <= (myItemPrices.getPriceLpp() * (!myItemPrices.isTaxIncluded() ? 1d : myItemPrices.getTaxRate())) ? COLOR_LESS_PRICE_LPP.darker() : DGridConsts.COLOR_BG_SELECT_EDIT);
                }

                moLabel.setForeground(dNumber < 0 ? DGridConsts.COLOR_FG_EDIT_NEG : DGridConsts.COLOR_FG_EDIT);
                moLabel.setBackground(background);
            }
            else {
                moLabel.setForeground(dNumber < 0 ? DGridConsts.COLOR_FG_READ_NEG : DGridConsts.COLOR_FG_READ);
                moLabel.setBackground(hasFocus ? DGridConsts.COLOR_BG_SELECT_READ_FOCUS : DGridConsts.COLOR_BG_SELECT_READ);
            }
        }
        else {
            if (table.isCellEditable(row, col)) {
                myItemPrices = (DMyItemPrices) ((DGridModel) table.getModel()).getGridRows().get(table.convertRowIndexToModel(row));

                moLabel.setForeground(dNumber < 0 ? DGridConsts.COLOR_FG_EDIT_NEG : DGridConsts.COLOR_FG_EDIT);
                moLabel.setBackground(dNumber <= 0d ? COLOR_LESS_ZERO : (myItemPrices != null && dNumber <= (myItemPrices.getPriceLpp() * (!myItemPrices.isTaxIncluded() ? 1d : myItemPrices.getTaxRate())) ? COLOR_LESS_PRICE_LPP : DGridConsts.COLOR_BG_PLAIN_EDIT));
            }
            else {
                moLabel.setForeground(dNumber < 0 ? DGridConsts.COLOR_FG_READ_NEG : DGridConsts.COLOR_FG_READ);
                moLabel.setBackground(DGridConsts.COLOR_BG_PLAIN_READ);
            }
        }

        return moLabel;
    }
}
