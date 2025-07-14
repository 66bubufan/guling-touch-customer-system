package com.ruoyi.system.service;

public class SaTokenBcryptTest {
    public static void main(String[] args) {
        String hash = cn.dev33.satoken.secure.BCrypt.hashpw("123456");
        System.out.println("123456 的 Sa-Token BCrypt 密文：" + hash);
    }
} 