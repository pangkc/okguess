package com.okguess.user.api.entity;

import java.util.Date;

/**
 * @Author hunter.pang
 * @Date 2019/10/1 上午8:21
 */
public class UserInfo {

    private long id;

    private long bpUserId;

    private String bpNick;

    private String bpPhoto;

    private Date createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBpUserId() {
        return bpUserId;
    }

    public void setBpUserId(long bpUserId) {
        this.bpUserId = bpUserId;
    }

    public String getBpNick() {
        return bpNick;
    }

    public void setBpNick(String bpNick) {
        this.bpNick = bpNick;
    }

    public String getBpPhoto() {
        return bpPhoto;
    }

    public void setBpPhoto(String bpPhoto) {
        this.bpPhoto = bpPhoto;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
