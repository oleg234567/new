package business.hub.userservice.kafka;

public class ProfileCreationEvent {
    private int userId;
    private boolean created;

    /**
     * Конструктор по умолчанию.
     */
    public ProfileCreationEvent() {
    }

    /**
     * Получение идентификатора пользователя.
     *
     * @return Идентификатор пользователя.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Установка идентификатора пользователя.
     *
     * @param userIdParam Идентификатор пользователя.
     */
    public void setUserId(final int userIdParam) {
        this.userId = userIdParam;
    }

    /**
     * Проверка успешности создания профиля.
     *
     * @return true, если профиль успешно создан, иначе false.
     */
    public boolean isCreated() {
        return created;
    }


    /**
     * Установка флага успешности создания профиля.
     *
     * @param createdParam Флаг успешности создания профиля.
     */
    public void setCreated(final boolean createdParam) {
        this.created = createdParam;
    }
}
