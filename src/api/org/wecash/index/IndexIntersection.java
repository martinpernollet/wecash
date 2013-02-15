package org.wecash.index;

import java.util.HashSet;
import java.util.Set;

import org.jzy3d.maths.Pair;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

public class IndexIntersection<K1, K2, V> {
    public IndexIntersection(IIndex<K1, V> index1, IIndex<K2, V> index2) {
        this.index1 = index1;
        this.index2 = index2;
    }

    public void build() {
        intersections = HashMultimap.create();
        for (K1 key1 : index1.getCategories()) {
            for (K2 key2 : index2.getCategories()) {
                Set<V> values = computeIntersection(key1, key2);
                intersections.putAll(new Pair<K1, K2>(key1, key2), values);
            }
        }
    }

    /**
     * Return the values that are indexed by key1 in first index and key2 in
     * second index.
     */
    public Set<V> computeIntersection(K1 key1, K2 key2) {
        Set<V> v1 = new HashSet<V>(index1.getValues(key1));
        Set<V> v2 = index2.getValues(key2);
        v1.retainAll(v2);
        return v1;
    }
    
    public Set<V> getIndexedIntersection(K1 k1, K2 k2) {
        return intersections.get(new Pair<K1, K2>(k1, k2));
    }

    public SetMultimap<Pair<K1, K2>, V> getIndexedIntersections(){
        return intersections;
    }

    public IIndex<K1, V> getIndex1() {
        return index1;
    }

    public IIndex<K2, V> getIndex2() {
        return index2;
    }

    public void console() {
        for (K1 k1 : getIndex1().getCategories()) {
            System.out.println(k1);
            for (K2 k2 : getIndex2().getCategories()) {
                System.out.println("  " + k2);
                for (V e : getIndexedIntersection(k1, k2))
                    System.out.println("   " + e);
            }
        }
    }

    protected IIndex<K1, V> index1;
    protected IIndex<K2, V> index2;
    protected SetMultimap<Pair<K1, K2>, V> intersections;

    public class Intersection {
        public Intersection(K1 key1, K2 key2, Set<V> value) {
            this.key1 = key1;
            this.key2 = key2;
            this.value = value;
        }

        public K1 getKey1() {
            return key1;
        }

        public K2 getKey2() {
            return key2;
        }

        public Set<V> getValue() {
            return value;
        }

        K1 key1;
        K2 key2;
        Set<V> value;
    }
}
