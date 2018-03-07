package com.job.tracker.task;

import com.system.db.repository.base.named.NamedEntityRepository;

import java.util.List;


/**
 * The <class>TaskRepository</class> defines the
 * database access repository for the associated entity
 *
 * @author Andrew
 */
public interface TaskRepository extends NamedEntityRepository<Task> {

    /**
     * Return all tasks for a given job id
     *
     * @param jobId
     * @return - list of tasks filtered by job id
     */
    public List<Task> findByJobId(Integer jobId);
}