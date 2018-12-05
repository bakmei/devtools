package com.lenspot.devtools.cache.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lenspot.devtools.cache.CacheBlock;
import com.lenspot.devtools.cache.CacheException;
import com.lenspot.devtools.cache.CacheSet;
import com.lenspot.devtools.cache.CacheSetFactory;

/**
 * This class implements both CacheSet and CacheSetFactory interface. The
 * algorithm LRU (Least Recently Used) is used for this implementation.
 * 
 * @link https://docs.oracle.com/javase/7/docs/api/java/util/LinkedHashMap.html
 * 
 * @author Raymond Tsang
 *
 * @param <K>
 *           the type of the keys
 * @param <V>
 *           the type of the values
 */
public class LRUCacheAlgorithm<K, V> implements CacheSet<K, V>,
      CacheSetFactory<K, V> {
   // logger
   final static Logger logger = LoggerFactory
         .getLogger(LRUCacheAlgorithm.class);

   // stores all the data elements: key and value pair.
   private LinkedHashMap<K, V> cacheSet = null;

   /**
    * Factory method
    */
   public LRUCacheAlgorithm() {
   }

   /**
    * For new instance of cache set.
    * 
    * @param numBlocks
    */
   LRUCacheAlgorithm(int numBlocks) {
      final int capacity = numBlocks + 1; // +1 because LinkedHashMap performs
                                          // add before remove.
      float loadFactor = 1.1f; // ensures the rehashing mechanism of the
                               // underlying HashMap class isn't triggered.
      boolean isAccessOrder = true; // the ordering mode - true for
                                    // access-order, false for insertion-order
      
      if (logger.isDebugEnabled()) {
      logger.debug("Creating cache set: capacity=" + capacity + " load factor="
            + loadFactor + " access order=" + isAccessOrder);
      }
      
      this.cacheSet = new LinkedHashMap<K, V>(capacity, loadFactor,
            isAccessOrder) {
         private static final long serialVersionUID = 1L;

         // override the behavior of the method to remove the oldest
         // element from the list when the size is full.
         protected boolean removeEldestEntry(Entry<K, V> eldest) {
            return size() > numBlocks;
         }
      };
   }

   @Override
   public V getValue(K k) throws CacheException {
      V ret = null;
      try {
         synchronized (this.cacheSet) {
            ret = this.cacheSet.get(k);
         }
         if (logger.isDebugEnabled()) {
            logger.debug("Getting value - key: " + k + " value: " + ret);
         }
      } catch (Exception ex) {
         logger.error("Failed to get value from cache set.", ex);
         throw new CacheException(ex);
      }
      return ret;
   }

   @Override
   public void setValue(K k, V v) throws CacheException {
      if (logger.isDebugEnabled()) {
         logger.debug("Setting value - key: " + k + " value: " + v);
      }
      try {
         synchronized (this.cacheSet) {
            this.cacheSet.put(k, v);
         }
      } catch (Exception ex) {
         logger.error("Failed to set value to cache set.", ex);
         throw new CacheException(ex);
      }
   }

   @Override
   public List<CacheBlock<K, V>> getCacheBlockList() {
      List<CacheBlock<K, V>> ret = new ArrayList<CacheBlock<K, V>>();
      if (this.cacheSet != null) {
         for (Entry<K, V> e : this.cacheSet.entrySet()) {
            ret.add(new DefaultCacheBlockImpl<K, V>(e.getKey(), e.getValue()));
         }
      }
      return ret;
   }

   @Override
   public int size() {
      return this.cacheSet.size();
   }

   @Override
   public CacheSet<K, V> createCacheSet(int numBlocks) {
      return new LRUCacheAlgorithm<K, V>(numBlocks);
   }

   public void dump() {
      for (Entry<K, V> e : this.cacheSet.entrySet()) {
         logger.debug("Key: " + e.getKey() + " value: " + e.getValue());
      }
   }
}
