package db.migration;

import com.job.tracker.Employee.Employee;
import com.job.tracker.branch.Branch;
import com.job.tracker.department.Department;
import com.job.tracker.job.Job;
import com.job.tracker.project.Project;
import com.job.tracker.task.Task;
import com.job.tracker.task.hour.TaskHour;
import com.job.tracker.task.revision.TaskRevision;
import com.job.tracker.task.status.TaskStatus;
import com.job.tracker.task.type.TaskType;
import com.system.db.entity.Entity;
import com.system.db.migration.table.TableCreationMigration;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.db.repository.event.types.SystemRepositoryEventTypes;
import com.system.security.privilege.SystemSecurityPrivilege;
import com.system.security.role.SystemSecurityRole;
import com.system.security.role.SystemSecurityRoles;
import com.system.security.user.SystemSecurityUser;
import com.system.security.user.SystemSecurityUserRepository;
import com.system.ws.entity.upload.util.LoadExternalDataUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.support.Repositories;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static com.system.util.collection.CollectionUtils.*;

/**
 * The <class>V17__task_initial_schema</class> defines the initial task schema for
 * job management.
 *
 * @author Andrew
 */
public class V17__task_initial_schema extends TableCreationMigration {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    ///////////////////////////////////////////////////////////////////////
    ////////                                                         Data                                                               //////////
    //////////////////////////////////////////////////////////////////////

    @Value(value = "classpath:company/employee/employees.tsv")
    private Resource employeesData;

    @Value(value = "classpath:company/employee/employeeSecurityUser.tsv")
    private Resource employeesSecurityUserData;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private Repositories repositories;

    @Autowired
    private NamedEntityRepository<SystemSecurityPrivilege> systemSecurityPrivilegeRepository;

    @Autowired
    private NamedEntityRepository<SystemSecurityRole> systemSecurityRoleRepository;

    @Autowired
    private SystemSecurityUserRepository systemSecurityUserRepository;

    @Autowired
    private NamedEntityRepository<Branch> branchRepository;

    @Autowired
    private NamedEntityRepository<Employee> employeeRepository;

    @Autowired
    private NamedEntityRepository<TaskStatus> taskStatusRepository;

    @Autowired
    private NamedEntityRepository<Department> departmentRepository;

    @Autowired
    private NamedEntityRepository<TaskType> taskTypeRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Table Creation                                                  //////////
    //////////////////////////////////////////////////////////////////////

    protected List<Class<? extends Entity>> getEntityClasses() {
        return asList(
                Branch.class, Employee.class, Project.class, TaskStatus.class, TaskType.class, Task.class, Job.class, TaskRevision.class, TaskHour.class
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {
        getBranchRepository().saveAll(getBranchData());
        loadEmployeeData();

        getTaskStatusRepository().saveAll(getTaskStatusData());
        getTaskTypeRepository().saveAll(getTaskTypeData());
    }

    public List<Branch> getBranchData() {
        return asList(
                Branch.newBasicInstance("Minneapolis", "MN", "\\\\YOURFILESHARE\\Projects\\"),
                Branch.newBasicInstance("Charlotte", "CH", "\\\\YOURFILESHARE\\Projects\\"),
                Branch.newBasicInstance("Fife", "FI", "\\\\YOURFILESHARE\\Projects\\"),
                Branch.newBasicInstance("Fremont", "FR", "\\\\YOURFILESHARE\\Projects\\"),
                Branch.newBasicInstance("Portland", "PO", "\\\\YOURFILESHARE\\Projects\\"),
                Branch.newBasicInstance("Chicago", "CI", "\\\\YOURFILESHARE\\Projects\\"),
                Branch.newBasicInstance("Las Vegas", "LV", "\\\\YOURFILESHARE\\Projects\\"),
                Branch.newBasicInstance("New York", "NY", "\\\\YOURFILESHARE\\Projects\\"),
                Branch.newBasicInstance("Phoenix", "PH", "\\\\YOURFILESHARE\\Projects\\"),
                Branch.newBasicInstance("Puerto Rico", "PR", "\\\\YOURFILESHARE\\Projects\\"),
                Branch.newBasicInstance("Sacramento", "SA", "\\\\YOURFILESHARE\\Projects\\"),
                Branch.newBasicInstance("Texas", "TX", "\\\\YOURFILESHARE\\Projects\\"),
                Branch.newBasicInstance("San Diego", "SD", "\\\\YOURFILESHARE\\Projects\\")
        );
    }

    private void loadEmployeeData() {
        try {
            LoadExternalDataUtils.loadExternalData(employeesSecurityUserData.getInputStream(), getSystemSecurityUserRepository(), SystemSecurityUser.class, SystemRepositoryEventTypes.INSERT, "text/tsv", null, repositories, publisher);
            LoadExternalDataUtils.loadExternalData(employeesData.getInputStream(), getEmployeeRepository(), Employee.class, SystemRepositoryEventTypes.INSERT, "text/tsv", null, repositories, publisher);
            updateAllUserSecurityRoles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<TaskStatus> getTaskStatusData() {
        return asList(
                TaskStatus.newInstance("New", "A job is marked as 'New' when it is first added."),
                TaskStatus.newInstance("Active", "A job is marked as 'Active' when it is currently being worked on."),
                TaskStatus.newInstance("Pending", "A job is marked as 'Pending' when we are awaiting completion."),
                TaskStatus.newInstance("On Hold", "A job is marked as 'On Hold' when the job is unable to be completed pending further information."),
                TaskStatus.newInstance("Canceled", "A job is marked as 'Canceled' when the job is unable to be completed and terminated."),
                TaskStatus.newInstance("Complete", "A job is marked as 'Complete' when the job has been completed")
        );
    }

    public List<TaskType> getTaskTypeData() {
        Department engineeringDepartment = departmentRepository.findByName("Engineering");
        return asList(
                TaskType.newInstance("Detailing", "4-RECEIVED", "This type of task defines a task related to detailing work", null, engineeringDepartment),
                TaskType.newInstance("Engineering", "5-CUSTOM ENGINEERING", "This type of task defines a task related to engineering related work.", null, engineeringDepartment)
        );
    }

    private void updateAllUserSecurityRoles() {
        List<SystemSecurityUser> allUserList = getSystemSecurityUserRepository().findAll();
        iterate(iterable(allUserList), user -> user.setRoles(getSecurityRoleFromMemberOf(user.getMemberOf())));
        getSystemSecurityUserRepository().saveAll(allUserList);
    }

    private Collection<SystemSecurityRole> getSecurityRoleFromMemberOf(String memberOf) {
        // SystemSecurityRole adminRole = getSystemSecurityRoleRepository().findByName(SystemSecurityRoles.ROLE_ADMIN.toString());
        SystemSecurityRole userRole = getSystemSecurityRoleRepository().findByName(SystemSecurityRoles.ROLE_USER.toString());
        Collection<SystemSecurityRole> roles = Arrays.asList(userRole);
        return roles;
    }

///////////////////////////////////////////////////////////////////////
////////                                             Basic   Getter/Setters                                          //////////
//////////////////////////////////////////////////////////////////////

    public Resource getEmployeesSecurityUserData() {
        return employeesSecurityUserData;
    }

    public void setEmployeesSecurityUserData(Resource employeesSecurityUserData) {
        this.employeesSecurityUserData = employeesSecurityUserData;
    }

    public ApplicationEventPublisher getPublisher() {
        return publisher;
    }

    public void setPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public Repositories getRepositories() {
        return repositories;
    }

    public void setRepositories(Repositories repositories) {
        this.repositories = repositories;
    }

    public NamedEntityRepository<SystemSecurityPrivilege> getSystemSecurityPrivilegeRepository() {
        return systemSecurityPrivilegeRepository;
    }

    public void setSystemSecurityPrivilegeRepository(NamedEntityRepository<SystemSecurityPrivilege> systemSecurityPrivilegeRepository) {
        this.systemSecurityPrivilegeRepository = systemSecurityPrivilegeRepository;
    }

    public NamedEntityRepository<SystemSecurityRole> getSystemSecurityRoleRepository() {
        return systemSecurityRoleRepository;
    }

    public void setSystemSecurityRoleRepository(NamedEntityRepository<SystemSecurityRole> systemSecurityRoleRepository) {
        this.systemSecurityRoleRepository = systemSecurityRoleRepository;
    }

    public SystemSecurityUserRepository getSystemSecurityUserRepository() {
        return systemSecurityUserRepository;
    }

    public void setSystemSecurityUserRepository(SystemSecurityUserRepository systemSecurityUserRepository) {
        this.systemSecurityUserRepository = systemSecurityUserRepository;
    }

    public NamedEntityRepository<Branch> getBranchRepository() {
        return branchRepository;
    }

    public void setBranchRepository(NamedEntityRepository<Branch> branchRepository) {
        this.branchRepository = branchRepository;
    }

    public NamedEntityRepository<Employee> getEmployeeRepository() {
        return employeeRepository;
    }

    public void setEmployeeRepository(NamedEntityRepository<Employee> employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public NamedEntityRepository<TaskStatus> getTaskStatusRepository() {
        return taskStatusRepository;
    }

    public void setTaskStatusRepository(NamedEntityRepository<TaskStatus> taskStatusRepository) {
        this.taskStatusRepository = taskStatusRepository;
    }

    public NamedEntityRepository<Department> getDepartmentRepository() {
        return departmentRepository;
    }

    public void setDepartmentRepository(NamedEntityRepository<Department> departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public NamedEntityRepository<TaskType> getTaskTypeRepository() {
        return taskTypeRepository;
    }

    public void setTaskTypeRepository(NamedEntityRepository<TaskType> taskTypeRepository) {
        this.taskTypeRepository = taskTypeRepository;
    }
}