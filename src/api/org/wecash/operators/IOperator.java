package org.wecash.operators;

import java.util.Collection;

public interface IOperator<TValue> {
    public double apply(Collection<TValue> expenses);
}