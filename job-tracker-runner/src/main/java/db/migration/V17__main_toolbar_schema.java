package db.migration;


import com.system.db.migration.data.BaseDataMigration;
import com.system.db.repository.base.entity.SystemRepository;
import com.system.db.repository.base.named.NamedEntityRepository;
import com.system.ui.base.component.UiComponent;
import com.system.ui.base.component.config.UiComponentConfig;
import com.system.ui.base.component.config.attribute.UiComponentConfigAttribute;
import com.system.ui.base.component.definition.UiComponentDefinition;
import com.system.ui.base.component.type.UiComponentType;
import org.springframework.beans.factory.annotation.Autowired;

import static com.system.ui.util.UiCreationUtils.createUiCompontConfigAttributeByKeyValues;
import static com.system.util.collection.Pair.newPair;

/**
 * The <class>V17__main_toolbar_schema</class> defines the main toolbar schema
 * for the project management system.
 *
 * @author Andrew
 */
public class V17__main_toolbar_schema extends BaseDataMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    /////////////////////////////////////////////////////////////////////

    @Autowired
    private NamedEntityRepository<UiComponentDefinition> uiComponentDefinitionRepository;

    @Autowired
    private NamedEntityRepository<UiComponentType> uiComponentTypeRepository;

    @Autowired
    private NamedEntityRepository<UiComponent> uiComponentRepository;

    @Autowired
    private SystemRepository<UiComponentConfig, Integer> uiComponentConfigRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {
        getUiComponentDefinitionRepository().save(updateMainHeaderToolbarRequires());
        getUiComponentConfigRepository().save(addToolbarMenu());
    }

    private UiComponentDefinition updateMainHeaderToolbarRequires() {
        //Need to add 'System.view.MainToolbarDefinition' as a new requirement to the top toolbar in the system
        // This will allow us to hook into it and add our own toolbar menus from this project.

        UiComponentDefinition toolbarHeaderDefinition = getUiComponentDefinitionRepository().findByName("System.view.application.main.header.Header");
        toolbarHeaderDefinition.addRequire("System.view.MainToolbarDefinition");

        return toolbarHeaderDefinition;
    }

    private UiComponentConfig addToolbarMenu() {
        UiComponentConfig uiComponentConfig = getHeaderToolbarUiComponentConfig();
        configureHeaderToolbarItemsElementsConfig(uiComponentConfig);
        return uiComponentConfig;
    }

    private UiComponentConfig getHeaderToolbarUiComponentConfig() {
        //Get UiComponent from Database with name "Main Header Toolbar Items Component"
        UiComponent mainHeaderToolbar = getUiComponentRepository().findByName("Main Header Toolbar Items Component");
        //Get the uiComponentConfig from that UiComponent (The Header Toolbar Component)
        UiComponentConfig headerToolbarComponentConfig = mainHeaderToolbar.getUiComponentConfig();
        //return the new toolbar menu component config
        return headerToolbarComponentConfig;
    }

    private void configureHeaderToolbarItemsElementsConfig(UiComponentConfig uiComponentConfig) {
        //Here add the Project Management Menu item with Jobs as a clickable item under it
        uiComponentConfig.getUiComponentConfigAttributeList().add(createAppHeaderToolsAttribute());
    }

    private UiComponentConfigAttribute createAppHeaderToolsAttribute() {
        UiComponentConfigAttribute headerToolbarAttribute = createUiCompontConfigAttributeByKeyValues("Main Header Toolbar Definitions", "This is a holder for our main UI toolbar definitions.",
                null,
                newPair("xtype", "'main-toolbar-definition'"));

        return headerToolbarAttribute;
    }


    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public NamedEntityRepository<UiComponentDefinition> getUiComponentDefinitionRepository() {
        return uiComponentDefinitionRepository;
    }

    public void setUiComponentDefinitionRepository(NamedEntityRepository<UiComponentDefinition> uiComponentDefinitionRepository) {
        this.uiComponentDefinitionRepository = uiComponentDefinitionRepository;
    }

    public NamedEntityRepository<UiComponentType> getUiComponentTypeRepository() {
        return uiComponentTypeRepository;
    }

    public void setUiComponentTypeRepository(NamedEntityRepository<UiComponentType> uiComponentTypeRepository) {
        this.uiComponentTypeRepository = uiComponentTypeRepository;
    }

    public NamedEntityRepository<UiComponent> getUiComponentRepository() {
        return uiComponentRepository;
    }

    public void setUiComponentRepository(NamedEntityRepository<UiComponent> uiComponentRepository) {
        this.uiComponentRepository = uiComponentRepository;
    }

    public SystemRepository<UiComponentConfig, Integer> getUiComponentConfigRepository() {
        return uiComponentConfigRepository;
    }

    public void setUiComponentConfigRepository(SystemRepository<UiComponentConfig, Integer> uiComponentConfigRepository) {
        this.uiComponentConfigRepository = uiComponentConfigRepository;
    }
}