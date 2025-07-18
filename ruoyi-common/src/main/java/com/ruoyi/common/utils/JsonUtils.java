package com.ruoyi.common.utils;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.ruoyi.common.utils.spring.SpringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON 工具类
 *
 * @author 芋道源码
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonUtils {

    private static ObjectMapper OBJECT_MAPPER;

    /**
     * 获取 ObjectMapper，如果不可用则返回null
     */
    private static ObjectMapper getObjectMapper() {
        if (OBJECT_MAPPER == null) {
            try {
                OBJECT_MAPPER = SpringUtils.getBean(ObjectMapper.class);
            } catch (Exception e) {
                // Spring容器未初始化时，OBJECT_MAPPER为null
                return null;
            }
        }
        return OBJECT_MAPPER;
    }

    public static ObjectMapper getObjectMapperInstance() {
        return getObjectMapper();
    }

    public static String toJsonString(Object object) {
        if (ObjectUtil.isNull(object)) {
            return null;
        }
        try {
            ObjectMapper mapper = getObjectMapper();
            if (mapper == null) {
                return null;
            }
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(String text, Class<T> clazz) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        try {
            ObjectMapper mapper = getObjectMapper();
            if (mapper == null) {
                return null;
            }
            return mapper.readValue(text, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(byte[] bytes, Class<T> clazz) {
        if (ArrayUtil.isEmpty(bytes)) {
            return null;
        }
        try {
            ObjectMapper mapper = getObjectMapper();
            if (mapper == null) {
                return null;
            }
            return mapper.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(String text, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        try {
            ObjectMapper mapper = getObjectMapper();
            if (mapper == null) {
                return null;
            }
            return mapper.readValue(text, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Dict parseMap(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        try {
            ObjectMapper mapper = getObjectMapper();
            if (mapper == null) {
                return null;
            }
            return mapper.readValue(text, mapper.getTypeFactory().constructType(Dict.class));
        } catch (MismatchedInputException e) {
            // 类型不匹配说明不是json
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Dict> parseArrayMap(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        try {
            ObjectMapper mapper = getObjectMapper();
            if (mapper == null) {
                return null;
            }
            return mapper.readValue(text, mapper.getTypeFactory().constructCollectionType(List.class, Dict.class));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        if (StringUtils.isEmpty(text)) {
            return new ArrayList<>();
        }
        try {
            ObjectMapper mapper = getObjectMapper();
            if (mapper == null) {
                return new ArrayList<>();
            }
            return mapper.readValue(text, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
