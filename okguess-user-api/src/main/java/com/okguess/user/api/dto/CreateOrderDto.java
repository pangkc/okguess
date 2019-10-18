package com.okguess.user.api.dto;

/**
 * @Author hunter.pang
 * @Date 2019/8/30 下午8:44
 */
public class CreateOrderDto {

    private  Long opissue;
    private Integer osolutionid;
    private String ousercount;
    private String ouserid;
    private String ousername;
    private String ouserorderno;
    private String return_url;

    public Long getOpissue() {
        return opissue;
    }

    public void setOpissue(Long opissue) {
        this.opissue = opissue;
    }

    public Integer getOsolutionid() {
        return osolutionid;
    }

    public void setOsolutionid(Integer osolutionid) {
        this.osolutionid = osolutionid;
    }

    public String getOusercount() {
        return ousercount;
    }

    public void setOusercount(String ousercount) {
        this.ousercount = ousercount;
    }

    public String getOuserid() {
        return ouserid;
    }

    public void setOuserid(String ouserid) {
        this.ouserid = ouserid;
    }

    public String getOusername() {
        return ousername;
    }

    public void setOusername(String ousername) {
        this.ousername = ousername;
    }

    public String getOuserorderno() {
        return ouserorderno;
    }

    public void setOuserorderno(String ouserorderno) {
        this.ouserorderno = ouserorderno;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    @Override
    public String toString() {
        return "CreateOrderDto{" +
                "opissue=" + opissue +
                ", osolutionid=" + osolutionid +
                ", ousercount='" + ousercount + '\'' +
                ", ouserid='" + ouserid + '\'' +
                ", ousername='" + ousername + '\'' +
                ", ouserorderno='" + ouserorderno + '\'' +
                '}';
    }
}
