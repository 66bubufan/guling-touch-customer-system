package com.ruoyi.common.utils;

import com.ruoyi.common.utils.spring.SpringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

/**
 * Validator 校验框架工具
 *
 * @author Lion Li
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorUtils {

    private static Validator VALID;

    /**
     * 获取验证器，如果不可用则返回null
     */
    private static Validator getValidator() {
        if (VALID == null) {
            try {
                VALID = SpringUtils.getBean(Validator.class);
            } catch (Exception e) {
                // Spring容器未初始化时，VALID为null
                return null;
            }
        }
        return VALID;
    }

    public static <T> void validate(T object, Class<?>... groups) {
        try {
            Validator validator = getValidator();
            if (validator == null) {
                return; // 如果验证器不可用，跳过验证
            }
            Set<ConstraintViolation<T>> validate = validator.validate(object, groups);
            if (!validate.isEmpty()) {
                throw new ConstraintViolationException("参数校验异常", validate);
            }
        } catch (Exception e) {
            if (e instanceof ConstraintViolationException) {
                throw e;
            }
            // 其他异常忽略，避免因为验证器不可用而影响业务
        }
    }

}
