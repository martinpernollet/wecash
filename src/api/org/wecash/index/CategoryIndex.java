package org.wecash.index;

import java.util.HashSet;
import java.util.Set;

import org.wecash.model.Category;
import org.wecash.model.Expense;

// an entry may match several patterns
// TODO: disambiguate
public class CategoryIndex extends AbstractIndex<Category,Expense> implements IIndex<Category,Expense>{
    protected Set<Category> patterns;
    protected Category unclassified = new Category("#unclassified");

    public CategoryIndex(Set<Category> patterns) {
        this.patterns = patterns;
    }
    
    @Override
    protected Set<Category> index(Expense e) {
        Set<Category> indices = new HashSet<Category>();
        for(Category categories: patterns){
            if(categories.accepts(e)){
                indices.add(categories);
            }
        }
        if(indices.size()==0)
            indices.add(unclassified);
        return indices;
    }
}
