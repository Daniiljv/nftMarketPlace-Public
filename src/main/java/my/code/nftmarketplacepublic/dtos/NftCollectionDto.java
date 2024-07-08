package my.code.nftmarketplacepublic.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NftCollectionDto {
    private Long id;

    private Long ownerId;

    private String collectionName;

    private List<Long> nftIds;
}
