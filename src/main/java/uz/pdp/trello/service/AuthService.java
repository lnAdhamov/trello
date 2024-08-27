package uz.pdp.trello.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import uz.pdp.trello.entity.User;
import uz.pdp.trello.repo.UserRepo;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepo userRepo;

    public User getAuthenticatedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String phone;
        if (principal instanceof UserDetails) {
            phone = ((UserDetails) principal).getUsername();
        } else {
            phone = principal.toString();
        }
        return userRepo.findByPhone(phone);
    }
}
