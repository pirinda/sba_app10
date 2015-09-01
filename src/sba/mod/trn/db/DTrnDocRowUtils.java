/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sba.mod.trn.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import sba.lib.DLibUtils;
import sba.lib.db.DDbRegistry;
import sba.lib.gui.DGuiSession;
import sba.mod.DModConsts;

/**
 *
 * @author Sergio Flores
 */
public abstract class DTrnDocRowUtils {

    public static boolean validateRowIn(final DGuiSession session, final int year, final DTrnDocRow rowToValidate) throws Exception {
        double stock = 0;

        for (DTrnStockMove move : rowToValidate.getStockMoves()) {
            if (move.getSerialNumber().isEmpty()) {
                // Incoming lots do not require validation.
            }
            else {
                // Validate incoming serial numbers:

                stock = DTrnUtils.getStockForLotSerialNumber(session, year, move.getLotKey(), move.getWarehouseKey(), move.getSerialNumber());
                if (stock != 0) {
                    throw new Exception("No se puede modificar el documento porque en la partida # " + rowToValidate.getRowNumber() + " " +
                            "para el ítem '" + rowToValidate.getItemName() + "',\n" +
                            "con número de serie '" + move.getSerialNumber() + "',\n" +
                            "ya hay " + DLibUtils.getDecimalFormatQuantity().format(stock) + " " + rowToValidate.getUnitCode() + " en existencia en el almacén " +
                            "'" + session.readField(DModConsts.CU_WAH, move.getWarehouseKey(), DDbRegistry.FIELD_NAME) + "'.");
                }
            }
        }

        return true;
    }

    public static boolean validateRowOut(final DGuiSession session, final int year, final DTrnDocRow rowToValidate, Vector<DTrnDocRow> rowsInDoc) throws Exception {
        int moves = 0;
        double stock = 0;
        double quantity = 0;
        boolean importDeclaration = false;

        for (DTrnStockMove move : rowToValidate.getStockMoves()) {
            if (move.getSerialNumber().isEmpty()) {
                // Validate outgoing lots for current move:

                if (move.getImportDeclaration().isEmpty()) {
                    stock = DTrnUtils.getStockForLot(session, year, move.getLotKey(), move.getWarehouseKey());
                }
                else {
                    importDeclaration = true;
                    stock = DTrnUtils.getStockForLotImportDeclaration(session, year, move.getLotKey(), move.getWarehouseKey(), move.getImportDeclaration(), move.getImportDeclarationDate());
                }

                if (move.getQuantity() > stock) {
                    throw new Exception("No se puede modificar el documento porque en la partida # " + rowToValidate.getRowNumber() + " " +
                            "el ítem '" + rowToValidate.getItemName() + "',\n" +
                            (!importDeclaration ? "" : "con pedimento: '" + move.getImportDeclaration() + "' del '" + DLibUtils.DateFormatDate.format(move.getImportDeclarationDate()) + "',\n") +
                            "requiere de " + DLibUtils.getDecimalFormatQuantity().format(move.getQuantity()) + " " + rowToValidate.getUnitCode() + ", " +
                            "pero hay " + DLibUtils.getDecimalFormatQuantity().format(stock) + " " + rowToValidate.getUnitCode() + " en existencia en el almacén " +
                            "'" + session.readField(DModConsts.CU_WAH, move.getWarehouseKey(), DDbRegistry.FIELD_NAME) + "'.");
                }
                else {
                    // Validate aswell outgoing lots for the same item on all document moves (serial numbers are discarted):

                    moves = 0;
                    quantity = 0;
                    for (DTrnDocRow row : rowsInDoc) {
                        if (!importDeclaration) {
                            for (DTrnStockMove moveAux : row.getStockMoves()) {
                                if (DLibUtils.compareKeys(move.getLotKey(), moveAux.getLotKey()) &&
                                        moveAux.getSerialNumber().isEmpty()) {
                                    moves++;
                                    quantity += moveAux.getQuantity();
                                }
                            }
                        }
                        else {
                            for (DTrnStockMove moveAux : row.getStockMoves()) {
                                if (DLibUtils.compareKeys(move.getLotKey(), moveAux.getLotKey()) &&
                                        moveAux.getSerialNumber().isEmpty() &&
                                        move.getImportDeclaration().compareTo(moveAux.getImportDeclaration()) == 0 &&
                                        move.getImportDeclarationDate().compareTo(moveAux.mtImportDeclarationDate) == 0) {
                                    moves++;
                                    quantity += moveAux.getQuantity();
                                }
                            }
                        }
                    }

                    if (moves > 1 && quantity > stock) {
                        throw new Exception("No se puede modificar el documento porque en " + moves + " " + (moves == 1 ? "movimiento " : "movimientos " ) +
                                "el ítem '" + rowToValidate.getItemName() + "',\n" +
                                (!importDeclaration ? "" : "con pedimento: '" + move.getImportDeclaration() + "' del '" + DLibUtils.DateFormatDate.format(move.getImportDeclarationDate()) + "',\n") +
                                "requiere de " + DLibUtils.getDecimalFormatQuantity().format(quantity) + " " + rowToValidate.getUnitCode() + ", " +
                                "pero hay " + DLibUtils.getDecimalFormatQuantity().format(stock) + " " + rowToValidate.getUnitCode() + " en existencia en el almacén " +
                                "'" + session.readField(DModConsts.CU_WAH, move.getWarehouseKey(), DDbRegistry.FIELD_NAME) + "'.");
                    }
                }
            }
            else {
                // Validate outgoing serial numbers:

                if (move.getImportDeclaration().isEmpty()) {
                    stock = DTrnUtils.getStockForLotSerialNumber(session, year, move.getLotKey(), move.getWarehouseKey(), move.getSerialNumber());
                }
                else {
                    importDeclaration = true;
                    stock = DTrnUtils.getStockForLotSerialNumberImportDeclaration(session, year, move.getLotKey(), move.getWarehouseKey(), move.getSerialNumber(), move.getImportDeclaration(), move.getImportDeclarationDate());
                }

                if (stock <= 0) {
                    throw new Exception("No se puede modificar el documento porque en la partida # " + rowToValidate.getRowNumber() + " " +
                            "para el ítem '" + rowToValidate.getItemName() + "',\n" +
                            "con número de serie '" + move.getSerialNumber() + "',\n" +
                            (!importDeclaration ? "" : "con pedimento: '" + move.getImportDeclaration() + "' del '" + DLibUtils.DateFormatDate.format(move.getImportDeclarationDate()) + "',\n") +
                            "no hay existencias en el almacén " +
                            "'" + session.readField(DModConsts.CU_WAH, move.getWarehouseKey(), DDbRegistry.FIELD_NAME) + "'.");
                }
            }
        }

        return true;
    }

    public static ArrayList<DTrnImportDeclaration> getDpsRowImportDeclarations(final DGuiSession session, final int[] keyDpsRow) throws SQLException, Exception {
        String sql = "";
        ResultSet resultSet = null;
        ArrayList<DTrnImportDeclaration> importDeclarations = new ArrayList<DTrnImportDeclaration>();

        sql = "SELECT DISTINCT imp_dec, imp_dec_dt_n " +
                "FROM " + DModConsts.TablesMap.get(DModConsts.T_STK) + " " +
                "WHERE fk_dps_inv_dps_n = " + keyDpsRow[0] + " AND fk_dps_inv_row_n = " + keyDpsRow[1] + " AND imp_dec <> '' AND b_del = 0 " +
                "ORDER BY imp_dec_dt_n, imp_dec ";
        resultSet = session.getStatement().executeQuery(sql);
        while (resultSet.next()) {
            importDeclarations.add(new DTrnImportDeclaration(resultSet.getString(1), resultSet.getDate(2)));
        }

        return importDeclarations;
    }
}
