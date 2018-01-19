package com.job.tracker.job;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.job.tracker.Employee.Employee;
import com.job.tracker.customer.Customer;
import com.job.tracker.project.Project;
import com.job.tracker.task.Task;
import com.system.db.entity.named.NamedEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;

import static com.system.logging.exception.util.ExceptionUtils.throwSystemException;


/**
 * The <class>Job</class> defines a job that needs to be managed by
 * project management.
 *
 * @author Andrew Popp
 */
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"customer_id", "project_id"})})
public class Job extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Unique and uniform company wide job number
     */
    private String jobNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    private String poNumber;

    private double amount;

    @ManyToOne
    @JoinColumn(name = "sales_rep_id", referencedColumnName = "id")
    private Employee salesRep;

    @JsonFormat(pattern = "MM-dd-yyyy")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "project_manager_id", referencedColumnName = "id")
    private Employee projectManager;

    /**
     * The absolute path of the folder structure created for
     * all files used by this entity within the filesystem
     */
    private String folderLocation;

    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(mappedBy = "job")
    private Collection<Task> children;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public Job() {
    }

    public static Job newInstance() {
        Job job = new Job();
        return job;
    }

    public void validate() {
        if (getProject() == null) {
            throwSystemException("Related Job, Number [" + getJobNumber() + "] is missing a Project. Please ensure the Job has a Project selected.");
        }
        getProject().validate();

        if (getCustomer() == null) {
            throwSystemException("Related Job, Number [" + getJobNumber() + "] is missing a Customer. Please ensure the Job has a Customer selected.");
        }
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////


    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
        setName(project.getName());
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Employee getSalesRep() {
        return salesRep;
    }

    public void setSalesRep(Employee salesRep) {
        this.salesRep = salesRep;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Employee getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(Employee projectManager) {
        this.projectManager = projectManager;
    }

    public String getFolderLocation() {
        return folderLocation;
    }

    public void setFolderLocation(String folderLocation) {
        this.folderLocation = folderLocation;
    }

    public Collection<Task> getChildren() {
        return children;
    }

    public void setChildren(Collection<Task> children) {
        this.children = children;
    }
}