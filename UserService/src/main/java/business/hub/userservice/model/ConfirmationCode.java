package business.hub.userservice.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@ToString
@AllArgsConstructor
@Table(name = "code")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfirmationCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="code_id")
    int id;

    @Column(name="confirmation_code")
    String code;

    @Column(name="expiry_date")
    LocalDateTime expiryDate;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user;

    public ConfirmationCode() {
        this.code = UUID.randomUUID().toString();
        this.expiryDate = LocalDateTime.now().plusHours(24);
    }
}

