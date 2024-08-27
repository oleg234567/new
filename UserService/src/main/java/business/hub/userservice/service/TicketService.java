package business.hub.userservice.service;

import business.hub.userservice.model.Ticket;
import business.hub.userservice.model.TicketStatus;

import java.util.List;

/**
 * Интерфейс TicketServices представляет сервис для работы с тикетами.
 * Определяет методы для выполнения операций CRUD (создание, чтение, обновление, удаление) с данными тикетов.
 * Также включает методы для обновления статуса тикета.
 */

public interface TicketService {
    /**
     * Получение списка всех тикетов.
     *
     * @return Список всех тикетов.
     */
    List<Ticket> getAllTickets();

    /**
     * Получение тикета по его идентификатору.
     *
     * @param id Идентификатор тикета.
     */
    void getTicket(int id);

    /**
     * Сохранение нового тикета.
     *
     * @param ticket Тикет для сохранения.
     */
    void saveTicket(Ticket ticket);

    /**
     * Удаление тикета по его идентификатору.
     *
     * @param id Идентификатор тикета для удаления.
     */
    void deleteTicket(int id);

    /**
     * Обновление статуса тикета.
     *
     * @param ticketId     Идентификатор тикета.
     * @param ticketStatus Новый статус тикета.
     */
    void updateTicketStatus(int ticketId, TicketStatus ticketStatus);


}

