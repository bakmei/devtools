package com.lenspot.devtools.cache.impl;

import org.junit.Assert;
import org.junit.Test;

import com.lenspot.devtools.cache.CacheException;
import com.lenspot.devtools.cache.impl.LRUCacheAlgorithm;

/**
 * Test the LRU algorithm here.
 * 
 * @author Raymond Tsang
 *
 */
public class LRUCacheAlgorithmTest {

   @Test
   public void test() {
      try {
         LRUCacheAlgorithm<Integer, String> cache = new LRUCacheAlgorithm<Integer, String>(3);

         cache.setValue(1, "one");          
         cache.setValue(2, "two");          
         cache.setValue(3, "three");       

         Assert.assertEquals("three", cache.getValue(3));
         Assert.assertEquals("two", cache.getValue(2));
         Assert.assertEquals("one", cache.getValue(1));
         
         cache.setValue(4, "four");    
         
         // size should be 2 since "two" should be gone
         Assert.assertEquals(3, cache.size());      
         Assert.assertNull(cache.getValue(3));
         Assert.assertEquals("four", cache.getValue(4));
         Assert.assertEquals("two", cache.getValue(2));
         Assert.assertEquals("one", cache.getValue(1));
         cache.dump();
         
         
      } catch (CacheException ex) {
         Assert.fail(ex.getMessage());
      }
   }
   
   @Test
   public void testCacheBlockSize() {
      
      LRUCacheAlgorithm<Integer, String> cache = new LRUCacheAlgorithm<Integer, String>(1);

      try {
         Assert.assertNull(cache.getValue(999));

         cache.setValue(111, "111");
         cache.setValue(222, "222");
         cache.setValue(333, "333");
         cache.setValue(444, "444");

         Assert.assertEquals(1, cache.size());
         
         cache.dump();
         
      } catch (CacheException ex) {
         Assert.fail(ex.getMessage());
      }
   }
     


}
