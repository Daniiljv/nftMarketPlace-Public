package my.code.nftmarketplacepublic.repositories;

import com.jdbc.nftmarketplace2.entities.TempResetCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TempResetCodeRepo extends JpaRepository<TempResetCode, Long> {

    Optional<TempResetCode> findByEmail(String email);

}
