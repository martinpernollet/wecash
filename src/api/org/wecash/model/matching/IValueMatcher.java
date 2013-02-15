package org.wecash.model.matching;

import org.wecash.model.Expense;

public interface IValueMatcher {
    public boolean accepts(Expense e);
}
