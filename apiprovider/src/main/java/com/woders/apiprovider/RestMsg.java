package com.woders.apiprovider;

/**
 * Title:[]
 * Description:[转换返回数据的实体类]
 *
 * @author jiangguangming
 * @date 2015/12/15
 */
public class RestMsg<T> {
    private int code;
    private String message;
    private T result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
