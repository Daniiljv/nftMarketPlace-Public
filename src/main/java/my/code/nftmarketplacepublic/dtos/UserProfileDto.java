package my.code.nftmarketplacepublic.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {

    private Long id;

    private String username;

    private Long countOfSoldNft;

    private BigDecimal transactionVolume;

    private String profileImage;

    private Long followersCount;

    private List<ProfileNftDto> createdNfts;

    private List<ProfileNftDto> ownedNfts;

}
