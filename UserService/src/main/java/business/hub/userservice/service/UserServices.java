package business.hub.userservice.service;

import business.hub.userservice.DTO.UserDTO;
import business.hub.userservice.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс UserServices представляет сервис для работы с пользователями.
 * Определяет методы для выполнения операций CRUD (создание, чтение, обновление, удаление) с данными пользователей.
 * Также включает методы для обновления статуса профиля пользователя и обработки событий создания профиля.
 */
public interface UserServices {

    /**
     * Получение списка всех пользователей.
     *
     * @return Список всех пользователей.
     */
    List<User> getAllUsers();

    /**
     * Получение пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Пользователь с указанным идентификатором или null, если пользователь не найден.
     */
    User getUser(int id);

    /**
     * Сохранение нового пользователя.
     *
     * @param user Пользователь для сохранения.
     */
    void saveUser(User user);

    /**
     * Удаление пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя для удаления.
     */
    void deleteUser(int id);

    /**
     * Обновление статуса профиля пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @param status Новый статус профиля.
     */
    void updateProfileStatus(int userId, String status);

    /**
     * Обработка события создания профиля пользователя.
     *
     * @param userId  Идентификатор пользователя.
     * @param created Флаг успешного создания профиля.
     */
    void handleProfileCreationEvent(int userId, boolean created);

    /**
     * Поиск пользователя по email.
     *
     * @param email пользователя для поиска.
     * @return Найденный пользователь или null, если пользователь с указанным email не найден.
     */
    Optional<User> findByEmail(String email);

    /**
     * Регистрация нового пользователя.
     *
     * @param userDTO объект содержащий данные нового пользователя.
     * @return ResponseEntity<UserDTO> объект, содержащий нового пользователя и статус HTTP
     */
    UserDTO registerUser(UserDTO userDTO);

    /**
     * Проверка кода подтверждения.
     *
     * @param code, код который отправлен пользователю для подтверждения учетной записи.
     * @return true, усли код действителен и false, если код недействителен или истек.
     */
    boolean verifyUser(String code);

    /**
     * Создание и отправка нового кода подтверждения.
     *
     * @param userId, идентификатор пользователя, для которго запрашивается code.
     * @return true, усли код действителен и false, если код недействителен или истек.
     */
    void renewConfirmationCode(Integer userId);

}


