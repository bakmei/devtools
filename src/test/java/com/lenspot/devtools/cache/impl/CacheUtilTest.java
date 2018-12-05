package com.lenspot.devtools.cache.impl;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.lenspot.devtools.cache.impl.CacheUtil;

/**
 * Test the cache related utilities.
 * 
 * @author Raymond Tsang
 *
 */
public class CacheUtilTest {
	final static int times = 9999;	// increase this number if the distribution is not even.

	@Test
	public void test() {
		Set<Integer> results = new HashSet<Integer>();

		for (int i=0; i<times; i++) {
			results.add(CacheUtil.getCacheSetIndex(String.valueOf(i), 0));
		}
		Assert.assertTrue(results.contains(0));
		Assert.assertEquals(1, results.size());
		
	}
	
	
	@Test
	public void test2() {
		Set<Integer> results = new HashSet<Integer>();
		for (int i=0; i<times; i++) {
			results.add(CacheUtil.getCacheSetIndex(String.valueOf(i), 2));
		}
		Assert.assertTrue(results.contains(0));
		Assert.assertTrue(results.contains(1));
		Assert.assertEquals(2, results.size());
	}
	
	@Test
	public void test10() {
		Set<Integer> results = new HashSet<Integer>();
		for (int i=0; i<times; i++) {
			results.add(CacheUtil.getCacheSetIndex(String.valueOf(i), 10));
		}
		Assert.assertTrue(results.contains(0));
		Assert.assertTrue(results.contains(1));
		Assert.assertTrue(results.contains(2));
		Assert.assertTrue(results.contains(3));
		Assert.assertTrue(results.contains(4));
		Assert.assertTrue(results.contains(5));
		Assert.assertTrue(results.contains(6));
		Assert.assertTrue(results.contains(7));
		Assert.assertTrue(results.contains(8));
		Assert.assertTrue(results.contains(9));
		Assert.assertEquals(10, results.size());
		
	}


}
