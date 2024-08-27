package uz.pdp.trello.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import uz.pdp.trello.entity.TaskList;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskListRepo extends JpaRepository<TaskList, UUID> {
    List<TaskList> findAllByBoardIdOrderByOrderNumber(UUID boardId);

    @Query("SELECT MAX(t.orderNumber) FROM TaskList t where t.board.id = :boardId")
    Integer findMaxOrderByBoardId(UUID boardId);

    List<TaskList> findByBoardIdAndOrderNumberGreaterThanAndOrderNumberLessThanEqual(UUID boardId, Integer oldOrderNumber, Integer newOrderNumber);

    List<TaskList> findByBoardIdAndOrderNumberGreaterThanEqualAndOrderNumberLessThan(UUID boardId, Integer newOrderNumber, Integer oldOrderNumber);

}
