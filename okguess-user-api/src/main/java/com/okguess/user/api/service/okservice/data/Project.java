package com.okguess.user.api.service.okservice.data;

/**
 * @Author hunter.pang
 * @Date 2019/8/13 下午1:54
 */
public class Project {

    private String pallcount;

    private String passet;

    private Long pbegintime;

    //总投注人数
    private Long pbetcount;

    //boc服务ID,用于boc服务的url的query参数,int64
    private Long pbocid;

    private Long pcreatetime;

    private String pcreator;

    //资产精度
    private Integer pdecimal;

    private Long pendtime;

    private Long pissue;

    private String pname;

    private String proom;

    private Integer pstatus;

    private String puserinfo;

    private Integer plevel;

    private String pdescription;

    private String pmaxusercount;

    private String pmargin;

    private Integer pwincount;

    private Integer pwinsid;

    private String pwinsname;

    private String pprofit;

    private String currencypprofit;

    private String ouserallcount;

    private String ouserprofit;

    private String currencyouserprofit;

    private Integer psettlestatus;

    private Long pliquidatetime;

    private String pmarginleft;

    private String pplatformprofit;

    public String getPmarginleft() {
        return pmarginleft;
    }

    public void setPmarginleft(String pmarginleft) {
        this.pmarginleft = pmarginleft;
    }

    public String getPplatformprofit() {
        return pplatformprofit;
    }

    public void setPplatformprofit(String pplatformprofit) {
        this.pplatformprofit = pplatformprofit;
    }

    public String getCurrencyouserprofit() {
        return currencyouserprofit;
    }

    public void setCurrencyouserprofit(String currencyouserprofit) {
        this.currencyouserprofit = currencyouserprofit;
    }

    public String getPasset() {
        return passet;
    }

    public void setPasset(String passet) {
        this.passet = passet;
    }

    public Long getPbegintime() {
        return pbegintime;
    }

    public void setPbegintime(Long pbegintime) {
        this.pbegintime = pbegintime;
    }

    public Long getPbetcount() {
        return pbetcount;
    }

    public void setPbetcount(Long pbetcount) {
        this.pbetcount = pbetcount;
    }

    public Long getPbocid() {
        return pbocid;
    }

    public void setPbocid(Long pbocid) {
        this.pbocid = pbocid;
    }

    public Long getPcreatetime() {
        return pcreatetime;
    }

    public void setPcreatetime(Long pcreatetime) {
        this.pcreatetime = pcreatetime;
    }

    public String getPcreator() {
        return pcreator;
    }

    public void setPcreator(String pcreator) {
        this.pcreator = pcreator;
    }

    public Integer getPdecimal() {
        return pdecimal;
    }

    public void setPdecimal(Integer pdecimal) {
        this.pdecimal = pdecimal;
    }

    public Long getPendtime() {
        return pendtime;
    }

    public void setPendtime(Long pendtime) {
        this.pendtime = pendtime;
    }

    public Long getPissue() {
        return pissue;
    }

    public void setPissue(Long pissue) {
        this.pissue = pissue;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getProom() {
        return proom;
    }

    public void setProom(String proom) {
        this.proom = proom;
    }

    public Integer getPstatus() {
        return pstatus;
    }

    public void setPstatus(Integer pstatus) {
        this.pstatus = pstatus;
    }

    public String getPuserinfo() {
        return puserinfo;
    }

    public void setPuserinfo(String puserinfo) {
        this.puserinfo = puserinfo;
    }

    public Integer getPlevel() {
        return plevel;
    }

    public void setPlevel(Integer plevel) {
        this.plevel = plevel;
    }

    public String getPdescription() {
        return pdescription;
    }

    public void setPdescription(String pdescription) {
        this.pdescription = pdescription;
    }

    public String getPmaxusercount() {
        return pmaxusercount;
    }

    public void setPmaxusercount(String pmaxusercount) {
        this.pmaxusercount = pmaxusercount;
    }

    public String getPallcount() {
        return pallcount;
    }

    public void setPallcount(String pallcount) {
        this.pallcount = pallcount;
    }


    public String getPmargin() {
        return pmargin;
    }

    public void setPmargin(String pmargin) {
        this.pmargin = pmargin;
    }

    public Integer getPwincount() {
        return pwincount;
    }

    public void setPwincount(Integer pwincount) {
        this.pwincount = pwincount;
    }

    public Integer getPwinsid() {
        return pwinsid;
    }

    public void setPwinsid(Integer pwinsid) {
        this.pwinsid = pwinsid;
    }

    public String getPwinsname() {
        return pwinsname;
    }

    public void setPwinsname(String pwinsname) {
        this.pwinsname = pwinsname;
    }

    public String getPprofit() {
        return pprofit;
    }

    public void setPprofit(String pprofit) {
        this.pprofit = pprofit;
    }

    public String getCurrencypprofit() {
        return currencypprofit;
    }

    public void setCurrencypprofit(String currencypprofit) {
        this.currencypprofit = currencypprofit;
    }

    public String getOuserallcount() {
        return ouserallcount;
    }

    public void setOuserallcount(String ouserallcount) {
        this.ouserallcount = ouserallcount;
    }

    public String getOuserprofit() {
        return ouserprofit;
    }

    public void setOuserprofit(String ouserprofit) {
        this.ouserprofit = ouserprofit;
    }

    public Integer getPsettlestatus() {
        return psettlestatus;
    }

    public void setPsettlestatus(Integer psettlestatus) {
        this.psettlestatus = psettlestatus;
    }

    public Long getPliquidatetime() {
        return pliquidatetime;
    }

    public void setPliquidatetime(Long pliquidatetime) {
        this.pliquidatetime = pliquidatetime;
    }

    @Override
    public String toString() {
        return "Project{" +
                "pallcount='" + pallcount + '\'' +
                ", passet='" + passet + '\'' +
                ", pbegintime=" + pbegintime +
                ", pbetcount=" + pbetcount +
                ", pbocid=" + pbocid +
                ", pcreatetime=" + pcreatetime +
                ", pcreator='" + pcreator + '\'' +
                ", pdecimal=" + pdecimal +
                ", pendtime=" + pendtime +
                ", pissue=" + pissue +
                ", pname='" + pname + '\'' +
                ", proom=" + proom +
                ", pstatus=" + pstatus +
                ", puserinfo='" + puserinfo + '\'' +
                ", plevel=" + plevel +
                ", pdescription='" + pdescription + '\'' +
                ", pmaxusercount='" + pmaxusercount + '\'' +
                ", pmargin='" + pmargin + '\'' +
                ", pwincount=" + pwincount +
                ", pwinsid=" + pwinsid +
                ", pwinsname='" + pwinsname + '\'' +
                '}';
    }


}

