package my.code.nftmarketplacepublic.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TempResetCode {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reset_code_generator")
    @SequenceGenerator(name = "reset_code_generator", sequenceName = "reset_code_seq", allocationSize = 1)
    private Long id;

    @NotNull(message = "Почта не указана")
    private String email;

    @NotNull(message = "Проверочный код не указан")
    private Integer code;

    @NotNull(message = "Время экспирации кода не указанно")
    private Timestamp expiredTime;
}
