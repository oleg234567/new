package business.hub.userservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для настройки Swagger.
 */
@Configuration
public class SwaggerConfig {
    /**
     * Бин для конфигурации OpenAPI.
     *
     * @return объект OpenAPI с информацией о документации
     */
    @Bean
    public OpenAPI api() {
        return  new OpenAPI()
                .info(
                        new Info().title("UserService API")
                                .description("API для управления пользователями")
                                .version("1.0.0")
                );
    }
}
