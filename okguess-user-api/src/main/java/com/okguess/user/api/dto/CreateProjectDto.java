package com.okguess.user.api.dto;

/**
 * @Author hunter.pang
 * @Date 2019/8/13 下午1:54
 */
public class CreateProjectDto {

    private String passet;

    private Long pbegintime;

    private String pcreator;

    private Integer pdecimal;

    private String pdescription;

    private Long pendtime;

    private Integer plevel;

    private String pmargin;

    private String pmaxusercount;

    private String pname;

    private String proom;

    private String puserinfo;

    private String puserproductno;

    private String pq;

    private String return_url;

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

    public String getPuserinfo() {
        return puserinfo;
    }

    public void setPuserinfo(String puserinfo) {
        this.puserinfo = puserinfo;
    }

    public Integer getPlevel() {
        return plevel;
    }

    public void setPlevel(Integer level) {
        this.plevel = level;
    }

    public String getPdescription() {
        return pdescription;
    }

    public void setPdescription(String pdescription) {
        this.pdescription = pdescription;
    }

    public String getPmargin() {
        return pmargin;
    }

    public void setPmargin(String pmargin) {
        this.pmargin = pmargin;
    }

    public String getPmaxusercount() {
        return pmaxusercount;
    }

    public void setPmaxusercount(String pmaxusercount) {
        this.pmaxusercount = pmaxusercount;
    }

    public String getPuserproductno() {
        return puserproductno;
    }

    public void setPuserproductno(String puserproductno) {
        this.puserproductno = puserproductno;
    }

    public String getPq() {
        return pq;
    }

    public void setPq(String pq) {
        this.pq = pq;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    @Override
    public String toString() {
        return "CreateProjectDto{" +
                "passet='" + passet + '\'' +
                ", pbegintime='" + pbegintime + '\'' +
                ", pcreator='" + pcreator + '\'' +
                ", pdecimal='" + pdecimal + '\'' +
                ", pdescription='" + pdescription + '\'' +
                ", pendtime='" + pendtime + '\'' +
                ", plevel='" + plevel + '\'' +
                ", pmargin='" + pmargin + '\'' +
                ", pmaxusercount='" + pmaxusercount + '\'' +
                ", pname='" + pname + '\'' +
                ", proom='" + proom + '\'' +
                ", puserinfo='" + puserinfo + '\'' +
                ", puserproductno='" + puserproductno + '\'' +
                '}';
    }
}

