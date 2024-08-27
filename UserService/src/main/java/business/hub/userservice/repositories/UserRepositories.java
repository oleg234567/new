package business.hub.userservice.repositories;

import business.hub.userservice.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Репозиторий для доступа к данным пользователей.
 * Предоставляет методы для выполнения операций чтения, записи и обновления данных пользователей в базе данных.
 */
@Repository
@Transactional
public interface UserRepositories extends JpaRepository<User, Integer> {
    /**
     * Поиск пользователя по имени пользователя.
     *
     * @param username Имя пользователя для поиска.
     * @return Найденный пользователь или null, если пользователь с указанным именем не найден.
     */
    User findByUsername(String username);

    /**
     * Поиск пользователя по email.
     *
     * @param email пользователя для поиска.
     * @return Найденный пользователь или null, если пользователь с указанным email не найден.
     */
    Optional<User> findByEmail(String email);


}

