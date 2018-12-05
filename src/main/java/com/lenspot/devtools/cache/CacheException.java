package com.lenspot.devtools.cache;

/**
 * This exception is specific for the cache library.  Any errors occurred with the scope of the
 * implementation should log and throw this exception along with the original cause exception.
 * 
 * @author Raymond Tsang
 *
 */
public class CacheException extends Exception {

   private static final long serialVersionUID = -1366317947048838773L;

   public CacheException(Exception ex) {
      super(ex);
   }

}
