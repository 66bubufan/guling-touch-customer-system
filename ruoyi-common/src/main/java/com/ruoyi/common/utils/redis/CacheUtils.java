package com.ruoyi.common.utils.redis;

import com.ruoyi.common.utils.spring.SpringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.redisson.api.RMap;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Set;

/**
 * 缓存操作工具类 {@link }
 *
 * @author Michelle.Chung
 * @date 2022/8/13
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings(value = {"unchecked"})
public class CacheUtils {

    private static CacheManager CACHE_MANAGER;

    /**
     * 获取缓存管理器，如果不可用则返回null
     */
    private static CacheManager getCacheManager() {
        if (CACHE_MANAGER == null) {
            try {
                CACHE_MANAGER = SpringUtils.getBean(CacheManager.class);
            } catch (Exception e) {
                // Redis不可用时，CACHE_MANAGER为null
                return null;
            }
        }
        return CACHE_MANAGER;
    }

    /**
     * 获取缓存组内所有的KEY
     *
     * @param cacheNames 缓存组名称
     */
    public static Set<Object> keys(String cacheNames) {
        try {
            CacheManager cacheManager = getCacheManager();
            if (cacheManager == null) {
                return java.util.Collections.emptySet();
            }
            Cache cache = cacheManager.getCache(cacheNames);
            if (cache != null) {
                Object nativeCache = cache.getNativeCache();
                if (nativeCache instanceof RMap) {
                    RMap<Object, Object> rmap = (RMap<Object, Object>) nativeCache;
                    return rmap.keySet();
                }
            }
        } catch (Exception e) {
            // Redis不可用时返回空集合
        }
        return java.util.Collections.emptySet();
    }

    /**
     * 获取缓存值
     *
     * @param cacheNames 缓存组名称
     * @param key        缓存key
     */
    public static <T> T get(String cacheNames, Object key) {
        try {
            CacheManager cacheManager = getCacheManager();
            if (cacheManager == null) {
                return null;
            }
            Cache cache = cacheManager.getCache(cacheNames);
            if (cache != null) {
                Cache.ValueWrapper wrapper = cache.get(key);
                return wrapper != null ? (T) wrapper.get() : null;
            }
        } catch (Exception e) {
            // Redis不可用时返回null
        }
        return null;
    }

    /**
     * 保存缓存值
     *
     * @param cacheNames 缓存组名称
     * @param key        缓存key
     * @param value      缓存值
     */
    public static void put(String cacheNames, Object key, Object value) {
        try {
            CacheManager cacheManager = getCacheManager();
            if (cacheManager == null) {
                return;
            }
            Cache cache = cacheManager.getCache(cacheNames);
            if (cache != null) {
                cache.put(key, value);
            }
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }

    /**
     * 删除缓存值
     *
     * @param cacheNames 缓存组名称
     * @param key        缓存key
     */
    public static void evict(String cacheNames, Object key) {
        try {
            CacheManager cacheManager = getCacheManager();
            if (cacheManager == null) {
                return;
            }
            Cache cache = cacheManager.getCache(cacheNames);
            if (cache != null) {
                cache.evict(key);
            }
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }

    /**
     * 清空缓存值
     *
     * @param cacheNames 缓存组名称
     */
    public static void clear(String cacheNames) {
        try {
            CacheManager cacheManager = getCacheManager();
            if (cacheManager == null) {
                return;
            }
            Cache cache = cacheManager.getCache(cacheNames);
            if (cache != null) {
                cache.clear();
            }
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }

}
