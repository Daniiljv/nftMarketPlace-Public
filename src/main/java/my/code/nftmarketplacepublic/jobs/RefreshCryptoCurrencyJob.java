package my.code.nftmarketplacepublic.jobs;

import com.jdbc.nftmarketplace2.services.CryptoCurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class RefreshCryptoCurrencyJob {

    private final CryptoCurrencyService cryptoCurrencyService;

    @Scheduled(fixedDelay = 15000)
    public void refreshRates() {
        cryptoCurrencyService.refreshPrice();
    }
}
