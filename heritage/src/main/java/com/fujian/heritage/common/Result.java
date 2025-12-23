package com.fujian.heritage.common;

import lombok.Data;

/**
 * 前后端交互统一响应结果封装
 * @param <T> 数据类型
 */
@Data
public class Result<T> {
    private Integer code; // 200:成功, 500:失败
    private String msg;   // 提示信息
    private T data;       // 返回的数据

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.code = 200;
        r.msg = "操作成功";
        r.data = data;
        return r;
    }

    // 2. 【新增】无参数的 success 方法
    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> error(String msg) {
        Result<T> r = new Result<>();
        r.code = 500;
        r.msg = msg;
        return r;
    }

}