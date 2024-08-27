package uz.pdp.trello.runnable;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.pdp.trello.entity.*;
import uz.pdp.trello.repo.*;
import uz.pdp.trello.utils.FileUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {
    private final PasswordEncoder passwordEncoder;
    private final BoardRepo boardRepo;
    private final TaskListRepo taskListRepo;
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final TaskRepo taskRepo;
    private final CommentRepo commentRepo;

    @Override
    public void run(ApplicationArguments args) throws IOException {
//
//        Board board1 = new Board();
//        board1.setTitle("g35");
//        Board board2 = new Board();
//        board2.setTitle("g36");
//        boardRepo.save(board1);
//        boardRepo.save(board2);
//
//        TaskList taskList1 = new TaskList();
//        taskList1.setOrderNumber(1);
//        taskList1.setTitle("exam");
//        taskList1.setBoard(board1);
//        TaskList taskList2 = new TaskList();
//        taskList2.setOrderNumber(2);
//        taskList2.setTitle("homework");
//        taskList2.setBoard(board1);
//        TaskList taskList3 = new TaskList();
//        taskList3.setOrderNumber(1);
//        taskList3.setTitle("classwork");
//        taskList3.setBoard(board2);
//        taskListRepo.save(taskList1);
//        taskListRepo.save(taskList2);
//        taskListRepo.save(taskList3);
//
//        Role role1 = new Role();
//        role1.setId(1);
//        role1.setName("ROLE_ADMIN");
//        Role role2 = new Role();
//        role2.setName("ROLE_USER");
//        role2.setId(2);
//        roleRepo.save(role1);
//        roleRepo.save(role2);
//
//        User user1 = new User();
//        user1.setFirstName("abdulmuhsin");
//        user1.setLastName("adhamov");
//        user1.setPhone("111");
//        user1.setPhoto(FileUtil.readImageAsByteArray("src/main/resources/images/photo1.jpg"));
//        user1.setPassword(passwordEncoder.encode("111"));
//        user1.setRoles(List.of(role1, role2));
//        User user2 = new User();
//        user2.setFirstName("diyorbek");
//        user2.setLastName("boqijnov");
//        user2.setPhone("222");
//        user2.setPassword(passwordEncoder.encode("222"));
//        user2.setRoles(List.of(role1));
//        user2.setPhoto(FileUtil.readImageAsByteArray("src/main/resources/images/photo2.jpg"));
//        User user3 = new User();
//        user3.setFirstName("eshmat");
//        user3.setLastName("toshmatov");
//        user3.setPhone("333");
//        user3.setPassword(passwordEncoder.encode("333"));
//        user3.setRoles(List.of(role1, role2));
//        user3.setPhoto(FileUtil.readImageAsByteArray("src/main/resources/images/photo3.jpg"));
//        userRepo.save(user1);
//        userRepo.save(user2);
//        userRepo.save(user3);
//
//        Task task1 = new Task();
//        task1.setTitle("Finish Exam Project");
//        task1.setTaskList(taskList1);
//        task1.setCreatedAt(LocalDateTime.now());
//        task1.setCreatedBy(user1);
//        task1.setDeadline(LocalDateTime.of(2024, 5, 29, 20, 30));
//        Task task2 = new Task();
//        task2.setTitle("Finish homework");
//        task2.setTaskList(taskList2);
//        task2.setCreatedAt(LocalDateTime.now());
//        task2.setCreatedBy(user1);
//        task2.setDeadline(LocalDateTime.of(2024, 5, 30, 9, 30));
//        Task task3 = new Task();
//        task3.setTitle("Go To Sleep");
//        task3.setTaskList(taskList2);
//        task3.setCreatedAt(LocalDateTime.now());
//        task3.setCreatedBy(user1);
//        task3.setDeadline(LocalDateTime.of(2024, 7, 12, 20, 30));
//        Task task4 = new Task();
//        task4.setTitle("Play Counter Strike 2");
//        task4.setTaskList(taskList1);
//        task4.setCreatedAt(LocalDateTime.now());
//        task4.setCreatedBy(user1);
//        task4.setDeadline(LocalDateTime.of(2024, 7, 12, 20, 30));
//        taskRepo.save(task1);
//        taskRepo.save(task2);
//        taskRepo.save(task3);
//        taskRepo.save(task4);
//
//        Comment comment1 = new Comment();
//        comment1.setCommentedAt(LocalDateTime.now());
//        comment1.setText("wow");
//        comment1.setTask(task1);
//        comment1.setCommentedBy(user2);
//        Comment comment2 = new Comment();
//        comment2.setCommentedAt(LocalDateTime.now());
//        comment2.setTask(task2);
//        comment2.setText("womp womp");
//        comment2.setCommentedBy(user1);
//        Comment comment3 = new Comment();
//        comment3.setCommentedAt(LocalDateTime.now());
//        comment3.setTask(task3);
//        comment3.setText("nice task!");
//        comment3.setCommentedBy(user2);
//        Comment comment4 = new Comment();
//        comment4.setCommentedAt(LocalDateTime.now());
//        comment4.setTask(task1);
//        comment4.setText("ain't doin that axaxax");
//        comment4.setCommentedBy(user1);
//
//        commentRepo.save(comment1);
//        commentRepo.save(comment2);
//        commentRepo.save(comment3);
//        commentRepo.save(comment4);
    }
}