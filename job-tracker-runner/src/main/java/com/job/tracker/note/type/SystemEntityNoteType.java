package com.job.tracker.note.type;


import com.system.db.entity.named.NamedEntity;
import com.system.db.schema.table.column.SchemaTableColumn;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SystemEntityNoteType</class> defines the
 * association of a note to a specific entity within the system.
 * <p>
 * This way you can freely and easily create notes for a specific entity while the
 * type itself defines the complexities of how and what entity that note
 * is associated with.
 *
 * @author Andrew Popp
 */
public class SystemEntityNoteType extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * The SchemaTableColumn defines a SchemaTable which tells us
     * which type of entity this note is for.
     * <p>
     * The SchemaTableColumn itself specifies which field/column this
     * note is associated with specifically on the entity.
     * <p>
     * This will usually be the ID of the table since we are associating notes
     * to the entity itself but it is possible to want to associate a note to a specific
     * field (SchemaTableColumn) for a given entity.
     * <p>
     * Instead of making a general note on the entity about the name of a customer
     * the note can be specific to the actual name field on that entity.
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "schema_table_column_id")
    private SchemaTableColumn schemaTableColumn;

    /*
    a condition that specifies who can create/update/delete these types of notes
    	private SystemCondition entityModifyCondition;
     */

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemEntityNoteType() {
    }

    public static SystemEntityNoteType newInstance(String name, String description, SchemaTableColumn schemaTableColumn) {
        SystemEntityNoteType systemEntityNoteType = new SystemEntityNoteType();
        systemEntityNoteType.setDescription(description);
        systemEntityNoteType.setName(name);
        systemEntityNoteType.setSchemaTableColumn(schemaTableColumn);
        return systemEntityNoteType;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SchemaTableColumn getSchemaTableColumn() {
        return schemaTableColumn;
    }

    public void setSchemaTableColumn(SchemaTableColumn schemaTableColumn) {
        this.schemaTableColumn = schemaTableColumn;
    }
}
