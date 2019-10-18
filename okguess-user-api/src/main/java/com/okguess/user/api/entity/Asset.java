package com.okguess.user.api.entity;

/**
 * @Author hunter.pang
 * @Date 2019/10/1 下午8:57
 */
public class Asset {

    private long id;

    private String asset;

    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
