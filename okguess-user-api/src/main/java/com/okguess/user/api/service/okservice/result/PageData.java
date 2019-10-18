package com.okguess.user.api.service.okservice.result;

import java.util.List;

/**
 * @Author hunter.pang
 * @Date 2019/8/13 下午2:03
 */
public class PageData<T> {

    private String has_next;
    private List<T> list;
    private String page;
    private String size;
    private String total_result;

    public String getHas_next() {
        return has_next;
    }

    public void setHas_next(String has_next) {
        this.has_next = has_next;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTotal_result() {
        return total_result;
    }

    public void setTotal_result(String total_result) {
        this.total_result = total_result;
    }

    @Override
    public String toString() {
        return "PageData{" +
                "has_next='" + has_next + '\'' +
                ", list=" + list +
                ", page='" + page + '\'' +
                ", size='" + size + '\'' +
                ", total_result='" + total_result + '\'' +
                '}';
    }
}
