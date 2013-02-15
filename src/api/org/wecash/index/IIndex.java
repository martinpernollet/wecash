package org.wecash.index;

import java.util.Collection;
import java.util.Set;

// index par groupes:
//   mois
//   dossier
public interface IIndex<T,V> {
    public void index(Collection<V> expenses);
    public Set<T> getCategories();
    public Set<V> getValues(T category);
}
