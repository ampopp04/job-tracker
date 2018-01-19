package com.job.tracker.system.entity.expression.operation.type;


import com.system.db.entity.named.NamedEntity;

import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * The <class>EntityExpressionOperationType</class> defines different
 * types of operations.
 * <p>
 * Ex. CREATE, DELETE, UPDATE
 *
 * @author Andrew Popp
 */
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class EntityExpressionOperationType extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public EntityExpressionOperationType() {
    }

    public static EntityExpressionOperationType newInstance(String name, String description) {
        EntityExpressionOperationType operationType = new EntityExpressionOperationType();
        operationType.setDescription(description);
        operationType.setName(name);
        return operationType;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

}
