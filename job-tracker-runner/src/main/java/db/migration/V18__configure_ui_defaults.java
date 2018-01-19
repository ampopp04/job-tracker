package db.migration;


import com.job.tracker.Employee.Employee;
import com.job.tracker.branch.Branch;
import com.job.tracker.customer.Customer;
import com.job.tracker.department.Department;
import com.job.tracker.job.Job;
import com.job.tracker.note.SystemEntityNote;
import com.job.tracker.note.type.SystemEntityNoteType;
import com.job.tracker.project.Project;
import com.job.tracker.project.type.ProjectType;
import com.job.tracker.system.entity.expression.EntityExpression;
import com.job.tracker.system.entity.expression.assignment.EntityExpressionAssignment;
import com.job.tracker.system.entity.expression.operation.EntityExpressionOperation;
import com.job.tracker.system.entity.expression.operation.type.EntityExpressionOperationType;
import com.job.tracker.task.Task;
import com.job.tracker.task.hour.TaskHour;
import com.job.tracker.task.revision.TaskRevision;
import com.job.tracker.task.status.TaskStatus;
import com.job.tracker.task.type.TaskType;
import com.system.db.migration.data.BaseDataMigration;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.db.schema.table.SchemaTable;
import com.system.db.schema.table.column.SchemaTableColumn;
import com.system.db.schema.table.column.SchemaTableColumnRepository;
import com.system.security.privilege.SystemSecurityPrivilege;
import com.system.security.role.SystemSecurityRole;
import com.system.security.user.SystemSecurityUser;
import com.system.ws.entity.upload.util.EntityPropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.system.util.collection.CollectionUtils.*;


/**
 * The <class>V18__configure_ui_defaults</class> is a migration
 * that configures which columns are hidden or shown by default within the UI.
 * <p>
 * It also configures what their display names will be (What their labels will be for each column/field) and
 * the order in which the column/fields are displayed. This is for any UI component that is generally
 * dynamically generated.
 * <p>
 * These can be overridden in JS code if more case specific behavior is desired.
 *
 * @author Andrew
 */
public class V18__configure_ui_defaults extends BaseDataMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SchemaTableColumnRepository schemaTableColumnRepository;

    @Autowired
    private NamedEntityRepository<SchemaTable> schemaTableRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {

        updateEntityUi(EntityExpressionOperationType.class,
                update("name", "Name"),
                update("description", "Description")
        );

        updateEntityUi(EntityExpressionOperation.class,
                update("name", "Name"),
                update("description", "Description"),
                update("entityExpressionOperationType", "Operation Type"),
                update("systemBean", "Operation Executor")
        );

        updateEntityUi(EntityExpression.class,
                update("name", "Name"),
                update("description", "Description", "{}", "{hidden:true}"),
                update("entityExpression", "Expression", "{xtype: 'textarea', grow: true}"),
                update("parentEntityExpression", "Parent"),
                update("entityExpressionType", "Type", "{allowBlank:false}"),
                update("childEntityExpressionPath", "Child Expression Path")
        );

        updateEntityUi(EntityExpressionAssignment.class,
                update("name", "Name"),
                update("schemaTable", "Entity Assignment"),
                update("schemaTableColumn", "Field Assignment"),
                update("entityExpression", "Expression"),
                update("entityExpressionOperation", "Operation")
        );

        updateEntityUi(Department.class,
                update("name", "Name", "{allowBlank:false}"),
                update("description", "Description")
        );

        updateEntityUi(Branch.class,
                update("name", "Name", "{allowBlank:false}"),
                update("code", "Code", "{allowBlank:false}"),
                update("folderLocation", "Folder Location", "{allowBlank:false}"),
                update("description", "Description", "{hidden:true}")
        );

        updateEntityUi(SystemSecurityPrivilege.class,
                update("name", "Name"),
                update("description", "Description")
        );

        updateEntityUi(SystemSecurityRole.class,
                update("name", "Name"),
                update("description", "Description")
        );

        updateEntityUi(SystemSecurityUser.class,
                update("name", "Name", "{hidden:true}"),
                update("description", "Description", "{hidden:true}"),
                update("firstName", "First Name", "{allowBlank:false}"),
                update("lastName", "Last Name", "{allowBlank:false}"),
                update("username", "Username", "{allowBlank:false}"),
                update("password", "Hashed Password"),

                update("userPrincipalName", "User Principal Name", "{allowBlank:false}"),
                update("distinguishedName", "Distinguished Name", "{allowBlank:false}"),

                update("enabled", "Active")
        );

        updateEntityUi(Employee.class,
                update("name", "Name", "{hidden:true}", "{hidden:false}"),
                update("description", "Description", "{hidden:true}"),
                update("firstName", "First Name", "{allowBlank:false}", "{hidden:true}"),
                update("lastName", "Last Name", "{allowBlank:false}", "{hidden:true}"),
                update("jobTitle", "Job Title", "{allowBlank:false}"),
                update("businessPhone", "Business Phone"),
                update("mobilePhone", "Mobile Phone", null, "{hidden:true}"),
                update("emailAddress", "E-Mail Address"),
                update("manager", "Manager", null, "{hidden:true}", "{reference:{parent: 'Employees', unique: true} }"),
                update("department", "Department", "{allowBlank:false}"),
                update("branch", "Office", "{allowBlank:false}"),
                update("company", "Company", null, "{hidden:true}"),
                update("streetAddress", "Address", null, "{hidden:true}"),
                update("city", "City", null, "{hidden:true}"),
                update("state", "State", null, "{hidden:true}"),
                update("zipCode", "Zip Code", null, "{hidden:true}"),
                update("country", "Country", null, "{hidden:true}"),
                update("systemSecurityUser", "Security Account", null, "{hidden:true}"),
                update("enabled", "Active", "{hidden:true}"),
                update("passwordExpired", "Password Expired", "{hidden:true}")
        );

        updateEntityUi(Customer.class,
                update("customerNumber", "Customer #", "{allowBlank:false, maskIfInitialValueExists: true}"),
                update("name", "Name", "{allowBlank:false}"),
                update("status", "Status", "{allowBlank:false}"),
                update("addressLine1", "Address Line 1"),
                update("addressLine2", "Address Line 2", null, "{hidden:true}"),
                update("addressLine3", "Address Line 3", null, "{hidden:true}"),
                update("city", "City"),
                update("state", "State"),
                update("zipCode", "Zip Code"),
                update("phoneNumber", "Phone Number"),
                update("description", "Description", "{hidden:true}")
        );

        updateEntityUi(TaskStatus.class,
                update("name", "Name"),
                update("description", "Description", "{hidden:true}")
        );

        updateEntityUi(Project.class,
                update("projectNumber", "Project Number", "{xtype: 'system-field-text',hideIfNull:true}"),
                update("folderLocation", "Folder Location", "{xtype: 'system-field-text',hideIfNull:true}", "{hidden:true}"),
                update("projectType", "Project Type", "{allowBlank:false}"),
                update("name", "Name", "{allowBlank:false, maskIfInitialValueExists: true}"),
                update("address", "Address"),
                update("city", "City"),
                update("state", "State"),
                update("zipCode", "Zip"),
                update("branch", "Branch", "{allowBlank:false,defaultLoadFilters:[{ \"operator\": \"=\", \"value\": System.util.system.UserUtils.getLoggedInEmployee().branch, \"property\": \"name\" }]}"),
                update("description", "Description", "{hidden:true}")
        );

        updateEntityUi(Job.class,
                update("jobNumber", "Job Number", "{xtype: 'system-field-text',hideIfNull:true}"),
                update("folderLocation", "Folder Location", "{xtype: 'system-field-text',hideIfNull:true}", "{hidden:true}"),
                update("name", "Name", "{hidden:true}"),
                update("project", "Project", "{maskIfInitialValueExists: true,allowBlank:false}"),
                update("customer", "Customer", "{maskIfInitialValueExists: true,allowBlank:false}"),
                update("poNumber", "PO #"),
                update("amount", "Amount", "{xtype: 'system-field-currency'}", "{xtype: 'numbercolumn', renderer: Ext.util.Format.usMoney, filter: {type: 'number',hideTrigger: true,keyNavEnabled: false,mouseWheelEnabled: false}}"),
                update("salesRep", "Sales Rep"),
                update("projectManager", "Project Manager"),
                update("date", "Date"),
                update("description", "Description", "{hidden:true}")
        );

        updateEntityUi(TaskType.class,
                update("name", "Name", "{allowBlank:false}"),
                update("department", "Department"),
                update("folderName", "Folder Name", "{allowBlank:false}"),
                update("entityExpression", "Subfolders", "{allowBlank:false}"),
                update("description", "Description", "{hidden:true}")
        );

        updateEntityUi(ProjectType.class,
                update("name", "Name", "{allowBlank:false}"),
                update("description", "Description", "{hidden:true}")
        );

        updateEntityUi(TaskRevision.class,
                update("revisionNumber", "Revision Number", "{xtype: 'system-field-text',hideIfNull:true}"),
                update("folderLocation", "Folder Location", "{xtype: 'system-field-text',hideIfNull:true}", "{hidden:true}"),
                update("task", "Task", "{maskIfInitialValueExists: true,allowBlank:false}"),

                update("name", "Description", "{xtype: 'textarea', grow: true}"),
                update("description", "Description", "{hidden:true}"),

                update("date", "Date")
        );

        updateEntityUi(Task.class,
                update("taskNumber", "Task Number", "{xtype: 'system-field-text',hideIfNull:true}"),
                update("folderLocation", "Folder Location", "{xtype: 'system-field-text',hideIfNull:true}", "{hidden:true}"),
                update("job", "Job", "{maskIfInitialValueExists: true,allowBlank:false}"),
                update("taskType", "Task Type", "{maskIfInitialValueExists: true,allowBlank:false}"),
                update("name", "Description", "{allowBlank:false}"),
                update("description", "Extended Description", "{hidden:true}"),
                update("taskStatus", "Status", "{value:'1',allowBlank:false}", "{allowBlank:false}"),
                update("assignedTo", "Assigned To", "{}"),
                update("date", "Date")
        );

        updateEntityUi(TaskHour.class,
                update("task", "Task", "{allowBlank:false}", "{hidden:true}"),
                update("taskRevision", "Revision", "{allowBlank:false}"),

                update("date", "Date", "{xtype: 'datefield',anchor: '100%',value: new Date(),format: 'm-d-Y',allowBlank:false}"),
                update("employee", "Employee", "{allowBlank:false}"),
                update("hours", "Hours", "{xtype: 'numberfield',hideTrigger: true,keyNavEnabled: false,mouseWheelEnabled: false,decimalPrecision:0,allowBlank:false}", "{xtype: 'numbercolumn', format:'0', filter: {type: 'number',hideTrigger: true,keyNavEnabled: false,mouseWheelEnabled: false}, summaryType: 'sum'}"),

                update("name", "Description", "{allowBlank:false,minLength:5,xtype: 'textarea', grow: true}"),
                update("description", "Extended Description", "{hidden:true}")
        );

        updateEntityUi(SystemEntityNoteType.class,
                update("schemaTableColumn", "Notes For", "{maskIfInitialValueExists: true}"),
                update("name", "Name", "{allowBlank:false}"),
                update("description", "Description")
        );

        updateEntityUi(SystemEntityNote.class,
                update("systemEntityNoteType", "Note Type", "{maskIfInitialValueExists: true}", "{hidden:true}"),
                update("name", "Text", "{xtype: 'textarea', grow: true}", "{allowBlank:false}"),
                update("description", "Extended Description", "{hidden:true}"),
                update("fkEntityId", "Link", "{allowBlank:false,hidden:true}")
        );

        setSchemaTableUiCacheDefaults();

    }

    private void setSchemaTableUiCacheDefaults() {
        List<String> enableTableCacheList = asList(
                "SchemaTable",
                "SchemaTableColumn",
                "UiComponentDefinition"
        );

        iterate(iterable(enableTableCacheList), schemaTableName -> enableSchemaTableCache(schemaTableName));
    }

    private void enableSchemaTableCache(String schemaTableName) {
        SchemaTable schemaTable = getSchemaTableRepository().findByName(schemaTableName);
        schemaTable.setCacheEnabled(true);
        getSchemaTableRepository().save(schemaTable);
    }

    private void updateEntityUi(Class tableEntityClass, SchemaTableColumn... schemaTableColumns) {
        updateTableUiConfiguration(tableEntityClass, asList(schemaTableColumns));
    }

    private void updateTableUiConfiguration(Class tableEntityClass, List<SchemaTableColumn> schemaTableColumnList) {
        final int[] count = {100};

        preconfigureColumnsAsHidden(tableEntityClass);

        iterate(iterable(schemaTableColumnList), updateColumn -> {
            updateColumn.setDefaultColumnOrder(count[0]++);
            SchemaTableColumn currentColumn = getSchemaTableColumnRepository().findBySchemaTableNameAndName(tableEntityClass.getSimpleName(), updateColumn.getName());

            if (currentColumn == null) {
                logger.error("Updating UI column display configs and current column is null but it should exist. Table Entity Class: [" + tableEntityClass.getSimpleName() + "], Update Column Name: [" + updateColumn.getName() + "]");
            }

            EntityPropertyUtils.copyNonNullProperties(updateColumn, currentColumn);
            getSchemaTableColumnRepository().save(currentColumn);
        });

    }

    private SchemaTableColumn update(String columnName, String displayName) {
        return update(columnName, displayName, null);
    }

    private SchemaTableColumn update(String columnName, String displayName, String uiFieldConfiguration) {
        return update(columnName, displayName, uiFieldConfiguration, null);
    }

    private SchemaTableColumn update(String columnName, String displayName, String uiFieldConfiguration, String uiColumnConfiguration) {
        return update(columnName, displayName, uiFieldConfiguration, uiColumnConfiguration, null);

    }

    private SchemaTableColumn update(String columnName, String displayName, String uiFieldConfiguration, String uiColumnConfiguration, String uiModelFieldConfiguration) {
        SchemaTableColumn updateColumn = new SchemaTableColumn();

        updateColumn.setName(columnName);
        updateColumn.setDisplayName(displayName);

        updateColumn.setUiFieldConfiguration(uiFieldConfiguration);
        updateColumn.setUiColumnConfiguration(uiColumnConfiguration);
        updateColumn.setUiModelFieldConfiguration(uiModelFieldConfiguration);

        if (uiFieldConfiguration != null && uiFieldConfiguration.contains("hidden:true")) {
            updateColumn.setDisplayHidden(true);
        } else {
            updateColumn.setDisplayHidden(false);
        }

        return updateColumn;
    }

    private void preconfigureColumnsAsHidden(Class tableEntityClass) {
        List<SchemaTableColumn> columnList = getSchemaTableColumnRepository().findBySchemaTableName(tableEntityClass.getSimpleName());

        /**
         * By default make all columns hidden so we only show the ones we want to show.
         */
        configureColumnAsHidden(columnList);

        getSchemaTableColumnRepository().saveAll(columnList);
    }

    private void configureColumnAsHidden(List<SchemaTableColumn> columnList) {
        final Integer[] count = {200};

        /**
         * Configure as a hidden column that is really far down the column order
         */
        iterate(columnList, column -> {
            column.setDisplayHidden(true);
            column.setDefaultColumnOrder(count[0]);
            column.setDisplayName(column.getName());
            count[0]++;
        });
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SchemaTableColumnRepository getSchemaTableColumnRepository() {
        return schemaTableColumnRepository;
    }

    public void setSchemaTableColumnRepository(SchemaTableColumnRepository schemaTableColumnRepository) {
        this.schemaTableColumnRepository = schemaTableColumnRepository;
    }

    public NamedEntityRepository<SchemaTable> getSchemaTableRepository() {
        return schemaTableRepository;
    }

    public void setSchemaTableRepository(NamedEntityRepository<SchemaTable> schemaTableRepository) {
        this.schemaTableRepository = schemaTableRepository;
    }
}