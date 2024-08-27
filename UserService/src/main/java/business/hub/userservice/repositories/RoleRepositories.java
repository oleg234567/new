package business.hub.userservice.repositories;

import business.hub.userservice.model.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface RoleRepositories extends JpaRepository<Role, Integer> {
}
