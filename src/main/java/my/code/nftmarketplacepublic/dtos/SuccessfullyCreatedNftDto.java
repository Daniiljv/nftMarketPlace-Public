package my.code.nftmarketplacepublic.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SuccessfullyCreatedNftDto {
    private Long id;

    private String name;

    private BigDecimal dollarPrice;

    private BigDecimal ethereumPrice;

    private String nftImage;

    private String description;

    private Long creatorId;

    private Long ownerId;

    private Timestamp createdAt;
}
