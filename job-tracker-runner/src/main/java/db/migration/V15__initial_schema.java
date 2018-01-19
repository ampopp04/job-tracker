package db.migration;

import com.job.tracker.customer.Customer;
import com.job.tracker.department.Department;
import com.job.tracker.project.type.ProjectType;
import com.job.tracker.system.entity.expression.EntityExpression;
import com.job.tracker.system.entity.expression.assignment.EntityExpressionAssignment;
import com.job.tracker.system.entity.expression.operation.EntityExpressionOperation;
import com.job.tracker.system.entity.expression.operation.type.EntityExpressionOperationType;
import com.job.tracker.system.entity.expression.type.EntityExpressionType;
import com.system.db.entity.Entity;
import com.system.db.entity.base.BaseEntity;
import com.system.db.migration.table.TableCreationMigration;
import com.system.db.repository.base.entity.SystemRepository;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.db.repository.event.types.SystemRepositoryEventTypes;
import com.system.util.compare.CompareUtils;
import com.system.ws.entity.upload.util.LoadExternalDataUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.support.Repositories;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static com.job.tracker.system.entity.expression.type.EntityExpressionType.*;
import static com.system.util.collection.CollectionUtils.asList;

/**
 * The <class>V15__initial_schema</class> defines the initial schema for
 * a project management system.
 *
 * @author Andrew
 */
public class V15__initial_schema extends TableCreationMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value(value = "classpath:company/customer/customers.tsv")
    private Resource customersData;

    @Value(value = "classpath:company/department/departments.tsv")
    private Resource departmentsData;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private Repositories repositories;

    @Autowired
    private NamedEntityRepository<EntityExpressionType> entityExpressionTypeRepository;
    @Autowired
    private NamedEntityRepository<ProjectType> projectTypeRepository;
    @Autowired
    private NamedEntityRepository<Customer> customerRepository;
    @Autowired
    private NamedEntityRepository<Department> departmentRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Table Creation                                                  //////////
    //////////////////////////////////////////////////////////////////////

    protected List<Class<? extends Entity>> getEntityClasses() {
        return asList(
                EntityExpressionOperationType.class, EntityExpressionOperation.class, EntityExpressionType.class, EntityExpression.class, EntityExpressionAssignment.class,
                Department.class, Customer.class, ProjectType.class
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {
        getEntityExpressionTypeRepository().saveAll(getEntityExpressionTypeData());
        getProjectTypeRepository().saveAll(getProjectTypeData());

        loadDepartmentData();
        loadCustomerData();
    }

    public List<EntityExpressionType> getEntityExpressionTypeData() {
        return asList(
                EntityExpressionType.newInstance(NUMBER_EXPRESSION_TYPE_NAME, "An expression used to generate a unique identifier number for a specific entity."),
                EntityExpressionType.newInstance(FOLDER_EXPRESSION_TYPE_NAME, "An expression used to generate an underlying folder path for a given entity."),
                EntityExpressionType.newInstance(OBJECT_EXPRESSION_TYPE_NAME, "An expression used to generate an object for a given entity.")
        );
    }

    public List<ProjectType> getProjectTypeData() {
        return asList(
                ProjectType.newInstance("Airport", ""),
                ProjectType.newInstance("Biopharm / Pharmaceutical", ""),
                ProjectType.newInstance("Hospital", ""),
                ProjectType.newInstance("Data Center", ""),
                ProjectType.newInstance("Housing", ""),
                ProjectType.newInstance("Transportation", ""),
                ProjectType.newInstance("Manufacturing/Heavy Industry", "")
        );
    }

    private void loadDepartmentData() {
        try {
            LoadExternalDataUtils.loadExternalData(departmentsData.getInputStream(), getDepartmentRepository(), Department.class, SystemRepositoryEventTypes.INSERT, "text/tsv", null, repositories, publisher);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCustomerData() {
        loadExternalData(customersData, getCustomerRepository(), Customer.class);
    }

    private <T extends BaseEntity> void loadExternalData(Resource externalData, SystemRepository<T, Integer> entityRepository, Class<T> entityClass) {
        try {

            BufferedReader reader = new BufferedReader(new InputStreamReader(externalData.getInputStream()));
            String line;

            //Get rid of header
            reader.readLine();

            while ((line = reader.readLine()) != null) {

                try {

                    T newEntity = parseEntity(entityClass, line);

                    if (newEntity != null)
                        entityRepository.save(newEntity);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private <T extends BaseEntity> T parseEntity(Class<T> entityClass, String customerCsvLine) {

        if (CompareUtils.equals(entityClass, Customer.class)) {
            return entityClass.cast(parseCustomerFromString(customerCsvLine));
        }

        return null;
    }


    private Customer parseCustomerFromString(String customerCsvLine) {
        //Customer No,Name,Status,AddressLine1,AddressLine2,AddressLine3,City,State,Zip Code,Phone Number
        //01-FCONCO , F CONNOLLY CO.,Active,1224 MONTGOMERY AVE,,,San Bruno,CA,94066,(650) 952-0542

        Customer customer = new Customer();
        String[] customerArray = null;

        try {
            customerArray = StringUtils.splitPreserveAllTokens(customerCsvLine, "\t");


            if (customerArray != null) {

                if (customerArray.length > 0)
                    customer.setCustomerNumber(getEntityPropertyValue(customerArray[0]));
                if (customerArray.length > 1)
                    customer.setName(getEntityPropertyValue(customerArray[1]));
                if (customerArray.length > 2)
                    customer.setStatus(getEntityPropertyValue(customerArray[2]));
                if (customerArray.length > 3)
                    customer.setAddressLine1(getEntityPropertyValue(customerArray[3]));
                if (customerArray.length > 4)
                    customer.setAddressLine2(getEntityPropertyValue(customerArray[4]));
                if (customerArray.length > 5)
                    customer.setAddressLine3(getEntityPropertyValue(customerArray[5]));
                if (customerArray.length > 6)
                    customer.setCity(getEntityPropertyValue(customerArray[6]));
                if (customerArray.length > 7)
                    customer.setState(getEntityPropertyValue(customerArray[7]));
                if (customerArray.length > 8)
                    customer.setZipCode(getEntityPropertyValue(customerArray[8]));
                if (customerArray.length > 9)
                    customer.setPhoneNumber(getEntityPropertyValue(customerArray[9]));

                return customer;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(customerCsvLine);
        }

        return null;
    }

    private String getEntityPropertyValue(String value) {
        return StringUtils.isBlank(value) ? null : value.trim();
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

    public NamedEntityRepository<ProjectType> getProjectTypeRepository() {
        return projectTypeRepository;
    }

    public void setProjectTypeRepository(NamedEntityRepository<ProjectType> projectTypeRepository) {
        this.projectTypeRepository = projectTypeRepository;
    }

    public NamedEntityRepository<Customer> getCustomerRepository() {
        return customerRepository;
    }

    public void setCustomerRepository(NamedEntityRepository<Customer> customerRepository) {
        this.customerRepository = customerRepository;
    }

    public NamedEntityRepository<Department> getDepartmentRepository() {
        return departmentRepository;
    }

    public void setDepartmentRepository(NamedEntityRepository<Department> departmentRepository) {
        this.departmentRepository = departmentRepository;
    }
}