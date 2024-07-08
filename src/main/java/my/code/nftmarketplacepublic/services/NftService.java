package my.code.nftmarketplacepublic.services;


import com.jdbc.nftmarketplace2.dtos.*;
import com.jdbc.nftmarketplace2.entities.Nft;
import org.springframework.stereotype.Service;

import java.util.List;
@Service

public interface NftService {
    SuccessfullyCreatedNftDto create(CreateNftDto nftDtoToCreate);
    Nft findById(Long id);
    NftDto update(NftDto nftDtoToUpdate);
    String delete(Long id);
    String pushNftToMarket(Long nftId);
    String cancelSelling(Long nftId);
    List<NftForSalesDto> getNftsForSale();
    ViewNftDto viewNftById(Long id);
    void save(Nft nft);
}
