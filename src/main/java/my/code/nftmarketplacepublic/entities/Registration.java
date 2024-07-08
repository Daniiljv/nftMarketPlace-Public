package my.code.nftmarketplacepublic.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nft_collection_seq_generator")
    @SequenceGenerator(name = "nft_collection_seq_generator", sequenceName = "nft_collection_seq", allocationSize = 1)
    private Long id;

    @Column(unique = true)
    @NotNull(message = "Имя пользователя не указанно")
    private String username;

    @Column(unique = true)
    @NotNull(message = "Почта не указана")
    private String email;

    @Size(min = 6, message = "Пароль должен содержать от 6 символов")
    @NotNull(message = "Пароль не должен быть пустым")
    private String password;

    @NotNull(message = "Код подтвеждения отсутствует")
    private Integer code;

    @NotNull(message = "Время экспирации не указанно")
    private Timestamp codeExpiredTime;

    @NotNull(message = "Состояние регистрации не указанно")
    private boolean isConfirmed;
}
