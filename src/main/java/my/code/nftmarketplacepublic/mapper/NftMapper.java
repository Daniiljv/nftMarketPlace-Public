package my.code.nftmarketplacepublic.mapper;

import com.jdbc.nftmarketplace2.dtos.NftDto;
import com.jdbc.nftmarketplace2.entities.Nft;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface NftMapper {
    Nft toEntity(NftDto nftDto);

}
