package uz.pdp.trello.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.trello.entity.Task;
import uz.pdp.trello.entity.User;
import uz.pdp.trello.handler.ResourceNotFoundException;
import uz.pdp.trello.repo.CommentRepo;
import uz.pdp.trello.repo.TaskAttachmentRepo;
import uz.pdp.trello.repo.TaskRepo;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepo taskRepo;
    private final CommentRepo commentRepo;
    private final TaskAttachmentRepo taskAttachmentRepo;

    public List<Task> getAllTasks() {
        return taskRepo.findAllByOrderByCreatedAtAsc();
    }

    public Task getTaskById(UUID id) {
        return taskRepo.findById(id).orElseThrow();
    }

    public Task save(Task task) {
        return taskRepo.save(task);
    }

    public void removeMember(UUID id, UUID taskId) {
        Task taskById = getTaskById(taskId);
        List<User> members = taskById.getMembers();
        List<User> newMembers = new ArrayList<>();
        for (User member : members) {
            if (!member.getId().equals(id)) {
                newMembers.add(member);
            }
        }
        taskById.setMembers(newMembers);
        taskRepo.save(taskById);
    }

    public void updateTaskName(UUID currentTaskId, String title) {
        Task taskById = getTaskById(currentTaskId);
        taskById.setTitle(title);
        taskRepo.save(taskById);
    }

    @Scheduled(fixedRate = 2000)
    public void checkAndUpdateTaskDeadlines() {
        List<Task> tasks = taskRepo.findAll();
        for (Task task : tasks) {
            if (task.getTaskList().getTitle().equalsIgnoreCase("completed")) {
                task.setCompletedBeforeDeadline(true);
                if (task.getFinishedAt() == null) {
                    task.setFinishedAt(LocalDateTime.now());
                }
                taskRepo.save(task);
            } else {
                boolean completedBeforeDeadline = task.getDeadline() == null || LocalDateTime.now().isBefore(task.getDeadline());
                task.setCompletedBeforeDeadline(completedBeforeDeadline);
                taskRepo.save(task);
            }
        }
    }

    public long countTasksByUserAndTaskListTitle(User user, String title) {
        return taskRepo.countTasksByUserAndTaskListTitle(user.getId(), title.toLowerCase());
    }

    public long countTasksByUser(User user) {
        return taskRepo.countTasksByUser(user.getId());
    }

    public Map<String, Long> getTaskCountsByTaskList() {
        List<Object[]> results = taskRepo.countTasksByTaskList();
        Map<String, Long> taskListCounts = new HashMap<>();

        taskListCounts.put("inProgress", 0L);
        taskListCounts.put("new", 0L);
        taskListCounts.put("completed", 0L);
        taskListCounts.put("paused", 0L);
        taskListCounts.put("uncompleted", 0L);

        for (Object[] result : results) {
            String taskListTitle = (String) result[0];
            Long taskCount = (Long) result[1];
            taskListCounts.put(taskListTitle, taskCount);
        }

        long uncompletedCount = taskRepo.findAll()
                .stream()
                .filter(task -> task.getDeadline() != null && task.getDeadline().isBefore(LocalDateTime.now()))
                .filter(task -> !task.getTaskList().getTitle().equalsIgnoreCase("completed"))
                .count();

        taskListCounts.put("uncompleted", uncompletedCount);

        return taskListCounts;
    }


    public long countUncompletedTasksByUser(User user) {
        return taskRepo.findByMembersId(user.getId())
                .stream()
                .filter(task -> task.getDeadline() != null && !task.isCompletedBeforeDeadline())
                .filter(task -> !task.getTaskList().getTitle().equalsIgnoreCase("completed"))
                .count();
    }

    public List<Task> getTasksByMemberId(UUID userId) {
        return taskRepo.findByMembersIdOrderByCreatedAtAsc(userId);
    }

    public void deleteTasksByTaskListId(UUID taskListId) {
        List<Task> tasks = taskRepo.findAllByTaskListId(taskListId);
        for (Task task : tasks) {
            deleteTask(task.getId());
        }
    }

    public void deleteTask(UUID taskId) {
        Task task = taskRepo.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        commentRepo.deleteAllByTaskId(taskId);
        taskAttachmentRepo.deleteAllByTaskId(taskId);

        taskRepo.delete(task);
    }

}
