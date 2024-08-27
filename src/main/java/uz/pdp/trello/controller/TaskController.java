package uz.pdp.trello.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import uz.pdp.trello.entity.*;
import uz.pdp.trello.service.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;
    private final TaskListService taskListService;
    private final UserService userService;
    private final AuthService authService;
    private final CommentService commentService;
    private final TaskAttachmentService taskAttachmentService;

    @GetMapping("/{id}")
    public ModelAndView taskInfo(@PathVariable UUID id, @RequestParam("boardId") UUID bardId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("task");
        modelAndView.addObject("boardId", bardId);
        if (id != null) {
            modelAndView.addObject("currentTask", taskService.getTaskById(id));
        }
        return modelAndView;
    }

    @PostMapping("/{id}")
    public String updateTask(@PathVariable UUID id, @ModelAttribute Task task, @RequestParam(name = "boardId") UUID boardId) {
        task.setId(id);
        taskService.save(task);
        return "redirect:/board/" + boardId;
    }

    @GetMapping("/add")
    public String showAddTaskPage(@RequestParam("taskListId") UUID taskListId, Model model) {
        TaskList taskList = taskListService.getTaskListsById(taskListId);
        List<User> users = userService.getAllUsers();
        model.addAttribute("task", new Task());
        model.addAttribute("taskList", taskList);
        model.addAttribute("users", users);
        return "addTask";
    }

    @PostMapping("/add")
    public String addTask(@ModelAttribute Task task, @RequestParam("taskListId") UUID taskListId) {
        User createdBy = authService.getAuthenticatedUser();
        TaskList taskList = taskListService.getTaskListsById(taskListId);

        task.setCreatedBy(createdBy);
        task.setTaskList(taskList);

        taskService.save(task);
        return "redirect:/board/" + taskList.getBoard().getId();
    }

    @GetMapping("/edit/{id}")
    public ModelAndView editTask(@PathVariable UUID id, @RequestParam("boardId") UUID boardId) {
        ModelAndView modelAndView = new ModelAndView();
        Task taskById = taskService.getTaskById(id);
        List<TaskList> taskLists = taskListService.getTaskListsByBoardId(boardId);
        modelAndView.setViewName("task");
        modelAndView.addObject("boardId", boardId);
        modelAndView.addObject("taskLists", taskLists);
        modelAndView.addObject("comments", commentService.getCommentsByTaskId(id));
        modelAndView.addObject("users", userService.getAllUsersNotInTheTask(id));
        modelAndView.addObject("attachments", taskAttachmentService.getTaskAttachmentsByTaskId(id));
        modelAndView.addObject("currentTask", taskById);
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public String editTask(@ModelAttribute Task task, @PathVariable UUID id, @RequestParam("boardId") UUID boardId) {
        task.setId(id);
        taskService.save(task);
        return "redirect:/board/" + boardId;
    }


    @PostMapping("/addmember")
    public String addMember(@RequestParam("taskId") UUID taskId,
                            @RequestParam(value = "member", required = false) UUID memberId,
                            @RequestParam("boardId") UUID boardId) {
        Task currentTask = taskService.getTaskById(taskId);
        if (memberId == null) {
            return "redirect:/task/edit/" + taskId + "?boardId=" + boardId;
        }
        User memberToAdd = userService.getUserById(memberId);
        if (!currentTask.getMembers().contains(memberToAdd)) {
            currentTask.getMembers().add(memberToAdd);
        }

        taskService.save(currentTask);
        return "redirect:/task/edit/" + taskId + "?boardId=" + boardId;
    }

    @GetMapping("/removemember/{id}")
    public String removeMember(@PathVariable UUID id, @RequestParam("taskId") UUID taskId, @RequestParam("boardId") UUID boardId) {
        taskService.removeMember(id, taskId);
        return "redirect:/task/edit/" + taskId + "?boardId=" + boardId;
    }

    @PostMapping("/addfile")
    public String addAttachment(@RequestParam("file") MultipartFile file,
                                Model model,
                                @RequestParam("taskId") UUID taskId,
                                @RequestParam("boardId") UUID boardId) {
        if (file.isEmpty()) {
            model.addAttribute("error", "File is empty");
            return "redirect:/task/addfile";
        }

        try {
            Task taskById = taskService.getTaskById(taskId);
            TaskAttachment attachment = new TaskAttachment();
            attachment.setName(file.getOriginalFilename());
            attachment.setTask(taskById);
            attachment.setContentType(file.getContentType());
            attachment.setSize((int) file.getSize());
            attachment.setContent(file.getBytes());
            taskAttachmentService.save(attachment);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Failed to upload file");
            return "redirect:/task/addfile";
        }

        return "redirect:/task/edit/" + taskId + "?boardId=" + boardId;
    }

    @GetMapping("/removefile/{id}")
    public String removeFile(@PathVariable UUID id, @RequestParam("taskId") UUID taskId, @RequestParam("boardId") UUID boardId) {
        taskAttachmentService.removeAttachment(id);
        return "redirect:/task/edit/" + taskId + "?boardId=" + boardId;
    }

    @GetMapping("/delete/{id}")
    public String deleteTaskList(@PathVariable UUID id, @RequestParam(name = "boardId") UUID boardId) {
        Task taskById = taskService.getTaskById(id);
        taskService.deleteTask(taskById.getId());
        return "redirect:/board/" + boardId;
    }

    @PostMapping("/applyname")
    public String applyTaskName(@RequestParam("currentTaskId") UUID currentTaskId,
                                @RequestParam("boardId") UUID boardId,
                                @RequestParam("title") String title) {
        taskService.updateTaskName(currentTaskId, title);

        return "redirect:/task/edit/" + currentTaskId + "?boardId=" + boardId;
    }

    @PostMapping("/applydeadline")
    public String applyDeadline(@RequestParam("taskId") UUID taskId,
                                @RequestParam("boardId") UUID boardId,
                                @RequestParam("deadline") LocalDateTime deadline) {

        Task taskById = taskService.getTaskById(taskId);
        taskById.setDeadline(deadline);
        taskService.save(taskById);
        return "redirect:/task/edit/" + taskId + "?boardId=" + boardId;
    }

    @PostMapping("/addcomment")
    public String addComment(@RequestParam("text") String text, @RequestParam("taskId") UUID taskId, @RequestParam("boardId") UUID boardId) {
        User currentUser = authService.getAuthenticatedUser();
        Task task = taskService.getTaskById(taskId);

        if (task != null && currentUser != null) {
            Comment comment = new Comment();
            comment.setText(text);
            comment.setTask(task);
            comment.setCommentedBy(currentUser);
            commentService.save(comment);
        }

        return "redirect:/task/edit/" + taskId + "?boardId=" + boardId;
    }

    @PostMapping("/changetasklist/{id}")
    public String changeTaskList(@PathVariable UUID id, @RequestParam("taskListId") UUID taskListId, @RequestParam("boardId") UUID boardId) {
        Task task = taskService.getTaskById(id);
        TaskList taskList = taskListService.getTaskListsById(taskListId);
        if (!task.getTaskList().getTitle().equalsIgnoreCase("completed") && taskList.getTitle().equalsIgnoreCase("completed")) {
            task.setFinishedAt(LocalDateTime.now());
        }
        task.setTaskList(taskList);
        taskService.save(task);
        return "redirect:/task/edit/" + id + "?boardId=" + boardId;
    }

}
