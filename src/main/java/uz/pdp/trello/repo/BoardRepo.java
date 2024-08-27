package uz.pdp.trello.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.trello.entity.Board;

import java.util.UUID;

@Repository
public interface BoardRepo extends JpaRepository<Board, UUID> {
}
