package my.code.nftmarketplacepublic.repositories;

import com.jdbc.nftmarketplace2.entities.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankCardRepo extends JpaRepository<BankCard, Long> {
    Optional<BankCard> findByCardNumber(String cardNumber);

}
