package com.job.tracker.task.status;


import com.system.db.entity.named.NamedEntity;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The <class>TaskStatus</class> defines different
 * types of task states.
 *
 * @author Andrew Popp
 */
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class TaskStatus extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public TaskStatus() {
    }

    public static TaskStatus newInstance(String name, String description) {
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setDescription(description);
        taskStatus.setName(name);
        return taskStatus;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

}
