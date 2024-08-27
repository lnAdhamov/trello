package uz.pdp.trello.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.trello.entity.Role;
import uz.pdp.trello.entity.User;
import uz.pdp.trello.repo.RoleRepo;
import uz.pdp.trello.repo.UserRepo;

import java.io.IOException;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
//    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;

    public User getUserByPhone(String phone) {
        return userRepo.findByPhone(phone);
    }

    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }

    public void saveUser(User user, List<Integer> roleIds, MultipartFile photo) throws IOException {
        List<Role> roles = roleRepo.findAllById(roleIds);
        user.setRoles(roles);
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (!photo.isEmpty()) {
            user.setPhoto(photo.getBytes());
        }
        userRepo.save(user);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public List<User> getAllUsersNotInTheTask(UUID currentTaskId) {
        return userRepo.findAllNotInTheTask(currentTaskId);
    }

    public List<User> getUsersByIds(List<UUID> memberIds) {
        return userRepo.findByIdIn(memberIds);
    }

    public User getUserById(UUID memberId) {
        return userRepo.findById(memberId).orElseThrow();
    }
}
