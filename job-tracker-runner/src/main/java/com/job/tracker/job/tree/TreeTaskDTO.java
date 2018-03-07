package com.job.tracker.job.tree;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.job.tracker.task.Task;
import com.system.util.collection.CollectionUtils;

import java.time.LocalDate;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * The <class>TreeTaskDTO</class> defines a virtual
 * DTO used for the dashboard tree grid view.
 *
 * @author Andrew Popp
 */
public class TreeTaskDTO {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    private String iconCls = "tree-grid-task";

    private Boolean expanded = false;
    private Boolean leaf = true;

    /**
     * Set as the job number + "-" + taskNumber to uniquely label this entity
     * relative to it's associated job
     */
    private String jobNumber;

    private Integer taskNumber;
    private Integer revisionNumber;

    private String branchName;

    private Integer entityId;

    private String taskTypeName;

    private String assignedTo;

    @JsonFormat(pattern = "MM-dd-yyyy")
    private LocalDate date;

    private String businessClientName;

    private String fileFolderPath = "unknown";

    private String projectName;

    private String description;

    private String status;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    private TreeTaskDTO() {
    }

    public static TreeTaskDTO newInstance(Task task) {
        task.validate();

        TreeTaskDTO treeTaskDTO = new TreeTaskDTO();
        treeTaskDTO.setEntityId(task.getId());

        Integer taskNumberValue = null;
        if (!isEmpty(task.getTaskNumber())) {
            taskNumberValue = Integer.valueOf(task.getTaskNumber());
            treeTaskDTO.setTaskNumber((taskNumberValue >= 0) ? taskNumberValue : null);
        } else {
            task.setTaskNumber(null);
        }

        treeTaskDTO.setRevisionNumber(CollectionUtils.empty(task.getTaskRevisionCollection()) ? null : task.getTaskRevisionCollection().size() - 1);

        treeTaskDTO.setTaskTypeName(task.getTaskType() != null ? task.getTaskType().getName() : null);
        treeTaskDTO.setAssignedTo(task.getAssignedTo() != null ? task.getAssignedTo().getName() : null);

        treeTaskDTO.setBranchName(task.getJob().getProject().getBranch().getName());
        treeTaskDTO.setDate(task.getDate());
        treeTaskDTO.setBusinessClientName(task.getJob().getCustomer().getName());
        treeTaskDTO.setProjectName(task.getJob().getProject().getName());
        treeTaskDTO.setDescription(task.getName());
        treeTaskDTO.setStatus(task.getTaskStatus().getName());
        treeTaskDTO.setFileFolderPath(task.getFolderLocation());
        treeTaskDTO.setJobNumber(task.getJob().getJobNumber() + "-" + task.getTaskNumber());

        return treeTaskDTO;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

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

    public Boolean getLeaf() {
        return leaf;
    }

    public void setLeaf(Boolean leaf) {
        this.leaf = leaf;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public Integer getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(Integer taskNumber) {
        this.taskNumber = taskNumber;
    }

    public Integer getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(Integer revisionNumber) {
        this.revisionNumber = revisionNumber;
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

    public String getTaskTypeName() {
        return taskTypeName;
    }

    public void setTaskTypeName(String taskTypeName) {
        this.taskTypeName = taskTypeName;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
