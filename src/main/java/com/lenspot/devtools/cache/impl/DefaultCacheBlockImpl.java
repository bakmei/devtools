package com.lenspot.devtools.cache.impl;

import com.google.common.base.Preconditions;
import com.lenspot.devtools.cache.CacheBlock;

/**
 * The default implementation for cache block.
 * 
 * @see CacheBlock
 * 
 * @author Raymond Tsang
 *
 * @param <K>
 *           the type of the keys
 * @param <V>
 *           the type of the values
 */
public class DefaultCacheBlockImpl<K, V> implements CacheBlock<K, V> {
   // use transient for multi-threads access from different CPU cores.
   private transient K key = null;
   private transient V value = null;

   /**
    * Default constructor.
    * 
    * @param k
    *           a key
    * @param v
    *           a value
    */
   public DefaultCacheBlockImpl(K k, V v) {
      Preconditions.checkNotNull(k);
      Preconditions.checkNotNull(v);
      this.key = k;
      this.value = v;
   }

   public K getKey() {
      return this.key;
   }

   public V getValue() {
      return this.value;
   }

   @Override
   public String toString() {
      return "key: " + key + " value: " + value;
   }

}
