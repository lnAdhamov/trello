package uz.pdp.trello.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.trello.entity.Comment;
import uz.pdp.trello.repo.CommentRepo;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepo commentRepo;

    public List<Comment> getCommentsByTaskId(UUID id) {
        return commentRepo.findAllByTaskId(id);
    }

    public Comment save(Comment comment) {
        return commentRepo.save(comment);
    }
}
