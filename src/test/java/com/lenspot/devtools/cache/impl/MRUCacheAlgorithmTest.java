package com.lenspot.devtools.cache.impl;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.lenspot.devtools.cache.CacheBlock;
import com.lenspot.devtools.cache.CacheException;
import com.lenspot.devtools.cache.CacheSet;
import com.lenspot.devtools.cache.impl.MRUCacheAlgorithm;

/**
 * Test the MRU algorithm here.
 * 
 * @author Raymond Tsang
 *
 */
public class MRUCacheAlgorithmTest {

   @Test
   public void test() {
      try {
         MRUCacheAlgorithm<Integer, String> cache = new MRUCacheAlgorithm<Integer, String>(
               3);

         cache.setValue(1, "one");
         cache.setValue(2, "two");
         cache.setValue(3, "three");

         Assert.assertEquals("three", cache.getValue(3));
         Assert.assertEquals("two", cache.getValue(2));
         Assert.assertEquals("one", cache.getValue(1));

         cache.setValue(4, "four");

         // size should cap at 3
         Assert.assertEquals(3, cache.size());
         
         // 1 should have been replaced by 4 now
         Assert.assertNull(cache.getValue(1));
         
         // 1, 2 and 4 should still in the cache
         Assert.assertEquals("four", cache.getValue(4));
         Assert.assertEquals("three", cache.getValue(3));
         Assert.assertEquals("two", cache.getValue(2));

         // size should cap at 3
         Assert.assertEquals(3, cache.size());

      } catch (CacheException ex) {
         Assert.fail(ex.getMessage());
      }
   }

   private void dump(CacheSet<Integer, String> cset) {
      List<CacheBlock<Integer, String>> blk = cset.getCacheBlockList();
      if (blk != null) {
         for (CacheBlock<Integer, String> b : blk) {
            System.out
                  .println("key: " + b.getKey() + " value: " + b.getValue());
         }
      }
   }

}
