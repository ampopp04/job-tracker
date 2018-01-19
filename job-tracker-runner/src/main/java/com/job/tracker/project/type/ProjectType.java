package com.job.tracker.project.type;


import com.system.db.entity.named.NamedEntity;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The <class>ProjectType</class> defines different
 * types of projects
 *
 * @author Andrew Popp
 */
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class ProjectType extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public ProjectType() {
    }

    public static ProjectType newInstance(String name, String description) {
        ProjectType projectType = new ProjectType();
        projectType.setDescription(description);
        projectType.setName(name);
        return projectType;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

}
