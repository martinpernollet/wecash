package org.wecash.model.treematrix;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.wecash.model.Category;
import org.wecash.model.Expense;
import org.wecash.poi.treematrix.ITreeMatrixItem;

public class CategoryDateTreeMatrixItem implements ITreeMatrixItem<Category, Date, Set<Expense>> {
    protected Category category;
    protected Map<Date, Set<Expense>> dates;
    protected int depth = -1;
    
    public CategoryDateTreeMatrixItem(Category category) {
        this(category,new HashMap<Date,Set<Expense>>());
    }
    
    Map<Date,Set<Expense>> map = new HashMap<Date,Set<Expense>>();

    public CategoryDateTreeMatrixItem(Category category, Map<Date, Set<Expense>> dates) {
        this.category = category;
        this.dates = dates;
    }

    @Override
    public String getLabel(){
        return category.getName();
    }
    
    @Override
    public Category getTreeItem() {
        return category;
    }

    @Override
    public String getPath() {
        return category.getPath();
    }

    @Override
    public Set<Expense> getValueFor(Date key2) {
        return dates.get(key2);
    }

    @Override
    public int compareTo(ITreeMatrixItem<Category,Date, Set<Expense>> arg0) {
        return getPath().compareTo(arg0.getPath());
    }

    public int getDepth() {
        if(depth==-1)
            depth = countOccurrences(getPath(), '/');
        return depth;
    }

    public static int countOccurrences(String haystack, char needle) {
        int count = 0;
        for (int i = 0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == needle) {
                count++;
            }
        }
        return count;
    }

 
}
