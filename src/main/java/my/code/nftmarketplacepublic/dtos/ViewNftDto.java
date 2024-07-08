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
public class ViewNftDto {

    private Long id;

    private String nftImage;

    private String name;

    private Timestamp createdAt;

    private String creatorUsername;

    private Long ownerId;

    private String description;

    private BigDecimal dollarPrice;

    private BigDecimal ethereumPrice;

}
