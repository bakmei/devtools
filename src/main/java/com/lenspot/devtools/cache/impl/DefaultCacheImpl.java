package com.lenspot.devtools.cache.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.lenspot.devtools.cache.Cache;
import com.lenspot.devtools.cache.CacheBlock;
import com.lenspot.devtools.cache.CacheException;
import com.lenspot.devtools.cache.CacheSet;
import com.lenspot.devtools.cache.CacheSetFactory;

/**
 * The default implementation for N-way, set associated cache.
 * 
 * @author Raymond Tsang
 *
 * @param <K>
 *           the type of the keys
 * @param <V>
 *           the type of the values
 */
class DefaultCacheImpl<K, V> implements Cache<K, V> {
   final static Logger logger = LoggerFactory.getLogger(DefaultCacheImpl.class);

   // the number of cache sets
   private int nSets = 100;

   // the number of cache bocks per each cache set
   private int nBlocks = 4;

   // map to hold the cache set, indexed by an integer which is converted from
   // the hash code of the key.
   // @see CacheUtil.getCacheSetIndex
   private Map<Integer, CacheSet<K, V>> cacheSetMap = null;

   private CacheSetFactory<K, V> cacheSetFactory = null;

   // a handle of the cache algorithm
   // private CacheAlgorithm<K, V> algorithm = null;

   /**
    * Default constructor.
    * 
    * @param nSets
    *           the number of cache sets will be allocated
    * @param nBlocks
    *           the number of cache blocks will be allocated for each cache set
    * @param alg
    *           the cache algorithm to optimize the order of cache blocks
    */
   public DefaultCacheImpl(int nSets, int nBlocks,
         CacheSetFactory<K, V> cacheSetFactory) {
      Preconditions.checkState(nSets > 0);
      Preconditions.checkState(nBlocks > 0);
      Preconditions.checkNotNull(cacheSetFactory);
      this.nSets = nSets;
      this.nBlocks = nBlocks;
      this.cacheSetFactory = cacheSetFactory;
      this.cacheSetMap = new HashMap<Integer, CacheSet<K, V>>(nSets);
      if (logger.isDebugEnabled()) {
         logger.debug("Initializing cache with cache sets=" + nSets
               + " cache blocks=" + nBlocks);
      }
   }

   /**
    * 
    */
   public V getValue(K k) throws CacheException {
      if (logger.isDebugEnabled()) {
         logger.debug("Getting value - key: " + k);
      }
      V ret = null;
      try {
         int setIndex = CacheUtil.getCacheSetIndex(k, this.nSets);
         if (this.cacheSetMap.containsKey(setIndex)) {
            CacheSet<K, V> set = this.cacheSetMap.get(setIndex);
            if (set != null) {
               ret = set.getValue(k);
            }
         }
      } catch (Exception ex) {
         logger.error("Failed to get value from cache set using key: "
               + k.toString());
         throw new CacheException(ex);
      }
      return ret;
   }

   /**
    * 
    */
   public void setValue(K k, V v) throws CacheException {
      if (logger.isDebugEnabled()) {
         logger.debug("Setting value - key: " + k + " value: " + v);
      }
      try {
         int setIndex = CacheUtil.getCacheSetIndex(k, this.nSets);
         CacheSet<K, V> set = this.getOrCreateCacheSet(setIndex);
         set.setValue(k, v);
      } catch (Exception ex) {
         logger.error("Failed to set value from cache set using key: "
               + k.toString());
         throw new CacheException(ex);
      }
   }

   /**
    * Return an existed cache set or create on if there is none.
    * 
    * @param setIndex
    *           a cache set index
    * @return a cache set associated with the index
    */
   private CacheSet<K, V> getOrCreateCacheSet(int setIndex) {
      CacheSet<K, V> ret = null;
      if (this.cacheSetMap.containsKey(setIndex)) {
         if (logger.isDebugEnabled()) {
            logger.debug("Using existing cache set - index: " + setIndex);
         }
         ret = this.cacheSetMap.get(setIndex);
      } else {
         if (logger.isDebugEnabled()) {
            logger.debug("Creating new cache set - index: " + setIndex);
         }
         ret = this.cacheSetFactory.createCacheSet(this.nBlocks);
         this.cacheSetMap.put(setIndex, ret);
      }
      return ret;
   }

   public void dump() {
      if (logger.isDebugEnabled()) {
         if (this.cacheSetMap != null && this.cacheSetMap.size() > 0) {
            for (Entry<Integer, CacheSet<K, V>> e : this.cacheSetMap.entrySet()) {
               logger.info("Cache Set: #" + e.getKey());
               CacheSet<K, V> cSet = e.getValue();
               if (cSet != null) {
                  List<CacheBlock<K, V>> blockList = cSet.getCacheBlockList();
                  if (blockList != null) {
                     for (CacheBlock<K, V> b : blockList) {
                        logger.debug("\t key: " + b.getKey() + " value: "
                              + b.getValue());
                     }
                  }
               }
            }
         }
      }
   }

}
