package uz.pdp.trello.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.trello.entity.User;
import uz.pdp.trello.service.TaskService;
import uz.pdp.trello.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/report")
public class ReportController {

    private final UserService userService;
    private final TaskService taskService;

    @GetMapping
    public String getReportPage() {
        return "report";
    }


    @GetMapping("/bylist")
    public String getTaskLists(Model model) {
        model.addAttribute("taskListCounts", taskService.getTaskCountsByTaskList());
        return "reportByList";
    }

    @GetMapping("/byuser")
    public String getUserTaskCounts(Model model) {
        List<User> users = userService.getAllUsers();
        Map<UUID, Map<String, Long>> userTaskCounts = new HashMap<>();

        for (User user : users) {
            Map<String, Long> taskCounts = new HashMap<>();
            long inProgressCount = taskService.countTasksByUserAndTaskListTitle(user, "inProgress");
            long newCount = taskService.countTasksByUserAndTaskListTitle(user, "new");
            long completedCount = taskService.countTasksByUserAndTaskListTitle(user, "completed");
            long uncompletedCount = taskService.countUncompletedTasksByUser(user);
            long pausedCount = taskService.countTasksByUserAndTaskListTitle(user, "paused");
            long allTasksCount = taskService.countTasksByUser(user);

            taskCounts.put("inprogress", inProgressCount);
            taskCounts.put("completed", completedCount);
            taskCounts.put("paused", pausedCount);
            taskCounts.put("uncompleted", uncompletedCount);
            taskCounts.put("new", newCount);
            taskCounts.put("all", allTasksCount);

            userTaskCounts.put(user.getId(), taskCounts);
        }

        model.addAttribute("users", users);
        model.addAttribute("userTaskCounts", userTaskCounts);
        return "reportByUser";
    }

}
