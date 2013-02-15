package org.wecash.operators;

import java.util.Collection;

import org.wecash.model.Expense;

public class OperatorCount implements IOperator<Expense> {
    @Override
    public double apply(Collection<Expense> expenses){
        if(expenses==null)
            return 0;
        return expenses.size();
    }
}
