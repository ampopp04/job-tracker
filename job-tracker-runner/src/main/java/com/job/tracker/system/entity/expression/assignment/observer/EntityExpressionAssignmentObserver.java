package com.job.tracker.system.entity.expression.assignment.observer;


import com.job.tracker.system.entity.expression.assignment.EntityExpressionAssignment;
import com.job.tracker.system.entity.expression.assignment.EntityExpressionAssignmentRepository;
import com.job.tracker.system.entity.expression.operation.executor.EntityExpressionOperationExecutor;
import com.job.tracker.system.entity.expression.operation.type.EntityExpressionOperationTypeEvents;
import com.system.bean.SystemBean;
import com.system.bean.service.SystemBeanService;
import com.system.db.entity.Entity;
import com.system.db.repository.base.entity.EntityRepository;
import com.system.util.stream.StreamUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.system.db.util.repository.RepositoryUtils.getRepositoryName;
import static com.system.util.collection.CollectionUtils.iterate;

/**
 * The <class>EntityExpressionAssignmentObserver</class> observes
 * actions on any entity defined within all EntityExpressionAssignments.
 * <p>
 * If one of these entities is subject to any database operations
 * it will be sent here for review.
 * <p>
 * This object will check to see if the operation performed was any of the
 * operations defined within the EntityExpressionOperation
 * as defined on all {EntityExpressionAssignments.entityExpressionOperation}
 * across all for this specific entities SchemaTable
 * <p>
 * If an operation of 'create' is performed then we will run the bean associated to that create operation.
 * Which will perform whatever operation it is designed to do.
 *
 * @author Andrew Popp
 */
@RepositoryEventHandler()
@Component
public class EntityExpressionAssignmentObserver {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private SystemBeanService systemBeanService;

    @Autowired
    private EntityExpressionAssignmentRepository entityExpressionAssignmentRepository;

    @Autowired
    private ApplicationContext applicationContext;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public EntityExpressionAssignmentObserver() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Observer Action                                                //////////
    //////////////////////////////////////////////////////////////////////

    //This is questional TODO, BEFORECREATE?
    @HandleBeforeCreate()
    public void handleAfterEntityCreate(Entity entity) {
        handleAfterEntityEvent(entity, EntityExpressionOperationTypeEvents.CREATE);
    }


    @HandleAfterCreate()
    public void handleAfterCreateEntity(Entity entity) {
        EntityRepository systemRepository = (EntityRepository) getApplicationContext().getBean(getRepositoryName(entity.getClass()));
        entity = (Entity) systemRepository.getOne(entity.getId());
        handleAfterEntityEvent(entity, EntityExpressionOperationTypeEvents.AFTER_CREATE);
        systemRepository.save(entity);
    }

    @HandleAfterDelete()
    public void handleAfterEntityDelete(Entity entity) {
        handleAfterEntityEvent(entity, EntityExpressionOperationTypeEvents.DELETE);
    }

    @HandleBeforeSave()
    public void handleAfterEntitySave(Entity entity) {
        handleAfterEntityEvent(entity, EntityExpressionOperationTypeEvents.UPDATE);
    }


    private void handleAfterEntityEvent(Entity entity, EntityExpressionOperationTypeEvents eventType) {
        List<EntityExpressionAssignment> entityExpressionAssignmentList = filterEntityExpressionAssignmentListByEventType(getEntityExpressionAssignmentsForEntity(entity), eventType);

        iterate(entityExpressionAssignmentList,
                entityExpressionAssignment ->
                        executeEntityExpressionOperation(entity, entityExpressionAssignment)
        );
    }

    private void executeEntityExpressionOperation(Entity entity, EntityExpressionAssignment entityExpressionAssignment) {
        SystemBean operationExecutorBean = entityExpressionAssignment.getEntityExpressionOperation().getSystemBean();

        EntityExpressionOperationExecutor operationExecutor =
                (EntityExpressionOperationExecutor) getSystemBeanService().getSystemBeanInstance(operationExecutorBean);

        operationExecutor.execute(entity, entityExpressionAssignment);
    }

    /**
     * Filter out entity expression assignments by the specific event that occurred
     */
    private List<EntityExpressionAssignment> filterEntityExpressionAssignmentListByEventType(List<EntityExpressionAssignment> entityExpressionAssignmentList, EntityExpressionOperationTypeEvents eventType) {
        return StreamUtils.stream(entityExpressionAssignmentList)
                .filter(
                        entityExpressionAssignment ->
                                entityExpressionAssignment.getEntityExpressionOperation().getEntityExpressionOperationType().getName().equals(eventType.name()))
                .collect(Collectors.toList());
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                         FolderPathAssignments Synchronization                        //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Return a list of all entity expression assignments for the provided entity.
     * This method is backed by a cache so that it has high performance.
     */
    private List<EntityExpressionAssignment> getEntityExpressionAssignmentsForEntity(Entity entity) {
        return getEntityExpressionAssignmentRepository().findBySchemaTableName(entity.getClass().getSimpleName());
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemBeanService getSystemBeanService() {
        return systemBeanService;
    }

    public void setSystemBeanService(SystemBeanService systemBeanService) {
        this.systemBeanService = systemBeanService;
    }

    public EntityExpressionAssignmentRepository getEntityExpressionAssignmentRepository() {
        return entityExpressionAssignmentRepository;
    }

    public void setEntityExpressionAssignmentRepository(EntityExpressionAssignmentRepository entityExpressionAssignmentRepository) {
        this.entityExpressionAssignmentRepository = entityExpressionAssignmentRepository;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
