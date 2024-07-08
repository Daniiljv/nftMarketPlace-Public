package my.code.nftmarketplacepublic.repositories;

import com.jdbc.nftmarketplace2.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Optional;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    @Query("SELECT count(t) " +
            "FROM Transaction t " +
            "WHERE t.buyerId = :userId " +
            "OR t.sellerId = :userId ")
    Optional<Long> getCountOfAllTransactionsWithUser(@Param("userId") Long userId);

    @Query("SELECT sum(t.volume) " +
            "FROM Transaction t " +
            "WHERE t.buyerId = :userId " +
            "OR t.sellerId = :userId ")
    Optional<BigDecimal> getVolumeOfAllTransactionsWithUser(@Param("userId") Long userId);

    @Query("SELECT count(t) " +
            "FROM Transaction t " +
            "WHERE t.buyerId = :userId " +
            "OR t.sellerId = :userId " +
            "AND t.date >= :date")
    Optional<Long> getCountOfTransactionsWithUserForDays(@Param("userId") Long userId,
                                          @Param("date") Date date);

    @Query("SELECT sum(t.volume) " +
            "FROM Transaction t " +
            "WHERE t.buyerId = :userId " +
            "OR t.sellerId = :userId " +
            "AND t.date >= :date")
    Optional<BigDecimal> getVolumeOfTransactionsWithUserForDays(@Param("userId") Long userId,
                                          @Param("date") Date date);

}
