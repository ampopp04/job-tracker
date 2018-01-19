package com.job.tracker.job;


import com.job.tracker.Employee.Employee;
import com.job.tracker.branch.Branch;
import com.job.tracker.customer.Customer;
import com.job.tracker.project.Project;
import com.job.tracker.task.Task;
import org.springframework.data.rest.core.config.Projection;

import java.time.LocalDate;
import java.util.List;

/**
 * The <class>Job</class> defines a job that needs to be managed by
 * project management.
 *
 * @author Andrew Popp
 */
@Projection(name = "job-all", types = Job.class)
public interface JobProjection {

    public String getJobNumber();

    public Customer getCustomer();

    public Project getProject();

    public String getCity();

    public String getState();

    public String getPoNumber();

    public double getAmount();

    public Employee getAssignedTo();

    public Employee getSalesRep();

    public Branch getSellingBranch();

    public LocalDate getDate();

    public Employee getProjectManager();

    public List<Task> getChildren();
}