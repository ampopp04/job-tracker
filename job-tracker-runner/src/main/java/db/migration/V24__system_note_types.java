package db.migration;

import com.job.tracker.Employee.Employee;
import com.job.tracker.branch.Branch;
import com.job.tracker.customer.Customer;
import com.job.tracker.job.Job;
import com.job.tracker.note.type.SystemEntityNoteType;
import com.job.tracker.project.Project;
import com.job.tracker.task.Task;
import com.job.tracker.task.hour.TaskHour;
import com.job.tracker.task.revision.TaskRevision;
import com.system.db.entity.Entity;
import com.system.db.migration.table.TableCreationMigration;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.db.schema.table.column.SchemaTableColumn;
import com.system.db.schema.table.column.SchemaTableColumnRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.system.util.collection.CollectionUtils.asList;

/**
 * The <class>V24__system_note_types</class> defines the initial schema for
 * note types to be used throughout this system.
 *
 * @author Andrew
 */
public class V24__system_note_types extends TableCreationMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private NamedEntityRepository<SystemEntityNoteType> systemEntityNoteTypeRepository;

    @Autowired
    private SchemaTableColumnRepository schemaTableColumnRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Table Creation                                                  //////////
    //////////////////////////////////////////////////////////////////////

    protected List<Class<? extends Entity>> getEntityClasses() {
        return asList();
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {
        getSystemEntityNoteTypeRepository().saveAll(getSystemEntityNoteTypeData());
    }

    public List<SystemEntityNoteType> getSystemEntityNoteTypeData() {
        return asList(
                SystemEntityNoteType.newInstance("Branch Note", "A note that is associated to a specific branch entity.", getIdColumnForTableName(Branch.class)),
                SystemEntityNoteType.newInstance("Employee Note", "A note that is associated to a specific employee entity.", getIdColumnForTableName(Employee.class)),
                SystemEntityNoteType.newInstance("Customer Note", "A note that is associated to a specific customer entity.", getIdColumnForTableName(Customer.class)),
                SystemEntityNoteType.newInstance("Project Note", "A note that is associated to a specific project entity.", getIdColumnForTableName(Project.class)),
                SystemEntityNoteType.newInstance("Job Note", "A note that is associated to a specific job entity.", getIdColumnForTableName(Job.class)),
                SystemEntityNoteType.newInstance("Task Note", "A note that is associated to a specific task entity.", getIdColumnForTableName(Task.class)),
                SystemEntityNoteType.newInstance("Task Revision", "A note that is associated to a specific task entity.", getIdColumnForTableName(TaskRevision.class)),
                SystemEntityNoteType.newInstance("Task Hour Note", "A note that is associated to a specific task hour entity.", getIdColumnForTableName(TaskHour.class))
        );
    }

    private SchemaTableColumn getIdColumnForTableName(Class entityClass) {
        return getSchemaTableColumnRepository().findBySchemaTableNameAndName(entityClass.getSimpleName(), "id");
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public NamedEntityRepository<SystemEntityNoteType> getSystemEntityNoteTypeRepository() {
        return systemEntityNoteTypeRepository;
    }

    public void setSystemEntityNoteTypeRepository(NamedEntityRepository<SystemEntityNoteType> systemEntityNoteTypeRepository) {
        this.systemEntityNoteTypeRepository = systemEntityNoteTypeRepository;
    }

    public SchemaTableColumnRepository getSchemaTableColumnRepository() {
        return schemaTableColumnRepository;
    }

    public void setSchemaTableColumnRepository(SchemaTableColumnRepository schemaTableColumnRepository) {
        this.schemaTableColumnRepository = schemaTableColumnRepository;
    }
}