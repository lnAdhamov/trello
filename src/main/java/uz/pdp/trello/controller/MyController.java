package uz.pdp.trello.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import uz.pdp.trello.entity.Role;
import uz.pdp.trello.entity.User;
import uz.pdp.trello.repo.RoleRepo;
import uz.pdp.trello.service.UserService;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyController {

    private final UserService userService;
    private final RoleRepo roleRepo;


    @GetMapping("/login")
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("login");
        if (error != null) {
            String errorMessage = (String) session.getAttribute("error");
            modelAndView.addObject("error", errorMessage);
        }
        return modelAndView;
    }

    @GetMapping("/logout")
    public String logout() {
        return "logout";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.getAllRoles());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, @RequestParam List<Integer> roleIds, @RequestParam("photo") MultipartFile photo) throws IOException {
        userService.saveUser(user, roleIds, photo);
        return "redirect:/login";
    }

}
