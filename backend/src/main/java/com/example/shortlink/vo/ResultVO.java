package com.example.shortlink.vo;

import lombok.Data;

@Data
public class ResultVO<T> {
    private Integer code;
    private String message;
    private T data;

    public static <T> ResultVO<T> success(T data) {
        ResultVO<T> vo = new ResultVO<>();
        vo.setCode(200);
        vo.setMessage("操作成功");
        vo.setData(data);
        return vo;
    }

    public static <T> ResultVO<T> success(String message, T data) {
        ResultVO<T> vo = new ResultVO<>();
        vo.setCode(200);
        vo.setMessage(message);
        vo.setData(data);
        return vo;
    }

    public static <T> ResultVO<T> fail(String message) {
        ResultVO<T> vo = new ResultVO<>();
        vo.setCode(500);
        vo.setMessage(message);
        return vo;
    }

    public static <T> ResultVO<T> fail(Integer code, String message) {
        ResultVO<T> vo = new ResultVO<>();
        vo.setCode(code);
        vo.setMessage(message);
        return vo;
    }
}
