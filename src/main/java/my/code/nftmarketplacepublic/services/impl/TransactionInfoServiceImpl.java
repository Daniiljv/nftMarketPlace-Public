package my.code.nftmarketplacepublic.services.impl;

import com.jdbc.nftmarketplace2.exceptions.*;
import com.jdbc.nftmarketplace2.repositories.*;
import com.jdbc.nftmarketplace2.services.TransactionInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionInfoServiceImpl implements TransactionInfoService {

    private final UserRepo userRepo;
    private final TransactionRepo transactionRepo;

    public Double getUserStatisticForDays(Long userId, Long days) {

        double volumeOfAllTransactions = getVolumeAllUserTransactions(userId).doubleValue();
        double volumeOfTransactionsForDays = getVolumeUserTransactionsForDays(userId, days).doubleValue();

        long countOfAllTransactions = getCountAllUserTransactions(userId);
        long countOfTransactionsForDays = getCountUserTransactionsForDays(userId, days);

        double statistic;

        if(volumeOfAllTransactions != 0){
            statistic =((volumeOfTransactionsForDays / countOfTransactionsForDays) / 100) -
                    ((volumeOfAllTransactions - volumeOfTransactionsForDays) /
                    (countOfAllTransactions - countOfTransactionsForDays) / 100);
        }
        else {
            return 0.0;
        }

        if (Double.isFinite(statistic)) {
            BigDecimal bd = new BigDecimal(statistic);
            bd = bd.setScale(3, RoundingMode.DOWN);
            return bd.doubleValue();
        }
        else {
            return 0.0;
        }
    }

    @Override
    public Long getCountAllUserTransactions(Long userId) {
        userRepo.findByDeletedAtIsNullAndId(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден!"));

        return transactionRepo.getCountOfAllTransactionsWithUser(userId)
                .orElse(0L);

    }

    @Override
    public BigDecimal getVolumeAllUserTransactions(Long userId) {
        userRepo.findByDeletedAtIsNullAndId(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден!"));

        return transactionRepo.getVolumeOfAllTransactionsWithUser(userId)
                .orElse(BigDecimal.valueOf(0));

    }

    @Override
    public Long getCountUserTransactionsForDays(Long userId, Long daysCount) {
        userRepo.findByDeletedAtIsNullAndId(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден!"));

        Date date = new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * daysCount));

        Long countOfTransactions = transactionRepo.getCountOfTransactionsWithUserForDays(userId, date)
                .orElse(1L);

        return countOfTransactions >= 1L ? countOfTransactions : 1L;
    }

    @Override
    public BigDecimal getVolumeUserTransactionsForDays(Long userId, Long daysCount) {
        userRepo.findByDeletedAtIsNullAndId(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с id " + userId + " не найден!"));

        Date date = new Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * daysCount));

        return transactionRepo.getVolumeOfTransactionsWithUserForDays(userId, date)
                .orElse(BigDecimal.valueOf(1));

    }
}


