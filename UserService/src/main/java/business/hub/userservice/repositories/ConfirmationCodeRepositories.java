package business.hub.userservice.repositories;

import business.hub.userservice.model.ConfirmationCode;
import business.hub.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Репозиторий для доступа к token.
 * Предоставляет методы для выполнения операций чтения, записи и обновления token в базе данных.
 */
@Repository
@Transactional
public interface ConfirmationCodeRepositories extends JpaRepository<ConfirmationCode, Integer> {

    /**
     * Поиск объекта ConfirmationCode по значению code.
     *
     * @param code значение для поиска в базе данных.
     * @return Найденный ConfirmationCode или null, если объект с указанным code не найден.
     */
    Optional<ConfirmationCode> findByCode(String code);

    /**
     * Находит код подтверждения, связанный с указанным пользователем.
     *
     * @param user пользователь, для которого необходимо найти код подтверждения
     * @return Код подтверждения, связанный с пользователем, или null, если для пользователя не существует кода
     */
    ConfirmationCode findByUser(User user);
}

