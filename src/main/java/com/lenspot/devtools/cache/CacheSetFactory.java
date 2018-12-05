package com.lenspot.devtools.cache;

/**
 * This interface defines how a cache set is instantiated. Each cache set
 * algorithm should provide customized implementation of the cache set data
 * structure.
 * 
 * @author Raymond Tsang
 *
 * @param <K>
 *           the type of the keys
 * @param <V>
 *           a the type of the values
 */
public interface CacheSetFactory<K, V> {

   /**
    * The implementation should return an instance of cache set. This method
    * will be called by the cache object.
    * 
    * @see Cache
    * @param numBlocks
    *           the maximum number of cache blocks.
    * @return a new instance of cache set.
    */
   CacheSet<K, V> createCacheSet(int numBlocks);
}
