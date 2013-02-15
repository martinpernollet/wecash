package org.wecash.io.accounts;

import java.text.ParseException;
import java.util.Date;

import org.apache.poi.ss.usermodel.Row;
import org.wecash.model.Expense;

//http://poi.apache.org/spreadsheet/examples.html
public class CAAccountReader extends AbstractAccountReader{
    private static final int COL_CREDIT = 3;
    private static final int COL_DEBIT = 2;
    private static final int COL_LABEL = 1;
    private static final int COL_DATE = 0;

    public Expense readExpense(Row row) throws ParseException {
        // Cell.CELL_TYPE_STRING, Cell.CELL_TYPE_NUMERIC,
        // Cell.CELL_TYPE_FORMULA, Cell.CELL_TYPE_BOOLEAN, Cell.CELL_TYPE_ERROR
        // row.getCell(0).getCellType()
        // if(row.getCell(COL_DATE).getCellType()==Cell.CEL)
        Date date = parseDate(row);
        String label = parseLabel(row);
        double debit = row.getCell(COL_DEBIT).getNumericCellValue();
        double credit = row.getCell(COL_CREDIT).getNumericCellValue();
        Expense e = new Expense(date, label, credit-debit);
        return e;
    }

    protected String parseLabel(Row row) {
        return row.getCell(COL_LABEL).getRichStringCellValue().getString().replace('\n', ' ');
    }

    protected Date parseDate(Row row) throws ParseException {
        String sd = row.getCell(COL_DATE).getRichStringCellValue().toString();
        int day = parseDay(sd);
        int month = parseMonth(sd);
        if(month==-1)
            throw new ParseException("can't parse date month '"+sd+"'", 0);
        return new Date(2013-1901, month, day);
    }
    protected int parseDay(String sd) {
        String num = sd.split("-")[0];
        return Integer.parseInt(num);
    }
    
    protected int parseMonth(String sd) {
        if(sd.toLowerCase().endsWith("jan"))
            return 1;
        else if(sd.toLowerCase().endsWith("fév"))
            return 2;
        else if(sd.toLowerCase().endsWith("mar"))
            return 3;
        else if(sd.toLowerCase().endsWith("avr"))
            return 4;
        else if(sd.toLowerCase().endsWith("mai"))
            return 5;
        else if(sd.toLowerCase().endsWith("juin"))
            return 6;
        else if(sd.toLowerCase().endsWith("juil"))
            return 7;
        else if(sd.toLowerCase().endsWith("aoû"))
            return 8;
        else if(sd.toLowerCase().endsWith("sep"))
            return 9;
        else if(sd.toLowerCase().endsWith("oct"))
            return 10;
        else if(sd.toLowerCase().endsWith("nov"))
            return 11;
        else if(sd.toLowerCase().endsWith("déc"))
            return 12;
        else
            return -1;
    }
}
