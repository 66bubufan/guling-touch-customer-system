-- 禁用验证码功能
UPDATE sys_config SET config_value = 'false' WHERE config_key = 'sys.account.captchaEnabled'; 