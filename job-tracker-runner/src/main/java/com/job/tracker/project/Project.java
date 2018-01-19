package com.job.tracker.project;


import com.job.tracker.branch.Branch;
import com.job.tracker.job.Job;
import com.job.tracker.project.type.ProjectType;
import com.system.db.entity.named.NamedEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Collection;

import static com.system.logging.exception.util.ExceptionUtils.throwSystemException;

/**
 * The <class>Project</class> defines a project
 * that is to be managed. A job would have a single project
 * but a project could have many jobs and a job could have many tasks.
 *
 * @author Andrew Popp
 */
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "branch_id"})})
public class Project extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Unique and uniform company wide job number
     */
    private String projectNumber;

    @ManyToOne
    @JoinColumn(name = "project_type_id", nullable = false)
    private ProjectType projectType;


    private String address;
    private String city;
    private String state;
    private String zipCode;


    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "id", nullable = false)
    private Branch branch;

    /**
     * The absolute path of the folder structure created for
     * all files used by this entity within the filesystem
     */
    private String folderLocation;

    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(mappedBy = "project")
    private Collection<Job> jobCollection;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public Project() {
    }

    public static Project newInstance(String name, Branch branch) {
        Project project = new Project();
        project.setBranch(branch);
        project.setName(name);
        return project;
    }

    public void validate() {
        if (getBranch() == null) {
            throwSystemException("Related Project [" + getName() + "] is missing a Branch. Please ensure the Project has a Branch selected.");
        }
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public String getProjectNumber() {
        return projectNumber;
    }

    public void setProjectNumber(String projectNumber) {
        this.projectNumber = projectNumber;
    }

    public String getFolderLocation() {
        return folderLocation;
    }

    public void setFolderLocation(String folderLocation) {
        this.folderLocation = folderLocation;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Collection<Job> getJobCollection() {
        return jobCollection;
    }

    public void setJobCollection(Collection<Job> jobCollection) {
        this.jobCollection = jobCollection;
    }
}
