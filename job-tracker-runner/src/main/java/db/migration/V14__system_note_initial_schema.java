package db.migration;

import com.job.tracker.note.SystemEntityNote;
import com.job.tracker.note.type.SystemEntityNoteType;
import com.system.db.entity.Entity;
import com.system.db.migration.table.TableCreationMigration;

import java.util.List;

import static com.system.util.collection.CollectionUtils.asList;

/**
 * The <class>V14__system_note_initial_schema</class> defines the initial schema for
 * note recording ability;
 *
 * @author Andrew
 */
public class V14__system_note_initial_schema extends TableCreationMigration {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////
    ////////                                                 Table Creation                                                  //////////
    //////////////////////////////////////////////////////////////////////

    protected List<Class<? extends Entity>> getEntityClasses() {
        return asList(
                SystemEntityNoteType.class, SystemEntityNote.class
        );
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                                  Data Insertion                                                   //////////
    //////////////////////////////////////////////////////////////////////

    @Override
    protected void insertData() {
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

}