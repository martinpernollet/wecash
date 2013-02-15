package org.wecash.operators;

import java.util.Collection;

import org.wecash.model.Expense;

public class OperatorSum implements IOperator<Expense>{
    public double apply(Collection<Expense> expenses){
        if(expenses==null)
            return 0;
        double sum = 0;
        for(Expense e: expenses){
            sum += e.getValue();
        }
        return sum;
    }
}
