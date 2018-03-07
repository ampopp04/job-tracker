package com.job.tracker.job.tree;

import com.job.tracker.job.Job;
import com.job.tracker.task.Task;
import com.job.tracker.task.TaskRepository;
import com.system.db.repository.base.entity.SystemRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static com.system.util.collection.CollectionUtils.iterable;
import static com.system.util.collection.CollectionUtils.iterate;

/**
 * The <class>JobTreeController</class> defines a web UI
 * exposed WS controller used
 * for serving virtual DTOs encapsulating data
 * that is to be used for the dashboard tree view.
 * <p>
 * This controller provides advanced search functionality on this grid.
 *
 * @author Andrew Popp
 */
@RestController
public class JobTreeController {

    ///////////////////////////////////////////////////////////////////////
    ////////                                                     Properties                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @Autowired
    private SystemRepository<Job, Integer> jobRepository;

    @Autowired
    private TaskRepository taskRepository;

    ///////////////////////////////////////////////////////////////////////
    ////////                                           Search Method                                                       //////////
    //////////////////////////////////////////////////////////////////////

    @GetMapping("/ajax/job/search")
    public ResponseEntity<?> getJobSearchResult(String filter, Integer searchDepth, String[] extraSearchParams) {
        JobResponseBody result = new JobResponseBody();

        if (searchDepth == null || searchDepth != -1) {
            Set<Job> jobSet = new HashSet<>(jobRepository.findAll(filter, searchDepth, extraSearchParams, Pageable.unpaged()).getContent());
            Set<Task> taskSet = new HashSet<>(taskRepository.findAll(filter, searchDepth, extraSearchParams, Pageable.unpaged()).getContent());

            result.setChildren(getTaskToJobList(taskSet, jobSet, filter));
        }

        return ResponseEntity.ok(result);
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                          Support Method                                                      //////////
    //////////////////////////////////////////////////////////////////////

    private List<TreeJobDTO> getTaskToJobList(Set<Task> taskSet, Set<Job> jobSet, String filter) {
        Map<Integer, TreeJobDTO> treeJobDTOIdMap = new HashMap<>();

        iterate(iterable(jobSet), job -> {
            TreeJobDTO treeJobDTO = TreeJobDTO.newInstance(job, expandRow(filter));
            treeJobDTOIdMap.put(job.getId(), treeJobDTO);
        });

        iterate(iterable(taskSet), task -> {
            TreeJobDTO treeJobDTO;

            if (treeJobDTOIdMap.containsKey(task.getJob().getId())) {
                treeJobDTO = treeJobDTOIdMap.get(task.getJob().getId());
            } else {
                treeJobDTO = TreeJobDTO.newInstance(task.getJob(), expandRow(filter));
                treeJobDTOIdMap.put(task.getJob().getId(), treeJobDTO);
            }

            treeJobDTO.addChild(TreeTaskDTO.newInstance(task));
        });

        return new ArrayList<>(treeJobDTOIdMap.values());
    }

    private boolean expandRow(String filter) {
        //Only expand rows if the filter is not our default login filter
        //This means filter contains a single {"operator" and has "property":"project.branch.name
        if (StringUtils.countMatches(filter, "{\"operator\"") == 1 && StringUtils.contains(filter, "\"property\":\"project.branch.name")) {
            return false;
        }
        return true;
    }

    ///////////////////////////////////////////////////////////////////////
    ////////                                             Basic   Getter/Setters                                          //////////
    //////////////////////////////////////////////////////////////////////

    public SystemRepository<Job, Integer> getJobRepository() {
        return jobRepository;
    }

    public void setJobRepository(SystemRepository<Job, Integer> jobRepository) {
        this.jobRepository = jobRepository;
    }

    public TaskRepository getTaskRepository() {
        return taskRepository;
    }

    public void setTaskRepository(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }
}
