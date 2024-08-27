package uz.pdp.trello.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.trello.entity.Board;
import uz.pdp.trello.entity.User;
import uz.pdp.trello.service.AuthService;
import uz.pdp.trello.service.BoardService;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class homeController {

    private final BoardService boardService;


    @GetMapping()
    public ModelAndView home() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("home");
        modelAndView.addObject("boards", boardService.getAllBoards());
        modelAndView.addObject("currentBoard", new Board(UUID.randomUUID(), ""));
        return modelAndView;
    }

}
