package com.lenspot.devtools.cache.impl;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lenspot.devtools.cache.Cache;
import com.lenspot.devtools.cache.CacheSetFactory;
import com.lenspot.devtools.cache.impl.DefaultCacheImpl;
import com.lenspot.devtools.cache.impl.LRUCacheAlgorithm;

/**
 * This test generate a map of key and values to verify the correctness in a
 * simulated multi-threading environment. The test data will be collected for
 * brief statistical analysis.
 * 
 * @author Raymond Tsang
 *
 */
public class MultithreadingTest {
   final static Logger logger = LoggerFactory.getLogger(MultithreadingTest.class);

   // test related
   final static int questionSize = 100;
   final static int numOfThreads = 100;
   final static long milliSecTestPeriod = 5000; // in millis

   // cache related
   int nSets = 4;
   int nBlocks = 10;
   CacheSetFactory<Integer, UUID> cacheSetFactory = new LRUCacheAlgorithm<Integer, UUID>();

   // holds the sample data, key and value pairs
   private static Map<Integer, UUID> answers = new HashMap<Integer, UUID>(
         questionSize);

   // for randomly picking the sample
   private Random random = new Random(System.currentTimeMillis());

   // create cache object
   private Cache<Integer, UUID> cache = new DefaultCacheImpl<Integer, UUID>(
         nSets, nBlocks, cacheSetFactory);

   // keep handles of all the the workers, for statistics
   private List<CacheConsumer> consumerList = new ArrayList<CacheConsumer>();

   /**
    * Generate some question and answers
    */
   @Before
   public void setup() {
      for (int i = 0; i < questionSize; i++) {
         UUID uuid = UUID.randomUUID();
         answers.put(i, uuid);
      }
   }

   @Test
   public void test() {
      ExecutorService executor = Executors.newFixedThreadPool(numOfThreads);
      for (int i = 0; i < numOfThreads; i++) {
         CacheConsumer c = new CacheConsumer(milliSecTestPeriod);
         consumerList.add(c);
         executor.execute(c);
      }
      executor.shutdown();
      while (!executor.isTerminated()) {
         // wait for all the threads to complete
      }
      reportStatistics();
   }

   private void reportStatistics() {
      long totalMiss = 0;
      long totalHits = 0;
      // prepare for statistic related method calls
      double[] hits = new double[this.consumerList.size()];
      double[] miss = new double[this.consumerList.size()];

      if (this.consumerList != null) {
         for (int i=0; i<this.consumerList.size(); i++) {
            CacheConsumer c = this.consumerList.get(i);
            hits[i] = c.getTotalHits();
            miss[i] = c.getTotalMiss();
            totalHits += c.getTotalHits();
            totalMiss += c.getTotalMiss();
         }
      }

      // summary
      NumberFormat formatter = new DecimalFormat("#0.00");     
      double hitPercent = Double.valueOf(totalHits) / (totalHits + totalMiss) * 100;
      
      logger.info("Time Period (millis) = " + milliSecTestPeriod);
      logger.info("Number of sample key/value pairs = " + questionSize);
      logger.info("Total consumers = " + numOfThreads);
      logger.info("Number of cache sets = " + nSets);
      logger.info("Number of cache blocks/set = " + nBlocks);
      logger.info("Total Hits = " + totalHits);
      logger.info("Total Miss = " + totalMiss);
      logger.info("Total Hit % = " + formatter.format(hitPercent));
      logger.info("Hits mean  = " + formatter.format(new Mean().evaluate(hits)));
      logger.info("Hits stdev = " + formatter.format(new StandardDeviation().evaluate(hits)));
      logger.info("Miss mean  = " + formatter.format(new Mean().evaluate(miss)));
      logger.info("Miss stdev = " + formatter.format(new StandardDeviation().evaluate(miss)));

      ((DefaultCacheImpl) cache).dump();
      
   }

   /**
    * Cache consume to be executed in multithreaded environment
    */
   class CacheConsumer implements Runnable {
      private long milliSecAllowed = 1000; // 1 sec
      private long totalMiss = 0;
      private long totalHits = 0;

      public long getTotalMiss() {
         return totalMiss;
      }

      public long getTotalHits() {
         return totalHits;
      }

      CacheConsumer(long milliSecAllowed) {
         this.milliSecAllowed = milliSecAllowed;
      }

      public void run() {
         try {
            long now = System.currentTimeMillis();
            while ((System.currentTimeMillis() - now) < milliSecAllowed) {

               // get a random question and answer
               int key = random.nextInt(questionSize);
               UUID value = answers.get(key);

               // also get the value from cache
               UUID valueFromCache = cache.getValue(key);
               if (valueFromCache != null) {
                  // verify with the correctness
                  Assert.assertEquals(value, valueFromCache);                  
                  totalHits++;
               } else {
                  // prepare for the future hit
                  cache.setValue(key, value);
                  totalMiss++;
               }
            }
         } catch (Exception ex) {
            ex.printStackTrace();
         }

      }
   }
}
