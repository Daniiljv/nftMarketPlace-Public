package my.code.nftmarketplacepublic.repositories;

import com.jdbc.nftmarketplace2.entities.NftCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NftCollectionRepo extends JpaRepository<NftCollection, Long> {
    Optional<NftCollection> findByCollectionNameAndDeletedAtIsNull(String name);
}
