package com.job.tracker.task.type;


import com.job.tracker.department.Department;
import com.job.tracker.system.entity.expression.EntityExpression;
import com.system.db.entity.named.NamedEntity;

import javax.persistence.*;

/**
 * The <class>TaskType</class> defines different
 * types of tasks
 *
 * @author Andrew Popp
 */
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class TaskType extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    /**
     * The name of the folder that will house task folders of this specific task type
     */
    @Column(nullable = false, updatable = false)
    private String folderName;

    /**
     * The entity expression that can be used to build folders for this type
     */
    @ManyToOne
    @JoinColumn(name = "entity_expression_id")
    private EntityExpression entityExpression;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public TaskType() {
    }

    public static TaskType newInstance(String name, String folderName, String description, EntityExpression entityExpression, Department department) {
        TaskType taskType = new TaskType();
        taskType.setDescription(description);
        taskType.setName(name);
        taskType.setFolderName(folderName);
        taskType.setDepartment(department);
        return taskType;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public EntityExpression getEntityExpression() {
        return entityExpression;
    }

    public void setEntityExpression(EntityExpression entityExpression) {
        this.entityExpression = entityExpression;
    }

}
