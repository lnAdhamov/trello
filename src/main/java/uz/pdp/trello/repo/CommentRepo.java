package uz.pdp.trello.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import uz.pdp.trello.entity.Comment;
import uz.pdp.trello.entity.Task;

import java.util.List;
import java.util.UUID;


@Repository
public interface CommentRepo extends JpaRepository<Comment, UUID> {
    List<Comment> findAllByTaskId(UUID id);

    @Modifying
    @Transactional
    void deleteAllByTaskId(UUID id);
}
