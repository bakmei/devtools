package com.lenspot.devtools.cache;

/**
 * This interface define the cache functionalities and serve as a Facade to
 * external applications. The implementation might contains a map of indexable
 * cache sets. The cached keys should ideally be distributed evenly among the
 * cache sets. Please refer to N-way, set associated cache implementation.
 * 
 * @see CacheSet
 * 
 * @author Raymond Tsang
 *
 * @param <K>
 *           the type of the keys
 * @param <V>
 *           the type of the values
 */
public interface Cache<K, V> {
   /**
    * Returns the value which is associated with the key.
    * 
    * @param k
    *           a key
    * @return the value which is associated with the key.
    */
   public V getValue(K k) throws CacheException;

   /**
    * Set a key and a value for future access.
    * 
    * @param k
    *           a key
    * @param v
    *           a value which is associated with the key
    */
   public void setValue(K k, V v) throws CacheException;
}
