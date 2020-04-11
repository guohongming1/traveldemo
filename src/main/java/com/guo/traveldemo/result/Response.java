package com.guo.traveldemo.result;



/**
 * 全局异常返回
 *
 * @author Monster
 * @since 1.0.0-SNAPSHOT
 */
public class Response<T> {
    private Integer code;
    private String message;
    private T data;

    private Response(T data) {
        this.data = data;
    }

    private Response(CodeMsg codeMsg) {
        if (codeMsg != null) {
            this.code = codeMsg.getCode();
            this.message = codeMsg.getMsg();
        }
    }

    private Response(T data, CodeMsg codeMsg) {
        this.data = data;
        if (codeMsg != null) {
            this.code = codeMsg.getCode();
            this.message = codeMsg.getMsg();
        }
    }

    private Response(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 返回成功响应，默认CodeMsg的SUCCESS
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Response<T> success(T data) {
        return new Response<>(data, CodeMsg.SUCCESS);
    }

    /**
     * 返回失败结果
     *
     * @param codeMsg
     * @param <T>
     * @return
     */
    public static <T> Response<T> fail(CodeMsg codeMsg) {
        return new Response<>(codeMsg);
    }

    /**
     * 作为异常结果返回
     *
     * @param message
     * @param <T>
     * @return
     */
    public static <T> Response<T> fail(Integer code, String message) {
        return new Response<>(code, message);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
