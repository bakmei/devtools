package com.lenspot.devtools.cache.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lenspot.devtools.cache.CacheBlock;
import com.lenspot.devtools.cache.CacheException;
import com.lenspot.devtools.cache.CacheSet;
import com.lenspot.devtools.cache.CacheSetFactory;

/**
 * The class implements the MRU - Most Recently Used algorithm for the cache
 * set. The class hold a reference to the most recently used key. This reference
 * will be changed each time the getValue method is called, it records the most
 * recently accessed key.
 * 
 * @author Raymond Tsang
 *
 * @param <K>
 *           the type of the keys
 * @param <V>
 *           the type of the values
 */
public class MRUCacheAlgorithm<K, V> extends ConcurrentHashMap<K, V> implements
      CacheSet<K, V>, CacheSetFactory<K, V> {
   /**
    * 
    */
   private static final long serialVersionUID = 4227816398138508613L;

   // logger
   final static Logger logger = LoggerFactory
         .getLogger(MRUCacheAlgorithm.class);

   // atomic reference points to the most recently used key
   private AtomicReference<K> mostRecentUsed = new AtomicReference<K>();

   private int numBlock = 8;

   /**
    * Instantiate an instance of the factory
    */
   public MRUCacheAlgorithm() {
   }

   /**
    * Instantiate an instance of cache set.
    * 
    * @param numBlocks
    */
   MRUCacheAlgorithm(int numBlocks) {
      super(numBlocks);
      this.numBlock = numBlocks;
   }

   /**
    * Factory method
    */
   @Override
   public CacheSet<K, V> createCacheSet(int numBlocks) {
      return new MRUCacheAlgorithm<K, V>(numBlocks);
   }

   @Override
   public V getValue(K k) throws CacheException {
      V ret = this.get(k);
      if (logger.isDebugEnabled()) {
         logger.debug("Getting value - key: " + k + " value: " + ret);
      }
      if (ret != null) {
         if (logger.isDebugEnabled()) {
            logger.debug("Setting MRU - key: " + k);
         }
         this.mostRecentUsed.set(k);
      }
      return ret;
   }

   @Override
   public void setValue(K k, V v) throws CacheException {
      if (logger.isDebugEnabled()) {
         logger.debug("Adding to cache set - key: " + k + " value: " + v);
      }
      replaceFirst(k, v);
   }

   private K replaceFirst(K k, V v) {
      K ret = null;
      synchronized (this.mostRecentUsed) {
         if (this.size() >= this.numBlock && this.mostRecentUsed.get() != null) {
            ret = this.mostRecentUsed.get();
            if (logger.isDebugEnabled()) {
               logger.debug("Removing old MRU from cache - key: " + ret);
            }
            this.remove(ret);
         }
         if (logger.isDebugEnabled()) {
            logger.debug("Adding cache - key: " + k + " value: " + v);
         }
         this.put(k, v);
         if (logger.isDebugEnabled()) {
            logger.debug("Setting new MRU - key: " + k);
         }
         this.mostRecentUsed.set(k);
      }
      return ret;
   }

   @Override
   public List<CacheBlock<K, V>> getCacheBlockList() {
      List<CacheBlock<K, V>> ret = new ArrayList<CacheBlock<K, V>>();
      for (Entry<K, V> e : this.entrySet()) {
         ret.add(new DefaultCacheBlockImpl<K, V>(e.getKey(), e.getValue()));
      }
      return ret;
   }
}
