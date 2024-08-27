package business.hub.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс User представляет сущность пользователя.
 * Описывает основные атрибуты пользователя, такие как имя, фамилия, электронная почта, дата рождения и роли.
 */
@Entity
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
@JsonIgnoreProperties("roles, ticket")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 255, nullable = false)
    int id;

    @Column(name = "username", length = 255)
    @NotEmpty(message = "not NULL")
    String username;

    @Column(name = "password", length = 255)
    String password;

    @Column(name = "name", length = 255)
    @Pattern(regexp = "^[a-zA-Z]+$", message = "error firstName")
    @NotEmpty(message = "not NULL")
    String firstName;

    @Column(name = "last_name", length = 255)
    @Pattern(regexp = "^[a-zA-Z]+$", message = "error lastName")
    @NotEmpty(message = "not NULL")
    String lastName;

    @Column(name = "email", length = 255)
    @NotEmpty(message = "not NULL")
    @Email(message = "error e-mail")
    String email;

    boolean emailVerified = false;

    @Column(name = "dateBirth", length = 255)
    @NotNull(message = "Date of birth cannot be null")
    LocalDate dateBirth;

    @Column(name = "profileStatus", length = 255)
    @NotEmpty(message = "not NULL")
    String profileStatus;

    @OneToMany(mappedBy = "user")
    List <Ticket> ticket;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    List<Role> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    ConfirmationCode confirmationCode;

    /**
     * Конструктор с параметрами.
     *
     * @param usernameParam      Имя пользователя.
     * @param passwordParam      Пароль пользователя.
     * @param firstNameParam     Имя пользователя.
     * @param lastNameParam      Фамилия пользователя.
     * @param emailParam         Электронная почта пользователя.
     * @param dateBirthParam     Дата рождения пользователя.
     * @param profileStatusParam Статус профиля пользователя.
     */
    public User(final String usernameParam, final String passwordParam,
                final String firstNameParam, final String lastNameParam,
                final String emailParam, final LocalDate dateBirthParam,
                final String profileStatusParam) {
        this.username = usernameParam;
        this.password = passwordParam;
        this.firstName = firstNameParam;
        this.lastName = lastNameParam;
        this.email = emailParam;
        this.dateBirth = dateBirthParam;
        this.profileStatus = profileStatusParam;
    }

    /**
     * Метод для добавления роли к пользователю.
     *
     * @param role Роль, которую нужно добавить пользователю.
     */
    public void addRoleUser(final Role role) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        roles.add(role);
    }

    /**
     * Переопределение метода equals для сравнения пользователей.
     *
     * @param o Объект для сравнения.
     * @return true, если пользователи равны, иначе false.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return id == user.id && Objects.equals(username, user.username)
                && Objects.equals(password, user.password)
                && Objects.equals(firstName, user.firstName)
                && Objects.equals(lastName, user.lastName)
                && Objects.equals(email, user.email)
                && Objects.equals(roles, user.roles);
    }
    /**
     * Переопределение метода hashCode для генерации хэш-кода пользователя.
     *
     * @return Хэш-код пользователя.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, username,
                password, firstName,
                lastName, email,
                roles, profileStatus);
    }
}

