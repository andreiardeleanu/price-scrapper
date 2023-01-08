package org.upb.project.services;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Cache used to store document metadata.
 */
public class GenericCache<T,V> {

  private LoadingCache<T, V> requestCache;

  public V get(T nodId) {
    return requestCache.getIfPresent(nodId);
  }

  public void put(T nodeId, V entity) {
    requestCache.put(nodeId, entity);
  }

  public void remove(T nodId) {
    requestCache.invalidate(nodId);
  }

  /**
   * Used to init the cache.
   */
  public GenericCache() {

    CacheLoader<T, V> requestLoader;

    requestLoader = new CacheLoader<T, V>() {
      @Override
      public V load(T key) throws ExecutionException {
        return requestCache.get(key);
      }
    };

    requestCache = CacheBuilder
        .newBuilder()
        .maximumSize(100000)
        .expireAfterWrite(1440, TimeUnit.MINUTES)
        .build(requestLoader);

  }
}
