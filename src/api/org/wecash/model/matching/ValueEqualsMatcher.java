package org.wecash.model.matching;

import org.wecash.model.Expense;

public class ValueEqualsMatcher implements IValueMatcher{
    protected double expected;
    
    public ValueEqualsMatcher(double expected) {
        this.expected = expected;
    }

    @Override
    public boolean accepts(Expense e) {
        if(e.getValue()==expected)
            return true;
        else
            return false;
    }
}
