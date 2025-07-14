package com.ruoyi.common.utils.redis;

import com.ruoyi.common.utils.spring.SpringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.redisson.api.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * redis 工具类
 *
 * @author Lion Li
 * @version 3.1.0 新增
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings(value = {"unchecked", "rawtypes"})
public class RedisUtils {

    private static RedissonClient CLIENT;
    
    static {
        try {
            CLIENT = SpringUtils.getBean(RedissonClient.class);
        } catch (Exception e) {
            // Redis不可用时，CLIENT为null
            CLIENT = null;
        }
    }

    /**
     * 限流
     *
     * @param key          限流key
     * @param rateType     限流类型
     * @param rate         速率
     * @param rateInterval 速率间隔
     * @return -1 表示失败
     */
    public static long rateLimiter(String key, RateType rateType, int rate, int rateInterval) {
        if (CLIENT == null) {
            return 0L; // Redis不可用时，允许所有请求
        }
        try {
            RRateLimiter rateLimiter = CLIENT.getRateLimiter(key);
            rateLimiter.trySetRate(rateType, rate, rateInterval, RateIntervalUnit.SECONDS);
            if (rateLimiter.tryAcquire()) {
                return rateLimiter.availablePermits();
            } else {
                return -1L;
            }
        } catch (Exception e) {
            return 0L; // Redis不可用时，允许所有请求
        }
    }

    /**
     * 获取客户端实例
     */
    public static RedissonClient getClient() {
        if (CLIENT == null) {
            throw new RuntimeException("Redis客户端未初始化，请检查Redis配置");
        }
        return CLIENT;
    }

    /**
     * 发布通道消息
     *
     * @param channelKey 通道key
     * @param msg        发送数据
     * @param consumer   自定义处理
     */
    public static <T> void publish(String channelKey, T msg, Consumer<T> consumer) {
        if (CLIENT == null) {
            return; // Redis不可用时直接返回
        }
        try {
            RTopic topic = CLIENT.getTopic(channelKey);
            topic.publish(msg);
            consumer.accept(msg);
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }

    public static <T> void publish(String channelKey, T msg) {
        if (CLIENT == null) {
            return; // Redis不可用时直接返回
        }
        try {
            RTopic topic = CLIENT.getTopic(channelKey);
            topic.publish(msg);
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }

    /**
     * 订阅通道接收消息
     *
     * @param channelKey 通道key
     * @param clazz      消息类型
     * @param consumer   自定义处理
     */
    public static <T> void subscribe(String channelKey, Class<T> clazz, Consumer<T> consumer) {
        if (CLIENT == null) {
            return; // Redis不可用时直接返回
        }
        try {
            RTopic topic = CLIENT.getTopic(channelKey);
            topic.addListener(clazz, (channel, msg) -> consumer.accept(msg));
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    public static <T> void setCacheObject(final String key, final T value) {
        if (CLIENT == null) {
            return; // Redis不可用时直接返回
        }
        setCacheObject(key, value, false);
    }

    /**
     * 缓存基本的对象，保留当前对象 TTL 有效期
     *
     * @param key       缓存的键值
     * @param value     缓存的值
     * @param isSaveTtl 是否保留TTL有效期(例如: set之前ttl剩余90 set之后还是为90)
     * @since Redis 6.X 以上使用 setAndKeepTTL 兼容 5.X 方案
     */
    public static <T> void setCacheObject(final String key, final T value, final boolean isSaveTtl) {
        if (CLIENT == null) {
            return; // Redis不可用时直接返回
        }
        try {
            RBucket<T> bucket = CLIENT.getBucket(key);
            if (isSaveTtl) {
                try {
                    bucket.setAndKeepTTL(value);
                } catch (Exception e) {
                    long timeToLive = bucket.remainTimeToLive();
                    setCacheObject(key, value, Duration.ofMillis(timeToLive));
                }
            } else {
                bucket.set(value);
            }
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param duration 时间
     */
    public static <T> void setCacheObject(final String key, final T value, final Duration duration) {
        if (CLIENT == null) {
            return; // Redis不可用时直接返回
        }
        try {
            RBatch batch = CLIENT.createBatch();
            RBucketAsync<T> bucket = batch.getBucket(key);
            bucket.setAsync(value);
            bucket.expireAsync(duration);
            batch.execute();
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }

    /**
     * 注册对象监听器
     * <p>
     * key 监听器需开启 `notify-keyspace-events` 等 redis 相关配置
     *
     * @param key      缓存的键值
     * @param listener 监听器配置
     */
    public static <T> void addObjectListener(final String key, final ObjectListener listener) {
        if (CLIENT == null) {
            return; // Redis不可用时忽略
        }
        try {
            RBucket<T> result = CLIENT.getBucket(key);
            result.addListener(listener);
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }

    /**
     * 设置有效时间
     *
     * @param key     Redis键
     * @param timeout 超时时间
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(final String key, final long timeout) {
        return expire(key, Duration.ofSeconds(timeout));
    }

    /**
     * 设置有效时间
     *
     * @param key      Redis键
     * @param duration 超时时间
     * @return true=设置成功；false=设置失败
     */
    public static boolean expire(final String key, final Duration duration) {
        if (CLIENT == null) {
            return false; // Redis不可用时返回false
        }
        try {
            RBucket rBucket = CLIENT.getBucket(key);
            return rBucket.expire(duration);
        } catch (Exception e) {
            return false; // Redis不可用时返回false
        }
    }

    /**
     * 获得缓存的基本对象。
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    public static <T> T getCacheObject(final String key) {
        if (CLIENT == null) {
            return null; // Redis不可用时返回null
        }
        try {
            RBucket<T> rBucket = CLIENT.getBucket(key);
            return rBucket.get();
        } catch (Exception e) {
            return null; // Redis不可用时返回null
        }
    }

    /**
     * 获得key剩余存活时间
     *
     * @param key 缓存键值
     * @return 剩余存活时间
     */
    public static <T> long getTimeToLive(final String key) {
        if (CLIENT == null) {
            return -1; // Redis不可用时返回-1
        }
        try {
            RBucket<T> rBucket = CLIENT.getBucket(key);
            return rBucket.remainTimeToLive();
        } catch (Exception e) {
            return -1; // Redis不可用时返回-1
        }
    }

    /**
     * 删除单个对象
     *
     * @param key 缓存的键值
     */
    public static boolean deleteObject(final String key) {
        if (CLIENT == null) {
            return false; // Redis不可用时返回false
        }
        try {
            return CLIENT.getBucket(key).delete();
        } catch (Exception e) {
            return false; // Redis不可用时返回false
        }
    }

    /**
     * 删除集合对象
     *
     * @param collection 多个对象
     */
    public static void deleteObject(final Collection collection) {
        if (CLIENT == null) {
            return; // Redis不可用时忽略
        }
        try {
            RBatch batch = CLIENT.createBatch();
            collection.forEach(t -> {
                batch.getBucket(t.toString()).deleteAsync();
            });
            batch.execute();
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }

    /**
     * 检查缓存对象是否存在
     *
     * @param key 缓存的键值
     */
    public static boolean isExistsObject(final String key) {
        if (CLIENT == null) {
            return false; // Redis不可用时返回false
        }
        try {
            return CLIENT.getBucket(key).isExists();
        } catch (Exception e) {
            return false; // Redis不可用时返回false
        }
    }

    /**
     * 缓存List数据
     *
     * @param key      缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象
     */
    public static <T> boolean setCacheList(final String key, final List<T> dataList) {
        if (CLIENT == null) {
            return false; // Redis不可用时返回false
        }
        try {
            RList<T> rList = CLIENT.getList(key);
            return rList.addAll(dataList);
        } catch (Exception e) {
            return false; // Redis不可用时返回false
        }
    }

    /**
     * 注册List监听器
     * <p>
     * key 监听器需开启 `notify-keyspace-events` 等 redis 相关配置
     *
     * @param key      缓存的键值
     * @param listener 监听器配置
     */
    public static <T> void addListListener(final String key, final ObjectListener listener) {
        if (CLIENT == null) {
            return; // Redis不可用时忽略
        }
        try {
            RList<T> rList = CLIENT.getList(key);
            rList.addListener(listener);
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }

    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    public static <T> List<T> getCacheList(final String key) {
        if (CLIENT == null) {
            return null; // Redis不可用时返回null
        }
        try {
            RList<T> rList = CLIENT.getList(key);
            return rList.readAll();
        } catch (Exception e) {
            return null; // Redis不可用时返回null
        }
    }

    /**
     * 缓存Set
     *
     * @param key     缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public static <T> boolean setCacheSet(final String key, final Set<T> dataSet) {
        if (CLIENT == null) {
            return false; // Redis不可用时返回false
        }
        try {
            RSet<T> rSet = CLIENT.getSet(key);
            return rSet.addAll(dataSet);
        } catch (Exception e) {
            return false; // Redis不可用时返回false
        }
    }

    /**
     * 注册Set监听器
     * <p>
     * key 监听器需开启 `notify-keyspace-events` 等 redis 相关配置
     *
     * @param key      缓存的键值
     * @param listener 监听器配置
     */
    public static <T> void addSetListener(final String key, final ObjectListener listener) {
        if (CLIENT == null) {
            return; // Redis不可用时忽略
        }
        try {
            RSet<T> rSet = CLIENT.getSet(key);
            rSet.addListener(listener);
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }

    /**
     * 获得缓存的set
     *
     * @param key 缓存的key
     * @return set对象
     */
    public static <T> Set<T> getCacheSet(final String key) {
        if (CLIENT == null) {
            return null; // Redis不可用时返回null
        }
        try {
            RSet<T> rSet = CLIENT.getSet(key);
            return rSet.readAll();
        } catch (Exception e) {
            return null; // Redis不可用时返回null
        }
    }

    /**
     * 缓存Map
     *
     * @param key     缓存的键值
     * @param dataMap 缓存的数据
     */
    public static <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (CLIENT == null || dataMap == null) {
            return; // Redis不可用时忽略
        }
        try {
            RMap<String, T> rMap = CLIENT.getMap(key);
            rMap.putAll(dataMap);
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }

    /**
     * 注册Map监听器
     * <p>
     * key 监听器需开启 `notify-keyspace-events` 等 redis 相关配置
     *
     * @param key      缓存的键值
     * @param listener 监听器配置
     */
    public static <T> void addMapListener(final String key, final ObjectListener listener) {
        if (CLIENT == null) {
            return; // Redis不可用时忽略
        }
        try {
            RMap<String, T> rMap = CLIENT.getMap(key);
            rMap.addListener(listener);
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }

    /**
     * 获得缓存的Map
     *
     * @param key 缓存的键值
     * @return map对象
     */
    public static <T> Map<String, T> getCacheMap(final String key) {
        if (CLIENT == null) {
            return null; // Redis不可用时返回null
        }
        try {
            RMap<String, T> rMap = CLIENT.getMap(key);
            return rMap.getAll(rMap.keySet());
        } catch (Exception e) {
            return null; // Redis不可用时返回null
        }
    }

    /**
     * 获得缓存Map的key列表
     *
     * @param key 缓存的键值
     * @return key列表
     */
    public static <T> Set<String> getCacheMapKeySet(final String key) {
        if (CLIENT == null) {
            return null; // Redis不可用时返回null
        }
        try {
            RMap<String, T> rMap = CLIENT.getMap(key);
            return rMap.keySet();
        } catch (Exception e) {
            return null; // Redis不可用时返回null
        }
    }

    /**
     * 往Hash中存入数据
     *
     * @param key   Redis键
     * @param hKey  Hash键
     * @param value 值
     */
    public static <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        if (CLIENT == null) {
            return; // Redis不可用时忽略
        }
        try {
            RMap<String, T> rMap = CLIENT.getMap(key);
            rMap.put(hKey, value);
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }

    /**
     * 获取Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public static <T> T getCacheMapValue(final String key, final String hKey) {
        if (CLIENT == null) {
            return null; // Redis不可用时返回null
        }
        try {
            RMap<String, T> rMap = CLIENT.getMap(key);
            return rMap.get(hKey);
        } catch (Exception e) {
            return null; // Redis不可用时返回null
        }
    }

    /**
     * 删除Hash中的数据
     *
     * @param key  Redis键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    public static <T> T delCacheMapValue(final String key, final String hKey) {
        if (CLIENT == null) {
            return null; // Redis不可用时返回null
        }
        try {
            RMap<String, T> rMap = CLIENT.getMap(key);
            return rMap.remove(hKey);
        } catch (Exception e) {
            return null; // Redis不可用时返回null
        }
    }

    /**
     * 获取多个Hash中的数据
     *
     * @param key   Redis键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    public static <K, V> Map<K, V> getMultiCacheMapValue(final String key, final Set<K> hKeys) {
        if (CLIENT == null) {
            return null; // Redis不可用时返回null
        }
        try {
            RMap<K, V> rMap = CLIENT.getMap(key);
            return rMap.getAll(hKeys);
        } catch (Exception e) {
            return null; // Redis不可用时返回null
        }
    }

    /**
     * 设置原子值
     *
     * @param key   Redis键
     * @param value 值
     */
    public static void setAtomicValue(String key, long value) {
        if (CLIENT == null) {
            return; // Redis不可用时忽略
        }
        try {
            RAtomicLong atomic = CLIENT.getAtomicLong(key);
            atomic.set(value);
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }

    /**
     * 获取原子值
     *
     * @param key Redis键
     * @return 当前值
     */
    public static long getAtomicValue(String key) {
        if (CLIENT == null) {
            return 0; // Redis不可用时返回0
        }
        try {
            RAtomicLong atomic = CLIENT.getAtomicLong(key);
            return atomic.get();
        } catch (Exception e) {
            return 0; // Redis不可用时返回0
        }
    }

    /**
     * 递增原子值
     *
     * @param key Redis键
     * @return 当前值
     */
    public static long incrAtomicValue(String key) {
        if (CLIENT == null) {
            return 0; // Redis不可用时返回0
        }
        try {
            RAtomicLong atomic = CLIENT.getAtomicLong(key);
            return atomic.incrementAndGet();
        } catch (Exception e) {
            return 0; // Redis不可用时返回0
        }
    }

    /**
     * 递减原子值
     *
     * @param key Redis键
     * @return 当前值
     */
    public static long decrAtomicValue(String key) {
        if (CLIENT == null) {
            return 0; // Redis不可用时返回0
        }
        try {
            RAtomicLong atomic = CLIENT.getAtomicLong(key);
            return atomic.decrementAndGet();
        } catch (Exception e) {
            return 0; // Redis不可用时返回0
        }
    }

    /**
     * 获得缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     * @return 对象列表
     */
    public static Collection<String> keys(final String pattern) {
        if (CLIENT == null) {
            return new ArrayList<>(); // Redis不可用时返回空列表
        }
        try {
            Stream<String> stream = CLIENT.getKeys().getKeysStreamByPattern(pattern);
            return stream.collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>(); // Redis不可用时返回空列表
        }
    }

    /**
     * 删除缓存的基本对象列表
     *
     * @param pattern 字符串前缀
     */
    public static void deleteKeys(final String pattern) {
        if (CLIENT == null) {
            return; // Redis不可用时忽略
        }
        try {
            CLIENT.getKeys().deleteByPattern(pattern);
        } catch (Exception e) {
            // Redis不可用时忽略
        }
    }

    /**
     * 检查redis中是否存在key
     *
     * @param key 键
     */
    public static Boolean hasKey(String key) {
        if (CLIENT == null) {
            return false; // Redis不可用时返回false
        }
        try {
            RKeys rKeys = CLIENT.getKeys();
            return rKeys.countExists(key) > 0;
        } catch (Exception e) {
            return false; // Redis不可用时返回false
        }
    }
}
