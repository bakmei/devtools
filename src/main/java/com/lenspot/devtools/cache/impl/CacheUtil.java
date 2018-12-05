package com.lenspot.devtools.cache.impl;

/**
 * Utilities for cache implementation.
 * 
 * @author Raymond Tsang
 *
 */
public class CacheUtil {

   /**
    * Return a cache set index based on the hash code of the given object - for
    * example, a key.
    * 
    * Please refer to the book: The art of computer programming
    *
    * @param obj
    *           any object implements the hashCode interface.
    * @param ceiling
    *           the returning integer will be in the range of between zero and
    *           ceiling minus one.
    * @return an integer between 0 and (ceiling-1)
    */
   public static int getCacheSetIndex(Object obj, int ceiling) {
      int hash = obj.hashCode();
      int ret = (int) Math.floor(ceiling * ((hash * 0.6180339887) % 1.0));
      return ret;
   }
}
