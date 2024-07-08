package my.code.nftmarketplacepublic.entities;

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
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq_generator")
    @SequenceGenerator(name = "transaction_seq_generator", sequenceName = "transaction_seq", allocationSize = 1)
    private Long id;

    @NotNull(message = "Покупатель не может быть пустым")
    private Long buyerId;

    @NotNull(message = "Продавец не может быть пустым")
    private Long sellerId;

    @NotNull(message = "Объем транзакции не может быть пустым")
    private BigDecimal volume;

    @NotNull(message = "Дата не должна быть пустой")
    private Timestamp date;
}
