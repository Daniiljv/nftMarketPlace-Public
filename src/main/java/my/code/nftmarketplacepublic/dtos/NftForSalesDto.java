package my.code.nftmarketplacepublic.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NftForSalesDto {
    private Long id;

    private String nftImage;

    private Long ownerId;

    private String name;

    private String ownerUsername;

    private BigDecimal dollarPrice;

    private BigDecimal ethereumPrice;
}
