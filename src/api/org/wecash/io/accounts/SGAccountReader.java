package org.wecash.io.accounts;

import java.text.ParseException;
import java.util.Date;

import org.apache.poi.ss.usermodel.Row;
import org.wecash.model.Expense;

//http://poi.apache.org/spreadsheet/examples.html
public class SGAccountReader extends AbstractAccountReader{
    private static final int COL_CURRENCY = 4;
    private static final int COL_VALUE = 3;
    private static final int COL_LABEL = 2;
    private static final int COL_TYPE = 1;
    private static final int COL_DATE = 0;

    public Expense readExpense(Row row) throws ParseException {
        // Cell.CELL_TYPE_STRING, Cell.CELL_TYPE_NUMERIC,
        // Cell.CELL_TYPE_FORMULA, Cell.CELL_TYPE_BOOLEAN, Cell.CELL_TYPE_ERROR
        // row.getCell(0).getCellType()
        // if(row.getCell(COL_DATE).getCellType()==Cell.CEL)
        Date date = parsetDate(row);
        String type = row.getCell(COL_TYPE).getRichStringCellValue().getString();
        String label = row.getCell(COL_LABEL).getRichStringCellValue().getString();
        double value = row.getCell(COL_VALUE).getNumericCellValue();
        String currency = row.getCell(COL_CURRENCY).getRichStringCellValue().getString();
        Expense e = new Expense(date, type, label, value);
        return e;
    }

    protected Date parsetDate(Row row) throws ParseException {
        String sd = row.getCell(COL_DATE).getRichStringCellValue().toString();
        Date date = dateFormat.parse(sd);
        return date;
    }
}
