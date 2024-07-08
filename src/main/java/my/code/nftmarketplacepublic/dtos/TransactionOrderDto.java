package my.code.nftmarketplacepublic.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jdbc.nftmarketplace2.enums.MeansOfPayment;
import com.jdbc.nftmarketplace2.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionOrderDto {

    private Long id;

    private Long ownerId;

    private MeansOfPayment meansOfPayment;

    private OrderStatus status;

    private String description;

    private Long transactionId;

    private Timestamp createdAt;
}
