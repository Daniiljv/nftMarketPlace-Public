package my.code.nftmarketplacepublic.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.FetchType.EAGER;
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq_generator")
    @SequenceGenerator(name = "users_seq_generator", sequenceName = "users_seq", allocationSize = 1)
    private Long id;

    @Size(min = 3, max = 20, message = "Имя пользователя должно быть от 3 до 20 символов")
    @Column(unique = true)
    @NotNull(message = "")
    private String username;

    @Email(message = "Почта должна быть валидной")
    @Column(unique = true)
    @NotNull(message = "")
    private String email;

    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    @NotNull(message = "Пароль не указан")
    private String password;

    @NotNull(message = "Колличество созданных нфт не указанно")
    private Long countOfSoldNft;

    @NotNull(message = "Объем транзакций не указан")
    private BigDecimal transactionVolume;

    @NotNull(message = "Фото профиля не указанно")
    private String profileImage;

    private List<Long> followersIds;

    @OneToOne
    private MetaMaskWallet metaMaskWallet;

    @OneToOne
    private BankCard bankCard;

    @NotNull(message = "Время создания не указанно")
    private Timestamp createdAt;

    private Timestamp lastUpdatedAt;

    private Timestamp deletedAt;

    @ManyToMany(fetch = EAGER)
    private Set<Role> roles = new HashSet<>();
}
