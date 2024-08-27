package business.hub.userservice.controller;

import business.hub.userservice.model.Ticket;
import business.hub.userservice.model.TicketStatus;
import business.hub.userservice.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketServices;

    public TicketController(TicketService ticketServices) {
        this.ticketServices = ticketServices;
    }

    /**
     * Создание нового билета
     * @param ticket Данные нового билета
     * @return Сохраненный билет
     */
    @PostMapping
    public ResponseEntity<Ticket> createTicket(final @RequestBody Ticket ticket) {
        ticketServices.saveTicket(ticket);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Просмотр билета по идентификатору
     * @param id идентификатор билета
     * @return полученный пользователь по идентификатору
     */
    @GetMapping("{id}")
    public ResponseEntity<Ticket> getTicketById(final @PathVariable("id") int id) {
        ticketServices.getTicket(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Получение билета по идентификатору для обновления статуса на возвращение
     * @param id идентификатор билета
     * @return заявку с обновленным статусом
     */

    @PatchMapping("/return/{id}")
    public ResponseEntity<Ticket> returnTicket(@PathVariable int id) {

        TicketStatus newStatus = null;
        for (TicketStatus status : TicketStatus.values()) {
            if ("RETURNED".equals(status.getStatus())) {
                newStatus = status;
                break;
            }
        }
        ticketServices.updateTicketStatus(id, newStatus);
        return ResponseEntity.ok().build();
    }
    /**
     * Получение билета по идентификатору для обновления статуса на возобновление
     * @param id идентификатор билета
     * @return заявку с обновленным статусом
     */
    @PatchMapping("/reopen/{id}")
    public ResponseEntity<Ticket> reopenTicket(@PathVariable int id) {

        TicketStatus newStatus = null;
        for (TicketStatus status : TicketStatus.values()) {
            if ("WORKS".equals(status.getStatus())) {
                newStatus = status;
                break;
            }
        }
        ticketServices.updateTicketStatus(id, newStatus);
        return ResponseEntity.ok().build();
    }

}
