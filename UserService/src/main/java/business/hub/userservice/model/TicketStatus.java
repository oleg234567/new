package business.hub.userservice.model;

public enum TicketStatus {
    CREATED("Создан"),
    WORKS("В работе"),
    COMPLETED("Завершен"),
    RETURNED("Возвращен");

    private final String status;

    TicketStatus(final String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
