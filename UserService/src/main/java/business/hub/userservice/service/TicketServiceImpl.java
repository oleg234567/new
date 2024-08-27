package business.hub.userservice.service;

import business.hub.userservice.model.Ticket;
import business.hub.userservice.model.TicketStatus;
import business.hub.userservice.repositories.TicketRepositories;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepositories ticketRepositories;

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
     */
    @Override
    public void getTicket(final int id) {
        Optional<Ticket> ticket = ticketRepositories.findById(id);
        log.info("Get ticket: {}", ticket.orElse(null));
    }

    /**
     * Сохранение нового тикета.
     *
     * @param ticket Тикет для сохранения.
     */
    @Override
    public void saveTicket(final Ticket ticket) {

        Ticket saved = ticketRepositories.save(ticket);
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
        log.info("Статус тикета {} обновлен на: {}", ticketId, ticketStatus);

    }


}