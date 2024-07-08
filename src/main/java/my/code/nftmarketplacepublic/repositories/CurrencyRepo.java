package my.code.nftmarketplacepublic.repositories;

import com.jdbc.nftmarketplace2.entities.Currency;
import com.jdbc.nftmarketplace2.enums.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CurrencyRepo extends JpaRepository<Currency, Long> {
    Optional<Currency> findByIsoCode(CurrencyType isoCode);
}
