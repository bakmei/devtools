package com.lenspot.devtools.cache;

import java.util.List;

/**
 * This interface provides the functionalities for a cache set. A cache set
 * should maintains a list of cache blocks for storing the cached values. This
 * interface provides ways for the cache algorithm to manipulate the order of
 * cache blocks.
 * 
 * @see CacheBlock
 * 
 * @author Raymond Tsang
 *
 * @param <K>
 *           the type of the keys
 * @param <V>
 *           a the type of the values
 */
public interface CacheSet<K, V> {
   /**
    * Returns a cached value.
    * 
    * @param k
    *           a key for looking the value
    * @return the value
    * @exception throws CacheException if the operation is failed.
    */
   public V getValue(K k) throws CacheException;

   /**
    * Set a key and a value for future access.
    * 
    * @param k
    *           a key
    * @param v
    *           a value which is associated with the key
    * @exception throws CacheException if the operation is failed.
    */
   public void setValue(K k, V v) throws CacheException;

   /**
    * Returns a list of Cache blocks. The list will be in sorted accordingly to
    * the current algorithm.
    * 
    * @return a list of cache blocks
    */
   public List<CacheBlock<K, V>> getCacheBlockList();

   /**
    * Returns the size of the cache blocks stored in the cache set.
    * 
    * @return an integer
    */
   public int size();

}
