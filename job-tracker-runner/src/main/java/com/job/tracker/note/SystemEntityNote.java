package com.job.tracker.note;


import com.job.tracker.note.type.SystemEntityNoteType;
import com.system.db.entity.named.NamedEntity;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * The <class>SystemEntityNote</class> defines a
 * simple note that is associated with a specific entity in the system.
 * <p>
 * This makes it easy to add notes to various entities in the system.
 * <p>
 * A note is any string text acting as a record/description on an entity
 *
 * @author Andrew Popp
 */
public class SystemEntityNote extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    /**
     * The type of note and which entity it is associated with
     */
    @ManyToOne(optional = false)
    @JoinColumn(name = "system_entity_note_type_id")
    private SystemEntityNoteType systemEntityNoteType;

    /**
     * The ID of the entity that this note is associated with.
     */
    @Column(nullable = false)
    private Integer fkEntityId;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public SystemEntityNote() {
    }

    public static SystemEntityNote newInstance(String name, String description, SystemEntityNoteType systemEntityNoteType, Integer fkEntityId) {
        SystemEntityNote systemEntityNote = new SystemEntityNote();
        systemEntityNote.setDescription(description);
        systemEntityNote.setName(name);
        systemEntityNote.setSystemEntityNoteType(systemEntityNoteType);
        systemEntityNote.setFkEntityId(fkEntityId);
        return systemEntityNote;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemEntityNoteType getSystemEntityNoteType() {
        return systemEntityNoteType;
    }

    public void setSystemEntityNoteType(SystemEntityNoteType systemEntityNoteType) {
        this.systemEntityNoteType = systemEntityNoteType;
    }

    public Integer getFkEntityId() {
        return fkEntityId;
    }

    public void setFkEntityId(Integer fkEntityId) {
        this.fkEntityId = fkEntityId;
    }
}
