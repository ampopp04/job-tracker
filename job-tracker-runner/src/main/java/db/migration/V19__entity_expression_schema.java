package db.migration;


import com.job.tracker.system.entity.expression.operation.EntityExpressionOperation;
import com.job.tracker.system.entity.expression.operation.type.EntityExpressionOperationType;
import com.job.tracker.system.entity.expression.operation.type.EntityExpressionOperationTypeEvents;
import com.system.bean.SystemBean;
import com.system.bean.definition.SystemBeanDefinition;
import com.system.bean.definition.type.SystemBeanDefinitionType;
import com.system.bean.type.SystemBeanType;
import com.system.db.migration.data.BaseDataMigration;
import com.system.db.repository.base.named.NamedEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static com.job.tracker.system.entity.expression.operation.type.EntityExpressionOperationTypeEvents.AFTER_CREATE;
import static com.job.tracker.system.entity.expression.operation.type.EntityExpressionOperationTypeEvents.CREATE;
import static com.system.util.collection.CollectionUtils.asList;

/**
 * The <class>V19__entity_expression_schema</class> is a migration
 * that configures all of the basic entities in the system to entity expression support.
 * <p>
 * This includes setting up all the beans, types, and expression paths settings to get the
 * user started.
 *
 * @author Andrew
 */
public class V19__entity_expression_schema extends BaseDataMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    @Autowired
    private NamedEntityRepository<SystemBeanDefinitionType> systemBeanDefinitionTypeRepository;

    @Autowired
    private NamedEntityRepository<SystemBeanDefinition> systemBeanDefinitionRepository;

    @Autowired
    private NamedEntityRepository<SystemBeanType> systemBeanTypeRepository;

    @Autowired
    private NamedEntityRepository<SystemBean> systemBeanRepository;

    @Autowired
    private NamedEntityRepository<EntityExpressionOperationType> entityExpressionOperationTypeRepository;

    @Autowired
    private NamedEntityRepository<EntityExpressionOperation> entityExpressionOperationRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                      Constants                                                       //////////
    //////////////////////////////////////////////////////////////////////

    public static final String FOLDER_CREATOR_BEAN_NAME = "Folder Path Operation Executor";
    public static final String OBJECT_CREATOR_BEAN_NAME = "Create Object Operation Executor";
    public static final String FIELD_OPERATION_EXECUTOR_BEAN_NAME = "Field Operation Executor";

    public static final String CREATE_FOLDER_OPERATION_NAME = "Create Folder Operation";
    public static final String CREATE_OBJECT_OPERATION_NAME = "Create Object Operation";

    public static final String AFTER_CREATE_FIELD_SETTER_OPERATION_NAME = "After Create Field Setter Operation";
    public static final String ON_CREATE_FIELD_SETTER_OPERATION_NAME = "On Create Field Setter Operation";

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {
        SystemBean folderCreatorBean = createAndSaveSystemBean(createAndSaveSystemBeanType(createAndSaveSystemBeanDefinition()));
        SystemBean objectCreatorBean = createAndSaveCreateObjectSystemBean(createAndSaveCreateObjectSystemBeanType(folderCreatorBean.getSystemBeanType().getSystemBeanDefinition()));
        SystemBean fieldOperationExecutorBean = createAndSaveFieldOperationSystemBean(createAndSaveFieldOperationSystemBeanType(folderCreatorBean.getSystemBeanType().getSystemBeanDefinition()));

        //Create EntityExpressionOperationTypes - CREATE, DELETE, UPDATE
        createEntityExpressionOperationTypes();

        //Create EntityExpressionOperation. Only CREATE operation for now
        createAndSaveEntityExpressionCreateOperation(folderCreatorBean);
        createAndSaveEntityExpressionAfterCreateObjectOperation(objectCreatorBean);

        createAndSaveEntityExpressionFieldSetAfterCreateOperation(fieldOperationExecutorBean);
        createAndSaveEntityExpressionFieldSetOnCreateOperation(fieldOperationExecutorBean);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                               Folder Creator Bean                                          //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Create new SystemBeanDefinition Interface Operation Executor
     */
    private SystemBeanDefinition createAndSaveSystemBeanDefinition() {
        return getSystemBeanDefinitionRepository().save(
                SystemBeanDefinition.newInstance(
                        "Entity Expression Operation Executor",
                        "An interface for executing entity expression operations.",
                        getSystemBeanDefinitionTypeRepository().findByName("Interface"),
                        "com.job.tracker.system.entity.expression.operation.executor.EntityExpressionOperationExecutor"
                )
        );
    }

    /**
     * Create SystemBeanType
     */
    private SystemBeanType createAndSaveSystemBeanType(SystemBeanDefinition systemBeanDefinition) {
        return getSystemBeanTypeRepository().save(
                SystemBeanType.newInstance(
                        "Folder Path Operation Executor",
                        "Concrete class implementation that executes the operations for folder path management.",
                        systemBeanDefinition,
                        "com.job.tracker.system.entity.expression.operation.executor.folder.EntityExpressionFolderOperationExecutor"
                )
        );
    }

    /**
     * Create SystemBean Operation Executor
     */
    private SystemBean createAndSaveSystemBean(SystemBeanType systemBeanType) {
        return getSystemBeanRepository().save(
                SystemBean.newInstance(
                        FOLDER_CREATOR_BEAN_NAME,
                        "Concrete class implementation that executes the actual operation for folder path assignments.",
                        systemBeanType
                )
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Object Creator Bean                                          //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Create Object Creator SystemBeanType
     */
    private SystemBeanType createAndSaveCreateObjectSystemBeanType(SystemBeanDefinition systemBeanDefinition) {
        return getSystemBeanTypeRepository().save(
                SystemBeanType.newInstance(
                        OBJECT_CREATOR_BEAN_NAME,
                        "Concrete class implementation that executes the operations for expression based object creation.",
                        systemBeanDefinition,
                        "com.job.tracker.system.entity.expression.operation.executor.object.EntityExpressionObjectOperationExecutor"
                )
        );
    }

    /**
     * Create Object SystemBean Operation Executor
     */
    private SystemBean createAndSaveCreateObjectSystemBean(SystemBeanType systemBeanType) {
        return getSystemBeanRepository().save(
                SystemBean.newInstance(
                        OBJECT_CREATOR_BEAN_NAME,
                        "Concrete class implementation that executes the actual operation for creating objects.",
                        systemBeanType
                )
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                Field Operation Executor Bean                                   //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Create SystemBeanType Field Operation
     */
    private SystemBeanType createAndSaveFieldOperationSystemBeanType(SystemBeanDefinition systemBeanDefinition) {
        return getSystemBeanTypeRepository().save(
                SystemBeanType.newInstance(
                        "Field Operation Executor",
                        "Concrete class implementation that dynamically sets entity field values based on an expression evaluation.",
                        systemBeanDefinition,
                        "com.job.tracker.system.entity.expression.operation.executor.field.EntityExpressionFieldOperationExecutor"
                )
        );
    }

    /**
     * Create SystemBean Operation Executor
     */
    private SystemBean createAndSaveFieldOperationSystemBean(SystemBeanType systemBeanType) {
        return getSystemBeanRepository().save(
                SystemBean.newInstance(
                        FIELD_OPERATION_EXECUTOR_BEAN_NAME,
                        "Concrete class implementation that executes the actual operation for setting field values dynamically based on evaluated expressions.",
                        systemBeanType
                )
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                Entity Expression Operation Types                          //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * Create EntityExpressionOperationTypes
     * CREATE, DELETE, UPDATE
     */
    public void createEntityExpressionOperationTypes() {
        getEntityExpressionOperationTypeRepository().saveAll(asList(
                EntityExpressionOperationType.newInstance(EntityExpressionOperationTypeEvents.CREATE.name(), "Denotes the operation type that occurs during an Entity Create."),
                EntityExpressionOperationType.newInstance(EntityExpressionOperationTypeEvents.DELETE.name(), "Denotes the operation type that occurs during an Entity Delete."),
                EntityExpressionOperationType.newInstance(EntityExpressionOperationTypeEvents.UPDATE.name(), "Denotes the operation type that can execute during a Entity UPDATE."),
                EntityExpressionOperationType.newInstance(AFTER_CREATE.name(), "Denotes the operation type that can execute right after an entity is created.")
        ));
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                            Create Folder Operation                                   //////////
    //////////////////////////////////////////////////////////////////////

    public EntityExpressionOperation createAndSaveEntityExpressionCreateOperation(SystemBean systemBean) {
        return getEntityExpressionOperationRepository().save(
                EntityExpressionOperation.newInstance(
                        CREATE_FOLDER_OPERATION_NAME,
                        "Defines the operation that will create new folders.",
                        getEntityExpressionOperationTypeRepository().findByName(AFTER_CREATE.name()),
                        systemBean
                )
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                            Create Object Operation                                   //////////
    //////////////////////////////////////////////////////////////////////

    public EntityExpressionOperation createAndSaveEntityExpressionAfterCreateObjectOperation(SystemBean systemBean) {
        return getEntityExpressionOperationRepository().save(
                EntityExpressionOperation.newInstance(
                        CREATE_OBJECT_OPERATION_NAME,
                        "Defines the operation that will create new objects.",
                        getEntityExpressionOperationTypeRepository().findByName(AFTER_CREATE.name()),
                        systemBean
                )
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                            Field Setter Operations                                   //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * After Create Field Setter Operation
     */
    public EntityExpressionOperation createAndSaveEntityExpressionFieldSetAfterCreateOperation(SystemBean systemBean) {
        return getEntityExpressionOperationRepository().save(
                EntityExpressionOperation.newInstance(
                        AFTER_CREATE_FIELD_SETTER_OPERATION_NAME,
                        "Defines the operation that will set a fields value dynamically based on an expression after entity creation.",
                        getEntityExpressionOperationTypeRepository().findByName(AFTER_CREATE.name()),
                        systemBean
                )
        );
    }

    /**
     * On Create Field Setter Operation
     */
    public EntityExpressionOperation createAndSaveEntityExpressionFieldSetOnCreateOperation(SystemBean systemBean) {
        return getEntityExpressionOperationRepository().save(
                EntityExpressionOperation.newInstance(
                        ON_CREATE_FIELD_SETTER_OPERATION_NAME,
                        "Defines the operation that will set a fields value dynamically based on an expression during entity creation.",
                        getEntityExpressionOperationTypeRepository().findByName(CREATE.name()),
                        systemBean
                )
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public NamedEntityRepository<SystemBeanDefinitionType> getSystemBeanDefinitionTypeRepository() {
        return systemBeanDefinitionTypeRepository;
    }

    public void setSystemBeanDefinitionTypeRepository(NamedEntityRepository<SystemBeanDefinitionType> systemBeanDefinitionTypeRepository) {
        this.systemBeanDefinitionTypeRepository = systemBeanDefinitionTypeRepository;
    }

    public NamedEntityRepository<SystemBeanDefinition> getSystemBeanDefinitionRepository() {
        return systemBeanDefinitionRepository;
    }

    public void setSystemBeanDefinitionRepository(NamedEntityRepository<SystemBeanDefinition> systemBeanDefinitionRepository) {
        this.systemBeanDefinitionRepository = systemBeanDefinitionRepository;
    }

    public NamedEntityRepository<SystemBeanType> getSystemBeanTypeRepository() {
        return systemBeanTypeRepository;
    }

    public void setSystemBeanTypeRepository(NamedEntityRepository<SystemBeanType> systemBeanTypeRepository) {
        this.systemBeanTypeRepository = systemBeanTypeRepository;
    }

    public NamedEntityRepository<SystemBean> getSystemBeanRepository() {
        return systemBeanRepository;
    }

    public void setSystemBeanRepository(NamedEntityRepository<SystemBean> systemBeanRepository) {
        this.systemBeanRepository = systemBeanRepository;
    }

    public NamedEntityRepository<EntityExpressionOperationType> getEntityExpressionOperationTypeRepository() {
        return entityExpressionOperationTypeRepository;
    }

    public void setEntityExpressionOperationTypeRepository(NamedEntityRepository<EntityExpressionOperationType> entityExpressionOperationTypeRepository) {
        this.entityExpressionOperationTypeRepository = entityExpressionOperationTypeRepository;
    }

    public NamedEntityRepository<EntityExpressionOperation> getEntityExpressionOperationRepository() {
        return entityExpressionOperationRepository;
    }

    public void setEntityExpressionOperationRepository(NamedEntityRepository<EntityExpressionOperation> entityExpressionOperationRepository) {
        this.entityExpressionOperationRepository = entityExpressionOperationRepository;
    }

}