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
public class UserRateDto {
    private String profileImage;

    private String username;

    private Double statistic;

    private Long countOfSoldNft;

    private BigDecimal transactionVolume;
}
