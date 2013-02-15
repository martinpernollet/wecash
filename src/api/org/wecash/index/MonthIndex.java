package org.wecash.index;

import java.util.Date;
import java.util.Set;

import org.wecash.model.Expense;

public class MonthIndex extends AbstractIndex<Date,Expense> implements IIndex<Date,Expense>{
    public MonthIndex(){
        super(Utils.newDateComparator());
    }
    
    protected Set<Date> index(Expense e) {
        Date date = e.getDate();
        int m = date.getMonth();
        int y = date.getYear();
        return newSet(newDate(m,y));
    }    
}
