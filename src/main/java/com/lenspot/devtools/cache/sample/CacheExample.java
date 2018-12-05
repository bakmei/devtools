package com.lenspot.devtools.cache.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.lenspot.devtools.cache.Cache;
import com.lenspot.devtools.cache.CacheException;
import com.lenspot.devtools.cache.impl.CacheFactory;

public class CacheExample {
   final static Logger logger = LoggerFactory.getLogger(CacheExample.class);

   public static void main(String... args) {

      Preconditions.checkNotNull(args,
            "Please provide two parameters, a key following by a value.");
      Preconditions.checkState(args.length >= 2,
            "Please provide two parameters, a key following by a value.");

      int numSets = 100;
      int numBlocks = 4;

      try {
         Cache<String, String> cache = new CacheFactory<String, String>()
               .createNWayLRUCache(numSets, numBlocks);

         logger.info("Got your input:");
         logger.info("Key: " + args[0]);
         logger.info("Value: " + args[1]);
         logger.info("Setting the value to cache.");

         cache.setValue(args[0], args[1]);
         logger.info("Retrieved back your value from the cache: "
               + cache.getValue(args[0]));
      } catch (CacheException ce) {
         logger.error("Failed in cache.", ce);
      } catch (Exception ex) {
         logger.error("Unexpected error occurred", ex);
      }
   }
}
