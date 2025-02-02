package uz.pdp.trello.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.pdp.trello.entity.Role;


@Repository
public interface RoleRepo extends JpaRepository<Role, Integer> {
}
