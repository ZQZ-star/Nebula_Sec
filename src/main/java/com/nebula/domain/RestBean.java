package com.nebula.domain;

// JDK 21 特性：Record 类，极其适合做不可变的 DTO
public record RestBean<T>(int code, String message, T data) {
    public static <T> RestBean<T> success(T data) {
        return new RestBean<>(200, "操作成功", data);
    }

    public static <T> RestBean<T> success(String message, T data) {
        return new RestBean<>(200, message, data);
    }

    public static <T> RestBean<T> error(int code, String message) {
        return new RestBean<>(code, message, null);
    }
}
