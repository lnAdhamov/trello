package uz.pdp.trello.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.trello.entity.Board;
import uz.pdp.trello.entity.TaskList;
import uz.pdp.trello.service.BoardService;
import uz.pdp.trello.service.TaskListService;

import java.util.UUID;


@Controller
@RequiredArgsConstructor
@RequestMapping("/tasklist")
public class TaskListController {

    private final TaskListService taskListService;
    private final BoardService boardService;


    @GetMapping("/edit/{id}")
    public ModelAndView editTaskList(@PathVariable UUID id, @RequestParam("boardId") UUID boardId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("taskList");
        modelAndView.addObject("boardId", boardId);
        modelAndView.addObject("orders", taskListService.getOrders(boardId));
        if (id != null) {
            modelAndView.addObject("currentTaskList", taskListService.getTaskListsById(id));
        }
        return modelAndView;
    }

    @PostMapping("/edit/{id}")
    public String editTaskList(@PathVariable UUID id, @ModelAttribute TaskList taskList, @RequestParam(name = "boardId") UUID boardId) {
        Board boardById = boardService.getBoardById(boardId);
        taskList.setBoard(boardById);
        TaskList oldTaskList = taskListService.getTaskListsById(id);
        taskList.setId(id);
        taskListService.updateTaskListOrderAndTitle(oldTaskList, taskList);
        return "redirect:/board/" + boardId;
    }

    @GetMapping("/delete/{id}")
    public String deleteTaskList(@PathVariable UUID id, @RequestParam(name = "boardId") UUID boardId) {
        taskListService.deleteTaskList(id);
        return "redirect:/board/" + boardId;
    }

    @GetMapping("/add")
    public ModelAndView addTaskList(@RequestParam("boardId") UUID boardId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addTaskList");
        modelAndView.addObject("boardId", boardId);
        if (taskListService.getMaxOrder(boardId) != null) {
            modelAndView.addObject("maxOrder", taskListService.getMaxOrder(boardId) + 1);
        } else {
            modelAndView.addObject("maxOrder", 1);
        }
        return modelAndView;
    }

    @PostMapping("/add")
    public String addTaskList(@ModelAttribute TaskList taskList, @RequestParam(name = "boardId") UUID boardId) {
        Board boardById = boardService.getBoardById(boardId);
        taskList.setBoard(boardById);
        taskListService.save(taskList);
        return "redirect:/board/" + boardId;
    }
}
