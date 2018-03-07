package db.migration;


import com.job.tracker.system.entity.expression.EntityExpression;
import com.job.tracker.system.entity.expression.assignment.EntityExpressionAssignment;
import com.job.tracker.system.entity.expression.assignment.EntityExpressionAssignmentRepository;
import com.job.tracker.system.entity.expression.operation.EntityExpressionOperation;
import com.job.tracker.system.entity.expression.type.EntityExpressionType;
import com.job.tracker.task.type.TaskType;
import com.system.db.migration.data.BaseDataMigration;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.db.schema.table.SchemaTable;
import com.system.db.schema.table.column.SchemaTableColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

import static db.migration.V20__entity_expression_schema.AFTER_CREATE_FIELD_SETTER_OPERATION_NAME;
import static db.migration.V20__entity_expression_schema.CREATE_FOLDER_OPERATION_NAME;

/**
 * The <class>V22__entity_folder_expression_setup</class> is a migration
 * that configures folder expression setup for creating various file-system folder expression
 * paths upon various entity database operations (create,update,delete,before,after)
 *
 * @author Andrew
 */
public class V22__entity_folder_expression_setup extends BaseDataMigration {

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

    @Autowired
    private NamedEntityRepository<TaskType> taskTypeRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {
        EntityExpressionOperation createFolderOperation = getEntityExpressionOperationRepository().findByName(CREATE_FOLDER_OPERATION_NAME);
        EntityExpressionOperation afterCreateExpressionFieldSetterOperation = getEntityExpressionOperationRepository().findByName(AFTER_CREATE_FIELD_SETTER_OPERATION_NAME);

        processProjectFolderSetup(createFolderOperation, afterCreateExpressionFieldSetterOperation);
        processJobFolderSetup(createFolderOperation, afterCreateExpressionFieldSetterOperation);
        processTaskFolderSetup(createFolderOperation, afterCreateExpressionFieldSetterOperation);
        processTaskRevisionFolderSetup(createFolderOperation, afterCreateExpressionFieldSetterOperation);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                Base Folder Expressions                                //////////
    //////////////////////////////////////////////////////////////////////

    public EntityExpression createAndSaveBranchCodeEntityExpression() {
        return createAndSaveEntityExpression(
                "Branch Code Folder Path",
                "A 2 digit branch code folder path for creating the top level folder structure of a project.",
                "${project.branch.folderLocation}${project.branch.code}" + File.separator,
                null
        );
    }

    public EntityExpression createAndSaveBaseProjectFolderPathEntityExpression() {
        return createAndSaveEntityExpression(
                "Project Base Folder Path",
                "Defines the base folder path for a project.",
                "${project.name} - ${project.branch.code}${project.projectNumber}" + File.separator,
                createAndSaveBranchCodeEntityExpression()
        );
    }

    public EntityExpression createAndSaveBaseTaskFolderPathEntityExpression() {
        return createAndSaveEntityExpression(
                "Task Base Folder Path",
                "Defines the base folder path for a task given it's associated information.",
                "${task.job.folderLocation}${task.taskType.folderName}/${task.taskNumber} - ${task.job.customer.name?replace(\"[^a-zA-Z0-9\\\\.\\\\-]\", \"_\", \"r\")} - ${task.name}" + File.separator,
                null
        );
    }

    public EntityExpression createAndSaveBaseTaskRevisionFolderPathEntityExpression() {
        return createAndSaveEntityExpressionWithChild(
                "Task Revision Base Folder Path",
                "Defines the base folder path for a task revisions given it's associated information.",
                "${task.folderLocation}Rev ${taskRevision.revisionNumber}" + File.separator,
                "task.taskType.entityExpression",
                null
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                          Entity Folder Creation and Field Set                               //////////
    //////////////////////////////////////////////////////////////////////

    private void processProjectFolderSetup(EntityExpressionOperation createFolderOperation, EntityExpressionOperation afterCreateExpressionFieldSetterOperation) {
        EntityExpression baseProjectFolderPathEntityExpression = createAndSaveBaseProjectFolderPathEntityExpression();

        //Create Project Folder and Set it on field
        assignExpressionAndOperationToEntity(
                "Project Branch Folder Creation",
                "Creates the branch code top level folder when a new project is created.",
                "Project",
                null,
                createFolderOperation,
                baseProjectFolderPathEntityExpression
        );
        assignExpressionAndOperationToEntity(
                "Project Folder Location Setter",
                "Dynamically resolves the correct folder location and sets it on new project creation.",
                "Project",
                "folderLocation",
                afterCreateExpressionFieldSetterOperation,
                baseProjectFolderPathEntityExpression
        );


        //Create the folder 0-BID DOCS when a new project is created, if the folder doesn't already exist
        assignExpressionAndOperationToEntity(
                "Bid Docs Folder Creation",
                "Creates the project Bid Docs folder when a new project is created.",
                "Project",
                null,
                createFolderOperation,
                createAndSaveEntityExpression(
                        "Bid Docs Folder Expression",
                        "The expression that creates the Bid Docs folder path.",
                        "0-BID DOCS",
                        baseProjectFolderPathEntityExpression
                )
        );

        //Create the folder 1-DESIGN CRITERIA when a new project is created, if the folder doesn't already exist
        assignExpressionAndOperationToEntity(
                "Design Criteria Folder Creation",
                "Creates the project Design Criteria folder when a new project is created.",
                "Project",
                null,
                createFolderOperation,
                createAndSaveEntityExpression(
                        "Design Criteria Folder Expression",
                        "The expression that creates the Design Criteria folder path.",
                        "1-DESIGN CRITERIA",
                        baseProjectFolderPathEntityExpression
                )
        );
    }

    private void processJobFolderSetup(EntityExpressionOperation createFolderOperation, EntityExpressionOperation afterCreateExpressionFieldSetterOperation) {

        //Create the folder ${job.project.jobCollection?size + 1}-${job.customer.name} when a new job is created, if the folder doesn't already exist
        EntityExpression jobCustomerFolderExpression = createAndSaveEntityExpression(
                "Job Customer Folder Expression",
                "The expression that creates the Job Customer folder path.",
                "${job.project.folderLocation}${job.project.jobCollection?size + 1}-${job.customer.name?replace(\"[^a-zA-Z0-9\\\\.\\\\-]\", \"_\", \"r\")}" + File.separator,
                null
        );

        assignExpressionAndOperationToEntity(
                "Job Customer Folder Creation",
                "Creates the jobs Customer folder when a new job is created.",
                "Job",
                null,
                createFolderOperation,
                jobCustomerFolderExpression
        );

        assignExpressionAndOperationToEntity(
                "Job Folder Location Setter",
                "Dynamically resolves the correct folder location and sets it on new job creation.",
                "Job",
                "folderLocation",
                afterCreateExpressionFieldSetterOperation,
                jobCustomerFolderExpression
        );

        createRemainingJobCustomerFolderStructure(jobCustomerFolderExpression, createFolderOperation);
    }

    private void processTaskFolderSetup(EntityExpressionOperation createFolderOperation, EntityExpressionOperation afterCreateExpressionFieldSetterOperation) {
        EntityExpression baseTaskFolderPathEntityExpression = createAndSaveBaseTaskFolderPathEntityExpression();

        //Create Task Folder and Set it on field
        assignExpressionAndOperationToEntity(
                "Task Base Folder Creation",
                "Creates the task base top level folder when a new task is created.",
                "Task",
                null,
                createFolderOperation,
                baseTaskFolderPathEntityExpression
        );
        assignExpressionAndOperationToEntity(
                "Task Folder Location Setter",
                "Dynamically resolves the correct folder location and sets it on new task creation.",
                "Task",
                "folderLocation",
                afterCreateExpressionFieldSetterOperation,
                baseTaskFolderPathEntityExpression
        );
    }

    private void processTaskRevisionFolderSetup(EntityExpressionOperation createFolderOperation, EntityExpressionOperation afterCreateExpressionFieldSetterOperation) {
        EntityExpression baseTaskRevisionFolderPathEntityExpression = createAndSaveBaseTaskRevisionFolderPathEntityExpression();

        //Create Task Revision Folder and Set it on field
        assignExpressionAndOperationToEntity("Task Revision Base Folder Creation",
                "Creates the task revision base top level folder when a new task revision is created.",
                "TaskRevision",
                null,
                createFolderOperation,
                baseTaskRevisionFolderPathEntityExpression
        );
        assignExpressionAndOperationToEntity(
                "Task Revision Folder Location Setter",
                "Dynamically resolves the correct folder location and sets it on new task revision creation.",
                "TaskRevision",
                "folderLocation",
                afterCreateExpressionFieldSetterOperation,
                baseTaskRevisionFolderPathEntityExpression
        );

        createAndSaveFileSeparatorEntityExpression();
        EntityExpression emptyEntityExpression = createAndSaveEmptyPathEntityExpression();

        EntityExpression revisionSubFolderPathEntityExpression = createAndSaveTaskRevisionSubFolderPathEntityExpression();
        createRemainingTaskRevisionSubfolderStructure(revisionSubFolderPathEntityExpression);


        TaskType detailingTask = getTaskTypeRepository().findByName("Detailing");
        detailingTask.setEntityExpression(emptyEntityExpression);
        getTaskTypeRepository().save(detailingTask);

        TaskType engineeringTask = getTaskTypeRepository().findByName("Engineering");
        engineeringTask.setEntityExpression(revisionSubFolderPathEntityExpression);
        getTaskTypeRepository().save(engineeringTask);

    }

    public EntityExpression createAndSaveFileSeparatorEntityExpression() {
        return createAndSaveEntityExpression(
                "File Separator Expression",
                "Defines an expression that resolves this server types directory separator character; / or \\.",
                File.separator,
                null
        );
    }

    public EntityExpression createAndSaveEmptyPathEntityExpression() {
        return createAndSaveEntityExpression(
                "Empty Expression",
                "Defines an empty expression.",
                "",
                null
        );
    }

    public EntityExpression createAndSaveTaskRevisionSubFolderPathEntityExpression() {
        return createAndSaveEntityExpression(
                "Revision Subfolders",
                "Defines the subfolders that can go under a revision",
                "",
                null
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                Helper Methods                                                  //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Need to create all the nested folders under the job base folder path
     */
    private void createRemainingTaskRevisionSubfolderStructure(EntityExpression revisionSubFolderPathEntityExpression) {
        createAndSaveEntityExpression(
                "BID DOCS Folder Expression",
                "The expression that creates the BID DOCS folder path.",
                "BID DOCS",
                revisionSubFolderPathEntityExpression
        );

        createAndSaveEntityExpression(
                "CAD Files Folder Expression",
                "The expression that creates the CAD Files folder path.",
                "CAD Files",
                revisionSubFolderPathEntityExpression
        );

        createAndSaveEntityExpression(
                "CAD_pdf Folder Expression",
                "The expression that creates the CAD_pdf folder path.",
                "CAD_pdf",
                revisionSubFolderPathEntityExpression
        );

        createAndSaveEntityExpression(
                "Calcs Folder Expression",
                "The expression that creates the Calcs folder path.",
                "Calcs",
                revisionSubFolderPathEntityExpression
        );

        createAndSaveEntityExpression(
                "Emails Folder Expression",
                "The expression that creates the Emails folder path.",
                "Emails",
                revisionSubFolderPathEntityExpression
        );

        createAndSaveEntityExpression(
                "Plan Check Comments Folder Expression",
                "The expression that creates the Plan Check Comments folder path.",
                "Plan Check Comments",
                revisionSubFolderPathEntityExpression
        );

        createAndSaveEntityExpression(
                "Quote and PO Folder Expression",
                "The expression that creates the Quote and PO folder path.",
                "Quote and PO",
                revisionSubFolderPathEntityExpression
        );

    }

    private void createRemainingJobCustomerFolderStructure(EntityExpression baseJobCustomerFolderPathEntityExpression, EntityExpressionOperation createFolderOperation) {

        //Create the folder 1-LEGENDS when a new job is created, if the folder doesn't already exist
        createFolderStructureWithAssignment(
                "Job",
                "1-",
                "LEGENDS",
                baseJobCustomerFolderPathEntityExpression,
                createFolderOperation
        );

        //Create the folder 2-CAD when a new job is created, if the folder doesn't already exist
        createFolderStructureWithAssignment(
                "Job",
                "2-",
                "CAD",
                baseJobCustomerFolderPathEntityExpression,
                createFolderOperation
        );

        //Create the folder 3-SUBMITTALS when a new job is created, if the folder doesn't already exist
        createFolderStructureWithAssignment(
                "Job",
                "3-",
                "SUBMITTALS",
                baseJobCustomerFolderPathEntityExpression,
                createFolderOperation
        );

        //Create the folder 4-RECEIVED when a new job is created, if the folder doesn't already exist
        createFolderStructureWithAssignment(
                "Job",
                "4-",
                "RECEIVED",
                baseJobCustomerFolderPathEntityExpression,
                createFolderOperation
        );

        //Create the folder 5-CUSTOM ENGINEERING when a new job is created, if the folder doesn't already exist
        createFolderStructureWithAssignment(
                "Job",
                "5-",
                "CUSTOM ENGINEERING",
                baseJobCustomerFolderPathEntityExpression,
                createFolderOperation
        );

        //Create the folder 6-CORRESPONDENCE when a new job is created, if the folder doesn't already exist
        createFolderStructureWithAssignment(
                "Job",
                "6-",
                "CORRESPONDENCE",
                baseJobCustomerFolderPathEntityExpression,
                createFolderOperation
        );

    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Utility Methods                                              //////////
    //////////////////////////////////////////////////////////////////////

    private void createFolderStructureWithAssignment(String schemaTableName, String folderNumberPrefix, String folderName, EntityExpression parentExpression, EntityExpressionOperation createFolderOperation) {
        String tableAndFolderName = schemaTableName + " " + folderName;

        assignExpressionAndOperationToEntity(
                tableAndFolderName + " Folder Creation",
                "Creates the " + tableAndFolderName + " folder when a new " + schemaTableName + " is created.",
                schemaTableName,
                null,
                createFolderOperation,
                createAndSaveEntityExpression(
                        tableAndFolderName + " Folder Expression",
                        "The expression that creates the " + tableAndFolderName + " folder path.",
                        folderNumberPrefix + folderName,
                        parentExpression
                )
        );
    }

    public EntityExpressionAssignment assignExpressionAndOperationToEntity(String name, String description, String schemaTableName, String schemaTableColumnName, EntityExpressionOperation entityExpressionOperation, EntityExpression entityExpression) {
        return getEntityExpressionAssignmentRepository().save(
                EntityExpressionAssignment.newInstance(
                        name,
                        description,
                        getSchemaTableRepository().findByName(schemaTableName),
                        getSchemaTableColumnRepository().findBySchemaTableNameAndName(schemaTableName, schemaTableColumnName),
                        entityExpression,
                        entityExpressionOperation
                )
        );
    }

    public EntityExpression createAndSaveEntityExpressionWithChild(String name, String description, String entityExpressionString, String childEntityExpressionString, EntityExpression parentExpression) {
        return getEntityExpressionRepository().save(
                EntityExpression.newInstance(
                        name,
                        description,
                        entityExpressionString,
                        childEntityExpressionString,
                        parentExpression,
                        getEntityExpressionTypeRepository().findByName(EntityExpressionType.FOLDER_EXPRESSION_TYPE_NAME)
                )
        );
    }

    public EntityExpression createAndSaveEntityExpression(String name, String description, String entityExpressionString, EntityExpression parentExpression) {
        return createAndSaveEntityExpressionWithChild(name, description, entityExpressionString, null, parentExpression);
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

    public EntityExpressionAssignmentRepository getEntityExpressionAssignmentRepository() {
        return entityExpressionAssignmentRepository;
    }

    public void setEntityExpressionAssignmentRepository(EntityExpressionAssignmentRepository entityExpressionAssignmentRepository) {
        this.entityExpressionAssignmentRepository = entityExpressionAssignmentRepository;
    }

    public SchemaTableColumnRepository getSchemaTableColumnRepository() {
        return schemaTableColumnRepository;
    }

    public void setSchemaTableColumnRepository(SchemaTableColumnRepository schemaTableColumnRepository) {
        this.schemaTableColumnRepository = schemaTableColumnRepository;
    }

    public NamedEntityRepository<TaskType> getTaskTypeRepository() {
        return taskTypeRepository;
    }

    public void setTaskTypeRepository(NamedEntityRepository<TaskType> taskTypeRepository) {
        this.taskTypeRepository = taskTypeRepository;
    }
}