package my.code.nftmarketplacepublic.entities;

import com.jdbc.nftmarketplace2.enums.CryptoCurrencyType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CryptoCurrency {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "crypto_currency_seq_generator")
    @SequenceGenerator(name = "crypto_currency_seq_generator", sequenceName = "crypto_currency_seq", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CryptoCurrencyType cryptoCurrencyType;

    @NotNull(message = "Цена не можетбыть пустой")
    private BigDecimal price;

    @NotNull
    private Timestamp lastUpdatedAt;
}
