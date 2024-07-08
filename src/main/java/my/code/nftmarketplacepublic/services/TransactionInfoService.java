package my.code.nftmarketplacepublic.services;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface TransactionInfoService {
    Long getCountAllUserTransactions(Long userId);
    Double getUserStatisticForDays(Long id, Long days);
    BigDecimal getVolumeAllUserTransactions(Long userId);
    Long getCountUserTransactionsForDays(Long userId, Long daysCount);
    BigDecimal getVolumeUserTransactionsForDays(Long userId, Long daysCount);

}
