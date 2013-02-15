package org.wecash.model.treematrix;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.wecash.model.Category;
import org.wecash.model.Expense;
import org.wecash.poi.treematrix.ITreeMatrix;

public class CategoryDateTreeMatrix implements ITreeMatrix<Category, Date, Set<Expense>> {
    public CategoryDateTreeMatrix(TreeSet<CategoryDateTreeMatrixItem> tree, List<Date> columns) {
        this.tree = tree;
        this.columns = columns;
    }

    public TreeSet<CategoryDateTreeMatrixItem> getTree() {
        return tree;
    }

    public List<Date> getColumns() {
        return columns;
    }

    public void console() {
        StringBuffer sb = new StringBuffer();
        toString(sb);
        System.out.println(sb.toString());
    }

    public void toString(StringBuffer sb) {
        for (CategoryDateTreeMatrixItem i : getTree()) {
            sb.append(i.getPath() + "\n");
            for (Date d : columns) {
                sb.append(" " + d + "\n");

                Set<Expense> ee = i.getValueFor(d);
                if (ee != null) {
                    for (Expense e : ee)
                        sb.append("   " + e + "\n");
                }
            }
        }
    }
    
    /**
     * fluent method that returns its input argument completed with
     * text reporting lines
     */
    public List<String> toString(List<String> lines) {
        for (CategoryDateTreeMatrixItem i : getTree()) {
            lines.add(i.getPath());
            for (Date d : columns) {
                lines.add(" " + d);

                Set<Expense> ee = i.getValueFor(d);
                if (ee != null) {
                    for (Expense e : ee)
                        lines.add("   " + e);
                }
            }
        }
        return lines;
    }

    protected TreeSet<CategoryDateTreeMatrixItem> tree;
    protected List<Date> columns;
}
