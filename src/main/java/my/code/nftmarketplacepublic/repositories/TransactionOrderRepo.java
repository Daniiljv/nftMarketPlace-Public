package my.code.nftmarketplacepublic.repositories;

import com.jdbc.nftmarketplace2.entities.TransactionOrders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionOrderRepo extends JpaRepository<TransactionOrders , Long> {

    List<TransactionOrders> findAllByOwnerId(Long ownerId);
}
