package uz.pdp.trello.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.trello.entity.Board;
import uz.pdp.trello.entity.TaskList;
import uz.pdp.trello.entity.User;
import uz.pdp.trello.service.AuthService;
import uz.pdp.trello.service.BoardService;
import uz.pdp.trello.service.TaskListService;
import uz.pdp.trello.service.TaskService;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final TaskListService taskListService;
    private final TaskService taskService;
    private final AuthService authService;

    @GetMapping("/{boardId}")
    public ModelAndView homeByBoardId(@PathVariable UUID boardId, @RequestParam(value = "userId", required = false) UUID userId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("board");
        if (boardId != null) {
            User currentUser = authService.getAuthenticatedUser();
            modelAndView.addObject("currentUser", currentUser);
            Board currentBoard = boardService.getBoardById(boardId);
            modelAndView.addObject("currentBoard", currentBoard);
            List<TaskList> taskLists = taskListService.getTaskListsByBoardId(boardId);
            modelAndView.addObject("taskLists", taskLists);
            if (userId != null) {
                modelAndView.addObject("tasks", taskService.getTasksByMemberId(userId));
            } else {
                modelAndView.addObject("tasks", taskService.getAllTasks());
            }
        }
        modelAndView.addObject("boards", boardService.getAllBoards());
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView addTaskList() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("addBoard");
        return modelAndView;
    }

    @PostMapping("/add")
    public String addTaskList(@ModelAttribute Board board) {
        boardService.save(board);
        return "redirect:/board/" + board.getId();
    }


}
