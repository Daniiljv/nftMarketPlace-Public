package my.code.nftmarketplacepublic.repositories;

import com.jdbc.nftmarketplace2.entities.CryptoCurrency;
import com.jdbc.nftmarketplace2.enums.CryptoCurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CryptoCurrencyRepo extends JpaRepository<CryptoCurrency, Long> {
    CryptoCurrency findByCryptoCurrencyType(CryptoCurrencyType cryptoCurrencyType);
}
