package uz.pdp.trello.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.pdp.trello.entity.TaskAttachment;

import java.util.List;
import java.util.UUID;

public interface TaskAttachmentRepo extends JpaRepository<TaskAttachment, UUID> {
    List<TaskAttachment> findAllByTaskId(UUID taskId);

    void deleteAllByTaskId(UUID taskId);
}
