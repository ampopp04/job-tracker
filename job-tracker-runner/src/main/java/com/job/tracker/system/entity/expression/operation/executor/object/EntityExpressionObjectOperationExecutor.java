package com.job.tracker.system.entity.expression.operation.executor.object;


import com.job.tracker.system.entity.expression.EntityExpression;
import com.job.tracker.system.entity.expression.assignment.EntityExpressionAssignment;
import com.job.tracker.system.entity.expression.operation.executor.EntityExpressionOperationExecutor;
import com.job.tracker.system.entity.expression.operation.type.EntityExpressionOperationType;
import com.job.tracker.system.entity.expression.operation.type.EntityExpressionOperationTypeEvents;
import com.job.tracker.system.entity.expression.resolver.EntityExpressionResolverService;
import com.system.db.entity.Entity;
import com.system.db.entity.base.BaseEntity;
import com.system.db.repository.base.entity.SystemRepository;
import com.system.db.util.repository.RepositoryUtils;
import com.system.logging.exception.SystemException;
import com.system.logging.util.LogUtils;
import com.system.ws.entity.upload.util.EntityEventOperationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * The <class>EntityExpressionObjectOperationExecutor</class> performs
 * the execution defined by the EntityExpressionOperationExecutor interface.
 * <p>
 * In this case this executor implementation will handle creating objects and saving them based on an expression produced and the
 * associated entity as the expressions meta-model data.
 *
 * @author Andrew Popp
 */
@Component
public class EntityExpressionObjectOperationExecutor implements EntityExpressionOperationExecutor {

    @Autowired
    private EntityExpressionResolverService entityExpressionResolverService;

    @Autowired
    private AutowireCapableBeanFactory beanFactory;

    @Autowired
    private ApplicationEventPublisher publisher;

    /**
     * Execute the object creation operation against the specified entity
     */
    public void execute(Entity entity, EntityExpressionAssignment entityExpressionAssignment) {
        EntityExpression objectEntityExpression = entityExpressionAssignment.getEntityExpression();
        Object newObject = getEntityExpressionResolverService().resolveSpelExpression(objectEntityExpression, entity);
        doExecute(newObject, entityExpressionAssignment.getEntityExpressionOperation().getEntityExpressionOperationType());
    }

    private void doExecute(Object newObject, EntityExpressionOperationType operationType) {
        try {
            EntityExpressionOperationTypeEvents operationTypeEvent = EntityExpressionOperationTypeEvents.valueOf(operationType.getName());

            switch (operationTypeEvent) {
                case CREATE:
                case AFTER_CREATE:
                    createAndSaveObject(newObject);
                    break;
                case DELETE:
                    //deleteFolderStructure(entityExpressionExpanded);
                    break;
                case UPDATE:
                    //updateFolderStructure(entityExpressionExpanded);
                    break;
            }
        } catch (Exception e) {
            LogUtils.logInfo("Error Creating Object: " + newObject);
        }
    }

    private void createAndSaveObject(Object newObject) {
        LogUtils.logInfo("Creating & Saving Object: " + newObject);

        try {
            saveEntityObject(newObject);
        } catch (Exception e) {
            throw SystemException.newInstance(e.getMessage(), e);
        }
    }

    private void saveEntityObject(Object newObject) {
        BaseEntity newEntity = (BaseEntity) newObject;
        EntityEventOperationUtils.doHandleInsert(newEntity, ((SystemRepository) getBeanFactory().getBean(RepositoryUtils.getRepositoryName(newEntity.getClass()))), getPublisher());
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

    public AutowireCapableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public ApplicationEventPublisher getPublisher() {
        return publisher;
    }

    public void setPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
}


