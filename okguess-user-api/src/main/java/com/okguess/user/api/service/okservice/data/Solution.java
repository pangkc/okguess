package com.okguess.user.api.service.okservice.data;

/**
 * @Author hunter.pang
 * @Date 2019/8/13 下午5:08
 */
public class Solution {

    private String sallcount;

    private String sbetcount;

    private String scurrentodds;

    private String sinitcount;

    private String sinitodds;

    private String sname;

    private String solutionid;

    private String spdecimal;

    private String spissue;

    private String sprofit;

    private Integer sstatus;

    private String currencysprofit;

    public String getSallcount() {
        return sallcount;
    }

    public void setSallcount(String sallcount) {
        this.sallcount = sallcount;
    }

    public String getSbetcount() {
        return sbetcount;
    }

    public void setSbetcount(String sbetcount) {
        this.sbetcount = sbetcount;
    }

    public String getScurrentodds() {
        return scurrentodds;
    }

    public void setScurrentodds(String scurrentodds) {
        this.scurrentodds = scurrentodds;
    }

    public String getSinitcount() {
        return sinitcount;
    }

    public void setSinitcount(String sinitcount) {
        this.sinitcount = sinitcount;
    }

    public String getSinitodds() {
        return sinitodds;
    }

    public void setSinitodds(String sinitodds) {
        this.sinitodds = sinitodds;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSolutionid() {
        return solutionid;
    }

    public void setSolutionid(String solutionid) {
        this.solutionid = solutionid;
    }

    public String getSpdecimal() {
        return spdecimal;
    }

    public void setSpdecimal(String spdecimal) {
        this.spdecimal = spdecimal;
    }

    public String getSpissue() {
        return spissue;
    }

    public void setSpissue(String spissue) {
        this.spissue = spissue;
    }

    public String getSprofit() {
        return sprofit;
    }

    public void setSprofit(String sprofit) {
        this.sprofit = sprofit;
    }

    public Integer getSstatus() {
        return sstatus;
    }

    public void setSstatus(Integer sstatus) {
        this.sstatus = sstatus;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "sallcount='" + sallcount + '\'' +
                ", sbetcount='" + sbetcount + '\'' +
                ", scurrentodds='" + scurrentodds + '\'' +
                ", sinitcount='" + sinitcount + '\'' +
                ", sinitodds='" + sinitodds + '\'' +
                ", sname='" + sname + '\'' +
                ", solutionid='" + solutionid + '\'' +
                ", spdecimal='" + spdecimal + '\'' +
                ", spissue='" + spissue + '\'' +
                ", sprofit='" + sprofit + '\'' +
                ", sstatus='" + sstatus + '\'' +
                '}';
    }

    public String getCurrencysprofit() {
        return currencysprofit;
    }

    public void setCurrencysprofit(String currencysprofit) {
        this.currencysprofit = currencysprofit;
    }

}
