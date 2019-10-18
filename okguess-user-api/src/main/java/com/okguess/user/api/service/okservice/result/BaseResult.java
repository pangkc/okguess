package com.okguess.user.api.service.okservice.result;

/**
 * @Author hunter.pang
 * @Date 2019/8/13 下午1:58
 */
public class BaseResult<T> {

    private String err;

    private String errmsg;

    private T result;

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "err='" + err + '\'' +
                ", errmsg='" + errmsg + '\'' +
                ", result=" + result +
                '}';
    }
}
