package uz.pdp.trello.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.trello.entity.TaskAttachment;
import uz.pdp.trello.repo.TaskAttachmentRepo;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskAttachmentService {

    private final TaskAttachmentRepo taskAttachmentRepo;

    public TaskAttachment save(TaskAttachment taskAttachment) {
        return taskAttachmentRepo.save(taskAttachment);
    }

    public List<TaskAttachment> getAllTaskAttachments() {
        return taskAttachmentRepo.findAll();
    }

    public List<TaskAttachment> getTaskAttachmentsByTaskId(UUID taskId) {
        return taskAttachmentRepo.findAllByTaskId(taskId);
    }


    public void removeAttachment(UUID id) {
        TaskAttachment attachment = taskAttachmentRepo.findById(id).orElseThrow();
        taskAttachmentRepo.delete(attachment);
    }
}
