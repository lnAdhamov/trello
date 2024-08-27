package uz.pdp.trello.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.springframework.stereotype.Service;
import uz.pdp.trello.entity.Board;
import uz.pdp.trello.repo.BoardRepo;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepo boardRepo;

    public List<Board> getAllBoards() {
        return boardRepo.findAll();
    }

    public Board getBoardById(UUID boardId) {
        return boardRepo.findById(boardId).orElseThrow();
    }

    public Board save(Board board) {
        return boardRepo.save(board);
    }
}
