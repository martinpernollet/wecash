package org.wecash.index;

import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

public abstract class AbstractIndex<K,V> implements IIndex<K,V>{
    public AbstractIndex() {
    }
    public AbstractIndex(Comparator<K> indexKeyComparator) {
        this.comparator = indexKeyComparator;
    }

    protected void beginIndex(){
        index = HashMultimap.create();
        if(comparator!=null)
            categories = new TreeSet<K>(comparator);
        else
            categories = new TreeSet<K>();
    }
    
    @Override
    public void index(Collection<V> expenses) {
        beginIndex();
        
        for(V e: expenses){
            Set<K> idx = index(e);
            for(K id: idx){
                index.put(id, e);
                categories.add(id);
            }
        }
    }

    /** Returns all keys that can index the input.*/
    protected abstract Set<K> index(V e);

    protected Set<K> newSet(K key) {
        Set<K> indices = new HashSet<K>();
        indices.add(key);
        return indices;
    }
    
    protected Date newDate(int m, int y){
        Date d = new Date(y,m,1);
        return d;
    }
    

    @Override
    public Set<K> getCategories() {
        return categories;
    }
    
    @Override
    public Set<V> getValues(K category) {
        return index.get(category);
    }
    
    public void console(){
        for(K month: getCategories()){
            System.out.println(month);
            for(V e: getValues(month))
                System.out.println("  " + e);
        }
    }

    protected Comparator<K> comparator;
    protected TreeSet<K> categories;
    protected SetMultimap<K,V> index;
}
