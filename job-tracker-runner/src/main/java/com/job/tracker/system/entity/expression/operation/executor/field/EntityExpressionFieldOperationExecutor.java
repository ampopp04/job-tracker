package com.job.tracker.system.entity.expression.operation.executor.field;

import com.job.tracker.system.entity.expression.EntityExpression;
import com.job.tracker.system.entity.expression.assignment.EntityExpressionAssignment;
import com.job.tracker.system.entity.expression.operation.executor.EntityExpressionOperationExecutor;
import com.job.tracker.system.entity.expression.resolver.EntityExpressionResolverService;
import com.system.db.entity.Entity;
import com.system.ws.entity.upload.util.EntityPropertyUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.job.tracker.system.entity.expression.assertion.AssertUtils.assertNotNull;

/**
 * The <class>EntityExpressionFieldOperationExecutor</class> resolves
 * the provided expression against associated entity instance and then
 * sets the assigned property, on that entity instance, to whatever was dynamically computed.
 * <p>
 * This class actually executes an expression, takes its result and then reflectively injects it
 * into the correct field on that entity.
 * <p>
 * This usually occurs before a new entity is saved or updated.
 *
 * @author Andrew Popp
 */
@Component
public class EntityExpressionFieldOperationExecutor implements EntityExpressionOperationExecutor {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private EntityExpressionResolverService entityExpressionResolverService;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Execution                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    public void execute(Entity entity, EntityExpressionAssignment entityExpressionAssignment) {
        assertNotNull(entityExpressionAssignment.getSchemaTableColumn(), "Missing field on your entity expression assignment for [" + entityExpressionAssignment.toString() + "]");

        EntityExpression entityExpression = entityExpressionAssignment.getEntityExpression();
        String result = getEntityExpressionResolverService().resolve(entityExpression.getName(), entityExpression.getEntityExpressionExpanded(), entity);

        BeanWrapper entityWrapper = EntityPropertyUtils.getEntityBeanPropertyWrapper(entity);
        entityWrapper.setPropertyValue(entityExpressionAssignment.getSchemaTableColumn().getName(), result);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public EntityExpressionResolverService getEntityExpressionResolverService() {
        return entityExpressionResolverService;
    }

    public void setEntityExpressionResolverService(EntityExpressionResolverService entityExpressionResolverService) {
        this.entityExpressionResolverService = entityExpressionResolverService;
    }
}


