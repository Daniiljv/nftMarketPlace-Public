package my.code.nftmarketplacepublic.repositories;


import com.jdbc.nftmarketplace2.entities.Nft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface NftRepo extends JpaRepository<Nft, Long> {
    Optional<Nft> findByDeletedAtIsNullAndId(Long id);
    Optional<List<Nft>> findAllByDeletedAtIsNullAndOnSaleIsTrue();
    List<Nft> findAllByDeletedAtIsNullAndCreatorId(Long creatorId);
    List<Nft> findAllByDeletedAtIsNullAndOwnerId(Long ownerId);
}
