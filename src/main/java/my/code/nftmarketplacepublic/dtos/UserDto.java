package my.code.nftmarketplacepublic.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    private String username;

    private String email;

    private String password;

    private Long countOfSoldNft;

    private BigDecimal transactionVolume;

    private String profileImage;

    private List<Long> followersIds;

    private Timestamp createdAt;

    private Timestamp lastUpdatedAt;

    private Timestamp deletedAt;
}
