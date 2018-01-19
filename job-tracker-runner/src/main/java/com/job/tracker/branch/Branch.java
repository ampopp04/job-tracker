package com.job.tracker.branch;


import com.job.tracker.project.Project;
import com.system.db.entity.named.NamedEntity;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.OneToMany;
import java.util.Collection;

/**
 * The <class>Branch</class> represents
 * a branch of this company
 *
 * @author Andrew Popp
 */
public class Branch extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * The code that represents this branch
     */
    private String code;

    /**
     * The absolute path of the folder structure created for
     * all files used by this entity within the filesystem
     */
    private String folderLocation;

    @LazyCollection(LazyCollectionOption.TRUE)
    @OneToMany(mappedBy = "branch")
    private Collection<Project> projectCollection;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public Branch() {
    }

    public static Branch newBasicInstance(String name, String code, String folderLocation) {
        Branch branch = new Branch();
        branch.setName(name);
        branch.setCode(code);
        branch.setFolderLocation(folderLocation);
        return branch;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFolderLocation() {
        return folderLocation;
    }

    public void setFolderLocation(String folderLocation) {
        this.folderLocation = folderLocation;
    }

    public Collection<Project> getProjectCollection() {
        return projectCollection;
    }

    public void setProjectCollection(Collection<Project> projectCollection) {
        this.projectCollection = projectCollection;
    }
}
