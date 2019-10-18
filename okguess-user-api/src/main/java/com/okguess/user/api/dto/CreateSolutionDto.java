package com.okguess.user.api.dto;

/**
 * @Author hunter.pang
 * @Date 2019/8/13 下午5:08
 */
public class CreateSolutionDto {

    private String sodds;

    private String sname;

    private Integer solutionid;

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getSodds() {
        return sodds;
    }

    public void setSodds(String sodds) {
        this.sodds = sodds;
    }

    public Integer getSolutionid() {
        return solutionid;
    }

    public void setSolutionid(Integer solutionid) {
        this.solutionid = solutionid;
    }
}
