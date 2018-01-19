package com.job.tracker.job.tree;

import com.job.tracker.job.Job;
import com.job.tracker.task.Task;
import com.job.tracker.task.TaskRepository;
import com.system.db.repository.base.entity.SystemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

import static com.system.util.collection.CollectionUtils.iterable;
import static com.system.util.collection.CollectionUtils.iterate;
import static org.apache.commons.lang3.StringUtils.isEmpty;

@RestController
public class JobTreeController {

    @Autowired
    private SystemRepository<Job, Integer> jobRepository;

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/ajax/job/search")
    public ResponseEntity<?> getJobSearchResult(String filter, Integer searchDepth, String[] extraSearchParams) {
        JobResponseBody result = new JobResponseBody();

        Set<Job> jobSet = new HashSet<>(jobRepository.findAll(filter, searchDepth, extraSearchParams, Pageable.unpaged()).getContent());
        Set<Task> taskSet = new HashSet<>(taskRepository.findAll(filter, searchDepth, extraSearchParams, Pageable.unpaged()).getContent());

        result.setChildren(getTaskToJobList(taskSet, jobSet, filter));

        return ResponseEntity.ok(result);
    }

    private List<TreeJobDTO> getTaskToJobList(Set<Task> taskSet, Set<Job> jobSet, String filter) {
        Map<Integer, TreeJobDTO> treeJobDTOIdMap = new HashMap<>();

        iterate(iterable(jobSet), job -> {
            TreeJobDTO treeJobDTO = TreeJobDTO.newInstance(job, !isEmpty(filter));
            treeJobDTOIdMap.put(job.getId(), treeJobDTO);
        });

        iterate(iterable(taskSet), task -> {
            TreeJobDTO treeJobDTO;

            if (treeJobDTOIdMap.containsKey(task.getJob().getId())) {
                treeJobDTO = treeJobDTOIdMap.get(task.getJob().getId());
            } else {
                treeJobDTO = TreeJobDTO.newInstance(task.getJob(), !isEmpty(filter));
                treeJobDTOIdMap.put(task.getJob().getId(), treeJobDTO);
            }

            treeJobDTO.addChild(TreeTaskDTO.newInstance(task));
        });

        return new ArrayList<>(treeJobDTOIdMap.values());
    }


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
