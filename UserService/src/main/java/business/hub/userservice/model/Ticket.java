package business.hub.userservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

import java.util.Objects;

@Entity
@Setter
@Getter
@ToString
@AllArgsConstructor
@Transactional
@NoArgsConstructor
@Table(name = "ticket")
public class Ticket {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TicketStatus ticketstatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    /**
     * Конструктор с параметрами.
     *
     * @param description      описание тикета.
     * @param ticketStatus Статус тикета.
     */
    public Ticket(String description, TicketStatus ticketStatus) {
        this.description = description;
        this.ticketstatus = ticketStatus;
    }
    /**
     * Метод для изменения статуса тикета.
     *
     * @param ticketStatus Статус тикета.
     */
    public void setTicketStatus (final TicketStatus ticketStatus) {
        this.ticketstatus = ticketStatus;
    }

    /**
     * Переопределение метода equals для сравнения тикетов.
     *
     * @param o Объект для сравнения.
     * @return true, если тикеты равны, иначе false.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ticket ticket = (Ticket) o;
        return Objects.equals(id, ticket.id) && Objects.equals(description, ticket.description)
                && Objects.equals(ticketstatus, ticket.ticketstatus);
    }
    /**
     * Переопределение метода hashCode для генерации хэш-кода тикетов.
     *
     * @return Хэш-код тикетов.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, description,
                ticketstatus);
    }
}