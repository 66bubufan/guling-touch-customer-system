package com.ruoyi.common.utils;

import com.ruoyi.common.utils.spring.SpringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 获取i18n资源文件
 *
 * @author Lion Li
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtils {

    private static MessageSource MESSAGE_SOURCE;

    /**
     * 获取消息源，如果不可用则返回null
     */
    private static MessageSource getMessageSource() {
        if (MESSAGE_SOURCE == null) {
            try {
                MESSAGE_SOURCE = SpringUtils.getBean(MessageSource.class);
            } catch (Exception e) {
                // Spring容器未初始化时，MESSAGE_SOURCE为null
                return null;
            }
        }
        return MESSAGE_SOURCE;
    }

    /**
     * 根据消息键和参数 获取消息 委托给spring messageSource
     *
     * @param code 消息键
     * @param args 参数
     * @return 获取国际化翻译值
     */
    public static String message(String code, Object... args) {
        try {
            MessageSource messageSource = getMessageSource();
            if (messageSource == null) {
                return code; // 如果消息源不可用，直接返回消息键
            }
            return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return code; // 如果获取消息失败，直接返回消息键
        }
    }
}
