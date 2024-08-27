package business.hub.userservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс Role представляет сущность роли пользователя.
 * Описывает роль, которая может быть присвоена одному или нескольким пользователям.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name_role", length = 255)
    private String name;

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @JsonBackReference
    private List<User> users;

    /**
     * Конструктор с параметрами.
     *
     * @param idParam   Идентификатор роли.
     * @param nameParam Название роли.
     */
    public Role(final int idParam, final String nameParam) {
        this.id = idParam;
        this.name = nameParam;
    }

    /**
     * Метод для добавления пользователя к роли.
     *
     * @param user Пользователь, которого нужно добавить к роли.
     */
    void addUserToRoles(final User user) {
        if (users == null) {
            users = new ArrayList<>();
        }
        users.add(user);
    }

    /**
     * Переопределение метода equals для сравнения ролей.
     *
     * @param o Объект для сравнения.
     * @return true, если роли равны, иначе false.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Role role = (Role) o;
        return id == role.id && Objects.equals(name, role.name)
                && Objects.equals(users, role.users);
    }

    /**
     * Переопределение метода hashCode для генерации хэш-кода роли.
     *
     * @return Хэш-код роли.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, users);
    }
}
