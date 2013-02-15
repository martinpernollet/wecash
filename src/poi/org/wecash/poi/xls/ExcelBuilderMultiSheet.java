package org.wecash.poi.xls;

import org.apache.poi.ss.usermodel.Cell;

/**
 * Override the default {@link ExcelBuilder} to write over several excel sheet
 * once a cell column exceeds the maximum number of column for excel.
 *
 * @author Martin Pernollet <mpernollet@nuxeo.com>
 */
public class ExcelBuilderMultiSheet extends ExcelBuilder {
    protected boolean multiSheetMode = false;

    public boolean isMultiSheetColumns() {
        return multiSheetMode;
    }

    /**
     * If true, the builder is able to switch on next sheets to fill cells with
     * column id > 255.
     */
    public void setMultiSheetColumns(boolean multiSheetMode) {
        this.multiSheetMode = multiSheetMode;
    }

    protected Cell getOrCreateCell(int i, int j) {
        if (multiSheetMode)
            j = autoSelectSheet(j);
        return super.getOrCreateCell(i, j);
    }

    /**
     * Only invalidate cells having a too large row id.
     *
     * {@inheritDoc}
     */
    protected boolean validateCellIndex(int row, int column, String content) {
        if (multiSheetMode) {
            if (row >= MAX_ROW) {
                String message = "max number of row (" + MAX_ROW
                        + ") exceeded @ " + row + " by '" + content + "'";
                if (CRASH_ON_CELL_OVERFLOW)
                    throw new IllegalArgumentException(message);
                else {
                    log.warn(message);
                    return false;
                }
            }
            return true;
        } else {
            return super.validateCellIndex(row, column, content);
        }
    }

    /* */

    /**
     * Return a sheet ID for the expected cell:
     * <ul>
     * <li>sheet 0 for cells [0;255]
     * <li>sheet 1 for cells [256;511]
     * <li>...
     * </ul>
     */
    public int getVirtualCellSheet(int realColumn) {
        return realColumn / MAX_COLUMN;
    }

    /**
     * Return a column ID for the expected cell:
     * <ul>
     * <li>returns 255 for realColumn=255
     * <li>returns 0 for realColumn=256
     * <li>...
     * </ul>
     */
    public int getVirtualCellColumn(int realColumn) {
        return realColumn % MAX_COLUMN;
    }

    /**
     * Compute the sheet/column required to access the given column. If expected
     * sheet is not current, change it. If it does not exist, create it.
     *
     * @return an updated column index.
     */
    protected int autoSelectSheet(int column) {
        int s = getVirtualCellSheet(column);

        // change current sheet if required
        if (s != getCurrentSheetId()) {
            if (!sheetInitialized(s)) {
                setCurrentSheetId(newSheet(s, "(" + s + ")"));
            } else {
                setCurrentSheetId(s);
            }
        }
        column = getVirtualCellColumn(column); // update J!!
        return column;
    }

}
