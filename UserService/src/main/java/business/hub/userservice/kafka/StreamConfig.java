package business.hub.userservice.kafka;

import business.hub.userservice.service.UserServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

/**
 * Конфигурационный класс для настройки потоков данных.
 * Определяет бин, который представляет собой потребителя событий создания профилей пользователей.
 */
@Configuration
public class StreamConfig {
    private final UserServices userService;

    /**
     * Конструктор с инъекцией зависимостей.
     *
     * @param userServiceParam Сервис пользователей.
     */
    public StreamConfig(final UserServices userServiceParam) {
        this.userService = userServiceParam;
    }
    /**
     * Создание потребителя событий создания профиля.
     *
     * @return Потребитель, который обрабатывает события создания профиля пользователя.
     */
    @Bean
    public Consumer<ProfileCreationEvent> profileCreated() {
        return event -> userService.handleProfileCreationEvent(
                event.getUserId(), event.isCreated());
    }
}
