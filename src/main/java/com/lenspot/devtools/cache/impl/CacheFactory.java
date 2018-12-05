package com.lenspot.devtools.cache.impl;

import com.google.common.base.Preconditions;
import com.lenspot.devtools.cache.Cache;
import com.lenspot.devtools.cache.CacheSetFactory;

/**
 * Factory for creating cache object instances.
 * 
 * @author Raymond Tsang
 *
 * @param <K>
 *           the type of the keys
 * @param <V>
 *           the type of the values
 */
public class CacheFactory<K, V> {

   /**
    * Create an instance of the cache object and set algorithm to LRU.
    * 
    * @param numSets
    *           the number of cache sets
    * @param numBlocks
    *           the number of cache blocks per cache set.
    * @return an instance of cache object
    */
   public Cache<K, V> createNWayLRUCache(int numSets, int numBlocks) {
      return createNWaySetAssociatedCache(numSets, numBlocks,
            new LRUCacheAlgorithm<K, V>());   }

   /**
    * Create an instance of the cache object and set algorithm to MRU.
    * 
    * @param numSets
    *           the number of cache sets
    * @param numBlocks
    *           the number of cache blocks per cache set.
    * @return an instance of cache object
    */
   public static <K, V> Cache<K, V> createNWayMRUCache(int numSets,
         int numBlocks) {
      return createNWaySetAssociatedCache(numSets, numBlocks,
            new MRUCacheAlgorithm<K, V>());
   }

   /**
    * Create an instance of the cache object by a given cache set.
    * 
    * @param numSets
    *           the number of the cache sets
    * @param numBlocks
    *           the number of cache blocks per cache set.
    * @return an instance of cache object
    */
   public static <K, V> Cache<K, V> createNWaySetAssociatedCache(int numSets,
         int numBlocks, CacheSetFactory<K, V> cacheSetFactory) {
      Preconditions.checkState(numSets > 0);
      Preconditions.checkState(numBlocks > 0);
      return new DefaultCacheImpl<K, V>(numSets, numBlocks, cacheSetFactory);
   }

}
