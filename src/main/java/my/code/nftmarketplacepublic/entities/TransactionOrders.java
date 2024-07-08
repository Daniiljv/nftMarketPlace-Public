package my.code.nftmarketplacepublic.entities;

import com.jdbc.nftmarketplace2.enums.MeansOfPayment;
import com.jdbc.nftmarketplace2.enums.OrderStatus;
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
public class TransactionOrders {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_order_seq_generator")
    @SequenceGenerator(name = "transaction_order_seq_generator", sequenceName = "transaction_order_seq", allocationSize = 1)
    private Long id;

    @NotNull(message = "Владелец ордера не указан")
    private Long ownerId;

    private Long transactionId;

    @NotNull(message = "Способ оплаты не указан")
    @Enumerated(value = EnumType.STRING)
    private MeansOfPayment meansOfPayment;

    @NotNull(message = "Состояние ордера не указано")
    @Enumerated(value = EnumType.STRING)
    private OrderStatus orderStatus;

    private String description;

    @NotNull(message = "Время создания ордера не указано")
    private Timestamp createdAt;

    private Timestamp processedAt;
}
