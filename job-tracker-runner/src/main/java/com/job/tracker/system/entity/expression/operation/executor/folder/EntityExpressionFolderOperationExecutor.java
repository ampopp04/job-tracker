package com.job.tracker.system.entity.expression.operation.executor.folder;


import com.job.tracker.system.entity.expression.EntityExpression;
import com.job.tracker.system.entity.expression.assignment.EntityExpressionAssignment;
import com.job.tracker.system.entity.expression.operation.executor.EntityExpressionOperationExecutor;
import com.job.tracker.system.entity.expression.operation.type.EntityExpressionOperationType;
import com.job.tracker.system.entity.expression.operation.type.EntityExpressionOperationTypeEvents;
import com.job.tracker.system.entity.expression.resolver.EntityExpressionResolverService;
import com.system.db.entity.Entity;
import com.system.db.schema.datatype.SchemaDataTypeRepository;
import com.system.logging.exception.SystemException;
import com.system.logging.util.LogUtils;
import com.system.util.string.StringUtils;
import com.system.ws.entity.upload.util.EntityPropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.system.util.collection.CollectionUtils.*;

/**
 * The <class>EntityExpressionFolderOperationExecutor</class> performs
 * the execution defined by the EntityExpressionOperationExecutor interface.
 * <p>
 * In this case this executor implementation will handle all event types. We should split this up
 * into different implementations per event type.  Given an expression it will evaluate it as
 * a folder path and then create/delete/update that folder structure in the file system.
 *
 * @author Andrew Popp
 */
@Component
public class EntityExpressionFolderOperationExecutor implements EntityExpressionOperationExecutor {

    @Autowired
    private SchemaDataTypeRepository schemaDataTypeRepository;

    @Autowired
    private EntityExpressionResolverService entityExpressionResolverService;

    /**
     * Execute the folder path operation against the specified entity
     */
    public void execute(Entity entity, EntityExpressionAssignment entityExpressionAssignment) {
        List<String> entityExpressionExpandedList = resolveEntityExpression(entity, entityExpressionAssignment.getEntityExpression());
        iterate(iterable(entityExpressionExpandedList),
                entityExpressionExpanded ->
                        doExecute(entityExpressionExpanded, entityExpressionAssignment.getEntityExpressionOperation().getEntityExpressionOperationType())
        );
    }

    private List<String> resolveEntityExpression(Entity entity, EntityExpression entityExpression) {
        List<String> expandedExpressionList = resolveExpandedEntityExpressionChildExpressionList(entityExpression, entity);
        Set<String> resolvedEntityExpressionList = asSet();

        String expandedBaseEntityExpression = entityExpression.getEntityExpressionExpanded();
        resolvedEntityExpressionList.add(getEntityExpressionResolverService().resolve(entityExpression.getName(), expandedBaseEntityExpression, entity));

        for (String expandedExpression : iterable(expandedExpressionList)) {
            resolvedEntityExpressionList.add(getEntityExpressionResolverService().resolve(entityExpression.getName(), expandedBaseEntityExpression + expandedExpression, entity));
        }

        return new ArrayList<>(resolvedEntityExpressionList);
    }

    private List<String> resolveExpandedEntityExpressionChildExpressionList(EntityExpression entityExpression, Entity entity) {
        List<String> expandedExpressionList = asList();

        if (!StringUtils.isEmpty(entityExpression.getChildEntityExpressionPath())) {
            try {
                expandedExpressionList.addAll(
                        (
                                (EntityExpression) EntityPropertyUtils.getEntityBeanPropertyWrapper(entity)
                                        .getPropertyValue(entityExpression.getChildEntityExpressionPath())
                        ).getEntityExpressionExpandedWithChildrenExpandedList()
                );
            } catch (Exception e) {
                LogUtils.logInfo("Entity expression child expression cannot be resolved via path [" + entityExpression.getChildEntityExpressionPath() + "], Entity Type [" + entity != null ? entity.getClass().getName() : null + "]");
            }
        }

        return expandedExpressionList;
    }

    private void doExecute(String entityExpressionExpanded, EntityExpressionOperationType operationType) {
        try {
            EntityExpressionOperationTypeEvents operationTypeEvent = EntityExpressionOperationTypeEvents.valueOf(operationType.getName());

            switch (operationTypeEvent) {
                case CREATE:
                case AFTER_CREATE:
                    createFolderStructure(entityExpressionExpanded);
                    break;
                case DELETE:
                    deleteFolderStructure(entityExpressionExpanded);
                    break;
                case UPDATE:
                    updateFolderStructure(entityExpressionExpanded);
                    break;
            }
        } catch (Exception e) {
            LogUtils.logInfo("Error Creating Path: " + entityExpressionExpanded);
        }
    }

    private void createFolderStructure(String folderStructureExpanded) {
        LogUtils.logInfo("Creating Path: " + folderStructureExpanded);

        Path path = Paths.get(folderStructureExpanded);

        try {
            Files.createDirectories(path);
        } catch (InvalidPathException e) {
            throw SystemException.newInstance("Failed to create folder path [" + e.getInput() + "] at index [" + e.getIndex() + "] for reason [" + e.getReason() + "]", e);
        } catch (IOException e) {
            throw SystemException.newInstance(e.getMessage(), e);
        }
    }

    private void deleteFolderStructure(String folderStructureExpanded) {
        LogUtils.logInfo("Delete path: " + folderStructureExpanded);
        /*
        Path path = Paths.get(folderStructureExpanded);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }*/
    }

    private void updateFolderStructure(String folderStructureExpanded) {
        LogUtils.logInfo("Update path: " + folderStructureExpanded);
        /*
        Path path = Paths.get(folderStructureExpanded);
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }*/
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SchemaDataTypeRepository getSchemaDataTypeRepository() {
        return schemaDataTypeRepository;
    }

    public void setSchemaDataTypeRepository(SchemaDataTypeRepository schemaDataTypeRepository) {
        this.schemaDataTypeRepository = schemaDataTypeRepository;
    }

    public EntityExpressionResolverService getEntityExpressionResolverService() {
        return entityExpressionResolverService;
    }

    public void setEntityExpressionResolverService(EntityExpressionResolverService entityExpressionResolverService) {
        this.entityExpressionResolverService = entityExpressionResolverService;
    }
}


