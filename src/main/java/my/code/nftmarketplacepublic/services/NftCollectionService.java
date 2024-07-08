package my.code.nftmarketplacepublic.services;

import com.jdbc.nftmarketplace2.entities.NftCollection;
import org.springframework.stereotype.Service;

@Service
public interface NftCollectionService {
    String create(String name);
    NftCollection findByName(String name);
    String addNft(String collectionName, Long nftId);
    String removeNft(String collectionName, Long nftId);
    String delete(String name);
}
