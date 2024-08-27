package business.hub.userservice.kafka;

import business.hub.userservice.service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.function.Consumer;

/**
 * Конфигурационный класс для обработки событий создания профилей пользователей.
 * Определяет бин, который представляет собой потребителя событий создания профиля пользователя.
 */
@Configuration
public class ProfileCreationListener {
    @Autowired
    private UserServices userService;

    /**
     * Создание потребителя событий создания профиля.
     *
     * @return Потребитель, который обновляет статус профиля пользователя на основе полученного события.
     */
    @Bean
    public Consumer<ProfileCreationEvent> processProfileCreation() {
        return event -> userService.updateProfileStatus(
                event.getUserId(), event.isCreated() ? "CREATED" : "CREATING");
    }
}
