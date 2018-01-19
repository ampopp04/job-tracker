package com.job.tracker.task.hour;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.job.tracker.Employee.Employee;
import com.job.tracker.task.Task;
import com.job.tracker.task.revision.TaskRevision;
import com.system.db.entity.named.NamedEntity;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

/**
 * The <class>TaskHour</class> defines a
 * record of hours for a given task
 * and engineer. An engineer(employee)
 * will record their hours work for a specific task.
 *
 * @author Andrew Popp
 */
public class TaskHour extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne
    @JoinColumn(name = "task_revision_id")
    private TaskRevision taskRevision;

    @Column(nullable = false)
    @JsonFormat(pattern = "MM-dd-yyyy")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private Double hours;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public TaskHour() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public TaskRevision getTaskRevision() {
        return taskRevision;
    }

    public void setTaskRevision(TaskRevision taskRevision) {
        this.taskRevision = taskRevision;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }
}
