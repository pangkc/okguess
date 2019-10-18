package com.okguess.admin.api.exception;

/**
 * @Author hunter.pang
 * @Date 2019/8/13 下午2:17
 */
public class LogicException extends RuntimeException {

    private String code;

    private String subCode;

    private String msg;

    public LogicException(String code) {
        this.code = code;
    }

    public LogicException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public LogicException(String code, String subCode, String msg) {
        this.code = code + "-" + subCode;
        this.subCode = subCode;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    @Override
    public String toString() {
        return "LogicException{" +
                "code='" + code + '\'' +
                ", subCode='" + subCode + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
