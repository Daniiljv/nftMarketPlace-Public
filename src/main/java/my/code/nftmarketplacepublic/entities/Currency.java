package my.code.nftmarketplacepublic.entities;

import com.jdbc.nftmarketplace2.enums.CurrencyType;
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
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currency_seq_generator")
    @SequenceGenerator(name = "currency_seq_generator", sequenceName = "currency_seq", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CurrencyType isoCode;

    @NotNull
    private Integer nominal;

    @NotNull
    private BigDecimal value;

    @NotNull
    private Timestamp refreshDate;
}
