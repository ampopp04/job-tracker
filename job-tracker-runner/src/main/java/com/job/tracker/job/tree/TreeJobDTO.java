package com.job.tracker.job.tree;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.job.tracker.job.Job;
import com.system.util.validation.ValidationUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TreeJobDTO {

    private String iconCls = "tree-grid-task-folder";

    private Boolean expanded = false;
    private Boolean leaf = true;

    private String jobNumber;

    private String branchName;

    private Integer entityId;

    @JsonFormat(pattern = "MM-dd-yyyy")
    private LocalDate date;

    private String businessClientName;

    private String fileFolderPath = "unknown";

    private String projectName;

    private List<TreeTaskDTO> children;

    public TreeJobDTO() {
    }

    public static TreeJobDTO newInstance(Job job, boolean expanded) {
        job.validate();

        TreeJobDTO treeJobDTO = new TreeJobDTO();
        treeJobDTO.setJobNumber(buildJobNumber(job));
        treeJobDTO.setBranchName(job.getProject().getBranch().getName());
        treeJobDTO.setEntityId(job.getId());
        treeJobDTO.setDate(job.getDate());
        treeJobDTO.setProjectName(job.getProject().getName());
        treeJobDTO.setBusinessClientName(job.getCustomer().getName());
        treeJobDTO.setFileFolderPath(job.getFolderLocation());
        treeJobDTO.setExpanded(expanded);

        return treeJobDTO;
    }

    /**
     * This isn't actually the job number
     * but a unique identifier joining the numbers
     * between branch + project + job numbers
     */
    private static String buildJobNumber(Job job) {
        ValidationUtils.assertNotNull(job.getProject(), "Job missing project. Please set project on job [" + job.getJobNumber() + "]");
        ValidationUtils.assertNotNull(job.getProject().getBranch(), "Project missing branch. Please set branch on project [" + job.getProject().getProjectNumber() + "]");
        return job.getProject().getBranch().getCode() + "" + job.getProject().getProjectNumber() + "" + job.getJobNumber();
    }

    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getBusinessClientName() {
        return businessClientName;
    }

    public void setBusinessClientName(String businessClientName) {
        this.businessClientName = businessClientName;
    }

    public String getFileFolderPath() {
        return fileFolderPath;
    }

    public void setFileFolderPath(String fileFolderPath) {
        this.fileFolderPath = fileFolderPath;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<TreeTaskDTO> getChildren() {
        return children;
    }

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public void setChildren(List<TreeTaskDTO> children) {
        this.children = children;
    }

    public synchronized void addChild(TreeTaskDTO treeTaskDTO) {
        List<TreeTaskDTO> childrenList = getChildren();

        //We have children therefore we aren't the leaf
        setLeaf(false);

        if (childrenList == null) {
            childrenList = new ArrayList<>();
            setChildren(childrenList);
        }

        childrenList.add(treeTaskDTO);
    }

}
