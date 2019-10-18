package com.okguess.user.api.dto;

import com.okguess.user.api.service.okservice.data.Solution;

import java.util.List;

/**
 * @Author hunter.pang
 * @Date 2019/8/30 上午10:21
 */
public class CreateProjectSolutionDto {

    private CreateProjectDto project;

    private List<CreateSolutionDto> solutions;

    public CreateProjectDto getProject() {
        return project;
    }

    public void setProject(CreateProjectDto project) {
        this.project = project;
    }

    public List<CreateSolutionDto> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<CreateSolutionDto> solutions) {
        this.solutions = solutions;
    }


}
