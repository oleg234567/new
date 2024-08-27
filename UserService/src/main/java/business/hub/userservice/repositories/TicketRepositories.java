package business.hub.userservice.repositories;

import business.hub.userservice.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface TicketRepositories extends JpaRepository<Ticket, Integer> {
}
