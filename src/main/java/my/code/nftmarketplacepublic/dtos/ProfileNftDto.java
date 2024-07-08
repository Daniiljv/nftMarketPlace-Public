package my.code.nftmarketplacepublic.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileNftDto {

    private Long id;

    private String name;

    private BigDecimal dollarPrice;

    private BigDecimal ethereumPrice;

    private String nftImage;

    private String creatorUsername;

    private String creatorProfileImage;

}
