package com.ruoyi.test;

import com.ruoyi.common.utils.redis.CacheUtils;
import com.ruoyi.common.utils.redis.RedisUtils;
import com.ruoyi.common.utils.JsonUtils;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.ValidatorUtils;
import com.ruoyi.common.helper.DataBaseHelper;
import com.ruoyi.common.enums.DataBaseType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Redis禁用情况下的测试
 * 验证修复后的工具类在Redis不可用时是否正常工作
 */
@SpringBootTest
@ActiveProfiles("test")
public class RedisDisabledTest {

    @Test
    public void testRedisUtilsWhenRedisDisabled() {
        // 测试 RedisUtils 在 Redis 不可用时的行为
        assertDoesNotThrow(() -> {
            // 这些方法应该不会抛出异常，而是返回默认值
            String result = RedisUtils.getCacheObject("test-key");
            assertNull(result);
            
            RedisUtils.setCacheObject("test-key", "test-value");
            // 设置操作应该被忽略，不会抛出异常
        });
    }

    @Test
    public void testCacheUtilsWhenRedisDisabled() {
        // 测试 CacheUtils 在 Redis 不可用时的行为
        assertDoesNotThrow(() -> {
            // 这些方法应该不会抛出异常，而是返回默认值
            Set<Object> keys = CacheUtils.keys("test-cache");
            assertTrue(keys.isEmpty());
            
            Object result = CacheUtils.get("test-cache", "test-key");
            assertNull(result);
            
            CacheUtils.put("test-cache", "test-key", "test-value");
            // 操作应该被忽略，不会抛出异常
        });
    }

    @Test
    public void testJsonUtilsWhenSpringNotReady() {
        // 测试 JsonUtils 在 Spring 容器未准备好时的行为
        assertDoesNotThrow(() -> {
            // 这些方法应该不会抛出异常
            String json = JsonUtils.toJsonString("test");
            // 如果 ObjectMapper 不可用，应该返回 null
            assertNull(json);
        });
    }

    @Test
    public void testMessageUtilsWhenSpringNotReady() {
        // 测试 MessageUtils 在 Spring 容器未准备好时的行为
        assertDoesNotThrow(() -> {
            String message = MessageUtils.message("test.key");
            // 如果 MessageSource 不可用，应该返回消息键本身
            assertEquals("test.key", message);
        });
    }

    @Test
    public void testValidatorUtilsWhenSpringNotReady() {
        // 测试 ValidatorUtils 在 Spring 容器未准备好时的行为
        assertDoesNotThrow(() -> {
            // 验证操作应该被跳过，不会抛出异常
            ValidatorUtils.validate("test");
        });
    }

    @Test
    public void testDataBaseHelperWhenSpringNotReady() {
        // 测试 DataBaseHelper 在 Spring 容器未准备好时的行为
        assertDoesNotThrow(() -> {
            // 应该返回默认的数据库类型
            DataBaseType dbType = DataBaseHelper.getDataBaseType();
            assertNotNull(dbType);
        });
    }

    @Test
    public void testLock4jWhenRedisDisabled() {
        // 测试 lock4j 在 Redis 不可用时的行为
        assertDoesNotThrow(() -> {
            // 由于 lock4j 被禁用，这些操作应该不会抛出异常
            // 这里只是验证应用能够正常启动，不会因为 lock4j 而失败
        });
    }
} 