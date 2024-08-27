package uz.pdp.trello.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.trello.entity.Task;

import java.util.Collection;
import java.util.List;
import java.util.UUID;


@Repository
public interface TaskRepo extends JpaRepository<Task, UUID> {

    @Query("SELECT COUNT(t) FROM Task t JOIN t.members m WHERE m.id = :userId AND LOWER(t.taskList.title) = :title")
    long countTasksByUserAndTaskListTitle(UUID userId, String title);

    @Query("SELECT COUNT(t) FROM Task t JOIN t.members m WHERE m.id = :userId")
    long countTasksByUser(UUID userId);

    @Query("SELECT t.taskList.title, COUNT(t) FROM Task t GROUP BY t.taskList.title")
    List<Object[]> countTasksByTaskList();

    @Query("SELECT t FROM Task t JOIN t.members m WHERE m.id = :memberId")
    List<Task> findByMembersId(UUID memberId);

    List<Task> findByMembersIdOrderByCreatedAtAsc(UUID id);

    List<Task> findAllByOrderByCreatedAtAsc();

    List<Task> findAllByTaskListId(UUID id);
}
