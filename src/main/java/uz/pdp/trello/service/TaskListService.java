package uz.pdp.trello.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.trello.entity.Task;
import uz.pdp.trello.entity.TaskList;
import uz.pdp.trello.handler.ResourceNotFoundException;
import uz.pdp.trello.repo.TaskListRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskListService {

    private final TaskListRepo taskListRepo;
    private final TaskService taskService;


    public List<TaskList> getTaskListsByBoardId(UUID boardId) {
        return taskListRepo.findAllByBoardIdOrderByOrderNumber(boardId);
    }

    public TaskList save(TaskList taskList) {
        return taskListRepo.save(taskList);
    }

    public TaskList getTaskListsById(UUID id) {
        return taskListRepo.findById(id).orElseThrow();
    }

    public Integer getMaxOrder(UUID boardId) {
        return taskListRepo.findMaxOrderByBoardId(boardId);
    }

    public List<Integer> getOrders(UUID boardId) {
        List<Integer> orders = new ArrayList<>();
        for (Integer i = 1; i <= getMaxOrder(boardId); i++) {
            orders.add(i);
        }
        return orders;
    }

    @Transactional
    public void updateTaskListOrderAndTitle(TaskList oldTaskList, TaskList newTaskList) {
        Integer newOrderNumber = newTaskList.getOrderNumber();
        Integer oldOrderNumber = oldTaskList.getOrderNumber();
        UUID boardId = oldTaskList.getBoard().getId();

        if (newOrderNumber.equals(oldOrderNumber)) {
            oldTaskList.setTitle(newTaskList.getTitle());
            taskListRepo.save(oldTaskList);
            return;
        }

        if (newOrderNumber > oldOrderNumber) {
            List<TaskList> affectedTaskLists = taskListRepo
                    .findByBoardIdAndOrderNumberGreaterThanAndOrderNumberLessThanEqual(boardId, oldOrderNumber, newOrderNumber);
            for (TaskList affectedTaskList : affectedTaskLists) {
                affectedTaskList.setOrderNumber(affectedTaskList.getOrderNumber() - 1);
                taskListRepo.save(affectedTaskList);
            }
        } else {
            List<TaskList> affectedTaskLists = taskListRepo
                    .findByBoardIdAndOrderNumberGreaterThanEqualAndOrderNumberLessThan(boardId, newOrderNumber, oldOrderNumber);
            for (TaskList affectedTaskList : affectedTaskLists) {
                affectedTaskList.setOrderNumber(affectedTaskList.getOrderNumber() + 1);
                taskListRepo.save(affectedTaskList);
            }
        }

        oldTaskList.setOrderNumber(newOrderNumber);
        oldTaskList.setTitle(newTaskList.getTitle());
        taskListRepo.save(oldTaskList);
    }

    public void deleteTaskList(UUID id) {
        TaskList taskList = taskListRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaskList", "id", id));

        taskService.deleteTasksByTaskListId(id);

        taskListRepo.delete(taskList);
    }
}
