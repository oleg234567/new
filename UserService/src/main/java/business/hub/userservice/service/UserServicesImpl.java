package business.hub.userservice.service;

import business.hub.userservice.DTO.UserDTO;
import business.hub.userservice.model.ConfirmationCode;
import business.hub.userservice.model.Role;
import business.hub.userservice.model.User;
import business.hub.userservice.repositories.ConfirmationCodeRepositories;
import business.hub.userservice.repositories.RoleRepositories;
import business.hub.userservice.repositories.UserRepositories;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Класс UserServicesImpl реализует сервис для работы с пользователями.
 * Предоставляет методы для выполнения операций CRUD с данными пользователей.
 * Также включает методы для обновления статуса профиля пользователя,
 * обработки событий создания профиля и регистрации нового пользователя.
 */
@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserServicesImpl implements UserServices {

    final UserRepositories userRepositories;
    final RoleRepositories roleRepositories;
    final StreamBridge streamBridge;

    final ConfirmationCodeRepositories confirmationCodeRepositories;

    final KafkaTemplate<String, Object> kafkaTemplate;

    static final String TOPIC = "user-registration";



    /**
     * Получение списка всех пользователей.
     *
     * @return Список всех пользователей.
     */
    @Override
    public List<User> getAllUsers() {
        List<User> users = userRepositories.findAll();
        log.info("Get all users: {}", users);
        return users;
    }


    /**
     * Получение пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя.
     * @return Пользователь с указанным идентификатором или null, если пользователь не найден.
     */
    @Override
    public User getUser(final int id) {
        Optional<User> user = userRepositories.findById(id);
        log.info("Get user: {}", user.orElse(null));
        return user.orElse(null);
    }

    /**
     * Сохранение нового пользователя.
     *
     * @param user Пользователь для сохранения.
     */
    @Override
    public void saveUser(final User user) {
        List<Role> roles = new ArrayList<>();
        user.getRoles().forEach(role -> {
            roles.add(roleRepositories.findById(role.getId()).orElse(null));
        });
        user.setRoles(roles);
        User saved = userRepositories.save(user);
        log.info("Success save user: {}", saved);
    }


    /**
     * Удаление пользователя по его идентификатору.
     *
     * @param id Идентификатор пользователя для удаления.
     */
    @Override
    public void deleteUser(final int id) {
        if (userRepositories.existsById(id)) {
            userRepositories.deleteById(id);
            log.info("Success delete by id: {}", id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    /**
     * Обработка события создания профиля пользователя.
     *
     * @param userId  Идентификатор пользователя.
     * @param created Флаг успешного создания профиля.
     */
    @Override
    public void handleProfileCreationEvent(final int userId, final boolean created) {
        String status = created ? "CREATED" : "ERROR";
        updateProfileStatus(userId, status);
    }

    /**
     * Регистрация нового пользователя.
     *
     * @param username Имя пользователя.
     * @return Сохраненный пользователь.
     */
    public User registerUser(final String username) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setProfileStatus("CREATING");
        User savedUser = userRepositories.save(newUser);

        streamBridge.send("profile-create-out-0", savedUser.getId());
        log.info("Событие на создание профиля отправлено для пользователя: {}",
                savedUser.getUsername());

        return savedUser;
    }

    /**
     * Обновление статуса профиля пользователя.
     *
     * @param userId Идентификатор пользователя.
     * @param status Новый статус профиля.
     */
    public void updateProfileStatus(final int userId, final String status) {
        User user = userRepositories.findById(userId).orElseThrow(()
                -> new EntityNotFoundException("Пользователь не найден: " + userId));
        user.setProfileStatus(status);
        userRepositories.save(user);
        log.info("Статус профиля пользователя {} обновлен на: {}", userId, status);
    }

    /**
     * Поиск пользователя по email.
     *
     * @param email пользователя для поиска.
     * @return Найденный пользователь или null, если пользователь с указанным email не найден.
     */
    @Override
    public Optional<User> findByEmail(String email) {
        return userRepositories.findByEmail(email);
    }

    /**
     * Регистрация нового пользователя.
     *
     * @param userDTO объект содержащий данные нового пользователя.
     * @return ResponseEntity<UserDTO> объект, содержащий нового пользователя и статус HTTP
     */
    @Override
    public UserDTO registerUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setEmailVerified(false);

        User savedUser = userRepositories.save(user);
        ConfirmationCode confirmationCode = new ConfirmationCode();
        confirmationCode.setUser(savedUser);

        confirmationCodeRepositories.save(confirmationCode);

        kafkaTemplate.send(TOPIC, savedUser.getEmail() + ":" + confirmationCode.getCode());

        UserDTO registeredUserDTO = new UserDTO();
        registeredUserDTO.setId(savedUser.getId());
        registeredUserDTO.setEmail(savedUser.getEmail());
        registeredUserDTO.setEmailVerified(savedUser.isEmailVerified());

        return registeredUserDTO;
    }

    /**
     * Проверка кода подтверждения.
     *
     * @param code, код который отправлен пользователю для подтверждения учетной записи.
     * @return true, усли код действителен и false, если код недействителен или истек.
     */
    @Override
    public boolean verifyUser(String code) {
        Optional<ConfirmationCode> confirmationCodeOpt = confirmationCodeRepositories.findByCode(code);
        if (confirmationCodeOpt.isPresent()) {
            ConfirmationCode confirmationCode = confirmationCodeOpt.get();
            if (confirmationCode.getExpiryDate().isAfter(LocalDateTime.now())) {
                User user = confirmationCode.getUser();
                user.setEmailVerified(true);
                userRepositories.save(user);
                return true;
            } else {
                confirmationCodeRepositories.delete(confirmationCode);
            }
        }
        return false;
    }

    /**
     * Создание и отправка нового кода подтверждения.
     *
     * @param userId, идентификатор пользователя, для которго запрашивается code.
     * @return true, усли код действителен и false, если код недействителен или истек.
     */
    @Override
    public void renewConfirmationCode(Integer userId) {
        Optional<User> userOpt = userRepositories.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            ConfirmationCode existingCode = confirmationCodeRepositories.findByUser(user);
            if (existingCode != null && existingCode.getExpiryDate().isAfter(LocalDateTime.now())) {
                return;
            } if (existingCode != null) {
                confirmationCodeRepositories.delete(existingCode);
            }
            ConfirmationCode newCode = new ConfirmationCode();
            newCode.setUser(user);
            confirmationCodeRepositories.save(newCode);

            kafkaTemplate.send(TOPIC, user.getEmail() + ":" + newCode.getCode());
        }
    }

}

