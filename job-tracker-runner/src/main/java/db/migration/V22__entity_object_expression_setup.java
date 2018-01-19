package db.migration;


import com.job.tracker.system.entity.expression.EntityExpression;
import com.job.tracker.system.entity.expression.assignment.EntityExpressionAssignment;
import com.job.tracker.system.entity.expression.assignment.EntityExpressionAssignmentRepository;
import com.job.tracker.system.entity.expression.operation.EntityExpressionOperation;
import com.job.tracker.system.entity.expression.type.EntityExpressionType;
import com.system.db.migration.data.BaseDataMigration;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.db.schema.table.SchemaTable;
import com.system.db.schema.table.column.SchemaTableColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static db.migration.V19__entity_expression_schema.CREATE_OBJECT_OPERATION_NAME;

/**
 * The <class>V22__entity_object_expression_setup</class> is a migration
 * that configures expressions that generate objects that are set onto new entities.
 *
 * @author Andrew
 */
public class V22__entity_object_expression_setup extends BaseDataMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    @Autowired
    private NamedEntityRepository<EntityExpressionOperation> entityExpressionOperationRepository;

    @Autowired
    private EntityExpressionAssignmentRepository entityExpressionAssignmentRepository;

    @Autowired
    private NamedEntityRepository<EntityExpressionType> entityExpressionTypeRepository;

    @Autowired
    private NamedEntityRepository<EntityExpression> entityExpressionRepository;

    @Autowired
    private NamedEntityRepository<SchemaTable> schemaTableRepository;

    @Autowired
    private SchemaTableColumnRepository schemaTableColumnRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {
        EntityExpressionOperation afterCreateExpressionCreateObjectOperation = getEntityExpressionOperationRepository().findByName(CREATE_OBJECT_OPERATION_NAME);
        EntityExpression taskRevisionObjectEntityExpression = createAndSaveTaskRevisionObjectEntityExpression();
        createAndSaveTaskRevisionObjectEntityAssignment(afterCreateExpressionCreateObjectOperation, taskRevisionObjectEntityExpression);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                          Entity Object Expressions                                 //////////
    //////////////////////////////////////////////////////////////////////

    public EntityExpression createAndSaveTaskRevisionObjectEntityExpression() {
        return getEntityExpressionRepository().save(
                EntityExpression.newInstance(
                        "Task Revision Object Generator",
                        "An expression that is used to dynamically generate task revision objects upon new task creation.",
                        "T(com.job.tracker.task.revision.TaskRevision).newInstance(#root)",
                        null,
                        getEntityExpressionTypeRepository().findByName(EntityExpressionType.OBJECT_EXPRESSION_TYPE_NAME)
                )
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                      Entity Expression Assignments                              //////////
    //////////////////////////////////////////////////////////////////////

    private void createAndSaveTaskRevisionObjectEntityAssignment(EntityExpressionOperation afterCreateExpressionCreateObjectOperation, EntityExpression taskRevisionObjectEntityExpression) {
        createAndSaveFieldSetterCreationAssignment(
                "Task Revision Object Creator",
                "Dynamically creates the initial task revision object and saves it on new task creation.",
                "Task",
                null,
                afterCreateExpressionCreateObjectOperation,
                taskRevisionObjectEntityExpression
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Utility Methods                                            //////////
    //////////////////////////////////////////////////////////////////////

    public EntityExpressionAssignment createAndSaveFieldSetterCreationAssignment(String name, String description, String schemaTableName, String schemaTableColumnName, EntityExpressionOperation createExpressionFieldSetterOperation, EntityExpression jobObjectEntityExpression) {
        return getEntityExpressionAssignmentRepository().save(
                EntityExpressionAssignment.newInstance(
                        name,
                        description,
                        getSchemaTableRepository().findByName(schemaTableName),
                        getSchemaTableColumnRepository().findBySchemaTableNameAndName(schemaTableName, schemaTableColumnName),
                        jobObjectEntityExpression,
                        createExpressionFieldSetterOperation
                )
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public NamedEntityRepository<EntityExpressionType> getEntityExpressionTypeRepository() {
        return entityExpressionTypeRepository;
    }

    public void setEntityExpressionTypeRepository(NamedEntityRepository<EntityExpressionType> entityExpressionTypeRepository) {
        this.entityExpressionTypeRepository = entityExpressionTypeRepository;
    }

    public NamedEntityRepository<EntityExpressionOperation> getEntityExpressionOperationRepository() {
        return entityExpressionOperationRepository;
    }

    public void setEntityExpressionOperationRepository(NamedEntityRepository<EntityExpressionOperation> entityExpressionOperationRepository) {
        this.entityExpressionOperationRepository = entityExpressionOperationRepository;
    }

    public EntityExpressionAssignmentRepository getEntityExpressionAssignmentRepository() {
        return entityExpressionAssignmentRepository;
    }

    public void setEntityExpressionAssignmentRepository(EntityExpressionAssignmentRepository entityExpressionAssignmentRepository) {
        this.entityExpressionAssignmentRepository = entityExpressionAssignmentRepository;
    }

    public NamedEntityRepository<EntityExpression> getEntityExpressionRepository() {
        return entityExpressionRepository;
    }

    public void setEntityExpressionRepository(NamedEntityRepository<EntityExpression> entityExpressionRepository) {
        this.entityExpressionRepository = entityExpressionRepository;
    }

    public NamedEntityRepository<SchemaTable> getSchemaTableRepository() {
        return schemaTableRepository;
    }

    public void setSchemaTableRepository(NamedEntityRepository<SchemaTable> schemaTableRepository) {
        this.schemaTableRepository = schemaTableRepository;
    }

    public SchemaTableColumnRepository getSchemaTableColumnRepository() {
        return schemaTableColumnRepository;
    }

    public void setSchemaTableColumnRepository(SchemaTableColumnRepository schemaTableColumnRepository) {
        this.schemaTableColumnRepository = schemaTableColumnRepository;
    }
}