package com.lenspot.devtools.cache.impl;

import org.junit.Assert;
import org.junit.Test;

import com.lenspot.devtools.cache.Cache;
import com.lenspot.devtools.cache.CacheException;
import com.lenspot.devtools.cache.impl.DefaultCacheImpl;
import com.lenspot.devtools.cache.impl.LRUCacheAlgorithm;

/**
 * Test the default cache implementation here.
 * 
 * @author Raymond Tsang
 *
 */
public class DefaultCacheImplTest {

   @Test
   public void test() {
      Integer expectedKey = 999;
      String expectedValue = "nine_nine_nine";

      Cache<Integer, String> cache = new DefaultCacheImpl<Integer, String>(100, 1,
            new LRUCacheAlgorithm<Integer, String>(4));

      try {
         Assert.assertNull(cache.getValue(expectedKey));

         cache.setValue(expectedKey, expectedValue);

         Assert.assertEquals(expectedValue, cache.getValue(expectedKey));
         
         ((DefaultCacheImpl<Integer, String>) cache).dump();
      } catch (CacheException ex) {
         Assert.fail(ex.getMessage());
      }
   }
   
}
