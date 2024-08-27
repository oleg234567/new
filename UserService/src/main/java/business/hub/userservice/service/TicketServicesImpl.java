package business.hub.userservice.service;

import business.hub.userservice.dto.TicketDTO;
import business.hub.userservice.model.Ticket;
import business.hub.userservice.model.TicketStatus;
import business.hub.userservice.repositories.TicketRepositories;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

/**
 * Класс TicketServicesImpl реализует сервис для работы с тикетами.
 * Предоставляет методы для выполнения операций CRUD с данными тикетов.
 * Также включает методы для обновления статуса тикета.
 */
@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class TicketServicesImpl implements TicketServices {

    private final TicketRepositories ticketRepositories;
    private KafkaTemplate<String, Object> kafkaTemplate;

    /**
     * Получение списка всех тикетов.
     *
     * @return Список всех тикетов.
     */
    @Override
    public List<Ticket> getAllTickets() {
        List<Ticket> tikets = ticketRepositories.findAll();
        log.info("Get all tickets: {}", tikets);
        return tikets;
    }


    /**
     * Получение тикета по его идентификатору.
     *
     * @param id Идентификатор тикета.
     * @return Тикет с указанным идентификатором или null, если тикет не найден.
     */
    @Override
    public Ticket getTicket(final int id) {
        Optional<Ticket> ticket = ticketRepositories.findById(id);
        log.debug("Retrieved ticket by ID: {}", id);
        return ticket.orElse(null);
    }

    /**
     * Сохранение нового тикета.
     *
     * @param ticket Тикет для сохранения.
     */
    @Override
    public void saveTicket(final Ticket ticket) {

        Ticket saved = ticketRepositories.save(ticket);
        TicketDTO dto = new TicketDTO(
                saved.getId(),
                saved.getDescription(),
                saved.getTicketstatus().getStatus()
        );
        kafkaTemplate.send("ticket-events-create", dto);

        log.info("Success save ticket: {}", saved);
    }


    /**
     * Удаление тикета по его идентификатору.
     *
     * @param id Идентификатор тикета для удаления.
     */
    @Override
    public void deleteTicket(final int id) {
        if (ticketRepositories.existsById(id)) {
            ticketRepositories.deleteById(id);
            log.info("Success delete by id: {}", id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    /**
     * Обновление статуса тикета.
     *
     * @param ticketId Идентификатор тикета.
     * @param ticketStatus Новый статус тикета.
     */
    @Override
    public void updateTicketStatus(final int ticketId,final TicketStatus ticketStatus) {
        Ticket ticket = ticketRepositories.findById(ticketId).orElseThrow(()
                -> new EntityNotFoundException("Тикет не найден: " + ticketId));
        ticket.setTicketStatus(ticketStatus);
        ticketRepositories.save(ticket);

        TicketDTO dto = new TicketDTO(
                ticket.getId(),
                ticket.getDescription(),
                ticket.getTicketstatus().getStatus()
        );
        kafkaTemplate.send("ticket-events-update-status", dto);

        log.info("Статус тикета {} обновлен на: {}", ticketId, ticketStatus);

    }


}




