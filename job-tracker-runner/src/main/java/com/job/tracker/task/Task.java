package com.job.tracker.task;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.job.tracker.Employee.Employee;
import com.job.tracker.job.Job;
import com.job.tracker.task.revision.TaskRevision;
import com.job.tracker.task.status.TaskStatus;
import com.job.tracker.task.type.TaskType;
import com.system.db.entity.named.NamedEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

import static com.system.logging.exception.util.ExceptionUtils.throwSystemException;
import static javax.persistence.FetchType.EAGER;

/**
 * The <class>Task</class> defines a task that needs to be managed by
 * project management.
 *
 * @author Andrew Popp
 */
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "job_id"})})
public class Task extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * The number of this task within a job
     */
    private String taskNumber;

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "job_id")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "task_type_id", nullable = false)
    private TaskType taskType;

    @ManyToOne
    @JoinColumn(name = "assign_to_id")
    private Employee assignedTo;

    @ManyToOne
    @JoinColumn(name = "task_status_id")
    private TaskStatus taskStatus;

    @JsonFormat(pattern = "MM-dd-yyyy")
    private LocalDate date;

    /**
     * The absolute path of the folder structure created for
     * all files used by this entity within the filesystem
     */
    private String folderLocation;

    /**
     * The list of task revisions that this task has
     */
    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(mappedBy = "task")
    private Collection<TaskRevision> taskRevisionCollection;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public Task() {
    }

    public static Task newInstance(String name, String description, String taskNumber, Job job, TaskStatus taskStatus) {
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setTaskNumber(taskNumber);
        task.setJob(job);
        task.setDate(LocalDate.now());
        task.setTaskStatus(taskStatus);
        return task;
    }

    public void validate() {
        if (getJob() == null) {
            throwSystemException("Related Task [" + getName() + "], Task Number [" + getTaskNumber() + "] is missing a Job. Please ensure the Task has a Job selected.");
        }
        getJob().validate();

        if (getTaskType() == null) {
            throwSystemException("Related Task [" + getName() + "], Task Number [" + getTaskNumber() + "] is missing a Task Type. Please ensure the Task has a Task Type selected.");
        }
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public String getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(String taskNumber) {
        this.taskNumber = taskNumber;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public Employee getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Employee assignedTo) {
        this.assignedTo = assignedTo;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getFolderLocation() {
        return folderLocation;
    }

    public void setFolderLocation(String folderLocation) {
        this.folderLocation = folderLocation;
    }

    public Collection<TaskRevision> getTaskRevisionCollection() {
        return taskRevisionCollection;
    }

    public void setTaskRevisionCollection(Collection<TaskRevision> taskRevisionCollection) {
        this.taskRevisionCollection = taskRevisionCollection;
    }
}
