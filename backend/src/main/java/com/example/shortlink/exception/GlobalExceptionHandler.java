package com.example.shortlink.exception;

import com.example.shortlink.vo.ResultVO;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResultVO<Void> handleBusinessException(BusinessException e) {
        return ResultVO.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResultVO<Void> handleValidException(Exception e) {
        return ResultVO.fail(400, "参数校验失败");
    }

    @ExceptionHandler(Exception.class)
    public ResultVO<Void> handleException(Exception e) {
        return ResultVO.fail("系统异常：" + e.getMessage());
    }
}
