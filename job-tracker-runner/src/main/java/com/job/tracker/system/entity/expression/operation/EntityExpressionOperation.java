package com.job.tracker.system.entity.expression.operation;


import com.job.tracker.system.entity.expression.operation.type.EntityExpressionOperationType;
import com.system.bean.SystemBean;
import com.system.db.entity.named.NamedEntity;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The <class>EntityExpressionOperation</class> defines
 * various operations we can perform for a specific EntityExpressionAssignment.
 * <p>
 * The EntityExpressionOperation defines the type of operation to
 * observe if it occurs on the entity and also the bean defining what code to execute
 * for that operation.
 * <p>
 * Ex.
 * EntityExpressionAssignment for to Job
 * entityExpressionOperationType = Create
 * systemBean - SystemBean that execute the code (create/modify/delete/etc folders)
 * <p>
 *
 * @author Andrew Popp
 */
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"}),
        @UniqueConstraint(columnNames = {"entity_expression_operation_type_id", "system_bean_id"})
})
public class EntityExpressionOperation extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @ManyToOne(optional = false)
    @JoinColumn(name = "entity_expression_operation_type_id")
    private EntityExpressionOperationType entityExpressionOperationType;

    /**
     * The SystemBean that will actually execute the code that
     * will perform this operation on the provided entity
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "system_bean_id")
    private SystemBean systemBean;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public EntityExpressionOperation() {
    }

    public static EntityExpressionOperation newInstance(String name, String description, EntityExpressionOperationType entityExpressionOperationType, SystemBean systemBean) {
        EntityExpressionOperation entity = new EntityExpressionOperation();
        entity.setName(name);
        entity.setDescription(description);
        entity.setEntityExpressionOperationType(entityExpressionOperationType);
        entity.setSystemBean(systemBean);
        return entity;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public EntityExpressionOperationType getEntityExpressionOperationType() {
        return entityExpressionOperationType;
    }

    public void setEntityExpressionOperationType(EntityExpressionOperationType entityExpressionOperationType) {
        this.entityExpressionOperationType = entityExpressionOperationType;
    }

    public SystemBean getSystemBean() {
        return systemBean;
    }

    public void setSystemBean(SystemBean systemBean) {
        this.systemBean = systemBean;
    }
}
