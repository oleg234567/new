package business.hub.userservice.controller;

import business.hub.userservice.model.User;
import business.hub.userservice.repositories.UserRepositories;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Контроллер для работы с дополнительными данными.
 * Обрабатывает HTTP-запросы, связанные с получением информации о пользователях по списку их идентификаторов.
 */
@RestController
@Tag(name="RESTful  контроллер для  работы  с  дополнительными данными")
@RequestMapping("/list")
public class AdditionalController {

    private final UserRepositories userRepositories;

    /**
     * Конструктор контроллера с инъекцией зависимостей.
     *
     * @param userRepositoriesParam Репозиторий пользователей.
     */
    @Autowired
    public AdditionalController(final UserRepositories userRepositoriesParam) {
        this.userRepositories = userRepositoriesParam;
    }

    /**
     * Получение списка email по списку идентификаторов пользователей.
     *
     * @param listId Список идентификаторов пользователей.
     * @return Список email пользователей.
     */
    @PostMapping()
    @Operation(
            summary = "Получение списка email",
            description = "Возвращает список email по списку идентификаторов пользователей"
    )
    List<String> getListByListUser(final @RequestBody List<Integer> listId) {
        List<String> listEmail = new ArrayList<>();
        listId.forEach(id -> listEmail.add(
                userRepositories.findById(id).orElse(new User()).getEmail()));
        return listEmail;
    }

}
