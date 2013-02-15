package org.wecash.poi.treematrix;

import java.util.List;
import java.util.TreeSet;

public interface ITreeMatrix<TNode, TColumn, TValue> {
    public TreeSet<? extends ITreeMatrixItem<TNode, TColumn, TValue>> getTree();

    public List<TColumn> getColumns();
}
