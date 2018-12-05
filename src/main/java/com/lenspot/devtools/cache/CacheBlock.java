package com.lenspot.devtools.cache;

/**
 * This interface defines the structure of a cache block. This is the elemental
 * structure for storing the cached values. A CacheSet may use this interface to
 * store key and value pairs. Alternatively, cache set may have an internal
 * structure to store the key and value pairs.
 * 
 * @see CacheSet
 * @author Raymond Tsang
 *
 * @param <K>
 *           the type of the keys
 * @param <V>
 *           the type of the values
 */
public interface CacheBlock<K, V> {

   /**
    * Returns a key.
    * 
    * @return the key
    */
   K getKey();

   /**
    * Returns a value.
    * 
    * @return the value
    */
   V getValue();
}
