package my.code.nftmarketplacepublic.repositories;

import com.jdbc.nftmarketplace2.entities.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface RegistrationRepo extends JpaRepository<Registration, Long> {
    Optional<Registration> findByEmail(String email);
    Optional<Registration> findByUsername(String username);
}
