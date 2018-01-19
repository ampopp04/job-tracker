package com.job.tracker.system.entity.expression.operation.executor;

import com.job.tracker.system.entity.expression.assignment.EntityExpressionAssignment;
import com.system.db.entity.Entity;

/**
 * The <class>EntityExpressionOperationExecutor</class> defines
 * an interface for executing entity expression operations.
 * <p>
 * This interface will have implementations that actually perform operations.
 * <p>
 * Ex. The folder operation executions regarding the creating/deleting/updating
 * of folder paths in the file system.
 * <p>
 * Ex. The operation that will actually update or perform a dynamic calculation
 * for a given entity objects field. This means we can have dynamically
 * calculated fields for any entity and it's fields.
 *
 * @author Andrew Popp
 */
public interface EntityExpressionOperationExecutor {

    /**
     * Execute the entity expression operation provided the specified entity assignment details
     */
    public void execute(Entity entity, EntityExpressionAssignment entityExpressionAssignment);

}
