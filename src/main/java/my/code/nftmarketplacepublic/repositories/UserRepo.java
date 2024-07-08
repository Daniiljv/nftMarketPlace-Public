package my.code.nftmarketplacepublic.repositories;

import com.jdbc.nftmarketplace2.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<List<User>> findAllByDeletedAtIsNull();
    Optional<User> findByDeletedAtIsNullAndId(Long id);
    Optional<User> findByEmailAndDeletedAtIsNull(String email);
    User findUserById(Long id);

}