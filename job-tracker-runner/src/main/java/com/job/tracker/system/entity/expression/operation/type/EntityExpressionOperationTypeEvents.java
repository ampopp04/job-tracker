package com.job.tracker.system.entity.expression.operation.type;

/**
 * The <class>EntityExpressionOperationTypeEvents</class> defines
 * the types of assignment operation events that can occur on entities.
 *
 * @author Andrew Popp
 */
public enum EntityExpressionOperationTypeEvents {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * A new entity was created
     */
    CREATE,

    /**
     * After a new entity is created
     */
    AFTER_CREATE,

    /**
     * An entity was updated
     */
    UPDATE,

    /**
     * An entity was deleted
     */
    DELETE;
}
