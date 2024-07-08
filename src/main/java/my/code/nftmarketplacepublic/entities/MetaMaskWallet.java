package my.code.nftmarketplacepublic.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MetaMaskWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meta_mask_wallet_seq_generator")
    @SequenceGenerator(name = "meta_mask_wallet_seq_generator", sequenceName = "meta_mask_wallet_seq", allocationSize = 1)
    private Long id;

    @NotNull(message = "Адрес кошелька не может быть пустым")
    private String walletAddress;

    @NotNull(message = "Пароль не может быть пустым")
    private String password;

    @NotNull(message = "Баланс обязателен")
    private BigDecimal balance;
}
