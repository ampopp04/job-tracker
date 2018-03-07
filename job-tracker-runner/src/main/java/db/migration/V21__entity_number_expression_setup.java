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

import static db.migration.V20__entity_expression_schema.AFTER_CREATE_FIELD_SETTER_OPERATION_NAME;
import static db.migration.V20__entity_expression_schema.ON_CREATE_FIELD_SETTER_OPERATION_NAME;


/**
 * The <class>V21__entity_number_expression_setup</class> is a migration
 * that configures expressions that generate numbers to be set on specific fields for
 * various entities in the system.
 *
 * @author Andrew
 */
public class V21__entity_number_expression_setup extends BaseDataMigration {

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
        EntityExpressionOperation afterCreateExpressionFieldSetterOperation = getEntityExpressionOperationRepository().findByName(AFTER_CREATE_FIELD_SETTER_OPERATION_NAME);
        EntityExpressionOperation onCreateExpressionFieldSetterOperation = getEntityExpressionOperationRepository().findByName(ON_CREATE_FIELD_SETTER_OPERATION_NAME);

        //Create number generation expressions
        EntityExpression projectNumberEntityExpression = createAndSaveProjectNumberEntityExpression();
        EntityExpression jobNumberEntityExpression = createAndSaveJobNumberEntityExpression();
        EntityExpression taskNumberEntityExpression = createAndSaveTaskNumberEntityExpression();
        EntityExpression taskRevisionNumberEntityExpression = createAndSaveTaskRevisionNumberEntityExpression();

        //Assign these expressions to their associated entity so the number values get set on those entities number field on entity creation
        createAndSaveProjectNumberEntityAssignment(afterCreateExpressionFieldSetterOperation, projectNumberEntityExpression);
        createAndSaveJobNumberEntityAssignment(afterCreateExpressionFieldSetterOperation, jobNumberEntityExpression);
        createAndSaveTaskNumberEntityAssignment(afterCreateExpressionFieldSetterOperation, taskNumberEntityExpression);
        createAndSaveTaskRevisionNumberEntityAssignment(afterCreateExpressionFieldSetterOperation, taskRevisionNumberEntityExpression);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                          Entity Number Expressions                                 //////////
    //////////////////////////////////////////////////////////////////////

    public EntityExpression createAndSaveProjectNumberEntityExpression() {
        return getEntityExpressionRepository().save(
                EntityExpression.newInstance(
                        "Project Number Generator",
                        "An expression that is used to dynamically generate globally unique project numbers upon new project creations.",
                        "${(project.branch.projectCollection?size)?string[\"00000\"]}",
                        null,
                        getEntityExpressionTypeRepository().findByName(EntityExpressionType.NUMBER_EXPRESSION_TYPE_NAME)
                )
        );
    }

    public EntityExpression createAndSaveJobNumberEntityExpression() {
        return getEntityExpressionRepository().save(
                EntityExpression.newInstance(
                        "Job Number Generator",
                        "An expression that is used to dynamically generate globally unique job numbers upon new job creations via imported customer identifiers.",
                        "${job.customer.customerNumber}",
                        null,
                        getEntityExpressionTypeRepository().findByName(EntityExpressionType.NUMBER_EXPRESSION_TYPE_NAME)
                )
        );
    }

    public EntityExpression createAndSaveTaskNumberEntityExpression() {
        return getEntityExpressionRepository().save(
                EntityExpression.newInstance(
                        "Task Number Generator",
                        "An expression that is used to dynamically generate globally unique task numbers upon new task creations.",
                        "${task.job.children?size}",
                        null,
                        getEntityExpressionTypeRepository().findByName(EntityExpressionType.NUMBER_EXPRESSION_TYPE_NAME)
                )
        );
    }

    public EntityExpression createAndSaveTaskRevisionNumberEntityExpression() {
        return getEntityExpressionRepository().save(
                EntityExpression.newInstance(
                        "Task Revision Number Generator",
                        "An expression that is used to dynamically generate task revision numbers upon new task revision creation.",
                        "<#if taskRevision.task.taskRevisionCollection??>${taskRevision.task.taskRevisionCollection?size-1}<#else>0</#if>",
                        null,
                        getEntityExpressionTypeRepository().findByName(EntityExpressionType.NUMBER_EXPRESSION_TYPE_NAME)
                )
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                      Entity Expression Assignments                              //////////
    //////////////////////////////////////////////////////////////////////

    private void createAndSaveProjectNumberEntityAssignment(EntityExpressionOperation afterCreateExpressionFieldSetterOperation, EntityExpression numberEntityExpression) {
        createAndSaveFieldSetterCreationAssignment(
                "Project Number Creator",
                "Dynamically resolves the correct project number and sets it on new project creation.",
                "Project",
                "projectNumber",
                afterCreateExpressionFieldSetterOperation,
                numberEntityExpression
        );
    }

    private void createAndSaveJobNumberEntityAssignment(EntityExpressionOperation afterCreateExpressionFieldSetterOperation, EntityExpression numberEntityExpression) {
        createAndSaveFieldSetterCreationAssignment(
                "Job Number Creator",
                "Dynamically resolves the correct job number and sets it on new job creation.",
                "Job",
                "jobNumber",
                afterCreateExpressionFieldSetterOperation,
                numberEntityExpression
        );
    }

    private void createAndSaveTaskNumberEntityAssignment(EntityExpressionOperation afterCreateExpressionFieldSetterOperation, EntityExpression numberEntityExpression) {
        createAndSaveFieldSetterCreationAssignment(
                "Task Number Creator",
                "Dynamically resolves the correct task number and sets it on new task creation.",
                "Task",
                "taskNumber",
                afterCreateExpressionFieldSetterOperation,
                numberEntityExpression
        );
    }

    private void createAndSaveTaskRevisionNumberEntityAssignment(EntityExpressionOperation onCreateExpressionFieldSetterOperation, EntityExpression numberEntityExpression) {
        createAndSaveFieldSetterCreationAssignment(
                "Task Revision Number Creator",
                "Dynamically resolves the correct task revision number and sets it on new task revision creation.",
                "TaskRevision",
                "revisionNumber",
                onCreateExpressionFieldSetterOperation,
                numberEntityExpression
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                       Utility Methods                                            //////////
    //////////////////////////////////////////////////////////////////////

    public EntityExpressionAssignment createAndSaveFieldSetterCreationAssignment(String name, String description, String schemaTableName, String schemaTableColumnName, EntityExpressionOperation createExpressionFieldSetterOperation, EntityExpression jobNumberEntityExpression) {
        return getEntityExpressionAssignmentRepository().save(
                EntityExpressionAssignment.newInstance(
                        name,
                        description,
                        getSchemaTableRepository().findByName(schemaTableName),
                        getSchemaTableColumnRepository().findBySchemaTableNameAndName(schemaTableName, schemaTableColumnName),
                        jobNumberEntityExpression,
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