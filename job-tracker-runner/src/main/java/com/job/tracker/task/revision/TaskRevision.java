package com.job.tracker.task.revision;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.job.tracker.task.Task;
import com.system.db.entity.named.NamedEntity;
import com.system.util.string.StringUtils;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

/**
 * The <class>TaskRevision</class> defines a
 * a slight customer driven modification to a task.
 * <p>
 * This revision essentially means a minor direction change in the
 * currently defined task.
 * <p>
 * This will mean creating a new folder structure under this task
 * with the new revision folders.
 * <p>
 * This means any new documents can be added to this revision
 * by including them into these new folders.
 *
 * @author Andrew Popp
 */
public class TaskRevision extends NamedEntity<Integer> {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @ManyToOne
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    /**
     * Revision number for a specific task
     */
    private String revisionNumber;

    @Column(nullable = false)
    @JsonFormat(pattern = "MM-dd-yyyy")
    private LocalDate date;

    /**
     * The absolute path of the folder structure created for
     * all files used by this entity within the filesystem
     */
    private String folderLocation;

    ///////////////////////////////////////////////////////////////////////
    ////////                                              Default Constructor                                           //////////
    //////////////////////////////////////////////////////////////////////

    public TaskRevision() {
    }

    public static TaskRevision newInstance(Task task) {
        TaskRevision taskRevision = new TaskRevision();
        taskRevision.setTask(task);
        taskRevision.setDate(task.getDate());
        return taskRevision;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getRevisionNumber() {
        return revisionNumber;
    }

    public void setRevisionNumber(String revisionNumber) {
        this.revisionNumber = revisionNumber;
        if (StringUtils.isEmpty(getName()) && !StringUtils.isEmpty(revisionNumber)) {
            setName("Rev " + revisionNumber);
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getFolderLocation() {
        return folderLocation;
    }

    public void setFolderLocation(String folderLocation) {
        this.folderLocation = folderLocation;
    }
}
