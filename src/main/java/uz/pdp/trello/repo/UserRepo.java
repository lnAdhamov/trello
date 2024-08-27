package uz.pdp.trello.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.pdp.trello.entity.User;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {

    User findByPhone(String phone);


    @Query("SELECT u FROM User u WHERE u.id NOT IN (SELECT m.id FROM Task t JOIN t.members m WHERE t.id = :currentTaskId)")
    List<User> findAllNotInTheTask(@Param("currentTaskId") UUID currentTaskId);

    List<User> findByIdIn(List<UUID> memberIds);
}
