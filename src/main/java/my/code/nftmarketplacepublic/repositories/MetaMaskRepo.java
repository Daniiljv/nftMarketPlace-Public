package my.code.nftmarketplacepublic.repositories;

import com.jdbc.nftmarketplace2.entities.MetaMaskWallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MetaMaskRepo extends JpaRepository<MetaMaskWallet, Long> {
    Optional<MetaMaskWallet> findByWalletAddress(String address);
}
